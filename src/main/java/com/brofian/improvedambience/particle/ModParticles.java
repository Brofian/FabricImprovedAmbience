package com.brofian.improvedambience.particle;

import com.brofian.improvedambience.ImprovedAmbience;
import com.brofian.improvedambience.particle.custom.FireflySpawnController;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/*
 * Created by: Fabian Holzwarth / 2022
 * https://github.com/Brofian
 * info@fabianholzwarth.de
 */

public class ModParticles {

    public static final DefaultParticleType FIREFLY_PARTICLE = FabricParticleTypes.simple();

    public static void registerParticles() {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(ImprovedAmbience.MOD_ID, "firefly_particle"), FIREFLY_PARTICLE);

        FireflySpawnController.registerListeners();
    }



}
