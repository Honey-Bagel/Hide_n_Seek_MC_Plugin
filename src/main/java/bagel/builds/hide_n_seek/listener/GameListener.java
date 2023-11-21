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
        main.getCameraManager().addPlayerCamera(e.getPlayer());
//        setSkin(e.getPlayer());
    }

//    public void setSkin(Player player) {
//        EntityPlayer ep = ((CraftPlayer) player).getHandle();
//        GameProfile gp = ep.getBukkitEntity().getProfile();
//        PropertyMap pm = gp.getProperties();
//        Property property = pm.get("textures").iterator().next();
//        pm.remove("textures", property);
//        pm.put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTY0MzcxMjcxMTcxOSwKICAicHJvZmlsZUlkIiA6ICI3ZGEyYWIzYTkzY2E0OGVlODMwNDhhZmMzYjgwZTY4ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJHb2xkYXBmZWwiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWVjNWIyNzI0MzEyYTI2YmVlY2MyOWY4Nzg5ZGQ4M2QzMTA5ZjRkYzU3NzM4NGQyYWRhMjFmOGU4MzdlNWFiYyIKICAgIH0KICB9Cn0=", "bHOLVe8gDakxGwxyJgBpUL5L/b6x65YiHw83k8FQ2fF9I0hCKrNGUB1uBWlUUgOvtDmX65L0DmQxLf66bzgJmnapRZZmBa4nM79U4Bnsy7yMKHQ+wS9HmJeKmH893+Fp1FdPmrcpD8OhdcN2Of1tq6LE3XLFGV/ptmMT0CldR27w9P5Fv87scMhyLLjSgstnIf5ZOLMsKr21jKPLuw5u30YUeyiup4igXX5ff/PlVgoVe2CTaucg7VwiJSWTwlxRJjp0eqRD6fSi1xNfYnl3WCGY5bQi72RgMCaHWoZdKURvncAu3eEuKRjvHsElJcrX8rHaXQ3sb9AOEPkJ2NhZOWZ3w3BtaqzTTGvET1bUJWV+xcIx9tkiJrWQJ1mba8eA2kIJ34Bvebns0Rx++K84UN6xU2i4FIr57xNhYq3NnyCLXerJ9kURgQfJr3dFPI6i/zGdhcSu2pYulsqlXYdWY3k8xqC/NLIoKlOauvXDAMACr5Ai8GABi2U7ZZWVlSGtMnBEt+4vVKslQprKrGbn5ZS6uw5JVZ/uwxDQ/TA/chWGQTtpSfZnk59wFYTN0PWDAcbv0J1jBRIuXvfgs7B94aASNm/nGMues7cQ5x6VY3nBAHMN16fpk+UxIwyx9Pm1L9OinUUfoQWKjVlAak+d24ELvLxaykL6vr/wcdAp6i8="));
//    }


    //UI Click
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        NamespacedKey key = new NamespacedKey(main, "team");

        if(e.getView().getTitle().contains("Class Selection") && e.getInventory() != null && e.getCurrentItem() != null) {
            e.setCancelled(true);
            if (!e.getClickedInventory().equals(player.getInventory())) {
            if (main.getGameManager().getAnimatronicsMap().containsKey(player.getUniqueId()) && e.getCurrentItem().getItemMeta().getLocalizedName().equalsIgnoreCase("remove class")) {
                if (gameManager.getTeams().get(player.getUniqueId()).equals(Team.ANIMATRONIC)) {
                    gameManager.removeAnimatronic(player);
                    player.sendMessage(ChatColor.GREEN + "Removed your class.");
                } else if (gameManager.getTeams().get(player.getUniqueId()).equals(Team.HIDER)) {
                    gameManager.removeHider(player);
                    player.sendMessage(ChatColor.GREEN + "Removed your class.");
                } else {
                    player.sendMessage(ChatColor.RED + "You don't have a class.");
                }
            } else if (e.getCurrentItem().getType() != Material.BARRIER && gameManager.getTeams().get(player.getUniqueId()).equals(Team.ANIMATRONIC)) {
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
            } else if (gameManager.getTeams().get(player.getUniqueId()).equals(Team.HIDER)) {
                Hider hider = Hider.valueOf(e.getCurrentItem().getItemMeta().getLocalizedName());

                if (!gameManager.isHiderTaken(hider)) {
                    player.sendMessage(ChatColor.GREEN + "You selected " + hider.getName() + ChatColor.GREEN + ".");
                    main.getGameManager().setHider(player, hider);
                    player.closeInventory();
                } else if (main.getGameManager().getHider(player) == hider) {
                    player.sendMessage(ChatColor.RED + "You already have this hider selected.");
                } else {
                    player.sendMessage(ChatColor.RED + "The hider you selected is already taken.");
                }
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
