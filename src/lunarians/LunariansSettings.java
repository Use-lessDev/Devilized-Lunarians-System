/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.math.Mathf
 *  arc.struct.Seq
 */
package lunarians;

import arc.Core;
import arc.math.Mathf;
import arc.struct.Seq;
import lunarians.LunariansI18n;
import lunarians.data.AttackSlot;
import lunarians.data.CargoSlot;
import lunarians.data.DefenceSlot;
import lunarians.data.HealSlot;
import lunarians.data.LossSlot;
import lunarians.data.MoonSlot;
import lunarians.data.ShieldSlot;
import lunarians.data.UnitHealSlot;
import lunarians.data.UnitSlot;

public class LunariansSettings {
    public static final int MAX_CATEGORIES = 5;
    public static final float MAX_HEIGHT = 600.0f;
    public static final float MAX_WIDTH = 700.0f;
    public static final float MIN_HEIGHT = 120.0f;
    public static final float MIN_WIDTH = 160.0f;
    public static float panelX = 20.0f;
    public static float panelY = 100.0f;
    public static float panelWidth = 280.0f;
    public static float panelHeight = 200.0f;
    public static int slotCount = 12;
    public static String language = "en";
    public static float slotSize = 56.0f;
    public static int columns = 4;
    public static boolean showTitle = true;
    public static float opacity = 1.0f;
    public static float holdSeconds = 3.0f;
    public static boolean panelMinimized = false;
    public static int categoryCount = 1;
    public static int currentCategory = 0;
    public static String[] categoryNames = new String[]{"Default", "PvP", "Survival", "Attack", "Extra"};
    public static boolean enableEnemyCoreAlert = false;
    public static float attackMessageDelay = 5.0f;
    public static float enemyAlertRange = 20.0f;
    public static boolean enableAutoFill = false;
    public static float autoFillRange = 15.0f;
    public static float autoFillInterval = 0.5f;
    public static boolean autoFillTurrets = true;
    public static boolean autoFillFactories = true;
    public static boolean enableFreeCam = false;
    public static boolean enableProductionAlert = false;
    public static boolean productionIgnoreOwn = true;
    public static boolean enableCamFollow = false;
    public static boolean showSchematicPanel = true;
    public static int schemRotation = 0;
    public static boolean enableLossWarn = false;
    public static int lossPanel = 0;
    public static boolean enableAutoUnit = false;
    public static boolean enableAutoCargo = false;
    public static boolean enableAutoContainer = false;
    public static String containerUnit = "";
    public static int containerMaxUnits = 1;
    public static int containerPickX = Integer.MIN_VALUE;
    public static int containerPickY = Integer.MIN_VALUE;
    public static int containerDropX = Integer.MIN_VALUE;
    public static int containerDropY = Integer.MIN_VALUE;
    public static float containerWaitSec = 5.0f;
    public static boolean autoAttackNavalOnly = false;
    public static boolean autoHealNavalOnly = false;
    public static final int MAX_CARGO_SLOTS = 10;
    public static final Seq<CargoSlot> cargoSlots = new Seq();
    public static boolean enableAutoDefence = false;
    public static final int MAX_DEFENCE_SLOTS = 8;
    public static final Seq<DefenceSlot> defenceSlots = new Seq();
    public static boolean enableAutoShield = false;
    public static final int MAX_SHIELD_SLOTS = 8;
    public static final Seq<ShieldSlot> shieldSlots = new Seq();
    public static boolean enableAutoHeal = false;
    public static float autoHealPercent = 40.0f;
    public static float autoHealSearchRange = 40.0f;
    public static final int MAX_HEAL_SLOTS = 10;
    public static final Seq<HealSlot> healSlots = new Seq();
    public static boolean pvpMode = false;
    public static float pvpDelay = 4.5f;
    public static boolean enableAutoAttack = false;
    public static boolean autoAttackHeal = true;

    // AutoAttack settings
    public static int standoffBlocks = 3;
    public static boolean regroupEnabled = false;
    public static int regroupSec = 5;
    public static boolean smartTargets = true;
    public static boolean formationOn = true;
    public static boolean overlayOn = true;
    public static boolean smartPath = false;
    public static int fleePercent = 0;

        // AutoAttack: Projectile evasion
        public static boolean enableAutoDodge = false;
        public static float autoDodgeSensitivity = 1.0f;

        // AutoAttack: Focus fire coordination
        public static boolean enableFocusFire = false;
        public static float focusFireRange = 600.0f;

        // Debug visual
        public static boolean enableDebugVisual = false;

