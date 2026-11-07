package lunarians;

import arc.Core;
import arc.Events;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.math.geom.Position;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.core.World;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.game.Schematic;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.blocks.power.PowerGenerator;
import mindustry.world.blocks.power.PowerGraph;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.storage.StorageBlock;

public class AutoDock {
  static final String[][] ORDER = new String[][] { 
      { "Field", "GP!3|GP!4|GP!2", "", "F", "coal" }, { "Field", "DrM!2|DrM!1|DrM!3", "", "F", "copper" }, { "Field", "DrM!3|DrM!4|DrM!1", "", "F", "lead" }, { "Early", "SP_GP10|GP!9", "coal:100" }, { "Early", "SP_SS23|SS24", "coal:100;sand:200" }, { "Early", "K17|K!2", "lead:200;sand:100" }, { "Medium", "PM!9", "coal:100;lead:100;sand:100" }, { "Medium", "MP17", "coal:300" }, { "Medium", "T2!4|T2!6", "silicon:300;graphite:100;lead:200;titanium:75", "U" }, { "Late", "PDG!3", "pyratite:100;titanium:100;coal:100;lead:100;sand:100" }, 
      { "Late", "SP_PW16", "thorium:100;sand:300" }, { "Late", "PC17DS", "titanium:300;sand:300" }, { "Late", "SP_SC21", "coal:200;sand:300;lead:100" }, { "Late", "T3!3", "silicon:800;titanium:500;metaglass:200;graphite:300" }, { "End", "SP_SUS4", "copper:300;lead:300;titanium:300;silicon:300" }, { "End", "SP_T45", "silicon:1500;titanium:1500;metaglass:300;plastanium:300;graphite:300" } };
  
  private static int energyFails = 0;
  
  private static int energyBuilt = 0;
  
  private static boolean unitsOnly = false;
  
  private static boolean unitsFinished = false;
  
  private static boolean[] done = new boolean[ORDER.length];
  
  private static Phase phase = Phase.NEXT;
  
  private static int idx = 0;
  
  private static Seq<BuildPlan> current = new Seq();
  
  private static float centerX;
  
  private static float centerY;
  
  private static float phaseStart;
  
  private static float nextCheck;
  
  public static void startUnits() {
    unitsOnly = true;
    unitsFinished = false;
    phase = Phase.NEXT;
    nextCheck = 0.0F;
  }
  
  public static boolean isUnitsMode() {
    return unitsOnly;
  }
  
  public static boolean unitsDone() {
    return unitsFinished;
  }
  
  public static void stopUnits() {
    unitsOnly = false;
    unitsFinished = false;
    releaseCamera();
    current = new Seq();
    phase = Phase.NEXT;
    nextCheck = Time.time + 60.0F;
  }
  
  public static void finishEconomy() {
    unitsOnly = false;
    unitsFinished = false;
    phase = Phase.NEXT;
    nextCheck = Time.time + 60.0F;
  }
  
  private static int pick() {
    for (byte b = 0; b < ORDER.length; ) {
      if (done[b] || (unitsOnly && ((ORDER[b]).length <= 3 || !"U".equals(ORDER[b][3])))) {
        b++;
        continue;
      } 
      return b;
    } 
    return -1;
  }
  
  public static void init() {
    Events.on(EventType.WorldLoadEvent.class, paramWorldLoadEvent -> reset());
    Events.run(EventType.Trigger.update, AutoDock::update);
  }
  
  public static void reset() {
    releaseCamera();
    done = new boolean[ORDER.length];
    unitsOnly = false;
    unitsFinished = false;
    phase = Phase.NEXT;
    idx = 0;
    current.clear();
    energyStep = false;
    energyFails = 0;
    energyBuilt = 0;
    stuckRetries = 0;
    nextCheck = 0.0F;
  }
  
  public static String coreInfo() {
    CoreBlock.CoreBuild coreBuild1 = (Vars.player == null) ? null : Vars.player.closestCore(), coreBuild2 = coreBuild1;
    if (coreBuild1 == null)
      return "sem nucleo"; 
    int i = freePerimeter(coreBuild1);
    return coreBuild1.block.localizedName + " " + coreBuild1.block.size + "x" + coreBuild1.block.size + " | capacidade " + coreBuild1.storageCapacity + " | lados livres: " + i + " tiles";
  }
  
  private static int freePerimeter(CoreBlock.CoreBuild paramCoreBuild) {
    byte b = 0;
    int[] arrayOfInt = rect(paramCoreBuild.tile.x, paramCoreBuild.tile.y, paramCoreBuild.block.size);
    for (int i = arrayOfInt[0] - 1; i <= arrayOfInt[2] + 1; i++) {
      for (int j = arrayOfInt[1] - 1; j <= arrayOfInt[3] + 1; j++) {
        boolean bool1 = (((i >= arrayOfInt[0] && i <= arrayOfInt[2]) ? true : false) != ((j >= arrayOfInt[1] && j <= arrayOfInt[3]) ? true : false)) ? true : false, bool2 = bool1;
        Tile tile;
        if (bool1 && (tile = Vars.world.tile(i, j)) != null && tile.build == null && !tile.solid())
          b++; 
      } 
    } 
    return b;
  }
  
