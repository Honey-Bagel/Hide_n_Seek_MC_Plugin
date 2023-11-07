package bagel.builds.hide_n_seek.classes.type;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.Animatronic;
import bagel.builds.hide_n_seek.classes.Hider;
import bagel.builds.hide_n_seek.classes.type.classrunnable.BonnieGlow;
import bagel.builds.hide_n_seek.classes.type.classutil.MarionetteTask;
import bagel.builds.hide_n_seek.classes.type.classutil.TempMarionetteBoxHandler;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MarionetteClass extends ClassType{

    private Main main;
    private Player player;
    private ItemStack MItem;
    private List<Player> hiders;
    private Cache<UUID, Long> cooldown = CacheBuilder.newBuilder().expireAfterWrite(120, TimeUnit.SECONDS).build();
    private TempMarionetteBoxHandler mbhandler;
    private List<Location> locations;

    public MarionetteClass(Main main, Animatronic animatronic, UUID uuid) {
        super(main, Animatronic.MARIONETTE, uuid);
        this.main = main;
        this.player = Bukkit.getPlayer(uuid);
    }

    @Override
    public void start() {

        this.mbhandler = new TempMarionetteBoxHandler(main);
        mbhandler.getLocations();

        this.MItem = new ItemStack(Material.JUKEBOX);
        ItemMeta mMeta = MItem.getItemMeta();
        mMeta.setDisplayName(ChatColor.BLUE + "Marionette's Music Box");
        mMeta.setLore(Arrays.asList(ChatColor.GRAY + "Creates a task hiders must complete", ChatColor.GRAY + "Hiders have 30sec to complete the task", ChatColor.GRAY + "30 second cooldown"));
        mMeta.setLocalizedName("marionette's music box");
        MItem.setItemMeta(mMeta);

        player.getInventory().addItem(MItem);
        player.getInventory().addItem(mbhandler.getMusicBox());

    }

    @Override
    public void reset() {
        if(player.getInventory().contains(MItem)) {
            player.getInventory().remove(MItem);
        }
        super.remove();
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent e) {
        for(UUID uuid : main.getGameManager().getHiderMap().keySet()) {
            hiders.add(Bukkit.getPlayer(uuid));
        }

        if(e.getPlayer().equals(player) && player.getInventory().getItemInMainHand().equals(MItem) && ( e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR) ) && e.getHand().equals(EquipmentSlot.HAND)) {
            e.setCancelled(true);
            if(!cooldown.asMap().containsKey(player.getUniqueId())) {
                new MarionetteTask(main.getGameManager(), hiders, locations);

                cooldown.put(player.getUniqueId(), System.currentTimeMillis() + 120000);
            } else {
                long distance = cooldown.asMap().get(player.getUniqueId()) - System.currentTimeMillis();
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§4You must wait " + "§f" + TimeUnit.MILLISECONDS.toSeconds(distance) + "§4 seconds to use this."));
            }
        }
    }
}
