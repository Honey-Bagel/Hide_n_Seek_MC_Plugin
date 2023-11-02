package bagel.builds.hide_n_seek.handlers.misc;

import bagel.builds.hide_n_seek.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class FallingBlockHandler implements Listener {

    public FallingBlockHandler(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

//    @EventHandler
//    public void onBlockBreak(EntityChangeBlockEvent e) {
//        if(e.getBlock().getLocation().subtract(0,1,0).getBlock().getType() != Material.AIR) return;
//        Block b = e.getBlock();
//        Material bType = b.getType();
//        if(bType != Material.SAND || bType != Material.GRAVEL || bType != Material.WHITE_CONCRETE_POWDER ) return;
//
//        e.setCancelled(true);
//
//    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    private void onSandFall(EntityChangeBlockEvent event){
        if(event.getEntityType()== EntityType.FALLING_BLOCK && event.getTo()==Material.AIR){

            if(checkMaterial(event.getBlock().getType())){
                event.setCancelled(true);
                //Update the block to fix a visual client bug, but don't apply physics
                event.getBlock().getState().update(false, false);
            }
        }
    }


    private boolean checkMaterial(Material mat) {
        Material[] fallingBlocks = { Material.SAND, Material.GRAVEL, Material.RED_SAND, Material.BLACK_CONCRETE_POWDER,Material.BLACK_CONCRETE_POWDER,Material.BLUE_CONCRETE_POWDER,Material.BROWN_CONCRETE_POWDER,Material.GRAY_CONCRETE_POWDER,Material.GREEN_CONCRETE_POWDER,Material.CYAN_CONCRETE_POWDER,Material.LIGHT_BLUE_CONCRETE_POWDER,Material.LIME_CONCRETE_POWDER,Material.LIGHT_GRAY_CONCRETE_POWDER,Material.BROWN_CONCRETE_POWDER,Material.MAGENTA_CONCRETE_POWDER,Material.ORANGE_CONCRETE_POWDER,Material.PURPLE_CONCRETE_POWDER,Material.WHITE_CONCRETE_POWDER,Material.YELLOW_CONCRETE_POWDER,Material.PINK_CONCRETE_POWDER,Material.WHITE_CONCRETE_POWDER,Material.RED_CONCRETE_POWDER};
        for(Material temp: fallingBlocks) {
            if(temp == mat) return true;
        }
        return false;
    }
}
