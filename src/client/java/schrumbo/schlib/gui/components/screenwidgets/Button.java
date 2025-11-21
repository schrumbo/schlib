package schrumbo.schlib.gui.components.screenwidgets;
import net.minecraft.client.gui.DrawContext;
import schrumbo.schlib.Schlib;
import schrumbo.schlib.gui.theme.Theme;
import schrumbo.schlib.utils.RenderUtils2D;



public class Button extends ScreenWidget {
    private final Runnable onClick;
    private final int color;
    private final ButtonRenderer customRenderer;



    private Button(Builder builder) {
        super(builder);
        this.onClick = builder.onClick;
        this.color = builder.color;
        this.customRenderer = builder.customRenderer;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY) {
        Theme screenTheme = parent.getTheme();
        RenderUtils2D.drawBoxWithShadow(context, x, y, x + width, y + height, color, screenTheme.shadowColor, screenTheme.controlBackgroundColor);
        if (this.customRenderer == null)return;
        if (isHovered(mouseX, mouseY)){
            customRenderer.render(context, x, y, width, height);
        }
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
        private int color;
        private ButtonRenderer customRenderer;

        public Builder onClick(Runnable runnable) {
            this.onClick = runnable;
            return this;
        }

        public Builder color(int color){
            this.color = color;
            return this;
        }
        public Builder customRenderer(ButtonRenderer renderer) {
            this.customRenderer = renderer;
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
