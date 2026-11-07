package lunarians;

import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.struct.Seq;
import arc.util.Log;
import java.util.ArrayList;
import java.util.HashSet;
import mindustry.Vars;
import mindustry.content.Liquids;
import mindustry.core.World;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Rules;
import mindustry.game.Schematic;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.modules.ItemModule;

public class SteamDrill {
  static final String B64 = "bXNjaAF4nEWOzUrDQBSFT/NvgnbjQlyFulFowEUrbgW7EKwbFRchi0kzLQNpUidTtYjv4CPUZ3DhW9UnKHonTemFuZfLPec7Awd7FvxRWSheqCGbwXj/gJOzlOcVjuPV8rP7+/XT7cQnl/0eG4+T1fI7TjoJrIJNOY7uFWfT8FqKPA/PL8LBm3oKT3v9MwQZr0ZSzJQoCwTpwn+8fbgZXt0NPLQrbYomvOCSqVJiPxfPc5FFspwrLnGQSpFNeES/yuZCIchZxWWU6RA4jai9E73wBUH8WflKqqLMONytFTikBxNo0bABg4bj/jWlb3D1vVULqJHGIpUJ06ZhudiUBb26O6devfragMyN36wjtJ+CqNmeBhs1sg5eN0RTBxs6bAfVq72FrolmEYN0Ng3jH04dbjI=";
  
  static final int[] DX;
  
  static final int[] DY;
  
  public static String lastError;
  
  public static final ArrayList lastTiles;
  
  private static Schematic schem;
  
  static {
    boolean bool = true;
    byte b = -1;
    DX = new int[] { bool, 0, b, 0 };
    DY = new int[] { 0, bool, 0, b };
    ArrayList arrayList = new ArrayList();
    this();
    lastTiles = arrayList;
    lastError = "";
  }
  
