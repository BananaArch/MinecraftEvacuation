package com.bananaarch.minecraftevacuation.bot.ML.NN;

import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.nd4j.linalg.api.ndarray.INDArray;

public class GameObservationSpace implements ObservationSpace<BotState> {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int[] getShape() {
        return new int[0];
    }

    @Override
    public INDArray getLow() {
        return null;
    }

    @Override
    public INDArray getHigh() {
        return null;
    }
}
