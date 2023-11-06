package bagel.builds.hide_n_seek.command;

import bagel.builds.hide_n_seek.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {
    private Main main;

    public TestCommand(Main main) { this.main = main; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)) return false;

        sender.sendMessage("In Vents: " + main.getGameManager().getVentManager().getVentList().get(((Player) sender).getUniqueId()));

        return false;
    }
}
