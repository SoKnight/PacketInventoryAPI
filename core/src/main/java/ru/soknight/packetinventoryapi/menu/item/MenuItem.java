package ru.soknight.packetinventoryapi.menu.item;

import ru.soknight.packetinventoryapi.configuration.parse.FillPatternType;
import ru.soknight.packetinventoryapi.nms.ImplementedAs;
import ru.soknight.packetinventoryapi.nms.vanilla.VanillaItem;

@ImplementedAs("SimpleMenuItem")
public interface MenuItem<I extends MenuItem<I, B>, B extends MenuItem.Builder<I, B>> extends VanillaItem<I, B> {

    int[] getSlots();

    FillPatternType getFillPattern();

    interface Builder<I extends MenuItem<I, B>, B extends Builder<I, B>> extends VanillaItem.Builder<I, B> {
        B slots(int... value);

        B fillPattern(FillPatternType value);
    }

}