        public static final int MAX_ATTACK_SLOTS = 10;
    public static final Seq<AttackSlot> attackSlots = new Seq();
    public static float betaWidth = 250.0f;
    public static float betaHeightPct = 50.0f;
    public static final int MAX_UNIT_SLOTS = 10;
    public static final Seq<UnitSlot> unitSlots = new Seq();
    public static boolean enableAutoUnitHeal = false;
    public static final int MAX_UNIT_HEAL_SLOTS = 8;
    public static final Seq<UnitHealSlot> unitHealSlots = new Seq();
    public static boolean enableAutoMoon = false;
    public static final int MAX_MOON_SLOTS = 8;
    public static final Seq<MoonSlot> moonSlots = new Seq();
    public static boolean enableAutoConnectConveyor = false;
    public static String conveyorBlock = "conveyor";
    public static int conveyorStartX = Integer.MIN_VALUE;
    public static int conveyorStartY = Integer.MIN_VALUE;
    public static boolean enableAutoDrill = false;
    public static String drillBlock = "mechanical-drill";
    public static int drillStartX = Integer.MIN_VALUE;
    public static int drillStartY = Integer.MIN_VALUE;
    public static int drillMaxTiles = 400;
    public static int drillMinOres = 1;
    public static int drillOptQuality = 10;
    public static boolean drillPlaceWaterPower = false;
    public static boolean enableAutoDrillConveyor = false;
    public static int drillExitDir = 0;
    public static int drillLayoutMode = 0;
    public static boolean drillPerfect = false;
    public static boolean dockAuto = false;
    private static final Seq<LossSlot> lossP0 = new Seq();
    private static final Seq<LossSlot> lossP1 = new Seq();
    private static final Seq<LossSlot> lossP2 = new Seq();
    private static final Seq<LossSlot> lossP3 = new Seq();

    public static Seq<LossSlot> lossSlots(int n) {
        switch (n) {
            case 1: {
                return lossP1;
            }
            case 2: {
                return lossP2;
            }
            case 3: {
                return lossP3;
            }
        }
        return lossP0;
    }

    private static void loadLoss() {
        int n;
        block0: for (n = 0; n < 4; ++n) {
            Seq<LossSlot> seq = LunariansSettings.lossSlots(n);
            seq.clear();
            String string = Core.settings.getString("lunarians-loss-slots-" + n, "");
            if (string.isEmpty()) continue;
            for (String string2 : string.split(";")) {
                if (seq.size >= 25) continue block0;
                seq.add(LossSlot.deserialize(string2));
            }
        }
        if (!Core.settings.getBool("lunarians-loss-presets-v1", false)) {
            for (n = 0; n < 3; ++n) {
                if (!LunariansSettings.lossSlots(n).isEmpty()) continue;
                LunariansSettings.applyLossPreset(n);
            }
            Core.settings.put("lunarians-loss-presets-v1", (Object)true);
        }
    }

    private static void slot(Seq<LossSlot> seq, String string, float f, float f2) {
        LossSlot lossSlot = new LossSlot();
        lossSlot.item = string;
        lossSlot.perSec = f;
        lossSlot.delay = f2;
        seq.add(lossSlot);
    }

    public static Seq<LossSlot> lossPreset(int n) {
        Seq seq = new Seq();
        if (n == 0) {
            LunariansSettings.slot((Seq<LossSlot>)seq, "copper", 6.0f, 2.0f);
            LunariansSettings.slot((Seq<LossSlot>)seq, "lead", 6.0f, 2.0f);
            LunariansSettings.slot((Seq<LossSlot>)seq, "sand", 5.0f, 2.0f);
            LunariansSettings.slot((Seq<LossSlot>)seq, "coal", 4.0f, 2.0f);
            LunariansSettings.slot((Seq<LossSlot>)seq, "graphite", 3.0f, 2.0f);
        } else if (n == 1) {
            LunariansSettings.slot((Seq<LossSlot>)seq, "copper", 15.0f, 2.0f);
            LunariansSettings.slot((Seq<LossSlot>)seq, "lead", 15.0f, 2.0f);
            LunariansSettings.slot((Seq<LossSlot>)seq, "graphite", 6.0f, 2.0f);
            LunariansSettings.slot((Seq<LossSlot>)seq, "silicon", 5.0f, 2.0f);
            LunariansSettings.slot((Seq<LossSlot>)seq, "titanium", 5.0f, 2.0f);
            LunariansSettings.slot((Seq<LossSlot>)seq, "metaglass", 4.0f, 2.0f);
        } else if (n == 2) {
            LunariansSettings.slot((Seq<LossSlot>)seq, "silicon", 10.0f, 3.0f);
            LunariansSettings.slot((Seq<LossSlot>)seq, "titanium", 10.0f, 3.0f);
            LunariansSettings.slot((Seq<LossSlot>)seq, "metaglass", 6.0f, 3.0f);
            LunariansSettings.slot((Seq<LossSlot>)seq, "thorium", 5.0f, 3.0f);
            LunariansSettings.slot((Seq<LossSlot>)seq, "plastanium", 4.0f, 3.0f);
            LunariansSettings.slot((Seq<LossSlot>)seq, "phase-fabric", 2.0f, 3.0f);
            LunariansSettings.slot((Seq<LossSlot>)seq, "surge-alloy", 2.0f, 3.0f);
        }
        return seq;
    }

