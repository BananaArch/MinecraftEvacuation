package com.bananaarch.minecraftevacuation.settings.blocklistener;

import com.bananaarch.minecraftevacuation.bot.BotFactory;
import com.bananaarch.minecraftevacuation.bot.subclasses.Junior;
import com.bananaarch.minecraftevacuation.utils.BotType;
import com.bananaarch.minecraftevacuation.utils.CustomItems;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class BlockPlacedListener implements Listener {

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e) {

        Player player = e.getPlayer();
        ItemStack itemInMainHand = e.getPlayer().getInventory().getItemInMainHand();
        Location blockPlacedLocation = e.getBlockPlaced().getLocation();
        
        if (itemInMainHand.equals(CustomItems.FRESHMAN_ITEM.getItemStack())) {
            BotFactory.createBot(blockPlacedLocation.add(new Vector(0.5, 0, 0.5)), "Freshman", BotType.FRESHMAN);
            e.setCancelled(true);
            player.sendMessage(ChatColor.GRAY + "Successfully created a " + ChatColor.GREEN + "Freshman");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
            return;
        }

        if (itemInMainHand.equals(CustomItems.SOPHOMORE_ITEM.getItemStack())) {
            BotFactory.createBot(blockPlacedLocation.add(new Vector(0.5, 0, 0.5)), "Sophomore", BotType.SOPHOMORE);
            e.setCancelled(true);
            player.sendMessage(ChatColor.GRAY + "Successfully created a " + ChatColor.GREEN + "Sophomore");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
            return;
        }

        if (itemInMainHand.equals(CustomItems.JUNIOR_ITEM.getItemStack())) {
            BotFactory.createBot(blockPlacedLocation.add(new Vector(0.5, 0, 0.5)), "Junior", BotType.JUNIOR);
            e.setCancelled(true);
            player.sendMessage(ChatColor.GRAY + "Successfully created a " + ChatColor.GREEN + "Junior");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
            return;
        }

        if (itemInMainHand.equals(CustomItems.SENIOR_ITEM.getItemStack())) {
            BotFactory.createBot(blockPlacedLocation.add(new Vector(0.5, 0, 0.5)), "Senior", BotType.SENIOR);
            e.setCancelled(true);
            player.sendMessage(ChatColor.GRAY + "Successfully created a " + ChatColor.GREEN + "Senior");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
            return;
        }

        if (itemInMainHand.equals(CustomItems.TEACHER_ITEM.getItemStack())) {
            BotFactory.createBot(blockPlacedLocation.add(new Vector(0.5, 0, 0.5)), "Teacher", BotType.TEACHER);
            e.setCancelled(true);
            player.sendMessage(ChatColor.GRAY + "Successfully created a " + ChatColor.GREEN + "Teacher");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
            return;
        }

    }

}