package schrumbo.schlib.gui.components.category;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import schrumbo.schlib.gui.SchlibScreen;
import schrumbo.schlib.gui.components.widget.Widget;
import schrumbo.schlib.gui.theme.Theme;
import schrumbo.schlib.utils.RenderUtils2D;

import java.util.ArrayList;
import java.util.List;

import static schrumbo.schlib.SchlibClient.mc;

public abstract class Category {
    protected String label;
    protected int x;
    protected int y;
    protected int width;
    protected final int height = 13;
    public SchlibScreen parentScreen;
    protected List<Widget> widgets;
    protected final int PADDING = 3;

    protected Category(Builder<?> builder){
        label = builder.label;
        widgets = builder.widgets;
    }

    /**
     * handles basic rendering like label and background
     * @param context
     * @param mouseX
     * @param mouseY
     */
    public void render(DrawContext context, double mouseX, double mouseY){
        Theme screenTheme = parentScreen.getTheme();
        int mainColor = isHovered(mouseX, mouseY) ? screenTheme.windowBackgroundColor : screenTheme.gridColor;
        int selectedColor = screenTheme.windowBackgroundColor;
        if (parentScreen.selectedCategory() == this){
            context.fill(x, y, x + width, y + height, selectedColor);
            RenderUtils2D.drawOutline(context, x, y, x + width, y + height, screenTheme.systemGray);
        }else{
            context.fill(x, y, x + width, y + height, mainColor);
        }

        int textColor =screenTheme.textColor;
        int textX;
        if (this instanceof MainCategory){
            textX = x + 4 * PADDING;
        }else{
            textX = x + PADDING;
        }

        int textY = y + height / 2 - mc.textRenderer.fontHeight / 2 + 1;

        context.drawText(mc.textRenderer, label, textX, textY, textColor, false);
    }

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
     * gets the label of a category
     * @return label
     */
    public String getLabel(){
        return label;
    }


    /**
     * builder design pattern
     * @param <T>
     */
    public static abstract class Builder<T extends Builder<T>>{
        protected String label;
        protected List<Widget> widgets = new ArrayList<>();

        public T label(String label){
            this.label = label;
            return self();
        }
        public T addWidget(Widget widget) {
            if (widget != null) {
                this.widgets.add(widget);
            }
            return self();
        }

        public T addWidgets(Widget... widgets) {
            for (Widget widget : widgets) {
                addWidget(widget);
            }
            return self();
        }

        public T addWidgets(List<Widget> widgets) {
            if (widgets != null) {
                this.widgets.addAll(widgets);
            }
            return self();
        }
        protected abstract T self();
        public abstract Category build();
    }

    public List<Widget> getWidgets(){
        return widgets;
    }
}
