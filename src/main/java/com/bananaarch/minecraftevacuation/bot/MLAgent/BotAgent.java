package com.bananaarch.minecraftevacuation.bot.MLAgent;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.Bot;
import com.bananaarch.minecraftevacuation.bot.BotManager;
import com.bananaarch.minecraftevacuation.tasks.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import org.nd4j.linalg.api.ops.impl.reduce.same.Min;

import java.util.HashSet;
import java.util.Set;

public class BotAgent {

    private final Plugin plugin = MinecraftEvacuation.getInstance();
    private final BotManager botManager;
    private final TaskManager taskManager = MinecraftEvacuation.getInstance().getTaskManager();
    private int tickTaskId;
    private int trainTaskId;

    public BotAgent(BotManager botManager) {
        this.botManager = botManager;
        setEnabled(true);
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.tickTaskId = taskManager.scheduleRepeatingTask(this::tick, 0, 1);
        } else {
            taskManager.cancelTask(tickTaskId);
        }
    }

    public void setTrainStatus(boolean enabled) {
        if (enabled) {
            this.trainTaskId = taskManager.scheduleRepeatingTask(this::trainBot, 0, 1);
        } else {
            taskManager.cancelTask(trainTaskId);
        }
    }

    public void trainBot() {
        Set<Bot> bots = botManager.getBots();

        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage("Training"));

        bots.forEach(bot -> {

            bot.setVelocity(new Vector(2 * Math.random() - 1, 0, 2 * Math.random() - 1).multiply(.05));

        });
    }

    private void tick() {
        Set<Bot> bots = botManager.getBots();
        botManager.updateVisibility();
        bots.forEach(Bot::tick);
    }

}
