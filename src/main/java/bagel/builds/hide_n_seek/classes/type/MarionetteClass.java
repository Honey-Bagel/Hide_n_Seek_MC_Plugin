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
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MarionetteClass extends ClassType{

    private Main main;
    private Player player;
    private ItemStack MItem;
    private Cache<UUID, Long> cooldown = CacheBuilder.newBuilder().expireAfterWrite(80, TimeUnit.SECONDS).build();
    private TempMarionetteBoxHandler mbhandler;
    private List<Location> locations;
    private static boolean activeTask = false;

    public MarionetteClass(Main main, Animatronic animatronic, UUID uuid) {
        super(main, Animatronic.MARIONETTE, uuid);
        this.main = main;
        this.player = Bukkit.getPlayer(uuid);
    }

    @Override
    public void start() {

        this.mbhandler = new TempMarionetteBoxHandler(main);
        try {
            mbhandler.fileStart();
        } catch(IOException e) {
            e.printStackTrace();
        }
        this.locations = mbhandler.getLocations();

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

            if (e.getPlayer().equals(player) && player.getInventory().getItemInMainHand().equals(MItem) && (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) && e.getHand().equals(EquipmentSlot.HAND) && !activeTask) {
                e.setCancelled(true);
                if (!cooldown.asMap().containsKey(player.getUniqueId())) {
                    new MarionetteTask(main, locations, player, this);

                } else {
                    long distance = cooldown.asMap().get(player.getUniqueId()) - System.currentTimeMillis();
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§4You must wait " + "§f" + TimeUnit.MILLISECONDS.toSeconds(distance) + "§4 seconds to use this."));
                }
            } else {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§4You already have an active ability."));
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        if(e.getItemDrop().equals(MItem)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if(e.getItemInHand().equals(MItem)) {
            e.setBuild(false);
        }
    }

    public void setActiveTask(boolean active) {
        if(active) {
            activeTask = true;
        } else {
            activeTask = false;
            cooldown.put(player.getUniqueId(), System.currentTimeMillis() + 80000);
        }
    }
}
