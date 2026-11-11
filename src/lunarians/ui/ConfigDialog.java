/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.func.Boolc
 *  arc.func.Cons
 *  arc.func.Floatc
 *  arc.func.Prov
 *  arc.scene.Element
 *  arc.scene.style.Drawable
 *  arc.scene.ui.layout.Table
 *  mindustry.Vars
 *  mindustry.gen.Icon
 *  mindustry.graphics.Pal
 *  mindustry.ui.Styles
 *  mindustry.ui.dialogs.BaseDialog
 */
package lunarians.ui;

import arc.Core;
import arc.func.Boolc;
import arc.func.Cons;
import arc.func.Floatc;
import arc.func.Prov;
import arc.scene.Element;
import arc.scene.style.Drawable;
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
import lunarians.ui.AutoAttackUi;
import lunarians.ui.AutoCargoUi;
import lunarians.ui.AutoConnectConveyorUi;
import lunarians.ui.AutoContainerUi;
import lunarians.ui.AutoDefenceUi;
import lunarians.ui.AutoDrillUi;
import lunarians.ui.AutoFillConfigDialog;
import lunarians.ui.AutoHealUi;
import lunarians.ui.AutoMoonUi;
import lunarians.ui.AutoShieldUi;
import lunarians.ui.AutoUnitHealUi;
import lunarians.ui.AutoUnitUi;
import lunarians.ui.BuilderDialog;
import lunarians.ui.LossWarnUi;
import lunarians.ui.SchematicSettingsUi;
import lunarians.ui.SimpleFeatureDialogs;
import lunarians.ui.UiText;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class ConfigDialog
extends BaseDialog {
    private int cols = Core.graphics.getWidth() >= 640 ? 2 : 1;
    private int col = 0;

    public ConfigDialog() {
        super(LunariansI18n.get("config_title"));
        this.cont.pane((Cons)new Cons<Table>(){

            public void get(Table table) {
                table.defaults().growX().padBottom(4.0f);
                ConfigDialog.this.build(table);
            }
        }).grow().maxHeight((float)Core.graphics.getHeight() * 0.78f).width(Math.min((float)Core.graphics.getWidth() * 0.96f, 900.0f));
        this.buttons.defaults().size(150.0f, 48.0f).pad(4.0f);
        this.buttons.button(LunariansI18n.get("reset_position"), new Runnable(){

            @Override
            public void run() {
                ConfigDialog.resetPosition();
            }
        });
        this.buttons.button(LunariansI18n.get("reset_all"), new Runnable(){

            @Override
            public void run() {
                ConfigDialog.this.confirmResetAll();
            }
        });
        this.buttons.button(LunariansI18n.get("back"), new Runnable(){

            @Override
            public void run() {
                ConfigDialog.this.hide();
            }
        });
        this.closeOnBack();
        this.hidden(new Runnable(){

            @Override
            public void run() {
                LunariansSettings.save();
            }
        });
    }

    private void build(Table table) {
        this.section(table, LunariansI18n.get("cfg_general"));
        this.menu(table, LunariansI18n.get("cfg_panel"), new Maker(){

            @Override
            public BuilderDialog make() {
                return ConfigDialog.dlg(LunariansI18n.get("cfg_panel"), (Cons<Table>)((Cons)new Cons<Table>(){

                    public void get(Table table) {
                        SchematicSettingsUi.build(table);
                    }
                }), new Runnable(){

                    @Override
                    public void run() {
                        SchematicSettingsUi.onClosed();
                    }
                });
            }
        });
        this.menu(table, LunariansI18n.get("cfg_categories"), new Maker(){

            @Override
            public BuilderDialog make() {
                return ConfigDialog.dlg(LunariansI18n.get("cfg_categories"), (Cons<Table>)((Cons)new Cons<Table>(){

                    public void get(Table table) {
                        SchematicSettingsUi.buildCategories(table);
                    }
                }), new Runnable(){

                    @Override
                    public void run() {
                        SchematicSettingsUi.onClosed();
                    }
                });
            }
        });
        this.menu(table, "Auto Set", new Maker(){

            @Override
            public BuilderDialog make() {
                return ConfigDialog.dlg("Auto Set", (Cons<Table>)((Cons)new Cons<Table>(){

                    public void get(Table table) {
                        AutoSetConfig.build(table);
                    }
                }), new Runnable(){

                    @Override
                    public void run() {
                        LunariansSettings.save();
                    }
                });
            }
        });
        this.menu(table, LunariansI18n.get("cfg_beta_panel"), new Maker(){

            @Override
            public BuilderDialog make() {
                return ConfigDialog.dlg(LunariansI18n.get("cfg_beta_panel"), (Cons<Table>)((Cons)new Cons<Table>(){

                    public void get(Table table) {
                        ConfigDialog.betaSize(table);
                    }
                }), new Runnable(){

                    @Override
                    public void run() {
                        if (LunariansMod.betaHud != null) {
                            LunariansMod.betaHud.refresh();
                        }
                    }
                });
            }
        });
        this.menu(table, LunariansI18n.get("language"), new Maker(){

            @Override
            public BuilderDialog make() {
                return ConfigDialog.dlg(LunariansI18n.get("language"), (Cons<Table>)((Cons)new Cons<Table>(){

                    public void get(final Table table) {
                        SimpleFeatureDialogs.Language.build(table, new Runnable(){

                            @Override
                            public void run() {
                                table.clear();
                                SimpleFeatureDialogs.Language.build(table, this);
                            }
                        });
                    }
                }), null);
            }
        });
        this.section(table, LunariansI18n.get("extras"));
        Table table2 = new Table();
        table2.add((CharSequence)"Modo:").left().padRight(8.0f);
        String string = LunariansSettings.pvpMode ? "Balanced ON" : "Normal";
        table2.button(string, new Runnable(){

            @Override
            public void run() {
                LunariansSettings.pvpMode = !LunariansSettings.pvpMode;
                LunariansSettings.save();
                ConfigDialog.this.hide();
                new ConfigDialog().show();
            }
        }).width(120.0f).height(40.0f);
        table.add((Element)table2).growX().pad(4.0f);
        this.next(table);
        if (LunariansSettings.pvpMode) {
            Table table3 = new Table();
            table3.add((CharSequence)"Balanced delay:").left().padRight(6.0f);
            table3.button("-", new Runnable(){

                @Override
                public void run() {
                    LunariansSettings.pvpDelay = Math.max(1.0f, LunariansSettings.pvpDelay - 0.5f);
                    LunariansSettings.save();
                    ConfigDialog.this.hide();
                    new ConfigDialog().show();
                }
            }).size(40.0f, 36.0f);
            table3.add((CharSequence)String.format("%.1fs", Float.valueOf(LunariansSettings.pvpDelay))).width(56.0f).center().pad(4.0f);
            table3.button("+", new Runnable(){

                @Override
                public void run() {
                    LunariansSettings.pvpDelay = Math.min(15.0f, LunariansSettings.pvpDelay + 0.5f);
                    LunariansSettings.save();
                    ConfigDialog.this.hide();
                    new ConfigDialog().show();
                }
            }).size(40.0f, 36.0f);
            table.add((Element)table3).left().pad(4.0f);
            this.next(table);
        }
        this.feature(table, "Auto drill", LunariansSettings.enableAutoDrill, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoDrill = bl;
            }
        }, new Maker(){

            @Override
            public BuilderDialog make() {
                return AutoDrillUi.make();
            }
        });
        this.feature(table, "Auto connect conveyor", LunariansSettings.enableAutoConnectConveyor, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoConnectConveyor = bl;
            }
        }, new Maker(){

            @Override
            public BuilderDialog make() {
                return AutoConnectConveyorUi.make();
            }
        });
        this.feature(table, "Auto Moon", LunariansSettings.enableAutoMoon, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoMoon = bl;
                AutoMoon.reset();
            }
        }, new Maker(){

            @Override
            public BuilderDialog make() {
                return AutoMoonUi.make();
            }
        });
        this.feature(table, LunariansI18n.get("enemy_core_alert"), LunariansSettings.enableEnemyCoreAlert, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableEnemyCoreAlert = bl;
            }
        }, new Maker(){

            @Override
            public BuilderDialog make() {
                return ConfigDialog.dlg(LunariansI18n.get("enemy_core_alert_title"), (Cons<Table>)((Cons)new Cons<Table>(){

                    public void get(Table table) {
                        SimpleFeatureDialogs.EnemyAlert.build(table);
                    }
                }), null);
            }
        });
        this.feature(table, LunariansI18n.get("autofill"), LunariansSettings.enableAutoFill, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoFill = bl;
            }
        }, null);
        this.autofillGear(table);
        this.feature(table, LunariansI18n.get("freecam"), LunariansSettings.enableFreeCam, new Boolc(){

            public void get(boolean bl) {
                FreeCam.setEnabled(bl);
            }
        }, null);
        this.feature(table, LunariansI18n.get("prod_alert"), LunariansSettings.enableProductionAlert, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableProductionAlert = bl;
                if (bl) {
                    ProductionAlert.reset();
                }
            }
        }, new Maker(){

            @Override
            public BuilderDialog make() {
                return ConfigDialog.dlg(LunariansI18n.get("prod_alert"), (Cons<Table>)((Cons)new Cons<Table>(){

                    public void get(Table table) {
                        SimpleFeatureDialogs.ProdAlert.build(table);
                    }
                }), null);
            }
        });
        this.feature(table, LunariansI18n.get("camfollow"), LunariansSettings.enableCamFollow, new Boolc(){

            public void get(boolean bl) {
                CamFollow.setEnabled(bl);
            }
        }, null);
        this.feature(table, LunariansI18n.get("loss"), LunariansSettings.enableLossWarn, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableLossWarn = bl;
                ResourceLossWarn.reset();
            }
        }, new Maker(){

            @Override
            public BuilderDialog make() {
                return ConfigDialog.dlg(LunariansI18n.get("loss_section"), (Cons<Table>)((Cons)new Cons<Table>(){

                    public void get(Table table) {
                        LossWarnUi.build(table);
                    }
                }), null);
            }
        });
        this.feature(table, LunariansI18n.get("autounit"), LunariansSettings.enableAutoUnit, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoUnit = bl;
                AutoUnit.reset();
            }
        }, new Maker(){

            @Override
            public BuilderDialog make() {
                return ConfigDialog.dlg(LunariansI18n.get("autounit"), (Cons<Table>)((Cons)new Cons<Table>(){

                    public void get(Table table) {
                        new AutoUnitUi(table);
                    }
                }), new Runnable(){

                    @Override
                    public void run() {
                        AutoUnit.reset();
                    }
                });
            }
        });
        this.feature(table, "Auto unit heal", LunariansSettings.enableAutoUnitHeal, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoUnitHeal = bl;
                AutoUnitHeal.reset();
            }
        }, new Maker(){

            @Override
            public BuilderDialog make() {
                return AutoUnitHealUi.make();
            }
        });
        this.feature(table, LunariansI18n.get("autocargo"), LunariansSettings.enableAutoCargo, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoCargo = bl;
                AutoCargo.reset();
            }
        }, new Maker(){

            @Override
            public BuilderDialog make() {
                return AutoCargoUi.make();
            }
        });
        this.feature(table, AutoContainerUi.title(), LunariansSettings.enableAutoContainer, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoContainer = bl;
                AutoContainer.reset();
                if (bl) {
                    AutoContainer.showMarks();
                }
            }
        }, new Maker(){

            @Override
            public BuilderDialog make() {
                return AutoContainerUi.make();
            }
        });
        this.feature(table, "Auto defesa", LunariansSettings.enableAutoDefence, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoDefence = bl;
                AutoDefence.reset();
            }
        }, new Maker(){

            @Override
            public BuilderDialog make() {
                return AutoDefenceUi.make();
            }
        });
        this.feature(table, "Auto shield", LunariansSettings.enableAutoShield, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoShield = bl;
                AutoShield.reset();
            }
        }, new Maker(){

            @Override
            public BuilderDialog make() {
                return AutoShieldUi.make();
            }
        });
        this.feature(table, "Auto heal", LunariansSettings.enableAutoHeal, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoHeal = bl;
                AutoHeal.reset();
            }
        }, new Maker(){

            @Override
            public BuilderDialog make() {
                return AutoHealUi.make();
            }
        });
        this.feature(table, "Auto attack", LunariansSettings.enableAutoAttack, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoAttack = bl;
                AutoAttack.reset();
            }
        }, new Maker(){

            @Override
            public BuilderDialog make() {
                return AutoAttackUi.make();
            }
        });
        this.feature(table, LunariansI18n.get("schem_ui"), LunariansSettings.showSchematicPanel, new Boolc(){

            public void get(boolean bl) {
                SchematicPlacer.setPanelVisible(bl);
            }
        }, null);
    }

    private static BuilderDialog dlg(String string, Cons<Table> cons, Runnable runnable) {
        return new BuilderDialog(string, cons, runnable);
    }

    private static void betaSize(Table table) {
        UiText.slider(table, LunariansI18n.get("beta_width"), 180.0f, 450.0f, 10.0f, LunariansSettings.betaWidth, new Floatc(){

            public void get(float f) {
                LunariansSettings.betaWidth = f;
            }
        }, new Prov<CharSequence>(){

            public CharSequence get() {
                return (int)LunariansSettings.betaWidth + "px";
            }
        });
        UiText.slider(table, LunariansI18n.get("beta_height"), 25.0f, 90.0f, 5.0f, LunariansSettings.betaHeightPct, new Floatc(){

            public void get(float f) {
                LunariansSettings.betaHeightPct = f;
            }
        }, new Prov<CharSequence>(){

            public CharSequence get() {
                return (int)LunariansSettings.betaHeightPct + "%";
            }
        });
    }

    private void menu(Table table, String string, final Maker maker) {
        table.button(string, new Runnable(){

            @Override
            public void run() {
                ConfigDialog.this.open(maker.make());
            }
        }).height(44.0f).growX().pad(2.0f);
        this.next(table);
    }

    private void section(Table table, String string) {
        if (this.col != 0) {
            table.row();
            this.col = 0;
        }
        table.add((CharSequence)string).color(Pal.accent).left().wrap().growX().padTop(8.0f).padBottom(4.0f).colspan(this.cols).row();
        table.image().color(Pal.accent).height(2.0f).growX().padBottom(6.0f).colspan(this.cols).row();
    }

    private void next(Table table) {
        ++this.col;
        if (this.col >= this.cols) {
            this.col = 0;
            table.row();
        }
    }

    private void autofillGear(Table table) {
        table.button(LunariansI18n.get("autofill_config"), (Drawable)Icon.settingsSmall, Styles.defaultt, new Runnable(){

            @Override
            public void run() {
                ConfigDialog.this.hide();
                AutoFillConfigDialog autoFillConfigDialog = new AutoFillConfigDialog();
                autoFillConfigDialog.hidden(new Runnable(){

                    @Override
                    public void run() {
                        new ConfigDialog().show();
                    }
                });
                autoFillConfigDialog.show();
            }
        }).height(40.0f).growX().pad(2.0f);
        this.next(table);
    }

    private void feature(Table table, String string, boolean bl, final Boolc boolc, final Maker maker) {
        Table table2 = new Table();
        UiText.check(table2, string, bl, new Boolc(){

            public void get(boolean bl) {
                boolc.get(bl);
                LunariansSettings.save();
            }
        }).left().growX();
        if (maker != null) {
            table2.button((Drawable)Icon.settingsSmall, Styles.defaulti, new Runnable(){

                @Override
                public void run() {
                    ConfigDialog.this.open(maker.make());
                }
            }).size(44.0f).padLeft(6.0f);
        }
        table.add((Element)table2).growX().pad(2.0f);
        this.next(table);
    }

    private void open(BuilderDialog builderDialog) {
        this.hide();
        builderDialog.after(new Runnable(){

            @Override
            public void run() {
                new ConfigDialog().show();
            }
        });
        builderDialog.show();
    }

    private static void resetPosition() {
        LunariansSettings.panelX = 20.0f;
        LunariansSettings.panelY = 100.0f;
        LunariansSettings.save();
        if (LunariansMod.panel != null) {
            LunariansMod.panel.applySettings();
        }
        Vars.ui.showInfoToast(LunariansI18n.get("position_reset"), 2.0f);
    }

    private void confirmResetAll() {
        Vars.ui.showConfirm(LunariansI18n.get("reset_all") + "?", new Runnable(){

            @Override
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

    private static interface Maker {
        public BuilderDialog make();
    }
}

