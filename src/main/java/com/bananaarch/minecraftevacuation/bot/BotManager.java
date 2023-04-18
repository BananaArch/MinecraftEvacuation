package com.bananaarch.minecraftevacuation.bot;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.utils.BotType;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BotManager {

    private final Set<Bot> bots;
    private final BotAgent botAgent;

    public BotManager() {
        this.bots = ConcurrentHashMap.newKeySet();
        this.botAgent = new BotAgent(this, MinecraftEvacuation.getInstance());
    }

    public static void createBot(Location initialLocation, String name, BotType botType) {

        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel nmsWorld = nmsServer.getLevel(Level.OVERWORLD);

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name.length() > 16 ? name.substring(0, 16) : name);
        boolean isMale = Math.random() < .5;
        String[] skin = botType.getSkin(isMale);
        gameProfile.getProperties().put("textures", new Property("textures", skin[0], skin[1]));

        Bot bot = new Bot(nmsServer, nmsWorld, gameProfile, initialLocation, botType);

        bot.connection = new ServerGamePacketListenerImpl(nmsServer, new Connection(PacketFlow.CLIENTBOUND) {

            @Override
            public void send(Packet<?> packet, @Nullable PacketSendListener callbacks) {

            }

        }, bot);

        bot.setPos(initialLocation.getX(), initialLocation.getY(), initialLocation.getZ());
//        bot.setYRot(3);
//        bot.setXRot();

        Bukkit.getOnlinePlayers().forEach(p -> ((CraftPlayer) p).getHandle().connection.send(
                new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, bot)
        ));

        nmsWorld.addNewPlayer(bot);
        bot.renderAll();

        MinecraftEvacuation.getInstance().getManager().addBot(bot);

    }

    public Set<Bot> getBots() {
        return bots;
    }

    public BotAgent getBotAgent() {
        return botAgent;
    }

    public void walk() {
        for (Bot bot : bots) {
            bot.walk(new Vector(1, 0, 1));
        }
    }

}
