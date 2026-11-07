package lunarians;

import arc.Events;
import arc.func.Cons;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.data.HealSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.logic.Ranged;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.MendProjector;
import mindustry.world.blocks.units.RepairTower;
import mindustry.world.blocks.units.RepairTurret;

public class AutoHeal {
  private static final float SCAN = 0.25F;
  
  private static float timer;
  
  private static int count;
  
  private static boolean errorLogged;
  
  private static final IntSet healing = new IntSet();
  
  private static final Vec2 tmp = new Vec2();
  
  public static void init() {
    if (Vars.headless)
      return; 
    Events.run(EventType.Trigger.update, new Runnable() {
          public void run() {
            AutoHeal.update();
          }
        });
    Events.on(EventType.WorldLoadEvent.class, new Cons<EventType.WorldLoadEvent>() {
          public void get(EventType.WorldLoadEvent param1WorldLoadEvent) {
            AutoHeal.reset();
          }
        });
  }
  
  public static void reset() {
    timer = 0.0F;
    count = 0;
    healing.clear();
  }
  
  public static int count() {
    return count;
  }
  
  public static boolean isHealing(int paramInt) {
    return healing.contains(paramInt);
  }
  
  public static float hpFrac(Unit paramUnit) {
    return paramUnit.health / Math.max(1.0F, paramUnit.maxHealth);
  }
  
  public static boolean needsHeal(Unit paramUnit) {
    return (hpFrac(paramUnit) < LunariansSettings.autoHealPercent / 100.0F);
  }
  
  public static boolean tryHeal(Unit paramUnit) {
    if (paramUnit == null || !paramUnit.isValid() || Vars.player == null)
      return false; 
    if (!needsHeal(paramUnit))
      return false; 
    if (!(paramUnit.controller() instanceof CommandAI))
      return false; 
    Building building = findNearestHeal(paramUnit);
    if (building == null)
      return false; 
    float f = healRadius(building);
    if (f <= 0.0F)
      f = 40.0F; 
    if (paramUnit.dst((Position)building) <= f * 0.9F && isGoodFloor(paramUnit.tileX(), paramUnit.tileY(), paramUnit)) {
      healing.add(paramUnit.id);
      return true;
    } 
    Vec2 vec2 = findFloorStand(building, paramUnit, f);
    if (vec2 == null)
      return false; 
    CommandAI commandAI = (CommandAI)paramUnit.controller();
    if (commandAI.command != UnitCommand.moveCommand)
      Call.setUnitCommand(Vars.player, new int[] { paramUnit.id }, UnitCommand.moveCommand); 
    Call.commandUnits(Vars.player, new int[] { paramUnit.id }, null, null, new Vec2(vec2.x, vec2.y), false, true);
    healing.add(paramUnit.id);
    return true;
  }
  
  private static Vec2 findFloorStand(Building paramBuilding, Unit paramUnit, float paramFloat) {
    int i = paramBuilding.tileX();
    int j = paramBuilding.tileY();
    int k = Math.max(2, Mathf.ceil(paramFloat / 8.0F));
    int m = Math.max(1, Mathf.floor(paramBuilding.block.size / 2.0F) + 1);
    Vec2 vec2 = null;
    float f = Float.MAX_VALUE;
    for (int n = m; n <= k; n++) {
      for (int i1 = -n; i1 <= n; i1++) {
        for (int i2 = -n; i2 <= n; i2++) {
          if (Math.max(Math.abs(i1), Math.abs(i2)) == n || n <= m) {
            int i3 = i + i1;
            int i4 = j + i2;
            if (Vars.world.tiles.in(i3, i4)) {
              float f1 = i3 * 8.0F + 4.0F;
              float f2 = i4 * 8.0F + 4.0F;
              if (Mathf.dst(f1, f2, paramBuilding.x, paramBuilding.y) <= paramFloat * 0.9F && 
                isGoodFloor(i3, i4, paramUnit)) {
                int i5 = countWallNeighbors(i3, i4);
                float f3 = i5 * 40.0F + paramUnit.dst(f1, f2) * 0.05F + n * 2.0F;
                if (f3 < f) {
                  f = f3;
                  if (vec2 == null)
                    vec2 = new Vec2(); 
                  vec2.set(f1, f2);
                } 
              } 
            } 
          } 
        } 
      } 
      if (vec2 != null && f < 40.0F)
        break; 
    } 
    if (vec2 == null) {
      Vec2 vec21 = new Vec2(paramUnit.x - paramBuilding.x, paramUnit.y - paramBuilding.y);
      if (vec21.len() < 0.1F)
        vec21.set(1.0F, 0.0F); 
      vec21.nor();
      float f1;
      for (f1 = Math.max(16.0F, paramBuilding.block.size * 8.0F); f1 <= paramFloat * 0.9F; f1 += 8.0F) {
        float f2 = paramBuilding.x + vec21.x * f1;
        float f3 = paramBuilding.y + vec21.y * f1;
        int i1 = Mathf.floor(f2 / 8.0F);
        int i2 = Mathf.floor(f3 / 8.0F);
        if (isGoodFloor(i1, i2, paramUnit)) {
          vec2 = new Vec2(i1 * 8.0F + 4.0F, i2 * 8.0F + 4.0F);
          break;
        } 
      } 
    } 
    return vec2;
  }
  
