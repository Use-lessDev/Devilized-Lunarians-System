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

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoFillConfigDialog.class */
public class AutoFillConfigDialog extends BaseDialog {
    public AutoFillConfigDialog() {
        super(LunariansI18n.get("autofill_config"));
        this.cont.defaults().growX().pad(6.0f);
        this.cont.add(LunariansI18n.get("autofill")).color(Pal.accent).left().row();
        UiText.check(this.cont, LunariansI18n.get("autofill_enable"), LunariansSettings.enableAutoFill, new Boolc() { // from class: lunarians.ui.AutoFillConfigDialog.1
            public final void get(boolean z) {
                LunariansSettings.enableAutoFill = z;
            }
        }).left().row();
        UiText.check(this.cont, LunariansI18n.get("autofill_turrets"), LunariansSettings.autoFillTurrets, new Boolc() { // from class: lunarians.ui.AutoFillConfigDialog.2
            public final void get(boolean z) {
                LunariansSettings.autoFillTurrets = z;
            }
        }).left().row();
        UiText.check(this.cont, LunariansI18n.get("autofill_factories"), LunariansSettings.autoFillFactories, new Boolc() { // from class: lunarians.ui.AutoFillConfigDialog.3
            public final void get(boolean z) {
                LunariansSettings.autoFillFactories = z;
            }
        }).left().row();
        sliderRow(this.cont, LunariansI18n.get("autofill_range"), 5.0f, 40.0f, 1.0f, LunariansSettings.autoFillRange, new Floatc() { // from class: lunarians.ui.AutoFillConfigDialog.4
            public final void get(float f) {
                LunariansSettings.autoFillRange = f;
            }
        }, new Prov() { // from class: lunarians.ui.AutoFillConfigDialog.5
            public final Object get() {
                return AutoFillConfigDialog.lam_new$4();
            }
        });
        sliderRow(this.cont, LunariansI18n.get("autofill_interval"), 0.2f, 5.0f, 0.1f, LunariansSettings.autoFillInterval, new Floatc() { // from class: lunarians.ui.AutoFillConfigDialog.6
            public final void get(float f) {
                LunariansSettings.autoFillInterval = f;
            }
        }, new Prov() { // from class: lunarians.ui.AutoFillConfigDialog.7
            public final Object get() {
                return AutoFillConfigDialog.lam_new$6();
            }
        });
        this.buttons.defaults().size(140.0f, 48.0f).pad(4.0f);
        this.buttons.button(LunariansI18n.get("apply"), new Runnable() { // from class: lunarians.ui.AutoFillConfigDialog.8
            @Override // java.lang.Runnable
            public final void run() {
                AutoFillConfigDialog.this.lam_new$7();
            }
        });
        this.buttons.button(LunariansI18n.get("back"), new Runnable() { // from class: lunarians.ui.AutoFillConfigDialog.9
            @Override // java.lang.Runnable
            public final void run() {
                AutoFillConfigDialog.this.hide();
            }
        });
        closeOnBack();
    }

    static CharSequence lam_new$4() {
        return ((int) LunariansSettings.autoFillRange) + "t";
    }

    static CharSequence lam_new$6() {
        return String.format("%.1fs", Float.valueOf(LunariansSettings.autoFillInterval));
    }

    public void lam_new$7() {
        LunariansSettings.save();
        hide();
    }

    private void sliderRow(Table table, final String str, final float f, final float f2, final float f3, final float f4, final Floatc floatc, final Prov<? extends CharSequence> prov) {
        table.table(new Cons() { // from class: lunarians.ui.AutoFillConfigDialog.10
            public final void get(Object obj) {
                AutoFillConfigDialog.lam_sliderRow$9(str, f, f2, f3, f4, floatc, prov, (Table) obj);
            }
        }).growX().padBottom(4.0f).row();
    }

    static void lam_sliderRow$9(String str, float f, float f2, float f3, float f4, Floatc floatc, final Prov prov, Table table) {
        table.add(str).width(140.0f).left();
        table.slider(f, f2, f3, f4, floatc).growX();
        table.label(new Prov() { // from class: lunarians.ui.AutoFillConfigDialog.11
            public final Object get() {
                return AutoFillConfigDialog.lam_sliderRow$8(prov);
            }
        }).width(50.0f).right().padLeft(6.0f);
    }

    static CharSequence lam_sliderRow$8(Prov prov) {
        return (CharSequence) prov.get();
    }
}
