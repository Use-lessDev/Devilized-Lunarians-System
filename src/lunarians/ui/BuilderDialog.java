package lunarians.ui;

import arc.func.Cons;
import arc.scene.ui.layout.Table;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/BuilderDialog.class */
public class BuilderDialog extends FeatureDialog {
    private final Runnable closed;

    public BuilderDialog(String str, Cons<Table> cons, Runnable runnable) {
        super(str);
        this.closed = runnable;
        cons.get(this.body);
    }

    @Override // lunarians.ui.FeatureDialog
    protected void onClosed() {
        if (this.closed != null) {
            this.closed.run();
        }
    }
}
