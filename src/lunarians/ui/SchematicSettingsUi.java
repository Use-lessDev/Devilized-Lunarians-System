/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.func.Boolc
 *  arc.func.Cons
 *  arc.func.Floatc
 *  arc.func.Prov
 *  arc.scene.Element
 *  arc.scene.ui.TextField
 *  arc.scene.ui.layout.Table
 */
package lunarians.ui;

import arc.func.Boolc;
import arc.func.Cons;
import arc.func.Floatc;
import arc.func.Prov;
import arc.scene.Element;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import lunarians.LunariansI18n;
import lunarians.LunariansMod;
import lunarians.LunariansSettings;
import lunarians.SchematicPlacer;
import lunarians.ui.UiText;

public final class SchematicSettingsUi {
    private SchematicSettingsUi() {
    }

    public static void build(Table table) {
        UiText.check(table, LunariansI18n.get("schem_ui_show"), LunariansSettings.showSchematicPanel, new Boolc(){

            public void get(boolean bl) {
                SchematicPlacer.setPanelVisible(bl);
            }
        }).left().row();
        UiText.section(table, LunariansI18n.get("panel_size"));
        UiText.slider(table, LunariansI18n.get("width"), 160.0f, 700.0f, 10.0f, LunariansSettings.panelWidth, new Floatc(){

            public void get(float f) {
                LunariansSettings.panelWidth = f;
            }
        }, new Prov<CharSequence>(){

            public CharSequence get() {
                return (int)LunariansSettings.panelWidth + "px";
            }
        });
        UiText.slider(table, LunariansI18n.get("height"), 120.0f, 600.0f, 10.0f, LunariansSettings.panelHeight, new Floatc(){

            public void get(float f) {
                LunariansSettings.panelHeight = f;
            }
        }, new Prov<CharSequence>(){

            public CharSequence get() {
                return (int)LunariansSettings.panelHeight + "px";
            }
        });
        UiText.slider(table, LunariansI18n.get("opacity"), 0.3f, 1.0f, 0.05f, LunariansSettings.opacity, new Floatc(){

            public void get(float f) {
                LunariansSettings.opacity = f;
            }
        }, new Prov<CharSequence>(){

            public CharSequence get() {
                return (int)(LunariansSettings.opacity * 100.0f) + "%";
            }
        });
        UiText.check(table, LunariansI18n.get("show_title"), LunariansSettings.showTitle, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.showTitle = bl;
            }
        }).left().row();
        UiText.section(table, LunariansI18n.get("slot_count"));
        UiText.slider(table, LunariansI18n.get("slots"), 1.0f, 300.0f, 1.0f, LunariansSettings.slotCount, new Floatc(){

            public void get(float f) {
                LunariansSettings.slotCount = (int)f;
            }
        }, new Prov<CharSequence>(){

            public CharSequence get() {
                return String.valueOf(LunariansSettings.slotCount);
            }
        });
        UiText.slider(table, LunariansI18n.get("slot_size"), 36.0f, 72.0f, 2.0f, LunariansSettings.slotSize, new Floatc(){

            public void get(float f) {
                LunariansSettings.slotSize = f;
            }
        }, new Prov<CharSequence>(){

            public CharSequence get() {
                return (int)LunariansSettings.slotSize + "px";
            }
        });
        UiText.slider(table, LunariansI18n.get("columns"), 0.0f, 10.0f, 1.0f, LunariansSettings.columns, new Floatc(){

            public void get(float f) {
                LunariansSettings.columns = (int)f;
            }
        }, new Prov<CharSequence>(){

            public CharSequence get() {
                return LunariansSettings.columns == 0 ? LunariansI18n.get("columns_auto") : String.valueOf(LunariansSettings.columns);
            }
        });
        UiText.slider(table, LunariansI18n.get("hold_seconds"), 1.0f, 10.0f, 0.5f, LunariansSettings.holdSeconds, new Floatc(){

            public void get(float f) {
                LunariansSettings.holdSeconds = f;
            }
        }, new Prov<CharSequence>(){

            public CharSequence get() {
                return LunariansSettings.holdSeconds + "s";
            }
        });
    }

    public static void buildCategories(Table table) {
        UiText.slider(table, LunariansI18n.get("category_count"), 1.0f, 5.0f, 1.0f, LunariansSettings.categoryCount, new Floatc(){

            public void get(float f) {
                LunariansSettings.categoryCount = (int)f;
                if (LunariansSettings.currentCategory >= LunariansSettings.categoryCount) {
                    LunariansSettings.currentCategory = LunariansSettings.categoryCount - 1;
                }
            }
        }, new Prov<CharSequence>(){

            public CharSequence get() {
                return String.valueOf(LunariansSettings.categoryCount);
            }
        });
        for (int i = 0; i < 5; ++i) {
            final int n = i;
            Table table2 = new Table();
            table2.add((CharSequence)("#" + (n + 1))).width(30.0f);
            TextField textField = (TextField)table2.field(LunariansSettings.categoryNames[n], (Cons)new Cons<String>(){

                public void get(String string) {
                    if (string != null && !string.isEmpty()) {
                        LunariansSettings.categoryNames[n] = string;
                    }
                }
            }).growX().height(36.0f).get();
            textField.setDisabled(n >= LunariansSettings.categoryCount);
            table.add((Element)table2).growX().padBottom(2.0f).row();
        }
    }

    public static void onClosed() {
        if (LunariansMod.panel != null) {
            LunariansMod.panel.applySettings();
        }
    }
}

