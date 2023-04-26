package com.bananaarch.minecraftevacuation.ux.listeners;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.Bot;
import com.bananaarch.minecraftevacuation.bot.BotManager;
import com.bananaarch.minecraftevacuation.bot.Genderable;
import com.bananaarch.minecraftevacuation.utils.CustomItems;
import com.bananaarch.minecraftevacuation.utils.CustomGUI;
import com.bananaarch.minecraftevacuation.utils.ItemStackUtil;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class GUIListener implements Listener {

    private final MinecraftEvacuation plugin = MinecraftEvacuation.getInstance();
    private final BotManager botManager = MinecraftEvacuation.getInstance().getBotManager();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();

        CustomGUI customGUI = Arrays.stream(CustomGUI.values())
                .filter(gui -> e.getView().getTitle().equals(gui.getTitle())) // I REALLY HATE THIS WAY OF CHECKING GUI BUT NO ALTERNATIVES (InventoryHolder is discouraged)
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
        if (customGUI.equals(CustomGUI.MENU_GUI)) {
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
                    int amountDestroyed = botManager.deleteAll();
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
                    break;
                default:
                    break;
            }
            return;
        }

        /*
            If GUI is a BOT GUI
         */
        if (customGUI.equals(CustomGUI.BOT_GUI)) {

            List<MetadataValue> metadata = player.getMetadata("selectedBotId");

            Optional<MetadataValue> botIdValue = metadata.stream()
                    .filter(value -> value.getOwningPlugin().equals(plugin))
                    .findFirst();

            if (!botIdValue.isPresent()) return;

            int botId = botIdValue.get().asInt();
            Bot bot = botManager.getBot(botId);

            switch(customItem) {

                case CHANGE_GENDER:
                    try {
                        Genderable genderable = (Genderable) bot;
                        genderable.setGender(genderable.getGender().nextGender());
                        player.sendMessage(ChatColor.GRAY + "You have updated the gender to " + ChatColor.GREEN + genderable.getGender().name());
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);

                        // update BotInfo
                        ItemStack baseBotInfo = CustomItems.BASE_BOT_INFO.getItemStack().clone();
                        ItemStack botInfo = ItemStackUtil.setCustomLore(
                                baseBotInfo,
                                bot.getInfo() == null ? Arrays.asList(ChatColor.RED + "Cannot get bot information") : bot.getInfo()
                        );
                        e.getInventory().setItem(0, botInfo);
                    } catch (ClassCastException exception) {
                        player.sendMessage(ChatColor.DARK_RED + "You are not allowed to change the gender of a non-genderable class");
                        throw new ClassCastException("You are not allowed to change gender of non-genderable class");
                    }
                    break;
                case SHOW_GAMESTATE:
                    // TODO: make gameState visible & change GameState into class not Util
                    break;
                case DELETE_BOT:
                    botManager.deleteBot(botId);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 0);
                    break;
                case EXIT_GUI:
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
                    break;
                default: break;
            }
            return;
        }

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().equals(CustomGUI.MENU_GUI.getInventory())) {

            CustomGUI.MENU_GUI.setItem(6, CustomItems.DESTROY_ALL_BOTS.getItemStack());

        }
    }


}