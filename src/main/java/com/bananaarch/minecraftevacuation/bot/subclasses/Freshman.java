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

public class Freshman extends Bot implements Genderable {

    private final Map<Gender, String[]> skins = new HashMap<Gender, String[]>()
    {{
        put(Gender.MALE, new String[]{
                "ewogICJ0aW1lc3RhbXAiIDogMTY4NjA1NTg2MDc0NSwKICAicHJvZmlsZUlkIiA6ICIzNjMzMGY2YzI5NmY0NjJmYjQ3M2I4MTc4NjczMzBhMSIsCiAgInByb2ZpbGVOYW1lIiA6ICJoZWFydHlkaWUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc2OThiODdhYTE0ZTIzZDJmZTE0MjgxM2Q2NDU3ZDdmMDUwZmFlYWYyNzI3M2E3MGJjN2JhOTJjNjMxMDFiZCIKICAgIH0KICB9Cn0=",
                "s7felG+MaHsAQsFTKpZ8BUamP9Kn+YsP6uj6L/SCC1gyr01b27fO6xo/dz0soJXiF2kTygpeDDALmDnpCCHpWbpYmb+qwxZfQpuKVTzo4h9Ljx5M/M66MKkp2hsSJDBN1bpzqrbwtLbHWfdKfHmca6+usETEgt8mScfQnYVJZByx4QR+Gux3bsYHQmuRN6gPe+UTUx1NJ8j3tvjftCvUNGgJQEhkuT90gzLP9tiJzi/27mpm0inyCTylxIo/vhfW5z+c+jrL4s6sNxugiCcEzew4VLhbFK8ZiuOLxrcLTx+VzaU6oK8EA/pXijQ4TPDZrLx9mDvQQWEqArpyBQ7sNzHbjJhQRU1/GjvdYJ4TsDI33AyriP4rv87pemvREwlGXdIcxm6TrbiqEFmRUmjXhfLqq0o/YeW8VS/MZ7440PEmqZpjV0eZ8nqYiThye4mA9MxwiPbaceIx9vZS3KVjCQD5cAv5pK+sBupOZ8Rv96M2+SWBvaV+EughlwygzyUvZe9xK8/idGSgtxCuwgHey/U50uLJjydlWrv7mvHxFuWNWlOMo9ojqoCWhQRMb6S74y6d2MYFUPHxfRfRI2YCmXKQ8WMgVvFDIHBTaON4fT4iFeaDrpxl2XKbIFUJ40qdK3a5/rnFh8W3LlYjEsy3xJfYSo5BH/hUog/Rrb7SIkM="
        });
        put(Gender.FEMALE, new String[]{
                "ewogICJ0aW1lc3RhbXAiIDogMTY4NjA1NTkzMzcwNywKICAicHJvZmlsZUlkIiA6ICJlYmRhZjExM2Y5ZTQ0OTJiOGM2MzUxMTVmYmYxNzVjYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJhbmRpeWFzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzE2NzI1ZDQ5YmU4MWZhMDU2ZWFiZGRjMTNjYWI0ZDMwNjVmZmI3YTg2MDIzMDAwZTAxZGViZGU3ZDY3NGRkYmMiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                "xcF4AHwKNkswWXzbYIp9jIBBa1iHPGLVomRLIJU1ElYrQ40+QzQcCwuCrHElMU4UQapjE4pjrhzrURElR/r2+8bKe2GEn81tSvqeLyZU8upK3cTTgkc399gz+hA7lSekL7Zmkd4xOF0ZanOTAVKb19PCYXacfnu0NUWL65J3PUbEqmdNH5+wYRjzQeegiU42LuXUU+Q7PikwBImkK6reLrks9M+v0nPW8YXQCAgXtH3CLYg40muv4fPLZokonQ34tLKe/udcM3hFZcdNFe5WM/lC3BkU4jHIo1DlEzw23C/Ox3A+vIyD1Xz+CPE/z7p4Z0NS3sWnyKOWRRO8cIHD6aUGd8AwC8/21gvNyYqpWd7KtRBxJyvuYWb6lOJrTzYiGdjG3ZcgLUF0jr+g0qHyaST05DgpTOEOGu5RyQ4LwwU1XHEERD3v0VGrPIKrbGDGryhdwc80MPCClzAUADwB53pf9dC9NFOBMeF9uDwhWXm0QAbA+mzwDiQDMNPABOozTTlseg1LMlWcOZpUYjYM4Nhi6nQLtMj5GVtgcosIkBg32bbC/PwtrNp1ohDr4EotFp6oQig2NjnoTR2LItKR62x6bVcYntCzmyp7hzk50dAxda8oO1Xnoh2s+a0ueZPyccILLf2/ZwcnZoCNL8lgeJFQWGn3KyjytsLqOEN7IDY="
        });
    }};

    private Gender gender;

    public Freshman(MinecraftServer minecraftserver, ServerLevel worldserver, GameProfile gameprofile, Location initialLocation) {
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
                ChatColor.GRAY + "Bot Type: " + ChatColor.WHITE + "Freshman",
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
