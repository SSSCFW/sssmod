package com.example.examplemod.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.example.examplemod.items.sssitems;

import net.minecraft.world.Container;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;

import java.util.Map;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;

@Mixin(AnvilMenu.class)
public class AnvilMenuInject extends ItemCombinerMenu {
    public AnvilMenuInject(MenuType<?> p_39773_, int p_39774_, Inventory p_39775_, ContainerLevelAccess p_39776_) {
        super(p_39773_, p_39774_, p_39775_, p_39776_);
        //TODO Auto-generated constructor stub
    }

    //@Shadow @Final private Container inputSlots;
    @Shadow @Final private DataSlot cost = DataSlot.standalone();
    //@Shadow @Final protected ResultContainer resultSlots = new ResultContainer();
    @Shadow public int repairItemCountCost;
    @Shadow private String itemName;
    //@Shadow @Final protected Player player;
    
    @Override
    public void createResult() {
        AnvilMenu anvilmenu = (AnvilMenu) (Object) this;
        ItemStack itemstack = inputSlots.getItem(0);
        this.cost.set(1);
        int i = 0;
        int j = 0;
        int k = 0;
        
        if (itemstack.isEmpty()) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            this.cost.set(0);
        } else {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = this.inputSlots.getItem(1);
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack1);
            j += itemstack.getBaseRepairCost() + (itemstack2.isEmpty() ? 0 : itemstack2.getBaseRepairCost());
            this.repairItemCountCost = 0;
            boolean flag = false;
            boolean unbreak_abyss = false;
            boolean force_ok = false;

            if (!net.minecraftforge.common.ForgeHooks.onAnvilChange(anvilmenu, itemstack, itemstack2, resultSlots, itemName, j, this.player)) return;
            if (!itemstack2.isEmpty()) {
                flag = itemstack2.getItem() == Items.ENCHANTED_BOOK && !EnchantedBookItem.getEnchantments(itemstack2).isEmpty();
                unbreak_abyss = itemstack2.getItem() == sssitems.ABYSS_PRIZE.get() && !itemstack1.getTag().getBoolean("abyss");
                
                if (unbreak_abyss) {
                    k = 1;
                    j = 29;
                    i += k;
                    force_ok = true;
                    itemstack1.setRepairCost(0);
                    CompoundTag tag = itemstack1.getOrCreateTag();
                    tag.putBoolean("Unbreakable", true); 
                    itemstack1.setTag(tag);
                    this.repairItemCountCost = 1;
                }
                if (itemstack1.isDamageableItem() && itemstack1.getItem().isValidRepairItem(itemstack, itemstack2)) {
                    int l2 = Math.min(itemstack1.getDamageValue(), itemstack1.getMaxDamage() / 4);
                    if (l2 <= 0) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                        return;
                    }

                    int i3;
                    for(i3 = 0; l2 > 0 && i3 < itemstack2.getCount(); ++i3) {
                        int j3 = itemstack1.getDamageValue() - l2;
                        itemstack1.setDamageValue(j3);
                        ++i;
                        l2 = Math.min(itemstack1.getDamageValue(), itemstack1.getMaxDamage() / 4);
                    }

                    this.repairItemCountCost = i3;
                } else {
                    if (!unbreak_abyss && !flag && (!itemstack1.is(itemstack2.getItem()) || !itemstack1.isDamageableItem())) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                        return;
                    }

                    if (itemstack1.isDamageableItem() && !flag) {
                        int l = itemstack.getMaxDamage() - itemstack.getDamageValue();
                        int i1 = itemstack2.getMaxDamage() - itemstack2.getDamageValue();
                        int j1 = i1 + itemstack1.getMaxDamage() * 12 / 100;
                        int k1 = l + j1;
                        int l1 = itemstack1.getMaxDamage() - k1;
                        if (l1 < 0) {
                            l1 = 0;
                        }

                        if (l1 < itemstack1.getDamageValue()) {
                            itemstack1.setDamageValue(l1);
                            i += 2;
                        }
                    }

                    Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(itemstack2);
                    boolean flag2 = false;
                    boolean flag3 = false;

                    for(Enchantment enchantment1 : map1.keySet()) {
                        if (enchantment1 != null) {
                            int i2 = map.getOrDefault(enchantment1, 0);
                            int j2 = map1.get(enchantment1);
                            boolean eq_level = i2 == j2;
                            j2 = eq_level ? j2 + 1 : Math.max(j2, i2);
                            boolean flag1 = enchantment1.canEnchant(itemstack);
                            flag1 = true;
                            //if (this.player.getAbilities().instabuild || itemstack.is(Items.ENCHANTED_BOOK)) {
                            //    flag1 = true;
                            //}

                            for(Enchantment enchantment : map.keySet()) {
                                if (enchantment != enchantment1 && !enchantment1.isCompatibleWith(enchantment)) {
                                    flag1 = false;
                                    ++i;
                                }
                            }

                            if (!flag1) {
                                flag3 = true;
                            } else {
                                flag2 = true;
                                if (j2 > enchantment1.getMaxLevel() && eq_level) {
                                    j2 = enchantment1.getMaxLevel();
                                }

                                map.put(enchantment1, j2);
                                int k3 = 0;
                                switch (enchantment1.getRarity()) {
                                case COMMON:
                                    k3 = 1;
                                    break;
                                case UNCOMMON:
                                    k3 = 2;
                                    break;
                                case RARE:
                                    k3 = 4;
                                    break;
                                case VERY_RARE:
                                    k3 = 8;
                                }

                                if (flag) {
                                k3 = Math.max(1, k3 / 2);
                                }

                                i += k3 * j2;
                                if (itemstack.getCount() > 1) {
                                    i = 100;
                                }
                            }
                        }
                    }

