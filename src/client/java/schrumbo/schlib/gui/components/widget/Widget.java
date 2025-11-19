package schrumbo.schlib.gui.components.widget;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import schrumbo.schlib.gui.SchlibScreen;
import schrumbo.schlib.gui.components.category.Category;

/**
 * base class for widgets such as sliders, switches, etc...
 */
public abstract class Widget {
    protected String label;
    protected String description;
    protected int x;
    protected int y;
    protected int width;
    protected int height = 21;
    public SchlibScreen parentScreen;
    public Category parentCategory;

    protected Widget(Builder<?> builder){
        label = builder.label;
        description = builder.description;
    }

    /**
     * widget rendering goes here
     * @param context
     * @param mouseX
     * @param mouseY
     */
    public abstract void render(DrawContext context, double mouseX, double mouseY);

    /**
     * does something if the mouse is dragged
     * @param click
     * @param offsetX
     * @param offsetY
     * @return
     */
    public abstract boolean mouseDragged(Click click, double offsetX, double offsetY);

    /**
     * does something if click is registered
     * @param click
     * @return
     */
    public abstract boolean mouseClicked(Click click);
    /**
     * checks if the widget is hovered
     * @param mouseX
     * @param mouseY
     * @return
     */
    public boolean isHovered(double mouseX, double mouseY){
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    /**
     * gets the label of the widget
     * @return label
     */
    public String getLabel(){
        return label;
    }

    /**
     * gets the description of the widget
     * @return description
     */
    public String getDescription(){
        return description;
    }

    /**
     * gets the widget's height
     * @return height
     */
    public int getHeight(){
        return height;
    }

    /**
     * sets the x and y coordinate of the widget
     * @param x
     * @param y
     */
    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * sets the width of the widget
     * @param width
     */
     public void setWidth(int width){
        this.width = width;
     }

    /**
     * sets parent screen of a widget
     * @param screen
     */
    public void setParentScreen(SchlibScreen screen) {
        this.parentScreen = screen;
    }

    /**
     * sets parent category of a widget
     * @param category
     */
    public void setParentCategory(Category category) {
        this.parentCategory = category;
    }

    public static abstract class Builder<T extends Builder<T>>{
        protected String label;
        protected String description;

        public T label(String label){
            this.label = label;
            return self();
        }

        public T description(String description){
            this.description = description;
            return self();
        }

        protected abstract T self();
        public abstract  Widget build();
    }







}
