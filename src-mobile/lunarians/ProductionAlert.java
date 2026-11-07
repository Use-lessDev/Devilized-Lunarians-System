package lunarians;

import arc.Events;
import arc.graphics.Color;
import arc.struct.IntFloatMap;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.core.UI;
import mindustry.ctype.UnlockableContent;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Player;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.type.UnitType;
import mindustry.ui.fragments.ChatFragment;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.units.Reconstructor;
import mindustry.world.blocks.units.UnitFactory;

public class ProductionAlert {
  private static final float IDLE_RESET = 45.0F;
  
  private static final int MAX_PER_SCAN = 3;
  
  private static final float SCAN_INTERVAL = 1.5F;
  
  private static float clock;
  
  private static boolean errorLogged;
  
  private static final IntFloatMap lastSeen;
  
  private static int notifiedThisScan;
  
  private static int silentScans;
  
  private static float timer;
  
  static {
    IntFloatMap intFloatMap = new IntFloatMap();
    this();
    lastSeen = intFloatMap;
  }
  
  private static void announce(Team paramTeam, UnlockableContent paramUnlockableContent) {
    String str3 = paramUnlockableContent.emoji();
    String str1 = teamName(paramTeam);
    String str2 = paramUnlockableContent.localizedName;
    String str4 = "prod_started";
    str1 = LunariansI18n.get(str4, str1, str2);
    try {
      UI uI = Vars.ui;
      ChatFragment chatFragment = uI.chatfrag;
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append(str1);
      str1 = " ";
      stringBuilder.append(str1);
      stringBuilder.append(str3);
      str1 = stringBuilder.toString();
      chatFragment.addMessage(str1);
    } finally {}
  }
  
  private static void found(Team paramTeam, UnlockableContent paramUnlockableContent, boolean paramBoolean) {
    if (paramTeam != null && paramUnlockableContent != null) {
      int j = paramTeam.id << 20;
      int k = paramUnlockableContent.getContentType().ordinal() << 14;
      j |= k;
      k = paramUnlockableContent.id & 0x3FFF;
      j |= k;
      IntFloatMap intFloatMap = lastSeen;
      boolean bool = intFloatMap.containsKey(j);
      int m = 0;
      float f1 = intFloatMap.get(j, 0.0F);
      if (bool) {
        float f3 = clock;
        f1 = f3 - f1;
        float f4 = 45.0F;
        m = f1 cmp f4;
        if (m <= 0) {
          intFloatMap.put(j, f3);
          return;
        } 
      } 
      if (paramBoolean) {
        float f = clock;
        intFloatMap.put(j, f);
        return;
      } 
      int i = notifiedThisScan;
      byte b = 3;
      float f2 = 4.2E-45F;
      if (i < b) {
        notifiedThisScan = ++i;
        float f = clock;
        intFloatMap.put(j, f);
        announce(paramTeam, paramUnlockableContent);
      } 
    } 
  }
  
  public static void init() {
    boolean bool = Vars.headless;
    if (bool)
      return; 
    EventType.Trigger trigger = EventType.Trigger.update;
    ProductionAlert$1 productionAlert$1 = new ProductionAlert$1();
    this();
    Events.run(trigger, productionAlert$1);
    ProductionAlert$2 productionAlert$2 = new ProductionAlert$2();
    this();
    Events.on(EventType.WorldLoadEvent.class, productionAlert$2);
  }
  
