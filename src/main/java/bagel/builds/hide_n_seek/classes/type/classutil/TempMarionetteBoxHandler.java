package bagel.builds.hide_n_seek.classes.type.classutil;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.type.MarionetteClass;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
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

        file = new File(main.getDataFolder().getPath(), "music-boxes.yml");
        this.musicboxes = YamlConfiguration.loadConfiguration(file);
        saveConfig();
        Bukkit.getPluginManager().registerEvents(this, main);

    }

    private void saveConfig() {
        try {
            this.musicboxes.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.file = new File(main.getDataFolder().getPath(), "music-boxes.yml");
        this.musicboxes = YamlConfiguration.loadConfiguration(file);
    }

    @EventHandler
    public void onBoxPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        int count = Objects.requireNonNull(musicboxes.getConfigurationSection("locations")).getKeys(false).size();
        if(e.getItemInHand().equals(musicBox) && e.getBlockPlaced().getType().equals(Material.JUKEBOX)) {
            Location loc = e.getBlockPlaced().getLocation();

//            musicboxes.createSection("locations.musicbox" + count);
//            musicboxes.createSection("locations.musicbox" + count + ".x");
//            musicboxes.createSection("locations.musicbox" + count + ".y");
//            musicboxes.createSection("locations.musicbox" + count + ".z");
//            musicboxes.createSection("locations.musicbox" + count + ".world");


            musicboxes.set("locations.musicbox-" + count + ".x", loc.getBlockX());
            musicboxes.set("locations.musicbox-" + count + ".y", loc.getBlockY());
            musicboxes.set("locations.musicbox-" + count + ".z", loc.getBlockZ());
            musicboxes.set("locations.musicbox-" + count + ".world", loc.getWorld().getName());

            saveConfig();
            marionetteClass.addLocation(loc);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

        if(e.getBlock().getType().equals(Material.JUKEBOX)) {
//            System.out.println("test");
            if(isMarionetteBox(e.getBlock().getLocation()) != -1) {
                musicboxes.set("locations.musicbox-" + (isMarionetteBox(e.getBlock().getLocation())), null);
//                System.out.println(locations.indexOf(e.getBlock().getLocation()));
                removeLocation(e.getBlock().getLocation());
            }

        }
    }

    //LEARN YML
    public void fileStart() throws IOException {
        System.out.println(ChatColor.GREEN + "Marionette on Enable event");
        if(!musicboxes.isConfigurationSection("locations")) {
            musicboxes.createSection("locations");
            musicboxes.save(file);
        }
            for (String string : musicboxes.getConfigurationSection("locations").getKeys(false)) {
//                System.out.println("test");

                marionetteClass.addLocation(new Location(
                        Bukkit.createWorld(new WorldCreator(musicboxes.getString("locations." + string + ".world"))),
                        musicboxes.getDouble("locations." + string + ".x"),
                        musicboxes.getDouble("locations." + string + ".y"),
                        musicboxes.getDouble("locations." + string + ".z")));
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
                System.out.println(locations.indexOf(l));
                return locations.indexOf(l);
            }
        }
        return -1;
    }


    public ItemStack getMusicBox() { return musicBox; }
    public void removeLocation(Location loc) {
        marionetteClass.removeLocation(loc);
        locations.remove(loc);
    }

}
