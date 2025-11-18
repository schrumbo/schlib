package schrumbo.schlib.gui.components.screenwidgets;

import net.minecraft.client.gui.DrawContext;

public abstract class ScreenWidget {
    protected static int x;
    protected static int y;
    protected static int width;
    protected static int height;
    protected static String label = "";
    protected static boolean hovering;

    protected ScreenWidget(Builder<?> builder){
        x = builder.x;
        y = builder.y;
        width = builder.width;
        height = builder.height;
        label = builder.label;
    }

    /**
     * renders the widget
     * @param context
     */
    public abstract void render(DrawContext context, double mouseX, double mouseY);

    /**
     * handles mouse clicks
     *
     * @param mouseX
     * @param mouseY
     * @param button
     */
    public abstract boolean mouseClicked(double mouseX, double mouseY, int button);

    /**
     * checks if the widget is currently hovered
     * @param mouseX
     * @param mouseY
     * @return
     */
    public static boolean isHovered(double mouseX, double mouseY){
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }



    public static abstract class Builder<T extends Builder<T>>{
        protected int x = 0;
        protected int y = 0;
        protected int width = 9;
        protected int height = 9;
        protected String label = "";

        public T position(int x, int y){
            this.x = x;
            this.y = y;
            return self();
        }

        public T size(int width, int height){
            this.width = width;
            this.height = height;
            return self();
        }

        public T label(String label){
            this.label = label;
            return self();
        }

        protected abstract T self();
        public abstract ScreenWidget build();
    }
}
