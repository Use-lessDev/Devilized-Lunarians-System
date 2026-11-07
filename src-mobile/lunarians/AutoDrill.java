package lunarians;

import arc.math.Mathf;
import arc.struct.IntQueue;
import arc.struct.IntSeq;
import arc.struct.Queue;
import arc.struct.Seq;
import java.util.Arrays;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.core.UI;
import mindustry.core.World;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.Tiles;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.production.Drill;

public class AutoDrill {
  private static final int[] DX;
  
  private static final int[] DY;
  
  public static boolean dryRun;
  
  private static boolean errorLogged;
  
  private static boolean failedNoneFit;
  
  public static int isoOverride;
  
  public static int lastCount;
  
  public static final IntSeq lastDrillTiles;
  
  public static int lastHub;
  
  public static final Seq lastPlans;
  
  public static boolean limitOn;
  
  public static float limitR;
  
  public static float limitX;
  
  public static float limitY;
  
  public static boolean quiet;
  
  static {
    boolean bool = true;
    byte b = -1;
    DX = new int[] { bool, 0, b, 0 };
    DY = new int[] { 0, bool, 0, b };
    isoOverride = b;
    Seq seq = new Seq();
    this();
    lastPlans = seq;
    IntSeq intSeq = new IntSeq();
    this();
    lastDrillTiles = intSeq;
    lastHub = b;
  }
  
  private static boolean canPlaceDrill(int paramInt1, int paramInt2, int paramInt3, boolean[] paramArrayOfboolean, int paramInt4, int paramInt5) {
    if (paramInt1 >= 0 && paramInt2 >= 0) {
      int i = paramInt1 + paramInt3;
      if (i <= paramInt4) {
        i = paramInt2 + paramInt3;
        if (i <= paramInt5) {
          for (paramInt5 = 0; paramInt5 < paramInt3; paramInt5++) {
            for (i = 0; i < paramInt3; i++) {
              int j = paramInt2 + paramInt5;
              int k = j * paramInt4;
              int m = paramInt1 + i;
              k += m;
              boolean bool1 = paramArrayOfboolean[k];
              if (bool1)
                return false; 
              World world = Vars.world;
              Tile tile = world.tile(m, j);
              if (tile == null)
                return false; 
              bool1 = tile.solid();
              if (bool1) {
                Building building1 = tile.build;
                if (building1 == null)
                  return false; 
              } 
              Building building = tile.build;
              if (building != null) {
                Block block = tile.build.block;
                if (block != null) {
                  Block block1 = tile.build.block;
                  boolean bool2 = block1.solid;
                  if (bool2)
                    return false; 
                } 
              } 
            } 
          } 
          boolean bool = limitOn;
          if (bool) {
            int j = isoOverride;
            if (j < 0)
              j = AutoSetConfig.spacing(); 
            boolean bool1 = crowded(paramInt1, paramInt2, paramInt3, j);
            if (bool1)
              return false; 
          } 
          return true;
        } 
      } 
    } 
    return false;
  }
  
  public static int count() {
    return lastCount;
  }
  
