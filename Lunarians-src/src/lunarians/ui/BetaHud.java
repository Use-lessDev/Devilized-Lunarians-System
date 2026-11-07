package lunarians.ui;

import arc.Core;
import arc.func.Boolc;
import arc.func.Boolf;
import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.Action;
import arc.scene.actions.Actions;
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
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/BetaHud.class */
public class BetaHud extends Table {
    private final Table panel;
    private final ImageButton toggleBtn;
    private boolean open = false;
    private Cell<ScrollPane> scrollCell;
    private final ScrollPane scroll;

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.BetaHud$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Boolf {
        public final /* synthetic */ int f$0;

        public final boolean get(Object obj) {
            return BetaHud.lambda$rebuildPanel$0(this.f$0, (TextButton) obj);
        }
    }

    public BetaHud() {
        setFillParent(true);
        right().top();
        defaults().right();
        this.panel = new Table(UiStyle.panelBg());
        this.panel.visible = false;
        this.panel.defaults().growX().pad(4.0f);
        this.toggleBtn = new ImageButton(Icon.rightOpen, Styles.clearNonei);
        this.toggleBtn.clicked(new Runnable() { // from class: lunarians.ui.BetaHud.1
            @Override // java.lang.Runnable
            public void run() {
                BetaHud.this.toggle();
            }
        });
        try {
            this.toggleBtn.resizeImage(28.0f);
        } catch (Throwable th) {
        }
        this.scroll = new ScrollPane(this.panel, Styles.smallPane);
        this.scroll.setScrollingDisabled(true, false);
        this.scroll.setFadeScrollBars(false);
        this.scroll.visible = false;
        Table table = new Table();
        this.scrollCell = table.add(this.scroll).top();
        applySize();
        table.add(this.toggleBtn).size(40.0f, 40.0f).top().pad(6.0f);
        add(table).top().right().padTop(120.0f);
        try {
            rebuildPanel();
        } catch (Throwable th2) {
            this.panel.clear();
            this.panel.add("Beta HUD error").color(Color.scarlet).pad(6.0f).row();
            Log.err("[Lunarians] BetaHud rebuild failed", th2);
        }
    }

    public void toggle() {
        this.open = !this.open;
        if (this.open) {
            try {
                rebuildPanel();
            } catch (Throwable th) {
                Log.err("[Lunarians] BetaHud rebuild failed", th);
            }
            this.panel.visible = true;
            this.scroll.visible = true;
            this.panel.actions(new Action[]{Actions.sequence(Actions.alpha(0.0f), Actions.fadeIn(0.2f))});
            try {
                this.toggleBtn.getStyle().imageUp = Icon.leftOpen;
                return;
            } catch (Throwable th2) {
                return;
            }
        }
        this.panel.actions(new Action[]{Actions.sequence(Actions.fadeOut(0.15f), Actions.run(new Runnable() { // from class: lunarians.ui.BetaHud.2
            @Override // java.lang.Runnable
            public void run() {
                BetaHud.this.panel.visible = false;
                BetaHud.this.scroll.visible = false;
            }
        }))});
        try {
            this.toggleBtn.getStyle().imageUp = Icon.rightOpen;
        } catch (Throwable th3) {
        }
    }

    public void applySize() {
        try {
            this.scrollCell.width(LunariansSettings.betaWidth).maxHeight((Core.graphics.getHeight() * LunariansSettings.betaHeightPct) / 100.0f);
            invalidateHierarchy();
        } catch (Throwable th) {
        }
    }

    public void refresh() {
        applySize();
        if (this.open) {
            try {
                rebuildPanel();
            } catch (Throwable th) {
                Log.err("[Lunarians] BetaHud refresh failed", th);
            }
        }
    }

