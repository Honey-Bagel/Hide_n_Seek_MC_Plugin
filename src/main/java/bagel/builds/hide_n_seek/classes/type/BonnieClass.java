package bagel.builds.hide_n_seek.classes.type;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.Animatronic;
import bagel.builds.hide_n_seek.classes.type.classrunnable.BonnieGlow;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BonnieClass extends ClassType{
    private Main main;
    private Player player;
    private ItemStack BItem;
    private Cache<UUID, Long> cooldown = CacheBuilder.newBuilder().expireAfterWrite(25, TimeUnit.SECONDS).build();

    public BonnieClass(Main main, Animatronic animatronic, UUID uuid) {
        super(main, Animatronic.BONNIE, uuid);
        this.main = main;
        this.player = Bukkit.getPlayer(uuid);
    }

    @Override
    public void start() {
        this.BItem = new ItemStack(Material.CLOCK);
        ItemMeta bMeta = BItem.getItemMeta();
        bMeta.setDisplayName(ChatColor.DARK_RED + "Bonnie's Guitar");
        bMeta.setLore(Arrays.asList(ChatColor.GRAY + "Reveals hiders within 5 blocks.", ChatColor.GRAY + "25 second cooldown."));
        bMeta.setLocalizedName("bonnie's guitar");
        BItem.setItemMeta(bMeta);

        player.getInventory().addItem(BItem);
    }

    @Override
    public void reset() {
        if(player.getInventory().contains(BItem)) {
            player.getInventory().remove(BItem);
        }
        super.remove();
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (e.getPlayer().equals(player) && player.getInventory().getItemInMainHand().equals(BItem) && ( e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR) ) && e.getHand().equals(EquipmentSlot.HAND)) {
            if(!cooldown.asMap().containsKey(player.getUniqueId())) {
                new BonnieGlow(main, player).start();
                cooldown.put(player.getUniqueId(), System.currentTimeMillis() + 25000);
            } else {
                long distance = cooldown.asMap().get(player.getUniqueId()) - System.currentTimeMillis();
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§4You must wait " + "§f" + TimeUnit.MILLISECONDS.toSeconds(distance) + "§4 seconds to use this."));
            }
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        if(e.getItemDrop().equals(BItem)) {
            e.setCancelled(true);
        }
    }
}
