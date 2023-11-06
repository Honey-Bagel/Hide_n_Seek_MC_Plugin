package bagel.builds.hide_n_seek.classes.type.classutil;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.manager.GameManager;
import bagel.builds.hide_n_seek.manager.JukeBoxFileManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.block.data.type.Jukebox;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class TempMarionetteBoxHandler implements Listener {

    private Main main;
    private GameManager gameManager;
    private ItemStack musicBox;
    private FileConfiguration musicboxes;
    private JukeBoxFileManager locmanager;
    private List<Location> locations;


    public TempMarionetteBoxHandler(Main main, GameManager gameManager) {
        this.gameManager = gameManager;
        this.main = main;

        musicBox = new ItemStack(Material.JUKEBOX);
        ItemMeta boxMeta = musicBox.getItemMeta();
        boxMeta.setDisplayName(ChatColor.GOLD + "Marionette Music Box Maker");
        boxMeta.setLocalizedName("marionette music box maker");
        musicBox.setItemMeta(boxMeta);

        this.locmanager = new JukeBoxFileManager(main);
        this.musicboxes = locmanager.getData();
    }

    @EventHandler
    public void onBoxPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        int count = musicboxes.getConfigurationSection("locations").getKeys(false).size();
        if(e.getItemInHand().equals(musicBox) && e.getBlockPlaced().getType().equals(Material.JUKEBOX)) {
            Location loc = e.getBlockPlaced().getLocation();

            musicboxes.set("locations.musicbox-" + count + ".x.", loc.getBlockX());
            musicboxes.set("locations.musicbox-" + count + ".y.", loc.getBlockY());
            musicboxes.set("locations.musicbox-" + count + ".z.", loc.getBlockZ());
            musicboxes.set("locations.musicbox-" + count + ".world.", loc.getWorld());

            locmanager.saveConfig();
        }
    }

    @EventHandler
    public void onEnable() {
        locmanager.load();

        for(String string : Objects.requireNonNull(musicboxes.getConfigurationSection("locations.").getKeys(false))) {
            locations.add(new Location(
                    Bukkit.createWorld(new WorldCreator(musicboxes.getString("location." + string + ".world"))),
                    musicboxes.getDouble("locations." + string + ".x"),
                    musicboxes.getDouble("locations." + string + ".y"),
                    musicboxes.getDouble("locations." + string + ".z")));
        }
        System.out.println(locations.toArray().toString());
    }

    public ItemStack getMusicBox() { return musicBox; }

}
