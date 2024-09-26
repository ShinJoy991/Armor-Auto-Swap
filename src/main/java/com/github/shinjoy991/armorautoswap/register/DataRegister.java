package com.github.shinjoy991.armorautoswap.register;

import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
import com.github.shinjoy991.armorautoswap.helpers.SwappedTag;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class DataRegister {
    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, ArmorAutoSwap.MOD_ID);

    public static final RegistryObject<DataComponentType<SwappedTag>> SWAPPED_TAG = DATA_COMPONENT_TYPES.register("swapped_tag",
            () -> DataComponentType.<SwappedTag>builder()
                    .persistent(SwappedTag.CODEC)
                    .build());

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
