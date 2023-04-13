package com.bananaarch.minecraftevacuation.bot;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.utils.BotType;
import org.bukkit.Location;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BotManager {

    private final Set<Bot> bots;
    private final BotAgent botAgent;

    public BotManager() {
        this.bots = ConcurrentHashMap.newKeySet();
        this.botAgent = new BotAgent(this, MinecraftEvacuation.getInstance());
    }

    public void createBot(Location location, String name, BotType type) {


    }

    public Set<Bot> getBots() {
        return bots;
    }

    public void addBot(Bot bot) {
        bots.add(bot);
    }

    public BotAgent getBotAgent() {
        return botAgent;
    }
}
