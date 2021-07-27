package ru.soknight.packetinventoryapi.nms;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.exception.UnsupportedVersionException;
import ru.soknight.packetinventoryapi.menu.item.regular.RegularMenuItem;
import ru.soknight.packetinventoryapi.nms.vanilla.VanillaItem;
import ru.soknight.packetinventoryapi.util.Invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("rawtypes")
public class NMSAssistant {

    public static final String NMS_VERSION;
    public static final String PACKAGE;

    private static Invoker<VanillaItem.Builder> vanillaItemBuilderSupplier;
    private static Invoker<RegularMenuItem.Builder> menuItemBuilderSupplier;

    static {
        NMS_VERSION = getNMSVersion();
        PACKAGE = NMSAssistant.class.getPackage().getName() + ".proxy.v" + NMS_VERSION;
    }

    public static void init(Plugin plugin) throws UnsupportedVersionException {
        plugin.getLogger().info("Detected NMS version: " + NMS_VERSION);

        vanillaItemBuilderSupplier = loadBuilder(VanillaItem.class, VanillaItem.Builder.class);
        menuItemBuilderSupplier = loadBuilder(RegularMenuItem.class, RegularMenuItem.Builder.class);
    }

    private static <T, B> Invoker<B> loadBuilder(Class<T> clazz, Class<B> builderClass) throws UnsupportedVersionException {
        ImplementedAs annotation = clazz.getAnnotation(ImplementedAs.class);
        if(annotation == null)
            throw new IllegalArgumentException("class '" + clazz.getName() + "' must be annotated with @ImplementedAs!");

        String value = annotation.value();
        try {
            Class<?> nmsClass = Class.forName(PACKAGE + "." + value);
            Method method = nmsClass.getMethod("build", ConfigurationSection.class);
            return args -> invokeQuietly(method, args);
        } catch (Throwable ex) {
            throw new UnsupportedVersionException(NMS_VERSION);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T invokeQuietly(Method method, Object... args) {
        try {
            return (T) method.invoke(null, args);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            return null;
        }
    }

    public static String getNMSVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 2);
    }

    public static VanillaItem.Builder<?, ?> newVanillaItem() {
        if(vanillaItemBuilderSupplier == null)
            throw new IllegalStateException("vanilla item creation is unavailable now!");

        return vanillaItemBuilderSupplier.invoke((ConfigurationSection) null);
    }

    public static RegularMenuItem.Builder<?, ?> newMenuItem(ConfigurationSection configuration) {
        if(menuItemBuilderSupplier == null)
            throw new IllegalStateException("menu item creation is unavailable now!");

        return menuItemBuilderSupplier.invoke(configuration);
    }

}
