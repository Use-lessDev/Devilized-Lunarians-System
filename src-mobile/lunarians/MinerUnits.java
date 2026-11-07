package lunarians;

import arc.Core;
import arc.Events;
import arc.Settings;
import arc.scene.Element;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import arc.util.Time;
import java.util.HashSet;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.UnitStance;
import mindustry.ai.types.CommandAI;
import mindustry.core.ContentLoader;
import mindustry.core.GameState;
import mindustry.core.UI;
import mindustry.entities.units.UnitController;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.ui.dialogs.BaseDialog;

public class MinerUnits {
  public static final String[] TYPES = new String[] { "mono", "poly", "mega", "quasar" };
  
  private static final HashSet mine;
  
  private static float next;
  
  private static boolean poor;
  
  private static boolean registered;
  
  static {
    HashSet hashSet = new HashSet();
    this();
    mine = hashSet;
  }
  
  public static int cap() {
    int i = Core.settings.getInt("lunarians-mu-cap", 8);
    i = Math.min(40, i);
    return Math.max(1, i);
  }
  
  public static BaseDialog dialog() {
    BaseDialog baseDialog = new BaseDialog();
    String str = title();
    this(str);
    Runnable[] arrayOfRunnable = new Runnable[1];
    MinerUnits$1 minerUnits$1 = new MinerUnits$1();
    this(baseDialog, arrayOfRunnable);
    arrayOfRunnable[0] = minerUnits$1;
    arrayOfRunnable[0].run();
    baseDialog.addCloseButton();
    MinerUnits$2 minerUnits$2 = new MinerUnits$2();
    this();
    baseDialog.hidden(minerUnits$2);
    return baseDialog;
  }
  
  public static boolean enabled() {
    return Core.settings.getBool("lunarians-mu-on", false);
  }
  
