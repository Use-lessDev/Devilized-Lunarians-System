package lunarians;

import arc.Events;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Time;
import lunarians.data.HealSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.core.GameState;
import mindustry.entities.EntityGroup;
import mindustry.entities.units.UnitController;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;

public class AutoHeal {
  private static final float SCAN = 0.25F;
  
  private static int count;
  
  private static boolean errorLogged;
  
  private static final IntSet healing;
  
  private static float timer;
  
  private static final Vec2 tmp;
  
  static {
    IntSet intSet = new IntSet();
    this();
    healing = intSet;
    Vec2 vec2 = new Vec2();
    this();
    tmp = vec2;
  }
  
  public static int count() {
    return count;
  }
  
  private static int countWallNeighbors(int paramInt1, int paramInt2) {
    boolean bool = false;
    for (int i = -1;; i++) {
      byte b = 1;
      if (i <= b) {
        int j = -1;
        while (true)
          j++; 
        continue;
      } 
      return bool;
    } 
  }
  
  private static Vec2 findFloorStand(Building paramBuilding, Unit paramUnit, float paramFloat) {
    // Byte code:
    //   0: aload_0
    //   1: astore_3
    //   2: aload_1
    //   3: astore #4
    //   5: aload_0
    //   6: invokevirtual tileX : ()I
    //   9: istore #5
    //   11: aload_0
    //   12: invokevirtual tileY : ()I
    //   15: istore #6
    //   17: ldc 1090519040
    //   19: istore #7
    //   21: ldc 8.0
    //   23: fstore #8
    //   25: fload_2
    //   26: fload #8
    //   28: fdiv
    //   29: fstore #9
    //   31: fload #9
    //   33: invokestatic ceil : (F)I
    //   36: istore #10
    //   38: iconst_2
    //   39: iload #10
    //   41: invokestatic max : (II)I
    //   44: istore #10
    //   46: aload_0
    //   47: getfield block : Lmindustry/world/Block;
    //   50: astore #11
    //   52: aload #11
    //   54: getfield size : I
    //   57: i2f
    //   58: fstore #12
    //   60: ldc 1073741824
    //   62: istore #13
    //   64: ldc 2.0
    //   66: fstore #14
    //   68: fload #12
    //   70: fload #14
    //   72: fdiv
    //   73: fstore #12
    //   75: fload #12
    //   77: invokestatic floor : (F)I
    //   80: istore #15
    //   82: iconst_1
    //   83: istore #16
    //   85: iload #15
    //   87: iload #16
    //   89: iadd
    //   90: istore #15
    //   92: iload #16
    //   94: iload #15
    //   96: invokestatic max : (II)I
    //   99: istore #15
    //   101: iconst_0
    //   102: istore #16
    //   104: aconst_null
    //   105: astore #17
    //   107: ldc 3.4028235E38
    //   109: fstore #18
    //   111: iload #15
    //   113: istore #19
    //   115: iload #19
    //   117: iload #10
    //   119: if_icmpgt -> 534
    //   122: iload #19
    //   124: ineg
    //   125: istore #20
    //   127: iload #20
    //   129: istore #21
    //   131: iload #21
    //   133: iload #19
    //   135: if_icmpgt -> 476
    //   138: iload #20
    //   140: istore #7
    //   142: ldc 8.0
    //   144: fstore #22
    //   146: iload #7
    //   148: iload #19
    //   150: if_icmpgt -> 467
    //   153: ldc 2.0
    //   155: fstore #23
    //   157: iload #21
    //   159: invokestatic abs : (I)I
    //   162: istore #13
    //   164: ldc 0.9
    //   166: fstore #24
    //   168: iload #7
    //   170: invokestatic abs : (I)I
    //   173: istore #25
    //   175: iload #13
    //   177: iload #25
    //   179: invokestatic max : (II)I
    //   182: istore #13
    //   184: iload #13
    //   186: iload #19
    //   188: if_icmpeq -> 198
    //   191: iload #19
    //   193: iload #15
    //   195: if_icmpgt -> 240
    //   198: iload #5
    //   200: iload #21
    //   202: iadd
    //   203: istore #13
    //   205: iload #6
    //   207: iload #7
    //   209: iadd
    //   210: istore #25
    //   212: ldc 4.0
    //   214: fstore #26
    //   216: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   219: getfield tiles : Lmindustry/world/Tiles;
    //   222: astore #27
    //   224: aload #27
    //   226: iload #13
    //   228: iload #25
    //   230: invokevirtual in : (II)Z
    //   233: istore #28
    //   235: iload #28
    //   237: ifne -> 251
    //   240: iload #5
    //   242: istore #29
    //   244: iload #6
    //   246: istore #30
    //   248: goto -> 450
    //   251: iload #13
    //   253: i2f
    //   254: fload #22
    //   256: fmul
    //   257: fload #26
    //   259: fadd
    //   260: fstore #31
    //   262: ldc 40.0
    //   264: fstore #32
    //   266: iload #25
    //   268: i2f
    //   269: fload #22
    //   271: fmul
    //   272: fload #26
    //   274: fadd
    //   275: fstore #33
    //   277: iload #5
    //   279: istore #29
    //   281: aload_3
    //   282: getfield x : F
    //   285: fstore #34
    //   287: iload #6
    //   289: istore #30
    //   291: aload_3
    //   292: getfield y : F
    //   295: fstore #35
    //   297: fload #31
    //   299: fload #33
    //   301: fload #34
    //   303: fload #35
    //   305: invokestatic dst : (FFFF)F
    //   308: fstore #34
    //   310: fload_2
    //   311: fload #24
    //   313: fmul
    //   314: fstore #35
    //   316: fload #34
    //   318: fload #35
    //   320: fcmpl
    //   321: istore #5
    //   323: iload #5
    //   325: ifgt -> 450
    //   328: iload #13
    //   330: iload #25
    //   332: aload #4
    //   334: invokestatic isGoodFloor : (IILmindustry/gen/Unit;)Z
    //   337: istore #5
    //   339: iload #5
    //   341: ifeq -> 450
    //   344: iload #13
    //   346: iload #25
    //   348: invokestatic countWallNeighbors : (II)I
    //   351: istore #5
    //   353: iload #5
    //   355: i2f
    //   356: fload #32
    //   358: fmul
    //   359: fstore #34
    //   361: aload #4
    //   363: fload #31
    //   365: fload #33
    //   367: invokevirtual dst : (FF)F
    //   370: fstore #35
    //   372: ldc 1028443341
    //   374: istore #13
    //   376: ldc 0.05
    //   378: fstore #14
    //   380: fload #35
    //   382: fload #14
    //   384: fmul
    //   385: fstore #35
    //   387: fload #34
    //   389: fload #35
    //   391: fadd
    //   392: fstore #34
    //   394: iload #19
    //   396: i2f
    //   397: fload #23
    //   399: fmul
    //   400: fstore #35
    //   402: fload #34
    //   404: fload #35
    //   406: fadd
    //   407: fstore #34
    //   409: fload #34
    //   411: fload #18
    //   413: fcmpg
    //   414: istore #6
    //   416: iload #6
    //   418: ifge -> 450
    //   421: aload #17
    //   423: ifnonnull -> 436
    //   426: new arc/math/geom/Vec2
    //   429: astore #17
    //   431: aload #17
    //   433: invokespecial <init> : ()V
    //   436: aload #17
    //   438: fload #31
    //   440: fload #33
    //   442: invokevirtual set : (FF)Larc/math/geom/Vec2;
    //   445: pop
    //   446: fload #34
    //   448: fstore #18
    //   450: iload #7
    //   452: iconst_1
    //   453: iadd
    //   454: istore #7
    //   456: iload #29
    //   458: istore #5
    //   460: iload #30
    //   462: istore #6
    //   464: goto -> 146
    //   467: iload #21
    //   469: iconst_1
    //   470: iadd
    //   471: istore #21
    //   473: goto -> 131
    //   476: iload #5
    //   478: istore #29
    //   480: iload #6
    //   482: istore #30
    //   484: ldc 8.0
    //   486: fstore #22
    //   488: ldc 0.9
    //   490: fstore #24
    //   492: ldc 4.0
    //   494: fstore #26
    //   496: ldc 40.0
    //   498: fstore #32
    //   500: aload #17
    //   502: ifnull -> 517
    //   505: fload #18
    //   507: fload #32
    //   509: fcmpg
    //   510: istore #5
    //   512: iload #5
    //   514: iflt -> 546
    //   517: iload #19
    //   519: iconst_1
    //   520: iadd
    //   521: istore #19
    //   523: iload #29
    //   525: istore #5
    //   527: iload #30
    //   529: istore #6
    //   531: goto -> 115
    //   534: ldc 8.0
    //   536: fstore #22
    //   538: ldc 0.9
    //   540: fstore #24
    //   542: ldc 4.0
    //   544: fstore #26
    //   546: aload #17
    //   548: ifnonnull -> 853
    //   551: new arc/math/geom/Vec2
    //   554: astore #36
    //   556: aload #4
    //   558: getfield x : F
    //   561: fstore #35
    //   563: aload_3
    //   564: getfield x : F
    //   567: fstore #8
    //   569: fload #35
    //   571: fload #8
    //   573: fsub
    //   574: fstore #35
    //   576: aload #4
    //   578: getfield y : F
    //   581: fstore #8
    //   583: aload_3
    //   584: getfield y : F
    //   587: fstore #9
    //   589: fload #8
    //   591: fload #9
    //   593: fsub
    //   594: fstore #8
    //   596: aload #36
    //   598: fload #35
    //   600: fload #8
    //   602: invokespecial <init> : (FF)V
    //   605: aload #36
    //   607: invokevirtual len : ()F
    //   610: fstore #35
    //   612: ldc 1036831949
    //   614: istore #7
    //   616: ldc 0.1
    //   618: fstore #8
    //   620: fload #35
    //   622: fload #8
    //   624: fcmpg
    //   625: istore #6
    //   627: iload #6
    //   629: ifge -> 655
    //   632: ldc 1065353216
    //   634: istore #6
    //   636: ldc 1.0
    //   638: fstore #35
    //   640: iconst_0
    //   641: istore #7
    //   643: fconst_0
    //   644: fstore #8
    //   646: aload #36
    //   648: fload #35
    //   650: fconst_0
    //   651: invokevirtual set : (FF)Larc/math/geom/Vec2;
    //   654: pop
    //   655: aload #36
    //   657: invokevirtual nor : ()Larc/math/geom/Vec2;
    //   660: pop
    //   661: aload_3
    //   662: getfield block : Lmindustry/world/Block;
    //   665: astore #37
    //   667: aload #37
    //   669: getfield size : I
    //   672: istore #6
    //   674: iload #6
    //   676: i2f
    //   677: fload #22
    //   679: fmul
    //   680: fstore #35
    //   682: ldc 1098907648
    //   684: istore #7
    //   686: ldc 16.0
    //   688: fstore #8
    //   690: fload #8
    //   692: fload #35
    //   694: invokestatic max : (FF)F
    //   697: fstore #35
    //   699: fload_2
    //   700: fload #24
    //   702: fmul
    //   703: fstore #38
    //   705: fload #35
    //   707: fload #38
    //   709: fcmpg
    //   710: istore #7
    //   712: iload #7
    //   714: ifgt -> 853
    //   717: aload_3
    //   718: getfield x : F
    //   721: fstore #8
    //   723: aload #36
    //   725: getfield x : F
    //   728: fload #35
    //   730: fmul
    //   731: fstore #9
    //   733: fload #8
    //   735: fload #9
    //   737: fadd
    //   738: fstore #8
    //   740: aload_3
    //   741: getfield y : F
    //   744: fstore #9
    //   746: aload #36
    //   748: getfield y : F
    //   751: fload #35
    //   753: fmul
    //   754: fstore #12
    //   756: fload #9
    //   758: fload #12
    //   760: fadd
    //   761: fstore #9
    //   763: fload #8
    //   765: fload #22
    //   767: fdiv
    //   768: fstore #8
    //   770: fload #8
    //   772: invokestatic floor : (F)I
    //   775: istore #7
    //   777: fload #9
    //   779: fload #22
    //   781: fdiv
    //   782: fstore #9
    //   784: fload #9
    //   786: invokestatic floor : (F)I
    //   789: istore #10
    //   791: iload #7
    //   793: iload #10
    //   795: aload #4
    //   797: invokestatic isGoodFloor : (IILmindustry/gen/Unit;)Z
    //   800: istore #15
    //   802: iload #15
    //   804: ifeq -> 843
    //   807: new arc/math/geom/Vec2
    //   810: astore_3
    //   811: iload #7
    //   813: i2f
    //   814: fload #22
    //   816: fmul
    //   817: fload #26
    //   819: fadd
    //   820: fstore #39
    //   822: iload #10
    //   824: i2f
    //   825: fload #22
    //   827: fmul
    //   828: fload #26
    //   830: fadd
    //   831: fstore #34
    //   833: aload_3
    //   834: fload #39
    //   836: fload #34
    //   838: invokespecial <init> : (FF)V
    //   841: aload_3
    //   842: areturn
    //   843: fload #35
    //   845: fload #22
    //   847: fadd
    //   848: fstore #35
    //   850: goto -> 699
    //   853: aload #17
    //   855: areturn
  }
  
