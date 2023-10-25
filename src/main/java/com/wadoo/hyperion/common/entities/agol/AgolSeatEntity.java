package com.wadoo.hyperion.common.entities.agol;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AgolSeatEntity extends AgolModuleEntity{
    public AgolSeatEntity(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
        this.energyCost = 2;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if(player.getItemInHand(hand).isEmpty()){
            player.startRiding(this);
        }
        return InteractionResult.FAIL;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.22d;
    }

}
