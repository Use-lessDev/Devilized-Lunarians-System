/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.math.Mathf
 *  arc.struct.IntQueue
 *  arc.struct.IntSeq
 *  arc.struct.Seq
 *  arc.util.Log
 *  mindustry.Vars
 *  mindustry.content.Blocks
 *  mindustry.entities.units.BuildPlan
 *  mindustry.gen.Unit
 *  mindustry.type.Item
 *  mindustry.world.Block
 *  mindustry.world.Tile
 *  mindustry.world.blocks.distribution.Conveyor
 *  mindustry.world.blocks.environment.Floor
 *  mindustry.world.blocks.production.Drill
 *  mindustry.world.blocks.production.GenericCrafter
 *  mindustry.world.blocks.storage.CoreBlock
 *  mindustry.world.blocks.storage.Unloader
 */
package lunarians;

import arc.math.Mathf;
import arc.struct.IntQueue;
import arc.struct.IntSeq;
import arc.struct.Seq;
import arc.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lunarians.AutoConnectConveyor;
import lunarians.AutoSetConfig;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
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

public class AutoDrill {
    private static final int[] DX = new int[]{1, 0, -1, 0};
    private static final int[] DY = new int[]{0, 1, 0, -1};
    private static boolean errorLogged;
    public static final IntSeq lastDrillTiles;
    public static int lastCount;
    public static int lastHub;
    public static boolean dryRun;
    public static boolean quiet;
    public static final Seq<BuildPlan> lastPlans;
    public static boolean limitOn;
    public static float limitX;
    public static float limitY;
    public static float limitR;
    public static int isoOverride;
    private static boolean failedNoneFit;

    static boolean inLimit(int n, int n2) {
        return !limitOn || Mathf.dst((float)n, (float)n2, (float)limitX, (float)limitY) <= limitR;
    }

    static boolean crowded(int n, int n2, int n3, int n4) {
        for (int i = n2 - n4; i <= n2 + n3 - 1 + n4; ++i) {
            for (int j = n - n4; j <= n + n3 - 1 + n4; ++j) {
                Tile tile;
                if (!Vars.world.tiles.in(j, i) || (tile = Vars.world.tile(j, i)) == null || tile.build == null || tile.build.block == null || tile.build.block instanceof CoreBlock) continue;
                return true;
            }
        }
        return false;
    }

    static boolean nextToDrill(int n, int n2) {
        for (int i = 0; i < 4; ++i) {
            Tile tile = Vars.world.tile(n + DX[i], n2 + DY[i]);
            if (tile == null || tile.build == null || !(tile.build.block instanceof Drill)) continue;
            return true;
        }
        return false;
    }

    static boolean floorOre(Tile tile) {
        if (tile == null) {
            return false;
        }
        return tile.overlay() == null || tile.overlay().itemDrop == null;
    }

    static boolean inFloor(int n, int n2, int n3, int n4) {
        if (!limitOn) {
            return true;
        }
        if (!AutoDrill.floorOre(Vars.world.tile(n3, n4))) {
            return true;
        }
        return Mathf.dst((float)n, (float)n2, (float)n3, (float)n4) <= Math.max(2.5f, (float)Math.sqrt(AutoSetConfig.floorMax()) * 0.75f + 0.5f);
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
        return AutoDrill.run();
    }

