package com.brofian.improvedambience;

import com.brofian.improvedambience.particle.ModParticles;
import com.brofian.improvedambience.particle.custom.FireflyParticle;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

import java.io.Console;

public class ImprovedAmbience implements ModInitializer {

    public static final String MOD_ID = "impamb";

    @Override
    public void onInitialize() {
        System.out.println("Starting improved ambience mod");

        ModParticles.registerParticles();
        ParticleFactoryRegistry.getInstance().register(ModParticles.FIREFLY_PARTICLE, FireflyParticle.Factory::new);

        System.out.println("Initialized improved ambience mod");
    }
}