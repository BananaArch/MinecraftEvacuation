package com.bananaarch.minecraftevacuation.bot.subclasses;

import com.bananaarch.minecraftevacuation.bot.Bot;
import com.bananaarch.minecraftevacuation.utils.BotType;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Location;

public class Freshman extends Bot {

    public Freshman(MinecraftServer minecraftserver, ServerLevel worldserver, GameProfile gameprofile, Location initialLocation) {
        super(minecraftserver, worldserver, gameprofile, initialLocation);
    }

}
