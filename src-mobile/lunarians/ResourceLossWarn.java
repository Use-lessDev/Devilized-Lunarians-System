package lunarians;

import arc.Events;
import arc.struct.ObjectFloatMap;
import arc.struct.ObjectIntMap;
import arc.struct.Seq;
import arc.util.Time;
import lunarians.data.LossSlot;
import mindustry.Vars;
import mindustry.core.ContentLoader;
import mindustry.core.GameState;
import mindustry.core.UI;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Player;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.ui.fragments.ChatFragment;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumeItems;
import mindustry.world.modules.ItemModule;

public class ResourceLossWarn {
  private static final float COOLDOWN = 30.0F;
  
  public static final int MAX_SLOTS = 25;
  
  public static final int PANELS = 4;
  
  private static final float SAMPLE = 0.5F;
  
  private static final float SMOOTH = 0.35F;
  
  private static final int SUSTAIN = 4;
  
  private static final ObjectFloatMap acc;
  
  private static float clock;
  
  private static final ObjectFloatMap ema;
  
  private static boolean errorLogged;
  
  private static final ObjectIntMap last;
  
  private static final ObjectFloatMap lastWarn;
  
  private static final ObjectIntMap streak;
  
  private static float timer;
  
  static {
    ObjectFloatMap objectFloatMap3 = new ObjectFloatMap();
    this();
    acc = objectFloatMap3;
    ObjectIntMap objectIntMap2 = new ObjectIntMap();
    this();
    last = objectIntMap2;
    ObjectFloatMap objectFloatMap2 = new ObjectFloatMap();
    this();
    ema = objectFloatMap2;
    ObjectIntMap objectIntMap1 = new ObjectIntMap();
    this();
    streak = objectIntMap1;
    ObjectFloatMap objectFloatMap1 = new ObjectFloatMap();
    this();
    lastWarn = objectFloatMap1;
  }
  
  private static float amountPerCraft(Block paramBlock, Item paramItem) {
    Consume[] arrayOfConsume = paramBlock.consumers;
    if (arrayOfConsume == null)
      return 0.0F; 
    for (Consume consume : paramBlock.consumers) {
      boolean bool = consume instanceof ConsumeItems;
      if (bool) {
        ItemStack[] arrayOfItemStack = ((ConsumeItems)consume).items;
        int i = arrayOfItemStack.length;
        int j = 0;
        while (j < i) {
          ItemStack itemStack = arrayOfItemStack[j];
          Item item = itemStack.item;
          if (item != paramItem) {
            j++;
            continue;
          } 
          return itemStack.amount;
        } 
      } 
    } 
    return 0.0F;
  }
  
