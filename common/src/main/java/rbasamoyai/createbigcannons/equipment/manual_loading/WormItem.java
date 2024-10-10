package rbasamoyai.createbigcannons.equipment.manual_loading;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.simibubi.create.content.kinetics.deployer.DeployerFakePlayer;
import com.simibubi.create.foundation.utility.NBTProcessors;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.base.CBCTooltip;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEnd;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;

public class WormItem extends Item implements HandloadingTool {

	private final Multimap<Attribute, AttributeModifier> defaultModifiers;

	public WormItem(Properties properties) {
		super(properties);
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 2.5d, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -3.0d, AttributeModifier.Operation.ADDITION));
		this.defaultModifiers = builder.build();
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		if (player instanceof DeployerFakePlayer && !CBCConfigs.SERVER.cannons.deployersCanUseLoadingTools.get())
			return InteractionResult.PASS;
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Direction reachDirection = context.getClickedFace().getOpposite();

		for (int i = 0; i < CBCConfigs.SERVER.cannons.wormReach.get(); ++i) {
			BlockPos pos1 = pos.relative(reachDirection, i);
			BlockState state1 = level.getBlockState(pos1);
			BlockEntity be = level.getBlockEntity(pos1);
			if (!isValidLoadBlock(state1, level, pos1, reachDirection)
				|| !(be instanceof IBigCannonBlockEntity cbe)) return InteractionResult.FAIL;

			StructureBlockInfo info = cbe.cannonBehavior().block();
			if (info.state().isAir()) continue;

			BlockPos pos2 = pos1.relative(context.getClickedFace());
			BlockEntity be1 = level.getBlockEntity(pos2);
			if (be1 instanceof IBigCannonBlockEntity cbe1 && cbe1.canLoadBlock(info)) {
				if (!level.isClientSide) {
					cbe1.cannonBehavior().loadBlock(info);
					be1.setChanged();
				}
			} else if (level.getBlockState(pos2).isAir()) {
				if (!level.isClientSide) {
					level.setBlock(pos2, info.state(), Block.UPDATE_MOVE_BY_PISTON | Block.UPDATE_ALL);
					BlockEntity be2 = level.getBlockEntity(pos2);
					CompoundTag tag = info.nbt();
					if (be2 != null)
						tag = NBTProcessors.process(info.state(), be2, tag, false);
					if (be2 != null && tag != null) {
						tag.putInt("x", pos2.getX());
						tag.putInt("y", pos2.getY());
						tag.putInt("z", pos2.getZ());
						be2.load(tag);
					}
				}
			} else {
				return InteractionResult.FAIL;
			}
			if (!level.isClientSide) {
				cbe.cannonBehavior().removeBlock();
				be.setChanged();
			}

			level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 1, 1);
			player.causeFoodExhaustion(CBCConfigs.SERVER.cannons.loadingToolHungerConsumption.getF());
			player.getCooldowns().addCooldown(this, CBCConfigs.SERVER.cannons.loadingToolCooldown.get());
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return super.useOn(context);
	}

	@Override
	public void onUseOnCannon(Player player, Level level, BlockPos startPos, Direction face, MountedBigCannonContraption contraption) {
		if (player instanceof DeployerFakePlayer && !CBCConfigs.SERVER.cannons.deployersCanUseLoadingTools.get())
			return;
		Direction reachDirection = face.getOpposite();

		Set<BlockPos> changes = new HashSet<>(2);
		for (int i = 0; i < CBCConfigs.SERVER.cannons.wormReach.get(); ++i) {
			BlockPos pos1 = startPos.relative(reachDirection, i);
			StructureBlockInfo info = contraption.getBlocks().get(pos1);
			if (info == null || !isValidLoadBlock(info.state(), contraption, pos1, reachDirection)) return;
			BlockEntity be = contraption.presentBlockEntities.get(pos1);
			if (!(be instanceof IBigCannonBlockEntity cbe)) return;

			StructureBlockInfo info1 = cbe.cannonBehavior().block();
			if (info1.state().isAir()) continue;

			BlockPos pos2 = pos1.relative(face);
			BlockEntity be1 = contraption.presentBlockEntities.get(pos2);
			if (be1 instanceof IBigCannonBlockEntity cbe1 && cbe1.cannonBehavior().canLoadBlock(info1)) {
				if (!level.isClientSide) {
					cbe1.cannonBehavior().loadBlock(info1);
					changes.add(pos2);
				}
			} else if (i == 0) {
				if (!level.isClientSide) {
					ItemStack stack = info1.state().getBlock() instanceof BigCannonMunitionBlock munition ? munition.getExtractedItem(info1) : ItemStack.EMPTY;
					if (!player.addItem(stack) && !player.isCreative()) {
						ItemEntity item = player.drop(stack, false);
						if (item != null) {
							item.setNoPickUpDelay();
							item.setTarget(player.getUUID());
						}
					}
				}
			} else {
				return;
			}
			if (!level.isClientSide) {
				cbe.cannonBehavior().removeBlock();
				changes.add(pos1);
				BigCannonBlock.writeAndSyncMultipleBlockData(changes, contraption.entity, contraption);
			}

			level.playSound(null, player.blockPosition(), SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 1, 1);
			player.causeFoodExhaustion(CBCConfigs.SERVER.cannons.loadingToolHungerConsumption.getF());
			player.getCooldowns().addCooldown(this, CBCConfigs.SERVER.cannons.loadingToolCooldown.get());
			return;
		}
	}

	public static boolean isValidLoadBlock(BlockState state, Level level, BlockPos pos, Direction dir) {
		return state.getBlock() instanceof BigCannonBlock cBlock
			&& cBlock.getOpeningType(level, state, pos) == BigCannonEnd.OPEN
			&& cBlock.getFacing(state).getAxis() == dir.getAxis();
	}

	public static boolean isValidLoadBlock(BlockState state, MountedBigCannonContraption contraption, BlockPos pos, Direction dir) {
		if (state.getBlock() instanceof BigCannonMunitionBlock munition)
			return munition.canBeLoaded(state, dir.getAxis());
		if (state.getBlock() instanceof BigCannonBlock cBlock)
			return cBlock.getOpeningType(contraption, state, pos) == BigCannonEnd.OPEN && cBlock.getFacing(state).getAxis() == dir.getAxis();
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		CBCTooltip.appendWormText(stack, level, tooltip, flag);
	}

	public static int getReach() {
		return CBCConfigs.SERVER.cannons.ramRodReach.get();
	}

	public static boolean deployersCanUse() {
		return CBCConfigs.SERVER.cannons.deployersCanUseLoadingTools.get();
	}

}
