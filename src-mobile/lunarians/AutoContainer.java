package lunarians;

import arc.Core;
import arc.Events;
import arc.Settings;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Font;
import arc.graphics.g2d.Lines;
import arc.math.geom.Vec2;
import arc.struct.IntFloatMap;
import arc.struct.IntIntMap;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.core.GameState;
import mindustry.core.World;
import mindustry.entities.EntityGroup;
import mindustry.entities.units.UnitController;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Payloadc;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.ui.Fonts;
import mindustry.world.Block;

public class AutoContainer {
  private static final int GO_A = 0;
  
  private static final int GO_A2 = 7;
  
  private static final int GO_B = 2;
  
  private static final int GO_B2 = 5;
  
  private static final int LOAD_A = 1;
  
  private static final int LOAD_B = 6;
  
  private static final float REACH = 6.0F;
  
  private static final float STAGE_TIMEOUT = 10.0F;
  
  private static final int UNLOAD_A = 8;
  
  private static final int UNLOAD_B = 3;
  
  public static final int UNSET = -2147483648;
  
  private static final int WAIT1 = 4;
  
  private static final int WAIT2 = 9;
  
  private static int blockSize = 2;
  
  private static int count = 0;
  
  private static boolean errorLogged = false;
  
  private static final IntFloatMap lastCmd;
  
  private static float markUntil = 0.0F;
  
  private static int origAx = -2147483648;
  
  private static int origAy = -2147483648;
  
  private static final IntIntMap phase;
  
  private static final IntFloatMap stageT;
  
  private static float timer;
  
  private static final Seq tmp;
  
  static {
    IntIntMap intIntMap = new IntIntMap();
    this();
    phase = intIntMap;
    IntFloatMap intFloatMap = new IntFloatMap();
    this();
    stageT = intFloatMap;
    intFloatMap = new IntFloatMap();
    this();
    lastCmd = intFloatMap;
    Seq seq = new Seq();
    this();
    tmp = seq;
  }
  
  private static boolean approachExact(Unit paramUnit, int paramInt1, int paramInt2, float paramFloat) {
    float f1 = paramInt1;
    float f2 = 8.0F;
    f1 = f1 * f2 + paramFloat;
    float f3 = paramInt2 * f2 + paramFloat;
    f2 = 40.0F;
    boolean bool = paramUnit.within(f1, f3, f2);
    if (!bool) {
      UnitCommand unitCommand1 = UnitCommand.moveCommand;
      setCommand(paramUnit, unitCommand1);
      goTo(paramUnit, f1, f3, false);
      return false;
    } 
    UnitCommand unitCommand = UnitCommand.loadBlocksCommand;
    setCommand(paramUnit, unitCommand);
    goTo(paramUnit, f1, f3, false);
    f2 = paramUnit.x - paramFloat;
    int i = World.toTile(f2);
    if (i == paramInt1) {
      float f = paramUnit.y - paramFloat;
      paramInt1 = World.toTile(f);
      if (paramInt1 == paramInt2) {
        paramInt1 = 1073741824;
        f = 2.0F;
        boolean bool1 = paramUnit.within(f1, f3, f);
        if (bool1)
          return true; 
      } 
    } 
    return false;
  }
  
  private static void box(int paramInt1, int paramInt2, int paramInt3, Color paramColor, String paramString) {
    float f1 = centerOf(paramInt1, paramInt3);
    float f2 = centerOf(paramInt2, paramInt3);
    float f3 = paramInt3;
    paramInt3 = 1082130432;
    float f4 = 4.0F;
    float f5 = f3 * f4;
    Lines.stroke(2.0F);
    Color color = Color.black;
    float f6 = 0.6F;
    Draw.color(color, f6);
    float f7 = f1 - f5;
    float f8 = f7 - f6;
    f5 = f2 - f5;
    f6 = f5 - f6;
    f3 *= 8.0F;
    float f9 = 1.2F + f3;
    Lines.rect(f8, f6, f9, f9);
    f6 = 0.95F;
    Draw.color(paramColor, f6);
    Lines.rect(f7, f5, f3, f3);
    try {
      Font font = Fonts.outline;
      f6 = f2 + f4;
      Color color1 = Color.white;
      boolean bool = true;
      f9 = 0.22F;
      String str = paramString;
      font.draw(paramString, f1, f6, color1, f9, false, bool);
    } finally {}
    Draw.color();
  }
  
