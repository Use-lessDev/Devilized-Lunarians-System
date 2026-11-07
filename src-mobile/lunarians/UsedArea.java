package lunarians;

import arc.Core;
import arc.Events;
import arc.graphics.Camera;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.geom.Vec2;
import java.lang.reflect.Field;
import java.util.HashMap;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.core.World;
import mindustry.game.EventType;

public class UsedArea {
  private static final float DELAY = 420.0F;
  
  private static float ar;
  
  private static int ax;
  
  private static int ay;
  
  private static boolean hasArea;
  
  private static float next;
  
  private static boolean[] red;
  
  private static boolean registered;
  
  private static final HashMap seen;
  
  static {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    this();
    seen = hashMap;
  }
  
  private static void draw() {
    boolean[] arrayOfBoolean = red;
    if (arrayOfBoolean != null) {
      boolean bool = hasArea;
      if (bool) {
        GameState gameState = Vars.state;
        if (gameState != null) {
          gameState = Vars.state;
          bool = gameState.isGame();
          if (bool) {
            World world1 = Vars.world;
            int i = world1.width();
            Vec2 vec21 = Core.camera.position;
            float f1 = vec21.x;
            Vec2 vec22 = Core.camera.position;
            float f2 = vec22.y;
            Camera camera = Core.camera;
            float f3 = camera.width;
            float f4 = 2.0F;
            f3 /= f4;
            float f5 = 16.0F;
            f3 += f5;
            float f6 = Core.camera.height / f4 + f5;
            f4 = f1 - f3;
            f5 = 8.0F;
            f4 /= f5;
            int j = (int)f4;
            j = Math.max(0, j);
            int k = i + -1;
            f1 = (f1 + f3) / f5;
            int m = (int)f1;
            m = Math.min(k, m);
            f3 = (f2 - f6) / f5;
            int n = (int)f3;
            n = Math.max(0, n);
            World world2 = Vars.world;
            int i1 = world2.height() + -1;
            f2 = (f2 + f6) / f5;
            int i2 = (int)f2;
            i2 = Math.min(i1, i2);
            int i3 = 1122893824;
            f6 = 119.0F;
            Draw.z(f6);
            Color color = Color.scarlet;
            i1 = 1055286886;
            float f7 = 0.45F;
            Draw.color(color, f7);
            while (n <= i2) {
              for (i3 = j; i3 <= m; i3++) {
                boolean[] arrayOfBoolean1 = red;
                k = n * i + i3;
                boolean bool1 = arrayOfBoolean1[k];
                if (bool1) {
                  f7 = i3 * f5;
                  float f = n * f5;
                  Fill.rect(f7, f, f5, f5);
                } 
              } 
              n++;
            } 
            Draw.reset();
          } 
        } 
      } 
    } 
  }
  
  private static Field fld(String paramString) {
    Field field = AutoSet.class.getDeclaredField(paramString);
    field.setAccessible(true);
    return field;
  }
  
  public static void init() {
    boolean bool = registered;
    if (bool)
      return; 
    registered = true;
    EventType.Trigger trigger = EventType.Trigger.update;
    UsedArea$0 usedArea$0 = new UsedArea$0();
    this();
    Events.run(trigger, usedArea$0);
    trigger = EventType.Trigger.draw;
    UsedArea$1 usedArea$1 = new UsedArea$1();
    this();
    Events.run(trigger, usedArea$1);
    UsedArea$2 usedArea$2 = new UsedArea$2();
    this();
    Events.on(EventType.WorldLoadEvent.class, usedArea$2);
  }
  
  private static void reset() {
    hasArea = false;
    seen.clear();
    red = null;
  }
  
