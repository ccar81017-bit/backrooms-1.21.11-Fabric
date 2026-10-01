package com.example.backrooms;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BackroomsConfig {
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("backrooms_event.json").toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Data current = new Data();

    public static class Data {
        public boolean eventActive = false;
        public long seed = 12345L;
        public int zoneSize = 100;
        public int roomCount = 50;
        public double lootDensity = 0.5;
        public boolean enableFlicker = true;
        public boolean enableSounds = true;
        public boolean enableVisualEffects = true;
        public double entitySpawnChance = 0.05;
        public double suddenNoiseFrequency = 0.001;
        public double screenDistortionIntensity = 0.5;
        public int requiredFragments = 4;
    }

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                Data data = GSON.fromJson(reader, Data.class);
                if (data != null) {
                    current = data;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
    }

    public static void save() {
        try {
            CONFIG_FILE.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(current, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
