package lunarians;

import arc.Core;
import arc.Events;
import arc.Input;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.Scene;
import arc.struct.IntQueue;
import arc.struct.Queue;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.core.ContentLoader;
import mindustry.core.GameState;
import mindustry.core.UI;
import mindustry.core.World;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.game.Schematic;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.Tiles;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.storage.CoreBlock;

public class AutoSet {
  private static final int[] DX;
  
  private static final int[] DY;
  
  private static float armedAt;
  
  private static boolean centerChosen;
  
  private static int cx;
  
  private static int cy;
  
  private static int deleteCount;
  
  private static final HashSet deleted;
  
  private static boolean errorLogged;
  
  private static String[] factoryQueue;
  
  private static int feedIndex;
  
  private static final ArrayList feedItemNames;
  
  private static final HashMap feedLines;
  
  private static final ArrayList feedReserved;
  
  private static final ArrayList feedTargets;
  
  private static boolean hasArea;
  
  private static float lastMineHint;
  
  private static int lastPending;
  
  private static float lastProgress;
  
  private static float lastWarn;
  
  private static boolean miningActive;
  
  private static float nextCheck;
  
  private static float nextMineCheck;
  
  private static float nextStepAt;
  
  private static String[] oreOrder = new String[] { "copper", "copper", "copper", "lead", "coal", "sand", "coal", "sand" };
  
  private static final HashMap ours;
  
  private static final Seq pending;
  
  private static int phase;
  
  private static float radius;
  
  private static boolean registered;
  
  private static int runMode;
  
  private static final HashMap schemCache;
  
  private static final Set seenPlans;
  
  private static int steamDone;
  
  private static int stepIndex;
  
  private static boolean[] used;
  
  static {
    factoryQueue = new String[0];
    HashMap<Object, Object> hashMap2 = new HashMap<Object, Object>();
    this();
    ours = hashMap2;
    IdentityHashMap<Object, Object> identityHashMap = new IdentityHashMap<Object, Object>();
    this();
    seenPlans = Collections.newSetFromMap((Map)identityHashMap);
    HashSet hashSet = new HashSet();
    this();
    deleted = hashSet;
    HashMap<Object, Object> hashMap1 = new HashMap<Object, Object>();
    this();
    feedLines = hashMap1;
    hashMap1 = new HashMap<Object, Object>();
    this();
    schemCache = hashMap1;
    ArrayList arrayList = new ArrayList();
    this();
    feedTargets = arrayList;
    arrayList = new ArrayList();
    this();
    feedItemNames = arrayList;
    arrayList = new ArrayList();
    this();
    feedReserved = arrayList;
    cx = -1 << -1;
    Seq seq = new Seq();
    this();
    pending = seq;
  }
  
  private static void autoMine(Unit paramUnit) {
    // Byte code:
    //   0: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   3: astore_1
    //   4: aload_1
    //   5: ifnull -> 1069
    //   8: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   11: invokevirtual closestCore : ()Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;
    //   14: astore_1
    //   15: aload_1
    //   16: ifnull -> 1069
    //   19: getstatic lunarians/AutoSet.miningActive : Z
    //   22: istore_2
    //   23: fconst_0
    //   24: fstore_3
    //   25: aconst_null
    //   26: astore #4
    //   28: iload_2
    //   29: ifeq -> 58
    //   32: aload_0
    //   33: getfield mineTile : Lmindustry/world/Tile;
    //   36: astore_1
    //   37: aload_1
    //   38: ifnull -> 58
    //   41: aload_0
    //   42: iconst_0
    //   43: putfield updateBuilding : Z
    //   46: getstatic mindustry/Vars.control : Lmindustry/core/Control;
    //   49: getfield input : Lmindustry/input/InputHandler;
    //   52: astore_1
    //   53: aload_1
    //   54: iconst_0
    //   55: putfield isBuilding : Z
    //   58: getstatic arc/util/Time.time : F
    //   61: fstore #5
    //   63: getstatic lunarians/AutoSet.nextMineCheck : F
    //   66: fstore #6
    //   68: fload #5
    //   70: fload #6
    //   72: fcmpg
    //   73: istore_2
    //   74: iload_2
    //   75: iflt -> 1069
    //   78: getstatic arc/util/Time.time : F
    //   81: fstore #5
    //   83: ldc 20.0
    //   85: fstore #6
    //   87: fload #5
    //   89: fload #6
    //   91: fadd
    //   92: fstore #5
    //   94: fload #5
    //   96: putstatic lunarians/AutoSet.nextMineCheck : F
    //   99: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   102: invokevirtual closestCore : ()Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;
    //   105: astore_1
    //   106: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   109: invokevirtual items : ()Larc/struct/Seq;
    //   112: getfield size : I
    //   115: istore #7
    //   117: iload #7
    //   119: newarray int
    //   121: astore #8
    //   123: iconst_0
    //   124: istore #9
    //   126: aconst_null
    //   127: astore #10
    //   129: iconst_0
    //   130: istore #11
    //   132: fconst_0
    //   133: fstore #12
    //   135: aconst_null
    //   136: astore #13
    //   138: getstatic lunarians/AutoSet.pending : Larc/struct/Seq;
    //   141: astore #14
    //   143: aload #14
    //   145: getfield size : I
    //   148: istore #15
    //   150: iconst_1
    //   151: istore #16
    //   153: iload #9
    //   155: iload #15
    //   157: if_icmpge -> 394
    //   160: aload #14
    //   162: iload #9
    //   164: invokevirtual get : (I)Ljava/lang/Object;
    //   167: astore #14
    //   169: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   172: astore #17
    //   174: aload #14
    //   176: checkcast mindustry/entities/units/BuildPlan
    //   179: astore #14
    //   181: aload #14
    //   183: getfield x : I
    //   186: istore #18
    //   188: aload #14
    //   190: getfield y : I
    //   193: istore #19
    //   195: aload #17
    //   197: iload #18
    //   199: iload #19
    //   201: invokevirtual tile : (II)Lmindustry/world/Tile;
    //   204: astore #17
    //   206: aload #14
    //   208: getfield breaking : Z
    //   211: istore #18
    //   213: iload #18
    //   215: ifne -> 385
    //   218: aload #14
    //   220: getfield block : Lmindustry/world/Block;
    //   223: astore #20
    //   225: aload #20
    //   227: ifnull -> 385
    //   230: aload #17
    //   232: ifnull -> 256
    //   235: aload #17
    //   237: invokevirtual block : ()Lmindustry/world/Block;
    //   240: astore #17
    //   242: aload #14
    //   244: getfield block : Lmindustry/world/Block;
    //   247: astore #20
    //   249: aload #17
    //   251: aload #20
    //   253: if_acmpeq -> 385
    //   256: aload #14
    //   258: getfield block : Lmindustry/world/Block;
    //   261: getfield requirements : [Lmindustry/type/ItemStack;
    //   264: astore #14
    //   266: aload #14
    //   268: arraylength
    //   269: istore #15
    //   271: iconst_0
    //   272: istore #18
    //   274: fconst_0
    //   275: fstore #21
    //   277: aconst_null
    //   278: astore #20
    //   280: iload #18
    //   282: iload #15
    //   284: if_icmpge -> 385
    //   287: aload #14
    //   289: iload #18
    //   291: aaload
    //   292: astore #13
    //   294: aload #13
    //   296: getfield item : Lmindustry/type/Item;
    //   299: astore #22
    //   301: aload #22
    //   303: getfield id : S
    //   306: istore #19
    //   308: aload #8
    //   310: iload #19
    //   312: iaload
    //   313: istore #23
    //   315: aload #13
    //   317: getfield amount : I
    //   320: i2f
    //   321: fstore #12
    //   323: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   326: getfield rules : Lmindustry/game/Rules;
    //   329: astore #24
    //   331: aload #24
    //   333: getfield buildCostMultiplier : F
    //   336: fstore #25
    //   338: fload #12
    //   340: fload #25
    //   342: fmul
    //   343: f2d
    //   344: invokestatic ceil : (D)D
    //   347: dstore #26
    //   349: dload #26
    //   351: d2i
    //   352: istore #11
    //   354: iload #23
    //   356: iload #11
    //   358: iadd
    //   359: istore #23
    //   361: aload #8
    //   363: iload #19
    //   365: iload #23
    //   367: iastore
    //   368: iload #18
    //   370: iconst_1
    //   371: iadd
    //   372: istore #18
    //   374: iconst_1
    //   375: istore #11
    //   377: ldc_w 1.4E-45
    //   380: fstore #12
    //   382: goto -> 280
    //   385: iload #9
    //   387: iconst_1
    //   388: iadd
    //   389: istore #9
    //   391: goto -> 138
    //   394: iconst_0
    //   395: istore #9
    //   397: aconst_null
    //   398: astore #10
    //   400: iload #11
    //   402: ifeq -> 565
    //   405: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   408: getfield rules : Lmindustry/game/Rules;
    //   411: astore #13
    //   413: aload #13
    //   415: getfield infiniteResources : Z
    //   418: istore #11
    //   420: iload #11
    //   422: ifne -> 565
    //   425: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   428: invokevirtual items : ()Larc/struct/Seq;
    //   431: invokevirtual iterator : ()Ljava/util/Iterator;
    //   434: astore #13
    //   436: aload #13
    //   438: invokeinterface hasNext : ()Z
    //   443: istore #28
    //   445: iload #28
    //   447: ifeq -> 565
    //   450: aload #13
    //   452: invokeinterface next : ()Ljava/lang/Object;
    //   457: checkcast mindustry/type/Item
    //   460: astore #14
    //   462: aload #14
    //   464: getfield id : S
    //   467: istore #15
    //   469: aload #8
    //   471: iload #15
    //   473: iaload
    //   474: istore #15
    //   476: aload_1
    //   477: getfield items : Lmindustry/world/modules/ItemModule;
    //   480: astore #20
    //   482: aload #20
    //   484: aload #14
    //   486: invokevirtual get : (Lmindustry/type/Item;)I
    //   489: istore #18
    //   491: iload #15
    //   493: iload #18
    //   495: if_icmple -> 436
    //   498: aload_0
    //   499: getfield type : Lmindustry/type/UnitType;
    //   502: astore #17
    //   504: aload #17
    //   506: getfield mineTier : I
    //   509: istore #15
    //   511: aload #14
    //   513: getfield hardness : I
    //   516: istore #18
    //   518: iload #15
    //   520: iload #18
    //   522: if_icmplt -> 436
    //   525: aload_0
    //   526: getfield type : Lmindustry/type/UnitType;
    //   529: getfield mineItems : Larc/struct/Seq;
    //   532: astore #17
    //   534: aload #17
    //   536: ifnull -> 436
    //   539: aload_0
    //   540: getfield type : Lmindustry/type/UnitType;
    //   543: getfield mineItems : Larc/struct/Seq;
    //   546: astore #17
    //   548: aload #17
    //   550: aload #14
    //   552: invokevirtual contains : (Ljava/lang/Object;)Z
    //   555: istore #15
    //   557: iload #15
    //   559: ifne -> 571
    //   562: goto -> 436
    //   565: iconst_0
    //   566: istore #28
    //   568: aconst_null
    //   569: astore #14
    //   571: aload #14
    //   573: ifnonnull -> 581
    //   576: aload_0
    //   577: invokestatic stopMining : (Lmindustry/gen/Unit;)V
    //   580: return
    //   581: aload_0
    //   582: getfield type : Lmindustry/type/UnitType;
    //   585: astore_1
    //   586: aload_1
    //   587: getfield mineRange : F
    //   590: fstore #5
    //   592: ldc_w 8.0
    //   595: fstore #6
    //   597: fload #5
    //   599: fload #6
    //   601: fdiv
    //   602: fstore #5
    //   604: fload #5
    //   606: f2i
    //   607: iconst_2
    //   608: iadd
    //   609: istore_2
    //   610: aload_0
    //   611: invokevirtual tileX : ()I
    //   614: istore #7
    //   616: aload_0
    //   617: invokevirtual tileY : ()I
    //   620: istore #11
    //   622: iload #11
    //   624: iload_2
    //   625: isub
    //   626: istore #15
    //   628: ldc_w 2139095039
    //   631: istore #18
    //   633: ldc_w 3.4028235E38
    //   636: fstore #21
    //   638: iload #11
    //   640: iload_2
    //   641: iadd
    //   642: istore #19
    //   644: iload #15
    //   646: iload #19
    //   648: if_icmpgt -> 788
    //   651: iload #7
    //   653: iload_2
    //   654: isub
    //   655: istore #19
    //   657: iload #7
    //   659: iload_2
    //   660: iadd
    //   661: istore #23
    //   663: iload #19
    //   665: iload #23
    //   667: if_icmpgt -> 779
    //   670: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   673: iload #19
    //   675: iload #15
    //   677: invokevirtual tile : (II)Lmindustry/world/Tile;
    //   680: astore #29
    //   682: aload #29
    //   684: ifnull -> 770
    //   687: aload #29
    //   689: getfield build : Lmindustry/gen/Building;
    //   692: astore #24
    //   694: aload #24
    //   696: ifnonnull -> 770
    //   699: aload #29
    //   701: invokevirtual drop : ()Lmindustry/type/Item;
    //   704: astore #24
    //   706: aload #24
    //   708: aload #14
    //   710: if_acmpne -> 770
    //   713: aload_0
    //   714: aload #29
    //   716: invokevirtual validMine : (Lmindustry/world/Tile;)Z
    //   719: istore #30
    //   721: iload #30
    //   723: ifeq -> 770
    //   726: aload #29
    //   728: invokevirtual worldx : ()F
    //   731: fstore #25
    //   733: aload #29
    //   735: invokevirtual worldy : ()F
    //   738: fstore #31
    //   740: aload_0
    //   741: fload #25
    //   743: fload #31
    //   745: invokevirtual dst : (FF)F
    //   748: fstore #25
    //   750: fload #25
    //   752: fload #21
    //   754: fcmpg
    //   755: istore #32
    //   757: iload #32
    //   759: ifge -> 770
    //   762: aload #29
    //   764: astore #10
    //   766: fload #25
    //   768: fstore #21
    //   770: iload #19
    //   772: iconst_1
    //   773: iadd
    //   774: istore #19
    //   776: goto -> 657
    //   779: iload #15
    //   781: iconst_1
    //   782: iadd
    //   783: istore #15
    //   785: goto -> 638
    //   788: ldc_w 'Missing '
    //   791: astore_1
    //   792: ldc_w 'Faltando '
    //   795: astore #8
    //   797: aload #10
    //   799: ifnull -> 939
    //   802: getstatic lunarians/AutoSet.miningActive : Z
    //   805: istore #11
    //   807: iload #11
    //   809: ifne -> 905
    //   812: new java/lang/StringBuilder
    //   815: astore #13
    //   817: aload #13
    //   819: aload #8
    //   821: invokespecial <init> : (Ljava/lang/String;)V
    //   824: aload #14
    //   826: getfield localizedName : Ljava/lang/String;
    //   829: astore #8
    //   831: aload #13
    //   833: aload #8
    //   835: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   838: pop
    //   839: aload #13
    //   841: ldc_w ': minerando automaticamente'
    //   844: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   847: pop
    //   848: aload #13
    //   850: invokevirtual toString : ()Ljava/lang/String;
    //   853: astore #8
    //   855: new java/lang/StringBuilder
    //   858: astore #13
    //   860: aload #13
    //   862: aload_1
    //   863: invokespecial <init> : (Ljava/lang/String;)V
    //   866: aload #14
    //   868: getfield localizedName : Ljava/lang/String;
    //   871: astore_1
    //   872: aload #13
    //   874: aload_1
    //   875: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   878: pop
    //   879: aload #13
    //   881: ldc_w ': mining automatically'
    //   884: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   887: pop
    //   888: aload #13
    //   890: invokevirtual toString : ()Ljava/lang/String;
    //   893: astore_1
    //   894: aload #8
    //   896: aload_1
    //   897: invokestatic tr : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   900: astore_1
    //   901: aload_1
    //   902: invokestatic toast : (Ljava/lang/String;)V
    //   905: iload #16
    //   907: putstatic lunarians/AutoSet.miningActive : Z
    //   910: aload #10
    //   912: checkcast mindustry/world/Tile
    //   915: astore #10
    //   917: aload_0
    //   918: aload #10
    //   920: putfield mineTile : Lmindustry/world/Tile;
    //   923: aload_0
    //   924: iconst_0
    //   925: putfield updateBuilding : Z
    //   928: getstatic mindustry/Vars.control : Lmindustry/core/Control;
    //   931: getfield input : Lmindustry/input/InputHandler;
    //   934: iconst_0
    //   935: putfield isBuilding : Z
    //   938: return
    //   939: aload_0
    //   940: invokestatic stopMining : (Lmindustry/gen/Unit;)V
    //   943: getstatic arc/util/Time.time : F
    //   946: fstore #33
    //   948: getstatic lunarians/AutoSet.lastMineHint : F
    //   951: fstore_3
    //   952: fload #33
    //   954: fload_3
    //   955: fsub
    //   956: fstore #33
    //   958: ldc_w 600.0
    //   961: fstore_3
    //   962: fload #33
    //   964: fload_3
    //   965: fcmpl
    //   966: istore #34
    //   968: iload #34
    //   970: ifle -> 1069
    //   973: getstatic arc/util/Time.time : F
    //   976: fstore #33
    //   978: fload #33
    //   980: putstatic lunarians/AutoSet.lastMineHint : F
    //   983: new java/lang/StringBuilder
    //   986: astore_0
    //   987: aload_0
    //   988: aload #8
    //   990: invokespecial <init> : (Ljava/lang/String;)V
    //   993: aload #14
    //   995: getfield localizedName : Ljava/lang/String;
    //   998: astore #4
    //   1000: aload_0
    //   1001: aload #4
    //   1003: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1006: pop
    //   1007: aload_0
    //   1008: ldc_w ': aproxime sua unidade do minério para minerar automaticamente'
    //   1011: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1014: pop
    //   1015: aload_0
    //   1016: invokevirtual toString : ()Ljava/lang/String;
    //   1019: astore_0
    //   1020: new java/lang/StringBuilder
    //   1023: astore #4
    //   1025: aload #4
    //   1027: aload_1
    //   1028: invokespecial <init> : (Ljava/lang/String;)V
    //   1031: aload #14
    //   1033: getfield localizedName : Ljava/lang/String;
    //   1036: astore_1
    //   1037: aload #4
    //   1039: aload_1
    //   1040: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1043: pop
    //   1044: aload #4
    //   1046: ldc_w ': move your unit near its ore to auto-mine'
    //   1049: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1052: pop
    //   1053: aload #4
    //   1055: invokevirtual toString : ()Ljava/lang/String;
    //   1058: astore_1
    //   1059: aload_0
    //   1060: aload_1
    //   1061: invokestatic tr : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1064: astore_0
    //   1065: aload_0
    //   1066: invokestatic toast : (Ljava/lang/String;)V
    //   1069: return
  }
  
  private static Block blockByName(String paramString1, String paramString2) {
    ContentLoader contentLoader = Vars.content;
    Block block = contentLoader.block(paramString1);
    if (block == null && paramString2 != null)
      block = Vars.content.block(paramString2); 
    return block;
  }
  
