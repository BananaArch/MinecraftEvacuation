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

public class Teacher extends Bot implements Genderable {

    private final Map<Gender, String[]> skins = new HashMap<Gender, String[]>()
    {{
        put(Gender.MALE, new String[]{
                "ewogICJ0aW1lc3RhbXAiIDogMTYzMjk2NTM0MjI3MywKICAicHJvZmlsZUlkIiA6ICIzNmMxODk4ZjlhZGE0NjZlYjk0ZDFmZWFmMjQ0MTkxMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJMdW5haWFuIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzJmZGFjYzE0ZWFkMzJlMTlhMDFmMjI4YjFhMGY1MDljODUwZDA0MmNlYmNkMzE0Mzg5Nzk2ZWNmMDA4NzE3ZGYiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                "eaa4iNT4ae639Y3kdmOhPaOAjt4t3IBuThv1kyrTyL12N+5ZkS9sT/FIA58/f+dUmbLjmbI8migonF5clUPzR8c9/khpklA1dXomh9qRE5mqS4blw6rMbg8qMG1Arha3l2emQz1LWeXXkjw314+ZvanZpbVtjGfKO1+16onHrYJyfCw9LE/We/3DA3CIQ846cvEd5xGAafSZ5OjXQ2NC7r9wE0+ukxzKD/iFqSZXZ7qcmpg5kHBHcgeZyiE1JGL6fdeeWnzF/IYsi/ZVHJMInkNCv+Hsfas8qqJa5mmOd+LSOyanvlVy4L9efvPuHlFSEeIvZHGONIXBmiPgPObRtuL2PDEN2/50AfrvkEurTcEGIWEqluWA13RL+W73Xr3QBE0tAxNFcE7m3aMlzUEUbczqMsUY0VXVJZ46gHTnO32OkL9F/VoXQRyeg6MzsHKTzwZR79L3+wXSuKsLlHuM42jKvJwioMYPZkG57rP6ZQsV2U22B0FfmbDPp0Bf6/Fdj6K2EPNoWgQE47pI7iyZaQ/DRiqqXqLCj1QpE4/gw4XLCyNTYmyF96xlKegUw5K/hFacgrEKOe6r5ixjvhFqFO7d4IKgJtVHeJcv1A9ySGAdRAveOENGF+rewwwQid/q6W6NhBA38mqBo0qRNCzbu3G8bEJ+QE1f0iUZ5Cp3lY0="
        });
        put(Gender.FEMALE, new String[]{
                "eyJ0aW1lc3RhbXAiOjE1NzE2MTY3NTQ5ODQsInByb2ZpbGVJZCI6IjkxOGEwMjk1NTlkZDRjZTZiMTZmN2E1ZDUzZWZiNDEyIiwicHJvZmlsZU5hbWUiOiJCZWV2ZWxvcGVyIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84YzgzNThiYjg4M2JhZDNkMDZlMTAxOWIzNWI1OGMxYTg1NzViY2E0ZGFkNTk4MTFiNWNmZTdmZjdiYmM4ZDcyIn19fQ==",
                "wciWmxItdXcGQbK+O0rOdjdBip47r8R0bHU8sBOQPoP3DmU8aXlstjLQB0nw5kjoye0yWDnNz9S+nuhYGnlHfabPHbc9lIO+/STsbB1L/xfZ8I46uQGEAyIA4Ht3z1eBt3L1DH6ioWG5XKmpf1i/5izhMN5DONih27jaBjEv+QRg/wWWzJ9GvFoEk9VO7GDGGDl3nsB3/MWpWJIk+ejIagS6Sml9MIOBnUmvijvmhG8KMT1r2QuhpmvrmhAGAvXkKv2M+8o3N8ICVTe53wONhoi/7/uIJSXy8e89KhVxTCIT88bh40kOxCShn8K8zWe8ng98yxuVvE0nkim2MDkCYTIkRx/izGTAwyhpT0jG5I1XW3d16q9iYcT6AjI927xldKOZjkz60KwjXPbCT1VdhMdTDu5uR6ELNxYqUaUxSrYZMEWzGCbfje3LlRDmxEr0VsYxTTnQGJbUkJIN+ldERTzWeH6qKDX58ylmewPuVDRKxsT4mdGCBDiYtKAHgWlqNSr8B9I8y48Tnq2i+20V+Xlchbhj+gJKUULNiqMwK55CcqNuU2sytQ2eVWztqGHDTQo5Xff/xfsFXQfSW6iMO/S7QBYh/BHaI4PZxsmnTAE+fNSD07kK/kvw0aRTzS/5u18AFVEYSKNcdWsisZ81KNhf9coDVlptpfnQwnqSBo0=",
        });
    }};
    private Gender gender;

    public Teacher(MinecraftServer minecraftserver, ServerLevel worldserver, GameProfile gameprofile, Location initialLocation) {
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
            ChatColor.GRAY + "Bot Type: " + ChatColor.WHITE + "Teacher",
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
