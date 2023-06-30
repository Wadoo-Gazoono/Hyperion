//package com.wadoo.hyperion.common.entities.fedran.attacks;
//
//import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
//import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;
//import com.wadoo.hyperion.common.entities.fedran.FedranEntity;
//import net.minecraft.world.phys.Vec3;
//
//public class JumpAttack extends AnimatedAttack {
//    Vec3 vec;
//
//    public JumpAttack(HyperionLivingEntity entity, int state, String animation, int animLength) {
//        super(entity, state, animation, animLength);
//    }
//
//    @Override
//    public void doEffects(int currentTick) {
//        super.doEffects(currentTick);
//        this.entity.getNavigation().stop();
//        if (this.entity.getTarget() == null) return;
//        this.entity.setDeltaMovement(0d,this.entity.getDeltaMovement().y,0d);
//        if (currentTick == 20) {
//            vec = new Vec3((this.entity.getRandom().nextFloat() * 2 -1) *4,1.3f,(this.entity.getRandom().nextFloat() * 2 -1) *4);
//        }
//        if (currentTick == 26){
//            this.entity.setDeltaMovement(vec);
//            if (this.entity instanceof FedranEntity) ((FedranEntity) this.entity).setJump(true);
//        }
//    }
//}
