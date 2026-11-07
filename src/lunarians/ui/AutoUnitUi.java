package lunarians.ui;

import arc.func.Boolc;
import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Button;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.AutoUnit;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.data.UnitSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

public class AutoUnitUi {
  private final Table list = new Table();
  
  private static UnitSlot clipboard = null;
  
  public AutoUnitUi(Table paramTable) {
    UiText.check(paramTable, LunariansI18n.get("autounit"), LunariansSettings.enableAutoUnit, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoUnit = param1Boolean;
            AutoUnit.reset();
          }
        }).left().row();
    paramTable.add(LunariansI18n.get("autounit_hint")).wrap().growX().color(Color.lightGray).padBottom(6.0F).row();
    paramTable.add((Element)this.list).growX().row();
    rebuild();
  }
  
  private void rebuild() {
    this.list.clear();
    this.list.defaults().growX();
    Seq<UnitSlot> seq = LunariansSettings.unitSlots;
    if (seq.isEmpty())
      this.list.add(LunariansI18n.get("au_empty")).wrap().growX().padBottom(6.0F).row(); 
    for (byte b = 0; b < seq.size; ) {
      this.list.add((Element)card((UnitSlot)seq.get(b))).growX().padBottom(6.0F).row();
      b++;
    } 
    if (seq.size < 10)
      this.list.button((Drawable)Icon.add, Styles.defaulti, new Runnable() {
            public void run() {
              LunariansSettings.unitSlots.add(new UnitSlot());
              LunariansSettings.save();
              AutoUnitUi.this.rebuild();
            }
          }).size(48.0F).padTop(4.0F).row(); 
    this.list.add("" + seq.size + "/10").color(Pal.accent).row();
    Table table = new Table();
    TextButton textButton = (TextButton)table.button(pasteLabel(), new Runnable() {
          public void run() {
            if (AutoUnitUi.clipboard == null) {
              Vars.ui.showInfoToast(AutoUnitUi.noClip(), 2.0F);
              return;
            } 
            if (LunariansSettings.unitSlots.size >= 10) {
              Vars.ui.showInfoToast("Max slots", 2.0F);
              return;
            } 
            LunariansSettings.unitSlots.add(AutoUnitUi.clipboard.copy());
            LunariansSettings.save();
            Vars.ui.showInfoToast(AutoUnitUi.pasted(), 2.0F);
            AutoUnitUi.this.rebuild();
          }
        }).width(90.0F).height(36.0F).pad(2.0F).get();
    textButton.getLabel().setFontScale(0.85F);
    textButton.setDisabled((clipboard == null));
    this.list.add((Element)table).left().padTop(2.0F).row();
  }
  
  private Table card(final UnitSlot slot) {
    Table table1 = new Table(Styles.black6);
    table1.defaults().growX();
    Table table2 = new Table();
    UnitType unitType = slot.unit.isEmpty() ? null : Vars.content.unit(slot.unit);
    ImageButton imageButton = new ImageButton((unitType != null) ? (Drawable)new TextureRegionDrawable(unitType.uiIcon) : (Drawable)Icon.add, Styles.defaulti);
    imageButton.clicked(new Runnable() {
          public void run() {
            (new UnitPickerDialog(new Cons<UnitType>() {
                  public void get(UnitType param2UnitType) {
                    if (!param2UnitType.name.equals(slot.unit)) {
                      slot.unit = param2UnitType.name;
                      slot.command = "";
                      slot.items.clear();
                    } 
                    LunariansSettings.save();
                    AutoUnitUi.this.rebuild();
                  }
                })).show();
          }
        });
    table2.add((Element)imageButton).size(56.0F).padRight(8.0F);
    table2.add((unitType != null) ? unitType.localizedName : LunariansI18n.get("au_pick_unit")).wrap().growX().left();
    TextButton textButton = (TextButton)table2.button(copyLabel(), new Runnable() {
          public void run() {
            AutoUnitUi.clipboard = slot.copy();
            Vars.ui.showInfoToast(AutoUnitUi.copied(), 2.0F);
            AutoUnitUi.this.rebuild();
          }
        }).width(64.0F).height(32.0F).padLeft(4.0F).get();
    textButton.getLabel().setFontScale(0.8F);
    table2.button((Drawable)Icon.trash, Styles.defaulti, new Runnable() {
          public void run() {
            LunariansSettings.unitSlots.remove(slot);
            LunariansSettings.save();
            AutoUnitUi.this.rebuild();
          }
        }).size(48.0F);
    table1.add((Element)table2).growX().row();
    Table table3 = new Table();
    table3.add("pt".equals(LunariansSettings.language) ? "Máx:" : "Max:").left().padRight(4.0F).fontScale(0.85F);
    table3.button("-", new Runnable() {
          public void run() {
            if (slot.maxUnits > 0) {
              slot.maxUnits--;
              LunariansSettings.save();
              AutoUnitUi.this.rebuild();
            } 
          }
        }).size(32.0F, 28.0F);
    table3.label(new Prov<CharSequence>() {
          public CharSequence get() {
            return (slot.maxUnits <= 0) ? ("pt".equals(LunariansSettings.language) ? "Todas" : "All") : String.valueOf(slot.maxUnits);
          }
        }).width(40.0F).center();
    table3.button("+", new Runnable() {
          public void run() {
            if (slot.maxUnits < 50) {
              slot.maxUnits++;
              LunariansSettings.save();
              AutoUnitUi.this.rebuild();
            } 
          }
        }).size(32.0F, 28.0F);
    table1.add((Element)table3).left().row();
    if (unitType != null && unitType.commands != null) {
      table1.add(LunariansI18n.get("au_command")).color(Pal.accent).left().row();
      Table table = new Table();
      for (byte b = 0; b < unitType.commands.size; b++) {
        final UnitCommand c = (UnitCommand)unitType.commands.get(b);
        TextButton textButton1 = (TextButton)table.button(unitCommand.localized(), new Runnable() {
              public void run() {
                slot.command = c.name;
                if (c != UnitCommand.mineCommand)
                  slot.items.clear(); 
                LunariansSettings.save();
                AutoUnitUi.this.rebuild();
              }
            }).height(36.0F).pad(2.0F).get();
        textButton1.setChecked(unitCommand.name.equals(slot.command));
      } 
      table1.add((Element)table).growX().left().row();
    } 
    if (unitType != null && UnitCommand.mineCommand.name.equals(slot.command)) {
      table1.add(LunariansI18n.get("au_items")).color(Pal.accent).left().row();
      Table table = new Table();
      byte b1 = 0;
      Seq seq = Vars.content.items();
      for (byte b2 = 0; b2 < seq.size; b2++) {
        final Item item = (Item)seq.get(b2);
        if (item != null && (
          unitType.mineTier < 0 || item.hardness <= unitType.mineTier)) {
          final Button btn = new Button((Button.ButtonStyle)Styles.togglet);
          button.setProgrammaticChangeEvents(false);
          button.image((Drawable)new TextureRegionDrawable(item.uiIcon)).size(28.0F).row();
          button.add(item.localizedName).fontScale(0.7F).width(70.0F);
          button.setChecked(slot.items.contains(item.name));
          button.clicked(new Runnable() {
                public void run() {
                  if (btn.isChecked()) {
                    if (!slot.items.contains(item.name))
                      slot.items.add(item.name); 
                  } else {
                    slot.items.remove(item.name);
                  } 
                  LunariansSettings.save();
                }
              });
          table.add((Element)button).size(80.0F).pad(2.0F);
          if (++b1 >= 4) {
            table.row();
            b1 = 0;
          } 
        } 
      } 
      table1.add((Element)table).left().row();
    } 
    return table1;
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
