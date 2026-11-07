package lunarians;

import arc.Events;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Time;
import lunarians.data.UnitSlot;
import mindustry.Vars;
import mindustry.ai.UnitStance;
import mindustry.ai.types.CommandAI;
import mindustry.core.GameState;
import mindustry.entities.EntityGroup;
import mindustry.entities.units.UnitController;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;

public class AutoUnit {
  private static final float SCAN = 1.5F;
  
  private static final IntSet claimed;
  
  private static int count;
  
  private static boolean errorLogged;
  
  private static float timer;
  
  static {
    IntSet intSet = new IntSet();
    this();
    claimed = intSet;
  }
  
  private static void apply(UnitSlot paramUnitSlot, Seq paramSeq) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual first : ()Ljava/lang/Object;
    //   4: checkcast mindustry/gen/Unit
    //   7: getfield type : Lmindustry/type/UnitType;
    //   10: astore_2
    //   11: aload_0
    //   12: getfield command : Ljava/lang/String;
    //   15: astore_3
    //   16: aload_3
    //   17: invokevirtual isEmpty : ()Z
    //   20: istore #4
    //   22: iconst_0
    //   23: istore #5
    //   25: iload #4
    //   27: ifne -> 114
    //   30: aload_2
    //   31: getfield commands : Larc/struct/Seq;
    //   34: astore_3
    //   35: aload_3
    //   36: ifnull -> 114
    //   39: iconst_0
    //   40: istore #4
    //   42: aconst_null
    //   43: astore_3
    //   44: aload_2
    //   45: getfield commands : Larc/struct/Seq;
    //   48: astore #6
    //   50: aload #6
    //   52: getfield size : I
    //   55: istore #7
    //   57: iload #4
    //   59: iload #7
    //   61: if_icmpge -> 114
    //   64: aload_2
    //   65: getfield commands : Larc/struct/Seq;
    //   68: iload #4
    //   70: invokevirtual get : (I)Ljava/lang/Object;
    //   73: checkcast mindustry/ai/UnitCommand
    //   76: astore #6
    //   78: aload #6
    //   80: getfield name : Ljava/lang/String;
    //   83: astore #8
    //   85: aload_0
    //   86: getfield command : Ljava/lang/String;
    //   89: astore #9
    //   91: aload #8
    //   93: aload #9
    //   95: invokevirtual equals : (Ljava/lang/Object;)Z
    //   98: istore #10
    //   100: iload #10
    //   102: ifne -> 120
    //   105: iload #4
    //   107: iconst_1
    //   108: iadd
    //   109: istore #4
    //   111: goto -> 44
    //   114: iconst_0
    //   115: istore #7
    //   117: aconst_null
    //   118: astore #6
    //   120: aload #6
    //   122: ifnull -> 238
    //   125: new arc/struct/IntSeq
    //   128: astore_2
    //   129: aload_2
    //   130: invokespecial <init> : ()V
    //   133: iconst_0
    //   134: istore #4
    //   136: aconst_null
    //   137: astore_3
    //   138: aload_1
    //   139: getfield size : I
    //   142: istore #10
    //   144: iload #4
    //   146: iload #10
    //   148: if_icmpge -> 211
    //   151: aload_1
    //   152: iload #4
    //   154: invokevirtual get : (I)Ljava/lang/Object;
    //   157: checkcast mindustry/gen/Unit
    //   160: invokevirtual controller : ()Lmindustry/entities/units/UnitController;
    //   163: checkcast mindustry/ai/types/CommandAI
    //   166: getfield command : Lmindustry/ai/UnitCommand;
    //   169: astore #8
    //   171: aload #8
    //   173: aload #6
    //   175: if_acmpeq -> 202
    //   178: aload_1
    //   179: iload #4
    //   181: invokevirtual get : (I)Ljava/lang/Object;
    //   184: checkcast mindustry/gen/Unit
    //   187: astore #8
    //   189: aload #8
    //   191: getfield id : I
    //   194: istore #10
    //   196: aload_2
    //   197: iload #10
    //   199: invokevirtual add : (I)V
    //   202: iload #4
    //   204: iconst_1
    //   205: iadd
    //   206: istore #4
    //   208: goto -> 138
    //   211: aload_2
    //   212: getfield size : I
    //   215: istore #4
    //   217: iload #4
    //   219: ifle -> 238
    //   222: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   225: astore_3
    //   226: aload_2
    //   227: invokevirtual toArray : ()[I
    //   230: astore_2
    //   231: aload_3
    //   232: aload_2
    //   233: aload #6
    //   235: invokestatic setUnitCommand : (Lmindustry/gen/Player;[ILmindustry/ai/UnitCommand;)V
    //   238: aload #6
    //   240: ifnull -> 347
    //   243: getstatic mindustry/ai/UnitCommand.mineCommand : Lmindustry/ai/UnitCommand;
    //   246: astore_2
    //   247: aload #6
    //   249: aload_2
    //   250: if_acmpne -> 347
    //   253: aload_0
    //   254: getfield items : Larc/struct/Seq;
    //   257: astore_2
    //   258: aload_2
    //   259: invokevirtual isEmpty : ()Z
    //   262: istore #11
    //   264: iload #11
    //   266: ifeq -> 280
    //   269: getstatic mindustry/ai/UnitStance.mineAuto : Lmindustry/ai/UnitStance;
    //   272: astore_0
    //   273: aload_1
    //   274: aload_0
    //   275: iconst_1
    //   276: invokestatic stance : (Larc/struct/Seq;Lmindustry/ai/UnitStance;Z)V
    //   279: return
    //   280: invokestatic all : ()Larc/struct/Seq;
    //   283: astore_2
    //   284: aload_2
    //   285: getfield size : I
    //   288: istore #4
    //   290: iload #5
    //   292: iload #4
    //   294: if_icmpge -> 347
    //   297: aload_2
    //   298: iload #5
    //   300: invokevirtual get : (I)Ljava/lang/Object;
    //   303: checkcast mindustry/ai/ItemUnitStance
    //   306: astore_3
    //   307: aload_0
    //   308: getfield items : Larc/struct/Seq;
    //   311: astore #6
    //   313: aload_3
    //   314: getfield item : Lmindustry/type/Item;
    //   317: getfield name : Ljava/lang/String;
    //   320: astore #8
    //   322: aload #6
    //   324: aload #8
    //   326: invokevirtual contains : (Ljava/lang/Object;)Z
    //   329: istore #7
    //   331: aload_1
    //   332: aload_3
    //   333: iload #7
    //   335: invokestatic stance : (Larc/struct/Seq;Lmindustry/ai/UnitStance;Z)V
    //   338: iload #5
    //   340: iconst_1
    //   341: iadd
    //   342: istore #5
    //   344: goto -> 284
    //   347: return
  }
  
  public static int count() {
    return count;
  }
  
  public static void init() {
    boolean bool = Vars.headless;
    if (bool)
      return; 
    EventType.Trigger trigger = EventType.Trigger.update;
    AutoUnit$1 autoUnit$1 = new AutoUnit$1();
    this();
    Events.run(trigger, autoUnit$1);
    AutoUnit$2 autoUnit$2 = new AutoUnit$2();
    this();
    Events.on(EventType.WorldLoadEvent.class, autoUnit$2);
  }
  
  public static void reset() {
    claimed.clear();
    timer = 0.0F;
    count = 0;
  }
  
  private static void scan() {
    Seq seq = LunariansSettings.unitSlots;
    boolean bool = seq.isEmpty();
    if (!bool) {
      claimed.clear();
      bool = false;
      count = 0;
      int i = 0;
      while (true) {
        int j = seq.size;
        if (i < j) {
          UnitSlot unitSlot = (UnitSlot)seq.get(i);
          boolean bool1 = unitSlot.ready();
          if (bool1) {
            Seq seq1 = new Seq();
            this();
            EntityGroup entityGroup = Groups.unit;
            int k = entityGroup.size();
            int m = 0;
            IntSet intSet = null;
            while (m < k) {
              Unit unit = (Unit)Groups.unit.index(m);
              if (unit != null) {
                boolean bool2 = unit.isValid();
                if (bool2) {
                  Team team1 = unit.team;
                  Team team2 = Vars.player.team();
                  if (team1 == team2) {
                    IntSet intSet1 = claimed;
                    int n = unit.id;
                    bool2 = intSet1.contains(n);
                    if (!bool2) {
                      UnitController unitController = unit.controller();
                      bool2 = unitController instanceof CommandAI;
                      if (bool2) {
                        String str1 = unitSlot.unit;
                        String str2 = unit.type.name;
                        bool2 = str1.equals(str2);
                        if (bool2)
                          seq1.add(unit); 
                      } 
                    } 
                  } 
                } 
              } 
              m++;
            } 
            k = unitSlot.maxUnits;
            if (k > 0) {
              k = seq1.size;
              m = unitSlot.maxUnits;
              if (k > m) {
                k = unitSlot.maxUnits;
                seq1.truncate(k);
              } 
            } 
            k = 0;
            entityGroup = null;
            while (true) {
              m = seq1.size;
              if (k < m) {
                intSet = claimed;
                Unit unit = (Unit)seq1.get(k);
                int n = unit.id;
                intSet.add(n);
                k++;
                continue;
              } 
              boolean bool2 = seq1.isEmpty();
              if (!bool2) {
                apply(unitSlot, seq1);
                j = count;
                int n = seq1.size;
                j += n;
                count = j;
              } 
              break;
            } 
          } 
          i++;
          continue;
        } 
        break;
      } 
    } 
  }
  
  private static void stance(Seq paramSeq, UnitStance paramUnitStance, boolean paramBoolean) {
    IntSeq intSeq = new IntSeq();
    this();
    int i = 0;
    while (true) {
      int j = paramSeq.size;
      if (i < j) {
        CommandAI commandAI = (CommandAI)((Unit)paramSeq.get(i)).controller();
        boolean bool = commandAI.hasStance(paramUnitStance);
        if (bool != paramBoolean) {
          Unit unit = (Unit)paramSeq.get(i);
          int m = unit.id;
          intSeq.add(m);
        } 
        i++;
        continue;
      } 
      int k = intSeq.size;
      if (k > 0) {
        Player player = Vars.player;
        int[] arrayOfInt = intSeq.toArray();
        Call.setUnitStance(player, arrayOfInt, paramUnitStance, paramBoolean);
      } 
      return;
    } 
  }
  
  private static void update() {
    boolean bool = LunariansSettings.enableAutoUnit;
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
            int j = 1069547520;
            f2 = LunariansSettings.actionInterval(1.5F);
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
          } 
        } 
      } 
    } 
  }
}
