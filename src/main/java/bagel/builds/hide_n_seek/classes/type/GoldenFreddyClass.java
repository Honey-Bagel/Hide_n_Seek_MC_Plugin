package bagel.builds.hide_n_seek.classes.type;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.Animatronic;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.authlib.GameProfile;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GoldenFreddyClass extends ClassType {

    private Main main;
    private Player player;
    private ItemStack item;
    private Cache<UUID, Long> cooldown = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();
    public GoldenFreddyClass(Main main, Animatronic animatronic, UUID uuid) {
        super(main, Animatronic.GOLDEN_FREDDY, uuid);
        this.main = main;
        this.player = Bukkit.getPlayer(uuid);
    }

    @Override
    public void start(int addCooldown) {
        item = createItem();
        player.getInventory().addItem(item);

        skinUtil.changeSkin(Animatronic.GOLDEN_FREDDY.getProperty());
    }

    @Override
    public void reset() {
        super.remove();
        if(player.getInventory().contains(item)) {
            player.getInventory().remove(item);
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(player.equals(this.player) && player.getInventory().getItemInMainHand().equals(item) && ( e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR) ) && e.getHand().equals(EquipmentSlot.HAND)) {
            if(!cooldown.asMap().containsKey(player.getUniqueId())) {
                long start = System.currentTimeMillis();



            } else {
                long distance = cooldown.asMap().get(player.getUniqueId()) - System.currentTimeMillis();
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§4You must wait " + "§f" + TimeUnit.MILLISECONDS.toSeconds(distance) + "§4 seconds to use this."));
            }
        }
    }

    public ItemStack createItem() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta cMeta = (SkullMeta) head.getItemMeta();
        cMeta.setDisplayName(ChatColor.GOLD + "Golden Freddy's Head");
        cMeta.setLore(Arrays.asList(ChatColor.GRAY + "Hold right click to throw", ChatColor.GRAY + "20 Second Cooldown"));
        cMeta.setLocalizedName("GFreddy Head");

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", Animatronic.GOLDEN_FREDDY.getProperty());
        Field field;
        try {
            field = cMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(cMeta,profile);
        } catch(NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
        head.setItemMeta(cMeta);
        return head;
    }


}
