package lunarians;

import arc.Core;
import arc.Events;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import lunarians.ui.SlotButton;
import mindustry.Vars;
import mindustry.core.Control;
import mindustry.core.World;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.game.Schematic;
import mindustry.game.Schematics;
import mindustry.input.InputHandler;
import mindustry.world.Block;

public class SchematicPlacer {
  private static boolean armed;
  
  private static float clock;
  
  private static float downTime;
  
  private static float downX;
  
  private static float downY;
  
  private static boolean errorLogged;
  
  private static boolean moved;
  
  private static boolean multiTouch;
  
  public static Schematic schematic;
  
  public static SlotButton selectedSlot;
  
  private static boolean startedOnUi;
  
  private static boolean tracking;
  
  public static void cycleRotation() {
    LunariansSettings.schemRotation = LunariansSettings.schemRotation + 1 & 0x3;
    LunariansSettings.save();
  }
  
  public static void deselect() {
    Schematic schematic = schematic;
    if (schematic != null) {
      Control control = Vars.control;
      if (control != null) {
        InputHandler inputHandler = Vars.control.input;
        if (inputHandler != null) {
          Schematic schematic1 = Vars.control.input.lastSchematic;
          Schematic schematic2 = schematic;
          if (schematic1 == schematic2) {
            Seq seq = Vars.control.input.selectPlans;
            seq.clear();
          } 
        } 
      } 
    } 
    selectedSlot = null;
    armed = false;
    schematic = null;
    tracking = false;
  }
  
  public static void init() {
    boolean bool = Vars.headless;
    if (bool)
      return; 
    EventType.Trigger trigger = EventType.Trigger.update;
    SchematicPlacer$1 schematicPlacer$1 = new SchematicPlacer$1();
    this();
    Events.run(trigger, schematicPlacer$1);
    SchematicPlacer$2 schematicPlacer$2 = new SchematicPlacer$2();
    this();
    Events.on(EventType.WorldLoadEvent.class, schematicPlacer$2);
  }
  
  public static boolean isActive() {
    Schematic schematic = schematic;
    return (schematic != null);
  }
  
  public static boolean isSelected(SlotButton paramSlotButton) {
    if (paramSlotButton != null) {
      SlotButton slotButton = selectedSlot;
      if (slotButton == paramSlotButton) {
        Schematic schematic = schematic;
        if (schematic != null)
          return true; 
      } 
    } 
    return false;
  }
  
  private static void rotatePlans(Seq paramSeq, int paramInt1, int paramInt2) {
    int i = 0;
    while (true) {
      int j = paramSeq.size;
      if (i < j) {
        BuildPlan buildPlan = (BuildPlan)paramSeq.get(i);
        boolean bool = buildPlan.breaking;
        if (!bool) {
          Block block = buildPlan.block;
          if (block != null) {
            block = buildPlan.block;
            int k = block.size % 2;
            if (k == 0) {
              k = -1090519040;
              f1 = -0.5F;
            } else {
              k = 0;
              f1 = 0.0F;
              block = null;
            } 
            SchematicPlacer$3 schematicPlacer$3 = new SchematicPlacer$3();
            this(f1);
            buildPlan.pointConfig(schematicPlacer$3);
            float f1 = (buildPlan.x - paramInt1 << 3);
            float f2 = buildPlan.block.offset;
            f1 += f2;
            f2 = (buildPlan.y - paramInt2 << 3);
            float f3 = buildPlan.block.offset;
            f2 = -(f2 + f3);
            Block block2 = buildPlan.block;
            f3 = block2.offset;
            int m = World.toTile(f2 - f3) + paramInt1;
            buildPlan.x = m;
            Block block1 = buildPlan.block;
            f2 = block1.offset;
            f1 -= f2;
            k = World.toTile(f1) + paramInt2;
            buildPlan.y = k;
            block = buildPlan.block;
            m = buildPlan.rotation + 1;
            byte b = 4;
            f3 = 5.6E-45F;
            m = Mathf.mod(m, b);
            k = block.planRotation(m);
            buildPlan.rotation = k;
          } 
        } 
        i++;
        continue;
      } 
      break;
    } 
  }
  
  public static int rotation() {
    return LunariansSettings.schemRotation;
  }
  
