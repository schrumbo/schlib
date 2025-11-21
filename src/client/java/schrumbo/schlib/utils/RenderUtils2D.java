package schrumbo.schlib.utils;

import net.minecraft.client.gui.DrawContext;

public class RenderUtils2D {
    /**
     * renders box with shadow
     * @param context draw context
     * @param x
     * @param y
     * @param x2
     * @param y2
     * @param mainColor main color of the box
     * @param shadowColor shadow color
     */
    public static void drawBoxWithShadow(DrawContext context, int x, int y, int x2, int y2, int mainColor, int shadowColor){
        context.fill(x, y, x2, y2, mainColor);
        context.drawVerticalLine(x2, y, y2, shadowColor);
        context.drawHorizontalLine(x + 1, x2, y2, shadowColor);
    }

    /**
     * draws a box with an outline
     * @param context
     * @param x
     * @param y
     * @param x2
     * @param y2
     * @param boxColor
     * @param outlineColor
     */
    public static void drawBoxWithOutline(DrawContext context, int x, int y, int x2, int y2, int boxColor, int outlineColor){
        context.fill(x + 1, y + 1, x2 - 1, y2 - 1, boxColor);
        drawOutline(context, x, y, x2, y2, outlineColor);
    }

    /**
     * draws an outline
     * @param context
     * @param x
     * @param y
     * @param x2
     * @param y2
     * @param outlineColor
     */
    public static void drawOutline(DrawContext context, int x, int y, int x2, int y2, int outlineColor){
        //top
        context.fill(x, y, x2, y + 1, outlineColor);
        //left
        context.fill(x, y, x + 1, y2, outlineColor);
        //right
        context.fill(x2 - 1, y, x2, y2, outlineColor);
        //bot
        context.fill(x, y2 - 1, x2, y2, outlineColor);
    }





}
