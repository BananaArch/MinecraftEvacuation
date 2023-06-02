package com.bananaarch.minecraftevacuation.bot.ML.NN;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.Bot;
import com.bananaarch.minecraftevacuation.bot.ML.BotAgent;
import com.bananaarch.minecraftevacuation.tasks.TaskManager;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;

public class Environment implements MDP<BotState, Integer, DiscreteSpace> {

    private final DiscreteSpace actionSpace = new DiscreteSpace(8);
    private final TaskManager taskManager = MinecraftEvacuation.getInstance().getTaskManager();
    private final BotAgent botAgent = MinecraftEvacuation.getInstance().getBotManager().getBotAgent();
    private final Bot bot;

    public Environment(Bot bot) {
        this.bot = bot;
    }


    @Override
    public ObservationSpace<BotState> getObservationSpace() {
        return new GameObservationSpace();
    }

    @Override
    public DiscreteSpace getActionSpace() {
        return this.actionSpace;
    }

    @Override
    public BotState reset() {
        return new BotState(bot.getState());
    }

    @Override
    public void close() {}

    @Override
    public StepReply<BotState> step(Integer actionIndex) {

        Action action = Action.getActionByIndex(actionIndex);

        double reward = botAgent.rewardFunction(bot, action);

        Location newLocation = bot.getLocation().add(action.getVector());

        bot.teleportTo(newLocation.getX(), newLocation.getY(), newLocation.getZ());

        BotState botState = new BotState(bot.getState());

        return new StepReply<>(
                botState,
                reward,
                isDone(),
                "MinecraftEvacuationDl4j"
        );
    }

    @Override
    public boolean isDone() {
        return bot.standingOnTargetBlock();
    }

    @Override
    public MDP<BotState, Integer, DiscreteSpace> newInstance() {
        return new Environment(bot);
    }
}
