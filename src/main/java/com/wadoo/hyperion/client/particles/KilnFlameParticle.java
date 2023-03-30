package com.wadoo.hyperion.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.Random;

public class KilnFlameParticle extends TextureSheetParticle {
    static final Random RANDOM = new Random();
    private final SpriteSet sprites;

    KilnFlameParticle(ClientLevel p_172136_, double p_172137_, double p_172138_, double p_172139_, double p_172140_,
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
        //this.alpha-=0.03f;
        move(0d,0.06d,0d);
        this.quadSize = 0.5f;
        this.roll = 5;
        this.oRoll = this.roll;
        this.roll += ((float)Math.random() - 0.5F) * 0.1F * 5.0F;
        if (this.onGround) {
            this.oRoll = this.roll = 0.0F;
        }
        //move(Math.sin(lifetime * 8360)/80, this.level.getRandom().nextFloat()/80 * 1, Math.cos(lifetime * 8360)/80);
        //this.quadSize;
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

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x,
                                       double y, double z, double xdel, double ydel, double zdel) {
            KilnFlameParticle glowparticle = new KilnFlameParticle(level, x, y, z, xdel, ydel,
                    zdel, this.sprite);
            glowparticle.setSize(5f,5f);
            glowparticle.setParticleSpeed(xdel*10,0d,zdel*10);
            glowparticle.setLifetime(level.random.nextInt(12) + 12);
            return glowparticle;
        }
    }
}