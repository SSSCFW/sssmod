package com.example.examplemod.entities;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.blocks.sssblocks;
import com.example.examplemod.event.noname_bow;
import com.example.examplemod.event.torch_rod_event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class entityInit {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ExampleMod.MODID);

    public static final RegistryObject<EntityType<noname_arrow>> NONAME_ARROW = ENTITY_TYPES.register("noname_arrow",
            () -> EntityType.Builder.of((EntityType.EntityFactory<noname_arrow>) noname_arrow::new, MobCategory.MISC).sized(0.5F, 0.5F).build("noname_arrow"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}