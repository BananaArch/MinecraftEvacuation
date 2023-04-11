package com.bananaarch.minecraftevacuation.bot.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class GameStateUtil {

    private static final int LENGTH = 15;
    private static final int WIDTH = 15;
    private static final int HEIGHT = 4;

    public static INDArray getGameState(Location location) {

        int[] shape = new int[]{LENGTH, WIDTH, HEIGHT};

        INDArray gameState = Nd4j.create(shape);

        int startingLengthIndex = LENGTH % 2 != 0 ? Math.floorDiv(LENGTH, 2) : LENGTH / 2 - 1;
        int startingWidthIndex = WIDTH % 2 != 0 ? Math.floorDiv(WIDTH, 2) : WIDTH / 2 - 1;
        int startingHeightIndex = HEIGHT % 2 != 0 ? Math.floorDiv(HEIGHT, 2) : HEIGHT / 2 - 1;

        for (
                int i = -startingLengthIndex;
                i <= Math.floorDiv(LENGTH, 2);
                i++
        ) {

            for (
                    int j = -startingWidthIndex;
                    j <= Math.floorDiv(WIDTH, 2);
                    j++
            ) {

                for (
                        int k = -startingHeightIndex;
                        k <= Math.floorDiv(HEIGHT, 2);
                        k++
                ) {

//                    System.out.print("(" + i + ", " + j + ", " + k + ")\t");

                    Block scannedBlock = getBlockAtLocation(location, i, j, k);

                    gameState.putScalar(
                            i + startingLengthIndex,
                            j + startingWidthIndex,
                            k + startingHeightIndex,
                            scannedBlock.isEmpty() || !scannedBlock.getType().isSolid() ? 1 : 0
                    );


                }

//                System.out.println();

            }

        }

//        position of player
        gameState.putScalar(
                startingLengthIndex,
                startingWidthIndex,
                startingHeightIndex,
                -1
        );

        return gameState;

    }

    public static Block getBlockAtLocation(Location location, int deltaX, int deltaY, int deltaZ) {

        Location blockLocation = new Location(
                location.getWorld(),
                location.getX() + deltaX,
                location.getY() + deltaY,
                location.getZ() + deltaZ);
        return blockLocation.getBlock();

    }

}