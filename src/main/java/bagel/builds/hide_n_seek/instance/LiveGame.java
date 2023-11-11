package bagel.builds.hide_n_seek.instance;

import bagel.builds.hide_n_seek.classes.Animatronic;
import bagel.builds.hide_n_seek.classes.Hider;
import bagel.builds.hide_n_seek.classes.Team;
import bagel.builds.hide_n_seek.enums.GameState;
import bagel.builds.hide_n_seek.manager.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LiveGame {
    private GameManager gameManager;
    private HashMap<UUID, Team> teams;
    private HashMap<UUID, Animatronic> animatronics;
    private HashMap<UUID, Hider> aliveHiders;
    private List<BukkitTask> tasks;

    private int gameTime;
    private int hideTime;

    public LiveGame(GameManager gameManager) {
        this.gameManager = gameManager;
        teams = gameManager.getTeams();
        animatronics = gameManager.getAnimatronicsMap();
        aliveHiders = gameManager.getHiderMap();
        tasks = new ArrayList<>();

        //times = config.gettime or smth
    }

    public void start() {
        gameManager.setState(GameState.HIDING);
        teleportStart();


        tasks.add(Bukkit.getScheduler().runTaskLater(gameManager.getMain(), () -> {
            gameManager.sendGameTitle(ChatColor.LIGHT_PURPLE + "GAME OVER : Hiders have won!", ChatColor.GRAY + "The hiders survived the required time");
            tasks.add(Bukkit.getScheduler().runTaskLater(gameManager.getMain(), () -> {
                gameManager.reset();
            }, 20 * 5));
        }, 20 * 60 * gameTime));
    }

    public void teleportStart() {
        //teleport hiders to start location and let them hide, hold seekers for a couple minutes
            for(UUID uuid : teams.keySet()) {
                if(teams.get(uuid).equals(Team.ANIMATRONIC)) {
                    Bukkit.getPlayer(uuid);
                } else if(teams.get(uuid).equals(Team.HIDER)) {
                    Bukkit.getPlayer(uuid);
                }
            }
    }

    public void hiderDeath(Player player) {
        if(teams.get(player.getUniqueId()).equals(Team.HIDER)) {
            aliveHiders.remove(player.getUniqueId());
            player.setGameMode(GameMode.SPECTATOR);
            for(UUID uuid : animatronics.keySet()) {
                player.hidePlayer(gameManager.getMain(), Bukkit.getPlayer(uuid));
            }
            gameManager.sendHiderTitle(ChatColor.DARK_RED + "One of your friends has died...", ChatColor.GRAY + "There are " + aliveHiders.size() + " left.");
            gameManager.sendAnimTitle(ChatColor.DARK_RED + "A hider has been slain", ChatColor.GRAY + "" + aliveHiders.size() + " hiders remain");
            if(aliveHiders.size() == 0) {
                gameManager.sendGameTitle(ChatColor.LIGHT_PURPLE + "GAME OVER : Animatronics have won!", ChatColor.GRAY + "All hiders have died");
                gameManager.reset();
            }
        }
    }

    public void animatronicRespawn(Player player) {
        if(teams.get(player.getUniqueId()).equals(Team.ANIMATRONIC)) {
            player.setGameMode(GameMode.SPECTATOR);
            tasks.add(Bukkit.getScheduler().runTaskLater(gameManager.getMain(), () -> {
                //respawn animatronic
            }, 20 * 30 /* 30 seconds */));
        }
    }


    public void cancelTasks() {
        for(BukkitTask task : tasks) {
            task.cancel();
        }
    }


}
