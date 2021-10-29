package ru.soknight.packetinventoryapi.integration.skins;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CurrentSkinsProvider extends AbstractSkinsProvider {

    private static final CurrentSkinsProvider SINGLETON = new CurrentSkinsProvider();

    public static @NotNull CurrentSkinsProvider getSingleton() {
        return SINGLETON;
    }

    @Override
    public @Nullable WrappedGameProfile getGameProfile(@NotNull String playerName) {
        Player onlinePlayer = Bukkit.getPlayer(playerName);
        return onlinePlayer != null ? WrappedGameProfile.fromPlayer(onlinePlayer) : null;
    }

}
