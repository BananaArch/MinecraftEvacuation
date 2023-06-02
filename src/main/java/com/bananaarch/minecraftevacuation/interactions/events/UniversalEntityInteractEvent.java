package com.bananaarch.minecraftevacuation.interactions.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UniversalEntityInteractEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;
    private final Player player;
    private final int entityId;

    public UniversalEntityInteractEvent(Player player, int entityId) {
        this.player = player;
        this.entityId = entityId;
        this.cancelled = false;
    }

    public Player getPlayer() {
        return player;
    }

    public int getEntityId() {
        return entityId;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    // DO NOT DELETE!!!
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
