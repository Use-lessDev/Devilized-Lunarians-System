package lunarians.ui;

import arc.Core;
import arc.func.Boolc;
import arc.func.Cons;
import arc.func.Floatc;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.ResourceLossWarn;
import lunarians.data.LossSlot;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.ui.Styles;

public final class LossWarnUi {
  private static final String[] PANEL_KEYS = new String[] { "loss_p1", "loss_p2", "loss_p3", "loss_p4" };
  
  public static void build(Table paramTable) {
    UiText.check(paramTable, LunariansI18n.get("loss"), LunariansSettings.enableLossWarn, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableLossWarn = param1Boolean;
            ResourceLossWarn.reset();
            LunariansSettings.save();
          }
        }).padTop(6.0F).row();
    paramTable.add(LunariansI18n.get("loss_hint")).wrap().growX().color(Color.lightGray).padBottom(6.0F).row();
    Table table1 = new Table();
    Table table2 = new Table();
    buildTabs(table1, table2);
    paramTable.add((Element)table1).growX().row();
    paramTable.add((Element)table2).growX().row();
    rebuild(table2);
  }
  
  private static int tabCols() {
    return (Core.graphics.getWidth() >= 640) ? 4 : 2;
  }
  
  private static void buildTabs(final Table table, final Table table2) {
    table.clear();
    table.defaults().height(42.0F).growX().pad(3.0F).uniformX();
    for (byte b = 0; b < PANEL_KEYS.length; b++) {
      final byte n = b;
      TextButton textButton = new TextButton(LunariansI18n.get(PANEL_KEYS[b]), Styles.togglet);
      textButton.setProgrammaticChangeEvents(false);
      textButton.setChecked((LunariansSettings.lossPanel == b));
      textButton.getLabel().setWrap(true);
      textButton.getLabel().setAlignment(1);
      textButton.clicked(new Runnable() {
            public void run() {
              if (LunariansSettings.lossPanel != n) {
                LunariansSettings.lossPanel = n;
                ResourceLossWarn.reset();
                LunariansSettings.save();
              } 
              LossWarnUi.buildTabs(table, table2);
              LossWarnUi.rebuild(table2);
            }
          });
      table.add((Element)textButton);
      if (b % tabCols() == tabCols() - 1)
        table.row(); 
    } 
  }
  
  private static void rebuild(final Table table) {
    table.clear();
    table.defaults().growX();
    final Seq<LossSlot> seq = LunariansSettings.lossSlots(LunariansSettings.lossPanel);
    if (seq.isEmpty())
      table.add(LunariansI18n.get("loss_empty")).wrap().growX().padBottom(6.0F).row(); 
    for (byte b = 0; b < seq.size; b++)
      table.add((Element)card(table, seq, (LossSlot)seq.get(b))).growX().padBottom(5.0F).row(); 
    if (seq.size < 25)
      table.button((Drawable)Icon.add, Styles.defaulti, new Runnable() {
            public void run() {
              seq.add(new LossSlot());
              LunariansSettings.save();
              LossWarnUi.rebuild(table);
            }
          }).size(52.0F).padBottom(2.0F).row(); 
    table.add(seq.size + "/" + '\031').color(Pal.accent).padBottom(6.0F).row();
    if (LunariansSettings.lossPanel < 3)
      table.button(LunariansI18n.get("loss_presets"), new Runnable() {
            public void run() {
              LunariansSettings.applyLossPreset(LunariansSettings.lossPanel);
              LunariansSettings.save();
              ResourceLossWarn.reset();
              LossWarnUi.rebuild(table);
            }
          }).height(42.0F).growX().padBottom(4.0F).row(); 
  }
  
  private static Table card(final Table table, final Seq<LossSlot> seq, final LossSlot lossSlot) {
    Table table1 = new Table(Styles.black6);
    table1.margin(8.0F);
    table1.defaults().growX().padBottom(4.0F);
    Item item = lossSlot.item.isEmpty() ? null : Vars.content.item(lossSlot.item);
    Table table2 = new Table();
    TextureRegionDrawable textureRegionDrawable = (item != null) ? new TextureRegionDrawable(item.uiIcon) : Icon.add;
    ImageButton imageButton = new ImageButton((Drawable)textureRegionDrawable, Styles.defaulti);
    imageButton.clicked(new Runnable() {
          public void run() {
            (new ItemPickerDialog(new Cons<Item>() {
                  public void get(Item param2Item) {
                    lossSlot.item = param2Item.name;
                    LunariansSettings.save();
                    LossWarnUi.rebuild(table);
                  }
                })).show();
          }
        });
    table2.add((Element)imageButton).size(56.0F).padRight(8.0F);
    table2.add((item != null) ? item.localizedName : LunariansI18n.get("loss_pick_item")).wrap().growX().left();
    table2.button((Drawable)Icon.trash, Styles.defaulti, new Runnable() {
          public void run() {
            seq.remove(lossSlot);
            LunariansSettings.save();
            LossWarnUi.rebuild(table);
          }
        }).size(48.0F).padLeft(6.0F);
    table1.add((Element)table2).growX().row();
    Table table3 = new Table();
    table3.add(LunariansI18n.get("loss_per_sec")).width(110.0F).left().wrap();
    table3.slider(0.5F, 300.0F, 0.5F, Math.min(300.0F, lossSlot.perSec), new Floatc() {
          public void get(float param1Float) {
            lossSlot.perSec = param1Float;
          }
        }).growX().padLeft(4.0F);
    table3.label(new Prov<CharSequence>() {
          public CharSequence get() {
            return LossWarnUi.fmt(lossSlot.perSec) + "/s";
          }
        }).width(64.0F).right().padLeft(6.0F);
    table1.add((Element)table3).growX().row();
    Table table4 = new Table();
    table4.add(LunariansI18n.get("loss_delay")).width(110.0F).left().wrap();
    table4.slider(1.0F, 60.0F, 1.0F, Math.min(60.0F, lossSlot.delay), new Floatc() {
          public void get(float param1Float) {
            lossSlot.delay = param1Float;
          }
        }).growX().padLeft(4.0F);
    table4.label(new Prov<CharSequence>() {
          public CharSequence get() {
            return LossWarnUi.fmt(lossSlot.delay) + "s";
          }
        }).width(64.0F).right().padLeft(6.0F);
    table1.add((Element)table4).growX().row();
    return table1;
  }
  
  private static String fmt(float paramFloat) {
    return (Math.abs(paramFloat - Math.round(paramFloat)) < 0.05F) ? String.valueOf(Math.round(paramFloat)) : String.valueOf(Math.round(paramFloat * 10.0F) / 10.0F);
  }
}
