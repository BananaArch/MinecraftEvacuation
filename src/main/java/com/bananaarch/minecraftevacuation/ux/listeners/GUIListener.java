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
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;


public class GUIListener implements Listener {

    private final BotManager botManager = MinecraftEvacuation.getInstance().getBotManager();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();

        CustomGUI customGUI = Arrays.stream(CustomGUI.values())
                .filter(gui -> e.getInventory().equals(gui.getInventory()))
                .findFirst()
                .orElse(null);

        // check if GUI is custom
        if (customGUI == null) return;
        // makes sure item is in GUI (not inventory)
        if (e.getClickedInventory().equals(e.getWhoClicked().getInventory())) return;

        ItemStack currentItem = e.getCurrentItem();

        CustomItems customItem = Arrays.stream(CustomItems.values())
                        .filter(customItems -> customItems.getItemStack().isSimilar(currentItem))
                        .findFirst()
                        .orElse(null);

        e.setCancelled(true);
        if (customGUI.equals(CustomGUI.MENU_GUI))
            switch (customItem) {

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
                case STUDENTS_SHOWN:
                    botManager.hideAll();
                    CustomGUI.MENU_GUI.setItem(5, CustomItems.STUDENTS_HIDDEN.getItemStack());
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 100);
                    break;
                case STUDENTS_HIDDEN:
                    botManager.showAll();
                    CustomGUI.MENU_GUI.setItem(5, CustomItems.STUDENTS_SHOWN.getItemStack());
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
                case EXIT_GUI:
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
                case TARGET_BLOCK:
                    player.getInventory().addItem(CustomItems.TARGET_BLOCK.getItemStack());
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 0);
                default: break;
            }


        /*
            If GUI is a BOT GUI
         */
        if (customGUI.equals(CustomGUI.BOT_GUI))
            switch(customItem) {

                case EXIT_GUI:
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
                    break;
                default: break;
            }

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().equals(CustomGUI.MENU_GUI.getInventory())) {

            CustomGUI.MENU_GUI.setItem(6, CustomItems.DESTROY_ALL_BOTS.getItemStack());

        }
    }


}