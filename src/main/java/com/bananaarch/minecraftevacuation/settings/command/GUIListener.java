package com.bananaarch.minecraftevacuation.settings.command;

import com.bananaarch.minecraftevacuation.settings.utils.CustomItems;
import com.bananaarch.minecraftevacuation.settings.utils.CustomGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;



public class GUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        if (e.getCurrentItem() == null)
            return;

//        SETTINGS GUI

        if (e.getInventory().equals(CustomGUI.SETTINGS_GUI.getInventory())) {

            e.setCancelled(true);

            // put it inside so that you can't take from double clicking
            if (e.getClickedInventory().equals(e.getWhoClicked().getInventory()))
                return;

            Player player = (Player) e.getWhoClicked();
            if (e.getCurrentItem().isSimilar(CustomItems.EXIT_ITEM.getItemStack()))
                player.closeInventory();
            if (e.getCurrentItem().isSimilar(CustomItems.FRESHMAN_ITEM.getItemStack()))
                player.getInventory().addItem(CustomItems.FRESHMAN_ITEM.getItemStack());
            if (e.getCurrentItem().isSimilar(CustomItems.SOPHOMORE_ITEM.getItemStack()))
                player.getInventory().addItem(CustomItems.SOPHOMORE_ITEM.getItemStack());
            if (e.getCurrentItem().isSimilar(CustomItems.JUNIOR_ITEM.getItemStack()))
                player.getInventory().addItem(CustomItems.JUNIOR_ITEM.getItemStack());
            if (e.getCurrentItem().isSimilar(CustomItems.SENIOR_ITEM.getItemStack()))
                player.getInventory().addItem(CustomItems.SENIOR_ITEM.getItemStack());
            if (e.getCurrentItem().isSimilar(CustomItems.TEACHER_ITEM.getItemStack()))
                player.getInventory().addItem(CustomItems.TEACHER_ITEM.getItemStack());

        }

    }
}
