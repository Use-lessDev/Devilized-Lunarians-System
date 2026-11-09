/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.graphics.Color
 *  arc.graphics.g2d.Draw
 *  arc.graphics.g2d.Fill
 *  arc.graphics.g2d.Lines
 *  arc.input.KeyCode
 *  arc.math.Mathf
 *  arc.math.geom.Position
 *  arc.math.geom.Vec2
 *  arc.struct.IntQueue
 *  arc.struct.Seq
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.content.Blocks
 *  mindustry.content.Items
 *  mindustry.core.World
 *  mindustry.entities.units.BuildPlan
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.Schematic
 *  mindustry.game.Schematics
 *  mindustry.game.Team
 *  mindustry.gen.Unit
 *  mindustry.type.Item
 *  mindustry.type.ItemStack
 *  mindustry.world.Block
 *  mindustry.world.Build
 *  mindustry.world.Tile
 *  mindustry.world.blocks.ConstructBlock
 *  mindustry.world.blocks.distribution.Conveyor
 *  mindustry.world.blocks.distribution.ItemBridge
 *  mindustry.world.blocks.distribution.Junction
 *  mindustry.world.blocks.distribution.OverflowGate
 *  mindustry.world.blocks.distribution.Router
 *  mindustry.world.blocks.distribution.Sorter
 *  mindustry.world.blocks.environment.Floor
 *  mindustry.world.blocks.liquid.Conduit
 *  mindustry.world.blocks.power.PowerNode
 *  mindustry.world.blocks.production.Drill
 *  mindustry.world.blocks.storage.CoreBlock
 *  mindustry.world.blocks.storage.CoreBlock$CoreBuild
 */
package lunarians;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.struct.IntQueue;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;
import lunarians.AutoConnectConveyor;
import lunarians.AutoDrill;
import lunarians.AutoEconomy;
import lunarians.AutoSetConfig;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.SteamDrill;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.core.World;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.game.Schematic;
import mindustry.game.Schematics;
import mindustry.game.Team;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.blocks.ConstructBlock;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.blocks.distribution.ItemBridge;
import mindustry.world.blocks.distribution.Junction;
import mindustry.world.blocks.distribution.OverflowGate;
import mindustry.world.blocks.distribution.Router;
import mindustry.world.blocks.distribution.Sorter;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.blocks.power.PowerNode;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.storage.CoreBlock;

public class AutoSet {
    private static final int[] DX = new int[]{1, 0, -1, 0, 1, 1, -1, -1};
    private static final int[] DY = new int[]{0, 1, 0, -1, 1, -1, 1, -1};
    private static String[] oreOrder = new String[]{"copper", "copper", "copper", "lead", "coal", "sand", "coal", "sand"};
    private static String[] factoryQueue = new String[0];
    private static int runMode;
    private static boolean registered;
    private static boolean errorLogged;
    private static int phase;
    private static float armedAt;
    private static boolean centerChosen;
    private static int cx;
    private static int cy;
    private static float radius;
    private static boolean hasArea;
    private static int stepIndex;
    private static float nextStepAt;
    private static float nextCheck;
    private static float lastProgress;
    private static float lastWarn;
    private static int lastPending;
    private static boolean[] used;
    private static final Seq<BuildPlan> pending;
    private static final HashMap<String, Schematic> schemCache;
    private static final ArrayList<int[]> feedTargets;
    private static final ArrayList<String> feedItemNames;
    private static final ArrayList<Integer> feedReserved;
    private static int feedIndex;
    private static final HashMap<String, ArrayList<int[]>> feedLines;
    private static int steamDone;
    private static boolean miningActive;
    private static float nextMineCheck;
    private static float lastMineHint;
    private static final HashMap<Integer, Integer> ours;
    private static final Set<BuildPlan> seenPlans;
    private static final HashSet<Integer> deleted;
    private static int deleteCount;

    public static void init() {
        if (registered) {
            return;
        }
        registered = true;
        Events.run((Object)EventType.Trigger.update, AutoSet::update);
        Events.run((Object)EventType.Trigger.draw, AutoSet::draw);
    }

