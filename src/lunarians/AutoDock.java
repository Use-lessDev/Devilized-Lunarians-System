package lunarians;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.core.World;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.game.Schematic;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.blocks.power.PowerGenerator;
import mindustry.world.blocks.power.PowerGraph;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.storage.StorageBlock;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoDock.class */
public class AutoDock {
    private static float centerX;
    private static float centerY;
    private static float phaseStart;
    private static float nextCheck;
    static final String[][] ORDER = {new String[]{"Field", "GP!3|GP!4|GP!2", "", "F", "coal"}, new String[]{"Field", "DrM!2|DrM!1|DrM!3", "", "F", "copper"}, new String[]{"Field", "DrM!3|DrM!4|DrM!1", "", "F", "lead"}, new String[]{"Early", "SP_GP10|GP!9", "coal:100"}, new String[]{"Early", "SP_SS23|SS24", "coal:100;sand:200"}, new String[]{"Early", "K17|K!2", "lead:200;sand:100"}, new String[]{"Medium", "PM!9", "coal:100;lead:100;sand:100"}, new String[]{"Medium", "MP17", "coal:300"}, new String[]{"Medium", "T2!4|T2!6", "silicon:300;graphite:100;lead:200;titanium:75", "U"}, new String[]{"Late", "PDG!3", "pyratite:100;titanium:100;coal:100;lead:100;sand:100"}, new String[]{"Late", "SP_PW16", "thorium:100;sand:300"}, new String[]{"Late", "PC17DS", "titanium:300;sand:300"}, new String[]{"Late", "SP_SC21", "coal:200;sand:300;lead:100"}, new String[]{"Late", "T3!3", "silicon:800;titanium:500;metaglass:200;graphite:300"}, new String[]{"End", "SP_SUS4", "copper:300;lead:300;titanium:300;silicon:300"}, new String[]{"End", "SP_T45", "silicon:1500;titanium:1500;metaglass:300;plastanium:300;graphite:300"}};
    private static int energyFails = 0;
    private static int energyBuilt = 0;
    private static boolean unitsOnly = false;
    private static boolean unitsFinished = false;
    private static boolean[] done = new boolean[ORDER.length];
    private static Phase phase = Phase.NEXT;
    private static int idx = 0;
    private static Seq<BuildPlan> current = new Seq<>();
    private static String lastToast = "";
    private static float lastToastAt = -999.0f;
    private static boolean energyStep = false;
    private static int stuckRetries = 0;
    private static boolean camHeld = false;
    private static boolean prevDetach = false;

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoDock$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements BiFunction {
        @Override // java.util.function.BiFunction
        public final Object apply(Object obj, Object obj2) {
            return Integer.valueOf(((Integer) obj).intValue() + ((Integer) obj2).intValue());
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoDock$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Cons {
        public final /* synthetic */ float f$0;

        public final void get(Object obj) {
            AutoDock.lambda$rotate$1(this.f$0, (Point2) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoDock$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements Cons {
        public final void get(Object obj) {
            AutoDock.reset();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoDock$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass4 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoDock.$r8$lambda$7__z32isPd_clOmLL6A5K5hyfnQ();
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoDock$Phase.class */
    private enum Phase {
        NEXT,
        MOVE,
        BUILD,
        GRID,
        DONE
    }

    public static void startUnits() {
        unitsOnly = true;
        unitsFinished = false;
        phase = Phase.NEXT;
        nextCheck = 0.0f;
    }

    public static boolean isUnitsMode() {
        return unitsOnly;
    }

    public static boolean unitsDone() {
        return unitsFinished;
    }

    public static void stopUnits() {
        unitsOnly = false;
        unitsFinished = false;
        releaseCamera();
        current = new Seq<>();
        phase = Phase.NEXT;
        nextCheck = Time.time + 60.0f;
    }

    public static void finishEconomy() {
        unitsOnly = false;
        unitsFinished = false;
        phase = Phase.NEXT;
        nextCheck = Time.time + 60.0f;
    }

    private static int pick() {
        for (int i = 0; i < ORDER.length; i++) {
            if (!done[i] && (!unitsOnly || (ORDER[i].length > 3 && "U".equals(ORDER[i][3])))) {
                return i;
            }
        }
        return -1;
    }

    public static void init() {
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            reset();
        });
        Events.run(EventType.Trigger.update, AutoDock::update);
    }

    public static void reset() {
        releaseCamera();
        done = new boolean[ORDER.length];
        unitsOnly = false;
        unitsFinished = false;
        phase = Phase.NEXT;
        idx = 0;
        current.clear();
        energyStep = false;
        energyFails = 0;
        energyBuilt = 0;
        stuckRetries = 0;
        nextCheck = 0.0f;
    }

    public static String coreInfo() {
        CoreBlock.CoreBuild closestCore = Vars.player == null ? null : Vars.player.closestCore();
        if (closestCore == null) {
            return "sem nucleo";
        }
        return closestCore.block.localizedName + " " + closestCore.block.size + "x" + closestCore.block.size + " | capacidade " + closestCore.storageCapacity + " | lados livres: " + freePerimeter(closestCore) + " tiles";
    }

    private static int freePerimeter(CoreBlock.CoreBuild coreBuild) {
        Tile tile;
        int i = 0;
        int[] rect = rect(coreBuild.tile.x, coreBuild.tile.y, coreBuild.block.size);
        int i2 = rect[0] - 1;
        while (i2 <= rect[2] + 1) {
            int i3 = rect[1] - 1;
            while (i3 <= rect[3] + 1) {
                if (((i2 >= rect[0] && i2 <= rect[2]) != (i3 >= rect[1] && i3 <= rect[3])) && (tile = Vars.world.tile(i2, i3)) != null && tile.build == null && !tile.solid()) {
                    i++;
                }
                i3++;
            }
            i2++;
        }
        return i;
    }

    private static void update() {
        CoreBlock.CoreBuild closestCore;
        Unit unit;
        if (!LunariansSettings.dockAuto) {
            releaseCamera();
            return;
        }
        if (Vars.state == null || !Vars.state.isGame() || Vars.player == null || Vars.player.dead()) {
            return;
        }
        if (((AutoSet.isActive() || AutoEconomy.active) && !unitsOnly) || (closestCore = Vars.player.closestCore()) == null || (unit = Vars.player.unit()) == null || unit.type.buildSpeed <= 0.0f) {
            return;
        }
        try {
            switch (phase) {
                case NEXT:
                    stepNext(closestCore, unit);
                    break;
                case MOVE:
                    stepMove(unit);
                    break;
                case BUILD:
                    stepBuild(unit);
                    break;
                case GRID:
                    stepGrid(closestCore, unit);
                    break;
            }
        } catch (Throwable th) {
            Log.err("[Lunarians] AutoDock", th);
            phase = Phase.NEXT;
            nextCheck = Time.time + 300.0f;
        }
    }

    private static boolean unloadersAllowed() {
        try {
            return Vars.state.rules.getClass().getField("allowCoreUnloaders").getBoolean(Vars.state.rules);
        } catch (Throwable th) {
            return true;
        }
    }

    private static void stepNext(CoreBlock.CoreBuild coreBuild, Unit unit) {
        if (Time.time < nextCheck) {
            return;
        }
        nextCheck = Time.time + 90.0f;
        if (!unloadersAllowed()) {
            toast("Auto Dock: este servidor desativou unloaders no nucleo (allowCoreUnloaders). Os schematics encostados nao funcionam aqui.");
            return;
        }
        idx = pick();
        if (idx < 0) {
            if (unitsOnly) {
                unitsFinished = true;
                return;
            }
            toast("Auto Dock: ordem completa.");
            releaseCamera();
            phase = Phase.DONE;
            return;
        }
        String[] strArr = ORDER[idx];
        Schematic find = find(strArr[1]);
        if (find == null) {
            toast("Auto Dock: schematic nao encontrado (" + strArr[1] + "), pulando.");
            done[idx] = true;
            return;
        }
        Seq plans = Vars.schematics.toPlans(find, 0, 0, false);
        if (plans.size < find.tiles.size) {
            toast("Auto Dock: " + strArr[1] + " tem blocos bloqueados, pulando.");
            done[idx] = true;
            return;
        }
        boolean z = strArr.length > 4 && "F".equals(strArr[3]);
        if (!z && alreadyBuilt(plans, coreBuild)) {
            done[idx] = true;
            return;
        }
        String missing = missing(coreBuild, plans, strArr[2]);
        if (missing != null) {
            toast("Auto Dock [" + strArr[0] + "] " + strArr[1] + ": falta " + missing);
            return;
        }
        if (!Vars.state.rules.infiniteResources) {
            int i = 0;
            Iterator it = plans.iterator();
            while (it.hasNext()) {
                if (((BuildPlan) it.next()).block == Blocks.unloader) {
                    i++;
                }
            }
            boolean z2 = strArr.length > 3 && "U".equals(strArr[3]);
            if (i > 0 || z2) {
                int i2 = (30 * i) + (z2 ? 150 : 30);
                int i3 = (25 * i) + (z2 ? 100 : 15);
                int i4 = coreBuild.items.get(Items.silicon);
                int i5 = coreBuild.items.get(Items.titanium);
                if (i4 < i2 || i5 < i3) {
                    toast("Auto Dock [" + strArr[0] + "] " + strArr[1] + ": esperando silicio " + i4 + "/" + i2 + " e titanio " + i5 + "/" + i3);
                    return;
                }
            }
        }
        float powerNeed = powerNeed(plans) * 60.0f;
        boolean z3 = powerNeed > 0.0f && supplyLeft() < powerNeed * 1.05f;
        if (!z3) {
            energyFails = 0;
        } else if (energyFails >= 2) {
            toast("Auto Energy: geradores nao produzem (sem carvao no nucleo?). Seguindo sem energia extra.");
            z3 = false;
        }
        if (z3 && energyBuilt >= 3) {
            toast("Auto Energy: limite de 3 grupos de geradores atingido. Seguindo sem energia extra.");
            z3 = false;
        }
        if (z3) {
            Seq<BuildPlan> energyCluster = energyCluster(coreBuild);
            if (energyCluster == null) {
                toast("Auto Energy: sem espaco para gerador junto ao nucleo.");
                return;
            }
            String missing2 = missing(coreBuild, energyCluster, "coal:300");
            if (missing2 != null) {
                toast("Auto Energy: falta " + missing2);
                return;
            }
            toast("Auto Energy: construindo geradores (" + Mathf.round(powerNeed) + " energia/s necessaria).");
            energyFails++;
            energyBuilt++;
            startBuild(energyCluster, true);
            return;
        }
        Seq<BuildPlan> fieldPlan = z ? fieldPlan(coreBuild, plans, strArr[4], unit) : bestDock(coreBuild, plans, unit);
        if (fieldPlan == null) {
            toast(z ? "Auto Dock: sem campo de " + strArr[4] + " livre para " + strArr[1] : "Auto Dock: sem lugar encostado no nucleo para " + strArr[1] + " (" + coreInfo() + ")");
            nextCheck = Time.time + 600.0f;
            int i6 = stuckRetries + 1;
            stuckRetries = i6;
            if (i6 >= 12) {
                done[idx] = true;
                stuckRetries = 0;
                return;
            }
            return;
        }
        stuckRetries = 0;
        toast("Auto Dock [" + strArr[0] + "]: construindo " + strArr[1]);
        startBuild(fieldPlan, false);
    }

    private static void startBuild(Seq<BuildPlan> seq, boolean z) {
        current = seq;
        energyStep = z;
        float f = 0.0f;
        float f2 = 0.0f;
        Iterator it = seq.iterator();
        while (it.hasNext()) {
            BuildPlan buildPlan = (BuildPlan) it.next();
            f += buildPlan.x;
            f2 += buildPlan.y;
        }
        centerX = (f / seq.size) * 8.0f;
        centerY = (f2 / seq.size) * 8.0f;
        phase = Phase.MOVE;
        phaseStart = Time.time;
    }

    private static void stepMove(Unit unit) {
        if (Mathf.dst(unit.x, unit.y, centerX, centerY) <= unit.type.buildRange * 0.6f) {
            Iterator it = current.iterator();
            while (it.hasNext()) {
                BuildPlan buildPlan = (BuildPlan) it.next();
                if (!isBuilt(buildPlan)) {
                    unit.addBuild(buildPlan.copy(), true);
                }
            }
            phase = Phase.BUILD;
            phaseStart = Time.time;
            nextCheck = Time.time + 60.0f;
            return;
        }
        cameraGo(unit, centerX, centerY);
        if (Time.time - phaseStart > 2400.0f) {
            abort("nao consegui chegar na obra");
        }
    }

    private static void cameraGo(Unit unit, float f, float f2) {
        if (!camHeld) {
            camHeld = true;
            prevDetach = Core.settings.getBool("detach-camera", false);
            Core.camera.position.set(unit.x, unit.y);
        }
        Core.settings.put("detach-camera", true);
        float f3 = Core.camera.position.x;
        float f4 = Core.camera.position.y;
        float dst = Mathf.dst(f3, f4, f, f2);
        if (dst < 1.0f) {
            return;
        }
        float min = Math.min(dst, Math.max(2.0f, unit.speed() * 1.25f) * Time.delta);
        Core.camera.position.set(f3 + (((f - f3) / dst) * min), f4 + (((f2 - f4) / dst) * min));
    }

    private static void releaseCamera() {
        if (camHeld) {
            Core.settings.put("detach-camera", Boolean.valueOf(prevDetach));
            camHeld = false;
        }
    }

    private static void stepBuild(Unit unit) {
        cameraGo(unit, centerX, centerY);
        if (Time.time < nextCheck) {
            return;
        }
        nextCheck = Time.time + 60.0f;
        boolean z = true;
        Iterator it = current.iterator();
        while (it.hasNext()) {
            BuildPlan buildPlan = (BuildPlan) it.next();
            if (!isBuilt(buildPlan)) {
                z = false;
                boolean z2 = false;
                Iterator it2 = unit.plans().iterator();
                while (it2.hasNext()) {
                    BuildPlan buildPlan2 = (BuildPlan) it2.next();
                    if (buildPlan2.x == buildPlan.x && buildPlan2.y == buildPlan.y) {
                        z2 = true;
                    }
                }
                if (!z2 && Build.validPlace(buildPlan.block, Vars.player.team(), buildPlan.x, buildPlan.y, buildPlan.rotation)) {
                    unit.addBuild(buildPlan.copy(), true);
                }
            }
        }
        if (z) {
            phase = Phase.GRID;
            phaseStart = Time.time;
            nextCheck = Time.time + 30.0f;
        } else if (Time.time - phaseStart > 9000.0f) {
            abort("obra travada (recursos acabaram ou area bloqueada)");
        }
    }

    private static void abort(String str) {
        toast("Auto Dock: " + str + ". Tentando de novo depois.");
        Vars.player.unit().plans().clear();
        releaseCamera();
        phase = Phase.NEXT;
        nextCheck = Time.time + 1200.0f;
    }

    private static void stepGrid(CoreBlock.CoreBuild coreBuild, Unit unit) {
        if (Time.time < nextCheck) {
            return;
        }
        nextCheck = Time.time + 60.0f;
        ObjectSet objectSet = new ObjectSet();
        Seq seq = new Seq();
        Iterator it = current.iterator();
        while (it.hasNext()) {
            BuildPlan buildPlan = (BuildPlan) it.next();
            Building building = buildPlan.tile() == null ? null : buildPlan.tile().build;
            if (building != null && building.power != null) {
                objectSet.add(building.power.graph);
                seq.add(building);
            }
        }
        if (seq.isEmpty() || (energyStep && seq.size < 1)) {
            finishStep();
            return;
        }
        boolean z = false;
        ObjectSet.ObjectSetIterator it2 = objectSet.iterator();
        while (it2.hasNext()) {
            if (((PowerGraph) it2.next()).producers.size > 0) {
                z = true;
            }
        }
        Building building2 = null;
        Building building3 = null;
        float f = 1.0E9f;
        Iterator it3 = Groups.build.iterator();
        while (it3.hasNext()) {
            Building building4 = (Building) it3.next();
            if (building4.team == Vars.player.team() && building4.power != null && !objectSet.contains(building4.power.graph) && (building4.power.graph.producers.size != 0 || building4.power.graph.batteries.size != 0)) {
                Iterator it4 = seq.iterator();
                while (it4.hasNext()) {
                    Building building5 = (Building) it4.next();
                    float dst = Mathf.dst(building4.tileX(), building4.tileY(), building5.tileX(), building5.tileY());
                    if (dst < f) {
                        f = dst;
                        building2 = building5;
                        building3 = building4;
                    }
                }
            }
        }
        if (building3 == null || f > 30.0f || (z && !energyStep && supplyLeft() > 0.0f && f > 40.0f)) {
            finishStep();
            return;
        }
        Seq<BuildPlan> nodeChain = nodeChain(building2, building3);
        if (nodeChain == null || nodeChain.isEmpty()) {
            finishStep();
            return;
        }
        toast("Auto Power Node: ligando a energia (" + nodeChain.size + " nos).");
        current = nodeChain;
        float f2 = 0.0f;
        float f3 = 0.0f;
        Iterator it5 = nodeChain.iterator();
        while (it5.hasNext()) {
            BuildPlan buildPlan2 = (BuildPlan) it5.next();
            f2 += buildPlan2.x;
            f3 += buildPlan2.y;
        }
        centerX = (f2 / nodeChain.size) * 8.0f;
        centerY = (f3 / nodeChain.size) * 8.0f;
        energyStep = true;
        phase = Phase.MOVE;
        phaseStart = Time.time;
    }

    private static void finishStep() {
        if (!energyStep && idx >= 0 && idx < done.length) {
            done[idx] = true;
        }
        energyStep = false;
        current = new Seq<>();
        releaseCamera();
        phase = Phase.NEXT;
        nextCheck = Time.time + 60.0f;
    }

    private static Seq<BuildPlan> nodeChain(Building building, Building building2) {
        Seq<BuildPlan> seq = new Seq<>();
        float tileX = building2.tileX() - building.tileX();
        float tileY = building2.tileY() - building.tileY();
        int max = Math.max(1, ((int) Math.ceil(Mathf.dst(tileX, tileY) / 5.0f)) - 1);
        if (max > 5) {
            return null;
        }
        for (int i = 1; i <= max; i++) {
            float f = i / (max + 1);
            int[] freeSpot = freeSpot(Blocks.powerNode, Mathf.round(building.tileX() + (tileX * f)), Mathf.round(building.tileY() + (tileY * f)), 3);
            if (freeSpot == null) {
                return null;
            }
            seq.add(new BuildPlan(freeSpot[0], freeSpot[1], 0, Blocks.powerNode));
        }
        return seq;
    }

    private static int[] freeSpot(Block block, int i, int i2, int i3) {
        for (int i4 = 0; i4 <= i3; i4++) {
            for (int i5 = i - i4; i5 <= i + i4; i5++) {
                for (int i6 = i2 - i4; i6 <= i2 + i4; i6++) {
                    if (Build.validPlace(block, Vars.player.team(), i5, i6, 0)) {
                        return new int[]{i5, i6};
                    }
                }
            }
        }
        return null;
    }

    private static float supplyLeft() {
        ObjectSet objectSet = new ObjectSet();
        Iterator it = Groups.build.iterator();
        while (it.hasNext()) {
            Building building = (Building) it.next();
            if (building.team == Vars.player.team() && building.power != null && building.power.graph != null) {
                objectSet.add(building.power.graph);
            }
        }
        float f = 0.0f;
        ObjectSet.ObjectSetIterator it2 = objectSet.iterator();
        while (it2.hasNext()) {
            PowerGraph powerGraph = (PowerGraph) it2.next();
            if (powerGraph.producers.size != 0) {
                float f2 = 0.0f;
                Iterator it3 = powerGraph.producers.iterator();
                while (it3.hasNext()) {
                    Building building2 = (Building) it3.next();
                    f2 += building2.block instanceof PowerGenerator ? building2.block.powerProduction : 0.0f;
                }
                float f3 = 0.0f;
                Iterator it4 = powerGraph.consumers.iterator();
                while (it4.hasNext()) {
                    Building building3 = (Building) it4.next();
                    if (building3.block.consPower != null && !building3.block.consPower.buffered) {
                        f3 += building3.block.consPower.usage;
                    }
                }
                f += f2 - f3;
            }
        }
        return f * 60.0f;
    }

    private static float powerNeed(Seq<BuildPlan> seq) {
        float f = 0.0f;
        Iterator it = seq.iterator();
        while (it.hasNext()) {
            BuildPlan buildPlan = (BuildPlan) it.next();
            if (buildPlan.block.consPower != null && !buildPlan.block.consPower.buffered) {
                f += buildPlan.block.consPower.usage;
            }
        }
        return f;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static Seq<BuildPlan> energyCluster(CoreBlock.CoreBuild coreBuild) {
        int[] freeSpot;
        int[] rect = rect(coreBuild.tile.x, coreBuild.tile.y, coreBuild.block.size);
        int[] iArr = {new int[]{1, 0}, new int[]{0, 1}, new int[]{-1, 0}, new int[]{0, -1}};
        int i = rect[0] - 1;
        while (i <= rect[2] + 1) {
            int i2 = rect[1] - 1;
            while (i2 <= rect[3] + 1) {
                if ((i >= rect[0] && i <= rect[2]) != (i2 >= rect[1] && i2 <= rect[3])) {
                    int i3 = i < rect[0] ? -1 : i > rect[2] ? 1 : 0;
                    int i4 = i2 < rect[1] ? -1 : i2 > rect[3] ? 1 : 0;
                    Seq<BuildPlan> seq = new Seq<>();
                    seq.add(new BuildPlan(i, i2, 0, Blocks.unloader, Items.coal));
                    boolean validPlace = Build.validPlace(Blocks.unloader, Vars.player.team(), i, i2, 0);
                    int i5 = 0;
                    for (Object[] objArr : iArr) {
                        if (objArr[0] != (-i3) || objArr[1] != (-i4)) {
                            int i6 = i + objArr[0];
                            int i7 = i2 + objArr[1];
                            if (Build.validPlace(Blocks.combustionGenerator, Vars.player.team(), i6, i7, 0)) {
                                seq.add(new BuildPlan(i6, i7, 0, Blocks.combustionGenerator));
                                i5++;
                            }
                        }
                    }
                    if (validPlace && i5 >= 2 && (freeSpot = freeSpot(Blocks.powerNode, i + (i3 * 3), i2 + (i4 * 3), 3)) != null) {
                        seq.add(new BuildPlan(freeSpot[0], freeSpot[1], 0, Blocks.powerNode));
                        return seq;
                    }
                }
                i2++;
            }
            i++;
        }
        return null;
    }

    private static Seq<BuildPlan> fieldPlan(CoreBlock.CoreBuild coreBuild, Seq<BuildPlan> seq, String str, Unit unit) {
        BuildPlan buildPlan;
        try {
            Item item = Vars.content.item(str);
            if (item == null) {
                return null;
            }
            int width = Vars.world.width();
            int height = Vars.world.height();
            int tileX = coreBuild.tileX();
            int tileY = coreBuild.tileY();
            boolean[] zArr = new boolean[width * height];
            for (int max = Math.max(0, (tileY - 55) - 20); max <= Math.min(height - 1, tileY + 55 + 20); max++) {
                for (int max2 = Math.max(0, (tileX - 55) - 20); max2 <= Math.min(width - 1, tileX + 55 + 20); max2++) {
                    Tile tile = Vars.world.tile(max2, max);
                    if (tile != null && tile.build == null && !tile.solid() && tile.drop() == item) {
                        zArr[(max * width) + max2] = true;
                    }
                }
            }
            boolean[] reservedFromPlans = AutoConnectConveyor.reservedFromPlans(unit);
            Seq<BuildPlan> copyAll = copyAll(seq);
            int i = 0;
            Iterator it = copyAll.iterator();
            while (it.hasNext()) {
                if (((BuildPlan) it.next()).block instanceof Drill) {
                    i++;
                }
            }
            if (i == 0) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < 4; i2++) {
                int[] bounds = bounds(copyAll);
                int i3 = (bounds[0] + bounds[2]) / 2;
                int i4 = (bounds[1] + bounds[3]) / 2;
                for (int i5 = (tileX - 55) - i3; i5 <= (tileX + 55) - i3; i5++) {
                    for (int i6 = (tileY - 55) - i4; i6 <= (tileY + 55) - i4; i6++) {
                        int i7 = 0;
                        Iterator it2 = copyAll.iterator();
                        while (it2.hasNext()) {
                            BuildPlan buildPlan2 = (BuildPlan) it2.next();
                            if (buildPlan2.block instanceof Drill) {
                                int[] rect = rect(buildPlan2.x + i5, buildPlan2.y + i6, buildPlan2.block.size);
                                int i8 = 0;
                                for (int i9 = rect[0]; i9 <= rect[2]; i9++) {
                                    for (int i10 = rect[1]; i10 <= rect[3]; i10++) {
                                        if (i9 >= 0 && i10 >= 0 && i9 < width && i10 < height && zArr[(i10 * width) + i9]) {
                                            i8++;
                                        }
                                    }
                                }
                                if (i8 * 4 >= buildPlan2.block.size * buildPlan2.block.size * 3) {
                                    i7++;
                                }
                            }
                        }
                        if (i7 * 10 >= i * 6) {
                            arrayList.add(new float[]{(i7 * 10.0f) - (Mathf.dst(i3 + i5, i4 + i6, tileX, tileY) * 0.3f), i2, i5, i6});
                        }
                    }
                }
                rotate(copyAll);
            }
            Collections.sort(arrayList, new Comparator<float[]>() { // from class: lunarians.AutoDock.1
                @Override // java.util.Comparator
                public int compare(float[] fArr, float[] fArr2) {
                    return Float.compare(fArr2[0], fArr[0]);
                }
            });
            ArrayList<int[]> dockRects = dockRects(coreBuild);
            int i11 = 0;
            Iterator it3 = arrayList.iterator();
            while (it3.hasNext()) {
                float[] fArr = (float[]) it3.next();
                int i12 = i11;
                i11++;
                if (i12 < 40) {
                    Seq<BuildPlan> copyAll2 = copyAll(seq);
                    for (int i13 = 0; i13 < ((int) fArr[1]); i13++) {
                        rotate(copyAll2);
                    }
                    int i14 = (int) fArr[2];
                    int i15 = (int) fArr[3];
                    HashSet hashSet = new HashSet();
                    boolean z = true;
                    Iterator it4 = copyAll2.iterator();
                    do {
                        if (!it4.hasNext()) {
                            break;
                        }
                        buildPlan = (BuildPlan) it4.next();
                        buildPlan.x += i14;
                        buildPlan.y += i15;
                        int[] rect2 = rect(buildPlan.x, buildPlan.y, buildPlan.block.size);
                        if (overlapsAny(new int[]{rect2[0] - 2, rect2[1] - 2, rect2[2] + 2, rect2[3] + 2}, dockRects)) {
                            z = false;
                            break;
                        }
                        for (int i16 = rect2[0]; i16 <= rect2[2] && z; i16++) {
                            for (int i17 = rect2[1]; i17 <= rect2[3]; i17++) {
                                if (i16 < 0 || i17 < 0 || i16 >= width || i17 >= height || reservedFromPlans[(i17 * width) + i16]) {
                                    z = false;
                                    break;
                                }
                                hashSet.add(Integer.valueOf((i17 * width) + i16));
                            }
                        }
                        if (!z) {
                            break;
                        }
                    } while (Build.validPlace(buildPlan.block, Vars.player.team(), buildPlan.x, buildPlan.y, buildPlan.rotation));
                    z = false;
                    if (z) {
                        int[] iArr = null;
                        float f = 1.0E9f;
                        int[] iArr2 = {1, 0, -1, 0};
                        int[] iArr3 = {0, 1, 0, -1};
                        Iterator it5 = copyAll2.iterator();
                        while (it5.hasNext()) {
                            BuildPlan buildPlan3 = (BuildPlan) it5.next();
                            if (buildPlan3.block instanceof Conveyor) {
                                int i18 = buildPlan3.x + iArr2[buildPlan3.rotation & 3];
                                int i19 = buildPlan3.y + iArr3[buildPlan3.rotation & 3];
                                if (Vars.world.tiles.in(i18, i19) && !hashSet.contains(Integer.valueOf((i19 * width) + i18))) {
                                    float dst = Mathf.dst(i18, i19, tileX, tileY);
                                    if (dst < f) {
                                        f = dst;
                                        iArr = new int[]{i18, i19, buildPlan3.rotation & 3};
                                    }
                                }
                            }
                        }
                        if (iArr != null) {
                            boolean[] zArr2 = (boolean[]) reservedFromPlans.clone();
                            Iterator it6 = copyAll2.iterator();
                            while (it6.hasNext()) {
                                AutoConnectConveyor.markPlan((BuildPlan) it6.next(), zArr2);
                            }
                            Seq<BuildPlan> planRoute = AutoConnectConveyor.planRoute(iArr[0], iArr[1], iArr[2], zArr2);
                            if (planRoute == null || planRoute.isEmpty()) {
                                planRoute = AutoConnectConveyor.planRoute(iArr[0], iArr[1], -1, zArr2);
                            }
                            if (planRoute != null && !planRoute.isEmpty()) {
                                Seq<BuildPlan> seq2 = new Seq<>();
                                seq2.addAll(copyAll2);
                                seq2.addAll(planRoute);
                                return seq2;
                            }
                        }
                    }
                } else {
                    return null;
                }
            }
            return null;
        } catch (Throwable th) {
            Log.err("[Lunarians] fieldPlan", th);
            return null;
        }
    }

    private static Seq<BuildPlan> bestDock(CoreBlock.CoreBuild coreBuild, Seq<BuildPlan> seq, Unit unit) {
        Seq<BuildPlan> bestDockImpl = bestDockImpl(coreBuild, seq, unit, true);
        return bestDockImpl != null ? bestDockImpl : bestDockImpl(coreBuild, seq, unit, false);
    }

    private static ArrayList<int[]> dockRects(CoreBlock.CoreBuild coreBuild) {
        ArrayList<int[]> arrayList = new ArrayList<>();
        arrayList.add(rect(coreBuild.tile.x, coreBuild.tile.y, coreBuild.block.size));
        try {
            Iterator it = coreBuild.proximity.iterator();
            while (it.hasNext()) {
                StorageBlock.StorageBuild storageBuild = (Building) it.next();
                if ((storageBuild instanceof StorageBlock.StorageBuild) && storageBuild.linkedCore == coreBuild) {
                    arrayList.add(rect(((Building) storageBuild).tile.x, ((Building) storageBuild).tile.y, ((Building) storageBuild).block.size));
                }
            }
        } catch (Throwable th) {
        }
        return arrayList;
    }

    private static boolean touchesAny(BuildPlan buildPlan, int i, int i2, ArrayList<int[]> arrayList) {
        Iterator<int[]> it = arrayList.iterator();
        while (it.hasNext()) {
            if (touches(buildPlan, i, i2, it.next())) {
                return true;
            }
        }
        return false;
    }

    private static boolean overlapsAny(int[] iArr, ArrayList<int[]> arrayList) {
        Iterator<int[]> it = arrayList.iterator();
        while (it.hasNext()) {
            int[] next = it.next();
            if (iArr[2] >= next[0] && iArr[0] <= next[2] && iArr[3] >= next[1] && iArr[1] <= next[3]) {
                return true;
            }
        }
        return false;
    }

    private static int ringCovered(Seq<BuildPlan> seq, int i, int i2, ArrayList<int[]> arrayList) {
        int i3 = 0;
        Iterator it = seq.iterator();
        while (it.hasNext()) {
            BuildPlan buildPlan = (BuildPlan) it.next();
            int[] rect = rect(buildPlan.x + i, buildPlan.y + i2, buildPlan.block.size);
            int i4 = rect[0];
            while (i4 <= rect[2]) {
                int i5 = rect[1];
                while (i5 <= rect[3]) {
                    Iterator<int[]> it2 = arrayList.iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            int[] next = it2.next();
                            if ((i4 >= next[0] && i4 <= next[2]) != (i5 >= next[1] && i5 <= next[3]) && i4 >= next[0] - 1 && i4 <= next[2] + 1 && i5 >= next[1] - 1 && i5 <= next[3] + 1) {
                                i3++;
                                break;
                            }
                        }
                    }
                    i5++;
                }
                i4++;
            }
        }
        return i3;
    }

    /* JADX WARN: Removed duplicated region for block: B:69:0x021f  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x02ca A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static Seq<BuildPlan> bestDockImpl(CoreBlock.CoreBuild coreBuild, Seq<BuildPlan> seq, Unit unit, boolean z) {
        float max;
        ArrayList<int[]> dockRects = dockRects(coreBuild);
        int i = 1048576;
        int i2 = 1048576;
        int i3 = -1048576;
        int i4 = -1048576;
        Iterator<int[]> it = dockRects.iterator();
        while (it.hasNext()) {
            int[] next = it.next();
            i = Math.min(i, next[0]);
            i2 = Math.min(i2, next[1]);
            i3 = Math.max(i3, next[2]);
            i4 = Math.max(i4, next[3]);
        }
        int i5 = 0;
        boolean z2 = false;
        Iterator it2 = seq.iterator();
        while (it2.hasNext()) {
            BuildPlan buildPlan = (BuildPlan) it2.next();
            if (buildPlan.block == Blocks.unloader) {
                i5++;
            }
            if (buildPlan.block instanceof StorageBlock) {
                z2 = true;
            }
        }
        Seq<BuildPlan> seq2 = null;
        float f = -1.0E9f;
        Seq<BuildPlan> copyAll = copyAll(seq);
        for (int i6 = 0; i6 < 4; i6++) {
            int[] bounds = bounds(copyAll);
            for (int i7 = (i - bounds[2]) - 1; i7 <= (i3 - bounds[0]) + 1; i7++) {
                for (int i8 = (i2 - bounds[3]) - 1; i8 <= (i4 - bounds[1]) + 1; i8++) {
                    int i9 = 0;
                    int i10 = 0;
                    boolean z3 = false;
                    Iterator it3 = copyAll.iterator();
                    while (true) {
                        if (!it3.hasNext()) {
                            break;
                        }
                        BuildPlan buildPlan2 = (BuildPlan) it3.next();
                        if (overlapsAny(rect(buildPlan2.x + i7, buildPlan2.y + i8, buildPlan2.block.size), dockRects)) {
                            z3 = true;
                            break;
                        }
                        if (touchesAny(buildPlan2, i7, i8, dockRects)) {
                            if (buildPlan2.block instanceof StorageBlock) {
                                i9++;
                            } else if (buildPlan2.block == Blocks.unloader) {
                                i10++;
                            }
                        }
                    }
                    if (!z3) {
                        if (!z2) {
                            if (i10 == 0) {
                            }
                            if (z || z2 || i10 >= i5) {
                                max = (((i9 * 100.0f) + (i10 * 10.0f)) - (3.0f * Math.max(0, (ringCovered(copyAll, i7, i8, dockRects) - i9) - i10))) - (Mathf.dst(((bounds[0] + bounds[2]) / 2.0f) + i7, ((bounds[1] + bounds[3]) / 2.0f) + i8, unit.tileX(), unit.tileY()) * 0.01f);
                                if (max <= f) {
                                    boolean z4 = true;
                                    Iterator it4 = copyAll.iterator();
                                    while (true) {
                                        if (!it4.hasNext()) {
                                            break;
                                        }
                                        BuildPlan buildPlan3 = (BuildPlan) it4.next();
                                        if (!Build.validPlace(buildPlan3.block, Vars.player.team(), buildPlan3.x + i7, buildPlan3.y + i8, buildPlan3.rotation)) {
                                            z4 = false;
                                            break;
                                        }
                                    }
                                    if (z4) {
                                        f = max;
                                        seq2 = new Seq<>();
                                        Iterator it5 = copyAll.iterator();
                                        while (it5.hasNext()) {
                                            BuildPlan copy = ((BuildPlan) it5.next()).copy();
                                            copy.x += i7;
                                            copy.y += i8;
                                            seq2.add(copy);
                                        }
                                    }
                                }
                            }
                        } else {
                            if (i9 == 0) {
                            }
                            if (z) {
                            }
                            max = (((i9 * 100.0f) + (i10 * 10.0f)) - (3.0f * Math.max(0, (ringCovered(copyAll, i7, i8, dockRects) - i9) - i10))) - (Mathf.dst(((bounds[0] + bounds[2]) / 2.0f) + i7, ((bounds[1] + bounds[3]) / 2.0f) + i8, unit.tileX(), unit.tileY()) * 0.01f);
                            if (max <= f) {
                            }
                        }
                    }
                }
            }
            rotate(copyAll);
        }
        return seq2;
    }

    private static boolean touches(BuildPlan buildPlan, int i, int i2, int[] iArr) {
        int[] rect = rect(buildPlan.x + i, buildPlan.y + i2, buildPlan.block.size);
        return ((rect[2] >= iArr[0] && rect[0] <= iArr[2]) && (rect[3] + 1 == iArr[1] || rect[1] - 1 == iArr[3])) || ((rect[3] >= iArr[1] && rect[1] <= iArr[3]) && (rect[2] + 1 == iArr[0] || rect[0] - 1 == iArr[2]));
    }

    private static int[] rect(int i, int i2, int i3) {
        int i4 = -((i3 - 1) / 2);
        return new int[]{i + i4, i2 + i4, ((i + i4) + i3) - 1, ((i2 + i4) + i3) - 1};
    }

    private static int[] bounds(Seq<BuildPlan> seq) {
        int i = 1048576;
        int i2 = 1048576;
        int i3 = -1048576;
        int i4 = -1048576;
        Iterator it = seq.iterator();
        while (it.hasNext()) {
            BuildPlan buildPlan = (BuildPlan) it.next();
            int[] rect = rect(buildPlan.x, buildPlan.y, buildPlan.block.size);
            i = Math.min(i, rect[0]);
            i2 = Math.min(i2, rect[1]);
            i3 = Math.max(i3, rect[2]);
            i4 = Math.max(i4, rect[3]);
        }
        return new int[]{i, i2, i3, i4};
    }

    private static Seq<BuildPlan> copyAll(Seq<BuildPlan> seq) {
        Seq<BuildPlan> seq2 = new Seq<>();
        Iterator it = seq.iterator();
        while (it.hasNext()) {
            seq2.add(((BuildPlan) it.next()).copy());
        }
        return seq2;
    }

    private static void rotate(Seq<BuildPlan> seq) {
        Iterator it = seq.iterator();
        while (it.hasNext()) {
            BuildPlan buildPlan = (BuildPlan) it.next();
            float f = buildPlan.block.size % 2 == 0 ? -0.5f : 0.0f;
            buildPlan.pointConfig(point2 -> {
                point2.set(Mathf.floor((-(point2.y + f)) - f), Mathf.floor((point2.x + f) - f));
            });
            float f2 = (buildPlan.x * 8.0f) + buildPlan.block.offset;
            buildPlan.x = World.toTile((-((buildPlan.y * 8.0f) + buildPlan.block.offset)) - buildPlan.block.offset);
            buildPlan.y = World.toTile(f2 - buildPlan.block.offset);
            buildPlan.rotation = buildPlan.block.planRotation(Mathf.mod(buildPlan.rotation + 1, 4));
        }
    }

    private static Schematic find(String str) {
        for (String str2 : str.split("\\|")) {
            Iterator it = Vars.schematics.all().iterator();
            while (it.hasNext()) {
                Schematic schematic = (Schematic) it.next();
                if (str2.equals(schematic.name())) {
                    return schematic;
                }
            }
        }
        return null;
    }

    private static boolean isBuilt(BuildPlan buildPlan) {
        Tile tile = Vars.world.tile(buildPlan.x, buildPlan.y);
        return tile != null && tile.build != null && tile.block() == buildPlan.block && tile.team() == Vars.player.team();
    }

    private static boolean alreadyBuilt(Seq<BuildPlan> seq, CoreBlock.CoreBuild coreBuild) {
        Block block = null;
        int i = 0;
        Iterator it = seq.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (((BuildPlan) next).block.consumers.length > 0 && ((BuildPlan) next).block.size >= 2 && !(((BuildPlan) next).block instanceof StorageBlock)) {
                int i2 = 0;
                Iterator it2 = seq.iterator();
                while (it2.hasNext()) {
                    if (((BuildPlan) it2.next()).block == ((BuildPlan) next).block) {
                        i2++;
                    }
                }
                if (i2 > i) {
                    i = i2;
                    block = ((BuildPlan) next).block;
                }
            }
        }
        if (block == null) {
            return false;
        }
        int i3 = 0;
        Iterator it3 = Groups.build.iterator();
        while (it3.hasNext()) {
            Building building = (Building) it3.next();
            if (building.team == Vars.player.team() && building.block == block && building.within(coreBuild, 480.0f)) {
                i3++;
            }
        }
        return i3 >= i;
    }

    private static String missing(CoreBlock.CoreBuild coreBuild, Seq<BuildPlan> seq, String str) {
        int i;
        if (Vars.state.rules.infiniteResources) {
            return null;
        }
        HashMap hashMap = new HashMap();
        Iterator it = seq.iterator();
        while (it.hasNext()) {
            for (ItemStack itemStack : ((BuildPlan) it.next()).block.requirements) {
                hashMap.merge(itemStack.item, Integer.valueOf(Mathf.ceil(r0.amount * Vars.state.rules.buildCostMultiplier)), (v0, v1) -> {
                    return Integer.sum(v0, v1);
                });
            }
        }
        for (Map.Entry entry : hashMap.entrySet()) {
            if (((Integer) entry.getValue()).intValue() > coreBuild.storageCapacity) {
                return ((Item) entry.getKey()).localizedName + " (precisa " + entry.getValue() + " > capacidade " + coreBuild.storageCapacity + " do nucleo)";
            }
        }
        HashMap hashMap2 = new HashMap();
        if (str != null && !str.isEmpty()) {
            for (String str2 : str.split(";")) {
                String[] split = str2.split(":");
                Item item = Vars.content.item(split[0]);
                if (item != null) {
                    hashMap2.put(item, Integer.valueOf(Integer.parseInt(split[1])));
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry2 : hashMap.entrySet()) {
            int i2 = coreBuild.items.get((Item) entry2.getKey());
            Integer num = (Integer) hashMap2.get(entry2.getKey());
            int intValue = ((Integer) entry2.getValue()).intValue() + (num == null ? 0 : num.intValue());
            if (i2 < intValue) {
                sb.append(((Item) entry2.getKey()).localizedName).append(' ').append(intValue - i2).append("  ");
            }
        }
        for (Map.Entry entry3 : hashMap2.entrySet()) {
            if (!hashMap.containsKey(entry3.getKey()) && (i = coreBuild.items.get((Item) entry3.getKey())) < ((Integer) entry3.getValue()).intValue()) {
                sb.append(((Item) entry3.getKey()).localizedName).append(' ').append(((Integer) entry3.getValue()).intValue() - i).append(" (alimentacao)  ");
            }
        }
        if (sb.length() == 0) {
            return null;
        }
        return sb.toString().trim();
    }

    private static void toast(String str) {
        if ((str.equals(lastToast) && Time.time - lastToastAt < 1800.0f) || Time.time - lastToastAt < 240.0f) {
            return;
        }
        lastToast = str;
        lastToastAt = Time.time;
        Log.info("[Lunarians] " + str);
        if (Vars.ui != null) {
            Vars.ui.showInfoToast(str, 4.0f);
        }
    }
}
