package ru.soknight.packetinventoryapi.event.type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.soknight.packetinventoryapi.container.Container;

import java.util.Objects;

/**
 * The container property types registry.
 * <br>
 * If your wanted property is not exist, you can use {@link Container#updatePropertyRaw(int, int)}.
 * <br>
 * See all known window properties <a href="https://wiki.vg/Protocol#Window_Property">here</a>.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WindowClickType {

    private final int modeId;
    private final int buttonId;
    private final boolean outsideInventory;

    public WindowClickType(int modeId, int buttonId) {
        this(modeId, buttonId, false);
    }
    
    public WindowClickType(int modeId, int buttonId, int clickedSlot) {
        this(modeId, buttonId, (modeId == 0 || modeId == 4) && clickedSlot == -999);
    }
    
    public static class Mouse {
        private static final int MODE = 0;
        
        /** Single left mouse click. */
        public static final WindowClickType LEFT_CLICK = new WindowClickType(MODE, 0);
        /** Single right mouse click. */
        public static final WindowClickType RIGHT_CLICK = new WindowClickType(MODE, 1);
    }
    
    public static class MouseShift {
        private static final int MODE = 1;
        
        /** Shift + left mouse click. */
        public static final WindowClickType LEFT_CLICK = new WindowClickType(MODE, 0);
        /** Shift + right mouse click (identical behavior). */
        public static final WindowClickType RIGHT_CLICK = new WindowClickType(MODE, 1);
    }

    public static class HotbarMove {
        private static final int MODE = 2;

        public static final WindowClickType HOTBAR_SLOT_1 = new WindowClickType(MODE, 0);
        public static final WindowClickType HOTBAR_SLOT_2 = new WindowClickType(MODE, 1);
        public static final WindowClickType HOTBAR_SLOT_3 = new WindowClickType(MODE, 2);
        public static final WindowClickType HOTBAR_SLOT_4 = new WindowClickType(MODE, 3);
        public static final WindowClickType HOTBAR_SLOT_5 = new WindowClickType(MODE, 4);
        public static final WindowClickType HOTBAR_SLOT_6 = new WindowClickType(MODE, 5);
        public static final WindowClickType HOTBAR_SLOT_7 = new WindowClickType(MODE, 6);
        public static final WindowClickType HOTBAR_SLOT_8 = new WindowClickType(MODE, 7);
        public static final WindowClickType HOTBAR_SLOT_9 = new WindowClickType(MODE, 8);
    }
    
    public static class DropItem {
        private static final int MODE = 4;
        
        /** Drop key (Q). */
        public static final WindowClickType DROP = new WindowClickType(MODE, 0);
        /** Ctrl + Drop key (Ctrl-Q). */
        public static final WindowClickType DROP_STACK = new WindowClickType(MODE, 1);
    }
    
    public static class Outside {
        private static final int MODE = 4;
        
        /** Left click outside inventory holding nothing (no-op). */
        public static final WindowClickType DROP = new WindowClickType(MODE, 0, true);
        /** Right click outside inventory holding nothing (no-op). */
        public static final WindowClickType DROP_STACK = new WindowClickType(MODE, 1, true);
    }
    
    public static class Drag {
        private static final int MODE = 5;
        
        public static class Starting {
            /** Starting left mouse drag. */
            public static final WindowClickType LEFT_MOUSE_BUTTON = new WindowClickType(MODE, 0, true);
            /** Starting right mouse drag. */
            public static final WindowClickType RIGHT_MOUSE_BUTTON = new WindowClickType(MODE, 4, true);
            /**
             * Starting middle mouse drag, only defined for creative players in non-player inventories.
             * <br>
             * (Note: the vanilla client will still incorrectly send this for non-creative players -
             * see <a href="https://bugs.mojang.com/browse/MC-46584">MC-46584<a>)
             */
            public static final WindowClickType MIDDLE_MOUSE_BUTTON = new WindowClickType(MODE, 8, true);
        }
        
        public static class AddSlot {
            /** Add slot for left-mouse drag. */
            public static final WindowClickType LEFT_MOUSE_BUTTON = new WindowClickType(MODE, 1);
            /** Add slot for right-mouse drag. */
            public static final WindowClickType RIGHT_MOUSE_BUTTON = new WindowClickType(MODE, 5);
            /**
             * Add slot for middle-mouse drag, only defined for creative players in non-player inventories.
             * <br>
             * (Note: the vanilla client will still incorrectly send this for non-creative players -
             * see <a href="https://bugs.mojang.com/browse/MC-46584">MC-46584<a>)
             */
            public static final WindowClickType MIDDLE_MOUSE_BUTTON = new WindowClickType(MODE, 9);
        }
        
        public static class Ending {
            /** Ending left mouse drag. */
            public static final WindowClickType LEFT_MOUSE_BUTTON = new WindowClickType(MODE, 2, true);
            /** Ending right mouse drag. */
            public static final WindowClickType RIGHT_MOUSE_BUTTON = new WindowClickType(MODE, 6, true);
            /**
             * Ending middle mouse drag, only defined for creative players in non-player inventories.
             * <br>
             * (Note: the vanilla client will still incorrectly send this for non-creative players -
             * see <a href="https://bugs.mojang.com/browse/MC-46584">MC-46584<a>)
             */
            public static final WindowClickType MIDDLE_MOUSE_BUTTON = new WindowClickType(MODE, 10, true);
        }
    }
    
    public static final WindowClickType MIDDLE_CLICK = new WindowClickType(3, 2);
    public static final WindowClickType DOUBLE_CLICK = new WindowClickType(6, 0);
    
    /** Returns click type in hotbar slot (from 0 to 8). */
    public static WindowClickType getBySlot(int slot) {
        return new WindowClickType(2, slot);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        WindowClickType that = (WindowClickType) o;
        return modeId == that.modeId &&
                buttonId == that.buttonId &&
                outsideInventory == that.outsideInventory;
    }

    @Override
    public int hashCode() {
        return Objects.hash(modeId, buttonId, outsideInventory);
    }

    @Override
    public String toString() {
        return "WindowClickType{" +
                "mode=" + modeId +
                ", button=" + buttonId +
                ", outsideInventory=" + outsideInventory +
                '}';
    }

}
