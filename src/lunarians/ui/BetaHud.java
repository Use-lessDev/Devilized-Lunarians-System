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
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;

public class BetaHud extends Table {
  private final Table panel;
  
  private final ImageButton toggleBtn;
  
  private boolean open = false;
  
  private Cell<ScrollPane> scrollCell;
  
  private final ScrollPane scroll;
  
  public BetaHud() {
    setFillParent(true);
    right().top();
    defaults().right();
    this.panel = new Table(UiStyle.panelBg());
    this.panel.visible = false;
    this.panel.defaults().growX().pad(4.0F);
    this.toggleBtn = new ImageButton((Drawable)Icon.rightOpen, Styles.clearNonei);
    this.toggleBtn.clicked(new Runnable() {
          public void run() {
            BetaHud.this.toggle();
          }
        });
    try {
      this.toggleBtn.resizeImage(28.0F);
    } catch (Throwable throwable) {}
    this.scroll = new ScrollPane((Element)this.panel, Styles.smallPane);
    this.scroll.setScrollingDisabled(true, false);
    this.scroll.setFadeScrollBars(false);
    this.scroll.visible = false;
    Table table = new Table();
    this.scrollCell = table.add((Element)this.scroll).top();
    applySize();
    table.add((Element)this.toggleBtn).size(40.0F, 40.0F).top().pad(6.0F);
    add((Element)table).top().right().padTop(120.0F);
    try {
      rebuildPanel();
    } catch (Throwable throwable) {
      this.panel.clear();
      this.panel.add("Beta HUD error").color(Color.scarlet).pad(6.0F).row();
      Log.err("[Lunarians] BetaHud rebuild failed", throwable);
    } 
  }
  
  public void toggle() {
    boolean bool = this.open = !this.open;
    if (this.open) {
      try {
        rebuildPanel();
      } catch (Throwable throwable) {
        Log.err("[Lunarians] BetaHud rebuild failed", throwable);
      } 
      this.panel.visible = true;
      this.scroll.visible = true;
      this.panel.actions(new Action[] { (Action)Actions.sequence((Action)Actions.alpha(0.0F), (Action)Actions.fadeIn(0.2F)) });
      try {
        (this.toggleBtn.getStyle()).imageUp = (Drawable)Icon.leftOpen;
      } catch (Throwable throwable) {}
    } else {
      this.panel.actions(new Action[] { (Action)Actions.sequence((Action)Actions.fadeOut(0.15F), (Action)Actions.run(new Runnable() {
                  public void run() {
                    BetaHud.this.panel.visible = false;
                    BetaHud.this.scroll.visible = false;
                  }
                })) });
      try {
        (this.toggleBtn.getStyle()).imageUp = (Drawable)Icon.rightOpen;
      } catch (Throwable throwable) {}
    } 
  }
  
  public void applySize() {
    try {
      float f = Core.graphics.getHeight() * LunariansSettings.betaHeightPct / 100.0F;
      this.scrollCell.width(LunariansSettings.betaWidth).maxHeight(f);
      invalidateHierarchy();
    } catch (Throwable throwable) {}
  }
  
  public void refresh() {
    applySize();
    if (this.open)
      try {
        rebuildPanel();
      } catch (Throwable throwable) {
        Log.err("[Lunarians] BetaHud refresh failed", throwable);
      }  
  }
  
