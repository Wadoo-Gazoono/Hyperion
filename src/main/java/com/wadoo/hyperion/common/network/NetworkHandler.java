package com.wadoo.hyperion.common.network;

import com.wadoo.hyperion.Hyperion;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class NetworkHandler {
     private static final String PROTOCOL_VERSION = "1";
        public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(
                        new ResourceLocation(Hyperion.MODID, "network"))
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .simpleChannel();

        protected static int packetID = 0;

        public NetworkHandler() {
        }

        public static <MSG> void sendToServer(MSG message) {
            INSTANCE.sendToServer(message);
        }

        public static void init() {
            INSTANCE.registerMessage(getPacketID(), OpenAgolScreenClientPacket.class, OpenAgolScreenClientPacket::write, OpenAgolScreenClientPacket::read, OpenAgolScreenClientPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));

            INSTANCE.registerMessage(getPacketID(), OpenAgolScreenPacket.class, OpenAgolScreenPacket::write, OpenAgolScreenPacket::read, OpenAgolScreenPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        }

        public static int getPacketID() {
            return packetID++;
        }
}