  public static void cancel() {
    try {
      Unit unit;
      Player player = Vars.player;
      if (player == null) {
        player = null;
      } else {
        player = Vars.player;
        unit = player.unit();
      } 
      if (unit != null) {
        int i = phase;
        byte b = 3;
        if (i == b) {
          i = 0;
          while (true) {
            Seq seq = pending;
            int j = seq.size;
            if (i < j) {
              Queue queue = unit.plans();
              Object object = seq.get(i);
              object = object;
              queue.remove(object);
              i++;
              continue;
            } 
            break;
          } 
        } 
      } 
    } finally {}
    reset();
    toast(LunariansI18n.get("as_cancelled"));
  }
  
  private static void checkPending(Unit paramUnit) {
    // Byte code:
    //   0: getstatic arc/util/Time.time : F
    //   3: fstore_1
    //   4: getstatic lunarians/AutoSet.nextCheck : F
    //   7: fstore_2
    //   8: fload_1
    //   9: fload_2
    //   10: fcmpg
    //   11: istore_3
    //   12: iload_3
    //   13: iflt -> 807
    //   16: getstatic arc/util/Time.time : F
    //   19: fstore_1
    //   20: ldc_w 1097859072
    //   23: istore #4
    //   25: ldc_w 15.0
    //   28: fstore_2
    //   29: fload_1
    //   30: fload_2
    //   31: fadd
    //   32: fstore_1
    //   33: fload_1
    //   34: putstatic lunarians/AutoSet.nextCheck : F
    //   37: invokestatic rememberOurs : ()V
    //   40: getstatic lunarians/AutoSet.pending : Larc/struct/Seq;
    //   43: astore #5
    //   45: aload #5
    //   47: getfield size : I
    //   50: istore_3
    //   51: iconst_1
    //   52: istore #6
    //   54: ldc_w 1.4E-45
    //   57: fstore #7
    //   59: iload_3
    //   60: iload #6
    //   62: isub
    //   63: istore_3
    //   64: getstatic lunarians/AutoSet.pending : Larc/struct/Seq;
    //   67: astore #8
    //   69: iload_3
    //   70: iflt -> 160
    //   73: aload #8
    //   75: iload_3
    //   76: invokevirtual get : (I)Ljava/lang/Object;
    //   79: checkcast mindustry/entities/units/BuildPlan
    //   82: astore #9
    //   84: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   87: astore #10
    //   89: aload #9
    //   91: getfield x : I
    //   94: istore #11
    //   96: aload #9
    //   98: getfield y : I
    //   101: istore #12
    //   103: aload #10
    //   105: iload #11
    //   107: iload #12
    //   109: invokevirtual tile : (II)Lmindustry/world/Tile;
    //   112: astore #10
    //   114: aload #10
    //   116: ifnull -> 146
    //   119: aload #10
    //   121: checkcast mindustry/world/Tile
    //   124: pop
    //   125: aload #10
    //   127: invokevirtual block : ()Lmindustry/world/Block;
    //   130: astore #10
    //   132: aload #9
    //   134: getfield block : Lmindustry/world/Block;
    //   137: astore #9
    //   139: aload #10
    //   141: aload #9
    //   143: if_acmpne -> 153
    //   146: aload #8
    //   148: iload_3
    //   149: invokevirtual remove : (I)Ljava/lang/Object;
    //   152: pop
    //   153: iload_3
    //   154: iconst_m1
    //   155: iadd
    //   156: istore_3
    //   157: goto -> 64
    //   160: aload #8
    //   162: invokevirtual isEmpty : ()Z
    //   165: istore_3
    //   166: iload_3
    //   167: ifeq -> 179
    //   170: getstatic arc/util/Time.time : F
    //   173: fload_2
    //   174: fadd
    //   175: putstatic lunarians/AutoSet.nextStepAt : F
    //   178: return
    //   179: aload #8
    //   181: getfield size : I
    //   184: istore_3
    //   185: getstatic lunarians/AutoSet.lastPending : I
    //   188: istore #4
    //   190: iload_3
    //   191: iload #4
    //   193: if_icmpge -> 204
    //   196: getstatic arc/util/Time.time : F
    //   199: fstore_1
    //   200: fload_1
    //   201: putstatic lunarians/AutoSet.lastProgress : F
    //   204: aload #8
    //   206: getfield size : I
    //   209: putstatic lunarians/AutoSet.lastPending : I
    //   212: aload #8
    //   214: getfield size : I
    //   217: iload #6
    //   219: isub
    //   220: istore_3
    //   221: getstatic lunarians/AutoSet.pending : Larc/struct/Seq;
    //   224: astore #13
    //   226: iload_3
    //   227: iflt -> 357
    //   230: aload #13
    //   232: iload_3
    //   233: invokevirtual get : (I)Ljava/lang/Object;
    //   236: checkcast mindustry/entities/units/BuildPlan
    //   239: astore #8
    //   241: aload_0
    //   242: invokevirtual plans : ()Larc/struct/Queue;
    //   245: astore #9
    //   247: aload #9
    //   249: aload #8
    //   251: iload #6
    //   253: invokevirtual contains : (Ljava/lang/Object;Z)Z
    //   256: istore #14
    //   258: iload #14
    //   260: ifne -> 350
    //   263: aload #8
    //   265: getfield block : Lmindustry/world/Block;
    //   268: astore #9
    //   270: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   273: invokevirtual team : ()Lmindustry/game/Team;
    //   276: astore #10
    //   278: aload #8
    //   280: getfield x : I
    //   283: istore #11
    //   285: aload #8
    //   287: getfield y : I
    //   290: istore #12
    //   292: aload #8
    //   294: getfield rotation : I
    //   297: istore #15
    //   299: aload #9
    //   301: aload #10
    //   303: iload #11
    //   305: iload #12
    //   307: iload #15
    //   309: invokestatic validPlace : (Lmindustry/world/Block;Lmindustry/game/Team;III)Z
    //   312: istore #14
    //   314: iload #14
    //   316: ifeq -> 330
    //   319: aload_0
    //   320: aload #8
    //   322: iload #6
    //   324: invokevirtual addBuild : (Lmindustry/entities/units/BuildPlan;Z)V
    //   327: goto -> 350
    //   330: aload_0
    //   331: aload #8
    //   333: invokestatic tryDelete : (Lmindustry/gen/Unit;Lmindustry/entities/units/BuildPlan;)Z
    //   336: istore #16
    //   338: iload #16
    //   340: ifne -> 350
    //   343: aload #13
    //   345: iload_3
    //   346: invokevirtual remove : (I)Ljava/lang/Object;
    //   349: pop
    //   350: iload_3
    //   351: iconst_m1
    //   352: iadd
    //   353: istore_3
    //   354: goto -> 221
    //   357: aload #13
    //   359: invokevirtual isEmpty : ()Z
    //   362: istore_3
    //   363: iload_3
    //   364: ifeq -> 378
    //   367: getstatic arc/util/Time.time : F
    //   370: ldc_w 30.0
    //   373: fadd
    //   374: putstatic lunarians/AutoSet.nextStepAt : F
    //   377: return
    //   378: getstatic arc/util/Time.time : F
    //   381: fstore_1
    //   382: getstatic lunarians/AutoSet.lastProgress : F
    //   385: fstore #7
    //   387: fload_1
    //   388: fload #7
    //   390: fsub
    //   391: fstore_1
    //   392: ldc_w 1135869952
    //   395: istore #6
    //   397: ldc_w 360.0
    //   400: fstore #7
    //   402: ldc_w 'as_stalled'
    //   405: astore #8
    //   407: fload_1
    //   408: fload #7
    //   410: fcmpl
    //   411: istore_3
    //   412: iload_3
    //   413: ifle -> 619
    //   416: invokestatic missingResource : ()Ljava/lang/String;
    //   419: astore_0
    //   420: aload_0
    //   421: ifnull -> 537
    //   424: getstatic arc/util/Time.time : F
    //   427: fstore_1
    //   428: getstatic lunarians/AutoSet.lastProgress : F
    //   431: fstore #7
    //   433: fload_1
    //   434: fload #7
    //   436: fsub
    //   437: fstore_1
    //   438: ldc_w 1168687104
    //   441: istore #6
    //   443: ldc_w 5400.0
    //   446: fstore #7
    //   448: fload_1
    //   449: fload #7
    //   451: fcmpg
    //   452: istore_3
    //   453: iload_3
    //   454: ifge -> 537
    //   457: getstatic arc/util/Time.time : F
    //   460: fstore_1
    //   461: getstatic lunarians/AutoSet.lastWarn : F
    //   464: fstore_2
    //   465: fload_1
    //   466: fload_2
    //   467: fsub
    //   468: fstore_1
    //   469: ldc_w 1133903872
    //   472: istore #4
    //   474: ldc_w 300.0
    //   477: fstore_2
    //   478: fload_1
    //   479: fload_2
    //   480: fcmpl
    //   481: istore_3
    //   482: iload_3
    //   483: ifle -> 807
    //   486: getstatic arc/util/Time.time : F
    //   489: putstatic lunarians/AutoSet.lastWarn : F
    //   492: new java/lang/StringBuilder
    //   495: astore #5
    //   497: aload #5
    //   499: invokespecial <init> : ()V
    //   502: ldc_w 'Aguardando recurso: '
    //   505: ldc_w 'Waiting for resource: '
    //   508: invokestatic tr : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   511: astore #13
    //   513: aload #5
    //   515: aload #13
    //   517: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   520: pop
    //   521: aload #5
    //   523: aload_0
    //   524: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   527: pop
    //   528: aload #5
    //   530: invokevirtual toString : ()Ljava/lang/String;
    //   533: invokestatic toast : (Ljava/lang/String;)V
    //   536: return
    //   537: aload #13
    //   539: getfield size : I
    //   542: istore #17
    //   544: aload #13
    //   546: invokevirtual clear : ()Larc/struct/Seq;
    //   549: pop
    //   550: getstatic arc/util/Time.time : F
    //   553: ldc 20.0
    //   555: fadd
    //   556: putstatic lunarians/AutoSet.nextStepAt : F
    //   559: new java/lang/StringBuilder
    //   562: astore #5
    //   564: aload #5
    //   566: invokespecial <init> : ()V
    //   569: aload #8
    //   571: invokestatic get : (Ljava/lang/String;)Ljava/lang/String;
    //   574: astore #13
    //   576: aload #5
    //   578: aload #13
    //   580: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   583: pop
    //   584: aload #5
    //   586: ldc_w ' → skip ('
    //   589: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   592: pop
    //   593: aload #5
    //   595: iload #17
    //   597: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   600: pop
    //   601: aload #5
    //   603: ldc_w ')'
    //   606: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   609: pop
    //   610: aload #5
    //   612: invokevirtual toString : ()Ljava/lang/String;
    //   615: invokestatic toast : (Ljava/lang/String;)V
    //   618: return
    //   619: getstatic arc/util/Time.time : F
    //   622: fstore_1
    //   623: getstatic lunarians/AutoSet.lastProgress : F
    //   626: fstore_2
    //   627: fload_1
    //   628: fload_2
    //   629: fsub
    //   630: fstore_1
    //   631: ldc_w 1127481344
    //   634: istore #4
    //   636: ldc_w 180.0
    //   639: fstore_2
    //   640: fload_1
    //   641: fload_2
    //   642: fcmpl
    //   643: istore_3
    //   644: iload_3
    //   645: ifle -> 807
    //   648: getstatic arc/util/Time.time : F
    //   651: fstore_1
    //   652: getstatic lunarians/AutoSet.lastWarn : F
    //   655: fstore #7
    //   657: fload_1
    //   658: fload #7
    //   660: fsub
    //   661: fstore_1
    //   662: fload_1
    //   663: fload_2
    //   664: fcmpl
    //   665: istore_3
    //   666: iload_3
    //   667: ifle -> 807
    //   670: getstatic arc/util/Time.time : F
    //   673: fstore_1
    //   674: fload_1
    //   675: putstatic lunarians/AutoSet.lastWarn : F
    //   678: aload_0
    //   679: invokestatic nearestPending : (Lmindustry/gen/Unit;)Lmindustry/entities/units/BuildPlan;
    //   682: astore #5
    //   684: aload #5
    //   686: ifnull -> 797
    //   689: aload #5
    //   691: astore #13
    //   693: aload #5
    //   695: checkcast mindustry/entities/units/BuildPlan
    //   698: astore #13
    //   700: aload #5
    //   702: invokevirtual drawx : ()F
    //   705: fstore_2
    //   706: aload #5
    //   708: invokevirtual drawy : ()F
    //   711: fstore #7
    //   713: ldc_w 1130102784
    //   716: istore #14
    //   718: ldc_w 220.0
    //   721: fstore #18
    //   723: aload_0
    //   724: fload_2
    //   725: fload #7
    //   727: fload #18
    //   729: invokevirtual within : (FFF)Z
    //   732: istore #17
    //   734: iload #17
    //   736: ifne -> 797
    //   739: new java/lang/StringBuilder
    //   742: astore_0
    //   743: aload_0
    //   744: invokespecial <init> : ()V
    //   747: aload #5
    //   749: getfield x : I
    //   752: istore #4
    //   754: aload_0
    //   755: iload #4
    //   757: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   760: pop
    //   761: aload_0
    //   762: ldc_w ','
    //   765: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   768: pop
    //   769: aload #5
    //   771: getfield y : I
    //   774: istore_3
    //   775: aload_0
    //   776: iload_3
    //   777: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   780: pop
    //   781: aload_0
    //   782: invokevirtual toString : ()Ljava/lang/String;
    //   785: astore_0
    //   786: ldc_w 'as_approach'
    //   789: aload_0
    //   790: invokestatic get : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   793: invokestatic toast : (Ljava/lang/String;)V
    //   796: return
    //   797: aload #8
    //   799: invokestatic get : (Ljava/lang/String;)Ljava/lang/String;
    //   802: astore_0
    //   803: aload_0
    //   804: invokestatic toast : (Ljava/lang/String;)V
    //   807: return
  }
  
  private static boolean deletable(Block paramBlock) {
    boolean bool = paramBlock instanceof mindustry.world.blocks.distribution.Conveyor;
    if (!bool) {
      bool = paramBlock instanceof mindustry.world.blocks.distribution.Junction;
      if (!bool) {
        bool = paramBlock instanceof mindustry.world.blocks.distribution.ItemBridge;
        if (!bool) {
          bool = paramBlock instanceof mindustry.world.blocks.distribution.Router;
          if (!bool) {
            bool = paramBlock instanceof mindustry.world.blocks.distribution.Sorter;
            if (!bool) {
              bool = paramBlock instanceof mindustry.world.blocks.distribution.OverflowGate;
              if (!bool) {
                bool = paramBlock instanceof mindustry.world.blocks.production.Drill;
                if (!bool) {
                  bool = paramBlock instanceof mindustry.world.blocks.power.PowerNode;
                  if (!bool) {
                    boolean bool1 = paramBlock instanceof mindustry.world.blocks.liquid.Conduit;
                    if (!bool1)
                      return false; 
                  } 
                } 
              } 
            } 
          } 
        } 
      } 
    } 
    return true;
  }
  
  private static void draw() {
    // Byte code:
    //   0: getstatic lunarians/AutoSet.phase : I
    //   3: istore_0
    //   4: iconst_2
    //   5: istore_1
    //   6: ldc_w 2.8E-45
    //   9: fstore_2
    //   10: iload_0
    //   11: iload_1
    //   12: if_icmpeq -> 23
    //   15: getstatic lunarians/AutoSet.hasArea : Z
    //   18: istore_0
    //   19: iload_0
    //   20: ifeq -> 415
    //   23: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   26: astore_3
    //   27: aload_3
    //   28: ifnull -> 415
    //   31: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   34: astore_3
    //   35: aload_3
    //   36: invokevirtual isGame : ()Z
    //   39: istore_0
    //   40: iload_0
    //   41: ifeq -> 415
    //   44: getstatic lunarians/AutoSet.cx : I
    //   47: istore_0
    //   48: iconst_m1
    //   49: iconst_m1
    //   50: ishl
    //   51: istore #4
    //   53: fconst_0
    //   54: fneg
    //   55: fstore #5
    //   57: iload_0
    //   58: iload #4
    //   60: if_icmpeq -> 415
    //   63: iload_0
    //   64: i2f
    //   65: fstore #6
    //   67: ldc_w 1090519040
    //   70: istore #4
    //   72: ldc_w 8.0
    //   75: fstore #5
    //   77: fload #6
    //   79: fload #5
    //   81: fmul
    //   82: fstore #6
    //   84: getstatic lunarians/AutoSet.cy : I
    //   87: istore #7
    //   89: iload #7
    //   91: i2f
    //   92: fload #5
    //   94: fmul
    //   95: fstore #8
    //   97: getstatic lunarians/AutoSet.phase : I
    //   100: istore #9
    //   102: ldc_w 0.9
    //   105: fstore #10
    //   107: ldc_w 2.0
    //   110: fstore #11
    //   112: iload #9
    //   114: iload_1
    //   115: if_icmpne -> 238
    //   118: getstatic lunarians/AutoSet.centerChosen : Z
    //   121: istore_1
    //   122: iload_1
    //   123: ifeq -> 415
    //   126: getstatic arc/Core.input : Larc/Input;
    //   129: astore #12
    //   131: aload #12
    //   133: invokevirtual mouseWorld : ()Larc/math/geom/Vec2;
    //   136: astore #12
    //   138: aload #12
    //   140: getfield x : F
    //   143: fstore #5
    //   145: aload #12
    //   147: getfield y : F
    //   150: fstore_2
    //   151: fload #6
    //   153: fload #8
    //   155: fload #5
    //   157: fload_2
    //   158: invokestatic dst : (FFFF)F
    //   161: fstore_2
    //   162: ldc_w 1142947840
    //   165: istore #4
    //   167: ldc_w 640.0
    //   170: fstore #5
    //   172: fload_2
    //   173: fload #5
    //   175: invokestatic min : (FF)F
    //   178: fstore_2
    //   179: getstatic arc/graphics/Color.cyan : Larc/graphics/Color;
    //   182: astore #13
    //   184: ldc_w 1041865114
    //   187: istore #9
    //   189: ldc_w 0.15
    //   192: fstore #14
    //   194: aload #13
    //   196: fload #14
    //   198: invokestatic color : (Larc/graphics/Color;F)V
    //   201: fload #6
    //   203: fload #8
    //   205: fload_2
    //   206: invokestatic circle : (FFF)V
    //   209: getstatic arc/graphics/Color.cyan : Larc/graphics/Color;
    //   212: astore #13
    //   214: aload #13
    //   216: fload #10
    //   218: invokestatic color : (Larc/graphics/Color;F)V
    //   221: fload #11
    //   223: invokestatic stroke : (F)V
    //   226: fload #6
    //   228: fload #8
    //   230: fload_2
    //   231: invokestatic circle : (FFF)V
    //   234: invokestatic reset : ()V
    //   237: return
    //   238: getstatic lunarians/AutoSet.radius : F
    //   241: fload #5
    //   243: fmul
    //   244: fstore_2
    //   245: getstatic arc/graphics/Color.orange : Larc/graphics/Color;
    //   248: astore #13
    //   250: ldc_w 1039516303
    //   253: istore #9
    //   255: ldc_w 0.12
    //   258: fstore #14
    //   260: aload #13
    //   262: fload #14
    //   264: invokestatic color : (Larc/graphics/Color;F)V
    //   267: fload #6
    //   269: fload #8
    //   271: fload_2
    //   272: invokestatic circle : (FFF)V
    //   275: getstatic arc/graphics/Color.orange : Larc/graphics/Color;
    //   278: astore #13
    //   280: ldc_w 1062836634
    //   283: istore #9
    //   285: ldc_w 0.85
    //   288: fstore #14
    //   290: aload #13
    //   292: fload #14
    //   294: invokestatic color : (Larc/graphics/Color;F)V
    //   297: fload #11
    //   299: invokestatic stroke : (F)V
    //   302: fload #6
    //   304: fload #8
    //   306: fload_2
    //   307: invokestatic circle : (FFF)V
    //   310: getstatic arc/graphics/Color.yellow : Larc/graphics/Color;
    //   313: astore_3
    //   314: aload_3
    //   315: fload #10
    //   317: invokestatic color : (Larc/graphics/Color;F)V
    //   320: ldc_w 1065353216
    //   323: istore_0
    //   324: ldc_w 1.0
    //   327: fstore #6
    //   329: fload #6
    //   331: invokestatic stroke : (F)V
    //   334: iconst_0
    //   335: istore_0
    //   336: fconst_0
    //   337: fstore #6
    //   339: aconst_null
    //   340: astore_3
    //   341: getstatic lunarians/AutoSet.pending : Larc/struct/Seq;
    //   344: astore #12
    //   346: aload #12
    //   348: getfield size : I
    //   351: istore #4
    //   353: iload_0
    //   354: iload #4
    //   356: if_icmpge -> 412
    //   359: aload #12
    //   361: iload_0
    //   362: invokevirtual get : (I)Ljava/lang/Object;
    //   365: astore #12
    //   367: aload #12
    //   369: checkcast mindustry/entities/units/BuildPlan
    //   372: astore #12
    //   374: aload #12
    //   376: invokevirtual drawx : ()F
    //   379: fstore #5
    //   381: aload #12
    //   383: invokevirtual drawy : ()F
    //   386: fstore_2
    //   387: ldc_w 1082130432
    //   390: istore #7
    //   392: ldc_w 4.0
    //   395: fstore #8
    //   397: fload #5
    //   399: fload_2
    //   400: fload #8
    //   402: invokestatic square : (FFF)V
    //   405: iload_0
    //   406: iconst_1
    //   407: iadd
    //   408: istore_0
    //   409: goto -> 341
    //   412: invokestatic reset : ()V
    //   415: return
    //   416: pop
    //   417: goto -> 415
    // Exception table:
    //   from	to	target	type
    //   84	87	416	finally
    //   97	100	416	finally
    //   118	121	416	finally
    //   126	129	416	finally
    //   131	136	416	finally
    //   138	143	416	finally
    //   145	150	416	finally
    //   157	161	416	finally
    //   173	178	416	finally
    //   179	182	416	finally
    //   196	201	416	finally
    //   205	209	416	finally
    //   209	212	416	finally
    //   216	221	416	finally
    //   221	226	416	finally
    //   230	234	416	finally
    //   234	237	416	finally
    //   238	241	416	finally
    //   245	248	416	finally
    //   262	267	416	finally
    //   271	275	416	finally
    //   275	278	416	finally
    //   292	297	416	finally
    //   297	302	416	finally
    //   306	310	416	finally
    //   310	313	416	finally
    //   315	320	416	finally
    //   329	334	416	finally
    //   341	344	416	finally
    //   346	351	416	finally
    //   361	365	416	finally
    //   367	372	416	finally
    //   374	379	416	finally
    //   381	386	416	finally
    //   400	405	416	finally
    //   412	415	416	finally
  }
  
