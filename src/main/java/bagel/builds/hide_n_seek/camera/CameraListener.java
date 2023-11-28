package bagel.builds.hide_n_seek.camera;

import bagel.builds.hide_n_seek.Main;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class CameraListener implements Listener {

    private ItemStack camera;
    private CameraManager cameraManager;
    private Main main;
    private PacketHandler packetHandler;

    public CameraListener(Main main, CameraManager cameraManager) {
        this.cameraManager = cameraManager;
        this.camera = cameraManager.getCameraItem();
        this.main = main;
        this.packetHandler = cameraManager.getPacketHandler();
    }

    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if(e.getItemInHand().hasItemMeta() && e.getItemInHand().getItemMeta().hasLocalizedName() && e.getItemInHand().getItemMeta().getLocalizedName().equals("cameracreator") && e.getItemInHand().getItemMeta().getPersistentDataContainer().has(cameraManager.getKey(), PersistentDataType.BOOLEAN) && e.getItemInHand().getItemMeta().getPersistentDataContainer().get(cameraManager.getKey(), PersistentDataType.BOOLEAN)) {
                e.setCancelled(true);

                BlockFace face = e.getBlockAgainst().getFace(e.getBlockPlaced());
                Block block = e.getBlockPlaced();
                Location tempLoc = block.getLocation();
                Location loc = new Location(tempLoc.getWorld(), tempLoc.getBlockX(), tempLoc.getBlockY(), tempLoc.getBlockZ()).add(0.5, 0, 0.5);
                switch(face) {
                    case NORTH:
                        loc.setYaw(180f);
                        break;
                    case EAST:
                        loc.setYaw(-90f);
                        break;
                    case WEST:
                        loc.setYaw(90f);
                        break;
                    default:
                        break;

                }

                CameraClass camera = new CameraClass(main, cameraManager, loc);
                cameraManager.addCamera(camera);
        }
    }

    @EventHandler
    public void test(PlayerInteractAtEntityEvent e) {
        if(e.getPlayer().isSneaking() && cameraManager.isCamera(e.getRightClicked())) {
            cameraManager.removeCamera(cameraManager.getCameraClass(e.getRightClicked()));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        packetHandler.inject(e.getPlayer(), main);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        packetHandler.stop(e.getPlayer());
    }

}
