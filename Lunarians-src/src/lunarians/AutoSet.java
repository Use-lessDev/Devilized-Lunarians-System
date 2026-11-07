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
import mindustry.world.Tiles;
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

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoSet.class */
public class AutoSet {
    private static int runMode;
    private static boolean registered;
    private static boolean errorLogged;
    private static int phase;
    private static float armedAt;
    private static boolean centerChosen;
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
    private static int feedIndex;
    private static int steamDone;
    private static boolean miningActive;
    private static float nextMineCheck;
    private static float lastMineHint;
    private static int deleteCount;
    private static final int[] DX = {1, 0, -1, 0, 1, 1, -1, -1};
    private static final int[] DY = {0, 1, 0, -1, 1, -1, 1, -1};
    private static String[] oreOrder = {"copper", "copper", "copper", "lead", "coal", "sand", "coal", "sand"};
    private static String[] factoryQueue = new String[0];
    private static final HashMap<Integer, Integer> ours = new HashMap<>();
    private static final Set<BuildPlan> seenPlans = Collections.newSetFromMap(new IdentityHashMap());
    private static final HashSet<Integer> deleted = new HashSet<>();
    private static final HashMap<String, ArrayList<int[]>> feedLines = new HashMap<>();
    private static final HashMap<String, Schematic> schemCache = new HashMap<>();
    private static final ArrayList<int[]> feedTargets = new ArrayList<>();
    private static final ArrayList<String> feedItemNames = new ArrayList<>();
    private static final ArrayList<Integer> feedReserved = new ArrayList<>();
    private static int cx = Integer.MIN_VALUE;
    private static final Seq<BuildPlan> pending = new Seq<>();

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSet$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoSet.$r8$lambda$3U-wYmrw-2MOz0iqXbzuvWWz8jA();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSet$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoSet.$r8$lambda$4v9r_6bSqeQUkEJ_gsR7us4eunY();
        }
    }

    public static void init() {
        if (registered) {
            return;
        }
        registered = true;
        Events.run(EventType.Trigger.update, AutoSet::update);
        Events.run(EventType.Trigger.draw, AutoSet::draw);
    }

    private static void stopMining(Unit unit) {
        if (!miningActive) {
            return;
        }
        miningActive = false;
        if (unit != null) {
            try {
                unit.mineTile = null;
                unit.updateBuilding = true;
            } catch (Throwable th) {
                return;
            }
        }
        if (Vars.control != null && Vars.control.input != null) {
            Vars.control.input.isBuilding = true;
        }
    }

    private static void autoMine(Unit unit) {
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
        CoreBlock.CoreBuild closestCore = Vars.player.closestCore();
        int[] iArr = new int[Vars.content.items().size];
        boolean z = false;
        for (int i = 0; i < pending.size; i++) {
            Object obj = pending.get(i);
            Tile tile = Vars.world.tile(((BuildPlan) obj).x, ((BuildPlan) obj).y);
            if (!((BuildPlan) obj).breaking && ((BuildPlan) obj).block != null && (tile == null || tile.block() != ((BuildPlan) obj).block)) {
                for (ItemStack itemStack : ((BuildPlan) obj).block.requirements) {
                    short s = itemStack.item.id;
                    iArr[s] = iArr[s] + ((int) Math.ceil(r0.amount * Vars.state.rules.buildCostMultiplier));
                    z = true;
                }
            }
        }
        Item item = null;
        if (z && !Vars.state.rules.infiniteResources) {
            Iterator it = Vars.content.items().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Item item2 = (Item) it.next();
                if (iArr[item2.id] > closestCore.items.get(item2) && unit.type.mineTier >= item2.hardness && unit.type.mineItems != null && unit.type.mineItems.contains(item2)) {
                    item = item2;
                    break;
                }
            }
        }
        if (item == null) {
            stopMining(unit);
            return;
        }
        Tile tile2 = null;
        float f = Float.MAX_VALUE;
        int i2 = ((int) (unit.type.mineRange / 8.0f)) + 2;
        int tileX = unit.tileX();
        int tileY = unit.tileY();
        for (int i3 = tileY - i2; i3 <= tileY + i2; i3++) {
            for (int i4 = tileX - i2; i4 <= tileX + i2; i4++) {
                Tile tile3 = Vars.world.tile(i4, i3);
                if (tile3 != null && tile3.build == null && tile3.drop() == item && unit.validMine(tile3)) {
                    float dst = unit.dst(tile3.worldx(), tile3.worldy());
                    if (dst < f) {
                        f = dst;
                        tile2 = tile3;
                    }
                }
            }
        }
        if (tile2 != null) {
            if (!miningActive) {
                toast(tr("Faltando " + item.localizedName + ": minerando automaticamente", "Missing " + item.localizedName + ": mining automatically"));
            }
            miningActive = true;
            unit.mineTile = tile2;
            unit.updateBuilding = false;
            Vars.control.input.isBuilding = false;
            return;
        }
        stopMining(unit);
        if (Time.time - lastMineHint > 600.0f) {
            lastMineHint = Time.time;
            toast(tr("Faltando " + item.localizedName + ": aproxime sua unidade do minério para minerar automaticamente", "Missing " + item.localizedName + ": move your unit near its ore to auto-mine"));
        }
    }

    public static void reset() {
        try {
            stopMining(Vars.player == null ? null : Vars.player.unit());
        } catch (Throwable th) {
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
            toast(LunariansI18n.get("as_enter_map"));
            return;
        }
        if (phase == 3) {
            toast(LunariansI18n.get("as_running"));
            return;
        }
        if (phase == 2) {
            toast(LunariansI18n.get("as_pick_hint"));
            return;
        }
        if (phase == 1 && Time.time - armedAt < 180.0f) {
            phase = 2;
            centerChosen = false;
            hasArea = false;
            toast(LunariansI18n.get("as_tap_center"));
            return;
        }
        phase = 1;
        armedAt = Time.time;
        toast(LunariansI18n.get("as_confirm"));
    }

    public static void cancel() {
        try {
            Unit unit = Vars.player == null ? null : Vars.player.unit();
            if (unit != null && phase == 3) {
                for (int i = 0; i < pending.size; i++) {
                    unit.plans().remove((BuildPlan) pending.get(i));
                }
            }
        } catch (Throwable th) {
        }
        reset();
        toast(LunariansI18n.get("as_cancelled"));
    }

    private static void update() {
        if (Vars.headless || Vars.state == null || !Vars.state.isGame()) {
            if (phase != 0) {
                reset();
                return;
            }
            return;
        }
        try {
            if (phase == 1 && Time.time - armedAt > 200.0f) {
                phase = 0;
                toast(LunariansI18n.get("as_timeout"));
            } else if (phase == 2) {
                handleAreaPick();
            } else {
                if (phase == 3) {
                    runSteps();
                }
            }
        } catch (Throwable th) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err("[Lunarians] AutoSet error", th);
            }
        }
    }

    private static void handleAreaPick() {
        if (Core.input.keyTap(KeyCode.escape) || Core.input.keyTap(KeyCode.back)) {
            cancel();
            return;
        }
        if (!Core.input.justTouched() || Core.scene.hasMouse() || Core.scene.hasDialog()) {
            return;
        }
        Vec2 mouseWorld = Core.input.mouseWorld();
        int tile = World.toTile(mouseWorld.x);
        Tiles tiles = Vars.world.tiles;
        int tile2 = World.toTile(mouseWorld.y);
        if (!tiles.in(tile, tile2)) {
            return;
        }
        if (!centerChosen) {
            cx = tile;
            cy = tile2;
            centerChosen = true;
            toast(LunariansI18n.get("as_tap_radius"));
            return;
        }
        float dst = Mathf.dst(cx, cy, tile, tile2);
        if (dst < 3.0f) {
            toast(LunariansI18n.get("as_radius_small"));
            return;
        }
        radius = Math.min(dst, 80.0f);
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
        toast(LunariansI18n.get("as_started", String.valueOf((int) radius)));
    }

    private static void runSteps() {
        Unit unit = Vars.player == null ? null : Vars.player.unit();
        if (unit == null || !unit.canBuild()) {
            return;
        }
        if (Vars.player.closestCore() == null) {
            toast(LunariansI18n.get("as_no_core"));
            reset();
            return;
        }
        autoMine(unit);
        if (pending.size > 0) {
            checkPending(unit);
            return;
        }
        if (Time.time < nextStepAt) {
            return;
        }
        if (runMode == 0 && AutoSetConfig.steamOn() && steamDone < AutoSetConfig.steamCount()) {
            steamDone++;
            nextStepAt = Time.time + 30.0f;
            startSteamStep(unit);
        } else if (runMode != 1 ? stepIndex >= oreOrder.length : !(stepIndex < factoryQueue.length || feedIndex < feedTargets.size())) {
            reset();
            toast(LunariansI18n.get("as_done"));
        } else {
            if (runMode == 1) {
                if (stepIndex < factoryQueue.length) {
                    startFactoryStep(unit);
                    return;
                } else {
                    startFeedStep(unit);
                    return;
                }
            }
            startStep(unit);
        }
    }

    private static Item itemFor(String str) {
        Item item = Vars.content.item(str);
        if (item != null) {
            return item;
        }
        if ("copper".equals(str)) {
            return Items.copper;
        }
        if ("lead".equals(str)) {
            return Items.lead;
        }
        if ("coal".equals(str)) {
            return Items.coal;
        }
        if ("sand".equals(str)) {
            return Items.sand;
        }
        return null;
    }

    private static void startStep(Unit unit) {
        Item itemFor = itemFor(oreOrder[stepIndex]);
        stepIndex++;
        nextStepAt = Time.time + 30.0f;
        if (itemFor == null) {
            return;
        }
        boolean z = false;
        boolean z2 = false;
        AutoDrill.limitOn = true;
        AutoDrill.limitX = cx;
        AutoDrill.limitY = cy;
        AutoDrill.limitR = radius;
        AutoDrill.quiet = true;
        try {
            for (int i = 0; i < 4 && !z; i++) {
                try {
                    int[] findOre = findOre(itemFor);
                    if (findOre == null) {
                        break;
                    }
                    z2 = true;
                    markUsed(findOre[0], findOre[1], itemFor);
                    LunariansSettings.drillStartX = findOre[0];
                    LunariansSettings.drillStartY = findOre[1];
                    setExitTowardCore(findOre[0], findOre[1]);
                    Seq seq = new Seq();
                    Iterator it = unit.plans().iterator();
                    while (it.hasNext()) {
                        seq.add((BuildPlan) it.next());
                    }
                    int generateWithConveyor = AutoDrill.generateWithConveyor();
                    if (generateWithConveyor > 0) {
                        pending.clear();
                        Iterator it2 = unit.plans().iterator();
                        while (it2.hasNext()) {
                            BuildPlan buildPlan = (BuildPlan) it2.next();
                            if (!seq.contains(buildPlan, true)) {
                                pending.add(buildPlan);
                            }
                        }
                        z = pending.size > 0;
                        if (z) {
                            lastPending = pending.size;
                            lastProgress = Time.time;
                            toast(LunariansI18n.get("as_step", itemFor.localizedName, String.valueOf(generateWithConveyor)));
                        }
                    }
                } catch (Throwable th) {
                    Log.err("[Lunarians] AutoSet step", th);
                    AutoDrill.isoOverride = -1;
                    AutoDrill.quiet = false;
                    AutoDrill.limitOn = false;
                    LunariansSettings.save();
                }
            }
            AutoDrill.isoOverride = -1;
            AutoDrill.quiet = false;
            AutoDrill.limitOn = false;
            LunariansSettings.save();
            if (!z) {
                toast(LunariansI18n.get(z2 ? "as_no_route" : "as_no_ore", itemFor.localizedName));
            }
        } catch (Throwable th2) {
            AutoDrill.isoOverride = -1;
            AutoDrill.quiet = false;
            AutoDrill.limitOn = false;
            LunariansSettings.save();
            throw th2;
        }
    }

    private static void startSteamStep(Unit unit) {
        try {
            Seq<BuildPlan> plan = SteamDrill.plan(unit, cx, cy, radius, used);
            if (plan == null || plan.isEmpty()) {
                steamDone = AutoSetConfig.steamCount();
                toast("Steam Drill: " + SteamDrill.lastError);
                return;
            }
            Seq seq = new Seq();
            Iterator it = unit.plans().iterator();
            while (it.hasNext()) {
                seq.add((BuildPlan) it.next());
            }
            if (used != null) {
                Iterator<Integer> it2 = SteamDrill.lastTiles.iterator();
                while (it2.hasNext()) {
                    Integer next = it2.next();
                    if (next.intValue() >= 0 && next.intValue() < used.length) {
                        used[next.intValue()] = true;
                    }
                }
            }
            for (int i = 0; i < plan.size; i++) {
                unit.addBuild((BuildPlan) plan.get(i), true);
            }
            pending.clear();
            Iterator it3 = unit.plans().iterator();
            while (it3.hasNext()) {
                BuildPlan buildPlan = (BuildPlan) it3.next();
                if (!seq.contains(buildPlan, true)) {
                    pending.add(buildPlan);
                }
            }
            lastPending = pending.size;
            lastProgress = Time.time;
            toast("Steam Drill: " + plan.size + tr(" blocos planejados", " blocks planned"));
        } catch (Throwable th) {
            Log.err("[Lunarians] steam step", th);
        }
    }

    private static String tr(String str, String str2) {
        return "pt".equals(LunariansI18n.getLanguage()) ? str : str2;
    }

    private static Schematic schemFor(String str) {
        Schematic schematic = schemCache.get(str);
        if (schematic == null) {
            try {
                schematic = Schematics.readBase64(AutoSetConfig.schematicFor(str));
            } catch (Throwable th) {
                Log.err("[Lunarians] schematic " + str, th);
            }
            if (schematic != null) {
                schemCache.put(str, schematic);
            }
        }
        return schematic;
    }

    private static void rotatePlansAround(Seq<BuildPlan> seq, int i, int i2) {
        for (int i3 = 0; i3 < seq.size; i3++) {
            BuildPlan buildPlan = (BuildPlan) seq.get(i3);
            if (!buildPlan.breaking && buildPlan.block != null) {
                float f = buildPlan.block.offset;
                buildPlan.x = Math.round(((-(((buildPlan.y - i2) * 8) + f)) - f) / 8.0f) + i;
                buildPlan.y = Math.round(((((buildPlan.x - i) * 8) + f) - f) / 8.0f) + i2;
                buildPlan.rotation = buildPlan.block.planRotation(Mathf.mod(buildPlan.rotation + 1, 4));
            }
        }
    }

    private static boolean planFits(Seq<BuildPlan> seq, boolean[] zArr, HashSet<Integer> hashSet) {
        int width = Vars.world.width();
        for (int i = 0; i < seq.size; i++) {
            BuildPlan buildPlan = (BuildPlan) seq.get(i);
            if (buildPlan.block == null) {
                return false;
            }
            int i2 = buildPlan.block.size;
            int i3 = (i2 - 1) / 2;
            for (int i4 = 0; i4 < i2; i4++) {
                for (int i5 = 0; i5 < i2; i5++) {
                    int i6 = (buildPlan.x - i3) + i5;
                    int i7 = (buildPlan.y - i3) + i4;
                    if (!Vars.world.tiles.in(i6, i7) || zArr[(i7 * width) + i6]) {
                        return false;
                    }
                    hashSet.add(Integer.valueOf((i7 * width) + i6));
                }
            }
            if (!Build.validPlace(buildPlan.block, Vars.player.team(), buildPlan.x, buildPlan.y, buildPlan.rotation)) {
                return false;
            }
        }
        return true;
    }

    private static int[] findOutput(Seq<BuildPlan> seq, HashSet<Integer> hashSet) {
        int width = Vars.world.width();
        for (int i = 0; i < seq.size; i++) {
            BuildPlan buildPlan = (BuildPlan) seq.get(i);
            if (buildPlan.block != null && buildPlan.block.name != null && buildPlan.block.name.contains("conveyor")) {
                Tiles tiles = Vars.world.tiles;
                int i2 = buildPlan.x;
                int[] iArr = DX;
                int i3 = buildPlan.rotation & 3;
                int i4 = i2 + iArr[i3];
                int i5 = buildPlan.y + DY[i3];
                if (tiles.in(i4, i5) && !hashSet.contains(Integer.valueOf((i5 * width) + i4))) {
                    return new int[]{i4, i5, i3};
                }
            }
        }
        return null;
    }

    private static String[] feedItems(String str) {
        if ("silicon".equals(str)) {
            return new String[]{"coal", "sand"};
        }
        if ("metaglass".equals(str)) {
            return new String[]{"lead", "sand"};
        }
        return new String[]{"coal", null};
    }

    private static Block blockByName(String str, String str2) {
        Block block = Vars.content.block(str);
        if (block == null && str2 != null) {
            block = Vars.content.block(str2);
        }
        return block;
    }

    private static int genInner() {
        try {
            Method declaredMethod = AutoDrill.class.getDeclaredMethod("generateInner", new Class[0]);
            declaredMethod.setAccessible(true);
            return ((Integer) declaredMethod.invoke(null, new Object[0])).intValue();
        } catch (Throwable th) {
            Log.err("[Lunarians] AutoSet genInner", th);
            return 0;
        }
    }

    private static Seq<BuildPlan> routeTo(int i, int i2, int i3, int i4, int i5, boolean[] zArr, Block block) {
        int width = Vars.world.width();
        int height = Vars.world.height();
        Team team = Vars.player.team();
        if (block == null || !freeFor(block, team, i, i2, zArr) || !freeFor(block, team, i3, i4, zArr)) {
            return null;
        }
        int[] iArr = new int[width * height];
        Arrays.fill(iArr, -2);
        ArrayDeque arrayDeque = new ArrayDeque();
        int i6 = (i2 * width) + i;
        int i7 = (i4 * width) + i3;
        iArr[i6] = -1;
        arrayDeque.add(Integer.valueOf(i6));
        int i8 = 0;
        while (!arrayDeque.isEmpty() && iArr[i7] == -2) {
            int i9 = i8;
            i8++;
            if (i9 >= 60000) {
                break;
            }
            int intValue = ((Integer) arrayDeque.poll()).intValue();
            int i10 = intValue % width;
            int i11 = intValue / width;
            for (int i12 = 0; i12 < 4; i12++) {
                int i13 = i10 + DX[i12];
                int i14 = i11 + DY[i12];
                if (Vars.world.tiles.in(i13, i14)) {
                    int i15 = (i14 * width) + i13;
                    if (iArr[i15] == -2 && freeFor(block, team, i13, i14, zArr)) {
                        iArr[i15] = intValue;
                        arrayDeque.add(Integer.valueOf(i15));
                    }
                }
            }
        }
        if (iArr[i7] == -2) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int i16 = i7;
        while (true) {
            int i17 = i16;
            if (i17 == -1) {
                break;
            }
            arrayList.add(Integer.valueOf(i17));
            i16 = iArr[i17];
        }
        Collections.reverse(arrayList);
        Seq<BuildPlan> seq = new Seq<>();
        for (int i18 = 0; i18 < arrayList.size(); i18++) {
            int intValue2 = ((Integer) arrayList.get(i18)).intValue();
            int i19 = intValue2 % width;
            int i20 = intValue2 / width;
            int i21 = i5;
            if (i18 + 1 < arrayList.size()) {
                int intValue3 = ((Integer) arrayList.get(i18 + 1)).intValue();
                int i22 = intValue3 % width;
                i21 = i22 > i19 ? 0 : i22 < i19 ? 2 : intValue3 / width > i20 ? 1 : 3;
            }
            seq.add(new BuildPlan(i19, i20, i21, block));
        }
        return seq;
    }

    private static boolean freeFor(Block block, Team team, int i, int i2, boolean[] zArr) {
        Tile tile;
        if (!Vars.world.tiles.in(i, i2) || zArr[(i2 * Vars.world.width()) + i] || (tile = Vars.world.tile(i, i2)) == null || tile.build != null) {
            return false;
        }
        return Build.validPlace(block, team, i, i2, 0);
    }

    /* JADX WARN: Finally extract failed */
    private static void startFeedStep(Unit unit) {
        int[] iArr = feedTargets.get(feedIndex);
        String str = feedItemNames.get(feedIndex);
        feedIndex++;
        nextStepAt = Time.time + 30.0f;
        Item itemFor = itemFor(str);
        Block blockByName = blockByName("conveyor", "titanium-conveyor");
        if (itemFor == null || blockByName == null) {
            return;
        }
        boolean z = false;
        AutoDrill.limitOn = true;
        AutoDrill.limitX = cx;
        AutoDrill.limitY = cy;
        AutoDrill.limitR = radius;
        AutoDrill.quiet = true;
        int i = LunariansSettings.drillExitDir;
        try {
            for (int i2 = 0; i2 < 6 && !z; i2++) {
                try {
                    int[] findOre = findOre(itemFor, iArr[0], iArr[1]);
                    if (findOre == null) {
                        break;
                    }
                    markUsed(findOre[0], findOre[1], itemFor);
                    LunariansSettings.drillStartX = findOre[0];
                    LunariansSettings.drillStartY = findOre[1];
                    float f = iArr[0] - findOre[0];
                    float f2 = iArr[1] - findOre[1];
                    int i3 = Math.abs(f) >= Math.abs(f2) ? f >= 0.0f ? 0 : 2 : f2 >= 0.0f ? 1 : 3;
                    int i4 = Math.abs(f) >= Math.abs(f2) ? f2 >= 0.0f ? 1 : 3 : f >= 0.0f ? 0 : 2;
                    int[] iArr2 = {i3, i4, i4 ^ 2, i3 ^ 2};
                    boolean[] reservedForFeed = AutoDrill.reservedForFeed(unit);
                    for (int i5 = 0; i5 < 4 && !z; i5++) {
                        int i6 = iArr2[i5];
                        LunariansSettings.drillExitDir = i6;
                        AutoDrill.dryRun = true;
                        try {
                            int genInner = genInner();
                            AutoDrill.dryRun = false;
                            if (genInner <= 0) {
                                break;
                            }
                            if (AutoDrill.lastHub >= 0) {
                                Seq seq = new Seq();
                                for (int i7 = 0; i7 < AutoDrill.lastPlans.size; i7++) {
                                    seq.add((BuildPlan) AutoDrill.lastPlans.get(i7));
                                }
                                boolean[] zArr = (boolean[]) reservedForFeed.clone();
                                for (int i8 = 0; i8 < seq.size; i8++) {
                                    AutoConnectConveyor.markPlan((BuildPlan) seq.get(i8), zArr);
                                }
                                Seq<BuildPlan> routeTo = routeTo((AutoDrill.lastHub % Vars.world.width()) + DX[i6], (AutoDrill.lastHub / Vars.world.width()) + DY[i6], iArr[0], iArr[1], iArr[2], zArr, blockByName);
                                if (routeTo != null) {
                                    Seq seq2 = new Seq();
                                    Iterator it = unit.plans().iterator();
                                    while (it.hasNext()) {
                                        seq2.add((BuildPlan) it.next());
                                    }
                                    for (int i9 = 0; i9 < seq.size; i9++) {
                                        unit.addBuild((BuildPlan) seq.get(i9), true);
                                    }
                                    for (int i10 = 0; i10 < routeTo.size; i10++) {
                                        unit.addBuild((BuildPlan) routeTo.get(i10), true);
                                    }
                                    pending.clear();
                                    Iterator it2 = unit.plans().iterator();
                                    while (it2.hasNext()) {
                                        BuildPlan buildPlan = (BuildPlan) it2.next();
                                        if (!seq2.contains(buildPlan, true)) {
                                            pending.add(buildPlan);
                                        }
                                    }
                                    lastPending = pending.size;
                                    lastProgress = Time.time;
                                    z = true;
                                    ArrayList<int[]> arrayList = feedLines.get(str);
                                    if (arrayList == null) {
                                        arrayList = new ArrayList<>();
                                        feedLines.put(str, arrayList);
                                    }
                                    for (int i11 = 0; i11 < routeTo.size; i11++) {
                                        BuildPlan buildPlan2 = (BuildPlan) routeTo.get(i11);
                                        if (buildPlan2.block instanceof Conveyor) {
                                            arrayList.add(new int[]{buildPlan2.x, buildPlan2.y});
                                        }
                                    }
                                    toast(itemFor.localizedName + tr(" → entrada da fábrica: ", " → factory input: ") + genInner + " drills + " + routeTo.size + tr(" esteiras", " conveyors"));
                                }
                            }
                        } catch (Throwable th) {
                            AutoDrill.dryRun = false;
                            throw th;
                        }
                    }
                } catch (Throwable th2) {
                    Log.err("[Lunarians] AutoSet feed step", th2);
                    AutoDrill.isoOverride = -1;
                    AutoDrill.quiet = false;
                    AutoDrill.limitOn = false;
                    AutoDrill.dryRun = false;
                    LunariansSettings.drillExitDir = i;
                    LunariansSettings.save();
                }
            }
            if (!z) {
                z = tapLine(unit, str, iArr);
            }
            AutoDrill.isoOverride = -1;
            AutoDrill.quiet = false;
            AutoDrill.limitOn = false;
            AutoDrill.dryRun = false;
            LunariansSettings.drillExitDir = i;
            LunariansSettings.save();
            if (!z) {
                toast(tr("Sem minério/rota para ", "No ore/route for ") + itemFor.localizedName);
            }
        } catch (Throwable th3) {
            AutoDrill.isoOverride = -1;
            AutoDrill.quiet = false;
            AutoDrill.limitOn = false;
            AutoDrill.dryRun = false;
            LunariansSettings.drillExitDir = i;
            LunariansSettings.save();
            throw th3;
        }
    }

    private static boolean tapLine(Unit unit, String str, int[] iArr) {
        ArrayList<int[]> arrayList = feedLines.get(str);
        Block blockByName = blockByName("conveyor", "titanium-conveyor");
        if (arrayList == null || arrayList.isEmpty() || blockByName == null) {
            return false;
        }
        HashSet hashSet = new HashSet();
        int width = Vars.world.width();
        Iterator<int[]> it = arrayList.iterator();
        while (it.hasNext()) {
            int[] next = it.next();
            hashSet.add(Integer.valueOf((next[1] * width) + next[0]));
        }
        boolean[] reservedForFeed = AutoDrill.reservedForFeed(unit);
        for (int i = 0; i < feedReserved.size(); i++) {
            reservedForFeed[feedReserved.get(i).intValue()] = true;
        }
        Seq seq = new Seq();
        Iterator<int[]> it2 = arrayList.iterator();
        while (it2.hasNext()) {
            int[] next2 = it2.next();
            Tile tile = Vars.world.tile(next2[0], next2[1]);
            if (tile != null && tile.build != null && (tile.build.block instanceof Conveyor)) {
                boolean z = true;
                for (int i2 = 0; i2 < 4 && z; i2++) {
                    Tile tile2 = Vars.world.tile(next2[0] + DX[i2], next2[1] + DY[i2]);
                    if (tile2 != null && tile2.build != null && !hashSet.contains(Integer.valueOf((tile2.y * width) + tile2.x))) {
                        z = false;
                    }
                }
                if (z) {
                    seq.add(new int[]{next2[0], next2[1], (int) (Mathf.dst(next2[0], next2[1], iArr[0], iArr[1]) * 10.0f)});
                }
            }
        }
        seq.sort(new Comparator<int[]>() { // from class: lunarians.AutoSet.1
            @Override // java.util.Comparator
            public int compare(int[] iArr2, int[] iArr3) {
                return iArr2[2] - iArr3[2];
            }
        });
        for (int i3 = 0; i3 < seq.size && i3 < 14; i3++) {
            int[] iArr2 = (int[]) seq.get(i3);
            for (int i4 = 0; i4 < 4; i4++) {
                Seq<BuildPlan> routeTo = routeTo(iArr2[0] + DX[i4], iArr2[1] + DY[i4], iArr[0], iArr[1], iArr[2], reservedForFeed, blockByName);
                if (routeTo != null) {
                    Seq seq2 = new Seq();
                    Iterator it3 = unit.plans().iterator();
                    while (it3.hasNext()) {
                        seq2.add((BuildPlan) it3.next());
                    }
                    unit.addBuild(new BuildPlan(iArr2[0], iArr2[1], 0, Blocks.router), true);
                    for (int i5 = 0; i5 < routeTo.size; i5++) {
                        unit.addBuild((BuildPlan) routeTo.get(i5), true);
                    }
                    pending.clear();
                    Iterator it4 = unit.plans().iterator();
                    while (it4.hasNext()) {
                        BuildPlan buildPlan = (BuildPlan) it4.next();
                        if (!seq2.contains(buildPlan, true)) {
                            pending.add(buildPlan);
                        }
                    }
                    lastPending = pending.size;
                    lastProgress = Time.time;
                    toast(tr("Sem outra fonte livre: router em " + iArr2[0] + "," + iArr2[1], "No other free source: router at " + iArr2[0] + "," + iArr2[1]));
                    return true;
                }
            }
        }
        return false;
    }

    private static int[] oreDistGrid(Item item, int i) {
        Tile tile;
        int i2 = (i * 2) + 1;
        int[] iArr = new int[i2 * i2];
        Arrays.fill(iArr, -1);
        if (item == null) {
            return iArr;
        }
        IntQueue intQueue = new IntQueue();
        int i3 = cx - i;
        int i4 = cy - i;
        int width = Vars.world.width();
        for (int i5 = 0; i5 < i2; i5++) {
            for (int i6 = 0; i6 < i2; i6++) {
                int i7 = i3 + i6;
                int i8 = i4 + i5;
                if (Vars.world.tiles.in(i7, i8) && inCircle(i7, i8) && !used[(i8 * width) + i7] && (tile = Vars.world.tile(i7, i8)) != null && tile.build == null && !tile.solid() && sameOre(tile, item)) {
                    iArr[(i5 * i2) + i6] = 0;
                    intQueue.addLast((i5 * i2) + i6);
                }
            }
        }
        while (!intQueue.isEmpty()) {
            int removeFirst = intQueue.removeFirst();
            int i9 = removeFirst % i2;
            int i10 = removeFirst / i2;
            int i11 = iArr[removeFirst];
            if (i11 < 60) {
                for (int i12 = 0; i12 < 4; i12++) {
                    int i13 = i9 + DX[i12];
                    int i14 = i10 + DY[i12];
                    if (i13 >= 0 && i14 >= 0 && i13 < i2 && i14 < i2 && iArr[(i14 * i2) + i13] < 0) {
                        iArr[(i14 * i2) + i13] = i11 + 1;
                        intQueue.addLast((i14 * i2) + i13);
                    }
                }
            }
        }
        return iArr;
    }

    private static int oreScore(int[][] iArr, int i, int i2, int i3) {
        int i4 = (i3 * 2) + 1;
        int i5 = i - (cx - i3);
        int i6 = i2 - (cy - i3);
        int i7 = 0;
        for (int i8 = 0; i8 < iArr.length; i8++) {
            if (iArr[i8] != null) {
                int i9 = (i5 < 0 || i6 < 0 || i5 >= i4 || i6 >= i4) ? -1 : iArr[i8][(i6 * i4) + i5];
                i7 += i9 < 0 ? 60 : i9;
            }
        }
        return i7;
    }

    private static boolean overlayOre(Tile tile, Item item) {
        return (tile == null || item == null || tile.overlay() == null || tile.overlay().itemDrop != item) ? false : true;
    }

    /* JADX WARN: Type inference failed for: r0v42, types: [int[], int[][]] */
    private static void startFactoryStep(Unit unit) {
        int[] findOutput;
        String str = factoryQueue[stepIndex];
        stepIndex++;
        nextStepAt = Time.time + 30.0f;
        Schematic schemFor = schemFor(str);
        if (schemFor == null || Vars.player.closestCore() == null) {
            toast(tr("Schematic inválido: ", "Invalid schematic: ") + str);
            return;
        }
        int tileX = Vars.player.closestCore().tileX();
        int tileY = Vars.player.closestCore().tileY();
        int width = Vars.world.width();
        int ceil = (int) Math.ceil(radius);
        String[] feedItems = feedItems(str);
        Item itemFor = feedItems[0] == null ? null : itemFor(feedItems[0]);
        Item itemFor2 = feedItems[1] == null ? null : itemFor(feedItems[1]);
        ?? r0 = new int[2];
        r0[0] = itemFor == null ? null : oreDistGrid(itemFor, ceil);
        r0[1] = itemFor2 == null ? null : oreDistGrid(itemFor2, ceil);
        ArrayList arrayList = new ArrayList();
        for (int i = cy - ceil; i <= cy + ceil; i++) {
            for (int i2 = cx - ceil; i2 <= cx + ceil; i2++) {
                if (Vars.world.tiles.in(i2, i) && inCircle(i2, i)) {
                    arrayList.add(new int[]{i2, i, ((int) (Mathf.dst(i2, i, cx, cy) * 20.0f)) + (oreScore(r0, i2, i, ceil) * 250)});
                }
            }
        }
        Collections.sort(arrayList, new Comparator<int[]>() { // from class: lunarians.AutoSet.2
            @Override // java.util.Comparator
            public int compare(int[] iArr, int[] iArr2) {
                return iArr[2] - iArr2[2];
            }
        });
        boolean[] reservedFromPlans = AutoConnectConveyor.reservedFromPlans(unit);
        for (int i3 = 0; i3 < feedReserved.size(); i3++) {
            reservedFromPlans[feedReserved.get(i3).intValue()] = true;
        }
        int i4 = 0;
        while (i4 < 3) {
            int i5 = 0;
            for (int i6 = 0; i6 < arrayList.size() && i5 < 12; i6++) {
                int[] iArr = (int[]) arrayList.get(i6);
                float f = tileX - iArr[0];
                float f2 = tileY - iArr[1];
                int i7 = ((Math.abs(f) >= Math.abs(f2) ? f >= 0.0f ? 0 : 2 : f2 >= 0.0f ? 1 : 3) - 1) & 3;
                Seq plans = Vars.schematics.toPlans(schemFor, iArr[0], iArr[1]);
                for (int i8 = 0; i8 < i7; i8++) {
                    rotatePlansAround(plans, iArr[0], iArr[1]);
                }
                HashSet hashSet = new HashSet();
                if (planFits(plans, reservedFromPlans, hashSet) && (findOutput = findOutput(plans, hashSet)) != null) {
                    int min = (Vars.player.closestCore().block.size / 2) + (i4 >= 2 ? Math.min(AutoSetConfig.coreGap(), 2) : AutoSetConfig.coreGap());
                    boolean z = false;
                    Iterator it = hashSet.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        Integer num = (Integer) it.next();
                        if (Math.max(Math.abs((num.intValue() % width) - tileX), Math.abs((num.intValue() / width) - tileY)) <= min) {
                            z = true;
                            break;
                        }
                    }
                    if (z) {
                        continue;
                    } else {
                        boolean z2 = false;
                        Iterator it2 = hashSet.iterator();
                        while (it2.hasNext()) {
                            Integer num2 = (Integer) it2.next();
                            int intValue = num2.intValue() % width;
                            int intValue2 = num2.intValue() / width;
                            for (int i9 = -1; i9 <= 1; i9++) {
                                for (int i10 = -1; i10 <= 1; i10++) {
                                    Tile tile = Vars.world.tile(intValue + i10, intValue2 + i9);
                                    if (overlayOre(tile, itemFor) || overlayOre(tile, itemFor2)) {
                                        z2 = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (!z2 || i4 >= 2) {
                            int facGap = i4 == 0 ? AutoSetConfig.facGap() : i4 == 1 ? Math.min(AutoSetConfig.facGap(), 1) : 0;
                            int wallGap = i4 == 0 ? AutoSetConfig.wallGap() : i4 == 1 ? Math.min(AutoSetConfig.wallGap(), 1) : 0;
                            int max = Math.max(facGap, wallGap);
                            boolean z3 = false;
                            Iterator it3 = hashSet.iterator();
                            while (it3.hasNext()) {
                                Integer num3 = (Integer) it3.next();
                                int intValue3 = num3.intValue() % width;
                                int intValue4 = num3.intValue() / width;
                                for (int i11 = -max; i11 <= max; i11++) {
                                    for (int i12 = -max; i12 <= max; i12++) {
                                        int i13 = intValue3 + i12;
                                        int i14 = intValue4 + i11;
                                        if (Vars.world.tiles.in(i13, i14) && !hashSet.contains(Integer.valueOf((i14 * width) + i13))) {
                                            Tile tile2 = Vars.world.tile(i13, i14);
                                            boolean z4 = Math.abs(i12) <= wallGap && Math.abs(i11) <= wallGap;
                                            boolean z5 = Math.abs(i12) <= facGap && Math.abs(i11) <= facGap;
                                            if ((z4 && tile2 != null && tile2.build == null && tile2.solid()) || (z5 && (reservedFromPlans[(i14 * width) + i13] || (tile2 != null && tile2.build != null && !(tile2.build.block instanceof CoreBlock))))) {
                                                z3 = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            if (z3) {
                                continue;
                            } else {
                                i5++;
                                boolean[] zArr = (boolean[]) reservedFromPlans.clone();
                                for (int i15 = 0; i15 < plans.size; i15++) {
                                    AutoConnectConveyor.markPlan((BuildPlan) plans.get(i15), zArr);
                                }
                                Seq<BuildPlan> planRoute = AutoConnectConveyor.planRoute(findOutput[0], findOutput[1], findOutput[2], zArr);
                                if (planRoute == null || planRoute.isEmpty()) {
                                    planRoute = AutoConnectConveyor.planRoute(findOutput[0], findOutput[1], -1, zArr);
                                }
                                if (planRoute != null && !planRoute.isEmpty()) {
                                    if (AutoSetConfig.facDrill()) {
                                        String[] feedItems2 = feedItems(str);
                                        int i16 = 0;
                                        for (int i17 = 0; i17 < plans.size && i16 < 2; i17++) {
                                            BuildPlan buildPlan = (Position) plans.get(i17);
                                            if (buildPlan.block != null && buildPlan.block.name != null && buildPlan.block.name.contains("conveyor")) {
                                                Tiles tiles = Vars.world.tiles;
                                                int i18 = buildPlan.x;
                                                int[] iArr2 = DX;
                                                int i19 = buildPlan.rotation & 3;
                                                int i20 = i18 - iArr2[i19];
                                                int i21 = buildPlan.y - DY[i19];
                                                if (tiles.in(i20, i21) && !hashSet.contains(Integer.valueOf((i21 * width) + i20))) {
                                                    String str2 = feedItems2[i16];
                                                    i16++;
                                                    if (str2 != null) {
                                                        feedTargets.add(new int[]{i20, i21, i19});
                                                        feedItemNames.add(str2);
                                                        for (int i22 = 0; i22 < 3; i22++) {
                                                            int i23 = i20 - (i22 * DX[i19]);
                                                            int i24 = i21 - (i22 * DY[i19]);
                                                            if (Vars.world.tiles.in(i23, i24)) {
                                                                feedReserved.add(Integer.valueOf((i24 * width) + i23));
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Seq seq = new Seq();
                                    Iterator it4 = unit.plans().iterator();
                                    while (it4.hasNext()) {
                                        seq.add((BuildPlan) it4.next());
                                    }
                                    for (int i25 = 0; i25 < plans.size; i25++) {
                                        unit.addBuild((BuildPlan) plans.get(i25), true);
                                    }
                                    for (int i26 = 0; i26 < planRoute.size; i26++) {
                                        unit.addBuild((BuildPlan) planRoute.get(i26), true);
                                    }
                                    pending.clear();
                                    Iterator it5 = unit.plans().iterator();
                                    while (it5.hasNext()) {
                                        BuildPlan buildPlan2 = (BuildPlan) it5.next();
                                        if (!seq.contains(buildPlan2, true)) {
                                            pending.add(buildPlan2);
                                        }
                                    }
                                    lastPending = pending.size;
                                    lastProgress = Time.time;
                                    toast(tr("Fábrica ", "Factory ") + str + ": " + plans.size + "+" + planRoute.size + tr(" blocos planejados", " blocks planned"));
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            i4++;
        }
        toast(tr("Sem espaço/rota para a fábrica: ", "No space/route for factory: ") + str);
    }

    private static void checkPending(Unit unit) {
        if (Time.time < nextCheck) {
            return;
        }
        nextCheck = Time.time + 15.0f;
        rememberOurs();
        for (int i = pending.size - 1; i >= 0; i--) {
            BuildPlan buildPlan = (BuildPlan) pending.get(i);
            Tile tile = Vars.world.tile(buildPlan.x, buildPlan.y);
            if (tile == null || tile.block() == buildPlan.block) {
                pending.remove(i);
            }
        }
        if (pending.isEmpty()) {
            nextStepAt = Time.time + 15.0f;
            return;
        }
        if (pending.size < lastPending) {
            lastProgress = Time.time;
        }
        lastPending = pending.size;
        for (int i2 = pending.size - 1; i2 >= 0; i2--) {
            BuildPlan buildPlan2 = (BuildPlan) pending.get(i2);
            if (!unit.plans().contains(buildPlan2, true)) {
                if (Build.validPlace(buildPlan2.block, Vars.player.team(), buildPlan2.x, buildPlan2.y, buildPlan2.rotation)) {
                    unit.addBuild(buildPlan2, true);
                } else if (!tryDelete(unit, buildPlan2)) {
                    pending.remove(i2);
                }
            }
        }
        if (pending.isEmpty()) {
            nextStepAt = Time.time + 30.0f;
            return;
        }
        if (Time.time - lastProgress <= 360.0f) {
            if (Time.time - lastProgress > 180.0f && Time.time - lastWarn > 180.0f) {
                lastWarn = Time.time;
                BuildPlan nearestPending = nearestPending(unit);
                if (nearestPending != null && !unit.within(nearestPending.drawx(), nearestPending.drawy(), 220.0f)) {
                    toast(LunariansI18n.get("as_approach", nearestPending.x + "," + nearestPending.y));
                    return;
                } else {
                    toast(LunariansI18n.get("as_stalled"));
                    return;
                }
            }
            return;
        }
        String missingResource = missingResource();
        if (missingResource == null || Time.time - lastProgress >= 5400.0f) {
            int i3 = pending.size;
            pending.clear();
            nextStepAt = Time.time + 20.0f;
            toast(LunariansI18n.get("as_stalled") + " → skip (" + i3 + ")");
            return;
        }
        if (Time.time - lastWarn > 300.0f) {
            lastWarn = Time.time;
            toast(tr("Aguardando recurso: ", "Waiting for resource: ") + missingResource);
        }
    }

    private static void rememberOurs() {
        int width = Vars.world.width();
        for (int i = 0; i < pending.size; i++) {
            BuildPlan buildPlan = (BuildPlan) pending.get(i);
            if (buildPlan != null && buildPlan.block != null && !buildPlan.breaking && seenPlans.add(buildPlan)) {
                int i2 = (buildPlan.block.size - 1) / 2;
                for (int i3 = 0; i3 < buildPlan.block.size; i3++) {
                    for (int i4 = 0; i4 < buildPlan.block.size; i4++) {
                        ours.merge(Integer.valueOf((((((buildPlan.y - i2) + i3) * width) + buildPlan.x) - i2) + i4), 1, (v0, v1) -> {
                            return Integer.sum(v0, v1);
                        });
                    }
                }
            }
        }
    }

    private static boolean deletable(Block block) {
        return (block instanceof Conveyor) || (block instanceof Junction) || (block instanceof ItemBridge) || (block instanceof Router) || (block instanceof Sorter) || (block instanceof OverflowGate) || (block instanceof Drill) || (block instanceof PowerNode) || (block instanceof Conduit);
    }

    private static boolean tryDelete(Unit unit, BuildPlan buildPlan) {
        Integer num;
        try {
            if (!AutoSetConfig.autoDelete() || deleteCount >= 30 || buildPlan.block == null || buildPlan.breaking) {
                return false;
            }
            int width = Vars.world.width();
            int i = (buildPlan.block.size - 1) / 2;
            boolean z = false;
            for (int i2 = 0; i2 < buildPlan.block.size; i2++) {
                for (int i3 = 0; i3 < buildPlan.block.size; i3++) {
                    int i4 = (buildPlan.x - i) + i3;
                    int i5 = (buildPlan.y - i) + i2;
                    Tile tile = Vars.world.tile(i4, i5);
                    if (tile != null && tile.build != null && tile.build.team == Vars.player.team() && tile.build.block != buildPlan.block) {
                        Block block = tile.build.block;
                        if (!(block instanceof ConstructBlock) && !(block instanceof CoreBlock) && deletable(block) && (num = ours.get(Integer.valueOf((i5 * width) + i4))) != null && num.intValue() >= 2 && !deleted.contains(Integer.valueOf(tile.build.pos()))) {
                            deleted.add(Integer.valueOf(tile.build.pos()));
                            deleteCount++;
                            unit.addBuild(new BuildPlan(tile.build.tileX(), tile.build.tileY()), true);
                            z = true;
                        }
                    }
                }
            }
            if (z) {
                toast(tr("Auto delete: removendo bloco que atrapalha (" + deleteCount + "/30)", "Auto delete: removing blocking block (" + deleteCount + "/30)"));
            }
            return z;
        } catch (Throwable th) {
            return false;
        }
    }

    private static String missingResource() {
        Tile tile;
        try {
            if (Vars.state.rules.infiniteResources || Vars.player == null || Vars.player.closestCore() == null) {
                return null;
            }
            float f = Vars.state.rules.buildCostMultiplier;
            for (int i = 0; i < pending.size; i++) {
                BuildPlan buildPlan = (BuildPlan) pending.get(i);
                if (buildPlan != null && buildPlan.block != null && !buildPlan.breaking && ((tile = Vars.world.tile(buildPlan.x, buildPlan.y)) == null || tile.block() != buildPlan.block)) {
                    for (ItemStack itemStack : buildPlan.block.requirements) {
                        if (Vars.player.closestCore().items.get(itemStack.item) < Math.ceil(itemStack.amount * f)) {
                            return itemStack.item.localizedName;
                        }
                    }
                }
            }
            return null;
        } catch (Throwable th) {
            return null;
        }
    }

    private static BuildPlan nearestPending(Unit unit) {
        BuildPlan buildPlan = null;
        float f = Float.MAX_VALUE;
        for (int i = 0; i < pending.size; i++) {
            BuildPlan buildPlan2 = (BuildPlan) pending.get(i);
            float dst = Mathf.dst(unit.x, unit.y, buildPlan2.drawx(), buildPlan2.drawy());
            if (dst < f) {
                f = dst;
                buildPlan = buildPlan2;
            }
        }
        return buildPlan;
    }

    private static void setExitTowardCore(int i, int i2) {
        try {
            if (Vars.player == null || Vars.player.closestCore() == null) {
                return;
            }
            float tileX = Vars.player.closestCore().tileX() - i;
            float tileY = Vars.player.closestCore().tileY() - i2;
            LunariansSettings.drillExitDir = Math.abs(tileX) >= Math.abs(tileY) ? tileX >= 0.0f ? 0 : 2 : tileY >= 0.0f ? 1 : 3;
        } catch (Throwable th) {
        }
    }

    private static boolean inCircle(int i, int i2) {
        return Mathf.dst((float) i, (float) i2, (float) cx, (float) cy) <= radius;
    }

    private static int[] findOre(Item item) {
        return findOre(item, cx, cy);
    }

    private static int[] findMarked(Item item, int i, int i2) {
        int i3;
        int i4;
        Tile tile;
        int[] markedTiles = AutoEconomy.markedTiles(item);
        if (markedTiles.length == 0 || used == null) {
            return null;
        }
        int width = Vars.world.width();
        int i5 = -1;
        int i6 = -1;
        int i7 = Integer.MIN_VALUE;
        for (int i8 : markedTiles) {
            if (i8 >= 0 && i8 < used.length && !used[i8] && (tile = Vars.world.tile((i3 = i8 % width), (i4 = i8 / width))) != null && tile.build == null && !tile.solid() && sameOre(tile, item)) {
                int dst = 1000 - ((int) Mathf.dst(i3, i4, i, i2));
                for (int i9 = 0; i9 < 8; i9++) {
                    int i10 = i3 + DX[i9];
                    int i11 = i4 + DY[i9];
                    if (Vars.world.tiles.in(i10, i11) && !used[(i11 * width) + i10] && sameOre(Vars.world.tile(i10, i11), item)) {
                        dst += 5;
                    }
                }
                if (dst > i7) {
                    i7 = dst;
                    i5 = i3;
                    i6 = i4;
                }
            }
        }
        if (i5 < 0) {
            return null;
        }
        return new int[]{i5, i6};
    }

    private static int[] findOre(Item item, int i, int i2) {
        int[] findMarked = findMarked(item, i, i2);
        if (findMarked != null) {
            AutoDrill.isoOverride = 1;
            return findMarked;
        }
        AutoDrill.isoOverride = -1;
        int[] findOreM = findOreM(item, i, i2, AutoSetConfig.spacing() + 1);
        if (findOreM == null) {
            int[] findOreM2 = findOreM(item, i, i2, 1);
            findOreM = findOreM2;
            if (findOreM2 != null) {
                AutoDrill.isoOverride = 1;
            }
        }
        return findOreM;
    }

    private static int[] findOreM(Item item, int i, int i2, int i3) {
        Tile tile;
        int width = Vars.world.width();
        int ceil = (int) Math.ceil(radius);
        int i4 = -1;
        int i5 = -1;
        int i6 = Integer.MIN_VALUE;
        for (int i7 = cy - ceil; i7 <= cy + ceil; i7++) {
            for (int i8 = cx - ceil; i8 <= cx + ceil; i8++) {
                if (Vars.world.tiles.in(i8, i7) && inCircle(i8, i7) && !used[(i7 * width) + i8] && (tile = Vars.world.tile(i8, i7)) != null && tile.build == null && !tile.solid() && sameOre(tile, item) && !AutoDrill.crowded(i8, i7, 1, i3)) {
                    int dst = 1000 - ((int) Mathf.dst(i8, i7, i, i2));
                    for (int i9 = 0; i9 < 8; i9++) {
                        int i10 = i8 + DX[i9];
                        int i11 = i7 + DY[i9];
                        if (Vars.world.tiles.in(i10, i11) && !used[(i11 * width) + i10] && sameOre(Vars.world.tile(i10, i11), item)) {
                            dst += 5;
                        }
                    }
                    if (dst > i6) {
                        i6 = dst;
                        i4 = i8;
                        i5 = i7;
                    }
                }
            }
        }
        if (i4 < 0) {
            return null;
        }
        return new int[]{i4, i5};
    }

    private static void markUsed(int i, int i2, Item item) {
        int width = Vars.world.width();
        int max = Math.max(16, LunariansSettings.drillMaxTiles);
        IntQueue intQueue = new IntQueue();
        boolean[] zArr = new boolean[width * Vars.world.height()];
        int i3 = (i2 * width) + i;
        intQueue.addLast(i3);
        zArr[i3] = true;
        int i4 = 0;
        while (!intQueue.isEmpty() && i4 < max) {
            int removeFirst = intQueue.removeFirst();
            used[removeFirst] = true;
            i4++;
            int i5 = removeFirst % width;
            int i6 = removeFirst / width;
            for (int i7 = 0; i7 < 4; i7++) {
                int i8 = i5 + DX[i7];
                int i9 = i6 + DY[i7];
                if (Vars.world.tiles.in(i8, i9)) {
                    int i10 = (i9 * width) + i8;
                    if (!zArr[i10] && inCircle(i8, i9) && sameOre(Vars.world.tile(i8, i9), item) && AutoDrill.inFloor(i8, i9, i, i2)) {
                        zArr[i10] = true;
                        intQueue.addLast(i10);
                    }
                }
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
        } catch (Throwable th) {
        }
        Floor overlay = tile.overlay() != null ? tile.overlay() : tile.floor();
        return overlay != null && overlay.itemDrop == item;
    }

    private static void draw() {
        if ((phase != 2 && !hasArea) || Vars.state == null || !Vars.state.isGame() || cx == Integer.MIN_VALUE) {
            return;
        }
        try {
            float f = cx * 8.0f;
            float f2 = cy * 8.0f;
            if (phase != 2) {
                float f3 = radius * 8.0f;
                Draw.color(Color.orange, 0.12f);
                Fill.circle(f, f2, f3);
                Draw.color(Color.orange, 0.85f);
                Lines.stroke(2.0f);
                Lines.circle(f, f2, f3);
                Draw.color(Color.yellow, 0.9f);
                Lines.stroke(1.0f);
                for (int i = 0; i < pending.size; i++) {
                    BuildPlan buildPlan = (BuildPlan) pending.get(i);
                    Lines.square(buildPlan.drawx(), buildPlan.drawy(), 4.0f);
                }
                Draw.reset();
                return;
            }
            if (!centerChosen) {
                return;
            }
            Vec2 mouseWorld = Core.input.mouseWorld();
            float min = Math.min(Mathf.dst(f, f2, mouseWorld.x, mouseWorld.y), 80.0f * 8.0f);
            Draw.color(Color.cyan, 0.15f);
            Fill.circle(f, f2, min);
            Draw.color(Color.cyan, 0.9f);
            Lines.stroke(2.0f);
            Lines.circle(f, f2, min);
            Draw.reset();
        } catch (Throwable th) {
        }
    }

    private static void toast(String str) {
        if (Vars.ui != null) {
            Vars.ui.showInfoToast(str, 3.0f);
        }
    }
}
