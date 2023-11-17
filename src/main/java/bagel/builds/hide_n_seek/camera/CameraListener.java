package bagel.builds.hide_n_seek.camera;

import bagel.builds.hide_n_seek.Main;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class CameraListener implements Listener {

    private ItemStack camera;
    private CameraManager cameraManager;
    private Main main;

    public CameraListener(Main main, CameraManager cameraManager) {
        this.cameraManager = cameraManager;
        this.camera = cameraManager.getCameraItem();
        this.main = main;
    }

    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if(e.getPlayer().getItemInUse().getItemMeta().hasLocalizedName() && e.getPlayer().getItemInUse().getItemMeta().getLocalizedName().equals("cameracreator") && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(cameraManager.getKey(), PersistentDataType.BOOLEAN) && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(cameraManager.getKey(), PersistentDataType.BOOLEAN)) {
                e.setCancelled(true);
                Location tempLoc = e.getBlockPlaced().getLocation();
                if(e.getBlockPlaced().getFace(e.getBlock()).equals(BlockFace.EAST) || e.getBlockPlaced().getFace(e.getBlock()).equals(BlockFace.WEST)) {
                    tempLoc.add(0,0,0.5);
                } else if(e.getBlockPlaced().getFace(e.getBlock()).equals(BlockFace.SOUTH) || e.getBlockPlaced().getFace(e.getBlock()).equals(BlockFace.NORTH)) {
                    tempLoc.add(0.5, 0, 0);
                }
                CameraClass camera = new CameraClass(main, cameraManager, tempLoc);
                cameraManager.addCamera(camera);
        }
    }

    @EventHandler
    public void test(PlayerInteractAtEntityEvent e) {
        if(e.getRightClicked().getType().equals(EntityType.ARMOR_STAND) && Boolean.TRUE.equals(e.getRightClicked().getPersistentDataContainer().get(cameraManager.getKey(), PersistentDataType.BOOLEAN))) {
            cameraManager.removeCamera(cameraManager.getCameraClass(e.getRightClicked()));
        }
    }

}
