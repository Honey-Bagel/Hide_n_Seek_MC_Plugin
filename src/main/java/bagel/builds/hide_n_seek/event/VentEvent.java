package bagel.builds.hide_n_seek.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VentEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    //private gameClass class;
    private final Boolean inVent;

    public VentEvent(Player player, Boolean inVent) {
        this.player = player;
        this.inVent = inVent;
        //this.class = class;
    }

    public Player getPlayer() { return player; }
    public Boolean getInVent() { return inVent; }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
