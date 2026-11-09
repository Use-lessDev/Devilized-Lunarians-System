/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Events
 *  arc.func.Cons
 *  arc.math.Mathf
 *  arc.math.geom.Position
 *  arc.math.geom.Vec2
 *  arc.struct.IntFloatMap
 *  arc.struct.IntSeq
 *  arc.struct.IntSet
 *  arc.struct.Seq
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.ai.UnitCommand
 *  mindustry.ai.types.CommandAI
 *  mindustry.entities.units.BuildPlan
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.gen.Building
 *  mindustry.gen.Call
 *  mindustry.gen.Groups
 *  mindustry.gen.Player
 *  mindustry.gen.Unit
 */
package lunarians;

import arc.Events;
import arc.func.Cons;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.struct.IntFloatMap;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.AutoUnitHeal;
import lunarians.LunariansSettings;
import lunarians.data.MoonSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;

public class AutoMoon {
    private static final float SCAN = 0.55f;
    private static final float ATTACK_RANGE_PAD = 1.15f;
    private static float timer;
    private static int count;
    private static boolean errorLogged;
    private static final IntSet claimed;
    private static final IntFloatMap angles;

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run((Object)EventType.Trigger.update, (Runnable)new Runnable(){

            @Override
            public void run() {
                AutoMoon.update();
            }
        });
        Events.on(EventType.WorldLoadEvent.class, (Cons)new Cons<EventType.WorldLoadEvent>(){

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
        block4: {
            if (!LunariansSettings.enableAutoMoon || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
                return;
            }
            if ((timer += Time.delta / 60.0f) < LunariansSettings.actionInterval(0.55f)) {
                return;
            }
            float f = timer;
            timer = 0.0f;
            try {
                AutoMoon.scan(f);
            }
            catch (Throwable throwable) {
                if (errorLogged) break block4;
                errorLogged = true;
                Log.err((String)"[Lunarians] AutoMoon error", (Throwable)throwable);
            }
        }
    }

    private static boolean isConstructing(Unit unit) {
        if (unit == null || !unit.isValid()) {
            return false;
        }
        try {
            if (unit.activelyBuilding()) {
                return true;
            }
            if (unit.isBuilding()) {
                return true;
            }
            if (unit.plans != null && unit.plans.size > 0) {
                return true;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return false;
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
            if (unit.plans != null && unit.plans.size > 0) {
                return (BuildPlan)unit.plans.first();
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return null;
    }

    private static void resolveCenter(MoonSlot moonSlot, float[] fArray) {
        Unit unit;
        if (moonSlot.hasUnitCenter() && (unit = (Unit)Groups.unit.getByID(moonSlot.followId)) != null && unit.isValid()) {
            fArray[0] = unit.x;
            fArray[1] = unit.y;
            return;
        }
        if (moonSlot.hasTileCenter()) {
            fArray[0] = (float)moonSlot.x * 8.0f + 4.0f;
            fArray[1] = (float)moonSlot.y * 8.0f + 4.0f;
            return;
        }
        if (Vars.player.unit() != null && Vars.player.unit().isValid()) {
            fArray[0] = Vars.player.unit().x;
            fArray[1] = Vars.player.unit().y;
        } else {
            fArray[0] = Vars.player.x;
            fArray[1] = Vars.player.y;
        }
    }

    private static Unit findBuildingHost(MoonSlot moonSlot) {
        Unit unit;
        if (moonSlot.hasUnitCenter() && AutoMoon.isConstructing(unit = (Unit)Groups.unit.getByID(moonSlot.followId))) {
            return unit;
        }
        unit = Vars.player.unit();
        if (AutoMoon.isConstructing(unit)) {
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
        int n = Groups.unit.size();
        float[] fArray = new float[2];
        for (int i = 0; i < seq.size; ++i) {
            int n2;
            MoonSlot moonSlot = (MoonSlot)seq.get(i);
            if (!moonSlot.ready()) continue;
            AutoMoon.resolveCenter(moonSlot, fArray);
            float f2 = fArray[0];
            float f3 = fArray[1];
            float f4 = moonSlot.radius * 8.0f;
            float f5 = Math.max(0.25f, Math.min(4.0f, moonSlot.speed));
            Seq seq2 = new Seq();
            for (n2 = 0; n2 < n; ++n2) {
                Unit unit = (Unit)Groups.unit.index(n2);
                if (unit == null || !unit.isValid() || unit.team != Vars.player.team() || claimed.contains(unit.id) || moonSlot.hasUnitCenter() && unit.id == moonSlot.followId || AutoUnitHeal.isActiveHealer(unit.id) || !(unit.controller() instanceof CommandAI) || !unit.isFlying() || !moonSlot.unit.equals(unit.type.name)) continue;
                seq2.add((Object)unit);
            }
            if (moonSlot.maxUnits > 0 && seq2.size > moonSlot.maxUnits) {
                seq2.truncate(moonSlot.maxUnits);
            }
            for (n2 = 0; n2 < seq2.size; ++n2) {
                claimed.add(((Unit)seq2.get((int)n2)).id);
            }
            if (seq2.isEmpty()) continue;
            Unit unit = AutoMoon.findBuildingHost(moonSlot);
            if (unit != null) {
                AutoMoon.assistBuild((Seq<Unit>)seq2, unit);
                count += seq2.size;
                continue;
            }
            float f6 = Math.max(0.25f, 2.2f / Math.max(1.0f, moonSlot.radius)) * f * f5;
            for (int j = 0; j < seq2.size; ++j) {
                float f7;
                float f8;
                float f9;
                Unit unit2 = (Unit)seq2.get(j);
                Unit unit3 = AutoMoon.nearestEnemy(unit2, f2, f3, f9 = f4 + Math.max(24.0f, unit2.type.maxRange) * 1.15f);
                Building building = unit3 == null ? AutoMoon.nearestEnemyBuild(unit2, f2, f3, f9) : null;
                CommandAI commandAI = (CommandAI)unit2.controller();
                if (commandAI.command != UnitCommand.moveCommand) {
                    Call.setUnitCommand((Player)Vars.player, (int[])new int[]{unit2.id}, (UnitCommand)UnitCommand.moveCommand);
                }
                if (unit3 != null) {
                    f8 = Math.max(24.0f, unit2.type.maxRange) * 0.9f;
                    f7 = unit3.angleTo((Position)unit2) * ((float)Math.PI / 180);
                    Call.commandUnits((Player)Vars.player, (int[])new int[]{unit2.id}, null, (Unit)unit3, (Vec2)new Vec2(unit3.x + Mathf.cos((float)f7) * f8, unit3.y + Mathf.sin((float)f7) * f8), (boolean)false, (boolean)true);
                    ++count;
                    continue;
                }
                if (building != null) {
                    f8 = Math.max(24.0f, unit2.type.maxRange) * 0.9f;
                    f7 = building.angleTo((Position)unit2) * ((float)Math.PI / 180);
                    Call.commandUnits((Player)Vars.player, (int[])new int[]{unit2.id}, (Building)building, null, (Vec2)new Vec2(building.x + Mathf.cos((float)f7) * f8, building.y + Mathf.sin((float)f7) * f8), (boolean)false, (boolean)true);
                    ++count;
                    continue;
                }
                f8 = angles.get(unit2.id, (float)j * ((float)Math.PI * 2 / (float)Math.max(1, seq2.size)));
                if ((f8 += f6) > (float)Math.PI * 2) {
                    f8 -= (float)Math.PI * 2;
                }
                angles.put(unit2.id, f8);
                Call.commandUnits((Player)Vars.player, (int[])new int[]{unit2.id}, null, null, (Vec2)new Vec2(f2 + Mathf.cos((float)f8) * f4, f3 + Mathf.sin((float)f8) * f4), (boolean)false, (boolean)true);
                ++count;
            }
        }
    }

    private static void assistBuild(Seq<Unit> seq, Unit unit) {
        Unit unit2;
        int n;
        BuildPlan buildPlan = AutoMoon.currentPlan(unit);
        float f = unit.x;
        float f2 = unit.y;
        if (buildPlan != null) {
            try {
                f = (float)buildPlan.x * 8.0f + 4.0f;
                f2 = (float)buildPlan.y * 8.0f + 4.0f;
            }
            catch (Throwable throwable) {
                f = unit.x;
                f2 = unit.y;
            }
        }
        IntSeq intSeq = new IntSeq();
        IntSeq intSeq2 = new IntSeq();
        for (n = 0; n < seq.size; ++n) {
            unit2 = (Unit)seq.get(n);
            if (unit2.type == null || unit2.type.buildSpeed <= 0.0f) {
                intSeq2.add(unit2.id);
                continue;
            }
            intSeq.add(unit2.id);
        }
        if (intSeq.size > 0) {
            UnitCommand unitCommand = UnitCommand.assistCommand;
            Call.setUnitCommand((Player)Vars.player, (int[])intSeq.toArray(), (UnitCommand)unitCommand);
        }
        for (n = 0; n < seq.size; ++n) {
            unit2 = (Unit)seq.get(n);
            float f3 = Math.max(24.0f, unit2.type != null ? unit2.type.buildRange : 40.0f) * 0.7f;
            float f4 = (float)n * 360.0f / (float)Math.max(1, seq.size) * ((float)Math.PI / 180);
            float f5 = Mathf.cos((float)f4) * Math.min(18.0f, f3 * 0.3f);
            float f6 = Mathf.sin((float)f4) * Math.min(18.0f, f3 * 0.3f);
            Call.commandUnits((Player)Vars.player, (int[])new int[]{unit2.id}, null, null, (Vec2)new Vec2(f + f5, f2 + f6), (boolean)false, (boolean)true);
        }
    }

    private static Unit nearestEnemy(Unit unit, float f, float f2, float f3) {
        Unit unit2 = null;
        float f4 = f3;
        int n = Groups.unit.size();
        for (int i = 0; i < n; ++i) {
            float f5;
            Unit unit3 = (Unit)Groups.unit.index(i);
            if (unit3 == null || !unit3.isValid() || unit3.team == unit.team || Mathf.dst((float)f, (float)f2, (float)unit3.x, (float)unit3.y) > f3 || !((f5 = unit.dst((Position)unit3)) < f4)) continue;
            f4 = f5;
            unit2 = unit3;
        }
        return unit2;
    }

    private static Building nearestEnemyBuild(Unit unit, float f, float f2, float f3) {
        Building building = null;
        float f4 = f3;
        int n = Groups.build.size();
        for (int i = 0; i < n; ++i) {
            float f5;
            Building building2 = (Building)Groups.build.index(i);
            if (building2 == null || !building2.isValid() || building2.team == unit.team || Mathf.dst((float)f, (float)f2, (float)building2.x, (float)building2.y) > f3 || !((f5 = unit.dst((Position)building2)) < f4)) continue;
            f4 = f5;
            building = building2;
        }
        return building;
    }

    static {
        claimed = new IntSet();
        angles = new IntFloatMap();
    }
}

