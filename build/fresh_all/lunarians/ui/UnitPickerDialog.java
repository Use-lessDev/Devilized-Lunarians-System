/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.func.Boolf
 *  arc.func.Cons
 *  arc.scene.Element
 *  arc.scene.style.Drawable
 *  arc.scene.style.TextureRegionDrawable
 *  arc.scene.ui.Button
 *  arc.scene.ui.Label
 *  arc.scene.ui.layout.Scl
 *  arc.scene.ui.layout.Table
 *  mindustry.Vars
 *  mindustry.type.UnitType
 *  mindustry.ui.dialogs.BaseDialog
 */
package lunarians.ui;

import arc.Core;
import arc.func.Boolf;
import arc.func.Cons;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Button;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import lunarians.LunariansI18n;
import mindustry.Vars;
import mindustry.type.UnitType;
import mindustry.ui.dialogs.BaseDialog;

public class UnitPickerDialog
extends BaseDialog {
    private final Cons<UnitType> callback;

    public UnitPickerDialog(Cons<UnitType> cons) {
        this(cons, null);
    }

    public UnitPickerDialog(Cons<UnitType> cons, Boolf<UnitType> boolf) {
        super(LunariansI18n.get("au_choose_title"));
        this.callback = cons;
        this.addCloseButton();
        Table table = new Table();
        int n = Math.max(3, Math.min(6, (int)((float)Core.graphics.getWidth() / Scl.scl((float)96.0f))));
        int n2 = 0;
        for (final UnitType unitType : Vars.content.units()) {
            if (unitType.isHidden() || !unitType.unlockedNow() || unitType.commands == null || unitType.commands.size <= 1 || boolf != null && !boolf.get((Object)unitType)) continue;
            table.button((Cons)new Cons<Button>(){

                public void get(Button button) {
                    button.image((Drawable)new TextureRegionDrawable(unitType.uiIcon)).size(40.0f);
                    button.row();
                    Label label = (Label)button.add((CharSequence)unitType.localizedName).wrap().width(80.0f).get();
                    label.setAlignment(1);
                    label.setFontScale(0.8f);
                }
            }, new Runnable(){

                @Override
                public void run() {
                    UnitPickerDialog.this.hide();
                    UnitPickerDialog.this.callback.get((Object)unitType);
                }
            }).size(92.0f, 92.0f).pad(3.0f);
            if (++n2 % n != 0) continue;
            table.row();
        }
        this.cont.pane((Element)table).grow();
    }
}

