package lunarians.ui;

import arc.func.Cons;
import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.AutoConnectConveyor;
import lunarians.LunariansSettings;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;

public class AutoConnectConveyorUi {
  public static BuilderDialog make() {
    return new BuilderDialog(title(), new Cons<Table>() {
          public void get(Table param1Table) {
            new AutoConnectConveyorUi(param1Table);
          }
        },  new Runnable() {
          public void run() {
            AutoConnectConveyor.reset();
          }
        });
  }
  
  public AutoConnectConveyorUi(Table paramTable) {
    paramTable.defaults().growX();
    paramTable.add(hint()).wrap().growX().color(Color.lightGray).padBottom(6.0F).row();
    Table table1 = new Table();
    table1.add(convL()).left().padRight(6.0F);
    Block block = Vars.content.block(LunariansSettings.conveyorBlock);
    String str1 = (block != null) ? block.localizedName : LunariansSettings.conveyorBlock;
    table1.add(str1).wrap().growX().left().color(Pal.accent);
    table1.button((Drawable)Icon.pencil, Styles.defaulti, new Runnable() {
          public void run() {
            AutoConnectConveyorUi.this.pickConveyor();
          }
        }).size(36.0F);
    paramTable.add((Element)table1).growX().pad(4.0F).row();
    Table table2 = new Table();
    String str2 = (LunariansSettings.conveyorStartX == Integer.MIN_VALUE) ? unset() : ("" + LunariansSettings.conveyorStartX + "," + LunariansSettings.conveyorStartX);
    table2.add(startL() + " " + startL()).wrap().growX().left();
    table2.button(mapBtn(), new Runnable() {
          public void run() {
            FeatureDialog featureDialog = FeatureDialog.top;
            if (featureDialog != null) {
              featureDialog.after((Runnable)null);
              featureDialog.hide();
            } 
            PositionPicker.pick(new Cons<int[]>() {
                  public void get(int[] param2ArrayOfint) {
                    LunariansSettings.conveyorStartX = param2ArrayOfint[0];
                    LunariansSettings.conveyorStartY = param2ArrayOfint[1];
                    LunariansSettings.save();
                    AutoConnectConveyorUi.make().after(new Runnable() {
                          public void run() {
                            (new ConfigDialog()).show();
                          }
                        },  ).show();
                  }
                },  new Runnable() {
                  public void run() {
                    AutoConnectConveyorUi.make().after(new Runnable() {
                          public void run() {
                            (new ConfigDialog()).show();
                          }
                        }).show();
                  }
                });
          }
        }).width(90.0F).height(32.0F);
    paramTable.add((Element)table2).growX().pad(4.0F).row();
    paramTable.button(runL(), new Runnable() {
          public void run() {
            int i = AutoConnectConveyor.connect();
          }
        }).height(44.0F).growX().pad(6.0F).row();
  }
  
  private void pickConveyor() {
    final BaseDialog d = new BaseDialog(convL());
    Table table = new Table();
    Seq seq = Vars.content.blocks();
    for (byte b = 0; b < seq.size; b++) {
      final Block b = (Block)seq.get(b);
      if (block instanceof mindustry.world.blocks.distribution.Conveyor && 
        !block.isHidden())
        table.button(block.localizedName, new Runnable() {
              public void run() {
                LunariansSettings.conveyorBlock = b.name;
                LunariansSettings.save();
                d.hide();
                AutoConnectConveyorUi.make().show();
              }
            }).growX().height(40.0F).pad(2.0F).row(); 
    } 
    baseDialog.cont.add((Element)new ScrollPane((Element)table)).grow();
    baseDialog.addCloseButton();
    baseDialog.show();
  }
  
  private static String title() {
    return "pt".equals(LunariansSettings.language) ? "Auto connect conveyor" : "Auto connect conveyor";
  }
  
  private static String hint() {
    if ("pt".equals(LunariansSettings.language))
      return "Pré-build (fantasma) até o core/vault. Não constrói sozinho. Escolha conveyor + início."; 
    return "Pre-build ghosts to core/vault. Does not auto-build. Pick conveyor + start.";
  }
  
  private static String convL() {
    return "pt".equals(LunariansSettings.language) ? "Conveyor:" : "Conveyor:";
  }
  
  private static String startL() {
    return "pt".equals(LunariansSettings.language) ? "Início:" : "Start:";
  }
  
  private static String mapBtn() {
    return "pt".equals(LunariansSettings.language) ? "Mapa" : "Map";
  }
  
  private static String runL() {
    return "pt".equals(LunariansSettings.language) ? "Gerar pré-build" : "Generate pre-build";
  }
  
  private static String unset() {
    return "pt".equals(LunariansSettings.language) ? "não definido" : "unset";
  }
}
