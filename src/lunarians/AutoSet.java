package lunarians;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.struct.IntQueue;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.core.World;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.game.Schematic;
import mindustry.game.Schematics;
import mindustry.game.Team;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.storage.CoreBlock;

public class AutoSet {
  private static final int[] DX = new int[] { 1, 0, -1, 0, 1, 1, -1, -1 };
  
  private static final int[] DY = new int[] { 0, 1, 0, -1, 1, -1, 1, -1 };
  
  private static String[] oreOrder = new String[] { "copper", "copper", "copper", "lead", "coal", "sand", "coal", "sand" };
  
  private static String[] factoryQueue = new String[0];
  
  private static int runMode;
  
  private static boolean registered;
  
  private static boolean errorLogged;
  
  private static int phase;
  
  private static float armedAt;
  
  private static boolean centerChosen;
  
  private static int cx;
  
  private static int cy;
  
  private static float radius;
  
  private static boolean hasArea;
  
  private static int stepIndex;
  
  private static float nextStepAt;
  
  private static float nextCheck;
  
  private static float lastProgress;
  
  private static float lastWarn;
  
  private static int lastPending;
  
  private static boolean[] used;
  
  private static final Seq<BuildPlan> pending;
  
  private static final HashMap<String, Schematic> schemCache;
  
  private static final ArrayList<int[]> feedTargets;
  
  private static final ArrayList<String> feedItemNames;
  
  private static final ArrayList<Integer> feedReserved;
  
  private static int feedIndex;
  
  private static final HashMap<String, ArrayList<int[]>> feedLines;
  
  private static int steamDone;
  
  private static boolean miningActive;
  
  private static float nextMineCheck;
  
  private static float lastMineHint;
  
  public static void init() {
    if (registered)
      return; 
    registered = true;
    Events.run(EventType.Trigger.update, AutoSet::update);
    Events.run(EventType.Trigger.draw, AutoSet::draw);
  }
  
  private static void stopMining(Unit paramUnit) {
    if (!miningActive)
      return; 
    miningActive = false;
    try {
      if (paramUnit != null) {
        paramUnit.mineTile = null;
        paramUnit.updateBuilding = true;
      } 
      if (Vars.control != null && Vars.control.input != null)
        Vars.control.input.isBuilding = true; 
    } catch (Throwable throwable) {}
  }
  
  private static void autoMine(Unit paramUnit) {
    if (Vars.player == null || Vars.player.closestCore() == null)
      return; 
    if (miningActive && paramUnit.mineTile != null) {
      paramUnit.updateBuilding = false;
      Vars.control.input.isBuilding = false;
    } 
    if (Time.time < nextMineCheck)
      return; 
    nextMineCheck = Time.time + 20.0F;
    CoreBlock.CoreBuild coreBuild = Vars.player.closestCore();
    int[] arrayOfInt = new int[(Vars.content.items()).size];
    boolean bool = false;
    for (byte b = 0; b < pending.size; b++) {
      Object object = pending.get(b);
      Tile tile1 = Vars.world.tile(((BuildPlan)object).x, ((BuildPlan)object).y);
      if (!((BuildPlan)object).breaking && ((BuildPlan)object).block != null && (tile1 == null || tile1.block() != ((BuildPlan)object).block))
        for (ItemStack itemStack : ((BuildPlan)object).block.requirements) {
          short s = itemStack.item.id;
          arrayOfInt[s] = arrayOfInt[s] + (int)Math.ceil((itemStack.amount * Vars.state.rules.buildCostMultiplier));
          bool = true;
        }  
    } 
    Item item = null;
    if (bool && !Vars.state.rules.infiniteResources)
      for (Item item1 : Vars.content.items()) {
        if (arrayOfInt[item1.id] <= coreBuild.items.get(item1) || paramUnit.type.mineTier < item1.hardness || paramUnit.type.mineItems == null || !paramUnit.type.mineItems.contains(item1))
          continue; 
        item = item1;
      }  
    if (item == null) {
      stopMining(paramUnit);
      return;
    } 
    Tile tile = null;
    float f = Float.MAX_VALUE;
    int i = (int)(paramUnit.type.mineRange / 8.0F) + 2;
    int j = paramUnit.tileX();
    int k = paramUnit.tileY();
    for (int m = k - i; m <= k + i; m++) {
      for (int n = j - i; n <= j + i; n++) {
        Tile tile1 = Vars.world.tile(n, m);
        float f1;
        if (tile1 != null && tile1.build == null && tile1.drop() == item && paramUnit.validMine(tile1) && (f1 = paramUnit.dst(tile1.worldx(), tile1.worldy())) < f) {
          f = f1;
          tile = tile1;
        } 
      } 
    } 
    if (tile != null) {
      if (!miningActive)
        toast(tr("Faltando " + item.localizedName + ": minerando automaticamente", "Missing " + item.localizedName + ": mining automatically")); 
      miningActive = true;
      paramUnit.mineTile = tile;
      paramUnit.updateBuilding = false;
      Vars.control.input.isBuilding = false;
      return;
    } 
    stopMining(paramUnit);
    if (Time.time - lastMineHint > 600.0F) {
      lastMineHint = Time.time;
      toast(tr("Faltando " + item.localizedName + ": aproxime sua unidade do minério para minerar automaticamente", "Missing " + item.localizedName + ": move your unit near its ore to auto-mine"));
    } 
  }
  
  public static void reset() {
    try {
      stopMining((Vars.player == null) ? null : Vars.player.unit());
    } catch (Throwable throwable) {}
    phase = 0;
    deleteCount = 0;
    ours.clear();
    seenPlans.clear();
    deleted.clear();
    steamDone = 0;
    hasArea = false;
    stepIndex = 0;
    centerChosen = false;
    pending.clear();
    AutoDrill.limitOn = false;
    AutoDrill.quiet = false;
  }
  
  public static boolean isActive() {
    return (phase != 0);
  }
  
  public static boolean isRunning() {
    return (phase == 3);
  }
  
  public static void request() {
    if (Vars.headless || Vars.state == null || !Vars.state.isGame()) {
      toast(LunariansI18n.get("as_enter_map"));
      return;
    } 
    if (phase == 3) {
      toast(LunariansI18n.get("as_running"));
      return;
    } 
    if (phase == 2) {
      toast(LunariansI18n.get("as_pick_hint"));
      return;
    } 
    if (phase == 1 && Time.time - armedAt < 180.0F) {
      phase = 2;
      centerChosen = false;
      hasArea = false;
      toast(LunariansI18n.get("as_tap_center"));
      return;
    } 
    phase = 1;
    armedAt = Time.time;
    toast(LunariansI18n.get("as_confirm"));
  }
  
  public static void cancel() {
    try {
      Unit unit1 = (Vars.player == null) ? null : Vars.player.unit();
      Unit unit2 = unit1;
      if (unit1 != null && phase == 3)
        for (byte b = 0; b < pending.size; b++)
          unit1.plans().remove(pending.get(b));  
    } catch (Throwable throwable) {}
    reset();
    toast(LunariansI18n.get("as_cancelled"));
  }
  
