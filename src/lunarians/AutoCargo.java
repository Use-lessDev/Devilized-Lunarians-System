/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Events
 *  arc.math.Mathf
 *  arc.math.geom.Vec2
 *  arc.struct.IntIntMap
 *  arc.struct.IntSeq
 *  arc.struct.IntSet
 *  arc.struct.Seq
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.ai.UnitCommand
 *  mindustry.ai.types.CommandAI
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.gen.Call
 *  mindustry.gen.Groups
 *  mindustry.gen.Payloadc
 *  mindustry.gen.Player
 *  mindustry.gen.Unit
 */
package lunarians;

import arc.Events;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.IntIntMap;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.LunariansSettings;
import lunarians.PathSafe;
import lunarians.data.CargoSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Payloadc;
import mindustry.gen.Player;
import mindustry.gen.Unit;

public class AutoCargo {
    private static float timer;
    private static int count;
    private static boolean errorLogged;
    private static final IntSet applied;
    private static final IntIntMap wpIndex;

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run((Object)EventType.Trigger.update, AutoCargo::update);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            timer = 0.0f;
            count = 0;
            applied.clear();
            wpIndex.clear();
        });
    }

    public static int count() {
        return count;
    }

    public static void reset() {
        timer = 0.0f;
        count = 0;
        applied.clear();
        wpIndex.clear();
    }

    private static void update() {
        block4: {
            if (!LunariansSettings.enableAutoCargo || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
                return;
            }
            if ((timer += Time.delta / 60.0f) < LunariansSettings.actionInterval(0.6f)) {
                return;
            }
            timer = 0.0f;
            try {
                AutoCargo.scan();
            }
            catch (Throwable throwable) {
                if (errorLogged) break block4;
                errorLogged = true;
                Log.err((String)"[Lunarians] AutoCargo error", (Throwable)throwable);
            }
        }
    }

    private static void scan() {
        Seq<CargoSlot> seq = LunariansSettings.cargoSlots;
        if (seq == null || seq.isEmpty()) {
            return;
        }
        applied.clear();
        count = 0;
        for (int i = 0; i < seq.size; ++i) {
            int n;
            CargoSlot cargoSlot = (CargoSlot)seq.get(i);
            if (cargoSlot == null || !cargoSlot.ready()) continue;
            Seq seq2 = new Seq();
            int n2 = Groups.unit.size();
            for (n = 0; n < n2; ++n) {
                Unit unit = (Unit)Groups.unit.index(n);
                if (unit == null || !unit.isValid() || unit.team != Vars.player.team() || applied.contains(unit.id) || !(unit instanceof Payloadc) || !(unit.controller() instanceof CommandAI) || unit.type == null || !cargoSlot.unit.equals(unit.type.name)) continue;
                seq2.add((Object)unit);
            }
            if (cargoSlot.maxUnits > 0 && seq2.size > cargoSlot.maxUnits) {
                seq2.truncate(cargoSlot.maxUnits);
            }
            for (n = 0; n < seq2.size; ++n) {
                applied.add(((Unit)seq2.get((int)n)).id);
            }
            if (seq2.isEmpty()) continue;
            AutoCargo.apply(cargoSlot, (Seq<Unit>)seq2);
            count += seq2.size;
        }
    }

    private static void apply(CargoSlot cargoSlot, Seq<Unit> seq) {
        int n;
        IntSeq intSeq = new IntSeq();
        for (n = 0; n < seq.size; ++n) {
            Unit unit = (Unit)seq.get(n);
            CommandAI commandAI = (CommandAI)unit.controller();
            try {
                if (UnitCommand.loopPayloadCommand == null || commandAI.command == UnitCommand.loopPayloadCommand) continue;
                intSeq.add(unit.id);
                continue;
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        if (intSeq.size > 0) {
            try {
                Call.setUnitCommand((Player)Vars.player, (int[])intSeq.toArray(), (UnitCommand)UnitCommand.loopPayloadCommand);
            }
            catch (Throwable throwable) {
                try {
                    Call.setUnitCommand((Player)Vars.player, (int[])intSeq.toArray(), (UnitCommand)UnitCommand.moveCommand);
                }
                catch (Throwable throwable2) {
                    // empty catch block
                }
            }
        }
        if ((n = cargoSlot.pointCount()) < 2) {
            return;
        }
        int n2 = seq.size;
        for (int i = 0; i < n2; ++i) {
            Unit unit = (Unit)seq.get(i);
            int n3 = wpIndex.get(unit.id, 0);
            if (n3 < 0 || n3 >= n) {
                n3 = 0;
            }
            float f = n2 <= 1 ? 0.0f : (float)i * 360.0f / (float)n2 * ((float)Math.PI / 180);
            float f2 = Mathf.cos((float)f) * 14.0f + (float)(unit.id % 7) * 1.5f;
            float f3 = Mathf.sin((float)f) * 14.0f + (float)(unit.id % 5) * 1.2f;
            float f4 = (float)cargoSlot.px(n3) * 8.0f + 4.0f + f2;
            float f5 = (float)cargoSlot.py(n3) * 8.0f + 4.0f + f3;
            Vec2 vec2 = PathSafe.safeNear(unit, f4, f5, 48.0f);
            float f6 = 36.0f;
            if (unit.dst(vec2.x, vec2.y) <= f6) {
                n3 = (n3 + 1) % n;
                wpIndex.put(unit.id, n3);
                f4 = (float)cargoSlot.px(n3) * 8.0f + 4.0f + f2;
                f5 = (float)cargoSlot.py(n3) * 8.0f + 4.0f + f3;
                vec2 = PathSafe.safeNear(unit, f4, f5, 48.0f);
            } else {
                wpIndex.put(unit.id, n3);
            }
            Call.commandUnits((Player)Vars.player, (int[])new int[]{unit.id}, null, null, (Vec2)vec2, (boolean)false, (boolean)true);
        }
    }

    static {
        applied = new IntSet();
        wpIndex = new IntIntMap();
    }
}

