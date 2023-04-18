package com.bananaarch.minecraftevacuation.bot.botsubclasses;

import com.bananaarch.minecraftevacuation.bot.Bot;
import com.bananaarch.minecraftevacuation.utils.BotType;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Location;

public class Freshman extends Bot {

    private Freshman(MinecraftServer minecraftserver, ServerLevel worldserver, GameProfile gameprofile, Location initialLocation, BotType botType) {
        super(minecraftserver, worldserver, gameprofile, initialLocation, botType);


    }

}
