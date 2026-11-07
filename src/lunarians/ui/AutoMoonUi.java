package lunarians.ui;

import arc.func.Boolf;
import arc.func.Cons;
import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.AutoMoon;
import lunarians.LunariansSettings;
import lunarians.data.MoonSlot;
import mindustry.Vars;
import mindustry.gen.Groups;
import mindustry.gen.Icon;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

public class AutoMoonUi {
  private final Table list = new Table();
  
  private static MoonSlot clipboard = null;
  
  public static BuilderDialog make() {
    return new BuilderDialog(title(), new Cons<Table>() {
          public void get(Table param1Table) {
            new AutoMoonUi(param1Table);
          }
        },  new Runnable() {
          public void run() {
            AutoMoon.reset();
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
  
  public AutoMoonUi(Table paramTable) {
    paramTable.defaults().growX();
    paramTable.add(hint()).wrap().growX().color(Color.lightGray).padBottom(4.0F).row();
    paramTable.add((Element)this.list).growX().row();
    rebuild();
  }
  
  private void rebuild() {
    this.list.clearChildren();
    Seq<MoonSlot> seq = LunariansSettings.moonSlots;
    this.list.add("" + seq.size + "/8").color(Pal.accent).padBottom(4.0F).row();
    if (seq.isEmpty()) {
      this.list.add(empty()).wrap().growX().row();
    } else {
      for (byte b = 0; b < seq.size; ) {
        this.list.add((Element)card((MoonSlot)seq.get(b))).growX().padBottom(3.0F).row();
        b++;
      } 
    } 
    if (seq.size < 8)
      this.list.button((Drawable)Icon.add, Styles.defaulti, new Runnable() {
            public void run() {
              LunariansSettings.moonSlots.add(new MoonSlot());
              LunariansSettings.save();
              AutoMoonUi.this.rebuild();
            }
          }).size(40.0F).padTop(2.0F).row(); 
  }
  
  private Table card(final MoonSlot slot) {
    String str;
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
                    AutoMoonUi.this.rebuild();
                  }
                },  new Boolf<UnitType>() {
                  public boolean get(UnitType param2UnitType) {
                    return param2UnitType.flying;
                  }
                })).show();
          }
        });
    table2.add((Element)imageButton).size(40.0F).padRight(6.0F);
    table2.add((unitType != null) ? unitType.localizedName : pick()).wrap().growX().left().fontScale(0.9F);
    TextButton textButton1 = (TextButton)table2.button(copyLabel(), new Runnable() {
          public void run() {
            AutoMoonUi.clipboard = slot.copy();
            Vars.ui.showInfoToast(AutoMoonUi.copied(), 2.0F);
            AutoMoonUi.this.rebuild();
          }
        }).width(64.0F).height(32.0F).padLeft(4.0F).get();
    textButton1.getLabel().setFontScale(0.8F);
    table2.button((Drawable)Icon.trash, Styles.defaulti, new Runnable() {
          public void run() {
            LunariansSettings.moonSlots.remove(slot);
            LunariansSettings.save();
            AutoMoonUi.this.rebuild();
          }
        }).size(36.0F);
    table1.add((Element)table2).growX().row();
    Table table3 = new Table();
    TextButton textButton2 = (TextButton)table3.button(pasteLabel(), new Runnable() {
          public void run() {
            if (AutoMoonUi.clipboard == null) {
              Vars.ui.showInfoToast(AutoMoonUi.noClip(), 2.0F);
              return;
            } 
            if (LunariansSettings.moonSlots.size >= 8) {
              Vars.ui.showInfoToast("Max slots", 2.0F);
              return;
            } 
            LunariansSettings.moonSlots.add(AutoMoonUi.clipboard.copy());
            LunariansSettings.save();
            Vars.ui.showInfoToast(AutoMoonUi.pasted(), 2.0F);
            AutoMoonUi.this.rebuild();
          }
        }).width(90.0F).height(36.0F).pad(2.0F).get();
    textButton2.getLabel().setFontScale(0.85F);
    textButton2.setDisabled((clipboard == null));
    this.list.add((Element)table3).left().padTop(2.0F).row();
    rowStepper(table1, maxL(), 
        (slot.maxUnits <= 0) ? all() : String.valueOf(slot.maxUnits), new Runnable() {
          public void run() {
            if (slot.maxUnits > 0) {
              slot.maxUnits--;
              LunariansSettings.save();
              AutoMoonUi.this.rebuild();
            } 
          }
        },  new Runnable() {
          public void run() {
            if (slot.maxUnits < 50) {
              slot.maxUnits++;
              LunariansSettings.save();
              AutoMoonUi.this.rebuild();
            } 
          }
        });
    rowStepper(table1, radL(), "" + (int)slot.radius + "b", new Runnable() {
          public void run() {
            if (slot.radius > 1.0F) {
              slot.radius = Math.max(1.0F, slot.radius - 1.0F);
              LunariansSettings.save();
              AutoMoonUi.this.rebuild();
            } 
          }
        }new Runnable() {
          public void run() {
            if (slot.radius < 40.0F) {
              slot.radius++;
              LunariansSettings.save();
              AutoMoonUi.this.rebuild();
            } 
          }
        });
    rowStepper(table1, speedL(), String.format("%.2fx", new Object[] { Float.valueOf(slot.speed) }), new Runnable() {
          public void run() {
            slot.speed = Math.max(0.25F, slot.speed - 0.25F);
            LunariansSettings.save();
            AutoMoonUi.this.rebuild();
          }
        }new Runnable() {
          public void run() {
            slot.speed = Math.min(4.0F, slot.speed + 0.25F);
            LunariansSettings.save();
            AutoMoonUi.this.rebuild();
          }
        });
    table3 = new Table();
    if (slot.hasUnitCenter()) {
      Unit unit = (Unit)Groups.unit.getByID(slot.followId);
      if (unit != null && unit.isValid()) {
        str = unit.type.localizedName + " #" + unit.type.localizedName;
      } else if (slot.followLabel != null && !slot.followLabel.isEmpty()) {
        str = slot.followLabel + " #" + slot.followLabel;
      } else {
        str = "#" + slot.followId;
      } 
    } else if (slot.hasTileCenter()) {
      str = "tile " + slot.x + "," + slot.y;
    } else {
      str = followPlayer();
    } 
    table3.add(centerL() + " " + centerL()).wrap().growX().left().fontScale(0.85F)
      .color((slot.hasUnitCenter() || slot.hasTileCenter()) ? Color.white : Color.lightGray);
    table1.add((Element)table3).growX().row();
    Table table4 = new Table();
    TextButton textButton3 = (TextButton)table4.button(unitMapBtn(), new Runnable() {
          public void run() {
            FeatureDialog featureDialog = FeatureDialog.top;
            if (featureDialog != null) {
              featureDialog.after((Runnable)null);
              featureDialog.hide();
            } 
            UnitOnMapPicker.pick(new Cons<Unit>() {
                  public void get(Unit param2Unit) {
                    slot.followId = param2Unit.id;
                    slot.followLabel = (param2Unit.type != null) ? param2Unit.type.localizedName : "";
                    LunariansSettings.save();
                    Vars.ui.showInfoToast("pt".equals(LunariansSettings.language) ? ("Órbita: " + slot.followLabel + " #" + slot.followId) : ("Orbit: " + slot.followLabel + " #" + slot.followId), 2.0F);
                    AutoMoonUi.reopen();
                  }
                }new Runnable() {
                  public void run() {
                    AutoMoonUi.reopen();
                  }
                });
          }
        }).width(110.0F).height(32.0F).get();
    textButton3.getLabel().setFontScale(0.8F);
    TextButton textButton4 = (TextButton)table4.button(mapBtn(), new Runnable() {
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
                    AutoMoonUi.reopen();
                  }
                },  new Runnable() {
                  public void run() {
                    AutoMoonUi.reopen();
                  }
                });
          }
        }).width(70.0F).height(32.0F).padLeft(4.0F).get();
    textButton4.getLabel().setFontScale(0.8F);
    table4.button((Drawable)Icon.trash, Styles.defaulti, new Runnable() {
          public void run() {
            slot.followId = -1;
            slot.followLabel = "";
            slot.x = Integer.MIN_VALUE;
            slot.y = Integer.MIN_VALUE;
            LunariansSettings.save();
            AutoMoonUi.this.rebuild();
          }
        }).size(32.0F).padLeft(4.0F);
    table1.add((Element)table4).left().row();
    return table1;
  }
  
  private void rowStepper(Table paramTable, String paramString1, String paramString2, Runnable paramRunnable1, Runnable paramRunnable2) {
    Table table = new Table();
    table.add(paramString1).left().padRight(4.0F).fontScale(0.85F);
    table.button("-", paramRunnable1).size(32.0F, 28.0F);
    table.add(paramString2).width(48.0F).center().pad(2.0F);
    table.button("+", paramRunnable2).size(32.0F, 28.0F);
    paramTable.add((Element)table).left().row();
  }
  
  private static String title() {
    return "Auto Moon";
  }
  
  private static String hint() {
    if ("pt".equals(LunariansSettings.language))
      return "Voadoras orbitam jogador / tile / unidade no mapa. Velocidade ajustável. Atacam perto."; 
    return "Flyers orbit player / tile / map unit. Adjustable speed. Attack nearby.";
  }
  
  private static String empty() {
    return "pt".equals(LunariansSettings.language) ? "Nenhum slot. +" : "No slots. +";
  }
  
  private static String pick() {
    return "pt".equals(LunariansSettings.language) ? "Unidade voadora" : "Flying unit";
  }
  
  private static String maxL() {
    return "pt".equals(LunariansSettings.language) ? "Máx:" : "Max:";
  }
  
  private static String all() {
    return "pt".equals(LunariansSettings.language) ? "Todas" : "All";
  }
  
  private static String radL() {
    return "pt".equals(LunariansSettings.language) ? "Raio:" : "Radius:";
  }
  
  private static String speedL() {
    return "pt".equals(LunariansSettings.language) ? "Vel:" : "Speed:";
  }
  
  private static String centerL() {
    return "pt".equals(LunariansSettings.language) ? "Centro:" : "Center:";
  }
  
  private static String followPlayer() {
    return "pt".equals(LunariansSettings.language) ? "jogador" : "player";
  }
  
  private static String mapBtn() {
    return "pt".equals(LunariansSettings.language) ? "Tile" : "Tile";
  }
  
  private static String unitMapBtn() {
    return "pt".equals(LunariansSettings.language) ? "Unidade mapa" : "Map unit";
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
