package schrumbo.schlib.gui.components.category;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import schrumbo.schlib.gui.components.widget.Widget;
import schrumbo.schlib.gui.theme.Theme;
import schrumbo.schlib.utils.RenderUtils2D;

import java.util.ArrayList;
import java.util.List;

public class SubCategory extends Category{
    protected List<Widget> widgets;
    protected MainCategory parentCategory;
    protected SubCategory(Builder builder){
        super(builder);
        widgets = builder.widgets;
        for (var widget : widgets){
            widget.parentScreen = parentScreen;
            widget.parentCategory = this;
        }
    }

    /**
     * gets the parent category's position and syncs it with the child
     * already subtracts the offset position
     */
    protected void updatePositionFromParent() {
        this.x = parentCategory.x  + 9;
        this.width = parentCategory.width - 9;
    }

    public void setY(int y) {
        this.y = y;
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
    }

    @Override
    public boolean mouseClicked(Click click) {
        return false;
    }


    public static class Builder extends Category.Builder<Builder>{
        protected final List<Widget> widgets = new ArrayList<>();


        public Builder addWidget(Widget widget){
            this.widgets.add(widget);
            return self();
        }

        public Builder addWidgets(Widget... widgets){
            for (var widget : widgets){
                addWidget(widget);
            }
            return self();
        }

        public Builder addWidgets(List<Widget> widgets){
            if (widgets != null){
                this.widgets.addAll(widgets);
            }
            return self();
        }

        @Override
        protected Builder self(){
            return this;
        }
        @Override
        public SubCategory build(){
            return new SubCategory(this);
        }
    }
    public static Builder builder(){
        return new Builder();
    }
}
