package schrumbo.schlib.gui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;
import schrumbo.schlib.Schlib;
import schrumbo.schlib.gui.components.Category;
import schrumbo.schlib.gui.components.screenwidgets.Button;
import schrumbo.schlib.gui.components.screenwidgets.ScreenWidget;
import schrumbo.schlib.gui.theme.Theme;
import schrumbo.schlib.utils.RenderUtils2D;

import java.util.ArrayList;
import java.util.List;

import static schrumbo.schlib.SchlibClient.mc;

/**
 * Main ClickGUI screen for configuration.
 * Handles rendering input and category management
 */
public class SchlibScreen extends Screen {

    private static Text title;
    public static Theme screenTheme;
    private static List<Category> categories = new ArrayList<>();
    private static List<ScreenWidget> widgets = new ArrayList<>();

    //Base panel dimensions
    private static final int TITLE_BAR_HEIGHT = 15;
    private static final int TITLE_BAR_X = 10;
    private static final int TITLE_BAR_Y = 10;
    private static int TITLE_BAR_X2;
    private static int TITLE_BAR_Y2;

    private static int PANEL_X;
    private static int PANEl_Y;
    private static int PANEL_X2;
    private static int PANEL_Y2;
    private static int PANEL_WIDTH;
    private static int PANEL_HEIGHT;

    private static final int PADDING  = 3;

    private static TextRenderer textRenderer = mc.textRenderer;

    public SchlibScreen(Text title) {
        super(title);
        SchlibScreen.title = title;
    }

    /**
     * needed for the builder design, sets all the different values
     * @param builder
     */
    void configure(SchlibScreenBuilder builder){
        if (builder.getCustomTheme() != null){
            screenTheme = builder.getCustomTheme();
        }
        categories = builder.getCategories();
    }

    @Override
    protected void init(){
        super.init();
        calculatePanel();
        initializeWidgets();

        checkForTheme();
    }

    /**
     * initializes Buttons etc
     */
    private void initializeWidgets(){
        Button close = Button.builder()
                .label("close")
                .position(TITLE_BAR_X + PADDING, TITLE_BAR_Y + PADDING)
                .size(9, 9)
                .onClick(() -> mc.setScreen(null))
                .build();
        widgets.add(close);
    }

    /**
     * renders all widgets
     * @param context
     * @param mouseX
     * @param mouseY
     */
    private static void renderWidgets(DrawContext context, double mouseX, double mouseY){
        for(ScreenWidget widget : widgets){
            widget.render(context, mouseX, mouseY);
        }
    }

    /**
     * overwrites render method from superclass, needed to draw stuff on the screen
     * @param context
     * @param mouseX
     * @param mouseY
     * @param deltaTicks
     */
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        renderPanelBackground(context);
        renderTitleBar(context);
        renderWidgets(context, mouseX, mouseY);
    }

    /**
     * overwrites renderBackground Method to only apply the blur
     * @param context
     * @param mouseX
     * @param mouseY
     * @param deltaTicks
     */
    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        this.applyBlur(context);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        for (ScreenWidget widget : widgets){
            widget.mouseClicked(click.x(), click.y(), click.button());
        }
        return true;
    }


    /**
     * renders panel Background
     */
    private static void renderPanelBackground(DrawContext context){
        RenderUtils2D.drawFancyBox(context, PANEL_X, PANEl_Y, PANEL_X2, PANEL_Y2, screenTheme.baseBackgroundColor, screenTheme.darkBorderColor, screenTheme.lightBorderColor);
    }

    /**
     * renders title bar on top of the panel
     * @param context
     */
    private static void renderTitleBar(DrawContext context){
        RenderUtils2D.drawFancyBox(context, TITLE_BAR_X, TITLE_BAR_Y, TITLE_BAR_X2, TITLE_BAR_Y2, screenTheme.componentBackgroundColor, screenTheme.darkBorderColor, screenTheme.lightBorderColor);
        renderTitle(context);
    }

    /**
     * renders the title inside the Title Bar
     * @param context
     */
    private static void renderTitle(DrawContext context){
         int titleWidth = textRenderer.getWidth(title);
         int titleX = TITLE_BAR_X2 - PADDING - titleWidth;

         int titleHeight = textRenderer.fontHeight;
         int titleY = TITLE_BAR_Y + TITLE_BAR_HEIGHT / 2 - titleHeight / 2;
        context.drawText(textRenderer, title, titleX, titleY, screenTheme.textColor, true);
    }

    /**
     * calculates the panel dimensions
     */
    private static void calculatePanel(){
        TITLE_BAR_X2 = mc.getWindow().getScaledWidth() - 10;
        TITLE_BAR_Y2 = TITLE_BAR_Y + TITLE_BAR_HEIGHT;

        PANEL_X = TITLE_BAR_X;
        PANEL_X2 = mc.getWindow().getScaledWidth() - 10;
        PANEl_Y = TITLE_BAR_Y2;
        PANEL_Y2 = mc.getWindow().getScaledHeight() - 10;

        PANEL_HEIGHT = PANEL_Y2 - PANEl_Y;
        PANEL_WIDTH = PANEL_X2 - PANEL_X;
    }

    /**
     * checks if a theme is applied, if not throws an error
     */
    private void checkForTheme(){
        if (screenTheme == null){
            throw new IllegalStateException("please set a theme for the screen");
        }
    }

    /**
     * stops the game from pausing in singleplayer when in screen
     * @return
     */
    @Override
    public boolean shouldPause(){
        return false;
    }

    /**
     * builder design for the screen for easy adding of categories
     */
    public static class SchlibScreenBuilder{
        private Text title;
        private Theme customTheme;
        private List<Category> categories = new ArrayList<>();

        private SchlibScreenBuilder(Text title){
            this.title = title;
        }

        /**
         * creates new Screen with a title
         * @param title
         * @return
         */
        public static SchlibScreenBuilder create(Text title){
            return new SchlibScreenBuilder(title);
        }

        /**
         * adds a theme to the screen
         * @param theme
         * @return
         */
        public SchlibScreenBuilder withTheme(Theme theme){
            this.customTheme = theme;
            return this;
        }

        /**
         * adds a category to the screen
         * @param category
         * @return
         */
        public SchlibScreenBuilder addCategory(Category category){
            categories.add(category);
            return this;
        }

        /**
         * builds the screen;
         * @return
         */
        public SchlibScreen build(){
            SchlibScreen screen = new SchlibScreen(title);
            screen.configure(this);
            return screen;
        }

        /**
         * gets the custom theme of the screen
         * @return
         */
        Theme getCustomTheme(){
            return customTheme;
        }

        /**
         * gets all categories of a screen
         * @return
         */
        List<Category> getCategories(){
            return categories;
        }


    }
}