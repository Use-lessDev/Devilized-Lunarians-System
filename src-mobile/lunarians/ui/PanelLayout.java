package lunarians.ui;

import arc.Core;
import arc.Graphics;
import lunarians.LunariansSettings;

public final class PanelLayout {
  private static float clamp(float paramFloat1, float paramFloat2, float paramFloat3) {
    paramFloat1 = Math.min(paramFloat3, paramFloat1);
    return Math.max(paramFloat2, paramFloat1);
  }
  
  public static int columns() {
    float f1 = panelWidth() - 24.0F;
    f1 = Math.max(1.0F, f1);
    float f2 = LunariansSettings.slotSize;
    float f3 = 72.0F;
    f2 = clamp(f2, 36.0F, f3);
    float f4 = 6.0F;
    f2 += f4;
    f1 /= f2;
    int i = (int)f1;
    i = Math.min(8, i);
    boolean bool = true;
    f2 = Float.MIN_VALUE;
    i = Math.max(bool, i);
    int j = LunariansSettings.columns;
    if (j > 0) {
      i = Math.min(j, i);
      i = Math.max(bool, i);
    } 
    return i;
  }
  
  public static float panelHeight() {
    float f1 = LunariansSettings.panelHeight;
    float f2 = 600.0F;
    float f3 = 120.0F;
    f1 = clamp(f1, f3, f2);
    Graphics graphics = Core.graphics;
    if (graphics == null)
      return f1; 
    f2 = Core.graphics.getHeight();
    f2 = Math.max(f3, f2);
    return Math.min(f1, f2);
  }
  
  public static float panelWidth() {
    float f1 = LunariansSettings.panelWidth;
    float f2 = 700.0F;
    float f3 = 160.0F;
    f1 = clamp(f1, f3, f2);
    Graphics graphics = Core.graphics;
    if (graphics == null)
      return f1; 
    f2 = Core.graphics.getWidth();
    f2 = Math.max(f3, f2);
    return Math.min(f1, f2);
  }
}
