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

public final class SchematicSettingsUi {
  public static void build(Table paramTable) {
    UiText.check(paramTable, LunariansI18n.get("schem_ui_show"), LunariansSettings.showSchematicPanel, new Boolc() {
          public void get(boolean param1Boolean) {
            SchematicPlacer.setPanelVisible(param1Boolean);
          }
        }).left().row();
    UiText.section(paramTable, LunariansI18n.get("panel_size"));
    UiText.slider(paramTable, LunariansI18n.get("width"), 160.0F, 700.0F, 10.0F, LunariansSettings.panelWidth, new Floatc() {
          public void get(float param1Float) {
            LunariansSettings.panelWidth = param1Float;
          }
        },  new Prov<CharSequence>() {
          public CharSequence get() {
            return (int)LunariansSettings.panelWidth + "px";
          }
        });
    UiText.slider(paramTable, LunariansI18n.get("height"), 120.0F, 600.0F, 10.0F, LunariansSettings.panelHeight, new Floatc() {
          public void get(float param1Float) {
            LunariansSettings.panelHeight = param1Float;
          }
        },  new Prov<CharSequence>() {
          public CharSequence get() {
            return (int)LunariansSettings.panelHeight + "px";
          }
        });
    UiText.slider(paramTable, LunariansI18n.get("opacity"), 0.3F, 1.0F, 0.05F, LunariansSettings.opacity, new Floatc() {
          public void get(float param1Float) {
            LunariansSettings.opacity = param1Float;
          }
        },  new Prov<CharSequence>() {
          public CharSequence get() {
            return (int)(LunariansSettings.opacity * 100.0F) + "%";
          }
        });
    UiText.check(paramTable, LunariansI18n.get("show_title"), LunariansSettings.showTitle, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.showTitle = param1Boolean;
          }
        }).left().row();
    UiText.section(paramTable, LunariansI18n.get("slot_count"));
    UiText.slider(paramTable, LunariansI18n.get("slots"), 1.0F, 300.0F, 1.0F, LunariansSettings.slotCount, new Floatc() {
          public void get(float param1Float) {
            LunariansSettings.slotCount = (int)param1Float;
          }
        },  new Prov<CharSequence>() {
          public CharSequence get() {
            return String.valueOf(LunariansSettings.slotCount);
          }
        });
    UiText.slider(paramTable, LunariansI18n.get("slot_size"), 36.0F, 72.0F, 2.0F, LunariansSettings.slotSize, new Floatc() {
          public void get(float param1Float) {
            LunariansSettings.slotSize = param1Float;
          }
        },  new Prov<CharSequence>() {
          public CharSequence get() {
            return (int)LunariansSettings.slotSize + "px";
          }
        });
    UiText.slider(paramTable, LunariansI18n.get("columns"), 0.0F, 10.0F, 1.0F, LunariansSettings.columns, new Floatc() {
          public void get(float param1Float) {
            LunariansSettings.columns = (int)param1Float;
          }
        },  new Prov<CharSequence>() {
          public CharSequence get() {
            return (LunariansSettings.columns == 0) ? LunariansI18n.get("columns_auto") : String.valueOf(LunariansSettings.columns);
          }
        });
    UiText.slider(paramTable, LunariansI18n.get("hold_seconds"), 1.0F, 10.0F, 0.5F, LunariansSettings.holdSeconds, new Floatc() {
          public void get(float param1Float) {
            LunariansSettings.holdSeconds = param1Float;
          }
        },  new Prov<CharSequence>() {
          public CharSequence get() {
            return LunariansSettings.holdSeconds + "s";
          }
        });
  }
  
  public static void buildCategories(Table paramTable) {
    UiText.slider(paramTable, LunariansI18n.get("category_count"), 1.0F, 5.0F, 1.0F, LunariansSettings.categoryCount, new Floatc() {
          public void get(float param1Float) {
            LunariansSettings.categoryCount = (int)param1Float;
            if (LunariansSettings.currentCategory >= LunariansSettings.categoryCount)
              LunariansSettings.currentCategory = LunariansSettings.categoryCount - 1; 
          }
        },  new Prov<CharSequence>() {
          public CharSequence get() {
            return String.valueOf(LunariansSettings.categoryCount);
          }
        });
    for (byte b = 0; b < 5; b++) {
      final byte n = b;
      Table table = new Table();
      table.add("#" + (b1 + 1)).width(30.0F);
      TextField textField = (TextField)table.field(LunariansSettings.categoryNames[b1], new Cons<String>() {
            public void get(String param1String) {
              if (param1String != null && !param1String.isEmpty())
                LunariansSettings.categoryNames[n] = param1String; 
            }
          }).growX().height(36.0F).get();
      textField.setDisabled((b1 >= LunariansSettings.categoryCount));
      paramTable.add((Element)table).growX().padBottom(2.0F).row();
    } 
  }
  
  public static void onClosed() {
    if (LunariansMod.panel != null)
      LunariansMod.panel.applySettings(); 
  }
}
