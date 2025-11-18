package schrumbo.schlib.utils;

import net.minecraft.client.gui.DrawContext;

public class RenderUtils2D {
    /**
     * renders a fancy box with 3d effect
     * @param context draw context
     * @param x
     * @param y
     * @param x2
     * @param y2
     * @param mainColor background color of the box
     * @param darkBorderColor bottom and right border
     * @param lightBorderColor top and left corner
     */
    public static void drawFancyBox(DrawContext context, int x, int y, int x2, int y2, int mainColor, int darkBorderColor, int lightBorderColor){
        var matrices = context.getMatrices();
        context.fill(x, y, x2, y2, mainColor);
        context.drawVerticalLine(x2, y - 1, y2, darkBorderColor);
        context.drawHorizontalLine(x, x2, y2 -1, darkBorderColor);

        context.drawHorizontalLine(x, x2, y - 1, lightBorderColor);
        context.drawVerticalLine(x, y - 1, y2, lightBorderColor);

    }



}
