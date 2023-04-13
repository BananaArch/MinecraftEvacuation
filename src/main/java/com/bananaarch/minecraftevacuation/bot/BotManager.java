package com.bananaarch.minecraftevacuation.bot;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.utils.BotType;
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

    public Set<Bot> getBots() {
        return bots;
    }

    public void addBot(Location location, BotType type) {
        bots.add(Bot.createBot(e.getBlockPlaced().getLocation(), "test", BotType.));
    }

    public BotAgent getBotAgent() {
        return botAgent;
    }
}
