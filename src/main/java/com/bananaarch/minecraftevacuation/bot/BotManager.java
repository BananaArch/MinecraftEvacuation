package com.bananaarch.minecraftevacuation.bot;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BotManager {

    private final Set<Bot> bots;
    private final BotAgent botAgent;
    private boolean isVisible;


    public BotManager() {
        this.bots = ConcurrentHashMap.newKeySet();
        this.botAgent = new BotAgent(this, MinecraftEvacuation.getInstance());
    }

    public void addBot(Bot bot) {
        bots.add(bot);
    }

    public Set<Bot> getBots() {
        return bots;
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
        bots.forEach(Bot::hide);
    }

    public void showAll() {
        this.isVisible = true;
        bots.forEach(Bot::show);
    }

    public void updateBotsForJoiningPlayers(Player player) {
        bots.forEach(b -> b.updateBotForJoiningPlayers(player));
        if (!isVisible)
            bots.forEach(b -> {

                // for some reason doesn't hide unless you add scheduler
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        b.hideForJoiningPlayers(player);
                        this.cancel();
                    }
                }.runTaskLater(MinecraftEvacuation.getInstance(), 1L); // 20 ticks = 1 second

            });
    }

    public boolean getVisibility() {
        return isVisible;
    }

    public int getSize() {
        return bots.size();
    }

    public int destroyAll() {
        bots.forEach(Bot::destroy);

        int output = bots.size();
        bots.clear();
        System.out.println("Successfully destroyed " + output + " bots");
        return output;

//        TODO: Delete serializable filen
    }
}
