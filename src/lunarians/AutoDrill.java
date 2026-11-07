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
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.production.Drill;

public class AutoDrill {
  private static final int[] DX = new int[] { 1, 0, -1, 0 };
  
  private static final int[] DY = new int[] { 0, 1, 0, -1 };
  
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
  
  static boolean inLimit(int paramInt1, int paramInt2) {
    return (!limitOn || Mathf.dst(paramInt1, paramInt2, limitX, limitY) <= limitR);
  }
  
  static boolean crowded(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    for (int i = paramInt2 - paramInt4; i <= paramInt2 + paramInt3 - 1 + paramInt4; i++) {
      for (int j = paramInt1 - paramInt4; j <= paramInt1 + paramInt3 - 1 + paramInt4; ) {
        Tile tile;
        if (!Vars.world.tiles.in(j, i) || (tile = Vars.world.tile(j, i)) == null || tile.build == null || tile.build.block == null || tile.build.block instanceof mindustry.world.blocks.storage.CoreBlock) {
          j++;
          continue;
        } 
        return true;
      } 
    } 
    return false;
  }
  
  static boolean nextToDrill(int paramInt1, int paramInt2) {
    for (byte b = 0; b < 4; ) {
      Tile tile = Vars.world.tile(paramInt1 + DX[b], paramInt2 + DY[b]);
      if (tile == null || tile.build == null || !(tile.build.block instanceof Drill)) {
        b++;
        continue;
      } 
      return true;
    } 
    return false;
  }
  
  static boolean floorOre(Tile paramTile) {
    if (paramTile == null)
      return false; 
    return (paramTile.overlay() == null || (paramTile.overlay()).itemDrop == null);
  }
  
  static boolean inFloor(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (!limitOn)
      return true; 
    if (!floorOre(Vars.world.tile(paramInt3, paramInt4)))
      return true; 
    return (Mathf.dst(paramInt1, paramInt2, paramInt3, paramInt4) <= Math.max(2.5F, (float)Math.sqrt(AutoSetConfig.floorMax()) * 0.75F + 0.5F));
  }
  
  public static void init() {}
  
  public static void reset() {
    lastDrillTiles.clear();
    lastCount = 0;
    lastHub = -1;
  }
  
  public static int count() {
    return lastCount;
  }
  
  public static int generate() {
    return run();
  }
  
  private static int[] exitOrder(int paramInt) {
    int[] arrayOfInt = new int[4];
    arrayOfInt[0] = paramInt;
    float f1 = 0.0F;
    float f2 = 0.0F;
    try {
      if (Vars.player != null && Vars.player.closestCore() != null && LunariansSettings.drillStartX != Integer.MIN_VALUE) {
        f1 = (Vars.player.closestCore().tileX() - LunariansSettings.drillStartX);
        f2 = (Vars.player.closestCore().tileY() - LunariansSettings.drillStartY);
      } 
    } catch (Throwable throwable) {}
    boolean[] arrayOfBoolean = new boolean[4];
    arrayOfBoolean[paramInt] = true;
    for (byte b = 1; b < 4; b++) {
      byte b1 = -1;
      float f = -3.4028235E38F;
      for (byte b2 = 0; b2 < 4; b2++) {
        float f3;
        if (!arrayOfBoolean[b2] && (f3 = DX[b2] * f1 + DY[b2] * f2) > f) {
          f = f3;
          b1 = b2;
        } 
      } 
      arrayOfBoolean[b1] = true;
      arrayOfInt[b] = b1;
    } 
    return arrayOfInt;
  }
  
  public static int generateWithConveyor() {
    if (Vars.headless || Vars.player == null || Vars.state == null || !Vars.state.isGame())
      return 0; 
    int i = LunariansSettings.drillExitDir & 0x3;
    try {
      Unit unit = Vars.player.unit();
      if (unit == null || !unit.canBuild()) {
        toast(LunariansI18n.get("cv_need_builder"));
        boolean bool5 = false;
        boolean bool6 = false;
        boolean bool4 = false, bool7 = bool4;
        return bool7;
      } 
      boolean[] arrayOfBoolean = AutoConnectConveyor.reservedFromPlans(unit);
      int[] arrayOfInt = exitOrder(i);
      int j = Vars.world.width();
      byte b;
      for (b = 0; b < arrayOfInt.length; b++) {
        int k, m = arrayOfInt[b];
        dryRun = true;
        try {
          k = generateInner();
        } finally {
          dryRun = false;
        } 
        if (k <= 0) {
          boolean bool6 = false;
          boolean bool4 = false;
          boolean bool5 = false, bool7 = bool5;
          return bool7;
        } 
        if (lastHub >= 0) {
          boolean[] arrayOfBoolean1 = (boolean[])arrayOfBoolean.clone();
          int n;
          for (n = 0; n < lastPlans.size; n++)
            AutoConnectConveyor.markPlan((BuildPlan)lastPlans.get(n), arrayOfBoolean1); 
          n = lastHub % j + DX[m];
          int i1 = lastHub / j + DY[m];
          Seq<BuildPlan> seq;
          if (Vars.world.tiles.in(n, i1) && (seq = AutoConnectConveyor.planRoute(n, i1, m, arrayOfBoolean1)) != null) {
            int i3;
            for (i3 = 0; i3 < lastPlans.size; i3++)
              unit.addBuild((BuildPlan)lastPlans.get(i3), true); 
            for (i3 = 0; i3 < seq.size; i3++)
              unit.addBuild((BuildPlan)seq.get(i3), true); 
            if (!quiet)
              toast(LunariansI18n.get("dr_summary", String.valueOf(k), String.valueOf(seq.size))); 
            int i4 = i3 = k;
            int i2 = i3, i5 = i2;
            return i5;
          } 
        } 
      } 
      if (!quiet)
        toast(LunariansI18n.get("cv_no_path")); 
      b = 0;
      boolean bool2 = false;
      boolean bool1 = false, bool3 = bool1;
      return bool3;
    } catch (Throwable throwable) {
      if (!errorLogged) {
        errorLogged = true;
        Log.err("[Lunarians] AutoDrill+Conveyor error", throwable);
      } 
      boolean bool3 = false;
      boolean bool1 = false;
      boolean bool2 = false, bool4 = bool2;
      return bool4;
    } finally {
      LunariansSettings.drillExitDir = i;
      dryRun = false;
    } 
  }
  
  private static int run() {
    if (Vars.headless || Vars.player == null || Vars.state == null || !Vars.state.isGame())
      return 0; 
    try {
      return generateInner();
    } catch (Throwable throwable) {
      if (!errorLogged) {
        errorLogged = true;
        Log.err("[Lunarians] AutoDrill error", throwable);
      } 
      return 0;
    } 
  }
  
