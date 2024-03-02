package com.example.examplemod.entities.render;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entities.explosion_arrow;
import com.example.examplemod.entities.homing_arrow;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class homing_arrow_renderer extends ArrowRenderer<homing_arrow> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MODID, "textures/entity/homing_arrow.png");

    public homing_arrow_renderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    public ResourceLocation getTextureLocation(homing_arrow arrow) {
        return TEXTURE;
    }
}