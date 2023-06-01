package com.bananaarch.minecraftevacuation.bot.ML;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.Bot;
import com.bananaarch.minecraftevacuation.bot.BotManager;
import com.bananaarch.minecraftevacuation.bot.ML.NN.Action;
import com.bananaarch.minecraftevacuation.bot.ML.NN.BotState;
import com.bananaarch.minecraftevacuation.bot.ML.NN.Environment;
import com.bananaarch.minecraftevacuation.tasks.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.IOException;
import java.util.Set;

public class BotAgent {

    private final Plugin plugin = MinecraftEvacuation.getInstance();
    private final BotManager botManager;
    private final TaskManager taskManager = MinecraftEvacuation.getInstance().getTaskManager();
    private int tickTaskId;
    private int trainTaskId;

    public BotAgent(BotManager botManager) {
        this.botManager = botManager;
        this.tickTaskId = -1;
        this.trainTaskId = -1;
        setEnabled(true);
    }

    // enable botAgent, tickTask
    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.tickTaskId = taskManager.scheduleRepeatingTask(this::tick, 0, 1);
        } else {
            taskManager.cancelTask(tickTaskId);
        }
    }

    // enable training, trainTaskId
    public void setTrainStatus(boolean enabled) {

        if (enabled) {
            trainBot();
        } else {
            taskManager.cancelTask(trainTaskId);
        }
    }

    public boolean getTrainStatus() {
        return taskManager.hasTask(trainTaskId);
    }

    private void tick() {
        Set<Bot> bots = botManager.getBots();
        botManager.updateVisibility();
        bots.forEach(Bot::tick);
    }

    public void trainBot() {
        Set<Bot> bots = botManager.getBots();

        final String randomNetworkName = "network-" + System.currentTimeMillis() + ".zip";

        this.trainTaskId =
            taskManager.scheduleRepeatingTask(
                () -> {

                    bots.forEach(bot -> {
                        Vector vel = new Vector(2 * Math.random() - 1, 0, 2 * Math.random() - 1);
                        bot.setVelocity(vel.multiply(.05));

                        final Environment mdp = new Environment(bot);
                        final QLearningDiscreteDense<BotState> dql = new QLearningDiscreteDense<>(
                                mdp,
                                NetworkUtil.buildDQNFactory(),
                                NetworkUtil.buildConfig()
                        );

                        dql.train();
                        mdp.close();

                        try {
                            dql.getNeuralNet().save(randomNetworkName);
                        } catch (IOException e) {
                            throw new RuntimeException("Error with NN saving");
                        }

                    });


                }, 0, 1
            );

        setTrainStatus(false);

    }

    private void evaluateNetwork(Bot bot, String randomNetworkName) {
        final MultiLayerNetwork multiLayerNetwork = NetworkUtil.loadNetwork(randomNetworkName);
        final int EPOCHS = 30;

        for (int i = 0; i < EPOCHS; i++) {
            while(getTrainStatus()) {
                try {
                    BotState botState = new BotState(bot.getState());
                    INDArray output = multiLayerNetwork.output(botState.getMatrix(), false);
                    double[] data = output.data().asDouble();
                    //TODO: DELETE LATER
                    if (data.length != Action.values().length)
                        throw new Exception();

                    int maxValueIndex = NetworkUtil.getMaxValueIndex(data);

                    Action action = Action.getActionByIndex(maxValueIndex);
                    Location newLocation = bot.getLocation().add(action.getVector());

                    bot.teleportTo(newLocation.getX(), newLocation.getY(), newLocation.getZ());

//                    bot.walk(Action.getActionByIndex(maxValueIndex).getVector());
                    //TODO: might need to change walk method and create walkNorth, walkSouth, jumpNorth, etc. methods

                    //TODO: maybe wait?
                } catch (Exception e) {
                    System.out.println("Training has stopped");
                    ChatUtil.broadcastError("Training has stopped. Error has occurred during training");
                    setTrainStatus(false);
                    return;
                }
            }

            // reset the bot
            bot.resetLocation();

        }
    }

    public int rewardFunction(Bot bot, Action movement) {
        Location botLocation = bot.getFlooredLocation();
        Location targetLocation = bot.getFlooredTargetLocation();
        Location botMovingBlockLocation = bot.getFlooredLocation().add(movement.getVector());
        Material blockMaterial = botMovingBlockLocation.getBlock().getType();

        if (botLocation.equals(targetLocation.add(new Vector(0, 1, 0)))) // if reached target Destination
            return 100;
        if (bot.containsInPastLocations(botLocation)) // if already passed
            return -5;
        if (!Materials.AIR.contains(blockMaterial)) // if wall
            return -100;
        return -1;
    }

}
