package com.bananaarch.minecraftevacuation.bot;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.tasks.TaskManager;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BotManager {

    private final TaskManager taskManager = MinecraftEvacuation.getInstance().getTaskManager();
    private final ConcurrentHashMap<Integer, Bot> bots = new ConcurrentHashMap<>();
    private final BotAgent botAgent = new BotAgent(this, MinecraftEvacuation.getInstance());
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

    public BotAgent getBotAgent() {
        return botAgent;
    }

//    public void walk() {
//        for (Bot bot : bots) {
//            bot.walk(new Vector(1, 0, 1));
//        }
//    }

    public void hideAll() {
        this.isVisible = false;
        bots.values().forEach(Bot::hide);
    }

    public void showAll() {
        this.isVisible = true;
        bots.values().forEach(Bot::show);
    }

    public void updateBotsForJoiningPlayers(Player player) {
        bots.values().forEach(b -> b.updateBotForJoiningPlayers(player));
        if (!isVisible)
            bots.values().forEach(b -> {

                // for some reason doesn't hide unless you add scheduler
                taskManager.runTask(() -> b.hideForJoiningPlayers(player));

            });
    }

    public int getSize() {
        return bots.size();
    }

    public int destroyAll() {
        bots.values().forEach(Bot::destroy);

        int output = bots.size();
        bots.clear();
        System.out.println("Successfully destroyed " + output + " bots");
        return output;

//        TODO: Delete serializable filen
    }
}
