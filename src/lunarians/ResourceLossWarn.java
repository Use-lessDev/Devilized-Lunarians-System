/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Events
 *  arc.func.Cons
 *  arc.struct.ObjectFloatMap
 *  arc.struct.ObjectIntMap
 *  arc.struct.ObjectIntMap$Keys
 *  arc.struct.Seq
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.game.Team
 *  mindustry.gen.Building
 *  mindustry.type.Item
 *  mindustry.type.ItemStack
 *  mindustry.world.Block
 *  mindustry.world.blocks.production.GenericCrafter
 *  mindustry.world.consumers.Consume
 *  mindustry.world.consumers.ConsumeItems
 *  mindustry.world.modules.ItemModule
 */
package lunarians;

import arc.Events;
import arc.func.Cons;
import arc.struct.ObjectFloatMap;
import arc.struct.ObjectIntMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import java.util.Comparator;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
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
    private static final float SAMPLE = 0.5f;
    private static final float SMOOTH = 0.35f;
    private static final int SUSTAIN = 4;
    private static final float COOLDOWN = 30.0f;
    private static final ObjectFloatMap<Item> acc = new ObjectFloatMap();
    private static final ObjectIntMap<Item> last = new ObjectIntMap();
    private static final ObjectFloatMap<Item> ema = new ObjectFloatMap();
    private static final ObjectIntMap<Item> streak = new ObjectIntMap();
    private static final ObjectFloatMap<Item> lastWarn = new ObjectFloatMap();
    private static float clock;
    private static float timer;
    private static boolean errorLogged;

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run((Object)EventType.Trigger.update, (Runnable)new Runnable(){

            @Override
            public void run() {
                ResourceLossWarn.update();
            }
        });
        Events.on(EventType.WorldLoadEvent.class, (Cons)new Cons<EventType.WorldLoadEvent>(){

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

    private static void update() {
        block5: {
            float f;
            if (!LunariansSettings.enableLossWarn || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
                return;
            }
            float f2 = Time.delta / 60.0f;
            if (f2 <= 0.0f) {
                return;
            }
            clock += f2;
            timer += f2;
            if (f < 0.5f) {
                return;
            }
            float f3 = timer;
            timer = 0.0f;
            try {
                ResourceLossWarn.sample(f3);
            }
            catch (Throwable throwable) {
                if (errorLogged) break block5;
                errorLogged = true;
                Log.err((String)"[Lunarians] ResourceLossWarn error", (Throwable)throwable);
            }
        }
    }

    private static void sample(float f) {
        Team team = Vars.player.team();
        if (team == null || team.core() == null) {
            return;
        }
        ItemModule itemModule = team.items();
        Seq<LossSlot> seq = LunariansSettings.lossSlots(LunariansSettings.lossPanel);
        for (int i = 0; i < seq.size; ++i) {
            Item item;
            LossSlot lossSlot = (LossSlot)seq.get(i);
            if (lossSlot == null || lossSlot.item.isEmpty() || (item = Vars.content.item(lossSlot.item)) == null) continue;
            float f2 = acc.get((Object)item, 0.0f) + f;
            if (f2 < Math.max(1.0f, lossSlot.delay) - 0.01f) {
                acc.put((Object)item, f2);
                continue;
            }
            acc.put((Object)item, 0.0f);
            float f3 = f2;
            int n = itemModule.get(item);
            if (!last.containsKey((Object)item)) {
                last.put((Object)item, n);
                ema.put((Object)item, 0.0f);
                continue;
            }
            float f4 = (float)(last.get((Object)item, n) - n) / f3;
            last.put((Object)item, n);
            float f5 = Math.min(1.0f, 0.35f * Math.max(1.0f, lossSlot.delay));
            float f6 = ema.get((Object)item, 0.0f) * (1.0f - f5) + f4 * f5;
            ema.put((Object)item, f6);
            if (f6 >= lossSlot.perSec) {
                int n2 = streak.get((Object)item, 0) + 1;
                streak.put((Object)item, n2);
                if (n2 < Math.max(1, Math.round(4.0f / Math.max(1.0f, lossSlot.delay))) || !(clock - lastWarn.get((Object)item, -999.0f) > 30.0f)) continue;
                lastWarn.put((Object)item, clock);
                ResourceLossWarn.warn(team, item, f6);
                continue;
            }
            streak.put((Object)item, 0);
        }
    }

    private static void warn(Team team, Item item, float f) {
        String string = LunariansI18n.get("loss_warn", ResourceLossWarn.fmt(f), item.localizedName) + " " + item.emoji();
        String string2 = ResourceLossWarn.consumers(team, item, f);
        if (!string2.isEmpty()) {
            string = string + LunariansI18n.get("loss_by", string2);
        }
        try {
            Vars.ui.chatfrag.addMessage(string);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static String consumers(Team team, Item item, float f) {
        float f2;
        Object object3;
        ObjectIntMap objectIntMap = new ObjectIntMap();
        ObjectFloatMap objectFloatMap = new ObjectFloatMap();
        Seq seq = team.data().buildings;
        for (int i = 0; i < seq.size; ++i) {
            float f3;
            Object object2 = seq.get(i);
            if (object2 == null || !((Building)object2).isValid() || !(((Building)object2).block instanceof GenericCrafter) || ((Building)object2).efficiency <= 0.0f || !((Building)object2).shouldConsume()) continue;
            float f4 = ResourceLossWarn.amountPerCraft(((Building)object2).block, item);
            if (f3 <= 0.0f) continue;
            object3 = (GenericCrafter)((Building)object2).block;
            f2 = f4 / (((GenericCrafter)object3).craftTime / 60.0f) * ((Building)object2).efficiency;
            objectIntMap.increment((Object)((Building)object2).block, 0, 1);
            objectFloatMap.put((Object)((Building)object2).block, objectFloatMap.get((Object)((Building)object2).block, 0.0f) + f2);
        }
        if (objectIntMap.size == 0) {
            return LunariansI18n.get("loss_other");
        }
        Seq seq2 = new Seq();
        for (Object object3 : objectIntMap.keys()) {
            seq2.add(object3);
        }
        ObjectIntMap.Keys keys = objectFloatMap;
        seq2.sort((Comparator)new Comparator<Block>((ObjectFloatMap)keys){
            final /* synthetic */ ObjectFloatMap val$fm;
            {
                this.val$fm = objectFloatMap;
            }

            @Override
            public int compare(Block block, Block block2) {
                return Float.compare(this.val$fm.get((Object)block2, 0.0f), this.val$fm.get((Object)block, 0.0f));
            }
        });
        object3 = new StringBuilder();
        f2 = 0.0f;
        for (int i = 0; i < seq2.size; ++i) {
            f2 += objectFloatMap.get((Object)((Block)seq2.get(i)), 0.0f);
            if (i >= 2) continue;
            if (((StringBuilder)object3).length() > 0) {
                ((StringBuilder)object3).append(", ");
            }
            ((StringBuilder)object3).append(((Block)seq2.get((int)i)).localizedName).append(" x").append(objectIntMap.get((Object)((Block)seq2.get(i)), 0));
        }
        if (f2 < f * 0.6f) {
            ((StringBuilder)object3).append(", ").append(LunariansI18n.get("loss_other"));
        }
        return ((StringBuilder)object3).toString();
    }

    private static float amountPerCraft(Block block, Item item) {
        if (block.consumers == null) {
            return 0.0f;
        }
        for (Consume consume : block.consumers) {
            if (!(consume instanceof ConsumeItems)) continue;
            for (ItemStack itemStack : ((ConsumeItems)consume).items) {
                if (itemStack.item != item) continue;
                return itemStack.amount;
            }
        }
        return 0.0f;
    }

    private static String fmt(float f) {
        return String.valueOf((float)Math.round(f * 10.0f) / 10.0f);
    }
}

