package com.bananaarch.minecraftevacuation.bot;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.AI.NN.Action;
import com.bananaarch.minecraftevacuation.bot.AI.pathfinding.AStar;
import com.bananaarch.minecraftevacuation.bot.utils.BotUtil;
import com.bananaarch.minecraftevacuation.bot.utils.ChatUtil;
import com.bananaarch.minecraftevacuation.bot.utils.MaterialUtil;
import com.bananaarch.minecraftevacuation.tasks.TaskManager;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Bot extends ServerPlayer {

    private Vector velocity;
    private Location initialLocation;
    private Location targetLocation;
    private Set<Location> pastLocations;
    private byte groundTicks;
    private byte jumpTicks;
    private Queue<Location> path;
    private final TaskManager taskManager = MinecraftEvacuation.getInstance().getTaskManager();
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
        this.path = null;
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

        if (jumpTicks > 0) --jumpTicks;
        if (checkGround()) {
            if (groundTicks < 5) groundTicks ++;
        } else {
            groundTicks = 0;
        }

        updateLocation();

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

    public void teleportByAction(Action action) {
        Location newLocation = getLocation().add(action.getVector());
        teleportTo(newLocation.getX(), newLocation.getY(), newLocation.getZ());
    }

    public void resetLocation() {

        this.teleportTo(initialLocation.getX(), initialLocation.getY(), initialLocation.getZ());

        pastLocations.clear();

        this.setVelocity(new Vector(0, 0, 0));

    }

    public void generatePath() {
        AStar aStar = new AStar(initialLocation, targetLocation);
        this.path = aStar.getPath();
    }

    /*
    TODO: make following algo better
     */
    public void followPath() {

        ChatUtil.broadcastMessage("Following path");

        if (path == null || path.isEmpty()) {
            return;
        }

        Queue<Location> checkpoints = BotUtil.cloneLinkedList(path);
        final double TOLERANCE = 1;

        taskManager.scheduleRepeatingTask(new BukkitRunnable() {

            @Override
            public void run() {

                System.out.println(checkpoints.peek());

                if (!checkpoints.isEmpty()) {
                    Location nextLocation = checkpoints.peek();
                    Location currentLocation = getLocation();

                    double distanceSquared = currentLocation.distanceSquared(nextLocation);

                    System.out.println(distanceSquared);

                    if (distanceSquared <= TOLERANCE) {
                        checkpoints.poll();
                        if (!checkpoints.isEmpty()) {
                            nextLocation = checkpoints.peek();
//                            generatePath();
                        } else {
                            this.cancel();
                            return;
                        }
                    }

                    System.out.println("Next Location: " + nextLocation);
                    System.out.println("Current Location: " + currentLocation);

                    Vector direction = new Vector(
                            nextLocation.getX() - currentLocation.getX(),
                            nextLocation.getY() - currentLocation.getY(),
                            nextLocation.getZ() - currentLocation.getZ()
                    ).normalize();

                    System.out.println(direction);

                    look(direction);
                    moveBot(direction.getX(), 1.5 * direction.getY(), direction.getZ());

                } else {
                    this.cancel();
                }

            }
        }, 0, 1);
    }

    public void moveBot(double x, double y, double z) {

        this.move(MoverType.SELF, new Vec3(x, y, z));
    }

    public void showPath() {
        AStar aStar = new AStar(initialLocation, targetLocation);
        LinkedList<Location> pathList = aStar.getPath();
        if (pathList == null) return;

        Map<Location, BlockData> blockDataMap = new HashMap<>();

        for (Location loc : pathList) {
            Block block = loc.getBlock();
            blockDataMap.put(loc, block.getBlockData());
            block.setType(Material.GLASS);
        }

        taskManager.scheduleTask(() -> {
            for (Location loc : pathList) {
                loc.getBlock().setBlockData(blockDataMap.get(loc));
            }
        }, 20*15);
    }

    public boolean checkGround() {
        double vy = velocity.getY();

        if (vy > 0) {
            return false;
        }

        return isStandingOnBlock();
    }

    public boolean isStandingOnBlock() {
        World world = getBukkitEntity().getWorld();
        AABB box = getBoundingBox();

        double[] xVals = new double[]{
                box.minX,
                box.maxX
        };

        double[] zVals = new double[]{
                box.minZ,
                box.maxZ
        };

        BoundingBox playerBox = new BoundingBox(box.minX, position().y - 0.01, box.minZ,
                box.maxX, position().y + getBbHeight(), box.maxZ);
        List<Block> standingOn = new ArrayList<>();
        List<Location> locations = new ArrayList<>();

        for (double x : xVals) {
            for (double z : zVals) {
                Location loc = new Location(world, x, position().y - 0.01, z);
                Block block = world.getBlockAt(loc);

                if ((block.getType().isSolid() || MaterialUtil.canStandOn(block.getType())) && playerBox.overlaps(block.getBoundingBox())) {
                    if (!locations.contains(block.getLocation())) {
                        standingOn.add(block);
                        locations.add(block.getLocation());
                    }
                }
            }
        }

        //Fence/wall check
        for (double x : xVals) {
            for (double z : zVals) {
                Location loc = new Location(world, x, position().y - 0.51, z);
                Block block = world.getBlockAt(loc);
                BoundingBox blockBox = loc.getBlock().getBoundingBox();
                BoundingBox modifiedBox = new BoundingBox(blockBox.getMinX(), blockBox.getMinY(), blockBox.getMinZ(), blockBox.getMaxX(),
                        blockBox.getMinY() + 1.5, blockBox.getMaxZ());

                if ((MaterialUtil.FENCE.contains(block.getType()) || MaterialUtil.GATES.contains(block.getType()))
                        && block.getType().isSolid() && playerBox.overlaps(modifiedBox)) {
                    if (!locations.contains(block.getLocation())) {
                        standingOn.add(block);
                        locations.add(block.getLocation());
                    }
                }
            }
        }

        Collections.sort(standingOn, (a, b) ->
                Double.compare(a.getLocation().distanceSquared(getLocation()), b.getLocation().distanceSquared(getLocation()))
        );

        return !standingOn.isEmpty();

    }

    private void updateLocation() {
        double y;

        if (isBotSwimming()) {
            y = Math.min(velocity.getY() + 0.005, 0.1);
            addFriction(0.8);
            velocity.setY(y);
        } else {
            if (groundTicks != 0) {
                velocity.setY(0);
                addFriction(0.5);
                y = 0;
            } else {
                y = velocity.getY();
                velocity.setY(Math.max(y - 0.0175, -3.5));
            }
        }

        this.move(MoverType.SELF, new Vec3(velocity.getX(), y, velocity.getZ()));
    }

    public void addFriction(double factor) {
        double frictionMin = 0.01;

        double x = velocity.getX();
        double z = velocity.getZ();

        velocity.setX(Math.abs(x) < frictionMin ? 0 : x * factor);
        velocity.setZ(Math.abs(z) < frictionMin ? 0 : z * factor);
    }

    public void walkByAction(Action action) {
        action.walk(this);
    }

    public void walk(Vector velocity) {
        double max = 0.4;
        if (velocity.length() > max)
            velocity.normalize().multiply(max);
    }
    public void jump(Vector vel) {
        if (jumpTicks == 0 && groundTicks > 1) {
            jumpTicks = 4;
            addVelocity(vel);
        }
    }

    public void jump() {
        jump(new Vector(0, 0.05, 0));
    }


    public boolean isBotSwimming() {
        Location loc = getLocation();

        for (int i = 0; i <= 2; i++) {
            Material type = loc.getBlock().getType();
            if (type == Material.WATER || type == Material.LAVA) {
                return true;
            }
            loc.add(0, 0.9, 0);
        }

        return false;
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
            this.velocity = vector;
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

    public Location floorLocation(Location location) {
        location.setX(Math.floor(location.getX()));
        location.setY(Math.floor(location.getY()));
        location.setZ(Math.floor(location.getZ()));
        return location;
    }

    /*
    * Calculates the size of observation based on OBSERVATION_WIDTH, OBSERVATION_LENGTH, OBSERVATION_HEIGHT
    *
    * @return Size of observation.
    */
    public static int getObservationSize() {
        return OBSERVATION_LENGTH * OBSERVATION_HEIGHT * OBSERVATION_WIDTH + 6;
//        6 comes from targetLocation x, y, z in world and playerLocation x, y, z in world
    }

    /*
     * Calculates an array of the State of the bot. This is a OBSERVATION_WIDTH x OBSERVATION_LENGTH x OBSERVATION_HEIGHT size rectangle that the bot can "see."
     *
     * @return Array of observation.
     */
    public double[] getState() {

        Location location = getLocation();

        double[] data = new double[getObservationSize()];

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

                    Block scannedBlock = getBlockAtLocation(location, i, j, k);

                    data[i + startingLengthIndex + j + startingWidthIndex + k + startingHeightIndex] = scannedBlock.isEmpty() || !scannedBlock.getType().isSolid() ? 1 : 0;

                }

            }

        }

        data[startingLengthIndex + startingWidthIndex + startingHeightIndex] = -1;
        data[getObservationSize() - 6] = targetLocation.getX();
        data[getObservationSize() - 5] = targetLocation.getY();
        data[getObservationSize() - 4] = targetLocation.getZ();
        data[getObservationSize() - 3] = location.getX();
        data[getObservationSize() - 2] = location.getY();
        data[getObservationSize() - 1] = location.getZ();

//        System.out.println(Arrays.toString(data));

        return data;

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
        return getFlooredLocation().add(new Vector(0, -1, 0)).equals(getFlooredTargetLocation()) || getFlooredLocation().equals(getFlooredTargetLocation());
    }

    public abstract List<String> getInfo();

}
