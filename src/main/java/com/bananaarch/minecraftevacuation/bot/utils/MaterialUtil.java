package com.bananaarch.minecraftevacuation.bot.utils;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MaterialUtil {

    public static final Set<Material> AIR = new HashSet<>(Arrays.asList(
            Material.WATER,
            Material.FIRE,
            Material.LAVA,
            Material.SNOW,
            Material.CAVE_AIR,
            Material.VINE,
            Material.FERN,
            Material.LARGE_FERN,
            Material.GRASS,
            Material.TALL_GRASS,
            Material.SEAGRASS,
            Material.TALL_SEAGRASS,
            Material.KELP,
            Material.KELP_PLANT,
            Material.SUNFLOWER,
            Material.AIR,
            Material.VOID_AIR,
            Material.FIRE,
            Material.SOUL_FIRE
    ));

    public static final Set<Material> FENCE;
    public static final Set<Material> GATES;

    static {
        FENCE = new HashSet<>();
        GATES = new HashSet<>();
        Predicate<Material> filterFenceWall = m -> Fence.class.isAssignableFrom(m.data) || Wall.class.isAssignableFrom(m.data);
        Predicate<Material> filterGate = m -> Gate.class.isAssignableFrom(m.data);

        for (Material m : Material.values()) {
            if (!m.isLegacy()) {
                if (m.equals(Material.GLASS_PANE) || m.equals(Material.IRON_BARS) || filterFenceWall.test(m)) {
                    FENCE.add(m);
                }
                if (filterGate.test(m)) {
                    GATES.add(m);
                }
            }
        }
    }
    public static boolean canStandOn(Material mat) {
        if(mat == Material.END_ROD || mat == Material.FLOWER_POT || mat == Material.REPEATER || mat == Material.COMPARATOR
                || mat == Material.SNOW || mat == Material.LADDER || mat == Material.VINE || mat == Material.SCAFFOLDING
                || mat == Material.AZALEA || mat == Material.FLOWERING_AZALEA || mat == Material.BIG_DRIPLEAF
                || mat == Material.CHORUS_FLOWER || mat == Material.CHORUS_PLANT || mat == Material.COCOA
                || mat == Material.LILY_PAD || mat == Material.SEA_PICKLE)
            return true;

        if(mat.name().endsWith("_CARPET"))
            return true;

        if(mat.name().startsWith("POTTED_"))
            return true;

        if((mat.name().endsWith("_HEAD") || mat.name().endsWith("_SKULL")) && !mat.name().equals("PISTON_HEAD"))
            return true;

        if(mat.data == Candle.class)
            return true;
        return false;
    }
}