  private static void factories(int paramInt) {
    // Byte code:
    //   0: iconst_0
    //   1: invokestatic typeOn : (I)Z
    //   4: istore_1
    //   5: iconst_1
    //   6: istore_2
    //   7: iload_1
    //   8: ifne -> 34
    //   11: iload_2
    //   12: invokestatic typeOn : (I)Z
    //   15: istore_1
    //   16: iload_1
    //   17: ifne -> 34
    //   20: iconst_2
    //   21: invokestatic typeOn : (I)Z
    //   24: istore_1
    //   25: iload_1
    //   26: ifne -> 34
    //   29: iconst_0
    //   30: istore_1
    //   31: goto -> 36
    //   34: iconst_1
    //   35: istore_1
    //   36: iconst_3
    //   37: invokestatic typeOn : (I)Z
    //   40: istore_3
    //   41: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   44: invokevirtual closestCore : ()Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;
    //   47: getfield items : Lmindustry/world/modules/ItemModule;
    //   50: astore #4
    //   52: getstatic mindustry/content/Items.silicon : Lmindustry/type/Item;
    //   55: astore #5
    //   57: aload #4
    //   59: aload #5
    //   61: invokevirtual get : (Lmindustry/type/Item;)I
    //   64: istore #6
    //   66: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   69: invokevirtual closestCore : ()Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;
    //   72: getfield items : Lmindustry/world/modules/ItemModule;
    //   75: astore #5
    //   77: getstatic mindustry/content/Items.lead : Lmindustry/type/Item;
    //   80: astore #7
    //   82: aload #5
    //   84: aload #7
    //   86: invokevirtual get : (Lmindustry/type/Item;)I
    //   89: istore #8
    //   91: bipush #30
    //   93: istore #9
    //   95: iload #6
    //   97: iload #9
    //   99: if_icmplt -> 142
    //   102: bipush #20
    //   104: istore #9
    //   106: iload #8
    //   108: iload #9
    //   110: if_icmplt -> 142
    //   113: bipush #100
    //   115: istore #9
    //   117: iload #6
    //   119: iload #9
    //   121: if_icmplt -> 146
    //   124: bipush #60
    //   126: istore #6
    //   128: iload #8
    //   130: iload #6
    //   132: if_icmplt -> 146
    //   135: iconst_0
    //   136: putstatic lunarians/MinerUnits.poor : Z
    //   139: goto -> 146
    //   142: iload_2
    //   143: putstatic lunarians/MinerUnits.poor : Z
    //   146: invokestatic cap : ()I
    //   149: istore #6
    //   151: iload_0
    //   152: iload #6
    //   154: if_icmpge -> 167
    //   157: getstatic lunarians/MinerUnits.poor : Z
    //   160: istore_0
    //   161: iload_0
    //   162: ifne -> 167
    //   165: iconst_0
    //   166: istore_2
    //   167: getstatic mindustry/gen/Groups.build : Lmindustry/entities/EntityGroup;
    //   170: invokevirtual iterator : ()Ljava/util/Iterator;
    //   173: astore #10
    //   175: aload #10
    //   177: invokeinterface hasNext : ()Z
    //   182: istore #6
    //   184: iload #6
    //   186: ifeq -> 627
    //   189: aload #10
    //   191: invokeinterface next : ()Ljava/lang/Object;
    //   196: checkcast mindustry/gen/Building
    //   199: astore #4
    //   201: aload #4
    //   203: getfield team : Lmindustry/game/Team;
    //   206: astore #5
    //   208: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   211: invokevirtual team : ()Lmindustry/game/Team;
    //   214: astore #7
    //   216: aload #5
    //   218: aload #7
    //   220: if_acmpne -> 175
    //   223: aload #4
    //   225: instanceof mindustry/world/blocks/units/UnitFactory$UnitFactoryBuild
    //   228: istore #8
    //   230: iload #8
    //   232: ifeq -> 175
    //   235: aload #4
    //   237: checkcast mindustry/world/blocks/units/UnitFactory$UnitFactoryBuild
    //   240: astore #4
    //   242: aload #4
    //   244: getfield block : Lmindustry/world/Block;
    //   247: checkcast mindustry/world/blocks/units/UnitFactory
    //   250: astore #5
    //   252: aload #5
    //   254: getfield name : Ljava/lang/String;
    //   257: astore #7
    //   259: aload #7
    //   261: ldc 'air-factory'
    //   263: invokevirtual equals : (Ljava/lang/Object;)Z
    //   266: istore #9
    //   268: aconst_null
    //   269: astore #11
    //   271: iload #9
    //   273: ifeq -> 287
    //   276: iload_1
    //   277: ifeq -> 320
    //   280: ldc 'mono'
    //   282: astore #11
    //   284: goto -> 320
    //   287: aload #5
    //   289: getfield name : Ljava/lang/String;
    //   292: astore #7
    //   294: ldc 'ground-factory'
    //   296: astore #12
    //   298: aload #7
    //   300: aload #12
    //   302: invokevirtual equals : (Ljava/lang/Object;)Z
    //   305: istore #9
    //   307: iload #9
    //   309: ifeq -> 320
    //   312: iload_3
    //   313: ifeq -> 320
    //   316: ldc 'nova'
    //   318: astore #11
    //   320: aload #11
    //   322: ifnull -> 175
    //   325: aload #4
    //   327: invokevirtual pos : ()I
    //   330: istore #9
    //   332: iconst_m1
    //   333: istore #13
    //   335: iload_2
    //   336: ifeq -> 403
    //   339: getstatic lunarians/MinerUnits.mine : Ljava/util/HashSet;
    //   342: astore #5
    //   344: iload #9
    //   346: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   349: astore #7
    //   351: aload #5
    //   353: aload #7
    //   355: invokevirtual remove : (Ljava/lang/Object;)Z
    //   358: istore #8
    //   360: iload #8
    //   362: ifeq -> 175
    //   365: aload #4
    //   367: getfield currentPlan : I
    //   370: istore #8
    //   372: iload #8
    //   374: iload #13
    //   376: if_icmpeq -> 175
    //   379: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   382: astore #5
    //   384: iload #13
    //   386: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   389: astore #7
    //   391: aload #5
    //   393: aload #4
    //   395: aload #7
    //   397: invokestatic tileConfig : (Lmindustry/gen/Player;Lmindustry/gen/Building;Ljava/lang/Object;)V
    //   400: goto -> 175
    //   403: aload #4
    //   405: getfield currentPlan : I
    //   408: istore #14
    //   410: iload #14
    //   412: iload #13
    //   414: if_icmpeq -> 443
    //   417: getstatic lunarians/MinerUnits.mine : Ljava/util/HashSet;
    //   420: astore #15
    //   422: iload #9
    //   424: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   427: astore #16
    //   429: aload #15
    //   431: aload #16
    //   433: invokevirtual contains : (Ljava/lang/Object;)Z
    //   436: istore #14
    //   438: iload #14
    //   440: ifeq -> 175
    //   443: iconst_0
    //   444: istore #14
    //   446: aconst_null
    //   447: astore #15
    //   449: aload #5
    //   451: getfield plans : Larc/struct/Seq;
    //   454: astore #16
    //   456: aload #16
    //   458: getfield size : I
    //   461: istore #17
    //   463: iload #14
    //   465: iload #17
    //   467: if_icmpge -> 541
    //   470: aload #5
    //   472: getfield plans : Larc/struct/Seq;
    //   475: iload #14
    //   477: invokevirtual get : (I)Ljava/lang/Object;
    //   480: checkcast mindustry/world/blocks/units/UnitFactory$UnitPlan
    //   483: getfield unit : Lmindustry/type/UnitType;
    //   486: astore #16
    //   488: aload #16
    //   490: ifnull -> 532
    //   493: aload #5
    //   495: getfield plans : Larc/struct/Seq;
    //   498: iload #14
    //   500: invokevirtual get : (I)Ljava/lang/Object;
    //   503: checkcast mindustry/world/blocks/units/UnitFactory$UnitPlan
    //   506: getfield unit : Lmindustry/type/UnitType;
    //   509: getfield name : Ljava/lang/String;
    //   512: astore #16
    //   514: aload #11
    //   516: aload #16
    //   518: invokevirtual equals : (Ljava/lang/Object;)Z
    //   521: istore #17
    //   523: iload #17
    //   525: ifeq -> 532
    //   528: iload #14
    //   530: istore #13
    //   532: iload #14
    //   534: iconst_1
    //   535: iadd
    //   536: istore #14
    //   538: goto -> 449
    //   541: iload #13
    //   543: iflt -> 175
    //   546: aload #4
    //   548: getfield currentPlan : I
    //   551: istore #8
    //   553: iload #8
    //   555: iload #13
    //   557: if_icmpeq -> 604
    //   560: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   563: astore #5
    //   565: iload #13
    //   567: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   570: astore #11
    //   572: aload #5
    //   574: aload #4
    //   576: aload #11
    //   578: invokestatic tileConfig : (Lmindustry/gen/Player;Lmindustry/gen/Building;Ljava/lang/Object;)V
    //   581: getstatic lunarians/MinerUnits.mine : Ljava/util/HashSet;
    //   584: astore #4
    //   586: iload #9
    //   588: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   591: astore #5
    //   593: aload #4
    //   595: aload #5
    //   597: invokevirtual add : (Ljava/lang/Object;)Z
    //   600: pop
    //   601: goto -> 175
    //   604: getstatic lunarians/MinerUnits.mine : Ljava/util/HashSet;
    //   607: astore #4
    //   609: iload #9
    //   611: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   614: astore #5
    //   616: aload #4
    //   618: aload #5
    //   620: invokevirtual add : (Ljava/lang/Object;)Z
    //   623: pop
    //   624: goto -> 175
    //   627: return
  }
  
