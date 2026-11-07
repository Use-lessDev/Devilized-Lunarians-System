package lunarians.ui;

import arc.func.Prov;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import arc.struct.Seq;
import lunarians.AutoContainer;
import lunarians.LunariansSettings;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.core.ContentLoader;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

public class AutoContainerUi {
  private final Table box;
  
  public AutoContainerUi(Table paramTable) {
    Table table = new Table();
    this();
    this.box = table;
    paramTable.defaults().growX();
    String str = tr("The carrier unit picks up the container at point A, places it at point B, waits, picks it up again, returns it to A and waits again. Then it repeats. Point A must already have the container. Needs the unit in command mode.", "A unidade de carga pega o container no ponto A, coloca no ponto B, espera, pega de novo, devolve em A e espera mais um pouco. Depois repete. O ponto A precisa já ter o container. Precisa da unidade em modo comando.", "La unidad de carga recoge el contenedor en el punto A, lo coloca en el punto B, espera, lo recoge de nuevo, lo devuelve a A y espera otra vez. Luego repite. El punto A ya debe tener el contenedor. Requiere la unidad en modo comando.", "Юнит берёт контейнер в точке A, ставит в точке B, ждёт, берёт снова, возвращает в A и снова ждёт. Затем повтор. В точке A уже должен стоять контейнер. Нужен режим командования.");
    Cell cell = paramTable.add(str).wrap().growX();
    Color color = Color.lightGray;
    cell.color(color).padBottom(6.0F).row();
    paramTable.add((Element)table).growX().row();
    rebuild();
  }
  
  private static boolean canCarryBlocks(UnitType paramUnitType) {
    float f = paramUnitType.payloadCapacity;
    UnitCommand unitCommand = null;
    int i = f cmp 0.0F;
    if (i > 0) {
      Seq seq = paramUnitType.commands;
      if (seq != null) {
        seq = paramUnitType.commands;
        unitCommand = UnitCommand.loadBlocksCommand;
        boolean bool = seq.contains(unitCommand);
        if (bool) {
          Seq seq1 = paramUnitType.commands;
          UnitCommand unitCommand1 = UnitCommand.unloadPayloadCommand;
          boolean bool1 = seq1.contains(unitCommand1);
          if (bool1)
            return true; 
        } 
      } 
    } 
    return false;
  }
  
  private static String fmt(float paramFloat) {
    int i = (int)paramFloat;
    float f = i;
    int j = paramFloat cmp f;
    return (j == 0) ? String.valueOf(i) : String.valueOf(paramFloat);
  }
  
  public static BuilderDialog make() {
    BuilderDialog builderDialog = new BuilderDialog();
    String str = title();
    AutoContainerUi$4 autoContainerUi$4 = new AutoContainerUi$4();
    this();
    AutoContainerUi$5 autoContainerUi$5 = new AutoContainerUi$5();
    this();
    this(str, autoContainerUi$4, autoContainerUi$5);
    return builderDialog;
  }
  
