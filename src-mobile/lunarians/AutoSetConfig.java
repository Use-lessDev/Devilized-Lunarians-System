package lunarians;

import arc.Core;
import arc.Settings;
import arc.func.Prov;
import arc.scene.Element;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import java.util.ArrayList;
import java.util.Iterator;
import lunarians.ui.UiText;
import mindustry.Vars;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.blocks.production.Drill;

public class AutoSetConfig {
  public static final int DEF_COAL = 2;
  
  public static final int DEF_COPPER = 3;
  
  public static final int DEF_LEAD = 1;
  
  public static final int DEF_SAND = 2;
  
  public static final String[] FACS;
  
  public static final String[] FAC_NAME_EN;
  
  public static final String[] FAC_NAME_PT;
  
  public static final String[] FAC_SCHEM;
  
  public static final int MODE_DRILLS = 0;
  
  public static final int MODE_FACTORIES = 1;
  
  public static final String[] ORES = new String[] { "copper", "lead", "coal", "sand", "titanium", "thorium" };
  
  public static final int[] ORE_DEF;
  
  public static final String[] ORE_NAME_EN = new String[] { "Copper", "Lead", "Coal", "Sand", "Titanium", "Thorium" };
  
  public static final String[] ORE_NAME_PT = new String[] { "Cobre", "Chumbo", "Carvão", "Areia", "Titânio", "Tório" };
  
  static {
    FACS = new String[] { "graphite", "silicon", "metaglass" };
    FAC_NAME_EN = new String[] { "Graphite", "Silicon", "Metaglass" };
    FAC_NAME_PT = new String[] { "Grafite", "Silicone", "Metavidro" };
    FAC_SCHEM = new String[] { "bXNjaAF4nCWM4QrCMAyEr10dqDjwQfYoPoH4o844C1tb2mwi4rvbdJBw4S73oUVrcBiCZ/J8sRH6+0M72TtNGfp6g/F2JuztwiG+HBOOD8pDcpFd8ECDbky2Jn1MlDPO7Nh6t8x9oa70CQmnsFJ6TuHdj7YQgK4slIwqBCViNtmJaEl1ESV/ckBMAy3vNWu2eiMmpIzKqDAj5h9WRyUl", "bXNjaAF4nCWMawrCMBCEJw8rKCoepEfxBOKPWFcJ5FGSbUXEu5ttYZdvmdkZdOgsdkNOTIkvboT+/tAFd6dQoa832OQiYesmztVH7B9Uh+JH9jkBBqfqg2/xvkYKTAVn9uySn2Lf1Jk+ueCQZyrPkN/9yzEBOLaFklGtQgnsio1Ai6sblPzJAREttLwvnlnjRkRIGEvHUmZF/AMCcCS/", "bXNjaAF4nCWMawrCQAyEZ7drEUXBg/QonkD8EWuUxX2UNq2IeHc3W0j4wkxm0KJ12PU5CSc50wD7/aENdOMwwV6ucIkiY0uz5MhC2N956kc/iM8JaOBePiScxAslP8euVC38ySMOeeHxEfK7e5IwgGNZGB1TckbhVmwUVl1bYPRPD6joYPW9es0ab1SEhlE7aplT8Q/6UyCn" };
  }
  
  public static int allRadius() {
    return clamp(Core.settings.getInt("lunarians-econ-radius", 45), 10, 80);
  }
  
  public static void applyPreset(int paramInt) {
    int i = 6;
    int[] arrayOfInt1 = new int[i];
    arrayOfInt1[0] = 3;
    arrayOfInt1[1] = 1;
    arrayOfInt1[2] = 2;
    arrayOfInt1[3] = 2;
    arrayOfInt1[4] = 2;
    arrayOfInt1[5] = 0;
    int[] arrayOfInt2 = new int[i];
    arrayOfInt2[0] = 3;
    arrayOfInt2[1] = 2;
    arrayOfInt2[2] = 1;
    arrayOfInt2[3] = 1;
    arrayOfInt2[4] = 2;
    arrayOfInt2[5] = 0;
    int[] arrayOfInt3 = new int[i];
    arrayOfInt3[0] = 2;
    arrayOfInt3[1] = 1;
    arrayOfInt3[2] = 1;
    arrayOfInt3[3] = 1;
    arrayOfInt3[4] = 0;
    arrayOfInt3[5] = 0;
    byte b1 = 3;
    int[][] arrayOfInt4 = new int[b1][];
    int j = 0;
    arrayOfInt4[0] = arrayOfInt1;
    boolean bool = true;
    arrayOfInt4[bool] = arrayOfInt2;
    int k = 2;
    arrayOfInt4[k] = arrayOfInt3;
    arrayOfInt3 = new int[i];
    arrayOfInt3[0] = 1;
    arrayOfInt3[1] = 1;
    arrayOfInt3[2] = 1;
    arrayOfInt3[3] = 1;
    arrayOfInt3[4] = 1;
    arrayOfInt3[5] = 1;
    int[] arrayOfInt5 = new int[i];
    arrayOfInt5[0] = 1;
    arrayOfInt5[1] = 1;
    arrayOfInt5[2] = 1;
    arrayOfInt5[3] = 1;
    arrayOfInt5[4] = 0;
    arrayOfInt5[5] = 1;
    int[] arrayOfInt6 = new int[i];
    arrayOfInt6[0] = 1;
    arrayOfInt6[1] = 1;
    arrayOfInt6[2] = 0;
    arrayOfInt6[3] = 1;
    arrayOfInt6[4] = 0;
    arrayOfInt6[5] = 1;
    int[][] arrayOfInt7 = new int[b1][];
    arrayOfInt7[0] = arrayOfInt3;
    arrayOfInt7[bool] = arrayOfInt5;
    arrayOfInt7[k] = arrayOfInt6;
    byte b2 = 30;
    arrayOfInt3 = new int[] { 45, b2, 20 };
    byte b3 = 8;
    int m = 4;
    arrayOfInt5 = new int[] { b3, m, b1 };
    char c1 = 'ל';
    char c2 = 'Ϩ';
    int n = 2000;
    int[] arrayOfInt8 = { n, c1, c2 };
    int[] arrayOfInt9 = { b1, k, k };
    int[] arrayOfInt10 = { i, m, b1 };
    m = 0;
    arrayOfInt6 = null;
    while (true) {
      String[] arrayOfString = ORES;
      int i1 = arrayOfString.length;
      if (m < i1) {
        i1 = arrayOfInt4[paramInt][m];
        setOreCount(m, i1);
        String str1 = arrayOfString[m];
        String str2 = "";
        setDrillFor(str1, str2);
        m++;
        continue;
      } 
      int i2 = 0;
      arrayOfInt4 = null;
      while (true) {
        String[] arrayOfString1 = FACS;
        m = arrayOfString1.length;
        if (i2 < m) {
          int[] arrayOfInt = arrayOfInt7[paramInt];
          n = i2 << 1;
          m = arrayOfInt[n];
          if (m != 0) {
            m = 1;
          } else {
            m = 0;
            arrayOfInt = null;
          } 
          setFacOn(i2, m);
          arrayOfInt = arrayOfInt7[paramInt];
          m = arrayOfInt[++n];
          setFacCount(i2, m);
          i2++;
          continue;
        } 
        setFacDrill(bool);
        i2 = arrayOfInt9[paramInt];
        setSpacing(i2);
        setCoreGap(arrayOfInt10[paramInt]);
        setFacGap(b1);
        setWallGap(k);
        setFloorMax(16);
        setSteamOn(false);
        setAutoPower(bool);
        setBigNode(false);
        Settings settings = Core.settings;
        k = arrayOfInt3[paramInt];
        Integer integer = Integer.valueOf(k);
        String str = "lunarians-econ-radius";
        settings.put(str, integer);
        ConveyorUpgrade.setEnabled(bool);
        i = arrayOfInt8[paramInt];
        ConveyorUpgrade.setThreshold(i);
        MinerUnits.setEnabled(bool);
        MinerUnits.setProduce(bool);
        paramInt = arrayOfInt5[paramInt];
        MinerUnits.setCap(paramInt);
        while (true) {
          String[] arrayOfString2 = MinerUnits.TYPES;
          paramInt = arrayOfString2.length;
          if (j < paramInt) {
            MinerUnits.setTypeOn(j, bool);
            j++;
            continue;
          } 
          break;
        } 
        return;
      } 
      break;
    } 
  }
  
