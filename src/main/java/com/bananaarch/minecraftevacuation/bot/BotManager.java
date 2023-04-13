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
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Objects;
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

    public void createBot(Player blockPlacer, Location blockLocation, String name, BotType botType) {
        blockPlacer.sendMessage("yo");

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name.length() > 16 ? name.substring(0, 16) : name);
        boolean isMale = Math.random() < .5;
        String[] skin = botType.getSkin(isMale);
        gameProfile.getProperties().put("textures", new Property("textures", skin[0], skin[1]));

        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();


//        System.out.println(Level.OVERWORLD.toString());

//        ServerLevel nmsWorld = new ServerLevel()
//        ServerLevel nmsWorld = nmsServer.getLevel(worldKey);

//        ServerLevel nmsWorld =;
//        ServerPlayer player = new ServerPlayer(nmsServer,  Objects.requireNonNull(), gameProfile);

//        Bot bot = new Bot(nmsServer, nmsWorld, gameProfile, blockLocation, isMale);


        System.out.println("HI");
    }

//    public void createBot(Location initialLocation, String name, BotType botType) {
//
//        System.out.println("Ran command");
//
//        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
//        ServerLevel nmsWorld = ((CraftWorld) Objects.requireNonNull(initialLocation.getWorld())).getHandle();
//
//        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name.length() > 16 ? name.substring(0, 16) : name);
//
//        boolean isMale = Math.random() < .5;
//        String[] skin = botType.getSkin(isMale);
//        gameProfile.getProperties().put("textures", new Property("textures", skin[0], skin[1]));
//
//        System.out.println("Creating bot");
//
//        Bot bot = new Bot(nmsServer, nmsWorld, gameProfile, initialLocation, isMale);
//
//        System.out.println("Created bot");
//
//        bot.connection = new ServerGamePacketListenerImpl(nmsServer, new Connection(PacketFlow.CLIENTBOUND) {
//
//            @Override
//            public void send(Packet<?> packet, @Nullable PacketSendListener callbacks) {
//
//            }
//
//        }, bot);
//
//        bot.setPos(initialLocation.getX(), initialLocation.getY(), initialLocation.getZ());
////        bot.setRot(initialLocation.getYaw(), initialLocation.getPitch());
////
//        Bukkit.getOnlinePlayers().forEach(p -> ((CraftPlayer) p).getHandle().connection.send(
//                new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, bot)
//        ));
//        nmsWorld.addNewPlayer(bot);
//
//        bot.renderAll();
//
//    }

    public Set<Bot> getBots() {
        return bots;
    }

    public BotAgent getBotAgent() {
        return botAgent;
    }
}
