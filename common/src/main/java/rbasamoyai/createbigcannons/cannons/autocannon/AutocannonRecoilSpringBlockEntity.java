package rbasamoyai.createbigcannons.cannons.autocannon;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

import java.util.HashMap;
import java.util.Map;

public class AutocannonRecoilSpringBlockEntity extends AutocannonBlockEntity implements AnimatedAutocannon {

    public Map<BlockPos, BlockState> toAnimate = new HashMap<>();

    private int animateTicks = 5;

    public AutocannonRecoilSpringBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        this.allTick();
    }

    @Override
    public void tickFromContraption(Level level, PitchOrientedContraptionEntity poce, BlockPos localPos) {
        super.tickFromContraption(level, poce, localPos);
        this.allTick();
    }

    private void allTick() {
        if (this.animateTicks < 5) ++this.animateTicks;
        if (this.animateTicks < 0) this.animateTicks = 0;
    }

    public void handleFiring() {
        this.animateTicks = 0;
    }

    public float getAnimateOffset(float partialTicks) {
        float t = ((float) this.animateTicks + partialTicks) * 1.2f;
        if (t <= 0 || t >= 4.8f) return 1;
        float f = t < 1 ? t : (4.8f - t) / 3.8f;
        return Mth.cos(f * Mth.HALF_PI);
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);

        tag.putInt("AnimateTicks", this.animateTicks);

        ListTag renderedList = new ListTag();
        for (Map.Entry<BlockPos, BlockState> entry : this.toAnimate.entrySet()) {
            CompoundTag block = new CompoundTag();
            block.put("Pos", NbtUtils.writeBlockPos(entry.getKey()));
            block.put("Block", NbtUtils.writeBlockState(entry.getValue()));
            renderedList.add(block);
        }

        tag.put("RenderedBlocks", renderedList);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);

        this.animateTicks = tag.getInt("AnimateTicks");

        this.toAnimate.clear();
        ListTag renderedList = tag.getList("RenderedBlocks", Tag.TAG_COMPOUND);
        for (int i = 0; i < renderedList.size(); ++i) {
            CompoundTag block = renderedList.getCompound(i);
            this.toAnimate.put(NbtUtils.readBlockPos(block.getCompound("Pos")), NbtUtils.readBlockState(block.getCompound("Block")));
        }
    }

    @Override public void incrementAnimationTicks() { ++this.animateTicks; }
    @Override public int getAnimationTicks() { return this.animateTicks; }

}
