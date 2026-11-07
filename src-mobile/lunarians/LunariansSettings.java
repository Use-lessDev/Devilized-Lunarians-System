package lunarians;

import arc.Core;
import arc.Settings;
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
  public static final int MAX_ATTACK_SLOTS = 10;
  
  public static final int MAX_CARGO_SLOTS = 10;
  
  public static final int MAX_CATEGORIES = 5;
  
  public static final int MAX_DEFENCE_SLOTS = 8;
  
  public static final int MAX_HEAL_SLOTS = 10;
  
  public static final float MAX_HEIGHT = 600.0F;
  
  public static final int MAX_MOON_SLOTS = 8;
  
  public static final int MAX_SHIELD_SLOTS = 8;
  
  public static final int MAX_UNIT_HEAL_SLOTS = 8;
  
  public static final int MAX_UNIT_SLOTS = 10;
  
  public static final float MAX_WIDTH = 700.0F;
  
  public static final float MIN_HEIGHT = 120.0F;
  
  public static final float MIN_WIDTH = 160.0F;
  
  public static float attackMessageDelay = 0.0F;
  
  public static final Seq attackSlots;
  
  public static boolean autoAttackHeal = false;
  
  public static boolean autoAttackNavalOnly = false;
  
  public static boolean autoFillFactories = false;
  
  public static float autoFillInterval = 0.0F;
  
  public static float autoFillRange = 0.0F;
  
  public static boolean autoFillTurrets = false;
  
  public static boolean autoHealNavalOnly = false;
  
  public static float autoHealPercent = 0.0F;
  
  public static float autoHealSearchRange = 0.0F;
  
  public static float betaHeightPct = 0.0F;
  
  public static float betaWidth = 0.0F;
  
  public static final Seq cargoSlots;
  
  public static int categoryCount = 1;
  
  public static String[] categoryNames = new String[] { "Default", "PvP", "Survival", "Attack", "Extra" };
  
  public static int columns = 4;
  
  public static int containerDropX = 0;
  
  public static int containerDropY = 0;
  
  public static int containerMaxUnits = 0;
  
  public static int containerPickX = 0;
  
  public static int containerPickY = 0;
  
  public static String containerUnit;
  
  public static float containerWaitSec = 0.0F;
  
  public static String conveyorBlock;
  
  public static int conveyorStartX = 0;
  
  public static int conveyorStartY = 0;
  
  public static int currentCategory = 0;
  
  public static final Seq defenceSlots;
  
  public static boolean dockAuto = false;
  
  public static String drillBlock;
  
  public static int drillExitDir = 0;
  
  public static int drillLayoutMode = 0;
  
  public static int drillMaxTiles = 0;
  
  public static int drillMinOres = 0;
  
  public static int drillOptQuality = 0;
  
  public static boolean drillPerfect = false;
  
  public static boolean drillPlaceWaterPower = false;
  
  public static int drillStartX = 0;
  
  public static int drillStartY = 0;
  
  public static boolean enableAutoAttack = false;
  
  public static boolean enableAutoCargo = false;
  
  public static boolean enableAutoConnectConveyor = false;
  
  public static boolean enableAutoContainer = false;
  
  public static boolean enableAutoDefence = false;
  
  public static boolean enableAutoDrill = false;
  
  public static boolean enableAutoDrillConveyor = false;
  
  public static boolean enableAutoFill = false;
  
  public static boolean enableAutoHeal = false;
  
  public static boolean enableAutoMoon = false;
  
  public static boolean enableAutoShield = false;
  
  public static boolean enableAutoUnit = false;
  
  public static boolean enableAutoUnitHeal = false;
  
  public static boolean enableCamFollow = false;
  
  public static boolean enableEnemyCoreAlert = false;
  
  public static boolean enableFreeCam = false;
  
  public static boolean enableLossWarn = false;
  
  public static boolean enableProductionAlert = false;
  
  public static float enemyAlertRange = 0.0F;
  
  public static final Seq healSlots;
  
  public static float holdSeconds = 3.0F;
  
  public static String language = "en";
  
  private static final Seq lossP0;
  
  private static final Seq lossP1;
  
  private static final Seq lossP2;
  
  private static final Seq lossP3;
  
  public static int lossPanel = 0;
  
  public static final Seq moonSlots;
  
  public static float opacity = 1.0F;
  
  public static float panelHeight = 200.0F;
  
  public static boolean panelMinimized = false;
  
  public static float panelWidth = 280.0F;
  
  public static float panelX = 20.0F;
  
  public static float panelY = 100.0F;
  
  public static boolean productionIgnoreOwn = false;
  
  public static float pvpDelay = 0.0F;
  
  public static boolean pvpMode = false;
  
  public static int schemRotation = 0;
  
  public static final Seq shieldSlots;
  
  public static boolean showSchematicPanel = false;
  
  public static boolean showTitle = true;
  
  public static int slotCount = 12;
  
  public static float slotSize = 56.0F;
  
  public static final Seq unitHealSlots;
  
  public static final Seq unitSlots;
  
  static {
    float f = 5.0F;
    attackMessageDelay = f;
    enemyAlertRange = 20.0F;
    enableAutoFill = false;
    autoFillRange = 15.0F;
    autoFillInterval = 0.5F;
    boolean bool = true;
    autoFillTurrets = bool;
    autoFillFactories = bool;
    enableFreeCam = false;
    enableProductionAlert = false;
    productionIgnoreOwn = bool;
    enableCamFollow = false;
    showSchematicPanel = bool;
    schemRotation = 0;
    enableLossWarn = false;
    lossPanel = 0;
    enableAutoUnit = false;
    enableAutoCargo = false;
    enableAutoContainer = false;
    containerUnit = "";
    containerMaxUnits = bool;
    int i = -1 << -1;
    containerPickX = i;
    containerPickY = i;
    containerDropX = i;
    containerDropY = i;
    containerWaitSec = f;
    autoAttackNavalOnly = false;
    autoHealNavalOnly = false;
    Seq seq1 = new Seq();
    this();
    cargoSlots = seq1;
    enableAutoDefence = false;
    seq1 = new Seq();
    this();
    defenceSlots = seq1;
    enableAutoShield = false;
    seq1 = new Seq();
    this();
    shieldSlots = seq1;
    enableAutoHeal = false;
    f = 40.0F;
    autoHealPercent = f;
    autoHealSearchRange = f;
    seq1 = new Seq();
    this();
    healSlots = seq1;
    pvpMode = false;
    pvpDelay = 4.5F;
    enableAutoAttack = false;
    autoAttackHeal = bool;
    seq1 = new Seq();
    this();
    attackSlots = seq1;
    betaWidth = 250.0F;
    betaHeightPct = 50.0F;
    seq1 = new Seq();
    this();
    unitSlots = seq1;
    enableAutoUnitHeal = false;
    seq1 = new Seq();
    this();
    unitHealSlots = seq1;
    enableAutoMoon = false;
    seq1 = new Seq();
    this();
    moonSlots = seq1;
    enableAutoConnectConveyor = false;
    conveyorBlock = "conveyor";
    conveyorStartX = i;
    conveyorStartY = i;
    enableAutoDrill = false;
    drillBlock = "mechanical-drill";
    drillStartX = i;
    drillStartY = i;
    drillMaxTiles = 400;
    drillMinOres = bool;
    drillOptQuality = 10;
    drillPlaceWaterPower = false;
    enableAutoDrillConveyor = false;
    drillExitDir = 0;
    drillLayoutMode = 0;
    drillPerfect = false;
    dockAuto = false;
    Seq seq2 = new Seq();
    this();
    lossP0 = seq2;
    seq2 = new Seq();
    this();
    lossP1 = seq2;
    seq2 = new Seq();
    this();
    lossP2 = seq2;
    seq2 = new Seq();
    this();
    lossP3 = seq2;
  }
  
  public static float actionInterval(float paramFloat) {
    boolean bool = pvpMode;
    if (!bool)
      return paramFloat; 
    float f1 = pvpDelay;
    float f2 = 1.0F;
    int i = f1 cmp f2;
    if (i < 0) {
      int j = 1065353216;
      f1 = 1.0F;
    } 
    return Math.max(paramFloat, f1);
  }
  
  public static void applyLossPreset(int paramInt) {
    Seq seq1 = lossSlots(paramInt);
    seq1.clear();
    Seq seq2 = lossPreset(paramInt);
    seq1.addAll(seq2);
  }
  
  public static boolean isBalanced() {
    return pvpMode;
  }
  
  public static void load() {
    Settings settings8 = Core.settings;
    int i = 1101004800;
    float f1 = 20.0F;
    panelX = settings8.getFloat("lunarians-panel-x", f1);
    panelY = Core.settings.getFloat("lunarians-panel-y", 100.0F);
    panelWidth = Core.settings.getFloat("lunarians-panel-width", 280.0F);
    panelHeight = Core.settings.getFloat("lunarians-panel-height", 200.0F);
    slotCount = Core.settings.getInt("lunarians-slot-count", 12);
    settings8 = Core.settings;
    String str8 = "en";
    language = settings8.getString("lunarians-language", str8);
    slotSize = Core.settings.getFloat("lunarians-slot-size", 56.0F);
    columns = Core.settings.getInt("lunarians-columns", 4);
    settings8 = Core.settings;
    byte b1 = 1;
    showTitle = settings8.getBool("lunarians-show-title", b1);
    settings8 = Core.settings;
    float f2 = 1.0F;
    opacity = settings8.getFloat("lunarians-opacity", f2);
    holdSeconds = Core.settings.getFloat("lunarians-hold-seconds", 3.0F);
    panelMinimized = Core.settings.getBool("lunarians-panel-minimized", false);
    settings8 = Core.settings;
    String str10 = "lunarians-cat-count";
    int j = settings8.getInt(str10, b1);
    byte b2 = 5;
    float f3 = 7.0E-45F;
    categoryCount = Mathf.clamp(j, b1, b2);
    settings8 = Core.settings;
    String str11 = "lunarians-cat-current";
    j = settings8.getInt(str11, 0);
    int k = categoryCount - b1;
    currentCategory = Mathf.clamp(j, 0, k);
    j = 0;
    float f4 = 0.0F;
    settings8 = null;
    while (j < b2) {
      String[] arrayOfString = categoryNames;
      Settings settings = Core.settings;
      StringBuilder stringBuilder = new StringBuilder();
      this("lunarians-cat-name-");
      stringBuilder.append(j);
      String str17 = stringBuilder.toString();
      String str18 = categoryNames[j];
      String str16 = settings.getString(str17, str18);
      arrayOfString[j] = str16;
      j++;
    } 
    enableEnemyCoreAlert = Core.settings.getBool("lunarians-enemy-core-alert", false);
    settings8 = Core.settings;
    k = 1084227584;
    float f5 = 5.0F;
    attackMessageDelay = settings8.getFloat("lunarians-attack-msg-delay", f5);
    enemyAlertRange = Core.settings.getFloat("lunarians-enemy-alert-range", f1);
    enableAutoFill = Core.settings.getBool("lunarians-autofill", false);
    autoFillRange = Core.settings.getFloat("lunarians-autofill-range", 15.0F);
    settings8 = Core.settings;
    i = 1056964608;
    f1 = 0.5F;
    f4 = settings8.getFloat("lunarians-autofill-interval", f1);
    autoFillInterval = f4;
    autoFillTurrets = Core.settings.getBool("lunarians-autofill-turrets", b1);
    autoFillFactories = Core.settings.getBool("lunarians-autofill-factories", b1);
    enableFreeCam = Core.settings.getBool("lunarians-freecam", false);
    enableProductionAlert = Core.settings.getBool("lunarians-prod-alert", false);
    productionIgnoreOwn = Core.settings.getBool("lunarians-prod-ignore-own", b1);
    enableCamFollow = Core.settings.getBool("lunarians-camfollow", false);
    showSchematicPanel = Core.settings.getBool("lunarians-show-panel", b1);
    j = Core.settings.getInt("lunarians-schem-rotation", 0);
    b2 = 3;
    f3 = 4.2E-45F;
    schemRotation = Mathf.clamp(j, 0, b2);
    enableLossWarn = Core.settings.getBool("lunarians-loss-warn", false);
    lossPanel = Mathf.clamp(Core.settings.getInt("lunarians-loss-panel", 0), 0, b2);
    loadLoss();
    enableAutoUnit = Core.settings.getBool("lunarians-auto-unit", false);
    boolean bool1 = Core.settings.getBool("lunarians-auto-unit-heal", false);
    enableAutoUnitHeal = bool1;
    unitHealSlots.clear();
    settings8 = Core.settings;
    str10 = "lunarians-unit-heal-slots";
    String str14 = "";
    String str7 = settings8.getString(str10, str14);
    boolean bool2 = str7.isEmpty();
    int m = 8;
    float f6 = 1.1E-44F;
    String str12 = ";";
    if (!bool2) {
      String[] arrayOfString = str7.split(str12);
      int i2 = arrayOfString.length;
      int i3 = 0;
      Object object = null;
      while (i3 < i2) {
        String str = arrayOfString[i3];
        Seq seq = unitHealSlots;
        int i4 = seq.size;
        if (i4 < m) {
          UnitHealSlot unitHealSlot = UnitHealSlot.deserialize(str);
          seq.add(unitHealSlot);
          i3++;
        } 
      } 
    } 
    enableAutoMoon = Core.settings.getBool("lunarians-auto-moon", false);
    enableAutoConnectConveyor = Core.settings.getBool("lunarians-auto-conveyor", false);
    Settings settings7 = Core.settings;
    String str13 = "conveyor";
    conveyorBlock = settings7.getString("lunarians-conveyor-block", str13);
    settings7 = Core.settings;
    int n = -1 << -1;
    conveyorStartX = settings7.getInt("lunarians-conveyor-sx", n);
    conveyorStartY = Core.settings.getInt("lunarians-conveyor-sy", n);
    enableAutoDrill = Core.settings.getBool("lunarians-auto-drill", false);
    settings7 = Core.settings;
    String str15 = "mechanical-drill";
    drillBlock = settings7.getString("lunarians-drill-block", str15);
    drillStartX = Core.settings.getInt("lunarians-drill-sx", n);
    drillStartY = Core.settings.getInt("lunarians-drill-sy", n);
    drillMaxTiles = Core.settings.getInt("lunarians-drill-max-tiles", 400);
    drillMinOres = Core.settings.getInt("lunarians-drill-min-ores", b1);
    settings7 = Core.settings;
    byte b3 = 10;
    drillOptQuality = settings7.getInt("lunarians-drill-opt", b3);
    drillPlaceWaterPower = Core.settings.getBool("lunarians-drill-water-power", false);
    enableAutoDrillConveyor = Core.settings.getBool("lunarians-auto-drill-conveyor", false);
    drillExitDir = Core.settings.getInt("lunarians-drill-exit-dir", 0);
    drillLayoutMode = Core.settings.getInt("lunarians-drill-layout", 0);
    drillPerfect = Core.settings.getBool("lunarians-drill-perfect", false);
    bool1 = Core.settings.getBool("lunarians-dock-auto", false);
    dockAuto = bool1;
    moonSlots.clear();
    settings7 = Core.settings;
    str10 = "lunarians-moon-slots";
    String str6 = settings7.getString(str10, str14);
    bool2 = str6.isEmpty();
    if (!bool2) {
      String[] arrayOfString = str6.split(str12);
      int i2 = arrayOfString.length;
      int i3 = 0;
      float f = 0.0F;
      Object object = null;
      while (i3 < i2) {
        String str = arrayOfString[i3];
        Seq seq = moonSlots;
        int i4 = seq.size;
        if (i4 < m) {
          MoonSlot moonSlot = MoonSlot.deserialize(str);
          seq.add(moonSlot);
          i3++;
        } 
      } 
    } 
    enableAutoCargo = Core.settings.getBool("lunarians-auto-cargo", false);
    enableAutoContainer = Core.settings.getBool("lunarians-auto-container", false);
    containerUnit = Core.settings.getString("lunarians-container-unit", str14);
    containerMaxUnits = Core.settings.getInt("lunarians-container-max", b1);
    containerPickX = Core.settings.getInt("lunarians-container-pick-x", n);
    containerPickY = Core.settings.getInt("lunarians-container-pick-y", n);
    containerDropX = Core.settings.getInt("lunarians-container-drop-x", n);
    containerDropY = Core.settings.getInt("lunarians-container-drop-y", n);
    f4 = Core.settings.getFloat("lunarians-container-wait", f5);
    containerWaitSec = f4;
    autoAttackNavalOnly = Core.settings.getBool("lunarians-attack-naval", false);
    bool1 = Core.settings.getBool("lunarians-heal-naval", false);
    autoHealNavalOnly = bool1;
    cargoSlots.clear();
    Settings settings6 = Core.settings;
    str10 = "lunarians-cargo-slots";
    String str5 = settings6.getString(str10, str14);
    bool2 = str5.isEmpty();
    if (!bool2) {
      String[] arrayOfString = str5.split(str12);
      int i2 = arrayOfString.length;
      n = 0;
      str13 = null;
      while (n < i2) {
        String str = arrayOfString[n];
        Seq seq = cargoSlots;
        int i3 = seq.size;
        if (i3 < b3) {
          CargoSlot cargoSlot = CargoSlot.deserialize(str);
          seq.add(cargoSlot);
          n++;
        } 
      } 
    } 
    bool1 = Core.settings.getBool("lunarians-auto-defence", false);
    enableAutoDefence = bool1;
    defenceSlots.clear();
    Settings settings5 = Core.settings;
    str10 = "lunarians-defence-slots";
    String str4 = settings5.getString(str10, str14);
    bool2 = str4.isEmpty();
    if (!bool2) {
      String[] arrayOfString = str4.split(str12);
      int i2 = arrayOfString.length;
      n = 0;
      str13 = null;
      while (n < i2) {
        String str = arrayOfString[n];
        Seq seq = defenceSlots;
        int i3 = seq.size;
        if (i3 < m) {
          DefenceSlot defenceSlot = DefenceSlot.deserialize(str);
          seq.add(defenceSlot);
          n++;
        } 
      } 
    } 
    bool1 = Core.settings.getBool("lunarians-auto-shield", false);
    enableAutoShield = bool1;
    shieldSlots.clear();
    Settings settings4 = Core.settings;
    str10 = "lunarians-shield-slots";
    String str3 = settings4.getString(str10, str14);
    bool2 = str3.isEmpty();
    if (!bool2) {
      String[] arrayOfString = str3.split(str12);
      int i2 = arrayOfString.length;
      n = 0;
      str13 = null;
      while (n < i2) {
        String str = arrayOfString[n];
        Seq seq = shieldSlots;
        int i3 = seq.size;
        if (i3 < m) {
          ShieldSlot shieldSlot = ShieldSlot.deserialize(str);
          seq.add(shieldSlot);
          n++;
        } 
      } 
    } 
    bool1 = Core.settings.getBool("lunarians-auto-heal", false);
    enableAutoHeal = bool1;
    Settings settings3 = Core.settings;
    m = 1109393408;
    f6 = 40.0F;
    autoHealPercent = settings3.getFloat("lunarians-heal-pct", f6);
    f4 = Core.settings.getFloat("lunarians-heal-search", f6);
    autoHealSearchRange = f4;
    healSlots.clear();
    settings3 = Core.settings;
    str10 = "lunarians-heal-slots";
    String str2 = settings3.getString(str10, str14);
    bool2 = str2.isEmpty();
    if (!bool2) {
      String[] arrayOfString = str2.split(str12);
      int i2 = arrayOfString.length;
      n = 0;
      str13 = null;
      while (n < i2) {
        String str = arrayOfString[n];
        Seq seq = healSlots;
        int i3 = seq.size;
        if (i3 < b3) {
          HealSlot healSlot = HealSlot.deserialize(str);
          seq.add(healSlot);
          n++;
        } 
      } 
    } 
    enableAutoAttack = Core.settings.getBool("lunarians-auto-attack", false);
    Settings settings2 = Core.settings;
    bool2 = Core.settings.getBool("lunarians-pvp-mode", false);
    pvpMode = settings2.getBool("lunarians-balanced-mode", bool2);
    settings2 = Core.settings;
    Settings settings9 = Core.settings;
    int i1 = 1083179008;
    float f7 = 4.5F;
    f3 = settings9.getFloat("lunarians-pvp-delay", f7);
    str13 = "lunarians-balanced-delay";
    f4 = settings2.getFloat(str13, f3);
    pvpDelay = f4;
    bool1 = Core.settings.getBool("lunarians-attack-heal", b1);
    autoAttackHeal = bool1;
    attackSlots.clear();
    settings2 = Core.settings;
    String str9 = "lunarians-attack-slots";
    String str1 = settings2.getString(str9, str14);
    bool2 = str1.isEmpty();
    if (!bool2) {
      String[] arrayOfString = str1.split(str12);
      int i2 = arrayOfString.length;
      i = 0;
      f1 = 0.0F;
      str14 = null;
      while (i < i2) {
        str12 = arrayOfString[i];
        Seq seq = attackSlots;
        i1 = seq.size;
        if (i1 < b3) {
          AttackSlot attackSlot = AttackSlot.deserialize(str12);
          seq.add(attackSlot);
          i++;
        } 
      } 
    } 
    betaWidth = Mathf.clamp(Core.settings.getFloat("lunarians-beta-width", 250.0F), 180.0F, 450.0F);
    Settings settings1 = Core.settings;
    f1 = 50.0F;
    betaHeightPct = Mathf.clamp(settings1.getFloat("lunarians-beta-height", f1), 25.0F, 90.0F);
    loadUnits();
    panelWidth = Mathf.clamp(panelWidth, 160.0F, 700.0F);
    panelHeight = Mathf.clamp(panelHeight, 120.0F, 600.0F);
    slotSize = Mathf.clamp(slotSize, 36.0F, 72.0F);
    columns = Mathf.clamp(columns, 0, b3);
    slotCount = Mathf.clamp(slotCount, b1, 300);
    opacity = Mathf.clamp(opacity, 0.3F, f2);
    holdSeconds = Mathf.clamp(holdSeconds, f2, 10.0F);
    attackMessageDelay = Mathf.clamp(attackMessageDelay, f2, 60.0F);
    enemyAlertRange = Mathf.clamp(enemyAlertRange, f5, f1);
    autoFillRange = Mathf.clamp(autoFillRange, f5, f6);
    autoFillInterval = Mathf.clamp(autoFillInterval, 0.2F, f5);
    LunariansI18n.setLanguage(language);
  }
  
  private static void loadLoss() {
    int i = 0;
    Settings settings1 = null;
    int j = 0;
    Settings settings2 = null;
    while (true) {
      byte b = 4;
      if (j < b) {
        Seq seq = lossSlots(j);
        seq.clear();
        Settings settings = Core.settings;
        StringBuilder stringBuilder = new StringBuilder();
        this("lunarians-loss-slots-");
        stringBuilder.append(j);
        String str2 = stringBuilder.toString();
        String str3 = "";
        String str1 = settings.getString(str2, str3);
        boolean bool1 = str1.isEmpty();
        if (!bool1) {
          str2 = ";";
          String[] arrayOfString = str1.split(str2);
          int k = arrayOfString.length;
          int m = 0;
          str3 = null;
          while (m < k) {
            String str4 = arrayOfString[m];
            int n = seq.size;
            byte b1 = 25;
            if (n < b1) {
              LossSlot lossSlot = LossSlot.deserialize(str4);
              seq.add(lossSlot);
              m++;
            } 
          } 
        } 
        j++;
        continue;
      } 
      settings2 = Core.settings;
      String str = "lunarians-loss-presets-v1";
      boolean bool = settings2.getBool(str, false);
      if (!bool)
        while (true) {
          byte b1 = 3;
          if (i < b1) {
            Seq seq = lossSlots(i);
            boolean bool2 = seq.isEmpty();
            if (bool2)
              applyLossPreset(i); 
            i++;
            continue;
          } 
          settings1 = Core.settings;
          Boolean bool1 = Boolean.TRUE;
          settings1.put(str, bool1);
          break;
        }  
      return;
    } 
  }
  
  private static void loadUnits() {
    unitSlots.clear();
    Settings settings = Core.settings;
    String str2 = "lunarians-unit-slots";
    String str3 = "";
    String str1 = settings.getString(str2, str3);
    boolean bool = str1.isEmpty();
    if (!bool) {
      str2 = ";";
      String[] arrayOfString = str1.split(str2);
      int i = arrayOfString.length;
      int j = 0;
      str3 = null;
      while (j < i) {
        String str = arrayOfString[j];
        Seq seq = unitSlots;
        int k = seq.size;
        byte b = 10;
        if (k < b) {
          UnitSlot unitSlot = UnitSlot.deserialize(str);
          seq.add(unitSlot);
          j++;
        } 
      } 
    } 
  }
  
  public static Seq lossPreset(int paramInt) {
    Seq seq = new Seq();
    this();
    String str1 = "graphite";
    String str2 = "lead";
    String str3 = "copper";
    float f1 = 4.0F;
    float f2 = 5.0F;
    float f3 = 6.0F;
    float f4 = 3.0F;
    float f5 = 2.0F;
    if (paramInt == 0) {
      slot(seq, str3, f3, f5);
      slot(seq, str2, f3, f5);
      slot(seq, "sand", f2, f5);
      slot(seq, "coal", f1, f5);
      slot(seq, str1, f4, f5);
      return seq;
    } 
    byte b1 = 1;
    String str4 = "metaglass";
    String str5 = "titanium";
    String str6 = "silicon";
    if (paramInt == b1) {
      float f = 15.0F;
      slot(seq, str3, f, f5);
      slot(seq, str2, f, f5);
      slot(seq, str1, f3, f5);
      slot(seq, str6, f2, f5);
      slot(seq, str5, f2, f5);
      slot(seq, str4, f1, f5);
      return seq;
    } 
    byte b2 = 2;
    if (paramInt == b2) {
      paramInt = 1092616192;
      float f = 10.0F;
      slot(seq, str6, f, f4);
      slot(seq, str5, f, f4);
      slot(seq, str4, f3, f4);
      slot(seq, "thorium", f2, f4);
      slot(seq, "plastanium", f1, f4);
      slot(seq, "phase-fabric", f5, f4);
      String str = "surge-alloy";
      slot(seq, str, f5, f4);
    } 
    return seq;
  }
  
  public static Seq lossSlots(int paramInt) {
    byte b = 1;
    if (paramInt != b) {
      b = 2;
      if (paramInt != b) {
        b = 3;
        return (paramInt != b) ? lossP0 : lossP3;
      } 
      return lossP2;
    } 
    return lossP1;
  }
  
  public static void resetAll() {
    float f = 20.0F;
    panelX = f;
    panelY = 100.0F;
    panelWidth = 280.0F;
    panelHeight = 200.0F;
    byte b = 12;
    slotCount = b;
    String str1 = "en";
    language = str1;
    slotSize = 56.0F;
    int i = 4;
    columns = i;
    char c = '\001';
    showTitle = c;
    opacity = 1.0F;
    holdSeconds = 3.0F;
    panelMinimized = false;
    categoryCount = c;
    currentCategory = 0;
    String str2 = "Extra";
    String str3 = "Default";
    String str4 = "PvP";
    String str5 = "Survival";
    categoryNames = new String[] { str3, str4, str5, "Attack", str2 };
    enableEnemyCoreAlert = false;
    attackMessageDelay = 5.0F;
    enemyAlertRange = f;
    enableAutoFill = false;
    autoFillRange = 15.0F;
    autoFillInterval = 0.5F;
    autoFillTurrets = c;
    autoFillFactories = c;
    enableFreeCam = false;
    enableProductionAlert = false;
    productionIgnoreOwn = c;
    enableCamFollow = false;
    showSchematicPanel = c;
    schemRotation = 0;
    enableLossWarn = false;
    lossPanel = 0;
    enableAutoUnit = false;
    enableAutoUnitHeal = false;
    Seq seq = unitHealSlots;
    seq.clear();
    enableAutoMoon = false;
    moonSlots.clear();
    enableAutoConnectConveyor = false;
    String str6 = "conveyor";
    conveyorBlock = str6;
    int j = -1 << -1;
    conveyorStartX = j;
    conveyorStartY = j;
    enableAutoUnitHeal = false;
    seq.clear();
    enableAutoCargo = false;
    cargoSlots.clear();
    enableAutoContainer = false;
    containerUnit = "";
    containerDropY = j;
    containerDropX = j;
    containerPickY = j;
    containerPickX = j;
    autoAttackNavalOnly = false;
    autoHealNavalOnly = false;
    enableAutoDefence = false;
    defenceSlots.clear();
    enableAutoShield = false;
    shieldSlots.clear();
    enableAutoHeal = false;
    f = 40.0F;
    autoHealPercent = f;
    autoHealSearchRange = f;
    healSlots.clear();
    enableAutoAttack = false;
    autoAttackHeal = c;
    attackSlots.clear();
    pvpMode = false;
    pvpDelay = 4.5F;
    betaWidth = 250.0F;
    betaHeightPct = 50.0F;
    unitSlots.clear();
    int k = 0;
    f = 0.0F;
    seq = null;
    while (k < i) {
      Seq seq1 = lossSlots(k);
      seq1.clear();
      c = '\003';
      if (k < c)
        applyLossPreset(k); 
      k++;
    } 
    k = 0;
    f = 0.0F;
    seq = null;
    while (true) {
      i = 5;
      if (k < i) {
        i = 0;
        while (true) {
          c = 'Ĭ';
          if (i < c) {
            Settings settings = Core.settings;
            StringBuilder stringBuilder2 = new StringBuilder();
            this("lunarians-slot-c");
            stringBuilder2.append(k);
            stringBuilder2.append("-");
            stringBuilder2.append(i);
            String str8 = stringBuilder2.toString();
            settings.remove(str8);
            settings = Core.settings;
            StringBuilder stringBuilder1 = new StringBuilder();
            str2 = "lunarians-slot-";
            this(str2);
            stringBuilder1.append(i);
            String str7 = stringBuilder1.toString();
            settings.remove(str7);
            i++;
            continue;
          } 
          k++;
        } 
        break;
      } 
      LunariansI18n.setLanguage(str1);
      save();
      return;
    } 
  }
  
  public static void save() {
    Settings settings = Core.settings;
    Float float_3 = Float.valueOf(panelX);
    settings.put("lunarians-panel-x", float_3);
    settings = Core.settings;
    float_3 = Float.valueOf(panelY);
    settings.put("lunarians-panel-y", float_3);
    settings = Core.settings;
    float_3 = Float.valueOf(panelWidth);
    settings.put("lunarians-panel-width", float_3);
    settings = Core.settings;
    float_3 = Float.valueOf(panelHeight);
    settings.put("lunarians-panel-height", float_3);
    settings = Core.settings;
    Integer integer3 = Integer.valueOf(slotCount);
    settings.put("lunarians-slot-count", integer3);
    settings = Core.settings;
    String str = language;
    settings.put("lunarians-language", str);
    settings = Core.settings;
    Float float_2 = Float.valueOf(slotSize);
    settings.put("lunarians-slot-size", float_2);
    settings = Core.settings;
    Integer integer2 = Integer.valueOf(columns);
    settings.put("lunarians-columns", integer2);
    settings = Core.settings;
    Boolean bool2 = Boolean.valueOf(showTitle);
    settings.put("lunarians-show-title", bool2);
    settings = Core.settings;
    Float float_1 = Float.valueOf(opacity);
    settings.put("lunarians-opacity", float_1);
    settings = Core.settings;
    float_1 = Float.valueOf(holdSeconds);
    settings.put("lunarians-hold-seconds", float_1);
    settings = Core.settings;
    Boolean bool1 = Boolean.valueOf(panelMinimized);
    settings.put("lunarians-panel-minimized", bool1);
    settings = Core.settings;
    Integer integer1 = Integer.valueOf(categoryCount);
    settings.put("lunarians-cat-count", integer1);
    settings = Core.settings;
    integer1 = Integer.valueOf(currentCategory);
    str = "lunarians-cat-current";
    settings.put(str, integer1);
    int i = 0;
    settings = null;
    int j = 0;
    integer1 = null;
    while (true) {
      byte b = 5;
      float f = 7.0E-45F;
      if (j < b) {
        Settings settings2 = Core.settings;
        StringBuilder stringBuilder1 = new StringBuilder();
        this("lunarians-cat-name-");
        stringBuilder1.append(j);
        String str4 = stringBuilder1.toString();
        String str5 = categoryNames[j];
        settings2.put(str4, str5);
        j++;
        continue;
      } 
      Settings settings1 = Core.settings;
      Boolean bool11 = Boolean.valueOf(enableEnemyCoreAlert);
      settings1.put("lunarians-enemy-core-alert", bool11);
      settings1 = Core.settings;
      Float float_7 = Float.valueOf(attackMessageDelay);
      settings1.put("lunarians-attack-msg-delay", float_7);
      settings1 = Core.settings;
      float_7 = Float.valueOf(enemyAlertRange);
      settings1.put("lunarians-enemy-alert-range", float_7);
      settings1 = Core.settings;
      Boolean bool10 = Boolean.valueOf(enableAutoFill);
      settings1.put("lunarians-autofill", bool10);
      settings1 = Core.settings;
      Float float_6 = Float.valueOf(autoFillRange);
      settings1.put("lunarians-autofill-range", float_6);
      settings1 = Core.settings;
      f = autoFillInterval;
      float_6 = Float.valueOf(f);
      settings1.put("lunarians-autofill-interval", float_6);
      settings1 = Core.settings;
      Boolean bool9 = Boolean.valueOf(autoFillTurrets);
      settings1.put("lunarians-autofill-turrets", bool9);
      settings1 = Core.settings;
      bool9 = Boolean.valueOf(autoFillFactories);
      settings1.put("lunarians-autofill-factories", bool9);
      settings1 = Core.settings;
      bool9 = Boolean.valueOf(enableFreeCam);
      settings1.put("lunarians-freecam", bool9);
      settings1 = Core.settings;
      bool9 = Boolean.valueOf(enableProductionAlert);
      settings1.put("lunarians-prod-alert", bool9);
      settings1 = Core.settings;
      bool9 = Boolean.valueOf(productionIgnoreOwn);
      settings1.put("lunarians-prod-ignore-own", bool9);
      settings1 = Core.settings;
      bool9 = Boolean.valueOf(enableCamFollow);
      settings1.put("lunarians-camfollow", bool9);
      settings1 = Core.settings;
      bool9 = Boolean.valueOf(showSchematicPanel);
      settings1.put("lunarians-show-panel", bool9);
      settings1 = Core.settings;
      Integer integer9 = Integer.valueOf(schemRotation);
      settings1.put("lunarians-schem-rotation", integer9);
      settings1 = Core.settings;
      Boolean bool8 = Boolean.valueOf(enableLossWarn);
      settings1.put("lunarians-loss-warn", bool8);
      settings1 = Core.settings;
      Integer integer8 = Integer.valueOf(lossPanel);
      settings1.put("lunarians-loss-panel", integer8);
      saveLoss();
      settings1 = Core.settings;
      Boolean bool7 = Boolean.valueOf(enableAutoUnit);
      settings1.put("lunarians-auto-unit", bool7);
      settings1 = Core.settings;
      bool7 = Boolean.valueOf(enableAutoUnitHeal);
      String str2 = "lunarians-auto-unit-heal";
      settings1.put(str2, bool7);
      settings1 = Core.settings;
      bool7 = Boolean.valueOf(enableAutoMoon);
      settings1.put("lunarians-auto-moon", bool7);
      settings1 = Core.settings;
      bool7 = Boolean.valueOf(enableAutoConnectConveyor);
      settings1.put("lunarians-auto-conveyor", bool7);
      settings1 = Core.settings;
      String str3 = conveyorBlock;
      settings1.put("lunarians-conveyor-block", str3);
      settings1 = Core.settings;
      Integer integer7 = Integer.valueOf(conveyorStartX);
      settings1.put("lunarians-conveyor-sx", integer7);
      settings1 = Core.settings;
      integer7 = Integer.valueOf(conveyorStartY);
      settings1.put("lunarians-conveyor-sy", integer7);
      settings1 = Core.settings;
      Boolean bool6 = Boolean.valueOf(enableAutoDrill);
      settings1.put("lunarians-auto-drill", bool6);
      settings1 = Core.settings;
      str3 = drillBlock;
      settings1.put("lunarians-drill-block", str3);
      settings1 = Core.settings;
      Integer integer6 = Integer.valueOf(drillStartX);
      settings1.put("lunarians-drill-sx", integer6);
      settings1 = Core.settings;
      integer6 = Integer.valueOf(drillStartY);
      settings1.put("lunarians-drill-sy", integer6);
      settings1 = Core.settings;
      integer6 = Integer.valueOf(drillMaxTiles);
      settings1.put("lunarians-drill-max-tiles", integer6);
      settings1 = Core.settings;
      integer6 = Integer.valueOf(drillMinOres);
      settings1.put("lunarians-drill-min-ores", integer6);
      settings1 = Core.settings;
      integer6 = Integer.valueOf(drillOptQuality);
      settings1.put("lunarians-drill-opt", integer6);
      settings1 = Core.settings;
      Boolean bool5 = Boolean.valueOf(drillPlaceWaterPower);
      settings1.put("lunarians-drill-water-power", bool5);
      settings1 = Core.settings;
      bool5 = Boolean.valueOf(enableAutoDrillConveyor);
      settings1.put("lunarians-auto-drill-conveyor", bool5);
      settings1 = Core.settings;
      Integer integer5 = Integer.valueOf(drillExitDir);
      settings1.put("lunarians-drill-exit-dir", integer5);
      settings1 = Core.settings;
      integer5 = Integer.valueOf(drillLayoutMode);
      settings1.put("lunarians-drill-layout", integer5);
      settings1 = Core.settings;
      Boolean bool4 = Boolean.valueOf(drillPerfect);
      settings1.put("lunarians-drill-perfect", bool4);
      settings1 = Core.settings;
      bool4 = Boolean.valueOf(dockAuto);
      str3 = "lunarians-dock-auto";
      settings1.put(str3, bool4);
      settings1 = Core.settings;
      bool4 = Boolean.valueOf(enableAutoUnitHeal);
      settings1.put(str2, bool4);
      settings1 = Core.settings;
      bool4 = Boolean.valueOf(enableAutoCargo);
      settings1.put("lunarians-auto-cargo", bool4);
      settings1 = Core.settings;
      boolean bool = enableAutoContainer;
      bool4 = Boolean.valueOf(bool);
      str2 = "lunarians-auto-container";
      settings1.put(str2, bool4);
      settings1 = Core.settings;
      String str1 = containerUnit;
      if (str1 == null)
        str1 = ""; 
      settings1.put("lunarians-container-unit", str1);
      settings1 = Core.settings;
      Integer integer4 = Integer.valueOf(containerMaxUnits);
      settings1.put("lunarians-container-max", integer4);
      settings1 = Core.settings;
      integer4 = Integer.valueOf(containerPickX);
      settings1.put("lunarians-container-pick-x", integer4);
      settings1 = Core.settings;
      integer4 = Integer.valueOf(containerPickY);
      settings1.put("lunarians-container-pick-y", integer4);
      settings1 = Core.settings;
      integer4 = Integer.valueOf(containerDropX);
      settings1.put("lunarians-container-drop-x", integer4);
      settings1 = Core.settings;
      integer4 = Integer.valueOf(containerDropY);
      settings1.put("lunarians-container-drop-y", integer4);
      settings1 = Core.settings;
      Float float_5 = Float.valueOf(containerWaitSec);
      settings1.put("lunarians-container-wait", float_5);
      settings1 = Core.settings;
      Boolean bool3 = Boolean.valueOf(autoAttackNavalOnly);
      settings1.put("lunarians-attack-naval", bool3);
      settings1 = Core.settings;
      bool3 = Boolean.valueOf(autoHealNavalOnly);
      settings1.put("lunarians-heal-naval", bool3);
      settings1 = Core.settings;
      bool3 = Boolean.valueOf(enableAutoDefence);
      settings1.put("lunarians-auto-defence", bool3);
      settings1 = Core.settings;
      bool3 = Boolean.valueOf(enableAutoShield);
      settings1.put("lunarians-auto-shield", bool3);
      settings1 = Core.settings;
      bool3 = Boolean.valueOf(enableAutoHeal);
      settings1.put("lunarians-auto-heal", bool3);
      settings1 = Core.settings;
      Float float_4 = Float.valueOf(autoHealPercent);
      settings1.put("lunarians-heal-pct", float_4);
      settings1 = Core.settings;
      float_4 = Float.valueOf(autoHealSearchRange);
      str2 = "lunarians-heal-search";
      settings1.put(str2, float_4);
      StringBuilder stringBuilder = new StringBuilder();
      this();
      bool = false;
      f = 0.0F;
      float_4 = null;
      while (true) {
        Seq seq = healSlots;
        int k = seq.size;
        byte b1 = 59;
        if (bool < k) {
          if (bool)
            stringBuilder.append(b1); 
          String str6 = ((HealSlot)seq.get(bool)).serialize();
          stringBuilder.append(str6);
          int m = bool + 1;
          continue;
        } 
        Settings settings3 = Core.settings;
        String str4 = stringBuilder.toString();
        settings3.put("lunarians-heal-slots", str4);
        Settings settings2 = Core.settings;
        Boolean bool13 = Boolean.valueOf(enableAutoAttack);
        settings2.put("lunarians-auto-attack", bool13);
        settings2 = Core.settings;
        bool13 = Boolean.valueOf(pvpMode);
        settings2.put("lunarians-pvp-mode", bool13);
        settings2 = Core.settings;
        bool13 = Boolean.valueOf(pvpMode);
        settings2.put("lunarians-balanced-mode", bool13);
        settings2 = Core.settings;
        Float float_ = Float.valueOf(pvpDelay);
        settings2.put("lunarians-pvp-delay", float_);
        settings2 = Core.settings;
        float_ = Float.valueOf(pvpDelay);
        settings2.put("lunarians-balanced-delay", float_);
        settings2 = Core.settings;
        Boolean bool12 = Boolean.valueOf(autoAttackHeal);
        String str5 = "lunarians-attack-heal";
        settings2.put(str5, bool12);
        StringBuilder stringBuilder1 = new StringBuilder();
        this();
        bool = false;
        f = 0.0F;
        bool12 = null;
        while (true) {
          Seq seq1 = cargoSlots;
          k = seq1.size;
          if (bool < k) {
            if (bool)
              stringBuilder1.append(b1); 
            String str8 = ((CargoSlot)seq1.get(bool)).serialize();
            stringBuilder1.append(str8);
            int m = bool + 1;
            continue;
          } 
          Settings settings5 = Core.settings;
          String str6 = stringBuilder1.toString();
          settings5.put("lunarians-cargo-slots", str6);
          Settings settings4 = Core.settings;
          Float float_8 = Float.valueOf(betaWidth);
          settings4.put("lunarians-beta-width", float_8);
          settings4 = Core.settings;
          float_8 = Float.valueOf(betaHeightPct);
          String str7 = "lunarians-beta-height";
          settings4.put(str7, float_8);
          StringBuilder stringBuilder2 = new StringBuilder();
          this();
          bool = false;
          f = 0.0F;
          float_8 = null;
          while (true) {
            Seq seq2 = defenceSlots;
            k = seq2.size;
            if (bool < k) {
              if (bool)
                stringBuilder2.append(b1); 
              String str10 = ((DefenceSlot)seq2.get(bool)).serialize();
              stringBuilder2.append(str10);
              int m = bool + 1;
              continue;
            } 
            Settings settings6 = Core.settings;
            String str9 = "lunarians-defence-slots";
            String str8 = stringBuilder2.toString();
            settings6.put(str9, str8);
            StringBuilder stringBuilder3 = new StringBuilder();
            this();
            while (true) {
              Seq seq3 = shieldSlots;
              int m = seq3.size;
              if (i < m) {
                if (i > 0)
                  stringBuilder3.append(b1); 
                String str11 = ((ShieldSlot)seq3.get(i)).serialize();
                stringBuilder3.append(str11);
                i++;
                continue;
              } 
              settings = Core.settings;
              String str10 = stringBuilder3.toString();
              settings.put("lunarians-shield-slots", str10);
              saveUnits();
              Core.settings.saveValues();
              return;
            } 
            break;
          } 
          break;
        } 
        break;
      } 
      break;
    } 
  }
  
  private static void saveLoss() {
    int i = 0;
    while (true) {
      byte b = 4;
      if (i < b) {
        StringBuilder stringBuilder1 = new StringBuilder();
        this();
        Seq seq = lossSlots(i);
        int j = 0;
        StringBuilder stringBuilder2 = null;
        while (true) {
          int k = seq.size;
          if (j < k) {
            if (j > 0) {
              k = 59;
              stringBuilder1.append(k);
            } 
            String str = ((LossSlot)seq.get(j)).serialize();
            stringBuilder1.append(str);
            j++;
            continue;
          } 
          Settings settings = Core.settings;
          stringBuilder2 = new StringBuilder();
          String str3 = "lunarians-loss-slots-";
          this(str3);
          stringBuilder2.append(i);
          String str2 = stringBuilder2.toString();
          String str1 = stringBuilder1.toString();
          settings.put(str2, str1);
          i++;
        } 
      } 
      break;
    } 
  }
  
  private static void saveUnits() {
    StringBuilder stringBuilder = new StringBuilder();
    this();
    int i = 0;
    Settings settings = null;
    while (true) {
      Seq seq = unitSlots;
      int j = seq.size;
      if (i < j) {
        if (i > 0) {
          j = 59;
          stringBuilder.append(j);
        } 
        String str1 = ((UnitSlot)seq.get(i)).serialize();
        stringBuilder.append(str1);
        i++;
        continue;
      } 
      settings = Core.settings;
      String str = stringBuilder.toString();
      settings.put("lunarians-unit-slots", str);
      return;
    } 
  }
  
  private static void slot(Seq paramSeq, String paramString, float paramFloat1, float paramFloat2) {
    LossSlot lossSlot = new LossSlot();
    this();
    lossSlot.item = paramString;
    lossSlot.perSec = paramFloat1;
    lossSlot.delay = paramFloat2;
    paramSeq.add(lossSlot);
  }
  
  public static String slotKey(int paramInt) {
    int i = currentCategory;
    StringBuilder stringBuilder = new StringBuilder();
    this("lunarians-slot-c");
    stringBuilder.append(i);
    stringBuilder.append("-");
    stringBuilder.append(paramInt);
    return stringBuilder.toString();
  }
}
