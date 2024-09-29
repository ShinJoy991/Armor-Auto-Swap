package com.github.shinjoy991.armorautoswap;

import com.github.shinjoy991.armorautoswap.client.ArmorSwapPacket;
import com.github.shinjoy991.armorautoswap.client.ArmorSwapPacketClientManage;
import com.github.shinjoy991.armorautoswap.client.ClientKeyMapping;
import com.github.shinjoy991.armorautoswap.client.NetworkHandler;
import com.github.shinjoy991.armorautoswap.register.CapsuleWardrobeItem;
import com.github.shinjoy991.armorautoswap.register.InitClientEvents;
import com.github.shinjoy991.armorautoswap.register.ModMenuRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@Mod(ArmorAutoSwap.MOD_ID)
public class ArmorAutoSwap {

    public static final String MOD_ID = "armor_auto_swap";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArmorAutoSwap.MOD_ID);
    public static final RegistryObject<Item> ARMOR_AUTO_SWAP_ITEM = ITEMS.register("armor_auto_swap_item",
            () -> new CapsuleWardrobeItem(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TOOLS)));
    public static SimpleChannel NETWORK;

    public ArmorAutoSwap() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        ITEMS.register(modEventBus);
        ModMenuRegistry.MENU_TYPES.register(modEventBus);
        InitClientEvents.init();
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void setup(final @NotNull FMLCommonSetupEvent event) {
        NetworkHandler.register();
    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("ARMOR AUTO SWAP HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onPlayerJoin(EntityJoinLevelEvent event) {
            if (!event.getEntity().level.isClientSide() && event.getEntity() instanceof ServerPlayer player) {
                boolean serverDefaultMode = Config.defaultMode;
                NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                        new ArmorSwapPacketClientManage(serverDefaultMode));

            }
        }
    }
}