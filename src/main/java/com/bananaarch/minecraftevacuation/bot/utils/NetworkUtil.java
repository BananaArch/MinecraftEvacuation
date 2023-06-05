package com.bananaarch.minecraftevacuation.bot.utils;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.rl4j.learning.configuration.QLearningConfiguration;
import org.deeplearning4j.rl4j.network.configuration.DQNDenseNetworkConfiguration;
import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.nd4j.linalg.learning.config.RmsProp;

import java.io.File;
import java.io.IOException;

public class NetworkUtil {

    public static QLearningConfiguration buildConfig() {
        return QLearningConfiguration.builder()
                .seed(0L)
                .maxEpochStep(100)
                .maxStep(30)
                .expRepMaxSize(1500)
                .batchSize(128)
                .targetDqnUpdateFreq(500)
                .updateStart(10)
                .rewardFactor(0.01)
                .gamma(0.99)
                .errorClamp(1.0)
                .minEpsilon(0.1f)
                .epsilonNbStep(1000)
                .doubleDQN(true)
                .build();
    }

    public static DQNDenseNetworkConfiguration buildDQNDenseNetworkConfiguration() {

        return DQNDenseNetworkConfiguration.builder()
                .l2(0.001)
                .updater(new RmsProp(0.000025))
                .numHiddenNodes(16)
                .numLayers(2)
                .build();

    }

    public static MultiLayerNetwork loadNetwork(final String networkName) {
        try {
            return MultiLayerNetwork.load(new File(networkName), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int getMaxValueIndex(double[] values) {

        int maxIndex = 0;
        for (int i = 1; i < values.length; i++) {
            maxIndex = values[i] > values[maxIndex] ? i : maxIndex;
        }
        return maxIndex;

    }
}