  public static Building findNearestHeal(Unit paramUnit) {
    EntityGroup entityGroup = Groups.build;
    int i = entityGroup.size();
    Building building = null;
    float f = Float.MAX_VALUE;
    int j;
    for (j = 0; j < i; j++) {
      Building building1 = (Building)Groups.build.index(j);
      if (building1 != null) {
        boolean bool = building1.isValid();
        if (bool) {
          Team team1 = building1.team;
          Team team2 = paramUnit.team;
          if (team1 == team2) {
            float f1 = healRadius(building1);
            float f2 = 0.0F;
            team2 = null;
            int k = f1 cmp 0.0F;
            if (k > 0) {
              f2 = paramUnit.dst((Position)building1);
              float f3 = 0.25F;
              f1 *= f3;
              f2 -= f1;
              int m = f2 cmp f;
              if (m < 0) {
                building = building1;
                f = f2;
              } 
            } 
          } 
        } 
      } 
    } 
    return building;
  }
  
  private static float healRadius(Building paramBuilding) {
    // Byte code:
    //   0: aload_0
    //   1: getfield block : Lmindustry/world/Block;
    //   4: astore_1
    //   5: aload_1
    //   6: instanceof mindustry/world/blocks/units/RepairTurret
    //   9: istore_2
    //   10: iload_2
    //   11: ifeq -> 25
    //   14: aload_0
    //   15: getfield block : Lmindustry/world/Block;
    //   18: checkcast mindustry/world/blocks/units/RepairTurret
    //   21: getfield repairRadius : F
    //   24: freturn
    //   25: aload_0
    //   26: getfield block : Lmindustry/world/Block;
    //   29: astore_1
    //   30: aload_1
    //   31: instanceof mindustry/world/blocks/units/RepairTower
    //   34: istore_2
    //   35: iload_2
    //   36: ifeq -> 50
    //   39: aload_0
    //   40: getfield block : Lmindustry/world/Block;
    //   43: checkcast mindustry/world/blocks/units/RepairTower
    //   46: getfield range : F
    //   49: freturn
    //   50: aload_0
    //   51: getfield block : Lmindustry/world/Block;
    //   54: astore_1
    //   55: aload_1
    //   56: instanceof mindustry/world/blocks/defense/MendProjector
    //   59: istore_2
    //   60: iload_2
    //   61: ifeq -> 75
    //   64: aload_0
    //   65: getfield block : Lmindustry/world/Block;
    //   68: checkcast mindustry/world/blocks/defense/MendProjector
    //   71: getfield range : F
    //   74: freturn
    //   75: aload_0
    //   76: instanceof mindustry/logic/Ranged
    //   79: istore_2
    //   80: iload_2
    //   81: ifeq -> 181
    //   84: aload_0
    //   85: getfield block : Lmindustry/world/Block;
    //   88: getfield name : Ljava/lang/String;
    //   91: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   94: astore_1
    //   95: ldc 'repair'
    //   97: astore_3
    //   98: aload_1
    //   99: aload_3
    //   100: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   103: istore #4
    //   105: iload #4
    //   107: ifne -> 153
    //   110: ldc 'mend'
    //   112: astore_3
    //   113: aload_1
    //   114: aload_3
    //   115: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   118: istore #4
    //   120: iload #4
    //   122: ifne -> 153
    //   125: ldc 'heal'
    //   127: astore_3
    //   128: aload_1
    //   129: aload_3
    //   130: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   133: istore #4
    //   135: iload #4
    //   137: ifne -> 153
    //   140: ldc 'regen'
    //   142: astore_3
    //   143: aload_1
    //   144: aload_3
    //   145: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   148: istore_2
    //   149: iload_2
    //   150: ifeq -> 181
    //   153: aload_0
    //   154: checkcast mindustry/logic/Ranged
    //   157: astore_0
    //   158: aload_0
    //   159: invokeinterface range : ()F
    //   164: fstore #5
    //   166: fload #5
    //   168: fconst_0
    //   169: fcmpl
    //   170: istore_2
    //   171: iload_2
    //   172: ifle -> 178
    //   175: fload #5
    //   177: freturn
    //   178: ldc 40.0
    //   180: freturn
    //   181: fconst_0
    //   182: freturn
  }
  
