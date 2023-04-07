package com.bananaarch.minecraftevacuation.settings.command;

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

        Player player = (Player) commandSender;
        player.openInventory(CustomGUI.SETTINGS_GUI.getInventory());

        return false;

    }
}
