/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.func.Boolc
 *  arc.func.Prov
 *  arc.scene.Element
 *  arc.scene.ui.layout.Table
 *  mindustry.Vars
 *  mindustry.type.Item
 *  mindustry.world.Block
 *  mindustry.world.blocks.production.Drill
 */
package lunarians;

import arc.Core;
import arc.func.Boolc;
import arc.func.Prov;
import arc.scene.Element;
import arc.scene.ui.layout.Table;
import java.util.ArrayList;
import lunarians.ConveyorUpgrade;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.MinerUnits;
import lunarians.PowerLink;
import lunarians.ui.UiText;
import mindustry.Vars;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.blocks.production.Drill;

public class AutoSetConfig {
    public static final int MODE_DRILLS = 0;
    public static final int MODE_FACTORIES = 1;
    public static final int DEF_COPPER = 3;
    public static final int DEF_LEAD = 1;
    public static final int DEF_COAL = 2;
    public static final int DEF_SAND = 2;
    public static final String[] ORES = new String[]{"copper", "lead", "coal", "sand", "titanium", "thorium"};
    public static final int[] ORE_DEF = new int[]{3, 1, 2, 2, 0, 0};
    public static final String[] ORE_NAME_EN = new String[]{"Copper", "Lead", "Coal", "Sand", "Titanium", "Thorium"};
    public static final String[] ORE_NAME_PT = new String[]{"Cobre", "Chumbo", "Carv\u00e3o", "Areia", "Tit\u00e2nio", "T\u00f3rio"};
    public static final String[] FACS = new String[]{"graphite", "silicon", "metaglass"};
    public static final String[] FAC_NAME_EN = new String[]{"Graphite", "Silicon", "Metaglass"};
    public static final String[] FAC_NAME_PT = new String[]{"Grafite", "Silicone", "Metavidro"};
    public static final String[] FAC_SCHEM = new String[]{"bXNjaAF4nCWM4QrCMAyEr10dqDjwQfYoPoH4o844C1tb2mwi4rvbdJBw4S73oUVrcBiCZ/J8sRH6+0M72TtNGfp6g/F2JuztwiG+HBOOD8pDcpFd8ECDbky2Jn1MlDPO7Nh6t8x9oa70CQmnsFJ6TuHdj7YQgK4slIwqBCViNtmJaEl1ESV/ckBMAy3vNWu2eiMmpIzKqDAj5h9WRyUl", "bXNjaAF4nCWMawrCMBCEJw8rKCoepEfxBOKPWFcJ5FGSbUXEu5ttYZdvmdkZdOgsdkNOTIkvboT+/tAFd6dQoa832OQiYesmztVH7B9Uh+JH9jkBBqfqg2/xvkYKTAVn9uySn2Lf1Jk+ueCQZyrPkN/9yzEBOLaFklGtQgnsio1Ai6sblPzJAREttLwvnlnjRkRIGEvHUmZF/AMCcCS/", "bXNjaAF4nCWMawrCQAyEZ7drEUXBg/QonkD8EWuUxX2UNq2IeHc3W0j4wkxm0KJ12PU5CSc50wD7/aENdOMwwV6ucIkiY0uz5MhC2N956kc/iM8JaOBePiScxAslP8euVC38ySMOeeHxEfK7e5IwgGNZGB1TckbhVmwUVl1bYPRPD6joYPW9es0ab1SEhlE7aplT8Q/6UyCn"};

    private static boolean pt() {
        return "pt".equals(LunariansI18n.getLanguage());
    }

    private static String tr(String string, String string2) {
        return AutoSetConfig.pt() ? string : string2;
    }

    public static int mode() {
        return Math.max(0, Math.min(2, Core.settings.getInt("lunarians-as-mode", 0)));
    }

    public static void setMode(int n) {
        Core.settings.put("lunarians-as-mode", (Object)n);
    }

    public static int oreCount(int n) {
        return AutoSetConfig.clamp(Core.settings.getInt("lunarians-as-ore-" + ORES[n], ORE_DEF[n]), 0, 12);
    }

    public static void setOreCount(int n, int n2) {
        Core.settings.put("lunarians-as-ore-" + ORES[n], (Object)AutoSetConfig.clamp(n2, 0, 12));
    }

    public static boolean facOn(int n) {
        return Core.settings.getInt("lunarians-as-fac-on-" + FACS[n], 1) != 0;
    }

    public static void setFacOn(int n, boolean bl) {
        Core.settings.put("lunarians-as-fac-on-" + FACS[n], (Object)(bl ? 1 : 0));
    }

    public static int facCount(int n) {
        return AutoSetConfig.clamp(Core.settings.getInt("lunarians-as-fac-n-" + FACS[n], 1), 1, 6);
    }

    public static void setFacCount(int n, int n2) {
        Core.settings.put("lunarians-as-fac-n-" + FACS[n], (Object)AutoSetConfig.clamp(n2, 1, 6));
    }

    public static boolean facDrill() {
        return Core.settings.getInt("lunarians-as-fac-drill", 1) != 0;
    }

    public static void setFacDrill(boolean bl) {
        Core.settings.put("lunarians-as-fac-drill", (Object)(bl ? 1 : 0));
    }

