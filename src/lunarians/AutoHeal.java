/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Events
 *  arc.func.Cons
 *  arc.math.Mathf
 *  arc.math.geom.Position
 *  arc.math.geom.Vec2
 *  arc.struct.IntSet
 *  arc.struct.Seq
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.ai.UnitCommand
 *  mindustry.ai.types.CommandAI
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.gen.Building
 *  mindustry.gen.Call
 *  mindustry.gen.Groups
 *  mindustry.gen.Player
 *  mindustry.gen.Unit
 *  mindustry.logic.Ranged
 *  mindustry.world.Tile
 *  mindustry.world.blocks.defense.MendProjector
 *  mindustry.world.blocks.units.RepairTower
 *  mindustry.world.blocks.units.RepairTurret
 */
package lunarians;

import arc.Events;
import arc.func.Cons;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.LunariansSettings;
import lunarians.data.HealSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.logic.Ranged;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.MendProjector;
import mindustry.world.blocks.units.RepairTower;
import mindustry.world.blocks.units.RepairTurret;

public class AutoHeal {
    private static final float SCAN = 0.25f;
    private static float timer;
    private static int count;
    private static boolean errorLogged;
    private static final IntSet healing;
    private static final Vec2 tmp;

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run((Object)EventType.Trigger.update, (Runnable)new Runnable(){

            @Override
            public void run() {
                AutoHeal.update();
            }
        });
        Events.on(EventType.WorldLoadEvent.class, (Cons)new Cons<EventType.WorldLoadEvent>(){

            public void get(EventType.WorldLoadEvent worldLoadEvent) {
                AutoHeal.reset();
            }
        });
    }

    public static void reset() {
        timer = 0.0f;
        count = 0;
        healing.clear();
    }

    public static int count() {
        return count;
    }

    public static boolean isHealing(int n) {
        return healing.contains(n);
    }

    public static float hpFrac(Unit unit) {
        return unit.health / Math.max(1.0f, unit.maxHealth);
    }

    public static boolean needsHeal(Unit unit) {
        return AutoHeal.hpFrac(unit) < LunariansSettings.autoHealPercent / 100.0f;
    }

    public static boolean tryHeal(Unit unit) {
        if (unit == null || !unit.isValid() || Vars.player == null) {
            return false;
        }
        if (!AutoHeal.needsHeal(unit)) {
            return false;
        }
        if (!(unit.controller() instanceof CommandAI)) {
            return false;
        }
        Building building = AutoHeal.findNearestHeal(unit);
        if (building == null) {
            return false;
        }
        float f = AutoHeal.healRadius(building);
        if (f <= 0.0f) {
            f = 40.0f;
        }
        if (unit.dst((Position)building) <= f * 0.9f && AutoHeal.isGoodFloor(unit.tileX(), unit.tileY(), unit)) {
            healing.add(unit.id);
            return true;
        }
        Vec2 vec2 = AutoHeal.findFloorStand(building, unit, f);
        if (vec2 == null) {
            return false;
        }
        CommandAI commandAI = (CommandAI)unit.controller();
        if (commandAI.command != UnitCommand.moveCommand) {
            Call.setUnitCommand((Player)Vars.player, (int[])new int[]{unit.id}, (UnitCommand)UnitCommand.moveCommand);
        }
        Call.commandUnits((Player)Vars.player, (int[])new int[]{unit.id}, null, null, (Vec2)new Vec2(vec2.x, vec2.y), (boolean)false, (boolean)true);
        healing.add(unit.id);
        return true;
    }

    private static Vec2 findFloorStand(Building building, Unit unit, float f) {
        int n;
        int n2 = building.tileX();
        int n3 = building.tileY();
        int n4 = Math.max(2, Mathf.ceil((float)(f / 8.0f)));
        int n5 = Math.max(1, Mathf.floor((float)((float)building.block.size / 2.0f)) + 1);
        Vec2 vec2 = null;
        float f2 = Float.MAX_VALUE;
        for (int i = n5; i <= n4; ++i) {
            for (int j = -i; j <= i; ++j) {
                for (int k = -i; k <= i; ++k) {
                    int n6;
                    float f3;
                    float f4;
                    float f5;
                    int n7;
                    if (Math.max(Math.abs(j), Math.abs(k)) != i && i > n5 || !Vars.world.tiles.in(n7 = n2 + j, n = n3 + k) || Mathf.dst((float)(f5 = (float)n7 * 8.0f + 4.0f), (float)(f4 = (float)n * 8.0f + 4.0f), (float)building.x, (float)building.y) > f * 0.9f || !AutoHeal.isGoodFloor(n7, n, unit) || !((f3 = (float)(n6 = AutoHeal.countWallNeighbors(n7, n)) * 40.0f + unit.dst(f5, f4) * 0.05f + (float)i * 2.0f) < f2)) continue;
                    f2 = f3;
                    if (vec2 == null) {
                        vec2 = new Vec2();
                    }
                    vec2.set(f5, f4);
                }
            }
            if (vec2 != null && f2 < 40.0f) break;
        }
        if (vec2 == null) {
            Vec2 vec22 = new Vec2(unit.x - building.x, unit.y - building.y);
            if (vec22.len() < 0.1f) {
                vec22.set(1.0f, 0.0f);
            }
            vec22.nor();
            for (float f6 = Math.max(16.0f, (float)building.block.size * 8.0f); f6 <= f * 0.9f; f6 += 8.0f) {
                int n8;
                float f7 = building.x + vec22.x * f6;
                float f8 = building.y + vec22.y * f6;
                n = Mathf.floor((float)(f7 / 8.0f));
                if (!AutoHeal.isGoodFloor(n, n8 = Mathf.floor((float)(f8 / 8.0f)), unit)) continue;
                vec2 = new Vec2((float)n * 8.0f + 4.0f, (float)n8 * 8.0f + 4.0f);
                break;
            }
        }
        return vec2;
    }

    private static boolean isGoodFloor(int n, int n2, Unit unit) {
        if (!Vars.world.tiles.in(n, n2)) {
            return false;
        }
        Tile tile = Vars.world.tile(n, n2);
        if (tile == null) {
            return false;
        }
        if (tile.build != null) {
            return false;
        }
        if (!tile.passable()) {
            return false;
        }
        if (!unit.isFlying() && tile.solid()) {
            return false;
        }
        if (tile.floor() == null) {
            return false;
        }
        return unit.isFlying() || unit.type != null && unit.type.naval || !tile.floor().isLiquid || !(tile.floor().drownTime > 0.0f);
    }

    private static int countWallNeighbors(int n, int n2) {
        int n3 = 0;
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if (i == 0 && j == 0) continue;
                if (!Vars.world.tiles.in(n + i, n2 + j)) {
                    ++n3;
                    continue;
                }
                Tile tile = Vars.world.tile(n + i, n2 + j);
                if (tile != null && !tile.solid() && (tile.build == null || tile.build.block == null || !tile.build.block.solid)) continue;
                ++n3;
            }
        }
        return n3;
    }

    public static Building findNearestHeal(Unit unit) {
        Building building = null;
        float f = Float.MAX_VALUE;
        int n = Groups.build.size();
        for (int i = 0; i < n; ++i) {
            float f2;
            float f3;
            Building building2 = (Building)Groups.build.index(i);
            if (building2 == null || !building2.isValid() || building2.team != unit.team || (f3 = AutoHeal.healRadius(building2)) <= 0.0f || !((f2 = unit.dst((Position)building2) - f3 * 0.25f) < f)) continue;
            f = f2;
            building = building2;
        }
        return building;
    }

    private static float healRadius(Building building) {
        String string;
        if (building.block instanceof RepairTurret) {
            return ((RepairTurret)building.block).repairRadius;
        }
        if (building.block instanceof RepairTower) {
            return ((RepairTower)building.block).range;
        }
        if (building.block instanceof MendProjector) {
            return ((MendProjector)building.block).range;
        }
        if (building instanceof Ranged && ((string = building.block.name.toLowerCase()).contains("repair") || string.contains("mend") || string.contains("heal") || string.contains("regen"))) {
            float f = ((Ranged)building).range();
            return f > 0.0f ? f : 40.0f;
        }
        return 0.0f;
    }

    private static void update() {
        block4: {
            if (!LunariansSettings.enableAutoHeal || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
                healing.clear();
                return;
            }
            if ((timer += Time.delta / 60.0f) < LunariansSettings.actionInterval(0.25f)) {
                return;
            }
            timer = 0.0f;
            healing.clear();
            count = 0;
            try {
                AutoHeal.scan();
            }
            catch (Throwable throwable) {
                if (errorLogged) break block4;
                errorLogged = true;
                Log.err((String)"[Lunarians] AutoHeal error", (Throwable)throwable);
            }
        }
    }

    private static void scan() {
        Seq<HealSlot> seq = LunariansSettings.healSlots;
        boolean bl = seq.isEmpty();
        int n = Groups.unit.size();
        if (bl) {
            for (int i = 0; i < n; ++i) {
                Unit unit = (Unit)Groups.unit.index(i);
                if (unit == null || !unit.isValid() || unit.team != Vars.player.team() || !(unit.controller() instanceof CommandAI) || !AutoHeal.tryHeal(unit)) continue;
                ++count;
            }
            return;
        }
        block1: for (int i = 0; i < seq.size; ++i) {
            HealSlot healSlot = (HealSlot)seq.get(i);
            if (!healSlot.ready()) continue;
            int n2 = 0;
            for (int j = 0; j < n; ++j) {
                Unit unit = (Unit)Groups.unit.index(j);
                if (unit == null || !unit.isValid() || unit.team != Vars.player.team() || !healSlot.unit.equals(unit.type.name) || !(unit.controller() instanceof CommandAI)) continue;
                if (healSlot.maxUnits > 0 && n2 >= healSlot.maxUnits) continue block1;
                if (!AutoHeal.tryHeal(unit)) continue;
                ++n2;
                ++count;
            }
        }
    }

    static {
        healing = new IntSet();
        tmp = new Vec2();
    }
}

