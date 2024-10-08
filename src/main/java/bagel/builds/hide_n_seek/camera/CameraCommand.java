package bagel.builds.hide_n_seek.camera;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CameraCommand implements CommandExecutor {

    private CameraManager cameraManager;
    public CameraCommand(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if(args.length == 1 && args[0].equalsIgnoreCase("get")) {
            player.getInventory().addItem(cameraManager.getCameraItem());
        } else if(args[0].equalsIgnoreCase("open")) {
            if(args.length == 2) {
                CameraClass camClass = cameraManager.getPlayerCamManager(player).getNearbyCameras().get(Integer.parseInt(args[1]) - 1);
                cameraManager.getPlayerCamManager(player).setCamera(camClass);
            } else if(args.length == 1) {
                new CameraGUI(cameraManager, player, cameraManager.getPlayerCamManager(player).getNearbyCameras());
            }
        } else if(args.length == 1 && args[0].equalsIgnoreCase("next")) {
            cameraManager.getPlayerCamManager(player).nextCam();
        } else if(args.length == 1 && args[0].equalsIgnoreCase("prev")) {
            cameraManager.getPlayerCamManager(player).prevCam();
        }

        return false;
    }
}
