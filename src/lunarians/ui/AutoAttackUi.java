package lunarians.ui;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.AutoAttack;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.data.AttackSlot;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class AutoAttackUi {
  private final Table list = new Table();
  
  private static AttackSlot clipboard = null;
  
  private static BuilderDialog currentDlg;
  
  public static BuilderDialog make0() {
    return new BuilderDialog(title(), paramTable -> new AutoAttackUi(paramTable), () -> {
        
        });
  }
  
  public AutoAttackUi(Table paramTable) {
    paramTable.defaults().growX();
    paramTable.add(hint()).wrap().growX().color(Color.lightGray).padBottom(4.0F).row();
    Table table1 = new Table();
    table1.check(tr("Regroup", "Reagrupar"), AutoAttack.regroupEnabled(), paramBoolean -> AutoAttack.setRegroupEnabled(paramBoolean)).left();
    table1.add(" " + tr("every", "a cada") + " ").fontScale(0.85F);
    table1.button("-", () -> {
          AutoAttack.setRegroupSec(AutoAttack.regroupSec() - 1);
          reopen();
        }).size(32.0F, 28.0F);
    table1.label(() -> AutoAttack.regroupSec() + "s").width(40.0F).center();
    table1.button("+", () -> {
          AutoAttack.setRegroupSec(AutoAttack.regroupSec() + 1);
          reopen();
        }).size(32.0F, 28.0F);
    table1.add(" " + tr("(min 5)", "(min 5)")).color(Color.lightGray).fontScale(0.75F);
    paramTable.add((Element)table1).left().padBottom(6.0F).row();
    Table table2 = new Table();
    table2.check(LunariansI18n.get("atk_heal"), LunariansSettings.autoAttackHeal, paramBoolean -> {
          LunariansSettings.autoAttackHeal = paramBoolean;
          LunariansSettings.save();
        }).left();
    paramTable.add((Element)table2).left().padBottom(4.0F).row();
    paramTable.add((Element)this.list).growX().row();
    rebuild();
  }
  
  private void rebuild() {
    this.list.clearChildren();
    Seq<AttackSlot> seq = LunariansSettings.attackSlots;
    this.list.add(seq.size + "/10").color(Pal.accent).padBottom(4.0F).row();
    if (seq.isEmpty()) {
      this.list.add(emptyMsg()).wrap().growX().padBottom(4.0F).row();
    } else {
      for (byte b = 0; b < seq.size; b++)
        this.list.add((Element)card((AttackSlot)seq.get(b), b)).growX().padBottom(3.0F).row(); 
    } 
    Table table = new Table();
    if (seq.size < 10)
      table.button((Drawable)Icon.add, Styles.defaulti, () -> {
            paramSeq.add(new AttackSlot());
            LunariansSettings.save();
            reopen();
          }).size(40.0F).pad(2.0F); 
    table.button(pasteLabel(), () -> {
          if (clipboard == null) {
            if (Vars.ui != null)
              Vars.ui.showInfoToast(noClip(), 2.0F); 
            return;
          } 
          if (paramSeq.size >= 10)
            return; 
          paramSeq.add(clipboard.copy());
          LunariansSettings.save();
          if (Vars.ui != null)
            Vars.ui.showInfoToast(pasted(), 2.0F); 
          reopen();
        }).width(90.0F).height(36.0F).pad(2.0F);
    this.list.add((Element)table).left().padTop(2.0F).row();
  }
  
  private Table card(AttackSlot paramAttackSlot, int paramInt) {
    Table table1 = new Table();
    table1.setBackground(Styles.black6);
    table1.defaults().growX().pad(4.0F);
    UnitType unitType = (paramAttackSlot.unit == null || paramAttackSlot.unit.isEmpty()) ? null : Vars.content.unit(paramAttackSlot.unit);
    Table table2 = new Table();
    ImageButton imageButton = new ImageButton((unitType != null) ? (Drawable)new TextureRegionDrawable(unitType.uiIcon) : (Drawable)Icon.add, Styles.clearNonei);
    imageButton.resizeImage(32.0F);
    imageButton.clicked(() -> openUnitPicker(paramAttackSlot));
    table2.add((Element)imageButton).size(40.0F).padRight(6.0F);
    table2.add((unitType != null) ? unitType.localizedName : pickUnit()).wrap().growX().left().fontScale(0.9F);
    table2.button(copyLabel(), () -> {
          clipboard = paramAttackSlot.copy();
          if (Vars.ui != null)
            Vars.ui.showInfoToast(copied(), 2.0F); 
        }).width(64.0F).height(32.0F).padLeft(4.0F);
    table2.button((Drawable)Icon.trash, Styles.defaulti, () -> {
          LunariansSettings.attackSlots.remove(paramInt);
          LunariansSettings.save();
          reopen();
        }).size(36.0F).padLeft(4.0F);
    table1.add((Element)table2).growX().row();
    Table table3 = new Table();
    table3.add(maxLabel()).left().padRight(4.0F).fontScale(0.85F);
    table3.button("-", () -> {
          paramAttackSlot.maxUnits = Math.max(0, paramAttackSlot.maxUnits - 1);
          LunariansSettings.save();
          reopen();
        }).size(32.0F, 28.0F);
    table3.label(() -> (paramAttackSlot.maxUnits <= 0) ? all() : String.valueOf(paramAttackSlot.maxUnits)).width(40.0F).center().pad(2.0F);
    table3.button("+", () -> {
          paramAttackSlot.maxUnits = Math.min(99, paramAttackSlot.maxUnits + 1);
          LunariansSettings.save();
          reopen();
        }).size(32.0F, 28.0F);
    table1.add((Element)table3).left().padTop(2.0F).row();
    if (unitType != null) {
      int i = AutoAttack.maxStandoffTiles(unitType);
      Table table = new Table();
      table.add(tr("Attack distance", "Dist. ataque")).left().padRight(4.0F).fontScale(0.85F);
      table.button("-", () -> {
            if (paramAttackSlot.standoffTiles <= 0) {
              paramAttackSlot.standoffTiles = paramInt;
            } else if (paramAttackSlot.standoffTiles <= 1) {
              paramAttackSlot.standoffTiles = 0;
            } else {
              paramAttackSlot.standoffTiles--;
            } 
            LunariansSettings.save();
            reopen();
          }).size(32.0F, 28.0F);
      table.label(() -> (paramAttackSlot.standoffTiles <= 0) ? tr("normal", "normal") : String.valueOf(Mathf.clamp(paramAttackSlot.standoffTiles, 1, paramInt)))

        
        .width(56.0F).center().pad(2.0F);
      table.button("+", () -> {
            if (paramAttackSlot.standoffTiles <= 0) {
              paramAttackSlot.standoffTiles = 1;
            } else {
              paramAttackSlot.standoffTiles = Math.min(paramInt, paramAttackSlot.standoffTiles + 1);
            } 
            LunariansSettings.save();
            reopen();
          }).size(32.0F, 28.0F);
      float f = unitType.maxRange / 8.0F;
      table.add(" " + tr("1-" + i + " (range " + (int)f + "+2)", "1-" + i + " (alcance " + (int)f + "+2)"))
        
        .color(Color.lightGray).fontScale(0.7F).padLeft(4.0F);
      table1.add((Element)table).left().padTop(2.0F).row();
    } 
    boolean bool = paramAttackSlot.hasZone();
    Table table4 = new Table();
    table4.add(LunariansI18n.get("atk_zone", bool ? (paramAttackSlot.x + "," + paramAttackSlot.y + " r" + (int)paramAttackSlot.zoneRadius) : LunariansI18n.get("atk_whole_map")))
      .wrap().growX().left().fontScale(0.85F).color(bool ? Color.white : Color.lightGray);
    table4.button(mapBtn(), () -> {
          if (paramBoolean) {
            paramAttackSlot.x = Integer.MIN_VALUE;
            paramAttackSlot.y = Integer.MIN_VALUE;
            paramAttackSlot.zoneRadius = 0.0F;
          } else if (Vars.player != null && Vars.player.unit() != null) {
            paramAttackSlot.x = Vars.player.unit().tileX();
            paramAttackSlot.y = Vars.player.unit().tileY();
            paramAttackSlot.zoneRadius = Math.max(10.0F, paramAttackSlot.zoneRadius);
          } 
          LunariansSettings.save();
          reopen();
        }).width(90.0F).height(32.0F).padLeft(4.0F);
    table1.add((Element)table4).growX().padTop(2.0F).row();
    if (bool) {
      Table table = new Table();
      table.add(LunariansI18n.get("atk_zone_radius")).left().padRight(4.0F).fontScale(0.85F);
      table.button("-", () -> {
            paramAttackSlot.zoneRadius = Math.max(1.0F, paramAttackSlot.zoneRadius - 5.0F);
            LunariansSettings.save();
            reopen();
          }).size(32.0F, 28.0F);
      table.label(() -> String.valueOf((int)paramAttackSlot.zoneRadius)).width(40.0F).center().pad(2.0F);
      table.button("+", () -> {
            paramAttackSlot.zoneRadius = Math.min(200.0F, paramAttackSlot.zoneRadius + 5.0F);
            LunariansSettings.save();
            reopen();
          }).size(32.0F, 28.0F);
      table1.add((Element)table).left().row();
    } 
    return table1;
  }
  
  private void openUnitPicker(AttackSlot paramAttackSlot) {
    BaseDialog baseDialog = new BaseDialog(pickUnit());
    baseDialog.cont.pane(paramTable -> {
          paramTable.defaults().size(48.0F).pad(3.0F);
          byte b = 0;
          for (UnitType unitType : Vars.content.units()) {
            if (unitType == null || unitType.isHidden() || unitType.uiIcon == null)
              continue; 
            ImageButton imageButton = new ImageButton((Drawable)new TextureRegionDrawable(unitType.uiIcon), Styles.clearNonei);
            imageButton.resizeImage(40.0F);
            imageButton.clicked(());
            paramTable.add((Element)imageButton).size(48.0F);
            if (++b % 6 == 0)
              paramTable.row(); 
          } 
        }).maxHeight(360.0F).growX();
    baseDialog.addCloseButton();
    baseDialog.show();
  }
  
  private static String tr(String paramString1, String paramString2) {
    String str = LunariansI18n.getLanguage();
    if ("pt".equals(str))
      return paramString2; 
    if ("es".equals(str))
      return paramString2; 
    if ("ru".equals(str))
      return paramString1; 
    return paramString1;
  }
  
  private static String title() {
    return LunariansI18n.get("auto_attack");
  }
  
  private static String hint() {
    return LunariansI18n.get("atk_hint");
  }
  
  private static String emptyMsg() {
    return LunariansI18n.get("t_noslots");
  }
  
  private static String pickUnit() {
    return LunariansI18n.get("t_choose");
  }
  
  private static String maxLabel() {
    return LunariansI18n.get("t_max");
  }
  
  private static String all() {
    return LunariansI18n.get("t_all");
  }
  
  private static String mapBtn() {
    return LunariansI18n.get("t_map");
  }
  
  private static String copyLabel() {
    return LunariansI18n.get("t_copy");
  }
  
  private static String pasteLabel() {
    return LunariansI18n.get("t_paste");
  }
  
  private static String copied() {
    return LunariansI18n.get("atk_copied");
  }
  
  private static String pasted() {
    return LunariansI18n.get("atk_pasted");
  }
  
  private static String noClip() {
    return LunariansI18n.get("atk_noclip");
  }
  
  public static BuilderDialog make() {
    return currentDlg = make0();
  }
  
  private static void reopen() {
    if (currentDlg != null)
      currentDlg.hide(); 
    currentDlg = make();
  }
}
