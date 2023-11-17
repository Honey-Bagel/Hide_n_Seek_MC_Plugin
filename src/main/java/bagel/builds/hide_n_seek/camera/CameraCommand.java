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

        player.getInventory().addItem(cameraManager.getCameraItem());

        return false;
    }
}