  private void rebuildPanel() {
    this.panel.clear();
    UiStyle.banner(this.panel, "LUNARIANS", safeI18n("beta_features", "Beta features"));
    UiStyle.section(this.panel, t("Mode", "Modo"));
    String str1 = LunariansSettings.pvpMode ? "Balanced ON" : "Normal";
    TextButton textButton2 = (TextButton)this.panel.button(str1, new Runnable() {
          public void run() {
            LunariansSettings.pvpMode = !LunariansSettings.pvpMode;
            LunariansSettings.save();
            BetaHud.this.rebuildPanel();
          }
        }).growX().height(42.0F).pad(4.0F).get();
    textButton2.setColor(LunariansSettings.pvpMode ? Color.scarlet : Pal.accent);
    try {
      textButton2.getLabel().setWrap(true);
    } catch (Throwable throwable) {}
    this.panel.row();
    if (LunariansSettings.pvpMode) {
      Table table = new Table();
      table.add("Delay:").left().padRight(4.0F);
      table.button("-", new Runnable() {
            public void run() {
              LunariansSettings.pvpDelay = Math.max(1.0F, LunariansSettings.pvpDelay - 0.5F);
              LunariansSettings.save();
              BetaHud.this.rebuildPanel();
            }
          }).size(36.0F, 32.0F);
      table.add(String.format("%.1fs", new Object[] { Float.valueOf(LunariansSettings.pvpDelay) })).width(52.0F).center().pad(2.0F);
      table.button("+", new Runnable() {
            public void run() {
              LunariansSettings.pvpDelay = Math.min(15.0F, LunariansSettings.pvpDelay + 0.5F);
              LunariansSettings.save();
              BetaHud.this.rebuildPanel();
            }
          }).size(36.0F, 32.0F);
      this.panel.add((Element)table).left().pad(4.0F).row();
    } 
    UiStyle.section(this.panel, t("Configuration", "Configuração"));
    TextButton textButton1 = (TextButton)this.panel.button(safeI18n("settings_btn", "Settings"), new Runnable() {
          public void run() {
            try {
              (new ConfigDialog()).show();
            } catch (Throwable throwable) {
              Log.err("[Lunarians] ConfigDialog", throwable);
              try {
                AutoDrillUi.make().show();
              } catch (Throwable throwable1) {
                if (Vars.ui != null)
                  Vars.ui.showInfoToast("Settings error", 3.0F); 
              } 
            } 
          }
        }).height(40.0F).growX().pad(4.0F).get();
    this.panel.row();
    UiStyle.section(this.panel, t("Auto Set and economy", "Auto Set e economia"));
    Table table1 = new Table();
    table1.add("Auto Set").left().padRight(6.0F);
    table1.button("GO", new Runnable() {
          public void run() {
            AutoSet.request();
          }
        }).size(52.0F, 40.0F).pad(2.0F);
    table1.button("X", new Runnable() {
          public void run() {
            AutoSet.cancel();
          }
        }).size(40.0F, 40.0F).pad(2.0F);
    try {
      table1.label(new Prov<CharSequence>() {
            public CharSequence get() {
              if (AutoSet.isRunning())
                return "[orange]RUN[]"; 
              if (AutoSet.isActive())
                return "[yellow]…[]"; 
              return "";
            }
          }).padLeft(4.0F);
    } catch (Throwable throwable) {}
    this.panel.add((Element)table1).growX().left().pad(4.0F).row();
    table1 = new Table();
    try {
      UiText.checkSmall(table1, "Drill", LunariansSettings.enableAutoDrill, new Boolc() {
            public void get(boolean param1Boolean) {
              LunariansSettings.enableAutoDrill = param1Boolean;
              LunariansSettings.save();
            }
          }).left();
    } catch (Throwable throwable) {
      table1.check("Drill", LunariansSettings.enableAutoDrill, new Boolc() {
            public void get(boolean param1Boolean) {
              LunariansSettings.enableAutoDrill = param1Boolean;
              LunariansSettings.save();
            }
          }).left();
    } 
    table1.button("Set", new Runnable() {
          public void run() {
            PositionPicker.pick(new Cons<int[]>() {
                  public void get(int[] param2ArrayOfint) {
                    LunariansSettings.drillStartX = param2ArrayOfint[0];
                    LunariansSettings.drillStartY = param2ArrayOfint[1];
                    LunariansSettings.save();
                    if (Vars.ui != null)
                      Vars.ui.showInfoToast("Ore: " + param2ArrayOfint[0] + "," + param2ArrayOfint[1], 2.0F); 
                  }
                }new Runnable() {
                  public void run() {}
                });
          }
        }).width(52.0F).height(40.0F).padLeft(4.0F);
    table1.button("Gen", new Runnable() {
          public void run() {
            AutoDrill.generate();
          }
        }).width(52.0F).height(40.0F).padLeft(2.0F);
    this.panel.add((Element)table1).growX().left().pad(4.0F).row();
    Table table2 = new Table();
    table2.add("Exit").padRight(6.0F);
    String[] arrayOfString = { ">", "^", "<", "v" };
    for (byte b1 = 0; b1 < 4; b1++) {
      final byte fmode = b1;
      TextButton textButton = (TextButton)table2.button(arrayOfString[b1], new Runnable() {
            public void run() {
              LunariansSettings.drillExitDir = n;
              LunariansSettings.save();
              BetaHud.this.rebuildPanel();
            }
          }).size(44.0F, 40.0F).pad(2.0F).get();
      try {
        if (LunariansSettings.drillExitDir == b) {
          textButton.setColor(Pal.accent);
        } else {
          textButton.setColor(Color.lightGray);
        } 
      } catch (Throwable throwable) {}
    } 
    String str2 = (LunariansSettings.drillLayoutMode == 1) ? "Full" : "Str";
    table2.button(str2, new Runnable() {
          public void run() {
            LunariansSettings.drillLayoutMode = (LunariansSettings.drillLayoutMode == 1) ? 0 : 1;
            LunariansSettings.save();
            BetaHud.this.rebuildPanel();
          }
        }).size(56.0F, 40.0F).padLeft(6.0F);
    this.panel.add((Element)table2).growX().left().pad(4.0F).row();
    table1 = new Table();
    try {
      UiText.checkSmall(table1, "D+C", LunariansSettings.enableAutoDrillConveyor, new Boolc() {
            public void get(boolean param1Boolean) {
              LunariansSettings.enableAutoDrillConveyor = param1Boolean;
              LunariansSettings.save();
            }
          }).left();
    } catch (Throwable throwable) {
      table1.check("D+C", LunariansSettings.enableAutoDrillConveyor, new Boolc() {
            public void get(boolean param1Boolean) {
              LunariansSettings.enableAutoDrillConveyor = param1Boolean;
              LunariansSettings.save();
            }
          }).left();
    } 
    table1.button("Gen", new Runnable() {
          public void run() {
            AutoDrill.generateWithConveyor();
          }
        }).width(52.0F).height(40.0F).padLeft(4.0F);
    this.panel.add((Element)table1).growX().left().pad(4.0F).row();
    Table table3 = new Table();
    table3.check("pt".equals(LunariansSettings.language) ? "Drills perfeitos (Beta)" : "Perfect drills (Beta)", LunariansSettings.drillPerfect, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.drillPerfect = param1Boolean;
            LunariansSettings.save();
          }
        }).left();
    this.panel.add((Element)table3).growX().left().pad(4.0F).row();
    Table table4 = new Table();
    table4.check("pt".equals(LunariansSettings.language) ? "Auto Dock + Energia (Beta)" : "Auto Dock + Energy (Beta)", LunariansSettings.dockAuto, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.dockAuto = param1Boolean;
            LunariansSettings.save();
            if (param1Boolean) {
              AutoDock.reset();
              Vars.ui.showInfoToast("Core: " + AutoDock.coreInfo(), 5.0F);
            } 
          }
        }).left();
    this.panel.add((Element)table4).growX().left().pad(4.0F).row();
    Table table5 = new Table();
    table5.check("pt".equals(LunariansSettings.language) ? "Economia em ordem (Beta)" : "Ordered economy (Beta)", AutoEconomy.enabled(), new Boolc() {
          public void get(boolean param1Boolean) {
            AutoEconomy.setEnabled(param1Boolean);
          }
        }).left();
    this.panel.add((Element)table5).growX().left().pad(4.0F).row();
    UiStyle.section(this.panel, t("Team and chat", "Equipe e chat"));
    Table table6 = new Table();
    table6.check("pt".equals(LunariansSettings.language) ? "Presença Lunarians (aura roxa)" : "Lunarians presence (purple aura)", LunariansPresence.enabled(), new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansPresence.setEnabled(param1Boolean);
            Vars.ui.showInfoToast(param1Boolean ? "Presence ON. Reconecte ao servidor para os outros te verem." : "Presence OFF. Reconecte para remover a marca do nome.", 2.0F);
          }
        }).left();
    this.panel.add((Element)table6).growX().left().pad(4.0F).row();
    Table table7 = new Table();
    table7.check("pt".equals(LunariansSettings.language) ? "Comandos por chat" : "Chat commands", ChatCommands.on(), new Boolc() {
          public void get(boolean param1Boolean) {
            Core.settings.put("lunarians-chat-on", Boolean.valueOf(param1Boolean));
          }
        }).left();
    table7.button("...", new Runnable() {
          public void run() {
            ChatCommands.openPanel();
          }
        }).width(52.0F).height(40.0F).padLeft(4.0F);
    this.panel.add((Element)table7).growX().left().pad(4.0F).row();
    UiStyle.section(this.panel, t("Units", "Unidades"));
    Table table8 = new Table();
    table8.check(MinerUnits.title() + " (Beta)", MinerUnits.enabled(), new Boolc() {
          public void get(boolean param1Boolean) {
            MinerUnits.setEnabled(param1Boolean);
            LunariansSettings.save();
          }
        }).left();
    table8.button("...", new Runnable() {
          public void run() {
            MinerUnits.dialog().show();
          }
        }).width(52.0F).height(40.0F).padLeft(4.0F);
    this.panel.add((Element)table8).growX().left().pad(4.0F).row();
    Table table9 = new Table();
    table9.add(t("Formation", "Formação")).color(UiStyle.TEXT_DIM).padRight(8.0F);
    for (byte b2 = 0; b2 <= 3; b2++) {
      final byte fmode = b2;
      table9.button(Formations.modeName(b2), Styles.togglet, new Runnable() {
            public void run() {
              Formations.setMode(fmode);
            }
          }).checked(paramTextButton -> (Formations.mode() == paramInt)).height(36.0F).minWidth(54.0F).pad(2.0F);
    } 
    table9.button("...", new Runnable() {
          public void run() {
            Formations.openDialog();
          }
        }).width(46.0F).height(36.0F).padLeft(4.0F);
    this.panel.add((Element)table9).growX().left().pad(4.0F).row();
    table1 = new Table();
    try {
      UiText.checkSmall(table1, "Conveyor", LunariansSettings.enableAutoConnectConveyor, new Boolc() {
            public void get(boolean param1Boolean) {
              LunariansSettings.enableAutoConnectConveyor = param1Boolean;
              LunariansSettings.save();
            }
          }).left();
    } catch (Throwable throwable) {
      table1.check("Conveyor", LunariansSettings.enableAutoConnectConveyor, new Boolc() {
            public void get(boolean param1Boolean) {
              LunariansSettings.enableAutoConnectConveyor = param1Boolean;
              LunariansSettings.save();
            }
          }).left();
    } 
    table1.button("Set", new Runnable() {
          public void run() {
            PositionPicker.pick(new Cons<int[]>() {
                  public void get(int[] param2ArrayOfint) {
                    LunariansSettings.conveyorStartX = param2ArrayOfint[0];
                    LunariansSettings.conveyorStartY = param2ArrayOfint[1];
                    LunariansSettings.save();
                    if (Vars.ui != null)
                      Vars.ui.showInfoToast("pt".equals(LunariansSettings.language) ? ("Início: " + param2ArrayOfint[0] + "," + param2ArrayOfint[1]) : ("Start: " + param2ArrayOfint[0] + "," + param2ArrayOfint[1]), 2.0F); 
                  }
                }new Runnable() {
                  public void run() {}
                });
          }
        }).width(52.0F).height(40.0F).padLeft(4.0F);
    table1.button("Gen", new Runnable() {
          public void run() {
            AutoConnectConveyor.connect();
          }
        }).width(52.0F).height(40.0F).padLeft(2.0F);
    this.panel.add((Element)table1).growX().left().pad(4.0F).row();
    this.panel.label(new Prov<CharSequence>() {
          public CharSequence get() {
            if (LunariansSettings.conveyorStartX == Integer.MIN_VALUE)
              return "pt".equals(LunariansSettings.language) ? "  início: —" : "  start: —"; 
            return "  " + LunariansSettings.conveyorStartX + "," + LunariansSettings.conveyorStartY;
          }
        }).left().padLeft(10.0F).row();
    UiStyle.section(this.panel, t("Alerts and camera", "Alertas e câmera"));
    addCheck(safeI18n("enemy_core_alert", "Enemy core alert"), LunariansSettings.enableEnemyCoreAlert, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableEnemyCoreAlert = param1Boolean;
            LunariansSettings.save();
          }
        });
    addCheck(safeI18n("autofill", "Auto fill"), LunariansSettings.enableAutoFill, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoFill = param1Boolean;
            LunariansSettings.save();
          }
        });
    this.panel.button(safeI18n("autofill_config", "Auto fill config"), new Runnable() {
          public void run() {
            try {
              (new AutoFillConfigDialog()).show();
            } catch (Throwable throwable) {
              Log.err("[Lunarians] AutoFillConfig", throwable);
            } 
          }
        }).height(36.0F).growX().pad(4.0F).row();
    addCheck(safeI18n("freecam", "Free cam"), LunariansSettings.enableFreeCam, new Boolc() {
          public void get(boolean param1Boolean) {
            try {
              FreeCam.setEnabled(param1Boolean);
            } catch (Throwable throwable) {
              LunariansSettings.enableFreeCam = param1Boolean;
              LunariansSettings.save();
            } 
          }
        });
    addCheck(safeI18n("prod_alert", "Production alert"), LunariansSettings.enableProductionAlert, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableProductionAlert = param1Boolean;
            LunariansSettings.save();
            if (param1Boolean)
              try {
                ProductionAlert.reset();
              } catch (Throwable throwable) {} 
          }
        });
    addCheck(safeI18n("camfollow", "Cam follow"), LunariansSettings.enableCamFollow, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableCamFollow = param1Boolean;
            LunariansSettings.save();
          }
        });
    addCheck(safeI18n("loss", "Resource loss"), LunariansSettings.enableLossWarn, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableLossWarn = param1Boolean;
            LunariansSettings.save();
          }
        });
    addCheckCount(safeI18n("autounit", "Auto unit"), LunariansSettings.enableAutoUnit, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoUnit = param1Boolean;
            AutoUnit.reset();
            LunariansSettings.save();
          }
        },  new Prov<CharSequence>() {
          public CharSequence get() {
            return LunariansSettings.enableAutoUnit ? ("[green]●[] " + AutoUnit.count()) : "";
          }
        });
    addCheckCount("Auto unit heal", LunariansSettings.enableAutoUnitHeal, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoUnitHeal = param1Boolean;
            AutoUnitHeal.reset();
            LunariansSettings.save();
          }
        },  new Prov<CharSequence>() {
          public CharSequence get() {
            return LunariansSettings.enableAutoUnitHeal ? ("[green]●[] " + AutoUnitHeal.count()) : "";
          }
        });
    addCheckCount("Auto Moon", LunariansSettings.enableAutoMoon, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoMoon = param1Boolean;
            AutoMoon.reset();
            LunariansSettings.save();
          }
        },  new Prov<CharSequence>() {
          public CharSequence get() {
            return LunariansSettings.enableAutoMoon ? ("[green]●[] " + AutoMoon.count()) : "";
          }
        });
    addCheckCount(safeI18n("autocargo", "Auto cargo"), LunariansSettings.enableAutoCargo, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoCargo = param1Boolean;
            AutoCargo.reset();
            LunariansSettings.save();
          }
        },  new Prov<CharSequence>() {
          public CharSequence get() {
            return LunariansSettings.enableAutoCargo ? ("[green]●[] " + AutoCargo.count()) : "";
          }
        });
    addCheckCount("Auto container", LunariansSettings.enableAutoContainer, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoContainer = param1Boolean;
            LunariansSettings.save();
          }
        },  new Prov<CharSequence>() {
          public CharSequence get() {
            return LunariansSettings.enableAutoContainer ? "[orange]●[]" : "";
          }
        });
    addCheckCount("Auto defesa", LunariansSettings.enableAutoDefence, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoDefence = param1Boolean;
            AutoDefence.reset();
            LunariansSettings.save();
          }
        },  new Prov<CharSequence>() {
          public CharSequence get() {
            return LunariansSettings.enableAutoDefence ? ("[green]●[] " + AutoDefence.count()) : "";
          }
        });
    addCheckCount("Auto shield", LunariansSettings.enableAutoShield, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoShield = param1Boolean;
            AutoShield.reset();
            LunariansSettings.save();
          }
        },  new Prov<CharSequence>() {
          public CharSequence get() {
            return LunariansSettings.enableAutoShield ? ("[green]●[] " + AutoShield.count()) : "";
          }
        });
    addCheckCount("Auto heal", LunariansSettings.enableAutoHeal, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoHeal = param1Boolean;
            AutoHeal.reset();
            LunariansSettings.save();
          }
        },  new Prov<CharSequence>() {
          public CharSequence get() {
            return LunariansSettings.enableAutoHeal ? ("[green]●[] " + AutoHeal.count()) : "";
          }
        });
    UiStyle.section(this.panel, t("Combat and interface", "Combate e interface"));
    addCheckCount("Auto attack", LunariansSettings.enableAutoAttack, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.enableAutoAttack = param1Boolean;
            AutoAttack.reset();
            LunariansSettings.save();
          }
        },  new Prov<CharSequence>() {
          public CharSequence get() {
            return LunariansSettings.enableAutoAttack ? ("[green]●[] " + AutoAttack.count()) : "";
          }
        });
    addCheck(safeI18n("schem_ui", "Schematic panel"), LunariansSettings.showSchematicPanel, new Boolc() {
          public void get(boolean param1Boolean) {
            LunariansSettings.showSchematicPanel = param1Boolean;
            LunariansSettings.save();
          }
        });
    UiStyle.decorate((Element)this.panel);
  }
  
  private static String t(String paramString1, String paramString2) {
    return "pt".equals(LunariansSettings.language) ? paramString2 : paramString1;
  }
  
  private void addCheck(String paramString, boolean paramBoolean, Boolc paramBoolc) {
    try {
      UiText.checkSmall(this.panel, paramString, paramBoolean, paramBoolc).left().pad(4.0F).row();
    } catch (Throwable throwable) {
      this.panel.check(paramString, paramBoolean, paramBoolc).left().pad(4.0F).row();
    } 
  }
  
  private void addCheckCount(String paramString, boolean paramBoolean, Boolc paramBoolc, Prov<CharSequence> paramProv) {
    addCheck(paramString, paramBoolean, paramBoolc);
    try {
      this.panel.label(paramProv).left().padLeft(10.0F).row();
    } catch (Throwable throwable) {}
  }
  
  private static String safeI18n(String paramString1, String paramString2) {
    try {
      String str = LunariansI18n.get(paramString1);
      if (str != null && !str.isEmpty() && !str.equals(paramString1))
        return str; 
    } catch (Throwable throwable) {}
    return paramString2;
  }
}
