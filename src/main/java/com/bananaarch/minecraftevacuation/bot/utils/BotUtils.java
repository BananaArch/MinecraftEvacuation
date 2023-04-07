package com.bananaarch.minecraftevacuation.bot.utils;

import java.util.UUID;


// from Terminators Plus

public class BotUtils {

    public static UUID randomSteveUUID() {
        UUID uuid = UUID.randomUUID();

        if (uuid.hashCode() % 2 == 0) {
            return uuid;
        }

        return randomSteveUUID();
    }


}
