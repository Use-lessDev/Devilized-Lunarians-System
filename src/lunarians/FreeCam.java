package lunarians;

import arc.Core;
import arc.Events;
import arc.func.Boolp;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Unit;
import mindustry.input.Binding;
import mindustry.input.InputHandler;

public class FreeCam {
  private static final Boolp FREECAM_INPUT_LOCK = new Boolp() {
      public final boolean get() {
        return FreeCam.lam_static$0();
      }
    };
  
  private static boolean active;
  
  private static float lastTouchX;
  
  private static float lastTouchY;
  
  private static boolean lastTouched;
  
  private static Unit lockedUnit;
  
  private static float lockedX;
  
  private static float lockedY;
  
  private static InputHandler registeredInput;
  
  private static boolean touchStartedOnUi;
  
  private static boolean errorLogged;
  
  static boolean lam_static$0() {
    return (active && LunariansSettings.enableFreeCam);
  }
  
  public static void init() {
    if (Vars.headless)
      return; 
    ensureInputLock();
    Events.run(EventType.Trigger.update, new Runnable() {
          public final void run() {
            FreeCam.update();
          }
        });
    Events.run(EventType.Trigger.draw, new Runnable() {
          public final void run() {
            FreeCam.freezeOnDraw();
          }
        });
  }
  
  public static void setEnabled(boolean paramBoolean) {
    LunariansSettings.enableFreeCam = paramBoolean;
    LunariansSettings.save();
    if (paramBoolean) {
      begin();
    } else {
      stop();
    } 
  }
  
  public static boolean isActive() {
    return active;
  }
  
  public static void pan(float paramFloat1, float paramFloat2) {
    if (!active || Core.camera == null)
      return; 
    Core.camera.position.add(paramFloat1, paramFloat2);
  }
  
  public static void update() {
    try {
      doUpdate();
    } catch (Throwable throwable) {
      if (!errorLogged) {
        errorLogged = true;
        Log.err("[Lunarians] FreeCam error, disabling", throwable);
      } 
      LunariansSettings.enableFreeCam = false;
      stop();
    } 
  }
  
  private static void doUpdate() {
    ensureInputLock();
    if (Vars.state == null || !Vars.state.isGame() || Vars.player == null || Vars.player.unit() == null) {
      stop();
      return;
    } 
    if (!LunariansSettings.enableFreeCam) {
      stop();
      return;
    } 
    Unit unit = Vars.player.unit();
    if (!active || lockedUnit != unit) {
      begin();
      if (Vars.player.unit() == null)
        return; 
    } 
    moveCameraFromInput();
    freezeUnit();
  }
  
  private static void moveCameraFromInput() {
    if (Core.camera == null || Core.input == null)
      return; 
    float f1 = Core.input.axis(Binding.moveX);
    float f2 = Core.input.axis(Binding.moveY);
    float f3 = 1.0F;
    if (f1 != 0.0F || f2 != 0.0F) {
      float f = Math.max(1.0F, Time.delta) * 6.0F;
      Core.camera.position.add(f1 * f, f2 * f);
    } 
    boolean bool = Core.input.isTouched();
    float f4 = Core.input.mouseX();
    float f5 = Core.input.mouseY();
    boolean bool1 = (Core.scene != null && Core.scene.hasMouse(f4, f5)) ? true : false, bool2 = bool1;
    if (bool && !lastTouched) {
      touchStartedOnUi = bool1;
      lastTouchX = f4;
      lastTouchY = f5;
    } else if (bool) {
      if (!touchStartedOnUi && !bool1 && !Core.input.isTouched(1)) {
        float f6 = f4 - lastTouchX;
        float f7 = f5 - lastTouchY;
        if (Core.graphics != null && Core.graphics.getWidth() > 0)
          f3 = Core.camera.width / Core.graphics.getWidth(); 
        Core.camera.position.add(-f6 * f3, -f7 * f3);
      } 
      lastTouchX = f4;
      lastTouchY = f5;
    } else {
      touchStartedOnUi = false;
    } 
    lastTouched = bool;
    clampCamera();
  }
  
  private static void clampCamera() {
    if (Core.camera == null || Vars.world == null || Vars.world.width() <= 0)
      return; 
    Core.camera.position.clamp(-Core.camera.width / 4.0F, -Core.camera.height / 4.0F, Vars.world.unitWidth() + Core.camera.width / 4.0F, Vars.world.unitHeight() + Core.camera.height / 4.0F);
  }
  
  private static void freezeUnit() {
    if (lockedUnit == null)
      return; 
    lockedUnit.x = lockedX;
    lockedUnit.y = lockedY;
    lockedUnit.vel.set(0.0F, 0.0F);
  }
  
  public static void freezeOnDraw() {
    try {
      doFreezeOnDraw();
    } catch (Throwable throwable) {}
  }
  
  private static void doFreezeOnDraw() {
    if (!active || !LunariansSettings.enableFreeCam || lockedUnit == null || Vars.player == null || Vars.player.unit() != lockedUnit)
      return; 
    freezeUnit();
  }
  
  private static void begin() {
    Unit unit1 = (Vars.player == null) ? null : Vars.player.unit(), unit2 = unit1;
    if (unit1 != null) {
      lockedUnit = unit1;
      lockedX = unit1.x;
      lockedY = unit1.y;
      active = true;
    } else {
      lockedUnit = null;
      active = false;
    } 
    lastTouched = false;
    touchStartedOnUi = false;
  }
  
  private static void stop() {
    active = false;
    lockedUnit = null;
    lastTouched = false;
    touchStartedOnUi = false;
  }
  
  private static void ensureInputLock() {
    InputHandler inputHandler;
    if (Vars.control != null && Vars.control.input != null && registeredInput != (inputHandler = Vars.control.input)) {
      inputHandler.inputLocks.add(FREECAM_INPUT_LOCK);
      registeredInput = inputHandler;
    } 
  }
}
