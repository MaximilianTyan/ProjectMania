package com.zondayland.blocks;

import com.zondayland.registry.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;


public class MicHolderBlock extends HorizontalDirectionalBlock {

    private static final BooleanProperty HOLDING = BooleanProperty.create("holding");

    public MicHolderBlock(FabricBlockSettings settings) {
        super(settings.hardness(1.0f).noCollision().notSolid());

        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(HOLDING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(HOLDING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx).setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult use(
            BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit
    ) {
        if (state.getValue(HOLDING)) {
            if (!player.getMainHandItem().isEmpty()) {
                return InteractionResult.FAIL;
            }

            level.setBlockAndUpdate(pos, state.setValue(HOLDING, false));
            player.addItem(new ItemStack(ModItems.MICROPHONE));
        } else {
            if (!player.getMainHandItem().is(ModItems.MICROPHONE)) {
                return InteractionResult.FAIL;
            }

            player.getMainHandItem().shrink(1);
            level.setBlockAndUpdate(pos, state.setValue(HOLDING, true));
        }

        return InteractionResult.SUCCESS;
    }
}
