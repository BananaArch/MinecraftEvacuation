package com.bananaarch.minecraftevacuation.settings.blocklistener;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.Bot;
import com.bananaarch.minecraftevacuation.bot.BotManager;
import com.bananaarch.minecraftevacuation.bot.utils.BotType;
import com.bananaarch.minecraftevacuation.settings.utils.CustomItems;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlacedListener implements Listener {

    private final BotManager botManager = MinecraftEvacuation.getInstance().getManager();



    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e) {

        if (e.getPlayer().getInventory().getItemInMainHand().equals(CustomItems.FRESHMAN_ITEM.getItemStack())) {
            botManager.addBot();
        }
        if (e.getPlayer().getInventory().getItemInMainHand().equals(CustomItems.SOPHOMORE_ITEM.getItemStack())) {
            e.getPlayer().sendMessage("YOLA");
        }

        if (e.getPlayer().getInventory().getItemInMainHand().equals(CustomItems.JUNIOR_ITEM.getItemStack())) {
            e.getPlayer().sendMessage("YOLAND");
        }

        if (e.getPlayer().getInventory().getItemInMainHand().equals(CustomItems.SENIOR_ITEM.getItemStack())) {
            e.getPlayer().sendMessage("YOLANDA");
        }

        if (e.getPlayer().getInventory().getItemInMainHand().equals(CustomItems.TEACHER_ITEM.getItemStack())) {

        }

    }

}