package lunarians.ui;

import arc.Core;
import arc.Events;
import arc.Input;
import arc.func.Cons;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.input.KeyCode;
import arc.math.geom.Vec2;
import arc.scene.Element;
import arc.scene.Scene;
import arc.scene.style.Drawable;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import arc.util.Time;
import lunarians.LunariansI18n;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.core.UI;
import mindustry.game.EventType;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;

public final class PathPicker {
  private static float armedAt;
  
  private static Cons callback;
  
  private static boolean cameraMode;
  
  private static Runnable cancelled;
  
  private static Table hud;
  
  private static final IntSeq points;
  
  private static boolean registered;
  
  static {
    IntSeq intSeq = new IntSeq();
    this();
    points = intSeq;
    cameraMode = true;
  }
  
  private static void addAtCurrent() {
    // Byte code:
    //   0: getstatic lunarians/ui/PathPicker.cameraMode : Z
    //   3: istore_0
    //   4: iload_0
    //   5: ifne -> 54
    //   8: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   11: astore_1
    //   12: aload_1
    //   13: ifnull -> 54
    //   16: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   19: invokevirtual unit : ()Lmindustry/gen/Unit;
    //   22: astore_1
    //   23: aload_1
    //   24: ifnull -> 54
    //   27: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   30: invokevirtual unit : ()Lmindustry/gen/Unit;
    //   33: astore_1
    //   34: aload_1
    //   35: invokevirtual tileX : ()I
    //   38: istore_0
    //   39: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   42: invokevirtual unit : ()Lmindustry/gen/Unit;
    //   45: astore_2
    //   46: aload_2
    //   47: invokevirtual tileY : ()I
    //   50: istore_3
    //   51: goto -> 92
    //   54: getstatic arc/Core.camera : Larc/graphics/Camera;
    //   57: getfield position : Larc/math/geom/Vec2;
    //   60: astore_1
    //   61: aload_1
    //   62: getfield x : F
    //   65: fstore #4
    //   67: fload #4
    //   69: invokestatic toTile : (F)I
    //   72: istore_0
    //   73: getstatic arc/Core.camera : Larc/graphics/Camera;
    //   76: getfield position : Larc/math/geom/Vec2;
    //   79: astore_2
    //   80: aload_2
    //   81: getfield y : F
    //   84: fstore #5
    //   86: fload #5
    //   88: invokestatic toTile : (F)I
    //   91: istore_3
    //   92: getstatic lunarians/ui/PathPicker.points : Larc/struct/IntSeq;
    //   95: astore #6
    //   97: aload #6
    //   99: iload_0
    //   100: invokevirtual add : (I)V
    //   103: aload #6
    //   105: iload_3
    //   106: invokevirtual add : (I)V
    //   109: invokestatic rebuildHud : ()V
    //   112: return
  }
  
  private static void draw() {
    Cons cons = callback;
    if (cons == null)
      return; 
    Draw.z(120.0F);
    Draw.color(Pal.accent);
    Lines.stroke(2.0F);
    boolean bool = false;
    float f1 = 0.0F;
    cons = null;
    float f2 = 0.0F;
    Vec2 vec2 = null;
    int i = 0;
    float f3 = 0.0F;
    while (true) {
      int j = i + 1;
      IntSeq intSeq = points;
      int k = intSeq.size;
      if (j < k) {
        k = intSeq.get(i) << 3;
        float f4 = k;
        float f5 = 4.0F;
        f4 += f5;
        j = intSeq.get(j) << 3;
        float f6 = j + f5;
        float f7 = 6.0F;
        Lines.circle(f4, f6, f7);
        if (i > 0)
          Lines.line(f1, f2, f4, f6); 
        i += 2;
        f2 = f6;
        f1 = f4;
        continue;
      } 
      bool = cameraMode;
      if (bool) {
        Vec2 vec21 = Core.camera.position;
        f1 = vec21.x;
        vec2 = Core.camera.position;
        f2 = vec2.y;
        i = 1090519040;
        f3 = 8.0F;
        float f4 = f1 - f3;
        float f5 = f1 + f3;
        Lines.line(f4, f2, f5, f2);
        f4 = f2 - f3;
        f2 += f3;
        Lines.line(f1, f4, f1, f2);
      } 
      Draw.reset();
      return;
    } 
  }
  
  private static void finish(boolean paramBoolean) {
    hideHud();
    Cons cons = callback;
    Runnable runnable = cancelled;
    callback = null;
    cancelled = null;
    if (paramBoolean) {
      if (runnable != null)
        runnable.run(); 
    } else if (cons != null) {
      IntSeq intSeq2 = new IntSeq();
      this();
      IntSeq intSeq1 = points;
      intSeq2.addAll(intSeq1);
      cons.get(intSeq2);
    } 
    points.clear();
  }
  
  private static void hideHud() {
    Table table = hud;
    if (table != null) {
      table.remove();
      table = null;
      hud = null;
    } 
  }
  
