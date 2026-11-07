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

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/UnitOnMapPicker.class */
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
            Events.run(EventType.Trigger.update, new Runnable() { // from class: lunarians.ui.UnitOnMapPicker.1
                @Override // java.lang.Runnable
                public void run() {
                    UnitOnMapPicker.update();
                }
            });
        }
        Vars.ui.showInfoToast(LunariansI18n.get("uom_hint"), 3.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void update() {
        if (callback == null) {
            return;
        }
        if (Vars.state == null || !Vars.state.isGame()) {
            finish(true, null);
            return;
        }
        if (Time.time - armedAt < 20.0f) {
            return;
        }
        if (Core.input.keyTap(KeyCode.escape) || Core.input.keyTap(KeyCode.back)) {
            finish(true, null);
            return;
        }
        if (Core.input.justTouched() && !Core.scene.hasMouse() && !Core.scene.hasDialog()) {
            Vec2 mouseWorld = Core.input.mouseWorld();
            Unit unit = null;
            float f = 48.0f;
            int size = Groups.unit.size();
            for (int i = 0; i < size; i++) {
                Unit unit2 = (Unit) Groups.unit.index(i);
                if (unit2 != null && unit2.isValid()) {
                    float dst = unit2.dst(mouseWorld.x, mouseWorld.y);
                    if (dst < f) {
                        f = dst;
                        unit = unit2;
                    }
                }
            }
            if (unit != null) {
                finish(false, unit);
            }
        }
    }

    private static void finish(boolean z, Unit unit) {
        Cons<Unit> cons = callback;
        Runnable runnable = cancelled;
        callback = null;
        cancelled = null;
        if (z) {
            if (runnable != null) {
                runnable.run();
            }
        } else if (cons != null && unit != null) {
            cons.get(unit);
        }
    }
}