  public static boolean autoDelete() {
    return Core.settings.getBool("lunarians-auto-delete", true);
  }
  
  public static boolean autoPower() {
    Settings settings = Core.settings;
    String str = "lunarians-as-power";
    boolean bool = true;
    int i = settings.getInt(str, bool);
    return (i != 0) ? bool : false;
  }
  
  public static boolean bigNode() {
    Settings settings = Core.settings;
    String str = "lunarians-as-bignode";
    int i = settings.getInt(str, 0);
    return (i != 0);
  }
  
  public static void build(Table paramTable) {
    paramTable.clear();
    Cell cell1 = paramTable.defaults().growX();
    float f1 = 4.0F;
    cell1.padBottom(f1);
    int i = mode();
    Table table = new Table();
    this();
    String str13;
    StringBuilder stringBuilder3 = new StringBuilder();
    String str5 = "[accent]> ";
    String str6 = "";
    if (i == 0) {
      str7 = str5;
    } else {
      str7 = str6;
    } 
    this(str7);
    String str8 = "Ресурсы";
    String str9 = "Recursos";
    String str7 = tr4(str9, "Resources", str9, str8);
    stringBuilder3.append(str7);
    str7 = "[]";
    if (i == 0) {
      str8 = str7;
    } else {
      str8 = str6;
    } 
    stringBuilder3.append(str8);
    String str4 = stringBuilder3.toString();
    AutoSetConfig$1 autoSetConfig$1 = new AutoSetConfig$1();
    this(paramTable);
    Cell cell3 = table.button(str4, autoSetConfig$1);
    float f2 = 46.0F;
    cell3 = cell3.height(f2).growX();
    float f3 = 2.0F;
    cell3.pad(f3);
    StringBuilder stringBuilder2 = new StringBuilder();
    byte b = 1;
    if (i == b) {
      str10 = str5;
    } else {
      str10 = str6;
    } 
    this(str10);
    String str11 = "Заводы";
    String str12 = "Fábricas";
    String str10 = tr4(str12, "Factories", str12, str11);
    stringBuilder2.append(str10);
    if (i == b) {
      str13 = str7;
    } else {
      str13 = str6;
    } 
    stringBuilder2.append(str13);
    String str3 = stringBuilder2.toString();
    AutoSetConfig$2 autoSetConfig$2 = new AutoSetConfig$2();
    this(paramTable);
    table.button(str3, autoSetConfig$2).height(f2).growX().pad(f3);
    StringBuilder stringBuilder1 = new StringBuilder();
    b = 2;
    if (i != b)
      str5 = str6; 
    this(str5);
    str5 = "Auto All";
    stringBuilder1.append(str5);
    if (i == b)
      str6 = str7; 
    stringBuilder1.append(str6);
    String str2 = stringBuilder1.toString();
    AutoSetConfig$3 autoSetConfig$3 = new AutoSetConfig$3();
    this(paramTable);
    table.button(str2, autoSetConfig$3).height(f2).growX().pad(f3);
    paramTable.add((Element)table).growX().row();
    str2 = "Selected tab = what the Auto Set GO button does.";
    String str1 = tr("Aba escolhida = o que o botão GO do Auto Set faz.", str2);
    Cell cell2 = paramTable.add(str1).left().wrap().growX();
    float f4 = 6.0F;
    cell2 = cell2.padBottom(f4);
    cell2.row();
    if (i == b) {
      buildAll(paramTable);
      return;
    } 
    if (i == 0) {
      buildDrills(paramTable);
      return;
    } 
    buildFactories(paramTable);
  }
  
