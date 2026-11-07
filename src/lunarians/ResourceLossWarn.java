package lunarians;

import arc.Events;
import arc.func.Cons;
import arc.struct.ObjectFloatMap;
import arc.struct.ObjectIntMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import java.util.Comparator;
import lunarians.data.LossSlot;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumeItems;
import mindustry.world.modules.ItemModule;

public class ResourceLossWarn {
  public static final int PANELS = 4;
  
  public static final int MAX_SLOTS = 25;
  
  private static final float SAMPLE = 0.5F;
  
  private static final float SMOOTH = 0.35F;
  
  private static final int SUSTAIN = 4;
  
  private static final float COOLDOWN = 30.0F;
  
  private static final ObjectFloatMap<Item> acc = new ObjectFloatMap();
  
  private static final ObjectIntMap<Item> last = new ObjectIntMap();
  
  private static final ObjectFloatMap<Item> ema = new ObjectFloatMap();
  
  private static final ObjectIntMap<Item> streak = new ObjectIntMap();
  
  private static final ObjectFloatMap<Item> lastWarn = new ObjectFloatMap();
  
  private static float clock;
  
  private static float timer;
  
  private static boolean errorLogged;
  
  public static void init() {
    if (Vars.headless)
      return; 
    Events.run(EventType.Trigger.update, new Runnable() {
          public void run() {
            ResourceLossWarn.update();
          }
        });
    Events.on(EventType.WorldLoadEvent.class, new Cons<EventType.WorldLoadEvent>() {
          public void get(EventType.WorldLoadEvent param1WorldLoadEvent) {
            ResourceLossWarn.reset();
          }
        });
  }
  
  public static void reset() {
    acc.clear();
    last.clear();
    ema.clear();
    streak.clear();
    lastWarn.clear();
    timer = 0.0F;
  }
  
  private static void update() {
    if (!LunariansSettings.enableLossWarn || Vars.state == null || !Vars.state.isGame() || Vars.player == null)
      return; 
    float f1 = Time.delta / 60.0F;
    if (f1 <= 0.0F)
      return; 
    clock += f1;
    if ((timer += f1) < 0.5F)
      return; 
    float f2 = timer;
    timer = 0.0F;
    try {
      sample(f2);
    } catch (Throwable throwable) {
      if (!errorLogged) {
        errorLogged = true;
        Log.err("[Lunarians] ResourceLossWarn error", throwable);
      } 
    } 
  }
  
  private static void sample(float paramFloat) {
    Team team = Vars.player.team();
    if (team == null || team.core() == null)
      return; 
    ItemModule itemModule = team.items();
    Seq<LossSlot> seq = LunariansSettings.lossSlots(LunariansSettings.lossPanel);
    for (byte b = 0; b < seq.size; b++) {
      LossSlot lossSlot = (LossSlot)seq.get(b);
      Item item;
      if (lossSlot != null && !lossSlot.item.isEmpty() && (item = Vars.content.item(lossSlot.item)) != null) {
        float f = acc.get(item, 0.0F) + paramFloat;
        if (f < Math.max(1.0F, lossSlot.delay) - 0.01F) {
          acc.put(item, f);
        } else {
          acc.put(item, 0.0F);
          float f1 = f;
          int i = itemModule.get(item);
          if (!last.containsKey(item)) {
            last.put(item, i);
            ema.put(item, 0.0F);
          } else {
            float f2 = (last.get(item, i) - i) / f1;
            last.put(item, i);
            float f3 = Math.min(1.0F, 0.35F * Math.max(1.0F, lossSlot.delay));
            float f4 = ema.get(item, 0.0F) * (1.0F - f3) + f2 * f3;
            ema.put(item, f4);
            if (f4 >= lossSlot.perSec) {
              int j = streak.get(item, 0) + 1;
              streak.put(item, j);
              if (j >= Math.max(1, Math.round(4.0F / Math.max(1.0F, lossSlot.delay))) && clock - lastWarn.get(item, -999.0F) > 30.0F) {
                lastWarn.put(item, clock);
                warn(team, item, f4);
              } 
            } else {
              streak.put(item, 0);
            } 
          } 
        } 
      } 
    } 
  }
  
