package com.example.examplemod.event;



import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;

public class torch_rod_event extends Item {
    public torch_rod_event(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        if (context.getLevel().getBlockState(pos).isAir()) {
            context.getLevel().setBlock(pos, Blocks.TORCH.defaultBlockState(), 1);
        }
        return super.useOn(context);
}

}
