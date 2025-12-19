package rbasamoyai.createbigcannons.index;

import com.simibubi.create.content.contraptions.render.ContraptionVisual;

import dev.engine_room.flywheel.api.visualization.VisualizerRegistry;
import dev.engine_room.flywheel.lib.visualization.SimpleEntityVisualizer;

import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

public final class CBCEntityVisuals {
    private CBCEntityVisuals() {}

    public static void register() {
        VisualizerRegistry.setVisualizer(
            CBCEntityTypes.PITCH_ORIENTED_CONTRAPTION.get(),
            new SimpleEntityVisualizer<PitchOrientedContraptionEntity>(ContraptionVisual::new, entity -> false)
        );
    }
}
