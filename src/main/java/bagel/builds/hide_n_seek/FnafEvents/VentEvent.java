package bagel.builds.hide_n_seek.FnafEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VentEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;

    public VentEvent(Player player) {
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }



}