  private static void update() {
    if (!LunariansSettings.dockAuto) {
      releaseCamera();
      return;
    } 
    if (Vars.state == null || !Vars.state.isGame() || Vars.player == null || Vars.player.dead())
      return; 
    if ((AutoSet.isActive() || AutoEconomy.active) && !unitsOnly)
      return; 
    CoreBlock.CoreBuild coreBuild = Vars.player.closestCore();
    if (coreBuild == null)
      return; 
    Unit unit = Vars.player.unit();
    if (unit == null || unit.type.buildSpeed <= 0.0F)
      return; 
    try {
      switch (phase.ordinal()) {
        case 0:
          stepNext(coreBuild, unit);
          break;
        case 1:
          stepMove(unit);
          break;
        case 2:
          stepBuild(unit);
          break;
        case 3:
          stepGrid(coreBuild, unit);
          break;
      } 
    } catch (Throwable throwable) {
      Log.err("[Lunarians] AutoDock", throwable);
      phase = Phase.NEXT;
      nextCheck = Time.time + 300.0F;
    } 
  }
  
  private static boolean unloadersAllowed() {
    try {
      return Vars.state.rules.getClass().getField("allowCoreUnloaders").getBoolean(Vars.state.rules);
    } catch (Throwable throwable) {
      return true;
    } 
  }
  
  private static void stepNext(CoreBlock.CoreBuild paramCoreBuild, Unit paramUnit) {
    if (Time.time < nextCheck)
      return; 
    nextCheck = Time.time + 90.0F;
    if (!unloadersAllowed()) {
      toast("Auto Dock: este servidor desativou unloaders no nucleo (allowCoreUnloaders). Os schematics encostados nao funcionam aqui.");
      return;
    } 
    idx = pick();
    if (idx < 0) {
      if (unitsOnly) {
        unitsFinished = true;
        return;
      } 
      toast("Auto Dock: ordem completa.");
      releaseCamera();
      phase = Phase.DONE;
      return;
    } 
    String[] arrayOfString = ORDER[idx];
    Schematic schematic = find(arrayOfString[1]);
    if (schematic == null) {
      toast("Auto Dock: schematic nao encontrado (" + arrayOfString[1] + "), pulando.");
      done[idx] = true;
      return;
    } 
    Seq<BuildPlan> seq1 = Vars.schematics.toPlans(schematic, 0, 0, false);
    if (seq1.size < schematic.tiles.size) {
      toast("Auto Dock: " + arrayOfString[1] + " tem blocos bloqueados, pulando.");
      done[idx] = true;
      return;
    } 
    boolean bool1 = (arrayOfString.length > 4 && "F".equals(arrayOfString[3])) ? true : false;
    if (!bool1 && alreadyBuilt(seq1, paramCoreBuild)) {
      done[idx] = true;
      return;
    } 
    String str = missing(paramCoreBuild, seq1, arrayOfString[2]);
    if (str != null) {
      toast("Auto Dock [" + arrayOfString[0] + "] " + arrayOfString[1] + ": falta " + str);
      return;
    } 
    if (!Vars.state.rules.infiniteResources) {
      byte b = 0;
      for (BuildPlan buildPlan : seq1) {
        if (buildPlan.block == Blocks.unloader)
          b++; 
      } 
      boolean bool = (arrayOfString.length > 3 && "U".equals(arrayOfString[3])) ? true : false;
      if (b > 0 || bool) {
        int i = 30 * b + (bool ? 150 : 30);
        int j = 25 * b + (bool ? 100 : 15);
        int k = paramCoreBuild.items.get(Items.silicon);
        int m = paramCoreBuild.items.get(Items.titanium);
        if (k < i || m < j) {
          toast("Auto Dock [" + arrayOfString[0] + "] " + arrayOfString[1] + ": esperando silicio " + k + "/" + i + " e titanio " + m + "/" + j);
          return;
        } 
      } 
    } 
    float f = powerNeed(seq1) * 60.0F;
    boolean bool2 = (f > 0.0F && supplyLeft() < f * 1.05F) ? true : false;
    if (!bool2) {
      energyFails = 0;
    } else if (energyFails >= 2) {
      toast("Auto Energy: geradores nao produzem (sem carvao no nucleo?). Seguindo sem energia extra.");
      bool2 = false;
    } 
    if (bool2 && energyBuilt >= 3) {
      toast("Auto Energy: limite de 3 grupos de geradores atingido. Seguindo sem energia extra.");
      bool2 = false;
    } 
    if (bool2) {
      Seq<BuildPlan> seq = energyCluster(paramCoreBuild);
      if (seq == null) {
        toast("Auto Energy: sem espaco para gerador junto ao nucleo.");
        return;
      } 
      String str1 = missing(paramCoreBuild, seq, "coal:300");
      if (str1 != null) {
        toast("Auto Energy: falta " + str1);
        return;
      } 
      toast("Auto Energy: construindo geradores (" + Mathf.round(f) + " energia/s necessaria).");
      energyFails++;
      energyBuilt++;
      startBuild(seq, true);
      return;
    } 
    Seq<BuildPlan> seq2 = bool1 ? fieldPlan(paramCoreBuild, seq1, arrayOfString[4], paramUnit) : bestDock(paramCoreBuild, seq1, paramUnit);
    if (seq2 == null) {
      toast(bool1 ? ("Auto Dock: sem campo de " + arrayOfString[4] + " livre para " + arrayOfString[1]) : ("Auto Dock: sem lugar encostado no nucleo para " + arrayOfString[1] + " (" + coreInfo() + ")"));
      nextCheck = Time.time + 600.0F;
      if (++stuckRetries >= 12) {
        done[idx] = true;
        stuckRetries = 0;
      } 
      return;
    } 
    stuckRetries = 0;
    toast("Auto Dock [" + arrayOfString[0] + "]: construindo " + arrayOfString[1]);
    startBuild(seq2, false);
  }
  
