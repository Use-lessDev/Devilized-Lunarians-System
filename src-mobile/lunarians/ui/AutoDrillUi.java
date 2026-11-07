package lunarians.ui;

import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import lunarians.AutoSetConfig;
import lunarians.LunariansSettings;
import mindustry.Vars;
import mindustry.core.ContentLoader;
import mindustry.world.Block;

public class AutoDrillUi {
  public AutoDrillUi(Table paramTable) {
    String str2;
    String str3;
    Cell cell4 = paramTable.defaults();
    cell4.growX();
    boolean bool1 = pt();
    if (bool1) {
      str3 = "Pre-build drills no patch (estilo AutoDrill). Max tiles / min ores / qualidade / água+power.";
    } else {
      str3 = "Pre-build drills on patch. Max tiles / min ores / quality / water+power.";
    } 
    Cell cell3 = paramTable.add(str3).wrap().growX();
    Color color = Color.lightGray;
    cell3 = cell3.color(color);
    float f1 = 6.0F;
    cell3.padBottom(f1).row();
    Table table1 = new Table();
    this();
    ContentLoader contentLoader = Vars.content;
    String str5 = LunariansSettings.drillBlock;
    Block block = contentLoader.block(str5);
    StringBuilder stringBuilder2 = new StringBuilder();
    String str6 = "Drill: ";
    this(str6);
    if (block != null) {
      str4 = block.localizedName;
    } else {
      str4 = LunariansSettings.drillBlock;
    } 
    stringBuilder2.append(str4);
    String str4 = stringBuilder2.toString();
    table1.add(str4).growX().left();
    AutoDrillUi$3 autoDrillUi$3 = new AutoDrillUi$3();
    this(this);
    Cell cell5 = table1.button("...", autoDrillUi$3);
    float f2 = 40.0F;
    cell5.size(f2);
    Cell cell2 = paramTable.add((Element)table1).growX();
    f1 = 4.0F;
    cell2 = cell2.pad(f1);
    cell2.row();
    AutoDrillUi$0 autoDrillUi$0 = new AutoDrillUi$0();
    this();
    AutoDrillUi$7 autoDrillUi$7 = new AutoDrillUi$7();
    this();
    AutoDrillUi$8 autoDrillUi$8 = new AutoDrillUi$8();
    this();
    String str7 = "Max tiles";
    AutoDrillUi autoDrillUi = this;
    Table table2 = paramTable;
    stepper(paramTable, str7, autoDrillUi$0, autoDrillUi$7, autoDrillUi$8);
    Table table3 = paramTable;
    AutoDrillUi$9 autoDrillUi$9 = new AutoDrillUi$9();
    this();
    AutoDrillUi$10 autoDrillUi$10 = new AutoDrillUi$10();
    this();
    AutoDrillUi$11 autoDrillUi$11 = new AutoDrillUi$11();
    this();
    stepper(paramTable, "Min ores", autoDrillUi$9, autoDrillUi$10, autoDrillUi$11);
    AutoDrillUi$12 autoDrillUi$12 = new AutoDrillUi$12();
    this();
    AutoDrillUi$13 autoDrillUi$13 = new AutoDrillUi$13();
    this();
    AutoDrillUi$14 autoDrillUi$14 = new AutoDrillUi$14();
    this();
    String str8 = "Opt quality";
    stepper(paramTable, str8, autoDrillUi$12, autoDrillUi$13, autoDrillUi$14);
    boolean bool2 = pt();
    if (bool2) {
      str2 = "Distância entre campos (Auto Set)";
    } else {
      str2 = "Field spacing (Auto Set)";
    } 
    str8 = str2;
    AutoDrillUi$15 autoDrillUi$15 = new AutoDrillUi$15();
    this();
    AutoDrillUi$4 autoDrillUi$4 = new AutoDrillUi$4();
    this();
    AutoDrillUi$5 autoDrillUi$5 = new AutoDrillUi$5();
    this();
    stepper(table3, str2, autoDrillUi$15, autoDrillUi$4, autoDrillUi$5);
    bool2 = pt();
    if (bool2) {
      str2 = "Água + power node";
    } else {
      str2 = "Water + power nodes";
    } 
    bool1 = LunariansSettings.drillPlaceWaterPower;
    AutoDrillUi$6 autoDrillUi$6 = new AutoDrillUi$6();
    this();
    Cell cell1 = table3.check(str2, bool1, autoDrillUi$6).left().pad(f1);
    cell1.row();
    int j = LunariansSettings.drillStartX;
    int i = -1 << -1;
    if (j == i) {
      str1 = "—";
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      i = LunariansSettings.drillStartX;
      stringBuilder.append(i);
      String str = ",";
      stringBuilder.append(str);
      i = LunariansSettings.drillStartY;
      stringBuilder.append(i);
      str1 = stringBuilder.toString();
    } 
    StringBuilder stringBuilder1 = new StringBuilder();
    this("Ore tile: ");
    stringBuilder1.append(str1);
    String str1 = stringBuilder1.toString();
    table3.add(str1).left().pad(f1).row();
  }
  
  public static BuilderDialog make() {
    BuilderDialog builderDialog = new BuilderDialog();
    AutoDrillUi$1 autoDrillUi$1 = new AutoDrillUi$1();
    this();
    AutoDrillUi$2 autoDrillUi$2 = new AutoDrillUi$2();
    this();
    this("Auto Drill", autoDrillUi$1, autoDrillUi$2);
    return builderDialog;
  }
  
  private static boolean pt() {
    String str = LunariansSettings.language;
    return "pt".equals(str);
  }
  
  private void stepper(Table paramTable, String paramString, Prov paramProv, Runnable paramRunnable1, Runnable paramRunnable2) {
    Table table = new Table();
    this();
    StringBuilder stringBuilder = new StringBuilder();
    this();
    stringBuilder.append(paramString);
    stringBuilder.append(":");
    paramString = stringBuilder.toString();
    table.add(paramString).left().padRight(4.0F);
    Cell cell = table.button("-", paramRunnable1);
    float f1 = 32.0F;
    float f2 = 28.0F;
    cell.size(f1, f2);
    table.label(paramProv).width(48.0F).center();
    table.button("+", paramRunnable2).size(f1, f2);
    paramTable.add((Element)table).left().pad(2.0F).row();
  }
}
