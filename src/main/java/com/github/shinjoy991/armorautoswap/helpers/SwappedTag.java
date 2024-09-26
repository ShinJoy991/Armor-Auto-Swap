package com.github.shinjoy991.armorautoswap.helpers;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;

import java.util.function.Consumer;

public class SwappedTag {
    public static final Codec<SwappedTag> CODEC = CompoundTag.CODEC.xmap(SwappedTag::new, p_327962_ -> p_327962_.tag);
    private final CompoundTag tag;

    public SwappedTag(CompoundTag tag) {
        this.tag = tag;
    }

    public boolean getBoolean(String key) {
        return this.tag.getBoolean(key);
    }

    public SwappedTag update(Consumer<CompoundTag> updater) {
        CompoundTag updatedTag = this.tag.copy();
        updater.accept(updatedTag);
        return new SwappedTag(updatedTag);
    }

    public boolean contains(String key) {
        return this.tag.contains(key);
    }

}