  private static float centerOf(int paramInt1, int paramInt2) {
    float f1 = paramInt1 * 8.0F;
    float f2 = ((paramInt2 + 1) % 2) * 4.0F;
    return f1 + f2;
  }
  
  public static void clearPoints() {
    int i = -1 << -1;
    LunariansSettings.containerPickX = i;
    LunariansSettings.containerPickY = i;
    LunariansSettings.containerDropX = i;
    LunariansSettings.containerDropY = i;
    origAy = i;
    origAx = i;
    phase.clear();
    stageT.clear();
    LunariansSettings.save();
  }
  
  public static int count() {
    return count;
  }
  
  private static void draw() {
    // Byte code:
    //   0: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   3: astore_0
    //   4: aload_0
    //   5: ifnull -> 253
    //   8: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   11: astore_0
    //   12: aload_0
    //   13: invokevirtual isGame : ()Z
    //   16: istore_1
    //   17: iload_1
    //   18: ifeq -> 253
    //   21: invokestatic hasPoints : ()Z
    //   24: istore_1
    //   25: iload_1
    //   26: ifeq -> 253
    //   29: getstatic lunarians/LunariansSettings.enableAutoContainer : Z
    //   32: istore_1
    //   33: iload_1
    //   34: ifne -> 53
    //   37: getstatic arc/util/Time.time : F
    //   40: fstore_2
    //   41: getstatic lunarians/AutoContainer.markUntil : F
    //   44: fstore_3
    //   45: fload_2
    //   46: fload_3
    //   47: fcmpl
    //   48: istore_1
    //   49: iload_1
    //   50: ifgt -> 253
    //   53: getstatic lunarians/LunariansSettings.containerPickX : I
    //   56: istore_1
    //   57: getstatic lunarians/LunariansSettings.containerPickY : I
    //   60: istore #4
    //   62: getstatic lunarians/LunariansSettings.containerDropX : I
    //   65: istore #5
    //   67: getstatic lunarians/LunariansSettings.containerDropY : I
    //   70: istore #6
    //   72: getstatic lunarians/AutoContainer.blockSize : I
    //   75: istore #7
    //   77: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   80: astore #8
    //   82: aload #8
    //   84: iload_1
    //   85: iload #4
    //   87: invokevirtual build : (II)Lmindustry/gen/Building;
    //   90: astore #8
    //   92: aload #8
    //   94: ifnull -> 136
    //   97: aload #8
    //   99: getfield block : Lmindustry/world/Block;
    //   102: astore #9
    //   104: aload #9
    //   106: ifnull -> 136
    //   109: aload #8
    //   111: getfield block : Lmindustry/world/Block;
    //   114: astore #9
    //   116: aload #9
    //   118: getfield size : I
    //   121: istore #7
    //   123: aload #8
    //   125: invokevirtual tileX : ()I
    //   128: istore_1
    //   129: aload #8
    //   131: invokevirtual tileY : ()I
    //   134: istore #4
    //   136: ldc 120.0
    //   138: invokestatic z : (F)V
    //   141: ldc 1.5
    //   143: invokestatic stroke : (F)V
    //   146: getstatic mindustry/graphics/Pal.accent : Larc/graphics/Color;
    //   149: ldc_w 0.7
    //   152: invokestatic color : (Larc/graphics/Color;F)V
    //   155: iload_1
    //   156: iload #7
    //   158: invokestatic centerOf : (II)F
    //   161: fstore #10
    //   163: iload #4
    //   165: iload #7
    //   167: invokestatic centerOf : (II)F
    //   170: fstore #11
    //   172: iload #5
    //   174: iload #7
    //   176: invokestatic centerOf : (II)F
    //   179: fstore #12
    //   181: iload #6
    //   183: iload #7
    //   185: invokestatic centerOf : (II)F
    //   188: fstore #13
    //   190: bipush #14
    //   192: istore #14
    //   194: fload #10
    //   196: fload #11
    //   198: fload #12
    //   200: fload #13
    //   202: iload #14
    //   204: invokestatic dashLine : (FFFFI)V
    //   207: getstatic arc/graphics/Color.orange : Larc/graphics/Color;
    //   210: astore #8
    //   212: ldc_w 'A'
    //   215: astore #9
    //   217: iload_1
    //   218: iload #4
    //   220: iload #7
    //   222: aload #8
    //   224: aload #9
    //   226: invokestatic box : (IIILarc/graphics/Color;Ljava/lang/String;)V
    //   229: getstatic arc/graphics/Color.cyan : Larc/graphics/Color;
    //   232: astore_0
    //   233: ldc_w 'B'
    //   236: astore #15
    //   238: iload #5
    //   240: iload #6
    //   242: iload #7
    //   244: aload_0
    //   245: aload #15
    //   247: invokestatic box : (IIILarc/graphics/Color;Ljava/lang/String;)V
    //   250: invokestatic reset : ()V
    //   253: return
    //   254: pop
    //   255: goto -> 136
    // Exception table:
    //   from	to	target	type
    //   77	80	254	finally
    //   85	90	254	finally
    //   97	102	254	finally
    //   109	114	254	finally
    //   116	121	254	finally
    //   123	128	254	finally
    //   129	134	254	finally
  }
  
