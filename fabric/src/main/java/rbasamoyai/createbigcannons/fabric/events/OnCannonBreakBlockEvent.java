package rbasamoyai.createbigcannons.fabric.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import rbasamoyai.createbigcannons.multiloader.event_classes.OnCannonBreakBlock;

public interface OnCannonBreakBlockEvent {
	Event<OnCannonBreakBlockEvent> EVENT = EventFactory.createArrayBacked(OnCannonBreakBlockEvent.class,
			(listeners) -> (event) -> {
				for (OnCannonBreakBlockEvent listener : listeners) {
					listener.OnCannonBreakBlockImpl(event);
				}
			}
	);

	void OnCannonBreakBlockImpl(OnCannonBreakBlock event);
}