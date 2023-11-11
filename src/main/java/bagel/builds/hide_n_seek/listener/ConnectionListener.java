package bagel.builds.hide_n_seek.listener;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.Team;
import bagel.builds.hide_n_seek.manager.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {
    private Main main;
    private GameManager gameManager;

    public ConnectionListener(Main main) {
        this.main = main;
        this.gameManager = main.getGameManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        //teleport player to spawn

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        gameManager.removePlayer(player);
//        if(gameManager.getTeams().containsKey(player.getUniqueId())) {
//            if (gameManager.getTeams().get(player.getUniqueId()).equals(Team.ANIMATRONIC)) {
//                gameManager.removeAnimatronic(player);
//            } else if (gameManager.getTeams().get(player.getUniqueId()).equals(Team.HIDER)) {
//                gameManager.removeHider(player);
//            }
//        }
    }

}
