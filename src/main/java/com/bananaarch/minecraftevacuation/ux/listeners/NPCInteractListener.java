package com.bananaarch.minecraftevacuation.ux.listeners;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.Bot;
import com.bananaarch.minecraftevacuation.bot.BotManager;
import com.bananaarch.minecraftevacuation.tasks.TaskManager;
import com.bananaarch.minecraftevacuation.ux.utils.CustomGUI;
import com.bananaarch.minecraftevacuation.ux.utils.CustomItems;
import com.bananaarch.minecraftevacuation.ux.utils.InventoryUtil;
import com.bananaarch.minecraftevacuation.ux.utils.ItemStackUtil;
import com.bananaarch.minecraftevacuation.ux.events.UniversalEntityInteractEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;

public class NPCInteractListener implements Listener {

    private final MinecraftEvacuation plugin = MinecraftEvacuation.getInstance();
    private final TaskManager taskManager = MinecraftEvacuation.getInstance().getTaskManager();
    private final BotManager botManager = MinecraftEvacuation.getInstance().getBotManager();

    @EventHandler
    public void onNPCInteractEvent(UniversalEntityInteractEvent e) {

        if (!botManager.containsBot(e.getEntityId())) return;

        Player player = e.getPlayer();
        int botId = e.getEntityId();
        Bot bot = botManager.getBot(botId);

        /*
        Create new NPC for custom bot by cloning the bot_gui enum's inventory
         */

        ItemStack baseBotInfo = CustomItems.BASE_BOT_INFO.getItemStack().clone();
        ItemStack botInfo = ItemStackUtil.setCustomLore(
                baseBotInfo,
                bot.getInfo() == null ? Arrays.asList(ChatColor.RED + "Cannot get bot information") : bot.getInfo()
        );

        Inventory botGUI = InventoryUtil.cloneInventory(CustomGUI.BOT_GUI.getInventory(), CustomGUI.BOT_GUI.getTitle());
        botGUI.setItem(0, botInfo);

        taskManager.runTask(() -> player.openInventory(botGUI));
        player.setMetadata("selectedBotId", new FixedMetadataValue(plugin, botId)); // adds metadata to player making it easily accessible



    }
}
