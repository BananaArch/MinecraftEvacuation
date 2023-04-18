package com.bananaarch.minecraftevacuation.utils;

import com.bananaarch.minecraftevacuation.bot.Bot;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

public class BlockPlaceUtil {

    public static void placedBlock(BlockPlaceEvent e, Player blockPlacer, Location blockPlacedLocation, BotType botType) {

        Bot.createBot(blockPlacedLocation.add(new Vector(0.5, 0, 0.5)), "Bot", botType);
        e.setCancelled(true);
        blockPlacer.sendMessage(ChatColor.GRAY + "Successfully created a " + ChatColor.GREEN + botType.getName());
        blockPlacer.playSound(blockPlacer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);

    }
}