  private static void update() {
    if (Vars.headless || Vars.state == null || !Vars.state.isGame()) {
      if (phase != 0)
        reset(); 
      return;
    } 
    try {
      if (phase == 1 && Time.time - armedAt > 200.0F) {
        phase = 0;
        toast(LunariansI18n.get("as_timeout"));
        return;
      } 
      if (phase == 2) {
        handleAreaPick();
        return;
      } 
      if (phase == 3)
        runSteps(); 
    } catch (Throwable throwable) {
      if (!errorLogged) {
        errorLogged = true;
        Log.err("[Lunarians] AutoSet error", throwable);
      } 
    } 
  }
  
  private static void handleAreaPick() {
    if (Core.input.keyTap(KeyCode.escape) || Core.input.keyTap(KeyCode.back)) {
      cancel();
      return;
    } 
    if (!Core.input.justTouched() || Core.scene.hasMouse() || Core.scene.hasDialog())
      return; 
    Vec2 vec2 = Core.input.mouseWorld();
    int j = World.toTile(vec2.x);
    int i;
    if (!Vars.world.tiles.in(j, i = World.toTile(vec2.y)))
      return; 
    if (!centerChosen) {
      cx = j;
      cy = i;
      centerChosen = true;
      toast(LunariansI18n.get("as_tap_radius"));
      return;
    } 
    float f = Mathf.dst(cx, cy, j, i);
    if (f < 3.0F) {
      toast(LunariansI18n.get("as_radius_small"));
      return;
    } 
    radius = Math.min(f, 80.0F);
    hasArea = true;
    phase = 3;
    stepIndex = 0;
    runMode = AutoSetConfig.mode();
    oreOrder = AutoSetConfig.oreOrder();
    factoryQueue = AutoSetConfig.factoryQueue();
    feedTargets.clear();
    feedItemNames.clear();
    feedReserved.clear();
    feedLines.clear();
    feedIndex = 0;
    pending.clear();
    used = new boolean[Vars.world.width() * Vars.world.height()];
    nextStepAt = Time.time + 10.0F;
    lastProgress = Time.time;
    lastPending = 0;
    toast(LunariansI18n.get("as_started", String.valueOf((int)radius)));
  }
  
  private static void runSteps() {
    Unit unit1 = (Vars.player == null) ? null : Vars.player.unit();
    Unit unit2 = unit1;
    if (unit1 == null || !unit1.canBuild())
      return; 
    if (Vars.player.closestCore() == null) {
      toast(LunariansI18n.get("as_no_core"));
      reset();
      return;
    } 
    autoMine(unit1);
    if (pending.size > 0) {
      checkPending(unit1);
      return;
    } 
    if (Time.time < nextStepAt)
      return; 
    if (runMode == 0 && AutoSetConfig.steamOn() && steamDone < AutoSetConfig.steamCount()) {
      steamDone++;
      nextStepAt = Time.time + 30.0F;
      startSteamStep(unit1);
      return;
    } 
    if ((runMode == 1) ? (stepIndex >= factoryQueue.length && feedIndex >= feedTargets.size()) : (stepIndex >= oreOrder.length)) {
      reset();
      toast(LunariansI18n.get("as_done"));
      return;
    } 
    if (runMode == 1) {
      if (stepIndex < factoryQueue.length) {
        startFactoryStep(unit1);
      } else {
        startFeedStep(unit1);
      } 
    } else {
      startStep(unit1);
    } 
  }
  
  private static Item itemFor(String paramString) {
    Item item = Vars.content.item(paramString);
    if (item != null)
      return item; 
    if ("copper".equals(paramString))
      return Items.copper; 
    if ("lead".equals(paramString))
      return Items.lead; 
    if ("coal".equals(paramString))
      return Items.coal; 
    if ("sand".equals(paramString))
      return Items.sand; 
    return null;
  }
  
  private static void startStep(Unit paramUnit) {
    Item item = itemFor(oreOrder[stepIndex]);
    stepIndex++;
    nextStepAt = Time.time + 30.0F;
    if (item == null)
      return; 
    boolean bool1 = false;
    boolean bool2 = false;
    AutoDrill.limitOn = true;
    AutoDrill.limitX = cx;
    AutoDrill.limitY = cy;
    AutoDrill.limitR = radius;
    AutoDrill.quiet = true;
    try {
      for (byte b = 0; b < 4 && !bool1; b++) {
        int[] arrayOfInt = findOre(item);
        if (arrayOfInt == null)
          break; 
        bool2 = true;
        markUsed(arrayOfInt[0], arrayOfInt[1], item);
        LunariansSettings.drillStartX = arrayOfInt[0];
        LunariansSettings.drillStartY = arrayOfInt[1];
        setExitTowardCore(arrayOfInt[0], arrayOfInt[1]);
        Seq seq = new Seq();
        for (BuildPlan buildPlan : paramUnit.plans())
          seq.add(buildPlan); 
        int i = AutoDrill.generateWithConveyor();
        if (i > 0) {
          pending.clear();
          for (BuildPlan buildPlan : paramUnit.plans()) {
            if (seq.contains(buildPlan, true))
              continue; 
            pending.add(buildPlan);
          } 
          bool1 = (pending.size > 0) ? true : false;
          boolean bool = bool1;
          if (bool1) {
            lastPending = pending.size;
            lastProgress = Time.time;
            toast(LunariansI18n.get("as_step", item.localizedName, String.valueOf(i)));
          } 
        } 
      } 
    } catch (Throwable throwable) {
      Log.err("[Lunarians] AutoSet step", throwable);
    } finally {
      AutoDrill.isoOverride = -1;
      AutoDrill.quiet = false;
      AutoDrill.limitOn = false;
      LunariansSettings.save();
    } 
    if (!bool1)
      toast(LunariansI18n.get(bool2 ? "as_no_route" : "as_no_ore", item.localizedName)); 
  }
  
  private static void startSteamStep(Unit paramUnit) {
    try {
      Seq<BuildPlan> seq = SteamDrill.plan(paramUnit, cx, cy, radius, used);
      if (seq == null || seq.isEmpty()) {
        steamDone = AutoSetConfig.steamCount();
        toast("Steam Drill: " + SteamDrill.lastError);
        return;
      } 
      Seq seq1 = new Seq();
      for (BuildPlan buildPlan : paramUnit.plans())
        seq1.add(buildPlan); 
      if (used != null)
        for (Integer integer : SteamDrill.lastTiles) {
          if (integer.intValue() >= 0 && integer.intValue() < used.length)
            used[integer.intValue()] = true; 
        }  
      for (byte b = 0; b < seq.size; b++)
        paramUnit.addBuild((BuildPlan)seq.get(b), true); 
      pending.clear();
      for (BuildPlan buildPlan : paramUnit.plans()) {
        if (seq1.contains(buildPlan, true))
          continue; 
        pending.add(buildPlan);
      } 
      lastPending = pending.size;
      lastProgress = Time.time;
      toast("Steam Drill: " + seq.size + tr(" blocos planejados", " blocks planned"));
    } catch (Throwable throwable) {
      Log.err("[Lunarians] steam step", throwable);
    } 
  }
  
