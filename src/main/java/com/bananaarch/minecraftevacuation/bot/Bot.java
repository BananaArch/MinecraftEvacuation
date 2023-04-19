package com.bananaarch.minecraftevacuation.bot;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.utils.BotType;
import com.bananaarch.minecraftevacuation.utils.GameStateUtil;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.reduce.same.Min;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.UUID;

public abstract class Bot extends ServerPlayer {

    private final BukkitScheduler scheduler = Bukkit.getScheduler();
    private Vector velocity;
    private Location initialLocation;


    protected Bot(MinecraftServer minecraftserver, ServerLevel worldserver, GameProfile gameprofile, Location initialLocation) {
        super(minecraftserver, worldserver, gameprofile);

        this.initialLocation = initialLocation;
        this.velocity = new Vector(0, 0, 0);
    }

    public void renderAll() {
        Packet<?>[] packets = getRenderPackets();
        Bukkit.getOnlinePlayers().forEach(p -> render(((CraftPlayer) p).getHandle().connection, packets));
    }

    public void hide() {
        Packet<?> destroyEntityPacket = new ClientboundRemoveEntitiesPacket(this.getId());
        sendPacket(destroyEntityPacket);
    }

    public void show() {
        Packet<?> addPlayerPacket = new ClientboundAddPlayerPacket(this);
        sendPacket(addPlayerPacket);
    }

    private void render(ServerGamePacketListenerImpl connection, Packet<?>[] packets) {
        connection.send(packets[0]);
        connection.send(packets[1]);
        connection.send(packets[2]);
    }

    private Packet<?>[] getRenderPackets() {
        return new Packet[] {
                new ClientboundAddPlayerPacket(this),
                new ClientboundSetEntityDataPacket(this.getId(), this.entityData.packDirty()),
                new ClientboundRotateHeadPacket(this, (byte) ((this.yHeadRot * 256f) / 360f))
        };
    }

    public void sendPacket(Packet<?> packet) {
        Bukkit.getOnlinePlayers().forEach(p -> ((CraftPlayer) p).getHandle().connection.send(packet));
    }
    
    public void tick() {
        super.tick();
        this.move(MoverType.SELF, new Vec3(velocity.getX(), velocity.getY(), velocity.getZ()));
        baseTick();
    }

    public void walk(Vector velocity) {
        double max = 0.4;
        addVelocity(velocity);
        if (velocity.length() > max)
            velocity.normalize().multiply(max);
    }

    private void look(Vector vector) {
        float yaw, pitch;

        double x = vector.getX();
        double z = vector.getZ();

        double theta = Math.atan2(-x, z);
        yaw = (float) Math.toDegrees((theta + 6.283185307179586D) % 6.283185307179586D);

        double x2 = NumberConversions.square(x);
        double z2 = NumberConversions.square(z);
        double xz = Math.sqrt(x2 + z2);
        pitch = (float) Math.toDegrees(Math.atan(-vector.getY() / xz));

        sendPacket(new ClientboundRotateHeadPacket(getBukkitEntity().getHandle(), (byte) (yaw * 256 / 360f)));

        setRot(yaw, pitch);
    }

    public void setSkin(String value, String signature) {

        //  https://mineskin.org/
        //  value, signature
        getGameProfile().getProperties().put("textures", new Property("textures", value, signature));
    }

    public void addVelocity(Vector vector) {
        if (NumberConversions.isFinite(vector.getX()) && NumberConversions.isFinite(vector.getY()) && NumberConversions.isFinite(vector.getZ())) {
            velocity.add(vector);
        } else {
            velocity = vector;
        }
        look(velocity);
    }

    public Vector getVelocity() {
        return velocity.clone();
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
        look(velocity);
    }

    public BoundingBox getBotBoundingBox() {
        return getBukkitEntity().getBoundingBox();
    }

    public Location getLocation() {
        return getBukkitEntity().getLocation();
    }

    public INDArray getGameState() {
        return GameStateUtil.getGameState(getLocation());
    }


}
