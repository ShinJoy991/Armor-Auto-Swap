package com.github.shinjoy991.armorautoswap.register;

import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
import com.github.shinjoy991.armorautoswap.Config;
import com.github.shinjoy991.armorautoswap.client.ArmorSwapPacket;
import com.github.shinjoy991.armorautoswap.client.InputEvents;
import com.github.shinjoy991.armorautoswap.client.NetworkHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class CapsuleWardrobeScreen extends AbstractContainerScreen<CapsuleWardrobeMenu> {

    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(ArmorAutoSwap.MOD_ID, "textures/gui/armor_auto_swap_gui.png");
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(ArmorAutoSwap.MOD_ID, "textures/gui/sprites/button.png");

    private final UUID playerUUID;
    private StateSwitchingButton switchModeButton;

    public CapsuleWardrobeScreen(CapsuleWardrobeMenu container, Inventory inv, Component title) {
        super(container, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 184;
        this.inventoryLabelY += 18;
        this.playerUUID = inv.player.getUUID();
    }

    @Override
    protected void init() {
        super.init();
        this.initSwitchModeButton();
    }

    private void initSwitchModeButton() {
        this.switchModeButton = new StateSwitchingButton(this.leftPos + 107, this.topPos + 71, 20, 19,
                InputEvents.armorSwapEnabled.getOrDefault(playerUUID, Config.defaultMode));
        this.switchModeButton.initTextureValues(0, 0, 21, 19, BUTTON_TEXTURE);
        this.updateButtonTooltip(InputEvents.armorSwapEnabled.getOrDefault(playerUUID, Config.defaultMode));
        this.addRenderableWidget(this.switchModeButton);
    }

    @Override
    public boolean mouseClicked(double p_93641_, double p_93642_, int p_93643_) {
        if (this.switchModeButton.mouseClicked(p_93641_, p_93642_, p_93643_)) {
            boolean isEnabled = InputEvents.armorSwapEnabled.getOrDefault(playerUUID, Config.defaultMode);
            this.switchModeButton.setStateTriggered(!isEnabled);
            InputEvents.armorSwapEnabled.put(playerUUID, !isEnabled);
            NetworkHandler.CHANNEL.sendToServer(new ArmorSwapPacket(!isEnabled));
            this.updateButtonTooltip(!isEnabled);
        }
        return super.mouseClicked(p_93641_, p_93642_, p_93643_);
    }

    private void updateButtonTooltip(boolean isEnabled) {
        this.switchModeButton.setTooltip(Tooltip.create(isEnabled ?
                Component.translatable("Activated").withStyle(ChatFormatting.GREEN) :
                Component.translatable("Deactivated").withStyle(ChatFormatting.DARK_GRAY)));
    }

    @Override
    public void render(GuiGraphics p_281745_, int p_282145_, int p_282358_, float p_283566_) {
        super.render(p_281745_, p_282145_, p_282358_, p_283566_);
        this.renderTooltip(p_281745_, p_282145_, p_282358_);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }
}