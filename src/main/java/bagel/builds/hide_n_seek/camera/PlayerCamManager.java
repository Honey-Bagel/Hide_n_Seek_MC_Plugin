package bagel.builds.hide_n_seek.camera;

import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerCamManager {

    private CameraManager cameraManager;
    private Player player;
    private int currentCamera;
    private CameraClass cameraClass;
    private List<CameraClass> nearbyCameras;


    public PlayerCamManager(CameraManager cameraManager, Player player, CameraClass cameraClass) {
        this.cameraManager = cameraManager;
        this.player = player;
        this.currentCamera = Integer.parseInt(null);
        this.cameraClass = cameraClass;
        this.nearbyCameras = getNearbyCameras(player);
    }

    public void setCamera(CameraClass camera, Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        CraftEntity entity = (CraftEntity) camera.getEntity();
        ServerPlayer serverPlayer = craftPlayer.getHandle();

        ServerGamePacketListenerImpl connection = serverPlayer.connection;
        connection.send(new ClientboundSetCameraPacket(entity.getHandle()));
        player.setGameMode(GameMode.SPECTATOR);
        cameraManager.getIsInCameras().put(player.getUniqueId(), true);
        new CameraViewListener(cameraManager);
    }

    public void exitCamera(Player player) {
        if(cameraManager.getIsInCameras().containsKey(player.getUniqueId())) {
            cameraManager.getIsInCameras().remove(player.getUniqueId());

            CraftPlayer craftPlayer = (CraftPlayer) player;
            ServerPlayer serverPlayer = craftPlayer.getHandle();

            ServerGamePacketListenerImpl connection = serverPlayer.connection;
            connection.send(new ClientboundSetCameraPacket(serverPlayer));
        }
    }

    public List<CameraClass> getNearbyCameras(Player player) {
        List<Entity> entities = player.getNearbyEntities(50, 50, 50);
        List<CameraClass> nearbyCameras = new ArrayList<>();
        for(CameraClass c : cameraManager.getCameras()) {
            if(entities.contains(c)) {
                nearbyCameras.add(c);
            }
        }
        return nearbyCameras;
    }

    public List<Entity> getNearbyCamerasAsEntity(Player player) {
        List<Entity> entities = player.getNearbyEntities(50, 50, 50);
        List<Entity> nearbyCameras = new ArrayList<>();
        for(CameraClass c : cameraManager.getCameras()) {
            if(entities.contains(c)) {
                nearbyCameras.add(c.getEntity());
            }
        }
        return nearbyCameras;
    }

}
