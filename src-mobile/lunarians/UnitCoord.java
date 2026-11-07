package lunarians;

import arc.math.geom.Vec2;
import arc.struct.IntFloatMap;
import arc.struct.IntIntMap;
import arc.util.Time;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.entities.units.UnitController;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.gen.Unit;

public final class UnitCoord {
  public static final int ATTACK = 1;
  
  public static final int ESCORT = 2;
  
  public static final int FLEE = 4;
  
  public static final int HEAL = 3;
  
  private static final int MAX_PER_WINDOW = 60;
  
  private static final IntFloatMap lastAt;
  
  private static final IntFloatMap lastX;
  
  private static final IntFloatMap lastY;
  
  private static final IntIntMap owner;
  
  private static final IntFloatMap until;
  
  private static int used;
  
  private static float windowStart;
  
  static {
    IntIntMap intIntMap = new IntIntMap();
    this();
    owner = intIntMap;
    IntFloatMap intFloatMap = new IntFloatMap();
    this();
    until = intFloatMap;
    intFloatMap = new IntFloatMap();
    this();
    lastX = intFloatMap;
    intFloatMap = new IntFloatMap();
    this();
    lastY = intFloatMap;
    intFloatMap = new IntFloatMap();
    this();
    lastAt = intFloatMap;
  }
  
  public static boolean cmd(Unit paramUnit1, int paramInt, float paramFloat, Building paramBuilding, Unit paramUnit2, Vec2 paramVec2) {
    int i = 0;
    UnitCommand unitCommand = null;
    if (paramUnit1 != null && paramVec2 != null) {
      Player player = Vars.player;
      if (player != null) {
        boolean bool = free(paramUnit1.id, paramInt);
        if (bool) {
          float f1 = Time.time;
          float f2 = windowStart;
          f1 -= f2;
          int k = 1106247680;
          f2 = 30.0F;
          int j = f1 cmp f2;
          if (j > 0) {
            f1 = Time.time;
            windowStart = f1;
            used = 0;
          } 
          IntIntMap intIntMap = owner;
          k = paramUnit1.id;
          intIntMap.put(k, paramInt);
          IntFloatMap intFloatMap1 = until;
          j = paramUnit1.id;
          f2 = Time.time + paramFloat;
          intFloatMap1.put(j, f2);
          intFloatMap1 = lastAt;
          int n = paramUnit1.id;
          j = -971228160;
          paramFloat = intFloatMap1.get(n, -9999.0F);
          f1 = Time.time - paramFloat;
          paramFloat = 45.0F;
          k = 1;
          f2 = Float.MIN_VALUE;
          n = f1 cmp paramFloat;
          if (n < 0) {
            IntFloatMap intFloatMap = lastX;
            j = paramUnit1.id;
            float f = 0.0F;
            Object object = null;
            paramFloat = intFloatMap.get(j, 0.0F);
            f1 = paramVec2.x;
            paramFloat = Math.abs(paramFloat - f1);
            j = 1086324736;
            f1 = 6.0F;
            n = paramFloat cmp f1;
            if (n < 0) {
              intFloatMap = lastY;
              int i3 = paramUnit1.id;
              paramFloat = intFloatMap.get(i3, 0.0F);
              f = paramVec2.y;
              paramFloat = Math.abs(paramFloat - f);
              n = paramFloat cmp f1;
              if (n < 0)
                return k; 
            } 
          } 
          n = used;
          j = 60;
          f1 = 8.4E-44F;
          if (n >= j)
            return false; 
          used = n + k;
          UnitController unitController = paramUnit1.controller();
          boolean bool1 = unitController instanceof CommandAI;
          if (bool1) {
            UnitCommand unitCommand1 = ((CommandAI)paramUnit1.controller()).command;
            unitCommand = UnitCommand.moveCommand;
            if (unitCommand1 != unitCommand) {
              Player player2 = Vars.player;
              i = paramUnit1.id;
              int[] arrayOfInt1 = { i };
              UnitCommand unitCommand2 = UnitCommand.moveCommand;
              Call.setUnitCommand(player2, arrayOfInt1, unitCommand2);
            } 
          } 
          Player player1 = Vars.player;
          int m = paramUnit1.id;
          int[] arrayOfInt = { m };
          Call.commandUnits(player1, arrayOfInt, paramBuilding, paramUnit2, paramVec2, false, true);
          IntFloatMap intFloatMap2 = lastX;
          int i1 = paramUnit1.id;
          float f3 = paramVec2.x;
          intFloatMap2.put(i1, f3);
          intFloatMap2 = lastY;
          i1 = paramUnit1.id;
          f3 = paramVec2.y;
          intFloatMap2.put(i1, f3);
          int i2 = paramUnit1.id;
          paramFloat = Time.time;
          intFloatMap1.put(i2, paramFloat);
          return k;
        } 
      } 
    } 
    return false;
  }
  
  public static boolean free(int paramInt1, int paramInt2) {
    paramInt1 = holder(paramInt1);
    return (paramInt1 <= paramInt2);
  }
  
  public static int holder(int paramInt) {
    IntFloatMap intFloatMap = until;
    float f1 = intFloatMap.get(paramInt, 0.0F);
    float f2 = Time.time;
    int i = f1 cmp f2;
    return (i > 0) ? owner.get(paramInt, 0) : 0;
  }
  
  public static void reset() {
    owner.clear();
    until.clear();
    lastX.clear();
    lastY.clear();
    lastAt.clear();
    used = 0;
    windowStart = 0.0F;
  }
}