  public static float hpFrac(Unit paramUnit) {
    float f1 = paramUnit.health;
    float f2 = paramUnit.maxHealth;
    f2 = Math.max(1.0F, f2);
    return f1 / f2;
  }
  
  public static void init() {
    boolean bool = Vars.headless;
    if (bool)
      return; 
    EventType.Trigger trigger = EventType.Trigger.update;
    AutoHeal$1 autoHeal$1 = new AutoHeal$1();
    this();
    Events.run(trigger, autoHeal$1);
    AutoHeal$2 autoHeal$2 = new AutoHeal$2();
    this();
    Events.on(EventType.WorldLoadEvent.class, autoHeal$2);
  }
  
  private static boolean isGoodFloor(int paramInt1, int paramInt2, Unit paramUnit) {
    // Byte code:
    //   0: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   3: getfield tiles : Lmindustry/world/Tiles;
    //   6: astore_3
    //   7: aload_3
    //   8: iload_0
    //   9: iload_1
    //   10: invokevirtual in : (II)Z
    //   13: istore #4
    //   15: iload #4
    //   17: ifne -> 22
    //   20: iconst_0
    //   21: ireturn
    //   22: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   25: astore_3
    //   26: aload_3
    //   27: iload_0
    //   28: iload_1
    //   29: invokevirtual tile : (II)Lmindustry/world/Tile;
    //   32: astore #5
    //   34: aload #5
    //   36: ifnonnull -> 41
    //   39: iconst_0
    //   40: ireturn
    //   41: aload #5
    //   43: getfield build : Lmindustry/gen/Building;
    //   46: astore #6
    //   48: aload #6
    //   50: ifnull -> 55
    //   53: iconst_0
    //   54: ireturn
    //   55: aload #5
    //   57: invokevirtual passable : ()Z
    //   60: istore_1
    //   61: iload_1
    //   62: ifne -> 67
    //   65: iconst_0
    //   66: ireturn
    //   67: aload_2
    //   68: invokevirtual isFlying : ()Z
    //   71: istore_1
    //   72: iload_1
    //   73: ifne -> 88
    //   76: aload #5
    //   78: invokevirtual solid : ()Z
    //   81: istore_1
    //   82: iload_1
    //   83: ifeq -> 88
    //   86: iconst_0
    //   87: ireturn
    //   88: aload #5
    //   90: invokevirtual floor : ()Lmindustry/world/blocks/environment/Floor;
    //   93: astore #6
    //   95: aload #6
    //   97: ifnonnull -> 102
    //   100: iconst_0
    //   101: ireturn
    //   102: aload_2
    //   103: invokevirtual isFlying : ()Z
    //   106: istore_1
    //   107: iload_1
    //   108: ifne -> 185
    //   111: aload_2
    //   112: getfield type : Lmindustry/type/UnitType;
    //   115: astore #6
    //   117: aload #6
    //   119: ifnull -> 138
    //   122: aload_2
    //   123: getfield type : Lmindustry/type/UnitType;
    //   126: astore #6
    //   128: aload #6
    //   130: getfield naval : Z
    //   133: istore_1
    //   134: iload_1
    //   135: ifne -> 185
    //   138: aload #5
    //   140: invokevirtual floor : ()Lmindustry/world/blocks/environment/Floor;
    //   143: astore #6
    //   145: aload #6
    //   147: getfield isLiquid : Z
    //   150: istore_1
    //   151: iload_1
    //   152: ifeq -> 185
    //   155: aload #5
    //   157: invokevirtual floor : ()Lmindustry/world/blocks/environment/Floor;
    //   160: astore #5
    //   162: aload #5
    //   164: getfield drownTime : F
    //   167: fstore #7
    //   169: iconst_0
    //   170: istore_1
    //   171: aconst_null
    //   172: astore #6
    //   174: fload #7
    //   176: fconst_0
    //   177: fcmpl
    //   178: istore_0
    //   179: iload_0
    //   180: ifle -> 185
    //   183: iconst_0
    //   184: ireturn
    //   185: iconst_1
    //   186: ireturn
  }
  
