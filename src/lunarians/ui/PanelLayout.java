package lunarians.ui;

import arc.Core;
import lunarians.LunariansSettings;

public final class PanelLayout {
  public static float panelWidth() {
    float f = clamp(LunariansSettings.panelWidth, 160.0F, 700.0F);
    return (Core.graphics == null) ? f : Math.min(f, Math.max(160.0F, Core.graphics.getWidth()));
  }
  
  public static float panelHeight() {
    float f = clamp(LunariansSettings.panelHeight, 120.0F, 600.0F);
    return (Core.graphics == null) ? f : Math.min(f, Math.max(120.0F, Core.graphics.getHeight()));
  }
  
  public static int columns() {
    int i = Math.max(1, Math.min(8, (int)(Math.max(1.0F, panelWidth() - 24.0F) / (clamp(LunariansSettings.slotSize, 36.0F, 72.0F) + 6.0F))));
    int j = LunariansSettings.columns;
    return (j > 0) ? Math.max(1, Math.min(j, i)) : i;
  }
  
  private static float clamp(float paramFloat1, float paramFloat2, float paramFloat3) {
    return Math.max(paramFloat2, Math.min(paramFloat3, paramFloat1));
  }
}
