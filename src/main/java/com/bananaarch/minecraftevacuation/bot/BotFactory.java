package com.bananaarch.minecraftevacuation.bot;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.subclasses.*;
import com.bananaarch.minecraftevacuation.utils.BotType;
import com.mojang.authlib.GameProfile;
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
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;

import javax.annotation.Nullable;
import java.util.UUID;

public class BotFactory {

    public static Bot createBot(Location initialLocation, String name, BotType botType) {

        Bot bot;

        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel nmsWorld = nmsServer.getLevel(Level.OVERWORLD);

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name.length() > 16 ? name.substring(0, 16) : name);
        boolean isMale = Math.random() < .5;

//        --see type--

        if (botType.equals(BotType.FRESHMAN)) {
            bot = new Freshman(nmsServer, nmsWorld, gameProfile, initialLocation);
        } else if (botType.equals(BotType.SOPHOMORE)) {
            bot = new Sophomore(nmsServer, nmsWorld, gameProfile, initialLocation);
        } else if (botType.equals(BotType.JUNIOR)) {
            bot = new Junior(nmsServer, nmsWorld, gameProfile, initialLocation);
        } else if (botType.equals(BotType.SENIOR)) {
            bot = new Senior(nmsServer, nmsWorld, gameProfile, initialLocation);
        } else if (botType.equals(BotType.TEACHER)) {
            bot = new Teacher(nmsServer, nmsWorld, gameProfile, initialLocation);
        } else {

            System.out.println("Invalid bot type");
            throw new IllegalArgumentException();

        }

//        ------------

        bot.connection = new ServerGamePacketListenerImpl(nmsServer, new Connection(PacketFlow.CLIENTBOUND) {

            @Override
            public void send(Packet<?> packet, @Nullable PacketSendListener callbacks) {

            }

        }, bot);

        bot.setPos(initialLocation.getX(), initialLocation.getY(), initialLocation.getZ());

        Bukkit.getOnlinePlayers().forEach(p -> ((CraftPlayer) p).getHandle().connection.send(
                new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, bot)
        ));

        nmsWorld.addNewPlayer(bot);
        bot.renderAll();

        MinecraftEvacuation.getInstance().getBotManager().addBot(bot);

        return bot;

    }
}
