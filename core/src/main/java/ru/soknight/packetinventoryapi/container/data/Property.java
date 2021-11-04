package ru.soknight.packetinventoryapi.container.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.potion.PotionEffectType;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.ContainerType;
import ru.soknight.packetinventoryapi.container.ContainerTypes;

import java.util.Objects;

/**
 * The container property types registry.
 * <br>
 * If your wanted property is not exist, you can use {@link Container#updatePropertyRaw(int, int)}.
 * See the known window properties <a href="https://wiki.vg/Protocol#Window_Property">here</a>.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Property {

    private final ContainerType containerType;
    private final int propertyId;
    
    public static class Furnace {
        private static final ContainerType TYPE = ContainerTypes.FURNACE;
        
        /** Counting from fuel burn time down to 0 (in-game ticks). */
        public static final Property FUEL_BURN_TIME_LEFT = new Property(TYPE, 0);
        /** Fuel burn time or 0 (in-game ticks). */
        public static final Property MAX_FUEL_BURN_TIME = new Property(TYPE, 1);
        /** Counting from 0 to maximum progress (in-game ticks). */
        public static final Property CURRENT_PROGRESS = new Property(TYPE, 2);
        /** Always 200 on the notchian server. */
        public static final Property MAX_PROGRESS = new Property(TYPE, 3);
    }
    
    public static class EnchantmentTable {
        private static final ContainerType TYPE = ContainerTypes.ENCHANTMENT_TABLE;
        
        /** The enchantment's xp level requirement. */
        public static final Property LEVEL_REQUIREMENT_TOP = new Property(TYPE, 0);
        /** The enchantment's xp level requirement. */
        public static final Property LEVEL_REQUIREMENT_MIDDLE = new Property(TYPE, 1);
        /** The enchantment's xp level requirement. */
        public static final Property LEVEL_REQUIREMENT_BOTTOM = new Property(TYPE, 2);
        /**
         * Used for drawing the enchantment names (in <b>SGA</b>) clientside.
         * The same seed is used to calculate enchantments, but some of the data
         * isn't sent to the client to prevent easily guessing the entire list
         * (the seed value here is the regular seed bitwise and <b>0xFFFFFFF0</b>).
         */
        public static final Property ENCHANTMENT_SEED = new Property(TYPE, 3);
        /** The enchantment id (set to -1 to hide it), see below for values. */
        public static final Property HOVER_ENCHANTMENT_ID_TOP = new Property(TYPE, 4);
        /** The enchantment id (set to -1 to hide it), see below for values. */
        public static final Property HOVER_ENCHANTMENT_ID_MIDDLE = new Property(TYPE, 5);
        /** The enchantment id (set to -1 to hide it), see below for values. */
        public static final Property HOVER_ENCHANTMENT_ID_BOTTOM = new Property(TYPE, 6);
        /** The enchantment level (1 = I, 2 = II, 6 = VI, etc.), or -1 if no enchant. */
        public static final Property HOVER_ENCHANTMENT_LEVEL_TOP = new Property(TYPE, 7);
        /** The enchantment level (1 = I, 2 = II, 6 = VI, etc.), or -1 if no enchant. */
        public static final Property HOVER_ENCHANTMENT_LEVEL_MIDDLE = new Property(TYPE, 8);
        /** The enchantment level (1 = I, 2 = II, 6 = VI, etc.), or -1 if no enchant. */
        public static final Property HOVER_ENCHANTMENT_LEVEL_BOTTOM = new Property(TYPE, 9);
        
        /** The enchantment's xp level requirement. */
        public static Property levelRequirement(EnchantmentPosition position) {
            switch (position) {
            case TOP:
                return LEVEL_REQUIREMENT_TOP;
            case MIDDLE:
                return LEVEL_REQUIREMENT_MIDDLE;
            case BOTTOM:
                return LEVEL_REQUIREMENT_BOTTOM;
            default:
                throw new IllegalArgumentException(position + " isn't allowed enchantment position");
            }
        }
        
        /** The enchantment id (set to -1 to hide it), see below for values. */
        public static Property hoverEnchantmentId(EnchantmentPosition position) {
            switch (position) {
            case TOP:
                return HOVER_ENCHANTMENT_ID_TOP;
            case MIDDLE:
                return HOVER_ENCHANTMENT_ID_MIDDLE;
            case BOTTOM:
                return HOVER_ENCHANTMENT_ID_BOTTOM;
            default:
                throw new IllegalArgumentException(position + " isn't allowed enchantment position");
            }
        }
        
        /** The enchantment level (1 = I, 2 = II, 6 = VI, etc.), or -1 if no enchant. */
        public static Property hoverEnchantmentLevel(EnchantmentPosition position) {
            switch (position) {
            case TOP:
                return HOVER_ENCHANTMENT_LEVEL_TOP;
            case MIDDLE:
                return HOVER_ENCHANTMENT_LEVEL_MIDDLE;
            case BOTTOM:
                return HOVER_ENCHANTMENT_LEVEL_BOTTOM;
            default:
                throw new IllegalArgumentException(position + " isn't allowed enchantment position");
            }
        }
    }
    
    public static class Beacon {
        private static final ContainerType TYPE = ContainerTypes.BEACON;
        
        /** 0-4, controls what effect buttons are enabled. */
        public static final Property POWER_LEVEL = new Property(TYPE, 0);
        /** {@link PotionEffectType} ID for the first effect, or -1 if no effect. */
        public static final Property FIRST_POTION_EFFECT = new Property(TYPE, 1);
        /** {@link PotionEffectType} ID for the second effect, or -1 if no effect. */
        public static final Property SECOND_POTION_EFFECT = new Property(TYPE, 2);
    }
    
    public static class Anvil {
        private static final ContainerType TYPE = ContainerTypes.ANVIL;
        
        /** The repair's cost in xp levels. */
        public static final Property REPAIR_COST = new Property(TYPE, 0);
    }
    
    public static class BrewingStand {
        private static final ContainerType TYPE = ContainerTypes.BREWING_STAND;
        
        /** 0 – 400, with 400 making the arrow empty, and 0 making the arrow full. */
        public static final Property BREW_TIME = new Property(TYPE, 0);
        /** 0 - 20, with 0 making the arrow empty, and 20 making the arrow full. */
        public static final Property FUEL_TIME = new Property(TYPE, 1);
    }
    
    public static class Stonecutter {
        private static final ContainerType TYPE = ContainerTypes.STONECUTTER;
        
        /** The index of the selected recipe. -1 means none is selected. */
        public static final Property SELECTED_RECIPE = new Property(TYPE, 0);
    }
    
    public static class Loom {
        private static final ContainerType TYPE = ContainerTypes.LOOM;
        
        /**
         * The index of the selected pattern. 0 means none is selected.
         * <br>
         * 0 is also the internal id of the <i>base</i> pattern.
         */
        public static final Property SELECTED_PATTERN = new Property(TYPE, 0);
    }

    public static class Lectern {
        private static final ContainerType TYPE = ContainerTypes.LECTERN;

        /**
         * The current page number, starting from 0.
         */
        public static final Property PAGE_NUMBER = new Property(TYPE, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Property property = (Property) o;
        return propertyId == property.propertyId &&
                Objects.equals(containerType, property.containerType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(containerType, propertyId);
    }

    @Override
    public String toString() {
        return "PropertyType{"
                + "containerType=" + containerType
                + ", propertyId=" + propertyId
                + "}";
    }
    
}