  private static void addPower(Seq paramSeq, HashSet paramHashSet, boolean[] paramArrayOfboolean, BuildPlan paramBuildPlan1, BuildPlan paramBuildPlan2, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: astore #6
    //   3: aload #4
    //   5: astore #7
    //   7: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   10: ldc 'power-node'
    //   12: invokevirtual block : (Ljava/lang/String;)Lmindustry/world/Block;
    //   15: astore #8
    //   17: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   20: ldc 'large-power-node'
    //   22: invokevirtual block : (Ljava/lang/String;)Lmindustry/world/Block;
    //   25: astore #9
    //   27: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   30: astore #10
    //   32: ldc 'solar-panel'
    //   34: astore #11
    //   36: aload #10
    //   38: aload #11
    //   40: invokevirtual block : (Ljava/lang/String;)Lmindustry/world/Block;
    //   43: astore #10
    //   45: aload #8
    //   47: ifnull -> 1365
    //   50: aload #4
    //   52: ifnull -> 1365
    //   55: aload_3
    //   56: getfield x : I
    //   59: i2d
    //   60: dstore #12
    //   62: ldc2_w 0.5
    //   65: dstore #14
    //   67: dload #12
    //   69: dload #14
    //   71: dadd
    //   72: dstore #16
    //   74: aload_3
    //   75: getfield y : I
    //   78: i2d
    //   79: dload #14
    //   81: dadd
    //   82: dstore #18
    //   84: aload #4
    //   86: getfield x : I
    //   89: istore #20
    //   91: iload #20
    //   93: i2d
    //   94: dstore #12
    //   96: aload #4
    //   98: getfield y : I
    //   101: istore #21
    //   103: iload #21
    //   105: i2d
    //   106: dstore #14
    //   108: dload #12
    //   110: dload #16
    //   112: dsub
    //   113: dstore #12
    //   115: dload #14
    //   117: dload #18
    //   119: dsub
    //   120: dstore #14
    //   122: dload #16
    //   124: dstore #22
    //   126: dload #12
    //   128: dload #14
    //   130: invokestatic hypot : (DD)D
    //   133: dstore #16
    //   135: ldc2_w 0.01
    //   138: dstore #24
    //   140: dload #16
    //   142: dload #24
    //   144: dcmpg
    //   145: istore #26
    //   147: iload #26
    //   149: ifge -> 160
    //   152: ldc2_w 1.0
    //   155: dstore #12
    //   157: goto -> 167
    //   160: dload #12
    //   162: dload #16
    //   164: ddiv
    //   165: dstore #12
    //   167: iload #26
    //   169: ifge -> 178
    //   172: dconst_0
    //   173: dstore #14
    //   175: goto -> 185
    //   178: dload #14
    //   180: dload #16
    //   182: ddiv
    //   183: dstore #14
    //   185: dload #18
    //   187: dstore #27
    //   189: iconst_0
    //   190: istore #26
    //   192: aconst_null
    //   193: astore #29
    //   195: iconst_2
    //   196: istore #30
    //   198: ldc2_w 1.5
    //   201: dstore #31
    //   203: dload #22
    //   205: dstore #33
    //   207: iconst_1
    //   208: istore #35
    //   210: iload #26
    //   212: iload #30
    //   214: if_icmpge -> 423
    //   217: iload #26
    //   219: ifne -> 244
    //   222: invokestatic bigNode : ()Z
    //   225: istore #36
    //   227: iload #36
    //   229: ifeq -> 244
    //   232: aload #9
    //   234: ifnull -> 244
    //   237: aload #9
    //   239: astore #37
    //   241: goto -> 248
    //   244: aload #8
    //   246: astore #37
    //   248: ldc2_w 2.0
    //   251: dstore #38
    //   253: aload #37
    //   255: astore #40
    //   257: aload #37
    //   259: checkcast mindustry/world/blocks/power/PowerNode
    //   262: astore #40
    //   264: aload #40
    //   266: getfield laserRange : F
    //   269: f2d
    //   270: dstore #41
    //   272: aload #9
    //   274: astore #43
    //   276: aload #10
    //   278: astore #44
    //   280: dload #41
    //   282: dload #38
    //   284: dsub
    //   285: dstore #45
    //   287: dload #41
    //   289: dstore #47
    //   291: dload #38
    //   293: dstore #41
    //   295: dload #38
    //   297: dload #45
    //   299: invokestatic max : (DD)D
    //   302: dstore #45
    //   304: ldc2_w 2.5
    //   307: dstore #41
    //   309: dload #41
    //   311: dload #16
    //   313: invokestatic min : (DD)D
    //   316: dstore #49
    //   318: iconst_1
    //   319: istore #51
    //   321: dload #16
    //   323: dload #49
    //   325: dsub
    //   326: dstore #52
    //   328: dload #47
    //   330: dload #31
    //   332: dsub
    //   333: dstore #54
    //   335: dload #52
    //   337: dload #54
    //   339: dcmpl
    //   340: istore #56
    //   342: iload #56
    //   344: ifle -> 374
    //   347: bipush #40
    //   349: istore #56
    //   351: iload #51
    //   353: iload #56
    //   355: if_icmpge -> 374
    //   358: dload #49
    //   360: dload #45
    //   362: dadd
    //   363: dstore #49
    //   365: iload #51
    //   367: iconst_1
    //   368: iadd
    //   369: istore #51
    //   371: goto -> 321
    //   374: aload #37
    //   376: aload #8
    //   378: if_acmpeq -> 416
    //   381: aload #37
    //   383: iload #51
    //   385: invokestatic affordable : (Lmindustry/world/Block;I)Z
    //   388: istore #57
    //   390: iload #57
    //   392: ifne -> 416
    //   395: iload #26
    //   397: iconst_1
    //   398: iadd
    //   399: istore #26
    //   401: dload #33
    //   403: dstore #22
    //   405: aload #43
    //   407: astore #9
    //   409: aload #44
    //   411: astore #10
    //   413: goto -> 195
    //   416: aload #37
    //   418: astore #58
    //   420: goto -> 437
    //   423: aload #10
    //   425: astore #44
    //   427: aload #8
    //   429: astore #58
    //   431: iconst_0
    //   432: istore #51
    //   434: aconst_null
    //   435: astore #40
    //   437: aload #58
    //   439: astore #8
    //   441: aload #58
    //   443: checkcast mindustry/world/blocks/power/PowerNode
    //   446: astore #8
    //   448: aload #8
    //   450: getfield laserRange : F
    //   453: fstore #59
    //   455: fload #59
    //   457: f2d
    //   458: dstore #60
    //   460: ldc2_w 2.0
    //   463: dstore #62
    //   465: dload #60
    //   467: dload #62
    //   469: dsub
    //   470: dstore #60
    //   472: dload #62
    //   474: dload #60
    //   476: invokestatic max : (DD)D
    //   479: dstore #38
    //   481: new java/util/ArrayList
    //   484: astore #37
    //   486: aload #37
    //   488: invokespecial <init> : ()V
    //   491: aload #44
    //   493: ifnull -> 759
    //   496: iconst_0
    //   497: istore #56
    //   499: iconst_2
    //   500: istore #64
    //   502: ldc 2.8E-45
    //   504: fstore #59
    //   506: iload #56
    //   508: iload #64
    //   510: if_icmpge -> 759
    //   513: dload #12
    //   515: dload #62
    //   517: dmul
    //   518: dstore #45
    //   520: dload #33
    //   522: dload #45
    //   524: dadd
    //   525: dstore #45
    //   527: dload #45
    //   529: dstore #47
    //   531: iload #56
    //   533: ifne -> 544
    //   536: dload #14
    //   538: dneg
    //   539: dstore #60
    //   541: goto -> 548
    //   544: dload #14
    //   546: dstore #60
    //   548: dload #60
    //   550: dload #31
    //   552: dmul
    //   553: dstore #60
    //   555: dload #47
    //   557: dload #60
    //   559: dadd
    //   560: dstore #45
    //   562: dload #14
    //   564: dload #62
    //   566: dmul
    //   567: dstore #47
    //   569: dload #27
    //   571: dload #47
    //   573: dadd
    //   574: dstore #47
    //   576: iload #56
    //   578: ifne -> 588
    //   581: dload #12
    //   583: dstore #62
    //   585: goto -> 593
    //   588: dload #12
    //   590: dneg
    //   591: dstore #62
    //   593: dload #62
    //   595: dload #31
    //   597: dmul
    //   598: dstore #62
    //   600: dload #47
    //   602: dload #62
    //   604: dadd
    //   605: dstore #47
    //   607: iload #5
    //   609: istore #26
    //   611: aload #44
    //   613: astore #8
    //   615: dload #12
    //   617: dstore #65
    //   619: dload #47
    //   621: dstore #12
    //   623: dload #14
    //   625: dstore #47
    //   627: aload_1
    //   628: astore #67
    //   630: aload #44
    //   632: dload #45
    //   634: dload #12
    //   636: aload_2
    //   637: aload_1
    //   638: iload #5
    //   640: invokestatic snap : (Lmindustry/world/Block;DD[ZLjava/util/HashSet;I)[I
    //   643: astore #9
    //   645: aload #9
    //   647: ifnull -> 733
    //   650: new mindustry/entities/units/BuildPlan
    //   653: astore #10
    //   655: aload #9
    //   657: iconst_0
    //   658: iaload
    //   659: istore #20
    //   661: aload #9
    //   663: iload #35
    //   665: iaload
    //   666: istore #68
    //   668: aload #10
    //   670: iload #20
    //   672: iload #68
    //   674: iconst_0
    //   675: aload #44
    //   677: invokespecial <init> : (IIILmindustry/world/Block;)V
    //   680: aload #37
    //   682: aload #10
    //   684: invokevirtual add : (Ljava/lang/Object;)Z
    //   687: pop
    //   688: aload #6
    //   690: aload #10
    //   692: invokevirtual add : (Ljava/lang/Object;)Larc/struct/Seq;
    //   695: pop
    //   696: aload #9
    //   698: iload #35
    //   700: iaload
    //   701: iload #5
    //   703: imul
    //   704: istore #69
    //   706: aload #9
    //   708: iconst_0
    //   709: iaload
    //   710: istore #57
    //   712: iload #69
    //   714: iload #57
    //   716: iadd
    //   717: istore #69
    //   719: iload #69
    //   721: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   724: astore #9
    //   726: aload_1
    //   727: aload #9
    //   729: invokevirtual add : (Ljava/lang/Object;)Z
    //   732: pop
    //   733: iload #56
    //   735: iconst_1
    //   736: iadd
    //   737: istore #56
    //   739: dload #65
    //   741: dstore #12
    //   743: dload #47
    //   745: dstore #14
    //   747: ldc2_w 2.0
    //   750: dstore #62
    //   752: aload #8
    //   754: astore #44
    //   756: goto -> 499
    //   759: dload #12
    //   761: dstore #65
    //   763: dload #14
    //   765: dstore #47
    //   767: aload_1
    //   768: astore #67
    //   770: new java/util/ArrayList
    //   773: astore #7
    //   775: aload #7
    //   777: invokespecial <init> : ()V
    //   780: ldc2_w 2.5
    //   783: dload #16
    //   785: invokestatic min : (DD)D
    //   788: dstore #60
    //   790: dload #60
    //   792: dstore #16
    //   794: iconst_0
    //   795: istore #56
    //   797: iload #56
    //   799: iload #51
    //   801: if_icmpge -> 923
    //   804: dload #65
    //   806: dload #16
    //   808: dmul
    //   809: dstore #12
    //   811: dload #33
    //   813: dload #12
    //   815: dadd
    //   816: dstore #45
    //   818: dload #47
    //   820: dload #16
    //   822: dmul
    //   823: dstore #12
    //   825: dload #27
    //   827: dload #12
    //   829: dadd
    //   830: dstore #12
    //   832: iload #5
    //   834: istore #26
    //   836: aload #58
    //   838: astore #8
    //   840: aload #58
    //   842: dload #45
    //   844: dload #12
    //   846: aload_2
    //   847: aload #67
    //   849: iload #5
    //   851: invokestatic snap : (Lmindustry/world/Block;DD[ZLjava/util/HashSet;I)[I
    //   854: astore #9
    //   856: aload #9
    //   858: ifnull -> 927
    //   861: aload #7
    //   863: aload #9
    //   865: invokevirtual add : (Ljava/lang/Object;)Z
    //   868: pop
    //   869: aload #9
    //   871: iload #35
    //   873: iaload
    //   874: iload #5
    //   876: imul
    //   877: istore #69
    //   879: aload #9
    //   881: iconst_0
    //   882: iaload
    //   883: istore #57
    //   885: iload #69
    //   887: iload #57
    //   889: iadd
    //   890: istore #69
    //   892: iload #69
    //   894: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   897: astore #9
    //   899: aload #67
    //   901: aload #9
    //   903: invokevirtual add : (Ljava/lang/Object;)Z
    //   906: pop
    //   907: dload #16
    //   909: dload #38
    //   911: dadd
    //   912: dstore #16
    //   914: iload #56
    //   916: iconst_1
    //   917: iadd
    //   918: istore #56
    //   920: goto -> 797
    //   923: aload #58
    //   925: astore #8
    //   927: aload #7
    //   929: invokevirtual size : ()I
    //   932: iconst_m1
    //   933: iadd
    //   934: istore #57
    //   936: iload #57
    //   938: iflt -> 1365
    //   941: aload #7
    //   943: iload #57
    //   945: invokevirtual get : (I)Ljava/lang/Object;
    //   948: checkcast [I
    //   951: astore #10
    //   953: new java/util/ArrayList
    //   956: astore #11
    //   958: aload #11
    //   960: invokespecial <init> : ()V
    //   963: aload #7
    //   965: invokevirtual size : ()I
    //   968: iconst_m1
    //   969: iadd
    //   970: istore #68
    //   972: iload #57
    //   974: iload #68
    //   976: if_icmpne -> 1045
    //   979: new arc/math/geom/Point2
    //   982: astore #70
    //   984: aload #4
    //   986: getfield x : I
    //   989: istore #71
    //   991: aload #10
    //   993: iconst_0
    //   994: iaload
    //   995: istore #26
    //   997: iload #71
    //   999: iload #26
    //   1001: isub
    //   1002: istore #71
    //   1004: aload #4
    //   1006: getfield y : I
    //   1009: istore #26
    //   1011: aload #10
    //   1013: iload #35
    //   1015: iaload
    //   1016: istore #72
    //   1018: iload #26
    //   1020: iload #72
    //   1022: isub
    //   1023: istore #26
    //   1025: aload #70
    //   1027: iload #71
    //   1029: iload #26
    //   1031: invokespecial <init> : (II)V
    //   1034: aload #11
    //   1036: aload #70
    //   1038: invokevirtual add : (Ljava/lang/Object;)Z
    //   1041: pop
    //   1042: goto -> 1125
    //   1045: iload #57
    //   1047: iconst_1
    //   1048: iadd
    //   1049: istore #68
    //   1051: aload #7
    //   1053: iload #68
    //   1055: invokevirtual get : (I)Ljava/lang/Object;
    //   1058: checkcast [I
    //   1061: astore #70
    //   1063: new arc/math/geom/Point2
    //   1066: astore #67
    //   1068: aload #70
    //   1070: iconst_0
    //   1071: iaload
    //   1072: istore #26
    //   1074: aload #10
    //   1076: iconst_0
    //   1077: iaload
    //   1078: istore #72
    //   1080: iload #26
    //   1082: iload #72
    //   1084: isub
    //   1085: istore #26
    //   1087: aload #70
    //   1089: iload #35
    //   1091: iaload
    //   1092: istore #68
    //   1094: aload #10
    //   1096: iload #35
    //   1098: iaload
    //   1099: istore #72
    //   1101: iload #68
    //   1103: iload #72
    //   1105: isub
    //   1106: istore #68
    //   1108: aload #67
    //   1110: iload #26
    //   1112: iload #68
    //   1114: invokespecial <init> : (II)V
    //   1117: aload #11
    //   1119: aload #67
    //   1121: invokevirtual add : (Ljava/lang/Object;)Z
    //   1124: pop
    //   1125: iload #57
    //   1127: ifne -> 1293
    //   1130: new arc/math/geom/Point2
    //   1133: astore #70
    //   1135: aload_3
    //   1136: astore #67
    //   1138: aload_3
    //   1139: getfield x : I
    //   1142: istore #26
    //   1144: aload #10
    //   1146: iconst_0
    //   1147: iaload
    //   1148: istore #72
    //   1150: iload #26
    //   1152: iload #72
    //   1154: isub
    //   1155: istore #26
    //   1157: aload_3
    //   1158: getfield y : I
    //   1161: istore #72
    //   1163: aload #10
    //   1165: iload #35
    //   1167: iaload
    //   1168: istore #73
    //   1170: iload #72
    //   1172: iload #73
    //   1174: isub
    //   1175: istore #72
    //   1177: aload #70
    //   1179: iload #26
    //   1181: iload #72
    //   1183: invokespecial <init> : (II)V
    //   1186: aload #11
    //   1188: aload #70
    //   1190: invokevirtual add : (Ljava/lang/Object;)Z
    //   1193: pop
    //   1194: aload #37
    //   1196: invokevirtual iterator : ()Ljava/util/Iterator;
    //   1199: astore #70
    //   1201: aload #70
    //   1203: invokeinterface hasNext : ()Z
    //   1208: istore #26
    //   1210: iload #26
    //   1212: ifeq -> 1293
    //   1215: aload #70
    //   1217: invokeinterface next : ()Ljava/lang/Object;
    //   1222: checkcast mindustry/entities/units/BuildPlan
    //   1225: astore #29
    //   1227: new arc/math/geom/Point2
    //   1230: astore #74
    //   1232: aload #29
    //   1234: getfield x : I
    //   1237: istore #73
    //   1239: aload #10
    //   1241: iconst_0
    //   1242: iaload
    //   1243: istore #51
    //   1245: iload #73
    //   1247: iload #51
    //   1249: isub
    //   1250: istore #73
    //   1252: aload #29
    //   1254: getfield y : I
    //   1257: istore #26
    //   1259: aload #10
    //   1261: iload #35
    //   1263: iaload
    //   1264: istore #51
    //   1266: iload #26
    //   1268: iload #51
    //   1270: isub
    //   1271: istore #26
    //   1273: aload #74
    //   1275: iload #73
    //   1277: iload #26
    //   1279: invokespecial <init> : (II)V
    //   1282: aload #11
    //   1284: aload #74
    //   1286: invokevirtual add : (Ljava/lang/Object;)Z
    //   1289: pop
    //   1290: goto -> 1201
    //   1293: new mindustry/entities/units/BuildPlan
    //   1296: astore #75
    //   1298: aload #10
    //   1300: iconst_0
    //   1301: iaload
    //   1302: istore #76
    //   1304: aload #10
    //   1306: iload #35
    //   1308: iaload
    //   1309: istore #77
    //   1311: iconst_0
    //   1312: anewarray arc/math/geom/Point2
    //   1315: astore #10
    //   1317: aload #11
    //   1319: aload #10
    //   1321: invokevirtual toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   1324: astore #78
    //   1326: aload #8
    //   1328: astore #58
    //   1330: aload #75
    //   1332: iload #76
    //   1334: iload #77
    //   1336: iconst_0
    //   1337: aload #8
    //   1339: aload #78
    //   1341: invokespecial <init> : (IIILmindustry/world/Block;Ljava/lang/Object;)V
    //   1344: aload #75
    //   1346: astore #10
    //   1348: aload #6
    //   1350: aload #75
    //   1352: invokevirtual add : (Ljava/lang/Object;)Larc/struct/Seq;
    //   1355: pop
    //   1356: iload #57
    //   1358: iconst_m1
    //   1359: iadd
    //   1360: istore #57
    //   1362: goto -> 936
    //   1365: return
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
      ItemModule itemModule = coreBuild.items;
      Item item = itemStack.item;
      int i = itemModule.get(item);
      int j = itemStack.amount * paramInt;
      if (i < j)
        return false; 
    } 
    return bool1;
  }
  
  private static boolean canPlace(Block paramBlock, int paramInt1, int paramInt2, int paramInt3) {
    try {
      Player player = Vars.player;
      return Build.validPlace(paramBlock, team, paramInt1, paramInt2, paramInt3);
    } finally {
      null;
    } 
  }
  
  private static void footprint(Block paramBlock, int paramInt1, int paramInt2, HashSet<Integer> paramHashSet) {
    World world = Vars.world;
    int i = world.width();
    int j = (paramBlock.size + -1) / 2;
    int k = 0;
    while (true) {
      int m = paramBlock.size;
      if (k < m) {
        m = 0;
        while (true) {
          int n = paramBlock.size;
          if (m < n) {
            n = paramInt1 - j + m;
            int i1 = (paramInt2 - j + k) * i + n;
            Integer integer = Integer.valueOf(i1);
            paramHashSet.add(integer);
            m++;
            continue;
          } 
          k++;
        } 
      } 
      break;
    } 
  }
  
  private static boolean freeArea(Block paramBlock, int paramInt1, int paramInt2, boolean[] paramArrayOfboolean, HashSet paramHashSet) {
    World world = Vars.world;
    int i = world.width();
    int j = paramBlock.size;
    byte b = 1;
    j = (j - b) / 2;
    int k = 0;
    while (true) {
      int m = paramBlock.size;
      if (k < m) {
        m = 0;
        while (true) {
          int n = paramBlock.size;
          k++;
        } 
        break;
      } 
      return b;
    } 
  }
  
  private static boolean inside(int paramInt1, int paramInt2) {
    return Vars.world.tiles.in(paramInt1, paramInt2);
  }
  
  public static Seq plan(Unit paramUnit, int paramInt1, int paramInt2, float paramFloat, boolean[] paramArrayOfboolean) {
    lastTiles.clear();
    String str = "";
    lastError = str;
    try {
      return planInner(paramUnit, paramInt1, paramInt2, paramFloat, paramArrayOfboolean);
    } finally {
      paramUnit = null;
      Log.err("[Lunarians] SteamDrill", (Throwable)paramUnit);
      lastError = "error";
    } 
  }
  
  private static Seq planInner(Unit paramUnit, int paramInt1, int paramInt2, float paramFloat, boolean[] paramArrayOfboolean) {
    // Byte code:
    //   0: iload_1
    //   1: istore #5
    //   3: iload_2
    //   4: istore #6
    //   6: fload_3
    //   7: fstore #7
    //   9: aload #4
    //   11: astore #8
    //   13: invokestatic schematic : ()Lmindustry/game/Schematic;
    //   16: astore #9
    //   18: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   21: ldc_w 'laser-drill'
    //   24: invokevirtual block : (Ljava/lang/String;)Lmindustry/world/Block;
    //   27: astore #10
    //   29: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   32: ldc_w 'rotary-pump'
    //   35: invokevirtual block : (Ljava/lang/String;)Lmindustry/world/Block;
    //   38: astore #11
    //   40: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   43: astore #12
    //   45: ldc_w 'conduit'
    //   48: astore #13
    //   50: aload #12
    //   52: aload #13
    //   54: invokevirtual block : (Ljava/lang/String;)Lmindustry/world/Block;
    //   57: astore #12
    //   59: aload #9
    //   61: ifnull -> 4130
    //   64: aload #10
    //   66: ifnull -> 4130
    //   69: aload #11
    //   71: ifnull -> 4130
    //   74: aload #12
    //   76: ifnull -> 4130
    //   79: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   82: invokevirtual closestCore : ()Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;
    //   85: astore #14
    //   87: aload #14
    //   89: ifnull -> 4130
    //   92: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   95: astore #14
    //   97: aload #14
    //   99: invokevirtual width : ()I
    //   102: istore #15
    //   104: aload_0
    //   105: invokestatic reservedFromPlans : (Lmindustry/gen/Unit;)[Z
    //   108: astore #16
    //   110: iconst_1
    //   111: istore #17
    //   113: aload #4
    //   115: ifnull -> 176
    //   118: iconst_0
    //   119: istore #18
    //   121: aconst_null
    //   122: astore #19
    //   124: aload #8
    //   126: arraylength
    //   127: istore #20
    //   129: iload #18
    //   131: iload #20
    //   133: if_icmpge -> 176
    //   136: aload #16
    //   138: arraylength
    //   139: istore #20
    //   141: iload #18
    //   143: iload #20
    //   145: if_icmpge -> 176
    //   148: aload #8
    //   150: iload #18
    //   152: baload
    //   153: istore #20
    //   155: iload #20
    //   157: ifeq -> 167
    //   160: aload #16
    //   162: iload #18
    //   164: iload #17
    //   166: bastore
    //   167: iload #18
    //   169: iconst_1
    //   170: iadd
    //   171: istore #18
    //   173: goto -> 124
    //   176: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   179: invokevirtual closestCore : ()Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;
    //   182: astore #8
    //   184: aload #8
    //   186: invokevirtual tileX : ()I
    //   189: istore #21
    //   191: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   194: invokevirtual closestCore : ()Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;
    //   197: astore #19
    //   199: aload #19
    //   201: invokevirtual tileY : ()I
    //   204: istore #18
    //   206: invokestatic steamMinCoal : ()I
    //   209: istore #20
    //   211: new java/util/ArrayList
    //   214: astore #22
    //   216: aload #22
    //   218: invokespecial <init> : ()V
    //   221: fload #7
    //   223: f2d
    //   224: invokestatic ceil : (D)D
    //   227: dstore #23
    //   229: dload #23
    //   231: d2i
    //   232: istore #25
    //   234: iload #6
    //   236: iload #25
    //   238: isub
    //   239: istore #26
    //   241: iload #6
    //   243: iload #25
    //   245: iadd
    //   246: istore #17
    //   248: iload #26
    //   250: iload #17
    //   252: if_icmpgt -> 704
    //   255: iload #5
    //   257: iload #25
    //   259: isub
    //   260: istore #17
    //   262: iload #5
    //   264: iload #25
    //   266: iadd
    //   267: istore #27
    //   269: iload #17
    //   271: iload #27
    //   273: if_icmpgt -> 689
    //   276: iload #17
    //   278: iload #26
    //   280: invokestatic inside : (II)Z
    //   283: istore #27
    //   285: iload #27
    //   287: ifeq -> 658
    //   290: iload #17
    //   292: i2f
    //   293: fstore #7
    //   295: iload #25
    //   297: istore #28
    //   299: iload #26
    //   301: i2f
    //   302: fstore #29
    //   304: iload #15
    //   306: istore #30
    //   308: iload #5
    //   310: i2f
    //   311: fstore #31
    //   313: iload #6
    //   315: i2f
    //   316: fstore #32
    //   318: fload #7
    //   320: fload #29
    //   322: fload #31
    //   324: fload #32
    //   326: invokestatic dst : (FFFF)F
    //   329: fstore #32
    //   331: ldc_w 1073741824
    //   334: istore #15
    //   336: fload_3
    //   337: ldc_w 2.0
    //   340: fsub
    //   341: fstore #31
    //   343: fload #32
    //   345: fload #31
    //   347: fcmpl
    //   348: istore #5
    //   350: iload #5
    //   352: ifgt -> 666
    //   355: iconst_m1
    //   356: istore #5
    //   358: fconst_0
    //   359: fconst_0
    //   360: fdiv
    //   361: fstore #32
    //   363: iconst_0
    //   364: istore #15
    //   366: fconst_0
    //   367: fstore #31
    //   369: aconst_null
    //   370: astore #33
    //   372: iconst_0
    //   373: istore #34
    //   375: aconst_null
    //   376: astore #35
    //   378: iconst_1
    //   379: istore #6
    //   381: ldc_w 1.4E-45
    //   384: fstore #36
    //   386: iload #5
    //   388: iload #6
    //   390: if_icmpgt -> 566
    //   393: iload #34
    //   395: ifne -> 566
    //   398: iload #15
    //   400: istore #37
    //   402: iconst_m1
    //   403: istore #15
    //   405: fconst_0
    //   406: fconst_0
    //   407: fdiv
    //   408: fstore #31
    //   410: iload #15
    //   412: iload #6
    //   414: if_icmpgt -> 549
    //   417: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   420: astore #38
    //   422: iload #5
    //   424: istore #39
    //   426: iload #17
    //   428: iload #15
    //   430: iadd
    //   431: istore #5
    //   433: iload #15
    //   435: istore #40
    //   437: iload #26
    //   439: iload #39
    //   441: iadd
    //   442: istore #15
    //   444: aload #38
    //   446: iload #5
    //   448: iload #15
    //   450: invokevirtual tile : (II)Lmindustry/world/Tile;
    //   453: astore #41
    //   455: aload #41
    //   457: ifnull -> 543
    //   460: aload #41
    //   462: getfield build : Lmindustry/gen/Building;
    //   465: astore #38
    //   467: aload #38
    //   469: ifnonnull -> 543
    //   472: iload #15
    //   474: iload #30
    //   476: imul
    //   477: iload #17
    //   479: iadd
    //   480: iload #40
    //   482: iadd
    //   483: istore #15
    //   485: aload #16
    //   487: iload #15
    //   489: baload
    //   490: istore #6
    //   492: iload #6
    //   494: ifne -> 543
    //   497: aload #41
    //   499: invokevirtual drop : ()Lmindustry/type/Item;
    //   502: astore #41
    //   504: getstatic mindustry/content/Items.coal : Lmindustry/type/Item;
    //   507: astore #38
    //   509: aload #41
    //   511: aload #38
    //   513: if_acmpne -> 522
    //   516: iload #37
    //   518: iconst_1
    //   519: iadd
    //   520: istore #37
    //   522: iload #40
    //   524: iconst_1
    //   525: iadd
    //   526: istore #15
    //   528: iload #39
    //   530: istore #5
    //   532: iconst_1
    //   533: istore #6
    //   535: ldc_w 1.4E-45
    //   538: fstore #36
    //   540: goto -> 410
    //   543: iconst_1
    //   544: istore #34
    //   546: goto -> 553
    //   549: iload #5
    //   551: istore #39
    //   553: iload #39
    //   555: iconst_1
    //   556: iadd
    //   557: istore #5
    //   559: iload #37
    //   561: istore #15
    //   563: goto -> 378
    //   566: iload #34
    //   568: ifne -> 666
    //   571: iload #15
    //   573: iload #20
    //   575: if_icmplt -> 666
    //   578: iload #21
    //   580: i2f
    //   581: fstore #32
    //   583: iload #18
    //   585: i2f
    //   586: fstore #36
    //   588: fload #7
    //   590: fload #29
    //   592: fload #32
    //   594: fload #36
    //   596: invokestatic dst : (FFFF)F
    //   599: fstore #32
    //   601: ldc_w 1082130432
    //   604: istore #6
    //   606: ldc_w 4.0
    //   609: fstore #36
    //   611: fload #32
    //   613: fload #36
    //   615: fmul
    //   616: fstore #32
    //   618: fload #32
    //   620: f2i
    //   621: istore #5
    //   623: iconst_4
    //   624: newarray int
    //   626: dup
    //   627: dup2
    //   628: iconst_0
    //   629: iload #17
    //   631: iastore
    //   632: iconst_1
    //   633: iload #26
    //   635: iastore
    //   636: iconst_2
    //   637: iload #15
    //   639: iastore
    //   640: dup
    //   641: iconst_3
    //   642: iload #5
    //   644: iastore
    //   645: astore #41
    //   647: aload #22
    //   649: aload #41
    //   651: invokevirtual add : (Ljava/lang/Object;)Z
    //   654: pop
    //   655: goto -> 666
    //   658: iload #25
    //   660: istore #28
    //   662: iload #15
    //   664: istore #30
    //   666: iload #17
    //   668: iconst_1
    //   669: iadd
    //   670: istore #17
    //   672: iload_1
    //   673: istore #5
    //   675: iload_2
    //   676: istore #6
    //   678: iload #28
    //   680: istore #25
    //   682: iload #30
    //   684: istore #15
    //   686: goto -> 262
    //   689: iload #26
    //   691: iconst_1
    //   692: iadd
    //   693: istore #26
    //   695: iload_1
    //   696: istore #5
    //   698: iload_2
    //   699: istore #6
    //   701: goto -> 241
    //   704: iload #15
    //   706: istore #30
    //   708: iconst_m1
    //   709: istore #42
    //   711: aload #22
    //   713: invokevirtual isEmpty : ()Z
    //   716: istore #5
    //   718: iload #5
    //   720: ifeq -> 743
    //   723: ldc_w 'Sem carvão 3x3 livre'
    //   726: ldc_w 'No free 3x3 coal'
    //   729: ldc_w 'Sin carbón 3x3 libre'
    //   732: ldc_w 'Нет свободного угля 3x3'
    //   735: invokestatic tr : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   738: putstatic lunarians/SteamDrill.lastError : Ljava/lang/String;
    //   741: aconst_null
    //   742: areturn
    //   743: new lunarians/SteamDrill$1
    //   746: astore #41
    //   748: aload #41
    //   750: invokespecial <init> : ()V
    //   753: aload #22
    //   755: aload #41
    //   757: invokestatic sort : (Ljava/util/List;Ljava/util/Comparator;)V
    //   760: new java/util/HashMap
    //   763: astore #41
    //   765: aload #41
    //   767: invokespecial <init> : ()V
    //   770: iconst_0
    //   771: istore #26
    //   773: aconst_null
    //   774: astore #14
    //   776: iconst_0
    //   777: istore #17
    //   779: aconst_null
    //   780: astore #43
    //   782: iconst_0
    //   783: istore #18
    //   785: aconst_null
    //   786: astore #19
    //   788: iconst_0
    //   789: istore #6
    //   791: fconst_0
    //   792: fstore #36
    //   794: aconst_null
    //   795: astore #38
    //   797: iconst_m1
    //   798: iconst_1
    //   799: iushr
    //   800: istore #27
    //   802: fconst_0
    //   803: fconst_0
    //   804: fdiv
    //   805: fstore #7
    //   807: iconst_0
    //   808: istore #21
    //   810: aconst_null
    //   811: astore #8
    //   813: iconst_0
    //   814: istore #25
    //   816: fconst_0
    //   817: fstore #29
    //   819: aconst_null
    //   820: astore #13
    //   822: aload #22
    //   824: invokevirtual size : ()I
    //   827: istore #20
    //   829: iload #21
    //   831: iload #20
    //   833: if_icmpge -> 3666
    //   836: bipush #28
    //   838: istore #20
    //   840: iload #6
    //   842: iload #20
    //   844: if_icmpge -> 3666
    //   847: aload #22
    //   849: iload #21
    //   851: invokevirtual get : (I)Ljava/lang/Object;
    //   854: checkcast [I
    //   857: astore #44
    //   859: iconst_0
    //   860: istore #15
    //   862: fconst_0
    //   863: fstore #31
    //   865: aconst_null
    //   866: astore #33
    //   868: iload #6
    //   870: istore_2
    //   871: iconst_4
    //   872: istore #6
    //   874: ldc_w 5.6E-45
    //   877: fstore #36
    //   879: iload #15
    //   881: iload #6
    //   883: if_icmpge -> 3624
    //   886: getstatic mindustry/Vars.schematics : Lmindustry/game/Schematics;
    //   889: astore #38
    //   891: iload #21
    //   893: istore #28
    //   895: iconst_0
    //   896: istore #21
    //   898: aload #38
    //   900: aload #9
    //   902: iconst_0
    //   903: iconst_0
    //   904: invokevirtual toPlans : (Lmindustry/game/Schematic;II)Larc/struct/Seq;
    //   907: astore #38
    //   909: aload #38
    //   911: invokevirtual iterator : ()Ljava/util/Iterator;
    //   914: astore #8
    //   916: aload #8
    //   918: invokeinterface hasNext : ()Z
    //   923: istore #34
    //   925: iload #34
    //   927: ifeq -> 983
    //   930: aload #8
    //   932: invokeinterface next : ()Ljava/lang/Object;
    //   937: astore #35
    //   939: aload #8
    //   941: astore #45
    //   943: aload #35
    //   945: astore #8
    //   947: aload #35
    //   949: checkcast mindustry/entities/units/BuildPlan
    //   952: astore #8
    //   954: aload #9
    //   956: astore #35
    //   958: aload #8
    //   960: getfield block : Lmindustry/world/Block;
    //   963: astore #9
    //   965: aload #9
    //   967: aload #10
    //   969: if_acmpeq -> 993
    //   972: aload #35
    //   974: astore #9
    //   976: aload #45
    //   978: astore #8
    //   980: goto -> 916
    //   983: aload #9
    //   985: astore #35
    //   987: iconst_0
    //   988: istore #21
    //   990: aconst_null
    //   991: astore #8
    //   993: aload #8
    //   995: ifnonnull -> 1006
    //   998: ldc_w 'schematic'
    //   1001: putstatic lunarians/SteamDrill.lastError : Ljava/lang/String;
    //   1004: aconst_null
    //   1005: areturn
    //   1006: aconst_null
    //   1007: astore #9
    //   1009: aload #44
    //   1011: iconst_0
    //   1012: iaload
    //   1013: istore #37
    //   1015: aload #8
    //   1017: getfield x : I
    //   1020: istore #46
    //   1022: iload #37
    //   1024: iload #46
    //   1026: isub
    //   1027: istore #37
    //   1029: iconst_1
    //   1030: istore #47
    //   1032: aload #44
    //   1034: iload #47
    //   1036: iaload
    //   1037: istore #46
    //   1039: aload #8
    //   1041: getfield y : I
    //   1044: istore #21
    //   1046: iload #46
    //   1048: iload #21
    //   1050: isub
    //   1051: istore #46
    //   1053: aload #38
    //   1055: invokevirtual iterator : ()Ljava/util/Iterator;
    //   1058: astore #8
    //   1060: aload #8
    //   1062: invokeinterface hasNext : ()Z
    //   1067: istore #39
    //   1069: iload #39
    //   1071: ifeq -> 1147
    //   1074: aload #8
    //   1076: invokeinterface next : ()Ljava/lang/Object;
    //   1081: astore #48
    //   1083: aload #8
    //   1085: astore #49
    //   1087: aload #48
    //   1089: astore #8
    //   1091: aload #48
    //   1093: checkcast mindustry/entities/units/BuildPlan
    //   1096: astore #8
    //   1098: iload #46
    //   1100: istore #39
    //   1102: aload #8
    //   1104: getfield x : I
    //   1107: iload #37
    //   1109: iadd
    //   1110: istore #46
    //   1112: aload #8
    //   1114: iload #46
    //   1116: putfield x : I
    //   1119: aload #8
    //   1121: getfield y : I
    //   1124: iload #39
    //   1126: iadd
    //   1127: istore #46
    //   1129: aload #8
    //   1131: iload #46
    //   1133: putfield y : I
    //   1136: iload #39
    //   1138: istore #46
    //   1140: aload #49
    //   1142: astore #8
    //   1144: goto -> 1060
    //   1147: iconst_0
    //   1148: istore #21
    //   1150: aconst_null
    //   1151: astore #8
    //   1153: iload #21
    //   1155: iload #15
    //   1157: if_icmpge -> 1200
    //   1160: aconst_null
    //   1161: astore_0
    //   1162: aload #44
    //   1164: iconst_0
    //   1165: iaload
    //   1166: istore #46
    //   1168: iload #21
    //   1170: istore #37
    //   1172: iconst_1
    //   1173: istore #47
    //   1175: aload #44
    //   1177: iload #47
    //   1179: iaload
    //   1180: istore #21
    //   1182: aload #38
    //   1184: iload #46
    //   1186: iload #21
    //   1188: invokestatic rotateOnce : (Larc/struct/Seq;II)V
    //   1191: iload #37
    //   1193: iconst_1
    //   1194: iadd
    //   1195: istore #21
    //   1197: goto -> 1153
    //   1200: new java/util/HashSet
    //   1203: astore #8
    //   1205: aload #8
    //   1207: invokespecial <init> : ()V
    //   1210: aload #38
    //   1212: invokevirtual iterator : ()Ljava/util/Iterator;
    //   1215: astore #9
    //   1217: aload #9
    //   1219: invokeinterface hasNext : ()Z
    //   1224: istore #37
    //   1226: iload #37
    //   1228: ifeq -> 1449
    //   1231: aload #9
    //   1233: invokeinterface next : ()Ljava/lang/Object;
    //   1238: astore #45
    //   1240: aload #38
    //   1242: astore #48
    //   1244: aload #45
    //   1246: astore #38
    //   1248: aload #45
    //   1250: checkcast mindustry/entities/units/BuildPlan
    //   1253: astore #38
    //   1255: aload #9
    //   1257: astore #45
    //   1259: aload #38
    //   1261: getfield block : Lmindustry/world/Block;
    //   1264: astore #9
    //   1266: aload #9
    //   1268: ifnull -> 1430
    //   1271: aload #38
    //   1273: getfield block : Lmindustry/world/Block;
    //   1276: astore #9
    //   1278: aload #10
    //   1280: astore #49
    //   1282: aload #38
    //   1284: getfield x : I
    //   1287: istore #50
    //   1289: iload #25
    //   1291: istore #51
    //   1293: fload #29
    //   1295: fstore #52
    //   1297: aload #38
    //   1299: getfield y : I
    //   1302: istore #25
    //   1304: aload #14
    //   1306: astore #53
    //   1308: iconst_0
    //   1309: istore #26
    //   1311: aconst_null
    //   1312: astore #14
    //   1314: aload #9
    //   1316: iload #50
    //   1318: iload #25
    //   1320: aload #16
    //   1322: aconst_null
    //   1323: invokestatic freeArea : (Lmindustry/world/Block;II[ZLjava/util/HashSet;)Z
    //   1326: istore #46
    //   1328: iload #46
    //   1330: ifeq -> 1568
    //   1333: aload #38
    //   1335: getfield block : Lmindustry/world/Block;
    //   1338: astore #9
    //   1340: aload #38
    //   1342: getfield x : I
    //   1345: istore #50
    //   1347: aload #38
    //   1349: getfield y : I
    //   1352: istore #25
    //   1354: aload #38
    //   1356: getfield rotation : I
    //   1359: istore #26
    //   1361: aload #9
    //   1363: iload #50
    //   1365: iload #25
    //   1367: iload #26
    //   1369: invokestatic canPlace : (Lmindustry/world/Block;III)Z
    //   1372: istore #46
    //   1374: iload #46
    //   1376: ifeq -> 1568
    //   1379: aload #38
    //   1381: getfield block : Lmindustry/world/Block;
    //   1384: astore #9
    //   1386: aload #38
    //   1388: getfield x : I
    //   1391: istore #50
    //   1393: aload #38
    //   1395: getfield y : I
    //   1398: istore #6
    //   1400: aload #9
    //   1402: iload #50
    //   1404: iload #6
    //   1406: aload #8
    //   1408: invokestatic footprint : (Lmindustry/world/Block;IILjava/util/HashSet;)V
    //   1411: aload #45
    //   1413: astore #9
    //   1415: aload #48
    //   1417: astore #38
    //   1419: iload #51
    //   1421: istore #25
    //   1423: aload #53
    //   1425: astore #14
    //   1427: goto -> 1217
    //   1430: aload #10
    //   1432: astore #49
    //   1434: iload #25
    //   1436: istore #51
    //   1438: fload #29
    //   1440: fstore #52
    //   1442: aload #14
    //   1444: astore #53
    //   1446: goto -> 1568
    //   1449: aload #38
    //   1451: astore #48
    //   1453: aload #10
    //   1455: astore #49
    //   1457: iload #25
    //   1459: istore #51
    //   1461: fload #29
    //   1463: fstore #52
    //   1465: aload #14
    //   1467: astore #53
    //   1469: aload #38
    //   1471: invokevirtual iterator : ()Ljava/util/Iterator;
    //   1474: astore #38
    //   1476: iconst_0
    //   1477: istore #46
    //   1479: aconst_null
    //   1480: astore #9
    //   1482: iconst_0
    //   1483: istore #50
    //   1485: aconst_null
    //   1486: astore #10
    //   1488: aload #38
    //   1490: invokeinterface hasNext : ()Z
    //   1495: istore #25
    //   1497: iload #25
    //   1499: ifeq -> 1563
    //   1502: aload #38
    //   1504: invokeinterface next : ()Ljava/lang/Object;
    //   1509: checkcast mindustry/entities/units/BuildPlan
    //   1512: astore #13
    //   1514: aload #13
    //   1516: getfield block : Lmindustry/world/Block;
    //   1519: astore #14
    //   1521: aload #14
    //   1523: aload #12
    //   1525: if_acmpne -> 1532
    //   1528: aload #13
    //   1530: astore #9
    //   1532: aload #13
    //   1534: getfield block : Lmindustry/world/Block;
    //   1537: astore #14
    //   1539: aload #14
    //   1541: instanceof mindustry/world/blocks/power/PowerNode
    //   1544: istore #26
    //   1546: iload #26
    //   1548: ifeq -> 1488
    //   1551: aload #10
    //   1553: ifnonnull -> 1488
    //   1556: aload #13
    //   1558: astore #10
    //   1560: goto -> 1488
    //   1563: aload #9
    //   1565: ifnonnull -> 1598
    //   1568: iload_2
    //   1569: istore #6
    //   1571: aload #44
    //   1573: astore #54
    //   1575: aload #22
    //   1577: astore #55
    //   1579: iload #15
    //   1581: istore #56
    //   1583: iload #51
    //   1585: istore #25
    //   1587: fload #52
    //   1589: fstore #29
    //   1591: aload #53
    //   1593: astore #14
    //   1595: goto -> 3592
    //   1598: iload_2
    //   1599: iconst_1
    //   1600: iadd
    //   1601: istore #6
    //   1603: aload #9
    //   1605: getfield x : I
    //   1608: istore #50
    //   1610: getstatic lunarians/SteamDrill.DX : [I
    //   1613: astore #13
    //   1615: aload #9
    //   1617: getfield rotation : I
    //   1620: istore #26
    //   1622: iconst_3
    //   1623: istore #37
    //   1625: iload #26
    //   1627: iconst_3
    //   1628: iand
    //   1629: istore #26
    //   1631: aload #13
    //   1633: iload #26
    //   1635: iaload
    //   1636: istore #25
    //   1638: iload #50
    //   1640: iload #25
    //   1642: isub
    //   1643: istore #50
    //   1645: aload #9
    //   1647: getfield y : I
    //   1650: istore #25
    //   1652: getstatic lunarians/SteamDrill.DY : [I
    //   1655: astore #14
    //   1657: iload #6
    //   1659: istore_2
    //   1660: aload #9
    //   1662: getfield rotation : I
    //   1665: iconst_3
    //   1666: iand
    //   1667: istore #6
    //   1669: aload #14
    //   1671: iload #6
    //   1673: iaload
    //   1674: istore #6
    //   1676: iload #25
    //   1678: iload #6
    //   1680: isub
    //   1681: istore #25
    //   1683: iload #50
    //   1685: iload #25
    //   1687: invokestatic inside : (II)Z
    //   1690: istore #6
    //   1692: iload #6
    //   1694: ifeq -> 3549
    //   1697: iload #25
    //   1699: iload #30
    //   1701: imul
    //   1702: iload #50
    //   1704: iadd
    //   1705: istore #6
    //   1707: aload #16
    //   1709: iload #6
    //   1711: baload
    //   1712: istore #26
    //   1714: iload #26
    //   1716: ifne -> 3549
    //   1719: iload #6
    //   1721: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1724: astore #14
    //   1726: aload #8
    //   1728: aload #14
    //   1730: invokevirtual contains : (Ljava/lang/Object;)Z
    //   1733: istore #26
    //   1735: iload #26
    //   1737: ifne -> 3549
    //   1740: iconst_0
    //   1741: istore #26
    //   1743: aconst_null
    //   1744: astore #14
    //   1746: aload #12
    //   1748: iload #50
    //   1750: iload #25
    //   1752: iconst_0
    //   1753: invokestatic canPlace : (Lmindustry/world/Block;III)Z
    //   1756: istore #50
    //   1758: iload #50
    //   1760: ifeq -> 3549
    //   1763: new java/util/HashMap
    //   1766: astore #10
    //   1768: aload #10
    //   1770: invokespecial <init> : ()V
    //   1773: new java/util/HashMap
    //   1776: astore #13
    //   1778: aload #13
    //   1780: invokespecial <init> : ()V
    //   1783: new java/util/ArrayDeque
    //   1786: astore #14
    //   1788: aload #14
    //   1790: invokespecial <init> : ()V
    //   1793: iload #6
    //   1795: istore #57
    //   1797: iload #6
    //   1799: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1802: astore #38
    //   1804: aload #43
    //   1806: astore #58
    //   1808: iload #42
    //   1810: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1813: astore #43
    //   1815: aload #10
    //   1817: aload #38
    //   1819: aload #43
    //   1821: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1824: pop
    //   1825: iload #6
    //   1827: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1830: astore #38
    //   1832: aconst_null
    //   1833: astore_0
    //   1834: iconst_0
    //   1835: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1838: astore #43
    //   1840: aload #13
    //   1842: aload #38
    //   1844: aload #43
    //   1846: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1849: pop
    //   1850: iload #6
    //   1852: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1855: astore #38
    //   1857: aload #14
    //   1859: aload #38
    //   1861: invokevirtual add : (Ljava/lang/Object;)Z
    //   1864: pop
    //   1865: iconst_0
    //   1866: istore #6
    //   1868: fconst_0
    //   1869: fstore #36
    //   1871: aconst_null
    //   1872: astore #38
    //   1874: iconst_0
    //   1875: istore #17
    //   1877: aconst_null
    //   1878: astore #43
    //   1880: aload #14
    //   1882: invokevirtual isEmpty : ()Z
    //   1885: istore #57
    //   1887: iload #57
    //   1889: ifne -> 2990
    //   1892: aload #14
    //   1894: invokevirtual poll : ()Ljava/lang/Object;
    //   1897: astore #59
    //   1899: aload #38
    //   1901: astore #60
    //   1903: aload #59
    //   1905: astore #38
    //   1907: aload #59
    //   1909: checkcast java/lang/Integer
    //   1912: astore #38
    //   1914: aload #38
    //   1916: invokevirtual intValue : ()I
    //   1919: istore #57
    //   1921: aload #43
    //   1923: astore #61
    //   1925: iload #57
    //   1927: iload #30
    //   1929: irem
    //   1930: istore #17
    //   1932: aload #19
    //   1934: astore #62
    //   1936: iload #57
    //   1938: iload #30
    //   1940: idiv
    //   1941: istore #18
    //   1943: aload #13
    //   1945: aload #38
    //   1947: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   1950: checkcast java/lang/Integer
    //   1953: astore #55
    //   1955: aload #44
    //   1957: astore #54
    //   1959: aload #55
    //   1961: invokevirtual intValue : ()I
    //   1964: istore #20
    //   1966: aload #22
    //   1968: astore #55
    //   1970: iload #15
    //   1972: istore #56
    //   1974: iconst_0
    //   1975: istore #63
    //   1977: aconst_null
    //   1978: astore #22
    //   1980: iconst_4
    //   1981: istore #15
    //   1983: ldc_w 5.6E-45
    //   1986: fstore #31
    //   1988: iload #63
    //   1990: iload #15
    //   1992: if_icmpge -> 2607
    //   1995: getstatic lunarians/SteamDrill.DX : [I
    //   1998: astore #33
    //   2000: aload #33
    //   2002: iload #63
    //   2004: iaload
    //   2005: iload #17
    //   2007: iadd
    //   2008: istore #15
    //   2010: getstatic lunarians/SteamDrill.DY : [I
    //   2013: iload #63
    //   2015: iaload
    //   2016: istore #64
    //   2018: iload #63
    //   2020: istore #65
    //   2022: iload #18
    //   2024: iload #64
    //   2026: iadd
    //   2027: istore #63
    //   2029: iload #15
    //   2031: iconst_m1
    //   2032: iadd
    //   2033: istore #64
    //   2035: iload #64
    //   2037: istore #66
    //   2039: aload #9
    //   2041: astore #67
    //   2043: iload #64
    //   2045: istore #46
    //   2047: iload #46
    //   2049: iload #15
    //   2051: if_icmpgt -> 2573
    //   2054: iload #63
    //   2056: iconst_m1
    //   2057: iadd
    //   2058: istore #66
    //   2060: iload #15
    //   2062: istore #68
    //   2064: iload #66
    //   2066: istore #15
    //   2068: iload #15
    //   2070: iload #63
    //   2072: if_icmpgt -> 2527
    //   2075: iload #17
    //   2077: iload #46
    //   2079: if_icmplt -> 2126
    //   2082: iload #63
    //   2084: istore #69
    //   2086: iload #46
    //   2088: iconst_1
    //   2089: iadd
    //   2090: istore #63
    //   2092: iload #17
    //   2094: iload #63
    //   2096: if_icmpgt -> 2130
    //   2099: iload #18
    //   2101: iload #15
    //   2103: if_icmplt -> 2130
    //   2106: iload #15
    //   2108: iconst_1
    //   2109: iadd
    //   2110: istore #63
    //   2112: iload #18
    //   2114: iload #63
    //   2116: if_icmpgt -> 2130
    //   2119: iload #17
    //   2121: istore #70
    //   2123: goto -> 2153
    //   2126: iload #63
    //   2128: istore #69
    //   2130: iload #46
    //   2132: iload #15
    //   2134: invokestatic waterTiles : (II)I
    //   2137: istore #63
    //   2139: iload #17
    //   2141: istore #70
    //   2143: iconst_2
    //   2144: istore #17
    //   2146: iload #63
    //   2148: iload #17
    //   2150: if_icmpge -> 2164
    //   2153: iload #46
    //   2155: istore #71
    //   2157: iload #18
    //   2159: istore #72
    //   2161: goto -> 2502
    //   2164: iconst_4
    //   2165: istore #17
    //   2167: iload #63
    //   2169: iload #17
    //   2171: if_icmpge -> 2179
    //   2174: aload #60
    //   2176: ifnonnull -> 2486
    //   2179: aload #11
    //   2181: iload #46
    //   2183: iload #15
    //   2185: aload #16
    //   2187: aload #8
    //   2189: invokestatic freeArea : (Lmindustry/world/Block;II[ZLjava/util/HashSet;)Z
    //   2192: istore #17
    //   2194: iload #17
    //   2196: ifeq -> 2486
    //   2199: iconst_0
    //   2200: istore #17
    //   2202: aconst_null
    //   2203: astore #43
    //   2205: aload #11
    //   2207: iload #46
    //   2209: iload #15
    //   2211: iconst_0
    //   2212: invokestatic canPlace : (Lmindustry/world/Block;III)Z
    //   2215: istore #51
    //   2217: iload #51
    //   2219: ifeq -> 2486
    //   2222: iload #18
    //   2224: istore #72
    //   2226: iload #57
    //   2228: istore #17
    //   2230: iconst_m1
    //   2231: istore #18
    //   2233: iload #17
    //   2235: iload #18
    //   2237: if_icmpeq -> 2342
    //   2240: iload #17
    //   2242: iload #30
    //   2244: irem
    //   2245: istore #18
    //   2247: iload #17
    //   2249: istore #51
    //   2251: iload #17
    //   2253: iload #30
    //   2255: idiv
    //   2256: istore #17
    //   2258: iload #18
    //   2260: iload #46
    //   2262: if_icmplt -> 2305
    //   2265: iload #46
    //   2267: istore #71
    //   2269: iload #46
    //   2271: iconst_1
    //   2272: iadd
    //   2273: istore #46
    //   2275: iload #18
    //   2277: iload #46
    //   2279: if_icmpgt -> 2309
    //   2282: iload #17
    //   2284: iload #15
    //   2286: if_icmplt -> 2309
    //   2289: iload #15
    //   2291: iconst_1
    //   2292: iadd
    //   2293: istore #46
    //   2295: iload #17
    //   2297: iload #46
    //   2299: if_icmpgt -> 2309
    //   2302: goto -> 2494
    //   2305: iload #46
    //   2307: istore #71
    //   2309: iload #51
    //   2311: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   2314: astore #9
    //   2316: aload #10
    //   2318: aload #9
    //   2320: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   2323: checkcast java/lang/Integer
    //   2326: astore #9
    //   2328: aload #9
    //   2330: invokevirtual intValue : ()I
    //   2333: istore #17
    //   2335: iload #71
    //   2337: istore #46
    //   2339: goto -> 2230
    //   2342: iload #46
    //   2344: istore #71
    //   2346: iconst_4
    //   2347: istore #17
    //   2349: iload #63
    //   2351: iload #17
    //   2353: if_icmpne -> 2412
    //   2356: iload #17
    //   2358: newarray int
    //   2360: astore #9
    //   2362: iconst_0
    //   2363: istore #18
    //   2365: aconst_null
    //   2366: astore #19
    //   2368: aload #9
    //   2370: iconst_0
    //   2371: iload #57
    //   2373: iastore
    //   2374: iconst_1
    //   2375: istore #47
    //   2377: aload #9
    //   2379: iload #47
    //   2381: iload #46
    //   2383: iastore
    //   2384: iconst_2
    //   2385: istore #51
    //   2387: ldc 2.8E-45
    //   2389: fstore #52
    //   2391: aload #9
    //   2393: iload #51
    //   2395: iload #15
    //   2397: iastore
    //   2398: aload #9
    //   2400: iload #37
    //   2402: iload #17
    //   2404: iastore
    //   2405: aload #9
    //   2407: astore #60
    //   2409: goto -> 2470
    //   2412: iconst_0
    //   2413: istore #18
    //   2415: aconst_null
    //   2416: astore #19
    //   2418: iconst_1
    //   2419: istore #47
    //   2421: iconst_2
    //   2422: istore #51
    //   2424: ldc 2.8E-45
    //   2426: fstore #52
    //   2428: aload #61
    //   2430: ifnonnull -> 2470
    //   2433: iload #17
    //   2435: newarray int
    //   2437: astore #9
    //   2439: aload #9
    //   2441: iconst_0
    //   2442: iload #57
    //   2444: iastore
    //   2445: aload #9
    //   2447: iload #47
    //   2449: iload #46
    //   2451: iastore
    //   2452: aload #9
    //   2454: iload #51
    //   2456: iload #15
    //   2458: iastore
    //   2459: aload #9
    //   2461: iload #37
    //   2463: iload #63
    //   2465: iastore
    //   2466: aload #9
    //   2468: astore #61
    //   2470: aload #60
    //   2472: ifnull -> 2494
    //   2475: iconst_1
    //   2476: istore #51
    //   2478: ldc_w 1.4E-45
    //   2481: fstore #52
    //   2483: goto -> 2543
    //   2486: iload #46
    //   2488: istore #71
    //   2490: iload #18
    //   2492: istore #72
    //   2494: iconst_1
    //   2495: istore #51
    //   2497: ldc_w 1.4E-45
    //   2500: fstore #52
    //   2502: iload #15
    //   2504: iconst_1
    //   2505: iadd
    //   2506: istore #15
    //   2508: iload #69
    //   2510: istore #63
    //   2512: iload #70
    //   2514: istore #17
    //   2516: iload #72
    //   2518: istore #18
    //   2520: iload #71
    //   2522: istore #46
    //   2524: goto -> 2068
    //   2527: iload #46
    //   2529: istore #71
    //   2531: iload #17
    //   2533: istore #70
    //   2535: iload #18
    //   2537: istore #72
    //   2539: iload #63
    //   2541: istore #69
    //   2543: aload #60
    //   2545: ifnonnull -> 2581
    //   2548: iload #71
    //   2550: iconst_1
    //   2551: iadd
    //   2552: istore #46
    //   2554: iload #68
    //   2556: istore #15
    //   2558: iload #69
    //   2560: istore #63
    //   2562: iload #70
    //   2564: istore #17
    //   2566: iload #72
    //   2568: istore #18
    //   2570: goto -> 2047
    //   2573: iload #17
    //   2575: istore #70
    //   2577: iload #18
    //   2579: istore #72
    //   2581: aload #60
    //   2583: ifnonnull -> 2619
    //   2586: iload #65
    //   2588: iconst_1
    //   2589: iadd
    //   2590: istore #63
    //   2592: aload #67
    //   2594: astore #9
    //   2596: iload #70
    //   2598: istore #17
    //   2600: iload #72
    //   2602: istore #18
    //   2604: goto -> 1980
    //   2607: aload #9
    //   2609: astore #67
    //   2611: iload #17
    //   2613: istore #70
    //   2615: iload #18
    //   2617: istore #72
    //   2619: aload #61
    //   2621: astore #43
    //   2623: aload #60
    //   2625: ifnull -> 2635
    //   2628: aload #60
    //   2630: astore #38
    //   2632: goto -> 3010
    //   2635: bipush #48
    //   2637: istore #46
    //   2639: iload #20
    //   2641: iload #46
    //   2643: if_icmpge -> 2959
    //   2646: iconst_0
    //   2647: istore #46
    //   2649: aconst_null
    //   2650: astore #9
    //   2652: iconst_4
    //   2653: istore #15
    //   2655: ldc_w 5.6E-45
    //   2658: fstore #31
    //   2660: iload #46
    //   2662: iload #15
    //   2664: if_icmpge -> 2959
    //   2667: getstatic lunarians/SteamDrill.DX : [I
    //   2670: astore #19
    //   2672: aload #19
    //   2674: iload #46
    //   2676: iaload
    //   2677: istore #18
    //   2679: iload #70
    //   2681: iload #18
    //   2683: iadd
    //   2684: istore #18
    //   2686: getstatic lunarians/SteamDrill.DY : [I
    //   2689: astore #22
    //   2691: aload #22
    //   2693: iload #46
    //   2695: iaload
    //   2696: istore #63
    //   2698: iload #72
    //   2700: iload #63
    //   2702: iadd
    //   2703: istore #63
    //   2705: iload #18
    //   2707: iload #63
    //   2709: invokestatic inside : (II)Z
    //   2712: istore #15
    //   2714: iload #15
    //   2716: ifne -> 2730
    //   2719: aload #8
    //   2721: astore #73
    //   2723: iload #46
    //   2725: istore #57
    //   2727: goto -> 2946
    //   2730: iload #63
    //   2732: iload #30
    //   2734: imul
    //   2735: iload #18
    //   2737: iadd
    //   2738: istore #15
    //   2740: iload #46
    //   2742: istore #57
    //   2744: iload #15
    //   2746: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   2749: astore #9
    //   2751: aload #10
    //   2753: aload #9
    //   2755: invokevirtual containsKey : (Ljava/lang/Object;)Z
    //   2758: istore #46
    //   2760: iload #46
    //   2762: ifne -> 2942
    //   2765: aload #16
    //   2767: iload #15
    //   2769: baload
    //   2770: istore #46
    //   2772: iload #46
    //   2774: ifne -> 2942
    //   2777: iload #15
    //   2779: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   2782: astore #9
    //   2784: aload #8
    //   2786: aload #9
    //   2788: invokevirtual contains : (Ljava/lang/Object;)Z
    //   2791: istore #46
    //   2793: iload #46
    //   2795: ifne -> 2942
    //   2798: iload #15
    //   2800: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   2803: astore #9
    //   2805: aload #41
    //   2807: aload #9
    //   2809: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   2812: checkcast java/lang/Boolean
    //   2815: astore #9
    //   2817: aload #8
    //   2819: astore #73
    //   2821: aload #9
    //   2823: ifnonnull -> 2865
    //   2826: iconst_0
    //   2827: istore #21
    //   2829: aload #12
    //   2831: iload #18
    //   2833: iload #63
    //   2835: iconst_0
    //   2836: invokestatic canPlace : (Lmindustry/world/Block;III)Z
    //   2839: istore #46
    //   2841: iload #46
    //   2843: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   2846: astore #9
    //   2848: iload #15
    //   2850: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   2853: astore #8
    //   2855: aload #41
    //   2857: aload #8
    //   2859: aload #9
    //   2861: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2864: pop
    //   2865: aload #9
    //   2867: invokevirtual booleanValue : ()Z
    //   2870: istore #21
    //   2872: iload #21
    //   2874: ifeq -> 2946
    //   2877: iload #15
    //   2879: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   2882: astore #8
    //   2884: aload #10
    //   2886: aload #8
    //   2888: aload #38
    //   2890: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2893: pop
    //   2894: iload #15
    //   2896: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   2899: astore #8
    //   2901: iload #20
    //   2903: iconst_1
    //   2904: iadd
    //   2905: istore #46
    //   2907: iload #46
    //   2909: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   2912: astore #9
    //   2914: aload #13
    //   2916: aload #8
    //   2918: aload #9
    //   2920: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   2923: pop
    //   2924: iload #15
    //   2926: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   2929: astore #8
    //   2931: aload #14
    //   2933: aload #8
    //   2935: invokevirtual add : (Ljava/lang/Object;)Z
    //   2938: pop
    //   2939: goto -> 2946
    //   2942: aload #8
    //   2944: astore #73
    //   2946: iload #57
    //   2948: iconst_1
    //   2949: iadd
    //   2950: istore #46
    //   2952: aload #73
    //   2954: astore #8
    //   2956: goto -> 2652
    //   2959: aload #8
    //   2961: astore #73
    //   2963: aload #60
    //   2965: astore #38
    //   2967: aload #62
    //   2969: astore #19
    //   2971: aload #55
    //   2973: astore #22
    //   2975: aload #54
    //   2977: astore #44
    //   2979: iload #56
    //   2981: istore #15
    //   2983: aload #67
    //   2985: astore #9
    //   2987: goto -> 1880
    //   2990: aload #9
    //   2992: astore #67
    //   2994: aload #19
    //   2996: astore #62
    //   2998: aload #44
    //   3000: astore #54
    //   3002: aload #22
    //   3004: astore #55
    //   3006: iload #15
    //   3008: istore #56
    //   3010: aload #38
    //   3012: ifnonnull -> 3019
    //   3015: aload #43
    //   3017: astore #38
    //   3019: aload #38
    //   3021: ifnull -> 3569
    //   3024: aload #38
    //   3026: iconst_0
    //   3027: iaload
    //   3028: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   3031: astore #8
    //   3033: aload #13
    //   3035: aload #8
    //   3037: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   3040: checkcast java/lang/Integer
    //   3043: astore #8
    //   3045: aload #8
    //   3047: invokevirtual intValue : ()I
    //   3050: bipush #10
    //   3052: imul
    //   3053: istore #21
    //   3055: iconst_2
    //   3056: istore #17
    //   3058: aload #54
    //   3060: iload #17
    //   3062: iaload
    //   3063: istore #46
    //   3065: bipush #9
    //   3067: istore #26
    //   3069: bipush #9
    //   3071: iload #46
    //   3073: isub
    //   3074: bipush #25
    //   3076: imul
    //   3077: istore #46
    //   3079: iload #21
    //   3081: iload #46
    //   3083: iadd
    //   3084: istore #21
    //   3086: aload #54
    //   3088: iload #37
    //   3090: iaload
    //   3091: istore #46
    //   3093: iconst_4
    //   3094: istore #15
    //   3096: ldc_w 5.6E-45
    //   3099: fstore #31
    //   3101: iload #46
    //   3103: iload #15
    //   3105: idiv
    //   3106: istore #46
    //   3108: iload #21
    //   3110: iload #46
    //   3112: iadd
    //   3113: istore #21
    //   3115: aload #38
    //   3117: iload #37
    //   3119: iaload
    //   3120: istore #46
    //   3122: iconst_4
    //   3123: iload #46
    //   3125: isub
    //   3126: bipush #30
    //   3128: imul
    //   3129: istore #46
    //   3131: iload #21
    //   3133: iload #46
    //   3135: iadd
    //   3136: istore #21
    //   3138: iload #21
    //   3140: iload #27
    //   3142: if_icmplt -> 3148
    //   3145: goto -> 3569
    //   3148: iconst_2
    //   3149: istore #17
    //   3151: iload #17
    //   3153: newarray int
    //   3155: astore #74
    //   3157: iconst_1
    //   3158: istore #47
    //   3160: aload #38
    //   3162: iload #47
    //   3164: iaload
    //   3165: istore #46
    //   3167: iconst_0
    //   3168: istore #18
    //   3170: aconst_null
    //   3171: astore #19
    //   3173: aload #74
    //   3175: iconst_0
    //   3176: iload #46
    //   3178: iastore
    //   3179: aload #38
    //   3181: iload #17
    //   3183: iaload
    //   3184: istore #46
    //   3186: aload #74
    //   3188: iload #47
    //   3190: iload #46
    //   3192: iastore
    //   3193: new java/util/ArrayList
    //   3196: astore #9
    //   3198: aload #9
    //   3200: invokespecial <init> : ()V
    //   3203: aload #38
    //   3205: iconst_0
    //   3206: iaload
    //   3207: istore #17
    //   3209: iconst_m1
    //   3210: istore #20
    //   3212: iload #17
    //   3214: iload #20
    //   3216: if_icmpeq -> 3409
    //   3219: iload #17
    //   3221: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   3224: astore #19
    //   3226: aload #10
    //   3228: aload #19
    //   3230: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   3233: checkcast java/lang/Integer
    //   3236: astore #19
    //   3238: aload #19
    //   3240: invokevirtual intValue : ()I
    //   3243: istore #18
    //   3245: iload #17
    //   3247: iload #30
    //   3249: irem
    //   3250: istore #63
    //   3252: iload #17
    //   3254: iload #30
    //   3256: idiv
    //   3257: istore #15
    //   3259: iload #18
    //   3261: iload #20
    //   3263: if_icmpne -> 3282
    //   3266: aload #67
    //   3268: astore #44
    //   3270: aload #67
    //   3272: getfield rotation : I
    //   3275: iconst_3
    //   3276: iand
    //   3277: istore #18
    //   3279: goto -> 3349
    //   3282: aload #67
    //   3284: astore #44
    //   3286: iload #18
    //   3288: iload #30
    //   3290: irem
    //   3291: istore #26
    //   3293: iload #18
    //   3295: iload #30
    //   3297: idiv
    //   3298: istore #18
    //   3300: iload #26
    //   3302: iload #63
    //   3304: if_icmple -> 3316
    //   3307: iconst_0
    //   3308: istore #26
    //   3310: aconst_null
    //   3311: astore #14
    //   3313: goto -> 3345
    //   3316: iload #26
    //   3318: iload #63
    //   3320: if_icmpge -> 3329
    //   3323: iconst_2
    //   3324: istore #26
    //   3326: goto -> 3345
    //   3329: iload #18
    //   3331: iload #15
    //   3333: if_icmple -> 3342
    //   3336: iconst_1
    //   3337: istore #26
    //   3339: goto -> 3345
    //   3342: iconst_3
    //   3343: istore #26
    //   3345: iload #26
    //   3347: istore #18
    //   3349: iconst_3
    //   3350: newarray int
    //   3352: dup
    //   3353: dup2
    //   3354: iconst_0
    //   3355: iload #63
    //   3357: iastore
    //   3358: iconst_1
    //   3359: iload #15
    //   3361: iastore
    //   3362: iconst_2
    //   3363: iload #18
    //   3365: iastore
    //   3366: astore #14
    //   3368: aload #9
    //   3370: aload #14
    //   3372: invokevirtual add : (Ljava/lang/Object;)Z
    //   3375: pop
    //   3376: iload #17
    //   3378: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   3381: astore #14
    //   3383: aload #10
    //   3385: aload #14
    //   3387: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   3390: checkcast java/lang/Integer
    //   3393: astore #14
    //   3395: aload #14
    //   3397: invokevirtual intValue : ()I
    //   3400: istore #17
    //   3402: aload #44
    //   3404: astore #67
    //   3406: goto -> 3209
    //   3409: iconst_0
    //   3410: istore #18
    //   3412: aconst_null
    //   3413: astore #19
    //   3415: aload #38
    //   3417: iconst_0
    //   3418: iaload
    //   3419: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   3422: astore #38
    //   3424: aload #13
    //   3426: aload #38
    //   3428: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   3431: checkcast java/lang/Integer
    //   3434: astore #38
    //   3436: aload #38
    //   3438: invokevirtual intValue : ()I
    //   3441: istore #6
    //   3443: bipush #6
    //   3445: istore #50
    //   3447: iload #6
    //   3449: iload #50
    //   3451: if_icmpgt -> 3519
    //   3454: iconst_2
    //   3455: istore #17
    //   3457: aload #54
    //   3459: iload #17
    //   3461: iaload
    //   3462: istore #6
    //   3464: bipush #9
    //   3466: istore #50
    //   3468: iload #6
    //   3470: iload #50
    //   3472: if_icmplt -> 3519
    //   3475: aload #55
    //   3477: invokevirtual size : ()I
    //   3480: istore #6
    //   3482: aload #74
    //   3484: astore #19
    //   3486: iload #21
    //   3488: istore #27
    //   3490: aload #9
    //   3492: astore #43
    //   3494: aload #48
    //   3496: astore #14
    //   3498: iload #6
    //   3500: istore #21
    //   3502: iload #51
    //   3504: istore #25
    //   3506: fload #52
    //   3508: fstore #29
    //   3510: iconst_1
    //   3511: istore #47
    //   3513: iload_2
    //   3514: istore #6
    //   3516: goto -> 3642
    //   3519: iload_2
    //   3520: istore #6
    //   3522: aload #74
    //   3524: astore #19
    //   3526: iload #21
    //   3528: istore #27
    //   3530: aload #9
    //   3532: astore #43
    //   3534: aload #48
    //   3536: astore #14
    //   3538: iload #51
    //   3540: istore #25
    //   3542: fload #52
    //   3544: fstore #29
    //   3546: goto -> 3592
    //   3549: aload #43
    //   3551: astore #58
    //   3553: aload #19
    //   3555: astore #62
    //   3557: aload #44
    //   3559: astore #54
    //   3561: aload #22
    //   3563: astore #55
    //   3565: iload #15
    //   3567: istore #56
    //   3569: iload_2
    //   3570: istore #6
    //   3572: iload #51
    //   3574: istore #25
    //   3576: fload #52
    //   3578: fstore #29
    //   3580: aload #53
    //   3582: astore #14
    //   3584: aload #58
    //   3586: astore #43
    //   3588: aload #62
    //   3590: astore #19
    //   3592: iload #56
    //   3594: iconst_1
    //   3595: iadd
    //   3596: istore #15
    //   3598: iload #28
    //   3600: istore #21
    //   3602: aload #35
    //   3604: astore #9
    //   3606: aload #49
    //   3608: astore #10
    //   3610: aload #55
    //   3612: astore #22
    //   3614: aload #54
    //   3616: astore #44
    //   3618: iconst_m1
    //   3619: istore #42
    //   3621: goto -> 868
    //   3624: aload #9
    //   3626: astore #35
    //   3628: aload #10
    //   3630: astore #49
    //   3632: aload #22
    //   3634: astore #55
    //   3636: iload_2
    //   3637: istore #6
    //   3639: iconst_1
    //   3640: istore #47
    //   3642: iload #21
    //   3644: iconst_1
    //   3645: iadd
    //   3646: istore #21
    //   3648: aload #35
    //   3650: astore #9
    //   3652: aload #49
    //   3654: astore #10
    //   3656: aload #55
    //   3658: astore #22
    //   3660: iconst_m1
    //   3661: istore #42
    //   3663: goto -> 822
    //   3666: aload #14
    //   3668: ifnonnull -> 3744
    //   3671: iload #25
    //   3673: ifeq -> 3708
    //   3676: ldc_w 'Нет места/пути к воде'
    //   3679: astore #38
    //   3681: ldc_w 'Sem espaço/rota até a água'
    //   3684: astore #74
    //   3686: ldc_w 'No space/route to water'
    //   3689: astore #8
    //   3691: aload #74
    //   3693: aload #8
    //   3695: ldc_w 'Sin espacio/ruta hasta el agua'
    //   3698: aload #38
    //   3700: invokestatic tr : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   3703: astore #41
    //   3705: goto -> 3737
    //   3708: ldc_w 'Нет воды рядом'
    //   3711: astore #38
    //   3713: ldc_w 'Sem água por perto'
    //   3716: astore #74
    //   3718: ldc_w 'No water nearby'
    //   3721: astore #8
    //   3723: aload #74
    //   3725: aload #8
    //   3727: ldc_w 'Sin agua cerca'
    //   3730: aload #38
    //   3732: invokestatic tr : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   3735: astore #41
    //   3737: aload #41
    //   3739: putstatic lunarians/SteamDrill.lastError : Ljava/lang/String;
    //   3742: aconst_null
    //   3743: areturn
    //   3744: aload #43
    //   3746: astore #41
    //   3748: new arc/struct/Seq
    //   3751: astore #43
    //   3753: aload #43
    //   3755: invokespecial <init> : ()V
    //   3758: aload #19
    //   3760: astore #38
    //   3762: new java/util/HashSet
    //   3765: astore #19
    //   3767: aload #19
    //   3769: invokespecial <init> : ()V
    //   3772: aload #14
    //   3774: invokevirtual iterator : ()Ljava/util/Iterator;
    //   3777: astore #74
    //   3779: iconst_0
    //   3780: istore #63
    //   3782: aconst_null
    //   3783: astore #22
    //   3785: aload #74
    //   3787: invokeinterface hasNext : ()Z
    //   3792: istore #21
    //   3794: iload #21
    //   3796: ifeq -> 3882
    //   3799: aload #74
    //   3801: invokeinterface next : ()Ljava/lang/Object;
    //   3806: checkcast mindustry/entities/units/BuildPlan
    //   3809: astore #8
    //   3811: aload #43
    //   3813: aload #8
    //   3815: invokevirtual add : (Ljava/lang/Object;)Larc/struct/Seq;
    //   3818: pop
    //   3819: aload #8
    //   3821: getfield block : Lmindustry/world/Block;
    //   3824: astore #9
    //   3826: aload #8
    //   3828: getfield x : I
    //   3831: istore #50
    //   3833: aload #8
    //   3835: getfield y : I
    //   3838: istore #25
    //   3840: aload #9
    //   3842: iload #50
    //   3844: iload #25
    //   3846: aload #19
    //   3848: invokestatic footprint : (Lmindustry/world/Block;IILjava/util/HashSet;)V
    //   3851: aload #8
    //   3853: getfield block : Lmindustry/world/Block;
    //   3856: astore #9
    //   3858: aload #9
    //   3860: instanceof mindustry/world/blocks/power/PowerNode
    //   3863: istore #46
    //   3865: iload #46
    //   3867: ifeq -> 3785
    //   3870: aload #22
    //   3872: ifnonnull -> 3785
    //   3875: aload #8
    //   3877: astore #22
    //   3879: goto -> 3785
    //   3882: aload #41
    //   3884: invokevirtual iterator : ()Ljava/util/Iterator;
    //   3887: astore #41
    //   3889: aload #41
    //   3891: invokeinterface hasNext : ()Z
    //   3896: istore #27
    //   3898: iload #27
    //   3900: ifeq -> 4018
    //   3903: aload #41
    //   3905: invokeinterface next : ()Ljava/lang/Object;
    //   3910: checkcast [I
    //   3913: astore #74
    //   3915: new mindustry/entities/units/BuildPlan
    //   3918: astore #8
    //   3920: iconst_0
    //   3921: istore #46
    //   3923: aconst_null
    //   3924: astore #9
    //   3926: aload #74
    //   3928: iconst_0
    //   3929: iaload
    //   3930: istore #50
    //   3932: iconst_1
    //   3933: istore #47
    //   3935: aload #74
    //   3937: iload #47
    //   3939: iaload
    //   3940: istore #25
    //   3942: iconst_2
    //   3943: istore #51
    //   3945: ldc 2.8E-45
    //   3947: fstore #52
    //   3949: aload #74
    //   3951: iload #51
    //   3953: iaload
    //   3954: istore #26
    //   3956: aload #8
    //   3958: iload #50
    //   3960: iload #25
    //   3962: iload #26
    //   3964: aload #12
    //   3966: invokespecial <init> : (IIILmindustry/world/Block;)V
    //   3969: aload #43
    //   3971: aload #8
    //   3973: invokevirtual add : (Ljava/lang/Object;)Larc/struct/Seq;
    //   3976: pop
    //   3977: aload #74
    //   3979: iload #47
    //   3981: iaload
    //   3982: iload #30
    //   3984: imul
    //   3985: istore #21
    //   3987: aload #74
    //   3989: iconst_0
    //   3990: iaload
    //   3991: istore #27
    //   3993: iload #21
    //   3995: iload #27
    //   3997: iadd
    //   3998: istore #21
    //   4000: iload #21
    //   4002: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   4005: astore #74
    //   4007: aload #19
    //   4009: aload #74
    //   4011: invokevirtual add : (Ljava/lang/Object;)Z
    //   4014: pop
    //   4015: goto -> 3889
    //   4018: iconst_0
    //   4019: istore #46
    //   4021: aconst_null
    //   4022: astore #9
    //   4024: iconst_1
    //   4025: istore #47
    //   4027: new mindustry/entities/units/BuildPlan
    //   4030: astore #44
    //   4032: aload #38
    //   4034: iconst_0
    //   4035: iaload
    //   4036: istore #5
    //   4038: aload #38
    //   4040: iload #47
    //   4042: iaload
    //   4043: istore #27
    //   4045: aload #44
    //   4047: iload #5
    //   4049: iload #27
    //   4051: iconst_0
    //   4052: aload #11
    //   4054: invokespecial <init> : (IIILmindustry/world/Block;)V
    //   4057: aload #43
    //   4059: aload #44
    //   4061: invokevirtual add : (Ljava/lang/Object;)Larc/struct/Seq;
    //   4064: pop
    //   4065: aload #38
    //   4067: iconst_0
    //   4068: iaload
    //   4069: istore #5
    //   4071: aload #38
    //   4073: iload #47
    //   4075: iaload
    //   4076: istore #6
    //   4078: aload #11
    //   4080: iload #5
    //   4082: iload #6
    //   4084: aload #19
    //   4086: invokestatic footprint : (Lmindustry/world/Block;IILjava/util/HashSet;)V
    //   4089: invokestatic autoPower : ()Z
    //   4092: istore #5
    //   4094: iload #5
    //   4096: ifeq -> 4118
    //   4099: iload #30
    //   4101: istore #15
    //   4103: aload #43
    //   4105: aload #19
    //   4107: aload #16
    //   4109: aload #44
    //   4111: aload #22
    //   4113: iload #30
    //   4115: invokestatic addPower : (Larc/struct/Seq;Ljava/util/HashSet;[ZLmindustry/entities/units/BuildPlan;Lmindustry/entities/units/BuildPlan;I)V
    //   4118: getstatic lunarians/SteamDrill.lastTiles : Ljava/util/ArrayList;
    //   4121: aload #19
    //   4123: invokevirtual addAll : (Ljava/util/Collection;)Z
    //   4126: pop
    //   4127: aload #43
    //   4129: areturn
    //   4130: ldc_w 'Blocos indisponíveis'
    //   4133: ldc_w 'Blocks unavailable'
    //   4136: ldc_w 'Bloques no disponibles'
    //   4139: ldc_w 'Блоки недоступны'
    //   4142: invokestatic tr : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   4145: putstatic lunarians/SteamDrill.lastError : Ljava/lang/String;
    //   4148: aconst_null
    //   4149: areturn
  }
  
  private static void rotateOnce(Seq paramSeq, int paramInt1, int paramInt2) {
    int i = 0;
    while (true) {
      int j = paramSeq.size;
      if (i < j) {
        BuildPlan buildPlan = (BuildPlan)paramSeq.get(i);
        boolean bool = buildPlan.breaking;
        if (!bool) {
          Block block = buildPlan.block;
          if (block != null) {
            float f1 = buildPlan.block.offset;
            float f2 = (buildPlan.x - paramInt1);
            float f3 = 8.0F;
            f2 = f2 * f3 + f1;
            float f4 = (-((buildPlan.y - paramInt2) * f3 + f1) - f1) / f3;
            int m = Math.round(f4) + paramInt1;
            buildPlan.x = m;
            f2 = (f2 - f1) / f3;
            int k = Math.round(f2) + paramInt2;
            buildPlan.y = k;
            block = buildPlan.block;
            int n = buildPlan.rotation + 1;
            byte b = 4;
            f3 = 5.6E-45F;
            n = Mathf.mod(n, b);
            k = block.planRotation(n);
            buildPlan.rotation = k;
            SteamDrill$0 steamDrill$0 = new SteamDrill$0();
            this();
            buildPlan.pointConfig(steamDrill$0);
          } 
        } 
        i++;
        continue;
      } 
      break;
    } 
  }
  
  private static Schematic schematic() {
    Schematic schematic = schem;
    if (schematic == null) {
      String str = "bXNjaAF4nEWOzUrDQBSFT/NvgnbjQlyFulFowEUrbgW7EKwbFRchi0kzLQNpUidTtYjv4CPUZ3DhW9UnKHonTemFuZfLPec7Awd7FvxRWSheqCGbwXj/gJOzlOcVjuPV8rP7+/XT7cQnl/0eG4+T1fI7TjoJrIJNOY7uFWfT8FqKPA/PL8LBm3oKT3v9MwQZr0ZSzJQoCwTpwn+8fbgZXt0NPLQrbYomvOCSqVJiPxfPc5FFspwrLnGQSpFNeES/yuZCIchZxWWU6RA4jai9E73wBUH8WflKqqLMONytFTikBxNo0bABg4bj/jWlb3D1vVULqJHGIpUJ06ZhudiUBb26O6devfragMyN36wjtJ+CqNmeBhs1sg5eN0RTBxs6bAfVq72FrolmEYN0Ng3jH04dbjI=";
      try {
      
      } finally {
        str = null;
        String str1 = "[Lunarians] steam schematic";
      } 
    } 
    return schem;
  }
  
  private static int[] snap(Block paramBlock, double paramDouble1, double paramDouble2, boolean[] paramArrayOfboolean, HashSet paramHashSet, int paramInt) {
    long l1 = Math.round(paramDouble1);
    int i = (int)l1;
    long l2 = Math.round(paramDouble2);
    int j = (int)l2;
    int k = 0;
    while (true) {
      paramInt = 3;
      if (k <= paramInt) {
        paramInt = -k;
        int m;
        for (m = paramInt; m <= k; m++) {
          int n;
          for (n = paramInt; n <= k; n++) {
            int i1 = Math.abs(n);
            int i2 = Math.abs(m);
            i1 = Math.max(i1, i2);
            if (i1 == k) {
              i1 = i + n;
              i2 = j + m;
              boolean bool = freeArea(paramBlock, i1, i2, paramArrayOfboolean, paramHashSet);
              if (bool) {
                bool = canPlace(paramBlock, i1, i2, 0);
                if (bool)
                  return new int[] { i1, i2 }; 
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
  
  private static String tr(String paramString1, String paramString2, String paramString3, String paramString4) {
    return AutoSetConfig.tr4(paramString1, paramString2, paramString3, paramString4);
  }
  
  private static int waterTiles(int paramInt1, int paramInt2) {
    int i = 0;
    int j = 0;
    while (true) {
      byte b = 2;
      if (i < b) {
        int k = 0;
        while (k < b) {
          World world = Vars.world;
          int m = paramInt1 + k;
          int n = paramInt2 + i;
          Tile tile = world.tile(m, n);
          if (tile != null) {
            Building building = tile.build;
            if (building == null) {
              Floor floor = tile.floor();
              if (floor != null) {
                Liquid liquid1 = (tile.floor()).liquidDrop;
                Liquid liquid2 = Liquids.water;
                if (liquid1 == liquid2)
                  j++; 
              } 
              k++;
              continue;
            } 
          } 
          return -1;
        } 
        i++;
        continue;
      } 
      return j;
    } 
  }
}
