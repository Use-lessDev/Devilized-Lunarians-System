package lunarians;

import arc.util.Log;
import lunarians.ui.BetaHud;
import lunarians.ui.SchematicPanel;
import mindustry.Vars;
import mindustry.mod.Mod;

public class LunariansMod extends Mod {
  public static BetaHud betaHud;
  
  public static SchematicPanel panel;
  
  private static void safeInit(String paramString, Runnable paramRunnable) {
    try {
      return;
    } finally {
      paramRunnable = null;
      StringBuilder stringBuilder = new StringBuilder();
      this("[Lunarians] ");
      stringBuilder.append(paramString);
      stringBuilder.append(" init failed");
      Log.err(stringBuilder.toString(), (Throwable)paramRunnable);
    } 
  }
  
  public void init() {
    boolean bool = Vars.headless;
    if (bool)
      return; 
    FirstRun.run();
    try {
      LunariansSettings.load();
    } finally {
      Exception exception = null;
      String str = "[Lunarians] settings load failed";
    } 
    LunariansMod$3 lunariansMod$3 = new LunariansMod$3();
    this(this);
    safeInit("EnemyCoreAlert", lunariansMod$3);
    LunariansMod$4 lunariansMod$4 = new LunariansMod$4();
    this(this);
    safeInit("AutoFill", lunariansMod$4);
    LunariansMod$5 lunariansMod$5 = new LunariansMod$5();
    this(this);
    safeInit("FreeCam", lunariansMod$5);
    LunariansMod$6 lunariansMod$6 = new LunariansMod$6();
    this(this);
    safeInit("ProductionAlert", lunariansMod$6);
    LunariansMod$7 lunariansMod$7 = new LunariansMod$7();
    this(this);
    safeInit("SchematicPlacer", lunariansMod$7);
    LunariansMod$8 lunariansMod$8 = new LunariansMod$8();
    this(this);
    safeInit("ResourceLossWarn", lunariansMod$8);
    LunariansMod$9 lunariansMod$9 = new LunariansMod$9();
    this(this);
    safeInit("AutoUnit", lunariansMod$9);
    LunariansMod$10 lunariansMod$10 = new LunariansMod$10();
    this(this);
    safeInit("AutoCargo", lunariansMod$10);
    LunariansMod$11 lunariansMod$11 = new LunariansMod$11();
    this(this);
    safeInit("AutoContainer", lunariansMod$11);
    LunariansMod$12 lunariansMod$12 = new LunariansMod$12();
    this(this);
    safeInit("CamFollow", lunariansMod$12);
    LunariansMod$13 lunariansMod$13 = new LunariansMod$13();
    this(this);
    safeInit("AutoDefence", lunariansMod$13);
    LunariansMod$14 lunariansMod$14 = new LunariansMod$14();
    this(this);
    safeInit("AutoShield", lunariansMod$14);
    LunariansMod$15 lunariansMod$15 = new LunariansMod$15();
    this(this);
    safeInit("AutoHeal", lunariansMod$15);
    LunariansMod$16 lunariansMod$16 = new LunariansMod$16();
    this(this);
    safeInit("UiStyle", lunariansMod$16);
    LunariansMod$17 lunariansMod$17 = new LunariansMod$17();
    this(this);
    safeInit("Formations", lunariansMod$17);
    LunariansMod$18 lunariansMod$18 = new LunariansMod$18();
    this(this);
    safeInit("AutoEconomy", lunariansMod$18);
    LunariansMod$19 lunariansMod$19 = new LunariansMod$19();
    this(this);
    safeInit("AutoDock", lunariansMod$19);
    LunariansMod$20 lunariansMod$20 = new LunariansMod$20();
    this(this);
    safeInit("ProtonSchematics", lunariansMod$20);
    LunariansMod$21 lunariansMod$21 = new LunariansMod$21();
    this(this);
    safeInit("MinerFix", lunariansMod$21);
    LunariansMod$22 lunariansMod$22 = new LunariansMod$22();
    this(this);
    safeInit("AutoAttack", lunariansMod$22);
    LunariansMod$23 lunariansMod$23 = new LunariansMod$23();
    this(this);
    safeInit("AutoUnitHeal", lunariansMod$23);
    LunariansMod$24 lunariansMod$24 = new LunariansMod$24();
    this(this);
    safeInit("AutoMoon", lunariansMod$24);
    LunariansMod$25 lunariansMod$25 = new LunariansMod$25();
    this(this);
    safeInit("AutoConnectConveyor", lunariansMod$25);
    LunariansMod$26 lunariansMod$26 = new LunariansMod$26();
    this(this);
    safeInit("AutoDrill", lunariansMod$26);
    LunariansMod$27 lunariansMod$27 = new LunariansMod$27();
    this(this);
    safeInit("AutoSet", lunariansMod$27);
    LunariansMod$28 lunariansMod$28 = new LunariansMod$28();
    this(this);
    safeInit("ServerGuard", lunariansMod$28);
    LunariansMod$29 lunariansMod$29 = new LunariansMod$29();
    this(this);
    safeInit("WeatherSafe", lunariansMod$29);
    LunariansMod$30 lunariansMod$30 = new LunariansMod$30();
    this(this);
    safeInit("LunariansHub", lunariansMod$30);
    Log.info("[Lunarians] v3.18 loaded (client-only, PC + mobile).");
  }
  
  public void loadContent() {}
}
