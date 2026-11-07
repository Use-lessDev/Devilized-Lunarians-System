package lunarians;

import arc.Core;
import arc.Events;
import arc.Settings;
import arc.struct.Seq;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.game.Rules;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.blocks.power.PowerGraph;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.consumers.ConsumePower;
import mindustry.world.modules.ItemModule;
import mindustry.world.modules.PowerModule;

public class PowerLink {
  private static final HashMap last;
  
  private static float next;
  
  private static boolean registered;
  
  private static final HashMap tries;
  
  static {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    this();
    tries = hashMap;
    hashMap = new HashMap<Object, Object>();
    this();
    last = hashMap;
  }
  
  private static boolean affordable(Block paramBlock, int paramInt) {
    Rules rules = Vars.state.rules;
    boolean bool = rules.infiniteResources;
    boolean bool1 = true;
    if (bool)
      return bool1; 
    CoreBlock.CoreBuild coreBuild = Vars.player.closestCore();
    if (coreBuild == null)
      return false; 
    for (ItemStack itemStack : paramBlock.requirements) {
      ItemModule itemModule = (Vars.player.closestCore()).items;
      Item item = itemStack.item;
      int i = itemModule.get(item);
      int j = itemStack.amount * paramInt;
      if (i < j)
        return false; 
    } 
    return bool1;
  }
  
  public static boolean enabled() {
    return Core.settings.getBool("lunarians-auto-pnode", true);
  }
  
  public static void init() {
    boolean bool = registered;
    if (bool)
      return; 
    registered = true;
    EventType.Trigger trigger = EventType.Trigger.update;
    PowerLink$0 powerLink$0 = new PowerLink$0();
    this();
    Events.run(trigger, powerLink$0);
    PowerLink$1 powerLink$1 = new PowerLink$1();
    this();
    Events.on(EventType.WorldLoadEvent.class, powerLink$1);
  }
  
  private static boolean occupiedByPlan(Unit paramUnit, int paramInt1, int paramInt2) {
    Iterator<BuildPlan> iterator = paramUnit.plans().iterator();
    while (true) {
      boolean bool = iterator.hasNext();
      if (bool) {
        BuildPlan buildPlan = iterator.next();
        Block block = buildPlan.block;
        if (block != null) {
          int i = Math.abs(buildPlan.x - paramInt1);
          Block block1 = buildPlan.block;
          int j = block1.size / 2;
          if (i <= j) {
            i = Math.abs(buildPlan.y - paramInt2);
            Block block2 = buildPlan.block;
            int k = block2.size / 2;
            if (i <= k)
              return true; 
          } 
        } 
        continue;
      } 
      return false;
    } 
  }
  
