package schrumbo.schlib.testing;

import net.minecraft.client.option.KeyBinding;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import schrumbo.schlib.SchlibClient;
import schrumbo.schlib.gui.SchlibScreen;


public class GuiKeybind {
    private static KeyBinding configKey;
    private static final KeyBinding.Category CATEGORY = KeyBinding.Category.create(Identifier.of("schlib", "main"));

    public static void register(){
        configKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                        "Open Test Screen",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_CONTROL,
                CATEGORY
                )
        );
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if(configKey.wasPressed()){
                SchlibScreen screen = SchlibScreen.SchlibScreenBuilder
                        .create(Text.literal("SCHLIB"))
                        .withTheme(SchlibClient.theme)
                        .build();

                client.setScreen(screen);
            }
        });
    }


}