  public static void pick(Cons paramCons, Runnable paramRunnable) {
    callback = paramCons;
    cancelled = paramRunnable;
    IntSeq intSeq = points;
    intSeq.clear();
    armedAt = Time.time;
    boolean bool = true;
    cameraMode = bool;
    showHud();
    boolean bool1 = registered;
    if (!bool1) {
      registered = bool;
      EventType.Trigger trigger = EventType.Trigger.update;
      paramRunnable = new PathPicker$1();
      super();
      Events.run(trigger, paramRunnable);
      trigger = EventType.Trigger.drawOver;
      paramRunnable = new PathPicker$2();
      super();
      Events.run(trigger, paramRunnable);
    } 
    toast(LunariansI18n.get("pp_hint"));
  }
  
  private static void rebuildHud() {
    Table table4 = hud;
    if (table4 == null)
      return; 
    table4.clearChildren();
    table4 = hud;
    int i = points.size;
    byte b = 2;
    float f1 = 2.8E-45F;
    String str1 = String.valueOf(i / b);
    String str2 = "pp_points";
    str1 = LunariansI18n.get(str2, str1);
    Cell cell3 = table4.add(str1).colspan(3);
    float f2 = 4.0F;
    cell3.padBottom(f2).row();
    Table table3 = hud;
    boolean bool = cameraMode;
    if (bool) {
      str1 = "pp_cam";
    } else {
      str1 = "pp_unit";
    } 
    str1 = LunariansI18n.get(str1);
    PathPicker$3 pathPicker$3 = new PathPicker$3();
    this();
    Cell cell2 = table3.button(str1, pathPicker$3);
    f2 = 150.0F;
    cell2 = cell2.width(f2);
    float f3 = 40.0F;
    ((TextButton)cell2.height(f3).get()).getLabel().setFontScale(0.85F);
    Table table2 = hud;
    String str3 = LunariansI18n.get("pp_add");
    PathPicker$4 pathPicker$4 = new PathPicker$4();
    this();
    table2.button(str3, pathPicker$4).width(f2).height(f3);
    table2 = hud;
    str3 = LunariansI18n.get("pp_back");
    PathPicker$5 pathPicker$5 = new PathPicker$5();
    this();
    Cell cell1 = table2.button(str3, pathPicker$5);
    float f4 = 110.0F;
    cell1.width(f4).height(f3).row();
    Table table1 = hud;
    String str4 = LunariansI18n.get("pp_done");
    PathPicker$6 pathPicker$6 = new PathPicker$6();
    this();
    table1.button(str4, pathPicker$6).width(f2).height(f3).colspan(b);
    table1 = hud;
    str1 = LunariansI18n.get("pp_cancel");
    PathPicker$7 pathPicker$7 = new PathPicker$7();
    this();
    table1.button(str1, pathPicker$7).width(f4).height(f3);
    hud.pack();
    table1 = hud;
    f2 = Core.graphics.getWidth();
    f1 = 2.0F;
    f2 /= f1;
    f4 = hud.getWidth() / f1;
    f2 -= f4;
    table1.setPosition(f2, f3);
  }
  
  private static void showHud() {
    hideHud();
    Table table2 = new Table();
    Drawable drawable = Styles.black6;
    this(drawable);
    hud = table2;
    table2.defaults().pad(4.0F);
    rebuildHud();
    Scene scene = Core.scene;
    Table table3 = hud;
    scene.add((Element)table3);
    hud.pack();
    Table table1 = hud;
    float f1 = Core.graphics.getWidth();
    float f2 = 2.0F;
    f1 /= f2;
    float f3 = hud.getWidth() / f2;
    f1 -= f3;
    table1.setPosition(f1, 40.0F);
  }
  
  private static void toast(String paramString) {
    UI uI = Vars.ui;
    if (uI != null) {
      uI = Vars.ui;
      float f = 2.0F;
      uI.showInfoToast(paramString, f);
    } 
  }
  
  private static void update() {
    boolean bool;
    Cons cons = callback;
    if (cons != null) {
      GameState gameState = Vars.state;
      bool = true;
      if (gameState != null) {
        gameState = Vars.state;
        boolean bool1 = gameState.isGame();
        if (bool1) {
          float f1 = Time.time;
          float f2 = armedAt;
          f1 -= f2;
          f2 = 15.0F;
          int i = f1 cmp f2;
          if (i >= 0) {
            Input input = Core.input;
            KeyCode keyCode = KeyCode.escape;
            boolean bool2 = input.keyTap(keyCode);
            if (!bool2) {
              input = Core.input;
              keyCode = KeyCode.back;
              bool2 = input.keyTap(keyCode);
              if (!bool2)
                return; 
            } 
          } else {
            return;
          } 
          finish(bool);
          return;
        } 
      } 
    } else {
      return;
    } 
    finish(bool);
  }
}
