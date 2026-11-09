/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.Events
 *  arc.graphics.Color
 *  arc.graphics.g2d.Draw
 *  arc.graphics.g2d.Lines
 *  arc.math.Mathf
 *  arc.math.geom.Position
 *  arc.math.geom.Vec2
 *  arc.struct.IntFloatMap
 *  arc.struct.IntSet
 *  arc.struct.Seq
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.ai.UnitCommand
 *  mindustry.ai.types.CommandAI
 *  mindustry.game.EventType$Trigger
 *  mindustry.game.EventType$WorldLoadEvent
 *  mindustry.game.Team
 *  mindustry.gen.Building
 *  mindustry.gen.Call
 *  mindustry.gen.Groups
 *  mindustry.gen.Player
 *  mindustry.gen.Unit
 *  mindustry.graphics.Pal
 *  mindustry.type.UnitType
 *  mindustry.world.blocks.defense.Wall
 *  mindustry.world.blocks.defense.turrets.Turret
 *  mindustry.world.blocks.power.PowerGenerator
 *  mindustry.world.blocks.production.GenericCrafter
 *  mindustry.world.blocks.storage.CoreBlock
 *  mindustry.world.blocks.storage.CoreBlock$CoreBuild
 *  mindustry.world.blocks.units.UnitFactory
 *  mindustry.world.meta.BlockGroup
 */
package lunarians;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.struct.IntFloatMap;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.AutoUnitHeal;
import lunarians.ChatCommands;
import lunarians.LunariansSettings;
import lunarians.PathSafe;
import lunarians.UnitCoord;
import lunarians.data.AttackSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.defense.turrets.Turret;
import mindustry.world.blocks.power.PowerGenerator;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.units.UnitFactory;
import mindustry.world.meta.BlockGroup;

public class AutoAttack {
    private static final float SCAN = 0.7f;
    private static float timer;
    private static int count;
    private static boolean errorLogged;
    private static final IntSet claimed;
    private static final IntFloatMap lastHp;
    private static final IntFloatMap dpsEst;
    private static float lastScanTime;
    private static float lastRegroupAt;
    public static final String KEY_STANDOFF = "lunarians-attack-standoff-blocks";
    public static final String KEY_REGROUP = "lunarians-attack-regroup";
    public static final String KEY_REGROUP_SEC = "lunarians-attack-regroup-sec";
    private static final IntSet fleeing;
    private static final IntFloatMap fleeStart;
    private static final IntFloatMap gatherSince;
    private static final Seq<Building> enemyB;
    private static final Seq<float[]> lines;
    private static final Seq<float[]> shown;
    private static boolean drawReg;

    public static int standoffBlocks() {
        return Math.max(1, Math.min(40, Core.settings.getInt(KEY_STANDOFF, 3)));
    }

    public static void setStandoffBlocks(int n) {
        Core.settings.put(KEY_STANDOFF, (Object)Math.max(1, Math.min(40, n)));
    }

    public static int maxStandoffTiles(Unit unit) {
        if (unit == null || unit.type == null) {
            return 4;
        }
        return AutoAttack.maxStandoffTiles(unit.type);
    }

    public static int maxStandoffTiles(UnitType unitType) {
        if (unitType == null) {
            return 4;
        }
        float f = unitType.maxRange / 8.0f;
        return Math.max(1, Mathf.ceil((float)f) + 2);
    }

    public static boolean regroupEnabled() {
        return Core.settings.getBool(KEY_REGROUP, false);
    }

    public static void setRegroupEnabled(boolean bl) {
        Core.settings.put(KEY_REGROUP, (Object)bl);
    }

    public static int regroupSec() {
        return Math.max(5, Math.min(120, Core.settings.getInt(KEY_REGROUP_SEC, 5)));
    }

    public static void setRegroupSec(int n) {
        Core.settings.put(KEY_REGROUP_SEC, (Object)Math.max(5, Math.min(120, n)));
    }

