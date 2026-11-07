package lunarians;

import arc.Events;
import arc.func.Cons;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.IntIntMap;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.data.CargoSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Payloadc;
import mindustry.gen.Unit;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoCargo.class */
public class AutoCargo {
    private static float timer;
    private static int count;
    private static boolean errorLogged;
    private static final IntSet applied = new IntSet();
    private static final IntIntMap wpIndex = new IntIntMap();

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoCargo$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoCargo.$r8$lambda$MgGZZ94itJQEVU-OHY5pcukBJ6M();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoCargo$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements Cons {
        public final void get(Object obj) {
            AutoCargo.lambda$init$0((EventType.WorldLoadEvent) obj);
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* renamed from: lunarians.AutoCargo$1, reason: invalid class name */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoCargo$1.class */
    class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            AutoCargo.update();
        }
    }

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run(EventType.Trigger.update, AutoCargo::update);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            timer = 0.0f;
            count = 0;
            applied.clear();
            wpIndex.clear();
        });
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* renamed from: lunarians.AutoCargo$2, reason: invalid class name */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoCargo$2.class */
    class AnonymousClass2 implements Cons<EventType.WorldLoadEvent> {
        AnonymousClass2() {
        }

        public void get(EventType.WorldLoadEvent worldLoadEvent) {
            AutoCargo.reset();
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoCargo$State.class */
    private static final class State {
        int slot;
        int phase;
        float since;
        int lastPayloads;
        float lastChange;

        private State() {
        }

        /* synthetic */ State(AnonymousClass1 anonymousClass1) {
            this();
        }
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
        if (!LunariansSettings.enableAutoCargo || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
            return;
        }
        timer += Time.delta / 60.0f;
        if (timer < LunariansSettings.actionInterval(0.6f)) {
            return;
        }
        timer = 0.0f;
        try {
            scan();
        } catch (Throwable th) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err("[Lunarians] AutoCargo error", th);
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
        for (int i = 0; i < seq.size; i++) {
            CargoSlot cargoSlot = (CargoSlot) seq.get(i);
            if (cargoSlot != null && cargoSlot.ready()) {
                Seq seq2 = new Seq();
                int size = Groups.unit.size();
                for (int i2 = 0; i2 < size; i2++) {
                    Unit index = Groups.unit.index(i2);
                    if (index != null && index.isValid() && index.team == Vars.player.team() && !applied.contains(index.id) && (index instanceof Payloadc) && (index.controller() instanceof CommandAI) && index.type != null && cargoSlot.unit.equals(index.type.name)) {
                        seq2.add(index);
                    }
                }
                if (cargoSlot.maxUnits > 0 && seq2.size > cargoSlot.maxUnits) {
                    seq2.truncate(cargoSlot.maxUnits);
                }
                for (int i3 = 0; i3 < seq2.size; i3++) {
                    applied.add(((Unit) seq2.get(i3)).id);
                }
                if (!seq2.isEmpty()) {
                    apply(cargoSlot, seq2);
                    count += seq2.size;
                }
            }
        }
    }

    private static void apply(CargoSlot cargoSlot, Seq<Unit> seq) {
        IntSeq intSeq = new IntSeq();
        for (int i = 0; i < seq.size; i++) {
            Unit unit = (Unit) seq.get(i);
            CommandAI controller = unit.controller();
            try {
                if (UnitCommand.loopPayloadCommand != null && controller.command != UnitCommand.loopPayloadCommand) {
                    intSeq.add(unit.id);
                }
            } catch (Throwable th) {
            }
        }
        if (intSeq.size > 0) {
            try {
                Call.setUnitCommand(Vars.player, intSeq.toArray(), UnitCommand.loopPayloadCommand);
            } catch (Throwable th2) {
                try {
                    Call.setUnitCommand(Vars.player, intSeq.toArray(), UnitCommand.moveCommand);
                } catch (Throwable th3) {
                }
            }
        }
        int pointCount = cargoSlot.pointCount();
        if (pointCount < 2) {
            return;
        }
        int i2 = seq.size;
        for (int i3 = 0; i3 < i2; i3++) {
            Unit unit2 = (Unit) seq.get(i3);
            int i4 = wpIndex.get(unit2.id, 0);
            if (i4 < 0 || i4 >= pointCount) {
                i4 = 0;
            }
            float f = i2 <= 1 ? 0.0f : ((i3 * 360.0f) / i2) * 0.017453292f;
            float cos = (Mathf.cos(f) * 14.0f) + ((unit2.id % 7) * 1.5f);
            float sin = (Mathf.sin(f) * 14.0f) + ((unit2.id % 5) * 1.2f);
            Vec2 safeNear = PathSafe.safeNear(unit2, (cargoSlot.px(i4) * 8.0f) + 4.0f + cos, (cargoSlot.py(i4) * 8.0f) + 4.0f + sin, 48.0f);
            if (unit2.dst(safeNear.x, safeNear.y) <= 36.0f) {
                wpIndex.put(unit2.id, (i4 + 1) % pointCount);
                safeNear = PathSafe.safeNear(unit2, (cargoSlot.px(r0) * 8.0f) + 4.0f + cos, (cargoSlot.py(r0) * 8.0f) + 4.0f + sin, 48.0f);
            } else {
                wpIndex.put(unit2.id, i4);
            }
            Call.commandUnits(Vars.player, new int[]{unit2.id}, (Building) null, (Unit) null, safeNear, false, true);
        }
    }
}
