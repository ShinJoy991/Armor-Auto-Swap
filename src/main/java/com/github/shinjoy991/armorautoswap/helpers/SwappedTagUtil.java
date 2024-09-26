package com.github.shinjoy991.armorautoswap.helpers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import static com.github.shinjoy991.armorautoswap.register.DataRegister.SWAPPED_TAG;

public class SwappedTagUtil {

    public static void putSwappedTag(String swappedPart, ItemStack capsuleStack, Boolean trueFalse) {
        SwappedTag customData = capsuleStack.getOrDefault(SWAPPED_TAG.get(), new SwappedTag(new CompoundTag()));
        SwappedTag newData = customData.update((CompoundTag tag) -> {
            tag.putBoolean(swappedPart, trueFalse);
        });
        capsuleStack.set(SWAPPED_TAG.get(), newData);
    }

    public static Boolean getSwappedTag(ItemStack itemStack, String key) {
        SwappedTag customData = itemStack.getOrDefault(SWAPPED_TAG.get(), new SwappedTag(new CompoundTag()));
        if (!customData.contains(key)) {
            return false;
        }
        return customData.getBoolean(key);
    }

}
