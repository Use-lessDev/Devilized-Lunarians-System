package lunarians;

import arc.Core;
import arc.Events;
import arc.Settings;
import arc.graphics.Camera;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.style.Drawable;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.core.GameState;
import mindustry.core.UI;
import mindustry.core.World;
import mindustry.game.EventType;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.ui.Styles;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;

public class AutoEconomy {
  public static final String KEY = "lunarians-econ-order";
  
  public static boolean active = false;
  
  private static boolean adopted;
  
  private static float areaR;
  
  private static int areaX;
  
  private static int areaY;
  
  private static float autoAt;
  
  private static boolean drawReg;
  
  private static boolean finished;
  
  private static int idx;
  
  private static boolean inUnits;
  
  private static int lastStep;
  
  private static int lastTotal;
  
  private static float lastX;
  
  private static float lastY;
  
  private static boolean mapWanted;
  
  private static int[] markColor;
  
  private static int markCount;
  
  private static int[] markIdx;
  
  private static boolean[] marked;
  
  private static boolean panelBuilt;
  
  private static final ArrayList sandList;
  
  private static boolean[] sandMarked;
  
  private static int sandMode;
  
  private static boolean sawRunning;
  
  private static float scanAt;
  
  private static float scanStart;
  
  private static boolean scanning;
  
  private static Object[] snap;
  
  private static final ArrayList stages;
  
  private static boolean startRequested;
  
  private static float stillSince;
  
  private static final Color tmpC;
  
  private static float unitsDeadline;
  
  static {
    ArrayList arrayList1 = new ArrayList();
    this();
    stages = arrayList1;
    idx = 0;
    adopted = false;
    markIdx = new int[0];
    markColor = new int[0];
    Color color = new Color();
    this();
    tmpC = color;
    ArrayList arrayList2 = new ArrayList();
    this();
    sandList = arrayList2;
    lastStep = 0;
    lastTotal = 0;
    sawRunning = false;
    unitsDeadline = 0.0F;
    inUnits = false;
  }
  
  public static void abort() {
    abort(false);
  }
  
  public static void abort(boolean paramBoolean) {
    if (paramBoolean)
      clearMarks(); 
    restoreProfile();
    active = false;
    adopted = false;
    inUnits = false;
    sawRunning = false;
    ArrayList arrayList = stages;
    arrayList.clear();
    paramBoolean = AutoDock.isUnitsMode();
    if (paramBoolean)
      AutoDock.stopUnits(); 
  }
  
  private static void advance() {
    int i = idx;
    byte b = 1;
    i += b;
    idx = i;
    ArrayList<Object[]> arrayList = stages;
    int j = arrayList.size();
    if (i >= j) {
      toast("Economia completa: Auto Dock assume os schematics.");
      active = false;
      adopted = false;
      finished = b;
      restoreProfile();
      LunariansSettings.dockAuto = b;
      LunariansSettings.save();
      AutoDock.finishEconomy();
      return;
    } 
    i = idx;
    Object[] arrayOfObject = arrayList.get(i);
    String str = "units";
    Object object = arrayOfObject[b];
    boolean bool = str.equals(object);
    j = idx;
    if (bool) {
      object = new StringBuilder();
      super("Economia: etapa ");
      j += b;
      object.append(j);
      object.append("/");
      int k = arrayList.size();
      object.append(k);
      object.append(" - Unidades (Auto Dock)");
      toast(object.toString());
      LunariansSettings.dockAuto = b;
      AutoDock.startUnits();
      inUnits = b;
      unitsDeadline = Time.time + 14400.0F;
      return;
    } 
    load(j, b);
  }
  
  public static void applyProfile() {
    Object[] arrayOfObject1 = snap;
    if (arrayOfObject1 != null)
      return; 
    Integer integer1 = Integer.valueOf(LunariansSettings.drillMaxTiles);
    Integer integer2 = Integer.valueOf(LunariansSettings.drillMinOres);
    Integer integer3 = Integer.valueOf(LunariansSettings.drillOptQuality);
    Boolean bool1 = Boolean.valueOf(LunariansSettings.drillPlaceWaterPower);
    Integer integer4 = Integer.valueOf(LunariansSettings.drillExitDir);
    Integer integer5 = Integer.valueOf(LunariansSettings.drillLayoutMode);
    Boolean bool2 = Boolean.valueOf(LunariansSettings.drillPerfect);
    Object[] arrayOfObject2 = new Object[8];
    String str = LunariansSettings.drillBlock;
    arrayOfObject2[0] = str;
    boolean bool = true;
    arrayOfObject2[bool] = integer1;
    byte b1 = 2;
    arrayOfObject2[b1] = integer2;
    byte b2 = 3;
    arrayOfObject2[b2] = integer3;
    byte b3 = 4;
    arrayOfObject2[b3] = bool1;
    byte b4 = 5;
    arrayOfObject2[b4] = integer4;
    byte b5 = 6;
    arrayOfObject2[b5] = integer5;
    byte b6 = 7;
    arrayOfObject2[b6] = bool2;
    snap = arrayOfObject2;
    Settings settings = Core.settings;
    StringBuilder stringBuilder = new StringBuilder();
    this();
    Object object2 = snap[0];
    stringBuilder.append(object2);
    object2 = "|";
    stringBuilder.append((String)object2);
    Object object3 = snap[bool];
    stringBuilder.append(object3);
    stringBuilder.append((String)object2);
    Object object1 = snap[b1];
    stringBuilder.append(object1);
    stringBuilder.append((String)object2);
    object1 = snap[b2];
    stringBuilder.append(object1);
    stringBuilder.append((String)object2);
    object1 = snap[b3];
    stringBuilder.append(object1);
    stringBuilder.append((String)object2);
    object1 = snap[b4];
    stringBuilder.append(object1);
    stringBuilder.append((String)object2);
    object1 = snap[b5];
    stringBuilder.append(object1);
    stringBuilder.append((String)object2);
    object1 = snap[b6];
    stringBuilder.append(object1);
    object1 = stringBuilder.toString();
    settings.put("lunarians-profile-backup", object1);
    LunariansSettings.drillBlock = "mechanical-drill";
    LunariansSettings.drillMaxTiles = 400;
    LunariansSettings.drillMinOres = bool;
    LunariansSettings.drillOptQuality = 10;
    LunariansSettings.drillPlaceWaterPower = false;
    LunariansSettings.drillExitDir = 0;
    LunariansSettings.drillLayoutMode = 0;
    LunariansSettings.drillPerfect = false;
  }
  
  private static void applySand(boolean[] paramArrayOfboolean) {
    ArrayList arrayList = sandList;
    boolean bool = arrayList.isEmpty();
    if (!bool) {
      boolean[] arrayOfBoolean = sandMarked;
      if (arrayOfBoolean != null && paramArrayOfboolean != null) {
        World world = Vars.world;
        int i = world.width();
        int j = areaY;
        int k = (int)areaR;
        j -= k;
        k = 1;
        j -= k;
        j = Math.max(0, j);
        while (true) {
          World world1 = Vars.world;
          int m = world1.height() - k;
          int n = areaY;
          float f = areaR;
          int i1 = (int)f;
          n = n + i1 + k;
          m = Math.min(m, n);
          if (j <= m) {
            m = areaX;
            float f1 = areaR;
            n = (int)f1;
            m = m - n - k;
            m = Math.max(0, m);
            while (true) {
              n = i + -1;
              i1 = areaX;
              float f2 = areaR;
              int i2 = (int)f2;
              i1 = i1 + i2 + k;
              n = Math.min(n, i1);
              if (m <= n) {
                n = j * i + m;
                boolean[] arrayOfBoolean1 = sandMarked;
                boolean bool1 = arrayOfBoolean1[n];
                if (!bool1) {
                  int i3 = paramArrayOfboolean.length;
                  if (n < i3) {
                    Tile tile = Vars.world.tile(m, j);
                    if (tile != null) {
                      boolean bool2 = isSandTile(tile);
                      if (bool2)
                        paramArrayOfboolean[n] = k; 
                    } 
                  } 
                } 
                m++;
                continue;
              } 
              j++;
            } 
          } 
          break;
        } 
      } 
    } 
  }
  
