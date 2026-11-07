/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.math.geom.Position
 *  arc.math.geom.Vec2
 *  arc.struct.IntSet
 *  arc.struct.Seq
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.ai.types.CommandAI
 *  mindustry.entities.Units
 *  mindustry.entities.abilities.Ability
 *  mindustry.entities.abilities.RepairFieldAbility
 *  mindustry.entities.abilities.UnitSpawnAbility
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.game.Team
 *  mindustry.gen.Groups
 *  mindustry.gen.Teamc
 *  mindustry.gen.Unit
 *  mindustry.type.UnitType
 *  mindustry.type.Weapon
 *  mindustry.world.blocks.defense.turrets.Turret
 */
package lunarians;

import arc.Core;
import arc.Events;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import java.lang.reflect.Field;
import lunarians.LunariansSettings;
import lunarians.PathSafe;
import lunarians.UnitCoord;
import lunarians.data.UnitHealSlot;
import mindustry.Vars;
import mindustry.ai.types.CommandAI;
import mindustry.entities.Units;
import mindustry.entities.abilities.Ability;
import mindustry.entities.abilities.RepairFieldAbility;
import mindustry.entities.abilities.UnitSpawnAbility;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Groups;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.world.blocks.defense.turrets.Turret;

public class AutoUnitHeal {
    private static final float SCAN = 0.8f;
    private static float timer;
    private static int count;
    private static boolean errorLogged;
    private static final IntSet claimed;
    public static final IntSet activeHealers;
    private static final IntSet targeted;
    private static final IntSet escortUsed;

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run((Object)EventType.Trigger.update, AutoUnitHeal::update);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> AutoUnitHeal.reset());
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
        Field field;
        try {
            field = weapon.getClass().getField("healPercent");
            if (field.getFloat(weapon) > 0.0f) {
                return true;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            field = weapon.getClass().getField("healAmount");
            if (field.getFloat(weapon) > 0.0f) {
                return true;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            if (weapon.bullet != null) {
                Field field2;
                field = weapon.bullet.getClass().getField("healPercent");
                if (field2.getFloat(weapon.bullet) > 0.0f) {
                    return true;
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return false;
    }

    public static boolean isHealerType(UnitType unitType) {
        if (unitType == null) {
            return false;
        }
        try {
            String string = unitType.name;
            if (string != null) {
                if ((string = string.toLowerCase()).equals("vela") || string.contains("vela")) {
                    return true;
                }
                if (string.equals("mega") || string.equals("poly") || string.equals("nova")) {
                    return true;
                }
                if (string.contains("heal") || string.contains("repair")) {
                    return true;
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            if (unitType.weapons != null) {
                for (int i = 0; i < unitType.weapons.size; ++i) {
                    Weapon weapon = (Weapon)unitType.weapons.get(i);
                    if (weapon == null || !AutoUnitHeal.weaponHeals(weapon)) continue;
                    return true;
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (unitType.abilities == null) {
            return false;
        }
        for (int i = 0; i < unitType.abilities.size; ++i) {
            String string;
            Ability ability = (Ability)unitType.abilities.get(i);
            if (ability == null) continue;
            if (ability instanceof RepairFieldAbility) {
                return true;
            }
            if (ability instanceof UnitSpawnAbility || !(string = ability.getClass().getSimpleName().toLowerCase()).contains("repair") && !string.contains("heal") && !string.contains("mend")) continue;
            return true;
        }
        return false;
    }

    public static boolean isActiveHealer(int n) {
        return activeHealers.contains(n);
    }

    private static void update() {
        block4: {
            float f;
            if (!LunariansSettings.enableAutoUnitHeal || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
                activeHealers.clear();
                return;
            }
            timer += Time.delta / 60.0f;
            if (f < LunariansSettings.actionInterval(0.8f)) {
                return;
            }
            timer = 0.0f;
            try {
                AutoUnitHeal.scan();
            }
            catch (Throwable throwable) {
                if (errorLogged) break block4;
                errorLogged = true;
                Log.err((String)"[Lunarians] AutoUnitHeal error", (Throwable)throwable);
            }
        }
    }

    private static void scan() {
        Object object;
        int n;
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
        int n2 = Groups.unit.size();
        for (n = 0; n < n2; ++n) {
            object = (Unit)Groups.unit.index(n);
            if (object == null || !object.isValid() || ((Unit)object).team != Vars.player.team() || !(((Unit)object).health / Math.max(1.0f, ((Unit)object).maxHealth) < 0.95f)) continue;
            seq2.add(object);
        }
        if (seq2.isEmpty()) {
            return;
        }
        for (n = 0; n < seq.size; ++n) {
            Unit unit;
            int n3;
            object = (UnitHealSlot)seq.get(n);
            if (object == null || !((UnitHealSlot)object).ready()) continue;
            Seq seq3 = new Seq();
            for (n3 = 0; n3 < n2; ++n3) {
                unit = (Unit)Groups.unit.index(n3);
                if (unit == null || !unit.isValid() || unit.team != Vars.player.team() || claimed.contains(unit.id) || !(unit.controller() instanceof CommandAI) || unit.type == null || !((UnitHealSlot)object).unit.equals(unit.type.name)) continue;
                seq3.add((Object)unit);
            }
            if (((UnitHealSlot)object).maxUnits > 0 && seq3.size > ((UnitHealSlot)object).maxUnits) {
                seq3.truncate(((UnitHealSlot)object).maxUnits);
            }
            for (n3 = 0; n3 < seq3.size; ++n3) {
                claimed.add(((Unit)seq3.get((int)n3)).id);
                activeHealers.add(((Unit)seq3.get((int)n3)).id);
            }
            for (n3 = 0; n3 < seq3.size; ++n3) {
                unit = (Unit)seq3.get(n3);
                Unit unit2 = AutoUnitHeal.nearestNeedy(unit, (Seq<Unit>)seq2);
                if (unit2 == null) continue;
                targeted.add(unit2.id);
                float f = unit2.angleTo((Position)unit) * ((float)Math.PI / 180);
                Vec2 vec2 = PathSafe.safeNear(unit, unit2.x + (float)Math.cos(f) * 24.0f, unit2.y + (float)Math.sin(f) * 24.0f, 40.0f);
                if (UnitCoord.cmd(unit, 3, 90.0f, null, unit2, vec2)) {
                    ++count;
                }
                if (!AutoUnitHeal.escortOn()) continue;
                AutoUnitHeal.escort(unit, unit2);
            }
        }
    }

    public static boolean escortOn() {
        return Core.settings.getBool("lunarians-heal-escort", true);
    }

    private static void escort(Unit unit2, Unit unit3) {
        float f;
        float f2;
        Unit unit4;
        int n;
        Teamc teamc;
        float f3 = unit2.x;
        float f4 = unit3.x - f3;
        float f5 = unit2.y;
        float f6 = unit3.y - f5;
        float f7 = (float)Math.sqrt(f4 * f4 + f6 * f6);
        if (f7 < 8.0f) {
            f4 = 1.0f;
            f6 = 0.0f;
            f7 = 8.0f;
        } else {
            f4 /= f7;
            f6 /= f7;
        }
        float f8 = -f6;
        float f9 = f4;
        Seq seq = new Seq();
        int n2 = Groups.unit.size();
        for (int i = 0; i < n2; ++i) {
            Unit unit5 = (Unit)Groups.unit.index(i);
            if (unit5 == null || !unit5.isValid() || unit5.team != Vars.player.team() || unit5 == unit2 || unit5 == unit3 || escortUsed.contains(unit5.id) || activeHealers.contains(unit5.id) || AutoUnitHeal.isHealerType(unit5.type) || !(unit5.controller() instanceof CommandAI) || UnitCoord.holder(unit5.id) >= 3 || unit5.dst(f3, f5) > 320.0f && unit5.dst(unit3.x, unit3.y) > 320.0f) continue;
            seq.add((Object)unit5);
        }
        if (seq.isEmpty()) {
            return;
        }
        float f10 = f3;
        float f11 = f5;
        float f12 = f4;
        float f13 = f6;
        float f14 = f8;
        float f15 = f9;
        seq.sort(unit -> Math.abs((unit.x - f10) * f14 + (unit.y - f11) * f15));
        if (seq.size > 8) {
            seq.truncate(8);
        }
        float f16 = (teamc = Units.closestTarget((Team)Vars.player.team(), (float)unit3.x, (float)unit3.y, (float)320.0f, unit -> true, building -> building.block instanceof Turret)) != null ? (float)Math.atan2(teamc.y() - unit3.y, teamc.x() - unit3.x) : 0.0f;
        int n3 = 0;
        int n4 = 0;
        for (n = 0; n < seq.size; ++n) {
            unit4 = (Unit)seq.get(n);
            f2 = (unit4.x - f3) * f4 + (unit4.y - f5) * f6;
            f = (unit4.x - f3) * f8 + (unit4.y - f5) * f9;
            if (f2 > -24.0f && f2 < f7 + 24.0f && Math.abs(f) < 28.0f) continue;
            ++n4;
        }
        for (n = 0; n < seq.size; ++n) {
            Vec2 vec2;
            float f17;
            unit4 = (Unit)seq.get(n);
            f2 = (unit4.x - f3) * f4 + (unit4.y - f5) * f6;
            f = (unit4.x - f3) * f8 + (unit4.y - f5) * f9;
            if (f2 > -24.0f && f2 < f7 + 24.0f && Math.abs(f) < 28.0f) {
                f17 = f >= 0.0f ? 1.0f : -1.0f;
                vec2 = new Vec2(f3 + f4 * f2 + f8 * f17 * 44.0f, f5 + f6 * f2 + f9 * f17 * 44.0f);
            } else {
                if (teamc == null) continue;
                f17 = f16 + ((float)n3 - (float)(n4 - 1) * 0.5f) * 0.45f;
                float f18 = 26.0f + (float)(n3 % 2) * 10.0f;
                vec2 = new Vec2(unit3.x + (float)Math.cos(f17) * f18, unit3.y + (float)Math.sin(f17) * f18);
                ++n3;
            }
            vec2 = PathSafe.safeNear(unit4, vec2.x, vec2.y, 48.0f);
            escortUsed.add(unit4.id);
            if (!UnitCoord.cmd(unit4, 2, 90.0f, null, null, vec2)) continue;
            ++count;
        }
    }

    private static Unit nearestNeedy(Unit unit, Seq<Unit> seq) {
        Unit unit2 = null;
        float f = Float.MAX_VALUE;
        Unit unit3 = null;
        float f2 = Float.MAX_VALUE;
        for (int i = 0; i < seq.size; ++i) {
            Unit unit4 = (Unit)seq.get(i);
            if (unit4.id == unit.id || activeHealers.contains(unit4.id)) continue;
            float f3 = unit.dst((Position)unit4) * (0.5f + unit4.health / Math.max(1.0f, unit4.maxHealth));
            if (f3 < f2) {
                f2 = f3;
                unit3 = unit4;
            }
            if (targeted.contains(unit4.id) || !(f3 < f)) continue;
            f = f3;
            unit2 = unit4;
        }
        return unit2 != null ? unit2 : unit3;
    }

    static {
        targeted = new IntSet();
        escortUsed = new IntSet();
        claimed = new IntSet();
        activeHealers = new IntSet();
    }
}

