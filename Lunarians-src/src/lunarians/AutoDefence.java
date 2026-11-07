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
import lunarians.data.DefenceSlot;
import mindustry.Vars;
import mindustry.ai.types.CommandAI;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoDefence.class */
public class AutoDefence {
    private static final float SCAN = 1.2f;
    private static float timer;
    private static int count;
    private static boolean errorLogged;
    private static final IntSet claimed = new IntSet();

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run(EventType.Trigger.update, new Runnable() { // from class: lunarians.AutoDefence.1
            @Override // java.lang.Runnable
            public void run() {
                AutoDefence.update();
            }
        });
        Events.run(EventType.Trigger.draw, new Runnable() { // from class: lunarians.AutoDefence.2
            @Override // java.lang.Runnable
            public void run() {
                AutoDefence.draw();
            }
        });
        Events.on(EventType.WorldLoadEvent.class, new Cons<EventType.WorldLoadEvent>() { // from class: lunarians.AutoDefence.3
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
        if (!LunariansSettings.enableAutoDefence || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
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
                Log.err("[Lunarians] AutoDefence error", th);
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
        for (int i = 0; i < seq.size; i++) {
            DefenceSlot defenceSlot = (DefenceSlot) seq.get(i);
            if (defenceSlot.ready()) {
                final float f = (defenceSlot.x * 8.0f) + 4.0f;
                final float f2 = (defenceSlot.y * 8.0f) + 4.0f;
                float f3 = defenceSlot.radius * 8.0f;
                int i2 = defenceSlot.maxUnits > 0 ? defenceSlot.maxUnits : Integer.MAX_VALUE;
                Seq seq2 = new Seq();
                int size = Groups.unit.size();
                for (int i3 = 0; i3 < size; i3++) {
                    Unit index = Groups.unit.index(i3);
                    if (index != null && index.isValid() && index.team == Vars.player.team() && defenceSlot.unit.equals(index.type.name) && (index.controller() instanceof CommandAI) && !claimed.contains(index.id) && !AutoUnitHeal.isActiveHealer(index.id) && !AutoUnitHeal.isHealerType(index.type)) {
                        seq2.add(index);
                    }
                }
                seq2.sort(new Comparator<Unit>() { // from class: lunarians.AutoDefence.4
                    @Override // java.util.Comparator
                    public int compare(Unit unit, Unit unit2) {
                        return Float.compare(unit.dst(f, f2), unit2.dst(f, f2));
                    }
                });
                Seq seq3 = new Seq();
                for (int i4 = 0; i4 < seq2.size && seq3.size < i2; i4++) {
                    Unit unit = (Unit) seq2.get(i4);
                    seq3.add(unit);
                    claimed.add(unit.id);
                }
                if (!seq3.isEmpty()) {
                    count += seq3.size;
                    Unit unit2 = null;
                    float f4 = Float.MAX_VALUE;
                    for (int i5 = 0; i5 < size; i5++) {
                        Unit unit3 = (Unit) Groups.unit.index(i5);
                        if (unit3 != null && unit3.isValid() && unit3.team != Vars.player.team()) {
                            float dst = unit3.dst(f, f2);
                            if (dst <= f3 && dst < f4) {
                                f4 = dst;
                                unit2 = unit3;
                            }
                        }
                    }
                    if (unit2 != null) {
                        IntSeq intSeq = new IntSeq();
                        for (int i6 = 0; i6 < seq3.size; i6++) {
                            intSeq.add(((Unit) seq3.get(i6)).id);
                        }
                        Call.commandUnits(Vars.player, intSeq.toArray(), (Building) null, unit2, (Vec2) null, false, false);
                    } else {
                        for (int i7 = 0; i7 < seq3.size; i7++) {
                            float f5 = seq3.size <= 1 ? 0.0f : ((i7 * 360.0f) / seq3.size) * 0.017453292f;
                            float cos = ((float) Math.cos(f5)) * 10.0f;
                            float sin = ((float) Math.sin(f5)) * 10.0f;
                            Unit unit4 = (Unit) seq3.get(i7);
                            Call.commandUnits(Vars.player, new int[]{unit4.id}, (Building) null, (Unit) null, PathSafe.safeNear(unit4, f + cos, f2 + sin, 64.0f), false, false);
                        }
                    }
                }
            }
        }
    }

    private static void draw() {
        if (LunariansSettings.enableAutoDefence && Vars.state != null && Vars.state.isGame()) {
            Seq<DefenceSlot> seq = LunariansSettings.defenceSlots;
            if (seq.isEmpty()) {
                return;
            }
            Draw.z(120.0f);
            for (int i = 0; i < seq.size; i++) {
                DefenceSlot defenceSlot = (DefenceSlot) seq.get(i);
                if (defenceSlot.ready()) {
                    float f = (defenceSlot.x * 8.0f) + 4.0f;
                    float f2 = (defenceSlot.y * 8.0f) + 4.0f;
                    float f3 = defenceSlot.radius * 8.0f;
                    Draw.color(Color.scarlet.cpy().a(0.08f));
                    Fill.circle(f, f2, f3);
                    Draw.color(Color.scarlet.cpy().a(0.55f));
                    Lines.stroke(1.4f);
                    Lines.circle(f, f2, f3);
                    Draw.color(Color.orange.cpy().a(0.8f));
                    Fill.circle(f, f2, 3.0f);
                }
            }
            Draw.reset();
        }
    }
}
