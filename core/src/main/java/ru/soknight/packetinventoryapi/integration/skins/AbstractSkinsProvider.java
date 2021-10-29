package ru.soknight.packetinventoryapi.integration.skins;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public abstract class AbstractSkinsProvider implements SkinsProvider {

    @Override
    public @Nullable HeadTextureProperty getTexture(@NotNull String playerName) {
        WrappedGameProfile gameProfile = getGameProfile(playerName);
        if(gameProfile == null)
            return null;

        Collection<WrappedSignedProperty> texturesProperties = gameProfile.getProperties().get("textures");
        if(texturesProperties == null || texturesProperties.isEmpty())
            return null;

        WrappedSignedProperty texturesProperty = texturesProperties.iterator().next();
        return new HeadTextureProperty(texturesProperty.getValue(), texturesProperty.getSignature());
    }

}