                    if (flag3 && !flag2) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                        return;
                    }
                }
            }


            if (this.itemName != null && !Util.isBlank(this.itemName)) {
                if (!this.itemName.equals(itemstack.getHoverName().getString())) {
                k = 1;
                i += k;
                itemstack1.setHoverName(Component.literal(this.itemName));
                }
            } else if (itemstack.hasCustomHoverName()) {
                k = 1;
                i += k;
                itemstack1.resetHoverName();
            }
            if (flag && !itemstack1.isBookEnchantable(itemstack2)) {
                itemstack1 = ItemStack.EMPTY;
            }
            this.cost.set(j + i);
            if (i <= 0) {
                itemstack1 = ItemStack.EMPTY;
            }

            if (k == i && k > 0 && this.cost.get() >= 100) {
                this.cost.set(99);
            }

            if (this.cost.get() >= 100 && !this.player.getAbilities().instabuild && !force_ok) {
                itemstack1 = ItemStack.EMPTY;
            }

            if (!itemstack1.isEmpty()) {
                int k2 = itemstack1.getBaseRepairCost();
                if (!itemstack2.isEmpty() && k2 < itemstack2.getBaseRepairCost()) {
                    k2 = itemstack2.getBaseRepairCost();
                }

                if (k != i || k == 0) {
                    k2 = k2 * 2 + 1;//calculateIncreasedRepairCost(k2);
                }

                itemstack1.setRepairCost(k2);
                EnchantmentHelper.setEnchantments(map, itemstack1);
            }

            this.resultSlots.setItem(0, itemstack1);
            anvilmenu.broadcastChanges();
        }
        //info.cancel();
    }

    /*public static int calculateIncreasedRepairCost(int p_39026_) {
        return p_39026_ * 2 + 1;
     }*/

    @Override
    protected boolean mayPickup(Player p_39798_, boolean p_39799_) {
        return (p_39798_.getAbilities().instabuild || p_39798_.experienceLevel >= this.cost.get()) && this.cost.get() > 0;
    }

    @Override
    protected void onTake(Player p_150601_, ItemStack p_150602_) {
       if (!p_150601_.getAbilities().instabuild) {
        p_150601_.giveExperienceLevels(-this.cost.get());
      }

      float breakChance = net.minecraftforge.common.ForgeHooks.onAnvilRepair(p_150601_, p_150602_, this.inputSlots.getItem(0), this.inputSlots.getItem(1));

      this.inputSlots.setItem(0, ItemStack.EMPTY);
      if (this.repairItemCountCost > 0) {
         ItemStack itemstack = this.inputSlots.getItem(1);
         if (!itemstack.isEmpty() && itemstack.getCount() > this.repairItemCountCost) {
            itemstack.shrink(this.repairItemCountCost);
            this.inputSlots.setItem(1, itemstack);
         } else {
            this.inputSlots.setItem(1, ItemStack.EMPTY);
         }
      } else {
         this.inputSlots.setItem(1, ItemStack.EMPTY);
      }

      this.cost.set(0);
      this.access.execute((p_150479_, p_150480_) -> {
         BlockState blockstate = p_150479_.getBlockState(p_150480_);
         if (!p_150601_.getAbilities().instabuild && blockstate.is(BlockTags.ANVIL) && p_150601_.getRandom().nextFloat() < breakChance) {
            BlockState blockstate1 = AnvilBlock.damage(blockstate);
            if (blockstate1 == null) {
               p_150479_.removeBlock(p_150480_, false);
               p_150479_.levelEvent(1029, p_150480_, 0);
            } else {
               p_150479_.setBlock(p_150480_, blockstate1, 2);
               p_150479_.levelEvent(1030, p_150480_, 0);
            }
         } else {
            p_150479_.levelEvent(1030, p_150480_, 0);
         }

      });
    }

    @Override
    protected boolean isValidBlock(BlockState p_39788_) {
        return p_39788_.is(BlockTags.ANVIL);
    }

    @Override
    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create().withSlot(0, 27, 47, (p_266635_) -> {
            return true;
         }).withSlot(1, 76, 47, (p_266634_) -> {
            return true;
         }).withResultSlot(2, 134, 47).build();
    }
   
}
