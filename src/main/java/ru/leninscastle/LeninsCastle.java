package ru.leninscastle;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import ru.leninscastle.registry.ModItems;
import ru.leninscastle.registry.ModEntities;
import ru.leninscastle.entity.RavenEntity;
import ru.leninscastle.entity.LeninBossEntity;

@Mod(LeninsCastle.MOD_ID)
public class LeninsCastle {
    public static final String MOD_ID = "leninscastle";

    public LeninsCastle() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);
        ru.leninscastle.registry.ModFeatures.register(modEventBus);
        modEventBus.addListener(this::registerAttributes);
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.RAVEN.get(), RavenEntity.createAttributes().build());
        event.put(ModEntities.LENIN_BOSS.get(), LeninBossEntity.createAttributes().build());
    }
}