  public static void select(SlotButton paramSlotButton, Schematic paramSchematic) {
    selectedSlot = paramSlotButton;
    schematic = paramSchematic;
    tracking = false;
    armed = false;
  }
  
  public static void setPanelVisible(boolean paramBoolean) {
    LunariansSettings.showSchematicPanel = paramBoolean;
    LunariansSettings.save();
    if (!paramBoolean)
      deselect(); 
  }
  
  private static void stageAt(float paramFloat1, float paramFloat2) {
    Vec2 vec2 = Core.input.mouseWorld(paramFloat1, paramFloat2);
    paramFloat2 = vec2.x;
    int i = World.toTile(paramFloat2);
    paramFloat1 = vec2.y;
    int j = World.toTile(paramFloat1);
    Schematics schematics = Vars.schematics;
    Schematic schematic = schematic;
    Seq seq = schematics.toPlans(schematic, i, j);
    int k = 0;
    schematic = null;
    int m = 0;
    BuildPlan buildPlan = null;
    while (true) {
      int n = rotation();
      if (m < n) {
        rotatePlans(seq, i, j);
        m++;
        continue;
      } 
      Seq seq1 = Vars.control.input.selectPlans;
      boolean bool = seq1.isEmpty();
      if (!bool) {
        bool = seq.isEmpty();
        if (!bool) {
          BuildPlan buildPlan1 = (BuildPlan)seq.first();
          while (true) {
            m = seq1.size;
            if (k < m) {
              buildPlan = (BuildPlan)seq1.get(k);
              n = buildPlan.x;
              int i1 = buildPlan1.x;
              if (n == i1) {
                n = buildPlan.y;
                i1 = buildPlan1.y;
                if (n == i1) {
                  Block block1 = buildPlan.block;
                  Block block2 = buildPlan1.block;
                  if (block1 == block2)
                    return; 
                } 
              } 
              k++;
              continue;
            } 
            break;
          } 
        } 
      } 
      seq1.addAll(seq);
      InputHandler inputHandler = Vars.control.input;
      Schematic schematic1 = schematic;
      inputHandler.lastSchematic = schematic1;
      return;
    } 
  }
  
  private static void stageDesktop() {
    InputHandler inputHandler = Vars.control.input;
    Schematic schematic = schematic;
    inputHandler.useSchematic(schematic);
    Vec2 vec2 = Core.input.mouseWorld();
    float f1 = vec2.x;
    int i = World.toTile(f1);
    float f2 = vec2.y;
    int j = World.toTile(f2);
    int k = 0;
    while (true) {
      int m = rotation();
      if (k < m) {
        Seq seq = Vars.control.input.selectPlans;
        rotatePlans(seq, i, j);
        k++;
        continue;
      } 
      break;
    } 
  }
  
