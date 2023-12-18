package bagel.builds.hide_n_seek.camera.camerasetup;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.camera.CameraManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EditCameraCommand implements CommandExecutor {

    private Main main;
    private CameraManager cameraManager;
    public EditCameraCommand(Main main, CameraManager cameraManager) {
        this.cameraManager = cameraManager;
        this.main = main;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if(args.length == 0) {
            cameraManager.addCamEditor(player);
        } else if(args.length == 1 && args[0].equalsIgnoreCase("exit")) {
            cameraManager.getCameraEditor(player).exitEditorMode();
        }

        return false;
    }
}
