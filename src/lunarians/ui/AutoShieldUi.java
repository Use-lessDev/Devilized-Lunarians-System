package lunarians.ui;

import arc.func.Boolf;
import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Image;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.AutoShield;
import lunarians.LunariansSettings;
import lunarians.data.ShieldSlot;
import mindustry.Vars;
import mindustry.gen.Groups;
import mindustry.gen.Icon;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

public class AutoShieldUi {
  private final Table list = new Table();
  
  private static ShieldSlot clipboard = null;
  
  public static BuilderDialog make() {
    return new BuilderDialog(title(), new Cons<Table>() {
          public void get(Table param1Table) {
            new AutoShieldUi(param1Table);
          }
        },  new Runnable() {
          public void run() {
            AutoShield.reset();
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
  
  public AutoShieldUi(Table paramTable) {
    paramTable.defaults().growX();
    paramTable.add(hint()).wrap().growX().color(Color.lightGray).padBottom(4.0F).row();
    paramTable.add((Element)this.list).growX().row();
    rebuild();
  }
  
  private void rebuild() {
    this.list.clearChildren();
    Seq<ShieldSlot> seq = LunariansSettings.shieldSlots;
    this.list.add("" + seq.size + "/8").color(Pal.accent).padBottom(4.0F).row();
    if (seq.isEmpty()) {
      this.list.add(emptyMsg()).wrap().growX().padBottom(4.0F).row();
    } else {
      for (byte b = 0; b < seq.size; b++)
        this.list.add((Element)card((ShieldSlot)seq.get(b))).growX().padBottom(3.0F).row(); 
    } 
    if (seq.size < 8)
      this.list.button((Drawable)Icon.add, Styles.defaulti, new Runnable() {
            public void run() {
              if (LunariansSettings.shieldSlots.size < 8) {
                LunariansSettings.shieldSlots.add(new ShieldSlot());
                LunariansSettings.save();
                AutoShieldUi.this.rebuild();
              } 
            }
          }).size(40.0F).padTop(2.0F).row(); 
  }
  
  private Table card(final ShieldSlot slot) {
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
                    Vars.ui.showInfoToast(param2UnitType.localizedName, 2.0F);
                    AutoShieldUi.this.rebuild();
                  }
                }new Boolf<UnitType>() {
                  public boolean get(UnitType param2UnitType) {
                    return AutoShield.hasShieldAbility(param2UnitType);
                  }
                })).show();
          }
        });
    table2.add((Element)imageButton).size(40.0F).padRight(6.0F);
    table2.add((unitType != null) ? unitType.localizedName : pickShield()).wrap().growX().left().fontScale(0.9F)
      .color((unitType != null) ? Color.white : Color.lightGray);
    TextButton textButton1 = (TextButton)table2.button(copyLabel(), new Runnable() {
          public void run() {
            AutoShieldUi.clipboard = slot.copy();
            Vars.ui.showInfoToast(AutoShieldUi.copied(), 2.0F);
            AutoShieldUi.this.rebuild();
          }
        }).width(64.0F).height(32.0F).padLeft(4.0F).get();
    textButton1.getLabel().setFontScale(0.8F);
    table2.button((Drawable)Icon.trash, Styles.defaulti, new Runnable() {
          public void run() {
            LunariansSettings.shieldSlots.remove(slot);
            LunariansSettings.save();
            AutoShieldUi.this.rebuild();
          }
        }).size(36.0F).padLeft(4.0F);
    table1.add((Element)table2).growX().row();
    Table table3 = new Table();
    TextButton textButton2 = (TextButton)table3.button(pasteLabel(), new Runnable() {
          public void run() {
            if (AutoShieldUi.clipboard == null) {
              Vars.ui.showInfoToast(AutoShieldUi.noClip(), 2.0F);
              return;
            } 
            if (LunariansSettings.shieldSlots.size >= 8) {
              Vars.ui.showInfoToast("Max slots", 2.0F);
              return;
            } 
            LunariansSettings.shieldSlots.add(AutoShieldUi.clipboard.copy());
            LunariansSettings.save();
            Vars.ui.showInfoToast(AutoShieldUi.pasted(), 2.0F);
            AutoShieldUi.this.rebuild();
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
              AutoShieldUi.this.rebuild();
            } 
          }
        }).size(32.0F, 28.0F);
    table3.label(new Prov<CharSequence>() {
          public CharSequence get() {
            return (slot.maxUnits <= 0) ? AutoShieldUi.all() : String.valueOf(slot.maxUnits);
          }
        }).width(40.0F).center().pad(2.0F);
    table3.button("+", new Runnable() {
          public void run() {
            if (slot.maxUnits < 50) {
              slot.maxUnits++;
              LunariansSettings.save();
              AutoShieldUi.this.rebuild();
            } 
          }
        }).size(32.0F, 28.0F);
    table1.add((Element)table3).left().padTop(2.0F).row();
    Table table4 = new Table();
    table4.add(followLabel()).left().padRight(4.0F).fontScale(0.85F);
    Unit unit = (slot.followId >= 0) ? (Unit)Groups.unit.getByID(slot.followId) : null;
    if (unit != null && unit.isValid()) {
      Image image = new Image(unit.type.uiIcon);
      table4.add((Element)image).size(28.0F).padRight(4.0F);
      table4.add(unit.type.localizedName + "  #" + unit.type.localizedName)
        .wrap().growX().left().fontScale(0.9F).color(Pal.accent);
    } else if (slot.followId >= 0) {
      String str = (slot.followLabel != null && !slot.followLabel.isEmpty()) ? slot.followLabel : ("#" + slot.followId);
      table4.add(str + "  #" + str + " [dead?]")
        .wrap().growX().left().fontScale(0.85F).color(Color.orange);
    } else {
      table4.add(pickFollow()).wrap().growX().left().fontScale(0.85F).color(Color.lightGray);
    } 
    TextButton textButton3 = (TextButton)table4.button(mapBtn(), new Runnable() {
          public void run() {
            FeatureDialog featureDialog = FeatureDialog.top;
            if (featureDialog != null) {
              featureDialog.after((Runnable)null);
              featureDialog.hide();
            } 
            UnitOnMapPicker.pick(new Cons<Unit>() {
                  public void get(Unit param2Unit) {
                    slot.followId = param2Unit.id;
                    slot.followLabel = param2Unit.type.localizedName;
                    LunariansSettings.save();
                    Vars.ui.showInfoToast(("pt".equals(LunariansSettings.language) ? "Seguindo: " : "Following: ") + ("pt".equals(LunariansSettings.language) ? "Seguindo: " : "Following: ") + " #" + param2Unit.type.localizedName, 3.0F);
                    AutoShieldUi.reopen();
                  }
                }new Runnable() {
                  public void run() {
                    AutoShieldUi.reopen();
                  }
                });
          }
        }).width(100.0F).height(32.0F).padLeft(4.0F).get();
    textButton3.getLabel().setFontScale(0.85F);
    table1.add((Element)table4).growX().padTop(2.0F).row();
    return table1;
  }
  
  private static String title() {
    String str = LunariansSettings.language;
    if ("pt".equals(str))
      return "Auto shield"; 
    return "Auto shield";
  }
  
  private static String hint() {
    String str = LunariansSettings.language;
    if ("pt".equals(str))
      return "1) Unidade shield  2) Máx  3) Mapa = toque na unidade a seguir. Movimento por comando."; 
    return "1) Shield unit  2) Max  3) Map = tap unit to follow. Uses unit commands.";
  }
  
  private static String emptyMsg() {
    String str = LunariansSettings.language;
    if ("pt".equals(str))
      return "Nenhum slot. Aperte +"; 
    return "No slots. Press +";
  }
  
  private static String pickShield() {
    String str = LunariansSettings.language;
    if ("pt".equals(str))
      return "Escolher shield"; 
    return "Pick shield unit";
  }
  
  private static String pickFollow() {
    String str = LunariansSettings.language;
    if ("pt".equals(str))
      return "Nenhuma — toque Mapa"; 
    return "None — tap Map";
  }
  
  private static String followLabel() {
    String str = LunariansSettings.language;
    if ("pt".equals(str) || "es".equals(str))
      return "Seguir:"; 
    return "Follow:";
  }
  
  private static String maxLabel() {
    String str = LunariansSettings.language;
    if ("pt".equals(str) || "es".equals(str))
      return "Máx:"; 
    return "Max:";
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
