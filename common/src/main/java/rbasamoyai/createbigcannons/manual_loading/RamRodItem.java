package rbasamoyai.createbigcannons.manual_loading;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

public class RamRodItem extends Item implements HandloadingTool {

	public static final UUID BASE_ATTACK_KNOCKBACK_UUID = UUID.fromString("bfa4160d-4ef0-4069-9569-3dfd2765f1c6");

	private final Multimap<Attribute, AttributeModifier> defaultModifiers;

	public RamRodItem(Properties properties) {
		super(properties);
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 3.0d, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -3.5d, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(BASE_ATTACK_KNOCKBACK_UUID, "Tool modifier", 2.5d, AttributeModifier.Operation.ADDITION));
		this.defaultModifiers = builder.build();
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		if (player instanceof DeployerFakePlayer && !deployersCanUse()) return InteractionResult.PASS;
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Direction face = context.getClickedFace();
		Direction pushDirection = face.getOpposite();

		if (level.isClientSide) return InteractionResult.SUCCESS;

		int k = 0;
		if (level.getBlockEntity(pos) instanceof IBigCannonBlockEntity) {
			BlockState state = level.getBlockState(pos.relative(face));
			if (state.blocksMotion()) return InteractionResult.PASS;
			k = -1;
			for (int i = 0; i < getReach(); ++i) {
				BlockPos pos1 = pos.relative(pushDirection, i);
				BlockState state1 = level.getBlockState(pos1);
				if (state1.isAir()) continue;
				if (!isValidLoadBlock(state1, level, pos1, pushDirection)) return InteractionResult.FAIL;

				if (level.getBlockEntity(pos1) instanceof IBigCannonBlockEntity cbe) {
					StructureBlockInfo info = cbe.cannonBehavior().block();
					if (info.state() == null || info.state().isAir()) continue;
				}
				k = i;
				break;
			}
			if (k == -1) return InteractionResult.PASS;
		}

