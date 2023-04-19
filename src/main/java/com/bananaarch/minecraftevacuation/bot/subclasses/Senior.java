package com.bananaarch.minecraftevacuation.bot.subclasses;

import com.bananaarch.minecraftevacuation.bot.Bot;
import com.bananaarch.minecraftevacuation.bot.Genderable;
import com.bananaarch.minecraftevacuation.utils.Gender;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class Senior extends Bot implements Genderable {

    private final Map<Gender, String[]> skins = new HashMap<Gender, String[]>()
    {{
        put(Gender.MALE, new String[]{
                "DWlByASeCe9QCIe2EDwg9lYLb43tu6HL5EauC0yaMhexYCWu638jRAqSOZyeO0oxRWhQz9y4chnpwlUPeA/0nmcjbM4ah3A04sHex5eXj6UyQEOshjBTA4HijmM4YU51ndO02ooOiOWmTwaDNw2T2FoLFyEj7/KNsZN5OWL8yE4Oo8VzyKm34D0AzXBiT1vgjkvUH+VHuHTsYc4hv1l73K3gELmXzdm2VBUDnDqw5RAIelldaiEmlma4FfYCO+Zrgmyy8xIQb8CmZRVKOpDWKS+s8h5J294YcLCPUV2gFef9jV5OT7jn+VGNMRRiOEp6QFFi1CvXWC2ZibLSCedlcxcZ8b9PCwlhOw+cui79isQQcl1SMOmaznwg26Zs+bJ/PZtD39R9e1ClEL75dpUAS5I+iwIDt4elh/y81W++zeeb7icDj5zyTvT4lVspGkQoOIzue6iaTobzD0VPKwTzJfqg+/YAdPTQafb9XcTHkt17inKTedFiF3yIlhIky80wNB1l09j/axs/PMlPBeVhkhjHezQBK1EgPoO/EITW7I8BkKnhTHUv78ZEd9BEcpU2GOkpeGSRt7P/ThBvdrxKpVV6XqXDUqRxB7fEYJ8YbZPodh65s29QmhIGmydkmft6AU69uwqphGWbLLnMljEdkIgiAcBy+TeYjc/d++4Squw=",
                "ewogICJ0aW1lc3RhbXAiIDogMTYzNDk4NjczNzgwNSwKICAicHJvZmlsZUlkIiA6ICJmMTA0NzMxZjljYTU0NmI0OTkzNjM4NTlkZWY5N2NjNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJ6aWFkODciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmM2MzQ4MDI4NjRiMTBlZDc5ZTE5MDY4ZWNkMDk0ODdmZWM2ZDFmMmM2MjAwNGEwZGFhMGMxZDdlNTFhNjlhIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0="
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
        this.gender = values[(int) (values.length * Math.random())];

        setGender(gender);
    }

    @Override
    public void setGender(Gender gender) {
        String[] skin = skins.get(gender);
        if (skin != null)
            setSkin(skin[0], skin[1]);
    }

    @Override
    public Gender getGender() {
        return gender;
    }

}
