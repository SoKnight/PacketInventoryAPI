package ru.soknight.packetinventoryapi;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.animation.Animation;
import ru.soknight.packetinventoryapi.api.PacketInventoryAPI;
import ru.soknight.packetinventoryapi.exception.UnsupportedVersionException;
import ru.soknight.packetinventoryapi.exception.packet.NoConstructorFoundException;
import ru.soknight.packetinventoryapi.integration.Integrations;
import ru.soknight.packetinventoryapi.integration.itemsadder.ItemsAdderListener;
import ru.soknight.packetinventoryapi.integration.skins.SkinsProvidingBus;
import ru.soknight.packetinventoryapi.listener.PacketsListener;
import ru.soknight.packetinventoryapi.menu.registry.MenuRegistry;
import ru.soknight.packetinventoryapi.menu.registry.SimpleMenuRegistry;
import ru.soknight.packetinventoryapi.nms.NMSAssistant;
import ru.soknight.packetinventoryapi.packet.PacketAssistant;
import ru.soknight.packetinventoryapi.placeholder.PlaceholderReplacer;
import ru.soknight.packetinventoryapi.placeholder.PlaceholderReplacerPAPI;
import ru.soknight.packetinventoryapi.plugin.command.CommandDebug;
import ru.soknight.packetinventoryapi.storage.ContainerStorage;
import ru.soknight.packetinventoryapi.storage.SimpleContainerStorage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class PacketInventoryAPIPlugin extends JavaPlugin {

    private static PacketInventoryAPIPlugin INSTANCE;
    private static PacketInventoryAPI API_INSTANCE;
    
    private SimpleContainerStorage containerStorage;
    private SimpleMenuRegistry menuRegistry;
    private PacketsListener packetsListener;

    private SkinsProvidingBus skinsProvidingBus;

    private PlaceholderReplacer papiPlaceholderReplacer;
    private ExecutorService asyncSingleExecutorService;
    private ExecutorService asyncMultiExecutorService;
    
    @Override
    public void onEnable() {
        INSTANCE = this;

        try {
            NMSAssistant.init(this);
            PacketAssistant.init(this);
        } catch (UnsupportedVersionException ex) {
            getLogger().severe("Couldn't find a NMS implementation for your server version!");
            getLogger().severe("Current supported versions is all from 1.13 to 1.17.1.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        } catch (NoConstructorFoundException ex) {
            getLogger().severe(ex.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.containerStorage = new SimpleContainerStorage();
        this.menuRegistry = new SimpleMenuRegistry();
        this.packetsListener = new PacketsListener(this, containerStorage);

        this.skinsProvidingBus = new SkinsProvidingBus(this);

        resolvePlaceholderReplacer();
        this.asyncSingleExecutorService = Executors.newSingleThreadExecutor();
        this.asyncMultiExecutorService = Executors.newCachedThreadPool();

        API_INSTANCE = new SimplePacketInventoryAPI(containerStorage, menuRegistry, skinsProvidingBus);
        
        new CommandDebug(this);

        if(Integrations.availableItemsAdder())
            new ItemsAdderListener(this, menuRegistry);
        
        getLogger().info("Ready to provide packet inventories management!");
    }
    
    @Override
    public void onDisable() {
        // finishing all container animations
        if(!Animation.PLAYING_ANIMATIONS.isEmpty()) {
            getLogger().info("Finishing " + Animation.PLAYING_ANIMATIONS.size() + " animation(s)...");
            Animation.finishAllSync();
        }

        if(containerStorage != null)
            containerStorage.closeAll();
        if(menuRegistry != null)
            menuRegistry.unregisterAll();
        if(packetsListener != null)
            packetsListener.unregister();
    }

    private void resolvePlaceholderReplacer() {
        // PlaceholderAPI placeholder replacer
        if(getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.papiPlaceholderReplacer = new PlaceholderReplacerPAPI();
        } else {
            this.papiPlaceholderReplacer = PlaceholderReplacer.DEFAULT;
        }
    }
    
    public static PacketInventoryAPI getApiInstance() {
        return API_INSTANCE;
    }

    public static PlaceholderReplacer getPlaceholderReplacerPAPI() {
        return INSTANCE.papiPlaceholderReplacer;
    }

    public static ExecutorService getExecutorService(boolean sequential) {
        return sequential ? getSingleExecutorService() : getMultiExecutorService();
    }

    public static ExecutorService getSingleExecutorService() {
        return INSTANCE.asyncSingleExecutorService;
    }

    public static ExecutorService getMultiExecutorService() {
        return INSTANCE.asyncMultiExecutorService;
    }

    public static void info(String format, Object... args) {
        INSTANCE.getLogger().info(String.format(format, args));
    }

    public static void warning(String format, Object... args) {
        INSTANCE.getLogger().warning(String.format(format, args));
    }

    public static void error(String format, Object... args) {
        INSTANCE.getLogger().severe(String.format(format, args));
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class SimplePacketInventoryAPI implements PacketInventoryAPI {
        private final ContainerStorage containerStorage;
        private final MenuRegistry menuRegistry;
        private final SkinsProvidingBus skinsProvidingBus;

        @Override
        public @NotNull ContainerStorage containerStorage() {
            return containerStorage;
        }

        @Override
        public @NotNull MenuRegistry menuRegistry() {
            return menuRegistry;
        }

        @Override
        public @NotNull SkinsProvidingBus skinsProvidingBus() {
            return skinsProvidingBus;
        }
    }
    
}
