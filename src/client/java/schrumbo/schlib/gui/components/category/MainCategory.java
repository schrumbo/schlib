package schrumbo.schlib.gui.components.category;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import schrumbo.schlib.gui.components.widget.Widget;
import schrumbo.schlib.gui.theme.Theme;
import schrumbo.schlib.utils.RenderUtils2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static schrumbo.schlib.SchlibClient.mc;
import static schrumbo.schlib.SchlibClient.theme;

public class MainCategory extends Category{
    protected List<SubCategory> subCategories;
    protected List<Widget> widgets;
    private boolean expanded = false;

    private final int PADDING = 3;

    protected MainCategory(Builder builder) {
        super(builder);
        this.subCategories = new ArrayList<>(builder.subCategories);
        this.widgets = new ArrayList<>(builder.widgets);
    }


    @Override
    public void render(DrawContext context, double mouseX, double mouseY) {
        Theme screenTheme = parent.getTheme();
        int mainColor = isHovered(mouseX, mouseY) ? screenTheme.baseBackgroundColor : screenTheme.componentBackgroundColor;
        RenderUtils2D.drawFancyBox(context, x, y, x + width, y + height, mainColor, screenTheme.darkBorderColor, screenTheme.lightBorderColor);

        int textColor = screenTheme.textColor;
        //Indicator kinda fat
        int textX = x + 4 * PADDING;
        int textY = y + height / 2 - mc.textRenderer.fontHeight / 2 + 1;

        context.drawText(mc.textRenderer, label, textX, textY, textColor, true);
        drawIndicator(context);
    }

    /**
     * renders the Categories SubCategories if expanded
     */
    private void renderSubCategories(){
        if (expanded){
            //TODO
        }
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
        expanded = !expanded;
        return true;
    }


    public static class Builder extends Category.Builder<Builder> {
        protected final List<SubCategory> subCategories = new ArrayList<>();
        protected final List<Widget> widgets = new ArrayList<>();

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
        protected Builder self() {
            return this;
        }

        @Override
        public MainCategory build() {
            if (parent == null) {
                throw new IllegalStateException("Parent screen must be set");
            }
            return new MainCategory(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }


}
