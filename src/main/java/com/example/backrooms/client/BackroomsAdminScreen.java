package com.example.backrooms.client;

import com.example.backrooms.BackroomsConfig;
import com.example.backrooms.ConfigSyncPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;

public class BackroomsAdminScreen extends Screen {
    private final Screen parent;

    public BackroomsAdminScreen(Screen parent) {
        super(Text.literal("Backrooms Event Control Panel"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Toggle Event Start/Stop Button
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Event Active: " + BackroomsConfig.current.eventActive),
            button -> {
                BackroomsConfig.current.eventActive = !BackroomsConfig.current.eventActive;
                button.setMessage(Text.literal("Event Active: " + BackroomsConfig.current.eventActive));
            })
            .dimensions(centerX - 100, centerY - 50, 200, 20)
            .build()
        );

        // Toggle Flicker Effect Checkbox
        this.addDrawableChild(CheckboxWidget.builder(Text.literal("Enable Lamp Flicker"), this.textRenderer)
            .pos(centerX - 100, centerY - 20)
            .checked(BackroomsConfig.current.enableFlicker)
            .callback((checkbox, checked) -> BackroomsConfig.current.enableFlicker = checked)
            .build()
        );

        // Save & Sync Button
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Save & Sync to Server"),
            button -> {
                ClientPlayNetworking.send(new ConfigSyncPayload(BackroomsConfig.current));
                if (this.client != null) {
                    this.client.setScreen(this.parent);
                }
            })
            .dimensions(centerX - 100, centerY + 40, 200, 20)
            .build()
        );

        // Close / Cancel Button
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Cancel"),
            button -> {
                if (this.client != null) {
                    this.client.setScreen(this.parent);
                }
            })
            .dimensions(centerX - 100, centerY + 70, 200, 20)
            .build()
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
