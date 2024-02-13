package com.example.examplemod.blocks;

import com.example.examplemod.ExampleMod;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
    

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
    
}
