package com.bananaarch.minecraftevacuation.bot;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashSet;
import java.util.Set;

public class BotAgent {

    private final Plugin plugin;
    private final BotManager manager;
    private final BukkitScheduler scheduler;
    private final Set<BukkitRunnable> taskList;
    private int taskId;
    private int count;


    public BotAgent(BotManager manager, Plugin plugin) {
        this.plugin = plugin;
        this.manager = manager;
        this.scheduler = Bukkit.getScheduler();
        this.taskList = new HashSet<>();

    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            taskId = scheduler.scheduleSyncRepeatingTask(plugin, this::tick, 0, 1);
        } else {
            scheduler.cancelTask(taskId);
            stopAllTasks();
        }
    }

    public void addTask(BukkitRunnable bukkitRunnable) {
        taskList.add(bukkitRunnable);
    }

    public void stopAllTasks() {
        if(!taskList.isEmpty()) {
            taskList.stream().filter(t -> !t.isCancelled()).forEach(BukkitRunnable::cancel);
            taskList.clear();
        }
    }

    private void tick() {
        Set<Bot> bots = manager.getBots();
        count = bots.size();
        bots.forEach(this::tickBot);
    }

    public void tickBot(Bot bot) {
        Location location = bot.getLocation();
    }

}
