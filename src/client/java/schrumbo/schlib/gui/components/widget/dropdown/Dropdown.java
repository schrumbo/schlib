package schrumbo.schlib.gui.components.widget.dropdown;

import schrumbo.schlib.gui.components.widget.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * abstract dropdown class used in
 * {@link SingleSelectDropdown}
 * {@link MultiSelectDropdown}
 * @param <T>
 */
public abstract class Dropdown<T> extends Widget{

    protected final List<DropdownEntry<T>> entries;

    protected boolean expanded = false;
    protected int hoveredIndex = -1;

    protected static final int ENTRY_HEIGHT = 19;
    protected static final int maxVisibleEntries = 5;
    protected static final int DROPDOWN_ARROW_SIZE = 8;
    protected int scrollOffset = 0;

    protected Dropdown(Builder<?, T> builder){
        super(builder);
        this.entries = new ArrayList<>(builder.entries);
    }

    /**
     * Gets all entries
     */
    public List<DropdownEntry<T>> getEntries() {
        return new ArrayList<>(entries);
    }

    /**
     * Checks if dropdown is currently open
     */
    public boolean isExpanded() {
        return expanded;
    }

    /**
     * Closes the dropdown
     */
    public void close() {
        expanded = false;
        hoveredIndex = -1;
    }

    public static abstract class Builder<B extends Builder<B, T>, T> extends Widget.Builder<B> {
        protected List<DropdownEntry<T>> entries = new ArrayList<>();

        public B addEntry(DropdownEntry<T> entry) {
            this.entries.add(entry);
            return self();
        }

        public B entries(List<DropdownEntry<T>> entries) {
            this.entries = new ArrayList<>(entries);
            return self();
        }
    }
}

