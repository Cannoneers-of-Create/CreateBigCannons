package rbasamoyai.createbigcannons.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class ProjectileDamageEvent extends Event implements ICancellableEvent {
    private final Level level;
    private final BlockPos pos;

    public ProjectileDamageEvent(Level level, BlockPos pos){
        this.level = level;
        this.pos = pos;
    }

    public Level getLevel(){
        return this.level;
    }

    public BlockPos getPos(){
        return this.pos;
    }
}
