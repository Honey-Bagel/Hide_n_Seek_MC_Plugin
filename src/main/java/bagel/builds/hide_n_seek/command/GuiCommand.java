package bagel.builds.hide_n_seek.command;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.ClassUI;
import bagel.builds.hide_n_seek.classes.TeamUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuiCommand implements CommandExecutor {
    private Main main;

    public GuiCommand(Main main) { this.main = main;}
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if(args.length == 1 && args[0].equalsIgnoreCase("class")) {
            new ClassUI(main, main.getGameManager(), player);
        } else if(args.length == 1 && args[0].equalsIgnoreCase("team")) {
            new TeamUI(main.getGameManager(), player);
        } else if(args.length == 1 && args[0].equalsIgnoreCase("getteam")) {
            if(main.getGameManager().getTeams().containsKey(player.getUniqueId())) {
                player.sendMessage(main.getGameManager().getTeam(player).toString());
            }
        }

        return false;
    }

}
