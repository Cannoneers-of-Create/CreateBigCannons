package rbasamoyai.createbigcannons.utils;

import java.util.function.Function;

import org.joml.Vector4f;

import com.mojang.datafixers.util.Function10;
import com.mojang.datafixers.util.Function7;
import com.mojang.datafixers.util.Function8;
import com.mojang.datafixers.util.Function9;

import io.netty.buffer.ByteBuf;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

public class CBCStreamCodecs {

    public static final StreamCodec<ByteBuf, SoundSource> SOUND_SOURCE = CatnipStreamCodecBuilders.ofEnum(SoundSource.class);

    public static final StreamCodec<ByteBuf, StructureBlockInfo> STRUCTURE_BLOCK_INFO = StreamCodec.composite(
        BlockPos.STREAM_CODEC, StructureBlockInfo::pos,
        CatnipStreamCodecs.BLOCK_STATE, StructureBlockInfo::state,
        CatnipStreamCodecBuilders.nullable(ByteBufCodecs.COMPOUND_TAG), StructureBlockInfo::nbt, // TODO c6 playtest
        StructureBlockInfo::new);

    public static final StreamCodec<ByteBuf, Vector4f> VECTOR_4F = StreamCodec.composite(
        ByteBufCodecs.FLOAT, Vector4f::x,
        ByteBufCodecs.FLOAT, Vector4f::y,
        ByteBufCodecs.FLOAT, Vector4f::z,
        ByteBufCodecs.FLOAT, Vector4f::w,
        Vector4f::new);

