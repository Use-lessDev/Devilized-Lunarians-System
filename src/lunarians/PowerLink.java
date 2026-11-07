package lunarians;

import arc.Core;
import arc.Events;
import arc.math.geom.Point2;
import arc.math.geom.Position;
import arc.util.Log;
import arc.util.Time;
import java.util.ArrayList;
import java.util.HashMap;
import mindustry.Vars;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.blocks.power.PowerNode;

public class PowerLink {
  private static boolean registered;
  
  private static float next;
  
  private static final HashMap<Integer, int[]> tries = (HashMap)new HashMap<>();
  
  private static final HashMap<Integer, Float> last = new HashMap<>();
  
  public static boolean enabled() {
    return Core.settings.getBool("lunarians-auto-pnode", true);
  }
  
  public static void setEnabled(boolean paramBoolean) {
    Core.settings.put("lunarians-auto-pnode", Boolean.valueOf(paramBoolean));
  }
  
  public static void init() {
    if (registered)
      return; 
    registered = true;
    Events.run(EventType.Trigger.update, PowerLink::update);
    Events.on(EventType.WorldLoadEvent.class, paramWorldLoadEvent -> {
          tries.clear();
          last.clear();
        });
  }
  
  private static boolean unpowered(Building paramBuilding) {
    return (paramBuilding.power != null && paramBuilding.block.consPower != null && !paramBuilding.block.consPower.buffered && paramBuilding.block.consPower.usage > 0.0F && !paramBuilding.block.outputsPower && paramBuilding.power.graph != null && paramBuilding.power.graph.producers.size == 0 && paramBuilding.power.graph.batteries.size == 0);
  }
  
  private static boolean affordable(Block paramBlock, int paramInt) {
    if (Vars.state.rules.infiniteResources)
      return true; 
    if (Vars.player.closestCore() == null)
      return false; 
    for (ItemStack itemStack : paramBlock.requirements) {
      if ((Vars.player.closestCore()).items.get(itemStack.item) < itemStack.amount * paramInt)
        return false; 
    } 
    return true;
  }
  
  private static void update() {
    if (!enabled() || Vars.state == null || !Vars.state.isGame() || Vars.player == null || Time.time < next)
      return; 
    next = Time.time + 300.0F;
    try {
      Unit unit = Vars.player.unit();
      if (unit == null || unit.dead || !unit.canBuild() || Vars.player.closestCore() == null || (unit.plans()).size > 1)
        return; 
      Block block1 = Vars.content.block("power-node");
      Block block2 = Vars.content.block("large-power-node");
      if (block1 == null)
        return; 
      Building building1 = null;
      float f1 = 1.0E9F;
      for (Building building : Groups.build) {
        if (building.team != Vars.player.team() || !unpowered(building))
          continue; 
        float f = building.dst((Position)unit) / 8.0F;
        if (f > 40.0F || f >= f1)
          continue; 
        int[] arrayOfInt1 = tries.get(Integer.valueOf(building.pos()));
        if (arrayOfInt1 != null && (arrayOfInt1[0] >= 2 || Time.time - arrayOfInt1[1] < 1800.0F))
          continue; 
        f1 = f;
        building1 = building;
      } 
      if (building1 == null)
        return; 
      Building building2 = null;
      float f2 = 1.0E9F;
      for (Building building : Groups.build) {
        if (building.team != Vars.player.team() || building.power == null || building.power.graph == building1.power.graph || building.power.graph.producers.size == 0)
          continue; 
        float f = building.dst((Position)building1) / 8.0F;
        if (f < f2 && f <= 60.0F) {
          f2 = f;
          building2 = building;
        } 
      } 
      int[] arrayOfInt = tries.get(Integer.valueOf(building1.pos()));
      if (arrayOfInt == null)
        arrayOfInt = new int[] { 0, 0 }; 
      arrayOfInt[0] = arrayOfInt[0] + 1;
      arrayOfInt[1] = (int)Time.time;
      tries.put(Integer.valueOf(building1.pos()), arrayOfInt);
      if (building2 == null)
        return; 
      plan(unit, building1, building2, block1, block2);
    } catch (Throwable throwable) {
      Log.err("[Lunarians] PowerLink", throwable);
    } 
  }
  
  private static boolean occupiedByPlan(Unit paramUnit, int paramInt1, int paramInt2) {
    for (BuildPlan buildPlan : paramUnit.plans()) {
      if (buildPlan.block != null && Math.abs(buildPlan.x - paramInt1) <= buildPlan.block.size / 2 && Math.abs(buildPlan.y - paramInt2) <= buildPlan.block.size / 2)
        return true; 
    } 
    return false;
  }
  
