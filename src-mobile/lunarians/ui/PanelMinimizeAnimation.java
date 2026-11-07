package lunarians.ui;

import arc.math.Interp;
import arc.scene.Action;
import arc.scene.actions.Actions;
import arc.scene.actions.MoveToAction;
import arc.scene.actions.ParallelAction;
import arc.scene.actions.SizeToAction;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import lunarians.LunariansSettings;

public final class PanelMinimizeAnimation {
  private static final float DURATION = 0.24F;
  
  private static final float HEADER_HEIGHT = 40.0F;
  
  public static void toggle(SchematicPanel paramSchematicPanel) {
    if (paramSchematicPanel == null)
      return; 
    try {
      float f2;
      boolean bool = LunariansSettings.panelMinimized;
      int i = bool ^ true;
      LunariansSettings.panelMinimized = i;
      LunariansSettings.save();
      Table table = paramSchematicPanel.getBody();
      if (table != null) {
        boolean bool1 = true;
        float f = Float.MIN_VALUE;
        table.visible = bool1;
      } 
      float f1 = paramSchematicPanel.getHeight();
      if (!bool) {
        int j = 1109393408;
        f2 = 40.0F;
      } else {
        f2 = PanelLayout.panelHeight();
      } 
      float f3 = paramSchematicPanel.y + f1 - f2;
      paramSchematicPanel.clearActions();
      f1 = paramSchematicPanel.getWidth();
      Interp interp = Interp.smooth;
      float f4 = 0.24F;
      SizeToAction sizeToAction = Actions.sizeTo(f1, f2, f4, interp);
      f1 = paramSchematicPanel.x;
      interp = Interp.smooth;
      MoveToAction moveToAction = Actions.moveTo(f1, f3, f4, interp);
      ParallelAction parallelAction = Actions.parallel((Action)sizeToAction, (Action)moveToAction);
      PanelMinimizeAnimation$1 panelMinimizeAnimation$1 = new PanelMinimizeAnimation$1();
      this(table, i, paramSchematicPanel);
      return;
    } finally {
      Exception exception = null;
      Log.err("[Lunarians] minimize animation failed", exception);
      paramSchematicPanel.applySettings();
    } 
  }
}
