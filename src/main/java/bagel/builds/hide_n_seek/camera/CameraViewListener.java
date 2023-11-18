package bagel.builds.hide_n_seek.camera;

import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class CameraViewListener implements Listener {

    private CameraManager cameraManager;
    public CameraViewListener(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if(cameraManager.getIsInCameras().containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        if(cameraManager.getIsInCameras().containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
//            cameraManager.exitCamera(e.getPlayer());
            removeHandlers();
        }
    }

    public void removeHandlers() {
        HandlerList.unregisterAll(this);
    }

}
