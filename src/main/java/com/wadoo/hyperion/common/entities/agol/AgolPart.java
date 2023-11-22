package com.wadoo.hyperion.common.entities.agol;

import com.wadoo.hyperion.common.entities.fedran.FedranEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.entity.PartEntity;

//Credit to Superlord for reference for this code

public class AgolPart extends PartEntity<AbstractAgolEntity> {
    public final AbstractAgolEntity parent;
    public final String name;
    public final EntityDimensions dimensions;
    public AgolPart(AbstractAgolEntity parent, String name, float width, float height) {
        super(parent);
        this.parent = parent;
        this.name = name;
        this.dimensions = EntityDimensions.scalable(width,height);
        this.refreshDimensions();
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return this.isInvulnerableTo(source) ? false : this.parent.hurt(source,amount);
    }
    @Override
    public boolean is(Entity entity) {
        return this == entity || parent == entity;
    }


    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.dimensions;
    }

    @Override
    public void tick() {
        super.tick();
    }
}