  public static void init() {
    boolean bool = registered;
    if (bool)
      return; 
    registered = true;
    EventType.Trigger trigger = EventType.Trigger.update;
    MinerUnits$3 minerUnits$3 = new MinerUnits$3();
    this();
    Events.run(trigger, minerUnits$3);
    MinerUnits$4 minerUnits$4 = new MinerUnits$4();
    this();
    Events.on(EventType.WorldLoadEvent.class, minerUnits$4);
  }
  
  public static boolean produce() {
    return Core.settings.getBool("lunarians-mu-prod", true);
  }
  
  public static void setCap(int paramInt) {
    Settings settings = Core.settings;
    paramInt = Math.min(40, paramInt);
    Integer integer = Integer.valueOf(Math.max(1, paramInt));
    settings.put("lunarians-mu-cap", integer);
  }
  
  public static void setEnabled(boolean paramBoolean) {
    Settings settings = Core.settings;
    Boolean bool = Boolean.valueOf(paramBoolean);
    settings.put("lunarians-mu-on", bool);
  }
  
  public static void setProduce(boolean paramBoolean) {
    Settings settings = Core.settings;
    Boolean bool = Boolean.valueOf(paramBoolean);
    settings.put("lunarians-mu-prod", bool);
  }
  
  public static void setTypeOn(int paramInt, boolean paramBoolean) {
    Settings settings = Core.settings;
    StringBuilder stringBuilder = new StringBuilder();
    this("lunarians-mu-t-");
    String str = TYPES[paramInt];
    stringBuilder.append(str);
    str = stringBuilder.toString();
    Boolean bool = Boolean.valueOf(paramBoolean);
    settings.put(str, bool);
  }
  
  public static String title() {
    return tr("Mineradores", "Miner units", "Unidades mineras", "Шахтёры");
  }
  
  private static String tr(String paramString1, String paramString2, String paramString3, String paramString4) {
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
  
  private static int typeIndex(UnitType paramUnitType) {
    int i = 0;
    while (true) {
      String[] arrayOfString = TYPES;
      int j = arrayOfString.length;
      if (i < j) {
        if (paramUnitType != null) {
          String str1 = arrayOfString[i];
          String str2 = paramUnitType.name;
          boolean bool = str1.equals(str2);
          if (bool)
            return i; 
        } 
        i++;
        continue;
      } 
      return -1;
    } 
  }
  
  public static boolean typeOn(int paramInt) {
    Settings settings = Core.settings;
    StringBuilder stringBuilder = new StringBuilder();
    this("lunarians-mu-t-");
    String str = TYPES[paramInt];
    stringBuilder.append(str);
    str = stringBuilder.toString();
    return settings.getBool(str, true);
  }
  
  private static void update() {
    boolean bool = enabled();
    if (bool) {
      GameState gameState = Vars.state;
      if (gameState != null) {
        gameState = Vars.state;
        bool = gameState.isGame();
        if (bool) {
          Player player = Vars.player;
          if (player != null) {
            float f1 = Time.time;
            float f2 = next;
            int i = f1 cmp f2;
            if (i >= 0) {
              f1 = Time.time;
              f2 = 120.0F;
              f1 += f2;
              next = f1;
              try {
                player = Vars.player;
              } finally {
                player = null;
                String str = "[Lunarians] MinerUnits";
              } 
            } 
          } 
        } 
      } 
    } 
  }
}