  private static void buildAll(Table paramTable) {
    String str2 = tr4("Auto All (recursos + fábricas + unidades + schematics)", "Auto All (resources + factories + units + schematics)", "Auto All (recursos + fábricas + unidades + esquemas)", "Auto All (ресурсы + заводы + юниты + схемы)");
    UiText.section(paramTable, str2);
    str2 = tr4("Com esta aba, GO (confirmar) abre o MAPEAMENTO: você voa até os minérios (ficam marcados), pode marcar a areia na mão e decide quando começar. Depois roda: cobre/chumbo, carvão/areia, grafite, metavidro, silício, titânio, unidades, tório e por fim os schematics do Auto Dock.", "With this tab, GO (confirm) opens MAPPING: fly to the ore (it gets marked), mark sand by hand if you want, and choose when to start. Then it runs: copper/lead, coal/sand, graphite, metaglass, silicon, titanium, units, thorium and finally the Auto Dock schematics.", "Con esta pestaña, GO (confirmar) abre el MAPEO: vuela al mineral (se marca), marca la arena a mano y decide cuándo empezar. Luego corre: cobre/plomo, carbón/arena, grafito, metacristal, silicio, titanio, unidades, torio y al final los esquemas del Auto Dock.", "В этой вкладке GO (подтвердить) открывает КАРТИРОВАНИЕ: летите к руде (она отмечается), отмечайте песок вручную и сами решайте, когда начать.");
    Cell cell = paramTable.add(str2).left().wrap().growX();
    float f = 6.0F;
    cell.padBottom(f).row();
    String str1 = tr4("Raio mínimo da área (tiles)", "Min area radius (tiles)", "Radio mínimo del área (tiles)", "Мин. радиус области (клеток)");
    AutoSetConfig$0 autoSetConfig$0 = new AutoSetConfig$0();
    this();
    AutoSetConfig$19 autoSetConfig$19 = new AutoSetConfig$19();
    this();
    AutoSetConfig$20 autoSetConfig$20 = new AutoSetConfig$20();
    this();
    stepRow(paramTable, str1, autoSetConfig$0, autoSetConfig$19, autoSetConfig$20);
    str1 = tr4("A área é centrada na sua unidade e cresce para cobrir tudo que você marcou. As quantidades vêm das abas Recursos e Fábricas.", "The area is centered on your unit and grows to cover everything you marked. Amounts come from the Resources and Factories tabs.", "El área se centra en tu unidad y crece para cubrir lo marcado. Las cantidades vienen de las pestañas Recursos y Fábricas.", "Область центрируется на вашей единице и растёт до отмеченного. Количества — во вкладках Ресурсы и Заводы.");
    paramTable.add(str1).left().wrap().growX().row();
    str1 = tr4("Auto delete e Auto power node", "Auto delete and Auto power node", "Auto delete y Auto power node", "Автоудаление и авто-узлы");
    UiText.section(paramTable, str1);
    str1 = tr4("Auto delete: remove esteiras/drills/nos próprios do Auto Set que bloqueiam um plano (máx. 30 por execução)", "Auto delete: removes Auto Set's own conveyors/drills/nodes that block a plan (max 30 per run)", "Auto delete: quita cintas/taladros/nodos propios del Auto Set que bloquean un plan (máx. 30 por ejecución)", "Автоудаление: убирает свои конвейеры/буры/узлы Auto Set, мешающие плану (макс. 30)");
    boolean bool = autoDelete();
    AutoSetConfig$4 autoSetConfig$4 = new AutoSetConfig$4();
    this();
    UiText.check(paramTable, str1, bool, autoSetConfig$4).left().row();
    str1 = tr4("Auto power node: liga consumidores sem energia à rede mais próxima", "Auto power node: links unpowered consumers to the nearest powered grid", "Auto power node: conecta consumidores sin energía a la red más cercana", "Авто-узлы: подключают потребителей без энергии к ближайшей сети");
    bool = PowerLink.enabled();
    AutoSetConfig$5 autoSetConfig$5 = new AutoSetConfig$5();
    this();
    UiText.check(paramTable, str1, bool, autoSetConfig$5).left().row();
    str1 = ConveyorUpgrade.title();
    UiText.section(paramTable, str1);
    str1 = tr4("Trocar esteiras comuns por esteiras de titânio perto da sua unidade", "Replace basic conveyors with titanium conveyors near your unit", "Cambiar cintas básicas por cintas de titanio cerca de tu unidad", "Заменять обычные конвейеры титановыми рядом с вами");
    bool = ConveyorUpgrade.enabled();
    AutoSetConfig$6 autoSetConfig$6 = new AutoSetConfig$6();
    this();
    UiText.check(paramTable, str1, bool, autoSetConfig$6).left().row();
    str1 = tr4("Titânio mínimo no núcleo", "Min titanium in core", "Titanio mínimo en el núcleo", "Мин. титана в ядре");
    AutoSetConfig$21 autoSetConfig$21 = new AutoSetConfig$21();
    this();
    AutoSetConfig$22 autoSetConfig$22 = new AutoSetConfig$22();
    this();
    AutoSetConfig$23 autoSetConfig$23 = new AutoSetConfig$23();
    this();
    stepRow(paramTable, str1, autoSetConfig$21, autoSetConfig$22, autoSetConfig$23);
    str1 = tr4("Resetar configurações para o modo", "Reset settings for mode", "Restablecer ajustes para el modo", "Сброс настроек под режим");
    UiText.section(paramTable, str1);
    str1 = tr4("Aplica quantidades, fábricas, raio, mineradores e esteira de titânio ideais para cada modo. Você pode ajustar depois.", "Applies amounts, factories, radius, miner units and titanium conveyor suited to each mode. You can tweak afterwards.", "Aplica cantidades, fábricas, radio, mineros y cinta de titanio adecuados a cada modo. Puedes ajustar después.", "Применяет количества, заводы, радиус, шахтёров и титановый конвейер под каждый режим.");
    paramTable.add(str1).left().wrap().growX().row();
    Table table = new Table();
    this();
    String str5 = "Survival";
    String str3 = tr4("Sobrevivência", str5, "Supervivencia", "Выживание");
    String str4 = "PvP Hexed";
    String[] arrayOfString = { str3, "PvP", str4 };
    int i = 0;
    autoSetConfig$22 = null;
    while (true) {
      byte b = 3;
      if (i < b) {
        str4 = arrayOfString[i];
        AutoSetConfig$7 autoSetConfig$7 = new AutoSetConfig$7();
        this(i, arrayOfString, paramTable);
        Cell cell1 = table.button(str4, autoSetConfig$7).height(46.0F).growX();
        float f1 = 2.0F;
        cell1.pad(f1);
        i++;
        continue;
      } 
      paramTable.add((Element)table).growX().row();
      return;
    } 
  }
  