  private static void inspect(Building paramBuilding, boolean paramBoolean) {
    Team team;
    boolean bool = paramBuilding instanceof UnitFactory.UnitFactoryBuild;
    int i = 0;
    float f = 0.0F;
    Seq seq = null;
    if (bool) {
      Building building = paramBuilding;
      UnitFactory.UnitFactoryBuild unitFactoryBuild = (UnitFactory.UnitFactoryBuild)paramBuilding;
      UnitFactory unitFactory = (UnitFactory)paramBuilding.block;
      int j = unitFactoryBuild.currentPlan;
      if (j >= 0) {
        j = unitFactoryBuild.currentPlan;
        Seq seq1 = unitFactory.plans;
        int k = seq1.size;
        if (j < k) {
          float f1 = unitFactoryBuild.efficiency;
          i = f1 cmp 0.0F;
          if (i > 0) {
            boolean bool1 = unitFactoryBuild.shouldConsume();
            if (bool1) {
              UnitType unitType;
              seq = unitFactory.plans;
              int m = unitFactoryBuild.currentPlan;
              UnitFactory.UnitPlan unitPlan = (UnitFactory.UnitPlan)seq.get(m);
              team = paramBuilding.team;
              if (unitPlan == null) {
                m = 0;
                float f2 = 0.0F;
                unitPlan = null;
              } else {
                unitType = unitPlan.unit;
              } 
              found(team, (UnlockableContent)unitType, paramBoolean);
              return;
            } 
          } 
        } 
      } 
    } else {
      bool = team instanceof Reconstructor.ReconstructorBuild;
      if (bool) {
        Team team1 = team;
        Reconstructor.ReconstructorBuild reconstructorBuild = (Reconstructor.ReconstructorBuild)team;
        boolean bool1 = reconstructorBuild.constructing();
        if (bool1) {
          float f1 = reconstructorBuild.efficiency;
          i = f1 cmp 0.0F;
          if (i > 0) {
            UnitType unitType = reconstructorBuild.unit();
            found(((Building)team).team, (UnlockableContent)unitType, paramBoolean);
            return;
          } 
        } 
      } else {
        bool = team instanceof GenericCrafter.GenericCrafterBuild;
        if (bool) {
          float f1 = ((Building)team).efficiency;
          int j = f1 cmp 0.0F;
          if (j > 0) {
            boolean bool1 = ((Building)team).shouldConsume();
            if (bool1) {
              LiquidStack[] arrayOfLiquidStack2;
              GenericCrafter genericCrafter = (GenericCrafter)((Building)team).block;
              ItemStack[] arrayOfItemStack = genericCrafter.outputItems;
              int k = 0;
              float f2 = 0.0F;
              Object object = null;
              if (arrayOfItemStack != null) {
                arrayOfItemStack = genericCrafter.outputItems;
                int m = arrayOfItemStack.length;
                int n = 0;
                Object object1 = null;
                while (n < m) {
                  ItemStack itemStack = arrayOfItemStack[n];
                  if (itemStack != null) {
                    Team team1 = ((Building)team).team;
                    Item item = itemStack.item;
                    found(team1, (UnlockableContent)item, paramBoolean);
                  } 
                  n++;
                } 
              } else {
                ItemStack itemStack = genericCrafter.outputItem;
                if (itemStack != null) {
                  Team team1 = ((Building)team).team;
                  Item item = genericCrafter.outputItem.item;
                  found(team1, (UnlockableContent)item, paramBoolean);
                } 
              } 
              LiquidStack[] arrayOfLiquidStack1 = genericCrafter.outputLiquids;
              if (arrayOfLiquidStack1 != null) {
                arrayOfLiquidStack2 = genericCrafter.outputLiquids;
                i = arrayOfLiquidStack2.length;
                while (k < i) {
                  LiquidStack liquidStack = arrayOfLiquidStack2[k];
                  if (liquidStack != null) {
                    Team team1 = ((Building)team).team;
                    Liquid liquid = liquidStack.liquid;
                    found(team1, (UnlockableContent)liquid, paramBoolean);
                  } 
                  k++;
                } 
              } else {
                LiquidStack liquidStack = ((GenericCrafter)arrayOfLiquidStack2).outputLiquid;
                if (liquidStack != null) {
                  team = ((Building)team).team;
                  Liquid liquid = ((GenericCrafter)arrayOfLiquidStack2).outputLiquid.liquid;
                  found(team, (UnlockableContent)liquid, paramBoolean);
                  return;
                } 
              } 
            } 
          } 
        } else {
          bool = team instanceof Drill.DrillBuild;
          if (bool) {
            Drill.DrillBuild drillBuild = (Drill.DrillBuild)team;
            Item item = drillBuild.dominantItem;
            if (item != null) {
              float f1 = drillBuild.warmup;
              i = 1050253722;
              f = 0.3F;
              int j = f1 cmp f;
              if (j > 0) {
                Team team1 = drillBuild.team;
                Item item1 = drillBuild.dominantItem;
                found(team1, (UnlockableContent)item1, paramBoolean);
              } 
            } 
          } 
        } 
      } 
    } 
  }
  
  public static void reset() {
    lastSeen.clear();
    silentScans = 2;
    timer = 0.0F;
  }
  
