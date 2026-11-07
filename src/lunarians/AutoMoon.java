package lunarians;

import arc.Events;
import arc.func.Cons;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.struct.IntFloatMap;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.data.MoonSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;

public class AutoMoon {
  private static final float SCAN = 0.55F;
  
  private static final float ATTACK_RANGE_PAD = 1.15F;
  
  private static float timer;
  
  private static int count;
  
  private static boolean errorLogged;
  
  private static final IntSet claimed = new IntSet();
  
  private static final IntFloatMap angles = new IntFloatMap();
  
  public static void init() {
    if (Vars.headless)
      return; 
    Events.run(EventType.Trigger.update, new Runnable() {
          public void run() {
            AutoMoon.update();
          }
        });
    Events.on(EventType.WorldLoadEvent.class, new Cons<EventType.WorldLoadEvent>() {
          public void get(EventType.WorldLoadEvent param1WorldLoadEvent) {
            AutoMoon.reset();
          }
        });
  }
  
  public static void reset() {
    timer = 0.0F;
    count = 0;
    claimed.clear();
    angles.clear();
  }
  
  public static int count() {
    return count;
  }
  
  private static void update() {
    if (!LunariansSettings.enableAutoMoon || Vars.state == null || !Vars.state.isGame() || Vars.player == null)
      return; 
    timer += Time.delta / 60.0F;
    if (timer < LunariansSettings.actionInterval(0.55F))
      return; 
    float f = timer;
    timer = 0.0F;
    try {
      scan(f);
    } catch (Throwable throwable) {
      if (!errorLogged) {
        errorLogged = true;
        Log.err("[Lunarians] AutoMoon error", throwable);
      } 
    } 
  }
  
  private static boolean isConstructing(Unit paramUnit) {
    if (paramUnit == null || !paramUnit.isValid())
      return false; 
    try {
      if (paramUnit.activelyBuilding())
        return true; 
      if (paramUnit.isBuilding())
        return true; 
      if (paramUnit.plans != null && paramUnit.plans.size > 0)
        return true; 
    } catch (Throwable throwable) {}
    return false;
  }
  
  private static BuildPlan currentPlan(Unit paramUnit) {
    if (paramUnit == null)
      return null; 
    try {
      BuildPlan buildPlan = paramUnit.buildPlan();
      if (buildPlan != null)
        return buildPlan; 
      if (paramUnit.plans != null && paramUnit.plans.size > 0)
        return (BuildPlan)paramUnit.plans.first(); 
    } catch (Throwable throwable) {}
    return null;
  }
  
  private static void resolveCenter(MoonSlot paramMoonSlot, float[] paramArrayOffloat) {
    if (paramMoonSlot.hasUnitCenter()) {
      Unit unit = (Unit)Groups.unit.getByID(paramMoonSlot.followId);
      if (unit != null && unit.isValid()) {
        paramArrayOffloat[0] = unit.x;
        paramArrayOffloat[1] = unit.y;
        return;
      } 
    } 
    if (paramMoonSlot.hasTileCenter()) {
      paramArrayOffloat[0] = paramMoonSlot.x * 8.0F + 4.0F;
      paramArrayOffloat[1] = paramMoonSlot.y * 8.0F + 4.0F;
      return;
    } 
    if (Vars.player.unit() != null && Vars.player.unit().isValid()) {
      paramArrayOffloat[0] = (Vars.player.unit()).x;
      paramArrayOffloat[1] = (Vars.player.unit()).y;
    } else {
      paramArrayOffloat[0] = Vars.player.x;
      paramArrayOffloat[1] = Vars.player.y;
    } 
  }
  
  private static Unit findBuildingHost(MoonSlot paramMoonSlot) {
    if (paramMoonSlot.hasUnitCenter()) {
      Unit unit1 = (Unit)Groups.unit.getByID(paramMoonSlot.followId);
      if (isConstructing(unit1))
        return unit1; 
    } 
    Unit unit = Vars.player.unit();
    if (isConstructing(unit))
      return unit; 
    return null;
  }
  
