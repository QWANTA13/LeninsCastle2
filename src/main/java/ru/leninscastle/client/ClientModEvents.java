package ru.leninscastle.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.leninscastle.LeninsCastle;
import ru.leninscastle.client.model.RavenModel;
import ru.leninscastle.client.renderer.RavenRenderer;
import ru.leninscastle.entity.LeninBossEntity;
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

        // Временная модель Ленина (как игрок), чтобы не было краша
        event.registerEntityRenderer(ModEntities.LENIN_BOSS.get(), context ->
                new HumanoidMobRenderer<LeninBossEntity, HumanoidModel<LeninBossEntity>>(
                        context,
                        new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)),
                        0.5F) {
                    @Override
                    public ResourceLocation getTextureLocation(LeninBossEntity entity) {
                        return ResourceLocation.fromNamespaceAndPath(
        LeninsCastle.MOD_ID, "textures/entity/lenin.png");
                    }
                });
    }
}
