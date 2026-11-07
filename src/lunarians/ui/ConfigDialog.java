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
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class ConfigDialog extends BaseDialog {
  private int cols = (Core.graphics.getWidth() >= 640) ? 2 : 1;
  
  private int col = 0;
  
  public ConfigDialog() {
    super(LunariansI18n.get("config_title"));
    this.cont.pane(new Cons<Table>() {
          public void get(Table param1Table) {
            param1Table.defaults().growX().padBottom(4.0F);
            ConfigDialog.this.build(param1Table);
          }
        }).grow().maxHeight(Core.graphics.getHeight() * 0.78F).width(Math.min(Core.graphics.getWidth() * 0.96F, 900.0F));
    this.buttons.defaults().size(150.0F, 48.0F).pad(4.0F);
    this.buttons.button(LunariansI18n.get("reset_position"), new Runnable() {
          public void run() {
            ConfigDialog.resetPosition();
          }
        });
    this.buttons.button(LunariansI18n.get("reset_all"), new Runnable() {
          public void run() {
            ConfigDialog.this.confirmResetAll();
          }
        });
    this.buttons.button(LunariansI18n.get("back"), new Runnable() {
          public void run() {
            ConfigDialog.this.hide();
          }
        });
    closeOnBack();
    hidden(new Runnable() {
          public void run() {
            LunariansSettings.save();
          }
        });
  }
  
  private void build(Table paramTable) {
    section(paramTable, LunariansI18n.get("cfg_general"));
    menu(paramTable, LunariansI18n.get("cfg_panel"), new Maker() {
          public BuilderDialog make() {
            return ConfigDialog.dlg(LunariansI18n.get("cfg_panel"), new Cons<Table>() {
                  public void get(Table param2Table) {
                    SchematicSettingsUi.build(param2Table);
                  }
                },  new Runnable() {
                  public void run() {
                    SchematicSettingsUi.onClosed();
                  }
                });
          }
        });
    menu(paramTable, LunariansI18n.get("cfg_categories"), new Maker() {
          public BuilderDialog make() {
            return ConfigDialog.dlg(LunariansI18n.get("cfg_categories"), new Cons<Table>() {
                  public void get(Table param2Table) {
                    SchematicSettingsUi.buildCategories(param2Table);
                  }
                },  new Runnable() {
                  public void run() {
                    SchematicSettingsUi.onClosed();
                  }
                });
          }
        });
    menu(paramTable, "Auto Set", new Maker() {
          public BuilderDialog make() {
            return ConfigDialog.dlg("Auto Set", new Cons<Table>() {
                  public void get(Table param2Table) {
                    AutoSetConfig.build(param2Table);
                  }
                },  new Runnable() {
                  public void run() {
                    LunariansSettings.save();
                  }
                });
          }
        });
    menu(paramTable, LunariansI18n.get("cfg_beta_panel"), new Maker() {
          public BuilderDialog make() {
            return ConfigDialog.dlg(LunariansI18n.get("cfg_beta_panel"), new Cons<Table>() {
                  public void get(Table param2Table) {
                    ConfigDialog.betaSize(param2Table);
                  }
                },  new Runnable() {
                  public void run() {
                    if (LunariansMod.betaHud != null)
                      LunariansMod.betaHud.refresh(); 
                  }
                });
          }
        });
    menu(paramTable, LunariansI18n.get("language"), new Maker() {
          public BuilderDialog make() {
            return ConfigDialog.dlg(LunariansI18n.get("language"), new Cons<Table>() {
                  public void get(final Table table) {
                    SimpleFeatureDialogs.Language.build(table, new Runnable() {
                          public void run() {
                            table.clear();
                            SimpleFeatureDialogs.Language.build(table, this);
                          }
                        });
                  }
                }(Runnable)null);
          }
        });
    section(paramTable, LunariansI18n.get("extras"));
    Table table = new Table();
    table.add("Modo:").left().padRight(8.0F);
    String str = LunariansSettings.pvpMode ? "Balanced ON" : "Normal";
    table.button(str, new Runnable() {
          public void run() {
            LunariansSettings.pvpMode = !LunariansSettings.pvpMode;
            LunariansSettings.save();
            ConfigDialog.this.hide();
            (new ConfigDialog()).show();
          }
        }).width(120.0F).height(40.0F);
    paramTable.add((Element)table).growX().pad(4.0F);
    next(paramTable);
    if (LunariansSettings.pvpMode) {
      Table table1 = new Table();
      table1.add("Balanced delay:").left().padRight(6.0F);
      table1.button("-", new Runnable() {
            public void run() {
              LunariansSettings.pvpDelay = Math.max(1.0F, LunariansSettings.pvpDelay - 0.5F);
              LunariansSettings.save();
              ConfigDialog.this.hide();
              (new ConfigDialog()).show();
            }
          }).size(40.0F, 36.0F);
      table1.add(String.format("%.1fs", new Object[] { Float.valueOf(LunariansSettings.pvpDelay) })).width(56.0F).center().pad(4.0F);
      table1.button("+", new Runnable() {
            public void run() {
              LunariansSettings.pvpDelay = Math.min(15.0F, LunariansSettings.pvpDelay + 0.5F);
              LunariansSettings.save();
              ConfigDialog.this.hide();
              (new ConfigDialog()).show();
            }
          }).size(40.0F, 36.0F);
      paramTable.add((Element)table1).left().pad(4.0F);
      next(paramTable);
    } 
    feature(paramTable, "Auto drill", LunariansSettings.enableAutoDrill, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoDrill = param1Boolean;
          }
        },  new Maker() {
          public BuilderDialog make() {
            return AutoDrillUi.make();
          }
        });
    feature(paramTable, "Auto connect conveyor", LunariansSettings.enableAutoConnectConveyor, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoConnectConveyor = param1Boolean;
          }
        },  new Maker() {
          public BuilderDialog make() {
            return AutoConnectConveyorUi.make();
          }
        });
    feature(paramTable, "Auto Moon", LunariansSettings.enableAutoMoon, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoMoon = param1Boolean;
            AutoMoon.reset();
          }
        },  new Maker() {
          public BuilderDialog make() {
            return AutoMoonUi.make();
          }
        });
    feature(paramTable, LunariansI18n.get("enemy_core_alert"), LunariansSettings.enableEnemyCoreAlert, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableEnemyCoreAlert = param1Boolean;
          }
        },  new Maker() {
          public BuilderDialog make() {
            return ConfigDialog.dlg(LunariansI18n.get("enemy_core_alert_title"), new Cons<Table>() {
                  public void get(Table param2Table) {
                    SimpleFeatureDialogs.EnemyAlert.build(param2Table);
                  }
                },  (Runnable)null);
          }
        });
    feature(paramTable, LunariansI18n.get("autofill"), LunariansSettings.enableAutoFill, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoFill = param1Boolean;
          }
        },  (Maker)null);
    autofillGear(paramTable);
    feature(paramTable, LunariansI18n.get("freecam"), LunariansSettings.enableFreeCam, new Boolc() {
          public void get(boolean param1Boolean) {
            FreeCam.setEnabled(param1Boolean);
          }
        },  (Maker)null);
    feature(paramTable, LunariansI18n.get("prod_alert"), LunariansSettings.enableProductionAlert, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableProductionAlert = param1Boolean;
            if (param1Boolean)
              ProductionAlert.reset(); 
          }
        },  new Maker() {
          public BuilderDialog make() {
            return ConfigDialog.dlg(LunariansI18n.get("prod_alert"), new Cons<Table>() {
                  public void get(Table param2Table) {
                    SimpleFeatureDialogs.ProdAlert.build(param2Table);
                  }
                },  (Runnable)null);
          }
        });
    feature(paramTable, LunariansI18n.get("camfollow"), LunariansSettings.enableCamFollow, new Boolc() {
          public void get(boolean param1Boolean) {
            CamFollow.setEnabled(param1Boolean);
          }
        },  (Maker)null);
    feature(paramTable, LunariansI18n.get("loss"), LunariansSettings.enableLossWarn, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableLossWarn = param1Boolean;
            ResourceLossWarn.reset();
          }
        },  new Maker() {
          public BuilderDialog make() {
            return ConfigDialog.dlg(LunariansI18n.get("loss_section"), new Cons<Table>() {
                  public void get(Table param2Table) {
                    LossWarnUi.build(param2Table);
                  }
                },  (Runnable)null);
          }
        });
    feature(paramTable, LunariansI18n.get("autounit"), LunariansSettings.enableAutoUnit, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoUnit = param1Boolean;
            AutoUnit.reset();
          }
        },  new Maker() {
          public BuilderDialog make() {
            return ConfigDialog.dlg(LunariansI18n.get("autounit"), new Cons<Table>() {
                  public void get(Table param2Table) {
                    new AutoUnitUi(param2Table);
                  }
                },  new Runnable() {
                  public void run() {
                    AutoUnit.reset();
                  }
                });
          }
        });
    feature(paramTable, "Auto unit heal", LunariansSettings.enableAutoUnitHeal, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoUnitHeal = param1Boolean;
            AutoUnitHeal.reset();
          }
        },  new Maker() {
          public BuilderDialog make() {
            return AutoUnitHealUi.make();
          }
        });
    feature(paramTable, LunariansI18n.get("autocargo"), LunariansSettings.enableAutoCargo, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoCargo = param1Boolean;
            AutoCargo.reset();
          }
        },  new Maker() {
          public BuilderDialog make() {
            return AutoCargoUi.make();
          }
        });
    feature(paramTable, AutoContainerUi.title(), LunariansSettings.enableAutoContainer, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoContainer = param1Boolean;
            AutoContainer.reset();
            if (param1Boolean)
              AutoContainer.showMarks(); 
          }
        },  new Maker() {
          public BuilderDialog make() {
            return AutoContainerUi.make();
          }
        });
    feature(paramTable, "Auto defesa", LunariansSettings.enableAutoDefence, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoDefence = param1Boolean;
            AutoDefence.reset();
          }
        },  new Maker() {
          public BuilderDialog make() {
            return AutoDefenceUi.make();
          }
        });
    feature(paramTable, "Auto shield", LunariansSettings.enableAutoShield, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoShield = param1Boolean;
            AutoShield.reset();
          }
        },  new Maker() {
          public BuilderDialog make() {
            return AutoShieldUi.make();
          }
        });
    feature(paramTable, "Auto heal", LunariansSettings.enableAutoHeal, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoHeal = param1Boolean;
            AutoHeal.reset();
          }
        },  new Maker() {
          public BuilderDialog make() {
            return AutoHealUi.make();
          }
        });
    feature(paramTable, "Auto attack", LunariansSettings.enableAutoAttack, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoAttack = param1Boolean;
            AutoAttack.reset();
          }
        },  new Maker() {
          public BuilderDialog make() {
            return AutoAttackUi.make();
          }
        });
    feature(paramTable, LunariansI18n.get("schem_ui"), LunariansSettings.showSchematicPanel, new Boolc() {
          public void get(boolean param1Boolean) {
            SchematicPlacer.setPanelVisible(param1Boolean);
          }
        },  (Maker)null);
  }
  
  private static interface Maker {
    BuilderDialog make();
  }
  
  private static BuilderDialog dlg(String paramString, Cons<Table> paramCons, Runnable paramRunnable) {
    return new BuilderDialog(paramString, paramCons, paramRunnable);
  }
  
  private static void betaSize(Table paramTable) {
    UiText.slider(paramTable, LunariansI18n.get("beta_width"), 180.0F, 450.0F, 10.0F, LunariansSettings.betaWidth, new Floatc() {
          public void get(float param1Float) {
            LunariansSettings.betaWidth = param1Float;
          }
        },  new Prov<CharSequence>() {
          public CharSequence get() {
            return (int)LunariansSettings.betaWidth + "px";
          }
        });
    UiText.slider(paramTable, LunariansI18n.get("beta_height"), 25.0F, 90.0F, 5.0F, LunariansSettings.betaHeightPct, new Floatc() {
          public void get(float param1Float) {
            LunariansSettings.betaHeightPct = param1Float;
          }
        },  new Prov<CharSequence>() {
          public CharSequence get() {
            return (int)LunariansSettings.betaHeightPct + "%";
          }
        });
  }
  
  private void menu(Table paramTable, String paramString, final Maker maker) {
    paramTable.button(paramString, new Runnable() {
          public void run() {
            ConfigDialog.this.open(maker.make());
          }
        }).height(44.0F).growX().pad(2.0F);
    next(paramTable);
  }
  
  private void section(Table paramTable, String paramString) {
    if (this.col != 0) {
      paramTable.row();
      this.col = 0;
    } 
    paramTable.add(paramString).color(Pal.accent).left().wrap().growX().padTop(8.0F).padBottom(4.0F).colspan(this.cols).row();
    paramTable.image().color(Pal.accent).height(2.0F).growX().padBottom(6.0F).colspan(this.cols).row();
  }
  
  private void next(Table paramTable) {
    this.col++;
    if (this.col >= this.cols) {
      this.col = 0;
      paramTable.row();
    } 
  }
  
  private void autofillGear(Table paramTable) {
    paramTable.button(LunariansI18n.get("autofill_config"), (Drawable)Icon.settingsSmall, Styles.defaultt, new Runnable() {
          public void run() {
            ConfigDialog.this.hide();
            AutoFillConfigDialog autoFillConfigDialog = new AutoFillConfigDialog();
            autoFillConfigDialog.hidden(new Runnable() {
                  public void run() {
                    (new ConfigDialog()).show();
                  }
                });
            autoFillConfigDialog.show();
          }
        }).height(40.0F).growX().pad(2.0F);
    next(paramTable);
  }
  
  private void feature(Table paramTable, String paramString, boolean paramBoolean, final Boolc boolc, final Maker maker) {
    Table table = new Table();
    UiText.check(table, paramString, paramBoolean, new Boolc() {
          public void get(boolean param1Boolean) {
            boolc.get(param1Boolean);
            LunariansSettings.save();
          }
        }).left().growX();
    if (maker != null)
      table.button((Drawable)Icon.settingsSmall, Styles.defaulti, new Runnable() {
            public void run() {
              ConfigDialog.this.open(maker.make());
            }
          }).size(44.0F).padLeft(6.0F); 
    paramTable.add((Element)table).growX().pad(2.0F);
    next(paramTable);
  }
  
  private void open(BuilderDialog paramBuilderDialog) {
    hide();
    paramBuilderDialog.after(new Runnable() {
          public void run() {
            (new ConfigDialog()).show();
          }
        });
    paramBuilderDialog.show();
  }
  
  private static void resetPosition() {
    LunariansSettings.panelX = 20.0F;
    LunariansSettings.panelY = 100.0F;
    LunariansSettings.save();
    if (LunariansMod.panel != null)
      LunariansMod.panel.applySettings(); 
    Vars.ui.showInfoToast(LunariansI18n.get("position_reset"), 2.0F);
  }
  
  private void confirmResetAll() {
    Vars.ui.showConfirm(LunariansI18n.get("reset_all") + "?", new Runnable() {
          public void run() {
            LunariansSettings.resetAll();
            AutoUnit.reset();
            AutoCargo.reset();
            AutoContainer.reset();
            if (LunariansMod.panel != null)
              LunariansMod.panel.applySettings(); 
            if (LunariansMod.betaHud != null)
              LunariansMod.betaHud.refresh(); 
            ConfigDialog.this.hide();
            Vars.ui.showInfoToast(LunariansI18n.get("all_reset"), 2.0F);
          }
        });
  }
}
