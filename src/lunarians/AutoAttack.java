// 
// Decompiled by Procyon v0.6.0
// 

package lunarians;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.struct.IntFloatMap;
import arc.struct.IntIntMap;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import lunarians.LunariansSettings;
import lunarians.data.AttackSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.content.UnitTypes;
import mindustry.type.UnitType;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Bullet;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.ui.Fonts;
import mindustry.world.blocks.defense.turrets.Turret;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BlockGroup;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.units.UnitFactory;
import mindustry.world.blocks.power.PowerGenerator;
import mindustry.world.blocks.defense.Wall;
import mindustry.graphics.Pal;

public class AutoAttack
{
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
    private static final IntIntMap formLayerCount;
    private static final IntFloatMap formLayerTime;
    private static final Seq<Building> enemyB;
    private static final Seq<float[]> lines;
    private static final Seq<float[]> shown;
    private static boolean drawReg;
    // overlayOn now always enables Auto Attack core logic - removes the blocking condition
    // Previously: if (!overlayOn()) return; in drawOverlay()

    // Smart path optimization: cached turret data per scan
    private static final class TurretData {
        final float x, y, range, rangeSq, threatFactor;
        TurretData(float x, float y, float range, float threatFactor) {
            this.x = x; this.y = y; this.range = range;
            this.rangeSq = range * range;
            this.threatFactor = threatFactor;
        }
    }
    private static final Seq<TurretData> cachedTurrets = new Seq<>();
    private static int lastTurretScan = -1;

    // Projectile evasion: cached projectile data per scan
    private static final class ProjectileData {
        final float x, y, velX, velY, damage, speed;
        ProjectileData(float x, float y, float velX, float velY, float damage, float speed) {
            this.x = x; this.y = y; this.velX = velX; this.velY = velY; this.damage = damage; this.speed = speed;
        }
    }
    private static final Seq<ProjectileData> cachedProjectiles = new Seq<>();

    // Focus fire: shared target per attack slot
    private static final IntIntMap focusTargets = new IntIntMap();
    private static int lastFocusFrame = -1;
    
    public static int standoffBlocks() {
        return LunariansSettings.standoffBlocks;
    }
    
    public static void setStandoffBlocks(final int b) {
        LunariansSettings.standoffBlocks = Math.max(1, Math.min(40, b));
        LunariansSettings.save();
    }
    
    public static int maxStandoffTiles(final Unit unit) {
        if (unit == null || unit.type == null) {
            return 4;
        }
        return maxStandoffTilesType(unit.type);
    }
    
    public static int maxStandoffTilesType(final UnitType unitType) {
        if (unitType == null) {
            return 4;
        }
        return Math.max(1, Mathf.ceil(unitType.maxRange / 8.0f) + 2);
    }
    
    public static boolean regroupEnabled() {
        return LunariansSettings.regroupEnabled;
    }
    
    public static void setRegroupEnabled(final boolean b) {
        LunariansSettings.regroupEnabled = b;
        LunariansSettings.save();
    }
    
    public static int regroupSec() {
        return LunariansSettings.regroupSec;
    }
    
    public static void setRegroupSec(final int b) {
        LunariansSettings.regroupSec = Math.max(5, Math.min(120, b));
        LunariansSettings.save();
    }
    
    public static float standoffDist(final Unit unit, final AttackSlot attackSlot) {
        final int maxStandoffTiles = maxStandoffTilesType(unit.type);
        final int n = (attackSlot != null) ? attackSlot.standoffTiles : 0;
        return (float)(((n <= 0) ? maxStandoffTiles : Mathf.clamp(n, 1, maxStandoffTiles)) * 8);
    }
    