  private static String consumers(Team paramTeam, Item paramItem, float paramFloat) {
    ObjectIntMap objectIntMap = new ObjectIntMap();
    this();
    ObjectFloatMap objectFloatMap = new ObjectFloatMap();
    this();
    Seq seq = (paramTeam.data()).buildings;
    int i = 0;
    ObjectIntMap.Keys<Block> keys = null;
    while (true) {
      int j = seq.size;
      if (i < j) {
        Object object = seq.get(i);
        if (object != null) {
          object = object;
          boolean bool = object.isValid();
          if (bool) {
            Block block = ((Building)object).block;
            bool = block instanceof GenericCrafter;
            if (bool) {
              float f = ((Building)object).efficiency;
              int m = f cmp 0.0F;
              if (m > 0) {
                boolean bool1 = object.shouldConsume();
                if (bool1) {
                  block = ((Building)object).block;
                  f = amountPerCraft(block, paramItem);
                  int n = f cmp 0.0F;
                  if (n > 0) {
                    float f1 = ((GenericCrafter)((Building)object).block).craftTime / 60.0F;
                    f /= f1;
                    f1 = ((Building)object).efficiency;
                    f *= f1;
                    Block block1 = ((Building)object).block;
                    boolean bool2 = true;
                    objectIntMap.increment(block1, 0, bool2);
                    block1 = ((Building)object).block;
                    object = ((Building)object).block;
                    float f2 = objectFloatMap.get(object, 0.0F) + f;
                    objectFloatMap.put(block1, f2);
                  } 
                } 
              } 
            } 
          } 
        } 
        i++;
        continue;
      } 
      int k = objectIntMap.size;
      String str = "loss_other";
      if (k == 0)
        return LunariansI18n.get(str); 
      seq = new Seq();
      this();
      keys = objectIntMap.keys().iterator();
      while (true) {
        boolean bool = keys.hasNext();
        if (bool) {
          Block block = keys.next();
          seq.add(block);
          continue;
        } 
        ResourceLossWarn$3 resourceLossWarn$3 = new ResourceLossWarn$3();
        this(objectFloatMap);
        seq.sort(resourceLossWarn$3);
        StringBuilder stringBuilder = new StringBuilder();
        this();
        bool = false;
        float f2 = 0.0F;
        Object object1 = null;
        boolean bool1 = false;
        float f1 = 0.0F;
        Object object2 = null;
        while (true) {
          int m = seq.size;
          String str1 = ", ";
          if (bool < m) {
            Block block = (Block)seq.get(bool);
            float f3 = objectFloatMap.get(block, 0.0F);
            f1 += f3;
            m = 2;
            f3 = 2.8E-45F;
            if (bool < m) {
              m = stringBuilder.length();
              if (m > 0)
                stringBuilder.append(str1); 
              String str2 = ((Block)seq.get(bool)).localizedName;
              stringBuilder.append(str2);
              stringBuilder.append(" x");
              Block block1 = (Block)seq.get(bool);
              m = objectIntMap.get(block1, 0);
              stringBuilder.append(m);
            } 
            int n = bool + 1;
            continue;
          } 
          float f = 0.6F;
          paramFloat *= f;
          k = f1 cmp paramFloat;
          if (k < 0) {
            stringBuilder.append(str1);
            String str2 = LunariansI18n.get(str);
            stringBuilder.append(str2);
          } 
          return stringBuilder.toString();
        } 
        break;
      } 
      break;
    } 
  }
  
  private static String fmt(float paramFloat) {
    float f = 10.0F;
    return String.valueOf(Math.round(paramFloat * f) / f);
  }
  
  public static void init() {
    boolean bool = Vars.headless;
    if (bool)
      return; 
    EventType.Trigger trigger = EventType.Trigger.update;
    ResourceLossWarn$1 resourceLossWarn$1 = new ResourceLossWarn$1();
    this();
    Events.run(trigger, resourceLossWarn$1);
    ResourceLossWarn$2 resourceLossWarn$2 = new ResourceLossWarn$2();
    this();
    Events.on(EventType.WorldLoadEvent.class, resourceLossWarn$2);
  }
  
  public static void reset() {
    acc.clear();
    last.clear();
    ema.clear();
    streak.clear();
    lastWarn.clear();
    timer = 0.0F;
  }
  
