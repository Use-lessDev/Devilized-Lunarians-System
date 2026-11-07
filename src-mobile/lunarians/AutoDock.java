package lunarians;

import arc.Core;
import arc.Events;
import arc.Settings;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.Time;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.core.ContentLoader;
import mindustry.core.GameState;
import mindustry.core.World;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.game.Rules;
import mindustry.game.Schematic;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.blocks.power.PowerGenerator;
import mindustry.world.blocks.power.PowerGraph;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.storage.StorageBlock;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumePower;
import mindustry.world.modules.ItemModule;
import mindustry.world.modules.PowerModule;

public class AutoDock {
  static final String[][] ORDER;
  
  private static boolean camHeld;
  
  private static float centerX;
  
  private static float centerY;
  
  private static Seq current;
  
  private static boolean[] done;
  
  private static int energyBuilt;
  
  private static int energyFails = 0;
  
  private static boolean energyStep;
  
  private static int idx;
  
  private static String lastToast;
  
  private static float lastToastAt;
  
  private static float nextCheck;
  
  private static AutoDock$Phase phase;
  
  private static float phaseStart;
  
  private static boolean prevDetach;
  
  private static int stuckRetries;
  
  private static boolean unitsFinished;
  
  private static boolean unitsOnly;
  
  static {
    energyBuilt = 0;
    unitsOnly = false;
    unitsFinished = false;
    done = new boolean[arrayOfString.length];
    phase = AutoDock$Phase.NEXT;
    idx = 0;
    Seq seq = new Seq();
    this();
    current = seq;
    lastToast = str2;
    lastToastAt = -999.0F;
    energyStep = false;
    stuckRetries = 0;
    camHeld = false;
    prevDetach = false;
  }
  
  private static void abort(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    this("Auto Dock: ");
    stringBuilder.append(paramString);
    stringBuilder.append(". Tentando de novo depois.");
    toast(stringBuilder.toString());
    Vars.player.unit().plans().clear();
    releaseCamera();
    phase = AutoDock$Phase.NEXT;
    nextCheck = Time.time + 1200.0F;
  }
  
  private static boolean alreadyBuilt(Seq paramSeq, CoreBlock.CoreBuild paramCoreBuild) {
    Iterator<BuildPlan> iterator = paramSeq.iterator();
    Block block = null;
    int i = 0;
    while (true) {
      boolean bool = iterator.hasNext();
      if (bool) {
        BuildPlan buildPlan = iterator.next();
        Consume[] arrayOfConsume = buildPlan.block.consumers;
        int k = arrayOfConsume.length;
        if (k > 0) {
          Block block1 = buildPlan.block;
          k = block1.size;
          int m = 2;
          if (k >= m) {
            block1 = buildPlan.block;
            boolean bool1 = block1 instanceof StorageBlock;
            if (!bool1) {
              Iterator iterator2 = paramSeq.iterator();
              m = 0;
              Object object = null;
              while (true) {
                boolean bool2 = iterator2.hasNext();
                if (bool2) {
                  Block block2 = ((BuildPlan)iterator2.next()).block;
                  Block block3 = buildPlan.block;
                  if (block2 == block3)
                    m++; 
                  continue;
                } 
                if (m > i) {
                  block = buildPlan.block;
                  i = m;
                } 
              } 
              break;
            } 
          } 
        } 
        continue;
      } 
      if (block == null)
        return false; 
      Iterator<Building> iterator1 = Groups.build.iterator();
      int j = 0;
      iterator = null;
      while (true) {
        bool = iterator1.hasNext();
        if (bool) {
          Building building = iterator1.next();
          Team team1 = building.team;
          Team team2 = Vars.player.team();
          if (team1 == team2) {
            Block block1 = building.block;
            if (block1 == block) {
              int k = 1139802112;
              float f = 480.0F;
              bool = building.within((Position)paramCoreBuild, f);
              if (bool)
                j++; 
            } 
          } 
          continue;
        } 
        return (j >= i);
      } 
      break;
    } 
  }
  
  private static Seq bestDock(CoreBlock.CoreBuild paramCoreBuild, Seq paramSeq, Unit paramUnit) {
    boolean bool = true;
    Seq seq = bestDockImpl(paramCoreBuild, paramSeq, paramUnit, bool);
    return (seq != null) ? seq : bestDockImpl(paramCoreBuild, paramSeq, paramUnit, false);
  }
  
  private static Seq bestDockImpl(CoreBlock.CoreBuild paramCoreBuild, Seq paramSeq, Unit paramUnit, boolean paramBoolean) {
    ArrayList arrayList = dockRects(paramCoreBuild);
    Iterator<int[]> iterator = arrayList.iterator();
    int i = 1048576;
    float f1 = 1.469368E-39F;
    int j = 1048576;
    float f2 = 1.469368E-39F;
    int k = -1048576;
    float f3 = 0.0F / 0.0F;
    int m = -1048576;
    float f4 = 0.0F / 0.0F;
    while (true) {
      boolean bool = iterator.hasNext();
      int n = 3;
      int i1 = 2;
      int i2 = 0;
      Team team = null;
      int i3 = 1;
      float f = Float.MIN_VALUE;
      if (bool) {
        int[] arrayOfInt = iterator.next();
        i2 = arrayOfInt[0];
        i = Math.min(i, i2);
        i2 = arrayOfInt[i3];
        j = Math.min(j, i2);
        i1 = arrayOfInt[i1];
        k = Math.max(k, i1);
        int i4 = arrayOfInt[n];
        m = Math.max(m, i4);
        continue;
      } 
      iterator = paramSeq.iterator();
      bool = false;
      Object object = null;
      boolean bool1 = false;
      while (true) {
        int i4;
        boolean bool2 = iterator.hasNext();
        if (bool2) {
          BuildPlan buildPlan = (BuildPlan)iterator.next();
          Block block2 = buildPlan.block;
          Block block3 = Blocks.unloader;
          if (block2 == block3)
            i4 = bool + 1; 
          Block block1 = buildPlan.block;
          bool2 = block1 instanceof StorageBlock;
          if (bool2)
            bool1 = true; 
          continue;
        } 
        Seq seq1 = copyAll(paramSeq);
        bool2 = false;
        Seq seq2 = null;
        float f5 = -1.0E9F;
        int i5 = 0;
        Object object1 = null;
        while (true) {
          byte b = 4;
          if (i5 < b) {
            int[] arrayOfInt = bounds(seq1);
            int i6 = arrayOfInt[i1];
            i6 = i - i6 + -1;
            n = i6;
            byte b1 = 3;
            while (true) {
              i6 = arrayOfInt[0];
              i6 = k - i6;
              byte b2 = 2;
              i1 = i6 + 1;
              if (n <= i1) {
                i1 = arrayOfInt[b1];
                i1 = j - i1 - i3;
                while (true) {
                  i6 = arrayOfInt[i3];
                  i6 = m - i6;
                  boolean bool3 = true;
                  i3 = i6 + 1;
                  if (i1 <= i3) {
                    int i8;
                    int i9;
                    int i10;
                    Seq seq4;
                    Iterator<Object> iterator1 = seq1.iterator();
                    int i7 = i;
                    i = 0;
                    f1 = 0.0F;
                    Iterator<BuildPlan> iterator2 = null;
                    i6 = 0;
                    while (true) {
                      Seq seq;
                      int i11;
                      boolean bool4 = iterator1.hasNext();
                      if (bool4) {
                        BuildPlan buildPlan1 = (BuildPlan)iterator1.next();
                        i9 = j;
                        BuildPlan buildPlan2 = buildPlan1;
                        buildPlan2 = buildPlan1;
                        i8 = k;
                        k = buildPlan2.x + n;
                        i10 = m;
                        m = buildPlan2.y + i1;
                        Iterator<Object> iterator3 = iterator1;
                        Block block = buildPlan2.block;
                        i3 = block.size;
                        int[] arrayOfInt1 = rect(k, m, i3);
                        boolean bool5 = overlapsAny(arrayOfInt1, arrayList);
                        if (!bool5) {
                          bool5 = touchesAny(buildPlan2, n, i1, arrayList);
                          if (bool5) {
                            Block block1 = buildPlan2.block;
                            bool5 = block1 instanceof StorageBlock;
                            if (bool5) {
                              i2++;
                            } else {
                              Block block2 = buildPlan2.block;
                              block1 = Blocks.unloader;
                              if (block2 == block1)
                                i++; 
                            } 
                          } 
                          Iterator<Object> iterator4 = iterator3;
                          i11 = i8;
                          j = i9;
                          m = i10;
                          continue;
                        } 
                      } else {
                        i9 = j;
                        i8 = i11;
                        i10 = m;
                        if ((!bool1 ? (i == 0) : (i2 == 0)) && (!paramBoolean || bool1 || i >= i4)) {
                          j = ringCovered(seq1, n, i1, arrayList);
                          i11 = arrayOfInt[0];
                          m = arrayOfInt[b2];
                          i11 += m;
                          f3 = i11;
                          m = 1073741824;
                          f4 = 2.0F;
                          f3 /= f4;
                          f = n;
                          f3 += f;
                          i3 = arrayOfInt[bool3];
                          int i12 = arrayOfInt[b1];
                          i3 += i12;
                          f = i3 / f4;
                          f4 = i1;
                          f += f4;
                          f4 = i2;
                          i12 = 1120403456;
                          float f6 = 100.0F;
                          f4 *= f6;
                          ArrayList arrayList1 = arrayList;
                          float f7 = i;
                          float f8 = 10.0F;
                          f7 *= f8;
                          f4 += f7;
                          j = j - i2 - i;
                          int i13 = 0;
                          f7 = 0.0F;
                          arrayList = null;
                          f1 = Math.max(0, j) * 3.0F;
                          f4 -= f1;
                          f1 = paramUnit.tileX();
                          f2 = paramUnit.tileY();
                          f1 = Mathf.dst(f3, f, f1, f2);
                          j = 1008981770;
                          f2 = 0.01F;
                          f1 *= f2;
                          f4 -= f1;
                          i = f4 cmp f5;
                          if (i > 0) {
                            iterator2 = seq1.iterator();
                            while (true) {
                              boolean bool5 = iterator2.hasNext();
                              if (bool5) {
                                BuildPlan buildPlan = iterator2.next();
                                Block block = buildPlan.block;
                                team = Vars.player.team();
                                i3 = buildPlan.x + n;
                                i13 = buildPlan.y + i1;
                                int i14 = buildPlan.rotation;
                                boolean bool6 = Build.validPlace(block, team, i3, i13, i14);
                                if (bool6)
                                  continue; 
                                break;
                              } 
                              seq = new Seq();
                              this();
                              iterator2 = seq1.iterator();
                              while (true) {
                                bool5 = iterator2.hasNext();
                                if (bool5) {
                                  BuildPlan buildPlan = ((BuildPlan)iterator2.next()).copy();
                                  i11 = buildPlan.x + n;
                                  buildPlan.x = i11;
                                  i11 = buildPlan.y + i1;
                                  buildPlan.y = i11;
                                  seq.add(buildPlan);
                                  continue;
                                } 
                                seq2 = seq;
                                f5 = f4;
                                break;
                              } 
                              break;
                            } 
                          } 
                          break;
                        } 
                      } 
                      seq4 = seq;
                      break;
                    } 
                    i1++;
                    i = i7;
                    k = i8;
                    j = i9;
                    m = i10;
                    Seq seq3 = seq4;
                    i2 = 0;
                    team = null;
                    i3 = 1;
                    f = Float.MIN_VALUE;
                    continue;
                  } 
                  n++;
                  i2 = 0;
                  team = null;
                  i3 = 1;
                  f = Float.MIN_VALUE;
                } 
                break;
              } 
              rotate(seq1);
              i5++;
              i1 = 2;
              i2 = 0;
              team = null;
              i3 = 1;
              f = Float.MIN_VALUE;
            } 
            break;
          } 
          return seq2;
        } 
        break;
      } 
      break;
    } 
  }
  
  private static int[] bounds(Seq paramSeq) {
    Iterator<BuildPlan> iterator = paramSeq.iterator();
    int i = 1048576;
    int j = 1048576;
    int k = -1048576;
    int m = -1048576;
    while (true) {
      boolean bool = iterator.hasNext();
      if (bool) {
        BuildPlan buildPlan = iterator.next();
        int i1 = buildPlan.x;
        int i2 = buildPlan.y;
        int n = buildPlan.block.size;
        int[] arrayOfInt = rect(i1, i2, n);
        i1 = arrayOfInt[0];
        i = Math.min(i, i1);
        i1 = arrayOfInt[1];
        j = Math.min(j, i1);
        i1 = arrayOfInt[2];
        k = Math.max(k, i1);
        i1 = 3;
        n = arrayOfInt[i1];
        m = Math.max(m, n);
        continue;
      } 
      return new int[] { i, j, k, m };
    } 
  }
  
  private static void cameraGo(Unit paramUnit, float paramFloat1, float paramFloat2) {
    boolean bool = camHeld;
    String str = "detach-camera";
    if (!bool) {
      float f6 = Float.MIN_VALUE;
      camHeld = true;
      Settings settings1 = Core.settings;
      Object object = null;
      bool = settings1.getBool(str, false);
      prevDetach = bool;
      Vec2 vec2 = Core.camera.position;
      float f7 = paramUnit.x;
      float f8 = paramUnit.y;
      vec2.set(f7, f8);
    } 
    Settings settings = Core.settings;
    Boolean bool1 = Boolean.TRUE;
    settings.put(str, bool1);
    Vec2 vec22 = Core.camera.position;
    float f1 = vec22.x;
    Vec2 vec21 = Core.camera.position;
    float f4 = vec21.y;
    float f2 = Mathf.dst(f1, f4, paramFloat1, paramFloat2);
    float f3 = 1.0F;
    int i = f2 cmp f3;
    if (i < 0)
      return; 
    float f5 = paramUnit.speed() * 1.25F;
    f5 = Math.max(2.0F, f5);
    f3 = Time.delta;
    f5 *= f3;
    f5 = Math.min(f2, f5);
    Vec2 vec23 = Core.camera.position;
    paramFloat1 = (paramFloat1 - f1) / f2 * f5;
    f1 += paramFloat1;
    paramFloat2 = (paramFloat2 - f4) / f2 * f5;
    f4 += paramFloat2;
    vec23.set(f1, f4);
  }
  
  private static Seq copyAll(Seq paramSeq) {
    Seq seq = new Seq();
    this();
    Iterator<BuildPlan> iterator = paramSeq.iterator();
    while (true) {
      boolean bool = iterator.hasNext();
      if (bool) {
        BuildPlan buildPlan = ((BuildPlan)iterator.next()).copy();
        seq.add(buildPlan);
        continue;
      } 
      return seq;
    } 
  }
  
  public static String coreInfo() {
    Player player = Vars.player;
    if (player == null) {
      boolean bool = false;
      player = null;
    } else {
      coreBuild = Vars.player.closestCore();
    } 
    if (coreBuild == null)
      return "sem nucleo"; 
    int j = freePerimeter(coreBuild);
    StringBuilder stringBuilder = new StringBuilder();
    this();
    CoreBlock.CoreBuild coreBuild;
    String str = coreBuild.block.localizedName;
    stringBuilder.append(str);
    stringBuilder.append(" ");
    int k = coreBuild.block.size;
    stringBuilder.append(k);
    stringBuilder.append("x");
    k = coreBuild.block.size;
    stringBuilder.append(k);
    stringBuilder.append(" | capacidade ");
    int i = coreBuild.storageCapacity;
    stringBuilder.append(i);
    stringBuilder.append(" | lados livres: ");
    stringBuilder.append(j);
    stringBuilder.append(" tiles");
    return stringBuilder.toString();
  }
  
