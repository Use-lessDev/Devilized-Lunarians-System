/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.func.Boolp
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.game.EventType$Trigger
 *  mindustry.gen.Unit
 *  mindustry.input.Binding
 *  mindustry.input.InputHandler
 */
package lunarians;

import arc.Core;
import arc.Events;
import arc.func.Boolp;
import arc.util.Log;
import arc.util.Time;
import lunarians.LunariansSettings;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Unit;
import mindustry.input.Binding;
import mindustry.input.InputHandler;

public class FreeCam {
    private static final Boolp FREECAM_INPUT_LOCK = new Boolp(){

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
        FreeCam.ensureInputLock();
        Events.run((Object)EventType.Trigger.update, (Runnable)new Runnable(){

            @Override
            public final void run() {
                FreeCam.update();
            }
        });
        Events.run((Object)EventType.Trigger.draw, (Runnable)new Runnable(){

            @Override
            public final void run() {
                FreeCam.freezeOnDraw();
            }
        });
    }

    public static void setEnabled(boolean bl) {
        LunariansSettings.enableFreeCam = bl;
        LunariansSettings.save();
        if (bl) {
            FreeCam.begin();
        } else {
            FreeCam.stop();
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
            FreeCam.doUpdate();
        }
        catch (Throwable throwable) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err((String)"[Lunarians] FreeCam error, disabling", (Throwable)throwable);
            }
            LunariansSettings.enableFreeCam = false;
            FreeCam.stop();
        }
    }

    private static void doUpdate() {
        FreeCam.ensureInputLock();
        if (Vars.state == null || !Vars.state.isGame() || Vars.player == null || Vars.player.unit() == null) {
            FreeCam.stop();
            return;
        }
        if (!LunariansSettings.enableFreeCam) {
            FreeCam.stop();
            return;
        }
        Unit unit = Vars.player.unit();
        if (!active || lockedUnit != unit) {
            FreeCam.begin();
            if (Vars.player.unit() == null) {
                return;
            }
        }
        FreeCam.moveCameraFromInput();
        FreeCam.freezeUnit();
    }

    private static void moveCameraFromInput() {
        if (Core.camera == null || Core.input == null) {
            return;
        }
        float f = Core.input.axis(Binding.moveX);
        float f2 = Core.input.axis(Binding.moveY);
        float f3 = 1.0f;
        if (f != 0.0f || f2 != 0.0f) {
            float f4 = Math.max(1.0f, Time.delta) * 6.0f;
            Core.camera.position.add(f * f4, f2 * f4);
        }
        boolean bl = Core.input.isTouched();
        float f5 = Core.input.mouseX();
        float f6 = Core.input.mouseY();
        boolean bl2 = Core.scene != null && Core.scene.hasMouse(f5, f6);
        boolean bl3 = bl2;
        if (bl && !lastTouched) {
            touchStartedOnUi = bl2;
            lastTouchX = f5;
            lastTouchY = f6;
        } else if (bl) {
            if (!(touchStartedOnUi || bl2 || Core.input.isTouched(1))) {
                float f7 = f5 - lastTouchX;
                float f8 = f6 - lastTouchY;
                if (Core.graphics != null && Core.graphics.getWidth() > 0) {
                    f3 = Core.camera.width / (float)Core.graphics.getWidth();
                }
                Core.camera.position.add(-f7 * f3, -f8 * f3);
            }
            lastTouchX = f5;
            lastTouchY = f6;
        } else {
            touchStartedOnUi = false;
        }
        lastTouched = bl;
        FreeCam.clampCamera();
    }

    private static void clampCamera() {
        if (Core.camera == null || Vars.world == null || Vars.world.width() <= 0) {
            return;
        }
        Core.camera.position.clamp(-Core.camera.width / 4.0f, -Core.camera.height / 4.0f, (float)Vars.world.unitWidth() + Core.camera.width / 4.0f, (float)Vars.world.unitHeight() + Core.camera.height / 4.0f);
    }

    private static void freezeUnit() {
        if (lockedUnit == null) {
            return;
        }
        FreeCam.lockedUnit.x = lockedX;
        FreeCam.lockedUnit.y = lockedY;
        FreeCam.lockedUnit.vel.set(0.0f, 0.0f);
    }

    public static void freezeOnDraw() {
        try {
            FreeCam.doFreezeOnDraw();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void doFreezeOnDraw() {
        if (!active || !LunariansSettings.enableFreeCam || lockedUnit == null || Vars.player == null || Vars.player.unit() != lockedUnit) {
            return;
        }
        FreeCam.freezeUnit();
    }

    private static void begin() {
        Unit unit = Vars.player == null ? null : Vars.player.unit();
        Unit unit2 = unit;
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
        InputHandler inputHandler;
        if (Vars.control != null && Vars.control.input != null && registeredInput != (inputHandler = Vars.control.input)) {
            inputHandler.inputLocks.add((Object)FREECAM_INPUT_LOCK);
            registeredInput = inputHandler;
        }
    }
}

