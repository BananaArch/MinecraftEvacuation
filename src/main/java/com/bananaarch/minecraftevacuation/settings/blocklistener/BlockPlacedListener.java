package com.bananaarch.minecraftevacuation.settings.blocklistener;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.Bot;
import com.bananaarch.minecraftevacuation.bot.BotManager;
import com.bananaarch.minecraftevacuation.utils.BlockPlaceUtil;
import com.bananaarch.minecraftevacuation.utils.BotType;
import com.bananaarch.minecraftevacuation.utils.CustomItems;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPlacedListener implements Listener {

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e) {

        Player player = e.getPlayer();
        ItemStack itemInMainHand = e.getPlayer().getInventory().getItemInMainHand();
        Location blockPlacedLocation = e.getBlockPlaced().getLocation();
        
        if (itemInMainHand.equals(CustomItems.FRESHMAN_ITEM.getItemStack())) {
            BlockPlaceUtil.placedBlock(player, blockPlacedLocation, BotType.FRESHMAN);
            return;
        }

        if (itemInMainHand.equals(CustomItems.SOPHOMORE_ITEM.getItemStack())) {
            BlockPlaceUtil.placedBlock(player, blockPlacedLocation, BotType.SOPHOMORE);
            return;
        }

        if (itemInMainHand.equals(CustomItems.JUNIOR_ITEM.getItemStack())) {
            BlockPlaceUtil.placedBlock(player, blockPlacedLocation, BotType.JUNIOR);
            return;
        }

        if (itemInMainHand.equals(CustomItems.SENIOR_ITEM.getItemStack())) {
            BlockPlaceUtil.placedBlock(player, blockPlacedLocation, BotType.SENIOR);
            return;
        }

        if (itemInMainHand.equals(CustomItems.TEACHER_ITEM.getItemStack())) {
            BlockPlaceUtil.placedBlock(player, blockPlacedLocation, BotType.TEACHER);
            return;
        }

    }

}