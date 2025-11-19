package schrumbo.schlib.gui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import schrumbo.schlib.Schlib;
import schrumbo.schlib.gui.components.category.MainCategory;
import schrumbo.schlib.gui.components.category.SubCategory;
import schrumbo.schlib.gui.components.screenwidgets.Button;
import schrumbo.schlib.gui.components.screenwidgets.ScreenWidget;
import schrumbo.schlib.gui.components.screenwidgets.SearchBar;
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

    private final Text title;
    public Theme screenTheme;
    private List<MainCategory> categories = new ArrayList<>();
    private List<ScreenWidget> widgets = new ArrayList<>();
    private Screen hudEditorScreen;


    //Base panel dimensions
    private static final int TITLE_BAR_HEIGHT = 15;
    private static final int TITLE_BAR_X = 10;
    private static final int TITLE_BAR_Y = 10;
    private int TITLE_BAR_X2;
    private int TITLE_BAR_Y2;

    private int PANEL_X;
    private int PANEl_Y;
    private int PANEL_X2;
    private int PANEL_Y2;
    private int PANEL_WIDTH;
    private int PANEL_HEIGHT;

    private int SEARCH_BAR_X;
    private int SEARCH_BAR_Y;
    private int SEARCH_BAR_X2;
    private int SEARCH_BAR_Y2;
    private int SEARCH_BAR_WIDTH;
    private int SEARCH_BAR_HEIGHT;

    //categories area
    private int CATEGORIES_WIDTH;
    private int CATEGORIES_HEIGHT;
    private int CATEGORIES_X;
    private int CATEGORIES_X2;
    private int CATEGORIES_Y;
    private int CATEGORIES_Y2;

    private int CATEGORY_START_Y;
    private int CATEGORY_X;
    private int CATEGORY_WIDTH;




    private static final int PADDING  = 3;

    private TextRenderer textRenderer = mc.textRenderer;

    private Button closeGUI;
    private Button moveHudElements;
    private Button collapseCategories;
    private SearchBar searchBar;

    public SchlibScreen(Text title) {
        super(title);
        this.title = title;
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
        hudEditorScreen = builder.getHudEditorScreen();
        this.categories = new ArrayList<>(builder.getCategories());

        //set parent screen to pass the theme
        for (MainCategory category : categories) {
            category.parent = this;
            for (SubCategory subCategory : category.subCategories){
                subCategory.parent = this;
            }
        }

        checkForTheme();
    }

    @Override
    protected void init(){
        super.init();
        calculatePanel();
        checkForTheme();
        initScreenWidgets();
        positionCategories();
    }



    /**
     * initializes all screen widgets
     */
    private void initScreenWidgets(){
        int y = TITLE_BAR_Y + PADDING;
        int currentX = TITLE_BAR_X + PADDING;

        closeGUI = Button.builder()
                .parentScreen(this)
                .label("close")
                .position(currentX, y)
                .size(9, 9)
                .onClick(() -> mc.setScreen(null))
                .customRenderer((context, cx, cy, width, height) -> {
                    context.drawHorizontalLine(cx + 2, cx + width - 3, cy + 4, 0xFF000000);
                })
                .color(screenTheme.closeColor)
                .build();
        currentX += PADDING + 9;

        moveHudElements = Button.builder()
                .parentScreen(this)
                .label("move")
                .position(currentX, y)
                .size(9, 9)
                .onClick(() -> Schlib.LOGGER.error("implement hud editor screen"))
                .customRenderer((context, cx, cy, width, height) -> {
                    context.drawHorizontalLine(cx + 2, cx + 2 , cy + 3, 0xFF000000);
                    context.drawHorizontalLine(cx + 4, cx + 4, cy + 3, 0xFF000000);
                    context.drawHorizontalLine(cx + 6, cx + 6, cy + 3, 0xFF000000);

                    context.drawHorizontalLine(cx + 2, cx + 2, cy + 5, 0xFF000000);
                    context.drawHorizontalLine(cx + 4, cx + 4, cy + 5, 0xFF000000);
                    context.drawHorizontalLine(cx + 6, cx + 6, cy + 5, 0xFF000000);
                })
                .color(screenTheme.moveHudColor)
                .build();

        currentX += PADDING + 9;

        collapseCategories = Button.builder()
                .parentScreen(this)
                .label("collapse categories")
                .position(currentX, y)
                .size(9, 9)
                .onClick(() -> Schlib.LOGGER.error("implement categories feature"))
                .customRenderer((context, cx, cy, width, height) -> {
                    context.drawHorizontalLine(cx + 2, cx + width - 3, cy + 2, 0xFF000000);
                    context.drawHorizontalLine(cx + 2, cx + width - 3, cy + 4, 0xFF000000);
                    context.drawHorizontalLine(cx + 2, cx + width - 3, cy + 6, 0xFF000000);
                })
                .color(screenTheme.collapseCategoriesColor)
                .build();

        searchBar = SearchBar.builder()
                .parentScreen(this)
                .position(SEARCH_BAR_X, SEARCH_BAR_Y)
                .size(SEARCH_BAR_WIDTH, SEARCH_BAR_HEIGHT)
                .label("Search Bar")
                .build();



        widgets.clear();
        widgets.add(closeGUI);
        widgets.add(moveHudElements);
        widgets.add(collapseCategories);
        widgets.add(searchBar);
    }



    /**
     * renders all widgets
     * @param context
     * @param mouseX
     * @param mouseY
     */
    private void renderScreenWidgets(DrawContext context, double mouseX, double mouseY){
        for(ScreenWidget widget : widgets){
            widget.render(context, mouseX, mouseY);
        }
    }

    /**
     * renders the main categories
     * @param context
     * @param mouseX
     * @param mouseY
     */
    private void renderCategories(DrawContext context, double mouseX, double mouseY){
        for (var category : categories){
            category.render(context, mouseX, mouseY);
            positionCategories();
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
        renderScreenWidgets(context, mouseX, mouseY);
        renderCategories(context, mouseX, mouseY);
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
        for(ScreenWidget widget : widgets){
            if(widget.mouseClicked(click.x(), click.y(), click.button())){
                return true;
            }
        }
        for (MainCategory category : categories){
            if (category.mouseClicked(click)){
                return true;
            }
        }
        return false;
    }


    /**
     * renders panel Background
     */
    private void renderPanelBackground(DrawContext context){
        RenderUtils2D.drawFancyBox(context, PANEL_X, PANEl_Y, PANEL_X2, PANEL_Y2, screenTheme.baseBackgroundColor, screenTheme.darkBorderColor, screenTheme.lightBorderColor);
    }

    /**
     * renders title bar on top of the panel
     * @param context
     */
    private void renderTitleBar(DrawContext context){
        RenderUtils2D.drawFancyBox(context, TITLE_BAR_X, TITLE_BAR_Y, TITLE_BAR_X2, TITLE_BAR_Y2, screenTheme.componentBackgroundColor, screenTheme.darkBorderColor, screenTheme.lightBorderColor);
        renderTitle(context);
    }

    /**
     * renders the title inside the Title Bar
     * @param context
     */
    private void renderTitle(DrawContext context){
         int titleWidth = textRenderer.getWidth(title);
         int titleX = TITLE_BAR_X2 - PADDING - titleWidth;

         int titleHeight = textRenderer.fontHeight;
         int titleY = TITLE_BAR_Y + TITLE_BAR_HEIGHT / 2 - titleHeight / 2;
        context.drawText(textRenderer, title, titleX, titleY, screenTheme.textColor, true);
    }

    /**
     * calculates the panel dimensions
     */
    private void calculatePanel(){
        TITLE_BAR_X2 = mc.getWindow().getScaledWidth() - 10;
        TITLE_BAR_Y2 = TITLE_BAR_Y + TITLE_BAR_HEIGHT;

        PANEL_X = TITLE_BAR_X;
        PANEL_X2 = mc.getWindow().getScaledWidth() - 10;
        PANEl_Y = TITLE_BAR_Y2;
        PANEL_Y2 = mc.getWindow().getScaledHeight() - 10;

        PANEL_HEIGHT = PANEL_Y2 - PANEl_Y;
        PANEL_WIDTH = PANEL_X2 - PANEL_X;

        //Search bar dimensions
        SEARCH_BAR_WIDTH = PANEL_WIDTH / 4;
        SEARCH_BAR_X = ((PANEL_WIDTH / 8) * 3) + PANEL_X;
        SEARCH_BAR_X2 = SEARCH_BAR_X + SEARCH_BAR_WIDTH;
        SEARCH_BAR_Y = TITLE_BAR_Y + PADDING - 1;
        SEARCH_BAR_Y2 = TITLE_BAR_Y2 - PADDING + 1;
        SEARCH_BAR_HEIGHT = SEARCH_BAR_Y2 - SEARCH_BAR_Y;

        //category area dimensions;
        CATEGORIES_X = PANEL_X + PADDING;
        CATEGORIES_WIDTH = PANEL_WIDTH / 4 - 2 * PADDING;
        CATEGORIES_X2 = CATEGORIES_X + CATEGORIES_WIDTH;
        CATEGORIES_Y = PANEl_Y + PADDING;
        CATEGORIES_HEIGHT = PANEL_HEIGHT - 2 * PADDING;
        CATEGORIES_Y2 = CATEGORIES_Y + CATEGORIES_HEIGHT;

        //category widget positions
        CATEGORY_START_Y = CATEGORIES_Y + PADDING;
        CATEGORY_X = CATEGORIES_X + PADDING;
        CATEGORY_WIDTH = (CATEGORIES_X2 - PADDING) - (CATEGORIES_X + PADDING);
    }

    /**
     * sets the position of all categories (and their children)
     */
    private void positionCategories() {
        int currentY = CATEGORY_START_Y;
        for (MainCategory category : categories) {
            category.setPosition(CATEGORY_X, currentY);
            category.setWidth(CATEGORY_WIDTH);
            currentY += category.getTotalHeight();
        }
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

    @Override
    public boolean keyPressed(KeyInput input) {
        if (searchBar != null && searchBar.keyPressed(input)) {
            return true;
        }
        return super.keyPressed(input);
    }

    /**
     * builder design for the screen for easy adding of categories
     */
    public static class SchlibScreenBuilder{
        private Text title;
        private Theme customTheme;
        private List<MainCategory> categories = new ArrayList<>();
        private Screen hudEditorScreen;

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

        public SchlibScreenBuilder hudEditorScreen(Screen screen){
            this.hudEditorScreen = screen;
            return this;
        }

        /**
         * adds a category to the screen
         * @param category
         * @return
         */
        public SchlibScreenBuilder addCategory(MainCategory category){
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

        Screen getHudEditorScreen(){
            return hudEditorScreen;
        }

        /**
         * gets all categories of a screen
         * @return
         */
        List<MainCategory> getCategories(){
            return categories;
        }
    }

    /**
     * gets the theme for the screen, used for widgets
     * @return
     */
    public Theme getTheme() {
        return screenTheme;
    }
}