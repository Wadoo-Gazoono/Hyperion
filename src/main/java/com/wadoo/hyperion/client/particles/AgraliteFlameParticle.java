package com.wadoo.hyperion.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

import java.util.Random;

public class AgraliteFlameParticle extends TextureSheetParticle {
    static final Random RANDOM = new Random();
    private final SpriteSet sprites;

    AgraliteFlameParticle(ClientLevel p_172136_, double p_172137_, double p_172138_, double p_172139_, double p_172140_,
                   double p_172141_, double p_172142_, SpriteSet p_172143_) {
        super(p_172136_, p_172137_, p_172138_, p_172139_, p_172140_, p_172141_, p_172142_);
        this.friction = 0.26F;
        this.speedUpWhenYMotionIsBlocked = true;
        this.sprites = p_172143_;
        this.quadSize *= 0.75F;
        this.hasPhysics = false;
        this.setSpriteFromAge(p_172143_);
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }


    public void tick() {
        super.tick();
        this.alpha-=0.03f;
        move(Math.sin(lifetime * 8360)/80, this.level.getRandom().nextFloat()/80 * 1, Math.cos(lifetime * 8360)/80);
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    protected int getLightColor(float p_107249_) {
        return 240;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Factory(SpriteSet p_172151_) {
            this.sprite = p_172151_;
        }

        public Particle createParticle(SimpleParticleType p_172162_, ClientLevel p_172163_, double p_172164_,
                                       double p_172165_, double p_172166_, double p_172167_, double p_172168_, double p_172169_) {
            AgraliteFlameParticle glowparticle = new AgraliteFlameParticle(p_172163_, p_172164_, p_172165_, p_172166_, 0.0D, 0.0D,
                    0.0D, this.sprite);
            glowparticle.setSize(2f,2f);
            glowparticle.setLifetime(p_172163_.random.nextInt(12) + 12);
            return glowparticle;
        }
    }
}