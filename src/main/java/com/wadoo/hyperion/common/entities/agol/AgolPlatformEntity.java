package com.wadoo.hyperion.common.entities.agol;

import com.wadoo.hyperion.common.items.ModuleItem;
import com.wadoo.hyperion.common.registry.EntityHandler;
import com.wadoo.hyperion.common.registry.ItemHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AgolPlatformEntity extends AgolModuleEntity{
    public AgolPlatformEntity(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
        this.energyCost = 2;
    }

    protected void positionRider(Entity entity, MoveFunction moveFunction) {
        int i = this.getPassengers().indexOf(entity);
        Vec3 posVec = Vec3.ZERO;
        switch(i){
            case 0:
                posVec = (new Vec3(-0.0D, 0.0D, 0.0D)).yRot(-this.yBodyRot * ((float)Math.PI / 180F));
                break;
            case 1:
                posVec = (new Vec3(-1.125D, 0.0D, -1.125D)).yRot(-this.yBodyRot * ((float)Math.PI / 180F));
                break;
            case 2:
                posVec = (new Vec3(1.125D, 0.0D, -1.125D)).yRot(-this.yBodyRot * ((float)Math.PI / 180F));
                break;
            case 3:
                posVec = (new Vec3(-1.125D, 0.0D, 1.125D)).yRot(-this.yBodyRot * ((float)Math.PI / 180F));
                break;
            case 4:
                posVec = (new Vec3(1.125D, 0.0D, 1.125D)).yRot(-this.yBodyRot * ((float)Math.PI / 180F));
                break;
        }
        moveFunction.accept(entity, this.getX() + posVec.x, this.getY() + 1d, this.getZ() + posVec.z);
    }

    protected boolean canAddPassenger(Entity entity) {
        return this.getPassengers().size() <= 5;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if(this.getPassengers().size() < 5) {
            if(player.getItemInHand(hand).getItem() instanceof ModuleItem && !player.getItemInHand(hand).is(ItemHandler.AGOL_WALKER_MODULE.get())){
                LivingEntity module = (LivingEntity) ((ModuleItem) player.getItemInHand(hand).getItem()).getType().create(this.level());
                module.moveTo(this.position().add(0d,1d,0d));
                this.level().addFreshEntity(module);
                module.startRiding(this);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
