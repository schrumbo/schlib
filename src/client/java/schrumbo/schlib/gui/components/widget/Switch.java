package schrumbo.schlib.gui.components.widget;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import schrumbo.schlib.gui.theme.Theme;
import schrumbo.schlib.utils.RenderUtils2D;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Switch extends Widget{

    private final Supplier<Boolean> getter;
    private final Consumer<Boolean> setter;

    private final int buttonHeight = 11;
    private final int buttonWidth = 20;

    protected Switch(Builder builder){
        super(builder);
        this.getter = builder.getter;
        this.setter = builder.setter;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY) {
        super.render(context, mouseX, mouseY);
        renderButton(context);
    }

    /**
     * renders a button like thing
     * @param context
     */
    private void renderButton(DrawContext context){
        Theme screenTheme = parentScreen.getTheme();

        int buttonX2 = x + width - clickablePadding;
        int buttonX = buttonX2 - buttonWidth;
        int buttonY = y + height / 2 - buttonHeight / 2;
        int buttonY2 = buttonY + buttonHeight;

        int buttonBackgroundColor = getter.get() ? screenTheme.systemBlue : screenTheme.systemGray;
        context.fill(buttonX, buttonY, buttonX2, buttonY2, buttonBackgroundColor);

        int enabledX2 = buttonX2 - 1;
        int disabledX = buttonX + 1;
        int knobSize = 9;
        int knobY = buttonY + 1;
        int knobY2 = buttonY2 - 1;

        if (getter.get()){
            context.fill(enabledX2 - knobSize, knobY, enabledX2, knobY2, screenTheme.gridColor);
        }else{
            context.fill(disabledX, knobY, disabledX + knobSize, knobY2, screenTheme.gridColor);
        }
    }

    /**
     * dont need
     * @param click
     * @param offsetX
     * @param offsetY
     * @return
     */
    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        return false;
    }

    /**
     * dont need
     * @param click
     * @return
     */
    @Override
    public boolean mouseReleased(Click click) {
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
