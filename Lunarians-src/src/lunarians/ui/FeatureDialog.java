package lunarians.ui;

import arc.Core;
import arc.scene.ui.layout.Table;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import mindustry.ui.dialogs.BaseDialog;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/FeatureDialog.class */
public abstract class FeatureDialog extends BaseDialog {
    protected final Table body;
    private Runnable after;
    public static FeatureDialog top;

    public FeatureDialog after(Runnable runnable) {
        this.after = runnable;
        return this;
    }

    protected FeatureDialog(String str) {
        super(str);
        this.body = new Table();
        this.body.defaults().growX().padBottom(4.0f);
        this.cont.pane(this.body).grow().maxHeight(Core.graphics.getHeight() * 0.78f).width(Math.min(Core.graphics.getWidth() * 0.96f, 900.0f));
        this.buttons.defaults().size(150.0f, 48.0f).pad(4.0f);
        this.buttons.button(LunariansI18n.get("back"), new Runnable() { // from class: lunarians.ui.FeatureDialog.1
            @Override // java.lang.Runnable
            public void run() {
                FeatureDialog.this.hide();
            }
        });
        shown(new Runnable() { // from class: lunarians.ui.FeatureDialog.2
            @Override // java.lang.Runnable
            public void run() {
                FeatureDialog.top = FeatureDialog.this;
            }
        });
        closeOnBack();
        hidden(new Runnable() { // from class: lunarians.ui.FeatureDialog.3
            @Override // java.lang.Runnable
            public void run() {
                if (FeatureDialog.top == FeatureDialog.this) {
                    FeatureDialog.top = null;
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
