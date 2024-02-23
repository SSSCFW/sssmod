package com.example.examplemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.world.entity.monster.ZombieVillager;

@Mixin(ZombieVillager.class)
public interface ZombieVillagerAccessor {

    @Invoker("startConverting")
    public void startConvertingAccessor(@Nullable UUID uuid, int time);
}

