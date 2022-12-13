package com.wadoo.hyperion.common.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class GruskHeadEntity extends Monster implements IAnimationTickable, IAnimatable {
    private AnimationFactory factory = GeckoLibUtil.createFactory((IAnimatable) this);

    public GruskHeadEntity(EntityType<? extends Monster> monster, Level level) {
        super(monster, level);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<GruskHeadEntity>(this, "controller", 2, this::predicate));
    }

    private PlayState predicate(AnimationEvent<GruskHeadEntity> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("attack", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 15.0D).add(Attributes.MOVEMENT_SPEED, 0.28F).add(Attributes.KNOCKBACK_RESISTANCE, 0.1D).add(Attributes.ATTACK_DAMAGE,4f);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public int tickTimer() {
        return tickCount;
    }
}