  private static void buildPanel() {
    boolean bool = panelBuilt;
    if (!bool) {
      UI uI = Vars.ui;
      if (uI != null) {
        WidgetGroup widgetGroup = Vars.ui.hudGroup;
        if (widgetGroup != null) {
          bool = true;
          panelBuilt = bool;
          widgetGroup = Vars.ui.hudGroup;
          AutoEconomy$0 autoEconomy$0 = new AutoEconomy$0();
          this();
          widgetGroup.fill(autoEconomy$0);
        } 
      } 
    } 
  }
  
  private static void buildStages() {
    // Byte code:
    //   0: getstatic lunarians/AutoEconomy.stages : Ljava/util/ArrayList;
    //   3: invokevirtual clear : ()V
    //   6: iconst_0
    //   7: invokestatic oreCount : (I)I
    //   10: istore_0
    //   11: iconst_1
    //   12: istore_1
    //   13: iload_1
    //   14: iload_0
    //   15: invokestatic max : (II)I
    //   18: istore_0
    //   19: iload_1
    //   20: invokestatic oreCount : (I)I
    //   23: istore_2
    //   24: iload_1
    //   25: iload_2
    //   26: invokestatic max : (II)I
    //   29: istore_2
    //   30: iconst_2
    //   31: istore_3
    //   32: iload_3
    //   33: invokestatic oreCount : (I)I
    //   36: istore #4
    //   38: iload_1
    //   39: iload #4
    //   41: invokestatic max : (II)I
    //   44: istore #4
    //   46: iconst_3
    //   47: istore #5
    //   49: iload #5
    //   51: invokestatic oreCount : (I)I
    //   54: istore #6
    //   56: iload_1
    //   57: iload #6
    //   59: invokestatic max : (II)I
    //   62: istore #6
    //   64: iconst_4
    //   65: invokestatic oreCount : (I)I
    //   68: istore #7
    //   70: iload_3
    //   71: iload #7
    //   73: invokestatic max : (II)I
    //   76: istore #7
    //   78: iconst_5
    //   79: invokestatic oreCount : (I)I
    //   82: istore #8
    //   84: iload_1
    //   85: iload #8
    //   87: invokestatic max : (II)I
    //   90: istore #8
    //   92: new java/util/ArrayList
    //   95: astore #9
    //   97: aload #9
    //   99: invokespecial <init> : ()V
    //   102: iconst_0
    //   103: istore #10
    //   105: aconst_null
    //   106: astore #11
    //   108: iload #10
    //   110: iload_0
    //   111: if_icmpge -> 136
    //   114: ldc_w 'copper'
    //   117: astore #12
    //   119: aload #9
    //   121: aload #12
    //   123: invokevirtual add : (Ljava/lang/Object;)Z
    //   126: pop
    //   127: iload #10
    //   129: iconst_1
    //   130: iadd
    //   131: istore #10
    //   133: goto -> 108
    //   136: iconst_0
    //   137: istore_0
    //   138: aconst_null
    //   139: astore #13
    //   141: iload_0
    //   142: iload_2
    //   143: if_icmpge -> 166
    //   146: ldc_w 'lead'
    //   149: astore #11
    //   151: aload #9
    //   153: aload #11
    //   155: invokevirtual add : (Ljava/lang/Object;)Z
    //   158: pop
    //   159: iload_0
    //   160: iconst_1
    //   161: iadd
    //   162: istore_0
    //   163: goto -> 141
    //   166: getstatic lunarians/AutoEconomy.stages : Ljava/util/ArrayList;
    //   169: astore #13
    //   171: iconst_0
    //   172: anewarray java/lang/String
    //   175: astore #14
    //   177: aload #9
    //   179: aload #14
    //   181: invokevirtual toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   184: astore #14
    //   186: iload #5
    //   188: anewarray java/lang/Object
    //   191: astore #9
    //   193: aload #9
    //   195: iconst_0
    //   196: ldc_w 'Cobre+Chumbo'
    //   199: aastore
    //   200: ldc_w 'drill'
    //   203: astore #11
    //   205: aload #9
    //   207: iload_1
    //   208: aload #11
    //   210: aastore
    //   211: aload #9
    //   213: iload_3
    //   214: aload #14
    //   216: aastore
    //   217: aload #13
    //   219: aload #9
    //   221: invokevirtual add : (Ljava/lang/Object;)Z
    //   224: pop
    //   225: new java/util/ArrayList
    //   228: astore #13
    //   230: aload #13
    //   232: invokespecial <init> : ()V
    //   235: iconst_0
    //   236: istore_2
    //   237: aconst_null
    //   238: astore #14
    //   240: iload #4
    //   242: iload #6
    //   244: invokestatic max : (II)I
    //   247: istore #15
    //   249: iload_2
    //   250: iload #15
    //   252: if_icmpge -> 300
    //   255: iload_2
    //   256: iload #4
    //   258: if_icmpge -> 274
    //   261: ldc_w 'coal'
    //   264: astore #9
    //   266: aload #13
    //   268: aload #9
    //   270: invokevirtual add : (Ljava/lang/Object;)Z
    //   273: pop
    //   274: iload_2
    //   275: iload #6
    //   277: if_icmpge -> 293
    //   280: ldc_w 'sand'
    //   283: astore #9
    //   285: aload #13
    //   287: aload #9
    //   289: invokevirtual add : (Ljava/lang/Object;)Z
    //   292: pop
    //   293: iload_2
    //   294: iconst_1
    //   295: iadd
    //   296: istore_2
    //   297: goto -> 240
    //   300: getstatic lunarians/AutoEconomy.stages : Ljava/util/ArrayList;
    //   303: astore #14
    //   305: iconst_0
    //   306: anewarray java/lang/String
    //   309: astore #16
    //   311: aload #13
    //   313: aload #16
    //   315: invokevirtual toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   318: astore #13
    //   320: iload #5
    //   322: anewarray java/lang/Object
    //   325: astore #16
    //   327: aload #16
    //   329: iconst_0
    //   330: ldc_w 'Carvao+Areia'
    //   333: aastore
    //   334: aload #16
    //   336: iload_1
    //   337: aload #11
    //   339: aastore
    //   340: aload #16
    //   342: iload_3
    //   343: aload #13
    //   345: aastore
    //   346: aload #14
    //   348: aload #16
    //   350: invokevirtual add : (Ljava/lang/Object;)Z
    //   353: pop
    //   354: iload #5
    //   356: anewarray [Ljava/lang/String;
    //   359: astore #13
    //   361: iconst_3
    //   362: anewarray java/lang/String
    //   365: dup
    //   366: dup2
    //   367: iconst_0
    //   368: ldc_w 'graphite'
    //   371: aastore
    //   372: iconst_1
    //   373: ldc_w '0'
    //   376: aastore
    //   377: iconst_2
    //   378: ldc_w 'Grafite'
    //   381: aastore
    //   382: astore #14
    //   384: aload #13
    //   386: iconst_0
    //   387: aload #14
    //   389: aastore
    //   390: iconst_3
    //   391: anewarray java/lang/String
    //   394: dup
    //   395: dup2
    //   396: iconst_0
    //   397: ldc_w 'metaglass'
    //   400: aastore
    //   401: iconst_1
    //   402: ldc_w '2'
    //   405: aastore
    //   406: iconst_2
    //   407: ldc_w 'Metavidro'
    //   410: aastore
    //   411: astore #14
    //   413: aload #13
    //   415: iload_1
    //   416: aload #14
    //   418: aastore
    //   419: ldc_w 'Silicio'
    //   422: astore #16
    //   424: ldc_w 'silicon'
    //   427: astore #17
    //   429: iconst_3
    //   430: anewarray java/lang/String
    //   433: dup
    //   434: dup2
    //   435: iconst_0
    //   436: aload #17
    //   438: aastore
    //   439: iconst_1
    //   440: ldc_w '1'
    //   443: aastore
    //   444: iconst_2
    //   445: aload #16
    //   447: aastore
    //   448: astore #14
    //   450: aload #13
    //   452: iload_3
    //   453: aload #14
    //   455: aastore
    //   456: iconst_0
    //   457: istore_2
    //   458: aconst_null
    //   459: astore #14
    //   461: iload_2
    //   462: iload #5
    //   464: if_icmpge -> 614
    //   467: aload #13
    //   469: iload_2
    //   470: aaload
    //   471: astore #16
    //   473: aload #16
    //   475: iload_1
    //   476: aaload
    //   477: astore #17
    //   479: aload #17
    //   481: invokestatic parseInt : (Ljava/lang/String;)I
    //   484: invokestatic facCount : (I)I
    //   487: istore #6
    //   489: iload_1
    //   490: iload #6
    //   492: invokestatic max : (II)I
    //   495: istore #6
    //   497: new java/util/ArrayList
    //   500: astore #9
    //   502: aload #9
    //   504: invokespecial <init> : ()V
    //   507: iconst_0
    //   508: istore #18
    //   510: aconst_null
    //   511: astore #12
    //   513: iload #18
    //   515: iload #6
    //   517: if_icmpge -> 543
    //   520: aload #16
    //   522: iconst_0
    //   523: aaload
    //   524: astore #19
    //   526: aload #9
    //   528: aload #19
    //   530: invokevirtual add : (Ljava/lang/Object;)Z
    //   533: pop
    //   534: iload #18
    //   536: iconst_1
    //   537: iadd
    //   538: istore #18
    //   540: goto -> 513
    //   543: getstatic lunarians/AutoEconomy.stages : Ljava/util/ArrayList;
    //   546: astore #17
    //   548: aload #16
    //   550: iload_3
    //   551: aaload
    //   552: astore #16
    //   554: iconst_0
    //   555: anewarray java/lang/String
    //   558: astore #12
    //   560: aload #9
    //   562: aload #12
    //   564: invokevirtual toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   567: astore #9
    //   569: iload #5
    //   571: anewarray java/lang/Object
    //   574: astore #12
    //   576: aload #12
    //   578: iconst_0
    //   579: aload #16
    //   581: aastore
    //   582: ldc_w 'fac'
    //   585: astore #16
    //   587: aload #12
    //   589: iload_1
    //   590: aload #16
    //   592: aastore
    //   593: aload #12
    //   595: iload_3
    //   596: aload #9
    //   598: aastore
    //   599: aload #17
    //   601: aload #12
    //   603: invokevirtual add : (Ljava/lang/Object;)Z
    //   606: pop
    //   607: iload_2
    //   608: iconst_1
    //   609: iadd
    //   610: istore_2
    //   611: goto -> 461
    //   614: new java/util/ArrayList
    //   617: astore #13
    //   619: aload #13
    //   621: invokespecial <init> : ()V
    //   624: iconst_0
    //   625: istore_2
    //   626: aconst_null
    //   627: astore #14
    //   629: iload_2
    //   630: iload #7
    //   632: if_icmpge -> 655
    //   635: ldc_w 'titanium'
    //   638: astore #16
    //   640: aload #13
    //   642: aload #16
    //   644: invokevirtual add : (Ljava/lang/Object;)Z
    //   647: pop
    //   648: iload_2
    //   649: iconst_1
    //   650: iadd
    //   651: istore_2
    //   652: goto -> 629
    //   655: getstatic lunarians/AutoEconomy.stages : Ljava/util/ArrayList;
    //   658: astore #14
    //   660: iconst_0
    //   661: anewarray java/lang/String
    //   664: astore #16
    //   666: aload #13
    //   668: aload #16
    //   670: invokevirtual toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   673: astore #13
    //   675: iload #5
    //   677: anewarray java/lang/Object
    //   680: astore #16
    //   682: aload #16
    //   684: iconst_0
    //   685: ldc_w 'Titanio'
    //   688: aastore
    //   689: aload #16
    //   691: iload_1
    //   692: aload #11
    //   694: aastore
    //   695: aload #16
    //   697: iload_3
    //   698: aload #13
    //   700: aastore
    //   701: aload #14
    //   703: aload #16
    //   705: invokevirtual add : (Ljava/lang/Object;)Z
    //   708: pop
    //   709: iconst_0
    //   710: anewarray java/lang/String
    //   713: astore #13
    //   715: iload #5
    //   717: anewarray java/lang/Object
    //   720: astore #16
    //   722: aload #16
    //   724: iconst_0
    //   725: ldc_w 'Unidades'
    //   728: aastore
    //   729: ldc 'units'
    //   731: astore #17
    //   733: aload #16
    //   735: iload_1
    //   736: aload #17
    //   738: aastore
    //   739: aload #16
    //   741: iload_3
    //   742: aload #13
    //   744: aastore
    //   745: aload #14
    //   747: aload #16
    //   749: invokevirtual add : (Ljava/lang/Object;)Z
    //   752: pop
    //   753: new java/util/ArrayList
    //   756: astore #13
    //   758: aload #13
    //   760: invokespecial <init> : ()V
    //   763: iconst_0
    //   764: istore_2
    //   765: aconst_null
    //   766: astore #14
    //   768: iload_2
    //   769: iload #8
    //   771: if_icmpge -> 794
    //   774: ldc_w 'thorium'
    //   777: astore #16
    //   779: aload #13
    //   781: aload #16
    //   783: invokevirtual add : (Ljava/lang/Object;)Z
    //   786: pop
    //   787: iload_2
    //   788: iconst_1
    //   789: iadd
    //   790: istore_2
    //   791: goto -> 768
    //   794: getstatic lunarians/AutoEconomy.stages : Ljava/util/ArrayList;
    //   797: astore #14
    //   799: iconst_0
    //   800: anewarray java/lang/String
    //   803: astore #16
    //   805: aload #13
    //   807: aload #16
    //   809: invokevirtual toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   812: astore #13
    //   814: iload #5
    //   816: anewarray java/lang/Object
    //   819: astore #16
    //   821: aload #16
    //   823: iconst_0
    //   824: ldc_w 'Torio'
    //   827: aastore
    //   828: aload #16
    //   830: iload_1
    //   831: aload #11
    //   833: aastore
    //   834: aload #16
    //   836: iload_3
    //   837: aload #13
    //   839: aastore
    //   840: aload #14
    //   842: aload #16
    //   844: invokevirtual add : (Ljava/lang/Object;)Z
    //   847: pop
    //   848: return
  }
  
