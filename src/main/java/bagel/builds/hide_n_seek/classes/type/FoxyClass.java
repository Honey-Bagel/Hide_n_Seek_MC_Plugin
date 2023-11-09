package bagel.builds.hide_n_seek.classes.type;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.Animatronic;
import bagel.builds.hide_n_seek.classes.Hider;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FoxyClass extends ClassType{
    private Main main;
    private UUID uuid;
    private Player player;
    private static Boolean running;
    private ItemStack FItem;
    private Cache<UUID, Long> cooldown = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();
    private BukkitTask task;

    public FoxyClass(Main main, Animatronic animatronic, UUID uuid) {
        super(main, Animatronic.FOXY, uuid);
        this.main = main;
        this.uuid = uuid;
        this.player = (Player) Bukkit.getPlayer(uuid);
        this.running = false;
    }

    @Override
    public void start() {
//        player.setWalkSpeed(0.4f);
        this.FItem = new ItemStack(Material.RABBIT_FOOT);
        ItemMeta fMeta = FItem.getItemMeta();
        fMeta.setDisplayName(ChatColor.DARK_RED + "Foxy Sprint");
        fMeta.setLore(Arrays.asList(ChatColor.GRAY + "Activates sprinting speed for 15 seconds.", ChatColor.GRAY + "Speed in view of hiders: 4.", ChatColor.GRAY + "Speed while not seen: 6." ,ChatColor.GRAY + "30 second cooldown."));
        fMeta.setLocalizedName("foxy sprint");
        FItem.setItemMeta(fMeta);

        player.getInventory().addItem(FItem);
    }

    @Override
    public void reset() {
        if(running) {
            player.setWalkSpeed(0.2f);
        }
        if(player.getInventory().contains(FItem)) {
            player.getInventory().remove(FItem);
        }
        task.cancel();
        super.remove();
    }

    //not seen .5or.6

    @EventHandler
    public void onItemUse(PlayerInteractEvent e) {
        if(e.getPlayer().equals(player) && player.getInventory().getItemInMainHand().equals(FItem) && ( e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR) ) && e.getHand().equals(EquipmentSlot.HAND)) {
            if(!cooldown.asMap().containsKey(player.getUniqueId())) {
//                if(getNearestEntityInSight(player, 100) == (null)) {
//                    player.setWalkSpeed(0.6f);
//                } else {
//                    player.setWalkSpeed(0.4f);
//                }
                if(inLineofSight()) {
                    player.setWalkSpeed(0.4f);
                } else {
                    player.setWalkSpeed(0.6f);
                }
                running = true;
                AtomicInteger atomint = new AtomicInteger(15);
                task = Bukkit.getScheduler().runTaskTimer(main, () -> {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§f " + atomint.getAndDecrement() + " sprint time left." ));
                }, 0, 20);

                Bukkit.getScheduler().runTaskLater(main, () -> {
                    task.cancel();
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
                    player.setWalkSpeed(0.2f);
                }, 15*20);
                cooldown.put(player.getUniqueId(), System.currentTimeMillis() + 30000);
            } else {
                long distance = cooldown.asMap().get(player.getUniqueId()) - System.currentTimeMillis();
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§4You must wait " + "§f" + TimeUnit.MILLISECONDS.toSeconds(distance) + "§4 seconds to use this."));
            }
        }
    }

    private Entity getNearestEntityInSight(Player player, int range) {
        ArrayList<Entity> entities = (ArrayList<Entity>) player.getNearbyEntities(range, range, range);
        ArrayList<Block> sightBlock = (ArrayList<Block>) player.getLineOfSight( (Set<Material>) null, range);
        ArrayList<Location> sight = new ArrayList<Location>();
        for (int i = 0;i<sightBlock.size();i++)
            sight.add(sightBlock.get(i).getLocation());
        for (int i = 0;i<sight.size();i++) {
            for (int k = 0;k<entities.size();k++) {
                if (Math.abs(entities.get(k).getLocation().getX()-sight.get(i).getX())<1.3) {
                    if (Math.abs(entities.get(k).getLocation().getY()-sight.get(i).getY())<1.5) {
                        if (Math.abs(entities.get(k).getLocation().getZ()-sight.get(i).getZ())<1.3) {
                            return entities.get(k);
                        }
                    }
                }
            }
        }
        return null; //Return null/nothing if no entity was found
    }

    private boolean inLineofSight() {
        for(UUID uuid : main.getGameManager().getHiderMap().keySet()) {
            Entity entity = (Entity) Bukkit.getEntity(uuid);
            LivingEntity livingEntity = (LivingEntity) entity;
            if(livingEntity.hasLineOfSight(player)) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        if(e.getItemDrop().getItemStack().equals(FItem)) {
            e.setCancelled(true);
        }
    }

    public static boolean getRunning() {
        return running;
    }

}
