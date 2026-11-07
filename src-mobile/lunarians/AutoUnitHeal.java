package lunarians;

import arc.Core;
import arc.Events;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Time;
import java.lang.reflect.Field;
import lunarians.data.UnitHealSlot;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.entities.EntityGroup;
import mindustry.entities.abilities.Ability;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.units.UnitController;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

public class AutoUnitHeal {
  private static final float SCAN = 0.8F;
  
  public static final IntSet activeHealers;
  
  private static final IntSet claimed;
  
  private static int count;
  
  private static boolean errorLogged;
  
  private static final IntSet escortUsed;
  
  private static final IntSet targeted;
  
  private static float timer;
  
  static {
    IntSet intSet = new IntSet();
    this();
    targeted = intSet;
    intSet = new IntSet();
    this();
    escortUsed = intSet;
    intSet = new IntSet();
    this();
    claimed = intSet;
    intSet = new IntSet();
    this();
    activeHealers = intSet;
  }
  
  public static int count() {
    return count;
  }
  
  private static void escort(Unit paramUnit1, Unit paramUnit2) {
    // Byte code:
    //   0: aload_0
    //   1: astore_2
    //   2: aload_1
    //   3: astore_3
    //   4: aload_0
    //   5: getfield x : F
    //   8: fstore #4
    //   10: aload_0
    //   11: getfield y : F
    //   14: fstore #5
    //   16: aload_1
    //   17: getfield x : F
    //   20: fload #4
    //   22: fsub
    //   23: fstore #6
    //   25: aload_1
    //   26: getfield y : F
    //   29: fload #5
    //   31: fsub
    //   32: fstore #7
    //   34: fload #6
    //   36: fload #6
    //   38: fmul
    //   39: fstore #8
    //   41: fload #7
    //   43: fload #7
    //   45: fmul
    //   46: fstore #9
    //   48: fload #8
    //   50: fload #9
    //   52: fadd
    //   53: f2d
    //   54: invokestatic sqrt : (D)D
    //   57: dstore #10
    //   59: dload #10
    //   61: d2f
    //   62: fstore #8
    //   64: ldc 8.0
    //   66: fstore #12
    //   68: fload #8
    //   70: fload #12
    //   72: fcmpg
    //   73: istore #13
    //   75: iload #13
    //   77: ifge -> 94
    //   80: ldc 1.0
    //   82: fstore #6
    //   84: fconst_0
    //   85: fstore #7
    //   87: ldc 8.0
    //   89: fstore #8
    //   91: goto -> 108
    //   94: fload #6
    //   96: fload #8
    //   98: fdiv
    //   99: fstore #6
    //   101: fload #7
    //   103: fload #8
    //   105: fdiv
    //   106: fstore #7
    //   108: fload #7
    //   110: fneg
    //   111: fstore #12
    //   113: new arc/struct/Seq
    //   116: astore #14
    //   118: aload #14
    //   120: invokespecial <init> : ()V
    //   123: getstatic mindustry/gen/Groups.unit : Lmindustry/entities/EntityGroup;
    //   126: astore #15
    //   128: aload #15
    //   130: invokevirtual size : ()I
    //   133: istore #16
    //   135: iconst_0
    //   136: istore #17
    //   138: fconst_0
    //   139: fstore #18
    //   141: aconst_null
    //   142: astore #19
    //   144: iload #17
    //   146: iload #16
    //   148: if_icmpge -> 416
    //   151: getstatic mindustry/gen/Groups.unit : Lmindustry/entities/EntityGroup;
    //   154: iload #17
    //   156: invokevirtual index : (I)Lmindustry/gen/Entityc;
    //   159: checkcast mindustry/gen/Unit
    //   162: astore #20
    //   164: aload #20
    //   166: ifnull -> 407
    //   169: aload #20
    //   171: invokevirtual isValid : ()Z
    //   174: istore #21
    //   176: iload #21
    //   178: ifeq -> 407
    //   181: aload #20
    //   183: getfield team : Lmindustry/game/Team;
    //   186: astore #22
    //   188: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   191: astore #23
    //   193: aload #23
    //   195: invokevirtual team : ()Lmindustry/game/Team;
    //   198: astore #24
    //   200: aload #22
    //   202: aload #24
    //   204: if_acmpne -> 407
    //   207: aload #20
    //   209: aload_2
    //   210: if_acmpeq -> 407
    //   213: aload #20
    //   215: aload_3
    //   216: if_acmpeq -> 407
    //   219: getstatic lunarians/AutoUnitHeal.escortUsed : Larc/struct/IntSet;
    //   222: astore #24
    //   224: aload #20
    //   226: getfield id : I
    //   229: istore #21
    //   231: aload #24
    //   233: iload #21
    //   235: invokevirtual contains : (I)Z
    //   238: istore #25
    //   240: iload #25
    //   242: ifne -> 407
    //   245: getstatic lunarians/AutoUnitHeal.activeHealers : Larc/struct/IntSet;
    //   248: astore #24
    //   250: aload #20
    //   252: getfield id : I
    //   255: istore #21
    //   257: aload #24
    //   259: iload #21
    //   261: invokevirtual contains : (I)Z
    //   264: istore #25
    //   266: iload #25
    //   268: ifne -> 407
    //   271: aload #20
    //   273: getfield type : Lmindustry/type/UnitType;
    //   276: astore #24
    //   278: aload #24
    //   280: invokestatic isHealerType : (Lmindustry/type/UnitType;)Z
    //   283: istore #25
    //   285: iload #25
    //   287: ifne -> 407
    //   290: aload #20
    //   292: invokevirtual controller : ()Lmindustry/entities/units/UnitController;
    //   295: astore #24
    //   297: aload #24
    //   299: instanceof mindustry/ai/types/CommandAI
    //   302: istore #25
    //   304: iload #25
    //   306: ifeq -> 407
    //   309: aload #20
    //   311: getfield id : I
    //   314: invokestatic holder : (I)I
    //   317: istore #25
    //   319: iconst_3
    //   320: istore #21
    //   322: ldc 4.2E-45
    //   324: fstore #26
    //   326: iload #25
    //   328: iload #21
    //   330: if_icmpge -> 407
    //   333: aload #20
    //   335: fload #4
    //   337: fload #5
    //   339: invokevirtual dst : (FF)F
    //   342: fstore #9
    //   344: ldc 1134559232
    //   346: istore #21
    //   348: ldc 320.0
    //   350: fstore #26
    //   352: fload #9
    //   354: fload #26
    //   356: fcmpl
    //   357: istore #25
    //   359: iload #25
    //   361: ifle -> 399
    //   364: aload_3
    //   365: getfield x : F
    //   368: fstore #9
    //   370: aload_3
    //   371: getfield y : F
    //   374: fstore #27
    //   376: aload #20
    //   378: fload #9
    //   380: fload #27
    //   382: invokevirtual dst : (FF)F
    //   385: fstore #9
    //   387: fload #9
    //   389: fload #26
    //   391: fcmpl
    //   392: istore #25
    //   394: iload #25
    //   396: ifgt -> 407
    //   399: aload #14
    //   401: aload #20
    //   403: invokevirtual add : (Ljava/lang/Object;)Larc/struct/Seq;
    //   406: pop
    //   407: iload #17
    //   409: iconst_1
    //   410: iadd
    //   411: istore #17
    //   413: goto -> 144
    //   416: aconst_null
    //   417: astore #23
    //   419: aload #14
    //   421: invokevirtual isEmpty : ()Z
    //   424: istore #28
    //   426: iload #28
    //   428: ifne -> 1396
    //   431: new lunarians/AutoUnitHeal$2
    //   434: astore_2
    //   435: aload_2
    //   436: fload #4
    //   438: fload #12
    //   440: fload #5
    //   442: fload #6
    //   444: invokespecial <init> : (FFFF)V
    //   447: aload #14
    //   449: aload_2
    //   450: invokevirtual sort : (Larc/func/Floatf;)Larc/struct/Seq;
    //   453: pop
    //   454: aload #14
    //   456: getfield size : I
    //   459: istore #28
    //   461: bipush #8
    //   463: istore #25
    //   465: ldc 1.1E-44
    //   467: fstore #9
    //   469: iload #28
    //   471: iload #25
    //   473: if_icmple -> 483
    //   476: aload #14
    //   478: iload #25
    //   480: invokevirtual truncate : (I)V
    //   483: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   486: invokevirtual team : ()Lmindustry/game/Team;
    //   489: astore #29
    //   491: aload_3
    //   492: getfield x : F
    //   495: fstore #30
    //   497: aload_3
    //   498: getfield y : F
    //   501: fstore #9
    //   503: new lunarians/AutoUnitHeal$3
    //   506: astore #31
    //   508: aload #31
    //   510: invokespecial <init> : ()V
    //   513: new lunarians/AutoUnitHeal$4
    //   516: astore #32
    //   518: aload #32
    //   520: invokespecial <init> : ()V
    //   523: ldc 320.0
    //   525: fstore #33
    //   527: fload #30
    //   529: fstore #34
    //   531: fload #9
    //   533: fstore #35
    //   535: aload #29
    //   537: fload #30
    //   539: fload #9
    //   541: fload #33
    //   543: aload #31
    //   545: aload #32
    //   547: invokestatic closestTarget : (Lmindustry/game/Team;FFFLarc/func/Boolf;Larc/func/Boolf;)Lmindustry/gen/Teamc;
    //   550: astore_2
    //   551: aload_2
    //   552: ifnull -> 620
    //   555: aload_2
    //   556: invokeinterface y : ()F
    //   561: fstore #9
    //   563: aload_3
    //   564: getfield y : F
    //   567: fstore #27
    //   569: fload #9
    //   571: fload #27
    //   573: fsub
    //   574: f2d
    //   575: dstore #36
    //   577: aload_2
    //   578: invokeinterface x : ()F
    //   583: fstore #38
    //   585: aload_3
    //   586: getfield x : F
    //   589: fstore #18
    //   591: fload #38
    //   593: fload #18
    //   595: fsub
    //   596: fstore #38
    //   598: fload #38
    //   600: f2d
    //   601: dstore #39
    //   603: dload #36
    //   605: dload #39
    //   607: invokestatic atan2 : (DD)D
    //   610: dstore #36
    //   612: dload #36
    //   614: d2f
    //   615: fstore #9
    //   617: goto -> 629
    //   620: iconst_0
    //   621: istore #25
    //   623: fconst_0
    //   624: fstore #9
    //   626: aconst_null
    //   627: astore #24
    //   629: iconst_0
    //   630: istore #41
    //   632: fconst_0
    //   633: fstore #27
    //   635: iconst_0
    //   636: istore #16
    //   638: fconst_0
    //   639: fstore #38
    //   641: aconst_null
    //   642: astore #15
    //   644: aload #14
    //   646: getfield size : I
    //   649: istore #17
    //   651: ldc 1103101952
    //   653: istore #21
    //   655: ldc 24.0
    //   657: fstore #26
    //   659: ldc -24.0
    //   661: fstore #42
    //   663: iload #41
    //   665: iload #17
    //   667: if_icmpge -> 817
    //   670: aload #14
    //   672: iload #41
    //   674: invokevirtual get : (I)Ljava/lang/Object;
    //   677: checkcast mindustry/gen/Unit
    //   680: astore #19
    //   682: aload #19
    //   684: getfield x : F
    //   687: fload #4
    //   689: fsub
    //   690: fload #6
    //   692: fmul
    //   693: fstore #43
    //   695: ldc 28.0
    //   697: fstore #44
    //   699: aload #19
    //   701: getfield y : F
    //   704: fload #5
    //   706: fsub
    //   707: fload #7
    //   709: fmul
    //   710: fstore #45
    //   712: fload #43
    //   714: fload #45
    //   716: fadd
    //   717: fstore #43
    //   719: aload #19
    //   721: getfield x : F
    //   724: fload #4
    //   726: fsub
    //   727: fload #12
    //   729: fmul
    //   730: fstore #45
    //   732: aload #19
    //   734: getfield y : F
    //   737: fload #5
    //   739: fsub
    //   740: fload #6
    //   742: fmul
    //   743: fstore #18
    //   745: fload #45
    //   747: fload #18
    //   749: fadd
    //   750: fstore #45
    //   752: fload #43
    //   754: fload #42
    //   756: fcmpl
    //   757: istore #17
    //   759: iload #17
    //   761: ifle -> 802
    //   764: fload #26
    //   766: fload #8
    //   768: fadd
    //   769: fstore #26
    //   771: fload #43
    //   773: fload #26
    //   775: fcmpg
    //   776: istore #46
    //   778: iload #46
    //   780: ifge -> 802
    //   783: fload #45
    //   785: invokestatic abs : (F)F
    //   788: fstore #43
    //   790: fload #43
    //   792: fload #44
    //   794: fcmpg
    //   795: istore #46
    //   797: iload #46
    //   799: iflt -> 808
    //   802: iload #16
    //   804: iconst_1
    //   805: iadd
    //   806: istore #16
    //   808: iload #41
    //   810: iconst_1
    //   811: iadd
    //   812: istore #41
    //   814: goto -> 644
    //   817: ldc 28.0
    //   819: fstore #44
    //   821: iconst_0
    //   822: istore #41
    //   824: fconst_0
    //   825: fstore #27
    //   827: iconst_0
    //   828: istore #46
    //   830: fconst_0
    //   831: fstore #43
    //   833: aload #14
    //   835: getfield size : I
    //   838: istore #17
    //   840: iload #46
    //   842: iload #17
    //   844: if_icmpge -> 1396
    //   847: aload #14
    //   849: iload #46
    //   851: invokevirtual get : (I)Ljava/lang/Object;
    //   854: checkcast mindustry/gen/Unit
    //   857: astore #19
    //   859: aload #19
    //   861: getfield x : F
    //   864: fload #4
    //   866: fsub
    //   867: fload #6
    //   869: fmul
    //   870: fstore #45
    //   872: ldc 24.0
    //   874: fstore #47
    //   876: aload #19
    //   878: getfield y : F
    //   881: fload #5
    //   883: fsub
    //   884: fload #7
    //   886: fmul
    //   887: fstore #26
    //   889: fload #45
    //   891: fload #26
    //   893: fadd
    //   894: fstore #45
    //   896: aload #19
    //   898: getfield x : F
    //   901: fload #4
    //   903: fsub
    //   904: fload #12
    //   906: fmul
    //   907: fstore #26
    //   909: aload_2
    //   910: astore #48
    //   912: aload #19
    //   914: getfield y : F
    //   917: fload #5
    //   919: fsub
    //   920: fload #6
    //   922: fmul
    //   923: fstore #30
    //   925: fload #26
    //   927: fload #30
    //   929: fadd
    //   930: fstore #26
    //   932: fload #45
    //   934: fload #42
    //   936: fcmpl
    //   937: istore #28
    //   939: iload #28
    //   941: ifle -> 1107
    //   944: fload #8
    //   946: fload #47
    //   948: fadd
    //   949: fstore #30
    //   951: fload #45
    //   953: fload #30
    //   955: fcmpg
    //   956: istore #28
    //   958: iload #28
    //   960: ifge -> 1107
    //   963: fload #26
    //   965: invokestatic abs : (F)F
    //   968: fstore #30
    //   970: fload #30
    //   972: fload #44
    //   974: fcmpg
    //   975: istore #28
    //   977: iload #28
    //   979: ifge -> 1107
    //   982: fload #26
    //   984: fconst_0
    //   985: fcmpl
    //   986: istore #28
    //   988: iload #28
    //   990: iflt -> 1004
    //   993: ldc 1065353216
    //   995: istore #28
    //   997: ldc 1.0
    //   999: fstore #30
    //   1001: goto -> 1012
    //   1004: ldc -1082130432
    //   1006: istore #28
    //   1008: ldc -1.0
    //   1010: fstore #30
    //   1012: new arc/math/geom/Vec2
    //   1015: astore #22
    //   1017: fload #6
    //   1019: fload #45
    //   1021: fmul
    //   1022: fstore #34
    //   1024: fload #4
    //   1026: fload #34
    //   1028: fadd
    //   1029: fstore #34
    //   1031: fload #12
    //   1033: fload #30
    //   1035: fmul
    //   1036: fstore #35
    //   1038: ldc 44.0
    //   1040: fstore #33
    //   1042: fload #35
    //   1044: fload #33
    //   1046: fmul
    //   1047: fstore #35
    //   1049: fload #30
    //   1051: fstore #49
    //   1053: fload #34
    //   1055: fload #35
    //   1057: fadd
    //   1058: fstore #30
    //   1060: fload #45
    //   1062: fload #7
    //   1064: fmul
    //   1065: fload #5
    //   1067: fadd
    //   1068: fstore #45
    //   1070: fload #6
    //   1072: fload #49
    //   1074: fmul
    //   1075: fload #33
    //   1077: fmul
    //   1078: fstore #34
    //   1080: fload #45
    //   1082: fload #34
    //   1084: fadd
    //   1085: fstore #45
    //   1087: aload #22
    //   1089: fload #30
    //   1091: fload #45
    //   1093: invokespecial <init> : (FF)V
    //   1096: fload #4
    //   1098: fstore #50
    //   1100: fload #5
    //   1102: fstore #51
    //   1104: goto -> 1262
    //   1107: aload #48
    //   1109: ifnull -> 1366
    //   1112: iload #41
    //   1114: i2f
    //   1115: fstore #30
    //   1117: iload #16
    //   1119: iconst_m1
    //   1120: iadd
    //   1121: i2f
    //   1122: ldc 0.5
    //   1124: fmul
    //   1125: fstore #45
    //   1127: fload #30
    //   1129: fload #45
    //   1131: fsub
    //   1132: ldc 0.45
    //   1134: fmul
    //   1135: fload #9
    //   1137: fadd
    //   1138: fstore #30
    //   1140: iload #41
    //   1142: iconst_2
    //   1143: irem
    //   1144: istore #52
    //   1146: iload #52
    //   1148: i2f
    //   1149: ldc 10.0
    //   1151: fmul
    //   1152: fstore #45
    //   1154: ldc 1104150528
    //   1156: istore #21
    //   1158: ldc 26.0
    //   1160: fstore #26
    //   1162: fload #45
    //   1164: fload #26
    //   1166: fadd
    //   1167: fstore #45
    //   1169: new arc/math/geom/Vec2
    //   1172: astore #22
    //   1174: fload #4
    //   1176: fstore #50
    //   1178: aload_3
    //   1179: getfield x : F
    //   1182: fstore #4
    //   1184: fload #4
    //   1186: fstore #34
    //   1188: fload #5
    //   1190: fstore #51
    //   1192: fload #30
    //   1194: f2d
    //   1195: dstore #53
    //   1197: dload #53
    //   1199: dstore #55
    //   1201: dload #53
    //   1203: invokestatic cos : (D)D
    //   1206: dstore #53
    //   1208: dload #53
    //   1210: d2f
    //   1211: fload #45
    //   1213: fmul
    //   1214: fstore #30
    //   1216: fload #4
    //   1218: fload #30
    //   1220: fadd
    //   1221: fstore #4
    //   1223: aload_3
    //   1224: getfield y : F
    //   1227: fstore #5
    //   1229: dload #55
    //   1231: invokestatic sin : (D)D
    //   1234: dstore #57
    //   1236: dload #57
    //   1238: d2f
    //   1239: fload #45
    //   1241: fmul
    //   1242: fload #5
    //   1244: fadd
    //   1245: fstore #30
    //   1247: aload #22
    //   1249: fload #4
    //   1251: fload #30
    //   1253: invokespecial <init> : (FF)V
    //   1256: iload #41
    //   1258: iconst_1
    //   1259: iadd
    //   1260: istore #41
    //   1262: aload #22
    //   1264: getfield x : F
    //   1267: fstore #30
    //   1269: aload #22
    //   1271: getfield y : F
    //   1274: fstore #59
    //   1276: ldc 48.0
    //   1278: fstore #4
    //   1280: aload #19
    //   1282: fload #30
    //   1284: fload #59
    //   1286: fload #4
    //   1288: invokestatic safeNear : (Lmindustry/gen/Unit;FFF)Larc/math/geom/Vec2;
    //   1291: astore #60
    //   1293: getstatic lunarians/AutoUnitHeal.escortUsed : Larc/struct/IntSet;
    //   1296: astore_2
    //   1297: aload #19
    //   1299: getfield id : I
    //   1302: istore #61
    //   1304: aload_2
    //   1305: iload #61
    //   1307: invokevirtual add : (I)Z
    //   1310: pop
    //   1311: fconst_0
    //   1312: fstore #49
    //   1314: aconst_null
    //   1315: astore #31
    //   1317: aconst_null
    //   1318: astore #32
    //   1320: iconst_2
    //   1321: istore #62
    //   1323: ldc 2.8E-45
    //   1325: fstore #35
    //   1327: ldc 90.0
    //   1329: fstore #33
    //   1331: aload #19
    //   1333: iload #62
    //   1335: fload #33
    //   1337: aconst_null
    //   1338: aconst_null
    //   1339: aload #60
    //   1341: invokestatic cmd : (Lmindustry/gen/Unit;IFLmindustry/gen/Building;Lmindustry/gen/Unit;Larc/math/geom/Vec2;)Z
    //   1344: istore #28
    //   1346: iload #28
    //   1348: ifeq -> 1374
    //   1351: getstatic lunarians/AutoUnitHeal.count : I
    //   1354: iconst_1
    //   1355: iadd
    //   1356: istore #28
    //   1358: iload #28
    //   1360: putstatic lunarians/AutoUnitHeal.count : I
    //   1363: goto -> 1374
    //   1366: fload #4
    //   1368: fstore #50
    //   1370: fload #5
    //   1372: fstore #51
    //   1374: iload #46
    //   1376: iconst_1
    //   1377: iadd
    //   1378: istore #46
    //   1380: aload_1
    //   1381: astore_3
    //   1382: aload #48
    //   1384: astore_2
    //   1385: fload #50
    //   1387: fstore #4
    //   1389: fload #51
    //   1391: fstore #5
    //   1393: goto -> 833
    //   1396: return
  }
  
