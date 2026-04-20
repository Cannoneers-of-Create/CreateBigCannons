package rbasamoyai.createbigcannons.compat.sable;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.entity.EntitySubLevelUtil;
import dev.ryanhcode.sable.mixinhelpers.CanFallAtleastHelper;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import rbasamoyai.createbigcannons.CBCCompatTransformers;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

public class SableCannonProjectileCompat {

    public static class ProjectileFallHandler implements CBCCompatTransformers.ProjectileFallHandler {
        @Override
        public boolean shouldFall(Level level, AbstractCannonProjectile projectile, AABB collision) {
            return CanFallAtleastHelper.canFallAtleastWithSubLevels(level, collision) == null;
        }

        @Override
        public void onFall(AbstractCannonProjectile projectile) {
            SubLevel sublevel = Sable.HELPER.getContaining(projectile);
            if (sublevel != null)
                EntitySubLevelUtil.kickEntity(sublevel, projectile);
        }
    }

}
