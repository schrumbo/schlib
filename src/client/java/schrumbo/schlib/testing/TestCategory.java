package schrumbo.schlib.testing;

import net.minecraft.text.Text;
import schrumbo.schlib.SchlibClient;
import schrumbo.schlib.gui.SchlibScreen;
import schrumbo.schlib.gui.components.category.MainCategory;
import schrumbo.schlib.gui.components.category.SubCategory;
import schrumbo.schlib.gui.components.widget.Switch;

public class TestCategory {


    public static SchlibScreen createScreen() {
        SubCategory rendering = SubCategory.builder()
                .label("Rendering")
                .build();

        SubCategory gameplay = SubCategory.builder()
                .label("Gameplay")
                .build();

        Switch testSwitch = Switch.builder()
                .label("Enable Test Feature")
                .value(() -> testEnabled,
                    (newValue) -> testEnabled = newValue
                )
                .build();

        MainCategory generalCategory = MainCategory.builder()
                .label("General")
                .addSubCategory(rendering)
                .addSubCategory(gameplay)
                .addWidget(testSwitch)
                .build();


        MainCategory hudCategory = MainCategory.builder()
                .label("HUD")
                .build();

        return SchlibScreen.SchlibScreenBuilder
                .create(Text.literal("schLib by schrumbo"))
                .withTheme(SchlibClient.theme)
                .addCategory(generalCategory)
                .addCategory(hudCategory)
                .build();
    }



    private static boolean testEnabled = false;
    // Getter für andere Klassen
    public static boolean isTestEnabled() {
        return testEnabled;
    }
}
