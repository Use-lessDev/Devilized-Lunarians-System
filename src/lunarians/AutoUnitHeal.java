package lunarians;

import arc.Core;
import arc.Events;
import arc.func.Boolf;
import arc.func.Cons;
import arc.func.Floatf;
import arc.math.geom.Vec2;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.data.UnitHealSlot;
import mindustry.Vars;
import mindustry.ai.types.CommandAI;
import mindustry.entities.Units;
import mindustry.entities.abilities.Ability;
import mindustry.entities.abilities.RepairFieldAbility;
import mindustry.entities.abilities.UnitSpawnAbility;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.world.blocks.defense.turrets.Turret;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoUnitHeal.class */
public class AutoUnitHeal {
    private static final float SCAN = 0.8f;
    private static float timer;
    private static int count;
    private static boolean errorLogged;
    private static final IntSet targeted = new IntSet();
    private static final IntSet escortUsed = new IntSet();
    private static final IntSet claimed = new IntSet();
    public static final IntSet activeHealers = new IntSet();

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoUnitHeal$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoUnitHeal.$r8$lambda$bJ8C0y0NiB4VEWNF5Wffys8LV8Q();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoUnitHeal$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Cons {
        public final void get(Object obj) {
            AutoUnitHeal.reset();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoUnitHeal$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Floatf {
        public final /* synthetic */ float f$0;
        public final /* synthetic */ float f$1;
        public final /* synthetic */ float f$2;
        public final /* synthetic */ float f$3;

        public /* synthetic */ AnonymousClass2(float f, float f2, float f3, float f4) {
            this.f$0 = f;
            this.f$1 = f2;
            this.f$2 = f3;
            this.f$3 = f4;
        }

        public final float get(Object obj) {
            return AutoUnitHeal.lambda$escort$1(this.f$0, this.f$1, this.f$2, this.f$3, (Unit) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoUnitHeal$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements Boolf {
        public final boolean get(Object obj) {
            return AutoUnitHeal.lambda$escort$2((Unit) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoUnitHeal$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass4 implements Boolf {
        public final boolean get(Object obj) {
            return AutoUnitHeal.lambda$escort$3((Building) obj);
        }
    }

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run(EventType.Trigger.update, AutoUnitHeal::update);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            reset();
        });
    }

    public static void reset() {
        timer = 0.0f;
        count = 0;
        claimed.clear();
        activeHealers.clear();
        UnitCoord.reset();
    }

    public static int count() {
        return count;
    }

    private static boolean weaponHeals(Weapon weapon) {
        try {
            if (weapon.getClass().getField("healPercent").getFloat(weapon) > 0.0f) {
                return true;
            }
        } catch (Throwable th) {
        }
        try {
            if (weapon.getClass().getField("healAmount").getFloat(weapon) > 0.0f) {
                return true;
            }
        } catch (Throwable th2) {
        }
        try {
            if (weapon.bullet == null) {
                return false;
            }
            if (weapon.bullet.getClass().getField("healPercent").getFloat(weapon.bullet) > 0.0f) {
                return true;
            }
            return false;
        } catch (Throwable th3) {
            return false;
        }
    }

    public static boolean isHealerType(UnitType unitType) {
        if (unitType == null) {
            return false;
        }
        try {
            String str = unitType.name;
            if (str != null) {
                String lowerCase = str.toLowerCase();
                if (lowerCase.equals("vela") || lowerCase.contains("vela") || lowerCase.equals("mega") || lowerCase.equals("poly") || lowerCase.equals("nova") || lowerCase.contains("heal")) {
                    return true;
                }
                if (lowerCase.contains("repair")) {
                    return true;
                }
            }
        } catch (Throwable th) {
        }
        try {
            if (unitType.weapons != null) {
                for (int i = 0; i < unitType.weapons.size; i++) {
                    Weapon weapon = (Weapon) unitType.weapons.get(i);
                    if (weapon != null && weaponHeals(weapon)) {
                        return true;
                    }
                }
            }
        } catch (Throwable th2) {
        }
        if (unitType.abilities == null) {
            return false;
        }
        for (int i2 = 0; i2 < unitType.abilities.size; i2++) {
            Ability ability = (Ability) unitType.abilities.get(i2);
            if (ability != null) {
                if (ability instanceof RepairFieldAbility) {
                    return true;
                }
                if (ability instanceof UnitSpawnAbility) {
                    continue;
                } else {
                    String lowerCase2 = ability.getClass().getSimpleName().toLowerCase();
                    if (lowerCase2.contains("repair") || lowerCase2.contains("heal") || lowerCase2.contains("mend")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isActiveHealer(int i) {
        return activeHealers.contains(i);
    }

    private static void update() {
        if (!LunariansSettings.enableAutoUnitHeal || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
            activeHealers.clear();
            return;
        }
        float f = timer + (Time.delta / 60.0f);
        timer = f;
        if (f < LunariansSettings.actionInterval(SCAN)) {
            return;
        }
        timer = 0.0f;
        try {
            scan();
        } catch (Throwable th) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err("[Lunarians] AutoUnitHeal error", th);
            }
        }
    }

    private static void scan() {
        Seq<UnitHealSlot> seq = LunariansSettings.unitHealSlots;
        claimed.clear();
        activeHealers.clear();
        targeted.clear();
        escortUsed.clear();
        count = 0;
        if (seq == null || seq.isEmpty()) {
            return;
        }
        Seq seq2 = new Seq();
        int size = Groups.unit.size();
        for (int i = 0; i < size; i++) {
            Unit index = Groups.unit.index(i);
            if (index != null && index.isValid() && index.team == Vars.player.team() && index.health / Math.max(1.0f, index.maxHealth) < 0.95f) {
                seq2.add(index);
            }
        }
        if (seq2.isEmpty()) {
            return;
        }
        for (int i2 = 0; i2 < seq.size; i2++) {
            UnitHealSlot unitHealSlot = (UnitHealSlot) seq.get(i2);
            if (unitHealSlot != null && unitHealSlot.ready()) {
                Seq seq3 = new Seq();
                for (int i3 = 0; i3 < size; i3++) {
                    Unit index2 = Groups.unit.index(i3);
                    if (index2 != null && index2.isValid() && index2.team == Vars.player.team() && !claimed.contains(index2.id) && (index2.controller() instanceof CommandAI) && index2.type != null && unitHealSlot.unit.equals(index2.type.name)) {
                        seq3.add(index2);
                    }
                }
                if (unitHealSlot.maxUnits > 0 && seq3.size > unitHealSlot.maxUnits) {
                    seq3.truncate(unitHealSlot.maxUnits);
                }
                for (int i4 = 0; i4 < seq3.size; i4++) {
                    claimed.add(((Unit) seq3.get(i4)).id);
                    activeHealers.add(((Unit) seq3.get(i4)).id);
                }
                for (int i5 = 0; i5 < seq3.size; i5++) {
                    Unit unit = (Unit) seq3.get(i5);
                    Unit nearestNeedy = nearestNeedy(unit, seq2);
                    if (nearestNeedy != null) {
                        targeted.add(nearestNeedy.id);
                        float angleTo = nearestNeedy.angleTo(unit) * 0.017453292f;
                        if (UnitCoord.cmd(unit, 3, 90.0f, null, nearestNeedy, PathSafe.safeNear(unit, nearestNeedy.x + (((float) Math.cos(angleTo)) * 24.0f), nearestNeedy.y + (((float) Math.sin(angleTo)) * 24.0f), 40.0f))) {
                            count++;
                        }
                        if (escortOn()) {
                            escort(unit, nearestNeedy);
                        }
                    }
                }
            }
        }
    }

    public static boolean escortOn() {
        return Core.settings.getBool("lunarians-heal-escort", true);
    }

    private static void escort(Unit unit, Unit unit2) {
        float f;
        float f2;
        Vec2 vec2;
        float f3 = unit.x;
        float f4 = unit.y;
        float f5 = unit2.x - f3;
        float f6 = unit2.y - f4;
        float sqrt = (float) Math.sqrt((f5 * f5) + (f6 * f6));
        if (sqrt < 8.0f) {
            f = 1.0f;
            f2 = 0.0f;
            sqrt = 8.0f;
        } else {
            f = f5 / sqrt;
            f2 = f6 / sqrt;
        }
        float f7 = -f2;
        float f8 = f;
        Seq seq = new Seq();
        int size = Groups.unit.size();
        for (int i = 0; i < size; i++) {
            Unit unit3 = (Unit) Groups.unit.index(i);
            if (unit3 != null && unit3.isValid() && unit3.team == Vars.player.team() && unit3 != unit && unit3 != unit2 && !escortUsed.contains(unit3.id) && !activeHealers.contains(unit3.id) && !isHealerType(unit3.type) && (unit3.controller() instanceof CommandAI) && UnitCoord.holder(unit3.id) < 3 && (unit3.dst(f3, f4) <= 320.0f || unit3.dst(unit2.x, unit2.y) <= 320.0f)) {
                seq.add(unit3);
            }
        }
        if (seq.isEmpty()) {
            return;
        }
        seq.sort(unit4 -> {
            return Math.abs(((unit4.x - f3) * f7) + ((unit4.y - f4) * f8));
        });
        if (seq.size > 8) {
            seq.truncate(8);
        }
        Teamc closestTarget = Units.closestTarget(Vars.player.team(), unit2.x, unit2.y, 320.0f, unit5 -> {
            return true;
        }, building -> {
            return building.block instanceof Turret;
        });
        float atan2 = closestTarget != null ? (float) Math.atan2(closestTarget.y() - unit2.y, closestTarget.x() - unit2.x) : 0.0f;
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < seq.size; i4++) {
            Unit unit6 = (Unit) seq.get(i4);
            float f9 = ((unit6.x - f3) * f) + ((unit6.y - f4) * f2);
            float f10 = ((unit6.x - f3) * f7) + ((unit6.y - f4) * f8);
            if (f9 <= -24.0f || f9 >= sqrt + 24.0f || Math.abs(f10) >= 28.0f) {
                i3++;
            }
        }
        for (int i5 = 0; i5 < seq.size; i5++) {
            Unit unit7 = (Unit) seq.get(i5);
            float f11 = ((unit7.x - f3) * f) + ((unit7.y - f4) * f2);
            float f12 = ((unit7.x - f3) * f7) + ((unit7.y - f4) * f8);
            if (f11 > -24.0f && f11 < sqrt + 24.0f && Math.abs(f12) < 28.0f) {
                float f13 = f12 >= 0.0f ? 1.0f : -1.0f;
                vec2 = new Vec2(f3 + (f * f11) + (f7 * f13 * 44.0f), f4 + (f2 * f11) + (f8 * f13 * 44.0f));
            } else if (closestTarget != null) {
                float f14 = atan2 + ((i2 - ((i3 - 1) * 0.5f)) * 0.45f);
                float f15 = 26.0f + ((i2 % 2) * 10.0f);
                vec2 = new Vec2(unit2.x + (((float) Math.cos(f14)) * f15), unit2.y + (((float) Math.sin(f14)) * f15));
                i2++;
            }
            Vec2 safeNear = PathSafe.safeNear(unit7, vec2.x, vec2.y, 48.0f);
            escortUsed.add(unit7.id);
            if (UnitCoord.cmd(unit7, 2, 90.0f, null, null, safeNear)) {
                count++;
            }
        }
    }

    private static Unit nearestNeedy(Unit unit, Seq<Unit> seq) {
        Unit unit2 = null;
        float f = Float.MAX_VALUE;
        Unit unit3 = null;
        float f2 = Float.MAX_VALUE;
        for (int i = 0; i < seq.size; i++) {
            Unit unit4 = (Unit) seq.get(i);
            if (unit4.id != unit.id && !activeHealers.contains(unit4.id)) {
                float dst = unit.dst(unit4) * (0.5f + (unit4.health / Math.max(1.0f, unit4.maxHealth)));
                if (dst < f2) {
                    f2 = dst;
                    unit3 = unit4;
                }
                if (!targeted.contains(unit4.id) && dst < f) {
                    f = dst;
                    unit2 = unit4;
                }
            }
        }
        return unit2 != null ? unit2 : unit3;
    }
}
