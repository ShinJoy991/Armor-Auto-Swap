package com.github.shinjoy991.armorautoswap;

import com.github.shinjoy991.armorautoswap.client.ClientKeyMapping;
import com.github.shinjoy991.armorautoswap.client.NetworkHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ArmorAutoSwap.MOD_ID)
public class ArmorAutoSwap {

    public static final String MOD_ID = "armor_auto_swap";
    public static final Logger LOGGER = LogManager.getLogger();
    public ArmorAutoSwap() {
        MinecraftForge.EVENT_BUS.register(this);
        NetworkHandler.register();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("ARMOR AUTO SWAP HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value =
            Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ClientRegistry.registerKeyBinding(ClientKeyMapping.DRINKING_KEY);
            LOGGER.info("ARMOR AUTO SWAP HELLO FROM CLIENT SETUP");
        }
    }
}