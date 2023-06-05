package com.bananaarch.minecraftevacuation.bot.AI;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.Bot;
import com.bananaarch.minecraftevacuation.bot.BotManager;
import com.bananaarch.minecraftevacuation.bot.AI.NN.Action;
import com.bananaarch.minecraftevacuation.bot.AI.NN.BotState;
import com.bananaarch.minecraftevacuation.bot.AI.NN.Environment;
import com.bananaarch.minecraftevacuation.bot.utils.ChatUtil;
import com.bananaarch.minecraftevacuation.bot.utils.NetworkUtil;
import com.bananaarch.minecraftevacuation.tasks.TaskManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
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
    private int evaluateTaskId;

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

    private void tick() {
        Set<Bot> bots = botManager.getBots();
        botManager.updateVisibility();
        bots.forEach(Bot::tick);
    }

    public boolean isTraining() {
        return taskManager.containsTask(trainTaskId);
    }

    // enable training, trainTaskId
    public void stopTraining() {
        if (!isTraining()) {
            ChatUtil.broadcastError("The bot is currently not training");
            return;
        }

        taskManager.cancelTask(trainTaskId);
        ChatUtil.broadcastMessage(ChatColor.RED + "Stopping the " + ChatColor.DARK_RED + "training" + ChatColor.RED + " process");
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

        ChatUtil.broadcastMessage(ChatColor.GRAY + "Starting the " + ChatColor.GREEN + "training" + ChatColor.GRAY + " process");


        final Environment mdp = new Environment(bot);
        QLearningDiscreteDense dql = new QLearningDiscreteDense<>(
                mdp,
                NetworkUtil.buildDQNDenseNetworkConfiguration(),
                NetworkUtil.buildConfig()
        );

        /*
        Has to be async task or will not clog up synchronous scheduler
         */
        this.trainTaskId = taskManager.runAsyncTask(() -> {
            dql.train();
            mdp.close();

            ChatUtil.broadcastMessage(ChatColor.GREEN + "Successfully finished training");

            try {
                dql.getNeuralNet().save(randomNetworkName);
            } catch (IOException e) {
                ChatUtil.broadcastError("Error with Neural Network saving.");
                throw new RuntimeException("Error with NN saving");
            }

            bot.resetLocation();
            evaluateNetwork(bot, randomNetworkName);


            ChatUtil.broadcastMessage(ChatColor.GRAY + "Successfully saved the " + ChatColor.GREEN + "model");
            taskManager.cancelTask(trainTaskId);

        });

    }


    public boolean isEvaluating() {
        return taskManager.containsTask(evaluateTaskId);
    }

    public void stopEvaluating() {
        if (!isEvaluating()) {
            ChatUtil.broadcastError("The bot is currently not replaying the model");
            return;
        }

        ChatUtil.broadcastMessage(ChatColor.RED + "Stopping the " + ChatColor.DARK_RED + "replaying" + ChatColor.RED + " process");
        taskManager.cancelTask(evaluateTaskId);

    }

    private void evaluateNetwork(Bot bot, String randomNetworkName) {
        final MultiLayerNetwork multiLayerNetwork = NetworkUtil.loadNetwork(randomNetworkName);

        taskManager.scheduleRepeatingTask(() -> {

            try {

                if (bot.standingOnTargetBlock())
                    stopEvaluating();

                BotState botState = new BotState(bot.getState());
                INDArray output = multiLayerNetwork.output(botState.getMatrix(), false);
                double[] data = output.data().asDouble();
                //TODO: DELETE LATER
                if (data.length != Action.values().length)
                    throw new ArrayIndexOutOfBoundsException();

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
                stopEvaluating();
            }

        }, 0, 1);

    }

}
