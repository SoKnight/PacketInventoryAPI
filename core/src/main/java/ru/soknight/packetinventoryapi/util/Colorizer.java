package ru.soknight.packetinventoryapi.util;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class Colorizer {

    public static final char COLOR_CHAR = '&';

    public static @Nullable String colorize(@Nullable String original) {
        return original != null && !original.isEmpty()
                ? ChatColor.translateAlternateColorCodes(COLOR_CHAR, original)
                : original;
    }

    public static @Nullable List<String> colorize(@Nullable List<String> original) {
        if(original != null && !original.isEmpty())
            original.replaceAll(Colorizer::colorize);
        return original;
    }

    public static @Nullable TextComponent asComponent(@Nullable String string) {
        if(string == null)
            return null;

        if(string.isEmpty())
            return new TextComponent();

        BaseComponent[] asComponentsArray = TextComponent.fromLegacyText(colorize(string));
        return new TextComponent(asComponentsArray);
    }

}
