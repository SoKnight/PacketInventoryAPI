package ru.soknight.packetinventoryapi.container.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum VillagerLevel {

    NOVICE(1, "merchant.level.novice"),
    APPRENTICE(2, "merchant.level.apprentice"),
    JOURNEYMAN(3, "merchant.level.journeyman"),
    EXPERT(4, "merchant.level.expert"),
    MASTER(5, "merchant.level.master");

    private final int id;
    private final String translationKey;

}
