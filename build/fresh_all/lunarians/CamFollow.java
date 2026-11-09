/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.func.Cons
 *  arc.math.Mathf
 *  arc.math.geom.Vec2
 *  arc.struct.IntSeq
 *  arc.struct.Seq
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.entities.Units
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.game.Team
 *  mindustry.gen.Building
 *  mindustry.gen.Call
 *  mindustry.gen.Groups
 *  mindustry.gen.Player
 *  mindustry.gen.Teamc
 *  mindustry.gen.Unit
 */
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
import lunarians.LunariansSettings;
import mindustry.Vars;
import mindustry.entities.Units;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;

public class CamFollow {
    private static final float INTERVAL = 0.4f;
    private static final float AIM_RADIUS_TILES = 5.0f;
    private static final float MOVE_REISSUE = 16.0f;
    private static final float TIME_REISSUE = 5.0f;
    private static final IntSeq selected = new IntSeq();
    private static final IntSeq tmpIds = new IntSeq();
    private static float clock;
    private static float timer;
    private static boolean errorLogged;
    private static int lastSig;
    private static boolean lastWasAttack;
    private static int lastTargetId;
    private static final Vec2 lastPos;
    private static float lastTime;
    private static boolean hasLast;
    private static long selectedHash;

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run((Object)EventType.Trigger.update, (Runnable)new Runnable(){

            @Override
            public void run() {
                CamFollow.update();
            }
        });
        Events.on(EventType.WorldLoadEvent.class, (Cons)new Cons<EventType.WorldLoadEvent>(){

            public void get(EventType.WorldLoadEvent worldLoadEvent) {
                selected.clear();
                selectedHash = 0L;
                CamFollow.resetOrder();
            }
        });
    }

    private static void resetOrder() {
        hasLast = false;
        lastSig = Integer.MIN_VALUE;
        lastTargetId = -1;
    }

    public static void setEnabled(boolean bl) {
        LunariansSettings.enableCamFollow = bl;
        LunariansSettings.save();
        CamFollow.resetOrder();
        if (bl) {
            CamFollow.captureSelection();
        } else {
            selected.clear();
            selectedHash = 0L;
        }
    }

    public static int count() {
        return CamFollow.selected.size;
    }

    private static void captureSelection() {
        Unit unit;
        int n;
        if (Vars.control == null || Vars.control.input == null) {
            return;
        }
        Seq seq = Vars.control.input.selectedUnits;
        if (seq == null || seq.isEmpty()) {
            return;
        }
        long l = 0L;
        int n2 = 0;
        for (n = 0; n < seq.size; ++n) {
            unit = (Unit)seq.get(n);
            if (unit == null || !unit.isValid() || !unit.isCommandable() || unit.isPlayer()) continue;
            l += ((long)unit.id + 1L) * 2654435761L;
            ++n2;
        }
        if (n2 == 0 || l == selectedHash) {
            return;
        }
        selectedHash = l;
        selected.clear();
        for (n = 0; n < seq.size; ++n) {
            unit = (Unit)seq.get(n);
            if (unit == null || !unit.isValid() || !unit.isCommandable() || unit.isPlayer()) continue;
            selected.add(unit.id);
        }
        CamFollow.resetOrder();
    }

    private static void update() {
        block4: {
            if (!LunariansSettings.enableCamFollow || Vars.state == null || !Vars.state.isGame() || Vars.state.isPaused() || Vars.player == null) {
                return;
            }
            clock += Time.delta / 60.0f;
            CamFollow.captureSelection();
            timer += Time.delta / 60.0f;
            if (timer < 0.4f) {
                return;
            }
            timer = 0.0f;
            try {
                CamFollow.run();
            }
            catch (Throwable throwable) {
                if (errorLogged) break block4;
                errorLogged = true;
                Log.err((String)"[Lunarians] CamFollow error", (Throwable)throwable);
            }
        }
    }

    private static void run() {
        Teamc teamc;
        Object object;
        int n;
        IntSeq intSeq = tmpIds;
        intSeq.clear();
        for (n = 0; n < CamFollow.selected.size; ++n) {
            object = Groups.unit.getByID(selected.get(n));
            if (object == null || !((Unit)object).isValid() || ((Unit)object).team != Vars.player.team() || !((Unit)object).isCommandable()) continue;
            intSeq.add(((Unit)object).id);
        }
        if (intSeq.size != CamFollow.selected.size) {
            selected.clear();
            selected.addAll(intSeq);
        }
        if (intSeq.size == 0) {
            return;
        }
        n = CamFollow.signature(intSeq);
        object = intSeq.toArray();
        Unit unit = Vars.player.dead() ? null : Vars.player.unit();
        Unit unit2 = unit;
        if (unit != null && unit.isValid() && unit.isShooting() && (teamc = Units.closestTarget((Team)Vars.player.team(), (float)unit.aimX(), (float)unit.aimY(), (float)40.0f)) != null) {
            int n2 = teamc instanceof Unit ? ((Unit)teamc).id : ((Building)teamc).pos();
            boolean bl = hasLast && lastWasAttack && lastTargetId == n2 && lastSig == n;
            boolean bl2 = bl;
            if (!bl || clock - lastTime > 2.0f) {
                Unit unit3 = teamc instanceof Unit ? (Unit)teamc : null;
                Building building = teamc instanceof Building ? (Building)teamc : null;
                Call.commandUnits((Player)Vars.player, (int[])((int[])object), (Building)building, (Unit)unit3, null, (boolean)false, (boolean)true);
                hasLast = true;
                lastWasAttack = true;
                lastTargetId = n2;
                lastSig = n;
                lastTime = clock;
            }
            return;
        }
        float f = Mathf.clamp((float)Core.camera.position.x, (float)0.0f, (float)Vars.world.unitWidth());
        float f2 = Mathf.clamp((float)Core.camera.position.y, (float)0.0f, (float)Vars.world.unitHeight());
        boolean bl = !hasLast || lastWasAttack || lastSig != n || lastPos.dst(f, f2) > 16.0f || clock - lastTime > 5.0f;
        boolean bl3 = bl;
        if (bl) {
            Call.commandUnits((Player)Vars.player, (int[])((int[])object), null, null, (Vec2)new Vec2(f, f2), (boolean)false, (boolean)true);
            hasLast = true;
            lastWasAttack = false;
            lastTargetId = -1;
            lastSig = n;
            lastPos.set(f, f2);
            lastTime = clock;
        }
    }

    private static int signature(IntSeq intSeq) {
        int n = intSeq.size;
        for (int i = 0; i < intSeq.size; ++i) {
            n = n * 31 + intSeq.get(i);
        }
        return n;
    }

    static {
        lastSig = Integer.MIN_VALUE;
        lastTargetId = -1;
        lastPos = new Vec2();
        lastTime = -999.0f;
    }
}

