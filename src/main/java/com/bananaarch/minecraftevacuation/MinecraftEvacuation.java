package com.bananaarch.minecraftevacuation;

import com.bananaarch.minecraftevacuation.bot.BotManager;
import com.bananaarch.minecraftevacuation.tasks.TaskManager;
import com.bananaarch.minecraftevacuation.interactions.listeners.*;
import com.bananaarch.minecraftevacuation.interactions.command.MenuCommand;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

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
