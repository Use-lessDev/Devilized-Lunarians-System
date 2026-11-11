/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.math.Mathf
 *  arc.math.geom.Point2
 *  arc.struct.Seq
 *  arc.util.Log
 *  mindustry.Vars
 *  mindustry.content.Items
 *  mindustry.content.Liquids
 *  mindustry.entities.units.BuildPlan
 *  mindustry.game.Schematic
 *  mindustry.game.Schematics
 *  mindustry.game.Team
 *  mindustry.gen.Unit
 *  mindustry.type.ItemStack
 *  mindustry.world.Block
 *  mindustry.world.Build
 *  mindustry.world.Tile
 *  mindustry.world.blocks.power.PowerNode
 *  mindustry.world.blocks.storage.CoreBlock$CoreBuild
 */
package lunarians;

import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.struct.Seq;
import arc.util.Log;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import lunarians.AutoConnectConveyor;
import lunarians.AutoSetConfig;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Schematic;
import mindustry.game.Schematics;
import mindustry.game.Team;
import mindustry.gen.Unit;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.blocks.power.PowerNode;
import mindustry.world.blocks.storage.CoreBlock;

public class SteamDrill {
    static final String B64 = "bXNjaAF4nEWOzUrDQBSFT/NvgnbjQlyFulFowEUrbgW7EKwbFRchi0kzLQNpUidTtYjv4CPUZ3DhW9UnKHonTemFuZfLPec7Awd7FvxRWSheqCGbwXj/gJOzlOcVjuPV8rP7+/XT7cQnl/0eG4+T1fI7TjoJrIJNOY7uFWfT8FqKPA/PL8LBm3oKT3v9MwQZr0ZSzJQoCwTpwn+8fbgZXt0NPLQrbYomvOCSqVJiPxfPc5FFspwrLnGQSpFNeES/yuZCIchZxWWU6RA4jai9E73wBUH8WflKqqLMONytFTikBxNo0bABg4bj/jWlb3D1vVULqJHGIpUJ06ZhudiUBb26O6devfragMyN36wjtJ+CqNmeBhs1sg5eN0RTBxs6bAfVq72FrolmEYN0Ng3jH04dbjI=";
    static final int[] DX = new int[]{1, 0, -1, 0};
    static final int[] DY = new int[]{0, 1, 0, -1};
    public static final ArrayList<Integer> lastTiles = new ArrayList();
    public static String lastError = "";
    private static Schematic schem;

    private static String tr(String string, String string2, String string3, String string4) {
        return AutoSetConfig.tr4(string, string2, string3, string4);
    }

    private static Schematic schematic() {
        if (schem == null) {
            try {
                schem = Schematics.readBase64((String)B64);
            }
            catch (Throwable throwable) {
                Log.err((String)"[Lunarians] steam schematic", (Throwable)throwable);
            }
        }
        return schem;
    }

    private static void rotateOnce(Seq<BuildPlan> seq, int n, int n2) {
        for (int i = 0; i < seq.size; ++i) {
            BuildPlan buildPlan = (BuildPlan)seq.get(i);
            if (buildPlan.breaking || buildPlan.block == null) continue;
            float f = buildPlan.block.offset;
            float f2 = (float)(buildPlan.x - n) * 8.0f + f;
            float f3 = (float)(buildPlan.y - n2) * 8.0f + f;
            buildPlan.x = Math.round((-f3 - f) / 8.0f) + n;
            buildPlan.y = Math.round((f2 - f) / 8.0f) + n2;
            buildPlan.rotation = buildPlan.block.planRotation(Mathf.mod((int)(buildPlan.rotation + 1), (int)4));
            buildPlan.pointConfig(point2 -> {
                int n = point2.x;
                point2.set(-point2.y, n);
            });
        }
    }

    private static boolean inside(int n, int n2) {
        return Vars.world.tiles.in(n, n2);
    }

