/*
 * NeoForge port of Better Mount HUD by Lortseam (https://modrinth.com/mod/better-mount-hud),
 * licensed GPL-3.0. Original mixin logic by Lortseam, adapted for NeoForge's HUD.
 */
package io.github.mddarmawan.bettermounthud.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Hud.class)
public abstract class HudMixin {
    @Shadow @Final private Minecraft minecraft;

    @Shadow
    private LivingEntity getPlayerVehicleWithHealth() { return null; }

    @Shadow
    private int getVehicleMaxHearts(LivingEntity entity) { return 0; }

    @ModifyVariable(method = "extractVehicleHealth", at = @At(value = "STORE"), name = "yLine1")
    private int bettermounthud$moveMountHealthUp(int y) {
        if (minecraft.gameMode.canHurtPlayer()) {
            y -= 10;
        }
        return y;
    }

    @Redirect(method = "extractFoodLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Hud;getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I"))
    private int bettermounthud$alwaysRenderFood(Hud hud, LivingEntity entity) {
        return 0;
    }

    @ModifyVariable(method = "getAirBubbleYLine", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
    private int bettermounthud$useMountHeartsForAir(int vehicleHearts) {
        LivingEntity entity = getPlayerVehicleWithHealth();
        if (entity != null) {
            return getVehicleMaxHearts(entity);
        }
        return vehicleHearts;
    }
}