  private static void buildDrills(Table paramTable) {
    String str1 = "Drills per resource";
    String str2 = tr("Quantidade de drills por recurso", str1);
    UiText.section(paramTable, str2);
    int i = 0;
    str2 = null;
    while (true) {
      String[] arrayOfString = ORES;
      int j = arrayOfString.length;
      float f = 8.0F;
      if (i < j) {
        Table table = new Table();
        this();
        String str7 = ORE_NAME_PT[i];
        String str8 = ORE_NAME_EN[i];
        str7 = tr(str7, str8);
        table.add(str7).left().width(110.0F);
        AutoSetConfig$8 autoSetConfig$8 = new AutoSetConfig$8();
        this(i, paramTable);
        Cell cell3 = table.button("-", autoSetConfig$8);
        float f1 = 46.0F;
        float f2 = 40.0F;
        cell3 = cell3.size(f1, f2);
        float f3 = 2.0F;
        cell3.pad(f3);
        int k = oreCount(i);
        String str6 = String.valueOf(k);
        Cell cell2 = table.add(str6).width(48.0F).center();
        float f4 = 4.0F;
        cell2.pad(f4);
        AutoSetConfig$9 autoSetConfig$9 = new AutoSetConfig$9();
        this(i, paramTable);
        String str9 = "+";
        table.button(str9, autoSetConfig$9).size(f1, f2).pad(f3);
        StringBuilder stringBuilder2 = new StringBuilder();
        this("[gray](");
        String str10 = "default";
        str8 = tr("padrão", str10);
        stringBuilder2.append(str8);
        stringBuilder2.append(" ");
        int m = ORE_DEF[i];
        stringBuilder2.append(m);
        str8 = ")[]";
        stringBuilder2.append(str8);
        String str5 = stringBuilder2.toString();
        table.add(str5).left().padLeft(f);
        paramTable.add((Element)table).left().row();
        String str3 = arrayOfString[i];
        StringBuilder stringBuilder1 = new StringBuilder();
        this();
        str5 = "Drill: ";
        str5 = tr(str5, str5);
        stringBuilder1.append(str5);
        str5 = drillLabel(str3);
        stringBuilder1.append(str5);
        String str4 = stringBuilder1.toString();
        AutoSetConfig$10 autoSetConfig$10 = new AutoSetConfig$10();
        this(str3, paramTable);
        Cell cell1 = paramTable.button(str4, autoSetConfig$10).height(f2).growX().padLeft(f);
        cell1.row();
        i++;
        continue;
      } 
      str2 = tr("Isolamento e areia", "Isolation and sand");
      UiText.section(paramTable, str2);
      str2 = tr("Distância entre campos", "Field spacing");
      AutoSetConfig$35 autoSetConfig$35 = new AutoSetConfig$35();
      this();
      AutoSetConfig$38 autoSetConfig$38 = new AutoSetConfig$38();
      this();
      AutoSetConfig$39 autoSetConfig$39 = new AutoSetConfig$39();
      this();
      stepRow(paramTable, str2, autoSetConfig$35, autoSetConfig$38, autoSetConfig$39);
      str2 = tr("Tamanho máx. do campo de areia (tiles)", "Max sand field size (tiles)");
      AutoSetConfig$40 autoSetConfig$40 = new AutoSetConfig$40();
      this();
      AutoSetConfig$41 autoSetConfig$41 = new AutoSetConfig$41();
      this();
      AutoSetConfig$42 autoSetConfig$42 = new AutoSetConfig$42();
      this();
      stepRow(paramTable, str2, autoSetConfig$40, autoSetConfig$41, autoSetConfig$42);
      str2 = tr4("Steam Drill (carvão 3x3 + água)", "Steam Drill (3x3 coal + water)", "Steam Drill (carbón 3x3 + agua)", "Steam Drill (уголь 3x3 + вода)");
      UiText.section(paramTable, str2);
      str2 = tr4("Ativar Steam Drill (schematic + bomba 2x2 na água)", "Enable Steam Drill (schematic + 2x2 pump on water)", "Activar Steam Drill (esquema + bomba 2x2 en el agua)", "Включить Steam Drill (схема + насос 2x2 на воде)");
      boolean bool = steamOn();
      AutoSetConfig$11 autoSetConfig$11 = new AutoSetConfig$11();
      this();
      UiText.check(paramTable, str2, bool, autoSetConfig$11).left().row();
      str2 = tr4("Quantidade de Steam Drills", "Steam Drill amount", "Cantidad de Steam Drills", "Количество Steam Drill");
      AutoSetConfig$43 autoSetConfig$43 = new AutoSetConfig$43();
      this();
      AutoSetConfig$44 autoSetConfig$44 = new AutoSetConfig$44();
      this();
      AutoSetConfig$45 autoSetConfig$45 = new AutoSetConfig$45();
      this();
      stepRow(paramTable, str2, autoSetConfig$43, autoSetConfig$44, autoSetConfig$45);
      str2 = tr4("Mín. tiles de carvão no 3x3", "Min coal tiles in the 3x3", "Mín. casillas de carbón en el 3x3", "Мин. клеток угля в 3x3");
      AutoSetConfig$46 autoSetConfig$46 = new AutoSetConfig$46();
      this();
      AutoSetConfig$36 autoSetConfig$36 = new AutoSetConfig$36();
      this();
      AutoSetConfig$37 autoSetConfig$37 = new AutoSetConfig$37();
      this();
      stepRow(paramTable, str2, autoSetConfig$46, autoSetConfig$36, autoSetConfig$37);
      str2 = tr4("Auto conectar energia (power node + painéis para ligar a bomba)", "Auto connect power (power nodes + solar panels to start the pump)", "Auto conectar energía (nodos + paneles para arrancar la bomba)", "Автоподключение энергии (узлы + солнечные панели для насоса)");
      bool = autoPower();
      AutoSetConfig$12 autoSetConfig$12 = new AutoSetConfig$12();
      this();
      UiText.check(paramTable, str2, bool, autoSetConfig$12).left().row();
      str2 = tr4("Usar Large Power Node se houver recursos", "Use Large Power Node when resources allow", "Usar Large Power Node si hay recursos", "Использовать Large Power Node, если хватает ресурсов");
      bool = bigNode();
      AutoSetConfig$13 autoSetConfig$13 = new AutoSetConfig$13();
      this();
      UiText.check(paramTable, str2, bool, autoSetConfig$13).left().row();
      str2 = tr("Restaurar padrão (3 / 1 / 2 / 2 / 0 / 0)", "Restore default (3 / 1 / 2 / 2 / 0 / 0)");
      AutoSetConfig$14 autoSetConfig$14 = new AutoSetConfig$14();
      this(paramTable);
      paramTable.button(str2, autoSetConfig$14).height(44.0F).growX().padTop(f).row();
      return;
    } 
  }
  
