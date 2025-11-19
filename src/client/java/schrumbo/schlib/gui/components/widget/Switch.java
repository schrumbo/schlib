package schrumbo.schlib.gui.components.widget;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import schrumbo.schlib.Schlib;
import schrumbo.schlib.gui.components.category.MainCategory;
import schrumbo.schlib.gui.theme.Theme;
import schrumbo.schlib.utils.RenderUtils2D;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Switch extends Widget{

    private final Supplier<Boolean> getter;
    private final Consumer<Boolean> setter;

    protected Switch(Builder builder){
        super(builder);
        this.getter = builder.getter;
        this.setter = builder.setter;
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
        renderButton(context, mouseX, mouseY);
    }

    /**
     * renders a button like thing
     * @param context
     * @param mouseX
     * @param mouseY
     */
    private void renderButton(DrawContext context, double mouseX, double mouseY){
        Theme screenTheme = parentScreen.getTheme();
        int buttonSize = height - 6;
        int buttonX2 = x + width - 3;
        int buttonX = buttonX2 - buttonSize;
        int buttonY = y + height / 2 - buttonSize / 2;

        int fillColor = getter.get() ? screenTheme.widgetEnabledColor : screenTheme.widgetDisabledColor;
        RenderUtils2D.drawFancyBox(context, buttonX, buttonY, buttonX2, buttonY + buttonSize, fillColor, screenTheme.darkBorderColor, screenTheme.lightBorderColor);
    }


    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        return false;
    }

    @Override
    public boolean mouseClicked(Click click) {
        if (isHovered(click.x(), click.y())) {
            setter.accept(!getter.get());
            return true;
        }
        return false;
    }


    public static class Builder extends Widget.Builder<Builder>{
        protected Supplier<Boolean> getter;
        protected Consumer<Boolean> setter;

        public Builder value(Supplier<Boolean> getter, Consumer<Boolean> setter){
            this.getter = getter;
            this.setter = setter;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Switch build(){
            return new Switch(this);
        }
    }
    public static Builder builder(){
        return new Builder();
    }
}
