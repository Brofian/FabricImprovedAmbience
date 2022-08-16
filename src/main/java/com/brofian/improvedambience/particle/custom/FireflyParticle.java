package com.brofian.improvedambience.particle.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class FireflyParticle extends SpriteBillboardParticle {

    protected FireflyParticle(ClientWorld clientWorld, double x, double y, double z, SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(clientWorld, x, y, z, xd, yd, zd);

        this.velocityMultiplier = 0.6F;
        this.scale = 0.75F;
        this.maxAge = 20;

        this.red = 1.0F;
        this.blue = 1.0F;
        this.green = 1.0F;

        this.setSpriteForAge(spriteSet);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        fadeOut();
    }

    private void fadeOut() {
        this.alpha = (-(1/(float)maxAge) * age + 1);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(DefaultParticleType particleType, ClientWorld level, double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new FireflyParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}
