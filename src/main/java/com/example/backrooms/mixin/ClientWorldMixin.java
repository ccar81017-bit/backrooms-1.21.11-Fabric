package com.example.backrooms.mixin;

import com.example.backrooms.BackroomsConfig;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(method = "getSkyDarkness", at = @At("HEAD"), cancellable = true)
    private void modifySkyDarkness(float tickDelta, CallbackInfoReturnable<Float> cir) {
        if (BackroomsConfig.current.eventActive) {
            cir.setReturnValue(0.9f); // Dense darkness for Backrooms atmosphere
        }
    }
}
