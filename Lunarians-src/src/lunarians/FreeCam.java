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

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/FreeCam.class */
public class FreeCam {
    private static final Boolp FREECAM_INPUT_LOCK = new Boolp() { // from class: lunarians.FreeCam.1
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
        return active && LunariansSettings.enableFreeCam;
    }

    public static void init() {
        if (Vars.headless) {
            return;
        }
        ensureInputLock();
        Events.run(EventType.Trigger.update, new Runnable() { // from class: lunarians.FreeCam.2
            @Override // java.lang.Runnable
            public final void run() {
                FreeCam.update();
            }
        });
        Events.run(EventType.Trigger.draw, new Runnable() { // from class: lunarians.FreeCam.3
            @Override // java.lang.Runnable
            public final void run() {
                FreeCam.freezeOnDraw();
            }
        });
    }

    public static void setEnabled(boolean z) {
        LunariansSettings.enableFreeCam = z;
        LunariansSettings.save();
        if (z) {
            begin();
        } else {
            stop();
        }
    }

    public static boolean isActive() {
        return active;
    }

    public static void pan(float f, float f2) {
        if (!active || Core.camera == null) {
            return;
        }
        Core.camera.position.add(f, f2);
    }

    public static void update() {
        try {
            doUpdate();
        } catch (Throwable th) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err("[Lunarians] FreeCam error, disabling", th);
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
            if (Vars.player.unit() == null) {
                return;
            }
        }
        moveCameraFromInput();
        freezeUnit();
    }

    private static void moveCameraFromInput() {
        if (Core.camera == null || Core.input == null) {
            return;
        }
        float axis = Core.input.axis(Binding.moveX);
        float axis2 = Core.input.axis(Binding.moveY);
        float f = 1.0f;
        if (axis != 0.0f || axis2 != 0.0f) {
            float max = Math.max(1.0f, Time.delta) * 6.0f;
            Core.camera.position.add(axis * max, axis2 * max);
        }
        boolean isTouched = Core.input.isTouched();
        float mouseX = Core.input.mouseX();
        float mouseY = Core.input.mouseY();
        boolean z = Core.scene != null && Core.scene.hasMouse(mouseX, mouseY);
        if (isTouched && !lastTouched) {
            touchStartedOnUi = z;
            lastTouchX = mouseX;
            lastTouchY = mouseY;
        } else if (isTouched) {
            if (!touchStartedOnUi && !z && !Core.input.isTouched(1)) {
                float f2 = mouseX - lastTouchX;
                float f3 = mouseY - lastTouchY;
                if (Core.graphics != null && Core.graphics.getWidth() > 0) {
                    f = Core.camera.width / Core.graphics.getWidth();
                }
                Core.camera.position.add((-f2) * f, (-f3) * f);
            }
            lastTouchX = mouseX;
            lastTouchY = mouseY;
        } else {
            touchStartedOnUi = false;
        }
        lastTouched = isTouched;
        clampCamera();
    }

    private static void clampCamera() {
        if (Core.camera == null || Vars.world == null || Vars.world.width() <= 0) {
            return;
        }
        Core.camera.position.clamp((-Core.camera.width) / 4.0f, (-Core.camera.height) / 4.0f, Vars.world.unitWidth() + (Core.camera.width / 4.0f), Vars.world.unitHeight() + (Core.camera.height / 4.0f));
    }

    private static void freezeUnit() {
        if (lockedUnit == null) {
            return;
        }
        lockedUnit.x = lockedX;
        lockedUnit.y = lockedY;
        lockedUnit.vel.set(0.0f, 0.0f);
    }

    public static void freezeOnDraw() {
        try {
            doFreezeOnDraw();
        } catch (Throwable th) {
        }
    }

    private static void doFreezeOnDraw() {
        if (!active || !LunariansSettings.enableFreeCam || lockedUnit == null || Vars.player == null || Vars.player.unit() != lockedUnit) {
            return;
        }
        freezeUnit();
    }

    private static void begin() {
        Unit unit = Vars.player == null ? null : Vars.player.unit();
        if (unit != null) {
            lockedUnit = unit;
            lockedX = unit.x;
            lockedY = unit.y;
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
        if (Vars.control == null || Vars.control.input == null) {
            return;
        }
        InputHandler inputHandler = registeredInput;
        InputHandler inputHandler2 = Vars.control.input;
        if (inputHandler != inputHandler2) {
            inputHandler2.inputLocks.add(FREECAM_INPUT_LOCK);
            registeredInput = inputHandler2;
        }
    }
}