  public static boolean escortOn() {
    return Core.settings.getBool("lunarians-heal-escort", true);
  }
  
  public static void init() {
    boolean bool = Vars.headless;
    if (bool)
      return; 
    EventType.Trigger trigger = EventType.Trigger.update;
    AutoUnitHeal$0 autoUnitHeal$0 = new AutoUnitHeal$0();
    this();
    Events.run(trigger, autoUnitHeal$0);
    AutoUnitHeal$1 autoUnitHeal$1 = new AutoUnitHeal$1();
    this();
    Events.on(EventType.WorldLoadEvent.class, autoUnitHeal$1);
  }
  
  public static boolean isActiveHealer(int paramInt) {
    return activeHealers.contains(paramInt);
  }
  
  public static boolean isHealerType(UnitType paramUnitType) {
    String str1 = "heal";
    String str2 = "repair";
    String str3 = "vela";
    if (paramUnitType == null)
      return false; 
    boolean bool = true;
    try {
      String str = paramUnitType.name;
      if (str != null) {
        str = str.toLowerCase();
        boolean bool1 = str.equals(str3);
        if (!bool1) {
          boolean bool2 = str.contains(str3);
          if (!bool2) {
            str3 = "mega";
            bool2 = str.equals(str3);
            if (!bool2) {
              str3 = "poly";
              bool2 = str.equals(str3);
              if (!bool2) {
                str3 = "nova";
                bool2 = str.equals(str3);
                if (!bool2) {
                  bool2 = str.contains(str1);
                  if (!bool2) {
                    bool2 = str.contains(str2);
                    if (bool2)
                      return bool; 
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
              return bool;
            } 
          } else {
            return bool;
          } 
        } else {
          return bool;
        } 
      } 
    } finally {}
    try {
      Seq seq1 = paramUnitType.weapons;
      if (seq1 != null) {
        int j = 0;
        seq1 = null;
        while (true) {
          Seq seq2 = paramUnitType.weapons;
          int k = seq2.size;
          if (j < k) {
            seq2 = paramUnitType.weapons;
            Object object = seq2.get(j);
            object = object;
            if (object != null) {
              boolean bool1 = weaponHeals((Weapon)object);
              if (bool1)
                return bool; 
            } 
            j++;
            continue;
          } 
          break;
        } 
      } 
    } finally {}
    Seq seq = paramUnitType.abilities;
    if (seq == null)
      return false; 
    int i = 0;
    seq = null;
    while (true) {
      Seq seq1 = paramUnitType.abilities;
      int j = seq1.size;
      if (i < j) {
        Ability ability = (Ability)paramUnitType.abilities.get(i);
        if (ability != null) {
          boolean bool1 = ability instanceof mindustry.entities.abilities.RepairFieldAbility;
          if (bool1)
            return bool; 
          bool1 = ability instanceof mindustry.entities.abilities.UnitSpawnAbility;
          if (!bool1) {
            String str = ability.getClass().getSimpleName().toLowerCase();
            bool1 = str.contains(str2);
            if (!bool1) {
              bool1 = str.contains(str1);
              if (!bool1) {
                String str4 = "mend";
                boolean bool2 = str.contains(str4);
                if (!bool2)
                  continue; 
                continue;
              } 
              continue;
            } 
            return bool;
          } 
        } 
        continue;
      } 
      return false;
      i++;
    } 
  }
  
  private static Unit nearestNeedy(Unit paramUnit, Seq paramSeq) {
    Unit unit1 = null;
    Unit unit2 = null;
    float f1 = Float.MAX_VALUE;
    float f2 = Float.MAX_VALUE;
    int i = 0;
    while (true) {
      int j = paramSeq.size;
      if (i < j) {
        Unit unit = (Unit)paramSeq.get(i);
        int k = unit.id;
        int m = paramUnit.id;
        if (k != m) {
          IntSet intSet = activeHealers;
          m = unit.id;
          boolean bool = intSet.contains(m);
          if (!bool) {
            float f3 = paramUnit.dst((Position)unit);
            float f4 = unit.health;
            float f5 = unit.maxHealth;
            float f6 = Math.max(1.0F, f5);
            f4 /= f6;
            int n = 1056964608;
            f6 = 0.5F;
            f4 += f6;
            f3 *= f4;
            m = f3 cmp f1;
            if (m < 0) {
              unit2 = unit;
              f1 = f3;
            } 
            IntSet intSet1 = targeted;
            n = unit.id;
            boolean bool1 = intSet1.contains(n);
            if (!bool1) {
              int i1 = f3 cmp f2;
              if (i1 < 0) {
                unit1 = unit;
                f2 = f3;
              } 
            } 
          } 
        } 
        i++;
        continue;
      } 
      return (unit1 != null) ? unit1 : unit2;
    } 
  }
  
  public static void reset() {
    timer = 0.0F;
    count = 0;
    claimed.clear();
    activeHealers.clear();
    UnitCoord.reset();
  }
  
  private static void scan() {
    Seq seq = LunariansSettings.unitHealSlots;
    claimed.clear();
    activeHealers.clear();
    targeted.clear();
    escortUsed.clear();
    count = 0;
    if (seq != null) {
      boolean bool = seq.isEmpty();
      if (!bool) {
        Seq seq1 = new Seq();
        this();
        EntityGroup entityGroup = Groups.unit;
        int i = entityGroup.size();
        int j;
        for (j = 0; j < i; j++) {
          Unit unit = (Unit)Groups.unit.index(j);
          if (unit != null) {
            boolean bool2 = unit.isValid();
            if (bool2) {
              Team team1 = unit.team;
              Team team2 = Vars.player.team();
              if (team1 == team2) {
                float f1 = unit.health;
                float f2 = unit.maxHealth;
                float f3 = Math.max(1.0F, f2);
                f1 /= f3;
                int m = 1064514355;
                f3 = 0.95F;
                int k = f1 cmp f3;
                if (k < 0)
                  seq1.add(unit); 
              } 
            } 
          } 
        } 
        boolean bool1 = seq1.isEmpty();
        if (!bool1) {
          bool1 = false;
          while (true) {
            int k = seq.size;
            if (bool1 < k) {
              UnitHealSlot unitHealSlot = (UnitHealSlot)seq.get(bool1);
              if (unitHealSlot != null) {
                boolean bool2 = unitHealSlot.ready();
                if (bool2) {
                  Seq seq2 = new Seq();
                  this();
                  int n = 0;
                  float f = 0.0F;
                  IntSet intSet = null;
                  while (n < i) {
                    Unit unit = (Unit)Groups.unit.index(n);
                    if (unit != null) {
                      boolean bool3 = unit.isValid();
                      if (bool3) {
                        Team team1 = unit.team;
                        Team team2 = Vars.player.team();
                        if (team1 == team2) {
                          IntSet intSet1 = claimed;
                          int i1 = unit.id;
                          bool3 = intSet1.contains(i1);
                          if (!bool3) {
                            UnitController unitController = unit.controller();
                            bool3 = unitController instanceof mindustry.ai.types.CommandAI;
                            if (bool3) {
                              UnitType unitType = unit.type;
                              if (unitType != null) {
                                String str1 = unitHealSlot.unit;
                                String str2 = unit.type.name;
                                bool3 = str1.equals(str2);
                                if (bool3)
                                  seq2.add(unit); 
                              } 
                            } 
                          } 
                        } 
                      } 
                    } 
                    n++;
                  } 
                  n = unitHealSlot.maxUnits;
                  if (n > 0) {
                    n = seq2.size;
                    int i1 = unitHealSlot.maxUnits;
                    if (n > i1) {
                      k = unitHealSlot.maxUnits;
                      seq2.truncate(k);
                    } 
                  } 
                  k = 0;
                  unitHealSlot = null;
                  while (true) {
                    n = seq2.size;
                    if (k < n) {
                      intSet = claimed;
                      int i1 = ((Unit)seq2.get(k)).id;
                      intSet.add(i1);
                      intSet = activeHealers;
                      Unit unit = (Unit)seq2.get(k);
                      i1 = unit.id;
                      intSet.add(i1);
                      k++;
                      continue;
                    } 
                    k = 0;
                    unitHealSlot = null;
                    while (true) {
                      n = seq2.size;
                      if (k < n) {
                        Object object1 = seq2.get(k);
                        Object object2 = object1;
                        object2 = object1;
                        Unit unit = nearestNeedy((Unit)object2, seq1);
                        if (unit != null) {
                          object1 = targeted;
                          int i1 = unit.id;
                          object1.add(i1);
                          f = unit.angleTo((Position)object2) * 0.017453292F;
                          float f1 = unit.x;
                          double d1 = f;
                          double d2 = Math.cos(d1);
                          f = (float)d2;
                          float f2 = 24.0F;
                          f *= f2;
                          f1 += f;
                          f = unit.y;
                          d1 = Math.sin(d1);
                          float f3 = (float)d1 * f2;
                          f += f3;
                          Vec2 vec2 = PathSafe.safeNear((Unit)object2, f1, f, 40.0F);
                          int i2 = 1119092736;
                          f3 = 90.0F;
                          i1 = 3;
                          f1 = 4.2E-45F;
                          boolean bool3 = UnitCoord.cmd((Unit)object2, i1, f3, null, unit, vec2);
                          if (bool3) {
                            int i3 = count + 1;
                            count = i3;
                          } 
                          bool3 = escortOn();
                          if (bool3)
                            escort((Unit)object2, unit); 
                        } 
                        k++;
                        continue;
                      } 
                      break;
                    } 
                    break;
                  } 
                } 
              } 
              int m = bool1 + 1;
              continue;
            } 
            break;
          } 
        } 
      } 
    } 
  }
  
  private static void update() {
    boolean bool = LunariansSettings.enableAutoUnitHeal;
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
            int j = 1061997773;
            f2 = LunariansSettings.actionInterval(0.8F);
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
    activeHealers.clear();
  }
  
  private static boolean weaponHeals(Weapon paramWeapon) {
    String str = "healPercent";
    boolean bool = true;
    try {
      Class<?> clazz = paramWeapon.getClass();
      Field field = clazz.getField(str);
      float f = field.getFloat(paramWeapon);
      int i = f cmp 0.0F;
      if (i > 0)
        return bool; 
    } finally {}
    try {
      Class<?> clazz = paramWeapon.getClass();
      String str1 = "healAmount";
      Field field = clazz.getField(str1);
      float f = field.getFloat(paramWeapon);
      int i = f cmp 0.0F;
      if (i > 0)
        return bool; 
    } finally {}
    try {
      BulletType bulletType = paramWeapon.bullet;
      if (bulletType != null) {
        bulletType = paramWeapon.bullet;
        Class<?> clazz = bulletType.getClass();
        Field field = clazz.getField(str);
        BulletType bulletType1 = paramWeapon.bullet;
        float f = field.getFloat(bulletType1);
        int i = f cmp 0.0F;
        if (i > 0)
          return bool; 
      } 
    } finally {}
    return false;
  }
}
