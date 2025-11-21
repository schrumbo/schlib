package schrumbo.schlib.gui.components.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import schrumbo.schlib.Schlib;
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

    public List<Widget> getChildren(){
        return  widgets;
    }

    public void setExpanded(boolean value){
        expanded = value;
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
        super.render(context, mouseX, mouseY);
        renderExpandedIndicator(context);
        if (expanded){
            for (var child : widgets){
                child.render(context, mouseX, mouseY);
            }
        }
    }

    /**
     * renders something that indicates if the group is expanded or collapsed
     * @param context
     */
    private void renderExpandedIndicator(DrawContext context){
        Theme screenTheme = parentScreen.getTheme();
        int indicatorMaxLength = 11;
        int indicatorSize = 5;
        int color = screenTheme.systemGray;

        if (!expanded){
            int indicatorYCollapsed = y + 4;
            int indicatorY2Collapsed = indicatorYCollapsed + indicatorMaxLength + 1;
            int indicatorXCollapsed = x + width - 7;
            context.drawVerticalLine(indicatorXCollapsed, indicatorYCollapsed, indicatorY2Collapsed, color);
            context.drawVerticalLine(indicatorXCollapsed - 1, indicatorYCollapsed + 1, indicatorY2Collapsed - 1, color);
            context.drawVerticalLine(indicatorXCollapsed - 2, indicatorYCollapsed + 2, indicatorY2Collapsed - 2, color);
            context.drawVerticalLine(indicatorXCollapsed - 3, indicatorYCollapsed + 3, indicatorY2Collapsed - 3, color);
            context.drawVerticalLine(indicatorXCollapsed - 4, indicatorYCollapsed + 4, indicatorY2Collapsed - 4, color);
            context.drawVerticalLine(indicatorXCollapsed - 5, indicatorYCollapsed + 5, indicatorY2Collapsed - 5, color);
        }else{
            int indicatorXCenter = x + width - 12;
            int indicatorYExpanded = y + 8;
            context.drawHorizontalLine(indicatorXCenter - 5, indicatorXCenter + 5, indicatorYExpanded, color);
            context.drawHorizontalLine(indicatorXCenter - 4, indicatorXCenter + 4, indicatorYExpanded + 1, color);
            context.drawHorizontalLine(indicatorXCenter - 3, indicatorXCenter + 3, indicatorYExpanded + 2, color);
            context.drawHorizontalLine(indicatorXCenter - 2, indicatorXCenter + 2, indicatorYExpanded + 3, color);
            context.drawHorizontalLine(indicatorXCenter - 1, indicatorXCenter + 1, indicatorYExpanded + 4, color);
            context.drawHorizontalLine(indicatorXCenter, indicatorXCenter, indicatorYExpanded + 5, color);
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