  private static boolean isGoodFloor(int paramInt1, int paramInt2, Unit paramUnit) {
    if (!Vars.world.tiles.in(paramInt1, paramInt2))
      return false; 
    Tile tile = Vars.world.tile(paramInt1, paramInt2);
    if (tile == null)
      return false; 
    if (tile.build != null)
      return false; 
    if (!tile.passable())
      return false; 
    if (!paramUnit.isFlying() && tile.solid())
      return false; 
    if (tile.floor() == null)
      return false; 
    if (!paramUnit.isFlying() && (paramUnit.type == null || !paramUnit.type.naval) && 
      (tile.floor()).isLiquid && (tile.floor()).drownTime > 0.0F)
      return false; 
    return true;
  }
  
  private static int countWallNeighbors(int paramInt1, int paramInt2) {
    byte b = 0;
    for (byte b1 = -1; b1 <= 1; b1++) {
      for (byte b2 = -1; b2 <= 1; b2++) {
        if (b1 != 0 || b2 != 0)
          if (!Vars.world.tiles.in(paramInt1 + b1, paramInt2 + b2)) {
            b++;
          } else {
            Tile tile = Vars.world.tile(paramInt1 + b1, paramInt2 + b2);
            if (tile == null || tile.solid() || (tile.build != null && tile.build.block != null && tile.build.block.solid))
              b++; 
          }  
      } 
    } 
    return b;
  }
  
  public static Building findNearestHeal(Unit paramUnit) {
    Building building = null;
    float f = Float.MAX_VALUE;
    int i = Groups.build.size();
    for (byte b = 0; b < i; b++) {
      Building building1 = (Building)Groups.build.index(b);
      if (building1 != null && building1.isValid() && building1.team == paramUnit.team) {
        float f1 = healRadius(building1);
        if (f1 > 0.0F) {
          float f2 = paramUnit.dst((Position)building1) - f1 * 0.25F;
          if (f2 < f) {
            f = f2;
            building = building1;
          } 
        } 
      } 
    } 
    return building;
  }
  
  private static float healRadius(Building paramBuilding) {
    if (paramBuilding.block instanceof RepairTurret)
      return ((RepairTurret)paramBuilding.block).repairRadius; 
    if (paramBuilding.block instanceof RepairTower)
      return ((RepairTower)paramBuilding.block).range; 
    if (paramBuilding.block instanceof MendProjector)
      return ((MendProjector)paramBuilding.block).range; 
    if (paramBuilding instanceof Ranged) {
      String str = paramBuilding.block.name.toLowerCase();
      if (str.contains("repair") || str.contains("mend") || str.contains("heal") || str.contains("regen")) {
        float f = ((Ranged)paramBuilding).range();
        return (f > 0.0F) ? f : 40.0F;
      } 
    } 
    return 0.0F;
  }
  
  private static void update() {
    if (!LunariansSettings.enableAutoHeal || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
      healing.clear();
      return;
    } 
    timer += Time.delta / 60.0F;
    if (timer < LunariansSettings.actionInterval(0.25F))
      return; 
    timer = 0.0F;
    healing.clear();
    count = 0;
    try {
      scan();
    } catch (Throwable throwable) {
      if (!errorLogged) {
        errorLogged = true;
        Log.err("[Lunarians] AutoHeal error", throwable);
      } 
    } 
  }
  
  private static void scan() {
    Seq<HealSlot> seq = LunariansSettings.healSlots;
    boolean bool = seq.isEmpty();
    int i = Groups.unit.size();
    if (bool) {
      for (byte b1 = 0; b1 < i; b1++) {
        Unit unit = (Unit)Groups.unit.index(b1);
        if (unit != null && unit.isValid() && unit.team == Vars.player.team() && 
          unit.controller() instanceof CommandAI && 
          tryHeal(unit))
          count++; 
      } 
      return;
    } 
    for (byte b = 0; b < seq.size; b++) {
      HealSlot healSlot = (HealSlot)seq.get(b);
      if (healSlot.ready()) {
        byte b1 = 0;
        for (byte b2 = 0; b2 < i; b2++) {
          Unit unit = (Unit)Groups.unit.index(b2);
          if (unit != null && unit.isValid() && unit.team == Vars.player.team() && 
            healSlot.unit.equals(unit.type.name) && 
            unit.controller() instanceof CommandAI) {
            if (healSlot.maxUnits > 0 && b1 >= healSlot.maxUnits)
              break; 
            if (tryHeal(unit)) {
              b1++;
              count++;
            } 
          } 
        } 
      } 
    } 
  }
}
