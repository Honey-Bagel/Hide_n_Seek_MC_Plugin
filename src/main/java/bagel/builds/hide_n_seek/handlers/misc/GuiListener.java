package bagel.builds.hide_n_seek.handlers.misc;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.GUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiListener implements Listener {

    public GuiListener(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        if(e.getInventory() != null && e.getCurrentItem() != null && e.getView().getTitle().contains("Page test")) {

            int page = Integer.parseInt(e.getInventory().getItem(0).getItemMeta().getLocalizedName());

            if(e.getRawSlot() == 0 && e.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE)) {
                new GUI((Player) e.getWhoClicked(), page - 1);
            } else if(e.getRawSlot() == 8 && e.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE));
            new GUI((Player) e.getWhoClicked(), page + 1);
            e.setCancelled(true);
        }

    }
}
