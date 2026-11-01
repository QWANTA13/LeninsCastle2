package ru.leninscastle.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.leninscastle.LeninsCastle;
import ru.leninscastle.item.KingsSwordItem;
import ru.leninscastle.item.RavenBootsItem;

public final class ModItems {
    private ModItems() {}

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, LeninsCastle.MOD_ID);

    public static final RegistryObject<Item> RAVEN_FEATHER =
            ITEMS.register("raven_feather", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> KINGS_SWORD =
            ITEMS.register("kings_sword", KingsSwordItem::new);

    public static final RegistryObject<Item> RAVEN_BOOTS =
            ITEMS.register("raven_boots", RavenBootsItem::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}