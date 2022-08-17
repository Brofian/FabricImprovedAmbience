package com.brofian.improvedambience.particle.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

public class FireflyParticle extends SpriteBillboardParticle {

    private double[] motionState            = new double[] { 0.0F, 0.0F, 0.0F};
    private double[] motionStateMultiplier  = new double[] { 0.0F, 0.0F, 0.0F};
    private double[] motionStateIncrease    = new double[] { 0.0F, 0.0F, 0.0F};

    private final static float flickeringConstant = 16F; // should be a multiple of 2. Higher values give shorter flickering

    private int spawningLightLevel = 0;

    protected FireflyParticle(ClientWorld clientWorld, double x, double y, double z, SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(clientWorld, x, y, z, xd, yd, zd);

        this.spawningLightLevel = clientWorld.getLightLevel(LightType.BLOCK, new BlockPos(x,y,z));

        this.velocityMultiplier = 0.6F;
        this.scale *= 0.75F;
        this.x = xd;
        this.y = yd;
        this.z = zd;
        this.maxAge = 200;

        this.motionState[0] = Math.random() * Math.PI;
        this.motionState[1] = Math.random() * Math.PI;
        this.motionState[2] = Math.random() * Math.PI;

        this.motionStateMultiplier[0] = Math.random() * 0.0025;
        this.motionStateMultiplier[1] = Math.random() * 0.025;
        this.motionStateMultiplier[2] = Math.random() * 0.025;

        this.motionStateIncrease[0] = Math.random() * 0.05;
        this.motionStateIncrease[1] = Math.random() * 0.05;
        this.motionStateIncrease[2] = Math.random() * 0.05;


        this.red = 1.0F;
        this.blue = 1.0F;
        this.green = 1.0F;

        this.setSpriteForAge(spriteSet);
    }

    @Override
    public int getBrightness(float tint) {
        // turn down the brightness now and then
        double flicker = 1 - Math.pow(Math.sin(this.motionState[0]*2),FireflyParticle.flickeringConstant);
        // create value from 0 to 255 -> dim this value by the spawning light level. Higher light level means lower brightness
        return (int)(255 * flicker) * ((15 - this.spawningLightLevel) / 15);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();

        this.velocityY = Math.sin(this.motionState[0]) * Math.abs(this.motionStateMultiplier[0]);
        this.velocityX = Math.sin(this.motionState[1]) * Math.abs(this.motionStateMultiplier[1]);
        this.velocityZ = Math.sin(this.motionState[2]) * Math.abs(this.motionStateMultiplier[2]);

        this.motionState[0] += this.motionStateIncrease[0];
        this.motionState[1] += this.motionStateIncrease[1];
        this.motionState[2] += this.motionStateIncrease[2];


        // only fade out at the end of the lifespan
        fadeOut();
    }

    private void fadeOut() {
        float threshold = this.maxAge * (float) 0.8;

        if(this.age > threshold) {
            float usedAge = this.age - threshold;
            float maxUsedAge = this.maxAge * 0.2F;
            this.alpha = (-(1/maxUsedAge) * (usedAge) + 1);
        }
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