  private static String[] feedItems(String paramString) {
    String str1 = "silicon";
    boolean bool1 = str1.equals(paramString);
    String str2 = "sand";
    String str3 = "coal";
    if (bool1)
      return new String[] { str3, str2 }; 
    str1 = "metaglass";
    boolean bool2 = str1.equals(paramString);
    return bool2 ? new String[] { "lead", str2 } : new String[] { str3, null };
  }
  
  private static int[] findMarked(Item paramItem, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: astore_3
    //   2: aload_0
    //   3: invokestatic markedTiles : (Lmindustry/type/Item;)[I
    //   6: astore #4
    //   8: aload #4
    //   10: arraylength
    //   11: istore #5
    //   13: iload #5
    //   15: ifeq -> 439
    //   18: getstatic lunarians/AutoSet.used : [Z
    //   21: astore #6
    //   23: aload #6
    //   25: ifnull -> 439
    //   28: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   31: astore #6
    //   33: aload #6
    //   35: invokevirtual width : ()I
    //   38: istore #5
    //   40: aload #4
    //   42: arraylength
    //   43: istore #7
    //   45: iconst_m1
    //   46: istore #8
    //   48: iconst_m1
    //   49: istore #9
    //   51: iconst_m1
    //   52: iconst_m1
    //   53: ishl
    //   54: istore #10
    //   56: iconst_0
    //   57: istore #11
    //   59: iload #11
    //   61: iload #7
    //   63: if_icmpge -> 416
    //   66: aload #4
    //   68: iload #11
    //   70: iaload
    //   71: istore #12
    //   73: iload #12
    //   75: iflt -> 399
    //   78: getstatic lunarians/AutoSet.used : [Z
    //   81: astore #13
    //   83: aload #13
    //   85: arraylength
    //   86: istore #14
    //   88: iload #12
    //   90: iload #14
    //   92: if_icmpge -> 399
    //   95: aload #13
    //   97: iload #12
    //   99: baload
    //   100: istore #15
    //   102: iload #15
    //   104: ifne -> 399
    //   107: iload #12
    //   109: iload #5
    //   111: irem
    //   112: istore #15
    //   114: iload #12
    //   116: iload #5
    //   118: idiv
    //   119: istore #12
    //   121: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   124: iload #15
    //   126: iload #12
    //   128: invokevirtual tile : (II)Lmindustry/world/Tile;
    //   131: astore #16
    //   133: aload #16
    //   135: ifnull -> 399
    //   138: aload #16
    //   140: getfield build : Lmindustry/gen/Building;
    //   143: astore #17
    //   145: aload #17
    //   147: ifnonnull -> 399
    //   150: aload #16
    //   152: invokevirtual solid : ()Z
    //   155: istore #18
    //   157: iload #18
    //   159: ifne -> 399
    //   162: aload #16
    //   164: aload_3
    //   165: invokestatic sameOre : (Lmindustry/world/Tile;Lmindustry/type/Item;)Z
    //   168: istore #14
    //   170: iload #14
    //   172: ifeq -> 399
    //   175: iload #15
    //   177: i2f
    //   178: fstore #19
    //   180: iload #12
    //   182: i2f
    //   183: fstore #20
    //   185: iload_1
    //   186: i2f
    //   187: fstore #21
    //   189: iload_2
    //   190: i2f
    //   191: fstore #22
    //   193: fload #19
    //   195: fload #20
    //   197: fload #21
    //   199: fload #22
    //   201: invokestatic dst : (FFFF)F
    //   204: fstore #22
    //   206: fload #22
    //   208: f2i
    //   209: istore #23
    //   211: sipush #1000
    //   214: iload #23
    //   216: isub
    //   217: istore #23
    //   219: iconst_0
    //   220: istore #14
    //   222: fconst_0
    //   223: fstore #19
    //   225: aconst_null
    //   226: astore #16
    //   228: bipush #8
    //   230: istore #18
    //   232: ldc_w 1.1E-44
    //   235: fstore #20
    //   237: iload #14
    //   239: iload #18
    //   241: if_icmpge -> 373
    //   244: getstatic lunarians/AutoSet.DX : [I
    //   247: astore #17
    //   249: aload #17
    //   251: iload #14
    //   253: iaload
    //   254: iload #15
    //   256: iadd
    //   257: istore #18
    //   259: getstatic lunarians/AutoSet.DY : [I
    //   262: astore #24
    //   264: aload #24
    //   266: iload #14
    //   268: iaload
    //   269: iload #12
    //   271: iadd
    //   272: istore #25
    //   274: aload #4
    //   276: astore #26
    //   278: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   281: getfield tiles : Lmindustry/world/Tiles;
    //   284: astore #4
    //   286: aload #4
    //   288: iload #18
    //   290: iload #25
    //   292: invokevirtual in : (II)Z
    //   295: istore #27
    //   297: iload #27
    //   299: ifeq -> 360
    //   302: getstatic lunarians/AutoSet.used : [Z
    //   305: astore #4
    //   307: iload #25
    //   309: iload #5
    //   311: imul
    //   312: iload #18
    //   314: iadd
    //   315: istore #28
    //   317: aload #4
    //   319: iload #28
    //   321: baload
    //   322: istore #27
    //   324: iload #27
    //   326: ifne -> 360
    //   329: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   332: iload #18
    //   334: iload #25
    //   336: invokevirtual tile : (II)Lmindustry/world/Tile;
    //   339: astore #4
    //   341: aload #4
    //   343: aload_3
    //   344: invokestatic sameOre : (Lmindustry/world/Tile;Lmindustry/type/Item;)Z
    //   347: istore #27
    //   349: iload #27
    //   351: ifeq -> 360
    //   354: iload #23
    //   356: iconst_5
    //   357: iadd
    //   358: istore #23
    //   360: iload #14
    //   362: iconst_1
    //   363: iadd
    //   364: istore #14
    //   366: aload #26
    //   368: astore #4
    //   370: goto -> 228
    //   373: aload #4
    //   375: astore #26
    //   377: iload #23
    //   379: iload #10
    //   381: if_icmple -> 403
    //   384: iload #23
    //   386: istore #10
    //   388: iload #12
    //   390: istore #9
    //   392: iload #15
    //   394: istore #8
    //   396: goto -> 403
    //   399: aload #4
    //   401: astore #26
    //   403: iload #11
    //   405: iconst_1
    //   406: iadd
    //   407: istore #11
    //   409: aload #26
    //   411: astore #4
    //   413: goto -> 59
    //   416: iload #8
    //   418: ifge -> 423
    //   421: aconst_null
    //   422: areturn
    //   423: iconst_2
    //   424: newarray int
    //   426: astore_3
    //   427: aload_3
    //   428: iconst_0
    //   429: iload #8
    //   431: iastore
    //   432: aload_3
    //   433: iconst_1
    //   434: iload #9
    //   436: iastore
    //   437: aload_3
    //   438: areturn
    //   439: aconst_null
    //   440: areturn
  }
  
  private static int[] findOre(Item paramItem) {
    int i = cx;
    int j = cy;
    return findOre(paramItem, i, j);
  }
  
  private static int[] findOre(Item paramItem, int paramInt1, int paramInt2) {
    int[] arrayOfInt = findMarked(paramItem, paramInt1, paramInt2);
    byte b = 1;
    if (arrayOfInt != null) {
      AutoDrill.isoOverride = b;
      return arrayOfInt;
    } 
    AutoDrill.isoOverride = -1;
    int i = AutoSetConfig.spacing() + b;
    arrayOfInt = findOreM(paramItem, paramInt1, paramInt2, i);
    if (arrayOfInt == null) {
      int[] arrayOfInt1 = findOreM(paramItem, paramInt1, paramInt2, b);
      if (arrayOfInt1 != null)
        AutoDrill.isoOverride = b; 
      return arrayOfInt1;
    } 
    return arrayOfInt;
  }
  
  private static int[] findOreM(Item paramItem, int paramInt1, int paramInt2, int paramInt3) {
    Item item = paramItem;
    World world = Vars.world;
    int i = world.width();
    float f = radius;
    double d = Math.ceil(f);
    int j = (int)d;
    int k = cy - j;
    int m = -1;
    int n = -1;
    int i1 = -1 << -1;
    while (true) {
      int i2 = cy + j;
      if (k <= i2) {
        i2 = cx - j;
        while (true) {
          int i3 = cx + j;
          if (i2 <= i3) {
            Tiles tiles = Vars.world.tiles;
            boolean bool = tiles.in(i2, k);
            if (bool) {
              bool = inCircle(i2, k);
              if (bool) {
                boolean[] arrayOfBoolean = used;
                int i4 = k * i + i2;
                bool = arrayOfBoolean[i4];
                if (!bool) {
                  Tile tile = Vars.world.tile(i2, k);
                  if (tile != null) {
                    Building building = tile.build;
                    if (building == null) {
                      boolean bool1 = tile.solid();
                      if (!bool1) {
                        bool = sameOre(tile, item);
                        if (bool) {
                          float f1 = Float.MIN_VALUE;
                          int i5 = paramInt3;
                          bool = AutoDrill.crowded(i2, k, 1, paramInt3);
                          if (!bool) {
                            f1 = i2;
                            float f2 = k;
                            float f3 = paramInt1;
                            float f4 = paramInt2;
                            f1 = Mathf.dst(f1, f2, f3, f4);
                            int i6 = (int)f1;
                            i6 = 1000 - i6;
                            int i7 = 0;
                            f2 = 0.0F;
                            while (true) {
                              int i8 = 8;
                              f3 = 1.1E-44F;
                              if (i7 < i8) {
                                int[] arrayOfInt1 = DX;
                                i8 = arrayOfInt1[i7] + i2;
                                int[] arrayOfInt2 = DY;
                                int i9 = arrayOfInt2[i7] + k;
                                Tiles tiles1 = Vars.world.tiles;
                                boolean bool2 = tiles1.in(i8, i9);
                                if (bool2) {
                                  boolean[] arrayOfBoolean1 = used;
                                  int i10 = i9 * i + i8;
                                  bool2 = arrayOfBoolean1[i10];
                                  if (!bool2) {
                                    World world1 = Vars.world;
                                    Tile tile1 = world1.tile(i8, i9);
                                    boolean bool3 = sameOre(tile1, item);
                                    if (bool3)
                                      i6 += 5; 
                                  } 
                                } 
                                i7++;
                                continue;
                              } 
                              if (i6 > i1) {
                                n = k;
                                m = i2;
                                i1 = i6;
                              } 
                              break;
                            } 
                          } 
                        } 
                      } 
                    } 
                  } 
                } 
              } 
            } 
            i2++;
            continue;
          } 
          k++;
        } 
        break;
      } 
      return (m < 0) ? null : new int[] { m, n };
    } 
  }
  
  private static int[] findOutput(Seq paramSeq, HashSet paramHashSet) {
    World world = Vars.world;
    int i = world.width();
    int j = 0;
    while (true) {
      int k = paramSeq.size;
      if (j < k) {
        BuildPlan buildPlan = (BuildPlan)paramSeq.get(j);
        Block block = buildPlan.block;
        if (block != null) {
          String str = buildPlan.block.name;
          if (str != null) {
            str = buildPlan.block.name;
            String str1 = "conveyor";
            boolean bool = str.contains(str1);
            if (bool) {
              Tiles tiles = Vars.world.tiles;
              int m = buildPlan.x;
              int[] arrayOfInt = DX;
              int n = buildPlan.rotation & 0x3;
              int i1 = arrayOfInt[n];
              m += i1;
              k = buildPlan.y;
              arrayOfInt = DY;
              i1 = arrayOfInt[n];
              k += i1;
              bool = tiles.in(m, k);
              if (bool) {
                Integer integer = Integer.valueOf(k * i + m);
                bool = paramHashSet.contains(integer);
                if (!bool)
                  return new int[] { m, k, n }; 
              } 
            } 
          } 
        } 
        j++;
        continue;
      } 
      return null;
    } 
  }
  
  private static boolean freeFor(Block paramBlock, Team paramTeam, int paramInt1, int paramInt2, boolean[] paramArrayOfboolean) {
    Tiles tiles = Vars.world.tiles;
    boolean bool = tiles.in(paramInt1, paramInt2);
    if (bool) {
      World world = Vars.world;
      int i = world.width() * paramInt2 + paramInt1;
      boolean bool1 = paramArrayOfboolean[i];
      if (!bool1) {
        Tile tile = Vars.world.tile(paramInt1, paramInt2);
        if (tile != null) {
          Building building = tile.build;
          if (building == null)
            return Build.validPlace(paramBlock, paramTeam, paramInt1, paramInt2, 0); 
        } 
      } 
    } 
    return false;
  }
  
  private static int genInner() {
    Class<AutoDrill> clazz = AutoDrill.class;
    String str = "generateInner";
    try {
      new Class[0];
      Method method = clazz.getDeclaredMethod(str, null);
      boolean bool = true;
      method.setAccessible(bool);
      return object.intValue();
    } finally {
      clazz = null;
      Log.err("[Lunarians] AutoSet genInner", (Throwable)clazz);
    } 
  }
  
  private static void handleAreaPick() {
    Input input = Core.input;
    KeyCode keyCode = KeyCode.escape;
    boolean bool = input.keyTap(keyCode);
    if (!bool) {
      input = Core.input;
      keyCode = KeyCode.back;
      bool = input.keyTap(keyCode);
      if (!bool) {
        input = Core.input;
        bool = input.justTouched();
        if (bool) {
          Scene scene = Core.scene;
          bool = scene.hasMouse();
          if (!bool) {
            scene = Core.scene;
            bool = scene.hasDialog();
            if (!bool) {
              Vec2 vec2 = Core.input.mouseWorld();
              float f1 = vec2.x;
              int j = World.toTile(f1);
              Tiles tiles = Vars.world.tiles;
              float f2 = vec2.y;
              int i = World.toTile(f2);
              boolean bool1 = tiles.in(j, i);
              if (bool1) {
                bool1 = centerChosen;
                boolean bool2 = true;
                if (!bool1) {
                  cx = j;
                  cy = i;
                  centerChosen = bool2;
                  toast(LunariansI18n.get("as_tap_radius"));
                  return;
                } 
                int k = cx;
                float f3 = k;
                int m = cy;
                float f4 = m;
                f1 = j;
                f2 = i;
                f2 = Mathf.dst(f3, f4, f1, f2);
                f1 = 3.0F;
                j = f2 cmp f1;
                if (j < 0) {
                  toast(LunariansI18n.get("as_radius_small"));
                  return;
                } 
                radius = Math.min(f2, 80.0F);
                hasArea = bool2;
                phase = 3;
                stepIndex = 0;
                runMode = AutoSetConfig.mode();
                oreOrder = AutoSetConfig.oreOrder();
                factoryQueue = AutoSetConfig.factoryQueue();
                feedTargets.clear();
                feedItemNames.clear();
                feedReserved.clear();
                feedLines.clear();
                feedIndex = 0;
                pending.clear();
                j = Vars.world.width();
                World world = Vars.world;
                k = world.height();
                j *= k;
                used = new boolean[j];
                f1 = Time.time;
                k = 1092616192;
                f3 = 10.0F;
                nextStepAt = f1 + f3;
                f1 = Time.time;
                lastProgress = f1;
                lastPending = 0;
                f2 = radius;
                i = (int)f2;
                String str1 = String.valueOf(i);
                String str2 = "as_started";
                str1 = LunariansI18n.get(str2, str1);
                toast(str1);
              } 
            } 
          } 
        } 
        return;
      } 
    } 
    cancel();
  }
  
