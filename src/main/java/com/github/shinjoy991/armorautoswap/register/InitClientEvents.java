package com.github.shinjoy991.armorautoswap.register;

import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(modid = ArmorAutoSwap.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class InitClientEvents {

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(InitClientEvents::onClientSetup);
    }

    public static void onClientSetup(FMLClientSetupEvent event) {ScreenManager.register(ModMenuRegistry.ARMOR_AUTO_SWAP.get(), CapsuleWardrobeScreen::new);
    }

}