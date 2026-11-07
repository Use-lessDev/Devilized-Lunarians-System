package lunarians;

import arc.Events;
import arc.math.geom.Position;
import arc.struct.Seq;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.ai.BlockIndexer;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.world.modules.ItemModule;

public class AutoFill {
  private static boolean pending;
  
  private static Item pendingItem;
  
  private static float pendingTimer;
  
  private static int previousAmount;
  
  private static float timer;
  
  private static Building findDeliveryTarget(Player paramPlayer, Unit paramUnit, Item paramItem, int paramInt, float paramFloat) {
    Seq seq = new Seq();
    this();
    BlockIndexer blockIndexer1 = Vars.indexer;
    Team team = paramPlayer.team();
    float f1 = paramUnit.x;
    float f2 = paramUnit.y;
    AutoFill$11 autoFill$11 = new AutoFill$11();
    this();
    AutoFill$8 autoFill$8 = new AutoFill$8();
    this(seq);
    blockIndexer1.eachBlock(team, f1, f2, paramFloat, autoFill$11, autoFill$8);
    Iterator iterator = seq.iterator();
    paramFloat = 0.0F;
    BlockIndexer blockIndexer2 = null;
    float f3 = Float.MAX_VALUE;
    while (true) {
      while (true) {
        boolean bool = iterator.hasNext();
        break;
      } 
      if (SYNTHETIC_LOCAL_VARIABLE_15 > null) {
        float f = paramUnit.dst((Position)blockIndexer1);
        int i = f cmp f3;
        if (i < 0) {
          blockIndexer2 = blockIndexer1;
          f3 = f;
        } 
      } 
    } 
  }
  
  public static void init() {
    boolean bool = Vars.headless;
    if (bool)
      return; 
    EventType.Trigger trigger = EventType.Trigger.update;
    AutoFill$9 autoFill$9 = new AutoFill$9();
    this();
    Events.run(trigger, autoFill$9);
    AutoFill$10 autoFill$10 = new AutoFill$10();
    this();
    Events.on(EventType.WorldLoadEvent.class, autoFill$10);
  }
  
