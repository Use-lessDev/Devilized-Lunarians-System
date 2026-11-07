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
import lunarians.AutoDefence;
import lunarians.LunariansSettings;
import lunarians.data.DefenceSlot;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

public class AutoDefenceUi {
  private final Table list = new Table();
  
  private static DefenceSlot clipboard = null;
  
  public static BuilderDialog make() {
    return new BuilderDialog(title(), new Cons<Table>() {
          public void get(Table param1Table) {
            new AutoDefenceUi(param1Table);
          }
        },  new Runnable() {
          public void run() {
            AutoDefence.reset();
          }
        });
  }
  
  private static void reopen() {
    make().after(new Runnable() {
          public void run() {
            (new ConfigDialog()).show();
          }
        }).show();
  }
  
  public AutoDefenceUi(Table paramTable) {
    paramTable.defaults().growX();
    paramTable.add(hint()).wrap().growX().color(Color.lightGray).padBottom(4.0F).row();
    paramTable.add((Element)this.list).growX().row();
    rebuild();
  }
  
  private void rebuild() {
    this.list.clearChildren();
    Seq<DefenceSlot> seq = LunariansSettings.defenceSlots;
    this.list.add("" + seq.size + "/8").color(Pal.accent).padBottom(4.0F).row();
    if (seq.isEmpty()) {
      this.list.add(emptyMsg()).wrap().growX().padBottom(4.0F).row();
    } else {
      for (byte b = 0; b < seq.size; b++)
        this.list.add((Element)card((DefenceSlot)seq.get(b))).growX().padBottom(3.0F).row(); 
    } 
    if (seq.size < 8)
      this.list.button((Drawable)Icon.add, Styles.defaulti, new Runnable() {
            public void run() {
              if (LunariansSettings.defenceSlots.size < 8) {
                LunariansSettings.defenceSlots.add(new DefenceSlot());
                LunariansSettings.save();
                AutoDefenceUi.this.rebuild();
              } 
            }
          }).size(40.0F).padTop(2.0F).row(); 
  }
  
