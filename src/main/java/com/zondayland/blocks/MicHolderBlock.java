package com.zondayland.blocks;

import com.zondayland.ZondayLand;
import com.zondayland.registry.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DecoratedPotBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.List;

import static com.zondayland.ZondayLand.MOD_ID;

public class MicHolderBlock extends HorizontalFacingBlock {

    private static final BooleanProperty HOLDING = BooleanProperty.of("holding");

    public MicHolderBlock(FabricBlockSettings settings) {
        super(settings.hardness(1.0f).noCollision().notSolid());
        setDefaultState(getDefaultState()
                .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
                .with(HOLDING, false)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
        builder.add(HOLDING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx)
                    .with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public ActionResult onUse(
            BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit
    ) {
        ZondayLand.LOGGER.info("onUse holding:%s hand:%s".formatted(state.get(HOLDING), player.getMainHandStack()));
        if (state.get(HOLDING)) {
            if (!player.getMainHandStack().isEmpty()) {
                return ActionResult.FAIL;
            }

            world.setBlockState(pos, state.with(HOLDING, false));
            player.giveItemStack(new ItemStack(ModItems.MICROPHONE));
        } else {
            if (!player.getMainHandStack().isOf(ModItems.MICROPHONE)) {
                return ActionResult.FAIL;
            }

            player.getMainHandStack().decrement(1);
            world.setBlockState(pos, state.with(HOLDING, true));
        }

        return ActionResult.SUCCESS;
    }
}
