package lunarians;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.math.geom.Point2;
import arc.util.Log;
import arc.util.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.blocks.power.PowerNode;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/PowerLink.class */
public class PowerLink {
    private static boolean registered;
    private static float next;
    private static final HashMap<Integer, int[]> tries = new HashMap<>();
    private static final HashMap<Integer, Float> last = new HashMap<>();

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.PowerLink$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            PowerLink.$r8$lambda$ghrdvfldFjPgWDo4FqJPgtgk7Ho();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.PowerLink$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Cons {
        public final void get(Object obj) {
            PowerLink.lambda$init$0((EventType.WorldLoadEvent) obj);
        }
    }

    public static boolean enabled() {
        return Core.settings.getBool("lunarians-auto-pnode", true);
    }

    public static void setEnabled(boolean z) {
        Core.settings.put("lunarians-auto-pnode", Boolean.valueOf(z));
    }

    public static void init() {
        if (registered) {
            return;
        }
        registered = true;
        Events.run(EventType.Trigger.update, PowerLink::update);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            tries.clear();
            last.clear();
        });
    }

    private static boolean unpowered(Building building) {
        return (building.power == null || building.block.consPower == null || building.block.consPower.buffered || building.block.consPower.usage <= 0.0f || building.block.outputsPower || building.power.graph == null || building.power.graph.producers.size != 0 || building.power.graph.batteries.size != 0) ? false : true;
    }

    private static boolean affordable(Block block, int i) {
        if (Vars.state.rules.infiniteResources) {
            return true;
        }
        if (Vars.player.closestCore() == null) {
            return false;
        }
        for (ItemStack itemStack : block.requirements) {
            if (Vars.player.closestCore().items.get(itemStack.item) < itemStack.amount * i) {
                return false;
            }
        }
        return true;
    }

    private static void update() {
        int[] iArr;
        if (!enabled() || Vars.state == null || !Vars.state.isGame() || Vars.player == null || Time.time < next) {
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
            Iterator it = Groups.build.iterator();
            while (it.hasNext()) {
                Building building2 = (Building) it.next();
                if (building2.team == Vars.player.team() && unpowered(building2)) {
                    float dst = building2.dst(unit) / 8.0f;
                    if (dst <= 40.0f && dst < f && ((iArr = tries.get(Integer.valueOf(building2.pos()))) == null || (iArr[0] < 2 && Time.time - iArr[1] >= 1800.0f))) {
                        f = dst;
                        building = building2;
                    }
                }
            }
            if (building == null) {
                return;
            }
            Building building3 = null;
            float f2 = 1.0E9f;
            Iterator it2 = Groups.build.iterator();
            while (it2.hasNext()) {
                Building building4 = (Building) it2.next();
                if (building4.team == Vars.player.team() && building4.power != null && building4.power.graph != building.power.graph && building4.power.graph.producers.size != 0) {
                    float dst2 = building4.dst(building) / 8.0f;
                    if (dst2 < f2 && dst2 <= 60.0f) {
                        f2 = dst2;
                        building3 = building4;
                    }
                }
            }
            int[] iArr2 = tries.get(Integer.valueOf(building.pos()));
            if (iArr2 == null) {
                iArr2 = new int[]{0, 0};
            }
            int[] iArr3 = iArr2;
            iArr3[0] = iArr3[0] + 1;
            iArr2[1] = (int) Time.time;
            tries.put(Integer.valueOf(building.pos()), iArr2);
            if (building3 == null) {
                return;
            }
            plan(unit, building, building3, block, block2);
        } catch (Throwable th) {
            Log.err("[Lunarians] PowerLink", th);
        }
    }

    private static boolean occupiedByPlan(Unit unit, int i, int i2) {
        Iterator it = unit.plans().iterator();
        while (it.hasNext()) {
            BuildPlan buildPlan = (BuildPlan) it.next();
            if (buildPlan.block != null && Math.abs(buildPlan.x - i) <= buildPlan.block.size / 2 && Math.abs(buildPlan.y - i2) <= buildPlan.block.size / 2) {
                return true;
            }
        }
        return false;
    }

    private static int[] snap(Unit unit, Block block, double d, double d2, ArrayList<int[]> arrayList) {
        int round = (int) Math.round(d);
        int round2 = (int) Math.round(d2);
        for (int i = 0; i <= 3; i++) {
            for (int i2 = -i; i2 <= i; i2++) {
                for (int i3 = -i; i3 <= i; i3++) {
                    if (Math.max(Math.abs(i3), Math.abs(i2)) == i) {
                        int i4 = round + i3;
                        int i5 = round2 + i2;
                        boolean occupiedByPlan = occupiedByPlan(unit, i4, i5);
                        Iterator<int[]> it = arrayList.iterator();
                        while (it.hasNext()) {
                            int[] next2 = it.next();
                            if (Math.abs(next2[0] - i4) <= 1 && Math.abs(next2[1] - i5) <= 1) {
                                occupiedByPlan = true;
                            }
                        }
                        if (!occupiedByPlan && Build.validPlace(block, Vars.player.team(), i4, i5, 0)) {
                            return new int[]{i4, i5};
                        }
                    }
                }
            }
        }
        return null;
    }

    private static void plan(Unit unit, Building building, Building building2, Block block, Block block2) {
        double tileX = building.tileX();
        double tileY = building.tileY();
        double tileX2 = building2.tileX();
        double tileY2 = building2.tileY();
        double hypot = Math.hypot(tileX2 - tileX, tileY2 - tileY);
        double max = (tileX2 - tileX) / Math.max(hypot, 0.01d);
        double max2 = (tileY2 - tileY) / Math.max(hypot, 0.01d);
        Block block3 = block;
        int i = 1;
        int i2 = 0;
        while (i2 < 2) {
            Block block4 = (i2 == 0 && AutoSetConfig.bigNode() && block2 != null) ? block2 : block;
            double d = ((PowerNode) block4).laserRange;
            double max3 = Math.max(2.0d, d - 2.0d);
            int i3 = 1;
            double max4 = Math.max(2.0d, (building.block.size / 2.0d) + 1.5d);
            while (hypot - max4 > d - 1.5d && i3 < 14) {
                max4 += max3;
                i3++;
            }
            if (i3 >= 14) {
                return;
            }
            if (block4 != block && !affordable(block4, i3)) {
                i2++;
            } else {
                block3 = block4;
                i = i3;
                break;
            }
        }
        if (affordable(block3, i)) {
            double max5 = Math.max(2.0d, ((PowerNode) block3).laserRange - 2.0d);
            ArrayList arrayList = new ArrayList();
            double max6 = Math.max(2.0d, (building.block.size / 2.0d) + 1.5d);
            for (int i4 = 0; i4 < i; i4++) {
                int[] snap = snap(unit, block3, tileX + (max * max6), tileY + (max2 * max6), arrayList);
                if (snap == null) {
                    return;
                }
                arrayList.add(snap);
                max6 += max5;
            }
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                int[] iArr = (int[]) arrayList.get(size);
                ArrayList arrayList2 = new ArrayList();
                if (size == arrayList.size() - 1) {
                    arrayList2.add(new Point2(building2.tileX() - iArr[0], building2.tileY() - iArr[1]));
                } else {
                    int[] iArr2 = (int[]) arrayList.get(size + 1);
                    arrayList2.add(new Point2(iArr2[0] - iArr[0], iArr2[1] - iArr[1]));
                }
                if (size == 0) {
                    arrayList2.add(new Point2(building.tileX() - iArr[0], building.tileY() - iArr[1]));
                }
                unit.addBuild(new BuildPlan(iArr[0], iArr[1], 0, block3, arrayList2.toArray(new Point2[0])), true);
            }
            if (Vars.ui != null) {
                String language = LunariansI18n.getLanguage();
                Vars.ui.showInfoToast("pt".equals(language) ? "Auto energia: ligando " + building.block.localizedName + " (" + i + " nos)" : "es".equals(language) ? "Auto energía: conectando " + building.block.localizedName + " (" + i + " nodos)" : "ru".equals(language) ? "Автоэнергия: " + building.block.localizedName + " (" + i + ")" : "Auto power: linking " + building.block.localizedName + " (" + i + " nodes)", 3.0f);
            }
        }
    }
}
