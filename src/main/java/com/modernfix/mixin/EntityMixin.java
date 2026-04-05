package com.modernfix.mixin;

import com.modernfix.client.ModernFixClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public class EntityMixin {
    
    @Redirect(method = "getWidth", at = @At(value = "RETURN"))
    private float onGetWidth(float original) {
        if (!ModernFixClient.hitboxEnabled) return original;
        if ((Object)this instanceof net.minecraft.client.network.ClientPlayerEntity) {
            return original + ModernFixClient.hitboxExpand;
        }
        return original;
    }
    
    @Redirect(method = "getHeight", at = @At(value = "RETURN"))
    private float onGetHeight(float original) {
        if (!ModernFixClient.hitboxEnabled) return original;
        if ((Object)this instanceof net.minecraft.client.network.ClientPlayerEntity) {
            return original + ModernFixClient.hitboxExpand * 0.5f;
        }
        return original;
    }
}
