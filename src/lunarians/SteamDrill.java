package lunarians;

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

public class SteamDrill {
  static final String B64 = "bXNjaAF4nEWOzUrDQBSFT/NvgnbjQlyFulFowEUrbgW7EKwbFRchi0kzLQNpUidTtYjv4CPUZ3DhW9UnKHonTemFuZfLPec7Awd7FvxRWSheqCGbwXj/gJOzlOcVjuPV8rP7+/XT7cQnl/0eG4+T1fI7TjoJrIJNOY7uFWfT8FqKPA/PL8LBm3oKT3v9MwQZr0ZSzJQoCwTpwn+8fbgZXt0NPLQrbYomvOCSqVJiPxfPc5FFspwrLnGQSpFNeES/yuZCIchZxWWU6RA4jai9E73wBUH8WflKqqLMONytFTikBxNo0bABg4bj/jWlb3D1vVULqJHGIpUJ06ZhudiUBb26O6devfragMyN36wjtJ+CqNmeBhs1sg5eN0RTBxs6bAfVq72FrolmEYN0Ng3jH04dbjI=";
  
  static final int[] DX = new int[] { 1, 0, -1, 0 };
  
  static final int[] DY = new int[] { 0, 1, 0, -1 };
  
  public static final ArrayList<Integer> lastTiles = new ArrayList<>();
  
  public static String lastError = "";
  
  private static Schematic schem;
  
  private static String tr(String paramString1, String paramString2, String paramString3, String paramString4) {
    return AutoSetConfig.tr4(paramString1, paramString2, paramString3, paramString4);
  }
  
  private static Schematic schematic() {
    if (schem == null)
      try {
        schem = Schematics.readBase64("bXNjaAF4nEWOzUrDQBSFT/NvgnbjQlyFulFowEUrbgW7EKwbFRchi0kzLQNpUidTtYjv4CPUZ3DhW9UnKHonTemFuZfLPec7Awd7FvxRWSheqCGbwXj/gJOzlOcVjuPV8rP7+/XT7cQnl/0eG4+T1fI7TjoJrIJNOY7uFWfT8FqKPA/PL8LBm3oKT3v9MwQZr0ZSzJQoCwTpwn+8fbgZXt0NPLQrbYomvOCSqVJiPxfPc5FFspwrLnGQSpFNeES/yuZCIchZxWWU6RA4jai9E73wBUH8WflKqqLMONytFTikBxNo0bABg4bj/jWlb3D1vVULqJHGIpUJ06ZhudiUBb26O6devfragMyN36wjtJ+CqNmeBhs1sg5eN0RTBxs6bAfVq72FrolmEYN0Ng3jH04dbjI=");
      } catch (Throwable throwable) {
        Log.err("[Lunarians] steam schematic", throwable);
      }  
    return schem;
  }
  
  private static void rotateOnce(Seq<BuildPlan> paramSeq, int paramInt1, int paramInt2) {
    for (byte b = 0; b < paramSeq.size; b++) {
      BuildPlan buildPlan = (BuildPlan)paramSeq.get(b);
      if (!buildPlan.breaking && buildPlan.block != null) {
        float f1 = buildPlan.block.offset;
        float f2 = (buildPlan.x - paramInt1) * 8.0F + f1;
        float f3 = (buildPlan.y - paramInt2) * 8.0F + f1;
        buildPlan.x = Math.round((-f3 - f1) / 8.0F) + paramInt1;
        buildPlan.y = Math.round((f2 - f1) / 8.0F) + paramInt2;
        buildPlan.rotation = buildPlan.block.planRotation(Mathf.mod(buildPlan.rotation + 1, 4));
        buildPlan.pointConfig(paramPoint2 -> {
              int i = paramPoint2.x;
              paramPoint2.set(-paramPoint2.y, i);
            });
      } 
    } 
  }
  
  private static boolean inside(int paramInt1, int paramInt2) {
    return Vars.world.tiles.in(paramInt1, paramInt2);
  }
  
