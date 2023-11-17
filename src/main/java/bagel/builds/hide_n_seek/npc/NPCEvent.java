package bagel.builds.hide_n_seek.npc;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NPCEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final int entityID;

    public NPCEvent(Player player, int entityID) {
        this.player = player;
        this.entityID = entityID;
    }

    public Player getPlayer() { return player; }
    public int getEntityID() { return entityID; }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
