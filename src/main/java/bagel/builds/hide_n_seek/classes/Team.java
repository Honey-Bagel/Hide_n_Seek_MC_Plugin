package bagel.builds.hide_n_seek.classes;

public enum Team {

    HIDER("Hider"),
    ANIMATRONIC("Animatronic");

    private final String name;

    Team(String name) {
        this.name = name;
    }

    public String getName() { return name; }

}
