package lunarians;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.geom.Vec2;
import arc.struct.IntFloatMap;
import arc.struct.IntIntMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.core.World;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Payloadc;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.ui.Fonts;
import mindustry.world.blocks.payloads.BuildPayload;
import mindustry.world.blocks.payloads.Payload;

public class AutoContainer {
  public static final int UNSET = -2147483648;
  
  private static final int GO_A = 0;
  
  private static final int LOAD_A = 1;
  
  private static final int GO_B = 2;
  
  private static final int UNLOAD_B = 3;
  
  private static final int WAIT1 = 4;
  
  private static final int GO_B2 = 5;
  
  private static final int LOAD_B = 6;
  
  private static final int GO_A2 = 7;
  
  private static final int UNLOAD_A = 8;
  
  private static final int WAIT2 = 9;
  
  private static final float REACH = 6.0F;
  
  private static final float STAGE_TIMEOUT = 10.0F;
  
  private static float timer;
  
  private static boolean errorLogged;
  
  private static int count;
  
  private static float markUntil;
  
  public static float waitAfterReturn() {
    return Math.max(0.0F, Core.settings.getFloat("lunarians-container-wait2", 5.0F));
  }
  
  public static void setWaitAfterReturn(float paramFloat) {
    Core.settings.put("lunarians-container-wait2", Float.valueOf(paramFloat));
  }
  
  public static float waitAfterPlace() {
    return Math.max(0.0F, LunariansSettings.containerWaitSec);
  }
  
  public static boolean hasPoints() {
    return (LunariansSettings.containerPickX != Integer.MIN_VALUE && LunariansSettings.containerDropX != Integer.MIN_VALUE);
  }
  
  public static void init() {
    if (Vars.headless)
      return; 
    Events.run(EventType.Trigger.update, AutoContainer::update);
    Events.run(EventType.Trigger.draw, AutoContainer::draw);
    Events.on(EventType.WorldLoadEvent.class, paramWorldLoadEvent -> reset());
  }
  
  public static int count() {
    return count;
  }
  
  public static void reset() {
    timer = 0.0F;
    count = 0;
    origAy = Integer.MIN_VALUE;
    origAx = Integer.MIN_VALUE;
    phase.clear();
    stageT.clear();
    lastCmd.clear();
  }
  
  public static void showMarks() {
    markUntil = Time.time + 1200.0F;
  }
  
  public static void setPoints(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    LunariansSettings.containerPickX = paramInt1;
    LunariansSettings.containerPickY = paramInt2;
    LunariansSettings.containerDropX = paramInt3;
    LunariansSettings.containerDropY = paramInt4;
    origAy = Integer.MIN_VALUE;
    origAx = Integer.MIN_VALUE;
    phase.clear();
    stageT.clear();
    showMarks();
    LunariansSettings.save();
  }
  
  public static void clearPoints() {
    LunariansSettings.containerPickX = Integer.MIN_VALUE;
    LunariansSettings.containerPickY = Integer.MIN_VALUE;
    LunariansSettings.containerDropX = Integer.MIN_VALUE;
    LunariansSettings.containerDropY = Integer.MIN_VALUE;
    origAy = Integer.MIN_VALUE;
    origAx = Integer.MIN_VALUE;
    phase.clear();
    stageT.clear();
    LunariansSettings.save();
  }
  
  private static float centerOf(int paramInt1, int paramInt2) {
    return paramInt1 * 8.0F + ((paramInt2 + 1) % 2) * 4.0F;
  }
  
  private static void box(int paramInt1, int paramInt2, int paramInt3, Color paramColor, String paramString) {
    float f1 = centerOf(paramInt1, paramInt3);
    float f2 = centerOf(paramInt2, paramInt3);
    float f3 = paramInt3 * 4.0F;
    Lines.stroke(2.0F);
    Draw.color(Color.black, 0.6F);
    Lines.rect(f1 - f3 - 0.6F, f2 - f3 - 0.6F, paramInt3 * 8.0F + 1.2F, paramInt3 * 8.0F + 1.2F);
    Draw.color(paramColor, 0.95F);
    Lines.rect(f1 - f3, f2 - f3, paramInt3 * 8.0F, paramInt3 * 8.0F);
    try {
      Fonts.outline.draw(paramString, f1, f2 + 4.0F, Color.white, 0.22F, false, 1);
    } catch (Throwable throwable) {}
    Draw.color();
  }
  
  private static void draw() {
    if (Vars.state == null || !Vars.state.isGame())
      return; 
    if (!hasPoints())
      return; 
    if (!LunariansSettings.enableAutoContainer && Time.time > markUntil)
      return; 
    int i = LunariansSettings.containerPickX;
    int j = LunariansSettings.containerPickY;
    int k = LunariansSettings.containerDropX;
    int m = LunariansSettings.containerDropY;
    int n = blockSize;
    int i1 = i;
    int i2 = j;
    try {
      Building building = Vars.world.build(i, j);
      if (building != null && building.block != null) {
        n = building.block.size;
        i1 = building.tileX();
        i2 = building.tileY();
      } 
    } catch (Throwable throwable) {}
    Draw.z(120.0F);
    Lines.stroke(1.5F);
    Draw.color(Pal.accent, 0.7F);
    Lines.dashLine(centerOf(i1, n), centerOf(i2, n), centerOf(k, n), centerOf(m, n), 14);
    box(i1, i2, n, Color.orange, "A");
    box(k, m, n, Color.cyan, "B");
    Draw.reset();
  }
  