  private static void footprint(Block paramBlock, int paramInt1, int paramInt2, HashSet<Integer> paramHashSet) {
    int i = Vars.world.width();
    int j = (paramBlock.size - 1) / 2;
    for (byte b = 0; b < paramBlock.size; b++) {
      for (byte b1 = 0; b1 < paramBlock.size; b1++) {
        int k = paramInt1 - j + b1, m = paramInt2 - j + b;
        paramHashSet.add(Integer.valueOf(m * i + k));
      } 
    } 
  }
  
  private static boolean freeArea(Block paramBlock, int paramInt1, int paramInt2, boolean[] paramArrayOfboolean, HashSet<Integer> paramHashSet) {
    int i = Vars.world.width();
    int j = (paramBlock.size - 1) / 2;
    for (byte b = 0; b < paramBlock.size; b++) {
      for (byte b1 = 0; b1 < paramBlock.size; b1++) {
        int k = paramInt1 - j + b1, m = paramInt2 - j + b;
        if (!inside(k, m))
          return false; 
        int n = m * i + k;
        if (paramArrayOfboolean[n] || (paramHashSet != null && paramHashSet.contains(Integer.valueOf(n))))
          return false; 
      } 
    } 
    return true;
  }
  
  private static boolean canPlace(Block paramBlock, int paramInt1, int paramInt2, int paramInt3) {
    try {
      return Build.validPlace(paramBlock, Vars.player.team(), paramInt1, paramInt2, paramInt3);
    } catch (Throwable throwable) {
      return false;
    } 
  }
  
  private static int waterTiles(int paramInt1, int paramInt2) {
    byte b1 = 0;
    for (byte b2 = 0; b2 < 2; b2++) {
      for (byte b = 0; b < 2; b++) {
        Tile tile = Vars.world.tile(paramInt1 + b, paramInt2 + b2);
        if (tile == null || tile.build != null)
          return -1; 
        if (tile.floor() != null && (tile.floor()).liquidDrop == Liquids.water)
          b1++; 
      } 
    } 
    return b1;
  }
  
  private static boolean affordable(Block paramBlock, int paramInt) {
    if (Vars.state.rules.infiniteResources)
      return true; 
    CoreBlock.CoreBuild coreBuild = Vars.player.closestCore();
    if (coreBuild == null)
      return false; 
    for (ItemStack itemStack : paramBlock.requirements) {
      if (coreBuild.items.get(itemStack.item) < itemStack.amount * paramInt)
        return false; 
    } 
    return true;
  }
  
  public static Seq<BuildPlan> plan(Unit paramUnit, int paramInt1, int paramInt2, float paramFloat, boolean[] paramArrayOfboolean) {
    lastTiles.clear();
    lastError = "";
    try {
      return planInner(paramUnit, paramInt1, paramInt2, paramFloat, paramArrayOfboolean);
    } catch (Throwable throwable) {
      Log.err("[Lunarians] SteamDrill", throwable);
      lastError = "error";
      return null;
    } 
  }
  
