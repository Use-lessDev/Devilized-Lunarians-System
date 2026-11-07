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

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/PositionPicker.class */
public final class PositionPicker {
    private static Cons<int[]> callback;
    private static Runnable cancelled;
    private static float armedAt;
    private static boolean registered;

    private PositionPicker() {
    }

    public static void pick(Cons<int[]> cons, Runnable runnable) {
        callback = cons;
        cancelled = runnable;
        armedAt = Time.time;
        if (!registered) {
            registered = true;
            Events.run(EventType.Trigger.update, new Runnable() { // from class: lunarians.ui.PositionPicker.1
                @Override // java.lang.Runnable
                public void run() {
                    PositionPicker.update();
                }
            });
        }
        Vars.ui.showInfoToast(LunariansI18n.get("ac_tap_hint"), 3.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void update() {
        if (callback == null) {
            return;
        }
        if (Vars.state == null || !Vars.state.isGame()) {
            finish(true, 0, 0);
            return;
        }
        if (Time.time - armedAt < 20.0f) {
            return;
        }
        if (Core.input.keyTap(KeyCode.escape) || Core.input.keyTap(KeyCode.back)) {
            finish(true, 0, 0);
        } else if (Core.input.justTouched() && !Core.scene.hasMouse() && !Core.scene.hasDialog()) {
            Vec2 mouseWorld = Core.input.mouseWorld();
            finish(false, World.toTile(mouseWorld.x), World.toTile(mouseWorld.y));
        }
    }

    private static void finish(boolean z, int i, int i2) {
        Cons<int[]> cons = callback;
        Runnable runnable = cancelled;
        callback = null;
        cancelled = null;
        if (z) {
            if (runnable != null) {
                runnable.run();
            }
        } else if (cons != null) {
            cons.get(new int[]{i, i2});
        }
    }
}
