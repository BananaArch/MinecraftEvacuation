package com.bananaarch.minecraftevacuation;

import com.bananaarch.minecraftevacuation.bot.BotManager;
import com.bananaarch.minecraftevacuation.tasks.TaskManager;
import com.bananaarch.minecraftevacuation.interactions.listeners.*;
import com.bananaarch.minecraftevacuation.interactions.command.MenuCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.deeplearning4j.config.DL4JClassLoading;
import org.nd4j.common.config.ND4JClassLoading;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public final class MinecraftEvacuation extends JavaPlugin {

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

        /*
        https://community.konduit.ai/t/deeplearning4j-for-minecraft-plugins-doesnt-work-due-to-different-entry-point/1719/14
        THANK YOU TO THIS MAN!!! I LOVE YOU!
         */
        DL4JClassLoading.setDl4jClassloader(this.getClassLoader());
        ND4JClassLoading.setNd4jClassloader(this.getClassLoader());
        System.out.println("Backend for MinecraftEvacuation: " + Nd4j.getBackend().getClass().getName());

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
