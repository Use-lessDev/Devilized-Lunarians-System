package lunarians;

import arc.math.geom.Point2;
import arc.struct.IntSeq;
import arc.struct.Seq;
import arc.util.Log;
import java.util.Arrays;
import java.util.PriorityQueue;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.blocks.distribution.ItemBridge;
import mindustry.world.blocks.storage.StorageBlock;

public class AutoConnectConveyor {
  private static final int[] DX = new int[] { 1, 0, -1, 0 };
  
  private static final int[] DY = new int[] { 0, 1, 0, -1 };
  
  private static boolean errorLogged;
  
  public static int lastJunctions;
  
  public static int lastBridges;
  
  private static final int LONG_ROUTE = 40;
  
  private static byte[] freeCache;
  
  public static void init() {}
  
  public static void reset() {}
  
  public static int connect() {
    if (Vars.headless || Vars.player == null || Vars.state == null || !Vars.state.isGame())
      return 0; 
    try {
      return connectInner();
    } catch (Throwable throwable) {
      if (!errorLogged) {
        errorLogged = true;
        Log.err("[Lunarians] AutoConnectConveyor error", throwable);
      } 
      return 0;
    } 
  }
  
  private static Block resolveConveyor() {
    String str = LunariansSettings.conveyorBlock;
    if (str == null || str.isEmpty())
      str = "conveyor"; 
    Block block;
    if (block = Vars.content.block(str) instanceof mindustry.world.blocks.distribution.Conveyor)
      return block; 
    return Blocks.conveyor;
  }
  
  private static Block resolveJunction() {
    if (Blocks.junction != null)
      return Blocks.junction; 
    return Vars.content.block("junction");
  }
  
  public static boolean isCoreNetworkStorage(Building paramBuilding) {
    if (paramBuilding == null || !paramBuilding.isValid())
      return false; 
    if (paramBuilding.team != Vars.player.team())
      return false; 
    if (paramBuilding.block instanceof mindustry.world.blocks.storage.CoreBlock)
      return true; 
    if (paramBuilding instanceof StorageBlock.StorageBuild) {
      try {
        if (((StorageBlock.StorageBuild)paramBuilding).linkedCore != null)
          return true; 
      } catch (Throwable throwable) {}
      for (byte b = 0; b < 4; ) {
        Tile tile = paramBuilding.tile.nearby(b);
        if (tile == null || tile.build == null || !(tile.build.block instanceof mindustry.world.blocks.storage.CoreBlock) || tile.build.team != Vars.player.team()) {
          b++;
          continue;
        } 
        return true;
      } 
    } 
    return false;
  }
  
  public static void markPlan(BuildPlan paramBuildPlan, boolean[] paramArrayOfboolean) {
    if (paramBuildPlan == null || paramBuildPlan.block == null || paramBuildPlan.breaking)
      return; 
    int i = Vars.world.width();
    int j = paramBuildPlan.block.size;
    int k = (j - 1) / 2;
    for (byte b = 0; b < j; b++) {
      for (byte b1 = 0; b1 < j; b1++) {
        int m = paramBuildPlan.x - k + b1;
        int n = paramBuildPlan.y - k + b;
        if (Vars.world.tiles.in(m, n))
          paramArrayOfboolean[n * i + m] = true; 
      } 
    } 
  }
  
  public static boolean[] reservedFromPlans(Unit paramUnit) {
    boolean[] arrayOfBoolean = new boolean[Vars.world.width() * Vars.world.height()];
    if (paramUnit != null)
      for (BuildPlan buildPlan : paramUnit.plans())
        markPlan(buildPlan, arrayOfBoolean);  
    return arrayOfBoolean;
  }
  
  private static boolean fedByNeighbor(int paramInt1, int paramInt2) {
    for (byte b = 0; b < 4; b++) {
      Tile tile = Vars.world.tile(paramInt1 + DX[b], paramInt2 + DY[b]);
      if (tile == null || tile.build == null)
        continue; 
      Block block = tile.build.block;
      if (block instanceof mindustry.world.blocks.distribution.Conveyor) {
        int i = tile.build.rotation;
        if (tile.x + DX[i & 0x3] != paramInt1 || tile.y + DY[i & 0x3] != paramInt2)
          continue; 
      } else if (block == null || block.name == null || (!block.name.contains("router") && !block.name.contains("overflow") && !block.name.contains("underflow") && !block.name.contains("sorter"))) {
        continue;
      } 
      return true;
    } 
    return false;
  }
  
