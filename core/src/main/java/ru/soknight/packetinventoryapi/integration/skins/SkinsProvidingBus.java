package ru.soknight.packetinventoryapi.integration.skins;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.*;

public final class SkinsProvidingBus {

    private final Plugin plugin;
    private final Set<SkinsProvider> skinsProviders;

    public SkinsProvidingBus(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.skinsProviders = new LinkedHashSet<>();
        load();
    }

    private void load() {
        registerSkinsProvider(new CurrentSkinsProvider());

        if(plugin.getServer().getPluginManager().isPluginEnabled("AdvancedSkins"))
            registerSkinsProvider(new AdvancedSkinsProvider());
    }

    public @NotNull Optional<WrappedGameProfile> findPlayerSkin(@NotNull String playerName) {
        if(skinsProviders.isEmpty())
            return Optional.empty();

        for(SkinsProvider skinsProvider : skinsProviders) {
            WrappedGameProfile gameProfile = skinsProvider.getGameProfile(playerName);
            if(gameProfile != null)
                return Optional.of(gameProfile);
        }

        return Optional.empty();
    }

    public @NotNull Optional<HeadTextureProperty> findHeadTexture(@NotNull String playerName) {
        if(skinsProviders.isEmpty())
            return Optional.empty();

        for(SkinsProvider skinsProvider : skinsProviders) {
            HeadTextureProperty textureProperty = skinsProvider.getTexture(playerName);
            if(textureProperty != null)
                return Optional.of(textureProperty);
        }

        return Optional.empty();
    }

    public @NotNull @UnmodifiableView Set<SkinsProvider> getSkinsProviders() {
        return Collections.unmodifiableSet(skinsProviders);
    }

    public boolean registerSkinsProvider(@NotNull SkinsProvider skinsProvider) {
        return registerSkinsProvider(skinsProvider, false);
    }

    public boolean registerSkinsProvider(@NotNull SkinsProvider skinsProvider, boolean insertFirst) {
        Validate.notNull(skinsProvider, "skinsProvider");
        synchronized (skinsProviders) {
            if(insertFirst) {
                if(skinsProviders.contains(skinsProvider))
                    return false;

                List<SkinsProvider> copy = new ArrayList<>(skinsProviders);
                copy.add(0, skinsProvider);
                skinsProviders.clear();
                skinsProviders.addAll(copy);
                return true;
            } else {
                return skinsProviders.add(skinsProvider);
            }
        }
    }

    public boolean unregisterSkinsProvider(@NotNull SkinsProvider skinsProvider) {
        Validate.notNull(skinsProvider, "skinsProvider");
        synchronized (skinsProviders) {
            return skinsProviders.remove(skinsProvider);
        }
    }

}
