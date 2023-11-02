package bagel.builds.hide_n_seek.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if(args[0].isEmpty()) {
            player.sendMessage(ChatColor.RED + "please enter a value for your new speed (-10 - 10).");
        }
        if(args[0].matches("reset")) {
            if(player.isFlying()) {
                try {
                    player.setFlySpeed(0.1F);
                    player.sendMessage("Reset player flying speed."); }
                catch(IllegalArgumentException e) {
                    System.out.println(e);
                }
            } else if(player.isOnGround()) {
                try {
                    player.setWalkSpeed(0.2F);
                    player.sendMessage("Reset player walking speed.");
                } catch(IllegalArgumentException e){
                    System.out.println(e);
                }
            }
        } else {

            float speed = Float.parseFloat(args[0]) / 10;
            if (speed <= -1 || speed > 1) {
                player.sendMessage(ChatColor.RED + "Please enter a number between -10 and 10.");
            }

            if (player.isFlying()) {
                try {
                    player.setFlySpeed(speed);
                    player.sendMessage("Set flying speed to " + speed * 10 + ".");
                } catch (IllegalArgumentException e) {
                    System.out.println(e);
                }
            } else if (player.isOnGround()) {
                try {
                    player.setWalkSpeed(speed);
                    player.sendMessage("Set walking speed to " + speed * 10 + ".");
                } catch (IllegalArgumentException e) {
                    System.out.println(e);
                }
            }
        }


        return false;
    }
}
