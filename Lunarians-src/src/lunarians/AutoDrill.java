package lunarians;

import arc.math.Mathf;
import arc.struct.IntQueue;
import arc.struct.IntSeq;
import arc.struct.Seq;
import arc.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.storage.Unloader;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoDrill.class */
public class AutoDrill {
    private static boolean errorLogged;
    public static int lastCount;
    public static boolean dryRun;
    public static boolean quiet;
    public static boolean limitOn;
    public static float limitX;
    public static float limitY;
    public static float limitR;
    private static boolean failedNoneFit;
    private static final int[] DX = {1, 0, -1, 0};
    private static final int[] DY = {0, 1, 0, -1};
    public static int isoOverride = -1;
    public static final Seq<BuildPlan> lastPlans = new Seq<>();
    public static final IntSeq lastDrillTiles = new IntSeq();
    public static int lastHub = -1;

    static boolean inLimit(int i, int i2) {
        return !limitOn || Mathf.dst((float) i, (float) i2, limitX, limitY) <= limitR;
    }

    static boolean crowded(int i, int i2, int i3, int i4) {
        Tile tile;
        for (int i5 = i2 - i4; i5 <= ((i2 + i3) - 1) + i4; i5++) {
            for (int i6 = i - i4; i6 <= ((i + i3) - 1) + i4; i6++) {
                if (Vars.world.tiles.in(i6, i5) && (tile = Vars.world.tile(i6, i5)) != null && tile.build != null && tile.build.block != null && !(tile.build.block instanceof CoreBlock)) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean nextToDrill(int i, int i2) {
        for (int i3 = 0; i3 < 4; i3++) {
            Tile tile = Vars.world.tile(i + DX[i3], i2 + DY[i3]);
            if (tile != null && tile.build != null && (tile.build.block instanceof Drill)) {
                return true;
            }
        }
        return false;
    }

    static boolean floorOre(Tile tile) {
        if (tile == null) {
            return false;
        }
        return tile.overlay() == null || tile.overlay().itemDrop == null;
    }

    static boolean inFloor(int i, int i2, int i3, int i4) {
        return (limitOn && floorOre(Vars.world.tile(i3, i4)) && Mathf.dst((float) i, (float) i2, (float) i3, (float) i4) > Math.max(2.5f, (((float) Math.sqrt((double) AutoSetConfig.floorMax())) * 0.75f) + 0.5f)) ? false : true;
    }

    public static void init() {
    }

    public static void reset() {
        lastDrillTiles.clear();
        lastCount = 0;
        lastHub = -1;
    }

    public static int count() {
        return lastCount;
    }

    public static int generate() {
        return run();
    }

    private static int[] exitOrder(int i) {
        int[] iArr = new int[4];
        iArr[0] = i;
        float f = 0.0f;
        float f2 = 0.0f;
        try {
            if (Vars.player != null && Vars.player.closestCore() != null && LunariansSettings.drillStartX != Integer.MIN_VALUE) {
                f = Vars.player.closestCore().tileX() - LunariansSettings.drillStartX;
                f2 = Vars.player.closestCore().tileY() - LunariansSettings.drillStartY;
            }
        } catch (Throwable th) {
        }
        boolean[] zArr = new boolean[4];
        zArr[i] = true;
        for (int i2 = 1; i2 < 4; i2++) {
            int i3 = -1;
            float f3 = -3.4028235E38f;
            for (int i4 = 0; i4 < 4; i4++) {
                if (!zArr[i4]) {
                    float f4 = (DX[i4] * f) + (DY[i4] * f2);
                    if (f4 > f3) {
                        f3 = f4;
                        i3 = i4;
                    }
                }
            }
            zArr[i3] = true;
            iArr[i2] = i3;
        }
        return iArr;
    }

    /* JADX WARN: Finally extract failed */
    public static int generateWithConveyor() {
        Seq<BuildPlan> planRoute;
        if (Vars.headless || Vars.player == null || Vars.state == null || !Vars.state.isGame()) {
            return 0;
        }
        int i = LunariansSettings.drillExitDir & 3;
        try {
            try {
                Unit unit = Vars.player.unit();
                if (unit == null || !unit.canBuild()) {
                    toast(LunariansI18n.get("cv_need_builder"));
                    LunariansSettings.drillExitDir = i;
                    dryRun = false;
                    return 0;
                }
                boolean[] reservedFromPlans = AutoConnectConveyor.reservedFromPlans(unit);
                int[] exitOrder = exitOrder(i);
                int width = Vars.world.width();
                for (int i2 : exitOrder) {
                    LunariansSettings.drillExitDir = i2;
                    dryRun = true;
                    try {
                        int generateInner = generateInner();
                        dryRun = false;
                        if (generateInner <= 0) {
                            LunariansSettings.drillExitDir = i;
                            dryRun = false;
                            return 0;
                        }
                        if (lastHub >= 0) {
                            boolean[] zArr = (boolean[]) reservedFromPlans.clone();
                            for (int i3 = 0; i3 < lastPlans.size; i3++) {
                                AutoConnectConveyor.markPlan((BuildPlan) lastPlans.get(i3), zArr);
                            }
                            int i4 = (lastHub % width) + DX[i2];
                            int i5 = (lastHub / width) + DY[i2];
                            if (Vars.world.tiles.in(i4, i5) && (planRoute = AutoConnectConveyor.planRoute(i4, i5, i2, zArr)) != null) {
                                for (int i6 = 0; i6 < lastPlans.size; i6++) {
                                    unit.addBuild((BuildPlan) lastPlans.get(i6), true);
                                }
                                for (int i7 = 0; i7 < planRoute.size; i7++) {
                                    unit.addBuild((BuildPlan) planRoute.get(i7), true);
                                }
                                if (!quiet) {
                                    toast(LunariansI18n.get("dr_summary", String.valueOf(generateInner), String.valueOf(planRoute.size)));
                                }
                                LunariansSettings.drillExitDir = i;
                                dryRun = false;
                                return generateInner;
                            }
                        }
                    } catch (Throwable th) {
                        dryRun = false;
                        throw th;
                    }
                }
                if (!quiet) {
                    toast(LunariansI18n.get("cv_no_path"));
                }
                LunariansSettings.drillExitDir = i;
                dryRun = false;
                return 0;
            } catch (Throwable th2) {
                if (!errorLogged) {
                    errorLogged = true;
                    Log.err("[Lunarians] AutoDrill+Conveyor error", th2);
                }
                LunariansSettings.drillExitDir = i;
                dryRun = false;
                return 0;
            }
        } catch (Throwable th3) {
            LunariansSettings.drillExitDir = i;
            dryRun = false;
            throw th3;
        }
    }

    private static int run() {
        if (Vars.headless || Vars.player == null || Vars.state == null || !Vars.state.isGame()) {
            return 0;
        }
        try {
            return generateInner();
        } catch (Throwable th) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err("[Lunarians] AutoDrill error", th);
                return 0;
            }
            return 0;
        }
    }

    private static Block resolveDrill() {
        String str = LunariansSettings.drillBlock;
        if (str == null || str.isEmpty()) {
            str = "mechanical-drill";
        }
        Block block = Vars.content.block(str);
        if (block instanceof Drill) {
            return block;
        }
        return Blocks.mechanicalDrill;
    }

    private static Block resolveConveyor() {
        String str = LunariansSettings.conveyorBlock;
        if (str == null || str.isEmpty()) {
            str = "conveyor";
        }
        Block block = Vars.content.block(str);
        if (block instanceof Conveyor) {
            return block;
        }
        return Blocks.conveyor;
    }

    private static Item oreAt(Tile tile) {
        if (tile == null) {
            return null;
        }
        try {
            if (tile.drop() != null) {
                return tile.drop();
            }
        } catch (Throwable th) {
        }
        Floor overlay = tile.overlay() != null ? tile.overlay() : tile.floor();
        if (overlay != null && overlay.itemDrop != null) {
            return overlay.itemDrop;
        }
        return null;
    }

    private static boolean sameOre(Tile tile, Item item) {
        Item oreAt = oreAt(tile);
        return oreAt != null && oreAt == item;
    }

    private static boolean free(int i, int i2, boolean[] zArr, int i3, int i4) {
        Tile tile;
        if (!Vars.world.tiles.in(i, i2) || zArr[(i2 * i3) + i] || (tile = Vars.world.tile(i, i2)) == null || tile.solid()) {
            return false;
        }
        return tile.build == null || tile.build.block == null || !tile.build.block.solid;
    }

    private static boolean freeConv(int i, int i2, boolean[] zArr, int i3, int i4) {
        Tile tile;
        if (!Vars.world.tiles.in(i, i2) || zArr[(i2 * i3) + i] || (tile = Vars.world.tile(i, i2)) == null) {
            return false;
        }
        if (tile.build != null && tile.build.block != null && tile.build.block.solid) {
            return false;
        }
        if (!limitOn || (tile.build == null && !nextToDrill(i, i2))) {
            return (tile.block() != null && tile.block().solid && tile.block().isStatic()) ? false : true;
        }
        return false;
    }

    private static int generateInner() {
        failedNoneFit = false;
        int generateInner2 = generateInner2(true);
        if (generateInner2 <= 0 && failedNoneFit) {
            failedNoneFit = false;
            generateInner2 = generateInner2(false);
        }
        return generateInner2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static int generateInner2(boolean z) {
        int[] iArr;
        lastHub = -1;
        int i = LunariansSettings.drillStartX;
        int i2 = LunariansSettings.drillStartY;
        if (i == Integer.MIN_VALUE || i2 == Integer.MIN_VALUE) {
            toast(LunariansI18n.get("dr_set_ore"));
            return 0;
        }
        Tile tile = Vars.world.tile(i, i2);
        Item oreAt = oreAt(tile);
        if (oreAt == null) {
            toast(LunariansI18n.get("dr_no_ore"));
            return 0;
        }
        Drill resolveDrillFor = resolveDrillFor(oreAt);
        Block resolveConveyor = resolveConveyor();
        if (!(resolveDrillFor instanceof Drill) || resolveConveyor == null) {
            toast(LunariansI18n.get("dr_invalid"));
            return 0;
        }
        if (resolveDrillFor.tier < oreAt.hardness) {
            toast(pt() ? "Nenhuma drill disponível consegue minerar " + oreAt.localizedName : "No available drill can mine " + oreAt.localizedName);
            return 0;
        }
        int i3 = ((Block) resolveDrillFor).size;
        int max = Math.max(16, LunariansSettings.drillMaxTiles);
        if (limitOn && floorOre(tile)) {
            max = Math.min(max, AutoSetConfig.floorMax());
        }
        int max2 = Math.max(1, LunariansSettings.drillMinOres);
        int i4 = max2;
        int i5 = ((i3 * i3) + 1) / 2;
        boolean z2 = false;
        if (limitOn && z && i5 > max2) {
            i4 = i5;
            z2 = true;
        }
        int i6 = LunariansSettings.drillExitDir & 3;
        boolean z3 = LunariansSettings.drillLayoutMode == 1;
        Unit unit = Vars.player.unit();
        if (unit == null || !unit.canBuild()) {
            toast(LunariansI18n.get("cv_need_builder"));
            return 0;
        }
        int width = Vars.world.width();
        int height = Vars.world.height();
        boolean[] zArr = new boolean[width * height];
        IntSeq intSeq = new IntSeq();
        IntQueue intQueue = new IntQueue();
        int i7 = (i2 * width) + i;
        intQueue.addLast(i7);
        zArr[i7] = true;
        while (!intQueue.isEmpty() && intSeq.size < max) {
            int removeFirst = intQueue.removeFirst();
            intSeq.add(removeFirst);
            int i8 = removeFirst % width;
            int i9 = removeFirst / width;
            for (int i10 = 0; i10 < 4; i10++) {
                int i11 = i8 + DX[i10];
                int i12 = i9 + DY[i10];
                if (Vars.world.tiles.in(i11, i12)) {
                    int i13 = (i12 * width) + i11;
                    if (!zArr[i13] && sameOre(Vars.world.tile(i11, i12), oreAt) && inLimit(i11, i12) && inFloor(i11, i12, i, i2)) {
                        zArr[i13] = true;
                        intQueue.addLast(i13);
                    }
                }
            }
        }
        if (intSeq.isEmpty()) {
            toast(LunariansI18n.get("dr_empty"));
            return 0;
        }
        int i14 = Integer.MAX_VALUE;
        int i15 = Integer.MAX_VALUE;
        int i16 = 0;
        int i17 = 0;
        for (int i18 = 0; i18 < intSeq.size; i18++) {
            int i19 = intSeq.get(i18);
            int i20 = i19 % width;
            int i21 = i19 / width;
            i14 = Math.min(i14, i20);
            i15 = Math.min(i15, i21);
            i16 = Math.max(i16, i20);
            i17 = Math.max(i17, i21);
        }
        boolean[] reservedFromPlans = limitOn ? AutoConnectConveyor.reservedFromPlans(unit) : new boolean[width * height];
        boolean[] zArr2 = new boolean[width * height];
        Seq seq = new Seq();
        lastDrillTiles.clear();
        IntSeq intSeq2 = new IntSeq();
        boolean z4 = i6 == 1 || i6 == 3;
        int i22 = 0;
        int i23 = 0;
        if (LunariansSettings.drillPerfect) {
            int i24 = -1;
            int i25 = Integer.MAX_VALUE;
            for (int i26 = 0; i26 <= i3; i26++) {
                for (int i27 = 0; i27 <= i3; i27++) {
                    int[] scanGrid = scanGrid(z4, z3, i26, i27, i3, i14, i15, i16, i17, i4, oreAt, zArr, (boolean[]) reservedFromPlans.clone(), (boolean[]) zArr2.clone(), width, height, resolveDrillFor, new Seq(), new IntSeq(), new IntSeq());
                    if (scanGrid[0] != 0 && (scanGrid[1] > i24 || (scanGrid[1] == i24 && scanGrid[0] < i25))) {
                        i24 = scanGrid[1];
                        i25 = scanGrid[0];
                        i22 = i26;
                        i23 = i27;
                    }
                }
            }
        }
        int i28 = scanGrid(z4, z3, i22, i23, i3, i14, i15, i16, i17, i4, oreAt, zArr, reservedFromPlans, zArr2, width, height, resolveDrillFor, seq, lastDrillTiles, intSeq2)[0];
        if (LunariansSettings.drillPerfect && i28 > 0) {
            i28 += fillGaps(z4, i3, i4, oreAt, zArr, reservedFromPlans, zArr2, width, height, resolveDrillFor, seq, lastDrillTiles, intSeq2);
        }
        if (i28 == 0) {
            if (z2) {
                failedNoneFit = true;
                return 0;
            }
            toast(LunariansI18n.get("dr_none_fit"));
            return 0;
        }
        int i29 = Integer.MAX_VALUE;
        int i30 = Integer.MAX_VALUE;
        int i31 = 0;
        int i32 = 0;
        for (int i33 = 0; i33 < lastDrillTiles.size; i33++) {
            int i34 = lastDrillTiles.get(i33);
            int i35 = i34 % width;
            int i36 = i34 / width;
            i29 = Math.min(i29, i35);
            i30 = Math.min(i30, i36);
            i31 = Math.max(i31, (i35 + i3) - 1);
            i32 = Math.max(i32, (i36 + i3) - 1);
        }
        int i37 = 0;
        if (i6 == 1 || i6 == 3) {
            int i38 = i29 - 1;
            int i39 = i31 + 1;
            if (i38 >= 0 && !intSeq2.contains(i38)) {
                intSeq2.add(i38);
            }
            if (i39 < width && !intSeq2.contains(i39)) {
                intSeq2.add(i39);
            }
        } else {
            int i40 = i30 - 1;
            int i41 = i32 + 1;
            if (i40 >= 0 && !intSeq2.contains(i40)) {
                intSeq2.add(i40);
            }
            if (i41 < height && !intSeq2.contains(i41)) {
                intSeq2.add(i41);
            }
        }
        if (i6 == 1 || i6 == 3) {
            for (int i42 = 0; i42 < intSeq2.size; i42++) {
                int i43 = intSeq2.get(i42);
                for (int i44 = i30; i44 <= i32; i44++) {
                    if (freeConv(i43, i44, reservedFromPlans, width, height)) {
                        seq.add(new BuildPlan(i43, i44, i6, resolveConveyor));
                        reservedFromPlans[(i44 * width) + i43] = true;
                        i37++;
                    }
                }
            }
        } else {
            for (int i45 = 0; i45 < intSeq2.size; i45++) {
                int i46 = intSeq2.get(i45);
                for (int i47 = i29; i47 <= i31; i47++) {
                    if (freeConv(i47, i46, reservedFromPlans, width, height)) {
                        seq.add(new BuildPlan(i47, i46, i6, resolveConveyor));
                        reservedFromPlans[(i46 * width) + i47] = true;
                        i37++;
                    }
                }
            }
        }
        for (int i48 = 0; i48 < lastDrillTiles.size; i48++) {
            int i49 = lastDrillTiles.get(i48);
            int[] pickFace = pickFace(i49 % width, i49 / width, i3, i6, intSeq2, reservedFromPlans, zArr2, width, height);
            if (pickFace != null) {
                int i50 = pickFace[0];
                int i51 = pickFace[1];
                int i52 = pickFace[2];
                if (freeConv(i50, i51, reservedFromPlans, width, height)) {
                    seq.add(new BuildPlan(i50, i51, i52, resolveConveyor));
                    reservedFromPlans[(i51 * width) + i50] = true;
                    i37++;
                }
            }
        }
        for (int i53 = 0; i53 < lastDrillTiles.size; i53++) {
            int i54 = lastDrillTiles.get(i53);
            int i55 = i54 % width;
            int i56 = i54 / width;
            if (!hasAdjConv(i55, i56, i3, reservedFromPlans, width)) {
                int[] iArr2 = new int[1];
                if (i6 == 0) {
                    iArr = new int[]{i55 + i3, i56, 0};
                } else if (i6 == 1) {
                    iArr = new int[]{i55, i56 + i3, 1};
                } else if (i6 == 2) {
                    iArr = new int[]{i55 - 1, i56, 2};
                } else {
                    iArr = new int[]{i55, i56 - 1, 3};
                }
                iArr2[0] = iArr;
                IntSeq intSeq3 = new IntSeq();
                IntSeq intSeq4 = new IntSeq();
                IntSeq intSeq5 = new IntSeq();
                for (int i57 = 0; i57 < i3; i57++) {
                    if (i6 == 0) {
                        intSeq3.add(i55 + i3);
                        intSeq4.add(i56 + i57);
                        intSeq5.add(0);
                    } else if (i6 == 1) {
                        intSeq3.add(i55 + i57);
                        intSeq4.add(i56 + i3);
                        intSeq5.add(1);
                    } else if (i6 == 2) {
                        intSeq3.add(i55 - 1);
                        intSeq4.add(i56 + i57);
                        intSeq5.add(2);
                    } else {
                        intSeq3.add(i55 + i57);
                        intSeq4.add(i56 - 1);
                        intSeq5.add(3);
                    }
                }
                for (int i58 = 0; i58 < i3; i58++) {
                    intSeq3.add(i55 + i3);
                    intSeq4.add(i56 + i58);
                    intSeq5.add(0);
                    intSeq3.add(i55 - 1);
                    intSeq4.add(i56 + i58);
                    intSeq5.add(2);
                    intSeq3.add(i55 + i58);
                    intSeq4.add(i56 + i3);
                    intSeq5.add(1);
                    intSeq3.add(i55 + i58);
                    intSeq4.add(i56 - 1);
                    intSeq5.add(3);
                }
                int i59 = 0;
                while (true) {
                    if (i59 < intSeq3.size) {
                        int i60 = intSeq3.get(i59);
                        int i61 = intSeq4.get(i59);
                        int i62 = intSeq5.get(i59);
                        if (!freeConv(i60, i61, reservedFromPlans, width, height)) {
                            i59++;
                        } else {
                            seq.add(new BuildPlan(i60, i61, i62, resolveConveyor));
                            reservedFromPlans[(i61 * width) + i60] = true;
                            i37++;
                            break;
                        }
                    }
                }
            }
        }
        for (int i63 = 0; i63 < lastDrillTiles.size; i63++) {
            int i64 = lastDrillTiles.get(i63);
            int i65 = i64 % width;
            int i66 = i64 / width;
            if (i6 == 1 || i6 == 3) {
                for (int i67 = 0; i67 < i3; i67++) {
                    int i68 = i65 + i3;
                    int i69 = i66 + i67;
                    if (freeConv(i68, i69, reservedFromPlans, width, height)) {
                        seq.add(new BuildPlan(i68, i69, 0, resolveConveyor));
                        reservedFromPlans[(i69 * width) + i68] = true;
                        i37++;
                    }
                    int i70 = i65 - 1;
                    int i71 = i66 + i67;
                    if (freeConv(i70, i71, reservedFromPlans, width, height)) {
                        seq.add(new BuildPlan(i70, i71, 2, resolveConveyor));
                        reservedFromPlans[(i71 * width) + i70] = true;
                        i37++;
                    }
                }
            } else {
                for (int i72 = 0; i72 < i3; i72++) {
                    int i73 = i65 + i72;
                    int i74 = i66 + i3;
                    if (freeConv(i73, i74, reservedFromPlans, width, height)) {
                        seq.add(new BuildPlan(i73, i74, 1, resolveConveyor));
                        reservedFromPlans[(i74 * width) + i73] = true;
                        i37++;
                    }
                    int i75 = i65 + i72;
                    int i76 = i66 - 1;
                    if (freeConv(i75, i76, reservedFromPlans, width, height)) {
                        seq.add(new BuildPlan(i75, i76, 3, resolveConveyor));
                        reservedFromPlans[(i76 * width) + i75] = true;
                        i37++;
                    }
                }
            }
        }
        if (i6 == 1 || i6 == 3) {
            for (int i77 = 0; i77 < lastDrillTiles.size; i77++) {
                int i78 = lastDrillTiles.get(i77);
                int i79 = i78 % width;
                int i80 = i78 / width;
                int i81 = Integer.MIN_VALUE;
                int i82 = Integer.MAX_VALUE;
                for (int i83 = 0; i83 < intSeq2.size; i83++) {
                    int i84 = intSeq2.get(i83);
                    int abs = Math.abs(i84 - (i79 + (i3 / 2)));
                    if (abs < i82) {
                        i82 = abs;
                        i81 = i84;
                    }
                }
                if (i81 != Integer.MIN_VALUE) {
                    for (int i85 = 0; i85 < i3; i85++) {
                        int i86 = i80 + i85;
                        int i87 = i81 > i79 ? i79 + i3 : i79 - 1;
                        int i88 = i81 > i79 ? 1 : -1;
                        int i89 = i87;
                        while (true) {
                            int i90 = i89;
                            if (i90 != i81 && freeConv(i90, i86, reservedFromPlans, width, height)) {
                                seq.add(new BuildPlan(i90, i86, i88 > 0 ? 0 : 2, resolveConveyor));
                                reservedFromPlans[(i86 * width) + i90] = true;
                                i37++;
                                if (Math.abs(i90 - i87) > 6) {
                                    break;
                                }
                                i89 = i90 + i88;
                            }
                        }
                    }
                }
            }
        } else {
            for (int i91 = 0; i91 < lastDrillTiles.size; i91++) {
                int i92 = lastDrillTiles.get(i91);
                int i93 = i92 % width;
                int i94 = i92 / width;
                int i95 = Integer.MIN_VALUE;
                int i96 = Integer.MAX_VALUE;
                for (int i97 = 0; i97 < intSeq2.size; i97++) {
                    int i98 = intSeq2.get(i97);
                    int abs2 = Math.abs(i98 - (i94 + (i3 / 2)));
                    if (abs2 < i96) {
                        i96 = abs2;
                        i95 = i98;
                    }
                }
                if (i95 != Integer.MIN_VALUE) {
                    for (int i99 = 0; i99 < i3; i99++) {
                        int i100 = i93 + i99;
                        int i101 = i95 > i94 ? i94 + i3 : i94 - 1;
                        int i102 = i95 > i94 ? 1 : -1;
                        int i103 = i101;
                        while (true) {
                            int i104 = i103;
                            if (i104 != i95 && freeConv(i100, i104, reservedFromPlans, width, height)) {
                                seq.add(new BuildPlan(i100, i104, i102 > 0 ? 1 : 3, resolveConveyor));
                                reservedFromPlans[(i104 * width) + i100] = true;
                                i37++;
                                if (Math.abs(i104 - i101) > 6) {
                                    break;
                                }
                                i103 = i104 + i102;
                            }
                        }
                    }
                }
            }
        }
        int i105 = (i29 + i31) / 2;
        int i106 = (i30 + i32) / 2;
        if ((i6 == 1 || i6 == 3) && intSeq2.size > 0) {
            int i107 = intSeq2.get(0);
            int abs3 = Math.abs(i107 - i105);
            for (int i108 = 1; i108 < intSeq2.size; i108++) {
                int i109 = intSeq2.get(i108);
                if (Math.abs(i109 - i105) < abs3) {
                    abs3 = Math.abs(i109 - i105);
                    i107 = i109;
                }
            }
            i105 = i107;
        }
        if ((i6 == 0 || i6 == 2) && intSeq2.size > 0) {
            int i110 = intSeq2.get(0);
            int abs4 = Math.abs(i110 - i106);
            for (int i111 = 1; i111 < intSeq2.size; i111++) {
                int i112 = intSeq2.get(i111);
                if (Math.abs(i112 - i106) < abs4) {
                    abs4 = Math.abs(i112 - i106);
                    i110 = i112;
                }
            }
            i106 = i110;
        }
        if (i6 == 1) {
            int i113 = i32 + 1;
            if (i113 < height) {
                int i114 = i29;
                while (i114 <= i31) {
                    if (freeConv(i114, i113, reservedFromPlans, width, height)) {
                        boolean z5 = false;
                        for (int i115 = i30; i115 <= i32 && !z5; i115++) {
                            if (zArr2[(i115 * width) + i114] || reservedFromPlans[(i115 * width) + i114]) {
                                z5 = true;
                            }
                        }
                        if (z5) {
                            seq.add(new BuildPlan(i114, i113, i114 == i105 ? 1 : i114 < i105 ? 0 : 2, resolveConveyor));
                            reservedFromPlans[(i113 * width) + i114] = true;
                            i37++;
                        }
                    }
                    i114++;
                }
                if (freeConv(i105, i113 + 1, reservedFromPlans, width, height)) {
                    seq.add(new BuildPlan(i105, i113 + 1, 1, resolveConveyor));
                    reservedFromPlans[((i113 + 1) * width) + i105] = true;
                    i37++;
                    lastHub = ((i113 + 1) * width) + i105;
                } else {
                    lastHub = (i113 * width) + i105;
                }
            }
        } else if (i6 == 3) {
            int i116 = i30 - 1;
            if (i116 >= 0) {
                int i117 = i29;
                while (i117 <= i31) {
                    if (freeConv(i117, i116, reservedFromPlans, width, height)) {
                        boolean z6 = false;
                        for (int i118 = i30; i118 <= i32 && !z6; i118++) {
                            if (zArr2[(i118 * width) + i117] || reservedFromPlans[(i118 * width) + i117]) {
                                z6 = true;
                            }
                        }
                        if (z6) {
                            seq.add(new BuildPlan(i117, i116, i117 == i105 ? 3 : i117 < i105 ? 0 : 2, resolveConveyor));
                            reservedFromPlans[(i116 * width) + i117] = true;
                            i37++;
                        }
                    }
                    i117++;
                }
                if (freeConv(i105, i116 - 1, reservedFromPlans, width, height)) {
                    seq.add(new BuildPlan(i105, i116 - 1, 3, resolveConveyor));
                    reservedFromPlans[((i116 - 1) * width) + i105] = true;
                    i37++;
                    lastHub = ((i116 - 1) * width) + i105;
                } else {
                    lastHub = (i116 * width) + i105;
                }
            }
        } else if (i6 == 0) {
            int i119 = i31 + 1;
            if (i119 < width) {
                int i120 = i30;
                while (i120 <= i32) {
                    if (freeConv(i119, i120, reservedFromPlans, width, height)) {
                        boolean z7 = false;
                        for (int i121 = i29; i121 <= i31 && !z7; i121++) {
                            if (zArr2[(i120 * width) + i121] || reservedFromPlans[(i120 * width) + i121]) {
                                z7 = true;
                            }
                        }
                        if (z7) {
                            seq.add(new BuildPlan(i119, i120, i120 == i106 ? 0 : i120 < i106 ? 1 : 3, resolveConveyor));
                            reservedFromPlans[(i120 * width) + i119] = true;
                            i37++;
                        }
                    }
                    i120++;
                }
                if (freeConv(i119 + 1, i106, reservedFromPlans, width, height)) {
                    seq.add(new BuildPlan(i119 + 1, i106, 0, resolveConveyor));
                    reservedFromPlans[(i106 * width) + i119 + 1] = true;
                    i37++;
                    lastHub = (i106 * width) + i119 + 1;
                } else {
                    lastHub = (i106 * width) + i119;
                }
            }
        } else {
            int i122 = i29 - 1;
            if (i122 >= 0) {
                int i123 = i30;
                while (i123 <= i32) {
                    if (freeConv(i122, i123, reservedFromPlans, width, height)) {
                        boolean z8 = false;
                        for (int i124 = i29; i124 <= i31 && !z8; i124++) {
                            if (zArr2[(i123 * width) + i124] || reservedFromPlans[(i123 * width) + i124]) {
                                z8 = true;
                            }
                        }
                        if (z8) {
                            seq.add(new BuildPlan(i122, i123, i123 == i106 ? 2 : i123 < i106 ? 1 : 3, resolveConveyor));
                            reservedFromPlans[(i123 * width) + i122] = true;
                            i37++;
                        }
                    }
                    i123++;
                }
                if (freeConv(i122 - 1, i106, reservedFromPlans, width, height)) {
                    seq.add(new BuildPlan(i122 - 1, i106, 2, resolveConveyor));
                    reservedFromPlans[((i106 * width) + i122) - 1] = true;
                    i37++;
                    lastHub = ((i106 * width) + i122) - 1;
                } else {
                    lastHub = (i106 * width) + i122;
                }
            }
        }
        if (i6 == 1 || i6 == 3) {
            int i125 = i6 == 1 ? i32 + 1 : i30 - 1;
            for (int i126 = 0; i126 < intSeq2.size; i126++) {
                int i127 = intSeq2.get(i126);
                if (i6 == 1) {
                    for (int i128 = i32; i128 <= i125; i128++) {
                        if (freeConv(i127, i128, reservedFromPlans, width, height)) {
                            seq.add(new BuildPlan(i127, i128, 1, resolveConveyor));
                            reservedFromPlans[(i128 * width) + i127] = true;
                            i37++;
                        }
                    }
                } else {
                    for (int i129 = i125; i129 <= i30; i129++) {
                        if (freeConv(i127, i129, reservedFromPlans, width, height)) {
                            seq.add(new BuildPlan(i127, i129, 3, resolveConveyor));
                            reservedFromPlans[(i129 * width) + i127] = true;
                            i37++;
                        }
                    }
                }
            }
        } else {
            int i130 = i6 == 0 ? i31 + 1 : i29 - 1;
            for (int i131 = 0; i131 < intSeq2.size; i131++) {
                int i132 = intSeq2.get(i131);
                if (i6 == 0) {
                    for (int i133 = i31; i133 <= i130; i133++) {
                        if (freeConv(i133, i132, reservedFromPlans, width, height)) {
                            seq.add(new BuildPlan(i133, i132, 0, resolveConveyor));
                            reservedFromPlans[(i132 * width) + i133] = true;
                            i37++;
                        }
                    }
                } else {
                    for (int i134 = i130; i134 <= i29; i134++) {
                        if (freeConv(i134, i132, reservedFromPlans, width, height)) {
                            seq.add(new BuildPlan(i134, i132, 2, resolveConveyor));
                            reservedFromPlans[(i132 * width) + i134] = true;
                            i37++;
                        }
                    }
                }
            }
        }
        fixRotations(seq, resolveConveyor, width);
        Seq<BuildPlan> pruneConveyors = pruneConveyors(seq, resolveConveyor, width);
        int i135 = 0;
        for (int i136 = 0; i136 < pruneConveyors.size; i136++) {
            if (((BuildPlan) pruneConveyors.get(i136)).block == resolveConveyor) {
                i135++;
            }
        }
        Seq<BuildPlan> sanitize = AutoConnectConveyor.sanitize(pruneConveyors);
        lastPlans.clear();
        lastPlans.addAll(sanitize);
        if (!dryRun) {
            for (int i137 = 0; i137 < sanitize.size; i137++) {
                BuildPlan buildPlan = (BuildPlan) sanitize.get(i137);
                if (buildPlan.block instanceof Drill) {
                    unit.addBuild(buildPlan, true);
                }
            }
            for (int i138 = 0; i138 < sanitize.size; i138++) {
                BuildPlan buildPlan2 = (BuildPlan) sanitize.get(i138);
                if (!(buildPlan2.block instanceof Drill)) {
                    unit.addBuild(buildPlan2, true);
                }
            }
        }
        lastCount = i28;
        if (!dryRun && !quiet) {
            toast(LunariansI18n.get("dr_summary", String.valueOf(i28), String.valueOf(i135)));
        }
        return lastCount;
    }

    private static int[] pickFace(int i, int i2, int i3, int i4, IntSeq intSeq, boolean[] zArr, boolean[] zArr2, int i5, int i6) {
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        if (i4 == 1 || i4 == 3) {
            int i13 = Integer.MIN_VALUE;
            int i14 = Integer.MAX_VALUE;
            for (int i15 = 0; i15 < intSeq.size; i15++) {
                int i16 = intSeq.get(i15);
                int abs = Math.abs(i16 - (i + (i3 / 2)));
                if (abs < i14) {
                    i14 = abs;
                    i13 = i16;
                }
            }
            if (i13 != Integer.MIN_VALUE) {
                int i17 = i13 > i ? i + i3 : i - 1;
                for (int i18 = 0; i18 < i3; i18++) {
                    int i19 = i2 + i18;
                    if (freeConv(i17, i19, zArr, i5, i6)) {
                        return new int[]{i17, i19, i13 > i ? 0 : 2};
                    }
                }
            }
            for (int i20 = 0; i20 < i3; i20++) {
                if (i4 == 1) {
                    i7 = i + i20;
                    i8 = i2 + i3;
                    i9 = 1;
                } else {
                    i7 = i + i20;
                    i8 = i2 - 1;
                    i9 = 3;
                }
                if (freeConv(i7, i8, zArr, i5, i6)) {
                    return new int[]{i7, i8, i9};
                }
            }
            return null;
        }
        int i21 = Integer.MIN_VALUE;
        int i22 = Integer.MAX_VALUE;
        for (int i23 = 0; i23 < intSeq.size; i23++) {
            int i24 = intSeq.get(i23);
            int abs2 = Math.abs(i24 - (i2 + (i3 / 2)));
            if (abs2 < i22) {
                i22 = abs2;
                i21 = i24;
            }
        }
        if (i21 != Integer.MIN_VALUE) {
            int i25 = i21 > i2 ? i2 + i3 : i2 - 1;
            for (int i26 = 0; i26 < i3; i26++) {
                int i27 = i + i26;
                if (freeConv(i27, i25, zArr, i5, i6)) {
                    return new int[]{i27, i25, i21 > i2 ? 1 : 3};
                }
            }
        }
        for (int i28 = 0; i28 < i3; i28++) {
            if (i4 == 0) {
                i10 = i + i3;
                i11 = i2 + i28;
                i12 = 0;
            } else {
                i10 = i - 1;
                i11 = i2 + i28;
                i12 = 2;
            }
            if (freeConv(i10, i11, zArr, i5, i6)) {
                return new int[]{i10, i11, i12};
            }
        }
        return null;
    }

    private static boolean hasAdjConv(int i, int i2, int i3, boolean[] zArr, int i4) {
        for (int i5 = 0; i5 < i3; i5++) {
            for (int i6 = 0; i6 < i3; i6++) {
                for (int i7 = 0; i7 < 4; i7++) {
                    int i8 = i + i6 + DX[i7];
                    int i9 = i2 + i5 + DY[i7];
                    if (Vars.world.tiles.in(i8, i9) && ((i8 < i || i8 >= i + i3 || i9 < i2 || i9 >= i2 + i3) && zArr[(i9 * i4) + i8])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean touchesDrill(int i, int i2, boolean[] zArr, int i3, int i4) {
        for (int i5 = 0; i5 < 4; i5++) {
            int i6 = i + DX[i5];
            int i7 = i2 + DY[i5];
            if (Vars.world.tiles.in(i6, i7) && zArr[(i7 * i3) + i6]) {
                return true;
            }
        }
        return false;
    }

    private static boolean canPlaceDrill(int i, int i2, int i3, boolean[] zArr, int i4, int i5) {
        Tile tile;
        if (i < 0 || i2 < 0 || i + i3 > i4 || i2 + i3 > i5) {
            return false;
        }
        for (int i6 = 0; i6 < i3; i6++) {
            for (int i7 = 0; i7 < i3; i7++) {
                if (zArr[((i2 + i6) * i4) + i + i7] || (tile = Vars.world.tile(i + i7, i2 + i6)) == null) {
                    return false;
                }
                if (tile.solid() && tile.build == null) {
                    return false;
                }
                if (tile.build != null && tile.build.block != null && tile.build.block.solid) {
                    return false;
                }
            }
        }
        if (limitOn) {
            if (crowded(i, i2, i3, isoOverride >= 0 ? isoOverride : AutoSetConfig.spacing())) {
                return false;
            }
        }
        return true;
    }

    private static int[] scanGrid(boolean z, boolean z2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Item item, boolean[] zArr, boolean[] zArr2, boolean[] zArr3, int i9, int i10, Block block, Seq<BuildPlan> seq, IntSeq intSeq, IntSeq intSeq2) {
        int countOre;
        int i11 = z ? i4 + i : i5 + i2;
        int i12 = z ? i5 + i2 : i4 + i;
        int i13 = z ? i6 : i7;
        int i14 = z ? i7 : i6;
        int i15 = z ? i9 : i10;
        int i16 = 0;
        int i17 = 0;
        int i18 = 0;
        while ((i11 + i3) - 1 <= i13) {
            boolean z3 = false;
            int i19 = i12;
            while (true) {
                int i20 = i19;
                if ((i20 + i3) - 1 > i14) {
                    break;
                }
                int i21 = z ? i11 : i20;
                int i22 = z ? i20 : i11;
                if (canPlaceDrill(i21, i22, i3, zArr2, i9, i10) && (countOre = countOre(i21, i22, i3, item, zArr, i9)) >= i8) {
                    seq.add(new BuildPlan(i21 + ((i3 - 1) / 2), i22 + ((i3 - 1) / 2), 0, block));
                    intSeq.add((i22 * i9) + i21);
                    mark(i21, i22, i3, zArr2, i9);
                    mark(i21, i22, i3, zArr3, i9);
                    i16++;
                    i17 += countOre;
                    z3 = true;
                }
                i19 = i20 + i3;
            }
            if (z3) {
                i18++;
                if (z2 && i18 % 2 != 0) {
                    i11 += i3;
                } else {
                    int i23 = i11 + i3;
                    if (i23 < i15) {
                        intSeq2.add(i23);
                    }
                    i11 += i3 + 1;
                }
            } else {
                i11++;
            }
        }
        return new int[]{i16, i17};
    }

    private static int fillGaps(boolean z, int i, int i2, Item item, boolean[] zArr, boolean[] zArr2, boolean[] zArr3, int i3, int i4, Block block, Seq<BuildPlan> seq, IntSeq intSeq, IntSeq intSeq2) {
        int countOre;
        if (intSeq.size == 0) {
            return 0;
        }
        int i5 = Integer.MAX_VALUE;
        int i6 = Integer.MAX_VALUE;
        int i7 = 0;
        int i8 = 0;
        for (int i9 = 0; i9 < intSeq.size; i9++) {
            int i10 = intSeq.get(i9);
            i5 = Math.min(i5, i10 % i3);
            i6 = Math.min(i6, i10 / i3);
            i7 = Math.max(i7, ((i10 % i3) + i) - 1);
            i8 = Math.max(i8, ((i10 / i3) + i) - 1);
        }
        int max = Math.max(1, Math.min(i2, ((i * i) + 2) / 3));
        int i11 = 0;
        for (int i12 = 0; i12 < 64; i12++) {
            int i13 = -1;
            int i14 = -1;
            int i15 = 0;
            for (int i16 = i6; (i16 + i) - 1 <= i8; i16++) {
                for (int i17 = i5; (i17 + i) - 1 <= i7; i17++) {
                    int i18 = z ? i17 : i16;
                    boolean z2 = false;
                    int i19 = 0;
                    while (true) {
                        if (i19 >= intSeq2.size) {
                            break;
                        }
                        int i20 = intSeq2.get(i19);
                        if (i20 < i18 || i20 > (i18 + i) - 1) {
                            i19++;
                        } else {
                            z2 = true;
                            break;
                        }
                    }
                    if (!z2 && canPlaceDrill(i17, i16, i, zArr2, i3, i4) && (countOre = countOre(i17, i16, i, item, zArr, i3)) > i15) {
                        i15 = countOre;
                        i13 = i17;
                        i14 = i16;
                    }
                }
            }
            if (i13 < 0 || i15 < max) {
                break;
            }
            seq.add(new BuildPlan(i13 + ((i - 1) / 2), i14 + ((i - 1) / 2), 0, block));
            intSeq.add((i14 * i3) + i13);
            mark(i13, i14, i, zArr2, i3);
            mark(i13, i14, i, zArr3, i3);
            i11++;
        }
        return i11;
    }

    private static int countOre(int i, int i2, int i3, Item item, boolean[] zArr, int i4) {
        int i5 = 0;
        int i6 = 0;
        HashMap hashMap = null;
        for (int i7 = 0; i7 < i3; i7++) {
            for (int i8 = 0; i8 < i3; i8++) {
                int i9 = ((i2 + i7) * i4) + i + i8;
                Tile tile = Vars.world.tile(i + i8, i2 + i7);
                Item oreAt = oreAt(tile);
                if (oreAt != null) {
                    if (oreAt == item) {
                        i6++;
                    } else if (limitOn) {
                        if (hashMap == null) {
                            hashMap = new HashMap();
                        }
                        HashMap hashMap2 = hashMap;
                        Integer num = (Integer) hashMap.get(oreAt);
                        hashMap2.put(oreAt, Integer.valueOf(num == null ? 1 : num.intValue() + 1));
                    }
                }
                if (zArr[i9] && sameOre(tile, item)) {
                    i5++;
                }
            }
        }
        if (limitOn && hashMap != null) {
            for (Map.Entry entry : hashMap.entrySet()) {
                Item item2 = (Item) entry.getKey();
                int intValue = ((Integer) entry.getValue()).intValue();
                if (!item2.lowPriority || item.lowPriority) {
                    if ((!item2.lowPriority && item.lowPriority) || intValue > i6) {
                        return -1;
                    }
                    if (intValue == i6 && item2.id > item.id) {
                        return -1;
                    }
                }
            }
        }
        return i5;
    }

    private static void mark(int i, int i2, int i3, boolean[] zArr, int i4) {
        for (int i5 = 0; i5 < i3; i5++) {
            for (int i6 = 0; i6 < i3; i6++) {
                zArr[((i2 + i5) * i4) + i + i6] = true;
            }
        }
    }

    private static void fixRotations(Seq<BuildPlan> seq, Block block, int i) {
        int i2;
        if (lastHub < 0 || seq == null || seq.isEmpty()) {
            return;
        }
        boolean[] zArr = new boolean[i * Vars.world.height()];
        int[] iArr = new int[i * Vars.world.height()];
        Arrays.fill(iArr, -1);
        for (int i3 = 0; i3 < seq.size; i3++) {
            Object obj = seq.get(i3);
            if (((BuildPlan) obj).block == block && (i2 = (((BuildPlan) obj).y * i) + ((BuildPlan) obj).x) >= 0 && i2 < zArr.length) {
                zArr[i2] = true;
                iArr[i2] = i3;
            }
        }
        if (!zArr[lastHub]) {
            return;
        }
        int[] iArr2 = new int[zArr.length];
        Arrays.fill(iArr2, -1);
        IntQueue intQueue = new IntQueue();
        intQueue.addLast(lastHub);
        iArr2[lastHub] = lastHub;
        while (!intQueue.isEmpty()) {
            int removeFirst = intQueue.removeFirst();
            int i4 = removeFirst % i;
            int i5 = removeFirst / i;
            for (int i6 = 0; i6 < 4; i6++) {
                int i7 = i4 + DX[i6];
                int i8 = i5 + DY[i6];
                if (Vars.world.tiles.in(i7, i8)) {
                    int i9 = (i8 * i) + i7;
                    if (iArr2[i9] < 0 && zArr[i9]) {
                        iArr2[i9] = removeFirst;
                        intQueue.addLast(i9);
                    }
                }
            }
        }
        for (int i10 = 0; i10 < zArr.length; i10++) {
            if (zArr[i10] && iArr2[i10] >= 0 && iArr2[i10] != i10) {
                int i11 = iArr2[i10];
                int i12 = i10 % i;
                int i13 = i11 % i;
                int i14 = i13 > i12 ? 0 : i13 < i12 ? 2 : i11 / i > i10 / i ? 1 : 3;
                int i15 = iArr[i10];
                if (i15 >= 0) {
                    BuildPlan buildPlan = (BuildPlan) seq.get(i15);
                    seq.set(i15, new BuildPlan(buildPlan.x, buildPlan.y, i14, block));
                }
            }
        }
    }

    private static Block resolveDrillFor(Item item) {
        String drillFor;
        Block block = null;
        if (limitOn && item != null && (drillFor = AutoSetConfig.drillFor(item.name)) != null && !drillFor.isEmpty()) {
            Block block2 = Vars.content.block(drillFor);
            if (block2 instanceof Drill) {
                block = block2;
            }
        }
        if (block == null) {
            block = resolveDrill();
        }
        if (item != null && (block instanceof Drill) && ((Drill) block).tier < item.hardness) {
            Block block3 = null;
            Iterator it = Vars.content.blocks().iterator();
            while (it.hasNext()) {
                Block block4 = (Block) it.next();
                if (block4 instanceof Drill) {
                    Block block5 = (Drill) block4;
                    if (((Drill) block5).tier >= item.hardness && (block3 == null || ((Drill) block5).tier < ((Drill) block3).tier || (((Drill) block5).tier == ((Drill) block3).tier && ((Drill) block5).size < ((Drill) block3).size))) {
                        block3 = block5;
                    }
                }
            }
            if (block3 != null) {
                if (!quiet) {
                    toast(pt() ? block.localizedName + " não minera " + item.localizedName + ": usando " + ((Drill) block3).localizedName : block.localizedName + " cannot mine " + item.localizedName + ": using " + ((Drill) block3).localizedName);
                }
                block = block3;
            }
        }
        return block;
    }

    private static boolean pollutes(Block block) {
        return (block instanceof Drill) || (block instanceof GenericCrafter) || (block instanceof Unloader);
    }

    private static void markAround(boolean[] zArr, int i, int i2, int i3, int i4) {
        for (int i5 = 0; i5 < 4; i5++) {
            int i6 = i + DX[i5];
            int i7 = i2 + DY[i5];
            if (i6 >= 0 && i7 >= 0 && i6 < i3 && i7 < i4) {
                zArr[(i7 * i3) + i6] = true;
            }
        }
    }

    public static boolean[] reservedForFeed(Unit unit) {
        boolean[] reservedFromPlans = AutoConnectConveyor.reservedFromPlans(unit);
        try {
            int width = Vars.world.width();
            int height = Vars.world.height();
            for (int i = 0; i < height; i++) {
                for (int i2 = 0; i2 < width; i2++) {
                    Tile tile = Vars.world.tile(i2, i);
                    if (tile != null && tile.build != null && tile.build.block != null && pollutes(tile.build.block)) {
                        markAround(reservedFromPlans, i2, i, width, height);
                    }
                }
            }
            Iterator it = unit.plans().iterator();
            while (it.hasNext()) {
                BuildPlan buildPlan = (BuildPlan) it.next();
                if (!buildPlan.breaking && buildPlan.block != null && pollutes(buildPlan.block)) {
                    int i3 = buildPlan.block.size;
                    int i4 = (i3 - 1) / 2;
                    for (int i5 = 0; i5 < i3; i5++) {
                        for (int i6 = 0; i6 < i3; i6++) {
                            markAround(reservedFromPlans, (buildPlan.x - i4) + i6, (buildPlan.y - i4) + i5, width, height);
                        }
                    }
                }
            }
        } catch (Throwable th) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err("[Lunarians] reservedForFeed", th);
            }
        }
        return reservedFromPlans;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static Seq<BuildPlan> pruneConveyors(Seq<BuildPlan> seq, Block block, int i) {
        int i2;
        Integer num;
        try {
            if (lastHub < 0 || seq == null || seq.isEmpty()) {
                return seq;
            }
            int height = Vars.world.height();
            int[] iArr = new int[i * height];
            Arrays.fill(iArr, -1);
            ArrayList arrayList = new ArrayList();
            HashMap hashMap = new HashMap();
            int i3 = 0;
            for (int i4 = 0; i4 < seq.size; i4++) {
                BuildPlan buildPlan = (BuildPlan) seq.get(i4);
                if (buildPlan.block == block) {
                    if (buildPlan.x >= 0 && buildPlan.y >= 0 && buildPlan.x < i && buildPlan.y < height) {
                        iArr[(buildPlan.y * i) + buildPlan.x] = arrayList.size();
                        arrayList.add(buildPlan);
                    }
                } else if (buildPlan.block instanceof Drill) {
                    int i5 = buildPlan.block.size;
                    int i6 = (i5 - 1) / 2;
                    for (int i7 = 0; i7 < i5; i7++) {
                        for (int i8 = 0; i8 < i5; i8++) {
                            hashMap.put(Integer.valueOf((((buildPlan.y - i6) + i7) * i) + (buildPlan.x - i6) + i8), Integer.valueOf(i3));
                        }
                    }
                    i3++;
                }
            }
            int size = arrayList.size();
            if (size == 0 || iArr[lastHub] < 0) {
                return seq;
            }
            int i9 = iArr[lastHub];
            int[] iArr2 = new int[size];
            int[] iArr3 = new int[size];
            for (int i10 = 0; i10 < size; i10++) {
                BuildPlan buildPlan2 = (BuildPlan) arrayList.get(i10);
                int i11 = buildPlan2.rotation & 3;
                int i12 = buildPlan2.x + DX[i11];
                int i13 = buildPlan2.y + DY[i11];
                iArr2[i10] = (i12 < 0 || i13 < 0 || i12 >= i || i13 >= height) ? -1 : iArr[(i13 * i) + i12];
                ArrayList arrayList2 = new ArrayList();
                for (int i14 = 0; i14 < 4; i14++) {
                    if (i14 != i11) {
                        int i15 = buildPlan2.x + DX[i14];
                        int i16 = buildPlan2.y + DY[i14];
                        if (i15 >= 0 && i16 >= 0 && i15 < i && i16 < height && (num = (Integer) hashMap.get(Integer.valueOf((i16 * i) + i15))) != null && !arrayList2.contains(num)) {
                            arrayList2.add(num);
                        }
                    }
                }
                iArr3[i10] = new int[arrayList2.size()];
                for (int i17 = 0; i17 < arrayList2.size(); i17++) {
                    iArr3[i10][i17] = ((Integer) arrayList2.get(i17)).intValue();
                }
            }
            boolean[] zArr = new boolean[size];
            for (int i18 = 0; i18 < size; i18++) {
                int i19 = i18;
                int i20 = 0;
                while (true) {
                    if (i19 >= 0 && i20 <= size) {
                        if (i19 == i9) {
                            zArr[i18] = true;
                            break;
                        }
                        i19 = iArr2[i19];
                        i20++;
                    }
                }
            }
            boolean z = true;
            while (z) {
                z = false;
                int[] iArr4 = new int[size];
                for (int i21 = 0; i21 < size; i21++) {
                    if (zArr[i21] && iArr2[i21] >= 0 && zArr[iArr2[i21]]) {
                        int i22 = iArr2[i21];
                        iArr4[i22] = iArr4[i22] + 1;
                    }
                }
                int[] iArr5 = new int[Math.max(1, i3)];
                for (int i23 = 0; i23 < size; i23++) {
                    if (zArr[i23]) {
                        for (char c : iArr3[i23]) {
                            iArr5[c] = iArr5[c] + 1;
                        }
                    }
                }
                for (int i24 = 0; i24 < size; i24++) {
                    if (zArr[i24] && i24 != i9 && iArr4[i24] <= 0) {
                        boolean z2 = false;
                        Object[] objArr = iArr3[i24];
                        int length = objArr.length;
                        int i25 = 0;
                        while (true) {
                            if (i25 >= length) {
                                break;
                            }
                            if (iArr5[objArr[i25]] > 1) {
                                i25++;
                            } else {
                                z2 = true;
                                break;
                            }
                        }
                        if (!z2) {
                            zArr[i24] = false;
                            z = true;
                            for (char c2 : iArr3[i24]) {
                                iArr5[c2] = iArr5[c2] - 1;
                            }
                        }
                    }
                }
            }
            Seq<BuildPlan> seq2 = new Seq<>();
            for (int i26 = 0; i26 < seq.size; i26++) {
                BuildPlan buildPlan3 = (BuildPlan) seq.get(i26);
                if (buildPlan3.block != block || buildPlan3.x < 0 || buildPlan3.y < 0 || buildPlan3.x >= i || buildPlan3.y >= height || (i2 = iArr[(buildPlan3.y * i) + buildPlan3.x]) < 0 || zArr[i2]) {
                    seq2.add(buildPlan3);
                }
            }
            return seq2;
        } catch (Throwable th) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err("[Lunarians] pruneConveyors", th);
            }
            return seq;
        }
    }

    private static boolean pt() {
        return "pt".equals(LunariansSettings.language);
    }

    private static void toast(String str) {
        if (Vars.ui != null) {
            Vars.ui.showInfoToast(str, 3.5f);
        }
    }
}
