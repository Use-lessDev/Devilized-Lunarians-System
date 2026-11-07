package lunarians.ui;

import arc.func.Floatc;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import mindustry.graphics.Pal;
import mindustry.ui.dialogs.BaseDialog;

public class AutoFillConfigDialog extends BaseDialog {
  public AutoFillConfigDialog() {
    super(str1);
    this.cont.defaults().growX().pad(6.0F);
    Table table2 = this.cont;
    String str3 = LunariansI18n.get("autofill");
    Cell cell = table2.add(str3);
    Color color = Pal.accent;
    cell.color(color).left().row();
    Table table1 = this.cont;
    String str2 = LunariansI18n.get("autofill_enable");
    boolean bool = LunariansSettings.enableAutoFill;
    AutoFillConfigDialog$1 autoFillConfigDialog$1 = new AutoFillConfigDialog$1();
    this(this);
    UiText.check(table1, str2, bool, autoFillConfigDialog$1).left().row();
    table1 = this.cont;
    str2 = LunariansI18n.get("autofill_turrets");
    bool = LunariansSettings.autoFillTurrets;
    AutoFillConfigDialog$2 autoFillConfigDialog$2 = new AutoFillConfigDialog$2();
    this(this);
    UiText.check(table1, str2, bool, autoFillConfigDialog$2).left().row();
    table1 = this.cont;
    str2 = LunariansI18n.get("autofill_factories");
    bool = LunariansSettings.autoFillFactories;
    AutoFillConfigDialog$3 autoFillConfigDialog$3 = new AutoFillConfigDialog$3();
    this(this);
    UiText.check(table1, str2, bool, autoFillConfigDialog$3).left().row();
    table1 = this.cont;
    str2 = LunariansI18n.get("autofill_range");
    float f = LunariansSettings.autoFillRange;
    AutoFillConfigDialog$4 autoFillConfigDialog$4 = new AutoFillConfigDialog$4();
    this(this);
    AutoFillConfigDialog$5 autoFillConfigDialog$5 = new AutoFillConfigDialog$5();
    this(this);
    sliderRow(table1, str2, 5.0F, 40.0F, 1.0F, f, autoFillConfigDialog$4, autoFillConfigDialog$5);
    table1 = this.cont;
    str2 = LunariansI18n.get("autofill_interval");
    f = LunariansSettings.autoFillInterval;
    AutoFillConfigDialog$6 autoFillConfigDialog$6 = new AutoFillConfigDialog$6();
    this(this);
    AutoFillConfigDialog$7 autoFillConfigDialog$7 = new AutoFillConfigDialog$7();
    this(this);
    sliderRow(table1, str2, 0.2F, 5.0F, 0.1F, f, autoFillConfigDialog$6, autoFillConfigDialog$7);
    this.buttons.defaults().size(140.0F, 48.0F).pad(4.0F);
    table1 = this.buttons;
    str2 = LunariansI18n.get("apply");
    AutoFillConfigDialog$8 autoFillConfigDialog$8 = new AutoFillConfigDialog$8();
    this(this);
    table1.button(str2, autoFillConfigDialog$8);
    table1 = this.buttons;
    str2 = LunariansI18n.get("back");
    AutoFillConfigDialog$9 autoFillConfigDialog$9 = new AutoFillConfigDialog$9();
    this(this);
    table1.button(str2, autoFillConfigDialog$9);
    closeOnBack();
  }
  
  static CharSequence lam_new$4() {
    StringBuilder stringBuilder = new StringBuilder();
    this();
    int i = (int)LunariansSettings.autoFillRange;
    stringBuilder.append(i);
    stringBuilder.append("t");
    return stringBuilder.toString();
  }
  
  static CharSequence lam_new$6() {
    Float float_ = Float.valueOf(LunariansSettings.autoFillInterval);
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = float_;
    return String.format("%.1fs", arrayOfObject);
  }
  
  static CharSequence lam_sliderRow$8(Prov paramProv) {
    return (CharSequence)paramProv.get();
  }
  
  static void lam_sliderRow$9(String paramString, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, Floatc paramFloatc, Prov paramProv, Table paramTable) {
    paramTable.add(paramString).width(140.0F).left();
    paramTable.slider(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloatc).growX();
    AutoFillConfigDialog$11 autoFillConfigDialog$11 = new AutoFillConfigDialog$11();
    this(paramProv);
    paramTable.label(autoFillConfigDialog$11).width(50.0F).right().padLeft(6.0F);
  }
  
  private void sliderRow(Table paramTable, String paramString, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, Floatc paramFloatc, Prov paramProv) {
    AutoFillConfigDialog$10 autoFillConfigDialog$10 = new AutoFillConfigDialog$10();
    this(this, paramString, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloatc, paramProv);
    paramTable.table(autoFillConfigDialog$10).growX().padBottom(4.0F).row();
  }
  
  public void lam_new$7() {
    LunariansSettings.save();
    hide();
  }
}
