package lunarians;

import arc.Events;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.IntIntMap;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.data.CargoSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;

public class AutoCargo {
  private static float timer;
  
  private static int count;
  
  private static boolean errorLogged;
  
  private static final IntSet applied = new IntSet();
  
  private static final IntIntMap wpIndex = new IntIntMap();
  
  public static void init() {
    if (Vars.headless)
      return; 
    Events.run(EventType.Trigger.update, AutoCargo::update);
    Events.on(EventType.WorldLoadEvent.class, paramWorldLoadEvent -> {
          timer = 0.0F;
          count = 0;
          applied.clear();
          wpIndex.clear();
        });
  }
  
  public static int count() {
    return count;
  }
  
  public static void reset() {
    timer = 0.0F;
    count = 0;
    applied.clear();
    wpIndex.clear();
  }
  
  private static void update() {
    if (!LunariansSettings.enableAutoCargo || Vars.state == null || !Vars.state.isGame() || Vars.player == null)
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
        Log.err("[Lunarians] AutoCargo error", throwable);
      } 
    } 
  }
  
  private static void scan() {
    Seq<CargoSlot> seq = LunariansSettings.cargoSlots;
    if (seq == null || seq.isEmpty())
      return; 
    applied.clear();
    count = 0;
    for (byte b = 0; b < seq.size; b++) {
      CargoSlot cargoSlot = (CargoSlot)seq.get(b);
      if (cargoSlot != null && cargoSlot.ready()) {
        Seq<Unit> seq1 = new Seq();
        int i = Groups.unit.size();
        byte b1;
        for (b1 = 0; b1 < i; b1++) {
          Unit unit = (Unit)Groups.unit.index(b1);
          if (unit != null && unit.isValid() && unit.team == Vars.player.team() && 
            !applied.contains(unit.id) && 
            unit instanceof mindustry.gen.Payloadc && 
            unit.controller() instanceof CommandAI && 
            unit.type != null && cargoSlot.unit.equals(unit.type.name))
            seq1.add(unit); 
        } 
        if (cargoSlot.maxUnits > 0 && seq1.size > cargoSlot.maxUnits)
          seq1.truncate(cargoSlot.maxUnits); 
        for (b1 = 0; b1 < seq1.size; ) {
          applied.add(((Unit)seq1.get(b1)).id);
          b1++;
        } 
        if (!seq1.isEmpty()) {
          apply(cargoSlot, seq1);
          count += seq1.size;
        } 
      } 
    } 
  }
  
  private static void apply(CargoSlot paramCargoSlot, Seq<Unit> paramSeq) {
    IntSeq intSeq = new IntSeq();
    int i;
    for (i = 0; i < paramSeq.size; i++) {
      Unit unit = (Unit)paramSeq.get(i);
      CommandAI commandAI = (CommandAI)unit.controller();
      try {
        if (UnitCommand.loopPayloadCommand != null && commandAI.command != UnitCommand.loopPayloadCommand)
          intSeq.add(unit.id); 
      } catch (Throwable throwable) {}
    } 
    if (intSeq.size > 0)
      try {
        Call.setUnitCommand(Vars.player, intSeq.toArray(), UnitCommand.loopPayloadCommand);
      } catch (Throwable throwable) {
        try {
          Call.setUnitCommand(Vars.player, intSeq.toArray(), UnitCommand.moveCommand);
        } catch (Throwable throwable1) {}
      }  
    i = paramCargoSlot.pointCount();
    if (i < 2)
      return; 
    int j = paramSeq.size;
    for (byte b = 0; b < j; b++) {
      Unit unit = (Unit)paramSeq.get(b);
      int k = wpIndex.get(unit.id, 0);
      if (k < 0 || k >= i)
        k = 0; 
      float f1 = (j <= 1) ? 0.0F : (b * 360.0F / j * 0.017453292F);
      float f2 = Mathf.cos(f1) * 14.0F + (unit.id % 7) * 1.5F;
      float f3 = Mathf.sin(f1) * 14.0F + (unit.id % 5) * 1.2F;
      float f4 = paramCargoSlot.px(k) * 8.0F + 4.0F + f2;
      float f5 = paramCargoSlot.py(k) * 8.0F + 4.0F + f3;
      Vec2 vec2 = PathSafe.safeNear(unit, f4, f5, 48.0F);
      float f6 = 36.0F;
      if (unit.dst(vec2.x, vec2.y) <= f6) {
        k = (k + 1) % i;
        wpIndex.put(unit.id, k);
        f4 = paramCargoSlot.px(k) * 8.0F + 4.0F + f2;
        f5 = paramCargoSlot.py(k) * 8.0F + 4.0F + f3;
        vec2 = PathSafe.safeNear(unit, f4, f5, 48.0F);
      } else {
        wpIndex.put(unit.id, k);
      } 
      Call.commandUnits(Vars.player, new int[] { unit.id }, null, null, vec2, false, true);
    } 
  }
}