  private static void drive(Unit paramUnit, Payloadc paramPayloadc) {
    UnitCommand unitCommand2;
    boolean bool4;
    int i3;
    boolean bool3;
    int i2;
    float f9;
    int i5;
    UnitCommand unitCommand3;
    Unit unit = paramUnit;
    int i = paramUnit.id;
    IntIntMap intIntMap = phase;
    float f1 = 0.0F / 0.0F;
    int j = intIntMap.get(i, -1);
    int k = LunariansSettings.containerPickX;
    int m = LunariansSettings.containerPickY;
    int n = LunariansSettings.containerDropX;
    int i1 = LunariansSettings.containerDropY;
    float[] arrayOfFloat1 = pickCenter(k, m);
    float[] arrayOfFloat2 = pickCenter(n, i1);
    float f2 = arrayOfFloat1[0];
    byte b = 1;
    float f3 = arrayOfFloat1[b];
    float f4 = arrayOfFloat2[0];
    float f5 = arrayOfFloat2[b];
    float f6 = dropOffset(paramPayloadc);
    boolean bool1 = paramPayloadc.hasPayload();
    if (j < 0) {
      if (bool1) {
        j = 2;
        f1 = 2.8E-45F;
      } else {
        j = 0;
        f1 = 0.0F;
        Object object = null;
      } 
      setStage(i, j);
      j = intIntMap.get(i, 0);
    } 
    b = 7;
    int i4 = 5;
    float f7 = 7.0E-45F;
    float f8 = 10.0F;
    switch (j) {
      default:
        setStage(i, 0);
        return;
      case 9:
        unitCommand1 = UnitCommand.moveCommand;
        setCommand(unit, unitCommand1);
        f9 = stageSecs(i);
        f7 = waitAfterReturn();
        i5 = f9 cmp f7;
        if (i5 >= 0) {
          setStage(i, 0);
          return;
        } 
        return;
      case 8:
        if (!bool1) {
          unitCommand1 = UnitCommand.moveCommand;
          setCommand(unit, unitCommand1);
          setStage(i, 9);
          return;
        } 
        f7 = stageSecs(i);
        i4 = f7 cmp f8;
        if (i4 > 0) {
          unitCommand1 = UnitCommand.moveCommand;
          setCommand(unit, unitCommand1);
          setStage(i, b);
          return;
        } 
        return;
      case 7:
        if (!bool1) {
          setStage(i, i4);
          return;
        } 
        i4 = origAx;
        j = -1 << -1;
        f1 = -0.0F;
        if (i4 != j)
          k = i4; 
        i4 = origAy;
        if (i4 != j)
          m = i4; 
        bool4 = approachExact(unit, k, m, f6);
        if (bool4) {
          unitCommand1 = UnitCommand.unloadPayloadCommand;
          setCommand(unit, unitCommand1);
          setStage(i, 8);
          return;
        } 
        return;
      case 6:
        if (bool1) {
          unitCommand1 = UnitCommand.moveCommand;
          setCommand(unit, unitCommand1);
          setStage(i, b);
          return;
        } 
        f1 = stageSecs(i);
        j = f1 cmp f8;
        if (j > 0) {
          UnitCommand unitCommand = UnitCommand.moveCommand;
          setCommand(unit, unitCommand);
          setStage(i, bool4);
          return;
        } 
        unitCommand3 = UnitCommand.loadBlocksCommand;
        setCommand(unit, unitCommand3);
        pickStep(unit, n, i1);
        return;
      case 5:
        if (bool1) {
          setStage(i, b);
          return;
        } 
        unitCommand1 = UnitCommand.moveCommand;
        setCommand(unit, unitCommand1);
        goTo(unit, f4, f5, false);
        f7 = 40.0F;
        bool4 = unit.within(f4, f5, f7);
        if (bool4) {
          unitCommand1 = UnitCommand.loadBlocksCommand;
          setCommand(unit, unitCommand1);
          setStage(i, 6);
          goTo(unit, f4, f5, true);
          return;
        } 
        return;
      case 4:
        unitCommand2 = UnitCommand.moveCommand;
        setCommand(unit, unitCommand2);
        f9 = stageSecs(i);
        f1 = waitAfterPlace();
        i5 = f9 cmp f1;
        if (i5 >= 0) {
          setStage(i, bool4);
          return;
        } 
        return;
      case 3:
        if (!bool1) {
          unitCommand1 = UnitCommand.moveCommand;
          setCommand(unit, unitCommand1);
          setStage(i, 4);
          return;
        } 
        f7 = stageSecs(i);
        i3 = f7 cmp f8;
        if (i3 > 0) {
          unitCommand1 = UnitCommand.moveCommand;
          setCommand(unit, unitCommand1);
          setStage(i, 2);
          return;
        } 
        return;
      case 2:
        if (!bool1) {
          setStage(i, 0);
          return;
        } 
        bool3 = approachExact(unit, n, i1, f6);
        if (bool3) {
          unitCommand1 = UnitCommand.unloadPayloadCommand;
          setCommand(unit, unitCommand1);
          setStage(i, 3);
          return;
        } 
        return;
      case 1:
        if (bool1) {
          unitCommand1 = UnitCommand.moveCommand;
          setCommand(unit, unitCommand1);
          setStage(i, 2);
          return;
        } 
        f7 = stageSecs(i);
        i2 = f7 cmp f8;
        if (i2 > 0) {
          unitCommand1 = UnitCommand.moveCommand;
          setCommand(unit, unitCommand1);
          setStage(i, 0);
          return;
        } 
        unitCommand3 = UnitCommand.loadBlocksCommand;
        setCommand(unit, unitCommand3);
        pickStep(unit, k, m);
        return;
      case 0:
        break;
    } 
    if (bool1) {
      setStage(i, 2);
      return;
    } 
    UnitCommand unitCommand1 = UnitCommand.moveCommand;
    setCommand(unit, unitCommand1);
    goTo(unit, f2, f3, false);
    f7 = 40.0F;
    boolean bool2 = unit.within(f2, f3, f7);
    if (bool2) {
      try {
        World world = Vars.world;
        Building building = world.build(k, m);
        if (building != null) {
          Block block = building.block;
          if (block != null) {
            j = building.tileX();
            origAx = j;
            j = building.tileY();
            origAy = j;
            Block block1 = building.block;
            int i6 = block1.size;
            blockSize = i6;
          } 
        } 
      } finally {}
      unitCommand1 = UnitCommand.loadBlocksCommand;
      setCommand(unit, unitCommand1);
      bool2 = true;
      setStage(i, bool2);
      goTo(unit, f2, f3, bool2);
      return;
    } 
  }
  
