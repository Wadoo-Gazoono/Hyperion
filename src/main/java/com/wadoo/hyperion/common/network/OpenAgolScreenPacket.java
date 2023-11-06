package com.wadoo.hyperion.common.network;

import com.wadoo.hyperion.HyperionClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenAgolScreenPacket {
     private final int containerId;
     private final int size;
     private final int entityId;

     public OpenAgolScreenPacket(int containerIdIn, int sizeIdIn, int entityIdIn) {
         this.containerId = containerIdIn;
         this.size = sizeIdIn;
         this.entityId = entityIdIn;
     }

    public static OpenAgolScreenPacket read(FriendlyByteBuf buf) {
        int containerId = buf.readUnsignedByte();
        int size = buf.readVarInt();
        int entityId = buf.readInt();
        return new OpenAgolScreenPacket(containerId, size, entityId);
    }

    public static void write(OpenAgolScreenPacket packet, FriendlyByteBuf buf) {
        buf.writeByte(packet.containerId);
        buf.writeVarInt(packet.size);
        buf.writeInt(packet.entityId);
    }

    public static void handle(OpenAgolScreenPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> HyperionClient.openAgolInventoryScreen(packet));
        ctx.get().setPacketHandled(true);
    }

    public int getContainerId() {
        return this.containerId;
    }

    public int getSize() {
        return this.size;
    }

    public int getEntityId() {
        return this.entityId;
    }

}
