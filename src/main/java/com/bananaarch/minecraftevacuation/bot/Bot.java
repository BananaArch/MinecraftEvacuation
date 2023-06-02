package com.bananaarch.minecraftevacuation.bot;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Bot extends ServerPlayer {

    private Vector velocity;
    private Location initialLocation;
    private Location targetLocation;
    private Set<Location> pastLocations;
//    private Environment mdp;
    private static final int OBSERVATION_LENGTH = 15;
    private static final int OBSERVATION_WIDTH = 15;
    private static final int OBSERVATION_HEIGHT = 4;


    protected Bot(MinecraftServer minecraftserver, ServerLevel worldserver, GameProfile gameprofile, Location initialLocation) {
        super(minecraftserver, worldserver, gameprofile);

        this.initialLocation = initialLocation;
        this.targetLocation = null;
        this.velocity = new Vector(0, 0, 0);
        this.pastLocations = new HashSet<>();
//        this.mdp = new Environment(this);
    }

    public void renderAll() {
        Packet<?> addPlayerPacket = new ClientboundAddPlayerPacket(this);
        Packet<?> setEntityDataPacket = new ClientboundSetEntityDataPacket(this.getId(), this.entityData.packDirty());
        Packet<?> rotateHeadPacket = new ClientboundRotateHeadPacket(this, (byte) ((this.yHeadRot * 256f) / 360f));

        sendPacket(addPlayerPacket);
        sendPacket(setEntityDataPacket);
        sendPacket(rotateHeadPacket);
    }

    public void show() {
        Packet<?> addPlayerPacket = new ClientboundAddPlayerPacket(this);
        sendPacket(addPlayerPacket);
    }

    public void hide() {
        Packet<?> destroyEntityPacket = new ClientboundRemoveEntitiesPacket(this.getId());
        sendPacket(destroyEntityPacket);
    }

    public void destroy() {

        this.remove(RemovalReason.KILLED);
        this.hide();
        this.velocity = null;
        this.initialLocation = null;

    }

    public void sendPacket(Packet<?> packet) {
        Bukkit.getOnlinePlayers().forEach(p -> ((CraftPlayer) p).getHandle().connection.send(packet));
    }
    
    public void tick() {
        loadChunks();
        super.tick();
        this.move(MoverType.SELF, new Vec3(velocity.getX(), velocity.getY(), velocity.getZ()));
        baseTick();
        pastLocations.add(getFlooredLocation());
    }

    public void loadChunks() {
        Level world = getLevel();

        for (int i = chunkPosition().x - 1; i <= chunkPosition().x + 1; i++) {
            for (int j = chunkPosition().z - 1; j <= chunkPosition().z + 1; j++) {
                LevelChunk chunk = world.getChunk(i, j);

                if (!chunk.loaded)
                    chunk.loaded = true;
            }
        }
    }

    public void walk(Vector velocity) {
        double max = 0.4;
        addVelocity(velocity);
        if (velocity.length() > max)
            velocity.normalize().multiply(max);
    }

    public void resetLocation() {

        ServerLevel level = (ServerLevel) initialLocation.getWorld();
        this.teleportTo(initialLocation.getX(), initialLocation.getY(), initialLocation.getZ());

        pastLocations.clear();

        this.setVelocity(new Vector(0, 0, 0));

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

        // TODO: update Skin w/ packets

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

    public boolean containsInPastLocations(Location location) {
        return pastLocations.contains(location);
    }

    public BoundingBox getBotBoundingBox() {
        return getBukkitEntity().getBoundingBox();
    }

    public void setTargetLocation(Location targetLocation) {
        this.targetLocation = targetLocation;
    }

    public Location getLocation() {
        return getBukkitEntity().getLocation();
    }

    public Location getTargetLocation() {
        return targetLocation;
    }

    public Location getInitialLocation() {
        return initialLocation;
    }

    public Location getFlooredLocation() {

        Location location = getBukkitEntity().getLocation();
        location.setX(Math.floor(location.getX()));
        location.setY(Math.floor(location.getY()));
        location.setZ(Math.floor(location.getZ()));
        return location;

    }

    public Location getFlooredTargetLocation() {

        Location location = targetLocation.clone();
        location.setX(Math.floor(location.getX()));
        location.setY(Math.floor(location.getY()));
        location.setZ(Math.floor(location.getZ()));
        return location;

    }

    public static int getObservationSize() {
        return OBSERVATION_LENGTH * OBSERVATION_HEIGHT * OBSERVATION_WIDTH + 6;
//        6 comes from targetLocation x, y, z in world and playerLocation x, y, z in world
    }

    public INDArray getState() {



        Location location = getLocation();

        int[] shape = new int[]{OBSERVATION_LENGTH, OBSERVATION_WIDTH, OBSERVATION_HEIGHT};

        INDArray gameState = Nd4j.create(shape);

        int startingLengthIndex = OBSERVATION_LENGTH % 2 != 0 ? Math.floorDiv(OBSERVATION_LENGTH, 2) : OBSERVATION_LENGTH / 2 - 1;
        int startingWidthIndex = OBSERVATION_WIDTH % 2 != 0 ? Math.floorDiv(OBSERVATION_WIDTH, 2) : OBSERVATION_WIDTH / 2 - 1;
        int startingHeightIndex = OBSERVATION_HEIGHT % 2 != 0 ? Math.floorDiv(OBSERVATION_HEIGHT, 2) : OBSERVATION_HEIGHT / 2 - 1;

        for (
                int i = -startingLengthIndex;
                i <= Math.floorDiv(OBSERVATION_LENGTH, 2);
                i++
        ) {

            for (
                    int j = -startingWidthIndex;
                    j <= Math.floorDiv(OBSERVATION_WIDTH, 2);
                    j++
            ) {

                for (
                        int k = -startingHeightIndex;
                        k <= Math.floorDiv(OBSERVATION_HEIGHT, 2);
                        k++
                ) {

//                    System.out.print("(" + i + ", " + j + ", " + k + ")\t");

                    Block scannedBlock = getBlockAtLocation(location, i, j, k);

                    gameState.putScalar(
                            i + startingLengthIndex,
                            j + startingWidthIndex,
                            k + startingHeightIndex,
                            scannedBlock.isEmpty() || !scannedBlock.getType().isSolid() ? 1 : 0
                    );


                }

            }

        }

//        position of player
        gameState.putScalar(
                startingLengthIndex,
                startingWidthIndex,
                startingHeightIndex,
                -1
        );

        INDArray arrayState = Nd4j.toFlattened(gameState);

//        position of targetLocation in world
        arrayState.add(targetLocation.getX());
        arrayState.add(targetLocation.getY());
        arrayState.add(targetLocation.getZ());

//        position of player in world
        arrayState.add(location.getX());
        arrayState.add(location.getY());
        arrayState.add(location.getZ());

        return arrayState;

    }

//    public Environment getMdp() {
//        return mdp;
//    }
//
//    public void closeEnvironment() {
//        this.mdp.close();
//    }

    private Block getBlockAtLocation(Location location, int deltaX, int deltaY, int deltaZ) {

        Location blockLocation = new Location(
                location.getWorld(),
                location.getX() + deltaX,
                location.getY() + deltaY,
                location.getZ() + deltaZ);
        return blockLocation.getBlock();

    }

    public boolean standingOnTargetBlock() {
        return getFlooredLocation().add(new Vector(0, -1, 0)).equals(getFlooredTargetLocation()) || getFlooredLocation().equals(getFlooredLocation());
    }

    public abstract List<String> getInfo();

}
