package lunarians.ui;

import arc.scene.Element;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import lunarians.LunariansI18n;
import lunarians.LunariansMod;
import lunarians.LunariansSettings;

public final class SchematicSettingsUi {
  public static void build(Table paramTable) {
    String str = LunariansI18n.get("schem_ui_show");
    boolean bool = LunariansSettings.showSchematicPanel;
    SchematicSettingsUi$1 schematicSettingsUi$1 = new SchematicSettingsUi$1();
    this();
    UiText.check(paramTable, str, bool, schematicSettingsUi$1).left().row();
    str = LunariansI18n.get("panel_size");
    UiText.section(paramTable, str);
    str = LunariansI18n.get("width");
    float f1 = LunariansSettings.panelWidth;
    SchematicSettingsUi$2 schematicSettingsUi$2 = new SchematicSettingsUi$2();
    this();
    SchematicSettingsUi$3 schematicSettingsUi$3 = new SchematicSettingsUi$3();
    this();
    float f2 = 10.0F;
    UiText.slider(paramTable, str, 160.0F, 700.0F, f2, f1, schematicSettingsUi$2, schematicSettingsUi$3);
    str = LunariansI18n.get("height");
    f1 = LunariansSettings.panelHeight;
    SchematicSettingsUi$4 schematicSettingsUi$4 = new SchematicSettingsUi$4();
    this();
    SchematicSettingsUi$5 schematicSettingsUi$5 = new SchematicSettingsUi$5();
    this();
    UiText.slider(paramTable, str, 120.0F, 600.0F, f2, f1, schematicSettingsUi$4, schematicSettingsUi$5);
    str = LunariansI18n.get("opacity");
    f1 = LunariansSettings.opacity;
    SchematicSettingsUi$6 schematicSettingsUi$6 = new SchematicSettingsUi$6();
    this();
    SchematicSettingsUi$7 schematicSettingsUi$7 = new SchematicSettingsUi$7();
    this();
    UiText.slider(paramTable, str, 0.3F, 1.0F, 0.05F, f1, schematicSettingsUi$6, schematicSettingsUi$7);
    str = LunariansI18n.get("show_title");
    bool = LunariansSettings.showTitle;
    SchematicSettingsUi$8 schematicSettingsUi$8 = new SchematicSettingsUi$8();
    this();
    UiText.check(paramTable, str, bool, schematicSettingsUi$8).left().row();
    str = LunariansI18n.get("slot_count");
    UiText.section(paramTable, str);
    str = LunariansI18n.get("slots");
    f1 = LunariansSettings.slotCount;
    SchematicSettingsUi$9 schematicSettingsUi$9 = new SchematicSettingsUi$9();
    this();
    SchematicSettingsUi$10 schematicSettingsUi$10 = new SchematicSettingsUi$10();
    this();
    UiText.slider(paramTable, str, 1.0F, 300.0F, 1.0F, f1, schematicSettingsUi$9, schematicSettingsUi$10);
    str = LunariansI18n.get("slot_size");
    f1 = LunariansSettings.slotSize;
    SchematicSettingsUi$11 schematicSettingsUi$11 = new SchematicSettingsUi$11();
    this();
    SchematicSettingsUi$12 schematicSettingsUi$12 = new SchematicSettingsUi$12();
    this();
    UiText.slider(paramTable, str, 36.0F, 72.0F, 2.0F, f1, schematicSettingsUi$11, schematicSettingsUi$12);
    str = LunariansI18n.get("columns");
    f1 = LunariansSettings.columns;
    SchematicSettingsUi$13 schematicSettingsUi$13 = new SchematicSettingsUi$13();
    this();
    SchematicSettingsUi$14 schematicSettingsUi$14 = new SchematicSettingsUi$14();
    this();
    float f3 = 10.0F;
    UiText.slider(paramTable, str, 0.0F, f3, 1.0F, f1, schematicSettingsUi$13, schematicSettingsUi$14);
    str = LunariansI18n.get("hold_seconds");
    f1 = LunariansSettings.holdSeconds;
    SchematicSettingsUi$15 schematicSettingsUi$15 = new SchematicSettingsUi$15();
    this();
    SchematicSettingsUi$16 schematicSettingsUi$16 = new SchematicSettingsUi$16();
    this();
    UiText.slider(paramTable, str, 1.0F, f3, 0.5F, f1, schematicSettingsUi$15, schematicSettingsUi$16);
  }
  
  public static void buildCategories(Table paramTable) {
    String str = LunariansI18n.get("category_count");
    float f1 = LunariansSettings.categoryCount;
    SchematicSettingsUi$17 schematicSettingsUi$17 = new SchematicSettingsUi$17();
    this();
    SchematicSettingsUi$18 schematicSettingsUi$18 = new SchematicSettingsUi$18();
    this();
    float f2 = 1.0F;
    int i = 1084227584;
    float f3 = 5.0F;
    int j = 1065353216;
    float f4 = 1.0F;
    Table table = paramTable;
    UiText.slider(paramTable, str, f2, f3, f4, f1, schematicSettingsUi$17, schematicSettingsUi$18);
    int k = 0;
    Cell cell = null;
    while (true) {
      int m = 5;
      float f = 7.0E-45F;
      if (k < m) {
        Table table1 = new Table();
        this();
        StringBuilder stringBuilder = new StringBuilder();
        String str2 = "#";
        this(str2);
        i = k + 1;
        stringBuilder.append(i);
        String str1 = stringBuilder.toString();
        table1.add(str1).width(30.0F);
        str1 = LunariansSettings.categoryNames[k];
        SchematicSettingsUi$19 schematicSettingsUi$19 = new SchematicSettingsUi$19();
        this(k);
        Cell cell1 = table1.field(str1, schematicSettingsUi$19).growX();
        f4 = 36.0F;
        TextField textField = (TextField)cell1.height(f4).get();
        j = LunariansSettings.categoryCount;
        if (k >= j) {
          k = 1;
        } else {
          k = 0;
          cell = null;
        } 
        textField.setDisabled(k);
        cell = table.add((Element)table1).growX();
        m = 1073741824;
        f = 2.0F;
        cell = cell.padBottom(f);
        cell.row();
        k = i;
        continue;
      } 
      break;
    } 
  }
  
  public static void onClosed() {
    SchematicPanel schematicPanel = LunariansMod.panel;
    if (schematicPanel != null) {
      schematicPanel = LunariansMod.panel;
      schematicPanel.applySettings();
    } 
  }
}
