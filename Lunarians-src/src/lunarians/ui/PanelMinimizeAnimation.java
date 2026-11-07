package lunarians.ui;

import arc.math.Interp;
import arc.scene.actions.Actions;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import lunarians.LunariansSettings;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/PanelMinimizeAnimation.class */
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
            boolean z = !LunariansSettings.panelMinimized;
            final boolean z2 = z;
            LunariansSettings.panelMinimized = z;
            LunariansSettings.save();
            final Table body = schematicPanel.getBody();
            if (body != null) {
                body.visible = true;
            }
            float height = schematicPanel.getHeight();
            float panelHeight = z2 ? HEADER_HEIGHT : PanelLayout.panelHeight();
            float f = (schematicPanel.y + height) - panelHeight;
            schematicPanel.clearActions();
            schematicPanel.addAction(Actions.sequence(Actions.parallel(Actions.sizeTo(schematicPanel.getWidth(), panelHeight, DURATION, Interp.smooth), Actions.moveTo(schematicPanel.x, f, DURATION, Interp.smooth)), Actions.run(new Runnable() { // from class: lunarians.ui.PanelMinimizeAnimation.1
                @Override // java.lang.Runnable
                public void run() {
                    if (body != null) {
                        body.visible = !z2;
                    }
                    schematicPanel.invalidateHierarchy();
                    LunariansSettings.panelX = schematicPanel.x;
                    LunariansSettings.panelY = schematicPanel.y;
                    LunariansSettings.save();
                }
            })));
        } catch (Throwable th) {
            Log.err("[Lunarians] minimize animation failed", th);
            schematicPanel.applySettings();
        }
    }
}