    public static float standoffDist(Unit unit, AttackSlot attackSlot) {
        int n = AutoAttack.maxStandoffTiles(unit);
        int n2 = attackSlot != null ? attackSlot.standoffTiles : 0;
        int n3 = n2 <= 0 ? n : Mathf.clamp((int)n2, (int)1, (int)n);
        return n3 * 8;
    }

    public static float standoffDist(Unit unit) {
        return AutoAttack.standoffDist(unit, null);
    }

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run((Object)EventType.Trigger.update, AutoAttack::update);
        if (!drawReg) {
            drawReg = true;
            Events.run((Object)EventType.Trigger.draw, AutoAttack::drawOverlay);
        }
        ChatCommands.init();
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> AutoAttack.reset());
        try {
            if (Vars.ui != null && Vars.ui.settings != null) {
                Vars.ui.settings.addCategory("Lunarians Attack", settingsTable -> {
                    AutoAttack.label(KEY_STANDOFF, "Attack distance", "Distancia de ataque");
                    AutoAttack.label(KEY_REGROUP, "Regroup periodically", "Reagrupar periodicamente");
                    AutoAttack.label(KEY_REGROUP_SEC, "Regroup interval", "Intervalo de reagrupar");
                    AutoAttack.label("lunarians-attack-smart", "Smart targets (turrets first)", "Alvos inteligentes (torres primeiro)");
                    AutoAttack.label("lunarians-attack-flee", "Retreat below HP %", "Recuar abaixo de % de vida");
                    AutoAttack.label("lunarians-attack-formation", "Formation + gather before advancing", "Formacao + juntar antes de avancar");
                    AutoAttack.label("lunarians-heal-escort", "Healer escort (open lane / shield)", "Escolta do curandeiro (abre caminho / protege)");
                    AutoAttack.label("lunarians-attack-overlay", "Show zones and targets on map", "Mostrar zonas e alvos no mapa");
                    settingsTable.sliderPref(KEY_STANDOFF, 3, 1, 40, 1, n -> n + " tiles");
                    settingsTable.checkPref(KEY_REGROUP, false);
                    settingsTable.sliderPref(KEY_REGROUP_SEC, 5, 5, 60, 1, n -> n + "s");
                    settingsTable.checkPref("lunarians-attack-smart", true);
                    settingsTable.sliderPref("lunarians-attack-flee", 0, 0, 90, 5, n -> n == 0 ? AutoAttack.tr("off", "desligado") : n + "%");
                    settingsTable.checkPref("lunarians-attack-formation", true);
                    settingsTable.checkPref("lunarians-heal-escort", true);
                    settingsTable.checkPref("lunarians-attack-overlay", true);
                    settingsTable.row();
                    settingsTable.button(AutoAttack.tr("Profile: Defend (retreat 40%)", "Perfil: Defender (recua 40%)"), () -> AutoAttack.profile(1, 40, 1)).growX().height(40.0f).row();
                    settingsTable.button(AutoAttack.tr("Profile: Balanced (retreat 25%)", "Perfil: Equilibrado (recua 25%)"), () -> AutoAttack.profile(1, 25, 1)).growX().height(40.0f).row();
                    settingsTable.button(AutoAttack.tr("Profile: Rush (no retreat)", "Perfil: Rush (sem recuo)"), () -> AutoAttack.profile(0, 0, 0)).growX().height(40.0f).row();
                });
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void reset() {
        timer = 0.0f;
        count = 0;
        lastHp.clear();
        dpsEst.clear();
        claimed.clear();
        lastScanTime = -1.0f;
        lastRegroupAt = 0.0f;
        fleeing.clear();
        enemyB.clear();
        fleeStart.clear();
        gatherSince.clear();
        lines.clear();
        shown.clear();
    }

    private static void update() {
        block4: {
            if (!LunariansSettings.enableAutoAttack || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
                return;
            }
            if ((timer += Time.delta / 60.0f) < LunariansSettings.actionInterval(0.7f)) {
                return;
            }
            float f = timer;
            timer = 0.0f;
            try {
                AutoAttack.scan(f);
            }
            catch (Throwable throwable) {
                if (errorLogged) break block4;
                errorLogged = true;
                Log.err((String)"[Lunarians] AutoAttack error", (Throwable)throwable);
            }
        }
    }

    private static Unit nearestHealer(Unit unit) {
        Unit unit2 = null;
        float f = Float.MAX_VALUE;
        for (int i = 0; i < Groups.unit.size(); ++i) {
            float f2;
            Unit unit3 = (Unit)Groups.unit.index(i);
            if (unit3 == null || !unit3.isValid() || unit3.team != Vars.player.team() || !AutoUnitHeal.isActiveHealer(unit3.id) || !((f2 = unit.dst((Position)unit3)) < f)) continue;
            f = f2;
            unit2 = unit3;
        }
        return unit2;
    }

    private static boolean isCore(Building building) {
        return building != null && building.block instanceof CoreBlock;
    }

    private static float coreSafeDist(Building building) {
        return (float)(building.block.size * 8) * 0.7f + 48.0f + 24.0f;
    }

    private static void trackDamage(Unit unit, float f) {
        float f2 = lastHp.get(unit.id, unit.health);
        float f3 = Math.max(0.0f, f2 - unit.health);
        float f4 = f > 0.05f ? f3 / f : 0.0f;
        float f5 = dpsEst.get(unit.id, 0.0f);
        dpsEst.put(unit.id, f5 * 0.55f + f4 * 0.45f);
        lastHp.put(unit.id, unit.health);
    }

    private static boolean isHighDamage(Unit unit) {
        float f = dpsEst.get(unit.id, 0.0f);
        return f > Math.max(4.0f, unit.maxHealth * 0.08f);
    }

    private static boolean groupLowDamage(Seq<Unit> seq) {
        if (seq.isEmpty()) {
            return true;
        }
        int n = 0;
        for (int i = 0; i < seq.size; ++i) {
            if (!AutoAttack.isHighDamage((Unit)seq.get(i))) continue;
            ++n;
        }
        return n * 2 <= seq.size;
    }

    private static void ensureMove(Unit unit) {
        if (!(unit.controller() instanceof CommandAI)) {
            return;
        }
        CommandAI commandAI = (CommandAI)unit.controller();
        if (commandAI.command != UnitCommand.moveCommand) {
            Call.setUnitCommand((Player)Vars.player, (int[])new int[]{unit.id}, (UnitCommand)UnitCommand.moveCommand);
        }
    }

    private static void commandBuild(Unit unit, Building building, float f, boolean bl) {
        AutoAttack.ensureMove(unit);
        float f2 = building.angleTo((Position)unit) * ((float)Math.PI / 180);
        float f3 = f * 0.9f;
        if (bl && AutoAttack.isCore(building)) {
            f3 = Math.max(f3, AutoAttack.coreSafeDist(building) * 0.95f);
        }
        float f4 = building.x + Mathf.cos((float)f2) * f3;
        float f5 = building.y + Mathf.sin((float)f2) * f3;
        Vec2 vec2 = PathSafe.safeNear(unit, f4, f5, 64.0f);
        UnitCoord.cmd(unit, 1, 90.0f, building, null, vec2);
    }

    private static void holdAwayFromCore(Unit unit, Building building, float f) {
        AutoAttack.ensureMove(unit);
        float f2 = Math.max(f * 0.95f, AutoAttack.coreSafeDist(building));
        float f3 = building.angleTo((Position)unit) * ((float)Math.PI / 180);
        float f4 = building.x + Mathf.cos((float)f3) * f2;
        float f5 = building.y + Mathf.sin((float)f3) * f2;
        Vec2 vec2 = PathSafe.safeNear(unit, f4, f5, 80.0f);
        UnitCoord.cmd(unit, 1, 90.0f, null, null, vec2);
    }

    private static void commandBuildFormation(Unit unit, Building building, float f, float f2, int n) {
        AutoAttack.ensureMove(unit);
        float f3 = building.angleTo((Position)unit) * ((float)Math.PI / 180);
        float f4 = n <= 1 ? 0.0f : (f2 - (float)(n - 1) * 0.5f) * 0.35f;
        float f5 = f3 + f4;
        float f6 = f * 0.9f;
        float f7 = building.x + Mathf.cos((float)f5) * f6;
        float f8 = building.y + Mathf.sin((float)f5) * f6;
        Vec2 vec2 = PathSafe.safeNear(unit, f7, f8, 64.0f);
        UnitCoord.cmd(unit, 1, 90.0f, building, null, vec2);
    }

    private static void regroupSlot(Seq<Unit> seq) {
        int n;
        if (seq.size < 2) {
            return;
        }
        float f = 0.0f;
        float f2 = 0.0f;
        for (n = 0; n < seq.size; ++n) {
            f += ((Unit)seq.get((int)n)).x;
            f2 += ((Unit)seq.get((int)n)).y;
        }
        f /= (float)seq.size;
        f2 /= (float)seq.size;
        for (n = 0; n < seq.size; ++n) {
            Unit unit = (Unit)seq.get(n);
            AutoAttack.ensureMove(unit);
            float f3 = (float)n * 360.0f / (float)seq.size * ((float)Math.PI / 180);
            float f4 = Mathf.cos((float)f3) * 16.0f;
            float f5 = Mathf.sin((float)f3) * 16.0f;
            Vec2 vec2 = PathSafe.safeNear(unit, f + f4, f2 + f5, 48.0f);
            UnitCoord.cmd(unit, 1, 90.0f, null, null, vec2);
            ++count;
        }
    }

    private static void scan(float f) {
        Seq<AttackSlot> seq = LunariansSettings.attackSlots;
        if (seq.isEmpty()) {
            return;
        }
        count = 0;
        lines.clear();
        claimed.clear();
        int n = Groups.unit.size();
        enemyB.clear();
        for (Building building : Groups.build) {
            if (building == null || !building.isValid() || building.team == Vars.player.team() || building.team == Team.derelict) continue;
            enemyB.add((Object)building);
        }
        boolean bl = AutoAttack.regroupEnabled() && Time.time - lastRegroupAt >= (float)AutoAttack.regroupSec() * 60.0f;
        boolean bl2 = bl;
        if (bl) {
            lastRegroupAt = Time.time;
        }
        for (int i = 0; i < seq.size; ++i) {
            int n2;
            Unit unit22;
            boolean bl3;
            Unit unit3;
            int n3;
            AttackSlot attackSlot = (AttackSlot)seq.get(i);
            if (!attackSlot.ready()) continue;
            Seq seq2 = new Seq();
            for (n3 = 0; n3 < n; ++n3) {
                Unit unit4 = (Unit)Groups.unit.index(n3);
                if (unit4 == null || !unit4.isValid() || unit4.team != Vars.player.team() || claimed.contains(unit4.id) || UnitCoord.holder(unit4.id) == 2 || UnitCoord.holder(unit4.id) == 3 || AutoUnitHeal.isActiveHealer(unit4.id) || AutoUnitHeal.isHealerType(unit4.type) || !attackSlot.unit.equals(unit4.type.name) || !(unit4.controller() instanceof CommandAI)) continue;
                seq2.add((Object)unit4);
            }
            if (attackSlot.maxUnits > 0 && seq2.size > attackSlot.maxUnits) {
                seq2.truncate(attackSlot.maxUnits);
            }
            if (seq2.isEmpty()) continue;
            for (n3 = 0; n3 < seq2.size; ++n3) {
                claimed.add(((Unit)seq2.get((int)n3)).id);
                AutoAttack.trackDamage((Unit)seq2.get(n3), f);
            }
            int n4 = AutoAttack.fleePercent();
            if (n4 > 0) {
                CoreBlock.CoreBuild coreBuild = Vars.player.closestCore();
                for (int j = seq2.size - 1; j >= 0; --j) {
                    Vec2 vec2;
                    unit3 = (Unit)seq2.get(j);
                    float f2 = unit3.health / Math.max(1.0f, unit3.maxHealth);
                    bl3 = fleeing.contains(unit3.id);
                    if (!bl3 && f2 * 100.0f < (float)n4) {
                        fleeing.add(unit3.id);
                        fleeStart.put(unit3.id, Time.time);
                        bl3 = true;
                    } else if (bl3 && (f2 >= 0.8f || Time.time - fleeStart.get(unit3.id, 0.0f) > 3600.0f)) {
                        fleeing.remove(unit3.id);
                        bl3 = false;
                    }
                    if (!bl3) continue;
                    Unit unit5 = unit22 = LunariansSettings.autoAttackHeal ? AutoAttack.nearestHealer(unit3) : null;
                    if (unit22 != null) {
                        vec2 = PathSafe.safeNear(unit3, unit22.x, unit22.y, 48.0f);
                        if (UnitCoord.cmd(unit3, 4, 90.0f, null, null, vec2)) {
                            ++count;
                        }
                    } else if (coreBuild != null && unit3.dst((Position)coreBuild) > 96.0f && UnitCoord.cmd(unit3, 4, 90.0f, null, null, vec2 = PathSafe.safeNear(unit3, coreBuild.x + 32.0f, coreBuild.y + 32.0f, 64.0f))) {
                        ++count;
                    }
                    seq2.remove(j);
                }
                if (seq2.isEmpty()) continue;
            }
            if (bl) {
                AutoAttack.regroupSlot((Seq<Unit>)seq2);
                continue;
            }
            float f3 = attackSlot.hasZone() ? (float)(attackSlot.x * 8) + 4.0f : ((Unit)seq2.first()).x;
            float f4 = attackSlot.hasZone() ? (float)(attackSlot.y * 8) + 4.0f : ((Unit)seq2.first()).y;
            float f5 = f4;
            if (!attackSlot.hasZone()) {
                f3 = 0.0f;
                f4 = 0.0f;
                for (int j = 0; j < seq2.size; ++j) {
                    f3 += ((Unit)seq2.get((int)j)).x;
                    f4 += ((Unit)seq2.get((int)j)).y;
                }
                f3 /= (float)seq2.size;
                f4 /= (float)seq2.size;
            }
            unit3 = new Seq();
            boolean bl4 = false;
            for (Unit unit22 : enemyB) {
                if (attackSlot.hasZone() && Mathf.dst((float)f3, (float)f4, (float)unit22.x, (float)unit22.y) > attackSlot.zoneRadius * 8.0f) continue;
                unit3.add((Object)unit22);
                if (!(unit22.block instanceof Turret)) continue;
                bl4 = true;
            }
            bl3 = AutoAttack.smartTargets();
            unit22 = null;
            Building building = null;
            float f6 = Float.MAX_VALUE;
            float f7 = Float.MAX_VALUE;
            float f8 = Float.MAX_VALUE;
            for (int j = 0; j < unit3.size; ++j) {
                Building building2 = (Building)unit3.get(j);
                float f9 = 0.0f;
                for (n2 = 0; n2 < seq2.size; ++n2) {
                    f9 += ((Unit)seq2.get(n2)).dst((Position)building2);
                }
                f9 /= (float)Math.max(1, seq2.size);
                if (AutoAttack.isCore(building2)) {
                    if (!(f9 < f6)) continue;
                    f6 = f9;
                    unit22 = building2;
                    continue;
                }
                float f10 = f9;
                if (bl3) {
                    boolean bl5;
                    boolean bl6 = bl5 = building2.block instanceof Wall || building2.block.group == BlockGroup.transportation;
                    if (building2.block instanceof Turret) {
                        if (f9 < 640.0f) {
                            f10 -= 240.0f;
                        }
                    } else if (building2.block instanceof PowerGenerator) {
                        f10 -= 96.0f;
                    } else if (building2.block instanceof UnitFactory || building2.block instanceof GenericCrafter) {
                        f10 -= 64.0f;
                    } else if (bl5) {
                        f10 += bl4 ? 800.0f : 160.0f;
                    }
                }
                if (!(f10 < f7)) continue;
                f7 = f10;
                f8 = f9;
                building = building2;
            }
            float f11 = Math.min(f6, f8);
            if (AutoAttack.formationOn() && !attackSlot.hasZone() && seq2.size > 1 && f11 > 240.0f) {
                float f12 = 0.0f;
                for (int j = 0; j < seq2.size; ++j) {
                    f12 = Math.max(f12, ((Unit)seq2.get(j)).dst(f3, f4));
                }
                if (f12 > 144.0f) {
                    float f13 = gatherSince.get(i, -1.0f);
                    if (f13 < 0.0f) {
                        gatherSince.put(i, Time.time);
                        f13 = Time.time;
                    }
                    if (Time.time - f13 < 600.0f) {
                        AutoAttack.regroupSlot((Seq<Unit>)seq2);
                        continue;
                    }
                } else {
                    gatherSince.put(i, -1.0f);
                }
            }
            if (AutoAttack.overlayOn()) {
                if (building != null) {
                    lines.add((Object)new float[]{f3, f4, building.x, building.y, 0.0f});
                }
                if (unit22 != null) {
                    lines.add((Object)new float[]{f3, f4, unit22.x, unit22.y, 1.0f});
                }
                if (attackSlot.hasZone()) {
                    lines.add((Object)new float[]{(float)(attackSlot.x * 8) + 4.0f, (float)(attackSlot.y * 8) + 4.0f, attackSlot.zoneRadius * 8.0f, 0.0f, 2.0f});
                }
            }
            seq2.sort(unit -> -unit.maxHealth);
            Unit unit6 = unit22 != null ? AutoAttack.pickCoreBreaker((Seq<Unit>)seq2, (Building)unit22) : null;
            boolean bl7 = AutoAttack.groupLowDamage((Seq<Unit>)seq2);
            for (n2 = 0; n2 < seq2.size; ++n2) {
                Unit unit7 = (Unit)seq2.get(n2);
                float f14 = AutoAttack.standoffDist(unit7, attackSlot);
                if (unit22 != null && AutoAttack.isHighDamage(unit7) && !bl7) {
                    AutoAttack.holdAwayFromCore(unit7, (Building)unit22, f14);
                    ++count;
                    continue;
                }
                if (unit7 == unit6 && unit22 != null && bl7) {
                    AutoAttack.commandBuild(unit7, (Building)unit22, f14, true);
                    ++count;
                    continue;
                }
                if (building != null) {
                    float f15 = unit7.dst(f3, f4);
                    float f16 = 40.0f;
                    if (f15 > 40.0f * 1.35f + 40.0f) {
                        AutoAttack.ensureMove(unit7);
                        float f17 = (float)n2 * 360.0f / (float)seq2.size * ((float)Math.PI / 180);
                        UnitCoord.cmd(unit7, 1, 90.0f, null, null, new Vec2(f3 + Mathf.cos((float)f17) * 18.0f, f4 + Mathf.sin((float)f17) * 18.0f));
                        ++count;
                        continue;
                    }
                    AutoAttack.commandBuildFormation(unit7, building, f14, AutoAttack.formationOn() ? (float)(n2 == 0 ? 0 : ((n2 & 1) == 1 ? (n2 + 1) / 2 : -n2 / 2)) + (float)(seq2.size - 1) * 0.5f : (float)n2, seq2.size);
                    ++count;
                    continue;
                }
                if (unit22 == null) continue;
                AutoAttack.holdAwayFromCore(unit7, (Building)unit22, f14);
                ++count;
            }
        }
        shown.clear();
        shown.addAll(lines);
    }

    private static Unit pickCoreBreaker(Seq<Unit> seq, Building building) {
        Unit unit = null;
        float f = Float.MAX_VALUE;
        for (int i = 0; i < seq.size; ++i) {
            float f2;
            Unit unit2 = (Unit)seq.get(i);
            if (AutoAttack.isHighDamage(unit2) || !((f2 = unit2.dst((Position)building) - unit2.health / Math.max(1.0f, unit2.maxHealth) * 40.0f) < f)) continue;
            f = f2;
            unit = unit2;
        }
        return unit != null ? unit : (Unit)seq.first();
    }

    public static boolean smartTargets() {
        return Core.settings.getBool("lunarians-attack-smart", true);
    }

    public static boolean formationOn() {
        return Core.settings.getBool("lunarians-attack-formation", true);
    }

    public static boolean overlayOn() {
        return Core.settings.getBool("lunarians-attack-overlay", true);
    }

    public static int fleePercent() {
        return Math.max(0, Math.min(90, Core.settings.getInt("lunarians-attack-flee", 0)));
    }

    private static boolean pt() {
        return LunariansSettings.language != null && LunariansSettings.language.startsWith("pt");
    }

    private static String tr(String string, String string2) {
        return AutoAttack.pt() ? string2 : string;
    }

    private static void label(String string, String string2, String string3) {
        try {
            Core.bundle.getProperties().put((Object)("setting." + string + ".name"), (Object)AutoAttack.tr(string2, string3));
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void profile(int n, int n2, int n3) {
        Core.settings.put("lunarians-attack-smart", (Object)(n == 1 ? 1 : 0));
        Core.settings.put("lunarians-attack-flee", (Object)n2);
        Core.settings.put("lunarians-attack-formation", (Object)(n3 == 1 ? 1 : 0));
        if (Vars.ui != null) {
            Vars.ui.showInfoToast(AutoAttack.tr("Profile applied. Reopen this menu to see the values.", "Perfil aplicado. Reabra o menu para ver os valores."), 3.0f);
        }
    }

    private static void drawOverlay() {
        if (!(Vars.state != null && Vars.state.isGame() && LunariansSettings.enableAutoAttack && AutoAttack.overlayOn())) {
            return;
        }
        Draw.z((float)120.0f);
        for (int i = 0; i < AutoAttack.shown.size; ++i) {
            float[] fArray = (float[])shown.get(i);
            if (fArray[4] == 2.0f) {
                Draw.color((Color)Pal.accent, (float)0.8f);
                Lines.stroke((float)1.5f);
                Lines.circle((float)fArray[0], (float)fArray[1], (float)fArray[2]);
                continue;
            }
            Draw.color((Color)(fArray[4] == 1.0f ? Pal.remove : Pal.lightishOrange), (float)0.8f);
            Lines.stroke((float)1.2f);
            Lines.line((float)fArray[0], (float)fArray[1], (float)fArray[2], (float)fArray[3]);
            Lines.circle((float)fArray[2], (float)fArray[3], (float)6.0f);
        }
        Draw.reset();
    }

    public static int count() {
        return count;
    }

    static {
        claimed = new IntSet();
        lastHp = new IntFloatMap();
        dpsEst = new IntFloatMap();
        lastScanTime = -1.0f;
        fleeing = new IntSet();
        fleeStart = new IntFloatMap();
        gatherSince = new IntFloatMap();
        enemyB = new Seq();
        lines = new Seq();
        shown = new Seq();
    }
}

