/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.func.Boolc
 *  arc.func.Cons
 *  arc.func.Floatc
 *  arc.func.Prov
 *  arc.scene.ui.layout.Table
 *  mindustry.graphics.Pal
 *  mindustry.ui.dialogs.BaseDialog
 */
package lunarians.ui;

import arc.func.Boolc;
import arc.func.Cons;
import arc.func.Floatc;
import arc.func.Prov;
import arc.scene.ui.layout.Table;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.ui.UiText;
import mindustry.graphics.Pal;
import mindustry.ui.dialogs.BaseDialog;

public class AutoFillConfigDialog
extends BaseDialog {
    public AutoFillConfigDialog() {
        super(LunariansI18n.get("autofill_config"));
        this.cont.defaults().growX().pad(6.0f);
        this.cont.add((CharSequence)LunariansI18n.get("autofill")).color(Pal.accent).left().row();
        UiText.check(this.cont, LunariansI18n.get("autofill_enable"), LunariansSettings.enableAutoFill, new Boolc(){

            public final void get(boolean bl) {
                LunariansSettings.enableAutoFill = bl;
            }
        }).left().row();
        UiText.check(this.cont, LunariansI18n.get("autofill_turrets"), LunariansSettings.autoFillTurrets, new Boolc(){

            public final void get(boolean bl) {
                LunariansSettings.autoFillTurrets = bl;
            }
        }).left().row();
        UiText.check(this.cont, LunariansI18n.get("autofill_factories"), LunariansSettings.autoFillFactories, new Boolc(){

            public final void get(boolean bl) {
                LunariansSettings.autoFillFactories = bl;
            }
        }).left().row();
        this.sliderRow(this.cont, LunariansI18n.get("autofill_range"), 5.0f, 40.0f, 1.0f, LunariansSettings.autoFillRange, new Floatc(){

            public final void get(float f) {
                LunariansSettings.autoFillRange = f;
            }
        }, (Prov<? extends CharSequence>)new Prov(){

            public final Object get() {
                return AutoFillConfigDialog.lam_new$4();
            }
        });
        this.sliderRow(this.cont, LunariansI18n.get("autofill_interval"), 0.2f, 5.0f, 0.1f, LunariansSettings.autoFillInterval, new Floatc(){

            public final void get(float f) {
                LunariansSettings.autoFillInterval = f;
            }
        }, (Prov<? extends CharSequence>)new Prov(){

            public final Object get() {
                return AutoFillConfigDialog.lam_new$6();
            }
        });
        this.buttons.defaults().size(140.0f, 48.0f).pad(4.0f);
        this.buttons.button(LunariansI18n.get("apply"), new Runnable(){

            @Override
            public final void run() {
                AutoFillConfigDialog.this.lam_new$7();
            }
        });
        this.buttons.button(LunariansI18n.get("back"), new Runnable(){

            @Override
            public final void run() {
                AutoFillConfigDialog.this.hide();
            }
        });
        this.closeOnBack();
    }

    static CharSequence lam_new$4() {
        return (int)LunariansSettings.autoFillRange + "t";
    }

    static CharSequence lam_new$6() {
        return String.format("%.1fs", Float.valueOf(LunariansSettings.autoFillInterval));
    }

    public void lam_new$7() {
        LunariansSettings.save();
        this.hide();
    }

    private void sliderRow(Table table, final String string, final float f, final float f2, final float f3, final float f4, final Floatc floatc, final Prov<? extends CharSequence> prov) {
        table.table(new Cons(){

            public final void get(Object object) {
                AutoFillConfigDialog.lam_sliderRow$9(string, f, f2, f3, f4, floatc, prov, (Table)object);
            }
        }).growX().padBottom(4.0f).row();
    }

    static void lam_sliderRow$9(String string, float f, float f2, float f3, float f4, Floatc floatc, final Prov prov, Table table) {
        table.add((CharSequence)string).width(140.0f).left();
        table.slider(f, f2, f3, f4, floatc).growX();
        table.label(new Prov(){

            public final Object get() {
                return AutoFillConfigDialog.lam_sliderRow$8(prov);
            }
        }).width(50.0f).right().padLeft(6.0f);
    }

    static CharSequence lam_sliderRow$8(Prov prov) {
        return (CharSequence)prov.get();
    }
}

