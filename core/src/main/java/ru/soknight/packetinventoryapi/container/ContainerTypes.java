package ru.soknight.packetinventoryapi.container;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.chat.TranslatableComponent;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ContainerTypes implements ContainerType {

    /**
     * A 1-row inventory, not used by the notchian server.
     */
    GENERIC_9X1(0, ContainerLocaleTitles.CHEST),
    /**
     * A 2-row inventory, not used by the notchian server.
     */
    GENERIC_9X2(1, ContainerLocaleTitles.CHEST),
    /**
     * General-purpose 3-row inventory.
     * Used by <i>Chest</i>, <i>Minecart with Chest</i>, <i>Ender-chest</i>, and <i>Barrel</i>.
     */
    GENERIC_9X3(2, ContainerLocaleTitles.CHEST),
    /**
     * A 4-row inventory, not used by the notchian server.
     */
    GENERIC_9X4(3, ContainerLocaleTitles.CHEST_LARGE),
    /**
     * A 5-row inventory, not used by the notchian server.
     */
    GENERIC_9X5(4, ContainerLocaleTitles.CHEST_LARGE),
    /**
     * General-purpose 6-row inventory.
     * Used by <i>Large chest</i>.
     */
    GENERIC_9X6(5, ContainerLocaleTitles.CHEST_LARGE),
    /**
     * General-purpose 3-by-3 square inventory.
     * Used by <i>Dispenser</i> and <i>Dropper</i>.
     */
    GENERIC_3X3(6, ContainerLocaleTitles.DROPPER),
    /**
     * Used by <i>Anvil</i>.
     */
    ANVIL(7, ContainerLocaleTitles.ANVIL),
    /**
     * Used by <i>Beacon</i>.
     */
    BEACON(8, ContainerLocaleTitles.BEACON),
    /**
     * Used by <i>Blast furnace</i>.
     */
    BLAST_FURNACE(9, ContainerLocaleTitles.BLAST_FURNACE),
    /**
     * Used by <i>Brewing stand</i>.
     */
    BREWING_STAND(10, ContainerLocaleTitles.BREWING_STAND),
    /**
     * Used by <i>Crafting table</i>.
     */
    CRAFTING_TABLE(11, ContainerLocaleTitles.CRAFTING_TABLE),
    /**
     * Used by <i>Enchantment table</i>.
     */
    ENCHANTMENT_TABLE(12, ContainerLocaleTitles.ENCHANTMENT_TABLE),
    /**
     * Used by <i>Furnace</i>.
     */
    FURNACE(13, ContainerLocaleTitles.FURNACE),
    /**
     * Used by <i>Grindstone</i>.
     */
    GRINDSTONE(14, ContainerLocaleTitles.GRINDSTONE),
    /**
     * Used by <i>Hopper</i> and also by <i>Minecraft with Hopper</i>.
     */
    HOPPER(15, ContainerLocaleTitles.HOPPER),
    /**
     * Used by <i>Lectern</i>.
     */
    LECTERN(16, ContainerLocaleTitles.LECTERN),
    /**
     * Used by <i>Loom</i>.
     */
    LOOM(17, ContainerLocaleTitles.LOOM),
    /**
     * Used by <i>Villager</i> and <i>Wandering Trader</i>.
     */
    MERCHANT(18, ContainerLocaleTitles.VILLAGER_NONE),
    /**
     * Used by <i>Shulker box</i>.
     */
    SHULKER_BOX(19, ContainerLocaleTitles.SHULKER_BOX),
    /**
     * Used by <i>Smithing table</i>.
     */
    SMITHING_TABLE(20, ContainerLocaleTitles.SMITHING_TABLE),
    /**
     * Used by <i>Smoker</i>.
     */
    SMOKER(21, ContainerLocaleTitles.SMOKER),
    /**
     * Used by <i>Cartography table</i>.
     */
    CARTOGRAPHY_TABLE(22, ContainerLocaleTitles.CARTOGRAPHY_TABLE),
    /**
     * Used by <i>Stonecutter</i>.
     */
    STONECUTTER(23, ContainerLocaleTitles.STONECUTTER);
    
    private final int typeId;
    private final TranslatableComponent defaultTitle;
    
    public boolean isFurnace() {
        switch (this) {
        case FURNACE:
        case BLAST_FURNACE:
        case SMOKER:
            return true;
        default:
            return false;
        }
    }
    
}
