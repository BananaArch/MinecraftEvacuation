package com.bananaarch.minecraftevacuation.tasks;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class TaskManager {

    private final Plugin plugin = MinecraftEvacuation.getInstance();
    private final Map<Integer, BukkitTask> tasks = new HashMap<>(); // make into concurrent if doesnt work
    private final BukkitScheduler scheduler = Bukkit.getScheduler();

    public int runTask(Runnable runnable) {
        BukkitTask task = scheduler.runTask(plugin, runnable);
        int taskId = task.getTaskId();
        tasks.put(taskId, task);
        return taskId;
    }

    public int runAsyncTask(Runnable runnable) {
        BukkitTask task = scheduler.runTaskAsynchronously(plugin, runnable);
        int taskId = task.getTaskId();
        tasks.put(taskId, task);
        return taskId;
    }

    public int scheduleTask(Runnable runnable, long delay) {
        BukkitTask task = scheduler.runTaskLater(plugin, runnable, delay);
        int taskId = task.getTaskId();
        tasks.put(taskId, task);
        return taskId;
    }

    public int scheduleRepeatingTask(Runnable runnable, long delay, long period) {
        BukkitTask task = scheduler.runTaskTimer(plugin, runnable, delay, period);
        int taskId = task.getTaskId();
        tasks.put(taskId, task);
        return taskId;
    }

    public void cancelTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.get(taskId).cancel();
            tasks.remove(taskId);
        }
    }

    public void cancelAllTasks() {
        tasks.values().forEach(BukkitTask::cancel);
        tasks.clear();
    }

    public boolean containsTask(int id) {
        return tasks.containsKey(id);
    }


}