  private static void update() {
    // Byte code:
    //   0: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   3: astore_0
    //   4: aload_0
    //   5: ifnull -> 707
    //   8: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   11: astore_0
    //   12: aload_0
    //   13: invokevirtual isGame : ()Z
    //   16: istore_1
    //   17: iload_1
    //   18: ifeq -> 707
    //   21: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   24: astore_0
    //   25: aload_0
    //   26: ifnull -> 707
    //   29: invokestatic isRunning : ()Z
    //   32: istore_1
    //   33: iconst_1
    //   34: istore_2
    //   35: iload_1
    //   36: ifeq -> 181
    //   39: ldc 'cx'
    //   41: astore_0
    //   42: aload_0
    //   43: invokestatic fld : (Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   46: astore_0
    //   47: iconst_0
    //   48: istore_3
    //   49: fconst_0
    //   50: fstore #4
    //   52: aconst_null
    //   53: astore #5
    //   55: aload_0
    //   56: aconst_null
    //   57: invokevirtual getInt : (Ljava/lang/Object;)I
    //   60: istore_1
    //   61: ldc 'cy'
    //   63: astore #6
    //   65: aload #6
    //   67: invokestatic fld : (Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   70: astore #6
    //   72: aload #6
    //   74: aconst_null
    //   75: invokevirtual getInt : (Ljava/lang/Object;)I
    //   78: istore #7
    //   80: ldc 'radius'
    //   82: astore #8
    //   84: aload #8
    //   86: invokestatic fld : (Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   89: astore #8
    //   91: aload #8
    //   93: aconst_null
    //   94: invokevirtual getFloat : (Ljava/lang/Object;)F
    //   97: fstore #9
    //   99: getstatic lunarians/UsedArea.hasArea : Z
    //   102: istore #10
    //   104: iload #10
    //   106: ifeq -> 149
    //   109: getstatic lunarians/UsedArea.ax : I
    //   112: istore #10
    //   114: iload_1
    //   115: iload #10
    //   117: if_icmpne -> 149
    //   120: getstatic lunarians/UsedArea.ay : I
    //   123: istore #10
    //   125: iload #7
    //   127: iload #10
    //   129: if_icmpne -> 149
    //   132: getstatic lunarians/UsedArea.ar : F
    //   135: fstore #11
    //   137: fload #9
    //   139: fload #11
    //   141: fcmpl
    //   142: istore #10
    //   144: iload #10
    //   146: ifeq -> 163
    //   149: getstatic lunarians/UsedArea.seen : Ljava/util/HashMap;
    //   152: astore #12
    //   154: aload #12
    //   156: invokevirtual clear : ()V
    //   159: aconst_null
    //   160: putstatic lunarians/UsedArea.red : [Z
    //   163: iload_1
    //   164: putstatic lunarians/UsedArea.ax : I
    //   167: iload #7
    //   169: putstatic lunarians/UsedArea.ay : I
    //   172: fload #9
    //   174: putstatic lunarians/UsedArea.ar : F
    //   177: iload_2
    //   178: putstatic lunarians/UsedArea.hasArea : Z
    //   181: getstatic lunarians/UsedArea.hasArea : Z
    //   184: istore_1
    //   185: iload_1
    //   186: ifeq -> 707
    //   189: getstatic arc/util/Time.time : F
    //   192: fstore #13
    //   194: getstatic lunarians/UsedArea.next : F
    //   197: fstore #4
    //   199: fload #13
    //   201: fload #4
    //   203: fcmpg
    //   204: istore_1
    //   205: iload_1
    //   206: iflt -> 707
    //   209: getstatic arc/util/Time.time : F
    //   212: fstore #13
    //   214: ldc 1114636288
    //   216: istore_3
    //   217: ldc 60.0
    //   219: fstore #4
    //   221: fload #13
    //   223: fload #4
    //   225: fadd
    //   226: fstore #13
    //   228: fload #13
    //   230: putstatic lunarians/UsedArea.next : F
    //   233: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   236: astore_0
    //   237: aload_0
    //   238: invokevirtual width : ()I
    //   241: istore_1
    //   242: getstatic lunarians/UsedArea.red : [Z
    //   245: astore #5
    //   247: aload #5
    //   249: ifnull -> 276
    //   252: aload #5
    //   254: arraylength
    //   255: istore_3
    //   256: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   259: astore #6
    //   261: aload #6
    //   263: invokevirtual height : ()I
    //   266: iload_1
    //   267: imul
    //   268: istore #7
    //   270: iload_3
    //   271: iload #7
    //   273: if_icmpeq -> 299
    //   276: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   279: astore #5
    //   281: aload #5
    //   283: invokevirtual height : ()I
    //   286: iload_1
    //   287: imul
    //   288: istore_3
    //   289: iload_3
    //   290: newarray boolean
    //   292: astore #5
    //   294: aload #5
    //   296: putstatic lunarians/UsedArea.red : [Z
    //   299: getstatic lunarians/UsedArea.ar : F
    //   302: fstore #4
    //   304: fload #4
    //   306: f2d
    //   307: dstore #14
    //   309: dload #14
    //   311: invokestatic ceil : (D)D
    //   314: dstore #14
    //   316: dload #14
    //   318: d2i
    //   319: istore_3
    //   320: getstatic lunarians/UsedArea.ay : I
    //   323: iload_3
    //   324: isub
    //   325: istore #7
    //   327: getstatic lunarians/UsedArea.ay : I
    //   330: iload_3
    //   331: iadd
    //   332: istore #16
    //   334: iload #7
    //   336: iload #16
    //   338: if_icmpgt -> 707
    //   341: getstatic lunarians/UsedArea.ax : I
    //   344: iload_3
    //   345: isub
    //   346: istore #16
    //   348: getstatic lunarians/UsedArea.ax : I
    //   351: iload_3
    //   352: iadd
    //   353: istore #10
    //   355: iload #16
    //   357: iload #10
    //   359: if_icmpgt -> 683
    //   362: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   365: astore #12
    //   367: aload #12
    //   369: getfield tiles : Lmindustry/world/Tiles;
    //   372: astore #12
    //   374: aload #12
    //   376: iload #16
    //   378: iload #7
    //   380: invokevirtual in : (II)Z
    //   383: istore #10
    //   385: iload #10
    //   387: ifeq -> 674
    //   390: iload #16
    //   392: i2f
    //   393: fstore #11
    //   395: iload #7
    //   397: i2f
    //   398: fstore #17
    //   400: getstatic lunarians/UsedArea.ax : I
    //   403: istore #18
    //   405: iload #18
    //   407: i2f
    //   408: fstore #19
    //   410: getstatic lunarians/UsedArea.ay : I
    //   413: istore #20
    //   415: iload #20
    //   417: i2f
    //   418: fstore #21
    //   420: fload #11
    //   422: fload #17
    //   424: fload #19
    //   426: fload #21
    //   428: invokestatic dst : (FFFF)F
    //   431: fstore #11
    //   433: getstatic lunarians/UsedArea.ar : F
    //   436: fstore #17
    //   438: fload #11
    //   440: fload #17
    //   442: fcmpl
    //   443: istore #10
    //   445: iload #10
    //   447: ifgt -> 674
    //   450: iload #7
    //   452: iload_1
    //   453: imul
    //   454: iload #16
    //   456: iadd
    //   457: istore #10
    //   459: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   462: astore #22
    //   464: aload #22
    //   466: iload #16
    //   468: iload #7
    //   470: invokevirtual tile : (II)Lmindustry/world/Tile;
    //   473: astore #22
    //   475: aload #22
    //   477: ifnull -> 611
    //   480: aload #22
    //   482: getfield build : Lmindustry/gen/Building;
    //   485: astore #22
    //   487: aload #22
    //   489: ifnull -> 611
    //   492: getstatic lunarians/UsedArea.seen : Ljava/util/HashMap;
    //   495: astore #22
    //   497: iload #10
    //   499: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   502: astore #23
    //   504: aload #22
    //   506: aload #23
    //   508: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   511: astore #23
    //   513: aload #23
    //   515: checkcast java/lang/Float
    //   518: astore #23
    //   520: aload #23
    //   522: ifnonnull -> 557
    //   525: iload #10
    //   527: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   530: astore #12
    //   532: getstatic arc/util/Time.time : F
    //   535: fstore #19
    //   537: fload #19
    //   539: invokestatic valueOf : (F)Ljava/lang/Float;
    //   542: astore #23
    //   544: aload #22
    //   546: aload #12
    //   548: aload #23
    //   550: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   553: pop
    //   554: goto -> 674
    //   557: getstatic arc/util/Time.time : F
    //   560: fstore #17
    //   562: aload #23
    //   564: invokevirtual floatValue : ()F
    //   567: fstore #19
    //   569: fload #17
    //   571: fload #19
    //   573: fsub
    //   574: fstore #17
    //   576: ldc_w 1137836032
    //   579: istore #18
    //   581: ldc 420.0
    //   583: fstore #19
    //   585: fload #17
    //   587: fload #19
    //   589: fcmpl
    //   590: istore #24
    //   592: iload #24
    //   594: iflt -> 674
    //   597: getstatic lunarians/UsedArea.red : [Z
    //   600: astore #22
    //   602: aload #22
    //   604: iload #10
    //   606: iload_2
    //   607: bastore
    //   608: goto -> 674
    //   611: getstatic lunarians/UsedArea.seen : Ljava/util/HashMap;
    //   614: astore #22
    //   616: iload #10
    //   618: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   621: astore #23
    //   623: aload #22
    //   625: aload #23
    //   627: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   630: astore #22
    //   632: aload #22
    //   634: ifnonnull -> 654
    //   637: getstatic lunarians/UsedArea.red : [Z
    //   640: astore #22
    //   642: aload #22
    //   644: iload #10
    //   646: baload
    //   647: istore #24
    //   649: iload #24
    //   651: ifeq -> 674
    //   654: getstatic lunarians/UsedArea.red : [Z
    //   657: astore #22
    //   659: iconst_0
    //   660: istore #18
    //   662: fconst_0
    //   663: fstore #19
    //   665: aconst_null
    //   666: astore #23
    //   668: aload #22
    //   670: iload #10
    //   672: iconst_0
    //   673: bastore
    //   674: iload #16
    //   676: iconst_1
    //   677: iadd
    //   678: istore #16
    //   680: goto -> 348
    //   683: iload #7
    //   685: iconst_1
    //   686: iadd
    //   687: istore #7
    //   689: goto -> 327
    //   692: astore_0
    //   693: ldc_w '[Lunarians] UsedArea'
    //   696: astore #25
    //   698: aload #25
    //   700: aload_0
    //   701: invokestatic err : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   704: invokestatic reset : ()V
    //   707: return
    // Exception table:
    //   from	to	target	type
    //   29	32	692	finally
    //   42	46	692	finally
    //   56	60	692	finally
    //   65	70	692	finally
    //   74	78	692	finally
    //   84	89	692	finally
    //   93	97	692	finally
    //   99	102	692	finally
    //   109	112	692	finally
    //   120	123	692	finally
    //   132	135	692	finally
    //   149	152	692	finally
    //   154	159	692	finally
    //   159	163	692	finally
    //   163	167	692	finally
    //   167	172	692	finally
    //   172	177	692	finally
    //   177	181	692	finally
    //   181	184	692	finally
    //   189	192	692	finally
    //   194	197	692	finally
    //   209	212	692	finally
    //   228	233	692	finally
    //   233	236	692	finally
    //   237	241	692	finally
    //   242	245	692	finally
    //   252	255	692	finally
    //   256	259	692	finally
    //   261	266	692	finally
    //   276	279	692	finally
    //   281	286	692	finally
    //   289	292	692	finally
    //   294	299	692	finally
    //   299	302	692	finally
    //   309	314	692	finally
    //   320	323	692	finally
    //   327	330	692	finally
    //   341	344	692	finally
    //   348	351	692	finally
    //   362	365	692	finally
    //   367	372	692	finally
    //   378	383	692	finally
    //   400	403	692	finally
    //   410	413	692	finally
    //   426	431	692	finally
    //   433	436	692	finally
    //   459	462	692	finally
    //   468	473	692	finally
    //   480	485	692	finally
    //   492	495	692	finally
    //   497	502	692	finally
    //   506	511	692	finally
    //   513	518	692	finally
    //   525	530	692	finally
    //   532	535	692	finally
    //   537	542	692	finally
    //   548	554	692	finally
    //   557	560	692	finally
    //   562	567	692	finally
    //   597	600	692	finally
    //   606	608	692	finally
    //   611	614	692	finally
    //   616	621	692	finally
    //   625	630	692	finally
    //   637	640	692	finally
    //   644	647	692	finally
    //   654	657	692	finally
    //   672	674	692	finally
  }
}
