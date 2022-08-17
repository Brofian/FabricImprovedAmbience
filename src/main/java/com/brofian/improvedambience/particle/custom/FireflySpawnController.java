package com.brofian.improvedambience.particle.custom;

import com.brofian.improvedambience.particle.ModParticles;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;

public class FireflySpawnController {


    public static BlockPos[] fireflyPacks = new BlockPos[32];

    public static final float wildFireflySpawnChance = 0.001F;
    public static final float fireflyPackSpawnChance = 0.001F;
    public static final float packFireflySpawnChance = 0.3F;
    public static final float fireflyPackDimension   = 1.7F;


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

        int loadedChunkCount = world.getChunkManager().getLoadedChunkCount();
        int maxWildFirefliesPerTick = loadedChunkCount * 5;
        int maxPackFirefliesPerTick = (int)(Math.sqrt(loadedChunkCount) * 0.5F);

        int renderDistance = (int)client.worldRenderer.getViewDistance() * 16;

        // generate wild fireflies
        for(int i = 0; i < maxWildFirefliesPerTick; i++) {
            if(Math.random() < FireflySpawnController.wildFireflySpawnChance) {
                BlockPos randomPos = FireflySpawnController.getRandomBlockPosInRadius(player.getBlockPos(), renderDistance);
                BlockPos topPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, randomPos).add(0,0.2+Math.random()*2,0);
                client.particleManager.addParticle(ModParticles.FIREFLY_PARTICLE, topPos.getX(), topPos.getY(), topPos.getZ(), 0, 0,0);
            }
        }

        int existingPacks = FireflySpawnController.cleanupFireflyPacks(player.getBlockPos(), 10, Math.max(renderDistance, 100));

        // generate firefly packs
        if(existingPacks < maxPackFirefliesPerTick && Math.random() < FireflySpawnController.fireflyPackSpawnChance) {
            BlockPos randomPos = FireflySpawnController.getRandomBlockPosInRadius(player.getBlockPos(), (float)Math.random()*renderDistance);
            BlockPos topPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, randomPos).add(0,1+Math.random()*2,0);

            for(int i = 0; i < FireflySpawnController.fireflyPacks.length; i++) {
                if(FireflySpawnController.fireflyPacks[i] == null) {
                    FireflySpawnController.fireflyPacks[i] = topPos;
                    break;
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
