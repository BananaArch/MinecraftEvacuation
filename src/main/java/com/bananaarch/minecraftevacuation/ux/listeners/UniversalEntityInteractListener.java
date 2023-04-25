package com.bananaarch.minecraftevacuation.ux.listeners;

import com.bananaarch.minecraftevacuation.ux.events.UniversalEntityInteractEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class UniversalEntityInteractListener implements Listener {

    @EventHandler
    public void onUniversalEntityInteractEvent(UniversalEntityInteractEvent e) {

        System.out.println("Clicked entity");

        e.getPlayer().sendMessage("The entity you clicked is: " + e.getEntityId());

    }
}
