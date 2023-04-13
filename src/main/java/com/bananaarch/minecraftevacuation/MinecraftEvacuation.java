package com.bananaarch.minecraftevacuation;

import com.bananaarch.minecraftevacuation.bot.Bot;
import com.bananaarch.minecraftevacuation.bot.BotManager;
import com.bananaarch.minecraftevacuation.settings.blocklistener.BlockPlacedListener;
import com.bananaarch.minecraftevacuation.settings.command.GUIListener;
import com.bananaarch.minecraftevacuation.settings.command.SettingsCommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MinecraftEvacuation extends JavaPlugin implements Listener {

    private static MinecraftEvacuation instance;
    private BotManager manager;

    public static MinecraftEvacuation getInstance() {
        return instance;
    }
    public BotManager getManager() {
        return manager;
    }


    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockPlacedListener(), this);
        getCommand("settings").setExecutor(new SettingsCommand());







        System.out.println("Minecraft School Evacuation by Kyle Shibao & Faith Kim");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        e.getPlayer().sendTitle(
                translate("#c2c2ffS#b5b5fec#a8a8feh#9b9bfdo#8e8efco#8181fcl #7474fbE#6767fav#5b5bfaa#4e4ef9c#4141f8u#3434f8a#2727f7t#1a1af6i#0d0df6o#0000f5n"),
                translate("#ffaa00b#ffae0by #ffb115K#ffb520y#ffb82bl#ffbc36e #ffbf40S#ffc34bh#ffc656i#ffca60b#ffce6ba#ffd176o #ffd580& #ffd88bF#ffdc96a#ffdfa1i#ffe3abt#ffe6b6h #ffeac1K#ffedcbi#fff1d6m"),
                20,
                20 * 5,
                20
        );

    }

    private Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    private String translate(String input) {

        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {

            String color = input.substring(matcher.start(), matcher.end());
            input = input.replace(color, ChatColor.of(color) + "");
            matcher = pattern.matcher(input);

        }

        return input;

    }

}
