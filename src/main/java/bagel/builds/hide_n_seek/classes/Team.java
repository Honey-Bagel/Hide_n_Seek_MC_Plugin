package bagel.builds.hide_n_seek.classes;

import org.bukkit.ChatColor;

public enum Team {

    HIDER(ChatColor.DARK_AQUA + "Hider"),
    ANIMATRONIC(ChatColor.DARK_PURPLE + "Animatronic");

    private final String name;

    Team(String name) {
        this.name = name;
    }

    public String getName() { return name; }

}
