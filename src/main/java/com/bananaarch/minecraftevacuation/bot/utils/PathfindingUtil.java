package com.bananaarch.minecraftevacuation.bot.utils;

import org.bukkit.Location;

public class PathfindingUtil {

    public static boolean isObstructed(Location location) {
        return location.getBlock().getType().isSolid();
    }

    public static boolean canStandAt(Location location) {
        return !(isObstructed(location) || isObstructed(location.clone().add(0, 1, 0)) || !isObstructed(location.clone().add(0, -1, 0)));
    }
}
