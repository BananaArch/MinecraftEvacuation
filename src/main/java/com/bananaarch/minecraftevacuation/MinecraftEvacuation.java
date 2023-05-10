package com.bananaarch.minecraftevacuation;

import com.bananaarch.minecraftevacuation.bot.BotManager;
import com.bananaarch.minecraftevacuation.tasks.TaskManager;
import com.bananaarch.minecraftevacuation.ux.listeners.*;
import com.bananaarch.minecraftevacuation.ux.command.MenuCommand;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.packs.repository.Pack;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MinecraftEvacuation extends JavaPlugin implements Listener {

    private static MinecraftEvacuation instance;
    private TaskManager taskManager;
    private BotManager botManager;
    private PacketListener packetListener;

    public static MinecraftEvacuation getInstance() {
        return instance;
    }
    public TaskManager getTaskManager() {
        return taskManager;
    }
    public BotManager getBotManager() {
        return botManager;
    }
    public PacketListener getPacketListener() {
        return packetListener;
    }


    @Override
    public void onEnable() {

        instance = this;
        taskManager = new TaskManager();
        botManager = new BotManager();
        packetListener = new PacketListener();

        Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockPlacedListener(), this);
        Bukkit.getPluginManager().registerEvents(new ConnectionListener(), this);
        Bukkit.getPluginManager().registerEvents(new NPCInteractListener(), this);
        getCommand("menu").setExecutor(new MenuCommand());

        System.out.println("Minecraft School Evacuation by Kyle Shibao & Faith Kim");
    }

    @Override
    public void onDisable() {
        taskManager.cancelAllTasks();
        botManager.deleteAll();
    }

}
