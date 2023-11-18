package bagel.builds.hide_n_seek.camera;

import bagel.builds.hide_n_seek.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;

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
    private ItemDisplay display;

    public CameraClass(Main main, CameraManager cameraManager, Location location) {
        this.main = main;
        this.cameraManager = cameraManager;
        this.location = location;
        this.peopleViewing = 0;
        this.broken = false;
        this.key = cameraManager.getKey();

        createEntity();
        createDisplayItem();
    }

    public void createEntity() {
        entity = Bukkit.getWorld(location.getWorld().getName()).spawnEntity(location, EntityType.ARMOR_STAND);
        ArmorStand armorStand = (ArmorStand) entity;
        armorStand.setPersistent(true);
        armorStand.setSmall(true);
        armorStand.setVisible(true);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.getEquipment().setHelmet(cameraManager.getCameraItem());
        armorStand.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
    }

    public void createDisplayItem() {
        display = (ItemDisplay) Bukkit.getWorld(location.getWorld().getName()).spawnEntity(location.add(0,0.5,0), EntityType.ITEM_DISPLAY);
        display.setItemStack(new ItemStack(Material.LEVER));
        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);
        float rotation = 0;
        switch((int) location.getYaw()) {
            case 0:
                rotation = 0f;
                break;
            case -90:
                rotation = -0.5f;
                break;
            case 90:
                rotation = 0.5f;
                break;
            case 180:
                rotation = 1f;
                break;
            default:
                break;
        }
        Transformation transformation = display.getTransformation();
        transformation.getLeftRotation().y = 0.5f;
        display.setTransformation(transformation);
    }

    public Location getLocation() { return location; }

    public UUID getUuid() {return uuid;}
    public int getPeopleViewing() { return peopleViewing; }
    public boolean isBroken() { return broken; }
    public Entity getEntity() {
        return entity; }
    public ItemDisplay getDisplay() { return display; }
}
