package com.brofian.improvedambience.particle.custom;

import com.brofian.improvedambience.particle.ModParticles;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.world.BlockEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FireflySpawnController {


    public static BlockPos[] fireflyPacks = new BlockPos[32];

    public static final float wildFireflySpawnChance = 0.003F;
    public static final float fireflyPackSpawnChance = 0.002F;
    public static final float packFireflySpawnChance = 0.5F;
    public static final float fireflyPackDimension   = 2.3F;
    public static final int minPackDistance   = 10;
    public static final int maxPackDistanceBase  = 100;


    public static void registerListeners() {
        ClientTickEvents.END_CLIENT_TICK.register(FireflySpawnController::onClientTick);
    }

    public static void onClientTick(MinecraftClient client) {
        if(client.isPaused()) {
            return;
        }

        ClientPlayerEntity player = client.player;
        ClientWorld world = client.world;

        if(player == null || world == null) {
            return;
        }

        long timeOfDay = world.getTimeOfDay();
        if(timeOfDay < 12000) {
            return;
        }

        int loadedChunkCount = world.getChunkManager().getLoadedChunkCount();
        int maxWildFirefliesPerTick = loadedChunkCount * 5;
        int maxPackFirefliesPerTick = (int)(Math.sqrt(loadedChunkCount) * 0.5F);

        int renderDistance = (int)client.worldRenderer.getViewDistance() * 16;

        // generate wild fireflies
        for(int i = 0; i < maxWildFirefliesPerTick; i++) {
            if(Math.random() < FireflySpawnController.wildFireflySpawnChance) {
                BlockPos randomPos = FireflySpawnController.getRandomBlockPosInRadius(player.getBlockPos(), renderDistance);
                BlockPos topPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, randomPos).add(0,0.2+Math.random()*3,0);
                if(FireflySpawnController.isValidSpawnArea(topPos, world)) {
                    client.particleManager.addParticle(ModParticles.FIREFLY_PARTICLE, topPos.getX(), topPos.getY(), topPos.getZ(), 0, 0,0);
                }
            }
        }

        int existingPacks = FireflySpawnController.cleanupFireflyPacks(player.getBlockPos(), FireflySpawnController.minPackDistance, Math.max(renderDistance, FireflySpawnController.maxPackDistanceBase));

        // generate firefly packs
        if(existingPacks < maxPackFirefliesPerTick && Math.random() < FireflySpawnController.fireflyPackSpawnChance) {
            BlockPos randomPos = FireflySpawnController.getRandomBlockPosInRadius(player.getBlockPos(), (float)Math.random()*renderDistance);
            BlockPos topPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, randomPos).add(0,1+Math.random()*2,0);

            if(FireflySpawnController.isValidSpawnArea(topPos, world)) {
                for(int i = 0; i < FireflySpawnController.fireflyPacks.length; i++) {
                    if(FireflySpawnController.fireflyPacks[i] == null) {
                        FireflySpawnController.fireflyPacks[i] = topPos;
                        break;
                    }
                }
            }
        }


        // add fireflies to pack
        for(int i = 0; i < FireflySpawnController.fireflyPacks.length; i++) {
            if(FireflySpawnController.fireflyPacks[i] != null && Math.random() < FireflySpawnController.packFireflySpawnChance) {
                BlockPos packCenter = FireflySpawnController.fireflyPacks[i];
                BlockPos spawnPos = FireflySpawnController.getRandomBlockPosInRadius(packCenter, FireflySpawnController.fireflyPackDimension, true);

                client.particleManager.addParticle(ModParticles.FIREFLY_PARTICLE, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 0, 0,0);
            }
        }

    }


    public static boolean isValidSpawnArea(BlockPos position, World world) {

        List<RegistryKey<Biome>> allowedBiomes = new LinkedList<>();
        allowedBiomes.add(BiomeKeys.SWAMP);
        allowedBiomes.add(BiomeKeys.MANGROVE_SWAMP);

        Optional<RegistryKey<Biome>> biomeToCheck = world.getBiome(position).getKey();

        for(RegistryKey<Biome> biome : allowedBiomes.stream().toList()) {
            if(biomeToCheck.isPresent() && biomeToCheck.get().equals(biome)) {
                return true;
            }
        }

        return false;
    }

    public static BlockPos getRandomBlockPosInRadius(BlockPos center, float maxDist) {
        return FireflySpawnController.getRandomBlockPosInRadius(center, maxDist, false);
    }

    public static BlockPos getRandomBlockPosInRadius(BlockPos center, float maxDist, Boolean includeY) {
        double x = center.getX() + (Math.cos(Math.random()*Math.PI) * maxDist);
        double z = center.getZ() + (Math.cos(Math.random()*Math.PI) * maxDist);
        double y = includeY ? (center.getY() + (Math.cos(Math.random()*Math.PI) * maxDist)) : 0;

        return new BlockPos(x,y,z);
    }

    public static int cleanupFireflyPacks(BlockPos playerPos, int minDistance, int maxDistance) {
        int existingPacks = 0;
        for (int i = 0; i < FireflySpawnController.fireflyPacks.length; i++) {
            if(FireflySpawnController.fireflyPacks[i] == null) {
                continue;
            }

            int distToPlayer = (int)Math.sqrt(playerPos.getSquaredDistance(FireflySpawnController.fireflyPacks[i]));
            if(distToPlayer < minDistance || distToPlayer > maxDistance) {
                // remove pack
                FireflySpawnController.fireflyPacks[i] = null;
            }
            else {
                existingPacks++;
            }
        }
        return existingPacks;
    }

}
