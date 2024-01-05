package com.wadoo.hyperion.common.entities.grusk;

import com.wadoo.hyperion.common.entities.CapslingEntity;
import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;
import com.wadoo.hyperion.common.registry.SoundsRegistry;
import com.wadoo.hyperion.common.util.GrabAnimation;
import com.wadoo.hyperion.common.util.GrabKeyframe;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class GruskGrabGoal extends AnimatedAttack {
    GrabAnimation grabAnim = new GrabAnimation();
    protected Vec3 lookVec = new Vec3(0,0,0);


    public GruskGrabGoal(HyperionLivingEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
        grabAnim.addKeyFrame(0,new GrabKeyframe(0, 6.25, -19.75));
        grabAnim.addKeyFrame(3,new GrabKeyframe(0, 6.25, -19.75));
        grabAnim.addKeyFrame(7,new GrabKeyframe(0, 6.25, -19.75));
        grabAnim.addKeyFrame(10,new GrabKeyframe(0, 17.75, -17.75));
        grabAnim.addKeyFrame(13,new GrabKeyframe(2, 25.75, -0.75));
        grabAnim.addKeyFrame(17,new GrabKeyframe(2, 25.75, -0.75));
        grabAnim.addKeyFrame(20,new GrabKeyframe(2, 25.75, -0.75));
        grabAnim.addKeyFrame(23,new GrabKeyframe(2, 25.75, -0.75));
        grabAnim.addKeyFrame(27,new GrabKeyframe(2, 25.75, -0.75));
        grabAnim.addKeyFrame(30,new GrabKeyframe(2, 25.75, -0.75));
        grabAnim.addKeyFrame(33,new GrabKeyframe(4.75, 26.25, -2.75));
        grabAnim.addKeyFrame(37,new GrabKeyframe(4.75, 21.25, -14.25));
        grabAnim.addKeyFrame(40,new GrabKeyframe(10, 4, -3));
    }

    @Override
    public void start() {
        super.start();
        lookVec = grabAnim.getKeyframes().get(0).toWorldCoords(this.entity);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        entity.getNavigation().stop();
        if(this.entity.getTarget() == null)return;
        LivingEntity target = this.entity.getTarget();
        entity.getLookControl().setLookAt(target);
        if(grabAnim.isOnKeyframe(currentTick) != null&& entity.distanceTo(target) < 3.2f){
            if(target instanceof CapslingEntity capsling){
                target.noPhysics = true;
                if(currentTick > 12) {
                    entity.playSound(SoundsRegistry.GRUSK_EAT.get());
                    capsling.hurt(entity.level().damageSources().mobAttack(entity), 0);
                }
                if (currentTick >= 35) {
                    capsling.playSound(SoundsRegistry.CAPSLING_DEATH.get());
                    capsling.discard();
                }
                if(currentTick > 2) {
                    capsling.triggerAnim("animController", "eaten");
                }
            }
            target.setDeltaMovement(0d,0d,0d);
            target.setPos(grabAnim.getKeyframes().get(currentTick).toWorldCoords(this.entity));
        }
    }

    @Override
    public void stop() {
        super.stop();
    }
}
