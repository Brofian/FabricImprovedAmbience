package com.brofian.improvedambience.config;

import com.brofian.improvedambience.ImprovedAmbience;
import com.mojang.datafixers.util.Pair;

public class ModConfigs {
    public static SimpleConfig CONFIG;
    private static ModConfigProvider configs;
    public static double FIREFLY_SPAWN_AMPLIFIER_WILD;
    public static double FIREFLY_SPAWN_AMPLIFIER_PACK;
    public static int FIREFLY_PACK_DISTANCE_MIN;

    public static void registerConfigs() {
        configs = new ModConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of(ImprovedAmbience.MOD_ID + "config").provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        //configs.addKeyValuePair(new Pair<>("key.test.value1", "Just a Testing string!"), "String");
        configs.addKeyValuePair(new Pair<>("firefly.spawn_amplifier.wild", 1.0), "double");
        configs.addKeyValuePair(new Pair<>("firefly.spawn_amplifier.pack", 1.0), "double");
        configs.addKeyValuePair(new Pair<>("firefly.pack_distance.min", 20), "int");


    }

    private static void assignConfigs() {
        FIREFLY_SPAWN_AMPLIFIER_WILD = CONFIG.getOrDefault("firefly.spawn_amplifier.wild", 1.0);
        FIREFLY_SPAWN_AMPLIFIER_PACK = CONFIG.getOrDefault("firefly.spawn_amplifier.pack", 1.0);
        FIREFLY_PACK_DISTANCE_MIN = CONFIG.getOrDefault("firefly.pack_distance.min", 20);

        System.out.println("All " + configs.getConfigsList().size() + " have been set properly");
    }
}
