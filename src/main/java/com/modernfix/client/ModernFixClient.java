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

    public static boolean hitboxEnabled = false;
    public static float hitboxExpand = 0.0f;
    public static float targetExpand = 0.0f;
    
    private KeyBinding expandKey;
    private KeyBinding resetKey;
    private KeyBinding toggleKey;

    private int ticksSinceAttack = 999;
    private float lastExpand = 0.15f;

    @Override
    public void onInitializeClient() {
        LOGGER.info("ModernFix loaded!");
        LOGGER.info("P = Expand | L = Reset | K = Toggle");
        
        expandKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.modernfix.expand",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            "key.categories.misc"
        ));

        resetKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.modernfix.reset",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_L,
            "key.categories.misc"
        ));
        
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.modernfix.toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "key.categories.misc"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            if (toggleKey.wasPressed()) {
                hitboxEnabled = !hitboxEnabled;
                if (!hitboxEnabled) {
                    targetExpand = 0f;
                } else {
                    targetExpand = lastExpand;
                }
                LOGGER.info("Hitbox: " + (hitboxEnabled ? "ON" : "OFF"));
            }

            if (expandKey.wasPressed()) {
                targetExpand += 0.05f;
                if (targetExpand > 0.5f) targetExpand = 0.5f;
                lastExpand = targetExpand;
                hitboxEnabled = true;
                LOGGER.info("Expand: " + String.format("%.2f", targetExpand));
            }

            if (resetKey.wasPressed()) {
                targetExpand = 0.0f;
                lastExpand = 0.15f;
                LOGGER.info("Reset");
            }

            ticksSinceAttack++;
            if (client.options.attackKey.isPressed()) {
                ticksSinceAttack = 0;
            }

            float speed = 0.02f;
            
            if (ticksSinceAttack < 3) {
                speed = 0.15f;
            }
            
            if (hitboxExpand < targetExpand) {
                hitboxExpand = Math.min(hitboxExpand + speed, targetExpand);
            } else if (hitboxExpand > targetExpand) {
                hitboxExpand = Math.max(hitboxExpand - speed, targetExpand);
            }
        });
    }
}
