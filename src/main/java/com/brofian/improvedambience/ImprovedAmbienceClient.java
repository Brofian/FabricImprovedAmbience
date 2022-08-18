package com.brofian.improvedambience;

import com.brofian.improvedambience.client.gui.screen.ConfigMenuScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ImprovedAmbienceClient implements ClientModInitializer {

    private static KeyBinding openConfigKeyBinding;

    @Override
    public void onInitializeClient() {
        this.registerKeyBindings();
    }

    public void registerKeyBindings() {
        openConfigKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "impamb.key.configscreen", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_I, // The keycode of the key
                "impamb.category.config" // The translation key of the keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(client.player != null) {
                if(openConfigKeyBinding.wasPressed()) {
                    client.setScreen(new ConfigMenuScreen());
                }
            }
        });
    }
}
