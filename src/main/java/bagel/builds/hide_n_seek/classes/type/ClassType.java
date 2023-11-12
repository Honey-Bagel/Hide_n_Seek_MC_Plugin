package bagel.builds.hide_n_seek.classes.type;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.Animatronic;
import bagel.builds.hide_n_seek.classes.Hider;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.UUID;

public abstract class ClassType implements Listener {
    private Main main;

    protected Animatronic animatronic;
    protected Hider hider;
    protected UUID uuid;

    public ClassType(Main main, Animatronic animatronic, UUID uuid) {
        this.animatronic = animatronic;
        this.uuid = uuid;
        this.main = main;

        Bukkit.getPluginManager().registerEvents(this, main);
    }

    public ClassType(Main main, Hider hider, UUID uuid) {
        this.hider = hider;
        this.uuid = uuid;

        Bukkit.getPluginManager().registerEvents(this, main);
    }

    public UUID getUuid() { return uuid; }
    public Animatronic getAnimatronic() { return animatronic; }
    public Hider getHider() { return hider; }

    public abstract void start(int addCooldown);

    public abstract void reset();
    public void remove() {
        HandlerList.unregisterAll(this);
    }

    //Need to handle player leaves/server stops

}
