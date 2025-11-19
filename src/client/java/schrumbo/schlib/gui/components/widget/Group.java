package schrumbo.schlib.gui.components.widget;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import schrumbo.schlib.gui.SchlibScreen;
import schrumbo.schlib.gui.components.category.Category;
import schrumbo.schlib.gui.theme.Theme;
import schrumbo.schlib.utils.RenderUtils2D;

import java.util.ArrayList;
import java.util.List;

/**
 * used to group widgets into an expandable widget
 * TODO add indicator for expanded state
 */
public class Group extends Widget{
    private List<Widget> widgets;
    private boolean expanded = false;
    private int expandedHeight;

    protected Group (Builder builder){
        super(builder);
        this.widgets = new ArrayList<>(builder.widgets);
        calculateExpandedHeight();
    }


    /**
     * calculates the expanded height of the group
     */
    private void calculateExpandedHeight() {
        expandedHeight = height;
        for (Widget child : widgets) {
            expandedHeight += child.getHeight() + 3;
        }
    }

    /**
     * gets the current height of the group, dependent on if it's expanded or not
     * @return height
     */
    @Override
    public int getHeight(){
        return expanded ? expandedHeight : height;
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        if (expanded) {
            int childY = y + 21 + 3;
            int childX = x + 12;
            for (var child : widgets) {
                child.setPosition(childX, childY);
                childY += child.getHeight() + 3;
            }
        }
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        int childWidth = width - 12;
        for (var child : widgets) {
            child.setWidth(childWidth);
        }
    }

    /**
     * sets the parent screen for the group and all it's children
     * @param screen
     */
    @Override
    public void setParentScreen(SchlibScreen screen) {
        super.setParentScreen(screen);
        for (var child : widgets) {
            child.setParentScreen(screen);
        }
    }
    @Override
    public void setParentCategory(Category category) {
        super.setParentCategory(category);
        for (var child : widgets) {
            child.setParentCategory(category);
        }
    }


    @Override
    public void render(DrawContext context, double mouseX, double mouseY) {
        Theme screenTheme = parentScreen.getTheme();
        int mainColor = isHovered(mouseX, mouseY) ? screenTheme.baseBackgroundColor : screenTheme.componentBackgroundColor;
        RenderUtils2D.drawFancyBox(context, x, y, x + width, y + height, mainColor, screenTheme.darkBorderColor, screenTheme.lightBorderColor);

        int textColor = screenTheme.textColor;
        int textX = x + 3;
        int textY = y + height / 2 - parentScreen.getTextRenderer().fontHeight / 2;
        context.drawText(parentScreen.getTextRenderer(), label, textX, textY, textColor, true);
        if (expanded){
            for (var child : widgets){
                child.render(context, mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        return false;
    }

    /**
     * handles mouse clicks with passthrough to child widgets of the group
     * @param click
     * @return
     */
    @Override
    public boolean mouseClicked(Click click) {
        if (isHovered(click.x(), click.y())){
            expanded = !expanded;
            return true;
        }
        for (var widget : widgets){
            if (widget.mouseClicked(click)){
                return true;
            }
        }
        return false;
    }

    public static class Builder extends Widget.Builder<Builder>{
        private final List<Widget> widgets = new ArrayList<>();

        public Builder addWidget(Widget widget) {
            if (widget != null) {
                this.widgets.add(widget);
            }
            return self();
        }

        public Builder addWidgets(Widget... widgets) {
            for (Widget widget : widgets) {
                addWidget(widget);
            }
            return self();
        }

        public Builder addWidgets(List<Widget> widgets) {
            if (widgets != null) {
                this.widgets.addAll(widgets);
            }
            return self();
        }

        @Override
        protected Builder self(){
            return this;
        }

        @Override
        public Group build(){
            return new Group(this);
        }
    }
    public static Builder builder(){
        return new Builder();
    }
}
