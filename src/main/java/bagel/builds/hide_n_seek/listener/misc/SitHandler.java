package bagel.builds.hide_n_seek.listener.misc;

import bagel.builds.hide_n_seek.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

public class SitHandler implements Listener {

    public SitHandler(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this,plugin);
    }

    //deletes "chair" armorstand entity on dismount
    @EventHandler
    public void onVehicle(EntityDismountEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        if(e.getDismounted() instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) e.getDismounted();
            armorStand.remove();
            if(e.getEntity() instanceof Player) {
                e.getEntity().teleport(e.getEntity().getLocation().add(0,0.7,0).setDirection(player.getLocation().getDirection()));
            }
        }
    }
}
