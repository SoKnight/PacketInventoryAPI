package ru.soknight.packetinventoryapi.container.type;

import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.ContainerLocaleTitles;
import ru.soknight.packetinventoryapi.container.ContainerTypes;
import ru.soknight.packetinventoryapi.container.data.LecternButtonType;
import ru.soknight.packetinventoryapi.container.data.Property;
import ru.soknight.packetinventoryapi.event.container.LecternButtonClickEvent;
import ru.soknight.packetinventoryapi.event.container.LecternPageOpenEvent;
import ru.soknight.packetinventoryapi.item.update.content.BaseContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.AnyEventListener;
import ru.soknight.packetinventoryapi.listener.event.EventListener;
import ru.soknight.packetinventoryapi.listener.event.container.LecternButtonClickListener;
import ru.soknight.packetinventoryapi.listener.event.container.LecternPageOpenListener;
import ru.soknight.packetinventoryapi.util.IntRange;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.Map;

@Getter
public class LecternContainer extends Container<LecternContainer, LecternContainer.LecternUpdateRequest> {

    public static final int BOOK_SLOT = 0;

    private @Nullable EventListener<LecternButtonClickEvent> buttonClickListener;
    private @Nullable EventListener<LecternPageOpenEvent> pageOpenListener;
    private int currentPage;

    private LecternContainer(@Nullable Player inventoryHolder, @Nullable String title) {
        super(inventoryHolder, ContainerTypes.LECTERN, title);
    }

    private LecternContainer(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        super(inventoryHolder, ContainerTypes.LECTERN, title);
    }

    public static @NotNull LecternContainer create(@Nullable Player inventoryHolder, @Nullable String title) {
        return new LecternContainer(inventoryHolder, title);
    }

    public static @NotNull LecternContainer create(@Nullable Player inventoryHolder, @NotNull BaseComponent title) {
        return new LecternContainer(inventoryHolder, title);
    }

    public static @NotNull LecternContainer createDefault(@Nullable Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.LECTERN);
    }

    @Override
    protected @NotNull LecternContainer getThis() {
        return this;
    }

    @Override
    public @NotNull LecternContainer copy(@Nullable Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public @NotNull LecternUpdateRequest updateContent() {
        return LecternUpdateRequest.create(this, contentData);
    }

    @Override
    protected void hookEventListener(@NotNull LecternContainer clone, @NotNull AnyEventListener listener) {
        clone.buttonClickListener = listener::handle;
        clone.pageOpenListener = listener::handle;
    }

    /**********************
     *  Events listening  *
     *********************/
    
    // --- button clicking listening
    public @NotNull LecternContainer buttonClickListener(@Nullable LecternButtonClickListener listener) {
        this.buttonClickListener = listener;
        return this;
    }
    
    public void onButtonClicked(@NotNull LecternButtonClickEvent event) {
        LecternButtonType button = event.getButtonType();
        
        if(button == LecternButtonType.NEXT_PAGE)
            this.currentPage++;
        else if(button == LecternButtonType.PREVIOUS_PAGE)
            this.currentPage--;

        if(button != LecternButtonType.TAKE_BOOK) {
            updateProperty(Property.Lectern.PAGE_NUMBER, currentPage);
            if(pageOpenListener != null)
                pageOpenListener.handle(new LecternPageOpenEvent(
                        event.getActor(),
                        event.getContainer(),
                        currentPage
                ));
        }
        
        if(buttonClickListener != null)
            buttonClickListener.handle(event);
    }

    // --- page opening listening
    public @NotNull LecternContainer pageOpenListener(@Nullable LecternPageOpenListener listener) {
        this.pageOpenListener = listener;
        return this;
    }

    public void onPageOpened(@NotNull LecternPageOpenEvent event) {
        this.currentPage = event.getPage();

        if(pageOpenListener != null)
            pageOpenListener.handle(event);
    }

    /*********************
     *  Inventory slots  *
     ********************/

    @Override
    public @NotNull IntRange containerSlots() {
        return new IntRange(0);
    }

    @Override
    public @NotNull IntRange playerInventorySlots() {
        return new IntRange(1, 27);
    }

    @Override
    public @NotNull IntRange playerHotbarSlots() {
        return new IntRange(28, 36);
    }

    public interface LecternUpdateRequest extends ContentUpdateRequest<LecternContainer, LecternUpdateRequest> {

        static @NotNull LecternUpdateRequest create(
                @NotNull LecternContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            return new BaseLecternUpdateRequest(container, contentData);
        }

        static @NotNull LecternUpdateRequest create(
                @NotNull LecternContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            return new BaseLecternUpdateRequest(container, contentData, slotsOffset);
        }

        // --- book slot
        @NotNull LecternUpdateRequest book(@Nullable ItemStack item);

        default @NotNull LecternUpdateRequest book(@NotNull Material type, int amount) {
            Validate.notNull(type, "type");
            return book(new ItemStack(type, amount));
        }

        default @NotNull LecternUpdateRequest book(@NotNull Material type) {
            return book(type, 1);
        }

    }

    private static final class BaseLecternUpdateRequest extends BaseContentUpdateRequest<LecternContainer, LecternUpdateRequest> implements LecternUpdateRequest {
        private BaseLecternUpdateRequest(
                @NotNull LecternContainer container,
                @NotNull Map<Integer, ItemStack> contentData
        ) {
            super(container, contentData);
        }

        private BaseLecternUpdateRequest(
                @NotNull LecternContainer container,
                @NotNull Map<Integer, ItemStack> contentData,
                int slotsOffset
        ) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public @NotNull LecternUpdateRequest book(@Nullable ItemStack item) {
            return set(item, BOOK_SLOT, true);
        }
    }
    
}
