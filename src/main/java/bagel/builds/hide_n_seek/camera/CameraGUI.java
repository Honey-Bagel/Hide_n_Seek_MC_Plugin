package bagel.builds.hide_n_seek.camera;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class CameraGUI {

    public CameraGUI(CameraManager cameraManager, Player player, List<Entity> cameras) {
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.BLACK + "Cameras");

        for(int i = 0; i < cameras.size(); i++) {
            ItemStack itemStack = new ItemStack(cameraManager.createCameraItem());
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(ChatColor.GRAY + "Camera (" + i + 1 + ")");
            meta.setLore(Arrays.asList(ChatColor.GRAY + "Location: " + ChatColor.UNDERLINE + cameras.get(i).getLocation()));
            meta.setLocalizedName(ChatColor.GRAY + "Camera (" + i + ")");
            itemStack.setItemMeta(meta);

            gui.addItem(itemStack);
        }
        player.openInventory(gui);
    }

}
