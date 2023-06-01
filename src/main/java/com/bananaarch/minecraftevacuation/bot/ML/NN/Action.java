package com.bananaarch.minecraftevacuation.bot.ML.NN;

import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

public enum Action {
    NORTH(new Vector(0, 0, -1)),
    SOUTH(new Vector(0, 0, 1)),
    EAST(new Vector(1, 0, 0)),
    WEST(new Vector(-1, 0, 0)),
    JUMP_NORTH(new Vector(0, 1, -1)),
    JUMP_SOUTH(new Vector(0, 1, 1)),
    JUMP_EAST(new Vector(1, 1, 0)),
    JUMP_WEST(new Vector(-1, 1, 0));

    private Vector vector;

    Action(Vector vector) {
        this.vector = vector;
    }

    public Vector getVector() {
        return vector;
    }

    private static final List<Action> VALUES = Arrays.asList(values());

    public static Action getActionByIndex(int index) {
        return VALUES.get(index);
    }


}
