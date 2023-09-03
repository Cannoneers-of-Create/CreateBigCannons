package rbasamoyai.createbigcannons.fabric.datagen.assets;

import javax.annotation.Nonnull;

import com.simibubi.create.Create;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import io.github.fabricators_of_create.porting_lib.models.generators.block.BlockModelProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CBCBlockPartialsGen extends BlockModelProvider {

	public CBCBlockPartialsGen(PackOutput output, ExistingFileHelper helper) {
		super(output, CreateBigCannons.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		autocannonPartials("autocannon/cast_iron");
		autocannonPartials("autocannon/bronze");
		autocannonPartials("autocannon/steel");

		//autocannonSeat();
	}

	private void autocannonPartials(String pathAndMaterial) {
		getBuilder("block/" + pathAndMaterial + "_autocannon_spring")
				.parent(getExistingFile(CreateBigCannons.resource("block/autocannon/spring")))
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

	@Nonnull
	@Override
	public String getName() {
		return "CBC Block Partials";
	}
}
