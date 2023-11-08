package bagel.builds.hide_n_seek.classes.type.classutil;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.type.MarionetteClass;
import bagel.builds.hide_n_seek.classes.type.classrunnable.MProgressDown;
import bagel.builds.hide_n_seek.manager.GameManager;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MarionetteTask implements Listener {

    private Main main;
    private MarionetteClass mClass;
    private HashMap<UUID, Long> playersWinding;
    private List<Player> hiders;
    private List<Location> locations;
    private static final double startPercent = 20;
    private double progress;
    private MProgressDown progressTask;
    Player player;


    public MarionetteTask(Main main, List<Player> hiders, List<Location> locations, Player player, MarionetteClass mClass) {
        this.main = main;
        this.hiders = hiders;
        this.locations = locations;
        this.playersWinding = new HashMap<>();
        this.progress = startPercent;
        this.player = player;
        this.mClass = mClass;

        this.progressTask = new MProgressDown(main, this, player);
        progressTask.start();

        Bukkit.getPluginManager().registerEvents(this, main);
    }


    public void end(boolean completed) {
        progressTask.cancel();
        HandlerList.unregisterAll(this);
        if(completed) {
            main.getGameManager().sendAnimTitle(ChatColor.GREEN + "Hiders successfully wound the music box", "");
            main.getGameManager().sendAnimTitle(ChatColor.RED + "Hiders successfully wound the music box", "");
        } else {
            main.getGameManager().sendAnimTitle(ChatColor.RED + "Hiders were unable to wind the music box", "");
            main.getGameManager().sendAnimTitle(ChatColor.GREEN + "Hiders were unable to wind the music box", "");
        }
    }


    @EventHandler
    public void onMusicBoxWind(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getHand().equals(EquipmentSlot.HAND) && e.getClickedBlock().getType().equals(Material.JUKEBOX)) {
            if (isMarionetteBox(e.getClickedBlock().getLocation())) {
                long curTime = System.currentTimeMillis();
                if (!playersWinding.containsKey(player.getUniqueId())) {
                    playersWinding.put(player.getUniqueId(), curTime);
                }
                long lastClick = playersWinding.get(player.getUniqueId());
                if (curTime - lastClick <= 252) {
                    playersWinding.put(player.getUniqueId(), curTime);
                    progress += 2;
                    Math.round(progress);
                    player.sendMessage(ChatColor.GREEN + "Increase: " + progress);
                    if(progress >= 100) {
                        end(true);
                    }

                    Bukkit.getScheduler().runTaskLater(main, () -> {
                        playersWinding.remove(player.getUniqueId());
                    }, 4);

                } else {
                    playersWinding.remove(player.getUniqueId());
                }
            }
        }
    }





    public boolean isMarionetteBox(Location loc) {
        for(Location l : locations) {
            if(l.equals(loc)) {
                return true;
            }
        }
        return false;
    }

    public double decrementProgress() {
        progress--;
        return progress;
    }

    public boolean isWinding() {
        if(playersWinding.containsKey(player.getUniqueId())) {
            return true;
        } else {
            return false;
        }
    }


}
