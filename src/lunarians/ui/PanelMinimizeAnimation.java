package lunarians.ui;

import arc.math.Interp;
import arc.scene.Action;
import arc.scene.actions.Actions;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import lunarians.LunariansSettings;

public final class PanelMinimizeAnimation {
  private static final float DURATION = 0.24F;
  
  private static final float HEADER_HEIGHT = 40.0F;
  
  public static void toggle(final SchematicPanel schematicPanel) {
    if (schematicPanel == null)
      return; 
    try {
      boolean bool1 = LunariansSettings.panelMinimized;
      final boolean bl = !bool1 ? true : false;
      LunariansSettings.save();
      final Table table = schematicPanel.getBody();
      if (table != null)
        table.visible = true; 
      float f1 = schematicPanel.getHeight();
      float f2 = bool ? 40.0F : PanelLayout.panelHeight();
      float f3 = schematicPanel.y + f1 - f2;
      schematicPanel.clearActions();
      schematicPanel.addAction((Action)Actions.sequence((Action)Actions.parallel((Action)Actions.sizeTo(schematicPanel.getWidth(), f2, 0.24F, Interp.smooth), (Action)Actions.moveTo(schematicPanel.x, f3, 0.24F, Interp.smooth)), (Action)Actions.run(new Runnable() {
                public void run() {
                  if (table != null)
                    table.visible = !bl; 
                  schematicPanel.invalidateHierarchy();
                  LunariansSettings.panelX = schematicPanel.x;
                  LunariansSettings.panelY = schematicPanel.y;
                  LunariansSettings.save();
                }
              })));
    } catch (Throwable throwable) {
      Log.err("[Lunarians] minimize animation failed", throwable);
      schematicPanel.applySettings();
    } 
  }
}