		List<StructureBlockInfo> toPush = new ArrayList<>();
		boolean encounteredCannon = false;
		int maxCount = getPushStrength();
		for (int i = 0; i < maxCount + 1; ++i) {
			BlockPos pos1 = pos.relative(pushDirection, i + k);
			BlockState state1 = level.getBlockState(pos1);
			if (state1.isAir()) break;
			if (!isValidLoadBlock(state1, level, pos1, pushDirection)) return InteractionResult.FAIL;

			BlockEntity be = level.getBlockEntity(pos1);
			if (be instanceof IBigCannonBlockEntity cbe) {
				encounteredCannon = true;
				StructureBlockInfo info = cbe.cannonBehavior().block();
				if (info.state().isAir()) break;
				toPush.add(info);
			} else {
				CompoundTag tag = null;
				if (be != null) {
					tag = be.saveWithFullMetadata();
					tag.remove("x");
					tag.remove("y");
					tag.remove("z");
				}
				toPush.add(new StructureBlockInfo(BlockPos.ZERO, state1, tag));
			}
			if (toPush.size() > maxCount) return InteractionResult.FAIL;
		}
		if (!encounteredCannon || toPush.isEmpty()) return InteractionResult.FAIL;
		for (int i = toPush.size() - 1; i >= 0; --i) {
			BlockPos pos1 = pos.relative(pushDirection, i + k);

			BlockEntity be1 = level.getBlockEntity(pos1);
			if (be1 instanceof IBigCannonBlockEntity cbe) {
				cbe.cannonBehavior().removeBlock();
				be1.setChanged();
			} else {
				level.removeBlock(pos1, false);
			}

			StructureBlockInfo info = toPush.get(i);
			BlockPos pos2 = pos1.relative(pushDirection);
			if (level.getBlockEntity(pos2) instanceof IBigCannonBlockEntity cbe) {
				cbe.cannonBehavior().tryLoadingBlock(info);
			} else {
				level.setBlock(pos2, info.state(), Block.UPDATE_MOVE_BY_PISTON | Block.UPDATE_ALL);
				BlockEntity be2 = level.getBlockEntity(pos2);
				CompoundTag tag = info.nbt();
				if (be2 != null) tag = NBTProcessors.process(be2, tag, false);
				if (be2 != null && tag != null) {
					tag.putInt("x", pos2.getX());
					tag.putInt("y", pos2.getY());
					tag.putInt("z", pos2.getZ());
					be2.load(tag);
				}
			}
		}
		level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.PLAYERS, 1, 1);
		player.causeFoodExhaustion(toPush.size() * CBCConfigs.SERVER.cannons.loadingToolHungerConsumption.getF());
		player.getCooldowns().addCooldown(this, CBCConfigs.SERVER.cannons.loadingToolCooldown.get());
		return InteractionResult.CONSUME;
	}

	@Override
	public void onUseOnCannon(Player player, Level level, BlockPos startPos, Direction face, MountedBigCannonContraption contraption) {
		if (player instanceof DeployerFakePlayer && !deployersCanUse()) return;
		Direction pushDirection = face.getOpposite();

		int k = 0;
		if (contraption.presentBlockEntities.get(startPos) instanceof IBigCannonBlockEntity) {
			k = -1;
			for (int i = 0; i < getReach(); ++i) {
				BlockPos pos1 = startPos.relative(pushDirection, i);
				StructureBlockInfo info = contraption.getBlocks().get(pos1);
				if (info == null || !isValidLoadBlock(info.state(), contraption, pos1, pushDirection)) return;

				if (contraption.presentBlockEntities.get(pos1) instanceof IBigCannonBlockEntity cbe) {
					StructureBlockInfo info1 = cbe.cannonBehavior().block();
					if (info1.state().isAir()) continue;
				}
				k = i;
				break;
			}
			if (k == -1) return;
		}

		List<StructureBlockInfo> toPush = new ArrayList<>();
		boolean encounteredCannon = false;
		int maxCount = getPushStrength();
		for (int i = 0; i < maxCount + 1; ++i) {
			BlockPos pos1 = startPos.relative(pushDirection, i + k);
			StructureBlockInfo info = contraption.getBlocks().get(pos1);
			if (info == null || !isValidLoadBlock(info.state(), contraption, pos1, pushDirection)) return;
			if (!(contraption.presentBlockEntities.get(pos1) instanceof IBigCannonBlockEntity cbe)) break;
			encounteredCannon = true;
			StructureBlockInfo info1 = cbe.cannonBehavior().block();
			if (info1.state().isAir()) break;
			toPush.add(info1);
			if (toPush.size() > maxCount) return;
		}
		if (!encounteredCannon || toPush.isEmpty()) return;

		if (!level.isClientSide) {
			Set<BlockPos> changes = new HashSet<>(2);
			for (int i = toPush.size() - 1; i >= 0; --i) {
				BlockPos pos1 = startPos.relative(pushDirection, i + k);
				BlockPos pos2 = pos1.relative(pushDirection);
				StructureBlockInfo info = toPush.get(i);

				BlockEntity be1 = contraption.presentBlockEntities.get(pos1);
				BlockEntity be2 = contraption.presentBlockEntities.get(pos2);
				if (!(be1 instanceof IBigCannonBlockEntity cbe)
					|| !(be2 instanceof IBigCannonBlockEntity cbe1)) break;
				cbe.cannonBehavior().removeBlock();
				cbe1.cannonBehavior().tryLoadingBlock(info);

				changes.add(pos2);
				if (i == 0) changes.add(pos1);
			}
			BigCannonBlock.writeAndSyncMultipleBlockData(changes, contraption.entity, contraption);
		}

		level.playSound(null, player.blockPosition(), SoundEvents.WOOL_PLACE, SoundSource.PLAYERS, 1, 1);
		player.causeFoodExhaustion(toPush.size() * CBCConfigs.SERVER.cannons.loadingToolHungerConsumption.getF());
		player.getCooldowns().addCooldown(this, CBCConfigs.SERVER.cannons.loadingToolCooldown.get());
	}

	public static boolean isValidLoadBlock(BlockState state, Level level, BlockPos pos, Direction dir) {
		if (state.getBlock() instanceof BigCannonMunitionBlock munition)
			return munition.canBeLoaded(state, dir.getAxis());
		if (state.getBlock() instanceof BigCannonBlock cBlock)
			return cBlock.getOpeningType(level, state, pos) == BigCannonEnd.OPEN && cBlock.getFacing(state).getAxis() == dir.getAxis();
		return false;
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
		CBCTooltip.appendRamRodText(stack, level, tooltip, flag);
	}

	public static int getPushStrength() {
		return CBCConfigs.SERVER.cannons.ramRodStrength.get();
	}

	public static int getReach() {
		return CBCConfigs.SERVER.cannons.ramRodReach.get();
	}

	public static boolean deployersCanUse() {
		return CBCConfigs.SERVER.cannons.deployersCanUseLoadingTools.get();
	}

}
