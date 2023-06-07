package com.bananaarch.minecraftevacuation.bot.subclasses;

import com.bananaarch.minecraftevacuation.bot.Bot;
import com.bananaarch.minecraftevacuation.bot.Genderable;
import com.bananaarch.minecraftevacuation.bot.Gender;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Senior extends Bot implements Genderable {

    private final Map<Gender, String[]> skins = new HashMap<Gender, String[]>()
    {{
        put(Gender.MALE, new String[]{
                "ewogICJ0aW1lc3RhbXAiIDogMTY4NjA1NjQ2NDUyNiwKICAicHJvZmlsZUlkIiA6ICJkMTE4NTk0MWE5OTY0OGQ1OGVhNzNhNTZkZTZkYjllYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJOZWthU2hhbmRyYSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84NzVjMzE0Mjc0MTk0MzFlNjYwMmMwYTRkNzZjYzYwMTMxMWUwMWExMTNhZDY5MWM2YWE0NzE2NWYxNThkMDlhIgogICAgfQogIH0KfQ==",
                "Cok7/6ckaOI2eyg10pDLHj80IGO1aGS8UrtS1nnZMxH2tuRvO2TLbHuObaerCZaYCVDLC1KFJsuDiQGk+ASxsIV+jWgF46myYzSj0VpeZC5aVHpZVYyFMlMaNmLSfhNPS29DMQ3DZjY6LcihkwYEHPIA0INVzA539sicKybmVFzQl8OyQNoy8MLnj6VL9E9sI0IJ27puz/gd8ShTCKX810EGINlBM4XZRyQO1WeEvj2Mn54rccbzac69NVXyc1UY8v56OP9Z6jbfJo6o+MnotAmvUPIijx1HYWnYg3zNcZu34utWqwPVNRv5E1A7DGMsegJy3Gn7xPU0CA1hET4yBK5NGhESdDeb9LXTYsYKX/wpIctOGQ3cwgLs0DwYsAQmcSeB/eBE1u3Xh1W8BuQRVBjjskkffi0VMpG1jyPyh69vFfjQWpHdvpTCtWAjzJDApNbzw8nmeV+47xbVy08hQhfzKvHxL2krsRZCzunR3z3ULMgLD2Uk0WT7IsrQfgN7PuV4kpEuDLOmXMHWjaQT02K9W1U5LHVZDs/rfSWUizEk9/joxhU3mpl3bwwzeBZvCvAlD8geDQz5p6d320bfAWoByHhBgOzrGrI0PMhdJo/8k6E9Xn19ZwfoCTpZvZwJEG0wzZJMiEinCJpQuSw9nX/h/wA9ezAeaX2xpn2I+Zc="
        });
        put(Gender.FEMALE, new String[]{
                "ewogICJ0aW1lc3RhbXAiIDogMTY4NjA3MTAxNjQ1NiwKICAicHJvZmlsZUlkIiA6ICJiMTM1MDRmMjMxOGI0OWNjYWFkZDcyYWVhYmMyNTQ1MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJUeXBrZW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDU3NDFiNDY1OTdlYTY3MWI5ZWE5YTM0YTA3MmQ4YTdhYTdmM2E5NTExNDEwZTgxZTZjNTEzYTk0ZjAwMmJkZiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
                "Dj4LaQmAFfLV8cdKtiYqjq6JtkK83R8iPr0xmIriSFNV31VVRiWHztrkloycyCTswUBprxQE4qBrffd5efkG//YwJcna36LXqr03yJcmGQzhw4WsQUKi9azhPVBCBPtUR+f4nLMmeCw0hsZKBT1uNjO2FWp87SLp+XfkOOPhjr6Ckk5D4Il4ePRkC7aYKguebi8sLJ9T36xkDApuMcK6ksG3R9aIhzkKbwkHh8g49joID/EEDCidGzm5CUCHoZntAFb26S+Cbct7uFmtQxSx6ab4DtbSir+dZZqDO/TXYu9AOjvFXr4hxBBpCU2kWrrVBwbdI2ZSK88BTrotVEVvje5ZWJNNEYoiy3lUKeI47bkJPzb9Z7dssasAXBcDNEEDU06oCkTve5Ta8+z/e8rHCtX2rqnUjcO/Q8U2257MKJr2p9AZr8xiMsL8UgeXG0DsCqvDeMXCj5Zv+my253lVVbBj7UrNGH2E+Rn0WQJJX+jzhVJGhga95ufeupMHSbKUl3y6L3RqOydJC1ZnYknXPqyJo8T8R1OPcfDv/1PcOXuOblRcotWqfnqVRZjSwqweDZ2u2Rvn9N+CyfCzO74/qOFbkQgbHVyTDsRYcakHSrEh9Bj54c1UgvQP3MaKhX9gNxtUWx9pG9NgCfOVe2r+Hspm3NOh7qZtdSX1v2Unr8s=",
        });
    }};
    private Gender gender;

    public Senior(MinecraftServer minecraftserver, ServerLevel worldserver, GameProfile gameprofile, Location initialLocation) {
        super(minecraftserver, worldserver, gameprofile, initialLocation);
        Gender[] values = Gender.values();
        Gender randomGender = values[(int) (values.length * Math.random())];
        setGender(randomGender);
    }

    @Override
    public List<String> getInfo() {

        Location initialLocation = getInitialLocation();
        Location targetLocation = getTargetLocation();

        return Arrays.asList(
                ChatColor.GRAY + "Bot Type: " + ChatColor.WHITE + "Senior",
                ChatColor.GRAY + "Gender: " + ChatColor.GREEN + gender.name().toUpperCase(),
                ChatColor.GRAY + "Initial Location: " + ChatColor.GREEN + "(" +
                        initialLocation.getX() + ", " +
                        initialLocation.getY() + ", " +
                        initialLocation.getZ() + ")",
                ChatColor.GRAY + "Target Location: " + (targetLocation == null ? ChatColor.RED + "No target location found" : ChatColor.GREEN + "(" +
                        targetLocation.getX() + ", " +
                        targetLocation.getY() + ", " +
                        targetLocation.getZ() + ")")
        );
    }

    @Override
    public void setGender(Gender gender) {
        this.gender = gender;
        String[] skin = skins.get(gender);
        if (skin != null)
            setSkin(skin[0], skin[1]);
    }

    @Override
    public Gender getGender() {
        return gender;
    }

}
