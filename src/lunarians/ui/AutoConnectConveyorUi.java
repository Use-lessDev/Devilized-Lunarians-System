/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.func.Cons
 *  arc.graphics.Color
 *  arc.scene.Element
 *  arc.scene.style.Drawable
 *  arc.scene.ui.ScrollPane
 *  arc.scene.ui.layout.Table
 *  arc.struct.Seq
 *  mindustry.Vars
 *  mindustry.gen.Icon
 *  mindustry.graphics.Pal
 *  mindustry.ui.Styles
 *  mindustry.ui.dialogs.BaseDialog
 *  mindustry.world.Block
 *  mindustry.world.blocks.distribution.Conveyor
 */
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
import lunarians.ui.BuilderDialog;
import lunarians.ui.ConfigDialog;
import lunarians.ui.FeatureDialog;
import lunarians.ui.PositionPicker;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;
import mindustry.world.blocks.distribution.Conveyor;

public class AutoConnectConveyorUi {
    public static BuilderDialog make() {
        return new BuilderDialog(AutoConnectConveyorUi.title(), new Cons<Table>(){

            public void get(Table table) {
                new AutoConnectConveyorUi(table);
            }
        }, new Runnable(){

            @Override
            public void run() {
                AutoConnectConveyor.reset();
            }
        });
    }

    public AutoConnectConveyorUi(Table table) {
        table.defaults().growX();
        table.add((CharSequence)AutoConnectConveyorUi.hint()).wrap().growX().color(Color.lightGray).padBottom(6.0f).row();
        Table table2 = new Table();
        table2.add((CharSequence)AutoConnectConveyorUi.convL()).left().padRight(6.0f);
        Block block = Vars.content.block(LunariansSettings.conveyorBlock);
        String string = block != null ? block.localizedName : LunariansSettings.conveyorBlock;
        table2.add((CharSequence)string).wrap().growX().left().color(Pal.accent);
        table2.button((Drawable)Icon.pencil, Styles.defaulti, new Runnable(){

            @Override
            public void run() {
                AutoConnectConveyorUi.this.pickConveyor();
            }
        }).size(36.0f);
        table.add((Element)table2).growX().pad(4.0f).row();
        Table table3 = new Table();
        String string2 = LunariansSettings.conveyorStartX == Integer.MIN_VALUE ? AutoConnectConveyorUi.unset() : LunariansSettings.conveyorStartX + "," + LunariansSettings.conveyorStartY;
        table3.add((CharSequence)(AutoConnectConveyorUi.startL() + " " + string2)).wrap().growX().left();
        table3.button(AutoConnectConveyorUi.mapBtn(), new Runnable(){

            @Override
            public void run() {
                FeatureDialog featureDialog = FeatureDialog.top;
                if (featureDialog != null) {
                    featureDialog.after(null);
                    featureDialog.hide();
                }
                PositionPicker.pick(new Cons<int[]>(){

                    public void get(int[] nArray) {
                        LunariansSettings.conveyorStartX = nArray[0];
                        LunariansSettings.conveyorStartY = nArray[1];
                        LunariansSettings.save();
                        AutoConnectConveyorUi.make().after(new Runnable(){

                            @Override
                            public void run() {
                                new ConfigDialog().show();
                            }
                        }).show();
                    }
                }, new Runnable(){

                    @Override
                    public void run() {
                        AutoConnectConveyorUi.make().after(new Runnable(){

                            @Override
                            public void run() {
                                new ConfigDialog().show();
                            }
                        }).show();
                    }
                });
            }
        }).width(90.0f).height(32.0f);
        table.add((Element)table3).growX().pad(4.0f).row();
        table.button(AutoConnectConveyorUi.runL(), new Runnable(){

            @Override
            public void run() {
                int n = AutoConnectConveyor.connect();
            }
        }).height(44.0f).growX().pad(6.0f).row();
    }

    private void pickConveyor() {
        final BaseDialog baseDialog = new BaseDialog(AutoConnectConveyorUi.convL());
        Table table = new Table();
        Seq seq = Vars.content.blocks();
        for (int i = 0; i < seq.size; ++i) {
            final Block block = (Block)seq.get(i);
            if (!(block instanceof Conveyor) || block.isHidden()) continue;
            table.button(block.localizedName, new Runnable(){

                @Override
                public void run() {
                    LunariansSettings.conveyorBlock = block.name;
                    LunariansSettings.save();
                    baseDialog.hide();
                    AutoConnectConveyorUi.make().show();
                }
            }).growX().height(40.0f).pad(2.0f).row();
        }
        baseDialog.cont.add((Element)new ScrollPane((Element)table)).grow();
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    private static String title() {
        return "pt".equals(LunariansSettings.language) ? "Auto connect conveyor" : "Auto connect conveyor";
    }

    private static String hint() {
        if ("pt".equals(LunariansSettings.language)) {
            return "Pr\u00e9-build (fantasma) at\u00e9 o core/vault. N\u00e3o constr\u00f3i sozinho. Escolha conveyor + in\u00edcio.";
        }
        return "Pre-build ghosts to core/vault. Does not auto-build. Pick conveyor + start.";
    }

    private static String convL() {
        return "pt".equals(LunariansSettings.language) ? "Conveyor:" : "Conveyor:";
    }

    private static String startL() {
        return "pt".equals(LunariansSettings.language) ? "In\u00edcio:" : "Start:";
    }

    private static String mapBtn() {
        return "pt".equals(LunariansSettings.language) ? "Mapa" : "Map";
    }

    private static String runL() {
        return "pt".equals(LunariansSettings.language) ? "Gerar pr\u00e9-build" : "Generate pre-build";
    }

    private static String unset() {
        return "pt".equals(LunariansSettings.language) ? "n\u00e3o definido" : "unset";
    }
}

