package com.example.examplemod.entities.render;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entities.noname_arrow;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class noname_arrow_renderer extends ArrowRenderer<noname_arrow> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MODID, "textures/entity/noname_arrow.png");

    public noname_arrow_renderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    public ResourceLocation getTextureLocation(noname_arrow arrow) {
        return TEXTURE;
    }
}