  private static float dropOffset(Payloadc paramPayloadc) {
    try {
      Seq seq = paramPayloadc.payloads();
      Object object = seq.peek();
      object = object;
      boolean bool = object instanceof mindustry.world.blocks.payloads.BuildPayload;
      if (bool) {
        object = object;
        object = object.block();
        return ((Block)object).offset;
      } 
    } finally {}
    return ((blockSize + 1) % 2) * 4.0F;
  }
  
  private static void goTo(Unit paramUnit, float paramFloat1, float paramFloat2, boolean paramBoolean) {
    IntFloatMap intFloatMap = lastCmd;
    int j = paramUnit.id;
    float f1 = -9999.0F;
    float f2 = intFloatMap.get(j, f1);
    if (!paramBoolean) {
      float f = (Time.time - f2) / 60.0F;
      j = 1075838976;
      f2 = 2.5F;
      int m = f cmp f2;
      if (m < 0)
        return; 
    } 
    int i = paramUnit.id;
    f2 = Time.time;
    intFloatMap.put(i, f2);
    Player player = Vars.player;
    int k = paramUnit.id;
    int[] arrayOfInt = { k };
    Vec2 vec2 = new Vec2();
    this(paramFloat1, paramFloat2);
    Call.commandUnits(player, arrayOfInt, null, null, vec2, false, true);
  }
  