  private static void buildFactories(Table paramTable) {
    String str1 = tr("Fábricas (schematics virados para cima)", "Factories (schematics face up)");
    UiText.section(paramTable, str1);
    String str2 = "Item inputs: the bottom conveyors. Output: top, routed to the core.";
    str1 = tr("Entradas: os 2 conveyors de baixo (esq. e dir.). Saída: topo, ligada ao núcleo.", str2);
    Cell cell = paramTable.add(str1).left().wrap().growX();
    float f = 6.0F;
    cell.padBottom(f).row();
    int i = 0;
    cell = null;
    while (true) {
      String[] arrayOfString = FACS;
      int j = arrayOfString.length;
      if (i < j) {
        Table table = new Table();
        this();
        String str4 = FAC_NAME_PT[i];
        String str5 = FAC_NAME_EN[i];
        str4 = tr(str4, str5);
        boolean bool1 = facOn(i);
        AutoSetConfig$15 autoSetConfig$15 = new AutoSetConfig$15();
        this(i);
        UiText.check(table, str4, bool1, autoSetConfig$15).left().width(170.0F);
        AutoSetConfig$16 autoSetConfig$16 = new AutoSetConfig$16();
        this(i, paramTable);
        str5 = "-";
        Cell cell4 = table.button(str5, autoSetConfig$16);
        int k = 1110966272;
        float f1 = 46.0F;
        float f2 = 40.0F;
        cell4 = cell4.size(f1, f2);
        float f3 = 2.0F;
        cell4.pad(f3);
        StringBuilder stringBuilder = new StringBuilder();
        this("x");
        int m = facCount(i);
        stringBuilder.append(m);
        String str3 = stringBuilder.toString();
        Cell cell3 = table.add(str3).width(48.0F).center();
        m = 1082130432;
        float f4 = 4.0F;
        cell3.pad(f4);
        AutoSetConfig$17 autoSetConfig$17 = new AutoSetConfig$17();
        this(i, paramTable);
        String str6 = "+";
        Cell cell2 = table.button(str6, autoSetConfig$17).size(f1, f2);
        cell2.pad(f3);
        Cell cell1 = paramTable.add((Element)table).left();
        cell1.row();
        i++;
        continue;
      } 
      String str = tr("Distância mínima do núcleo (tiles)", "Min distance from core (tiles)");
      AutoSetConfig$26 autoSetConfig$26 = new AutoSetConfig$26();
      this();
      AutoSetConfig$27 autoSetConfig$27 = new AutoSetConfig$27();
      this();
      AutoSetConfig$28 autoSetConfig$28 = new AutoSetConfig$28();
      this();
      stepRow(paramTable, str, autoSetConfig$26, autoSetConfig$27, autoSetConfig$28);
      str = tr("Distância mínima das paredes (tiles)", "Min distance from walls (tiles)");
      AutoSetConfig$29 autoSetConfig$29 = new AutoSetConfig$29();
      this();
      AutoSetConfig$30 autoSetConfig$30 = new AutoSetConfig$30();
      this();
      AutoSetConfig$31 autoSetConfig$31 = new AutoSetConfig$31();
      this();
      stepRow(paramTable, str, autoSetConfig$29, autoSetConfig$30, autoSetConfig$31);
      str = tr("Distância entre fábricas (tiles)", "Gap between factories (tiles)");
      AutoSetConfig$32 autoSetConfig$32 = new AutoSetConfig$32();
      this();
      AutoSetConfig$33 autoSetConfig$33 = new AutoSetConfig$33();
      this();
      AutoSetConfig$34 autoSetConfig$34 = new AutoSetConfig$34();
      this();
      stepRow(paramTable, str, autoSetConfig$32, autoSetConfig$33, autoSetConfig$34);
      str = tr("Autodrill nas entradas: drills em minério ligados a cada entrada (1 recurso por entrada)", "Autodrill on inputs: ore drills wired to each input (1 resource per input)");
      boolean bool = facDrill();
      AutoSetConfig$18 autoSetConfig$18 = new AutoSetConfig$18();
      this();
      UiText.check(paramTable, str, bool, autoSetConfig$18).left().padTop(f).row();
      str = tr("Ordem: 1) máquinas  2) autodrill  3) conexão específica em cada entrada.", "Order: 1) machines  2) autodrill  3) specific connection to each input.");
      paramTable.add(str).left().wrap().growX().row();
      str = tr("Use GO, toque no CENTRO e depois no raio: as fábricas vão para um espaço livre dentro do círculo.", "Press GO, tap the CENTER then the radius: factories go to free space inside the circle.");
      paramTable.add(str).left().wrap().growX().padTop(8.0F).row();
      return;
    } 
  }
  
