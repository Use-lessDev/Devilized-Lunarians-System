package lunarians.ui;

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
import lunarians.AutoHeal;
import lunarians.LunariansSettings;
import lunarians.data.HealSlot;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

public class AutoHealUi {
  private final Table list = new Table();
  
  private static HealSlot clipboard = null;
  
  public static BuilderDialog make() {
    return new BuilderDialog(title(), new Cons<Table>() {
          public void get(Table param1Table) {
            new AutoHealUi(param1Table);
          }
        },  new Runnable() {
          public void run() {
            AutoHeal.reset();
          }
        });
  }
  
  public AutoHealUi(Table paramTable) {
    paramTable.defaults().growX();
    paramTable.add(hint()).wrap().growX().color(Color.lightGray).padBottom(4.0F).row();
    Table table = new Table();
    table.add(pctLabel()).left().padRight(6.0F);
    table.button("-", new Runnable() {
          public void run() {
            LunariansSettings.autoHealPercent = Math.max(5.0F, LunariansSettings.autoHealPercent - 5.0F);
            LunariansSettings.save();
          }
        }).size(36.0F, 32.0F);
    table.label(new Prov<CharSequence>() {
          public CharSequence get() {
            return "" + (int)LunariansSettings.autoHealPercent + "%";
          }
        }).width(48.0F).center().pad(4.0F).color(Pal.accent);
    table.button("+", new Runnable() {
          public void run() {
            LunariansSettings.autoHealPercent = Math.min(90.0F, LunariansSettings.autoHealPercent + 5.0F);
            LunariansSettings.save();
          }
        }).size(36.0F, 32.0F);
    paramTable.add((Element)table).left().padBottom(6.0F).row();
    paramTable.add((Element)this.list).growX().row();
    rebuild();
  }
  
  private void rebuild() {
    this.list.clearChildren();
    Seq<HealSlot> seq = LunariansSettings.healSlots;
    this.list.add("" + seq.size + "/10").color(Pal.accent).padBottom(4.0F).row();
    if (seq.isEmpty()) {
      this.list.add(emptyMsg()).wrap().growX().padBottom(4.0F).row();
    } else {
      for (byte b = 0; b < seq.size; b++)
        this.list.add((Element)card((HealSlot)seq.get(b))).growX().padBottom(3.0F).row(); 
    } 
    if (seq.size < 10)
      this.list.button((Drawable)Icon.add, Styles.defaulti, new Runnable() {
            public void run() {
              if (LunariansSettings.healSlots.size < 10) {
                LunariansSettings.healSlots.add(new HealSlot());
                LunariansSettings.save();
                AutoHealUi.this.rebuild();
              } 
            }
          }).size(40.0F).padTop(2.0F).row(); 
  }
  
  private Table card(final HealSlot slot) {
    Table table1 = new Table(Styles.black6);
    table1.defaults().growX();
    table1.defaults().pad(4.0F);
    Table table2 = new Table();
    UnitType unitType = slot.unit.isEmpty() ? null : Vars.content.unit(slot.unit);
    ImageButton imageButton = new ImageButton((unitType != null) ? (Drawable)new TextureRegionDrawable(unitType.uiIcon) : (Drawable)Icon.add, Styles.defaulti);
    imageButton.clicked(new Runnable() {
          public void run() {
            (new UnitPickerDialog(new Cons<UnitType>() {
                  public void get(UnitType param2UnitType) {
                    slot.unit = param2UnitType.name;
                    LunariansSettings.save();
                    AutoHealUi.this.rebuild();
                  }
                },  null)).show();
          }
        });
    table2.add((Element)imageButton).size(40.0F).padRight(6.0F);
    table2.add((unitType != null) ? unitType.localizedName : pickUnit()).wrap().growX().left().fontScale(0.9F);
    TextButton textButton1 = (TextButton)table2.button(copyLabel(), new Runnable() {
          public void run() {
            AutoHealUi.clipboard = slot.copy();
            Vars.ui.showInfoToast(AutoHealUi.copied(), 2.0F);
            AutoHealUi.this.rebuild();
          }
        }).width(64.0F).height(32.0F).padLeft(4.0F).get();
    textButton1.getLabel().setFontScale(0.8F);
    table2.button((Drawable)Icon.trash, Styles.defaulti, new Runnable() {
          public void run() {
            LunariansSettings.healSlots.remove(slot);
            LunariansSettings.save();
            AutoHealUi.this.rebuild();
          }
        }).size(36.0F).padLeft(4.0F);
    table1.add((Element)table2).growX().row();
    Table table3 = new Table();
    TextButton textButton2 = (TextButton)table3.button(pasteLabel(), new Runnable() {
          public void run() {
            if (AutoHealUi.clipboard == null) {
              Vars.ui.showInfoToast(AutoHealUi.noClip(), 2.0F);
              return;
            } 
            if (LunariansSettings.healSlots.size >= 10) {
              Vars.ui.showInfoToast("Max slots", 2.0F);
              return;
            } 
            LunariansSettings.healSlots.add(AutoHealUi.clipboard.copy());
            LunariansSettings.save();
            Vars.ui.showInfoToast(AutoHealUi.pasted(), 2.0F);
            AutoHealUi.this.rebuild();
          }
        }).width(90.0F).height(36.0F).pad(2.0F).get();
    textButton2.getLabel().setFontScale(0.85F);
    textButton2.setDisabled((clipboard == null));
    this.list.add((Element)table3).left().padTop(2.0F).row();
    table3 = new Table();
    table3.add(maxLabel()).left().padRight(4.0F).fontScale(0.85F);
    table3.button("-", new Runnable() {
          public void run() {
            if (slot.maxUnits > 0) {
              slot.maxUnits--;
              LunariansSettings.save();
              AutoHealUi.this.rebuild();
            } 
          }
        }).size(32.0F, 28.0F);
    table3.label(new Prov<CharSequence>() {
          public CharSequence get() {
            return (slot.maxUnits <= 0) ? AutoHealUi.all() : String.valueOf(slot.maxUnits);
          }
        }).width(40.0F).center().pad(2.0F);
    table3.button("+", new Runnable() {
          public void run() {
            if (slot.maxUnits < 50) {
              slot.maxUnits++;
              LunariansSettings.save();
              AutoHealUi.this.rebuild();
            } 
          }
        }).size(32.0F, 28.0F);
    table1.add((Element)table3).left().padTop(2.0F).row();
    return table1;
  }
  
  private static String title() {
    return "Auto heal";
  }
  
  private static String hint() {
    if ("pt".equals(LunariansSettings.language))
      return "Sem slots = todas as unidades em comando. Com slots = só esses tipos. Vai ao repair (borda do raio, pathfind)."; 
    return "No slots = all command units. With slots = those types only. Paths to repair edge.";
  }
  
  private static String emptyMsg() {
    if ("pt".equals(LunariansSettings.language))
      return "Vazio = cura todas. Aperte + para filtrar por tipo."; 
    return "Empty = heal all. Press + to filter by type.";
  }
  
  private static String pctLabel() {
    return "pt".equals(LunariansSettings.language) ? "Vida <" : "HP <";
  }
  
  private static String pickUnit() {
    return "pt".equals(LunariansSettings.language) ? "Escolher" : "Choose";
  }
  
  private static String maxLabel() {
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