  private static String tr(String paramString1, String paramString2) {
    return "pt".equals(LunariansI18n.getLanguage()) ? paramString1 : paramString2;
  }
  
  private static Schematic schemFor(String paramString) {
    Schematic schematic = schemCache.get(paramString);
    if (schematic == null) {
      try {
        schematic = Schematics.readBase64(AutoSetConfig.schematicFor(paramString));
      } catch (Throwable throwable) {
        Log.err("[Lunarians] schematic " + paramString, throwable);
      } 
      if (schematic != null)
        schemCache.put(paramString, schematic); 
    } 
    return schematic;
  }
  
  private static void rotatePlansAround(Seq<BuildPlan> paramSeq, int paramInt1, int paramInt2) {
    for (byte b = 0; b < paramSeq.size; b++) {
      BuildPlan buildPlan = (BuildPlan)paramSeq.get(b);
      if (!buildPlan.breaking && buildPlan.block != null) {
        float f1 = buildPlan.block.offset;
        float f2 = ((buildPlan.x - paramInt1) * 8) + f1;
        float f3 = ((buildPlan.y - paramInt2) * 8) + f1;
        float f4 = -f3;
        float f5 = f2;
        buildPlan.x = Math.round((f4 - f1) / 8.0F) + paramInt1;
        buildPlan.y = Math.round((f5 - f1) / 8.0F) + paramInt2;
        buildPlan.rotation = buildPlan.block.planRotation(Mathf.mod(buildPlan.rotation + 1, 4));
      } 
    } 
  }
  
  private static boolean planFits(Seq<BuildPlan> paramSeq, boolean[] paramArrayOfboolean, HashSet<Integer> paramHashSet) {
    int i = Vars.world.width();
    for (byte b = 0; b < paramSeq.size; ) {
      BuildPlan buildPlan = (BuildPlan)paramSeq.get(b);
      if (buildPlan.block == null)
        return false; 
      int j = buildPlan.block.size;
      int k = (j - 1) / 2;
      for (byte b1 = 0; b1 < j; b1++) {
        for (byte b2 = 0; b2 < j; b2++) {
          int m = buildPlan.x - k + b2;
          int n = buildPlan.y - k + b1;
          if (!Vars.world.tiles.in(m, n) || paramArrayOfboolean[n * i + m])
            return false; 
          paramHashSet.add(Integer.valueOf(n * i + m));
        } 
      } 
      if (Build.validPlace(buildPlan.block, Vars.player.team(), buildPlan.x, buildPlan.y, buildPlan.rotation)) {
        b++;
        continue;
      } 
      return false;
    } 
    return true;
  }
  
  private static int[] findOutput(Seq<BuildPlan> paramSeq, HashSet<Integer> paramHashSet) {
    int i = Vars.world.width();
    for (byte b = 0; b < paramSeq.size; ) {
      BuildPlan buildPlan = (BuildPlan)paramSeq.get(b);
      int j, k, m;
      if (buildPlan.block == null || buildPlan.block.name == null || !buildPlan.block.name.contains("conveyor") || !Vars.world.tiles.in(m = buildPlan.x + DX[k = buildPlan.rotation & 0x3], j = buildPlan.y + DY[k]) || paramHashSet.contains(Integer.valueOf(j * i + m))) {
        b++;
        continue;
      } 
      return new int[] { m, j, k };
    } 
    return null;
  }
  
  private static String[] feedItems(String paramString) {
    if ("silicon".equals(paramString))
      return new String[] { "coal", "sand" }; 
    if ("metaglass".equals(paramString))
      return new String[] { "lead", "sand" }; 
    return new String[] { "coal", null };
  }
  
  private static Block blockByName(String paramString1, String paramString2) {
    Block block = Vars.content.block(paramString1);
    if (block == null && paramString2 != null)
      block = Vars.content.block(paramString2); 
    return block;
  }
  
  private static int genInner() {
    try {
      Method method = AutoDrill.class.getDeclaredMethod("generateInner", new Class[0]);
      method.setAccessible(true);
      return ((Integer)method.invoke(null, new Object[0])).intValue();
    } catch (Throwable throwable) {
      Log.err("[Lunarians] AutoSet genInner", throwable);
      return 0;
    } 
  }
  
  private static Seq<BuildPlan> routeTo(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean[] paramArrayOfboolean, Block paramBlock) {
    int j = Vars.world.width();
    int k = Vars.world.height();
    Team team = Vars.player.team();
    if (paramBlock == null || !freeFor(paramBlock, team, paramInt1, paramInt2, paramArrayOfboolean) || !freeFor(paramBlock, team, paramInt3, paramInt4, paramArrayOfboolean))
      return null; 
    int[] arrayOfInt = new int[j * k];
    Arrays.fill(arrayOfInt, -2);
    ArrayDeque<Integer> arrayDeque = new ArrayDeque();
    int m = paramInt2 * j + paramInt1;
    int n = paramInt4 * j + paramInt3;
    arrayOfInt[m] = -1;
    arrayDeque.add(Integer.valueOf(m));
    byte b2 = 0;
    while (!arrayDeque.isEmpty() && arrayOfInt[n] == -2 && b2++ < '') {
      int i3 = ((Integer)arrayDeque.poll()).intValue();
      int i2 = i3 % j;
      int i1 = i3 / j;
      for (byte b = 0; b < 4; b++) {
        int i6 = i2 + DX[b];
        int i5 = i1 + DY[b];
        int i4;
        if (Vars.world.tiles.in(i6, i5) && arrayOfInt[i4 = i5 * j + i6] == -2 && freeFor(paramBlock, team, i6, i5, paramArrayOfboolean)) {
          arrayOfInt[i4] = i3;
          arrayDeque.add(Integer.valueOf(i4));
        } 
      } 
    } 
    if (arrayOfInt[n] == -2)
      return null; 
    ArrayList<Integer> arrayList = new ArrayList();
    int i = n;
    while (i != -1) {
      arrayList.add(Integer.valueOf(i));
      i = arrayOfInt[i];
    } 
    Collections.reverse(arrayList);
    Seq<BuildPlan> seq = new Seq();
    for (byte b1 = 0; b1 < arrayList.size(); b1++) {
      int i4 = ((Integer)arrayList.get(b1)).intValue();
      int i3 = i4 % j;
      int i2 = i4 / j;
      int i1 = paramInt5;
      if (b1 + 1 < arrayList.size()) {
        int i5 = ((Integer)arrayList.get(b1 + 1)).intValue();
        int i6 = i5 % j;
        int i7 = i5 / j;
        i1 = (i6 > i3) ? 0 : ((i6 < i3) ? 2 : ((i7 > i2) ? 1 : 3));
      } 
      seq.add(new BuildPlan(i3, i2, i1, paramBlock));
    } 
    return seq;
  }
  
