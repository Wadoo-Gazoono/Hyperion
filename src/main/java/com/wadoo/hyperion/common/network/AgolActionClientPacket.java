package com.wadoo.hyperion.common.network;

import com.wadoo.hyperion.common.entities.agol.AbstractAgolEntity;
import com.wadoo.hyperion.common.entities.agol.AgolWalker;
import com.wadoo.hyperion.common.inventory.menu.agol.AbstractAgolMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class AgolActionClientPacket {
     private final byte inputId;

     public AgolActionClientPacket(byte inputId) {
         this.inputId = inputId;
     }

    public static AgolActionClientPacket read(FriendlyByteBuf buf) {
        byte input = buf.readByte();
        return new AgolActionClientPacket(input);
    }

    public static void write(AgolActionClientPacket packet, FriendlyByteBuf buf) {
        buf.writeByte(packet.inputId);
    }

    public static void handle(AgolActionClientPacket packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer serverPlayer = context.getSender();
            ServerLevel level = (ServerLevel) serverPlayer.level();
            if (serverPlayer.getRootVehicle() instanceof AgolWalker walker){
                walker.executeAction(packet.inputId);
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
