package com.github.shinjoy991.armorautoswap;

import com.github.shinjoy991.armorautoswap.client.ClientKeyMapping;
import com.github.shinjoy991.armorautoswap.client.NetworkHandler;
import com.github.shinjoy991.armorautoswap.register.CapsuleWardrobeItem;
import com.github.shinjoy991.armorautoswap.register.InitClientEvents;
import com.github.shinjoy991.armorautoswap.register.ModMenuRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.github.shinjoy991.armorautoswap.Config.reloadCommand;

@Mod(ArmorAutoSwap.MOD_ID)
public class ArmorAutoSwap {

    public static final String MOD_ID = "armor_auto_swap";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArmorAutoSwap.MOD_ID);
    public static final RegistryObject<Item> ARMOR_AUTO_SWAP_ITEM = ITEMS.register("armor_auto_swap_item",
            () -> new CapsuleWardrobeItem(new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_TOOLS)));
    public static SimpleChannel NETWORK;

    public ArmorAutoSwap() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        ITEMS.register(modEventBus);
        ModMenuRegistry.MENU_TYPES.register(modEventBus);
        InitClientEvents.init();
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void setup(final FMLCommonSetupEvent event) {
        NetworkHandler.register();
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        LOGGER.info("ARMOR AUTO SWAP HELLO from server starting");
    }
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value =
            Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ClientRegistry.registerKeyBinding(ClientKeyMapping.SWAP_KEY);
            LOGGER.info("ARMOR AUTO SWAP HELLO FROM CLIENT SETUP");
        }
    }
}