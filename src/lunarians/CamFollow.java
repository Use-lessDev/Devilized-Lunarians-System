package lunarians;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.IntSeq;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.entities.Units;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Entityc;
import mindustry.gen.Groups;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;

public class CamFollow {
  private static final float INTERVAL = 0.4F;
  
  private static final float AIM_RADIUS_TILES = 5.0F;
  
  private static final float MOVE_REISSUE = 16.0F;
  
  private static final float TIME_REISSUE = 5.0F;
  
  private static final IntSeq selected = new IntSeq();
  
  private static final IntSeq tmpIds = new IntSeq();
  
  private static float clock;
  
  private static float timer;
  
  private static boolean errorLogged;
  
  public static void init() {
    if (Vars.headless)
      return; 
    Events.run(EventType.Trigger.update, new Runnable() {
          public void run() {
            CamFollow.update();
          }
        });
    Events.on(EventType.WorldLoadEvent.class, new Cons<EventType.WorldLoadEvent>() {
          public void get(EventType.WorldLoadEvent param1WorldLoadEvent) {
            CamFollow.selected.clear();
            CamFollow.selectedHash = 0L;
            CamFollow.resetOrder();
          }
        });
  }
  
  private static void resetOrder() {
    hasLast = false;
    lastSig = Integer.MIN_VALUE;
    lastTargetId = -1;
  }
  
  public static void setEnabled(boolean paramBoolean) {
    LunariansSettings.enableCamFollow = paramBoolean;
    LunariansSettings.save();
    resetOrder();
    if (paramBoolean) {
      captureSelection();
    } else {
      selected.clear();
      selectedHash = 0L;
    } 
  }
  
  public static int count() {
    return selected.size;
  }
  
  private static void captureSelection() {
    if (Vars.control == null || Vars.control.input == null)
      return; 
    Seq seq = Vars.control.input.selectedUnits;
    if (seq == null || seq.isEmpty())
      return; 
    long l = 0L;
    byte b2 = 0;
    byte b1;
    for (b1 = 0; b1 < seq.size; b1++) {
      Unit unit = (Unit)seq.get(b1);
      if (unit != null && unit.isValid() && unit.isCommandable() && !unit.isPlayer()) {
        l += (unit.id + 1L) * 2654435761L;
        b2++;
      } 
    } 
    if (b2 == 0 || l == selectedHash)
      return; 
    selectedHash = l;
    selected.clear();
    for (b1 = 0; b1 < seq.size; b1++) {
      Unit unit = (Unit)seq.get(b1);
      if (unit != null && unit.isValid() && unit.isCommandable() && !unit.isPlayer())
        selected.add(unit.id); 
    } 
    resetOrder();
  }
  
  private static void update() {
    if (!LunariansSettings.enableCamFollow || Vars.state == null || !Vars.state.isGame() || Vars.state.isPaused() || Vars.player == null)
      return; 
    clock += Time.delta / 60.0F;
    captureSelection();
    timer += Time.delta / 60.0F;
    if (timer < 0.4F)
      return; 
    timer = 0.0F;
    try {
      run();
    } catch (Throwable throwable) {
      if (!errorLogged) {
        errorLogged = true;
        Log.err("[Lunarians] CamFollow error", throwable);
      } 
    } 
  }
  
  private static void run() {
    IntSeq intSeq = tmpIds;
    intSeq.clear();
    int i;
    for (i = 0; i < selected.size; i++) {
      Entityc entityc = Groups.unit.getByID(selected.get(i));
      if (entityc != null && ((Unit)entityc).isValid() && ((Unit)entityc).team == Vars.player.team() && ((Unit)entityc).isCommandable())
        intSeq.add(((Unit)entityc).id); 
    } 
    if (intSeq.size != selected.size) {
      selected.clear();
      selected.addAll(intSeq);
    } 
    if (intSeq.size == 0)
      return; 
    i = signature(intSeq);
    int[] arrayOfInt = intSeq.toArray();
    Unit unit1 = Vars.player.dead() ? null : Vars.player.unit(), unit2 = unit1;
    Teamc teamc;
    if (unit1 != null && unit1.isValid() && unit1.isShooting() && (teamc = Units.closestTarget(Vars.player.team(), unit1.aimX(), unit1.aimY(), 40.0F)) != null) {
      int j = (teamc instanceof Unit) ? ((Unit)teamc).id : ((Building)teamc).pos();
      boolean bool3 = (hasLast && lastWasAttack && lastTargetId == j && lastSig == i) ? true : false, bool4 = bool3;
      if (!bool3 || clock - lastTime > 2.0F) {
        Unit unit = (teamc instanceof Unit) ? (Unit)teamc : null;
        Building building = (teamc instanceof Building) ? (Building)teamc : null;
        Call.commandUnits(Vars.player, arrayOfInt, building, unit, null, false, true);
        hasLast = true;
        lastWasAttack = true;
        lastTargetId = j;
        lastSig = i;
        lastTime = clock;
      } 
      return;
    } 
    float f1 = Mathf.clamp(Core.camera.position.x, 0.0F, Vars.world.unitWidth());
    float f2 = Mathf.clamp(Core.camera.position.y, 0.0F, Vars.world.unitHeight());
    boolean bool1 = (!hasLast || lastWasAttack || lastSig != i || lastPos.dst(f1, f2) > 16.0F || clock - lastTime > 5.0F) ? true : false, bool2 = bool1;
    if (bool1) {
      Call.commandUnits(Vars.player, arrayOfInt, null, null, new Vec2(f1, f2), false, true);
      hasLast = true;
      lastWasAttack = false;
      lastTargetId = -1;
      lastSig = i;
      lastPos.set(f1, f2);
      lastTime = clock;
    } 
  }
  
  private static int signature(IntSeq paramIntSeq) {
    int i = paramIntSeq.size;
    for (byte b = 0; b < paramIntSeq.size; b++)
      i = i * 31 + paramIntSeq.get(b); 
    return i;
  }
  
  private static int lastSig = Integer.MIN_VALUE;
  
  private static boolean lastWasAttack;
  
  private static int lastTargetId = -1;
  
  private static final Vec2 lastPos = new Vec2();
  
  private static float lastTime = -999.0F;
  
  private static boolean hasLast;
  
  private static long selectedHash;
}
