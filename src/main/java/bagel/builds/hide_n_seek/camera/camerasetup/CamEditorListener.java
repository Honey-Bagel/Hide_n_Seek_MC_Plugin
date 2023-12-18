package bagel.builds.hide_n_seek.camera.camerasetup;

import bagel.builds.hide_n_seek.camera.CameraManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CamEditorListener implements Listener {

    private CameraManager cameraManager;
    private CameraEditor cameraEditor;
    private Player player;

    public CamEditorListener(CameraManager cameraManager, CameraEditor cameraEditor, Player player) {
        this.cameraManager = cameraManager;
        this.cameraEditor = cameraEditor;
        this.player = player;
    }

    public void remove() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent e) {
        if(e.getPlayer().equals(player) && (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && e.getItem() != null) {
            if(e.getItem().getItemMeta().getLocalizedName().equalsIgnoreCase(EditorItem.EXIT.getName())) {
                System.out.println("event test");
                cameraEditor.exitEditorMode();
                remove();
            } else if(e.getItem().getItemMeta().getLocalizedName().equalsIgnoreCase(EditorItem.ROTATE_UP.getName())) {
                if(!e.getPlayer().isSneaking()) {
                    cameraEditor.moveCamera(1, 0);
                } else {
                    cameraEditor.moveCamera(5, 0);
                }
            } else if(e.getItem().getItemMeta().getLocalizedName().equalsIgnoreCase(EditorItem.ROTATE_DOWN.getName())) {
                if(!e.getPlayer().isSneaking()) {
                    cameraEditor.moveCamera(-1, 0);
                } else {
                    cameraEditor.moveCamera(-5, 0);
                }
            } else if(e.getItem().getItemMeta().getLocalizedName().equalsIgnoreCase(EditorItem.ROTATE_RIGHT.getName())) {
                if(!e.getPlayer().isSneaking()) {
                    cameraEditor.moveCamera(0, 1);
                } else {
                    cameraEditor.moveCamera(0, 5);
                }
            } else if(e.getItem().getItemMeta().getLocalizedName().equalsIgnoreCase(EditorItem.ROTATE_LEFT.getName())) {
                if(!e.getPlayer().isSneaking()) {
                    cameraEditor.moveCamera(0, -1);
                } else {
                    cameraEditor.moveCamera(0, -5);
                }
            } else if(e.getItem().getItemMeta().getLocalizedName().equalsIgnoreCase(EditorItem.SELECT.getName())) {
                cameraEditor.setEditTarget();
                System.out.println("Selection:" + cameraEditor.getCurrentSelection());
            }
        }
    }

    public Entity getClosestEntity() {

        List<Entity> nearbycams = cameraManager.getPlayerCamManager(player).getNearbyCamerasAsEntity();
        System.out.println(nearbycams.toString());

        Entity closestCam = cameraEditor.getCurrentSelection();

        for (Entity ent : nearbycams) {
            if (player.hasLineOfSight(ent)) {
                System.out.println("LOS: " + ent);
                if (closestCam == null) {
                    closestCam = ent;
                    System.out.println("closestCam == null");
                } else if (player.getLocation().distance(ent.getLocation()) < player.getLocation().distance(closestCam.getLocation())) {
                    closestCam = ent;
                    System.out.println("closestCam distnace" + player.getLocation().distance(ent.getLocation()));
                }
            }
        }

        return closestCam;
    }

    public void checkLOS() {

        Entity closestCam = getClosestEntity();

        if (closestCam != null) {
            cameraEditor.setCurrentSelection(cameraManager.getCameraClass(closestCam).getEntity());
            cameraEditor.getCurrentSelection().setGlowing(true);
        }

    }

    public boolean checkItem(ItemStack item) {
        if(item != null && item.hasItemMeta() && item.getItemMeta().hasLocalizedName() && item.getItemMeta().getLocalizedName().equalsIgnoreCase(EditorItem.SELECT.getName())) {
            return true;
        } else {
            return false;
        }
    }

    @EventHandler
    public void onItemSwitch(PlayerItemHeldEvent e) {
        if(e.getPlayer().equals(player)) {
            int loc = e.getNewSlot();
            ItemStack item = e.getPlayer().getInventory().getItem(loc);
            if(checkItem(item)) {

                checkLOS();

            } else if (cameraEditor.getCurrentSelection() != null) {
                cameraEditor.getCurrentSelection().setGlowing(false);
            }
        }
    }

    // CHECK IF PLAYER IS ACTUALLY FACING THE FUCKING CAMERA CAUSE APPARENTLY THAT DOESNT WORK YET

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if(checkItem(e.getPlayer().getInventory().getItemInMainHand()) && e.getPlayer().equals(player)) {
            if(player.hasLineOfSight(cameraEditor.getCurrentSelection())) {
                checkLOS();
            } else {
                cameraEditor.getCurrentSelection().setGlowing(false);
            }

        }
    }

    @EventHandler
    public void temp(InventoryInteractEvent e) {
        if(e.getWhoClicked().equals(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if(e.getPlayer().equals(player)) {
            e.setCancelled(true);
        }
    }

}
