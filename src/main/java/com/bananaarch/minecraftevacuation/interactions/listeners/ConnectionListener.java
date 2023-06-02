package com.bananaarch.minecraftevacuation.interactions.listeners;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.BotManager;
import com.bananaarch.minecraftevacuation.tasks.TaskManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectionListener implements Listener {

    private final PacketListener packetListener = MinecraftEvacuation.getInstance().getPacketListener();
    private final TaskManager taskManager = MinecraftEvacuation.getInstance().getTaskManager();
    private final BotManager botManager = MinecraftEvacuation.getInstance().getBotManager();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        e.getPlayer().sendTitle(
                translate("#c2c2ffS#b5b5fec#a8a8feh#9b9bfdo#8e8efco#8181fcl #7474fbE#6767fav#5b5bfaa#4e4ef9c#4141f8u#3434f8a#2727f7t#1a1af6i#0d0df6o#0000f5n"),
                translate("#ffaa00b#ffae0by #ffb115K#ffb520y#ffb82bl#ffbc36e #ffbf40S#ffc34bh#ffc656i#ffca60b#ffce6ba#ffd176o #ffd580& #ffd88bF#ffdc96a#ffdfa1i#ffe3abt#ffe6b6h #ffeac1K#ffedcbi#fff1d6m"),
                20,
                20 * 5,
                20
        );

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

    private Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    private String translate(String input) {

        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {

            String color = input.substring(matcher.start(), matcher.end());
            input = input.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
            matcher = pattern.matcher(input);

        }

        return input;

    }

}
