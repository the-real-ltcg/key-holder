package com.ltcg.keyholder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class KeyHolderConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public String heldKey = "key.keyboard.unknown";
    public boolean enabled = false;

    public static KeyHolderConfig load(Path path) {
        if (Files.exists(path)) {
            try (var reader = Files.newBufferedReader(path)) {
                KeyHolderConfig loaded = GSON.fromJson(reader, KeyHolderConfig.class);
                if (loaded != null) {
                    return loaded.sanitize();
                }
            } catch (IOException e) {
                System.err.println("[KeyHolder] Failed to read config, using defaults: " + e.getMessage());
            }
        }
        KeyHolderConfig defaults = new KeyHolderConfig();
        defaults.save(path);
        return defaults;
    }

    public void save(Path path) {
        try {
            Files.createDirectories(path.getParent());
            try (var writer = Files.newBufferedWriter(path)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            System.err.println("[KeyHolder] Failed to save config: " + e.getMessage());
        }
    }

    private KeyHolderConfig sanitize() {
        if (heldKey == null || heldKey.isBlank()) {
            heldKey = "key.keyboard.unknown";
        }
        return this;
    }
}
