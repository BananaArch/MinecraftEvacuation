package com.bananaarch.minecraftevacuation.ux.listeners;

import io.netty.channel.*;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class PacketListener {

    public void inject(Player player) throws NoSuchFieldException, IllegalAccessException {


        ChannelInboundHandlerAdapter channelInboundHandlerAdapter = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object rawPacket) throws Exception {
                if (!(rawPacket instanceof ServerboundInteractPacket)) return;

                ServerboundInteractPacket packet = (ServerboundInteractPacket) rawPacket;
                Field action = packet.getClass().getDeclaredField("b"); // b is the obfuscated "action
                action.setAccessible(true);
                Object actionData = action.get(packet);

                if (actionData.toString().split("\\$")[1].charAt(0) == 'e') return;

                Field hand = actionData.getClass().getDeclaredField("a");
                hand.setAccessible(true);
                if (!hand.get(actionData).toString().equals("MAIN_HAND")) return;

                Field id = packet.getClass().getDeclaredField("a"); // a is the obfuscated "entityId"
                id.setAccessible(true);
                int entityId = id.getInt(packet);

                super.channelRead(ctx, rawPacket);
            }
        };


//        I HATE NMS

        ServerGamePacketListenerImpl serverGamePacketListener = ((CraftPlayer) player).getHandle().connection;
        Field connectionField = serverGamePacketListener.getClass().getDeclaredField("h"); // h is the obfuscated "connection"
        connectionField.setAccessible(true);
        Connection connection = (Connection) connectionField.get(serverGamePacketListener);
        ChannelPipeline channelPipeline = connection.channel.pipeline();
        channelPipeline.addBefore("packet_handler", player.getName() + "_read", channelInboundHandlerAdapter);


    }

    public void stop(Player player) throws NoSuchFieldException, IllegalAccessException {
        ServerGamePacketListenerImpl serverGamePacketListener = ((CraftPlayer) player).getHandle().connection;
        Field connectionField = serverGamePacketListener.getClass().getDeclaredField("h"); // h is the obfuscated "connection"
        connectionField.setAccessible(true);
        Connection connection = (Connection) connectionField.get(serverGamePacketListener);
        Channel channel = connection.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
    }

}