  private static void clearMarks() {
    sandList.clear();
    sandMarked = null;
    sandMode = 0;
    startRequested = false;
    mapWanted = false;
    markCount = 0;
    marked = null;
    scanning = false;
  }
  
  private static void drawMarks() {
    boolean[] arrayOfBoolean = marked;
    if (arrayOfBoolean != null) {
      GameState gameState = Vars.state;
      if (gameState != null) {
        gameState = Vars.state;
        boolean bool = gameState.isGame();
        if (bool) {
          World world = Vars.world;
          int i = world.width();
          Draw.z(120.0F);
          Vec2 vec21 = Core.camera.position;
          float f1 = vec21.x;
          Vec2 vec22 = Core.camera.position;
          float f2 = vec22.y;
          Camera camera1 = Core.camera;
          float f3 = camera1.width;
          float f4 = 2.0F;
          f3 /= f4;
          float f5 = 16.0F;
          f3 += f5;
          Camera camera2 = Core.camera;
          float f6 = camera2.height / f4 + f5;
          int j = 0;
          f4 = 0.0F;
          int k = 0;
          f5 = 0.0F;
          Color color = null;
          while (true) {
            int m = markCount;
            float f7 = 7.0F;
            if (k < m) {
              int[] arrayOfInt = markIdx;
              m = arrayOfInt[k];
              int n = m % i;
              m /= i;
              n <<= 3;
              float f9 = n;
              float f10 = Math.abs(f9 - f1);
              int i1 = f10 cmp f3;
              if (i1 <= 0) {
                m <<= 3;
                float f = m;
                f10 = Math.abs(f - f2);
                i1 = f10 cmp f6;
                if (i1 <= 0) {
                  Color color1 = tmpC;
                  int[] arrayOfInt1 = markColor;
                  int i2 = arrayOfInt1[k];
                  color1.rgba8888(i2);
                  i2 = 1057803469;
                  float f11 = 0.55F;
                  Draw.color(color1, f11);
                  Fill.rect(f9, f, f7, f7);
                } 
              } 
              k++;
              continue;
            } 
            color = Items.sand.color;
            m = 1058642330;
            float f8 = 0.6F;
            Draw.color(color, f8);
            while (true) {
              ArrayList<Integer> arrayList = sandList;
              m = arrayList.size();
              if (j < m) {
                Integer integer2 = arrayList.get(j);
                m = integer2.intValue() % i;
                Integer integer1 = arrayList.get(j);
                k = integer1.intValue() / i;
                m <<= 3;
                f8 = m;
                float f = Math.abs(f8 - f1);
                int n = f cmp f3;
                if (n <= 0) {
                  k <<= 3;
                  f5 = k;
                  f = Math.abs(f5 - f2);
                  n = f cmp f6;
                  if (n <= 0)
                    Fill.rect(f8, f5, f7, f7); 
                } 
                j++;
                continue;
              } 
              Draw.reset();
              break;
            } 
            break;
          } 
        } 
      } 
    } 
  }
  