  private void rebuild() {
    this.box.clearChildren();
    this.box.defaults().growX();
    String str3 = LunariansSettings.containerUnit;
    boolean bool = str3.isEmpty();
    if (bool) {
      bool = false;
      str3 = null;
    } else {
      ContentLoader contentLoader = Vars.content;
      str4 = LunariansSettings.containerUnit;
      unitType = contentLoader.unit(str4);
    } 
    Table table2 = new Table();
    Drawable drawable = Styles.black6;
    this(drawable);
    UnitType unitType;
    String str2;
    String str4;
    TextureRegionDrawable textureRegionDrawable2;
    Color color;
    Cell cell4 = table2.defaults();
    float f1 = 4.0F;
    cell4.pad(f1);
    ImageButton imageButton = new ImageButton();
    if (unitType != null) {
      textureRegionDrawable2 = new TextureRegionDrawable();
      TextureRegion textureRegion = unitType.uiIcon;
      this(textureRegion);
    } else {
      textureRegionDrawable2 = Icon.add;
    } 
    ImageButton.ImageButtonStyle imageButtonStyle2 = Styles.defaulti;
    this((Drawable)textureRegionDrawable2, imageButtonStyle2);
    AutoContainerUi$9 autoContainerUi$9 = new AutoContainerUi$9();
    this(this);
    imageButton.clicked(autoContainerUi$9);
    Cell cell3 = table2.add((Element)imageButton).size(40.0F);
    int i = 1086324736;
    float f2 = 6.0F;
    cell3.padRight(f2);
    if (unitType != null) {
      str2 = unitType.localizedName;
    } else {
      String str9 = "Выбрать юнит";
      String str10 = "Choose carrier unit";
      String str11 = "Escolher unidade de carga";
      str2 = tr(str10, str11, "Elegir unidad de carga", str9);
    } 
    table2.add(str2).wrap().growX().left();
    TextureRegionDrawable textureRegionDrawable1 = Icon.trash;
    ImageButton.ImageButtonStyle imageButtonStyle1 = Styles.defaulti;
    AutoContainerUi$13 autoContainerUi$13 = new AutoContainerUi$13();
    this(this);
    Cell cell1 = table2.button((Drawable)textureRegionDrawable1, imageButtonStyle1, autoContainerUi$13);
    float f3 = 36.0F;
    cell1.size(f3);
    this.box.add((Element)table2).growX().padBottom(f1).row();
    String str6 = "Máx unidades:";
    String str1 = tr("Max units:", str6, str6, "Макс. юнитов:");
    AutoContainerUi$14 autoContainerUi$14 = new AutoContainerUi$14();
    this();
    AutoContainerUi$15 autoContainerUi$15 = new AutoContainerUi$15();
    this();
    AutoContainerUi$16 autoContainerUi$16 = new AutoContainerUi$16();
    this();
    stepper(str1, autoContainerUi$14, autoContainerUi$15, autoContainerUi$16);
    bool = AutoContainer.hasPoints();
    if (!bool) {
      String str9 = "Точки не заданы. Нажмите «Точки»: 1-я = где контейнер (A), 2-я = где он останется (B).";
      String str10 = "Points not set. Press \"Map points\": 1st = where the container is (A), 2nd = where it will stay (B).";
      String str11 = "Pontos não definidos. Aperte \"Pontos mapa\": 1º = onde está o container (A), 2º = onde ele vai ficar (B).";
      str4 = tr(str10, str11, "Puntos sin definir. Pulsa \"Puntos mapa\": 1.º = donde está el contenedor (A), 2.º = donde se quedará (B).", str9);
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      this("A (");
      i = LunariansSettings.containerPickX;
      stringBuilder.append(i);
      String str9 = ",";
      stringBuilder.append(str9);
      int k = LunariansSettings.containerPickY;
      stringBuilder.append(k);
      String str10 = ")  →  B (";
      stringBuilder.append(str10);
      k = LunariansSettings.containerDropX;
      stringBuilder.append(k);
      stringBuilder.append(str9);
      i = LunariansSettings.containerDropY;
      stringBuilder.append(i);
      str9 = ")";
      stringBuilder.append(str9);
      str4 = stringBuilder.toString();
    } 
    Table table3 = this.box;
    Cell cell2 = table3.add(str4).wrap().growX().left();
    if (bool) {
      color = Color.white;
    } else {
      color = Color.lightGray;
    } 
    cell2.color(color).padTop(f1).row();
    Table table1 = new Table();
    this();
    String str7 = "Map points";
    String str8 = "Pontos mapa";
    String str5 = tr(str7, str8, "Puntos mapa", "Точки");
    AutoContainerUi$17 autoContainerUi$17 = new AutoContainerUi$17();
    this();
    Cell cell5 = table1.button(str5, autoContainerUi$17);
    int j = 1124204544;
    float f4 = 130.0F;
    cell5 = cell5.width(f4);
    cell5.height(f3);
    if (bool) {
      TextureRegionDrawable textureRegionDrawable = Icon.trash;
      ImageButton.ImageButtonStyle imageButtonStyle = Styles.defaulti;
      AutoContainerUi$18 autoContainerUi$18 = new AutoContainerUi$18();
      this(this);
      Cell cell = table1.button((Drawable)textureRegionDrawable, imageButtonStyle, autoContainerUi$18).size(f3);
      cell.padLeft(f1);
    } 
    this.box.add((Element)table1).left().padTop(2.0F).padBottom(f1).row();
    str1 = tr("Wait after placing (s):", "Espera após colocar (s):", "Espera tras colocar (s):", "Ожидание после установки (с):");
    AutoContainerUi$19 autoContainerUi$19 = new AutoContainerUi$19();
    this();
    AutoContainerUi$20 autoContainerUi$20 = new AutoContainerUi$20();
    this();
    AutoContainerUi$21 autoContainerUi$21 = new AutoContainerUi$21();
    this();
    stepper(str1, autoContainerUi$19, autoContainerUi$20, autoContainerUi$21);
    str1 = tr("Wait after returning (s):", "Espera após devolver (s):", "Espera tras devolver (s):", "Ожидание после возврата (с):");
    AutoContainerUi$10 autoContainerUi$10 = new AutoContainerUi$10();
    this();
    AutoContainerUi$11 autoContainerUi$11 = new AutoContainerUi$11();
    this();
    AutoContainerUi$12 autoContainerUi$12 = new AutoContainerUi$12();
    this();
    stepper(str1, autoContainerUi$10, autoContainerUi$11, autoContainerUi$12);
  }
  
  private static void reopen() {
    BuilderDialog builderDialog = make();
    AutoContainerUi$8 autoContainerUi$8 = new AutoContainerUi$8();
    this();
    builderDialog.after(autoContainerUi$8).show();
  }
  
  private void stepper(String paramString, Prov paramProv, Runnable paramRunnable1, Runnable paramRunnable2) {
    Table table = new Table();
    this();
    Cell cell2 = table.add(paramString).left().padRight(6.0F).wrap().width(190.0F);
    Color color = Pal.accent;
    cell2.color(color).fontScale(0.9F);
    AutoContainerUi$2 autoContainerUi$2 = new AutoContainerUi$2();
    this(this, paramRunnable1);
    Cell cell1 = table.button("-", autoContainerUi$2);
    float f1 = 36.0F;
    float f2 = 32.0F;
    cell1.size(f1, f2);
    CharSequence charSequence = (CharSequence)paramProv.get();
    table.add(charSequence).width(50.0F).center().pad(2.0F);
    AutoContainerUi$3 autoContainerUi$3 = new AutoContainerUi$3();
    this(this, paramRunnable2);
    table.button("+", autoContainerUi$3).size(f1, f2);
    this.box.add((Element)table).left().padTop(3.0F).row();
  }
  
  public static String title() {
    String str = "Auto container (Beta)";
    return tr(str, str, "Auto contenedor (Beta)", "Авто-контейнер (Бета)");
  }
  
  public static String tr(String paramString1, String paramString2, String paramString3, String paramString4) {
    String str1 = LunariansSettings.language;
    String str2 = "pt";
    boolean bool1 = str2.equals(str1);
    if (bool1)
      return paramString2; 
    paramString2 = "es";
    boolean bool2 = paramString2.equals(str1);
    if (bool2)
      return paramString3; 
    paramString2 = "ru";
    bool2 = paramString2.equals(str1);
    return bool2 ? paramString4 : paramString1;
  }
}