    public static String drillFor(String string) {
        String string2 = Core.settings.getString("lunarians-as-drill-" + string, "");
        if (string2 == null || string2.isEmpty()) {
            if ("titanium".equals(string)) {
                return "pneumatic-drill";
            }
            if ("thorium".equals(string)) {
                return "laser-drill";
            }
            return "";
        }
        return string2;
    }

    public static void setDrillFor(String string, String string2) {
        Core.settings.put("lunarians-as-drill-" + string, (Object)(string2 == null ? "" : string2));
    }

    private static boolean hasCustomDrill(String string) {
        String string2 = Core.settings.getString("lunarians-as-drill-" + string, "");
        return string2 != null && !string2.isEmpty();
    }

    private static ArrayList<String> drillChoices(String string) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("");
        Item item = Vars.content.item(string);
        for (Block block : Vars.content.blocks()) {
            if (!(block instanceof Drill)) continue;
            Drill drill = (Drill)block;
            if (item != null && drill.tier < item.hardness) continue;
            arrayList.add(drill.name);
        }
        return arrayList;
    }

    private static String drillLabel(String string) {
        String string2;
        String string3 = AutoSetConfig.hasCustomDrill(string) ? AutoSetConfig.drillFor(string) : "";
        String string4 = AutoSetConfig.drillFor(string);
        if (string4 == null || string4.isEmpty()) {
            string4 = LunariansSettings.drillBlock;
        }
        Block block = string4 == null ? null : Vars.content.block(string4);
        String string5 = string2 = block == null ? String.valueOf(string4) : block.localizedName + " " + block.size + "x" + block.size;
        if (string3.isEmpty()) {
            return AutoSetConfig.tr("padr\u00e3o", "default") + " (" + string2 + ")";
        }
        return string2;
    }

    public static String tr4(String string, String string2, String string3, String string4) {
        String string5 = LunariansI18n.getLanguage();
        if ("pt".equals(string5)) {
            return string;
        }
        if ("es".equals(string5)) {
            return string3;
        }
        if ("ru".equals(string5)) {
            return string4;
        }
        return string2;
    }

    public static boolean steamOn() {
        return Core.settings.getInt("lunarians-as-steam", 0) != 0;
    }

    public static void setSteamOn(boolean bl) {
        Core.settings.put("lunarians-as-steam", (Object)(bl ? 1 : 0));
    }

    public static int steamCount() {
        return AutoSetConfig.clamp(Core.settings.getInt("lunarians-as-steam-n", 1), 1, 6);
    }

    public static void setSteamCount(int n) {
        Core.settings.put("lunarians-as-steam-n", (Object)AutoSetConfig.clamp(n, 1, 6));
    }

    public static int steamMinCoal() {
        return AutoSetConfig.clamp(Core.settings.getInt("lunarians-as-steam-coal", 9), 5, 9);
    }

    public static void setSteamMinCoal(int n) {
        Core.settings.put("lunarians-as-steam-coal", (Object)AutoSetConfig.clamp(n, 5, 9));
    }

    public static boolean autoPower() {
        return Core.settings.getInt("lunarians-as-power", 1) != 0;
    }

    public static void setAutoPower(boolean bl) {
        Core.settings.put("lunarians-as-power", (Object)(bl ? 1 : 0));
    }

    public static boolean bigNode() {
        return Core.settings.getInt("lunarians-as-bignode", 0) != 0;
    }

    public static void setBigNode(boolean bl) {
        Core.settings.put("lunarians-as-bignode", (Object)(bl ? 1 : 0));
    }

    private static int clamp(int n, int n2, int n3) {
        return Math.max(n2, Math.min(n3, n));
    }

    public static int spacing() {
        return AutoSetConfig.clamp(Core.settings.getInt("lunarians-set-spacing", 3), 1, 10);
    }

    public static void setSpacing(int n) {
        Core.settings.put("lunarians-set-spacing", (Object)AutoSetConfig.clamp(n, 1, 10));
    }

    public static int wallGap() {
        return AutoSetConfig.clamp(Core.settings.getInt("lunarians-set-wall-gap", 2), 0, 10);
    }

    public static void setWallGap(int n) {
        Core.settings.put("lunarians-set-wall-gap", (Object)AutoSetConfig.clamp(n, 0, 10));
    }

    public static int facGap() {
        return AutoSetConfig.clamp(Core.settings.getInt("lunarians-set-fac-gap", 3), 0, 12);
    }

    public static void setFacGap(int n) {
        Core.settings.put("lunarians-set-fac-gap", (Object)AutoSetConfig.clamp(n, 0, 12));
    }

    public static int coreGap() {
        return AutoSetConfig.clamp(Core.settings.getInt("lunarians-set-core-gap", 6), 0, 30);
    }

    public static void setCoreGap(int n) {
        Core.settings.put("lunarians-set-core-gap", (Object)AutoSetConfig.clamp(n, 0, 30));
    }

    public static int floorMax() {
        return AutoSetConfig.clamp(Core.settings.getInt("lunarians-set-floor-max", 16), 4, 64);
    }

    public static void setFloorMax(int n) {
        Core.settings.put("lunarians-set-floor-max", (Object)AutoSetConfig.clamp(n, 4, 64));
    }

    private static void stepRow(Table table, String string, Prov<String> prov, Runnable runnable, Runnable runnable2) {
        Table table2 = new Table();
        table2.add((CharSequence)string).left().width(190.0f).wrap();
        table2.button("-", () -> {
            runnable.run();
            LunariansSettings.save();
            AutoSetConfig.build(table);
        }).size(46.0f, 40.0f).pad(2.0f);
        table2.add((CharSequence)prov.get()).width(48.0f).center().pad(4.0f);
        table2.button("+", () -> {
            runnable2.run();
            LunariansSettings.save();
            AutoSetConfig.build(table);
        }).size(46.0f, 40.0f).pad(2.0f);
        table.add((Element)table2).left().row();
    }

    public static String[] oreOrder() {
        int n;
        ArrayList<String> arrayList = new ArrayList<String>();
        for (n = 0; n < AutoSetConfig.oreCount(0); ++n) {
            arrayList.add("copper");
        }
        for (n = 0; n < AutoSetConfig.oreCount(1); ++n) {
            arrayList.add("lead");
        }
        n = AutoSetConfig.oreCount(2);
        int n2 = AutoSetConfig.oreCount(3);
        while (n > 0 || n2 > 0) {
            if (n > 0) {
                arrayList.add("coal");
                --n;
            }
            if (n2 <= 0) continue;
            arrayList.add("sand");
            --n2;
        }
        for (n = 0; n < AutoSetConfig.oreCount(4); ++n) {
            arrayList.add("titanium");
        }
        for (n = 0; n < AutoSetConfig.oreCount(5); ++n) {
            arrayList.add("thorium");
        }
        return arrayList.toArray(new String[0]);
    }

    public static String[] factoryQueue() {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (int i = 0; i < FACS.length; ++i) {
            if (!AutoSetConfig.facOn(i)) continue;
            for (int j = 0; j < AutoSetConfig.facCount(i); ++j) {
                arrayList.add(FACS[i]);
            }
        }
        return arrayList.toArray(new String[0]);
    }

    public static String schematicFor(String string) {
        for (int i = 0; i < FACS.length; ++i) {
            if (!FACS[i].equals(string)) continue;
            return FAC_SCHEM[i];
        }
        return null;
    }

    public static void build(final Table table) {
        table.clear();
        table.defaults().growX().padBottom(4.0f);
        int n = AutoSetConfig.mode();
        Table table2 = new Table();
        table2.button((n == 0 ? "[accent]> " : "") + AutoSetConfig.tr4("Recursos", "Resources", "Recursos", "\u0420\u0435\u0441\u0443\u0440\u0441\u044b") + (n == 0 ? "[]" : ""), new Runnable(){

            @Override
            public void run() {
                AutoSetConfig.setMode(0);
                LunariansSettings.save();
                AutoSetConfig.build(table);
            }
        }).height(46.0f).growX().pad(2.0f);
        table2.button((n == 1 ? "[accent]> " : "") + AutoSetConfig.tr4("F\u00e1bricas", "Factories", "F\u00e1bricas", "\u0417\u0430\u0432\u043e\u0434\u044b") + (n == 1 ? "[]" : ""), new Runnable(){

            @Override
            public void run() {
                AutoSetConfig.setMode(1);
                LunariansSettings.save();
                AutoSetConfig.build(table);
            }
        }).height(46.0f).growX().pad(2.0f);
        table2.button((n == 2 ? "[accent]> " : "") + "Auto All" + (n == 2 ? "[]" : ""), new Runnable(){

            @Override
            public void run() {
                AutoSetConfig.setMode(2);
                LunariansSettings.save();
                AutoSetConfig.build(table);
            }
        }).height(46.0f).growX().pad(2.0f);
        table.add((Element)table2).growX().row();
        table.add((CharSequence)AutoSetConfig.tr("Aba escolhida = o que o bot\u00e3o GO do Auto Set faz.", "Selected tab = what the Auto Set GO button does.")).left().wrap().growX().padBottom(6.0f).row();
        if (n == 2) {
            AutoSetConfig.buildAll(table);
        } else if (n == 0) {
            AutoSetConfig.buildDrills(table);
        } else {
            AutoSetConfig.buildFactories(table);
        }
    }

    public static boolean autoDelete() {
        return Core.settings.getBool("lunarians-auto-delete", true);
    }

    public static void setAutoDelete(boolean bl) {
        Core.settings.put("lunarians-auto-delete", (Object)bl);
    }

    public static int allRadius() {
        return AutoSetConfig.clamp(Core.settings.getInt("lunarians-econ-radius", 45), 10, 80);
    }

    private static void buildAll(final Table table) {
        UiText.section(table, AutoSetConfig.tr4("Auto All (recursos + f\u00e1bricas + unidades + schematics)", "Auto All (resources + factories + units + schematics)", "Auto All (recursos + f\u00e1bricas + unidades + esquemas)", "Auto All (\u0440\u0435\u0441\u0443\u0440\u0441\u044b + \u0437\u0430\u0432\u043e\u0434\u044b + \u044e\u043d\u0438\u0442\u044b + \u0441\u0445\u0435\u043c\u044b)"));
        table.add((CharSequence)AutoSetConfig.tr4("Com esta aba, GO (confirmar) abre o MAPEAMENTO: voc\u00ea voa at\u00e9 os min\u00e9rios (ficam marcados), pode marcar a areia na m\u00e3o e decide quando come\u00e7ar. Depois roda: cobre/chumbo, carv\u00e3o/areia, grafite, metavidro, sil\u00edcio, tit\u00e2nio, unidades, t\u00f3rio e por fim os schematics do Auto Dock.", "With this tab, GO (confirm) opens MAPPING: fly to the ore (it gets marked), mark sand by hand if you want, and choose when to start. Then it runs: copper/lead, coal/sand, graphite, metaglass, silicon, titanium, units, thorium and finally the Auto Dock schematics.", "Con esta pesta\u00f1a, GO (confirmar) abre el MAPEO: vuela al mineral (se marca), marca la arena a mano y decide cu\u00e1ndo empezar. Luego corre: cobre/plomo, carb\u00f3n/arena, grafito, metacristal, silicio, titanio, unidades, torio y al final los esquemas del Auto Dock.", "\u0412 \u044d\u0442\u043e\u0439 \u0432\u043a\u043b\u0430\u0434\u043a\u0435 GO (\u043f\u043e\u0434\u0442\u0432\u0435\u0440\u0434\u0438\u0442\u044c) \u043e\u0442\u043a\u0440\u044b\u0432\u0430\u0435\u0442 \u041a\u0410\u0420\u0422\u0418\u0420\u041e\u0412\u0410\u041d\u0418\u0415: \u043b\u0435\u0442\u0438\u0442\u0435 \u043a \u0440\u0443\u0434\u0435 (\u043e\u043d\u0430 \u043e\u0442\u043c\u0435\u0447\u0430\u0435\u0442\u0441\u044f), \u043e\u0442\u043c\u0435\u0447\u0430\u0439\u0442\u0435 \u043f\u0435\u0441\u043e\u043a \u0432\u0440\u0443\u0447\u043d\u0443\u044e \u0438 \u0441\u0430\u043c\u0438 \u0440\u0435\u0448\u0430\u0439\u0442\u0435, \u043a\u043e\u0433\u0434\u0430 \u043d\u0430\u0447\u0430\u0442\u044c.")).left().wrap().growX().padBottom(6.0f).row();
        AutoSetConfig.stepRow(table, AutoSetConfig.tr4("Raio m\u00ednimo da \u00e1rea (tiles)", "Min area radius (tiles)", "Radio m\u00ednimo del \u00e1rea (tiles)", "\u041c\u0438\u043d. \u0440\u0430\u0434\u0438\u0443\u0441 \u043e\u0431\u043b\u0430\u0441\u0442\u0438 (\u043a\u043b\u0435\u0442\u043e\u043a)"), (Prov<String>)((Prov)() -> String.valueOf(AutoSetConfig.allRadius())), () -> Core.settings.put("lunarians-econ-radius", (Object)(AutoSetConfig.allRadius() - 5)), () -> Core.settings.put("lunarians-econ-radius", (Object)(AutoSetConfig.allRadius() + 5)));
        table.add((CharSequence)AutoSetConfig.tr4("A \u00e1rea \u00e9 centrada na sua unidade e cresce para cobrir tudo que voc\u00ea marcou. As quantidades v\u00eam das abas Recursos e F\u00e1bricas.", "The area is centered on your unit and grows to cover everything you marked. Amounts come from the Resources and Factories tabs.", "El \u00e1rea se centra en tu unidad y crece para cubrir lo marcado. Las cantidades vienen de las pesta\u00f1as Recursos y F\u00e1bricas.", "\u041e\u0431\u043b\u0430\u0441\u0442\u044c \u0446\u0435\u043d\u0442\u0440\u0438\u0440\u0443\u0435\u0442\u0441\u044f \u043d\u0430 \u0432\u0430\u0448\u0435\u0439 \u0435\u0434\u0438\u043d\u0438\u0446\u0435 \u0438 \u0440\u0430\u0441\u0442\u0451\u0442 \u0434\u043e \u043e\u0442\u043c\u0435\u0447\u0435\u043d\u043d\u043e\u0433\u043e. \u041a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u0430 \u2014 \u0432\u043e \u0432\u043a\u043b\u0430\u0434\u043a\u0430\u0445 \u0420\u0435\u0441\u0443\u0440\u0441\u044b \u0438 \u0417\u0430\u0432\u043e\u0434\u044b.")).left().wrap().growX().row();
        UiText.section(table, AutoSetConfig.tr4("Auto delete e Auto power node", "Auto delete and Auto power node", "Auto delete y Auto power node", "\u0410\u0432\u0442\u043e\u0443\u0434\u0430\u043b\u0435\u043d\u0438\u0435 \u0438 \u0430\u0432\u0442\u043e-\u0443\u0437\u043b\u044b"));
        UiText.check(table, AutoSetConfig.tr4("Auto delete: remove esteiras/drills/nos pr\u00f3prios do Auto Set que bloqueiam um plano (m\u00e1x. 30 por execu\u00e7\u00e3o)", "Auto delete: removes Auto Set's own conveyors/drills/nodes that block a plan (max 30 per run)", "Auto delete: quita cintas/taladros/nodos propios del Auto Set que bloquean un plan (m\u00e1x. 30 por ejecuci\u00f3n)", "\u0410\u0432\u0442\u043e\u0443\u0434\u0430\u043b\u0435\u043d\u0438\u0435: \u0443\u0431\u0438\u0440\u0430\u0435\u0442 \u0441\u0432\u043e\u0438 \u043a\u043e\u043d\u0432\u0435\u0439\u0435\u0440\u044b/\u0431\u0443\u0440\u044b/\u0443\u0437\u043b\u044b Auto Set, \u043c\u0435\u0448\u0430\u044e\u0449\u0438\u0435 \u043f\u043b\u0430\u043d\u0443 (\u043c\u0430\u043a\u0441. 30)"), AutoSetConfig.autoDelete(), new Boolc(){

            public void get(boolean bl) {
                AutoSetConfig.setAutoDelete(bl);
                LunariansSettings.save();
            }
        }).left().row();
        UiText.check(table, AutoSetConfig.tr4("Auto power node: liga consumidores sem energia \u00e0 rede mais pr\u00f3xima", "Auto power node: links unpowered consumers to the nearest powered grid", "Auto power node: conecta consumidores sin energ\u00eda a la red m\u00e1s cercana", "\u0410\u0432\u0442\u043e-\u0443\u0437\u043b\u044b: \u043f\u043e\u0434\u043a\u043b\u044e\u0447\u0430\u044e\u0442 \u043f\u043e\u0442\u0440\u0435\u0431\u0438\u0442\u0435\u043b\u0435\u0439 \u0431\u0435\u0437 \u044d\u043d\u0435\u0440\u0433\u0438\u0438 \u043a \u0431\u043b\u0438\u0436\u0430\u0439\u0448\u0435\u0439 \u0441\u0435\u0442\u0438"), PowerLink.enabled(), new Boolc(){

            public void get(boolean bl) {
                PowerLink.setEnabled(bl);
                LunariansSettings.save();
            }
        }).left().row();
        UiText.section(table, ConveyorUpgrade.title());
        UiText.check(table, AutoSetConfig.tr4("Trocar esteiras comuns por esteiras de tit\u00e2nio perto da sua unidade", "Replace basic conveyors with titanium conveyors near your unit", "Cambiar cintas b\u00e1sicas por cintas de titanio cerca de tu unidad", "\u0417\u0430\u043c\u0435\u043d\u044f\u0442\u044c \u043e\u0431\u044b\u0447\u043d\u044b\u0435 \u043a\u043e\u043d\u0432\u0435\u0439\u0435\u0440\u044b \u0442\u0438\u0442\u0430\u043d\u043e\u0432\u044b\u043c\u0438 \u0440\u044f\u0434\u043e\u043c \u0441 \u0432\u0430\u043c\u0438"), ConveyorUpgrade.enabled(), new Boolc(){

            public void get(boolean bl) {
                ConveyorUpgrade.setEnabled(bl);
                LunariansSettings.save();
            }
        }).left().row();
        AutoSetConfig.stepRow(table, AutoSetConfig.tr4("Tit\u00e2nio m\u00ednimo no n\u00facleo", "Min titanium in core", "Titanio m\u00ednimo en el n\u00facleo", "\u041c\u0438\u043d. \u0442\u0438\u0442\u0430\u043d\u0430 \u0432 \u044f\u0434\u0440\u0435"), (Prov<String>)((Prov)() -> String.valueOf(ConveyorUpgrade.threshold())), () -> ConveyorUpgrade.setThreshold(ConveyorUpgrade.threshold() - 250), () -> ConveyorUpgrade.setThreshold(ConveyorUpgrade.threshold() + 250));
        UiText.section(table, AutoSetConfig.tr4("Resetar configura\u00e7\u00f5es para o modo", "Reset settings for mode", "Restablecer ajustes para el modo", "\u0421\u0431\u0440\u043e\u0441 \u043d\u0430\u0441\u0442\u0440\u043e\u0435\u043a \u043f\u043e\u0434 \u0440\u0435\u0436\u0438\u043c"));
        table.add((CharSequence)AutoSetConfig.tr4("Aplica quantidades, f\u00e1bricas, raio, mineradores e esteira de tit\u00e2nio ideais para cada modo. Voc\u00ea pode ajustar depois.", "Applies amounts, factories, radius, miner units and titanium conveyor suited to each mode. You can tweak afterwards.", "Aplica cantidades, f\u00e1bricas, radio, mineros y cinta de titanio adecuados a cada modo. Puedes ajustar despu\u00e9s.", "\u041f\u0440\u0438\u043c\u0435\u043d\u044f\u0435\u0442 \u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u0430, \u0437\u0430\u0432\u043e\u0434\u044b, \u0440\u0430\u0434\u0438\u0443\u0441, \u0448\u0430\u0445\u0442\u0451\u0440\u043e\u0432 \u0438 \u0442\u0438\u0442\u0430\u043d\u043e\u0432\u044b\u0439 \u043a\u043e\u043d\u0432\u0435\u0439\u0435\u0440 \u043f\u043e\u0434 \u043a\u0430\u0436\u0434\u044b\u0439 \u0440\u0435\u0436\u0438\u043c.")).left().wrap().growX().row();
        Table table2 = new Table();
        final String[] stringArray = new String[]{AutoSetConfig.tr4("Sobreviv\u00eancia", "Survival", "Supervivencia", "\u0412\u044b\u0436\u0438\u0432\u0430\u043d\u0438\u0435"), "PvP", "PvP Hexed"};
        for (int i = 0; i < 3; ++i) {
            final int n = i;
            table2.button(stringArray[i], new Runnable(){

                @Override
                public void run() {
                    AutoSetConfig.applyPreset(n);
                    LunariansSettings.save();
                    if (Vars.ui != null) {
                        Vars.ui.showInfoToast(AutoSetConfig.tr4("Configura\u00e7\u00e3o aplicada: ", "Settings applied: ", "Ajustes aplicados: ", "\u041d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0438 \u043f\u0440\u0438\u043c\u0435\u043d\u0435\u043d\u044b: ") + stringArray[n], 3.0f);
                    }
                    AutoSetConfig.build(table);
                }
            }).height(46.0f).growX().pad(2.0f);
        }
        table.add((Element)table2).growX().row();
    }

    public static void applyPreset(int n) {
        int n2;
        int[][] nArrayArray = new int[][]{{3, 1, 2, 2, 2, 0}, {3, 2, 1, 1, 2, 0}, {2, 1, 1, 1, 0, 0}};
        int[][] nArrayArray2 = new int[][]{{1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 0, 1}, {1, 1, 0, 1, 0, 1}};
        int[] nArray = new int[]{45, 30, 20};
        int[] nArray2 = new int[]{8, 4, 3};
        int[] nArray3 = new int[]{2000, 1500, 1000};
        int[] nArray4 = new int[]{3, 2, 2};
        int[] nArray5 = new int[]{6, 4, 3};
        for (n2 = 0; n2 < ORES.length; ++n2) {
            AutoSetConfig.setOreCount(n2, nArrayArray[n][n2]);
            AutoSetConfig.setDrillFor(ORES[n2], "");
        }
        for (n2 = 0; n2 < FACS.length; ++n2) {
            AutoSetConfig.setFacOn(n2, nArrayArray2[n][n2 * 2] != 0);
            AutoSetConfig.setFacCount(n2, nArrayArray2[n][n2 * 2 + 1]);
        }
        AutoSetConfig.setFacDrill(true);
        AutoSetConfig.setSpacing(nArray4[n]);
        AutoSetConfig.setCoreGap(nArray5[n]);
        AutoSetConfig.setFacGap(3);
        AutoSetConfig.setWallGap(2);
        AutoSetConfig.setFloorMax(16);
        AutoSetConfig.setSteamOn(false);
        AutoSetConfig.setAutoPower(true);
        AutoSetConfig.setBigNode(false);
        Core.settings.put("lunarians-econ-radius", (Object)nArray[n]);
        ConveyorUpgrade.setEnabled(true);
        ConveyorUpgrade.setThreshold(nArray3[n]);
        MinerUnits.setEnabled(true);
        MinerUnits.setProduce(true);
        MinerUnits.setCap(nArray2[n]);
        for (n2 = 0; n2 < MinerUnits.TYPES.length; ++n2) {
            MinerUnits.setTypeOn(n2, true);
        }
    }

    private static void buildDrills(final Table table) {
        UiText.section(table, AutoSetConfig.tr("Quantidade de drills por recurso", "Drills per resource"));
        for (int i = 0; i < ORES.length; ++i) {
            final int n = i;
            Table table2 = new Table();
            table2.add((CharSequence)AutoSetConfig.tr(ORE_NAME_PT[i], ORE_NAME_EN[i])).left().width(110.0f);
            table2.button("-", new Runnable(){

                @Override
                public void run() {
                    AutoSetConfig.setOreCount(n, AutoSetConfig.oreCount(n) - 1);
                    LunariansSettings.save();
                    AutoSetConfig.build(table);
                }
            }).size(46.0f, 40.0f).pad(2.0f);
            table2.add((CharSequence)String.valueOf(AutoSetConfig.oreCount(i))).width(48.0f).center().pad(4.0f);
            table2.button("+", new Runnable(){

                @Override
                public void run() {
                    AutoSetConfig.setOreCount(n, AutoSetConfig.oreCount(n) + 1);
                    LunariansSettings.save();
                    AutoSetConfig.build(table);
                }
            }).size(46.0f, 40.0f).pad(2.0f);
            table2.add((CharSequence)("[gray](" + AutoSetConfig.tr("padr\u00e3o", "default") + " " + ORE_DEF[i] + ")[]")).left().padLeft(8.0f);
            table.add((Element)table2).left().row();
            final String string = ORES[i];
            table.button(AutoSetConfig.tr("Drill: ", "Drill: ") + AutoSetConfig.drillLabel(string), new Runnable(){

                @Override
                public void run() {
                    ArrayList arrayList = AutoSetConfig.drillChoices(string);
                    String string3 = AutoSetConfig.hasCustomDrill(string) ? AutoSetConfig.drillFor(string) : "";
                    int n = arrayList.indexOf(string3);
                    String string2 = (String)arrayList.get((n + 1) % arrayList.size());
                    AutoSetConfig.setDrillFor(string, string2);
                    LunariansSettings.save();
                    AutoSetConfig.build(table);
                }
            }).height(40.0f).growX().padLeft(8.0f).row();
        }
        UiText.section(table, AutoSetConfig.tr("Isolamento e areia", "Isolation and sand"));
        AutoSetConfig.stepRow(table, AutoSetConfig.tr("Dist\u00e2ncia entre campos", "Field spacing"), (Prov<String>)((Prov)() -> String.valueOf(AutoSetConfig.spacing())), () -> AutoSetConfig.setSpacing(AutoSetConfig.spacing() - 1), () -> AutoSetConfig.setSpacing(AutoSetConfig.spacing() + 1));
        AutoSetConfig.stepRow(table, AutoSetConfig.tr("Tamanho m\u00e1x. do campo de areia (tiles)", "Max sand field size (tiles)"), (Prov<String>)((Prov)() -> String.valueOf(AutoSetConfig.floorMax())), () -> AutoSetConfig.setFloorMax(AutoSetConfig.floorMax() - 4), () -> AutoSetConfig.setFloorMax(AutoSetConfig.floorMax() + 4));
        UiText.section(table, AutoSetConfig.tr4("Steam Drill (carv\u00e3o 3x3 + \u00e1gua)", "Steam Drill (3x3 coal + water)", "Steam Drill (carb\u00f3n 3x3 + agua)", "Steam Drill (\u0443\u0433\u043e\u043b\u044c 3x3 + \u0432\u043e\u0434\u0430)"));
        UiText.check(table, AutoSetConfig.tr4("Ativar Steam Drill (schematic + bomba 2x2 na \u00e1gua)", "Enable Steam Drill (schematic + 2x2 pump on water)", "Activar Steam Drill (esquema + bomba 2x2 en el agua)", "\u0412\u043a\u043b\u044e\u0447\u0438\u0442\u044c Steam Drill (\u0441\u0445\u0435\u043c\u0430 + \u043d\u0430\u0441\u043e\u0441 2x2 \u043d\u0430 \u0432\u043e\u0434\u0435)"), AutoSetConfig.steamOn(), new Boolc(){

            public void get(boolean bl) {
                AutoSetConfig.setSteamOn(bl);
                LunariansSettings.save();
            }
        }).left().row();
        AutoSetConfig.stepRow(table, AutoSetConfig.tr4("Quantidade de Steam Drills", "Steam Drill amount", "Cantidad de Steam Drills", "\u041a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e Steam Drill"), (Prov<String>)((Prov)() -> String.valueOf(AutoSetConfig.steamCount())), () -> AutoSetConfig.setSteamCount(AutoSetConfig.steamCount() - 1), () -> AutoSetConfig.setSteamCount(AutoSetConfig.steamCount() + 1));
        AutoSetConfig.stepRow(table, AutoSetConfig.tr4("M\u00edn. tiles de carv\u00e3o no 3x3", "Min coal tiles in the 3x3", "M\u00edn. casillas de carb\u00f3n en el 3x3", "\u041c\u0438\u043d. \u043a\u043b\u0435\u0442\u043e\u043a \u0443\u0433\u043b\u044f \u0432 3x3"), (Prov<String>)((Prov)() -> String.valueOf(AutoSetConfig.steamMinCoal())), () -> AutoSetConfig.setSteamMinCoal(AutoSetConfig.steamMinCoal() - 1), () -> AutoSetConfig.setSteamMinCoal(AutoSetConfig.steamMinCoal() + 1));
        UiText.check(table, AutoSetConfig.tr4("Auto conectar energia (power node + pain\u00e9is para ligar a bomba)", "Auto connect power (power nodes + solar panels to start the pump)", "Auto conectar energ\u00eda (nodos + paneles para arrancar la bomba)", "\u0410\u0432\u0442\u043e\u043f\u043e\u0434\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u0435 \u044d\u043d\u0435\u0440\u0433\u0438\u0438 (\u0443\u0437\u043b\u044b + \u0441\u043e\u043b\u043d\u0435\u0447\u043d\u044b\u0435 \u043f\u0430\u043d\u0435\u043b\u0438 \u0434\u043b\u044f \u043d\u0430\u0441\u043e\u0441\u0430)"), AutoSetConfig.autoPower(), new Boolc(){

            public void get(boolean bl) {
                AutoSetConfig.setAutoPower(bl);
                LunariansSettings.save();
            }
        }).left().row();
        UiText.check(table, AutoSetConfig.tr4("Usar Large Power Node se houver recursos", "Use Large Power Node when resources allow", "Usar Large Power Node si hay recursos", "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c Large Power Node, \u0435\u0441\u043b\u0438 \u0445\u0432\u0430\u0442\u0430\u0435\u0442 \u0440\u0435\u0441\u0443\u0440\u0441\u043e\u0432"), AutoSetConfig.bigNode(), new Boolc(){

            public void get(boolean bl) {
                AutoSetConfig.setBigNode(bl);
                LunariansSettings.save();
            }
        }).left().row();
        table.button(AutoSetConfig.tr("Restaurar padr\u00e3o (3 / 1 / 2 / 2 / 0 / 0)", "Restore default (3 / 1 / 2 / 2 / 0 / 0)"), new Runnable(){

            @Override
            public void run() {
                for (int i = 0; i < ORES.length; ++i) {
                    AutoSetConfig.setOreCount(i, ORE_DEF[i]);
                    AutoSetConfig.setDrillFor(ORES[i], "");
                }
                LunariansSettings.save();
                AutoSetConfig.build(table);
            }
        }).height(44.0f).growX().padTop(8.0f).row();
    }

    private static void buildFactories(final Table table) {
        UiText.section(table, AutoSetConfig.tr("F\u00e1bricas (schematics virados para cima)", "Factories (schematics face up)"));
        table.add((CharSequence)AutoSetConfig.tr("Entradas: os 2 conveyors de baixo (esq. e dir.). Sa\u00edda: topo, ligada ao n\u00facleo.", "Item inputs: the bottom conveyors. Output: top, routed to the core.")).left().wrap().growX().padBottom(6.0f).row();
        for (int i = 0; i < FACS.length; ++i) {
            final int n = i;
            Table table2 = new Table();
            UiText.check(table2, AutoSetConfig.tr(FAC_NAME_PT[i], FAC_NAME_EN[i]), AutoSetConfig.facOn(i), new Boolc(){

                public void get(boolean bl) {
                    AutoSetConfig.setFacOn(n, bl);
                    LunariansSettings.save();
                }
            }).left().width(170.0f);
            table2.button("-", new Runnable(){

                @Override
                public void run() {
                    AutoSetConfig.setFacCount(n, AutoSetConfig.facCount(n) - 1);
                    LunariansSettings.save();
                    AutoSetConfig.build(table);
                }
            }).size(46.0f, 40.0f).pad(2.0f);
            table2.add((CharSequence)("x" + AutoSetConfig.facCount(i))).width(48.0f).center().pad(4.0f);
            table2.button("+", new Runnable(){

                @Override
                public void run() {
                    AutoSetConfig.setFacCount(n, AutoSetConfig.facCount(n) + 1);
                    LunariansSettings.save();
                    AutoSetConfig.build(table);
                }
            }).size(46.0f, 40.0f).pad(2.0f);
            table.add((Element)table2).left().row();
        }
        AutoSetConfig.stepRow(table, AutoSetConfig.tr("Dist\u00e2ncia m\u00ednima do n\u00facleo (tiles)", "Min distance from core (tiles)"), (Prov<String>)((Prov)() -> String.valueOf(AutoSetConfig.coreGap())), () -> AutoSetConfig.setCoreGap(AutoSetConfig.coreGap() - 1), () -> AutoSetConfig.setCoreGap(AutoSetConfig.coreGap() + 1));
        AutoSetConfig.stepRow(table, AutoSetConfig.tr("Dist\u00e2ncia m\u00ednima das paredes (tiles)", "Min distance from walls (tiles)"), (Prov<String>)((Prov)() -> String.valueOf(AutoSetConfig.wallGap())), () -> AutoSetConfig.setWallGap(AutoSetConfig.wallGap() - 1), () -> AutoSetConfig.setWallGap(AutoSetConfig.wallGap() + 1));
        AutoSetConfig.stepRow(table, AutoSetConfig.tr("Dist\u00e2ncia entre f\u00e1bricas (tiles)", "Gap between factories (tiles)"), (Prov<String>)((Prov)() -> String.valueOf(AutoSetConfig.facGap())), () -> AutoSetConfig.setFacGap(AutoSetConfig.facGap() - 1), () -> AutoSetConfig.setFacGap(AutoSetConfig.facGap() + 1));
        UiText.check(table, AutoSetConfig.tr("Autodrill nas entradas: drills em min\u00e9rio ligados a cada entrada (1 recurso por entrada)", "Autodrill on inputs: ore drills wired to each input (1 resource per input)"), AutoSetConfig.facDrill(), new Boolc(){

            public void get(boolean bl) {
                AutoSetConfig.setFacDrill(bl);
                LunariansSettings.save();
            }
        }).left().padTop(6.0f).row();
        table.add((CharSequence)AutoSetConfig.tr("Ordem: 1) m\u00e1quinas  2) autodrill  3) conex\u00e3o espec\u00edfica em cada entrada.", "Order: 1) machines  2) autodrill  3) specific connection to each input.")).left().wrap().growX().row();
        table.add((CharSequence)AutoSetConfig.tr("Use GO, toque no CENTRO e depois no raio: as f\u00e1bricas v\u00e3o para um espa\u00e7o livre dentro do c\u00edrculo.", "Press GO, tap the CENTER then the radius: factories go to free space inside the circle.")).left().wrap().growX().padTop(8.0f).row();
    }
}

