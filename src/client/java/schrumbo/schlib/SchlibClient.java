package schrumbo.schlib;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import schrumbo.schlib.gui.theme.Theme;
import schrumbo.schlib.testing.GuiKeybind;

public class SchlibClient implements ClientModInitializer {

	public static final MinecraftClient mc = MinecraftClient.getInstance();
	public static final Theme theme = new Theme();

	@Override
	public void onInitializeClient() {
		GuiKeybind.register();
	}

}
