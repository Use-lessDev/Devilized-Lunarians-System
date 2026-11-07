package lunarians;

import arc.Events;
import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.geom.Vec2;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import java.util.Comparator;
import lunarians.data.DefenceSlot;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;

public class AutoDefence {
  private static final float SCAN = 1.2F;
  
  private static float timer;
  
  private static int count;
  
  private static boolean errorLogged;
  
  private static final IntSet claimed = new IntSet();
  
  public static void init() {
    if (Vars.headless)
      return; 
    Events.run(EventType.Trigger.update, new Runnable() {
          public void run() {
            AutoDefence.update();
          }
        });
    Events.run(EventType.Trigger.draw, new Runnable() {
          public void run() {
            AutoDefence.draw();
          }
        });
    Events.on(EventType.WorldLoadEvent.class, new Cons<EventType.WorldLoadEvent>() {
          public void get(EventType.WorldLoadEvent param1WorldLoadEvent) {
            AutoDefence.reset();
          }
        });
  }
  
  public static void reset() {
    timer = 0.0F;
    count = 0;
    claimed.clear();
  }
  
  public static int count() {
    return count;
  }
  
  private static void update() {
    if (!LunariansSettings.enableAutoDefence || Vars.state == null || !Vars.state.isGame() || Vars.player == null)
      return; 
    timer += Time.delta / 60.0F;
    if (timer < LunariansSettings.actionInterval(1.2F))
      return; 
    timer = 0.0F;
    try {
      scan();
    } catch (Throwable throwable) {
      if (!errorLogged) {
        errorLogged = true;
        Log.err("[Lunarians] AutoDefence error", throwable);
      } 
    } 
  }
  
  private static void scan() {
    Seq<DefenceSlot> seq = LunariansSettings.defenceSlots;
    if (seq.isEmpty())
      return; 
    count = 0;
    claimed.clear();
    for (byte b = 0; b < seq.size; b++) {
      DefenceSlot defenceSlot = (DefenceSlot)seq.get(b);
      if (defenceSlot.ready()) {
        final float cx = defenceSlot.x * 8.0F + 4.0F;
        final float cy = defenceSlot.y * 8.0F + 4.0F;
        float f3 = defenceSlot.radius * 8.0F;
        int i = (defenceSlot.maxUnits > 0) ? defenceSlot.maxUnits : Integer.MAX_VALUE;
        Seq seq1 = new Seq();
        int j = Groups.unit.size();
        for (byte b1 = 0; b1 < j; b1++) {
          Unit unit = (Unit)Groups.unit.index(b1);
          if (unit != null && unit.isValid() && unit.team == Vars.player.team() && 
            defenceSlot.unit.equals(unit.type.name) && 
            unit.controller() instanceof mindustry.ai.types.CommandAI && 
            !claimed.contains(unit.id) && 
            !AutoUnitHeal.isActiveHealer(unit.id) && 
            !AutoUnitHeal.isHealerType(unit.type))
            seq1.add(unit); 
        } 
        seq1.sort(new Comparator<Unit>() {
              public int compare(Unit param1Unit1, Unit param1Unit2) {
                float f1 = param1Unit1.dst(cx, cy);
                float f2 = param1Unit2.dst(cx, cy);
                return Float.compare(f1, f2);
              }
            });
        Seq seq2 = new Seq();
        for (byte b2 = 0; b2 < seq1.size && seq2.size < i; b2++) {
          Unit unit = (Unit)seq1.get(b2);
          seq2.add(unit);
          claimed.add(unit.id);
        } 
        if (!seq2.isEmpty()) {
          count += seq2.size;
          Unit unit = null;
          float f = Float.MAX_VALUE;
          byte b3;
          for (b3 = 0; b3 < j; b3++) {
            Unit unit1 = (Unit)Groups.unit.index(b3);
            if (unit1 != null && unit1.isValid() && unit1.team != Vars.player.team()) {
              float f4 = unit1.dst(f1, f2);
              if (f4 <= f3 && f4 < f) {
                f = f4;
                unit = unit1;
              } 
            } 
          } 
          if (unit != null) {
            IntSeq intSeq = new IntSeq();
            for (byte b4 = 0; b4 < seq2.size; ) {
              intSeq.add(((Unit)seq2.get(b4)).id);
              b4++;
            } 
            Call.commandUnits(Vars.player, intSeq.toArray(), null, unit, null, false, false);
          } else {
            for (b3 = 0; b3 < seq2.size; b3++) {
              float f4 = (seq2.size <= 1) ? 0.0F : (b3 * 360.0F / seq2.size * 0.017453292F);
              float f5 = (float)Math.cos(f4) * 10.0F;
              float f6 = (float)Math.sin(f4) * 10.0F;
              Unit unit1 = (Unit)seq2.get(b3);
              Vec2 vec2 = PathSafe.safeNear(unit1, f1 + f5, f2 + f6, 64.0F);
              Call.commandUnits(Vars.player, new int[] { unit1.id }, null, null, vec2, false, false);
            } 
          } 
        } 
      } 
    } 
  }
  
  private static void draw() {
    if (!LunariansSettings.enableAutoDefence || Vars.state == null || !Vars.state.isGame())
      return; 
    Seq<DefenceSlot> seq = LunariansSettings.defenceSlots;
    if (seq.isEmpty())
      return; 
    Draw.z(120.0F);
    for (byte b = 0; b < seq.size; b++) {
      DefenceSlot defenceSlot = (DefenceSlot)seq.get(b);
      if (defenceSlot.ready()) {
        float f1 = defenceSlot.x * 8.0F + 4.0F;
        float f2 = defenceSlot.y * 8.0F + 4.0F;
        float f3 = defenceSlot.radius * 8.0F;
        Draw.color(Color.scarlet.cpy().a(0.08F));
        Fill.circle(f1, f2, f3);
        Draw.color(Color.scarlet.cpy().a(0.55F));
        Lines.stroke(1.4F);
        Lines.circle(f1, f2, f3);
        Draw.color(Color.orange.cpy().a(0.8F));
        Fill.circle(f1, f2, 3.0F);
      } 
    } 
    Draw.reset();
  }
}
