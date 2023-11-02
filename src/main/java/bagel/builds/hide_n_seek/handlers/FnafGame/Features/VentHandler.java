package bagel.builds.hide_n_seek.handlers.FnafGame.Features;

import bagel.builds.hide_n_seek.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

public class VentHandler implements Listener {

    public VentHandler(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    //add ability to set certain trapdoors to vent trapdoors so not all are possible


    @EventHandler
    public void onVent(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        if (!player.isSneaking() || !player.isOnGround()) return;
        Block targetBlock = player.getTargetBlockExact(1);
        if (targetBlock == null) return;
        Block vent = player.getWorld().getBlockAt(targetBlock.getLocation().subtract(0, 1, 0));
        if (vent.getType() == Material.IRON_TRAPDOOR) {
            if (vent.getType() != null && targetBlock.getType() != null && targetBlock.getType() != Material.AIR && !targetBlock.isPassable()) {
                Location vLoc = vent.getLocation();
                Location test = new Location(player.getWorld(), getRelativeCoord((int) vLoc.getX()), vLoc.getY(), getRelativeCoord((int) vLoc.getZ()), player.getLocation().getYaw(), player.getLocation().getPitch());
                player.setSwimming(true);
                player.teleport(test);
                player.sendMessage(ChatColor.GRAY + "Entered the vents...");
            }
        }

    }

    private double getRelativeCoord(int i) {
        double d = i;
        d = d < 0 ? d + .5 : d + .5;
        return d;
    }



}
