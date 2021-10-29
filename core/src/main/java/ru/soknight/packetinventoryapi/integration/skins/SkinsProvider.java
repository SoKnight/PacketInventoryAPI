package ru.soknight.packetinventoryapi.integration.skins;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SkinsProvider {

    @Nullable WrappedGameProfile getGameProfile(@NotNull String playerName);

    @Nullable HeadTextureProperty getTexture(@NotNull String playerName);

}