    private void rebuildPanel() {
        this.panel.clear();
        UiStyle.banner(this.panel, "LUNARIANS", safeI18n("beta_features", "Beta features"));
        UiStyle.section(this.panel, t("Mode", "Modo"));
        TextButton textButton = this.panel.button(LunariansSettings.pvpMode ? "Balanced ON" : "Normal", new Runnable() { // from class: lunarians.ui.BetaHud.3
            @Override // java.lang.Runnable
            public void run() {
                LunariansSettings.pvpMode = !LunariansSettings.pvpMode;
                LunariansSettings.save();
                BetaHud.this.rebuildPanel();
            }
        }).growX().height(42.0f).pad(4.0f).get();
        textButton.setColor(LunariansSettings.pvpMode ? Color.scarlet : Pal.accent);
        try {
            textButton.getLabel().setWrap(true);
        } catch (Throwable th) {
        }
        this.panel.row();
        if (LunariansSettings.pvpMode) {
            Table table = new Table();
            table.add("Delay:").left().padRight(4.0f);
            table.button("-", new Runnable() { // from class: lunarians.ui.BetaHud.4
                @Override // java.lang.Runnable
                public void run() {
                    LunariansSettings.pvpDelay = Math.max(1.0f, LunariansSettings.pvpDelay - 0.5f);
                    LunariansSettings.save();
                    BetaHud.this.rebuildPanel();
                }
            }).size(36.0f, 32.0f);
            table.add(String.format("%.1fs", Float.valueOf(LunariansSettings.pvpDelay))).width(52.0f).center().pad(2.0f);
            table.button("+", new Runnable() { // from class: lunarians.ui.BetaHud.5
                @Override // java.lang.Runnable
                public void run() {
                    LunariansSettings.pvpDelay = Math.min(15.0f, LunariansSettings.pvpDelay + 0.5f);
                    LunariansSettings.save();
                    BetaHud.this.rebuildPanel();
                }
            }).size(36.0f, 32.0f);
            this.panel.add(table).left().pad(4.0f).row();
        }
        UiStyle.section(this.panel, t("Configuration", "Configuração"));
        this.panel.button(safeI18n("settings_btn", "Settings"), new Runnable() { // from class: lunarians.ui.BetaHud.6
            @Override // java.lang.Runnable
            public void run() {
                try {
                    new ConfigDialog().show();
                } catch (Throwable th2) {
                    Log.err("[Lunarians] ConfigDialog", th2);
                    try {
                        AutoDrillUi.make().show();
                    } catch (Throwable th3) {
                        if (Vars.ui != null) {
                            Vars.ui.showInfoToast("Settings error", 3.0f);
                        }
                    }
                }
            }
        }).height(40.0f).growX().pad(4.0f).get();
        this.panel.row();
        UiStyle.section(this.panel, t("Auto Set and economy", "Auto Set e economia"));
        Table table2 = new Table();
        table2.add("Auto Set").left().padRight(6.0f);
        table2.button("GO", new Runnable() { // from class: lunarians.ui.BetaHud.7
            @Override // java.lang.Runnable
            public void run() {
                AutoSet.request();
            }
        }).size(52.0f, 40.0f).pad(2.0f);
        table2.button("X", new Runnable() { // from class: lunarians.ui.BetaHud.8
            @Override // java.lang.Runnable
            public void run() {
                AutoSet.cancel();
            }
        }).size(40.0f, 40.0f).pad(2.0f);
        try {
            table2.label(new Prov<CharSequence>() { // from class: lunarians.ui.BetaHud.9
                /* renamed from: get, reason: merged with bridge method [inline-methods] */
                public CharSequence m71get() {
                    if (AutoSet.isRunning()) {
                        return "[orange]RUN[]";
                    }
                    if (AutoSet.isActive()) {
                        return "[yellow]…[]";
                    }
                    return "";
                }
            }).padLeft(4.0f);
        } catch (Throwable th2) {
        }
        this.panel.add(table2).growX().left().pad(4.0f).row();
        Table table3 = new Table();
        try {
            UiText.checkSmall(table3, "Drill", LunariansSettings.enableAutoDrill, new Boolc() { // from class: lunarians.ui.BetaHud.10
                public void get(boolean z) {
                    LunariansSettings.enableAutoDrill = z;
                    LunariansSettings.save();
                }
            }).left();
        } catch (Throwable th3) {
            table3.check("Drill", LunariansSettings.enableAutoDrill, new Boolc() { // from class: lunarians.ui.BetaHud.11
                public void get(boolean z) {
                    LunariansSettings.enableAutoDrill = z;
                    LunariansSettings.save();
                }
            }).left();
        }
        table3.button("Set", new Runnable() { // from class: lunarians.ui.BetaHud.12
            @Override // java.lang.Runnable
            public void run() {
                PositionPicker.pick(new Cons<int[]>() { // from class: lunarians.ui.BetaHud.12.1
                    public void get(int[] iArr) {
                        LunariansSettings.drillStartX = iArr[0];
                        LunariansSettings.drillStartY = iArr[1];
                        LunariansSettings.save();
                        if (Vars.ui != null) {
                            Vars.ui.showInfoToast("Ore: " + iArr[0] + "," + iArr[1], 2.0f);
                        }
                    }
                }, new Runnable() { // from class: lunarians.ui.BetaHud.12.2
                    @Override // java.lang.Runnable
                    public void run() {
                    }
                });
            }
        }).width(52.0f).height(40.0f).padLeft(4.0f);
        table3.button("Gen", new Runnable() { // from class: lunarians.ui.BetaHud.13
            @Override // java.lang.Runnable
            public void run() {
                AutoDrill.generate();
            }
        }).width(52.0f).height(40.0f).padLeft(2.0f);
        this.panel.add(table3).growX().left().pad(4.0f).row();
        Table table4 = new Table();
        table4.add("Exit").padRight(6.0f);
        String[] strArr = {">", "^", "<", "v"};
        for (int i = 0; i < 4; i++) {
            final int i2 = i;
            TextButton textButton2 = table4.button(strArr[i], new Runnable() { // from class: lunarians.ui.BetaHud.14
                @Override // java.lang.Runnable
                public void run() {
                    LunariansSettings.drillExitDir = i2;
                    LunariansSettings.save();
                    BetaHud.this.rebuildPanel();
                }
            }).size(44.0f, 40.0f).pad(2.0f).get();
            try {
                if (LunariansSettings.drillExitDir == i2) {
                    textButton2.setColor(Pal.accent);
                } else {
                    textButton2.setColor(Color.lightGray);
                }
            } catch (Throwable th4) {
            }
        }
        table4.button(LunariansSettings.drillLayoutMode == 1 ? "Full" : "Str", new Runnable() { // from class: lunarians.ui.BetaHud.15
            @Override // java.lang.Runnable
            public void run() {
                LunariansSettings.drillLayoutMode = LunariansSettings.drillLayoutMode == 1 ? 0 : 1;
                LunariansSettings.save();
                BetaHud.this.rebuildPanel();
            }
        }).size(56.0f, 40.0f).padLeft(6.0f);
        this.panel.add(table4).growX().left().pad(4.0f).row();
        Table table5 = new Table();
        try {
            UiText.checkSmall(table5, "D+C", LunariansSettings.enableAutoDrillConveyor, new Boolc() { // from class: lunarians.ui.BetaHud.16
                public void get(boolean z) {
                    LunariansSettings.enableAutoDrillConveyor = z;
                    LunariansSettings.save();
                }
            }).left();
        } catch (Throwable th5) {
            table5.check("D+C", LunariansSettings.enableAutoDrillConveyor, new Boolc() { // from class: lunarians.ui.BetaHud.17
                public void get(boolean z) {
                    LunariansSettings.enableAutoDrillConveyor = z;
                    LunariansSettings.save();
                }
            }).left();
        }
        table5.button("Gen", new Runnable() { // from class: lunarians.ui.BetaHud.18
            @Override // java.lang.Runnable
            public void run() {
                AutoDrill.generateWithConveyor();
            }
        }).width(52.0f).height(40.0f).padLeft(4.0f);
        this.panel.add(table5).growX().left().pad(4.0f).row();
        Table table6 = new Table();
        table6.check("pt".equals(LunariansSettings.language) ? "Drills perfeitos (Beta)" : "Perfect drills (Beta)", LunariansSettings.drillPerfect, new Boolc() { // from class: lunarians.ui.BetaHud.19
            public void get(boolean z) {
                LunariansSettings.drillPerfect = z;
                LunariansSettings.save();
            }
        }).left();
        this.panel.add(table6).growX().left().pad(4.0f).row();
        Table table7 = new Table();
        table7.check("pt".equals(LunariansSettings.language) ? "Auto Dock + Energia (Beta)" : "Auto Dock + Energy (Beta)", LunariansSettings.dockAuto, new Boolc() { // from class: lunarians.ui.BetaHud.20
            public void get(boolean z) {
                LunariansSettings.dockAuto = z;
                LunariansSettings.save();
                if (z) {
                    AutoDock.reset();
                    Vars.ui.showInfoToast("Core: " + AutoDock.coreInfo(), 5.0f);
                }
            }
        }).left();
        this.panel.add(table7).growX().left().pad(4.0f).row();
        Table table8 = new Table();
        table8.check("pt".equals(LunariansSettings.language) ? "Economia em ordem (Beta)" : "Ordered economy (Beta)", AutoEconomy.enabled(), new Boolc() { // from class: lunarians.ui.BetaHud.21
            public void get(boolean z) {
                AutoEconomy.setEnabled(z);
            }
        }).left();
        this.panel.add(table8).growX().left().pad(4.0f).row();
        UiStyle.section(this.panel, t("Team and chat", "Equipe e chat"));
        Table table9 = new Table();
        table9.check("pt".equals(LunariansSettings.language) ? "Presença Lunarians (aura roxa)" : "Lunarians presence (purple aura)", LunariansPresence.enabled(), new Boolc() { // from class: lunarians.ui.BetaHud.22
            public void get(boolean z) {
                LunariansPresence.setEnabled(z);
                Vars.ui.showInfoToast(z ? "Presence ON. Reconecte ao servidor para os outros te verem." : "Presence OFF. Reconecte para remover a marca do nome.", 2.0f);
            }
        }).left();
        this.panel.add(table9).growX().left().pad(4.0f).row();
        Table table10 = new Table();
        table10.check("pt".equals(LunariansSettings.language) ? "Comandos por chat" : "Chat commands", ChatCommands.on(), new Boolc() { // from class: lunarians.ui.BetaHud.23
            public void get(boolean z) {
                Core.settings.put(ChatCommands.KEY_ON, Boolean.valueOf(z));
            }
        }).left();
        table10.button("...", new Runnable() { // from class: lunarians.ui.BetaHud.24
            @Override // java.lang.Runnable
            public void run() {
                ChatCommands.openPanel();
            }
        }).width(52.0f).height(40.0f).padLeft(4.0f);
        this.panel.add(table10).growX().left().pad(4.0f).row();
        UiStyle.section(this.panel, t("Units", "Unidades"));
        Table table11 = new Table();
        table11.check(MinerUnits.title() + " (Beta)", MinerUnits.enabled(), new Boolc() { // from class: lunarians.ui.BetaHud.25
            public void get(boolean z) {
                MinerUnits.setEnabled(z);
                LunariansSettings.save();
            }
        }).left();
        table11.button("...", new Runnable() { // from class: lunarians.ui.BetaHud.26
            @Override // java.lang.Runnable
            public void run() {
                MinerUnits.dialog().show();
            }
        }).width(52.0f).height(40.0f).padLeft(4.0f);
        this.panel.add(table11).growX().left().pad(4.0f).row();
        Table table12 = new Table();
        table12.add(t("Formation", "Formação")).color(UiStyle.TEXT_DIM).padRight(8.0f);
        for (int i3 = 0; i3 <= 3; i3++) {
            final int i4 = i3;
            table12.button(Formations.modeName(i3), Styles.togglet, new Runnable() { // from class: lunarians.ui.BetaHud.27
                @Override // java.lang.Runnable
                public void run() {
                    Formations.setMode(i4);
                }
            }).checked(textButton3 -> {
                return Formations.mode() == i4;
            }).height(36.0f).minWidth(54.0f).pad(2.0f);
        }
        table12.button("...", new Runnable() { // from class: lunarians.ui.BetaHud.28
            @Override // java.lang.Runnable
            public void run() {
                Formations.openDialog();
            }
        }).width(46.0f).height(36.0f).padLeft(4.0f);
        this.panel.add(table12).growX().left().pad(4.0f).row();
        Table table13 = new Table();
        try {
            UiText.checkSmall(table13, "Conveyor", LunariansSettings.enableAutoConnectConveyor, new Boolc() { // from class: lunarians.ui.BetaHud.29
                public void get(boolean z) {
                    LunariansSettings.enableAutoConnectConveyor = z;
                    LunariansSettings.save();
                }
            }).left();
        } catch (Throwable th6) {
            table13.check("Conveyor", LunariansSettings.enableAutoConnectConveyor, new Boolc() { // from class: lunarians.ui.BetaHud.30
                public void get(boolean z) {
                    LunariansSettings.enableAutoConnectConveyor = z;
                    LunariansSettings.save();
                }
            }).left();
        }
        table13.button("Set", new Runnable() { // from class: lunarians.ui.BetaHud.31
            @Override // java.lang.Runnable
            public void run() {
                PositionPicker.pick(new Cons<int[]>() { // from class: lunarians.ui.BetaHud.31.1
                    public void get(int[] iArr) {
                        LunariansSettings.conveyorStartX = iArr[0];
                        LunariansSettings.conveyorStartY = iArr[1];
                        LunariansSettings.save();
                        if (Vars.ui != null) {
                            Vars.ui.showInfoToast("pt".equals(LunariansSettings.language) ? "Início: " + iArr[0] + "," + iArr[1] : "Start: " + iArr[0] + "," + iArr[1], 2.0f);
                        }
                    }
                }, new Runnable() { // from class: lunarians.ui.BetaHud.31.2
                    @Override // java.lang.Runnable
                    public void run() {
                    }
                });
            }
        }).width(52.0f).height(40.0f).padLeft(4.0f);
        table13.button("Gen", new Runnable() { // from class: lunarians.ui.BetaHud.32
            @Override // java.lang.Runnable
            public void run() {
                AutoConnectConveyor.connect();
            }
        }).width(52.0f).height(40.0f).padLeft(2.0f);
        this.panel.add(table13).growX().left().pad(4.0f).row();
        this.panel.label(new Prov<CharSequence>() { // from class: lunarians.ui.BetaHud.33
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m61get() {
                if (LunariansSettings.conveyorStartX == Integer.MIN_VALUE) {
                    return "pt".equals(LunariansSettings.language) ? "  início: —" : "  start: —";
                }
                return "  " + LunariansSettings.conveyorStartX + "," + LunariansSettings.conveyorStartY;
            }
        }).left().padLeft(10.0f).row();
        UiStyle.section(this.panel, t("Alerts and camera", "Alertas e câmera"));
        addCheck(safeI18n("enemy_core_alert", "Enemy core alert"), LunariansSettings.enableEnemyCoreAlert, new Boolc() { // from class: lunarians.ui.BetaHud.34
            public void get(boolean z) {
                LunariansSettings.enableEnemyCoreAlert = z;
                LunariansSettings.save();
            }
        });
        addCheck(safeI18n("autofill", "Auto fill"), LunariansSettings.enableAutoFill, new Boolc() { // from class: lunarians.ui.BetaHud.35
            public void get(boolean z) {
                LunariansSettings.enableAutoFill = z;
                LunariansSettings.save();
            }
        });
        this.panel.button(safeI18n("autofill_config", "Auto fill config"), new Runnable() { // from class: lunarians.ui.BetaHud.36
            @Override // java.lang.Runnable
            public void run() {
                try {
                    new AutoFillConfigDialog().show();
                } catch (Throwable th7) {
                    Log.err("[Lunarians] AutoFillConfig", th7);
                }
            }
        }).height(36.0f).growX().pad(4.0f).row();
        addCheck(safeI18n("freecam", "Free cam"), LunariansSettings.enableFreeCam, new Boolc() { // from class: lunarians.ui.BetaHud.37
            public void get(boolean z) {
                try {
                    FreeCam.setEnabled(z);
                } catch (Throwable th7) {
                    LunariansSettings.enableFreeCam = z;
                    LunariansSettings.save();
                }
            }
        });
        addCheck(safeI18n("prod_alert", "Production alert"), LunariansSettings.enableProductionAlert, new Boolc() { // from class: lunarians.ui.BetaHud.38
            public void get(boolean z) {
                LunariansSettings.enableProductionAlert = z;
                LunariansSettings.save();
                if (z) {
                    try {
                        ProductionAlert.reset();
                    } catch (Throwable th7) {
                    }
                }
            }
        });
        addCheck(safeI18n("camfollow", "Cam follow"), LunariansSettings.enableCamFollow, new Boolc() { // from class: lunarians.ui.BetaHud.39
            public void get(boolean z) {
                LunariansSettings.enableCamFollow = z;
                LunariansSettings.save();
            }
        });
        addCheck(safeI18n("loss", "Resource loss"), LunariansSettings.enableLossWarn, new Boolc() { // from class: lunarians.ui.BetaHud.40
            public void get(boolean z) {
                LunariansSettings.enableLossWarn = z;
                LunariansSettings.save();
            }
        });
        addCheckCount(safeI18n("autounit", "Auto unit"), LunariansSettings.enableAutoUnit, new Boolc() { // from class: lunarians.ui.BetaHud.41
            public void get(boolean z) {
                LunariansSettings.enableAutoUnit = z;
                AutoUnit.reset();
                LunariansSettings.save();
            }
        }, new Prov<CharSequence>() { // from class: lunarians.ui.BetaHud.42
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m62get() {
                return LunariansSettings.enableAutoUnit ? "[green]●[] " + AutoUnit.count() : "";
            }
        });
        addCheckCount("Auto unit heal", LunariansSettings.enableAutoUnitHeal, new Boolc() { // from class: lunarians.ui.BetaHud.43
            public void get(boolean z) {
                LunariansSettings.enableAutoUnitHeal = z;
                AutoUnitHeal.reset();
                LunariansSettings.save();
            }
        }, new Prov<CharSequence>() { // from class: lunarians.ui.BetaHud.44
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m63get() {
                return LunariansSettings.enableAutoUnitHeal ? "[green]●[] " + AutoUnitHeal.count() : "";
            }
        });
        addCheckCount("Auto Moon", LunariansSettings.enableAutoMoon, new Boolc() { // from class: lunarians.ui.BetaHud.45
            public void get(boolean z) {
                LunariansSettings.enableAutoMoon = z;
                AutoMoon.reset();
                LunariansSettings.save();
            }
        }, new Prov<CharSequence>() { // from class: lunarians.ui.BetaHud.46
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m64get() {
                return LunariansSettings.enableAutoMoon ? "[green]●[] " + AutoMoon.count() : "";
            }
        });
        addCheckCount(safeI18n("autocargo", "Auto cargo"), LunariansSettings.enableAutoCargo, new Boolc() { // from class: lunarians.ui.BetaHud.47
            public void get(boolean z) {
                LunariansSettings.enableAutoCargo = z;
                AutoCargo.reset();
                LunariansSettings.save();
            }
        }, new Prov<CharSequence>() { // from class: lunarians.ui.BetaHud.48
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m65get() {
                return LunariansSettings.enableAutoCargo ? "[green]●[] " + AutoCargo.count() : "";
            }
        });
        addCheckCount("Auto container", LunariansSettings.enableAutoContainer, new Boolc() { // from class: lunarians.ui.BetaHud.49
            public void get(boolean z) {
                LunariansSettings.enableAutoContainer = z;
                LunariansSettings.save();
            }
        }, new Prov<CharSequence>() { // from class: lunarians.ui.BetaHud.50
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m66get() {
                return LunariansSettings.enableAutoContainer ? "[orange]●[]" : "";
            }
        });
        addCheckCount("Auto defesa", LunariansSettings.enableAutoDefence, new Boolc() { // from class: lunarians.ui.BetaHud.51
            public void get(boolean z) {
                LunariansSettings.enableAutoDefence = z;
                AutoDefence.reset();
                LunariansSettings.save();
            }
        }, new Prov<CharSequence>() { // from class: lunarians.ui.BetaHud.52
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m67get() {
                return LunariansSettings.enableAutoDefence ? "[green]●[] " + AutoDefence.count() : "";
            }
        });
        addCheckCount("Auto shield", LunariansSettings.enableAutoShield, new Boolc() { // from class: lunarians.ui.BetaHud.53
            public void get(boolean z) {
                LunariansSettings.enableAutoShield = z;
                AutoShield.reset();
                LunariansSettings.save();
            }
        }, new Prov<CharSequence>() { // from class: lunarians.ui.BetaHud.54
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m68get() {
                return LunariansSettings.enableAutoShield ? "[green]●[] " + AutoShield.count() : "";
            }
        });
        addCheckCount("Auto heal", LunariansSettings.enableAutoHeal, new Boolc() { // from class: lunarians.ui.BetaHud.55
            public void get(boolean z) {
                LunariansSettings.enableAutoHeal = z;
                AutoHeal.reset();
                LunariansSettings.save();
            }
        }, new Prov<CharSequence>() { // from class: lunarians.ui.BetaHud.56
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m69get() {
                return LunariansSettings.enableAutoHeal ? "[green]●[] " + AutoHeal.count() : "";
            }
        });
        UiStyle.section(this.panel, t("Combat and interface", "Combate e interface"));
        addCheckCount("Auto attack", LunariansSettings.enableAutoAttack, new Boolc() { // from class: lunarians.ui.BetaHud.57
            public void get(boolean z) {
                LunariansSettings.enableAutoAttack = z;
                AutoAttack.reset();
                LunariansSettings.save();
            }
        }, new Prov<CharSequence>() { // from class: lunarians.ui.BetaHud.58
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m70get() {
                return LunariansSettings.enableAutoAttack ? "[green]●[] " + AutoAttack.count() : "";
            }
        });
        addCheck(safeI18n("schem_ui", "Schematic panel"), LunariansSettings.showSchematicPanel, new Boolc() { // from class: lunarians.ui.BetaHud.59
            public void get(boolean z) {
                LunariansSettings.showSchematicPanel = z;
                LunariansSettings.save();
            }
        });
        UiStyle.decorate(this.panel);
    }

    private static String t(String str, String str2) {
        return "pt".equals(LunariansSettings.language) ? str2 : str;
    }

    private void addCheck(String str, boolean z, Boolc boolc) {
        try {
            UiText.checkSmall(this.panel, str, z, boolc).left().pad(4.0f).row();
        } catch (Throwable th) {
            this.panel.check(str, z, boolc).left().pad(4.0f).row();
        }
    }

    private void addCheckCount(String str, boolean z, Boolc boolc, Prov<CharSequence> prov) {
        addCheck(str, z, boolc);
        try {
            this.panel.label(prov).left().padLeft(10.0f).row();
        } catch (Throwable th) {
        }
    }

    private static String safeI18n(String str, String str2) {
        try {
            String str3 = LunariansI18n.get(str);
            if (str3 != null && !str3.isEmpty()) {
                if (!str3.equals(str)) {
                    return str3;
                }
            }
        } catch (Throwable th) {
        }
        return str2;
    }
}
