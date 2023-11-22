package com.wadoo.hyperion.common.entities.grusk;

import com.wadoo.hyperion.common.entities.CapslingEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;


public class GruskAttackAI extends Goal {
    private final GruskEntity grusk;

    private int repath;
    private double targetX;
    private double targetY;
    private double targetZ;

    private double leapCoolDown = 0;
    private double roarCoolDown = 0;
    private double grabCoolDown = 0;

    private int attacksSinceVertical;
    private int timeSinceStomp;

    public GruskAttackAI(GruskEntity grusk) {
        this.grusk = grusk;
        //this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.grusk.getTarget();
        return target != null && target.isAlive() && this.grusk.getState() == 0;
    }

    @Override
    public void start() {
        this.repath = 0;
    }

    @Override
    public void stop() {
        this.grusk.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity target = this.grusk.getTarget();
        leapCoolDown--;
        roarCoolDown--;
        grabCoolDown--;
        if(!this.grusk.hasHead()){
           roarCoolDown = 400;
           grabCoolDown = 400;
        }
        if (target == null) return;



        double dist = this.grusk.distanceToSqr(this.targetX, this.targetY, this.targetZ);
        this.grusk.getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (--this.repath <= 0 && (
                this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D ||
                        target.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D) ||
                this.grusk.getNavigation().isDone()
        ) {
            this.targetX = target.getX();
            this.targetY = target.getY();
            this.targetZ = target.getZ();
            this.repath = 4 + this.grusk.getRandom().nextInt(7);
            if (dist > 32.0D * 32.0D) {
                this.repath += 10;
            } else if (dist > 16.0D * 16.0D) {
                this.repath += 5;
            }
            if (!this.grusk.getNavigation().moveTo(target, 1.3D)) {
                this.repath += 15;
            }
        }
        dist = this.grusk.distanceToSqr(this.targetX, this.targetY, this.targetZ);
        if (target.getY() - this.grusk.getY() >= -1 && target.getY() - this.grusk.getY() <= 3 && grusk.decapitateTimer == 0) {
            //Attack Checker

            if(Math.sqrt(this.grusk.distanceToSqr(targetX,targetY,targetZ)) < 2.5){
                if(leapCoolDown < 0 && (!(this.grusk.getTarget() instanceof CapslingEntity) || (this.grusk.getTarget() instanceof CapslingEntity && !this.grusk.hasHead()))) {
                    this.grusk.setAnimation(1);
                    leapCoolDown = this.grusk.getRandom().nextInt(13) + 5;
                }
            }
            if(Math.sqrt(this.grusk.distanceToSqr(targetX,targetY,targetZ)) < 0.8){
                if(grabCoolDown < 0 && (this.grusk.getTarget() instanceof CapslingEntity && this.grusk.hasHead())) {
                    this.grusk.setAnimation(3);
                    grabCoolDown = this.grusk.getRandom().nextInt(23) + 15;
                }
            }
            if(
                    Math.sqrt(this.grusk.distanceToSqr(targetX,targetY,targetZ)) > 12.5){
                if(roarCoolDown < 0) {
                    this.grusk.setAnimation(2);
                    roarCoolDown = this.grusk.getRandom().nextInt(50) + 50;
                }
            }
        }
        this.timeSinceStomp++;
    }
}