  public static boolean tileFree(int paramInt1, int paramInt2, boolean[] paramArrayOfboolean) {
    if (!Vars.world.tiles.in(paramInt1, paramInt2))
      return false; 
    int i = Vars.world.width();
    if (paramArrayOfboolean != null && paramArrayOfboolean[paramInt2 * i + paramInt1])
      return false; 
    int j = paramInt2 * i + paramInt1;
    if (freeCache != null && freeCache[j] != 0)
      return (freeCache[j] == 1); 
    Tile tile = Vars.world.tile(paramInt1, paramInt2);
    boolean bool = false;
    if (tile != null && tile.build == null) {
      bool = (Build.validPlace(resolveConveyor(), Vars.player.team(), paramInt1, paramInt2, 0) && !fedByNeighbor(paramInt1, paramInt2) && (!AutoDrill.limitOn || !AutoDrill.nextToDrill(paramInt1, paramInt2))) ? true : false;
      boolean bool1 = bool;
    } 
    if (freeCache != null)
      freeCache[j] = (byte)(bool ? 1 : 2); 
    return bool;
  }
  
  private static boolean crossable(int paramInt1, int paramInt2, int paramInt3, boolean[] paramArrayOfboolean) {
    int i = Vars.world.width();
    if (paramArrayOfboolean != null && paramArrayOfboolean[paramInt2 * i + paramInt1])
      return false; 
    Tile tile1 = Vars.world.tile(paramInt1, paramInt2);
    if (tile1 == null || tile1.build == null || tile1.build.team != Vars.player.team())
      return false; 
    Block block = tile1.build.block;
    if (block instanceof mindustry.world.blocks.distribution.Junction)
      return true; 
    if (!(block instanceof mindustry.world.blocks.distribution.Conveyor))
      return false; 
    int j = tile1.build.rotation & 0x3;
    if ((j & 0x1) == (paramInt3 & 0x1))
      return false; 
    Tile tile2 = Vars.world.tile(paramInt1 + DX[j], paramInt2 + DY[j]);
    if (tile2 == null || tile2.build == null)
      return false; 
    for (byte b = 1; b <= 3; ) {
      int m = j + b & 0x3;
      Tile tile = Vars.world.tile(paramInt1 + DX[m], paramInt2 + DY[m]);
      int k;
      if (tile == null || tile.build == null || !(tile.build.block instanceof mindustry.world.blocks.distribution.Conveyor) || tile.x + DX[k = tile.build.rotation & 0x3] != paramInt1 || tile.y + DY[k] != paramInt2) {
        b += 2;
        continue;
      } 
      return false;
    } 
    return true;
  }
  
  public static Seq<BuildPlan> planRoute(int paramInt1, int paramInt2, int paramInt3, boolean[] paramArrayOfboolean) {
    lastJunctions = 0;
    Block block1 = resolveConveyor();
    Block block2 = resolveJunction();
    if (block1 == null || block2 == null || !Vars.world.tiles.in(paramInt1, paramInt2))
      return null; 
    int i = Vars.world.width();
    int j = Vars.world.height();
    freeCache = new byte[i * j];
    try {
      lastBridges = 0;
      Seq<BuildPlan> seq1 = planRouteInner(paramInt1, paramInt2, paramInt3, paramArrayOfboolean, block1, block2, i, j, false);
      int k = lastJunctions;
      if ((seq1 != null && seq1.size <= 40) || Blocks.itemBridge == null)
        return seq1; 
      lastJunctions = 0;
      lastBridges = 0;
      Seq<BuildPlan> seq2 = planRouteInner(paramInt1, paramInt2, paramInt3, paramArrayOfboolean, block1, block2, i, j, true);
      if (seq2 != null && (seq1 == null || seq2.size < seq1.size))
        return seq2; 
      lastJunctions = k;
      lastBridges = 0;
      return seq1;
    } finally {
      freeCache = null;
    } 
  }
  
