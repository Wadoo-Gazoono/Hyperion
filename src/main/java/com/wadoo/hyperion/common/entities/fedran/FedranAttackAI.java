package com.wadoo.hyperion.common.entities.fedran;

import com.wadoo.hyperion.common.entities.CapslingEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class FedranAttackAI extends Goal {
    private final FedranEntity fedran;

    private int repath;
    private double targetX;
    private double targetY;
    private double targetZ;

    public int FJ_cooldown = 20;
    public int HS_cooldown = 20;
    public int STOMP_cooldown = 20;
    public int SS_cooldown = 40;


    public FedranAttackAI(FedranEntity fedran) {
        this.fedran = fedran;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.fedran.getTarget();
        return target != null && target.isAlive() && this.fedran.getAnimation() == 0;
    }

    @Override
    public void start() {
        this.repath = 0;
    }

    @Override
    public void stop() {
        this.fedran.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity target = this.fedran.getTarget();
        if (target == null) return;

        FJ_cooldown--;
        HS_cooldown--;
        STOMP_cooldown--;
        SS_cooldown--;

        double dist = this.fedran.distanceToSqr(this.targetX, this.targetY, this.targetZ);
        this.fedran.getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (--this.repath <= 0 && (
                this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D ||
                        target.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D) ||
                this.fedran.getNavigation().isDone()
        ) {
            this.targetX = target.getX();
            this.targetY = target.getY();
            this.targetZ = target.getZ();
            this.repath = 4 + this.fedran.getRandom().nextInt(7);
            if (dist > 32.0D * 32.0D) {
                this.repath += 10;
            } else if (dist > 16.0D * 16.0D) {
                this.repath += 5;
            }
            if (!this.fedran.getNavigation().moveTo(target, 1.3D)) {
                this.repath += 15;
            }
        }
        dist = this.fedran.distanceToSqr(this.targetX, this.targetY, this.targetZ);
        if (Math.sqrt(dist) < 6f) this.fedran.getNavigation().stop();
        if (target.getY() - this.fedran.getY() >= -1 && target.getY() - this.fedran.getY() <= 3) {
            //Attack Checker
            if(SS_cooldown <= 0 && Math.sqrt(this.fedran.distanceToSqr(targetX,targetY,targetZ)) < 18.5){
                this.fedran.setAnimation(5);
                SS_cooldown = this.fedran.getRandom().nextInt(10) + 10;
            }

//            if(FJ_cooldown <= 0 && Math.sqrt(this.fedran.distanceToSqr(targetX,targetY,targetZ)) < 11.5){
//                this.fedran.setAnimation(2);
//                FJ_cooldown = this.fedran.getRandom().nextInt(10) + 10;
//            }
//
//            if(STOMP_cooldown <= 0 && Math.sqrt(this.fedran.distanceToSqr(targetX,targetY,targetZ)) < 8.5){
//                this.fedran.setAnimation(3);
//                STOMP_cooldown = this.fedran.getRandom().nextInt(10) + 10;
//            }
//
//            if(HS_cooldown <= 0 && Math.sqrt(this.fedran.distanceToSqr(targetX,targetY,targetZ)) < 9.5){
//                this.fedran.setAnimation(4);
//                HS_cooldown = this.fedran.getRandom().nextInt(15) + 15;
//            }
        }
    }
}
