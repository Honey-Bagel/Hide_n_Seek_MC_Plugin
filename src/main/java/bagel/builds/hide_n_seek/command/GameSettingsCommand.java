package bagel.builds.hide_n_seek.command;

import bagel.builds.hide_n_seek.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

public class GameSettingsCommand implements CommandExecutor {
    private Main main;
    private FileConfiguration config;
    public GameSettingsCommand(Main main) throws UnsupportedEncodingException {
        this.main = main;
        this.config = main.getGameSettingsConfig().getConfig();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)) {
            return false;
        }

        //check if player is in a game

        //check if player is game manager

        if(args.length == 2 && args[0].equalsIgnoreCase("setgametime")) {
            int time;
            try {
                time = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }

            config.set("Game.game-time", time);
            main.getGameSettingsConfig().saveConfig();
            sender.sendMessage("" + config.get("Game.game-time"));

        } else if(args.length == 2 && args[0].equalsIgnoreCase("sethidetime")) {
            int time;
            try {
                time = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }

            config.set("Game.hiding-time", time);
            main.getGameSettingsConfig().saveConfig();
            sender.sendMessage("" + config.get("Game.hiding-time"));
        } else if(args.length == 2 && args[0].equalsIgnoreCase("allowdupes")) {
            boolean value;
            try {
                value = Boolean.parseBoolean(args[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }

            config.set("Game.allow-duplicates", value);
            main.getGameSettingsConfig().saveConfig();
            sender.sendMessage("" + config.get("Game.allow-duplicates"));
            if(!value) {
                //remove dupes
//                main.getGameManager().removeDupes;
            }
        } else if(args.length == 2 && args[0].equalsIgnoreCase("nightmare")) {
            boolean value;
            try {
                value = Boolean.parseBoolean(args[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }

            config.set("Game.nightmare", value);
            main.getGameSettingsConfig().saveConfig();
            sender.sendMessage("" + config.get("Game.nightmare"));
        } else if(args.length == 2 && args[0].equalsIgnoreCase("cooldown")) {
            int cooldown;
            try {
                cooldown = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
            if(Objects.equals(config.get("Game.additonal-cooldown"), 0) && cooldown <0) {
                return false;
            } else if(cooldown == 0) {
                config.set("Game.additional-cooldown", cooldown);
            } else if(Objects.equals(config.get("Game.additional-cooldown"), 50) && cooldown>0) {
                return false;
            } else {
                config.set("Game.additional-cooldown", Integer.parseInt(Objects.requireNonNull(config.get("Game.additional-cooldown")).toString()) + cooldown);
            }
            main.getGameSettingsConfig().saveConfig();
            sender.sendMessage("" + config.get("Game.additional-cooldown"));
        }


        return false;
    }
}
