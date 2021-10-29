package ru.soknight.packetinventoryapi.integration.skins;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.advancedskins.api.AdvancedSkinsApi;
import ru.soknight.advancedskins.api.exception.FeatureUnavailableException;
import ru.soknight.advancedskins.api.profile.PlayerProfile;
import ru.soknight.advancedskins.api.profile.ProfileCache;

import java.util.UUID;

public final class AdvancedSkinsProvider extends AbstractSkinsProvider {

    private static final AdvancedSkinsProvider SINGLETON = new AdvancedSkinsProvider();

    public static @NotNull AdvancedSkinsProvider getSingleton() {
        return SINGLETON;
    }

    @Override
    public @Nullable WrappedGameProfile getGameProfile(@NotNull String playerName) {
        PlayerProfile playerProfile = getPlayerProfile(playerName);
        if(playerProfile == null)
            return null;

        ProfileCache profileCache = playerProfile.getProfileCache();
        if(profileCache == null || !profileCache.isCached())
            return null;

        UUID uuid = playerProfile.getPlayerUUID().orElse(UUID.randomUUID());

        String value = profileCache.getBase64Value();
        String signature = profileCache.getSignature();
        WrappedSignedProperty texturesProperty = new WrappedSignedProperty("textures", value, signature);

        WrappedGameProfile gameProfile = new WrappedGameProfile(uuid, playerName);
        gameProfile.getProperties().put("textures", texturesProperty);
        return gameProfile;
    }

    @Override
    public @Nullable HeadTextureProperty getTexture(@NotNull String playerName) {
        PlayerProfile playerProfile = getPlayerProfile(playerName);
        if(playerProfile == null)
            return null;

        ProfileCache profileCache = playerProfile.getProfileCache();
        if(profileCache == null || !profileCache.isCached())
            return null;

        String value = profileCache.getBase64Value();
        String signature = profileCache.getSignature();
        return new HeadTextureProperty(value, signature);
    }

    private @Nullable PlayerProfile getPlayerProfile(@NotNull String playerName) {
        try {
            return AdvancedSkinsApi.get().getProfile(playerName).orElse(null);
        } catch (FeatureUnavailableException ignored) {
            return null;
        }
    }

}
