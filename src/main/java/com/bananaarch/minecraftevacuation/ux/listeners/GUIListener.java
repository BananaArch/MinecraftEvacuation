package com.bananaarch.minecraftevacuation.ux.listeners;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.BotManager;
import com.bananaarch.minecraftevacuation.utils.CustomItems;
import com.bananaarch.minecraftevacuation.utils.CustomGUI;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;


public class GUIListener implements Listener {

    private final BotManager botManager = MinecraftEvacuation.getInstance().getManager();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        if (e.getCurrentItem() == null)
            return;

//        SETTINGS GUI

        if (e.getInventory().equals(CustomGUI.MENU_GUI.getInventory())) {

            e.setCancelled(true);

//            if clicked in player's own inventory
            if (e.getClickedInventory().equals(e.getWhoClicked().getInventory()))
                return;

            Player player = (Player) e.getWhoClicked();



            if (e.getCurrentItem().isSimilar(CustomItems.START_REPLAY.getItemStack())) {
                CustomGUI.MENU_GUI.setItem(3, CustomItems.STOP_REPLAY.getItemStack());
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                return;
            }
            if (e.getCurrentItem().isSimilar(CustomItems.STOP_REPLAY.getItemStack())) {
                CustomGUI.MENU_GUI.setItem(3, CustomItems.START_REPLAY.getItemStack());
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                return;
            }
            if (e.getCurrentItem().isSimilar(CustomItems.START_TRAINING.getItemStack())) {
                CustomGUI.MENU_GUI.setItem(4, CustomItems.STOP_TRAINING.getItemStack());
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                return;
            }
            if (e.getCurrentItem().isSimilar(CustomItems.STOP_TRAINING.getItemStack())) {
                CustomGUI.MENU_GUI.setItem(4, CustomItems.START_TRAINING.getItemStack());
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                return;
            }
            if (e.getCurrentItem().isSimilar(CustomItems.SHOW_STUDENTS.getItemStack())) {
                botManager.hideAll();
                CustomGUI.MENU_GUI.setItem(5, CustomItems.HIDE_STUDENTS.getItemStack());
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                return;
            }
            if (e.getCurrentItem().isSimilar(CustomItems.HIDE_STUDENTS.getItemStack())) {
                botManager.showAll();
                CustomGUI.MENU_GUI.setItem(5, CustomItems.SHOW_STUDENTS.getItemStack());
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                return;
            }
            if (e.getCurrentItem().isSimilar(CustomItems.DESTROY_ALL_BOTS.getItemStack())) {
                CustomGUI.MENU_GUI.setItem(6, CustomItems.CONFIRM_DESTROY_ALL_BOTS.getItemStack());
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                return;
            }
            if (e.getCurrentItem().isSimilar(CustomItems.CONFIRM_DESTROY_ALL_BOTS.getItemStack())) {
                int amountDestroyed = botManager.destroyAll();
                player.sendMessage(ChatColor.GRAY + "Successfully destroyed " + ChatColor.DARK_RED + amountDestroyed + " bots");
                CustomGUI.MENU_GUI.setItem(6, CustomItems.DESTROY_ALL_BOTS.getItemStack());
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

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().equals(CustomGUI.MENU_GUI.getInventory())) {

            CustomGUI.MENU_GUI.setItem(6, CustomItems.DESTROY_ALL_BOTS.getItemStack());

        }
    }
}