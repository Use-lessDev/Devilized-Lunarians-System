package lunarians;

import arc.func.Cons;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.struct.Seq;
import arc.util.Log;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Schematic;
import mindustry.game.Schematics;
import mindustry.gen.Unit;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.blocks.power.PowerNode;
import mindustry.world.blocks.storage.CoreBlock;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/SteamDrill.class */
public class SteamDrill {
    static final String B64 = "bXNjaAF4nEWOzUrDQBSFT/NvgnbjQlyFulFowEUrbgW7EKwbFRchi0kzLQNpUidTtYjv4CPUZ3DhW9UnKHonTemFuZfLPec7Awd7FvxRWSheqCGbwXj/gJOzlOcVjuPV8rP7+/XT7cQnl/0eG4+T1fI7TjoJrIJNOY7uFWfT8FqKPA/PL8LBm3oKT3v9MwQZr0ZSzJQoCwTpwn+8fbgZXt0NPLQrbYomvOCSqVJiPxfPc5FFspwrLnGQSpFNeES/yuZCIchZxWWU6RA4jai9E73wBUH8WflKqqLMONytFTikBxNo0bABg4bj/jWlb3D1vVULqJHGIpUJ06ZhudiUBb26O6devfragMyN36wjtJ+CqNmeBhs1sg5eN0RTBxs6bAfVq72FrolmEYN0Ng3jH04dbjI=";
    static final int[] DX = {1, 0, -1, 0};
    static final int[] DY = {0, 1, 0, -1};
    public static final ArrayList<Integer> lastTiles = new ArrayList<>();
    public static String lastError = "";
    private static Schematic schem;

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.SteamDrill$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Cons {
        public final void get(Object obj) {
            SteamDrill.lambda$rotateOnce$0((Point2) obj);
        }
    }

    private static String tr(String str, String str2, String str3, String str4) {
        return AutoSetConfig.tr4(str, str2, str3, str4);
    }

    private static Schematic schematic() {
        if (schem == null) {
            try {
                schem = Schematics.readBase64(B64);
            } catch (Throwable th) {
                Log.err("[Lunarians] steam schematic", th);
            }
        }
        return schem;
    }

    private static void rotateOnce(Seq<BuildPlan> seq, int i, int i2) {
        for (int i3 = 0; i3 < seq.size; i3++) {
            BuildPlan buildPlan = (BuildPlan) seq.get(i3);
            if (!buildPlan.breaking && buildPlan.block != null) {
                float f = buildPlan.block.offset;
                buildPlan.x = Math.round(((-(((buildPlan.y - i2) * 8.0f) + f)) - f) / 8.0f) + i;
                buildPlan.y = Math.round(((((buildPlan.x - i) * 8.0f) + f) - f) / 8.0f) + i2;
                buildPlan.rotation = buildPlan.block.planRotation(Mathf.mod(buildPlan.rotation + 1, 4));
                buildPlan.pointConfig(point2 -> {
                    point2.set(-point2.y, point2.x);
                });
            }
        }
    }

    private static boolean inside(int i, int i2) {
        return Vars.world.tiles.in(i, i2);
    }

    private static void footprint(Block block, int i, int i2, HashSet<Integer> hashSet) {
        int width = Vars.world.width();
        int i3 = (block.size - 1) / 2;
        for (int i4 = 0; i4 < block.size; i4++) {
            for (int i5 = 0; i5 < block.size; i5++) {
                hashSet.add(Integer.valueOf((((i2 - i3) + i4) * width) + (i - i3) + i5));
            }
        }
    }

