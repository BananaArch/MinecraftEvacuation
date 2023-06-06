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

public class Sophomore extends Bot implements Genderable {

    private final Map<Gender, String[]> skins = new HashMap<Gender, String[]>()
    {{
        put(Gender.MALE, new String[]{
                "ewogICJ0aW1lc3RhbXAiIDogMTY4NjA1NjQwNjk4NSwKICAicHJvZmlsZUlkIiA6ICI3MDQ0ZDlkY2I5OGI0YzgzYWFjNjIzNjFlYTY5YmNmOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJfV2F0cnlzaGthXyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xZDc2N2I5MGU2NTliOTI0NDYyZmMwNDE3NTBmOTMwYzZlYmJjZjQ0YzY3MTE4M2IyZGU2NTZkY2I3ZDE0MTQ1IgogICAgfQogIH0KfQ==",
                "FfiziBy5OIiolU5gR5w1GP3s4ehlXHEznuybrXKn7nWH7aAo3+4nUNvBrYDaSQ1u1UxpRrQXGnfen6G+C0RFLShGwQnZGJ+ouAt2dTU8Uq2duZ/b5x4z/cM323jgbsWxg0jjLs+cKXdjHR+fK022toE9wSb+bQ18PU6QsVH4E38NsKEdTVj6KoLRacCHJjy5/YRPz1r5c8xHRk0BhIPqEYTeWKy6XzM1X61dd9FT83bX09DIl8UCSjA9KlyOCJX50X5qCI9mtOz8ForwVeJlohrTvF7qoI+Ax+DUFdhuQa+1Osvt2M2adMWDNSSbhjYOMqKlURkV4aVMkEauAD5p/kwZ/I63O4CWJ8p6vG3lhnmdaEkR8OERpdrphNFdKjsjV1P1ubFCKEviTv45ivaQN8F9yBWasdM+5yH8IfNWyFWrQH6h8BP6JFB2EbN7fCrT6eL0vA82mpgZYJSs1tibMOxboftcMFJer6SdypKcJ4eWM0ya55BiJskF6dgng26xl0kiC6CZwh4M8vYAkKQFL77Hfb9xDgtNnSrvmvpANrIr5mkhWPDaKuHzhmU7zaywc9IRHsvUhN50bVX+pKMYaJoK+eLcLxgOe/T90BMRSe1I4RLDT26/gOVTMTVH4oeC3JQO/ow8BfZfpONToLX6VztbmM28uZ/GrFYAcE7Z6Vc="
        });
        put(Gender.FEMALE, new String[]{
                "ewogICJ0aW1lc3RhbXAiIDogMTY4NjA1NjMzODM4NywKICAicHJvZmlsZUlkIiA6ICI1MDBmZWZlMWUzZjU0NTA4OTc1YzZmMTE5ZGIyZDY1NiIsCiAgInByb2ZpbGVOYW1lIiA6ICJUZWNoU2VlZCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81N2ZhNGU5MGIwMmQzNGY1NmE5MTVmNzhiMDcyNGYzNWY2MzQ4MDg4MmQ3NTVmYTg5ZmQ2ZTkzZDRiZDc3MTY3IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
                "AuFy1wWhAEFXbVArpXCzobdE9ICu6tlj1eECHMqdpNmKxAxkuY3CSJXV9aF3UOUbDB4fOwy+gmMyVMsWU4PyLsP+Fr3XHiTvKud7BvrLTvxHOaWRM1Eo6lvzzpiJxQeLkMnE2KC7EMib0uCJ8Wy/LJG0xFBiwHGeIPL5N8cqjWKd8h4zh5N1hlwyevZJNLndJb2fA6XTnbrS4HmxU3Bl1UGyiWke+QrigAHMPSz5poXHijOMfE2tyU+sDzhO2hRSxxAg/1Jw14+uz3TarWGVtJQ5LuIwDp+Mx1jIt/Eqqi/5yFnw52uszy14nMh1L+foiTIiInf2bb3nFA9BaHAUztRnuQmQDMg4eBfHjJXql6KP604eNHEVs4ZkSpKIZ5Y2Id1kAcHLPc9xm48/dgkHTHCHtzYFgtq+CcfI6xDS40U7u8cynvQgnbDh/XYcM4TR5FckxamWkKA7N26BodgDdtV44x3m8dLHwH/xmRekCF8YAWbTYeyrX2excAfczkCSXeOtFDHV1iAWwixQ6wheh5vye/Lq9kX8uCvjXMhOc2cIvXOIK+ahZ2k5CWJmJllugcD20xUJ4CenBR/8MuTt2A8tNWp3kIkqMAWKLUFKAfKLRUXNN/lvt2AYmeA7JuVuGfj3Rk944WUCHI0VCh/oYIGDz/xDOAyKAb1BhZ7twKY=",
        });
    }};
    private Gender gender;

    public Sophomore(MinecraftServer minecraftserver, ServerLevel worldserver, GameProfile gameprofile, Location initialLocation) {
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
                ChatColor.GRAY + "Bot Type: " + ChatColor.WHITE + "Sophomore",
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
