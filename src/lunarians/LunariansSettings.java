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

public class LunariansSettings {
  public static final int MAX_CATEGORIES = 5;
  
  public static final float MAX_HEIGHT = 600.0F;
  
  public static final float MAX_WIDTH = 700.0F;
  
  public static final float MIN_HEIGHT = 120.0F;
  
  public static final float MIN_WIDTH = 160.0F;
  
  public static float panelX = 20.0F;
  
  public static float panelY = 100.0F;
  
  public static float panelWidth = 280.0F;
  
  public static float panelHeight = 200.0F;
  
  public static int slotCount = 12;
  
  public static String language = "en";
  
  public static float slotSize = 56.0F;
  
  public static int columns = 4;
  
  public static boolean showTitle = true;
  
  public static float opacity = 1.0F;
  
  public static float holdSeconds = 3.0F;
  
  public static boolean panelMinimized = false;
  
  public static int categoryCount = 1;
  
  public static int currentCategory = 0;
  
  public static String[] categoryNames = new String[] { "Default", "PvP", "Survival", "Attack", "Extra" };
  
  public static boolean enableEnemyCoreAlert = false;
  
  public static float attackMessageDelay = 5.0F;
  
  public static float enemyAlertRange = 20.0F;
  
  public static boolean enableAutoFill = false;
  
  public static float autoFillRange = 15.0F;
  
  public static float autoFillInterval = 0.5F;
  
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
  
  public static float containerWaitSec = 5.0F;
  
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
  
  public static float autoHealPercent = 40.0F;
  
  public static float autoHealSearchRange = 40.0F;
  
  public static final int MAX_HEAL_SLOTS = 10;
  
  public static final Seq<HealSlot> healSlots = new Seq();
  
  public static boolean pvpMode = false;
  
  public static float pvpDelay = 4.5F;
  
  public static boolean enableAutoAttack = false;
  
  public static boolean autoAttackHeal = true;
  
  public static final int MAX_ATTACK_SLOTS = 10;
  
  public static final Seq<AttackSlot> attackSlots = new Seq();
  
  public static float betaWidth = 250.0F;
  
  public static float betaHeightPct = 50.0F;
  
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
  
  public static Seq<LossSlot> lossSlots(int paramInt) {
    switch (paramInt) {
      case 1:
        return lossP1;
      case 2:
        return lossP2;
      case 3:
        return lossP3;
    } 
    return lossP0;
  }
  
  private static void loadLoss() {
    byte b;
    for (b = 0; b < 4; b++) {
      Seq<LossSlot> seq = lossSlots(b);
      seq.clear();
      String str = Core.settings.getString("lunarians-loss-slots-" + b, "");
      if (!str.isEmpty())
        for (String str1 : str.split(";")) {
          if (seq.size >= 25)
            break; 
          seq.add(LossSlot.deserialize(str1));
        }  
    } 
    if (!Core.settings.getBool("lunarians-loss-presets-v1", false)) {
      for (b = 0; b < 3; b++) {
        if (lossSlots(b).isEmpty())
          applyLossPreset(b); 
      } 
      Core.settings.put("lunarians-loss-presets-v1", Boolean.valueOf(true));
    } 
  }
  
  private static void slot(Seq<LossSlot> paramSeq, String paramString, float paramFloat1, float paramFloat2) {
    LossSlot lossSlot = new LossSlot();
    lossSlot.item = paramString;
    lossSlot.perSec = paramFloat1;
    lossSlot.delay = paramFloat2;
    paramSeq.add(lossSlot);
  }
  