    private static void footprint(Block block, int n, int n2, HashSet<Integer> hashSet) {
        int n3 = Vars.world.width();
        int n4 = (block.size - 1) / 2;
        for (int i = 0; i < block.size; ++i) {
            for (int j = 0; j < block.size; ++j) {
                int n5 = n - n4 + j;
                int n6 = n2 - n4 + i;
                hashSet.add(n6 * n3 + n5);
            }
        }
    }

    private static boolean freeArea(Block block, int n, int n2, boolean[] blArray, HashSet<Integer> hashSet) {
        int n3 = Vars.world.width();
        int n4 = (block.size - 1) / 2;
        for (int i = 0; i < block.size; ++i) {
            for (int j = 0; j < block.size; ++j) {
                int n5 = n - n4 + j;
                int n6 = n2 - n4 + i;
                if (!SteamDrill.inside(n5, n6)) {
                    return false;
                }
                int n7 = n6 * n3 + n5;
                if (!blArray[n7] && (hashSet == null || !hashSet.contains(n7))) continue;
                return false;
            }
        }
        return true;
    }

    private static boolean canPlace(Block block, int n, int n2, int n3) {
        try {
            return Build.validPlace((Block)block, (Team)Vars.player.team(), (int)n, (int)n2, (int)n3);
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    private static int waterTiles(int n, int n2) {
        int n3 = 0;
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                Tile tile = Vars.world.tile(n + j, n2 + i);
                if (tile == null || tile.build != null) {
                    return -1;
                }
                if (tile.floor() == null || tile.floor().liquidDrop != Liquids.water) continue;
                ++n3;
            }
        }
        return n3;
    }

    private static boolean affordable(Block block, int n) {
        if (Vars.state.rules.infiniteResources) {
            return true;
        }
        CoreBlock.CoreBuild coreBuild = Vars.player.closestCore();
        if (coreBuild == null) {
            return false;
        }
        for (ItemStack itemStack : block.requirements) {
            if (coreBuild.items.get(itemStack.item) >= itemStack.amount * n) continue;
            return false;
        }
        return true;
    }