  private static void update() {
    if (!LunariansSettings.enableAutoContainer || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
      count = 0;
      return;
    } 
    if ((timer += Time.delta / 60.0F) < LunariansSettings.actionInterval(0.3F))
      return; 
    timer = 0.0F;
    try {
      scan();
    } catch (Throwable throwable) {
      if (!errorLogged) {
        errorLogged = true;
        Log.err("[Lunarians] AutoContainer", throwable);
      } 
    } 
  }
  
  private static void scan() {
    count = 0;
    if (LunariansSettings.containerUnit == null || LunariansSettings.containerUnit.isEmpty() || !hasPoints())
      return; 
    tmp.clear();
    int i = Groups.unit.size();
    int j = LunariansSettings.containerMaxUnits;
    byte b;
    for (b = 0; b < i; b++) {
      Unit unit = (Unit)Groups.unit.index(b);
      if (unit != null && unit.isValid() && unit.team == Vars.player.team() && unit instanceof Payloadc && unit.controller() instanceof CommandAI && unit.type != null && LunariansSettings.containerUnit.equals(unit.type.name)) {
        tmp.add(unit);
        if (j > 0 && tmp.size >= j)
          break; 
      } 
    } 
    for (b = 0; b < tmp.size; b++)
      drive((Unit)tmp.get(b), (Payloadc)tmp.get(b)); 
    count = tmp.size;
  }
  
  private static void setStage(int paramInt1, int paramInt2) {
    phase.put(paramInt1, paramInt2);
    stageT.put(paramInt1, Time.time);
    lastCmd.remove(paramInt1, 0.0F);
  }
  
  private static float stageSecs(int paramInt) {
    return (Time.time - stageT.get(paramInt, Time.time)) / 60.0F;
  }
  
  private static void setCommand(Unit paramUnit, UnitCommand paramUnitCommand) {
    try {
      CommandAI commandAI = (CommandAI)paramUnit.controller();
      if (commandAI.command != paramUnitCommand)
        Call.setUnitCommand(Vars.player, new int[] { paramUnit.id }, paramUnitCommand); 
    } catch (Throwable throwable) {}
  }
  
  private static void goTo(Unit paramUnit, float paramFloat1, float paramFloat2, boolean paramBoolean) {
    float f = lastCmd.get(paramUnit.id, -9999.0F);
    if (!paramBoolean && (Time.time - f) / 60.0F < 2.5F)
      return; 
    lastCmd.put(paramUnit.id, Time.time);
    Call.commandUnits(Vars.player, new int[] { paramUnit.id }, null, null, new Vec2(paramFloat1, paramFloat2), false, true);
  }
  
  private static float dropOffset(Payloadc paramPayloadc) {
    try {
      Payload payload = (Payload)paramPayloadc.payloads().peek();
      if (payload instanceof BuildPayload)
        return (((BuildPayload)payload).block()).offset; 
    } catch (Throwable throwable) {}
    return ((blockSize + 1) % 2) * 4.0F;
  }
  
  private static boolean approachExact(Unit paramUnit, int paramInt1, int paramInt2, float paramFloat) {
    float f1 = paramInt1 * 8.0F + paramFloat;
    float f2 = paramInt2 * 8.0F + paramFloat;
    if (!paramUnit.within(f1, f2, 40.0F)) {
      setCommand(paramUnit, UnitCommand.moveCommand);
      goTo(paramUnit, f1, f2, false);
      return false;
    } 
    setCommand(paramUnit, UnitCommand.loadBlocksCommand);
    goTo(paramUnit, f1, f2, false);
    return (World.toTile(paramUnit.x - paramFloat) == paramInt1 && World.toTile(paramUnit.y - paramFloat) == paramInt2 && paramUnit.within(f1, f2, 2.0F));
  }
  
  private static float[] pickCenter(int paramInt1, int paramInt2) {
    Building building = Vars.world.build(paramInt1, paramInt2);
    if (building != null)
      return new float[] { building.x, building.y }; 
    return new float[] { paramInt1 * 8.0F, paramInt2 * 8.0F };
  }
  
  private static void pickStep(Unit paramUnit, int paramInt1, int paramInt2) {
    Building building1 = Vars.world.build(paramInt1, paramInt2);
    Building building2 = Vars.world.buildWorld(paramUnit.x, paramUnit.y);
    float[] arrayOfFloat = pickCenter(paramInt1, paramInt2);
    if (building1 != null && building2 == building1 && paramUnit.within(arrayOfFloat[0], arrayOfFloat[1], 6.0F)) {
      goTo(paramUnit, paramUnit.x, paramUnit.y, true);
    } else {
      goTo(paramUnit, arrayOfFloat[0], arrayOfFloat[1], false);
    } 
  }
  
