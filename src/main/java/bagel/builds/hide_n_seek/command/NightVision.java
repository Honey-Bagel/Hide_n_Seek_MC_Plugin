package bagel.builds.hide_n_seek.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightVision implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if(!(sender instanceof Player)) return true;

        Player player = (Player) sender;

            if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                player.sendMessage("nv toggled off");

            } else {
                player.sendMessage("nv toggled on");
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 1, true,false,false));
            }

        return false;
    }
}
