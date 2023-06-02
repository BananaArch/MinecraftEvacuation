package com.bananaarch.minecraftevacuation.bot.ML;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ChatUtil {

    public static void broadcastMessage(String message) {
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(
                ChatColor.BLUE + "[NN] " +
                message
                ));
    }

    public static void broadcastError(String error) {
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(
                ChatColor.YELLOW + "[NN | Error] " +
                ChatColor.DARK_RED + error)
        );
    }
}
