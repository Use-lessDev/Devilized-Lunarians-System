package lunarians.ui;

import arc.Core;
import arc.Events;
import arc.Input;
import arc.func.Cons;
import arc.input.KeyCode;
import arc.math.geom.Vec2;
import arc.scene.Scene;
import arc.util.Time;
import lunarians.LunariansI18n;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.entities.EntityGroup;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.gen.Unit;

public final class UnitOnMapPicker {
  private static final float PICK_RANGE = 48.0F;
  
  private static float armedAt;
  
  private static Cons callback;
  
  private static Runnable cancelled;
  
  private static boolean registered;
  
  private static void finish(boolean paramBoolean, Unit paramUnit) {
    Cons cons = callback;
    Runnable runnable = cancelled;
    callback = null;
    cancelled = null;
    if (paramBoolean) {
      if (runnable != null) {
        runnable.run();
        return;
      } 
    } else if (cons != null && paramUnit != null) {
      cons.get(paramUnit);
    } 
  }
  
  public static void pick(Cons paramCons, Runnable paramRunnable) {
    callback = paramCons;
    cancelled = paramRunnable;
    float f = Time.time;
    armedAt = f;
    boolean bool = registered;
    if (!bool) {
      bool = true;
      f = Float.MIN_VALUE;
      registered = bool;
      EventType.Trigger trigger = EventType.Trigger.update;
      paramRunnable = new UnitOnMapPicker$1();
      super();
      Events.run(trigger, paramRunnable);
    } 
    String str = LunariansI18n.get("uom_hint");
    Vars.ui.showInfoToast(str, 3.0F);
  }
  
  private static void update() {
    int i;
    Cons cons = callback;
    if (cons != null) {
      GameState gameState = Vars.state;
      Unit unit = null;
      i = 1;
      if (gameState != null) {
        gameState = Vars.state;
        boolean bool = gameState.isGame();
        if (bool) {
          float f1 = Time.time;
          float f2 = armedAt;
          f1 -= f2;
          f2 = 20.0F;
          int j = f1 cmp f2;
          if (j >= 0) {
            Input input = Core.input;
            KeyCode keyCode = KeyCode.escape;
            boolean bool1 = input.keyTap(keyCode);
            if (!bool1) {
              input = Core.input;
              keyCode = KeyCode.back;
              bool1 = input.keyTap(keyCode);
              if (!bool1) {
                input = Core.input;
                bool1 = input.justTouched();
                if (bool1) {
                  Scene scene = Core.scene;
                  bool1 = scene.hasMouse();
                  if (!bool1) {
                    scene = Core.scene;
                    bool1 = scene.hasDialog();
                    if (!bool1) {
                      Vec2 vec2 = Core.input.mouseWorld();
                      EntityGroup entityGroup = Groups.unit;
                      i = entityGroup.size();
                      f2 = 0.0F;
                      keyCode = null;
                      float f = 48.0F;
                      int k;
                      for (k = 0; k < i; k++) {
                        Unit unit1 = (Unit)Groups.unit.index(k);
                        if (unit1 != null) {
                          boolean bool2 = unit1.isValid();
                          if (bool2) {
                            float f3 = vec2.x;
                            float f4 = vec2.y;
                            f3 = unit1.dst(f3, f4);
                            int m = f3 cmp f;
                            if (m < 0) {
                              unit = unit1;
                              f = f3;
                            } 
                          } 
                        } 
                      } 
                      if (unit != null)
                        finish(false, unit); 
                    } 
                  } 
                } 
                return;
              } 
            } 
          } else {
            return;
          } 
          finish(i, null);
          return;
        } 
      } 
    } else {
      return;
    } 
    finish(i, null);
  }
}
