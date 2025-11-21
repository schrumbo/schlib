package schrumbo.schlib.gui.components.category;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import schrumbo.schlib.Schlib;
import schrumbo.schlib.gui.components.widget.Widget;
import schrumbo.schlib.gui.theme.Theme;
import schrumbo.schlib.utils.RenderUtils2D;

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

    @Override
    public void render(DrawContext context, double mouseX, double mouseY) {
        super.render(context, mouseX, mouseY);
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * gets the parent category of this sub category
     * @return parent
     */
    public MainCategory getParentCategory(){
        return parentCategory;
    }

    @Override
    public boolean mouseClicked(Click click) {
        if (!isHovered(click.x(), click.y()))return false;
        parentScreen.searchBar.clearSearch();
        parentScreen.setSelectedCategroy(this);
        return false;
    }


    public static class Builder extends Category.Builder<Builder>{

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
