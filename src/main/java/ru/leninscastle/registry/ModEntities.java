package ru.leninscastle.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.leninscastle.LeninsCastle;
import ru.leninscastle.entity.RavenEntity;
import ru.leninscastle.entity.LeninBossEntity;

public final class ModEntities {
    private ModEntities() {}

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, LeninsCastle.MOD_ID);

    public static final RegistryObject<EntityType<RavenEntity>> RAVEN =
            ENTITY_TYPES.register("raven", () -> EntityType.Builder
                    .of(RavenEntity::new, MobCategory.CREATURE)
                    .sized(1.15F, 1.35F)
                    .clientTrackingRange(10)
                    .build("raven"));

    public static final RegistryObject<EntityType<LeninBossEntity>> LENIN_BOSS =
            ENTITY_TYPES.register("lenin_boss", () -> EntityType.Builder
                    .of(LeninBossEntity::new, MobCategory.MONSTER)
                    .sized(0.9F, 2.4F)
                    .clientTrackingRange(10)
                    .fireImmune()
                    .build("lenin_boss"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}