package com.ltcg.keyholder.fabric;

import com.ltcg.keyholder.KeyHolderCore;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class KeyHolderFabric implements ClientModInitializer {
    public static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath("keyholder", "main"));

    public static KeyHolderCore CORE;
    private static KeyMapping toggleKeyBinding;

    @Override
    public void onInitializeClient() {
        var configPath = FabricLoader.getInstance().getConfigDir().resolve("keyholder.json");
        CORE = new KeyHolderCore(configPath, (keyName, pressed) ->
                KeyMapping.set(InputConstants.getKey(keyName), pressed));

        toggleKeyBinding = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.keyholder.toggle",
                InputConstants.UNKNOWN.getValue(),
                CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            CORE.clientTick();
            while (toggleKeyBinding.consumeClick()) {
                sendFeedback(CORE.toggle());
            }
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(literal("keyholder")
                .then(literal("on").executes(ctx -> feedback(ctx, CORE.setEnabled(true))))
                .then(literal("off").executes(ctx -> feedback(ctx, CORE.setEnabled(false))))
                .then(literal("toggle").executes(ctx -> feedback(ctx, CORE.toggle())))
                .then(literal("status").executes(ctx -> feedback(ctx, CORE.status())))
                .then(literal("set")
                        .then(argument("key", StringArgumentType.word())
                                .executes(ctx -> feedback(ctx, CORE.setKey(normalizeKeyName(StringArgumentType.getString(ctx, "key")))))))
                .executes(ctx -> feedback(ctx, CORE.status()))));
    }

    /**
     * Accepts either a bare key name (e.g. "w", "space") or a full translation
     * key (e.g. "key.keyboard.left.shift", "key.mouse.left") as-is.
     */
    private static String normalizeKeyName(String raw) {
        String lower = raw.toLowerCase(java.util.Locale.ROOT);
        return lower.startsWith("key.") ? lower : "key.keyboard." + lower;
    }

    private static int feedback(CommandContext<FabricClientCommandSource> ctx, String message) {
        ctx.getSource().sendFeedback(Component.literal(message));
        return 1;
    }

    private static void sendFeedback(String message) {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.sendSystemMessage(Component.literal(message));
        }
    }
}
