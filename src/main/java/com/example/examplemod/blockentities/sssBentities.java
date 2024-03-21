package com.example.examplemod.blockentities;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.blocks.sssblocks;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
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
    public static final RegistryObject<BlockEntityType<IronSpikeBlockEntity>> IRONSPIKEBLOCK_ENTITY = TILE_ENTITY_TYPES.register("iron_spike_block_entity",
        () -> BlockEntityType.Builder.of(IronSpikeBlockEntity::new, sssblocks.IRON_SPIKE_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<SuperChestEntity>> SUPERCHEST_ENTITY = TILE_ENTITY_TYPES.register("superchest_entity",
        () -> BlockEntityType.Builder.of(SuperChestEntity::new, sssblocks.SUPERCHEST.get()).build(null));
    public static final RegistryObject<BlockEntityType<AbyssChangerEntity>> ABYSSCHANGER_ENTITY = TILE_ENTITY_TYPES.register("abyss_changer_entity",
        () -> BlockEntityType.Builder.of(AbyssChangerEntity::new, sssblocks.ABYSS_CHANGER.get()).build(null));
    public static final RegistryObject<BlockEntityType<SuperHopperEntity>> SUPERHOPPER_ENTITY = TILE_ENTITY_TYPES.register("superhopper_entity",
        () -> BlockEntityType.Builder.of(SuperHopperEntity::new, sssblocks.SUPERHOPPER.get()).build(null));
    public static final RegistryObject<BlockEntityType<SuperDropperEntity>> SUPERDROPPER_ENTITY = TILE_ENTITY_TYPES.register("superdropper_entity",
        () -> BlockEntityType.Builder.of(SuperDropperEntity::new, sssblocks.SUPERDROPPER.get()).build(null));
    public static final RegistryObject<BlockEntityType<DestroyerEntity>> DESTROYER_ENTITY = TILE_ENTITY_TYPES.register("destroyer_entity",
        () -> BlockEntityType.Builder.of(DestroyerEntity::new, sssblocks.DESTROYER.get()).build(null));


    public static void register(IEventBus eventBus) {
        TILE_ENTITY_TYPES.register(eventBus);
    }
    
}
