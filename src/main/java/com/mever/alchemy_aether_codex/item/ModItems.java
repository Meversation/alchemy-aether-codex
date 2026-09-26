package com.mever.alchemy_aether_codex.item;

import com.mever.alchemy_aether_codex.AlchemyAetherCodex;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;


public class ModItems {
    public static final Item MERCURY_DROP = register (
        "mercury_drop",
        new Item(new Item.Settings())
    );

    public static final Item SULFUR_DUST = register (
        "sulfur_dust",
        new Item(new Item.Settings())
    );

    public static final Item SALT_CRYSTAL = register (
        "salt_crystal",
        new Item(new Item.Settings())
    );

    public static final Item RUNE_STAFF = register (
        "rune_staff",
        new Item(new Item.Settings().maxCount(1))
    );

    public static final Item AETHER_CODEX = register (
        "aether_codex",
        new Item(new Item.Settings().maxCount(1))
    );

    private static Item register(String name, Item item) {
        return Registry.register(
            Registries.ITEM,
            AlchemyAetherCodex.id(name),
            item
        );
    }
    
    public static void register(){
        
    }
}
