package lunarians;

import arc.Events;
import arc.func.Cons;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.data.HealSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.logic.Ranged;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.MendProjector;
import mindustry.world.blocks.units.RepairTower;
import mindustry.world.blocks.units.RepairTurret;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoHeal.class */
public class AutoHeal {
    private static final float SCAN = 0.25f;
    private static float timer;
    private static int count;
    private static boolean errorLogged;
    private static final IntSet healing = new IntSet();
    private static final Vec2 tmp = new Vec2();

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run(EventType.Trigger.update, new Runnable() { // from class: lunarians.AutoHeal.1
            @Override // java.lang.Runnable
            public void run() {
                AutoHeal.update();
            }
        });
        Events.on(EventType.WorldLoadEvent.class, new Cons<EventType.WorldLoadEvent>() { // from class: lunarians.AutoHeal.2
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

    public static boolean isHealing(int i) {
        return healing.contains(i);
    }

    public static float hpFrac(Unit unit) {
        return unit.health / Math.max(1.0f, unit.maxHealth);
    }

    public static boolean needsHeal(Unit unit) {
        return hpFrac(unit) < LunariansSettings.autoHealPercent / 100.0f;
    }

    public static boolean tryHeal(Unit unit) {
        Building findNearestHeal;
        if (unit == null || !unit.isValid() || Vars.player == null || !needsHeal(unit) || !(unit.controller() instanceof CommandAI) || (findNearestHeal = findNearestHeal(unit)) == null) {
            return false;
        }
        float healRadius = healRadius(findNearestHeal);
        if (healRadius <= 0.0f) {
            healRadius = 40.0f;
        }
        if (unit.dst(findNearestHeal) <= healRadius * 0.9f && isGoodFloor(unit.tileX(), unit.tileY(), unit)) {
            healing.add(unit.id);
            return true;
        }
        Vec2 findFloorStand = findFloorStand(findNearestHeal, unit, healRadius);
        if (findFloorStand == null) {
            return false;
        }
        if (unit.controller().command != UnitCommand.moveCommand) {
            Call.setUnitCommand(Vars.player, new int[]{unit.id}, UnitCommand.moveCommand);
        }
        Call.commandUnits(Vars.player, new int[]{unit.id}, (Building) null, (Unit) null, new Vec2(findFloorStand.x, findFloorStand.y), false, true);
        healing.add(unit.id);
        return true;
    }

    private static Vec2 findFloorStand(Building building, Unit unit, float f) {
        int tileX = building.tileX();
        int tileY = building.tileY();
        int max = Math.max(2, Mathf.ceil(f / 8.0f));
        int max2 = Math.max(1, Mathf.floor(building.block.size / 2.0f) + 1);
        Vec2 vec2 = null;
        float f2 = Float.MAX_VALUE;
        for (int i = max2; i <= max; i++) {
            for (int i2 = -i; i2 <= i; i2++) {
                for (int i3 = -i; i3 <= i; i3++) {
                    if (Math.max(Math.abs(i2), Math.abs(i3)) == i || i <= max2) {
                        int i4 = tileX + i2;
                        int i5 = tileY + i3;
                        if (Vars.world.tiles.in(i4, i5)) {
                            float f3 = (i4 * 8.0f) + 4.0f;
                            float f4 = (i5 * 8.0f) + 4.0f;
                            if (Mathf.dst(f3, f4, building.x, building.y) <= f * 0.9f && isGoodFloor(i4, i5, unit)) {
                                float countWallNeighbors = (countWallNeighbors(i4, i5) * 40.0f) + (unit.dst(f3, f4) * 0.05f) + (i * 2.0f);
                                if (countWallNeighbors < f2) {
                                    f2 = countWallNeighbors;
                                    if (vec2 == null) {
                                        vec2 = new Vec2();
                                    }
                                    vec2.set(f3, f4);
                                }
                            }
                        }
                    }
                }
            }
            if (vec2 != null && f2 < 40.0f) {
                break;
            }
        }
        if (vec2 == null) {
            Vec2 vec22 = new Vec2(unit.x - building.x, unit.y - building.y);
            if (vec22.len() < 0.1f) {
                vec22.set(1.0f, 0.0f);
            }
            vec22.nor();
            float max3 = Math.max(16.0f, building.block.size * 8.0f);
            while (true) {
                float f5 = max3;
                if (f5 > f * 0.9f) {
                    break;
                }
                float f6 = building.x + (vec22.x * f5);
                float f7 = building.y + (vec22.y * f5);
                int floor = Mathf.floor(f6 / 8.0f);
                int floor2 = Mathf.floor(f7 / 8.0f);
                if (!isGoodFloor(floor, floor2, unit)) {
                    max3 = f5 + 8.0f;
                } else {
                    vec2 = new Vec2((floor * 8.0f) + 4.0f, (floor2 * 8.0f) + 4.0f);
                    break;
                }
            }
        }
        return vec2;
    }

    private static boolean isGoodFloor(int i, int i2, Unit unit) {
        Tile tile;
        if (!Vars.world.tiles.in(i, i2) || (tile = Vars.world.tile(i, i2)) == null || tile.build != null || !tile.passable()) {
            return false;
        }
        if ((!unit.isFlying() && tile.solid()) || tile.floor() == null) {
            return false;
        }
        if (unit.isFlying()) {
            return true;
        }
        return (unit.type != null && unit.type.naval) || !tile.floor().isLiquid || tile.floor().drownTime <= 0.0f;
    }

    private static int countWallNeighbors(int i, int i2) {
        int i3 = 0;
        for (int i4 = -1; i4 <= 1; i4++) {
            for (int i5 = -1; i5 <= 1; i5++) {
                if (i4 != 0 || i5 != 0) {
                    if (!Vars.world.tiles.in(i + i4, i2 + i5)) {
                        i3++;
                    } else {
                        Tile tile = Vars.world.tile(i + i4, i2 + i5);
                        if (tile == null || tile.solid() || (tile.build != null && tile.build.block != null && tile.build.block.solid)) {
                            i3++;
                        }
                    }
                }
            }
        }
        return i3;
    }

    public static Building findNearestHeal(Unit unit) {
        Building building = null;
        float f = Float.MAX_VALUE;
        int size = Groups.build.size();
        for (int i = 0; i < size; i++) {
            Building building2 = (Building) Groups.build.index(i);
            if (building2 != null && building2.isValid() && building2.team == unit.team) {
                float healRadius = healRadius(building2);
                if (healRadius > 0.0f) {
                    float dst = unit.dst(building2) - (healRadius * SCAN);
                    if (dst < f) {
                        f = dst;
                        building = building2;
                    }
                }
            }
        }
        return building;
    }

    private static float healRadius(Building building) {
        if (building.block instanceof RepairTurret) {
            return building.block.repairRadius;
        }
        if (building.block instanceof RepairTower) {
            return building.block.range;
        }
        if (building.block instanceof MendProjector) {
            return building.block.range;
        }
        if (building instanceof Ranged) {
            String lowerCase = building.block.name.toLowerCase();
            if (lowerCase.contains("repair") || lowerCase.contains("mend") || lowerCase.contains("heal") || lowerCase.contains("regen")) {
                float range = ((Ranged) building).range();
                if (range > 0.0f) {
                    return range;
                }
                return 40.0f;
            }
            return 0.0f;
        }
        return 0.0f;
    }

    private static void update() {
        if (!LunariansSettings.enableAutoHeal || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
            healing.clear();
            return;
        }
        timer += Time.delta / 60.0f;
        if (timer < LunariansSettings.actionInterval(SCAN)) {
            return;
        }
        timer = 0.0f;
        healing.clear();
        count = 0;
        try {
            scan();
        } catch (Throwable th) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err("[Lunarians] AutoHeal error", th);
            }
        }
    }

    private static void scan() {
        Seq<HealSlot> seq = LunariansSettings.healSlots;
        boolean isEmpty = seq.isEmpty();
        int size = Groups.unit.size();
        if (isEmpty) {
            for (int i = 0; i < size; i++) {
                Unit index = Groups.unit.index(i);
                if (index != null && index.isValid() && index.team == Vars.player.team() && (index.controller() instanceof CommandAI) && tryHeal(index)) {
                    count++;
                }
            }
            return;
        }
        for (int i2 = 0; i2 < seq.size; i2++) {
            HealSlot healSlot = (HealSlot) seq.get(i2);
            if (healSlot.ready()) {
                int i3 = 0;
                for (int i4 = 0; i4 < size; i4++) {
                    Unit index2 = Groups.unit.index(i4);
                    if (index2 != null && index2.isValid() && index2.team == Vars.player.team() && healSlot.unit.equals(index2.type.name) && (index2.controller() instanceof CommandAI)) {
                        if (healSlot.maxUnits <= 0 || i3 < healSlot.maxUnits) {
                            if (tryHeal(index2)) {
                                i3++;
                                count++;
                            }
                        }
                    }
                }
            }
        }
    }
}
