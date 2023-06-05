package com.bananaarch.minecraftevacuation.bot.AI.NN;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.AI.BotAgent;
import com.bananaarch.minecraftevacuation.bot.utils.ChatUtil;
import com.bananaarch.minecraftevacuation.bot.Bot;
import com.bananaarch.minecraftevacuation.bot.utils.MaterialUtil;
import com.bananaarch.minecraftevacuation.tasks.TaskManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.ArrayObservationSpace;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;

public class Environment implements MDP<BotState, Integer, DiscreteSpace> {

    private final DiscreteSpace actionSpace = new DiscreteSpace(4);
    private final TaskManager taskManager = MinecraftEvacuation.getInstance().getTaskManager();
    private final BotAgent botAgent = MinecraftEvacuation.getInstance().getBotManager().getBotAgent();
    private final ArrayObservationSpace<BotState> botStateArrayObservationSpace = new ArrayObservationSpace<>(new int[]{Bot.getObservationSize()});
    private final Bot bot;

    public Environment(Bot bot) {
        this.bot = bot;
    }


    @Override
    public ObservationSpace<BotState> getObservationSpace() {
        return botStateArrayObservationSpace;
    }

    @Override
    public DiscreteSpace getActionSpace() {
        return this.actionSpace;
    }

    @Override
    public BotState reset() {
        taskManager.runTask(() -> bot.resetLocation());
        return new BotState(bot.getState());
    }

    @Override
    public void close() {}

    @Override
    public StepReply<BotState> step(Integer actionIndex) {

        Action action = Action.getActionByIndex(actionIndex);
        ChatUtil.broadcastMessage(ChatColor.YELLOW + action.toString());

        double reward = rewardFunction(action);
        ChatUtil.broadcastMessage(ChatColor.YELLOW + "Reward: " + reward);

        taskManager.runTask(() -> bot.walkByAction(action));

        BotState botState = new BotState(bot.getState());

        return new StepReply<>(
                botState,
                reward,
                isDone(),
                "MinecraftEvacuationDl4j"
        );
    }

    public int rewardFunction(Action movement) {
        Location botLocation = bot.getFlooredLocation();
        Location targetLocation = bot.getFlooredTargetLocation();
        Location botMovingBlockLocation = bot.getFlooredLocation().add(movement.getVector());
        Material blockMaterial = botMovingBlockLocation.getBlock().getType();

        if (bot.standingOnTargetBlock()) // if reached target Destination
            return 500;
        if (!MaterialUtil.AIR.contains(blockMaterial)) // if wall
            return -25;
        if (bot.containsInPastLocations(botMovingBlockLocation)) // if already passed
            return -10;
        if (botLocation.distanceSquared(targetLocation) > botMovingBlockLocation.distanceSquared(targetLocation)) // if moving towards target
            return 25;
        else return -1;

    }

    @Override
    public boolean isDone() {
        return bot.standingOnTargetBlock();
    }

    @Override
    public MDP<BotState, Integer, DiscreteSpace> newInstance() {
        System.out.println("New Instance");
        bot.resetLocation();
        return new Environment(bot);
    }
}
