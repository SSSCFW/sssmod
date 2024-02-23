package com.example.examplemod.entities.render;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entities.explosion_arrow;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class explosion_arrow_renderer extends ArrowRenderer<explosion_arrow> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MODID, "textures/entity/explosion_arrow.png");

    public explosion_arrow_renderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    public ResourceLocation getTextureLocation(explosion_arrow arrow) {
        return TEXTURE;
    }
}