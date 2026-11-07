package lunarians;

import arc.Core;
import arc.Events;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import java.lang.reflect.Field;
import lunarians.data.UnitHealSlot;
import mindustry.Vars;
import mindustry.entities.Units;
import mindustry.entities.abilities.Ability;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

public class AutoUnitHeal {
  private static final float SCAN = 0.8F;
  
  private static float timer;
  
  private static int count;
  
  private static boolean errorLogged;
  
  private static final IntSet claimed;
  
  public static final IntSet activeHealers;
  
  public static void init() {
    if (Vars.headless)
      return; 
    Events.run(EventType.Trigger.update, AutoUnitHeal::update);
    Events.on(EventType.WorldLoadEvent.class, paramWorldLoadEvent -> reset());
  }
  
  public static void reset() {
    timer = 0.0F;
    count = 0;
    claimed.clear();
    activeHealers.clear();
    UnitCoord.reset();
  }
  
  public static int count() {
    return count;
  }
  
  private static boolean weaponHeals(Weapon paramWeapon) {
    try {
      Field field = paramWeapon.getClass().getField("healPercent");
      if (field.getFloat(paramWeapon) > 0.0F)
        return true; 
    } catch (Throwable throwable) {}
    try {
      Field field = paramWeapon.getClass().getField("healAmount");
      if (field.getFloat(paramWeapon) > 0.0F)
        return true; 
    } catch (Throwable throwable) {}
    try {
      Field field;
      if (paramWeapon.bullet != null && (field = paramWeapon.bullet.getClass().getField("healPercent")).getFloat(paramWeapon.bullet) > 0.0F)
        return true; 
    } catch (Throwable throwable) {}
    return false;
  }
  
  public static boolean isHealerType(UnitType paramUnitType) {
    if (paramUnitType == null)
      return false; 
    try {
      String str = paramUnitType.name;
      if (str != null) {
        if ((str = str.toLowerCase()).equals("vela") || str.contains("vela"))
          return true; 
        if (str.equals("mega") || str.equals("poly") || str.equals("nova"))
          return true; 
        if (str.contains("heal") || str.contains("repair"))
          return true; 
      } 
    } catch (Throwable throwable) {}
    try {
      if (paramUnitType.weapons != null)
        for (byte b1 = 0; b1 < paramUnitType.weapons.size; ) {
          Weapon weapon = (Weapon)paramUnitType.weapons.get(b1);
          if (weapon == null || !weaponHeals(weapon)) {
            b1++;
            continue;
          } 
          return true;
        }  
    } catch (Throwable throwable) {}
    if (paramUnitType.abilities == null)
      return false; 
    for (byte b = 0; b < paramUnitType.abilities.size; b++) {
      Ability ability = (Ability)paramUnitType.abilities.get(b);
      if (ability != null) {
        if (ability instanceof mindustry.entities.abilities.RepairFieldAbility)
          return true; 
        if (!(ability instanceof mindustry.entities.abilities.UnitSpawnAbility)) {
          String str = ability.getClass().getSimpleName().toLowerCase();
          if (str.contains("repair") || str.contains("heal") || str.contains("mend"))
            return true; 
        } 
      } 
    } 
    return false;
  }
  
  public static boolean isActiveHealer(int paramInt) {
    return activeHealers.contains(paramInt);
  }
  
  private static void update() {
    if (!LunariansSettings.enableAutoUnitHeal || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
      activeHealers.clear();
      return;
    } 
    if ((timer += Time.delta / 60.0F) < LunariansSettings.actionInterval(0.8F))
      return; 
    timer = 0.0F;
    try {
      scan();
    } catch (Throwable throwable) {
      if (!errorLogged) {
        errorLogged = true;
        Log.err("[Lunarians] AutoUnitHeal error", throwable);
      } 
    } 
  }
  
  private static final IntSet targeted = new IntSet();
  
  private static final IntSet escortUsed = new IntSet();
  
  private static void scan() {
    Seq<UnitHealSlot> seq = LunariansSettings.unitHealSlots;
    claimed.clear();
    activeHealers.clear();
    targeted.clear();
    escortUsed.clear();
    count = 0;
    if (seq == null || seq.isEmpty())
      return; 
    Seq<Unit> seq1 = new Seq();
    int i = Groups.unit.size();
    byte b;
    for (b = 0; b < i; b++) {
      Unit unit = (Unit)Groups.unit.index(b);
      if (unit != null && unit.isValid() && unit.team == Vars.player.team() && unit.health / Math.max(1.0F, unit.maxHealth) < 0.95F)
        seq1.add(unit); 
    } 
    if (seq1.isEmpty())
      return; 
    for (b = 0; b < seq.size; b++) {
      UnitHealSlot unitHealSlot = (UnitHealSlot)seq.get(b);
      if (unitHealSlot != null && unitHealSlot.ready()) {
        Seq seq2 = new Seq();
        byte b1;
        for (b1 = 0; b1 < i; b1++) {
          Unit unit = (Unit)Groups.unit.index(b1);
          if (unit != null && unit.isValid() && unit.team == Vars.player.team() && !claimed.contains(unit.id) && unit.controller() instanceof mindustry.ai.types.CommandAI && unit.type != null && unitHealSlot.unit.equals(unit.type.name))
            seq2.add(unit); 
        } 
        if (unitHealSlot.maxUnits > 0 && seq2.size > unitHealSlot.maxUnits)
          seq2.truncate(unitHealSlot.maxUnits); 
        for (b1 = 0; b1 < seq2.size; b1++) {
          claimed.add(((Unit)seq2.get(b1)).id);
          activeHealers.add(((Unit)seq2.get(b1)).id);
        } 
        for (b1 = 0; b1 < seq2.size; b1++) {
          Unit unit1 = (Unit)seq2.get(b1);
          Unit unit2 = nearestNeedy(unit1, seq1);
          if (unit2 != null) {
            targeted.add(unit2.id);
            float f = unit2.angleTo((Position)unit1) * 0.017453292F;
            Vec2 vec2 = PathSafe.safeNear(unit1, unit2.x + (float)Math.cos(f) * 24.0F, unit2.y + (float)Math.sin(f) * 24.0F, 40.0F);
            if (UnitCoord.cmd(unit1, 3, 90.0F, null, unit2, vec2))
              count++; 
            if (escortOn())
              escort(unit1, unit2); 
          } 
        } 
      } 
    } 
  }
  
