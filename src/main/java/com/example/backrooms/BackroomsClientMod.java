package com.example.backrooms;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class BackroomsClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register client receiver for config sync from server
        ClientPlayNetworking.registerGlobalReceiver(SyncConfigS2CPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                BackroomsConfig.current = payload.config();
            });
        });
    }
}
