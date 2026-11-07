package lunarians;

import arc.Core;
import arc.func.Boolp;
import arc.util.Log;
import lunarians.hub.LunariansHub;
import lunarians.ui.BetaHud;
import lunarians.ui.SchematicPanel;
import mindustry.Vars;
import mindustry.mod.Mod;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/LunariansMod.class */
public class LunariansMod extends Mod {
    public static BetaHud betaHud;
    public static SchematicPanel panel;

    public void init() {
        if (Vars.headless) {
            return;
        }
        FirstRun.run();
        try {
            LunariansSettings.load();
        } catch (Throwable th) {
            Log.err("[Lunarians] settings load failed", th);
        }
        try {
            panel = new SchematicPanel();
            panel.visible(new Boolp() { // from class: lunarians.LunariansMod.1
                public boolean get() {
                    return Vars.state != null && Vars.state.isGame() && LunariansSettings.showSchematicPanel;
                }
            });
            Core.scene.add(panel);
            try {
                panel.applySettings();
            } catch (Throwable th2) {
                Log.err("[Lunarians] schematic panel settings", th2);
            }
        } catch (Throwable th3) {
            Log.err("[Lunarians] SchematicPanel init failed", th3);
        }
        try {
            betaHud = new BetaHud();
            betaHud.visible(new Boolp() { // from class: lunarians.LunariansMod.2
                public boolean get() {
                    return Vars.state != null && Vars.state.isGame();
                }
            });
            Core.scene.add(betaHud);
        } catch (Throwable th4) {
            Log.err("[Lunarians] BetaHud init failed (mod still loads)", th4);
            betaHud = null;
        }
        safeInit("EnemyCoreAlert", new Runnable() { // from class: lunarians.LunariansMod.3
            @Override // java.lang.Runnable
            public void run() {
                EnemyCoreAlert.init();
            }
        });
        safeInit("AutoFill", new Runnable() { // from class: lunarians.LunariansMod.4
            @Override // java.lang.Runnable
            public void run() {
                AutoFill.init();
            }
        });
        safeInit("FreeCam", new Runnable() { // from class: lunarians.LunariansMod.5
            @Override // java.lang.Runnable
            public void run() {
                FreeCam.init();
            }
        });
        safeInit("ProductionAlert", new Runnable() { // from class: lunarians.LunariansMod.6
            @Override // java.lang.Runnable
            public void run() {
                ProductionAlert.init();
            }
        });
        safeInit("SchematicPlacer", new Runnable() { // from class: lunarians.LunariansMod.7
            @Override // java.lang.Runnable
            public void run() {
                SchematicPlacer.init();
            }
        });
        safeInit("ResourceLossWarn", new Runnable() { // from class: lunarians.LunariansMod.8
            @Override // java.lang.Runnable
            public void run() {
                ResourceLossWarn.init();
            }
        });
        safeInit("AutoUnit", new Runnable() { // from class: lunarians.LunariansMod.9
            @Override // java.lang.Runnable
            public void run() {
                AutoUnit.init();
            }
        });
        safeInit("AutoCargo", new Runnable() { // from class: lunarians.LunariansMod.10
            @Override // java.lang.Runnable
            public void run() {
                AutoCargo.init();
            }
        });
        safeInit("AutoContainer", new Runnable() { // from class: lunarians.LunariansMod.11
            @Override // java.lang.Runnable
            public void run() {
                AutoContainer.init();
            }
        });
        safeInit("CamFollow", new Runnable() { // from class: lunarians.LunariansMod.12
            @Override // java.lang.Runnable
            public void run() {
                CamFollow.init();
            }
        });
        safeInit("AutoDefence", new Runnable() { // from class: lunarians.LunariansMod.13
            @Override // java.lang.Runnable
            public void run() {
                AutoDefence.init();
            }
        });
        safeInit("AutoShield", new Runnable() { // from class: lunarians.LunariansMod.14
            @Override // java.lang.Runnable
            public void run() {
                AutoShield.init();
            }
        });
        safeInit("AutoHeal", new Runnable() { // from class: lunarians.LunariansMod.15
            @Override // java.lang.Runnable
            public void run() {
                AutoHeal.init();
            }
        });
        safeInit("UiStyle", new Runnable() { // from class: lunarians.LunariansMod.16
            @Override // java.lang.Runnable
            public void run() {
                UiStyle.install();
            }
        });
        safeInit("Formations", new Runnable() { // from class: lunarians.LunariansMod.17
            @Override // java.lang.Runnable
            public void run() {
                Formations.init();
            }
        });
        safeInit("AutoEconomy", new Runnable() { // from class: lunarians.LunariansMod.18
            @Override // java.lang.Runnable
            public void run() {
                AutoEconomy.init();
            }
        });
        safeInit("AutoDock", new Runnable() { // from class: lunarians.LunariansMod.19
            @Override // java.lang.Runnable
            public void run() {
                AutoDock.init();
            }
        });
        safeInit("ProtonSchematics", new Runnable() { // from class: lunarians.LunariansMod.20
            @Override // java.lang.Runnable
            public void run() {
                ProtonSchematics.init();
            }
        });
        safeInit("MinerFix", new Runnable() { // from class: lunarians.LunariansMod.21
            @Override // java.lang.Runnable
            public void run() {
                MinerFix.init();
                MinerUnits.init();
                UsedArea.init();
                ConveyorUpgrade.init();
                EnergyWatch.init();
                PowerLink.init();
            }
        });
        safeInit("AutoAttack", new Runnable() { // from class: lunarians.LunariansMod.22
            @Override // java.lang.Runnable
            public void run() {
                AutoAttack.init();
            }
        });
        safeInit("AutoUnitHeal", new Runnable() { // from class: lunarians.LunariansMod.23
            @Override // java.lang.Runnable
            public void run() {
                AutoUnitHeal.init();
            }
        });
        safeInit("AutoMoon", new Runnable() { // from class: lunarians.LunariansMod.24
            @Override // java.lang.Runnable
            public void run() {
                AutoMoon.init();
            }
        });
        safeInit("AutoConnectConveyor", new Runnable() { // from class: lunarians.LunariansMod.25
            @Override // java.lang.Runnable
            public void run() {
                AutoConnectConveyor.init();
            }
        });
        safeInit("AutoDrill", new Runnable() { // from class: lunarians.LunariansMod.26
            @Override // java.lang.Runnable
            public void run() {
                AutoDrill.init();
            }
        });
        safeInit("AutoSet", new Runnable() { // from class: lunarians.LunariansMod.27
            @Override // java.lang.Runnable
            public void run() {
                AutoSet.init();
            }
        });
        safeInit("ServerGuard", new Runnable() { // from class: lunarians.LunariansMod.28
            @Override // java.lang.Runnable
            public void run() {
                ServerGuard.init();
            }
        });
        safeInit("WeatherSafe", new Runnable() { // from class: lunarians.LunariansMod.29
            @Override // java.lang.Runnable
            public void run() {
                WeatherSafe.init();
            }
        });
        safeInit("LunariansHub", new Runnable() { // from class: lunarians.LunariansMod.30
            @Override // java.lang.Runnable
            public void run() {
                LunariansHub.init();
            }
        });
        Log.info("[Lunarians] v3.18 loaded (client-only, PC + mobile).");
    }

    private static void safeInit(String str, Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable th) {
            Log.err("[Lunarians] " + str + " init failed", th);
        }
    }

    public void loadContent() {
    }
}