  private static boolean freeFor(Block paramBlock, Team paramTeam, int paramInt1, int paramInt2, boolean[] paramArrayOfboolean) {
    if (!Vars.world.tiles.in(paramInt1, paramInt2) || paramArrayOfboolean[paramInt2 * Vars.world.width() + paramInt1])
      return false; 
    Tile tile = Vars.world.tile(paramInt1, paramInt2);
    if (tile == null || tile.build != null)
      return false; 
    return Build.validPlace(paramBlock, paramTeam, paramInt1, paramInt2, 0);
  }
  
  private static void startFeedStep(Unit paramUnit) {
    int[] arrayOfInt = feedTargets.get(feedIndex);
    String str = feedItemNames.get(feedIndex);
    feedIndex++;
    nextStepAt = Time.time + 30.0F;
    Item item = itemFor(str);
    Block block = blockByName("conveyor", "titanium-conveyor");
    if (item == null || block == null)
      return; 
    boolean bool = false;
    AutoDrill.limitOn = true;
    AutoDrill.limitX = cx;
    AutoDrill.limitY = cy;
    AutoDrill.limitR = radius;
    AutoDrill.quiet = true;
    int i = LunariansSettings.drillExitDir;
    try {
      int[] arrayOfInt1;
      for (byte b = 0; b < 6 && !bool && (arrayOfInt1 = findOre(item, arrayOfInt[0], arrayOfInt[1])) != null; b++) {
        markUsed(arrayOfInt1[0], arrayOfInt1[1], item);
        LunariansSettings.drillStartX = arrayOfInt1[0];
        LunariansSettings.drillStartY = arrayOfInt1[1];
        float f1 = (arrayOfInt[0] - arrayOfInt1[0]);
        float f2 = (arrayOfInt[1] - arrayOfInt1[1]);
        boolean bool1 = (Math.abs(f1) >= Math.abs(f2)) ? ((f1 >= 0.0F) ? false : true) : ((f2 >= 0.0F) ? true : true);
        boolean bool2 = (Math.abs(f1) >= Math.abs(f2)) ? ((f2 >= 0.0F) ? true : true) : ((f1 >= 0.0F) ? false : true);
        int[] arrayOfInt2 = { bool1, bool2, bool2 ^ 0x2, bool1 ^ 0x2 };
        boolean[] arrayOfBoolean = AutoDrill.reservedForFeed(paramUnit);
        for (byte b1 = 0; b1 < 4 && !bool; b1++) {
          int j, k = arrayOfInt2[b1];
          AutoDrill.dryRun = true;
          try {
            j = genInner();
          } finally {
            AutoDrill.dryRun = false;
          } 
          if (j <= 0)
            break; 
          if (AutoDrill.lastHub >= 0) {
            Seq seq = new Seq();
            for (byte b2 = 0; b2 < AutoDrill.lastPlans.size; b2++)
              seq.add(AutoDrill.lastPlans.get(b2)); 
            boolean[] arrayOfBoolean1 = (boolean[])arrayOfBoolean.clone();
            int n;
            for (n = 0; n < seq.size; n++)
              AutoConnectConveyor.markPlan((BuildPlan)seq.get(n), arrayOfBoolean1); 
            n = AutoDrill.lastHub % Vars.world.width() + DX[k];
            int m;
            Seq<BuildPlan> seq1 = routeTo(n, m = AutoDrill.lastHub / Vars.world.width() + DY[k], arrayOfInt[0], arrayOfInt[1], arrayOfInt[2], arrayOfBoolean1, block);
            if (seq1 != null) {
              Seq seq2 = new Seq();
              for (BuildPlan buildPlan : paramUnit.plans())
                seq2.add(buildPlan); 
              byte b3;
              for (b3 = 0; b3 < seq.size; b3++)
                paramUnit.addBuild((BuildPlan)seq.get(b3), true); 
              for (b3 = 0; b3 < seq1.size; b3++)
                paramUnit.addBuild((BuildPlan)seq1.get(b3), true); 
              pending.clear();
              for (BuildPlan buildPlan : paramUnit.plans()) {
                if (seq2.contains(buildPlan, true))
                  continue; 
                pending.add(buildPlan);
              } 
              lastPending = pending.size;
              lastProgress = Time.time;
              bool = true;
              ArrayList<int[]> arrayList = feedLines.get(str);
              if (arrayList == null) {
                arrayList = new ArrayList();
                feedLines.put(str, arrayList);
              } 
              for (byte b4 = 0; b4 < seq1.size; b4++) {
                BuildPlan buildPlan = (BuildPlan)seq1.get(b4);
                if (buildPlan.block instanceof mindustry.world.blocks.distribution.Conveyor)
                  arrayList.add(new int[] { buildPlan.x, buildPlan.y }); 
              } 
              toast(item.localizedName + tr(" → entrada da fábrica: ", " → factory input: ") + j + " drills + " + seq1.size + tr(" esteiras", " conveyors"));
            } 
          } 
        } 
      } 
      if (!bool)
        bool = tapLine(paramUnit, str, arrayOfInt); 
    } catch (Throwable throwable) {
      Log.err("[Lunarians] AutoSet feed step", throwable);
    } finally {
      AutoDrill.isoOverride = -1;
      AutoDrill.quiet = false;
      AutoDrill.limitOn = false;
      AutoDrill.dryRun = false;
      LunariansSettings.drillExitDir = i;
      LunariansSettings.save();
    } 
    if (!bool)
      toast(tr("Sem minério/rota para ", "No ore/route for ") + item.localizedName); 
  }
  
  private static boolean tapLine(Unit paramUnit, String paramString, int[] paramArrayOfint) {
    ArrayList arrayList = feedLines.get(paramString);
    Block block = blockByName("conveyor", "titanium-conveyor");
    if (arrayList == null || arrayList.isEmpty() || block == null)
      return false; 
    HashSet<Integer> hashSet = new HashSet();
    int i = Vars.world.width();
    for (int[] arrayOfInt : arrayList)
      hashSet.add(Integer.valueOf(arrayOfInt[1] * i + arrayOfInt[0])); 
    boolean[] arrayOfBoolean = AutoDrill.reservedForFeed(paramUnit);
    for (byte b1 = 0; b1 < feedReserved.size(); b1++)
      arrayOfBoolean[((Integer)feedReserved.get(b1)).intValue()] = true; 
    Seq seq = new Seq();
    for (int[] arrayOfInt : arrayList) {
      Tile tile = Vars.world.tile(arrayOfInt[0], arrayOfInt[1]);
      if (tile == null || tile.build == null || !(tile.build.block instanceof mindustry.world.blocks.distribution.Conveyor))
        continue; 
      boolean bool = true;
      for (byte b = 0; b < 4 && bool; b++) {
        Tile tile1 = Vars.world.tile(arrayOfInt[0] + DX[b], arrayOfInt[1] + DY[b]);
        if (tile1 != null && tile1.build != null && !hashSet.contains(Integer.valueOf(tile1.y * i + tile1.x)))
          bool = false; 
      } 
      if (!bool)
        continue; 
      seq.add(new int[] { arrayOfInt[0], arrayOfInt[1], (int)(Mathf.dst(arrayOfInt[0], arrayOfInt[1], paramArrayOfint[0], paramArrayOfint[1]) * 10.0F) });
    } 
    seq.sort(new Comparator<int[]>() {
          public int compare(int[] param1ArrayOfint1, int[] param1ArrayOfint2) {
            return param1ArrayOfint1[2] - param1ArrayOfint2[2];
          }
        });
    for (byte b2 = 0; b2 < seq.size && b2 < 14; b2++) {
      int[] arrayOfInt = (int[])seq.get(b2);
      for (byte b = 0; b < 4; ) {
        Seq<BuildPlan> seq1 = routeTo(arrayOfInt[0] + DX[b], arrayOfInt[1] + DY[b], paramArrayOfint[0], paramArrayOfint[1], paramArrayOfint[2], arrayOfBoolean, block);
        if (seq1 == null) {
          b++;
          continue;
        } 
        Seq seq2 = new Seq();
        for (BuildPlan buildPlan : paramUnit.plans())
          seq2.add(buildPlan); 
        paramUnit.addBuild(new BuildPlan(arrayOfInt[0], arrayOfInt[1], 0, Blocks.router), true);
        for (byte b3 = 0; b3 < seq1.size; b3++)
          paramUnit.addBuild((BuildPlan)seq1.get(b3), true); 
        pending.clear();
        for (BuildPlan buildPlan : paramUnit.plans()) {
          if (seq2.contains(buildPlan, true))
            continue; 
          pending.add(buildPlan);
        } 
        lastPending = pending.size;
        lastProgress = Time.time;
        toast(tr("Sem outra fonte livre: router em " + arrayOfInt[0] + "," + arrayOfInt[1], "No other free source: router at " + arrayOfInt[0] + "," + arrayOfInt[1]));
        return true;
      } 
    } 
    return false;
  }
  