  private static ArrayList dockRects(CoreBlock.CoreBuild paramCoreBuild) {
    ArrayList<int[]> arrayList = new ArrayList();
    this();
    short s1 = paramCoreBuild.tile.x;
    Tile tile = paramCoreBuild.tile;
    short s2 = tile.y;
    Block block = paramCoreBuild.block;
    int i = block.size;
    int[] arrayOfInt = rect(s1, s2, i);
    arrayList.add(arrayOfInt);
    try {
      Seq seq = paramCoreBuild.proximity;
      Iterator<Tile> iterator = seq.iterator();
      while (true) {
        boolean bool = iterator.hasNext();
        if (bool) {
          tile = iterator.next();
          Building building = (Building)tile;
          boolean bool1 = building instanceof StorageBlock.StorageBuild;
          if (bool1) {
            Building building2 = building;
            StorageBlock.StorageBuild storageBuild = (StorageBlock.StorageBuild)building;
            Building building1 = storageBuild.linkedCore;
            if (building1 == paramCoreBuild) {
              Tile tile1 = building.tile;
              short s3 = tile1.x;
              Tile tile2 = building.tile;
              short s4 = tile2.y;
              Block block1 = building.block;
              int j = block1.size;
              int[] arrayOfInt1 = rect(s3, s4, j);
              arrayList.add(arrayOfInt1);
            } 
          } 
          continue;
        } 
        break;
      } 
    } finally {}
    return arrayList;
  }
  
  private static Seq energyCluster(CoreBlock.CoreBuild paramCoreBuild) {
    CoreBlock.CoreBuild coreBuild = paramCoreBuild;
    Tile tile = paramCoreBuild.tile;
    short s1 = tile.x;
    short s2 = paramCoreBuild.tile.y;
    int i = paramCoreBuild.block.size;
    int[] arrayOfInt1 = rect(s1, s2, i);
    s1 = 4;
    int[][] arrayOfInt = new int[s1][];
    byte b1 = 1;
    int[] arrayOfInt2 = { b1, 0 };
    arrayOfInt[0] = arrayOfInt2;
    arrayOfInt2 = new int[] { 0, b1 };
    arrayOfInt[b1] = arrayOfInt2;
    byte b = -1;
    int[] arrayOfInt3 = { b, 0 };
    byte b2 = 2;
    arrayOfInt[b2] = arrayOfInt3;
    arrayOfInt3 = new int[] { 0, b };
    byte b3 = 3;
    arrayOfInt[b3] = arrayOfInt3;
    int j = arrayOfInt1[0] - b1;
    int k = j;
    while (true) {
      j = arrayOfInt1[b2] + b1;
      if (k <= j) {
        j = arrayOfInt1[b1] - b1;
        int m = j;
        while (true) {
          j = arrayOfInt1[b3];
          int n = j + 1;
          k++;
          s1 = 4;
          b1 = 1;
        } 
        break;
      } 
      return null;
    } 
  }
  
