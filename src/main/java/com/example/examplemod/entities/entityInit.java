package com.example.examplemod.entities;

import com.example.examplemod.ExampleMod;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class entityInit {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ExampleMod.MODID);

    public static final RegistryObject<EntityType<noname_arrow>> NONAME_ARROW = ENTITY_TYPES.register("noname_arrow",
            () -> EntityType.Builder.of((EntityType.EntityFactory<noname_arrow>) noname_arrow::new, MobCategory.MISC).sized(0.5F, 0.5F).build("noname_arrow"));
    public static final RegistryObject<EntityType<torch_arrow>> TORCH_ARROW = ENTITY_TYPES.register("torch_arrow",
            () -> EntityType.Builder.of((EntityType.EntityFactory<torch_arrow>) torch_arrow::new, MobCategory.MISC).sized(0.5F, 0.5F).build("torch_arrow"));

    public static final RegistryObject<EntityType<abyss>> ABYSS = ENTITY_TYPES.register("abyss",
            () -> EntityType.Builder.of((EntityType.EntityFactory<abyss>) abyss::new, MobCategory.MONSTER).build("abyss"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }



/* 
    public Zombie EntityAbyss(Level level) {
        Zombie zombie = new Zombie(level);
        CompoundTag tag = new CompoundTag();
        tag.putFloat("Health", 1200.0F);
        zombie.addAdditionalSaveData(tag);
        return zombie;
    }*/
}