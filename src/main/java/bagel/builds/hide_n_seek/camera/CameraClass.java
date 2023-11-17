package bagel.builds.hide_n_seek.camera;

import bagel.builds.hide_n_seek.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class CameraClass  {

    private Main main;
    private CameraManager cameraManager;
    private Location location;
    private UUID uuid;
    private int peopleViewing;
    private boolean broken;
    private NamespacedKey key;
    private Entity entity;

    public CameraClass(Main main, CameraManager cameraManager, Location location) {
        this.main = main;
        this.cameraManager = cameraManager;
        this.location = location;
        this.peopleViewing = 0;
        this.broken = false;
        this.key = cameraManager.getKey();

        createEntity();
    }

    public void createEntity() {
        entity = Bukkit.getWorld(location.getWorld().getName()).spawnEntity(location, EntityType.ARMOR_STAND);
        ArmorStand armorStand = (ArmorStand) entity;
        armorStand.setPersistent(true);
        armorStand.setSmall(true);
        armorStand.setVisible(true);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(false);
        armorStand.getEquipment().setHelmet(cameraManager.getCameraItem());
        armorStand.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
    }

    public Location getLocation() { return location; }

    public UUID getUuid() {return uuid;}
    public int getPeopleViewing() { return peopleViewing; }
    public boolean isBroken() { return broken; }
    public Entity getEntity() { return entity; }
}
