package lunarians;

import arc.struct.Seq;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.core.GameState;
import mindustry.core.UI;
import mindustry.core.World;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.Tiles;
import mindustry.world.blocks.storage.StorageBlock;

public class AutoConnectConveyor {
  private static final int[] DX;
  
  private static final int[] DY;
  
  private static final int LONG_ROUTE = 40;
  
  private static boolean errorLogged;
  
  private static byte[] freeCache;
  
  public static int lastBridges;
  
  public static int lastJunctions;
  
  static {
    boolean bool = true;
    byte b = -1;
    DX = new int[] { bool, 0, b, 0 };
    DY = new int[] { 0, bool, 0, b };
  }
  
  public static int connect() {
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
              return connectInner();
            } finally {
              gameState = null;
              boolean bool1 = errorLogged;
            }  
        } 
      } 
    } 
    return 0;
  }
  
  private static int connectInner() {
    int i = LunariansSettings.conveyorStartX;
    int j = LunariansSettings.conveyorStartY;
    int k = 0;
    String str = null;
    int m = -1 << -1;
    if (i != m && j != m) {
      Block block = resolveConveyor();
      if (block == null) {
        toast(LunariansI18n.get("cv_invalid"));
        return 0;
      } 
      Unit unit = Vars.player.unit();
      if (unit != null) {
        boolean bool = unit.canBuild();
        if (bool) {
          boolean[] arrayOfBoolean = reservedFromPlans(unit);
          boolean bool1 = tileFree(i, j, arrayOfBoolean);
          if (!bool1) {
            toast(LunariansI18n.get("cv_start_blocked"));
            return 0;
          } 
          byte b = -1;
          Seq seq = planRoute(i, j, b, arrayOfBoolean);
          if (seq != null) {
            boolean bool2 = seq.isEmpty();
            if (!bool2)
              while (true) {
                int n = seq.size;
                if (k < n) {
                  BuildPlan buildPlan = (BuildPlan)seq.get(k);
                  bool = true;
                  unit.addBuild(buildPlan, bool);
                  k++;
                  continue;
                } 
                String str1 = String.valueOf(seq.size);
                str = String.valueOf(lastJunctions);
                toast(LunariansI18n.get("cv_planned", str1, str));
                return seq.size;
              }  
          } 
          toast(LunariansI18n.get("cv_no_path"));
          return 0;
        } 
      } 
      toast(LunariansI18n.get("cv_need_builder"));
      return 0;
    } 
    toast(LunariansI18n.get("cv_set_start"));
    return 0;
  }
  
  private static boolean crossable(int paramInt1, int paramInt2, int paramInt3, boolean[] paramArrayOfboolean) {
    World world = Vars.world;
    int i = world.width();
    if (paramArrayOfboolean != null) {
      i = i * paramInt2 + paramInt1;
      boolean bool = paramArrayOfboolean[i];
      if (bool)
        return false; 
    } 
    Tile tile = Vars.world.tile(paramInt1, paramInt2);
    if (tile != null) {
      Building building = tile.build;
      if (building != null) {
        Team team1 = tile.build.team;
        Team team2 = Vars.player.team();
        if (team1 == team2) {
          Block block = tile.build.block;
          boolean bool2 = block instanceof mindustry.world.blocks.distribution.Junction;
          byte b = 1;
          if (bool2)
            return b; 
          boolean bool1 = block instanceof mindustry.world.blocks.distribution.Conveyor;
          if (!bool1)
            return false; 
          Building building1 = tile.build;
          int k = building1.rotation;
          int j = k & 0x3;
          k &= b;
          paramInt3 &= b;
          if (k == paramInt3)
            return false; 
          World world1 = Vars.world;
          int[] arrayOfInt1 = DX;
          k = arrayOfInt1[j] + paramInt1;
          int[] arrayOfInt2 = DY;
          int m = arrayOfInt2[j] + paramInt2;
          Tile tile1 = world1.tile(k, m);
          if (tile1 != null) {
            Building building2 = tile1.build;
            if (building2 != null) {
              paramInt3 = 1;
              while (true) {
                k = 3;
                if (paramInt3 <= k) {
                  m = j + paramInt3 & k;
                  World world2 = Vars.world;
                  int[] arrayOfInt3 = DX;
                  int n = arrayOfInt3[m] + paramInt1;
                  int[] arrayOfInt4 = DY;
                  m = arrayOfInt4[m] + paramInt2;
                  Tile tile2 = world2.tile(n, m);
                  if (tile2 != null) {
                    Building building3 = tile2.build;
                    if (building3 != null) {
                      Block block1 = tile2.build.block;
                      boolean bool = block1 instanceof mindustry.world.blocks.distribution.Conveyor;
                      if (bool) {
                        short s = tile2.x;
                        Building building4 = tile2.build;
                        n = building4.rotation & k;
                        k = arrayOfInt3[n];
                        int i1 = s + k;
                        if (i1 == paramInt1) {
                          k = tile2.y;
                          m = arrayOfInt4[n];
                          k += m;
                          if (k == paramInt2)
                            return false; 
                        } 
                      } 
                    } 
                  } 
                  paramInt3 += 2;
                  continue;
                } 
                return b;
              } 
            } 
          } 
        } 
      } 
    } 
    return false;
  }
  
  private static boolean fedByNeighbor(int paramInt1, int paramInt2) {
    for (int i = 0;; i++) {
      int j = 4;
      if (i < j) {
        World world = Vars.world;
        int[] arrayOfInt1 = DX;
        int k = arrayOfInt1[i] + paramInt1;
        int[] arrayOfInt2 = DY;
        int m = arrayOfInt2[i] + paramInt2;
        Tile tile = world.tile(k, m);
        if (tile != null) {
          Building building = tile.build;
          if (building != null) {
            Building building1;
            Block block = tile.build.block;
            boolean bool = block instanceof mindustry.world.blocks.distribution.Conveyor;
            if (bool) {
              building1 = tile.build;
              k = building1.rotation;
              short s = tile.x;
              k &= 0x3;
              int i1 = arrayOfInt1[k];
              int n = s + i1;
              if (n == paramInt1) {
                j = tile.y;
                i1 = arrayOfInt2[k];
                j += i1;
                if (j != paramInt2)
                  continue; 
                return true;
              } 
              continue;
            } 
            if (building1 != null) {
              String str = ((Block)building1).name;
              if (str != null) {
                str = ((Block)building1).name;
                String str1 = "router";
                boolean bool1 = str.contains(str1);
                if (!bool1) {
                  str = ((Block)building1).name;
                  str1 = "overflow";
                  bool1 = str.contains(str1);
                  if (!bool1) {
                    str = ((Block)building1).name;
                    str1 = "underflow";
                    bool1 = str.contains(str1);
                    if (!bool1) {
                      str = ((Block)building1).name;
                      str1 = "sorter";
                      bool1 = str.contains(str1);
                      if (bool1)
                        return true; 
                    } else {
                      return true;
                    } 
                  } else {
                    return true;
                  } 
                } else {
                  return true;
                } 
              } 
            } 
          } 
        } 
        continue;
      } 
      return false;
    } 
  }
  
  public static void init() {}
  
  public static boolean isCoreNetworkStorage(Building paramBuilding) {
    if (paramBuilding != null) {
      boolean bool = paramBuilding.isValid();
      if (bool) {
        Team team1 = paramBuilding.team;
        Team team2 = Vars.player.team();
        if (team1 != team2)
          return false; 
        Block block = paramBuilding.block;
        bool = block instanceof mindustry.world.blocks.storage.CoreBlock;
        boolean bool1 = true;
        if (bool)
          return bool1; 
        bool = paramBuilding instanceof StorageBlock.StorageBuild;
        if (bool) {
          Building building = paramBuilding;
          try {
            StorageBlock.StorageBuild storageBuild = (StorageBlock.StorageBuild)paramBuilding;
            Building building1 = storageBuild.linkedCore;
            if (building1 != null)
              return bool1; 
          } finally {}
          bool = false;
          building = null;
          while (true) {
            byte b = 4;
            if (bool < b) {
              Tile tile = paramBuilding.tile.nearby(bool);
              if (tile != null) {
                Building building1 = tile.build;
                if (building1 != null) {
                  Block block1 = tile.build.block;
                  boolean bool2 = block1 instanceof mindustry.world.blocks.storage.CoreBlock;
                  if (bool2) {
                    Team team3 = tile.build.team;
                    Team team4 = Vars.player.team();
                    if (team3 == team4)
                      return bool1; 
                  } 
                } 
              } 
              int i = bool + 1;
              continue;
            } 
            break;
          } 
        } 
      } 
    } 
    return false;
  }
  
  public static void markPlan(BuildPlan paramBuildPlan, boolean[] paramArrayOfboolean) {
    if (paramBuildPlan != null) {
      Block block = paramBuildPlan.block;
      if (block != null) {
        boolean bool = paramBuildPlan.breaking;
        if (!bool) {
          World world = Vars.world;
          int i = world.width();
          Block block1 = paramBuildPlan.block;
          int j = block1.size;
          int k = (j + -1) / 2;
          int m;
          for (m = 0; m < j; m++) {
            int n;
            for (n = 0; n < j; n++) {
              int i1 = paramBuildPlan.x - k + n;
              int i2 = paramBuildPlan.y - k + m;
              Tiles tiles = Vars.world.tiles;
              boolean bool1 = tiles.in(i1, i2);
              if (bool1) {
                i2 = i2 * i + i1;
                i1 = 1;
                paramArrayOfboolean[i2] = i1;
              } 
            } 
          } 
        } 
      } 
    } 
  }
  
  public static Seq planRoute(int paramInt1, int paramInt2, int paramInt3, boolean[] paramArrayOfboolean) {
    // Byte code:
    //   0: aconst_null
    //   1: astore #4
    //   3: iconst_0
    //   4: putstatic lunarians/AutoConnectConveyor.lastJunctions : I
    //   7: invokestatic resolveConveyor : ()Lmindustry/world/Block;
    //   10: astore #5
    //   12: invokestatic resolveJunction : ()Lmindustry/world/Block;
    //   15: astore #6
    //   17: aload #5
    //   19: ifnull -> 255
    //   22: aload #6
    //   24: ifnull -> 255
    //   27: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   30: getfield tiles : Lmindustry/world/Tiles;
    //   33: astore #7
    //   35: aload #7
    //   37: iload_0
    //   38: iload_1
    //   39: invokevirtual in : (II)Z
    //   42: istore #8
    //   44: iload #8
    //   46: ifeq -> 255
    //   49: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   52: invokevirtual width : ()I
    //   55: istore #9
    //   57: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   60: invokevirtual height : ()I
    //   63: istore #10
    //   65: iload #9
    //   67: iload #10
    //   69: imul
    //   70: istore #8
    //   72: iload #8
    //   74: newarray byte
    //   76: astore #7
    //   78: aload #7
    //   80: putstatic lunarians/AutoConnectConveyor.freeCache : [B
    //   83: iconst_0
    //   84: putstatic lunarians/AutoConnectConveyor.lastBridges : I
    //   87: iconst_0
    //   88: istore #11
    //   90: iload_0
    //   91: istore #8
    //   93: iload_1
    //   94: istore #12
    //   96: iload_2
    //   97: istore #13
    //   99: aload_3
    //   100: astore #14
    //   102: iload_0
    //   103: iload_1
    //   104: iload_2
    //   105: aload_3
    //   106: aload #5
    //   108: aload #6
    //   110: iload #9
    //   112: iload #10
    //   114: iconst_0
    //   115: invokestatic planRouteInner : (III[ZLmindustry/world/Block;Lmindustry/world/Block;IIZ)Larc/struct/Seq;
    //   118: astore #15
    //   120: getstatic lunarians/AutoConnectConveyor.lastJunctions : I
    //   123: istore_1
    //   124: aload #15
    //   126: ifnull -> 145
    //   129: aload #15
    //   131: getfield size : I
    //   134: istore_2
    //   135: bipush #40
    //   137: istore #16
    //   139: iload_2
    //   140: iload #16
    //   142: if_icmple -> 235
    //   145: getstatic mindustry/content/Blocks.itemBridge : Lmindustry/world/Block;
    //   148: astore #17
    //   150: aload #17
    //   152: ifnull -> 235
    //   155: iconst_0
    //   156: putstatic lunarians/AutoConnectConveyor.lastJunctions : I
    //   159: iconst_0
    //   160: putstatic lunarians/AutoConnectConveyor.lastBridges : I
    //   163: iconst_1
    //   164: istore #11
    //   166: iload #8
    //   168: iload #12
    //   170: iload #13
    //   172: aload #14
    //   174: aload #5
    //   176: aload #6
    //   178: iload #9
    //   180: iload #10
    //   182: iload #11
    //   184: invokestatic planRouteInner : (III[ZLmindustry/world/Block;Lmindustry/world/Block;IIZ)Larc/struct/Seq;
    //   187: astore #17
    //   189: aload #17
    //   191: ifnull -> 227
    //   194: aload #15
    //   196: ifnull -> 220
    //   199: aload #17
    //   201: getfield size : I
    //   204: istore #16
    //   206: aload #15
    //   208: getfield size : I
    //   211: istore #8
    //   213: iload #16
    //   215: iload #8
    //   217: if_icmpge -> 227
    //   220: aconst_null
    //   221: putstatic lunarians/AutoConnectConveyor.freeCache : [B
    //   224: aload #17
    //   226: areturn
    //   227: iload_1
    //   228: putstatic lunarians/AutoConnectConveyor.lastJunctions : I
    //   231: iconst_0
    //   232: putstatic lunarians/AutoConnectConveyor.lastBridges : I
    //   235: aconst_null
    //   236: putstatic lunarians/AutoConnectConveyor.freeCache : [B
    //   239: aload #15
    //   241: areturn
    //   242: astore #4
    //   244: aload #4
    //   246: astore #15
    //   248: aconst_null
    //   249: putstatic lunarians/AutoConnectConveyor.freeCache : [B
    //   252: aload #4
    //   254: athrow
    //   255: aconst_null
    //   256: areturn
    // Exception table:
    //   from	to	target	type
    //   83	87	242	finally
    //   114	118	242	finally
    //   120	123	242	finally
    //   129	134	242	finally
    //   145	148	242	finally
    //   155	159	242	finally
    //   159	163	242	finally
    //   182	187	242	finally
    //   199	204	242	finally
    //   206	211	242	finally
    //   227	231	242	finally
    //   231	235	242	finally
  }
  
  private static Seq planRouteInner(int paramInt1, int paramInt2, int paramInt3, boolean[] paramArrayOfboolean, Block paramBlock1, Block paramBlock2, int paramInt4, int paramInt5, boolean paramBoolean) {
    // Byte code:
    //   0: iload_0
    //   1: istore #9
    //   3: iload_1
    //   4: istore #10
    //   6: iload_2
    //   7: istore #11
    //   9: aload_3
    //   10: astore #12
    //   12: iload_0
    //   13: iload_1
    //   14: aload_3
    //   15: invokestatic tileFree : (II[Z)Z
    //   18: istore #13
    //   20: iconst_0
    //   21: istore #14
    //   23: aconst_null
    //   24: astore #15
    //   26: iload #13
    //   28: ifne -> 33
    //   31: aconst_null
    //   32: areturn
    //   33: iload #6
    //   35: iload #7
    //   37: imul
    //   38: istore #13
    //   40: iconst_2
    //   41: istore #16
    //   43: iload #13
    //   45: iload #16
    //   47: ishl
    //   48: istore #13
    //   50: iload #13
    //   52: newarray int
    //   54: astore #17
    //   56: iload #13
    //   58: newarray int
    //   60: astore #18
    //   62: iconst_m1
    //   63: iconst_1
    //   64: iushr
    //   65: istore #19
    //   67: aload #17
    //   69: iload #19
    //   71: invokestatic fill : ([II)V
    //   74: iconst_m1
    //   75: istore #20
    //   77: aload #18
    //   79: iload #20
    //   81: invokestatic fill : ([II)V
    //   84: iload #13
    //   86: newarray byte
    //   88: astore #21
    //   90: iconst_0
    //   91: istore #22
    //   93: aconst_null
    //   94: astore #23
    //   96: iload #8
    //   98: ifeq -> 119
    //   101: getstatic mindustry/content/Blocks.itemBridge : Lmindustry/world/Block;
    //   104: checkcast mindustry/world/blocks/distribution/ItemBridge
    //   107: astore #24
    //   109: aload #24
    //   111: getfield range : I
    //   114: istore #25
    //   116: goto -> 125
    //   119: iconst_0
    //   120: istore #25
    //   122: aconst_null
    //   123: astore #24
    //   125: new java/util/PriorityQueue
    //   128: astore #26
    //   130: aload #26
    //   132: invokespecial <init> : ()V
    //   135: iconst_0
    //   136: istore #27
    //   138: aconst_null
    //   139: astore #28
    //   141: iconst_4
    //   142: istore #14
    //   144: iload #27
    //   146: iload #14
    //   148: if_icmpge -> 232
    //   151: iload #11
    //   153: iflt -> 170
    //   156: iload #27
    //   158: iload #11
    //   160: if_icmpeq -> 170
    //   163: aload #17
    //   165: astore #29
    //   167: goto -> 216
    //   170: iload #10
    //   172: iload #6
    //   174: imul
    //   175: iload #9
    //   177: iadd
    //   178: iload #16
    //   180: ishl
    //   181: iload #27
    //   183: iadd
    //   184: istore #14
    //   186: aload #17
    //   188: iload #14
    //   190: iconst_0
    //   191: iastore
    //   192: aload #17
    //   194: astore #29
    //   196: iload #14
    //   198: i2l
    //   199: lstore #30
    //   201: lload #30
    //   203: invokestatic valueOf : (J)Ljava/lang/Long;
    //   206: astore #15
    //   208: aload #26
    //   210: aload #15
    //   212: invokevirtual add : (Ljava/lang/Object;)Z
    //   215: pop
    //   216: iload #27
    //   218: iconst_1
    //   219: iadd
    //   220: istore #27
    //   222: aload #29
    //   224: astore #17
    //   226: iconst_2
    //   227: istore #16
    //   229: goto -> 141
    //   232: aload #17
    //   234: astore #29
    //   236: iconst_2
    //   237: istore #7
    //   239: iconst_m1
    //   240: istore #9
    //   242: iconst_0
    //   243: istore #10
    //   245: aconst_null
    //   246: astore #32
    //   248: aload #26
    //   250: invokevirtual isEmpty : ()Z
    //   253: istore #11
    //   255: iload #11
    //   257: ifne -> 1131
    //   260: aload #26
    //   262: invokevirtual poll : ()Ljava/lang/Object;
    //   265: checkcast java/lang/Long
    //   268: astore #33
    //   270: aload #33
    //   272: invokevirtual longValue : ()J
    //   275: lstore #34
    //   277: bipush #32
    //   279: istore #11
    //   281: iconst_1
    //   282: istore_0
    //   283: lload #34
    //   285: iload #11
    //   287: lushr
    //   288: lstore #30
    //   290: lload #30
    //   292: l2i
    //   293: istore #36
    //   295: ldc2_w 4294967295
    //   298: lstore #37
    //   300: aload #21
    //   302: astore #28
    //   304: lload #34
    //   306: lload #37
    //   308: land
    //   309: lstore #39
    //   311: lload #39
    //   313: l2i
    //   314: istore #41
    //   316: aload #29
    //   318: iload #41
    //   320: iaload
    //   321: istore #20
    //   323: iload #36
    //   325: iload #20
    //   327: if_icmple -> 333
    //   330: goto -> 248
    //   333: iload #36
    //   335: iload #19
    //   337: if_icmpge -> 1137
    //   340: iload #41
    //   342: iconst_3
    //   343: iand
    //   344: istore #20
    //   346: iload #41
    //   348: iconst_2
    //   349: ishr
    //   350: istore #42
    //   352: bipush #32
    //   354: istore_1
    //   355: iload #42
    //   357: iload #6
    //   359: irem
    //   360: istore #11
    //   362: iload #42
    //   364: iload #6
    //   366: idiv
    //   367: istore #16
    //   369: iconst_0
    //   370: istore #42
    //   372: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   375: iload #11
    //   377: iload #16
    //   379: invokevirtual tile : (II)Lmindustry/world/Tile;
    //   382: astore #23
    //   384: aload #23
    //   386: ifnull -> 407
    //   389: aload #23
    //   391: getfield build : Lmindustry/gen/Building;
    //   394: astore #23
    //   396: aload #23
    //   398: ifnull -> 407
    //   401: iconst_1
    //   402: istore #22
    //   404: goto -> 413
    //   407: iconst_0
    //   408: istore #22
    //   410: aconst_null
    //   411: astore #23
    //   413: iload #9
    //   415: istore #43
    //   417: iconst_0
    //   418: istore #9
    //   420: iload #9
    //   422: iload #14
    //   424: if_icmpge -> 817
    //   427: iload #22
    //   429: ifeq -> 442
    //   432: iload #9
    //   434: iload #20
    //   436: if_icmpne -> 781
    //   439: goto -> 461
    //   442: iload #20
    //   444: iconst_2
    //   445: iadd
    //   446: istore #44
    //   448: iload #44
    //   450: iconst_3
    //   451: iand
    //   452: istore #14
    //   454: iload #9
    //   456: iload #14
    //   458: if_icmpeq -> 781
    //   461: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   464: getfield tiles : Lmindustry/world/Tiles;
    //   467: astore #15
    //   469: getstatic lunarians/AutoConnectConveyor.DX : [I
    //   472: iload #9
    //   474: iaload
    //   475: istore #44
    //   477: iload #10
    //   479: istore #45
    //   481: iload #11
    //   483: iload #44
    //   485: iadd
    //   486: istore #10
    //   488: getstatic lunarians/AutoConnectConveyor.DY : [I
    //   491: astore #46
    //   493: aload #46
    //   495: iload #9
    //   497: iaload
    //   498: istore #44
    //   500: iload #11
    //   502: istore #47
    //   504: iload #16
    //   506: iload #44
    //   508: iadd
    //   509: istore #11
    //   511: aload #15
    //   513: iload #10
    //   515: iload #11
    //   517: invokevirtual in : (II)Z
    //   520: istore #14
    //   522: iload #14
    //   524: ifeq -> 789
    //   527: iload #9
    //   529: iload #20
    //   531: if_icmpeq -> 540
    //   534: iconst_1
    //   535: istore #44
    //   537: goto -> 546
    //   540: iconst_0
    //   541: istore #44
    //   543: aconst_null
    //   544: astore #46
    //   546: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   549: iload #10
    //   551: iload #11
    //   553: invokevirtual tile : (II)Lmindustry/world/Tile;
    //   556: astore #15
    //   558: aload #15
    //   560: ifnonnull -> 566
    //   563: goto -> 789
    //   566: iload #16
    //   568: istore #48
    //   570: iload #22
    //   572: ifne -> 635
    //   575: aload #15
    //   577: getfield build : Lmindustry/gen/Building;
    //   580: astore #49
    //   582: aload #49
    //   584: ifnull -> 635
    //   587: aload #15
    //   589: getfield build : Lmindustry/gen/Building;
    //   592: astore #15
    //   594: aload #15
    //   596: invokestatic isCoreNetworkStorage : (Lmindustry/gen/Building;)Z
    //   599: istore #14
    //   601: iload #14
    //   603: ifeq -> 635
    //   606: iload #36
    //   608: iload #44
    //   610: iadd
    //   611: istore #10
    //   613: iload #10
    //   615: iload #19
    //   617: if_icmpge -> 793
    //   620: iload #10
    //   622: istore #19
    //   624: iload #41
    //   626: istore #43
    //   628: iload #9
    //   630: istore #10
    //   632: goto -> 797
    //   635: iload #10
    //   637: iload #11
    //   639: aload #12
    //   641: invokestatic tileFree : (II[Z)Z
    //   644: istore #14
    //   646: iload #14
    //   648: ifeq -> 667
    //   651: iload #36
    //   653: iconst_1
    //   654: iadd
    //   655: istore #14
    //   657: iload #14
    //   659: iload #44
    //   661: iadd
    //   662: istore #14
    //   664: goto -> 695
    //   667: iload #10
    //   669: iload #11
    //   671: iload #9
    //   673: aload #12
    //   675: invokestatic crossable : (III[Z)Z
    //   678: istore #14
    //   680: iload #14
    //   682: ifeq -> 793
    //   685: iload #36
    //   687: bipush #6
    //   689: iadd
    //   690: istore #14
    //   692: goto -> 657
    //   695: iload #11
    //   697: iload #6
    //   699: imul
    //   700: iload #10
    //   702: iadd
    //   703: iconst_2
    //   704: ishl
    //   705: iload #9
    //   707: iadd
    //   708: istore #11
    //   710: aload #29
    //   712: iload #11
    //   714: iaload
    //   715: istore #10
    //   717: iload #14
    //   719: iload #10
    //   721: if_icmpge -> 793
    //   724: aload #29
    //   726: iload #11
    //   728: iload #14
    //   730: iastore
    //   731: aload #18
    //   733: iload #11
    //   735: iload #41
    //   737: iastore
    //   738: aload #28
    //   740: iload #11
    //   742: iconst_0
    //   743: bastore
    //   744: iload #14
    //   746: i2l
    //   747: iload_1
    //   748: lshl
    //   749: lstore #50
    //   751: iload #11
    //   753: i2l
    //   754: lstore #52
    //   756: lload #50
    //   758: lload #52
    //   760: lor
    //   761: lstore #50
    //   763: lload #50
    //   765: invokestatic valueOf : (J)Ljava/lang/Long;
    //   768: astore #32
    //   770: aload #26
    //   772: aload #32
    //   774: invokevirtual add : (Ljava/lang/Object;)Z
    //   777: pop
    //   778: goto -> 793
    //   781: iload #10
    //   783: istore #45
    //   785: iload #11
    //   787: istore #47
    //   789: iload #16
    //   791: istore #48
    //   793: iload #45
    //   795: istore #10
    //   797: iload #9
    //   799: iconst_1
    //   800: iadd
    //   801: istore #9
    //   803: iload #47
    //   805: istore #11
    //   807: iload #48
    //   809: istore #16
    //   811: iconst_4
    //   812: istore #14
    //   814: goto -> 420
    //   817: iload #10
    //   819: istore #45
    //   821: iload #11
    //   823: istore #47
    //   825: iload #16
    //   827: istore #48
    //   829: iload #8
    //   831: ifeq -> 1110
    //   834: iload #22
    //   836: ifne -> 1110
    //   839: iconst_0
    //   840: istore #9
    //   842: iconst_4
    //   843: istore #11
    //   845: iload #9
    //   847: iload #11
    //   849: if_icmpge -> 1110
    //   852: iload #20
    //   854: iconst_2
    //   855: iadd
    //   856: iconst_3
    //   857: iand
    //   858: istore #10
    //   860: iload #9
    //   862: iload #10
    //   864: if_icmpeq -> 1098
    //   867: iconst_2
    //   868: istore #10
    //   870: iload #10
    //   872: iload #25
    //   874: if_icmpgt -> 1098
    //   877: getstatic lunarians/AutoConnectConveyor.DX : [I
    //   880: astore #15
    //   882: aload #15
    //   884: iload #9
    //   886: iaload
    //   887: iload #10
    //   889: imul
    //   890: istore #14
    //   892: iload #47
    //   894: iload #14
    //   896: iadd
    //   897: istore #14
    //   899: getstatic lunarians/AutoConnectConveyor.DY : [I
    //   902: astore #49
    //   904: aload #49
    //   906: iload #9
    //   908: iaload
    //   909: iload #10
    //   911: imul
    //   912: istore #16
    //   914: iload #48
    //   916: iload #16
    //   918: iadd
    //   919: istore #16
    //   921: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   924: getfield tiles : Lmindustry/world/Tiles;
    //   927: astore #23
    //   929: aload #23
    //   931: iload #14
    //   933: iload #16
    //   935: invokevirtual in : (II)Z
    //   938: istore #22
    //   940: iload #22
    //   942: ifeq -> 1086
    //   945: iload #14
    //   947: iload #16
    //   949: aload #12
    //   951: invokestatic tileFree : (II[Z)Z
    //   954: istore #22
    //   956: iload #22
    //   958: ifeq -> 1086
    //   961: iload #36
    //   963: bipush #8
    //   965: iadd
    //   966: iload #10
    //   968: iadd
    //   969: istore #22
    //   971: iload #9
    //   973: iload #20
    //   975: if_icmpeq -> 984
    //   978: iconst_1
    //   979: istore #44
    //   981: goto -> 990
    //   984: iconst_0
    //   985: istore #44
    //   987: aconst_null
    //   988: astore #46
    //   990: iload #22
    //   992: iload #44
    //   994: iadd
    //   995: istore #22
    //   997: iload #16
    //   999: iload #6
    //   1001: imul
    //   1002: iload #14
    //   1004: iadd
    //   1005: iconst_2
    //   1006: ishl
    //   1007: iload #9
    //   1009: iadd
    //   1010: istore #16
    //   1012: aload #29
    //   1014: iload #16
    //   1016: iaload
    //   1017: istore #14
    //   1019: iload #22
    //   1021: iload #14
    //   1023: if_icmpge -> 1086
    //   1026: aload #29
    //   1028: iload #16
    //   1030: iload #22
    //   1032: iastore
    //   1033: aload #18
    //   1035: iload #16
    //   1037: iload #41
    //   1039: iastore
    //   1040: iload #10
    //   1042: i2b
    //   1043: istore #14
    //   1045: aload #28
    //   1047: iload #16
    //   1049: iload #14
    //   1051: bastore
    //   1052: iload #22
    //   1054: i2l
    //   1055: iload_1
    //   1056: lshl
    //   1057: lstore #54
    //   1059: iload #16
    //   1061: i2l
    //   1062: lstore #50
    //   1064: lload #54
    //   1066: lload #50
    //   1068: lor
    //   1069: lstore #54
    //   1071: lload #54
    //   1073: invokestatic valueOf : (J)Ljava/lang/Long;
    //   1076: astore #33
    //   1078: aload #26
    //   1080: aload #33
    //   1082: invokevirtual add : (Ljava/lang/Object;)Z
    //   1085: pop
    //   1086: iload #10
    //   1088: iconst_1
    //   1089: iadd
    //   1090: istore #10
    //   1092: aload_3
    //   1093: astore #12
    //   1095: goto -> 870
    //   1098: iload #9
    //   1100: iconst_1
    //   1101: iadd
    //   1102: istore #9
    //   1104: aload_3
    //   1105: astore #12
    //   1107: goto -> 842
    //   1110: aload_3
    //   1111: astore #12
    //   1113: aload #28
    //   1115: astore #21
    //   1117: iload #43
    //   1119: istore #9
    //   1121: iload #45
    //   1123: istore #10
    //   1125: iconst_4
    //   1126: istore #14
    //   1128: goto -> 248
    //   1131: aload #21
    //   1133: astore #28
    //   1135: iconst_1
    //   1136: istore_0
    //   1137: iload #9
    //   1139: ifge -> 1144
    //   1142: aconst_null
    //   1143: areturn
    //   1144: new arc/struct/IntSeq
    //   1147: astore #33
    //   1149: aload #33
    //   1151: invokespecial <init> : ()V
    //   1154: iconst_m1
    //   1155: istore #16
    //   1157: iload #9
    //   1159: iload #16
    //   1161: if_icmpeq -> 1197
    //   1164: aload #33
    //   1166: iload #9
    //   1168: invokevirtual add : (I)V
    //   1171: aload #33
    //   1173: getfield size : I
    //   1176: istore #56
    //   1178: iload #56
    //   1180: iload #13
    //   1182: if_icmple -> 1187
    //   1185: aconst_null
    //   1186: areturn
    //   1187: aload #18
    //   1189: iload #9
    //   1191: iaload
    //   1192: istore #9
    //   1194: goto -> 1157
    //   1197: aload #33
    //   1199: getfield size : I
    //   1202: istore #9
    //   1204: new arc/struct/Seq
    //   1207: astore #12
    //   1209: aload #12
    //   1211: invokespecial <init> : ()V
    //   1214: iconst_0
    //   1215: istore #13
    //   1217: aconst_null
    //   1218: astore #57
    //   1220: iload #13
    //   1222: iload #9
    //   1224: if_icmpge -> 1645
    //   1227: iload #9
    //   1229: iconst_m1
    //   1230: iadd
    //   1231: iload #13
    //   1233: isub
    //   1234: istore #14
    //   1236: aload #33
    //   1238: iload #14
    //   1240: invokevirtual get : (I)I
    //   1243: istore #14
    //   1245: iload #14
    //   1247: iconst_2
    //   1248: ishr
    //   1249: istore #16
    //   1251: iload #16
    //   1253: iload #6
    //   1255: irem
    //   1256: istore #36
    //   1258: iload #16
    //   1260: iload #6
    //   1262: idiv
    //   1263: istore #16
    //   1265: iload #13
    //   1267: iconst_1
    //   1268: iadd
    //   1269: istore #58
    //   1271: iload #58
    //   1273: iload #9
    //   1275: if_icmpge -> 1302
    //   1278: iload #9
    //   1280: bipush #-2
    //   1282: iadd
    //   1283: iload #13
    //   1285: isub
    //   1286: istore #19
    //   1288: aload #33
    //   1290: iload #19
    //   1292: invokevirtual get : (I)I
    //   1295: iconst_3
    //   1296: iand
    //   1297: istore #19
    //   1299: goto -> 1306
    //   1302: iload #10
    //   1304: istore #19
    //   1306: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   1309: iload #36
    //   1311: iload #16
    //   1313: invokevirtual tile : (II)Lmindustry/world/Tile;
    //   1316: astore #59
    //   1318: iload #58
    //   1320: iload #9
    //   1322: if_icmpge -> 1354
    //   1325: iload #9
    //   1327: bipush #-2
    //   1329: iadd
    //   1330: iload #13
    //   1332: isub
    //   1333: istore #41
    //   1335: aload #33
    //   1337: iload #41
    //   1339: invokevirtual get : (I)I
    //   1342: istore #13
    //   1344: aload #28
    //   1346: iload #13
    //   1348: baload
    //   1349: istore #13
    //   1351: goto -> 1360
    //   1354: iconst_0
    //   1355: istore #13
    //   1357: aconst_null
    //   1358: astore #57
    //   1360: aload #28
    //   1362: iload #14
    //   1364: baload
    //   1365: istore #14
    //   1367: iload #14
    //   1369: ifle -> 1378
    //   1372: iconst_1
    //   1373: istore #14
    //   1375: goto -> 1384
    //   1378: iconst_0
    //   1379: istore #14
    //   1381: aconst_null
    //   1382: astore #15
    //   1384: aload #59
    //   1386: getfield build : Lmindustry/gen/Building;
    //   1389: astore #21
    //   1391: aload #21
    //   1393: ifnonnull -> 1524
    //   1396: iload #13
    //   1398: ifgt -> 1406
    //   1401: iload #14
    //   1403: ifeq -> 1524
    //   1406: new mindustry/entities/units/BuildPlan
    //   1409: astore #15
    //   1411: getstatic mindustry/content/Blocks.itemBridge : Lmindustry/world/Block;
    //   1414: astore #59
    //   1416: iconst_0
    //   1417: istore #41
    //   1419: aconst_null
    //   1420: astore #21
    //   1422: aload #15
    //   1424: iload #36
    //   1426: iload #16
    //   1428: iconst_0
    //   1429: aload #59
    //   1431: invokespecial <init> : (IIILmindustry/world/Block;)V
    //   1434: iload #13
    //   1436: ifle -> 1496
    //   1439: iload #19
    //   1441: iconst_3
    //   1442: iand
    //   1443: istore #19
    //   1445: new arc/math/geom/Point2
    //   1448: astore #49
    //   1450: getstatic lunarians/AutoConnectConveyor.DX : [I
    //   1453: astore #17
    //   1455: aload #17
    //   1457: iload #19
    //   1459: iaload
    //   1460: iload #13
    //   1462: imul
    //   1463: istore #36
    //   1465: getstatic lunarians/AutoConnectConveyor.DY : [I
    //   1468: astore #59
    //   1470: aload #59
    //   1472: iload #19
    //   1474: iaload
    //   1475: iload #13
    //   1477: imul
    //   1478: istore #19
    //   1480: aload #49
    //   1482: iload #36
    //   1484: iload #19
    //   1486: invokespecial <init> : (II)V
    //   1489: aload #15
    //   1491: aload #49
    //   1493: putfield config : Ljava/lang/Object;
    //   1496: aload #12
    //   1498: aload #15
    //   1500: invokevirtual add : (Ljava/lang/Object;)Larc/struct/Seq;
    //   1503: pop
    //   1504: iload #13
    //   1506: ifle -> 1638
    //   1509: getstatic lunarians/AutoConnectConveyor.lastBridges : I
    //   1512: iconst_1
    //   1513: iadd
    //   1514: istore #13
    //   1516: iload #13
    //   1518: putstatic lunarians/AutoConnectConveyor.lastBridges : I
    //   1521: goto -> 1638
    //   1524: aload #59
    //   1526: getfield build : Lmindustry/gen/Building;
    //   1529: astore #57
    //   1531: aload #57
    //   1533: ifnull -> 1608
    //   1536: aload #59
    //   1538: getfield build : Lmindustry/gen/Building;
    //   1541: getfield block : Lmindustry/world/Block;
    //   1544: astore #57
    //   1546: aload #57
    //   1548: instanceof mindustry/world/blocks/distribution/Junction
    //   1551: istore #13
    //   1553: iload #13
    //   1555: ifne -> 1638
    //   1558: new mindustry/entities/units/BuildPlan
    //   1561: astore #57
    //   1563: aload #5
    //   1565: astore #15
    //   1567: iconst_0
    //   1568: istore #41
    //   1570: aconst_null
    //   1571: astore #21
    //   1573: aload #57
    //   1575: iload #36
    //   1577: iload #16
    //   1579: iconst_0
    //   1580: aload #5
    //   1582: invokespecial <init> : (IIILmindustry/world/Block;)V
    //   1585: aload #12
    //   1587: aload #57
    //   1589: invokevirtual add : (Ljava/lang/Object;)Larc/struct/Seq;
    //   1592: pop
    //   1593: getstatic lunarians/AutoConnectConveyor.lastJunctions : I
    //   1596: iconst_1
    //   1597: iadd
    //   1598: istore #13
    //   1600: iload #13
    //   1602: putstatic lunarians/AutoConnectConveyor.lastJunctions : I
    //   1605: goto -> 1638
    //   1608: new mindustry/entities/units/BuildPlan
    //   1611: astore #57
    //   1613: aload #4
    //   1615: astore #59
    //   1617: aload #57
    //   1619: iload #36
    //   1621: iload #16
    //   1623: iload #19
    //   1625: aload #4
    //   1627: invokespecial <init> : (IIILmindustry/world/Block;)V
    //   1630: aload #12
    //   1632: aload #57
    //   1634: invokevirtual add : (Ljava/lang/Object;)Larc/struct/Seq;
    //   1637: pop
    //   1638: iload #58
    //   1640: istore #13
    //   1642: goto -> 1220
    //   1645: aload #12
    //   1647: areturn
  }
  
  public static boolean[] reservedFromPlans(Unit paramUnit) {
    int i = Vars.world.width();
    World world = Vars.world;
    int j = world.height();
    i *= j;
    boolean[] arrayOfBoolean = new boolean[i];
    if (paramUnit != null) {
      Iterator<BuildPlan> iterator = paramUnit.plans().iterator();
      while (true) {
        boolean bool = iterator.hasNext();
        if (bool) {
          BuildPlan buildPlan = iterator.next();
          markPlan(buildPlan, arrayOfBoolean);
          continue;
        } 
        break;
      } 
    } 
    return arrayOfBoolean;
  }
  
  public static void reset() {}
  
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
  
  private static Block resolveJunction() {
    Block block = Blocks.junction;
    return (block != null) ? Blocks.junction : Vars.content.block("junction");
  }
  
  public static Seq sanitize(Seq paramSeq) {
    Seq seq = new Seq();
    this();
    Block block = resolveJunction();
    for (int i = 0;; i++) {
      int j = paramSeq.size;
      if (i < j) {
        BuildPlan buildPlan = (BuildPlan)paramSeq.get(i);
        Block block1 = buildPlan.block;
        boolean bool = block1 instanceof mindustry.world.blocks.distribution.Conveyor;
        if (bool) {
          bool = buildPlan.breaking;
          if (!bool) {
            World world = Vars.world;
            int k = buildPlan.x;
            int m = buildPlan.y;
            Tile tile = world.tile(k, m);
            if (tile != null) {
              Building building = tile.build;
              if (building != null) {
                Block block2 = tile.build.block;
                if (block2 != null) {
                  block2 = tile.build.block;
                  boolean bool1 = block2 instanceof mindustry.world.blocks.distribution.Conveyor;
                  if (bool1 && block != null) {
                    Building building1 = tile.build;
                    int n = building1.rotation & 0x1;
                    int i1 = buildPlan.rotation & 0x1;
                    if (n != i1) {
                      BuildPlan buildPlan1 = new BuildPlan();
                      i1 = buildPlan.x;
                      j = buildPlan.y;
                      this(i1, j, 0, block);
                      seq.add(buildPlan1);
                    } 
                  } 
                  continue;
                } 
              } 
            } 
          } 
        } 
        seq.add(buildPlan);
        continue;
      } 
      return seq;
    } 
  }
  
  public static boolean tileFree(int paramInt1, int paramInt2, boolean[] paramArrayOfboolean) {
    // Byte code:
    //   0: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   3: getfield tiles : Lmindustry/world/Tiles;
    //   6: astore_3
    //   7: aload_3
    //   8: iload_0
    //   9: iload_1
    //   10: invokevirtual in : (II)Z
    //   13: istore #4
    //   15: iconst_0
    //   16: istore #5
    //   18: iload #4
    //   20: ifne -> 25
    //   23: iconst_0
    //   24: ireturn
    //   25: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   28: astore_3
    //   29: aload_3
    //   30: invokevirtual width : ()I
    //   33: istore #4
    //   35: aload_2
    //   36: ifnull -> 60
    //   39: iload_1
    //   40: iload #4
    //   42: imul
    //   43: iload_0
    //   44: iadd
    //   45: istore #6
    //   47: aload_2
    //   48: iload #6
    //   50: baload
    //   51: istore #7
    //   53: iload #7
    //   55: ifeq -> 60
    //   58: iconst_0
    //   59: ireturn
    //   60: iload #4
    //   62: iload_1
    //   63: imul
    //   64: iload_0
    //   65: iadd
    //   66: istore #4
    //   68: getstatic lunarians/AutoConnectConveyor.freeCache : [B
    //   71: astore_2
    //   72: iconst_1
    //   73: istore #6
    //   75: aload_2
    //   76: ifnull -> 102
    //   79: aload_2
    //   80: iload #4
    //   82: baload
    //   83: istore #7
    //   85: iload #7
    //   87: ifeq -> 102
    //   90: iload #7
    //   92: iload #6
    //   94: if_icmpne -> 100
    //   97: iload #6
    //   99: ireturn
    //   100: iconst_0
    //   101: ireturn
    //   102: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   105: iload_0
    //   106: iload_1
    //   107: invokevirtual tile : (II)Lmindustry/world/Tile;
    //   110: astore_2
    //   111: aload_2
    //   112: ifnull -> 187
    //   115: aload_2
    //   116: getfield build : Lmindustry/gen/Building;
    //   119: astore_2
    //   120: aload_2
    //   121: ifnonnull -> 187
    //   124: invokestatic resolveConveyor : ()Lmindustry/world/Block;
    //   127: astore_2
    //   128: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   131: invokevirtual team : ()Lmindustry/game/Team;
    //   134: astore #8
    //   136: aload_2
    //   137: aload #8
    //   139: iload_0
    //   140: iload_1
    //   141: iconst_0
    //   142: invokestatic validPlace : (Lmindustry/world/Block;Lmindustry/game/Team;III)Z
    //   145: istore #7
    //   147: iload #7
    //   149: ifeq -> 187
    //   152: iload_0
    //   153: iload_1
    //   154: invokestatic fedByNeighbor : (II)Z
    //   157: istore #7
    //   159: iload #7
    //   161: ifne -> 187
    //   164: getstatic lunarians/AutoDrill.limitOn : Z
    //   167: istore #7
    //   169: iload #7
    //   171: ifeq -> 184
    //   174: iload_0
    //   175: iload_1
    //   176: invokestatic nextToDrill : (II)Z
    //   179: istore_0
    //   180: iload_0
    //   181: ifne -> 187
    //   184: iconst_1
    //   185: istore #5
    //   187: getstatic lunarians/AutoConnectConveyor.freeCache : [B
    //   190: astore #9
    //   192: aload #9
    //   194: ifnull -> 215
    //   197: iload #5
    //   199: ifne -> 205
    //   202: iconst_2
    //   203: istore #6
    //   205: iload #6
    //   207: i2b
    //   208: istore_1
    //   209: aload #9
    //   211: iload #4
    //   213: iload_1
    //   214: bastore
    //   215: iload #5
    //   217: ireturn
  }
  
  private static void toast(String paramString) {
    UI uI = Vars.ui;
    if (uI != null) {
      uI = Vars.ui;
      float f = 3.0F;
      uI.showInfoToast(paramString, f);
    } 
  }
}