  public static boolean hasPoints() {
    int i = LunariansSettings.containerPickX;
    int j = -1 << -1;
    if (i != j) {
      i = LunariansSettings.containerDropX;
      if (i != j)
        return true; 
    } 
    return false;
  }
  
  public static void init() {
    boolean bool = Vars.headless;
    if (bool)
      return; 
    EventType.Trigger trigger = EventType.Trigger.update;
    AutoContainer$0 autoContainer$0 = new AutoContainer$0();
    this();
    Events.run(trigger, autoContainer$0);
    trigger = EventType.Trigger.draw;
    AutoContainer$1 autoContainer$1 = new AutoContainer$1();
    this();
    Events.run(trigger, autoContainer$1);
    AutoContainer$2 autoContainer$2 = new AutoContainer$2();
    this();
    Events.on(EventType.WorldLoadEvent.class, autoContainer$2);
  }
  
  private static float[] pickCenter(int paramInt1, int paramInt2) {
    Building building = Vars.world.build(paramInt1, paramInt2);
    boolean bool = true;
    byte b = 2;
    if (building != null) {
      float f4 = building.x;
      float f5 = building.y;
      float[] arrayOfFloat1 = new float[b];
      arrayOfFloat1[0] = f4;
      arrayOfFloat1[bool] = f5;
      return arrayOfFloat1;
    } 
    float f1 = paramInt1;
    float f3 = 8.0F;
    f1 *= f3;
    float f2 = paramInt2 * f3;
    float[] arrayOfFloat = new float[b];
    arrayOfFloat[0] = f1;
    arrayOfFloat[bool] = f2;
    return arrayOfFloat;
  }
  
  private static void pickStep(Unit paramUnit, int paramInt1, int paramInt2) {
    Building building1 = Vars.world.build(paramInt1, paramInt2);
    World world = Vars.world;
    float f1 = paramUnit.x;
    float f2 = paramUnit.y;
    Building building2 = world.buildWorld(f1, f2);
    float[] arrayOfFloat = pickCenter(paramInt1, paramInt2);
    paramInt2 = 1;
    f1 = 0.0F;
    if (building1 != null && building2 == building1) {
      float f5 = arrayOfFloat[0];
      float f6 = arrayOfFloat[paramInt2];
      f2 = 6.0F;
      boolean bool = paramUnit.within(f5, f6, f2);
      if (bool) {
        float f = paramUnit.x;
        f5 = paramUnit.y;
        goTo(paramUnit, f, f5, paramInt2);
        return;
      } 
    } 
    float f3 = arrayOfFloat[0];
    float f4 = arrayOfFloat[paramInt2];
    goTo(paramUnit, f3, f4, false);
  }
  
  public static void reset() {
    timer = 0.0F;
    count = 0;
    int i = -1 << -1;
    origAy = i;
    origAx = i;
    phase.clear();
    stageT.clear();
    lastCmd.clear();
  }
  