  private static void startBuild(Seq<BuildPlan> paramSeq, boolean paramBoolean) {
    current = paramSeq;
    energyStep = paramBoolean;
    float f1 = 0.0F;
    float f2 = 0.0F;
    for (BuildPlan buildPlan : paramSeq) {
      f1 += buildPlan.x;
      f2 += buildPlan.y;
    } 
    centerX = f1 / paramSeq.size * 8.0F;
    centerY = f2 / paramSeq.size * 8.0F;
    phase = Phase.MOVE;
    phaseStart = Time.time;
  }
  
  private static void stepMove(Unit paramUnit) {
    float f1 = paramUnit.type.buildRange * 0.6F;
    float f2 = Mathf.dst(paramUnit.x, paramUnit.y, centerX, centerY);
    if (f2 <= f1) {
      for (BuildPlan buildPlan : current) {
        if (isBuilt(buildPlan))
          continue; 
        paramUnit.addBuild(buildPlan.copy(), true);
      } 
      phase = Phase.BUILD;
      phaseStart = Time.time;
      nextCheck = Time.time + 60.0F;
      return;
    } 
    cameraGo(paramUnit, centerX, centerY);
    if (Time.time - phaseStart > 2400.0F)
      abort("nao consegui chegar na obra"); 
  }
  
  private static void cameraGo(Unit paramUnit, float paramFloat1, float paramFloat2) {
    if (!camHeld) {
      camHeld = true;
      prevDetach = Core.settings.getBool("detach-camera", false);
      Core.camera.position.set(paramUnit.x, paramUnit.y);
    } 
    Core.settings.put("detach-camera", Boolean.valueOf(true));
    float f1 = Core.camera.position.x;
    float f2 = Core.camera.position.y;
    float f3 = Mathf.dst(f1, f2, paramFloat1, paramFloat2);
    if (f3 < 1.0F)
      return; 
    float f4 = Math.min(f3, Math.max(2.0F, paramUnit.speed() * 1.25F) * Time.delta);
    Core.camera.position.set(f1 + (paramFloat1 - f1) / f3 * f4, f2 + (paramFloat2 - f2) / f3 * f4);
  }
  
  private static void releaseCamera() {
    if (camHeld) {
      Core.settings.put("detach-camera", Boolean.valueOf(prevDetach));
      camHeld = false;
    } 
  }
  
  private static void stepBuild(Unit paramUnit) {
    cameraGo(paramUnit, centerX, centerY);
    if (Time.time < nextCheck)
      return; 
    nextCheck = Time.time + 60.0F;
    boolean bool = true;
    for (BuildPlan buildPlan : current) {
      if (isBuilt(buildPlan))
        continue; 
      bool = false;
      boolean bool1 = false;
      for (BuildPlan buildPlan1 : paramUnit.plans()) {
        if (buildPlan1.x != buildPlan.x || buildPlan1.y != buildPlan.y)
          continue; 
        bool1 = true;
      } 
      if (bool1 || !Build.validPlace(buildPlan.block, Vars.player.team(), buildPlan.x, buildPlan.y, buildPlan.rotation))
        continue; 
      paramUnit.addBuild(buildPlan.copy(), true);
    } 
    if (bool) {
      phase = Phase.GRID;
      phaseStart = Time.time;
      nextCheck = Time.time + 30.0F;
    } else if (Time.time - phaseStart > 9000.0F) {
      abort("obra travada (recursos acabaram ou area bloqueada)");
    } 
  }
  
  private static void abort(String paramString) {
    toast("Auto Dock: " + paramString + ". Tentando de novo depois.");
    Vars.player.unit().plans().clear();
    releaseCamera();
    phase = Phase.NEXT;
    nextCheck = Time.time + 1200.0F;
  }
  
  private static void stepGrid(CoreBlock.CoreBuild paramCoreBuild, Unit paramUnit) {
    Building building1;
    if (Time.time < nextCheck)
      return; 
    nextCheck = Time.time + 60.0F;
    ObjectSet objectSet = new ObjectSet();
    Seq seq = new Seq();
    for (BuildPlan buildPlan : current) {
      Building building = (buildPlan.tile() == null) ? null : (buildPlan.tile()).build;
      if (building == null || building.power == null)
        continue; 
      objectSet.add(building.power.graph);
      seq.add(building);
    } 
    if (seq.isEmpty() || (energyStep && seq.size < 1)) {
      finishStep();
      return;
    } 
    boolean bool = false;
    ObjectSet.ObjectSetIterator<PowerGraph> objectSetIterator;
    for (objectSetIterator = objectSet.iterator(); objectSetIterator.hasNext(); ) {
      PowerGraph powerGraph = objectSetIterator.next();
      if (powerGraph.producers.size <= 0)
        continue; 
      bool = true;
    } 
    objectSetIterator = null;
    Building building2 = null;
    float f1 = 1.0E9F;
    for (Building building : Groups.build) {
      if (building.team != Vars.player.team() || building.power == null || objectSet.contains(building.power.graph) || (building.power.graph.producers.size == 0 && building.power.graph.batteries.size == 0))
        continue; 
      for (Building building3 : seq) {
        float f = Mathf.dst(building.tileX(), building.tileY(), building3.tileX(), building3.tileY());
        if (f >= f1)
          continue; 
        f1 = f;
        building1 = building3;
        building2 = building;
      } 
    } 
    if (building2 == null || f1 > 30.0F || (bool && !energyStep && supplyLeft() > 0.0F && f1 > 40.0F)) {
      finishStep();
      return;
    } 
    Seq<BuildPlan> seq1 = nodeChain(building1, building2);
    if (seq1 == null || seq1.isEmpty()) {
      finishStep();
      return;
    } 
    toast("Auto Power Node: ligando a energia (" + seq1.size + " nos).");
    current = seq1;
    float f2 = 0.0F;
    float f3 = 0.0F;
    for (BuildPlan buildPlan : seq1) {
      f2 += buildPlan.x;
      f3 += buildPlan.y;
    } 
    centerX = f2 / seq1.size * 8.0F;
    centerY = f3 / seq1.size * 8.0F;
    energyStep = true;
    phase = Phase.MOVE;
    phaseStart = Time.time;
  }
  
