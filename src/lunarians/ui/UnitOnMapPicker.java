/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.func.Cons
 *  arc.input.KeyCode
 *  arc.math.geom.Vec2
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.game.EventType$Trigger
 *  mindustry.gen.Groups
 *  mindustry.gen.Unit
 */
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
    private static final float PICK_RANGE = 48.0f;

    private UnitOnMapPicker() {
    }

    public static void pick(Cons<Unit> cons, Runnable runnable) {
        callback = cons;
        cancelled = runnable;
        armedAt = Time.time;
        if (!registered) {
            registered = true;
            Events.run((Object)EventType.Trigger.update, (Runnable)new Runnable(){

                @Override
                public void run() {
                    UnitOnMapPicker.update();
                }
            });
        }
        String string = LunariansI18n.get("uom_hint");
        Vars.ui.showInfoToast(string, 3.0f);
    }

    private static void update() {
        if (callback == null) {
            return;
        }
        if (Vars.state == null || !Vars.state.isGame()) {
            UnitOnMapPicker.finish(true, null);
            return;
        }
        if (Time.time - armedAt < 20.0f) {
            return;
        }
        if (Core.input.keyTap(KeyCode.escape) || Core.input.keyTap(KeyCode.back)) {
            UnitOnMapPicker.finish(true, null);
            return;
        }
        if (Core.input.justTouched() && !Core.scene.hasMouse() && !Core.scene.hasDialog()) {
            Vec2 vec2 = Core.input.mouseWorld();
            Unit unit = null;
            float f = 48.0f;
            int n = Groups.unit.size();
            for (int i = 0; i < n; ++i) {
                float f2;
                Unit unit2 = (Unit)Groups.unit.index(i);
                if (unit2 == null || !unit2.isValid() || !((f2 = unit2.dst(vec2.x, vec2.y)) < f)) continue;
                f = f2;
                unit = unit2;
            }
            if (unit != null) {
                UnitOnMapPicker.finish(false, unit);
            }
        }
    }

    private static void finish(boolean bl, Unit unit) {
        Cons<Unit> cons = callback;
        Runnable runnable = cancelled;
        callback = null;
        cancelled = null;
        if (bl) {
            if (runnable != null) {
                runnable.run();
            }
        } else if (cons != null && unit != null) {
            cons.get((Object)unit);
        }
    }
}

