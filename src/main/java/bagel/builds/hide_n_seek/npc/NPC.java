package bagel.builds.hide_n_seek.npc;

import bagel.builds.hide_n_seek.classes.Animatronic;
import bagel.builds.hide_n_seek.classes.Hider;
import bagel.builds.hide_n_seek.classes.Team;

public class NPC {

    private String name;
    private int id;
    private Team team;
    private Animatronic animatronic = null;
    private Hider hider = null;

    public NPC(String name, int id, Animatronic animatronic) {
        this.name = name;
        this.id = id;
        this.team = Team.ANIMATRONIC;
        this.animatronic = animatronic;
    }
    public NPC(String name, int id, Hider hider) {
        this.name = name;
        this.id = id;
        this.team = Team.HIDER;
        this.hider = null;
    }

    public String getName() { return name; }
    public int getId() { return id; }
    public Team getTeam() { return team; }
    public Animatronic getAnimatronic() { return animatronic; }
    public Hider getHider() { return hider; }

}