    private static void stopMining(Unit unit) {
        if (!miningActive) {
            return;
        }
        miningActive = false;
        try {
            if (unit != null) {
                unit.mineTile = null;
                unit.updateBuilding = true;
            }
            if (Vars.control != null && Vars.control.input != null) {
                Vars.control.input.isBuilding = true;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void autoMine(Unit unit) {
        int n;
        Object object;
        if (Vars.player == null || Vars.player.closestCore() == null) {
            return;
        }
        if (miningActive && unit.mineTile != null) {
            unit.updateBuilding = false;
            Vars.control.input.isBuilding = false;
        }
        if (Time.time < nextMineCheck) {
            return;
        }
        nextMineCheck = Time.time + 20.0f;
        CoreBlock.CoreBuild coreBuild = Vars.player.closestCore();
        int[] nArray = new int[Vars.content.items().size];
        boolean bl = false;
        for (int i = 0; i < AutoSet.pending.size; ++i) {
            object = pending.get(i);
            Iterator iterator = Vars.world.tile(((BuildPlan)object).x, ((BuildPlan)object).y);
            if (((BuildPlan)object).breaking || ((BuildPlan)object).block == null || iterator != null && iterator.block() == ((BuildPlan)object).block) continue;
            for (ItemStack itemStack : ((BuildPlan)object).block.requirements) {
                n = itemStack.item.id;
                nArray[n] = nArray[n] + (int)Math.ceil((float)itemStack.amount * Vars.state.rules.buildCostMultiplier);
                bl = true;
            }
        }
        ItemStack[] itemStackArray = null;
        if (bl && !Vars.state.rules.infiniteResources) {
            for (ItemStack[] itemStackArray2 : Vars.content.items()) {
                if (nArray[itemStackArray2.id] <= coreBuild.items.get((Item)itemStackArray2) || unit.type.mineTier < itemStackArray2.hardness || unit.type.mineItems == null || !unit.type.mineItems.contains((Object)itemStackArray2)) continue;
                itemStackArray = itemStackArray2;
                break;
            }
        }
        if (itemStackArray == null) {
            AutoSet.stopMining(unit);
            return;
        }
        object = null;
        float f = Float.MAX_VALUE;
        int n2 = (int)(unit.type.mineRange / 8.0f) + 2;
        int n3 = unit.tileX();
        int n4 = unit.tileY();
        for (int i = n4 - n2; i <= n4 + n2; ++i) {
            for (n = n3 - n2; n <= n3 + n2; ++n) {
                float f2;
                Tile tile = Vars.world.tile(n, i);
                if (tile == null || tile.build != null || tile.drop() != itemStackArray || !unit.validMine(tile) || !((f2 = unit.dst(tile.worldx(), tile.worldy())) < f)) continue;
                f = f2;
                object = tile;
            }
        }
        if (object != null) {
            if (!miningActive) {
                AutoSet.toast(AutoSet.tr("Faltando " + itemStackArray.localizedName + ": minerando automaticamente", "Missing " + itemStackArray.localizedName + ": mining automatically"));
            }
            miningActive = true;
            unit.mineTile = (Tile)object;
            unit.updateBuilding = false;
            Vars.control.input.isBuilding = false;
            return;
        }
        AutoSet.stopMining(unit);
        if (Time.time - lastMineHint > 600.0f) {
            lastMineHint = Time.time;
            AutoSet.toast(AutoSet.tr("Faltando " + itemStackArray.localizedName + ": aproxime sua unidade do min\u00e9rio para minerar automaticamente", "Missing " + itemStackArray.localizedName + ": move your unit near its ore to auto-mine"));
        }
    }

    public static void reset() {
        try {
            AutoSet.stopMining(Vars.player == null ? null : Vars.player.unit());
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        phase = 0;
        deleteCount = 0;
        ours.clear();
        seenPlans.clear();
        deleted.clear();
        steamDone = 0;
        hasArea = false;
        stepIndex = 0;
        centerChosen = false;
        pending.clear();
        AutoDrill.limitOn = false;
        AutoDrill.quiet = false;
    }

    public static boolean isActive() {
        return phase != 0;
    }

    public static boolean isRunning() {
        return phase == 3;
    }

    public static void request() {
        if (Vars.headless || Vars.state == null || !Vars.state.isGame()) {
            AutoSet.toast(LunariansI18n.get("as_enter_map"));
            return;
        }
        if (phase == 3) {
            AutoSet.toast(LunariansI18n.get("as_running"));
            return;
        }
        if (phase == 2) {
            AutoSet.toast(LunariansI18n.get("as_pick_hint"));
            return;
        }
        if (phase == 1 && Time.time - armedAt < 180.0f) {
            phase = 2;
            centerChosen = false;
            hasArea = false;
            AutoSet.toast(LunariansI18n.get("as_tap_center"));
            return;
        }
        phase = 1;
        armedAt = Time.time;
        AutoSet.toast(LunariansI18n.get("as_confirm"));
    }

    public static void cancel() {
        try {
            Unit unit;
            Unit unit2 = unit = Vars.player == null ? null : Vars.player.unit();
            if (unit != null && phase == 3) {
                for (int i = 0; i < AutoSet.pending.size; ++i) {
                    unit.plans().remove((Object)((BuildPlan)pending.get(i)));
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        AutoSet.reset();
        AutoSet.toast(LunariansI18n.get("as_cancelled"));
    }

    private static void update() {
        block7: {
            if (Vars.headless || Vars.state == null || !Vars.state.isGame()) {
                if (phase != 0) {
                    AutoSet.reset();
                }
                return;
            }
            try {
                if (phase == 1 && Time.time - armedAt > 200.0f) {
                    phase = 0;
                    AutoSet.toast(LunariansI18n.get("as_timeout"));
                    return;
                }
                if (phase == 2) {
                    AutoSet.handleAreaPick();
                    return;
                }
                if (phase == 3) {
                    AutoSet.runSteps();
                }
            }
            catch (Throwable throwable) {
                if (errorLogged) break block7;
                errorLogged = true;
                Log.err((String)"[Lunarians] AutoSet error", (Throwable)throwable);
            }
        }
    }

    private static void handleAreaPick() {
        int n;
        if (Core.input.keyTap(KeyCode.escape) || Core.input.keyTap(KeyCode.back)) {
            AutoSet.cancel();
            return;
        }
        if (!Core.input.justTouched() || Core.scene.hasMouse() || Core.scene.hasDialog()) {
            return;
        }
        Vec2 vec2 = Core.input.mouseWorld();
        int n2 = World.toTile((float)vec2.x);
        if (!Vars.world.tiles.in(n2, n = World.toTile((float)vec2.y))) {
            return;
        }
        if (!centerChosen) {
            cx = n2;
            cy = n;
            centerChosen = true;
            AutoSet.toast(LunariansI18n.get("as_tap_radius"));
            return;
        }
        float f = Mathf.dst((float)cx, (float)cy, (float)n2, (float)n);
        if (f < 3.0f) {
            AutoSet.toast(LunariansI18n.get("as_radius_small"));
            return;
        }
        radius = Math.min(f, 80.0f);
        hasArea = true;
        phase = 3;
        stepIndex = 0;
        runMode = AutoSetConfig.mode();
        oreOrder = AutoSetConfig.oreOrder();
        factoryQueue = AutoSetConfig.factoryQueue();
        feedTargets.clear();
        feedItemNames.clear();
        feedReserved.clear();
        feedLines.clear();
        feedIndex = 0;
        pending.clear();
        used = new boolean[Vars.world.width() * Vars.world.height()];
        nextStepAt = Time.time + 10.0f;
        lastProgress = Time.time;
        lastPending = 0;
        AutoSet.toast(LunariansI18n.get("as_started", String.valueOf((int)radius)));
    }

    private static void runSteps() {
        Unit unit;
        Unit unit2 = unit = Vars.player == null ? null : Vars.player.unit();
        if (unit == null || !unit.canBuild()) {
            return;
        }
        if (Vars.player.closestCore() == null) {
            AutoSet.toast(LunariansI18n.get("as_no_core"));
            AutoSet.reset();
            return;
        }
        AutoSet.autoMine(unit);
        if (AutoSet.pending.size > 0) {
            AutoSet.checkPending(unit);
            return;
        }
        if (Time.time < nextStepAt) {
            return;
        }
        if (runMode == 0 && AutoSetConfig.steamOn() && steamDone < AutoSetConfig.steamCount()) {
            ++steamDone;
            nextStepAt = Time.time + 30.0f;
            AutoSet.startSteamStep(unit);
            return;
        }
        if (runMode == 1 ? stepIndex >= factoryQueue.length && feedIndex >= feedTargets.size() : stepIndex >= oreOrder.length) {
            AutoSet.reset();
            AutoSet.toast(LunariansI18n.get("as_done"));
            return;
        }
        if (runMode == 1) {
            if (stepIndex < factoryQueue.length) {
                AutoSet.startFactoryStep(unit);
            } else {
                AutoSet.startFeedStep(unit);
            }
        } else {
            AutoSet.startStep(unit);
        }
    }

    private static Item itemFor(String string) {
        Item item = Vars.content.item(string);
        if (item != null) {
            return item;
        }
        if ("copper".equals(string)) {
            return Items.copper;
        }
        if ("lead".equals(string)) {
            return Items.lead;
        }
        if ("coal".equals(string)) {
            return Items.coal;
        }
        if ("sand".equals(string)) {
            return Items.sand;
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void startStep(Unit unit) {
        Item item = AutoSet.itemFor(oreOrder[stepIndex]);
        ++stepIndex;
        nextStepAt = Time.time + 30.0f;
        if (item == null) {
            return;
        }
        boolean bl = false;
        boolean bl2 = false;
        AutoDrill.limitOn = true;
        AutoDrill.limitX = cx;
        AutoDrill.limitY = cy;
        AutoDrill.limitR = radius;
        AutoDrill.quiet = true;
        try {
            for (int i = 0; i < 4 && !bl; ++i) {
                Object object2;
                int[] nArray = AutoSet.findOre(item);
                if (nArray == null) {
                    break;
                }
                bl2 = true;
                AutoSet.markUsed(nArray[0], nArray[1], item);
                LunariansSettings.drillStartX = nArray[0];
                LunariansSettings.drillStartY = nArray[1];
                AutoSet.setExitTowardCore(nArray[0], nArray[1]);
                Seq seq = new Seq();
                for (Object object2 : unit.plans()) {
                    seq.add(object2);
                }
                int n = AutoDrill.generateWithConveyor();
                if (n <= 0) continue;
                pending.clear();
                object2 = unit.plans().iterator();
                while (object2.hasNext()) {
                    BuildPlan buildPlan = (BuildPlan)object2.next();
                    if (seq.contains((Object)buildPlan, true)) continue;
                    pending.add((Object)buildPlan);
                }
                boolean bl3 = bl = AutoSet.pending.size > 0;
                if (!bl) continue;
                lastPending = AutoSet.pending.size;
                lastProgress = Time.time;
                AutoSet.toast(LunariansI18n.get("as_step", item.localizedName, String.valueOf(n)));
            }
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] AutoSet step", (Throwable)throwable);
        }
        finally {
            AutoDrill.isoOverride = -1;
            AutoDrill.quiet = false;
            AutoDrill.limitOn = false;
            LunariansSettings.save();
        }
        if (!bl) {
            AutoSet.toast(LunariansI18n.get(bl2 ? "as_no_route" : "as_no_ore", item.localizedName));
        }
    }

    private static void startSteamStep(Unit unit) {
        try {
            Seq<BuildPlan> seq = SteamDrill.plan(unit, cx, cy, radius, used);
            if (seq == null || seq.isEmpty()) {
                steamDone = AutoSetConfig.steamCount();
                AutoSet.toast("Steam Drill: " + SteamDrill.lastError);
                return;
            }
            Seq seq2 = new Seq();
            for (BuildPlan object : unit.plans()) {
                seq2.add((Object)object);
            }
            if (used != null) {
                for (Integer n : SteamDrill.lastTiles) {
                    if (n < 0 || n >= used.length) continue;
                    AutoSet.used[n.intValue()] = true;
                }
            }
            for (int i = 0; i < seq.size; ++i) {
                unit.addBuild((BuildPlan)seq.get(i), true);
            }
            pending.clear();
            for (BuildPlan buildPlan : unit.plans()) {
                if (seq2.contains((Object)buildPlan, true)) continue;
                pending.add((Object)buildPlan);
            }
            lastPending = AutoSet.pending.size;
            lastProgress = Time.time;
            AutoSet.toast("Steam Drill: " + seq.size + AutoSet.tr(" blocos planejados", " blocks planned"));
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] steam step", (Throwable)throwable);
        }
    }

    private static String tr(String string, String string2) {
        return "pt".equals(LunariansI18n.getLanguage()) ? string : string2;
    }

    private static Schematic schemFor(String string) {
        Schematic schematic = schemCache.get(string);
        if (schematic == null) {
            try {
                schematic = Schematics.readBase64((String)AutoSetConfig.schematicFor(string));
            }
            catch (Throwable throwable) {
                Log.err((String)("[Lunarians] schematic " + string), (Throwable)throwable);
            }
            if (schematic != null) {
                schemCache.put(string, schematic);
            }
        }
        return schematic;
    }

    private static void rotatePlansAround(Seq<BuildPlan> seq, int n, int n2) {
        for (int i = 0; i < seq.size; ++i) {
            BuildPlan buildPlan = (BuildPlan)seq.get(i);
            if (buildPlan.breaking || buildPlan.block == null) continue;
            float f = buildPlan.block.offset;
            float f2 = (float)((buildPlan.x - n) * 8) + f;
            float f3 = (float)((buildPlan.y - n2) * 8) + f;
            float f4 = -f3;
            float f5 = f2;
            buildPlan.x = Math.round((f4 - f) / 8.0f) + n;
            buildPlan.y = Math.round((f5 - f) / 8.0f) + n2;
            buildPlan.rotation = buildPlan.block.planRotation(Mathf.mod((int)(buildPlan.rotation + 1), (int)4));
        }
    }

    private static boolean planFits(Seq<BuildPlan> seq, boolean[] blArray, HashSet<Integer> hashSet) {
        int n = Vars.world.width();
        for (int i = 0; i < seq.size; ++i) {
            BuildPlan buildPlan = (BuildPlan)seq.get(i);
            if (buildPlan.block == null) {
                return false;
            }
            int n2 = buildPlan.block.size;
            int n3 = (n2 - 1) / 2;
            for (int j = 0; j < n2; ++j) {
                for (int k = 0; k < n2; ++k) {
                    int n4 = buildPlan.x - n3 + k;
                    int n5 = buildPlan.y - n3 + j;
                    if (!Vars.world.tiles.in(n4, n5) || blArray[n5 * n + n4]) {
                        return false;
                    }
                    hashSet.add(n5 * n + n4);
                }
            }
            if (Build.validPlace((Block)buildPlan.block, (Team)Vars.player.team(), (int)buildPlan.x, (int)buildPlan.y, (int)buildPlan.rotation)) continue;
            return false;
        }
        return true;
    }

    private static int[] findOutput(Seq<BuildPlan> seq, HashSet<Integer> hashSet) {
        int n = Vars.world.width();
        for (int i = 0; i < seq.size; ++i) {
            int n2;
            int n3;
            int n4;
            BuildPlan buildPlan = (BuildPlan)seq.get(i);
            if (buildPlan.block == null || buildPlan.block.name == null || !buildPlan.block.name.contains("conveyor") || !Vars.world.tiles.in(n4 = buildPlan.x + DX[n3 = buildPlan.rotation & 3], n2 = buildPlan.y + DY[n3]) || hashSet.contains(n2 * n + n4)) continue;
            return new int[]{n4, n2, n3};
        }
        return null;
    }

    private static String[] feedItems(String string) {
        if ("silicon".equals(string)) {
            return new String[]{"coal", "sand"};
        }
        if ("metaglass".equals(string)) {
            return new String[]{"lead", "sand"};
        }
        return new String[]{"coal", null};
    }

    private static Block blockByName(String string, String string2) {
        Block block = Vars.content.block(string);
        if (block == null && string2 != null) {
            block = Vars.content.block(string2);
        }
        return block;
    }

    private static int genInner() {
        try {
            Method method = AutoDrill.class.getDeclaredMethod("generateInner", new Class[0]);
            method.setAccessible(true);
            return (Integer)method.invoke(null, new Object[0]);
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] AutoSet genInner", (Throwable)throwable);
            return 0;
        }
    }

    private static Seq<BuildPlan> routeTo(int n, int n2, int n3, int n4, int n5, boolean[] blArray, Block block) {
        int n6;
        int n7;
        int n8;
        int n9;
        int n10;
        int n11;
        int n12 = Vars.world.width();
        int n13 = Vars.world.height();
        Team team = Vars.player.team();
        if (block == null || !AutoSet.freeFor(block, team, n, n2, blArray) || !AutoSet.freeFor(block, team, n3, n4, blArray)) {
            return null;
        }
        int[] nArray = new int[n12 * n13];
        Arrays.fill(nArray, -2);
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<Integer>();
        int n14 = n2 * n12 + n;
        int n15 = n4 * n12 + n3;
        nArray[n14] = -1;
        arrayDeque.add(n14);
        int n16 = 0;
        while (!arrayDeque.isEmpty() && nArray[n15] == -2 && n16++ < 60000) {
            int n17 = (Integer)arrayDeque.poll();
            n11 = n17 % n12;
            n10 = n17 / n12;
            for (n9 = 0; n9 < 4; ++n9) {
                n8 = n11 + DX[n9];
                n7 = n10 + DY[n9];
                if (!Vars.world.tiles.in(n8, n7) || nArray[n6 = n7 * n12 + n8] != -2 || !AutoSet.freeFor(block, team, n8, n7, blArray)) continue;
                nArray[n6] = n17;
                arrayDeque.add(n6);
            }
        }
        if (nArray[n15] == -2) {
            return null;
        }
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        n11 = n15;
        while (n11 != -1) {
            arrayList.add(n11);
            n11 = nArray[n11];
        }
        Collections.reverse(arrayList);
        Seq seq = new Seq();
        for (n10 = 0; n10 < arrayList.size(); ++n10) {
            n9 = (Integer)arrayList.get(n10);
            n8 = n9 % n12;
            n7 = n9 / n12;
            n6 = n5;
            if (n10 + 1 < arrayList.size()) {
                int n18 = (Integer)arrayList.get(n10 + 1);
                int n19 = n18 % n12;
                int n20 = n18 / n12;
                n6 = n19 > n8 ? 0 : (n19 < n8 ? 2 : (n20 > n7 ? 1 : 3));
            }
            seq.add((Object)new BuildPlan(n8, n7, n6, block));
        }
        return seq;
    }

    private static boolean freeFor(Block block, Team team, int n, int n2, boolean[] blArray) {
        if (!Vars.world.tiles.in(n, n2) || blArray[n2 * Vars.world.width() + n]) {
            return false;
        }
        Tile tile = Vars.world.tile(n, n2);
        if (tile == null || tile.build != null) {
            return false;
        }
        return Build.validPlace((Block)block, (Team)team, (int)n, (int)n2, (int)0);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void startFeedStep(Unit unit) {
        int[] nArray = feedTargets.get(feedIndex);
        String string = feedItemNames.get(feedIndex);
        ++feedIndex;
        nextStepAt = Time.time + 30.0f;
        Item item = AutoSet.itemFor(string);
        Block block = AutoSet.blockByName("conveyor", "titanium-conveyor");
        if (item == null || block == null) {
            return;
        }
        boolean bl = false;
        AutoDrill.limitOn = true;
        AutoDrill.limitX = cx;
        AutoDrill.limitY = cy;
        AutoDrill.limitR = radius;
        AutoDrill.quiet = true;
        int n = LunariansSettings.drillExitDir;
        try {
            int[] nArray2;
            block8: for (int i = 0; i < 6 && !bl && (nArray2 = AutoSet.findOre(item, nArray[0], nArray[1])) != null; ++i) {
                int n2;
                AutoSet.markUsed(nArray2[0], nArray2[1], item);
                LunariansSettings.drillStartX = nArray2[0];
                LunariansSettings.drillStartY = nArray2[1];
                float f = nArray[0] - nArray2[0];
                float f2 = nArray[1] - nArray2[1];
                int n3 = Math.abs(f) >= Math.abs(f2) ? (f >= 0.0f ? 0 : 2) : (n2 = f2 >= 0.0f ? 1 : 3);
                int n4 = Math.abs(f) >= Math.abs(f2) ? (f2 >= 0.0f ? 1 : 3) : (f >= 0.0f ? 0 : 2);
                int[] nArray3 = new int[]{n2, n4, n4 ^ 2, n2 ^ 2};
                boolean[] blArray = AutoDrill.reservedForFeed(unit);
                for (int j = 0; j < 4 && !bl; ++j) {
                    int n5;
                    int n6;
                    int n7;
                    int n8;
                    int n9;
                    LunariansSettings.drillExitDir = n9 = nArray3[j];
                    AutoDrill.dryRun = true;
                    try {
                        n8 = AutoSet.genInner();
                    }
                    finally {
                        AutoDrill.dryRun = false;
                    }
                    if (n8 <= 0) continue block8;
                    if (AutoDrill.lastHub < 0) continue;
                    Seq seq = new Seq();
                    for (int k = 0; k < AutoDrill.lastPlans.size; ++k) {
                        seq.add((Object)((BuildPlan)AutoDrill.lastPlans.get(k)));
                    }
                    boolean[] blArray2 = (boolean[])blArray.clone();
                    for (n7 = 0; n7 < seq.size; ++n7) {
                        AutoConnectConveyor.markPlan((BuildPlan)seq.get(n7), blArray2);
                    }
                    n7 = AutoDrill.lastHub % Vars.world.width() + DX[n9];
                    Seq<BuildPlan> seq2 = AutoSet.routeTo(n7, n6 = AutoDrill.lastHub / Vars.world.width() + DY[n9], nArray[0], nArray[1], nArray[2], blArray2, block);
                    if (seq2 == null) continue;
                    Seq seq3 = new Seq();
                    for (BuildPlan buildPlan : unit.plans()) {
                        seq3.add((Object)buildPlan);
                    }
                    for (n5 = 0; n5 < seq.size; ++n5) {
                        unit.addBuild((BuildPlan)seq.get(n5), true);
                    }
                    for (n5 = 0; n5 < seq2.size; ++n5) {
                        unit.addBuild((BuildPlan)seq2.get(n5), true);
                    }
                    pending.clear();
                    for (BuildPlan buildPlan : unit.plans()) {
                        if (seq3.contains((Object)buildPlan, true)) continue;
                        pending.add((Object)buildPlan);
                    }
                    lastPending = AutoSet.pending.size;
                    lastProgress = Time.time;
                    bl = true;
                    ArrayList<int[]> arrayList = feedLines.get(string);
                    if (arrayList == null) {
                        arrayList = new ArrayList<int[]>();
                        feedLines.put(string, arrayList);
                    }
                    for (int k = 0; k < seq2.size; ++k) {
                        BuildPlan buildPlan = (BuildPlan)seq2.get(k);
                        if (!(buildPlan.block instanceof Conveyor)) continue;
                        arrayList.add(new int[]{buildPlan.x, buildPlan.y});
                    }
                    AutoSet.toast(item.localizedName + AutoSet.tr(" \u2192 entrada da f\u00e1brica: ", " \u2192 factory input: ") + n8 + " drills + " + seq2.size + AutoSet.tr(" esteiras", " conveyors"));
                }
            }
            if (!bl) {
                bl = AutoSet.tapLine(unit, string, nArray);
            }
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] AutoSet feed step", (Throwable)throwable);
        }
        finally {
            AutoDrill.isoOverride = -1;
            AutoDrill.quiet = false;
            AutoDrill.limitOn = false;
            AutoDrill.dryRun = false;
            LunariansSettings.drillExitDir = n;
            LunariansSettings.save();
        }
        if (!bl) {
            AutoSet.toast(AutoSet.tr("Sem min\u00e9rio/rota para ", "No ore/route for ") + item.localizedName);
        }
    }

    private static boolean tapLine(Unit unit, String string, int[] nArray) {
        ArrayList<int[]> arrayList = feedLines.get(string);
        Block block = AutoSet.blockByName("conveyor", "titanium-conveyor");
        if (arrayList == null || arrayList.isEmpty() || block == null) {
            return false;
        }
        HashSet<Integer> hashSet = new HashSet<Integer>();
        int n = Vars.world.width();
        for (int[] nArray2 : arrayList) {
            hashSet.add(nArray2[1] * n + nArray2[0]);
        }
        Object object = AutoDrill.reservedForFeed(unit);
        for (int i = 0; i < feedReserved.size(); ++i) {
            object[AutoSet.feedReserved.get((int)i).intValue()] = true;
        }
        Seq seq = new Seq();
        for (int[] nArray3 : arrayList) {
            Tile tile = Vars.world.tile(nArray3[0], nArray3[1]);
            if (tile == null || tile.build == null || !(tile.build.block instanceof Conveyor)) continue;
            boolean bl = true;
            for (int i = 0; i < 4 && bl; ++i) {
                Tile tile2 = Vars.world.tile(nArray3[0] + DX[i], nArray3[1] + DY[i]);
                if (tile2 == null || tile2.build == null || hashSet.contains(tile2.y * n + tile2.x)) continue;
                bl = false;
            }
            if (!bl) continue;
            seq.add((Object)new int[]{nArray3[0], nArray3[1], (int)(Mathf.dst((float)nArray3[0], (float)nArray3[1], (float)nArray[0], (float)nArray[1]) * 10.0f)});
        }
        seq.sort((Comparator)new Comparator<int[]>(){

            @Override
            public int compare(int[] nArray, int[] nArray2) {
                return nArray[2] - nArray2[2];
            }
        });
        for (int i = 0; i < seq.size && i < 14; ++i) {
            int[] nArray3;
            nArray3 = (int[])seq.get(i);
            for (int j = 0; j < 4; ++j) {
                Seq<BuildPlan> seq2 = AutoSet.routeTo(nArray3[0] + DX[j], nArray3[1] + DY[j], nArray[0], nArray[1], nArray[2], (boolean[])object, block);
                if (seq2 == null) continue;
                Seq seq3 = new Seq();
                for (BuildPlan buildPlan : unit.plans()) {
                    seq3.add((Object)buildPlan);
                }
                unit.addBuild(new BuildPlan(nArray3[0], nArray3[1], 0, Blocks.router), true);
                for (int k = 0; k < seq2.size; ++k) {
                    unit.addBuild((BuildPlan)seq2.get(k), true);
                }
                pending.clear();
                for (BuildPlan buildPlan : unit.plans()) {
                    if (seq3.contains((Object)buildPlan, true)) continue;
                    pending.add((Object)buildPlan);
                }
                lastPending = AutoSet.pending.size;
                lastProgress = Time.time;
                AutoSet.toast(AutoSet.tr("Sem outra fonte livre: router em " + nArray3[0] + "," + nArray3[1], "No other free source: router at " + nArray3[0] + "," + nArray3[1]));
                return true;
            }
        }
        return false;
    }

    private static int[] oreDistGrid(Item item, int n) {
        int n2;
        int n3;
        int n4;
        int n5;
        int n6 = n * 2 + 1;
        int[] nArray = new int[n6 * n6];
        Arrays.fill(nArray, -1);
        if (item == null) {
            return nArray;
        }
        IntQueue intQueue = new IntQueue();
        int n7 = cx - n;
        int n8 = cy - n;
        int n9 = Vars.world.width();
        for (n5 = 0; n5 < n6; ++n5) {
            for (n4 = 0; n4 < n6; ++n4) {
                Tile tile;
                n3 = n7 + n4;
                n2 = n8 + n5;
                if (!Vars.world.tiles.in(n3, n2) || !AutoSet.inCircle(n3, n2) || used[n2 * n9 + n3] || (tile = Vars.world.tile(n3, n2)) == null || tile.build != null || tile.solid() || !AutoSet.sameOre(tile, item)) continue;
                nArray[n5 * n6 + n4] = 0;
                intQueue.addLast(n5 * n6 + n4);
            }
        }
        while (!intQueue.isEmpty()) {
            n5 = intQueue.removeFirst();
            n4 = n5 % n6;
            n3 = n5 / n6;
            n2 = nArray[n5];
            if (n2 >= 60) continue;
            for (int i = 0; i < 4; ++i) {
                int n10 = n4 + DX[i];
                int n11 = n3 + DY[i];
                if (n10 < 0 || n11 < 0 || n10 >= n6 || n11 >= n6 || nArray[n11 * n6 + n10] >= 0) continue;
                nArray[n11 * n6 + n10] = n2 + 1;
                intQueue.addLast(n11 * n6 + n10);
            }
        }
        return nArray;
    }

    private static int oreScore(int[][] nArray, int n, int n2, int n3) {
        int n4 = n3 * 2 + 1;
        int n5 = n - (cx - n3);
        int n6 = n2 - (cy - n3);
        int n7 = 0;
        for (int i = 0; i < nArray.length; ++i) {
            if (nArray[i] == null) continue;
            int n8 = n5 >= 0 && n6 >= 0 && n5 < n4 && n6 < n4 ? nArray[i][n6 * n4 + n5] : -1;
            n7 += n8 < 0 ? 60 : n8;
        }
        return n7;
    }

    private static boolean overlayOre(Tile tile, Item item) {
        return tile != null && item != null && tile.overlay() != null && tile.overlay().itemDrop == item;
    }

    private static void startFactoryStep(Unit unit) {
        int n;
        String string = factoryQueue[stepIndex];
        ++stepIndex;
        nextStepAt = Time.time + 30.0f;
        Schematic schematic = AutoSet.schemFor(string);
        if (schematic == null || Vars.player.closestCore() == null) {
            AutoSet.toast(AutoSet.tr("Schematic inv\u00e1lido: ", "Invalid schematic: ") + string);
            return;
        }
        int n2 = Vars.player.closestCore().tileX();
        int n3 = Vars.player.closestCore().tileY();
        int n4 = Vars.world.width();
        int n5 = (int)Math.ceil(radius);
        String[] stringArray = AutoSet.feedItems(string);
        Item item = stringArray[0] == null ? null : AutoSet.itemFor(stringArray[0]);
        Item item2 = stringArray[1] == null ? null : AutoSet.itemFor(stringArray[1]);
        int[][] nArrayArray = new int[][]{item == null ? null : AutoSet.oreDistGrid(item, n5), item2 == null ? null : AutoSet.oreDistGrid(item2, n5)};
        ArrayList<int[]> arrayList = new ArrayList<int[]>();
        for (int i = cy - n5; i <= cy + n5; ++i) {
            for (n = cx - n5; n <= cx + n5; ++n) {
                if (!Vars.world.tiles.in(n, i) || !AutoSet.inCircle(n, i)) continue;
                arrayList.add(new int[]{n, i, (int)(Mathf.dst((float)n, (float)i, (float)cx, (float)cy) * 20.0f) + AutoSet.oreScore(nArrayArray, n, i, n5) * 250});
            }
        }
        Collections.sort(arrayList, new Comparator<int[]>(){

            @Override
            public int compare(int[] nArray, int[] nArray2) {
                return nArray[2] - nArray2[2];
            }
        });
        boolean[] blArray = AutoConnectConveyor.reservedFromPlans(unit);
        for (n = 0; n < feedReserved.size(); ++n) {
            blArray[AutoSet.feedReserved.get((int)n).intValue()] = true;
        }
        for (int i = 0; i < 3; ++i) {
            n = 0;
            for (int j = 0; j < arrayList.size() && n < 12; ++j) {
                int n6;
                int buildPlan;
                int n8;
                int n7;
                Tile tile;
                int n10;
                int n11;
                int n12;
                int n13;
                int n14;
                int n9;
                String[] stringArray2;
                int n16;
                int n17;
                Object object2;
                int n18;
                int n19;
                int[] nArray;
                int[] nArray2 = (int[])arrayList.get(j);
                float f = n2 - nArray2[0];
                float f2 = n3 - nArray2[1];
                int n20 = Math.abs(f) >= Math.abs(f2) ? (f >= 0.0f ? 0 : 2) : (f2 >= 0.0f ? 1 : 3);
                int n21 = n20 - 1 & 3;
                Seq seq = Vars.schematics.toPlans(schematic, nArray2[0], nArray2[1]);
                for (int k = 0; k < n21; ++k) {
                    AutoSet.rotatePlansAround((Seq<BuildPlan>)seq, nArray2[0], nArray2[1]);
                }
                HashSet<Integer> hashSet = new HashSet<Integer>();
                if (!AutoSet.planFits((Seq<BuildPlan>)seq, blArray, hashSet) || (nArray = AutoSet.findOutput((Seq<BuildPlan>)seq, hashSet)) == null) continue;
                int n22 = Vars.player.closestCore().block.size / 2 + (i >= 2 ? Math.min(AutoSetConfig.coreGap(), 2) : AutoSetConfig.coreGap());
                boolean bl = false;
                for (Integer object22 : hashSet) {
                    n19 = object22 % n4;
                    n18 = object22 / n4;
                    if (Math.max(Math.abs(n19 - n2), Math.abs(n18 - n3)) > n22) continue;
                    bl = true;
                    break;
                }
                if (bl) continue;
                boolean bl2 = false;
                block7: for (Object object2 : hashSet) {
                    n18 = (Integer)object2 % n4;
                    n17 = (Integer)object2 / n4;
                    for (int n24 = -1; n24 <= 1; ++n24) {
                        for (n16 = -1; n16 <= 1; ++n16) {
                            stringArray2 = Vars.world.tile(n18 + n16, n17 + n24);
                            if (!AutoSet.overlayOre((Tile)stringArray2, item) && !AutoSet.overlayOre((Tile)stringArray2, item2)) continue;
                            bl2 = true;
                            break block7;
                        }
                    }
                }
                if (bl2 && i < 2) continue;
                int n15 = i == 0 ? AutoSetConfig.facGap() : (n9 = i == 1 ? Math.min(AutoSetConfig.facGap(), 1) : 0);
                n19 = i == 0 ? AutoSetConfig.wallGap() : (i == 1 ? Math.min(AutoSetConfig.wallGap(), 1) : 0);
                n18 = Math.max(n9, n19);
                n17 = 0;
                object2 = hashSet.iterator();
                block10: while (object2.hasNext()) {
                    Integer seq2 = (Integer)object2.next();
                    int string2 = seq2 % n4;
                    n14 = seq2 / n4;
                    for (n13 = -n18; n13 <= n18; ++n13) {
                        for (n12 = -n18; n12 <= n18; ++n12) {
                            n11 = string2 + n12;
                            n10 = n14 + n13;
                            if (!Vars.world.tiles.in(n11, n10) || hashSet.contains(n10 * n4 + n11)) continue;
                            tile = Vars.world.tile(n11, n10);
                            n7 = Math.abs(n12) <= n19 && Math.abs(n13) <= n19 ? 1 : 0;
                            n8 = Math.abs(n12) <= n9 && Math.abs(n13) <= n9 ? 1 : 0;
                            buildPlan = n8;
                            if (!(n7 != 0 && tile != null && tile.build == null && tile.solid() || n8 != 0 && (blArray[n10 * n4 + n11] || tile != null && tile.build != null && !(tile.build.block instanceof CoreBlock)))) continue;
                            n17 = 1;
                            break block10;
                        }
                    }
                }
                if (n17 != 0) continue;
                ++n;
                object2 = (boolean[])blArray.clone();
                for (n16 = 0; n16 < seq.size; ++n16) {
                    AutoConnectConveyor.markPlan((BuildPlan)seq.get(n16), (boolean[])object2);
                }
                Seq<BuildPlan> seq2 = AutoConnectConveyor.planRoute(nArray[0], nArray[1], nArray[2], (boolean[])object2);
                if (seq2 == null || seq2.isEmpty()) {
                    seq2 = AutoConnectConveyor.planRoute(nArray[0], nArray[1], -1, (boolean[])object2);
                }
                if (seq2 == null || seq2.isEmpty()) continue;
                if (AutoSetConfig.facDrill()) {
                    stringArray2 = AutoSet.feedItems(string);
                    n14 = 0;
                    for (n13 = 0; n13 < seq.size && n14 < 2; ++n13) {
                        tile = (Position)seq.get(n13);
                        if (((BuildPlan)tile).block == null || ((BuildPlan)tile).block.name == null || !((BuildPlan)tile).block.name.contains("conveyor") || !Vars.world.tiles.in(n10 = ((BuildPlan)tile).x - DX[n11 = ((BuildPlan)tile).rotation & 3], n12 = ((BuildPlan)tile).y - DY[n11]) || hashSet.contains(n12 * n4 + n10)) continue;
                        String seq3 = stringArray2[n14];
                        ++n14;
                        if (seq3 == null) continue;
                        feedTargets.add(new int[]{n10, n12, n11});
                        feedItemNames.add(seq3);
                        for (n8 = 0; n8 < 3; ++n8) {
                            n7 = n10 - n8 * DX[n11];
                            buildPlan = n12 - n8 * DY[n11];
                            if (!Vars.world.tiles.in(n7, buildPlan)) continue;
                            feedReserved.add(buildPlan * n4 + n7);
                        }
                    }
                }
                Seq seq3 = new Seq();
                for (BuildPlan buildPlan2 : unit.plans()) {
                    seq3.add((Object)buildPlan2);
                }
                for (n6 = 0; n6 < seq.size; ++n6) {
                    unit.addBuild((BuildPlan)seq.get(n6), true);
                }
                for (n6 = 0; n6 < seq2.size; ++n6) {
                    unit.addBuild((BuildPlan)seq2.get(n6), true);
                }
                pending.clear();
                for (BuildPlan buildPlan3 : unit.plans()) {
                    if (seq3.contains((Object)buildPlan3, true)) continue;
                    pending.add((Object)buildPlan3);
                }
                lastPending = AutoSet.pending.size;
                lastProgress = Time.time;
                AutoSet.toast(AutoSet.tr("F\u00e1brica ", "Factory ") + string + ": " + seq.size + "+" + seq2.size + AutoSet.tr(" blocos planejados", " blocks planned"));
                return;
            }
        }
        AutoSet.toast(AutoSet.tr("Sem espa\u00e7o/rota para a f\u00e1brica: ", "No space/route for factory: ") + string);
    }

    private static void checkPending(Unit unit) {
        Tile tile;
        BuildPlan buildPlan;
        int n;
        if (Time.time < nextCheck) {
            return;
        }
        nextCheck = Time.time + 15.0f;
        AutoSet.rememberOurs();
        for (n = AutoSet.pending.size - 1; n >= 0; --n) {
            buildPlan = (BuildPlan)pending.get(n);
            tile = Vars.world.tile(buildPlan.x, buildPlan.y);
            if (tile != null && tile.block() != buildPlan.block) continue;
            pending.remove(n);
        }
        if (pending.isEmpty()) {
            nextStepAt = Time.time + 15.0f;
            return;
        }
        if (AutoSet.pending.size < lastPending) {
            lastProgress = Time.time;
        }
        lastPending = AutoSet.pending.size;
        for (n = AutoSet.pending.size - 1; n >= 0; --n) {
            buildPlan = (BuildPlan)pending.get(n);
            if (unit.plans().contains((Object)buildPlan, true)) continue;
            if (Build.validPlace((Block)buildPlan.block, (Team)Vars.player.team(), (int)buildPlan.x, (int)buildPlan.y, (int)buildPlan.rotation)) {
                unit.addBuild(buildPlan, true);
                continue;
            }
            if (AutoSet.tryDelete(unit, buildPlan)) continue;
            pending.remove(n);
        }
        if (pending.isEmpty()) {
            nextStepAt = Time.time + 30.0f;
            return;
        }
        if (Time.time - lastProgress > 360.0f) {
            String string = AutoSet.missingResource();
            if (string != null && Time.time - lastProgress < 5400.0f) {
                if (Time.time - lastWarn > 300.0f) {
                    lastWarn = Time.time;
                    AutoSet.toast(AutoSet.tr("Aguardando recurso: ", "Waiting for resource: ") + string);
                }
                return;
            }
            int n2 = AutoSet.pending.size;
            pending.clear();
            nextStepAt = Time.time + 20.0f;
            AutoSet.toast(LunariansI18n.get("as_stalled") + " \u2192 skip (" + n2 + ")");
            return;
        }
        if (Time.time - lastProgress > 180.0f && Time.time - lastWarn > 180.0f) {
            lastWarn = Time.time;
            tile = AutoSet.nearestPending(unit);
            if (tile != null && !unit.within(((BuildPlan)tile).drawx(), ((BuildPlan)tile).drawy(), 220.0f)) {
                AutoSet.toast(LunariansI18n.get("as_approach", ((BuildPlan)tile).x + "," + ((BuildPlan)tile).y));
            } else {
                AutoSet.toast(LunariansI18n.get("as_stalled"));
            }
        }
    }

    private static void rememberOurs() {
        int n = Vars.world.width();
        for (int i = 0; i < AutoSet.pending.size; ++i) {
            BuildPlan buildPlan = (BuildPlan)pending.get(i);
            if (buildPlan == null || buildPlan.block == null || buildPlan.breaking || !seenPlans.add(buildPlan)) continue;
            int n2 = (buildPlan.block.size - 1) / 2;
            for (int j = 0; j < buildPlan.block.size; ++j) {
                for (int k = 0; k < buildPlan.block.size; ++k) {
                    ours.merge((buildPlan.y - n2 + j) * n + buildPlan.x - n2 + k, 1, Integer::sum);
                }
            }
        }
    }

    private static boolean deletable(Block block) {
        return block instanceof Conveyor || block instanceof Junction || block instanceof ItemBridge || block instanceof Router || block instanceof Sorter || block instanceof OverflowGate || block instanceof Drill || block instanceof PowerNode || block instanceof Conduit;
    }

    private static boolean tryDelete(Unit unit, BuildPlan buildPlan) {
        try {
            if (!AutoSetConfig.autoDelete() || deleteCount >= 30 || buildPlan.block == null || buildPlan.breaking) {
                return false;
            }
            int n = Vars.world.width();
            int n2 = (buildPlan.block.size - 1) / 2;
            boolean bl = false;
            for (int i = 0; i < buildPlan.block.size; ++i) {
                for (int j = 0; j < buildPlan.block.size; ++j) {
                    Integer n3;
                    Block block;
                    int n4 = buildPlan.x - n2 + j;
                    int n5 = buildPlan.y - n2 + i;
                    Tile tile = Vars.world.tile(n4, n5);
                    if (tile == null || tile.build == null || tile.build.team != Vars.player.team() || tile.build.block == buildPlan.block || (block = tile.build.block) instanceof ConstructBlock || block instanceof CoreBlock || !AutoSet.deletable(block) || (n3 = ours.get(n5 * n + n4)) == null || n3 < 2 || deleted.contains(tile.build.pos())) continue;
                    deleted.add(tile.build.pos());
                    ++deleteCount;
                    unit.addBuild(new BuildPlan(tile.build.tileX(), tile.build.tileY()), true);
                    bl = true;
                }
            }
            if (bl) {
                AutoSet.toast(AutoSet.tr("Auto delete: removendo bloco que atrapalha (" + deleteCount + "/30)", "Auto delete: removing blocking block (" + deleteCount + "/30)"));
            }
            return bl;
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    private static String missingResource() {
        try {
            if (Vars.state.rules.infiniteResources || Vars.player == null || Vars.player.closestCore() == null) {
                return null;
            }
            float f = Vars.state.rules.buildCostMultiplier;
            for (int i = 0; i < AutoSet.pending.size; ++i) {
                Tile tile;
                BuildPlan buildPlan = (BuildPlan)pending.get(i);
                if (buildPlan == null || buildPlan.block == null || buildPlan.breaking || (tile = Vars.world.tile(buildPlan.x, buildPlan.y)) != null && tile.block() == buildPlan.block) continue;
                for (ItemStack itemStack : buildPlan.block.requirements) {
                    if ((double)Vars.player.closestCore().items.get(itemStack.item) >= Math.ceil((float)itemStack.amount * f)) continue;
                    return itemStack.item.localizedName;
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return null;
    }

    private static BuildPlan nearestPending(Unit unit) {
        BuildPlan buildPlan = null;
        float f = Float.MAX_VALUE;
        for (int i = 0; i < AutoSet.pending.size; ++i) {
            BuildPlan buildPlan2 = (BuildPlan)pending.get(i);
            float f2 = Mathf.dst((float)unit.x, (float)unit.y, (float)buildPlan2.drawx(), (float)buildPlan2.drawy());
            if (!(f2 < f)) continue;
            f = f2;
            buildPlan = buildPlan2;
        }
        return buildPlan;
    }

    private static void setExitTowardCore(int n, int n2) {
        try {
            if (Vars.player == null || Vars.player.closestCore() == null) {
                return;
            }
            float f = Vars.player.closestCore().tileX() - n;
            float f2 = Vars.player.closestCore().tileY() - n2;
            LunariansSettings.drillExitDir = Math.abs(f) >= Math.abs(f2) ? (f >= 0.0f ? 0 : 2) : (f2 >= 0.0f ? 1 : 3);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static boolean inCircle(int n, int n2) {
        return Mathf.dst((float)n, (float)n2, (float)cx, (float)cy) <= radius;
    }

    private static int[] findOre(Item item) {
        return AutoSet.findOre(item, cx, cy);
    }

    private static int[] findMarked(Item item, int n, int n2) {
        int[] nArray;
        int[] nArray2 = AutoEconomy.markedTiles(item);
        if (nArray2.length == 0 || used == null) {
            return null;
        }
        int n3 = Vars.world.width();
        int n4 = -1;
        int n5 = -1;
        int n6 = Integer.MIN_VALUE;
        for (int n7 : nArray2) {
            int n8;
            int n9;
            Tile tile;
            if (n7 < 0 || n7 >= used.length || used[n7] || (tile = Vars.world.tile(n9 = n7 % n3, n8 = n7 / n3)) == null || tile.build != null || tile.solid() || !AutoSet.sameOre(tile, item)) continue;
            int n10 = 1000 - (int)Mathf.dst((float)n9, (float)n8, (float)n, (float)n2);
            for (int i = 0; i < 8; ++i) {
                int n11 = n9 + DX[i];
                int n12 = n8 + DY[i];
                if (!Vars.world.tiles.in(n11, n12) || used[n12 * n3 + n11] || !AutoSet.sameOre(Vars.world.tile(n11, n12), item)) continue;
                n10 += 5;
            }
            if (n10 <= n6) continue;
            n6 = n10;
            n4 = n9;
            n5 = n8;
        }
        if (n4 < 0) {
            nArray = null;
        } else {
            int[] nArray3 = new int[2];
            nArray3[0] = n4;
            nArray = nArray3;
            nArray3[1] = n5;
        }
        return nArray;
    }

    private static int[] findOre(Item item, int n, int n2) {
        int[] nArray = AutoSet.findMarked(item, n, n2);
        if (nArray != null) {
            AutoDrill.isoOverride = 1;
            return nArray;
        }
        AutoDrill.isoOverride = -1;
        int[] nArray2 = AutoSet.findOreM(item, n, n2, AutoSetConfig.spacing() + 1);
        if (nArray2 == null && (nArray2 = AutoSet.findOreM(item, n, n2, 1)) != null) {
            AutoDrill.isoOverride = 1;
        }
        return nArray2;
    }

    private static int[] findOreM(Item item, int n, int n2, int n3) {
        int n4 = Vars.world.width();
        int n5 = (int)Math.ceil(radius);
        int n6 = -1;
        int n7 = -1;
        int n8 = Integer.MIN_VALUE;
        for (int i = cy - n5; i <= cy + n5; ++i) {
            for (int j = cx - n5; j <= cx + n5; ++j) {
                Tile tile;
                if (!Vars.world.tiles.in(j, i) || !AutoSet.inCircle(j, i) || used[i * n4 + j] || (tile = Vars.world.tile(j, i)) == null || tile.build != null || tile.solid() || !AutoSet.sameOre(tile, item) || AutoDrill.crowded(j, i, 1, n3)) continue;
                int n9 = 1000 - (int)Mathf.dst((float)j, (float)i, (float)n, (float)n2);
                for (int k = 0; k < 8; ++k) {
                    int n10 = j + DX[k];
                    int n11 = i + DY[k];
                    if (!Vars.world.tiles.in(n10, n11) || used[n11 * n4 + n10] || !AutoSet.sameOre(Vars.world.tile(n10, n11), item)) continue;
                    n9 += 5;
                }
                if (n9 <= n8) continue;
                n8 = n9;
                n6 = j;
                n7 = i;
            }
        }
        if (n6 < 0) {
            return null;
        }
        return new int[]{n6, n7};
    }

    private static void markUsed(int n, int n2, Item item) {
        int n3 = Vars.world.width();
        int n4 = Math.max(16, LunariansSettings.drillMaxTiles);
        IntQueue intQueue = new IntQueue();
        boolean[] blArray = new boolean[n3 * Vars.world.height()];
        int n5 = n2 * n3 + n;
        intQueue.addLast(n5);
        blArray[n5] = true;
        int n6 = 0;
        while (!intQueue.isEmpty() && n6 < n4) {
            int n7 = intQueue.removeFirst();
            AutoSet.used[n7] = true;
            ++n6;
            int n8 = n7 % n3;
            int n9 = n7 / n3;
            for (int i = 0; i < 4; ++i) {
                int n10;
                int n11 = n8 + DX[i];
                int n12 = n9 + DY[i];
                if (!Vars.world.tiles.in(n11, n12) || blArray[n10 = n12 * n3 + n11] || !AutoSet.inCircle(n11, n12) || !AutoSet.sameOre(Vars.world.tile(n11, n12), item) || !AutoDrill.inFloor(n11, n12, n, n2)) continue;
                blArray[n10] = true;
                intQueue.addLast(n10);
            }
        }
    }

    private static boolean sameOre(Tile tile, Item item) {
        if (tile == null || item == null) {
            return false;
        }
        try {
            if (tile.drop() != null) {
                return tile.drop() == item;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        Floor floor = tile.overlay() != null ? tile.overlay() : tile.floor();
        return floor != null && floor.itemDrop == item;
    }

    private static void draw() {
        if (phase != 2 && !hasArea) {
            return;
        }
        if (Vars.state == null || !Vars.state.isGame() || cx == Integer.MIN_VALUE) {
            return;
        }
        try {
            float f = 8.0f;
            float f2 = (float)cx * f;
            float f3 = (float)cy * f;
            if (phase == 2) {
                if (!centerChosen) {
                    return;
                }
                Vec2 vec2 = Core.input.mouseWorld();
                float f4 = Mathf.dst((float)f2, (float)f3, (float)vec2.x, (float)vec2.y);
                f4 = Math.min(f4, 80.0f * f);
                Draw.color((Color)Color.cyan, (float)0.15f);
                Fill.circle((float)f2, (float)f3, (float)f4);
                Draw.color((Color)Color.cyan, (float)0.9f);
                Lines.stroke((float)2.0f);
                Lines.circle((float)f2, (float)f3, (float)f4);
                Draw.reset();
                return;
            }
            float f5 = radius * f;
            Draw.color((Color)Color.orange, (float)0.12f);
            Fill.circle((float)f2, (float)f3, (float)f5);
            Draw.color((Color)Color.orange, (float)0.85f);
            Lines.stroke((float)2.0f);
            Lines.circle((float)f2, (float)f3, (float)f5);
            Draw.color((Color)Color.yellow, (float)0.9f);
            Lines.stroke((float)1.0f);
            for (int i = 0; i < AutoSet.pending.size; ++i) {
                BuildPlan buildPlan = (BuildPlan)pending.get(i);
                Lines.square((float)buildPlan.drawx(), (float)buildPlan.drawy(), (float)4.0f);
            }
            Draw.reset();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void toast(String string) {
        if (Vars.ui != null) {
            Vars.ui.showInfoToast(string, 3.0f);
        }
    }

    static {
        ours = new HashMap();
        seenPlans = Collections.newSetFromMap(new IdentityHashMap());
        deleted = new HashSet();
        feedLines = new HashMap();
        schemCache = new HashMap();
        feedTargets = new ArrayList();
        feedItemNames = new ArrayList();
        feedReserved = new ArrayList();
        cx = Integer.MIN_VALUE;
        pending = new Seq();
    }
}

