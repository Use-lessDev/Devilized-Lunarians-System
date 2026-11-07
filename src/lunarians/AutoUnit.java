/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Events
 *  arc.func.Cons
 *  arc.struct.IntSeq
 *  arc.struct.IntSet
 *  arc.struct.Seq
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.ai.ItemUnitStance
 *  mindustry.ai.UnitCommand
 *  mindustry.ai.UnitStance
 *  mindustry.ai.types.CommandAI
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.gen.Call
 *  mindustry.gen.Groups
 *  mindustry.gen.Player
 *  mindustry.gen.Unit
 *  mindustry.type.UnitType
 */
package lunarians;

import arc.Events;
import arc.func.Cons;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.LunariansSettings;
import lunarians.data.UnitSlot;
import mindustry.Vars;
import mindustry.ai.ItemUnitStance;
import mindustry.ai.UnitCommand;
import mindustry.ai.UnitStance;
import mindustry.ai.types.CommandAI;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

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
        Events.run((Object)EventType.Trigger.update, (Runnable)new Runnable(){

            @Override
            public void run() {
                AutoUnit.update();
            }
        });
        Events.on(EventType.WorldLoadEvent.class, (Cons)new Cons<EventType.WorldLoadEvent>(){

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
        block4: {
            if (!LunariansSettings.enableAutoUnit || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
                return;
            }
            if ((timer += Time.delta / 60.0f) < LunariansSettings.actionInterval(1.5f)) {
                return;
            }
            timer = 0.0f;
            try {
                AutoUnit.scan();
            }
            catch (Throwable throwable) {
                if (errorLogged) break block4;
                errorLogged = true;
                Log.err((String)"[Lunarians] AutoUnit error", (Throwable)throwable);
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
        for (int i = 0; i < seq.size; ++i) {
            int n;
            UnitSlot unitSlot = (UnitSlot)seq.get(i);
            if (!unitSlot.ready()) continue;
            Seq seq2 = new Seq();
            int n2 = Groups.unit.size();
            for (n = 0; n < n2; ++n) {
                Unit unit = (Unit)Groups.unit.index(n);
                if (unit == null || !unit.isValid() || unit.team != Vars.player.team() || claimed.contains(unit.id) || !(unit.controller() instanceof CommandAI) || !unitSlot.unit.equals(unit.type.name)) continue;
                seq2.add((Object)unit);
            }
            if (unitSlot.maxUnits > 0 && seq2.size > unitSlot.maxUnits) {
                seq2.truncate(unitSlot.maxUnits);
            }
            for (n = 0; n < seq2.size; ++n) {
                claimed.add(((Unit)seq2.get((int)n)).id);
            }
            if (seq2.isEmpty()) continue;
            AutoUnit.apply(unitSlot, (Seq<Unit>)seq2);
            count += seq2.size;
        }
    }

    private static void apply(UnitSlot unitSlot, Seq<Unit> seq) {
        CommandAI commandAI;
        UnitType unitType = ((Unit)seq.first()).type;
        UnitCommand unitCommand = null;
        if (!unitSlot.command.isEmpty() && unitType.commands != null) {
            for (int i = 0; i < unitType.commands.size; ++i) {
                UnitCommand unitCommand2 = (UnitCommand)unitType.commands.get(i);
                if (!unitCommand2.name.equals(unitSlot.command)) continue;
                unitCommand = unitCommand2;
                break;
            }
        }
        if (unitCommand != null) {
            IntSeq intSeq = new IntSeq();
            for (int i = 0; i < seq.size; ++i) {
                commandAI = (CommandAI)((Unit)seq.get(i)).controller();
                if (commandAI.command == unitCommand) continue;
                intSeq.add(((Unit)seq.get((int)i)).id);
            }
            if (intSeq.size > 0) {
                Call.setUnitCommand((Player)Vars.player, (int[])intSeq.toArray(), unitCommand);
            }
        }
        if (unitCommand != null && unitCommand == UnitCommand.mineCommand) {
            if (unitSlot.items.isEmpty()) {
                AutoUnit.stance(seq, UnitStance.mineAuto, true);
            } else {
                Seq seq2 = ItemUnitStance.all();
                for (int i = 0; i < seq2.size; ++i) {
                    commandAI = (ItemUnitStance)seq2.get(i);
                    AutoUnit.stance(seq, (UnitStance)commandAI, unitSlot.items.contains((Object)commandAI.item.name));
                }
            }
        }
    }

    private static void stance(Seq<Unit> seq, UnitStance unitStance, boolean bl) {
        IntSeq intSeq = new IntSeq();
        for (int i = 0; i < seq.size; ++i) {
            CommandAI commandAI = (CommandAI)((Unit)seq.get(i)).controller();
            if (commandAI.hasStance(unitStance) == bl) continue;
            intSeq.add(((Unit)seq.get((int)i)).id);
        }
        if (intSeq.size > 0) {
            Call.setUnitStance((Player)Vars.player, (int[])intSeq.toArray(), (UnitStance)unitStance, (boolean)bl);
        }
    }
}