  private static Seq<BuildPlan> planRouteInner(int paramInt1, int paramInt2, int paramInt3, boolean[] paramArrayOfboolean, Block paramBlock1, Block paramBlock2, int paramInt4, int paramInt5, boolean paramBoolean) {
    if (!tileFree(paramInt1, paramInt2, paramArrayOfboolean))
      return null; 
    int k = paramInt4 * paramInt5 * 4;
    int[] arrayOfInt1 = new int[k];
    int[] arrayOfInt2 = new int[k];
    Arrays.fill(arrayOfInt1, 2147483647);
    Arrays.fill(arrayOfInt2, -1);
    byte[] arrayOfByte = new byte[k];
    byte b2 = paramBoolean ? ((ItemBridge)Blocks.itemBridge).range : 0;
    PriorityQueue<Long> priorityQueue = new PriorityQueue();
    int j;
    for (j = 0; j < 4; j++) {
      if (paramInt3 < 0 || j == paramInt3) {
        int n = (paramInt2 * paramInt4 + paramInt1) * 4 + j;
        arrayOfInt1[n] = 0;
        priorityQueue.add(Long.valueOf(n));
      } 
    } 
    j = Integer.MAX_VALUE;
    int i = -1;
    byte b3 = 0;
    while (!priorityQueue.isEmpty()) {
      long l = ((Long)priorityQueue.poll()).longValue();
      int i5 = (int)(l >>> 32L);
      int i4;
      if (i5 > arrayOfInt1[i4 = (int)(l & 0xFFFFFFFFL)])
        continue; 
      if (i5 >= j)
        break; 
      int i3 = i4 & 0x3;
      int i2 = i4 >> 2;
      int i1 = i2 % paramInt4;
      int n = i2 / paramInt4;
      Tile tile = Vars.world.tile(i1, n);
      boolean bool = (tile != null && tile.build != null) ? true : false;
      byte b;
      for (b = 0; b < 4; b++) {
        int i6, i7, i8;
        if ((bool && b != i3) || (!bool && b == (i3 + 2 & 0x3)) || !Vars.world.tiles.in(i8 = i1 + DX[b], i7 = n + DY[b]))
          continue; 
        byte b4 = (b != i3) ? 1 : 0;
        Tile tile1 = Vars.world.tile(i8, i7);
        if (tile1 == null)
          continue; 
        if (!bool && tile1.build != null && isCoreNetworkStorage(tile1.build)) {
          i6 = i5 + b4;
          if (i6 < j) {
            j = i6;
            i = i4;
            b3 = b;
          } 
          continue;
        } 
        if (tileFree(i8, i7, paramArrayOfboolean)) {
          i6 = i5 + 1 + b4;
        } else {
          if (!crossable(i8, i7, b, paramArrayOfboolean))
            continue; 
          i6 = i5 + 6 + b4;
        } 
        int i9 = (i7 * paramInt4 + i8) * 4 + b;
        if (i6 < arrayOfInt1[i9]) {
          arrayOfInt1[i9] = i6;
          arrayOfInt2[i9] = i4;
          arrayOfByte[i9] = 0;
          priorityQueue.add(Long.valueOf(i6 << 32L | i9));
        } 
        continue;
      } 
      if (paramBoolean && !bool)
        for (b = 0; b < 4; b++) {
          if (b != (i3 + 2 & 0x3))
            for (byte b4 = 2; b4 <= b2; b4++) {
              int i6 = i1 + DX[b] * b4;
              int i7 = n + DY[b] * b4;
              if (Vars.world.tiles.in(i6, i7) && tileFree(i6, i7, paramArrayOfboolean)) {
                int i8 = i5 + 8 + b4 + ((b != i3) ? 1 : 0);
                int i9 = (i7 * paramInt4 + i6) * 4 + b;
                if (i8 < arrayOfInt1[i9]) {
                  arrayOfInt1[i9] = i8;
                  arrayOfInt2[i9] = i4;
                  arrayOfByte[i9] = (byte)b4;
                  priorityQueue.add(Long.valueOf(i8 << 32L | i9));
                } 
              } 
            }  
        }  
    } 
    if (i < 0)
      return null; 
    IntSeq intSeq = new IntSeq();
    int m = i;
    while (m != -1) {
      intSeq.add(m);
      if (intSeq.size > k)
        return null; 
      m = arrayOfInt2[m];
    } 
    m = intSeq.size;
    Seq<BuildPlan> seq = new Seq();
    for (byte b1 = 0; b1 < m; b1++) {
      int i3 = intSeq.get(m - 1 - b1);
      int i2 = i3 >> 2;
      int i1 = i2 % paramInt4;
      int n = i2 / paramInt4;
      boolean bool1 = (b1 + 1 < m) ? (intSeq.get(m - 2 - b1) & 0x3) : b3;
      Tile tile = Vars.world.tile(i1, n);
      byte b = (b1 + 1 < m) ? arrayOfByte[intSeq.get(m - 2 - b1)] : 0;
      boolean bool2 = (arrayOfByte[i3] > 0) ? true : false;
      if (tile.build == null && (!b || bool2)) {
        BuildPlan buildPlan = new BuildPlan(i1, n, 0, Blocks.itemBridge);
        if (b) {
          int i4 = bool1 & 0x3;
          buildPlan.config = new Point2(DX[i4] * b, DY[i4] * b);
        } 
        seq.add(buildPlan);
        if (b > 0)
          lastBridges++; 
      } else if (tile.build != null) {
        if (!(tile.build.block instanceof mindustry.world.blocks.distribution.Junction)) {
          seq.add(new BuildPlan(i1, n, 0, paramBlock2));
          lastJunctions++;
        } 
      } else {
        seq.add(new BuildPlan(i1, n, bool1, paramBlock1));
      } 
    } 
    return seq;
  }
  
