package bagel.builds.hide_n_seek;

import bagel.builds.hide_n_seek.commands.*;
import bagel.builds.hide_n_seek.handlers.FnafGame.Features.VentHandler;
import bagel.builds.hide_n_seek.handlers.misc.*;
import org.bukkit.plugin.java.JavaPlugin;



public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        new IronTDHandler(this);
        new FallingBlockHandler(this);
        new CarryHandler(this);
        new SitHandler(this);
        new ResourcePackHandler(this);
        new GuiListener(this);
        new VentHandler(this);

        getCommand("nightvision").setExecutor(new NightVision());
        getCommand("sit").setExecutor(new SitCommand());
        getCommand("speed").setExecutor(new SpeedCommand());
        getCommand("speed").setTabCompleter(new SpeedTabCompleter());
        getCommand("hat").setExecutor(new HatCommand());
        getCommand("hat").setTabCompleter(new HatTabCompleter());
        getCommand("gui").setExecutor(new GuiCommand());

    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
