package bagel.builds.hide_n_seek.npc;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.Animatronic;
import bagel.builds.hide_n_seek.classes.Hider;
import bagel.builds.hide_n_seek.classes.Team;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class NPCCommand implements CommandExecutor {

    private Main main;
    private NPCManager npcManager;
    public NPCCommand(Main main, NPCManager npcManager) {
        this.main = main;
        this.npcManager = npcManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!(sender instanceof Player)) return false;

        Animatronic animatronic = null;
        Hider hider = null;
        Team team = null;
        String name = "";

        Player player = (Player) sender;
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();


        if(args.length == 2 && args[0].equalsIgnoreCase("animatronic")) {
            switch (args[1]) {
                case "bonnie":
                    animatronic = Animatronic.BONNIE;
                    break;
                case "foxy":
                    animatronic = Animatronic.FOXY;
                    break;
                case "freddy":
                    animatronic = Animatronic.FREDDY;
                    break;
                case "chica":
                    animatronic = Animatronic.CHICA;
                    break;
                case "mangle":
                    animatronic = Animatronic.MANGLE;
                    break;
                case "marionette":
                    animatronic = Animatronic.MARIONETTE;
                    break;
                case "gfreddy":
                    animatronic = Animatronic.GOLDEN_FREDDY;
                    break;
            }
            if(animatronic != null) {
                name = animatronic.getName();
            }

        } else if(args.length == 2 && args[0].equalsIgnoreCase("hider")) {

        } else if(args.length == 2 && args[0].equalsIgnoreCase("team")) {

        }

        GameProfile profile = new GameProfile(UUID.randomUUID(), name);
        profile.getProperties().put("textures", animatronic.getProperty());

        ServerPlayer npc = new ServerPlayer(serverPlayer.getServer(), serverPlayer.serverLevel(), profile);
        if(animatronic != null) {
            NPC myNPC = new NPC(name, npc.getId(), animatronic);
            npcManager.addNPC(myNPC);
        } else if(hider != null) {
            NPC myNPC = new NPC(name, npc.getId(), hider);
            npcManager.addNPC(myNPC);
        }
        npc.setPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());

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


        return false;
    }
}
