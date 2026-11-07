package lunarians;

import arc.Core;
import arc.Events;
import arc.func.Boolc;
import arc.func.Cons;
import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import arc.util.Log;
import arc.util.Time;
import java.util.HashSet;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.ai.UnitStance;
import mindustry.ai.types.CommandAI;
import mindustry.content.Items;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.blocks.units.UnitFactory;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/MinerUnits.class */
public class MinerUnits {
    private static float next;
    private static boolean registered;
    private static boolean poor;
    public static final String[] TYPES = {"mono", "poly", "mega", "quasar"};
    private static final HashSet<Integer> mine = new HashSet<>();

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.MinerUnits$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Cons {
        public final /* synthetic */ int[] f$0;
        public final /* synthetic */ IntSeq f$1;
        public final /* synthetic */ IntSeq f$2;

        public /* synthetic */ AnonymousClass0(int[] iArr, IntSeq intSeq, IntSeq intSeq2) {
            this.f$0 = iArr;
            this.f$1 = intSeq;
            this.f$2 = intSeq2;
        }

        public final void get(Object obj) {
            MinerUnits.lambda$update$1(this.f$0, this.f$1, this.f$2, (Unit) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.MinerUnits$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Runnable {
        public final /* synthetic */ BaseDialog f$0;
        public final /* synthetic */ Runnable[] f$1;

        public /* synthetic */ AnonymousClass1(BaseDialog baseDialog, Runnable[] runnableArr) {
            this.f$0 = baseDialog;
            this.f$1 = runnableArr;
        }

        @Override // java.lang.Runnable
        public final void run() {
            MinerUnits.lambda$dialog$7(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.MinerUnits$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass2 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            LunariansSettings.save();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.MinerUnits$3, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass3 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            MinerUnits.$r8$lambda$8JDraylVJX_Cy8KT8fbDX-1Zt1w();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.MinerUnits$4, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass4 implements Cons {
        public final void get(Object obj) {
            MinerUnits.lambda$init$0((EventType.WorldLoadEvent) obj);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.MinerUnits$5, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass5 implements Boolc {
        public final void get(boolean z) {
            MinerUnits.lambda$dialog$2(z);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.MinerUnits$6, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass6 implements Boolc {
        public final /* synthetic */ int f$0;

        public final void get(boolean z) {
            MinerUnits.setTypeOn(this.f$0, z);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.MinerUnits$7, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass7 implements Boolc {
        public final void get(boolean z) {
            MinerUnits.setProduce(z);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.MinerUnits$8, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass8 implements Runnable {
        public final /* synthetic */ Runnable[] f$0;

        @Override // java.lang.Runnable
        public final void run() {
            MinerUnits.lambda$dialog$5(this.f$0);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.MinerUnits$9, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass9 implements Runnable {
        public final /* synthetic */ Runnable[] f$0;

        @Override // java.lang.Runnable
        public final void run() {
            MinerUnits.lambda$dialog$6(this.f$0);
        }
    }

    public static void init() {
        if (registered) {
            return;
        }
        registered = true;
        Events.run(EventType.Trigger.update, MinerUnits::update);
        Events.on(EventType.WorldLoadEvent.class, worldLoadEvent -> {
            mine.clear();
        });
    }

    private static String tr(String str, String str2, String str3, String str4) {
        String language = LunariansI18n.getLanguage();
        return "pt".equals(language) ? str : "es".equals(language) ? str3 : "ru".equals(language) ? str4 : str2;
    }

    public static String title() {
        return tr("Mineradores", "Miner units", "Unidades mineras", "Шахтёры");
    }

    public static boolean enabled() {
        return Core.settings.getBool("lunarians-mu-on", false);
    }

    public static void setEnabled(boolean z) {
        Core.settings.put("lunarians-mu-on", Boolean.valueOf(z));
    }

    public static boolean typeOn(int i) {
        return Core.settings.getBool("lunarians-mu-t-" + TYPES[i], true);
    }

    public static void setTypeOn(int i, boolean z) {
        Core.settings.put("lunarians-mu-t-" + TYPES[i], Boolean.valueOf(z));
    }

    public static boolean produce() {
        return Core.settings.getBool("lunarians-mu-prod", true);
    }

    public static void setProduce(boolean z) {
        Core.settings.put("lunarians-mu-prod", Boolean.valueOf(z));
    }

    public static int cap() {
        return Math.max(1, Math.min(40, Core.settings.getInt("lunarians-mu-cap", 8)));
    }

    public static void setCap(int i) {
        Core.settings.put("lunarians-mu-cap", Integer.valueOf(Math.max(1, Math.min(40, i))));
    }

    private static int typeIndex(UnitType unitType) {
        for (int i = 0; i < TYPES.length; i++) {
            if (unitType != null && TYPES[i].equals(unitType.name)) {
                return i;
            }
        }
        return -1;
    }

    private static void update() {
        if (!enabled() || Vars.state == null || !Vars.state.isGame() || Vars.player == null || Time.time < next) {
            return;
        }
        next = Time.time + 120.0f;
        try {
            if (Vars.player.closestCore() == null) {
                return;
            }
            IntSeq intSeq = new IntSeq();
            IntSeq intSeq2 = new IntSeq();
            int[] iArr = {0};
            Groups.unit.each(unit -> {
                int typeIndex;
                if (unit.team != Vars.player.team() || unit.isPlayer() || unit.dead || (typeIndex = typeIndex(unit.type)) < 0 || !typeOn(typeIndex)) {
                    return;
                }
                iArr[0] = iArr[0] + 1;
                CommandAI controller = unit.controller();
                if (controller instanceof CommandAI) {
                    CommandAI commandAI = controller;
                    if (commandAI.currentCommand() != UnitCommand.mineCommand) {
                        intSeq.add(unit.id);
                    } else if (!commandAI.hasStance(UnitStance.mineAuto)) {
                        intSeq2.add(unit.id);
                    }
                }
            });
            if (intSeq.size > 0) {
                Call.setUnitCommand(Vars.player, intSeq.toArray(), UnitCommand.mineCommand);
            }
            if (intSeq2.size > 0) {
                Call.setUnitStance(Vars.player, intSeq2.toArray(), UnitStance.mineAuto, true);
            }
            if (produce()) {
                factories(iArr[0]);
            }
        } catch (Throwable th) {
            Log.err("[Lunarians] MinerUnits", th);
        }
    }

    private static void factories(int i) {
        boolean z = typeOn(0) || typeOn(1) || typeOn(2);
        boolean typeOn = typeOn(3);
        int i2 = Vars.player.closestCore().items.get(Items.silicon);
        int i3 = Vars.player.closestCore().items.get(Items.lead);
        if (i2 < 30 || i3 < 20) {
            poor = true;
        } else if (i2 >= 100 && i3 >= 60) {
            poor = false;
        }
        boolean z2 = i >= cap() || poor;
        Iterator it = Groups.build.iterator();
        while (it.hasNext()) {
            UnitFactory.UnitFactoryBuild unitFactoryBuild = (Building) it.next();
            if (((Building) unitFactoryBuild).team == Vars.player.team() && (unitFactoryBuild instanceof UnitFactory.UnitFactoryBuild)) {
                UnitFactory.UnitFactoryBuild unitFactoryBuild2 = unitFactoryBuild;
                UnitFactory unitFactory = unitFactoryBuild2.block;
                String str = unitFactory.name.equals("air-factory") ? z ? "mono" : null : unitFactory.name.equals("ground-factory") ? typeOn ? "nova" : null : null;
                if (str != null) {
                    int pos = unitFactoryBuild2.pos();
                    if (z2) {
                        if (mine.remove(Integer.valueOf(pos)) && unitFactoryBuild2.currentPlan != -1) {
                            Call.tileConfig(Vars.player, unitFactoryBuild2, -1);
                        }
                    } else if (unitFactoryBuild2.currentPlan == -1 || mine.contains(Integer.valueOf(pos))) {
                        int i4 = -1;
                        for (int i5 = 0; i5 < unitFactory.plans.size; i5++) {
                            if (((UnitFactory.UnitPlan) unitFactory.plans.get(i5)).unit != null && str.equals(((UnitFactory.UnitPlan) unitFactory.plans.get(i5)).unit.name)) {
                                i4 = i5;
                            }
                        }
                        if (i4 >= 0 && unitFactoryBuild2.currentPlan != i4) {
                            Call.tileConfig(Vars.player, unitFactoryBuild2, Integer.valueOf(i4));
                            mine.add(Integer.valueOf(pos));
                        } else if (i4 >= 0) {
                            mine.add(Integer.valueOf(pos));
                        }
                    }
                }
            }
        }
    }

    public static BaseDialog dialog() {
        BaseDialog baseDialog = new BaseDialog(title());
        Runnable[] runnableArr = new Runnable[1];
        runnableArr[0] = () -> {
            Table table = baseDialog.cont;
            table.clear();
            table.defaults().left().pad(4.0f);
            table.check(tr("Ativar mineradores", "Enable miner units", "Activar unidades mineras", "Включить шахтёров"), enabled(), z -> {
                setEnabled(z);
                Vars.ui.showInfoToast(title() + ": " + z, 2.0f);
            }).row();
            table.add(tr("Manda as unidades minerarem sempre o recurso que mais falta no núcleo.", "Sends the units to mine whatever the core lacks most.", "Envía las unidades a minar lo que más falta en el núcleo.", "Отправляет юнитов добывать то, чего в ядре меньше всего.")).wrap().width(380.0f).row();
            for (int i = 0; i < TYPES.length; i++) {
                int i2 = i;
                UnitType unit = Vars.content.unit(TYPES[i]);
                table.check(unit != null ? unit.localizedName : TYPES[i], typeOn(i), z2 -> {
                    setTypeOn(i2, z2);
                }).row();
            }
            table.check(tr("Produzir unidades (configura air/ground factory)", "Produce units (configures air/ground factory)", "Producir unidades (configura air/ground factory)", "Производить юнитов (настраивает air/ground factory)"), produce(), z3 -> {
                setProduce(z3);
            }).row();
            Table table2 = new Table();
            table2.add(tr("Quantidade máxima", "Max amount", "Cantidad máxima", "Макс. количество")).width(190.0f);
            table2.button("-", () -> {
                setCap(cap() - 1);
                LunariansSettings.save();
                runnableArr[0].run();
            }).size(46.0f, 40.0f).pad(2.0f);
            table2.add(String.valueOf(cap())).width(48.0f).center();
            table2.button("+", () -> {
                setCap(cap() + 1);
                LunariansSettings.save();
                runnableArr[0].run();
            }).size(46.0f, 40.0f).pad(2.0f);
            table.add(table2).row();
            table.add(tr("Mono→Poly→Mega vem das reconstructors; Quasar: ground factory (Nova→Pulsar→Quasar).", "Mono→Poly→Mega come from the reconstructors; Quasar: ground factory (Nova→Pulsar→Quasar).", "Mono→Poly→Mega vienen de los reconstructores; Quasar: ground factory (Nova→Pulsar→Quasar).", "Mono→Poly→Mega — из реконструкторов; Quasar — ground factory (Nova→Pulsar→Quasar).")).wrap().width(380.0f).row();
        };
        runnableArr[0].run();
        baseDialog.addCloseButton();
        baseDialog.hidden(LunariansSettings::save);
        return baseDialog;
    }
}
