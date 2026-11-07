package lunarians.ui;

import arc.func.Boolc;
import arc.func.Cons;
import arc.func.Floatc;
import arc.func.Prov;
import arc.scene.ui.layout.Table;
import lunarians.LunariansI18n;
import lunarians.LunariansMod;
import lunarians.LunariansSettings;
import lunarians.SchematicPlacer;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/SchematicSettingsUi.class */
public final class SchematicSettingsUi {
    private SchematicSettingsUi() {
    }

    public static void build(Table table) {
        UiText.check(table, LunariansI18n.get("schem_ui_show"), LunariansSettings.showSchematicPanel, new Boolc() { // from class: lunarians.ui.SchematicSettingsUi.1
            public void get(boolean z) {
                SchematicPlacer.setPanelVisible(z);
            }
        }).left().row();
        UiText.section(table, LunariansI18n.get("panel_size"));
        UiText.slider(table, LunariansI18n.get("width"), 160.0f, 700.0f, 10.0f, LunariansSettings.panelWidth, new Floatc() { // from class: lunarians.ui.SchematicSettingsUi.2
            public void get(float f) {
                LunariansSettings.panelWidth = f;
            }
        }, new Prov<CharSequence>() { // from class: lunarians.ui.SchematicSettingsUi.3
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m83get() {
                return ((int) LunariansSettings.panelWidth) + "px";
            }
        });
        UiText.slider(table, LunariansI18n.get("height"), 120.0f, 600.0f, 10.0f, LunariansSettings.panelHeight, new Floatc() { // from class: lunarians.ui.SchematicSettingsUi.4
            public void get(float f) {
                LunariansSettings.panelHeight = f;
            }
        }, new Prov<CharSequence>() { // from class: lunarians.ui.SchematicSettingsUi.5
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m84get() {
                return ((int) LunariansSettings.panelHeight) + "px";
            }
        });
        UiText.slider(table, LunariansI18n.get("opacity"), 0.3f, 1.0f, 0.05f, LunariansSettings.opacity, new Floatc() { // from class: lunarians.ui.SchematicSettingsUi.6
            public void get(float f) {
                LunariansSettings.opacity = f;
            }
        }, new Prov<CharSequence>() { // from class: lunarians.ui.SchematicSettingsUi.7
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m85get() {
                return ((int) (LunariansSettings.opacity * 100.0f)) + "%";
            }
        });
        UiText.check(table, LunariansI18n.get("show_title"), LunariansSettings.showTitle, new Boolc() { // from class: lunarians.ui.SchematicSettingsUi.8
            public void get(boolean z) {
                LunariansSettings.showTitle = z;
            }
        }).left().row();
        UiText.section(table, LunariansI18n.get("slot_count"));
        UiText.slider(table, LunariansI18n.get("slots"), 1.0f, 300.0f, 1.0f, LunariansSettings.slotCount, new Floatc() { // from class: lunarians.ui.SchematicSettingsUi.9
            public void get(float f) {
                LunariansSettings.slotCount = (int) f;
            }
        }, new Prov<CharSequence>() { // from class: lunarians.ui.SchematicSettingsUi.10
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m78get() {
                return String.valueOf(LunariansSettings.slotCount);
            }
        });
        UiText.slider(table, LunariansI18n.get("slot_size"), 36.0f, 72.0f, 2.0f, LunariansSettings.slotSize, new Floatc() { // from class: lunarians.ui.SchematicSettingsUi.11
            public void get(float f) {
                LunariansSettings.slotSize = f;
            }
        }, new Prov<CharSequence>() { // from class: lunarians.ui.SchematicSettingsUi.12
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m79get() {
                return ((int) LunariansSettings.slotSize) + "px";
            }
        });
        UiText.slider(table, LunariansI18n.get("columns"), 0.0f, 10.0f, 1.0f, LunariansSettings.columns, new Floatc() { // from class: lunarians.ui.SchematicSettingsUi.13
            public void get(float f) {
                LunariansSettings.columns = (int) f;
            }
        }, new Prov<CharSequence>() { // from class: lunarians.ui.SchematicSettingsUi.14
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m80get() {
                return LunariansSettings.columns == 0 ? LunariansI18n.get("columns_auto") : String.valueOf(LunariansSettings.columns);
            }
        });
        UiText.slider(table, LunariansI18n.get("hold_seconds"), 1.0f, 10.0f, 0.5f, LunariansSettings.holdSeconds, new Floatc() { // from class: lunarians.ui.SchematicSettingsUi.15
            public void get(float f) {
                LunariansSettings.holdSeconds = f;
            }
        }, new Prov<CharSequence>() { // from class: lunarians.ui.SchematicSettingsUi.16
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m81get() {
                return LunariansSettings.holdSeconds + "s";
            }
        });
    }

    public static void buildCategories(Table table) {
        UiText.slider(table, LunariansI18n.get("category_count"), 1.0f, 5.0f, 1.0f, LunariansSettings.categoryCount, new Floatc() { // from class: lunarians.ui.SchematicSettingsUi.17
            public void get(float f) {
                LunariansSettings.categoryCount = (int) f;
                if (LunariansSettings.currentCategory >= LunariansSettings.categoryCount) {
                    LunariansSettings.currentCategory = LunariansSettings.categoryCount - 1;
                }
            }
        }, new Prov<CharSequence>() { // from class: lunarians.ui.SchematicSettingsUi.18
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m82get() {
                return String.valueOf(LunariansSettings.categoryCount);
            }
        });
        for (int i = 0; i < 5; i++) {
            final int i2 = i;
            Table table2 = new Table();
            table2.add("#" + (i2 + 1)).width(30.0f);
            table2.field(LunariansSettings.categoryNames[i2], new Cons<String>() { // from class: lunarians.ui.SchematicSettingsUi.19
                public void get(String str) {
                    if (str != null && !str.isEmpty()) {
                        LunariansSettings.categoryNames[i2] = str;
                    }
                }
            }).growX().height(36.0f).get().setDisabled(i2 >= LunariansSettings.categoryCount);
            table.add(table2).growX().padBottom(2.0f).row();
        }
    }

    public static void onClosed() {
        if (LunariansMod.panel != null) {
            LunariansMod.panel.applySettings();
        }
    }
}
