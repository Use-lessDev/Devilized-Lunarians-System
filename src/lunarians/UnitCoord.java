package lunarians;

import arc.math.geom.Vec2;
import arc.struct.IntFloatMap;
import arc.struct.IntIntMap;
import arc.util.Time;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Unit;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/UnitCoord.class */
public final class UnitCoord {
    public static final int ATTACK = 1;
    public static final int ESCORT = 2;
    public static final int HEAL = 3;
    public static final int FLEE = 4;
    private static final IntIntMap owner = new IntIntMap();
    private static final IntFloatMap until = new IntFloatMap();
    private static final IntFloatMap lastX = new IntFloatMap();
    private static final IntFloatMap lastY = new IntFloatMap();
    private static final IntFloatMap lastAt = new IntFloatMap();
    private static float windowStart;
    private static int used;
    private static final int MAX_PER_WINDOW = 60;

    private UnitCoord() {
    }

    public static void reset() {
        owner.clear();
        until.clear();
        lastX.clear();
        lastY.clear();
        lastAt.clear();
        used = 0;
        windowStart = 0.0f;
    }

    public static int holder(int i) {
        if (until.get(i, 0.0f) > Time.time) {
            return owner.get(i, 0);
        }
        return 0;
    }

    public static boolean free(int i, int i2) {
        return holder(i) <= i2;
    }

    public static boolean cmd(Unit unit, int i, float f, Building building, Unit unit2, Vec2 vec2) {
        if (unit == null || vec2 == null || Vars.player == null || !free(unit.id, i)) {
            return false;
        }
        if (Time.time - windowStart > 30.0f) {
            windowStart = Time.time;
            used = 0;
        }
        owner.put(unit.id, i);
        until.put(unit.id, Time.time + f);
        if (Time.time - lastAt.get(unit.id, -9999.0f) < 45.0f && Math.abs(lastX.get(unit.id, 0.0f) - vec2.x) < 6.0f && Math.abs(lastY.get(unit.id, 0.0f) - vec2.y) < 6.0f) {
            return true;
        }
        if (used >= MAX_PER_WINDOW) {
            return false;
        }
        used++;
        if ((unit.controller() instanceof CommandAI) && unit.controller().command != UnitCommand.moveCommand) {
            Call.setUnitCommand(Vars.player, new int[]{unit.id}, UnitCommand.moveCommand);
        }
        Call.commandUnits(Vars.player, new int[]{unit.id}, building, unit2, vec2, false, true);
        lastX.put(unit.id, vec2.x);
        lastY.put(unit.id, vec2.y);
        lastAt.put(unit.id, Time.time);
        return true;
    }
}
