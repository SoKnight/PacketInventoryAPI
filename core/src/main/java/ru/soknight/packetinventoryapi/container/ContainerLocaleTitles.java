package ru.soknight.packetinventoryapi.container;

import net.md_5.bungee.api.chat.TranslatableComponent;

public class ContainerLocaleTitles {

    public static final TranslatableComponent ANVIL = create("container.repair");
    public static final TranslatableComponent BARREL = create("container.barrel");
    public static final TranslatableComponent BEACON = create("container.beacon");
    public static final TranslatableComponent BLAST_FURNACE = create("container.blast_furnace");
    public static final TranslatableComponent BREWING_STAND = create("container.brewing");
    public static final TranslatableComponent CARTOGRAPHY_TABLE = create("container.cartography_table");
    public static final TranslatableComponent CHEST = create("container.chest");
    public static final TranslatableComponent CHEST_LARGE = create("container.chestDouble");
    public static final TranslatableComponent CRAFTING_TABLE = create("container.crafting");
    public static final TranslatableComponent DISPENSER = create("container.dispenser");
    public static final TranslatableComponent DROPPER = create("container.dropper");
    public static final TranslatableComponent ENCHANTMENT_TABLE = create("container.enchant");
    public static final TranslatableComponent ENDERCHEST = create("container.enderchest");
    public static final TranslatableComponent FURNACE = create("container.furnace");
    public static final TranslatableComponent GRINDSTONE = create("container.grindstone_title");
    public static final TranslatableComponent HOPPER = create("container.hopper");
    public static final TranslatableComponent LECTERN = create("container.lectern");
    public static final TranslatableComponent LOOM = create("container.loom");
    public static final TranslatableComponent SHULKER_BOX = create("container.shulkerBox");
    public static final TranslatableComponent SMITHING_TABLE = create("container.upgrade");
    public static final TranslatableComponent SMOKER = create("container.smoker");
    public static final TranslatableComponent STONECUTTER = create("container.stonecutter");
    public static final TranslatableComponent VILLAGER_NONE = create("entity.minecraft.villager.none");
    public static final TranslatableComponent VILLAGER_ARMORER = create("entity.minecraft.villager.armorer");
    public static final TranslatableComponent VILLAGER_BUTCHER = create("entity.minecraft.villager.butcher");
    public static final TranslatableComponent VILLAGER_CARTOGRAPHER = create("entity.minecraft.villager.cartographer");
    public static final TranslatableComponent VILLAGER_CLERIC = create("entity.minecraft.villager.cleric");
    public static final TranslatableComponent VILLAGER_FARMER = create("entity.minecraft.villager.farmer");
    public static final TranslatableComponent VILLAGER_FISHERMAN = create("entity.minecraft.villager.fisherman");
    public static final TranslatableComponent VILLAGER_FLETCHER = create("entity.minecraft.villager.fletcher");
    public static final TranslatableComponent VILLAGER_LEATHERWORKER = create("entity.minecraft.villager.leatherworker");
    public static final TranslatableComponent VILLAGER_LIBRARIAN = create("entity.minecraft.villager.librarian");
    public static final TranslatableComponent VILLAGER_MASON = create("entity.minecraft.villager.mason");
    public static final TranslatableComponent VILLAGER_NITWIT = create("entity.minecraft.villager.nitwit");
    public static final TranslatableComponent VILLAGER_SHEPHERD = create("entity.minecraft.villager.shepherd");
    public static final TranslatableComponent VILLAGER_TOOLSMITH = create("entity.minecraft.villager.toolsmith");
    public static final TranslatableComponent VILLAGER_WEAPONSMITH = create("entity.minecraft.villager.weaponsmith");
    public static final TranslatableComponent WANDERING_TRADER = create("entity.minecraft.wandering_trader");

    private static TranslatableComponent create(String localeKey) {
        return new TranslatableComponent(localeKey);
    }

}
