package com.brofian.improvedambience.client.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ConfigMenuScreen extends Screen {

    private final boolean showMenu;

    public ConfigMenuScreen() {
        super(Text.translatable("impamb.menu.config.title"));
        this.showMenu = true;
    }

    protected void init() {
        if (this.showMenu) {
            this.initWidgets();
        }
    }


    private void initWidgets() {
        if(this.client == null || this.client.player == null) {
            return;
        }

        int yPos = this.height / 4;
        int xPos = 50;

        // return to game
        yPos += 24;
        this.addDrawableChild(new ButtonWidget(xPos, yPos-16, 98, 20, Text.translatable("menu.returnToGame"), (button) -> {
            this.client.setScreen((Screen)null);
            this.client.mouse.lockCursor();
        }));

        yPos += 24;
        this.addDrawableChild(new ButtonWidget(xPos, yPos-16, 98, 20, Text.translatable("impamb.menu.config.btn.firefly"), (button) -> {
            this.client.setScreen(new FireflyConfigScreen(this, this.client.options));
        }));

    }

    public void tick() {
        super.tick();
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.showMenu) {
            this.renderBackgroundTexture(0);
            this.renderBackground(matrices);
            drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 40, 16777215);
        } else {
            drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 10, 16777215);
        }

        super.render(matrices, mouseX, mouseY, delta);
    }

}
