package com.bananaarch.minecraftevacuation.bot.utils;

import org.bukkit.Location;

import java.util.LinkedList;
import java.util.Queue;

public class BotUtil {

    public static Queue<Location> cloneLinkedList(Queue<Location> originalList) {
        Queue<Location> clonedList = new LinkedList<>();

        for (Location location : originalList) {
            clonedList.add(location.clone());
        }

        return clonedList;
    }
}
