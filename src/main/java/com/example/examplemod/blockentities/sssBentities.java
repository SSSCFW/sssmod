package com.example.examplemod.blockentities;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.blocks.IronFurnace;
import com.example.examplemod.blocks.sssblocks;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class sssBentities {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ExampleMod.MODID);

    public static final RegistryObject<BlockEntityType<IronFurnaceEntity>> IRON_FURNACE_ENTITY = TILE_ENTITY_TYPES.register("iron_furnace_entity",
        () -> BlockEntityType.Builder.of(IronFurnaceEntity::new, sssblocks.IRON_FURNACE.get()).build(null));
    public static final RegistryObject<BlockEntityType<MonsterBlockEntity>> MONSTERBLOCK_ENTITY = TILE_ENTITY_TYPES.register("monster_block_entity",
        () -> BlockEntityType.Builder.of(MonsterBlockEntity::new, sssblocks.MONSTER_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<SpikeBlockEntity>> SPIKEBLOCK_ENTITY = TILE_ENTITY_TYPES.register("spike_block_entity",
        () -> BlockEntityType.Builder.of(SpikeBlockEntity::new, sssblocks.SPIKE_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        TILE_ENTITY_TYPES.register(eventBus);
    }
    }