  private static Block resolveDrill() {
    String str = LunariansSettings.drillBlock;
    if (str == null || str.isEmpty())
      str = "mechanical-drill"; 
    Block block;
    if (block = Vars.content.block(str) instanceof Drill)
      return block; 
    return Blocks.mechanicalDrill;
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
  
  private static Item oreAt(Tile paramTile) {
    if (paramTile == null)
      return null; 
    try {
      if (paramTile.drop() != null)
        return paramTile.drop(); 
    } catch (Throwable throwable) {}
    Floor floor1 = (paramTile.overlay() != null) ? paramTile.overlay() : paramTile.floor(), floor2 = floor1;
    if (floor1 != null && floor1.itemDrop != null)
      return floor1.itemDrop; 
    return null;
  }
  
  private static boolean sameOre(Tile paramTile, Item paramItem) {
    Item item = oreAt(paramTile);
    return (item != null && item == paramItem);
  }
  
  private static boolean free(int paramInt1, int paramInt2, boolean[] paramArrayOfboolean, int paramInt3, int paramInt4) {
    if (!Vars.world.tiles.in(paramInt1, paramInt2))
      return false; 
    if (paramArrayOfboolean[paramInt2 * paramInt3 + paramInt1])
      return false; 
    Tile tile = Vars.world.tile(paramInt1, paramInt2);
    if (tile == null || tile.solid())
      return false; 
    return (tile.build == null || tile.build.block == null || !tile.build.block.solid);
  }
  
  private static boolean freeConv(int paramInt1, int paramInt2, boolean[] paramArrayOfboolean, int paramInt3, int paramInt4) {
    if (!Vars.world.tiles.in(paramInt1, paramInt2))
      return false; 
    if (paramArrayOfboolean[paramInt2 * paramInt3 + paramInt1])
      return false; 
    Tile tile = Vars.world.tile(paramInt1, paramInt2);
    if (tile == null)
      return false; 
    if (tile.build != null && tile.build.block != null && tile.build.block.solid)
      return false; 
    if (limitOn && (tile.build != null || nextToDrill(paramInt1, paramInt2)))
      return false; 
    return (tile.block() == null || !(tile.block()).solid || !tile.block().isStatic());
  }
  
  private static int generateInner() {
    failedNoneFit = false;
    int i = generateInner2(true);
    if (i <= 0 && failedNoneFit) {
      failedNoneFit = false;
      i = generateInner2(false);
    } 
    return i;
  }
  
  private static int generateInner2(boolean paramBoolean) {
    lastHub = -1;
    int i8 = LunariansSettings.drillStartX;
    int i9 = LunariansSettings.drillStartY;
    if (i8 == Integer.MIN_VALUE || i9 == Integer.MIN_VALUE) {
      toast(LunariansI18n.get("dr_set_ore"));
      return 0;
    } 
    Tile tile = Vars.world.tile(i8, i9);
    Item item = oreAt(tile);
    if (item == null) {
      toast(LunariansI18n.get("dr_no_ore"));
      return 0;
    } 
    Block block1 = resolveDrillFor(item);
    Block block2 = resolveConveyor();
    if (!(block1 instanceof Drill) || block2 == null) {
      toast(LunariansI18n.get("dr_invalid"));
      return 0;
    } 
    if (((Drill)block1).tier < item.hardness) {
      toast(pt() ? ("Nenhuma drill disponível consegue minerar " + item.localizedName) : ("No available drill can mine " + item.localizedName));
      return 0;
    } 
    int i10 = block1.size;
    int i11 = Math.max(16, LunariansSettings.drillMaxTiles);
    if (limitOn && floorOre(tile))
      i11 = Math.min(i11, AutoSetConfig.floorMax()); 
    int i7 = Math.max(1, LunariansSettings.drillMinOres), i12 = i7;
    int i13 = (i10 * i10 + 1) / 2;
    boolean bool1 = false;
    if (limitOn && paramBoolean && i13 > i7) {
      i12 = i13;
      bool1 = true;
    } 
    int i14 = LunariansSettings.drillExitDir & 0x3;
    boolean bool2 = (LunariansSettings.drillLayoutMode == 1) ? true : false;
    Unit unit = Vars.player.unit();
    if (unit == null || !unit.canBuild()) {
      toast(LunariansI18n.get("cv_need_builder"));
      return 0;
    } 
    int i15 = Vars.world.width();
    int i16 = Vars.world.height();
    boolean[] arrayOfBoolean1 = new boolean[i15 * i16];
    IntSeq intSeq1 = new IntSeq();
    IntQueue intQueue = new IntQueue();
    int i17 = i9 * i15 + i8;
    intQueue.addLast(i17);
    arrayOfBoolean1[i17] = true;
    while (!intQueue.isEmpty() && intSeq1.size < i11) {
      int i20 = intQueue.removeFirst();
      intSeq1.add(i20);
      int i19 = i20 % i15;
      int i18 = i20 / i15;
      for (byte b = 0; b < 4; b++) {
        int i23 = i19 + DX[b];
        int i22 = i18 + DY[b];
        int i21;
        if (Vars.world.tiles.in(i23, i22) && !arrayOfBoolean1[i21 = i22 * i15 + i23] && sameOre(Vars.world.tile(i23, i22), item) && inLimit(i23, i22) && inFloor(i23, i22, i8, i9)) {
          arrayOfBoolean1[i21] = true;
          intQueue.addLast(i21);
        } 
      } 
    } 
    if (intSeq1.isEmpty()) {
      toast(LunariansI18n.get("dr_empty"));
      return 0;
    } 
    int i6 = Integer.MAX_VALUE;
    int i5 = Integer.MAX_VALUE;
    int i4 = 0;
    int i3 = 0;
    for (byte b2 = 0; b2 < intSeq1.size; b2++) {
      int i20 = intSeq1.get(b2);
      int i19 = i20 % i15;
      int i18 = i20 / i15;
      i6 = Math.min(i6, i19);
      i5 = Math.min(i5, i18);
      i4 = Math.max(i4, i19);
      i3 = Math.max(i3, i18);
    } 
    boolean[] arrayOfBoolean2 = limitOn ? AutoConnectConveyor.reservedFromPlans(unit) : new boolean[i15 * i16];
    boolean[] arrayOfBoolean3 = new boolean[i15 * i16];
    Seq<BuildPlan> seq = new Seq();
    lastDrillTiles.clear();
    int i2 = 0;
    IntSeq intSeq2 = new IntSeq();
    boolean bool3 = (i14 == 1 || i14 == 3) ? true : false;
    byte b3 = 0;
    byte b4 = 0;
    if (LunariansSettings.drillPerfect) {
      int i18 = -1;
      int i19 = Integer.MAX_VALUE;
      for (byte b = 0; b <= i10; b++) {
        for (byte b5 = 0; b5 <= i10; b5++) {
          int[] arrayOfInt1 = scanGrid(bool3, bool2, b, b5, i10, i6, i5, i4, i3, i12, item, arrayOfBoolean1, (boolean[])arrayOfBoolean2.clone(), (boolean[])arrayOfBoolean3.clone(), i15, i16, block1, new Seq(), new IntSeq(), new IntSeq());
          if (arrayOfInt1[0] != 0 && (
            arrayOfInt1[1] > i18 || (arrayOfInt1[1] == i18 && arrayOfInt1[0] < i19))) {
            i18 = arrayOfInt1[1];
            i19 = arrayOfInt1[0];
            b3 = b;
            b4 = b5;
          } 
        } 
      } 
    } 
    int[] arrayOfInt = scanGrid(bool3, bool2, b3, b4, i10, i6, i5, i4, i3, i12, item, arrayOfBoolean1, arrayOfBoolean2, arrayOfBoolean3, i15, i16, block1, seq, lastDrillTiles, intSeq2);
    i2 = arrayOfInt[0];
    if (LunariansSettings.drillPerfect && i2 > 0)
      i2 += fillGaps(bool3, i10, i12, item, arrayOfBoolean1, arrayOfBoolean2, arrayOfBoolean3, i15, i16, block1, seq, lastDrillTiles, intSeq2); 
    if (i2 == 0) {
      if (bool1) {
        failedNoneFit = true;
        return 0;
      } 
      toast(LunariansI18n.get("dr_none_fit"));
      return 0;
    } 
    int i1 = Integer.MAX_VALUE;
    int n = Integer.MAX_VALUE;
    int m = 0;
    int k = 0;
    byte b1;
    for (b1 = 0; b1 < lastDrillTiles.size; b1++) {
      int i20 = lastDrillTiles.get(b1);
      int i19 = i20 % i15;
      int i18 = i20 / i15;
      i1 = Math.min(i1, i19);
      n = Math.min(n, i18);
      m = Math.max(m, i19 + i10 - 1);
      k = Math.max(k, i18 + i10 - 1);
    } 
    b1 = 0;
    if (i14 == 1 || i14 == 3) {
      int i19 = i1 - 1;
      int i18 = m + 1;
      if (i19 >= 0 && !intSeq2.contains(i19))
        intSeq2.add(i19); 
      if (i18 < i15 && !intSeq2.contains(i18))
        intSeq2.add(i18); 
    } else {
      int i19 = n - 1;
      int i18 = k + 1;
      if (i19 >= 0 && !intSeq2.contains(i19))
        intSeq2.add(i19); 
      if (i18 < i16 && !intSeq2.contains(i18))
        intSeq2.add(i18); 
    } 
    if (i14 == 1 || i14 == 3) {
      for (byte b = 0; b < intSeq2.size; b++) {
        int i19 = intSeq2.get(b);
        for (int i18 = n; i18 <= k; i18++) {
          if (freeConv(i19, i18, arrayOfBoolean2, i15, i16)) {
            seq.add(new BuildPlan(i19, i18, i14, block2));
            arrayOfBoolean2[i18 * i15 + i19] = true;
            b1++;
          } 
        } 
      } 
    } else {
      for (byte b = 0; b < intSeq2.size; b++) {
        int i19 = intSeq2.get(b);
        for (int i18 = i1; i18 <= m; i18++) {
          if (freeConv(i18, i19, arrayOfBoolean2, i15, i16)) {
            seq.add(new BuildPlan(i18, i19, i14, block2));
            arrayOfBoolean2[i19 * i15 + i18] = true;
            b1++;
          } 
        } 
      } 
    } 
    int j;
    for (j = 0; j < lastDrillTiles.size; j++) {
      int i20 = lastDrillTiles.get(j);
      int i19 = i20 % i15;
      int i18, arrayOfInt1[] = pickFace(i19, i18 = i20 / i15, i10, i14, intSeq2, arrayOfBoolean2, arrayOfBoolean3, i15, i16);
      if (arrayOfInt1 != null) {
        int i21 = arrayOfInt1[0];
        int i22 = arrayOfInt1[1];
        int i23 = arrayOfInt1[2];
        if (freeConv(i21, i22, arrayOfBoolean2, i15, i16)) {
          seq.add(new BuildPlan(i21, i22, i23, block2));
          arrayOfBoolean2[i22 * i15 + i21] = true;
          b1++;
        } 
      } 
    } 
    for (j = 0; j < lastDrillTiles.size; j++) {
      int i20 = lastDrillTiles.get(j);
      int i19 = i20 % i15;
      int i18;
      if (!hasAdjConv(i19, i18 = i20 / i15, i10, arrayOfBoolean2, i15)) {
        int[] arrayOfInt1;
        boolean bool = false;
        int[][] arrayOfInt2 = new int[1][];
        if (i14 == 0) {
          int[] arrayOfInt3 = new int[3];
          arrayOfInt3[0] = i19 + i10;
          arrayOfInt3[1] = i18;
          arrayOfInt1 = arrayOfInt3;
          arrayOfInt3[2] = 0;
        } else if (i14 == 1) {
          int[] arrayOfInt3 = new int[3];
          arrayOfInt3[0] = i19;
          arrayOfInt3[1] = i18 + i10;
          arrayOfInt1 = arrayOfInt3;
          arrayOfInt3[2] = 1;
        } else if (i14 == 2) {
          int[] arrayOfInt3 = new int[3];
          arrayOfInt3[0] = i19 - 1;
          arrayOfInt3[1] = i18;
          arrayOfInt1 = arrayOfInt3;
          arrayOfInt3[2] = 2;
        } else {
          int[] arrayOfInt3 = new int[3];
          arrayOfInt3[0] = i19;
          arrayOfInt3[1] = i18 - 1;
          arrayOfInt1 = arrayOfInt3;
          arrayOfInt3[2] = 3;
        } 
        arrayOfInt2[0] = arrayOfInt1;
        IntSeq intSeq3 = new IntSeq();
        IntSeq intSeq4 = new IntSeq();
        IntSeq intSeq5 = new IntSeq();
        byte b;
        for (b = 0; b < i10; b++) {
          if (i14 == 0) {
            intSeq3.add(i19 + i10);
            intSeq4.add(i18 + b);
            intSeq5.add(0);
          } else if (i14 == 1) {
            intSeq3.add(i19 + b);
            intSeq4.add(i18 + i10);
            intSeq5.add(1);
          } else if (i14 == 2) {
            intSeq3.add(i19 - 1);
            intSeq4.add(i18 + b);
            intSeq5.add(2);
          } else {
            intSeq3.add(i19 + b);
            intSeq4.add(i18 - 1);
            intSeq5.add(3);
          } 
        } 
        for (b = 0; b < i10; b++) {
          intSeq3.add(i19 + i10);
          intSeq4.add(i18 + b);
          intSeq5.add(0);
          intSeq3.add(i19 - 1);
          intSeq4.add(i18 + b);
          intSeq5.add(2);
          intSeq3.add(i19 + b);
          intSeq4.add(i18 + i10);
          intSeq5.add(1);
          intSeq3.add(i19 + b);
          intSeq4.add(i18 - 1);
          intSeq5.add(3);
        } 
        for (b = 0; b < intSeq3.size; ) {
          int i21 = intSeq3.get(b);
          int i22 = intSeq4.get(b);
          int i23 = intSeq5.get(b);
          if (!freeConv(i21, i22, arrayOfBoolean2, i15, i16)) {
            b++;
            continue;
          } 
          seq.add(new BuildPlan(i21, i22, i23, block2));
          arrayOfBoolean2[i22 * i15 + i21] = true;
          b1++;
          bool = true;
        } 
      } 
    } 
    for (j = 0; j < lastDrillTiles.size; j++) {
      int i20 = lastDrillTiles.get(j);
      int i19 = i20 % i15;
      int i18 = i20 / i15;
      if (i14 == 1 || i14 == 3) {
        for (byte b = 0; b < i10; b++) {
          int i21 = i19 + i10;
          int i22 = i18 + b;
          if (freeConv(i21, i22, arrayOfBoolean2, i15, i16)) {
            boolean bool = false;
            seq.add(new BuildPlan(i21, i22, bool, block2));
            arrayOfBoolean2[i22 * i15 + i21] = true;
            b1++;
          } 
          if (freeConv(i21 = i19 - 1, i22 = i18 + b, arrayOfBoolean2, i15, i16)) {
            seq.add(new BuildPlan(i21, i22, 2, block2));
            arrayOfBoolean2[i22 * i15 + i21] = true;
            b1++;
          } 
        } 
      } else {
        for (byte b = 0; b < i10; b++) {
          int i21 = i19 + b;
          int i22 = i18 + i10;
          if (freeConv(i21, i22, arrayOfBoolean2, i15, i16)) {
            seq.add(new BuildPlan(i21, i22, 1, block2));
            arrayOfBoolean2[i22 * i15 + i21] = true;
            b1++;
          } 
          if (freeConv(i21 = i19 + b, i22 = i18 - 1, arrayOfBoolean2, i15, i16)) {
            seq.add(new BuildPlan(i21, i22, 3, block2));
            arrayOfBoolean2[i22 * i15 + i21] = true;
            b1++;
          } 
        } 
      } 
    } 
    if (i14 == 1 || i14 == 3) {
      for (j = 0; j < lastDrillTiles.size; j++) {
        int i22 = lastDrillTiles.get(j);
        int i21 = i22 % i15;
        int i18 = i22 / i15;
        int i20 = Integer.MIN_VALUE;
        int i19 = Integer.MAX_VALUE;
        byte b;
        for (b = 0; b < intSeq2.size; b++) {
          int i24 = intSeq2.get(b);
          int i23 = Math.abs(i24 - i21 + i10 / 2);
          if (i23 < i19) {
            i19 = i23;
            i20 = i24;
          } 
        } 
        if (i20 != Integer.MIN_VALUE)
          for (b = 0; b < i10; b++) {
            int i25 = i18 + b;
            int i24 = (i20 > i21) ? (i21 + i10) : (i21 - 1);
            byte b5 = (i20 > i21) ? 1 : -1;
            for (int i23 = i24; i23 != i20 && freeConv(i23, i25, arrayOfBoolean2, i15, i16); i23 += b5) {
              seq.add(new BuildPlan(i23, i25, b5 ? 0 : 2, block2));
              arrayOfBoolean2[i25 * i15 + i23] = true;
              b1++;
              if (Math.abs(i23 - i24) > 6)
                break; 
            } 
          }  
      } 
    } else {
      for (j = 0; j < lastDrillTiles.size; j++) {
        int i22 = lastDrillTiles.get(j);
        int i21 = i22 % i15;
        int i18 = i22 / i15;
        int i20 = Integer.MIN_VALUE;
        int i19 = Integer.MAX_VALUE;
        byte b;
        for (b = 0; b < intSeq2.size; b++) {
          int i24 = intSeq2.get(b);
          int i23 = Math.abs(i24 - i18 + i10 / 2);
          if (i23 < i19) {
            i19 = i23;
            i20 = i24;
          } 
        } 
        if (i20 != Integer.MIN_VALUE)
          for (b = 0; b < i10; b++) {
            int i25 = i21 + b;
            int i24 = (i20 > i18) ? (i18 + i10) : (i18 - 1);
            byte b5 = (i20 > i18) ? 1 : -1;
            for (int i23 = i24; i23 != i20 && freeConv(i25, i23, arrayOfBoolean2, i15, i16); i23 += b5) {
              seq.add(new BuildPlan(i25, i23, b5 ? 1 : 3, block2));
              arrayOfBoolean2[i23 * i15 + i25] = true;
              b1++;
              if (Math.abs(i23 - i24) > 6)
                break; 
            } 
          }  
      } 
    } 
    j = (i1 + m) / 2;
    int i = (n + k) / 2;
    if ((i14 == 1 || i14 == 3) && intSeq2.size > 0) {
      int i19 = intSeq2.get(0);
      int i18 = Math.abs(i19 - j);
      for (byte b = 1; b < intSeq2.size; b++) {
        int i20 = intSeq2.get(b);
        if (Math.abs(i20 - j) < i18) {
          i18 = Math.abs(i20 - j);
          i19 = i20;
        } 
      } 
      j = i19;
    } 
    if ((i14 == 0 || i14 == 2) && intSeq2.size > 0) {
      int i19 = intSeq2.get(0);
      int i18 = Math.abs(i19 - i);
      for (byte b = 1; b < intSeq2.size; b++) {
        int i20 = intSeq2.get(b);
        if (Math.abs(i20 - i) < i18) {
          i18 = Math.abs(i20 - i);
          i19 = i20;
        } 
      } 
      i = i19;
    } 
    if (i14 == 1) {
      int i18 = k + 1;
      if (i18 < i16) {
        for (int i19 = i1; i19 <= m; i19++) {
          if (freeConv(i19, i18, arrayOfBoolean2, i15, i16)) {
            boolean bool = false;
            int i20;
            for (i20 = n; i20 <= k && !bool; i20++) {
              if (arrayOfBoolean3[i20 * i15 + i19] || arrayOfBoolean2[i20 * i15 + i19])
                bool = true; 
            } 
            if (bool) {
              i20 = (i19 == j) ? 1 : ((i19 < j) ? 0 : 2);
              seq.add(new BuildPlan(i19, i18, i20, block2));
              arrayOfBoolean2[i18 * i15 + i19] = true;
              b1++;
            } 
          } 
        } 
        if (freeConv(j, i18 + 1, arrayOfBoolean2, i15, i16)) {
          seq.add(new BuildPlan(j, i18 + 1, 1, block2));
          arrayOfBoolean2[(i18 + 1) * i15 + j] = true;
          b1++;
          lastHub = (i18 + 1) * i15 + j;
        } else {
          lastHub = i18 * i15 + j;
        } 
      } 
    } else if (i14 == 3) {
      int i18 = n - 1;
      if (i18 >= 0) {
        for (int i19 = i1; i19 <= m; i19++) {
          if (freeConv(i19, i18, arrayOfBoolean2, i15, i16)) {
            boolean bool = false;
            int i20;
            for (i20 = n; i20 <= k && !bool; i20++) {
              if (arrayOfBoolean3[i20 * i15 + i19] || arrayOfBoolean2[i20 * i15 + i19])
                bool = true; 
            } 
            if (bool) {
              i20 = (i19 == j) ? 3 : ((i19 < j) ? 0 : 2);
              seq.add(new BuildPlan(i19, i18, i20, block2));
              arrayOfBoolean2[i18 * i15 + i19] = true;
              b1++;
            } 
          } 
        } 
        if (freeConv(j, i18 - 1, arrayOfBoolean2, i15, i16)) {
          seq.add(new BuildPlan(j, i18 - 1, 3, block2));
          arrayOfBoolean2[(i18 - 1) * i15 + j] = true;
          b1++;
          lastHub = (i18 - 1) * i15 + j;
        } else {
          lastHub = i18 * i15 + j;
        } 
      } 
    } else if (i14 == 0) {
      int i18 = m + 1;
      if (i18 < i15) {
        for (int i19 = n; i19 <= k; i19++) {
          if (freeConv(i18, i19, arrayOfBoolean2, i15, i16)) {
            boolean bool = false;
            int i20;
            for (i20 = i1; i20 <= m && !bool; i20++) {
              if (arrayOfBoolean3[i19 * i15 + i20] || arrayOfBoolean2[i19 * i15 + i20])
                bool = true; 
            } 
            if (bool) {
              i20 = (i19 == i) ? 0 : ((i19 < i) ? 1 : 3);
              seq.add(new BuildPlan(i18, i19, i20, block2));
              arrayOfBoolean2[i19 * i15 + i18] = true;
              b1++;
            } 
          } 
        } 
        if (freeConv(i18 + 1, i, arrayOfBoolean2, i15, i16)) {
          seq.add(new BuildPlan(i18 + 1, i, 0, block2));
          arrayOfBoolean2[i * i15 + i18 + 1] = true;
          b1++;
          lastHub = i * i15 + i18 + 1;
        } else {
          lastHub = i * i15 + i18;
        } 
      } 
    } else {
      int i18 = i1 - 1;
      if (i18 >= 0) {
        for (int i19 = n; i19 <= k; i19++) {
          if (freeConv(i18, i19, arrayOfBoolean2, i15, i16)) {
            boolean bool = false;
            int i20;
            for (i20 = i1; i20 <= m && !bool; i20++) {
              if (arrayOfBoolean3[i19 * i15 + i20] || arrayOfBoolean2[i19 * i15 + i20])
                bool = true; 
            } 
            if (bool) {
              i20 = (i19 == i) ? 2 : ((i19 < i) ? 1 : 3);
              seq.add(new BuildPlan(i18, i19, i20, block2));
              arrayOfBoolean2[i19 * i15 + i18] = true;
              b1++;
            } 
          } 
        } 
        if (freeConv(i18 - 1, i, arrayOfBoolean2, i15, i16)) {
          seq.add(new BuildPlan(i18 - 1, i, 2, block2));
          arrayOfBoolean2[i * i15 + i18 - 1] = true;
          b1++;
          lastHub = i * i15 + i18 - 1;
        } else {
          lastHub = i * i15 + i18;
        } 
      } 
    } 
    if (i14 == 1 || i14 == 3) {
      int i18 = (i14 == 1) ? (k + 1) : (n - 1);
      for (byte b = 0; b < intSeq2.size; b++) {
        int i19 = intSeq2.get(b);
        if (i14 == 1) {
          for (int i20 = k; i20 <= i18; i20++) {
            if (freeConv(i19, i20, arrayOfBoolean2, i15, i16)) {
              seq.add(new BuildPlan(i19, i20, 1, block2));
              arrayOfBoolean2[i20 * i15 + i19] = true;
              b1++;
            } 
          } 
        } else {
          for (int i20 = i18; i20 <= n; i20++) {
            if (freeConv(i19, i20, arrayOfBoolean2, i15, i16)) {
              seq.add(new BuildPlan(i19, i20, 3, block2));
              arrayOfBoolean2[i20 * i15 + i19] = true;
              b1++;
            } 
          } 
        } 
      } 
    } else {
      int i18 = (i14 == 0) ? (m + 1) : (i1 - 1);
      for (byte b = 0; b < intSeq2.size; b++) {
        int i19 = intSeq2.get(b);
        if (i14 == 0) {
          for (int i20 = m; i20 <= i18; i20++) {
            if (freeConv(i20, i19, arrayOfBoolean2, i15, i16)) {
              seq.add(new BuildPlan(i20, i19, 0, block2));
              arrayOfBoolean2[i19 * i15 + i20] = true;
              b1++;
            } 
          } 
        } else {
          for (int i20 = i18; i20 <= i1; i20++) {
            if (freeConv(i20, i19, arrayOfBoolean2, i15, i16)) {
              seq.add(new BuildPlan(i20, i19, 2, block2));
              arrayOfBoolean2[i19 * i15 + i20] = true;
              b1++;
            } 
          } 
        } 
      } 
    } 
    fixRotations(seq, block2, i15);
    seq = pruneConveyors(seq, block2, i15);
    b1 = 0;
    for (j = 0; j < seq.size; j++) {
      if (((BuildPlan)seq.get(j)).block == block2)
        b1++; 
    } 
    seq = AutoConnectConveyor.sanitize(seq);
    lastPlans.clear();
    lastPlans.addAll(seq);
    if (!dryRun) {
      byte b;
      for (b = 0; b < seq.size; b++) {
        BuildPlan buildPlan = (BuildPlan)seq.get(b);
        if (buildPlan.block instanceof Drill)
          unit.addBuild(buildPlan, true); 
      } 
      for (b = 0; b < seq.size; b++) {
        BuildPlan buildPlan = (BuildPlan)seq.get(b);
        if (!(buildPlan.block instanceof Drill))
          unit.addBuild(buildPlan, true); 
      } 
    } 
    lastCount = i2;
    if (!dryRun && !quiet)
      toast(LunariansI18n.get("dr_summary", String.valueOf(i2), String.valueOf(b1))); 
    return lastCount;
  }
  
  private static int[] pickFace(int paramInt1, int paramInt2, int paramInt3, int paramInt4, IntSeq paramIntSeq, boolean[] paramArrayOfboolean1, boolean[] paramArrayOfboolean2, int paramInt5, int paramInt6) {
    if (paramInt4 == 1 || paramInt4 == 3) {
      int j = Integer.MIN_VALUE;
      int k = Integer.MAX_VALUE;
      int i;
      for (i = 0; i < paramIntSeq.size; i++) {
        int n = paramIntSeq.get(i);
        int m = Math.abs(n - paramInt1 + paramInt3 / 2);
        if (m < k) {
          k = m;
          j = n;
        } 
      } 
      if (j != Integer.MIN_VALUE) {
        i = (j > paramInt1) ? (paramInt1 + paramInt3) : (paramInt1 - 1);
        for (byte b = 0; b < paramInt3; ) {
          int m = paramInt2 + b;
          if (!freeConv(i, m, paramArrayOfboolean1, paramInt5, paramInt6)) {
            b++;
            continue;
          } 
          boolean bool = (j > paramInt1) ? false : true;
          return new int[] { i, m, bool };
        } 
      } 
      for (i = 0; i < paramInt3; ) {
        int m;
        int n;
        byte b;
        if (paramInt4 == 1) {
          n = paramInt1 + i;
          m = paramInt2 + paramInt3;
          b = 1;
        } else {
          n = paramInt1 + i;
          m = paramInt2 - 1;
          b = 3;
        } 
        if (!freeConv(n, m, paramArrayOfboolean1, paramInt5, paramInt6)) {
          i++;
          continue;
        } 
        return new int[] { n, m, b };
      } 
    } else {
      int j = Integer.MIN_VALUE;
      int k = Integer.MAX_VALUE;
      int i;
      for (i = 0; i < paramIntSeq.size; i++) {
        int n = paramIntSeq.get(i);
        int m = Math.abs(n - paramInt2 + paramInt3 / 2);
        if (m < k) {
          k = m;
          j = n;
        } 
      } 
      if (j != Integer.MIN_VALUE) {
        i = (j > paramInt2) ? (paramInt2 + paramInt3) : (paramInt2 - 1);
        for (byte b = 0; b < paramInt3; ) {
          int m = paramInt1 + b;
          if (!freeConv(m, i, paramArrayOfboolean1, paramInt5, paramInt6)) {
            b++;
            continue;
          } 
          boolean bool = (j > paramInt2) ? true : true;
          return new int[] { m, i, bool };
        } 
      } 
      for (i = 0; i < paramInt3; ) {
        int m;
        int n;
        byte b;
        if (paramInt4 == 0) {
          n = paramInt1 + paramInt3;
          m = paramInt2 + i;
          b = 0;
        } else {
          n = paramInt1 - 1;
          m = paramInt2 + i;
          b = 2;
        } 
        if (!freeConv(n, m, paramArrayOfboolean1, paramInt5, paramInt6)) {
          i++;
          continue;
        } 
        return new int[] { n, m, b };
      } 
    } 
    return null;
  }
  
  private static boolean hasAdjConv(int paramInt1, int paramInt2, int paramInt3, boolean[] paramArrayOfboolean, int paramInt4) {
    for (byte b = 0; b < paramInt3; b++) {
      for (byte b1 = 0; b1 < paramInt3; b1++) {
        for (byte b2 = 0; b2 < 4; ) {
          int i = paramInt1 + b1 + DX[b2];
          int j = paramInt2 + b + DY[b2];
          if (!Vars.world.tiles.in(i, j) || (i >= paramInt1 && i < paramInt1 + paramInt3 && j >= paramInt2 && j < paramInt2 + paramInt3) || !paramArrayOfboolean[j * paramInt4 + i]) {
            b2++;
            continue;
          } 
          return true;
        } 
      } 
    } 
    return false;
  }
  
  private static boolean touchesDrill(int paramInt1, int paramInt2, boolean[] paramArrayOfboolean, int paramInt3, int paramInt4) {
    for (byte b = 0; b < 4; ) {
      int i = paramInt1 + DX[b];
      int j = paramInt2 + DY[b];
      if (!Vars.world.tiles.in(i, j) || !paramArrayOfboolean[j * paramInt3 + i]) {
        b++;
        continue;
      } 
      return true;
    } 
    return false;
  }
  
  private static boolean canPlaceDrill(int paramInt1, int paramInt2, int paramInt3, boolean[] paramArrayOfboolean, int paramInt4, int paramInt5) {
    if (paramInt1 < 0 || paramInt2 < 0 || paramInt1 + paramInt3 > paramInt4 || paramInt2 + paramInt3 > paramInt5)
      return false; 
    for (byte b = 0; b < paramInt3; b++) {
      for (byte b1 = 0; b1 < paramInt3; ) {
        if (paramArrayOfboolean[(paramInt2 + b) * paramInt4 + paramInt1 + b1])
          return false; 
        Tile tile = Vars.world.tile(paramInt1 + b1, paramInt2 + b);
        if (tile == null)
          return false; 
        if (tile.solid() && tile.build == null)
          return false; 
        if (tile.build == null || tile.build.block == null || !tile.build.block.solid) {
          b1++;
          continue;
        } 
        return false;
      } 
    } 
    return (!limitOn || !crowded(paramInt1, paramInt2, paramInt3, (isoOverride >= 0) ? isoOverride : AutoSetConfig.spacing()));
  }
  
  private static int[] scanGrid(boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Item paramItem, boolean[] paramArrayOfboolean1, boolean[] paramArrayOfboolean2, boolean[] paramArrayOfboolean3, int paramInt9, int paramInt10, Block paramBlock, Seq<BuildPlan> paramSeq, IntSeq paramIntSeq1, IntSeq paramIntSeq2) {
    int i = paramBoolean1 ? (paramInt4 + paramInt1) : (paramInt5 + paramInt2);
    int j = paramBoolean1 ? (paramInt5 + paramInt2) : (paramInt4 + paramInt1);
    int k = paramBoolean1 ? paramInt6 : paramInt7;
    int m = paramBoolean1 ? paramInt7 : paramInt6;
    int n = paramBoolean1 ? paramInt9 : paramInt10;
    byte b1 = 0;
    int i1 = 0;
    byte b2 = 0;
    while (i + paramInt3 - 1 <= k) {
      boolean bool = false;
      int i2 = j;
      while (i2 + paramInt3 - 1 <= m) {
        int i3 = paramBoolean1 ? i : i2;
        int i4 = paramBoolean1 ? i2 : i;
        if (canPlaceDrill(i3, i4, paramInt3, paramArrayOfboolean2, paramInt9, paramInt10)) {
          int i5 = countOre(i3, i4, paramInt3, paramItem, paramArrayOfboolean1, paramInt9);
          if (i5 >= paramInt8) {
            paramSeq.add(new BuildPlan(i3 + (paramInt3 - 1) / 2, i4 + (paramInt3 - 1) / 2, 0, paramBlock));
            paramIntSeq1.add(i4 * paramInt9 + i3);
            mark(i3, i4, paramInt3, paramArrayOfboolean2, paramInt9);
            mark(i3, i4, paramInt3, paramArrayOfboolean3, paramInt9);
            b1++;
            i1 += i5;
            bool = true;
          } 
        } 
        i2 += paramInt3;
      } 
      if (bool) {
        b2++;
        if (paramBoolean2 && b2 % 2 != 0) {
          i += paramInt3;
          continue;
        } 
        int i3 = i + paramInt3;
        if (i3 < n)
          paramIntSeq2.add(i3); 
        i += paramInt3 + 1;
        continue;
      } 
      i++;
    } 
    return new int[] { b1, i1 };
  }
  
  private static int fillGaps(boolean paramBoolean, int paramInt1, int paramInt2, Item paramItem, boolean[] paramArrayOfboolean1, boolean[] paramArrayOfboolean2, boolean[] paramArrayOfboolean3, int paramInt3, int paramInt4, Block paramBlock, Seq<BuildPlan> paramSeq, IntSeq paramIntSeq1, IntSeq paramIntSeq2) {
    if (paramIntSeq1.size == 0)
      return 0; 
    int i = Integer.MAX_VALUE;
    int j = Integer.MAX_VALUE;
    int k = 0;
    int m = 0;
    int n;
    for (n = 0; n < paramIntSeq1.size; n++) {
      int i1 = paramIntSeq1.get(n);
      i = Math.min(i, i1 % paramInt3);
      j = Math.min(j, i1 / paramInt3);
      k = Math.max(k, i1 % paramInt3 + paramInt1 - 1);
      m = Math.max(m, i1 / paramInt3 + paramInt1 - 1);
    } 
    n = Math.max(1, Math.min(paramInt2, (paramInt1 * paramInt1 + 2) / 3));
    byte b1 = 0;
    for (byte b2 = 0; b2 < 64; b2++) {
      int i1 = -1;
      int i2 = -1;
      int i3 = 0;
      for (int i4 = j; i4 + paramInt1 - 1 <= m; i4++) {
        for (int i5 = i; i5 + paramInt1 - 1 <= k; i5++) {
          int i6 = paramBoolean ? i5 : i4;
          boolean bool = false;
          int i7;
          for (i7 = 0; i7 < paramIntSeq2.size; i7++) {
            int i8 = paramIntSeq2.get(i7);
            if (i8 >= i6 && i8 <= i6 + paramInt1 - 1) {
              bool = true;
              break;
            } 
          } 
          if (!bool && canPlaceDrill(i5, i4, paramInt1, paramArrayOfboolean2, paramInt3, paramInt4)) {
            i7 = countOre(i5, i4, paramInt1, paramItem, paramArrayOfboolean1, paramInt3);
            if (i7 > i3) {
              i3 = i7;
              i1 = i5;
              i2 = i4;
            } 
          } 
        } 
      } 
      if (i1 < 0 || i3 < n)
        break; 
      paramSeq.add(new BuildPlan(i1 + (paramInt1 - 1) / 2, i2 + (paramInt1 - 1) / 2, 0, paramBlock));
      paramIntSeq1.add(i2 * paramInt3 + i1);
      mark(i1, i2, paramInt1, paramArrayOfboolean2, paramInt3);
      mark(i1, i2, paramInt1, paramArrayOfboolean3, paramInt3);
      b1++;
    } 
    return b1;
  }
  
  private static int countOre(int paramInt1, int paramInt2, int paramInt3, Item paramItem, boolean[] paramArrayOfboolean, int paramInt4) {
    byte b1 = 0;
    byte b2 = 0;
    HashMap<Object, Object> hashMap = null;
    for (byte b3 = 0; b3 < paramInt3; b3++) {
      for (byte b = 0; b < paramInt3; b++) {
        int i = (paramInt2 + b3) * paramInt4 + paramInt1 + b;
        Tile tile = Vars.world.tile(paramInt1 + b, paramInt2 + b3);
        Item item = oreAt(tile);
        if (item != null)
          if (item == paramItem) {
            b2++;
          } else if (limitOn) {
            if (hashMap == null)
              hashMap = new HashMap<>(); 
            Integer integer;
            hashMap.put(item, Integer.valueOf(((integer = (Integer)hashMap.get(item)) == null) ? 1 : (integer.intValue() + 1)));
          }  
        if (paramArrayOfboolean[i] && sameOre(tile, paramItem))
          b1++; 
      } 
    } 
    if (limitOn && hashMap != null)
      for (Map.Entry<Object, Object> entry : hashMap.entrySet()) {
        Item item = (Item)entry.getKey();
        int i = ((Integer)entry.getValue()).intValue();
        if (item.lowPriority && !paramItem.lowPriority)
          continue; 
        if (!item.lowPriority && paramItem.lowPriority)
          return -1; 
        if (i <= b2 && (i != b2 || item.id <= paramItem.id))
          continue; 
        return -1;
      }  
    return b1;
  }
  
  private static void mark(int paramInt1, int paramInt2, int paramInt3, boolean[] paramArrayOfboolean, int paramInt4) {
    for (byte b = 0; b < paramInt3; b++) {
      for (byte b1 = 0; b1 < paramInt3; b1++)
        paramArrayOfboolean[(paramInt2 + b) * paramInt4 + paramInt1 + b1] = true; 
    } 
  }
  
  private static void fixRotations(Seq<BuildPlan> paramSeq, Block paramBlock, int paramInt) {
    if (lastHub < 0 || paramSeq == null || paramSeq.isEmpty())
      return; 
    boolean[] arrayOfBoolean = new boolean[paramInt * Vars.world.height()];
    int[] arrayOfInt1 = new int[paramInt * Vars.world.height()];
    Arrays.fill(arrayOfInt1, -1);
    for (byte b2 = 0; b2 < paramSeq.size; b2++) {
      Object object = paramSeq.get(b2);
      int i;
      if (((BuildPlan)object).block == paramBlock && (i = ((BuildPlan)object).y * paramInt + ((BuildPlan)object).x) >= 0 && i < arrayOfBoolean.length) {
        arrayOfBoolean[i] = true;
        arrayOfInt1[i] = b2;
      } 
    } 
    if (!arrayOfBoolean[lastHub])
      return; 
    int[] arrayOfInt2 = new int[arrayOfBoolean.length];
    Arrays.fill(arrayOfInt2, -1);
    IntQueue intQueue = new IntQueue();
    intQueue.addLast(lastHub);
    arrayOfInt2[lastHub] = lastHub;
    while (!intQueue.isEmpty()) {
      int k = intQueue.removeFirst();
      int j = k % paramInt;
      int i = k / paramInt;
      for (byte b = 0; b < 4; b++) {
        int i1 = j + DX[b];
        int n = i + DY[b];
        int m;
        if (Vars.world.tiles.in(i1, n) && arrayOfInt2[m = n * paramInt + i1] < 0 && arrayOfBoolean[m]) {
          arrayOfInt2[m] = k;
          intQueue.addLast(m);
        } 
      } 
    } 
    for (byte b1 = 0; b1 < arrayOfBoolean.length; b1++) {
      if (arrayOfBoolean[b1] && arrayOfInt2[b1] >= 0 && arrayOfInt2[b1] != b1) {
        int n = arrayOfInt2[b1];
        int m = b1 % paramInt;
        int k = b1 / paramInt;
        int j = n % paramInt;
        int i = n / paramInt;
        boolean bool = false;
        bool = (j > m) ? false : ((j < m) ? true : ((i > k) ? true : true));
        int i1 = arrayOfInt1[b1];
        if (i1 >= 0) {
          BuildPlan buildPlan = (BuildPlan)paramSeq.get(i1);
          paramSeq.set(i1, new BuildPlan(buildPlan.x, buildPlan.y, bool, paramBlock));
        } 
      } 
    } 
  }
  
  private static Block resolveDrillFor(Item paramItem) {
    Drill drill;
    Block block = null;
    if (limitOn && paramItem != null) {
      String str = AutoSetConfig.drillFor(paramItem.name);
      if (str != null && !str.isEmpty()) {
        Block block1 = Vars.content.block(str);
        if (block1 instanceof Drill)
          block = block1; 
      } 
    } 
    if (block == null)
      block = resolveDrill(); 
    if (paramItem != null && block instanceof Drill && ((Drill)block).tier < paramItem.hardness) {
      Drill drill1 = null;
      for (Block block1 : Vars.content.blocks()) {
        if (!(block1 instanceof Drill))
          continue; 
        Drill drill2 = (Drill)block1;
        if (drill2.tier < paramItem.hardness || (drill1 != null && drill2.tier >= drill1.tier && (drill2.tier != drill1.tier || drill2.size >= drill1.size)))
          continue; 
        drill1 = drill2;
      } 
      if (drill1 != null) {
        if (!quiet)
          toast(pt() ? (block.localizedName + " não minera " + block.localizedName + ": usando " + paramItem.localizedName) : (block.localizedName + " cannot mine " + block.localizedName + ": using " + paramItem.localizedName)); 
        drill = drill1;
      } 
    } 
    return (Block)drill;
  }
  
  private static boolean pollutes(Block paramBlock) {
    return (paramBlock instanceof Drill || paramBlock instanceof mindustry.world.blocks.production.GenericCrafter || paramBlock instanceof mindustry.world.blocks.storage.Unloader);
  }
  
  private static void markAround(boolean[] paramArrayOfboolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    for (byte b = 0; b < 4; b++) {
      int i = paramInt1 + DX[b];
      int j = paramInt2 + DY[b];
      if (i >= 0 && j >= 0 && i < paramInt3 && j < paramInt4)
        paramArrayOfboolean[j * paramInt3 + i] = true; 
    } 
  }
  
  public static boolean[] reservedForFeed(Unit paramUnit) {
    boolean[] arrayOfBoolean = AutoConnectConveyor.reservedFromPlans(paramUnit);
    try {
      int i = Vars.world.width();
      int j = Vars.world.height();
      for (byte b = 0; b < j; b++) {
        for (byte b1 = 0; b1 < i; b1++) {
          Tile tile = Vars.world.tile(b1, b);
          if (tile != null && tile.build != null && tile.build.block != null && pollutes(tile.build.block))
            markAround(arrayOfBoolean, b1, b, i, j); 
        } 
      } 
      for (BuildPlan buildPlan : paramUnit.plans()) {
        if (buildPlan.breaking || buildPlan.block == null || !pollutes(buildPlan.block))
          continue; 
        int k = buildPlan.block.size;
        int m = (k - 1) / 2;
        for (byte b1 = 0; b1 < k; b1++) {
          for (byte b2 = 0; b2 < k; b2++)
            markAround(arrayOfBoolean, buildPlan.x - m + b2, buildPlan.y - m + b1, i, j); 
        } 
      } 
    } catch (Throwable throwable) {
      if (!errorLogged) {
        errorLogged = true;
        Log.err("[Lunarians] reservedForFeed", throwable);
      } 
    } 
    return arrayOfBoolean;
  }
  
  private static Seq<BuildPlan> pruneConveyors(Seq<BuildPlan> paramSeq, Block paramBlock, int paramInt) {
    try {
      if (lastHub < 0 || paramSeq == null || paramSeq.isEmpty())
        return paramSeq; 
      int j = Vars.world.height();
      int[] arrayOfInt1 = new int[paramInt * j];
      Arrays.fill(arrayOfInt1, -1);
      ArrayList<BuildPlan> arrayList = new ArrayList();
      HashMap<Object, Object> hashMap = new HashMap<>();
      byte b3 = 0;
      int i;
      for (i = 0; i < paramSeq.size; i++) {
        BuildPlan buildPlan = (BuildPlan)paramSeq.get(i);
        if (buildPlan.block == paramBlock) {
          if (buildPlan.x >= 0 && buildPlan.y >= 0 && buildPlan.x < paramInt && buildPlan.y < j) {
            arrayOfInt1[buildPlan.y * paramInt + buildPlan.x] = arrayList.size();
            arrayList.add(buildPlan);
          } 
        } else if (buildPlan.block instanceof Drill) {
          int m = buildPlan.block.size;
          int n = (m - 1) / 2;
          for (byte b = 0; b < m; b++) {
            for (byte b5 = 0; b5 < m; b5++)
              hashMap.put(Integer.valueOf((buildPlan.y - n + b) * paramInt + buildPlan.x - n + b5), Integer.valueOf(b3)); 
          } 
          b3++;
        } 
      } 
      i = arrayList.size();
      if (i == 0 || arrayOfInt1[lastHub] < 0)
        return paramSeq; 
      int k = arrayOfInt1[lastHub];
      int[] arrayOfInt2 = new int[i];
      int[][] arrayOfInt = new int[i][];
      for (byte b2 = 0; b2 < i; b2++) {
        BuildPlan buildPlan = arrayList.get(b2);
        int i1 = buildPlan.rotation & 0x3;
        int n = buildPlan.x + DX[i1];
        int m = buildPlan.y + DY[i1];
        arrayOfInt2[b2] = (n < 0 || m < 0 || n >= paramInt || m >= j) ? -1 : arrayOfInt1[m * paramInt + n];
        ArrayList<Integer> arrayList1 = new ArrayList();
        byte b;
        for (b = 0; b < 4; b++) {
          if (b != i1) {
            int i3 = buildPlan.x + DX[b];
            int i2 = buildPlan.y + DY[b];
            Integer integer;
            if (i3 >= 0 && i2 >= 0 && i3 < paramInt && i2 < j && (integer = (Integer)hashMap.get(Integer.valueOf(i2 * paramInt + i3))) != null && !arrayList1.contains(integer))
              arrayList1.add(integer); 
          } 
        } 
        arrayOfInt[b2] = new int[arrayList1.size()];
        for (b = 0; b < arrayList1.size(); b++)
          arrayOfInt[b2][b] = ((Integer)arrayList1.get(b)).intValue(); 
      } 
      boolean[] arrayOfBoolean = new boolean[i];
      byte b1;
      for (b1 = 0; b1 < i; b1++) {
        int m = b1;
        for (byte b = 0; m >= 0 && b <= i; b++) {
          if (m == k) {
            arrayOfBoolean[b1] = true;
            break;
          } 
          m = arrayOfInt2[m];
        } 
      } 
      b1 = 1;
      while (b1 != 0) {
        b1 = 0;
        int[] arrayOfInt3 = new int[i];
        for (byte b6 = 0; b6 < i; b6++) {
          if (arrayOfBoolean[b6] && arrayOfInt2[b6] >= 0 && arrayOfBoolean[arrayOfInt2[b6]]) {
            int[] arrayOfInt5 = arrayOfInt3;
            int m = arrayOfInt2[b6];
            arrayOfInt5[m] = arrayOfInt5[m] + 1;
          } 
        } 
        int[] arrayOfInt4 = new int[Math.max(1, b3)];
        byte b5;
        for (b5 = 0; b5 < i; b5++) {
          if (arrayOfBoolean[b5]) {
            int[] arrayOfInt5 = arrayOfInt[b5];
            int m = arrayOfInt5.length;
            for (byte b = 0; b < m; b++) {
              int n = arrayOfInt5[b], i1 = n;
              arrayOfInt4[i1] = arrayOfInt4[i1] + 1;
            } 
          } 
        } 
        for (b5 = 0; b5 < i; b5++) {
          if (arrayOfBoolean[b5] && b5 != k && arrayOfInt3[b5] <= 0) {
            boolean bool = false;
            int[] arrayOfInt5 = arrayOfInt[b5];
            int m = arrayOfInt5.length;
            byte b;
            for (b = 0; b < m; ) {
              int n = arrayOfInt5[b];
              if (arrayOfInt4[n] > 1) {
                b++;
                continue;
              } 
              bool = true;
            } 
            if (!bool) {
              arrayOfBoolean[b5] = false;
              b1 = 1;
              arrayOfInt5 = arrayOfInt[b5];
              m = arrayOfInt5.length;
              for (b = 0; b < m; b++) {
                int n = arrayOfInt5[b], i1 = n;
                arrayOfInt4[i1] = arrayOfInt4[i1] - 1;
              } 
            } 
          } 
        } 
      } 
      Seq<BuildPlan> seq = new Seq();
      for (byte b4 = 0; b4 < paramSeq.size; b4++) {
        BuildPlan buildPlan = (BuildPlan)paramSeq.get(b4);
        int m;
        if (buildPlan.block != paramBlock || buildPlan.x < 0 || buildPlan.y < 0 || buildPlan.x >= paramInt || buildPlan.y >= j || (m = arrayOfInt1[buildPlan.y * paramInt + buildPlan.x]) < 0 || arrayOfBoolean[m])
          seq.add(buildPlan); 
      } 
      return seq;
    } catch (Throwable throwable) {
      if (!errorLogged) {
        errorLogged = true;
        Log.err("[Lunarians] pruneConveyors", throwable);
      } 
      return paramSeq;
    } 
  }
  
  private static boolean pt() {
    return "pt".equals(LunariansSettings.language);
  }
  
  private static void toast(String paramString) {
    if (Vars.ui != null)
      Vars.ui.showInfoToast(paramString, 3.5F); 
  }
  
  public static int isoOverride = -1;
  
  private static boolean failedNoneFit;
  
  static {
    lastPlans = new Seq();
    lastDrillTiles = new IntSeq();
    lastHub = -1;
  }
}
