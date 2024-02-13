package com.example.examplemod.entities.render;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entities.torch_arrow;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class torch_arrow_renderer extends ArrowRenderer<torch_arrow> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MODID, "textures/entity/torch_arrow.png");

    public torch_arrow_renderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    public ResourceLocation getTextureLocation(torch_arrow arrow) {
        return TEXTURE;
    }
}