package bagel.builds.hide_n_seek.classes.type.classutil;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.manager.GameManager;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class TempMarionetteBoxHandler implements Listener {

    private Main main;
    private ItemStack musicBox;
    private FileConfiguration musicboxes;
    private File file;
    private List<Location> locations;


    public TempMarionetteBoxHandler(Main main) {
        this.main = main;

        musicBox = new ItemStack(Material.JUKEBOX);
        ItemMeta boxMeta = musicBox.getItemMeta();
        boxMeta.setDisplayName(ChatColor.GOLD + "Marionette Music Box Maker");
        boxMeta.setLocalizedName("marionette music box maker");
        musicBox.setItemMeta(boxMeta);

        this.file = new File(main.getDataFolder(), "music-boxes.yml");
        this.musicboxes = YamlConfiguration.loadConfiguration(file);
        saveConfig();

    }

    private void saveConfig() {
        try {
            this.musicboxes.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.file = new File(main.getDataFolder(), "music-boxes.yml");
        this.musicboxes = YamlConfiguration.loadConfiguration(file);
    }

    @EventHandler
    public void onBoxPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        int count = Objects.requireNonNull(musicboxes.getConfigurationSection("locations").getKeys(false).size());
        if(e.getItemInHand().equals(musicBox) && e.getBlockPlaced().getType().equals(Material.JUKEBOX)) {
            Location loc = e.getBlockPlaced().getLocation();

            musicboxes.createSection("locations.musicbox" + count);
//            musicboxes.createSection("locations.musicbox" + count + ".x.");
//            musicboxes.createSection("locations.musicbox" + count + ".y.");
//            musicboxes.createSection("locations.musicbox" + count + ".z.");
//            musicboxes.createSection("locations.musicbox" + count + ".world.");


            musicboxes.set("locations.musicbox-" + count + ".x.", loc.getBlockX());
            musicboxes.set("locations.musicbox-" + count + ".y.", loc.getBlockY());
            musicboxes.set("locations.musicbox-" + count + ".z.", loc.getBlockZ());
            musicboxes.set("locations.musicbox-" + count + ".world.", loc.getWorld());

            saveConfig();
        }
    }

    //LEARN YML
    @EventHandler
    public void onEnable(ServerLoadEvent e) throws IOException {
        System.out.println(ChatColor.GREEN + "Marionette on Enable event");
        if(!musicboxes.isConfigurationSection("locations")) {
            musicboxes.createSection("locations");
            musicboxes.save(file);
        }
            for (String string : Objects.requireNonNull(musicboxes.getConfigurationSection("locations").getKeys(false))) {
                locations.add(new Location(
                        Bukkit.createWorld(new WorldCreator(musicboxes.getString("locations." + string + ".world"))),
                        musicboxes.getDouble("locations." + string + ".x"),
                        musicboxes.getDouble("locations." + string + ".y"),
                        musicboxes.getDouble("locations." + string + ".z")));
            }
    }

    public ItemStack getMusicBox() { return musicBox; }
    public List<Location> getLocations() { return locations; }

}
