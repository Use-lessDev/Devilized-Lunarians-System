package lunarians;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.util.Log;
import arc.util.Time;
import java.lang.reflect.Field;
import java.util.HashMap;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.world.Tile;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/UsedArea.class */
public class UsedArea {
    private static final float DELAY = 420.0f;
    private static boolean registered;
    private static boolean hasArea;
    private static int ax;
    private static int ay;
    private static float ar;
    private static float next;
    private static final HashMap<Integer, Float> seen = new HashMap<>();
    private static boolean[] red;

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.UsedArea$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            UsedArea.$r8$lambda$Z-6EUKPf8pZUJIUXU_-8DGk44ns();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.UsedArea$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            UsedArea.$r8$lambda$wNMbpGyMt6F-uYi6cTxVgN9loSA();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.UsedArea$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Cons {
        public final void get(Object obj) {
            UsedArea.reset();
        }
    }

    public static void init() {
        if (registered) {
            return;
        }
        registered = true;
        Events.run(EventType.Trigger.update, UsedArea::update);
        Events.run(EventType.Trigger.draw, UsedArea::draw);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            reset();
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void reset() {
        hasArea = false;
        seen.clear();
        red = null;
    }

    private static Field fld(String str) throws Exception {
        Field declaredField = AutoSet.class.getDeclaredField(str);
        declaredField.setAccessible(true);
        return declaredField;
    }

    private static void update() {
        if (Vars.state == null || !Vars.state.isGame() || Vars.world == null) {
            return;
        }
        try {
            if (AutoSet.isRunning()) {
                int i = fld("cx").getInt(null);
                int i2 = fld("cy").getInt(null);
                float f = fld("radius").getFloat(null);
                if (!hasArea || i != ax || i2 != ay || f != ar) {
                    seen.clear();
                    red = null;
                }
                ax = i;
                ay = i2;
                ar = f;
                hasArea = true;
            }
            if (!hasArea || Time.time < next) {
                return;
            }
            next = Time.time + 60.0f;
            int width = Vars.world.width();
            if (red == null || red.length != width * Vars.world.height()) {
                red = new boolean[width * Vars.world.height()];
            }
            int ceil = (int) Math.ceil(ar);
            for (int i3 = ay - ceil; i3 <= ay + ceil; i3++) {
                for (int i4 = ax - ceil; i4 <= ax + ceil; i4++) {
                    if (Vars.world.tiles.in(i4, i3) && Mathf.dst(i4, i3, ax, ay) <= ar) {
                        int i5 = (i3 * width) + i4;
                        Tile tile = Vars.world.tile(i4, i3);
                        if (tile != null && tile.build != null) {
                            Float f2 = seen.get(Integer.valueOf(i5));
                            if (f2 == null) {
                                seen.put(Integer.valueOf(i5), Float.valueOf(Time.time));
                            } else if (Time.time - f2.floatValue() >= DELAY) {
                                red[i5] = true;
                            }
                        } else if (seen.remove(Integer.valueOf(i5)) != null || red[i5]) {
                            red[i5] = false;
                        }
                    }
                }
            }
        } catch (Throwable th) {
            Log.err("[Lunarians] UsedArea", th);
            reset();
        }
    }

    private static void draw() {
        if (red == null || !hasArea || Vars.state == null || !Vars.state.isGame()) {
            return;
        }
        int width = Vars.world.width();
        float f = Core.camera.position.x;
        float f2 = Core.camera.position.y;
        float f3 = (Core.camera.width / 2.0f) + 16.0f;
        float f4 = (Core.camera.height / 2.0f) + 16.0f;
        int max = Math.max(0, (int) ((f - f3) / 8.0f));
        int min = Math.min(width - 1, (int) ((f + f3) / 8.0f));
        int max2 = Math.max(0, (int) ((f2 - f4) / 8.0f));
        int min2 = Math.min(Vars.world.height() - 1, (int) ((f2 + f4) / 8.0f));
        Draw.z(119.0f);
        Draw.color(Color.scarlet, 0.45f);
        for (int i = max2; i <= min2; i++) {
            for (int i2 = max; i2 <= min; i2++) {
                if (red[(i * width) + i2]) {
                    Fill.rect(i2 * 8.0f, i * 8.0f, 8.0f, 8.0f);
                }
            }
        }
        Draw.reset();
    }
}
