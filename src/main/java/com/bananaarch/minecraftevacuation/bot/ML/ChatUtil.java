package com.bananaarch.minecraftevacuation.bot.ML;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ChatUtil {

    public static void broadcastMessage(String message, String distinctMessage) {
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(
                ChatColor.BLUE + "[NN] " +
                ChatColor.GRAY + message
                ));
    }

    public static void broadcastError(String error) {
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(
                ChatColor.RED+ "[NN] " +
                ChatColor.DARK_RED + error)
        );
    }
}
