package com.bananaarch.minecraftevacuation.bot.ML;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.Bot;
import com.bananaarch.minecraftevacuation.bot.BotManager;
import com.bananaarch.minecraftevacuation.bot.ML.NN.Action;
import com.bananaarch.minecraftevacuation.bot.ML.NN.BotState;
import com.bananaarch.minecraftevacuation.bot.ML.NN.Environment;
import com.bananaarch.minecraftevacuation.tasks.TaskManager;
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
//        if (enabled) {
//            this.tickTaskId = taskManager.scheduleRepeatingTask(this::tick, 0, 1);
//        } else {
//            taskManager.cancelTask(tickTaskId);
//        }
    }

    // enable training, trainTaskId
    public void stopTraining() {
        if (!getTrainStatus()) {
            ChatUtil.broadcastError("The bot is currently not training");
            return;
        }

        ChatUtil.broadcastMessage(ChatColor.RED + "Stopping the " + ChatColor.DARK_RED + "training" + ChatColor.RED + " process");
        taskManager.cancelTask(trainTaskId);
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

        Bot bot = bots.iterator().next();

        if (bot == null) {
            ChatUtil.broadcastError("There are no bots");
            return;
        }
        if (bot.getTargetLocation() == null) {
            ChatUtil.broadcastError("Target location has not been initialized");
            return;
        }

        final String randomNetworkName = "network-" + System.currentTimeMillis() + ".zip";

        this.trainTaskId = taskManager.runTask(() -> {

            ChatUtil.broadcastMessage(ChatColor.GRAY + "Starting the " + ChatColor.GREEN + "training" + ChatColor.GRAY + " process");

            try {
                final Environment mdp = new Environment(bot);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            QLearningDiscreteDense dql = new QLearningDiscreteDense<>(
//                    mdp,
//                    NetworkUtil.buildDQNFactory(),
//                    NetworkUtil.buildConfig()
//            );
//
//            dql.train();
//            mdp.close();
//
//            try {
//                dql.getNeuralNet().save(randomNetworkName);
//            } catch (IOException e) {
//                ChatUtil.broadcastError("Error with Neural Network saving.");
//                throw new RuntimeException("Error with NN saving");
//            }
//
//            bot.resetLocation();
//            evaluateNetwork(bot, randomNetworkName);
//

            ChatUtil.broadcastMessage(ChatColor.GRAY + "Successfully finished the " + ChatColor.GREEN + "training" + ChatColor.GRAY + " process");
            taskManager.cancelTask(trainTaskId);

        });

    }

    private void evaluateNetwork(Bot bot, String randomNetworkName) {
        final MultiLayerNetwork multiLayerNetwork = NetworkUtil.loadNetwork(randomNetworkName);

        while(!bot.standingOnTargetBlock()) {
            try {
                BotState botState = new BotState(bot.getState());
                INDArray output = multiLayerNetwork.output(botState.getMatrix(), false);
                double[] data = output.data().asDouble();
                //TODO: DELETE LATER
//                    if (data.length != Action.values().length)
//                        throw new Exception();

                int maxValueIndex = NetworkUtil.getMaxValueIndex(data);

                Action action = Action.getActionByIndex(maxValueIndex);
                Location newLocation = bot.getLocation().add(action.getVector());

                bot.teleportTo(newLocation.getX(), newLocation.getY(), newLocation.getZ());

//                    bot.walk(Action.getActionByIndex(maxValueIndex).getVector());
                //TODO: might need to change walk method and create walkNorth, walkSouth, jumpNorth, etc. methods

                //TODO: maybe add wait to see more clearly?
            } catch (Exception e) {
                System.out.println(e);
                ChatUtil.broadcastError("Error with evaluating the network.");
                ChatUtil.broadcastError(e.toString());
                return;
            }
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
            return -1;
        if (!Materials.AIR.contains(blockMaterial)) // if wall
            return -25;
        return 1;
    }

}