  private static void drive(Unit paramUnit, Payloadc paramPayloadc) {
    int i = paramUnit.id;
    int j = phase.get(i, -1);
    int k = LunariansSettings.containerPickX;
    int m = LunariansSettings.containerPickY;
    int n = LunariansSettings.containerDropX;
    int i1 = LunariansSettings.containerDropY;
    float[] arrayOfFloat1 = pickCenter(k, m);
    float[] arrayOfFloat2 = pickCenter(n, i1);
    float f1 = arrayOfFloat1[0];
    float f2 = arrayOfFloat1[1];
    float f3 = arrayOfFloat2[0];
    float f4 = arrayOfFloat2[1];
    float f5 = dropOffset(paramPayloadc);
    boolean bool = paramPayloadc.hasPayload();
    if (j < 0) {
      setStage(i, bool ? 2 : 0);
      j = phase.get(i, 0);
    } 
    switch (j) {
      case 0:
        if (bool) {
          setStage(i, 2);
        } else {
          setCommand(paramUnit, UnitCommand.moveCommand);
          goTo(paramUnit, f1, f2, false);
          if (paramUnit.within(f1, f2, 40.0F)) {
            try {
              Building building = Vars.world.build(k, m);
              if (building != null && building.block != null) {
                origAx = building.tileX();
                origAy = building.tileY();
                blockSize = building.block.size;
              } 
            } catch (Throwable throwable) {}
            setCommand(paramUnit, UnitCommand.loadBlocksCommand);
            setStage(i, 1);
            goTo(paramUnit, f1, f2, true);
          } 
        } 
        return;
      case 1:
        if (bool) {
          setCommand(paramUnit, UnitCommand.moveCommand);
          setStage(i, 2);
        } else if (stageSecs(i) > 10.0F) {
          setCommand(paramUnit, UnitCommand.moveCommand);
          setStage(i, 0);
        } else {
          setCommand(paramUnit, UnitCommand.loadBlocksCommand);
          pickStep(paramUnit, k, m);
        } 
        return;
      case 2:
        if (!bool) {
          setStage(i, 0);
        } else if (approachExact(paramUnit, n, i1, f5)) {
          setCommand(paramUnit, UnitCommand.unloadPayloadCommand);
          setStage(i, 3);
        } 
        return;
      case 3:
        if (!bool) {
          setCommand(paramUnit, UnitCommand.moveCommand);
          setStage(i, 4);
        } else if (stageSecs(i) > 10.0F) {
          setCommand(paramUnit, UnitCommand.moveCommand);
          setStage(i, 2);
        } 
        return;
      case 4:
        setCommand(paramUnit, UnitCommand.moveCommand);
        if (stageSecs(i) >= waitAfterPlace())
          setStage(i, 5); 
        return;
      case 5:
        if (bool) {
          setStage(i, 7);
        } else {
          setCommand(paramUnit, UnitCommand.moveCommand);
          goTo(paramUnit, f3, f4, false);
          if (paramUnit.within(f3, f4, 40.0F)) {
            setCommand(paramUnit, UnitCommand.loadBlocksCommand);
            setStage(i, 6);
            goTo(paramUnit, f3, f4, true);
          } 
        } 
        return;
      case 6:
        if (bool) {
          setCommand(paramUnit, UnitCommand.moveCommand);
          setStage(i, 7);
        } else if (stageSecs(i) > 10.0F) {
          setCommand(paramUnit, UnitCommand.moveCommand);
          setStage(i, 5);
        } else {
          setCommand(paramUnit, UnitCommand.loadBlocksCommand);
          pickStep(paramUnit, n, i1);
        } 
        return;
      case 7:
        if (!bool) {
          setStage(i, 5);
        } else {
          int i3 = (origAx == Integer.MIN_VALUE) ? k : origAx;
          int i2 = (origAy == Integer.MIN_VALUE) ? m : origAy, i4 = i2;
          if (approachExact(paramUnit, i3, i2, f5)) {
            setCommand(paramUnit, UnitCommand.unloadPayloadCommand);
            setStage(i, 8);
          } 
        } 
        return;
      case 8:
        if (!bool) {
          setCommand(paramUnit, UnitCommand.moveCommand);
          setStage(i, 9);
        } else if (stageSecs(i) > 10.0F) {
          setCommand(paramUnit, UnitCommand.moveCommand);
          setStage(i, 7);
        } 
        return;
      case 9:
        setCommand(paramUnit, UnitCommand.moveCommand);
        if (stageSecs(i) >= waitAfterReturn())
          setStage(i, 0); 
        return;
    } 
    setStage(i, 0);
  }
  
  private static int origAx = Integer.MIN_VALUE;
  
  private static int origAy = Integer.MIN_VALUE;
  
  private static int blockSize = 2;
  
  private static final IntIntMap phase = new IntIntMap();
  
  private static final IntFloatMap stageT = new IntFloatMap();
  
  private static final IntFloatMap lastCmd = new IntFloatMap();
  
  private static final Seq<Unit> tmp = new Seq();
}
