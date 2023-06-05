package com.bananaarch.minecraftevacuation.bot.AI.NN;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.Bot;
import com.bananaarch.minecraftevacuation.tasks.TaskManager;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

public enum Action {
    NORTH(new Vector(0, 0, -1)),
    SOUTH(new Vector(0, 0, 1)),
    EAST(new Vector(1, 0, 0)),
    WEST(new Vector(-1, 0, 0));
//    JUMP_NORTH(new Vector(0, 1, -1)),
//    JUMP_SOUTH(new Vector(0, 1, 1)),
//    JUMP_EAST(new Vector(1, 1, 0)),
//    JUMP_WEST(new Vector(-1, 1, 0));

    private final Vector vector;
    private final TaskManager taskManager = MinecraftEvacuation.getInstance().getTaskManager();

    Action(Vector vector) {
        this.vector = vector;
    }

    public Vector getVector() {
        return vector;
    }

    public void walk(Bot bot) {
        bot.move(MoverType.SELF, new Vec3(vector.getX(), vector.getY(), vector.getZ()));
    }

    private static final List<Action> VALUES = Arrays.asList(values());

    public static Action getActionByIndex(int index) {
        return VALUES.get(index);
    }


}
