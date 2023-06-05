package com.bananaarch.minecraftevacuation.bot;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.AI.BotAgent;
import com.bananaarch.minecraftevacuation.tasks.TaskManager;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BotManager {

    private final ConcurrentHashMap<Integer, Bot> bots = new ConcurrentHashMap<>();
    private final BotAgent botAgent = new BotAgent(this);
    private final TaskManager taskManager = MinecraftEvacuation.getInstance().getTaskManager();
    private boolean isVisible;

    public BotManager() {
        this.isVisible = true;
    }

    public void addBot(Bot bot) {
        bots.put(bot.getId(), bot);
    }

    public Set<Bot> getBots() {
        return new HashSet<>(bots.values());
    }

    public boolean containsBot(int botId) {
        return bots.containsKey(botId);
    }

    public Bot getBot(int botId) {
        return bots.get(botId);
    }

    public BotAgent getBotAgent() {
        return botAgent;
    }

    public void setBotsTargetLocation(Location targetLocation) {
        bots.values().forEach(b -> b.setTargetLocation(targetLocation));
    }

    public void hideAll() {
        this.isVisible = false;
        bots.values().forEach(Bot::hide);
    }

    public void showAll() {
        this.isVisible = true;
        bots.values().forEach(Bot::show);
    }

    public void updateVisibility() {
        if (!isVisible)
            bots.values().forEach(Bot::hide);
    }

    public int getSize() {
        return bots.size();
    }

    public int deleteAll() {
        bots.values().forEach(Bot::destroy);

        int output = bots.size();
        bots.clear();
        System.out.println("Successfully destroyed " + output + " bots");
        return output;

            //TODO: Delete serializable file
    }

    public void deleteBot(int botId) {
        Bot bot = bots.get(botId);
        bot.destroy();
        bots.remove(botId);

        //TODO: reserialize?
    }

    public void resetLocation() {
        bots.values().forEach(Bot::resetLocation);
    }

    public void generatePaths() { bots.values().forEach(bot -> {
        taskManager.runAsyncTask(() -> bot.generatePath());
    });
    }

    public void followPaths() { bots.values().forEach(Bot::followPath); }

}
