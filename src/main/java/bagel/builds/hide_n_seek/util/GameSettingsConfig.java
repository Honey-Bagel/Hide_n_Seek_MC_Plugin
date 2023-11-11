package bagel.builds.hide_n_seek.util;

import bagel.builds.hide_n_seek.Main;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.File;
import java.io.IOException;

public class GameSettingsConfig {
    private Main main;

    private File file;
    private FileConfiguration fileConfig;
    private ItemStack book;

    public GameSettingsConfig(Main main) {
        this.main = main;
        this.file = new File(main.getDataFolder(), "GameSettings.yml");
        this.fileConfig = YamlConfiguration.loadConfiguration(file);
    }

    public void saveConfig() {
        try {
            fileConfig.save(file);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void createBook() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bMeta = (BookMeta) book.getItemMeta();
        bMeta.setAuthor("FNAF");
        bMeta.setTitle("Game Settings");

        TextComponent clickable = new TextComponent("");
    }



    public File getFile() { return file; }
    public FileConfiguration getFileConfig() { return fileConfig; }


}
