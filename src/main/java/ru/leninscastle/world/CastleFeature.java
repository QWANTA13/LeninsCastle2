package ru.leninscastle.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Редкая процедурная генерация замка в обычном мире.
 * Генерирует компактную крепость на поверхности; босс не создаётся во время
 * worldgen, чтобы не создавать сущностей в небезопасной фазе генерации чанков.
 */
public class CastleFeature extends Feature<NoneFeatureConfiguration> {
    private static final BlockState WALL = Blocks.STONE_BRICKS.defaultBlockState();
    private static final BlockState ROOF = Blocks.DEEPSLATE_BRICKS.defaultBlockState();
    private static final BlockState FLOOR = Blocks.POLISHED_ANDESITE.defaultBlockState();

    public CastleFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        int x0 = origin.getX();
        int z0 = origin.getZ();
        int y0 = origin.getY();

        // Проверка свободной площадки и умеренного перепада высот.
        for (int x = -2; x <= 34; x += 6) {
            for (int z = -2; z <= 34; z += 6) {
                int surfaceY = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE_WG,
                        x0 + x, z0 + z);
                if (Math.abs(surfaceY - y0) > 5) return false;
            }
        }

        // Основание и двор 33x33.
        fill(level, x0, y0 - 1, z0, 33, 1, 33, FLOOR);
        fill(level, x0 + 3, y0, z0 + 3, 27, 1, 27, FLOOR);

        // Стены и ворота с южной стороны.
        for (int y = 1; y <= 7; y++) {
            for (int i = 3; i <= 29; i++) {
                set(level, x0 + i, y0 + y, z0 + 3, WALL);
                set(level, x0 + i, y0 + y, z0 + 29, WALL);
                set(level, x0 + 3, y0 + y, z0 + i, WALL);
                set(level, x0 + 29, y0 + y, z0 + i, WALL);
            }
            if (y <= 5) {
                for (int x = 14; x <= 18; x++)
                    set(level, x0 + x, y0 + y, z0 + 3, Blocks.AIR.defaultBlockState());
            }
        }

        // Зубцы.
        for (int i = 3; i <= 29; i += 2) {
            set(level, x0 + i, y0 + 8, z0 + 3, ROOF);
            set(level, x0 + i, y0 + 8, z0 + 29, ROOF);
            set(level, x0 + 3, y0 + 8, z0 + i, ROOF);
            set(level, x0 + 29, y0 + 8, z0 + i, ROOF);
        }

        // Четыре башни и главный донжон.
        tower(level, x0 + 3, y0, z0 + 3);
        tower(level, x0 + 25, y0, z0 + 3);
        tower(level, x0 + 3, y0, z0 + 25);
        tower(level, x0 + 25, y0, z0 + 25);
        keep(level, x0 + 10, y0, z0 + 10, 23, 21, 23);

        // Тронный зал с ковровой дорожкой и золотым троном.
        fill(level, x0 + 13, y0 + 1, z0 + 16, 8, 1, 6, Blocks.RED_CARPET.defaultBlockState());
        set(level, x0 + 17, y0 + 2, z0 + 21, Blocks.GOLD_BLOCK.defaultBlockState());
        set(level, x0 + 17, y0 + 3, z0 + 21, Blocks.GOLD_BLOCK.defaultBlockState());
        set(level, x0 + 16, y0 + 2, z0 + 21, Blocks.GOLD_BLOCK.defaultBlockState());
        set(level, x0 + 18, y0 + 2, z0 + 21, Blocks.GOLD_BLOCK.defaultBlockState());

        // Ворота и декоративные фонари.
        set(level, x0 + 13, y0 + 2, z0 + 2, Blocks.LANTERN.defaultBlockState());
        set(level, x0 + 19, y0 + 2, z0 + 2, Blocks.LANTERN.defaultBlockState());
        return true;
    }

    private static void tower(WorldGenLevel level, int x, int y, int z) {
        keep(level, x, y, z, x + 4, y + 13, z + 4);
        for (int i = 0; i < 5; i++) {
            set(level, x + i, y + 14, z, ROOF);
            set(level, x + i, y + 14, z + 4, ROOF);
            set(level, x, y + 14, z + i, ROOF);
            set(level, x + 4, y + 14, z + i, ROOF);
        }
        set(level, x + 2, y + 13, z + 2, Blocks.LANTERN.defaultBlockState());
    }

    private static void keep(WorldGenLevel level, int x1, int y1, int z1,
                             int x2, int y2, int z2) {
        for (int x = x1; x <= x2; x++)
            for (int y = y1; y <= y2; y++)
                for (int z = z1; z <= z2; z++) {
                    boolean edge = x == x1 || x == x2 || z == z1 || z == z2
                            || y == y1 || y == y2;
                    set(level, x, y, z, edge ? WALL : Blocks.AIR.defaultBlockState());
                }
        fill(level, x1 - 1, y2 + 1, z1 - 1, x2 - x1 + 3, 1, z2 - z1 + 3, ROOF);
    }

    private static void fill(WorldGenLevel level, int x, int y, int z,
                              int sx, int sy, int sz, BlockState state) {
        for (int dx = 0; dx < sx; dx++)
            for (int dy = 0; dy < sy; dy++)
                for (int dz = 0; dz < sz; dz++)
                    set(level, x + dx, y + dy, z + dz, state);
    }

    private static void set(WorldGenLevel level, int x, int y, int z, BlockState state) {
        level.setBlock(new BlockPos(x, y, z), state, 3);
    }
}