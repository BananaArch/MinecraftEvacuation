package com.bananaarch.minecraftevacuation.settings.command;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.BotManager;
import com.bananaarch.minecraftevacuation.utils.CustomItems;
import com.bananaarch.minecraftevacuation.utils.CustomGUI;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;


public class GUIListener implements Listener {

    private final BotManager botManager = MinecraftEvacuation.getInstance().getManager();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        if (e.getCurrentItem() == null)
            return;

//        SETTINGS GUI

        if (e.getInventory().equals(CustomGUI.SETTINGS_GUI.getInventory())) {

            e.setCancelled(true);

            if (e.getClickedInventory().equals(e.getWhoClicked().getInventory()))
                return;

            Player player = (Player) e.getWhoClicked();



            if (e.getCurrentItem().isSimilar(CustomItems.START_REPLAY.getItemStack())) {
                CustomGUI.SETTINGS_GUI.setItem(3, CustomItems.STOP_REPLAY.getItemStack());
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                return;
            }
            if (e.getCurrentItem().isSimilar(CustomItems.STOP_REPLAY.getItemStack())) {
                CustomGUI.SETTINGS_GUI.setItem(3, CustomItems.START_REPLAY.getItemStack());
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                return;
            }
            if (e.getCurrentItem().isSimilar(CustomItems.START_TRAINING.getItemStack())) {
                CustomGUI.SETTINGS_GUI.setItem(4, CustomItems.STOP_TRAINING.getItemStack());
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                return;
            }
            if (e.getCurrentItem().isSimilar(CustomItems.STOP_TRAINING.getItemStack())) {
                botManager.showAll();
                CustomGUI.SETTINGS_GUI.setItem(4, CustomItems.START_TRAINING.getItemStack());
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                return;
            }
            if (e.getCurrentItem().isSimilar(CustomItems.SHOW_STUDENTS.getItemStack())) {
                botManager.hideAll();
                CustomGUI.SETTINGS_GUI.setItem(5, CustomItems.HIDE_STUDENTS.getItemStack());
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                return;
            }
            if (e.getCurrentItem().isSimilar(CustomItems.HIDE_STUDENTS.getItemStack())) {

                CustomGUI.SETTINGS_GUI.setItem(5, CustomItems.SHOW_STUDENTS.getItemStack());
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                return;
            }
            if (e.getCurrentItem().isSimilar(CustomItems.EXIT_ITEM.getItemStack())) {
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0);
                return;
            }
            if (e.getCurrentItem().isSimilar(CustomItems.FRESHMAN_ITEM.getItemStack())) {
                player.getInventory().addItem(CustomItems.FRESHMAN_ITEM.getItemStack());
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
                return;
            }
            if (e.getCurrentItem().isSimilar(CustomItems.SOPHOMORE_ITEM.getItemStack())) {
                player.getInventory().addItem(CustomItems.SOPHOMORE_ITEM.getItemStack());
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
                return;
            }
            if (e.getCurrentItem().isSimilar(CustomItems.JUNIOR_ITEM.getItemStack())) {
                player.getInventory().addItem(CustomItems.JUNIOR_ITEM.getItemStack());
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
                return;
            }
            if (e.getCurrentItem().isSimilar(CustomItems.SENIOR_ITEM.getItemStack())) {
                player.getInventory().addItem(CustomItems.SENIOR_ITEM.getItemStack());
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
                return;
            }
            if (e.getCurrentItem().isSimilar(CustomItems.TEACHER_ITEM.getItemStack())) {
                player.getInventory().addItem(CustomItems.TEACHER_ITEM.getItemStack());
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
                return;
            }

        }

    }
}