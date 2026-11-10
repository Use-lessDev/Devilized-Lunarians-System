/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.func.Boolc
 *  arc.func.Cons
 *  arc.func.Prov
 *  arc.graphics.Color
 *  arc.scene.Action
 *  arc.scene.Element
 *  arc.scene.actions.Actions
 *  arc.scene.style.Drawable
 *  arc.scene.ui.ImageButton
 *  arc.scene.ui.ScrollPane
 *  arc.scene.ui.TextButton
 *  arc.scene.ui.layout.Cell
 *  arc.scene.ui.layout.Table
 *  arc.util.Log
 *  mindustry.Vars
 *  mindustry.gen.Icon
 *  mindustry.graphics.Pal
 *  mindustry.ui.Styles
 */
package lunarians.ui;

import arc.Core;
import arc.func.Boolc;
import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.Action;
import arc.scene.Element;
import arc.scene.actions.Actions;
import arc.scene.style.Drawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import lunarians.AutoAttack;
import lunarians.AutoCargo;
import lunarians.AutoConnectConveyor;
import lunarians.AutoDefence;
import lunarians.AutoDock;
import lunarians.AutoDrill;
import lunarians.AutoEconomy;
import lunarians.AutoHeal;
import lunarians.AutoMoon;
import lunarians.AutoSet;
import lunarians.AutoShield;
import lunarians.AutoUnit;
import lunarians.AutoUnitHeal;
import lunarians.ChatCommands;
import lunarians.Formations;
import lunarians.FreeCam;
import lunarians.LunariansI18n;
import lunarians.LunariansPresence;
import lunarians.LunariansSettings;
import lunarians.MinerUnits;
import lunarians.ProductionAlert;
import lunarians.UiStyle;
import lunarians.UnitConversations;
import lunarians.ui.AutoDrillUi;
import lunarians.ui.AutoFillConfigDialog;
import lunarians.ui.ConfigDialog;
import lunarians.ui.PositionPicker;
import lunarians.ui.UiText;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;

