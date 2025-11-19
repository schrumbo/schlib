package schrumbo.schlib.gui.components.screenwidgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.KeyInput;
import org.lwjgl.glfw.GLFW;
import schrumbo.schlib.gui.theme.Theme;
import schrumbo.schlib.utils.RenderUtils2D;

import static schrumbo.schlib.SchlibClient.mc;

public class SearchBar extends ScreenWidget{

    private String text = "";
    private boolean focused = false;
    private int cursorPos = 0;
    private long lastBlink = 0;
    private boolean cursorVisible = true;

    private final int PADDING = 3;

    private int selectionStart = -1;
    private int selectionEnd = -1;


    private static final int CURSOR_BLINK_SPEED = 500;

    private SearchBar(Builder builder) {
        super(builder);
    }


    @Override
    public void render(DrawContext context, double mouseX, double mouseY) {
        Theme screenTheme = parent.getTheme();


        int mainColor = isHovered(mouseX, mouseY) ? screenTheme.componentBackgroundColor : screenTheme.baseBackgroundColor;
        RenderUtils2D.drawFancyBox(context, x, y, x + width, y + height, mainColor, screenTheme.darkBorderColor, screenTheme.lightBorderColor);

        int textColor = screenTheme.textColor;
        String display = text.isEmpty() && !focused ? "Search..." : text;

        int textX = x + PADDING;
        int textY = y + height / 2 - mc.textRenderer.fontHeight / 2 + 1;

        context.enableScissor(x, y, x + width, y + height);
        context.drawText(mc.textRenderer, display, textX, textY, textColor, false);
        if (hasSelection()) {
            renderSelection(context);
        }
        if (focused && cursorVisible){
            int cursorX = textX + mc.textRenderer.getWidth(text.substring(0, Math.min(cursorPos, text.length())));
            context.fill(cursorX, textY, cursorX + 1, textY + mc.textRenderer.fontHeight - 2, screenTheme.textColor);
        }
        context.disableScissor();

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBlink > CURSOR_BLINK_SPEED) {
            cursorVisible = !cursorVisible;
            lastBlink = currentTime;
        }
    }

    /**
     * renders the users text selection
     * @param context
     */
    private void renderSelection(DrawContext context) {
        int start = Math.min(selectionStart, selectionEnd);
        int end = Math.max(selectionStart, selectionEnd);

        String beforeSelection = text.substring(0, start);
        String selectedText = text.substring(start, end);

        int selectionX = x + PADDING + mc.textRenderer.getWidth(beforeSelection);
        int selectionWidth = mc.textRenderer.getWidth(selectedText);
        context.fill(
                selectionX,
                y + height / 2 - mc.textRenderer.fontHeight / 2 + 1,
                selectionX + selectionWidth,
                y + height / 2 - mc.textRenderer.fontHeight / 2 + 1 + mc.textRenderer.fontHeight - 2,
                0x801e90ff
        );
    }

    /**
     * handles mouse clicks
     * @param mouseX
     * @param mouseY
     * @param button
     * @return
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isHovered(mouseX, mouseY)) {
            focused = false;
            return false;
        }
        if (!focused){
            focused = true;
            cursorPos = text.length();
        }else {
            int relativeX = (int) (mouseX - x - PADDING);
            cursorPos = getClickedPosition(relativeX);
        }
        resetCursor();
        return false;
    }

    /**
     * clears the search content
     */
    public void clearSearch(){
        text = "";
    }

    /**
     * gets the position at which the user clicked onto the text
     * @param clickX
     * @return
     */
    private int getClickedPosition(int clickX) {
        if (clickX <= 0) return 0;

        for (int i = 0; i <= text.length(); i++) {
            int width = mc.textRenderer.getWidth(text.substring(0, i));
            if (clickX < width) {
                return Math.max(0, i - 1);
            }
        }
        return text.length();
    }


    public boolean keyPressed(KeyInput input) {
        if (!focused) return false;

        if (input.isEnterOrSpace()){
            insertText(" ");
            return true;
        }

        if (input.isPaste()) {
            insertText(mc.keyboard.getClipboard());
            return true;
        }

        if (input.isCopy()) {
            if (hasSelection()) {
                mc.keyboard.setClipboard(getSelectedText());
            }
            return true;
        }

        if (input.isCut()) {
            if (hasSelection()) {
                mc.keyboard.setClipboard(getSelectedText());
                deleteSelection();
            }
            return true;
        }

        if (input.isSelectAll()) {
            selectionStart = 0;
            selectionEnd = text.length();
            cursorPos = text.length();
            return true;
        }

        if (input.isLeft()){
            if (cursorPos > 0) {
                cursorPos--;
                resetCursor();
            }
            return true;
        }

        if (input.isRight()){
            if (cursorPos < text.length()) {
                cursorPos++;
                resetCursor();
            }
            return true;
        }


        int keyCode = input.getKeycode();

        switch (keyCode) {
            //back
            case 259:
                if (hasSelection()) {
                    deleteSelection();
                } else if (cursorPos > 0) {
                    text = text.substring(0, cursorPos - 1) + text.substring(cursorPos);
                    cursorPos--;
                }
                resetCursor();
                return true;
            //del
            case 261:
                if (hasSelection()) {
                    deleteSelection();
                } else if (cursorPos < text.length()) {
                    text = text.substring(0, cursorPos) + text.substring(cursorPos + 1);
                }
                resetCursor();
                return true;
            //home
            case 268:
                clearSelection();
                cursorPos = 0;
                resetCursor();
                return true;

            case 269:
                clearSelection();
                cursorPos = text.length();
                resetCursor();
                return true;

            case 257:
            case 335:
                return true;

            default:
                String keyName = getKeyName(input);
                if (keyName!= null){
                    insertText(keyName);
                    return true;
                }
                return false;
        }
    }

    /**
     * gets a keys name/char value
     * @param input
     * @return
     */
    private String getKeyName(KeyInput input){
        String keyName = GLFW.glfwGetKeyName(input.key(), 0);
        if (keyName != null && keyName.length() == 1){
            return keyName;
        }
        return null;
    }

    /**
     * checks if there is currently text selected in the search field
     * @return
     */
    private boolean hasSelection() {
        return selectionStart != -1 && selectionEnd != -1 && selectionStart != selectionEnd;
    }

    /**
     * gets the text selected by the user
     * @return
     */
    private String getSelectedText() {
        if (!hasSelection()) return "";
        int start = Math.min(selectionStart, selectionEnd);
        int end = Math.max(selectionStart, selectionEnd);
        return text.substring(start, end);
    }

    /**
     * deletes the selected text in the search field
     */
    private void deleteSelection() {
        if (!hasSelection()) return;
        int start = Math.min(selectionStart, selectionEnd);
        int end = Math.max(selectionStart, selectionEnd);
        text = text.substring(0, start) + text.substring(end);
        cursorPos = start;
        clearSelection();
    }

    /**
     * clears selection
     */
    private void clearSelection() {
        selectionStart = -1;
        selectionEnd = -1;
    }

    /**
     * inserts text to the text search field
     * @param insertedText
     */
    private void insertText(String insertedText) {
        text = text.substring(0, cursorPos) + insertedText + text.substring(cursorPos);
        cursorPos += insertedText.length();

        cursorVisible = true;
        lastBlink = System.currentTimeMillis();
    }



    /**
     * resets cursor
     */
    private void resetCursor() {
        cursorVisible = true;
        lastBlink = System.currentTimeMillis();
    }

    /**
     * gets the text inside the field
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * sets text inside the field to smth
     * @param text
     */
    public void setText(String text) {
        this.text = text;
        cursorPos = Math.min(cursorPos, text.length());
    }

    public static class Builder extends ScreenWidget.Builder<Builder> {

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public SearchBar build() {
            return new SearchBar(this);
        }
    }
    public static Builder builder() {
        return new Builder();
    }

}
