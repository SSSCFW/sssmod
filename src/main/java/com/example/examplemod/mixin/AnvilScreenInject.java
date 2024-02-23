package com.example.examplemod.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;

@Mixin(AnvilScreen.class)
public class AnvilScreenInject extends ItemCombinerScreen<AnvilMenu> {
    private static final ResourceLocation ERROR_SPRITE = new ResourceLocation("container/anvil/error");
    private static final Component TOO_EXPENSIVE_TEXT = Component.translatable("container.repair.expensive");
    @Shadow @Final private Player player;
    
    public AnvilScreenInject(AnvilMenu p_98901_, Inventory p_98902_, Component p_98903_, ResourceLocation p_98904_) {
        super(p_98901_, p_98902_, p_98903_, p_98904_);
        //this.player = p_98902_.player;
        //this.titleLabelX = 60;
        //TODO Auto-generated constructor stub
    }

    @Override
    protected void renderLabels(GuiGraphics p_281442_, int p_282417_, int p_283022_) {
        super.renderLabels(p_281442_, p_282417_, p_283022_);
        int i = this.menu.getCost();
        if (i > 0) {
           int j = 8453920;
           Component component;
           if (i >= 100 && !this.minecraft.player.getAbilities().instabuild) {
              component = TOO_EXPENSIVE_TEXT;
              j = 16736352;
           } else if (!this.menu.getSlot(2).hasItem()) {
              component = null;
           } else {
              component = Component.translatable("container.repair.cost", i);
              if (!this.menu.getSlot(2).mayPickup(this.player)) {
                 j = 16736352;
              }
           }
  
           if (component != null) {
              int k = this.imageWidth - 8 - this.font.width(component) - 2;
              int l = 69;
              p_281442_.fill(k - 2, 67, this.imageWidth - 8, 79, 1325400064);
              p_281442_.drawString(this.font, component, k, 69, j);
           }
        }
  
     }

    @Override
    protected void renderErrorIcon(GuiGraphics p_281990_, int p_266822_, int p_267045_) {
        if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem()) && !this.menu.getSlot(this.menu.getResultSlot()).hasItem()) {
            p_281990_.blitSprite(ERROR_SPRITE, p_266822_ + 99, p_267045_ + 45, 28, 21);
         }
    }
    
}
