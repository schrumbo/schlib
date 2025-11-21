package schrumbo.schlib.gui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import schrumbo.schlib.Schlib;
import schrumbo.schlib.gui.components.category.Category;
import schrumbo.schlib.gui.components.category.MainCategory;
import schrumbo.schlib.gui.components.category.SubCategory;
import schrumbo.schlib.gui.components.screenwidgets.Button;
import schrumbo.schlib.gui.components.screenwidgets.ScreenWidget;
import schrumbo.schlib.gui.components.screenwidgets.SearchBar;
import schrumbo.schlib.gui.components.widget.Group;
import schrumbo.schlib.gui.components.widget.Widget;
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

    private Category selectedCategory;


    //Base panel dimensions
    private static final int TITLE_BAR_HEIGHT = 15;
    private static final int TITLE_BAR_X = 10;
    private static final int TITLE_BAR_Y = 10;
    private int TITLE_BAR_X2;
    private int TITLE_BAR_Y2;

    private int PANEL_X;
    private int PANEL_Y;
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
    private int CATEGORIES_X;
    private int CATEGORIES_X2;

    //content y - same for all
    private int CONTENT_Y;
    private int CONTENT_Y2;
    private int CONTENT_HEIGHT;

    //CATEGORY POSITION
    private int CATEGORY_START_Y;
    private int CATEGORY_X;
    private int CATEGORY_WIDTH;

    //WIDGET AREA
    private int WIDGETS_WIDTH;
    private int WIDGETS_HEIGHT;
    private int WIDGETS_X;
    private int WIDGETS_X2;

    //WIDGET POSITION
    private int WIDGET_START_Y;
    private int WIDGET_X;
    private int WIDGET_WIDTH;

    //MISC AREA
    private int MISC_X;
    private int MISC_X2;
    private int MISC_WIDTH;





    private static final int PADDING  = 3;

    private TextRenderer textRenderer = mc.textRenderer;

    private Button closeGUI;
    private Button moveHudElements;
    private Button collapseCategories;
    public SearchBar searchBar;

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
            category.setParentScreen(this);
        }

        checkForTheme();
    }

    @Override
    protected void init(){
        super.init();
        calculatePanel();
        checkForTheme();
        initScreenWidgets();
        selectedCategory = categories.getFirst();
        positionCategories();
        positionWidgets();

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
                .color(screenTheme.systemRed)
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
                .color(screenTheme.systemTeal)
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
                .color(screenTheme.systemGray)
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
        context.fill(CATEGORIES_X, CONTENT_Y, CATEGORIES_X2, CONTENT_Y2, screenTheme.gridColor);
        for (var category : categories){
            category.render(context, mouseX, mouseY);
            positionCategories();
        }
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
     * renders the widgets of the currently selected category
     * @param context
     * @param mouseX
     * @param mouseY
     */
    private void renderWidgets(DrawContext context, double mouseX, double mouseY){
        context.fill(WIDGETS_X, CONTENT_Y, WIDGETS_X2, CONTENT_Y2, screenTheme.gridColor);
        if (selectedCategory.getWidgets() == null)return;
        if (selectedCategory.getWidgets().isEmpty())return;
        if (getFilteredWidgets().isEmpty())return;
        for (var widget : getFilteredWidgets()){
            widget.render(context, mouseX, mouseY);
            positionWidgets();
        }
    }

    /**
     * positions the widgets correctly
     */
    private void positionWidgets(){
        int currentY = WIDGET_START_Y;
        for (var widget : getFilteredWidgets()){
            widget.setPosition(WIDGET_X, currentY);
            widget.setWidth(WIDGET_WIDTH);
            currentY += widget.getHeight() + PADDING;
        }
    }

    /**
     * gets a list of all widgets in the currently selected category which are matching the search results
     * @return list of all matching widgets
     */
    private List<Widget> getFilteredWidgets(){
        List<Widget> filtered = new ArrayList<>();
        String searchText = searchBar.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            return selectedCategory.getWidgets();
        }

        Category bestMatch = findBestMatchingCategory(searchText);
        if (bestMatch != null && bestMatch != selectedCategory) {
            selectedCategory = bestMatch;
        }

        for (var widget : selectedCategory.getWidgets()){
            boolean shouldAdd = false;

            if (widget.getLabel().toLowerCase().contains(searchText)){
                shouldAdd = true;
            }

            if (widget instanceof Group group){
                for (var groupWidget : group.getChildren()){
                    if (groupWidget.getLabel().toLowerCase().contains(searchText)){
                        shouldAdd = true;
                        group.setExpanded(true);
                        break;
                    }
                }
            }

            if (shouldAdd) {
                filtered.add(widget);
            }
        }
        return filtered;
    }

    /**
     * finds a best match based on the search
     * @param searchText
     * @return best match or null
     */
    private Category findBestMatchingCategory(String searchText) {
        Category bestMatch = null;

        for (MainCategory mainCategory : categories) {
            if (mainCategory.getLabel().toLowerCase().contains(searchText)) {
                if (bestMatch == null) {
                    bestMatch = mainCategory;
                }
            }

            for (Widget widget : mainCategory.getWidgets()) {
                if (widget.getLabel().toLowerCase().contains(searchText)) {
                    return mainCategory;
                }

                if (widget instanceof Group group) {
                    for (Widget child : group.getChildren()) {
                        if (child.getLabel().toLowerCase().contains(searchText)) {
                            return mainCategory;
                        }
                    }
                }
            }

            for (SubCategory subCategory : mainCategory.getSubCategories()) {
                if (subCategory.getLabel().toLowerCase().contains(searchText)) {
                    if (bestMatch == null) {
                        bestMatch = subCategory;
                    }
                }

                for (Widget widget : subCategory.getWidgets()) {
                    if (widget.getLabel().toLowerCase().contains(searchText)) {
                        return subCategory;
                    }

                    if (widget instanceof Group group) {
                        for (Widget child : group.getChildren()) {
                            if (child.getLabel().toLowerCase().contains(searchText)) {
                                return subCategory;
                            }
                        }
                    }
                }
            }
        }

        return bestMatch;
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
        renderWidgets(context, mouseX, mouseY);
        renderMisc(context);
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

    /**
     * handles mouse clicks
     * @param click
     * @param doubled
     * @return
     */
    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        for(ScreenWidget widget : widgets){
            if(widget.mouseClicked(click.x(), click.y(), click.button())){
                return true;
            }
        }
        for (var category : categories){
            if (category.mouseClicked(click))return true;
            if (category.isExpanded()){
                for (var childCategory : category.getSubCategories()){
                    if (childCategory.mouseClicked(click))return true;
                }
            }
        }
        for (var widget : getFilteredWidgets()){
            if (widget.mouseClicked(click)){
                return true;
            }
        }
        return false;
    }

    //TODO
    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY){
        return false;
    }



    /**
     * renders panel Background
     */
    private void renderPanelBackground(DrawContext context){
        RenderUtils2D.drawBoxWithShadow(context, PANEL_X, PANEL_Y, PANEL_X2, PANEL_Y2, screenTheme.windowBackgroundColor, screenTheme.shadowColor, screenTheme.controlBackgroundColor);
    }

    /**
     * renders title bar on top of the panel
     * @param context
     */
    private void renderTitleBar(DrawContext context){
        RenderUtils2D.drawBoxWithShadow(context, TITLE_BAR_X, TITLE_BAR_Y, TITLE_BAR_X2, TITLE_BAR_Y2, screenTheme.gridColor, screenTheme.shadowColor, screenTheme.controlBackgroundColor);
        renderTitle(context);
    }

    private void renderMisc(DrawContext context){
        context.fill(MISC_X, CONTENT_Y, MISC_X2, CONTENT_Y2, screenTheme.gridColor);
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
     * TODO split up in different method for better readability
     */
    private void calculatePanel(){
        TITLE_BAR_X2 = mc.getWindow().getScaledWidth() - 10;
        TITLE_BAR_Y2 = TITLE_BAR_Y + TITLE_BAR_HEIGHT;

        //'canvas' size
        PANEL_X = TITLE_BAR_X;
        PANEL_X2 = mc.getWindow().getScaledWidth() - 10;
        PANEL_Y = TITLE_BAR_Y2;
        PANEL_Y2 = mc.getWindow().getScaledHeight() - 10;

        PANEL_HEIGHT = PANEL_Y2 - PANEL_Y;
        PANEL_WIDTH = PANEL_X2 - PANEL_X;

        //Search bar dimensions
        SEARCH_BAR_WIDTH = PANEL_WIDTH / 4;
        SEARCH_BAR_X = ((PANEL_WIDTH / 8) * 3) + PANEL_X;
        SEARCH_BAR_X2 = SEARCH_BAR_X + SEARCH_BAR_WIDTH;
        SEARCH_BAR_Y = TITLE_BAR_Y + PADDING - 1;
        SEARCH_BAR_Y2 = TITLE_BAR_Y2 - PADDING + 1;
        SEARCH_BAR_HEIGHT = SEARCH_BAR_Y2 - SEARCH_BAR_Y;

        //content height and y
        CONTENT_Y = PANEL_Y + PADDING;
        CONTENT_HEIGHT = PANEL_HEIGHT - 2 * PADDING;
        CONTENT_Y2 = CONTENT_Y + CONTENT_HEIGHT;

        //category area dimensions
        CATEGORIES_X = PANEL_X + PADDING;
        CATEGORIES_WIDTH = PANEL_WIDTH / 4 - 2 * PADDING;
        CATEGORIES_X2 = CATEGORIES_X + CATEGORIES_WIDTH;

        //category widget positions
        CATEGORY_START_Y = CONTENT_Y + PADDING;
        CATEGORY_X = CATEGORIES_X + PADDING;
        CATEGORY_WIDTH = (CATEGORIES_X2 - PADDING) - (CATEGORIES_X + PADDING);

        //widget area dimensions
        WIDGETS_X = CATEGORIES_X2 + PADDING;
        WIDGETS_WIDTH = PANEL_WIDTH / 2 - PADDING;
        WIDGETS_X2 = WIDGETS_X + WIDGETS_WIDTH;

        //widget positions
        WIDGET_START_Y = CONTENT_Y + PADDING;
        WIDGET_X = WIDGETS_X + PADDING;
        WIDGET_WIDTH = (WIDGETS_X2 - PADDING) - (WIDGETS_X + PADDING);

        //misc area
        MISC_X2 = PANEL_X2 - PADDING;
        MISC_WIDTH = PANEL_WIDTH / 4 - PADDING;
        MISC_X = MISC_X2 - MISC_WIDTH;

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

    /**
     * sets the currently selected main category
     * @param category
     */
    public void setSelectedCategroy(Category category){
        selectedCategory = category;
    }


    /**
     * gets the screens selected category
     * currently only used for debugging
     * @return category which is selected
     */
    public Category selectedCategory(){
        return selectedCategory;
    }
}