package com.brofian.improvedambience;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImprovedAmbience implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("impamb");

    @Override
    public void onInitialize() {

        LOGGER.info("Initiallized Improved Ambience!");
    }
}