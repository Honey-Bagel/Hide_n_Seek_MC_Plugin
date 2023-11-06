package bagel.builds.hide_n_seek;

import bagel.builds.hide_n_seek.classes.type.classutil.TempMarionetteBoxHandler;
import bagel.builds.hide_n_seek.command.*;
import bagel.builds.hide_n_seek.listener.GameListener;
import bagel.builds.hide_n_seek.listener.misc.*;
import bagel.builds.hide_n_seek.manager.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;


public final class Main extends JavaPlugin {

    private GameManager gameManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        new CarryHandler(this);
        new IronTDHandler(this);
        new FallingBlockHandler(this);
        new CarryHandler(this);
        new SitHandler(this);
        new ResourcePackHandler(this);
        new GuiListener(this);
        new VentHandler(this);

        gameManager = new GameManager(this);

        getCommand("nightvision").setExecutor(new NightVision());
        getCommand("sit").setExecutor(new SitCommand());
        getCommand("speed").setExecutor(new SpeedCommand());
        getCommand("speed").setTabCompleter(new SpeedTabCompleter());
        getCommand("hat").setExecutor(new HatCommand());
        getCommand("hat").setTabCompleter(new HatTabCompleter());
        getCommand("gui").setExecutor(new GuiCommand(this));
        getCommand("vent").setExecutor(new TestCommand(this));

        Bukkit.getPluginManager().registerEvents(new GameListener(this, gameManager), this);
        Bukkit.getPluginManager().registerEvents(new TempMarionetteBoxHandler(this, gameManager), this);

    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initiateFile(String name) throws Exception {
        File file = new File(getDataFolder(), name);
        if(!file.exists()) {
            file.createNewFile();
        }
        YamlConfiguration modifyFile = YamlConfiguration.loadConfiguration(file);
    }

    public GameManager getGameManager() { return gameManager; }
}
