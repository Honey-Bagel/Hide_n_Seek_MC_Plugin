package bagel.builds.hide_n_seek.manager;

import bagel.builds.hide_n_seek.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class JukeBoxFileManager {


    private FileConfiguration boxLoc;
    private File file;
    private Main main;

    public JukeBoxFileManager(Main main) {
        this.main = main;
    }

    public void load() {
        this.file = new File(main.getDataFolder(), "music-boxes.yml");
        this.boxLoc = YamlConfiguration.loadConfiguration(file);
        this.boxLoc.options().copyDefaults(true);
        saveConfig();
    }

    public void saveConfig() {
        try {
            this.boxLoc.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // reassign variables
        this.file = new File(main.getDataFolder(), "music-boxes.yml");
        this.boxLoc = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getData() {
        return boxLoc;
    }



}
