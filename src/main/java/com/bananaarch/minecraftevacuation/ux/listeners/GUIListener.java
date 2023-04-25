package com.bananaarch.minecraftevacuation.ux.listeners;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.BotManager;
import com.bananaarch.minecraftevacuation.utils.BotType;
import com.bananaarch.minecraftevacuation.utils.CustomItems;
import com.bananaarch.minecraftevacuation.utils.CustomGUI;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;


public class GUIListener implements Listener {

    private final BotManager botManager = MinecraftEvacuation.getInstance().getBotManager();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {


        if (e.getCurrentItem() == null)
            return;

//        SETTINGS GUI



        if (CustomGUI.values().st) {

            e.setCancelled(true);

//            if clicked in player's own inventory
            if (e.getClickedInventory().equals(e.getWhoClicked().getInventory()))
                return;

            Player player = (Player) e.getWhoClicked();

        }

    }

    private void handleAction(Player player, CustomItems clickedItem, CustomGUI gui) {

        /*
            If GUI is MENU GUI
         */

        if (gui.equals(CustomGUI.MENU_GUI))
            switch (clickedItem) {

                case START_REPLAY:
                    CustomGUI.MENU_GUI.setItem(3, CustomItems.STOP_REPLAY.getItemStack());
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                    break;
                case STOP_REPLAY:
                    CustomGUI.MENU_GUI.setItem(3, CustomItems.START_REPLAY.getItemStack());
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                    break;
                case START_TRAINING:
                    CustomGUI.MENU_GUI.setItem(4, CustomItems.STOP_TRAINING.getItemStack());
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                    break;
                case STOP_TRAINING:
                    CustomGUI.MENU_GUI.setItem(4, CustomItems.START_TRAINING.getItemStack());
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                    break;
                case SHOW_STUDENTS:
                    botManager.hideAll();
                    CustomGUI.MENU_GUI.setItem(5, CustomItems.HIDE_STUDENTS.getItemStack());
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                    break;
                case HIDE_STUDENTS:
                    botManager.showAll();
                    CustomGUI.MENU_GUI.setItem(5, CustomItems.SHOW_STUDENTS.getItemStack());
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                    break;
                case DESTROY_ALL_BOTS:
                    CustomGUI.MENU_GUI.setItem(6, CustomItems.CONFIRM_DESTROY_ALL_BOTS.getItemStack());
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                    break;
                case CONFIRM_DESTROY_ALL_BOTS:
                    int amountDestroyed = botManager.destroyAll();
                    player.sendMessage(ChatColor.GRAY + "Successfully destroyed " + ChatColor.DARK_RED + amountDestroyed + " bots");
                    CustomGUI.MENU_GUI.setItem(6, CustomItems.DESTROY_ALL_BOTS.getItemStack());
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                    break;
                case EXIT_ITEM:
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0);
                    break;
                case FRESHMAN_ITEM:
                    player.getInventory().addItem(CustomItems.FRESHMAN_ITEM.getItemStack());
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
                    break;
                case SOPHOMORE_ITEM:
                    player.getInventory().addItem(CustomItems.SOPHOMORE_ITEM.getItemStack());
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
                    break;
                case JUNIOR_ITEM:
                    player.getInventory().addItem(CustomItems.JUNIOR_ITEM.getItemStack());
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
                    break;
                case SENIOR_ITEM:
                    player.getInventory().addItem(CustomItems.SENIOR_ITEM.getItemStack());
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
                    break;
                case TEACHER_ITEM:
                    player.getInventory().addItem(CustomItems.TEACHER_ITEM.getItemStack());
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
                    break;
            }


        /*
            If GUI is a BOT GUI
         */
        if (gui.equals(CustomGUI.BOT_GUI))
            switch(clickedItem) {

                case EXIT_ITEM:
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
                    break;
            }

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().equals(CustomGUI.MENU_GUI.getInventory())) {

            CustomGUI.MENU_GUI.setItem(6, CustomItems.DESTROY_ALL_BOTS.getItemStack());

        }
    }


}