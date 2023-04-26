package com.bananaarch.minecraftevacuation.ux.listeners;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.BotManager;
import com.bananaarch.minecraftevacuation.tasks.TaskManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    private final PacketListener packetListener = MinecraftEvacuation.getInstance().getPacketListener();
    private final TaskManager taskManager = MinecraftEvacuation.getInstance().getTaskManager();
    private final BotManager botManager = MinecraftEvacuation.getInstance().getBotManager();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        botManager.updateBotsForJoiningPlayers(e.getPlayer());

        try {
            packetListener.inject(e.getPlayer());
        } catch (NoSuchFieldException exception) {
            e.getPlayer().sendMessage(ChatColor.DARK_RED + "ERROR: Packet Listener throwing NoSuchFieldException");
            throw new RuntimeException("Error with packet listener");
        } catch (IllegalAccessException exception) {
            e.getPlayer().sendMessage(ChatColor.DARK_RED + "ERROR: Packet Listener throwing IllegalAccessException");
            throw new RuntimeException("Error with packet listener");
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        try {
            packetListener.stop(e.getPlayer());
        } catch (NoSuchFieldException exception) {
            throw new RuntimeException("Error with packet listener");
        } catch (IllegalAccessException exception) {
            throw new RuntimeException("Error with packet listener");
        }
    }

}
