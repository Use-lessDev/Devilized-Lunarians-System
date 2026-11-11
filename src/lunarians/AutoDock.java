/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.math.Mathf
 *  arc.math.geom.Position
 *  arc.struct.ObjectSet
 *  arc.struct.Seq
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.content.Blocks
 *  mindustry.content.Items
 *  mindustry.core.World
 *  mindustry.entities.units.BuildPlan
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.game.Schematic
 *  mindustry.game.Team
 *  mindustry.gen.Building
 *  mindustry.gen.Groups
 *  mindustry.gen.Unit
 *  mindustry.type.Item
 *  mindustry.type.ItemStack
 *  mindustry.world.Block
 *  mindustry.world.Build
 *  mindustry.world.Tile
 *  mindustry.world.blocks.distribution.Conveyor
 *  mindustry.world.blocks.power.PowerGenerator
 *  mindustry.world.blocks.power.PowerGraph
 *  mindustry.world.blocks.production.Drill
 *  mindustry.world.blocks.storage.CoreBlock$CoreBuild
 *  mindustry.world.blocks.storage.StorageBlock
 *  mindustry.world.blocks.storage.StorageBlock$StorageBuild
 */
package lunarians;

import arc.Core;
import arc.Events;
import arc.math.Mathf;
import arc.math.geom.Position;
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
import lunarians.AutoConnectConveyor;
import lunarians.AutoEconomy;
import lunarians.AutoSet;
import lunarians.LunariansSettings;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.core.World;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.game.Schematic;
import mindustry.game.Team;
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

