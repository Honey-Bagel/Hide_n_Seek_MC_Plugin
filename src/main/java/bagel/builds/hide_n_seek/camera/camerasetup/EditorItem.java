package bagel.builds.hide_n_seek.camera.camerasetup;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum EditorItem {

    ROTATE_LEFT(Material.RED_DYE, ChatColor.WHITE + "Rotate Left", ChatColor.GRAY + "Rotates the camera direction to the left", 2),
    ROTATE_RIGHT(Material.GREEN_DYE, ChatColor.WHITE + "Rotate Right", ChatColor.GRAY + "Rotates the camera direction to the right", 3),
    ROTATE_UP(Material.BLUE_DYE, ChatColor.WHITE + "Rotate Up", ChatColor.GRAY + "Rotates the camera direction up", 5),
    ROTATE_DOWN(Material.LIGHT_BLUE_DYE , ChatColor.WHITE + "Rotate Down", ChatColor.GRAY + "Rotates the camera direction down", 4),
    EXIT(Material.BARRIER, ChatColor.RED + "Exit Editor", ChatColor.GRAY + "Exits the camera editor mode", 8);

    private Material material;
    private String name;
    private String description;
    private int slot;

    EditorItem(Material material, String name, String description, int slot) {
        this.material = material;
        this.name = name;
        this.description = description;
        this.slot = slot;
    }

    public Material getMaterial() { return material; }

    public int getSlot() {
        return slot;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
