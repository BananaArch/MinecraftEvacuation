package com.bananaarch.minecraftevacuation.settings.command;

import com.bananaarch.minecraftevacuation.MinecraftEvacuation;
import com.bananaarch.minecraftevacuation.bot.BotManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {

            System.out.println("You must be a player to run the command, " + s);
            return false;

        }

        BotManager botManager = MinecraftEvacuation.getInstance().getManager();

        Player player = (Player) commandSender;

//        Bot.createBot(player.getLocation(), "test", BotType.FRESHMAN);
        try {
            System.out.println(Stock.onCreate());
        } catch (Exception e) {
            System.out.println("TEST");
        }


//        player.openInventory(CustomGUI.SETTINGS_GUI.getInventory());

        return false;

    }
}
