package rbasamoyai.createbigcannons.munitions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import rbasamoyai.createbigcannons.CBCCompatTransformers;
import rbasamoyai.createbigcannons.events.ProjectileDamageEvent;

public class ProjectileDamageHooks {
    private ProjectileDamageHooks(){
    }

    public static boolean canDamageTerrain(Level level, BlockPos pos){
        if(level.isClientSide) {
            return true;
        }
        BlockPos realPos = CBCCompatTransformers.transformBlockPos(level, pos);
        return !NeoForge.EVENT_BUS.post(new ProjectileDamageEvent(level, realPos)).isCanceled();
    }
}
