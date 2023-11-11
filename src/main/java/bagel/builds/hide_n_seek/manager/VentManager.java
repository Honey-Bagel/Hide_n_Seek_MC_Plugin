package bagel.builds.hide_n_seek.manager;

import bagel.builds.hide_n_seek.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VentManager {

    private GameManager gameManager;

    private HashMap<UUID, Boolean> ventList;

    public VentManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.ventList = new HashMap<>();
        //this.class = class
    }

    public void start() {

    }

    public void addPlayer(Player player) {
            ventList.put(player.getUniqueId(), false);
    }
    public void removePlayer(Player player) {
        if(ventList.containsKey(player.getUniqueId())) {
            ventList.remove(player.getUniqueId());
        }
    }



    public HashMap<UUID, Boolean> getVentList() { return ventList; }


}
