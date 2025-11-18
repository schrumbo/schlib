package schrumbo.schlib.gui.components.screenwidgets;

import net.minecraft.client.gui.DrawContext;

@FunctionalInterface
public interface ButtonRenderer {
    void render(DrawContext context, int x, int y, int width, int height);
}
