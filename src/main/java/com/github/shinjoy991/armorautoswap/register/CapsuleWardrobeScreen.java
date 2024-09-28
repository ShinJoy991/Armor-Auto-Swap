package com.github.shinjoy991.armorautoswap.register;

import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
import com.github.shinjoy991.armorautoswap.Config;
import com.github.shinjoy991.armorautoswap.client.ArmorSwapPacket;
import com.github.shinjoy991.armorautoswap.client.InputEvents;
import com.github.shinjoy991.armorautoswap.client.NetworkHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class CapsuleWardrobeScreen extends ContainerScreen<CapsuleWardrobeMenu> {

    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(ArmorAutoSwap.MOD_ID, "textures/gui/armor_auto_swap_gui.png");
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(ArmorAutoSwap.MOD_ID, "textures/gui/sprites/button.png");

    private final UUID playerUUID;
    private ToggleWidget switchModeButton;

    public CapsuleWardrobeScreen(CapsuleWardrobeMenu container, PlayerInventory inv, ITextComponent title) {
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
        this.switchModeButton = new ToggleWidget(this.leftPos + 107, this.topPos + 71, 20, 19,
                InputEvents.armorSwapEnabled.getOrDefault(playerUUID, Config.defaultMode));
        this.switchModeButton.initTextureValues(0, 0, 21, 19, BUTTON_TEXTURE);
        this.updateButtonTooltip(InputEvents.armorSwapEnabled.getOrDefault(playerUUID, Config.defaultMode));
        this.addWidget(this.switchModeButton);

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
        this.switchModeButton.setMessage(isEnabled ?
                new TranslationTextComponent("Activated").withStyle(TextFormatting.GREEN) :
                new TranslationTextComponent("Deactivated").withStyle(TextFormatting.DARK_GRAY));
    }

    @Override
    public void render(MatrixStack p_281745_, int p_282145_, int p_282358_, float p_283566_) {
        super.render(p_281745_, p_282145_, p_282358_, p_283566_);
        this.switchModeButton.render(p_281745_, p_282145_, p_282358_, p_283566_);
        this.renderTooltip(p_281745_, p_282145_, p_282358_);
    }

    @Override
    protected void renderBg(MatrixStack guiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bind(GUI_TEXTURE);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        this.blit(guiGraphics, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }
}