  private static void update() {
    // Byte code:
    //   0: getstatic lunarians/SchematicPlacer.schematic : Lmindustry/game/Schematic;
    //   3: astore_0
    //   4: aload_0
    //   5: ifnull -> 654
    //   8: iconst_1
    //   9: istore_1
    //   10: getstatic lunarians/SchematicPlacer.clock : F
    //   13: fstore_2
    //   14: getstatic arc/util/Time.delta : F
    //   17: fstore_3
    //   18: ldc 1114636288
    //   20: istore #4
    //   22: ldc 60.0
    //   24: fstore #5
    //   26: fload_3
    //   27: fload #5
    //   29: fdiv
    //   30: fstore_3
    //   31: fload_2
    //   32: fload_3
    //   33: fadd
    //   34: fstore_2
    //   35: fload_2
    //   36: putstatic lunarians/SchematicPlacer.clock : F
    //   39: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   42: astore #6
    //   44: aload #6
    //   46: ifnull -> 655
    //   49: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   52: astore #6
    //   54: aload #6
    //   56: invokevirtual isGame : ()Z
    //   59: istore #7
    //   61: iload #7
    //   63: ifeq -> 655
    //   66: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   69: astore #6
    //   71: aload #6
    //   73: ifnull -> 655
    //   76: getstatic mindustry/Vars.control : Lmindustry/core/Control;
    //   79: astore #6
    //   81: aload #6
    //   83: getfield input : Lmindustry/input/InputHandler;
    //   86: astore #6
    //   88: aload #6
    //   90: getfield block : Lmindustry/world/Block;
    //   93: astore #6
    //   95: aload #6
    //   97: ifnull -> 104
    //   100: invokestatic deselect : ()V
    //   103: return
    //   104: getstatic mindustry/Vars.mobile : Z
    //   107: istore #7
    //   109: iload #7
    //   111: ifne -> 159
    //   114: getstatic arc/Core.input : Larc/Input;
    //   117: astore #6
    //   119: getstatic mindustry/input/Binding.deselect : Larc/input/KeyBind;
    //   122: astore #8
    //   124: aload #6
    //   126: aload #8
    //   128: invokevirtual keyTap : (Larc/input/KeyBind;)Z
    //   131: istore #7
    //   133: iload #7
    //   135: ifeq -> 159
    //   138: getstatic arc/Core.scene : Larc/scene/Scene;
    //   141: astore #6
    //   143: aload #6
    //   145: invokevirtual hasMouse : ()Z
    //   148: istore #7
    //   150: iload #7
    //   152: ifne -> 159
    //   155: invokestatic deselect : ()V
    //   158: return
    //   159: getstatic mindustry/Vars.mobile : Z
    //   162: istore #7
    //   164: iconst_0
    //   165: istore #9
    //   167: fconst_0
    //   168: fstore_3
    //   169: aconst_null
    //   170: astore #8
    //   172: iload #7
    //   174: ifne -> 298
    //   177: getstatic arc/Core.scene : Larc/scene/Scene;
    //   180: astore #6
    //   182: aload #6
    //   184: invokevirtual hasMouse : ()Z
    //   187: istore #7
    //   189: iload #7
    //   191: ifne -> 218
    //   194: getstatic arc/Core.scene : Larc/scene/Scene;
    //   197: astore #6
    //   199: aload #6
    //   201: invokevirtual hasDialog : ()Z
    //   204: istore #7
    //   206: iload #7
    //   208: ifne -> 218
    //   211: iconst_1
    //   212: istore #9
    //   214: ldc_w 1.4E-45
    //   217: fstore_3
    //   218: getstatic lunarians/SchematicPlacer.armed : Z
    //   221: istore #7
    //   223: iload #7
    //   225: ifne -> 258
    //   228: iload #9
    //   230: ifeq -> 654
    //   233: getstatic arc/Core.input : Larc/Input;
    //   236: astore #6
    //   238: aload #6
    //   240: invokevirtual justTouched : ()Z
    //   243: istore #7
    //   245: iload #7
    //   247: ifeq -> 654
    //   250: iload_1
    //   251: putstatic lunarians/SchematicPlacer.armed : Z
    //   254: invokestatic stageDesktop : ()V
    //   257: return
    //   258: getstatic mindustry/Vars.control : Lmindustry/core/Control;
    //   261: astore #6
    //   263: aload #6
    //   265: getfield input : Lmindustry/input/InputHandler;
    //   268: astore #6
    //   270: aload #6
    //   272: getfield selectPlans : Larc/struct/Seq;
    //   275: astore #6
    //   277: aload #6
    //   279: invokevirtual isEmpty : ()Z
    //   282: istore #7
    //   284: iload #7
    //   286: ifeq -> 654
    //   289: iload #9
    //   291: ifeq -> 654
    //   294: invokestatic stageDesktop : ()V
    //   297: return
    //   298: getstatic arc/Core.input : Larc/Input;
    //   301: astore #6
    //   303: aload #6
    //   305: invokevirtual isTouched : ()Z
    //   308: istore #7
    //   310: getstatic arc/Core.input : Larc/Input;
    //   313: astore #10
    //   315: aload #10
    //   317: invokevirtual mouseX : ()I
    //   320: istore #4
    //   322: iload #4
    //   324: i2f
    //   325: fstore #5
    //   327: getstatic arc/Core.input : Larc/Input;
    //   330: astore #11
    //   332: aload #11
    //   334: invokevirtual mouseY : ()I
    //   337: istore #12
    //   339: iload #12
    //   341: i2f
    //   342: fstore #13
    //   344: getstatic lunarians/SchematicPlacer.tracking : Z
    //   347: istore #14
    //   349: iload #7
    //   351: ifeq -> 571
    //   354: iload #14
    //   356: ifne -> 471
    //   359: iload_1
    //   360: putstatic lunarians/SchematicPlacer.tracking : Z
    //   363: fload #5
    //   365: putstatic lunarians/SchematicPlacer.downX : F
    //   368: fload #13
    //   370: putstatic lunarians/SchematicPlacer.downY : F
    //   373: getstatic lunarians/SchematicPlacer.clock : F
    //   376: fstore_2
    //   377: fload_2
    //   378: putstatic lunarians/SchematicPlacer.downTime : F
    //   381: iconst_0
    //   382: putstatic lunarians/SchematicPlacer.moved : Z
    //   385: getstatic arc/Core.input : Larc/Input;
    //   388: astore #6
    //   390: aload #6
    //   392: iload_1
    //   393: invokevirtual isTouched : (I)Z
    //   396: istore #7
    //   398: iload #7
    //   400: putstatic lunarians/SchematicPlacer.multiTouch : Z
    //   403: getstatic arc/Core.scene : Larc/scene/Scene;
    //   406: astore #6
    //   408: aload #6
    //   410: fload #5
    //   412: fload #13
    //   414: invokevirtual hasMouse : (FF)Z
    //   417: istore #7
    //   419: iload #7
    //   421: ifne -> 458
    //   424: getstatic arc/Core.scene : Larc/scene/Scene;
    //   427: astore #6
    //   429: aload #6
    //   431: invokevirtual hasDialog : ()Z
    //   434: istore #7
    //   436: iload #7
    //   438: ifne -> 458
    //   441: getstatic arc/Core.scene : Larc/scene/Scene;
    //   444: astore #6
    //   446: aload #6
    //   448: invokevirtual hasField : ()Z
    //   451: istore #7
    //   453: iload #7
    //   455: ifeq -> 465
    //   458: iconst_1
    //   459: istore #9
    //   461: ldc_w 1.4E-45
    //   464: fstore_3
    //   465: iload #9
    //   467: putstatic lunarians/SchematicPlacer.startedOnUi : Z
    //   470: return
    //   471: getstatic arc/Core.input : Larc/Input;
    //   474: astore #6
    //   476: aload #6
    //   478: iload_1
    //   479: invokevirtual isTouched : (I)Z
    //   482: istore #7
    //   484: iload #7
    //   486: ifeq -> 493
    //   489: iload_1
    //   490: putstatic lunarians/SchematicPlacer.multiTouch : Z
    //   493: getstatic lunarians/SchematicPlacer.downX : F
    //   496: fstore_2
    //   497: fload #5
    //   499: fload_2
    //   500: fsub
    //   501: fstore #5
    //   503: fload #5
    //   505: invokestatic abs : (F)F
    //   508: fstore_2
    //   509: ldc_w 1096810496
    //   512: istore #9
    //   514: ldc_w 14.0
    //   517: fstore_3
    //   518: fload_3
    //   519: invokestatic scl : (F)F
    //   522: fstore #5
    //   524: fload_2
    //   525: fload #5
    //   527: fcmpl
    //   528: istore #7
    //   530: iload #7
    //   532: ifgt -> 566
    //   535: getstatic lunarians/SchematicPlacer.downY : F
    //   538: fstore_2
    //   539: fload #13
    //   541: fload_2
    //   542: fsub
    //   543: fstore #13
    //   545: fload #13
    //   547: invokestatic abs : (F)F
    //   550: fstore_2
    //   551: fload_3
    //   552: invokestatic scl : (F)F
    //   555: fstore_3
    //   556: fload_2
    //   557: fload_3
    //   558: fcmpl
    //   559: istore #7
    //   561: iload #7
    //   563: ifle -> 654
    //   566: iload_1
    //   567: putstatic lunarians/SchematicPlacer.moved : Z
    //   570: return
    //   571: iload #14
    //   573: ifeq -> 654
    //   576: iconst_0
    //   577: putstatic lunarians/SchematicPlacer.tracking : Z
    //   580: getstatic lunarians/SchematicPlacer.startedOnUi : Z
    //   583: istore #7
    //   585: iload #7
    //   587: ifne -> 654
    //   590: getstatic lunarians/SchematicPlacer.moved : Z
    //   593: istore #7
    //   595: iload #7
    //   597: ifne -> 654
    //   600: getstatic lunarians/SchematicPlacer.multiTouch : Z
    //   603: istore #7
    //   605: iload #7
    //   607: ifne -> 654
    //   610: getstatic lunarians/SchematicPlacer.clock : F
    //   613: fstore_2
    //   614: getstatic lunarians/SchematicPlacer.downTime : F
    //   617: fstore_3
    //   618: fload_2
    //   619: fload_3
    //   620: fsub
    //   621: fstore_2
    //   622: ldc_w 1058642330
    //   625: istore #9
    //   627: ldc_w 0.6
    //   630: fstore_3
    //   631: fload_2
    //   632: fload_3
    //   633: fcmpg
    //   634: istore #7
    //   636: iload #7
    //   638: ifge -> 654
    //   641: getstatic lunarians/SchematicPlacer.downX : F
    //   644: fstore_2
    //   645: getstatic lunarians/SchematicPlacer.downY : F
    //   648: fstore_3
    //   649: fload_2
    //   650: fload_3
    //   651: invokestatic stageAt : (FF)V
    //   654: return
    //   655: invokestatic deselect : ()V
    //   658: return
    //   659: astore #6
    //   661: getstatic lunarians/SchematicPlacer.errorLogged : Z
    //   664: istore #9
    //   666: iload #9
    //   668: ifne -> 685
    //   671: iload_1
    //   672: putstatic lunarians/SchematicPlacer.errorLogged : Z
    //   675: ldc_w '[Lunarians] SchematicPlacer error'
    //   678: astore_0
    //   679: aload_0
    //   680: aload #6
    //   682: invokestatic err : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   685: invokestatic deselect : ()V
    //   688: return
    // Exception table:
    //   from	to	target	type
    //   10	13	659	finally
    //   14	17	659	finally
    //   35	39	659	finally
    //   39	42	659	finally
    //   49	52	659	finally
    //   54	59	659	finally
    //   66	69	659	finally
    //   76	79	659	finally
    //   81	86	659	finally
    //   88	93	659	finally
    //   100	103	659	finally
    //   104	107	659	finally
    //   114	117	659	finally
    //   119	122	659	finally
    //   126	131	659	finally
    //   138	141	659	finally
    //   143	148	659	finally
    //   155	158	659	finally
    //   159	162	659	finally
    //   177	180	659	finally
    //   182	187	659	finally
    //   194	197	659	finally
    //   199	204	659	finally
    //   218	221	659	finally
    //   233	236	659	finally
    //   238	243	659	finally
    //   250	254	659	finally
    //   254	257	659	finally
    //   258	261	659	finally
    //   263	268	659	finally
    //   270	275	659	finally
    //   277	282	659	finally
    //   294	297	659	finally
    //   298	301	659	finally
    //   303	308	659	finally
    //   310	313	659	finally
    //   315	320	659	finally
    //   327	330	659	finally
    //   332	337	659	finally
    //   344	347	659	finally
    //   359	363	659	finally
    //   363	368	659	finally
    //   368	373	659	finally
    //   373	376	659	finally
    //   377	381	659	finally
    //   381	385	659	finally
    //   385	388	659	finally
    //   392	396	659	finally
    //   398	403	659	finally
    //   403	406	659	finally
    //   412	417	659	finally
    //   424	427	659	finally
    //   429	434	659	finally
    //   441	444	659	finally
    //   446	451	659	finally
    //   465	470	659	finally
    //   471	474	659	finally
    //   478	482	659	finally
    //   489	493	659	finally
    //   493	496	659	finally
    //   503	508	659	finally
    //   518	522	659	finally
    //   535	538	659	finally
    //   545	550	659	finally
    //   551	555	659	finally
    //   566	570	659	finally
    //   576	580	659	finally
    //   580	583	659	finally
    //   590	593	659	finally
    //   600	603	659	finally
    //   610	613	659	finally
    //   614	617	659	finally
    //   641	644	659	finally
    //   645	648	659	finally
    //   650	654	659	finally
    //   655	658	659	finally
  }
}
