package bagel.builds.hide_n_seek.npc;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NPCEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final int entityID;
    private final boolean attack;

    public NPCEvent(Player player, int entityID, boolean attack) {
        this.player = player;
        this.entityID = entityID;
        this.attack = attack;
    }

    public Player getPlayer() { return player; }
    public int getEntityID() { return entityID; }
    public boolean getAttack() { return attack; }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
