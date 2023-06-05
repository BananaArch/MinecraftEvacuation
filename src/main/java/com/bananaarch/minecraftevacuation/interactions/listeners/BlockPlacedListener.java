package com.bananaarch.minecraftevacuation.interactions.listeners;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.BotFactory;
import com.bananaarch.minecraftevacuation.bot.BotManager;
import com.bananaarch.minecraftevacuation.bot.BotType;
import com.bananaarch.minecraftevacuation.interactions.utils.CustomItems;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class BlockPlacedListener implements Listener {

    private final BotManager botManager = MinecraftEvacuation.getInstance().getBotManager();

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e) {

        Player player = e.getPlayer();
        ItemStack itemInMainHand = e.getPlayer().getInventory().getItemInMainHand();
        Location blockPlacedLocation = e.getBlockPlaced().getLocation();

        CustomItems customItem = Arrays.stream(CustomItems.values())
                .filter(item -> itemInMainHand.isSimilar(item.getItemStack()))
                .findFirst()
                .orElse(null);

        // return if block is not custom Item
        if (customItem == null) return;

        switch(customItem) {
            case FRESHMAN_ITEM:
                botBlockPlaced(player, blockPlacedLocation, BotType.FRESHMAN);
                e.setCancelled(true);
                break;
            case SOPHOMORE_ITEM:
                botBlockPlaced(player, blockPlacedLocation, BotType.SOPHOMORE);
                e.setCancelled(true);
                break;
            case JUNIOR_ITEM:
                botBlockPlaced(player, blockPlacedLocation, BotType.JUNIOR);
                e.setCancelled(true);
                break;
            case SENIOR_ITEM:
                botBlockPlaced(player, blockPlacedLocation, BotType.SENIOR);
                e.setCancelled(true);
                break;
            case TEACHER_ITEM:
                botBlockPlaced(player, blockPlacedLocation, BotType.TEACHER);
                e.setCancelled(true);
                break;
            case TARGET_BLOCK:
                botManager.setBotsTargetLocation(blockPlacedLocation);
                botManager.generatePaths();
                player.sendMessage(ChatColor.GRAY + "Successfully set a " + ChatColor.GREEN + "Target");
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 0);
                e.setCancelled(true);
                break;
        }

    }

    private void botBlockPlaced(Player player, Location blockPlacedLocation, BotType botType) {
        BotFactory.createBot(blockPlacedLocation.add(new Vector(.5, 0, .5)), botType.name(), botType);
        player.sendMessage(ChatColor.GRAY + "Successfully created a " + ChatColor.GREEN + botType.name().toUpperCase().charAt(0) + botType.name().toLowerCase().substring(1));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
    }

}