  public static boolean enabled() {
    return Core.settings.getBool("lunarians-econ-order", false);
  }
  
  private static Field fld(String paramString) {
    Field field = AutoSet.class.getDeclaredField(paramString);
    field.setAccessible(true);
    return field;
  }
  
  private static int geti(String paramString) {
    return fld(paramString).getInt(null);
  }
  
  public static void init() {
    recoverProfile();
    EventType.Trigger trigger = EventType.Trigger.update;
    AutoEconomy$10 autoEconomy$10 = new AutoEconomy$10();
    this();
    Events.run(trigger, autoEconomy$10);
    AutoEconomy$11 autoEconomy$11 = new AutoEconomy$11();
    this();
    Events.on(EventType.WorldLoadEvent.class, autoEconomy$11);
  }
  
  private static void input(Unit paramUnit) {
    // Byte code:
    //   0: invokestatic buildPanel : ()V
    //   3: getstatic lunarians/AutoEconomy.sandMode : I
    //   6: istore_1
    //   7: iload_1
    //   8: ifeq -> 424
    //   11: getstatic arc/Core.input : Larc/Input;
    //   14: astore_0
    //   15: aload_0
    //   16: invokevirtual justTouched : ()Z
    //   19: istore_1
    //   20: iload_1
    //   21: ifeq -> 424
    //   24: getstatic arc/Core.scene : Larc/scene/Scene;
    //   27: astore_0
    //   28: aload_0
    //   29: invokevirtual hasMouse : ()Z
    //   32: istore_1
    //   33: iload_1
    //   34: ifne -> 424
    //   37: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   40: astore_0
    //   41: aload_0
    //   42: invokevirtual width : ()I
    //   45: istore_1
    //   46: getstatic lunarians/AutoEconomy.sandMarked : [Z
    //   49: astore_2
    //   50: aload_2
    //   51: ifnull -> 77
    //   54: aload_2
    //   55: arraylength
    //   56: istore_3
    //   57: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   60: astore #4
    //   62: aload #4
    //   64: invokevirtual height : ()I
    //   67: iload_1
    //   68: imul
    //   69: istore #5
    //   71: iload_3
    //   72: iload #5
    //   74: if_icmpeq -> 100
    //   77: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   80: invokevirtual height : ()I
    //   83: iload_1
    //   84: imul
    //   85: istore_3
    //   86: iload_3
    //   87: newarray boolean
    //   89: putstatic lunarians/AutoEconomy.sandMarked : [Z
    //   92: getstatic lunarians/AutoEconomy.sandList : Ljava/util/ArrayList;
    //   95: astore_2
    //   96: aload_2
    //   97: invokevirtual clear : ()V
    //   100: getstatic arc/Core.input : Larc/Input;
    //   103: astore_2
    //   104: aload_2
    //   105: invokevirtual mouseWorldX : ()F
    //   108: fstore #6
    //   110: fload #6
    //   112: invokestatic toTile : (F)I
    //   115: istore_3
    //   116: getstatic arc/Core.input : Larc/Input;
    //   119: astore #4
    //   121: aload #4
    //   123: invokevirtual mouseWorldY : ()F
    //   126: fstore #7
    //   128: fload #7
    //   130: invokestatic toTile : (F)I
    //   133: istore #5
    //   135: iload #5
    //   137: bipush #-3
    //   139: iadd
    //   140: istore #8
    //   142: iload #5
    //   144: iconst_3
    //   145: iadd
    //   146: istore #9
    //   148: iload #8
    //   150: iload #9
    //   152: if_icmpgt -> 424
    //   155: iload_3
    //   156: bipush #-3
    //   158: iadd
    //   159: istore #9
    //   161: iload_3
    //   162: iconst_3
    //   163: iadd
    //   164: istore #10
    //   166: iload #9
    //   168: iload #10
    //   170: if_icmpgt -> 415
    //   173: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   176: getfield tiles : Lmindustry/world/Tiles;
    //   179: astore #11
    //   181: aload #11
    //   183: iload #9
    //   185: iload #8
    //   187: invokevirtual in : (II)Z
    //   190: istore #10
    //   192: iload #10
    //   194: ifeq -> 406
    //   197: iload #9
    //   199: i2f
    //   200: fstore #12
    //   202: iload #8
    //   204: i2f
    //   205: fstore #13
    //   207: iload_3
    //   208: i2f
    //   209: fstore #14
    //   211: iload #5
    //   213: i2f
    //   214: fstore #15
    //   216: fload #12
    //   218: fload #13
    //   220: fload #14
    //   222: fload #15
    //   224: invokestatic dst : (FFFF)F
    //   227: fstore #12
    //   229: ldc_w 1080033280
    //   232: istore #16
    //   234: ldc_w 3.5
    //   237: fstore #13
    //   239: fload #12
    //   241: fload #13
    //   243: fcmpl
    //   244: istore #10
    //   246: iload #10
    //   248: ifgt -> 406
    //   251: iload #8
    //   253: iload_1
    //   254: imul
    //   255: iload #9
    //   257: iadd
    //   258: istore #10
    //   260: getstatic lunarians/AutoEconomy.sandMode : I
    //   263: istore #16
    //   265: iconst_1
    //   266: istore #17
    //   268: ldc_w 1.4E-45
    //   271: fstore #14
    //   273: iload #16
    //   275: iload #17
    //   277: if_icmpne -> 357
    //   280: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   283: iload #9
    //   285: iload #8
    //   287: invokevirtual tile : (II)Lmindustry/world/Tile;
    //   290: astore #18
    //   292: getstatic lunarians/AutoEconomy.sandMarked : [Z
    //   295: astore #19
    //   297: aload #19
    //   299: iload #10
    //   301: baload
    //   302: istore #20
    //   304: iload #20
    //   306: ifne -> 406
    //   309: aload #18
    //   311: ifnull -> 406
    //   314: aload #18
    //   316: invokestatic isSandTile : (Lmindustry/world/Tile;)Z
    //   319: istore #16
    //   321: iload #16
    //   323: ifeq -> 406
    //   326: getstatic lunarians/AutoEconomy.sandMarked : [Z
    //   329: iload #10
    //   331: iload #17
    //   333: bastore
    //   334: getstatic lunarians/AutoEconomy.sandList : Ljava/util/ArrayList;
    //   337: astore #18
    //   339: iload #10
    //   341: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   344: astore #11
    //   346: aload #18
    //   348: aload #11
    //   350: invokevirtual add : (Ljava/lang/Object;)Z
    //   353: pop
    //   354: goto -> 406
    //   357: getstatic lunarians/AutoEconomy.sandMarked : [Z
    //   360: astore #18
    //   362: aload #18
    //   364: iload #10
    //   366: baload
    //   367: istore #17
    //   369: iload #17
    //   371: ifeq -> 406
    //   374: iconst_0
    //   375: istore #17
    //   377: fconst_0
    //   378: fstore #14
    //   380: aload #18
    //   382: iload #10
    //   384: iconst_0
    //   385: bastore
    //   386: getstatic lunarians/AutoEconomy.sandList : Ljava/util/ArrayList;
    //   389: astore #18
    //   391: iload #10
    //   393: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   396: astore #11
    //   398: aload #18
    //   400: aload #11
    //   402: invokevirtual remove : (Ljava/lang/Object;)Z
    //   405: pop
    //   406: iload #9
    //   408: iconst_1
    //   409: iadd
    //   410: istore #9
    //   412: goto -> 161
    //   415: iload #8
    //   417: iconst_1
    //   418: iadd
    //   419: istore #8
    //   421: goto -> 142
    //   424: return
  }
  
