package rbasamoyai.createbigcannons.datagen;

import com.simibubi.create.Create;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CBCBlockPartialsGen extends BlockModelProvider {

	public CBCBlockPartialsGen(DataGenerator gen, ExistingFileHelper helper) {
		super(gen, CreateBigCannons.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		autocannonPartials("autocannon/cast_iron");
		autocannonPartials("autocannon/bronze");
		autocannonPartials("autocannon/steel");

		autocannonSeat();
	}

	private void autocannonPartials(String pathAndMaterial) {
		getBuilder("block/" + pathAndMaterial + "_autocannon_spring")
				.parent(getExistingFile(CreateBigCannons.resource("block/autocannon/spring")))
				.texture("material", CreateBigCannons.resource("block/" + pathAndMaterial + "_autocannon"));

		getBuilder("block/" + pathAndMaterial + "_autocannon_ejector")
				.parent(getExistingFile(CreateBigCannons.resource("block/autocannon/ejector")))
				.texture("material", CreateBigCannons.resource("block/" + pathAndMaterial + "_autocannon"));
	}

	private void autocannonSeat() {
		for (DyeColor color : DyeColor.values()) {
			String s = color.getSerializedName();
			getBuilder("block/autocannon/seat_" + s)
					.parent(getExistingFile(CreateBigCannons.resource("block/autocannon/seat")))
					.texture("side", Create.asResource("block/seat/side_" + s))
					.texture("top", Create.asResource("block/seat/top_" + s));
		}
	}

	@NotNull
	@Override
	public String getName() {
		return "CBC Block Partials";
	}
}
