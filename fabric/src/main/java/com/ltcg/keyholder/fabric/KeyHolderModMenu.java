package com.ltcg.keyholder.fabric;

import com.ltcg.keyholder.KeyHolderConfig;
import com.mojang.blaze3d.platform.InputConstants;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

public class KeyHolderModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            KeyHolderConfig config = KeyHolderFabric.CORE.config();

            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("Key Holder"))
                .setSavingRunnable(() -> KeyHolderFabric.CORE.save());

            ConfigCategory category = builder.getOrCreateCategory(Component.literal("General"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            category.addEntry(entryBuilder.startBooleanToggle(Component.literal("Enabled"), config.enabled)
                .setDefaultValue(false)
                .setSaveConsumer(value -> config.enabled = value)
                .build());

            category.addEntry(entryBuilder.startKeyCodeField(Component.literal("Held Key"), InputConstants.getKey(config.heldKey))
                .setDefaultValue(InputConstants.UNKNOWN)
                .setAllowMouse(true)
                .setAllowModifiers(false)
                .setKeySaveConsumer(key -> config.heldKey = key.getName())
                .build());

            return builder.build();
        };
    }
}
