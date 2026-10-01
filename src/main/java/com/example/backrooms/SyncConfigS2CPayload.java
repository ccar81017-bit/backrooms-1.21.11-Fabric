package com.example.backrooms;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SyncConfigS2CPayload(BackroomsConfig.Data config) implements CustomPayload {
    public static final CustomPayload.Id<SyncConfigS2CPayload> ID = new CustomPayload.Id<>(Identifier.of(BackroomsMod.MOD_ID, "sync_config_s2c"));

    public static final PacketCodec<RegistryByteBuf, SyncConfigS2CPayload> CODEC = PacketCodec.of(
        (value, buf) -> {
            com.google.gson.Gson gson = new com.google.gson.Gson();
            buf.writeString(gson.toJson(value.config));
        },
        buf -> {
            com.google.gson.Gson gson = new com.google.gson.Gson();
            String json = buf.readString();
            BackroomsConfig.Data data = gson.fromJson(json, BackroomsConfig.Data.class);
            return new SyncConfigS2CPayload(data);
        }
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
