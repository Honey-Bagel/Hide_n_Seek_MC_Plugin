package bagel.builds.hide_n_seek.listener.misc;

import bagel.builds.hide_n_seek.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

public class CarryHandler implements Listener {

    public CarryHandler(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent e) {
        if(e.getRightClicked().getType() == EntityType.PLAYER) {
            if (e.getPlayer().isSneaking()) {
                e.getPlayer().addPassenger(e.getRightClicked());
            } else {
                e.getRightClicked().addPassenger(e.getPlayer());
            }
        }
    }

//    @EventHandler
//    public void onSneak(PlayerToggleSneakEvent e) {
//        if(e.isSneaking()) {
//            e.getPlayer().eject();
//        }
//    }

    //throw passengers off when shift left click
    @EventHandler
    public void onLeftClick(PlayerInteractEvent e) {
        if (e.getPlayer().isSneaking()) {
            if (e.getHand() == EquipmentSlot.HAND) {
                if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                    Player player = e.getPlayer();
                    if (player.getPassengers() != null) {
                        Vector dir = player.getLocation().getDirection();
                        Location loc = player.getLocation();
                        Vector vec = loc.getDirection().multiply(5f).setY(1.2f);
                        player.eject();
                        for (Entity entity : player.getPassengers()) {
                            System.out.println("test");
                            entity.setVelocity(vec);
                        }
                    }
                }
            }
        }
    }

}
