package lunarians;

import arc.Core;
import arc.math.Mathf;
import arc.struct.Seq;
import lunarians.data.AttackSlot;
import lunarians.data.CargoSlot;
import lunarians.data.DefenceSlot;
import lunarians.data.HealSlot;
import lunarians.data.LossSlot;
import lunarians.data.MoonSlot;
import lunarians.data.ShieldSlot;
import lunarians.data.UnitHealSlot;
import lunarians.data.UnitSlot;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/LunariansSettings.class */
public class LunariansSettings {
    public static final int MAX_CATEGORIES = 5;
    public static final float MAX_HEIGHT = 600.0f;
    public static final float MAX_WIDTH = 700.0f;
    public static final float MIN_HEIGHT = 120.0f;
    public static final float MIN_WIDTH = 160.0f;
    public static final int MAX_CARGO_SLOTS = 10;
    public static final int MAX_DEFENCE_SLOTS = 8;
    public static final int MAX_SHIELD_SLOTS = 8;
    public static final int MAX_HEAL_SLOTS = 10;
    public static final int MAX_ATTACK_SLOTS = 10;
    public static final int MAX_UNIT_SLOTS = 10;
    public static final int MAX_UNIT_HEAL_SLOTS = 8;
    public static final int MAX_MOON_SLOTS = 8;
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
    public static String[] categoryNames = {"Default", "PvP", "Survival", "Attack", "Extra"};
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
    public static final Seq<CargoSlot> cargoSlots = new Seq<>();
    public static boolean enableAutoDefence = false;
    public static final Seq<DefenceSlot> defenceSlots = new Seq<>();
    public static boolean enableAutoShield = false;
    public static final Seq<ShieldSlot> shieldSlots = new Seq<>();
    public static boolean enableAutoHeal = false;
    public static float autoHealPercent = 40.0f;
    public static float autoHealSearchRange = 40.0f;
    public static final Seq<HealSlot> healSlots = new Seq<>();
    public static boolean pvpMode = false;
    public static float pvpDelay = 4.5f;
    public static boolean enableAutoAttack = false;
    public static boolean autoAttackHeal = true;
    public static final Seq<AttackSlot> attackSlots = new Seq<>();
    public static float betaWidth = 250.0f;
    public static float betaHeightPct = 50.0f;
    public static final Seq<UnitSlot> unitSlots = new Seq<>();
    public static boolean enableAutoUnitHeal = false;
    public static final Seq<UnitHealSlot> unitHealSlots = new Seq<>();
    public static boolean enableAutoMoon = false;
    public static final Seq<MoonSlot> moonSlots = new Seq<>();
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
    private static final Seq<LossSlot> lossP0 = new Seq<>();
    private static final Seq<LossSlot> lossP1 = new Seq<>();
    private static final Seq<LossSlot> lossP2 = new Seq<>();
    private static final Seq<LossSlot> lossP3 = new Seq<>();

    public static Seq<LossSlot> lossSlots(int i) {
        switch (i) {
            case 1:
                return lossP1;
            case 2:
                return lossP2;
            case 3:
                return lossP3;
            default:
                return lossP0;
        }
    }

    private static void loadLoss() {
        for (int i = 0; i < 4; i++) {
            Seq<LossSlot> lossSlots = lossSlots(i);
            lossSlots.clear();
            String string = Core.settings.getString("lunarians-loss-slots-" + i, "");
            if (!string.isEmpty()) {
                for (String str : string.split(";")) {
                    if (lossSlots.size >= 25) {
                        break;
                    }
                    lossSlots.add(LossSlot.deserialize(str));
                }
            }
        }
        if (!Core.settings.getBool("lunarians-loss-presets-v1", false)) {
            for (int i2 = 0; i2 < 3; i2++) {
                if (lossSlots(i2).isEmpty()) {
                    applyLossPreset(i2);
                }
            }
            Core.settings.put("lunarians-loss-presets-v1", true);
        }
    }

    private static void slot(Seq<LossSlot> seq, String str, float f, float f2) {
        LossSlot lossSlot = new LossSlot();
        lossSlot.item = str;
        lossSlot.perSec = f;
        lossSlot.delay = f2;
        seq.add(lossSlot);
    }

