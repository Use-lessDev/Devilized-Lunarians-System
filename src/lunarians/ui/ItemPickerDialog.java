/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.func.Cons
 *  arc.scene.Element
 *  arc.scene.style.Drawable
 *  arc.scene.style.TextureRegionDrawable
 *  arc.scene.ui.Button
 *  arc.scene.ui.Label
 *  arc.scene.ui.layout.Scl
 *  arc.scene.ui.layout.Table
 *  mindustry.Vars
 *  mindustry.type.Item
 *  mindustry.ui.dialogs.BaseDialog
 */
package lunarians.ui;

import arc.Core;
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
import mindustry.type.Item;
import mindustry.ui.dialogs.BaseDialog;

public class ItemPickerDialog
extends BaseDialog {
    private final Cons<Item> callback;

    public ItemPickerDialog(Cons<Item> cons) {
        super(LunariansI18n.get("loss_choose_title"));
        this.callback = cons;
        this.addCloseButton();
        this.build();
    }

    private void build() {
        Table table = new Table();
        int n = Math.max(3, Math.min(6, (int)((float)Core.graphics.getWidth() / Scl.scl((float)96.0f))));
        int n2 = 0;
        for (final Item item : Vars.content.items()) {
            if (item.isHidden() || !item.unlockedNow()) continue;
            table.button((Cons)new Cons<Button>(){

                public void get(Button button) {
                    button.image((Drawable)new TextureRegionDrawable(item.uiIcon)).size(40.0f);
                    button.row();
                    Label label = (Label)button.add((CharSequence)item.localizedName).wrap().width(80.0f).get();
                    label.setAlignment(1);
                    label.setFontScale(0.8f);
                }
            }, new Runnable(){

                @Override
                public void run() {
                    ItemPickerDialog.this.hide();
                    ItemPickerDialog.this.callback.get((Object)item);
                }
            }).size(92.0f, 92.0f).pad(3.0f);
            if (++n2 % n != 0) continue;
            table.row();
        }
        this.cont.pane((Element)table).grow();
    }
}

