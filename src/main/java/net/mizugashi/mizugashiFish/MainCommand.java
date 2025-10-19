package net.mizugashi.mizugashiFish;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.mizugashi.mizugashiFish.MizugashiFish.*;

public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {

            if (args.length == 0) {
                player.sendMessage("§7========" + prefix + "§7========");
                player.sendMessage("§7);
            }

        } else {
            sender.sendMessage(prefix + " §cプレイヤーのみ入力できます！");
            return false;
        }
        return false;
    }
}
