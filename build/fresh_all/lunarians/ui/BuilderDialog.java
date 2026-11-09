/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.func.Cons
 *  arc.scene.ui.layout.Table
 */
package lunarians.ui;

import arc.func.Cons;
import arc.scene.ui.layout.Table;
import lunarians.ui.FeatureDialog;

public class BuilderDialog
extends FeatureDialog {
    private final Runnable closed;

    public BuilderDialog(String string, Cons<Table> cons, Runnable runnable) {
        super(string);
        this.closed = runnable;
        cons.get((Object)this.body);
    }

    @Override
    protected void onClosed() {
        if (this.closed != null) {
            this.closed.run();
        }
    }
}

