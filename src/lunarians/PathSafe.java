package lunarians;

import arc.math.Mathf;
import arc.math.geom.Vec2;
import mindustry.Vars;
import mindustry.gen.Unit;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/PathSafe.class */
public final class PathSafe {
    private PathSafe() {
    }

    public static boolean isBadFor(Unit unit, int i, int i2) {
        Tile tile;
        if (!Vars.world.tiles.in(i, i2) || (tile = Vars.world.tile(i, i2)) == null) {
            return true;
        }
        if (unit.isFlying()) {
            return false;
        }
        if (unit.type != null && unit.type.naval) {
            return tile.solid();
        }
        if (tile.solid()) {
            return true;
        }
        Floor floor = tile.floor();
        if (floor == null || !floor.isLiquid || floor.drownTime <= 0.0f) {
            return (tile.build == null || tile.build.block == null || !tile.build.block.solid) ? false : true;
        }
        return true;
    }

    public static Vec2 safeNear(Unit unit, float f, float f2, float f3) {
        int floor = Mathf.floor(f / 8.0f);
        int floor2 = Mathf.floor(f2 / 8.0f);
        if (!isBadFor(unit, floor, floor2)) {
            return new Vec2((floor * 8.0f) + 4.0f, (floor2 * 8.0f) + 4.0f);
        }
        int max = Math.max(1, Mathf.ceil(f3 / 8.0f));
        for (int i = 1; i <= max; i++) {
            for (int i2 = -i; i2 <= i; i2++) {
                for (int i3 = -i; i3 <= i; i3++) {
                    if (Math.abs(i2) == i || Math.abs(i3) == i) {
                        int i4 = floor + i2;
                        int i5 = floor2 + i3;
                        if (!isBadFor(unit, i4, i5)) {
                            return new Vec2((i4 * 8.0f) + 4.0f, (i5 * 8.0f) + 4.0f);
                        }
                    }
                }
            }
        }
        return new Vec2(f, f2);
    }
}