  public static boolean isHealing(int paramInt) {
    return healing.contains(paramInt);
  }
  
  public static boolean needsHeal(Unit paramUnit) {
    float f1 = hpFrac(paramUnit);
    float f2 = LunariansSettings.autoHealPercent;
    float f3 = 100.0F;
    f2 /= f3;
    int i = f1 cmp f2;
    return (i < 0);
  }
  
  public static void reset() {
    timer = 0.0F;
    count = 0;
    healing.clear();
  }
  
  private static void scan() {
    Unit unit;
    Seq seq = LunariansSettings.healSlots;
    boolean bool = seq.isEmpty();
    EntityGroup entityGroup = Groups.unit;
    int i = entityGroup.size();
    int j = 0;
    if (bool) {
      while (j < i) {
        unit = (Unit)Groups.unit.index(j);
        if (unit != null) {
          bool = unit.isValid();
          if (bool) {
            Team team1 = unit.team;
            Team team2 = Vars.player.team();
            if (team1 == team2) {
              UnitController unitController = unit.controller();
              bool = unitController instanceof CommandAI;
              if (bool) {
                boolean bool1 = tryHeal(unit);
                if (bool1) {
                  int k = count + 1;
                  count = k;
                } 
              } 
            } 
          } 
        } 
        j++;
      } 
    } else {
      int k;
      bool = false;
      Object object = null;
      while (true) {
        int m = ((Seq)unit).size;
        if (k < m) {
          HealSlot healSlot = (HealSlot)unit.get(k);
          boolean bool1 = healSlot.ready();
          if (bool1) {
            int n;
            bool1 = false;
            boolean bool2 = false;
            while (true)
              n = bool1 + 1; 
          } 
          continue;
        } 
        break;
        k = bool + 1;
      } 
    } 
  }
  
