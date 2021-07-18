package ru.soknight.packetinventoryapi.container.type;

import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.ContainerLocaleTitles;
import ru.soknight.packetinventoryapi.container.ContainerType;
import ru.soknight.packetinventoryapi.container.data.LecternButtonType;
import ru.soknight.packetinventoryapi.container.data.Property;
import ru.soknight.packetinventoryapi.event.container.LecternButtonClickEvent;
import ru.soknight.packetinventoryapi.event.container.LecternPageOpenEvent;
import ru.soknight.packetinventoryapi.item.update.BaseContentUpdateRequest;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.AnyEventListener;
import ru.soknight.packetinventoryapi.listener.event.EventListener;
import ru.soknight.packetinventoryapi.listener.event.container.LecternButtonClickListener;
import ru.soknight.packetinventoryapi.listener.event.container.LecternPageOpenListener;
import ru.soknight.packetinventoryapi.util.IntRange;

import java.util.Map;

@Getter
public class LecternContainer extends Container<LecternContainer, LecternContainer.LecternUpdateRequest> {

    public static final int BOOK_SLOT = 0;

    private EventListener<LecternButtonClickEvent> buttonClickListener;
    private EventListener<LecternPageOpenEvent> pageOpenListener;
    private int currentPage;

    private LecternContainer(Player inventoryHolder, String title) {
        super(inventoryHolder, ContainerType.LECTERN, title);
    }

    private LecternContainer(Player inventoryHolder, BaseComponent title) {
        super(inventoryHolder, ContainerType.LECTERN, title);
    }

    public static LecternContainer create(Player inventoryHolder, String title) {
        return new LecternContainer(inventoryHolder, title);
    }

    public static LecternContainer create(Player inventoryHolder, BaseComponent title) {
        return new LecternContainer(inventoryHolder, title);
    }

    public static LecternContainer createDefault(Player inventoryHolder) {
        return create(inventoryHolder, ContainerLocaleTitles.LECTERN);
    }

    @Override
    protected LecternContainer getThis() {
        return this;
    }

    @Override
    public LecternContainer copy(Player holder) {
        return create(holder, title.duplicate());
    }

    @Override
    public LecternUpdateRequest updateContent() {
        return LecternUpdateRequest.create(this, contentData);
    }

    @Override
    protected void hookEventListener(LecternContainer clone, AnyEventListener listener) {
        clone.buttonClickListener = listener::handle;
        clone.pageOpenListener = listener::handle;
    }

    /**********************
     *  Events listening  *
     *********************/
    
    // --- button clicking listening
    public LecternContainer buttonClickListener(LecternButtonClickListener listener) {
        this.buttonClickListener = listener;
        return this;
    }
    
    public void onButtonClicked(LecternButtonClickEvent event) {
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
    public LecternContainer pageOpenListener(LecternPageOpenListener listener) {
        this.pageOpenListener = listener;
        return this;
    }

    public void onPageOpened(LecternPageOpenEvent event) {
        this.currentPage = event.getPage();

        if(pageOpenListener != null)
            pageOpenListener.handle(event);
    }

    /*********************
     *  Inventory slots  *
     ********************/

    @Override
    public IntRange containerSlots() {
        return new IntRange(0);
    }

    @Override
    public IntRange playerInventorySlots() {
        return new IntRange(1, 27);
    }

    @Override
    public IntRange playerHotbarSlots() {
        return new IntRange(28, 36);
    }

    public interface LecternUpdateRequest extends ContentUpdateRequest<LecternContainer, LecternUpdateRequest> {

        static LecternUpdateRequest create(LecternContainer container, Map<Integer, ItemStack> contentData) {
            return new BaseLecternUpdateRequest(container, contentData);
        }

        static LecternUpdateRequest create(LecternContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            return new BaseLecternUpdateRequest(container, contentData, slotsOffset);
        }

        LecternUpdateRequest book(ItemStack item);
        default LecternUpdateRequest book(Material type, int amount) { return book(new ItemStack(type, amount)); }
        default LecternUpdateRequest book(Material type) { return book(type, 1); }

    }

    private static final class BaseLecternUpdateRequest extends BaseContentUpdateRequest<LecternContainer, LecternUpdateRequest> implements LecternUpdateRequest {
        private BaseLecternUpdateRequest(LecternContainer container, Map<Integer, ItemStack> contentData) {
            super(container, contentData);
        }

        private BaseLecternUpdateRequest(LecternContainer container, Map<Integer, ItemStack> contentData, int slotsOffset) {
            super(container, contentData, slotsOffset);
        }

        @Override
        public LecternUpdateRequest book(ItemStack item) {
            return set(item, BOOK_SLOT, true);
        }
    }
    
}
