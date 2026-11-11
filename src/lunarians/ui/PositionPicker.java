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
 *  mindustry.core.World
 *  mindustry.game.EventType$Trigger
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
import mindustry.core.World;
import mindustry.game.EventType;

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
            Events.run((Object)EventType.Trigger.update, (Runnable)new Runnable(){

                @Override
                public void run() {
                    PositionPicker.update();
                }
            });
        }
        Vars.ui.showInfoToast(LunariansI18n.get("ac_tap_hint"), 3.0f);
    }

    private static void update() {
        if (callback == null) {
            return;
        }
        if (Vars.state == null || !Vars.state.isGame()) {
            PositionPicker.finish(true, 0, 0);
            return;
        }
        if (Time.time - armedAt < 20.0f) {
            return;
        }
        if (Core.input.keyTap(KeyCode.escape) || Core.input.keyTap(KeyCode.back)) {
            PositionPicker.finish(true, 0, 0);
            return;
        }
        if (Core.input.justTouched() && !Core.scene.hasMouse() && !Core.scene.hasDialog()) {
            Vec2 vec2 = Core.input.mouseWorld();
            PositionPicker.finish(false, World.toTile((float)vec2.x), World.toTile((float)vec2.y));
        }
    }

    private static void finish(boolean bl, int n, int n2) {
        Cons<int[]> cons = callback;
        Runnable runnable = cancelled;
        callback = null;
        cancelled = null;
        if (bl) {
            if (runnable != null) {
                runnable.run();
            }
        } else if (cons != null) {
            cons.get((Object)new int[]{n, n2});
        }
    }
}

