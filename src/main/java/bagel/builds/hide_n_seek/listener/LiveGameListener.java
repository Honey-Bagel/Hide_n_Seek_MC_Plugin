package bagel.builds.hide_n_seek.listener;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.enums.GameState;
import bagel.builds.hide_n_seek.instance.LiveGame;
import bagel.builds.hide_n_seek.manager.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class LiveGameListener implements Listener {
    private Main main;

    public LiveGameListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        GameManager gameManager = main.getGameManager();
        System.out.println("manager: " + gameManager + " state: " + gameManager.getState());
        if(gameManager != null && gameManager.getState().equals(GameState.LIVE) || gameManager.getState().equals(GameState.HIDING)) {
            System.out.println("listener");
            LiveGame liveGame = gameManager.getLiveGame();
            if(gameManager.getHiderMap().containsKey(player.getUniqueId())) {
                liveGame.hiderDeath(player);
            } else if(gameManager.getAnimatronicsMap().containsKey(player.getUniqueId())) {
                liveGame.animatronicRespawn(player);
            }
        }
    }

}
