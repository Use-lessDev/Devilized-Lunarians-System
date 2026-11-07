package lunarians.ui;

import arc.func.Boolf;
import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import lunarians.AutoContainer;
import lunarians.LunariansSettings;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoContainerUi.class */
public class AutoContainerUi {
    private final Table box = new Table();

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Cons {
        public final void get(Object obj) {
            AutoContainerUi.lambda$rebuild$8((IntSeq) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoContainerUi.$r8$lambda$cJSxvMp47QObKo3UkQGtEW20i-k();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$10, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass10 implements Prov {
        public final Object get() {
            return AutoContainerUi.lambda$rebuild$14();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$11, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass11 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoContainerUi.lambda$rebuild$15();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$12, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass12 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoContainerUi.lambda$rebuild$16();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$13, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass13 implements Runnable {
        public /* synthetic */ AnonymousClass13() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            AutoContainerUi.this.lambda$rebuild$4$lunarians-ui-AutoContainerUi();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$14, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass14 implements Prov {
        public final Object get() {
            return AutoContainerUi.lambda$rebuild$5();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$15, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass15 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoContainerUi.lambda$rebuild$6();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$16, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass16 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoContainerUi.lambda$rebuild$7();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$17, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass17 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoContainerUi.lambda$rebuild$9();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$18, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass18 implements Runnable {
        public /* synthetic */ AnonymousClass18() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            AutoContainerUi.this.lambda$rebuild$10$lunarians-ui-AutoContainerUi();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$19, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass19 implements Prov {
        public final Object get() {
            return AutoContainerUi.lambda$rebuild$11();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Runnable {
        public final /* synthetic */ Runnable f$1;

        @Override // java.lang.Runnable
        public final void run() {
            AutoContainerUi.this.lambda$stepper$17$lunarians-ui-AutoContainerUi(this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$20, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass20 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoContainerUi.lambda$rebuild$12();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$21, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass21 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoContainerUi.lambda$rebuild$13();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements Runnable {
        public final /* synthetic */ Runnable f$1;

        @Override // java.lang.Runnable
        public final void run() {
            AutoContainerUi.this.lambda$stepper$18$lunarians-ui-AutoContainerUi(this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass4 implements Cons {
        public final void get(Object obj) {
            AutoContainerUi.lambda$make$0((Table) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$5, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass5 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoContainer.reset();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$6, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass6 implements Cons {
        public /* synthetic */ AnonymousClass6() {
        }

        public final void get(Object obj) {
            AutoContainerUi.this.lambda$rebuild$2$lunarians-ui-AutoContainerUi((UnitType) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$7, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass7 implements Boolf {
        public final boolean get(Object obj) {
            return AutoContainerUi.$r8$lambda$coaAd8g8dh9dwICRjJsAh8Q6Rss((UnitType) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$8, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass8 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoContainerUi.lambda$reopen$1();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.ui.AutoContainerUi$9, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass9 implements Runnable {
        public /* synthetic */ AnonymousClass9() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            AutoContainerUi.this.lambda$rebuild$3$lunarians-ui-AutoContainerUi();
        }
    }

    public static BuilderDialog make() {
        return new BuilderDialog(title(), table -> {
            new AutoContainerUi(table);
        }, AutoContainer::reset);
    }

    private static void reopen() {
        make().after(() -> {
            new ConfigDialog().show();
        }).show();
    }

    public static String tr(String str, String str2, String str3, String str4) {
        String str5 = LunariansSettings.language;
        return "pt".equals(str5) ? str2 : "es".equals(str5) ? str3 : "ru".equals(str5) ? str4 : str;
    }

    public static String title() {
        return tr("Auto container (Beta)", "Auto container (Beta)", "Auto contenedor (Beta)", "Авто-контейнер (Бета)");
    }

    private static boolean canCarryBlocks(UnitType unitType) {
        return unitType.payloadCapacity > 0.0f && unitType.commands != null && unitType.commands.contains(UnitCommand.loadBlocksCommand) && unitType.commands.contains(UnitCommand.unloadPayloadCommand);
    }

    public AutoContainerUi(Table table) {
        table.defaults().growX();
        table.add(tr("The carrier unit picks up the container at point A, places it at point B, waits, picks it up again, returns it to A and waits again. Then it repeats. Point A must already have the container. Needs the unit in command mode.", "A unidade de carga pega o container no ponto A, coloca no ponto B, espera, pega de novo, devolve em A e espera mais um pouco. Depois repete. O ponto A precisa já ter o container. Precisa da unidade em modo comando.", "La unidad de carga recoge el contenedor en el punto A, lo coloca en el punto B, espera, lo recoge de nuevo, lo devuelve a A y espera otra vez. Luego repite. El punto A ya debe tener el contenedor. Requiere la unidad en modo comando.", "Юнит берёт контейнер в точке A, ставит в точке B, ждёт, берёт снова, возвращает в A и снова ждёт. Затем повтор. В точке A уже должен стоять контейнер. Нужен режим командования.")).wrap().growX().color(Color.lightGray).padBottom(6.0f).row();
        table.add(this.box).growX().row();
        rebuild();
    }

    private void rebuild() {
        String str;
        this.box.clearChildren();
        this.box.defaults().growX();
        UnitType unit = LunariansSettings.containerUnit.isEmpty() ? null : Vars.content.unit(LunariansSettings.containerUnit);
        Table table = new Table(Styles.black6);
        table.defaults().pad(4.0f);
        ImageButton imageButton = new ImageButton(unit != null ? new TextureRegionDrawable(unit.uiIcon) : Icon.add, Styles.defaulti);
        imageButton.clicked(() -> {
            new UnitPickerDialog(unitType -> {
                LunariansSettings.containerUnit = unitType.name;
                LunariansSettings.save();
                AutoContainer.reset();
                rebuild();
            }, AutoContainerUi::canCarryBlocks).show();
        });
        table.add(imageButton).size(40.0f).padRight(6.0f);
        table.add(unit != null ? unit.localizedName : tr("Choose carrier unit", "Escolher unidade de carga", "Elegir unidad de carga", "Выбрать юнит")).wrap().growX().left();
        table.button(Icon.trash, Styles.defaulti, () -> {
            LunariansSettings.containerUnit = "";
            LunariansSettings.save();
            AutoContainer.reset();
            rebuild();
        }).size(36.0f);
        this.box.add(table).growX().padBottom(4.0f).row();
        stepper(tr("Max units:", "Máx unidades:", "Máx unidades:", "Макс. юнитов:"), () -> {
            return LunariansSettings.containerMaxUnits <= 0 ? tr("All", "Todas", "Todas", "Все") : String.valueOf(LunariansSettings.containerMaxUnits);
        }, () -> {
            if (LunariansSettings.containerMaxUnits > 0) {
                LunariansSettings.containerMaxUnits--;
            }
        }, () -> {
            if (LunariansSettings.containerMaxUnits < 10) {
                LunariansSettings.containerMaxUnits++;
            }
        });
        boolean hasPoints = AutoContainer.hasPoints();
        if (!hasPoints) {
            str = tr("Points not set. Press \"Map points\": 1st = where the container is (A), 2nd = where it will stay (B).", "Pontos não definidos. Aperte \"Pontos mapa\": 1º = onde está o container (A), 2º = onde ele vai ficar (B).", "Puntos sin definir. Pulsa \"Puntos mapa\": 1.º = donde está el contenedor (A), 2.º = donde se quedará (B).", "Точки не заданы. Нажмите «Точки»: 1-я = где контейнер (A), 2-я = где он останется (B).");
        } else {
            str = "A (" + LunariansSettings.containerPickX + "," + LunariansSettings.containerPickY + ")  →  B (" + LunariansSettings.containerDropX + "," + LunariansSettings.containerDropY + ")";
        }
        this.box.add(str).wrap().growX().left().color(hasPoints ? Color.white : Color.lightGray).padTop(4.0f).row();
        Table table2 = new Table();
        table2.button(tr("Map points", "Pontos mapa", "Puntos mapa", "Точки"), () -> {
            FeatureDialog featureDialog = FeatureDialog.top;
            if (featureDialog != null) {
                featureDialog.after(null);
                featureDialog.hide();
            }
            PathPicker.pick(intSeq -> {
                if (intSeq.size >= 4) {
                    AutoContainer.setPoints(intSeq.get(0), intSeq.get(1), intSeq.get(intSeq.size - 2), intSeq.get(intSeq.size - 1));
                }
                reopen();
            }, AutoContainerUi::reopen);
        }).width(130.0f).height(36.0f);
        if (hasPoints) {
            table2.button(Icon.trash, Styles.defaulti, () -> {
                AutoContainer.clearPoints();
                rebuild();
            }).size(36.0f).padLeft(4.0f);
        }
        this.box.add(table2).left().padTop(2.0f).padBottom(4.0f).row();
        stepper(tr("Wait after placing (s):", "Espera após colocar (s):", "Espera tras colocar (s):", "Ожидание после установки (с):"), () -> {
            return fmt(AutoContainer.waitAfterPlace());
        }, () -> {
            LunariansSettings.containerWaitSec = Math.max(0.0f, LunariansSettings.containerWaitSec - 1.0f);
        }, () -> {
            LunariansSettings.containerWaitSec = Math.min(300.0f, LunariansSettings.containerWaitSec + 1.0f);
        });
        stepper(tr("Wait after returning (s):", "Espera após devolver (s):", "Espera tras devolver (s):", "Ожидание после возврата (с):"), () -> {
            return fmt(AutoContainer.waitAfterReturn());
        }, () -> {
            AutoContainer.setWaitAfterReturn(Math.max(0.0f, AutoContainer.waitAfterReturn() - 1.0f));
        }, () -> {
            AutoContainer.setWaitAfterReturn(Math.min(300.0f, AutoContainer.waitAfterReturn() + 1.0f));
        });
    }

    private static String fmt(float f) {
        return f == ((float) ((int) f)) ? String.valueOf((int) f) : String.valueOf(f);
    }

    private void stepper(String str, Prov<String> prov, Runnable runnable, Runnable runnable2) {
        Table table = new Table();
        table.add(str).left().padRight(6.0f).wrap().width(190.0f).color(Pal.accent).fontScale(0.9f);
        table.button("-", () -> {
            runnable.run();
            LunariansSettings.save();
            rebuild();
        }).size(36.0f, 32.0f);
        table.add((CharSequence) prov.get()).width(50.0f).center().pad(2.0f);
        table.button("+", () -> {
            runnable2.run();
            LunariansSettings.save();
            rebuild();
        }).size(36.0f, 32.0f);
        this.box.add(table).left().padTop(3.0f).row();
    }
}