  public static void update() {
    // Byte code:
    //   0: getstatic lunarians/LunariansSettings.enableAutoFill : Z
    //   3: istore_0
    //   4: iload_0
    //   5: ifeq -> 1072
    //   8: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   11: astore_1
    //   12: aload_1
    //   13: ifnull -> 1072
    //   16: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   19: astore_1
    //   20: aload_1
    //   21: invokevirtual isGame : ()Z
    //   24: istore_0
    //   25: iload_0
    //   26: ifeq -> 1072
    //   29: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   32: astore_1
    //   33: aload_1
    //   34: ifnull -> 1072
    //   37: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   40: invokevirtual unit : ()Lmindustry/gen/Unit;
    //   43: astore_1
    //   44: aload_1
    //   45: ifnull -> 1072
    //   48: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   51: astore_1
    //   52: aload_1
    //   53: invokevirtual dead : ()Z
    //   56: istore_0
    //   57: iload_0
    //   58: ifne -> 1072
    //   61: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   64: invokevirtual unit : ()Lmindustry/gen/Unit;
    //   67: astore_2
    //   68: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   71: astore_1
    //   72: aload_2
    //   73: getfield stack : Lmindustry/type/ItemStack;
    //   76: astore_3
    //   77: getstatic lunarians/AutoFill.pending : Z
    //   80: istore #4
    //   82: ldc 1114636288
    //   84: istore #5
    //   86: ldc 60.0
    //   88: fstore #6
    //   90: fconst_0
    //   91: fstore #7
    //   93: aconst_null
    //   94: astore #8
    //   96: iconst_1
    //   97: istore #9
    //   99: iload #4
    //   101: ifeq -> 276
    //   104: getstatic lunarians/AutoFill.pendingTimer : F
    //   107: fstore #10
    //   109: getstatic arc/util/Time.delta : F
    //   112: fload #6
    //   114: fdiv
    //   115: fstore #11
    //   117: fload #10
    //   119: fload #11
    //   121: fadd
    //   122: fstore #10
    //   124: fload #10
    //   126: putstatic lunarians/AutoFill.pendingTimer : F
    //   129: aload_3
    //   130: ifnull -> 161
    //   133: aload_3
    //   134: getfield amount : I
    //   137: istore #4
    //   139: getstatic lunarians/AutoFill.previousAmount : I
    //   142: istore #12
    //   144: iload #4
    //   146: iload #12
    //   148: if_icmpeq -> 161
    //   151: iconst_1
    //   152: istore #4
    //   154: ldc 1.4E-45
    //   156: fstore #10
    //   158: goto -> 170
    //   161: iconst_0
    //   162: istore #4
    //   164: fconst_0
    //   165: fstore #10
    //   167: aconst_null
    //   168: astore #13
    //   170: aload_3
    //   171: ifnull -> 213
    //   174: aload_3
    //   175: getfield item : Lmindustry/type/Item;
    //   178: astore #14
    //   180: getstatic lunarians/AutoFill.pendingItem : Lmindustry/type/Item;
    //   183: astore #15
    //   185: aload #14
    //   187: aload #15
    //   189: if_acmpne -> 213
    //   192: aload_3
    //   193: getfield amount : I
    //   196: istore #12
    //   198: iload #12
    //   200: ifle -> 213
    //   203: iconst_1
    //   204: istore #12
    //   206: ldc 1.4E-45
    //   208: fstore #11
    //   210: goto -> 222
    //   213: iconst_0
    //   214: istore #12
    //   216: fconst_0
    //   217: fstore #11
    //   219: aconst_null
    //   220: astore #14
    //   222: iload #4
    //   224: ifne -> 264
    //   227: iload #12
    //   229: ifne -> 264
    //   232: getstatic lunarians/AutoFill.pendingTimer : F
    //   235: fstore #10
    //   237: ldc_w 1067450368
    //   240: istore #12
    //   242: ldc_w 1.25
    //   245: fstore #11
    //   247: fload #10
    //   249: fload #11
    //   251: fcmpl
    //   252: istore #4
    //   254: iload #4
    //   256: ifge -> 264
    //   259: fconst_0
    //   260: putstatic lunarians/AutoFill.timer : F
    //   263: return
    //   264: iconst_0
    //   265: putstatic lunarians/AutoFill.pending : Z
    //   268: aconst_null
    //   269: putstatic lunarians/AutoFill.pendingItem : Lmindustry/type/Item;
    //   272: fconst_0
    //   273: putstatic lunarians/AutoFill.pendingTimer : F
    //   276: getstatic lunarians/AutoFill.timer : F
    //   279: fstore #10
    //   281: getstatic arc/util/Time.delta : F
    //   284: fload #6
    //   286: fdiv
    //   287: fstore #11
    //   289: fload #10
    //   291: fload #11
    //   293: fadd
    //   294: fstore #10
    //   296: fload #10
    //   298: putstatic lunarians/AutoFill.timer : F
    //   301: ldc_w 1028443341
    //   304: istore #5
    //   306: getstatic lunarians/LunariansSettings.autoFillInterval : F
    //   309: fstore #11
    //   311: ldc_w 0.05
    //   314: fload #11
    //   316: invokestatic max : (FF)F
    //   319: invokestatic actionInterval : (F)F
    //   322: fstore #6
    //   324: fload #10
    //   326: fload #6
    //   328: fcmpg
    //   329: istore #4
    //   331: iload #4
    //   333: iflt -> 1072
    //   336: fconst_0
    //   337: putstatic lunarians/AutoFill.timer : F
    //   340: getstatic lunarians/LunariansSettings.autoFillRange : F
    //   343: fstore #6
    //   345: ldc_w 1.0
    //   348: fload #6
    //   350: invokestatic max : (FF)F
    //   353: ldc_w 8.0
    //   356: fmul
    //   357: fstore #10
    //   359: ldc_w 1130102784
    //   362: istore #5
    //   364: ldc_w 220.0
    //   367: fstore #6
    //   369: fload #10
    //   371: fload #6
    //   373: invokestatic min : (FF)F
    //   376: fstore #16
    //   378: fload #16
    //   380: fconst_0
    //   381: fcmpg
    //   382: istore #4
    //   384: iload #4
    //   386: ifle -> 1072
    //   389: aload_3
    //   390: ifnull -> 524
    //   393: aload_3
    //   394: getfield item : Lmindustry/type/Item;
    //   397: astore #13
    //   399: aload #13
    //   401: ifnull -> 524
    //   404: aload_3
    //   405: getfield amount : I
    //   408: istore #4
    //   410: iload #4
    //   412: ifle -> 524
    //   415: aload_3
    //   416: getfield item : Lmindustry/type/Item;
    //   419: astore #13
    //   421: aload_3
    //   422: getfield amount : I
    //   425: istore #5
    //   427: aload_1
    //   428: aload_2
    //   429: aload #13
    //   431: iload #5
    //   433: fload #16
    //   435: invokestatic findDeliveryTarget : (Lmindustry/gen/Player;Lmindustry/gen/Unit;Lmindustry/type/Item;IF)Lmindustry/gen/Building;
    //   438: astore_1
    //   439: aload_1
    //   440: ifnull -> 1072
    //   443: aload_3
    //   444: getfield item : Lmindustry/type/Item;
    //   447: astore #13
    //   449: aload_3
    //   450: getfield amount : I
    //   453: istore #5
    //   455: aload_1
    //   456: aload #13
    //   458: iload #5
    //   460: aload_2
    //   461: invokevirtual acceptStack : (Lmindustry/type/Item;ILmindustry/gen/Teamc;)I
    //   464: istore #4
    //   466: iload #4
    //   468: ifle -> 1072
    //   471: aload_3
    //   472: getfield amount : I
    //   475: putstatic lunarians/AutoFill.previousAmount : I
    //   478: aload_3
    //   479: getfield item : Lmindustry/type/Item;
    //   482: putstatic lunarians/AutoFill.pendingItem : Lmindustry/type/Item;
    //   485: iload #9
    //   487: putstatic lunarians/AutoFill.pending : Z
    //   490: fconst_0
    //   491: putstatic lunarians/AutoFill.pendingTimer : F
    //   494: aload_3
    //   495: getfield item : Lmindustry/type/Item;
    //   498: astore_3
    //   499: aload_2
    //   500: getfield x : F
    //   503: fstore #6
    //   505: aload_2
    //   506: getfield y : F
    //   509: fstore #7
    //   511: aload_2
    //   512: aload_3
    //   513: iload #4
    //   515: fload #6
    //   517: fload #7
    //   519: aload_1
    //   520: invokestatic transferItemTo : (Lmindustry/gen/Unit;Lmindustry/type/Item;IFFLmindustry/gen/Building;)V
    //   523: return
    //   524: new arc/struct/Seq
    //   527: astore_3
    //   528: aload_3
    //   529: invokespecial <init> : ()V
    //   532: getstatic mindustry/Vars.indexer : Lmindustry/ai/BlockIndexer;
    //   535: astore #14
    //   537: aload_1
    //   538: invokevirtual team : ()Lmindustry/game/Team;
    //   541: astore #15
    //   543: aload_2
    //   544: getfield x : F
    //   547: fstore #17
    //   549: aload_2
    //   550: getfield y : F
    //   553: fstore #18
    //   555: new lunarians/AutoFill$0
    //   558: astore #19
    //   560: aload #19
    //   562: invokespecial <init> : ()V
    //   565: new lunarians/AutoFill$8
    //   568: astore #20
    //   570: aload #20
    //   572: aload_3
    //   573: invokespecial <init> : (Larc/struct/Seq;)V
    //   576: aload #14
    //   578: aload #15
    //   580: fload #17
    //   582: fload #18
    //   584: fload #16
    //   586: aload #19
    //   588: aload #20
    //   590: invokevirtual eachBlock : (Lmindustry/game/Team;FFFLarc/func/Boolf;Larc/func/Cons;)Z
    //   593: pop
    //   594: aload_3
    //   595: invokevirtual iterator : ()Ljava/util/Iterator;
    //   598: astore_1
    //   599: ldc_w 2139095039
    //   602: istore #21
    //   604: ldc 3.4028235E38
    //   606: fstore #22
    //   608: iconst_0
    //   609: istore #4
    //   611: aconst_null
    //   612: astore #13
    //   614: fconst_0
    //   615: fstore #10
    //   617: iconst_0
    //   618: istore #5
    //   620: fconst_0
    //   621: fstore #6
    //   623: aload_1
    //   624: invokeinterface hasNext : ()Z
    //   629: istore #12
    //   631: iload #12
    //   633: ifeq -> 1029
    //   636: aload_1
    //   637: invokeinterface next : ()Ljava/lang/Object;
    //   642: checkcast mindustry/gen/Building
    //   645: astore #14
    //   647: aload #14
    //   649: getfield items : Lmindustry/world/modules/ItemModule;
    //   652: astore #15
    //   654: aload #15
    //   656: ifnull -> 623
    //   659: aload #14
    //   661: getfield items : Lmindustry/world/modules/ItemModule;
    //   664: astore #15
    //   666: aload #15
    //   668: invokevirtual empty : ()Z
    //   671: istore #23
    //   673: iload #23
    //   675: ifne -> 623
    //   678: aload #14
    //   680: getfield block : Lmindustry/world/Block;
    //   683: astore #15
    //   685: aload #15
    //   687: instanceof mindustry/world/blocks/storage/CoreBlock
    //   690: istore #23
    //   692: aload #14
    //   694: getfield block : Lmindustry/world/Block;
    //   697: astore #24
    //   699: aload #24
    //   701: ifnull -> 827
    //   704: aload #14
    //   706: getfield block : Lmindustry/world/Block;
    //   709: getfield name : Ljava/lang/String;
    //   712: astore #24
    //   714: ldc 'container'
    //   716: astore #25
    //   718: aload #24
    //   720: aload #25
    //   722: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   725: istore #26
    //   727: iload #26
    //   729: ifne -> 817
    //   732: aload #14
    //   734: getfield block : Lmindustry/world/Block;
    //   737: getfield name : Ljava/lang/String;
    //   740: astore #24
    //   742: ldc 'vault'
    //   744: astore #25
    //   746: aload #24
    //   748: aload #25
    //   750: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   753: istore #26
    //   755: iload #26
    //   757: ifne -> 817
    //   760: aload #14
    //   762: getfield block : Lmindustry/world/Block;
    //   765: getfield name : Ljava/lang/String;
    //   768: astore #24
    //   770: ldc 'core'
    //   772: astore #25
    //   774: aload #24
    //   776: aload #25
    //   778: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   781: istore #26
    //   783: iload #26
    //   785: ifne -> 817
    //   788: aload #14
    //   790: getfield block : Lmindustry/world/Block;
    //   793: getfield name : Ljava/lang/String;
    //   796: astore #24
    //   798: ldc_w 'storage'
    //   801: astore #25
    //   803: aload #24
    //   805: aload #25
    //   807: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   810: istore #26
    //   812: iload #26
    //   814: ifeq -> 827
    //   817: iconst_1
    //   818: istore #26
    //   820: ldc 1.4E-45
    //   822: fstore #17
    //   824: goto -> 836
    //   827: iconst_0
    //   828: istore #26
    //   830: fconst_0
    //   831: fstore #17
    //   833: aconst_null
    //   834: astore #24
    //   836: iload #23
    //   838: ifne -> 846
    //   841: iload #26
    //   843: ifeq -> 623
    //   846: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   849: invokevirtual items : ()Larc/struct/Seq;
    //   852: invokevirtual iterator : ()Ljava/util/Iterator;
    //   855: astore #15
    //   857: aload #15
    //   859: invokeinterface hasNext : ()Z
    //   864: istore #26
    //   866: iload #26
    //   868: ifeq -> 623
    //   871: aload #15
    //   873: invokeinterface next : ()Ljava/lang/Object;
    //   878: checkcast mindustry/type/Item
    //   881: astore #24
    //   883: aload #24
    //   885: ifnull -> 857
    //   888: aload #14
    //   890: getfield items : Lmindustry/world/modules/ItemModule;
    //   893: astore #25
    //   895: aload #25
    //   897: aload #24
    //   899: invokevirtual get : (Lmindustry/type/Item;)I
    //   902: istore #27
    //   904: iload #27
    //   906: ifle -> 857
    //   909: aload_2
    //   910: aload #14
    //   912: invokevirtual dst : (Larc/math/geom/Position;)F
    //   915: fstore #16
    //   917: fload #16
    //   919: fload #22
    //   921: fcmpg
    //   922: istore #28
    //   924: iload #28
    //   926: ifge -> 857
    //   929: aload_2
    //   930: invokevirtual itemCapacity : ()I
    //   933: istore #21
    //   935: aload_2
    //   936: getfield stack : Lmindustry/type/ItemStack;
    //   939: astore #13
    //   941: aload #13
    //   943: getfield amount : I
    //   946: istore #4
    //   948: iload #4
    //   950: ifle -> 969
    //   953: aload_2
    //   954: getfield stack : Lmindustry/type/ItemStack;
    //   957: astore #13
    //   959: aload #13
    //   961: getfield amount : I
    //   964: istore #4
    //   966: goto -> 978
    //   969: iconst_0
    //   970: istore #4
    //   972: fconst_0
    //   973: fstore #10
    //   975: aconst_null
    //   976: astore #13
    //   978: iload #21
    //   980: iload #4
    //   982: isub
    //   983: istore #21
    //   985: iload #27
    //   987: iload #21
    //   989: invokestatic min : (II)I
    //   992: istore #5
    //   994: iload #5
    //   996: ifgt -> 1014
    //   999: aload_2
    //   1000: invokevirtual itemCapacity : ()I
    //   1003: istore #21
    //   1005: iload #27
    //   1007: iload #21
    //   1009: invokestatic min : (II)I
    //   1012: istore #5
    //   1014: aload #14
    //   1016: astore #8
    //   1018: aload #24
    //   1020: astore #13
    //   1022: fload #16
    //   1024: fstore #22
    //   1026: goto -> 857
    //   1029: aload #8
    //   1031: ifnull -> 1072
    //   1034: aload #13
    //   1036: ifnull -> 1072
    //   1039: iload #5
    //   1041: ifle -> 1072
    //   1044: iconst_0
    //   1045: putstatic lunarians/AutoFill.previousAmount : I
    //   1048: aload #13
    //   1050: putstatic lunarians/AutoFill.pendingItem : Lmindustry/type/Item;
    //   1053: iload #9
    //   1055: putstatic lunarians/AutoFill.pending : Z
    //   1058: fconst_0
    //   1059: putstatic lunarians/AutoFill.pendingTimer : F
    //   1062: aload #8
    //   1064: aload #13
    //   1066: iload #5
    //   1068: aload_2
    //   1069: invokestatic takeItems : (Lmindustry/gen/Building;Lmindustry/type/Item;ILmindustry/gen/Unit;)V
    //   1072: return
  }
}
