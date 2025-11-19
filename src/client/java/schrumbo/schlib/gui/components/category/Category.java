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
    public SchlibScreen parent;

    protected Category(Builder<?> builder){
        x = builder.x;
        y = builder.y;
        width = builder.width;
        label = builder.label;
        this.parent = builder.parent;
    }

    /**
     * category rendering goes here
     * @param context
     * @parae mouseX
     * @paramemouseY
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
        protected int x;
        protected int y;
        protected int width;
        protected SchlibScreen parent;

        public T parentScreen(SchlibScreen screen){
            this.parent = screen;
            return self();
        }

        public T label(String label){
            this.label = label;
            return self();
        }

        public T position(int x, int y){
            this.x = x;
            this.y = y;
            return self();
        }

        public T width(int width){
            this.width = width;
            return self();
        }

        protected abstract T self();
        public abstract Category build();
    }
}
