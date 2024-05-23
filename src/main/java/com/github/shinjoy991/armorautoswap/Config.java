package com.github.shinjoy991.armorautoswap;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = ArmorAutoSwap.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue DEFAULT_MODE = BUILDER
            .comment("Whether to active swap armor when join world")
            .define("defaultMode", true);

    public static final ForgeConfigSpec.IntValue PERCENT_NUMBER = BUILDER
            .comment("A percent number in %, 0 to disable")
            .defineInRange("percentNumber", 10, 0, 99);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean defaultMode;
    public static int percentNumber;

    @SubscribeEvent
    public static void onLoad(final ModConfig.ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            defaultMode = DEFAULT_MODE.get();
            percentNumber = PERCENT_NUMBER.get();
        }
    }

    public static void reloadCommand() {
        defaultMode = DEFAULT_MODE.get();
        percentNumber = PERCENT_NUMBER.get();
    }
}