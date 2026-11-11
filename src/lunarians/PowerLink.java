/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.math.geom.Point2
 *  arc.math.geom.Position
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.entities.units.BuildPlan
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.game.Team
 *  mindustry.gen.Building
 *  mindustry.gen.Groups
 *  mindustry.gen.Unit
 *  mindustry.type.ItemStack
 *  mindustry.world.Block
 *  mindustry.world.Build
 *  mindustry.world.blocks.power.PowerNode
 */
package lunarians;

import arc.Core;
import arc.Events;
import arc.math.geom.Point2;
import arc.math.geom.Position;
import arc.util.Log;
import arc.util.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import lunarians.AutoSetConfig;
import lunarians.LunariansI18n;
import mindustry.Vars;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.blocks.power.PowerNode;

public class PowerLink {
    private static boolean registered;
    private static float next;
    private static final HashMap<Integer, int[]> tries;
    private static final HashMap<Integer, Float> last;

    public static boolean enabled() {
        return Core.settings.getBool("lunarians-auto-pnode", true);
    }

    public static void setEnabled(boolean bl) {
        Core.settings.put("lunarians-auto-pnode", (Object)bl);
    }

    public static void init() {
        if (registered) {
            return;
        }
        registered = true;
        Events.run((Object)EventType.Trigger.update, PowerLink::update);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            tries.clear();
            last.clear();
        });
    }

    private static boolean unpowered(Building building) {
        return building.power != null && building.block.consPower != null && !building.block.consPower.buffered && building.block.consPower.usage > 0.0f && !building.block.outputsPower && building.power.graph != null && building.power.graph.producers.size == 0 && building.power.graph.batteries.size == 0;
    }

    private static boolean affordable(Block block, int n) {
        if (Vars.state.rules.infiniteResources) {
            return true;
        }
        if (Vars.player.closestCore() == null) {
            return false;
        }
        for (ItemStack itemStack : block.requirements) {
            if (Vars.player.closestCore().items.get(itemStack.item) >= itemStack.amount * n) continue;
            return false;
        }
        return true;
    }

    private static void update() {
        if (!PowerLink.enabled() || Vars.state == null || !Vars.state.isGame() || Vars.player == null || Time.time < next) {
            return;
        }
        next = Time.time + 300.0f;
        try {
            Unit unit = Vars.player.unit();
            if (unit == null || unit.dead || !unit.canBuild() || Vars.player.closestCore() == null || unit.plans().size > 1) {
                return;
            }
            Block block = Vars.content.block("power-node");
            Block block2 = Vars.content.block("large-power-node");
            if (block == null) {
                return;
            }
            Building building = null;
            float f = 1.0E9f;
            for (Building building2 : Groups.build) {
                Object object;
                float f2;
                if (building2.team != Vars.player.team() || !PowerLink.unpowered(building2) || (f2 = building2.dst((Position)unit) / 8.0f) > 40.0f || f2 >= f || (object = (Object)tries.get(building2.pos())) != null && (object[0] >= 2 || Time.time - (float)object[1] < 1800.0f)) continue;
                f = f2;
                building = building2;
            }
            if (building == null) {
                return;
            }
            Iterator iterator = null;
            float f3 = 1.0E9f;
            for (Object object : Groups.build) {
                float f4;
                if (object.team != Vars.player.team() || object.power == null || object.power.graph == building.power.graph || object.power.graph.producers.size == 0 || !((f4 = object.dst((Position)building) / 8.0f) < f3) || !(f4 <= 60.0f)) continue;
                f3 = f4;
                iterator = object;
            }
            Object object = tries.get(building.pos());
            if (object == null) {
                object = new int[]{0, 0};
            }
            Object object2 = object;
            object2[0] = object2[0] + true;
            object[1] = (int)Time.time;
            tries.put(building.pos(), (int[])object);
            if (iterator == null) {
                return;
            }
            PowerLink.plan(unit, building, (Building)iterator, block, block2);
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] PowerLink", (Throwable)throwable);
        }
    }

    private static boolean occupiedByPlan(Unit unit, int n, int n2) {
        for (BuildPlan buildPlan : unit.plans()) {
            if (buildPlan.block == null || Math.abs(buildPlan.x - n) > buildPlan.block.size / 2 || Math.abs(buildPlan.y - n2) > buildPlan.block.size / 2) continue;
            return true;
        }
        return false;
    }

    private static int[] snap(Unit unit, Block block, double d, double d2, ArrayList<int[]> arrayList) {
        int n = (int)Math.round(d);
        int n2 = (int)Math.round(d2);
        for (int i = 0; i <= 3; ++i) {
            for (int j = -i; j <= i; ++j) {
                for (int k = -i; k <= i; ++k) {
                    if (Math.max(Math.abs(k), Math.abs(j)) != i) continue;
                    int n3 = n + k;
                    int n4 = n2 + j;
                    boolean bl = PowerLink.occupiedByPlan(unit, n3, n4);
                    for (int[] nArray : arrayList) {
                        if (Math.abs(nArray[0] - n3) > 1 || Math.abs(nArray[1] - n4) > 1) continue;
                        bl = true;
                    }
                    if (bl || !Build.validPlace((Block)block, (Team)Vars.player.team(), (int)n3, (int)n4, (int)0)) continue;
                    return new int[]{n3, n4};
                }
            }
        }
        return null;
    }

    private static void plan(Unit unit, Building building, Building building2, Block block, Block block2) {
        int[] nArray;
        int n;
        double d;
        double d2 = building.tileX();
        double d3 = building.tileY();
        double d4 = building2.tileX();
        double d5 = building2.tileY();
        double d6 = Math.hypot(d4 - d2, d5 - d3);
        double d7 = (d4 - d2) / Math.max(d6, 0.01);
        double d8 = (d5 - d3) / Math.max(d6, 0.01);
        Block block3 = block;
        int n2 = 1;
        for (int i = 0; i < 2; ++i) {
            int n3;
            Block block4 = i == 0 && AutoSetConfig.bigNode() && block2 != null ? block2 : block;
            d = ((PowerNode)block4).laserRange;
            double d9 = Math.max(2.0, d - 2.0);
            double d10 = Math.max(2.0, (double)building.block.size / 2.0 + 1.5);
            for (n3 = 1; d6 - d10 > d - 1.5 && n3 < 14; ++n3) {
                d10 += d9;
            }
            if (n3 >= 14) {
                return;
            }
            if (block4 != block && !PowerLink.affordable(block4, n3)) continue;
            block3 = block4;
            n2 = n3;
            break;
        }
        if (!PowerLink.affordable(block3, n2)) {
            return;
        }
        double d11 = ((PowerNode)block3).laserRange;
        d = Math.max(2.0, d11 - 2.0);
        ArrayList<int[]> arrayList = new ArrayList<int[]>();
        double d12 = Math.max(2.0, (double)building.block.size / 2.0 + 1.5);
        for (n = 0; n < n2; ++n) {
            nArray = PowerLink.snap(unit, block3, d2 + d7 * d12, d3 + d8 * d12, arrayList);
            if (nArray == null) {
                return;
            }
            arrayList.add(nArray);
            d12 += d;
        }
        for (n = arrayList.size() - 1; n >= 0; --n) {
            nArray = (int[])arrayList.get(n);
            ArrayList<Point2> arrayList2 = new ArrayList<Point2>();
            if (n == arrayList.size() - 1) {
                arrayList2.add(new Point2(building2.tileX() - nArray[0], building2.tileY() - nArray[1]));
            } else {
                int[] nArray2 = arrayList.get(n + 1);
                arrayList2.add(new Point2(nArray2[0] - nArray[0], nArray2[1] - nArray[1]));
            }
            if (n == 0) {
                arrayList2.add(new Point2(building.tileX() - nArray[0], building.tileY() - nArray[1]));
            }
            unit.addBuild(new BuildPlan(nArray[0], nArray[1], 0, block3, (Object)arrayList2.toArray(new Point2[0])), true);
        }
        if (Vars.ui != null) {
            String string = LunariansI18n.getLanguage();
            Vars.ui.showInfoToast("pt".equals(string) ? "Auto energia: ligando " + building.block.localizedName + " (" + n2 + " nos)" : ("es".equals(string) ? "Auto energ\u00eda: conectando " + building.block.localizedName + " (" + n2 + " nodos)" : ("ru".equals(string) ? "\u0410\u0432\u0442\u043e\u044d\u043d\u0435\u0440\u0433\u0438\u044f: " + building.block.localizedName + " (" + n2 + ")" : "Auto power: linking " + building.block.localizedName + " (" + n2 + " nodes)")), 3.0f);
        }
    }

    static {
        tries = new HashMap();
        last = new HashMap();
    }
}

