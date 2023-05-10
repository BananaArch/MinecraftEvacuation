package com.bananaarch.minecraftevacuation.ux.listeners;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.tasks.TaskManager;
import com.bananaarch.minecraftevacuation.ux.events.UniversalEntityInteractEvent;
import io.netty.channel.*;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class PacketListener {


    private final TaskManager taskManager = MinecraftEvacuation.getInstance().getTaskManager();

    public void inject(Player player) throws NoSuchFieldException, IllegalAccessException {

        ChannelInboundHandlerAdapter channelInboundHandlerAdapter = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object rawPacket) throws Exception {

                if (!(rawPacket instanceof ServerboundInteractPacket)) {
                    super.channelRead(ctx, rawPacket);
                    return;
                }

                ServerboundInteractPacket packet = (ServerboundInteractPacket) rawPacket;
                Field action = packet.getClass().getDeclaredField("b"); // b is the obfuscated "action
                action.setAccessible(true);
                Object actionData = action.get(packet);

                if (actionData.toString().split("\\$")[1].charAt(0) != 'd') { // if not right click
                    super.channelRead(ctx, rawPacket);
                    return;
                }

                Field actionType = actionData.getClass().getDeclaredField("a"); // ActionType
                actionType.setAccessible(true);
                if (!actionType.get(actionData).toString().equals("MAIN_HAND")) { // if not main hand
                    super.channelRead(ctx, rawPacket);
                    return;
                }

                Field id = packet.getClass().getDeclaredField("a"); // a is the obfuscated "entityId"
                id.setAccessible(true);
                int entityId = id.getInt(packet);

                System.out.println(entityId);
                UniversalEntityInteractEvent event = new UniversalEntityInteractEvent(player, entityId);
                taskManager.scheduleTask(() -> Bukkit.getPluginManager().callEvent(event), 2);
                taskManager.runTask(() -> Bukkit.getPluginManager().callEvent(event));

                if (!event.isCancelled())
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
