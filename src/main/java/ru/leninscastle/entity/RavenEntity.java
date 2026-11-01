package ru.leninscastle.entity;

import java.util.UUID;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import java.util.EnumSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;

/**
 * Большой ворон. Приручается мясом, может ждать/следовать за хозяином
 * и служит ездовым существом. Полётная физика будет дорабатываться
 * вместе с клиентскими анимациями и сетевым управлением.
 */
public class RavenEntity extends TamableAnimal {
    private static final EntityDataAccessor<Boolean> WAITING =
            SynchedEntityData.defineId(RavenEntity.class, EntityDataSerializers.BOOLEAN);

    public RavenEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.FLYING_SPEED, 0.42D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new Goal() {
            { this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP)); }
            @Override public boolean canUse() { return RavenEntity.this.isWaiting(); }
            @Override public boolean canContinueToUse() { return RavenEntity.this.isWaiting(); }
            @Override public void tick() {
                RavenEntity.this.getNavigation().stop();
                RavenEntity.this.setDeltaMovement(0.0D, RavenEntity.this.getDeltaMovement().y, 0.0D);
            }
        });
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.15D, 8.0F, 2.0F));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(WAITING, false);
    }

    public boolean isWaiting() {
        return this.entityData.get(WAITING);
    }

    public void setWaiting(boolean waiting) {
        this.entityData.set(WAITING, waiting);
        this.setOrderedToSit(waiting);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Waiting", this.isWaiting());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setWaiting(tag.getBoolean("Waiting"));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return isTamingFood(stack);
    }

    private boolean isTamingFood(ItemStack stack) {
        Item item = stack.getItem();
        return item == Items.BEEF || item == Items.PORKCHOP || item == Items.CHICKEN
                || item == Items.MUTTON || item == Items.RABBIT || item == Items.COD
                || item == Items.SALMON;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);

        if (!this.level().isClientSide && !this.isTame() && this.isTamingFood(held)) {
            if (!player.getAbilities().instabuild) {
                held.shrink(1);
            }
            if (this.random.nextInt(3) == 0) {
                this.tame(player);
                this.setWaiting(false);
                this.navigation.stop();
                this.level().broadcastEntityEvent(this, (byte) 7);
            } else {
                this.level().broadcastEntityEvent(this, (byte) 6);
            }
            return InteractionResult.CONSUME;
        }

        if (this.isTame() && this.isOwnedBy(player)) {
            if (player.isShiftKeyDown()) {
                if (!this.level().isClientSide) {
                    this.setWaiting(!this.isWaiting());
                    this.navigation.stop();
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            if (!this.level().isClientSide && !this.isVehicle()) {
                this.setWaiting(false);
                this.navigation.stop();
                player.startRiding(this);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public LivingEntity getControllingPassenger() {
        return this.getFirstPassenger() instanceof Player player ? player : null;
    }

    @Override
    public boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().isEmpty() && passenger instanceof Player && this.isTame();
    }

    @Override
    public void travel(Vec3 travelVector) {
        LivingEntity controller = this.getControllingPassenger();
        if (controller instanceof Player player) {
            this.setYRot(player.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(player.getXRot() * 0.35F);
            this.setNoGravity(true);

            // Управление направлением как у верхового животного.
            // Взгляд вверх поднимает ворона, взгляд вниз опускает.
            float forward = player.zza;
            float strafe = player.xxa;
            double vertical = -Math.sin(Math.toRadians(player.getXRot())) * 0.10D;
            if (player.isShiftKeyDown()) {
                vertical = -0.12D;
            }
            Vec3 look = player.getLookAngle();
            Vec3 horizontal = new Vec3(look.x, 0.0D, look.z);
            if (horizontal.lengthSqr() > 1.0E-5D) {
                horizontal = horizontal.normalize();
            }
            Vec3 movement = horizontal.scale(forward * 0.34D)
                    .add(-horizontal.z * strafe * 0.22D, vertical,
                            horizontal.x * strafe * 0.22D);
            this.setDeltaMovement(movement);
            this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
            this.setSpeed(0.0F);
            return;
        }

        this.setNoGravity(false);
        super.travel(travelVector);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.isVehicle()) {
            this.setNoGravity(true);
        } else if (!this.level().isClientSide && !this.isVehicle()) {
            this.setNoGravity(false);
        }
    }

    @Override
    protected void dropCustomDeathLoot(net.minecraft.server.level.ServerLevel level,
                                       net.minecraft.world.damagesource.DamageSource source,
                                       boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        this.spawnAtLocation(new ItemStack(ru.leninscastle.registry.ModItems.RAVEN_FEATHER.get()));
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier,
                                   net.minecraft.world.damagesource.DamageSource source) {
        // Ворон сам по себе не получает урон от приземления.
        return false;
    }
}