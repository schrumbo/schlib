package schrumbo.schlib.gui.components.category;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import schrumbo.schlib.gui.SchlibScreen;

public abstract class Category {
    protected String label;
    protected int x;
    protected int y;
    protected int width;
    protected final int height = 12;
    public SchlibScreen parentScreen;

    protected Category(Builder<?> builder){
        label = builder.label;
    }

    /**
     * category rendering goes here
     * @param context
     * @param mouseX
     * @param mouseY
     */
    public abstract void render(DrawContext context, double mouseX, double mouseY);

    /**
     * does something if clicked
     * @param click
     * @return
     */
    public abstract boolean mouseClicked(Click click);

    /**
     * checks if the category ist currently hovered
     * @param mouseX
     * @param mouseY
     * @return
     */
    public boolean isHovered(double mouseX, double mouseY){
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    /**
     * builder design pattern
     * @param <T>
     */
    public static abstract class Builder<T extends Builder<T>>{
        protected String label;

        public T label(String label){
            this.label = label;
            return self();
        }

        protected abstract T self();
        public abstract Category build();
    }
}
