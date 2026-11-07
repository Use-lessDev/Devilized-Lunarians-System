package lunarians.ui;

import arc.func.Boolf;
import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.AutoUnitHeal;
import lunarians.LunariansSettings;
import lunarians.data.UnitHealSlot;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

public class AutoUnitHealUi {
  private final Table list = new Table();
  
  private static UnitHealSlot clipboard = null;
  
  public static BuilderDialog make() {
    return new BuilderDialog(title(), new Cons<Table>() {
          public void get(Table param1Table) {
            new AutoUnitHealUi(param1Table);
          }
        },  new Runnable() {
          public void run() {
            AutoUnitHeal.reset();
          }
        });
  }
  
  public AutoUnitHealUi(Table paramTable) {
    paramTable.defaults().growX();
    paramTable.add(hint()).wrap().growX().color(Color.lightGray).padBottom(4.0F).row();
    paramTable.add((Element)this.list).growX().row();
    rebuild();
  }
  
  private void rebuild() {
    this.list.clearChildren();
    Seq<UnitHealSlot> seq = LunariansSettings.unitHealSlots;
    this.list.add("" + seq.size + "/8").color(Pal.accent).padBottom(4.0F).row();
    if (seq.isEmpty()) {
      this.list.add(empty()).wrap().growX().row();
    } else {
      for (byte b = 0; b < seq.size; ) {
        this.list.add((Element)card((UnitHealSlot)seq.get(b))).growX().padBottom(3.0F).row();
        b++;
      } 
    } 
    if (seq.size < 8)
      this.list.button((Drawable)Icon.add, Styles.defaulti, new Runnable() {
            public void run() {
              LunariansSettings.unitHealSlots.add(new UnitHealSlot());
              LunariansSettings.save();
              AutoUnitHealUi.this.rebuild();
            }
          }).size(40.0F).padTop(2.0F).row(); 
  }
  
  private Table card(final UnitHealSlot slot) {
    Table table1 = new Table(Styles.black6);
    table1.defaults().pad(4.0F).growX();
    Table table2 = new Table();
    UnitType unitType = slot.unit.isEmpty() ? null : Vars.content.unit(slot.unit);
    ImageButton imageButton = new ImageButton((unitType != null) ? (Drawable)new TextureRegionDrawable(unitType.uiIcon) : (Drawable)Icon.add, Styles.defaulti);
    imageButton.clicked(new Runnable() {
          public void run() {
            (new UnitPickerDialog(new Cons<UnitType>() {
                  public void get(UnitType param2UnitType) {
                    slot.unit = param2UnitType.name;
                    LunariansSettings.save();
                    AutoUnitHealUi.this.rebuild();
                  }
                },  new Boolf<UnitType>() {
                  public boolean get(UnitType param2UnitType) {
                    return AutoUnitHeal.isHealerType(param2UnitType);
                  }
                })).show();
          }
        });
    table2.add((Element)imageButton).size(40.0F).padRight(6.0F);
    table2.add((unitType != null) ? unitType.localizedName : pick()).wrap().growX().left().fontScale(0.9F);
    TextButton textButton1 = (TextButton)table2.button(copyLabel(), new Runnable() {
          public void run() {
            AutoUnitHealUi.clipboard = slot.copy();
            Vars.ui.showInfoToast(AutoUnitHealUi.copied(), 2.0F);
            AutoUnitHealUi.this.rebuild();
          }
        }).width(64.0F).height(32.0F).padLeft(4.0F).get();
    textButton1.getLabel().setFontScale(0.8F);
    table2.button((Drawable)Icon.trash, Styles.defaulti, new Runnable() {
          public void run() {
            LunariansSettings.unitHealSlots.remove(slot);
            LunariansSettings.save();
            AutoUnitHealUi.this.rebuild();
          }
        }).size(36.0F);
    table1.add((Element)table2).growX().row();
    Table table3 = new Table();
    TextButton textButton2 = (TextButton)table3.button(pasteLabel(), new Runnable() {
          public void run() {
            if (AutoUnitHealUi.clipboard == null) {
              Vars.ui.showInfoToast(AutoUnitHealUi.noClip(), 2.0F);
              return;
            } 
            if (LunariansSettings.unitHealSlots.size >= 8) {
              Vars.ui.showInfoToast("Max slots", 2.0F);
              return;
            } 
            LunariansSettings.unitHealSlots.add(AutoUnitHealUi.clipboard.copy());
            LunariansSettings.save();
            Vars.ui.showInfoToast(AutoUnitHealUi.pasted(), 2.0F);
            AutoUnitHealUi.this.rebuild();
          }
        }).width(90.0F).height(36.0F).pad(2.0F).get();
    textButton2.getLabel().setFontScale(0.85F);
    textButton2.setDisabled((clipboard == null));
    this.list.add((Element)table3).left().padTop(2.0F).row();
    table3 = new Table();
    table3.add(maxL()).left().padRight(4.0F).fontScale(0.85F);
    table3.button("-", new Runnable() {
          public void run() {
            if (slot.maxUnits > 0) {
              slot.maxUnits--;
              LunariansSettings.save();
              AutoUnitHealUi.this.rebuild();
            } 
          }
        }).size(32.0F, 28.0F);
    table3.label(new Prov<CharSequence>() {
          public CharSequence get() {
            return (slot.maxUnits <= 0) ? AutoUnitHealUi.all() : String.valueOf(slot.maxUnits);
          }
        }).width(40.0F).center();
    table3.button("+", new Runnable() {
          public void run() {
            if (slot.maxUnits < 50) {
              slot.maxUnits++;
              LunariansSettings.save();
              AutoUnitHealUi.this.rebuild();
            } 
          }
        }).size(32.0F, 28.0F);
    table1.add((Element)table3).left().row();
    return table1;
  }
  
  private static String title() {
    return "Auto unit heal";
  }
  
  private static String hint() {
    if ("pt".equals(LunariansSettings.language))
      return "Healers se aproximam de aliados com vida baixa. Não usados por defence/attack."; 
    return "Healers approach low-HP allies. Not used by defence/attack.";
  }
  
  private static String empty() {
    return "pt".equals(LunariansSettings.language) ? "Nenhum slot. +" : "No slots. +";
  }
  
  private static String pick() {
    return "pt".equals(LunariansSettings.language) ? "Escolher healer" : "Pick healer";
  }
  
  private static String maxL() {
    return "pt".equals(LunariansSettings.language) ? "Máx:" : "Max:";
  }
  
  private static String all() {
    return "pt".equals(LunariansSettings.language) ? "Todas" : "All";
  }
  
  private static String copyLabel() {
    return "pt".equals(LunariansSettings.language) ? "Copiar" : "Copy";
  }
  
  private static String pasteLabel() {
    return "pt".equals(LunariansSettings.language) ? "Colar" : "Paste";
  }
  
  private static String copied() {
    return "pt".equals(LunariansSettings.language) ? "Slot copiado" : "Slot copied";
  }
  
  private static String pasted() {
    return "pt".equals(LunariansSettings.language) ? "Slot colado" : "Slot pasted";
  }
  
  private static String noClip() {
    return "pt".equals(LunariansSettings.language) ? "Nada copiado" : "Nothing copied";
  }
}
