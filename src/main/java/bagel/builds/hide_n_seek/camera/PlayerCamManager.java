package bagel.builds.hide_n_seek.camera;

import bagel.builds.hide_n_seek.Main;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlayerCamManager implements Listener {

    private Main main;
    private CameraManager cameraManager;
    private Player player;
    private ServerPlayer npc;
    private Entity npcEnt;
    private List<CameraClass> nearbyCameras;
    private CameraClass curCamClass;
    private CameraClass lastCam;
    private int currentCamera;
    private List<BukkitTask> tasks;
    private Location location;
    private GameMode gameMode;
    private double health;

    public PlayerCamManager(Main main, CameraManager cameraManager, Player player) {
        this.main = main;
        this.cameraManager = cameraManager;
        this.player = player;
        this.currentCamera = -1;
        this.nearbyCameras = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }

    public void setCamera(CameraClass camera) {
        if(curCamClass != null) {
            exitCamera();
        }
        this.curCamClass = camera;
        if(this.location == null) {
            this.location = player.getLocation();
        }
        if(this.gameMode == null) {
            this.gameMode = player.getGameMode();
        }
        if(this.health == -1) {
            this.health = player.getHealth();
        }
        Bukkit.getPluginManager().registerEvents(this, main);

        createDummy();
        player.setGameMode(GameMode.SPECTATOR);
        player.setSpectatorTarget(curCamClass.getViewEntity());
        player.hideEntity(main, curCamClass.getEntity());
    }

    public void exitCamera() {
        removeDummy();
        player.setGameMode(gameMode);
        player.teleport(location);
        this.gameMode = null;
        this.location = null;
        player.showEntity(main, curCamClass.getEntity());
        lastCam = curCamClass;
        curCamClass = null;

        HandlerList.unregisterAll(this);
        cancelTasks();
    }

    public void nextCam() {
        setCamera(nearbyCameras.get(nearbyCameras.indexOf(curCamClass) + 1));
    }

    public void prevCam() {
        setCamera(nearbyCameras.get(nearbyCameras.indexOf(curCamClass) - 1));
    }

    public void reset() {
        if(curCamClass != null) {
            exitCamera();
        }
        lastCam = null;
    }

    public List<CameraClass> getNearbyCameras() {
        List<Entity> entities = player.getNearbyEntities(50, 50, 50);
        List<CameraClass> nearbyCameras = new ArrayList<>();
        for(CameraClass c : cameraManager.getCameras()) {
            if(entities.contains(c.getEntity())) {
                nearbyCameras.add(c);
            }
        }
        this.nearbyCameras = nearbyCameras;
        return nearbyCameras;
    }

    public List<Entity> getNearbyCamerasAsEntity() {
        List<Entity> entities = player.getNearbyEntities(50, 50, 50);
        List<Entity> nearbyCameras = new ArrayList<>();
        for(CameraClass c : cameraManager.getCameras()) {
            if(entities.contains(c)) {
                nearbyCameras.add(c.getEntity());
            }
        }
        return nearbyCameras;
    }

    public void cancelTasks() {
        for(BukkitTask task : tasks) {
            task.cancel();
        }
    }

    public void damagePlayer() {
        player.damage(1);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, SoundCategory.PLAYERS, 1, 1);
        player.sendHurtAnimation(0);
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        if(e.getPlayer().equals(player) && curCamClass != null) {
                exitCamera();
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemSwap(PlayerItemHeldEvent e) {
        if(e.getPlayer().equals(player) && curCamClass != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if((e.getPlayer().equals(player) || e.getPlayer().equals(curCamClass.getEntity()) )&& curCamClass != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if(e.getPlayer().equals(player)) {
            if(e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                nextCam();
            } else if(e.getAction().equals(Action.LEFT_CLICK_AIR)) {
                prevCam();
            }
        }
    }

    public void createDummy() {
        boolean sneaking = player.isSneaking();
        boolean swimming = player.isSwimming();

        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();

        GameProfile profile = new GameProfile(UUID.randomUUID(), player.getName() + "-fake");
        profile.getProperties().put("textures", craftPlayer.getProfile().getProperties().get("textures").iterator().next());

        npc = new ServerPlayer(serverPlayer.getServer(), serverPlayer.serverLevel(), profile);

        npc.setPos(location.getX(), location.getY(), location.getZ());
        npcEnt = npc.getBukkitEntity();
        npcEnt.setPersistent(true);
        Player eAsPlayer = (Player) npcEnt;
        eAsPlayer.setSneaking(sneaking);
        eAsPlayer.setSwimming(swimming);

        ServerGamePacketListenerImpl connection = serverPlayer.connection;
        connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc));
        connection.send(new ClientboundAddPlayerPacket(npc));

        SynchedEntityData.DataItem<Byte> dataItem = new SynchedEntityData.DataItem<>(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) (0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40));
        serverPlayer.connection.send(new ClientboundSetEntityDataPacket(npc.getBukkitEntity().getEntityId(), List.of(dataItem.value())));

        float yaw = 1f;
        float pitch = 1f;
        connection.send(new ClientboundRotateHeadPacket(npc, (byte) ((yaw % 360) * 256 /360)));
        connection.send(new ClientboundMoveEntityPacket.Rot(
                npc.getBukkitEntity().getEntityId(),
                (byte) ((yaw % 360) * 256 / 360),
                (byte) ((pitch % 360) * 256 / 360),
                true));

        Bukkit.getScheduler().runTaskLater(main, () -> {
            connection.send(new ClientboundPlayerInfoRemovePacket(Arrays.asList(npc.getUUID())));
        }, 20);
    }

    public void removeDummy() {
        npc.kill();
    }

    public Entity getEntity() { return npcEnt; }

}
