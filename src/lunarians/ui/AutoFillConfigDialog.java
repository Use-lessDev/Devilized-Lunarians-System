package lunarians.ui;

import arc.func.Boolc;
import arc.func.Cons;
import arc.func.Floatc;
import arc.func.Prov;
import arc.scene.ui.layout.Table;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import mindustry.graphics.Pal;
import mindustry.ui.dialogs.BaseDialog;

public class AutoFillConfigDialog extends BaseDialog {
  public AutoFillConfigDialog() {
    super(LunariansI18n.get("autofill_config"));
    this.cont.defaults().growX().pad(6.0F);
    this.cont.add(LunariansI18n.get("autofill")).color(Pal.accent).left().row();
    UiText.check(this.cont, LunariansI18n.get("autofill_enable"), LunariansSettings.enableAutoFill, new Boolc() {
          public final void get(boolean param1Boolean) {
            LunariansSettings.enableAutoFill = param1Boolean;
          }
        }).left().row();
    UiText.check(this.cont, LunariansI18n.get("autofill_turrets"), LunariansSettings.autoFillTurrets, new Boolc() {
          public final void get(boolean param1Boolean) {
            LunariansSettings.autoFillTurrets = param1Boolean;
          }
        }).left().row();
    UiText.check(this.cont, LunariansI18n.get("autofill_factories"), LunariansSettings.autoFillFactories, new Boolc() {
          public final void get(boolean param1Boolean) {
            LunariansSettings.autoFillFactories = param1Boolean;
          }
        }).left().row();
    sliderRow(this.cont, LunariansI18n.get("autofill_range"), 5.0F, 40.0F, 1.0F, LunariansSettings.autoFillRange, new Floatc() {
          public final void get(float param1Float) {
            LunariansSettings.autoFillRange = param1Float;
          }
        },  new Prov() {
          public final Object get() {
            return AutoFillConfigDialog.lam_new$4();
          }
        });
    sliderRow(this.cont, LunariansI18n.get("autofill_interval"), 0.2F, 5.0F, 0.1F, LunariansSettings.autoFillInterval, new Floatc() {
          public final void get(float param1Float) {
            LunariansSettings.autoFillInterval = param1Float;
          }
        },  new Prov() {
          public final Object get() {
            return AutoFillConfigDialog.lam_new$6();
          }
        });
    this.buttons.defaults().size(140.0F, 48.0F).pad(4.0F);
    this.buttons.button(LunariansI18n.get("apply"), new Runnable() {
          public final void run() {
            AutoFillConfigDialog.this.lam_new$7();
          }
        });
    this.buttons.button(LunariansI18n.get("back"), new Runnable() {
          public final void run() {
            AutoFillConfigDialog.this.hide();
          }
        });
    closeOnBack();
  }
  
  static CharSequence lam_new$4() {
    return (int)LunariansSettings.autoFillRange + "t";
  }
  
  static CharSequence lam_new$6() {
    return String.format("%.1fs", new Object[] { Float.valueOf(LunariansSettings.autoFillInterval) });
  }
  
  public void lam_new$7() {
    LunariansSettings.save();
    hide();
  }
  
  private void sliderRow(Table paramTable, final String string, final float f, final float f2, final float f3, final float f4, final Floatc floatc, final Prov<? extends CharSequence> prov) {
    paramTable.table(new Cons() {
          public final void get(Object param1Object) {
            AutoFillConfigDialog.lam_sliderRow$9(string, f, f2, f3, f4, floatc, prov, (Table)param1Object);
          }
        }).growX().padBottom(4.0F).row();
  }
  
  static void lam_sliderRow$9(String paramString, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, Floatc paramFloatc, final Prov prov, Table paramTable) {
    paramTable.add(paramString).width(140.0F).left();
    paramTable.slider(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloatc).growX();
    paramTable.label(new Prov() {
          public final Object get() {
            return AutoFillConfigDialog.lam_sliderRow$8(prov);
          }
        }).width(50.0F).right().padLeft(6.0F);
  }
  
  static CharSequence lam_sliderRow$8(Prov paramProv) {
    return (CharSequence)paramProv.get();
  }
}