    private static int[] exitOrder(int n) {
        int[] nArray = new int[4];
        nArray[0] = n;
        float f = 0.0f;
        float f2 = 0.0f;
        try {
            if (Vars.player != null && Vars.player.closestCore() != null && LunariansSettings.drillStartX != Integer.MIN_VALUE) {
                f = Vars.player.closestCore().tileX() - LunariansSettings.drillStartX;
                f2 = Vars.player.closestCore().tileY() - LunariansSettings.drillStartY;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        boolean[] blArray = new boolean[4];
        blArray[n] = true;
        for (int i = 1; i < 4; ++i) {
            int n2 = -1;
            float f3 = -3.4028235E38f;
            for (int j = 0; j < 4; ++j) {
                float f4;
                if (blArray[j] || !((f4 = (float)DX[j] * f + (float)DY[j] * f2) > f3)) continue;
                f3 = f4;
                n2 = j;
            }
            blArray[n2] = true;
            nArray[i] = n2;
        }
        return nArray;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int generateWithConveyor() {
        int n;
        int n2;
        if (Vars.headless || Vars.player == null || Vars.state == null || !Vars.state.isGame()) {
            return 0;
        }
        int n3 = LunariansSettings.drillExitDir & 3;
        try {
            int n4;
            Unit unit = Vars.player.unit();
            if (unit == null || !unit.canBuild()) {
                int n5;
                AutoDrill.toast(LunariansI18n.get("cv_need_builder"));
                boolean bl = false;
                boolean bl2 = false;
                int n6 = 0;
                int n7 = n5 = 0;
                return n7;
            }
            boolean[] blArray = AutoConnectConveyor.reservedFromPlans(unit);
            int[] nArray = AutoDrill.exitOrder(n3);
            n2 = Vars.world.width();
            for (n = 0; n < nArray.length; ++n) {
                int n8;
                int n9;
                int n10;
                Seq<BuildPlan> seq;
                int n11;
                int n12;
                int n13;
                int n14;
                LunariansSettings.drillExitDir = n14 = nArray[n];
                dryRun = true;
                try {
                    n13 = AutoDrill.generateInner();
                }
                finally {
                    dryRun = false;
                }
                if (n13 <= 0) {
                    int n15;
                    boolean bl = false;
                    n12 = 0;
                    int n16 = 0;
                    int n17 = n15 = 0;
                    return n17;
                }
                if (lastHub < 0) continue;
                boolean[] blArray2 = (boolean[])blArray.clone();
                for (n11 = 0; n11 < AutoDrill.lastPlans.size; ++n11) {
                    AutoConnectConveyor.markPlan((BuildPlan)lastPlans.get(n11), blArray2);
                }
                n11 = lastHub % n2 + DX[n14];
                n12 = lastHub / n2 + DY[n14];
                if (!Vars.world.tiles.in(n11, n12) || (seq = AutoConnectConveyor.planRoute(n11, n12, n14, blArray2)) == null) continue;
                for (n10 = 0; n10 < AutoDrill.lastPlans.size; ++n10) {
                    unit.addBuild((BuildPlan)lastPlans.get(n10), true);
                }
                for (n10 = 0; n10 < seq.size; ++n10) {
                    unit.addBuild((BuildPlan)seq.get(n10), true);
                }
                if (!quiet) {
                    AutoDrill.toast(LunariansI18n.get("dr_summary", String.valueOf(n13), String.valueOf(seq.size)));
                }
                int n18 = n10 = n13;
                int n19 = n9 = (n8 = n10);
                return n19;
            }
            if (!quiet) {
                AutoDrill.toast(LunariansI18n.get("cv_no_path"));
            }
            n = 0;
            boolean bl = false;
            int n20 = 0;
            int n21 = n4 = 0;
            return n21;
        }
        catch (Throwable throwable) {
            int n22;
            if (!errorLogged) {
                errorLogged = true;
                Log.err((String)"[Lunarians] AutoDrill+Conveyor error", (Throwable)throwable);
            }
            boolean bl = false;
            n = 0;
            int n23 = 0;
            n2 = n22 = 0;
            return n2;
        }
        finally {
            LunariansSettings.drillExitDir = n3;
            dryRun = false;
        }
    }

    private static int run() {
        if (Vars.headless || Vars.player == null || Vars.state == null || !Vars.state.isGame()) {
            return 0;
        }
        try {
            return AutoDrill.generateInner();
        }
        catch (Throwable throwable) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err((String)"[Lunarians] AutoDrill error", (Throwable)throwable);
            }
            return 0;
        }
    }

    private static Block resolveDrill() {
        Block block;
        String string = LunariansSettings.drillBlock;
        if (string == null || string.isEmpty()) {
            string = "mechanical-drill";
        }
        if ((block = Vars.content.block(string)) instanceof Drill) {
            return block;
        }
        return Blocks.mechanicalDrill;
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

    private static Item oreAt(Tile tile) {
        if (tile == null) {
            return null;
        }
        try {
            if (tile.drop() != null) {
                return tile.drop();
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        Floor floor = tile.overlay() != null ? tile.overlay() : tile.floor();
        Floor floor2 = floor;
        if (floor != null && floor.itemDrop != null) {
            return floor.itemDrop;
        }
        return null;
    }

    private static boolean sameOre(Tile tile, Item item) {
        Item item2 = AutoDrill.oreAt(tile);
        return item2 != null && item2 == item;
    }

    private static boolean free(int n, int n2, boolean[] blArray, int n3, int n4) {
        if (!Vars.world.tiles.in(n, n2)) {
            return false;
        }
        if (blArray[n2 * n3 + n]) {
            return false;
        }
        Tile tile = Vars.world.tile(n, n2);
        if (tile == null || tile.solid()) {
            return false;
        }
        return tile.build == null || tile.build.block == null || !tile.build.block.solid;
    }

    private static boolean freeConv(int n, int n2, boolean[] blArray, int n3, int n4) {
        if (!Vars.world.tiles.in(n, n2)) {
            return false;
        }
        if (blArray[n2 * n3 + n]) {
            return false;
        }
        Tile tile = Vars.world.tile(n, n2);
        if (tile == null) {
            return false;
        }
        if (tile.build != null && tile.build.block != null && tile.build.block.solid) {
            return false;
        }
        if (limitOn && (tile.build != null || AutoDrill.nextToDrill(n, n2))) {
            return false;
        }
        return tile.block() == null || !tile.block().solid || !tile.block().isStatic();
    }

    private static int generateInner() {
        failedNoneFit = false;
        int n = AutoDrill.generateInner2(true);
        if (n <= 0 && failedNoneFit) {
            failedNoneFit = false;
            n = AutoDrill.generateInner2(false);
        }
        return n;
    }

    private static int generateInner2(boolean bl) {
        int n;
        int n2;
        int n3;
        int[] nArray;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        int n10;
        Object object;
        int n11;
        int n12;
        int n13;
        int n14;
        int n15;
        int n16;
        int n17;
        int n18;
        int n19;
        int n20;
        int n21;
        lastHub = -1;
        int n22 = LunariansSettings.drillStartX;
        int n23 = LunariansSettings.drillStartY;
        if (n22 == Integer.MIN_VALUE || n23 == Integer.MIN_VALUE) {
            AutoDrill.toast(LunariansI18n.get("dr_set_ore"));
            return 0;
        }
        Tile tile = Vars.world.tile(n22, n23);
        Item item = AutoDrill.oreAt(tile);
        if (item == null) {
            AutoDrill.toast(LunariansI18n.get("dr_no_ore"));
            return 0;
        }
        Block block = AutoDrill.resolveDrillFor(item);
        Block block2 = AutoDrill.resolveConveyor();
        if (!(block instanceof Drill) || block2 == null) {
            AutoDrill.toast(LunariansI18n.get("dr_invalid"));
            return 0;
        }
        if (((Drill)block).tier < item.hardness) {
            AutoDrill.toast(AutoDrill.pt() ? "Nenhuma drill dispon\u00edvel consegue minerar " + item.localizedName : "No available drill can mine " + item.localizedName);
            return 0;
        }
        int n24 = block.size;
        int n25 = Math.max(16, LunariansSettings.drillMaxTiles);
        if (limitOn && AutoDrill.floorOre(tile)) {
            n25 = Math.min(n25, AutoSetConfig.floorMax());
        }
        int n26 = n21 = Math.max(1, LunariansSettings.drillMinOres);
        int n27 = (n24 * n24 + 1) / 2;
        boolean bl2 = false;
        if (limitOn && bl && n27 > n21) {
            n26 = n27;
            bl2 = true;
        }
        int n28 = LunariansSettings.drillExitDir & 3;
        boolean bl3 = LunariansSettings.drillLayoutMode == 1;
        Unit unit = Vars.player.unit();
        if (unit == null || !unit.canBuild()) {
            AutoDrill.toast(LunariansI18n.get("cv_need_builder"));
            return 0;
        }
        int n29 = Vars.world.width();
        int n30 = Vars.world.height();
        boolean[] blArray = new boolean[n29 * n30];
        IntSeq intSeq = new IntSeq();
        IntQueue intQueue = new IntQueue();
        int n31 = n23 * n29 + n22;
        intQueue.addLast(n31);
        blArray[n31] = true;
        while (!intQueue.isEmpty() && intSeq.size < n25) {
            n20 = intQueue.removeFirst();
            intSeq.add(n20);
            n19 = n20 % n29;
            n18 = n20 / n29;
            for (n17 = 0; n17 < 4; ++n17) {
                n16 = n19 + DX[n17];
                n15 = n18 + DY[n17];
                if (!Vars.world.tiles.in(n16, n15) || blArray[n14 = n15 * n29 + n16] || !AutoDrill.sameOre(Vars.world.tile(n16, n15), item) || !AutoDrill.inLimit(n16, n15) || !AutoDrill.inFloor(n16, n15, n22, n23)) continue;
                blArray[n14] = true;
                intQueue.addLast(n14);
            }
        }
        if (intSeq.isEmpty()) {
            AutoDrill.toast(LunariansI18n.get("dr_empty"));
            return 0;
        }
        n20 = Integer.MAX_VALUE;
        n19 = Integer.MAX_VALUE;
        n18 = 0;
        n17 = 0;
        for (n16 = 0; n16 < intSeq.size; ++n16) {
            n15 = intSeq.get(n16);
            n14 = n15 % n29;
            n13 = n15 / n29;
            n20 = Math.min(n20, n14);
            n19 = Math.min(n19, n13);
            n18 = Math.max(n18, n14);
            n17 = Math.max(n17, n13);
        }
        boolean[] blArray2 = limitOn ? AutoConnectConveyor.reservedFromPlans(unit) : new boolean[n29 * n30];
        boolean[] blArray3 = new boolean[n29 * n30];
        Seq<BuildPlan> seq = new Seq<BuildPlan>();
        lastDrillTiles.clear();
        n13 = 0;
        IntSeq intSeq2 = new IntSeq();
        boolean bl4 = n28 == 1 || n28 == 3;
        int n32 = 0;
        int n33 = 0;
        if (LunariansSettings.drillPerfect) {
            int n34 = -1;
            n12 = Integer.MAX_VALUE;
            for (n11 = 0; n11 <= n24; ++n11) {
                for (int i = 0; i <= n24; ++i) {
                    object = AutoDrill.scanGrid(bl4, bl3, n11, i, n24, n20, n19, n18, n17, n26, item, blArray, (boolean[])blArray2.clone(), (boolean[])blArray3.clone(), n29, n30, block, (Seq<BuildPlan>)new Seq(), new IntSeq(), new IntSeq());
                    if (object[0] == 0 || object[1] <= n34 && (object[1] != n34 || object[0] >= n12)) continue;
                    n34 = object[1];
                    n12 = object[0];
                    n32 = n11;
                    n33 = i;
                }
            }
        }
        int[] nArray2 = AutoDrill.scanGrid(bl4, bl3, n32, n33, n24, n20, n19, n18, n17, n26, item, blArray, blArray2, blArray3, n29, n30, block, seq, lastDrillTiles, intSeq2);
        n13 = nArray2[0];
        if (LunariansSettings.drillPerfect && n13 > 0) {
            n13 += AutoDrill.fillGaps(bl4, n24, n26, item, blArray, blArray2, blArray3, n29, n30, block, seq, lastDrillTiles, intSeq2);
        }
        if (n13 == 0) {
            if (bl2) {
                failedNoneFit = true;
                return 0;
            }
            AutoDrill.toast(LunariansI18n.get("dr_none_fit"));
            return 0;
        }
        int n35 = Integer.MAX_VALUE;
        int n36 = Integer.MAX_VALUE;
        int n37 = 0;
        int n38 = 0;
        for (n10 = 0; n10 < AutoDrill.lastDrillTiles.size; ++n10) {
            n9 = lastDrillTiles.get(n10);
            n8 = n9 % n29;
            n7 = n9 / n29;
            n35 = Math.min(n35, n8);
            n36 = Math.min(n36, n7);
            n37 = Math.max(n37, n8 + n24 - 1);
            n38 = Math.max(n38, n7 + n24 - 1);
        }
        n10 = 0;
        if (n28 == 1 || n28 == 3) {
            n6 = n35 - 1;
            n5 = n37 + 1;
            if (n6 >= 0 && !intSeq2.contains(n6)) {
                intSeq2.add(n6);
            }
            if (n5 < n29 && !intSeq2.contains(n5)) {
                intSeq2.add(n5);
            }
        } else {
            n6 = n36 - 1;
            n5 = n38 + 1;
            if (n6 >= 0 && !intSeq2.contains(n6)) {
                intSeq2.add(n6);
            }
            if (n5 < n30 && !intSeq2.contains(n5)) {
                intSeq2.add(n5);
            }
        }
        if (n28 == 1 || n28 == 3) {
            for (n9 = 0; n9 < intSeq2.size; ++n9) {
                n8 = intSeq2.get(n9);
                for (n7 = n36; n7 <= n38; ++n7) {
                    if (!AutoDrill.freeConv(n8, n7, blArray2, n29, n30)) continue;
                    seq.add((Object)new BuildPlan(n8, n7, n28, block2));
                    blArray2[n7 * n29 + n8] = true;
                    ++n10;
                }
            }
        } else {
            for (n9 = 0; n9 < intSeq2.size; ++n9) {
                n8 = intSeq2.get(n9);
                for (n7 = n35; n7 <= n37; ++n7) {
                    if (!AutoDrill.freeConv(n7, n8, blArray2, n29, n30)) continue;
                    seq.add((Object)new BuildPlan(n7, n8, n28, block2));
                    blArray2[n8 * n29 + n7] = true;
                    ++n10;
                }
            }
        }
        for (n9 = 0; n9 < AutoDrill.lastDrillTiles.size; ++n9) {
            n8 = lastDrillTiles.get(n9);
            n7 = n8 % n29;
            nArray = AutoDrill.pickFace(n7, n4 = n8 / n29, n24, n28, intSeq2, blArray2, blArray3, n29, n30);
            if (nArray == null) continue;
            n3 = nArray[0];
            n5 = nArray[1];
            n12 = nArray[2];
            if (!AutoDrill.freeConv(n3, n5, blArray2, n29, n30)) continue;
            seq.add((Object)new BuildPlan(n3, n5, n12, block2));
            blArray2[n5 * n29 + n3] = true;
            ++n10;
        }
        block11: for (n9 = 0; n9 < AutoDrill.lastDrillTiles.size; ++n9) {
            n8 = lastDrillTiles.get(n9);
            n7 = n8 % n29;
            if (AutoDrill.hasAdjConv(n7, n4 = n8 / n29, n24, blArray2, n29)) continue;
            n6 = 0;
            int[][] nArrayArray = new int[1][];
            if (n28 == 0) {
                var53_40 = new int[3];
                var53_40[0] = n7 + n24;
                var53_40[1] = n4;
                nArray = var53_40;
                var53_40[2] = 0;
            } else if (n28 == 1) {
                var53_40 = new int[3];
                var53_40[0] = n7;
                var53_40[1] = n4 + n24;
                nArray = var53_40;
                var53_40[2] = 1;
            } else if (n28 == 2) {
                var53_40 = new int[3];
                var53_40[0] = n7 - 1;
                var53_40[1] = n4;
                nArray = var53_40;
                var53_40[2] = 2;
            } else {
                var53_40 = new int[3];
                var53_40[0] = n7;
                var53_40[1] = n4 - 1;
                nArray = var53_40;
                var53_40[2] = 3;
            }
            nArrayArray[0] = nArray;
            IntSeq intSeq3 = new IntSeq();
            object = new IntSeq();
            IntSeq intSeq4 = new IntSeq();
            for (n2 = 0; n2 < n24; ++n2) {
                if (n28 == 0) {
                    intSeq3.add(n7 + n24);
                    object.add(n4 + n2);
                    intSeq4.add(0);
                    continue;
                }
                if (n28 == 1) {
                    intSeq3.add(n7 + n2);
                    object.add(n4 + n24);
                    intSeq4.add(1);
                    continue;
                }
                if (n28 == 2) {
                    intSeq3.add(n7 - 1);
                    object.add(n4 + n2);
                    intSeq4.add(2);
                    continue;
                }
                intSeq3.add(n7 + n2);
                object.add(n4 - 1);
                intSeq4.add(3);
            }
            for (n2 = 0; n2 < n24; ++n2) {
                intSeq3.add(n7 + n24);
                object.add(n4 + n2);
                intSeq4.add(0);
                intSeq3.add(n7 - 1);
                object.add(n4 + n2);
                intSeq4.add(2);
                intSeq3.add(n7 + n2);
                object.add(n4 + n24);
                intSeq4.add(1);
                intSeq3.add(n7 + n2);
                object.add(n4 - 1);
                intSeq4.add(3);
            }
            for (n2 = 0; n2 < intSeq3.size; ++n2) {
                n = intSeq3.get(n2);
                int n39 = object.get(n2);
                int n40 = intSeq4.get(n2);
                if (!AutoDrill.freeConv(n, n39, blArray2, n29, n30)) continue;
                seq.add((Object)new BuildPlan(n, n39, n40, block2));
                blArray2[n39 * n29 + n] = true;
                ++n10;
                n6 = 1;
                continue block11;
            }
        }
        for (n9 = 0; n9 < AutoDrill.lastDrillTiles.size; ++n9) {
            n8 = lastDrillTiles.get(n9);
            n7 = n8 % n29;
            n4 = n8 / n29;
            if (n28 == 1 || n28 == 3) {
                for (n6 = 0; n6 < n24; ++n6) {
                    n5 = n7 + n24;
                    n12 = n4 + n6;
                    if (AutoDrill.freeConv(n5, n12, blArray2, n29, n30)) {
                        n11 = 0;
                        seq.add((Object)new BuildPlan(n5, n12, n11, block2));
                        blArray2[n12 * n29 + n5] = true;
                        ++n10;
                    }
                    if (!AutoDrill.freeConv(n5 = n7 - 1, n12 = n4 + n6, blArray2, n29, n30)) continue;
                    seq.add((Object)new BuildPlan(n5, n12, 2, block2));
                    blArray2[n12 * n29 + n5] = true;
                    ++n10;
                }
                continue;
            }
            for (n6 = 0; n6 < n24; ++n6) {
                n5 = n7 + n6;
                n12 = n4 + n24;
                if (AutoDrill.freeConv(n5, n12, blArray2, n29, n30)) {
                    seq.add((Object)new BuildPlan(n5, n12, 1, block2));
                    blArray2[n12 * n29 + n5] = true;
                    ++n10;
                }
                if (!AutoDrill.freeConv(n5 = n7 + n6, n12 = n4 - 1, blArray2, n29, n30)) continue;
                seq.add((Object)new BuildPlan(n5, n12, 3, block2));
                blArray2[n12 * n29 + n5] = true;
                ++n10;
            }
        }
        if (n28 == 1 || n28 == 3) {
            for (n9 = 0; n9 < AutoDrill.lastDrillTiles.size; ++n9) {
                int n41;
                n8 = lastDrillTiles.get(n9);
                n7 = n8 % n29;
                n4 = n8 / n29;
                n6 = Integer.MIN_VALUE;
                n5 = Integer.MAX_VALUE;
                for (n41 = 0; n41 < intSeq2.size; ++n41) {
                    n11 = intSeq2.get(n41);
                    n12 = Math.abs(n11 - (n7 + n24 / 2));
                    if (n12 >= n5) continue;
                    n5 = n12;
                    n6 = n11;
                }
                if (n6 == Integer.MIN_VALUE) continue;
                block20: for (n41 = 0; n41 < n24; ++n41) {
                    n11 = n4 + n41;
                    n12 = n6 > n7 ? n7 + n24 : n7 - 1;
                    n2 = n6 > n7 ? 1 : -1;
                    for (n = n12; n != n6 && AutoDrill.freeConv(n, n11, blArray2, n29, n30); n += n2) {
                        seq.add((Object)new BuildPlan(n, n11, n2 > 0 ? 0 : 2, block2));
                        blArray2[n11 * n29 + n] = true;
                        ++n10;
                        if (Math.abs(n - n12) > 6) continue block20;
                    }
                }
            }
        } else {
            for (n9 = 0; n9 < AutoDrill.lastDrillTiles.size; ++n9) {
                int n42;
                n8 = lastDrillTiles.get(n9);
                n7 = n8 % n29;
                n4 = n8 / n29;
                n6 = Integer.MIN_VALUE;
                n5 = Integer.MAX_VALUE;
                for (n42 = 0; n42 < intSeq2.size; ++n42) {
                    n11 = intSeq2.get(n42);
                    n12 = Math.abs(n11 - (n4 + n24 / 2));
                    if (n12 >= n5) continue;
                    n5 = n12;
                    n6 = n11;
                }
                if (n6 == Integer.MIN_VALUE) continue;
                block24: for (n42 = 0; n42 < n24; ++n42) {
                    n11 = n7 + n42;
                    n12 = n6 > n4 ? n4 + n24 : n4 - 1;
                    n2 = n6 > n4 ? 1 : -1;
                    for (n = n12; n != n6 && AutoDrill.freeConv(n11, n, blArray2, n29, n30); n += n2) {
                        seq.add((Object)new BuildPlan(n11, n, n2 > 0 ? 1 : 3, block2));
                        blArray2[n * n29 + n11] = true;
                        ++n10;
                        if (Math.abs(n - n12) > 6) continue block24;
                    }
                }
            }
        }
        n9 = (n35 + n37) / 2;
        n8 = (n36 + n38) / 2;
        if ((n28 == 1 || n28 == 3) && intSeq2.size > 0) {
            n7 = intSeq2.get(0);
            n4 = Math.abs(n7 - n9);
            for (n6 = 1; n6 < intSeq2.size; ++n6) {
                n3 = intSeq2.get(n6);
                if (Math.abs(n3 - n9) >= n4) continue;
                n4 = Math.abs(n3 - n9);
                n7 = n3;
            }
            n9 = n7;
        }
        if ((n28 == 0 || n28 == 2) && intSeq2.size > 0) {
            n7 = intSeq2.get(0);
            n4 = Math.abs(n7 - n8);
            for (n6 = 1; n6 < intSeq2.size; ++n6) {
                n3 = intSeq2.get(n6);
                if (Math.abs(n3 - n8) >= n4) continue;
                n4 = Math.abs(n3 - n8);
                n7 = n3;
            }
            n8 = n7;
        }
        if (n28 == 1) {
            n7 = n38 + 1;
            if (n7 < n30) {
                for (n4 = n35; n4 <= n37; ++n4) {
                    if (!AutoDrill.freeConv(n4, n7, blArray2, n29, n30)) continue;
                    n6 = 0;
                    for (n3 = n36; n3 <= n38 && n6 == 0; ++n3) {
                        if (!blArray3[n3 * n29 + n4] && !blArray2[n3 * n29 + n4]) continue;
                        n6 = 1;
                    }
                    if (n6 == 0) continue;
                    n3 = n4 == n9 ? 1 : (n4 < n9 ? 0 : 2);
                    seq.add((Object)new BuildPlan(n4, n7, n3, block2));
                    blArray2[n7 * n29 + n4] = true;
                    ++n10;
                }
                if (AutoDrill.freeConv(n9, n7 + 1, blArray2, n29, n30)) {
                    seq.add((Object)new BuildPlan(n9, n7 + 1, 1, block2));
                    blArray2[(n7 + 1) * n29 + n9] = true;
                    ++n10;
                    lastHub = (n7 + 1) * n29 + n9;
                } else {
                    lastHub = n7 * n29 + n9;
                }
            }
        } else if (n28 == 3) {
            n7 = n36 - 1;
            if (n7 >= 0) {
                for (n4 = n35; n4 <= n37; ++n4) {
                    if (!AutoDrill.freeConv(n4, n7, blArray2, n29, n30)) continue;
                    n6 = 0;
                    for (n3 = n36; n3 <= n38 && n6 == 0; ++n3) {
                        if (!blArray3[n3 * n29 + n4] && !blArray2[n3 * n29 + n4]) continue;
                        n6 = 1;
                    }
                    if (n6 == 0) continue;
                    n3 = n4 == n9 ? 3 : (n4 < n9 ? 0 : 2);
                    seq.add((Object)new BuildPlan(n4, n7, n3, block2));
                    blArray2[n7 * n29 + n4] = true;
                    ++n10;
                }
                if (AutoDrill.freeConv(n9, n7 - 1, blArray2, n29, n30)) {
                    seq.add((Object)new BuildPlan(n9, n7 - 1, 3, block2));
                    blArray2[(n7 - 1) * n29 + n9] = true;
                    ++n10;
                    lastHub = (n7 - 1) * n29 + n9;
                } else {
                    lastHub = n7 * n29 + n9;
                }
            }
        } else if (n28 == 0) {
            n7 = n37 + 1;
            if (n7 < n29) {
                for (n4 = n36; n4 <= n38; ++n4) {
                    if (!AutoDrill.freeConv(n7, n4, blArray2, n29, n30)) continue;
                    n6 = 0;
                    for (n3 = n35; n3 <= n37 && n6 == 0; ++n3) {
                        if (!blArray3[n4 * n29 + n3] && !blArray2[n4 * n29 + n3]) continue;
                        n6 = 1;
                    }
                    if (n6 == 0) continue;
                    n3 = n4 == n8 ? 0 : (n4 < n8 ? 1 : 3);
                    seq.add((Object)new BuildPlan(n7, n4, n3, block2));
                    blArray2[n4 * n29 + n7] = true;
                    ++n10;
                }
                if (AutoDrill.freeConv(n7 + 1, n8, blArray2, n29, n30)) {
                    seq.add((Object)new BuildPlan(n7 + 1, n8, 0, block2));
                    blArray2[n8 * n29 + n7 + 1] = true;
                    ++n10;
                    lastHub = n8 * n29 + n7 + 1;
                } else {
                    lastHub = n8 * n29 + n7;
                }
            }
        } else {
            n7 = n35 - 1;
            if (n7 >= 0) {
                for (n4 = n36; n4 <= n38; ++n4) {
                    if (!AutoDrill.freeConv(n7, n4, blArray2, n29, n30)) continue;
                    n6 = 0;
                    for (n3 = n35; n3 <= n37 && n6 == 0; ++n3) {
                        if (!blArray3[n4 * n29 + n3] && !blArray2[n4 * n29 + n3]) continue;
                        n6 = 1;
                    }
                    if (n6 == 0) continue;
                    n3 = n4 == n8 ? 2 : (n4 < n8 ? 1 : 3);
                    seq.add((Object)new BuildPlan(n7, n4, n3, block2));
                    blArray2[n4 * n29 + n7] = true;
                    ++n10;
                }
                if (AutoDrill.freeConv(n7 - 1, n8, blArray2, n29, n30)) {
                    seq.add((Object)new BuildPlan(n7 - 1, n8, 2, block2));
                    blArray2[n8 * n29 + n7 - 1] = true;
                    ++n10;
                    lastHub = n8 * n29 + n7 - 1;
                } else {
                    lastHub = n8 * n29 + n7;
                }
            }
        }
        if (n28 == 1 || n28 == 3) {
            n7 = n28 == 1 ? n38 + 1 : n36 - 1;
            for (n4 = 0; n4 < intSeq2.size; ++n4) {
                n6 = intSeq2.get(n4);
                if (n28 == 1) {
                    for (n3 = n38; n3 <= n7; ++n3) {
                        if (!AutoDrill.freeConv(n6, n3, blArray2, n29, n30)) continue;
                        seq.add((Object)new BuildPlan(n6, n3, 1, block2));
                        blArray2[n3 * n29 + n6] = true;
                        ++n10;
                    }
                    continue;
                }
                for (n3 = n7; n3 <= n36; ++n3) {
                    if (!AutoDrill.freeConv(n6, n3, blArray2, n29, n30)) continue;
                    seq.add((Object)new BuildPlan(n6, n3, 3, block2));
                    blArray2[n3 * n29 + n6] = true;
                    ++n10;
                }
            }
        } else {
            n7 = n28 == 0 ? n37 + 1 : n35 - 1;
            for (n4 = 0; n4 < intSeq2.size; ++n4) {
                n6 = intSeq2.get(n4);
                if (n28 == 0) {
                    for (n3 = n37; n3 <= n7; ++n3) {
                        if (!AutoDrill.freeConv(n3, n6, blArray2, n29, n30)) continue;
                        seq.add((Object)new BuildPlan(n3, n6, 0, block2));
                        blArray2[n6 * n29 + n3] = true;
                        ++n10;
                    }
                    continue;
                }
                for (n3 = n7; n3 <= n35; ++n3) {
                    if (!AutoDrill.freeConv(n3, n6, blArray2, n29, n30)) continue;
                    seq.add((Object)new BuildPlan(n3, n6, 2, block2));
                    blArray2[n6 * n29 + n3] = true;
                    ++n10;
                }
            }
        }
        AutoDrill.fixRotations(seq, block2, n29);
        seq = AutoDrill.pruneConveyors(seq, block2, n29);
        n10 = 0;
        for (n9 = 0; n9 < seq.size; ++n9) {
            if (((BuildPlan)seq.get((int)n9)).block != block2) continue;
            ++n10;
        }
        seq = AutoConnectConveyor.sanitize(seq);
        lastPlans.clear();
        lastPlans.addAll(seq);
        if (!dryRun) {
            for (n7 = 0; n7 < seq.size; ++n7) {
                BuildPlan buildPlan = (BuildPlan)seq.get(n7);
                if (!(buildPlan.block instanceof Drill)) continue;
                unit.addBuild(buildPlan, true);
            }
            for (n7 = 0; n7 < seq.size; ++n7) {
                BuildPlan buildPlan = (BuildPlan)seq.get(n7);
                if (buildPlan.block instanceof Drill) continue;
                unit.addBuild(buildPlan, true);
            }
        }
        lastCount = n13;
        if (!dryRun && !quiet) {
            AutoDrill.toast(LunariansI18n.get("dr_summary", String.valueOf(n13), String.valueOf(n10)));
        }
        return lastCount;
    }

    private static int[] pickFace(int n, int n2, int n3, int n4, IntSeq intSeq, boolean[] blArray, boolean[] blArray2, int n5, int n6) {
        if (n4 == 1 || n4 == 3) {
            int n7;
            int n8;
            int n9;
            int n10 = Integer.MIN_VALUE;
            int n11 = Integer.MAX_VALUE;
            for (n9 = 0; n9 < intSeq.size; ++n9) {
                n8 = intSeq.get(n9);
                n7 = Math.abs(n8 - (n + n3 / 2));
                if (n7 >= n11) continue;
                n11 = n7;
                n10 = n8;
            }
            if (n10 != Integer.MIN_VALUE) {
                n9 = n10 > n ? n + n3 : n - 1;
                for (n8 = 0; n8 < n3; ++n8) {
                    n7 = n2 + n8;
                    if (!AutoDrill.freeConv(n9, n7, blArray, n5, n6)) continue;
                    int n12 = n10 > n ? 0 : 2;
                    return new int[]{n9, n7, n12};
                }
            }
            for (n9 = 0; n9 < n3; ++n9) {
                int n13;
                if (n4 == 1) {
                    n8 = n + n9;
                    n7 = n2 + n3;
                    n13 = 1;
                } else {
                    n8 = n + n9;
                    n7 = n2 - 1;
                    n13 = 3;
                }
                if (!AutoDrill.freeConv(n8, n7, blArray, n5, n6)) continue;
                return new int[]{n8, n7, n13};
            }
        } else {
            int n14;
            int n15;
            int n16;
            int n17 = Integer.MIN_VALUE;
            int n18 = Integer.MAX_VALUE;
            for (n16 = 0; n16 < intSeq.size; ++n16) {
                n15 = intSeq.get(n16);
                n14 = Math.abs(n15 - (n2 + n3 / 2));
                if (n14 >= n18) continue;
                n18 = n14;
                n17 = n15;
            }
            if (n17 != Integer.MIN_VALUE) {
                n16 = n17 > n2 ? n2 + n3 : n2 - 1;
                for (n15 = 0; n15 < n3; ++n15) {
                    n14 = n + n15;
                    if (!AutoDrill.freeConv(n14, n16, blArray, n5, n6)) continue;
                    int n19 = n17 > n2 ? 1 : 3;
                    return new int[]{n14, n16, n19};
                }
            }
            for (n16 = 0; n16 < n3; ++n16) {
                int n20;
                if (n4 == 0) {
                    n15 = n + n3;
                    n14 = n2 + n16;
                    n20 = 0;
                } else {
                    n15 = n - 1;
                    n14 = n2 + n16;
                    n20 = 2;
                }
                if (!AutoDrill.freeConv(n15, n14, blArray, n5, n6)) continue;
                return new int[]{n15, n14, n20};
            }
        }
        return null;
    }

    private static boolean hasAdjConv(int n, int n2, int n3, boolean[] blArray, int n4) {
        for (int i = 0; i < n3; ++i) {
            for (int j = 0; j < n3; ++j) {
                for (int k = 0; k < 4; ++k) {
                    int n5 = n + j + DX[k];
                    int n6 = n2 + i + DY[k];
                    if (!Vars.world.tiles.in(n5, n6) || n5 >= n && n5 < n + n3 && n6 >= n2 && n6 < n2 + n3 || !blArray[n6 * n4 + n5]) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean touchesDrill(int n, int n2, boolean[] blArray, int n3, int n4) {
        for (int i = 0; i < 4; ++i) {
            int n5 = n + DX[i];
            int n6 = n2 + DY[i];
            if (!Vars.world.tiles.in(n5, n6) || !blArray[n6 * n3 + n5]) continue;
            return true;
        }
        return false;
    }

    private static boolean canPlaceDrill(int n, int n2, int n3, boolean[] blArray, int n4, int n5) {
        if (n < 0 || n2 < 0 || n + n3 > n4 || n2 + n3 > n5) {
            return false;
        }
        for (int i = 0; i < n3; ++i) {
            for (int j = 0; j < n3; ++j) {
                if (blArray[(n2 + i) * n4 + (n + j)]) {
                    return false;
                }
                Tile tile = Vars.world.tile(n + j, n2 + i);
                if (tile == null) {
                    return false;
                }
                if (tile.solid() && tile.build == null) {
                    return false;
                }
                if (tile.build == null || tile.build.block == null || !tile.build.block.solid) continue;
                return false;
            }
        }
        return !limitOn || !AutoDrill.crowded(n, n2, n3, isoOverride >= 0 ? isoOverride : AutoSetConfig.spacing());
    }

    private static int[] scanGrid(boolean bl, boolean bl2, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, Item item, boolean[] blArray, boolean[] blArray2, boolean[] blArray3, int n9, int n10, Block block, Seq<BuildPlan> seq, IntSeq intSeq, IntSeq intSeq2) {
        int n11 = bl ? n4 + n : n5 + n2;
        int n12 = bl ? n5 + n2 : n4 + n;
        int n13 = bl ? n6 : n7;
        int n14 = bl ? n7 : n6;
        int n15 = bl ? n9 : n10;
        int n16 = 0;
        int n17 = 0;
        int n18 = 0;
        while (n11 + n3 - 1 <= n13) {
            int n19;
            boolean bl3 = false;
            int n20 = n12;
            while (n20 + n3 - 1 <= n14) {
                int n21;
                int n22;
                n19 = bl ? n11 : n20;
                int n23 = n22 = bl ? n20 : n11;
                if (AutoDrill.canPlaceDrill(n19, n22, n3, blArray2, n9, n10) && (n21 = AutoDrill.countOre(n19, n22, n3, item, blArray, n9)) >= n8) {
                    seq.add((Object)new BuildPlan(n19 + (n3 - 1) / 2, n22 + (n3 - 1) / 2, 0, block));
                    intSeq.add(n22 * n9 + n19);
                    AutoDrill.mark(n19, n22, n3, blArray2, n9);
                    AutoDrill.mark(n19, n22, n3, blArray3, n9);
                    ++n16;
                    n17 += n21;
                    bl3 = true;
                }
                n20 += n3;
            }
            if (bl3) {
                if (bl2 && ++n18 % 2 != 0) {
                    n11 += n3;
                    continue;
                }
                n19 = n11 + n3;
                if (n19 < n15) {
                    intSeq2.add(n19);
                }
                n11 += n3 + 1;
                continue;
            }
            ++n11;
        }
        return new int[]{n16, n17};
    }

    private static int fillGaps(boolean bl, int n, int n2, Item item, boolean[] blArray, boolean[] blArray2, boolean[] blArray3, int n3, int n4, Block block, Seq<BuildPlan> seq, IntSeq intSeq, IntSeq intSeq2) {
        int n5;
        int n6;
        if (intSeq.size == 0) {
            return 0;
        }
        int n7 = Integer.MAX_VALUE;
        int n8 = Integer.MAX_VALUE;
        int n9 = 0;
        int n10 = 0;
        for (n6 = 0; n6 < intSeq.size; ++n6) {
            n5 = intSeq.get(n6);
            n7 = Math.min(n7, n5 % n3);
            n8 = Math.min(n8, n5 / n3);
            n9 = Math.max(n9, n5 % n3 + n - 1);
            n10 = Math.max(n10, n5 / n3 + n - 1);
        }
        n6 = Math.max(1, Math.min(n2, (n * n + 2) / 3));
        n5 = 0;
        for (int i = 0; i < 64; ++i) {
            int n11 = -1;
            int n12 = -1;
            int n13 = 0;
            int n14 = n8;
            while (n14 + n - 1 <= n10) {
                int n15 = n7;
                while (n15 + n - 1 <= n9) {
                    int n16;
                    int n17 = bl ? n15 : n14;
                    boolean bl2 = false;
                    for (n16 = 0; n16 < intSeq2.size; ++n16) {
                        int n18 = intSeq2.get(n16);
                        if (n18 < n17 || n18 > n17 + n - 1) continue;
                        bl2 = true;
                        break;
                    }
                    if (!bl2 && AutoDrill.canPlaceDrill(n15, n14, n, blArray2, n3, n4) && (n16 = AutoDrill.countOre(n15, n14, n, item, blArray, n3)) > n13) {
                        n13 = n16;
                        n11 = n15;
                        n12 = n14;
                    }
                    ++n15;
                }
                ++n14;
            }
            if (n11 < 0 || n13 < n6) break;
            seq.add((Object)new BuildPlan(n11 + (n - 1) / 2, n12 + (n - 1) / 2, 0, block));
            intSeq.add(n12 * n3 + n11);
            AutoDrill.mark(n11, n12, n, blArray2, n3);
            AutoDrill.mark(n11, n12, n, blArray3, n3);
            ++n5;
        }
        return n5;
    }

    private static int countOre(int n, int n2, int n3, Item item, boolean[] blArray, int n4) {
        int n5 = 0;
        int n6 = 0;
        HashMap<Item, Integer> hashMap = null;
        for (int i = 0; i < n3; ++i) {
            for (int j = 0; j < n3; ++j) {
                int n7 = (n2 + i) * n4 + (n + j);
                Tile tile = Vars.world.tile(n + j, n2 + i);
                Item item2 = AutoDrill.oreAt(tile);
                if (item2 != null) {
                    if (item2 == item) {
                        ++n6;
                    } else if (limitOn) {
                        Integer n8;
                        if (hashMap == null) {
                            hashMap = new HashMap<Item, Integer>();
                        }
                        hashMap.put(item2, (n8 = (Integer)hashMap.get(item2)) == null ? 1 : n8 + 1);
                    }
                }
                if (!blArray[n7] || !AutoDrill.sameOre(tile, item)) continue;
                ++n5;
            }
        }
        if (limitOn && hashMap != null) {
            for (Map.Entry entry : hashMap.entrySet()) {
                Item item3 = (Item)entry.getKey();
                int n9 = (Integer)entry.getValue();
                if (item3.lowPriority && !item.lowPriority) continue;
                if (!item3.lowPriority && item.lowPriority) {
                    return -1;
                }
                if (n9 <= n6 && (n9 != n6 || item3.id <= item.id)) continue;
                return -1;
            }
        }
        return n5;
    }

    private static void mark(int n, int n2, int n3, boolean[] blArray, int n4) {
        for (int i = 0; i < n3; ++i) {
            for (int j = 0; j < n3; ++j) {
                blArray[(n2 + i) * n4 + (n + j)] = true;
            }
        }
    }

    private static void fixRotations(Seq<BuildPlan> seq, Block block, int n) {
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        Object object;
        if (lastHub < 0 || seq == null || seq.isEmpty()) {
            return;
        }
        boolean[] blArray = new boolean[n * Vars.world.height()];
        int[] nArray = new int[n * Vars.world.height()];
        Arrays.fill(nArray, -1);
        for (int i = 0; i < seq.size; ++i) {
            object = seq.get(i);
            if (((BuildPlan)object).block != block || (n8 = ((BuildPlan)object).y * n + ((BuildPlan)object).x) < 0 || n8 >= blArray.length) continue;
            blArray[n8] = true;
            nArray[n8] = i;
        }
        if (!blArray[lastHub]) {
            return;
        }
        int[] nArray2 = new int[blArray.length];
        Arrays.fill(nArray2, -1);
        object = new IntQueue();
        ((IntQueue)object).addLast(lastHub);
        nArray2[AutoDrill.lastHub] = lastHub;
        while (!((IntQueue)object).isEmpty()) {
            n8 = ((IntQueue)object).removeFirst();
            n7 = n8 % n;
            n6 = n8 / n;
            for (n5 = 0; n5 < 4; ++n5) {
                n4 = n7 + DX[n5];
                n3 = n6 + DY[n5];
                if (!Vars.world.tiles.in(n4, n3) || nArray2[n2 = n3 * n + n4] >= 0 || !blArray[n2]) continue;
                nArray2[n2] = n8;
                ((IntQueue)object).addLast(n2);
            }
        }
        for (n8 = 0; n8 < blArray.length; ++n8) {
            if (!blArray[n8] || nArray2[n8] < 0 || nArray2[n8] == n8) continue;
            n7 = nArray2[n8];
            n6 = n8 % n;
            n5 = n8 / n;
            n4 = n7 % n;
            n3 = n7 / n;
            n2 = 0;
            n2 = n4 > n6 ? 0 : (n4 < n6 ? 2 : (n3 > n5 ? 1 : 3));
            int n9 = nArray[n8];
            if (n9 < 0) continue;
            BuildPlan buildPlan = (BuildPlan)seq.get(n9);
            seq.set(n9, (Object)new BuildPlan(buildPlan.x, buildPlan.y, n2, block));
        }
    }

    private static Block resolveDrillFor(Item item) {
        Object object;
        String string;
        Object object2 = null;
        if (limitOn && item != null && (string = AutoSetConfig.drillFor(item.name)) != null && !string.isEmpty() && (object = Vars.content.block(string)) instanceof Drill) {
            object2 = object;
        }
        if (object2 == null) {
            object2 = AutoDrill.resolveDrill();
        }
        if (item != null && object2 instanceof Drill && ((Drill)object2).tier < item.hardness) {
            string = null;
            for (Block block : Vars.content.blocks()) {
                if (!(block instanceof Drill)) continue;
                Drill drill = (Drill)block;
                if (drill.tier < item.hardness || string != null && drill.tier >= ((Drill)string).tier && (drill.tier != ((Drill)string).tier || drill.size >= ((Drill)string).size)) continue;
                string = drill;
            }
            if (string != null) {
                if (!quiet) {
                    AutoDrill.toast(AutoDrill.pt() ? object2.localizedName + " n\u00e3o minera " + item.localizedName + ": usando " + ((Drill)string).localizedName : object2.localizedName + " cannot mine " + item.localizedName + ": using " + ((Drill)string).localizedName);
                }
                object2 = string;
            }
        }
        return object2;
    }

    private static boolean pollutes(Block block) {
        return block instanceof Drill || block instanceof GenericCrafter || block instanceof Unloader;
    }

    private static void markAround(boolean[] blArray, int n, int n2, int n3, int n4) {
        for (int i = 0; i < 4; ++i) {
            int n5 = n + DX[i];
            int n6 = n2 + DY[i];
            if (n5 < 0 || n6 < 0 || n5 >= n3 || n6 >= n4) continue;
            blArray[n6 * n3 + n5] = true;
        }
    }

    public static boolean[] reservedForFeed(Unit unit) {
        boolean[] blArray;
        block7: {
            blArray = AutoConnectConveyor.reservedFromPlans(unit);
            try {
                int n = Vars.world.width();
                int n2 = Vars.world.height();
                for (int i = 0; i < n2; ++i) {
                    for (int j = 0; j < n; ++j) {
                        Tile tile = Vars.world.tile(j, i);
                        if (tile == null || tile.build == null || tile.build.block == null || !AutoDrill.pollutes(tile.build.block)) continue;
                        AutoDrill.markAround(blArray, j, i, n, n2);
                    }
                }
                for (BuildPlan buildPlan : unit.plans()) {
                    if (buildPlan.breaking || buildPlan.block == null || !AutoDrill.pollutes(buildPlan.block)) continue;
                    int n3 = buildPlan.block.size;
                    int n4 = (n3 - 1) / 2;
                    for (int i = 0; i < n3; ++i) {
                        for (int j = 0; j < n3; ++j) {
                            AutoDrill.markAround(blArray, buildPlan.x - n4 + j, buildPlan.y - n4 + i, n, n2);
                        }
                    }
                }
            }
            catch (Throwable throwable) {
                if (errorLogged) break block7;
                errorLogged = true;
                Log.err((String)"[Lunarians] reservedForFeed", (Throwable)throwable);
            }
        }
        return blArray;
    }

    private static Seq<BuildPlan> pruneConveyors(Seq<BuildPlan> seq, Block block, int n) {
        try {
            int n2;
            int n3;
            int n4;
            Object object;
            int n5;
            int n6;
            int n7;
            Object object2;
            int n8;
            int n9;
            int n10;
            if (lastHub < 0 || seq == null || seq.isEmpty()) {
                return seq;
            }
            int n11 = Vars.world.height();
            int[] nArray = new int[n * n11];
            Arrays.fill(nArray, -1);
            ArrayList<BuildPlan> arrayList = new ArrayList<BuildPlan>();
            HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
            int n12 = 0;
            for (n10 = 0; n10 < seq.size; ++n10) {
                BuildPlan buildPlan = (BuildPlan)seq.get(n10);
                if (buildPlan.block == block) {
                    if (buildPlan.x < 0 || buildPlan.y < 0 || buildPlan.x >= n || buildPlan.y >= n11) continue;
                    nArray[buildPlan.y * n + buildPlan.x] = arrayList.size();
                    arrayList.add(buildPlan);
                    continue;
                }
                if (!(buildPlan.block instanceof Drill)) continue;
                int n13 = buildPlan.block.size;
                int n14 = (n13 - 1) / 2;
                for (n9 = 0; n9 < n13; ++n9) {
                    for (n8 = 0; n8 < n13; ++n8) {
                        hashMap.put((buildPlan.y - n14 + n9) * n + (buildPlan.x - n14 + n8), n12);
                    }
                }
                ++n12;
            }
            n10 = arrayList.size();
            if (n10 == 0 || nArray[lastHub] < 0) {
                return seq;
            }
            int n15 = nArray[lastHub];
            int[] nArray2 = new int[n10];
            int[][] nArrayArray = new int[n10][];
            for (n9 = 0; n9 < n10; ++n9) {
                object2 = (BuildPlan)arrayList.get(n9);
                n7 = object2.rotation & 3;
                n6 = object2.x + DX[n7];
                n5 = object2.y + DY[n7];
                nArray2[n9] = n6 < 0 || n5 < 0 || n6 >= n || n5 >= n11 ? -1 : nArray[n5 * n + n6];
                object = new ArrayList();
                for (n8 = 0; n8 < 4; ++n8) {
                    Integer n16;
                    if (n8 == n7) continue;
                    n4 = object2.x + DX[n8];
                    n3 = object2.y + DY[n8];
                    if (n4 < 0 || n3 < 0 || n4 >= n || n3 >= n11 || (n16 = (Integer)hashMap.get(n3 * n + n4)) == null || ((ArrayList)object).contains(n16)) continue;
                    ((ArrayList)object).add(n16);
                }
                nArrayArray[n9] = new int[((ArrayList)object).size()];
                for (n8 = 0; n8 < ((ArrayList)object).size(); ++n8) {
                    nArrayArray[n9][n8] = (Integer)((ArrayList)object).get(n8);
                }
            }
            boolean[] blArray = new boolean[n10];
            block8: for (n2 = 0; n2 < n10; ++n2) {
                n7 = n2;
                for (n6 = 0; n7 >= 0 && n6 <= n10; ++n6) {
                    if (n7 == n15) {
                        blArray[n2] = true;
                        continue block8;
                    }
                    n7 = nArray2[n7];
                }
            }
            n2 = 1;
            while (n2 != 0) {
                int n17;
                n2 = 0;
                object2 = new int[n10];
                for (int i = 0; i < n10; ++i) {
                    if (!blArray[i] || nArray2[i] < 0 || !blArray[nArray2[i]]) continue;
                    Object object3 = object2;
                    n17 = nArray2[i];
                    object3[n17] = object3[n17] + true;
                }
                object = new int[Math.max(1, n12)];
                for (n5 = 0; n5 < n10; ++n5) {
                    if (!blArray[n5]) continue;
                    int[] nArray3 = nArrayArray[n5];
                    n17 = nArray3.length;
                    for (n4 = 0; n4 < n17; ++n4) {
                        int n18 = n3 = nArray3[n4];
                        object[n18] = object[n18] + true;
                    }
                }
                for (n5 = 0; n5 < n10; ++n5) {
                    if (!blArray[n5] || n5 == n15 || object2[n5] > 0) continue;
                    n17 = 0;
                    int[] nArray4 = nArrayArray[n5];
                    n4 = nArray4.length;
                    for (n3 = 0; n3 < n4; ++n3) {
                        int n19 = nArray4[n3];
                        if (object[n19] > true) continue;
                        n17 = 1;
                        break;
                    }
                    if (n17 != 0) continue;
                    blArray[n5] = false;
                    n2 = 1;
                    nArray4 = nArrayArray[n5];
                    n4 = nArray4.length;
                    for (n3 = 0; n3 < n4; ++n3) {
                        int n20;
                        int n21 = n20 = nArray4[n3];
                        object[n21] = object[n21] - true;
                    }
                }
            }
            object2 = new Seq();
            for (int i = 0; i < seq.size; ++i) {
                int n22;
                BuildPlan buildPlan = (BuildPlan)seq.get(i);
                if (buildPlan.block == block && buildPlan.x >= 0 && buildPlan.y >= 0 && buildPlan.x < n && buildPlan.y < n11 && (n22 = nArray[buildPlan.y * n + buildPlan.x]) >= 0 && !blArray[n22]) continue;
                object2.add((Object)buildPlan);
            }
            return object2;
        }
        catch (Throwable throwable) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err((String)"[Lunarians] pruneConveyors", (Throwable)throwable);
            }
            return seq;
        }
    }

    private static boolean pt() {
        return "pt".equals(LunariansSettings.language);
    }

    private static void toast(String string) {
        if (Vars.ui != null) {
            Vars.ui.showInfoToast(string, 3.5f);
        }
    }

    static {
        isoOverride = -1;
        lastPlans = new Seq();
        lastDrillTiles = new IntSeq();
        lastHub = -1;
    }
}

