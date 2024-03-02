package com.example.examplemod;

import com.example.examplemod.blockentities.sssBentities;
import com.example.examplemod.blocks.sssblocks;
import com.example.examplemod.enchants.Enchant;
import com.example.examplemod.entities.abyss;
import com.example.examplemod.entities.entityInit;
import com.example.examplemod.entities.rapid_skeleton;
import com.example.examplemod.entities.client.ModModelLayer;
import com.example.examplemod.entities.render.explosion_arrow_renderer;
import com.example.examplemod.entities.render.flight_boat_renderer;
import com.example.examplemod.entities.render.homing_arrow_renderer;
import com.example.examplemod.entities.render.noname_arrow_renderer;
import com.example.examplemod.entities.render.torch_arrow_renderer;
import com.example.examplemod.items.sssitems;
import com.example.examplemod.network.packet;
import com.example.examplemod.util.ModWoodType;
import com.example.examplemod.world.ModWorldGenProvider;
import com.example.examplemod.world.dimension.ModDimensions;
import com.mojang.logging.LogUtils;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "sssmod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
    // Creates a new BlockItem with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));

    // Creates a new food item with the id "examplemod:example_id", nutrition 1 and saturation 2
    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab
    @SuppressWarnings("null")
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("sssmod", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> sssitems.DIRT_FOOD.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(sssitems.DIRT_FOOD.get());
                output.accept(sssitems.TORCH_ROD.get());
                output.accept(sssitems.TORCH_BLOCK.get());
                output.accept(sssitems.IRON_FURNACE.get());
                output.accept(sssitems.NONAME_BOW.get());
                output.accept(sssitems.NONAME_ARROW.get());
                output.accept(sssitems.TORCH_ARROW.get());
                output.accept(sssitems.SPIKE_BLOCK.get());
                output.accept(sssitems.IRON_SPIKE_BLOCK.get());
                output.accept(sssitems.REVERSE_STAR.get());
                output.accept(sssitems.ZOMBIE_STAR.get());
                output.accept(sssitems.NETHER_PORTAL.get());
                output.accept(sssitems.FLIGHT_BOAT.get());
                output.accept(sssitems.DIAMOND_STICK.get());
                output.accept(sssitems.ORE_TELEPORT.get());
                output.accept(sssitems.INVINCIBLE_STAR.get());
                output.accept(sssitems.ABYSS_PRIZE.get());
                output.accept(sssitems.ABYSS_BLOCK.get());
                output.accept(sssitems.HOMING_ARROW.get());
                //output.accept(sssitems.ABYSS.get());
            }).build());

    public static void addCustomItemProperties() {
        makeBow(sssitems.NONAME_BOW.get());
    }

    private static void makeBow(Item item) {
        ItemProperties.register(item, new ResourceLocation("pull"), (p_174635_, p_174636_, p_174637_, p_174638_) -> {
            if (p_174637_ == null) {
                return 0.0F;
            } else {
                return p_174637_.getUseItem() != p_174635_ ? 0.0F : (float)(p_174635_.getUseDuration() -
                        p_174637_.getUseItemRemainingTicks()) / 20.0F;
            }
        });

        ItemProperties.register(item, new ResourceLocation("pulling"), (p_174630_, p_174631_, p_174632_, p_174633_) -> {
            return p_174632_ != null && p_174632_.isUsingItem() && p_174632_.getUseItem() == p_174630_ ? 1.0F : 0.0F;
        });
    }

    

    public ExampleMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        //modEventBus.addListener(this::gatherData);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        //ModDimensions.register();
        sssitems.register(modEventBus);
        sssblocks.register(modEventBus);
        sssBentities.register(modEventBus);

        Enchant.ENCHANTMENTS.register(modEventBus);
        entityInit.ENTITY_TYPES.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.register(this));
        

        // Register the item to a creative tab
        //modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new ModWorldGenProvider(packOutput, lookupProvider));
        System.out.println("GatherData loaded");
    }

    @SuppressWarnings("deprecation")
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
        event.enqueueWork(() -> {
            packet.register();
            SpawnPlacements.register(entityInit.ABYSS.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, abyss::canSpawn);
            SpawnPlacements.register(entityInit.RAPID_SKELETON.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, rapid_skeleton::canSpawn);
        });
    }

    // Add the example block item to the building blocks tab
   /* private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(EXAMPLE_BLOCK_ITEM);
    }*/
/*
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
*/
    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            //LOGGER.info("HELLO FROM CLIENT SETUP");
            //LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
            Sheets.addWoodType(ModWoodType.TORCH);
            addCustomItemProperties();
            EntityRenderers.register(entityInit.NONAME_ARROW.get(), noname_arrow_renderer::new);
            EntityRenderers.register(entityInit.TORCH_ARROW.get(), torch_arrow_renderer::new);
            EntityRenderers.register(entityInit.HOMING_ARROW.get(), homing_arrow_renderer::new);
            EntityRenderers.register(entityInit.EXPLOSION_ARROW.get(), explosion_arrow_renderer::new);
            EntityRenderers.register(entityInit.ABYSS.get(), ZombieRenderer::new);
            EntityRenderers.register(entityInit.RAPID_SKELETON.get(), SkeletonRenderer::new);
            EntityRenderers.register(entityInit.FLIGHT_BOAT.get(),  pContext -> new flight_boat_renderer(pContext, false));
        }

        @SubscribeEvent
        public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(ModModelLayer.FLIGHT_BOAT_LAYER, BoatModel::createBodyModel);
        }

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents
    {
        @SubscribeEvent
        public static void registerAttributes(EntityAttributeCreationEvent event) {
            event.put(entityInit.ABYSS.get(), abyss.createAttributes().build());
            event.put(entityInit.RAPID_SKELETON.get(), abyss.createAttributes().build());
        }

        

       /*  @SubscribeEvent
        public static void addCustomTrades(VillagerTradesEvent event) {
            if(event.getType() == VillagerProfession.FARMER) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

                // Level 1
                trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                        new ItemStack(Items.WHEAT_SEEDS, 32),
                        new ItemStack(Items.EMERALD, 1),
                        32, 8, 0.02f));

            }
        }*/
    }
}