  private static void plan(Unit paramUnit, Building paramBuilding1, Building paramBuilding2, Block paramBlock1, Block paramBlock2) {
    // Byte code:
    //   0: aload_1
    //   1: astore #5
    //   3: aload_1
    //   4: invokevirtual tileX : ()I
    //   7: istore #6
    //   9: iload #6
    //   11: i2d
    //   12: dstore #7
    //   14: aload_1
    //   15: invokevirtual tileY : ()I
    //   18: istore #9
    //   20: iload #9
    //   22: i2d
    //   23: dstore #10
    //   25: aload_2
    //   26: invokevirtual tileX : ()I
    //   29: istore #12
    //   31: iload #12
    //   33: i2d
    //   34: dstore #13
    //   36: aload_2
    //   37: invokevirtual tileY : ()I
    //   40: istore #15
    //   42: iload #15
    //   44: i2d
    //   45: dstore #16
    //   47: dload #13
    //   49: dload #7
    //   51: dsub
    //   52: dstore #13
    //   54: dload #16
    //   56: dload #10
    //   58: dsub
    //   59: dstore #16
    //   61: dload #13
    //   63: dload #16
    //   65: invokestatic hypot : (DD)D
    //   68: dstore #18
    //   70: ldc2_w 0.01
    //   73: dstore #20
    //   75: dload #18
    //   77: dload #20
    //   79: invokestatic max : (DD)D
    //   82: dstore #22
    //   84: dload #13
    //   86: dload #22
    //   88: ddiv
    //   89: dstore #13
    //   91: dload #18
    //   93: dload #20
    //   95: invokestatic max : (DD)D
    //   98: dstore #20
    //   100: dload #16
    //   102: dload #20
    //   104: ddiv
    //   105: dstore #16
    //   107: iconst_0
    //   108: istore #24
    //   110: iconst_2
    //   111: istore #25
    //   113: ldc2_w 1.5
    //   116: dstore #26
    //   118: ldc2_w 2.0
    //   121: dstore #28
    //   123: iload #24
    //   125: iload #25
    //   127: if_icmpge -> 355
    //   130: iload #24
    //   132: ifne -> 157
    //   135: invokestatic bigNode : ()Z
    //   138: istore #25
    //   140: iload #25
    //   142: ifeq -> 157
    //   145: aload #4
    //   147: ifnull -> 157
    //   150: aload #4
    //   152: astore #30
    //   154: goto -> 160
    //   157: aload_3
    //   158: astore #30
    //   160: iconst_1
    //   161: istore #31
    //   163: aload #30
    //   165: astore #32
    //   167: aload #30
    //   169: checkcast mindustry/world/blocks/power/PowerNode
    //   172: getfield laserRange : F
    //   175: fstore #33
    //   177: dload #7
    //   179: dstore #34
    //   181: fload #33
    //   183: f2d
    //   184: dstore #7
    //   186: dload #7
    //   188: dstore #36
    //   190: dload #7
    //   192: dload #28
    //   194: dsub
    //   195: dstore #7
    //   197: dload #28
    //   199: dload #7
    //   201: invokestatic max : (DD)D
    //   204: dstore #7
    //   206: aload #5
    //   208: getfield block : Lmindustry/world/Block;
    //   211: astore #32
    //   213: aload #32
    //   215: getfield size : I
    //   218: istore #38
    //   220: dload #28
    //   222: dstore #39
    //   224: iload #38
    //   226: i2d
    //   227: dload #39
    //   229: ddiv
    //   230: dload #26
    //   232: dadd
    //   233: dstore #28
    //   235: dload #7
    //   237: dstore #41
    //   239: dload #39
    //   241: dstore #7
    //   243: dload #39
    //   245: dload #28
    //   247: invokestatic max : (DD)D
    //   250: dstore #28
    //   252: iconst_1
    //   253: istore #6
    //   255: ldc 1.4E-45
    //   257: fstore #43
    //   259: dload #18
    //   261: dload #28
    //   263: dsub
    //   264: dstore #44
    //   266: dload #36
    //   268: dload #26
    //   270: dsub
    //   271: dstore #46
    //   273: bipush #14
    //   275: istore #48
    //   277: dload #44
    //   279: dload #46
    //   281: dcmpl
    //   282: istore #38
    //   284: iload #38
    //   286: ifle -> 312
    //   289: iload #6
    //   291: iload #48
    //   293: if_icmpge -> 312
    //   296: dload #28
    //   298: dload #41
    //   300: dadd
    //   301: dstore #28
    //   303: iload #6
    //   305: iconst_1
    //   306: iadd
    //   307: istore #6
    //   309: goto -> 259
    //   312: iload #6
    //   314: iload #48
    //   316: if_icmpge -> 1267
    //   319: aload_3
    //   320: astore #49
    //   322: aload #30
    //   324: aload_3
    //   325: if_acmpeq -> 375
    //   328: aload #30
    //   330: iload #6
    //   332: invokestatic affordable : (Lmindustry/world/Block;I)Z
    //   335: istore #38
    //   337: iload #38
    //   339: ifne -> 375
    //   342: iload #24
    //   344: iconst_1
    //   345: iadd
    //   346: istore #24
    //   348: dload #34
    //   350: dstore #7
    //   352: goto -> 110
    //   355: dload #7
    //   357: dstore #34
    //   359: iconst_1
    //   360: istore #31
    //   362: aload_3
    //   363: astore #49
    //   365: aload_3
    //   366: astore #30
    //   368: iconst_1
    //   369: istore #6
    //   371: ldc 1.4E-45
    //   373: fstore #43
    //   375: aload #30
    //   377: iload #6
    //   379: invokestatic affordable : (Lmindustry/world/Block;I)Z
    //   382: istore #48
    //   384: iload #48
    //   386: ifeq -> 1267
    //   389: aload #30
    //   391: astore #49
    //   393: aload #30
    //   395: checkcast mindustry/world/blocks/power/PowerNode
    //   398: getfield laserRange : F
    //   401: f2d
    //   402: dstore #18
    //   404: ldc2_w 2.0
    //   407: dstore #20
    //   409: dload #18
    //   411: dload #20
    //   413: dsub
    //   414: dstore #18
    //   416: dload #20
    //   418: dload #18
    //   420: invokestatic max : (DD)D
    //   423: dstore #18
    //   425: new java/util/ArrayList
    //   428: astore #50
    //   430: aload #50
    //   432: invokespecial <init> : ()V
    //   435: aload #5
    //   437: getfield block : Lmindustry/world/Block;
    //   440: getfield size : I
    //   443: i2d
    //   444: dload #20
    //   446: ddiv
    //   447: dload #26
    //   449: dadd
    //   450: dstore #28
    //   452: dload #20
    //   454: dload #28
    //   456: invokestatic max : (DD)D
    //   459: dstore #20
    //   461: iconst_0
    //   462: istore #48
    //   464: aconst_null
    //   465: astore #49
    //   467: iload #48
    //   469: iload #6
    //   471: if_icmpge -> 560
    //   474: dload #13
    //   476: dload #20
    //   478: dmul
    //   479: dstore #28
    //   481: dload #34
    //   483: dload #28
    //   485: dadd
    //   486: dstore #39
    //   488: dload #16
    //   490: dload #20
    //   492: dmul
    //   493: dstore #28
    //   495: dload #10
    //   497: dload #28
    //   499: dadd
    //   500: dstore #41
    //   502: aload_0
    //   503: astore #51
    //   505: aload #30
    //   507: astore #52
    //   509: aload_0
    //   510: aload #30
    //   512: dload #39
    //   514: dload #41
    //   516: aload #50
    //   518: invokestatic snap : (Lmindustry/gen/Unit;Lmindustry/world/Block;DDLjava/util/ArrayList;)[I
    //   521: astore #30
    //   523: aload #50
    //   525: astore #53
    //   527: aload #30
    //   529: ifnull -> 1267
    //   532: aload #50
    //   534: aload #30
    //   536: invokevirtual add : (Ljava/lang/Object;)Z
    //   539: pop
    //   540: dload #20
    //   542: dload #18
    //   544: dadd
    //   545: dstore #20
    //   547: iload #48
    //   549: iconst_1
    //   550: iadd
    //   551: istore #48
    //   553: aload #52
    //   555: astore #30
    //   557: goto -> 467
    //   560: aload #30
    //   562: astore #52
    //   564: aload #50
    //   566: astore #53
    //   568: aload #50
    //   570: invokevirtual size : ()I
    //   573: iconst_m1
    //   574: iadd
    //   575: istore #48
    //   577: iload #48
    //   579: iflt -> 915
    //   582: aload #53
    //   584: iload #48
    //   586: invokevirtual get : (I)Ljava/lang/Object;
    //   589: checkcast [I
    //   592: astore #54
    //   594: new java/util/ArrayList
    //   597: astore #55
    //   599: aload #55
    //   601: invokespecial <init> : ()V
    //   604: aload #53
    //   606: invokevirtual size : ()I
    //   609: iconst_m1
    //   610: iadd
    //   611: istore #12
    //   613: iload #48
    //   615: iload #12
    //   617: if_icmpne -> 684
    //   620: new arc/math/geom/Point2
    //   623: astore #56
    //   625: aload_2
    //   626: invokevirtual tileX : ()I
    //   629: istore #57
    //   631: aload #54
    //   633: iconst_0
    //   634: iaload
    //   635: istore #15
    //   637: iload #57
    //   639: iload #15
    //   641: isub
    //   642: istore #57
    //   644: aload_2
    //   645: invokevirtual tileY : ()I
    //   648: istore #15
    //   650: aload #54
    //   652: iload #31
    //   654: iaload
    //   655: istore #58
    //   657: iload #15
    //   659: iload #58
    //   661: isub
    //   662: istore #15
    //   664: aload #56
    //   666: iload #57
    //   668: iload #15
    //   670: invokespecial <init> : (II)V
    //   673: aload #55
    //   675: aload #56
    //   677: invokevirtual add : (Ljava/lang/Object;)Z
    //   680: pop
    //   681: goto -> 764
    //   684: iload #48
    //   686: iconst_1
    //   687: iadd
    //   688: istore #12
    //   690: aload #53
    //   692: iload #12
    //   694: invokevirtual get : (I)Ljava/lang/Object;
    //   697: checkcast [I
    //   700: astore #56
    //   702: new arc/math/geom/Point2
    //   705: astore #59
    //   707: aload #56
    //   709: iconst_0
    //   710: iaload
    //   711: istore #15
    //   713: aload #54
    //   715: iconst_0
    //   716: iaload
    //   717: istore #58
    //   719: iload #15
    //   721: iload #58
    //   723: isub
    //   724: istore #15
    //   726: aload #56
    //   728: iload #31
    //   730: iaload
    //   731: istore #12
    //   733: aload #54
    //   735: iload #31
    //   737: iaload
    //   738: istore #58
    //   740: iload #12
    //   742: iload #58
    //   744: isub
    //   745: istore #12
    //   747: aload #59
    //   749: iload #15
    //   751: iload #12
    //   753: invokespecial <init> : (II)V
    //   756: aload #55
    //   758: aload #59
    //   760: invokevirtual add : (Ljava/lang/Object;)Z
    //   763: pop
    //   764: iload #48
    //   766: ifne -> 832
    //   769: new arc/math/geom/Point2
    //   772: astore #56
    //   774: aload #5
    //   776: invokevirtual tileX : ()I
    //   779: istore #57
    //   781: aload #54
    //   783: iconst_0
    //   784: iaload
    //   785: istore #15
    //   787: iload #57
    //   789: iload #15
    //   791: isub
    //   792: istore #57
    //   794: aload #5
    //   796: invokevirtual tileY : ()I
    //   799: istore #15
    //   801: aload #54
    //   803: iload #31
    //   805: iaload
    //   806: istore #58
    //   808: iload #15
    //   810: iload #58
    //   812: isub
    //   813: istore #15
    //   815: aload #56
    //   817: iload #57
    //   819: iload #15
    //   821: invokespecial <init> : (II)V
    //   824: aload #55
    //   826: aload #56
    //   828: invokevirtual add : (Ljava/lang/Object;)Z
    //   831: pop
    //   832: new mindustry/entities/units/BuildPlan
    //   835: astore #51
    //   837: aload #54
    //   839: iconst_0
    //   840: iaload
    //   841: istore #60
    //   843: aload #54
    //   845: iload #31
    //   847: iaload
    //   848: istore #61
    //   850: iconst_0
    //   851: istore #9
    //   853: aconst_null
    //   854: astore #54
    //   856: iconst_0
    //   857: anewarray arc/math/geom/Point2
    //   860: astore #56
    //   862: aload #55
    //   864: aload #56
    //   866: invokevirtual toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   869: astore #62
    //   871: aload #51
    //   873: iload #60
    //   875: iload #61
    //   877: iconst_0
    //   878: aload #52
    //   880: aload #62
    //   882: invokespecial <init> : (IIILmindustry/world/Block;Ljava/lang/Object;)V
    //   885: iconst_1
    //   886: istore #57
    //   888: aload_0
    //   889: astore #55
    //   891: aload #51
    //   893: astore #56
    //   895: aload_0
    //   896: aload #51
    //   898: iload #57
    //   900: invokevirtual addBuild : (Lmindustry/entities/units/BuildPlan;Z)V
    //   903: iload #48
    //   905: iconst_m1
    //   906: iadd
    //   907: istore #48
    //   909: iconst_1
    //   910: istore #31
    //   912: goto -> 577
    //   915: getstatic mindustry/Vars.ui : Lmindustry/core/UI;
    //   918: astore #49
    //   920: aload #49
    //   922: ifnull -> 1267
    //   925: invokestatic getLanguage : ()Ljava/lang/String;
    //   928: astore #49
    //   930: getstatic mindustry/Vars.ui : Lmindustry/core/UI;
    //   933: astore #54
    //   935: ldc_w 'pt'
    //   938: astore #55
    //   940: aload #55
    //   942: aload #49
    //   944: invokevirtual equals : (Ljava/lang/Object;)Z
    //   947: istore #63
    //   949: ldc_w ' ('
    //   952: astore #56
    //   954: iload #63
    //   956: ifeq -> 1033
    //   959: new java/lang/StringBuilder
    //   962: astore #49
    //   964: ldc_w 'Auto energia: ligando '
    //   967: astore #55
    //   969: aload #49
    //   971: aload #55
    //   973: invokespecial <init> : (Ljava/lang/String;)V
    //   976: aload #5
    //   978: getfield block : Lmindustry/world/Block;
    //   981: getfield localizedName : Ljava/lang/String;
    //   984: astore #5
    //   986: aload #49
    //   988: aload #5
    //   990: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   993: pop
    //   994: aload #49
    //   996: aload #56
    //   998: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1001: pop
    //   1002: aload #49
    //   1004: iload #6
    //   1006: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1009: pop
    //   1010: ldc_w ' nos)'
    //   1013: astore #5
    //   1015: aload #49
    //   1017: aload #5
    //   1019: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1022: pop
    //   1023: aload #49
    //   1025: invokevirtual toString : ()Ljava/lang/String;
    //   1028: astore #5
    //   1030: goto -> 1248
    //   1033: ldc_w 'es'
    //   1036: astore #55
    //   1038: aload #55
    //   1040: aload #49
    //   1042: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1045: istore #63
    //   1047: iload #63
    //   1049: ifeq -> 1111
    //   1052: new java/lang/StringBuilder
    //   1055: astore #49
    //   1057: ldc_w 'Auto energía: conectando '
    //   1060: astore #55
    //   1062: aload #49
    //   1064: aload #55
    //   1066: invokespecial <init> : (Ljava/lang/String;)V
    //   1069: aload #5
    //   1071: getfield block : Lmindustry/world/Block;
    //   1074: getfield localizedName : Ljava/lang/String;
    //   1077: astore #5
    //   1079: aload #49
    //   1081: aload #5
    //   1083: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1086: pop
    //   1087: aload #49
    //   1089: aload #56
    //   1091: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1094: pop
    //   1095: aload #49
    //   1097: iload #6
    //   1099: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1102: pop
    //   1103: ldc_w ' nodos)'
    //   1106: astore #5
    //   1108: goto -> 1015
    //   1111: ldc_w 'ru'
    //   1114: astore #55
    //   1116: aload #55
    //   1118: aload #49
    //   1120: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1123: istore #48
    //   1125: iload #48
    //   1127: ifeq -> 1189
    //   1130: new java/lang/StringBuilder
    //   1133: astore #49
    //   1135: ldc_w 'Автоэнергия: '
    //   1138: astore #55
    //   1140: aload #49
    //   1142: aload #55
    //   1144: invokespecial <init> : (Ljava/lang/String;)V
    //   1147: aload #5
    //   1149: getfield block : Lmindustry/world/Block;
    //   1152: getfield localizedName : Ljava/lang/String;
    //   1155: astore #5
    //   1157: aload #49
    //   1159: aload #5
    //   1161: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1164: pop
    //   1165: aload #49
    //   1167: aload #56
    //   1169: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1172: pop
    //   1173: aload #49
    //   1175: iload #6
    //   1177: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1180: pop
    //   1181: ldc_w ')'
    //   1184: astore #5
    //   1186: goto -> 1015
    //   1189: new java/lang/StringBuilder
    //   1192: astore #49
    //   1194: ldc_w 'Auto power: linking '
    //   1197: astore #55
    //   1199: aload #49
    //   1201: aload #55
    //   1203: invokespecial <init> : (Ljava/lang/String;)V
    //   1206: aload #5
    //   1208: getfield block : Lmindustry/world/Block;
    //   1211: getfield localizedName : Ljava/lang/String;
    //   1214: astore #5
    //   1216: aload #49
    //   1218: aload #5
    //   1220: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1223: pop
    //   1224: aload #49
    //   1226: aload #56
    //   1228: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1231: pop
    //   1232: aload #49
    //   1234: iload #6
    //   1236: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1239: pop
    //   1240: ldc_w ' nodes)'
    //   1243: astore #5
    //   1245: goto -> 1015
    //   1248: ldc_w 1077936128
    //   1251: istore #6
    //   1253: ldc_w 3.0
    //   1256: fstore #43
    //   1258: aload #54
    //   1260: aload #5
    //   1262: fload #43
    //   1264: invokevirtual showInfoToast : (Ljava/lang/String;F)V
    //   1267: return
  }
  
