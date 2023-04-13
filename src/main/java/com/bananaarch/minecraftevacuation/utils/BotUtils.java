package com.bananaarch.minecraftevacuation.utils;

import java.util.UUID;


// code stolen from Terminators Plus

public class BotUtils {

    public static UUID randomSteveUUID() {
        UUID uuid = UUID.randomUUID();

        if (uuid.hashCode() % 2 == 0) {
            return uuid;
        }

        return randomSteveUUID();
    }


}
