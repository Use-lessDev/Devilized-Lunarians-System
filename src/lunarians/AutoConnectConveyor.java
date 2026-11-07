package lunarians;

import arc.math.geom.Point2;
import arc.struct.IntSeq;
import arc.struct.Seq;
import arc.util.Log;
import java.util.Arrays;
import java.util.Iterator;
import java.util.PriorityQueue;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.Tiles;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.blocks.distribution.Junction;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.storage.StorageBlock;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoConnectConveyor.class */
public class AutoConnectConveyor {
    private static final int[] DX = {1, 0, -1, 0};
    private static final int[] DY = {0, 1, 0, -1};
    private static boolean errorLogged;
    public static int lastJunctions;
    public static int lastBridges;
    private static final int LONG_ROUTE = 40;
    private static byte[] freeCache;

    public static void init() {
    }

    public static void reset() {
    }

    public static int connect() {
        if (Vars.headless || Vars.player == null || Vars.state == null || !Vars.state.isGame()) {
            return 0;
        }
        try {
            return connectInner();
        } catch (Throwable th) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err("[Lunarians] AutoConnectConveyor error", th);
                return 0;
            }
            return 0;
        }
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

    private static Block resolveJunction() {
        if (Blocks.junction != null) {
            return Blocks.junction;
        }
        return Vars.content.block("junction");
    }

    public static boolean isCoreNetworkStorage(Building building) {
        if (building == null || !building.isValid() || building.team != Vars.player.team()) {
            return false;
        }
        if (building.block instanceof CoreBlock) {
            return true;
        }
        if (building instanceof StorageBlock.StorageBuild) {
            try {
                if (((StorageBlock.StorageBuild) building).linkedCore != null) {
                    return true;
                }
            } catch (Throwable th) {
            }
            for (int i = 0; i < 4; i++) {
                Tile nearby = building.tile.nearby(i);
                if (nearby != null && nearby.build != null && (nearby.build.block instanceof CoreBlock) && nearby.build.team == Vars.player.team()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static void markPlan(BuildPlan buildPlan, boolean[] zArr) {
        if (buildPlan == null || buildPlan.block == null || buildPlan.breaking) {
            return;
        }
        int width = Vars.world.width();
        int i = buildPlan.block.size;
        int i2 = (i - 1) / 2;
        for (int i3 = 0; i3 < i; i3++) {
            for (int i4 = 0; i4 < i; i4++) {
                int i5 = (buildPlan.x - i2) + i4;
                int i6 = (buildPlan.y - i2) + i3;
                if (Vars.world.tiles.in(i5, i6)) {
                    zArr[(i6 * width) + i5] = true;
                }
            }
        }
    }

    public static boolean[] reservedFromPlans(Unit unit) {
        boolean[] zArr = new boolean[Vars.world.width() * Vars.world.height()];
        if (unit != null) {
            Iterator it = unit.plans().iterator();
            while (it.hasNext()) {
                markPlan((BuildPlan) it.next(), zArr);
            }
        }
        return zArr;
    }

    private static boolean fedByNeighbor(int i, int i2) {
        for (int i3 = 0; i3 < 4; i3++) {
            Tile tile = Vars.world.tile(i + DX[i3], i2 + DY[i3]);
            if (tile != null && tile.build != null) {
                Block block = tile.build.block;
                if (block instanceof Conveyor) {
                    int i4 = tile.build.rotation;
                    if (tile.x + DX[i4 & 3] == i && tile.y + DY[i4 & 3] == i2) {
                        return true;
                    }
                } else if (block != null && block.name != null && (block.name.contains("router") || block.name.contains("overflow") || block.name.contains("underflow") || block.name.contains("sorter"))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean tileFree(int i, int i2, boolean[] zArr) {
        if (!Vars.world.tiles.in(i, i2)) {
            return false;
        }
        int width = Vars.world.width();
        if (zArr != null && zArr[(i2 * width) + i]) {
            return false;
        }
        int i3 = (i2 * width) + i;
        if (freeCache != null && freeCache[i3] != 0) {
            return freeCache[i3] == 1;
        }
        Tile tile = Vars.world.tile(i, i2);
        boolean z = false;
        if (tile != null && tile.build == null) {
            z = (!Build.validPlace(resolveConveyor(), Vars.player.team(), i, i2, 0) || fedByNeighbor(i, i2) || (AutoDrill.limitOn && AutoDrill.nextToDrill(i, i2))) ? false : true;
        }
        if (freeCache != null) {
            freeCache[i3] = (byte) (z ? 1 : 2);
        }
        return z;
    }

    private static boolean crossable(int i, int i2, int i3, boolean[] zArr) {
        Tile tile;
        Tile tile2;
        int width = Vars.world.width();
        if ((zArr != null && zArr[(i2 * width) + i]) || (tile = Vars.world.tile(i, i2)) == null || tile.build == null || tile.build.team != Vars.player.team()) {
            return false;
        }
        Block block = tile.build.block;
        if (block instanceof Junction) {
            return true;
        }
        if (!(block instanceof Conveyor)) {
            return false;
        }
        int i4 = tile.build.rotation & 3;
        if ((i4 & 1) == (i3 & 1) || (tile2 = Vars.world.tile(i + DX[i4], i2 + DY[i4])) == null || tile2.build == null) {
            return false;
        }
        for (int i5 = 1; i5 <= 3; i5 += 2) {
            int i6 = (i4 + i5) & 3;
            Tile tile3 = Vars.world.tile(i + DX[i6], i2 + DY[i6]);
            if (tile3 != null && tile3.build != null && (tile3.build.block instanceof Conveyor)) {
                short s = tile3.x;
                int[] iArr = DX;
                int i7 = tile3.build.rotation & 3;
                if (s + iArr[i7] == i && tile3.y + DY[i7] == i2) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Seq<BuildPlan> planRoute(int i, int i2, int i3, boolean[] zArr) {
        lastJunctions = 0;
        Block resolveConveyor = resolveConveyor();
        Block resolveJunction = resolveJunction();
        if (resolveConveyor == null || resolveJunction == null || !Vars.world.tiles.in(i, i2)) {
            return null;
        }
        int width = Vars.world.width();
        int height = Vars.world.height();
        freeCache = new byte[width * height];
        try {
            lastBridges = 0;
            Seq<BuildPlan> planRouteInner = planRouteInner(i, i2, i3, zArr, resolveConveyor, resolveJunction, width, height, false);
            int i4 = lastJunctions;
            if ((planRouteInner != null && planRouteInner.size <= LONG_ROUTE) || Blocks.itemBridge == null) {
                return planRouteInner;
            }
            lastJunctions = 0;
            lastBridges = 0;
            Seq<BuildPlan> planRouteInner2 = planRouteInner(i, i2, i3, zArr, resolveConveyor, resolveJunction, width, height, true);
            if (planRouteInner2 != null && (planRouteInner == null || planRouteInner2.size < planRouteInner.size)) {
                freeCache = null;
                return planRouteInner2;
            }
            lastJunctions = i4;
            lastBridges = 0;
            freeCache = null;
            return planRouteInner;
        } finally {
            freeCache = null;
        }
    }

    private static Seq<BuildPlan> planRouteInner(int i, int i2, int i3, boolean[] zArr, Block block, Block block2, int i4, int i5, boolean z) {
        int i6;
        if (!tileFree(i, i2, zArr)) {
            return null;
        }
        int i7 = i4 * i5 * 4;
        int[] iArr = new int[i7];
        int[] iArr2 = new int[i7];
        Arrays.fill(iArr, Integer.MAX_VALUE);
        Arrays.fill(iArr2, -1);
        byte[] bArr = new byte[i7];
        int i8 = z ? Blocks.itemBridge.range : 0;
        PriorityQueue priorityQueue = new PriorityQueue();
        for (int i9 = 0; i9 < 4; i9++) {
            if (i3 < 0 || i9 == i3) {
                int i10 = (((i2 * i4) + i) * 4) + i9;
                iArr[i10] = 0;
                priorityQueue.add(Long.valueOf(i10));
            }
        }
        int i11 = Integer.MAX_VALUE;
        int i12 = -1;
        int i13 = 0;
        while (!priorityQueue.isEmpty()) {
            long longValue = ((Long) priorityQueue.poll()).longValue();
            int i14 = (int) (longValue >>> 32);
            int i15 = (int) (longValue & 4294967295L);
            if (i14 <= iArr[i15]) {
                if (i14 >= i11) {
                    break;
                }
                int i16 = i15 & 3;
                int i17 = i15 >> 2;
                int i18 = i17 % i4;
                int i19 = i17 / i4;
                Tile tile = Vars.world.tile(i18, i19);
                boolean z2 = (tile == null || tile.build == null) ? false : true;
                int i20 = 0;
                while (i20 < 4) {
                    if ((!z2 || i20 == i16) && (z2 || i20 != ((i16 + 2) & 3))) {
                        Tiles tiles = Vars.world.tiles;
                        int i21 = i18 + DX[i20];
                        int i22 = i19 + DY[i20];
                        if (tiles.in(i21, i22)) {
                            int i23 = i20 != i16 ? 1 : 0;
                            Tile tile2 = Vars.world.tile(i21, i22);
                            if (tile2 != null) {
                                if (!z2 && tile2.build != null && isCoreNetworkStorage(tile2.build)) {
                                    int i24 = i14 + i23;
                                    if (i24 < i11) {
                                        i11 = i24;
                                        i12 = i15;
                                        i13 = i20;
                                    }
                                } else {
                                    if (tileFree(i21, i22, zArr)) {
                                        i6 = i14 + 1 + i23;
                                    } else if (crossable(i21, i22, i20, zArr)) {
                                        i6 = i14 + 6 + i23;
                                    }
                                    int i25 = (((i22 * i4) + i21) * 4) + i20;
                                    if (i6 < iArr[i25]) {
                                        iArr[i25] = i6;
                                        iArr2[i25] = i15;
                                        bArr[i25] = 0;
                                        priorityQueue.add(Long.valueOf((i6 << 32) | i25));
                                    }
                                }
                            }
                        }
                    }
                    i20++;
                }
                if (z && !z2) {
                    int i26 = 0;
                    while (i26 < 4) {
                        if (i26 != ((i16 + 2) & 3)) {
                            for (int i27 = 2; i27 <= i8; i27++) {
                                int i28 = i18 + (DX[i26] * i27);
                                int i29 = i19 + (DY[i26] * i27);
                                if (Vars.world.tiles.in(i28, i29) && tileFree(i28, i29, zArr)) {
                                    int i30 = i14 + 8 + i27 + (i26 != i16 ? 1 : 0);
                                    int i31 = (((i29 * i4) + i28) * 4) + i26;
                                    if (i30 < iArr[i31]) {
                                        iArr[i31] = i30;
                                        iArr2[i31] = i15;
                                        bArr[i31] = (byte) i27;
                                        priorityQueue.add(Long.valueOf((i30 << 32) | i31));
                                    }
                                }
                            }
                        }
                        i26++;
                    }
                }
            }
        }
        if (i12 < 0) {
            return null;
        }
        IntSeq intSeq = new IntSeq();
        int i32 = i12;
        while (true) {
            int i33 = i32;
            if (i33 != -1) {
                intSeq.add(i33);
                if (intSeq.size > i7) {
                    return null;
                }
                i32 = iArr2[i33];
            } else {
                int i34 = intSeq.size;
                Seq<BuildPlan> seq = new Seq<>();
                for (int i35 = 0; i35 < i34; i35++) {
                    int i36 = intSeq.get((i34 - 1) - i35);
                    int i37 = i36 >> 2;
                    int i38 = i37 % i4;
                    int i39 = i37 / i4;
                    int i40 = i35 + 1 < i34 ? intSeq.get((i34 - 2) - i35) & 3 : i13;
                    Tile tile3 = Vars.world.tile(i38, i39);
                    byte b = i35 + 1 < i34 ? bArr[intSeq.get((i34 - 2) - i35)] : (byte) 0;
                    boolean z3 = bArr[i36] > 0;
                    if (tile3.build == null && (b > 0 || z3)) {
                        BuildPlan buildPlan = new BuildPlan(i38, i39, 0, Blocks.itemBridge);
                        if (b > 0) {
                            int i41 = i40 & 3;
                            buildPlan.config = new Point2(DX[i41] * b, DY[i41] * b);
                        }
                        seq.add(buildPlan);
                        if (b > 0) {
                            lastBridges++;
                        }
                    } else if (tile3.build != null) {
                        if (!(tile3.build.block instanceof Junction)) {
                            seq.add(new BuildPlan(i38, i39, 0, block2));
                            lastJunctions++;
                        }
                    } else {
                        seq.add(new BuildPlan(i38, i39, i40, block));
                    }
                }
                return seq;
            }
        }
    }

    public static Seq<BuildPlan> sanitize(Seq<BuildPlan> seq) {
        Tile tile;
        Seq<BuildPlan> seq2 = new Seq<>();
        Block resolveJunction = resolveJunction();
        for (int i = 0; i < seq.size; i++) {
            BuildPlan buildPlan = (BuildPlan) seq.get(i);
            if ((buildPlan.block instanceof Conveyor) && !buildPlan.breaking && (tile = Vars.world.tile(buildPlan.x, buildPlan.y)) != null && tile.build != null && tile.build.block != null) {
                if ((tile.build.block instanceof Conveyor) && resolveJunction != null && (tile.build.rotation & 1) != (buildPlan.rotation & 1)) {
                    seq2.add(new BuildPlan(buildPlan.x, buildPlan.y, 0, resolveJunction));
                }
            } else {
                seq2.add(buildPlan);
            }
        }
        return seq2;
    }

    private static int connectInner() {
        int i = LunariansSettings.conveyorStartX;
        int i2 = LunariansSettings.conveyorStartY;
        if (i == Integer.MIN_VALUE || i2 == Integer.MIN_VALUE) {
            toast(LunariansI18n.get("cv_set_start"));
            return 0;
        }
        if (resolveConveyor() == null) {
            toast(LunariansI18n.get("cv_invalid"));
            return 0;
        }
        Unit unit = Vars.player.unit();
        if (unit == null || !unit.canBuild()) {
            toast(LunariansI18n.get("cv_need_builder"));
            return 0;
        }
        boolean[] reservedFromPlans = reservedFromPlans(unit);
        if (!tileFree(i, i2, reservedFromPlans)) {
            toast(LunariansI18n.get("cv_start_blocked"));
            return 0;
        }
        Seq<BuildPlan> planRoute = planRoute(i, i2, -1, reservedFromPlans);
        if (planRoute == null || planRoute.isEmpty()) {
            toast(LunariansI18n.get("cv_no_path"));
            return 0;
        }
        for (int i3 = 0; i3 < planRoute.size; i3++) {
            unit.addBuild((BuildPlan) planRoute.get(i3), true);
        }
        toast(LunariansI18n.get("cv_planned", String.valueOf(planRoute.size), String.valueOf(lastJunctions)));
        return planRoute.size;
    }

    private static void toast(String str) {
        if (Vars.ui != null) {
            Vars.ui.showInfoToast(str, 3.0f);
        }
    }
}
