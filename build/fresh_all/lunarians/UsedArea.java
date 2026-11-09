/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.graphics.Color
 *  arc.graphics.g2d.Draw
 *  arc.graphics.g2d.Fill
 *  arc.math.Mathf
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.world.Tile
 */
package lunarians;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.util.Log;
import arc.util.Time;
import java.lang.reflect.Field;
import java.util.HashMap;
import lunarians.AutoSet;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.world.Tile;

public class UsedArea {
    private static final float DELAY = 420.0f;
    private static boolean registered;
    private static boolean hasArea;
    private static int ax;
    private static int ay;
    private static float ar;
    private static float next;
    private static final HashMap<Integer, Float> seen;
    private static boolean[] red;

    public static void init() {
        if (registered) {
            return;
        }
        registered = true;
        Events.run((Object)EventType.Trigger.update, UsedArea::update);
        Events.run((Object)EventType.Trigger.draw, UsedArea::draw);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> UsedArea.reset());
    }

    private static void reset() {
        hasArea = false;
        seen.clear();
        red = null;
    }

    private static Field fld(String string) throws Exception {
        Field field = AutoSet.class.getDeclaredField(string);
        field.setAccessible(true);
        return field;
    }

    private static void update() {
        if (Vars.state == null || !Vars.state.isGame() || Vars.world == null) {
            return;
        }
        try {
            int n;
            int n2;
            if (AutoSet.isRunning()) {
                n2 = UsedArea.fld("cx").getInt(null);
                n = UsedArea.fld("cy").getInt(null);
                float f = UsedArea.fld("radius").getFloat(null);
                if (!hasArea || n2 != ax || n != ay || f != ar) {
                    seen.clear();
                    red = null;
                }
                ax = n2;
                ay = n;
                ar = f;
                hasArea = true;
            }
            if (!hasArea || Time.time < next) {
                return;
            }
            next = Time.time + 60.0f;
            n2 = Vars.world.width();
            if (red == null || red.length != n2 * Vars.world.height()) {
                red = new boolean[n2 * Vars.world.height()];
            }
            n = (int)Math.ceil(ar);
            for (int i = ay - n; i <= ay + n; ++i) {
                for (int j = ax - n; j <= ax + n; ++j) {
                    if (!Vars.world.tiles.in(j, i) || Mathf.dst((float)j, (float)i, (float)ax, (float)ay) > ar) continue;
                    int n3 = i * n2 + j;
                    Tile tile = Vars.world.tile(j, i);
                    if (tile != null && tile.build != null) {
                        Float f = seen.get(n3);
                        if (f == null) {
                            seen.put(n3, Float.valueOf(Time.time));
                            continue;
                        }
                        if (!(Time.time - f.floatValue() >= 420.0f)) continue;
                        UsedArea.red[n3] = true;
                        continue;
                    }
                    if (seen.remove(n3) == null && !red[n3]) continue;
                    UsedArea.red[n3] = false;
                }
            }
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] UsedArea", (Throwable)throwable);
            UsedArea.reset();
        }
    }

    private static void draw() {
        if (red == null || !hasArea || Vars.state == null || !Vars.state.isGame()) {
            return;
        }
        int n = Vars.world.width();
        float f = Core.camera.position.x;
        float f2 = Core.camera.position.y;
        float f3 = Core.camera.width / 2.0f + 16.0f;
        float f4 = Core.camera.height / 2.0f + 16.0f;
        int n2 = Math.max(0, (int)((f - f3) / 8.0f));
        int n3 = Math.min(n - 1, (int)((f + f3) / 8.0f));
        int n4 = Math.max(0, (int)((f2 - f4) / 8.0f));
        int n5 = Math.min(Vars.world.height() - 1, (int)((f2 + f4) / 8.0f));
        Draw.z((float)119.0f);
        Draw.color((Color)Color.scarlet, (float)0.45f);
        for (int i = n4; i <= n5; ++i) {
            for (int j = n2; j <= n3; ++j) {
                if (!red[i * n + j]) continue;
                Fill.rect((float)((float)j * 8.0f), (float)((float)i * 8.0f), (float)8.0f, (float)8.0f);
            }
        }
        Draw.reset();
    }

    static {
        seen = new HashMap();
    }
}

