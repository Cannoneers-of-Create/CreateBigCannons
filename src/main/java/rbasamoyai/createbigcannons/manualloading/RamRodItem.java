package rbasamoyai.createbigcannons.manualloading;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.simibubi.create.content.contraptions.components.deployer.DeployerFakePlayer;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.base.CBCTooltip;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonEnd;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.ProjectileBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RamRodItem extends Item {

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
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getAttributeModifiers(slot, stack);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		if (player instanceof DeployerFakePlayer && !deployersCanUse()) return InteractionResult.PASS;
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Direction pushDirection = context.getClickedFace().getOpposite();
		
		int k = 0;
		if (level.getBlockEntity(pos) instanceof IBigCannonBlockEntity) {
			k = -1;
			for (int i = 0; i < getReach(); ++i) {
				BlockPos pos1 = pos.relative(pushDirection, i);
				BlockState state1 = level.getBlockState(pos1);
				if (state1.isAir()) continue;
				if (!isValidLoadBlock(state1, level, pos1, pushDirection)) return InteractionResult.FAIL;
				
				if (level.getBlockEntity(pos1) instanceof IBigCannonBlockEntity cbe) {
					StructureBlockInfo info = cbe.cannonBehavior().block();
					if (info == null || info.state == null || info.state.isAir()) continue;
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
			
			if (level.getBlockEntity(pos1) instanceof IBigCannonBlockEntity cbe) {
				encounteredCannon = true;
				StructureBlockInfo info = cbe.cannonBehavior().block();
				if (info == null || info.state == null || info.state.isAir()) break;
				toPush.add(info);
			} else {
				BlockEntity be = level.getBlockEntity(pos1);
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
		if (!level.isClientSide) {
			for (int i = toPush.size() - 1; i >= 0; --i) {
				BlockPos pos1 = pos.relative(pushDirection, i + k);
				
				if (level.getBlockEntity(pos1) instanceof IBigCannonBlockEntity cbe) cbe.cannonBehavior().removeBlock();
				else level.removeBlock(pos1, false);
				
				StructureBlockInfo info = toPush.get(i);
				BlockPos pos2 = pos1.relative(pushDirection);
				if (level.getBlockEntity(pos2) instanceof IBigCannonBlockEntity cbe) {
					cbe.cannonBehavior().tryLoadingBlock(info);
				} else {
					level.setBlock(pos2, info.state, Block.UPDATE_MOVE_BY_PISTON | Block.UPDATE_ALL);
					BlockEntity be = level.getBlockEntity(pos2);
					CompoundTag tag = info.nbt;
					if (be != null) tag = NBTProcessors.process(be, tag, false);
					if (be != null && tag != null) {
						tag.putInt("x", pos2.getX());
						tag.putInt("y", pos2.getY());
						tag.putInt("z", pos2.getZ());
						be.load(tag);
					}
				}
			}
			level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.PLAYERS, 1, 1);
		}
		player.causeFoodExhaustion(toPush.size() * CBCConfigs.SERVER.cannons.loadingToolHungerConsumption.getF());
		player.getCooldowns().addCooldown(this, CBCConfigs.SERVER.cannons.loadingToolCooldown.get());
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
	
	public static boolean isValidLoadBlock(BlockState state, Level level, BlockPos pos, Direction dir) {
		if (CBCBlocks.POWDER_CHARGE.has(state))
			return state.getValue(BlockStateProperties.AXIS) == dir.getAxis();
		if (state.getBlock() instanceof ProjectileBlock)
			return state.getValue(BlockStateProperties.FACING).getAxis() == dir.getAxis();
		if (state.getBlock() instanceof BigCannonBlock cBlock)
			return cBlock.getOpeningType(level, state, pos) == BigCannonEnd.OPEN && cBlock.getFacing(state).getAxis() == dir.getAxis();
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		CBCTooltip.appendRamRodText(stack, level, tooltip, flag);
	}
	
	public static int getPushStrength() { return CBCConfigs.SERVER.cannons.ramRodStrength.get(); }
	public static int getReach() { return CBCConfigs.SERVER.cannons.ramRodReach.get(); }
	public static boolean deployersCanUse() { return CBCConfigs.SERVER.cannons.deployersCanUseLoadingTools.get(); }
	
}
