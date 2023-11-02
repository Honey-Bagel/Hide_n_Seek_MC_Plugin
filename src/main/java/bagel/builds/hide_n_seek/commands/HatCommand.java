package bagel.builds.hide_n_seek.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class HatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)) return false;

//        String[] hatArray = {"Santa", "Catears"};
//
//        int index = -1;
//
//        ArrayList<String> hats = new ArrayList<String>();
//        for(String h : hatArray) {
//            hats.add(h);
//        }
//
//        if(args[0].isEmpty()) {
//            sender.sendMessage(ChatColor.RED + "Please select the hat you want.");
//        }
//        for(String hat : hats) {
//            if(args[0].matches(hat.toLowerCase())) {
//                index = hats.indexOf(args[0]) + 1;
//                System.out.println(hats.indexOf(args[0]));
//            }
//        }
//        if(index > 0) {
//            ItemStack is = new ItemStack(Material.CARVED_PUMPKIN);
//            ItemMeta meta = is.getItemMeta();
//            meta.setCustomModelData(index);
//            meta.setDisplayName(ChatColor.WHITE + args[index]);
//            meta.setLocalizedName(args[0]);
//            is.setItemMeta(meta);
//
//            ((Player) sender).getInventory().addItem(is);
//        } else {
//            sender.sendMessage(ChatColor.RED + "The hat name you selected does not exist");
//        }
            ItemStack is = new ItemStack(Material.CARVED_PUMPKIN);
            ItemMeta meta = is.getItemMeta();
            meta.setCustomModelData(0);
            meta.setDisplayName(ChatColor.WHITE + "Santa Hat");
            meta.setLocalizedName("santa");
            is.setItemMeta(meta);

        ((Player) sender).getInventory().addItem(is);




        return false;
    }
}
