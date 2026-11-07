package lunarians;

import arc.Events;
import arc.func.Cons;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.data.UnitSlot;
import mindustry.Vars;
import mindustry.ai.ItemUnitStance;
import mindustry.ai.UnitCommand;
import mindustry.ai.UnitStance;
import mindustry.ai.types.CommandAI;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoUnit.class */
public class AutoUnit {
    private static final float SCAN = 1.5f;
    private static final IntSet claimed = new IntSet();
    private static float timer;
    private static int count;
    private static boolean errorLogged;

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run(EventType.Trigger.update, new Runnable() { // from class: lunarians.AutoUnit.1
            @Override // java.lang.Runnable
            public void run() {
                AutoUnit.update();
            }
        });
        Events.on(EventType.WorldLoadEvent.class, new Cons<EventType.WorldLoadEvent>() { // from class: lunarians.AutoUnit.2
            public void get(EventType.WorldLoadEvent worldLoadEvent) {
                AutoUnit.reset();
            }
        });
    }

    public static void reset() {
        claimed.clear();
        timer = 0.0f;
        count = 0;
    }

    public static int count() {
        return count;
    }

    private static void update() {
        if (!LunariansSettings.enableAutoUnit || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
            return;
        }
        timer += Time.delta / 60.0f;
        if (timer < LunariansSettings.actionInterval(SCAN)) {
            return;
        }
        timer = 0.0f;
        try {
            scan();
        } catch (Throwable th) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err("[Lunarians] AutoUnit error", th);
            }
        }
    }

    private static void scan() {
        Seq<UnitSlot> seq = LunariansSettings.unitSlots;
        if (seq.isEmpty()) {
            return;
        }
        claimed.clear();
        count = 0;
        for (int i = 0; i < seq.size; i++) {
            UnitSlot unitSlot = (UnitSlot) seq.get(i);
            if (unitSlot.ready()) {
                Seq seq2 = new Seq();
                int size = Groups.unit.size();
                for (int i2 = 0; i2 < size; i2++) {
                    Unit index = Groups.unit.index(i2);
                    if (index != null && index.isValid() && index.team == Vars.player.team() && !claimed.contains(index.id) && (index.controller() instanceof CommandAI) && unitSlot.unit.equals(index.type.name)) {
                        seq2.add(index);
                    }
                }
                if (unitSlot.maxUnits > 0 && seq2.size > unitSlot.maxUnits) {
                    seq2.truncate(unitSlot.maxUnits);
                }
                for (int i3 = 0; i3 < seq2.size; i3++) {
                    claimed.add(((Unit) seq2.get(i3)).id);
                }
                if (!seq2.isEmpty()) {
                    apply(unitSlot, seq2);
                    count += seq2.size;
                }
            }
        }
    }

    private static void apply(UnitSlot unitSlot, Seq<Unit> seq) {
        UnitType unitType = ((Unit) seq.first()).type;
        UnitCommand unitCommand = null;
        if (!unitSlot.command.isEmpty() && unitType.commands != null) {
            int i = 0;
            while (true) {
                if (i >= unitType.commands.size) {
                    break;
                }
                UnitCommand unitCommand2 = (UnitCommand) unitType.commands.get(i);
                if (!unitCommand2.name.equals(unitSlot.command)) {
                    i++;
                } else {
                    unitCommand = unitCommand2;
                    break;
                }
            }
        }
        if (unitCommand != null) {
            IntSeq intSeq = new IntSeq();
            for (int i2 = 0; i2 < seq.size; i2++) {
                if (((Unit) seq.get(i2)).controller().command != unitCommand) {
                    intSeq.add(((Unit) seq.get(i2)).id);
                }
            }
            if (intSeq.size > 0) {
                Call.setUnitCommand(Vars.player, intSeq.toArray(), unitCommand);
            }
        }
        if (unitCommand != null && unitCommand == UnitCommand.mineCommand) {
            if (unitSlot.items.isEmpty()) {
                stance(seq, UnitStance.mineAuto, true);
                return;
            }
            Seq all = ItemUnitStance.all();
            for (int i3 = 0; i3 < all.size; i3++) {
                ItemUnitStance itemUnitStance = (ItemUnitStance) all.get(i3);
                stance(seq, itemUnitStance, unitSlot.items.contains(itemUnitStance.item.name));
            }
        }
    }

    private static void stance(Seq<Unit> seq, UnitStance unitStance, boolean z) {
        IntSeq intSeq = new IntSeq();
        for (int i = 0; i < seq.size; i++) {
            if (((Unit) seq.get(i)).controller().hasStance(unitStance) != z) {
                intSeq.add(((Unit) seq.get(i)).id);
            }
        }
        if (intSeq.size > 0) {
            Call.setUnitStance(Vars.player, intSeq.toArray(), unitStance, z);
        }
    }
}
