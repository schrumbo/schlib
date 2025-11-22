package schrumbo.schlib.testing;

import net.minecraft.text.Text;
import schrumbo.schlib.SchlibClient;
import schrumbo.schlib.gui.SchlibScreen;
import schrumbo.schlib.gui.components.category.MainCategory;
import schrumbo.schlib.gui.components.category.SubCategory;
import schrumbo.schlib.gui.components.widget.Group;
import schrumbo.schlib.gui.components.widget.Slider;
import schrumbo.schlib.gui.components.widget.Switch;

public class TestCategory {


    public static  SchlibScreen createScreen() {
        Slider floatSlider = Slider.builder()
                .range(10, 100)
                .value(TestCategory::getFloatValue, TestCategory::setFloatValue)
                .label("SLIDER")
                .description("Schere Diggi")
                .build();

        Slider groupSlider = Slider.builder()
                .range(10, 100)
                .value(TestCategory::getFloatValue, TestCategory::setFloatValue)
                .label("Hello Slider")
                .build();

        Switch groupSwitch = Switch.builder()
                .label("Hello World")
                .value(() -> testEnabled,
                        (newValue) -> testEnabled = newValue)
                .build();

        Group testGroup = Group.builder()
                .label("Hello Group")
                .addWidget(groupSwitch)
                .addWidget(groupSlider)
                .build();

        Switch testSwitch = Switch.builder()
                .label("Berlin")
                .value(() -> testEnabled,
                        (newValue) -> testEnabled = newValue
                )
                .build();

        Switch testSwitch2 = Switch.builder()
                .label("Fokushima")
                .value(() -> testEnabled,
                        (newValue) -> testEnabled = newValue
                )
                .build();

        Switch testSwitch3 = Switch.builder()
                .label("Hiroshima")
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
                .addWidget(testSwitch3)
                .build();

        SubCategory gameplay = SubCategory.builder()
                .label("Gameplay")
                .addWidget(testSwitch)
                .build();

        SubCategory fartplay = SubCategory.builder()
                .label("fart")
                .addWidget(testSwitch2)
                .build();

        MainCategory generalCategory = MainCategory.builder()
                .label("General")
                .addSubCategory(rendering)
                .addSubCategory(gameplay)
                .addWidget(testGroup)
                .addWidget(furzSwitch)
                .addWidget(floatSlider)
                .build();

        MainCategory hudCategory = MainCategory.builder()
                .label("HUD")
                .addSubCategory(fartplay)
                .addWidget(testSwitch3)
                .build();

        return SchlibScreen.SchlibScreenBuilder
                .create(Text.literal("moin"))
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

    private static float floatValue = 1.0f;
    private static float getFloatValue(){
        return floatValue;
    }
    private static void setFloatValue(float value){
        floatValue = value;
    }
}