    public static Seq<LossSlot> lossPreset(int i) {
        Seq<LossSlot> seq = new Seq<>();
        if (i == 0) {
            slot(seq, "copper", 6.0f, 2.0f);
            slot(seq, "lead", 6.0f, 2.0f);
            slot(seq, "sand", 5.0f, 2.0f);
            slot(seq, "coal", 4.0f, 2.0f);
            slot(seq, "graphite", 3.0f, 2.0f);
        } else if (i == 1) {
            slot(seq, "copper", 15.0f, 2.0f);
            slot(seq, "lead", 15.0f, 2.0f);
            slot(seq, "graphite", 6.0f, 2.0f);
            slot(seq, "silicon", 5.0f, 2.0f);
            slot(seq, "titanium", 5.0f, 2.0f);
            slot(seq, "metaglass", 4.0f, 2.0f);
        } else if (i == 2) {
            slot(seq, "silicon", 10.0f, 3.0f);
            slot(seq, "titanium", 10.0f, 3.0f);
            slot(seq, "metaglass", 6.0f, 3.0f);
            slot(seq, "thorium", 5.0f, 3.0f);
            slot(seq, "plastanium", 4.0f, 3.0f);
            slot(seq, "phase-fabric", 2.0f, 3.0f);
            slot(seq, "surge-alloy", 2.0f, 3.0f);
        }
        return seq;
    }

    public static void applyLossPreset(int i) {
        Seq<LossSlot> lossSlots = lossSlots(i);
        lossSlots.clear();
        lossSlots.addAll(lossPreset(i));
    }

    private static void saveLoss() {
        for (int i = 0; i < 4; i++) {
            StringBuilder sb = new StringBuilder();
            Seq<LossSlot> lossSlots = lossSlots(i);
            for (int i2 = 0; i2 < lossSlots.size; i2++) {
                if (i2 > 0) {
                    sb.append(';');
                }
                sb.append(((LossSlot) lossSlots.get(i2)).serialize());
            }
            Core.settings.put("lunarians-loss-slots-" + i, sb.toString());
        }
    }

    private static void loadUnits() {
        unitSlots.clear();
        String string = Core.settings.getString("lunarians-unit-slots", "");
        if (!string.isEmpty()) {
            for (String str : string.split(";")) {
                if (unitSlots.size < 10) {
                    unitSlots.add(UnitSlot.deserialize(str));
                } else {
                    return;
                }
            }
        }
    }

