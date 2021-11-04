package ru.soknight.packetinventoryapi.container;

import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

public interface ContainerType {

    int[] FURNACES_IDS = new int[] { 9, 13, 21 };

    int getTypeId();

    @NotNull BaseComponent getDefaultTitle();

    default boolean isFurnace() {
        int thisId = getTypeId();

        for(int furnaceId : FURNACES_IDS)
            if(furnaceId == thisId)
                return true;

        return false;
    }

}
