package lunarians;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Scl;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.ui.SlotButton;
import mindustry.Vars;
import mindustry.core.World;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.game.Schematic;
import mindustry.input.Binding;

public class SchematicPlacer {
  public static SlotButton selectedSlot;
  
  public static Schematic schematic;
  
  private static boolean tracking;
  
  private static boolean startedOnUi;
  
  private static boolean moved;
  
  private static boolean multiTouch;
  
  private static float downX;
  
  private static float downY;
  
  private static float downTime;
  
  private static float clock;
  
  private static boolean errorLogged;
  
  private static boolean armed;
  
  public static void init() {
    if (Vars.headless)
      return; 
    Events.run(EventType.Trigger.update, new Runnable() {
          public void run() {
            SchematicPlacer.update();
          }
        });
    Events.on(EventType.WorldLoadEvent.class, new Cons<EventType.WorldLoadEvent>() {
          public void get(EventType.WorldLoadEvent param1WorldLoadEvent) {
            SchematicPlacer.deselect();
          }
        });
  }
  
  public static void setPanelVisible(boolean paramBoolean) {
    LunariansSettings.showSchematicPanel = paramBoolean;
    LunariansSettings.save();
    if (!paramBoolean)
      deselect(); 
  }
  
  public static boolean isActive() {
    return (schematic != null);
  }
  
  public static boolean isSelected(SlotButton paramSlotButton) {
    return (paramSlotButton != null && selectedSlot == paramSlotButton && schematic != null);
  }
  
  public static void select(SlotButton paramSlotButton, Schematic paramSchematic) {
    selectedSlot = paramSlotButton;
    schematic = paramSchematic;
    tracking = false;
    armed = false;
  }
  
  public static void deselect() {
    if (schematic != null && Vars.control != null && Vars.control.input != null && Vars.control.input.lastSchematic == schematic)
      Vars.control.input.selectPlans.clear(); 
    selectedSlot = null;
    armed = false;
    schematic = null;
    tracking = false;
  }
  
  public static int rotation() {
    return LunariansSettings.schemRotation;
  }
  
  public static void cycleRotation() {
    LunariansSettings.schemRotation = LunariansSettings.schemRotation + 1 & 0x3;
    LunariansSettings.save();
  }
  
  private static void update() {
    if (schematic == null)
      return; 
    try {
      clock += Time.delta / 60.0F;
      if (Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
        deselect();
        return;
      } 
      if (Vars.control.input.block != null) {
        deselect();
        return;
      } 
      if (!Vars.mobile && Core.input.keyTap(Binding.deselect) && !Core.scene.hasMouse()) {
        deselect();
        return;
      } 
      if (!Vars.mobile) {
        boolean bool1 = (!Core.scene.hasMouse() && !Core.scene.hasDialog()) ? true : false, bool2 = bool1;
        if (!armed) {
          if (bool1 && Core.input.justTouched()) {
            armed = true;
            stageDesktop();
          } 
        } else if (Vars.control.input.selectPlans.isEmpty() && bool1) {
          stageDesktop();
        } 
        return;
      } 
      boolean bool = Core.input.isTouched();
      float f1 = Core.input.mouseX();
      float f2 = Core.input.mouseY();
      if (bool && !tracking) {
        tracking = true;
        downX = f1;
        downY = f2;
        downTime = clock;
        moved = false;
        multiTouch = Core.input.isTouched(1);
        startedOnUi = (Core.scene.hasMouse(f1, f2) || Core.scene.hasDialog() || Core.scene.hasField());
      } else if (bool) {
        if (Core.input.isTouched(1))
          multiTouch = true; 
        if (Math.abs(f1 - downX) > Scl.scl(14.0F) || Math.abs(f2 - downY) > Scl.scl(14.0F))
          moved = true; 
      } else if (tracking) {
        tracking = false;
        if (!startedOnUi && !moved && !multiTouch && clock - downTime < 0.6F)
          stageAt(downX, downY); 
      } 
    } catch (Throwable throwable) {
      if (!errorLogged) {
        errorLogged = true;
        Log.err("[Lunarians] SchematicPlacer error", throwable);
      } 
      deselect();
    } 
  }
  
  private static void stageAt(float paramFloat1, float paramFloat2) {
    Vec2 vec2 = Core.input.mouseWorld(paramFloat1, paramFloat2);
    int i = World.toTile(vec2.x);
    int j = World.toTile(vec2.y);
    Seq<BuildPlan> seq = Vars.schematics.toPlans(schematic, i, j);
    for (byte b = 0; b < rotation(); b++)
      rotatePlans(seq, i, j); 
    Seq seq1 = Vars.control.input.selectPlans;
    if (!seq1.isEmpty() && !seq.isEmpty()) {
      BuildPlan buildPlan = (BuildPlan)seq.first();
      for (byte b1 = 0; b1 < seq1.size; ) {
        BuildPlan buildPlan1 = (BuildPlan)seq1.get(b1);
        if (buildPlan1.x != buildPlan.x || buildPlan1.y != buildPlan.y || buildPlan1.block != buildPlan.block) {
          b1++;
          continue;
        } 
        return;
      } 
    } 
    seq1.addAll(seq);
    Vars.control.input.lastSchematic = schematic;
  }
  
  private static void stageDesktop() {
    Vars.control.input.useSchematic(schematic);
    Vec2 vec2 = Core.input.mouseWorld();
    int i = World.toTile(vec2.x);
    int j = World.toTile(vec2.y);
    for (byte b = 0; b < rotation(); b++)
      rotatePlans(Vars.control.input.selectPlans, i, j); 
  }
  
  private static void rotatePlans(Seq<BuildPlan> paramSeq, int paramInt1, int paramInt2) {
    for (byte b = 0; b < paramSeq.size; b++) {
      BuildPlan buildPlan = (BuildPlan)paramSeq.get(b);
      if (!buildPlan.breaking && buildPlan.block != null) {
        final float f = (buildPlan.block.size % 2 == 0) ? -0.5F : 0.0F;
        buildPlan.pointConfig(new Cons<Point2>() {
              public void get(Point2 param1Point2) {
                float f1 = param1Point2.x + f;
                float f2 = param1Point2.y + f;
                float f3 = f1;
                f1 = -f2;
                f2 = f3;
                param1Point2.set(Mathf.floor(f1 - f), Mathf.floor(f2 - f));
              }
            });
        float f2 = ((buildPlan.x - paramInt1) * 8) + buildPlan.block.offset;
        float f3 = ((buildPlan.y - paramInt2) * 8) + buildPlan.block.offset;
        float f4 = f2;
        f2 = -f3;
        f3 = f4;
        buildPlan.x = World.toTile(f2 - buildPlan.block.offset) + paramInt1;
        buildPlan.y = World.toTile(f3 - buildPlan.block.offset) + paramInt2;
        buildPlan.rotation = buildPlan.block.planRotation(Mathf.mod(buildPlan.rotation + 1, 4));
      } 
    } 
  }
}
