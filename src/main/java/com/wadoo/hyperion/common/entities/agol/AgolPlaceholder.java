package com.wadoo.hyperion.common.entities.agol;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AgolPlaceholder extends Entity {
    private static final EntityDataAccessor<Byte> ID = SynchedEntityData.defineId(AgolPlaceholder.class, EntityDataSerializers.BYTE);


    public AgolPlaceholder(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ID,(byte)0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        setAgolId(pCompound.getByte("id"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putByte("id", getAgolId());
    }

    public void setAgolId(byte id){
        this.entityData.set(ID, id);
    }

    public byte getAgolId(){
        return this.entityData.get(ID);
    }
}
