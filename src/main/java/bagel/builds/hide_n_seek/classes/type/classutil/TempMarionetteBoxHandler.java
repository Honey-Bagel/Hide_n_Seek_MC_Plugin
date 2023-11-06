package bagel.builds.hide_n_seek.classes.type.classutil;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.manager.GameManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TempMarionetteBoxHandler implements Listener {

    private Main main;
    private GameManager gameManager;
    private ItemStack musicBox;
    private List<Location> locations;


    public TempMarionetteBoxHandler(Main main, GameManager gameManager) {
        this.gameManager = gameManager;
        this.main = main;

        musicBox = new ItemStack(Material.JUKEBOX);
        ItemMeta boxMeta = musicBox.getItemMeta();
        boxMeta.setDisplayName(ChatColor.GOLD + "Marionette Music Box Maker");
        boxMeta.setLocalizedName("marionette music box maker");
        musicBox.setItemMeta(boxMeta);

    }

    @EventHandler
    public void onBoxPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if(e.getItemInHand().equals(musicBox) && e.getBlockPlaced().getType().equals(Material.JUKEBOX)) {
            //save block location to yml file
        }
    }

    //on start add locations in yml file to a list to be used

    public ItemStack getMusicBox() { return musicBox; }

}