  private static Seq<BuildPlan> planInner(Unit paramUnit, int paramInt1, int paramInt2, float paramFloat, boolean[] paramArrayOfboolean) {
    Schematic schematic = schematic();
    Block block1 = Vars.content.block("laser-drill");
    Block block2 = Vars.content.block("rotary-pump");
    Block block3 = Vars.content.block("conduit");
    if (schematic == null || block1 == null || block2 == null || block3 == null || Vars.player.closestCore() == null) {
      lastError = tr("Blocos indisponíveis", "Blocks unavailable", "Bloques no disponibles", "Блоки недоступны");
      return null;
    } 
    int i = Vars.world.width();
    boolean[] arrayOfBoolean = AutoConnectConveyor.reservedFromPlans(paramUnit);
    if (paramArrayOfboolean != null)
      for (byte b1 = 0; b1 < paramArrayOfboolean.length && b1 < arrayOfBoolean.length; ) {
        if (paramArrayOfboolean[b1])
          arrayOfBoolean[b1] = true; 
        b1++;
      }  
    int j = Vars.player.closestCore().tileX();
    int k = Vars.player.closestCore().tileY();
    int m = AutoSetConfig.steamMinCoal();
    ArrayList<int[]> arrayList1 = new ArrayList();
    int n = (int)Math.ceil(paramFloat);
    for (int i1 = paramInt2 - n; i1 <= paramInt2 + n; i1++) {
      for (int i4 = paramInt1 - n; i4 <= paramInt1 + n; i4++) {
        if (inside(i4, i1) && Mathf.dst(i4, i1, paramInt1, paramInt2) <= paramFloat - 2.0F) {
          byte b1 = 0;
          boolean bool1 = false;
          for (byte b2 = -1; b2 <= 1 && !bool1; b2++) {
            for (byte b3 = -1; b3 <= 1; b3++) {
              Tile tile = Vars.world.tile(i4 + b3, i1 + b2);
              if (tile == null || tile.build != null || arrayOfBoolean[(i1 + b2) * i + i4 + b3]) {
                bool1 = true;
                break;
              } 
              if (tile.drop() == Items.coal)
                b1++; 
            } 
          } 
          if (!bool1 && b1 >= m)
            arrayList1.add(new int[] { i4, i1, b1, (int)(Mathf.dst(i4, i1, j, k) * 4.0F) }); 
        } 
      } 
    } 
    if (arrayList1.isEmpty()) {
      lastError = tr("Sem carvão 3x3 livre", "No free 3x3 coal", "Sin carbón 3x3 libre", "Нет свободного угля 3x3");
      return null;
    } 
    Collections.sort(arrayList1, (Comparator)new Comparator<int[]>() {
          public int compare(int[] param1ArrayOfint1, int[] param1ArrayOfint2) {
            return (9 - param1ArrayOfint1[2]) * 60 + param1ArrayOfint1[3] - (9 - param1ArrayOfint2[2]) * 60 + param1ArrayOfint2[3];
          }
        });
    HashMap<Object, Object> hashMap = new HashMap<>();
    boolean bool = false;
    Seq<BuildPlan> seq1 = null;
    ArrayList<int[]> arrayList2 = null;
    int[] arrayOfInt = null;
    int i2 = Integer.MAX_VALUE;
    byte b = 0;
    for (int i3 = 0; i3 < arrayList1.size() && b < 28; i3++) {
      int[] arrayOfInt1 = arrayList1.get(i3);
      for (byte b1 = 0; b1 < 4; b1++) {
        Seq<BuildPlan> seq = Vars.schematics.toPlans(schematic, 0, 0);
        BuildPlan buildPlan = null;
        for (BuildPlan buildPlan3 : seq) {
          if (buildPlan3.block == block1) {
            buildPlan = buildPlan3;
            break;
          } 
        } 
        if (buildPlan == null) {
          lastError = "schematic";
          return null;
        } 
        int i4 = arrayOfInt1[0] - buildPlan.x, i5 = arrayOfInt1[1] - buildPlan.y;
        for (BuildPlan buildPlan3 : seq) {
          buildPlan3.x += i4;
          buildPlan3.y += i5;
        } 
        byte b2;
        for (b2 = 0; b2 < b1; ) {
          rotateOnce(seq, arrayOfInt1[0], arrayOfInt1[1]);
          b2++;
        } 
        b2 = 1;
        HashSet<Integer> hashSet1 = new HashSet();
        for (BuildPlan buildPlan3 : seq) {
          if (buildPlan3.block == null || !freeArea(buildPlan3.block, buildPlan3.x, buildPlan3.y, arrayOfBoolean, null) || !canPlace(buildPlan3.block, buildPlan3.x, buildPlan3.y, buildPlan3.rotation)) {
            b2 = 0;
            break;
          } 
          footprint(buildPlan3.block, buildPlan3.x, buildPlan3.y, hashSet1);
        } 
        if (b2 != 0) {
          BuildPlan buildPlan3 = null, buildPlan4 = null;
          for (BuildPlan buildPlan5 : seq) {
            if (buildPlan5.block == block3)
              buildPlan3 = buildPlan5; 
            if (buildPlan5.block instanceof PowerNode && buildPlan4 == null)
              buildPlan4 = buildPlan5; 
          } 
          if (buildPlan3 != null) {
            b++;
            int i6 = buildPlan3.x - DX[buildPlan3.rotation & 0x3];
            int i7 = buildPlan3.y - DY[buildPlan3.rotation & 0x3];
            if (inside(i6, i7) && !arrayOfBoolean[i7 * i + i6] && !hashSet1.contains(Integer.valueOf(i7 * i + i6)) && canPlace(block3, i6, i7, 0)) {
              HashMap<Object, Object> hashMap1 = new HashMap<>();
              HashMap<Object, Object> hashMap2 = new HashMap<>();
              ArrayDeque<Integer> arrayDeque = new ArrayDeque();
              int i8 = i7 * i + i6;
              hashMap1.put(Integer.valueOf(i8), Integer.valueOf(-1));
              hashMap2.put(Integer.valueOf(i8), Integer.valueOf(0));
              arrayDeque.add(Integer.valueOf(i8));
              int[] arrayOfInt2 = null;
              int[] arrayOfInt3 = null;
              byte b3 = 48;
              while (!arrayDeque.isEmpty()) {
                int i9 = ((Integer)arrayDeque.poll()).intValue();
                int i10 = i9 % i, i11 = i9 / i;
                int i12 = ((Integer)hashMap2.get(Integer.valueOf(i9))).intValue();
                byte b4;
                for (b4 = 0; b4 < 4; b4++) {
                  int i13 = i10 + DX[b4], i14 = i11 + DY[b4];
                  for (int i15 = i13 - 1; i15 <= i13; i15++) {
                    for (int i16 = i14 - 1; i16 <= i14; i16++) {
                      if (i10 < i15 || i10 > i15 + 1 || i11 < i16 || i11 > i16 + 1) {
                        int i17 = waterTiles(i15, i16);
                        if (i17 >= 2) {
                          bool = true;
                          if ((i17 >= 4 || arrayOfInt2 == null) && 
                            freeArea(block2, i15, i16, arrayOfBoolean, hashSet1) && canPlace(block2, i15, i16, 0)) {
                            boolean bool1 = false;
                            int i18;
                            for (i18 = i9; i18 != -1; i18 = ((Integer)hashMap1.get(Integer.valueOf(i18))).intValue()) {
                              int i19 = i18 % i, i20 = i18 / i;
                              if (i19 >= i15 && i19 <= i15 + 1 && i20 >= i16 && i20 <= i16 + 1) {
                                bool1 = true;
                                break;
                              } 
                            } 
                            if (!bool1) {
                              if (i17 == 4) {
                                arrayOfInt2 = new int[] { i9, i15, i16, 4 };
                              } else if (arrayOfInt3 == null) {
                                arrayOfInt3 = new int[] { i9, i15, i16, i17 };
                              } 
                              if (arrayOfInt2 != null)
                                break; 
                            } 
                          } 
                        } 
                      } 
                    } 
                    if (arrayOfInt2 != null)
                      break; 
                  } 
                  if (arrayOfInt2 != null)
                    break; 
                } 
                if (arrayOfInt2 != null)
                  break; 
                if (i12 >= b3)
                  continue; 
                for (b4 = 0; b4 < 4; b4++) {
                  int i13 = i10 + DX[b4], i14 = i11 + DY[b4];
                  if (inside(i13, i14)) {
                    int i15 = i14 * i + i13;
                    if (!hashMap1.containsKey(Integer.valueOf(i15)) && !arrayOfBoolean[i15] && !hashSet1.contains(Integer.valueOf(i15))) {
                      Boolean bool1 = (Boolean)hashMap.get(Integer.valueOf(i15));
                      if (bool1 == null) {
                        bool1 = Boolean.valueOf(canPlace(block3, i13, i14, 0));
                        hashMap.put(Integer.valueOf(i15), bool1);
                      } 
                      if (bool1.booleanValue()) {
                        hashMap1.put(Integer.valueOf(i15), Integer.valueOf(i9));
                        hashMap2.put(Integer.valueOf(i15), Integer.valueOf(i12 + 1));
                        arrayDeque.add(Integer.valueOf(i15));
                      } 
                    } 
                  } 
                } 
              } 
              if (arrayOfInt2 == null)
                arrayOfInt2 = arrayOfInt3; 
              if (arrayOfInt2 != null) {
                int i9 = ((Integer)hashMap2.get(Integer.valueOf(arrayOfInt2[0]))).intValue() * 10 + (9 - arrayOfInt1[2]) * 25 + arrayOfInt1[3] / 4 + (4 - arrayOfInt2[3]) * 30;
                if (i9 < i2) {
                  i2 = i9;
                  seq1 = seq;
                  arrayOfInt = new int[] { arrayOfInt2[1], arrayOfInt2[2] };
                  ArrayList<int[]> arrayList = new ArrayList();
                  int i10;
                  for (i10 = arrayOfInt2[0]; i10 != -1; i10 = ((Integer)hashMap1.get(Integer.valueOf(i10))).intValue()) {
                    boolean bool1;
                    int i11 = ((Integer)hashMap1.get(Integer.valueOf(i10))).intValue();
                    int i12 = i10 % i, i13 = i10 / i;
                    if (i11 == -1) {
                      bool1 = buildPlan3.rotation & 0x3;
                    } else {
                      int i14 = i11 % i, i15 = i11 / i;
                      bool1 = (i14 > i12) ? false : ((i14 < i12) ? true : ((i15 > i13) ? true : true));
                    } 
                    arrayList.add(new int[] { i12, i13, bool1 });
                  } 
                  arrayList2 = arrayList;
                  if (((Integer)hashMap2.get(Integer.valueOf(arrayOfInt2[0]))).intValue() <= 6 && arrayOfInt1[2] >= 9) {
                    i3 = arrayList1.size();
                    break;
                  } 
                } 
              } 
            } 
          } 
        } 
      } 
    } 
    if (seq1 == null) {
      lastError = bool ? tr("Sem espaço/rota até a água", "No space/route to water", "Sin espacio/ruta hasta el agua", "Нет места/пути к воде") : tr("Sem água por perto", "No water nearby", "Sin agua cerca", "Нет воды рядом");
      return null;
    } 
    Seq<BuildPlan> seq2 = new Seq();
    HashSet<Integer> hashSet = new HashSet();
    BuildPlan buildPlan1 = null;
    for (BuildPlan buildPlan : seq1) {
      seq2.add(buildPlan);
      footprint(buildPlan.block, buildPlan.x, buildPlan.y, hashSet);
      if (buildPlan.block instanceof PowerNode && buildPlan1 == null)
        buildPlan1 = buildPlan; 
    } 
    for (int[] arrayOfInt1 : arrayList2) {
      seq2.add(new BuildPlan(arrayOfInt1[0], arrayOfInt1[1], arrayOfInt1[2], block3));
      hashSet.add(Integer.valueOf(arrayOfInt1[1] * i + arrayOfInt1[0]));
    } 
    BuildPlan buildPlan2 = new BuildPlan(arrayOfInt[0], arrayOfInt[1], 0, block2);
    seq2.add(buildPlan2);
    footprint(block2, arrayOfInt[0], arrayOfInt[1], hashSet);
    if (AutoSetConfig.autoPower())
      addPower(seq2, hashSet, arrayOfBoolean, buildPlan2, buildPlan1, i); 
    lastTiles.addAll(hashSet);
    return seq2;
  }
  
