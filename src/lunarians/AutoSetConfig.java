package lunarians;

import arc.Core;
import arc.func.Boolc;
import arc.func.Prov;
import arc.scene.ui.layout.Table;
import java.util.ArrayList;
import java.util.Iterator;
import lunarians.ui.UiText;
import mindustry.Vars;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.blocks.production.Drill;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/AutoSetConfig.class */
public class AutoSetConfig {
    public static final int MODE_DRILLS = 0;
    public static final int MODE_FACTORIES = 1;
    public static final int DEF_COPPER = 3;
    public static final int DEF_LEAD = 1;
    public static final int DEF_COAL = 2;
    public static final int DEF_SAND = 2;
    public static final String[] ORES = {"copper", "lead", "coal", "sand", "titanium", "thorium"};
    public static final int[] ORE_DEF = {3, 1, 2, 2, 0, 0};
    public static final String[] ORE_NAME_EN = {"Copper", "Lead", "Coal", "Sand", "Titanium", "Thorium"};
    public static final String[] ORE_NAME_PT = {"Cobre", "Chumbo", "Carvão", "Areia", "Titânio", "Tório"};
    public static final String[] FACS = {"graphite", "silicon", "metaglass"};
    public static final String[] FAC_NAME_EN = {"Graphite", "Silicon", "Metaglass"};
    public static final String[] FAC_NAME_PT = {"Grafite", "Silicone", "Metavidro"};
    public static final String[] FAC_SCHEM = {"bXNjaAF4nCWM4QrCMAyEr10dqDjwQfYoPoH4o844C1tb2mwi4rvbdJBw4S73oUVrcBiCZ/J8sRH6+0M72TtNGfp6g/F2JuztwiG+HBOOD8pDcpFd8ECDbky2Jn1MlDPO7Nh6t8x9oa70CQmnsFJ6TuHdj7YQgK4slIwqBCViNtmJaEl1ESV/ckBMAy3vNWu2eiMmpIzKqDAj5h9WRyUl", "bXNjaAF4nCWMawrCMBCEJw8rKCoepEfxBOKPWFcJ5FGSbUXEu5ttYZdvmdkZdOgsdkNOTIkvboT+/tAFd6dQoa832OQiYesmztVH7B9Uh+JH9jkBBqfqg2/xvkYKTAVn9uySn2Lf1Jk+ueCQZyrPkN/9yzEBOLaFklGtQgnsio1Ai6sblPzJAREttLwvnlnjRkRIGEvHUmZF/AMCcCS/", "bXNjaAF4nCWMawrCQAyEZ7drEUXBg/QonkD8EWuUxX2UNq2IeHc3W0j4wkxm0KJ12PU5CSc50wD7/aENdOMwwV6ucIkiY0uz5MhC2N956kc/iM8JaOBePiScxAslP8euVC38ySMOeeHxEfK7e5IwgGNZGB1TckbhVmwUVl1bYPRPD6joYPW9es0ab1SEhlE7aplT8Q/6UyCn"};

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Prov {
        public final Object get() {
            return AutoSetConfig.lambda$buildAll$2();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$19, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass19 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoSetConfig.lambda$buildAll$3();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$20, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass20 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoSetConfig.lambda$buildAll$4();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$21, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass21 implements Prov {
        public final Object get() {
            return AutoSetConfig.lambda$buildAll$5();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$22, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass22 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoSetConfig.lambda$buildAll$6();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$23, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass23 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoSetConfig.lambda$buildAll$7();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$24, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass24 implements Runnable {
        public final /* synthetic */ Runnable f$0;
        public final /* synthetic */ Table f$1;

        public /* synthetic */ AnonymousClass24(Runnable runnable, Table table) {
            this.f$0 = runnable;
            this.f$1 = table;
        }

        @Override // java.lang.Runnable
        public final void run() {
            AutoSetConfig.lambda$stepRow$0(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$25, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass25 implements Runnable {
        public final /* synthetic */ Runnable f$0;
        public final /* synthetic */ Table f$1;

        public /* synthetic */ AnonymousClass25(Runnable runnable, Table table) {
            this.f$0 = runnable;
            this.f$1 = table;
        }

        @Override // java.lang.Runnable
        public final void run() {
            AutoSetConfig.lambda$stepRow$1(this.f$0, this.f$1);
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$26, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass26 implements Prov {
        public final Object get() {
            return AutoSetConfig.lambda$buildFactories$20();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$27, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass27 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoSetConfig.lambda$buildFactories$21();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$28, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass28 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoSetConfig.lambda$buildFactories$22();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$29, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass29 implements Prov {
        public final Object get() {
            return AutoSetConfig.lambda$buildFactories$23();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$30, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass30 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoSetConfig.lambda$buildFactories$24();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$31, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass31 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoSetConfig.lambda$buildFactories$25();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$32, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass32 implements Prov {
        public final Object get() {
            return AutoSetConfig.lambda$buildFactories$26();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$33, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass33 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoSetConfig.lambda$buildFactories$27();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$34, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass34 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoSetConfig.lambda$buildFactories$28();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$35, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass35 implements Prov {
        public final Object get() {
            return AutoSetConfig.lambda$buildDrills$8();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$36, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass36 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoSetConfig.lambda$buildDrills$18();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$37, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass37 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoSetConfig.lambda$buildDrills$19();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$38, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass38 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoSetConfig.lambda$buildDrills$9();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$39, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass39 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoSetConfig.lambda$buildDrills$10();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$40, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass40 implements Prov {
        public final Object get() {
            return AutoSetConfig.lambda$buildDrills$11();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$41, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass41 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoSetConfig.lambda$buildDrills$12();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$42, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass42 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoSetConfig.lambda$buildDrills$13();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$43, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass43 implements Prov {
        public final Object get() {
            return AutoSetConfig.lambda$buildDrills$14();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$44, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass44 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoSetConfig.lambda$buildDrills$15();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$45, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass45 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            AutoSetConfig.lambda$buildDrills$16();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.AutoSetConfig$46, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass46 implements Prov {
        public final Object get() {
            return AutoSetConfig.lambda$buildDrills$17();
        }
    }

    private static boolean pt() {
        return "pt".equals(LunariansI18n.getLanguage());
    }

    private static String tr(String str, String str2) {
        return pt() ? str : str2;
    }

    public static int mode() {
        return Math.max(0, Math.min(2, Core.settings.getInt("lunarians-as-mode", 0)));
    }

    public static void setMode(int i) {
        Core.settings.put("lunarians-as-mode", Integer.valueOf(i));
    }

    public static int oreCount(int i) {
        return clamp(Core.settings.getInt("lunarians-as-ore-" + ORES[i], ORE_DEF[i]), 0, 12);
    }

    public static void setOreCount(int i, int i2) {
        Core.settings.put("lunarians-as-ore-" + ORES[i], Integer.valueOf(clamp(i2, 0, 12)));
    }

    public static boolean facOn(int i) {
        return Core.settings.getInt(new StringBuilder().append("lunarians-as-fac-on-").append(FACS[i]).toString(), 1) != 0;
    }

    public static void setFacOn(int i, boolean z) {
        Core.settings.put("lunarians-as-fac-on-" + FACS[i], Integer.valueOf(z ? 1 : 0));
    }

    public static int facCount(int i) {
        return clamp(Core.settings.getInt("lunarians-as-fac-n-" + FACS[i], 1), 1, 6);
    }

    public static void setFacCount(int i, int i2) {
        Core.settings.put("lunarians-as-fac-n-" + FACS[i], Integer.valueOf(clamp(i2, 1, 6)));
    }

    public static boolean facDrill() {
        return Core.settings.getInt("lunarians-as-fac-drill", 1) != 0;
    }

    public static void setFacDrill(boolean z) {
        Core.settings.put("lunarians-as-fac-drill", Integer.valueOf(z ? 1 : 0));
    }

    public static String drillFor(String str) {
        String string = Core.settings.getString("lunarians-as-drill-" + str, "");
        if (string == null || string.isEmpty()) {
            if ("titanium".equals(str)) {
                return "pneumatic-drill";
            }
            if ("thorium".equals(str)) {
                return "laser-drill";
            }
            return "";
        }
        return string;
    }

    public static void setDrillFor(String str, String str2) {
        Core.settings.put("lunarians-as-drill-" + str, str2 == null ? "" : str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean hasCustomDrill(String str) {
        String string = Core.settings.getString("lunarians-as-drill-" + str, "");
        return (string == null || string.isEmpty()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ArrayList<String> drillChoices(String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("");
        Item item = Vars.content.item(str);
        Iterator it = Vars.content.blocks().iterator();
        while (it.hasNext()) {
            Drill drill = (Block) it.next();
            if (drill instanceof Drill) {
                Drill drill2 = drill;
                if (item == null || drill2.tier >= item.hardness) {
                    arrayList.add(drill2.name);
                }
            }
        }
        return arrayList;
    }

    private static String drillLabel(String str) {
        String drillFor = hasCustomDrill(str) ? drillFor(str) : "";
        String drillFor2 = drillFor(str);
        if (drillFor2 == null || drillFor2.isEmpty()) {
            drillFor2 = LunariansSettings.drillBlock;
        }
        Block block = drillFor2 == null ? null : Vars.content.block(drillFor2);
        String valueOf = block == null ? String.valueOf(drillFor2) : block.localizedName + " " + block.size + "x" + block.size;
        if (drillFor.isEmpty()) {
            return tr("padrão", "default") + " (" + valueOf + ")";
        }
        return valueOf;
    }

    public static String tr4(String str, String str2, String str3, String str4) {
        String language = LunariansI18n.getLanguage();
        if ("pt".equals(language)) {
            return str;
        }
        if ("es".equals(language)) {
            return str3;
        }
        if ("ru".equals(language)) {
            return str4;
        }
        return str2;
    }

    public static boolean steamOn() {
        return Core.settings.getInt("lunarians-as-steam", 0) != 0;
    }

    public static void setSteamOn(boolean z) {
        Core.settings.put("lunarians-as-steam", Integer.valueOf(z ? 1 : 0));
    }

    public static int steamCount() {
        return clamp(Core.settings.getInt("lunarians-as-steam-n", 1), 1, 6);
    }

    public static void setSteamCount(int i) {
        Core.settings.put("lunarians-as-steam-n", Integer.valueOf(clamp(i, 1, 6)));
    }

    public static int steamMinCoal() {
        return clamp(Core.settings.getInt("lunarians-as-steam-coal", 9), 5, 9);
    }

    public static void setSteamMinCoal(int i) {
        Core.settings.put("lunarians-as-steam-coal", Integer.valueOf(clamp(i, 5, 9)));
    }

    public static boolean autoPower() {
        return Core.settings.getInt("lunarians-as-power", 1) != 0;
    }

    public static void setAutoPower(boolean z) {
        Core.settings.put("lunarians-as-power", Integer.valueOf(z ? 1 : 0));
    }

    public static boolean bigNode() {
        return Core.settings.getInt("lunarians-as-bignode", 0) != 0;
    }

    public static void setBigNode(boolean z) {
        Core.settings.put("lunarians-as-bignode", Integer.valueOf(z ? 1 : 0));
    }

    private static int clamp(int i, int i2, int i3) {
        return Math.max(i2, Math.min(i3, i));
    }

    public static int spacing() {
        return clamp(Core.settings.getInt("lunarians-set-spacing", 3), 1, 10);
    }

    public static void setSpacing(int i) {
        Core.settings.put("lunarians-set-spacing", Integer.valueOf(clamp(i, 1, 10)));
    }

    public static int wallGap() {
        return clamp(Core.settings.getInt("lunarians-set-wall-gap", 2), 0, 10);
    }

    public static void setWallGap(int i) {
        Core.settings.put("lunarians-set-wall-gap", Integer.valueOf(clamp(i, 0, 10)));
    }

    public static int facGap() {
        return clamp(Core.settings.getInt("lunarians-set-fac-gap", 3), 0, 12);
    }

    public static void setFacGap(int i) {
        Core.settings.put("lunarians-set-fac-gap", Integer.valueOf(clamp(i, 0, 12)));
    }

    public static int coreGap() {
        return clamp(Core.settings.getInt("lunarians-set-core-gap", 6), 0, 30);
    }

    public static void setCoreGap(int i) {
        Core.settings.put("lunarians-set-core-gap", Integer.valueOf(clamp(i, 0, 30)));
    }

    public static int floorMax() {
        return clamp(Core.settings.getInt("lunarians-set-floor-max", 16), 4, 64);
    }

    public static void setFloorMax(int i) {
        Core.settings.put("lunarians-set-floor-max", Integer.valueOf(clamp(i, 4, 64)));
    }

    private static void stepRow(Table table, String str, Prov<String> prov, Runnable runnable, Runnable runnable2) {
        Table table2 = new Table();
        table2.add(str).left().width(190.0f).wrap();
        table2.button("-", () -> {
            runnable.run();
            LunariansSettings.save();
            build(table);
        }).size(46.0f, 40.0f).pad(2.0f);
        table2.add((CharSequence) prov.get()).width(48.0f).center().pad(4.0f);
        table2.button("+", () -> {
            runnable2.run();
            LunariansSettings.save();
            build(table);
        }).size(46.0f, 40.0f).pad(2.0f);
        table.add(table2).left().row();
    }

    public static String[] oreOrder() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < oreCount(0); i++) {
            arrayList.add("copper");
        }
        for (int i2 = 0; i2 < oreCount(1); i2++) {
            arrayList.add("lead");
        }
        int oreCount = oreCount(2);
        int oreCount2 = oreCount(3);
        while (true) {
            if (oreCount <= 0 && oreCount2 <= 0) {
                break;
            }
            if (oreCount > 0) {
                arrayList.add("coal");
                oreCount--;
            }
            if (oreCount2 > 0) {
                arrayList.add("sand");
                oreCount2--;
            }
        }
        for (int i3 = 0; i3 < oreCount(4); i3++) {
            arrayList.add("titanium");
        }
        for (int i4 = 0; i4 < oreCount(5); i4++) {
            arrayList.add("thorium");
        }
        return (String[]) arrayList.toArray(new String[0]);
    }

    public static String[] factoryQueue() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < FACS.length; i++) {
            if (facOn(i)) {
                for (int i2 = 0; i2 < facCount(i); i2++) {
                    arrayList.add(FACS[i]);
                }
            }
        }
        return (String[]) arrayList.toArray(new String[0]);
    }

    public static String schematicFor(String str) {
        for (int i = 0; i < FACS.length; i++) {
            if (FACS[i].equals(str)) {
                return FAC_SCHEM[i];
            }
        }
        return null;
    }

    public static void build(final Table table) {
        table.clear();
        table.defaults().growX().padBottom(4.0f);
        int mode = mode();
        Table table2 = new Table();
        table2.button((mode == 0 ? "[accent]> " : "") + tr4("Recursos", "Resources", "Recursos", "Ресурсы") + (mode == 0 ? "[]" : ""), new Runnable() { // from class: lunarians.AutoSetConfig.1
            @Override // java.lang.Runnable
            public void run() {
                AutoSetConfig.setMode(0);
                LunariansSettings.save();
                AutoSetConfig.build(table);
            }
        }).height(46.0f).growX().pad(2.0f);
        table2.button((mode == 1 ? "[accent]> " : "") + tr4("Fábricas", "Factories", "Fábricas", "Заводы") + (mode == 1 ? "[]" : ""), new Runnable() { // from class: lunarians.AutoSetConfig.2
            @Override // java.lang.Runnable
            public void run() {
                AutoSetConfig.setMode(1);
                LunariansSettings.save();
                AutoSetConfig.build(table);
            }
        }).height(46.0f).growX().pad(2.0f);
        table2.button((mode == 2 ? "[accent]> " : "") + "Auto All" + (mode == 2 ? "[]" : ""), new Runnable() { // from class: lunarians.AutoSetConfig.3
            @Override // java.lang.Runnable
            public void run() {
                AutoSetConfig.setMode(2);
                LunariansSettings.save();
                AutoSetConfig.build(table);
            }
        }).height(46.0f).growX().pad(2.0f);
        table.add(table2).growX().row();
        table.add(tr("Aba escolhida = o que o botão GO do Auto Set faz.", "Selected tab = what the Auto Set GO button does.")).left().wrap().growX().padBottom(6.0f).row();
        if (mode == 2) {
            buildAll(table);
        } else if (mode == 0) {
            buildDrills(table);
        } else {
            buildFactories(table);
        }
    }

    public static boolean autoDelete() {
        return Core.settings.getBool("lunarians-auto-delete", true);
    }

    public static void setAutoDelete(boolean z) {
        Core.settings.put("lunarians-auto-delete", Boolean.valueOf(z));
    }

    public static int allRadius() {
        return clamp(Core.settings.getInt("lunarians-econ-radius", 45), 10, 80);
    }

    private static void buildAll(final Table table) {
        UiText.section(table, tr4("Auto All (recursos + fábricas + unidades + schematics)", "Auto All (resources + factories + units + schematics)", "Auto All (recursos + fábricas + unidades + esquemas)", "Auto All (ресурсы + заводы + юниты + схемы)"));
        table.add(tr4("Com esta aba, GO (confirmar) abre o MAPEAMENTO: você voa até os minérios (ficam marcados), pode marcar a areia na mão e decide quando começar. Depois roda: cobre/chumbo, carvão/areia, grafite, metavidro, silício, titânio, unidades, tório e por fim os schematics do Auto Dock.", "With this tab, GO (confirm) opens MAPPING: fly to the ore (it gets marked), mark sand by hand if you want, and choose when to start. Then it runs: copper/lead, coal/sand, graphite, metaglass, silicon, titanium, units, thorium and finally the Auto Dock schematics.", "Con esta pestaña, GO (confirmar) abre el MAPEO: vuela al mineral (se marca), marca la arena a mano y decide cuándo empezar. Luego corre: cobre/plomo, carbón/arena, grafito, metacristal, silicio, titanio, unidades, torio y al final los esquemas del Auto Dock.", "В этой вкладке GO (подтвердить) открывает КАРТИРОВАНИЕ: летите к руде (она отмечается), отмечайте песок вручную и сами решайте, когда начать.")).left().wrap().growX().padBottom(6.0f).row();
        stepRow(table, tr4("Raio mínimo da área (tiles)", "Min area radius (tiles)", "Radio mínimo del área (tiles)", "Мин. радиус области (клеток)"), () -> {
            return String.valueOf(allRadius());
        }, () -> {
            Core.settings.put("lunarians-econ-radius", Integer.valueOf(allRadius() - 5));
        }, () -> {
            Core.settings.put("lunarians-econ-radius", Integer.valueOf(allRadius() + 5));
        });
        table.add(tr4("A área é centrada na sua unidade e cresce para cobrir tudo que você marcou. As quantidades vêm das abas Recursos e Fábricas.", "The area is centered on your unit and grows to cover everything you marked. Amounts come from the Resources and Factories tabs.", "El área se centra en tu unidad y crece para cubrir lo marcado. Las cantidades vienen de las pestañas Recursos y Fábricas.", "Область центрируется на вашей единице и растёт до отмеченного. Количества — во вкладках Ресурсы и Заводы.")).left().wrap().growX().row();
        UiText.section(table, tr4("Auto delete e Auto power node", "Auto delete and Auto power node", "Auto delete y Auto power node", "Автоудаление и авто-узлы"));
        UiText.check(table, tr4("Auto delete: remove esteiras/drills/nos próprios do Auto Set que bloqueiam um plano (máx. 30 por execução)", "Auto delete: removes Auto Set's own conveyors/drills/nodes that block a plan (max 30 per run)", "Auto delete: quita cintas/taladros/nodos propios del Auto Set que bloquean un plan (máx. 30 por ejecución)", "Автоудаление: убирает свои конвейеры/буры/узлы Auto Set, мешающие плану (макс. 30)"), autoDelete(), new Boolc() { // from class: lunarians.AutoSetConfig.4
            public void get(boolean z) {
                AutoSetConfig.setAutoDelete(z);
                LunariansSettings.save();
            }
        }).left().row();
        UiText.check(table, tr4("Auto power node: liga consumidores sem energia à rede mais próxima", "Auto power node: links unpowered consumers to the nearest powered grid", "Auto power node: conecta consumidores sin energía a la red más cercana", "Авто-узлы: подключают потребителей без энергии к ближайшей сети"), PowerLink.enabled(), new Boolc() { // from class: lunarians.AutoSetConfig.5
            public void get(boolean z) {
                PowerLink.setEnabled(z);
                LunariansSettings.save();
            }
        }).left().row();
        UiText.section(table, ConveyorUpgrade.title());
        UiText.check(table, tr4("Trocar esteiras comuns por esteiras de titânio perto da sua unidade", "Replace basic conveyors with titanium conveyors near your unit", "Cambiar cintas básicas por cintas de titanio cerca de tu unidad", "Заменять обычные конвейеры титановыми рядом с вами"), ConveyorUpgrade.enabled(), new Boolc() { // from class: lunarians.AutoSetConfig.6
            public void get(boolean z) {
                ConveyorUpgrade.setEnabled(z);
                LunariansSettings.save();
            }
        }).left().row();
        stepRow(table, tr4("Titânio mínimo no núcleo", "Min titanium in core", "Titanio mínimo en el núcleo", "Мин. титана в ядре"), () -> {
            return String.valueOf(ConveyorUpgrade.threshold());
        }, () -> {
            ConveyorUpgrade.setThreshold(ConveyorUpgrade.threshold() - 250);
        }, () -> {
            ConveyorUpgrade.setThreshold(ConveyorUpgrade.threshold() + 250);
        });
        UiText.section(table, tr4("Resetar configurações para o modo", "Reset settings for mode", "Restablecer ajustes para el modo", "Сброс настроек под режим"));
        table.add(tr4("Aplica quantidades, fábricas, raio, mineradores e esteira de titânio ideais para cada modo. Você pode ajustar depois.", "Applies amounts, factories, radius, miner units and titanium conveyor suited to each mode. You can tweak afterwards.", "Aplica cantidades, fábricas, radio, mineros y cinta de titanio adecuados a cada modo. Puedes ajustar después.", "Применяет количества, заводы, радиус, шахтёров и титановый конвейер под каждый режим.")).left().wrap().growX().row();
        Table table2 = new Table();
        final String[] strArr = {tr4("Sobrevivência", "Survival", "Supervivencia", "Выживание"), "PvP", "PvP Hexed"};
        for (int i = 0; i < 3; i++) {
            final int i2 = i;
            table2.button(strArr[i], new Runnable() { // from class: lunarians.AutoSetConfig.7
                @Override // java.lang.Runnable
                public void run() {
                    AutoSetConfig.applyPreset(i2);
                    LunariansSettings.save();
                    if (Vars.ui != null) {
                        Vars.ui.showInfoToast(AutoSetConfig.tr4("Configuração aplicada: ", "Settings applied: ", "Ajustes aplicados: ", "Настройки применены: ") + strArr[i2], 3.0f);
                    }
                    AutoSetConfig.build(table);
                }
            }).height(46.0f).growX().pad(2.0f);
        }
        table.add(table2).growX().row();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void applyPreset(int i) {
        int[] iArr = {new int[]{3, 1, 2, 2, 2, 0}, new int[]{3, 2, 1, 1, 2, 0}, new int[]{2, 1, 1, 1, 0, 0}};
        int[] iArr2 = {new int[]{1, 1, 1, 1, 1, 1}, new int[]{1, 1, 1, 1, 0, 1}, new int[]{1, 1, 0, 1, 0, 1}};
        int[] iArr3 = {45, 30, 20};
        int[] iArr4 = {8, 4, 3};
        int[] iArr5 = {2000, 1500, 1000};
        int[] iArr6 = {3, 2, 2};
        int[] iArr7 = {6, 4, 3};
        for (int i2 = 0; i2 < ORES.length; i2++) {
            setOreCount(i2, iArr[i][i2]);
            setDrillFor(ORES[i2], "");
        }
        for (int i3 = 0; i3 < FACS.length; i3++) {
            setFacOn(i3, iArr2[i][i3 * 2] != 0);
            setFacCount(i3, iArr2[i][(i3 * 2) + 1]);
        }
        setFacDrill(true);
        setSpacing(iArr6[i]);
        setCoreGap(iArr7[i]);
        setFacGap(3);
        setWallGap(2);
        setFloorMax(16);
        setSteamOn(false);
        setAutoPower(true);
        setBigNode(false);
        Core.settings.put("lunarians-econ-radius", Integer.valueOf(iArr3[i]));
        ConveyorUpgrade.setEnabled(true);
        ConveyorUpgrade.setThreshold(iArr5[i]);
        MinerUnits.setEnabled(true);
        MinerUnits.setProduce(true);
        MinerUnits.setCap(iArr4[i]);
        for (int i4 = 0; i4 < MinerUnits.TYPES.length; i4++) {
            MinerUnits.setTypeOn(i4, true);
        }
    }

    private static void buildDrills(final Table table) {
        UiText.section(table, tr("Quantidade de drills por recurso", "Drills per resource"));
        for (int i = 0; i < ORES.length; i++) {
            final int i2 = i;
            Table table2 = new Table();
            table2.add(tr(ORE_NAME_PT[i], ORE_NAME_EN[i])).left().width(110.0f);
            table2.button("-", new Runnable() { // from class: lunarians.AutoSetConfig.8
                @Override // java.lang.Runnable
                public void run() {
                    AutoSetConfig.setOreCount(i2, AutoSetConfig.oreCount(i2) - 1);
                    LunariansSettings.save();
                    AutoSetConfig.build(table);
                }
            }).size(46.0f, 40.0f).pad(2.0f);
            table2.add(String.valueOf(oreCount(i))).width(48.0f).center().pad(4.0f);
            table2.button("+", new Runnable() { // from class: lunarians.AutoSetConfig.9
                @Override // java.lang.Runnable
                public void run() {
                    AutoSetConfig.setOreCount(i2, AutoSetConfig.oreCount(i2) + 1);
                    LunariansSettings.save();
                    AutoSetConfig.build(table);
                }
            }).size(46.0f, 40.0f).pad(2.0f);
            table2.add("[gray](" + tr("padrão", "default") + " " + ORE_DEF[i] + ")[]").left().padLeft(8.0f);
            table.add(table2).left().row();
            final String str = ORES[i];
            table.button(tr("Drill: ", "Drill: ") + drillLabel(str), new Runnable() { // from class: lunarians.AutoSetConfig.10
                @Override // java.lang.Runnable
                public void run() {
                    ArrayList drillChoices = AutoSetConfig.drillChoices(str);
                    AutoSetConfig.setDrillFor(str, (String) drillChoices.get((drillChoices.indexOf(AutoSetConfig.hasCustomDrill(str) ? AutoSetConfig.drillFor(str) : "") + 1) % drillChoices.size()));
                    LunariansSettings.save();
                    AutoSetConfig.build(table);
                }
            }).height(40.0f).growX().padLeft(8.0f).row();
        }
        UiText.section(table, tr("Isolamento e areia", "Isolation and sand"));
        stepRow(table, tr("Distância entre campos", "Field spacing"), () -> {
            return String.valueOf(spacing());
        }, () -> {
            setSpacing(spacing() - 1);
        }, () -> {
            setSpacing(spacing() + 1);
        });
        stepRow(table, tr("Tamanho máx. do campo de areia (tiles)", "Max sand field size (tiles)"), () -> {
            return String.valueOf(floorMax());
        }, () -> {
            setFloorMax(floorMax() - 4);
        }, () -> {
            setFloorMax(floorMax() + 4);
        });
        UiText.section(table, tr4("Steam Drill (carvão 3x3 + água)", "Steam Drill (3x3 coal + water)", "Steam Drill (carbón 3x3 + agua)", "Steam Drill (уголь 3x3 + вода)"));
        UiText.check(table, tr4("Ativar Steam Drill (schematic + bomba 2x2 na água)", "Enable Steam Drill (schematic + 2x2 pump on water)", "Activar Steam Drill (esquema + bomba 2x2 en el agua)", "Включить Steam Drill (схема + насос 2x2 на воде)"), steamOn(), new Boolc() { // from class: lunarians.AutoSetConfig.11
            public void get(boolean z) {
                AutoSetConfig.setSteamOn(z);
                LunariansSettings.save();
            }
        }).left().row();
        stepRow(table, tr4("Quantidade de Steam Drills", "Steam Drill amount", "Cantidad de Steam Drills", "Количество Steam Drill"), () -> {
            return String.valueOf(steamCount());
        }, () -> {
            setSteamCount(steamCount() - 1);
        }, () -> {
            setSteamCount(steamCount() + 1);
        });
        stepRow(table, tr4("Mín. tiles de carvão no 3x3", "Min coal tiles in the 3x3", "Mín. casillas de carbón en el 3x3", "Мин. клеток угля в 3x3"), () -> {
            return String.valueOf(steamMinCoal());
        }, () -> {
            setSteamMinCoal(steamMinCoal() - 1);
        }, () -> {
            setSteamMinCoal(steamMinCoal() + 1);
        });
        UiText.check(table, tr4("Auto conectar energia (power node + painéis para ligar a bomba)", "Auto connect power (power nodes + solar panels to start the pump)", "Auto conectar energía (nodos + paneles para arrancar la bomba)", "Автоподключение энергии (узлы + солнечные панели для насоса)"), autoPower(), new Boolc() { // from class: lunarians.AutoSetConfig.12
            public void get(boolean z) {
                AutoSetConfig.setAutoPower(z);
                LunariansSettings.save();
            }
        }).left().row();
        UiText.check(table, tr4("Usar Large Power Node se houver recursos", "Use Large Power Node when resources allow", "Usar Large Power Node si hay recursos", "Использовать Large Power Node, если хватает ресурсов"), bigNode(), new Boolc() { // from class: lunarians.AutoSetConfig.13
            public void get(boolean z) {
                AutoSetConfig.setBigNode(z);
                LunariansSettings.save();
            }
        }).left().row();
        table.button(tr("Restaurar padrão (3 / 1 / 2 / 2 / 0 / 0)", "Restore default (3 / 1 / 2 / 2 / 0 / 0)"), new Runnable() { // from class: lunarians.AutoSetConfig.14
            @Override // java.lang.Runnable
            public void run() {
                for (int i3 = 0; i3 < AutoSetConfig.ORES.length; i3++) {
                    AutoSetConfig.setOreCount(i3, AutoSetConfig.ORE_DEF[i3]);
                    AutoSetConfig.setDrillFor(AutoSetConfig.ORES[i3], "");
                }
                LunariansSettings.save();
                AutoSetConfig.build(table);
            }
        }).height(44.0f).growX().padTop(8.0f).row();
    }

    private static void buildFactories(final Table table) {
        UiText.section(table, tr("Fábricas (schematics virados para cima)", "Factories (schematics face up)"));
        table.add(tr("Entradas: os 2 conveyors de baixo (esq. e dir.). Saída: topo, ligada ao núcleo.", "Item inputs: the bottom conveyors. Output: top, routed to the core.")).left().wrap().growX().padBottom(6.0f).row();
        for (int i = 0; i < FACS.length; i++) {
            final int i2 = i;
            Table table2 = new Table();
            UiText.check(table2, tr(FAC_NAME_PT[i], FAC_NAME_EN[i]), facOn(i), new Boolc() { // from class: lunarians.AutoSetConfig.15
                public void get(boolean z) {
                    AutoSetConfig.setFacOn(i2, z);
                    LunariansSettings.save();
                }
            }).left().width(170.0f);
            table2.button("-", new Runnable() { // from class: lunarians.AutoSetConfig.16
                @Override // java.lang.Runnable
                public void run() {
                    AutoSetConfig.setFacCount(i2, AutoSetConfig.facCount(i2) - 1);
                    LunariansSettings.save();
                    AutoSetConfig.build(table);
                }
            }).size(46.0f, 40.0f).pad(2.0f);
            table2.add("x" + facCount(i)).width(48.0f).center().pad(4.0f);
            table2.button("+", new Runnable() { // from class: lunarians.AutoSetConfig.17
                @Override // java.lang.Runnable
                public void run() {
                    AutoSetConfig.setFacCount(i2, AutoSetConfig.facCount(i2) + 1);
                    LunariansSettings.save();
                    AutoSetConfig.build(table);
                }
            }).size(46.0f, 40.0f).pad(2.0f);
            table.add(table2).left().row();
        }
        stepRow(table, tr("Distância mínima do núcleo (tiles)", "Min distance from core (tiles)"), () -> {
            return String.valueOf(coreGap());
        }, () -> {
            setCoreGap(coreGap() - 1);
        }, () -> {
            setCoreGap(coreGap() + 1);
        });
        stepRow(table, tr("Distância mínima das paredes (tiles)", "Min distance from walls (tiles)"), () -> {
            return String.valueOf(wallGap());
        }, () -> {
            setWallGap(wallGap() - 1);
        }, () -> {
            setWallGap(wallGap() + 1);
        });
        stepRow(table, tr("Distância entre fábricas (tiles)", "Gap between factories (tiles)"), () -> {
            return String.valueOf(facGap());
        }, () -> {
            setFacGap(facGap() - 1);
        }, () -> {
            setFacGap(facGap() + 1);
        });
        UiText.check(table, tr("Autodrill nas entradas: drills em minério ligados a cada entrada (1 recurso por entrada)", "Autodrill on inputs: ore drills wired to each input (1 resource per input)"), facDrill(), new Boolc() { // from class: lunarians.AutoSetConfig.18
            public void get(boolean z) {
                AutoSetConfig.setFacDrill(z);
                LunariansSettings.save();
            }
        }).left().padTop(6.0f).row();
        table.add(tr("Ordem: 1) máquinas  2) autodrill  3) conexão específica em cada entrada.", "Order: 1) machines  2) autodrill  3) specific connection to each input.")).left().wrap().growX().row();
        table.add(tr("Use GO, toque no CENTRO e depois no raio: as fábricas vão para um espaço livre dentro do círculo.", "Press GO, tap the CENTER then the radius: factories go to free space inside the circle.")).left().wrap().growX().padTop(8.0f).row();
    }
}
