package com.wadoo.hyperion.common.entities.ai.util;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public class AttackChooser extends Goal {
    private HyperionLivingEntity entity;
    private Attack attack;
    private int repath = 0;
    private double targetX;
    private double targetY;
    private double targetZ;

    public AttackChooser(HyperionLivingEntity entity, Attack attack){
        this.entity = entity;
        this.attack = attack;
        //this.setFlags(EnumSet.of(Flag.LOOK,Flag.MOVE,Flag.JUMP));
    }


    @Override
    public boolean canUse() {
        LivingEntity target = this.entity.getTarget();
        //System.out.println("start");
        return true;
        //return target != null && target.isAlive() && this.entity.getAnimation() == 0;
    }

    @Override
    public void start() {
        this.repath = 0;
       // this.entity.getNavigation().moveTo(this.entity.getTarget(), 1.2D);

    }

    @Override
    public void stop() {
        this.entity.getNavigation().stop();
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.entity.getTarget();
        if(target != null) {
        //System.out.println("start" + this.entity.tickCount);
//
//        double dist = this.entity.distanceToSqr(targetX, targetY, targetY);
//        this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
//
//        if (--this.repath <= 0 && (
//                this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D ||
//                        target.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D) ||
//                this.entity.getNavigation().isDone()
//        ) {
//            this.targetX = target.getX();
//            this.targetY = target.getY();
//            this.targetZ = target.getZ();
//            this.repath = 4 + this.entity.getRandom().nextInt(7);
//            if (dist > 32.0D * 32.0D) {
//                this.repath += 10;
//            } else if (dist > 16.0D * 16.0D) {
//                this.repath += 5;
//            }
            Path path = this.entity.getNavigation().createPath(this.entity.getTarget(), 0);
            if(this.entity.tickCount % 30 == 0) {
                this.entity.triggerAnim("controller", "leap");
                //this.entity.getLookControl().setLookAt(target);
                this.entity.setDeltaMovement(this.entity.position().vectorTo(this.entity.getTarget().position()).normalize().multiply(1.7f,0.6f,1.7f).add(0d,0.2d,0d));
            }
        }

    }
}
