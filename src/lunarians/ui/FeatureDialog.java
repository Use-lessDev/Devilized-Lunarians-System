/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.scene.Element
 *  arc.scene.ui.layout.Table
 *  mindustry.ui.dialogs.BaseDialog
 */
package lunarians.ui;

import arc.Core;
import arc.scene.Element;
import arc.scene.ui.layout.Table;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import mindustry.ui.dialogs.BaseDialog;

public abstract class FeatureDialog
extends BaseDialog {
    protected final Table body = new Table();
    private Runnable after;
    public static FeatureDialog top;

    public FeatureDialog after(Runnable runnable) {
        this.after = runnable;
        return this;
    }

    protected FeatureDialog(String string) {
        super(string);
        this.body.defaults().growX().padBottom(4.0f);
        this.cont.pane((Element)this.body).grow().maxHeight((float)Core.graphics.getHeight() * 0.78f).width(Math.min((float)Core.graphics.getWidth() * 0.96f, 900.0f));
        this.buttons.defaults().size(150.0f, 48.0f).pad(4.0f);
        this.buttons.button(LunariansI18n.get("back"), new Runnable(){

            @Override
            public void run() {
                FeatureDialog.this.hide();
            }
        });
        this.shown(new Runnable(){

            @Override
            public void run() {
                top = FeatureDialog.this;
            }
        });
        this.closeOnBack();
        this.hidden(new Runnable(){

            @Override
            public void run() {
                if (top == FeatureDialog.this) {
                    top = null;
                }
                LunariansSettings.save();
                FeatureDialog.this.onClosed();
                Runnable runnable = FeatureDialog.this.after;
                FeatureDialog.this.after = null;
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
    }

    protected void onClosed() {
    }
}

