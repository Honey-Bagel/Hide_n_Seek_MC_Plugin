package bagel.builds.hide_n_seek.listener.misc;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.type.FoxyClass;
import bagel.builds.hide_n_seek.event.VentEvent;
import bagel.builds.hide_n_seek.classes.Animatronic;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class VentHandler implements Listener {

    private Main main;

    public VentHandler(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.main = plugin;
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
                Location location = new Location(player.getWorld(), getRelativeCoord((int) vLoc.getX()), vLoc.getY(), getRelativeCoord((int) vLoc.getZ()), player.getLocation().getYaw(), player.getLocation().getPitch());
                player.setSwimming(true);
                player.teleport(location);
                player.sendMessage(ChatColor.GRAY + "Entered the vents...");
                main.getGameManager().getVentManager().getVentList().put(player.getUniqueId(), true);
                Bukkit.getPluginManager().callEvent(new VentEvent(player, true));
            }
        }

    }

    @EventHandler
    public void onExitVent(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        if(player.getWorld().getBlockAt(player.getLocation()) != null && player.getWorld().getBlockAt(player.getLocation()).getType() != Material.IRON_TRAPDOOR && !main.getGameManager().getVentManager().getVentList().isEmpty() && main.getGameManager().getVentManager().getVentList().get(player.getUniqueId())) {
            main.getGameManager().getVentManager().getVentList().put(player.getUniqueId(), false);
            Bukkit.getPluginManager().callEvent(new VentEvent(player, false));
        }
    }

//    @EventHandler
//    public void onVentChange(VentEvent e) {
//        Player player = e.getPlayer();
//
//        if(e.getInVent()) {
//            if(main.getGameManager().getAnimatronic(player) == Animatronic.MANGLE) {
//                player.setWalkSpeed(1f);
//            } else {
//                player.setWalkSpeed(0.5f);
//            }
//        } else {
//            player.setWalkSpeed(0.2f);
//        }
//    }

    @EventHandler
    public void onVentChange(VentEvent e) {
            if (!(main.getGameManager().getAnimatronic(e.getPlayer()) == Animatronic.MANGLE)) {
                if (e.getInVent()) {
                    e.getPlayer().setWalkSpeed(0.5f);
                } else if(main.getGameManager().getAnimatronicsMap().containsKey(e.getPlayer().getUniqueId()) && main.getGameManager().getAnimatronic(e.getPlayer()).equals(Animatronic.FOXY) && main.getGameManager().getClasstypes().get(e.getPlayer().getUniqueId()).equals(Animatronic.FOXY) && FoxyClass.getRunning()) {
                    e.getPlayer().setWalkSpeed(0.4f);
                } else {
                    e.getPlayer().setWalkSpeed(0.2f);
                }
            }

    }


    private double getRelativeCoord(int i) {
        double d = i;
        d = d < 0 ? d + .5 : d + .5;
        return d;
    }



}
