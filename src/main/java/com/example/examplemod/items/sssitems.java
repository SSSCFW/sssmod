package com.example.examplemod.items;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.blocks.sssblocks;
import com.example.examplemod.event.noname_bow;
import com.example.examplemod.event.torch_rod_event;
import com.example.examplemod.items.spcial.noname_arrow_item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class sssitems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExampleMod.MODID);
    
    @SuppressWarnings("null")
    public static final RegistryObject<Item> DIRT_FOOD = ITEMS.register("dirt_food", () -> new Item(
        new Item.Properties().food(
            new FoodProperties.Builder()
            .fast().alwaysEat().nutrition(1).saturationMod(20f).build()
            )
        )
    );
    public static final RegistryObject<Item> NONAME_BOW = ITEMS.register("noname_bow", () -> new noname_bow(
        new Item.Properties()
        .fireResistant()
        .durability(9999999)
        ));
    public static final RegistryObject<Item> TORCH_ROD = ITEMS.register("torch_rod", () -> new torch_rod_event(new Item.Properties()));
    public static final RegistryObject<Item> TORCH_BLOCK = ITEMS.register("torch_block", () -> new BlockItem(sssblocks.TORCH_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> IRON_FURNACE = ITEMS.register("iron_furnace", () -> new BlockItem(sssblocks.IRON_FURNACE.get(), new Item.Properties()));
    public static final RegistryObject<Item> SPIKE_BLOCK = ITEMS.register("spike_block", () -> new BlockItem(sssblocks.SPIKE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> NONAME_ARROW = ITEMS.register("noname_arrow",
            () -> new noname_arrow_item(new Item.Properties().fireResistant()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
