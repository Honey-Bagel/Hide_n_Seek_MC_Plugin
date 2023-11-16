package bagel.builds.hide_n_seek.classes.type;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.Animatronic;
import bagel.builds.hide_n_seek.classes.Hider;
import bagel.builds.hide_n_seek.classes.type.classutil.SkinUtil;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.UUID;

public abstract class ClassType implements Listener {
    private Main main;

    protected Animatronic animatronic;
    protected Hider hider;
    protected UUID uuid;
    protected SkinUtil skinUtil;
    protected Property property;

    public ClassType(Main main, Animatronic animatronic, UUID uuid) {
        this.animatronic = animatronic;
        this.uuid = uuid;
        this.main = main;
        this.skinUtil = new SkinUtil(main, Bukkit.getPlayer(uuid));
        this.property = ((CraftPlayer) Bukkit.getPlayer(uuid)).getProfile().getProperties().get("textures").iterator().next();

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
        skinUtil.resetSkin(property);
    }

    //Need to handle player leaves/server stops

}