  public static boolean isSandTile(Tile paramTile) {
    String str = "sand";
    if (paramTile == null)
      return false; 
    boolean bool = true;
    try {
      Floor floor = paramTile.floor();
      if (floor != null) {
        floor = paramTile.floor();
        boolean bool1 = floor.isLiquid;
        if (!bool1) {
          floor = paramTile.floor();
          Liquid liquid = floor.liquidDrop;
          if (liquid == null) {
            Floor floor2 = paramTile.floor();
            Item item1 = floor2.itemDrop;
            Item item2 = Items.sand;
            if (item1 == item2)
              return bool; 
            Floor floor1 = paramTile.floor();
            String str1 = floor1.name;
            if (str1 != null) {
              str1 = str1.toLowerCase();
              String str2 = "water";
              boolean bool2 = str1.contains(str2);
              if (!bool2) {
                str2 = "deep";
                bool2 = str1.contains(str2);
                if (!bool2) {
                  str2 = "pool";
                  bool2 = str1.contains(str2);
                  if (!bool2) {
                    str2 = "darksand";
                    bool2 = str1.contains(str2);
                    if (!bool2) {
                      bool2 = str1.equals(str);
                      if (!bool2) {
                        str2 = "sand-floor";
                        bool2 = str1.contains(str2);
                        if (!bool2) {
                          boolean bool3 = str1.contains(str);
                          if (bool3) {
                            str = "sandstone";
                            bool3 = str1.contains(str);
                            if (!bool3)
                              return bool; 
                          } 
                        } else {
                          return bool;
                        } 
                      } else {
                        return bool;
                      } 
                    } else {
                      return bool;
                    } 
                  } else {
                    return false;
                  } 
                } else {
                  return false;
                } 
              } else {
                return false;
              } 
            } 
          } else {
            return false;
          } 
        } else {
          return false;
        } 
      } 
    } finally {}
    try {
      Item item1 = paramTile.drop();
      Item item2 = Items.sand;
      if (item1 == item2)
        return bool; 
    } finally {}
    try {
      Floor floor = paramTile.overlay();
      if (floor != null) {
        Floor floor1 = paramTile.overlay();
        Item item1 = floor1.itemDrop;
        Item item2 = Items.sand;
        if (item1 == item2)
          return bool; 
      } 
    } finally {}
    return false;
  }
  
  private static void load(int paramInt, boolean paramBoolean) {
    ArrayList<Object[]> arrayList = stages;
    Object[] arrayOfObject = arrayList.get(paramInt);
    byte b1 = 1;
    String str1 = (String)arrayOfObject[b1];
    byte b2 = 2;
    String[] arrayOfString2 = (String[])arrayOfObject[b2];
    String str2 = "fac";
    boolean bool1 = str1.equals(str2);
    seti("runMode", bool1);
    Field field = fld("oreOrder");
    String str3 = "drill";
    boolean bool2 = str1.equals(str3);
    if (bool2) {
      arrayOfString3 = arrayOfString2;
    } else {
      arrayOfString3 = new String[0];
    } 
    field.set(null, arrayOfString3);
    field = fld("factoryQueue");
    boolean bool3 = str1.equals(str2);
    if (bool3) {
      arrayOfString1 = arrayOfString2;
    } else {
      arrayOfString1 = new String[0];
    } 
    field.set(null, arrayOfString1);
    seti("stepIndex", 0);
    ((ArrayList)fld("feedTargets").get(null)).clear();
    ((ArrayList)fld("feedItemNames").get(null)).clear();
    ((ArrayList)fld("feedReserved").get(null)).clear();
    ((HashMap)fld("feedLines").get(null)).clear();
    seti("feedIndex", 0);
    Seq seq = (Seq)fld("pending").get(null);
    seq.clear();
    if (paramBoolean) {
      seti("phase", 3);
      setb("hasArea", b1);
      setb("centerChosen", b1);
      int i = areaX;
      seti("cx", i);
      i = areaY;
      seti("cy", i);
      float f1 = areaR;
      setf("radius", f1);
      i = 1048576;
      seti("steamDone", i);
      f1 = Time.time;
      setf("lastProgress", f1);
      String str = "lastPending";
      seti(str, 0);
    } 
    float f = Time.time + 10.0F;
    setf("nextStepAt", f);
    lastStep = 0;
    lastTotal = arrayOfString2.length;
    sawRunning = b1;
    StringBuilder stringBuilder = new StringBuilder();
    this("Economia: etapa ");
    String[] arrayOfString1;
    String[] arrayOfString3;
    paramInt += b1;
    stringBuilder.append(paramInt);
    stringBuilder.append("/");
    paramInt = arrayList.size();
    stringBuilder.append(paramInt);
    stringBuilder.append(" - ");
    String str4 = String.valueOf(arrayOfObject[0]);
    stringBuilder.append(str4);
    toast(stringBuilder.toString());
  }
  
