package com.github.shinjoy991.armorautoswap;

import com.github.shinjoy991.armorautoswap.client.NetworkHandler;
import com.github.shinjoy991.armorautoswap.register.CapsuleWardrobeItem;
import com.github.shinjoy991.armorautoswap.register.DataRegister;
import com.github.shinjoy991.armorautoswap.register.InitClientEvents;
import com.github.shinjoy991.armorautoswap.register.ModMenuRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@Mod(ArmorAutoSwap.MOD_ID)
public class ArmorAutoSwap {

    public static final String MOD_ID = "armor_auto_swap";
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArmorAutoSwap.MOD_ID);
    public static final RegistryObject<Item> ARMOR_AUTO_SWAP_ITEM = ITEMS.register("armor_auto_swap_item",
            () -> new CapsuleWardrobeItem(new Item.Properties().stacksTo(1)));

    private static final Logger LOGGER = LogManager.getLogger();
    public static SimpleChannel NETWORK;

    public ArmorAutoSwap() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        ITEMS.register(modEventBus);
        ModMenuRegistry.MENU_TYPES.register(modEventBus);
        InitClientEvents.init();
        DataRegister.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void setup(final @NotNull FMLCommonSetupEvent event) {
        NetworkHandler.registerNetworkChannel();
    }
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES)
            event.accept(ARMOR_AUTO_SWAP_ITEM);
    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("ARMOR AUTO SWAP HELLO from server starting");
    }
}