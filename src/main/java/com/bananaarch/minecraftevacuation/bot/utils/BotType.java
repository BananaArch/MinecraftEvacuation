package com.bananaarch.minecraftevacuation.bot.utils;

public enum BotType {

    FRESHMAN(new String[]{}),
    SENIOR(new String[]{"test", "as"});
    private String[] skin;

    BotType(String[] skin) {
        this.skin = skin;
    };

    public String[] getSkin() {
        return skin;
    }

}