public class AutoDock {
    static final String[][] ORDER = new String[][]{{"Field", "GP!3|GP!4|GP!2", "", "F", "coal"}, {"Field", "DrM!2|DrM!1|DrM!3", "", "F", "copper"}, {"Field", "DrM!3|DrM!4|DrM!1", "", "F", "lead"}, {"Early", "SP_GP10|GP!9", "coal:100"}, {"Early", "SP_SS23|SS24", "coal:100;sand:200"}, {"Early", "K17|K!2", "lead:200;sand:100"}, {"Medium", "PM!9", "coal:100;lead:100;sand:100"}, {"Medium", "MP17", "coal:300"}, {"Medium", "T2!4|T2!6", "silicon:300;graphite:100;lead:200;titanium:75", "U"}, {"Late", "PDG!3", "pyratite:100;titanium:100;coal:100;lead:100;sand:100"}, {"Late", "SP_PW16", "thorium:100;sand:300"}, {"Late", "PC17DS", "titanium:300;sand:300"}, {"Late", "SP_SC21", "coal:200;sand:300;lead:100"}, {"Late", "T3!3", "silicon:800;titanium:500;metaglass:200;graphite:300"}, {"End", "SP_SUS4", "copper:300;lead:300;titanium:300;silicon:300"}, {"End", "SP_T45", "silicon:1500;titanium:1500;metaglass:300;plastanium:300;graphite:300"}};
    private static int energyFails = 0;
    private static int energyBuilt = 0;
    private static boolean unitsOnly = false;
    private static boolean unitsFinished = false;
    private static boolean[] done = new boolean[ORDER.length];
    private static Phase phase = Phase.NEXT;
    private static int idx = 0;
    private static Seq<BuildPlan> current = new Seq();
    private static float centerX;
    private static float centerY;
    private static float phaseStart;
    private static float nextCheck;
    private static String lastToast;
    private static float lastToastAt;
    private static boolean energyStep;
    private static int stuckRetries;
    private static boolean camHeld;
    private static boolean prevDetach;

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
        AutoDock.releaseCamera();
        current = new Seq();
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
        for (int i = 0; i < ORDER.length; ++i) {
            if (done[i] || unitsOnly && (ORDER[i].length <= 3 || !"U".equals(ORDER[i][3]))) continue;
            return i;
        }
        return -1;
    }

    public static void init() {
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> AutoDock.reset());
        Events.run((Object)EventType.Trigger.update, AutoDock::update);
    }

    public static void reset() {
        AutoDock.releaseCamera();
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
        CoreBlock.CoreBuild coreBuild = Vars.player == null ? null : Vars.player.closestCore();
        CoreBlock.CoreBuild coreBuild2 = coreBuild;
        if (coreBuild == null) {
            return "sem nucleo";
        }
        int n = AutoDock.freePerimeter(coreBuild);
        return coreBuild.block.localizedName + " " + coreBuild.block.size + "x" + coreBuild.block.size + " | capacidade " + coreBuild.storageCapacity + " | lados livres: " + n + " tiles";
    }

    private static int freePerimeter(CoreBlock.CoreBuild coreBuild) {
        int n = 0;
        int[] nArray = AutoDock.rect(coreBuild.tile.x, coreBuild.tile.y, coreBuild.block.size);
        for (int i = nArray[0] - 1; i <= nArray[2] + 1; ++i) {
            for (int j = nArray[1] - 1; j <= nArray[3] + 1; ++j) {
                Tile tile;
                boolean bl = (i >= nArray[0] && i <= nArray[2]) != (j >= nArray[1] && j <= nArray[3]);
                boolean bl2 = bl;
                if (!bl || (tile = Vars.world.tile(i, j)) == null || tile.build != null || tile.solid()) continue;
                ++n;
            }
        }
        return n;
    }

    private static void update() {
        if (!LunariansSettings.dockAuto) {
            AutoDock.releaseCamera();
            return;
        }
        if (Vars.state == null || !Vars.state.isGame() || Vars.player == null || Vars.player.dead()) {
            return;
        }
        if ((AutoSet.isActive() || AutoEconomy.active) && !unitsOnly) {
            return;
        }
        CoreBlock.CoreBuild coreBuild = Vars.player.closestCore();
        if (coreBuild == null) {
            return;
        }
        Unit unit = Vars.player.unit();
        if (unit == null || unit.type.buildSpeed <= 0.0f) {
            return;
        }
        try {
            switch (phase.ordinal()) {
                case 0: {
                    AutoDock.stepNext(coreBuild, unit);
                    break;
                }
                case 1: {
                    AutoDock.stepMove(unit);
                    break;
                }
                case 2: {
                    AutoDock.stepBuild(unit);
                    break;
                }
                case 3: {
                    AutoDock.stepGrid(coreBuild, unit);
                }
            }
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] AutoDock", (Throwable)throwable);
            phase = Phase.NEXT;
            nextCheck = Time.time + 300.0f;
        }
    }

    private static boolean unloadersAllowed() {
        try {
            return Vars.state.rules.getClass().getField("allowCoreUnloaders").getBoolean(Vars.state.rules);
        }
        catch (Throwable throwable) {
            return true;
        }
    }

    private static void stepNext(CoreBlock.CoreBuild coreBuild, Unit unit) {
        Seq<BuildPlan> seq;
        float f;
        boolean bl;
        boolean bl2;
        if (Time.time < nextCheck) {
            return;
        }
        nextCheck = Time.time + 90.0f;
        if (!AutoDock.unloadersAllowed()) {
            AutoDock.toast("Auto Dock: este servidor desativou unloaders no nucleo (allowCoreUnloaders). Os schematics encostados nao funcionam aqui.");
            return;
        }
        idx = AutoDock.pick();
        if (idx < 0) {
            if (unitsOnly) {
                unitsFinished = true;
                return;
            }
            AutoDock.toast("Auto Dock: ordem completa.");
            AutoDock.releaseCamera();
            phase = Phase.DONE;
            return;
        }
        String[] stringArray = ORDER[idx];
        Schematic schematic = AutoDock.find(stringArray[1]);
        if (schematic == null) {
            AutoDock.toast("Auto Dock: schematic nao encontrado (" + stringArray[1] + "), pulando.");
            AutoDock.done[AutoDock.idx] = true;
            return;
        }
        Seq seq2 = Vars.schematics.toPlans(schematic, 0, 0, false);
        if (seq2.size < schematic.tiles.size) {
            AutoDock.toast("Auto Dock: " + stringArray[1] + " tem blocos bloqueados, pulando.");
            AutoDock.done[AutoDock.idx] = true;
            return;
        }
        boolean bl3 = bl2 = stringArray.length > 4 && "F".equals(stringArray[3]);
        if (!bl2 && AutoDock.alreadyBuilt((Seq<BuildPlan>)seq2, coreBuild)) {
            AutoDock.done[AutoDock.idx] = true;
            return;
        }
        String string = AutoDock.missing(coreBuild, (Seq<BuildPlan>)seq2, stringArray[2]);
        if (string != null) {
            AutoDock.toast("Auto Dock [" + stringArray[0] + "] " + stringArray[1] + ": falta " + string);
            return;
        }
        if (!Vars.state.rules.infiniteResources) {
            int n = 0;
            for (Object e : seq2) {
                if (((BuildPlan)e).block != Blocks.unloader) continue;
                ++n;
            }
            boolean bl4 = bl = stringArray.length > 3 && "U".equals(stringArray[3]);
            if (n > 0 || bl) {
                int n2 = 30 * n + (bl ? 150 : 30);
                int n3 = 25 * n + (bl ? 100 : 15);
                int n4 = coreBuild.items.get(Items.silicon);
                int n5 = coreBuild.items.get(Items.titanium);
                if (n4 < n2 || n5 < n3) {
                    AutoDock.toast("Auto Dock [" + stringArray[0] + "] " + stringArray[1] + ": esperando silicio " + n4 + "/" + n2 + " e titanio " + n5 + "/" + n3);
                    return;
                }
            }
        }
        boolean bl5 = bl = (f = AutoDock.powerNeed((Seq<BuildPlan>)seq2) * 60.0f) > 0.0f && AutoDock.supplyLeft() < f * 1.05f;
        if (!bl) {
            energyFails = 0;
        } else if (energyFails >= 2) {
            AutoDock.toast("Auto Energy: geradores nao produzem (sem carvao no nucleo?). Seguindo sem energia extra.");
            bl = false;
        }
        if (bl && energyBuilt >= 3) {
            AutoDock.toast("Auto Energy: limite de 3 grupos de geradores atingido. Seguindo sem energia extra.");
            bl = false;
        }
        if (bl) {
            Seq<BuildPlan> seq3 = AutoDock.energyCluster(coreBuild);
            if (seq3 == null) {
                AutoDock.toast("Auto Energy: sem espaco para gerador junto ao nucleo.");
                return;
            }
            String string2 = AutoDock.missing(coreBuild, seq3, "coal:300");
            if (string2 != null) {
                AutoDock.toast("Auto Energy: falta " + string2);
                return;
            }
            AutoDock.toast("Auto Energy: construindo geradores (" + Mathf.round((float)f) + " energia/s necessaria).");
            ++energyFails;
            ++energyBuilt;
            AutoDock.startBuild(seq3, true);
            return;
        }
        Seq<BuildPlan> seq4 = seq = bl2 ? AutoDock.fieldPlan(coreBuild, (Seq<BuildPlan>)seq2, stringArray[4], unit) : AutoDock.bestDock(coreBuild, (Seq<BuildPlan>)seq2, unit);
        if (seq == null) {
            AutoDock.toast(bl2 ? "Auto Dock: sem campo de " + stringArray[4] + " livre para " + stringArray[1] : "Auto Dock: sem lugar encostado no nucleo para " + stringArray[1] + " (" + AutoDock.coreInfo() + ")");
            nextCheck = Time.time + 600.0f;
            if (++stuckRetries >= 12) {
                AutoDock.done[AutoDock.idx] = true;
                stuckRetries = 0;
            }
            return;
        }
        stuckRetries = 0;
        AutoDock.toast("Auto Dock [" + stringArray[0] + "]: construindo " + stringArray[1]);
        AutoDock.startBuild(seq, false);
    }

    private static void startBuild(Seq<BuildPlan> seq, boolean bl) {
        current = seq;
        energyStep = bl;
        float f = 0.0f;
        float f2 = 0.0f;
        for (BuildPlan buildPlan : seq) {
            f += (float)buildPlan.x;
            f2 += (float)buildPlan.y;
        }
        centerX = f / (float)seq.size * 8.0f;
        centerY = f2 / (float)seq.size * 8.0f;
        phase = Phase.MOVE;
        phaseStart = Time.time;
    }

    private static void stepMove(Unit unit) {
        float f = unit.type.buildRange * 0.6f;
        float f2 = Mathf.dst((float)unit.x, (float)unit.y, (float)centerX, (float)centerY);
        if (f2 <= f) {
            for (BuildPlan buildPlan : current) {
                if (AutoDock.isBuilt(buildPlan)) continue;
                unit.addBuild(buildPlan.copy(), true);
            }
            phase = Phase.BUILD;
            phaseStart = Time.time;
            nextCheck = Time.time + 60.0f;
            return;
        }
        AutoDock.cameraGo(unit, centerX, centerY);
        if (Time.time - phaseStart > 2400.0f) {
            AutoDock.abort("nao consegui chegar na obra");
        }
    }

    private static void cameraGo(Unit unit, float f, float f2) {
        if (!camHeld) {
            camHeld = true;
            prevDetach = Core.settings.getBool("detach-camera", false);
            Core.camera.position.set(unit.x, unit.y);
        }
        Core.settings.put("detach-camera", (Object)true);
        float f3 = Core.camera.position.x;
        float f4 = Core.camera.position.y;
        float f5 = Mathf.dst((float)f3, (float)f4, (float)f, (float)f2);
        if (f5 < 1.0f) {
            return;
        }
        float f6 = Math.min(f5, Math.max(2.0f, unit.speed() * 1.25f) * Time.delta);
        Core.camera.position.set(f3 + (f - f3) / f5 * f6, f4 + (f2 - f4) / f5 * f6);
    }

    private static void releaseCamera() {
        if (camHeld) {
            Core.settings.put("detach-camera", (Object)prevDetach);
            camHeld = false;
        }
    }

    private static void stepBuild(Unit unit) {
        AutoDock.cameraGo(unit, centerX, centerY);
        if (Time.time < nextCheck) {
            return;
        }
        nextCheck = Time.time + 60.0f;
        boolean bl = true;
        for (BuildPlan buildPlan : current) {
            if (AutoDock.isBuilt(buildPlan)) continue;
            bl = false;
            boolean bl2 = false;
            for (BuildPlan buildPlan2 : unit.plans()) {
                if (buildPlan2.x != buildPlan.x || buildPlan2.y != buildPlan.y) continue;
                bl2 = true;
            }
            if (bl2 || !Build.validPlace((Block)buildPlan.block, (Team)Vars.player.team(), (int)buildPlan.x, (int)buildPlan.y, (int)buildPlan.rotation)) continue;
            unit.addBuild(buildPlan.copy(), true);
        }
        if (bl) {
            phase = Phase.GRID;
            phaseStart = Time.time;
            nextCheck = Time.time + 30.0f;
        } else if (Time.time - phaseStart > 9000.0f) {
            AutoDock.abort("obra travada (recursos acabaram ou area bloqueada)");
        }
    }

    private static void abort(String string) {
        AutoDock.toast("Auto Dock: " + string + ". Tentando de novo depois.");
        Vars.player.unit().plans().clear();
        AutoDock.releaseCamera();
        phase = Phase.NEXT;
        nextCheck = Time.time + 1200.0f;
    }

    private static void stepGrid(CoreBlock.CoreBuild coreBuild, Unit unit) {
        Building building2;
        Object object2;
        if (Time.time < nextCheck) {
            return;
        }
        nextCheck = Time.time + 60.0f;
        ObjectSet objectSet = new ObjectSet();
        Seq seq = new Seq();
        for (Object object2 : current) {
            building2 = object2.tile() == null ? null : object2.tile().build;
            if (building2 == null || building2.power == null) continue;
            objectSet.add((Object)building2.power.graph);
            seq.add((Object)building2);
        }
        if (seq.isEmpty() || energyStep && seq.size < 1) {
            AutoDock.finishStep();
            return;
        }
        boolean bl = false;
        for (Building building2 : objectSet) {
            if (building2.producers.size <= 0) continue;
            bl = true;
        }
        object2 = null;
        building2 = null;
        float f = 1.0E9f;
        for (Building building3 : Groups.build) {
            if (building3.team != Vars.player.team() || building3.power == null || objectSet.contains((Object)building3.power.graph) || building3.power.graph.producers.size == 0 && building3.power.graph.batteries.size == 0) continue;
            for (Object object3 : seq) {
                float f2 = Mathf.dst((float)building3.tileX(), (float)building3.tileY(), (float)object3.tileX(), (float)object3.tileY());
                if (!(f2 < f)) continue;
                f = f2;
                object2 = object3;
                building2 = building3;
            }
        }
        if (building2 == null || f > 30.0f || bl && !energyStep && AutoDock.supplyLeft() > 0.0f && f > 40.0f) {
            AutoDock.finishStep();
            return;
        }
        Seq<BuildPlan> seq2 = AutoDock.nodeChain((Building)object2, building2);
        if (seq2 == null || seq2.isEmpty()) {
            AutoDock.finishStep();
            return;
        }
        AutoDock.toast("Auto Power Node: ligando a energia (" + seq2.size + " nos).");
        current = seq2;
        float f3 = 0.0f;
        float f4 = 0.0f;
        for (BuildPlan buildPlan : seq2) {
            f3 += (float)buildPlan.x;
            f4 += (float)buildPlan.y;
        }
        centerX = f3 / (float)seq2.size * 8.0f;
        centerY = f4 / (float)seq2.size * 8.0f;
        energyStep = true;
        phase = Phase.MOVE;
        phaseStart = Time.time;
    }

    private static void finishStep() {
        if (!energyStep && idx >= 0 && idx < done.length) {
            AutoDock.done[AutoDock.idx] = true;
        }
        energyStep = false;
        current = new Seq();
        AutoDock.releaseCamera();
        phase = Phase.NEXT;
        nextCheck = Time.time + 60.0f;
    }

    private static Seq<BuildPlan> nodeChain(Building building, Building building2) {
        float f;
        Seq seq = new Seq();
        float f2 = building2.tileX() - building.tileX();
        float f3 = Mathf.dst((float)f2, (float)(f = (float)(building2.tileY() - building.tileY())));
        int n = Math.max(1, (int)Math.ceil(f3 / 5.0f) - 1);
        if (n > 5) {
            return null;
        }
        for (int i = 1; i <= n; ++i) {
            int n2;
            float f4 = (float)i / (float)(n + 1);
            int n3 = Mathf.round((float)((float)building.tileX() + f2 * f4));
            int[] nArray = AutoDock.freeSpot(Blocks.powerNode, n3, n2 = Mathf.round((float)((float)building.tileY() + f * f4)), 3);
            if (nArray == null) {
                return null;
            }
            seq.add((Object)new BuildPlan(nArray[0], nArray[1], 0, Blocks.powerNode));
        }
        return seq;
    }

    private static int[] freeSpot(Block block, int n, int n2, int n3) {
        for (int i = 0; i <= n3; ++i) {
            for (int j = n - i; j <= n + i; ++j) {
                for (int k = n2 - i; k <= n2 + i; ++k) {
                    if (!Build.validPlace((Block)block, (Team)Vars.player.team(), (int)j, (int)k, (int)0)) continue;
                    return new int[]{j, k};
                }
            }
        }
        return null;
    }

    private static float supplyLeft() {
        ObjectSet objectSet = new ObjectSet();
        for (Building building : Groups.build) {
            if (building.team != Vars.player.team() || building.power == null || building.power.graph == null) continue;
            objectSet.add((Object)building.power.graph);
        }
        float f = 0.0f;
        for (PowerGraph powerGraph : objectSet) {
            if (powerGraph.producers.size == 0) continue;
            float f2 = 0.0f;
            for (Object object : powerGraph.producers) {
                f2 += ((Building)object).block instanceof PowerGenerator ? ((PowerGenerator)((Building)object).block).powerProduction : 0.0f;
            }
            float f3 = 0.0f;
            for (Building building : powerGraph.consumers) {
                if (building.block.consPower == null || building.block.consPower.buffered) continue;
                f3 += building.block.consPower.usage;
            }
            f += f2 - f3;
        }
        return f * 60.0f;
    }

    private static float powerNeed(Seq<BuildPlan> seq) {
        float f = 0.0f;
        for (BuildPlan buildPlan : seq) {
            if (buildPlan.block.consPower == null || buildPlan.block.consPower.buffered) continue;
            f += buildPlan.block.consPower.usage;
        }
        return f;
    }

    private static Seq<BuildPlan> energyCluster(CoreBlock.CoreBuild coreBuild) {
        int[] nArray = AutoDock.rect(coreBuild.tile.x, coreBuild.tile.y, coreBuild.block.size);
        int[][] nArrayArray = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        for (int i = nArray[0] - 1; i <= nArray[2] + 1; ++i) {
            for (int j = nArray[1] - 1; j <= nArray[3] + 1; ++j) {
                int n;
                boolean bl = i >= nArray[0] && i <= nArray[2];
                boolean bl2 = j >= nArray[1] && j <= nArray[3];
                boolean bl3 = bl2;
                if (bl == bl2) continue;
                int n2 = i < nArray[0] ? -1 : (n = i > nArray[2] ? 1 : 0);
                int n3 = j < nArray[1] ? -1 : (j > nArray[3] ? 1 : 0);
                Seq seq = new Seq();
                seq.add((Object)new BuildPlan(i, j, 0, Blocks.unloader, (Object)Items.coal));
                boolean bl4 = Build.validPlace((Block)Blocks.unloader, (Team)Vars.player.team(), (int)i, (int)j, (int)0);
                int n4 = 0;
                for (int[] nArray2 : nArrayArray) {
                    if (nArray2[0] == -n && nArray2[1] == -n3) continue;
                    int n5 = i + nArray2[0];
                    int n6 = j + nArray2[1];
                    if (!Build.validPlace((Block)Blocks.combustionGenerator, (Team)Vars.player.team(), (int)n5, (int)n6, (int)0)) continue;
                    seq.add((Object)new BuildPlan(n5, n6, 0, Blocks.combustionGenerator));
                    ++n4;
                }
                int[] nArray3 = null;
                if (!bl4 || n4 < 2 || (nArray3 = AutoDock.freeSpot(Blocks.powerNode, i + n * 3, j + n3 * 3, 3)) == null) continue;
                seq.add((Object)new BuildPlan(nArray3[0], nArray3[1], 0, Blocks.powerNode));
                return seq;
            }
        }
        return null;
    }

    private static Seq<BuildPlan> fieldPlan(CoreBlock.CoreBuild coreBuild, Seq<BuildPlan> seq, String string, Unit unit) {
        try {
            Object object;
            int n;
            int n2;
            Item item = Vars.content.item(string);
            if (item == null) {
                return null;
            }
            int n3 = Vars.world.width();
            int n4 = Vars.world.height();
            int n5 = coreBuild.tileX();
            int n6 = coreBuild.tileY();
            int n7 = 55;
            boolean[] blArray = new boolean[n3 * n4];
            for (int i = Math.max(0, n6 - n7 - 20); i <= Math.min(n4 - 1, n6 + n7 + 20); ++i) {
                for (int j = Math.max(0, n5 - n7 - 20); j <= Math.min(n3 - 1, n5 + n7 + 20); ++j) {
                    Tile tile = Vars.world.tile(j, i);
                    if (tile == null || tile.build != null || tile.solid() || tile.drop() != item) continue;
                    blArray[i * n3 + j] = true;
                }
            }
            boolean[] blArray2 = AutoConnectConveyor.reservedFromPlans(unit);
            Seq<BuildPlan> seq2 = AutoDock.copyAll(seq);
            int n8 = 0;
            for (BuildPlan buildPlan : seq2) {
                if (!(buildPlan.block instanceof Drill)) continue;
                ++n8;
            }
            if (n8 == 0) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < 4; ++i) {
                int[] nArray = AutoDock.bounds(seq2);
                int n9 = (nArray[0] + nArray[2]) / 2;
                int n10 = (nArray[1] + nArray[3]) / 2;
                for (int j = n5 - n7 - n9; j <= n5 + n7 - n9; ++j) {
                    for (n2 = n6 - n7 - n10; n2 <= n6 + n7 - n10; ++n2) {
                        n = 0;
                        for (BuildPlan buildPlan : seq2) {
                            if (!(buildPlan.block instanceof Drill)) continue;
                            object = AutoDock.rect(buildPlan.x + j, buildPlan.y + n2, buildPlan.block.size);
                            int n11 = 0;
                            for (reference var26_42 = object[0]; var26_42 <= object[2]; ++var26_42) {
                                for (reference var27_45 = object[1]; var27_45 <= object[3]; ++var27_45) {
                                    if (var26_42 < 0 || var27_45 < 0 || var26_42 >= n3 || var27_45 >= n4 || !blArray[var27_45 * n3 + var26_42]) continue;
                                    ++n11;
                                }
                            }
                            if (n11 * 4 < buildPlan.block.size * buildPlan.block.size * 3) continue;
                            ++n;
                        }
                        if (n * 10 < n8 * 6) continue;
                        float f = Mathf.dst((float)(n9 + j), (float)(n10 + n2), (float)n5, (float)n6);
                        arrayList.add(new float[]{(float)n * 10.0f - f * 0.3f, i, j, n2});
                    }
                }
                AutoDock.rotate(seq2);
            }
            Collections.sort(arrayList, new Comparator<float[]>(){

                @Override
                public int compare(float[] fArray, float[] fArray2) {
                    return Float.compare(fArray2[0], fArray[0]);
                }
            });
            ArrayList<int[]> arrayList2 = AutoDock.dockRects(coreBuild);
            int n12 = 0;
            Iterator iterator = arrayList.iterator();
            while (iterator.hasNext()) {
                Object object22;
                int[] nArray;
                float[] fArray = (float[])iterator.next();
                if (n12++ >= 40) break;
                Seq<BuildPlan> seq3 = AutoDock.copyAll(seq);
                for (n2 = 0; n2 < (int)fArray[1]; ++n2) {
                    AutoDock.rotate(seq3);
                }
                n2 = (int)fArray[2];
                n = (int)fArray[3];
                HashSet hashSet = new HashSet();
                boolean bl = true;
                for (BuildPlan buildPlan : seq3) {
                    buildPlan.x += n2;
                    buildPlan.y += n;
                    nArray = AutoDock.rect(buildPlan.x, buildPlan.y, buildPlan.block.size);
                    if (AutoDock.overlapsAny(new int[]{nArray[0] - 2, nArray[1] - 2, nArray[2] + 2, nArray[3] + 2}, arrayList2)) {
                        bl = false;
                        break;
                    }
                    block14: for (int i = nArray[0]; i <= nArray[2] && bl; ++i) {
                        for (int j = nArray[1]; j <= nArray[3]; ++j) {
                            if (i < 0 || j < 0 || i >= n3 || j >= n4 || blArray2[j * n3 + i]) {
                                bl = false;
                                continue block14;
                            }
                            hashSet.add(j * n3 + i);
                        }
                    }
                    if (bl && Build.validPlace((Block)buildPlan.block, (Team)Vars.player.team(), (int)buildPlan.x, (int)buildPlan.y, (int)buildPlan.rotation)) continue;
                    bl = false;
                    break;
                }
                if (!bl) continue;
                object = null;
                float f = 1.0E9f;
                nArray = new int[]{1, 0, -1, 0};
                int[] nArray2 = new int[]{0, 1, 0, -1};
                for (Object object22 : seq3) {
                    float f2;
                    int n13;
                    int n14;
                    if (!(((BuildPlan)object22).block instanceof Conveyor) || !Vars.world.tiles.in(n14 = ((BuildPlan)object22).x + nArray[((BuildPlan)object22).rotation & 3], n13 = ((BuildPlan)object22).y + nArray2[((BuildPlan)object22).rotation & 3]) || hashSet.contains(n13 * n3 + n14) || !((f2 = Mathf.dst((float)n14, (float)n13, (float)n5, (float)n6)) < f)) continue;
                    f = f2;
                    object = new int[]{n14, n13, ((BuildPlan)object22).rotation & 3};
                }
                if (object == null) continue;
                Object object3 = (boolean[])blArray2.clone();
                for (BuildPlan buildPlan : seq3) {
                    AutoConnectConveyor.markPlan(buildPlan, (boolean[])object3);
                }
                object22 = AutoConnectConveyor.planRoute((int)object[0], (int)object[1], (int)object[2], (boolean[])object3);
                if (object22 == null || object22.isEmpty()) {
                    object22 = AutoConnectConveyor.planRoute((int)object[0], (int)object[1], -1, (boolean[])object3);
                }
                if (object22 == null || object22.isEmpty()) continue;
                Seq seq4 = new Seq();
                seq4.addAll(seq3);
                seq4.addAll((Seq)object22);
                return seq4;
            }
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] fieldPlan", (Throwable)throwable);
        }
        return null;
    }

    private static Seq<BuildPlan> bestDock(CoreBlock.CoreBuild coreBuild, Seq<BuildPlan> seq, Unit unit) {
        Seq<BuildPlan> seq2 = AutoDock.bestDockImpl(coreBuild, seq, unit, true);
        return seq2 != null ? seq2 : AutoDock.bestDockImpl(coreBuild, seq, unit, false);
    }

    private static ArrayList<int[]> dockRects(CoreBlock.CoreBuild coreBuild) {
        ArrayList<int[]> arrayList = new ArrayList<int[]>();
        arrayList.add(AutoDock.rect(coreBuild.tile.x, coreBuild.tile.y, coreBuild.block.size));
        try {
            for (Building building : coreBuild.proximity) {
                if (!(building instanceof StorageBlock.StorageBuild) || ((StorageBlock.StorageBuild)building).linkedCore != coreBuild) continue;
                arrayList.add(AutoDock.rect(building.tile.x, building.tile.y, building.block.size));
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return arrayList;
    }

    private static boolean touchesAny(BuildPlan buildPlan, int n, int n2, ArrayList<int[]> arrayList) {
        for (int[] nArray : arrayList) {
            if (!AutoDock.touches(buildPlan, n, n2, nArray)) continue;
            return true;
        }
        return false;
    }

    private static boolean overlapsAny(int[] nArray, ArrayList<int[]> arrayList) {
        for (int[] nArray2 : arrayList) {
            if (nArray[2] < nArray2[0] || nArray[0] > nArray2[2] || nArray[3] < nArray2[1] || nArray[1] > nArray2[3]) continue;
            return true;
        }
        return false;
    }

    private static int ringCovered(Seq<BuildPlan> seq, int n, int n2, ArrayList<int[]> arrayList) {
        int n3 = 0;
        for (BuildPlan buildPlan : seq) {
            int[] nArray = AutoDock.rect(buildPlan.x + n, buildPlan.y + n2, buildPlan.block.size);
            for (int i = nArray[0]; i <= nArray[2]; ++i) {
                block2: for (int j = nArray[1]; j <= nArray[3]; ++j) {
                    for (int[] nArray2 : arrayList) {
                        boolean bl;
                        boolean bl2 = i >= nArray2[0] && i <= nArray2[2];
                        if (bl2 == (bl = j >= nArray2[1] && j <= nArray2[3]) || i < nArray2[0] - 1 || i > nArray2[2] + 1 || j < nArray2[1] - 1 || j > nArray2[3] + 1) continue;
                        ++n3;
                        continue block2;
                    }
                }
            }
        }
        return n3;
    }

    private static Seq<BuildPlan> bestDockImpl(CoreBlock.CoreBuild coreBuild, Seq<BuildPlan> seq, Unit unit, boolean bl) {
        ArrayList<int[]> arrayList = AutoDock.dockRects(coreBuild);
        int n = 0x100000;
        int n2 = 0x100000;
        int n3 = -1048576;
        int n4 = -1048576;
        for (int[] nArray : arrayList) {
            n = Math.min(n, nArray[0]);
            n2 = Math.min(n2, nArray[1]);
            n3 = Math.max(n3, nArray[2]);
            n4 = Math.max(n4, nArray[3]);
        }
        int n5 = 0;
        boolean bl2 = false;
        for (BuildPlan buildPlan : seq) {
            if (buildPlan.block == Blocks.unloader) {
                ++n5;
            }
            if (!(buildPlan.block instanceof StorageBlock)) continue;
            bl2 = true;
        }
        Iterator iterator = null;
        float f = -1.0E9f;
        Seq<BuildPlan> seq2 = AutoDock.copyAll(seq);
        for (int i = 0; i < 4; ++i) {
            int[] nArray = AutoDock.bounds(seq2);
            for (int j = n - nArray[2] - 1; j <= n3 - nArray[0] + 1; ++j) {
                for (int k = n2 - nArray[3] - 1; k <= n4 - nArray[1] + 1; ++k) {
                    int n6 = 0;
                    int n7 = 0;
                    boolean bl3 = false;
                    for (BuildPlan buildPlan : seq2) {
                        int[] nArray2 = AutoDock.rect(buildPlan.x + j, buildPlan.y + k, buildPlan.block.size);
                        if (AutoDock.overlapsAny(nArray2, arrayList)) {
                            bl3 = true;
                            break;
                        }
                        if (!AutoDock.touchesAny(buildPlan, j, k, arrayList)) continue;
                        if (buildPlan.block instanceof StorageBlock) {
                            ++n6;
                            continue;
                        }
                        if (buildPlan.block != Blocks.unloader) continue;
                        ++n7;
                    }
                    if (bl3 || (bl2 ? n6 == 0 : n7 == 0)) continue;
                    if (bl && !bl2 && n7 < n5) continue;
                    int n8 = AutoDock.ringCovered(seq2, j, k, arrayList);
                    float f2 = (float)(nArray[0] + nArray[2]) / 2.0f + (float)j;
                    float f3 = (float)(nArray[1] + nArray[3]) / 2.0f + (float)k;
                    float f4 = (float)n6 * 100.0f + (float)n7 * 10.0f - 3.0f * (float)Math.max(0, n8 - n6 - n7) - Mathf.dst((float)f2, (float)f3, (float)unit.tileX(), (float)unit.tileY()) * 0.01f;
                    if (f4 <= f) continue;
                    boolean bl4 = true;
                    for (BuildPlan buildPlan : seq2) {
                        if (Build.validPlace((Block)buildPlan.block, (Team)Vars.player.team(), (int)(buildPlan.x + j), (int)(buildPlan.y + k), (int)buildPlan.rotation)) continue;
                        bl4 = false;
                        break;
                    }
                    if (!bl4) continue;
                    f = f4;
                    iterator = new Seq();
                    for (BuildPlan buildPlan : seq2) {
                        BuildPlan buildPlan2 = buildPlan.copy();
                        buildPlan2.x += j;
                        buildPlan2.y += k;
                        iterator.add(buildPlan2);
                    }
                }
            }
            AutoDock.rotate(seq2);
        }
        return iterator;
    }

    private static boolean touches(BuildPlan buildPlan, int n, int n2, int[] nArray) {
        int[] nArray2 = AutoDock.rect(buildPlan.x + n, buildPlan.y + n2, buildPlan.block.size);
        boolean bl = nArray2[2] >= nArray[0] && nArray2[0] <= nArray[2];
        boolean bl2 = nArray2[3] >= nArray[1] && nArray2[1] <= nArray[3];
        return bl && (nArray2[3] + 1 == nArray[1] || nArray2[1] - 1 == nArray[3]) || bl2 && (nArray2[2] + 1 == nArray[0] || nArray2[0] - 1 == nArray[2]);
    }

    private static int[] rect(int n, int n2, int n3) {
        int n4 = -((n3 - 1) / 2);
        return new int[]{n + n4, n2 + n4, n + n4 + n3 - 1, n2 + n4 + n3 - 1};
    }

    private static int[] bounds(Seq<BuildPlan> seq) {
        int n = 0x100000;
        int n2 = 0x100000;
        int n3 = -1048576;
        int n4 = -1048576;
        for (BuildPlan buildPlan : seq) {
            int[] nArray = AutoDock.rect(buildPlan.x, buildPlan.y, buildPlan.block.size);
            n = Math.min(n, nArray[0]);
            n2 = Math.min(n2, nArray[1]);
            n3 = Math.max(n3, nArray[2]);
            n4 = Math.max(n4, nArray[3]);
        }
        return new int[]{n, n2, n3, n4};
    }

    private static Seq<BuildPlan> copyAll(Seq<BuildPlan> seq) {
        Seq seq2 = new Seq();
        for (BuildPlan buildPlan : seq) {
            seq2.add((Object)buildPlan.copy());
        }
        return seq2;
    }

    private static void rotate(Seq<BuildPlan> seq) {
        for (BuildPlan buildPlan : seq) {
            float f = buildPlan.block.size % 2 == 0 ? -0.5f : 0.0f;
            buildPlan.pointConfig(point2 -> {
                float f2 = (float)point2.x + f;
                float f3 = (float)point2.y + f;
                float f4 = f2;
                f2 = -f3;
                f3 = f4;
                point2.set(Mathf.floor((float)(f2 - f)), Mathf.floor((float)(f3 - f)));
            });
            float f2 = (float)buildPlan.x * 8.0f + buildPlan.block.offset;
            float f3 = (float)buildPlan.y * 8.0f + buildPlan.block.offset;
            float f4 = f2;
            f2 = -f3;
            f3 = f4;
            buildPlan.x = World.toTile((float)(f2 - buildPlan.block.offset));
            buildPlan.y = World.toTile((float)(f3 - buildPlan.block.offset));
            buildPlan.rotation = buildPlan.block.planRotation(Mathf.mod((int)(buildPlan.rotation + 1), (int)4));
        }
    }

    private static Schematic find(String string) {
        for (String string2 : string.split("\\|")) {
            for (Schematic schematic : Vars.schematics.all()) {
                if (!string2.equals(schematic.name())) continue;
                return schematic;
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
        int n = 0;
        for (Object object : seq) {
            if (((BuildPlan)object).block.consumers.length <= 0 || ((BuildPlan)object).block.size < 2 || ((BuildPlan)object).block instanceof StorageBlock) continue;
            int n2 = 0;
            for (BuildPlan buildPlan : seq) {
                if (buildPlan.block != ((BuildPlan)object).block) continue;
                ++n2;
            }
            if (n2 <= n) continue;
            n = n2;
            block = ((BuildPlan)object).block;
        }
        if (block == null) {
            return false;
        }
        int n3 = 0;
        for (Building building : Groups.build) {
            if (building.team != Vars.player.team() || building.block != block || !building.within((Position)coreBuild, 480.0f)) continue;
            ++n3;
        }
        return n3 >= n;
    }

    private static String missing(CoreBlock.CoreBuild coreBuild, Seq<BuildPlan> seq, String string) {
        Object object;
        Object object22;
        if (Vars.state.rules.infiniteResources) {
            return null;
        }
        HashMap<Item, Integer> hashMap = new HashMap<Item, Integer>();
        for (Object object22 : seq) {
            ItemStack[] itemStackArray = ((BuildPlan)object22).block.requirements;
            int n = itemStackArray.length;
            for (int i = 0; i < n; ++i) {
                object = itemStackArray[i];
                int n2 = Mathf.ceil((float)((float)((ItemStack)object).amount * Vars.state.rules.buildCostMultiplier));
                hashMap.merge(((ItemStack)object).item, n2, Integer::sum);
            }
        }
        for (Object object22 : hashMap.entrySet()) {
            if ((Integer)object22.getValue() <= coreBuild.storageCapacity) continue;
            return ((Item)object22.getKey()).localizedName + " (precisa " + object22.getValue() + " > capacidade " + coreBuild.storageCapacity + " do nucleo)";
        }
        HashMap hashMap2 = new HashMap();
        if (string != null && !string.isEmpty()) {
            for (String string2 : string.split(";")) {
                object = string2.split(":");
                Item item = Vars.content.item(object[0]);
                if (item == null) continue;
                hashMap2.put(item, Integer.parseInt(object[1]));
            }
        }
        object22 = new StringBuilder();
        for (Map.Entry entry : hashMap.entrySet()) {
            int n = coreBuild.items.get((Item)entry.getKey());
            object = (Integer)hashMap2.get(entry.getKey());
            int n3 = (Integer)entry.getValue() + (object == null ? 0 : (Integer)object);
            if (n >= n3) continue;
            ((StringBuilder)object22).append(((Item)entry.getKey()).localizedName).append(' ').append(n3 - n).append("  ");
        }
        for (Map.Entry entry : hashMap2.entrySet()) {
            int n;
            if (hashMap.containsKey(entry.getKey()) || (n = coreBuild.items.get((Item)entry.getKey())) >= (Integer)entry.getValue()) continue;
            ((StringBuilder)object22).append(((Item)entry.getKey()).localizedName).append(' ').append((Integer)entry.getValue() - n).append(" (alimentacao)  ");
        }
        return ((StringBuilder)object22).length() == 0 ? null : ((StringBuilder)object22).toString().trim();
    }

    private static void toast(String string) {
        if (string.equals(lastToast) && Time.time - lastToastAt < 1800.0f) {
            return;
        }
        if (Time.time - lastToastAt < 240.0f) {
            return;
        }
        lastToast = string;
        lastToastAt = Time.time;
        Log.info((Object)("[Lunarians] " + string));
        if (Vars.ui != null) {
            Vars.ui.showInfoToast(string, 4.0f);
        }
    }

    static {
        lastToast = "";
        lastToastAt = -999.0f;
        energyStep = false;
        stuckRetries = 0;
        camHeld = false;
        prevDetach = false;
    }

    private static enum Phase {
        NEXT,
        MOVE,
        BUILD,
        GRID,
        DONE;

    }
}

