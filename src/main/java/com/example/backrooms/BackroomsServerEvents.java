package com.example.backrooms;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class BackroomsServerEvents {
    private static final Random RANDOM = new Random();

    public static void onServerTick(MinecraftServer server) {
        if (!BackroomsConfig.current.eventActive) return;

        // Iterate online players for ambient sound triggers and sudden jumpscares
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            if (BackroomsConfig.current.enableSounds) {
                // Rare creaks (e.g. ~0.5% chance per tick)
                if (RANDOM.nextDouble() < 0.005) {
                    double offsetX = (RANDOM.nextDouble() - 0.5) * 30;
                    double offsetY = (RANDOM.nextDouble() - 0.5) * 10;
                    double offsetZ = (RANDOM.nextDouble() - 0.5) * 30;
                    player.getWorld().playSound(
                        null,
                        player.getX() + offsetX, player.getY() + offsetY, player.getZ() + offsetZ,
                        SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.AMBIENT, 0.7f, 0.5f
                    );
                }

                // Sudden loud noises (jumpscare)
                if (RANDOM.nextDouble() < BackroomsConfig.current.suddenNoiseFrequency) {
                    player.getWorld().playSound(
                        null,
                        player.getBlockPos(),
                        SoundEvents.ENTITY_WARDEN_HEARTBEAT, SoundCategory.RECORDS, 1.5f, 0.8f
                    );
                }
            }
        }
    }
}