    /**
     * Copied from NeoForgeStreamCodecs
     */
    public static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> composite(
        final StreamCodec<? super B, T1> codec1,
        final Function<C, T1> getter1,
        final StreamCodec<? super B, T2> codec2,
        final Function<C, T2> getter2,
        final StreamCodec<? super B, T3> codec3,
        final Function<C, T3> getter3,
        final StreamCodec<? super B, T4> codec4,
        final Function<C, T4> getter4,
        final StreamCodec<? super B, T5> codec5,
        final Function<C, T5> getter5,
        final StreamCodec<? super B, T6> codec6,
        final Function<C, T6> getter6,
        final StreamCodec<? super B, T7> codec7,
        final Function<C, T7> getter7,
        final Function7<T1, T2, T3, T4, T5, T6, T7, C> cons) {
        return new StreamCodec<>() {
            @Override
            public C decode(B buf) {
                T1 t1 = codec1.decode(buf);
                T2 t2 = codec2.decode(buf);
                T3 t3 = codec3.decode(buf);
                T4 t4 = codec4.decode(buf);
                T5 t5 = codec5.decode(buf);
                T6 t6 = codec6.decode(buf);
                T7 t7 = codec7.decode(buf);
                return cons.apply(t1, t2, t3, t4, t5, t6, t7);
            }

            @Override
            public void encode(B buf, C coded) {
                codec1.encode(buf, getter1.apply(coded));
                codec2.encode(buf, getter2.apply(coded));
                codec3.encode(buf, getter3.apply(coded));
                codec4.encode(buf, getter4.apply(coded));
                codec5.encode(buf, getter5.apply(coded));
                codec6.encode(buf, getter6.apply(coded));
                codec7.encode(buf, getter7.apply(coded));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8> StreamCodec<B, C> composite(
        final StreamCodec<? super B, T1> codec1,
        final Function<C, T1> getter1,
        final StreamCodec<? super B, T2> codec2,
        final Function<C, T2> getter2,
        final StreamCodec<? super B, T3> codec3,
        final Function<C, T3> getter3,
        final StreamCodec<? super B, T4> codec4,
        final Function<C, T4> getter4,
        final StreamCodec<? super B, T5> codec5,
        final Function<C, T5> getter5,
        final StreamCodec<? super B, T6> codec6,
        final Function<C, T6> getter6,
        final StreamCodec<? super B, T7> codec7,
        final Function<C, T7> getter7,
        final StreamCodec<? super B, T8> codec8,
        final Function<C, T8> getter8,
        final Function8<T1, T2, T3, T4, T5, T6, T7, T8, C> cons) {
        return new StreamCodec<>() {
            @Override
            public C decode(B buf) {
                T1 t1 = codec1.decode(buf);
                T2 t2 = codec2.decode(buf);
                T3 t3 = codec3.decode(buf);
                T4 t4 = codec4.decode(buf);
                T5 t5 = codec5.decode(buf);
                T6 t6 = codec6.decode(buf);
                T7 t7 = codec7.decode(buf);
                T8 t8 = codec8.decode(buf);
                return cons.apply(t1, t2, t3, t4, t5, t6, t7, t8);
            }

            @Override
            public void encode(B buf, C coded) {
                codec1.encode(buf, getter1.apply(coded));
                codec2.encode(buf, getter2.apply(coded));
                codec3.encode(buf, getter3.apply(coded));
                codec4.encode(buf, getter4.apply(coded));
                codec5.encode(buf, getter5.apply(coded));
                codec6.encode(buf, getter6.apply(coded));
                codec7.encode(buf, getter7.apply(coded));
                codec8.encode(buf, getter8.apply(coded));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9> StreamCodec<B, C> composite(
        final StreamCodec<? super B, T1> codec1,
        final Function<C, T1> getter1,
        final StreamCodec<? super B, T2> codec2,
        final Function<C, T2> getter2,
        final StreamCodec<? super B, T3> codec3,
        final Function<C, T3> getter3,
        final StreamCodec<? super B, T4> codec4,
        final Function<C, T4> getter4,
        final StreamCodec<? super B, T5> codec5,
        final Function<C, T5> getter5,
        final StreamCodec<? super B, T6> codec6,
        final Function<C, T6> getter6,
        final StreamCodec<? super B, T7> codec7,
        final Function<C, T7> getter7,
        final StreamCodec<? super B, T8> codec8,
        final Function<C, T8> getter8,
        final StreamCodec<? super B, T9> codec9,
        final Function<C, T9> getter9,
        final Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, C> cons) {
        return new StreamCodec<>() {
            @Override
            public C decode(B buf) {
                T1 t1 = codec1.decode(buf);
                T2 t2 = codec2.decode(buf);
                T3 t3 = codec3.decode(buf);
                T4 t4 = codec4.decode(buf);
                T5 t5 = codec5.decode(buf);
                T6 t6 = codec6.decode(buf);
                T7 t7 = codec7.decode(buf);
                T8 t8 = codec8.decode(buf);
                T9 t9 = codec9.decode(buf);
                return cons.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9);
            }

            @Override
            public void encode(B buf, C coded) {
                codec1.encode(buf, getter1.apply(coded));
                codec2.encode(buf, getter2.apply(coded));
                codec3.encode(buf, getter3.apply(coded));
                codec4.encode(buf, getter4.apply(coded));
                codec5.encode(buf, getter5.apply(coded));
                codec6.encode(buf, getter6.apply(coded));
                codec7.encode(buf, getter7.apply(coded));
                codec8.encode(buf, getter8.apply(coded));
                codec9.encode(buf, getter9.apply(coded));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> StreamCodec<B, C> composite(
        final StreamCodec<? super B, T1> codec1,
        final Function<C, T1> getter1,
        final StreamCodec<? super B, T2> codec2,
        final Function<C, T2> getter2,
        final StreamCodec<? super B, T3> codec3,
        final Function<C, T3> getter3,
        final StreamCodec<? super B, T4> codec4,
        final Function<C, T4> getter4,
        final StreamCodec<? super B, T5> codec5,
        final Function<C, T5> getter5,
        final StreamCodec<? super B, T6> codec6,
        final Function<C, T6> getter6,
        final StreamCodec<? super B, T7> codec7,
        final Function<C, T7> getter7,
        final StreamCodec<? super B, T8> codec8,
        final Function<C, T8> getter8,
        final StreamCodec<? super B, T9> codec9,
        final Function<C, T9> getter9,
        final StreamCodec<? super B, T10> codec10,
        final Function<C, T10> getter10,
        final Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, C> cons) {
        return new StreamCodec<>() {
            @Override
            public C decode(B buf) {
                T1 t1 = codec1.decode(buf);
                T2 t2 = codec2.decode(buf);
                T3 t3 = codec3.decode(buf);
                T4 t4 = codec4.decode(buf);
                T5 t5 = codec5.decode(buf);
                T6 t6 = codec6.decode(buf);
                T7 t7 = codec7.decode(buf);
                T8 t8 = codec8.decode(buf);
                T9 t9 = codec9.decode(buf);
                T10 t10 = codec10.decode(buf);
                return cons.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
            }

            @Override
            public void encode(B buf, C coded) {
                codec1.encode(buf, getter1.apply(coded));
                codec2.encode(buf, getter2.apply(coded));
                codec3.encode(buf, getter3.apply(coded));
                codec4.encode(buf, getter4.apply(coded));
                codec5.encode(buf, getter5.apply(coded));
                codec6.encode(buf, getter6.apply(coded));
                codec7.encode(buf, getter7.apply(coded));
                codec8.encode(buf, getter8.apply(coded));
                codec9.encode(buf, getter9.apply(coded));
                codec10.encode(buf, getter10.apply(coded));
            }
        };
    }

}
