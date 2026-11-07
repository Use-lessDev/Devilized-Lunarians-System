package lunarians;

import arc.Events;
import arc.func.Cons;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.IntFloatMap;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.data.MoonSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoMoon.class */
public class AutoMoon {
    private static final float SCAN = 0.55f;
    private static final float ATTACK_RANGE_PAD = 1.15f;
    private static float timer;
    private static int count;
    private static boolean errorLogged;
    private static final IntSet claimed = new IntSet();
    private static final IntFloatMap angles = new IntFloatMap();

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run(EventType.Trigger.update, new Runnable() { // from class: lunarians.AutoMoon.1
            @Override // java.lang.Runnable
            public void run() {
                AutoMoon.update();
            }
        });
        Events.on(EventType.WorldLoadEvent.class, new Cons<EventType.WorldLoadEvent>() { // from class: lunarians.AutoMoon.2
            public void get(EventType.WorldLoadEvent worldLoadEvent) {
                AutoMoon.reset();
            }
        });
    }

    public static void reset() {
        timer = 0.0f;
        count = 0;
        claimed.clear();
        angles.clear();
    }

    public static int count() {
        return count;
    }

    private static void update() {
        if (!LunariansSettings.enableAutoMoon || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
            return;
        }
        timer += Time.delta / 60.0f;
        if (timer < LunariansSettings.actionInterval(SCAN)) {
            return;
        }
        float f = timer;
        timer = 0.0f;
        try {
            scan(f);
        } catch (Throwable th) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err("[Lunarians] AutoMoon error", th);
            }
        }
    }

    private static boolean isConstructing(Unit unit) {
        if (unit == null || !unit.isValid()) {
            return false;
        }
        try {
            if (unit.activelyBuilding() || unit.isBuilding()) {
                return true;
            }
            if (unit.plans != null) {
                return unit.plans.size > 0;
            }
            return false;
        } catch (Throwable th) {
            return false;
        }
    }

    private static BuildPlan currentPlan(Unit unit) {
        if (unit == null) {
            return null;
        }
        try {
            BuildPlan buildPlan = unit.buildPlan();
            if (buildPlan != null) {
                return buildPlan;
            }
            if (unit.plans == null || unit.plans.size <= 0) {
                return null;
            }
            return (BuildPlan) unit.plans.first();
        } catch (Throwable th) {
            return null;
        }
    }

    private static void resolveCenter(MoonSlot moonSlot, float[] fArr) {
        Unit byID;
        if (moonSlot.hasUnitCenter() && (byID = Groups.unit.getByID(moonSlot.followId)) != null && byID.isValid()) {
            fArr[0] = byID.x;
            fArr[1] = byID.y;
            return;
        }
        if (moonSlot.hasTileCenter()) {
            fArr[0] = (moonSlot.x * 8.0f) + 4.0f;
            fArr[1] = (moonSlot.y * 8.0f) + 4.0f;
        } else if (Vars.player.unit() != null && Vars.player.unit().isValid()) {
            fArr[0] = Vars.player.unit().x;
            fArr[1] = Vars.player.unit().y;
        } else {
            fArr[0] = Vars.player.x;
            fArr[1] = Vars.player.y;
        }
    }

    private static Unit findBuildingHost(MoonSlot moonSlot) {
        if (moonSlot.hasUnitCenter()) {
            Unit byID = Groups.unit.getByID(moonSlot.followId);
            if (isConstructing(byID)) {
                return byID;
            }
        }
        Unit unit = Vars.player.unit();
        if (isConstructing(unit)) {
            return unit;
        }
        return null;
    }

    private static void scan(float f) {
        Seq<MoonSlot> seq = LunariansSettings.moonSlots;
        if (seq.isEmpty()) {
            return;
        }
        claimed.clear();
        count = 0;
        int size = Groups.unit.size();
        float[] fArr = new float[2];
        for (int i = 0; i < seq.size; i++) {
            MoonSlot moonSlot = (MoonSlot) seq.get(i);
            if (moonSlot.ready()) {
                resolveCenter(moonSlot, fArr);
                float f2 = fArr[0];
                float f3 = fArr[1];
                float f4 = moonSlot.radius * 8.0f;
                float max = Math.max(0.25f, Math.min(4.0f, moonSlot.speed));
                Seq seq2 = new Seq();
                for (int i2 = 0; i2 < size; i2++) {
                    Unit index = Groups.unit.index(i2);
                    if (index != null && index.isValid() && index.team == Vars.player.team() && !claimed.contains(index.id) && ((!moonSlot.hasUnitCenter() || index.id != moonSlot.followId) && !AutoUnitHeal.isActiveHealer(index.id) && (index.controller() instanceof CommandAI) && index.isFlying() && moonSlot.unit.equals(index.type.name))) {
                        seq2.add(index);
                    }
                }
                if (moonSlot.maxUnits > 0 && seq2.size > moonSlot.maxUnits) {
                    seq2.truncate(moonSlot.maxUnits);
                }
                for (int i3 = 0; i3 < seq2.size; i3++) {
                    claimed.add(((Unit) seq2.get(i3)).id);
                }
                if (!seq2.isEmpty()) {
                    Unit findBuildingHost = findBuildingHost(moonSlot);
                    if (findBuildingHost != null) {
                        assistBuild(seq2, findBuildingHost);
                        count += seq2.size;
                    } else {
                        float max2 = Math.max(0.25f, 2.2f / Math.max(1.0f, moonSlot.radius)) * f * max;
                        for (int i4 = 0; i4 < seq2.size; i4++) {
                            Unit unit = (Unit) seq2.get(i4);
                            float max3 = f4 + (Math.max(24.0f, unit.type.maxRange) * ATTACK_RANGE_PAD);
                            Unit nearestEnemy = nearestEnemy(unit, f2, f3, max3);
                            Building nearestEnemyBuild = nearestEnemy == null ? nearestEnemyBuild(unit, f2, f3, max3) : null;
                            if (unit.controller().command != UnitCommand.moveCommand) {
                                Call.setUnitCommand(Vars.player, new int[]{unit.id}, UnitCommand.moveCommand);
                            }
                            if (nearestEnemy != null) {
                                float max4 = Math.max(24.0f, unit.type.maxRange) * 0.9f;
                                float angleTo = nearestEnemy.angleTo(unit) * 0.017453292f;
                                Call.commandUnits(Vars.player, new int[]{unit.id}, (Building) null, nearestEnemy, new Vec2(nearestEnemy.x + (Mathf.cos(angleTo) * max4), nearestEnemy.y + (Mathf.sin(angleTo) * max4)), false, true);
                                count++;
                            } else if (nearestEnemyBuild != null) {
                                float max5 = Math.max(24.0f, unit.type.maxRange) * 0.9f;
                                float angleTo2 = nearestEnemyBuild.angleTo(unit) * 0.017453292f;
                                Call.commandUnits(Vars.player, new int[]{unit.id}, nearestEnemyBuild, (Unit) null, new Vec2(nearestEnemyBuild.x + (Mathf.cos(angleTo2) * max5), nearestEnemyBuild.y + (Mathf.sin(angleTo2) * max5)), false, true);
                                count++;
                            } else {
                                float f5 = angles.get(unit.id, i4 * (6.2831855f / Math.max(1, seq2.size))) + max2;
                                if (f5 > 6.2831855f) {
                                    f5 -= 6.2831855f;
                                }
                                angles.put(unit.id, f5);
                                Call.commandUnits(Vars.player, new int[]{unit.id}, (Building) null, (Unit) null, new Vec2(f2 + (Mathf.cos(f5) * f4), f3 + (Mathf.sin(f5) * f4)), false, true);
                                count++;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void assistBuild(Seq<Unit> seq, Unit unit) {
        BuildPlan currentPlan = currentPlan(unit);
        float f = unit.x;
        float f2 = unit.y;
        if (currentPlan != null) {
            try {
                f = (currentPlan.x * 8.0f) + 4.0f;
                f2 = (currentPlan.y * 8.0f) + 4.0f;
            } catch (Throwable th) {
                f = unit.x;
                f2 = unit.y;
            }
        }
        IntSeq intSeq = new IntSeq();
        IntSeq intSeq2 = new IntSeq();
        for (int i = 0; i < seq.size; i++) {
            Unit unit2 = (Unit) seq.get(i);
            if (unit2.type == null || unit2.type.buildSpeed <= 0.0f) {
                intSeq2.add(unit2.id);
            } else {
                intSeq.add(unit2.id);
            }
        }
        if (intSeq.size > 0) {
            Call.setUnitCommand(Vars.player, intSeq.toArray(), UnitCommand.assistCommand);
        }
        for (int i2 = 0; i2 < seq.size; i2++) {
            Unit unit3 = (Unit) seq.get(i2);
            float max = Math.max(24.0f, unit3.type != null ? unit3.type.buildRange : 40.0f) * 0.7f;
            float max2 = ((i2 * 360.0f) / Math.max(1, seq.size)) * 0.017453292f;
            Call.commandUnits(Vars.player, new int[]{unit3.id}, (Building) null, (Unit) null, new Vec2(f + (Mathf.cos(max2) * Math.min(18.0f, max * 0.3f)), f2 + (Mathf.sin(max2) * Math.min(18.0f, max * 0.3f))), false, true);
        }
    }

    private static Unit nearestEnemy(Unit unit, float f, float f2, float f3) {
        Unit unit2 = null;
        float f4 = f3;
        int size = Groups.unit.size();
        for (int i = 0; i < size; i++) {
            Unit unit3 = (Unit) Groups.unit.index(i);
            if (unit3 != null && unit3.isValid() && unit3.team != unit.team && Mathf.dst(f, f2, unit3.x, unit3.y) <= f3) {
                float dst = unit.dst(unit3);
                if (dst < f4) {
                    f4 = dst;
                    unit2 = unit3;
                }
            }
        }
        return unit2;
    }

    private static Building nearestEnemyBuild(Unit unit, float f, float f2, float f3) {
        Building building = null;
        float f4 = f3;
        int size = Groups.build.size();
        for (int i = 0; i < size; i++) {
            Building building2 = (Building) Groups.build.index(i);
            if (building2 != null && building2.isValid() && building2.team != unit.team && Mathf.dst(f, f2, building2.x, building2.y) <= f3) {
                float dst = unit.dst(building2);
                if (dst < f4) {
                    f4 = dst;
                    building = building2;
                }
            }
        }
        return building;
    }
}
