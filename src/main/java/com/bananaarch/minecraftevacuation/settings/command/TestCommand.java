package com.bananaarch.minecraftevacuation.settings.command;

import com.bananaarch.minecraftevacuation.utils.GameStateUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.nd4j.linalg.api.ndarray.INDArray;

// For testing purposes only.

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        {

            if (!(commandSender instanceof Player)) {

                System.out.println("You must be a player to run the command, " + s);
                return false;

            }

            Player player = (Player) commandSender;

            player.sendMessage("Testing command");
            INDArray array = GameStateUtil.getGameState(player.getLocation());

            return false;

        }

    }

}
