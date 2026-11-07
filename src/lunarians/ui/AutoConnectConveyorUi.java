package lunarians.ui;

import arc.func.Cons;
import arc.graphics.Color;
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
import mindustry.world.blocks.distribution.Conveyor;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoConnectConveyorUi.class */
public class AutoConnectConveyorUi {
    public static BuilderDialog make() {
        return new BuilderDialog(title(), new Cons<Table>() { // from class: lunarians.ui.AutoConnectConveyorUi.1
            public void get(Table table) {
                new AutoConnectConveyorUi(table);
            }
        }, new Runnable() { // from class: lunarians.ui.AutoConnectConveyorUi.2
            @Override // java.lang.Runnable
            public void run() {
                AutoConnectConveyor.reset();
            }
        });
    }

    public AutoConnectConveyorUi(Table table) {
        String str;
        table.defaults().growX();
        table.add(hint()).wrap().growX().color(Color.lightGray).padBottom(6.0f).row();
        Table table2 = new Table();
        table2.add(convL()).left().padRight(6.0f);
        Block block = Vars.content.block(LunariansSettings.conveyorBlock);
        table2.add(block != null ? block.localizedName : LunariansSettings.conveyorBlock).wrap().growX().left().color(Pal.accent);
        table2.button(Icon.pencil, Styles.defaulti, new Runnable() { // from class: lunarians.ui.AutoConnectConveyorUi.3
            @Override // java.lang.Runnable
            public void run() {
                AutoConnectConveyorUi.this.pickConveyor();
            }
        }).size(36.0f);
        table.add(table2).growX().pad(4.0f).row();
        Table table3 = new Table();
        if (LunariansSettings.conveyorStartX == Integer.MIN_VALUE) {
            str = unset();
        } else {
            str = LunariansSettings.conveyorStartX + "," + LunariansSettings.conveyorStartY;
        }
        table3.add(startL() + " " + str).wrap().growX().left();
        table3.button(mapBtn(), new Runnable() { // from class: lunarians.ui.AutoConnectConveyorUi.4
            @Override // java.lang.Runnable
            public void run() {
                FeatureDialog featureDialog = FeatureDialog.top;
                if (featureDialog != null) {
                    featureDialog.after(null);
                    featureDialog.hide();
                }
                PositionPicker.pick(new Cons<int[]>() { // from class: lunarians.ui.AutoConnectConveyorUi.4.1
                    public void get(int[] iArr) {
                        LunariansSettings.conveyorStartX = iArr[0];
                        LunariansSettings.conveyorStartY = iArr[1];
                        LunariansSettings.save();
                        AutoConnectConveyorUi.make().after(new Runnable() { // from class: lunarians.ui.AutoConnectConveyorUi.4.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                new ConfigDialog().show();
                            }
                        }).show();
                    }
                }, new Runnable() { // from class: lunarians.ui.AutoConnectConveyorUi.4.2
                    @Override // java.lang.Runnable
                    public void run() {
                        AutoConnectConveyorUi.make().after(new Runnable() { // from class: lunarians.ui.AutoConnectConveyorUi.4.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                new ConfigDialog().show();
                            }
                        }).show();
                    }
                });
            }
        }).width(90.0f).height(32.0f);
        table.add(table3).growX().pad(4.0f).row();
        table.button(runL(), new Runnable() { // from class: lunarians.ui.AutoConnectConveyorUi.5
            @Override // java.lang.Runnable
            public void run() {
                AutoConnectConveyor.connect();
            }
        }).height(44.0f).growX().pad(6.0f).row();
    }

    private void pickConveyor() {
        final BaseDialog baseDialog = new BaseDialog(convL());
        Table table = new Table();
        Seq blocks = Vars.content.blocks();
        for (int i = 0; i < blocks.size; i++) {
            final Block block = (Block) blocks.get(i);
            if ((block instanceof Conveyor) && !block.isHidden()) {
                table.button(block.localizedName, new Runnable() { // from class: lunarians.ui.AutoConnectConveyorUi.6
                    @Override // java.lang.Runnable
                    public void run() {
                        LunariansSettings.conveyorBlock = block.name;
                        LunariansSettings.save();
                        baseDialog.hide();
                        AutoConnectConveyorUi.make().show();
                    }
                }).growX().height(40.0f).pad(2.0f).row();
            }
        }
        baseDialog.cont.add(new ScrollPane(table)).grow();
        baseDialog.addCloseButton();
        baseDialog.show();
    }

    private static String title() {
        return "pt".equals(LunariansSettings.language) ? "Auto connect conveyor" : "Auto connect conveyor";
    }

    private static String hint() {
        if ("pt".equals(LunariansSettings.language)) {
            return "Pré-build (fantasma) até o core/vault. Não constrói sozinho. Escolha conveyor + início.";
        }
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