    private static boolean freeArea(Block block, int i, int i2, boolean[] zArr, HashSet<Integer> hashSet) {
        int width = Vars.world.width();
        int i3 = (block.size - 1) / 2;
        for (int i4 = 0; i4 < block.size; i4++) {
            for (int i5 = 0; i5 < block.size; i5++) {
                int i6 = (i - i3) + i5;
                int i7 = (i2 - i3) + i4;
                if (!inside(i6, i7)) {
                    return false;
                }
                int i8 = (i7 * width) + i6;
                if (zArr[i8]) {
                    return false;
                }
                if (hashSet != null && hashSet.contains(Integer.valueOf(i8))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean canPlace(Block block, int i, int i2, int i3) {
        try {
            return Build.validPlace(block, Vars.player.team(), i, i2, i3);
        } catch (Throwable th) {
            return false;
        }
    }

    private static int waterTiles(int i, int i2) {
        int i3 = 0;
        for (int i4 = 0; i4 < 2; i4++) {
            for (int i5 = 0; i5 < 2; i5++) {
                Tile tile = Vars.world.tile(i + i5, i2 + i4);
                if (tile == null || tile.build != null) {
                    return -1;
                }
                if (tile.floor() != null && tile.floor().liquidDrop == Liquids.water) {
                    i3++;
                }
            }
        }
        return i3;
    }

    private static boolean affordable(Block block, int i) {
        if (Vars.state.rules.infiniteResources) {
            return true;
        }
        CoreBlock.CoreBuild closestCore = Vars.player.closestCore();
        if (closestCore == null) {
            return false;
        }
        for (ItemStack itemStack : block.requirements) {
            if (closestCore.items.get(itemStack.item) < itemStack.amount * i) {
                return false;
            }
        }
        return true;
    }

    public static Seq<BuildPlan> plan(Unit unit, int i, int i2, float f, boolean[] zArr) {
        lastTiles.clear();
        lastError = "";
        try {
            return planInner(unit, i, i2, f, zArr);
        } catch (Throwable th) {
            Log.err("[Lunarians] SteamDrill", th);
            lastError = "error";
            return null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:285:0x082d, code lost:
    
        r32 = r32 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static Seq<BuildPlan> planInner(Unit unit, int i, int i2, float f, boolean[] zArr) {
        String tr;
        int intValue;
        int i3;
        int waterTiles;
        Schematic schematic = schematic();
        Block block = Vars.content.block("laser-drill");
        Block block2 = Vars.content.block("rotary-pump");
        Block block3 = Vars.content.block("conduit");
        if (schematic == null || block == null || block2 == null || block3 == null || Vars.player.closestCore() == null) {
            lastError = tr("Blocos indisponíveis", "Blocks unavailable", "Bloques no disponibles", "Блоки недоступны");
            return null;
        }
        int width = Vars.world.width();
        boolean[] reservedFromPlans = AutoConnectConveyor.reservedFromPlans(unit);
        if (zArr != null) {
            for (int i4 = 0; i4 < zArr.length && i4 < reservedFromPlans.length; i4++) {
                if (zArr[i4]) {
                    reservedFromPlans[i4] = true;
                }
            }
        }
        int tileX = Vars.player.closestCore().tileX();
        int tileY = Vars.player.closestCore().tileY();
        int steamMinCoal = AutoSetConfig.steamMinCoal();
        ArrayList arrayList = new ArrayList();
        int ceil = (int) Math.ceil(f);
        for (int i5 = i2 - ceil; i5 <= i2 + ceil; i5++) {
            for (int i6 = i - ceil; i6 <= i + ceil; i6++) {
                if (inside(i6, i5) && Mathf.dst(i6, i5, i, i2) <= f - 2.0f) {
                    int i7 = 0;
                    boolean z = false;
                    for (int i8 = -1; i8 <= 1 && !z; i8++) {
                        for (int i9 = -1; i9 <= 1; i9++) {
                            Tile tile = Vars.world.tile(i6 + i9, i5 + i8);
                            if (tile == null || tile.build != null || reservedFromPlans[((i5 + i8) * width) + i6 + i9]) {
                                z = true;
                                break;
                            }
                            if (tile.drop() == Items.coal) {
                                i7++;
                            }
                        }
                    }
                    if (!z && i7 >= steamMinCoal) {
                        arrayList.add(new int[]{i6, i5, i7, (int) (Mathf.dst(i6, i5, tileX, tileY) * 4.0f)});
                    }
                }
            }
        }
        if (arrayList.isEmpty()) {
            lastError = tr("Sem carvão 3x3 livre", "No free 3x3 coal", "Sin carbón 3x3 libre", "Нет свободного угля 3x3");
            return null;
        }
        Collections.sort(arrayList, new Comparator<int[]>() { // from class: lunarians.SteamDrill.1
            @Override // java.util.Comparator
            public int compare(int[] iArr, int[] iArr2) {
                return (((9 - iArr[2]) * 60) + iArr[3]) - (((9 - iArr2[2]) * 60) + iArr2[3]);
            }
        });
        HashMap hashMap = new HashMap();
        boolean z2 = false;
        Seq seq = null;
        ArrayList arrayList2 = null;
        int[] iArr = null;
        int i10 = Integer.MAX_VALUE;
        int i11 = 0;
        int i12 = 0;
        while (i12 < arrayList.size() && i11 < 28) {
            int[] iArr2 = (int[]) arrayList.get(i12);
            int i13 = 0;
            while (true) {
                if (i13 < 4) {
                    Seq plans = Vars.schematics.toPlans(schematic, 0, 0);
                    BuildPlan buildPlan = null;
                    Iterator it = plans.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        BuildPlan buildPlan2 = (BuildPlan) it.next();
                        if (buildPlan2.block == block) {
                            buildPlan = buildPlan2;
                            break;
                        }
                    }
                    if (buildPlan == null) {
                        lastError = "schematic";
                        return null;
                    }
                    int i14 = iArr2[0] - buildPlan.x;
                    int i15 = iArr2[1] - buildPlan.y;
                    Iterator it2 = plans.iterator();
                    while (it2.hasNext()) {
                        BuildPlan buildPlan3 = (BuildPlan) it2.next();
                        buildPlan3.x += i14;
                        buildPlan3.y += i15;
                    }
                    for (int i16 = 0; i16 < i13; i16++) {
                        rotateOnce(plans, iArr2[0], iArr2[1]);
                    }
                    boolean z3 = true;
                    HashSet hashSet = new HashSet();
                    Iterator it3 = plans.iterator();
                    while (it3.hasNext()) {
                        BuildPlan buildPlan4 = (BuildPlan) it3.next();
                        if (buildPlan4.block == null || !freeArea(buildPlan4.block, buildPlan4.x, buildPlan4.y, reservedFromPlans, null) || !canPlace(buildPlan4.block, buildPlan4.x, buildPlan4.y, buildPlan4.rotation)) {
                            z3 = false;
                            break;
                        }
                        footprint(buildPlan4.block, buildPlan4.x, buildPlan4.y, hashSet);
                    }
                    if (z3) {
                        BuildPlan buildPlan5 = null;
                        BuildPlan buildPlan6 = null;
                        Iterator it4 = plans.iterator();
                        while (it4.hasNext()) {
                            BuildPlan buildPlan7 = (BuildPlan) it4.next();
                            if (buildPlan7.block == block3) {
                                buildPlan5 = buildPlan7;
                            }
                            if ((buildPlan7.block instanceof PowerNode) && buildPlan6 == null) {
                                buildPlan6 = buildPlan7;
                            }
                        }
                        if (buildPlan5 != null) {
                            i11++;
                            int i17 = buildPlan5.x - DX[buildPlan5.rotation & 3];
                            int i18 = buildPlan5.y - DY[buildPlan5.rotation & 3];
                            if (inside(i17, i18) && !reservedFromPlans[(i18 * width) + i17] && !hashSet.contains(Integer.valueOf((i18 * width) + i17)) && canPlace(block3, i17, i18, 0)) {
                                HashMap hashMap2 = new HashMap();
                                HashMap hashMap3 = new HashMap();
                                ArrayDeque arrayDeque = new ArrayDeque();
                                int i19 = (i18 * width) + i17;
                                hashMap2.put(Integer.valueOf(i19), -1);
                                hashMap3.put(Integer.valueOf(i19), 0);
                                arrayDeque.add(Integer.valueOf(i19));
                                int[] iArr3 = null;
                                int[] iArr4 = null;
                                while (!arrayDeque.isEmpty()) {
                                    int intValue2 = ((Integer) arrayDeque.poll()).intValue();
                                    int i20 = intValue2 % width;
                                    int i21 = intValue2 / width;
                                    int intValue3 = ((Integer) hashMap3.get(Integer.valueOf(intValue2))).intValue();
                                    for (int i22 = 0; i22 < 4; i22++) {
                                        int i23 = i20 + DX[i22];
                                        int i24 = i21 + DY[i22];
                                        for (int i25 = i23 - 1; i25 <= i23; i25++) {
                                            for (int i26 = i24 - 1; i26 <= i24; i26++) {
                                                if ((i20 < i25 || i20 > i25 + 1 || i21 < i26 || i21 > i26 + 1) && (waterTiles = waterTiles(i25, i26)) >= 2) {
                                                    z2 = true;
                                                    if ((waterTiles >= 4 || iArr3 == null) && freeArea(block2, i25, i26, reservedFromPlans, hashSet) && canPlace(block2, i25, i26, 0)) {
                                                        boolean z4 = false;
                                                        int i27 = intValue2;
                                                        while (true) {
                                                            int i28 = i27;
                                                            if (i28 == -1) {
                                                                break;
                                                            }
                                                            int i29 = i28 % width;
                                                            int i30 = i28 / width;
                                                            if (i29 < i25 || i29 > i25 + 1 || i30 < i26 || i30 > i26 + 1) {
                                                                i27 = ((Integer) hashMap2.get(Integer.valueOf(i28))).intValue();
                                                            } else {
                                                                z4 = true;
                                                                break;
                                                            }
                                                        }
                                                        if (!z4) {
                                                            if (waterTiles == 4) {
                                                                iArr3 = new int[]{intValue2, i25, i26, 4};
                                                            } else if (iArr4 == null) {
                                                                iArr4 = new int[]{intValue2, i25, i26, waterTiles};
                                                            }
                                                            if (iArr3 != null) {
                                                                break;
                                                            }
                                                        } else {
                                                            continue;
                                                        }
                                                    }
                                                }
                                            }
                                            if (iArr3 != null) {
                                                break;
                                            }
                                        }
                                        if (iArr3 != null) {
                                            break;
                                        }
                                    }
                                    if (iArr3 != null) {
                                        break;
                                    }
                                    if (intValue3 < 48) {
                                        for (int i31 = 0; i31 < 4; i31++) {
                                            int i32 = i20 + DX[i31];
                                            int i33 = i21 + DY[i31];
                                            if (inside(i32, i33)) {
                                                int i34 = (i33 * width) + i32;
                                                if (!hashMap2.containsKey(Integer.valueOf(i34)) && !reservedFromPlans[i34] && !hashSet.contains(Integer.valueOf(i34))) {
                                                    Boolean bool = (Boolean) hashMap.get(Integer.valueOf(i34));
                                                    if (bool == null) {
                                                        bool = Boolean.valueOf(canPlace(block3, i32, i33, 0));
                                                        hashMap.put(Integer.valueOf(i34), bool);
                                                    }
                                                    if (bool.booleanValue()) {
                                                        hashMap2.put(Integer.valueOf(i34), Integer.valueOf(intValue2));
                                                        hashMap3.put(Integer.valueOf(i34), Integer.valueOf(intValue3 + 1));
                                                        arrayDeque.add(Integer.valueOf(i34));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (iArr3 == null) {
                                    iArr3 = iArr4;
                                }
                                if (iArr3 != null && (intValue = (((Integer) hashMap3.get(Integer.valueOf(iArr3[0]))).intValue() * 10) + ((9 - iArr2[2]) * 25) + (iArr2[3] / 4) + ((4 - iArr3[3]) * 30)) < i10) {
                                    i10 = intValue;
                                    seq = plans;
                                    iArr = new int[]{iArr3[1], iArr3[2]};
                                    ArrayList arrayList3 = new ArrayList();
                                    int i35 = iArr3[0];
                                    while (true) {
                                        int i36 = i35;
                                        if (i36 == -1) {
                                            break;
                                        }
                                        int intValue4 = ((Integer) hashMap2.get(Integer.valueOf(i36))).intValue();
                                        int i37 = i36 % width;
                                        int i38 = i36 / width;
                                        if (intValue4 == -1) {
                                            i3 = buildPlan5.rotation & 3;
                                        } else {
                                            int i39 = intValue4 % width;
                                            i3 = i39 > i37 ? 0 : i39 < i37 ? 2 : intValue4 / width > i38 ? 1 : 3;
                                        }
                                        arrayList3.add(new int[]{i37, i38, i3});
                                        i35 = ((Integer) hashMap2.get(Integer.valueOf(i36))).intValue();
                                    }
                                    arrayList2 = arrayList3;
                                    if (((Integer) hashMap3.get(Integer.valueOf(iArr3[0]))).intValue() <= 6 && iArr2[2] >= 9) {
                                        i12 = arrayList.size();
                                        break;
                                    }
                                }
                            }
                        } else {
                            continue;
                        }
                    }
                    i13++;
                }
            }
        }
        if (seq == null) {
            if (z2) {
                tr = tr("Sem espaço/rota até a água", "No space/route to water", "Sin espacio/ruta hasta el agua", "Нет места/пути к воде");
            } else {
                tr = tr("Sem água por perto", "No water nearby", "Sin agua cerca", "Нет воды рядом");
            }
            lastError = tr;
            return null;
        }
        Seq<BuildPlan> seq2 = new Seq<>();
        HashSet hashSet2 = new HashSet();
        BuildPlan buildPlan8 = null;
        Iterator it5 = seq.iterator();
        while (it5.hasNext()) {
            BuildPlan buildPlan9 = (BuildPlan) it5.next();
            seq2.add(buildPlan9);
            footprint(buildPlan9.block, buildPlan9.x, buildPlan9.y, hashSet2);
            if ((buildPlan9.block instanceof PowerNode) && buildPlan8 == null) {
                buildPlan8 = buildPlan9;
            }
        }
        Iterator it6 = arrayList2.iterator();
        while (it6.hasNext()) {
            int[] iArr5 = (int[]) it6.next();
            seq2.add(new BuildPlan(iArr5[0], iArr5[1], iArr5[2], block3));
            hashSet2.add(Integer.valueOf((iArr5[1] * width) + iArr5[0]));
        }
        BuildPlan buildPlan10 = new BuildPlan(iArr[0], iArr[1], 0, block2);
        seq2.add(buildPlan10);
        footprint(block2, iArr[0], iArr[1], hashSet2);
        if (AutoSetConfig.autoPower()) {
            addPower(seq2, hashSet2, reservedFromPlans, buildPlan10, buildPlan8, width);
        }
        lastTiles.addAll(hashSet2);
        return seq2;
    }

    private static int[] snap(Block block, double d, double d2, boolean[] zArr, HashSet<Integer> hashSet, int i) {
        int round = (int) Math.round(d);
        int round2 = (int) Math.round(d2);
        for (int i2 = 0; i2 <= 3; i2++) {
            for (int i3 = -i2; i3 <= i2; i3++) {
                for (int i4 = -i2; i4 <= i2; i4++) {
                    if (Math.max(Math.abs(i4), Math.abs(i3)) == i2) {
                        int i5 = round + i4;
                        int i6 = round2 + i3;
                        if (freeArea(block, i5, i6, zArr, hashSet) && canPlace(block, i5, i6, 0)) {
                            return new int[]{i5, i6};
                        }
                    }
                }
            }
        }
        return null;
    }

    private static void addPower(Seq<BuildPlan> seq, HashSet<Integer> hashSet, boolean[] zArr, BuildPlan buildPlan, BuildPlan buildPlan2, int i) {
        int[] snap;
        PowerNode block = Vars.content.block("power-node");
        PowerNode block2 = Vars.content.block("large-power-node");
        Block block3 = Vars.content.block("solar-panel");
        if (block == null || buildPlan2 == null) {
            return;
        }
        double d = buildPlan.x + 0.5d;
        double d2 = buildPlan.y + 0.5d;
        double d3 = buildPlan2.x;
        double d4 = buildPlan2.y;
        double hypot = Math.hypot(d3 - d, d4 - d2);
        double d5 = hypot < 0.01d ? 1.0d : (d3 - d) / hypot;
        double d6 = hypot < 0.01d ? 0.0d : (d4 - d2) / hypot;
        PowerNode powerNode = block;
        int i2 = 0;
        int i3 = 0;
        while (i3 < 2) {
            PowerNode powerNode2 = (i3 == 0 && AutoSetConfig.bigNode() && block2 != null) ? block2 : block;
            double d7 = powerNode2.laserRange;
            double max = Math.max(2.0d, d7 - 2.0d);
            int i4 = 1;
            double min = Math.min(2.5d, hypot);
            while (hypot - min > d7 - 1.5d && i4 < 40) {
                min += max;
                i4++;
            }
            if (powerNode2 != block && !affordable(powerNode2, i4)) {
                i3++;
            } else {
                powerNode = powerNode2;
                i2 = i4;
                break;
            }
        }
        double max2 = Math.max(2.0d, powerNode.laserRange - 2.0d);
        ArrayList arrayList = new ArrayList();
        if (block3 != null) {
            int i5 = 0;
            while (i5 < 2) {
                int[] snap2 = snap(block3, d + (d5 * 2.0d) + ((i5 == 0 ? -d6 : d6) * 1.5d), d2 + (d6 * 2.0d) + ((i5 == 0 ? d5 : -d5) * 1.5d), zArr, hashSet, i);
                if (snap2 != null) {
                    BuildPlan buildPlan3 = new BuildPlan(snap2[0], snap2[1], 0, block3);
                    arrayList.add(buildPlan3);
                    seq.add(buildPlan3);
                    hashSet.add(Integer.valueOf((snap2[1] * i) + snap2[0]));
                }
                i5++;
            }
        }
        ArrayList arrayList2 = new ArrayList();
        double min2 = Math.min(2.5d, hypot);
        for (int i6 = 0; i6 < i2 && (snap = snap(powerNode, d + (d5 * min2), d2 + (d6 * min2), zArr, hashSet, i)) != null; i6++) {
            arrayList2.add(snap);
            hashSet.add(Integer.valueOf((snap[1] * i) + snap[0]));
            min2 += max2;
        }
        for (int size = arrayList2.size() - 1; size >= 0; size--) {
            int[] iArr = (int[]) arrayList2.get(size);
            ArrayList arrayList3 = new ArrayList();
            if (size == arrayList2.size() - 1) {
                arrayList3.add(new Point2(buildPlan2.x - iArr[0], buildPlan2.y - iArr[1]));
            } else {
                int[] iArr2 = (int[]) arrayList2.get(size + 1);
                arrayList3.add(new Point2(iArr2[0] - iArr[0], iArr2[1] - iArr[1]));
            }
            if (size == 0) {
                arrayList3.add(new Point2(buildPlan.x - iArr[0], buildPlan.y - iArr[1]));
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    BuildPlan buildPlan4 = (BuildPlan) it.next();
                    arrayList3.add(new Point2(buildPlan4.x - iArr[0], buildPlan4.y - iArr[1]));
                }
            }
            seq.add(new BuildPlan(iArr[0], iArr[1], 0, powerNode, arrayList3.toArray(new Point2[0])));
        }
    }
}
