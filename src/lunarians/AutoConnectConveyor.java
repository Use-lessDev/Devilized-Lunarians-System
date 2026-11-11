/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.math.geom.Point2
 *  arc.struct.IntSeq
 *  arc.struct.Seq
 *  arc.util.Log
 *  mindustry.Vars
 *  mindustry.content.Blocks
 *  mindustry.entities.units.BuildPlan
 *  mindustry.game.Team
 *  mindustry.gen.Building
 *  mindustry.gen.Unit
 *  mindustry.world.Block
 *  mindustry.world.Build
 *  mindustry.world.Tile
 *  mindustry.world.blocks.distribution.Conveyor
 *  mindustry.world.blocks.distribution.ItemBridge
 *  mindustry.world.blocks.distribution.Junction
 *  mindustry.world.blocks.storage.CoreBlock
 *  mindustry.world.blocks.storage.StorageBlock$StorageBuild
 */
package lunarians;

import arc.math.geom.Point2;
import arc.struct.IntSeq;
import arc.struct.Seq;
import arc.util.Log;
import java.util.Arrays;
import java.util.PriorityQueue;
import lunarians.AutoDrill;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.blocks.distribution.ItemBridge;
import mindustry.world.blocks.distribution.Junction;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.storage.StorageBlock;

public class AutoConnectConveyor {
    private static final int[] DX = new int[]{1, 0, -1, 0};
    private static final int[] DY = new int[]{0, 1, 0, -1};
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
            return AutoConnectConveyor.connectInner();
        }
        catch (Throwable throwable) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err((String)"[Lunarians] AutoConnectConveyor error", (Throwable)throwable);
            }
            return 0;
        }
    }

    private static Block resolveConveyor() {
        Block block;
        String string = LunariansSettings.conveyorBlock;
        if (string == null || string.isEmpty()) {
            string = "conveyor";
        }
        if ((block = Vars.content.block(string)) instanceof Conveyor) {
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
        if (building == null || !building.isValid()) {
            return false;
        }
        if (building.team != Vars.player.team()) {
            return false;
        }
        if (building.block instanceof CoreBlock) {
            return true;
        }
        if (building instanceof StorageBlock.StorageBuild) {
            try {
                if (((StorageBlock.StorageBuild)building).linkedCore != null) {
                    return true;
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            for (int i = 0; i < 4; ++i) {
                Tile tile = building.tile.nearby(i);
                if (tile == null || tile.build == null || !(tile.build.block instanceof CoreBlock) || tile.build.team != Vars.player.team()) continue;
                return true;
            }
        }
        return false;
    }

    public static void markPlan(BuildPlan buildPlan, boolean[] blArray) {
        if (buildPlan == null || buildPlan.block == null || buildPlan.breaking) {
            return;
        }
        int n = Vars.world.width();
        int n2 = buildPlan.block.size;
        int n3 = (n2 - 1) / 2;
        for (int i = 0; i < n2; ++i) {
            for (int j = 0; j < n2; ++j) {
                int n4 = buildPlan.x - n3 + j;
                int n5 = buildPlan.y - n3 + i;
                if (!Vars.world.tiles.in(n4, n5)) continue;
                blArray[n5 * n + n4] = true;
            }
        }
    }

    public static boolean[] reservedFromPlans(Unit unit) {
        boolean[] blArray = new boolean[Vars.world.width() * Vars.world.height()];
        if (unit != null) {
            for (BuildPlan buildPlan : unit.plans()) {
                AutoConnectConveyor.markPlan(buildPlan, blArray);
            }
        }
        return blArray;
    }

    private static boolean fedByNeighbor(int n, int n2) {
        for (int i = 0; i < 4; ++i) {
            int n3;
            Block block;
            Tile tile = Vars.world.tile(n + DX[i], n2 + DY[i]);
            if (tile == null || tile.build == null || ((block = tile.build.block) instanceof Conveyor ? tile.x + DX[(n3 = tile.build.rotation) & 3] != n || tile.y + DY[n3 & 3] != n2 : block == null || block.name == null || !block.name.contains("router") && !block.name.contains("overflow") && !block.name.contains("underflow") && !block.name.contains("sorter"))) continue;
            return true;
        }
        return false;
    }

    public static boolean tileFree(int n, int n2, boolean[] blArray) {
        if (!Vars.world.tiles.in(n, n2)) {
            return false;
        }
        int n3 = Vars.world.width();
        if (blArray != null && blArray[n2 * n3 + n]) {
            return false;
        }
        int n4 = n2 * n3 + n;
        if (freeCache != null && freeCache[n4] != 0) {
            return freeCache[n4] == 1;
        }
        Tile tile = Vars.world.tile(n, n2);
        boolean bl = false;
        if (tile != null && tile.build == null) {
            boolean bl2 = bl = Build.validPlace((Block)AutoConnectConveyor.resolveConveyor(), (Team)Vars.player.team(), (int)n, (int)n2, (int)0) && !AutoConnectConveyor.fedByNeighbor(n, n2) && (!AutoDrill.limitOn || !AutoDrill.nextToDrill(n, n2));
        }
        if (freeCache != null) {
            AutoConnectConveyor.freeCache[n4] = (byte)(bl ? 1 : 2);
        }
        return bl;
    }

    private static boolean crossable(int n, int n2, int n3, boolean[] blArray) {
        int n4 = Vars.world.width();
        if (blArray != null && blArray[n2 * n4 + n]) {
            return false;
        }
        Tile tile = Vars.world.tile(n, n2);
        if (tile == null || tile.build == null || tile.build.team != Vars.player.team()) {
            return false;
        }
        Block block = tile.build.block;
        if (block instanceof Junction) {
            return true;
        }
        if (!(block instanceof Conveyor)) {
            return false;
        }
        int n5 = tile.build.rotation & 3;
        if ((n5 & 1) == (n3 & 1)) {
            return false;
        }
        Tile tile2 = Vars.world.tile(n + DX[n5], n2 + DY[n5]);
        if (tile2 == null || tile2.build == null) {
            return false;
        }
        for (int i = 1; i <= 3; i += 2) {
            int n6;
            int n7 = n5 + i & 3;
            Tile tile3 = Vars.world.tile(n + DX[n7], n2 + DY[n7]);
            if (tile3 == null || tile3.build == null || !(tile3.build.block instanceof Conveyor) || tile3.x + DX[n6 = tile3.build.rotation & 3] != n || tile3.y + DY[n6] != n2) continue;
            return false;
        }
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Seq<BuildPlan> planRoute(int n, int n2, int n3, boolean[] blArray) {
        lastJunctions = 0;
        Block block = AutoConnectConveyor.resolveConveyor();
        Block block2 = AutoConnectConveyor.resolveJunction();
        if (block == null || block2 == null || !Vars.world.tiles.in(n, n2)) {
            return null;
        }
        int n4 = Vars.world.width();
        int n5 = Vars.world.height();
        freeCache = new byte[n4 * n5];
        try {
            lastBridges = 0;
            Seq<BuildPlan> seq = AutoConnectConveyor.planRouteInner(n, n2, n3, blArray, block, block2, n4, n5, false);
            int n6 = lastJunctions;
            if (seq != null && seq.size <= 40 || Blocks.itemBridge == null) {
                Seq<BuildPlan> seq2 = seq;
                return seq2;
            }
            lastJunctions = 0;
            lastBridges = 0;
            Seq<BuildPlan> seq3 = AutoConnectConveyor.planRouteInner(n, n2, n3, blArray, block, block2, n4, n5, true);
            if (seq3 != null && (seq == null || seq3.size < seq.size)) {
                Seq<BuildPlan> seq4 = seq3;
                return seq4;
            }
            lastJunctions = n6;
            lastBridges = 0;
            Seq<BuildPlan> seq5 = seq;
            return seq5;
        }
        finally {
            freeCache = null;
        }
    }

    private static Seq<BuildPlan> planRouteInner(int n, int n2, int n3, boolean[] blArray, Block block, Block block2, int n4, int n5, boolean bl) {
        int n6;
        int n7;
        int n8;
        int n9;
        int n10;
        int n11;
        int n12;
        int n13;
        int n14;
        int n15;
        if (!AutoConnectConveyor.tileFree(n, n2, blArray)) {
            return null;
        }
        int n16 = n4 * n5 * 4;
        int[] nArray = new int[n16];
        int[] nArray2 = new int[n16];
        Arrays.fill(nArray, Integer.MAX_VALUE);
        Arrays.fill(nArray2, -1);
        byte[] byArray = new byte[n16];
        int n17 = bl ? ((ItemBridge)Blocks.itemBridge).range : 0;
        PriorityQueue<Long> priorityQueue = new PriorityQueue<Long>();
        for (n15 = 0; n15 < 4; ++n15) {
            if (n3 >= 0 && n15 != n3) continue;
            n14 = (n2 * n4 + n) * 4 + n15;
            nArray[n14] = 0;
            priorityQueue.add(Long.valueOf(n14));
        }
        n15 = Integer.MAX_VALUE;
        n14 = -1;
        int n18 = 0;
        while (!priorityQueue.isEmpty()) {
            int n19;
            int n20;
            long l = (Long)priorityQueue.poll();
            int n21 = (int)(l >>> 32);
            if (n21 > nArray[n13 = (int)(l & 0xFFFFFFFFL)]) continue;
            if (n21 >= n15) break;
            n12 = n13 & 3;
            n11 = n13 >> 2;
            n10 = n11 % n4;
            n9 = n11 / n4;
            Tile tile = Vars.world.tile(n10, n9);
            boolean bl2 = tile != null && tile.build != null;
            for (n8 = 0; n8 < 4; ++n8) {
                if (bl2 && n8 != n12 || !bl2 && n8 == (n12 + 2 & 3) || !Vars.world.tiles.in(n7 = n10 + DX[n8], n20 = n9 + DY[n8])) continue;
                n19 = n8 != n12 ? 1 : 0;
                Tile tile2 = Vars.world.tile(n7, n20);
                if (tile2 == null) continue;
                if (!bl2 && tile2.build != null && AutoConnectConveyor.isCoreNetworkStorage(tile2.build)) {
                    n6 = n21 + n19;
                    if (n6 >= n15) continue;
                    n15 = n6;
                    n14 = n13;
                    n18 = n8;
                    continue;
                }
                if (AutoConnectConveyor.tileFree(n7, n20, blArray)) {
                    n6 = n21 + 1 + n19;
                } else {
                    if (!AutoConnectConveyor.crossable(n7, n20, n8, blArray)) continue;
                    n6 = n21 + 6 + n19;
                }
                int n22 = (n20 * n4 + n7) * 4 + n8;
                if (n6 >= nArray[n22]) continue;
                nArray[n22] = n6;
                nArray2[n22] = n13;
                byArray[n22] = 0;
                priorityQueue.add((long)n6 << 32 | (long)n22);
            }
            if (!bl || bl2) continue;
            for (n8 = 0; n8 < 4; ++n8) {
                if (n8 == (n12 + 2 & 3)) continue;
                for (n6 = 2; n6 <= n17; ++n6) {
                    int n23;
                    n20 = n10 + DX[n8] * n6;
                    n7 = n9 + DY[n8] * n6;
                    if (!Vars.world.tiles.in(n20, n7) || !AutoConnectConveyor.tileFree(n20, n7, blArray) || (n19 = n21 + 8 + n6 + (n8 != n12 ? 1 : 0)) >= nArray[n23 = (n7 * n4 + n20) * 4 + n8]) continue;
                    nArray[n23] = n19;
                    nArray2[n23] = n13;
                    byArray[n23] = (byte)n6;
                    priorityQueue.add((long)n19 << 32 | (long)n23);
                }
            }
        }
        if (n14 < 0) {
            return null;
        }
        IntSeq intSeq = new IntSeq();
        int n24 = n14;
        while (n24 != -1) {
            intSeq.add(n24);
            if (intSeq.size > n16) {
                return null;
            }
            n24 = nArray2[n24];
        }
        n24 = intSeq.size;
        Seq seq = new Seq();
        for (n13 = 0; n13 < n24; ++n13) {
            n12 = intSeq.get(n24 - 1 - n13);
            n11 = n12 >> 2;
            n10 = n11 % n4;
            n9 = n11 / n4;
            int n25 = n13 + 1 < n24 ? intSeq.get(n24 - 2 - n13) & 3 : n18;
            Tile tile = Vars.world.tile(n10, n9);
            n8 = n13 + 1 < n24 ? byArray[intSeq.get(n24 - 2 - n13)] : 0;
            int n26 = n6 = byArray[n12] > 0 ? 1 : 0;
            if (tile.build == null && (n8 > 0 || n6 != 0)) {
                BuildPlan buildPlan = new BuildPlan(n10, n9, 0, Blocks.itemBridge);
                if (n8 > 0) {
                    n7 = n25 & 3;
                    buildPlan.config = new Point2(DX[n7] * n8, DY[n7] * n8);
                }
                seq.add((Object)buildPlan);
                if (n8 <= 0) continue;
                ++lastBridges;
                continue;
            }
            if (tile.build != null) {
                if (tile.build.block instanceof Junction) continue;
                seq.add((Object)new BuildPlan(n10, n9, 0, block2));
                ++lastJunctions;
                continue;
            }
            seq.add((Object)new BuildPlan(n10, n9, n25, block));
        }
        return seq;
    }

    public static Seq<BuildPlan> sanitize(Seq<BuildPlan> seq) {
        Seq seq2 = new Seq();
        Block block = AutoConnectConveyor.resolveJunction();
        for (int i = 0; i < seq.size; ++i) {
            Tile tile;
            BuildPlan buildPlan = (BuildPlan)seq.get(i);
            if (buildPlan.block instanceof Conveyor && !buildPlan.breaking && (tile = Vars.world.tile(buildPlan.x, buildPlan.y)) != null && tile.build != null && tile.build.block != null) {
                Block block2 = tile.build.block;
                if (!(block2 instanceof Conveyor) || block == null || (tile.build.rotation & 1) == (buildPlan.rotation & 1)) continue;
                seq2.add((Object)new BuildPlan(buildPlan.x, buildPlan.y, 0, block));
                continue;
            }
            seq2.add((Object)buildPlan);
        }
        return seq2;
    }

    private static int connectInner() {
        int n = LunariansSettings.conveyorStartX;
        int n2 = LunariansSettings.conveyorStartY;
        if (n == Integer.MIN_VALUE || n2 == Integer.MIN_VALUE) {
            AutoConnectConveyor.toast(LunariansI18n.get("cv_set_start"));
            return 0;
        }
        if (AutoConnectConveyor.resolveConveyor() == null) {
            AutoConnectConveyor.toast(LunariansI18n.get("cv_invalid"));
            return 0;
        }
        Unit unit = Vars.player.unit();
        if (unit == null || !unit.canBuild()) {
            AutoConnectConveyor.toast(LunariansI18n.get("cv_need_builder"));
            return 0;
        }
        boolean[] blArray = AutoConnectConveyor.reservedFromPlans(unit);
        if (!AutoConnectConveyor.tileFree(n, n2, blArray)) {
            AutoConnectConveyor.toast(LunariansI18n.get("cv_start_blocked"));
            return 0;
        }
        Seq<BuildPlan> seq = AutoConnectConveyor.planRoute(n, n2, -1, blArray);
        if (seq == null || seq.isEmpty()) {
            AutoConnectConveyor.toast(LunariansI18n.get("cv_no_path"));
            return 0;
        }
        for (int i = 0; i < seq.size; ++i) {
            unit.addBuild((BuildPlan)seq.get(i), true);
        }
        AutoConnectConveyor.toast(LunariansI18n.get("cv_planned", String.valueOf(seq.size), String.valueOf(lastJunctions)));
        return seq.size;
    }

    private static void toast(String string) {
        if (Vars.ui != null) {
            Vars.ui.showInfoToast(string, 3.0f);
        }
    }
}