  private static int clamp(int paramInt1, int paramInt2, int paramInt3) {
    paramInt1 = Math.min(paramInt3, paramInt1);
    return Math.max(paramInt2, paramInt1);
  }
  
  public static int coreGap() {
    return clamp(Core.settings.getInt("lunarians-set-core-gap", 6), 0, 30);
  }
  
  private static ArrayList drillChoices(String paramString) {
    ArrayList<String> arrayList = new ArrayList();
    this();
    arrayList.add("");
    Item item = Vars.content.item(paramString);
    Iterator<Block> iterator = Vars.content.blocks().iterator();
    while (true) {
      boolean bool = iterator.hasNext();
      if (bool) {
        Block block = iterator.next();
        boolean bool1 = block instanceof Drill;
        if (bool1) {
          Drill drill = (Drill)block;
          if (item != null) {
            int i = drill.tier;
            int j = item.hardness;
            if (i >= j)
              continue; 
            continue;
          } 
          continue;
        } 
        continue;
      } 
      return arrayList;
      str = ((Drill)SYNTHETIC_LOCAL_VARIABLE_4).name;
      arrayList.add(str);
    } 
  }
  
  public static String drillFor(String paramString) {
    Settings settings = Core.settings;
    StringBuilder stringBuilder = new StringBuilder();
    this("lunarians-as-drill-");
    stringBuilder.append(paramString);
    String str2 = stringBuilder.toString();
    String str3 = "";
    String str1 = settings.getString(str2, str3);
    if (str1 != null) {
      boolean bool = str1.isEmpty();
      if (!bool)
        return str1; 
    } 
    str1 = "titanium";
    boolean bool1 = str1.equals(paramString);
    if (bool1)
      return "pneumatic-drill"; 
    str1 = "thorium";
    boolean bool2 = str1.equals(paramString);
    return bool2 ? "laser-drill" : str3;
  }
  
  private static String drillLabel(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic hasCustomDrill : (Ljava/lang/String;)Z
    //   4: istore_1
    //   5: iload_1
    //   6: ifeq -> 17
    //   9: aload_0
    //   10: invokestatic drillFor : (Ljava/lang/String;)Ljava/lang/String;
    //   13: astore_2
    //   14: goto -> 20
    //   17: ldc ''
    //   19: astore_2
    //   20: aload_0
    //   21: invokestatic drillFor : (Ljava/lang/String;)Ljava/lang/String;
    //   24: astore_0
    //   25: aload_0
    //   26: ifnull -> 38
    //   29: aload_0
    //   30: invokevirtual isEmpty : ()Z
    //   33: istore_3
    //   34: iload_3
    //   35: ifeq -> 42
    //   38: getstatic lunarians/LunariansSettings.drillBlock : Ljava/lang/String;
    //   41: astore_0
    //   42: aload_0
    //   43: ifnonnull -> 54
    //   46: iconst_0
    //   47: istore_3
    //   48: aconst_null
    //   49: astore #4
    //   51: goto -> 63
    //   54: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   57: aload_0
    //   58: invokevirtual block : (Ljava/lang/String;)Lmindustry/world/Block;
    //   61: astore #4
    //   63: aload #4
    //   65: ifnonnull -> 76
    //   68: aload_0
    //   69: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   72: astore_0
    //   73: goto -> 149
    //   76: new java/lang/StringBuilder
    //   79: astore_0
    //   80: aload_0
    //   81: invokespecial <init> : ()V
    //   84: aload #4
    //   86: getfield localizedName : Ljava/lang/String;
    //   89: astore #5
    //   91: aload_0
    //   92: aload #5
    //   94: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   97: pop
    //   98: aload_0
    //   99: ldc_w ' '
    //   102: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   105: pop
    //   106: aload #4
    //   108: getfield size : I
    //   111: istore #6
    //   113: aload_0
    //   114: iload #6
    //   116: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   119: pop
    //   120: ldc_w 'x'
    //   123: astore #5
    //   125: aload_0
    //   126: aload #5
    //   128: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: pop
    //   132: aload #4
    //   134: getfield size : I
    //   137: istore_3
    //   138: aload_0
    //   139: iload_3
    //   140: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   143: pop
    //   144: aload_0
    //   145: invokevirtual toString : ()Ljava/lang/String;
    //   148: astore_0
    //   149: aload_2
    //   150: invokevirtual isEmpty : ()Z
    //   153: istore_1
    //   154: iload_1
    //   155: ifeq -> 219
    //   158: new java/lang/StringBuilder
    //   161: astore_2
    //   162: aload_2
    //   163: invokespecial <init> : ()V
    //   166: ldc_w 'default'
    //   169: astore #5
    //   171: ldc_w 'padrão'
    //   174: aload #5
    //   176: invokestatic tr : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   179: astore #4
    //   181: aload_2
    //   182: aload #4
    //   184: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   187: pop
    //   188: ldc_w ' ('
    //   191: astore #4
    //   193: aload_2
    //   194: aload #4
    //   196: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   199: pop
    //   200: aload_2
    //   201: aload_0
    //   202: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   205: pop
    //   206: aload_2
    //   207: ldc_w ')'
    //   210: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   213: pop
    //   214: aload_2
    //   215: invokevirtual toString : ()Ljava/lang/String;
    //   218: astore_0
    //   219: aload_0
    //   220: areturn
  }
  
  public static int facCount(int paramInt) {
    Settings settings = Core.settings;
    StringBuilder stringBuilder = new StringBuilder();
    this("lunarians-as-fac-n-");
    String str = FACS[paramInt];
    stringBuilder.append(str);
    str = stringBuilder.toString();
    boolean bool = true;
    return clamp(settings.getInt(str, bool), bool, 6);
  }
  