  public static Seq<LossSlot> lossPreset(int paramInt) {
    Seq<LossSlot> seq = new Seq();
    if (paramInt == 0) {
      slot(seq, "copper", 6.0F, 2.0F);
      slot(seq, "lead", 6.0F, 2.0F);
      slot(seq, "sand", 5.0F, 2.0F);
      slot(seq, "coal", 4.0F, 2.0F);
      slot(seq, "graphite", 3.0F, 2.0F);
    } else if (paramInt == 1) {
      slot(seq, "copper", 15.0F, 2.0F);
      slot(seq, "lead", 15.0F, 2.0F);
      slot(seq, "graphite", 6.0F, 2.0F);
      slot(seq, "silicon", 5.0F, 2.0F);
      slot(seq, "titanium", 5.0F, 2.0F);
      slot(seq, "metaglass", 4.0F, 2.0F);
    } else if (paramInt == 2) {
      slot(seq, "silicon", 10.0F, 3.0F);
      slot(seq, "titanium", 10.0F, 3.0F);
      slot(seq, "metaglass", 6.0F, 3.0F);
      slot(seq, "thorium", 5.0F, 3.0F);
      slot(seq, "plastanium", 4.0F, 3.0F);
      slot(seq, "phase-fabric", 2.0F, 3.0F);
      slot(seq, "surge-alloy", 2.0F, 3.0F);
    } 
    return seq;
  }
  
  public static void applyLossPreset(int paramInt) {
    Seq<LossSlot> seq = lossSlots(paramInt);
    seq.clear();
    seq.addAll(lossPreset(paramInt));
  }
  
  private static void saveLoss() {
    for (byte b = 0; b < 4; b++) {
      StringBuilder stringBuilder = new StringBuilder();
      Seq<LossSlot> seq = lossSlots(b);
      for (byte b1 = 0; b1 < seq.size; b1++) {
        if (b1 > 0)
          stringBuilder.append(';'); 
        stringBuilder.append(((LossSlot)seq.get(b1)).serialize());
      } 
      Core.settings.put("lunarians-loss-slots-" + b, stringBuilder.toString());
    } 
  }
  
  private static void loadUnits() {
    unitSlots.clear();
    String str = Core.settings.getString("lunarians-unit-slots", "");
    if (!str.isEmpty())
      for (String str1 : str.split(";")) {
        if (unitSlots.size >= 10)
          break; 
        unitSlots.add(UnitSlot.deserialize(str1));
      }  
  }
  
