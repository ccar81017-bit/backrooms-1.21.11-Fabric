package com.example.backrooms;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ConfigSyncPayload(BackroomsConfig.Data config) implements CustomPayload {
    public static final CustomPayload.Id<ConfigSyncPayload> ID = new CustomPayload.Id<>(Identifier.of(BackroomsMod.MOD_ID, "config_sync"));
    
    // Simple codec mapping using JSON string representation over network
    public static final PacketCodec<RegistryByteBuf, ConfigSyncPayload> CODEC = PacketCodec.of(
        (value, buf) -> {
            com.google.gson.Gson gson = new com.google.gson.Gson();
            buf.writeString(gson.toJson(value.config));
        },
        buf -> {
            com.google.gson.Gson gson = new com.google.gson.Gson();
            String json = buf.readString();
            BackroomsConfig.Data data = gson.fromJson(json, BackroomsConfig.Data.class);
            return new ConfigSyncPayload(data);
        }
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