  private static void scan() {
    int i = 0;
    count = 0;
    String str = LunariansSettings.containerUnit;
    if (str != null) {
      str = LunariansSettings.containerUnit;
      boolean bool = str.isEmpty();
      if (!bool) {
        bool = hasPoints();
        if (bool) {
          tmp.clear();
          EntityGroup entityGroup = Groups.unit;
          int j = entityGroup.size();
          int k = LunariansSettings.containerMaxUnits;
          int m;
          for (m = 0; m < j; m++) {
            Unit unit = (Unit)Groups.unit.index(m);
            if (unit != null) {
              boolean bool1 = unit.isValid();
              if (bool1) {
                Team team1 = unit.team;
                Team team2 = Vars.player.team();
                if (team1 == team2) {
                  bool1 = unit instanceof Payloadc;
                  if (bool1) {
                    UnitController unitController = unit.controller();
                    bool1 = unitController instanceof CommandAI;
                    if (bool1) {
                      UnitType unitType = unit.type;
                      if (unitType != null) {
                        String str1 = LunariansSettings.containerUnit;
                        String str2 = unit.type.name;
                        bool1 = str1.equals(str2);
                        if (bool1) {
                          Seq seq = tmp;
                          seq.add(unit);
                          if (k > 0) {
                            int n = seq.size;
                            if (n < k)
                              continue; 
                            break;
                          } 
                        } 
                      } 
                    } 
                  } 
                } 
              } 
            } 
            continue;
          } 
          while (true) {
            Payloadc payloadc;
            Seq seq = tmp;
            k = seq.size;
            if (i < k) {
              Unit unit = (Unit)seq.get(i);
              payloadc = (Payloadc)seq.get(i);
              drive(unit, payloadc);
              i++;
              continue;
            } 
            i = ((Seq)payloadc).size;
            count = i;
            break;
          } 
        } 
      } 
    } 
  }
  
  private static void setCommand(Unit paramUnit, UnitCommand paramUnitCommand) {
    try {
      UnitController unitController = paramUnit.controller();
      CommandAI commandAI = (CommandAI)unitController;
      UnitCommand unitCommand = commandAI.command;
      if (unitCommand != paramUnitCommand) {
        Player player = Vars.player;
        int i = paramUnit.id;
        int[] arrayOfInt = { i };
        Call.setUnitCommand(player, arrayOfInt, paramUnitCommand);
      } 
    } finally {}
  }
  
  public static void setPoints(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    LunariansSettings.containerPickX = paramInt1;
    LunariansSettings.containerPickY = paramInt2;
    LunariansSettings.containerDropX = paramInt3;
    LunariansSettings.containerDropY = paramInt4;
    paramInt1 = -1 << -1;
    origAy = paramInt1;
    origAx = paramInt1;
    phase.clear();
    stageT.clear();
    showMarks();
    LunariansSettings.save();
  }
  
  private static void setStage(int paramInt1, int paramInt2) {
    phase.put(paramInt1, paramInt2);
    IntFloatMap intFloatMap = stageT;
    float f = Time.time;
    intFloatMap.put(paramInt1, f);
    lastCmd.remove(paramInt1, 0.0F);
  }
  
  public static void setWaitAfterReturn(float paramFloat) {
    Settings settings = Core.settings;
    Float float_ = Float.valueOf(paramFloat);
    settings.put("lunarians-container-wait2", float_);
  }
  
  public static void showMarks() {
    markUntil = Time.time + 1200.0F;
  }
  
  private static float stageSecs(int paramInt) {
    float f1 = Time.time;
    IntFloatMap intFloatMap = stageT;
    float f2 = Time.time;
    float f3 = intFloatMap.get(paramInt, f2);
    return (f1 - f3) / 60.0F;
  }
  
  private static void update() {
    boolean bool = LunariansSettings.enableAutoContainer;
    if (bool) {
      GameState gameState = Vars.state;
      if (gameState != null) {
        gameState = Vars.state;
        bool = gameState.isGame();
        if (bool) {
          Player player = Vars.player;
          if (player != null) {
            float f1 = timer;
            float f2 = Time.delta;
            float f3 = 60.0F;
            f2 /= f3;
            f1 += f2;
            timer = f1;
            int j = 1050253722;
            f2 = LunariansSettings.actionInterval(0.3F);
            int i = f1 cmp f2;
            if (i >= 0) {
              i = 0;
              f1 = 0.0F;
              player = null;
              timer = 0.0F;
              try {
                return;
              } finally {
                player = null;
                boolean bool1 = errorLogged;
              } 
            } 
            return;
          } 
        } 
      } 
    } 
    count = 0;
  }
  
  public static float waitAfterPlace() {
    float f = LunariansSettings.containerWaitSec;
    return Math.max(0.0F, f);
  }
  
  public static float waitAfterReturn() {
    float f = Core.settings.getFloat("lunarians-container-wait2", 5.0F);
    return Math.max(0.0F, f);
  }
}
