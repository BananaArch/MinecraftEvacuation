package com.bananaarch.minecraftevacuation.bot;

import com.bananaarch.minecraftevacuation.bot.utils.BotType;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public class Bot extends ServerPlayer {

//    private final MinecraftEvacuation plugin;
    private final BukkitScheduler scheduler;
    private Vector velocity;
    private Location initialLocation;
    private Location targetLocation;

    private Bot(MinecraftServer minecraftserver, ServerLevel worldserver, GameProfile gameprofile, Location initialLocation) {
        super(minecraftserver, worldserver, gameprofile);

//        this.plugin = MinecraftEvacuation.getPlugin();
        this.initialLocation = initialLocation;
        this.scheduler = Bukkit.getScheduler();
    }

    public static Bot createBot(Location loc, String name, BotType botType) {
        return null;
    }






}
