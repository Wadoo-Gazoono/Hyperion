package com.arathain.vigorem.anim;


import com.arathain.vigorem.api.anim.entity.AnimatedEntity;
import com.wadoo.hyperion.Hyperion;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.Collection;

public class EntityAnimationSyncPacket {
    public static final ResourceLocation ID = new ResourceLocation(Hyperion.MODID, "entity_animation_sync");


    public static void send(Entity entity, CompoundTag animNbt, Collection<ServerPlayer> e) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

        if(entity instanceof AnimatedEntity) {
            buf.writeInt(entity.getId());
            buf.writeNbt(animNbt);
        }

        //ServerPlayNetworking.send(e, ID, buf);
    }

    public static void handle(Minecraft cli, FriendlyByteBuf buf) {
        int entityId = buf.isReadable() ? buf.readInt() : -1;
        CompoundTag toRead = buf.isReadable() ? buf.readNbt() : null;
        cli.execute(() -> {
            if(cli.level.getEntity(entityId) instanceof AnimatedEntity e) {
                e.readAnimNbt(toRead);
            }
        });
    }
}