  public static boolean escortOn() {
    return Core.settings.getBool("lunarians-heal-escort", true);
  }
  
  private static void escort(Unit paramUnit1, Unit paramUnit2) {
    float f1 = paramUnit1.x;
    float f2 = paramUnit1.y;
    float f3 = paramUnit2.x - f1;
    float f4 = paramUnit2.y - f2;
    float f5 = (float)Math.sqrt((f3 * f3 + f4 * f4));
    if (f5 < 8.0F) {
      f3 = 1.0F;
      f4 = 0.0F;
      f5 = 8.0F;
    } else {
      f3 /= f5;
      f4 /= f5;
    } 
    float f6 = -f4;
    float f7 = f3;
    Seq seq = new Seq();
    int i = Groups.unit.size();
    for (byte b1 = 0; b1 < i; b1++) {
      Unit unit = (Unit)Groups.unit.index(b1);
      if (unit != null && unit.isValid() && unit.team == Vars.player.team() && unit != paramUnit1 && unit != paramUnit2 && !escortUsed.contains(unit.id) && !activeHealers.contains(unit.id) && !isHealerType(unit.type) && unit.controller() instanceof mindustry.ai.types.CommandAI && UnitCoord.holder(unit.id) < 3 && (unit.dst(f1, f2) <= 320.0F || unit.dst(paramUnit2.x, paramUnit2.y) <= 320.0F))
        seq.add(unit); 
    } 
    if (seq.isEmpty())
      return; 
    float f8 = f1;
    float f9 = f2;
    float f10 = f3;
    float f11 = f4;
    float f12 = f6;
    float f13 = f7;
    seq.sort(paramUnit -> Math.abs((paramUnit.x - paramFloat1) * paramFloat2 + (paramUnit.y - paramFloat3) * paramFloat4));
    if (seq.size > 8)
      seq.truncate(8); 
    Teamc teamc = Units.closestTarget(Vars.player.team(), paramUnit2.x, paramUnit2.y, 320.0F, paramUnit -> true, paramBuilding -> paramBuilding.block instanceof mindustry.world.blocks.defense.turrets.Turret);
    float f14 = (teamc != null) ? (float)Math.atan2((teamc.y() - paramUnit2.y), (teamc.x() - paramUnit2.x)) : 0.0F;
    byte b2 = 0;
    byte b3 = 0;
    byte b4;
    for (b4 = 0; b4 < seq.size; b4++) {
      Unit unit = (Unit)seq.get(b4);
      float f15 = (unit.x - f1) * f3 + (unit.y - f2) * f4;
      float f16 = (unit.x - f1) * f6 + (unit.y - f2) * f7;
      if (f15 <= -24.0F || f15 >= f5 + 24.0F || Math.abs(f16) >= 28.0F)
        b3++; 
    } 
    for (b4 = 0; b4 < seq.size; b4++) {
      Unit unit = (Unit)seq.get(b4);
      float f15 = (unit.x - f1) * f3 + (unit.y - f2) * f4;
      float f16 = (unit.x - f1) * f6 + (unit.y - f2) * f7;
      if (f15 > -24.0F && f15 < f5 + 24.0F && Math.abs(f16) < 28.0F) {
        float f = (f16 >= 0.0F) ? 1.0F : -1.0F;
        vec2 = new Vec2(f1 + f3 * f15 + f6 * f * 44.0F, f2 + f4 * f15 + f7 * f * 44.0F);
      } else if (teamc != null) {
        float f17 = f14 + (b2 - (b3 - 1) * 0.5F) * 0.45F;
        float f18 = 26.0F + (b2 % 2) * 10.0F;
        vec2 = new Vec2(paramUnit2.x + (float)Math.cos(f17) * f18, paramUnit2.y + (float)Math.sin(f17) * f18);
        b2++;
      } else {
        continue;
      } 
      Vec2 vec2 = PathSafe.safeNear(unit, vec2.x, vec2.y, 48.0F);
      escortUsed.add(unit.id);
      if (UnitCoord.cmd(unit, 2, 90.0F, null, null, vec2))
        count++; 
      continue;
    } 
  }
  
  private static Unit nearestNeedy(Unit paramUnit, Seq<Unit> paramSeq) {
    Unit unit1 = null;
    float f1 = Float.MAX_VALUE;
    Unit unit2 = null;
    float f2 = Float.MAX_VALUE;
    for (byte b = 0; b < paramSeq.size; b++) {
      Unit unit = (Unit)paramSeq.get(b);
      if (unit.id != paramUnit.id && !activeHealers.contains(unit.id)) {
        float f = paramUnit.dst((Position)unit) * (0.5F + unit.health / Math.max(1.0F, unit.maxHealth));
        if (f < f2) {
          f2 = f;
          unit2 = unit;
        } 
        if (!targeted.contains(unit.id) && f < f1) {
          f1 = f;
          unit1 = unit;
        } 
      } 
    } 
    return (unit1 != null) ? unit1 : unit2;
  }
  
  static {
    claimed = new IntSet();
    activeHealers = new IntSet();
  }
}
