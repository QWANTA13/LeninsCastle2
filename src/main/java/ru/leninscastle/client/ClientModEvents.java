package ru.leninscastle.client;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.leninscastle.LeninsCastle;
import ru.leninscastle.client.model.RavenModel;
import ru.leninscastle.client.renderer.RavenRenderer;
import ru.leninscastle.registry.ModEntities;

@Mod.EventBusSubscriber(modid = LeninsCastle.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
    private ClientModEvents() {}

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(RavenModel.LAYER_LOCATION, RavenModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.RAVEN.get(), RavenRenderer::new);
    }
}