  private static boolean inCircle(int paramInt1, int paramInt2) {
    float f1 = paramInt1;
    float f2 = paramInt2;
    int i = cx;
    float f3 = i;
    int j = cy;
    float f4 = j;
    f1 = Mathf.dst(f1, f2, f3, f4);
    f2 = radius;
    paramInt1 = f1 cmp f2;
    return (paramInt1 <= 0);
  }
  
  public static void init() {
    boolean bool = registered;
    if (bool)
      return; 
    registered = true;
    EventType.Trigger trigger = EventType.Trigger.update;
    AutoSet$0 autoSet$0 = new AutoSet$0();
    this();
    Events.run(trigger, autoSet$0);
    trigger = EventType.Trigger.draw;
    AutoSet$3 autoSet$3 = new AutoSet$3();
    this();
    Events.run(trigger, autoSet$3);
  }
  
  public static boolean isActive() {
    int i = phase;
    return (i != 0);
  }
  
  public static boolean isRunning() {
    int i = phase;
    byte b = 3;
    return (i == b);
  }
  
  private static Item itemFor(String paramString) {
    Item item = Vars.content.item(paramString);
    if (item != null)
      return item; 
    String str = "copper";
    boolean bool1 = str.equals(paramString);
    if (bool1)
      return Items.copper; 
    str = "lead";
    bool1 = str.equals(paramString);
    if (bool1)
      return Items.lead; 
    str = "coal";
    bool1 = str.equals(paramString);
    if (bool1)
      return Items.coal; 
    str = "sand";
    boolean bool2 = str.equals(paramString);
    return bool2 ? Items.sand : null;
  }
  
  private static void markUsed(int paramInt1, int paramInt2, Item paramItem) {
    int i = paramInt1;
    int j = paramInt2;
    World world = Vars.world;
    int k = world.width();
    int m = LunariansSettings.drillMaxTiles;
    int n = Math.max(16, m);
    IntQueue intQueue = new IntQueue();
    this();
    int i1 = Vars.world.height() * k;
    boolean[] arrayOfBoolean = new boolean[i1];
    int i2 = paramInt2 * k + paramInt1;
    intQueue.addLast(i2);
    boolean bool = true;
    arrayOfBoolean[i2] = bool;
    int i3 = 0;
    label24: while (true) {
      boolean bool1 = intQueue.isEmpty();
      if (!bool1 && i3 < n) {
        int i4 = intQueue.removeFirst();
        boolean[] arrayOfBoolean1 = used;
        arrayOfBoolean1[i4] = bool;
        i3++;
        int i5 = i4 % k;
        i4 /= k;
        int i6 = 0;
        while (true) {
          int i7 = 4;
          if (i6 < i7) {
            int[] arrayOfInt1 = DX;
            i7 = arrayOfInt1[i6] + i5;
            int[] arrayOfInt2 = DY;
            int i8 = arrayOfInt2[i6] + i4;
            Tiles tiles = Vars.world.tiles;
            boolean bool2 = tiles.in(i7, i8);
            if (bool2) {
              int i9 = i8 * k + i7;
              boolean bool3 = arrayOfBoolean[i9];
              if (!bool3) {
                bool3 = inCircle(i7, i8);
                if (bool3) {
                  Tile tile = Vars.world.tile(i7, i8);
                  bool3 = sameOre(tile, paramItem);
                  if (bool3) {
                    boolean bool4 = AutoDrill.inFloor(i7, i8, i, j);
                    if (bool4) {
                      arrayOfBoolean[i9] = bool;
                      intQueue.addLast(i9);
                    } 
                  } 
                } 
              } 
            } 
            i6++;
            continue;
          } 
          continue label24;
        } 
      } 
      break;
    } 
  }
  
  private static String missingResource() {
    // Byte code:
    //   0: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   3: astore_0
    //   4: aload_0
    //   5: getfield rules : Lmindustry/game/Rules;
    //   8: astore_0
    //   9: aload_0
    //   10: getfield infiniteResources : Z
    //   13: istore_1
    //   14: iload_1
    //   15: ifne -> 318
    //   18: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   21: astore_0
    //   22: aload_0
    //   23: ifnull -> 318
    //   26: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   29: astore_0
    //   30: aload_0
    //   31: invokevirtual closestCore : ()Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;
    //   34: astore_0
    //   35: aload_0
    //   36: ifnull -> 318
    //   39: getstatic mindustry/Vars.state : Lmindustry/core/GameState;
    //   42: astore_0
    //   43: aload_0
    //   44: getfield rules : Lmindustry/game/Rules;
    //   47: astore_0
    //   48: aload_0
    //   49: getfield buildCostMultiplier : F
    //   52: fstore_2
    //   53: iconst_0
    //   54: istore_3
    //   55: getstatic lunarians/AutoSet.pending : Larc/struct/Seq;
    //   58: astore #4
    //   60: aload #4
    //   62: getfield size : I
    //   65: istore #5
    //   67: iload_3
    //   68: iload #5
    //   70: if_icmpge -> 318
    //   73: aload #4
    //   75: iload_3
    //   76: invokevirtual get : (I)Ljava/lang/Object;
    //   79: astore #4
    //   81: aload #4
    //   83: checkcast mindustry/entities/units/BuildPlan
    //   86: astore #4
    //   88: aload #4
    //   90: ifnull -> 310
    //   93: aload #4
    //   95: getfield block : Lmindustry/world/Block;
    //   98: astore #6
    //   100: aload #6
    //   102: ifnull -> 310
    //   105: aload #4
    //   107: getfield breaking : Z
    //   110: istore #5
    //   112: iload #5
    //   114: ifne -> 310
    //   117: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   120: astore #6
    //   122: aload #4
    //   124: getfield x : I
    //   127: istore #7
    //   129: aload #4
    //   131: getfield y : I
    //   134: istore #8
    //   136: aload #6
    //   138: iload #7
    //   140: iload #8
    //   142: invokevirtual tile : (II)Lmindustry/world/Tile;
    //   145: astore #6
    //   147: aload #6
    //   149: ifnull -> 173
    //   152: aload #6
    //   154: invokevirtual block : ()Lmindustry/world/Block;
    //   157: astore #6
    //   159: aload #4
    //   161: getfield block : Lmindustry/world/Block;
    //   164: astore #9
    //   166: aload #6
    //   168: aload #9
    //   170: if_acmpeq -> 310
    //   173: aload #4
    //   175: getfield block : Lmindustry/world/Block;
    //   178: astore #4
    //   180: aload #4
    //   182: getfield requirements : [Lmindustry/type/ItemStack;
    //   185: astore #4
    //   187: aload #4
    //   189: arraylength
    //   190: istore #5
    //   192: iconst_0
    //   193: istore #7
    //   195: aconst_null
    //   196: astore #9
    //   198: iload #7
    //   200: iload #5
    //   202: if_icmpge -> 310
    //   205: aload #4
    //   207: iload #7
    //   209: aaload
    //   210: astore #10
    //   212: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   215: astore #11
    //   217: aload #11
    //   219: invokevirtual closestCore : ()Lmindustry/world/blocks/storage/CoreBlock$CoreBuild;
    //   222: astore #11
    //   224: aload #11
    //   226: getfield items : Lmindustry/world/modules/ItemModule;
    //   229: astore #11
    //   231: aload #10
    //   233: getfield item : Lmindustry/type/Item;
    //   236: astore #12
    //   238: aload #11
    //   240: aload #12
    //   242: invokevirtual get : (Lmindustry/type/Item;)I
    //   245: istore #13
    //   247: iload #13
    //   249: i2d
    //   250: dstore #14
    //   252: aload #10
    //   254: getfield amount : I
    //   257: istore #16
    //   259: iload #16
    //   261: i2f
    //   262: fload_2
    //   263: fmul
    //   264: fstore #17
    //   266: fload #17
    //   268: f2d
    //   269: dstore #18
    //   271: dload #18
    //   273: invokestatic ceil : (D)D
    //   276: dstore #18
    //   278: dload #14
    //   280: dload #18
    //   282: dcmpl
    //   283: istore #20
    //   285: iload #20
    //   287: iflt -> 299
    //   290: iload #7
    //   292: iconst_1
    //   293: iadd
    //   294: istore #7
    //   296: goto -> 198
    //   299: aload #10
    //   301: getfield item : Lmindustry/type/Item;
    //   304: astore_0
    //   305: aload_0
    //   306: getfield localizedName : Ljava/lang/String;
    //   309: areturn
    //   310: iload_3
    //   311: iconst_1
    //   312: iadd
    //   313: istore_3
    //   314: goto -> 55
    //   317: pop
    //   318: aconst_null
    //   319: areturn
    // Exception table:
    //   from	to	target	type
    //   0	3	317	finally
    //   4	8	317	finally
    //   9	13	317	finally
    //   18	21	317	finally
    //   26	29	317	finally
    //   30	34	317	finally
    //   39	42	317	finally
    //   43	47	317	finally
    //   48	52	317	finally
    //   55	58	317	finally
    //   60	65	317	finally
    //   75	79	317	finally
    //   81	86	317	finally
    //   93	98	317	finally
    //   105	110	317	finally
    //   117	120	317	finally
    //   122	127	317	finally
    //   129	134	317	finally
    //   140	145	317	finally
    //   152	157	317	finally
    //   159	164	317	finally
    //   173	178	317	finally
    //   180	185	317	finally
    //   187	190	317	finally
    //   207	210	317	finally
    //   212	215	317	finally
    //   217	222	317	finally
    //   224	229	317	finally
    //   231	236	317	finally
    //   240	245	317	finally
    //   252	257	317	finally
    //   271	276	317	finally
    //   299	304	317	finally
    //   305	309	317	finally
  }
  
  private static BuildPlan nearestPending(Unit paramUnit) {
    BuildPlan buildPlan = null;
    float f = Float.MAX_VALUE;
    int i = 0;
    while (true) {
      Seq seq = pending;
      int j = seq.size;
      if (i < j) {
        BuildPlan buildPlan1 = (BuildPlan)seq.get(i);
        float f1 = paramUnit.x;
        float f2 = paramUnit.y;
        float f3 = buildPlan1.drawx();
        float f4 = buildPlan1.drawy();
        f1 = Mathf.dst(f1, f2, f3, f4);
        int k = f1 cmp f;
        if (k < 0) {
          buildPlan = buildPlan1;
          f = f1;
        } 
        i++;
        continue;
      } 
      return buildPlan;
    } 
  }
  
  private static int[] oreDistGrid(Item paramItem, int paramInt) {
    int i = (paramInt << 1) + 1;
    int j = i * i;
    int[] arrayOfInt = new int[j];
    byte b = -1;
    Arrays.fill(arrayOfInt, b);
    if (paramItem != null) {
      IntQueue intQueue = new IntQueue();
      this();
      int k = cx - paramInt;
      int m = cy - paramInt;
      World world = Vars.world;
      paramInt = world.width();
      int n = 0;
      int[] arrayOfInt1 = null;
      while (n < i) {
        int i1 = 0;
        Object object = null;
        while (i1 < i) {
          int i2 = k + i1;
          int i3 = m + n;
          Tiles tiles = Vars.world.tiles;
          boolean bool = tiles.in(i2, i3);
          if (bool) {
            bool = inCircle(i2, i3);
            if (bool) {
              boolean[] arrayOfBoolean = used;
              int i4 = i3 * paramInt + i2;
              bool = arrayOfBoolean[i4];
              if (!bool) {
                World world1 = Vars.world;
                Tile tile = world1.tile(i2, i3);
                if (tile != null) {
                  Building building = tile.build;
                  if (building == null) {
                    boolean bool1 = tile.solid();
                    if (!bool1) {
                      boolean bool2 = sameOre(tile, paramItem);
                      if (bool2) {
                        int i5 = n * i + i1;
                        arrayOfInt[i5] = 0;
                        intQueue.addLast(i5);
                      } 
                    } 
                  } 
                } 
              } 
            } 
          } 
          i1++;
        } 
        n++;
      } 
      label46: while (true) {
        boolean bool = intQueue.isEmpty();
        if (!bool) {
          int i1 = intQueue.removeFirst();
          paramInt = i1 % i;
          k = i1 / i;
          i1 = arrayOfInt[i1];
          m = 60;
          if (i1 < m) {
            m = 0;
            while (true) {
              n = 4;
              if (m < n) {
                arrayOfInt1 = DX;
                n = arrayOfInt1[m] + paramInt;
                int[] arrayOfInt2 = DY;
                int i2 = arrayOfInt2[m] + k;
                if (n >= 0 && i2 >= 0 && n < i && i2 < i) {
                  i2 = i2 * i + n;
                  n = arrayOfInt[i2];
                  if (n < 0) {
                    n = i1 + 1;
                    arrayOfInt[i2] = n;
                    intQueue.addLast(i2);
                  } 
                } 
                m++;
                continue;
              } 
              continue label46;
            } 
          } 
          continue;
        } 
        break;
      } 
    } 
    return arrayOfInt;
  }
  
  private static int oreScore(int[][] paramArrayOfint, int paramInt1, int paramInt2, int paramInt3) {
    int i = (paramInt3 << 1) + 1;
    int j = cx - paramInt3;
    paramInt1 -= j;
    j = cy - paramInt3;
    paramInt2 -= j;
    paramInt3 = 0;
    j = 0;
    while (true) {
      int k = paramArrayOfint.length;
      if (paramInt3 < k) {
        int[] arrayOfInt = paramArrayOfint[paramInt3];
        if (arrayOfInt != null) {
          if (paramInt1 >= 0 && paramInt2 >= 0 && paramInt1 < i && paramInt2 < i) {
            int m = paramInt2 * i + paramInt1;
            k = arrayOfInt[m];
          } else {
            k = -1;
          } 
          if (k < 0)
            k = 60; 
          j += k;
        } 
        paramInt3++;
        continue;
      } 
      return j;
    } 
  }
  
  private static boolean overlayOre(Tile paramTile, Item paramItem) {
    if (paramTile != null && paramItem != null) {
      Floor floor = paramTile.overlay();
      if (floor != null) {
        Item item = (paramTile.overlay()).itemDrop;
        if (item == paramItem)
          return true; 
      } 
    } 
    return false;
  }
  
  private static boolean planFits(Seq paramSeq, boolean[] paramArrayOfboolean, HashSet<Integer> paramHashSet) {
    World world = Vars.world;
    int i = world.width();
    int j = 0;
    while (true) {
      int k = paramSeq.size;
      if (j < k) {
        BuildPlan buildPlan = (BuildPlan)paramSeq.get(j);
        Block block = buildPlan.block;
        if (block == null)
          return false; 
        block = buildPlan.block;
        int m = block.size;
        int n = (m + -1) / 2;
        int i1;
        for (i1 = 0; i1 < m; i1++) {
          int i3 = 0;
          while (i3 < m) {
            int i4 = buildPlan.x - n + i3;
            int i5 = buildPlan.y - n + i1;
            Tiles tiles = Vars.world.tiles;
            boolean bool1 = tiles.in(i4, i5);
            if (bool1) {
              i5 = i5 * i + i4;
              boolean bool2 = paramArrayOfboolean[i5];
              if (!bool2) {
                Integer integer = Integer.valueOf(i5);
                paramHashSet.add(integer);
                i3++;
                continue;
              } 
            } 
            return false;
          } 
        } 
        block = buildPlan.block;
        Team team = Vars.player.team();
        i1 = buildPlan.x;
        int i2 = buildPlan.y;
        k = buildPlan.rotation;
        boolean bool = Build.validPlace(block, team, i1, i2, k);
        if (bool) {
          j++;
          continue;
        } 
        return false;
      } 
      return true;
    } 
  }
  
  private static void rememberOurs() {
    World world = Vars.world;
    int i = world.width();
    int j = 0;
    while (true) {
      Seq seq = pending;
      int k = seq.size;
      if (j < k) {
        BuildPlan buildPlan = (BuildPlan)seq.get(j);
        if (buildPlan != null) {
          Block block = buildPlan.block;
          if (block != null) {
            boolean bool = buildPlan.breaking;
            if (!bool) {
              Set<BuildPlan> set = seenPlans;
              bool = set.add(buildPlan);
              if (bool) {
                Block block1 = buildPlan.block;
                int m = block1.size;
                byte b = 1;
                m = (m - b) / 2;
                int n = 0;
                while (true) {
                  Block block2 = buildPlan.block;
                  int i1 = block2.size;
                  if (n < i1) {
                    i1 = 0;
                    block2 = null;
                    while (true) {
                      Block block3 = buildPlan.block;
                      int i2 = block3.size;
                      if (i1 < i2) {
                        HashMap<Integer, Integer> hashMap = ours;
                        int i3 = (buildPlan.y - m + n) * i;
                        int i4 = buildPlan.x;
                        i3 = i3 + i4 - m + i1;
                        Integer integer1 = Integer.valueOf(i3);
                        Integer integer2 = Integer.valueOf(b);
                        AutoDock$0 autoDock$0 = new AutoDock$0();
                        this();
                        hashMap.merge(integer1, integer2, autoDock$0);
                        i1++;
                        continue;
                      } 
                      n++;
                    } 
                  } 
                  break;
                } 
              } 
            } 
          } 
        } 
        j++;
        continue;
      } 
      break;
    } 
  }
  
  public static void request() {
    boolean bool = Vars.headless;
    if (!bool) {
      GameState gameState = Vars.state;
      if (gameState != null) {
        gameState = Vars.state;
        bool = gameState.isGame();
        if (bool) {
          int i = phase;
          byte b1 = 3;
          if (i == b1) {
            toast(LunariansI18n.get("as_running"));
            return;
          } 
          b1 = 2;
          if (i == b1) {
            toast(LunariansI18n.get("as_pick_hint"));
            return;
          } 
          byte b2 = 1;
          if (i == b2) {
            float f1 = Time.time;
            float f2 = armedAt;
            f1 -= f2;
            f2 = 180.0F;
            i = f1 cmp f2;
            if (i < 0) {
              phase = b1;
              centerChosen = false;
              hasArea = false;
              toast(LunariansI18n.get("as_tap_center"));
              return;
            } 
          } 
          phase = b2;
          armedAt = Time.time;
          toast(LunariansI18n.get("as_confirm"));
          return;
        } 
      } 
    } 
    toast(LunariansI18n.get("as_enter_map"));
  }
  
  public static void reset() {
    try {
      Unit unit;
      Player player = Vars.player;
      if (player == null) {
        player = null;
      } else {
        player = Vars.player;
        unit = player.unit();
      } 
      stopMining(unit);
    } finally {}
    phase = 0;
    deleteCount = 0;
    ours.clear();
    seenPlans.clear();
    deleted.clear();
    steamDone = 0;
    hasArea = false;
    stepIndex = 0;
    centerChosen = false;
    pending.clear();
    AutoDrill.limitOn = false;
    AutoDrill.quiet = false;
  }
  
