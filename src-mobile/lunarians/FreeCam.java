package lunarians;

import arc.Core;
import arc.Events;
import arc.func.Boolp;
import arc.graphics.Camera;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.Vars;
import mindustry.core.Control;
import mindustry.core.World;
import mindustry.game.EventType;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.input.InputHandler;

public class FreeCam {
  private static final Boolp FREECAM_INPUT_LOCK;
  
  private static boolean active;
  
  private static boolean errorLogged;
  
  private static float lastTouchX;
  
  private static float lastTouchY;
  
  private static boolean lastTouched;
  
  private static Unit lockedUnit;
  
  private static float lockedX;
  
  private static float lockedY;
  
  private static InputHandler registeredInput;
  
  private static boolean touchStartedOnUi;
  
  static {
    FreeCam$1 freeCam$1 = new FreeCam$1();
    this();
    FREECAM_INPUT_LOCK = freeCam$1;
  }
  
  private static void begin() {
    Unit unit;
    Player player = Vars.player;
    float f = 0.0F;
    if (player == null) {
      boolean bool = false;
      player = null;
    } else {
      unit = Vars.player.unit();
    } 
    if (unit != null) {
      lockedUnit = unit;
      f = unit.x;
      lockedX = f;
      lockedY = unit.y;
      boolean bool = true;
      active = bool;
    } else {
      lockedUnit = null;
      active = false;
    } 
    lastTouched = false;
    touchStartedOnUi = false;
  }
  
  private static void clampCamera() {
    Camera camera = Core.camera;
    if (camera != null) {
      World world = Vars.world;
      if (world != null) {
        world = Vars.world;
        int i = world.width();
        if (i > 0) {
          Vec2 vec2 = Core.camera.position;
          Camera camera1 = Core.camera;
          float f1 = -camera1.width;
          float f2 = 4.0F;
          f1 /= f2;
          Camera camera2 = Core.camera;
          float f3 = -camera2.height / f2;
          World world1 = Vars.world;
          int j = world1.unitWidth();
          float f4 = j;
          float f5 = Core.camera.width / f2;
          f4 += f5;
          World world2 = Vars.world;
          int k = world2.unitHeight();
          f5 = k;
          Camera camera3 = Core.camera;
          float f6 = camera3.height / f2;
          f5 += f6;
          vec2.clamp(f1, f3, f4, f5);
        } 
      } 
    } 
  }
  
  private static void doFreezeOnDraw() {
    boolean bool = active;
    if (bool) {
      bool = LunariansSettings.enableFreeCam;
      if (bool) {
        Unit unit = lockedUnit;
        if (unit != null) {
          Player player = Vars.player;
          if (player != null) {
            Unit unit1 = Vars.player.unit();
            Unit unit2 = lockedUnit;
            if (unit1 == unit2)
              freezeUnit(); 
          } 
        } 
      } 
    } 
  }
  
  private static void doUpdate() {
    // Byte code:
    //   0: invokestatic ensureInputLock : ()V
    //   3: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   6: astore_0
    //   7: aload_0
    //   8: ifnull -> 101
    //   11: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   14: astore_0
    //   15: aload_0
    //   16: invokevirtual isGame : ()Z
    //   19: istore_1
    //   20: iload_1
    //   21: ifeq -> 101
    //   24: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   27: astore_0
    //   28: aload_0
    //   29: ifnull -> 101
    //   32: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   35: invokevirtual unit : ()Lmindustry/gen/Unit;
    //   38: astore_0
    //   39: aload_0
    //   40: ifnull -> 101
    //   43: getstatic lunarians/LunariansSettings.enableFreeCam : Z
    //   46: istore_1
    //   47: iload_1
    //   48: ifne -> 55
    //   51: invokestatic stop : ()V
    //   54: return
    //   55: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   58: invokevirtual unit : ()Lmindustry/gen/Unit;
    //   61: astore_0
    //   62: getstatic lunarians/FreeCam.active : Z
    //   65: istore_2
    //   66: iload_2
    //   67: ifeq -> 79
    //   70: getstatic lunarians/FreeCam.lockedUnit : Lmindustry/gen/Unit;
    //   73: astore_3
    //   74: aload_3
    //   75: aload_0
    //   76: if_acmpeq -> 94
    //   79: invokestatic begin : ()V
    //   82: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   85: invokevirtual unit : ()Lmindustry/gen/Unit;
    //   88: astore_0
    //   89: aload_0
    //   90: ifnonnull -> 94
    //   93: return
    //   94: invokestatic moveCameraFromInput : ()V
    //   97: invokestatic freezeUnit : ()V
    //   100: return
    //   101: invokestatic stop : ()V
    //   104: return
  }
  
  private static void ensureInputLock() {
    Control control = Vars.control;
    if (control != null) {
      InputHandler inputHandler = Vars.control.input;
      if (inputHandler != null) {
        inputHandler = registeredInput;
        InputHandler inputHandler1 = Vars.control.input;
        if (inputHandler != inputHandler1) {
          Seq seq = inputHandler1.inputLocks;
          Boolp boolp = FREECAM_INPUT_LOCK;
          seq.add(boolp);
          registeredInput = inputHandler1;
        } 
      } 
    } 
  }
  