  private static int[] oreDistGrid(Item paramItem, int paramInt) {
    int j = paramInt * 2 + 1;
    int[] arrayOfInt = new int[j * j];
    Arrays.fill(arrayOfInt, -1);
    if (paramItem == null)
      return arrayOfInt; 
    IntQueue intQueue = new IntQueue();
    int k = cx - paramInt;
    int m = cy - paramInt;
    int n = Vars.world.width();
    int i;
    for (i = 0; i < j; i++) {
      for (byte b = 0; b < j; b++) {
        int i2 = k + b;
        int i1 = m + i;
        Tile tile;
        if (Vars.world.tiles.in(i2, i1) && inCircle(i2, i1) && !used[i1 * n + i2] && (tile = Vars.world.tile(i2, i1)) != null && tile.build == null && !tile.solid() && sameOre(tile, paramItem)) {
          arrayOfInt[i * j + b] = 0;
          intQueue.addLast(i * j + b);
        } 
      } 
    } 
    while (!intQueue.isEmpty()) {
      i = intQueue.removeFirst();
      int i3 = i % j;
      int i2 = i / j;
      int i1 = arrayOfInt[i];
      if (i1 >= 60)
        continue; 
      for (byte b = 0; b < 4; b++) {
        int i4 = i3 + DX[b];
        int i5 = i2 + DY[b];
        if (i4 >= 0 && i5 >= 0 && i4 < j && i5 < j && arrayOfInt[i5 * j + i4] < 0) {
          arrayOfInt[i5 * j + i4] = i1 + 1;
          intQueue.addLast(i5 * j + i4);
        } 
      } 
    } 
    return arrayOfInt;
  }
  
  private static int oreScore(int[][] paramArrayOfint, int paramInt1, int paramInt2, int paramInt3) {
    int i = paramInt3 * 2 + 1;
    int j = paramInt1 - cx - paramInt3;
    int k = paramInt2 - cy - paramInt3;
    int m = 0;
    for (byte b = 0; b < paramArrayOfint.length; b++) {
      if (paramArrayOfint[b] != null) {
        boolean bool = (j >= 0 && k >= 0 && j < i && k < i) ? paramArrayOfint[b][k * i + j] : true;
        m += bool ? 60 : bool;
      } 
    } 
    return m;
  }
  
  private static boolean overlayOre(Tile paramTile, Item paramItem) {
    return (paramTile != null && paramItem != null && paramTile.overlay() != null && (paramTile.overlay()).itemDrop == paramItem);
  }
  