  private static int[] snap(Block paramBlock, double paramDouble1, double paramDouble2, boolean[] paramArrayOfboolean, HashSet<Integer> paramHashSet, int paramInt) {
    int i = (int)Math.round(paramDouble1), j = (int)Math.round(paramDouble2);
    for (byte b = 0; b <= 3; b++) {
      for (byte b1 = -b; b1 <= b; b1++) {
        for (byte b2 = -b; b2 <= b; b2++) {
          if (Math.max(Math.abs(b2), Math.abs(b1)) == b) {
            int k = i + b2, m = j + b1;
            if (freeArea(paramBlock, k, m, paramArrayOfboolean, paramHashSet) && canPlace(paramBlock, k, m, 0))
              return new int[] { k, m }; 
          } 
        } 
      } 
    } 
    return null;
  }
  
  private static void addPower(Seq<BuildPlan> paramSeq, HashSet<Integer> paramHashSet, boolean[] paramArrayOfboolean, BuildPlan paramBuildPlan1, BuildPlan paramBuildPlan2, int paramInt) {
    Block block1 = Vars.content.block("power-node");
    Block block2 = Vars.content.block("large-power-node");
    Block block3 = Vars.content.block("solar-panel");
    if (block1 == null || paramBuildPlan2 == null)
      return; 
    double d1 = paramBuildPlan1.x + 0.5D, d2 = paramBuildPlan1.y + 0.5D;
    double d3 = paramBuildPlan2.x, d4 = paramBuildPlan2.y;
    double d5 = Math.hypot(d3 - d1, d4 - d2);
    double d6 = (d5 < 0.01D) ? 1.0D : ((d3 - d1) / d5), d7 = (d5 < 0.01D) ? 0.0D : ((d4 - d2) / d5);
    Block block4 = block1;
    byte b1 = 0;
    for (byte b2 = 0; b2 < 2; b2++) {
      Block block = (b2 == 0 && AutoSetConfig.bigNode() && block2 != null) ? block2 : block1;
      double d11 = ((PowerNode)block).laserRange;
      double d12 = Math.max(2.0D, d11 - 2.0D);
      byte b = 1;
      double d13 = Math.min(2.5D, d5);
      while (d5 - d13 > d11 - 1.5D && b < 40) {
        d13 += d12;
        b++;
      } 
      if (block == block1 || affordable(block, b)) {
        block4 = block;
        b1 = b;
        break;
      } 
    } 
    double d8 = ((PowerNode)block4).laserRange;
    double d9 = Math.max(2.0D, d8 - 2.0D);
    ArrayList<BuildPlan> arrayList = new ArrayList();
    if (block3 != null)
      for (byte b = 0; b < 2; b++) {
        int[] arrayOfInt = snap(block3, d1 + d6 * 2.0D + ((b == 0) ? -d7 : d7) * 1.5D, d2 + d7 * 2.0D + ((b == 0) ? d6 : -d6) * 1.5D, paramArrayOfboolean, paramHashSet, paramInt);
        if (arrayOfInt != null) {
          BuildPlan buildPlan = new BuildPlan(arrayOfInt[0], arrayOfInt[1], 0, block3);
          arrayList.add(buildPlan);
          paramSeq.add(buildPlan);
          paramHashSet.add(Integer.valueOf(arrayOfInt[1] * paramInt + arrayOfInt[0]));
        } 
      }  
    ArrayList<int[]> arrayList1 = new ArrayList();
    double d10 = Math.min(2.5D, d5);
    int i;
    for (i = 0; i < b1; i++) {
      int[] arrayOfInt = snap(block4, d1 + d6 * d10, d2 + d7 * d10, paramArrayOfboolean, paramHashSet, paramInt);
      if (arrayOfInt == null)
        break; 
      arrayList1.add(arrayOfInt);
      paramHashSet.add(Integer.valueOf(arrayOfInt[1] * paramInt + arrayOfInt[0]));
      d10 += d9;
    } 
    for (i = arrayList1.size() - 1; i >= 0; i--) {
      int[] arrayOfInt = arrayList1.get(i);
      ArrayList<Point2> arrayList2 = new ArrayList();
      if (i == arrayList1.size() - 1) {
        arrayList2.add(new Point2(paramBuildPlan2.x - arrayOfInt[0], paramBuildPlan2.y - arrayOfInt[1]));
      } else {
        int[] arrayOfInt1 = arrayList1.get(i + 1);
        arrayList2.add(new Point2(arrayOfInt1[0] - arrayOfInt[0], arrayOfInt1[1] - arrayOfInt[1]));
      } 
      if (i == 0) {
        arrayList2.add(new Point2(paramBuildPlan1.x - arrayOfInt[0], paramBuildPlan1.y - arrayOfInt[1]));
        for (BuildPlan buildPlan : arrayList)
          arrayList2.add(new Point2(buildPlan.x - arrayOfInt[0], buildPlan.y - arrayOfInt[1])); 
      } 
      paramSeq.add(new BuildPlan(arrayOfInt[0], arrayOfInt[1], 0, block4, arrayList2.toArray(new Point2[0])));
    } 
  }
}