  private static void finishStep() {
    if (!energyStep && idx >= 0 && idx < done.length)
      done[idx] = true; 
    energyStep = false;
    current = new Seq();
    releaseCamera();
    phase = Phase.NEXT;
    nextCheck = Time.time + 60.0F;
  }
  
  private static Seq<BuildPlan> nodeChain(Building paramBuilding1, Building paramBuilding2) {
    Seq<BuildPlan> seq = new Seq();
    float f2 = (paramBuilding2.tileX() - paramBuilding1.tileX());
    float f1, f3 = Mathf.dst(f2, f1 = (paramBuilding2.tileY() - paramBuilding1.tileY()));
    int i = Math.max(1, (int)Math.ceil((f3 / 5.0F)) - 1);
    if (i > 5)
      return null; 
    for (byte b = 1; b <= i; b++) {
      float f = b / (i + 1);
      int k = Mathf.round(paramBuilding1.tileX() + f2 * f);
      int j, arrayOfInt[] = freeSpot(Blocks.powerNode, k, j = Mathf.round(paramBuilding1.tileY() + f1 * f), 3);
      if (arrayOfInt == null)
        return null; 
      seq.add(new BuildPlan(arrayOfInt[0], arrayOfInt[1], 0, Blocks.powerNode));
    } 
    return seq;
  }
  
  private static int[] freeSpot(Block paramBlock, int paramInt1, int paramInt2, int paramInt3) {
    for (byte b = 0; b <= paramInt3; b++) {
      for (int i = paramInt1 - b; i <= paramInt1 + b; i++) {
        for (int j = paramInt2 - b; j <= paramInt2 + b; ) {
          if (!Build.validPlace(paramBlock, Vars.player.team(), i, j, 0)) {
            j++;
            continue;
          } 
          return new int[] { i, j };
        } 
      } 
    } 
    return null;
  }
  
  private static float supplyLeft() {
    ObjectSet objectSet = new ObjectSet();
    for (Building building : Groups.build) {
      if (building.team != Vars.player.team() || building.power == null || building.power.graph == null)
        continue; 
      objectSet.add(building.power.graph);
    } 
    float f = 0.0F;
    for (ObjectSet.ObjectSetIterator<PowerGraph> objectSetIterator = objectSet.iterator(); objectSetIterator.hasNext(); ) {
      PowerGraph powerGraph = objectSetIterator.next();
      if (powerGraph.producers.size == 0)
        continue; 
      float f1 = 0.0F;
      for (Building building : powerGraph.producers)
        f1 += (building.block instanceof PowerGenerator) ? ((PowerGenerator)building.block).powerProduction : 0.0F; 
      float f2 = 0.0F;
      for (Building building : powerGraph.consumers) {
        if (building.block.consPower == null || building.block.consPower.buffered)
          continue; 
        f2 += building.block.consPower.usage;
      } 
      f += f1 - f2;
    } 
    return f * 60.0F;
  }
  
  private static float powerNeed(Seq<BuildPlan> paramSeq) {
    float f = 0.0F;
    for (BuildPlan buildPlan : paramSeq) {
      if (buildPlan.block.consPower == null || buildPlan.block.consPower.buffered)
        continue; 
      f += buildPlan.block.consPower.usage;
    } 
    return f;
  }
  
