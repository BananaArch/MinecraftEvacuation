package com.bananaarch.minecraftevacuation.ux.listeners;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.BotManager;
import com.bananaarch.minecraftevacuation.tasks.TaskManager;
import com.bananaarch.minecraftevacuation.utils.CustomGUI;
import com.bananaarch.minecraftevacuation.ux.events.UniversalEntityInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;

public class NPCInteractListener implements Listener {

    private final MinecraftEvacuation plugin = MinecraftEvacuation.getInstance();
    private final TaskManager taskManager = MinecraftEvacuation.getInstance().getTaskManager();
    private final BotManager botManager = MinecraftEvacuation.getInstance().getBotManager();

    @EventHandler
    public void onNPCInteractEvent(UniversalEntityInteractEvent e) {

        Player player = e.getPlayer();
        int entityId = e.getEntityId();

        if (botManager.containsBot(entityId)) {

            taskManager.runTask(() -> player.openInventory(CustomGUI.BOT_GUI.getInventory()));
            player.setMetadata("selectedBotId", new FixedMetadataValue(plugin, entityId)); // adds metadata to player making it easily accessible

        }

    }
}
