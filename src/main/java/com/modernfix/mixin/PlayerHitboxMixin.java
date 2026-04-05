package com.modernfix.mixin;

import com.modernfix.client.ModernFixClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPlayerEntity.class)
public class PlayerHitboxMixin {
    
    @ModifyArg(method = "getWidth", at = @At(value = "RETURN"), index = 0)
    private float onGetWidth(float original) {
        if (ModernFixClient.hitboxExpand > 0) {
            return original + ModernFixClient.hitboxExpand;
        }
        return original;
    }
}