  public static boolean facDrill() {
    Settings settings = Core.settings;
    String str = "lunarians-as-fac-drill";
    boolean bool = true;
    int i = settings.getInt(str, bool);
    return (i != 0) ? bool : false;
  }
  
  public static int facGap() {
    return clamp(Core.settings.getInt("lunarians-set-fac-gap", 3), 0, 12);
  }
  
  public static boolean facOn(int paramInt) {
    Settings settings = Core.settings;
    StringBuilder stringBuilder = new StringBuilder();
    this("lunarians-as-fac-on-");
    String[] arrayOfString = FACS;
    String str = arrayOfString[paramInt];
    stringBuilder.append(str);
    str = stringBuilder.toString();
    boolean bool = true;
    paramInt = settings.getInt(str, bool);
    return (paramInt != 0) ? bool : false;
  }
  
  public static String[] factoryQueue() {
    ArrayList<String> arrayList = new ArrayList();
    this();
    String[] arrayOfString = null;
    int i = 0;
    while (true) {
      String[] arrayOfString1 = FACS;
      int j = arrayOfString1.length;
      if (i < j) {
        boolean bool = facOn(i);
        if (bool) {
          bool = false;
          arrayOfString1 = null;
          while (true) {
            int k = facCount(i);
            if (bool < k) {
              String str = FACS[i];
              arrayList.add(str);
              int m = bool + 1;
              continue;
            } 
            break;
          } 
        } 
        i++;
        continue;
      } 
      arrayOfString = new String[0];
      return arrayList.<String>toArray(arrayOfString);
    } 
  }
  
  public static int floorMax() {
    return clamp(Core.settings.getInt("lunarians-set-floor-max", 16), 4, 64);
  }
  
  private static boolean hasCustomDrill(String paramString) {
    Settings settings = Core.settings;
    StringBuilder stringBuilder = new StringBuilder();
    String str2 = "lunarians-as-drill-";
    this(str2);
    stringBuilder.append(paramString);
    paramString = stringBuilder.toString();
    String str1 = "";
    paramString = settings.getString(paramString, str1);
    if (paramString != null) {
      boolean bool = paramString.isEmpty();
      if (!bool)
        return true; 
    } 
    return false;
  }
  
  public static int mode() {
    int i = Core.settings.getInt("lunarians-as-mode", 0);
    i = Math.min(2, i);
    return Math.max(0, i);
  }
  
  public static int oreCount(int paramInt) {
    Settings settings = Core.settings;
    StringBuilder stringBuilder = new StringBuilder();
    this("lunarians-as-ore-");
    String str2 = ORES[paramInt];
    stringBuilder.append(str2);
    String str1 = stringBuilder.toString();
    paramInt = ORE_DEF[paramInt];
    return clamp(settings.getInt(str1, paramInt), 0, 12);
  }
  
  public static String[] oreOrder() {
    ArrayList<String> arrayList = new ArrayList();
    this();
    String[] arrayOfString = null;
    int i = 0;
    while (true) {
      int j = oreCount(0);
      if (i < j) {
        String str = "copper";
        arrayList.add(str);
        i++;
        continue;
      } 
      i = 0;
      while (true) {
        j = oreCount(1);
        if (i < j) {
          String str = "lead";
          arrayList.add(str);
          i++;
          continue;
        } 
        i = oreCount(2);
        j = oreCount(3);
        while (true) {
          if (i <= 0) {
            if (j <= 0) {
              i = 0;
              while (true) {
                j = oreCount(4);
                if (i < j) {
                  String str = "titanium";
                  arrayList.add(str);
                  i++;
                  continue;
                } 
                i = 0;
                while (true) {
                  j = oreCount(5);
                  if (i < j) {
                    String str = "thorium";
                    arrayList.add(str);
                    i++;
                    continue;
                  } 
                  arrayOfString = new String[0];
                  return arrayList.<String>toArray(arrayOfString);
                } 
                break;
              } 
              break;
            } 
          } else {
            String str = "coal";
            arrayList.add(str);
            i--;
          } 
          if (j > 0) {
            String str = "sand";
            arrayList.add(str);
            j--;
          } 
        } 
        break;
      } 
      break;
    } 
  }
  
  private static boolean pt() {
    String str = LunariansI18n.getLanguage();
    return "pt".equals(str);
  }
  
  public static String schematicFor(String paramString) {
    int i = 0;
    while (true) {
      String[] arrayOfString = FACS;
      int j = arrayOfString.length;
      if (i < j) {
        String str = arrayOfString[i];
        boolean bool = str.equals(paramString);
        if (!bool) {
          i++;
          continue;
        } 
        return FAC_SCHEM[i];
      } 
      return null;
    } 
  }
  
  public static void setAutoDelete(boolean paramBoolean) {
    Settings settings = Core.settings;
    Boolean bool = Boolean.valueOf(paramBoolean);
    settings.put("lunarians-auto-delete", bool);
  }
  
  public static void setAutoPower(boolean paramBoolean) {
    Settings settings = Core.settings;
    Integer integer = Integer.valueOf(paramBoolean);
    settings.put("lunarians-as-power", integer);
  }
  
  public static void setBigNode(boolean paramBoolean) {
    Settings settings = Core.settings;
    Integer integer = Integer.valueOf(paramBoolean);
    settings.put("lunarians-as-bignode", integer);
  }
  
  public static void setCoreGap(int paramInt) {
    Settings settings = Core.settings;
    Integer integer = Integer.valueOf(clamp(paramInt, 0, 30));
    settings.put("lunarians-set-core-gap", integer);
  }
  
  public static void setDrillFor(String paramString1, String paramString2) {
    Settings settings = Core.settings;
    StringBuilder stringBuilder = new StringBuilder();
    String str = "lunarians-as-drill-";
    this(str);
    stringBuilder.append(paramString1);
    paramString1 = stringBuilder.toString();
    if (paramString2 == null)
      paramString2 = ""; 
    settings.put(paramString1, paramString2);
  }
  
