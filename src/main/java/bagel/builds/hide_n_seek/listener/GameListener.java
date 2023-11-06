package bagel.builds.hide_n_seek.listener;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.*;
import bagel.builds.hide_n_seek.manager.GameManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class GameListener implements Listener {

    private Main main;
    private GameManager gameManager;

    public GameListener(Main main, GameManager gameManager) {
        this.main = main;
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        main.getGameManager().addPlayer(e.getPlayer());
    }


    //UI Click
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        NamespacedKey key = new NamespacedKey(main, "team");

        if(e.getView().getTitle().contains("Class Selection") && e.getInventory() != null && e.getCurrentItem() != null) {
            e.setCancelled(true);
            if(main.getGameManager().getAnimatronicsMap().containsKey(player.getUniqueId()) && e.getCurrentItem().getItemMeta().getLocalizedName().equalsIgnoreCase("remove class")) {
                if(gameManager.getTeams().get(player.getUniqueId()).equals(Team.ANIMATRONIC)) {
                    gameManager.removeAnimatronic(player);
                    player.sendMessage(ChatColor.GREEN + "Removed your class.");
                } else if(gameManager.getTeams().get(player.getUniqueId()).equals(Team.HIDER)) {
                    gameManager.removeHider(player);
                    player.sendMessage(ChatColor.GREEN + "Removed your class.");
                } else {
                    player.sendMessage(ChatColor.RED + "You don't have a class.");
                }
            } else if(e.getCurrentItem().getType() != Material.BARRIER && gameManager.getTeams().get(player.getUniqueId()).equals(Team.ANIMATRONIC)) {
                    Animatronic animatronic = Animatronic.valueOf(e.getCurrentItem().getItemMeta().getLocalizedName());

                    if (!gameManager.isAnimatronicTaken(animatronic)) {
                        player.sendMessage(ChatColor.GREEN + "You selected " + animatronic.getName() + ChatColor.GREEN + ".");
                        main.getGameManager().setAnimatronic(player, animatronic);
                        player.closeInventory();
                    } else if (main.getGameManager().getAnimatronic(player) == animatronic) {
                        player.sendMessage(ChatColor.RED + "You already have this animatronic selected.");
                    } else {
                        player.sendMessage(ChatColor.RED + "The animatronic you selected is already taken.");
                    }
            } else if(gameManager.getTeams().get(player.getUniqueId()).equals(Team.HIDER)) {
                Hider hider = Hider.valueOf(e.getCurrentItem().getItemMeta().getLocalizedName());

                if(!gameManager.isHiderTaken(hider)) {
                    player.sendMessage(ChatColor.GREEN + "You selected " + hider.getName() + ChatColor.GREEN + ".");
                    main.getGameManager().setHider(player, hider);
                    player.closeInventory();
                } else if(main.getGameManager().getHider(player) == hider) {
                    player.sendMessage(ChatColor.RED + "You already have this hider selected.");
                } else {
                    player.sendMessage(ChatColor.RED + "The hider you selected is already taken.");
                }
            }

        }

        else if(e.getView().getTitle().contains("Team Selection") && e.getInventory() != null && e.getCurrentItem() != null) {
            e.setCancelled(true);
            if(gameManager.getTeams().containsKey(player.getUniqueId()) && e.getCurrentItem().getItemMeta().getLocalizedName().equalsIgnoreCase("remove team")) {
                gameManager.removeTeam(player);
                player.sendMessage(ChatColor.GREEN + "Removed your team.");
                new TeamUI(gameManager, player);
            } else if(e.getCurrentItem().getType() != Material.BARRIER && Team.valueOf(e.getCurrentItem().getItemMeta().getLocalizedName()) != null) {
                Team team = Team.valueOf(e.getCurrentItem().getItemMeta().getLocalizedName());
                if(team.equals(Team.ANIMATRONIC) && gameManager.getHiderMap().containsKey(player.getUniqueId())) {
                    gameManager.removeHider(player);
                } else if(team.equals(Team.HIDER) && gameManager.getAnimatronicsMap().containsKey(player.getUniqueId())) {
                    gameManager.removeAnimatronic(player);
                }
                if(gameManager.getTeams().containsKey(player.getUniqueId()) && gameManager.getTeam(player).equals(team)) {
                    player.sendMessage(ChatColor.RED + "You are already on this team.");
                } else {
                    /* MAKE THIS NOT ALWAYS SAY YOU ARE ALREADYO N THIS TEAM */
                    gameManager.setTeam(player, team);
                    player.sendMessage(ChatColor.GREEN + "You chose the " + team.getName() + ChatColor.GREEN + " team.");
                    player.closeInventory();
                    new ClassUI(main, gameManager, player);
                }
            }
        }
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if(main.getGameManager().getHiderMap().containsKey(player.getUniqueId())) {
            e.setFormat("[" + main.getGameManager().getHider(player).getName() + ChatColor.WHITE + "] " + player.getName() + ": " + e.getMessage());
        } else if(main.getGameManager().getClasstypes().containsKey(player.getUniqueId()) ){
            if(main.getGameManager().getClasstypes().get(player.getUniqueId()).getAnimatronic() != null && main.getGameManager().getAnimatronic(player) != null) {
                e.setFormat("[" + main.getGameManager().getAnimatronic(player).getName() + ChatColor.WHITE + "] " + player.getName() + ": " + e.getMessage());
            } else {
                e.setFormat(player.getName() + ": " + e.getMessage());
            }
        } else {
            e.setFormat(player.getName() + ": " + e.getMessage());
        }


    }

}
