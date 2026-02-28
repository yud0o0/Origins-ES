package yud0o0.main;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import yud0o0.main.powers.passive.powers.ESWaterBomb;

public class ESEntities {
    public static EntityType<ESWaterBomb.WaterBombEntity> WATER_BOMB_TYPE;

    public static void register() {
        WATER_BOMB_TYPE = Registry.register(
                Registries.ENTITY_TYPE,
                Identifier.of("origins-es", "water_bomb"),
                EntityType.Builder.<ESWaterBomb.WaterBombEntity>create(ESWaterBomb.WaterBombEntity::new, SpawnGroup.MISC)
                        .dimensions(0.25F, 0.25F)
                        .build()
        );
    }
}