  private static void warn(Team paramTeam, Item paramItem, float paramFloat) {
    String str1 = LunariansI18n.get("loss_warn", fmt(paramFloat), paramItem.localizedName) + " " + paramItem.emoji();
    String str2 = consumers(paramTeam, paramItem, paramFloat);
    if (!str2.isEmpty())
      str1 = str1 + LunariansI18n.get("loss_by", str2); 
    try {
      Vars.ui.chatfrag.addMessage(str1);
    } catch (Throwable throwable) {}
  }
  
  private static String consumers(Team paramTeam, Item paramItem, float paramFloat) {
    ObjectIntMap objectIntMap = new ObjectIntMap();
    ObjectFloatMap objectFloatMap1 = new ObjectFloatMap();
    Seq seq1 = (paramTeam.data()).buildings;
    for (byte b1 = 0; b1 < seq1.size; b1++) {
      Object object = seq1.get(b1);
      float f1;
      if (object != null && ((Building)object).isValid() && ((Building)object).block instanceof GenericCrafter && ((Building)object).efficiency > 0.0F && ((Building)object).shouldConsume() && (f1 = amountPerCraft(((Building)object).block, paramItem)) > 0.0F) {
        GenericCrafter genericCrafter = (GenericCrafter)((Building)object).block;
        float f2 = f1 / genericCrafter.craftTime / 60.0F * ((Building)object).efficiency;
        objectIntMap.increment(((Building)object).block, 0, 1);
        objectFloatMap1.put(((Building)object).block, objectFloatMap1.get(((Building)object).block, 0.0F) + f2);
      } 
    } 
    if (objectIntMap.size == 0)
      return LunariansI18n.get("loss_other"); 
    Seq seq2 = new Seq();
    for (ObjectIntMap.Keys<Block> keys = objectIntMap.keys().iterator(); keys.hasNext(); ) {
      Block block = keys.next();
      seq2.add(block);
    } 
    final ObjectFloatMap fm = objectFloatMap1;
    seq2.sort(new Comparator<Block>() {
          public int compare(Block param1Block1, Block param1Block2) {
            return Float.compare(fm.get(param1Block2, 0.0F), fm.get(param1Block1, 0.0F));
          }
        });
    StringBuilder stringBuilder = new StringBuilder();
    float f = 0.0F;
    for (byte b2 = 0; b2 < seq2.size; b2++) {
      f += objectFloatMap1.get(seq2.get(b2), 0.0F);
      if (b2 < 2) {
        if (stringBuilder.length() > 0)
          stringBuilder.append(", "); 
        stringBuilder.append(((Block)seq2.get(b2)).localizedName).append(" x").append(objectIntMap.get(seq2.get(b2), 0));
      } 
    } 
    if (f < paramFloat * 0.6F)
      stringBuilder.append(", ").append(LunariansI18n.get("loss_other")); 
    return stringBuilder.toString();
  }
  
  private static float amountPerCraft(Block paramBlock, Item paramItem) {
    if (paramBlock.consumers == null)
      return 0.0F; 
    for (Consume consume : paramBlock.consumers) {
      if (consume instanceof ConsumeItems) {
        ItemStack[] arrayOfItemStack;
        int i;
        byte b;
        for (arrayOfItemStack = ((ConsumeItems)consume).items, i = arrayOfItemStack.length, b = 0; b < i; ) {
          ItemStack itemStack = arrayOfItemStack[b];
          if (itemStack.item != paramItem) {
            b++;
            continue;
          } 
          return itemStack.amount;
        } 
      } 
    } 
    return 0.0F;
  }
  
  private static String fmt(float paramFloat) {
    return String.valueOf(Math.round(paramFloat * 10.0F) / 10.0F);
  }
}
