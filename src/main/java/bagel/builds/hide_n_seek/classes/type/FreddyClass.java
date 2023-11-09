package bagel.builds.hide_n_seek.classes.type;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.Animatronic;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FreddyClass extends ClassType{

    private Main main;
    private List<Location> teleportLocations;
    private ItemStack FItem;
    private Player player;
    private Cache<UUID, Long> cooldown = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();

    public FreddyClass(Main main, Animatronic animatronic, UUID uuid) {
        super(main, Animatronic.FREDDY, uuid);
        this.main = main;

        this.player = Bukkit.getPlayer(uuid);

        teleportLocations = new ArrayList<>();
    }

    @Override
    public void start() {
        loadLocations();
        FItem = new ItemStack(Material.LEVER);
        ItemMeta iMeta = FItem.getItemMeta();
        iMeta.setDisplayName(ChatColor.DARK_GRAY + "Freddy's Microphone");
        iMeta.setLore(Arrays.asList(ChatColor.GRAY + "Randomly teleports you to a set location", ChatColor.GRAY + "30 Second cooldown"));
        iMeta.setLocalizedName("Freddy's Microphone");
        FItem.setItemMeta(iMeta);

        player.getInventory().addItem(FItem);
    }

    @Override
    public void reset() {
        if(player.getInventory().contains(FItem)) {
            player.getInventory().remove(FItem);
        }
        super.remove();
    }


    public void loadLocations() {
        teleportLocations = main.getLocFileManager().getLocation("FreddyTPLoc");
    }

    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.LEVER);
        ItemMeta iMeta = item.getItemMeta();
        iMeta.setDisplayName(ChatColor.DARK_GRAY + "Freddy's Microphone");
        iMeta.setLore(Arrays.asList(ChatColor.GRAY + "Randomly teleports you to a set location", ChatColor.GRAY + "30 Second cooldown"));
        iMeta.setLocalizedName("Freddy's Microphone");
        item.setItemMeta(iMeta);

        player.getInventory().addItem(item);
        return item;
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(player.equals(this.player) && player.getInventory().getItemInMainHand().equals(FItem) && ( e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR) ) && e.getHand().equals(EquipmentSlot.HAND)) {
            if(!cooldown.asMap().containsKey(player.getUniqueId())) {
                player.teleport(teleportLocations.get((int) Math.random()*teleportLocations.size()));
            } else {
                long distance = cooldown.asMap().get(player.getUniqueId()) - System.currentTimeMillis();
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§4You must wait " + "§f" + TimeUnit.MILLISECONDS.toSeconds(distance) + "§4 seconds to use this."));
            }
        }
    }
}
