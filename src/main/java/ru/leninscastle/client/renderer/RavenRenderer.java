package ru.leninscastle.client.renderer;

import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import ru.leninscastle.LeninsCastle;
import ru.leninscastle.client.model.RavenModel;
import ru.leninscastle.entity.RavenEntity;

public class RavenRenderer extends MobRenderer<RavenEntity, RavenModel> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(LeninsCastle.MOD_ID,
                    "textures/entity/raven.png");

    public RavenRenderer(EntityRendererProvider.Context context) {
        super(context, new RavenModel(context.bakeLayer(RavenModel.LAYER_LOCATION)), 0.65F);
    }

    @Override
    public ResourceLocation getTextureLocation(RavenEntity entity) {
        return TEXTURE;
    }
}