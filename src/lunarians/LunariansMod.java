/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.func.Boolp
 *  arc.scene.Element
 *  arc.util.Log
 *  mindustry.Vars
 *  mindustry.mod.Mod
 */
package lunarians;

import arc.Core;
import arc.func.Boolp;
import arc.scene.Element;
import arc.util.Log;
import lunarians.AutoAttack;
import lunarians.AutoCargo;
import lunarians.AutoConnectConveyor;
import lunarians.AutoContainer;
import lunarians.AutoDefence;
import lunarians.AutoDock;
import lunarians.AutoDrill;
import lunarians.AutoEconomy;
import lunarians.AutoFill;
import lunarians.AutoHeal;
import lunarians.AutoMoon;
import lunarians.AutoSet;
import lunarians.AutoShield;
import lunarians.AutoUnit;
import lunarians.AutoUnitHeal;
import lunarians.CamFollow;
import lunarians.ConveyorUpgrade;
import lunarians.EnemyCoreAlert;
import lunarians.EnergyWatch;
import lunarians.FirstRun;
import lunarians.Formations;
import lunarians.FreeCam;
import lunarians.LunariansSettings;
import lunarians.MinerFix;
import lunarians.MinerUnits;
import lunarians.PowerLink;
import lunarians.ProductionAlert;
import lunarians.ProtonSchematics;
import lunarians.ResourceLossWarn;
import lunarians.SchematicPlacer;
import lunarians.ServerGuard;
import lunarians.UiStyle;
import lunarians.UsedArea;
import lunarians.WeatherSafe;
import lunarians.UnitConversations;
import lunarians.hub.LunariansHub;
import lunarians.ui.BetaHud;
import lunarians.ui.SchematicPanel;
import mindustry.Vars;
import mindustry.mod.Mod;

public class LunariansMod
extends Mod {
    public static BetaHud betaHud;
    public static SchematicPanel panel;

    public void init() {
        if (Vars.headless) {
            return;
        }
        FirstRun.run();
        try {
            LunariansSettings.load();
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] settings load failed", (Throwable)throwable);
        }
        try {
            panel = new SchematicPanel();
            panel.visible(new Boolp(){

                public boolean get() {
                    return Vars.state != null && Vars.state.isGame() && LunariansSettings.showSchematicPanel;
                }
            });
            Core.scene.add((Element)panel);
            try {
                panel.applySettings();
            }
            catch (Throwable throwable) {
                Log.err((String)"[Lunarians] schematic panel settings", (Throwable)throwable);
            }
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] SchematicPanel init failed", (Throwable)throwable);
        }
        try {
            betaHud = new BetaHud();
            betaHud.visible(new Boolp(){

                public boolean get() {
                    return Vars.state != null && Vars.state.isGame();
                }
            });
            Core.scene.add((Element)betaHud);
        }
        catch (Throwable throwable) {
            Log.err((String)"[Lunarians] BetaHud init failed (mod still loads)", (Throwable)throwable);
            betaHud = null;
        }
        LunariansMod.safeInit("EnemyCoreAlert", new Runnable(){

            @Override
            public void run() {
                EnemyCoreAlert.init();
            }
        });
        LunariansMod.safeInit("AutoFill", new Runnable(){

            @Override
            public void run() {
                AutoFill.init();
            }
        });
        LunariansMod.safeInit("FreeCam", new Runnable(){

            @Override
            public void run() {
                FreeCam.init();
            }
        });
        LunariansMod.safeInit("ProductionAlert", new Runnable(){

            @Override
            public void run() {
                ProductionAlert.init();
            }
        });
        LunariansMod.safeInit("SchematicPlacer", new Runnable(){

            @Override
            public void run() {
                SchematicPlacer.init();
            }
        });
        LunariansMod.safeInit("ResourceLossWarn", new Runnable(){

            @Override
            public void run() {
                ResourceLossWarn.init();
            }
        });
        LunariansMod.safeInit("AutoUnit", new Runnable(){

            @Override
            public void run() {
                AutoUnit.init();
            }
        });
        LunariansMod.safeInit("AutoCargo", new Runnable(){

            @Override
            public void run() {
                AutoCargo.init();
            }
        });
        LunariansMod.safeInit("AutoContainer", new Runnable(){

            @Override
            public void run() {
                AutoContainer.init();
            }
        });
        LunariansMod.safeInit("CamFollow", new Runnable(){

            @Override
            public void run() {
                CamFollow.init();
            }
        });
        LunariansMod.safeInit("AutoDefence", new Runnable(){

            @Override
            public void run() {
                AutoDefence.init();
            }
        });
        LunariansMod.safeInit("AutoShield", new Runnable(){

            @Override
            public void run() {
                AutoShield.init();
            }
        });
        LunariansMod.safeInit("AutoHeal", new Runnable(){

            @Override
            public void run() {
                AutoHeal.init();
            }
        });
        LunariansMod.safeInit("UiStyle", new Runnable(){

            @Override
            public void run() {
                UiStyle.install();
            }
        });
        LunariansMod.safeInit("Formations", new Runnable(){

            @Override
            public void run() {
                Formations.init();
            }
        });
        LunariansMod.safeInit("AutoEconomy", new Runnable(){

            @Override
            public void run() {
                AutoEconomy.init();
            }
        });
        LunariansMod.safeInit("AutoDock", new Runnable(){

            @Override
            public void run() {
                AutoDock.init();
            }
        });
        LunariansMod.safeInit("ProtonSchematics", new Runnable(){

            @Override
            public void run() {
                ProtonSchematics.init();
            }
        });
        LunariansMod.safeInit("MinerFix", new Runnable(){

            @Override
            public void run() {
                MinerFix.init();
                MinerUnits.init();
                UsedArea.init();
                ConveyorUpgrade.init();
                EnergyWatch.init();
                PowerLink.init();
            }
        });
        LunariansMod.safeInit("AutoAttack", new Runnable(){

            @Override
            public void run() {
                AutoAttack.init();
            }
        });
        LunariansMod.safeInit("AutoUnitHeal", new Runnable(){

            @Override
            public void run() {
                AutoUnitHeal.init();
            }
        });
        LunariansMod.safeInit("AutoMoon", new Runnable(){

            @Override
            public void run() {
                AutoMoon.init();
            }
        });
        LunariansMod.safeInit("AutoConnectConveyor", new Runnable(){

            @Override
            public void run() {
                AutoConnectConveyor.init();
            }
        });
        LunariansMod.safeInit("AutoDrill", new Runnable(){

            @Override
            public void run() {
                AutoDrill.init();
            }
        });
        LunariansMod.safeInit("AutoSet", new Runnable(){

            @Override
            public void run() {
                AutoSet.init();
            }
        });
        LunariansMod.safeInit("ServerGuard", new Runnable(){

            @Override
            public void run() {
                ServerGuard.init();
            }
        });
        LunariansMod.safeInit("WeatherSafe", new Runnable(){

            @Override
            public void run() {
                WeatherSafe.init();
            }
        });
        LunariansMod.safeInit("LunariansHub", new Runnable(){

            @Override
            public void run() {
                LunariansHub.init();
            }
        });
        LunariansMod.safeInit("UnitConversations", new Runnable(){

            @Override
            public void run() {
                UnitConversations.init();
            }
        });
        Log.info((Object)"[Lunarians] loaded (client-only, PC + mobile).");
    }

    private static void safeInit(String string, Runnable runnable) {
        try {
            runnable.run();
        }
        catch (Throwable throwable) {
            Log.err((String)("[Lunarians] " + string + " init failed"), (Throwable)throwable);
        }
    }

    public void loadContent() {
    }
}

