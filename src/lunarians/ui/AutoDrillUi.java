package lunarians.ui;

import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.AutoDrill;
import lunarians.AutoSetConfig;
import lunarians.LunariansSettings;
import mindustry.Vars;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;

public class AutoDrillUi {
  public static BuilderDialog make() {
    return new BuilderDialog("Auto Drill", new Cons<Table>() {
          public void get(Table param1Table) {
            new AutoDrillUi(param1Table);
          }
        },  new Runnable() {
          public void run() {
            AutoDrill.reset();
          }
        });
  }
  
  public AutoDrillUi(Table paramTable) {
    paramTable.defaults().growX();
    paramTable.add(pt() ? "Pre-build drills no patch (estilo AutoDrill). Max tiles / min ores / qualidade / água+power." : "Pre-build drills on patch. Max tiles / min ores / quality / water+power.").wrap().growX().color(Color.lightGray).padBottom(6.0F).row();
    Table table = new Table();
    Block block = Vars.content.block(LunariansSettings.drillBlock);
    table.add("Drill: " + ((block != null) ? block.localizedName : LunariansSettings.drillBlock)).growX().left();
    table.button("...", new Runnable() {
          public void run() {
            final BaseDialog baseDialog = new BaseDialog("Drill");
            Table table = new Table();
            Seq seq = Vars.content.blocks();
            for (byte b = 0; b < seq.size; b++) {
              final Block block = (Block)seq.get(b);
              if (block instanceof mindustry.world.blocks.production.Drill && !block.isHidden())
                table.button(block.localizedName, new Runnable() {
                      public void run() {
                        LunariansSettings.drillBlock = block.name;
                        LunariansSettings.save();
                        baseDialog.hide();
                        AutoDrillUi.make().show();
                      }
                    }).growX().height(40.0F).pad(2.0F).row(); 
            } 
            baseDialog.cont.add((Element)new ScrollPane((Element)table)).grow();
            baseDialog.addCloseButton();
            baseDialog.show();
          }
        }).size(40.0F);
    paramTable.add((Element)table).growX().pad(4.0F).row();
    stepper(paramTable, "Max tiles", () -> String.valueOf(LunariansSettings.drillMaxTiles), () -> {
          LunariansSettings.drillMaxTiles = Math.max(16, LunariansSettings.drillMaxTiles - 50);
          LunariansSettings.save();
        }() -> {
          LunariansSettings.drillMaxTiles = Math.min(2000, LunariansSettings.drillMaxTiles + 50);
          LunariansSettings.save();
        });
    stepper(paramTable, "Min ores", () -> String.valueOf(LunariansSettings.drillMinOres), () -> {
          LunariansSettings.drillMinOres = Math.max(1, LunariansSettings.drillMinOres - 1);
          LunariansSettings.save();
        }() -> {
          LunariansSettings.drillMinOres = Math.min(16, LunariansSettings.drillMinOres + 1);
          LunariansSettings.save();
        });
    stepper(paramTable, "Opt quality", () -> String.valueOf(LunariansSettings.drillOptQuality), () -> {
          LunariansSettings.drillOptQuality = Math.max(1, LunariansSettings.drillOptQuality - 1);
          LunariansSettings.save();
        }() -> {
          LunariansSettings.drillOptQuality = Math.min(50, LunariansSettings.drillOptQuality + 1);
          LunariansSettings.save();
        });
    stepper(paramTable, pt() ? "Distância entre campos (Auto Set)" : "Field spacing (Auto Set)", () -> String.valueOf(AutoSetConfig.spacing()), () -> AutoSetConfig.setSpacing(AutoSetConfig.spacing() - 1), () -> AutoSetConfig.setSpacing(AutoSetConfig.spacing() + 1));
    paramTable.check(pt() ? "Água + power node" : "Water + power nodes", LunariansSettings.drillPlaceWaterPower, paramBoolean -> {
          LunariansSettings.drillPlaceWaterPower = paramBoolean;
          LunariansSettings.save();
        }).left().pad(4.0F).row();
    String str = (LunariansSettings.drillStartX == Integer.MIN_VALUE) ? "—" : (LunariansSettings.drillStartX + "," + LunariansSettings.drillStartY);
    paramTable.add("Ore tile: " + str).left().pad(4.0F).row();
  }
  
  private void stepper(Table paramTable, String paramString, Prov<CharSequence> paramProv, Runnable paramRunnable1, Runnable paramRunnable2) {
    Table table = new Table();
    table.add(paramString + ":").left().padRight(4.0F);
    table.button("-", paramRunnable1).size(32.0F, 28.0F);
    table.label(paramProv).width(48.0F).center();
    table.button("+", paramRunnable2).size(32.0F, 28.0F);
    paramTable.add((Element)table).left().pad(2.0F).row();
  }
  
  private static boolean pt() {
    return "pt".equals(LunariansSettings.language);
  }
}
