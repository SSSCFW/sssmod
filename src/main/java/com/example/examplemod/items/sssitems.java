package com.example.examplemod.items;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.blocks.sssblocks;
import com.example.examplemod.entities.entityInit;
import com.example.examplemod.entities.flight_boat;
import com.example.examplemod.event.noname_bow;
import com.example.examplemod.event.torch_rod_event;
import com.example.examplemod.items.spcial.flight_boat_item;
import com.example.examplemod.items.spcial.invincible_star;
import com.example.examplemod.items.spcial.noname_arrow_item;
import com.example.examplemod.items.spcial.reverse_star;
import com.example.examplemod.items.spcial.ssselytra;
import com.example.examplemod.items.spcial.torch_arrow_item;
import com.example.examplemod.items.spcial.zombie_star;

import net.minecraft.core.Direction;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.level.block.Blocks;
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
    public static final RegistryObject<Item> TORCH_ROD = ITEMS.register("torch_rod", () -> new torch_rod_event(Blocks.TORCH, Blocks.WALL_TORCH, new Item.Properties(), Direction.DOWN));
    public static final RegistryObject<Item> TORCH_BLOCK = ITEMS.register("torch_block", () -> new BlockItem(sssblocks.TORCH_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> IRON_FURNACE = ITEMS.register("iron_furnace", () -> new BlockItem(sssblocks.IRON_FURNACE.get(), new Item.Properties()));
    public static final RegistryObject<Item> SPIKE_BLOCK = ITEMS.register("spike_block", () -> new BlockItem(sssblocks.SPIKE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> IRON_SPIKE_BLOCK = ITEMS.register("iron_spike_block", () -> new BlockItem(sssblocks.IRON_SPIKE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> NONAME_ARROW = ITEMS.register("noname_arrow",
            () -> new noname_arrow_item(new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> TORCH_ARROW = ITEMS.register("torch_arrow",
            () -> new torch_arrow_item(new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> REVERSE_STAR = ITEMS.register("reverse_star", () -> new reverse_star(new Item.Properties()));
    public static final RegistryObject<Item> ZOMBIE_STAR = ITEMS.register("zombie_star", () -> new zombie_star(new Item.Properties()));
    public static final RegistryObject<Item> NETHER_PORTAL = ITEMS.register("nether_portal", () -> new BlockItem(Blocks.NETHER_PORTAL, new Item.Properties()));
    public static final RegistryObject<Item> FLIGHT_BOAT = ITEMS.register("flight_boat",
            () -> new flight_boat_item(flight_boat.Type.TORCH, new Item.Properties().fireResistant()));
    //public static final RegistryObject<Item> ABYSS = ITEMS.register("abyss_spawn", () -> new ForgeSpawnEggItem((Supplier<? extends EntityType<? extends Mob>>) entityInit.ABYSS.get(), 0, 0, new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_STICK = ITEMS.register("diamond_stick", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ORE_TELEPORT = ITEMS.register("ore_teleport", () -> new BlockItem(sssblocks.ORE_TELEPORT.get(), new Item.Properties()));
    public static final RegistryObject<Item> INVINCIBLE_STAR = ITEMS.register("invincible_star", () -> new invincible_star(new Item.Properties()));
    public static final RegistryObject<Item> ABYSS_PRIZE = ITEMS.register("abyss_prize", () -> new Item(new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> ABYSS_BLOCK = ITEMS.register("abyss_block", () -> new BlockItem(sssblocks.ABYSS_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> SSS_ELYTRA = ITEMS.register("sss_elytra", () -> new ssselytra(
        new Item.Properties().fireResistant().durability(1200)));
    
    
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
