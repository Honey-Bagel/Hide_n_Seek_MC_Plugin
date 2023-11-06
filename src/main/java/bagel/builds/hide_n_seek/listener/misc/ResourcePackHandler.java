package bagel.builds.hide_n_seek.listener.misc;

import bagel.builds.hide_n_seek.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ResourcePackHandler implements Listener {

    public ResourcePackHandler(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private int[] hatCustomModels = {1};

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) {
        Player player = (Player) e.getPlayer();

        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(player.getInventory().getItemInMainHand().getType() == Material.CARVED_PUMPKIN) {
                if (player.getInventory().getItemInMainHand().getItemMeta().hasCustomModelData()) {
                    if (player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 1) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
