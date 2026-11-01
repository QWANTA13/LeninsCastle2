package ru.leninscastle.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import ru.leninscastle.LeninsCastle;
import ru.leninscastle.entity.RavenEntity;

/**
 * Геометрия большого ворона из кубов в стиле Minecraft.
 * Модель процедурная: её можно расширять в Blockbench позднее.
 */
public class RavenModel extends HierarchicalModel<RavenEntity> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(
                    LeninsCastle.MOD_ID, "raven"), "main");

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final ModelPart tail;

    public RavenModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = body.getChild("head");
        this.leftWing = body.getChild("left_wing");
        this.rightWing = body.getChild("right_wing");
        this.tail = body.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0F, -4.0F, -5.0F, 8.0F, 8.0F, 11.0F),
                PartPose.offset(0.0F, 14.0F, 0.0F));

        body.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 20).addBox(-2.5F, -3.0F, -4.0F, 5.0F, 5.0F, 5.0F)
                        .texOffs(20, 20).addBox(-1.0F, -1.5F, -7.0F, 2.0F, 2.0F, 3.0F),
                PartPose.offset(0.0F, -2.0F, -5.0F));

        body.addOrReplaceChild("left_wing",
                CubeListBuilder.create()
                        .texOffs(0, 32).addBox(-1.0F, 0.0F, -4.0F, 2.0F, 2.0F, 10.0F)
                        .texOffs(24, 32).addBox(-1.0F, 0.0F, 2.0F, 2.0F, 1.0F, 7.0F),
                PartPose.offset(4.0F, -2.0F, -1.0F));

        body.addOrReplaceChild("right_wing",
                CubeListBuilder.create()
                        .texOffs(0, 32).addBox(-1.0F, 0.0F, -4.0F, 2.0F, 2.0F, 10.0F)
                        .texOffs(24, 32).addBox(-1.0F, 0.0F, 2.0F, 2.0F, 1.0F, 7.0F),
                PartPose.offset(-4.0F, -2.0F, -1.0F));

        body.addOrReplaceChild("tail",
                CubeListBuilder.create()
                        .texOffs(40, 0).addBox(-2.5F, -1.0F, 0.0F, 5.0F, 2.0F, 7.0F),
                PartPose.offset(0.0F, 0.0F, 5.0F));

        body.addOrReplaceChild("legs",
                CubeListBuilder.create()
                        .texOffs(40, 12).addBox(-3.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F)
                        .addBox(1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F),
                PartPose.offset(0.0F, 3.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(RavenEntity raven, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        head.xRot = headPitch * ((float)Math.PI / 180F);

        boolean flying = raven.isVehicle() || !raven.onGround();
        if (flying) {
            float flap = (float)Math.sin(ageInTicks * 0.9F) * 0.75F;
            leftWing.zRot = -0.25F - flap;
            rightWing.zRot = 0.25F + flap;
            body.xRot = -0.12F;
            tail.xRot = 0.12F;
        } else {
            float flap = (float)Math.sin(ageInTicks * 0.08F) * 0.04F;
            leftWing.zRot = -0.04F - flap;
            rightWing.zRot = 0.04F + flap;
            body.xRot = 0.0F;
            tail.xRot = 0.0F;
        }
    }
}