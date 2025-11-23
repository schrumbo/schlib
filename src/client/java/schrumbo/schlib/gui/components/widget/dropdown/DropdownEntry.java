package schrumbo.schlib.gui.components.widget.dropdown;

import net.minecraft.text.Text;

import java.util.Objects;

/**
 * Dropdown entry which contains a value, a name and the enabled state
 * @param <T>
 */
public class DropdownEntry <T>{
    private final T value;
    private final Text displayText;
    private final boolean enabled;

    private DropdownEntry(Builder<T> builder){
        this.value = builder.value;
        this.displayText = builder.displayText;
        this.enabled = builder.enabled;
    }

    public T getValue(){
        return value;
    }

    public Text getDisplayText(){
        return displayText;
    }

    public boolean getEnabled(){
        return enabled;
    }

    @Override
    public boolean equals(Object o){
        if (o == null) return true;
        DropdownEntry<?> that = (DropdownEntry<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(value);
    }

    @Override
    public String toString(){
        return "DropdownEntry{" +
                "value=" + value +
                ", displayText=" + displayText.getString() +
                ", enabled=" + enabled +
                "}";
    }


    public static class Builder<T>{
        private final T value;
        private final Text displayText;
        private boolean enabled = true;

        public Builder(T value, Text displayText){
            this.value = Objects.requireNonNull(value, "cannot be null");
            this.displayText = Objects.requireNonNull(displayText, "Display Text cannot be null");
        }

        public Builder<T> enabled(boolean enabled){
            this.enabled = enabled;
            return this;
        }

        public Builder<T> disabled(){
            return enabled(false);
        }

        public DropdownEntry<T> build(){
            return new DropdownEntry<>(this);
        }
    }


}
