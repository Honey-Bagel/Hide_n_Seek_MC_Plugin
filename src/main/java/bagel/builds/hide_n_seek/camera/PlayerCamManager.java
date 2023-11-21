package bagel.builds.hide_n_seek.camera;

import bagel.builds.hide_n_seek.Main;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlayerCamManager implements Listener {

    private Main main;
    private CameraManager cameraManager;
    private Player player;
    private List<CameraClass> nearbyCameras;
    private CameraClass curCamClass;
    private int currentCamera;
    private List<BukkitTask> tasks;
    private Location location;
    private GameMode gameMode;

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
        this.location = player.getLocation();
        this.gameMode = player.getGameMode();
        Bukkit.getPluginManager().registerEvents(this, main);

        player.setGameMode(GameMode.SPECTATOR);
        player.setSpectatorTarget(curCamClass.getViewEntity());
        player.hideEntity(main, curCamClass.getEntity());
//        createDummy();
    }

    public void exitCamera() {
        player.setGameMode(gameMode);
        player.teleport(location);
        this.gameMode = null;
        this.location = null;
        player.showEntity(main, curCamClass.getEntity());
        //kill dummy

        HandlerList.unregisterAll(this);
        cancelTasks();
    }

    public void nextCam() {
        setCamera(nearbyCameras.get(nearbyCameras.indexOf(curCamClass) + 1));
    }

    public void prevCam() {
        setCamera(nearbyCameras.get(nearbyCameras.indexOf(curCamClass) - 1));
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

    public void createDummy() {
        boolean sneaking = player.isSneaking();
        boolean swimming = player.isSwimming();

        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();

        GameProfile profile = new GameProfile(UUID.randomUUID(), player.getName() + "-fake");
        profile.getProperties().put("textures", craftPlayer.getProfile().getProperties().get("textures").iterator().next());

        ServerPlayer npc = new ServerPlayer(serverPlayer.getServer(), serverPlayer.serverLevel(), profile);


        npc.setPos(location.getX(), location.getY(), location.getZ());
        Entity entity = npc.getBukkitEntity();
        entity.setPersistent(true);

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

    }

}
