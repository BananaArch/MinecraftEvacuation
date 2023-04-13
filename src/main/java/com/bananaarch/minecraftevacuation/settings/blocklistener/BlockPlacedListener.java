package com.bananaarch.minecraftevacuation.settings.blocklistener;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.Bot;
import com.bananaarch.minecraftevacuation.bot.BotManager;
import com.bananaarch.minecraftevacuation.utils.BotType;
import com.bananaarch.minecraftevacuation.utils.CustomItems;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.nd4j.linalg.api.ops.impl.reduce.same.Min;

public class BlockPlacedListener implements Listener {


    private final BotManager botManager;

    public BlockPlacedListener() {
        this.botManager = MinecraftEvacuation.getInstance().getManager();
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e) {

        Player player = e.getPlayer();
        ItemStack itemInMainHand = e.getPlayer().getInventory().getItemInMainHand();
        
        if (itemInMainHand.equals(CustomItems.FRESHMAN_ITEM.getItemStack())) {
            player.sendMessage("creating bot");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
            botManager.createBot(e.getBlockPlaced().getLocation(), "test", BotType.FRESHMAN);
            player.sendMessage("finsihed");
            return;
        }
        if (itemInMainHand.equals(CustomItems.SOPHOMORE_ITEM.getItemStack())) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
            return;
        }

        if (itemInMainHand.equals(CustomItems.JUNIOR_ITEM.getItemStack())) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
            return;
        }

        if (itemInMainHand.equals(CustomItems.SENIOR_ITEM.getItemStack())) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
            return;
        }

        if (itemInMainHand.equals(CustomItems.TEACHER_ITEM.getItemStack())) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 8);
            return;
        }

    }

}