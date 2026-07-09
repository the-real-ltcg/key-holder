package com.ltcg.keyholder;

import java.nio.file.Path;

public class KeyHolderCore {
    private final KeyHolderConfig config;
    private final Path configPath;
    private final KeySetter setter;

    public KeyHolderCore(Path configPath, KeySetter setter) {
        this.configPath = configPath;
        this.config = KeyHolderConfig.load(configPath);
        this.setter = setter;
    }

    public void clientTick() {
        if (config.enabled) {
            setter.setPressed(config.heldKey, true);
        }
    }

    public String setKey(String keyName) {
        release();
        config.heldKey = keyName;
        save();
        return "Key Holder will hold: " + keyName;
    }

    public String setEnabled(boolean enabled) {
        if (!enabled) {
            release();
        }
        config.enabled = enabled;
        save();
        return enabled
            ? "Key Holder is now holding " + config.heldKey + "."
            : "Key Holder stopped.";
    }

    public String toggle() {
        return setEnabled(!config.enabled);
    }

    public String status() {
        return String.format("Key Holder is %s. Held key: %s.",
                config.enabled ? "holding" : "idle", config.heldKey);
    }

    public KeyHolderConfig config() {
        return config;
    }

    public void save() {
        config.save(configPath);
    }

    private void release() {
        if (config.heldKey != null) {
            setter.setPressed(config.heldKey, false);
        }
    }
}
