package com.example.examplemod.util;

import com.example.examplemod.ExampleMod;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ModWoodType {
    public static final WoodType TORCH = WoodType.register(new WoodType(ExampleMod.MODID + ":torch", BlockSetType.OAK));
}