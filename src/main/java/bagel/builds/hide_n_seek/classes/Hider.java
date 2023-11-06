package bagel.builds.hide_n_seek.classes;

import org.bukkit.ChatColor;

public enum Hider {

    CRYING_CHILD(ChatColor.GRAY + "Crying Child", "", 18),
    ELIZABETH("Elizabeth", "", 19),
    MICAHEL_AFTON("Michael A.", "", 20),
    MIKE_SCHMIDT("Mike S.", "", 21),
    JEREMY_FITZGERALD("Jeremy F.", "", 22),
    OLDER_BROTHER("Older Brother", "", 23);

    private final String name;
    private final String description;
    private final int guiLoc;

    Hider(String name, String description, int guiLoc) {
        this.name = name;
        this.description = description;
        this.guiLoc = guiLoc;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getGuiLoc() { return guiLoc; }

}
