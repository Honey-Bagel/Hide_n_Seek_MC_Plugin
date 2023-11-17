package bagel.builds.hide_n_seek.npc;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.Animatronic;
import bagel.builds.hide_n_seek.classes.Hider;
import bagel.builds.hide_n_seek.classes.Team;
import bagel.builds.hide_n_seek.manager.GameManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class NPCListener implements Listener {

    private packetHandler packetHandler;
    private NPCManager npcManager;
    private Main main;
    private GameManager gameManager;

    public NPCListener(Main main, packetHandler packetHandler, NPCManager npcManager) {
        this.main = main;
        this.npcManager = npcManager;
        this.packetHandler = packetHandler;
        this.gameManager = main.getGameManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        packetHandler.inject(e.getPlayer(), main);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        packetHandler.stop(e.getPlayer());
    }

    @EventHandler
    public void onNpcInteract(NPCEvent e) {
        List<NPC> npcList = npcManager.getNpcList();
        Player player = e.getPlayer();

        for(NPC npc : npcList) {
            if(npc.getId() == e.getEntityID()) {
                if(npc.getTeam().equals(Team.ANIMATRONIC)) {
                    /* Animatronics */

                    Animatronic animatronic = npc.getAnimatronic();
                    if (!gameManager.isAnimatronicTaken(animatronic)) {
                        player.sendMessage(ChatColor.GREEN + "You selected " + animatronic.getName() + ChatColor.GREEN + ".");
                        main.getGameManager().setAnimatronic(player, animatronic);
                        main.getGameManager().setTeam(player, Team.ANIMATRONIC);
                        player.closeInventory();
                    } else if (main.getGameManager().getAnimatronic(player) == animatronic) {
                        player.sendMessage(ChatColor.RED + "You already have this animatronic selected.");
                    } else {
                        player.sendMessage(ChatColor.RED + "The animatronic you selected is already taken.");
                    }
                } else if(npc.getTeam().equals(Team.HIDER)) {
                    /* Hiders */

                    Hider hider = npc.getHider();
                    if (!gameManager.isHiderTaken(hider)) {
                        player.sendMessage(ChatColor.GREEN + "You selected " + hider.getName() + ChatColor.GREEN + ".");
                        main.getGameManager().setHider(player, hider);
                        main.getGameManager().setTeam(player, Team.HIDER);
                        player.closeInventory();
                    } else if (main.getGameManager().getHider(player) == hider) {
                        player.sendMessage(ChatColor.RED + "You already have this hider selected.");
                    } else {
                        player.sendMessage(ChatColor.RED + "The hider you selected is already taken.");
                    }
                }
            }
        }
    }

}