    public static void applyLossPreset(int n) {
        Seq<LossSlot> seq = LunariansSettings.lossSlots(n);
        seq.clear();
        seq.addAll(LunariansSettings.lossPreset(n));
    }

    private static void saveLoss() {
        for (int i = 0; i < 4; ++i) {
            StringBuilder stringBuilder = new StringBuilder();
            Seq<LossSlot> seq = LunariansSettings.lossSlots(i);
            for (int j = 0; j < seq.size; ++j) {
                if (j > 0) {
                    stringBuilder.append(';');
                }
                stringBuilder.append(((LossSlot)seq.get(j)).serialize());
            }
            Core.settings.put("lunarians-loss-slots-" + i, (Object)stringBuilder.toString());
        }
    }

    private static void loadUnits() {
        unitSlots.clear();
        String string = Core.settings.getString("lunarians-unit-slots", "");
        if (!string.isEmpty()) {
            for (String string2 : string.split(";")) {
                if (LunariansSettings.unitSlots.size >= 10) break;
                unitSlots.add(UnitSlot.deserialize(string2));
            }
        }
    }

    private static void saveUnits() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < LunariansSettings.unitSlots.size; ++i) {
            if (i > 0) {
                stringBuilder.append(';');
            }
            stringBuilder.append(((UnitSlot)unitSlots.get(i)).serialize());
        }
        Core.settings.put("lunarians-unit-slots", (Object)stringBuilder.toString());
    }

    /*
     * WARNING - void declaration
     */
    public static void load() {
        String string;
        int n;
        Object bl;
        int string7;
        panelX = Core.settings.getFloat("lunarians-panel-x", 20.0f);
        panelY = Core.settings.getFloat("lunarians-panel-y", 100.0f);
        panelWidth = Core.settings.getFloat("lunarians-panel-width", 280.0f);
        panelHeight = Core.settings.getFloat("lunarians-panel-height", 200.0f);
        slotCount = Core.settings.getInt("lunarians-slot-count", 12);
        language = Core.settings.getString("lunarians-language", "en");
        slotSize = Core.settings.getFloat("lunarians-slot-size", 56.0f);
        columns = Core.settings.getInt("lunarians-columns", 4);
        showTitle = Core.settings.getBool("lunarians-show-title", true);
        opacity = Core.settings.getFloat("lunarians-opacity", 1.0f);
        holdSeconds = Core.settings.getFloat("lunarians-hold-seconds", 3.0f);
        panelMinimized = Core.settings.getBool("lunarians-panel-minimized", false);
        categoryCount = Mathf.clamp((int)Core.settings.getInt("lunarians-cat-count", 1), (int)1, (int)5);
        currentCategory = Mathf.clamp((int)Core.settings.getInt("lunarians-cat-current", 0), (int)0, (int)(categoryCount - 1));
        for (int i = 0; i < 5; ++i) {
            LunariansSettings.categoryNames[i] = Core.settings.getString("lunarians-cat-name-" + i, categoryNames[i]);
        }
        enableEnemyCoreAlert = Core.settings.getBool("lunarians-enemy-core-alert", false);
        attackMessageDelay = Core.settings.getFloat("lunarians-attack-msg-delay", 5.0f);
        enemyAlertRange = Core.settings.getFloat("lunarians-enemy-alert-range", 20.0f);
        enableAutoFill = Core.settings.getBool("lunarians-autofill", false);
        autoFillRange = Core.settings.getFloat("lunarians-autofill-range", 15.0f);
        autoFillInterval = Core.settings.getFloat("lunarians-autofill-interval", 0.5f);
        autoFillTurrets = Core.settings.getBool("lunarians-autofill-turrets", true);
        autoFillFactories = Core.settings.getBool("lunarians-autofill-factories", true);
        enableFreeCam = Core.settings.getBool("lunarians-freecam", false);
        enableProductionAlert = Core.settings.getBool("lunarians-prod-alert", false);
        productionIgnoreOwn = Core.settings.getBool("lunarians-prod-ignore-own", true);
        enableCamFollow = Core.settings.getBool("lunarians-camfollow", false);
        showSchematicPanel = Core.settings.getBool("lunarians-show-panel", true);
        schemRotation = Mathf.clamp((int)Core.settings.getInt("lunarians-schem-rotation", 0), (int)0, (int)3);
        enableLossWarn = Core.settings.getBool("lunarians-loss-warn", false);
        lossPanel = Mathf.clamp((int)Core.settings.getInt("lunarians-loss-panel", 0), (int)0, (int)3);
        LunariansSettings.loadLoss();
        enableAutoUnit = Core.settings.getBool("lunarians-auto-unit", false);
        enableAutoUnitHeal = Core.settings.getBool("lunarians-auto-unit-heal", false);
        unitHealSlots.clear();
        String string2 = Core.settings.getString("lunarians-unit-heal-slots", "");
        if (!string2.isEmpty()) {
            for (String stringArray : string2.split(";")) {
                if (LunariansSettings.unitHealSlots.size >= 8) break;
                unitHealSlots.add(UnitHealSlot.deserialize(stringArray));
            }
        }
        enableAutoMoon = Core.settings.getBool("lunarians-auto-moon", false);
        enableAutoConnectConveyor = Core.settings.getBool("lunarians-auto-conveyor", false);
        conveyorBlock = Core.settings.getString("lunarians-conveyor-block", "conveyor");
        conveyorStartX = Core.settings.getInt("lunarians-conveyor-sx", Integer.MIN_VALUE);
        conveyorStartY = Core.settings.getInt("lunarians-conveyor-sy", Integer.MIN_VALUE);
        enableAutoDrill = Core.settings.getBool("lunarians-auto-drill", false);
        drillBlock = Core.settings.getString("lunarians-drill-block", "mechanical-drill");
        drillStartX = Core.settings.getInt("lunarians-drill-sx", Integer.MIN_VALUE);
        drillStartY = Core.settings.getInt("lunarians-drill-sy", Integer.MIN_VALUE);
        drillMaxTiles = Core.settings.getInt("lunarians-drill-max-tiles", 400);
        drillMinOres = Core.settings.getInt("lunarians-drill-min-ores", 1);
        drillOptQuality = Core.settings.getInt("lunarians-drill-opt", 10);
        drillPlaceWaterPower = Core.settings.getBool("lunarians-drill-water-power", false);
        enableAutoDrillConveyor = Core.settings.getBool("lunarians-auto-drill-conveyor", false);
        drillExitDir = Core.settings.getInt("lunarians-drill-exit-dir", 0);
        drillLayoutMode = Core.settings.getInt("lunarians-drill-layout", 0);
        drillPerfect = Core.settings.getBool("lunarians-drill-perfect", false);
        dockAuto = Core.settings.getBool("lunarians-dock-auto", false);
        moonSlots.clear();
        String string3 = Core.settings.getString("lunarians-moon-slots", "");
        if (!string3.isEmpty()) {
            String[] stringArray = string3.split(";");
            int n3 = stringArray.length;
            string7 = 0;
            for (int i = 0; i < n3; ++i) {
                bl = stringArray[i];
                if (LunariansSettings.moonSlots.size >= 8) break;
                moonSlots.add(MoonSlot.deserialize((String)bl));
            }
        }
        enableAutoCargo = Core.settings.getBool("lunarians-auto-cargo", false);
        enableAutoContainer = Core.settings.getBool("lunarians-auto-container", false);
        containerUnit = Core.settings.getString("lunarians-container-unit", "");
        containerMaxUnits = Core.settings.getInt("lunarians-container-max", 1);
        containerPickX = Core.settings.getInt("lunarians-container-pick-x", Integer.MIN_VALUE);
        containerPickY = Core.settings.getInt("lunarians-container-pick-y", Integer.MIN_VALUE);
        containerDropX = Core.settings.getInt("lunarians-container-drop-x", Integer.MIN_VALUE);
        containerDropY = Core.settings.getInt("lunarians-container-drop-y", Integer.MIN_VALUE);
        containerWaitSec = Core.settings.getFloat("lunarians-container-wait", 5.0f);
        autoAttackNavalOnly = Core.settings.getBool("lunarians-attack-naval", false);
        autoHealNavalOnly = Core.settings.getBool("lunarians-heal-naval", false);
        cargoSlots.clear();
        String string4 = Core.settings.getString("lunarians-cargo-slots", "");
        if (!string4.isEmpty()) {
            String[] cargoArray = string4.split(";");
            for (int i2 = 0; i2 < cargoArray.length; i2++) {
                String s = cargoArray[i2];
                if (LunariansSettings.cargoSlots.size >= 10) break;
                cargoSlots.add(CargoSlot.deserialize(s));
            }
        }
        enableAutoDefence = Core.settings.getBool("lunarians-auto-defence", false);
        defenceSlots.clear();
        String string5 = Core.settings.getString("lunarians-defence-slots", "");
        if (!string5.isEmpty()) {
            String[] stringArray = string5.split(";");
            for (int i = 0; i < stringArray.length; i++) {
                String s = stringArray[i];
                if (LunariansSettings.defenceSlots.size >= 8) break;
                defenceSlots.add(DefenceSlot.deserialize(s));
            }
        }
        enableAutoShield = Core.settings.getBool("lunarians-auto-shield", false);
        shieldSlots.clear();
        String string6 = Core.settings.getString("lunarians-shield-slots", "");
        if (!string6.isEmpty()) {
            String[] stringArray = string6.split(";");
            n = stringArray.length;
            boolean bl5 = false;
            for (int i = 0; i < n; ++i) {
                string = stringArray[i];
                if (LunariansSettings.shieldSlots.size >= 8) break;
                shieldSlots.add(ShieldSlot.deserialize(string));
            }
        }
        enableAutoHeal = Core.settings.getBool("lunarians-auto-heal", false);
        autoHealPercent = Core.settings.getFloat("lunarians-heal-pct", 40.0f);
        autoHealSearchRange = Core.settings.getFloat("lunarians-heal-search", 40.0f);
        healSlots.clear();
        String string8 = Core.settings.getString("lunarians-heal-slots", "");
        if (!string8.isEmpty()) {
            for (String string9 : string8.split(";")) {
                if (LunariansSettings.healSlots.size >= 10) break;
                healSlots.add(HealSlot.deserialize(string9));
            }
        }
        enableAutoAttack = Core.settings.getBool("lunarians-auto-attack", false);
        pvpMode = Core.settings.getBool("lunarians-balanced-mode", Core.settings.getBool("lunarians-pvp-mode", false));
        pvpDelay = Core.settings.getFloat("lunarians-balanced-delay", Core.settings.getFloat("lunarians-pvp-delay", 4.5f));
        autoAttackHeal = Core.settings.getBool("lunarians-attack-heal", true);

        // AutoAttack settings
        standoffBlocks = Core.settings.getInt("lunarians-attack-standoff-blocks", 3);
        regroupEnabled = Core.settings.getBool("lunarians-attack-regroup", false);
        regroupSec = Core.settings.getInt("lunarians-attack-regroup-sec", 5);
        smartTargets = Core.settings.getBool("lunarians-attack-smart", true);
        formationOn = Core.settings.getBool("lunarians-attack-formation", true);
        overlayOn = Core.settings.getBool("lunarians-attack-overlay", true);
        smartPath = Core.settings.getBool("lunarians-attack-smart-path", false);
        fleePercent = Core.settings.getInt("lunarians-attack-flee", 0);
        // IMPORTANTE: força fleePercent = 0 para evitar unidades fugirem
        // e parecerem "paradas" quando o mod reinicia ou muitas features são ativadas.
        fleePercent = 0;
        Core.settings.put("lunarians-attack-flee", 0);

        // AutoAttack: Projectile evasion
        enableAutoDodge = Core.settings.getBool("lunarians-auto-dodge", false);
        autoDodgeSensitivity = Core.settings.getFloat("lunarians-dodge-sensitivity", 1.0f);

        // AutoAttack: Focus fire coordination
        enableFocusFire = Core.settings.getBool("lunarians-focus-fire", false);
        focusFireRange = Core.settings.getFloat("lunarians-focus-range", 600.0f);

        // Debug visual
        enableDebugVisual = Core.settings.getBool("lunarians-debug-visual", false);

        attackSlots.clear();
        bl = Core.settings.getString("lunarians-attack-slots", "");
        if (!((String)bl).isEmpty()) {
            for (String string10 : ((String)bl).split(";")) {
                if (LunariansSettings.attackSlots.size >= 10) break;
                attackSlots.add(AttackSlot.deserialize(string10));
            }
        }
        betaWidth = Mathf.clamp((float)Core.settings.getFloat("lunarians-beta-width", 250.0f), (float)180.0f, (float)450.0f);
        betaHeightPct = Mathf.clamp((float)Core.settings.getFloat("lunarians-beta-height", 50.0f), (float)25.0f, (float)90.0f);
        LunariansSettings.loadUnits();
        panelWidth = Mathf.clamp((float)panelWidth, (float)160.0f, (float)700.0f);
        panelHeight = Mathf.clamp((float)panelHeight, (float)120.0f, (float)600.0f);
        slotSize = Mathf.clamp((float)slotSize, (float)36.0f, (float)72.0f);
        columns = Mathf.clamp((int)columns, (int)0, (int)10);
        slotCount = Mathf.clamp((int)slotCount, (int)1, (int)300);
        opacity = Mathf.clamp((float)opacity, (float)0.3f, (float)1.0f);
        holdSeconds = Mathf.clamp((float)holdSeconds, (float)1.0f, (float)10.0f);
        attackMessageDelay = Mathf.clamp((float)attackMessageDelay, (float)1.0f, (float)60.0f);
        enemyAlertRange = Mathf.clamp((float)enemyAlertRange, (float)5.0f, (float)50.0f);
        autoFillRange = Mathf.clamp((float)autoFillRange, (float)5.0f, (float)40.0f);
        autoFillInterval = Mathf.clamp((float)autoFillInterval, (float)0.2f, (float)5.0f);
        LunariansI18n.setLanguage(language);
    }

    public static void save() {
        Core.settings.put("lunarians-panel-x", (Object)Float.valueOf(panelX));
        Core.settings.put("lunarians-panel-y", (Object)Float.valueOf(panelY));
        Core.settings.put("lunarians-panel-width", (Object)Float.valueOf(panelWidth));
        Core.settings.put("lunarians-panel-height", (Object)Float.valueOf(panelHeight));
        Core.settings.put("lunarians-slot-count", (Object)slotCount);
        Core.settings.put("lunarians-language", (Object)language);
        Core.settings.put("lunarians-slot-size", (Object)Float.valueOf(slotSize));
        Core.settings.put("lunarians-columns", (Object)columns);
        Core.settings.put("lunarians-show-title", (Object)showTitle);
        Core.settings.put("lunarians-opacity", (Object)Float.valueOf(opacity));
        Core.settings.put("lunarians-hold-seconds", (Object)Float.valueOf(holdSeconds));
        Core.settings.put("lunarians-panel-minimized", (Object)panelMinimized);
        Core.settings.put("lunarians-cat-count", (Object)categoryCount);
        Core.settings.put("lunarians-cat-current", (Object)currentCategory);
        for (int i = 0; i < 5; ++i) {
            Core.settings.put("lunarians-cat-name-" + i, (Object)categoryNames[i]);
        }
        Core.settings.put("lunarians-enemy-core-alert", (Object)enableEnemyCoreAlert);
        Core.settings.put("lunarians-attack-msg-delay", (Object)Float.valueOf(attackMessageDelay));
        Core.settings.put("lunarians-enemy-alert-range", (Object)Float.valueOf(enemyAlertRange));
        Core.settings.put("lunarians-autofill", (Object)enableAutoFill);
        Core.settings.put("lunarians-autofill-range", (Object)Float.valueOf(autoFillRange));
        Core.settings.put("lunarians-autofill-interval", (Object)Float.valueOf(autoFillInterval));
        Core.settings.put("lunarians-autofill-turrets", (Object)autoFillTurrets);
        Core.settings.put("lunarians-autofill-factories", (Object)autoFillFactories);
        Core.settings.put("lunarians-freecam", (Object)enableFreeCam);
        Core.settings.put("lunarians-prod-alert", (Object)enableProductionAlert);
        Core.settings.put("lunarians-prod-ignore-own", (Object)productionIgnoreOwn);
        Core.settings.put("lunarians-camfollow", (Object)enableCamFollow);
        Core.settings.put("lunarians-show-panel", (Object)showSchematicPanel);
        Core.settings.put("lunarians-schem-rotation", (Object)schemRotation);
        Core.settings.put("lunarians-loss-warn", (Object)enableLossWarn);
        Core.settings.put("lunarians-loss-panel", (Object)lossPanel);
        LunariansSettings.saveLoss();
        Core.settings.put("lunarians-auto-unit", (Object)enableAutoUnit);
        Core.settings.put("lunarians-auto-unit-heal", (Object)enableAutoUnitHeal);
        Core.settings.put("lunarians-auto-moon", (Object)enableAutoMoon);
        Core.settings.put("lunarians-auto-conveyor", (Object)enableAutoConnectConveyor);
        Core.settings.put("lunarians-conveyor-block", (Object)conveyorBlock);
        Core.settings.put("lunarians-conveyor-sx", (Object)conveyorStartX);
        Core.settings.put("lunarians-conveyor-sy", (Object)conveyorStartY);
        Core.settings.put("lunarians-auto-drill", (Object)enableAutoDrill);
        Core.settings.put("lunarians-drill-block", (Object)drillBlock);
        Core.settings.put("lunarians-drill-sx", (Object)drillStartX);
        Core.settings.put("lunarians-drill-sy", (Object)drillStartY);
        Core.settings.put("lunarians-drill-max-tiles", (Object)drillMaxTiles);
        Core.settings.put("lunarians-drill-min-ores", (Object)drillMinOres);
        Core.settings.put("lunarians-drill-opt", (Object)drillOptQuality);
        Core.settings.put("lunarians-drill-water-power", (Object)drillPlaceWaterPower);
        Core.settings.put("lunarians-auto-drill-conveyor", (Object)enableAutoDrillConveyor);
        Core.settings.put("lunarians-drill-exit-dir", (Object)drillExitDir);
        Core.settings.put("lunarians-drill-layout", (Object)drillLayoutMode);
        Core.settings.put("lunarians-drill-perfect", (Object)drillPerfect);
        Core.settings.put("lunarians-dock-auto", (Object)dockAuto);
        Core.settings.put("lunarians-auto-unit-heal", (Object)enableAutoUnitHeal);
        Core.settings.put("lunarians-auto-cargo", (Object)enableAutoCargo);
        Core.settings.put("lunarians-auto-container", (Object)enableAutoContainer);
        Core.settings.put("lunarians-container-unit", (Object)(containerUnit == null ? "" : containerUnit));
        Core.settings.put("lunarians-container-max", (Object)containerMaxUnits);
        Core.settings.put("lunarians-container-pick-x", (Object)containerPickX);
        Core.settings.put("lunarians-container-pick-y", (Object)containerPickY);
        Core.settings.put("lunarians-container-drop-x", (Object)containerDropX);
        Core.settings.put("lunarians-container-drop-y", (Object)containerDropY);
        Core.settings.put("lunarians-container-wait", (Object)Float.valueOf(containerWaitSec));
        Core.settings.put("lunarians-attack-naval", (Object)autoAttackNavalOnly);
        Core.settings.put("lunarians-heal-naval", (Object)autoHealNavalOnly);
        Core.settings.put("lunarians-auto-defence", (Object)enableAutoDefence);
        Core.settings.put("lunarians-auto-shield", (Object)enableAutoShield);
        Core.settings.put("lunarians-auto-heal", (Object)enableAutoHeal);
        Core.settings.put("lunarians-heal-pct", (Object)Float.valueOf(autoHealPercent));
        Core.settings.put("lunarians-heal-search", (Object)Float.valueOf(autoHealSearchRange));
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < LunariansSettings.healSlots.size; ++i) {
            if (i > 0) {
                stringBuilder.append(';');
            }
            stringBuilder.append(((HealSlot)healSlots.get(i)).serialize());
        }
        Core.settings.put("lunarians-heal-slots", (Object)stringBuilder.toString());
        Core.settings.put("lunarians-auto-attack", (Object)enableAutoAttack);
        Core.settings.put("lunarians-pvp-mode", (Object)pvpMode);
        Core.settings.put("lunarians-balanced-mode", (Object)pvpMode);
        Core.settings.put("lunarians-pvp-delay", (Object)Float.valueOf(pvpDelay));
        Core.settings.put("lunarians-balanced-delay", (Object)Float.valueOf(pvpDelay));
        Core.settings.put("lunarians-attack-heal", (Object)autoAttackHeal);

        // AutoAttack settings
        Core.settings.put("lunarians-attack-standoff-blocks", (Object)standoffBlocks);
        Core.settings.put("lunarians-attack-regroup", (Object)regroupEnabled);
        Core.settings.put("lunarians-attack-regroup-sec", (Object)regroupSec);
        Core.settings.put("lunarians-attack-smart", (Object)smartTargets);
        Core.settings.put("lunarians-attack-formation", (Object)formationOn);
        Core.settings.put("lunarians-attack-overlay", (Object)overlayOn);
        Core.settings.put("lunarians-attack-smart-path", (Object)smartPath);
        Core.settings.put("lunarians-attack-flee", (Object)fleePercent);

        // AutoAttack: Retreat per unit type
        // AutoAttack: Projectile evasion
        Core.settings.put("lunarians-auto-dodge", (Object)enableAutoDodge);
        Core.settings.put("lunarians-dodge-sensitivity", (Object)Float.valueOf(autoDodgeSensitivity));

        // AutoAttack: Focus fire coordination
        Core.settings.put("lunarians-focus-fire", (Object)enableFocusFire);
        Core.settings.put("lunarians-focus-range", (Object)Float.valueOf(focusFireRange));

        // Debug visual
        Core.settings.put("lunarians-debug-visual", (Object)enableDebugVisual);

        StringBuilder stringBuilder2 = new StringBuilder();
        for (int i = 0; i < LunariansSettings.cargoSlots.size; ++i) {
            if (i > 0) {
                stringBuilder2.append(';');
            }
            stringBuilder2.append(((CargoSlot)cargoSlots.get(i)).serialize());
        }
        Core.settings.put("lunarians-cargo-slots", (Object)stringBuilder2.toString());
        Core.settings.put("lunarians-beta-width", (Object)Float.valueOf(betaWidth));
        Core.settings.put("lunarians-beta-height", (Object)Float.valueOf(betaHeightPct));
        StringBuilder stringBuilder3 = new StringBuilder();
        for (int i = 0; i < LunariansSettings.defenceSlots.size; ++i) {
            if (i > 0) {
                stringBuilder3.append(';');
            }
            stringBuilder3.append(((DefenceSlot)defenceSlots.get(i)).serialize());
        }
        Core.settings.put("lunarians-defence-slots", (Object)stringBuilder3.toString());
        StringBuilder stringBuilder4 = new StringBuilder();
        for (int i = 0; i < LunariansSettings.shieldSlots.size; ++i) {
            if (i > 0) {
                stringBuilder4.append(';');
            }
            stringBuilder4.append(((ShieldSlot)shieldSlots.get(i)).serialize());
        }
        Core.settings.put("lunarians-shield-slots", (Object)stringBuilder4.toString());
        LunariansSettings.saveUnits();
        Core.settings.saveValues();
    }

    public static String slotKey(int n) {
        return "lunarians-slot-c" + currentCategory + "-" + n;
    }

    public static void resetAll() {
        int n;
        panelX = 20.0f;
        panelY = 100.0f;
        panelWidth = 280.0f;
        panelHeight = 200.0f;
        slotCount = 12;
        language = "en";
        slotSize = 56.0f;
        columns = 4;
        showTitle = true;
        opacity = 1.0f;
        holdSeconds = 3.0f;
        panelMinimized = false;
        categoryCount = 1;
        currentCategory = 0;
        categoryNames = new String[]{"Default", "PvP", "Survival", "Attack", "Extra"};
        enableEnemyCoreAlert = false;
        attackMessageDelay = 5.0f;
        enemyAlertRange = 20.0f;
        enableAutoFill = false;
        autoFillRange = 15.0f;
        autoFillInterval = 0.5f;
        autoFillTurrets = true;
        autoFillFactories = true;
        enableFreeCam = false;
        enableProductionAlert = false;
        productionIgnoreOwn = true;
        enableCamFollow = false;
        showSchematicPanel = true;
        schemRotation = 0;
        enableLossWarn = false;
        lossPanel = 0;
        enableAutoUnit = false;
        enableAutoUnitHeal = false;
        unitHealSlots.clear();
        enableAutoMoon = false;
        moonSlots.clear();
        enableAutoConnectConveyor = false;
        conveyorBlock = "conveyor";
        conveyorStartX = Integer.MIN_VALUE;
        conveyorStartY = Integer.MIN_VALUE;
        enableAutoUnitHeal = false;
        unitHealSlots.clear();
        enableAutoCargo = false;
        cargoSlots.clear();
        enableAutoContainer = false;
        containerUnit = "";
        containerDropY = Integer.MIN_VALUE;
        containerDropX = Integer.MIN_VALUE;
        containerPickY = Integer.MIN_VALUE;
        containerPickX = Integer.MIN_VALUE;
        autoAttackNavalOnly = false;
        autoHealNavalOnly = false;
        enableAutoDefence = false;
        defenceSlots.clear();
        enableAutoShield = false;
        shieldSlots.clear();
        enableAutoHeal = false;
        autoHealPercent = 40.0f;
        autoHealSearchRange = 40.0f;
        healSlots.clear();
        enableAutoAttack = false;
        autoAttackHeal = true;
        attackSlots.clear();
        pvpMode = false;
        pvpDelay = 4.5f;
        betaWidth = 250.0f;
        betaHeightPct = 50.0f;

        // AutoAttack: Projectile evasion
        enableAutoDodge = false;
        autoDodgeSensitivity = 1.0f;

        // AutoAttack: Focus fire coordination
        enableFocusFire = false;
        focusFireRange = 600.0f;

        // Debug visual
        enableDebugVisual = false;
        unitSlots.clear();
        for (n = 0; n < 4; ++n) {
            LunariansSettings.lossSlots(n).clear();
            if (n >= 3) continue;
            LunariansSettings.applyLossPreset(n);
        }
        for (n = 0; n < 5; ++n) {
            for (int i = 0; i < 300; ++i) {
                Core.settings.remove("lunarians-slot-c" + n + "-" + i);
                Core.settings.remove("lunarians-slot-" + i);
            }
        }
        LunariansI18n.setLanguage("en");
        LunariansSettings.save();
    }

    public static boolean isBalanced() {
        return pvpMode;
    }

    public static float actionInterval(float f) {
        if (!pvpMode) {
            return f;
        }
        float f2 = pvpDelay < 1.0f ? 1.0f : pvpDelay;
        return Math.max(f, f2);
    }
}