public class BetaHud
extends Table {
    private final Table panel;
    private final ImageButton toggleBtn;
    private boolean open = false;
    private Cell<ScrollPane> scrollCell;
    private final ScrollPane scroll;

    public BetaHud() {
        this.setFillParent(true);
        this.right().top();
        this.defaults().right();
        this.panel = new Table(UiStyle.panelBg());
        this.panel.visible = false;
        this.panel.defaults().growX().pad(4.0f);
        this.toggleBtn = new ImageButton((Drawable)Icon.rightOpen, Styles.clearNonei);
        this.toggleBtn.clicked(new Runnable(){

            @Override
            public void run() {
                BetaHud.this.toggle();
            }
        });
        try {
            this.toggleBtn.resizeImage(28.0f);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        this.scroll = new ScrollPane((Element)this.panel, Styles.smallPane);
        this.scroll.setScrollingDisabled(true, false);
        this.scroll.setFadeScrollBars(false);
        this.scroll.visible = false;
        Table table = new Table();
        this.scrollCell = (Cell)table.add((Element)this.scroll).top();
        this.applySize();
        table.add((Element)this.toggleBtn).size(40.0f, 40.0f).top().pad(6.0f);
        this.add((Element)table).top().right().padTop(120.0f);
        try {
            this.rebuildPanel();
        }
        catch (Throwable throwable) {
            this.panel.clear();
            this.panel.add((CharSequence)"Beta HUD error").color(Color.scarlet).pad(6.0f).row();
            Log.err((String)"[Lunarians] BetaHud rebuild failed", (Throwable)throwable);
        }
    }

    public void toggle() {
        this.open = !this.open;
        boolean bl = this.open;
        if (this.open) {
            try {
                this.rebuildPanel();
            }
            catch (Throwable throwable) {
                Log.err((String)"[Lunarians] BetaHud rebuild failed", (Throwable)throwable);
            }
            this.panel.visible = true;
            this.scroll.visible = true;
            this.panel.actions(new Action[]{Actions.sequence((Action)Actions.alpha((float)0.0f), (Action)Actions.fadeIn((float)0.2f))});
            try {
                this.toggleBtn.getStyle().imageUp = Icon.leftOpen;
            }
            catch (Throwable throwable) {}
        } else {
            this.panel.actions(new Action[]{Actions.sequence((Action)Actions.fadeOut((float)0.15f), (Action)Actions.run((Runnable)new Runnable(){

                @Override
                public void run() {
                    BetaHud.this.panel.visible = false;
                    BetaHud.this.scroll.visible = false;
                }
            }))});
            try {
                this.toggleBtn.getStyle().imageUp = Icon.rightOpen;
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    public void applySize() {
        try {
            float f = (float)Core.graphics.getHeight() * LunariansSettings.betaHeightPct / 100.0f;
            this.scrollCell.width(LunariansSettings.betaWidth).maxHeight(f);
            this.invalidateHierarchy();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public void refresh() {
        this.applySize();
        if (this.open) {
            try {
                this.rebuildPanel();
            }
            catch (Throwable throwable) {
                Log.err((String)"[Lunarians] BetaHud refresh failed", (Throwable)throwable);
            }
        }
    }

    private void rebuildPanel() {
        TextButton textButton2;
        Table table;
        this.panel.clear();
        UiStyle.banner(this.panel, "LUNARIANS", BetaHud.safeI18n("beta_features", "Beta features"));
        UiStyle.section(this.panel, BetaHud.t("Mode", "Modo"));
        String string = LunariansSettings.pvpMode ? "Balanced ON" : "Normal";
        TextButton textButton3 = (TextButton)this.panel.button(string, new Runnable(){

            @Override
            public void run() {
                LunariansSettings.pvpMode = !LunariansSettings.pvpMode;
                LunariansSettings.save();
                BetaHud.this.rebuildPanel();
            }
        }).growX().height(42.0f).pad(4.0f).get();
        textButton3.setColor(LunariansSettings.pvpMode ? Color.scarlet : Pal.accent);
        try {
            textButton3.getLabel().setWrap(true);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        this.panel.row();
        if (LunariansSettings.pvpMode) {
            table = new Table();
            table.add((CharSequence)"Delay:").left().padRight(4.0f);
            table.button("-", new Runnable(){

                @Override
                public void run() {
                    LunariansSettings.pvpDelay = Math.max(1.0f, LunariansSettings.pvpDelay - 0.5f);
                    LunariansSettings.save();
                    BetaHud.this.rebuildPanel();
                }
            }).size(36.0f, 32.0f);
            table.add((CharSequence)String.format("%.1fs", Float.valueOf(LunariansSettings.pvpDelay))).width(52.0f).center().pad(2.0f);
            table.button("+", new Runnable(){

                @Override
                public void run() {
                    LunariansSettings.pvpDelay = Math.min(15.0f, LunariansSettings.pvpDelay + 0.5f);
                    LunariansSettings.save();
                    BetaHud.this.rebuildPanel();
                }
            }).size(36.0f, 32.0f);
            this.panel.add((Element)table).left().pad(4.0f).row();
        }
        UiStyle.section(this.panel, BetaHud.t("Configuration", "Configura\u00e7\u00e3o"));
        table = (TextButton)this.panel.button(BetaHud.safeI18n("settings_btn", "Settings"), new Runnable(){

            @Override
            public void run() {
                block4: {
                    try {
                        new ConfigDialog().show();
                    }
                    catch (Throwable throwable) {
                        Log.err((String)"[Lunarians] ConfigDialog", (Throwable)throwable);
                        try {
                            AutoDrillUi.make().show();
                        }
                        catch (Throwable throwable2) {
                            if (Vars.ui == null) break block4;
                            Vars.ui.showInfoToast("Settings error", 3.0f);
                        }
                    }
                }
            }
        }).height(40.0f).growX().pad(4.0f).get();
        this.panel.row();
        UiStyle.section(this.panel, BetaHud.t("Auto Set and economy", "Auto Set e economia"));
        Table table2 = new Table();
        table2.add((CharSequence)"Auto Set").left().padRight(6.0f);
        table2.button("GO", new Runnable(){

            @Override
            public void run() {
                AutoSet.request();
            }
        }).size(52.0f, 40.0f).pad(2.0f);
        table2.button("X", new Runnable(){

            @Override
            public void run() {
                AutoSet.cancel();
            }
        }).size(40.0f, 40.0f).pad(2.0f);
        try {
            table2.label((Prov)new Prov<CharSequence>(){

                public CharSequence get() {
                    if (AutoSet.isRunning()) {
                        return "[orange]RUN[]";
                    }
                    if (AutoSet.isActive()) {
                        return "[yellow]\u2026[]";
                    }
                    return "";
                }
            }).padLeft(4.0f);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        this.panel.add((Element)table2).growX().left().pad(4.0f).row();
        table2 = new Table();
        try {
            UiText.checkSmall(table2, "Drill", LunariansSettings.enableAutoDrill, new Boolc(){

                public void get(boolean bl) {
                    LunariansSettings.enableAutoDrill = bl;
                    LunariansSettings.save();
                }
            }).left();
        }
        catch (Throwable throwable) {
            table2.check("Drill", LunariansSettings.enableAutoDrill, new Boolc(){

                public void get(boolean bl) {
                    LunariansSettings.enableAutoDrill = bl;
                    LunariansSettings.save();
                }
            }).left();
        }
        table2.button("Set", new Runnable(){

            @Override
            public void run() {
                PositionPicker.pick(new Cons<int[]>(){

                    public void get(int[] nArray) {
                        LunariansSettings.drillStartX = nArray[0];
                        LunariansSettings.drillStartY = nArray[1];
                        LunariansSettings.save();
                        if (Vars.ui != null) {
                            Vars.ui.showInfoToast("Ore: " + nArray[0] + "," + nArray[1], 2.0f);
                        }
                    }
                }, new Runnable(){

                    @Override
                    public void run() {
                    }
                });
            }
        }).width(52.0f).height(40.0f).padLeft(4.0f);
        table2.button("Gen", new Runnable(){

            @Override
            public void run() {
                AutoDrill.generate();
            }
        }).width(52.0f).height(40.0f).padLeft(2.0f);
        this.panel.add((Element)table2).growX().left().pad(4.0f).row();
        Table table3 = new Table();
        table3.add((CharSequence)"Exit").padRight(6.0f);
        String[] stringArray = new String[]{">", "^", "<", "v"};
        for (int i = 0; i < 4; ++i) {
            final int n = i;
            textButton2 = (TextButton)table3.button(stringArray[i], new Runnable(){

                @Override
                public void run() {
                    LunariansSettings.drillExitDir = n;
                    LunariansSettings.save();
                    BetaHud.this.rebuildPanel();
                }
            }).size(44.0f, 40.0f).pad(2.0f).get();
            try {
                if (LunariansSettings.drillExitDir == n) {
                    textButton2.setColor(Pal.accent);
                    continue;
                }
                textButton2.setColor(Color.lightGray);
                continue;
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        String string2 = LunariansSettings.drillLayoutMode == 1 ? "Full" : "Str";
        table3.button(string2, new Runnable(){

            @Override
            public void run() {
                LunariansSettings.drillLayoutMode = LunariansSettings.drillLayoutMode == 1 ? 0 : 1;
                LunariansSettings.save();
                BetaHud.this.rebuildPanel();
            }
        }).size(56.0f, 40.0f).padLeft(6.0f);
        this.panel.add((Element)table3).growX().left().pad(4.0f).row();
        table2 = new Table();
        try {
            UiText.checkSmall(table2, "D+C", LunariansSettings.enableAutoDrillConveyor, new Boolc(){

                public void get(boolean bl) {
                    LunariansSettings.enableAutoDrillConveyor = bl;
                    LunariansSettings.save();
                }
            }).left();
        }
        catch (Throwable throwable) {
            table2.check("D+C", LunariansSettings.enableAutoDrillConveyor, new Boolc(){

                public void get(boolean bl) {
                    LunariansSettings.enableAutoDrillConveyor = bl;
                    LunariansSettings.save();
                }
            }).left();
        }
        table2.button("Gen", new Runnable(){

            @Override
            public void run() {
                AutoDrill.generateWithConveyor();
            }
        }).width(52.0f).height(40.0f).padLeft(4.0f);
        this.panel.add((Element)table2).growX().left().pad(4.0f).row();
        Table table4 = new Table();
        table4.check("pt".equals(LunariansSettings.language) ? "Drills perfeitos (Beta)" : "Perfect drills (Beta)", LunariansSettings.drillPerfect, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.drillPerfect = bl;
                LunariansSettings.save();
            }
        }).left();
        this.panel.add((Element)table4).growX().left().pad(4.0f).row();
        Table tbDock = new Table();
        tbDock.check("pt".equals(LunariansSettings.language) ? "Auto Dock + Energia (Beta)" : "Auto Dock + Energy (Beta)", LunariansSettings.dockAuto, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.dockAuto = bl;
                LunariansSettings.save();
                if (bl) {
                    AutoDock.reset();
                    Vars.ui.showInfoToast("Core: " + AutoDock.coreInfo(), 5.0f);
                }
            }
        }).left();
        this.panel.add((Element)tbDock).growX().left().pad(4.0f).row();
        Table table5 = new Table();
        table5.check("pt".equals(LunariansSettings.language) ? "Economia em ordem (Beta)" : "Ordered economy (Beta)", AutoEconomy.enabled(), new Boolc(){

            public void get(boolean bl) {
                AutoEconomy.setEnabled(bl);
            }
        }).left();
        this.panel.add((Element)table5).growX().left().pad(4.0f).row();
        UiStyle.section(this.panel, BetaHud.t("Team and chat", "Equipe e chat"));
        Table table6 = new Table();
        table6.check("pt".equals(LunariansSettings.language) ? "Presen\u00e7a Lunarians (aura roxa)" : "Lunarians presence (purple aura)", LunariansPresence.enabled(), new Boolc(){

            public void get(boolean bl) {
                LunariansPresence.setEnabled(bl);
                Vars.ui.showInfoToast(bl ? "Presence ON. Reconecte ao servidor para os outros te verem." : "Presence OFF. Reconecte para remover a marca do nome.", 2.0f);
            }
        }).left();
        this.panel.add((Element)table6).growX().left().pad(4.0f).row();
        Table table7 = new Table();
        table7.check("pt".equals(LunariansSettings.language) ? "Comandos por chat" : "Chat commands", ChatCommands.on(), new Boolc(){

            public void get(boolean bl) {
                Core.settings.put("lunarians-chat-on", (Object)bl);
            }
        }).left();
        table7.button("...", new Runnable(){

            @Override
            public void run() {
                ChatCommands.openPanel();
            }
        }).width(52.0f).height(40.0f).padLeft(4.0f);
        this.panel.add((Element)table7).growX().left().pad(4.0f).row();
        Table table7c = new Table();
        table7c.check("pt".equals(LunariansSettings.language) ? "Conversas entre unidades (T1-T3 divertidas, T4-T5 sérias)" : "Unit conversations (T1-T3 playful, T4-T5 serious)", UnitConversations.enabled(), new Boolc(){

            public void get(boolean bl) {
                UnitConversations.setEnabled(bl);
            }
        }).left().wrap().growX();
        this.panel.add((Element)table7c).growX().left().pad(4.0f).row();
        UiStyle.section(this.panel, BetaHud.t("Units", "Unidades"));
        Table table8 = new Table();
        table8.check(MinerUnits.title() + " (Beta)", MinerUnits.enabled(), new Boolc(){

            public void get(boolean bl) {
                MinerUnits.setEnabled(bl);
                LunariansSettings.save();
            }
        }).left();
        table8.button("...", new Runnable(){

            @Override
            public void run() {
                MinerUnits.dialog().show();
            }
        }).width(52.0f).height(40.0f).padLeft(4.0f);
        this.panel.add((Element)table8).growX().left().pad(4.0f).row();
        
        // AutoAttack section
        UiStyle.section(this.panel, BetaHud.t("Auto Attack", "Auto Ataque"));
        Table tableAA = new Table();
        tableAA.check("pt".equals(LunariansSettings.language) ? "Auto Attack" : "Auto Attack", LunariansSettings.enableAutoAttack, new Boolc(){
            public void get(boolean bl) {
                LunariansSettings.enableAutoAttack = bl;
                LunariansSettings.save();
            }
        }).left();
        this.panel.add((Element)tableAA).growX().left().pad(4.0f).row();
        
        Table tableAARetreat = new Table();
        tableAARetreat.check("pt".equals(LunariansSettings.language) ? "Retreat por tipo" : "Retreat by type", Core.settings.getBool("lunarians-attack-flee", false), bl -> Core.settings.put("lunarians-attack-flee", (Object)bl)).left().wrap().growX();
        this.panel.add((Element)tableAARetreat).growX().left().pad(4.0f).row();
        
        Table tableAADodge = new Table();
        tableAADodge.check("pt".equals(LunariansSettings.language) ? "Desvio de projeteis" : "Projectile dodge", AutoAttack.enableAutoDodge(), bl -> Core.settings.put("lunarians-auto-dodge", (Object)bl)).left().wrap().growX();
        this.panel.add((Element)tableAADodge).growX().left().pad(4.0f).row();
        
        Table tableAAFocus = new Table();
        tableAAFocus.check("pt".equals(LunariansSettings.language) ? "Foco coordenado" : "Focus fire", AutoAttack.enableFocusFire(), bl -> Core.settings.put("lunarians-focus-fire", (Object)bl)).left().wrap().growX();
        this.panel.add((Element)tableAAFocus).growX().left().pad(4.0f).row();
        
        Table tableAADebug = new Table();
        tableAADebug.check("pt".equals(LunariansSettings.language) ? "Debug visual (quadrados)" : "Debug visual (squares)", AutoAttack.enableDebugVisual(), bl -> Core.settings.put("lunarians-debug-visual", (Object)bl)).left().wrap().growX();
        this.panel.add((Element)tableAADebug).growX().left().pad(4.0f).row();
        
        Table tableAABtn = new Table();
        tableAABtn.button("Auto Attack Settings", new Runnable(){
            @Override
            public void run() {
                AutoAttackUi.make().show();
            }
        }).height(36.0f).pad(4.0f);
        this.panel.add((Element)tableAABtn).growX().left().pad(4.0f).row();
        
        Table table9 = new Table();
        table9.add((CharSequence)BetaHud.t("Formation", "Forma\u00e7\u00e3o")).color(UiStyle.TEXT_DIM).padRight(8.0f);
        for (int i = 0; i <= 3; ++i) {
            final int n = i;
            table9.button(Formations.modeName(i), Styles.togglet, new Runnable(){

                @Override
                public void run() {
                    Formations.setMode(n);
                }
            }).checked(textButton -> Formations.mode() == n).height(36.0f).minWidth(54.0f).pad(2.0f);
        }
        table9.button("...", new Runnable(){

            @Override
            public void run() {
                Formations.openDialog();
            }
        }).width(46.0f).height(36.0f).padLeft(4.0f);
        this.panel.add((Element)table9).growX().left().pad(4.0f).row();
        table2 = new Table();
        try {
            UiText.checkSmall(table2, "Conveyor", LunariansSettings.enableAutoConnectConveyor, new Boolc(){

                public void get(boolean bl) {
                    LunariansSettings.enableAutoConnectConveyor = bl;
                    LunariansSettings.save();
                }
            }).left();
        }
        catch (Throwable throwable) {
            table2.check("Conveyor", LunariansSettings.enableAutoConnectConveyor, new Boolc(){

                public void get(boolean bl) {
                    LunariansSettings.enableAutoConnectConveyor = bl;
                    LunariansSettings.save();
                }
            }).left();
        }
        table2.button("Set", new Runnable(){

            @Override
            public void run() {
                PositionPicker.pick(new Cons<int[]>(){

                    public void get(int[] nArray) {
                        LunariansSettings.conveyorStartX = nArray[0];
                        LunariansSettings.conveyorStartY = nArray[1];
                        LunariansSettings.save();
                        if (Vars.ui != null) {
                            Vars.ui.showInfoToast("pt".equals(LunariansSettings.language) ? "In\u00edcio: " + nArray[0] + "," + nArray[1] : "Start: " + nArray[0] + "," + nArray[1], 2.0f);
                        }
                    }
                }, new Runnable(){

                    @Override
                    public void run() {
                    }
                });
            }
        }).width(52.0f).height(40.0f).padLeft(4.0f);
        table2.button("Gen", new Runnable(){

            @Override
            public void run() {
                AutoConnectConveyor.connect();
            }
        }).width(52.0f).height(40.0f).padLeft(2.0f);
        this.panel.add((Element)table2).growX().left().pad(4.0f).row();
        this.panel.label((Prov)new Prov<CharSequence>(){

            public CharSequence get() {
                if (LunariansSettings.conveyorStartX == Integer.MIN_VALUE) {
                    return "pt".equals(LunariansSettings.language) ? "  in\u00edcio: \u2014" : "  start: \u2014";
                }
                return "  " + LunariansSettings.conveyorStartX + "," + LunariansSettings.conveyorStartY;
            }
        }).left().padLeft(10.0f).row();
        UiStyle.section(this.panel, BetaHud.t("Alerts and camera", "Alertas e c\u00e2mera"));
        this.addCheck(BetaHud.safeI18n("enemy_core_alert", "Enemy core alert"), LunariansSettings.enableEnemyCoreAlert, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableEnemyCoreAlert = bl;
                LunariansSettings.save();
            }
        });
        this.addCheck(BetaHud.safeI18n("autofill", "Auto fill"), LunariansSettings.enableAutoFill, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoFill = bl;
                LunariansSettings.save();
            }
        });
        this.panel.button(BetaHud.safeI18n("autofill_config", "Auto fill config"), new Runnable(){

            @Override
            public void run() {
                try {
                    new AutoFillConfigDialog().show();
                }
                catch (Throwable throwable) {
                    Log.err((String)"[Lunarians] AutoFillConfig", (Throwable)throwable);
                }
            }
        }).height(36.0f).growX().pad(4.0f).row();
        this.addCheck(BetaHud.safeI18n("freecam", "Free cam"), LunariansSettings.enableFreeCam, new Boolc(){

            public void get(boolean bl) {
                try {
                    FreeCam.setEnabled(bl);
                }
                catch (Throwable throwable) {
                    LunariansSettings.enableFreeCam = bl;
                    LunariansSettings.save();
                }
            }
        });
        this.addCheck(BetaHud.safeI18n("prod_alert", "Production alert"), LunariansSettings.enableProductionAlert, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableProductionAlert = bl;
                LunariansSettings.save();
                if (bl) {
                    try {
                        ProductionAlert.reset();
                    }
                    catch (Throwable throwable) {
                        // empty catch block
                    }
                }
            }
        });
        this.addCheck(BetaHud.safeI18n("camfollow", "Cam follow"), LunariansSettings.enableCamFollow, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableCamFollow = bl;
                LunariansSettings.save();
            }
        });
        this.addCheck(BetaHud.safeI18n("loss", "Resource loss"), LunariansSettings.enableLossWarn, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableLossWarn = bl;
                LunariansSettings.save();
            }
        });
        this.addCheckCount(BetaHud.safeI18n("autounit", "Auto unit"), LunariansSettings.enableAutoUnit, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoUnit = bl;
                AutoUnit.reset();
                LunariansSettings.save();
            }
        }, new Prov<CharSequence>(){

            public CharSequence get() {
                return LunariansSettings.enableAutoUnit ? "[green]\u25cf[] " + AutoUnit.count() : "";
            }
        });
        this.addCheckCount("Auto unit heal", LunariansSettings.enableAutoUnitHeal, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoUnitHeal = bl;
                AutoUnitHeal.reset();
                LunariansSettings.save();
            }
        }, new Prov<CharSequence>(){

            public CharSequence get() {
                return LunariansSettings.enableAutoUnitHeal ? "[green]\u25cf[] " + AutoUnitHeal.count() : "";
            }
        });
        this.addCheckCount("Auto Moon", LunariansSettings.enableAutoMoon, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoMoon = bl;
                AutoMoon.reset();
                LunariansSettings.save();
            }
        }, new Prov<CharSequence>(){

            public CharSequence get() {
                return LunariansSettings.enableAutoMoon ? "[green]\u25cf[] " + AutoMoon.count() : "";
            }
        });
        this.addCheckCount(BetaHud.safeI18n("autocargo", "Auto cargo"), LunariansSettings.enableAutoCargo, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoCargo = bl;
                AutoCargo.reset();
                LunariansSettings.save();
            }
        }, new Prov<CharSequence>(){

            public CharSequence get() {
                return LunariansSettings.enableAutoCargo ? "[green]\u25cf[] " + AutoCargo.count() : "";
            }
        });
        this.addCheckCount("Auto container", LunariansSettings.enableAutoContainer, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoContainer = bl;
                LunariansSettings.save();
            }
        }, new Prov<CharSequence>(){

            public CharSequence get() {
                return LunariansSettings.enableAutoContainer ? "[orange]\u25cf[]" : "";
            }
        });
        this.addCheckCount("Auto defesa", LunariansSettings.enableAutoDefence, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoDefence = bl;
                AutoDefence.reset();
                LunariansSettings.save();
            }
        }, new Prov<CharSequence>(){

            public CharSequence get() {
                return LunariansSettings.enableAutoDefence ? "[green]\u25cf[] " + AutoDefence.count() : "";
            }
        });
        this.addCheckCount("Auto shield", LunariansSettings.enableAutoShield, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoShield = bl;
                AutoShield.reset();
                LunariansSettings.save();
            }
        }, new Prov<CharSequence>(){

            public CharSequence get() {
                return LunariansSettings.enableAutoShield ? "[green]\u25cf[] " + AutoShield.count() : "";
            }
        });
        this.addCheckCount("Auto heal", LunariansSettings.enableAutoHeal, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoHeal = bl;
                AutoHeal.reset();
                LunariansSettings.save();
            }
        }, new Prov<CharSequence>(){

            public CharSequence get() {
                return LunariansSettings.enableAutoHeal ? "[green]\u25cf[] " + AutoHeal.count() : "";
            }
        });
        UiStyle.section(this.panel, BetaHud.t("Combat and interface", "Combate e interface"));
        this.addCheckCount("Auto attack", LunariansSettings.enableAutoAttack, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoAttack = bl;
                AutoAttack.reset();
                LunariansSettings.save();
            }
        }, new Prov<CharSequence>(){

            public CharSequence get() {
                return LunariansSettings.enableAutoAttack ? "[green]\u25cf[] " + AutoAttack.count() : "";
            }
        });
        this.addCheck(BetaHud.safeI18n("schem_ui", "Schematic panel"), LunariansSettings.showSchematicPanel, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.showSchematicPanel = bl;
                LunariansSettings.save();
            }
        });
        UiStyle.decorate((Element)this.panel);
    }

    private static String t(String string, String string2) {
        return "pt".equals(LunariansSettings.language) ? string2 : string;
    }

    private void addCheck(String string, boolean bl, Boolc boolc) {
        try {
            UiText.checkSmall(this.panel, string, bl, boolc).left().pad(4.0f).row();
        }
        catch (Throwable throwable) {
            this.panel.check(string, bl, boolc).left().pad(4.0f).row();
        }
    }

    private void addCheckCount(String string, boolean bl, Boolc boolc, Prov<CharSequence> prov) {
        this.addCheck(string, bl, boolc);
        try {
            this.panel.label(prov).left().padLeft(10.0f).row();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static String safeI18n(String string, String string2) {
        try {
            String string3 = LunariansI18n.get(string);
            if (string3 != null && !string3.isEmpty() && !string3.equals(string)) {
                return string3;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return string2;
    }
}