    public static float standoffDist(final Unit unit) {
        return standoffDist(unit, null);
    }
    
    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run((Object)EventType.Trigger.update, AutoAttack::update);
        if (!AutoAttack.drawReg) {
            AutoAttack.drawReg = true;
            Events.run((Object)EventType.Trigger.draw, AutoAttack::drawOverlay);
        }
        ChatCommands.init();
        Events.on((Class)EventType.WorldLoadEvent.class, worldLoadEvent -> reset());
        try {
            if (Vars.ui != null && Vars.ui.settings != null) {
                Vars.ui.settings.addCategory("Lunarians Attack", settingsTable -> {
                    label("lunarians-attack-standoff-blocks", "Attack distance", "Distancia de ataque");
                    label("lunarians-attack-regroup", "Regroup periodically", "Reagrupar periodicamente");
                    label("lunarians-attack-regroup-sec", "Regroup interval", "Intervalo de reagrupar");
                    label("lunarians-attack-smart", "Smart targets (turrets first)", "Alvos inteligentes (torres primeiro)");
                    label("lunarians-attack-smart-path", "Smart path (fewer turrets, safer route to core)", "Rota inteligente (menos torres, caminho seguro ate o nucleo)");
                    label("lunarians-attack-flee", "Retreat below HP % (global)", "Recuar abaixo de % de vida (global)");
                    label("lunarians-attack-formation", "Formation + gather before advancing", "Formacao + juntar antes de avancar");
                    label("lunarians-heal-escort", "Healer escort (open lane / shield)", "Escolta do curandeiro (abre caminho / protege)");
                    label("lunarians-attack-overlay", "Show zones and targets on map", "Mostrar zonas e alvos no mapa");
                    settingsTable.sliderPref("lunarians-attack-standoff-blocks", 3, 1, 40, 1, i -> i + " tiles");
                    settingsTable.checkPref("lunarians-attack-regroup", false);
                    settingsTable.sliderPref("lunarians-attack-regroup-sec", 5, 5, 60, 1, i -> i + "s");
                    settingsTable.checkPref("lunarians-attack-smart", true);
                    settingsTable.checkPref("lunarians-attack-smart-path", false);
                    settingsTable.sliderPref("lunarians-attack-flee", 0, 0, 90, 5, i -> (i == 0) ? tr("off", "desligado") : (i + "%"));
                    settingsTable.checkPref("lunarians-attack-formation", true);
                    settingsTable.checkPref("lunarians-heal-escort", true);
                    settingsTable.checkPref("lunarians-attack-overlay", true);
                    settingsTable.row();
                    label("lunarians-auto-dodge", "Enable projectile dodge", "Ativar desvio de projeteis");
                    label("lunarians-dodge-sensitivity", "Dodge sensitivity", "Sensibilidade do desvio");
                    settingsTable.checkPref("lunarians-auto-dodge", false);
                    settingsTable.sliderPref("lunarians-dodge-sensitivity", 10, 1, 30, 1, i -> String.format("%.1f", i / 10f));
                    settingsTable.row();
                    label("lunarians-focus-fire", "Enable focus fire (coordinated)", "Ativar foco coordenado");
                    label("lunarians-focus-range", "Focus fire range", "Alcance do foco coordenado");
                    settingsTable.checkPref("lunarians-focus-fire", false);
                    settingsTable.sliderPref("lunarians-focus-range", 600, 200, 2000, 50, i -> i + "m");
                    settingsTable.row();
                    label("lunarians-debug-visual", "Debug visual (colored unit squares)", "Debug visual (quadrados coloridos)");
                    settingsTable.checkPref("lunarians-debug-visual", false);
                    settingsTable.row();
                    settingsTable.button(tr("Profile: Defend (retreat 40%)", "Perfil: Defender (recua 40%)"), () -> profile(1, 40, 1)).growX().height(40.0f).row();
                    settingsTable.button(tr("Profile: Balanced (retreat 25%)", "Perfil: Equilibrado (recua 25%)"), () -> profile(1, 25, 1)).growX().height(40.0f).row();
                    settingsTable.button(tr("Profile: Rush (no retreat)", "Perfil: Rush (sem recuo)"), () -> profile(0, 0, 0)).growX().height(40.0f).row();
                });
            }
        }
        catch (final Throwable t) {}
    }
    
    public static void reset() {
        AutoAttack.timer = 0.0f;
        AutoAttack.count = 0;
        AutoAttack.lastHp.clear();
        AutoAttack.dpsEst.clear();
        AutoAttack.claimed.clear();
        AutoAttack.lastScanTime = -1.0f;
        AutoAttack.lastRegroupAt = 0.0f;
        AutoAttack.fleeing.clear();
        AutoAttack.enemyB.clear();
        AutoAttack.fleeStart.clear();
        AutoAttack.gatherSince.clear();
        AutoAttack.formLayerCount.clear();
        AutoAttack.formLayerTime.clear();
        AutoAttack.lines.clear();
        AutoAttack.shown.clear();
    }
    
    private static void update() {
        if (!LunariansSettings.enableAutoAttack || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
            return;
        }
        AutoAttack.timer += Time.delta / 60.0f;
        if (AutoAttack.timer < LunariansSettings.actionInterval(0.7f)) {
            return;
        }
        final float timer = AutoAttack.timer;
        AutoAttack.timer = 0.0f;
        try {
            scan(timer);
        }
        catch (final Throwable t) {
            if (!AutoAttack.errorLogged) {
                AutoAttack.errorLogged = true;
                Log.err("[Lunarians] AutoAttack error", t);
            }
        }
    }
    
    private static Unit nearestHealer(final Unit unit) {
        Unit unit2 = null;
        float n = Float.MAX_VALUE;
        for (int i = 0; i < Groups.unit.size(); ++i) {
            final Unit unit3 = (Unit)Groups.unit.index(i);
            if (unit3 != null && unit3.isValid() && unit3.team == Vars.player.team()) {
                if (AutoUnitHeal.isActiveHealer(unit3.id)) {
                    final float dst = unit.dst((Position)unit3);
                    if (dst < n) {
                        n = dst;
                        unit2 = unit3;
                    }
                }
            }
        }
        return unit2;
    }
    
    private static boolean isCore(final Building building) {
        return building != null && building.block instanceof CoreBlock;
    }
    
    private static float coreSafeDist(final Building building) {
        return building.block.size * 8 * 0.7f + 48.0f + 24.0f;
    }
    
    private static void trackDamage(final Unit unit, final float n) {
        final float max = Math.max(0.0f, AutoAttack.lastHp.get(unit.id, unit.health) - unit.health);
        AutoAttack.dpsEst.put(unit.id, AutoAttack.dpsEst.get(unit.id, 0.0f) * 0.55f + ((n > 0.05f) ? (max / n) : 0.0f) * 0.45f);
        AutoAttack.lastHp.put(unit.id, unit.health);
    }
    
    private static boolean isHighDamage(final Unit unit) {
        return AutoAttack.dpsEst.get(unit.id, 0.0f) > Math.max(4.0f, unit.maxHealth * 0.08f);
    }
    
    private static boolean groupLowDamage(final Seq<Unit> seq) {
        if (seq.isEmpty()) {
            return true;
        }
        int n = 0;
        for (int i = 0; i < seq.size; ++i) {
            if (isHighDamage((Unit)seq.get(i))) {
                ++n;
            }
        }
        return n * 2 <= seq.size;
    }
    
    private static void ensureMove(final Unit unit) {
        if (!(unit.controller() instanceof CommandAI)) {
            return;
        }
        if (((CommandAI)unit.controller()).command != UnitCommand.moveCommand) {
            Call.setUnitCommand(Vars.player, new int[] { unit.id }, UnitCommand.moveCommand);
        }
    }
    
    private static float turretThreatAt(final Unit unit, final float wx, final float wy) {
        if (!smartPath() || cachedTurrets == null || cachedTurrets.isEmpty()) return 0f;
        float threat = 0f;
        try {
            final int tx = Mathf.floor(wx / 8f), ty = Mathf.floor(wy / 8f);
            if (!unit.isFlying() && PathSafe.isBadFor(unit, tx, ty)) threat += 45f;
            // Early exit: if no turrets in range at all, return early
            boolean anyNear = false;
            for (int i = 0; i < cachedTurrets.size; ++i) {
                final TurretData td = cachedTurrets.get(i);
                final float dx = wx - td.x;
                final float dy = wy - td.y;
                if (dx * dx + dy * dy < td.rangeSq * 1.44f) { // 1.2^2 = 1.44
                    anyNear = true;
                    break;
                }
            }
            if (!anyNear) return threat; // only tile penalty
            for (int i = 0; i < cachedTurrets.size; ++i) {
                final TurretData td = cachedTurrets.get(i);
                final float dx = wx - td.x;
                final float dy = wy - td.y;
                final float d2 = dx * dx + dy * dy;
                if (d2 < td.rangeSq * 1.44f) {
                    final float d = Mathf.sqrt(d2);
                    threat += (1f - Mathf.clamp(d / (td.range * 1.2f))) * td.threatFactor;
                }
            }
        } catch (Throwable ignored) {}
        return threat;
    }

    private static float pathDanger(final Unit unit, final float x1, final float y1, final float x2, final float y2) {
        if (!smartPath()) return 0f;
        float d = 0f;
        final int steps = 5;
        for (int i = 1; i <= steps; ++i) {
            final float t = i / (float)steps;
            d += turretThreatAt(unit, Mathf.lerp(x1, x2, t), Mathf.lerp(y1, y2, t));
        }
        return d / (float)steps;
    }

    private static Vec2 smartApproach(final Unit unit, final Building building, final float standoff, final boolean coreTarget, final float formationOffsetRad) {
        final float baseAng = building.angleTo((Position)unit) * 0.017453292f;
        float baseDist = standoff * 0.9f;
        if (coreTarget && isCore(building)) {
            baseDist = Math.max(baseDist, coreSafeDist(building) * 0.95f);
        }
        if (!smartPath()) {
            final float ang = baseAng + formationOffsetRad;
            return PathSafe.safeNear(unit, building.x + Mathf.cos(ang) * baseDist, building.y + Mathf.sin(ang) * baseDist, 64.0f);
        }
        float bestScore = Float.MAX_VALUE;
        float bestX = building.x + Mathf.cos(baseAng + formationOffsetRad) * baseDist;
        float bestY = building.y + Mathf.sin(baseAng + formationOffsetRad) * baseDist;
        final int samples = 8; // Reduced from 12 for performance
        for (int i = 0; i < samples; ++i) {
            final float ang = baseAng + formationOffsetRad + (i - samples / 2f) * ((float)Math.PI * 2f / 18f);
            final float dist = baseDist * (1f + ((i % 3 == 0) ? 0.18f : 0f));
            final float cx = building.x + Mathf.cos(ang) * dist;
            final float cy = building.y + Mathf.sin(ang) * dist;
            final Vec2 c = PathSafe.safeNear(unit, cx, cy, 72.0f);
            final float cx2 = c.x, cy2 = c.y;
            final float threat = turretThreatAt(unit, cx2, cy2);
            final float pdan = pathDanger(unit, unit.x, unit.y, cx2, cy2);
            final float toObj = Mathf.dst(cx2, cy2, building.x, building.y);
            final float objScore = Math.max(0f, 140f - toObj * 0.12f);
            final float turn = Mathf.dst(cx2, cy2, bestX, bestY) * 0.01f;
            float score = threat * 2.4f + pdan * 1.7f + turn - objScore * 0.4f;
            if (coreTarget) score -= Math.max(0f, (baseDist - toObj)) * 0.05f;
            if (score < bestScore) { bestScore = score; bestX = cx2; bestY = cy2; }
        }
        return new Vec2(bestX, bestY);
    }

    private static void commandBuild(final Unit unit, final Building building, final float standoff, final boolean coreTarget) {
        ensureMove(unit);
        final Vec2 vec2 = smartApproach(unit, building, standoff, coreTarget, 0f);
        Vec2 finalVec = getDodgeOffset(unit, vec2);
        UnitCoord.cmd(unit, 1, 90.0f, building, (Unit)null, finalVec);
    }

    private static void holdAwayFromCore(final Unit unit, final Building building, final float n) {
        ensureMove(unit);
        final float max = Math.max(n * 0.95f, coreSafeDist(building));
        final float n2 = building.angleTo((Position)unit) * 0.017453292f;
        Vec2 base = PathSafe.safeNear(unit, building.x + Mathf.cos(n2) * max, building.y + Mathf.sin(n2) * max, 80.0f);
        Vec2 finalVec = getDodgeOffset(unit, base);
        UnitCoord.cmd(unit, 1, 90.0f, (Building)null, (Unit)null, finalVec);
    }

    private static void commandBuildFormation(final Unit unit, final Building building, final float standoff, final float n2, final int n3) {
        ensureMove(unit);
        final float formationOffset = (n3 <= 1) ? 0.0f : ((n2 - (n3 - 1) * 0.5f) * 0.35f);
        final Vec2 vec2 = smartApproach(unit, building, standoff, isCore(building), formationOffset);
        Vec2 finalVec = getDodgeOffset(unit, vec2);
        UnitCoord.cmd(unit, 1, 90.0f, building, (Unit)null, finalVec);
    }
    
    private static void regroupSlot(final Seq<Unit> seq) {
        if (seq.size < 2) {
            return;
        }
        float n = 0.0f;
        float n2 = 0.0f;
        for (int i = 0; i < seq.size; ++i) {
            n += ((Unit)seq.get(i)).x;
            n2 += ((Unit)seq.get(i)).y;
        }
        final float n3 = n / seq.size;
        final float n4 = n2 / seq.size;
        for (int j = 0; j < seq.size; ++j) {
            final Unit unit = (Unit)seq.get(j);
            ensureMove(unit);
            final float n5 = j * 360.0f / seq.size * 0.017453292f;
            UnitCoord.cmd(unit, 1, 90.0f, (Building)null, (Unit)null, PathSafe.safeNear(unit, n3 + Mathf.cos(n5) * 16.0f, n4 + Mathf.sin(n5) * 16.0f, 48.0f));
            ++AutoAttack.count;
        }
    }
    
    private static void scan(final float n) {
        final Seq attackSlots = LunariansSettings.attackSlots;
        if (attackSlots.isEmpty()) {
            return;
        }
        AutoAttack.count = 0;
        AutoAttack.lines.clear();
        AutoAttack.claimed.clear();
        final int size = Groups.unit.size();
        AutoAttack.enemyB.clear();
        for (final Building building : Groups.build) {
            if (building != null && building.isValid() && building.team != Vars.player.team()) {
                if (building.team == Team.derelict) {
                    continue;
                }
                AutoAttack.enemyB.add(building);
            }
        }
        // Cache turret data for smart path optimization
        AutoAttack.cachedTurrets.clear();
        int scanFrame = (int)Time.time;
        if (scanFrame != AutoAttack.lastTurretScan) {
            AutoAttack.lastTurretScan = scanFrame;
            for (final Building building : AutoAttack.enemyB) {
                if (building != null && building.isValid() && building.block instanceof Turret) {
                    float range = 80f;
                    try { range = ((Turret)building.block).range; } catch (Throwable ignored) { range = 80f; }
                    if (range > 0f) {
                        float threatFactor = 20f;
                        AutoAttack.cachedTurrets.add(new AutoAttack.TurretData(building.x, building.y, range, threatFactor));
                    }
                }
            }
        }
        // Cache projectile data for dodge
        AutoAttack.cachedProjectiles.clear();
        for (final Bullet bullet : Groups.bullet) {
            if (bullet != null && bullet.team() != Vars.player.team()) {
                AutoAttack.cachedProjectiles.add(new AutoAttack.ProjectileData(bullet.x, bullet.y, bullet.vel.x, bullet.vel.y, bullet.type.damage, bullet.type.speed));
            }
        }
        int n2 = 0;
        if (!regroupEnabled() || Time.time - AutoAttack.lastRegroupAt < regroupSec() * 60.0f) {
            n2 = 0;
        }
        final int n3 = n2;
        if (n3 != 0) {
            AutoAttack.lastRegroupAt = Time.time;
        }
        for (int i = 0; i < attackSlots.size; ++i) {
            final AttackSlot attackSlot = (AttackSlot)attackSlots.get(i);
            if (attackSlot.ready()) {
                final Seq seq = new Seq();
                for (int j = 0; j < size; ++j) {
                    final Unit unit = (Unit)Groups.unit.index(j);
                    if (unit != null && unit.isValid() && unit.team == Vars.player.team() && !AutoAttack.claimed.contains(unit.id) && UnitCoord.holder(unit.id) != 2 && UnitCoord.holder(unit.id) != 3 && !AutoUnitHeal.isActiveHealer(unit.id) && !AutoUnitHeal.isHealerType(unit.type) && attackSlot.unit.equals(unit.type.name)) {
                        if (unit.controller() instanceof CommandAI) {
                            seq.add((Object)unit);
                        }
                    }
                }
                if (attackSlot.maxUnits > 0 && seq.size > attackSlot.maxUnits) {
                    seq.truncate(attackSlot.maxUnits);
                }
                if (!seq.isEmpty()) {
                    for (int k = 0; k < seq.size; ++k) {
                        AutoAttack.claimed.add(((Unit)seq.get(k)).id);
                        trackDamage((Unit)seq.get(k), n);
                    }
                    final CoreBlock.CoreBuild closestCore = Vars.player.closestCore();
                    for (int l = seq.size - 1; l >= 0; --l) {
                        final Unit unit2 = (Unit)seq.get(l);
                        final float hpPct = unit2.health / Math.max(1.0f, unit2.maxHealth) * 100.0f;
                        final int unitFleePct = attackSlot.retreatPercent > 0 ? attackSlot.retreatPercent : fleePercent();
                        int contains = AutoAttack.fleeing.contains(unit2.id) ? 1 : 0;
                        if (contains == 0 && hpPct < unitFleePct) {
                            AutoAttack.fleeing.add(unit2.id);
                            AutoAttack.fleeStart.put(unit2.id, Time.time);
                            contains = 1;
                        }
                        else if (contains != 0 && (hpPct >= 80.0f || Time.time - AutoAttack.fleeStart.get(unit2.id, 0.0f) > 3600.0f)) {
                            AutoAttack.fleeing.remove(unit2.id);
                            contains = 0;
                        }
                        if (contains != 0) {
                            final Unit unit3 = LunariansSettings.autoAttackHeal ? nearestHealer(unit2) : null;
                            if (unit3 != null) {
                                if (UnitCoord.cmd(unit2, 4, 90.0f, (Building)null, (Unit)null, PathSafe.safeNear(unit2, unit3.x, unit3.y, 48.0f))) {
                                    ++AutoAttack.count;
                                }
                            }
                            else if (closestCore != null && unit2.dst((Position)closestCore) > 96.0f && UnitCoord.cmd(unit2, 4, 90.0f, (Building)null, (Unit)null, PathSafe.safeNear(unit2, ((Building)closestCore).x + 32.0f, ((Building)closestCore).y + 32.0f, 64.0f))) {
                                ++AutoAttack.count;
                            }
                            seq.remove(l);
                        }
                    }
                    if (seq.isEmpty()) {
                        continue;
                    }
                    if (n3 != 0) {
                        regroupSlot((Seq<Unit>)seq);
                    }
                    else {
                        float n5 = attackSlot.hasZone() ? (attackSlot.x * 8 + 4.0f) : ((Unit)seq.first()).x;
                        float y = 0.0f;
                        if (attackSlot.hasZone()) {
                            y = attackSlot.y * 8 + 4.0f;
                        } else {
                            y = ((Unit)seq.first()).y;
                        }
                        float n7 = y;
                        if (!attackSlot.hasZone()) {
                            float n8 = 0.0f;
                            float n9 = 0.0f;
                            for (int n10 = 0; n10 < seq.size; ++n10) {
                                n8 += ((Unit)seq.get(n10)).x;
                                n9 += ((Unit)seq.get(n10)).y;
                            }
                            n5 = n8 / seq.size;
                            n7 = n9 / seq.size;
                        }
                        final Seq seq2 = new Seq();
                        boolean b = false;
                        for (final Building building2 : AutoAttack.enemyB) {
                            if (attackSlot.hasZone() && Mathf.dst(n5, n7, building2.x, building2.y) > attackSlot.zoneRadius * 8.0f) {
                                continue;
                            }
                            seq2.add((Object)building2);
                            if (!(building2.block instanceof Turret)) {
                                continue;
                            }
                            b = true;
                        }
                        final boolean smartTargets = smartTargets();
                        Building building3 = null;
                        Building building4 = null;
                        float a = Float.MAX_VALUE;
                        float n11 = Float.MAX_VALUE;
                        float b2 = Float.MAX_VALUE;
                        for (int n12 = 0; n12 < seq2.size; ++n12) {
                            final Building building5 = (Building)seq2.get(n12);
                            float n13 = 0.0f;
                            for (int n14 = 0; n14 < seq.size; ++n14) {
                                n13 += ((Unit)seq.get(n14)).dst((Position)building5);
                            }
                            final float n15 = n13 / Math.max(1, seq.size);
                            if (isCore(building5)) {
                                if (n15 < a) {
                                    a = n15;
                                    building3 = building5;
                                }
                            }
                            else {
                                float n16 = n15;
                                if (smartTargets) {
                                    final boolean b3 = building5.block instanceof Wall || building5.block.group == BlockGroup.transportation;
                                    if (building5.block instanceof Turret) {
                                        if (n15 < 640.0f) {
                                            n16 -= 240.0f;
                                        }
                                    }
                                    else if (building5.block instanceof PowerGenerator) {
                                        n16 -= 96.0f;
                                    }
                                    else if (building5.block instanceof UnitFactory || building5.block instanceof GenericCrafter) {
                                        n16 -= 64.0f;
                                    }
                                    else if (b3) {
                                        n16 += (b ? 800.0f : 160.0f);
                                    }
                                }
                                if (n16 < n11) {
                                    n11 = n16;
                                    b2 = n15;
                                    building4 = building5;
                                }
                            }
                        }
                        final float min = Math.min(a, b2);
                        if (formationOn() && !attackSlot.hasZone() && seq.size > 1 && min > 240.0f) {
                            float max = 0.0f;
                            for (int n17 = 0; n17 < seq.size; ++n17) {
                                max = Math.max(max, ((Unit)seq.get(n17)).dst(n5, n7));
                            }
                            if (max > 144.0f) {
                                float n18 = AutoAttack.gatherSince.get(i, -1.0f);
                                if (n18 < 0.0f) {
                                    AutoAttack.gatherSince.put(i, Time.time);
                                    n18 = Time.time;
                                }
                                if (Time.time - n18 < 600.0f) {
                                    regroupSlot((Seq<Unit>)seq);
                                    continue;
                                }
                            }
                            else {
                                AutoAttack.gatherSince.put(i, -1.0f);
                            }
                        }
                        if (overlayOn()) {
                            if (building4 != null) {
                                AutoAttack.lines.add(new float[] { n5, n7, building4.x, building4.y, 0.0f });
                            }
                            if (building3 != null) {
                                AutoAttack.lines.add(new float[] { n5, n7, building3.x, building3.y, 1.0f });
                            }
                            if (attackSlot.hasZone()) {
                                AutoAttack.lines.add(new float[] { attackSlot.x * 8 + 4.0f, attackSlot.y * 8 + 4.0f, attackSlot.zoneRadius * 8.0f, 0.0f, 2.0f });
                            }
                        }
                        // Focus fire: assign shared target for this slot
                        int slotFocusTargetId = -1;
                        if (enableFocusFire() && building4 != null) {
                            int existing = AutoAttack.focusTargets.get(i, -1);
                            if (existing >= 0) {
                                Building existingB = (Building)Groups.build.getByID(existing);
                                if (existingB != null && existingB.isValid() && existingB.team != Vars.player.team() && existingB.dst(n5, n7) <= focusFireRange()) {
                                    slotFocusTargetId = existing;
                                } else {
                                    AutoAttack.focusTargets.remove(i);
                                }
                            }
                            if (slotFocusTargetId == -1 && building4 != null) {
                                AutoAttack.focusTargets.put(i, building4.id);
                                slotFocusTargetId = building4.id;
                            }
                        } else {
                            AutoAttack.focusTargets.remove(i);
                        }
                        final int focusTargetId = slotFocusTargetId;
                        seq.sort(u -> -((Unit)u).maxHealth);
                        final Unit unit4 = (building3 != null) ? pickCoreBreaker((Seq<Unit>)seq, building3) : null;
                        final boolean groupLowDamage = groupLowDamage((Seq<Unit>)seq);
                        for (int n19 = 0; n19 < seq.size; ++n19) {
                            final Unit unit5 = (Unit)seq.get(n19);
                            final float standoffDist = standoffDist(unit5, attackSlot);
                            if (building3 != null && isHighDamage(unit5) && !groupLowDamage) {
                                holdAwayFromCore(unit5, building3, standoffDist);
                                ++AutoAttack.count;
                            }
                            else if (unit5 == unit4 && building3 != null && groupLowDamage) {
                                commandBuild(unit5, building3, standoffDist, true);
                                ++AutoAttack.count;
                            }
                            else if (building4 != null) {
                                Building focusTarget = null;
                                if (focusTargetId >= 0) {
                                    focusTarget = (Building)Groups.build.getByID(focusTargetId);
                                    if (focusTarget == null || !focusTarget.isValid() || focusTarget.team == Vars.player.team() || focusTarget.dst(unit5.x, unit5.y) > focusFireRange()) {
                                        focusTarget = building4; // fallback
                                    }
                                } else {
                                    focusTarget = building4;
                                }
                                Building targetBuilding = focusTarget;
                                if (unit5.dst(n5, n7) > 40.0f * 1.35f + 40.0f) {
                                    ensureMove(unit5);
                                    final float n20 = n19 * 360.0f / seq.size * 0.017453292f;
                                    UnitCoord.cmd(unit5, 1, 90.0f, (Building)null, (Unit)null, new Vec2(n5 + Mathf.cos(n20) * 18.0f, n7 + Mathf.sin(n20) * 18.0f));
                                    ++AutoAttack.count;
                                }
                                else {
                                    // Layer-by-layer auto formation: release layers progressively instead of all at once
                                    if (formationOn() && seq.size > 1) {
                                        final int perLayer = 4;
                                        final int maxLayers = (seq.size + perLayer - 1) / perLayer;
                                        int released = AutoAttack.formLayerCount.get(i, 1);
                                        float lastT = AutoAttack.formLayerTime.get(i, -1.0f);
                                        if (lastT < 0.0f) { AutoAttack.formLayerTime.put(i, Time.time); lastT = Time.time; }
                                        if (released < maxLayers && Time.time - lastT > 150.0f) {
                                            released++;
                                            AutoAttack.formLayerCount.put(i, released);
                                            AutoAttack.formLayerTime.put(i, Time.time);
                                        }
                                        final int layerIdx = n19 / perLayer;
                                        if (layerIdx < released) {
                                            Building targetB = building4;
                                            if (focusTargetId >= 0) {
                                                Building ft = (Building)Groups.build.getByID(focusTargetId);
                                                if (ft != null && ft.isValid()) targetB = ft;
                                            }
                                            commandBuildFormation(unit5, targetB, standoffDist, (((n19 == 0) ? 0 : (((n19 & 0x1) == 0x1) ? ((n19 + 1) / 2) : (-n19 / 2))) + (seq.size - 1) * 0.5f), seq.size);
                                            ++AutoAttack.count;
                                        } else {
                                            // hold back: wait as next layer behind the front, near group anchor
                                            ensureMove(unit5);
                                            Building targetB = building4;
                                            if (focusTargetId >= 0) {
                                                Building ft = (Building)Groups.build.getByID(focusTargetId);
                                                if (ft != null && ft.isValid()) targetB = ft;
                                            }
                                            final float holdAng = targetB.angleTo((Position)unit5) * 0.017453292f + (float)Math.PI;
                                            final float holdR = 20.0f + (layerIdx - released) * 14.0f + (n19 % perLayer) * 6.0f;
                                            final Vec2 hold = PathSafe.safeNear(unit5, n5 + Mathf.cos(holdAng) * holdR, n7 + Mathf.sin(holdAng) * holdR, 56.0f);
                                            UnitCoord.cmd(unit5, 1, 90.0f, (Building)null, (Unit)null, hold);
                                            ++AutoAttack.count;
                                        }
                                    } else {
                                        Building targetB = building4;
                                        if (focusTargetId >= 0) {
                                            Building ft = (Building)Groups.build.getByID(focusTargetId);
                                            if (ft != null && ft.isValid()) targetB = ft;
                                        }
                                        commandBuildFormation(unit5, targetB, standoffDist, formationOn() ? (((n19 == 0) ? 0 : (((n19 & 0x1) == 0x1) ? ((n19 + 1) / 2) : (-n19 / 2))) + (seq.size - 1) * 0.5f) : ((float)n19), seq.size);
                                        ++AutoAttack.count;
                                    }
                                }
                            }
                            else if (building3 != null) {
                                holdAwayFromCore(unit5, building3, standoffDist);
                                ++AutoAttack.count;
                            }
                        }
                    }
                }
            }
        }
        AutoAttack.shown.clear();
        AutoAttack.shown.addAll((Seq)AutoAttack.lines);
    }
    
    private static Unit pickCoreBreaker(final Seq<Unit> seq, final Building building) {
        Unit unit = null;
        float n = Float.MAX_VALUE;
        for (int i = 0; i < seq.size; ++i) {
            final Unit unit2 = (Unit)seq.get(i);
            if (!isHighDamage(unit2)) {
                final float n2;
                if ((n2 = unit2.dst((Position)building) - unit2.health / Math.max(1.0f, unit2.maxHealth) * 40.0f) < n) {
                    n = n2;
                    unit = unit2;
                }
            }
        }
        return (Unit)((unit != null) ? unit : seq.first());
    }
    
    public static boolean smartTargets() {
        return LunariansSettings.smartTargets;
    }
    
    public static boolean formationOn() {
        return LunariansSettings.formationOn;
    }
    
    public static boolean overlayOn() {
        return LunariansSettings.overlayOn;
    }

    public static boolean smartPath() {
        return LunariansSettings.smartPath;
    }

    private static Vec2 getDodgeOffset(Unit unit, Vec2 target) {
        if (target == null) return null;
        Vec2 dodge = calculateDodgeOffset(unit);
        if (dodge == null) return target;
        // Apply dodge offset to target
        float newX = target.x + dodge.x;
        float newY = target.y + dodge.y;
        return new Vec2(newX, newY);
    }
    
    public static int fleePercent() {
        return LunariansSettings.fleePercent;
    }

    public static boolean enableAutoDodge() {
        return LunariansSettings.enableAutoDodge;
    }

    public static float autoDodgeSensitivity() {
        return LunariansSettings.autoDodgeSensitivity / 10f;
    }

    public static boolean enableFocusFire() {
        return LunariansSettings.enableFocusFire;
    }

    public static float focusFireRange() {
        return LunariansSettings.focusFireRange;
    }

    public static boolean enableDebugVisual() {
        return LunariansSettings.enableDebugVisual;
    }

    // Projectile evasion: calculate dodge offset for a unit
    private static Vec2 calculateDodgeOffset(Unit unit) {
        if (!enableAutoDodge() || cachedProjectiles.isEmpty()) return null;
        Vec2 bestOffset = null;
        float bestScore = Float.MAX_VALUE;
        float sensitivity = autoDodgeSensitivity();
        float maxDodgeDist = unit.type.maxRange > 0 ? unit.type.maxRange * 0.5f : 80f;
        
        for (int i = 0; i < cachedProjectiles.size; ++i) {
            ProjectileData proj = cachedProjectiles.get(i);
            float dx = proj.x - unit.x;
            float dy = proj.y - unit.y;
            float dist = Mathf.sqrt(dx * dx + dy * dy);
            
            // Only dodge projectiles that are close and heading towards us
            if (dist > 400f) continue;
            
            // Projectile velocity towards unit
            float projVelX = proj.velX;
            float projVelY = proj.velY;
            float projSpeed = proj.speed;
            if (projSpeed <= 0) continue;
            
            // Time to impact
            float timeToImpact = dist / projSpeed;
            if (timeToImpact < 0.1f || timeToImpact > 5f) continue;
            
            // Predicted impact position
            float predX = proj.x + projVelX * timeToImpact;
            float predY = proj.y + projVelY * timeToImpact;
            float predDist = Mathf.dst(unit.x, unit.y, predX, predY);
            
            if (predDist < unit.hitSize * 2f) {
                // Need to dodge perpendicular to projectile velocity
                float perpX = -projVelY;
                float perpY = projVelX;
                float perpLen = Mathf.sqrt(perpX * perpX + perpY * perpY);
                if (perpLen > 0) {
                    perpX /= perpLen;
                    perpY /= perpLen;
                    
                    // Choose direction away from projectile
                    float dot = (unit.x - proj.x) * perpX + (unit.y - proj.y) * perpY;
                    if (dot < 0) { perpX = -perpX; perpY = -perpY; }
                    
                    float dodgeDist = Math.min(maxDodgeDist, 60f * sensitivity);
                    Vec2 offset = new Vec2(perpX * dodgeDist, perpY * dodgeDist);
                    
                    // Score: prefer dodges that move away from projectile
                    float score = -Mathf.dst(unit.x + offset.x, unit.y + offset.y, proj.x, proj.y);
                    if (score < bestScore) {
                        bestScore = score;
                        bestOffset = offset;
                    }
                }
            }
        }
        return bestOffset;
    }
    
    private static boolean pt() {
        return LunariansSettings.language != null && LunariansSettings.language.startsWith("pt");
    }
    
    private static String tr(final String s, final String s2) {
        return pt() ? s2 : s;
    }
    
    private static void label(final String str, final String s, final String s2) {
        try {
            Core.bundle.getProperties().put("setting." + str + ".name", tr(s, s2));
        }
        catch (final Throwable t) {}
    }
    
    private static void profile(final int n, final int i, final int n2) {
        Core.settings.put("lunarians-attack-smart", (Object)(n == 1));
        Core.settings.put("lunarians-attack-flee", (Object)i);
        Core.settings.put("lunarians-attack-formation", (Object)(n2 == 1));
        if (Vars.ui != null) {
            Vars.ui.showInfoToast(tr("Profile applied. Reopen this menu to see the values.", "Perfil aplicado. Reabra o menu para ver os valores."), 3.0f);
        }
    }
    
    private static void drawOverlay() {
        if (Vars.state == null || !Vars.state.isGame() || !LunariansSettings.enableAutoAttack) {
            return;
        }
        // overlayOn check removed - Auto Attack core logic runs regardless
        Draw.z(120.0f);
        for (int i = 0; i < AutoAttack.shown.size; ++i) {
            final float[] array = (float[])AutoAttack.shown.get(i);
            if (array[4] == 2.0f) {
                Draw.color(Pal.accent, 0.8f);
                Lines.stroke(1.5f);
                Lines.circle(array[0], array[1], array[2]);
            }
            else {
                Draw.color((array[4] == 1.0f) ? Pal.remove : Pal.lightishOrange, 0.8f);
                Lines.stroke(1.2f);
                Lines.line(array[0], array[1], array[2], array[3]);
                Lines.circle(array[2], array[3], 6.0f);
            }
        }
        // Debug visual: colored squares for units based on their module
        if (enableDebugVisual()) {
            drawDebugUnitSquares();
        }
        Draw.reset();
    }

    private static void drawDebugUnitSquares() {
        try {
            int size = Groups.unit.size();
            for (int i = 0; i < size; ++i) {
                Unit unit = (Unit)Groups.unit.index(i);
                if (unit == null || !unit.isValid() || unit.team != Vars.player.team() || !(unit.controller() instanceof CommandAI)) continue;
                
                CommandAI ai = (CommandAI)unit.controller();
                arc.graphics.Color color = null;
                String label = "";
                
                // Determine module based on command - use string comparison since enum constants may vary
                String cmdName = ai.command.toString().toLowerCase();
                if (cmdName.contains("attack")) {
                    color = arc.graphics.Color.red;
                    label = "ATTACK";
                } else if (cmdName.contains("move")) {
                    // Check if it's a formation move (has targetPos but no attack target)
                    if (ai.targetPos != null && ai.attackTarget == null) {
                        color = arc.graphics.Color.blue;
                        label = "MOVE";
                    } else {
                        color = arc.graphics.Color.cyan;
                        label = "FORMATION";
                    }
                } else if (cmdName.contains("repair")) {
                    color = arc.graphics.Color.green;
                    label = "HEAL/REPAIR";
                } else if (cmdName.contains("rally")) {
                    color = arc.graphics.Color.yellow;
                    label = "RALLY";
                } else {
                    continue; // skip unknown
                }
                
                // Draw transparent square
                Draw.color(color, 0.4f);
                float sqSize = unit.hitSize * 1.5f;
                Fill.square(unit.x, unit.y, sqSize);
                
                // Draw border
                Draw.color(color, 0.8f);
                Lines.stroke(1.5f);
                Lines.square(unit.x, unit.y, sqSize);
                
                // Draw label
                Fonts.outline.draw(label, unit.x, unit.y + unit.hitSize + 8f, color, 0.2f, false, 1);
            }
        } catch (Throwable ignored) {}
    }
    
    public static int count() {
        return AutoAttack.count;
    }
    
    static {
        claimed = new IntSet();
        lastHp = new IntFloatMap();
        dpsEst = new IntFloatMap();
        AutoAttack.lastScanTime = -1.0f;
        fleeing = new IntSet();
        fleeStart = new IntFloatMap();
        gatherSince = new IntFloatMap();
        formLayerCount = new IntIntMap();
        formLayerTime = new IntFloatMap();
        enemyB = new Seq();
        lines = new Seq();
        shown = new Seq();
    }
}
