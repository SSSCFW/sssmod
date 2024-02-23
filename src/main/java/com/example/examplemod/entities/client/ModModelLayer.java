package com.example.examplemod.entities.client;

import com.example.examplemod.ExampleMod;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayer {
    public static final ModelLayerLocation FLIGHT_BOAT_LAYER = new ModelLayerLocation(
            new ResourceLocation(ExampleMod.MODID, "boat/flight_boat"), "main");

}