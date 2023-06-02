package com.bananaarch.minecraftevacuation.bot.ML.NN;

import com.bananaarch.minecraftevacuation.bot.Bot;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class GameObservationSpace implements ObservationSpace<BotState> {

    @Override
    public String getName() {
        return "MinecraftGameObservationSpace";
    }

    @Override
    public int[] getShape() {
        return new int[Bot.getObservationSize()];
    }

    @Override
    public INDArray getLow() {
        return Nd4j.create(1, getShape()[0]).assign(Float.MIN_VALUE);
    }

    @Override
    public INDArray getHigh() {
        return Nd4j.create(1, getShape()[0]).assign(Float.MAX_VALUE);
    }
}
