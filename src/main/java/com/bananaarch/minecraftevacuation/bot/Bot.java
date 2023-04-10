package com.bananaarch.minecraftevacuation.bot;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.utils.BotType;
import com.bananaarch.minecraftevacuation.bot.utils.BotUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public class Bot extends ServerPlayer {

    private final MinecraftEvacuation plugin;
    private final BotAgent botAgent;
    private final BukkitScheduler scheduler;
    private Vector velocity;
    private Location initialLocation;
    private Location targetLocation;

    private Bot(MinecraftServer minecraftserver, ServerLevel worldserver, GameProfile gameprofile, Location initialLocation) {
        super(minecraftserver, worldserver, gameprofile);
        
        this.plugin = MinecraftEvacuation.getInstance();
        this.botAgent = plugin.getManager().getBotAgent();
        this.initialLocation = initialLocation;
        this.scheduler = Bukkit.getScheduler();
        this.velocity = new Vector(0, 0, 0);

    }

    public static Bot createBot(Location initialLocation, String name, BotType botType) {
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel nmsWorld = ((CraftWorld) Objects.requireNonNull(initialLocation.getWorld())).getHandle();

        UUID uuid = BotUtils.randomSteveUUID();
        GameProfile gameProfile = new GameProfile(uuid, name.length() > 16 ? name.substring(0, 16) : name);

        String[] skin = botType.getSkin();
        if (skin != null) {
            gameProfile.getProperties().put("textures", new Property("textures", skin[0], skin[1]));
        }

        Bot bot = new Bot(nmsServer, nmsWorld, gameProfile, initialLocation);

        bot.connection = new ServerGamePacketListenerImpl(nmsServer, new Connection(PacketFlow.CLIENTBOUND) {

            @Override
            public void send(Packet<?> packet, @Nullable PacketSendListener callbacks) {

            }

        }, bot);

        bot.setPos(initialLocation.getX(), initialLocation.getY(), initialLocation.getZ());
        bot.setRot(initialLocation.getYaw(), initialLocation.getPitch());

        Bukkit.getOnlinePlayers().forEach(p -> ((CraftPlayer) p).getHandle().connection.send(
                new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, bot)
        ));
        nmsWorld.addNewPlayer(bot);

        bot.renderAll();

        MinecraftEvacuation.getInstance().getManager().addBot(bot);

        return bot;
    }

    public void renderAll() {
        Packet<?>[] packets = getRenderPackets();
        Bukkit.getOnlinePlayers().forEach(p -> render(((CraftPlayer) p).getHandle().connection, packets));
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

    public void addVelocity(Vector vector) {
        if (NumberConversions.isFinite(vector.getX()) && NumberConversions.isFinite(vector.getY()) && NumberConversions.isFinite(vector.getZ())) {
            velocity.add(vector);
        } else {
            velocity = vector;
        }
    }

    public Vector getVelocity() {
        return velocity.clone();
    }
    
    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public BoundingBox getBotBoundingBox() {
        return getBukkitEntity().getBoundingBox();
    }
    
    public Location getLocation() {
        return getBukkitEntity().getLocation();
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

}
