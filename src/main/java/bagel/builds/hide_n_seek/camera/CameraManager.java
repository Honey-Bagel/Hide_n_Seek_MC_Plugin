package bagel.builds.hide_n_seek.camera;

import bagel.builds.hide_n_seek.Main;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.util.*;

public class CameraManager {

    private Main main;
    private List<CameraClass> cameras;
    private ItemStack cameraItem;
    private NamespacedKey key;
    private HashMap<UUID, PlayerCamManager> playerCameras;
    private PacketHandler packetHandler;

    public CameraManager(Main main) {
        this.main = main;
        this.cameras = new ArrayList<>();
        this.key = new NamespacedKey(main, "camera");
        this.cameraItem = createCameraItem();
        this.playerCameras = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(new CameraListener(main, this), main);
        main.getCommand("camera").setExecutor(new CameraCommand(this));

    }

    public void start() {
//        for(UUID uuid : main.getGameManager().getHiderMap().keySet()) {
//            addPlayerCamera(Bukkit.getPlayer(uuid));
//        }
        for(UUID uuid : main.getGameManager().getPlayers()) {
            System.out.println(uuid);
            addPlayerCamera(Bukkit.getPlayer(uuid));
        }
    }

    public ItemStack createCameraItem() {
        ItemStack camera = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta cMeta = (SkullMeta) camera.getItemMeta();
        cMeta.setDisplayName(ChatColor.DARK_RED + "Camera");
        cMeta.setLore(Arrays.asList(ChatColor.GRAY + "Creates a camera entity"));
        cMeta.setLocalizedName("cameracreator");
        cMeta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDlkZDM3MjIyNzgwOTFiZGNlMGI0NDZhZDIwYTNlZWE4YTdiMzNjZDI5N2ZlZjMzNzBmZTE1YTkyYWJjMWQ3YSJ9fX0="));
        Field field;
        try {
            field = cMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(cMeta,profile);
        } catch(NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
        camera.setItemMeta(cMeta);
        return camera;
    }

    public List<CameraClass> getCameras() { return cameras; }
    public void addCamera(CameraClass camera) {
        cameras.add(camera);
    }
    public void removeCamera(CameraClass camera) {
        if(cameras.contains(camera)) {
            cameras.remove(camera);
            camera.getEntity().remove();
            camera.getDisplay().remove();
            camera.getViewEntity().remove();
        }
    }

    public ItemStack getCameraItem() {
        return cameraItem;
    }
    public NamespacedKey getKey() { return key; }

    public void addPlayerCamera(Player player) {
        PlayerCamManager playerCam = new PlayerCamManager(main, this, player);
        playerCameras.put(player.getUniqueId(), playerCam);
    }
    public void removePlayerCamera(Player player) {
        if(playerCameras.containsKey(player.getUniqueId())) {
//            playerCameras.get(player.getUniqueId());
            playerCameras.remove(player.getUniqueId());
        }
    }

    public PlayerCamManager getPlayerCamManager(Player player) {
        return playerCameras.get(player.getUniqueId());
    }



    public CameraClass getCameraClass(Entity entity) {
        for(CameraClass c : cameras) {
            if(c.getEntity().equals(entity)) {
                return c;
            }
        }
        return null;
    }

    public boolean isCamera(Entity entity) {
        if(entity.getPersistentDataContainer().has(key, PersistentDataType.BOOLEAN) && entity.getPersistentDataContainer().get(key, PersistentDataType.BOOLEAN)) {
            return true;
        }
        return false;
    }

    public PlayerCamManager getCamManByEntityID(int id) {
        for(PlayerCamManager cam : playerCameras.values()) {
            if(cam.getEntity().getEntityId() == id) {
                return cam;
            }
        }
        return null;
    }

    public PacketHandler getPacketHandler() { return packetHandler; }


}
