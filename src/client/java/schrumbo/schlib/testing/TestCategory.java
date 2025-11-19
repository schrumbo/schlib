package schrumbo.schlib.testing;

import net.minecraft.text.Text;
import schrumbo.schlib.SchlibClient;
import schrumbo.schlib.gui.SchlibScreen;
import schrumbo.schlib.gui.components.category.MainCategory;
import schrumbo.schlib.gui.components.category.SubCategory;
import schrumbo.schlib.gui.components.widget.Group;
import schrumbo.schlib.gui.components.widget.Switch;

public class TestCategory {


    public static  SchlibScreen createScreen() {
        Switch groupSwitch = Switch.builder()
                .label("Hello World")
                .value(() -> testEnabled,
                        (newValue) -> testEnabled = newValue)
                .build();

        Group testGroup = Group.builder()
                .label("Hello Group")
                .addWidget(groupSwitch)
                .build();


        Switch testSwitch = Switch.builder()
                .label("Enable Test Feature")
                .value(() -> testEnabled,
                        (newValue) -> testEnabled = newValue
                )
                .build();

        Switch furzSwitch = Switch.builder()
                .label("forz")
                .value(() -> testEnabled,
                        (newValue) -> testEnabled = newValue
                )
                .build();

        SubCategory rendering = SubCategory.builder()
                .label("Rendering")
                .addWidget(testSwitch)
                .build();

        SubCategory gameplay = SubCategory.builder()
                .label("Gameplay")
                .build();

        SubCategory fartplay = SubCategory.builder()
                .label("fart")
                .addWidget(testSwitch)
                .build();



        MainCategory generalCategory = MainCategory.builder()
                .label("General")
                .addSubCategory(rendering)
                .addSubCategory(gameplay)
                .addWidget(testGroup)
                .addWidget(testSwitch)
                .addWidget(furzSwitch)
                .build();


        MainCategory hudCategory = MainCategory.builder()
                .label("HUD")
                .addSubCategory(fartplay)
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
