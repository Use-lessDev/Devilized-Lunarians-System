package lunarians;

import arc.math.geom.Vec2;
import arc.struct.IntFloatMap;
import arc.struct.IntIntMap;
import arc.util.Time;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Unit;

public final class UnitCoord {
  public static final int ATTACK = 1;
  
  public static final int ESCORT = 2;
  
  public static final int HEAL = 3;
  
  public static final int FLEE = 4;
  
  private static final IntIntMap owner = new IntIntMap();
  
  private static final IntFloatMap until = new IntFloatMap();
  
  private static final IntFloatMap lastX = new IntFloatMap();
  
  private static final IntFloatMap lastY = new IntFloatMap();
  
  private static final IntFloatMap lastAt = new IntFloatMap();
  
  private static float windowStart;
  
  private static int used;
  
  private static final int MAX_PER_WINDOW = 60;
  
  public static void reset() {
    owner.clear();
    until.clear();
    lastX.clear();
    lastY.clear();
    lastAt.clear();
    used = 0;
    windowStart = 0.0F;
  }
  
  public static int holder(int paramInt) {
    return (until.get(paramInt, 0.0F) > Time.time) ? owner.get(paramInt, 0) : 0;
  }
  
  public static boolean free(int paramInt1, int paramInt2) {
    return (holder(paramInt1) <= paramInt2);
  }
  
  public static boolean cmd(Unit paramUnit1, int paramInt, float paramFloat, Building paramBuilding, Unit paramUnit2, Vec2 paramVec2) {
    if (paramUnit1 == null || paramVec2 == null || Vars.player == null || !free(paramUnit1.id, paramInt))
      return false; 
    if (Time.time - windowStart > 30.0F) {
      windowStart = Time.time;
      used = 0;
    } 
    owner.put(paramUnit1.id, paramInt);
    until.put(paramUnit1.id, Time.time + paramFloat);
    float f = lastAt.get(paramUnit1.id, -9999.0F);
    if (Time.time - f < 45.0F && Math.abs(lastX.get(paramUnit1.id, 0.0F) - paramVec2.x) < 6.0F && Math.abs(lastY.get(paramUnit1.id, 0.0F) - paramVec2.y) < 6.0F)
      return true; 
    if (used >= 60)
      return false; 
    used++;
    if (paramUnit1.controller() instanceof CommandAI) {
      CommandAI commandAI = (CommandAI)paramUnit1.controller();
      if (commandAI.command != UnitCommand.moveCommand)
        Call.setUnitCommand(Vars.player, new int[] { paramUnit1.id }, UnitCommand.moveCommand); 
    } 
    Call.commandUnits(Vars.player, new int[] { paramUnit1.id }, paramBuilding, paramUnit2, paramVec2, false, true);
    lastX.put(paramUnit1.id, paramVec2.x);
    lastY.put(paramUnit1.id, paramVec2.y);
    lastAt.put(paramUnit1.id, Time.time);
    return true;
  }
}
