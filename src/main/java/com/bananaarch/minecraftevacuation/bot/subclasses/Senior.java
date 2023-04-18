package com.bananaarch.minecraftevacuation.bot.subclasses;

import com.bananaarch.minecraftevacuation.bot.Bot;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Location;

public class Senior extends Bot {

    public Senior(MinecraftServer minecraftserver, ServerLevel worldserver, GameProfile gameprofile, Location initialLocation) {
        super(minecraftserver, worldserver, gameprofile, initialLocation);
    }

}
