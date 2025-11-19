package schrumbo.schlib.gui.components.category;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import schrumbo.schlib.Schlib;
import schrumbo.schlib.gui.SchlibScreen;
import schrumbo.schlib.gui.components.widget.Widget;
import schrumbo.schlib.gui.theme.Theme;
import schrumbo.schlib.utils.RenderUtils2D;

import java.util.ArrayList;
import java.util.List;

import static schrumbo.schlib.SchlibClient.mc;
import static schrumbo.schlib.SchlibClient.theme;

public class MainCategory extends Category{
    public List<SubCategory> subCategories;
    protected List<Widget> widgets;
    private boolean expanded = false;

    private final int PADDING = 3;

    protected MainCategory(Builder builder) {
        super(builder);
        this.subCategories = new ArrayList<>(builder.subCategories);
        this.widgets = new ArrayList<>(builder.widgets);
        for (var subCategory : subCategories){
            subCategory.parentCategory = this;
        }
        for (var widget : widgets){
            widget.setParentCategory(this);
        }
    }


    @Override
    public void render(DrawContext context, double mouseX, double mouseY) {
        Theme screenTheme = parentScreen.getTheme();
        int mainColor = isHovered(mouseX, mouseY) ? screenTheme.baseBackgroundColor : screenTheme.componentBackgroundColor;
        RenderUtils2D.drawFancyBox(context, x, y, x + width, y + height, mainColor, screenTheme.darkBorderColor, screenTheme.lightBorderColor);

        if (parentScreen.selectedCategory() == this || checkIfSelectedIsChild()){
            expanded = true;
        } else {
            expanded = false;
        }

        int textColor = expanded ? screenTheme.selectedTextColor : screenTheme.textColor;
        int textX = x + 4 * PADDING;
        int textY = y + height / 2 - mc.textRenderer.fontHeight / 2;

        context.drawText(mc.textRenderer, label, textX, textY, textColor, true);
        drawIndicator(context);
        renderSubCategories(context, mouseX, mouseY);
    }

    /**
     * checks if the parent screen selected category is a child category of this category
     * @return true if is child
     */
    private boolean checkIfSelectedIsChild(){
        Category selected = parentScreen.selectedCategory();

        if (selected == null) return false;
        if (selected == this) return true;

        if (selected instanceof SubCategory) {
            return subCategories.contains(selected);
        }

        return false;
    }



    /**
     * indicates if the Category is collapsed or expanded
     * @param context
     */
    private void drawIndicator(DrawContext context) {
        int centerX = x + PADDING + 2;
        int centerY = y + height / 2;
        int color = theme.textColor;

        if (!expanded) {
            context.fill(centerX + 1, centerY,     centerX + 2, centerY + 1, color);
            context.fill(centerX,     centerY - 1, centerX + 1, centerY + 2, color);
            context.fill(centerX - 1, centerY - 2, centerX,     centerY + 3, color);
        } else {
            context.fill(centerX - 1, centerY - 1, centerX + 4, centerY,     color);
            context.fill(centerX,     centerY,     centerX + 3, centerY + 1, color);
            context.fill(centerX + 1, centerY + 1, centerX + 2, centerY + 2, color);
        }
    }

    @Override
    public boolean mouseClicked(Click click) {
        if (!isHovered(click.x(), click.y()))return false;
        parentScreen.searchBar.clearSearch();
        if (!expanded){
            expanded = true;
        } else{
            expanded = false;
        }
        parentScreen.setSelectedCategroy(this);
        Schlib.LOGGER.info(parentScreen.selectedCategory().label);

        return true;
    }

    /**
     * renders sub category if expanded and existent
     * @param context
     * @param mouseX
     * @param mouseY
     */
    private void renderSubCategories(DrawContext context, double mouseX, double mouseY) {
        if (expanded && !subCategories.isEmpty()) {
            int currentY = y + height;
            for (SubCategory subCategory : subCategories) {
                subCategory.setY(currentY);
                subCategory.render(context, mouseX, mouseY);
                currentY += subCategory.height;
            }
        }
    }

    /**
     * gets the total height of a category with expanded sub categories
     * @return height
     */
    public int getTotalHeight() {
        if (!expanded) return height;

        int total = height;
        for (SubCategory subCategory : subCategories) {
            total += subCategory.height;
        }
        return total;
    }

    /**
     * sets parent screen for all widgets and sub categories of this main category
     * @param screen
     */
    public void setParentScreen(SchlibScreen screen) {
        this.parentScreen = screen;

        for (Widget widget : widgets) {
            widget.setParentScreen(screen);
        }

        for (SubCategory subCategory : subCategories) {
            for (var widget : subCategory.getWidgets()){
                widget.setParentScreen(screen);
            }
            subCategory.parentScreen = screen;
        }
    }

    /**
     * sets the categories position
     * @param x
     * @param y
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * sets category width
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
        updateSubCategoriesPosition();
    }

    /**
     * updates the child categories positions
     */
    private void updateSubCategoriesPosition() {
        for (SubCategory subCategory : subCategories) {
            subCategory.updatePositionFromParent();
        }
    }

    /**
     * gets all the widgets in the category
     * @return
     */
    public List<Widget> getWidgets(){
        return widgets;
    }

    /**
     * gets all sub categories
     * @return
     */
    public List<SubCategory> getSubCategories(){
        return subCategories;
    }



    public static class Builder extends Category.Builder<Builder> {
        protected final List<SubCategory> subCategories = new ArrayList<>();

        public Builder addSubCategory(SubCategory subCategory) {
            if (subCategory != null) {
                this.subCategories.add(subCategory);
            }
            return self();
        }

        public Builder addSubCategories(SubCategory... subCategories) {
            for (SubCategory subCategory : subCategories) {
                addSubCategory(subCategory);
            }
            return self();
        }

        public Builder addSubCategories(List<SubCategory> subCategories) {
            if (subCategories != null) {
                this.subCategories.addAll(subCategories);
            }
            return self();
        }


        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public MainCategory build() {
            return new MainCategory(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }


}
