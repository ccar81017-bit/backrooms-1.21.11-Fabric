package com.example.backrooms;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackroomsMod implements ModInitializer {
    public static final String MOD_ID = "backrooms";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Backrooms Mod for Minecraft 1.21.1");

        // Load or initialize config
        BackroomsConfig.load();

        // Register Networking Payloads
        PayloadTypeRegistry.playC2S().id(ConfigSyncPayload.ID, ConfigSyncPayload.CODEC);
        PayloadTypeRegistry.playS2C().id(SyncConfigS2CPayload.ID, SyncConfigS2CPayload.CODEC);

        // Register Server Play Networking Receivers
        ServerPlayNetworking.registerGlobalReceiver(ConfigSyncPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                if (context.player().hasPermissionLevel(2)) {
                    BackroomsConfig.current = payload.config();
                    BackroomsConfig.save();
                    // Broadcast updated config to all clients
                    SyncConfigS2CPayload syncPacket = new SyncConfigS2CPayload(BackroomsConfig.current);
                    for (var player : context.server().getPlayerManager().getPlayerList()) {
                        ServerPlayNetworking.send(player, syncPacket);
                    }
                    context.player().sendMessage(Text.literal("§a[Backrooms] Settings updated successfully!"), false);
                }
            });
        });

        // Register Admin Command /backrooms menu
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("backrooms")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("menu")
                    .executes(context -> {
                        // Open menu on client side via packet or command feedback
                        context.getSource().sendFeedback(() -> Text.literal("§eUse the client menu interface or configure via config/backrooms_event.json"), false);
                        return 1;
                    })
                )
            );
        });

        // Server tick events for ambient sounds, random creaks, and event timers
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            BackroomsServerEvents.onServerTick(server);
        });
    }
}
