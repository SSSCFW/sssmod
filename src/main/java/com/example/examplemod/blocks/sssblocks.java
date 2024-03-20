package com.example.examplemod.blocks;

import com.example.examplemod.ExampleMod;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class sssblocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ExampleMod.MODID);
    @SuppressWarnings("null")
    public static final RegistryObject<Block> TORCH_BLOCK = BLOCKS.register("torch_block", () -> new Block(
        BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
        .strength(1f, 120f)
        .lightLevel(value -> 15)
    ));

    @SuppressWarnings("null")
    public static final RegistryObject<Block> IRON_FURNACE = BLOCKS.register("iron_furnace", () -> new IronFurnace(
        BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
        .strength(1f, 120f)
    ));

    @SuppressWarnings("null")
    public static final RegistryObject<Block> SUPERCHEST = BLOCKS.register("superchest", () -> new SuperChest(
        BlockBehaviour.Properties.ofFullCopy(Blocks.CHEST)
        .strength(1f, 120f)
    ));


    @SuppressWarnings("null")
    public static final RegistryObject<Block> MONSTER_BLOCK = BLOCKS.register("monster_block", () -> new MonsterBlock(
        BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
        .strength(1f, 120f)
    ));

    @SuppressWarnings("null")
    public static final RegistryObject<Block> SPIKE_BLOCK = BLOCKS.register("spike_block", () -> new SpikeBlock(
        BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
        .strength(1f, 120f)
        .noCollission()
    ));

    @SuppressWarnings("null")
    public static final RegistryObject<Block> IRON_SPIKE_BLOCK = BLOCKS.register("iron_spike_block", () -> new IronSpikeBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
            .strength(1f, 120f)
            .noCollission()
    ));

    public static final RegistryObject<Block> ORE_TELEPORT = BLOCKS.register("ore_teleport",
    () -> new ModPortalBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noLootTable().strength(1f, 120f).noOcclusion().noCollission()));

    public static final RegistryObject<Block> ABYSS_BLOCK = BLOCKS.register("abyss_block", () -> new Block(
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
            .strength(1f, 120f)
    ));

    public static final RegistryObject<Block> ABYSS_CHANGER = BLOCKS.register("abyss_changer", () -> new Block(
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
            .strength(2f, 120f)
    ));

    public static final RegistryObject<Block> SUPERHOPPER = BLOCKS.register("superhopper", () -> new SuperHopper(
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
            .strength(2f, 120f)
    ));
    

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
    
}
