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

public class Junior extends Bot implements Genderable {

    private final Map<Gender, String[]> skins = new HashMap<Gender, String[]>()
    {{
        put(Gender.MALE, new String[]{
                "ewogICJ0aW1lc3RhbXAiIDogMTY4NjA1NjA3MDg3MywKICAicHJvZmlsZUlkIiA6ICJjNDdiNWNmNDBkNTU0MWNjYjFiNTE1ZjRiNjA3ZWQzOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJTdHJhbmdlckd1cyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jZTVjZTgzOTBkOGNhNjM5MWMwNjc0NDYyYTFmM2EwOTgzMWY0NzdjZjNjNjcyMjg1Nzc0ZjhhYzZiYjQxYTk2IgogICAgfQogIH0KfQ==",
                "C9jSB8y4NZCn1FoZ6XRWFz8I3Lg2/UmDHz0VgOQB52CglWD3gpeDbrxylH+XMYKmGLrR10BCVXG+/zEFAm7cZEc6SxzQlYZziSZfrrEiUpbJbN3E96EJJD4C5W+ULEF1VZt/NQbCByBK6KNENjMotig4cX+dcmKnps8e3HdTjRhMgpkIwoZQDU2Im/zNW/pJ9U4wjHQEXezufl2nq4+p+7JsQgrtHUm7EHNZKwr62nq9ppIjMFGE+UHy0KHE0VpWOzaYgyIqjFQ/Cm4nuvpXlF2pf0sirQ5s2GQJc3kWcFGyeb8cJ9sWRWJE2cruaWhdjI1BdCa85xnrFqe3V4B9lUD+wN+EoHlfSQVgePK3+jJSHItFhtFhW2YVmYFJluCEJ850mjyauGq40j7rzScuzMbX64+q2JnkTqQMac6hTCwZP1t90CVBJgB4WAG+3AcDg2Ht4WYZ9Z53HGdFh58u/P3suCHb90LmXXvom0aFa/BOfFtesYTffpm24vn6J2LVO0b50F2Qm2wl8cjN/yIe+asLrdFxRHPSZRALcVw4q6/fhRBxHu/qVKyjlldnK6CdDaSCS3vl2FWhsWGEu5UYk816rdMnqpBqE/BtLDiRFgMw6spoCwM4U68iCIBH29yOftWBkTRJdflIePUOtLPCq+LhlCh152yqJ4BUwmcaqsw="
        });
        put(Gender.FEMALE, new String[]{
            "ewogICJ0aW1lc3RhbXAiIDogMTY4NjA1NjAxNDY4NCwKICAicHJvZmlsZUlkIiA6ICJkYmEyNmVkNTk2ZmE0NjBjOTZjOThhYWYwOWM2MDZhNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJPcmlnaW5hbFJlemEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTY2MmRjMTVlYzAyNTJlOTFkNTBjMjFiN2Y1YjQzYzMzOGY1MjY3OTY0Njc1NDlhOTY2NTg3YmZhNTFmMjc5ZCIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
            "hDNcZ4+6mivAgDwAILEcwBu9D/y8VPgY0mZ4n2n9Fy6DVdJBZerTEsrj4jQrC9ujOCl9zVO88O8YiQEdqq85e71KObiv1h6nylE8MnDLVgQ3JWHLQtiYMNtAN4JII4G21L9EEubO9sOcz8/i/5sboeQVEEJpokwkuH7Ct1DmIUDLOIepnjWFTW0xu0IcHfA3dYPoTWY9Lu9rDsz9hHvFB2AauCHeCT3l8Y1miiRuuytr9b7fk9qtmFw0ZEvdxo5eJ5VNX8VITRfZeMEdUXJwTymCbZ9YjZwt21aiiKRRNLhDwNridUUFmBjVqzlHkoAc6Z+Xz3y3J9Uge9RxQgIlW70qNQDqU1WY6oA2urUmQ7+Lqxo7OOsafoc4tIE+3UYPFdi6kyyROMj9LKpzfTTqZELJ+0mVHxY7ArAWIUCQhfnb6t6jRHNJH/HX2mDNYP4WTcZkjoDCF2cu0IF65+5Hqk9w/82zEG5DeP1/YsFwYbsmqEivFuvV2daxvbUmWwJMGf8if/NEf8p2hUxpXTske+2EUyBrauC69Z+JlgzFNMJRqDxA6Xwbdu9y6jOPcoW5vyXIOVc9fmSwvg+DtRw+e3TIDZqali9Fr0zoBREs/teAYxnyiZuNPxR+estzAHkCpIfWEsgYEjUNUkp/5H+RyDRif2xd+VzmuOdM09FYNP8=",
        });
    }};

    private Gender gender;

    public Junior(MinecraftServer minecraftserver, ServerLevel worldserver, GameProfile gameprofile, Location initialLocation) {
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
                ChatColor.GRAY + "Bot Type: " + ChatColor.WHITE + "Junior",
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
