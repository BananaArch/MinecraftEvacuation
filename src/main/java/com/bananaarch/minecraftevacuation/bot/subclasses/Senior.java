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
                "ewogICJ0aW1lc3RhbXAiIDogMTY4MDkwMTg1Nzg0NCwKICAicHJvZmlsZUlkIiA6ICIzNmMxODk4ZjlhZGE0NjZlYjk0ZDFmZWFmMjQ0MTkxMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJMdW5haWFuIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzE5NzA1NTk0N2YxMDEyNTNhMWQ4YmNiNTU0NGFiZGRlNDNmZDBiYTlkYzRmMmU0N2I0NjcwZGYwY2U2ZDNiNTMiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                "xo/IVa1J5fb1AV/8UBWEXyH5rXrFXdL1zR+RREeFaRT0CrPKZMOIy9SisXKl41tDxZMsxu3qxkpLV3T/vwtRwxx1j4omvDQfOE+ff9IMjMrNRENCWGqhHPJb415mQs2JBthVgJTc0SZGGNKYqqlylow+L0p/Xgjti9Ur4S/sR7chRtpOSuwkJgbVr0CG0Wfik/1pbuEabIhMS6jzE6o9a0Fteyn2p0qz/QKPV8MAyb7xGCX7HjARHzYj1B5uYOZFyGj82t0D54rM/bXSL2ffq/Vq6Do6ZW+G56R4gXCdbVsRI4VMJYxfErmivqiRDysolXgyNKeMqrGBBnzBv1nQm7GC07SDtjr0ZboNn6o87TTXH5+Bisa8xGpdO2OnyTzBgG0hAQrOIdYMG6Kp5JH3hkzl7fu+90513Has5zBAn3U5/2YEraUiY9NO1VuPiSB4C5GSGGAnW37Xm7E+MDrAty2FRPrO2I9Vn14lKZUgJl12D1CJVZ0Ddso/nVxDlMZHqRchv6zFeeYtBnya6tRQPm15A09JMyWzfIHsSLmnGrSZwHypPCWwywp5oIGcLD1Yeqphy5CkIw5NiSXG6+CjEz2DBC3zOkX+yHyJvu1Z5yL5h/sscE5tt9bv/7j0QUrBbQCe0tElOtMMYn2s8sdp/IMGzejIdPCMD0aLh6WILPU=",
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
