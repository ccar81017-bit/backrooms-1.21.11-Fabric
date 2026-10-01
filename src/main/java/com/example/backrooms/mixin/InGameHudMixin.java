package com.example.backrooms.mixin;

import com.example.backrooms.BackroomsConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void renderBackroomsHud(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (BackroomsConfig.current.eventActive) {
            // Render HUD overlay / fragment tracker
            int width = context.getScaledWindowWidth();
            context.drawTextWithShadow(
                net.minecraft.client.MinecraftClient.getInstance().textRenderer,
                "§e[Backrooms Event Active]",
                width - 150, 10, 0xFF5555
            );
        }
    }
}