  private static int countOre(int paramInt1, int paramInt2, int paramInt3, Item paramItem, boolean[] paramArrayOfboolean, int paramInt4) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #6
    //   3: aconst_null
    //   4: astore #7
    //   6: iconst_0
    //   7: istore #8
    //   9: iconst_0
    //   10: istore #9
    //   12: iconst_0
    //   13: istore #10
    //   15: iload #8
    //   17: iload_2
    //   18: if_icmpge -> 224
    //   21: iconst_0
    //   22: istore #11
    //   24: iload #11
    //   26: iload_2
    //   27: if_icmpge -> 215
    //   30: iload_1
    //   31: iload #8
    //   33: iadd
    //   34: istore #12
    //   36: iload #12
    //   38: iload #5
    //   40: imul
    //   41: istore #13
    //   43: iload_0
    //   44: iload #11
    //   46: iadd
    //   47: istore #14
    //   49: iload #13
    //   51: iload #14
    //   53: iadd
    //   54: istore #13
    //   56: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   59: astore #15
    //   61: aload #15
    //   63: iload #14
    //   65: iload #12
    //   67: invokevirtual tile : (II)Lmindustry/world/Tile;
    //   70: astore #16
    //   72: aload #16
    //   74: invokestatic oreAt : (Lmindustry/world/Tile;)Lmindustry/type/Item;
    //   77: astore #17
    //   79: aload #17
    //   81: ifnull -> 175
    //   84: aload #17
    //   86: aload_3
    //   87: if_acmpne -> 99
    //   90: iload #10
    //   92: iconst_1
    //   93: iadd
    //   94: istore #10
    //   96: goto -> 175
    //   99: getstatic lunarians/AutoDrill.limitOn : Z
    //   102: istore #18
    //   104: iload #18
    //   106: ifeq -> 175
    //   109: aload #7
    //   111: ifnonnull -> 124
    //   114: new java/util/HashMap
    //   117: astore #7
    //   119: aload #7
    //   121: invokespecial <init> : ()V
    //   124: aload #7
    //   126: aload #17
    //   128: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   131: checkcast java/lang/Integer
    //   134: astore #15
    //   136: iconst_1
    //   137: istore #19
    //   139: aload #15
    //   141: ifnull -> 158
    //   144: aload #15
    //   146: invokevirtual intValue : ()I
    //   149: istore #18
    //   151: iload #19
    //   153: iload #18
    //   155: iadd
    //   156: istore #19
    //   158: iload #19
    //   160: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   163: astore #15
    //   165: aload #7
    //   167: aload #17
    //   169: aload #15
    //   171: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   174: pop
    //   175: aload #4
    //   177: iload #13
    //   179: baload
    //   180: istore #13
    //   182: iload #13
    //   184: ifeq -> 206
    //   187: aload #16
    //   189: aload_3
    //   190: invokestatic sameOre : (Lmindustry/world/Tile;Lmindustry/type/Item;)Z
    //   193: istore #12
    //   195: iload #12
    //   197: ifeq -> 206
    //   200: iload #9
    //   202: iconst_1
    //   203: iadd
    //   204: istore #9
    //   206: iload #11
    //   208: iconst_1
    //   209: iadd
    //   210: istore #11
    //   212: goto -> 24
    //   215: iload #8
    //   217: iconst_1
    //   218: iadd
    //   219: istore #8
    //   221: goto -> 15
    //   224: getstatic lunarians/AutoDrill.limitOn : Z
    //   227: istore_0
    //   228: iload_0
    //   229: ifeq -> 386
    //   232: aload #7
    //   234: ifnull -> 386
    //   237: aload #7
    //   239: invokevirtual entrySet : ()Ljava/util/Set;
    //   242: invokeinterface iterator : ()Ljava/util/Iterator;
    //   247: astore #20
    //   249: aload #20
    //   251: invokeinterface hasNext : ()Z
    //   256: istore_1
    //   257: iload_1
    //   258: ifeq -> 386
    //   261: aload #20
    //   263: invokeinterface next : ()Ljava/lang/Object;
    //   268: checkcast java/util/Map$Entry
    //   271: astore #21
    //   273: aload #21
    //   275: invokeinterface getKey : ()Ljava/lang/Object;
    //   280: checkcast mindustry/type/Item
    //   283: astore #22
    //   285: aload #21
    //   287: invokeinterface getValue : ()Ljava/lang/Object;
    //   292: checkcast java/lang/Integer
    //   295: astore #21
    //   297: aload #21
    //   299: invokevirtual intValue : ()I
    //   302: istore_1
    //   303: aload #22
    //   305: getfield lowPriority : Z
    //   308: istore #23
    //   310: iload #23
    //   312: ifeq -> 326
    //   315: aload_3
    //   316: getfield lowPriority : Z
    //   319: istore #23
    //   321: iload #23
    //   323: ifeq -> 249
    //   326: aload #22
    //   328: getfield lowPriority : Z
    //   331: istore #23
    //   333: iconst_m1
    //   334: istore #6
    //   336: iload #23
    //   338: ifne -> 355
    //   341: aload_3
    //   342: getfield lowPriority : Z
    //   345: istore #23
    //   347: iload #23
    //   349: ifeq -> 355
    //   352: iload #6
    //   354: ireturn
    //   355: iload_1
    //   356: iload #10
    //   358: if_icmpgt -> 383
    //   361: iload_1
    //   362: iload #10
    //   364: if_icmpne -> 249
    //   367: aload #22
    //   369: getfield id : S
    //   372: istore_1
    //   373: aload_3
    //   374: getfield id : S
    //   377: istore_2
    //   378: iload_1
    //   379: iload_2
    //   380: if_icmple -> 249
    //   383: iload #6
    //   385: ireturn
    //   386: iload #9
    //   388: ireturn
  }
  
  static boolean crowded(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = paramInt2 - paramInt4;
    while (true) {
      int j = paramInt2 + paramInt3;
      byte b = 1;
      j = j - b + paramInt4;
      if (i <= j) {
        j = paramInt1 - paramInt4;
        while (true) {
          int k = paramInt1 + paramInt3 - b + paramInt4;
          if (j <= k) {
            Tiles tiles = Vars.world.tiles;
            boolean bool = tiles.in(j, i);
            if (bool) {
              Tile tile = Vars.world.tile(j, i);
              if (tile != null) {
                Building building = tile.build;
                if (building != null) {
                  Block block = tile.build.block;
                  if (block != null) {
                    Block block1 = tile.build.block;
                    bool = block1 instanceof mindustry.world.blocks.storage.CoreBlock;
                    if (!bool)
                      return b; 
                  } 
                } 
              } 
            } 
            j++;
            continue;
          } 
          i++;
        } 
        break;
      } 
      return false;
    } 
  }
  
  private static int[] exitOrder(int paramInt) {
    // Byte code:
    //   0: iconst_4
    //   1: istore_1
    //   2: iload_1
    //   3: newarray int
    //   5: astore_2
    //   6: aload_2
    //   7: iconst_0
    //   8: iload_0
    //   9: iastore
    //   10: iconst_0
    //   11: istore_3
    //   12: fconst_0
    //   13: fstore #4
    //   15: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   18: astore #5
    //   20: aload #5
    //   22: ifnull -> 158
    //   25: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   28: astore #5
    //   30: aload #5
    //   32: invokevirtual closestCore : ()Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;
    //   35: astore #5
    //   37: aload #5
    //   39: ifnull -> 158
    //   42: getstatic lunarians/LunariansSettings.drillStartX : I
    //   45: istore #6
    //   47: iconst_m1
    //   48: iconst_m1
    //   49: ishl
    //   50: istore #7
    //   52: iload #6
    //   54: iload #7
    //   56: if_icmpeq -> 158
    //   59: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   62: astore #5
    //   64: aload #5
    //   66: invokevirtual closestCore : ()Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;
    //   69: astore #5
    //   71: aload #5
    //   73: invokevirtual tileX : ()I
    //   76: istore #6
    //   78: getstatic lunarians/LunariansSettings.drillStartX : I
    //   81: istore #7
    //   83: iload #6
    //   85: iload #7
    //   87: isub
    //   88: istore #6
    //   90: iload #6
    //   92: i2f
    //   93: fstore #8
    //   95: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   98: astore #9
    //   100: aload #9
    //   102: invokevirtual closestCore : ()Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;
    //   105: astore #9
    //   107: aload #9
    //   109: invokevirtual tileY : ()I
    //   112: istore #7
    //   114: getstatic lunarians/LunariansSettings.drillStartY : I
    //   117: istore_3
    //   118: iload #7
    //   120: iload_3
    //   121: isub
    //   122: istore #7
    //   124: iload #7
    //   126: i2f
    //   127: fstore #4
    //   129: fload #8
    //   131: fstore #10
    //   133: fload #4
    //   135: fstore #8
    //   137: fload #10
    //   139: fstore #4
    //   141: goto -> 167
    //   144: pop
    //   145: iconst_0
    //   146: istore #6
    //   148: fconst_0
    //   149: fstore #8
    //   151: aconst_null
    //   152: astore #5
    //   154: fload #8
    //   156: fstore #4
    //   158: iconst_0
    //   159: istore #6
    //   161: fconst_0
    //   162: fstore #8
    //   164: aconst_null
    //   165: astore #5
    //   167: iload_1
    //   168: newarray boolean
    //   170: astore #9
    //   172: iconst_1
    //   173: istore #11
    //   175: aload #9
    //   177: iload_0
    //   178: iload #11
    //   180: bastore
    //   181: iconst_1
    //   182: istore_0
    //   183: iload_0
    //   184: iload_1
    //   185: if_icmpge -> 307
    //   188: iconst_m1
    //   189: istore #12
    //   191: ldc -3.4028235E38
    //   193: fstore #13
    //   195: iconst_0
    //   196: istore #14
    //   198: iload #14
    //   200: iload_1
    //   201: if_icmpge -> 288
    //   204: aload #9
    //   206: iload #14
    //   208: baload
    //   209: istore #15
    //   211: iload #15
    //   213: ifne -> 279
    //   216: getstatic lunarians/AutoDrill.DX : [I
    //   219: astore #16
    //   221: aload #16
    //   223: iload #14
    //   225: iaload
    //   226: istore #15
    //   228: iload #15
    //   230: i2f
    //   231: fload #4
    //   233: fmul
    //   234: fstore #17
    //   236: getstatic lunarians/AutoDrill.DY : [I
    //   239: astore #18
    //   241: aload #18
    //   243: iload #14
    //   245: iaload
    //   246: i2f
    //   247: fload #8
    //   249: fmul
    //   250: fstore #19
    //   252: fload #17
    //   254: fload #19
    //   256: fadd
    //   257: fstore #17
    //   259: fload #17
    //   261: fload #13
    //   263: fcmpl
    //   264: istore #20
    //   266: iload #20
    //   268: ifle -> 279
    //   271: iload #14
    //   273: istore #12
    //   275: fload #17
    //   277: fstore #13
    //   279: iload #14
    //   281: iconst_1
    //   282: iadd
    //   283: istore #14
    //   285: goto -> 198
    //   288: aload #9
    //   290: iload #12
    //   292: iload #11
    //   294: bastore
    //   295: aload_2
    //   296: iload_0
    //   297: iload #12
    //   299: iastore
    //   300: iload_0
    //   301: iconst_1
    //   302: iadd
    //   303: istore_0
    //   304: goto -> 183
    //   307: aload_2
    //   308: areturn
    //   309: pop
    //   310: goto -> 154
    // Exception table:
    //   from	to	target	type
    //   15	18	144	finally
    //   25	28	144	finally
    //   30	35	144	finally
    //   42	45	144	finally
    //   59	62	144	finally
    //   64	69	144	finally
    //   71	76	144	finally
    //   78	81	144	finally
    //   95	98	309	finally
    //   100	105	309	finally
    //   107	112	309	finally
    //   114	117	309	finally
  }
  
  private static int fillGaps(boolean paramBoolean, int paramInt1, int paramInt2, Item paramItem, boolean[] paramArrayOfboolean1, boolean[] paramArrayOfboolean2, boolean[] paramArrayOfboolean3, int paramInt3, int paramInt4, Block paramBlock, Seq paramSeq, IntSeq paramIntSeq1, IntSeq paramIntSeq2) {
    IntSeq intSeq1 = paramIntSeq1;
    IntSeq intSeq2 = paramIntSeq2;
    int i = paramIntSeq1.size;
    int j = 0;
    if (i == 0)
      return 0; 
    i = 0;
    int k = -1 >>> 1;
    int m = -1 >>> 1;
    int n = 0;
    int i1 = 0;
    while (true) {
      int i2 = intSeq1.size;
      int i3 = 1;
      if (i < i2) {
        i2 = intSeq1.get(i);
        int i7 = i2 % paramInt3;
        k = Math.min(k, i7);
        i2 /= paramInt3;
        m = Math.min(m, i2);
        i7 = i7 + paramInt1 - i3;
        n = Math.max(n, i7);
        i2 = i2 + paramInt1 - i3;
        i1 = Math.max(i1, i2);
        i++;
        continue;
      } 
      i = (paramInt1 * paramInt1 + 2) / 3;
      i2 = paramInt2;
      i = Math.min(paramInt2, i);
      int i4 = Math.max(i3, i);
      int i5 = 0;
      int i6 = 0;
      while (true) {
        i = 64;
        if (i5 < i) {
          i = -1;
          int i7 = m;
          i2 = -1;
          int i8 = 0;
          boolean[] arrayOfBoolean = null;
          while (true) {
            int i9 = i7 + paramInt1 - i3;
            if (i9 <= i1) {
              int i10 = i;
              int i11 = i2;
              i = k;
              while (true) {
                i2 = i + paramInt1 - i3;
                if (i2 <= n) {
                  if (paramBoolean) {
                    i2 = i;
                  } else {
                    i2 = i7;
                  } 
                  for (i9 = 0;; i9++) {
                    int i12 = intSeq2.size;
                    if (i9 < i12) {
                      i12 = intSeq2.get(i9);
                      if (i12 >= i2) {
                        int i13 = i2 + paramInt1;
                        i3 = i13 + -1;
                        if (i12 <= i3) {
                          i2 = i7;
                          i3 = i8;
                        } else {
                          continue;
                        } 
                      } else {
                        continue;
                      } 
                    } else {
                      i9 = paramInt3;
                      i12 = paramInt4;
                      i2 = i7;
                      i3 = i8;
                      i7 = paramInt1;
                      arrayOfBoolean = paramArrayOfboolean2;
                      boolean bool = canPlaceDrill(i, i2, paramInt1, paramArrayOfboolean2, paramInt3, paramInt4);
                      if (bool) {
                        Item item = paramItem;
                        i12 = paramInt3;
                        j = countOre(i, i2, paramInt1, paramItem, paramArrayOfboolean1, paramInt3);
                        if (j > i8) {
                          i10 = i;
                          i11 = i2;
                          i8 = j;
                          break;
                        } 
                      } 
                    } 
                    i8 = i3;
                    break;
                  } 
                  i++;
                  i7 = i2;
                  i3 = 1;
                  continue;
                } 
                i2 = i7;
                i7++;
                i = i10;
                i2 = i11;
                i3 = 1;
              } 
            } 
            i7 = paramInt1;
            i9 = paramInt3;
            if (i >= 0 && i8 >= i4) {
              BuildPlan buildPlan = new BuildPlan();
              int i10 = (paramInt1 + -1) / 2;
              j = i + i10;
              i10 += i2;
              this(j, i10, 0, paramBlock);
              paramSeq.add(buildPlan);
              i8 = i2 * paramInt3 + i;
              intSeq1.add(i8);
              boolean[] arrayOfBoolean1 = paramArrayOfboolean2;
              mark(i, i2, paramInt1, paramArrayOfboolean2, paramInt3);
              mark(i, i2, paramInt1, paramArrayOfboolean3, paramInt3);
              i6++;
              i5++;
              intSeq2 = paramIntSeq2;
              i3 = 1;
              continue;
            } 
            break;
          } 
        } 
        break;
      } 
      return i6;
    } 
  }
  
  private static void fixRotations(Seq paramSeq, Block paramBlock, int paramInt) {
    Seq seq = paramSeq;
    Block block = paramBlock;
    int i = lastHub;
    if (i >= 0 && paramSeq != null) {
      boolean bool = paramSeq.isEmpty();
      if (!bool) {
        World world = Vars.world;
        int j = world.height() * paramInt;
        boolean[] arrayOfBoolean = new boolean[j];
        int k = Vars.world.height() * paramInt;
        int[] arrayOfInt1 = new int[k];
        int m = -1;
        Arrays.fill(arrayOfInt1, m);
        int n = 0;
        int[] arrayOfInt2 = null;
        while (true) {
          int i1 = seq.size;
          boolean bool2 = true;
          if (n < i1) {
            BuildPlan buildPlan = (BuildPlan)seq.get(n);
            Block block1 = buildPlan.block;
            if (block1 == block) {
              int i2 = buildPlan.y * paramInt;
              i1 = buildPlan.x;
              i2 += i1;
              if (i2 >= 0 && i2 < j) {
                arrayOfBoolean[i2] = bool2;
                arrayOfInt1[i2] = n;
              } 
            } 
            n++;
            continue;
          } 
          n = lastHub;
          boolean bool1 = arrayOfBoolean[n];
          if (bool1) {
            arrayOfInt2 = new int[j];
            Arrays.fill(arrayOfInt2, m);
            IntQueue intQueue2 = new IntQueue();
            this();
            IntQueue intQueue1 = intQueue2;
            intQueue1 = intQueue2;
            i1 = lastHub;
            intQueue2.addLast(i1);
            i1 = lastHub;
            arrayOfInt2[i1] = i1;
            label61: while (true) {
              boolean bool3 = intQueue2.isEmpty();
              if (!bool3) {
                int i2 = intQueue2.removeFirst();
                int i3 = i2 % paramInt;
                int i4 = i2 / paramInt;
                int i5 = 0;
                Object object = null;
                while (true) {
                  int i6 = 4;
                  if (i5 < i6) {
                    int[] arrayOfInt3 = DX;
                    i6 = arrayOfInt3[i5] + i3;
                    int[] arrayOfInt4 = DY;
                    int i7 = arrayOfInt4[i5] + i4;
                    Tiles tiles = Vars.world.tiles;
                    boolean bool4 = tiles.in(i6, i7);
                    if (bool4) {
                      i7 = i7 * paramInt + i6;
                      i6 = arrayOfInt2[i7];
                      if (i6 < 0) {
                        boolean bool5 = arrayOfBoolean[i7];
                        if (bool5) {
                          arrayOfInt2[i7] = i2;
                          intQueue2.addLast(i7);
                        } 
                      } 
                    } 
                    i5++;
                    continue;
                  } 
                  continue label61;
                } 
              } 
              m = 0;
              intQueue2 = null;
              while (m < j) {
                bool3 = arrayOfBoolean[m];
                if (bool3) {
                  int i2 = arrayOfInt2[m];
                  if (i2 >= 0 && i2 != m) {
                    int i3 = m % paramInt;
                    int i4 = m / paramInt;
                    int i5 = i2 % paramInt;
                    i2 /= paramInt;
                    if (i5 > i3) {
                      i2 = 0;
                      intQueue1 = null;
                    } else if (i5 < i3) {
                      i2 = 2;
                    } else if (i2 > i4) {
                      i2 = 1;
                    } else {
                      i2 = 3;
                    } 
                    i3 = arrayOfInt1[m];
                    if (i3 >= 0) {
                      BuildPlan buildPlan2 = (BuildPlan)seq.get(i3);
                      BuildPlan buildPlan1 = new BuildPlan();
                      int i6 = buildPlan2.x;
                      i4 = buildPlan2.y;
                      this(i6, i4, i2, block);
                      seq.set(i3, buildPlan1);
                    } 
                  } 
                } 
                m++;
              } 
              break;
            } 
          } 
          break;
        } 
      } 
    } 
  }
  
  static boolean floorOre(Tile paramTile) {
    if (paramTile == null)
      return false; 
    Floor floor = paramTile.overlay();
    if (floor != null) {
      Item item = (paramTile.overlay()).itemDrop;
      if (item != null)
        return false; 
    } 
    return true;
  }
  
  private static boolean free(int paramInt1, int paramInt2, boolean[] paramArrayOfboolean, int paramInt3, int paramInt4) {
    Tiles tiles = Vars.world.tiles;
    boolean bool1 = tiles.in(paramInt1, paramInt2);
    if (!bool1)
      return false; 
    paramInt3 = paramInt3 * paramInt2 + paramInt1;
    boolean bool2 = paramArrayOfboolean[paramInt3];
    if (bool2)
      return false; 
    World world = Vars.world;
    Tile tile = world.tile(paramInt1, paramInt2);
    if (tile != null) {
      boolean bool = tile.solid();
      if (!bool) {
        Building building = tile.build;
        if (building != null) {
          Block block = tile.build.block;
          if (block != null) {
            Block block1 = tile.build.block;
            boolean bool3 = block1.solid;
            if (bool3)
              return false; 
          } 
        } 
        return true;
      } 
    } 
    return false;
  }
  
  private static boolean freeConv(int paramInt1, int paramInt2, boolean[] paramArrayOfboolean, int paramInt3, int paramInt4) {
    Tiles tiles = Vars.world.tiles;
    boolean bool2 = tiles.in(paramInt1, paramInt2);
    if (!bool2)
      return false; 
    paramInt3 = paramInt3 * paramInt2 + paramInt1;
    boolean bool3 = paramArrayOfboolean[paramInt3];
    if (bool3)
      return false; 
    Tile tile = Vars.world.tile(paramInt1, paramInt2);
    if (tile == null)
      return false; 
    Building building = tile.build;
    if (building != null) {
      Block block1 = tile.build.block;
      if (block1 != null) {
        block1 = tile.build.block;
        boolean bool = block1.solid;
        if (bool)
          return false; 
      } 
    } 
    boolean bool1 = limitOn;
    if (bool1) {
      building = tile.build;
      if (building == null) {
        boolean bool = nextToDrill(paramInt1, paramInt2);
        if (bool)
          return false; 
      } else {
        return false;
      } 
    } 
    Block block = tile.block();
    if (block != null) {
      block = tile.block();
      boolean bool = block.solid;
      if (bool) {
        block = tile.block();
        bool = block.isStatic();
        if (bool)
          return false; 
      } 
    } 
    return true;
  }
  
  public static int generate() {
    return run();
  }
  
  private static int generateInner() {
    failedNoneFit = false;
    int i = generateInner2(true);
    if (i <= 0) {
      boolean bool = failedNoneFit;
      if (bool) {
        failedNoneFit = false;
        return generateInner2(false);
      } 
    } 
    return i;
  }
  
  private static int generateInner2(boolean paramBoolean) {
    byte b = -1;
    lastHub = b;
    int i = LunariansSettings.drillStartX;
    int j = LunariansSettings.drillStartY;
    int k = 0;
    IntSeq intSeq = null;
    int m = -1 << -1;
    if (i != m && j != m) {
      Tile tile = Vars.world.tile(i, j);
      Item item = oreAt(tile);
      if (item == null) {
        toast(LunariansI18n.get("dr_no_ore"));
        return 0;
      } 
      Block block1 = resolveDrillFor(item);
      Block block2 = resolveConveyor();
      boolean bool = block1 instanceof Drill;
      if (bool && block2 != null) {
        boolean bool2;
        int i8;
        Block block = block1;
        Drill drill = (Drill)block1;
        int n = drill.tier;
        int i1 = item.hardness;
        if (n < i1) {
          String str;
          StringBuilder stringBuilder;
          boolean bool3 = pt();
          if (bool3) {
            str = item.localizedName;
            stringBuilder = new StringBuilder();
            String str1 = "Nenhuma drill disponível consegue minerar ";
            this(str1);
          } else {
            str = item.localizedName;
            stringBuilder = new StringBuilder();
            String str1 = "No available drill can mine ";
            this(str1);
          } 
          stringBuilder.append(str);
          toast(stringBuilder.toString());
          return 0;
        } 
        n = block1.size;
        int i3 = LunariansSettings.drillMaxTiles;
        i1 = Math.max(16, i3);
        int i2 = limitOn;
        if (i2) {
          boolean bool3 = floorOre(tile);
          if (bool3) {
            int i10 = AutoSetConfig.floorMax();
            i1 = Math.min(i1, i10);
          } 
        } 
        int i4 = LunariansSettings.drillMinOres;
        i2 = 1;
        i4 = Math.max(i2, i4);
        int i5 = n * n + i2;
        int i6 = 2;
        i5 /= i6;
        boolean bool1 = limitOn;
        if (bool1 && paramBoolean && i5 > i4) {
          i4 = i5;
          bool2 = true;
        } else {
          bool2 = false;
        } 
        i5 = LunariansSettings.drillExitDir;
        int i7 = 3;
        i5 &= i7;
        int i9 = LunariansSettings.drillLayoutMode;
        if (i9 == i2) {
          i8 = 1;
          float f = Float.MIN_VALUE;
        } else {
          i8 = 0;
          float f = 0.0F;
        } 
        Player player = Vars.player;
        Unit unit = player.unit();
        if (unit != null) {
          boolean bool3 = unit.canBuild();
          if (bool3) {
            int i11 = Vars.world.width();
            World world = Vars.world;
            int i12 = world.height();
            i6 = i11 * i12;
            boolean[] arrayOfBoolean2 = new boolean[i6];
            intSeq = new IntSeq();
            this();
            IntQueue intQueue = new IntQueue();
            this();
            int i13 = j * i11;
            int i14 = 1;
            int i10 = i13 + i;
            intQueue.addLast(i10);
            arrayOfBoolean2[i10] = i14;
            while (true) {
              boolean bool5 = intQueue.isEmpty();
              if (!bool5) {
                int i16 = intSeq.size;
                if (i16 < i1) {
                  i16 = intQueue.removeFirst();
                  intSeq.add(i16);
                  i13 = i16 % i11;
                  i16 /= i11;
                  boolean[] arrayOfBoolean = arrayOfBoolean2;
                  int i17 = i4;
                  m = 0;
                  arrayOfBoolean2 = null;
                  while (true) {
                    i4 = 4;
                    arrayOfBoolean2 = arrayOfBoolean;
                    i4 = i17;
                  } 
                } 
              } 
              break;
            } 
            boolean[] arrayOfBoolean3 = arrayOfBoolean2;
            int i15 = i4;
            Block block3 = block2;
            boolean bool4 = intSeq.isEmpty();
            if (bool4) {
              toast(LunariansI18n.get("dr_empty"));
              return 0;
            } 
            i10 = i8;
            j = 0;
            boolean[] arrayOfBoolean1 = null;
            m = -1 >>> 1;
            i4 = -1 >>> 1;
            i7 = 0;
            intQueue = null;
            i8 = 0;
            float f = 0.0F;
            while (true) {
              int i18;
              Object object2;
              Object object3;
              Item item1;
              Object object5;
              IntSeq intSeq2;
              boolean bool8;
              Object object8;
              Object object9;
              int i21 = intSeq.size;
              if (j < i21)
                continue; 
              boolean bool5 = limitOn;
              if (bool5) {
                arrayOfBoolean1 = AutoConnectConveyor.reservedFromPlans(unit);
              } else {
                arrayOfBoolean1 = new boolean[i6];
              } 
              Object object1 = new boolean[i6];
              Seq seq = new Seq();
              this();
              IntSeq intSeq1 = lastDrillTiles;
              intSeq1.clear();
              IntSeq intSeq3 = new IntSeq();
              this();
              i21 = 1;
              float f1 = Float.MIN_VALUE;
              if (i5 != i21) {
                i1 = 3;
                if (i5 != i1) {
                  i21 = 0;
                  f1 = 0.0F;
                  intSeq1 = null;
                } 
              } 
              boolean bool7 = LunariansSettings.drillPerfect;
              if (bool7) {
                int i23;
                i1 = 0;
                intSeq2 = null;
                bool7 = false;
                i13 = 0;
                boolean[] arrayOfBoolean4 = null;
                int i24 = -1;
                int i25 = -1 >>> 1;
                while (i1 <= n) {
                  boolean bool10;
                  int i27;
                  Item item2 = item;
                  boolean bool11 = bool7;
                  int i28 = i13;
                  int i29 = 0;
                  float f2 = 0.0F;
                  item = null;
                  while (i29 <= n) {
                    arrayOfBoolean4 = (boolean[])object3.clone();
                    boolean[] arrayOfBoolean5 = (boolean[])object1.clone();
                    Object object19 = object6;
                    object6 = new Seq();
                    super();
                    IntSeq intSeq7 = new IntSeq();
                    this();
                    IntSeq intSeq8 = new IntSeq();
                    this();
                    int i31 = i10;
                    i10 = n;
                    n = i31;
                    Object object17 = object5;
                    Object object20 = object2;
                    Object object21 = object5;
                    int i32 = i4;
                    boolean[] arrayOfBoolean6 = arrayOfBoolean4;
                    arrayOfBoolean4 = arrayOfBoolean3;
                    arrayOfBoolean3 = arrayOfBoolean5;
                    Object object22 = object19;
                    int i30 = i24;
                    i4 = i25;
                    Object object18 = object3;
                    bool5 = bool10;
                    i19 = m;
                    int[] arrayOfInt1 = scanGrid(i21, n, i1, i29, i10, m, i32, i7, i8, object5, item2, arrayOfBoolean4, arrayOfBoolean6, arrayOfBoolean3, i11, i12, (Block)object2, (Seq)object6, intSeq7, intSeq8);
                    int i33 = i29;
                    i29 = i21;
                    f2 = f1;
                    i21 = i32;
                    i32 = i33;
                    i33 = i10;
                    i10 = n;
                    n = i33;
                    arrayOfBoolean3 = arrayOfBoolean4;
                    boolean bool12 = true;
                    m = arrayOfInt1[0];
                    object6 = object1;
                    if (m != 0) {
                      k = arrayOfInt1[bool12];
                      if (k > i24 || (k == i24 && m < i25)) {
                        i24 = k;
                        i25 = m;
                        i27 = i1;
                        i28 = i32;
                        continue;
                      } 
                    } 
                    i24 = i30;
                    i25 = i4;
                    continue;
                    i30 = bool7 + 1;
                    object1 = SYNTHETIC_LOCAL_VARIABLE_53;
                    object5 = SYNTHETIC_LOCAL_VARIABLE_54;
                    object2 = SYNTHETIC_LOCAL_VARIABLE_53;
                    i4 = i21;
                    i21 = i29;
                    f1 = f2;
                    m = i5;
                    object1 = block3;
                    object6 = SYNTHETIC_LOCAL_VARIABLE_56;
                    i29 = i30;
                    bool10 = bool5;
                    object3 = SYNTHETIC_LOCAL_VARIABLE_49;
                  } 
                  Object object12 = object5;
                  Object object14 = object2;
                  Object object15 = object5;
                  Object object13 = object3;
                  i29 = i21;
                  f2 = f1;
                  int i26 = i19;
                  Object object16 = object6;
                  object6 = object1;
                  i21 = i4;
                  i1++;
                  object1 = object2;
                  i21 = i29;
                  item = item2;
                  object1 = object6;
                  i23 = i27;
                  i13 = i28;
                  object6 = object16;
                } 
                Object object = object5;
                object8 = object2;
                object9 = object5;
                item1 = item;
                i1 = i23;
                bool8 = i13;
              } else {
                Object object = object5;
                object8 = object2;
                object9 = object5;
                item1 = item;
                i1 = 0;
                intSeq2 = null;
                bool8 = false;
                float f2 = 0.0F;
                item = null;
              } 
              Object object7 = object3;
              int i17 = i19;
              Object object11 = object6;
              boolean bool9 = true;
              Object object6 = object1;
              int i19 = m;
              int i20 = i4;
              IntSeq intSeq4 = lastDrillTiles;
              i14 = i10;
              i10 = n;
              n = i14;
              IntSeq intSeq5 = intSeq4;
              boolean[] arrayOfBoolean = arrayOfBoolean3;
              Object object4 = object1;
              object6 = seq;
              IntSeq intSeq6 = intSeq3;
              Object object10 = object3;
              int[] arrayOfInt = scanGrid(i21, i14, i1, bool8, i10, m, i4, i7, i8, object9, item1, arrayOfBoolean, (boolean[])object3, (boolean[])object1, i11, i12, (Block)object8, seq, intSeq5, intSeq3);
              n = i10;
              int i16 = arrayOfInt[0];
              boolean bool6 = LunariansSettings.drillPerfect;
              if (bool6 && i16 > 0) {
                Object object12 = object9;
                item = item1;
                boolean[] arrayOfBoolean4 = arrayOfBoolean;
                Object object14 = object3;
                i7 = i11;
                i8 = i12;
                object2 = object8;
                Seq seq1 = seq;
                IntSeq intSeq8 = intSeq5;
                object10 = intSeq3;
                i18 = fillGaps(i21, i10, object9, item, arrayOfBoolean4, (boolean[])object3, (boolean[])object1, i11, i12, (Block)object8, seq, intSeq5, intSeq3);
                Object object13 = object3;
                i20 = i11;
                i7 = i12;
                Seq seq2 = seq;
                IntSeq intSeq7 = intSeq3;
                i16 += i18;
              } else {
                Object object13 = object10;
                i20 = i11;
                i7 = i12;
                Object object12 = object6;
                IntSeq intSeq7 = intSeq6;
              } 
              if (i16 == 0) {
                if (bool2) {
                  failedNoneFit = bool9;
                  return 0;
                } 
                toast(LunariansI18n.get("dr_none_fit"));
                return 0;
              } 
              bool6 = false;
              object1 = null;
              i4 = 0;
              tile = null;
              i21 = 0;
              f1 = 0.0F;
              intSeq1 = null;
              i8 = -1 >>> 1;
              f = 0.0F / 0.0F;
              int i22 = -1 >>> 1;
              while (true) {
                intSeq2 = lastDrillTiles;
                i19 = intSeq2.size;
                i1 = intSeq2.get(bool6);
                i19 = i1 % i20;
                i1 /= i20;
                i8 = Math.min(i8, i19);
                i22 = Math.min(i22, i1);
                i19 = i19 + n + -1;
                i4 = Math.max(i4, i19);
                i1 = i1 + n + -1;
                i21 = Math.max(i21, i1);
                i18 = bool6 + 1;
              } 
              break;
              i16 = intSeq.get(j);
              i1 = i16 % i11;
              i16 /= i11;
              m = Math.min(m, i1);
              i4 = Math.min(i4, i16);
              i7 = Math.max(i7, i1);
              i8 = Math.max(i8, i16);
              j++;
            } 
          } 
        } 
        toast(LunariansI18n.get("cv_need_builder"));
        return 0;
      } 
      toast(LunariansI18n.get("dr_invalid"));
      return 0;
    } 
    toast(LunariansI18n.get("dr_set_ore"));
    return 0;
  }
  
  public static int generateWithConveyor() {
    boolean bool = Vars.headless;
    if (!bool) {
      Player player = Vars.player;
      if (player != null) {
        GameState gameState = Vars.state;
        if (gameState != null) {
          gameState = Vars.state;
          bool = gameState.isGame();
          if (bool) {
            int i = LunariansSettings.drillExitDir & 0x3;
            boolean bool1 = true;
            try {
              Player player1 = Vars.player;
              Unit unit = player1.unit();
              if (unit != null) {
                boolean bool2 = unit.canBuild();
                if (bool2) {
                  boolean[] arrayOfBoolean = AutoConnectConveyor.reservedFromPlans(unit);
                  int[] arrayOfInt = exitOrder(i);
                  World world = Vars.world;
                  int j = world.width();
                  int k = 0;
                  while (true) {
                    int m = arrayOfInt.length;
                    if (k < m) {
                      m = arrayOfInt[k];
                      LunariansSettings.drillExitDir = m;
                      dryRun = bool1;
                      try {
                        int n = generateInner();
                        dryRun = false;
                      } finally {
                        dryRun = false;
                      } 
                      break;
                    } 
                    boolean bool3 = quiet;
                    if (!bool3) {
                      String str1 = "cv_no_path";
                      str1 = LunariansI18n.get(str1);
                      toast(str1);
                    } 
                    break;
                  } 
                  LunariansSettings.drillExitDir = i;
                  return 0;
                } 
              } 
              String str = "cv_need_builder";
              str = LunariansI18n.get(str);
            } finally {
              Exception exception = null;
            } 
            LunariansSettings.drillExitDir = i;
            dryRun = false;
            return 0;
          } 
        } 
      } 
    } 
    return 0;
  }
  
  private static boolean hasAdjConv(int paramInt1, int paramInt2, int paramInt3, boolean[] paramArrayOfboolean, int paramInt4) {
    int i;
    for (i = 0; i < paramInt3; i++) {
      int j = 0;
      while (j < paramInt3) {
        byte b = 0;
        while (true) {
          byte b1 = 4;
          j++;
        } 
      } 
    } 
    return false;
  }
  
  static boolean inFloor(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    boolean bool = limitOn;
    boolean bool1 = true;
    if (!bool)
      return bool1; 
    Tile tile = Vars.world.tile(paramInt3, paramInt4);
    bool = floorOre(tile);
    if (!bool)
      return bool1; 
    float f1 = paramInt1;
    float f2 = paramInt2;
    float f3 = paramInt3;
    float f4 = paramInt4;
    f1 = Mathf.dst(f1, f2, f3, f4);
    paramInt2 = AutoSetConfig.floorMax();
    double d = Math.sqrt(paramInt2);
    f2 = (float)d * 0.75F + 0.5F;
    paramInt3 = 1075838976;
    f3 = 2.5F;
    f2 = Math.max(f3, f2);
    paramInt1 = f1 cmp f2;
    return (paramInt1 <= 0) ? bool1 : false;
  }
  
  static boolean inLimit(int paramInt1, int paramInt2) {
    boolean bool = limitOn;
    if (bool) {
      float f1 = paramInt1;
      float f2 = paramInt2;
      float f3 = limitX;
      float f4 = limitY;
      f1 = Mathf.dst(f1, f2, f3, f4);
      f2 = limitR;
      paramInt1 = f1 cmp f2;
      if (paramInt1 > 0)
        return false; 
    } 
    return true;
  }
  
  public static void init() {}
  
  private static void mark(int paramInt1, int paramInt2, int paramInt3, boolean[] paramArrayOfboolean, int paramInt4) {
    int i;
    for (i = 0; i < paramInt3; i++) {
      int j;
      for (j = 0; j < paramInt3; j++) {
        int k = (paramInt2 + i) * paramInt4;
        int m = paramInt1 + j;
        k += m;
        m = 1;
        paramArrayOfboolean[k] = m;
      } 
    } 
  }
  
  private static void markAround(boolean[] paramArrayOfboolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = 0;
    while (true) {
      int j = 4;
      if (i < j) {
        int[] arrayOfInt1 = DX;
        j = arrayOfInt1[i] + paramInt1;
        int[] arrayOfInt2 = DY;
        int k = arrayOfInt2[i] + paramInt2;
        if (j >= 0 && k >= 0 && j < paramInt3 && k < paramInt4) {
          k = k * paramInt3 + j;
          j = 1;
          paramArrayOfboolean[k] = j;
        } 
        i++;
        continue;
      } 
      break;
    } 
  }
  
  static boolean nextToDrill(int paramInt1, int paramInt2) {
    int i = 0;
    while (true) {
      byte b = 4;
      if (i < b) {
        World world = Vars.world;
        int[] arrayOfInt1 = DX;
        int j = arrayOfInt1[i] + paramInt1;
        int[] arrayOfInt2 = DY;
        int k = arrayOfInt2[i] + paramInt2;
        Tile tile = world.tile(j, k);
        if (tile != null) {
          Building building = tile.build;
          if (building != null) {
            Block block = tile.build.block;
            boolean bool = block instanceof Drill;
            if (bool)
              return true; 
          } 
        } 
        i++;
        continue;
      } 
      return false;
    } 
  }
  
  private static Item oreAt(Tile paramTile) {
    Floor floor1;
    if (paramTile == null)
      return null; 
    try {
      Item item = paramTile.drop();
      if (item != null)
        return paramTile.drop(); 
    } finally {}
    Floor floor2 = paramTile.overlay();
    if (floor2 != null) {
      floor1 = paramTile.overlay();
    } else {
      floor1 = floor1.floor();
    } 
    if (floor1 != null) {
      Item item = floor1.itemDrop;
      if (item != null)
        return floor1.itemDrop; 
    } 
    return null;
  }
  
  private static int[] pickFace(int paramInt1, int paramInt2, int paramInt3, int paramInt4, IntSeq paramIntSeq, boolean[] paramArrayOfboolean1, boolean[] paramArrayOfboolean2, int paramInt5, int paramInt6) {
    int i4;
    int i = paramInt1;
    int j = paramInt2;
    int k = paramInt3;
    int m = paramInt4;
    IntSeq intSeq = paramIntSeq;
    boolean[] arrayOfBoolean = paramArrayOfboolean1;
    int n = paramInt5;
    int i1 = paramInt6;
    int i2 = -1 >>> 1;
    int i3 = 3;
    int i5 = -1 << -1;
    int i6 = 0;
    byte b = 1;
    if (paramInt4 != b && paramInt4 != i3) {
      int i8 = 0;
      int i9 = -1 << -1;
      while (true) {
        i3 = intSeq.size;
        if (i8 < i3) {
          i3 = intSeq.get(i8);
          int i11 = k / 2;
          i11 = j + i11;
          i11 = i3 - i11;
          int i12 = Math.abs(i11);
          if (i12 < i2) {
            i9 = i3;
            i2 = i12;
          } 
          i8++;
          continue;
        } 
        if (i9 != i5) {
          int i11;
          if (i9 > j) {
            i11 = j + k;
          } else {
            i11 = j + -1;
          } 
          i2 = 0;
          while (i2 < k) {
            i3 = i + i2;
            boolean bool = freeConv(i3, i11, arrayOfBoolean, n, i1);
            if (!bool) {
              i2++;
              continue;
            } 
            if (i9 <= j)
              b = 3; 
            return new int[] { i3, i11, b };
          } 
        } 
        int i10 = 0;
        intSeq = null;
        while (i10 < k) {
          byte b1;
          if (m == 0) {
            i2 = i + k;
            i3 = j + i10;
            b1 = 0;
          } else {
            i2 = i + -1;
            i3 = j + i10;
            b1 = 2;
          } 
          i4 = freeConv(i2, i3, arrayOfBoolean, n, i1);
          if (!i4) {
            i10++;
            continue;
          } 
          return new int[] { i2, i3, b1 };
        } 
        return null;
      } 
    } 
    i3 = 0;
    int i7 = -1 << -1;
    while (true) {
      int i8 = intSeq.size;
      if (i3 < i8) {
        i8 = intSeq.get(i3);
        int i9 = k / 2 + i;
        i9 = Math.abs(i8 - i9);
        if (i9 < i2) {
          i7 = i8;
          i2 = i9;
        } 
        i3++;
        continue;
      } 
      if (i7 != i4) {
        int i9;
        if (i7 > i) {
          i9 = i + k;
        } else {
          i9 = i + -1;
        } 
        i2 = 0;
        while (i2 < k) {
          byte b1;
          i3 = j + i2;
          boolean bool = freeConv(i9, i3, arrayOfBoolean, n, i1);
          if (!bool) {
            i2++;
            continue;
          } 
          if (i7 > i) {
            b1 = 0;
          } else {
            b1 = 2;
          } 
          return new int[] { i9, i3, b1 };
        } 
      } 
      while (i6 < k) {
        int i9 = i + i6;
        if (m == b) {
          i2 = j + k;
          i3 = 1;
        } else {
          i2 = j + -1;
          i3 = 3;
        } 
        boolean bool = freeConv(i9, i2, arrayOfBoolean, n, i1);
        if (!bool) {
          i6++;
          continue;
        } 
        return new int[] { i9, i2, i3 };
      } 
      return null;
    } 
  }
  
  private static boolean pollutes(Block paramBlock) {
    boolean bool = paramBlock instanceof Drill;
    if (!bool) {
      bool = paramBlock instanceof mindustry.world.blocks.production.GenericCrafter;
      if (!bool) {
        boolean bool1 = paramBlock instanceof mindustry.world.blocks.storage.Unloader;
        if (!bool1)
          return false; 
      } 
    } 
    return true;
  }
  
  private static Seq pruneConveyors(Seq paramSeq, Block paramBlock, int paramInt) {
    Seq seq = paramSeq;
    Block block = paramBlock;
    int i = paramInt;
    try {
    
    } finally {
      block = null;
      boolean bool = errorLogged;
    } 
    return seq;
  }
  
  private static boolean pt() {
    String str = LunariansSettings.language;
    return "pt".equals(str);
  }
  
  public static boolean[] reservedForFeed(Unit paramUnit) {
    boolean[] arrayOfBoolean = AutoConnectConveyor.reservedFromPlans(paramUnit);
    try {
      World world1 = Vars.world;
      int i = world1.width();
      World world2 = Vars.world;
      int j = world2.height();
      int k = 0;
      BuildPlan buildPlan = null;
      while (k < j) {
        int m = 0;
        Object object = null;
        while (m < i) {
          World world = Vars.world;
          Tile tile = world.tile(m, k);
          if (tile != null) {
            Building building = tile.build;
            if (building != null) {
              building = tile.build;
              Block block = building.block;
              if (block != null) {
                Building building1 = tile.build;
                Block block1 = building1.block;
                boolean bool = pollutes(block1);
                if (bool)
                  markAround(arrayOfBoolean, m, k, i, j); 
              } 
            } 
          } 
          m++;
        } 
        k++;
      } 
      Queue queue = paramUnit.plans();
      Iterator<Object> iterator = queue.iterator();
      while (true) {
        boolean bool = iterator.hasNext();
        if (bool) {
          buildPlan = (BuildPlan)iterator.next();
          buildPlan = buildPlan;
          boolean bool1 = buildPlan.breaking;
          if (!bool1) {
            Block block = buildPlan.block;
            if (block != null) {
              block = buildPlan.block;
              bool1 = pollutes(block);
              if (bool1) {
                block = buildPlan.block;
                int m = block.size;
                int n = m + -1;
                n /= 2;
                int i1 = 0;
                Object object = null;
                while (i1 < m) {
                  int i2;
                  for (i2 = 0; i2 < m; i2++) {
                    int i3 = buildPlan.x - n + i2;
                    int i4 = buildPlan.y - n + i1;
                    markAround(arrayOfBoolean, i3, i4, i, j);
                  } 
                  i1++;
                } 
              } 
            } 
          } 
          continue;
        } 
        break;
      } 
    } finally {
      paramUnit = null;
      boolean bool = errorLogged;
    } 
    return arrayOfBoolean;
  }
  
  public static void reset() {
    lastDrillTiles.clear();
    lastCount = 0;
    lastHub = -1;
  }
  
  private static Block resolveConveyor() {
    // Byte code:
    //   0: getstatic lunarians/LunariansSettings.conveyorBlock : Ljava/lang/String;
    //   3: astore_0
    //   4: aload_0
    //   5: ifnull -> 17
    //   8: aload_0
    //   9: invokevirtual isEmpty : ()Z
    //   12: istore_1
    //   13: iload_1
    //   14: ifeq -> 21
    //   17: ldc_w 'conveyor'
    //   20: astore_0
    //   21: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   24: astore_2
    //   25: aload_2
    //   26: aload_0
    //   27: invokevirtual block : (Ljava/lang/String;)Lmindustry/world/Block;
    //   30: astore_0
    //   31: aload_0
    //   32: instanceof mindustry/world/blocks/distribution/Conveyor
    //   35: istore_1
    //   36: iload_1
    //   37: ifeq -> 45
    //   40: aload_0
    //   41: checkcast mindustry/world/Block
    //   44: areturn
    //   45: getstatic mindustry/content/Blocks.conveyor : Lmindustry/world/Block;
    //   48: areturn
  }
  
  private static Block resolveDrill() {
    // Byte code:
    //   0: getstatic lunarians/LunariansSettings.drillBlock : Ljava/lang/String;
    //   3: astore_0
    //   4: aload_0
    //   5: ifnull -> 17
    //   8: aload_0
    //   9: invokevirtual isEmpty : ()Z
    //   12: istore_1
    //   13: iload_1
    //   14: ifeq -> 21
    //   17: ldc_w 'mechanical-drill'
    //   20: astore_0
    //   21: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   24: astore_2
    //   25: aload_2
    //   26: aload_0
    //   27: invokevirtual block : (Ljava/lang/String;)Lmindustry/world/Block;
    //   30: astore_0
    //   31: aload_0
    //   32: instanceof mindustry/world/blocks/production/Drill
    //   35: istore_1
    //   36: iload_1
    //   37: ifeq -> 45
    //   40: aload_0
    //   41: checkcast mindustry/world/Block
    //   44: areturn
    //   45: getstatic mindustry/content/Blocks.mechanicalDrill : Lmindustry/world/Block;
    //   48: areturn
  }
  
  private static Block resolveDrillFor(Item paramItem) {
    // Byte code:
    //   0: getstatic lunarians/AutoDrill.limitOn : Z
    //   3: istore_1
    //   4: aconst_null
    //   5: astore_2
    //   6: iload_1
    //   7: ifeq -> 60
    //   10: aload_0
    //   11: ifnull -> 60
    //   14: aload_0
    //   15: getfield name : Ljava/lang/String;
    //   18: invokestatic drillFor : (Ljava/lang/String;)Ljava/lang/String;
    //   21: astore_3
    //   22: aload_3
    //   23: ifnull -> 60
    //   26: aload_3
    //   27: invokevirtual isEmpty : ()Z
    //   30: istore #4
    //   32: iload #4
    //   34: ifne -> 60
    //   37: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   40: astore #5
    //   42: aload #5
    //   44: aload_3
    //   45: invokevirtual block : (Ljava/lang/String;)Lmindustry/world/Block;
    //   48: astore_3
    //   49: aload_3
    //   50: instanceof mindustry/world/blocks/production/Drill
    //   53: istore #4
    //   55: iload #4
    //   57: ifne -> 64
    //   60: iconst_0
    //   61: istore_1
    //   62: aconst_null
    //   63: astore_3
    //   64: aload_3
    //   65: ifnonnull -> 72
    //   68: invokestatic resolveDrill : ()Lmindustry/world/Block;
    //   71: astore_3
    //   72: aload_0
    //   73: ifnull -> 419
    //   76: aload_3
    //   77: instanceof mindustry/world/blocks/production/Drill
    //   80: istore #4
    //   82: iload #4
    //   84: ifeq -> 419
    //   87: aload_3
    //   88: astore #5
    //   90: aload_3
    //   91: checkcast mindustry/world/blocks/production/Drill
    //   94: astore #5
    //   96: aload #5
    //   98: getfield tier : I
    //   101: istore #4
    //   103: aload_0
    //   104: getfield hardness : I
    //   107: istore #6
    //   109: iload #4
    //   111: iload #6
    //   113: if_icmpge -> 419
    //   116: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   119: invokevirtual blocks : ()Larc/struct/Seq;
    //   122: invokevirtual iterator : ()Ljava/util/Iterator;
    //   125: astore #5
    //   127: aload #5
    //   129: invokeinterface hasNext : ()Z
    //   134: istore #6
    //   136: iload #6
    //   138: ifeq -> 262
    //   141: aload #5
    //   143: invokeinterface next : ()Ljava/lang/Object;
    //   148: checkcast mindustry/world/Block
    //   151: astore #7
    //   153: aload #7
    //   155: instanceof mindustry/world/blocks/production/Drill
    //   158: istore #8
    //   160: iload #8
    //   162: ifeq -> 127
    //   165: aload #7
    //   167: checkcast mindustry/world/blocks/production/Drill
    //   170: astore #7
    //   172: aload #7
    //   174: getfield tier : I
    //   177: istore #8
    //   179: aload_0
    //   180: getfield hardness : I
    //   183: istore #9
    //   185: iload #8
    //   187: iload #9
    //   189: if_icmplt -> 127
    //   192: aload_2
    //   193: ifnull -> 256
    //   196: aload #7
    //   198: getfield tier : I
    //   201: istore #8
    //   203: aload_2
    //   204: getfield tier : I
    //   207: istore #9
    //   209: iload #8
    //   211: iload #9
    //   213: if_icmplt -> 256
    //   216: aload #7
    //   218: getfield tier : I
    //   221: istore #8
    //   223: aload_2
    //   224: getfield tier : I
    //   227: istore #9
    //   229: iload #8
    //   231: iload #9
    //   233: if_icmpne -> 127
    //   236: aload #7
    //   238: getfield size : I
    //   241: istore #8
    //   243: aload_2
    //   244: getfield size : I
    //   247: istore #9
    //   249: iload #8
    //   251: iload #9
    //   253: if_icmpge -> 127
    //   256: aload #7
    //   258: astore_2
    //   259: goto -> 127
    //   262: aload_2
    //   263: ifnull -> 419
    //   266: getstatic lunarians/AutoDrill.quiet : Z
    //   269: istore #4
    //   271: iload #4
    //   273: ifne -> 417
    //   276: invokestatic pt : ()Z
    //   279: istore #4
    //   281: aload_3
    //   282: checkcast mindustry/world/Block
    //   285: getfield localizedName : Ljava/lang/String;
    //   288: astore_3
    //   289: aload_0
    //   290: getfield localizedName : Ljava/lang/String;
    //   293: astore_0
    //   294: iload #4
    //   296: ifeq -> 347
    //   299: aload_2
    //   300: getfield localizedName : Ljava/lang/String;
    //   303: astore #5
    //   305: new java/lang/StringBuilder
    //   308: astore #7
    //   310: aload #7
    //   312: invokespecial <init> : ()V
    //   315: aload #7
    //   317: aload_3
    //   318: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   321: pop
    //   322: ldc_w ' não minera '
    //   325: astore_3
    //   326: aload #7
    //   328: aload_3
    //   329: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   332: pop
    //   333: aload #7
    //   335: aload_0
    //   336: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   339: pop
    //   340: ldc_w ': usando '
    //   343: astore_0
    //   344: goto -> 392
    //   347: aload_2
    //   348: getfield localizedName : Ljava/lang/String;
    //   351: astore #5
    //   353: new java/lang/StringBuilder
    //   356: astore #7
    //   358: aload #7
    //   360: invokespecial <init> : ()V
    //   363: aload #7
    //   365: aload_3
    //   366: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   369: pop
    //   370: ldc_w ' cannot mine '
    //   373: astore_3
    //   374: aload #7
    //   376: aload_3
    //   377: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   380: pop
    //   381: aload #7
    //   383: aload_0
    //   384: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   387: pop
    //   388: ldc_w ': using '
    //   391: astore_0
    //   392: aload #7
    //   394: aload_0
    //   395: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   398: pop
    //   399: aload #7
    //   401: aload #5
    //   403: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   406: pop
    //   407: aload #7
    //   409: invokevirtual toString : ()Ljava/lang/String;
    //   412: astore_0
    //   413: aload_0
    //   414: invokestatic toast : (Ljava/lang/String;)V
    //   417: aload_2
    //   418: areturn
    //   419: aload_3
    //   420: checkcast mindustry/world/Block
    //   423: areturn
  }
  
  private static int run() {
    boolean bool = Vars.headless;
    if (!bool) {
      Player player = Vars.player;
      if (player != null) {
        GameState gameState = Vars.state;
        if (gameState != null) {
          gameState = Vars.state;
          bool = gameState.isGame();
          if (bool)
            try {
              return generateInner();
            } finally {
              gameState = null;
              boolean bool1 = errorLogged;
            }  
        } 
      } 
    } 
    return 0;
  }
  
  private static boolean sameOre(Tile paramTile, Item paramItem) {
    Item item = oreAt(paramTile);
    return (item != null && item == paramItem);
  }
  
  private static int[] scanGrid(boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Item paramItem, boolean[] paramArrayOfboolean1, boolean[] paramArrayOfboolean2, boolean[] paramArrayOfboolean3, int paramInt9, int paramInt10, Block paramBlock, Seq paramSeq, IntSeq paramIntSeq1, IntSeq paramIntSeq2) {
    int k;
    if (paramBoolean1) {
      i = paramInt4 + paramInt1;
      j = paramInt5 + paramInt2;
      k = paramInt6;
      int i1 = paramInt7;
      int i2 = paramInt9;
    } else {
      i = paramInt5 + paramInt2;
      j = paramInt4 + paramInt1;
      int i1 = paramInt6;
      k = paramInt7;
      int i2 = paramInt10;
    } 
    int m = j;
    int n = i;
    int i = 0;
    int j = 0;
    boolean bool = false;
    while (true) {
      int i1 = n + paramInt3;
      int i2 = i1 + -1;
      if (i2 <= k) {
        Object object;
        int i3 = i;
        int i4 = j;
        i = m;
        boolean bool1 = false;
        while (true) {
          int i5 = i + paramInt3;
          j = i5 + -1;
          i = i3;
          j = i4;
          object = SYNTHETIC_LOCAL_VARIABLE_35;
        } 
        continue;
      } 
      return new int[] { i, j };
    } 
  }
  
  private static void toast(String paramString) {
    UI uI = Vars.ui;
    if (uI != null) {
      uI = Vars.ui;
      float f = 3.5F;
      uI.showInfoToast(paramString, f);
    } 
  }
  
  private static boolean touchesDrill(int paramInt1, int paramInt2, boolean[] paramArrayOfboolean, int paramInt3, int paramInt4) {
    int i = 0;
    while (true) {
      int j = 4;
      if (i < j) {
        int[] arrayOfInt1 = DX;
        j = arrayOfInt1[i] + paramInt1;
        int[] arrayOfInt2 = DY;
        int k = arrayOfInt2[i] + paramInt2;
        Tiles tiles = Vars.world.tiles;
        boolean bool = tiles.in(j, k);
        if (bool) {
          k = k * paramInt3 + j;
          boolean bool1 = paramArrayOfboolean[k];
          if (bool1)
            return true; 
        } 
        i++;
        continue;
      } 
      return false;
    } 
  }
}
