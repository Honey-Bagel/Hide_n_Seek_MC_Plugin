package bagel.builds.hide_n_seek.classes.type.classrunnable;

import bagel.builds.hide_n_seek.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class BonnieGlow extends BukkitRunnable {
    private List<Entity> lowerNearby;
    private List<Entity> upperNearby;
    private Main main;
    private int seconds;
    private Player player;

    public BonnieGlow(Main main, Player player) {
        this.main = main;
        this.seconds = 5;
        this.player = player;
    }

    public void start() {
        runTaskTimer(main, 0, 20);
    }


    @Override
    public void run() {
        if(seconds != 5) {
            for (Entity ent : upperNearby) {
                ent.setGlowing(false);
            }
        }
        if(seconds == 0) {
            cancel();
            return;
        }

        lowerNearby = player.getNearbyEntities(10,10,10);
        upperNearby = player.getNearbyEntities(15,15,15);
        for(Entity e : lowerNearby) {
            if(upperNearby.contains(e)) {
                upperNearby.remove(e);
            }
        }

        for(Entity ent : upperNearby) {
            ent.setGlowing(true);
        }
//        player.sendMessage(ChatColor.DARK_BLUE + "You revealed " + ChatColor.WHITE + nearby.size() + ChatColor.DARK_BLUE + " players!");
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§1You revealed " + "§f" + upperNearby.size() + "§1 players!"));
        seconds--;
    }

    @Override
    public void cancel() {
        for(Entity ent : upperNearby) {
            ent.setGlowing(false);
        }
    }
}
