/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.math.Mathf
 *  arc.math.geom.Vec2
 *  mindustry.Vars
 *  mindustry.gen.Unit
 *  mindustry.world.Tile
 *  mindustry.world.blocks.environment.Floor
 */
package lunarians;

import arc.math.Mathf;
import arc.math.geom.Vec2;
import mindustry.Vars;
import mindustry.gen.Unit;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;

public final class PathSafe {
    private PathSafe() {
    }

    public static boolean isBadFor(Unit unit, int n, int n2) {
        if (!Vars.world.tiles.in(n, n2)) {
            return true;
        }
        Tile tile = Vars.world.tile(n, n2);
        if (tile == null) {
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
        if (floor != null && floor.isLiquid && floor.drownTime > 0.0f) {
            return true;
        }
        return tile.build != null && tile.build.block != null && tile.build.block.solid;
    }

    public static Vec2 safeNear(Unit unit, float f, float f2, float f3) {
        int n;
        int n2 = Mathf.floor((float)(f / 8.0f));
        if (!PathSafe.isBadFor(unit, n2, n = Mathf.floor((float)(f2 / 8.0f)))) {
            return new Vec2((float)n2 * 8.0f + 4.0f, (float)n * 8.0f + 4.0f);
        }
        int n3 = Math.max(1, Mathf.ceil((float)(f3 / 8.0f)));
        for (int i = 1; i <= n3; ++i) {
            for (int j = -i; j <= i; ++j) {
                for (int k = -i; k <= i; ++k) {
                    int n4;
                    int n5;
                    if (Math.abs(j) != i && Math.abs(k) != i || PathSafe.isBadFor(unit, n5 = n2 + j, n4 = n + k)) continue;
                    return new Vec2((float)n5 * 8.0f + 4.0f, (float)n4 * 8.0f + 4.0f);
                }
            }
        }
        return new Vec2(f, f2);
    }
}