  public static void setFacCount(int paramInt1, int paramInt2) {
    Settings settings = Core.settings;
    StringBuilder stringBuilder = new StringBuilder();
    this("lunarians-as-fac-n-");
    String str = FACS[paramInt1];
    stringBuilder.append(str);
    str = stringBuilder.toString();
    Integer integer = Integer.valueOf(clamp(paramInt2, 1, 6));
    settings.put(str, integer);
  }
  
  public static void setFacDrill(boolean paramBoolean) {
    Settings settings = Core.settings;
    Integer integer = Integer.valueOf(paramBoolean);
    settings.put("lunarians-as-fac-drill", integer);
  }
  
  public static void setFacGap(int paramInt) {
    Settings settings = Core.settings;
    Integer integer = Integer.valueOf(clamp(paramInt, 0, 12));
    settings.put("lunarians-set-fac-gap", integer);
  }
  
  public static void setFacOn(int paramInt, boolean paramBoolean) {
    Settings settings = Core.settings;
    StringBuilder stringBuilder = new StringBuilder();
    this("lunarians-as-fac-on-");
    String str = FACS[paramInt];
    stringBuilder.append(str);
    str = stringBuilder.toString();
    Integer integer = Integer.valueOf(paramBoolean);
    settings.put(str, integer);
  }
  
  public static void setFloorMax(int paramInt) {
    Settings settings = Core.settings;
    Integer integer = Integer.valueOf(clamp(paramInt, 4, 64));
    settings.put("lunarians-set-floor-max", integer);
  }
  
  public static void setMode(int paramInt) {
    Settings settings = Core.settings;
    Integer integer = Integer.valueOf(paramInt);
    settings.put("lunarians-as-mode", integer);
  }
  
  public static void setOreCount(int paramInt1, int paramInt2) {
    Settings settings = Core.settings;
    StringBuilder stringBuilder = new StringBuilder();
    this("lunarians-as-ore-");
    String str = ORES[paramInt1];
    stringBuilder.append(str);
    str = stringBuilder.toString();
    Integer integer = Integer.valueOf(clamp(paramInt2, 0, 12));
    settings.put(str, integer);
  }
  
  public static void setSpacing(int paramInt) {
    Settings settings = Core.settings;
    Integer integer = Integer.valueOf(clamp(paramInt, 1, 10));
    settings.put("lunarians-set-spacing", integer);
  }
  
  public static void setSteamCount(int paramInt) {
    Settings settings = Core.settings;
    Integer integer = Integer.valueOf(clamp(paramInt, 1, 6));
    settings.put("lunarians-as-steam-n", integer);
  }
  
  public static void setSteamMinCoal(int paramInt) {
    Settings settings = Core.settings;
    Integer integer = Integer.valueOf(clamp(paramInt, 5, 9));
    settings.put("lunarians-as-steam-coal", integer);
  }
  
  public static void setSteamOn(boolean paramBoolean) {
    Settings settings = Core.settings;
    Integer integer = Integer.valueOf(paramBoolean);
    settings.put("lunarians-as-steam", integer);
  }
  
  public static void setWallGap(int paramInt) {
    Settings settings = Core.settings;
    Integer integer = Integer.valueOf(clamp(paramInt, 0, 10));
    settings.put("lunarians-set-wall-gap", integer);
  }
  
  public static int spacing() {
    return clamp(Core.settings.getInt("lunarians-set-spacing", 3), 1, 10);
  }
  
  public static int steamCount() {
    Settings settings = Core.settings;
    boolean bool = true;
    return clamp(settings.getInt("lunarians-as-steam-n", bool), bool, 6);
  }
  
  public static int steamMinCoal() {
    Settings settings = Core.settings;
    byte b = 9;
    return clamp(settings.getInt("lunarians-as-steam-coal", b), 5, b);
  }
  
  public static boolean steamOn() {
    Settings settings = Core.settings;
    String str = "lunarians-as-steam";
    int i = settings.getInt(str, 0);
    return (i != 0);
  }
  
  private static void stepRow(Table paramTable, String paramString, Prov paramProv, Runnable paramRunnable1, Runnable paramRunnable2) {
    Table table = new Table();
    this();
    table.add(paramString).left().width(190.0F).wrap();
    AutoSetConfig$24 autoSetConfig$24 = new AutoSetConfig$24();
    this(paramRunnable1, paramTable);
    Cell cell = table.button("-", autoSetConfig$24);
    float f1 = 46.0F;
    float f2 = 40.0F;
    cell = cell.size(f1, f2);
    float f3 = 2.0F;
    cell.pad(f3);
    CharSequence charSequence = (CharSequence)paramProv.get();
    table.add(charSequence).width(48.0F).center().pad(4.0F);
    AutoSetConfig$25 autoSetConfig$25 = new AutoSetConfig$25();
    this(paramRunnable2, paramTable);
    table.button("+", autoSetConfig$25).size(f1, f2).pad(f3);
    paramTable.add((Element)table).left().row();
  }
  
  private static String tr(String paramString1, String paramString2) {
    boolean bool = pt();
    return bool ? paramString1 : paramString2;
  }
  
  public static String tr4(String paramString1, String paramString2, String paramString3, String paramString4) {
    String str1 = LunariansI18n.getLanguage();
    String str2 = "pt";
    boolean bool1 = str2.equals(str1);
    if (bool1)
      return paramString1; 
    paramString1 = "es";
    boolean bool2 = paramString1.equals(str1);
    if (bool2)
      return paramString3; 
    paramString1 = "ru";
    bool2 = paramString1.equals(str1);
    return bool2 ? paramString4 : paramString2;
  }
  
  public static int wallGap() {
    return clamp(Core.settings.getInt("lunarians-set-wall-gap", 2), 0, 10);
  }
  
  static {
    int[] arrayOfInt = new int[6];
    arrayOfInt[0] = 3;
    arrayOfInt[1] = 1;
    arrayOfInt[2] = 2;
    arrayOfInt[3] = 2;
    arrayOfInt[4] = 0;
    arrayOfInt[5] = 0;
    ORE_DEF = arrayOfInt;
  }
}