    public static Seq<BuildPlan> plan(Unit unit, int n, int n2, float f, boolean[] blArray) {
        lastTiles.clear();
        lastError = "";
        try {
            return SteamDrill.planInner(unit, n, n2, f, blArray);
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] SteamDrill", (Throwable)throwable);
            lastError = "error";
            return null;
        }
    }

    private static Seq<BuildPlan> planInner(Unit unit, int n, int n2, float f, boolean[] blArray) {
        Object object5;
        BuildPlan buildPlan;
        Object object2;
        int n3;
        int n4;
        int n5;
        Schematic schematic = SteamDrill.schematic();
        Block block = Vars.content.block("laser-drill");
        Block block2 = Vars.content.block("rotary-pump");
        Block block3 = Vars.content.block("conduit");
        if (schematic == null || block == null || block2 == null || block3 == null || Vars.player.closestCore() == null) {
            lastError = SteamDrill.tr("Blocos indispon\u00edveis", "Blocks unavailable", "Bloques no disponibles", "\u0411\u043b\u043e\u043a\u0438 \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u043d\u044b");
            return null;
        }
        int n6 = Vars.world.width();
        boolean[] blArray2 = AutoConnectConveyor.reservedFromPlans(unit);
        if (blArray != null) {
            for (n5 = 0; n5 < blArray.length && n5 < blArray2.length; ++n5) {
                if (!blArray[n5]) continue;
                blArray2[n5] = true;
            }
        }
        n5 = Vars.player.closestCore().tileX();
        int n7 = Vars.player.closestCore().tileY();
        int n8 = AutoSetConfig.steamMinCoal();
        ArrayList<int[]> arrayList = new ArrayList<int[]>();
        int n9 = (int)Math.ceil(f);
        for (int i = n2 - n9; i <= n2 + n9; ++i) {
            for (n4 = n - n9; n4 <= n + n9; ++n4) {
                if (!SteamDrill.inside(n4, i) || Mathf.dst((float)n4, (float)i, (float)n, (float)n2) > f - 2.0f) continue;
                int n10 = 0;
                boolean bl = false;
                block3: for (int j = -1; j <= 1 && !bl; ++j) {
                    for (n3 = -1; n3 <= 1; ++n3) {
                        Tile tile = Vars.world.tile(n4 + n3, i + j);
                        if (tile == null || tile.build != null || blArray2[(i + j) * n6 + n4 + n3]) {
                            bl = true;
                            continue block3;
                        }
                        if (tile.drop() != Items.coal) continue;
                        ++n10;
                    }
                }
                if (bl || n10 < n8) continue;
                arrayList.add(new int[]{n4, i, n10, (int)(Mathf.dst((float)n4, (float)i, (float)n5, (float)n7) * 4.0f)});
            }
        }
        if (arrayList.isEmpty()) {
            lastError = SteamDrill.tr("Sem carv\u00e3o 3x3 livre", "No free 3x3 coal", "Sin carb\u00f3n 3x3 libre", "\u041d\u0435\u0442 \u0441\u0432\u043e\u0431\u043e\u0434\u043d\u043e\u0433\u043e \u0443\u0433\u043b\u044f 3x3");
            return null;
        }
        Collections.sort(arrayList, new Comparator<int[]>(){

            @Override
            public int compare(int[] nArray, int[] nArray2) {
                return (9 - nArray[2]) * 60 + nArray[3] - ((9 - nArray2[2]) * 60 + nArray2[3]);
            }
        });
        HashMap<Integer, Boolean> hashMap = new HashMap<Integer, Boolean>();
        n4 = 0;
        BuildPlan buildPlan2 = null;
        ArrayList<int[]> arrayList2 = null;
        int[] nArray = null;
        n3 = Integer.MAX_VALUE;
        int n11 = 0;
        block5: for (int i = 0; i < arrayList.size() && n11 < 28; ++i) {
            object2 = (int[])arrayList.get(i);
            for (int j = 0; j < 4; ++j) {
                int n12;
                int n13;
                int n14;
                int n15;
                int n16;
                int n17;
                int n18;
                int n19;
                BuildPlan buildPlan32;
                int n20;
                Object object32;
                buildPlan = Vars.schematics.toPlans(schematic, 0, 0);
                object5 = null;
                for (BuildPlan buildPlan4 : buildPlan) {
                    if (buildPlan4.block != block) continue;
                    object5 = buildPlan4;
                    break;
                }
                if (object5 == null) {
                    lastError = "schematic";
                    return null;
                }
                int n21 = object2[0] - object5.x;
                int n22 = object2[1] - object5.y;
                for (Object object32 : buildPlan) {
                    ((BuildPlan)object32).x += n21;
                    ((BuildPlan)object32).y += n22;
                }
                for (n20 = 0; n20 < j; ++n20) {
                    SteamDrill.rotateOnce((Seq<BuildPlan>)buildPlan, (int)object2[0], (int)object2[1]);
                }
                n20 = 1;
                object32 = new HashSet();
                for (BuildPlan buildPlan32 : buildPlan) {
                    if (buildPlan32.block == null || !SteamDrill.freeArea(buildPlan32.block, buildPlan32.x, buildPlan32.y, blArray2, null) || !SteamDrill.canPlace(buildPlan32.block, buildPlan32.x, buildPlan32.y, buildPlan32.rotation)) {
                        n20 = 0;
                        break;
                    }
                    SteamDrill.footprint(buildPlan32.block, buildPlan32.x, buildPlan32.y, (HashSet<Integer>)object32);
                }
                if (n20 == 0) continue;
                Iterator iterator = null;
                buildPlan32 = null;
                for (BuildPlan buildPlan5 : buildPlan) {
                    if (buildPlan5.block == block3) {
                        iterator = buildPlan5;
                    }
                    if (!(buildPlan5.block instanceof PowerNode) || buildPlan32 != null) continue;
                    buildPlan32 = buildPlan5;
                }
                if (iterator == null) continue;
                ++n11;
                int n23 = ((BuildPlan)iterator).x - DX[((BuildPlan)iterator).rotation & 3];
                int n24 = ((BuildPlan)iterator).y - DY[((BuildPlan)iterator).rotation & 3];
                if (!SteamDrill.inside(n23, n24) || blArray2[n24 * n6 + n23] || ((HashSet)object32).contains(n24 * n6 + n23) || !SteamDrill.canPlace(block3, n23, n24, 0)) continue;
                HashMap<Integer, Integer> hashMap2 = new HashMap<Integer, Integer>();
                HashMap<Integer, Integer> hashMap3 = new HashMap<Integer, Integer>();
                ArrayDeque<Integer> arrayDeque = new ArrayDeque<Integer>();
                int n25 = n24 * n6 + n23;
                hashMap2.put(n25, -1);
                hashMap3.put(n25, 0);
                arrayDeque.add(n25);
                int[] nArray2 = null;
                int[] nArray3 = null;
                int n26 = 48;
                while (!arrayDeque.isEmpty()) {
                    n19 = (Integer)arrayDeque.poll();
                    int n27 = n19 % n6;
                    n18 = n19 / n6;
                    n17 = (Integer)hashMap3.get(n19);
                    for (n16 = 0; n16 < 4; ++n16) {
                        n15 = n27 + DX[n16];
                        n14 = n18 + DY[n16];
                        for (n13 = n15 - 1; n13 <= n15; ++n13) {
                            for (n12 = n14 - 1; n12 <= n14; ++n12) {
                                int n28;
                                if (n27 >= n13 && n27 <= n13 + 1 && n18 >= n12 && n18 <= n12 + 1 || (n28 = SteamDrill.waterTiles(n13, n12)) < 2) continue;
                                n4 = 1;
                                if (n28 < 4 && nArray2 != null || !SteamDrill.freeArea(block2, n13, n12, blArray2, (HashSet<Integer>)object32) || !SteamDrill.canPlace(block2, n13, n12, 0)) continue;
                                boolean bl = false;
                                int n29 = n19;
                                while (n29 != -1) {
                                    int n30 = n29 % n6;
                                    int n31 = n29 / n6;
                                    if (n30 >= n13 && n30 <= n13 + 1 && n31 >= n12 && n31 <= n12 + 1) {
                                        bl = true;
                                        break;
                                    }
                                    n29 = (Integer)hashMap2.get(n29);
                                }
                                if (bl) continue;
                                if (n28 == 4) {
                                    nArray2 = new int[]{n19, n13, n12, 4};
                                } else if (nArray3 == null) {
                                    nArray3 = new int[]{n19, n13, n12, n28};
                                }
                                if (nArray2 != null) break;
                            }
                            if (nArray2 != null) break;
                        }
                        if (nArray2 != null) break;
                    }
                    if (nArray2 != null) break;
                    if (n17 >= n26) continue;
                    for (n16 = 0; n16 < 4; ++n16) {
                        n15 = n27 + DX[n16];
                        n14 = n18 + DY[n16];
                        if (!SteamDrill.inside(n15, n14) || hashMap2.containsKey(n13 = n14 * n6 + n15) || blArray2[n13] || ((HashSet)object32).contains(n13)) continue;
                        Boolean bl = (Boolean)hashMap.get(n13);
                        if (bl == null) {
                            bl = SteamDrill.canPlace(block3, n15, n14, 0);
                            hashMap.put(n13, bl);
                        }
                        if (!bl.booleanValue()) continue;
                        hashMap2.put(n13, n19);
                        hashMap3.put(n13, n17 + 1);
                        arrayDeque.add(n13);
                    }
                }
                if (nArray2 == null) {
                    nArray2 = nArray3;
                }
                if (nArray2 == null || (n19 = (Integer)hashMap3.get((int)nArray2[0]) * 10 + (9 - object2[2]) * 25 + object2[3] / 4 + (4 - nArray2[3]) * 30) >= n3) continue;
                n3 = n19;
                buildPlan2 = buildPlan;
                nArray = new int[]{nArray2[1], nArray2[2]};
                ArrayList<int[]> arrayList3 = new ArrayList<int[]>();
                n18 = nArray2[0];
                while (n18 != -1) {
                    n17 = (Integer)hashMap2.get(n18);
                    n16 = n18 % n6;
                    n15 = n18 / n6;
                    if (n17 == -1) {
                        n14 = ((BuildPlan)iterator).rotation & 3;
                    } else {
                        n13 = n17 % n6;
                        n12 = n17 / n6;
                        n14 = n13 > n16 ? 0 : (n13 < n16 ? 2 : (n12 > n15 ? 1 : 3));
                    }
                    arrayList3.add(new int[]{n16, n15, n14});
                    n18 = (Integer)hashMap2.get(n18);
                }
                arrayList2 = arrayList3;
                if ((Integer)hashMap3.get((int)nArray2[0]) > 6 || object2[2] < 9) continue;
                i = arrayList.size();
                continue block5;
            }
        }
        if (buildPlan2 == null) {
            lastError = n4 != 0 ? SteamDrill.tr("Sem espa\u00e7o/rota at\u00e9 a \u00e1gua", "No space/route to water", "Sin espacio/ruta hasta el agua", "\u041d\u0435\u0442 \u043c\u0435\u0441\u0442\u0430/\u043f\u0443\u0442\u0438 \u043a \u0432\u043e\u0434\u0435") : SteamDrill.tr("Sem \u00e1gua por perto", "No water nearby", "Sin agua cerca", "\u041d\u0435\u0442 \u0432\u043e\u0434\u044b \u0440\u044f\u0434\u043e\u043c");
            return null;
        }
        Seq seq = new Seq();
        object2 = new HashSet();
        Object object4 = null;
        for (Object object5 : buildPlan2) {
            seq.add(object5);
            SteamDrill.footprint(object5.block, object5.x, object5.y, (HashSet<Integer>)object2);
            if (!(object5.block instanceof PowerNode) || object4 != null) continue;
            object4 = object5;
        }
        buildPlan = arrayList2.iterator();
        while (buildPlan.hasNext()) {
            object5 = (int[])buildPlan.next();
            seq.add((Object)new BuildPlan((int)object5[0], (int)object5[1], (int)object5[2], block3));
            ((HashSet)object2).add((int)(object5[1] * n6 + object5[0]));
        }
        buildPlan = new BuildPlan((int)nArray[0], (int)nArray[1], 0, block2);
        seq.add((Object)buildPlan);
        SteamDrill.footprint(block2, (int)nArray[0], (int)nArray[1], (HashSet<Integer>)object2);
        if (AutoSetConfig.autoPower()) {
            SteamDrill.addPower((Seq<BuildPlan>)seq, (HashSet<Integer>)object2, blArray2, buildPlan, object4, n6);
        }
        lastTiles.addAll((Collection<Integer>)object2);
        return seq;
    }

    private static int[] snap(Block block, double d, double d2, boolean[] blArray, HashSet<Integer> hashSet, int n) {
        int n2 = (int)Math.round(d);
        int n3 = (int)Math.round(d2);
        for (int i = 0; i <= 3; ++i) {
            for (int j = -i; j <= i; ++j) {
                for (int k = -i; k <= i; ++k) {
                    int n4;
                    int n5;
                    if (Math.max(Math.abs(k), Math.abs(j)) != i || !SteamDrill.freeArea(block, n5 = n2 + k, n4 = n3 + j, blArray, hashSet) || !SteamDrill.canPlace(block, n5, n4, 0)) continue;
                    return new int[]{n5, n4};
                }
            }
        }
        return null;
    }

    private static void addPower(Seq<BuildPlan> seq, HashSet<Integer> hashSet, boolean[] blArray, BuildPlan buildPlan, BuildPlan buildPlan2, int n) {
        int[] nArray;
        int n2;
        double d;
        Block block = Vars.content.block("power-node");
        Block block2 = Vars.content.block("large-power-node");
        Block block3 = Vars.content.block("solar-panel");
        if (block == null || buildPlan2 == null) {
            return;
        }
        double d2 = buildPlan2.x;
        double d3 = (double)buildPlan.x + 0.5;
        double d4 = buildPlan2.y;
        double d5 = (double)buildPlan.y + 0.5;
        double d6 = Math.hypot(d2 - d3, d4 - d5);
        double d7 = d6 < 0.01 ? 1.0 : (d2 - d3) / d6;
        double d8 = d6 < 0.01 ? 0.0 : (d4 - d5) / d6;
        Block block4 = block;
        int n3 = 0;
        for (int i = 0; i < 2; ++i) {
            int n4;
            Block block5 = i == 0 && AutoSetConfig.bigNode() && block2 != null ? block2 : block;
            d = ((PowerNode)block5).laserRange;
            double d9 = Math.max(2.0, d - 2.0);
            double d10 = Math.min(2.5, d6);
            for (n4 = 1; d6 - d10 > d - 1.5 && n4 < 40; ++n4) {
                d10 += d9;
            }
            if (block5 != block && !SteamDrill.affordable(block5, n4)) continue;
            block4 = block5;
            n3 = n4;
            break;
        }
        double d11 = ((PowerNode)block4).laserRange;
        d = Math.max(2.0, d11 - 2.0);
        ArrayList<BuildPlan> arrayList = new ArrayList<BuildPlan>();
        if (block3 != null) {
            for (int i = 0; i < 2; ++i) {
                int[] nArray2 = SteamDrill.snap(block3, d3 + d7 * 2.0 + (i == 0 ? -d8 : d8) * 1.5, d5 + d8 * 2.0 + (i == 0 ? d7 : -d7) * 1.5, blArray, hashSet, n);
                if (nArray2 == null) continue;
                BuildPlan buildPlan3 = new BuildPlan(nArray2[0], nArray2[1], 0, block3);
                arrayList.add(buildPlan3);
                seq.add((Object)buildPlan3);
                hashSet.add(nArray2[1] * n + nArray2[0]);
            }
        }
        ArrayList<int[]> arrayList2 = new ArrayList<int[]>();
        double d12 = Math.min(2.5, d6);
        for (n2 = 0; n2 < n3 && (nArray = SteamDrill.snap(block4, d3 + d7 * d12, d5 + d8 * d12, blArray, hashSet, n)) != null; ++n2) {
            arrayList2.add(nArray);
            hashSet.add(nArray[1] * n + nArray[0]);
            d12 += d;
        }
        for (n2 = arrayList2.size() - 1; n2 >= 0; --n2) {
            Object object;
            nArray = (int[])arrayList2.get(n2);
            ArrayList<Point2> arrayList3 = new ArrayList<Point2>();
            if (n2 == arrayList2.size() - 1) {
                arrayList3.add(new Point2(buildPlan2.x - nArray[0], buildPlan2.y - nArray[1]));
            } else {
                object = (int[])arrayList2.get(n2 + 1);
                arrayList3.add(new Point2(object[0] - nArray[0], object[1] - nArray[1]));
            }
            if (n2 == 0) {
                arrayList3.add(new Point2(buildPlan.x - nArray[0], buildPlan.y - nArray[1]));
                object = arrayList.iterator();
                while (object.hasNext()) {
                    BuildPlan buildPlan4 = (BuildPlan)object.next();
                    arrayList3.add(new Point2(buildPlan4.x - nArray[0], buildPlan4.y - nArray[1]));
                }
            }
            seq.add((Object)new BuildPlan(nArray[0], nArray[1], 0, block4, (Object)arrayList3.toArray(new Point2[0])));
        }
    }
}

