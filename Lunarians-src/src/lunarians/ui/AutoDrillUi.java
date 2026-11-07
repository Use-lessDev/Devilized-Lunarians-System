package lunarians.ui;

import arc.func.Boolc;
import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.AutoDrill;
import lunarians.AutoSetConfig;
import lunarians.LunariansSettings;
import mindustry.Vars;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;
import mindustry.world.blocks.production.Drill;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoDrillUi.class */
public class AutoDrillUi {

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoDrillUi$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Prov {
        public final Object get() {
            return AutoDrillUi.lambda$new$0();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoDrillUi$10, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass10 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoDrillUi.lambda$new$4();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoDrillUi$11, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass11 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoDrillUi.lambda$new$5();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoDrillUi$12, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass12 implements Prov {
        public final Object get() {
            return AutoDrillUi.lambda$new$6();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoDrillUi$13, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass13 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoDrillUi.lambda$new$7();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoDrillUi$14, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass14 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoDrillUi.lambda$new$8();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoDrillUi$15, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass15 implements Prov {
        public final Object get() {
            return AutoDrillUi.lambda$new$9();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoDrillUi$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass4 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoDrillUi.lambda$new$10();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoDrillUi$5, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass5 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoDrillUi.lambda$new$11();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoDrillUi$6, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass6 implements Boolc {
        public final void get(boolean z) {
            AutoDrillUi.lambda$new$12(z);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoDrillUi$7, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass7 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoDrillUi.lambda$new$1();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoDrillUi$8, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass8 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoDrillUi.lambda$new$2();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoDrillUi$9, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass9 implements Prov {
        public final Object get() {
            return AutoDrillUi.lambda$new$3();
        }
    }

    public static BuilderDialog make() {
        return new BuilderDialog("Auto Drill", new Cons<Table>() { // from class: lunarians.ui.AutoDrillUi.1
            public void get(Table table) {
                new AutoDrillUi(table);
            }
        }, new Runnable() { // from class: lunarians.ui.AutoDrillUi.2
            @Override // java.lang.Runnable
            public void run() {
                AutoDrill.reset();
            }
        });
    }

    public AutoDrillUi(Table table) {
        table.defaults().growX();
        table.add(pt() ? "Pre-build drills no patch (estilo AutoDrill). Max tiles / min ores / qualidade / água+power." : "Pre-build drills on patch. Max tiles / min ores / quality / water+power.").wrap().growX().color(Color.lightGray).padBottom(6.0f).row();
        Table table2 = new Table();
        Block block = Vars.content.block(LunariansSettings.drillBlock);
        table2.add("Drill: " + (block != null ? block.localizedName : LunariansSettings.drillBlock)).growX().left();
        table2.button("...", new Runnable() { // from class: lunarians.ui.AutoDrillUi.3
            @Override // java.lang.Runnable
            public void run() {
                final BaseDialog baseDialog = new BaseDialog("Drill");
                Table table3 = new Table();
                Seq blocks = Vars.content.blocks();
                for (int i = 0; i < blocks.size; i++) {
                    final Block block2 = (Block) blocks.get(i);
                    if ((block2 instanceof Drill) && !block2.isHidden()) {
                        table3.button(block2.localizedName, new Runnable() { // from class: lunarians.ui.AutoDrillUi.3.1
                            @Override // java.lang.Runnable
                            public void run() {
                                LunariansSettings.drillBlock = block2.name;
                                LunariansSettings.save();
                                baseDialog.hide();
                                AutoDrillUi.make().show();
                            }
                        }).growX().height(40.0f).pad(2.0f).row();
                    }
                }
                baseDialog.cont.add(new ScrollPane(table3)).grow();
                baseDialog.addCloseButton();
                baseDialog.show();
            }
        }).size(40.0f);
        table.add(table2).growX().pad(4.0f).row();
        stepper(table, "Max tiles", () -> {
            return String.valueOf(LunariansSettings.drillMaxTiles);
        }, () -> {
            LunariansSettings.drillMaxTiles = Math.max(16, LunariansSettings.drillMaxTiles - 50);
            LunariansSettings.save();
        }, () -> {
            LunariansSettings.drillMaxTiles = Math.min(2000, LunariansSettings.drillMaxTiles + 50);
            LunariansSettings.save();
        });
        stepper(table, "Min ores", () -> {
            return String.valueOf(LunariansSettings.drillMinOres);
        }, () -> {
            LunariansSettings.drillMinOres = Math.max(1, LunariansSettings.drillMinOres - 1);
            LunariansSettings.save();
        }, () -> {
            LunariansSettings.drillMinOres = Math.min(16, LunariansSettings.drillMinOres + 1);
            LunariansSettings.save();
        });
        stepper(table, "Opt quality", () -> {
            return String.valueOf(LunariansSettings.drillOptQuality);
        }, () -> {
            LunariansSettings.drillOptQuality = Math.max(1, LunariansSettings.drillOptQuality - 1);
            LunariansSettings.save();
        }, () -> {
            LunariansSettings.drillOptQuality = Math.min(50, LunariansSettings.drillOptQuality + 1);
            LunariansSettings.save();
        });
        stepper(table, pt() ? "Distância entre campos (Auto Set)" : "Field spacing (Auto Set)", () -> {
            return String.valueOf(AutoSetConfig.spacing());
        }, () -> {
            AutoSetConfig.setSpacing(AutoSetConfig.spacing() - 1);
        }, () -> {
            AutoSetConfig.setSpacing(AutoSetConfig.spacing() + 1);
        });
        table.check(pt() ? "Água + power node" : "Water + power nodes", LunariansSettings.drillPlaceWaterPower, z -> {
            LunariansSettings.drillPlaceWaterPower = z;
            LunariansSettings.save();
        }).left().pad(4.0f).row();
        table.add("Ore tile: " + (LunariansSettings.drillStartX == Integer.MIN_VALUE ? "—" : LunariansSettings.drillStartX + "," + LunariansSettings.drillStartY)).left().pad(4.0f).row();
    }

    private void stepper(Table table, String str, Prov<CharSequence> prov, Runnable runnable, Runnable runnable2) {
        Table table2 = new Table();
        table2.add(str + ":").left().padRight(4.0f);
        table2.button("-", runnable).size(32.0f, 28.0f);
        table2.label(prov).width(48.0f).center();
        table2.button("+", runnable2).size(32.0f, 28.0f);
        table.add(table2).left().pad(2.0f).row();
    }

    private static boolean pt() {
        return "pt".equals(LunariansSettings.language);
    }
}