  private static void saveUnits() {
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < unitSlots.size; b++) {
      if (b > 0)
        stringBuilder.append(';'); 
      stringBuilder.append(((UnitSlot)unitSlots.get(b)).serialize());
    } 
    Core.settings.put("lunarians-unit-slots", stringBuilder.toString());
  }
  
  public static void load() {
    panelX = Core.settings.getFloat("lunarians-panel-x", 20.0F);
    panelY = Core.settings.getFloat("lunarians-panel-y", 100.0F);
    panelWidth = Core.settings.getFloat("lunarians-panel-width", 280.0F);
    panelHeight = Core.settings.getFloat("lunarians-panel-height", 200.0F);
    slotCount = Core.settings.getInt("lunarians-slot-count", 12);
    language = Core.settings.getString("lunarians-language", "en");
    slotSize = Core.settings.getFloat("lunarians-slot-size", 56.0F);
    columns = Core.settings.getInt("lunarians-columns", 4);
    showTitle = Core.settings.getBool("lunarians-show-title", true);
    opacity = Core.settings.getFloat("lunarians-opacity", 1.0F);
    holdSeconds = Core.settings.getFloat("lunarians-hold-seconds", 3.0F);
    panelMinimized = Core.settings.getBool("lunarians-panel-minimized", false);
    categoryCount = Mathf.clamp(Core.settings.getInt("lunarians-cat-count", 1), 1, 5);
    currentCategory = Mathf.clamp(Core.settings.getInt("lunarians-cat-current", 0), 0, categoryCount - 1);
    for (byte b = 0; b < 5; b++)
      categoryNames[b] = Core.settings.getString("lunarians-cat-name-" + b, categoryNames[b]); 
    enableEnemyCoreAlert = Core.settings.getBool("lunarians-enemy-core-alert", false);
    attackMessageDelay = Core.settings.getFloat("lunarians-attack-msg-delay", 5.0F);
    enemyAlertRange = Core.settings.getFloat("lunarians-enemy-alert-range", 20.0F);
    enableAutoFill = Core.settings.getBool("lunarians-autofill", false);
    autoFillRange = Core.settings.getFloat("lunarians-autofill-range", 15.0F);
    autoFillInterval = Core.settings.getFloat("lunarians-autofill-interval", 0.5F);
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
    String str1 = Core.settings.getString("lunarians-unit-heal-slots", "");
    if (!str1.isEmpty())
      for (String str : str1.split(";")) {
        if (unitHealSlots.size >= 8)
          break; 
        unitHealSlots.add(UnitHealSlot.deserialize(str));
      }  
    enableAutoMoon = Core.settings.getBool("lunarians-auto-moon", false);
    enableAutoConnectConveyor = Core.settings.getBool("lunarians-auto-conveyor", false);
    conveyorBlock = Core.settings.getString("lunarians-conveyor-block", "conveyor");
    conveyorStartX = Core.settings.getInt("lunarians-conveyor-sx", -2147483648);
    conveyorStartY = Core.settings.getInt("lunarians-conveyor-sy", -2147483648);
    enableAutoDrill = Core.settings.getBool("lunarians-auto-drill", false);
    drillBlock = Core.settings.getString("lunarians-drill-block", "mechanical-drill");
    drillStartX = Core.settings.getInt("lunarians-drill-sx", -2147483648);
    drillStartY = Core.settings.getInt("lunarians-drill-sy", -2147483648);
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
    String str2 = Core.settings.getString("lunarians-moon-slots", "");
    if (!str2.isEmpty()) {
      byte b1 = 0;
      String[] arrayOfString = str2.split(";");
      int i = arrayOfString.length;
      boolean bool = false;
      while (b1 < i) {
        String str = arrayOfString[b1];
        if (moonSlots.size >= 8)
          break; 
        moonSlots.add(MoonSlot.deserialize(str));
        b1++;
      } 
    } 
    enableAutoCargo = Core.settings.getBool("lunarians-auto-cargo", false);
    enableAutoContainer = Core.settings.getBool("lunarians-auto-container", false);
    containerUnit = Core.settings.getString("lunarians-container-unit", "");
    containerMaxUnits = Core.settings.getInt("lunarians-container-max", 1);
    containerPickX = Core.settings.getInt("lunarians-container-pick-x", -2147483648);
    containerPickY = Core.settings.getInt("lunarians-container-pick-y", -2147483648);
    containerDropX = Core.settings.getInt("lunarians-container-drop-x", -2147483648);
    containerDropY = Core.settings.getInt("lunarians-container-drop-y", -2147483648);
    containerWaitSec = Core.settings.getFloat("lunarians-container-wait", 5.0F);
    autoAttackNavalOnly = Core.settings.getBool("lunarians-attack-naval", false);
    autoHealNavalOnly = Core.settings.getBool("lunarians-heal-naval", false);
    cargoSlots.clear();
    String str3 = Core.settings.getString("lunarians-cargo-slots", "");
    if (!str3.isEmpty()) {
      byte b1 = 0;
      String[] arrayOfString = str3.split(";");
      int i = arrayOfString.length;
      boolean bool = false;
      while (b1 < i) {
        String str = arrayOfString[b1];
        if (cargoSlots.size >= 10)
          break; 
        cargoSlots.add(CargoSlot.deserialize(str));
        b1++;
      } 
    } 
    enableAutoDefence = Core.settings.getBool("lunarians-auto-defence", false);
    defenceSlots.clear();
    String str4 = Core.settings.getString("lunarians-defence-slots", "");
    if (!str4.isEmpty()) {
      byte b1 = 0;
      String[] arrayOfString = str4.split(";");
      int i = arrayOfString.length;
      boolean bool1 = false;
      boolean bool2 = false;
      while (b1 < i) {
        String str = arrayOfString[b1];
        if (defenceSlots.size >= 8)
          break; 
        defenceSlots.add(DefenceSlot.deserialize(str));
        b1++;
      } 
    } 
    enableAutoShield = Core.settings.getBool("lunarians-auto-shield", false);
    shieldSlots.clear();
    String str5 = Core.settings.getString("lunarians-shield-slots", "");
    if (!str5.isEmpty()) {
      String[] arrayOfString = str5.split(";");
      int i = arrayOfString.length;
      boolean bool = false;
      for (byte b1 = 0; b1 < i; b1++) {
        String str = arrayOfString[b1];
        if (shieldSlots.size >= 8)
          break; 
        shieldSlots.add(ShieldSlot.deserialize(str));
      } 
    } 
    enableAutoHeal = Core.settings.getBool("lunarians-auto-heal", false);
    autoHealPercent = Core.settings.getFloat("lunarians-heal-pct", 40.0F);
    autoHealSearchRange = Core.settings.getFloat("lunarians-heal-search", 40.0F);
    healSlots.clear();
    String str6 = Core.settings.getString("lunarians-heal-slots", "");
    if (!str6.isEmpty())
      for (String str : str6.split(";")) {
        if (healSlots.size >= 10)
          break; 
        healSlots.add(HealSlot.deserialize(str));
      }  
    enableAutoAttack = Core.settings.getBool("lunarians-auto-attack", false);
    pvpMode = Core.settings.getBool("lunarians-balanced-mode", Core.settings.getBool("lunarians-pvp-mode", false));
    pvpDelay = Core.settings.getFloat("lunarians-balanced-delay", Core.settings.getFloat("lunarians-pvp-delay", 4.5F));
    autoAttackHeal = Core.settings.getBool("lunarians-attack-heal", true);
    attackSlots.clear();
    String str7 = Core.settings.getString("lunarians-attack-slots", "");
    if (!str7.isEmpty())
      for (String str : str7.split(";")) {
        if (attackSlots.size >= 10)
          break; 
        attackSlots.add(AttackSlot.deserialize(str));
      }  
    betaWidth = Mathf.clamp(Core.settings.getFloat("lunarians-beta-width", 250.0F), 180.0F, 450.0F);
    betaHeightPct = Mathf.clamp(Core.settings.getFloat("lunarians-beta-height", 50.0F), 25.0F, 90.0F);
    loadUnits();
    panelWidth = Mathf.clamp(panelWidth, 160.0F, 700.0F);
    panelHeight = Mathf.clamp(panelHeight, 120.0F, 600.0F);
    slotSize = Mathf.clamp(slotSize, 36.0F, 72.0F);
    columns = Mathf.clamp(columns, 0, 10);
    slotCount = Mathf.clamp(slotCount, 1, 300);
    opacity = Mathf.clamp(opacity, 0.3F, 1.0F);
    holdSeconds = Mathf.clamp(holdSeconds, 1.0F, 10.0F);
    attackMessageDelay = Mathf.clamp(attackMessageDelay, 1.0F, 60.0F);
    enemyAlertRange = Mathf.clamp(enemyAlertRange, 5.0F, 50.0F);
    autoFillRange = Mathf.clamp(autoFillRange, 5.0F, 40.0F);
    autoFillInterval = Mathf.clamp(autoFillInterval, 0.2F, 5.0F);
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
    for (byte b1 = 0; b1 < 5; b1++)
      Core.settings.put("lunarians-cat-name-" + b1, categoryNames[b1]); 
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
    Core.settings.put("lunarians-container-unit", (containerUnit == null) ? "" : containerUnit);
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
    StringBuilder stringBuilder1 = new StringBuilder();
    for (byte b2 = 0; b2 < healSlots.size; b2++) {
      if (b2 > 0)
        stringBuilder1.append(';'); 
      stringBuilder1.append(((HealSlot)healSlots.get(b2)).serialize());
    } 
    Core.settings.put("lunarians-heal-slots", stringBuilder1.toString());
    Core.settings.put("lunarians-auto-attack", Boolean.valueOf(enableAutoAttack));
    Core.settings.put("lunarians-pvp-mode", Boolean.valueOf(pvpMode));
    Core.settings.put("lunarians-balanced-mode", Boolean.valueOf(pvpMode));
    Core.settings.put("lunarians-pvp-delay", Float.valueOf(pvpDelay));
    Core.settings.put("lunarians-balanced-delay", Float.valueOf(pvpDelay));
    Core.settings.put("lunarians-attack-heal", Boolean.valueOf(autoAttackHeal));
    StringBuilder stringBuilder2 = new StringBuilder();
    for (byte b3 = 0; b3 < cargoSlots.size; b3++) {
      if (b3 > 0)
        stringBuilder2.append(';'); 
      stringBuilder2.append(((CargoSlot)cargoSlots.get(b3)).serialize());
    } 
    Core.settings.put("lunarians-cargo-slots", stringBuilder2.toString());
    Core.settings.put("lunarians-beta-width", Float.valueOf(betaWidth));
    Core.settings.put("lunarians-beta-height", Float.valueOf(betaHeightPct));
    StringBuilder stringBuilder3 = new StringBuilder();
    for (byte b4 = 0; b4 < defenceSlots.size; b4++) {
      if (b4 > 0)
        stringBuilder3.append(';'); 
      stringBuilder3.append(((DefenceSlot)defenceSlots.get(b4)).serialize());
    } 
    Core.settings.put("lunarians-defence-slots", stringBuilder3.toString());
    StringBuilder stringBuilder4 = new StringBuilder();
    for (byte b5 = 0; b5 < shieldSlots.size; b5++) {
      if (b5 > 0)
        stringBuilder4.append(';'); 
      stringBuilder4.append(((ShieldSlot)shieldSlots.get(b5)).serialize());
    } 
    Core.settings.put("lunarians-shield-slots", stringBuilder4.toString());
    saveUnits();
    Core.settings.saveValues();
  }
  
  public static String slotKey(int paramInt) {
    return "lunarians-slot-c" + currentCategory + "-" + paramInt;
  }
  
  public static void resetAll() {
    panelX = 20.0F;
    panelY = 100.0F;
    panelWidth = 280.0F;
    panelHeight = 200.0F;
    slotCount = 12;
    language = "en";
    slotSize = 56.0F;
    columns = 4;
    showTitle = true;
    opacity = 1.0F;
    holdSeconds = 3.0F;
    panelMinimized = false;
    categoryCount = 1;
    currentCategory = 0;
    categoryNames = new String[] { "Default", "PvP", "Survival", "Attack", "Extra" };
    enableEnemyCoreAlert = false;
    attackMessageDelay = 5.0F;
    enemyAlertRange = 20.0F;
    enableAutoFill = false;
    autoFillRange = 15.0F;
    autoFillInterval = 0.5F;
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
    autoHealPercent = 40.0F;
    autoHealSearchRange = 40.0F;
    healSlots.clear();
    enableAutoAttack = false;
    autoAttackHeal = true;
    attackSlots.clear();
    pvpMode = false;
    pvpDelay = 4.5F;
    betaWidth = 250.0F;
    betaHeightPct = 50.0F;
    unitSlots.clear();
    byte b;
    for (b = 0; b < 4; b++) {
      lossSlots(b).clear();
      if (b < 3)
        applyLossPreset(b); 
    } 
    for (b = 0; b < 5; b++) {
      for (byte b1 = 0; b1 < 'Ĭ'; b1++) {
        Core.settings.remove("lunarians-slot-c" + b + "-" + b1);
        Core.settings.remove("lunarians-slot-" + b1);
      } 
    } 
    LunariansI18n.setLanguage("en");
    save();
  }
  
  public static boolean isBalanced() {
    return pvpMode;
  }
  
  public static float actionInterval(float paramFloat) {
    if (!pvpMode)
      return paramFloat; 
    float f = (pvpDelay < 1.0F) ? 1.0F : pvpDelay;
    return Math.max(paramFloat, f);
  }
}
