package lunarians.ui;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.input.KeyCode;
import arc.math.geom.Vec2;
import arc.util.Time;
import lunarians.LunariansI18n;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.gen.Unit;

public final class UnitOnMapPicker {
  private static Cons<Unit> callback;
  
  private static Runnable cancelled;
  
  private static float armedAt;
  
  private static boolean registered;
  
  private static final float PICK_RANGE = 48.0F;
  
  public static void pick(Cons<Unit> paramCons, Runnable paramRunnable) {
    callback = paramCons;
    cancelled = paramRunnable;
    armedAt = Time.time;
    if (!registered) {
      registered = true;
      Events.run(EventType.Trigger.update, new Runnable() {
            public void run() {
              UnitOnMapPicker.update();
            }
          });
    } 
    String str = LunariansI18n.get("uom_hint");
    Vars.ui.showInfoToast(str, 3.0F);
  }
  
  private static void update() {
    if (callback == null)
      return; 
    if (Vars.state == null || !Vars.state.isGame()) {
      finish(true, null);
      return;
    } 
    if (Time.time - armedAt < 20.0F)
      return; 
    if (Core.input.keyTap(KeyCode.escape) || Core.input.keyTap(KeyCode.back)) {
      finish(true, null);
      return;
    } 
    if (Core.input.justTouched() && !Core.scene.hasMouse() && !Core.scene.hasDialog()) {
      Vec2 vec2 = Core.input.mouseWorld();
      Unit unit = null;
      float f = 48.0F;
      int i = Groups.unit.size();
      for (byte b = 0; b < i; b++) {
        Unit unit1 = (Unit)Groups.unit.index(b);
        float f1;
        if (unit1 != null && unit1.isValid() && (f1 = unit1.dst(vec2.x, vec2.y)) < f) {
          f = f1;
          unit = unit1;
        } 
      } 
      if (unit != null)
        finish(false, unit); 
    } 
  }
  
  private static void finish(boolean paramBoolean, Unit paramUnit) {
    Cons<Unit> cons = callback;
    Runnable runnable = cancelled;
    callback = null;
    cancelled = null;
    if (paramBoolean) {
      if (runnable != null)
        runnable.run(); 
    } else if (cons != null && paramUnit != null) {
      cons.get(paramUnit);
    } 
  }
}
