package bagel.builds.hide_n_seek.camera.camerasetup;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.camera.CameraClass;
import bagel.builds.hide_n_seek.camera.CameraManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class CameraEditor {

    private Main main;
    private CameraManager cameraManager;
    private Player player;
    private Entity cameraEntity;
    private CameraClass cameraClass;
    private ItemStack[] oldInv;
    private Entity currentSelection;

    public CameraEditor(Main main, CameraManager cameraManager, Player player) {
        this.main = main;
        this.cameraManager = cameraManager;
        this.player = player;
//        this.cameraEntity = entity;
//        this.cameraClass = cameraManager.getCameraClass(entity);
        this.oldInv = player.getInventory().getContents();
        this.currentSelection = null;

        Bukkit.getPluginManager().registerEvents(new CamEditorListener(cameraManager, this, player), main);
        enterEditorMode();
    }

    public void enterEditorMode() {
        //Replace players inventory
        addInventory();
        // Activate listeners for each item
    }

    public void exitEditorMode() {
        // On Exit replace player inventory with old inventory
        player.getInventory().setContents(oldInv);
    }

    public void addInventory() {
        PlayerInventory tempInv = player.getInventory();
        tempInv.clear();
        for(EditorItem i : EditorItem.values()) {
            ItemStack item = new ItemStack(i.getMaterial());
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(i.getName());
            itemMeta.setLocalizedName(i.getName());
            itemMeta.setLore(Arrays.asList(i.getDescription()));
            item.setItemMeta(itemMeta);
            tempInv.setItem(i.getSlot(), item);
        }
//        player.getInventory().setContents(tempInv.getContents());
    }

    public void setEditTarget() {
        if(currentSelection != null) {
            cameraEntity =  currentSelection;
            cameraClass = cameraManager.getCameraClass(cameraEntity);
            System.out.println("Class: " + cameraClass);
        }
    }

    public void moveCamera(float y, float x) {
        // x = yaw, y = pitch
        System.out.println("yaw: " + x + " pitch: " + y);
        cameraClass.getEntity().setRotation(cameraClass.getEntity().getLocation().getYaw() + x, cameraClass.getEntity().getLocation().getPitch() - y);
        cameraClass.getViewEntity().setRotation(cameraClass.getViewEntity().getLocation().getYaw() + x, cameraClass.getViewEntity().getLocation().getPitch() - y);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§fNew Camera pitch: " + cameraClass.getViewEntity().getLocation().getPitch() + ", yaw: " + cameraClass.getViewEntity().getLocation().getYaw()));
        Bukkit.getScheduler().runTaskLater(main, () -> {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
        }, 20 * 3);
    }

    public void setCurrentSelection(Entity ent) {
        currentSelection = ent;
    }

    public Entity getCurrentSelection() {
        return currentSelection;
    }

    public void removeCurrentSelection() {
        currentSelection = null;
    }



}
