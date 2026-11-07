/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Events
 *  arc.func.Cons
 *  arc.graphics.Color
 *  arc.graphics.g2d.Draw
 *  arc.graphics.g2d.Fill
 *  arc.graphics.g2d.Lines
 *  arc.math.geom.Vec2
 *  arc.struct.IntSeq
 *  arc.struct.IntSet
 *  arc.struct.Seq
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.ai.types.CommandAI
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.gen.Call
 *  mindustry.gen.Groups
 *  mindustry.gen.Player
 *  mindustry.gen.Unit
 */
package lunarians;

import arc.Events;
import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.geom.Vec2;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import java.util.Comparator;
import lunarians.AutoUnitHeal;
import lunarians.LunariansSettings;
import lunarians.PathSafe;
import lunarians.data.DefenceSlot;
import mindustry.Vars;
import mindustry.ai.types.CommandAI;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;

public class AutoDefence {
    private static final float SCAN = 1.2f;
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
                AutoDefence.update();
            }
        });
        Events.run((Object)EventType.Trigger.draw, (Runnable)new Runnable(){

            @Override
            public void run() {
                AutoDefence.draw();
            }
        });
        Events.on(EventType.WorldLoadEvent.class, (Cons)new Cons<EventType.WorldLoadEvent>(){

            public void get(EventType.WorldLoadEvent worldLoadEvent) {
                AutoDefence.reset();
            }
        });
    }

    public static void reset() {
        timer = 0.0f;
        count = 0;
        claimed.clear();
    }

    public static int count() {
        return count;
    }

    private static void update() {
        block4: {
            if (!LunariansSettings.enableAutoDefence || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
                return;
            }
            if ((timer += Time.delta / 60.0f) < LunariansSettings.actionInterval(1.2f)) {
                return;
            }
            timer = 0.0f;
            try {
                AutoDefence.scan();
            }
            catch (Throwable throwable) {
                if (errorLogged) break block4;
                errorLogged = true;
                Log.err((String)"[Lunarians] AutoDefence error", (Throwable)throwable);
            }
        }
    }

    private static void scan() {
        Seq<DefenceSlot> seq = LunariansSettings.defenceSlots;
        if (seq.isEmpty()) {
            return;
        }
        count = 0;
        claimed.clear();
        for (int i = 0; i < seq.size; ++i) {
            float f;
            int n;
            DefenceSlot defenceSlot = (DefenceSlot)seq.get(i);
            if (!defenceSlot.ready()) continue;
            final float f2 = (float)defenceSlot.x * 8.0f + 4.0f;
            final float f3 = (float)defenceSlot.y * 8.0f + 4.0f;
            float f4 = defenceSlot.radius * 8.0f;
            int n2 = defenceSlot.maxUnits > 0 ? defenceSlot.maxUnits : Integer.MAX_VALUE;
            Seq seq2 = new Seq();
            int n3 = Groups.unit.size();
            for (int j = 0; j < n3; ++j) {
                Unit unit = (Unit)Groups.unit.index(j);
                if (unit == null || !unit.isValid() || unit.team != Vars.player.team() || !defenceSlot.unit.equals(unit.type.name) || !(unit.controller() instanceof CommandAI) || claimed.contains(unit.id) || AutoUnitHeal.isActiveHealer(unit.id) || AutoUnitHeal.isHealerType(unit.type)) continue;
                seq2.add((Object)unit);
            }
            seq2.sort((Comparator)new Comparator<Unit>(){

                @Override
                public int compare(Unit unit, Unit unit2) {
                    float f = unit.dst(f2, f3);
                    float f22 = unit2.dst(f2, f3);
                    return Float.compare(f, f22);
                }
            });
            Seq seq3 = new Seq();
            for (int j = 0; j < seq2.size && seq3.size < n2; ++j) {
                Unit unit = (Unit)seq2.get(j);
                seq3.add((Object)unit);
                claimed.add(unit.id);
            }
            if (seq3.isEmpty()) continue;
            count += seq3.size;
            Unit unit = null;
            float f5 = Float.MAX_VALUE;
            for (n = 0; n < n3; ++n) {
                Unit unit2 = (Unit)Groups.unit.index(n);
                if (unit2 == null || !unit2.isValid() || unit2.team == Vars.player.team() || !((f = unit2.dst(f2, f3)) <= f4) || !(f < f5)) continue;
                f5 = f;
                unit = unit2;
            }
            if (unit != null) {
                IntSeq intSeq = new IntSeq();
                for (int j = 0; j < seq3.size; ++j) {
                    intSeq.add(((Unit)seq3.get((int)j)).id);
                }
                Call.commandUnits((Player)Vars.player, (int[])intSeq.toArray(), null, unit, null, (boolean)false, (boolean)false);
                continue;
            }
            for (n = 0; n < seq3.size; ++n) {
                float f6 = seq3.size <= 1 ? 0.0f : (float)n * 360.0f / (float)seq3.size * ((float)Math.PI / 180);
                f = (float)Math.cos(f6) * 10.0f;
                float f7 = (float)Math.sin(f6) * 10.0f;
                Unit unit3 = (Unit)seq3.get(n);
                Vec2 vec2 = PathSafe.safeNear(unit3, f2 + f, f3 + f7, 64.0f);
                Call.commandUnits((Player)Vars.player, (int[])new int[]{unit3.id}, null, null, (Vec2)vec2, (boolean)false, (boolean)false);
            }
        }
    }

    private static void draw() {
        if (!LunariansSettings.enableAutoDefence || Vars.state == null || !Vars.state.isGame()) {
            return;
        }
        Seq<DefenceSlot> seq = LunariansSettings.defenceSlots;
        if (seq.isEmpty()) {
            return;
        }
        Draw.z((float)120.0f);
        for (int i = 0; i < seq.size; ++i) {
            DefenceSlot defenceSlot = (DefenceSlot)seq.get(i);
            if (!defenceSlot.ready()) continue;
            float f = (float)defenceSlot.x * 8.0f + 4.0f;
            float f2 = (float)defenceSlot.y * 8.0f + 4.0f;
            float f3 = defenceSlot.radius * 8.0f;
            Draw.color((Color)Color.scarlet.cpy().a(0.08f));
            Fill.circle((float)f, (float)f2, (float)f3);
            Draw.color((Color)Color.scarlet.cpy().a(0.55f));
            Lines.stroke((float)1.4f);
            Lines.circle((float)f, (float)f2, (float)f3);
            Draw.color((Color)Color.orange.cpy().a(0.8f));
            Fill.circle((float)f, (float)f2, (float)3.0f);
        }
        Draw.reset();
    }

    static {
        claimed = new IntSet();
    }
}

