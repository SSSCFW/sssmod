package com.example.examplemod.event;


import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import java.util.function.Predicate;

import com.example.examplemod.entities.noname_arrow;
import com.example.examplemod.items.sssitems;
import com.example.examplemod.items.spcial.noname_arrow_item;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoubleBlockCombiner.NeighborCombineResult.Double;
import net.minecraft.world.item.*;



public class noname_bow extends BowItem {

    public noname_bow(Item.Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void releaseUsing(ItemStack p_40667_, Level p_40668_, LivingEntity p_40669_, int p_40670_) {
      if (p_40669_ instanceof Player player) {
         boolean flag = true;
         ItemStack itemstack = player.getProjectile(p_40667_);

         int i = this.getUseDuration(p_40667_) - p_40670_;
         i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(p_40667_, p_40668_, player, i, !itemstack.isEmpty() || flag);
         //if (i < 0) return;

         if (!itemstack.isEmpty() || flag) {
            if (itemstack.isEmpty()) {
               itemstack = new ItemStack(Items.ARROW);
            }
            float powerTime = getPowerForTime(i);
            float f = powerTime*1.25f;
            boolean noname = false;
            if (!((double)f < 0.1D)) {
               boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, p_40667_, player));
               if (!p_40668_.isClientSide) {
                  ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
                  
                  AbstractArrow abstractarrow = arrowitem.createArrow(p_40668_, itemstack, player);
                  if (itemstack.getItem() == sssitems.NONAME_ARROW.get()) {
                     abstractarrow.pickup = AbstractArrow.Pickup.DISALLOWED;
                     if (powerTime >= 1.0) {
                        abstractarrow.setCritArrow(true);
                        abstractarrow.setNoGravity(true);
                        f *= 1.5;
                        noname = true;
                     }
                  }
                  else {
                     if (f >= 1.0F) {
                        abstractarrow.setCritArrow(true);
                     }
                  }
                  
                  double AddDamage = 2.0D;
                  abstractarrow = customArrow(abstractarrow);
                  abstractarrow.setPierceLevel((byte) 10);
                  abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
                  abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + AddDamage);
                  

                  int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, p_40667_);
                  if (j > 0) {
                     abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + AddDamage + (double)j * 0.75D + 0.5D);
                  }

                  int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, p_40667_);
                  if (k > 0) {
                     abstractarrow.setKnockback(k);
                  }

                  /*if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, p_40667_) > 0) {
                     if (noname) {
                        abstractarrow.setSecondsOnFire(100);
                     }
                  }*/

                  /*p_40667_.hurtAndBreak(1, player, (p_309230_) -> {
                     p_309230_.broadcastBreakEvent(player.getUsedItemHand());
                  });*/
                  if (flag1 || player.getAbilities().instabuild && (itemstack.is(Items.SPECTRAL_ARROW) || itemstack.is(Items.TIPPED_ARROW))) {
                     abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                  }
                  
                  p_40668_.addFreshEntity(abstractarrow);
               }
               if (noname) {
                  p_40668_.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 4.0F, 1.0F);
               }
               else {
                  p_40668_.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (p_40668_.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
               }
               player.awardStat(Stats.ITEM_USED.get(this));
               if (noname) {
                  itemstack.shrink(1);
                  if (itemstack.isEmpty()) {
                     player.getInventory().removeItem(itemstack);
                  }
               }
            }
         }
      }
   }

    
}
