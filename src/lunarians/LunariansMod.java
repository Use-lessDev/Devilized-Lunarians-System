package lunarians;

import arc.Core;
import arc.func.Boolp;
import arc.scene.Element;
import arc.util.Log;
import lunarians.hub.LunariansHub;
import lunarians.ui.BetaHud;
import lunarians.ui.SchematicPanel;
import mindustry.Vars;
import mindustry.mod.Mod;

public class LunariansMod extends Mod {
  public static BetaHud betaHud;
  
  public static SchematicPanel panel;
  
  public void init() {
    if (Vars.headless)
      return; 
    FirstRun.run();
    try {
      LunariansSettings.load();
    } catch (Throwable throwable) {
      Log.err("[Lunarians] settings load failed", throwable);
    } 
    try {
      panel = new SchematicPanel();
      panel.visible(new Boolp() {
            public boolean get() {
              return (Vars.state != null && Vars.state.isGame() && LunariansSettings.showSchematicPanel);
            }
          });
      Core.scene.add((Element)panel);
      try {
        panel.applySettings();
      } catch (Throwable throwable) {
        Log.err("[Lunarians] schematic panel settings", throwable);
      } 
    } catch (Throwable throwable) {
      Log.err("[Lunarians] SchematicPanel init failed", throwable);
    } 
    try {
      betaHud = new BetaHud();
      betaHud.visible(new Boolp() {
            public boolean get() {
              return (Vars.state != null && Vars.state.isGame());
            }
          });
      Core.scene.add((Element)betaHud);
    } catch (Throwable throwable) {
      Log.err("[Lunarians] BetaHud init failed (mod still loads)", throwable);
      betaHud = null;
    } 
    safeInit("EnemyCoreAlert", new Runnable() {
          public void run() {
            EnemyCoreAlert.init();
          }
        });
    safeInit("AutoFill", new Runnable() {
          public void run() {
            AutoFill.init();
          }
        });
    safeInit("FreeCam", new Runnable() {
          public void run() {
            FreeCam.init();
          }
        });
    safeInit("ProductionAlert", new Runnable() {
          public void run() {
            ProductionAlert.init();
          }
        });
    safeInit("SchematicPlacer", new Runnable() {
          public void run() {
            SchematicPlacer.init();
          }
        });
    safeInit("ResourceLossWarn", new Runnable() {
          public void run() {
            ResourceLossWarn.init();
          }
        });
    safeInit("AutoUnit", new Runnable() {
          public void run() {
            AutoUnit.init();
          }
        });
    safeInit("AutoCargo", new Runnable() {
          public void run() {
            AutoCargo.init();
          }
        });
    safeInit("AutoContainer", new Runnable() {
          public void run() {
            AutoContainer.init();
          }
        });
    safeInit("CamFollow", new Runnable() {
          public void run() {
            CamFollow.init();
          }
        });
    safeInit("AutoDefence", new Runnable() {
          public void run() {
            AutoDefence.init();
          }
        });
    safeInit("AutoShield", new Runnable() {
          public void run() {
            AutoShield.init();
          }
        });
    safeInit("AutoHeal", new Runnable() {
          public void run() {
            AutoHeal.init();
          }
        });
    safeInit("UiStyle", new Runnable() {
          public void run() {
            UiStyle.install();
          }
        });
    safeInit("Formations", new Runnable() {
          public void run() {
            Formations.init();
          }
        });
    safeInit("AutoEconomy", new Runnable() {
          public void run() {
            AutoEconomy.init();
          }
        });
    safeInit("AutoDock", new Runnable() {
          public void run() {
            AutoDock.init();
          }
        });
    safeInit("ProtonSchematics", new Runnable() {
          public void run() {
            ProtonSchematics.init();
          }
        });
    safeInit("MinerFix", new Runnable() {
          public void run() {
            MinerFix.init();
            MinerUnits.init();
            UsedArea.init();
            ConveyorUpgrade.init();
            EnergyWatch.init();
            PowerLink.init();
          }
        });
    safeInit("AutoAttack", new Runnable() {
          public void run() {
            AutoAttack.init();
          }
        });
    safeInit("AutoUnitHeal", new Runnable() {
          public void run() {
            AutoUnitHeal.init();
          }
        });
    safeInit("AutoMoon", new Runnable() {
          public void run() {
            AutoMoon.init();
          }
        });
    safeInit("AutoConnectConveyor", new Runnable() {
          public void run() {
            AutoConnectConveyor.init();
          }
        });
    safeInit("AutoDrill", new Runnable() {
          public void run() {
            AutoDrill.init();
          }
        });
    safeInit("AutoSet", new Runnable() {
          public void run() {
            AutoSet.init();
          }
        });
    safeInit("ServerGuard", new Runnable() {
          public void run() {
            ServerGuard.init();
          }
        });
    safeInit("WeatherSafe", new Runnable() {
          public void run() {
            WeatherSafe.init();
          }
        });
    safeInit("LunariansHub", new Runnable() {
          public void run() {
            LunariansHub.init();
          }
        });
    Log.info("[Lunarians] v3.18 loaded (client-only, PC + mobile).");
  }
  
  private static void safeInit(String paramString, Runnable paramRunnable) {
    try {
      paramRunnable.run();
    } catch (Throwable throwable) {
      Log.err("[Lunarians] " + paramString + " init failed", throwable);
    } 
  }
  
  public void loadContent() {}
}
