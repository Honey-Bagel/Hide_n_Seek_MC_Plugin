package bagel.builds.hide_n_seek.classes.type.classrunnable;

import bagel.builds.hide_n_seek.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class BonnieGlow extends BukkitRunnable {
    private List<Entity> nearby;
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
            for (Entity ent : nearby) {
                ent.setGlowing(false);
            }
        }
        if(seconds == 0) {
            cancel();
            return;
        }

        nearby = player.getNearbyEntities(5,5,5);
        for(Entity ent : nearby) {
            ent.setGlowing(true);
        }
//        player.sendMessage(ChatColor.DARK_BLUE + "You revealed " + ChatColor.WHITE + nearby.size() + ChatColor.DARK_BLUE + " players!");
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§1You revealed " + "§f" +nearby.size() + "§1 players!"));
        seconds--;
    }

    @Override
    public void cancel() {
        for(Entity ent : nearby) {
            ent.setGlowing(false);
        }
    }
}