  private static void scan(float paramFloat) {
    Seq<MoonSlot> seq = LunariansSettings.moonSlots;
    if (seq.isEmpty())
      return; 
    claimed.clear();
    count = 0;
    int i = Groups.unit.size();
    float[] arrayOfFloat = new float[2];
    for (byte b = 0; b < seq.size; b++) {
      MoonSlot moonSlot = (MoonSlot)seq.get(b);
      if (moonSlot.ready()) {
        resolveCenter(moonSlot, arrayOfFloat);
        float f1 = arrayOfFloat[0], f2 = arrayOfFloat[1];
        float f3 = moonSlot.radius * 8.0F;
        float f4 = Math.max(0.25F, Math.min(4.0F, moonSlot.speed));
        Seq<Unit> seq1 = new Seq();
        byte b1;
        for (b1 = 0; b1 < i; b1++) {
          Unit unit = (Unit)Groups.unit.index(b1);
          if (unit != null && unit.isValid() && unit.team == Vars.player.team() && 
            !claimed.contains(unit.id) && (
            !moonSlot.hasUnitCenter() || unit.id != moonSlot.followId) && 
            !AutoUnitHeal.isActiveHealer(unit.id) && 
            unit.controller() instanceof CommandAI && 
            unit.isFlying() && 
            moonSlot.unit.equals(unit.type.name))
            seq1.add(unit); 
        } 
        if (moonSlot.maxUnits > 0 && seq1.size > moonSlot.maxUnits)
          seq1.truncate(moonSlot.maxUnits); 
        for (b1 = 0; b1 < seq1.size; ) {
          claimed.add(((Unit)seq1.get(b1)).id);
          b1++;
        } 
        if (!seq1.isEmpty()) {
          Unit unit = findBuildingHost(moonSlot);
          if (unit != null) {
            assistBuild(seq1, unit);
            count += seq1.size;
          } else {
            float f = Math.max(0.25F, 2.2F / Math.max(1.0F, moonSlot.radius)) * paramFloat * f4;
            for (byte b2 = 0; b2 < seq1.size; b2++) {
              Unit unit1 = (Unit)seq1.get(b2);
              float f5 = f3 + Math.max(24.0F, unit1.type.maxRange) * 1.15F;
              Unit unit2 = nearestEnemy(unit1, f1, f2, f5);
              Building building = (unit2 == null) ? nearestEnemyBuild(unit1, f1, f2, f5) : null;
              CommandAI commandAI = (CommandAI)unit1.controller();
              if (commandAI.command != UnitCommand.moveCommand)
                Call.setUnitCommand(Vars.player, new int[] { unit1.id }, UnitCommand.moveCommand); 
              if (unit2 != null) {
                float f6 = Math.max(24.0F, unit1.type.maxRange) * 0.9F;
                float f7 = unit2.angleTo((Position)unit1) * 0.017453292F;
                Call.commandUnits(Vars.player, new int[] { unit1.id }, null, unit2, new Vec2(unit2.x + 
                      Mathf.cos(f7) * f6, unit2.y + Mathf.sin(f7) * f6), false, true);
                count++;
              } else if (building != null) {
                float f6 = Math.max(24.0F, unit1.type.maxRange) * 0.9F;
                float f7 = building.angleTo((Position)unit1) * 0.017453292F;
                Call.commandUnits(Vars.player, new int[] { unit1.id }, building, null, new Vec2(building.x + 
                      Mathf.cos(f7) * f6, building.y + Mathf.sin(f7) * f6), false, true);
                count++;
              } else {
                float f6 = angles.get(unit1.id, b2 * 6.2831855F / Math.max(1, seq1.size));
                f6 += f;
                if (f6 > 6.2831855F)
                  f6 -= 6.2831855F; 
                angles.put(unit1.id, f6);
                Call.commandUnits(Vars.player, new int[] { unit1.id }, null, null, new Vec2(f1 + 
                      Mathf.cos(f6) * f3, f2 + Mathf.sin(f6) * f3), false, true);
                count++;
              } 
            } 
          } 
        } 
      } 
    } 
  }
  
  private static void assistBuild(Seq<Unit> paramSeq, Unit paramUnit) {
    BuildPlan buildPlan = currentPlan(paramUnit);
    float f1 = paramUnit.x, f2 = paramUnit.y;
    if (buildPlan != null)
      try {
        f1 = buildPlan.x * 8.0F + 4.0F;
        f2 = buildPlan.y * 8.0F + 4.0F;
      } catch (Throwable throwable) {
        f1 = paramUnit.x;
        f2 = paramUnit.y;
      }  
    IntSeq intSeq1 = new IntSeq();
    IntSeq intSeq2 = new IntSeq();
    byte b;
    for (b = 0; b < paramSeq.size; b++) {
      Unit unit = (Unit)paramSeq.get(b);
      if (unit.type == null || unit.type.buildSpeed <= 0.0F) {
        intSeq2.add(unit.id);
      } else {
        intSeq1.add(unit.id);
      } 
    } 
    if (intSeq1.size > 0) {
      UnitCommand unitCommand = UnitCommand.assistCommand;
      Call.setUnitCommand(Vars.player, intSeq1.toArray(), unitCommand);
    } 
    for (b = 0; b < paramSeq.size; b++) {
      Unit unit = (Unit)paramSeq.get(b);
      float f3 = Math.max(24.0F, (unit.type != null) ? unit.type.buildRange : 40.0F) * 0.7F;
      float f4 = b * 360.0F / Math.max(1, paramSeq.size) * 0.017453292F;
      float f5 = Mathf.cos(f4) * Math.min(18.0F, f3 * 0.3F);
      float f6 = Mathf.sin(f4) * Math.min(18.0F, f3 * 0.3F);
      Call.commandUnits(Vars.player, new int[] { unit.id }, null, null, new Vec2(f1 + f5, f2 + f6), false, true);
    } 
  }
  
  private static Unit nearestEnemy(Unit paramUnit, float paramFloat1, float paramFloat2, float paramFloat3) {
    Unit unit = null;
    float f = paramFloat3;
    int i = Groups.unit.size();
    for (byte b = 0; b < i; b++) {
      Unit unit1 = (Unit)Groups.unit.index(b);
      if (unit1 != null && unit1.isValid() && unit1.team != paramUnit.team && 
        Mathf.dst(paramFloat1, paramFloat2, unit1.x, unit1.y) <= paramFloat3) {
        float f1 = paramUnit.dst((Position)unit1);
        if (f1 < f) {
          f = f1;
          unit = unit1;
        } 
      } 
    } 
    return unit;
  }
  
  private static Building nearestEnemyBuild(Unit paramUnit, float paramFloat1, float paramFloat2, float paramFloat3) {
    Building building = null;
    float f = paramFloat3;
    int i = Groups.build.size();
    for (byte b = 0; b < i; b++) {
      Building building1 = (Building)Groups.build.index(b);
      if (building1 != null && building1.isValid() && building1.team != paramUnit.team && 
        Mathf.dst(paramFloat1, paramFloat2, building1.x, building1.y) <= paramFloat3) {
        float f1 = paramUnit.dst((Position)building1);
        if (f1 < f) {
          f = f1;
          building = building1;
        } 
      } 
    } 
    return building;
  }
}
