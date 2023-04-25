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

    private final PacketListener packetListener = new PacketListener();
    private final TaskManager taskManager = MinecraftEvacuation.getInstance().getTaskManager();
    private final BotManager botManager = MinecraftEvacuation.getInstance().getBotManager();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        botManager.updateBotsForJoiningPlayers(e.getPlayer());

        try {
            packetListener.inject(e.getPlayer());
        } catch (NoSuchFieldException exception) {
            System.out.println("Error with packet listener");
            e.getPlayer().sendMessage(ChatColor.DARK_RED + "ERROR: Packet Listener throwing NoSuchFieldException");
            throw new RuntimeException(exception);
        } catch (IllegalAccessException exception) {
            System.out.println("Error with packet listener");
            e.getPlayer().sendMessage(ChatColor.DARK_RED + "ERROR: Packet Listener throwing IllegalAccessException");
            throw new RuntimeException(exception);
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        try {
            packetListener.stop(e.getPlayer());
        } catch (NoSuchFieldException ex) {
            System.out.println("Error with packet listener");
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            System.out.println("Error with packet listener");
            throw new RuntimeException(ex);
        }
    }

}