  private static void scan() {
    // Byte code:
    //   0: iconst_0
    //   1: istore_0
    //   2: iconst_0
    //   3: putstatic lunarians/ProductionAlert.notifiedThisScan : I
    //   6: getstatic lunarians/ProductionAlert.silentScans : I
    //   9: istore_1
    //   10: iconst_1
    //   11: istore_2
    //   12: iload_1
    //   13: ifle -> 21
    //   16: iconst_1
    //   17: istore_1
    //   18: goto -> 23
    //   21: iconst_0
    //   22: istore_1
    //   23: getstatic mindustry/Vars.player : Lmindustry/gen/Player;
    //   26: invokevirtual team : ()Lmindustry/game/Team;
    //   29: astore_3
    //   30: getstatic mindustry/gen/Groups.build : Lmindustry/entities/EntityGroup;
    //   33: astore #4
    //   35: aload #4
    //   37: invokevirtual size : ()I
    //   40: istore #5
    //   42: iload_0
    //   43: iload #5
    //   45: if_icmpge -> 144
    //   48: getstatic mindustry/gen/Groups.build : Lmindustry/entities/EntityGroup;
    //   51: iload_0
    //   52: invokevirtual index : (I)Lmindustry/gen/Entityc;
    //   55: checkcast mindustry/gen/Building
    //   58: astore #6
    //   60: aload #6
    //   62: ifnull -> 137
    //   65: aload #6
    //   67: invokevirtual isValid : ()Z
    //   70: istore #7
    //   72: iload #7
    //   74: ifeq -> 137
    //   77: aload #6
    //   79: getfield team : Lmindustry/game/Team;
    //   82: astore #8
    //   84: aload #8
    //   86: ifnull -> 137
    //   89: aload #6
    //   91: getfield team : Lmindustry/game/Team;
    //   94: astore #8
    //   96: getstatic mindustry/game/Team.derelict : Lmindustry/game/Team;
    //   99: astore #9
    //   101: aload #8
    //   103: aload #9
    //   105: if_acmpeq -> 137
    //   108: getstatic lunarians/LunariansSettings.productionIgnoreOwn : Z
    //   111: istore #7
    //   113: iload #7
    //   115: ifeq -> 131
    //   118: aload #6
    //   120: getfield team : Lmindustry/game/Team;
    //   123: astore #8
    //   125: aload #8
    //   127: aload_3
    //   128: if_acmpeq -> 137
    //   131: aload #6
    //   133: iload_1
    //   134: invokestatic inspect : (Lmindustry/gen/Building;Z)V
    //   137: iload_0
    //   138: iconst_1
    //   139: iadd
    //   140: istore_0
    //   141: goto -> 42
    //   144: getstatic lunarians/ProductionAlert.silentScans : I
    //   147: istore_0
    //   148: iload_0
    //   149: ifle -> 160
    //   152: iload_0
    //   153: iload_2
    //   154: isub
    //   155: istore_0
    //   156: iload_0
    //   157: putstatic lunarians/ProductionAlert.silentScans : I
    //   160: return
  }
  
  public static String teamName(Team paramTeam) {
    StringBuilder stringBuilder1 = new StringBuilder();
    this("[#");
    String str1;
    Color color = paramTeam.color;
    stringBuilder1.append(color);
    stringBuilder1.append("]");
    String str2 = stringBuilder1.toString();
    String str3 = paramTeam.name;
    String str4 = "[]";
    if (str3 != null) {
      str3 = paramTeam.name;
      String str = "team#";
      boolean bool = str3.startsWith(str);
      if (!bool) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append(str2);
        str1 = paramTeam.localized();
        stringBuilder.append(str1);
        stringBuilder.append(str4);
        return stringBuilder.toString();
      } 
    } 
    StringBuilder stringBuilder2 = new StringBuilder();
    this();
    stringBuilder2.append(str2);
    stringBuilder2.append("Team ");
    int i = ((Team)str1).id;
    stringBuilder2.append(i);
    stringBuilder2.append(str4);
    return stringBuilder2.toString();
  }
  
  private static void update() {
    boolean bool = LunariansSettings.enableProductionAlert;
    if (bool) {
      GameState gameState = Vars.state;
      if (gameState != null) {
        gameState = Vars.state;
        bool = gameState.isGame();
        if (bool) {
          Player player = Vars.player;
          if (player != null) {
            float f1 = Time.delta;
            int j = 1114636288;
            f1 /= 60.0F;
            clock += f1;
            float f2 = timer + f1;
            timer = f2;
            f1 = 1.5F;
            int i = f2 cmp f1;
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