    private static void saveUnits() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < unitSlots.size; i++) {
            if (i > 0) {
                sb.append(';');
            }
            sb.append(((UnitSlot) unitSlots.get(i)).serialize());
        }
        Core.settings.put("lunarians-unit-slots", sb.toString());
    }

    public static void load() {
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
        categoryCount = Mathf.clamp(Core.settings.getInt("lunarians-cat-count", 1), 1, 5);
        currentCategory = Mathf.clamp(Core.settings.getInt("lunarians-cat-current", 0), 0, categoryCount - 1);
        for (int i = 0; i < 5; i++) {
            categoryNames[i] = Core.settings.getString("lunarians-cat-name-" + i, categoryNames[i]);
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
        schemRotation = Mathf.clamp(Core.settings.getInt("lunarians-schem-rotation", 0), 0, 3);
        enableLossWarn = Core.settings.getBool("lunarians-loss-warn", false);
        lossPanel = Mathf.clamp(Core.settings.getInt("lunarians-loss-panel", 0), 0, 3);
        loadLoss();
        enableAutoUnit = Core.settings.getBool("lunarians-auto-unit", false);
        enableAutoUnitHeal = Core.settings.getBool("lunarians-auto-unit-heal", false);
        unitHealSlots.clear();
        String string = Core.settings.getString("lunarians-unit-heal-slots", "");
        if (!string.isEmpty()) {
            for (String str : string.split(";")) {
                if (unitHealSlots.size >= 8) {
                    break;
                }
                unitHealSlots.add(UnitHealSlot.deserialize(str));
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
        String string2 = Core.settings.getString("lunarians-moon-slots", "");
        if (!string2.isEmpty()) {
            for (String str2 : string2.split(";")) {
                if (moonSlots.size >= 8) {
                    break;
                }
                moonSlots.add(MoonSlot.deserialize(str2));
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
        String string3 = Core.settings.getString("lunarians-cargo-slots", "");
        if (!string3.isEmpty()) {
            for (String str3 : string3.split(";")) {
                if (cargoSlots.size >= 10) {
                    break;
                }
                cargoSlots.add(CargoSlot.deserialize(str3));
            }
        }
        enableAutoDefence = Core.settings.getBool("lunarians-auto-defence", false);
        defenceSlots.clear();
        String string4 = Core.settings.getString("lunarians-defence-slots", "");
        if (!string4.isEmpty()) {
            for (String str4 : string4.split(";")) {
                if (defenceSlots.size >= 8) {
                    break;
                }
                defenceSlots.add(DefenceSlot.deserialize(str4));
            }
        }
        enableAutoShield = Core.settings.getBool("lunarians-auto-shield", false);
        shieldSlots.clear();
        String string5 = Core.settings.getString("lunarians-shield-slots", "");
        if (!string5.isEmpty()) {
            for (String str5 : string5.split(";")) {
                if (shieldSlots.size >= 8) {
                    break;
                }
                shieldSlots.add(ShieldSlot.deserialize(str5));
            }
        }
        enableAutoHeal = Core.settings.getBool("lunarians-auto-heal", false);
        autoHealPercent = Core.settings.getFloat("lunarians-heal-pct", 40.0f);
        autoHealSearchRange = Core.settings.getFloat("lunarians-heal-search", 40.0f);
        healSlots.clear();
        String string6 = Core.settings.getString("lunarians-heal-slots", "");
        if (!string6.isEmpty()) {
            for (String str6 : string6.split(";")) {
                if (healSlots.size >= 10) {
                    break;
                }
                healSlots.add(HealSlot.deserialize(str6));
            }
        }
        enableAutoAttack = Core.settings.getBool("lunarians-auto-attack", false);
        pvpMode = Core.settings.getBool("lunarians-balanced-mode", Core.settings.getBool("lunarians-pvp-mode", false));
        pvpDelay = Core.settings.getFloat("lunarians-balanced-delay", Core.settings.getFloat("lunarians-pvp-delay", 4.5f));
        autoAttackHeal = Core.settings.getBool("lunarians-attack-heal", true);
        attackSlots.clear();
        String string7 = Core.settings.getString("lunarians-attack-slots", "");
        if (!string7.isEmpty()) {
            for (String str7 : string7.split(";")) {
                if (attackSlots.size >= 10) {
                    break;
                }
                attackSlots.add(AttackSlot.deserialize(str7));
            }
        }
        betaWidth = Mathf.clamp(Core.settings.getFloat("lunarians-beta-width", 250.0f), 180.0f, 450.0f);
        betaHeightPct = Mathf.clamp(Core.settings.getFloat("lunarians-beta-height", 50.0f), 25.0f, 90.0f);
        loadUnits();
        panelWidth = Mathf.clamp(panelWidth, 160.0f, 700.0f);
        panelHeight = Mathf.clamp(panelHeight, 120.0f, 600.0f);
        slotSize = Mathf.clamp(slotSize, 36.0f, 72.0f);
        columns = Mathf.clamp(columns, 0, 10);
        slotCount = Mathf.clamp(slotCount, 1, 300);
        opacity = Mathf.clamp(opacity, 0.3f, 1.0f);
        holdSeconds = Mathf.clamp(holdSeconds, 1.0f, 10.0f);
        attackMessageDelay = Mathf.clamp(attackMessageDelay, 1.0f, 60.0f);
        enemyAlertRange = Mathf.clamp(enemyAlertRange, 5.0f, 50.0f);
        autoFillRange = Mathf.clamp(autoFillRange, 5.0f, 40.0f);
        autoFillInterval = Mathf.clamp(autoFillInterval, 0.2f, 5.0f);
        LunariansI18n.setLanguage(language);
    }

    public static void save() {
        Core.settings.put("lunarians-panel-x", Float.valueOf(panelX));
        Core.settings.put("lunarians-panel-y", Float.valueOf(panelY));
        Core.settings.put("lunarians-panel-width", Float.valueOf(panelWidth));
        Core.settings.put("lunarians-panel-height", Float.valueOf(panelHeight));
        Core.settings.put("lunarians-slot-count", Integer.valueOf(slotCount));
        Core.settings.put("lunarians-language", language);
        Core.settings.put("lunarians-slot-size", Float.valueOf(slotSize));
        Core.settings.put("lunarians-columns", Integer.valueOf(columns));
        Core.settings.put("lunarians-show-title", Boolean.valueOf(showTitle));
        Core.settings.put("lunarians-opacity", Float.valueOf(opacity));
        Core.settings.put("lunarians-hold-seconds", Float.valueOf(holdSeconds));
        Core.settings.put("lunarians-panel-minimized", Boolean.valueOf(panelMinimized));
        Core.settings.put("lunarians-cat-count", Integer.valueOf(categoryCount));
        Core.settings.put("lunarians-cat-current", Integer.valueOf(currentCategory));
        for (int i = 0; i < 5; i++) {
            Core.settings.put("lunarians-cat-name-" + i, categoryNames[i]);
        }
        Core.settings.put("lunarians-enemy-core-alert", Boolean.valueOf(enableEnemyCoreAlert));
        Core.settings.put("lunarians-attack-msg-delay", Float.valueOf(attackMessageDelay));
        Core.settings.put("lunarians-enemy-alert-range", Float.valueOf(enemyAlertRange));
        Core.settings.put("lunarians-autofill", Boolean.valueOf(enableAutoFill));
        Core.settings.put("lunarians-autofill-range", Float.valueOf(autoFillRange));
        Core.settings.put("lunarians-autofill-interval", Float.valueOf(autoFillInterval));
        Core.settings.put("lunarians-autofill-turrets", Boolean.valueOf(autoFillTurrets));
        Core.settings.put("lunarians-autofill-factories", Boolean.valueOf(autoFillFactories));
        Core.settings.put("lunarians-freecam", Boolean.valueOf(enableFreeCam));
        Core.settings.put("lunarians-prod-alert", Boolean.valueOf(enableProductionAlert));
        Core.settings.put("lunarians-prod-ignore-own", Boolean.valueOf(productionIgnoreOwn));
        Core.settings.put("lunarians-camfollow", Boolean.valueOf(enableCamFollow));
        Core.settings.put("lunarians-show-panel", Boolean.valueOf(showSchematicPanel));
        Core.settings.put("lunarians-schem-rotation", Integer.valueOf(schemRotation));
        Core.settings.put("lunarians-loss-warn", Boolean.valueOf(enableLossWarn));
        Core.settings.put("lunarians-loss-panel", Integer.valueOf(lossPanel));
        saveLoss();
        Core.settings.put("lunarians-auto-unit", Boolean.valueOf(enableAutoUnit));
        Core.settings.put("lunarians-auto-unit-heal", Boolean.valueOf(enableAutoUnitHeal));
        Core.settings.put("lunarians-auto-moon", Boolean.valueOf(enableAutoMoon));
        Core.settings.put("lunarians-auto-conveyor", Boolean.valueOf(enableAutoConnectConveyor));
        Core.settings.put("lunarians-conveyor-block", conveyorBlock);
        Core.settings.put("lunarians-conveyor-sx", Integer.valueOf(conveyorStartX));
        Core.settings.put("lunarians-conveyor-sy", Integer.valueOf(conveyorStartY));
        Core.settings.put("lunarians-auto-drill", Boolean.valueOf(enableAutoDrill));
        Core.settings.put("lunarians-drill-block", drillBlock);
        Core.settings.put("lunarians-drill-sx", Integer.valueOf(drillStartX));
        Core.settings.put("lunarians-drill-sy", Integer.valueOf(drillStartY));
        Core.settings.put("lunarians-drill-max-tiles", Integer.valueOf(drillMaxTiles));
        Core.settings.put("lunarians-drill-min-ores", Integer.valueOf(drillMinOres));
        Core.settings.put("lunarians-drill-opt", Integer.valueOf(drillOptQuality));
        Core.settings.put("lunarians-drill-water-power", Boolean.valueOf(drillPlaceWaterPower));
        Core.settings.put("lunarians-auto-drill-conveyor", Boolean.valueOf(enableAutoDrillConveyor));
        Core.settings.put("lunarians-drill-exit-dir", Integer.valueOf(drillExitDir));
        Core.settings.put("lunarians-drill-layout", Integer.valueOf(drillLayoutMode));
        Core.settings.put("lunarians-drill-perfect", Boolean.valueOf(drillPerfect));
        Core.settings.put("lunarians-dock-auto", Boolean.valueOf(dockAuto));
        Core.settings.put("lunarians-auto-unit-heal", Boolean.valueOf(enableAutoUnitHeal));
        Core.settings.put("lunarians-auto-cargo", Boolean.valueOf(enableAutoCargo));
        Core.settings.put("lunarians-auto-container", Boolean.valueOf(enableAutoContainer));
        Core.settings.put("lunarians-container-unit", containerUnit == null ? "" : containerUnit);
        Core.settings.put("lunarians-container-max", Integer.valueOf(containerMaxUnits));
        Core.settings.put("lunarians-container-pick-x", Integer.valueOf(containerPickX));
        Core.settings.put("lunarians-container-pick-y", Integer.valueOf(containerPickY));
        Core.settings.put("lunarians-container-drop-x", Integer.valueOf(containerDropX));
        Core.settings.put("lunarians-container-drop-y", Integer.valueOf(containerDropY));
        Core.settings.put("lunarians-container-wait", Float.valueOf(containerWaitSec));
        Core.settings.put("lunarians-attack-naval", Boolean.valueOf(autoAttackNavalOnly));
        Core.settings.put("lunarians-heal-naval", Boolean.valueOf(autoHealNavalOnly));
        Core.settings.put("lunarians-auto-defence", Boolean.valueOf(enableAutoDefence));
        Core.settings.put("lunarians-auto-shield", Boolean.valueOf(enableAutoShield));
        Core.settings.put("lunarians-auto-heal", Boolean.valueOf(enableAutoHeal));
        Core.settings.put("lunarians-heal-pct", Float.valueOf(autoHealPercent));
        Core.settings.put("lunarians-heal-search", Float.valueOf(autoHealSearchRange));
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < healSlots.size; i2++) {
            if (i2 > 0) {
                sb.append(';');
            }
            sb.append(((HealSlot) healSlots.get(i2)).serialize());
        }
        Core.settings.put("lunarians-heal-slots", sb.toString());
        Core.settings.put("lunarians-auto-attack", Boolean.valueOf(enableAutoAttack));
        Core.settings.put("lunarians-pvp-mode", Boolean.valueOf(pvpMode));
        Core.settings.put("lunarians-balanced-mode", Boolean.valueOf(pvpMode));
        Core.settings.put("lunarians-pvp-delay", Float.valueOf(pvpDelay));
        Core.settings.put("lunarians-balanced-delay", Float.valueOf(pvpDelay));
        Core.settings.put("lunarians-attack-heal", Boolean.valueOf(autoAttackHeal));
        StringBuilder sb2 = new StringBuilder();
        for (int i3 = 0; i3 < cargoSlots.size; i3++) {
            if (i3 > 0) {
                sb2.append(';');
            }
            sb2.append(((CargoSlot) cargoSlots.get(i3)).serialize());
        }
        Core.settings.put("lunarians-cargo-slots", sb2.toString());
        Core.settings.put("lunarians-beta-width", Float.valueOf(betaWidth));
        Core.settings.put("lunarians-beta-height", Float.valueOf(betaHeightPct));
        StringBuilder sb3 = new StringBuilder();
        for (int i4 = 0; i4 < defenceSlots.size; i4++) {
            if (i4 > 0) {
                sb3.append(';');
            }
            sb3.append(((DefenceSlot) defenceSlots.get(i4)).serialize());
        }
        Core.settings.put("lunarians-defence-slots", sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        for (int i5 = 0; i5 < shieldSlots.size; i5++) {
            if (i5 > 0) {
                sb4.append(';');
            }
            sb4.append(((ShieldSlot) shieldSlots.get(i5)).serialize());
        }
        Core.settings.put("lunarians-shield-slots", sb4.toString());
        saveUnits();
        Core.settings.saveValues();
    }

    public static String slotKey(int i) {
        return "lunarians-slot-c" + currentCategory + "-" + i;
    }

    public static void resetAll() {
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
        unitSlots.clear();
        for (int i = 0; i < 4; i++) {
            lossSlots(i).clear();
            if (i < 3) {
                applyLossPreset(i);
            }
        }
        for (int i2 = 0; i2 < 5; i2++) {
            for (int i3 = 0; i3 < 300; i3++) {
                Core.settings.remove("lunarians-slot-c" + i2 + "-" + i3);
                Core.settings.remove("lunarians-slot-" + i3);
            }
        }
        LunariansI18n.setLanguage("en");
        save();
    }

    public static boolean isBalanced() {
        return pvpMode;
    }

    public static float actionInterval(float f) {
        if (!pvpMode) {
            return f;
        }
        return Math.max(f, pvpDelay < 1.0f ? 1.0f : pvpDelay);
    }
}