  public static void freezeOnDraw() {
    try {
      doFreezeOnDraw();
    } finally {}
  }
  
  private static void freezeUnit() {
    Unit unit = lockedUnit;
    if (unit == null)
      return; 
    float f = lockedX;
    unit.x = f;
    unit = lockedUnit;
    f = lockedY;
    unit.y = f;
    lockedUnit.vel.set(0.0F, 0.0F);
  }
  
  public static void init() {
    boolean bool = Vars.headless;
    if (bool)
      return; 
    ensureInputLock();
    EventType.Trigger trigger = EventType.Trigger.update;
    FreeCam$2 freeCam$2 = new FreeCam$2();
    this();
    Events.run(trigger, freeCam$2);
    trigger = EventType.Trigger.draw;
    FreeCam$3 freeCam$3 = new FreeCam$3();
    this();
    Events.run(trigger, freeCam$3);
  }
  
  public static boolean isActive() {
    return active;
  }
  
  static boolean lam_static$0() {
    boolean bool = active;
    if (bool) {
      bool = LunariansSettings.enableFreeCam;
      if (bool)
        return true; 
    } 
    return false;
  }
  
  private static void moveCameraFromInput() {
    // Byte code:
    //   0: getstatic arc/Core.camera : Larc/graphics/Camera;
    //   3: astore_0
    //   4: aload_0
    //   5: ifnull -> 455
    //   8: getstatic arc/Core.input : Larc/Input;
    //   11: astore_0
    //   12: aload_0
    //   13: ifnull -> 455
    //   16: getstatic arc/Core.input : Larc/Input;
    //   19: astore_0
    //   20: getstatic mindustry/input/Binding.moveX : Larc/input/KeyBind;
    //   23: astore_1
    //   24: aload_0
    //   25: aload_1
    //   26: invokevirtual axis : (Larc/input/KeyBind;)F
    //   29: fstore_2
    //   30: getstatic arc/Core.input : Larc/Input;
    //   33: astore_1
    //   34: getstatic mindustry/input/Binding.moveY : Larc/input/KeyBind;
    //   37: astore_3
    //   38: aload_1
    //   39: aload_3
    //   40: invokevirtual axis : (Larc/input/KeyBind;)F
    //   43: fstore #4
    //   45: iconst_0
    //   46: istore #5
    //   48: fconst_0
    //   49: fstore #6
    //   51: aconst_null
    //   52: astore_3
    //   53: ldc 1.0
    //   55: fstore #7
    //   57: fload_2
    //   58: fconst_0
    //   59: fcmpl
    //   60: istore #8
    //   62: iload #8
    //   64: ifne -> 78
    //   67: fload #4
    //   69: fconst_0
    //   70: fcmpl
    //   71: istore #5
    //   73: iload #5
    //   75: ifeq -> 136
    //   78: getstatic arc/util/Time.delta : F
    //   81: fstore #6
    //   83: fload #7
    //   85: fload #6
    //   87: invokestatic max : (FF)F
    //   90: fstore #6
    //   92: ldc 1086324736
    //   94: istore #8
    //   96: ldc 6.0
    //   98: fstore #9
    //   100: fload #6
    //   102: fload #9
    //   104: fmul
    //   105: fstore #6
    //   107: getstatic arc/Core.camera : Larc/graphics/Camera;
    //   110: getfield position : Larc/math/geom/Vec2;
    //   113: astore #10
    //   115: fload_2
    //   116: fload #6
    //   118: fmul
    //   119: fstore_2
    //   120: fload #4
    //   122: fload #6
    //   124: fmul
    //   125: fstore #4
    //   127: aload #10
    //   129: fload_2
    //   130: fload #4
    //   132: invokevirtual add : (FF)Larc/math/geom/Vec2;
    //   135: pop
    //   136: getstatic arc/Core.input : Larc/Input;
    //   139: astore_0
    //   140: aload_0
    //   141: invokevirtual isTouched : ()Z
    //   144: istore #11
    //   146: getstatic arc/Core.input : Larc/Input;
    //   149: astore_1
    //   150: aload_1
    //   151: invokevirtual mouseX : ()I
    //   154: istore #12
    //   156: iload #12
    //   158: i2f
    //   159: fstore #4
    //   161: getstatic arc/Core.input : Larc/Input;
    //   164: astore_3
    //   165: aload_3
    //   166: invokevirtual mouseY : ()I
    //   169: istore #5
    //   171: iload #5
    //   173: i2f
    //   174: fstore #6
    //   176: getstatic arc/Core.scene : Larc/scene/Scene;
    //   179: astore #10
    //   181: iconst_1
    //   182: istore #13
    //   184: ldc 1.4E-45
    //   186: fstore #14
    //   188: iconst_0
    //   189: istore #15
    //   191: fconst_0
    //   192: fstore #16
    //   194: aconst_null
    //   195: astore #17
    //   197: aload #10
    //   199: ifnull -> 233
    //   202: getstatic arc/Core.scene : Larc/scene/Scene;
    //   205: astore #10
    //   207: aload #10
    //   209: fload #4
    //   211: fload #6
    //   213: invokevirtual hasMouse : (FF)Z
    //   216: istore #8
    //   218: iload #8
    //   220: ifeq -> 233
    //   223: iconst_1
    //   224: istore #8
    //   226: ldc 1.4E-45
    //   228: fstore #9
    //   230: goto -> 242
    //   233: iconst_0
    //   234: istore #8
    //   236: fconst_0
    //   237: fstore #9
    //   239: aconst_null
    //   240: astore #10
    //   242: iload #11
    //   244: ifeq -> 443
    //   247: getstatic lunarians/FreeCam.lastTouched : Z
    //   250: istore #15
    //   252: iload #15
    //   254: ifne -> 275
    //   257: iload #8
    //   259: putstatic lunarians/FreeCam.touchStartedOnUi : Z
    //   262: fload #4
    //   264: putstatic lunarians/FreeCam.lastTouchX : F
    //   267: fload #6
    //   269: putstatic lunarians/FreeCam.lastTouchY : F
    //   272: goto -> 447
    //   275: getstatic lunarians/FreeCam.touchStartedOnUi : Z
    //   278: istore #15
    //   280: iload #15
    //   282: ifne -> 430
    //   285: iload #8
    //   287: ifne -> 430
    //   290: getstatic arc/Core.input : Larc/Input;
    //   293: astore #10
    //   295: aload #10
    //   297: iload #13
    //   299: invokevirtual isTouched : (I)Z
    //   302: istore #8
    //   304: iload #8
    //   306: ifne -> 430
    //   309: getstatic lunarians/FreeCam.lastTouchX : F
    //   312: fstore #9
    //   314: fload #4
    //   316: fload #9
    //   318: fsub
    //   319: fstore #9
    //   321: getstatic lunarians/FreeCam.lastTouchY : F
    //   324: fstore #14
    //   326: fload #6
    //   328: fload #14
    //   330: fsub
    //   331: fstore #14
    //   333: getstatic arc/Core.graphics : Larc/Graphics;
    //   336: astore #17
    //   338: aload #17
    //   340: ifnull -> 396
    //   343: getstatic arc/Core.graphics : Larc/Graphics;
    //   346: astore #17
    //   348: aload #17
    //   350: invokevirtual getWidth : ()I
    //   353: istore #15
    //   355: iload #15
    //   357: ifle -> 396
    //   360: getstatic arc/Core.camera : Larc/graphics/Camera;
    //   363: astore #18
    //   365: aload #18
    //   367: getfield width : F
    //   370: fstore #7
    //   372: getstatic arc/Core.graphics : Larc/Graphics;
    //   375: astore #17
    //   377: aload #17
    //   379: invokevirtual getWidth : ()I
    //   382: istore #15
    //   384: iload #15
    //   386: i2f
    //   387: fstore #16
    //   389: fload #7
    //   391: fload #16
    //   393: fdiv
    //   394: fstore #7
    //   396: getstatic arc/Core.camera : Larc/graphics/Camera;
    //   399: getfield position : Larc/math/geom/Vec2;
    //   402: astore #17
    //   404: fload #9
    //   406: fneg
    //   407: fload #7
    //   409: fmul
    //   410: fstore #9
    //   412: fload #14
    //   414: fneg
    //   415: fload #7
    //   417: fmul
    //   418: fstore #14
    //   420: aload #17
    //   422: fload #9
    //   424: fload #14
    //   426: invokevirtual add : (FF)Larc/math/geom/Vec2;
    //   429: pop
    //   430: fload #4
    //   432: putstatic lunarians/FreeCam.lastTouchX : F
    //   435: fload #6
    //   437: putstatic lunarians/FreeCam.lastTouchY : F
    //   440: goto -> 447
    //   443: iconst_0
    //   444: putstatic lunarians/FreeCam.touchStartedOnUi : Z
    //   447: iload #11
    //   449: putstatic lunarians/FreeCam.lastTouched : Z
    //   452: invokestatic clampCamera : ()V
    //   455: return
  }
  
  public static void pan(float paramFloat1, float paramFloat2) {
    boolean bool = active;
    if (bool) {
      Camera camera = Core.camera;
      if (camera != null) {
        Vec2 vec2 = Core.camera.position;
        vec2.add(paramFloat1, paramFloat2);
      } 
    } 
  }
  
  public static void setEnabled(boolean paramBoolean) {
    LunariansSettings.enableFreeCam = paramBoolean;
    LunariansSettings.save();
    if (paramBoolean) {
      begin();
      return;
    } 
    stop();
  }
  
  private static void stop() {
    active = false;
    lockedUnit = null;
    lastTouched = false;
    touchStartedOnUi = false;
  }
  
  public static void update() {
    try {
      return;
    } finally {
      Exception exception = null;
      boolean bool = errorLogged;
      if (!bool) {
        bool = true;
        errorLogged = bool;
        String str = "[Lunarians] FreeCam error, disabling";
        Log.err(str, exception);
      } 
      LunariansSettings.enableFreeCam = false;
      stop();
    } 
  }
}