  private static Seq fieldPlan(CoreBlock.CoreBuild paramCoreBuild, Seq paramSeq, String paramString, Unit paramUnit) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #4
    //   3: aconst_null
    //   4: astore #5
    //   6: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   9: astore #6
    //   11: aload_2
    //   12: astore #7
    //   14: aload #6
    //   16: aload_2
    //   17: invokevirtual item : (Ljava/lang/String;)Lmindustry/type/Item;
    //   20: astore #6
    //   22: aload #6
    //   24: ifnonnull -> 29
    //   27: aconst_null
    //   28: areturn
    //   29: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   32: astore #7
    //   34: aload #7
    //   36: invokevirtual width : ()I
    //   39: istore #8
    //   41: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   44: astore #9
    //   46: aload #9
    //   48: invokevirtual height : ()I
    //   51: istore #10
    //   53: aload_0
    //   54: invokevirtual tileX : ()I
    //   57: istore #11
    //   59: aload_0
    //   60: invokevirtual tileY : ()I
    //   63: istore #12
    //   65: iload #8
    //   67: iload #10
    //   69: imul
    //   70: istore #13
    //   72: iload #13
    //   74: newarray boolean
    //   76: astore #14
    //   78: iload #12
    //   80: bipush #-55
    //   82: iadd
    //   83: istore #15
    //   85: iload #12
    //   87: bipush #-75
    //   89: iadd
    //   90: istore #16
    //   92: iconst_0
    //   93: istore #17
    //   95: fconst_0
    //   96: fstore #18
    //   98: iconst_0
    //   99: iload #16
    //   101: invokestatic max : (II)I
    //   104: istore #16
    //   106: iload #10
    //   108: iconst_m1
    //   109: iadd
    //   110: istore #19
    //   112: iload #12
    //   114: bipush #55
    //   116: iadd
    //   117: istore #20
    //   119: iload #12
    //   121: bipush #75
    //   123: iadd
    //   124: istore #21
    //   126: iload #19
    //   128: iload #21
    //   130: invokestatic min : (II)I
    //   133: istore #19
    //   135: iconst_1
    //   136: istore #21
    //   138: iload #16
    //   140: iload #19
    //   142: if_icmpgt -> 283
    //   145: iload #11
    //   147: bipush #-75
    //   149: iadd
    //   150: istore #19
    //   152: iconst_0
    //   153: iload #19
    //   155: invokestatic max : (II)I
    //   158: istore #19
    //   160: iload #8
    //   162: iconst_m1
    //   163: iadd
    //   164: istore #20
    //   166: iload #11
    //   168: bipush #75
    //   170: iadd
    //   171: istore #22
    //   173: iload #20
    //   175: iload #22
    //   177: invokestatic min : (II)I
    //   180: istore #20
    //   182: iload #19
    //   184: iload #20
    //   186: if_icmpgt -> 274
    //   189: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   192: astore #23
    //   194: aload #23
    //   196: iload #19
    //   198: iload #16
    //   200: invokevirtual tile : (II)Lmindustry/world/Tile;
    //   203: astore #23
    //   205: aload #23
    //   207: ifnull -> 265
    //   210: aload #23
    //   212: getfield build : Lmindustry/gen/Building;
    //   215: astore #24
    //   217: aload #24
    //   219: ifnonnull -> 265
    //   222: aload #23
    //   224: invokevirtual solid : ()Z
    //   227: istore #22
    //   229: iload #22
    //   231: ifne -> 265
    //   234: aload #23
    //   236: invokevirtual drop : ()Lmindustry/type/Item;
    //   239: astore #23
    //   241: aload #23
    //   243: aload #6
    //   245: if_acmpne -> 265
    //   248: iload #16
    //   250: iload #8
    //   252: imul
    //   253: iload #19
    //   255: iadd
    //   256: istore #20
    //   258: aload #14
    //   260: iload #20
    //   262: iload #21
    //   264: bastore
    //   265: iload #19
    //   267: iconst_1
    //   268: iadd
    //   269: istore #19
    //   271: goto -> 160
    //   274: iload #16
    //   276: iconst_1
    //   277: iadd
    //   278: istore #16
    //   280: goto -> 106
    //   283: aload_3
    //   284: invokestatic reservedFromPlans : (Lmindustry/gen/Unit;)[Z
    //   287: astore #6
    //   289: aload_1
    //   290: invokestatic copyAll : (Larc/struct/Seq;)Larc/struct/Seq;
    //   293: astore #25
    //   295: aload #25
    //   297: invokevirtual iterator : ()Ljava/util/Iterator;
    //   300: astore #26
    //   302: iconst_0
    //   303: istore #22
    //   305: aconst_null
    //   306: astore #24
    //   308: aload #26
    //   310: invokeinterface hasNext : ()Z
    //   315: istore #27
    //   317: iload #27
    //   319: ifeq -> 366
    //   322: aload #26
    //   324: invokeinterface next : ()Ljava/lang/Object;
    //   329: astore #28
    //   331: aload #28
    //   333: checkcast mindustry/entities/units/BuildPlan
    //   336: astore #28
    //   338: aload #28
    //   340: getfield block : Lmindustry/world/Block;
    //   343: astore #28
    //   345: aload #28
    //   347: instanceof mindustry/world/blocks/production/Drill
    //   350: istore #27
    //   352: iload #27
    //   354: ifeq -> 308
    //   357: iload #22
    //   359: iconst_1
    //   360: iadd
    //   361: istore #22
    //   363: goto -> 308
    //   366: iload #22
    //   368: ifne -> 373
    //   371: aconst_null
    //   372: areturn
    //   373: new java/util/ArrayList
    //   376: astore #26
    //   378: aload #26
    //   380: invokespecial <init> : ()V
    //   383: iconst_0
    //   384: istore #27
    //   386: aconst_null
    //   387: astore #28
    //   389: iconst_4
    //   390: istore #29
    //   392: iconst_3
    //   393: istore #30
    //   395: iconst_2
    //   396: istore #31
    //   398: iload #27
    //   400: iload #29
    //   402: if_icmpge -> 1130
    //   405: aload #25
    //   407: invokestatic bounds : (Larc/struct/Seq;)[I
    //   410: astore #32
    //   412: aload #32
    //   414: iconst_0
    //   415: iaload
    //   416: istore #33
    //   418: aload #32
    //   420: iload #31
    //   422: iaload
    //   423: istore #34
    //   425: iload #33
    //   427: iload #34
    //   429: iadd
    //   430: istore #33
    //   432: iload #33
    //   434: iconst_2
    //   435: idiv
    //   436: istore #33
    //   438: aload #32
    //   440: iload #21
    //   442: iaload
    //   443: istore #34
    //   445: aload #32
    //   447: iload #30
    //   449: iaload
    //   450: istore #35
    //   452: iload #34
    //   454: iload #35
    //   456: iadd
    //   457: istore #34
    //   459: iload #34
    //   461: iconst_2
    //   462: idiv
    //   463: istore #34
    //   465: iload #11
    //   467: bipush #-55
    //   469: iadd
    //   470: iload #33
    //   472: isub
    //   473: istore #35
    //   475: iload #35
    //   477: istore #4
    //   479: iload #11
    //   481: bipush #55
    //   483: iadd
    //   484: istore #35
    //   486: aconst_null
    //   487: astore_2
    //   488: iload #35
    //   490: iload #33
    //   492: isub
    //   493: istore #17
    //   495: iload #4
    //   497: iload #17
    //   499: if_icmpgt -> 1085
    //   502: iload #15
    //   504: iload #34
    //   506: isub
    //   507: istore #17
    //   509: iconst_1
    //   510: istore #35
    //   512: iload #20
    //   514: iload #34
    //   516: isub
    //   517: istore #21
    //   519: iload #17
    //   521: iload #21
    //   523: if_icmpgt -> 1076
    //   526: aload #25
    //   528: invokevirtual iterator : ()Ljava/util/Iterator;
    //   531: astore #36
    //   533: iconst_0
    //   534: istore #29
    //   536: aconst_null
    //   537: astore #37
    //   539: aload #36
    //   541: invokeinterface hasNext : ()Z
    //   546: istore #38
    //   548: iload #38
    //   550: ifeq -> 879
    //   553: aload #36
    //   555: invokeinterface next : ()Ljava/lang/Object;
    //   560: astore #39
    //   562: aload #6
    //   564: astore #40
    //   566: aload #39
    //   568: astore #6
    //   570: aload #39
    //   572: checkcast mindustry/entities/units/BuildPlan
    //   575: astore #6
    //   577: aload #14
    //   579: astore #39
    //   581: aload #6
    //   583: getfield block : Lmindustry/world/Block;
    //   586: astore #14
    //   588: aload #14
    //   590: instanceof mindustry/world/blocks/production/Drill
    //   593: istore #13
    //   595: iload #13
    //   597: ifne -> 611
    //   600: aload #39
    //   602: astore #14
    //   604: aload #40
    //   606: astore #6
    //   608: goto -> 539
    //   611: aload #6
    //   613: getfield x : I
    //   616: iload #4
    //   618: iadd
    //   619: istore #13
    //   621: iload #15
    //   623: istore #41
    //   625: aload #6
    //   627: getfield y : I
    //   630: iload #17
    //   632: iadd
    //   633: istore #15
    //   635: aload #25
    //   637: astore #42
    //   639: aload #6
    //   641: getfield block : Lmindustry/world/Block;
    //   644: astore #25
    //   646: aload #25
    //   648: getfield size : I
    //   651: istore #16
    //   653: iload #13
    //   655: iload #15
    //   657: iload #16
    //   659: invokestatic rect : (III)[I
    //   662: astore #14
    //   664: aload #14
    //   666: iconst_0
    //   667: iaload
    //   668: istore #15
    //   670: aload #14
    //   672: astore #43
    //   674: iload #15
    //   676: istore #16
    //   678: iconst_0
    //   679: istore #15
    //   681: fconst_0
    //   682: fstore #44
    //   684: aconst_null
    //   685: astore #45
    //   687: aload #43
    //   689: iload #31
    //   691: iaload
    //   692: istore #13
    //   694: iload #16
    //   696: iload #13
    //   698: if_icmpgt -> 804
    //   701: aload #43
    //   703: iload #35
    //   705: iaload
    //   706: istore #13
    //   708: iload #15
    //   710: istore #46
    //   712: aload #43
    //   714: iload #30
    //   716: iaload
    //   717: istore #15
    //   719: iload #13
    //   721: iload #15
    //   723: if_icmpgt -> 791
    //   726: iload #16
    //   728: iflt -> 782
    //   731: iload #13
    //   733: iflt -> 782
    //   736: iload #16
    //   738: iload #8
    //   740: if_icmpge -> 782
    //   743: iload #13
    //   745: iload #10
    //   747: if_icmpge -> 782
    //   750: iload #13
    //   752: iload #8
    //   754: imul
    //   755: iload #16
    //   757: iadd
    //   758: istore #15
    //   760: aload #39
    //   762: iload #15
    //   764: baload
    //   765: istore #15
    //   767: iload #15
    //   769: ifeq -> 782
    //   772: iload #46
    //   774: iconst_1
    //   775: iadd
    //   776: istore #15
    //   778: iload #15
    //   780: istore #46
    //   782: iload #13
    //   784: iconst_1
    //   785: iadd
    //   786: istore #13
    //   788: goto -> 712
    //   791: iload #16
    //   793: iconst_1
    //   794: iadd
    //   795: istore #16
    //   797: iload #46
    //   799: istore #15
    //   801: goto -> 687
    //   804: iload #15
    //   806: iconst_2
    //   807: ishl
    //   808: istore #15
    //   810: aload #6
    //   812: getfield block : Lmindustry/world/Block;
    //   815: astore #14
    //   817: aload #14
    //   819: getfield size : I
    //   822: istore #13
    //   824: aload #6
    //   826: getfield block : Lmindustry/world/Block;
    //   829: astore #6
    //   831: aload #6
    //   833: getfield size : I
    //   836: istore #47
    //   838: iload #13
    //   840: iload #47
    //   842: imul
    //   843: iconst_3
    //   844: imul
    //   845: istore #13
    //   847: iload #15
    //   849: iload #13
    //   851: if_icmplt -> 860
    //   854: iload #29
    //   856: iconst_1
    //   857: iadd
    //   858: istore #29
    //   860: aload #39
    //   862: astore #14
    //   864: aload #40
    //   866: astore #6
    //   868: iload #41
    //   870: istore #15
    //   872: aload #42
    //   874: astore #25
    //   876: goto -> 539
    //   879: aload #6
    //   881: astore #40
    //   883: aload #14
    //   885: astore #39
    //   887: iload #15
    //   889: istore #41
    //   891: aload #25
    //   893: astore #42
    //   895: iload #29
    //   897: bipush #10
    //   899: imul
    //   900: istore #47
    //   902: iload #22
    //   904: bipush #6
    //   906: imul
    //   907: istore #13
    //   909: iload #47
    //   911: iload #13
    //   913: if_icmplt -> 1051
    //   916: iload #33
    //   918: iload #4
    //   920: iadd
    //   921: istore #47
    //   923: iload #47
    //   925: i2f
    //   926: fstore #48
    //   928: iload #34
    //   930: iload #17
    //   932: iadd
    //   933: istore #13
    //   935: iload #13
    //   937: i2f
    //   938: fstore #49
    //   940: iload #11
    //   942: i2f
    //   943: fstore #44
    //   945: iload #12
    //   947: i2f
    //   948: fstore #50
    //   950: fload #48
    //   952: fload #49
    //   954: fload #44
    //   956: fload #50
    //   958: invokestatic dst : (FFFF)F
    //   961: fstore #48
    //   963: iload #29
    //   965: i2f
    //   966: ldc_w 10.0
    //   969: fmul
    //   970: fstore #49
    //   972: ldc_w 1050253722
    //   975: istore #15
    //   977: fload #48
    //   979: ldc_w 0.3
    //   982: fmul
    //   983: fstore #48
    //   985: fload #49
    //   987: fload #48
    //   989: fsub
    //   990: fstore #49
    //   992: iload #27
    //   994: i2f
    //   995: fstore #48
    //   997: iload #4
    //   999: i2f
    //   1000: fstore #44
    //   1002: iload #17
    //   1004: i2f
    //   1005: fstore #50
    //   1007: iconst_4
    //   1008: istore #21
    //   1010: iload #21
    //   1012: newarray float
    //   1014: astore #37
    //   1016: aload #37
    //   1018: iconst_0
    //   1019: fload #49
    //   1021: fastore
    //   1022: aload #37
    //   1024: iload #35
    //   1026: fload #48
    //   1028: fastore
    //   1029: aload #37
    //   1031: iload #31
    //   1033: fload #44
    //   1035: fastore
    //   1036: aload #37
    //   1038: iload #30
    //   1040: fload #50
    //   1042: fastore
    //   1043: aload #26
    //   1045: aload #37
    //   1047: invokevirtual add : (Ljava/lang/Object;)Z
    //   1050: pop
    //   1051: iload #17
    //   1053: iconst_1
    //   1054: iadd
    //   1055: istore #17
    //   1057: aload #39
    //   1059: astore #14
    //   1061: aload #40
    //   1063: astore #6
    //   1065: iload #41
    //   1067: istore #15
    //   1069: aload #42
    //   1071: astore #25
    //   1073: goto -> 512
    //   1076: iload #4
    //   1078: iconst_1
    //   1079: iadd
    //   1080: istore #4
    //   1082: goto -> 479
    //   1085: aload #6
    //   1087: astore #40
    //   1089: aload #14
    //   1091: astore #39
    //   1093: iload #15
    //   1095: istore #41
    //   1097: aload #25
    //   1099: astore #42
    //   1101: aload #25
    //   1103: invokestatic rotate : (Larc/struct/Seq;)V
    //   1106: iload #27
    //   1108: iconst_1
    //   1109: iadd
    //   1110: istore #27
    //   1112: iconst_0
    //   1113: istore #4
    //   1115: aconst_null
    //   1116: astore #5
    //   1118: iconst_0
    //   1119: istore #17
    //   1121: fconst_0
    //   1122: fstore #18
    //   1124: iconst_1
    //   1125: istore #21
    //   1127: goto -> 389
    //   1130: aload #6
    //   1132: astore #40
    //   1134: aconst_null
    //   1135: astore_2
    //   1136: iconst_1
    //   1137: istore #35
    //   1139: new lunarians/AutoDock$1
    //   1142: astore #6
    //   1144: aload #6
    //   1146: invokespecial <init> : ()V
    //   1149: aload #26
    //   1151: aload #6
    //   1153: invokestatic sort : (Ljava/util/List;Ljava/util/Comparator;)V
    //   1156: aload_0
    //   1157: invokestatic dockRects : (Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;)Ljava/util/ArrayList;
    //   1160: astore #6
    //   1162: aload #26
    //   1164: invokevirtual iterator : ()Ljava/util/Iterator;
    //   1167: astore #5
    //   1169: iconst_0
    //   1170: istore #13
    //   1172: fconst_0
    //   1173: fstore #49
    //   1175: aconst_null
    //   1176: astore #14
    //   1178: aload #5
    //   1180: invokeinterface hasNext : ()Z
    //   1185: istore #15
    //   1187: iload #15
    //   1189: ifeq -> 2438
    //   1192: aload #5
    //   1194: invokeinterface next : ()Ljava/lang/Object;
    //   1199: astore #45
    //   1201: aload #45
    //   1203: checkcast [F
    //   1206: astore #45
    //   1208: iload #13
    //   1210: iconst_1
    //   1211: iadd
    //   1212: istore #16
    //   1214: bipush #40
    //   1216: istore #17
    //   1218: ldc_w 5.6E-44
    //   1221: fstore #18
    //   1223: iload #13
    //   1225: iload #17
    //   1227: if_icmpge -> 2438
    //   1230: aload_1
    //   1231: invokestatic copyAll : (Larc/struct/Seq;)Larc/struct/Seq;
    //   1234: astore #14
    //   1236: iconst_0
    //   1237: istore #17
    //   1239: fconst_0
    //   1240: fstore #18
    //   1242: aload #45
    //   1244: iload #35
    //   1246: faload
    //   1247: fstore #51
    //   1249: fload #51
    //   1251: f2i
    //   1252: istore #19
    //   1254: iload #17
    //   1256: iload #19
    //   1258: if_icmpge -> 1275
    //   1261: aload #14
    //   1263: invokestatic rotate : (Larc/struct/Seq;)V
    //   1266: iload #17
    //   1268: iconst_1
    //   1269: iadd
    //   1270: istore #17
    //   1272: goto -> 1242
    //   1275: aload #45
    //   1277: iload #31
    //   1279: faload
    //   1280: fstore #18
    //   1282: fload #18
    //   1284: f2i
    //   1285: istore #17
    //   1287: aload #45
    //   1289: iload #30
    //   1291: faload
    //   1292: fstore #44
    //   1294: fload #44
    //   1296: f2i
    //   1297: istore #15
    //   1299: new java/util/HashSet
    //   1302: astore #26
    //   1304: aload #26
    //   1306: invokespecial <init> : ()V
    //   1309: aload #14
    //   1311: invokevirtual iterator : ()Ljava/util/Iterator;
    //   1314: astore #23
    //   1316: iconst_1
    //   1317: istore #21
    //   1319: aload #23
    //   1321: invokeinterface hasNext : ()Z
    //   1326: istore #22
    //   1328: iload #22
    //   1330: ifeq -> 1726
    //   1333: aload #23
    //   1335: invokeinterface next : ()Ljava/lang/Object;
    //   1340: astore #24
    //   1342: aload #24
    //   1344: checkcast mindustry/entities/units/BuildPlan
    //   1347: astore #24
    //   1349: aload #24
    //   1351: getfield x : I
    //   1354: iload #17
    //   1356: iadd
    //   1357: istore #27
    //   1359: aload #24
    //   1361: iload #27
    //   1363: putfield x : I
    //   1366: aload #24
    //   1368: getfield y : I
    //   1371: iload #15
    //   1373: iadd
    //   1374: istore #27
    //   1376: aload #24
    //   1378: iload #27
    //   1380: putfield y : I
    //   1383: aload #24
    //   1385: getfield x : I
    //   1388: istore #27
    //   1390: aload #24
    //   1392: getfield y : I
    //   1395: istore #29
    //   1397: aload #5
    //   1399: astore_0
    //   1400: aload #24
    //   1402: getfield block : Lmindustry/world/Block;
    //   1405: astore #5
    //   1407: aload #5
    //   1409: getfield size : I
    //   1412: istore #4
    //   1414: iload #27
    //   1416: iload #29
    //   1418: iload #4
    //   1420: invokestatic rect : (III)[I
    //   1423: astore #5
    //   1425: aload #5
    //   1427: iconst_0
    //   1428: iaload
    //   1429: bipush #-2
    //   1431: iadd
    //   1432: istore #27
    //   1434: aload #5
    //   1436: iload #35
    //   1438: iaload
    //   1439: bipush #-2
    //   1441: iadd
    //   1442: istore #29
    //   1444: aload #5
    //   1446: iload #31
    //   1448: iaload
    //   1449: istore #33
    //   1451: aload #5
    //   1453: astore_3
    //   1454: iload #33
    //   1456: iconst_2
    //   1457: iadd
    //   1458: istore #4
    //   1460: aload #5
    //   1462: iload #30
    //   1464: iaload
    //   1465: istore #33
    //   1467: iload #15
    //   1469: istore #34
    //   1471: iload #33
    //   1473: iconst_2
    //   1474: iadd
    //   1475: istore #15
    //   1477: iconst_4
    //   1478: newarray int
    //   1480: dup
    //   1481: dup2
    //   1482: iconst_0
    //   1483: iload #27
    //   1485: iastore
    //   1486: iconst_1
    //   1487: iload #29
    //   1489: iastore
    //   1490: iconst_2
    //   1491: iload #4
    //   1493: iastore
    //   1494: dup
    //   1495: iconst_3
    //   1496: iload #15
    //   1498: iastore
    //   1499: astore #5
    //   1501: aload #5
    //   1503: aload #6
    //   1505: invokestatic overlapsAny : ([ILjava/util/ArrayList;)Z
    //   1508: istore #4
    //   1510: iload #4
    //   1512: ifne -> 1717
    //   1515: aload_3
    //   1516: iconst_0
    //   1517: iaload
    //   1518: istore #4
    //   1520: aload_3
    //   1521: iload #31
    //   1523: iaload
    //   1524: istore #15
    //   1526: iload #4
    //   1528: iload #15
    //   1530: if_icmpgt -> 1642
    //   1533: iload #21
    //   1535: ifeq -> 1642
    //   1538: aload_3
    //   1539: iload #35
    //   1541: iaload
    //   1542: istore #15
    //   1544: aload_3
    //   1545: iload #30
    //   1547: iaload
    //   1548: istore #27
    //   1550: iload #15
    //   1552: iload #27
    //   1554: if_icmpgt -> 1633
    //   1557: iload #4
    //   1559: iflt -> 1627
    //   1562: iload #15
    //   1564: iflt -> 1627
    //   1567: iload #4
    //   1569: iload #8
    //   1571: if_icmpge -> 1627
    //   1574: iload #15
    //   1576: iload #10
    //   1578: if_icmpge -> 1627
    //   1581: iload #15
    //   1583: iload #8
    //   1585: imul
    //   1586: iload #4
    //   1588: iadd
    //   1589: istore #27
    //   1591: aload #40
    //   1593: iload #27
    //   1595: baload
    //   1596: istore #29
    //   1598: iload #29
    //   1600: ifne -> 1627
    //   1603: iload #27
    //   1605: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1608: astore #28
    //   1610: aload #26
    //   1612: aload #28
    //   1614: invokevirtual add : (Ljava/lang/Object;)Z
    //   1617: pop
    //   1618: iload #15
    //   1620: iconst_1
    //   1621: iadd
    //   1622: istore #15
    //   1624: goto -> 1544
    //   1627: iconst_0
    //   1628: istore #21
    //   1630: aconst_null
    //   1631: astore #36
    //   1633: iload #4
    //   1635: iconst_1
    //   1636: iadd
    //   1637: istore #4
    //   1639: goto -> 1520
    //   1642: iload #21
    //   1644: ifeq -> 1717
    //   1647: aload #24
    //   1649: getfield block : Lmindustry/world/Block;
    //   1652: astore #5
    //   1654: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   1657: astore #45
    //   1659: aload #45
    //   1661: invokevirtual team : ()Lmindustry/game/Team;
    //   1664: astore #45
    //   1666: aload #24
    //   1668: getfield x : I
    //   1671: istore #27
    //   1673: aload #24
    //   1675: getfield y : I
    //   1678: istore #29
    //   1680: aload #24
    //   1682: getfield rotation : I
    //   1685: istore #22
    //   1687: aload #5
    //   1689: aload #45
    //   1691: iload #27
    //   1693: iload #29
    //   1695: iload #22
    //   1697: invokestatic validPlace : (Lmindustry/world/Block;Lmindustry/game/Team;III)Z
    //   1700: istore #4
    //   1702: iload #4
    //   1704: ifeq -> 1717
    //   1707: aload_0
    //   1708: astore #5
    //   1710: iload #34
    //   1712: istore #15
    //   1714: goto -> 1319
    //   1717: iconst_0
    //   1718: istore #21
    //   1720: aconst_null
    //   1721: astore #36
    //   1723: goto -> 1729
    //   1726: aload #5
    //   1728: astore_0
    //   1729: iload #21
    //   1731: ifne -> 1753
    //   1734: aload #6
    //   1736: astore #52
    //   1738: iload #8
    //   1740: istore #34
    //   1742: iload #10
    //   1744: istore #38
    //   1746: iload #11
    //   1748: istore #41
    //   1750: goto -> 2391
    //   1753: iconst_m1
    //   1754: istore #4
    //   1756: iconst_0
    //   1757: istore #15
    //   1759: fconst_0
    //   1760: fstore #44
    //   1762: aconst_null
    //   1763: astore #45
    //   1765: iconst_1
    //   1766: istore #17
    //   1768: ldc_w 1.4E-45
    //   1771: fstore #18
    //   1773: iconst_4
    //   1774: newarray int
    //   1776: dup
    //   1777: dup2
    //   1778: iconst_0
    //   1779: iload #17
    //   1781: iastore
    //   1782: iconst_1
    //   1783: iconst_0
    //   1784: iastore
    //   1785: iconst_2
    //   1786: iload #4
    //   1788: iastore
    //   1789: dup
    //   1790: iconst_3
    //   1791: iconst_0
    //   1792: iastore
    //   1793: astore #23
    //   1795: iconst_4
    //   1796: newarray int
    //   1798: dup
    //   1799: dup2
    //   1800: iconst_0
    //   1801: iconst_0
    //   1802: iastore
    //   1803: iconst_1
    //   1804: iload #17
    //   1806: iastore
    //   1807: iconst_2
    //   1808: iconst_0
    //   1809: iastore
    //   1810: dup
    //   1811: iconst_3
    //   1812: iload #4
    //   1814: iastore
    //   1815: astore #36
    //   1817: aload #14
    //   1819: invokevirtual iterator : ()Ljava/util/Iterator;
    //   1822: astore #45
    //   1824: ldc_w 1315859240
    //   1827: istore #17
    //   1829: ldc_w 1.0E9
    //   1832: fstore #18
    //   1834: iconst_0
    //   1835: istore #22
    //   1837: aconst_null
    //   1838: astore #24
    //   1840: aload #45
    //   1842: invokeinterface hasNext : ()Z
    //   1847: istore #27
    //   1849: iload #27
    //   1851: ifeq -> 2143
    //   1854: aload #45
    //   1856: invokeinterface next : ()Ljava/lang/Object;
    //   1861: astore #28
    //   1863: aload #28
    //   1865: checkcast mindustry/entities/units/BuildPlan
    //   1868: astore #28
    //   1870: aload #28
    //   1872: getfield block : Lmindustry/world/Block;
    //   1875: astore #37
    //   1877: aload #37
    //   1879: instanceof mindustry/world/blocks/distribution/Conveyor
    //   1882: istore #29
    //   1884: iload #29
    //   1886: ifne -> 1908
    //   1889: aload #6
    //   1891: astore #52
    //   1893: iload #8
    //   1895: istore #34
    //   1897: iload #10
    //   1899: istore #38
    //   1901: iload #11
    //   1903: istore #41
    //   1905: goto -> 2124
    //   1908: aload #28
    //   1910: getfield x : I
    //   1913: istore #29
    //   1915: aload #28
    //   1917: getfield rotation : I
    //   1920: iconst_3
    //   1921: iand
    //   1922: istore #4
    //   1924: aload #23
    //   1926: iload #4
    //   1928: iaload
    //   1929: istore #4
    //   1931: iload #29
    //   1933: iload #4
    //   1935: iadd
    //   1936: istore #29
    //   1938: aload #28
    //   1940: getfield y : I
    //   1943: istore #4
    //   1945: aload #6
    //   1947: astore #52
    //   1949: aload #28
    //   1951: getfield rotation : I
    //   1954: iconst_3
    //   1955: iand
    //   1956: istore #47
    //   1958: aload #36
    //   1960: iload #47
    //   1962: iaload
    //   1963: istore #47
    //   1965: iload #4
    //   1967: iload #47
    //   1969: iadd
    //   1970: istore #4
    //   1972: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   1975: astore #6
    //   1977: aload #6
    //   1979: getfield tiles : Lmindustry/world/Tiles;
    //   1982: astore #6
    //   1984: aload #6
    //   1986: iload #29
    //   1988: iload #4
    //   1990: invokevirtual in : (II)Z
    //   1993: istore #47
    //   1995: iload #47
    //   1997: ifeq -> 1893
    //   2000: iload #4
    //   2002: iload #8
    //   2004: imul
    //   2005: iload #29
    //   2007: iadd
    //   2008: istore #47
    //   2010: iload #47
    //   2012: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   2015: astore #6
    //   2017: aload #26
    //   2019: aload #6
    //   2021: invokevirtual contains : (Ljava/lang/Object;)Z
    //   2024: istore #47
    //   2026: iload #47
    //   2028: ifne -> 1893
    //   2031: iload #29
    //   2033: i2f
    //   2034: fstore #48
    //   2036: iload #8
    //   2038: istore #34
    //   2040: iload #4
    //   2042: i2f
    //   2043: fstore #53
    //   2045: iload #10
    //   2047: istore #38
    //   2049: iload #11
    //   2051: i2f
    //   2052: fstore #54
    //   2054: iload #11
    //   2056: istore #41
    //   2058: iload #12
    //   2060: i2f
    //   2061: fstore #55
    //   2063: fload #48
    //   2065: fload #53
    //   2067: fload #54
    //   2069: fload #55
    //   2071: invokestatic dst : (FFFF)F
    //   2074: fstore #48
    //   2076: fload #48
    //   2078: fload #18
    //   2080: fcmpg
    //   2081: istore #8
    //   2083: iload #8
    //   2085: ifge -> 2124
    //   2088: aload #28
    //   2090: getfield rotation : I
    //   2093: iconst_3
    //   2094: iand
    //   2095: istore #8
    //   2097: iconst_3
    //   2098: newarray int
    //   2100: dup
    //   2101: dup2
    //   2102: iconst_0
    //   2103: iload #29
    //   2105: iastore
    //   2106: iconst_1
    //   2107: iload #4
    //   2109: iastore
    //   2110: iconst_2
    //   2111: iload #8
    //   2113: iastore
    //   2114: astore #5
    //   2116: fload #48
    //   2118: fstore #18
    //   2120: aload #5
    //   2122: astore #24
    //   2124: aload #52
    //   2126: astore #6
    //   2128: iload #34
    //   2130: istore #8
    //   2132: iload #38
    //   2134: istore #10
    //   2136: iload #41
    //   2138: istore #11
    //   2140: goto -> 1840
    //   2143: aload #6
    //   2145: astore #52
    //   2147: iload #8
    //   2149: istore #34
    //   2151: iload #10
    //   2153: istore #38
    //   2155: iload #11
    //   2157: istore #41
    //   2159: aload #24
    //   2161: ifnonnull -> 2170
    //   2164: iconst_1
    //   2165: istore #35
    //   2167: goto -> 2391
    //   2170: aload #40
    //   2172: invokevirtual clone : ()Ljava/lang/Object;
    //   2175: astore #6
    //   2177: aload #6
    //   2179: checkcast [Z
    //   2182: astore #6
    //   2184: aload #14
    //   2186: invokevirtual iterator : ()Ljava/util/Iterator;
    //   2189: astore #5
    //   2191: aload #5
    //   2193: invokeinterface hasNext : ()Z
    //   2198: istore #8
    //   2200: iload #8
    //   2202: ifeq -> 2231
    //   2205: aload #5
    //   2207: invokeinterface next : ()Ljava/lang/Object;
    //   2212: astore #7
    //   2214: aload #7
    //   2216: checkcast mindustry/entities/units/BuildPlan
    //   2219: astore #7
    //   2221: aload #7
    //   2223: aload #6
    //   2225: invokestatic markPlan : (Lmindustry/entities/units/BuildPlan;[Z)V
    //   2228: goto -> 2191
    //   2231: iconst_0
    //   2232: istore #15
    //   2234: fconst_0
    //   2235: fstore #44
    //   2237: aconst_null
    //   2238: astore #45
    //   2240: aload #24
    //   2242: iconst_0
    //   2243: iaload
    //   2244: istore #4
    //   2246: iconst_1
    //   2247: istore #35
    //   2249: aload #24
    //   2251: iload #35
    //   2253: iaload
    //   2254: istore #8
    //   2256: aload #24
    //   2258: iload #31
    //   2260: iaload
    //   2261: istore #10
    //   2263: iload #4
    //   2265: iload #8
    //   2267: iload #10
    //   2269: aload #6
    //   2271: invokestatic planRoute : (III[Z)Larc/struct/Seq;
    //   2274: astore #5
    //   2276: aload #5
    //   2278: ifnull -> 2299
    //   2281: aload #5
    //   2283: invokevirtual isEmpty : ()Z
    //   2286: istore #8
    //   2288: iload #8
    //   2290: ifne -> 2299
    //   2293: iconst_1
    //   2294: istore #35
    //   2296: goto -> 2345
    //   2299: iconst_0
    //   2300: istore #15
    //   2302: fconst_0
    //   2303: fstore #44
    //   2305: aconst_null
    //   2306: astore #45
    //   2308: aload #24
    //   2310: iconst_0
    //   2311: iaload
    //   2312: istore #4
    //   2314: iconst_1
    //   2315: istore #35
    //   2317: aload #24
    //   2319: iload #35
    //   2321: iaload
    //   2322: istore #8
    //   2324: iconst_m1
    //   2325: istore #10
    //   2327: fconst_0
    //   2328: fconst_0
    //   2329: fdiv
    //   2330: fstore #54
    //   2332: iload #4
    //   2334: iload #8
    //   2336: iload #10
    //   2338: aload #6
    //   2340: invokestatic planRoute : (III[Z)Larc/struct/Seq;
    //   2343: astore #5
    //   2345: aload #5
    //   2347: ifnull -> 2391
    //   2350: aload #5
    //   2352: invokevirtual isEmpty : ()Z
    //   2355: istore #47
    //   2357: iload #47
    //   2359: ifne -> 2391
    //   2362: new arc/struct/Seq
    //   2365: astore #6
    //   2367: aload #6
    //   2369: invokespecial <init> : ()V
    //   2372: aload #6
    //   2374: aload #14
    //   2376: invokevirtual addAll : (Larc/struct/Seq;)Larc/struct/Seq;
    //   2379: pop
    //   2380: aload #6
    //   2382: aload #5
    //   2384: invokevirtual addAll : (Larc/struct/Seq;)Larc/struct/Seq;
    //   2387: pop
    //   2388: aload #6
    //   2390: areturn
    //   2391: aload_0
    //   2392: astore #5
    //   2394: iload #16
    //   2396: istore #13
    //   2398: aload #52
    //   2400: astore #6
    //   2402: iload #34
    //   2404: istore #8
    //   2406: iload #38
    //   2408: istore #10
    //   2410: iload #41
    //   2412: istore #11
    //   2414: aconst_null
    //   2415: astore_2
    //   2416: goto -> 1178
    //   2419: astore #6
    //   2421: goto -> 2426
    //   2424: astore #6
    //   2426: ldc_w '[Lunarians] fieldPlan'
    //   2429: astore #5
    //   2431: aload #5
    //   2433: aload #6
    //   2435: invokestatic err : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   2438: aconst_null
    //   2439: areturn
    // Exception table:
    //   from	to	target	type
    //   6	9	2424	finally
    //   16	20	2424	finally
    //   29	32	2424	finally
    //   34	39	2424	finally
    //   41	44	2424	finally
    //   46	51	2424	finally
    //   53	57	2424	finally
    //   59	63	2424	finally
    //   72	76	2424	finally
    //   99	104	2424	finally
    //   128	133	2424	finally
    //   153	158	2424	finally
    //   175	180	2424	finally
    //   189	192	2424	finally
    //   198	203	2424	finally
    //   210	215	2424	finally
    //   222	227	2424	finally
    //   234	239	2424	finally
    //   262	265	2424	finally
    //   283	287	2424	finally
    //   289	293	2424	finally
    //   295	300	2424	finally
    //   308	315	2424	finally
    //   322	329	2424	finally
    //   331	336	2424	finally
    //   338	343	2424	finally
    //   373	376	2424	finally
    //   378	383	2424	finally
    //   405	410	2424	finally
    //   414	416	2424	finally
    //   420	423	2424	finally
    //   434	436	2424	finally
    //   440	443	2424	finally
    //   447	450	2424	finally
    //   461	463	2424	finally
    //   526	531	2419	finally
    //   539	546	2419	finally
    //   553	560	2419	finally
    //   570	575	2419	finally
    //   581	586	2419	finally
    //   611	616	2419	finally
    //   625	630	2419	finally
    //   639	644	2419	finally
    //   646	651	2419	finally
    //   657	662	2419	finally
    //   666	668	2419	finally
    //   689	692	2419	finally
    //   703	706	2419	finally
    //   714	717	2419	finally
    //   762	765	2419	finally
    //   810	815	2419	finally
    //   817	822	2419	finally
    //   824	829	2419	finally
    //   831	836	2419	finally
    //   956	961	2419	finally
    //   1010	1014	2419	finally
    //   1019	1022	2419	finally
    //   1026	1029	2419	finally
    //   1033	1036	2419	finally
    //   1040	1043	2419	finally
    //   1045	1051	2419	finally
    //   1101	1106	2419	finally
    //   1139	1142	2419	finally
    //   1144	1149	2419	finally
    //   1151	1156	2419	finally
    //   1156	1160	2419	finally
    //   1162	1167	2419	finally
    //   1178	1185	2419	finally
    //   1192	1199	2419	finally
    //   1201	1206	2419	finally
    //   1230	1234	2419	finally
    //   1244	1247	2419	finally
    //   1261	1266	2419	finally
    //   1277	1280	2419	finally
    //   1289	1292	2419	finally
    //   1299	1302	2419	finally
    //   1304	1309	2419	finally
    //   1309	1314	2419	finally
    //   1319	1326	2419	finally
    //   1333	1340	2419	finally
    //   1342	1347	2419	finally
    //   1349	1354	2419	finally
    //   1361	1366	2419	finally
    //   1366	1371	2419	finally
    //   1378	1383	2419	finally
    //   1383	1388	2419	finally
    //   1390	1395	2419	finally
    //   1400	1405	2419	finally
    //   1407	1412	2419	finally
    //   1418	1423	2419	finally
    //   1427	1429	2419	finally
    //   1436	1439	2419	finally
    //   1446	1449	2419	finally
    //   1462	1465	2419	finally
    //   1477	1499	2419	finally
    //   1503	1508	2419	finally
    //   1516	1518	2419	finally
    //   1521	1524	2419	finally
    //   1539	1542	2419	finally
    //   1545	1548	2419	finally
    //   1593	1596	2419	finally
    //   1603	1608	2419	finally
    //   1612	1618	2419	finally
    //   1647	1652	2419	finally
    //   1654	1657	2419	finally
    //   1659	1664	2419	finally
    //   1666	1671	2419	finally
    //   1673	1678	2419	finally
    //   1680	1685	2419	finally
    //   1695	1700	2419	finally
    //   1773	1793	2419	finally
    //   1795	1815	2419	finally
    //   1817	1822	2419	finally
    //   1840	1847	2419	finally
    //   1854	1861	2419	finally
    //   1863	1868	2419	finally
    //   1870	1875	2419	finally
    //   1908	1913	2419	finally
    //   1915	1920	2419	finally
    //   1926	1929	2419	finally
    //   1938	1943	2419	finally
    //   1949	1954	2419	finally
    //   1960	1963	2419	finally
    //   1972	1975	2419	finally
    //   1977	1982	2419	finally
    //   1988	1993	2419	finally
    //   2010	2015	2419	finally
    //   2019	2024	2419	finally
    //   2069	2074	2419	finally
    //   2088	2093	2419	finally
    //   2097	2114	2419	finally
    //   2170	2175	2419	finally
    //   2177	2182	2419	finally
    //   2184	2189	2419	finally
    //   2191	2198	2419	finally
    //   2205	2212	2419	finally
    //   2214	2219	2419	finally
    //   2223	2228	2419	finally
    //   2242	2244	2419	finally
    //   2251	2254	2419	finally
    //   2258	2261	2419	finally
    //   2269	2274	2419	finally
    //   2281	2286	2419	finally
    //   2310	2312	2419	finally
    //   2319	2322	2419	finally
    //   2338	2343	2419	finally
    //   2350	2355	2419	finally
    //   2362	2365	2419	finally
    //   2367	2372	2419	finally
    //   2374	2380	2419	finally
    //   2382	2388	2419	finally
  }
  
  private static Schematic find(String paramString) {
    String str = "\\|";
    String[] arrayOfString = paramString.split(str);
    int i = arrayOfString.length;
    int j = 0;
    while (j < i) {
      String str1 = arrayOfString[j];
      Iterator<Schematic> iterator = Vars.schematics.all().iterator();
      while (true) {
        boolean bool = iterator.hasNext();
        if (bool) {
          Schematic schematic = iterator.next();
          String str2 = schematic.name();
          boolean bool1 = str1.equals(str2);
          if (bool1)
            return schematic; 
          continue;
        } 
        j++;
      } 
    } 
    return null;
  }
  
  public static void finishEconomy() {
    unitsOnly = false;
    unitsFinished = false;
    phase = AutoDock$Phase.NEXT;
    nextCheck = Time.time + 60.0F;
  }
  
  private static void finishStep() {
    boolean bool = energyStep;
    if (!bool) {
      int i = idx;
      if (i >= 0) {
        boolean[] arrayOfBoolean = done;
        int j = arrayOfBoolean.length;
        if (i < j) {
          j = 1;
          arrayOfBoolean[i] = j;
        } 
      } 
    } 
    energyStep = false;
    Seq seq = new Seq();
    this();
    current = seq;
    releaseCamera();
    phase = AutoDock$Phase.NEXT;
    nextCheck = Time.time + 60.0F;
  }
  
  private static int freePerimeter(CoreBlock.CoreBuild paramCoreBuild) {
    short s1 = paramCoreBuild.tile.x;
    Tile tile = paramCoreBuild.tile;
    short s2 = tile.y;
    int j = paramCoreBuild.block.size;
    int[] arrayOfInt = rect(s1, s2, j);
    s1 = 0;
    int i = arrayOfInt[0];
    byte b = 1;
    i -= b;
    boolean bool = false;
    while (true) {
      byte b1 = 2;
      int k = arrayOfInt[b1] + b;
      if (i <= k) {
        k = arrayOfInt[b] - b;
        while (true) {
          int m = arrayOfInt[3];
          int n = m + 1;
          i++;
        } 
        break;
      } 
      return bool;
    } 
  }
  
  private static int[] freeSpot(Block paramBlock, int paramInt1, int paramInt2, int paramInt3) {
    int i = 0;
    while (i <= paramInt3) {
      int j = paramInt1 - i;
      while (true) {
        int k = paramInt1 + i;
        if (j <= k) {
          k = paramInt2 - i;
          while (true) {
            int m = paramInt2 + i;
            if (k <= m) {
              Team team = Vars.player.team();
              boolean bool = Build.validPlace(paramBlock, team, j, k, 0);
              if (!bool) {
                k++;
                continue;
              } 
              return new int[] { j, k };
            } 
            j++;
          } 
          break;
        } 
        i++;
      } 
    } 
    return null;
  }
  
  public static void init() {
    AutoDock$3 autoDock$3 = new AutoDock$3();
    this();
    Events.on(EventType.WorldLoadEvent.class, autoDock$3);
    EventType.Trigger trigger = EventType.Trigger.update;
    AutoDock$4 autoDock$4 = new AutoDock$4();
    this();
    Events.run(trigger, autoDock$4);
  }
  
  private static boolean isBuilt(BuildPlan paramBuildPlan) {
    World world = Vars.world;
    int i = paramBuildPlan.x;
    int j = paramBuildPlan.y;
    Tile tile = world.tile(i, j);
    if (tile != null) {
      Building building = tile.build;
      if (building != null) {
        Block block2 = tile.block();
        Block block1 = paramBuildPlan.block;
        if (block2 == block1) {
          Team team1 = tile.team();
          Team team2 = Vars.player.team();
          if (team1 == team2)
            return true; 
        } 
      } 
    } 
    return false;
  }
  
  public static boolean isUnitsMode() {
    return unitsOnly;
  }
  
  private static String missing(CoreBlock.CoreBuild paramCoreBuild, Seq paramSeq, String paramString) {
    Rules rules = Vars.state.rules;
    boolean bool = rules.infiniteResources;
    if (bool)
      return null; 
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    this();
    Iterator<Map.Entry> iterator = paramSeq.iterator();
    while (true) {
      boolean bool1 = iterator.hasNext();
      int i = 0;
      Object object = null;
      if (bool1) {
        ItemStack[] arrayOfItemStack = ((BuildPlan)iterator.next()).block.requirements;
        int j = arrayOfItemStack.length;
        while (i < j) {
          ItemStack itemStack = arrayOfItemStack[i];
          float f1 = itemStack.amount;
          float f2 = Vars.state.rules.buildCostMultiplier;
          f1 *= f2;
          int k = Mathf.ceil(f1);
          Item item = itemStack.item;
          Integer integer = Integer.valueOf(k);
          AutoDock$0 autoDock$0 = new AutoDock$0();
          this();
          hashMap.merge(item, integer, autoDock$0);
          i++;
        } 
        continue;
      } 
      iterator = hashMap.entrySet().iterator();
      while (true) {
        bool1 = iterator.hasNext();
        if (bool1) {
          Map.Entry entry = iterator.next();
          Integer integer = (Integer)entry.getValue();
          int j = integer.intValue();
          int k = paramCoreBuild.storageCapacity;
          if (j > k) {
            StringBuilder stringBuilder1 = new StringBuilder();
            this();
            paramString = ((Item)entry.getKey()).localizedName;
            stringBuilder1.append(paramString);
            stringBuilder1.append(" (precisa ");
            paramString = (String)entry.getValue();
            stringBuilder1.append(paramString);
            stringBuilder1.append(" > capacidade ");
            int m = paramCoreBuild.storageCapacity;
            stringBuilder1.append(m);
            stringBuilder1.append(" do nucleo)");
            return stringBuilder1.toString();
          } 
          continue;
        } 
        HashMap<Object, Object> hashMap1 = new HashMap<Object, Object>();
        this();
        if (paramString != null) {
          bool1 = paramString.isEmpty();
          if (!bool1) {
            String str = ";";
            String[] arrayOfString = paramString.split(str);
            int j = arrayOfString.length;
            int k = 0;
            Object object1 = null;
            while (k < j) {
              String[] arrayOfString1 = arrayOfString[k].split(":");
              ContentLoader contentLoader = Vars.content;
              String str1 = arrayOfString1[0];
              Item item = contentLoader.item(str1);
              if (item != null) {
                boolean bool2 = true;
                float f = Float.MIN_VALUE;
                int m = Integer.parseInt(arrayOfString1[bool2]);
                Integer integer = Integer.valueOf(m);
                hashMap1.put(item, integer);
              } 
              k++;
            } 
          } 
        } 
        StringBuilder stringBuilder = new StringBuilder();
        this();
        Iterator<Map.Entry> iterator1 = hashMap.entrySet().iterator();
        while (true) {
          boolean bool2 = iterator1.hasNext();
          byte b = 32;
          if (bool2) {
            int k;
            Map.Entry entry = iterator1.next();
            ItemModule itemModule = paramCoreBuild.items;
            Item item = (Item)entry.getKey();
            int j = itemModule.get(item);
            item = (Item)entry.getKey();
            Integer integer1 = (Integer)hashMap1.get(item);
            Integer integer2 = (Integer)entry.getValue();
            int m = integer2.intValue();
            if (integer1 == null) {
              k = 0;
              float f = 0.0F;
              integer1 = null;
            } else {
              k = integer1.intValue();
            } 
            m += k;
            if (j < m) {
              String str = ((Item)entry.getKey()).localizedName;
              stringBuilder.append(str);
              stringBuilder.append(b);
              m -= j;
              stringBuilder.append(m);
              str = "  ";
              stringBuilder.append(str);
            } 
            continue;
          } 
          Iterator<Map.Entry> iterator2 = hashMap1.entrySet().iterator();
          while (true) {
            bool1 = iterator2.hasNext();
            if (bool1) {
              Map.Entry entry = iterator2.next();
              object = entry.getKey();
              boolean bool3 = hashMap.containsKey(object);
              if (!bool3) {
                object = paramCoreBuild.items;
                Item item = (Item)entry.getKey();
                int k = object.get(item);
                Integer integer = (Integer)entry.getValue();
                int m = integer.intValue();
                if (k < m) {
                  String str2 = ((Item)entry.getKey()).localizedName;
                  stringBuilder.append(str2);
                  stringBuilder.append(b);
                  int n = ((Integer)entry.getValue()).intValue() - k;
                  stringBuilder.append(n);
                  String str1 = " (alimentacao)  ";
                  stringBuilder.append(str1);
                } 
              } 
              continue;
            } 
            int j = stringBuilder.length();
            return (j == 0) ? null : stringBuilder.toString().trim();
          } 
          break;
        } 
        break;
      } 
      break;
    } 
  }
  
  private static Seq nodeChain(Building paramBuilding1, Building paramBuilding2) {
    Seq seq = new Seq();
    this();
    int i = paramBuilding2.tileX();
    int j = paramBuilding1.tileX();
    i -= j;
    float f1 = i;
    int k = paramBuilding2.tileY();
    j = paramBuilding1.tileY();
    k -= j;
    float f2 = k;
    float f3 = Mathf.dst(f1, f2) / 5.0F;
    double d = Math.ceil(f3);
    j = (int)d;
    byte b = 1;
    j -= b;
    j = Math.max(b, j);
    int m = 5;
    if (j > m)
      return null; 
    for (m = 1; m <= j; m++) {
      float f4 = m;
      float f5 = (j + 1);
      f4 /= f5;
      f5 = paramBuilding1.tileX();
      float f6 = f1 * f4;
      f5 += f6;
      int n = Mathf.round(f5);
      Block block1 = Blocks.powerNode;
      float f7 = paramBuilding1.tileY();
      f4 *= f2;
      int i1 = Mathf.round(f7 + f4);
      int i2 = 3;
      f7 = 4.2E-45F;
      int[] arrayOfInt = freeSpot(block1, n, i1, i2);
      if (arrayOfInt == null)
        return null; 
      BuildPlan buildPlan = new BuildPlan();
      f6 = 0.0F;
      block1 = null;
      i2 = arrayOfInt[0];
      i1 = arrayOfInt[b];
      Block block2 = Blocks.powerNode;
      this(i2, i1, 0, block2);
      seq.add(buildPlan);
    } 
    return seq;
  }
  
  private static boolean overlapsAny(int[] paramArrayOfint, ArrayList paramArrayList) {
    Iterator<int[]> iterator = paramArrayList.iterator();
    while (true) {
      boolean bool = iterator.hasNext();
      int i = 0;
      if (bool) {
        int[] arrayOfInt = iterator.next();
        int j = 2;
        int k = paramArrayOfint[j];
        int m = arrayOfInt[0];
        if (k >= m) {
          i = paramArrayOfint[0];
          j = arrayOfInt[j];
          if (i <= j) {
            i = 3;
            j = paramArrayOfint[i];
            k = 1;
            m = arrayOfInt[k];
            if (j >= m) {
              j = paramArrayOfint[k];
              int n = arrayOfInt[i];
              if (j <= n)
                return k; 
            } 
          } 
        } 
        continue;
      } 
      return false;
    } 
  }
  
  private static int pick() {
    int i = 0;
    while (true) {
      String[][] arrayOfString = ORDER;
      int j = arrayOfString.length;
      if (i < j) {
        boolean[] arrayOfBoolean = done;
        boolean bool = arrayOfBoolean[i];
        if (!bool) {
          bool = unitsOnly;
          if (bool) {
            String[] arrayOfString1 = arrayOfString[i];
            int k = arrayOfString1.length;
            byte b = 3;
            if (k > b) {
              String str2 = "U";
              String str1 = arrayOfString1[b];
              boolean bool1 = str2.equals(str1);
              if (bool1)
                return i; 
            } 
          } else {
            return i;
          } 
        } 
        i++;
        continue;
      } 
      return -1;
    } 
  }
  
  private static float powerNeed(Seq paramSeq) {
    Iterator<BuildPlan> iterator = paramSeq.iterator();
    float f = 0.0F;
    while (true) {
      boolean bool = iterator.hasNext();
      if (bool) {
        BuildPlan buildPlan = iterator.next();
        ConsumePower consumePower = buildPlan.block.consPower;
        if (consumePower != null) {
          consumePower = buildPlan.block.consPower;
          boolean bool1 = consumePower.buffered;
          if (!bool1) {
            ConsumePower consumePower1 = buildPlan.block.consPower;
            float f1 = consumePower1.usage;
            f += f1;
          } 
        } 
        continue;
      } 
      return f;
    } 
  }
  
  private static int[] rect(int paramInt1, int paramInt2, int paramInt3) {
    int i = -((paramInt3 + -1) / 2);
    paramInt1 += i;
    paramInt2 += i;
    i = paramInt1 + paramInt3 + -1;
    paramInt3 = paramInt3 + paramInt2 + -1;
    return new int[] { paramInt1, paramInt2, i, paramInt3 };
  }
  
  private static void releaseCamera() {
    boolean bool = camHeld;
    if (bool) {
      Settings settings = Core.settings;
      boolean bool1 = prevDetach;
      Boolean bool2 = Boolean.valueOf(bool1);
      String str = "detach-camera";
      settings.put(str, bool2);
      bool = false;
      settings = null;
      camHeld = false;
    } 
  }
  
  public static void reset() {
    releaseCamera();
    done = new boolean[ORDER.length];
    unitsOnly = false;
    unitsFinished = false;
    phase = AutoDock$Phase.NEXT;
    idx = 0;
    current.clear();
    energyStep = false;
    energyFails = 0;
    energyBuilt = 0;
    stuckRetries = 0;
    nextCheck = 0.0F;
  }
  
  private static int ringCovered(Seq paramSeq, int paramInt1, int paramInt2, ArrayList paramArrayList) {
    Iterator<BuildPlan> iterator = paramSeq.iterator();
    int i = 0;
    label46: while (true) {
      boolean bool = iterator.hasNext();
      if (bool) {
        BuildPlan buildPlan = iterator.next();
        int k = buildPlan.x + paramInt1;
        int m = buildPlan.y + paramInt2;
        int j = buildPlan.block.size;
        int[] arrayOfInt = rect(k, m, j);
        k = arrayOfInt[0];
        while (true) {
          m = 2;
          int n = arrayOfInt[m];
          if (k <= n) {
            n = 1;
            int i1 = arrayOfInt[n];
            while (true) {
              byte b = 3;
              int i2 = arrayOfInt[b];
              if (i1 <= i2) {
                Iterator iterator1 = paramArrayList.iterator();
                while (true) {
                  while (true) {
                    boolean bool1 = iterator1.hasNext();
                    break;
                  } 
                  if (SYNTHETIC_LOCAL_VARIABLE_17 != SYNTHETIC_LOCAL_VARIABLE_19) {
                    int i3 = SYNTHETIC_LOCAL_VARIABLE_16 + -1;
                    if (k >= i3) {
                      i3 = SYNTHETIC_LOCAL_VARIABLE_15[m] + n;
                      if (k <= i3) {
                        int i4 = SYNTHETIC_LOCAL_VARIABLE_18 + -1;
                        if (i1 >= i4) {
                          int i5 = SYNTHETIC_LOCAL_VARIABLE_15[b] + n;
                          if (i1 <= i5) {
                            i++;
                            break;
                          } 
                        } 
                      } 
                    } 
                  } 
                } 
                i1++;
                continue;
              } 
              k++;
            } 
            break;
          } 
          continue label46;
        } 
        break;
      } 
      return i;
    } 
  }
  
  private static void rotate(Seq paramSeq) {
    Iterator<BuildPlan> iterator = paramSeq.iterator();
    while (true) {
      boolean bool = iterator.hasNext();
      if (bool) {
        BuildPlan buildPlan = iterator.next();
        Block block1 = buildPlan.block;
        int i = block1.size % 2;
        if (i == 0) {
          i = -1090519040;
          f1 = -0.5F;
        } else {
          i = 0;
          f1 = 0.0F;
          block1 = null;
        } 
        AutoDock$2 autoDock$2 = new AutoDock$2();
        this(f1);
        buildPlan.pointConfig(autoDock$2);
        float f1 = buildPlan.x;
        float f2 = 8.0F;
        f1 *= f2;
        float f3 = buildPlan.block.offset;
        f1 += f3;
        f3 = buildPlan.y * f2;
        f2 = buildPlan.block.offset;
        f2 = -(f3 + f2);
        Block block3 = buildPlan.block;
        f3 = block3.offset;
        int j = World.toTile(f2 - f3);
        buildPlan.x = j;
        Block block2 = buildPlan.block;
        f2 = block2.offset;
        f1 -= f2;
        i = World.toTile(f1);
        buildPlan.y = i;
        block1 = buildPlan.block;
        j = buildPlan.rotation + 1;
        byte b = 4;
        f3 = 5.6E-45F;
        j = Mathf.mod(j, b);
        i = block1.planRotation(j);
        buildPlan.rotation = i;
        continue;
      } 
      break;
    } 
  }
  
  private static void startBuild(Seq paramSeq, boolean paramBoolean) {
    current = paramSeq;
    energyStep = paramBoolean;
    Iterator<BuildPlan> iterator = paramSeq.iterator();
    float f1 = 0.0F;
    float f2 = 0.0F;
    while (true) {
      boolean bool = iterator.hasNext();
      if (bool) {
        BuildPlan buildPlan = iterator.next();
        int j = buildPlan.x;
        float f5 = j;
        f1 += f5;
        int i = buildPlan.y;
        float f6 = i;
        f2 += f6;
        continue;
      } 
      float f3 = paramSeq.size;
      f1 /= f3;
      f3 = 8.0F;
      centerX = f1 * f3;
      float f4 = paramSeq.size;
      centerY = f2 / f4 * f3;
      phase = AutoDock$Phase.MOVE;
      phaseStart = Time.time;
      return;
    } 
  }
  
  public static void startUnits() {
    unitsOnly = true;
    unitsFinished = false;
    phase = AutoDock$Phase.NEXT;
    nextCheck = 0.0F;
  }
  
  private static void stepBuild(Unit paramUnit) {
    float f1 = centerX;
    float f2 = centerY;
    cameraGo(paramUnit, f1, f2);
    f1 = Time.time;
    f2 = nextCheck;
    int i = f1 cmp f2;
    if (i >= 0) {
      f1 = Time.time + 60.0F;
      nextCheck = f1;
      Iterator<BuildPlan> iterator = current.iterator();
      boolean bool = true;
      f2 = Float.MIN_VALUE;
      boolean bool1 = true;
      while (true) {
        boolean bool2 = iterator.hasNext();
        if (bool2) {
          BuildPlan buildPlan = iterator.next();
          boolean bool3 = isBuilt(buildPlan);
          if (!bool3) {
            Iterator<BuildPlan> iterator1 = paramUnit.plans().iterator();
            boolean bool4 = false;
            Team team = null;
            while (true) {
              boolean bool5 = iterator1.hasNext();
              if (bool5) {
                BuildPlan buildPlan1 = iterator1.next();
                int k = buildPlan1.x;
                int m = buildPlan.x;
                if (k == m) {
                  int n = buildPlan1.y;
                  k = buildPlan.y;
                  if (n == k)
                    bool4 = true; 
                } 
                continue;
              } 
              if (!bool4) {
                Block block = buildPlan.block;
                team = Vars.player.team();
                int k = buildPlan.x;
                int m = buildPlan.y;
                int n = buildPlan.rotation;
                bool1 = Build.validPlace(block, team, k, m, n);
                if (bool1) {
                  BuildPlan buildPlan1 = buildPlan.copy();
                  paramUnit.addBuild(buildPlan1, bool);
                } 
              } 
              break;
            } 
            bool1 = false;
            iterator1 = null;
          } 
          continue;
        } 
        if (bool1) {
          phase = AutoDock$Phase.GRID;
          phaseStart = Time.time;
          nextCheck = Time.time + 30.0F;
          return;
        } 
        float f = Time.time;
        f1 = phaseStart;
        f -= f1;
        i = 1175232512;
        f1 = 9000.0F;
        int j = f cmp f1;
        if (j > 0) {
          String str = "obra travada (recursos acabaram ou area bloqueada)";
          abort(str);
        } 
        break;
      } 
    } 
  }
  
  private static void stepGrid(CoreBlock.CoreBuild paramCoreBuild, Unit paramUnit) {
    float f1 = Time.time;
    float f2 = nextCheck;
    int i = f1 cmp f2;
    if (i < 0)
      return; 
    f1 = Time.time;
    int j = 1114636288;
    f2 = 60.0F;
    f1 += f2;
    nextCheck = f1;
    ObjectSet objectSet = new ObjectSet();
    this();
    Seq seq = new Seq();
    this();
    Iterator iterator = current.iterator();
    while (true) {
      Object object;
      while (true) {
        boolean bool = iterator.hasNext();
        boolean bool1 = false;
        float f = 0.0F;
        object = null;
        break;
      } 
      if (object != null) {
        PowerModule powerModule = ((Building)object).power;
        if (powerModule != null) {
          PowerGraph powerGraph = ((Building)object).power.graph;
          objectSet.add(powerGraph);
          seq.add(object);
        } 
      } 
    } 
  }
  
  private static void stepMove(Unit paramUnit) {
    UnitType unitType = paramUnit.type;
    float f1 = unitType.buildRange;
    int i = 1058642330;
    f1 *= 0.6F;
    float f2 = paramUnit.x;
    float f3 = paramUnit.y;
    float f4 = centerX;
    float f5 = centerY;
    f2 = Mathf.dst(f2, f3, f4, f5);
    int j = f2 cmp f1;
    if (j <= 0) {
      Iterator<BuildPlan> iterator = current.iterator();
      while (true) {
        boolean bool = iterator.hasNext();
        if (bool) {
          BuildPlan buildPlan = iterator.next();
          boolean bool1 = isBuilt(buildPlan);
          if (!bool1) {
            buildPlan = buildPlan.copy();
            bool1 = true;
            f3 = Float.MIN_VALUE;
            paramUnit.addBuild(buildPlan, bool1);
          } 
          continue;
        } 
        phase = AutoDock$Phase.BUILD;
        phaseStart = Time.time;
        nextCheck = Time.time + 60.0F;
        return;
      } 
    } 
    f1 = centerX;
    f2 = centerY;
    cameraGo(paramUnit, f1, f2);
    float f6 = Time.time;
    f1 = phaseStart;
    f6 -= f1;
    j = 1159069696;
    f1 = 2400.0F;
    int k = f6 cmp f1;
    if (k > 0) {
      String str = "nao consegui chegar na obra";
      abort(str);
    } 
  }
  
  private static void stepNext(CoreBlock.CoreBuild paramCoreBuild, Unit paramUnit) {
    // Byte code:
    //   0: aload_0
    //   1: astore_2
    //   2: aload_1
    //   3: astore_3
    //   4: getstatic arc/util/Time.time : F
    //   7: fstore #4
    //   9: getstatic lunarians/AutoDock.nextCheck : F
    //   12: fstore #5
    //   14: fload #4
    //   16: fload #5
    //   18: fcmpg
    //   19: istore #6
    //   21: iload #6
    //   23: iflt -> 1353
    //   26: getstatic arc/util/Time.time : F
    //   29: fstore #4
    //   31: ldc_w 1119092736
    //   34: istore #7
    //   36: ldc_w 90.0
    //   39: fstore #5
    //   41: fload #4
    //   43: fload #5
    //   45: fadd
    //   46: fstore #4
    //   48: fload #4
    //   50: putstatic lunarians/AutoDock.nextCheck : F
    //   53: invokestatic unloadersAllowed : ()Z
    //   56: istore #6
    //   58: iload #6
    //   60: ifne -> 70
    //   63: ldc_w 'Auto Dock: este servidor desativou unloaders no nucleo (allowCoreUnloaders). Os schematics encostados nao funcionam aqui.'
    //   66: invokestatic toast : (Ljava/lang/String;)V
    //   69: return
    //   70: invokestatic pick : ()I
    //   73: istore #6
    //   75: iload #6
    //   77: putstatic lunarians/AutoDock.idx : I
    //   80: iconst_1
    //   81: istore #7
    //   83: ldc_w 1.4E-45
    //   86: fstore #5
    //   88: iload #6
    //   90: ifge -> 125
    //   93: getstatic lunarians/AutoDock.unitsOnly : Z
    //   96: istore #8
    //   98: iload #8
    //   100: ifeq -> 109
    //   103: iload #7
    //   105: putstatic lunarians/AutoDock.unitsFinished : Z
    //   108: return
    //   109: ldc_w 'Auto Dock: ordem completa.'
    //   112: invokestatic toast : (Ljava/lang/String;)V
    //   115: invokestatic releaseCamera : ()V
    //   118: getstatic lunarians/AutoDock$Phase.DONE : Llunarians/AutoDock$Phase;
    //   121: putstatic lunarians/AutoDock.phase : Llunarians/AutoDock$Phase;
    //   124: return
    //   125: getstatic lunarians/AutoDock.ORDER : [[Ljava/lang/String;
    //   128: iload #6
    //   130: aaload
    //   131: astore #9
    //   133: aload #9
    //   135: iload #7
    //   137: aaload
    //   138: invokestatic find : (Ljava/lang/String;)Lmindustry/game/Schematic;
    //   141: astore #10
    //   143: aload #10
    //   145: ifnonnull -> 202
    //   148: new java/lang/StringBuilder
    //   151: astore_2
    //   152: aload_2
    //   153: ldc_w 'Auto Dock: schematic nao encontrado ('
    //   156: invokespecial <init> : (Ljava/lang/String;)V
    //   159: aload #9
    //   161: iload #7
    //   163: aaload
    //   164: astore_3
    //   165: aload_2
    //   166: aload_3
    //   167: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   170: pop
    //   171: aload_2
    //   172: ldc_w '), pulando.'
    //   175: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   178: pop
    //   179: aload_2
    //   180: invokevirtual toString : ()Ljava/lang/String;
    //   183: invokestatic toast : (Ljava/lang/String;)V
    //   186: getstatic lunarians/AutoDock.done : [Z
    //   189: astore_2
    //   190: getstatic lunarians/AutoDock.idx : I
    //   193: istore #11
    //   195: aload_2
    //   196: iload #11
    //   198: iload #7
    //   200: bastore
    //   201: return
    //   202: getstatic mindustry/Vars.schematics : Lmindustry/game/Schematics;
    //   205: aload #10
    //   207: iconst_0
    //   208: iconst_0
    //   209: iconst_0
    //   210: invokevirtual toPlans : (Lmindustry/game/Schematic;IIZ)Larc/struct/Seq;
    //   213: astore #12
    //   215: aload #12
    //   217: getfield size : I
    //   220: istore #13
    //   222: aload #10
    //   224: getfield tiles : Larc/struct/Seq;
    //   227: astore #10
    //   229: aload #10
    //   231: getfield size : I
    //   234: istore #14
    //   236: iload #13
    //   238: iload #14
    //   240: if_icmpge -> 296
    //   243: new java/lang/StringBuilder
    //   246: astore_2
    //   247: aload_2
    //   248: ldc 'Auto Dock: '
    //   250: invokespecial <init> : (Ljava/lang/String;)V
    //   253: aload #9
    //   255: iload #7
    //   257: aaload
    //   258: astore_3
    //   259: aload_2
    //   260: aload_3
    //   261: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   264: pop
    //   265: aload_2
    //   266: ldc_w ' tem blocos bloqueados, pulando.'
    //   269: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   272: pop
    //   273: aload_2
    //   274: invokevirtual toString : ()Ljava/lang/String;
    //   277: invokestatic toast : (Ljava/lang/String;)V
    //   280: getstatic lunarians/AutoDock.done : [Z
    //   283: astore_2
    //   284: getstatic lunarians/AutoDock.idx : I
    //   287: istore #11
    //   289: aload_2
    //   290: iload #11
    //   292: iload #7
    //   294: bastore
    //   295: return
    //   296: aload #9
    //   298: arraylength
    //   299: istore #14
    //   301: iconst_4
    //   302: istore #13
    //   304: ldc_w 5.6E-45
    //   307: fstore #15
    //   309: iconst_3
    //   310: istore #16
    //   312: iload #14
    //   314: iload #13
    //   316: if_icmple -> 350
    //   319: ldc 'F'
    //   321: astore #10
    //   323: aload #9
    //   325: iload #16
    //   327: aaload
    //   328: astore #17
    //   330: aload #10
    //   332: aload #17
    //   334: invokevirtual equals : (Ljava/lang/Object;)Z
    //   337: istore #14
    //   339: iload #14
    //   341: ifeq -> 350
    //   344: iconst_1
    //   345: istore #14
    //   347: goto -> 356
    //   350: iconst_0
    //   351: istore #14
    //   353: aconst_null
    //   354: astore #10
    //   356: iload #14
    //   358: ifne -> 390
    //   361: aload #12
    //   363: aload_2
    //   364: invokestatic alreadyBuilt : (Larc/struct/Seq;Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;)Z
    //   367: istore #18
    //   369: iload #18
    //   371: ifeq -> 390
    //   374: getstatic lunarians/AutoDock.done : [Z
    //   377: astore_2
    //   378: getstatic lunarians/AutoDock.idx : I
    //   381: istore #11
    //   383: aload_2
    //   384: iload #11
    //   386: iload #7
    //   388: bastore
    //   389: return
    //   390: iconst_2
    //   391: istore #18
    //   393: aload #9
    //   395: iload #18
    //   397: aaload
    //   398: astore #19
    //   400: aload_2
    //   401: aload #12
    //   403: aload #19
    //   405: invokestatic missing : (Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;Larc/struct/Seq;Ljava/lang/String;)Ljava/lang/String;
    //   408: astore #19
    //   410: ldc_w '] '
    //   413: astore #20
    //   415: ldc_w 'Auto Dock ['
    //   418: astore #21
    //   420: aload #19
    //   422: ifnull -> 488
    //   425: new java/lang/StringBuilder
    //   428: astore_2
    //   429: aload_2
    //   430: aload #21
    //   432: invokespecial <init> : (Ljava/lang/String;)V
    //   435: aload #9
    //   437: iconst_0
    //   438: aaload
    //   439: astore_3
    //   440: aload_2
    //   441: aload_3
    //   442: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   445: pop
    //   446: aload_2
    //   447: aload #20
    //   449: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   452: pop
    //   453: aload #9
    //   455: iload #7
    //   457: aaload
    //   458: astore_3
    //   459: aload_2
    //   460: aload_3
    //   461: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   464: pop
    //   465: aload_2
    //   466: ldc_w ': falta '
    //   469: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   472: pop
    //   473: aload_2
    //   474: aload #19
    //   476: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   479: pop
    //   480: aload_2
    //   481: invokevirtual toString : ()Ljava/lang/String;
    //   484: invokestatic toast : (Ljava/lang/String;)V
    //   487: return
    //   488: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   491: getfield rules : Lmindustry/game/Rules;
    //   494: astore #19
    //   496: aload #19
    //   498: getfield infiniteResources : Z
    //   501: istore #22
    //   503: iload #22
    //   505: ifne -> 871
    //   508: aload #12
    //   510: invokevirtual iterator : ()Ljava/util/Iterator;
    //   513: astore #19
    //   515: iconst_0
    //   516: istore #23
    //   518: aload #19
    //   520: invokeinterface hasNext : ()Z
    //   525: istore #24
    //   527: iload #24
    //   529: ifeq -> 568
    //   532: aload #19
    //   534: invokeinterface next : ()Ljava/lang/Object;
    //   539: checkcast mindustry/entities/units/BuildPlan
    //   542: getfield block : Lmindustry/world/Block;
    //   545: astore #25
    //   547: getstatic mindustry/content/Blocks.unloader : Lmindustry/world/Block;
    //   550: astore #26
    //   552: aload #25
    //   554: aload #26
    //   556: if_acmpne -> 518
    //   559: iload #23
    //   561: iconst_1
    //   562: iadd
    //   563: istore #23
    //   565: goto -> 518
    //   568: aload #9
    //   570: arraylength
    //   571: istore #22
    //   573: iload #22
    //   575: iload #16
    //   577: if_icmple -> 616
    //   580: ldc 'U'
    //   582: astore #19
    //   584: aload #9
    //   586: iload #16
    //   588: aaload
    //   589: astore #25
    //   591: aload #19
    //   593: aload #25
    //   595: invokevirtual equals : (Ljava/lang/Object;)Z
    //   598: istore #22
    //   600: iload #22
    //   602: ifeq -> 616
    //   605: iconst_1
    //   606: istore #22
    //   608: ldc_w 1.4E-45
    //   611: fstore #27
    //   613: goto -> 625
    //   616: iconst_0
    //   617: istore #22
    //   619: fconst_0
    //   620: fstore #27
    //   622: aconst_null
    //   623: astore #19
    //   625: iload #23
    //   627: ifgt -> 635
    //   630: iload #22
    //   632: ifeq -> 871
    //   635: iload #23
    //   637: bipush #30
    //   639: imul
    //   640: istore #24
    //   642: iload #22
    //   644: ifeq -> 655
    //   647: sipush #150
    //   650: istore #28
    //   652: goto -> 659
    //   655: bipush #30
    //   657: istore #28
    //   659: iload #24
    //   661: iload #28
    //   663: iadd
    //   664: istore #24
    //   666: iload #23
    //   668: bipush #25
    //   670: imul
    //   671: istore #23
    //   673: iload #22
    //   675: ifeq -> 690
    //   678: bipush #100
    //   680: istore #22
    //   682: ldc_w 1.4E-43
    //   685: fstore #27
    //   687: goto -> 699
    //   690: bipush #15
    //   692: istore #22
    //   694: ldc_w 2.1E-44
    //   697: fstore #27
    //   699: iload #23
    //   701: iload #22
    //   703: iadd
    //   704: istore #23
    //   706: aload_2
    //   707: getfield items : Lmindustry/world/modules/ItemModule;
    //   710: astore #19
    //   712: getstatic mindustry/content/Items.silicon : Lmindustry/type/Item;
    //   715: astore #26
    //   717: aload #19
    //   719: aload #26
    //   721: invokevirtual get : (Lmindustry/type/Item;)I
    //   724: istore #22
    //   726: aload_2
    //   727: getfield items : Lmindustry/world/modules/ItemModule;
    //   730: astore #26
    //   732: iconst_4
    //   733: istore #29
    //   735: getstatic mindustry/content/Items.titanium : Lmindustry/type/Item;
    //   738: astore #30
    //   740: aload #26
    //   742: aload #30
    //   744: invokevirtual get : (Lmindustry/type/Item;)I
    //   747: istore #13
    //   749: iload #22
    //   751: iload #24
    //   753: if_icmplt -> 763
    //   756: iload #13
    //   758: iload #23
    //   760: if_icmpge -> 874
    //   763: new java/lang/StringBuilder
    //   766: astore_2
    //   767: aload_2
    //   768: aload #21
    //   770: invokespecial <init> : (Ljava/lang/String;)V
    //   773: aload #9
    //   775: iconst_0
    //   776: aaload
    //   777: astore_3
    //   778: aload_2
    //   779: aload_3
    //   780: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   783: pop
    //   784: aload_2
    //   785: aload #20
    //   787: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   790: pop
    //   791: aload #9
    //   793: iload #7
    //   795: aaload
    //   796: astore_3
    //   797: aload_2
    //   798: aload_3
    //   799: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   802: pop
    //   803: aload_2
    //   804: ldc_w ': esperando silicio '
    //   807: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   810: pop
    //   811: aload_2
    //   812: iload #22
    //   814: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   817: pop
    //   818: ldc_w '/'
    //   821: astore_3
    //   822: aload_2
    //   823: aload_3
    //   824: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   827: pop
    //   828: aload_2
    //   829: iload #24
    //   831: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   834: pop
    //   835: aload_2
    //   836: ldc_w ' e titanio '
    //   839: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   842: pop
    //   843: aload_2
    //   844: iload #13
    //   846: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   849: pop
    //   850: aload_2
    //   851: aload_3
    //   852: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   855: pop
    //   856: aload_2
    //   857: iload #23
    //   859: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   862: pop
    //   863: aload_2
    //   864: invokevirtual toString : ()Ljava/lang/String;
    //   867: invokestatic toast : (Ljava/lang/String;)V
    //   870: return
    //   871: iconst_4
    //   872: istore #29
    //   874: aload #12
    //   876: invokestatic powerNeed : (Larc/struct/Seq;)F
    //   879: ldc_w 60.0
    //   882: fmul
    //   883: fstore #15
    //   885: fconst_0
    //   886: fstore #27
    //   888: aconst_null
    //   889: astore #19
    //   891: fload #15
    //   893: fconst_0
    //   894: fcmpl
    //   895: istore #22
    //   897: iload #22
    //   899: ifle -> 943
    //   902: invokestatic supplyLeft : ()F
    //   905: fstore #27
    //   907: ldc_w 1065772646
    //   910: istore #31
    //   912: ldc_w 1.05
    //   915: fload #15
    //   917: fmul
    //   918: fstore #32
    //   920: fload #27
    //   922: fload #32
    //   924: fcmpg
    //   925: istore #22
    //   927: iload #22
    //   929: ifge -> 943
    //   932: iconst_1
    //   933: istore #22
    //   935: ldc_w 1.4E-45
    //   938: fstore #27
    //   940: goto -> 952
    //   943: iconst_0
    //   944: istore #22
    //   946: fconst_0
    //   947: fstore #27
    //   949: aconst_null
    //   950: astore #19
    //   952: iload #22
    //   954: ifne -> 964
    //   957: iconst_0
    //   958: putstatic lunarians/AutoDock.energyFails : I
    //   961: goto -> 995
    //   964: getstatic lunarians/AutoDock.energyFails : I
    //   967: istore #31
    //   969: iload #31
    //   971: iload #18
    //   973: if_icmplt -> 995
    //   976: ldc_w 'Auto Energy: geradores nao produzem (sem carvao no nucleo?). Seguindo sem energia extra.'
    //   979: astore #17
    //   981: aload #17
    //   983: invokestatic toast : (Ljava/lang/String;)V
    //   986: iconst_0
    //   987: istore #22
    //   989: fconst_0
    //   990: fstore #27
    //   992: aconst_null
    //   993: astore #19
    //   995: iload #22
    //   997: ifeq -> 1031
    //   1000: getstatic lunarians/AutoDock.energyBuilt : I
    //   1003: istore #18
    //   1005: iload #18
    //   1007: iload #16
    //   1009: if_icmplt -> 1031
    //   1012: ldc_w 'Auto Energy: limite de 3 grupos de geradores atingido. Seguindo sem energia extra.'
    //   1015: astore #33
    //   1017: aload #33
    //   1019: invokestatic toast : (Ljava/lang/String;)V
    //   1022: iconst_0
    //   1023: istore #22
    //   1025: fconst_0
    //   1026: fstore #27
    //   1028: aconst_null
    //   1029: astore #19
    //   1031: iload #22
    //   1033: ifeq -> 1158
    //   1036: aload_2
    //   1037: invokestatic energyCluster : (Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;)Larc/struct/Seq;
    //   1040: astore_3
    //   1041: aload_3
    //   1042: ifnonnull -> 1052
    //   1045: ldc_w 'Auto Energy: sem espaco para gerador junto ao nucleo.'
    //   1048: invokestatic toast : (Ljava/lang/String;)V
    //   1051: return
    //   1052: ldc 'coal:300'
    //   1054: astore #9
    //   1056: aload_2
    //   1057: aload_3
    //   1058: aload #9
    //   1060: invokestatic missing : (Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;Larc/struct/Seq;Ljava/lang/String;)Ljava/lang/String;
    //   1063: astore_2
    //   1064: aload_2
    //   1065: ifnull -> 1093
    //   1068: new java/lang/StringBuilder
    //   1071: astore_3
    //   1072: aload_3
    //   1073: ldc_w 'Auto Energy: falta '
    //   1076: invokespecial <init> : (Ljava/lang/String;)V
    //   1079: aload_3
    //   1080: aload_2
    //   1081: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1084: pop
    //   1085: aload_3
    //   1086: invokevirtual toString : ()Ljava/lang/String;
    //   1089: invokestatic toast : (Ljava/lang/String;)V
    //   1092: return
    //   1093: new java/lang/StringBuilder
    //   1096: astore_2
    //   1097: aload_2
    //   1098: ldc_w 'Auto Energy: construindo geradores ('
    //   1101: invokespecial <init> : (Ljava/lang/String;)V
    //   1104: fload #15
    //   1106: invokestatic round : (F)I
    //   1109: istore #6
    //   1111: aload_2
    //   1112: iload #6
    //   1114: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1117: pop
    //   1118: aload_2
    //   1119: ldc_w ' energia/s necessaria).'
    //   1122: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1125: pop
    //   1126: aload_2
    //   1127: invokevirtual toString : ()Ljava/lang/String;
    //   1130: invokestatic toast : (Ljava/lang/String;)V
    //   1133: getstatic lunarians/AutoDock.energyFails : I
    //   1136: iload #7
    //   1138: iadd
    //   1139: putstatic lunarians/AutoDock.energyFails : I
    //   1142: getstatic lunarians/AutoDock.energyBuilt : I
    //   1145: iload #7
    //   1147: iadd
    //   1148: putstatic lunarians/AutoDock.energyBuilt : I
    //   1151: aload_3
    //   1152: iload #7
    //   1154: invokestatic startBuild : (Larc/struct/Seq;Z)V
    //   1157: return
    //   1158: iload #14
    //   1160: ifeq -> 1183
    //   1163: aload #9
    //   1165: iload #29
    //   1167: aaload
    //   1168: astore #30
    //   1170: aload_2
    //   1171: aload #12
    //   1173: aload #30
    //   1175: aload_3
    //   1176: invokestatic fieldPlan : (Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;Larc/struct/Seq;Ljava/lang/String;Lmindustry/gen/Unit;)Larc/struct/Seq;
    //   1179: astore_2
    //   1180: goto -> 1191
    //   1183: aload_2
    //   1184: aload #12
    //   1186: aload_3
    //   1187: invokestatic bestDock : (Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;Larc/struct/Seq;Lmindustry/gen/Unit;)Larc/struct/Seq;
    //   1190: astore_2
    //   1191: aload_2
    //   1192: ifnonnull -> 1354
    //   1195: new java/lang/StringBuilder
    //   1198: astore_2
    //   1199: iload #14
    //   1201: ifeq -> 1240
    //   1204: aload_2
    //   1205: ldc_w 'Auto Dock: sem campo de '
    //   1208: invokespecial <init> : (Ljava/lang/String;)V
    //   1211: aload #9
    //   1213: iload #29
    //   1215: aaload
    //   1216: astore_3
    //   1217: aload_2
    //   1218: aload_3
    //   1219: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1222: pop
    //   1223: aload_2
    //   1224: ldc_w ' livre para '
    //   1227: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1230: pop
    //   1231: aload #9
    //   1233: iload #7
    //   1235: aaload
    //   1236: astore_3
    //   1237: goto -> 1281
    //   1240: aload_2
    //   1241: ldc_w 'Auto Dock: sem lugar encostado no nucleo para '
    //   1244: invokespecial <init> : (Ljava/lang/String;)V
    //   1247: aload #9
    //   1249: iload #7
    //   1251: aaload
    //   1252: astore_3
    //   1253: aload_2
    //   1254: aload_3
    //   1255: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1258: pop
    //   1259: aload_2
    //   1260: ldc_w ' ('
    //   1263: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1266: pop
    //   1267: invokestatic coreInfo : ()Ljava/lang/String;
    //   1270: astore_3
    //   1271: aload_2
    //   1272: aload_3
    //   1273: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1276: pop
    //   1277: ldc_w ')'
    //   1280: astore_3
    //   1281: aload_2
    //   1282: aload_3
    //   1283: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1286: pop
    //   1287: aload_2
    //   1288: invokevirtual toString : ()Ljava/lang/String;
    //   1291: astore_2
    //   1292: aload_2
    //   1293: invokestatic toast : (Ljava/lang/String;)V
    //   1296: getstatic arc/util/Time.time : F
    //   1299: ldc_w 600.0
    //   1302: fadd
    //   1303: fstore #34
    //   1305: fload #34
    //   1307: putstatic lunarians/AutoDock.nextCheck : F
    //   1310: getstatic lunarians/AutoDock.stuckRetries : I
    //   1313: iload #7
    //   1315: iadd
    //   1316: istore #8
    //   1318: iload #8
    //   1320: putstatic lunarians/AutoDock.stuckRetries : I
    //   1323: bipush #12
    //   1325: istore #11
    //   1327: iload #8
    //   1329: iload #11
    //   1331: if_icmplt -> 1353
    //   1334: getstatic lunarians/AutoDock.done : [Z
    //   1337: astore_2
    //   1338: getstatic lunarians/AutoDock.idx : I
    //   1341: istore #11
    //   1343: aload_2
    //   1344: iload #11
    //   1346: iload #7
    //   1348: bastore
    //   1349: iconst_0
    //   1350: putstatic lunarians/AutoDock.stuckRetries : I
    //   1353: return
    //   1354: iconst_0
    //   1355: putstatic lunarians/AutoDock.stuckRetries : I
    //   1358: new java/lang/StringBuilder
    //   1361: astore_3
    //   1362: aload_3
    //   1363: aload #21
    //   1365: invokespecial <init> : (Ljava/lang/String;)V
    //   1368: aload #9
    //   1370: iconst_0
    //   1371: aaload
    //   1372: astore #10
    //   1374: aload_3
    //   1375: aload #10
    //   1377: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1380: pop
    //   1381: aload_3
    //   1382: ldc_w ']: construindo '
    //   1385: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1388: pop
    //   1389: aload #9
    //   1391: iload #7
    //   1393: aaload
    //   1394: astore #9
    //   1396: aload_3
    //   1397: aload #9
    //   1399: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1402: pop
    //   1403: aload_3
    //   1404: invokevirtual toString : ()Ljava/lang/String;
    //   1407: invokestatic toast : (Ljava/lang/String;)V
    //   1410: aload_2
    //   1411: iconst_0
    //   1412: invokestatic startBuild : (Larc/struct/Seq;Z)V
    //   1415: return
  }
  
  public static void stopUnits() {
    unitsOnly = false;
    unitsFinished = false;
    releaseCamera();
    Seq seq = new Seq();
    this();
    current = seq;
    phase = AutoDock$Phase.NEXT;
    nextCheck = Time.time + 60.0F;
  }
  
  private static float supplyLeft() {
    ObjectSet objectSet = new ObjectSet();
    this();
    Iterator<Building> iterator = Groups.build.iterator();
    while (true) {
      boolean bool = iterator.hasNext();
      if (bool) {
        Building building = iterator.next();
        Team team1 = building.team;
        Team team2 = Vars.player.team();
        if (team1 == team2) {
          PowerModule powerModule = building.power;
          if (powerModule != null) {
            PowerGraph powerGraph = building.power.graph;
            if (powerGraph != null) {
              PowerGraph powerGraph1 = building.power.graph;
              objectSet.add(powerGraph1);
            } 
          } 
        } 
        continue;
      } 
      ObjectSet.ObjectSetIterator<PowerGraph> objectSetIterator = objectSet.iterator();
      bool = false;
      float f = 0.0F;
      Object object = null;
      while (true) {
        float f1;
        float f2;
        boolean bool1 = objectSetIterator.hasNext();
        if (bool1) {
          PowerGraph powerGraph = objectSetIterator.next();
          Seq seq = powerGraph.producers;
          int i = seq.size;
          if (i != 0) {
            Iterator<Building> iterator1 = powerGraph.producers.iterator();
            f1 = 0.0F;
            while (true) {
              boolean bool2 = iterator1.hasNext();
              if (bool2) {
                float f3;
                Building building = iterator1.next();
                Block block = building.block;
                boolean bool3 = block instanceof PowerGenerator;
                if (bool3) {
                  PowerGenerator powerGenerator = (PowerGenerator)building.block;
                  f3 = powerGenerator.powerProduction;
                } else {
                  bool2 = false;
                  f3 = 0.0F;
                  building = null;
                } 
                f1 += f3;
                continue;
              } 
              Iterator<Building> iterator2 = powerGraph.consumers.iterator();
              i = 0;
              f2 = 0.0F;
              iterator1 = null;
              while (true) {
                bool2 = iterator2.hasNext();
                if (bool2) {
                  Building building = iterator2.next();
                  ConsumePower consumePower = building.block.consPower;
                  if (consumePower != null) {
                    consumePower = building.block.consPower;
                    boolean bool3 = consumePower.buffered;
                    if (!bool3) {
                      ConsumePower consumePower1 = building.block.consPower;
                      float f3 = consumePower1.usage;
                      f2 += f3;
                    } 
                  } 
                  continue;
                } 
                f1 -= f2;
                f += f1;
              } 
            } 
          } else {
            continue;
          } 
        } else {
          return f * 60.0F;
        } 
        f1 -= f2;
        f += f1;
      } 
      break;
    } 
  }
  
  private static void toast(String paramString) {
    // Byte code:
    //   0: getstatic lunarians/AutoDock.lastToast : Ljava/lang/String;
    //   3: astore_1
    //   4: aload_0
    //   5: aload_1
    //   6: invokevirtual equals : (Ljava/lang/Object;)Z
    //   9: istore_2
    //   10: iload_2
    //   11: ifeq -> 42
    //   14: getstatic arc/util/Time.time : F
    //   17: fstore_3
    //   18: getstatic lunarians/AutoDock.lastToastAt : F
    //   21: fstore #4
    //   23: fload_3
    //   24: fload #4
    //   26: fsub
    //   27: fstore_3
    //   28: ldc_w 1800.0
    //   31: fstore #4
    //   33: fload_3
    //   34: fload #4
    //   36: fcmpg
    //   37: istore_2
    //   38: iload_2
    //   39: iflt -> 134
    //   42: getstatic arc/util/Time.time : F
    //   45: fstore_3
    //   46: getstatic lunarians/AutoDock.lastToastAt : F
    //   49: fstore #4
    //   51: fload_3
    //   52: fload #4
    //   54: fsub
    //   55: fstore_3
    //   56: ldc_w 240.0
    //   59: fstore #4
    //   61: fload_3
    //   62: fload #4
    //   64: fcmpg
    //   65: istore_2
    //   66: iload_2
    //   67: iflt -> 134
    //   70: aload_0
    //   71: putstatic lunarians/AutoDock.lastToast : Ljava/lang/String;
    //   74: getstatic arc/util/Time.time : F
    //   77: fstore_3
    //   78: fload_3
    //   79: putstatic lunarians/AutoDock.lastToastAt : F
    //   82: new java/lang/StringBuilder
    //   85: astore_1
    //   86: ldc_w '[Lunarians] '
    //   89: astore #5
    //   91: aload_1
    //   92: aload #5
    //   94: invokespecial <init> : (Ljava/lang/String;)V
    //   97: aload_1
    //   98: aload_0
    //   99: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   102: pop
    //   103: aload_1
    //   104: invokevirtual toString : ()Ljava/lang/String;
    //   107: invokestatic info : (Ljava/lang/Object;)V
    //   110: getstatic mindustry/Vars.ui : Lmindustry/core/UI;
    //   113: astore_1
    //   114: aload_1
    //   115: ifnull -> 134
    //   118: getstatic mindustry/Vars.ui : Lmindustry/core/UI;
    //   121: astore_1
    //   122: ldc_w 4.0
    //   125: fstore #4
    //   127: aload_1
    //   128: aload_0
    //   129: fload #4
    //   131: invokevirtual showInfoToast : (Ljava/lang/String;F)V
    //   134: return
  }
  
  private static boolean touches(BuildPlan paramBuildPlan, int paramInt1, int paramInt2, int[] paramArrayOfint) {
    // Byte code:
    //   0: aload_0
    //   1: getfield x : I
    //   4: iload_1
    //   5: iadd
    //   6: istore #4
    //   8: aload_0
    //   9: getfield y : I
    //   12: iload_2
    //   13: iadd
    //   14: istore_1
    //   15: aload_0
    //   16: getfield block : Lmindustry/world/Block;
    //   19: getfield size : I
    //   22: istore #5
    //   24: iload #4
    //   26: iload_1
    //   27: iload #5
    //   29: invokestatic rect : (III)[I
    //   32: astore_0
    //   33: iconst_2
    //   34: istore_1
    //   35: aload_0
    //   36: iload_1
    //   37: iaload
    //   38: istore_2
    //   39: iconst_0
    //   40: istore #4
    //   42: aload_3
    //   43: iconst_0
    //   44: iaload
    //   45: istore #6
    //   47: iconst_1
    //   48: istore #7
    //   50: iload_2
    //   51: iload #6
    //   53: if_icmplt -> 79
    //   56: aload_0
    //   57: iconst_0
    //   58: iaload
    //   59: istore #8
    //   61: aload_3
    //   62: iload_1
    //   63: iaload
    //   64: istore #9
    //   66: iload #8
    //   68: iload #9
    //   70: if_icmpgt -> 79
    //   73: iconst_1
    //   74: istore #8
    //   76: goto -> 82
    //   79: iconst_0
    //   80: istore #8
    //   82: iconst_3
    //   83: istore #9
    //   85: aload_0
    //   86: iload #9
    //   88: iaload
    //   89: istore #10
    //   91: aload_3
    //   92: iload #7
    //   94: iaload
    //   95: istore #11
    //   97: iload #10
    //   99: iload #11
    //   101: if_icmplt -> 129
    //   104: aload_0
    //   105: iload #7
    //   107: iaload
    //   108: istore #12
    //   110: aload_3
    //   111: iload #9
    //   113: iaload
    //   114: istore #13
    //   116: iload #12
    //   118: iload #13
    //   120: if_icmpgt -> 129
    //   123: iconst_1
    //   124: istore #12
    //   126: goto -> 132
    //   129: iconst_0
    //   130: istore #12
    //   132: iload #8
    //   134: ifeq -> 173
    //   137: iload #10
    //   139: iload #7
    //   141: iadd
    //   142: istore #10
    //   144: iload #10
    //   146: iload #11
    //   148: if_icmpeq -> 207
    //   151: aload_0
    //   152: iload #7
    //   154: iaload
    //   155: iload #7
    //   157: isub
    //   158: istore #8
    //   160: aload_3
    //   161: iload #9
    //   163: iaload
    //   164: istore #9
    //   166: iload #8
    //   168: iload #9
    //   170: if_icmpeq -> 207
    //   173: iload #12
    //   175: ifeq -> 210
    //   178: iload_2
    //   179: iload #7
    //   181: iadd
    //   182: istore_2
    //   183: iload_2
    //   184: iload #6
    //   186: if_icmpeq -> 207
    //   189: aload_0
    //   190: iconst_0
    //   191: iaload
    //   192: iload #7
    //   194: isub
    //   195: istore #5
    //   197: aload_3
    //   198: iload_1
    //   199: iaload
    //   200: istore_1
    //   201: iload #5
    //   203: iload_1
    //   204: if_icmpne -> 210
    //   207: iload #7
    //   209: ireturn
    //   210: iconst_0
    //   211: ireturn
  }
  
  private static boolean touchesAny(BuildPlan paramBuildPlan, int paramInt1, int paramInt2, ArrayList paramArrayList) {
    Iterator<int[]> iterator = paramArrayList.iterator();
    while (true) {
      boolean bool = iterator.hasNext();
      if (bool) {
        int[] arrayOfInt = iterator.next();
        bool = touches(paramBuildPlan, paramInt1, paramInt2, arrayOfInt);
        if (bool)
          return true; 
        continue;
      } 
      return false;
    } 
  }
  
  public static boolean unitsDone() {
    return unitsFinished;
  }
  
  private static boolean unloadersAllowed() {
    try {
      GameState gameState1 = Vars.state;
      Rules rules1 = gameState1.rules;
      Class<?> clazz = rules1.getClass();
      String str = "allowCoreUnloaders";
      Field field = clazz.getField(str);
      GameState gameState2 = Vars.state;
      return field.getBoolean(rules2);
    } finally {
      null;
    } 
  }
  
  private static void update() {
    // Byte code:
    //   0: getstatic lunarians/LunariansSettings.dockAuto : Z
    //   3: istore_0
    //   4: iload_0
    //   5: ifne -> 12
    //   8: invokestatic releaseCamera : ()V
    //   11: return
    //   12: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   15: astore_1
    //   16: aload_1
    //   17: ifnull -> 232
    //   20: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   23: astore_1
    //   24: aload_1
    //   25: invokevirtual isGame : ()Z
    //   28: istore_0
    //   29: iload_0
    //   30: ifeq -> 232
    //   33: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   36: astore_1
    //   37: aload_1
    //   38: ifnull -> 232
    //   41: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   44: astore_1
    //   45: aload_1
    //   46: invokevirtual dead : ()Z
    //   49: istore_0
    //   50: iload_0
    //   51: ifne -> 232
    //   54: invokestatic isActive : ()Z
    //   57: istore_0
    //   58: iload_0
    //   59: ifne -> 70
    //   62: getstatic lunarians/AutoEconomy.active : Z
    //   65: istore_0
    //   66: iload_0
    //   67: ifeq -> 78
    //   70: getstatic lunarians/AutoDock.unitsOnly : Z
    //   73: istore_0
    //   74: iload_0
    //   75: ifeq -> 232
    //   78: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   81: invokevirtual closestCore : ()Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;
    //   84: astore_1
    //   85: aload_1
    //   86: ifnull -> 232
    //   89: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   92: invokevirtual unit : ()Lmindustry/gen/Unit;
    //   95: astore_2
    //   96: aload_2
    //   97: ifnull -> 232
    //   100: aload_2
    //   101: getfield type : Lmindustry/type/UnitType;
    //   104: astore_3
    //   105: aload_3
    //   106: getfield buildSpeed : F
    //   109: fstore #4
    //   111: iconst_0
    //   112: istore #5
    //   114: fload #4
    //   116: fconst_0
    //   117: fcmpg
    //   118: istore #6
    //   120: iload #6
    //   122: ifle -> 232
    //   125: getstatic lunarians/AutoDock.phase : Llunarians/AutoDock$Phase;
    //   128: astore_3
    //   129: aload_3
    //   130: invokevirtual ordinal : ()I
    //   133: istore #6
    //   135: iload #6
    //   137: ifeq -> 186
    //   140: iconst_1
    //   141: istore #5
    //   143: iload #6
    //   145: iload #5
    //   147: if_icmpeq -> 181
    //   150: iconst_2
    //   151: istore #5
    //   153: iload #6
    //   155: iload #5
    //   157: if_icmpeq -> 176
    //   160: iconst_3
    //   161: istore #5
    //   163: iload #6
    //   165: iload #5
    //   167: if_icmpne -> 232
    //   170: aload_1
    //   171: aload_2
    //   172: invokestatic stepGrid : (Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;Lmindustry/gen/Unit;)V
    //   175: return
    //   176: aload_2
    //   177: invokestatic stepBuild : (Lmindustry/gen/Unit;)V
    //   180: return
    //   181: aload_2
    //   182: invokestatic stepMove : (Lmindustry/gen/Unit;)V
    //   185: return
    //   186: aload_1
    //   187: aload_2
    //   188: invokestatic stepNext : (Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;Lmindustry/gen/Unit;)V
    //   191: return
    //   192: astore_1
    //   193: ldc_w '[Lunarians] AutoDock'
    //   196: astore_2
    //   197: aload_2
    //   198: aload_1
    //   199: invokestatic err : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   202: getstatic lunarians/AutoDock$Phase.NEXT : Llunarians/AutoDock$Phase;
    //   205: astore_1
    //   206: aload_1
    //   207: putstatic lunarians/AutoDock.phase : Llunarians/AutoDock$Phase;
    //   210: getstatic arc/util/Time.time : F
    //   213: fstore #7
    //   215: ldc_w 300.0
    //   218: fstore #8
    //   220: fload #7
    //   222: fload #8
    //   224: fadd
    //   225: fstore #7
    //   227: fload #7
    //   229: putstatic lunarians/AutoDock.nextCheck : F
    //   232: return
    // Exception table:
    //   from	to	target	type
    //   125	128	192	finally
    //   129	133	192	finally
    //   171	175	192	finally
    //   176	180	192	finally
    //   181	185	192	finally
    //   187	191	192	finally
  }
  
  static {
    String[][] arrayOfString = new String[16][];
    String str1 = "Field";
    String str2 = "";
    String str3 = "F";
    String[] arrayOfString1 = { str1, "GP!3|GP!4|GP!2", str2, str3, "coal" };
    arrayOfString[0] = arrayOfString1;
    arrayOfString1 = new String[] { str1, "DrM!2|DrM!1|DrM!3", str2, str3, "copper" };
    arrayOfString[1] = arrayOfString1;
    arrayOfString1 = new String[] { str1, "DrM!3|DrM!4|DrM!1", str2, str3, "lead" };
    arrayOfString[2] = arrayOfString1;
    str3 = "Early";
    arrayOfString1 = new String[] { str3, "SP_GP10|GP!9", "coal:100" };
    arrayOfString[3] = arrayOfString1;
    arrayOfString1 = new String[] { str3, "SP_SS23|SS24", "coal:100;sand:200" };
    arrayOfString[4] = arrayOfString1;
    arrayOfString1 = new String[] { str3, "K17|K!2", "lead:200;sand:100" };
    arrayOfString[5] = arrayOfString1;
    str3 = "Medium";
    arrayOfString1 = new String[] { str3, "PM!9", "coal:100;lead:100;sand:100" };
    arrayOfString[6] = arrayOfString1;
    arrayOfString1 = new String[] { str3, "MP17", "coal:300" };
    arrayOfString[7] = arrayOfString1;
    arrayOfString1 = new String[] { str3, "T2!4|T2!6", "silicon:300;graphite:100;lead:200;titanium:75", "U" };
    arrayOfString[8] = arrayOfString1;
    str3 = "Late";
    arrayOfString1 = new String[] { str3, "PDG!3", "pyratite:100;titanium:100;coal:100;lead:100;sand:100" };
    arrayOfString[9] = arrayOfString1;
    arrayOfString1 = new String[] { str3, "SP_PW16", "thorium:100;sand:300" };
    arrayOfString[10] = arrayOfString1;
    arrayOfString1 = new String[] { str3, "PC17DS", "titanium:300;sand:300" };
    arrayOfString[11] = arrayOfString1;
    arrayOfString1 = new String[] { str3, "SP_SC21", "coal:200;sand:300;lead:100" };
    arrayOfString[12] = arrayOfString1;
    arrayOfString1 = new String[] { str3, "T3!3", "silicon:800;titanium:500;metaglass:200;graphite:300" };
    arrayOfString[13] = arrayOfString1;
    str3 = "End";
    arrayOfString1 = new String[] { str3, "SP_SUS4", "copper:300;lead:300;titanium:300;silicon:300" };
    arrayOfString[14] = arrayOfString1;
    arrayOfString1 = new String[] { str3, "SP_T45", "silicon:1500;titanium:1500;metaglass:300;plastanium:300;graphite:300" };
    arrayOfString[15] = arrayOfString1;
    ORDER = arrayOfString;
  }
}