  public static int[] markedTiles(Item paramItem) {
    if (paramItem != null)
      try {
        boolean[] arrayOfBoolean = marked;
        return new int[0];
      } finally {
        null;
      }  
    return new int[0];
  }
  
  private static void recoverProfile() {
    String str = "lunarians-profile-backup";
    try {
      Settings settings = Core.settings;
      String str2 = "";
      return;
    } finally {
      null;
      Core.settings.remove(str);
    } 
  }
  
  public static void restoreProfile() {
    Object[] arrayOfObject = snap;
    if (arrayOfObject == null)
      return; 
    LunariansSettings.drillBlock = (String)arrayOfObject[0];
    LunariansSettings.drillMaxTiles = ((Integer)snap[1]).intValue();
    LunariansSettings.drillMinOres = ((Integer)snap[2]).intValue();
    LunariansSettings.drillOptQuality = ((Integer)snap[3]).intValue();
    LunariansSettings.drillPlaceWaterPower = ((Boolean)snap[4]).booleanValue();
    LunariansSettings.drillExitDir = ((Integer)snap[5]).intValue();
    LunariansSettings.drillLayoutMode = ((Integer)snap[6]).intValue();
    LunariansSettings.drillPerfect = ((Boolean)snap[7]).booleanValue();
    snap = null;
    Core.settings.remove("lunarians-profile-backup");
  }
  
  private static boolean scan(Unit paramUnit) {
    World world = Vars.world;
    int i = world.width();
    boolean bool = drawReg;
    boolean bool1 = true;
    if (!bool) {
      drawReg = bool1;
      EventType.Trigger trigger = EventType.Trigger.draw;
      AutoEconomy$9 autoEconomy$9 = new AutoEconomy$9();
      this();
      Events.run(trigger, autoEconomy$9);
    } 
    bool = scanning;
    Object object = null;
    if (bool) {
      boolean[] arrayOfBoolean = marked;
      if (arrayOfBoolean != null) {
        int k = arrayOfBoolean.length;
        World world1 = Vars.world;
        int m = world1.height() * i;
        if (k == m) {
          float f1 = paramUnit.x;
          float f2 = paramUnit.y;
          float f3 = lastX;
          float f4 = lastY;
          f1 = Mathf.dst(f1, f2, f3, f4);
          m = 1098907648;
          f2 = 16.0F;
          k = f1 cmp f2;
          if (k > 0) {
            lastX = paramUnit.x;
            lastY = paramUnit.y;
            f1 = Time.time;
            stillSince = f1;
          } 
          f1 = Time.time;
          f2 = scanAt;
          k = f1 cmp f2;
          if (k >= 0) {
            f1 = Time.time;
            f2 = 20.0F;
            f1 += f2;
            scanAt = f1;
            k = paramUnit.tileX();
            m = paramUnit.tileY();
            int n = m + -30;
            while (true) {
              int i1 = m + 30;
              if (n <= i1) {
                i1 = k + -30;
                while (true) {
                  int i2 = k + 30;
                  n++;
                } 
              } 
              break;
            } 
          } 
          input(paramUnit);
          boolean bool2 = startRequested;
          if (bool2) {
            startRequested = false;
            return bool1;
          } 
          return false;
        } 
      } 
    } 
    scanning = bool1;
    int j = Vars.world.height();
    marked = new boolean[i * j];
    i = 2048;
    markIdx = new int[i];
    markColor = new int[i];
    markCount = 0;
    scanStart = Time.time;
    stillSince = Time.time;
    lastX = paramUnit.x;
    lastY = paramUnit.y;
    toast(tr4("Auto Dock: va ate onde ha minerios (ficam marcados), marque areia se quiser e aperte COMECAR.", "Auto Dock: fly to the ore (it gets marked), mark sand if you want, then press START.", "Auto Dock: ve donde hay mineral (se marca), marca arena si quieres y pulsa EMPEZAR.", "Auto Dock: летите к руде, отметьте песок и нажмите СТАРТ."));
    return false;
  }
  
  public static void setEnabled(boolean paramBoolean) {
    Settings settings = Core.settings;
    String str = "lunarians-econ-order";
    Boolean bool = Boolean.valueOf(paramBoolean);
    settings.put(str, bool);
    if (!paramBoolean) {
      paramBoolean = true;
      abort(paramBoolean);
    } 
  }
  
  private static void setb(String paramString, boolean paramBoolean) {
    fld(paramString).setBoolean(null, paramBoolean);
  }
  
  private static void setf(String paramString, float paramFloat) {
    fld(paramString).setFloat(null, paramFloat);
  }
  
  private static void seti(String paramString, int paramInt) {
    fld(paramString).setInt(null, paramInt);
  }
  
  private static void toast(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    String str = "[Lunarians] ";
    this(str);
    stringBuilder.append(paramString);
    Log.info(stringBuilder.toString());
    UI uI = Vars.ui;
    if (uI != null) {
      uI = Vars.ui;
      float f = 4.0F;
      uI.showInfoToast(paramString, f);
    } 
  }
  
  private static String tr4(String paramString1, String paramString2, String paramString3, String paramString4) {
    String str1 = LunariansI18n.getLanguage();
    String str2 = "pt";
    boolean bool1 = str2.equals(str1);
    if (bool1)
      return paramString1; 
    paramString1 = "es";
    boolean bool2 = paramString1.equals(str1);
    if (bool2)
      return paramString3; 
    paramString1 = "ru";
    bool2 = paramString1.equals(str1);
    return bool2 ? paramString4 : paramString2;
  }
  
