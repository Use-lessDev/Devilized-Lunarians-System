package lunarians.ui;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.input.KeyCode;
import arc.math.geom.Vec2;
import arc.util.Time;
import lunarians.LunariansI18n;
import mindustry.Vars;
import mindustry.core.World;
import mindustry.game.EventType;

public final class PositionPicker {
  private static Cons<int[]> callback;
  
  private static Runnable cancelled;
  
  private static float armedAt;
  
  private static boolean registered;
  
  public static void pick(Cons<int[]> paramCons, Runnable paramRunnable) {
    callback = paramCons;
    cancelled = paramRunnable;
    armedAt = Time.time;
    if (!registered) {
      registered = true;
      Events.run(EventType.Trigger.update, new Runnable() {
            public void run() {
              PositionPicker.update();
            }
          });
    } 
    Vars.ui.showInfoToast(LunariansI18n.get("ac_tap_hint"), 3.0F);
  }
  
  private static void update() {
    if (callback == null)
      return; 
    if (Vars.state == null || !Vars.state.isGame()) {
      finish(true, 0, 0);
      return;
    } 
    if (Time.time - armedAt < 20.0F)
      return; 
    if (Core.input.keyTap(KeyCode.escape) || Core.input.keyTap(KeyCode.back)) {
      finish(true, 0, 0);
      return;
    } 
    if (Core.input.justTouched() && !Core.scene.hasMouse() && !Core.scene.hasDialog()) {
      Vec2 vec2 = Core.input.mouseWorld();
      finish(false, World.toTile(vec2.x), World.toTile(vec2.y));
    } 
  }
  
  private static void finish(boolean paramBoolean, int paramInt1, int paramInt2) {
    Cons<int[]> cons = callback;
    Runnable runnable = cancelled;
    callback = null;
    cancelled = null;
    if (paramBoolean) {
      if (runnable != null)
        runnable.run(); 
    } else if (cons != null) {
      cons.get(new int[] { paramInt1, paramInt2 });
    } 
  }
}