  public static void setEnabled(boolean paramBoolean) {
    Settings settings = Core.settings;
    Boolean bool = Boolean.valueOf(paramBoolean);
    settings.put("lunarians-auto-pnode", bool);
  }
  
  private static int[] snap(Unit paramUnit, Block paramBlock, double paramDouble1, double paramDouble2, ArrayList paramArrayList) {
    long l1 = Math.round(paramDouble1);
    int i = (int)l1;
    long l2 = Math.round(paramDouble2);
    int j = (int)l2;
    int k = 0;
    while (true) {
      byte b = 3;
      if (k <= b) {
        b = -k;
        int m;
        for (m = b; m <= k; m++) {
          int n;
          for (n = b; n <= k; n++) {
            int i1 = Math.abs(n);
            int i2 = Math.abs(m);
            i1 = Math.max(i1, i2);
            if (i1 == k) {
              i1 = i + n;
              i2 = j + m;
              boolean bool = occupiedByPlan(paramUnit, i1, i2);
              Iterator<int[]> iterator = paramArrayList.iterator();
              while (true) {
                boolean bool1 = iterator.hasNext();
                if (bool1) {
                  int[] arrayOfInt = iterator.next();
                  int i3 = Math.abs(arrayOfInt[0] - i1);
                  byte b1 = 1;
                  if (i3 <= b1) {
                    int i4 = Math.abs(arrayOfInt[b1] - i2);
                    if (i4 <= b1)
                      bool = true; 
                  } 
                  continue;
                } 
                if (!bool) {
                  Team team = Vars.player.team();
                  bool = Build.validPlace(paramBlock, team, i1, i2, 0);
                  if (bool)
                    return new int[] { i1, i2 }; 
                } 
                break;
              } 
            } 
          } 
        } 
        k++;
        continue;
      } 
      return null;
    } 
  }
  
