package bagel.builds.hide_n_seek.classes.type.classutil;

import bagel.builds.hide_n_seek.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationsFileManager {
    private Main main;

    private File file = new File(main.getDataFolder(), "locations.yml");;
    private FileConfiguration fileConfig;

    public LocationsFileManager(Main main) {
        this.main = main;
    }

    public void saveConfig() {
        try {
            fileConfig.save(file);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public List<Location> getLocation(String section) {
        if(section.equalsIgnoreCase("Music-Box-locations") || section.equalsIgnoreCase("FreddyTPLoc")) {
            List<Location> temp = new ArrayList<>();
            if (!fileConfig.isConfigurationSection(section)) {
                fileConfig.createSection(section);
                saveConfig();
            }

            for (String str : fileConfig.getConfigurationSection(section).getKeys(false)) {
                temp.add(new Location(
                        Bukkit.createWorld(new WorldCreator(fileConfig.getString(section + "." + str + ".world"))),
                        fileConfig.getDouble(section + "." + str + ".x"),
                        fileConfig.getDouble(section + "." + str + ".y"),
                        fileConfig.getDouble(section + "." + str + ".z")));
            }
            return temp;
        } else { return null; }
    }


}