  private static void sample(float paramFloat) {
    Team team = Vars.player.team();
    if (team != null) {
      CoreBlock.CoreBuild coreBuild = team.core();
      if (coreBuild != null) {
        ItemModule itemModule = team.items();
        int i = LunariansSettings.lossPanel;
        Seq seq = LunariansSettings.lossSlots(i);
        int j = 0;
        while (true) {
          int k = seq.size;
          if (j < k) {
            LossSlot lossSlot = (LossSlot)seq.get(j);
            if (lossSlot != null) {
              String str = lossSlot.item;
              boolean bool = str.isEmpty();
              if (!bool) {
                ContentLoader contentLoader = Vars.content;
                String str1 = lossSlot.item;
                Item item = contentLoader.item(str1);
                if (item != null) {
                  ObjectFloatMap objectFloatMap = acc;
                  float f1 = 0.0F;
                  float f2 = objectFloatMap.get(item, 0.0F) + paramFloat;
                  float f3 = lossSlot.delay;
                  float f4 = 1.0F;
                  f3 = Math.max(f4, f3);
                  int m = 1008981770;
                  float f5 = 0.01F;
                  f3 -= f5;
                  int n = f2 cmp f3;
                  if (n < 0) {
                    objectFloatMap.put(item, f2);
                  } else {
                    ObjectFloatMap objectFloatMap1;
                    objectFloatMap.put(item, 0.0F);
                    int i1 = itemModule.get(item);
                    ObjectIntMap objectIntMap = last;
                    boolean bool1 = objectIntMap.containsKey(item);
                    if (!bool1) {
                      objectIntMap.put(item, i1);
                      objectFloatMap1 = ema;
                      objectFloatMap1.put(item, 0.0F);
                    } else {
                      int i2 = objectIntMap.get(item, i1) - i1;
                      f5 = i2 / f2;
                      objectIntMap.put(item, i1);
                      float f = ((LossSlot)objectFloatMap1).delay;
                      f = Math.max(f4, f);
                      f2 = 0.35F;
                      f *= f2;
                      f = Math.min(f4, f);
                      ObjectFloatMap objectFloatMap2 = ema;
                      f1 = objectFloatMap2.get(item, 0.0F);
                      f3 = f4 - f;
                      f1 *= f3;
                      f5 *= f;
                      f1 += f5;
                      objectFloatMap2.put(item, f1);
                      f = ((LossSlot)objectFloatMap1).perSec;
                      i1 = f1 cmp f;
                      ObjectIntMap objectIntMap1 = streak;
                      if (i1 >= 0) {
                        i1 = objectIntMap1.get(item, 0);
                        n = 1;
                        f3 = Float.MIN_VALUE;
                        i1 += n;
                        objectIntMap1.put(item, i1);
                        float f6 = ((LossSlot)objectFloatMap1).delay;
                        f6 = Math.max(f4, f6);
                        f2 = 4.0F / f6;
                        k = Math.round(f2);
                        k = Math.max(n, k);
                        if (i1 >= k) {
                          f6 = clock;
                          objectFloatMap = lastWarn;
                          f2 = objectFloatMap.get(item, -999.0F);
                          f6 -= f2;
                          f2 = 30.0F;
                          k = f6 cmp f2;
                          if (k > 0) {
                            f6 = clock;
                            objectFloatMap.put(item, f6);
                            warn(team, item, f1);
                          } 
                        } 
                      } else {
                        objectIntMap1.put(item, 0);
                      } 
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
    } 
  }
  
  private static void update() {
    boolean bool = LunariansSettings.enableLossWarn;
    if (bool) {
      GameState gameState = Vars.state;
      if (gameState != null) {
        gameState = Vars.state;
        bool = gameState.isGame();
        if (bool) {
          Player player = Vars.player;
          if (player != null) {
            float f = Time.delta / 60.0F;
            boolean bool1 = false;
            String str = null;
            int i = f cmp 0.0F;
            if (i > 0) {
              clock += f;
              float f1 = timer + f;
              timer = f1;
              f = 0.5F;
              int j = f1 cmp f;
              if (j >= 0) {
                timer = 0.0F;
                try {
                  return;
                } finally {
                  player = null;
                  bool1 = errorLogged;
                } 
              } 
            } 
          } 
        } 
      } 
    } 
  }
  
  private static void warn(Team paramTeam, Item paramItem, float paramFloat) {
    StringBuilder stringBuilder = new StringBuilder();
    this();
    String str3 = fmt(paramFloat);
    String str4 = paramItem.localizedName;
    String str5 = "loss_warn";
    str3 = LunariansI18n.get(str5, str3, str4);
    stringBuilder.append(str3);
    stringBuilder.append(" ");
    str3 = paramItem.emoji();
    stringBuilder.append(str3);
    String str2 = stringBuilder.toString();
    String str1 = consumers(paramTeam, paramItem, paramFloat);
    boolean bool = str1.isEmpty();
    if (!bool) {
      StringBuilder stringBuilder1 = new StringBuilder();
      this();
      stringBuilder1.append(str2);
      String str = "loss_by";
      str1 = LunariansI18n.get(str, str1);
      stringBuilder1.append(str1);
      str2 = stringBuilder1.toString();
    } 
    try {
      UI uI = Vars.ui;
      ChatFragment chatFragment = uI.chatfrag;
      chatFragment.addMessage(str2);
    } finally {}
  }
}