  private static boolean unpowered(Building paramBuilding) {
    PowerModule powerModule = paramBuilding.power;
    if (powerModule != null) {
      ConsumePower consumePower = paramBuilding.block.consPower;
      if (consumePower != null) {
        consumePower = paramBuilding.block.consPower;
        boolean bool = consumePower.buffered;
        if (!bool) {
          consumePower = paramBuilding.block.consPower;
          float f = consumePower.usage;
          int i = f cmp 0.0F;
          if (i > 0) {
            Block block = paramBuilding.block;
            boolean bool1 = block.outputsPower;
            if (!bool1) {
              PowerGraph powerGraph = paramBuilding.power.graph;
              if (powerGraph != null) {
                Seq seq = paramBuilding.power.graph.producers;
                int j = seq.size;
                if (j == 0) {
                  Seq seq1 = paramBuilding.power.graph.batteries;
                  int k = seq1.size;
                  if (k == 0)
                    return true; 
                } 
              } 
            } 
          } 
        } 
      } 
    } 
    return false;
  }
  
  private static void update() {
    // Byte code:
    //   0: invokestatic enabled : ()Z
    //   3: istore_0
    //   4: iload_0
    //   5: ifeq -> 802
    //   8: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   11: astore_1
    //   12: aload_1
    //   13: ifnull -> 802
    //   16: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   19: astore_1
    //   20: aload_1
    //   21: invokevirtual isGame : ()Z
    //   24: istore_0
    //   25: iload_0
    //   26: ifeq -> 802
    //   29: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   32: astore_1
    //   33: aload_1
    //   34: ifnull -> 802
    //   37: getstatic arc/util/Time.time : F
    //   40: fstore_2
    //   41: getstatic lunarians/PowerLink.next : F
    //   44: fstore_3
    //   45: fload_2
    //   46: fload_3
    //   47: fcmpg
    //   48: istore_0
    //   49: iload_0
    //   50: iflt -> 802
    //   53: getstatic arc/util/Time.time : F
    //   56: fstore_2
    //   57: ldc_w 1133903872
    //   60: istore #4
    //   62: ldc_w 300.0
    //   65: fstore_3
    //   66: fload_2
    //   67: fload_3
    //   68: fadd
    //   69: fstore_2
    //   70: fload_2
    //   71: putstatic lunarians/PowerLink.next : F
    //   74: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   77: astore_1
    //   78: aload_1
    //   79: invokevirtual unit : ()Lmindustry/gen/Unit;
    //   82: astore_1
    //   83: aload_1
    //   84: ifnull -> 802
    //   87: aload_1
    //   88: getfield dead : Z
    //   91: istore #4
    //   93: iload #4
    //   95: ifne -> 802
    //   98: aload_1
    //   99: invokevirtual canBuild : ()Z
    //   102: istore #4
    //   104: iload #4
    //   106: ifeq -> 802
    //   109: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   112: astore #5
    //   114: aload #5
    //   116: invokevirtual closestCore : ()Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;
    //   119: astore #5
    //   121: aload #5
    //   123: ifnull -> 802
    //   126: aload_1
    //   127: invokevirtual plans : ()Larc/struct/Queue;
    //   130: astore #5
    //   132: aload #5
    //   134: getfield size : I
    //   137: istore #4
    //   139: iconst_1
    //   140: istore #6
    //   142: iload #4
    //   144: iload #6
    //   146: if_icmpgt -> 802
    //   149: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   152: astore #5
    //   154: ldc_w 'power-node'
    //   157: astore #7
    //   159: aload #5
    //   161: aload #7
    //   163: invokevirtual block : (Ljava/lang/String;)Lmindustry/world/Block;
    //   166: astore #5
    //   168: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   171: astore #7
    //   173: ldc_w 'large-power-node'
    //   176: astore #8
    //   178: aload #7
    //   180: aload #8
    //   182: invokevirtual block : (Ljava/lang/String;)Lmindustry/world/Block;
    //   185: astore #7
    //   187: aload #5
    //   189: ifnull -> 802
    //   192: getstatic mindustry/gen/Groups.build : Lmindustry/entities/EntityGroup;
    //   195: astore #8
    //   197: aload #8
    //   199: invokevirtual iterator : ()Ljava/util/Iterator;
    //   202: astore #8
    //   204: ldc_w 1315859240
    //   207: istore #9
    //   209: ldc_w 1.0E9
    //   212: fstore #10
    //   214: aconst_null
    //   215: astore #11
    //   217: aconst_null
    //   218: astore #12
    //   220: ldc_w 1315859240
    //   223: istore #13
    //   225: ldc_w 1.0E9
    //   228: fstore #14
    //   230: aload #8
    //   232: invokeinterface hasNext : ()Z
    //   237: istore #15
    //   239: ldc_w 1090519040
    //   242: istore #16
    //   244: ldc_w 8.0
    //   247: fstore #17
    //   249: iconst_0
    //   250: istore #18
    //   252: fconst_0
    //   253: fstore #19
    //   255: iload #15
    //   257: ifeq -> 458
    //   260: aload #8
    //   262: invokeinterface next : ()Ljava/lang/Object;
    //   267: astore #20
    //   269: aload #20
    //   271: checkcast mindustry/gen/Building
    //   274: astore #20
    //   276: aload #20
    //   278: getfield team : Lmindustry/game/Team;
    //   281: astore #21
    //   283: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   286: astore #22
    //   288: aload #22
    //   290: invokevirtual team : ()Lmindustry/game/Team;
    //   293: astore #22
    //   295: aload #21
    //   297: aload #22
    //   299: if_acmpne -> 230
    //   302: aload #20
    //   304: invokestatic unpowered : (Lmindustry/gen/Building;)Z
    //   307: istore #23
    //   309: iload #23
    //   311: ifeq -> 230
    //   314: aload #20
    //   316: aload_1
    //   317: invokevirtual dst : (Larc/math/geom/Position;)F
    //   320: fload #17
    //   322: fdiv
    //   323: fstore #24
    //   325: ldc_w 40.0
    //   328: fstore #17
    //   330: fload #24
    //   332: fload #17
    //   334: fcmpl
    //   335: istore #16
    //   337: iload #16
    //   339: ifgt -> 230
    //   342: fload #24
    //   344: fload #14
    //   346: fcmpl
    //   347: istore #16
    //   349: iload #16
    //   351: ifge -> 230
    //   354: getstatic lunarians/PowerLink.tries : Ljava/util/HashMap;
    //   357: astore #25
    //   359: aload #20
    //   361: invokevirtual pos : ()I
    //   364: istore #26
    //   366: iload #26
    //   368: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   371: astore #22
    //   373: aload #25
    //   375: aload #22
    //   377: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   380: astore #25
    //   382: aload #25
    //   384: checkcast [I
    //   387: astore #25
    //   389: aload #25
    //   391: ifnull -> 447
    //   394: aload #25
    //   396: iconst_0
    //   397: iaload
    //   398: istore #18
    //   400: iconst_2
    //   401: istore #26
    //   403: iload #18
    //   405: iload #26
    //   407: if_icmpge -> 230
    //   410: getstatic arc/util/Time.time : F
    //   413: fstore #19
    //   415: aload #25
    //   417: iload #6
    //   419: iaload
    //   420: i2f
    //   421: fstore #17
    //   423: fload #19
    //   425: fload #17
    //   427: fsub
    //   428: fstore #19
    //   430: ldc_w 1800.0
    //   433: fstore #17
    //   435: fload #19
    //   437: fload #17
    //   439: fcmpg
    //   440: istore #16
    //   442: iload #16
    //   444: iflt -> 230
    //   447: aload #20
    //   449: astore #12
    //   451: fload #24
    //   453: fstore #14
    //   455: goto -> 230
    //   458: aload #12
    //   460: ifnull -> 802
    //   463: getstatic mindustry/gen/Groups.build : Lmindustry/entities/EntityGroup;
    //   466: astore #8
    //   468: aload #8
    //   470: invokevirtual iterator : ()Ljava/util/Iterator;
    //   473: astore #8
    //   475: aload #8
    //   477: invokeinterface hasNext : ()Z
    //   482: istore #13
    //   484: iload #13
    //   486: ifeq -> 663
    //   489: aload #8
    //   491: invokeinterface next : ()Ljava/lang/Object;
    //   496: astore #27
    //   498: aload #27
    //   500: checkcast mindustry/gen/Building
    //   503: astore #27
    //   505: aload #27
    //   507: getfield team : Lmindustry/game/Team;
    //   510: astore #20
    //   512: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   515: astore #21
    //   517: aload #21
    //   519: invokevirtual team : ()Lmindustry/game/Team;
    //   522: astore #21
    //   524: aload #20
    //   526: aload #21
    //   528: if_acmpne -> 475
    //   531: aload #27
    //   533: getfield power : Lmindustry/world/modules/PowerModule;
    //   536: astore #20
    //   538: aload #20
    //   540: ifnull -> 475
    //   543: aload #27
    //   545: getfield power : Lmindustry/world/modules/PowerModule;
    //   548: astore #20
    //   550: aload #20
    //   552: getfield graph : Lmindustry/world/blocks/power/PowerGraph;
    //   555: astore #20
    //   557: aload #12
    //   559: getfield power : Lmindustry/world/modules/PowerModule;
    //   562: astore #21
    //   564: aload #21
    //   566: getfield graph : Lmindustry/world/blocks/power/PowerGraph;
    //   569: astore #21
    //   571: aload #20
    //   573: aload #21
    //   575: if_acmpeq -> 475
    //   578: aload #27
    //   580: getfield power : Lmindustry/world/modules/PowerModule;
    //   583: astore #20
    //   585: aload #20
    //   587: getfield graph : Lmindustry/world/blocks/power/PowerGraph;
    //   590: astore #20
    //   592: aload #20
    //   594: getfield producers : Larc/struct/Seq;
    //   597: astore #20
    //   599: aload #20
    //   601: getfield size : I
    //   604: istore #15
    //   606: iload #15
    //   608: ifeq -> 475
    //   611: aload #27
    //   613: aload #12
    //   615: invokevirtual dst : (Larc/math/geom/Position;)F
    //   618: fload #17
    //   620: fdiv
    //   621: fstore #28
    //   623: fload #28
    //   625: fload #10
    //   627: fcmpg
    //   628: istore #23
    //   630: iload #23
    //   632: ifge -> 475
    //   635: ldc_w 60.0
    //   638: fstore #24
    //   640: fload #28
    //   642: fload #24
    //   644: fcmpg
    //   645: istore #23
    //   647: iload #23
    //   649: ifgt -> 475
    //   652: aload #27
    //   654: astore #11
    //   656: fload #28
    //   658: fstore #10
    //   660: goto -> 475
    //   663: getstatic lunarians/PowerLink.tries : Ljava/util/HashMap;
    //   666: astore #8
    //   668: aload #12
    //   670: invokevirtual pos : ()I
    //   673: istore #9
    //   675: iload #9
    //   677: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   680: astore #29
    //   682: aload #8
    //   684: aload #29
    //   686: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   689: astore #29
    //   691: aload #29
    //   693: checkcast [I
    //   696: astore #29
    //   698: aload #29
    //   700: ifnonnull -> 716
    //   703: iconst_2
    //   704: newarray int
    //   706: dup
    //   707: iconst_0
    //   708: iconst_0
    //   709: iastore
    //   710: dup
    //   711: iconst_1
    //   712: iconst_0
    //   713: iastore
    //   714: astore #29
    //   716: aload #29
    //   718: iconst_0
    //   719: iaload
    //   720: iload #6
    //   722: iadd
    //   723: istore #13
    //   725: aload #29
    //   727: iconst_0
    //   728: iload #13
    //   730: iastore
    //   731: getstatic arc/util/Time.time : F
    //   734: fstore #14
    //   736: fload #14
    //   738: f2i
    //   739: istore #13
    //   741: aload #29
    //   743: iload #6
    //   745: iload #13
    //   747: iastore
    //   748: aload #12
    //   750: invokevirtual pos : ()I
    //   753: istore #6
    //   755: iload #6
    //   757: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   760: astore #30
    //   762: aload #8
    //   764: aload #30
    //   766: aload #29
    //   768: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   771: pop
    //   772: aload #11
    //   774: ifnull -> 802
    //   777: aload_1
    //   778: aload #12
    //   780: aload #11
    //   782: aload #5
    //   784: aload #7
    //   786: invokestatic plan : (Lmindustry/gen/Unit;Lmindustry/gen/Building;Lmindustry/gen/Building;Lmindustry/world/Block;Lmindustry/world/Block;)V
    //   789: return
    //   790: astore_1
    //   791: ldc_w '[Lunarians] PowerLink'
    //   794: astore #5
    //   796: aload #5
    //   798: aload_1
    //   799: invokestatic err : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   802: return
    // Exception table:
    //   from	to	target	type
    //   74	77	790	finally
    //   78	82	790	finally
    //   87	91	790	finally
    //   98	102	790	finally
    //   109	112	790	finally
    //   114	119	790	finally
    //   126	130	790	finally
    //   132	137	790	finally
    //   149	152	790	finally
    //   161	166	790	finally
    //   168	171	790	finally
    //   180	185	790	finally
    //   192	195	790	finally
    //   197	202	790	finally
    //   230	237	790	finally
    //   260	267	790	finally
    //   269	274	790	finally
    //   276	281	790	finally
    //   283	286	790	finally
    //   288	293	790	finally
    //   302	307	790	finally
    //   316	320	790	finally
    //   354	357	790	finally
    //   359	364	790	finally
    //   366	371	790	finally
    //   375	380	790	finally
    //   382	387	790	finally
    //   396	398	790	finally
    //   410	413	790	finally
    //   417	420	790	finally
    //   463	466	790	finally
    //   468	473	790	finally
    //   475	482	790	finally
    //   489	496	790	finally
    //   498	503	790	finally
    //   505	510	790	finally
    //   512	515	790	finally
    //   517	522	790	finally
    //   531	536	790	finally
    //   543	548	790	finally
    //   550	555	790	finally
    //   557	562	790	finally
    //   564	569	790	finally
    //   578	583	790	finally
    //   585	590	790	finally
    //   592	597	790	finally
    //   599	604	790	finally
    //   613	618	790	finally
    //   663	666	790	finally
    //   668	673	790	finally
    //   675	680	790	finally
    //   684	689	790	finally
    //   691	696	790	finally
    //   703	714	790	finally
    //   718	720	790	finally
    //   728	731	790	finally
    //   731	734	790	finally
    //   745	748	790	finally
    //   748	753	790	finally
    //   755	760	790	finally
    //   766	772	790	finally
    //   784	789	790	finally
  }
}
