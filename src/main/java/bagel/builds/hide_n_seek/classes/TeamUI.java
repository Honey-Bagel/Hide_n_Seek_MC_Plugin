package bagel.builds.hide_n_seek.classes;

import bagel.builds.hide_n_seek.manager.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class TeamUI {

    public TeamUI(GameManager gameManager, Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.DARK_GREEN + "Team Selection");

        for(Team team : Team.values()) {
            ItemStack is = new ItemStack(Material.GRAY_DYE);
            ItemMeta isMeta = is.getItemMeta();
            isMeta.setDisplayName(team.getName());
            isMeta.setLocalizedName(team.name());
            is.setItemMeta(isMeta);
            gui.addItem(is);
        }

        ItemStack is = new ItemStack(Material.BARRIER);
        ItemMeta isMeta = is.getItemMeta();
        isMeta.setDisplayName(ChatColor.RED + "Remove Team");
        isMeta.setLocalizedName("remove team");
        isMeta.setLore(Arrays.asList(ChatColor.GREEN + "Removes selected team"));
        is.setItemMeta(isMeta);
        gui.setItem(26, is);

        player.openInventory(gui);
    }


}
