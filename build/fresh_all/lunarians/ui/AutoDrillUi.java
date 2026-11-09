/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.func.Cons
 *  arc.func.Prov
 *  arc.graphics.Color
 *  arc.scene.Element
 *  arc.scene.ui.ScrollPane
 *  arc.scene.ui.layout.Table
 *  arc.struct.Seq
 *  mindustry.Vars
 *  mindustry.ui.dialogs.BaseDialog
 *  mindustry.world.Block
 *  mindustry.world.blocks.production.Drill
 */
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
import lunarians.ui.BuilderDialog;
import mindustry.Vars;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;
import mindustry.world.blocks.production.Drill;

public class AutoDrillUi {
    public static BuilderDialog make() {
        return new BuilderDialog("Auto Drill", new Cons<Table>(){

            public void get(Table table) {
                new AutoDrillUi(table);
            }
        }, new Runnable(){

            @Override
            public void run() {
                AutoDrill.reset();
            }
        });
    }

    public AutoDrillUi(Table table) {
        table.defaults().growX();
        table.add((CharSequence)(AutoDrillUi.pt() ? "Pre-build drills no patch (estilo AutoDrill). Max tiles / min ores / qualidade / \u00e1gua+power." : "Pre-build drills on patch. Max tiles / min ores / quality / water+power.")).wrap().growX().color(Color.lightGray).padBottom(6.0f).row();
        Table table2 = new Table();
        Block block = Vars.content.block(LunariansSettings.drillBlock);
        table2.add((CharSequence)("Drill: " + (block != null ? block.localizedName : LunariansSettings.drillBlock))).growX().left();
        table2.button("...", new Runnable(){

            @Override
            public void run() {
                final BaseDialog baseDialog = new BaseDialog("Drill");
                Table table = new Table();
                Seq seq = Vars.content.blocks();
                for (int i = 0; i < seq.size; ++i) {
                    final Block block = (Block)seq.get(i);
                    if (!(block instanceof Drill) || block.isHidden()) continue;
                    table.button(block.localizedName, new Runnable(){

                        @Override
                        public void run() {
                            LunariansSettings.drillBlock = block.name;
                            LunariansSettings.save();
                            baseDialog.hide();
                            AutoDrillUi.make().show();
                        }
                    }).growX().height(40.0f).pad(2.0f).row();
                }
                baseDialog.cont.add((Element)new ScrollPane((Element)table)).grow();
                baseDialog.addCloseButton();
                baseDialog.show();
            }
        }).size(40.0f);
        table.add((Element)table2).growX().pad(4.0f).row();
        this.stepper(table, "Max tiles", (Prov<CharSequence>)((Prov)() -> String.valueOf(LunariansSettings.drillMaxTiles)), () -> {
            LunariansSettings.drillMaxTiles = Math.max(16, LunariansSettings.drillMaxTiles - 50);
            LunariansSettings.save();
        }, () -> {
            LunariansSettings.drillMaxTiles = Math.min(2000, LunariansSettings.drillMaxTiles + 50);
            LunariansSettings.save();
        });
        this.stepper(table, "Min ores", (Prov<CharSequence>)((Prov)() -> String.valueOf(LunariansSettings.drillMinOres)), () -> {
            LunariansSettings.drillMinOres = Math.max(1, LunariansSettings.drillMinOres - 1);
            LunariansSettings.save();
        }, () -> {
            LunariansSettings.drillMinOres = Math.min(16, LunariansSettings.drillMinOres + 1);
            LunariansSettings.save();
        });
        this.stepper(table, "Opt quality", (Prov<CharSequence>)((Prov)() -> String.valueOf(LunariansSettings.drillOptQuality)), () -> {
            LunariansSettings.drillOptQuality = Math.max(1, LunariansSettings.drillOptQuality - 1);
            LunariansSettings.save();
        }, () -> {
            LunariansSettings.drillOptQuality = Math.min(50, LunariansSettings.drillOptQuality + 1);
            LunariansSettings.save();
        });
        this.stepper(table, AutoDrillUi.pt() ? "Dist\u00e2ncia entre campos (Auto Set)" : "Field spacing (Auto Set)", (Prov<CharSequence>)((Prov)() -> String.valueOf(AutoSetConfig.spacing())), () -> AutoSetConfig.setSpacing(AutoSetConfig.spacing() - 1), () -> AutoSetConfig.setSpacing(AutoSetConfig.spacing() + 1));
        table.check(AutoDrillUi.pt() ? "\u00c1gua + power node" : "Water + power nodes", LunariansSettings.drillPlaceWaterPower, bl -> {
            LunariansSettings.drillPlaceWaterPower = bl;
            LunariansSettings.save();
        }).left().pad(4.0f).row();
        String string = LunariansSettings.drillStartX == Integer.MIN_VALUE ? "\u2014" : LunariansSettings.drillStartX + "," + LunariansSettings.drillStartY;
        table.add((CharSequence)("Ore tile: " + string)).left().pad(4.0f).row();
    }

    private void stepper(Table table, String string, Prov<CharSequence> prov, Runnable runnable, Runnable runnable2) {
        Table table2 = new Table();
        table2.add((CharSequence)(string + ":")).left().padRight(4.0f);
        table2.button("-", runnable).size(32.0f, 28.0f);
        table2.label(prov).width(48.0f).center();
        table2.button("+", runnable2).size(32.0f, 28.0f);
        table.add((Element)table2).left().pad(2.0f).row();
    }

    private static boolean pt() {
        return "pt".equals(LunariansSettings.language);
    }
}

