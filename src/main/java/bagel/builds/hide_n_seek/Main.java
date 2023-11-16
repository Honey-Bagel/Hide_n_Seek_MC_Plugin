package bagel.builds.hide_n_seek;

import bagel.builds.hide_n_seek.classes.type.classutil.LocationsFileManager;
import bagel.builds.hide_n_seek.command.*;
import bagel.builds.hide_n_seek.listener.ConnectionListener;
import bagel.builds.hide_n_seek.listener.GameListener;
import bagel.builds.hide_n_seek.listener.LiveGameListener;
import bagel.builds.hide_n_seek.listener.misc.*;
import bagel.builds.hide_n_seek.manager.GameManager;
import bagel.builds.hide_n_seek.util.GameSettingsConfig;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.UnsupportedEncodingException;


public final class Main extends JavaPlugin {

    private GameManager gameManager;
    private LocationsFileManager locFileManager;
    private GameSettingsConfig gameSettingsConfig;
    private BukkitAudiences adventure;

    public @NonNull BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.adventure = BukkitAudiences.create(this);
        new CarryHandler(this);
        new IronTDHandler(this);
        new FallingBlockHandler(this);
        new CarryHandler(this);
        new SitHandler(this);
        new ResourcePackHandler(this);
        new GuiListener(this);
        new VentHandler(this);
        new ConnectionListener(this);

        gameManager = new GameManager(this);
        locFileManager = new LocationsFileManager(this);
        gameSettingsConfig = new GameSettingsConfig(this);
        gameSettingsConfig.saveDefaultConfig();

        getCommand("nightvision").setExecutor(new NightVision());
        getCommand("sit").setExecutor(new SitCommand());
        getCommand("speed").setExecutor(new SpeedCommand());
        getCommand("speed").setTabCompleter(new SpeedTabCompleter());
        getCommand("hat").setExecutor(new HatCommand());
        getCommand("hat").setTabCompleter(new HatTabCompleter());
        getCommand("gui").setExecutor(new GuiCommand(this));
        getCommand("vent").setExecutor(new TestCommand(this));
        getCommand("start").setExecutor(new StartCommand(this));
        getCommand("npc").setExecutor(new NPCCommand(this));
        try {
            getCommand("setting").setExecutor(new GameSettingsCommand(this));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        Bukkit.getPluginManager().registerEvents(new GameListener(this, gameManager), this);
        Bukkit.getPluginManager().registerEvents(new LiveGameListener(this), this);
        Bukkit.getPluginManager().registerEvents(new GameSettingsConfig(this), this);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }


    public GameManager getGameManager() { return gameManager; }
    public LocationsFileManager getLocFileManager() { return locFileManager; }
    public GameSettingsConfig getGameSettingsConfig() { return gameSettingsConfig; }
}
