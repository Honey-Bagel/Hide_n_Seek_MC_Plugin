package bagel.builds.hide_n_seek.camera.camerasetup;

import bagel.builds.hide_n_seek.camera.CameraManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EditCameraCommand implements CommandExecutor {

    private CameraManager cameraManager;
    public EditCameraCommand(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        List<Entity> nearbycams = cameraManager.getPlayerCamManager(player).getNearbyCamerasAsEntity();

        Entity closestCam = null;

        for(Entity e : nearbycams) {
            if(player.hasLineOfSight(e)) {
                if(closestCam == null ) {
                    closestCam = e;
                } else if(player.getLocation().distance(e.getLocation()) < player.getLocation().distance(closestCam.getLocation())) {
                    closestCam = e;
                }
            }
        }

        if(closestCam != null) {
            new CameraEditor(cameraManager, player, closestCam);
        }

        return false;
    }
}
