package lunarians;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.func.Floatf;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.IntFloatMap;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import java.util.Iterator;
import lunarians.data.AttackSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.CommandAI;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.dialogs.SettingsMenuDialog;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.defense.turrets.Turret;
import mindustry.world.blocks.power.PowerGenerator;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.units.UnitFactory;
import mindustry.world.meta.BlockGroup;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoAttack.class */
public class AutoAttack {
    private static final float SCAN = 0.7f;
    private static float timer;
    private static int count;
    private static boolean errorLogged;
    private static float lastRegroupAt;
    public static final String KEY_STANDOFF = "lunarians-attack-standoff-blocks";
    public static final String KEY_REGROUP = "lunarians-attack-regroup";
    public static final String KEY_REGROUP_SEC = "lunarians-attack-regroup-sec";
    private static boolean drawReg;
    private static final IntSet claimed = new IntSet();
    private static final IntFloatMap lastHp = new IntFloatMap();
    private static final IntFloatMap dpsEst = new IntFloatMap();
    private static float lastScanTime = -1.0f;
    private static final IntSet fleeing = new IntSet();
    private static final IntFloatMap fleeStart = new IntFloatMap();
    private static final IntFloatMap gatherSince = new IntFloatMap();
    private static final Seq<Building> enemyB = new Seq<>();
    private static final Seq<float[]> lines = new Seq<>();
    private static final Seq<float[]> shown = new Seq<>();

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoAttack$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Floatf {
        public final float get(Object obj) {
            return AutoAttack.lambda$scan$8((Unit) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoAttack$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements SettingsMenuDialog.StringProcessor {
        public final String get(int i) {
            return AutoAttack.lambda$init$1(i);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoAttack$10, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass10 implements Cons {
        public final void get(Object obj) {
            AutoAttack.lambda$init$7((SettingsMenuDialog.SettingsTable) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoAttack$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements SettingsMenuDialog.StringProcessor {
        public final String get(int i) {
            return AutoAttack.lambda$init$2(i);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoAttack$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements SettingsMenuDialog.StringProcessor {
        public final String get(int i) {
            return AutoAttack.lambda$init$3(i);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoAttack$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass4 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoAttack.profile(1, 40, 1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoAttack$5, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass5 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoAttack.profile(1, 25, 1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoAttack$6, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass6 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoAttack.profile(0, 0, 0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoAttack$7, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass7 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoAttack.$r8$lambda$8jlK-L3WswuRMwxBMv4twpKwbcs();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoAttack$8, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass8 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoAttack.$r8$lambda$Jfj-tyPH4y5IDWhLNVvm8KWSapU();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoAttack$9, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass9 implements Cons {
        public final void get(Object obj) {
            AutoAttack.reset();
        }
    }

    public static int standoffBlocks() {
        return Math.max(1, Math.min(40, Core.settings.getInt(KEY_STANDOFF, 3)));
    }

    public static void setStandoffBlocks(int i) {
        Core.settings.put(KEY_STANDOFF, Integer.valueOf(Math.max(1, Math.min(40, i))));
    }

    public static int maxStandoffTiles(Unit unit) {
        if (unit == null || unit.type == null) {
            return 4;
        }
        return maxStandoffTiles(unit.type);
    }

    public static int maxStandoffTiles(UnitType unitType) {
        if (unitType == null) {
            return 4;
        }
        return Math.max(1, Mathf.ceil(unitType.maxRange / 8.0f) + 2);
    }

    public static boolean regroupEnabled() {
        return Core.settings.getBool(KEY_REGROUP, false);
    }

    public static void setRegroupEnabled(boolean z) {
        Core.settings.put(KEY_REGROUP, Boolean.valueOf(z));
    }

    public static int regroupSec() {
        return Math.max(5, Math.min(120, Core.settings.getInt(KEY_REGROUP_SEC, 5)));
    }

    public static void setRegroupSec(int i) {
        Core.settings.put(KEY_REGROUP_SEC, Integer.valueOf(Math.max(5, Math.min(120, i))));
    }

    public static float standoffDist(Unit unit, AttackSlot attackSlot) {
        int maxStandoffTiles = maxStandoffTiles(unit);
        return ((attackSlot != null ? attackSlot.standoffTiles : 0) <= 0 ? maxStandoffTiles : Mathf.clamp(r7, 1, maxStandoffTiles)) * 8;
    }

    public static float standoffDist(Unit unit) {
        return standoffDist(unit, null);
    }

    public static void init() {
        if (Vars.headless) {
            return;
        }
        Events.run(EventType.Trigger.update, AutoAttack::update);
        if (!drawReg) {
            drawReg = true;
            Events.run(EventType.Trigger.draw, AutoAttack::drawOverlay);
        }
        ChatCommands.init();
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            reset();
        });
        try {
            if (Vars.ui != null && Vars.ui.settings != null) {
                Vars.ui.settings.addCategory("Lunarians Attack", settingsTable -> {
                    label(KEY_STANDOFF, "Attack distance", "Distancia de ataque");
                    label(KEY_REGROUP, "Regroup periodically", "Reagrupar periodicamente");
                    label(KEY_REGROUP_SEC, "Regroup interval", "Intervalo de reagrupar");
                    label("lunarians-attack-smart", "Smart targets (turrets first)", "Alvos inteligentes (torres primeiro)");
                    label("lunarians-attack-flee", "Retreat below HP %", "Recuar abaixo de % de vida");
                    label("lunarians-attack-formation", "Formation + gather before advancing", "Formacao + juntar antes de avancar");
                    label("lunarians-heal-escort", "Healer escort (open lane / shield)", "Escolta do curandeiro (abre caminho / protege)");
                    label("lunarians-attack-overlay", "Show zones and targets on map", "Mostrar zonas e alvos no mapa");
                    settingsTable.sliderPref(KEY_STANDOFF, 3, 1, 40, 1, i -> {
                        return i + " tiles";
                    });
                    settingsTable.checkPref(KEY_REGROUP, false);
                    settingsTable.sliderPref(KEY_REGROUP_SEC, 5, 5, 60, 1, i2 -> {
                        return i2 + "s";
                    });
                    settingsTable.checkPref("lunarians-attack-smart", true);
                    settingsTable.sliderPref("lunarians-attack-flee", 0, 0, 90, 5, i3 -> {
                        return i3 == 0 ? tr("off", "desligado") : i3 + "%";
                    });
                    settingsTable.checkPref("lunarians-attack-formation", true);
                    settingsTable.checkPref("lunarians-heal-escort", true);
                    settingsTable.checkPref("lunarians-attack-overlay", true);
                    settingsTable.row();
                    settingsTable.button(tr("Profile: Defend (retreat 40%)", "Perfil: Defender (recua 40%)"), () -> {
                        profile(1, 40, 1);
                    }).growX().height(40.0f).row();
                    settingsTable.button(tr("Profile: Balanced (retreat 25%)", "Perfil: Equilibrado (recua 25%)"), () -> {
                        profile(1, 25, 1);
                    }).growX().height(40.0f).row();
                    settingsTable.button(tr("Profile: Rush (no retreat)", "Perfil: Rush (sem recuo)"), () -> {
                        profile(0, 0, 0);
                    }).growX().height(40.0f).row();
                });
            }
        } catch (Throwable th) {
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
        if (!LunariansSettings.enableAutoAttack || Vars.state == null || !Vars.state.isGame() || Vars.player == null) {
            return;
        }
        timer += Time.delta / 60.0f;
        if (timer < LunariansSettings.actionInterval(SCAN)) {
            return;
        }
        float f = timer;
        timer = 0.0f;
        try {
            scan(f);
        } catch (Throwable th) {
            if (!errorLogged) {
                errorLogged = true;
                Log.err("[Lunarians] AutoAttack error", th);
            }
        }
    }

    private static Unit nearestHealer(Unit unit) {
        Unit unit2 = null;
        float f = Float.MAX_VALUE;
        for (int i = 0; i < Groups.unit.size(); i++) {
            Unit unit3 = (Unit) Groups.unit.index(i);
            if (unit3 != null && unit3.isValid() && unit3.team == Vars.player.team() && AutoUnitHeal.isActiveHealer(unit3.id)) {
                float dst = unit.dst(unit3);
                if (dst < f) {
                    f = dst;
                    unit2 = unit3;
                }
            }
        }
        return unit2;
    }

    private static boolean isCore(Building building) {
        return building != null && (building.block instanceof CoreBlock);
    }

    private static float coreSafeDist(Building building) {
        return (building.block.size * 8 * SCAN) + 48.0f + 24.0f;
    }

    private static void trackDamage(Unit unit, float f) {
        dpsEst.put(unit.id, (dpsEst.get(unit.id, 0.0f) * 0.55f) + ((f > 0.05f ? Math.max(0.0f, lastHp.get(unit.id, unit.health) - unit.health) / f : 0.0f) * 0.45f));
        lastHp.put(unit.id, unit.health);
    }

    private static boolean isHighDamage(Unit unit) {
        return dpsEst.get(unit.id, 0.0f) > Math.max(4.0f, unit.maxHealth * 0.08f);
    }

    private static boolean groupLowDamage(Seq<Unit> seq) {
        if (seq.isEmpty()) {
            return true;
        }
        int i = 0;
        for (int i2 = 0; i2 < seq.size; i2++) {
            if (isHighDamage((Unit) seq.get(i2))) {
                i++;
            }
        }
        return i * 2 <= seq.size;
    }

    private static void ensureMove(Unit unit) {
        if ((unit.controller() instanceof CommandAI) && unit.controller().command != UnitCommand.moveCommand) {
            Call.setUnitCommand(Vars.player, new int[]{unit.id}, UnitCommand.moveCommand);
        }
    }

    private static void commandBuild(Unit unit, Building building, float f, boolean z) {
        ensureMove(unit);
        float angleTo = building.angleTo(unit) * 0.017453292f;
        float f2 = f * 0.9f;
        if (z && isCore(building)) {
            f2 = Math.max(f2, coreSafeDist(building) * 0.95f);
        }
        UnitCoord.cmd(unit, 1, 90.0f, building, null, PathSafe.safeNear(unit, building.x + (Mathf.cos(angleTo) * f2), building.y + (Mathf.sin(angleTo) * f2), 64.0f));
    }

    private static void holdAwayFromCore(Unit unit, Building building, float f) {
        ensureMove(unit);
        float max = Math.max(f * 0.95f, coreSafeDist(building));
        float angleTo = building.angleTo(unit) * 0.017453292f;
        UnitCoord.cmd(unit, 1, 90.0f, null, null, PathSafe.safeNear(unit, building.x + (Mathf.cos(angleTo) * max), building.y + (Mathf.sin(angleTo) * max), 80.0f));
    }

    private static void commandBuildFormation(Unit unit, Building building, float f, float f2, int i) {
        ensureMove(unit);
        float angleTo = (building.angleTo(unit) * 0.017453292f) + (i <= 1 ? 0.0f : (f2 - ((i - 1) * 0.5f)) * 0.35f);
        float f3 = f * 0.9f;
        UnitCoord.cmd(unit, 1, 90.0f, building, null, PathSafe.safeNear(unit, building.x + (Mathf.cos(angleTo) * f3), building.y + (Mathf.sin(angleTo) * f3), 64.0f));
    }

    private static void regroupSlot(Seq<Unit> seq) {
        if (seq.size < 2) {
            return;
        }
        float f = 0.0f;
        float f2 = 0.0f;
        for (int i = 0; i < seq.size; i++) {
            f += ((Unit) seq.get(i)).x;
            f2 += ((Unit) seq.get(i)).y;
        }
        float f3 = f / seq.size;
        float f4 = f2 / seq.size;
        for (int i2 = 0; i2 < seq.size; i2++) {
            Unit unit = (Unit) seq.get(i2);
            ensureMove(unit);
            float f5 = ((i2 * 360.0f) / seq.size) * 0.017453292f;
            UnitCoord.cmd(unit, 1, 90.0f, null, null, PathSafe.safeNear(unit, f3 + (Mathf.cos(f5) * 16.0f), f4 + (Mathf.sin(f5) * 16.0f), 48.0f));
            count++;
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
        int size = Groups.unit.size();
        enemyB.clear();
        Iterator it = Groups.build.iterator();
        while (it.hasNext()) {
            Building building = (Building) it.next();
            if (building != null && building.isValid() && building.team != Vars.player.team() && building.team != Team.derelict) {
                enemyB.add(building);
            }
        }
        boolean z = regroupEnabled() && Time.time - lastRegroupAt >= ((float) regroupSec()) * 60.0f;
        if (z) {
            lastRegroupAt = Time.time;
        }
        for (int i = 0; i < seq.size; i++) {
            AttackSlot attackSlot = (AttackSlot) seq.get(i);
            if (attackSlot.ready()) {
                Seq seq2 = new Seq();
                for (int i2 = 0; i2 < size; i2++) {
                    Unit index = Groups.unit.index(i2);
                    if (index != null && index.isValid() && index.team == Vars.player.team() && !claimed.contains(index.id) && UnitCoord.holder(index.id) != 2 && UnitCoord.holder(index.id) != 3 && !AutoUnitHeal.isActiveHealer(index.id) && !AutoUnitHeal.isHealerType(index.type) && attackSlot.unit.equals(index.type.name) && (index.controller() instanceof CommandAI)) {
                        seq2.add(index);
                    }
                }
                if (attackSlot.maxUnits > 0 && seq2.size > attackSlot.maxUnits) {
                    seq2.truncate(attackSlot.maxUnits);
                }
                if (!seq2.isEmpty()) {
                    for (int i3 = 0; i3 < seq2.size; i3++) {
                        claimed.add(((Unit) seq2.get(i3)).id);
                        trackDamage((Unit) seq2.get(i3), f);
                    }
                    int fleePercent = fleePercent();
                    if (fleePercent > 0) {
                        CoreBlock.CoreBuild closestCore = Vars.player.closestCore();
                        for (int i4 = seq2.size - 1; i4 >= 0; i4--) {
                            Unit unit = (Unit) seq2.get(i4);
                            float max = unit.health / Math.max(1.0f, unit.maxHealth);
                            boolean contains = fleeing.contains(unit.id);
                            if (!contains && max * 100.0f < fleePercent) {
                                fleeing.add(unit.id);
                                fleeStart.put(unit.id, Time.time);
                                contains = true;
                            } else if (contains && (max >= 0.8f || Time.time - fleeStart.get(unit.id, 0.0f) > 3600.0f)) {
                                fleeing.remove(unit.id);
                                contains = false;
                            }
                            if (contains) {
                                Unit nearestHealer = LunariansSettings.autoAttackHeal ? nearestHealer(unit) : null;
                                if (nearestHealer != null) {
                                    if (UnitCoord.cmd(unit, 4, 90.0f, null, null, PathSafe.safeNear(unit, nearestHealer.x, nearestHealer.y, 48.0f))) {
                                        count++;
                                    }
                                } else if (closestCore != null && unit.dst(closestCore) > 96.0f && UnitCoord.cmd(unit, 4, 90.0f, null, null, PathSafe.safeNear(unit, ((Building) closestCore).x + 32.0f, ((Building) closestCore).y + 32.0f, 64.0f))) {
                                    count++;
                                }
                                seq2.remove(i4);
                            }
                        }
                        if (seq2.isEmpty()) {
                        }
                    }
                    if (z) {
                        regroupSlot(seq2);
                    } else {
                        float f2 = attackSlot.hasZone() ? (attackSlot.x * 8) + 4.0f : ((Unit) seq2.first()).x;
                        float f3 = attackSlot.hasZone() ? (attackSlot.y * 8) + 4.0f : ((Unit) seq2.first()).y;
                        if (!attackSlot.hasZone()) {
                            float f4 = 0.0f;
                            float f5 = 0.0f;
                            for (int i5 = 0; i5 < seq2.size; i5++) {
                                f4 += ((Unit) seq2.get(i5)).x;
                                f5 += ((Unit) seq2.get(i5)).y;
                            }
                            f2 = f4 / seq2.size;
                            f3 = f5 / seq2.size;
                        }
                        Seq seq3 = new Seq();
                        boolean z2 = false;
                        Iterator it2 = enemyB.iterator();
                        while (it2.hasNext()) {
                            Building building2 = (Building) it2.next();
                            if (!attackSlot.hasZone() || Mathf.dst(f2, f3, building2.x, building2.y) <= attackSlot.zoneRadius * 8.0f) {
                                seq3.add(building2);
                                if (building2.block instanceof Turret) {
                                    z2 = true;
                                }
                            }
                        }
                        boolean smartTargets = smartTargets();
                        Building building3 = null;
                        Building building4 = null;
                        float f6 = Float.MAX_VALUE;
                        float f7 = Float.MAX_VALUE;
                        float f8 = Float.MAX_VALUE;
                        for (int i6 = 0; i6 < seq3.size; i6++) {
                            Building building5 = (Building) seq3.get(i6);
                            float f9 = 0.0f;
                            for (int i7 = 0; i7 < seq2.size; i7++) {
                                f9 += ((Unit) seq2.get(i7)).dst(building5);
                            }
                            float max2 = f9 / Math.max(1, seq2.size);
                            if (!isCore(building5)) {
                                float f10 = max2;
                                if (smartTargets) {
                                    boolean z3 = (building5.block instanceof Wall) || building5.block.group == BlockGroup.transportation;
                                    if (building5.block instanceof Turret) {
                                        if (max2 < 640.0f) {
                                            f10 -= 240.0f;
                                        }
                                    } else if (building5.block instanceof PowerGenerator) {
                                        f10 -= 96.0f;
                                    } else if ((building5.block instanceof UnitFactory) || (building5.block instanceof GenericCrafter)) {
                                        f10 -= 64.0f;
                                    } else if (z3) {
                                        f10 += z2 ? 800.0f : 160.0f;
                                    }
                                }
                                if (f10 < f7) {
                                    f7 = f10;
                                    f8 = max2;
                                    building4 = building5;
                                }
                            } else if (max2 < f6) {
                                f6 = max2;
                                building3 = building5;
                            }
                        }
                        float min = Math.min(f6, f8);
                        if (formationOn() && !attackSlot.hasZone() && seq2.size > 1 && min > 240.0f) {
                            float f11 = 0.0f;
                            for (int i8 = 0; i8 < seq2.size; i8++) {
                                f11 = Math.max(f11, ((Unit) seq2.get(i8)).dst(f2, f3));
                            }
                            if (f11 > 144.0f) {
                                float f12 = gatherSince.get(i, -1.0f);
                                if (f12 < 0.0f) {
                                    gatherSince.put(i, Time.time);
                                    f12 = Time.time;
                                }
                                if (Time.time - f12 < 600.0f) {
                                    regroupSlot(seq2);
                                }
                            } else {
                                gatherSince.put(i, -1.0f);
                            }
                        }
                        if (overlayOn()) {
                            if (building4 != null) {
                                lines.add(new float[]{f2, f3, building4.x, building4.y, 0.0f});
                            }
                            if (building3 != null) {
                                lines.add(new float[]{f2, f3, building3.x, building3.y, 1.0f});
                            }
                            if (attackSlot.hasZone()) {
                                lines.add(new float[]{(attackSlot.x * 8) + 4.0f, (attackSlot.y * 8) + 4.0f, attackSlot.zoneRadius * 8.0f, 0.0f, 2.0f});
                            }
                        }
                        seq2.sort(unit2 -> {
                            return -unit2.maxHealth;
                        });
                        Unit pickCoreBreaker = building3 != null ? pickCoreBreaker(seq2, building3) : null;
                        boolean groupLowDamage = groupLowDamage(seq2);
                        int i9 = 0;
                        while (i9 < seq2.size) {
                            Unit unit3 = (Unit) seq2.get(i9);
                            float standoffDist = standoffDist(unit3, attackSlot);
                            if (building3 != null && isHighDamage(unit3) && !groupLowDamage) {
                                holdAwayFromCore(unit3, building3, standoffDist);
                                count++;
                            } else if (unit3 == pickCoreBreaker && building3 != null && groupLowDamage) {
                                commandBuild(unit3, building3, standoffDist, true);
                                count++;
                            } else if (building4 != null) {
                                if (unit3.dst(f2, f3) > (40.0f * 1.35f) + 40.0f) {
                                    ensureMove(unit3);
                                    float f13 = ((i9 * 360.0f) / seq2.size) * 0.017453292f;
                                    UnitCoord.cmd(unit3, 1, 90.0f, null, null, new Vec2(f2 + (Mathf.cos(f13) * 18.0f), f3 + (Mathf.sin(f13) * 18.0f)));
                                    count++;
                                } else {
                                    commandBuildFormation(unit3, building4, standoffDist, formationOn() ? (i9 == 0 ? 0 : (i9 & 1) == 1 ? (i9 + 1) / 2 : (-i9) / 2) + ((seq2.size - 1) * 0.5f) : i9, seq2.size);
                                    count++;
                                }
                            } else if (building3 != null) {
                                holdAwayFromCore(unit3, building3, standoffDist);
                                count++;
                            }
                            i9++;
                        }
                    }
                }
            }
        }
        shown.clear();
        shown.addAll(lines);
    }

    private static Unit pickCoreBreaker(Seq<Unit> seq, Building building) {
        Unit unit = null;
        float f = Float.MAX_VALUE;
        for (int i = 0; i < seq.size; i++) {
            Unit unit2 = (Unit) seq.get(i);
            if (!isHighDamage(unit2)) {
                float dst = unit2.dst(building) - ((unit2.health / Math.max(1.0f, unit2.maxHealth)) * 40.0f);
                if (dst < f) {
                    f = dst;
                    unit = unit2;
                }
            }
        }
        return unit != null ? unit : (Unit) seq.first();
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

    private static String tr(String str, String str2) {
        return pt() ? str2 : str;
    }

    private static void label(String str, String str2, String str3) {
        try {
            Core.bundle.getProperties().put("setting." + str + ".name", tr(str2, str3));
        } catch (Throwable th) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void profile(int i, int i2, int i3) {
        Core.settings.put("lunarians-attack-smart", Boolean.valueOf(i == 1));
        Core.settings.put("lunarians-attack-flee", Integer.valueOf(i2));
        Core.settings.put("lunarians-attack-formation", Boolean.valueOf(i3 == 1));
        if (Vars.ui != null) {
            Vars.ui.showInfoToast(tr("Profile applied. Reopen this menu to see the values.", "Perfil aplicado. Reabra o menu para ver os valores."), 3.0f);
        }
    }

    private static void drawOverlay() {
        if (Vars.state == null || !Vars.state.isGame() || !LunariansSettings.enableAutoAttack || !overlayOn()) {
            return;
        }
        Draw.z(120.0f);
        for (int i = 0; i < shown.size; i++) {
            float[] fArr = (float[]) shown.get(i);
            if (fArr[4] == 2.0f) {
                Draw.color(Pal.accent, 0.8f);
                Lines.stroke(1.5f);
                Lines.circle(fArr[0], fArr[1], fArr[2]);
            } else {
                Draw.color(fArr[4] == 1.0f ? Pal.remove : Pal.lightishOrange, 0.8f);
                Lines.stroke(1.2f);
                Lines.line(fArr[0], fArr[1], fArr[2], fArr[3]);
                Lines.circle(fArr[2], fArr[3], 6.0f);
            }
        }
        Draw.reset();
    }

    public static int count() {
        return count;
    }
}