  private Table card(final DefenceSlot slot) {
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
                    AutoDefenceUi.this.rebuild();
                  }
                },  null)).show();
          }
        });
    table2.add((Element)imageButton).size(40.0F).padRight(6.0F);
    table2.add((unitType != null) ? unitType.localizedName : pickUnit()).wrap().growX().left().fontScale(0.9F);
    TextButton textButton1 = (TextButton)table2.button(copyLabel(), new Runnable() {
          public void run() {
            AutoDefenceUi.clipboard = slot.copy();
            Vars.ui.showInfoToast(AutoDefenceUi.copied(), 2.0F);
            AutoDefenceUi.this.rebuild();
          }
        }).width(64.0F).height(32.0F).padLeft(4.0F).get();
    textButton1.getLabel().setFontScale(0.8F);
    table2.button((Drawable)Icon.trash, Styles.defaulti, new Runnable() {
          public void run() {
            LunariansSettings.defenceSlots.remove(slot);
            LunariansSettings.save();
            AutoDefenceUi.this.rebuild();
          }
        }).size(36.0F).padLeft(4.0F);
    table1.add((Element)table2).growX().row();
    Table table3 = new Table();
    TextButton textButton2 = (TextButton)table3.button(pasteLabel(), new Runnable() {
          public void run() {
            if (AutoDefenceUi.clipboard == null) {
              Vars.ui.showInfoToast(AutoDefenceUi.noClip(), 2.0F);
              return;
            } 
            if (LunariansSettings.defenceSlots.size >= 8) {
              Vars.ui.showInfoToast("Max slots", 2.0F);
              return;
            } 
            LunariansSettings.defenceSlots.add(AutoDefenceUi.clipboard.copy());
            LunariansSettings.save();
            Vars.ui.showInfoToast(AutoDefenceUi.pasted(), 2.0F);
            AutoDefenceUi.this.rebuild();
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
              AutoDefenceUi.this.rebuild();
            } 
          }
        }).size(32.0F, 28.0F);
    table3.label(new Prov<CharSequence>() {
          public CharSequence get() {
            return (slot.maxUnits <= 0) ? AutoDefenceUi.all() : String.valueOf(slot.maxUnits);
          }
        }).width(40.0F).center().pad(2.0F);
    table3.button("+", new Runnable() {
          public void run() {
            if (slot.maxUnits < 50) {
              slot.maxUnits++;
              LunariansSettings.save();
              AutoDefenceUi.this.rebuild();
            } 
          }
        }).size(32.0F, 28.0F);
    table1.add((Element)table3).left().padTop(2.0F).row();
    Table table4 = new Table();
    table4.add(radLabel()).left().padRight(4.0F).fontScale(0.85F);
    table4.button("-", new Runnable() {
          public void run() {
            if (slot.radius > 2.0F) {
              slot.radius = Math.max(2.0F, slot.radius - 1.0F);
              LunariansSettings.save();
              AutoDefenceUi.this.rebuild();
            } 
          }
        }).size(32.0F, 28.0F);
    table4.label(new Prov<CharSequence>() {
          public CharSequence get() {
            return "" + (int)slot.radius + "b";
          }
        }).width(40.0F).center().pad(2.0F);
    table4.button("+", new Runnable() {
          public void run() {
            if (slot.radius < 70.0F) {
              slot.radius++;
              LunariansSettings.save();
              AutoDefenceUi.this.rebuild();
            } 
          }
        }).size(32.0F, 28.0F);
    table1.add((Element)table4).left().padTop(2.0F).row();
    Table table5 = new Table();
    boolean bool = (slot.x != Integer.MIN_VALUE) ? true : false;
    String str = bool ? ("" + slot.x + "," + slot.x) : "—";
    table5.add("Pos: " + str).wrap().growX().left().fontScale(0.85F)
      .color(bool ? Color.white : Color.lightGray);
    table5.button(mapBtn(), new Runnable() {
          public void run() {
            FeatureDialog featureDialog = FeatureDialog.top;
            if (featureDialog != null) {
              featureDialog.after((Runnable)null);
              featureDialog.hide();
            } 
            PositionPicker.pick(new Cons<int[]>() {
                  public void get(int[] param2ArrayOfint) {
                    slot.x = param2ArrayOfint[0];
                    slot.y = param2ArrayOfint[1];
                    LunariansSettings.save();
                    AutoDefenceUi.reopen();
                  }
                },  new Runnable() {
                  public void run() {
                    AutoDefenceUi.reopen();
                  }
                });
          }
        }).width(90.0F).height(32.0F).padLeft(4.0F);
    table1.add((Element)table5).growX().row();
    return table1;
  }
  
  private static String title() {
    String str = LunariansSettings.language;
    if ("pt".equals(str))
      return "Auto defesa"; 
    if ("es".equals(str))
      return "Auto defensa"; 
    if ("ru".equals(str))
      return "Авто-защита"; 
    return "Auto defence";
  }
  
  private static String hint() {
    String str = LunariansSettings.language;
    if ("pt".equals(str))
      return "Unidade + máx + posição + raio. Círculo vermelho = zona de ataque."; 
    if ("es".equals(str))
      return "Unidad + máx + posición + radio. Círculo rojo = zona."; 
    return "Unit + max + position + radius. Red circle = attack zone.";
  }
  
  private static String emptyMsg() {
    String str = LunariansSettings.language;
    if ("pt".equals(str))
      return "Nenhuma zona. Aperte +"; 
    return "No zones. Press +";
  }
  
  private static String pickUnit() {
    String str = LunariansSettings.language;
    if ("pt".equals(str))
      return "Escolher"; 
    return "Choose";
  }
  
  private static String maxLabel() {
    String str = LunariansSettings.language;
    if ("pt".equals(str) || "es".equals(str))
      return "Máx:"; 
    return "Max:";
  }
  
  private static String radLabel() {
    String str = LunariansSettings.language;
    if ("pt".equals(str))
      return "Raio:"; 
    if ("es".equals(str))
      return "Radio:"; 
    return "Radius:";
  }
  
  private static String all() {
    String str = LunariansSettings.language;
    if ("pt".equals(str) || "es".equals(str))
      return "Todas"; 
    return "All";
  }
  
  private static String mapBtn() {
    String str = LunariansSettings.language;
    if ("pt".equals(str) || "es".equals(str))
      return "Mapa"; 
    return "Map";
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
