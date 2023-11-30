package bagel.builds.hide_n_seek.camera.camerasetup;

import bagel.builds.hide_n_seek.camera.CameraClass;
import bagel.builds.hide_n_seek.camera.CameraManager;

import net.minecraft.world.entity.player.Inventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Vector;

public class CameraEditor {

    private CameraManager cameraManager;
    private Player player;
    private Entity cameraEntity;
    private CameraClass cameraClass;
    private PlayerInventory oldInv;

    public CameraEditor(CameraManager cameraManager, Player player, Entity entity) {
        this.cameraManager = cameraManager;
        this.player = player;
        this.cameraEntity = entity;
        this.cameraClass = cameraManager.getCameraClass(entity);
        this.oldInv = player.getInventory();
//        player.getInventory().setContents(oldInv.getContents());

        enterEditorMode();
    }

    public void enterEditorMode() {
        //Replace players inventory
        addInventory();
        // Activate listeners for each item
    }

    public void exitEditorMode() {
        // Listen for "exit item"
        // On Exit replace player inventory with old inventory
    }

    public void addInventory() {
        PlayerInventory tempInv = (PlayerInventory) Bukkit.createInventory(null, 36);
        for(EditorItem i : EditorItem.values()) {
            ItemStack item = new ItemStack(i.getMaterial());
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(i.getName());
            itemMeta.setLocalizedName(i.getName());
            itemMeta.setLore(Arrays.asList(i.getDescription()));
            item.setItemMeta(itemMeta);
            tempInv.setItem(i.getSlot(), item);
        }
        player.getInventory().setContents(tempInv.getContents());
    }

    public void moveCamera(Vector vector) {

    }



}
