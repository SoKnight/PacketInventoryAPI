package ru.soknight.packetinventoryapi.configuration;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.configuration.parse.ParsedDataBundle;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.type.GenericContainer;
import ru.soknight.packetinventoryapi.exception.configuration.*;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.menu.Menu;
import ru.soknight.packetinventoryapi.menu.container.PublicWrapper;
import ru.soknight.packetinventoryapi.util.Validate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class MenuLoader {

    public static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>, M extends Menu<C, R>> M load(
            M instance,
            boolean resetContent
    ) throws
            ResourceCopyException,
            ResourceNotFoundException,
            NoSlotsToDisplayException,
            InvalidRowsAmountException,
            UnknownFillingPatternException,
            UnknownMaterialException,
            NoMaterialProvidedException,
            NoContentProvidedException,
            NoMenuIOException
    {
        Validate.notNull(instance, "instance");

        MenuIO menuIO = instance.getClass().getAnnotation(MenuIO.class);
        if(menuIO == null)
            throw new NoMenuIOException(instance.getClass().getSimpleName());

        String resourcePath = menuIO.resourcePath();
        Validate.notEmpty(resourcePath, "resourcePath");

        String outputFile = menuIO.outputFile();
        Validate.notEmpty(outputFile, "outputFile");

        resourcePath = resourcePath.replace("//", File.separator);
        outputFile = outputFile.replace("//", File.separator);

        return load(instance, resourcePath, outputFile, resetContent);
    }

    public static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>, M extends Menu<C, R>> M load(
            M instance,
            String resourcePath,
            String outputFile,
            boolean resetContent
    ) throws
            ResourceCopyException,
            ResourceNotFoundException,
            NoSlotsToDisplayException,
            InvalidRowsAmountException,
            UnknownFillingPatternException,
            UnknownMaterialException,
            NoMaterialProvidedException,
            NoContentProvidedException
    {
        Validate.notNull(instance, "instance");
        Validate.notEmpty(resourcePath, "resourcePath");
        Validate.notEmpty(outputFile, "outputFile");

        Plugin plugin = instance.getProvidingPlugin();
        if(plugin == null)
            throw new IllegalArgumentException("a menu must be provided by anyone plugin!");

        Configuration configuration = loadConfiguration(plugin, resourcePath, outputFile);
        ParsedDataBundle parsedDataBundle = MenuParser.parse(plugin, outputFile, configuration, instance.configurationStructure());
        instance.updateParsedData(parsedDataBundle);

        PublicWrapper<C, R> wrapper = instance.getContainer();
        C original = wrapper.getOriginal();

        // --- update rows amount
        parsedDataBundle.getRowsAmount().ifPresent(rowsAmount -> {
            if(original instanceof GenericContainer)
                ((GenericContainer) original).setRowsAmount(rowsAmount);
        });

        // --- reset existing container content
        R updateContent = original.updateContent();

        if(resetContent)
            updateContent.resetItemMatrix();

        wrapper.setTitle(parsedDataBundle.getTitle());
        wrapper.setFiller(parsedDataBundle.getFiller());
        updateContent.fromParsedData(parsedDataBundle, true).pushSync();

        return instance;
    }

    public static @NotNull Configuration loadConfiguration(Plugin plugin, String resourceName, String outputFile)
            throws ResourceNotFoundException, ResourceCopyException
    {
        Validate.notNull(plugin, "plugin");
        Validate.notEmpty(resourceName, "resourceName");
        Validate.notEmpty(outputFile, "outputFile");

        Path dataFolder = plugin.getDataFolder().toPath();
        Path filePath = dataFolder.resolve(outputFile);

        // --- save plugin resource to file
        if(!Files.isRegularFile(filePath)) {
            InputStream resource = plugin.getClass().getResourceAsStream(resourceName);
            if(resource == null)
                throw new ResourceNotFoundException("resource '%s' isn't provided by plugin %s!", resourceName, plugin.getName());

            try {
                Files.createDirectories(filePath);
                Files.copy(resource, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                throw new ResourceCopyException(ex);
            }
        }

        return YamlConfiguration.loadConfiguration(filePath.toFile());
    }

}
