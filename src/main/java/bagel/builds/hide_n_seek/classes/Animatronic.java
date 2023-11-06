package bagel.builds.hide_n_seek.classes;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum Animatronic {

    FREDDY(ChatColor.GOLD + "Freddy", "Randomized teleport at will.", Material.ORANGE_DYE, 0, true),
    FOXY(ChatColor.DARK_RED + "Foxy", "Extremely fast.", Material.RED_DYE, 1, true),
    BONNIE(ChatColor.DARK_PURPLE + "Bonnie", "Plays guitar to reveal hider locations within small radius.", Material.PURPLE_DYE, 2, true),
    CHICA(ChatColor.YELLOW + "Chica", "Place cupcake traps (Faint particles reveal traps).", Material.YELLOW_DYE, 3, true),
    MANGLE(ChatColor.LIGHT_PURPLE + "Mangle", "Faster crawling through vents (So cute).", Material.PINK_DYE, 4, true),
    GOLDEN_FREDDY(ChatColor.GOLD + "Golden Freddy", "Throwable head for large amounts of damage, small view radius.", Material.GRAY_DYE, 5, false),
    MARIONETTE(ChatColor.BLUE + "Marionette", "Passive: creates tasks every 45 sec, if left incomplete hiders are affected.", Material.BLUE_DYE, 6, false);

    private final String name;
    private final String description;
    private final Boolean defaultUnlock;
    private final Material material;
    private final int guiLoc;

    Animatronic(String name, String description, Material material, int guiLoc, Boolean defaultUnlock) {
        this.name = name;
        this.description = description;
        this.material = material;
        this.defaultUnlock = defaultUnlock;
        this.guiLoc = guiLoc;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Material getMaterial() { return material; }
    public int getGuiLoc() { return  guiLoc; }
    public Boolean getDefaultUnlock() { return defaultUnlock; }

}
