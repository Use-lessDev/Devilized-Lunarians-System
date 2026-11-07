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
import mindustry.world.consumers.ConsumeItems;
import mindustry.world.modules.ItemModule;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ResourceLossWarn.class */
public class ResourceLossWarn {
    public static final int PANELS = 4;
    public static final int MAX_SLOTS = 25;
    private static final float SAMPLE = 0.5f;
    private static final float SMOOTH = 0.35f;
    private static final int SUSTAIN = 4;
    private static final float COOLDOWN = 30.0f;
    private static final ObjectFloatMap<Item> acc = new ObjectFloatMap<>();
    private static final ObjectIntMap<Item> last = new ObjectIntMap<>();
    private static final ObjectFloatMap<Item> ema = new ObjectFloatMap<>();
    private static final ObjectIntMap<Item> streak = new ObjectIntMap<>();
    private static final ObjectFloatMap<Item> lastWarn = new ObjectFloatMap<>();
    private static float clock;
    private static float timer;
    private static boolean errorLogged;

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run(EventType.Trigger.update, new Runnable() { // from class: lunarians.ResourceLossWarn.1
            @Override // java.lang.Runnable
            public void run() {
                ResourceLossWarn.update();
            }
        });
        Events.on(EventType.WorldLoadEvent.class, new Cons<EventType.WorldLoadEvent>() { // from class: lunarians.ResourceLossWarn.2
            public void get(EventType.WorldLoadEvent worldLoadEvent) {
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
        timer = 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void update() {
        if (!LunariansSettings.enableLossWarn || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
            return;
        }
        float f = Time.delta / 60.0f;
        if (f <= 0.0f) {
            return;
        }
        clock += f;
        float f2 = timer + f;
        timer = f2;
        if (f2 < SAMPLE) {
            return;
        }
        float f3 = timer;
        timer = 0.0f;
        try {
            sample(f3);
        } catch (Throwable th) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err("[Lunarians] ResourceLossWarn error", th);
            }
        }
    }

    private static void sample(float f) {
        Item item;
        Team team = Vars.player.team();
        if (team == null || team.core() == null) {
            return;
        }
        ItemModule items = team.items();
        Seq<LossSlot> lossSlots = LunariansSettings.lossSlots(LunariansSettings.lossPanel);
        for (int i = 0; i < lossSlots.size; i++) {
            LossSlot lossSlot = (LossSlot) lossSlots.get(i);
            if (lossSlot != null && !lossSlot.item.isEmpty() && (item = Vars.content.item(lossSlot.item)) != null) {
                float f2 = acc.get(item, 0.0f) + f;
                if (f2 < Math.max(1.0f, lossSlot.delay) - 0.01f) {
                    acc.put(item, f2);
                } else {
                    acc.put(item, 0.0f);
                    int i2 = items.get(item);
                    if (last.containsKey(item)) {
                        float f3 = (last.get(item, i2) - i2) / f2;
                        last.put(item, i2);
                        float min = Math.min(1.0f, SMOOTH * Math.max(1.0f, lossSlot.delay));
                        float f4 = (ema.get(item, 0.0f) * (1.0f - min)) + (f3 * min);
                        ema.put(item, f4);
                        if (f4 >= lossSlot.perSec) {
                            int i3 = streak.get(item, 0) + 1;
                            streak.put(item, i3);
                            if (i3 >= Math.max(1, Math.round(4.0f / Math.max(1.0f, lossSlot.delay))) && clock - lastWarn.get(item, -999.0f) > COOLDOWN) {
                                lastWarn.put(item, clock);
                                warn(team, item, f4);
                            }
                        } else {
                            streak.put(item, 0);
                        }
                    } else {
                        last.put(item, i2);
                        ema.put(item, 0.0f);
                    }
                }
            }
        }
    }

    private static void warn(Team team, Item item, float f) {
        String str = LunariansI18n.get("loss_warn", fmt(f), item.localizedName) + " " + item.emoji();
        String consumers = consumers(team, item, f);
        if (!consumers.isEmpty()) {
            str = str + LunariansI18n.get("loss_by", consumers);
        }
        try {
            Vars.ui.chatfrag.addMessage(str);
        } catch (Throwable th) {
        }
    }

    private static String consumers(Team team, Item item, float f) {
        ObjectIntMap objectIntMap = new ObjectIntMap();
        final ObjectFloatMap objectFloatMap = new ObjectFloatMap();
        Seq seq = team.data().buildings;
        for (int i = 0; i < seq.size; i++) {
            Object obj = seq.get(i);
            if (obj != null && ((Building) obj).isValid() && (((Building) obj).block instanceof GenericCrafter) && ((Building) obj).efficiency > 0.0f && ((Building) obj).shouldConsume()) {
                float amountPerCraft = amountPerCraft(((Building) obj).block, item);
                if (amountPerCraft > 0.0f) {
                    float f2 = (amountPerCraft / (((Building) obj).block.craftTime / 60.0f)) * ((Building) obj).efficiency;
                    objectIntMap.increment(((Building) obj).block, 0, 1);
                    objectFloatMap.put(((Building) obj).block, objectFloatMap.get(((Building) obj).block, 0.0f) + f2);
                }
            }
        }
        if (objectIntMap.size == 0) {
            return LunariansI18n.get("loss_other");
        }
        Seq seq2 = new Seq();
        ObjectIntMap.Keys it = objectIntMap.keys().iterator();
        while (it.hasNext()) {
            seq2.add((Block) it.next());
        }
        seq2.sort(new Comparator<Block>() { // from class: lunarians.ResourceLossWarn.3
            @Override // java.util.Comparator
            public int compare(Block block, Block block2) {
                return Float.compare(objectFloatMap.get(block2, 0.0f), objectFloatMap.get(block, 0.0f));
            }
        });
        StringBuilder sb = new StringBuilder();
        float f3 = 0.0f;
        for (int i2 = 0; i2 < seq2.size; i2++) {
            f3 += objectFloatMap.get((Block) seq2.get(i2), 0.0f);
            if (i2 < 2) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(((Block) seq2.get(i2)).localizedName).append(" x").append(objectIntMap.get((Block) seq2.get(i2), 0));
            }
        }
        if (f3 < f * 0.6f) {
            sb.append(", ").append(LunariansI18n.get("loss_other"));
        }
        return sb.toString();
    }

    private static float amountPerCraft(Block block, Item item) {
        if (block.consumers == null) {
            return 0.0f;
        }
        for (ConsumeItems consumeItems : block.consumers) {
            if (consumeItems instanceof ConsumeItems) {
                for (ItemStack itemStack : consumeItems.items) {
                    if (itemStack.item == item) {
                        return r0.amount;
                    }
                }
            }
        }
        return 0.0f;
    }

    private static String fmt(float f) {
        return String.valueOf(Math.round(f * 10.0f) / 10.0f);
    }
}
