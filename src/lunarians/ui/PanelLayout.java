package lunarians.ui;

import arc.Core;
import lunarians.LunariansSettings;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/PanelLayout.class */
public final class PanelLayout {
    private PanelLayout() {
    }

    public static float panelWidth() {
        float clamp = clamp(LunariansSettings.panelWidth, 160.0f, 700.0f);
        return Core.graphics == null ? clamp : Math.min(clamp, Math.max(160.0f, Core.graphics.getWidth()));
    }

    public static float panelHeight() {
        float clamp = clamp(LunariansSettings.panelHeight, 120.0f, 600.0f);
        return Core.graphics == null ? clamp : Math.min(clamp, Math.max(120.0f, Core.graphics.getHeight()));
    }

    public static int columns() {
        int max = Math.max(1, Math.min(8, (int) (Math.max(1.0f, panelWidth() - 24.0f) / (clamp(LunariansSettings.slotSize, 36.0f, 72.0f) + 6.0f))));
        int i = LunariansSettings.columns;
        return i > 0 ? Math.max(1, Math.min(i, max)) : max;
    }

    private static float clamp(float f, float f2, float f3) {
        return Math.max(f2, Math.min(f3, f));
    }
}
