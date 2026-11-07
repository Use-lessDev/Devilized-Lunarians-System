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
import arc.struct.IntSeq;
import arc.struct.Seq;
import lunarians.AutoCargo;
import lunarians.LunariansSettings;
import lunarians.data.CargoSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

public class AutoCargoUi {
  private final Table list = new Table();
  
  private static CargoSlot clipboard = null;
  
  public static BuilderDialog make() {
    return new BuilderDialog(title(), new Cons<Table>() {
          public void get(Table param1Table) {
            new AutoCargoUi(param1Table);
          }
        },  new Runnable() {
          public void run() {
            AutoCargo.reset();
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
  
  private static boolean isCarrier(UnitType paramUnitType) {
    return (paramUnitType.pickupUnits && paramUnitType.payloadCapacity > 0.0F && paramUnitType.commands != null && paramUnitType.commands
      
      .contains(UnitCommand.loopPayloadCommand));
  }
  
  public AutoCargoUi(Table paramTable) {
    paramTable.defaults().growX();
    paramTable.add(hint()).wrap().growX().color(Color.lightGray).padBottom(4.0F).row();
    paramTable.add((Element)this.list).growX().row();
    rebuild();
  }
  
  private void rebuild() {
    this.list.clearChildren();
    Seq<CargoSlot> seq = LunariansSettings.cargoSlots;
    this.list.add("" + seq.size + "/10").color(Pal.accent).padBottom(4.0F).row();
    if (seq.isEmpty()) {
      this.list.add(emptyMsg()).wrap().growX().padBottom(4.0F).row();
    } else {
      for (byte b = 0; b < seq.size; b++)
        this.list.add((Element)card((CargoSlot)seq.get(b))).growX().padBottom(3.0F).row(); 
    } 
    if (seq.size < 10)
      this.list.button((Drawable)Icon.add, Styles.defaulti, new Runnable() {
            public void run() {
              if (LunariansSettings.cargoSlots.size < 10) {
                LunariansSettings.cargoSlots.add(new CargoSlot());
                LunariansSettings.save();
                AutoCargoUi.this.rebuild();
              } 
            }
          }).size(40.0F).padTop(2.0F).row(); 
  }
  
  private Table card(final CargoSlot slot) {
    String str;
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
                    AutoCargoUi.this.rebuild();
                  }
                },  new Boolf<UnitType>() {
                  public boolean get(UnitType param2UnitType) {
                    return AutoCargoUi.isCarrier(param2UnitType);
                  }
                })).show();
          }
        });
    table2.add((Element)imageButton).size(40.0F).padRight(6.0F);
    table2.add((unitType != null) ? unitType.localizedName : pickUnit()).wrap().growX().left().fontScale(0.9F);
    TextButton textButton1 = (TextButton)table2.button(copyLabel(), new Runnable() {
          public void run() {
            AutoCargoUi.clipboard = slot.copy();
            Vars.ui.showInfoToast(AutoCargoUi.copied(), 2.0F);
            AutoCargoUi.this.rebuild();
          }
        }).width(64.0F).height(32.0F).padLeft(4.0F).get();
    textButton1.getLabel().setFontScale(0.8F);
    table2.button((Drawable)Icon.trash, Styles.defaulti, new Runnable() {
          public void run() {
            LunariansSettings.cargoSlots.remove(slot);
            LunariansSettings.save();
            AutoCargoUi.this.rebuild();
          }
        }).size(36.0F).padLeft(4.0F);
    table1.add((Element)table2).growX().row();
    Table table3 = new Table();
    TextButton textButton2 = (TextButton)table3.button(pasteLabel(), new Runnable() {
          public void run() {
            if (AutoCargoUi.clipboard == null) {
              Vars.ui.showInfoToast(AutoCargoUi.noClip(), 2.0F);
              return;
            } 
            if (LunariansSettings.cargoSlots.size >= 10) {
              Vars.ui.showInfoToast("Max slots", 2.0F);
              return;
            } 
            LunariansSettings.cargoSlots.add(AutoCargoUi.clipboard.copy());
            LunariansSettings.save();
            Vars.ui.showInfoToast(AutoCargoUi.pasted(), 2.0F);
            AutoCargoUi.this.rebuild();
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
              AutoCargoUi.this.rebuild();
            } 
          }
        }).size(32.0F, 28.0F);
    table3.label(new Prov<CharSequence>() {
          public CharSequence get() {
            return (slot.maxUnits <= 0) ? AutoCargoUi.maxAll() : String.valueOf(slot.maxUnits);
          }
        }).width(40.0F).center().pad(2.0F);
    table3.button("+", new Runnable() {
          public void run() {
            if (slot.maxUnits < 50) {
              slot.maxUnits++;
              LunariansSettings.save();
              AutoCargoUi.this.rebuild();
            } 
          }
        }).size(32.0F, 28.0F);
    table1.add((Element)table3).left().padTop(2.0F).row();
    int i = slot.pointCount();
    if (i == 0) {
      str = pathEmpty();
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      for (byte b = 0; b < i; b++) {
        if (b > 0)
          stringBuilder.append(" → "); 
        String str1 = (b == 0) ? "P1" : ((b == i - 1) ? "Drop" : ("P" + b + 1));
        stringBuilder.append(str1).append("(").append(slot.px(b)).append(",").append(slot.py(b)).append(")");
      } 
      str = stringBuilder.toString();
    } 
    table1.add(str).wrap().growX().left().fontScale(0.8F)
      .color((i >= 2) ? Color.white : Color.lightGray).row();
    Table table4 = new Table();
    TextButton textButton3 = (TextButton)table4.button(pathBtn(), new Runnable() {
          public void run() {
            FeatureDialog featureDialog = FeatureDialog.top;
            if (featureDialog != null) {
              featureDialog.after((Runnable)null);
              featureDialog.hide();
            } 
            PathPicker.pick(new Cons<IntSeq>() {
                  public void get(IntSeq param2IntSeq) {
                    slot.clearPoints();
                    for (byte b = 0; b + 1 < param2IntSeq.size; b += 2)
                      slot.addPoint(param2IntSeq.get(b), param2IntSeq.get(b + 1)); 
                    LunariansSettings.save();
                    AutoCargoUi.reopen();
                  }
                }new Runnable() {
                  public void run() {
                    AutoCargoUi.reopen();
                  }
                });
          }
        }).width(120.0F).height(32.0F).get();
    textButton3.getLabel().setFontScale(0.85F);
    if (i > 0)
      table4.button((Drawable)Icon.trash, Styles.defaulti, new Runnable() {
            public void run() {
              slot.clearPoints();
              LunariansSettings.save();
              AutoCargoUi.this.rebuild();
            }
          }).size(32.0F).padLeft(4.0F); 
    table1.add((Element)table4).left().padTop(2.0F).row();
    return table1;
  }
  
  private static String title() {
    return "Auto cargo";
  }
  
  private static String hint() {
    if ("pt".equals(LunariansSettings.language))
      return "Pontos: 1=pegar, meio=caminho, último=soltar. Mapa → Add point / Back / Done. Camera/Unit."; 
    return "Points: 1=pick, mid=path, last=drop. Map → Add/Back/Done. Camera/Unit mode.";
  }
  
  private static String emptyMsg() {
    return "pt".equals(LunariansSettings.language) ? "Nenhum slot. Aperte +" : "No slots. Press +";
  }
  
  private static String pickUnit() {
    return "pt".equals(LunariansSettings.language) ? "Escolher" : "Choose";
  }
  
  private static String maxLabel() {
    return "pt".equals(LunariansSettings.language) ? "Máx:" : "Max:";
  }
  
  private static String maxAll() {
    return "pt".equals(LunariansSettings.language) ? "Todas" : "All";
  }
  
  private static String pathEmpty() {
    return "pt".equals(LunariansSettings.language) ? "Sem rota — defina pontos" : "No path — set points";
  }
  
  private static String pathBtn() {
    return "pt".equals(LunariansSettings.language) ? "Pontos mapa" : "Map points";
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
