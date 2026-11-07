package lunarians;

import arc.Core;
import arc.func.Boolc;
import arc.func.Prov;
import arc.scene.Element;
import arc.scene.ui.layout.Table;
import java.util.ArrayList;
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
  
  public static final String[] ORES = new String[] { "copper", "lead", "coal", "sand", "titanium", "thorium" };
  
  public static final int[] ORE_DEF = new int[] { 3, 1, 2, 2, 0, 0 };
  
  public static final String[] ORE_NAME_EN = new String[] { "Copper", "Lead", "Coal", "Sand", "Titanium", "Thorium" };
  
  public static final String[] ORE_NAME_PT = new String[] { "Cobre", "Chumbo", "Carvão", "Areia", "Titânio", "Tório" };
  
  public static final String[] FACS = new String[] { "graphite", "silicon", "metaglass" };
  
  public static final String[] FAC_NAME_EN = new String[] { "Graphite", "Silicon", "Metaglass" };
  
  public static final String[] FAC_NAME_PT = new String[] { "Grafite", "Silicone", "Metavidro" };
  
  public static final String[] FAC_SCHEM = new String[] { "bXNjaAF4nCWM4QrCMAyEr10dqDjwQfYoPoH4o844C1tb2mwi4rvbdJBw4S73oUVrcBiCZ/J8sRH6+0M72TtNGfp6g/F2JuztwiG+HBOOD8pDcpFd8ECDbky2Jn1MlDPO7Nh6t8x9oa70CQmnsFJ6TuHdj7YQgK4slIwqBCViNtmJaEl1ESV/ckBMAy3vNWu2eiMmpIzKqDAj5h9WRyUl", "bXNjaAF4nCWMawrCMBCEJw8rKCoepEfxBOKPWFcJ5FGSbUXEu5ttYZdvmdkZdOgsdkNOTIkvboT+/tAFd6dQoa832OQiYesmztVH7B9Uh+JH9jkBBqfqg2/xvkYKTAVn9uySn2Lf1Jk+ueCQZyrPkN/9yzEBOLaFklGtQgnsio1Ai6sblPzJAREttLwvnlnjRkRIGEvHUmZF/AMCcCS/", "bXNjaAF4nCWMawrCQAyEZ7drEUXBg/QonkD8EWuUxX2UNq2IeHc3W0j4wkxm0KJ12PU5CSc50wD7/aENdOMwwV6ucIkiY0uz5MhC2N956kc/iM8JaOBePiScxAslP8euVC38ySMOeeHxEfK7e5IwgGNZGB1TckbhVmwUVl1bYPRPD6joYPW9es0ab1SEhlE7aplT8Q/6UyCn" };
  
  private static boolean pt() {
    return "pt".equals(LunariansI18n.getLanguage());
  }
  
  private static String tr(String paramString1, String paramString2) {
    return pt() ? paramString1 : paramString2;
  }
  
  public static int mode() {
    return Math.max(0, Math.min(2, Core.settings.getInt("lunarians-as-mode", 0)));
  }
  
  public static void setMode(int paramInt) {
    Core.settings.put("lunarians-as-mode", Integer.valueOf(paramInt));
  }
  
  public static int oreCount(int paramInt) {
    return clamp(Core.settings.getInt("lunarians-as-ore-" + ORES[paramInt], ORE_DEF[paramInt]), 0, 12);
  }
  
  public static void setOreCount(int paramInt1, int paramInt2) {
    Core.settings.put("lunarians-as-ore-" + ORES[paramInt1], Integer.valueOf(clamp(paramInt2, 0, 12)));
  }
  
  public static boolean facOn(int paramInt) {
    return (Core.settings.getInt("lunarians-as-fac-on-" + FACS[paramInt], 1) != 0);
  }
  
  public static void setFacOn(int paramInt, boolean paramBoolean) {
    Core.settings.put("lunarians-as-fac-on-" + FACS[paramInt], Integer.valueOf(paramBoolean ? 1 : 0));
  }
  
  public static int facCount(int paramInt) {
    return clamp(Core.settings.getInt("lunarians-as-fac-n-" + FACS[paramInt], 1), 1, 6);
  }
  
  public static void setFacCount(int paramInt1, int paramInt2) {
    Core.settings.put("lunarians-as-fac-n-" + FACS[paramInt1], Integer.valueOf(clamp(paramInt2, 1, 6)));
  }
  
  public static boolean facDrill() {
    return (Core.settings.getInt("lunarians-as-fac-drill", 1) != 0);
  }
  
  public static void setFacDrill(boolean paramBoolean) {
    Core.settings.put("lunarians-as-fac-drill", Integer.valueOf(paramBoolean ? 1 : 0));
  }
  
  public static String drillFor(String paramString) {
    String str = Core.settings.getString("lunarians-as-drill-" + paramString, "");
    if (str == null || str.isEmpty()) {
      if ("titanium".equals(paramString))
        return "pneumatic-drill"; 
      if ("thorium".equals(paramString))
        return "laser-drill"; 
      return "";
    } 
    return str;
  }
  
  public static void setDrillFor(String paramString1, String paramString2) {
    Core.settings.put("lunarians-as-drill-" + paramString1, (paramString2 == null) ? "" : paramString2);
  }
  
  private static boolean hasCustomDrill(String paramString) {
    String str = Core.settings.getString("lunarians-as-drill-" + paramString, "");
    return (str != null && !str.isEmpty());
  }
  
  private static ArrayList<String> drillChoices(String paramString) {
    ArrayList<String> arrayList = new ArrayList();
    arrayList.add("");
    Item item = Vars.content.item(paramString);
    for (Block block : Vars.content.blocks()) {
      if (!(block instanceof Drill))
        continue; 
      Drill drill = (Drill)block;
      if (item != null && drill.tier < item.hardness)
        continue; 
      arrayList.add(drill.name);
    } 
    return arrayList;
  }
  
  private static String drillLabel(String paramString) {
    String str1 = hasCustomDrill(paramString) ? drillFor(paramString) : "";
    String str2 = drillFor(paramString);
    if (str2 == null || str2.isEmpty())
      str2 = LunariansSettings.drillBlock; 
    Block block = (str2 == null) ? null : Vars.content.block(str2);
    String str3 = (block == null) ? String.valueOf(str2) : (block.localizedName + " " + block.size + "x" + block.size);
    String str4 = str3;
    if (str1.isEmpty())
      return tr("padrão", "default") + " (" + str3 + ")"; 
    return str3;
  }
  
  public static String tr4(String paramString1, String paramString2, String paramString3, String paramString4) {
    String str = LunariansI18n.getLanguage();
    if ("pt".equals(str))
      return paramString1; 
    if ("es".equals(str))
      return paramString3; 
    if ("ru".equals(str))
      return paramString4; 
    return paramString2;
  }
  
  public static boolean steamOn() {
    return (Core.settings.getInt("lunarians-as-steam", 0) != 0);
  }
  
  public static void setSteamOn(boolean paramBoolean) {
    Core.settings.put("lunarians-as-steam", Integer.valueOf(paramBoolean ? 1 : 0));
  }
  
  public static int steamCount() {
    return clamp(Core.settings.getInt("lunarians-as-steam-n", 1), 1, 6);
  }
  
  public static void setSteamCount(int paramInt) {
    Core.settings.put("lunarians-as-steam-n", Integer.valueOf(clamp(paramInt, 1, 6)));
  }
  
  public static int steamMinCoal() {
    return clamp(Core.settings.getInt("lunarians-as-steam-coal", 9), 5, 9);
  }
  
  public static void setSteamMinCoal(int paramInt) {
    Core.settings.put("lunarians-as-steam-coal", Integer.valueOf(clamp(paramInt, 5, 9)));
  }
  
  public static boolean autoPower() {
    return (Core.settings.getInt("lunarians-as-power", 1) != 0);
  }
  
  public static void setAutoPower(boolean paramBoolean) {
    Core.settings.put("lunarians-as-power", Integer.valueOf(paramBoolean ? 1 : 0));
  }
  
  public static boolean bigNode() {
    return (Core.settings.getInt("lunarians-as-bignode", 0) != 0);
  }
  
  public static void setBigNode(boolean paramBoolean) {
    Core.settings.put("lunarians-as-bignode", Integer.valueOf(paramBoolean ? 1 : 0));
  }
  
  private static int clamp(int paramInt1, int paramInt2, int paramInt3) {
    return Math.max(paramInt2, Math.min(paramInt3, paramInt1));
  }
  
  public static int spacing() {
    return clamp(Core.settings.getInt("lunarians-set-spacing", 3), 1, 10);
  }
  
  public static void setSpacing(int paramInt) {
    Core.settings.put("lunarians-set-spacing", Integer.valueOf(clamp(paramInt, 1, 10)));
  }
  
  public static int wallGap() {
    return clamp(Core.settings.getInt("lunarians-set-wall-gap", 2), 0, 10);
  }
  
  public static void setWallGap(int paramInt) {
    Core.settings.put("lunarians-set-wall-gap", Integer.valueOf(clamp(paramInt, 0, 10)));
  }
  
  public static int facGap() {
    return clamp(Core.settings.getInt("lunarians-set-fac-gap", 3), 0, 12);
  }
  
  public static void setFacGap(int paramInt) {
    Core.settings.put("lunarians-set-fac-gap", Integer.valueOf(clamp(paramInt, 0, 12)));
  }
  
  public static int coreGap() {
    return clamp(Core.settings.getInt("lunarians-set-core-gap", 6), 0, 30);
  }
  
  public static void setCoreGap(int paramInt) {
    Core.settings.put("lunarians-set-core-gap", Integer.valueOf(clamp(paramInt, 0, 30)));
  }
  
  public static int floorMax() {
    return clamp(Core.settings.getInt("lunarians-set-floor-max", 16), 4, 64);
  }
  
  public static void setFloorMax(int paramInt) {
    Core.settings.put("lunarians-set-floor-max", Integer.valueOf(clamp(paramInt, 4, 64)));
  }
  
  private static void stepRow(Table paramTable, String paramString, Prov<String> paramProv, Runnable paramRunnable1, Runnable paramRunnable2) {
    Table table = new Table();
    table.add(paramString).left().width(190.0F).wrap();
    table.button("-", () -> {
          paramRunnable.run();
          LunariansSettings.save();
          build(paramTable);
        }).size(46.0F, 40.0F).pad(2.0F);
    table.add((CharSequence)paramProv.get()).width(48.0F).center().pad(4.0F);
    table.button("+", () -> {
          paramRunnable.run();
          LunariansSettings.save();
          build(paramTable);
        }).size(46.0F, 40.0F).pad(2.0F);
    paramTable.add((Element)table).left().row();
  }
  
  public static String[] oreOrder() {
    ArrayList<String> arrayList = new ArrayList();
    int i;
    for (i = 0; i < oreCount(0); i++)
      arrayList.add("copper"); 
    for (i = 0; i < oreCount(1); i++)
      arrayList.add("lead"); 
    i = oreCount(2);
    int j = oreCount(3);
    while (i > 0 || j > 0) {
      if (i > 0) {
        arrayList.add("coal");
        i--;
      } 
      if (j <= 0)
        continue; 
      arrayList.add("sand");
      j--;
    } 
    for (i = 0; i < oreCount(4); i++)
      arrayList.add("titanium"); 
    for (i = 0; i < oreCount(5); i++)
      arrayList.add("thorium"); 
    return arrayList.<String>toArray(new String[0]);
  }
  
  public static String[] factoryQueue() {
    ArrayList<String> arrayList = new ArrayList();
    for (byte b = 0; b < FACS.length; b++) {
      if (facOn(b))
        for (byte b1 = 0; b1 < facCount(b); b1++)
          arrayList.add(FACS[b]);  
    } 
    return arrayList.<String>toArray(new String[0]);
  }
  
  public static String schematicFor(String paramString) {
    for (byte b = 0; b < FACS.length; ) {
      if (!FACS[b].equals(paramString)) {
        b++;
        continue;
      } 
      return FAC_SCHEM[b];
    } 
    return null;
  }
  
  public static void build(final Table table) {
    table.clear();
    table.defaults().growX().padBottom(4.0F);
    int i = mode();
    Table table = new Table();
    table.button(((i == 0) ? "[accent]> " : "") + tr4("Recursos", "Resources", "Recursos", "Ресурсы") + ((i == 0) ? "[]" : ""), new Runnable() {
          public void run() {
            AutoSetConfig.setMode(0);
            LunariansSettings.save();
            AutoSetConfig.build(table);
          }
        }).height(46.0F).growX().pad(2.0F);
    table.button(((i == 1) ? "[accent]> " : "") + tr4("Fábricas", "Factories", "Fábricas", "Заводы") + ((i == 1) ? "[]" : ""), new Runnable() {
          public void run() {
            AutoSetConfig.setMode(1);
            LunariansSettings.save();
            AutoSetConfig.build(table);
          }
        }).height(46.0F).growX().pad(2.0F);
    table.button(((i == 2) ? "[accent]> " : "") + "Auto All" + ((i == 2) ? "[]" : ""), new Runnable() {
          public void run() {
            AutoSetConfig.setMode(2);
            LunariansSettings.save();
            AutoSetConfig.build(table);
          }
        }).height(46.0F).growX().pad(2.0F);
    table.add((Element)table).growX().row();
    table.add(tr("Aba escolhida = o que o botão GO do Auto Set faz.", "Selected tab = what the Auto Set GO button does.")).left().wrap().growX().padBottom(6.0F).row();
    if (i == 2) {
      buildAll(table);
    } else if (i == 0) {
      buildDrills(table);
    } else {
      buildFactories(table);
    } 
  }
  
  public static boolean autoDelete() {
    return Core.settings.getBool("lunarians-auto-delete", true);
  }
  
  public static void setAutoDelete(boolean paramBoolean) {
    Core.settings.put("lunarians-auto-delete", Boolean.valueOf(paramBoolean));
  }
  
  public static int allRadius() {
    return clamp(Core.settings.getInt("lunarians-econ-radius", 45), 10, 80);
  }
  
  private static void buildAll(final Table table) {
    UiText.section(table, tr4("Auto All (recursos + fábricas + unidades + schematics)", "Auto All (resources + factories + units + schematics)", "Auto All (recursos + fábricas + unidades + esquemas)", "Auto All (ресурсы + заводы + юниты + схемы)"));
    table.add(tr4("Com esta aba, GO (confirmar) abre o MAPEAMENTO: você voa até os minérios (ficam marcados), pode marcar a areia na mão e decide quando começar. Depois roda: cobre/chumbo, carvão/areia, grafite, metavidro, silício, titânio, unidades, tório e por fim os schematics do Auto Dock.", "With this tab, GO (confirm) opens MAPPING: fly to the ore (it gets marked), mark sand by hand if you want, and choose when to start. Then it runs: copper/lead, coal/sand, graphite, metaglass, silicon, titanium, units, thorium and finally the Auto Dock schematics.", "Con esta pestaña, GO (confirmar) abre el MAPEO: vuela al mineral (se marca), marca la arena a mano y decide cuándo empezar. Luego corre: cobre/plomo, carbón/arena, grafito, metacristal, silicio, titanio, unidades, torio y al final los esquemas del Auto Dock.", "В этой вкладке GO (подтвердить) открывает КАРТИРОВАНИЕ: летите к руде (она отмечается), отмечайте песок вручную и сами решайте, когда начать.")).left().wrap().growX().padBottom(6.0F).row();
    stepRow(table, tr4("Raio mínimo da área (tiles)", "Min area radius (tiles)", "Radio mínimo del área (tiles)", "Мин. радиус области (клеток)"), () -> String.valueOf(allRadius()), () -> Core.settings.put("lunarians-econ-radius", Integer.valueOf(allRadius() - 5)), () -> Core.settings.put("lunarians-econ-radius", Integer.valueOf(allRadius() + 5)));
    table.add(tr4("A área é centrada na sua unidade e cresce para cobrir tudo que você marcou. As quantidades vêm das abas Recursos e Fábricas.", "The area is centered on your unit and grows to cover everything you marked. Amounts come from the Resources and Factories tabs.", "El área se centra en tu unidad y crece para cubrir lo marcado. Las cantidades vienen de las pestañas Recursos y Fábricas.", "Область центрируется на вашей единице и растёт до отмеченного. Количества — во вкладках Ресурсы и Заводы.")).left().wrap().growX().row();
    UiText.section(table, tr4("Auto delete e Auto power node", "Auto delete and Auto power node", "Auto delete y Auto power node", "Автоудаление и авто-узлы"));
    UiText.check(table, tr4("Auto delete: remove esteiras/drills/nos próprios do Auto Set que bloqueiam um plano (máx. 30 por execução)", "Auto delete: removes Auto Set's own conveyors/drills/nodes that block a plan (max 30 per run)", "Auto delete: quita cintas/taladros/nodos propios del Auto Set que bloquean un plan (máx. 30 por ejecución)", "Автоудаление: убирает свои конвейеры/буры/узлы Auto Set, мешающие плану (макс. 30)"), autoDelete(), new Boolc() {
          public void get(boolean param1Boolean) {
            AutoSetConfig.setAutoDelete(param1Boolean);
            LunariansSettings.save();
          }
        }).left().row();
    UiText.check(table, tr4("Auto power node: liga consumidores sem energia à rede mais próxima", "Auto power node: links unpowered consumers to the nearest powered grid", "Auto power node: conecta consumidores sin energía a la red más cercana", "Авто-узлы: подключают потребителей без энергии к ближайшей сети"), PowerLink.enabled(), new Boolc() {
          public void get(boolean param1Boolean) {
            PowerLink.setEnabled(param1Boolean);
            LunariansSettings.save();
          }
        }).left().row();
    UiText.section(table, ConveyorUpgrade.title());
    UiText.check(table, tr4("Trocar esteiras comuns por esteiras de titânio perto da sua unidade", "Replace basic conveyors with titanium conveyors near your unit", "Cambiar cintas básicas por cintas de titanio cerca de tu unidad", "Заменять обычные конвейеры титановыми рядом с вами"), ConveyorUpgrade.enabled(), new Boolc() {
          public void get(boolean param1Boolean) {
            ConveyorUpgrade.setEnabled(param1Boolean);
            LunariansSettings.save();
          }
        }).left().row();
    stepRow(table, tr4("Titânio mínimo no núcleo", "Min titanium in core", "Titanio mínimo en el núcleo", "Мин. титана в ядре"), () -> String.valueOf(ConveyorUpgrade.threshold()), () -> ConveyorUpgrade.setThreshold(ConveyorUpgrade.threshold() - 250), () -> ConveyorUpgrade.setThreshold(ConveyorUpgrade.threshold() + 250));
    UiText.section(table, tr4("Resetar configurações para o modo", "Reset settings for mode", "Restablecer ajustes para el modo", "Сброс настроек под режим"));
    table.add(tr4("Aplica quantidades, fábricas, raio, mineradores e esteira de titânio ideais para cada modo. Você pode ajustar depois.", "Applies amounts, factories, radius, miner units and titanium conveyor suited to each mode. You can tweak afterwards.", "Aplica cantidades, fábricas, radio, mineros y cinta de titanio adecuados a cada modo. Puedes ajustar después.", "Применяет количества, заводы, радиус, шахтёров и титановый конвейер под каждый режим.")).left().wrap().growX().row();
    Table table = new Table();
    final String[] names = { tr4("Sobrevivência", "Survival", "Supervivencia", "Выживание"), "PvP", "PvP Hexed" };
    for (byte b = 0; b < 3; b++) {
      final byte mm = b;
      table.button(arrayOfString[b], new Runnable() {
            public void run() {
              AutoSetConfig.applyPreset(mm);
              LunariansSettings.save();
              if (Vars.ui != null)
                Vars.ui.showInfoToast(AutoSetConfig.tr4("Configuração aplicada: ", "Settings applied: ", "Ajustes aplicados: ", "Настройки применены: ") + names[mm], 3.0F); 
              AutoSetConfig.build(table);
            }
          }).height(46.0F).growX().pad(2.0F);
    } 
    table.add((Element)table).growX().row();
  }
  
  public static void applyPreset(int paramInt) {
    int[][] arrayOfInt1 = { { 3, 1, 2, 2, 2, 0 }, { 3, 2, 1, 1, 2, 0 }, { 2, 1, 1, 1, 0, 0 } };
    int[][] arrayOfInt2 = { { 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 0, 1 }, { 1, 1, 0, 1, 0, 1 } };
    int[] arrayOfInt3 = { 45, 30, 20 };
    int[] arrayOfInt4 = { 8, 4, 3 };
    int[] arrayOfInt5 = { 2000, 1500, 1000 };
    int[] arrayOfInt6 = { 3, 2, 2 };
    int[] arrayOfInt7 = { 6, 4, 3 };
    byte b;
    for (b = 0; b < ORES.length; b++) {
      setOreCount(b, arrayOfInt1[paramInt][b]);
      setDrillFor(ORES[b], "");
    } 
    for (b = 0; b < FACS.length; b++) {
      setFacOn(b, (arrayOfInt2[paramInt][b * 2] != 0));
      setFacCount(b, arrayOfInt2[paramInt][b * 2 + 1]);
    } 
    setFacDrill(true);
    setSpacing(arrayOfInt6[paramInt]);
    setCoreGap(arrayOfInt7[paramInt]);
    setFacGap(3);
    setWallGap(2);
    setFloorMax(16);
    setSteamOn(false);
    setAutoPower(true);
    setBigNode(false);
    Core.settings.put("lunarians-econ-radius", Integer.valueOf(arrayOfInt3[paramInt]));
    ConveyorUpgrade.setEnabled(true);
    ConveyorUpgrade.setThreshold(arrayOfInt5[paramInt]);
    MinerUnits.setEnabled(true);
    MinerUnits.setProduce(true);
    MinerUnits.setCap(arrayOfInt4[paramInt]);
    for (b = 0; b < MinerUnits.TYPES.length; b++)
      MinerUnits.setTypeOn(b, true); 
  }
  
  private static void buildDrills(final Table table) {
    UiText.section(table, tr("Quantidade de drills por recurso", "Drills per resource"));
    for (byte b = 0; b < ORES.length; b++) {
      final byte n = b;
      Table table = new Table();
      table.add(tr(ORE_NAME_PT[b], ORE_NAME_EN[b])).left().width(110.0F);
      table.button("-", new Runnable() {
            public void run() {
              AutoSetConfig.setOreCount(n, AutoSetConfig.oreCount(n) - 1);
              LunariansSettings.save();
              AutoSetConfig.build(table);
            }
          }).size(46.0F, 40.0F).pad(2.0F);
      table.add(String.valueOf(oreCount(b))).width(48.0F).center().pad(4.0F);
      table.button("+", new Runnable() {
            public void run() {
              AutoSetConfig.setOreCount(n, AutoSetConfig.oreCount(n) + 1);
              LunariansSettings.save();
              AutoSetConfig.build(table);
            }
          }).size(46.0F, 40.0F).pad(2.0F);
      table.add("[gray](" + tr("padrão", "default") + " " + ORE_DEF[b] + ")[]").left().padLeft(8.0F);
      table.add((Element)table).left().row();
      final String string = ORES[b];
      table.button(tr("Drill: ", "Drill: ") + drillLabel(str), new Runnable() {
            public void run() {
              ArrayList<String> arrayList = AutoSetConfig.drillChoices(string);
              String str1 = AutoSetConfig.hasCustomDrill(string) ? AutoSetConfig.drillFor(string) : "";
              int i = arrayList.indexOf(str1);
              String str2 = arrayList.get((i + 1) % arrayList.size());
              AutoSetConfig.setDrillFor(string, str2);
              LunariansSettings.save();
              AutoSetConfig.build(table);
            }
          }).height(40.0F).growX().padLeft(8.0F).row();
    } 
    UiText.section(table, tr("Isolamento e areia", "Isolation and sand"));
    stepRow(table, tr("Distância entre campos", "Field spacing"), () -> String.valueOf(spacing()), () -> setSpacing(spacing() - 1), () -> setSpacing(spacing() + 1));
    stepRow(table, tr("Tamanho máx. do campo de areia (tiles)", "Max sand field size (tiles)"), () -> String.valueOf(floorMax()), () -> setFloorMax(floorMax() - 4), () -> setFloorMax(floorMax() + 4));
    UiText.section(table, tr4("Steam Drill (carvão 3x3 + água)", "Steam Drill (3x3 coal + water)", "Steam Drill (carbón 3x3 + agua)", "Steam Drill (уголь 3x3 + вода)"));
    UiText.check(table, tr4("Ativar Steam Drill (schematic + bomba 2x2 na água)", "Enable Steam Drill (schematic + 2x2 pump on water)", "Activar Steam Drill (esquema + bomba 2x2 en el agua)", "Включить Steam Drill (схема + насос 2x2 на воде)"), steamOn(), new Boolc() {
          public void get(boolean param1Boolean) {
            AutoSetConfig.setSteamOn(param1Boolean);
            LunariansSettings.save();
          }
        }).left().row();
    stepRow(table, tr4("Quantidade de Steam Drills", "Steam Drill amount", "Cantidad de Steam Drills", "Количество Steam Drill"), () -> String.valueOf(steamCount()), () -> setSteamCount(steamCount() - 1), () -> setSteamCount(steamCount() + 1));
    stepRow(table, tr4("Mín. tiles de carvão no 3x3", "Min coal tiles in the 3x3", "Mín. casillas de carbón en el 3x3", "Мин. клеток угля в 3x3"), () -> String.valueOf(steamMinCoal()), () -> setSteamMinCoal(steamMinCoal() - 1), () -> setSteamMinCoal(steamMinCoal() + 1));
    UiText.check(table, tr4("Auto conectar energia (power node + painéis para ligar a bomba)", "Auto connect power (power nodes + solar panels to start the pump)", "Auto conectar energía (nodos + paneles para arrancar la bomba)", "Автоподключение энергии (узлы + солнечные панели для насоса)"), autoPower(), new Boolc() {
          public void get(boolean param1Boolean) {
            AutoSetConfig.setAutoPower(param1Boolean);
            LunariansSettings.save();
          }
        }).left().row();
    UiText.check(table, tr4("Usar Large Power Node se houver recursos", "Use Large Power Node when resources allow", "Usar Large Power Node si hay recursos", "Использовать Large Power Node, если хватает ресурсов"), bigNode(), new Boolc() {
          public void get(boolean param1Boolean) {
            AutoSetConfig.setBigNode(param1Boolean);
            LunariansSettings.save();
          }
        }).left().row();
    table.button(tr("Restaurar padrão (3 / 1 / 2 / 2 / 0 / 0)", "Restore default (3 / 1 / 2 / 2 / 0 / 0)"), new Runnable() {
          public void run() {
            for (byte b = 0; b < AutoSetConfig.ORES.length; b++) {
              AutoSetConfig.setOreCount(b, AutoSetConfig.ORE_DEF[b]);
              AutoSetConfig.setDrillFor(AutoSetConfig.ORES[b], "");
            } 
            LunariansSettings.save();
            AutoSetConfig.build(table);
          }
        }).height(44.0F).growX().padTop(8.0F).row();
  }
  
  private static void buildFactories(final Table table) {
    UiText.section(table, tr("Fábricas (schematics virados para cima)", "Factories (schematics face up)"));
    table.add(tr("Entradas: os 2 conveyors de baixo (esq. e dir.). Saída: topo, ligada ao núcleo.", "Item inputs: the bottom conveyors. Output: top, routed to the core.")).left().wrap().growX().padBottom(6.0F).row();
    for (byte b = 0; b < FACS.length; b++) {
      final byte n = b;
      Table table = new Table();
      UiText.check(table, tr(FAC_NAME_PT[b], FAC_NAME_EN[b]), facOn(b), new Boolc() {
            public void get(boolean param1Boolean) {
              AutoSetConfig.setFacOn(n, param1Boolean);
              LunariansSettings.save();
            }
          }).left().width(170.0F);
      table.button("-", new Runnable() {
            public void run() {
              AutoSetConfig.setFacCount(n, AutoSetConfig.facCount(n) - 1);
              LunariansSettings.save();
              AutoSetConfig.build(table);
            }
          }).size(46.0F, 40.0F).pad(2.0F);
      table.add("x" + facCount(b)).width(48.0F).center().pad(4.0F);
      table.button("+", new Runnable() {
            public void run() {
              AutoSetConfig.setFacCount(n, AutoSetConfig.facCount(n) + 1);
              LunariansSettings.save();
              AutoSetConfig.build(table);
            }
          }).size(46.0F, 40.0F).pad(2.0F);
      table.add((Element)table).left().row();
    } 
    stepRow(table, tr("Distância mínima do núcleo (tiles)", "Min distance from core (tiles)"), () -> String.valueOf(coreGap()), () -> setCoreGap(coreGap() - 1), () -> setCoreGap(coreGap() + 1));
    stepRow(table, tr("Distância mínima das paredes (tiles)", "Min distance from walls (tiles)"), () -> String.valueOf(wallGap()), () -> setWallGap(wallGap() - 1), () -> setWallGap(wallGap() + 1));
    stepRow(table, tr("Distância entre fábricas (tiles)", "Gap between factories (tiles)"), () -> String.valueOf(facGap()), () -> setFacGap(facGap() - 1), () -> setFacGap(facGap() + 1));
    UiText.check(table, tr("Autodrill nas entradas: drills em minério ligados a cada entrada (1 recurso por entrada)", "Autodrill on inputs: ore drills wired to each input (1 resource per input)"), facDrill(), new Boolc() {
          public void get(boolean param1Boolean) {
            AutoSetConfig.setFacDrill(param1Boolean);
            LunariansSettings.save();
          }
        }).left().padTop(6.0F).row();
    table.add(tr("Ordem: 1) máquinas  2) autodrill  3) conexão específica em cada entrada.", "Order: 1) machines  2) autodrill  3) specific connection to each input.")).left().wrap().growX().row();
    table.add(tr("Use GO, toque no CENTRO e depois no raio: as fábricas vão para um espaço livre dentro do círculo.", "Press GO, tap the CENTER then the radius: factories go to free space inside the circle.")).left().wrap().growX().padTop(8.0F).row();
  }
}
