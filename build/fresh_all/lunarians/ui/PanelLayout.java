/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 */
package lunarians.ui;

import arc.Core;
import lunarians.LunariansSettings;

public final class PanelLayout {
    private PanelLayout() {
    }

    public static float panelWidth() {
        float f = PanelLayout.clamp(LunariansSettings.panelWidth, 160.0f, 700.0f);
        return Core.graphics == null ? f : Math.min(f, Math.max(160.0f, (float)Core.graphics.getWidth()));
    }

    public static float panelHeight() {
        float f = PanelLayout.clamp(LunariansSettings.panelHeight, 120.0f, 600.0f);
        return Core.graphics == null ? f : Math.min(f, Math.max(120.0f, (float)Core.graphics.getHeight()));
    }

    public static int columns() {
        int n = Math.max(1, Math.min(8, (int)(Math.max(1.0f, PanelLayout.panelWidth() - 24.0f) / (PanelLayout.clamp(LunariansSettings.slotSize, 36.0f, 72.0f) + 6.0f))));
        int n2 = LunariansSettings.columns;
        return n2 > 0 ? Math.max(1, Math.min(n2, n)) : n;
    }

    private static float clamp(float f, float f2, float f3) {
        return Math.max(f2, Math.min(f3, f));
    }
}

