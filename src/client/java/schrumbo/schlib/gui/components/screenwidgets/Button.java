package schrumbo.schlib.gui.components.screenwidgets;

import net.minecraft.client.gui.DrawContext;
import schrumbo.schlib.Schlib;
import schrumbo.schlib.gui.SchlibScreen;
import schrumbo.schlib.gui.components.screenwidgets.ScreenWidget;
import schrumbo.schlib.utils.RenderUtils2D;

import static schrumbo.schlib.gui.SchlibScreen.screenTheme;

public class Button extends ScreenWidget {
    private final Runnable onClick;

    private Button(Builder builder) {
        super(builder);
        this.onClick = builder.onClick;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY) {
        int color = screenTheme.baseBackgroundColor;
        RenderUtils2D.drawFancyBox(context, x, y, x + width, y + height, color, screenTheme.darkBorderColor, screenTheme.lightBorderColor);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!(button == 0) && !(button == 1)) return false;
        if (!isHovered(mouseX, mouseY)) return false;
        try {
            onClick.run();
        } catch (Exception e) {
            Schlib.LOGGER.error("Error running: {}", onClick.toString());
        }
        return false;
    }

    public static class Builder extends ScreenWidget.Builder<Builder> {
        private Runnable onClick;

        public Builder onClick(Runnable runnable) {
            this.onClick = runnable;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Button build() {
            return new Button(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
