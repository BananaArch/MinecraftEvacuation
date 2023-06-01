package com.bananaarch.minecraftevacuation.bot.ML.NN;

import org.deeplearning4j.rl4j.space.Encodable;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class BotState implements Encodable {

    private final INDArray indArray;

    public BotState(INDArray indArray) {
        this.indArray = indArray;
    }

    @Override
    public double[] toArray() {
        return indArray.data().asDouble();
    }

    @Override
    public boolean isSkipped() {
        return false;
    }

    @Override
    public INDArray getData() {
        return indArray;
    }

    @Override
    public Encodable dup() {
        return null;
    }

    public INDArray getMatrix() {
        return Nd4j.create(new double[][] {
                toArray()
        });
    }
}
