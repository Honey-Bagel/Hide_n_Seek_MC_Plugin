package bagel.builds.hide_n_seek.command;

import bagel.builds.hide_n_seek.instance.Game;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameSettingsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)) {
            return false;
        }

        //check if player is in a game

        //check if player is game manager

        if(args.length == 2 && args[0].equalsIgnoreCase("setGameTime")) {
            int time;
            try {
                time = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }

            switch(time) {
                case(15):
                    Game.setGameTime(15);
                    break;
                case(20):
                    Game.setGameTime(20);
                    break;
                case(25):
                    Game.setGameTime(25);
                    break;
                case(30):
                    Game.setGameTime(30);
                    break;
                default:
                    break;
            }

        }


        return false;
    }
}
