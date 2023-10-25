package com.wadoo.hyperion.common.entities.agol;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class AgolSpeakerEntity extends AgolModuleEntity{
    public AgolSpeakerEntity(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
        this.energyCost = 2;
    }
}
