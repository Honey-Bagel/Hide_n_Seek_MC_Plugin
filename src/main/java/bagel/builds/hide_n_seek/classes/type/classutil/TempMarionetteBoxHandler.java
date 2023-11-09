package bagel.builds.hide_n_seek.classes.type.classutil;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.type.MarionetteClass;
import bagel.builds.hide_n_seek.manager.GameManager;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TempMarionetteBoxHandler implements Listener {

    private Main main;
    private ItemStack musicBox;
    private FileConfiguration musicboxes;
    private File file;
    private List<Location> locations;
    private MarionetteClass marionetteClass;


    public TempMarionetteBoxHandler(Main main, MarionetteClass marionetteClass) {
        this.main = main;
        this.marionetteClass = marionetteClass;
        this.locations = marionetteClass.getLocations();

        musicBox = new ItemStack(Material.JUKEBOX);
        ItemMeta boxMeta = musicBox.getItemMeta();
        boxMeta.setDisplayName(ChatColor.GOLD + "Marionette Music Box Maker");
        boxMeta.setLocalizedName("marionette music box maker");
        musicBox.setItemMeta(boxMeta);

        this.file = new File(main.getDataFolder(), "locations.yml");
        this.musicboxes = YamlConfiguration.loadConfiguration(file);
        saveConfig();
        Bukkit.getPluginManager().registerEvents(this, this.main);

    }

    private void saveConfig() {
        try {
            this.musicboxes.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.file = new File(main.getDataFolder().getPath(), "locations.yml");
        this.musicboxes = YamlConfiguration.loadConfiguration(file);
    }

    @EventHandler
    public void onBoxPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        int count = Objects.requireNonNull(musicboxes.getConfigurationSection("locations").getKeys(false).size()) ;
        if(e.getItemInHand().equals(musicBox) && e.getBlockPlaced().getType().equals(Material.JUKEBOX)) {
            Location loc = e.getBlockPlaced().getLocation();

//            musicboxes.createSection("locations.musicbox" + count);
//            musicboxes.createSection("locations.musicbox" + count + ".x");
//            musicboxes.createSection("locations.musicbox" + count + ".y");
//            musicboxes.createSection("locations.musicbox" + count + ".z");
//            musicboxes.createSection("locations.musicbox" + count + ".world");


            musicboxes.set("Music-Box-locations.musicbox-" + count + ".x", loc.getBlockX());
            musicboxes.set("Music-Box-locations.musicbox-" + count + ".y", loc.getBlockY());
            musicboxes.set("Music-Box-locations.musicbox-" + count + ".z", loc.getBlockZ());
            musicboxes.set("Music-Box-locations.musicbox-" + count + ".world", loc.getWorld().getName());

            saveConfig();
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

        if(e.getBlock().getType().equals(Material.JUKEBOX)) {
//            System.out.println("test");
            if(isMarionetteBox(e.getBlock().getLocation()) != -1) {
                musicboxes.set("Music-Box-locations.musicbox-" + isMarionetteBox(e.getBlock().getLocation()), null);
//                System.out.println(locations.indexOf(e.getBlock().getLocation()));
            }

        }
    }

    //LEARN YML
    public void fileStart() throws IOException {
        System.out.println(ChatColor.GREEN + "Marionette on Enable event");
        if(!musicboxes.isConfigurationSection("Music-Box-locations")) {
            musicboxes.createSection("Music-Box-locations");
            musicboxes.save(file);
        }
            for (String string : musicboxes.getConfigurationSection("Music-Box-locations").getKeys(false)) {
//                System.out.println("test");

                marionetteClass.addLocation(new Location(
                        Bukkit.createWorld(new WorldCreator(musicboxes.getString("locations." + string + ".world"))),
                        musicboxes.getDouble("Music-Box-locations." + string + ".x"),
                        musicboxes.getDouble("Music-Box-locations." + string + ".y"),
                        musicboxes.getDouble("Music-Box-locations." + string + ".z")));
//                System.out.println(locations.toString());
//                System.out.println(locations.toArray());
//                System.out.println(locations.toArray().toString());
            }
    }

    public int isMarionetteBox(Location loc) {
//        System.out.println("loc test");
        for(Location l : locations) {
//            System.out.println("l -------- " + l.toString() + " ------ " + l.toVector());
//            System.out.println("loc --------- " + loc.toString() + " --------- " + l.toVector());

            if(l.equals(loc)) {
                return locations.indexOf(l);
            }
        }
        return -1;
    }


    public ItemStack getMusicBox() { return musicBox; }

}
