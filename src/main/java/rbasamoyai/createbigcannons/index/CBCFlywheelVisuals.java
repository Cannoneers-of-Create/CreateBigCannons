package rbasamoyai.createbigcannons.index;

import com.simibubi.create.content.kinetics.base.ShaftVisual;

import dev.engine_room.flywheel.api.visualization.VisualizerRegistry;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;

import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountVisual;
import rbasamoyai.createbigcannons.cannons.autocannon.breech.AutocannonBreechVisual;
import rbasamoyai.createbigcannons.cannons.autocannon.recoil_spring.AutocannonRecoilSpringVisual;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.QuickfiringBreechVisual;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.screw_breech.ScrewBreechVisual;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.sliding_breech.SlidingBreechVisual;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBlockVisual;

public final class CBCFlywheelVisuals {
    private CBCFlywheelVisuals() {}

    public static void registerVisuals() {
        VisualizerRegistry.setVisualizer(CBCBlockEntities.SLIDING_BREECH.get(),
            new SimpleBlockEntityVisualizer<>(SlidingBreechVisual::new, be -> false));

        VisualizerRegistry.setVisualizer(CBCBlockEntities.SCREW_BREECH.get(),
            new SimpleBlockEntityVisualizer<>(ScrewBreechVisual::new, be -> false));

        VisualizerRegistry.setVisualizer(CBCBlockEntities.QUICKFIRING_BREECH.get(),
            new SimpleBlockEntityVisualizer<>(QuickfiringBreechVisual::new, be -> false));

        VisualizerRegistry.setVisualizer(CBCBlockEntities.CANNON_LOADER.get(),
            new SimpleBlockEntityVisualizer<>(ShaftVisual::new, be -> false));

        VisualizerRegistry.setVisualizer(CBCBlockEntities.CANNON_MOUNT.get(),
            new SimpleBlockEntityVisualizer<>(CannonMountVisual::new, be -> false));

        VisualizerRegistry.setVisualizer(CBCBlockEntities.YAW_CONTROLLER.get(),
            new SimpleBlockEntityVisualizer<>(ShaftVisual::new, be -> false));

        VisualizerRegistry.setVisualizer(CBCBlockEntities.CANNON_MOUNT_EXTENSION.get(),
            new SimpleBlockEntityVisualizer<>(ShaftVisual::new, be -> false));

        VisualizerRegistry.setVisualizer(CBCBlockEntities.FUZED_BLOCK.get(),
            new SimpleBlockEntityVisualizer<>(FuzedBlockVisual::new, be -> false));

        VisualizerRegistry.setVisualizer(CBCBlockEntities.FLUID_SHELL.get(),
            new SimpleBlockEntityVisualizer<>(FuzedBlockVisual::new, be -> false));

        VisualizerRegistry.setVisualizer(CBCBlockEntities.CANNON_DRILL.get(),
            new SimpleBlockEntityVisualizer<>(ShaftVisual::new, be -> false));

        VisualizerRegistry.setVisualizer(CBCBlockEntities.CANNON_BUILDER.get(),
            new SimpleBlockEntityVisualizer<>(ShaftVisual::new, be -> false));

        VisualizerRegistry.setVisualizer(CBCBlockEntities.AUTOCANNON_BREECH.get(),
            new SimpleBlockEntityVisualizer<>(AutocannonBreechVisual::new, be -> false));

        VisualizerRegistry.setVisualizer(CBCBlockEntities.AUTOCANNON_RECOIL_SPRING.get(),
            new SimpleBlockEntityVisualizer<>(AutocannonRecoilSpringVisual::new, be -> false));
    }
}
