package bagel.builds.hide_n_seek.listener.misc;

import bagel.builds.hide_n_seek.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.material.Openable;

public class IronTDHandler implements Listener {

    public IronTDHandler(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onTrapdoorRC(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getHand() == EquipmentSlot.HAND) {
                //        if(event.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR) return;
                if (event.getClickedBlock().getType() == Material.IRON_TRAPDOOR && !event.getPlayer().isSneaking()) {
                    Block td = event.getClickedBlock();

                    BlockState state = td.getState();
                    Openable openable = (Openable) state.getData();

                    if (!openable.isOpen()) {
                        openable.setOpen(true);
                    } else {
                        openable.setOpen(false);
                    }
                    state.setBlockData((BlockData) openable);
                    state.update();
                }
            }
        }
    }

    @EventHandler
    public void onClickBlock(BlockPlaceEvent e) {
        if(e.getHand() != EquipmentSlot.OFF_HAND) {
            if (e.getBlockAgainst().getType() == Material.IRON_TRAPDOOR && !e.getPlayer().isSneaking()) {
                e.setCancelled(true);
            }
        }
    }

}
