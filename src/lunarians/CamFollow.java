package lunarians;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.IntSeq;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.entities.Units;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/CamFollow.class */
public class CamFollow {
    private static final float INTERVAL = 0.4f;
    private static final float AIM_RADIUS_TILES = 5.0f;
    private static final float MOVE_REISSUE = 16.0f;
    private static final float TIME_REISSUE = 5.0f;
    private static float clock;
    private static float timer;
    private static boolean errorLogged;
    private static boolean lastWasAttack;
    private static boolean hasLast;
    private static long selectedHash;
    private static final IntSeq selected = new IntSeq();
    private static final IntSeq tmpIds = new IntSeq();
    private static int lastSig = Integer.MIN_VALUE;
    private static int lastTargetId = -1;
    private static final Vec2 lastPos = new Vec2();
    private static float lastTime = -999.0f;

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run(EventType.Trigger.update, new Runnable() { // from class: lunarians.CamFollow.1
            @Override // java.lang.Runnable
            public void run() {
                CamFollow.update();
            }
        });
        Events.on(EventType.WorldLoadEvent.class, new Cons<EventType.WorldLoadEvent>() { // from class: lunarians.CamFollow.2
            public void get(EventType.WorldLoadEvent worldLoadEvent) {
                CamFollow.selected.clear();
                long unused = CamFollow.selectedHash = 0L;
                CamFollow.resetOrder();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void resetOrder() {
        hasLast = false;
        lastSig = Integer.MIN_VALUE;
        lastTargetId = -1;
    }

    public static void setEnabled(boolean z) {
        LunariansSettings.enableCamFollow = z;
        LunariansSettings.save();
        resetOrder();
        if (z) {
            captureSelection();
        } else {
            selected.clear();
            selectedHash = 0L;
        }
    }

    public static int count() {
        return selected.size;
    }

    private static void captureSelection() {
        Seq seq;
        if (Vars.control == null || Vars.control.input == null || (seq = Vars.control.input.selectedUnits) == null || seq.isEmpty()) {
            return;
        }
        long j = 0;
        int i = 0;
        for (int i2 = 0; i2 < seq.size; i2++) {
            Unit unit = (Unit) seq.get(i2);
            if (unit != null && unit.isValid() && unit.isCommandable() && !unit.isPlayer()) {
                j += (unit.id + 1) * 2654435761L;
                i++;
            }
        }
        if (i == 0 || j == selectedHash) {
            return;
        }
        selectedHash = j;
        selected.clear();
        for (int i3 = 0; i3 < seq.size; i3++) {
            Unit unit2 = (Unit) seq.get(i3);
            if (unit2 != null && unit2.isValid() && unit2.isCommandable() && !unit2.isPlayer()) {
                selected.add(unit2.id);
            }
        }
        resetOrder();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void update() {
        if (!LunariansSettings.enableCamFollow || Vars.state == null || !Vars.state.isGame() || Vars.state.isPaused() || Vars.player == null) {
            return;
        }
        clock += Time.delta / 60.0f;
        captureSelection();
        timer += Time.delta / 60.0f;
        if (timer < INTERVAL) {
            return;
        }
        timer = 0.0f;
        try {
            run();
        } catch (Throwable th) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err("[Lunarians] CamFollow error", th);
            }
        }
    }

    private static void run() {
        Unit closestTarget;
        IntSeq intSeq = tmpIds;
        intSeq.clear();
        for (int i = 0; i < selected.size; i++) {
            Unit byID = Groups.unit.getByID(selected.get(i));
            if (byID != null && byID.isValid() && byID.team == Vars.player.team() && byID.isCommandable()) {
                intSeq.add(byID.id);
            }
        }
        if (intSeq.size != selected.size) {
            selected.clear();
            selected.addAll(intSeq);
        }
        if (intSeq.size == 0) {
            return;
        }
        int signature = signature(intSeq);
        int[] array = intSeq.toArray();
        Unit unit = Vars.player.dead() ? null : Vars.player.unit();
        if (unit != null && unit.isValid() && unit.isShooting() && (closestTarget = Units.closestTarget(Vars.player.team(), unit.aimX(), unit.aimY(), 40.0f)) != null) {
            int pos = closestTarget instanceof Unit ? closestTarget.id : ((Building) closestTarget).pos();
            if (!(hasLast && lastWasAttack && lastTargetId == pos && lastSig == signature) || clock - lastTime > 2.0f) {
                Call.commandUnits(Vars.player, array, closestTarget instanceof Building ? (Building) closestTarget : null, closestTarget instanceof Unit ? closestTarget : null, (Vec2) null, false, true);
                hasLast = true;
                lastWasAttack = true;
                lastTargetId = pos;
                lastSig = signature;
                lastTime = clock;
                return;
            }
            return;
        }
        float clamp = Mathf.clamp(Core.camera.position.x, 0.0f, Vars.world.unitWidth());
        float clamp2 = Mathf.clamp(Core.camera.position.y, 0.0f, Vars.world.unitHeight());
        if (!hasLast || lastWasAttack || lastSig != signature || lastPos.dst(clamp, clamp2) > MOVE_REISSUE || clock - lastTime > 5.0f) {
            Call.commandUnits(Vars.player, array, (Building) null, (Unit) null, new Vec2(clamp, clamp2), false, true);
            hasLast = true;
            lastWasAttack = false;
            lastTargetId = -1;
            lastSig = signature;
            lastPos.set(clamp, clamp2);
            lastTime = clock;
        }
    }

    private static int signature(IntSeq intSeq) {
        int i = intSeq.size;
        for (int i2 = 0; i2 < intSeq.size; i2++) {
            i = (i * 31) + intSeq.get(i2);
        }
        return i;
    }
}
