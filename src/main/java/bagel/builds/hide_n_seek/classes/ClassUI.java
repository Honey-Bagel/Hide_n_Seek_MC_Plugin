package bagel.builds.hide_n_seek.classes;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.manager.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ClassUI {

    public ClassUI(Main main, GameManager gameManager, Player player) {

        if(gameManager.getTeams().containsKey(player.getUniqueId())) {
            Inventory gui = Bukkit.createInventory(null, 27, ChatColor.DARK_BLUE + "Class Selection");

            //Animatronics
            if (gameManager.getTeam(player).equals(Team.ANIMATRONIC)) {
                if (gameManager.getAnimatronicsMap().containsKey(player.getUniqueId())) {
                    gui = Bukkit.createInventory(null, 27, ChatColor.DARK_BLUE + "Class Selection - Current: (" + main.getGameManager().getAnimatronic(player).getName() + ChatColor.DARK_BLUE + ")");
                }
                for (Animatronic animatronic : Animatronic.values()) {
                    ItemStack is = new ItemStack(animatronic.getMaterial());
                    ItemMeta isMeta = is.getItemMeta();
                    isMeta.setDisplayName(animatronic.getName());
                    isMeta.setLore(Arrays.asList(ChatColor.GRAY + animatronic.getDescription()));
                    isMeta.setLocalizedName(animatronic.name());
                    is.setItemMeta(isMeta);

                    gui.addItem(is);
                }
            }

            //Hiders
            else if (gameManager.getTeam(player).equals(Team.HIDER)) {
                if (gameManager.getHiderMap().containsKey(player.getUniqueId())) {
                    gui = Bukkit.createInventory(null, 27, ChatColor.DARK_BLUE + "Class Selection - Current: (" + main.getGameManager().getHider(player).getName() + ChatColor.DARK_BLUE + ")");
                }
                for (Hider hider : Hider.values()) {
                    ItemStack is = new ItemStack(Material.GRAY_DYE);
                    ItemMeta isMeta = is.getItemMeta();
                    isMeta.setDisplayName(hider.getName());
                    isMeta.setLore(Arrays.asList(hider.getDescription()));
                    isMeta.setLocalizedName(hider.name());
                    is.setItemMeta(isMeta);

                    gui.addItem(is);
                }
            }

            ItemStack is = new ItemStack(Material.BARRIER);
            ItemMeta isMeta = is.getItemMeta();
            isMeta.setDisplayName(ChatColor.RED + "Remove Class");
            isMeta.setLocalizedName("remove class");
            isMeta.setLore(Arrays.asList(ChatColor.GREEN + "Removes selected class"));
            is.setItemMeta(isMeta);
            gui.setItem(26, is);


            player.openInventory(gui);
        } else {
            new TeamUI(gameManager ,player);
        }
    }

}
