package bagel.builds.hide_n_seek.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PushCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        List<Entity> entites = player.getNearbyEntities(10,2,10);

        for(Entity e : entites) {
            if(player.hasLineOfSight(e)) {
                Vector vector = null;
                if(player.getLocation().getPitch() <= 0) {
                    vector = player.getLocation().getDirection().add(new Vector(0,0.15,0)).multiply(2);
                } else {
                    vector = player.getLocation().getDirection().add(new Vector(0, -0.1, 0)).multiply(2);
                }

                assert vector != null;
                e.setVelocity(vector);
            }
        }

        return false;
    }
}
