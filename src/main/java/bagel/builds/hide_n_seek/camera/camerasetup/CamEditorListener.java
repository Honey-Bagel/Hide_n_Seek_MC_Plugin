package bagel.builds.hide_n_seek.camera.camerasetup;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class CamEditorListener implements Listener {

    private CameraEditor cameraEditor;
    private Player player;

    public CamEditorListener(CameraEditor cameraEditor, Player player) {
        this.cameraEditor = cameraEditor;
        this.player = player;
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent e) {
        if(e.getPlayer().equals(player) && (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            if(e.getItem().getItemMeta().getLocalizedName().equalsIgnoreCase(EditorItem.EXIT.getName())) {
                cameraEditor.exitEditorMode();
            } else if(e.getItem().getItemMeta().getLocalizedName().equalsIgnoreCase(EditorItem.ROTATE_UP.getName())) {

            } else if(e.getItem().getItemMeta().getLocalizedName().equalsIgnoreCase(EditorItem.ROTATE_DOWN.getName())) {

            } else if(e.getItem().getItemMeta().getLocalizedName().equalsIgnoreCase(EditorItem.ROTATE_RIGHT.getName())) {

            } else if(e.getItem().getItemMeta().getLocalizedName().equalsIgnoreCase(EditorItem.ROTATE_LEFT.getName())) {

            }
        }
    }

}
