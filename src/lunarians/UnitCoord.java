/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.math.geom.Vec2
 *  arc.struct.IntFloatMap
 *  arc.struct.IntIntMap
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.ai.UnitCommand
 *  mindustry.ai.types.CommandAI
 *  mindustry.gen.Building
 *  mindustry.gen.Call
 *  mindustry.gen.Player
 *  mindustry.gen.Unit
 */
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
import mindustry.gen.Player;
import mindustry.gen.Unit;

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

    public static int holder(int n) {
        return until.get(n, 0.0f) > Time.time ? owner.get(n, 0) : 0;
    }

    public static boolean free(int n, int n2) {
        return UnitCoord.holder(n) <= n2;
    }

    public static boolean cmd(Unit unit, int n, float f, Building building, Unit unit2, Vec2 vec2) {
        if (unit == null || vec2 == null || Vars.player == null || !UnitCoord.free(unit.id, n)) {
            return false;
        }
        if (Time.time - windowStart > 30.0f) {
            windowStart = Time.time;
            used = 0;
        }
        owner.put(unit.id, n);
        until.put(unit.id, Time.time + f);
        float f2 = lastAt.get(unit.id, -9999.0f);
        if (Time.time - f2 < 45.0f && Math.abs(lastX.get(unit.id, 0.0f) - vec2.x) < 6.0f && Math.abs(lastY.get(unit.id, 0.0f) - vec2.y) < 6.0f) {
            return true;
        }
        if (used >= 60) {
            return false;
        }
        ++used;
        if (unit.controller() instanceof CommandAI) {
            CommandAI commandAI = (CommandAI)unit.controller();
            if (commandAI.command != UnitCommand.moveCommand) {
                Call.setUnitCommand((Player)Vars.player, (int[])new int[]{unit.id}, (UnitCommand)UnitCommand.moveCommand);
            }
        }
        Call.commandUnits((Player)Vars.player, (int[])new int[]{unit.id}, (Building)building, (Unit)unit2, (Vec2)vec2, (boolean)false, (boolean)true);
        lastX.put(unit.id, vec2.x);
        lastY.put(unit.id, vec2.y);
        lastAt.put(unit.id, Time.time);
        return true;
    }
}

