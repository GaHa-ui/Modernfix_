package com.modernfix.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModernFixClient implements ClientModInitializer {
    public static final String MOD_ID = "modernfix";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static float hitboxExpand = 0.0f;
    public static float targetExpand = 0.0f;
    private KeyBinding expandKey;
    private KeyBinding resetKey;

    @Override
    public void onInitializeClient() {
        LOGGER.info("ModernFix initialized!");
        
        expandKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.modernfix.expand",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            KeyBinding.Category.MISC
        ));

        resetKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.modernfix.reset",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_L,
            KeyBinding.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            if (expandKey.wasPressed()) {
                targetExpand += 0.1f;
                LOGGER.info("Hitbox Expand: " + targetExpand);
            }

            if (resetKey.wasPressed()) {
                targetExpand = 0.0f;
                LOGGER.info("Hitbox Reset to default");
            }

            float speed = 0.02f;
            if (hitboxExpand < targetExpand) {
                hitboxExpand = Math.min(hitboxExpand + speed, targetExpand);
            } else if (hitboxExpand > targetExpand) {
                hitboxExpand = Math.max(hitboxExpand - speed, targetExpand);
            }
        });
    }
}
