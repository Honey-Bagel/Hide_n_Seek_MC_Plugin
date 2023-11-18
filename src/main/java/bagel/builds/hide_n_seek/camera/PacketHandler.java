package bagel.builds.hide_n_seek.camera;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.npc.NPCEvent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class PacketHandler {

    private CameraManager cameraManager;
    public PacketHandler(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    public void inject(Player player, Main main) {
        ChannelDuplexHandler channelHandler = new ChannelDuplexHandler(){
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object rawPacket) throws Exception {
                if(rawPacket instanceof ServerboundInteractPacket) {
                    ServerboundInteractPacket packet = (ServerboundInteractPacket) rawPacket;

                    Field type = packet.getClass().getDeclaredField("b");
                    type.setAccessible(true);
                    Object typeData = type.get(packet);
                    if (typeData.toString().split("\\$")[1].charAt(0) != 'e') {
                        if (typeData.toString().split("\\$")[1].charAt(0) == 'b') {

                        }

                        try {
                            Field hand = typeData.getClass().getDeclaredField("a");
                            hand.setAccessible(true);
                            if (!hand.get(typeData).toString().equals("MAIN_HAND")) {
                                return;
                            }
                        } catch (NoSuchFieldException x) {

                        }
                        /*What happens with this packet here*/
                        Field id = packet.getClass().getDeclaredField("a");
                        id.setAccessible(true);
                        int entityID = id.getInt(packet);


                        Bukkit.getScheduler().runTask(main, () -> {
                            Bukkit.getPluginManager().callEvent(new NPCEvent(player, entityID, attack));
                        });

                    }
                }
                super.channelRead(ctx, rawPacket);
            }


        };

        ChannelPipeline pipeline = getChannel(((CraftPlayer) player).getHandle().connection).pipeline();
        pipeline.addBefore("packet_handler", player.getName(), channelHandler);
    }

    public void stop(Player player) {
        Channel channel = getChannel(((CraftPlayer) player).getHandle().connection);
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
    }

    private Field connectionField;
    private Field channelField;

    private Channel getChannel(final ServerGamePacketListenerImpl playerConnection) {
        try {
            if (connectionField == null) {
                connectionField = ServerGamePacketListenerImpl.class.getDeclaredField("h"); // You have to get the "h" or AKA obfuscated name of "connection". since, reflection operations cannot be reobfuscated.
                connectionField.setAccessible(true);
            }
            if (channelField == null) {
                channelField = Connection.class.getDeclaredField("m"); // You have to get the "m" or AKA obfuscated name of "channel". since, reflection operations cannot be reobfuscated.
                channelField.setAccessible(true);
            }
            return (Channel) channelField.get(connectionField.get(playerConnection)); // packetlistenerimpl -> connection(h) -> channel(m)
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