  private static void startFactoryStep(Unit paramUnit) {
    String str = factoryQueue[stepIndex];
    stepIndex++;
    nextStepAt = Time.time + 30.0F;
    Schematic schematic = schemFor(str);
    if (schematic == null || Vars.player.closestCore() == null) {
      toast(tr("Schematic inválido: ", "Invalid schematic: ") + str);
      return;
    } 
    int i = Vars.player.closestCore().tileX();
    int j = Vars.player.closestCore().tileY();
    int k = Vars.world.width();
    int m = (int)Math.ceil(radius);
    String[] arrayOfString = feedItems(str);
    Item item1 = (arrayOfString[0] == null) ? null : itemFor(arrayOfString[0]);
    Item item2 = (arrayOfString[1] == null) ? null : itemFor(arrayOfString[1]);
    int[][] arrayOfInt = { (item1 == null) ? null : oreDistGrid(item1, m), (item2 == null) ? null : oreDistGrid(item2, m) };
    ArrayList<int[]> arrayList = new ArrayList();
    for (int n = cy - m; n <= cy + m; n++) {
      for (int i1 = cx - m; i1 <= cx + m; i1++) {
        if (Vars.world.tiles.in(i1, n) && inCircle(i1, n))
          arrayList.add(new int[] { i1, n, (int)(Mathf.dst(i1, n, cx, cy) * 20.0F) + oreScore(arrayOfInt, i1, n, m) * 250 }); 
      } 
    } 
    Collections.sort(arrayList, (Comparator)new Comparator<int[]>() {
          public int compare(int[] param1ArrayOfint1, int[] param1ArrayOfint2) {
            return param1ArrayOfint1[2] - param1ArrayOfint2[2];
          }
        });
    boolean[] arrayOfBoolean = AutoConnectConveyor.reservedFromPlans(paramUnit);
    byte b1;
    for (b1 = 0; b1 < feedReserved.size(); b1++)
      arrayOfBoolean[((Integer)feedReserved.get(b1)).intValue()] = true; 
    for (byte b2 = 0; b2 < 3; b2++) {
      b1 = 0;
      for (byte b = 0; b < arrayList.size() && b1 < 12; b++) {
        int[] arrayOfInt2 = arrayList.get(b);
        float f1 = (i - arrayOfInt2[0]);
        float f2 = (j - arrayOfInt2[1]);
        byte b3 = (Math.abs(f1) >= Math.abs(f2)) ? ((f1 >= 0.0F) ? 0 : 2) : ((f2 >= 0.0F) ? 1 : 3);
        int i1 = b3 - 1 & 0x3;
        Seq<BuildPlan> seq = Vars.schematics.toPlans(schematic, arrayOfInt2[0], arrayOfInt2[1]);
        for (byte b4 = 0; b4 < i1; b4++)
          rotatePlansAround(seq, arrayOfInt2[0], arrayOfInt2[1]); 
        HashSet<Integer> hashSet = new HashSet();
        int[] arrayOfInt1;
        if (planFits(seq, arrayOfBoolean, hashSet) && (arrayOfInt1 = findOutput(seq, hashSet)) != null) {
          int i2 = (Vars.player.closestCore()).block.size / 2 + ((b2 >= 2) ? Math.min(AutoSetConfig.coreGap(), 2) : AutoSetConfig.coreGap());
          boolean bool = false;
          for (Integer integer : hashSet) {
            int i4 = integer.intValue() % k;
            int i3 = integer.intValue() / k;
            if (Math.max(Math.abs(i4 - i), Math.abs(i3 - j)) > i2)
              continue; 
            bool = true;
          } 
          if (!bool) {
            boolean bool1 = false;
            label225: for (Integer integer : hashSet) {
              int i4 = integer.intValue() % k;
              int i3 = integer.intValue() / k;
              for (byte b5 = -1; b5 <= 1; b5++) {
                for (byte b6 = -1; b6 <= 1; ) {
                  Tile tile = Vars.world.tile(i4 + b6, i3 + b5);
                  if (!overlayOre(tile, item1) && !overlayOre(tile, item2)) {
                    b6++;
                    continue;
                  } 
                  bool1 = true;
                  break label225;
                } 
              } 
            } 
            if (!bool1 || b2 >= 2) {
              byte b6 = (b2 == 0) ? AutoSetConfig.facGap() : ((b2 == 1) ? Math.min(AutoSetConfig.facGap(), 1) : 0);
              byte b5 = (b2 == 0) ? AutoSetConfig.wallGap() : ((b2 == 1) ? Math.min(AutoSetConfig.wallGap(), 1) : 0);
              int i3 = Math.max(b6, b5);
              boolean bool2 = false;
              label230: for (Integer integer : hashSet) {
                int i6 = integer.intValue() % k;
                int i5 = integer.intValue() / k;
                for (int i4 = -i3; i4 <= i3; i4++) {
                  for (int i7 = -i3; i7 <= i3; i7++) {
                    int i9 = i6 + i7;
                    int i8 = i5 + i4;
                    if (Vars.world.tiles.in(i9, i8) && !hashSet.contains(Integer.valueOf(i8 * k + i9))) {
                      Tile tile = Vars.world.tile(i9, i8);
                      boolean bool4 = (Math.abs(i7) <= b5 && Math.abs(i4) <= b5) ? true : false;
                      boolean bool3 = (Math.abs(i7) <= b6 && Math.abs(i4) <= b6) ? true : false, bool5 = bool3;
                      if ((bool4 && tile != null && tile.build == null && tile.solid()) || (bool3 && (arrayOfBoolean[i8 * k + i9] || (tile != null && tile.build != null && !(tile.build.block instanceof CoreBlock))))) {
                        bool2 = true;
                        break label230;
                      } 
                    } 
                  } 
                } 
              } 
              if (!bool2) {
                b1++;
                boolean[] arrayOfBoolean1 = (boolean[])arrayOfBoolean.clone();
                for (byte b7 = 0; b7 < seq.size; b7++)
                  AutoConnectConveyor.markPlan((BuildPlan)seq.get(b7), arrayOfBoolean1); 
                Seq<BuildPlan> seq1 = AutoConnectConveyor.planRoute(arrayOfInt1[0], arrayOfInt1[1], arrayOfInt1[2], arrayOfBoolean1);
                if (seq1 == null || seq1.isEmpty())
                  seq1 = AutoConnectConveyor.planRoute(arrayOfInt1[0], arrayOfInt1[1], -1, arrayOfBoolean1); 
                if (seq1 != null && !seq1.isEmpty()) {
                  if (AutoSetConfig.facDrill()) {
                    String[] arrayOfString1 = feedItems(str);
                    byte b10 = 0;
                    for (byte b9 = 0; b9 < seq.size && b10 < 2; b9++) {
                      Position position = (Position)seq.get(b9);
                      int i4, i5, i6;
                      if (((BuildPlan)position).block != null && ((BuildPlan)position).block.name != null && ((BuildPlan)position).block.name.contains("conveyor") && Vars.world.tiles.in(i4 = ((BuildPlan)position).x - DX[i5 = ((BuildPlan)position).rotation & 0x3], i6 = ((BuildPlan)position).y - DY[i5]) && !hashSet.contains(Integer.valueOf(i6 * k + i4))) {
                        String str1 = arrayOfString1[b10];
                        b10++;
                        if (str1 != null) {
                          feedTargets.add(new int[] { i4, i6, i5 });
                          feedItemNames.add(str1);
                          for (byte b11 = 0; b11 < 3; b11++) {
                            int i7 = i4 - b11 * DX[i5];
                            int i8 = i6 - b11 * DY[i5];
                            if (Vars.world.tiles.in(i7, i8))
                              feedReserved.add(Integer.valueOf(i8 * k + i7)); 
                          } 
                        } 
                      } 
                    } 
                  } 
                  Seq seq2 = new Seq();
                  for (BuildPlan buildPlan : paramUnit.plans())
                    seq2.add(buildPlan); 
                  byte b8;
                  for (b8 = 0; b8 < seq.size; b8++)
                    paramUnit.addBuild((BuildPlan)seq.get(b8), true); 
                  for (b8 = 0; b8 < seq1.size; b8++)
                    paramUnit.addBuild((BuildPlan)seq1.get(b8), true); 
                  pending.clear();
                  for (BuildPlan buildPlan : paramUnit.plans()) {
                    if (seq2.contains(buildPlan, true))
                      continue; 
                    pending.add(buildPlan);
                  } 
                  lastPending = pending.size;
                  lastProgress = Time.time;
                  toast(tr("Fábrica ", "Factory ") + str + ": " + seq.size + "+" + seq1.size + tr(" blocos planejados", " blocks planned"));
                  return;
                } 
              } 
            } 
          } 
        } 
      } 
    } 
    toast(tr("Sem espaço/rota para a fábrica: ", "No space/route for factory: ") + str);
  }
  
  private static void checkPending(Unit paramUnit) {
    if (Time.time < nextCheck)
      return; 
    nextCheck = Time.time + 15.0F;
    rememberOurs();
    int i;
    for (i = pending.size - 1; i >= 0; i--) {
      BuildPlan buildPlan = (BuildPlan)pending.get(i);
      Tile tile = Vars.world.tile(buildPlan.x, buildPlan.y);
      if (tile == null || tile.block() == buildPlan.block)
        pending.remove(i); 
    } 
    if (pending.isEmpty()) {
      nextStepAt = Time.time + 15.0F;
      return;
    } 
    if (pending.size < lastPending)
      lastProgress = Time.time; 
    lastPending = pending.size;
    for (i = pending.size - 1; i >= 0; i--) {
      BuildPlan buildPlan = (BuildPlan)pending.get(i);
      if (!paramUnit.plans().contains(buildPlan, true))
        if (Build.validPlace(buildPlan.block, Vars.player.team(), buildPlan.x, buildPlan.y, buildPlan.rotation)) {
          paramUnit.addBuild(buildPlan, true);
        } else if (!tryDelete(paramUnit, buildPlan)) {
          pending.remove(i);
        }  
    } 
    if (pending.isEmpty()) {
      nextStepAt = Time.time + 30.0F;
      return;
    } 
    if (Time.time - lastProgress > 360.0F) {
      String str = missingResource();
      if (str != null && Time.time - lastProgress < 5400.0F) {
        if (Time.time - lastWarn > 300.0F) {
          lastWarn = Time.time;
          toast(tr("Aguardando recurso: ", "Waiting for resource: ") + str);
        } 
        return;
      } 
      int j = pending.size;
      pending.clear();
      nextStepAt = Time.time + 20.0F;
      toast(LunariansI18n.get("as_stalled") + " → skip (" + j + ")");
      return;
    } 
    if (Time.time - lastProgress > 180.0F && Time.time - lastWarn > 180.0F) {
      lastWarn = Time.time;
      BuildPlan buildPlan = nearestPending(paramUnit);
      if (buildPlan != null && !paramUnit.within(buildPlan.drawx(), buildPlan.drawy(), 220.0F)) {
        toast(LunariansI18n.get("as_approach", buildPlan.x + "," + buildPlan.y));
      } else {
        toast(LunariansI18n.get("as_stalled"));
      } 
    } 
  }
  
