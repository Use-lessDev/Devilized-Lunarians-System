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
import mindustry.core.UI;
import mindustry.core.World;
import mindustry.game.EventType;

public final class PositionPicker {
  private static float armedAt;
  
  private static Cons callback;
  
  private static Runnable cancelled;
  
  private static boolean registered;
  
  private static void finish(boolean paramBoolean, int paramInt1, int paramInt2) {
    Cons cons = callback;
    Runnable runnable = cancelled;
    callback = null;
    cancelled = null;
    if (paramBoolean) {
      if (runnable != null) {
        runnable.run();
        return;
      } 
    } else if (cons != null) {
      int[] arrayOfInt = { paramInt1, paramInt2 };
      cons.get(arrayOfInt);
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
      paramRunnable = new PositionPicker$1();
      super();
      Events.run(trigger, paramRunnable);
    } 
    UI uI = Vars.ui;
    String str = LunariansI18n.get("ac_tap_hint");
    uI.showInfoToast(str, 3.0F);
  }
  
  private static void update() {
    int i;
    Cons cons = callback;
    if (cons != null) {
      GameState gameState = Vars.state;
      i = 1;
      float f = Float.MIN_VALUE;
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
                      f = vec2.x;
                      i = World.toTile(f);
                      f1 = vec2.y;
                      int k = World.toTile(f1);
                      finish(false, i, k);
                    } 
                  } 
                } 
                return;
              } 
            } 
          } else {
            return;
          } 
          finish(i, 0, 0);
          return;
        } 
      } 
    } else {
      return;
    } 
    finish(i, 0, 0);
  }
}
