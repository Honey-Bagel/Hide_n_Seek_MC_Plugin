package bagel.builds.hide_n_seek.classes.type.classutil;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.type.MarionetteClass;
import bagel.builds.hide_n_seek.classes.type.classrunnable.MProgressDown;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MarionetteTask implements Listener {

    private Main main;
    private MarionetteClass mClass;
    private MProgressDown progressTask;

    private HashMap<UUID, Long> playersWinding;
    private List<Location> locations;
    private double progress;
    private Player player;
    private BossBar animatronicBar;
    private BossBar hiderBar;
    private BukkitTask bTask;


    public MarionetteTask(Main main, Player player, MarionetteClass mClass) {
        this.main = main;
        this.locations = mClass.getLocations();
        this.playersWinding = new HashMap<>();
        this.progress = 20;
        this.player = player;
        this.mClass = mClass;

        Bukkit.getPluginManager().registerEvents(this, main);

        this.progressTask = new MProgressDown(main, this, player);
        progressTask.start();
        mClass.setActiveTask(true);

        barsStart();
        start();
    }

    public void start() {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.registerNewTeam("MusicBoxes");
        team.setColor(ChatColor.YELLOW);
        for(Location loc : locations) {
            Entity ent = Bukkit.getWorld(loc.getWorld().getName()).spawnEntity(loc, EntityType.SHULKER);
            team.addEntry(ent.getUniqueId().toString());
            ((LivingEntity) ent).setAI(false);
            ((LivingEntity) ent).setInvisible(true);
            ent.setInvulnerable(true);
            ent.setGravity(false);
            ent.setSilent(true);
            ent.setGlowing(true);
            Bukkit.getScheduler().runTaskLater(main, ent::remove, 60);
        }
    }

    public void barsStart() {
        animatronicBar = Bukkit.createBossBar(ChatColor.BLUE + "Stop the hiders from winding the music box. [" + ChatColor.WHITE + progress + "%" + ChatColor.BLUE + "]", BarColor.BLUE, BarStyle.SEGMENTED_20);
        hiderBar = Bukkit.createBossBar(ChatColor.BLUE + "Wind the music boxes before time runs out! [" + ChatColor.WHITE + progress + "%" + ChatColor.BLUE + "]", BarColor.BLUE, BarStyle.SEGMENTED_20);
        hiderBar.setProgress(0.2);
        animatronicBar.setProgress(0.2);


        bTask = Bukkit.getScheduler().runTaskTimer(main, () -> {
            for(UUID uuid : main.getGameManager().getHiderMap().keySet()) {
                hiderBar.addPlayer(Bukkit.getPlayer(uuid));
            }
            for(UUID uuid : main.getGameManager().getAnimatronicsMap().keySet()) {
                animatronicBar.addPlayer(Bukkit.getPlayer(uuid));
            }
        }, 0, 20);
    }


    public void end(boolean completed) {
        progressTask.cancel();
        HandlerList.unregisterAll(this);
        hiderBar.removeAll();
        bTask.cancel();
        animatronicBar.removeAll();
        mClass.setActiveTask(false);
        if(completed) {

            main.getGameManager().sendHiderTitle(ChatColor.GREEN + "Successfully wound music box!", ChatColor.GRAY + "successfully avoided a punishment");
            main.getGameManager().sendAnimTitle(ChatColor.RED + "Failed to stop the hiders.", ChatColor.GRAY + "Marionette's ability failed, the music boxes were not protected.");
        } else {

            main.getGameManager().sendHiderTitle(ChatColor.RED + "Failed to wind the music box.", ChatColor.GRAY + "You failed to wind the music boxes, you will be punished...");
            main.getGameManager().sendAnimTitle(ChatColor.GREEN + "Successfully stopped the hiders!", ChatColor.GRAY + "Marionette's ability succeeded, punishing the hiders...");
        }
    }


    @EventHandler
    public void onMusicBoxWind(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getHand().equals(EquipmentSlot.HAND) && e.getClickedBlock().getType().equals(Material.JUKEBOX)) { //&& main.getGameManager.getHiderMap.containsKey(player.getUniqueId()
            if (isMarionetteBox(e.getClickedBlock().getLocation())) {
                long curTime = System.currentTimeMillis();
                if (!playersWinding.containsKey(player.getUniqueId())) {
                    playersWinding.put(player.getUniqueId(), curTime);
                }
                long lastClick = playersWinding.get(player.getUniqueId());
                if (curTime - lastClick <= 252) {
                    playersWinding.put(player.getUniqueId(), curTime);
                    progress += 0.5;
                    setBarProgress(progress);
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

    public void setBarProgress(double progress) {
        if(progress%5 == 0) {
            hiderBar.setProgress(progress/100);
            animatronicBar.setProgress(progress/100);
        }
        if(progress <= 20) {
            if(!hiderBar.getColor().equals(BarColor.RED)) {
                hiderBar.setColor(BarColor.RED);
            }
            hiderBar.setTitle(ChatColor.RED + "Wind the music boxes before time runs out! [" + ChatColor.WHITE + progress + "%" + ChatColor.RED + "]");
        } else if(progress > 20) {
            if (!hiderBar.getColor().equals(BarColor.BLUE)) {
                hiderBar.setColor(BarColor.BLUE);
            }
            hiderBar.setTitle(ChatColor.BLUE + "Wind the music boxes before time runs out! [" + ChatColor.WHITE + progress + "%" + ChatColor.BLUE + "]");
        }
        if(progress >= 80) {
            if(!animatronicBar.getColor().equals(BarColor.RED)) {
                animatronicBar.setColor(BarColor.RED);
            }
            animatronicBar.setTitle(ChatColor.RED + "Stop the hiders from winding the music box. [" + ChatColor.WHITE + progress + "%" + ChatColor.RED + "]");
        } else if(progress < 80) {
            if(!animatronicBar.getColor().equals(BarColor.BLUE)) {
                animatronicBar.setColor(BarColor.BLUE);
            }
            animatronicBar.setTitle(ChatColor.BLUE + "Stop the hiders from winding the music box. [" + ChatColor.WHITE + progress + "%" + ChatColor.BLUE + "]");
        }
    }

}
