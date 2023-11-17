package bagel.builds.hide_n_seek.classes.type.classutil;

import bagel.builds.hide_n_seek.Main;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class SkinUtil {

    private Main main;
    private Player player;
    private Collection<PotionEffect> effects;
    private Location location;
    private int slot;
    private boolean flying;
    private float walkspeed;
    private float flyspeed;

    public SkinUtil(Main main, Player player) {
        this.main = main;
        this.player = player;
    }

    public void changeSkin(Property skinProp) {

        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();

        GameProfile profile = craftPlayer.getProfile();
        PropertyMap pm = profile.getProperties();
        Property property = pm.get("textures").iterator().next();
        pm.remove("textures", property);
        pm.put("textures", skinProp);
        updateSkin();
    }

    public void resetSkin(Property oldSkin) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();

        GameProfile profile = craftPlayer.getProfile();
        PropertyMap pm = profile.getProperties();
        Property property = pm.get("textures").iterator().next();
        pm.remove("textures", property);
        pm.put("textures", oldSkin);
        updateSkin();
    }

    public void updateSkin() {
        effects = player.getActivePotionEffects();
        location = player.getLocation();
        slot = player.getInventory().getHeldItemSlot();
        flying = player.isFlying();
        flyspeed = player.getFlySpeed();
        walkspeed = player.getWalkSpeed();

        CraftWorld world = (CraftWorld) location.getWorld();
        CraftPlayer craftPlayer = ((CraftPlayer) player);
        ServerPlayer serverPlayer = craftPlayer.getHandle();
        serverPlayer.connection.send(new ClientboundPlayerInfoRemovePacket(Arrays.asList(player.getUniqueId())));
        serverPlayer.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, serverPlayer));
        serverPlayer.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED, serverPlayer));
        serverPlayer.connection.send(new ClientboundRespawnPacket(world.getHandle().getLevel().dimensionTypeId(), world.getHandle().getLevel().dimension(), world.getSeed(), getGameType(), getGameType(), false, false, Byte.parseByte("0"), Optional.empty() , 20));

        SynchedEntityData.DataItem<Byte> dataItem = new SynchedEntityData.DataItem<>(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) (0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40));
        serverPlayer.connection.send(new ClientboundSetEntityDataPacket(serverPlayer.getId(), List.of(dataItem.value())));

        player.teleport(location);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.hidePlayer(this.player);
            player.showPlayer(this.player);
        }


        Bukkit.getScheduler().runTaskLater(main, () -> {
            player.getInventory().setHeldItemSlot(slot);
            player.addPotionEffects(effects);
            player.setExp(player.getExp());
            player.setHealth(player.getHealth()-0.0001);
            player.openInventory(player.getEnderChest());
            player.closeInventory();
            player.setFlying(flying);
            player.setWalkSpeed(walkspeed);
            player.setFlySpeed(flyspeed);
        }, 2);
    }

    public GameType getGameType() {
        switch (player.getGameMode()) {
            case SURVIVAL:
                return GameType.SURVIVAL;
            case CREATIVE:
                return GameType.CREATIVE;
            case SPECTATOR:
                return GameType.SPECTATOR;
            default:
                return GameType.ADVENTURE;
        }
    }
}
