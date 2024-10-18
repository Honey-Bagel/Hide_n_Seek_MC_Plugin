package bagel.builds.hide_n_seek.listener.misc;

import bagel.builds.hide_n_seek.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class SitHandler implements Listener {

    public SitHandler(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this,plugin);
    }

    //deletes "chair" armorstand entity on dismount
    @EventHandler
    public void onVehicle(VehicleExitEvent e) {
        if(!(e.getVehicle() instanceof Player)) return;
        Player player = (Player) e.getVehicle();
        if(e.getVehicle() instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) e.getVehicle();
            armorStand.remove();
            if(e.getVehicle() instanceof Player) {
                e.getVehicle().teleport(e.getVehicle().getLocation().add(0,0.7,0).setDirection(player.getLocation().getDirection()));
            }
        }
    }
}