  private static void update() {
    // Byte code:
    //   0: ldc_w 'phase'
    //   3: astore_0
    //   4: invokestatic enabled : ()Z
    //   7: istore_1
    //   8: iconst_2
    //   9: istore_2
    //   10: ldc_w 2.8E-45
    //   13: fstore_3
    //   14: iload_1
    //   15: ifne -> 43
    //   18: getstatic lunarians/LunariansSettings.dockAuto : Z
    //   21: istore_1
    //   22: iload_1
    //   23: ifne -> 43
    //   26: invokestatic mode : ()I
    //   29: istore_1
    //   30: iload_1
    //   31: iload_2
    //   32: if_icmpeq -> 43
    //   35: getstatic lunarians/AutoEconomy.scanning : Z
    //   38: istore_1
    //   39: iload_1
    //   40: ifeq -> 848
    //   43: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   46: astore #4
    //   48: aload #4
    //   50: ifnull -> 848
    //   53: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   56: astore #4
    //   58: aload #4
    //   60: invokevirtual isGame : ()Z
    //   63: istore_1
    //   64: iload_1
    //   65: ifeq -> 848
    //   68: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   71: astore #4
    //   73: aload #4
    //   75: ifnull -> 848
    //   78: iconst_0
    //   79: istore_1
    //   80: aconst_null
    //   81: astore #4
    //   83: invokestatic isRunning : ()Z
    //   86: istore #5
    //   88: getstatic lunarians/AutoEconomy.adopted : Z
    //   91: istore #6
    //   93: iconst_1
    //   94: istore #7
    //   96: iload #6
    //   98: ifne -> 149
    //   101: getstatic lunarians/AutoEconomy.mapWanted : Z
    //   104: istore #6
    //   106: iload #6
    //   108: ifne -> 149
    //   111: invokestatic mode : ()I
    //   114: istore #6
    //   116: iload #6
    //   118: iload_2
    //   119: if_icmpne -> 149
    //   122: aload_0
    //   123: invokestatic geti : (Ljava/lang/String;)I
    //   126: istore #6
    //   128: iload #6
    //   130: iload_2
    //   131: if_icmpne -> 149
    //   134: aload_0
    //   135: iconst_0
    //   136: invokestatic seti : (Ljava/lang/String;I)V
    //   139: iload #7
    //   141: putstatic lunarians/AutoEconomy.mapWanted : Z
    //   144: iconst_0
    //   145: putstatic lunarians/AutoEconomy.finished : Z
    //   148: return
    //   149: getstatic lunarians/AutoEconomy.adopted : Z
    //   152: istore #8
    //   154: iconst_0
    //   155: istore_2
    //   156: fconst_0
    //   157: fstore_3
    //   158: aconst_null
    //   159: astore #9
    //   161: iload #8
    //   163: ifne -> 591
    //   166: iload #5
    //   168: ifne -> 591
    //   171: getstatic lunarians/AutoEconomy.mapWanted : Z
    //   174: istore #8
    //   176: iload #8
    //   178: ifeq -> 591
    //   181: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   184: astore_0
    //   185: aload_0
    //   186: invokevirtual closestCore : ()Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;
    //   189: astore_0
    //   190: aload_0
    //   191: ifnull -> 591
    //   194: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   197: astore_0
    //   198: aload_0
    //   199: invokevirtual unit : ()Lmindustry/gen/Unit;
    //   202: astore_0
    //   203: aload_0
    //   204: ifnull -> 591
    //   207: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   210: astore_0
    //   211: aload_0
    //   212: invokevirtual unit : ()Lmindustry/gen/Unit;
    //   215: astore_0
    //   216: aload_0
    //   217: invokevirtual canBuild : ()Z
    //   220: istore #8
    //   222: iload #8
    //   224: ifeq -> 591
    //   227: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   230: astore_0
    //   231: aload_0
    //   232: invokevirtual unit : ()Lmindustry/gen/Unit;
    //   235: astore_0
    //   236: aload_0
    //   237: invokestatic scan : (Lmindustry/gen/Unit;)Z
    //   240: istore #8
    //   242: iload #8
    //   244: ifeq -> 848
    //   247: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   250: astore_0
    //   251: aload_0
    //   252: invokevirtual unit : ()Lmindustry/gen/Unit;
    //   255: astore_0
    //   256: aload_0
    //   257: invokevirtual tileX : ()I
    //   260: istore #8
    //   262: iload #8
    //   264: putstatic lunarians/AutoEconomy.areaX : I
    //   267: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   270: astore_0
    //   271: aload_0
    //   272: invokevirtual unit : ()Lmindustry/gen/Unit;
    //   275: astore_0
    //   276: aload_0
    //   277: invokevirtual tileY : ()I
    //   280: istore #8
    //   282: iload #8
    //   284: putstatic lunarians/AutoEconomy.areaY : I
    //   287: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   290: astore_0
    //   291: aload_0
    //   292: invokevirtual width : ()I
    //   295: istore #8
    //   297: iconst_0
    //   298: istore #5
    //   300: fconst_0
    //   301: fstore #10
    //   303: aconst_null
    //   304: astore #11
    //   306: iconst_0
    //   307: istore #6
    //   309: fconst_0
    //   310: fstore #12
    //   312: aconst_null
    //   313: astore #13
    //   315: getstatic lunarians/AutoEconomy.markCount : I
    //   318: istore #14
    //   320: iload #6
    //   322: iload #14
    //   324: if_icmpge -> 414
    //   327: getstatic lunarians/AutoEconomy.markIdx : [I
    //   330: astore #15
    //   332: aload #15
    //   334: iload #6
    //   336: iaload
    //   337: istore #14
    //   339: iload #14
    //   341: iload #8
    //   343: irem
    //   344: istore #16
    //   346: iload #16
    //   348: i2f
    //   349: fstore #17
    //   351: iload #14
    //   353: iload #8
    //   355: idiv
    //   356: istore #14
    //   358: iload #14
    //   360: i2f
    //   361: fstore #18
    //   363: getstatic lunarians/AutoEconomy.areaX : I
    //   366: istore #19
    //   368: iload #19
    //   370: i2f
    //   371: fstore #20
    //   373: getstatic lunarians/AutoEconomy.areaY : I
    //   376: istore #21
    //   378: iload #21
    //   380: i2f
    //   381: fstore #22
    //   383: fload #17
    //   385: fload #18
    //   387: fload #20
    //   389: fload #22
    //   391: invokestatic dst : (FFFF)F
    //   394: fstore #18
    //   396: fload #10
    //   398: fload #18
    //   400: invokestatic max : (FF)F
    //   403: fstore #10
    //   405: iload #6
    //   407: iconst_1
    //   408: iadd
    //   409: istore #6
    //   411: goto -> 315
    //   414: invokestatic allRadius : ()I
    //   417: istore #8
    //   419: iload #8
    //   421: i2f
    //   422: fstore #23
    //   424: fload #10
    //   426: ldc_w 4.0
    //   429: fadd
    //   430: fstore #10
    //   432: ldc_w 1117782016
    //   435: istore #6
    //   437: ldc_w 80.0
    //   440: fstore #12
    //   442: fload #12
    //   444: fload #10
    //   446: invokestatic min : (FF)F
    //   449: fstore #10
    //   451: fload #23
    //   453: fload #10
    //   455: invokestatic max : (FF)F
    //   458: fstore #23
    //   460: fload #23
    //   462: putstatic lunarians/AutoEconomy.areaR : F
    //   465: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   468: astore_0
    //   469: aload_0
    //   470: invokevirtual width : ()I
    //   473: istore #8
    //   475: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   478: astore #11
    //   480: aload #11
    //   482: invokevirtual height : ()I
    //   485: istore #5
    //   487: iload #8
    //   489: iload #5
    //   491: imul
    //   492: istore #8
    //   494: iload #8
    //   496: newarray boolean
    //   498: astore_0
    //   499: aload_0
    //   500: invokestatic applySand : ([Z)V
    //   503: ldc_w 'used'
    //   506: astore #11
    //   508: aload #11
    //   510: invokestatic fld : (Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   513: astore #11
    //   515: aload #11
    //   517: aconst_null
    //   518: aload_0
    //   519: invokevirtual set : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   522: invokestatic applyProfile : ()V
    //   525: invokestatic buildStages : ()V
    //   528: iconst_0
    //   529: putstatic lunarians/AutoEconomy.idx : I
    //   532: iload #7
    //   534: putstatic lunarians/AutoEconomy.adopted : Z
    //   537: iload #7
    //   539: putstatic lunarians/AutoEconomy.active : Z
    //   542: iconst_0
    //   543: putstatic lunarians/AutoEconomy.scanning : Z
    //   546: iconst_0
    //   547: putstatic lunarians/AutoEconomy.mapWanted : Z
    //   550: iconst_0
    //   551: iload #7
    //   553: invokestatic load : (IZ)V
    //   556: ldc_w 'Mapeamento pronto: Auto Set na sua posicao, depois schematics.'
    //   559: astore_0
    //   560: ldc_w 'Mapping done: Auto Set at your position, then schematics.'
    //   563: astore #9
    //   565: ldc_w 'Mapeo listo: Auto Set en tu posicion, luego esquemas.'
    //   568: astore #11
    //   570: ldc_w 'Карта готова: Auto Set в вашей точке, потом схемы.'
    //   573: astore #13
    //   575: aload_0
    //   576: aload #9
    //   578: aload #11
    //   580: aload #13
    //   582: invokestatic tr4 : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   585: astore_0
    //   586: aload_0
    //   587: invokestatic toast : (Ljava/lang/String;)V
    //   590: return
    //   591: getstatic lunarians/AutoEconomy.adopted : Z
    //   594: istore #8
    //   596: iload #8
    //   598: ifne -> 680
    //   601: iload #5
    //   603: ifeq -> 848
    //   606: ldc_w 'cx'
    //   609: astore_0
    //   610: aload_0
    //   611: invokestatic geti : (Ljava/lang/String;)I
    //   614: istore #8
    //   616: iload #8
    //   618: putstatic lunarians/AutoEconomy.areaX : I
    //   621: ldc_w 'cy'
    //   624: astore_0
    //   625: aload_0
    //   626: invokestatic geti : (Ljava/lang/String;)I
    //   629: istore #8
    //   631: iload #8
    //   633: putstatic lunarians/AutoEconomy.areaY : I
    //   636: ldc_w 'radius'
    //   639: astore_0
    //   640: aload_0
    //   641: invokestatic fld : (Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   644: astore_0
    //   645: aload_0
    //   646: aconst_null
    //   647: invokevirtual getFloat : (Ljava/lang/Object;)F
    //   650: fstore #23
    //   652: fload #23
    //   654: putstatic lunarians/AutoEconomy.areaR : F
    //   657: invokestatic buildStages : ()V
    //   660: iconst_0
    //   661: putstatic lunarians/AutoEconomy.idx : I
    //   664: iload #7
    //   666: putstatic lunarians/AutoEconomy.adopted : Z
    //   669: iload #7
    //   671: putstatic lunarians/AutoEconomy.active : Z
    //   674: iconst_0
    //   675: iconst_0
    //   676: invokestatic load : (IZ)V
    //   679: return
    //   680: getstatic lunarians/AutoEconomy.inUnits : Z
    //   683: istore #8
    //   685: iload #8
    //   687: ifeq -> 731
    //   690: getstatic arc/util/Time.time : F
    //   693: fstore #23
    //   695: getstatic lunarians/AutoEconomy.unitsDeadline : F
    //   698: fstore_3
    //   699: fload #23
    //   701: fload_3
    //   702: fcmpl
    //   703: istore #8
    //   705: iload #8
    //   707: ifgt -> 720
    //   710: invokestatic unitsDone : ()Z
    //   713: istore #8
    //   715: iload #8
    //   717: ifeq -> 848
    //   720: invokestatic stopUnits : ()V
    //   723: iconst_0
    //   724: putstatic lunarians/AutoEconomy.inUnits : Z
    //   727: invokestatic advance : ()V
    //   730: return
    //   731: iload #5
    //   733: ifeq -> 752
    //   736: ldc_w 'stepIndex'
    //   739: astore_0
    //   740: aload_0
    //   741: invokestatic geti : (Ljava/lang/String;)I
    //   744: istore #8
    //   746: iload #8
    //   748: putstatic lunarians/AutoEconomy.lastStep : I
    //   751: return
    //   752: getstatic lunarians/AutoEconomy.sawRunning : Z
    //   755: istore #8
    //   757: iload #8
    //   759: ifeq -> 848
    //   762: iconst_0
    //   763: putstatic lunarians/AutoEconomy.sawRunning : Z
    //   766: getstatic lunarians/AutoEconomy.lastStep : I
    //   769: istore #8
    //   771: getstatic lunarians/AutoEconomy.lastTotal : I
    //   774: istore_2
    //   775: iload #8
    //   777: iload_2
    //   778: if_icmplt -> 785
    //   781: invokestatic advance : ()V
    //   784: return
    //   785: ldc_w 'Economia em ordem cancelada.'
    //   788: astore_0
    //   789: aload_0
    //   790: invokestatic toast : (Ljava/lang/String;)V
    //   793: iload #7
    //   795: putstatic lunarians/AutoEconomy.finished : Z
    //   798: invokestatic abort : ()V
    //   801: return
    //   802: astore_0
    //   803: ldc_w '[Lunarians] AutoEconomy'
    //   806: aload_0
    //   807: invokestatic err : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   810: new java/lang/StringBuilder
    //   813: astore #9
    //   815: ldc_w 'Auto All erro: '
    //   818: astore #11
    //   820: aload #9
    //   822: aload #11
    //   824: invokespecial <init> : (Ljava/lang/String;)V
    //   827: aload #9
    //   829: aload_0
    //   830: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   833: pop
    //   834: aload #9
    //   836: invokevirtual toString : ()Ljava/lang/String;
    //   839: astore_0
    //   840: aload_0
    //   841: invokestatic toast : (Ljava/lang/String;)V
    //   844: iconst_0
    //   845: invokestatic abort : (Z)V
    //   848: return
    // Exception table:
    //   from	to	target	type
    //   83	86	802	finally
    //   88	91	802	finally
    //   101	104	802	finally
    //   111	114	802	finally
    //   122	126	802	finally
    //   135	139	802	finally
    //   139	144	802	finally
    //   144	148	802	finally
    //   149	152	802	finally
    //   171	174	802	finally
    //   181	184	802	finally
    //   185	189	802	finally
    //   194	197	802	finally
    //   198	202	802	finally
    //   207	210	802	finally
    //   211	215	802	finally
    //   216	220	802	finally
    //   227	230	802	finally
    //   231	235	802	finally
    //   236	240	802	finally
    //   247	250	802	finally
    //   251	255	802	finally
    //   256	260	802	finally
    //   262	267	802	finally
    //   267	270	802	finally
    //   271	275	802	finally
    //   276	280	802	finally
    //   282	287	802	finally
    //   287	290	802	finally
    //   291	295	802	finally
    //   315	318	802	finally
    //   327	330	802	finally
    //   334	337	802	finally
    //   341	344	802	finally
    //   353	356	802	finally
    //   363	366	802	finally
    //   373	376	802	finally
    //   389	394	802	finally
    //   398	403	802	finally
    //   414	417	802	finally
    //   444	449	802	finally
    //   453	458	802	finally
    //   460	465	802	finally
    //   465	468	802	finally
    //   469	473	802	finally
    //   475	478	802	finally
    //   480	485	802	finally
    //   494	498	802	finally
    //   499	503	802	finally
    //   508	513	802	finally
    //   518	522	802	finally
    //   522	525	802	finally
    //   525	528	802	finally
    //   528	532	802	finally
    //   532	537	802	finally
    //   537	542	802	finally
    //   542	546	802	finally
    //   546	550	802	finally
    //   551	556	802	finally
    //   580	585	802	finally
    //   586	590	802	finally
    //   591	594	802	finally
    //   610	614	802	finally
    //   616	621	802	finally
    //   625	629	802	finally
    //   631	636	802	finally
    //   640	644	802	finally
    //   646	650	802	finally
    //   652	657	802	finally
    //   657	660	802	finally
    //   660	664	802	finally
    //   664	669	802	finally
    //   669	674	802	finally
    //   675	679	802	finally
    //   680	683	802	finally
    //   690	693	802	finally
    //   695	698	802	finally
    //   710	713	802	finally
    //   720	723	802	finally
    //   723	727	802	finally
    //   727	730	802	finally
    //   740	744	802	finally
    //   746	751	802	finally
    //   752	755	802	finally
    //   762	766	802	finally
    //   766	769	802	finally
    //   771	774	802	finally
    //   781	784	802	finally
    //   789	793	802	finally
    //   793	798	802	finally
    //   798	801	802	finally
  }
}
