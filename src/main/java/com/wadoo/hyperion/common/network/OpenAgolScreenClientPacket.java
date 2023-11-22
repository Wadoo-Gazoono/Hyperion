package com.wadoo.hyperion.common.network;

import com.wadoo.hyperion.HyperionClient;
import com.wadoo.hyperion.common.entities.agol.AbstractAgolEntity;
import com.wadoo.hyperion.common.inventory.menu.agol.AbstractAgolMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class OpenAgolScreenClientPacket {
     private final int entityId;
     private final byte direction;

     public OpenAgolScreenClientPacket(int entityIdIn, byte direction) {
         this.entityId = entityIdIn;
         this.direction = direction;
     }

    public static OpenAgolScreenClientPacket read(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        byte direction = buf.readByte();
        return new OpenAgolScreenClientPacket(entityId, direction);
    }

    public static void write(OpenAgolScreenClientPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.entityId);
        buf.writeByte(packet.direction);
    }

    public static void handle(OpenAgolScreenClientPacket packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer serverPlayer = context.getSender();
            ServerLevel level = (ServerLevel) serverPlayer.level();
            AbstractAgolEntity baseEntity = (AbstractAgolEntity) level.getEntity(packet.getEntityId());
            AbstractAgolEntity send_to_entity;
            //1 : UP
            //2 : DOWN
            //3 : LEFT
            //4 : RIGHT
            switch (packet.getDirection()){
                case 1:
                    send_to_entity = (AbstractAgolEntity) baseEntity.getAboveEntity();
                    break;
                case 2:
                    send_to_entity = (AbstractAgolEntity) baseEntity.getBelowEntity();
                    break;
                case 3:
                    send_to_entity = (AbstractAgolEntity) baseEntity.getLeftEntity();
                    break;
                case 4:
                    send_to_entity = (AbstractAgolEntity) baseEntity.getRightEntity();
                    break;
                default:
                    send_to_entity = baseEntity;
                    break;

            }
            if (!send_to_entity.level().isClientSide) {
                serverPlayer.nextContainerCounter();
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new OpenAgolScreenPacket(serverPlayer.containerCounter, send_to_entity.getInventorySize(), send_to_entity.getId()));
                serverPlayer.containerMenu = new AbstractAgolMenu(serverPlayer.containerCounter, serverPlayer.getInventory(), send_to_entity.inventory, send_to_entity, serverPlayer);
                serverPlayer.initMenu(serverPlayer.containerMenu);
                MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(serverPlayer, serverPlayer.containerMenu));
            }
        });
        ctx.get().setPacketHandled(true);
    }
    public byte getDirection() {
        return this.direction;
    }


    public int getEntityId() {
        return this.entityId;
    }

}
