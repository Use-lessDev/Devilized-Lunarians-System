/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.math.Interp
 *  arc.scene.Action
 *  arc.scene.actions.Actions
 *  arc.scene.ui.layout.Table
 *  arc.util.Log
 */
package lunarians.ui;

import arc.math.Interp;
import arc.scene.Action;
import arc.scene.actions.Actions;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import lunarians.LunariansSettings;
import lunarians.ui.PanelLayout;
import lunarians.ui.SchematicPanel;

public final class PanelMinimizeAnimation {
    private static final float DURATION = 0.24f;
    private static final float HEADER_HEIGHT = 40.0f;

    private PanelMinimizeAnimation() {
    }

    public static void toggle(final SchematicPanel schematicPanel) {
        if (schematicPanel == null) {
            return;
        }
        try {
            boolean bl = LunariansSettings.panelMinimized;
            final boolean bl2 = !bl;
            LunariansSettings.panelMinimized = bl2;
            LunariansSettings.save();
            final Table table = schematicPanel.getBody();
            if (table != null) {
                table.visible = true;
            }
            float f = schematicPanel.getHeight();
            float f2 = bl2 ? 40.0f : PanelLayout.panelHeight();
            float f3 = schematicPanel.y + f - f2;
            schematicPanel.clearActions();
            schematicPanel.addAction((Action)Actions.sequence((Action)Actions.parallel((Action)Actions.sizeTo((float)schematicPanel.getWidth(), (float)f2, (float)0.24f, (Interp)Interp.smooth), (Action)Actions.moveTo((float)schematicPanel.x, (float)f3, (float)0.24f, (Interp)Interp.smooth)), (Action)Actions.run((Runnable)new Runnable(){

                @Override
                public void run() {
                    if (table != null) {
                        table.visible = !bl2;
                    }
                    schematicPanel.invalidateHierarchy();
                    LunariansSettings.panelX = schematicPanel.x;
                    LunariansSettings.panelY = schematicPanel.y;
                    LunariansSettings.save();
                }
            })));
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] minimize animation failed", (Throwable)throwable);
            schematicPanel.applySettings();
        }
    }
}

