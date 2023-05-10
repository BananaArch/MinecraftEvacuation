package com.bananaarch.minecraftevacuation.bot;

public enum Gender {

    MALE,
    FEMALE;

    public Gender nextGender() {
        int nextOrdinal = (this.ordinal() + 1) % values().length;
        return values()[nextOrdinal];
    }

}
