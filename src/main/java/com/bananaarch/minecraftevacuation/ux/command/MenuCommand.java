package com.bananaarch.minecraftevacuation.ux.command;

import com.bananaarch.minecraftevacuation.ux.utils.CustomGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {

            System.out.println("You must be a player to run the command /" + s);
            return false;

        }

        Player player = (Player) commandSender;
        player.openInventory(CustomGUI.MENU_GUI.getInventory());

        return false;

    }
}