  private static int[] snap(Unit paramUnit, Block paramBlock, double paramDouble1, double paramDouble2, ArrayList<int[]> paramArrayList) {
    int i = (int)Math.round(paramDouble1), j = (int)Math.round(paramDouble2);
    for (byte b = 0; b <= 3; b++) {
      for (byte b1 = -b; b1 <= b; b1++) {
        for (byte b2 = -b; b2 <= b; b2++) {
          if (Math.max(Math.abs(b2), Math.abs(b1)) == b) {
            int k = i + b2, m = j + b1;
            boolean bool = occupiedByPlan(paramUnit, k, m);
            for (int[] arrayOfInt : paramArrayList) {
              if (Math.abs(arrayOfInt[0] - k) <= 1 && Math.abs(arrayOfInt[1] - m) <= 1)
                bool = true; 
            } 
            if (!bool && Build.validPlace(paramBlock, Vars.player.team(), k, m, 0))
              return new int[] { k, m }; 
          } 
        } 
      } 
    } 
    return null;
  }
  
  private static void plan(Unit paramUnit, Building paramBuilding1, Building paramBuilding2, Block paramBlock1, Block paramBlock2) {
    double d1 = paramBuilding1.tileX(), d2 = paramBuilding1.tileY(), d3 = paramBuilding2.tileX(), d4 = paramBuilding2.tileY();
    double d5 = Math.hypot(d3 - d1, d4 - d2);
    double d6 = (d3 - d1) / Math.max(d5, 0.01D), d7 = (d4 - d2) / Math.max(d5, 0.01D);
    Block block = paramBlock1;
    byte b1 = 1;
    for (byte b2 = 0; b2 < 2; b2++) {
      Block block1 = (b2 == 0 && AutoSetConfig.bigNode() && paramBlock2 != null) ? paramBlock2 : paramBlock1;
      double d11 = ((PowerNode)block1).laserRange;
      double d12 = Math.max(2.0D, d11 - 2.0D);
      byte b = 1;
      double d13 = Math.max(2.0D, paramBuilding1.block.size / 2.0D + 1.5D);
      while (d5 - d13 > d11 - 1.5D && b < 14) {
        d13 += d12;
        b++;
      } 
      if (b >= 14)
        return; 
      if (block1 == paramBlock1 || affordable(block1, b)) {
        block = block1;
        b1 = b;
        break;
      } 
    } 
    if (!affordable(block, b1))
      return; 
    double d8 = ((PowerNode)block).laserRange;
    double d9 = Math.max(2.0D, d8 - 2.0D);
    ArrayList<int[]> arrayList = new ArrayList();
    double d10 = Math.max(2.0D, paramBuilding1.block.size / 2.0D + 1.5D);
    int i;
    for (i = 0; i < b1; i++) {
      int[] arrayOfInt = snap(paramUnit, block, d1 + d6 * d10, d2 + d7 * d10, arrayList);
      if (arrayOfInt == null)
        return; 
      arrayList.add(arrayOfInt);
      d10 += d9;
    } 
    for (i = arrayList.size() - 1; i >= 0; i--) {
      int[] arrayOfInt = arrayList.get(i);
      ArrayList<Point2> arrayList1 = new ArrayList();
      if (i == arrayList.size() - 1) {
        arrayList1.add(new Point2(paramBuilding2.tileX() - arrayOfInt[0], paramBuilding2.tileY() - arrayOfInt[1]));
      } else {
        int[] arrayOfInt1 = arrayList.get(i + 1);
        arrayList1.add(new Point2(arrayOfInt1[0] - arrayOfInt[0], arrayOfInt1[1] - arrayOfInt[1]));
      } 
      if (i == 0)
        arrayList1.add(new Point2(paramBuilding1.tileX() - arrayOfInt[0], paramBuilding1.tileY() - arrayOfInt[1])); 
      paramUnit.addBuild(new BuildPlan(arrayOfInt[0], arrayOfInt[1], 0, block, arrayList1.toArray(new Point2[0])), true);
    } 
    if (Vars.ui != null) {
      String str = LunariansI18n.getLanguage();
      Vars.ui.showInfoToast("pt".equals(str) ? ("Auto energia: ligando " + paramBuilding1.block.localizedName + " (" + b1 + " nos)") : ("es".equals(str) ? ("Auto energía: conectando " + paramBuilding1.block.localizedName + " (" + b1 + " nodos)") : ("ru".equals(str) ? ("Автоэнергия: " + paramBuilding1.block.localizedName + " (" + b1 + ")") : ("Auto power: linking " + paramBuilding1.block.localizedName + " (" + b1 + " nodes)"))), 3.0F);
    } 
  }
}
