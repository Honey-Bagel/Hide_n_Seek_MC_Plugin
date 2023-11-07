package bagel.builds.hide_n_seek.classes.type.classutil;

import bagel.builds.hide_n_seek.manager.GameManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class MarionetteTask implements Listener {

    private GameManager gameManager;
    private List<Player> hiders;
    private List<Block> musicboxes;
    private List<Location> locations;
    private static final double startPercent = 0.2;
    private static final double percentDownperSec = 0.033; //every 3 seconds down 1 percent (0.01)

    public MarionetteTask(GameManager gameManager, List<Player> hiders, List<Location> locations) {
        this.gameManager = gameManager;
        this.hiders = hiders;
        this.locations = locations;
    }

    @EventHandler
    public void onMusicBoxWind(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        //if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getItem())
        for(Location loc : locations) {
            if(loc.equals(e.getClickedBlock().getLocation())) {

            }
        }
    }

}
