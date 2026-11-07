package lunarians;

import arc.math.geom.Vec2;
import mindustry.gen.Unit;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/UnitCoord.class */
public final class UnitCoord {
    private UnitCoord() {
    }

    public static Vec2 of(Unit unit) {
        return unit == null ? new Vec2() : new Vec2(unit.x, unit.y);
    }

    public static float x(Unit unit) {
        if (unit == null) {
            return 0.0f;
        }
        return unit.x;
    }

    public static float y(Unit unit) {
        if (unit == null) {
            return 0.0f;
        }
        return unit.y;
    }
}
