package lunarians.ui;

import arc.Core;
import arc.func.Boolc;
import arc.func.Cons;
import arc.func.Floatc;
import arc.func.Prov;
import arc.scene.ui.layout.Table;
import lunarians.AutoAttack;
import lunarians.AutoCargo;
import lunarians.AutoContainer;
import lunarians.AutoDefence;
import lunarians.AutoHeal;
import lunarians.AutoMoon;
import lunarians.AutoSetConfig;
import lunarians.AutoShield;
import lunarians.AutoUnit;
import lunarians.AutoUnitHeal;
import lunarians.CamFollow;
import lunarians.FreeCam;
import lunarians.LunariansI18n;
import lunarians.LunariansMod;
import lunarians.LunariansSettings;
import lunarians.ProductionAlert;
import lunarians.ResourceLossWarn;
import lunarians.SchematicPlacer;
import lunarians.ui.SimpleFeatureDialogs;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/ConfigDialog.class */
public class ConfigDialog extends BaseDialog {
    private int cols;
    private int col;

    /* JADX WARN: Classes with same name are omitted:
      classes.dex
     */
    /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/ConfigDialog$Maker.class */
    private interface Maker {
        BuilderDialog make();
    }

    public ConfigDialog() {
        super(LunariansI18n.get("config_title"));
        this.cols = Core.graphics.getWidth() >= 640 ? 2 : 1;
        this.col = 0;
        this.cont.pane(new Cons<Table>() { // from class: lunarians.ui.ConfigDialog.1
            public void get(Table table) {
                table.defaults().growX().padBottom(4.0f);
                ConfigDialog.this.build(table);
            }
        }).grow().maxHeight(Core.graphics.getHeight() * 0.78f).width(Math.min(Core.graphics.getWidth() * 0.96f, 900.0f));
        this.buttons.defaults().size(150.0f, 48.0f).pad(4.0f);
        this.buttons.button(LunariansI18n.get("reset_position"), new Runnable() { // from class: lunarians.ui.ConfigDialog.2
            @Override // java.lang.Runnable
            public void run() {
                ConfigDialog.resetPosition();
            }
        });
        this.buttons.button(LunariansI18n.get("reset_all"), new Runnable() { // from class: lunarians.ui.ConfigDialog.3
            @Override // java.lang.Runnable
            public void run() {
                ConfigDialog.this.confirmResetAll();
            }
        });
        this.buttons.button(LunariansI18n.get("back"), new Runnable() { // from class: lunarians.ui.ConfigDialog.4
            @Override // java.lang.Runnable
            public void run() {
                ConfigDialog.this.hide();
            }
        });
        closeOnBack();
        hidden(new Runnable() { // from class: lunarians.ui.ConfigDialog.5
            @Override // java.lang.Runnable
            public void run() {
                LunariansSettings.save();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void build(Table table) {
        section(table, LunariansI18n.get("cfg_general"));
        menu(table, LunariansI18n.get("cfg_panel"), new Maker() { // from class: lunarians.ui.ConfigDialog.6
            @Override // lunarians.ui.ConfigDialog.Maker
            public BuilderDialog make() {
                return ConfigDialog.dlg(LunariansI18n.get("cfg_panel"), new Cons<Table>() { // from class: lunarians.ui.ConfigDialog.6.1
                    public void get(Table table2) {
                        SchematicSettingsUi.build(table2);
                    }
                }, new Runnable() { // from class: lunarians.ui.ConfigDialog.6.2
                    @Override // java.lang.Runnable
                    public void run() {
                        SchematicSettingsUi.onClosed();
                    }
                });
            }
        });
        menu(table, LunariansI18n.get("cfg_categories"), new Maker() { // from class: lunarians.ui.ConfigDialog.7
            @Override // lunarians.ui.ConfigDialog.Maker
            public BuilderDialog make() {
                return ConfigDialog.dlg(LunariansI18n.get("cfg_categories"), new Cons<Table>() { // from class: lunarians.ui.ConfigDialog.7.1
                    public void get(Table table2) {
                        SchematicSettingsUi.buildCategories(table2);
                    }
                }, new Runnable() { // from class: lunarians.ui.ConfigDialog.7.2
                    @Override // java.lang.Runnable
                    public void run() {
                        SchematicSettingsUi.onClosed();
                    }
                });
            }
        });
        menu(table, "Auto Set", new Maker() { // from class: lunarians.ui.ConfigDialog.8
            @Override // lunarians.ui.ConfigDialog.Maker
            public BuilderDialog make() {
                return ConfigDialog.dlg("Auto Set", new Cons<Table>() { // from class: lunarians.ui.ConfigDialog.8.1
                    public void get(Table table2) {
                        AutoSetConfig.build(table2);
                    }
                }, new Runnable() { // from class: lunarians.ui.ConfigDialog.8.2
                    @Override // java.lang.Runnable
                    public void run() {
                        LunariansSettings.save();
                    }
                });
            }
        });
        menu(table, LunariansI18n.get("cfg_beta_panel"), new Maker() { // from class: lunarians.ui.ConfigDialog.9
            @Override // lunarians.ui.ConfigDialog.Maker
            public BuilderDialog make() {
                return ConfigDialog.dlg(LunariansI18n.get("cfg_beta_panel"), new Cons<Table>() { // from class: lunarians.ui.ConfigDialog.9.1
                    public void get(Table table2) {
                        ConfigDialog.betaSize(table2);
                    }
                }, new Runnable() { // from class: lunarians.ui.ConfigDialog.9.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (LunariansMod.betaHud != null) {
                            LunariansMod.betaHud.refresh();
                        }
                    }
                });
            }
        });
        menu(table, LunariansI18n.get("language"), new Maker() { // from class: lunarians.ui.ConfigDialog.10
            @Override // lunarians.ui.ConfigDialog.Maker
            public BuilderDialog make() {
                return ConfigDialog.dlg(LunariansI18n.get("language"), new Cons<Table>() { // from class: lunarians.ui.ConfigDialog.10.1
                    public void get(final Table table2) {
                        SimpleFeatureDialogs.Language.build(table2, new Runnable() { // from class: lunarians.ui.ConfigDialog.10.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                table2.clear();
                                SimpleFeatureDialogs.Language.build(table2, this);
                            }
                        });
                    }
                }, null);
            }
        });
        section(table, LunariansI18n.get("extras"));
        Table table2 = new Table();
        table2.add("Modo:").left().padRight(8.0f);
        table2.button(LunariansSettings.pvpMode ? "Balanced ON" : "Normal", new Runnable() { // from class: lunarians.ui.ConfigDialog.11
            @Override // java.lang.Runnable
            public void run() {
                LunariansSettings.pvpMode = !LunariansSettings.pvpMode;
                LunariansSettings.save();
                ConfigDialog.this.hide();
                new ConfigDialog().show();
            }
        }).width(120.0f).height(40.0f);
        table.add(table2).growX().pad(4.0f);
        next(table);
        if (LunariansSettings.pvpMode) {
            Table table3 = new Table();
            table3.add("Balanced delay:").left().padRight(6.0f);
            table3.button("-", new Runnable() { // from class: lunarians.ui.ConfigDialog.12
                @Override // java.lang.Runnable
                public void run() {
                    LunariansSettings.pvpDelay = Math.max(1.0f, LunariansSettings.pvpDelay - 0.5f);
                    LunariansSettings.save();
                    ConfigDialog.this.hide();
                    new ConfigDialog().show();
                }
            }).size(40.0f, 36.0f);
            table3.add(String.format("%.1fs", Float.valueOf(LunariansSettings.pvpDelay))).width(56.0f).center().pad(4.0f);
            table3.button("+", new Runnable() { // from class: lunarians.ui.ConfigDialog.13
                @Override // java.lang.Runnable
                public void run() {
                    LunariansSettings.pvpDelay = Math.min(15.0f, LunariansSettings.pvpDelay + 0.5f);
                    LunariansSettings.save();
                    ConfigDialog.this.hide();
                    new ConfigDialog().show();
                }
            }).size(40.0f, 36.0f);
            table.add(table3).left().pad(4.0f);
            next(table);
        }
        feature(table, "Auto drill", LunariansSettings.enableAutoDrill, new Boolc() { // from class: lunarians.ui.ConfigDialog.14
            public void get(boolean z) {
                LunariansSettings.enableAutoDrill = z;
            }
        }, new Maker() { // from class: lunarians.ui.ConfigDialog.15
            @Override // lunarians.ui.ConfigDialog.Maker
            public BuilderDialog make() {
                return AutoDrillUi.make();
            }
        });
        feature(table, "Auto connect conveyor", LunariansSettings.enableAutoConnectConveyor, new Boolc() { // from class: lunarians.ui.ConfigDialog.16
            public void get(boolean z) {
                LunariansSettings.enableAutoConnectConveyor = z;
            }
        }, new Maker() { // from class: lunarians.ui.ConfigDialog.17
            @Override // lunarians.ui.ConfigDialog.Maker
            public BuilderDialog make() {
                return AutoConnectConveyorUi.make();
            }
        });
        feature(table, "Auto Moon", LunariansSettings.enableAutoMoon, new Boolc() { // from class: lunarians.ui.ConfigDialog.18
            public void get(boolean z) {
                LunariansSettings.enableAutoMoon = z;
                AutoMoon.reset();
            }
        }, new Maker() { // from class: lunarians.ui.ConfigDialog.19
            @Override // lunarians.ui.ConfigDialog.Maker
            public BuilderDialog make() {
                return AutoMoonUi.make();
            }
        });
        feature(table, LunariansI18n.get("enemy_core_alert"), LunariansSettings.enableEnemyCoreAlert, new Boolc() { // from class: lunarians.ui.ConfigDialog.20
            public void get(boolean z) {
                LunariansSettings.enableEnemyCoreAlert = z;
            }
        }, new Maker() { // from class: lunarians.ui.ConfigDialog.21
            @Override // lunarians.ui.ConfigDialog.Maker
            public BuilderDialog make() {
                return ConfigDialog.dlg(LunariansI18n.get("enemy_core_alert_title"), new Cons<Table>() { // from class: lunarians.ui.ConfigDialog.21.1
                    public void get(Table table4) {
                        SimpleFeatureDialogs.EnemyAlert.build(table4);
                    }
                }, null);
            }
        });
        feature(table, LunariansI18n.get("autofill"), LunariansSettings.enableAutoFill, new Boolc() { // from class: lunarians.ui.ConfigDialog.22
            public void get(boolean z) {
                LunariansSettings.enableAutoFill = z;
            }
        }, null);
        autofillGear(table);
        feature(table, LunariansI18n.get("freecam"), LunariansSettings.enableFreeCam, new Boolc() { // from class: lunarians.ui.ConfigDialog.23
            public void get(boolean z) {
                FreeCam.setEnabled(z);
            }
        }, null);
        feature(table, LunariansI18n.get("prod_alert"), LunariansSettings.enableProductionAlert, new Boolc() { // from class: lunarians.ui.ConfigDialog.24
            public void get(boolean z) {
                LunariansSettings.enableProductionAlert = z;
                if (z) {
                    ProductionAlert.reset();
                }
            }
        }, new Maker() { // from class: lunarians.ui.ConfigDialog.25
            @Override // lunarians.ui.ConfigDialog.Maker
            public BuilderDialog make() {
                return ConfigDialog.dlg(LunariansI18n.get("prod_alert"), new Cons<Table>() { // from class: lunarians.ui.ConfigDialog.25.1
                    public void get(Table table4) {
                        SimpleFeatureDialogs.ProdAlert.build(table4);
                    }
                }, null);
            }
        });
        feature(table, LunariansI18n.get("camfollow"), LunariansSettings.enableCamFollow, new Boolc() { // from class: lunarians.ui.ConfigDialog.26
            public void get(boolean z) {
                CamFollow.setEnabled(z);
            }
        }, null);
        feature(table, LunariansI18n.get("loss"), LunariansSettings.enableLossWarn, new Boolc() { // from class: lunarians.ui.ConfigDialog.27
            public void get(boolean z) {
                LunariansSettings.enableLossWarn = z;
                ResourceLossWarn.reset();
            }
        }, new Maker() { // from class: lunarians.ui.ConfigDialog.28
            @Override // lunarians.ui.ConfigDialog.Maker
            public BuilderDialog make() {
                return ConfigDialog.dlg(LunariansI18n.get("loss_section"), new Cons<Table>() { // from class: lunarians.ui.ConfigDialog.28.1
                    public void get(Table table4) {
                        LossWarnUi.build(table4);
                    }
                }, null);
            }
        });
        feature(table, LunariansI18n.get("autounit"), LunariansSettings.enableAutoUnit, new Boolc() { // from class: lunarians.ui.ConfigDialog.29
            public void get(boolean z) {
                LunariansSettings.enableAutoUnit = z;
                AutoUnit.reset();
            }
        }, new Maker() { // from class: lunarians.ui.ConfigDialog.30
            @Override // lunarians.ui.ConfigDialog.Maker
            public BuilderDialog make() {
                return ConfigDialog.dlg(LunariansI18n.get("autounit"), new Cons<Table>() { // from class: lunarians.ui.ConfigDialog.30.1
                    public void get(Table table4) {
                        new AutoUnitUi(table4);
                    }
                }, new Runnable() { // from class: lunarians.ui.ConfigDialog.30.2
                    @Override // java.lang.Runnable
                    public void run() {
                        AutoUnit.reset();
                    }
                });
            }
        });
        feature(table, "Auto unit heal", LunariansSettings.enableAutoUnitHeal, new Boolc() { // from class: lunarians.ui.ConfigDialog.31
            public void get(boolean z) {
                LunariansSettings.enableAutoUnitHeal = z;
                AutoUnitHeal.reset();
            }
        }, new Maker() { // from class: lunarians.ui.ConfigDialog.32
            @Override // lunarians.ui.ConfigDialog.Maker
            public BuilderDialog make() {
                return AutoUnitHealUi.make();
            }
        });
        feature(table, LunariansI18n.get("autocargo"), LunariansSettings.enableAutoCargo, new Boolc() { // from class: lunarians.ui.ConfigDialog.33
            public void get(boolean z) {
                LunariansSettings.enableAutoCargo = z;
                AutoCargo.reset();
            }
        }, new Maker() { // from class: lunarians.ui.ConfigDialog.34
            @Override // lunarians.ui.ConfigDialog.Maker
            public BuilderDialog make() {
                return AutoCargoUi.make();
            }
        });
        feature(table, AutoContainerUi.title(), LunariansSettings.enableAutoContainer, new Boolc() { // from class: lunarians.ui.ConfigDialog.35
            public void get(boolean z) {
                LunariansSettings.enableAutoContainer = z;
                AutoContainer.reset();
                if (z) {
                    AutoContainer.showMarks();
                }
            }
        }, new Maker() { // from class: lunarians.ui.ConfigDialog.36
            @Override // lunarians.ui.ConfigDialog.Maker
            public BuilderDialog make() {
                return AutoContainerUi.make();
            }
        });
        feature(table, "Auto defesa", LunariansSettings.enableAutoDefence, new Boolc() { // from class: lunarians.ui.ConfigDialog.37
            public void get(boolean z) {
                LunariansSettings.enableAutoDefence = z;
                AutoDefence.reset();
            }
        }, new Maker() { // from class: lunarians.ui.ConfigDialog.38
            @Override // lunarians.ui.ConfigDialog.Maker
            public BuilderDialog make() {
                return AutoDefenceUi.make();
            }
        });
        feature(table, "Auto shield", LunariansSettings.enableAutoShield, new Boolc() { // from class: lunarians.ui.ConfigDialog.39
            public void get(boolean z) {
                LunariansSettings.enableAutoShield = z;
                AutoShield.reset();
            }
        }, new Maker() { // from class: lunarians.ui.ConfigDialog.40
            @Override // lunarians.ui.ConfigDialog.Maker
            public BuilderDialog make() {
                return AutoShieldUi.make();
            }
        });
        feature(table, "Auto heal", LunariansSettings.enableAutoHeal, new Boolc() { // from class: lunarians.ui.ConfigDialog.41
            public void get(boolean z) {
                LunariansSettings.enableAutoHeal = z;
                AutoHeal.reset();
            }
        }, new Maker() { // from class: lunarians.ui.ConfigDialog.42
            @Override // lunarians.ui.ConfigDialog.Maker
            public BuilderDialog make() {
                return AutoHealUi.make();
            }
        });
        feature(table, "Auto attack", LunariansSettings.enableAutoAttack, new Boolc() { // from class: lunarians.ui.ConfigDialog.43
            public void get(boolean z) {
                LunariansSettings.enableAutoAttack = z;
                AutoAttack.reset();
            }
        }, new Maker() { // from class: lunarians.ui.ConfigDialog.44
            @Override // lunarians.ui.ConfigDialog.Maker
            public BuilderDialog make() {
                return AutoAttackUi.make();
            }
        });
        feature(table, LunariansI18n.get("schem_ui"), LunariansSettings.showSchematicPanel, new Boolc() { // from class: lunarians.ui.ConfigDialog.45
            public void get(boolean z) {
                SchematicPlacer.setPanelVisible(z);
            }
        }, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static BuilderDialog dlg(String str, Cons<Table> cons, Runnable runnable) {
        return new BuilderDialog(str, cons, runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void betaSize(Table table) {
        UiText.slider(table, LunariansI18n.get("beta_width"), 180.0f, 450.0f, 10.0f, LunariansSettings.betaWidth, new Floatc() { // from class: lunarians.ui.ConfigDialog.46
            public void get(float f) {
                LunariansSettings.betaWidth = f;
            }
        }, new Prov<CharSequence>() { // from class: lunarians.ui.ConfigDialog.47
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m72get() {
                return ((int) LunariansSettings.betaWidth) + "px";
            }
        });
        UiText.slider(table, LunariansI18n.get("beta_height"), 25.0f, 90.0f, 5.0f, LunariansSettings.betaHeightPct, new Floatc() { // from class: lunarians.ui.ConfigDialog.48
            public void get(float f) {
                LunariansSettings.betaHeightPct = f;
            }
        }, new Prov<CharSequence>() { // from class: lunarians.ui.ConfigDialog.49
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m73get() {
                return ((int) LunariansSettings.betaHeightPct) + "%";
            }
        });
    }

    private void menu(Table table, String str, final Maker maker) {
        table.button(str, new Runnable() { // from class: lunarians.ui.ConfigDialog.50
            @Override // java.lang.Runnable
            public void run() {
                ConfigDialog.this.open(maker.make());
            }
        }).height(44.0f).growX().pad(2.0f);
        next(table);
    }

    private void section(Table table, String str) {
        if (this.col != 0) {
            table.row();
            this.col = 0;
        }
        table.add(str).color(Pal.accent).left().wrap().growX().padTop(8.0f).padBottom(4.0f).colspan(this.cols).row();
        table.image().color(Pal.accent).height(2.0f).growX().padBottom(6.0f).colspan(this.cols).row();
    }

    private void next(Table table) {
        this.col++;
        if (this.col >= this.cols) {
            this.col = 0;
            table.row();
        }
    }

    private void autofillGear(Table table) {
        table.button(LunariansI18n.get("autofill_config"), Icon.settingsSmall, Styles.defaultt, new Runnable() { // from class: lunarians.ui.ConfigDialog.51
            @Override // java.lang.Runnable
            public void run() {
                ConfigDialog.this.hide();
                AutoFillConfigDialog autoFillConfigDialog = new AutoFillConfigDialog();
                autoFillConfigDialog.hidden(new Runnable() { // from class: lunarians.ui.ConfigDialog.51.1
                    @Override // java.lang.Runnable
                    public void run() {
                        new ConfigDialog().show();
                    }
                });
                autoFillConfigDialog.show();
            }
        }).height(40.0f).growX().pad(2.0f);
        next(table);
    }

    private void feature(Table table, String str, boolean z, final Boolc boolc, final Maker maker) {
        Table table2 = new Table();
        UiText.check(table2, str, z, new Boolc() { // from class: lunarians.ui.ConfigDialog.52
            public void get(boolean z2) {
                boolc.get(z2);
                LunariansSettings.save();
            }
        }).left().growX();
        if (maker != null) {
            table2.button(Icon.settingsSmall, Styles.defaulti, new Runnable() { // from class: lunarians.ui.ConfigDialog.53
                @Override // java.lang.Runnable
                public void run() {
                    ConfigDialog.this.open(maker.make());
                }
            }).size(44.0f).padLeft(6.0f);
        }
        table.add(table2).growX().pad(2.0f);
        next(table);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void open(BuilderDialog builderDialog) {
        hide();
        builderDialog.after(new Runnable() { // from class: lunarians.ui.ConfigDialog.54
            @Override // java.lang.Runnable
            public void run() {
                new ConfigDialog().show();
            }
        });
        builderDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void resetPosition() {
        LunariansSettings.panelX = 20.0f;
        LunariansSettings.panelY = 100.0f;
        LunariansSettings.save();
        if (LunariansMod.panel != null) {
            LunariansMod.panel.applySettings();
        }
        Vars.ui.showInfoToast(LunariansI18n.get("position_reset"), 2.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void confirmResetAll() {
        Vars.ui.showConfirm(LunariansI18n.get("reset_all") + "?", new Runnable() { // from class: lunarians.ui.ConfigDialog.55
            @Override // java.lang.Runnable
            public void run() {
                LunariansSettings.resetAll();
                AutoUnit.reset();
                AutoCargo.reset();
                AutoContainer.reset();
                if (LunariansMod.panel != null) {
                    LunariansMod.panel.applySettings();
                }
                if (LunariansMod.betaHud != null) {
                    LunariansMod.betaHud.refresh();
                }
                ConfigDialog.this.hide();
                Vars.ui.showInfoToast(LunariansI18n.get("all_reset"), 2.0f);
            }
        });
    }
}
