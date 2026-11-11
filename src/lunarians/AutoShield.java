/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Events
 *  arc.func.Cons
 *  arc.math.geom.Vec2
 *  arc.struct.IntSeq
 *  arc.struct.IntSet
 *  arc.struct.Seq
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.ai.UnitCommand
 *  mindustry.ai.types.CommandAI
 *  mindustry.entities.abilities.Ability
 *  mindustry.entities.abilities.ForceFieldAbility
 *  mindustry.entities.abilities.ShieldArcAbility
 *  mindustry.entities.abilities.ShieldRegenFieldAbility
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
import arc.math.geom.Vec2;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.LunariansSettings;
import lunarians.PathSafe;
import lunarians.data.ShieldSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.entities.abilities.Ability;
import mindustry.entities.abilities.ForceFieldAbility;
import mindustry.entities.abilities.ShieldArcAbility;
import mindustry.entities.abilities.ShieldRegenFieldAbility;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class AutoShield {
    private static final float SCAN = 0.6f;
    private static float timer;
    private static int count;
    private static boolean errorLogged;
    private static final IntSet claimed;

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run((Object)EventType.Trigger.update, (Runnable)new Runnable(){

            @Override
            public void run() {
                AutoShield.update();
            }
        });
        Events.on(EventType.WorldLoadEvent.class, (Cons)new Cons<EventType.WorldLoadEvent>(){

            public void get(EventType.WorldLoadEvent worldLoadEvent) {
                AutoShield.reset();
            }
        });
    }

    public static void reset() {
        timer = 0.0f;
        count = 0;
    }

    public static int count() {
        return count;
    }

    public static boolean hasShieldAbility(UnitType unitType) {
        if (unitType == null || unitType.abilities == null) {
            return false;
        }
        for (int i = 0; i < unitType.abilities.size; ++i) {
            Ability ability = (Ability)unitType.abilities.get(i);
            if (ability instanceof ForceFieldAbility) {
                return true;
            }
            if (ability instanceof ShieldArcAbility) {
                return true;
            }
            if (ability instanceof ShieldRegenFieldAbility) {
                return true;
            }
            if (!ability.getClass().getSimpleName().toLowerCase().contains("shield")) continue;
            return true;
        }
        return false;
    }

    private static void update() {
        block4: {
            if (!LunariansSettings.enableAutoShield || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
                return;
            }
            if ((timer += Time.delta / 60.0f) < LunariansSettings.actionInterval(0.6f)) {
                return;
            }
            timer = 0.0f;
            try {
                AutoShield.scan();
            }
            catch (Throwable throwable) {
                if (errorLogged) break block4;
                errorLogged = true;
                Log.err((String)"[Lunarians] AutoShield error", (Throwable)throwable);
            }
        }
    }

    private static void scan() {
        Seq<ShieldSlot> seq = LunariansSettings.shieldSlots;
        if (seq.isEmpty()) {
            return;
        }
        count = 0;
        claimed.clear();
        int n = Groups.unit.size();
        for (int i = 0; i < seq.size; ++i) {
            Unit unit;
            int n2;
            Unit unit2;
            ShieldSlot shieldSlot = (ShieldSlot)seq.get(i);
            if (!shieldSlot.ready() || (unit2 = (Unit)Groups.unit.getByID(shieldSlot.followId)) == null || !unit2.isValid()) continue;
            shieldSlot.followLabel = unit2.type.localizedName;
            Seq seq2 = new Seq();
            IntSeq intSeq = new IntSeq();
            for (n2 = 0; n2 < n; ++n2) {
                unit = (Unit)Groups.unit.index(n2);
                if (unit == null || !unit.isValid() || unit.team != Vars.player.team() || !shieldSlot.unit.equals(unit.type.name) || !(unit.controller() instanceof CommandAI) || unit.id == unit2.id) continue;
                seq2.add((Object)unit);
                CommandAI commandAI = (CommandAI)unit.controller();
                if (commandAI.command == UnitCommand.moveCommand) continue;
                intSeq.add(unit.id);
            }
            if (shieldSlot.maxUnits > 0 && seq2.size > shieldSlot.maxUnits) {
                seq2.truncate(shieldSlot.maxUnits);
            }
            for (n2 = seq2.size - 1; n2 >= 0; --n2) {
                if (!claimed.contains(((Unit)seq2.get((int)n2)).id)) continue;
                seq2.remove(n2);
            }
            if (seq2.isEmpty()) continue;
            for (n2 = 0; n2 < seq2.size; ++n2) {
                claimed.add(((Unit)seq2.get((int)n2)).id);
            }
            if (intSeq.size > 0) {
                Call.setUnitCommand((Player)Vars.player, (int[])intSeq.toArray(), (UnitCommand)UnitCommand.moveCommand);
            }
            for (n2 = 0; n2 < seq2.size; ++n2) {
                unit = (Unit)seq2.get(n2);
                float f = seq2.size <= 1 ? 0.0f : (float)n2 * 360.0f / (float)seq2.size * ((float)Math.PI / 180);
                float f2 = (float)Math.cos(f) * 18.0f;
                float f3 = (float)Math.sin(f) * 18.0f;
                Vec2 vec2 = PathSafe.safeNear(unit, unit2.x + f2, unit2.y + f3, 48.0f);
                Call.commandUnits((Player)Vars.player, (int[])new int[]{unit.id}, null, null, (Vec2)vec2, (boolean)false, (boolean)true);
            }
            count += seq2.size;
        }
    }

    static {
        claimed = new IntSet();
    }
}