  public static Seq<BuildPlan> sanitize(Seq<BuildPlan> paramSeq) {
    Seq<BuildPlan> seq = new Seq();
    Block block = resolveJunction();
    for (byte b = 0; b < paramSeq.size; b++) {
      BuildPlan buildPlan = (BuildPlan)paramSeq.get(b);
      Tile tile;
      if (buildPlan.block instanceof mindustry.world.blocks.distribution.Conveyor && !buildPlan.breaking && (tile = Vars.world.tile(buildPlan.x, buildPlan.y)) != null && tile.build != null && tile.build.block != null) {
        Block block1 = tile.build.block;
        if (block1 instanceof mindustry.world.blocks.distribution.Conveyor && block != null && (tile.build.rotation & 0x1) != (buildPlan.rotation & 0x1))
          seq.add(new BuildPlan(buildPlan.x, buildPlan.y, 0, block)); 
      } else {
        seq.add(buildPlan);
      } 
    } 
    return seq;
  }
  
  private static int connectInner() {
    int i = LunariansSettings.conveyorStartX;
    int j = LunariansSettings.conveyorStartY;
    if (i == Integer.MIN_VALUE || j == Integer.MIN_VALUE) {
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
    boolean[] arrayOfBoolean = reservedFromPlans(unit);
    if (!tileFree(i, j, arrayOfBoolean)) {
      toast(LunariansI18n.get("cv_start_blocked"));
      return 0;
    } 
    Seq<BuildPlan> seq = planRoute(i, j, -1, arrayOfBoolean);
    if (seq == null || seq.isEmpty()) {
      toast(LunariansI18n.get("cv_no_path"));
      return 0;
    } 
    for (byte b = 0; b < seq.size; b++)
      unit.addBuild((BuildPlan)seq.get(b), true); 
    toast(LunariansI18n.get("cv_planned", String.valueOf(seq.size), String.valueOf(lastJunctions)));
    return seq.size;
  }
  
  private static void toast(String paramString) {
    if (Vars.ui != null)
      Vars.ui.showInfoToast(paramString, 3.0F); 
  }
}
