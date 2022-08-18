package com.brofian.improvedambience.client.gui.screen;

import com.brofian.improvedambience.config.ModConfigProvider;
import com.brofian.improvedambience.config.ModConfigs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.SimpleOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class FireflyConfigScreen extends SimpleOptionsScreen {

    private static final SimpleOption firefly_spawn_amplifier_wild;

    public FireflyConfigScreen(Screen parent, GameOptions options) {
        super(parent, options, Text.translatable("impamb.menu.config.firefly.title"), FireflyConfigScreen.getSimpleOptions());
    }

    public static SimpleOption[] getSimpleOptions() {
        FireflyConfigScreen.initConfigFields();

        return new SimpleOption[]{
                FireflyConfigScreen.firefly_spawn_amplifier_wild,
        };
    }

    public static void initConfigFields() {
        FireflyConfigScreen.firefly_spawn_amplifier_wild = new SimpleOption("impamb.options.fireflies.wild_spawn_amplifier", SimpleOption.emptyTooltip(), (optionText, value) -> {
            return getPercentValueText(optionText, value * 0.9 + 0.1);
        }, SimpleOption.DoubleSliderCallbacks.INSTANCE, 1.0, (value) -> {
            ModConfigProvider.
        });
    }

    public static void configFieldChangeCallback() {

    }

    public void close() {
        this.client.setScreen(this.parent);
    }

    private static Text getPercentValueText(Text prefix, double value) {
        return Text.translatable("options.percent_value", prefix, (int)(value * 100.0));
    }
}