  private static final HashMap<Integer, Integer> ours = new HashMap<>();
  
  private static final Set<BuildPlan> seenPlans = Collections.newSetFromMap(new IdentityHashMap<>());
  
  private static final HashSet<Integer> deleted = new HashSet<>();
  
  private static int deleteCount;
  
  private static void rememberOurs() {
    int i = Vars.world.width();
    for (byte b = 0; b < pending.size; b++) {
      BuildPlan buildPlan = (BuildPlan)pending.get(b);
      if (buildPlan != null && buildPlan.block != null && !buildPlan.breaking && seenPlans.add(buildPlan)) {
        int j = (buildPlan.block.size - 1) / 2;
        for (byte b1 = 0; b1 < buildPlan.block.size; b1++) {
          for (byte b2 = 0; b2 < buildPlan.block.size; b2++)
            ours.merge(Integer.valueOf((buildPlan.y - j + b1) * i + buildPlan.x - j + b2), Integer.valueOf(1), Integer::sum); 
        } 
      } 
    } 
  }
  
  private static boolean deletable(Block paramBlock) {
    return (paramBlock instanceof mindustry.world.blocks.distribution.Conveyor || paramBlock instanceof mindustry.world.blocks.distribution.Junction || paramBlock instanceof mindustry.world.blocks.distribution.ItemBridge || paramBlock instanceof mindustry.world.blocks.distribution.Router || paramBlock instanceof mindustry.world.blocks.distribution.Sorter || paramBlock instanceof mindustry.world.blocks.distribution.OverflowGate || paramBlock instanceof mindustry.world.blocks.production.Drill || paramBlock instanceof mindustry.world.blocks.power.PowerNode || paramBlock instanceof mindustry.world.blocks.liquid.Conduit);
  }
  
  private static boolean tryDelete(Unit paramUnit, BuildPlan paramBuildPlan) {
    try {
      if (!AutoSetConfig.autoDelete() || deleteCount >= 30 || paramBuildPlan.block == null || paramBuildPlan.breaking)
        return false; 
      int i = Vars.world.width();
      int j = (paramBuildPlan.block.size - 1) / 2;
      boolean bool = false;
      for (byte b = 0; b < paramBuildPlan.block.size; b++) {
        for (byte b1 = 0; b1 < paramBuildPlan.block.size; b1++) {
          int k = paramBuildPlan.x - j + b1;
          int m = paramBuildPlan.y - j + b;
          Tile tile = Vars.world.tile(k, m);
          if (tile != null && tile.build != null && tile.build.team == Vars.player.team() && tile.build.block != paramBuildPlan.block) {
            Block block = tile.build.block;
            if (!(block instanceof mindustry.world.blocks.ConstructBlock) && !(block instanceof CoreBlock) && deletable(block)) {
              Integer integer = ours.get(Integer.valueOf(m * i + k));
              if (integer != null && integer.intValue() >= 2 && !deleted.contains(Integer.valueOf(tile.build.pos()))) {
                deleted.add(Integer.valueOf(tile.build.pos()));
                deleteCount++;
                paramUnit.addBuild(new BuildPlan(tile.build.tileX(), tile.build.tileY()), true);
                bool = true;
              } 
            } 
          } 
        } 
      } 
      if (bool)
        toast(tr("Auto delete: removendo bloco que atrapalha (" + deleteCount + "/30)", "Auto delete: removing blocking block (" + deleteCount + "/30)")); 
      return bool;
    } catch (Throwable throwable) {
      return false;
    } 
  }
  
  private static String missingResource() {
    try {
      if (Vars.state.rules.infiniteResources || Vars.player == null || Vars.player.closestCore() == null)
        return null; 
      float f = Vars.state.rules.buildCostMultiplier;
      for (byte b = 0; b < pending.size; b++) {
        BuildPlan buildPlan = (BuildPlan)pending.get(b);
        if (buildPlan != null && buildPlan.block != null && !buildPlan.breaking) {
          Tile tile = Vars.world.tile(buildPlan.x, buildPlan.y);
          if (tile == null || tile.block() != buildPlan.block) {
            ItemStack[] arrayOfItemStack;
            int i;
            byte b1;
            for (arrayOfItemStack = buildPlan.block.requirements, i = arrayOfItemStack.length, b1 = 0; b1 < i; ) {
              ItemStack itemStack = arrayOfItemStack[b1];
              if ((Vars.player.closestCore()).items.get(itemStack.item) >= Math.ceil((itemStack.amount * f))) {
                b1++;
                continue;
              } 
              return itemStack.item.localizedName;
            } 
          } 
        } 
      } 
    } catch (Throwable throwable) {}
    return null;
  }
  
  private static BuildPlan nearestPending(Unit paramUnit) {
    BuildPlan buildPlan = null;
    float f = Float.MAX_VALUE;
    for (byte b = 0; b < pending.size; b++) {
      BuildPlan buildPlan1 = (BuildPlan)pending.get(b);
      float f1 = Mathf.dst(paramUnit.x, paramUnit.y, buildPlan1.drawx(), buildPlan1.drawy());
      if (f1 < f) {
        f = f1;
        buildPlan = buildPlan1;
      } 
    } 
    return buildPlan;
  }
  
  private static void setExitTowardCore(int paramInt1, int paramInt2) {
    try {
      if (Vars.player == null || Vars.player.closestCore() == null)
        return; 
      float f1 = (Vars.player.closestCore().tileX() - paramInt1);
      float f2 = (Vars.player.closestCore().tileY() - paramInt2);
      LunariansSettings.drillExitDir = (Math.abs(f1) >= Math.abs(f2)) ? ((f1 >= 0.0F) ? 0 : 2) : ((f2 >= 0.0F) ? 1 : 3);
    } catch (Throwable throwable) {}
  }
  
  private static boolean inCircle(int paramInt1, int paramInt2) {
    return (Mathf.dst(paramInt1, paramInt2, cx, cy) <= radius);
  }
  
