package ru.soknight.packetinventoryapi.menu.extended;

import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.annotation.window.CloseListener;
import ru.soknight.packetinventoryapi.container.data.holder.DataHolder;
import ru.soknight.packetinventoryapi.container.type.AnvilContainer;
import ru.soknight.packetinventoryapi.event.window.WindowCloseEvent;
import ru.soknight.packetinventoryapi.menu.input.validate.InputValidatable;
import ru.soknight.packetinventoryapi.menu.input.validate.InputValidatorBus;
import ru.soknight.packetinventoryapi.menu.input.validate.ValidationResult;
import ru.soknight.packetinventoryapi.menu.type.AbstractAnvilMenu;

@Getter
public abstract class AnvilInputMenu extends AbstractAnvilMenu implements InputValidatable<AnvilContainer, AnvilContainer.AnvilUpdateRequest> {

    protected static final String INPUT_DATA_KEY = "input-data";

    protected final InputValidatorBus<AnvilContainer, AnvilContainer.AnvilUpdateRequest> validatorBus;

    public AnvilInputMenu(String name, Plugin providingPlugin) {
        super(name, providingPlugin);
        this.validatorBus = InputValidatorBus.create();
    }

    public AnvilInputMenu(String name, Plugin providingPlugin, String title) {
        super(name, providingPlugin, title);
        this.validatorBus = InputValidatorBus.create();
    }

    public AnvilInputMenu(String name, Plugin providingPlugin, BaseComponent title) {
        super(name, providingPlugin, title);
        this.validatorBus = InputValidatorBus.create();
    }

    public AnvilInputMenu(AnvilContainer container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
        this.validatorBus = InputValidatorBus.create();
    }

    @Override
    protected void onRename(Player actor, AnvilContainer container, String customName) {
        synchronized (this) {
            if(customName.equals(getInputData(actor)))
                return;

            setInputData(actor, customName);

            ValidationResult<AnvilContainer, AnvilContainer.AnvilUpdateRequest> result = validatorBus.validate(actor, container, customName);
            onInputValidation(result);

            if(result.hasValidatedSuccessfully())
                onInputValidationSucceed(result);
            else
                onInputValidationFailed(result);
        }
    }

    @CloseListener
    public void resetInputDataOnClose(WindowCloseEvent<AnvilContainer, AnvilContainer.AnvilUpdateRequest> event) {
        Player actor = event.getActor();
        setInputData(actor, (String) null);
    }

    protected @Nullable String getInputData(@NotNull Player player) {
        synchronized (this) {
            return getDataHolder(player).get(INPUT_DATA_KEY);
        }
    }

    protected void setInputData(@NotNull Player player, @Nullable String inputData) {
        synchronized (this) {
            DataHolder dataHolder = getDataHolder(player);
            if(dataHolder != null)
                dataHolder.set(INPUT_DATA_KEY, inputData);
        }
    }

    protected void setInputData(@NotNull Player player, @Nullable Number inputData) {
        setInputData(player, inputData != null ? inputData.toString() : null);
    }

}
