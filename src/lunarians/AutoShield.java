package lunarians;

import arc.Events;
import arc.func.Cons;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.data.ShieldSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.entities.abilities.Ability;
import mindustry.entities.abilities.ForceFieldAbility;
import mindustry.entities.abilities.ShieldArcAbility;
import mindustry.entities.abilities.ShieldRegenFieldAbility;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoShield.class */
public class AutoShield {
    private static final float SCAN = 0.6f;
    private static float timer;
    private static int count;
    private static boolean errorLogged;
    private static final IntSet claimed = new IntSet();

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run(EventType.Trigger.update, new Runnable() { // from class: lunarians.AutoShield.1
            @Override // java.lang.Runnable
            public void run() {
                AutoShield.update();
            }
        });
        Events.on(EventType.WorldLoadEvent.class, new Cons<EventType.WorldLoadEvent>() { // from class: lunarians.AutoShield.2
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
        for (int i = 0; i < unitType.abilities.size; i++) {
            Ability ability = (Ability) unitType.abilities.get(i);
            if ((ability instanceof ForceFieldAbility) || (ability instanceof ShieldArcAbility) || (ability instanceof ShieldRegenFieldAbility) || ability.getClass().getSimpleName().toLowerCase().contains("shield")) {
                return true;
            }
        }
        return false;
    }

    private static void update() {
        if (!LunariansSettings.enableAutoShield || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
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
                Log.err("[Lunarians] AutoShield error", th);
            }
        }
    }

    private static void scan() {
        Unit byID;
        Seq<ShieldSlot> seq = LunariansSettings.shieldSlots;
        if (seq.isEmpty()) {
            return;
        }
        count = 0;
        claimed.clear();
        int size = Groups.unit.size();
        for (int i = 0; i < seq.size; i++) {
            ShieldSlot shieldSlot = (ShieldSlot) seq.get(i);
            if (shieldSlot.ready() && (byID = Groups.unit.getByID(shieldSlot.followId)) != null && byID.isValid()) {
                shieldSlot.followLabel = byID.type.localizedName;
                Seq seq2 = new Seq();
                IntSeq intSeq = new IntSeq();
                for (int i2 = 0; i2 < size; i2++) {
                    Unit index = Groups.unit.index(i2);
                    if (index != null && index.isValid() && index.team == Vars.player.team() && shieldSlot.unit.equals(index.type.name) && (index.controller() instanceof CommandAI) && index.id != byID.id) {
                        seq2.add(index);
                        if (index.controller().command != UnitCommand.moveCommand) {
                            intSeq.add(index.id);
                        }
                    }
                }
                if (shieldSlot.maxUnits > 0 && seq2.size > shieldSlot.maxUnits) {
                    seq2.truncate(shieldSlot.maxUnits);
                }
                for (int i3 = seq2.size - 1; i3 >= 0; i3--) {
                    if (claimed.contains(((Unit) seq2.get(i3)).id)) {
                        seq2.remove(i3);
                    }
                }
                if (!seq2.isEmpty()) {
                    for (int i4 = 0; i4 < seq2.size; i4++) {
                        claimed.add(((Unit) seq2.get(i4)).id);
                    }
                    if (intSeq.size > 0) {
                        Call.setUnitCommand(Vars.player, intSeq.toArray(), UnitCommand.moveCommand);
                    }
                    for (int i5 = 0; i5 < seq2.size; i5++) {
                        Unit unit = (Unit) seq2.get(i5);
                        float f = seq2.size <= 1 ? 0.0f : ((i5 * 360.0f) / seq2.size) * 0.017453292f;
                        Call.commandUnits(Vars.player, new int[]{unit.id}, (Building) null, (Unit) null, PathSafe.safeNear(unit, byID.x + (((float) Math.cos(f)) * 18.0f), byID.y + (((float) Math.sin(f)) * 18.0f), 48.0f), false, true);
                    }
                    count += seq2.size;
                }
            }
        }
    }
}
