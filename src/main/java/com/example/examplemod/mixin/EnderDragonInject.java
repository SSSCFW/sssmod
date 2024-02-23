package com.example.examplemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.dimension.end.EndDragonFight;
@Mixin(EndDragonFight.class)
public class EnderDragonInject {
    @Shadow public boolean previouslyKilled;
    
    @Inject(method = "setDragonKilled", at = @At(value = "HEAD"))
    public void setDragonKilledMixin(EnderDragon p_64086_, CallbackInfo info) {
        previouslyKilled = false;
    }
}
