package bagel.builds.hide_n_seek.commands;

import bagel.builds.hide_n_seek.classes.GUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuiCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)) return false;

        new GUI((Player) sender, 1);

        return false;
    }
}
