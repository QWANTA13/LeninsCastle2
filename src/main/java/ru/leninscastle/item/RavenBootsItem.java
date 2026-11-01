package ru.leninscastle.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;

/**
 * Вороньи сапоги: кожаные сапоги с эффектом плавного падения.
 * Эффект выдаётся обработчиком RavenBootsEvents при ношении.
 */
public class RavenBootsItem extends ArmorItem {
    public RavenBootsItem() {
        super(ArmorMaterials.LEATHER, ArmorItem.Type.BOOTS,
                new Item.Properties().durability(165));
    }
}
