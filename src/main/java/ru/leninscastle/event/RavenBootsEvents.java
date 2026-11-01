package ru.leninscastle.event;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.leninscastle.LeninsCastle;
import ru.leninscastle.registry.ModItems;

@Mod.EventBusSubscriber(modid = LeninsCastle.MOD_ID)
public final class RavenBootsEvents {
    private RavenBootsEvents() {}

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (player.level().isClientSide || player.isSpectator()) return;

        if (player.tickCount % 20 == 0
                && player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.FEET)
                .is(ModItems.RAVEN_BOOTS.get())) {
            // Короткое обновляемое действие не даёт эффекту пропасть, пока сапоги надеты.
            player.addEffect(new MobEffectInstance(
                    MobEffects.SLOW_FALLING, 40, 0, true, false, true));
        }
    }
}