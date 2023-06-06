package com.bananaarch.minecraftevacuation.bot.AI.pathfinding;

import com.bananaarch.minecraftevacuation.bot.utils.ChatUtil;
import com.bananaarch.minecraftevacuation.bot.utils.PathfindingUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.*;

public class AStar {

    private Location startLocation;
    private Location endLocation;

    private Node startNode;
    private Node endNode;

    private boolean pathFound = false;
    private Set<Node> checkedNodes = new HashSet<>();
    private PriorityQueue<Node> uncheckedNodes = new PriorityQueue<>(Comparator.comparing(Node::getEstimatedFinalWeight));

    private Map<Location, Node> nodeMap = new HashMap<>();
    private Map<Location, Double> locationDistanceCache = new HashMap<>();

    /*
    Maybe make it static and make it thread-safe so info would be shared across different instances
     */

    private final int maxNodeTests = 15000;
    private final int maxFallDistance = 2;

    public AStar(Location startLocation, Location endLocation) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;

        this.startNode = new Node(startLocation, null, 0);
        this.endNode = new Node(endLocation, null, 0);

        this.nodeMap.put(startLocation, this.startNode);
        this.nodeMap.put(endLocation, this.endNode);
    }

    public LinkedList<Location> getPath() {
        if (endLocation == null) {
            ChatUtil.broadcastError("Target location does not exist");
            return null;
        }
        if (!PathfindingUtil.canStandAt(startLocation) || !PathfindingUtil.canStandAt(endLocation)) {
            ChatUtil.broadcastError("Start or target location cannot be stood on");
            return null;
        }

        long timeStart = System.nanoTime();

        uncheckedNodes.add(startNode);

        while (checkedNodes.size() <= maxNodeTests && pathFound == false && uncheckedNodes.size() > 0) {
            Node node = uncheckedNodes.poll();

            if (node.getEstimatedWeightLeft() != null && node.getEstimatedWeightLeft() < 1) {
                pathFound = true;
                endNode = node;
                break;
            }

            node.getReachableLocations();
            uncheckedNodes.remove(node);
            checkedNodes.add(node);

        }

        if (!pathFound) {
            ChatUtil.broadcastError("[" + Math.floorDiv((int) (System.nanoTime() - timeStart), (int) Math.pow(10, 6)) + "ms] A* did not find a path");
            return null;
        }


        LinkedList<Location> locationsList = new LinkedList<>();

        Node end = endNode;
        while(end != null) {
            locationsList.addFirst(end.getLocation());
            end = end.getOrigin();
        }

        ChatUtil.broadcastMessage(ChatColor.YELLOW + "[" + Math.floorDiv((int) (System.nanoTime() - timeStart), (int) Math.pow(10, 6)) + "ms] A* found the path");

        return locationsList;
    }

    private Node getNode(Location location) {
        Node node = this.nodeMap.get(location);
        if (node == null) {
            node = new Node(location, null, 0);
            this.nodeMap.put(location, node);
        }
        return node;
    }

    public class Node {

        private Location location;

        private Node origin;

        private double weight;
        private Double estimatedWeightLeft;

        public Node(Location location, Node origin, double weight) {
            this.location = location;
            this.origin = origin;
            this.weight = weight;
            this.estimatedWeightLeft = null;
        }

        public double getEstimatedFinalWeight() {
            if (estimatedWeightLeft == null)
                this.estimatedWeightLeft = getCachedDistance(location, endLocation);

            return weight + 1.5 * estimatedWeightLeft;
        }

        private double getCachedDistance(Location location1, Location location2) {
            if (locationDistanceCache.containsKey(location1)) {
                return locationDistanceCache.get(location1);
            } else {
                double distance = location1.distanceSquared(location2);
                locationDistanceCache.put(location1, distance);
                return distance;
            }
        }

        public void getReachableLocations() {

//        3x3 around centered location

            for (int deltaX = -1; deltaX <= 1; deltaX++) {
                for (int deltaZ = -1; deltaZ <= 1; deltaZ++) {

                    if (deltaX == 0 && deltaZ == 0) continue;

                    Location checkedLocation = location.clone().add(deltaX, 0, deltaZ);
                    Location aboveCheckedLocation = checkedLocation.clone().add(0, 1, 0);
                    Location belowCheckedLocation = checkedLocation.clone().add(0, -1, 0);

                    if (PathfindingUtil.canStandAt(checkedLocation))
                        reachNode(checkedLocation, weight + 1);

                    if (!PathfindingUtil.isObstructed(location.clone().add(0, 2, 0))) {
                        if (PathfindingUtil.canStandAt(aboveCheckedLocation))
                            reachNode(aboveCheckedLocation, weight + 1.414);
                    }

                    if (!PathfindingUtil.isObstructed(aboveCheckedLocation)) {
                        if (PathfindingUtil.canStandAt(belowCheckedLocation)) {
                            reachNode(belowCheckedLocation, weight + 1.414);
                        }
                        if (!PathfindingUtil.isObstructed(belowCheckedLocation) && !PathfindingUtil.isObstructed(checkedLocation)) {


                            for (int drop = 1; drop <= maxFallDistance && !PathfindingUtil.isObstructed(checkedLocation.clone().add(0, -drop, 0)); drop++) {
                                Location dropCheckLocation = checkedLocation.clone().add(0, -drop, 0);

                                if (PathfindingUtil.canStandAt(dropCheckLocation)) {
                                    Node fallNode = addFallNode(checkedLocation, weight + 1);
                                    fallNode.reachNode(dropCheckLocation, weight + drop * 2);
                                }
                            }
                        }
                    }

                }
            }
        }

        public void reachNode(Location destLocation, double destWeight) {
            Node targetNode = getNode(destLocation);

            if (targetNode.getOrigin() == null && targetNode != startNode) {
                targetNode.setWeight(destWeight);
                targetNode.setOrigin(this);

                uncheckedNodes.add(targetNode);
                nodeMap.put(destLocation, targetNode);

                return;
            }

            if (targetNode.getWeight() > destWeight) {
                targetNode.setWeight(destWeight);
                targetNode.setOrigin(this);
            }

        }

        public Node addFallNode(Location location, double weight) {
            Node node = new Node(location, this, weight);

            return node;
        }

        public Location getLocation() {
            return location;
        }

        public Node getOrigin() {
            return origin;
        }
        public double getWeight() {
            return weight;
        }

        public Double getEstimatedWeightLeft() {
            return estimatedWeightLeft;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public void setOrigin(Node node) {
            this.origin = node;
        }

    }

}