  private static int[] findOre(Item paramItem) {
    return findOre(paramItem, cx, cy);
  }
  
  private static int[] findMarked(Item paramItem, int paramInt1, int paramInt2) {
    int[] arrayOfInt = AutoEconomy.markedTiles(paramItem);
    if (arrayOfInt.length == 0 || used == null)
      return null; 
    int i = Vars.world.width();
    int j = -1;
    int k = -1;
    int m = Integer.MIN_VALUE;
    for (int n : arrayOfInt) {
      if (n >= 0 && n < used.length && !used[n]) {
        int i1 = n % i;
        int i2 = n / i;
        Tile tile = Vars.world.tile(i1, i2);
        if (tile != null && tile.build == null && !tile.solid() && sameOre(tile, paramItem)) {
          int i3 = 1000 - (int)Mathf.dst(i1, i2, paramInt1, paramInt2);
          for (byte b = 0; b < 8; b++) {
            int i4 = i1 + DX[b];
            int i5 = i2 + DY[b];
            if (Vars.world.tiles.in(i4, i5) && !used[i5 * i + i4] && sameOre(Vars.world.tile(i4, i5), paramItem))
              i3 += 5; 
          } 
          if (i3 > m) {
            m = i3;
            j = i1;
            k = i2;
          } 
        } 
      } 
    } 
    (new int[2])[0] = j;
    (new int[2])[1] = k;
    return (j < 0) ? null : new int[2];
  }
  
  private static int[] findOre(Item paramItem, int paramInt1, int paramInt2) {
    int[] arrayOfInt1 = findMarked(paramItem, paramInt1, paramInt2);
    if (arrayOfInt1 != null) {
      AutoDrill.isoOverride = 1;
      return arrayOfInt1;
    } 
    AutoDrill.isoOverride = -1;
    int[] arrayOfInt2 = findOreM(paramItem, paramInt1, paramInt2, AutoSetConfig.spacing() + 1);
    if (arrayOfInt2 == null && (arrayOfInt2 = findOreM(paramItem, paramInt1, paramInt2, 1)) != null)
      AutoDrill.isoOverride = 1; 
    return arrayOfInt2;
  }
  
  private static int[] findOreM(Item paramItem, int paramInt1, int paramInt2, int paramInt3) {
    int i = Vars.world.width();
    int j = (int)Math.ceil(radius);
    int k = -1;
    int m = -1;
    int n = Integer.MIN_VALUE;
    for (int i1 = cy - j; i1 <= cy + j; i1++) {
      for (int i2 = cx - j; i2 <= cx + j; i2++) {
        Tile tile;
        if (Vars.world.tiles.in(i2, i1) && inCircle(i2, i1) && !used[i1 * i + i2] && (tile = Vars.world.tile(i2, i1)) != null && tile.build == null && !tile.solid() && sameOre(tile, paramItem) && !AutoDrill.crowded(i2, i1, 1, paramInt3)) {
          int i3 = 1000 - (int)Mathf.dst(i2, i1, paramInt1, paramInt2);
          for (byte b = 0; b < 8; b++) {
            int i4 = i2 + DX[b];
            int i5 = i1 + DY[b];
            if (Vars.world.tiles.in(i4, i5) && !used[i5 * i + i4] && sameOre(Vars.world.tile(i4, i5), paramItem))
              i3 += 5; 
          } 
          if (i3 > n) {
            n = i3;
            k = i2;
            m = i1;
          } 
        } 
      } 
    } 
    if (k < 0)
      return null; 
    return new int[] { k, m };
  }
  
  private static void markUsed(int paramInt1, int paramInt2, Item paramItem) {
    int i = Vars.world.width();
    int j = Math.max(16, LunariansSettings.drillMaxTiles);
    IntQueue intQueue = new IntQueue();
    boolean[] arrayOfBoolean = new boolean[i * Vars.world.height()];
    int k = paramInt2 * i + paramInt1;
    intQueue.addLast(k);
    arrayOfBoolean[k] = true;
    byte b = 0;
    while (!intQueue.isEmpty() && b < j) {
      int m = intQueue.removeFirst();
      used[m] = true;
      b++;
      int n = m % i;
      int i1 = m / i;
      for (byte b1 = 0; b1 < 4; b1++) {
        int i3 = n + DX[b1];
        int i4 = i1 + DY[b1];
        int i2;
        if (Vars.world.tiles.in(i3, i4) && !arrayOfBoolean[i2 = i4 * i + i3] && inCircle(i3, i4) && sameOre(Vars.world.tile(i3, i4), paramItem) && AutoDrill.inFloor(i3, i4, paramInt1, paramInt2)) {
          arrayOfBoolean[i2] = true;
          intQueue.addLast(i2);
        } 
      } 
    } 
  }
  
  private static boolean sameOre(Tile paramTile, Item paramItem) {
    if (paramTile == null || paramItem == null)
      return false; 
    try {
      if (paramTile.drop() != null)
        return (paramTile.drop() == paramItem); 
    } catch (Throwable throwable) {}
    Floor floor = (paramTile.overlay() != null) ? paramTile.overlay() : paramTile.floor();
    return (floor != null && floor.itemDrop == paramItem);
  }
  
  private static void draw() {
    if (phase != 2 && !hasArea)
      return; 
    if (Vars.state == null || !Vars.state.isGame() || cx == Integer.MIN_VALUE)
      return; 
    try {
      float f1 = 8.0F;
      float f2 = cx * f1;
      float f3 = cy * f1;
      if (phase == 2) {
        if (!centerChosen)
          return; 
        Vec2 vec2 = Core.input.mouseWorld();
        float f = Mathf.dst(f2, f3, vec2.x, vec2.y);
        f = Math.min(f, 80.0F * f1);
        Draw.color(Color.cyan, 0.15F);
        Fill.circle(f2, f3, f);
        Draw.color(Color.cyan, 0.9F);
        Lines.stroke(2.0F);
        Lines.circle(f2, f3, f);
        Draw.reset();
        return;
      } 
      float f4 = radius * f1;
      Draw.color(Color.orange, 0.12F);
      Fill.circle(f2, f3, f4);
      Draw.color(Color.orange, 0.85F);
      Lines.stroke(2.0F);
      Lines.circle(f2, f3, f4);
      Draw.color(Color.yellow, 0.9F);
      Lines.stroke(1.0F);
      for (byte b = 0; b < pending.size; b++) {
        BuildPlan buildPlan = (BuildPlan)pending.get(b);
        Lines.square(buildPlan.drawx(), buildPlan.drawy(), 4.0F);
      } 
      Draw.reset();
    } catch (Throwable throwable) {}
  }
  
  private static void toast(String paramString) {
    if (Vars.ui != null)
      Vars.ui.showInfoToast(paramString, 3.0F); 
  }
  
  static {
    feedLines = new HashMap<>();
    schemCache = new HashMap<>();
    feedTargets = (ArrayList)new ArrayList<>();
    feedItemNames = new ArrayList<>();
    feedReserved = new ArrayList<>();
    cx = Integer.MIN_VALUE;
    pending = new Seq();
  }
}
