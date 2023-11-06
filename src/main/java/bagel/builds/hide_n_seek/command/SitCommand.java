package bagel.builds.hide_n_seek.command;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

public class SitCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(!player.isOnGround()) return false;


            /*
            Check if something is stair/slab, create different spawn location of armorstand because of different height
             */

            Location standingBlock = player.getLocation().subtract(0,1,0);
            BlockState standingState = standingBlock.getBlock().getState();

            ArmorStand stand;

//            System.out.println(standingBlock.getBlock());
//            if(standingState.getBlockData() instanceof Stairs) {
//                System.out.println("stair");
//                Stairs data = (Stairs) standingState.getData();
//                stand = (ArmorStand) player.getWorld().spawnEntity(standingBlock.add(0,0.5,0), EntityType.ARMOR_STAND);
//            } else if(standingState.getData() instanceof Slab) {
//                System.out.println("slab");
//                Slab data = (Slab) standingState.getData();
//                stand = (ArmorStand) player.getWorld().spawnEntity(standingBlock.add(0,0.5,0), EntityType.ARMOR_STAND);
//            } else {
//
//                stand = (ArmorStand) player.getWorld().spawnEntity(standingBlock.add(0,1,0), EntityType.ARMOR_STAND);
//            }
            stand = (ArmorStand) player.getWorld().spawnEntity(standingBlock, EntityType.ARMOR_STAND);
                stand.setVisible(false);
                stand.setGravity(false);
                stand.setSmall(true);
                stand.setCustomName("ChairEntity");

            stand.addPassenger(player);
        }


        return false;
    }

}