  public static boolean tryHeal(Unit paramUnit) {
    int i = 0;
    float f = 0.0F;
    IntSet intSet = null;
    if (paramUnit != null) {
      boolean bool = paramUnit.isValid();
      if (bool) {
        Player player = Vars.player;
        if (player != null) {
          bool = needsHeal(paramUnit);
          if (!bool)
            return false; 
          UnitController unitController = paramUnit.controller();
          bool = unitController instanceof CommandAI;
          if (!bool)
            return false; 
          Building building = findNearestHeal(paramUnit);
          if (building == null)
            return false; 
          float f1 = healRadius(building);
          float f2 = 0.0F;
          UnitCommand unitCommand2 = null;
          int j = f1 cmp 0.0F;
          if (j <= 0) {
            int n = 1109393408;
            f1 = 40.0F;
          } 
          f2 = paramUnit.dst((Position)building);
          int k = 1063675494;
          float f3 = 0.9F * f1;
          boolean bool1 = true;
          j = f2 cmp f3;
          if (j <= 0) {
            j = paramUnit.tileX();
            k = paramUnit.tileY();
            boolean bool2 = isGoodFloor(j, k, paramUnit);
            if (bool2) {
              intSet = healing;
              int n = paramUnit.id;
              intSet.add(n);
              return bool1;
            } 
          } 
          Vec2 vec21 = findFloorStand(building, paramUnit, f1);
          if (vec21 == null)
            return false; 
          UnitCommand unitCommand1 = ((CommandAI)paramUnit.controller()).command;
          UnitCommand unitCommand3 = UnitCommand.moveCommand;
          if (unitCommand1 != unitCommand3) {
            Player player2 = Vars.player;
            int n = paramUnit.id;
            int[] arrayOfInt1 = { n };
            unitCommand2 = UnitCommand.moveCommand;
            Call.setUnitCommand(player2, arrayOfInt1, unitCommand2);
          } 
          Player player1 = Vars.player;
          i = paramUnit.id;
          int[] arrayOfInt = { i };
          Vec2 vec22 = new Vec2();
          f = vec21.x;
          float f4 = vec21.y;
          this(f, f4);
          Call.commandUnits(player1, arrayOfInt, null, null, vec22, false, true);
          IntSet intSet1 = healing;
          int m = paramUnit.id;
          intSet1.add(m);
          return bool1;
        } 
      } 
    } 
    return false;
  }
  
  private static void update() {
    boolean bool = LunariansSettings.enableAutoHeal;
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
            int j = 1048576000;
            f2 = LunariansSettings.actionInterval(0.25F);
            int i = f1 cmp f2;
            if (i >= 0) {
              timer = 0.0F;
              healing.clear();
              i = 0;
              f1 = 0.0F;
              player = null;
              count = 0;
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
    healing.clear();
  }
}
