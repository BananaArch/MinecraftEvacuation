package com.bananaarch.minecraftevacuation.bot.utils;

public enum BotType {

//  https://mineskin.org/
//  value, signature

    FRESHMAN(new String[][]{new String[]{}, new String[]{}}),
    SOPHOMORE(new String[][]{new String[]{}, new String[]{}}),
    JUNIOR(new String[][]{new String[]{}, new String[]{}}),
    SENIOR(new String[][]{new String[]{}, new String[]{}}),
    TEACHER(new String[][]{new String[]{}, new String[]{}});
    private String[][] skin;

    BotType(String[][] skin) {
        this.skin = skin;
    };

    public String[] getSkin(boolean isMale) {
        if (isMale)
            return skin[0];
        else
            return skin[1];
    }

}
