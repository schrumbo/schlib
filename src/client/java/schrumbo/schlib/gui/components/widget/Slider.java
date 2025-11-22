package schrumbo.schlib.gui.components.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import schrumbo.schlib.Schlib;
import schrumbo.schlib.gui.theme.Theme;
import schrumbo.schlib.utils.RenderUtils2D;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Slider extends Widget{
    private final Supplier<Float> getter;
    private final Consumer<Float> setter;
    private final float min;
    private final float max;

    private boolean dragging = false;
    private int sliderX;
    private int sliderY;
    private int sliderHeight;
    private int sliderWidth;
    private int sliderPadding;

    protected Slider(Builder builder){
        super(builder);
        this.min = builder.min;
        this.max = builder.max;
        this.getter = builder.getter;
        this.setter = builder.setter;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY){
        calculateSliderDimensions();
        super.render(context, mouseX, mouseY);
        renderSlider(context);
    }

    /**
     * renders the slider
     * @param context
     */
    private void renderSlider(DrawContext context){
        Theme screenTheme = parentScreen.getTheme();

        float currentValue = getter.get();
        float percentage = (currentValue - min) / (max - min);
        int fillWidth = (int) (sliderWidth * percentage);

        context.fill(sliderX, sliderY, sliderX + sliderWidth, sliderY + sliderHeight, screenTheme.gridColor);
        if (fillWidth > 0){
            context.fill(sliderX, sliderY, sliderX + fillWidth, sliderY + sliderHeight, screenTheme.systemBlue);
        }
        renderKnob(context, sliderX + fillWidth, sliderY);

        int textY = y + PADDING + 1;
        int textX = sliderX - MinecraftClient.getInstance().textRenderer.getWidth(formatValue(getter.get())) - 5;
        context.drawText(MinecraftClient.getInstance().textRenderer, formatValue(getter.get()), textX, textY, screenTheme.textColor, false);
    }

    private void renderKnob(DrawContext context, int x, int y){
        Theme screenTheme = parentScreen.getTheme();
        int knobSize = sliderHeight + 2;
        int knobX = x - knobSize / 2;
        int knobY = y - 1;
        RenderUtils2D.drawBoxWithOutline(context, knobX, knobY, knobX + knobSize, knobY + knobSize, screenTheme.systemBlue, screenTheme.systemGray);
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        if (!dragging)return false;
        updateValueFromMouse(click.x());
        return true;
    }

    @Override
    public boolean mouseReleased(Click click) {
        if (click.button() == 0 && dragging) {
            dragging = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(Click click) {
        if (!hoveringSlider(click) || click.button() != 0)return false;
        dragging = true;
        updateValueFromMouse(click.x());
        return true;
    }

    /**
     * checks if the slider ist hovered
     * @param click
     * @return true if slider is hovered
     */
    private boolean hoveringSlider(Click click){
        return click.x() >= sliderX && click.x() <= sliderX + sliderWidth && click.y() >= sliderY && click.y() <= sliderY + sliderHeight;
    }

    /**
     * sets the new value with the mouse
     * @param mouseX
     */
    private void updateValueFromMouse(double mouseX) {
        double clampedX = Math.max(sliderX, Math.min(sliderX + sliderWidth, mouseX));

        float percentage = (float) ((clampedX - sliderX) / sliderWidth);
        float newValue = min + percentage * (max - min);

        newValue = roundValue(newValue);

        if (Math.abs(getter.get() - newValue) > 0.001f) {
            setter.accept(newValue);
        }
    }

    /**
     * decides in what steps the slider should progress
     * @param value
     * @return rounded value
     */
    private float roundValue(float value) {
        float range = max - min;

        if (range <= 1.0f) {
            return Math.round(value * 100) / 100.0f;
        } else if (range <= 5.0f) {
            return Math.round(value * 10) / 10.0f;
        } else if (range <= 20.0f) {
            return Math.round(value * 2) / 2.0f;
        } else {
            return Math.round(value);
        }
    }

    /**
     * formats the slider's value to a string
     * @param value
     * @return
     */
    private String formatValue(float value) {
        float range = max - min;
        if (range <= 1.0f) {
            return String.format("%.2f", value);
        } else if (range <= 10.0f) {
            return String.format("%.1f", value);
        } else {
            return String.format("%.0f", value);
        }
    }

    /**
     * calculates the sliders length and according to the widget width
     */
    private void calculateSliderDimensions(){
        sliderWidth = width / 3;
        sliderHeight = height / 3;
        sliderY = y + height / 2 - sliderHeight / 2;
        sliderX = (x + width) - clickablePadding - sliderWidth;

    }



    public static class Builder extends Widget.Builder<Builder>{
        protected Supplier<Float> getter;
        protected Consumer<Float> setter;
        protected float min;
        protected float max;

        public Builder range(int min, int max){
            this.min = min;
            this.max = max;
            return self();
        }

        public Builder value(Supplier<Float> getter, Consumer<Float> setter){
            this.getter = getter;
            this.setter = setter;
            return self();
        }

        @Override
        protected Builder self(){
            return this;
        }

        @Override
        public Slider build(){
            return new Slider(this);
        }
    }
    public static Builder builder(){
        return new Builder();
    }

}