  private static Seq<BuildPlan> energyCluster(CoreBlock.CoreBuild paramCoreBuild) {
    int[] arrayOfInt = rect(paramCoreBuild.tile.x, paramCoreBuild.tile.y, paramCoreBuild.block.size);
    int[][] arrayOfInt1 = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };
    for (int i = arrayOfInt[0] - 1; i <= arrayOfInt[2] + 1; i++) {
      for (int j = arrayOfInt[1] - 1; j <= arrayOfInt[3] + 1; j++) {
        boolean bool2 = (i >= arrayOfInt[0] && i <= arrayOfInt[2]) ? true : false;
        boolean bool1 = (j >= arrayOfInt[1] && j <= arrayOfInt[3]) ? true : false, bool3 = bool1;
        if (bool2 != bool1) {
          byte b1 = (i < arrayOfInt[0]) ? -1 : ((i > arrayOfInt[2]) ? 1 : 0);
          byte b2 = (j < arrayOfInt[1]) ? -1 : ((j > arrayOfInt[3]) ? 1 : 0);
          Seq<BuildPlan> seq = new Seq();
          seq.add(new BuildPlan(i, j, 0, Blocks.unloader, Items.coal));
          boolean bool = Build.validPlace(Blocks.unloader, Vars.player.team(), i, j, 0);
          byte b3 = 0;
          int[][] arrayOfInt2 = arrayOfInt1;
          int k = arrayOfInt2.length;
          for (byte b4 = 0; b4 < k; b4++) {
            int[] arrayOfInt4 = arrayOfInt2[b4];
            if (arrayOfInt4[0] != -b1 || arrayOfInt4[1] != -b2) {
              int m = i + arrayOfInt4[0];
              int n = j + arrayOfInt4[1];
              if (Build.validPlace(Blocks.combustionGenerator, Vars.player.team(), m, n, 0)) {
                seq.add(new BuildPlan(m, n, 0, Blocks.combustionGenerator));
                b3++;
              } 
            } 
          } 
          int[] arrayOfInt3 = null;
          if (bool && b3 >= 2 && (arrayOfInt3 = freeSpot(Blocks.powerNode, i + b1 * 3, j + b2 * 3, 3)) != null) {
            seq.add(new BuildPlan(arrayOfInt3[0], arrayOfInt3[1], 0, Blocks.powerNode));
            return seq;
          } 
        } 
      } 
    } 
    return null;
  }
  
  private static Seq<BuildPlan> fieldPlan(CoreBlock.CoreBuild paramCoreBuild, Seq<BuildPlan> paramSeq, String paramString, Unit paramUnit) {
    try {
      Item item = Vars.content.item(paramString);
      if (item == null)
        return null; 
      int i = Vars.world.width();
      int j = Vars.world.height();
      int k = paramCoreBuild.tileX();
      int m = paramCoreBuild.tileY();
      byte b1 = 55;
      boolean[] arrayOfBoolean1 = new boolean[i * j];
      for (int n = Math.max(0, m - b1 - 20); n <= Math.min(j - 1, m + b1 + 20); n++) {
        for (int i1 = Math.max(0, k - b1 - 20); i1 <= Math.min(i - 1, k + b1 + 20); i1++) {
          Tile tile = Vars.world.tile(i1, n);
          if (tile != null && tile.build == null && !tile.solid() && tile.drop() == item)
            arrayOfBoolean1[n * i + i1] = true; 
        } 
      } 
      boolean[] arrayOfBoolean2 = AutoConnectConveyor.reservedFromPlans(paramUnit);
      Seq<BuildPlan> seq = copyAll(paramSeq);
      byte b2 = 0;
      for (BuildPlan buildPlan : seq) {
        if (buildPlan.block instanceof mindustry.world.blocks.production.Drill)
          b2++; 
      } 
      if (b2 == 0)
        return null; 
      ArrayList<float[]> arrayList = new ArrayList();
      for (byte b3 = 0; b3 < 4; b3++) {
        int[] arrayOfInt = bounds(seq);
        int i1 = (arrayOfInt[0] + arrayOfInt[2]) / 2;
        int i2 = (arrayOfInt[1] + arrayOfInt[3]) / 2;
        for (int i3 = k - b1 - i1; i3 <= k + b1 - i1; i3++) {
          for (int i4 = m - b1 - i2; i4 <= m + b1 - i2; i4++) {
            byte b = 0;
            for (BuildPlan buildPlan : seq) {
              if (!(buildPlan.block instanceof mindustry.world.blocks.production.Drill))
                continue; 
              int[] arrayOfInt1 = rect(buildPlan.x + i3, buildPlan.y + i4, buildPlan.block.size);
              byte b5 = 0;
              for (int i5 = arrayOfInt1[0]; i5 <= arrayOfInt1[2]; i5++) {
                for (int i6 = arrayOfInt1[1]; i6 <= arrayOfInt1[3]; i6++) {
                  if (i5 >= 0 && i6 >= 0 && i5 < i && i6 < j && arrayOfBoolean1[i6 * i + i5])
                    b5++; 
                } 
              } 
              if (b5 * 4 >= buildPlan.block.size * buildPlan.block.size * 3)
                b++; 
            } 
            if (b * 10 >= b2 * 6) {
              float f = Mathf.dst((i1 + i3), (i2 + i4), k, m);
              arrayList.add(new float[] { b * 10.0F - f * 0.3F, b3, i3, i4 });
            } 
          } 
        } 
        rotate(seq);
      } 
      Collections.sort(arrayList, (Comparator)new Comparator<float[]>() {
            public int compare(float[] param1ArrayOffloat1, float[] param1ArrayOffloat2) {
              return Float.compare(param1ArrayOffloat2[0], param1ArrayOffloat1[0]);
            }
          });
      ArrayList<int[]> arrayList1 = dockRects(paramCoreBuild);
      byte b4 = 0;
      for (float[] arrayOfFloat : arrayList) {
        if (b4++ >= 40)
          break; 
        Seq<BuildPlan> seq1 = copyAll(paramSeq);
        int i1;
        for (i1 = 0; i1 < (int)arrayOfFloat[1]; ) {
          rotate(seq1);
          i1++;
        } 
        i1 = (int)arrayOfFloat[2];
        int i2 = (int)arrayOfFloat[3];
        HashSet<Integer> hashSet = new HashSet();
        boolean bool = true;
        for (BuildPlan buildPlan : seq1) {
          buildPlan.x += i1;
          buildPlan.y += i2;
          int[] arrayOfInt = rect(buildPlan.x, buildPlan.y, buildPlan.block.size);
          if (overlapsAny(new int[] { arrayOfInt[0] - 2, arrayOfInt[1] - 2, arrayOfInt[2] + 2, arrayOfInt[3] + 2 }, arrayList1)) {
            bool = false;
            break;
          } 
          for (int i3 = arrayOfInt[0]; i3 <= arrayOfInt[2] && bool; i3++) {
            for (int i4 = arrayOfInt[1]; i4 <= arrayOfInt[3]; i4++) {
              if (i3 < 0 || i4 < 0 || i3 >= i || i4 >= j || arrayOfBoolean2[i4 * i + i3]) {
                bool = false;
                break;
              } 
              hashSet.add(Integer.valueOf(i4 * i + i3));
            } 
          } 
          if (!bool || !Build.validPlace(buildPlan.block, Vars.player.team(), buildPlan.x, buildPlan.y, buildPlan.rotation)) {
            bool = false;
            break;
          } 
        } 
        if (!bool)
          continue; 
        int[] arrayOfInt1 = null;
        float f = 1.0E9F;
        int[] arrayOfInt2 = { 1, 0, -1, 0 };
        int[] arrayOfInt3 = { 0, 1, 0, -1 };
        for (BuildPlan buildPlan : seq1) {
          if (!(buildPlan.block instanceof mindustry.world.blocks.distribution.Conveyor))
            continue; 
          int i3 = buildPlan.x + arrayOfInt2[buildPlan.rotation & 0x3];
          int i4 = buildPlan.y + arrayOfInt3[buildPlan.rotation & 0x3];
          if (!Vars.world.tiles.in(i3, i4) || hashSet.contains(Integer.valueOf(i4 * i + i3)))
            continue; 
          float f1 = Mathf.dst(i3, i4, k, m);
          if (f1 < f) {
            f = f1;
            arrayOfInt1 = new int[] { i3, i4, buildPlan.rotation & 0x3 };
          } 
        } 
        if (arrayOfInt1 == null)
          continue; 
        boolean[] arrayOfBoolean = (boolean[])arrayOfBoolean2.clone();
        for (BuildPlan buildPlan : seq1)
          AutoConnectConveyor.markPlan(buildPlan, arrayOfBoolean); 
        Seq<BuildPlan> seq2 = AutoConnectConveyor.planRoute(arrayOfInt1[0], arrayOfInt1[1], arrayOfInt1[2], arrayOfBoolean);
        if (seq2 == null || seq2.isEmpty())
          seq2 = AutoConnectConveyor.planRoute(arrayOfInt1[0], arrayOfInt1[1], -1, arrayOfBoolean); 
        if (seq2 == null || seq2.isEmpty())
          continue; 
        Seq<BuildPlan> seq3 = new Seq();
        seq3.addAll(seq1);
        seq3.addAll(seq2);
        return seq3;
      } 
    } catch (Throwable throwable) {
      Log.err("[Lunarians] fieldPlan", throwable);
    } 
    return null;
  }
  
  private static Seq<BuildPlan> bestDock(CoreBlock.CoreBuild paramCoreBuild, Seq<BuildPlan> paramSeq, Unit paramUnit) {
    Seq<BuildPlan> seq = bestDockImpl(paramCoreBuild, paramSeq, paramUnit, true);
    return (seq != null) ? seq : bestDockImpl(paramCoreBuild, paramSeq, paramUnit, false);
  }
  
  private static ArrayList<int[]> dockRects(CoreBlock.CoreBuild paramCoreBuild) {
    ArrayList<int[]> arrayList = new ArrayList();
    arrayList.add(rect(paramCoreBuild.tile.x, paramCoreBuild.tile.y, paramCoreBuild.block.size));
    try {
      for (Building building : paramCoreBuild.proximity) {
        if (building instanceof StorageBlock.StorageBuild && ((StorageBlock.StorageBuild)building).linkedCore == paramCoreBuild)
          arrayList.add(rect(building.tile.x, building.tile.y, building.block.size)); 
      } 
    } catch (Throwable throwable) {}
    return arrayList;
  }
  
  private static boolean touchesAny(BuildPlan paramBuildPlan, int paramInt1, int paramInt2, ArrayList<int[]> paramArrayList) {
    for (int[] arrayOfInt : paramArrayList) {
      if (touches(paramBuildPlan, paramInt1, paramInt2, arrayOfInt))
        return true; 
    } 
    return false;
  }
  
  private static boolean overlapsAny(int[] paramArrayOfint, ArrayList<int[]> paramArrayList) {
    for (int[] arrayOfInt : paramArrayList) {
      if (paramArrayOfint[2] >= arrayOfInt[0] && paramArrayOfint[0] <= arrayOfInt[2] && paramArrayOfint[3] >= arrayOfInt[1] && paramArrayOfint[1] <= arrayOfInt[3])
        return true; 
    } 
    return false;
  }
  
  private static int ringCovered(Seq<BuildPlan> paramSeq, int paramInt1, int paramInt2, ArrayList<int[]> paramArrayList) {
    byte b = 0;
    for (BuildPlan buildPlan : paramSeq) {
      int[] arrayOfInt = rect(buildPlan.x + paramInt1, buildPlan.y + paramInt2, buildPlan.block.size);
      for (int i = arrayOfInt[0]; i <= arrayOfInt[2]; i++) {
        for (int j = arrayOfInt[1]; j <= arrayOfInt[3]; j++) {
          for (int[] arrayOfInt1 : paramArrayList) {
            boolean bool1 = (i >= arrayOfInt1[0] && i <= arrayOfInt1[2]) ? true : false;
            boolean bool2 = (j >= arrayOfInt1[1] && j <= arrayOfInt1[3]) ? true : false;
            if (bool1 != bool2 && i >= arrayOfInt1[0] - 1 && i <= arrayOfInt1[2] + 1 && j >= arrayOfInt1[1] - 1 && j <= arrayOfInt1[3] + 1) {
              b++;
              break;
            } 
          } 
        } 
      } 
    } 
    return b;
  }
  
  private static Seq<BuildPlan> bestDockImpl(CoreBlock.CoreBuild paramCoreBuild, Seq<BuildPlan> paramSeq, Unit paramUnit, boolean paramBoolean) {
    ArrayList<int[]> arrayList = dockRects(paramCoreBuild);
    int i = 1048576, j = 1048576, k = -1048576, m = -1048576;
    for (int[] arrayOfInt : arrayList) {
      i = Math.min(i, arrayOfInt[0]);
      j = Math.min(j, arrayOfInt[1]);
      k = Math.max(k, arrayOfInt[2]);
      m = Math.max(m, arrayOfInt[3]);
    } 
    byte b1 = 0;
    boolean bool = false;
    for (BuildPlan buildPlan : paramSeq) {
      if (buildPlan.block == Blocks.unloader)
        b1++; 
      if (buildPlan.block instanceof StorageBlock)
        bool = true; 
    } 
    Seq<BuildPlan> seq1 = null;
    float f = -1.0E9F;
    Seq<BuildPlan> seq2 = copyAll(paramSeq);
    for (byte b2 = 0; b2 < 4; b2++) {
      int[] arrayOfInt = bounds(seq2);
      for (int n = i - arrayOfInt[2] - 1; n <= k - arrayOfInt[0] + 1; n++) {
        for (int i1 = j - arrayOfInt[3] - 1; i1 <= m - arrayOfInt[1] + 1; i1++) {
          byte b3 = 0;
          byte b4 = 0;
          boolean bool1 = false;
          for (BuildPlan buildPlan : seq2) {
            int[] arrayOfInt1 = rect(buildPlan.x + n, buildPlan.y + i1, buildPlan.block.size);
            if (overlapsAny(arrayOfInt1, arrayList)) {
              bool1 = true;
              break;
            } 
            if (!touchesAny(buildPlan, n, i1, arrayList))
              continue; 
            if (buildPlan.block instanceof StorageBlock) {
              b3++;
              continue;
            } 
            if (buildPlan.block == Blocks.unloader)
              b4++; 
          } 
          if (!bool1 && (!bool ? (b4 == 0) : (b3 == 0)) && (
            !paramBoolean || bool || b4 >= b1)) {
            int i2 = ringCovered(seq2, n, i1, arrayList);
            float f1 = (arrayOfInt[0] + arrayOfInt[2]) / 2.0F + n;
            float f2 = (arrayOfInt[1] + arrayOfInt[3]) / 2.0F + i1;
            float f3 = b3 * 100.0F + b4 * 10.0F - 3.0F * Math.max(0, i2 - b3 - b4) - Mathf.dst(f1, f2, paramUnit.tileX(), paramUnit.tileY()) * 0.01F;
            if (f3 > f) {
              boolean bool2 = true;
              for (BuildPlan buildPlan : seq2) {
                if (Build.validPlace(buildPlan.block, Vars.player.team(), buildPlan.x + n, buildPlan.y + i1, buildPlan.rotation))
                  continue; 
                bool2 = false;
              } 
              if (bool2) {
                f = f3;
                seq1 = new Seq();
                for (BuildPlan buildPlan1 : seq2) {
                  BuildPlan buildPlan2 = buildPlan1.copy();
                  buildPlan2.x += n;
                  buildPlan2.y += i1;
                  seq1.add(buildPlan2);
                } 
              } 
            } 
          } 
        } 
      } 
      rotate(seq2);
    } 
    return seq1;
  }
  
  private static boolean touches(BuildPlan paramBuildPlan, int paramInt1, int paramInt2, int[] paramArrayOfint) {
    int[] arrayOfInt = rect(paramBuildPlan.x + paramInt1, paramBuildPlan.y + paramInt2, paramBuildPlan.block.size);
    boolean bool1 = (arrayOfInt[2] >= paramArrayOfint[0] && arrayOfInt[0] <= paramArrayOfint[2]) ? true : false;
    boolean bool2 = (arrayOfInt[3] >= paramArrayOfint[1] && arrayOfInt[1] <= paramArrayOfint[3]) ? true : false;
    return ((bool1 && (arrayOfInt[3] + 1 == paramArrayOfint[1] || arrayOfInt[1] - 1 == paramArrayOfint[3])) || (bool2 && (arrayOfInt[2] + 1 == paramArrayOfint[0] || arrayOfInt[0] - 1 == paramArrayOfint[2])));
  }
  
  private static int[] rect(int paramInt1, int paramInt2, int paramInt3) {
    int i = -((paramInt3 - 1) / 2);
    return new int[] { paramInt1 + i, paramInt2 + i, paramInt1 + i + paramInt3 - 1, paramInt2 + i + paramInt3 - 1 };
  }
  
  private static int[] bounds(Seq<BuildPlan> paramSeq) {
    int i = 1048576;
    int j = 1048576;
    int k = -1048576;
    int m = -1048576;
    for (BuildPlan buildPlan : paramSeq) {
      int[] arrayOfInt = rect(buildPlan.x, buildPlan.y, buildPlan.block.size);
      i = Math.min(i, arrayOfInt[0]);
      j = Math.min(j, arrayOfInt[1]);
      k = Math.max(k, arrayOfInt[2]);
      m = Math.max(m, arrayOfInt[3]);
    } 
    return new int[] { i, j, k, m };
  }
  
  private static Seq<BuildPlan> copyAll(Seq<BuildPlan> paramSeq) {
    Seq<BuildPlan> seq = new Seq();
    for (BuildPlan buildPlan : paramSeq)
      seq.add(buildPlan.copy()); 
    return seq;
  }
  
  private static void rotate(Seq<BuildPlan> paramSeq) {
    for (Iterator<BuildPlan> iterator = paramSeq.iterator(); iterator.hasNext(); ) {
      BuildPlan buildPlan = iterator.next();
      float f1 = (buildPlan.block.size % 2 == 0) ? -0.5F : 0.0F;
      buildPlan.pointConfig(paramPoint2 -> {
            float f1 = paramPoint2.x + paramFloat;
            float f2 = paramPoint2.y + paramFloat;
            float f3 = f1;
            f1 = -f2;
            f2 = f3;
            paramPoint2.set(Mathf.floor(f1 - paramFloat), Mathf.floor(f2 - paramFloat));
          });
      float f2 = buildPlan.x * 8.0F + buildPlan.block.offset;
      float f3 = buildPlan.y * 8.0F + buildPlan.block.offset;
      float f4 = f2;
      f2 = -f3;
      f3 = f4;
      buildPlan.x = World.toTile(f2 - buildPlan.block.offset);
      buildPlan.y = World.toTile(f3 - buildPlan.block.offset);
      buildPlan.rotation = buildPlan.block.planRotation(Mathf.mod(buildPlan.rotation + 1, 4));
    } 
  }
  
  private static Schematic find(String paramString) {
    for (String str : paramString.split("\\|")) {
      for (Schematic schematic : Vars.schematics.all()) {
        if (!str.equals(schematic.name()))
          continue; 
        return schematic;
      } 
    } 
    return null;
  }
  
  private static boolean isBuilt(BuildPlan paramBuildPlan) {
    Tile tile = Vars.world.tile(paramBuildPlan.x, paramBuildPlan.y);
    return (tile != null && tile.build != null && tile.block() == paramBuildPlan.block && tile.team() == Vars.player.team());
  }
  
  private static boolean alreadyBuilt(Seq<BuildPlan> paramSeq, CoreBlock.CoreBuild paramCoreBuild) {
    Block block = null;
    byte b1 = 0;
    for (BuildPlan buildPlan : paramSeq) {
      if (buildPlan.block.consumers.length <= 0 || buildPlan.block.size < 2 || buildPlan.block instanceof StorageBlock)
        continue; 
      byte b = 0;
      for (BuildPlan buildPlan1 : paramSeq) {
        if (buildPlan1.block != buildPlan.block)
          continue; 
        b++;
      } 
      if (b <= b1)
        continue; 
      b1 = b;
      block = buildPlan.block;
    } 
    if (block == null)
      return false; 
    byte b2 = 0;
    for (Building building : Groups.build) {
      if (building.team != Vars.player.team() || building.block != block || !building.within((Position)paramCoreBuild, 480.0F))
        continue; 
      b2++;
    } 
    return (b2 >= b1);
  }
  
  private static String missing(CoreBlock.CoreBuild paramCoreBuild, Seq<BuildPlan> paramSeq, String paramString) {
    if (Vars.state.rules.infiniteResources)
      return null; 
    HashMap<Object, Object> hashMap1 = new HashMap<>();
    for (BuildPlan buildPlan : paramSeq) {
      for (ItemStack itemStack : buildPlan.block.requirements) {
        int i = Mathf.ceil(itemStack.amount * Vars.state.rules.buildCostMultiplier);
        hashMap1.merge(itemStack.item, Integer.valueOf(i), Integer::sum);
      } 
    } 
    for (Map.Entry<Object, Object> entry : hashMap1.entrySet()) {
      if (((Integer)entry.getValue()).intValue() <= paramCoreBuild.storageCapacity)
        continue; 
      return ((Item)entry.getKey()).localizedName + " (precisa " + entry.getValue() + " > capacidade " + paramCoreBuild.storageCapacity + " do nucleo)";
    } 
    HashMap<Object, Object> hashMap2 = new HashMap<>();
    if (paramString != null && !paramString.isEmpty())
      for (String str : paramString.split(";")) {
        String[] arrayOfString = str.split(":");
        Item item = Vars.content.item(arrayOfString[0]);
        if (item != null)
          hashMap2.put(item, Integer.valueOf(Integer.parseInt(arrayOfString[1]))); 
      }  
    StringBuilder stringBuilder = new StringBuilder();
    for (Map.Entry<Object, Object> entry : hashMap1.entrySet()) {
      int i = paramCoreBuild.items.get((Item)entry.getKey());
      Integer integer = (Integer)hashMap2.get(entry.getKey());
      int j = ((Integer)entry.getValue()).intValue() + ((integer == null) ? 0 : integer.intValue());
      if (i >= j)
        continue; 
      stringBuilder.append(((Item)entry.getKey()).localizedName).append(' ').append(j - i).append("  ");
    } 
    for (Map.Entry<Object, Object> entry : hashMap2.entrySet()) {
      if (hashMap1.containsKey(entry.getKey()))
        continue; 
      int i = paramCoreBuild.items.get((Item)entry.getKey());
      if (i >= ((Integer)entry.getValue()).intValue())
        continue; 
      stringBuilder.append(((Item)entry.getKey()).localizedName).append(' ').append(((Integer)entry.getValue()).intValue() - i).append(" (alimentacao)  ");
    } 
    return (stringBuilder.length() == 0) ? null : stringBuilder.toString().trim();
  }
  
  private static void toast(String paramString) {
    if (paramString.equals(lastToast) && Time.time - lastToastAt < 1800.0F)
      return; 
    if (Time.time - lastToastAt < 240.0F)
      return; 
    lastToast = paramString;
    lastToastAt = Time.time;
    Log.info("[Lunarians] " + paramString);
    if (Vars.ui != null)
      Vars.ui.showInfoToast(paramString, 4.0F); 
  }
  
  private static String lastToast = "";
  
  private static float lastToastAt = -999.0F;
  
  private static boolean energyStep = false;
  
  private static int stuckRetries = 0;
  
  private static boolean camHeld = false;
  
  private static boolean prevDetach = false;
  
  private enum Phase {
    NEXT, MOVE, BUILD, GRID, DONE;
  }
}
