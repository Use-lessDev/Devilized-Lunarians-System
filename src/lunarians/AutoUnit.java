package lunarians;

import arc.Events;
import arc.func.Cons;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.data.UnitSlot;
import mindustry.Vars;
import mindustry.ai.ItemUnitStance;
import mindustry.ai.UnitCommand;
import mindustry.ai.UnitStance;
import mindustry.ai.types.CommandAI;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class AutoUnit {
  private static final float SCAN = 1.5F;
  
  private static final IntSet claimed = new IntSet();
  
  private static float timer;
  
  private static int count;
  
  private static boolean errorLogged;
  
  public static void init() {
    if (Vars.headless)
      return; 
    Events.run(EventType.Trigger.update, new Runnable() {
          public void run() {
            AutoUnit.update();
          }
        });
    Events.on(EventType.WorldLoadEvent.class, new Cons<EventType.WorldLoadEvent>() {
          public void get(EventType.WorldLoadEvent param1WorldLoadEvent) {
            AutoUnit.reset();
          }
        });
  }
  
  public static void reset() {
    claimed.clear();
    timer = 0.0F;
    count = 0;
  }
  
  public static int count() {
    return count;
  }
  
  private static void update() {
    if (!LunariansSettings.enableAutoUnit || Vars.state == null || !Vars.state.isGame() || Vars.player == null)
      return; 
    timer += Time.delta / 60.0F;
    if (timer < LunariansSettings.actionInterval(1.5F))
      return; 
    timer = 0.0F;
    try {
      scan();
    } catch (Throwable throwable) {
      if (!errorLogged) {
        errorLogged = true;
        Log.err("[Lunarians] AutoUnit error", throwable);
      } 
    } 
  }
  
  private static void scan() {
    Seq<UnitSlot> seq = LunariansSettings.unitSlots;
    if (seq.isEmpty())
      return; 
    claimed.clear();
    count = 0;
    for (byte b = 0; b < seq.size; b++) {
      UnitSlot unitSlot = (UnitSlot)seq.get(b);
      if (unitSlot.ready()) {
        Seq<Unit> seq1 = new Seq();
        int i = Groups.unit.size();
        byte b1;
        for (b1 = 0; b1 < i; b1++) {
          Unit unit = (Unit)Groups.unit.index(b1);
          if (unit != null && unit.isValid() && unit.team == Vars.player.team() && 
            !claimed.contains(unit.id) && 
            unit.controller() instanceof CommandAI && 
            unitSlot.unit.equals(unit.type.name))
            seq1.add(unit); 
        } 
        if (unitSlot.maxUnits > 0 && seq1.size > unitSlot.maxUnits)
          seq1.truncate(unitSlot.maxUnits); 
        for (b1 = 0; b1 < seq1.size; ) {
          claimed.add(((Unit)seq1.get(b1)).id);
          b1++;
        } 
        if (!seq1.isEmpty()) {
          apply(unitSlot, seq1);
          count += seq1.size;
        } 
      } 
    } 
  }
  
  private static void apply(UnitSlot paramUnitSlot, Seq<Unit> paramSeq) {
    UnitType unitType = ((Unit)paramSeq.first()).type;
    UnitCommand unitCommand = null;
    if (!paramUnitSlot.command.isEmpty() && unitType.commands != null)
      for (byte b = 0; b < unitType.commands.size; b++) {
        UnitCommand unitCommand1 = (UnitCommand)unitType.commands.get(b);
        if (unitCommand1.name.equals(paramUnitSlot.command)) {
          unitCommand = unitCommand1;
          break;
        } 
      }  
    if (unitCommand != null) {
      IntSeq intSeq = new IntSeq();
      for (byte b = 0; b < paramSeq.size; b++) {
        CommandAI commandAI = (CommandAI)((Unit)paramSeq.get(b)).controller();
        if (commandAI.command != unitCommand)
          intSeq.add(((Unit)paramSeq.get(b)).id); 
      } 
      if (intSeq.size > 0)
        Call.setUnitCommand(Vars.player, intSeq.toArray(), unitCommand); 
    } 
    if (unitCommand != null && unitCommand == UnitCommand.mineCommand)
      if (paramUnitSlot.items.isEmpty()) {
        stance(paramSeq, UnitStance.mineAuto, true);
      } else {
        Seq seq = ItemUnitStance.all();
        for (byte b = 0; b < seq.size; b++) {
          ItemUnitStance itemUnitStance = (ItemUnitStance)seq.get(b);
          stance(paramSeq, (UnitStance)itemUnitStance, paramUnitSlot.items.contains(itemUnitStance.item.name));
        } 
      }  
  }
  
  private static void stance(Seq<Unit> paramSeq, UnitStance paramUnitStance, boolean paramBoolean) {
    IntSeq intSeq = new IntSeq();
    for (byte b = 0; b < paramSeq.size; b++) {
      CommandAI commandAI = (CommandAI)((Unit)paramSeq.get(b)).controller();
      if (commandAI.hasStance(paramUnitStance) != paramBoolean)
        intSeq.add(((Unit)paramSeq.get(b)).id); 
    } 
    if (intSeq.size > 0)
      Call.setUnitStance(Vars.player, intSeq.toArray(), paramUnitStance, paramBoolean); 
  }
}
