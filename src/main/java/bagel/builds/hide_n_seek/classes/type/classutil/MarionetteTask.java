package bagel.builds.hide_n_seek.classes.type.classutil;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.manager.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MarionetteTask implements Listener {

    private Main main;
    private HashMap<UUID, Long> playersWinding;
    private List<Player> hiders;
    private List<Location> locations;
    private static final double startPercent = 0.2;
    private static final double percentDownperSec = 0.033; //every 3 seconds down 1 percent (0.01)
    private double progress;

    public MarionetteTask(Main main, List<Player> hiders, List<Location> locations) {
        this.main = main;
        this.hiders = hiders;
        this.locations = locations;
        this.playersWinding = new HashMap<>();
        this.progress = startPercent;
        Bukkit.getPluginManager().registerEvents(this, main);
        System.out.println(locations.size());
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
                if (curTime - lastClick > 250) {
                    playersWinding.remove(player.getUniqueId());
                } else {
                    playersWinding.put(player.getUniqueId(), curTime);
                    progress += 0.01 / 5;
                    player.sendMessage(progress + "");
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

}
