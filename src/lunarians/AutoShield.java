package lunarians;

import arc.Events;
import arc.func.Cons;
import arc.math.geom.Vec2;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.data.ShieldSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.entities.abilities.Ability;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class AutoShield {
  private static final float SCAN = 0.6F;
  
  private static float timer;
  
  private static int count;
  
  private static boolean errorLogged;
  
  private static final IntSet claimed = new IntSet();
  
  public static void init() {
    if (Vars.headless)
      return; 
    Events.run(EventType.Trigger.update, new Runnable() {
          public void run() {
            AutoShield.update();
          }
        });
    Events.on(EventType.WorldLoadEvent.class, new Cons<EventType.WorldLoadEvent>() {
          public void get(EventType.WorldLoadEvent param1WorldLoadEvent) {
            AutoShield.reset();
          }
        });
  }
  
  public static void reset() {
    timer = 0.0F;
    count = 0;
  }
  
  public static int count() {
    return count;
  }
  
  public static boolean hasShieldAbility(UnitType paramUnitType) {
    if (paramUnitType == null || paramUnitType.abilities == null)
      return false; 
    for (byte b = 0; b < paramUnitType.abilities.size; b++) {
      Ability ability = (Ability)paramUnitType.abilities.get(b);
      if (ability instanceof mindustry.entities.abilities.ForceFieldAbility)
        return true; 
      if (ability instanceof mindustry.entities.abilities.ShieldArcAbility)
        return true; 
      if (ability instanceof mindustry.entities.abilities.ShieldRegenFieldAbility)
        return true; 
      if (ability.getClass().getSimpleName().toLowerCase().contains("shield"))
        return true; 
    } 
    return false;
  }
  
  private static void update() {
    if (!LunariansSettings.enableAutoShield || Vars.state == null || !Vars.state.isGame() || Vars.player == null)
      return; 
    timer += Time.delta / 60.0F;
    if (timer < LunariansSettings.actionInterval(0.6F))
      return; 
    timer = 0.0F;
    try {
      scan();
    } catch (Throwable throwable) {
      if (!errorLogged) {
        errorLogged = true;
        Log.err("[Lunarians] AutoShield error", throwable);
      } 
    } 
  }
  
  private static void scan() {
    Seq<ShieldSlot> seq = LunariansSettings.shieldSlots;
    if (seq.isEmpty())
      return; 
    count = 0;
    claimed.clear();
    int i = Groups.unit.size();
    for (byte b = 0; b < seq.size; b++) {
      ShieldSlot shieldSlot = (ShieldSlot)seq.get(b);
      if (shieldSlot.ready()) {
        Unit unit = (Unit)Groups.unit.getByID(shieldSlot.followId);
        if (unit != null && unit.isValid()) {
          shieldSlot.followLabel = unit.type.localizedName;
          Seq seq1 = new Seq();
          IntSeq intSeq = new IntSeq();
          int j;
          for (j = 0; j < i; j++) {
            Unit unit1 = (Unit)Groups.unit.index(j);
            if (unit1 != null && unit1.isValid() && unit1.team == Vars.player.team() && 
              shieldSlot.unit.equals(unit1.type.name) && 
              unit1.controller() instanceof CommandAI && 
              unit1.id != unit.id) {
              seq1.add(unit1);
              CommandAI commandAI = (CommandAI)unit1.controller();
              if (commandAI.command != UnitCommand.moveCommand)
                intSeq.add(unit1.id); 
            } 
          } 
          if (shieldSlot.maxUnits > 0 && seq1.size > shieldSlot.maxUnits)
            seq1.truncate(shieldSlot.maxUnits); 
          for (j = seq1.size - 1; j >= 0; j--) {
            if (claimed.contains(((Unit)seq1.get(j)).id))
              seq1.remove(j); 
          } 
          if (!seq1.isEmpty()) {
            for (j = 0; j < seq1.size; ) {
              claimed.add(((Unit)seq1.get(j)).id);
              j++;
            } 
            if (intSeq.size > 0)
              Call.setUnitCommand(Vars.player, intSeq.toArray(), UnitCommand.moveCommand); 
            for (j = 0; j < seq1.size; j++) {
              Unit unit1 = (Unit)seq1.get(j);
              float f1 = (seq1.size <= 1) ? 0.0F : (j * 360.0F / seq1.size * 0.017453292F);
              float f2 = (float)Math.cos(f1) * 18.0F;
              float f3 = (float)Math.sin(f1) * 18.0F;
              Vec2 vec2 = PathSafe.safeNear(unit1, unit.x + f2, unit.y + f3, 48.0F);
              Call.commandUnits(Vars.player, new int[] { unit1.id }, null, null, vec2, false, true);
            } 
            count += seq1.size;
          } 
        } 
      } 
    } 
  }
}