  private static void rotatePlansAround(Seq paramSeq, int paramInt1, int paramInt2) {
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
            float f2 = (buildPlan.x - paramInt1 << 3) + f1;
            float f3 = -((buildPlan.y - paramInt2 << 3) + f1) - f1;
            float f4 = 8.0F;
            int m = Math.round(f3 / f4) + paramInt1;
            buildPlan.x = m;
            f2 = (f2 - f1) / f4;
            int k = Math.round(f2) + paramInt2;
            buildPlan.y = k;
            block = buildPlan.block;
            int n = buildPlan.rotation + 1;
            m = 4;
            f3 = 5.6E-45F;
            n = Mathf.mod(n, m);
            k = block.planRotation(n);
            buildPlan.rotation = k;
          } 
        } 
        i++;
        continue;
      } 
      break;
    } 
  }
  
  private static Seq routeTo(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean[] paramArrayOfboolean, Block paramBlock) {
    int i = paramInt1;
    int j = paramInt2;
    int k = paramInt3;
    int m = paramInt4;
    boolean[] arrayOfBoolean = paramArrayOfboolean;
    Block block = paramBlock;
    World world1 = Vars.world;
    int n = world1.width();
    World world2 = Vars.world;
    int i1 = world2.height();
    Team team = Vars.player.team();
    if (paramBlock != null) {
      boolean bool = freeFor(paramBlock, team, paramInt1, paramInt2, paramArrayOfboolean);
      if (bool) {
        bool = freeFor(paramBlock, team, paramInt3, paramInt4, paramArrayOfboolean);
        if (bool) {
          int i4;
          i1 *= n;
          int[] arrayOfInt = new int[i1];
          byte b = -2;
          Arrays.fill(arrayOfInt, b);
          ArrayDeque<Integer> arrayDeque = new ArrayDeque();
          this();
          j = paramInt2 * n + paramInt1;
          i = paramInt4 * n + paramInt3;
          k = -1;
          arrayOfInt[j] = k;
          Integer integer1 = Integer.valueOf(j);
          arrayDeque.add(integer1);
          m = 0;
          Integer integer2 = null;
          while (true) {
            boolean bool1 = arrayDeque.isEmpty();
            if (!bool1) {
              int i7 = arrayOfInt[i];
              if (i7 == b) {
                i7 = m + 1;
                int i8 = 60000;
                if (m < i8) {
                  integer2 = arrayDeque.poll();
                  m = integer2.intValue();
                  i8 = m % n;
                  int i9 = m / n;
                  int i10 = 0;
                  while (true) {
                    j = 4;
                    if (i10 < j) {
                      int i11;
                      int[] arrayOfInt1 = DX;
                      j = arrayOfInt1[i10] + i8;
                      int[] arrayOfInt2 = DY;
                      int i12 = arrayOfInt2[i10];
                      int i13 = i9 + i12;
                      Tiles tiles = Vars.world.tiles;
                      boolean bool2 = tiles.in(j, i13);
                      if (bool2) {
                        int i14 = i13 * n + j;
                        paramInt3 = i;
                        i = arrayOfInt[i14];
                        if (i == b) {
                          i4 = freeFor(block, team, j, i13, arrayOfBoolean);
                          if (i4) {
                            arrayOfInt[i14] = m;
                            Integer integer = Integer.valueOf(i14);
                            arrayDeque.add(integer);
                          } 
                        } 
                      } else {
                        i11 = i4;
                      } 
                      i10++;
                      i4 = i11;
                      continue;
                    } 
                    m = i7;
                  } 
                } 
              } 
            } 
            break;
          } 
          int i2 = i4;
          int i3 = arrayOfInt[i4];
          if (i3 == b)
            return null; 
          ArrayList<Integer> arrayList = new ArrayList();
          this();
          int i5 = i2;
          k = -1;
          while (i5 != k) {
            integer2 = Integer.valueOf(i5);
            arrayList.add(integer2);
            int i7 = arrayOfInt[i5];
          } 
          Collections.reverse(arrayList);
          Seq seq = new Seq();
          this();
          int i6 = 0;
          Object object = null;
          while (true) {
            m = arrayList.size();
            if (i6 < m) {
              integer2 = arrayList.get(i6);
              m = integer2.intValue();
              int i7 = m % n;
              m /= n;
              i6++;
              i1 = arrayList.size();
              if (i6 < i1) {
                Integer integer = arrayList.get(i6);
                i1 = integer.intValue();
                int i8 = i1 % n;
                i1 /= n;
                if (i8 > i7) {
                  i1 = 0;
                  integer = null;
                } else if (i8 < i7) {
                  i1 = 2;
                } else if (i1 > m) {
                  i1 = 1;
                } else {
                  i1 = 3;
                } 
              } else {
                i1 = paramInt5;
              } 
              BuildPlan buildPlan = new BuildPlan();
              this(i7, m, i1, block);
              seq.add(buildPlan);
              continue;
            } 
            return seq;
          } 
        } 
      } 
    } 
    return null;
  }
  
  private static void runSteps() {
    Unit unit;
    Player player = Vars.player;
    if (player == null) {
      player = null;
    } else {
      unit = Vars.player.unit();
    } 
    if (unit != null) {
      boolean bool = unit.canBuild();
      if (bool) {
        CoreBlock.CoreBuild coreBuild = Vars.player.closestCore();
        if (coreBuild == null) {
          toast(LunariansI18n.get("as_no_core"));
          reset();
          return;
        } 
        autoMine(unit);
        Seq seq = pending;
        int i = seq.size;
        if (i > 0) {
          checkPending(unit);
          return;
        } 
        float f1 = Time.time;
        float f2 = nextStepAt;
        i = f1 cmp f2;
        if (i >= 0) {
          i = runMode;
          int j = 1;
          f2 = Float.MIN_VALUE;
          if (i == 0) {
            boolean bool1 = AutoSetConfig.steamOn();
            if (bool1) {
              int m = steamDone;
              int n = AutoSetConfig.steamCount();
              if (m < n) {
                steamDone += j;
                nextStepAt = Time.time + 30.0F;
                startSteamStep(unit);
                return;
              } 
            } 
          } 
          i = runMode;
          int k = stepIndex;
          if (i == j) {
            String[] arrayOfString = factoryQueue;
            i = arrayOfString.length;
            if (k >= i) {
              i = feedIndex;
              ArrayList arrayList = feedTargets;
              k = arrayList.size();
              if (i >= k) {
                reset();
                toast(LunariansI18n.get("as_done"));
                return;
              } 
            } 
          } else {
            String[] arrayOfString = oreOrder;
            i = arrayOfString.length;
            if (k >= i) {
              reset();
              toast(LunariansI18n.get("as_done"));
              return;
            } 
          } 
          i = runMode;
          if (i == j) {
            i = stepIndex;
            String[] arrayOfString = factoryQueue;
            j = arrayOfString.length;
            if (i < j) {
              startFactoryStep(unit);
              return;
            } 
            startFeedStep(unit);
            return;
          } 
          startStep(unit);
        } 
      } 
    } 
  }
  
  private static boolean sameOre(Tile paramTile, Item paramItem) {
    boolean bool = false;
    if (paramTile != null && paramItem != null) {
      Item item;
      Floor floor1;
      boolean bool1 = true;
      try {
        Item item1 = paramTile.drop();
        if (item1 != null) {
          item = paramTile.drop();
          return (item == paramItem) ? bool1 : false;
        } 
      } finally {}
      Floor floor2 = item.overlay();
      if (floor2 != null) {
        floor1 = item.overlay();
      } else {
        floor1 = floor1.floor();
      } 
      if (floor1 != null) {
        Item item1 = floor1.itemDrop;
        if (item1 == paramItem)
          bool = true; 
      } 
    } 
    return bool;
  }
  
  private static Schematic schemFor(String paramString) {
    Schematic schematic = (Schematic)schemCache.get(paramString);
    if (schematic == null) {
      try {
      
      } finally {
        Exception exception = null;
        StringBuilder stringBuilder = new StringBuilder();
        String str2 = "[Lunarians] schematic ";
        this(str2);
        stringBuilder.append(paramString);
        String str1 = stringBuilder.toString();
      } 
      if (schematic != null) {
        HashMap<String, Schematic> hashMap = schemCache;
        hashMap.put(paramString, schematic);
      } 
    } 
    return schematic;
  }
  
  private static void setExitTowardCore(int paramInt1, int paramInt2) {
    try {
      Player player = Vars.player;
      if (player != null) {
        player = Vars.player;
        CoreBlock.CoreBuild coreBuild = player.closestCore();
        if (coreBuild != null) {
          Player player2 = Vars.player;
          CoreBlock.CoreBuild coreBuild2 = player2.closestCore();
          int i = coreBuild2.tileX() - paramInt1;
          float f1 = i;
          Player player1 = Vars.player;
          CoreBlock.CoreBuild coreBuild1 = player1.closestCore();
          i = coreBuild1.tileY() - paramInt2;
          float f2 = i;
          float f3 = Math.abs(f1);
          float f4 = Math.abs(f2);
          i = f3 cmp f4;
          if (i >= 0) {
            paramInt1 = f1 cmp 0.0F;
            if (paramInt1 >= 0) {
              paramInt1 = 0;
              f1 = 0.0F;
            } else {
              paramInt1 = 2;
              f1 = 2.8E-45F;
            } 
          } else {
            paramInt1 = f2 cmp 0.0F;
            if (paramInt1 >= 0) {
              paramInt1 = 1;
              f1 = Float.MIN_VALUE;
            } else {
              paramInt1 = 3;
              f1 = 4.2E-45F;
            } 
          } 
          LunariansSettings.drillExitDir = paramInt1;
        } 
      } 
    } finally {}
  }
  
  private static void startFactoryStep(Unit paramUnit) {
    Unit unit = paramUnit;
    String[] arrayOfString = factoryQueue;
    int i = stepIndex;
    String str1 = arrayOfString[i];
    byte b = 1;
    float f1 = Float.MIN_VALUE;
    i += b;
    stepIndex = i;
    float f2 = Time.time;
    int j = 1106247680;
    float f3 = 30.0F;
    f2 += f3;
    nextStepAt = f2;
    Schematic schematic = schemFor(str1);
    if (schematic != null) {
      CoreBlock.CoreBuild coreBuild = Vars.player.closestCore();
      if (coreBuild != null) {
        Item item1;
        Item item2;
        int[] arrayOfInt2;
        coreBuild = Vars.player.closestCore();
        j = coreBuild.tileX();
        CoreBlock.CoreBuild coreBuild1 = Vars.player.closestCore();
        int k = coreBuild1.tileY();
        World world = Vars.world;
        int m = world.width();
        float f4 = radius;
        double d = Math.ceil(f4);
        int n = (int)d;
        String[] arrayOfString1 = feedItems(str1);
        int i1 = 0;
        float f5 = 0.0F;
        Object object = null;
        String str4 = arrayOfString1[0];
        boolean bool = false;
        int[] arrayOfInt1 = null;
        if (str4 == null) {
          boolean bool1 = false;
          str4 = null;
        } else {
          item2 = itemFor(str4);
        } 
        String str3 = arrayOfString1[b];
        if (str3 == null) {
          boolean bool1 = false;
          str3 = null;
        } else {
          item1 = itemFor(str3);
        } 
        if (item2 == null) {
          boolean bool1 = false;
          arrayOfInt2 = null;
        } else {
          arrayOfInt2 = oreDistGrid(item2, n);
        } 
        if (item1 != null)
          arrayOfInt1 = oreDistGrid(item1, n); 
        byte b1 = 2;
        float f6 = 2.8E-45F;
        int[][] arrayOfInt = new int[b1][];
        arrayOfInt[0] = arrayOfInt2;
        arrayOfInt[b] = arrayOfInt1;
        ArrayList<?> arrayList = new ArrayList();
        this();
        int i2 = cy - n;
        while (true) {
          int i3 = cy + n;
          if (i2 <= i3) {
            i3 = cx - n;
            while (true) {
              int i5 = cx;
              i1 = i5 + n;
              i2++;
            } 
            break;
          } 
          int i4 = j;
          boolean bool1 = true;
          byte b2 = 2;
          AutoSet$2 autoSet$2 = new AutoSet$2();
          this();
          Collections.sort(arrayList, autoSet$2);
          boolean[] arrayOfBoolean = AutoConnectConveyor.reservedFromPlans(unit);
          j = 0;
          f3 = 0.0F;
          coreBuild = null;
          while (true) {
            ArrayList arrayList1 = feedReserved;
            i1 = arrayList1.size();
            if (j < i1)
              continue; 
            j = 0;
            f3 = 0.0F;
            coreBuild = null;
            while (true) {
              n = 3;
              f4 = 4.2E-45F;
              schematic1 = schematic;
              arrayOfBoolean1 = arrayOfBoolean;
              i7 = j;
              i5 = k;
              i6 = m;
              item = item1;
              j++;
              bool1 = true;
              b2 = 2;
            } 
            break;
            integer = SYNTHETIC_LOCAL_VARIABLE_38.get(j);
            n = integer.intValue();
            arrayOfBoolean[n] = bool1;
            j++;
          } 
          break;
        } 
      } 
    } 
    StringBuilder stringBuilder = new StringBuilder();
    this();
    String str2 = tr("Schematic inválido: ", "Invalid schematic: ");
    stringBuilder.append(str2);
    stringBuilder.append(str1);
    toast(stringBuilder.toString());
  }
  
  private static void startFeedStep(Unit paramUnit) {
    // Byte code:
    //   0: aload_0
    //   1: astore_1
    //   2: getstatic lunarians/AutoSet.feedTargets : Ljava/util/ArrayList;
    //   5: astore_2
    //   6: getstatic lunarians/AutoSet.feedIndex : I
    //   9: istore_3
    //   10: aload_2
    //   11: iload_3
    //   12: invokevirtual get : (I)Ljava/lang/Object;
    //   15: checkcast [I
    //   18: astore_2
    //   19: getstatic lunarians/AutoSet.feedItemNames : Ljava/util/ArrayList;
    //   22: astore #4
    //   24: getstatic lunarians/AutoSet.feedIndex : I
    //   27: istore #5
    //   29: aload #4
    //   31: iload #5
    //   33: invokevirtual get : (I)Ljava/lang/Object;
    //   36: checkcast java/lang/String
    //   39: astore #4
    //   41: getstatic lunarians/AutoSet.feedIndex : I
    //   44: istore #5
    //   46: iconst_1
    //   47: istore #6
    //   49: ldc_w 1.4E-45
    //   52: fstore #7
    //   54: iload #5
    //   56: iload #6
    //   58: iadd
    //   59: istore #5
    //   61: iload #5
    //   63: putstatic lunarians/AutoSet.feedIndex : I
    //   66: getstatic arc/util/Time.time : F
    //   69: fstore #8
    //   71: ldc_w 1106247680
    //   74: istore #9
    //   76: ldc_w 30.0
    //   79: fstore #10
    //   81: fload #8
    //   83: fload #10
    //   85: fadd
    //   86: fstore #8
    //   88: fload #8
    //   90: putstatic lunarians/AutoSet.nextStepAt : F
    //   93: aload #4
    //   95: invokestatic itemFor : (Ljava/lang/String;)Lmindustry/type/Item;
    //   98: astore #11
    //   100: ldc_w 'conveyor'
    //   103: astore #12
    //   105: ldc_w 'titanium-conveyor'
    //   108: astore #13
    //   110: aload #12
    //   112: aload #13
    //   114: invokestatic blockByName : (Ljava/lang/String;Ljava/lang/String;)Lmindustry/world/Block;
    //   117: astore #14
    //   119: aload #11
    //   121: ifnull -> 1837
    //   124: aload #14
    //   126: ifnull -> 1837
    //   129: iload #6
    //   131: putstatic lunarians/AutoDrill.limitOn : Z
    //   134: getstatic lunarians/AutoSet.cx : I
    //   137: i2f
    //   138: putstatic lunarians/AutoDrill.limitX : F
    //   141: getstatic lunarians/AutoSet.cy : I
    //   144: i2f
    //   145: putstatic lunarians/AutoDrill.limitY : F
    //   148: getstatic lunarians/AutoSet.radius : F
    //   151: fstore #10
    //   153: fload #10
    //   155: putstatic lunarians/AutoDrill.limitR : F
    //   158: iload #6
    //   160: putstatic lunarians/AutoDrill.quiet : Z
    //   163: getstatic lunarians/LunariansSettings.drillExitDir : I
    //   166: istore #9
    //   168: iconst_0
    //   169: istore #15
    //   171: aconst_null
    //   172: astore #13
    //   174: iconst_0
    //   175: istore #16
    //   177: fconst_0
    //   178: fstore #17
    //   180: aconst_null
    //   181: astore #18
    //   183: iconst_0
    //   184: istore #19
    //   186: bipush #6
    //   188: istore #20
    //   190: ldc_w 8.4E-45
    //   193: fstore #21
    //   195: iload #19
    //   197: iload #20
    //   199: if_icmpge -> 1660
    //   202: iload #16
    //   204: ifne -> 1660
    //   207: aload_2
    //   208: iconst_0
    //   209: iaload
    //   210: istore #20
    //   212: aload_2
    //   213: iload #6
    //   215: iaload
    //   216: istore #22
    //   218: aload #11
    //   220: iload #20
    //   222: iload #22
    //   224: invokestatic findOre : (Lmindustry/type/Item;II)[I
    //   227: astore #23
    //   229: aload #23
    //   231: ifnull -> 1660
    //   234: aload #23
    //   236: iconst_0
    //   237: iaload
    //   238: istore #22
    //   240: aload #23
    //   242: iload #6
    //   244: iaload
    //   245: istore #24
    //   247: iload #22
    //   249: iload #24
    //   251: aload #11
    //   253: invokestatic markUsed : (IILmindustry/type/Item;)V
    //   256: aload #23
    //   258: iconst_0
    //   259: iaload
    //   260: istore #22
    //   262: iload #22
    //   264: putstatic lunarians/LunariansSettings.drillStartX : I
    //   267: aload #23
    //   269: iload #6
    //   271: iaload
    //   272: istore #22
    //   274: iload #22
    //   276: putstatic lunarians/LunariansSettings.drillStartY : I
    //   279: aload_2
    //   280: iconst_0
    //   281: iaload
    //   282: istore #22
    //   284: aload #23
    //   286: iconst_0
    //   287: iaload
    //   288: istore #24
    //   290: iload #22
    //   292: iload #24
    //   294: isub
    //   295: istore #22
    //   297: iload #22
    //   299: i2f
    //   300: fstore #25
    //   302: aload_2
    //   303: iload #6
    //   305: iaload
    //   306: istore #24
    //   308: aload #23
    //   310: iload #6
    //   312: iaload
    //   313: istore #20
    //   315: iload #24
    //   317: iload #20
    //   319: isub
    //   320: istore #24
    //   322: iload #24
    //   324: i2f
    //   325: fstore #21
    //   327: fload #25
    //   329: invokestatic abs : (F)F
    //   332: fstore #26
    //   334: fload #21
    //   336: invokestatic abs : (F)F
    //   339: fstore #27
    //   341: iconst_3
    //   342: istore #28
    //   344: aconst_null
    //   345: astore #29
    //   347: iconst_2
    //   348: istore #30
    //   350: fload #26
    //   352: fload #27
    //   354: fcmpl
    //   355: istore #24
    //   357: iload #24
    //   359: iflt -> 396
    //   362: fload #25
    //   364: fconst_0
    //   365: fcmpl
    //   366: istore #24
    //   368: iload #24
    //   370: iflt -> 385
    //   373: iconst_0
    //   374: istore #24
    //   376: fconst_0
    //   377: fstore #26
    //   379: aconst_null
    //   380: astore #31
    //   382: goto -> 426
    //   385: iconst_2
    //   386: istore #24
    //   388: ldc_w 2.8E-45
    //   391: fstore #26
    //   393: goto -> 426
    //   396: fload #21
    //   398: fconst_0
    //   399: fcmpl
    //   400: istore #24
    //   402: iload #24
    //   404: iflt -> 418
    //   407: iconst_1
    //   408: istore #24
    //   410: ldc_w 1.4E-45
    //   413: fstore #26
    //   415: goto -> 426
    //   418: iconst_3
    //   419: istore #24
    //   421: ldc_w 4.2E-45
    //   424: fstore #26
    //   426: fload #25
    //   428: invokestatic abs : (F)F
    //   431: fstore #27
    //   433: fload #21
    //   435: invokestatic abs : (F)F
    //   438: fstore #32
    //   440: fload #27
    //   442: fload #32
    //   444: fcmpl
    //   445: istore #33
    //   447: iload #33
    //   449: iflt -> 469
    //   452: fload #21
    //   454: fconst_0
    //   455: fcmpl
    //   456: istore #20
    //   458: iload #20
    //   460: iflt -> 492
    //   463: iconst_1
    //   464: istore #28
    //   466: goto -> 492
    //   469: fload #25
    //   471: fconst_0
    //   472: fcmpl
    //   473: istore #20
    //   475: iload #20
    //   477: iflt -> 489
    //   480: iconst_0
    //   481: istore #28
    //   483: aconst_null
    //   484: astore #34
    //   486: goto -> 492
    //   489: iconst_2
    //   490: istore #28
    //   492: iload #28
    //   494: iconst_2
    //   495: ixor
    //   496: istore #20
    //   498: iload #24
    //   500: iconst_2
    //   501: ixor
    //   502: istore #22
    //   504: iconst_4
    //   505: newarray int
    //   507: dup
    //   508: dup2
    //   509: iconst_0
    //   510: iload #24
    //   512: iastore
    //   513: iconst_1
    //   514: iload #28
    //   516: iastore
    //   517: iconst_2
    //   518: iload #20
    //   520: iastore
    //   521: dup
    //   522: iconst_3
    //   523: iload #22
    //   525: iastore
    //   526: astore #29
    //   528: aload_1
    //   529: invokestatic reservedForFeed : (Lmindustry/gen/Unit;)[Z
    //   532: astore #35
    //   534: iload #16
    //   536: istore #36
    //   538: fload #17
    //   540: fstore #37
    //   542: iconst_0
    //   543: istore #16
    //   545: fconst_0
    //   546: fstore #17
    //   548: aconst_null
    //   549: astore #18
    //   551: iconst_4
    //   552: istore #20
    //   554: ldc_w 5.6E-45
    //   557: fstore #21
    //   559: iload #16
    //   561: iload #20
    //   563: if_icmpge -> 1622
    //   566: iload #36
    //   568: ifne -> 1622
    //   571: aload #29
    //   573: iload #16
    //   575: iaload
    //   576: istore #20
    //   578: iload #20
    //   580: putstatic lunarians/LunariansSettings.drillExitDir : I
    //   583: iload #6
    //   585: putstatic lunarians/AutoDrill.dryRun : Z
    //   588: invokestatic genInner : ()I
    //   591: istore #22
    //   593: iconst_0
    //   594: putstatic lunarians/AutoDrill.dryRun : Z
    //   597: iload #22
    //   599: ifle -> 1622
    //   602: getstatic lunarians/AutoDrill.lastHub : I
    //   605: istore #24
    //   607: iload #24
    //   609: ifge -> 619
    //   612: iload #16
    //   614: istore #15
    //   616: goto -> 953
    //   619: new arc/struct/Seq
    //   622: astore #31
    //   624: aload #31
    //   626: invokespecial <init> : ()V
    //   629: iconst_0
    //   630: istore #33
    //   632: fconst_0
    //   633: fstore #27
    //   635: aconst_null
    //   636: astore #38
    //   638: getstatic lunarians/AutoDrill.lastPlans : Larc/struct/Seq;
    //   641: astore #34
    //   643: aload #34
    //   645: getfield size : I
    //   648: istore #28
    //   650: iload #33
    //   652: iload #28
    //   654: if_icmpge -> 710
    //   657: getstatic lunarians/AutoDrill.lastPlans : Larc/struct/Seq;
    //   660: astore #34
    //   662: aload #34
    //   664: iload #33
    //   666: invokevirtual get : (I)Ljava/lang/Object;
    //   669: astore #34
    //   671: aload #34
    //   673: checkcast mindustry/entities/units/BuildPlan
    //   676: astore #34
    //   678: aload #31
    //   680: aload #34
    //   682: invokevirtual add : (Ljava/lang/Object;)Larc/struct/Seq;
    //   685: pop
    //   686: iload #33
    //   688: iconst_1
    //   689: iadd
    //   690: istore #33
    //   692: goto -> 638
    //   695: astore_1
    //   696: iload #36
    //   698: istore #6
    //   700: fload #37
    //   702: fstore #7
    //   704: iconst_m1
    //   705: istore #39
    //   707: goto -> 1689
    //   710: aload #35
    //   712: invokevirtual clone : ()Ljava/lang/Object;
    //   715: astore #38
    //   717: aload #38
    //   719: astore #34
    //   721: aload #38
    //   723: checkcast [Z
    //   726: astore #34
    //   728: iconst_0
    //   729: istore #33
    //   731: fconst_0
    //   732: fstore #27
    //   734: aconst_null
    //   735: astore #38
    //   737: aload #31
    //   739: getfield size : I
    //   742: istore #15
    //   744: iload #33
    //   746: iload #15
    //   748: if_icmpge -> 795
    //   751: aload #31
    //   753: iload #33
    //   755: invokevirtual get : (I)Ljava/lang/Object;
    //   758: astore #13
    //   760: aload #13
    //   762: checkcast mindustry/entities/units/BuildPlan
    //   765: astore #13
    //   767: aload #13
    //   769: aload #34
    //   771: invokestatic markPlan : (Lmindustry/entities/units/BuildPlan;[Z)V
    //   774: iload #33
    //   776: iconst_1
    //   777: iadd
    //   778: istore #33
    //   780: goto -> 737
    //   783: astore_1
    //   784: iload #36
    //   786: istore #6
    //   788: fload #37
    //   790: fstore #7
    //   792: goto -> 704
    //   795: getstatic lunarians/AutoDrill.lastHub : I
    //   798: istore #15
    //   800: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   803: astore #38
    //   805: aload #38
    //   807: invokevirtual width : ()I
    //   810: istore #33
    //   812: iload #15
    //   814: iload #33
    //   816: irem
    //   817: istore #15
    //   819: getstatic lunarians/AutoSet.DX : [I
    //   822: astore #38
    //   824: aload #38
    //   826: iload #20
    //   828: iaload
    //   829: istore #33
    //   831: iload #15
    //   833: iload #33
    //   835: iadd
    //   836: istore #15
    //   838: getstatic lunarians/AutoDrill.lastHub : I
    //   841: istore #33
    //   843: getstatic mindustry/Vars.world : Lmindustry/core/World;
    //   846: astore #40
    //   848: aload #40
    //   850: invokevirtual width : ()I
    //   853: istore #39
    //   855: iload #33
    //   857: iload #39
    //   859: idiv
    //   860: istore #33
    //   862: getstatic lunarians/AutoSet.DY : [I
    //   865: astore #40
    //   867: aload #40
    //   869: iload #20
    //   871: iaload
    //   872: iload #33
    //   874: iadd
    //   875: istore #20
    //   877: iload #22
    //   879: istore #33
    //   881: aload_2
    //   882: iconst_0
    //   883: iaload
    //   884: istore #22
    //   886: aload #31
    //   888: astore #40
    //   890: aload_2
    //   891: iload #6
    //   893: iaload
    //   894: istore #24
    //   896: iload #33
    //   898: istore #41
    //   900: aload_2
    //   901: iload #30
    //   903: iaload
    //   904: istore #33
    //   906: iload #16
    //   908: istore #6
    //   910: iload #15
    //   912: istore #16
    //   914: iload #6
    //   916: istore #15
    //   918: aload #31
    //   920: astore #42
    //   922: iload #41
    //   924: istore #43
    //   926: iconst_m1
    //   927: istore #39
    //   929: iload #16
    //   931: iload #20
    //   933: iload #22
    //   935: iload #24
    //   937: iload #33
    //   939: aload #34
    //   941: aload #14
    //   943: invokestatic routeTo : (IIIII[ZLmindustry/world/Block;)Larc/struct/Seq;
    //   946: astore #18
    //   948: aload #18
    //   950: ifnonnull -> 956
    //   953: goto -> 1554
    //   956: new arc/struct/Seq
    //   959: astore #23
    //   961: aload #23
    //   963: invokespecial <init> : ()V
    //   966: aload_1
    //   967: invokevirtual plans : ()Larc/struct/Queue;
    //   970: astore #44
    //   972: aload #44
    //   974: invokevirtual iterator : ()Ljava/util/Iterator;
    //   977: astore #44
    //   979: aload #44
    //   981: invokeinterface hasNext : ()Z
    //   986: istore #24
    //   988: iload #24
    //   990: ifeq -> 1020
    //   993: aload #44
    //   995: invokeinterface next : ()Ljava/lang/Object;
    //   1000: astore #31
    //   1002: aload #31
    //   1004: checkcast mindustry/entities/units/BuildPlan
    //   1007: astore #31
    //   1009: aload #23
    //   1011: aload #31
    //   1013: invokevirtual add : (Ljava/lang/Object;)Larc/struct/Seq;
    //   1016: pop
    //   1017: goto -> 979
    //   1020: iconst_0
    //   1021: istore #22
    //   1023: fconst_0
    //   1024: fstore #25
    //   1026: aconst_null
    //   1027: astore #44
    //   1029: aload #42
    //   1031: getfield size : I
    //   1034: istore #24
    //   1036: iload #22
    //   1038: iload #24
    //   1040: if_icmpge -> 1084
    //   1043: aload #42
    //   1045: iload #22
    //   1047: invokevirtual get : (I)Ljava/lang/Object;
    //   1050: astore #31
    //   1052: aload #31
    //   1054: checkcast mindustry/entities/units/BuildPlan
    //   1057: astore #31
    //   1059: iconst_1
    //   1060: istore #33
    //   1062: ldc_w 1.4E-45
    //   1065: fstore #27
    //   1067: aload_1
    //   1068: aload #31
    //   1070: iload #33
    //   1072: invokevirtual addBuild : (Lmindustry/entities/units/BuildPlan;Z)V
    //   1075: iload #22
    //   1077: iconst_1
    //   1078: iadd
    //   1079: istore #22
    //   1081: goto -> 1029
    //   1084: iconst_0
    //   1085: istore #6
    //   1087: fconst_0
    //   1088: fstore #7
    //   1090: aconst_null
    //   1091: astore #42
    //   1093: aload #18
    //   1095: getfield size : I
    //   1098: istore #22
    //   1100: iload #6
    //   1102: iload #22
    //   1104: if_icmpge -> 1148
    //   1107: aload #18
    //   1109: iload #6
    //   1111: invokevirtual get : (I)Ljava/lang/Object;
    //   1114: astore #44
    //   1116: aload #44
    //   1118: checkcast mindustry/entities/units/BuildPlan
    //   1121: astore #44
    //   1123: iconst_1
    //   1124: istore #33
    //   1126: ldc_w 1.4E-45
    //   1129: fstore #27
    //   1131: aload_1
    //   1132: aload #44
    //   1134: iload #33
    //   1136: invokevirtual addBuild : (Lmindustry/entities/units/BuildPlan;Z)V
    //   1139: iload #6
    //   1141: iconst_1
    //   1142: iadd
    //   1143: istore #6
    //   1145: goto -> 1093
    //   1148: getstatic lunarians/AutoSet.pending : Larc/struct/Seq;
    //   1151: astore #42
    //   1153: aload #42
    //   1155: invokevirtual clear : ()Larc/struct/Seq;
    //   1158: pop
    //   1159: aload_1
    //   1160: invokevirtual plans : ()Larc/struct/Queue;
    //   1163: astore #42
    //   1165: aload #42
    //   1167: invokevirtual iterator : ()Ljava/util/Iterator;
    //   1170: astore #42
    //   1172: aload #42
    //   1174: invokeinterface hasNext : ()Z
    //   1179: istore #22
    //   1181: iload #22
    //   1183: ifeq -> 1242
    //   1186: aload #42
    //   1188: invokeinterface next : ()Ljava/lang/Object;
    //   1193: astore #44
    //   1195: aload #44
    //   1197: checkcast mindustry/entities/units/BuildPlan
    //   1200: astore #44
    //   1202: iconst_1
    //   1203: istore #33
    //   1205: ldc_w 1.4E-45
    //   1208: fstore #27
    //   1210: aload #23
    //   1212: aload #44
    //   1214: iload #33
    //   1216: invokevirtual contains : (Ljava/lang/Object;Z)Z
    //   1219: istore #24
    //   1221: iload #24
    //   1223: ifne -> 1172
    //   1226: getstatic lunarians/AutoSet.pending : Larc/struct/Seq;
    //   1229: astore #31
    //   1231: aload #31
    //   1233: aload #44
    //   1235: invokevirtual add : (Ljava/lang/Object;)Larc/struct/Seq;
    //   1238: pop
    //   1239: goto -> 1172
    //   1242: getstatic lunarians/AutoSet.pending : Larc/struct/Seq;
    //   1245: astore #42
    //   1247: aload #42
    //   1249: getfield size : I
    //   1252: istore #6
    //   1254: iload #6
    //   1256: putstatic lunarians/AutoSet.lastPending : I
    //   1259: getstatic arc/util/Time.time : F
    //   1262: fstore #7
    //   1264: fload #7
    //   1266: putstatic lunarians/AutoSet.lastProgress : F
    //   1269: getstatic lunarians/AutoSet.feedLines : Ljava/util/HashMap;
    //   1272: astore #42
    //   1274: aload #42
    //   1276: aload #4
    //   1278: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   1281: astore #23
    //   1283: aload #23
    //   1285: checkcast java/util/ArrayList
    //   1288: astore #23
    //   1290: aload #23
    //   1292: ifnonnull -> 1315
    //   1295: new java/util/ArrayList
    //   1298: astore #23
    //   1300: aload #23
    //   1302: invokespecial <init> : ()V
    //   1305: aload #42
    //   1307: aload #4
    //   1309: aload #23
    //   1311: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1314: pop
    //   1315: iconst_0
    //   1316: istore #6
    //   1318: fconst_0
    //   1319: fstore #7
    //   1321: aconst_null
    //   1322: astore #42
    //   1324: aload #18
    //   1326: getfield size : I
    //   1329: istore #22
    //   1331: iload #6
    //   1333: iload #22
    //   1335: if_icmpge -> 1419
    //   1338: aload #18
    //   1340: iload #6
    //   1342: invokevirtual get : (I)Ljava/lang/Object;
    //   1345: astore #44
    //   1347: aload #44
    //   1349: checkcast mindustry/entities/units/BuildPlan
    //   1352: astore #44
    //   1354: aload #44
    //   1356: getfield block : Lmindustry/world/Block;
    //   1359: astore #31
    //   1361: aload #31
    //   1363: instanceof mindustry/world/blocks/distribution/Conveyor
    //   1366: istore #24
    //   1368: iload #24
    //   1370: ifeq -> 1410
    //   1373: aload #44
    //   1375: getfield x : I
    //   1378: istore #24
    //   1380: aload #44
    //   1382: getfield y : I
    //   1385: istore #22
    //   1387: iconst_2
    //   1388: newarray int
    //   1390: dup
    //   1391: iconst_0
    //   1392: iload #24
    //   1394: iastore
    //   1395: dup
    //   1396: iconst_1
    //   1397: iload #22
    //   1399: iastore
    //   1400: astore #44
    //   1402: aload #23
    //   1404: aload #44
    //   1406: invokevirtual add : (Ljava/lang/Object;)Z
    //   1409: pop
    //   1410: iload #6
    //   1412: iconst_1
    //   1413: iadd
    //   1414: istore #6
    //   1416: goto -> 1324
    //   1419: new java/lang/StringBuilder
    //   1422: astore #42
    //   1424: aload #42
    //   1426: invokespecial <init> : ()V
    //   1429: aload #11
    //   1431: getfield localizedName : Ljava/lang/String;
    //   1434: astore #23
    //   1436: aload #42
    //   1438: aload #23
    //   1440: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1443: pop
    //   1444: ldc_w ' → entrada da fábrica: '
    //   1447: astore #23
    //   1449: ldc_w ' → factory input: '
    //   1452: astore #44
    //   1454: aload #23
    //   1456: aload #44
    //   1458: invokestatic tr : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1461: astore #23
    //   1463: aload #42
    //   1465: aload #23
    //   1467: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1470: pop
    //   1471: aload #42
    //   1473: iload #43
    //   1475: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1478: pop
    //   1479: ldc_w ' drills + '
    //   1482: astore #23
    //   1484: aload #42
    //   1486: aload #23
    //   1488: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1491: pop
    //   1492: aload #18
    //   1494: getfield size : I
    //   1497: istore #16
    //   1499: aload #42
    //   1501: iload #16
    //   1503: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1506: pop
    //   1507: ldc_w ' esteiras'
    //   1510: astore #18
    //   1512: ldc_w ' conveyors'
    //   1515: astore #23
    //   1517: aload #18
    //   1519: aload #23
    //   1521: invokestatic tr : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1524: astore #18
    //   1526: aload #42
    //   1528: aload #18
    //   1530: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1533: pop
    //   1534: aload #42
    //   1536: invokevirtual toString : ()Ljava/lang/String;
    //   1539: astore #42
    //   1541: aload #42
    //   1543: invokestatic toast : (Ljava/lang/String;)V
    //   1546: iconst_1
    //   1547: istore #36
    //   1549: ldc_w 1.4E-45
    //   1552: fstore #37
    //   1554: iload #15
    //   1556: iconst_1
    //   1557: iadd
    //   1558: istore #16
    //   1560: iconst_1
    //   1561: istore #6
    //   1563: ldc_w 1.4E-45
    //   1566: fstore #7
    //   1568: iconst_0
    //   1569: istore #15
    //   1571: aconst_null
    //   1572: astore #13
    //   1574: goto -> 551
    //   1577: astore_1
    //   1578: iconst_1
    //   1579: istore #6
    //   1581: ldc_w 1.4E-45
    //   1584: fstore #7
    //   1586: goto -> 1689
    //   1589: astore_1
    //   1590: goto -> 1608
    //   1593: astore_1
    //   1594: iconst_m1
    //   1595: istore #39
    //   1597: iconst_0
    //   1598: putstatic lunarians/AutoDrill.dryRun : Z
    //   1601: aload_1
    //   1602: athrow
    //   1603: astore_1
    //   1604: goto -> 1611
    //   1607: astore_1
    //   1608: iconst_m1
    //   1609: istore #39
    //   1611: iload #36
    //   1613: istore #6
    //   1615: fload #37
    //   1617: fstore #7
    //   1619: goto -> 1689
    //   1622: iload #19
    //   1624: iconst_1
    //   1625: iadd
    //   1626: istore #19
    //   1628: iload #36
    //   1630: istore #16
    //   1632: fload #37
    //   1634: fstore #17
    //   1636: iconst_1
    //   1637: istore #6
    //   1639: ldc_w 1.4E-45
    //   1642: fstore #7
    //   1644: iconst_0
    //   1645: istore #15
    //   1647: aconst_null
    //   1648: astore #13
    //   1650: goto -> 186
    //   1653: astore_1
    //   1654: iconst_m1
    //   1655: istore #39
    //   1657: goto -> 1681
    //   1660: iconst_m1
    //   1661: istore #39
    //   1663: iload #16
    //   1665: ifne -> 1754
    //   1668: aload_1
    //   1669: aload #4
    //   1671: aload_2
    //   1672: invokestatic tapLine : (Lmindustry/gen/Unit;Ljava/lang/String;[I)Z
    //   1675: istore #16
    //   1677: goto -> 1754
    //   1680: astore_1
    //   1681: iload #16
    //   1683: istore #6
    //   1685: fload #17
    //   1687: fstore #7
    //   1689: ldc_w '[Lunarians] AutoSet feed step'
    //   1692: astore_2
    //   1693: aload_2
    //   1694: aload_1
    //   1695: invokestatic err : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   1698: iload #39
    //   1700: putstatic lunarians/AutoDrill.isoOverride : I
    //   1703: iconst_0
    //   1704: putstatic lunarians/AutoDrill.quiet : Z
    //   1707: iconst_0
    //   1708: putstatic lunarians/AutoDrill.limitOn : Z
    //   1711: iconst_0
    //   1712: putstatic lunarians/AutoDrill.dryRun : Z
    //   1715: iload #9
    //   1717: putstatic lunarians/LunariansSettings.drillExitDir : I
    //   1720: invokestatic save : ()V
    //   1723: goto -> 1783
    //   1726: astore_1
    //   1727: iload #39
    //   1729: putstatic lunarians/AutoDrill.isoOverride : I
    //   1732: iconst_0
    //   1733: putstatic lunarians/AutoDrill.quiet : Z
    //   1736: iconst_0
    //   1737: putstatic lunarians/AutoDrill.limitOn : Z
    //   1740: iconst_0
    //   1741: putstatic lunarians/AutoDrill.dryRun : Z
    //   1744: iload #9
    //   1746: putstatic lunarians/LunariansSettings.drillExitDir : I
    //   1749: invokestatic save : ()V
    //   1752: aload_1
    //   1753: athrow
    //   1754: iload #39
    //   1756: putstatic lunarians/AutoDrill.isoOverride : I
    //   1759: iconst_0
    //   1760: putstatic lunarians/AutoDrill.quiet : Z
    //   1763: iconst_0
    //   1764: putstatic lunarians/AutoDrill.limitOn : Z
    //   1767: iconst_0
    //   1768: putstatic lunarians/AutoDrill.dryRun : Z
    //   1771: iload #9
    //   1773: putstatic lunarians/LunariansSettings.drillExitDir : I
    //   1776: invokestatic save : ()V
    //   1779: iload #16
    //   1781: istore #6
    //   1783: iload #6
    //   1785: ifne -> 1837
    //   1788: new java/lang/StringBuilder
    //   1791: astore_1
    //   1792: aload_1
    //   1793: invokespecial <init> : ()V
    //   1796: ldc_w 'No ore/route for '
    //   1799: astore #4
    //   1801: ldc_w 'Sem minério/rota para '
    //   1804: aload #4
    //   1806: invokestatic tr : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1809: astore_2
    //   1810: aload_1
    //   1811: aload_2
    //   1812: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1815: pop
    //   1816: aload #11
    //   1818: getfield localizedName : Ljava/lang/String;
    //   1821: astore_2
    //   1822: aload_1
    //   1823: aload_2
    //   1824: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1827: pop
    //   1828: aload_1
    //   1829: invokevirtual toString : ()Ljava/lang/String;
    //   1832: astore_1
    //   1833: aload_1
    //   1834: invokestatic toast : (Ljava/lang/String;)V
    //   1837: return
    // Exception table:
    //   from	to	target	type
    //   208	210	1653	finally
    //   213	216	1653	finally
    //   222	227	1653	finally
    //   236	238	1653	finally
    //   242	245	1653	finally
    //   251	256	1653	finally
    //   258	260	1653	finally
    //   262	267	1653	finally
    //   269	272	1653	finally
    //   274	279	1653	finally
    //   280	282	1653	finally
    //   286	288	1653	finally
    //   303	306	1653	finally
    //   310	313	1653	finally
    //   327	332	1653	finally
    //   334	339	1653	finally
    //   426	431	1653	finally
    //   433	438	1653	finally
    //   504	526	1653	finally
    //   528	532	1653	finally
    //   573	576	1607	finally
    //   578	583	1607	finally
    //   583	588	1607	finally
    //   588	591	1593	finally
    //   593	597	1607	finally
    //   602	605	1607	finally
    //   619	622	1607	finally
    //   624	629	1607	finally
    //   638	641	1607	finally
    //   643	648	1607	finally
    //   657	660	695	finally
    //   664	669	695	finally
    //   671	676	695	finally
    //   680	686	695	finally
    //   710	715	1607	finally
    //   721	726	1607	finally
    //   737	742	1589	finally
    //   753	758	783	finally
    //   760	765	783	finally
    //   769	774	783	finally
    //   795	798	1589	finally
    //   800	803	1589	finally
    //   805	810	1589	finally
    //   814	817	1589	finally
    //   819	822	1589	finally
    //   826	829	1589	finally
    //   838	841	1589	finally
    //   843	846	1589	finally
    //   848	853	1589	finally
    //   857	860	1589	finally
    //   862	865	1589	finally
    //   869	872	1589	finally
    //   882	884	1589	finally
    //   891	894	1589	finally
    //   901	904	1589	finally
    //   941	946	1603	finally
    //   956	959	1603	finally
    //   961	966	1603	finally
    //   966	970	1603	finally
    //   972	977	1603	finally
    //   979	986	1603	finally
    //   993	1000	1603	finally
    //   1002	1007	1603	finally
    //   1011	1017	1603	finally
    //   1029	1034	1603	finally
    //   1045	1050	1603	finally
    //   1052	1057	1603	finally
    //   1070	1075	1603	finally
    //   1093	1098	1603	finally
    //   1109	1114	1603	finally
    //   1116	1121	1603	finally
    //   1134	1139	1603	finally
    //   1148	1151	1603	finally
    //   1153	1159	1603	finally
    //   1159	1163	1603	finally
    //   1165	1170	1603	finally
    //   1172	1179	1603	finally
    //   1186	1193	1603	finally
    //   1195	1200	1603	finally
    //   1214	1219	1603	finally
    //   1226	1229	1603	finally
    //   1233	1239	1603	finally
    //   1242	1245	1603	finally
    //   1247	1252	1603	finally
    //   1254	1259	1603	finally
    //   1259	1262	1603	finally
    //   1264	1269	1603	finally
    //   1269	1272	1577	finally
    //   1276	1281	1577	finally
    //   1283	1288	1577	finally
    //   1295	1298	1577	finally
    //   1300	1305	1577	finally
    //   1309	1315	1577	finally
    //   1324	1329	1577	finally
    //   1340	1345	1577	finally
    //   1347	1352	1577	finally
    //   1354	1359	1577	finally
    //   1373	1378	1577	finally
    //   1380	1385	1577	finally
    //   1387	1400	1577	finally
    //   1404	1410	1577	finally
    //   1419	1422	1577	finally
    //   1424	1429	1577	finally
    //   1429	1434	1577	finally
    //   1438	1444	1577	finally
    //   1456	1461	1577	finally
    //   1465	1471	1577	finally
    //   1473	1479	1577	finally
    //   1486	1492	1577	finally
    //   1492	1497	1577	finally
    //   1501	1507	1577	finally
    //   1519	1524	1577	finally
    //   1528	1534	1577	finally
    //   1534	1539	1577	finally
    //   1541	1546	1577	finally
    //   1597	1601	1603	finally
    //   1601	1603	1603	finally
    //   1671	1675	1680	finally
    //   1694	1698	1726	finally
  }
  
  private static void startSteamStep(Unit paramUnit) {
    try {
      int i = cx;
      int j = cy;
      float f = radius;
      boolean[] arrayOfBoolean = used;
      Seq seq = SteamDrill.plan(paramUnit, i, j, f, arrayOfBoolean);
      String str3 = "Steam Drill: ";
      if (seq != null) {
        boolean bool = seq.isEmpty();
        if (!bool) {
          Seq seq1 = new Seq();
          this();
          Queue queue = paramUnit.plans();
          Iterator<Object> iterator = queue.iterator();
          while (true) {
            boolean bool1 = iterator.hasNext();
            if (bool1) {
              BuildPlan buildPlan = (BuildPlan)iterator.next();
              buildPlan = buildPlan;
              seq1.add(buildPlan);
              continue;
            } 
            boolean[] arrayOfBoolean1 = used;
            bool1 = true;
            if (arrayOfBoolean1 != null) {
              ArrayList arrayList = SteamDrill.lastTiles;
              Iterator<Object> iterator1 = arrayList.iterator();
              while (true) {
                boolean bool2 = iterator1.hasNext();
                if (bool2) {
                  Integer integer = (Integer)iterator1.next();
                  integer = integer;
                  int n = integer.intValue();
                  if (n >= 0) {
                    n = integer.intValue();
                    boolean[] arrayOfBoolean2 = used;
                    int i1 = arrayOfBoolean2.length;
                    if (n < i1) {
                      int i2 = integer.intValue();
                      arrayOfBoolean2[i2] = bool1;
                    } 
                  } 
                  continue;
                } 
                break;
              } 
            } 
            int m = 0;
            arrayOfBoolean1 = null;
            while (true) {
              int n = seq.size;
              if (m < n) {
                Object object = seq.get(m);
                object = object;
                paramUnit.addBuild((BuildPlan)object, bool1);
                m++;
                continue;
              } 
              Seq seq2 = pending;
              seq2.clear();
              Queue queue1 = paramUnit.plans();
              Iterator<Seq> iterator1 = queue1.iterator();
              while (true) {
                boolean bool2 = iterator1.hasNext();
                if (bool2) {
                  seq2 = iterator1.next();
                  BuildPlan buildPlan = (BuildPlan)seq2;
                  boolean bool3 = seq1.contains(buildPlan, bool1);
                  if (!bool3) {
                    Seq seq4 = pending;
                    seq4.add(buildPlan);
                  } 
                  continue;
                } 
                Seq seq3 = pending;
                int i1 = seq3.size;
                lastPending = i1;
                float f1 = Time.time;
                lastProgress = f1;
                StringBuilder stringBuilder1 = new StringBuilder();
                this();
                stringBuilder1.append(str3);
                i = seq.size;
                stringBuilder1.append(i);
                String str5 = " blocos planejados";
                str3 = " blocks planned";
                str5 = tr(str5, str3);
                stringBuilder1.append(str5);
                return;
              } 
              break;
            } 
            break;
          } 
        } 
      } 
      int k = AutoSetConfig.steamCount();
      steamDone = k;
      StringBuilder stringBuilder = new StringBuilder();
      this(str3);
      String str2 = SteamDrill.lastError;
      stringBuilder.append(str2);
      return;
    } finally {
      paramUnit = null;
      Log.err("[Lunarians] steam step", (Throwable)paramUnit);
    } 
  }
  
  private static void startStep(Unit paramUnit) {
    // Byte code:
    //   0: getstatic lunarians/AutoSet.oreOrder : [Ljava/lang/String;
    //   3: astore_1
    //   4: getstatic lunarians/AutoSet.stepIndex : I
    //   7: istore_2
    //   8: aload_1
    //   9: iload_2
    //   10: aaload
    //   11: invokestatic itemFor : (Ljava/lang/String;)Lmindustry/type/Item;
    //   14: astore_1
    //   15: getstatic lunarians/AutoSet.stepIndex : I
    //   18: istore_2
    //   19: iconst_1
    //   20: istore_3
    //   21: ldc_w 1.4E-45
    //   24: fstore #4
    //   26: iload_2
    //   27: iload_3
    //   28: iadd
    //   29: istore_2
    //   30: iload_2
    //   31: putstatic lunarians/AutoSet.stepIndex : I
    //   34: getstatic arc/util/Time.time : F
    //   37: fstore #5
    //   39: ldc_w 1106247680
    //   42: istore #6
    //   44: ldc_w 30.0
    //   47: fstore #7
    //   49: fload #5
    //   51: fload #7
    //   53: fadd
    //   54: fstore #5
    //   56: fload #5
    //   58: putstatic lunarians/AutoSet.nextStepAt : F
    //   61: aload_1
    //   62: ifnull -> 592
    //   65: iload_3
    //   66: putstatic lunarians/AutoDrill.limitOn : Z
    //   69: getstatic lunarians/AutoSet.cx : I
    //   72: i2f
    //   73: putstatic lunarians/AutoDrill.limitX : F
    //   76: getstatic lunarians/AutoSet.cy : I
    //   79: i2f
    //   80: putstatic lunarians/AutoDrill.limitY : F
    //   83: getstatic lunarians/AutoSet.radius : F
    //   86: putstatic lunarians/AutoDrill.limitR : F
    //   89: iload_3
    //   90: putstatic lunarians/AutoDrill.quiet : Z
    //   93: iconst_0
    //   94: istore_2
    //   95: fconst_0
    //   96: fstore #5
    //   98: iconst_0
    //   99: istore #6
    //   101: fconst_0
    //   102: fstore #7
    //   104: aconst_null
    //   105: astore #8
    //   107: iconst_0
    //   108: istore #9
    //   110: iconst_0
    //   111: istore #10
    //   113: fconst_0
    //   114: fstore #11
    //   116: aconst_null
    //   117: astore #12
    //   119: iconst_4
    //   120: istore #13
    //   122: iconst_m1
    //   123: istore #14
    //   125: iload #6
    //   127: iload #13
    //   129: if_icmpge -> 540
    //   132: iload #9
    //   134: ifne -> 540
    //   137: aload_1
    //   138: invokestatic findOre : (Lmindustry/type/Item;)[I
    //   141: astore #15
    //   143: aload #15
    //   145: ifnull -> 540
    //   148: aload #15
    //   150: iconst_0
    //   151: iaload
    //   152: istore #10
    //   154: aload #15
    //   156: iload_3
    //   157: iaload
    //   158: istore #16
    //   160: iload #10
    //   162: iload #16
    //   164: aload_1
    //   165: invokestatic markUsed : (IILmindustry/type/Item;)V
    //   168: aload #15
    //   170: iconst_0
    //   171: iaload
    //   172: istore #10
    //   174: iload #10
    //   176: putstatic lunarians/LunariansSettings.drillStartX : I
    //   179: aload #15
    //   181: iload_3
    //   182: iaload
    //   183: istore #10
    //   185: iload #10
    //   187: putstatic lunarians/LunariansSettings.drillStartY : I
    //   190: aload #15
    //   192: iconst_0
    //   193: iaload
    //   194: istore #10
    //   196: aload #15
    //   198: iload_3
    //   199: iaload
    //   200: istore #13
    //   202: iload #10
    //   204: iload #13
    //   206: invokestatic setExitTowardCore : (II)V
    //   209: new arc/struct/Seq
    //   212: astore #12
    //   214: aload #12
    //   216: invokespecial <init> : ()V
    //   219: aload_0
    //   220: invokevirtual plans : ()Larc/struct/Queue;
    //   223: astore #15
    //   225: aload #15
    //   227: invokevirtual iterator : ()Ljava/util/Iterator;
    //   230: astore #15
    //   232: aload #15
    //   234: invokeinterface hasNext : ()Z
    //   239: istore #16
    //   241: iload #16
    //   243: ifeq -> 273
    //   246: aload #15
    //   248: invokeinterface next : ()Ljava/lang/Object;
    //   253: astore #17
    //   255: aload #17
    //   257: checkcast mindustry/entities/units/BuildPlan
    //   260: astore #17
    //   262: aload #12
    //   264: aload #17
    //   266: invokevirtual add : (Ljava/lang/Object;)Larc/struct/Seq;
    //   269: pop
    //   270: goto -> 232
    //   273: invokestatic generateWithConveyor : ()I
    //   276: istore #13
    //   278: iload #13
    //   280: ifle -> 455
    //   283: getstatic lunarians/AutoSet.pending : Larc/struct/Seq;
    //   286: astore #17
    //   288: aload #17
    //   290: invokevirtual clear : ()Larc/struct/Seq;
    //   293: pop
    //   294: aload_0
    //   295: invokevirtual plans : ()Larc/struct/Queue;
    //   298: astore #17
    //   300: aload #17
    //   302: invokevirtual iterator : ()Ljava/util/Iterator;
    //   305: astore #17
    //   307: aload #17
    //   309: invokeinterface hasNext : ()Z
    //   314: istore #18
    //   316: iload #18
    //   318: ifeq -> 368
    //   321: aload #17
    //   323: invokeinterface next : ()Ljava/lang/Object;
    //   328: astore #19
    //   330: aload #19
    //   332: checkcast mindustry/entities/units/BuildPlan
    //   335: astore #19
    //   337: aload #12
    //   339: aload #19
    //   341: iload_3
    //   342: invokevirtual contains : (Ljava/lang/Object;Z)Z
    //   345: istore #20
    //   347: iload #20
    //   349: ifne -> 307
    //   352: getstatic lunarians/AutoSet.pending : Larc/struct/Seq;
    //   355: astore #21
    //   357: aload #21
    //   359: aload #19
    //   361: invokevirtual add : (Ljava/lang/Object;)Larc/struct/Seq;
    //   364: pop
    //   365: goto -> 307
    //   368: getstatic lunarians/AutoSet.pending : Larc/struct/Seq;
    //   371: astore #12
    //   373: aload #12
    //   375: getfield size : I
    //   378: istore #9
    //   380: iload #9
    //   382: ifle -> 391
    //   385: iconst_1
    //   386: istore #9
    //   388: goto -> 394
    //   391: iconst_0
    //   392: istore #9
    //   394: iload #9
    //   396: ifeq -> 455
    //   399: aload #12
    //   401: getfield size : I
    //   404: istore #10
    //   406: iload #10
    //   408: putstatic lunarians/AutoSet.lastPending : I
    //   411: getstatic arc/util/Time.time : F
    //   414: fstore #11
    //   416: fload #11
    //   418: putstatic lunarians/AutoSet.lastProgress : F
    //   421: ldc_w 'as_step'
    //   424: astore #12
    //   426: aload_1
    //   427: getfield localizedName : Ljava/lang/String;
    //   430: astore #17
    //   432: iload #13
    //   434: invokestatic valueOf : (I)Ljava/lang/String;
    //   437: astore #15
    //   439: aload #12
    //   441: aload #17
    //   443: aload #15
    //   445: invokestatic get : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   448: astore #12
    //   450: aload #12
    //   452: invokestatic toast : (Ljava/lang/String;)V
    //   455: iload #6
    //   457: iconst_1
    //   458: iadd
    //   459: istore #6
    //   461: iconst_1
    //   462: istore #10
    //   464: ldc_w 1.4E-45
    //   467: fstore #11
    //   469: goto -> 119
    //   472: astore_0
    //   473: goto -> 484
    //   476: astore_0
    //   477: iload #10
    //   479: istore_3
    //   480: fload #11
    //   482: fstore #4
    //   484: ldc_w '[Lunarians] AutoSet step'
    //   487: astore #8
    //   489: aload #8
    //   491: aload_0
    //   492: invokestatic err : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   495: iload #14
    //   497: putstatic lunarians/AutoDrill.isoOverride : I
    //   500: iconst_0
    //   501: putstatic lunarians/AutoDrill.quiet : Z
    //   504: iconst_0
    //   505: putstatic lunarians/AutoDrill.limitOn : Z
    //   508: invokestatic save : ()V
    //   511: iload_3
    //   512: istore #10
    //   514: fload #4
    //   516: fstore #11
    //   518: goto -> 556
    //   521: astore_0
    //   522: iload #14
    //   524: putstatic lunarians/AutoDrill.isoOverride : I
    //   527: iconst_0
    //   528: putstatic lunarians/AutoDrill.quiet : Z
    //   531: iconst_0
    //   532: putstatic lunarians/AutoDrill.limitOn : Z
    //   535: invokestatic save : ()V
    //   538: aload_0
    //   539: athrow
    //   540: iload #14
    //   542: putstatic lunarians/AutoDrill.isoOverride : I
    //   545: iconst_0
    //   546: putstatic lunarians/AutoDrill.quiet : Z
    //   549: iconst_0
    //   550: putstatic lunarians/AutoDrill.limitOn : Z
    //   553: invokestatic save : ()V
    //   556: iload #9
    //   558: ifne -> 592
    //   561: iload #10
    //   563: ifeq -> 573
    //   566: ldc_w 'as_no_route'
    //   569: astore_0
    //   570: goto -> 577
    //   573: ldc_w 'as_no_ore'
    //   576: astore_0
    //   577: aload_1
    //   578: getfield localizedName : Ljava/lang/String;
    //   581: astore_1
    //   582: aload_0
    //   583: aload_1
    //   584: invokestatic get : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   587: astore_0
    //   588: aload_0
    //   589: invokestatic toast : (Ljava/lang/String;)V
    //   592: return
    // Exception table:
    //   from	to	target	type
    //   137	141	476	finally
    //   150	152	472	finally
    //   156	158	472	finally
    //   164	168	472	finally
    //   170	172	472	finally
    //   174	179	472	finally
    //   181	183	472	finally
    //   185	190	472	finally
    //   192	194	472	finally
    //   198	200	472	finally
    //   204	209	472	finally
    //   209	212	472	finally
    //   214	219	472	finally
    //   219	223	472	finally
    //   225	230	472	finally
    //   232	239	472	finally
    //   246	253	472	finally
    //   255	260	472	finally
    //   264	270	472	finally
    //   273	276	472	finally
    //   283	286	472	finally
    //   288	294	472	finally
    //   294	298	472	finally
    //   300	305	472	finally
    //   307	314	472	finally
    //   321	328	472	finally
    //   330	335	472	finally
    //   341	345	472	finally
    //   352	355	472	finally
    //   359	365	472	finally
    //   368	371	472	finally
    //   373	378	472	finally
    //   399	404	472	finally
    //   406	411	472	finally
    //   411	414	472	finally
    //   416	421	472	finally
    //   426	430	472	finally
    //   432	437	472	finally
    //   443	448	472	finally
    //   450	455	472	finally
    //   491	495	521	finally
  }
  
  private static void stopMining(Unit paramUnit) {
    // Byte code:
    //   0: getstatic lunarians/AutoSet.miningActive : Z
    //   3: istore_1
    //   4: iload_1
    //   5: ifeq -> 63
    //   8: iconst_0
    //   9: putstatic lunarians/AutoSet.miningActive : Z
    //   12: iconst_1
    //   13: istore_1
    //   14: aload_0
    //   15: ifnull -> 28
    //   18: aload_0
    //   19: aconst_null
    //   20: putfield mineTile : Lmindustry/world/Tile;
    //   23: aload_0
    //   24: iload_1
    //   25: putfield updateBuilding : Z
    //   28: getstatic mindustry/Vars.control : Lmindustry/core/Control;
    //   31: astore_0
    //   32: aload_0
    //   33: ifnull -> 63
    //   36: getstatic mindustry/Vars.control : Lmindustry/core/Control;
    //   39: astore_0
    //   40: aload_0
    //   41: getfield input : Lmindustry/input/InputHandler;
    //   44: astore_0
    //   45: aload_0
    //   46: ifnull -> 63
    //   49: getstatic mindustry/Vars.control : Lmindustry/core/Control;
    //   52: astore_0
    //   53: aload_0
    //   54: getfield input : Lmindustry/input/InputHandler;
    //   57: astore_0
    //   58: aload_0
    //   59: iload_1
    //   60: putfield isBuilding : Z
    //   63: return
    //   64: pop
    //   65: goto -> 63
    // Exception table:
    //   from	to	target	type
    //   19	23	64	finally
    //   24	28	64	finally
    //   28	31	64	finally
    //   36	39	64	finally
    //   40	44	64	finally
    //   49	52	64	finally
    //   53	57	64	finally
    //   59	63	64	finally
  }
  
  private static boolean tapLine(Unit paramUnit, String paramString, int[] paramArrayOfint) {
    Unit unit = paramUnit;
    ArrayList arrayList = (ArrayList)feedLines.get(paramString);
    String str = "titanium-conveyor";
    Block block = blockByName("conveyor", str);
    int i = 0;
    if (arrayList != null) {
      boolean bool = arrayList.isEmpty();
      if (!bool && block != null) {
        HashSet<Integer> hashSet = new HashSet();
        this();
        World world = Vars.world;
        int j = world.width();
        Iterator<int[]> iterator = arrayList.iterator();
        while (true) {
          boolean bool1 = iterator.hasNext();
          boolean bool2 = true;
          if (bool1) {
            int[] arrayOfInt = iterator.next();
            int n = arrayOfInt[bool2] * j;
            int m = arrayOfInt[0];
            n += m;
            Integer integer = Integer.valueOf(n);
            hashSet.add(integer);
            continue;
          } 
          boolean[] arrayOfBoolean = AutoDrill.reservedForFeed(unit);
          int k = 0;
          iterator = null;
          while (true) {
            ArrayList<Integer> arrayList1 = feedReserved;
            int m = arrayList1.size();
            if (k < m) {
              Integer integer = arrayList1.get(k);
              int n = integer.intValue();
              arrayOfBoolean[n] = bool2;
              k++;
              continue;
            } 
            Seq seq = new Seq();
            this();
            Iterator<int[]> iterator1 = arrayList.iterator();
            while (true) {
              boolean bool3 = iterator1.hasNext();
              int n = 4;
              float f = 5.6E-45F;
              if (bool3) {
                int[] arrayOfInt = iterator1.next();
                World world1 = Vars.world;
                m = arrayOfInt[0];
                int i2 = arrayOfInt[bool2];
                Tile tile = world1.tile(m, i2);
                if (tile != null) {
                  Building building = tile.build;
                  if (building != null) {
                    Block block1 = tile.build.block;
                    bool1 = block1 instanceof mindustry.world.blocks.distribution.Conveyor;
                    if (bool1) {
                      bool1 = false;
                      block1 = null;
                      m = 1;
                      float f1 = Float.MIN_VALUE;
                      while (bool1 < n && m != 0) {
                        World world2 = Vars.world;
                        int i4 = arrayOfInt[0];
                        int[] arrayOfInt1 = DX;
                        int i5 = arrayOfInt1[bool1];
                        i4 += i5;
                        i5 = arrayOfInt[bool2];
                        int[] arrayOfInt2 = DY;
                        int i6 = arrayOfInt2[bool1];
                        i5 += i6;
                        Tile tile1 = world2.tile(i4, i5);
                        if (tile1 != null) {
                          Building building1 = tile1.build;
                          if (building1 != null) {
                            i4 = tile1.y * j;
                            i2 = tile1.x;
                            i4 += i2;
                            Integer integer = Integer.valueOf(i4);
                            boolean bool4 = hashSet.contains(integer);
                            if (!bool4) {
                              m = 0;
                              f1 = 0.0F;
                              building = null;
                            } 
                          } 
                        } 
                        int i3 = bool1 + 1;
                      } 
                      if (m != 0) {
                        int i3 = arrayOfInt[0];
                        int i4 = arrayOfInt[bool2];
                        f1 = i3;
                        float f2 = i4;
                        n = paramArrayOfint[0];
                        f = n;
                        int i5 = paramArrayOfint[bool2];
                        float f3 = i5;
                        f1 = Mathf.dst(f1, f2, f, f3);
                        i2 = 1092616192;
                        f2 = 10.0F;
                        f1 *= f2;
                        m = (int)f1;
                        arrayOfInt = new int[] { i3, i4, m };
                        seq.add(arrayOfInt);
                      } 
                    } 
                  } 
                } 
                continue;
              } 
              AutoSet$1 autoSet$1 = new AutoSet$1();
              this();
              seq.sort(autoSet$1);
              int i1 = 0;
              autoSet$1 = null;
              while (true) {
                int i2 = seq.size;
                if (i1 < i2) {
                  i2 = 14;
                  if (i1 < i2) {
                    int[] arrayOfInt = (int[])seq.get(i1);
                    int i3 = 0;
                    float f1 = 0.0F;
                    Object object = null;
                    while (i3 < n) {
                      j = arrayOfInt[0];
                      int[] arrayOfInt1 = DX;
                      int i5 = arrayOfInt1[i3];
                      j += i5;
                      i5 = arrayOfInt[bool2];
                      int[] arrayOfInt2 = DY;
                      int i4 = arrayOfInt2[i3];
                      i5 += i4;
                      i4 = paramArrayOfint[0];
                      m = paramArrayOfint[bool2];
                      float f2 = 2.8E-45F;
                      int i6 = paramArrayOfint[2];
                      Seq seq2 = routeTo(j, i5, i4, m, i6, arrayOfBoolean, block);
                      if (seq2 == null) {
                        i3++;
                        continue;
                      } 
                      Seq seq1 = new Seq();
                      this();
                      Iterator<BuildPlan> iterator2 = unit.plans().iterator();
                      while (true) {
                        boolean bool4 = iterator2.hasNext();
                        if (bool4) {
                          BuildPlan buildPlan1 = iterator2.next();
                          seq1.add(buildPlan1);
                          continue;
                        } 
                        BuildPlan buildPlan = new BuildPlan();
                        int i7 = arrayOfInt[0];
                        m = arrayOfInt[bool2];
                        Block block1 = Blocks.router;
                        this(i7, m, 0, block1);
                        unit.addBuild(buildPlan, bool2);
                        i5 = 0;
                        buildPlan = null;
                        while (true) {
                          i7 = seq2.size;
                          if (i5 < i7) {
                            BuildPlan buildPlan1 = (BuildPlan)seq2.get(i5);
                            unit.addBuild(buildPlan1, bool2);
                            i5++;
                            continue;
                          } 
                          seq2 = pending;
                          seq2.clear();
                          Iterator<BuildPlan> iterator3 = unit.plans().iterator();
                          while (true) {
                            boolean bool5 = iterator3.hasNext();
                            if (bool5) {
                              BuildPlan buildPlan1 = iterator3.next();
                              boolean bool6 = seq1.contains(buildPlan1, bool2);
                              if (!bool6) {
                                Seq seq3 = pending;
                                seq3.add(buildPlan1);
                              } 
                              continue;
                            } 
                            lastPending = pending.size;
                            lastProgress = Time.time;
                            StringBuilder stringBuilder1 = new StringBuilder();
                            this("Sem outra fonte livre: router em ");
                            i1 = arrayOfInt[0];
                            stringBuilder1.append(i1);
                            String str2 = ",";
                            stringBuilder1.append(str2);
                            int i8 = arrayOfInt[bool2];
                            stringBuilder1.append(i8);
                            String str1 = stringBuilder1.toString();
                            StringBuilder stringBuilder2 = new StringBuilder();
                            this("No other free source: router at ");
                            i = arrayOfInt[0];
                            stringBuilder2.append(i);
                            stringBuilder2.append(str2);
                            i1 = arrayOfInt[bool2];
                            stringBuilder2.append(i1);
                            str2 = stringBuilder2.toString();
                            toast(tr(str1, str2));
                            return bool2;
                          } 
                          break;
                        } 
                        break;
                      } 
                    } 
                    i1++;
                    continue;
                  } 
                } 
                break;
              } 
              break;
            } 
            break;
          } 
          break;
        } 
      } 
    } 
    return false;
  }
  
  private static void toast(String paramString) {
    UI uI = Vars.ui;
    if (uI != null) {
      uI = Vars.ui;
      float f = 3.0F;
      uI.showInfoToast(paramString, f);
    } 
  }
  
  private static String tr(String paramString1, String paramString2) {
    String str1 = "pt";
    String str2 = LunariansI18n.getLanguage();
    boolean bool = str1.equals(str2);
    return bool ? paramString1 : paramString2;
  }
  
  private static boolean tryDelete(Unit paramUnit, BuildPlan paramBuildPlan) {
    String str = "/30)";
    try {
      boolean bool = AutoSetConfig.autoDelete();
      if (bool) {
        int i = deleteCount;
        int j = 30;
        if (i < j) {
          Block block = paramBuildPlan.block;
          if (block != null) {
            boolean bool1 = paramBuildPlan.breaking;
            if (!bool1) {
              World world = Vars.world;
              int k = world.width();
              Block block1 = paramBuildPlan.block;
              j = block1.size;
              byte b1 = 1;
              j -= b1;
              byte b2 = 2;
              j /= b2;
              int m = 0;
              int n = 0;
              Building building = null;
              while (true) {
                Block block2 = paramBuildPlan.block;
                int i1 = block2.size;
                if (m < i1) {
                  i1 = 0;
                  block2 = null;
                  while (true) {
                    Block block3 = paramBuildPlan.block;
                    int i2 = block3.size;
                    if (i1 < i2) {
                      i2 = paramBuildPlan.x - j + i1;
                      int i3 = paramBuildPlan.y - j + m;
                      World world1 = Vars.world;
                      Tile tile = world1.tile(i2, i3);
                      if (tile != null) {
                        Building building1 = tile.build;
                        if (building1 != null) {
                          building1 = tile.build;
                          Team team1 = building1.team;
                          Player player = Vars.player;
                          Team team2 = player.team();
                          if (team1 == team2) {
                            Building building2 = tile.build;
                            Block block4 = building2.block;
                            Block block5 = paramBuildPlan.block;
                            if (block4 != block5) {
                              Building building3 = tile.build;
                              Block block6 = building3.block;
                              boolean bool2 = block6 instanceof mindustry.world.blocks.ConstructBlock;
                              if (!bool2) {
                                bool2 = block6 instanceof CoreBlock;
                                if (!bool2) {
                                  boolean bool3 = deletable(block6);
                                  if (bool3) {
                                    HashMap hashMap = ours;
                                    i3 = i3 * k + i2;
                                    Integer integer = Integer.valueOf(i3);
                                    integer = (Integer)hashMap.get(integer);
                                    integer = integer;
                                    if (integer != null) {
                                      i2 = integer.intValue();
                                      if (i2 >= b2) {
                                        HashSet<Integer> hashSet = deleted;
                                        Building building4 = tile.build;
                                        i3 = building4.pos();
                                        Integer integer1 = Integer.valueOf(i3);
                                        boolean bool4 = hashSet.contains(integer1);
                                        if (!bool4) {
                                          building = tile.build;
                                          n = building.pos();
                                          Integer integer2 = Integer.valueOf(n);
                                          hashSet.add(integer2);
                                          n = deleteCount + b1;
                                          deleteCount = n;
                                          BuildPlan buildPlan = new BuildPlan();
                                          Building building5 = tile.build;
                                          i2 = building5.tileX();
                                          Building building6 = tile.build;
                                          int i4 = building6.tileY();
                                          this(i2, i4);
                                          paramUnit.addBuild(buildPlan, b1);
                                          n = 1;
                                        } 
                                      } 
                                    } 
                                  } 
                                } 
                              } 
                            } 
                          } 
                        } 
                      } 
                      i1++;
                      continue;
                    } 
                    m++;
                  } 
                  break;
                } 
                if (n != 0) {
                  StringBuilder stringBuilder1 = new StringBuilder();
                  this();
                  String str3 = "Auto delete: removendo bloco que atrapalha (";
                  stringBuilder1.append(str3);
                  int i2 = deleteCount;
                  stringBuilder1.append(i2);
                  stringBuilder1.append(str);
                  String str1 = stringBuilder1.toString();
                  StringBuilder stringBuilder2 = new StringBuilder();
                  this();
                  String str4 = "Auto delete: removing blocking block (";
                  stringBuilder2.append(str4);
                  k = deleteCount;
                  stringBuilder2.append(k);
                  stringBuilder2.append(str);
                  String str2 = stringBuilder2.toString();
                  str1 = tr(str1, str2);
                  toast(str1);
                } 
                return n;
              } 
            } 
          } 
        } 
      } 
    } finally {}
    return false;
  }
  
  private static void update() {
    int j = Vars.headless;
    if (!j) {
      GameState gameState = Vars.state;
      if (gameState != null) {
        gameState = Vars.state;
        j = gameState.isGame();
        if (j) {
          j = 1;
          try {
            int m = phase;
            if (m == j) {
              float f1 = Time.time;
              float f2 = armedAt;
              f1 -= f2;
              int i1 = 1128792064;
              f2 = 200.0F;
              int n = f1 cmp f2;
              if (n > 0) {
                n = 0;
                f1 = 0.0F;
                String str = null;
                phase = 0;
                str = "as_timeout";
                return;
              } 
            } 
            int k = phase;
            byte b = 2;
            float f = 2.8E-45F;
            if (k == b)
              return; 
            b = 3;
          } finally {
            Exception exception = null;
            boolean bool = errorLogged;
          } 
          return;
        } 
      } 
    } 
    int i = phase;
    if (i != 0)
      reset(); 
  }
  
  static {
    byte b = 8;
    int[] arrayOfInt1 = new int[b];
    arrayOfInt1[0] = 1;
    arrayOfInt1[1] = 0;
    arrayOfInt1[2] = -1;
    arrayOfInt1[3] = 0;
    arrayOfInt1[4] = 1;
    arrayOfInt1[5] = 1;
    arrayOfInt1[6] = -1;
    arrayOfInt1[7] = -1;
    DX = arrayOfInt1;
    int[] arrayOfInt2 = new int[b];
    arrayOfInt2[0] = 0;
    arrayOfInt2[1] = 1;
    arrayOfInt2[2] = 0;
    arrayOfInt2[3] = -1;
    arrayOfInt2[4] = 1;
    arrayOfInt2[5] = -1;
    arrayOfInt2[6] = 1;
    arrayOfInt2[7] = -1;
    DY = arrayOfInt2;
  }
}
