package lunarians.ui;

import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.Label;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.LunariansSettings;
import lunarians.data.CargoSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.core.ContentLoader;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

public class AutoCargoUi {
  private static CargoSlot clipboard;
  
  private final Table list;
  
  public AutoCargoUi(Table paramTable) {
    Table table = new Table();
    this();
    this.list = table;
    paramTable.defaults().growX();
    String str = hint();
    Cell cell = paramTable.add(str).wrap().growX();
    Color color = Color.lightGray;
    cell.color(color).padBottom(4.0F).row();
    paramTable.add((Element)table).growX().row();
    rebuild();
  }
  
  private Table card(CargoSlot paramCargoSlot) {
    Table table1 = new Table();
    Drawable drawable = Styles.black6;
    this(drawable);
    UnitType unitType;
    String str1;
    TextureRegionDrawable textureRegionDrawable2;
    Color color;
    table1.defaults().growX();
    Cell cell2 = table1.defaults();
    float f1 = 4.0F;
    cell2.pad(f1);
    Table table2 = new Table();
    this();
    String str5 = paramCargoSlot.unit;
    boolean bool = str5.isEmpty();
    if (bool) {
      bool = false;
      str5 = null;
    } else {
      ContentLoader contentLoader = Vars.content;
      String str = paramCargoSlot.unit;
      unitType = contentLoader.unit(str);
    } 
    ImageButton imageButton = new ImageButton();
    if (unitType != null) {
      textureRegionDrawable2 = new TextureRegionDrawable();
      TextureRegion textureRegion = unitType.uiIcon;
      this(textureRegion);
    } else {
      textureRegionDrawable2 = Icon.add;
    } 
    ImageButton.ImageButtonStyle imageButtonStyle1 = Styles.defaulti;
    this((Drawable)textureRegionDrawable2, imageButtonStyle1);
    AutoCargoUi$5 autoCargoUi$5 = new AutoCargoUi$5();
    this(this, paramCargoSlot);
    imageButton.clicked(autoCargoUi$5);
    Cell cell9 = table2.add((Element)imageButton);
    int i = 1109393408;
    float f2 = 40.0F;
    cell9 = cell9.size(f2);
    float f3 = 6.0F;
    cell9.padRight(f3);
    if (unitType != null) {
      str4 = unitType.localizedName;
    } else {
      str4 = pickUnit();
    } 
    table2.add(str4).wrap().growX().left().fontScale(0.9F);
    String str4 = copyLabel();
    AutoCargoUi$6 autoCargoUi$6 = new AutoCargoUi$6();
    this(this, paramCargoSlot);
    Cell cell8 = table2.button(str4, autoCargoUi$6).width(64.0F);
    float f4 = 32.0F;
    Label label1 = ((TextButton)cell8.height(f4).padLeft(f1).get()).getLabel();
    f3 = 0.8F;
    label1.setFontScale(f3);
    TextureRegionDrawable textureRegionDrawable1 = Icon.trash;
    ImageButton.ImageButtonStyle imageButtonStyle2 = Styles.defaulti;
    AutoCargoUi$7 autoCargoUi$7 = new AutoCargoUi$7();
    this(this, paramCargoSlot);
    Cell cell7 = table2.button((Drawable)textureRegionDrawable1, imageButtonStyle2, autoCargoUi$7);
    float f5 = 36.0F;
    cell7.size(f5).padLeft(f1);
    table1.add((Element)table2).growX().row();
    table2 = new Table();
    this();
    String str3 = pasteLabel();
    AutoCargoUi$8 autoCargoUi$8 = new AutoCargoUi$8();
    this(this);
    Cell cell6 = table2.button(str3, autoCargoUi$8);
    int j = 1119092736;
    float f6 = 90.0F;
    cell6 = cell6.width(f6).height(f5);
    f5 = 2.0F;
    TextButton textButton = (TextButton)cell6.pad(f5).get();
    Label label3 = textButton.getLabel();
    float f7 = 0.85F;
    label3.setFontScale(f7);
    CargoSlot cargoSlot = clipboard;
    int k = 0;
    if (cargoSlot == null) {
      j = 1;
      f6 = Float.MIN_VALUE;
    } else {
      j = 0;
      f6 = 0.0F;
      cargoSlot = null;
    } 
    textButton.setDisabled(j);
    this.list.add((Element)table2).left().padTop(f5).row();
    table2 = new Table();
    this();
    String str2 = maxLabel();
    table2.add(str2).left().padRight(f1).fontScale(f7);
    AutoCargoUi$9 autoCargoUi$9 = new AutoCargoUi$9();
    this(this, paramCargoSlot);
    String str8 = "-";
    Cell cell5 = table2.button(str8, autoCargoUi$9);
    j = 1105199104;
    f6 = 28.0F;
    cell5.size(f4, f6);
    AutoCargoUi$10 autoCargoUi$10 = new AutoCargoUi$10();
    this(this, paramCargoSlot);
    table2.label(autoCargoUi$10).width(f2).center().pad(f5);
    AutoCargoUi$11 autoCargoUi$11 = new AutoCargoUi$11();
    this(this, paramCargoSlot);
    String str7 = "+";
    Cell cell4 = table2.button(str7, autoCargoUi$11);
    cell4.size(f4, f6);
    Cell cell1 = table1.add((Element)table2).left().padTop(f5);
    cell1.row();
    int m = paramCargoSlot.pointCount();
    if (m == 0) {
      str1 = pathEmpty();
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      while (k < m) {
        if (k > 0) {
          str7 = " → ";
          stringBuilder.append(str7);
        } 
        if (k == 0) {
          str7 = "P1";
        } else {
          i = m + -1;
          if (k == i) {
            str7 = "Drop";
          } else {
            i = k + 1;
            StringBuilder stringBuilder1 = new StringBuilder();
            String str = "P";
            this(str);
            stringBuilder1.append(i);
            str7 = stringBuilder1.toString();
          } 
        } 
        stringBuilder.append(str7);
        stringBuilder.append("(");
        i = paramCargoSlot.px(k);
        stringBuilder.append(i);
        stringBuilder.append(",");
        i = paramCargoSlot.py(k);
        stringBuilder.append(i);
        str7 = ")";
        stringBuilder.append(str7);
        k++;
      } 
      str1 = stringBuilder.toString();
    } 
    Cell cell3 = table1.add(str1).wrap().growX().left().fontScale(f3);
    i = 2;
    f2 = 2.8E-45F;
    if (m >= i) {
      color = Color.white;
    } else {
      color = Color.lightGray;
    } 
    cell3.color(color).row();
    Table table3 = new Table();
    this();
    String str6 = pathBtn();
    AutoCargoUi$12 autoCargoUi$12 = new AutoCargoUi$12();
    this(this, paramCargoSlot);
    Cell cell10 = table3.button(str6, autoCargoUi$12);
    f3 = 120.0F;
    Label label2 = ((TextButton)cell10.width(f3).height(f4).get()).getLabel();
    label2.setFontScale(f7);
    if (m > 0) {
      TextureRegionDrawable textureRegionDrawable = Icon.trash;
      ImageButton.ImageButtonStyle imageButtonStyle = Styles.defaulti;
      AutoCargoUi$13 autoCargoUi$13 = new AutoCargoUi$13();
      this(this, paramCargoSlot);
      Cell cell = table3.button((Drawable)textureRegionDrawable, imageButtonStyle, autoCargoUi$13).size(f4);
      cell.padLeft(f1);
    } 
    table1.add((Element)table3).left().padTop(f5).row();
    return table1;
  }
  
  private static String copied() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Slot copiado" : "Slot copied";
  }
  
  private static String copyLabel() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Copiar" : "Copy";
  }
  
  private static String emptyMsg() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Nenhum slot. Aperte +" : "No slots. Press +";
  }
  
  private static String hint() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Pontos: 1=pegar, meio=caminho, último=soltar. Mapa → Add point / Back / Done. Camera/Unit." : "Points: 1=pick, mid=path, last=drop. Map → Add/Back/Done. Camera/Unit mode.";
  }
  
  private static boolean isCarrier(UnitType paramUnitType) {
    boolean bool = paramUnitType.pickupUnits;
    if (bool) {
      float f = paramUnitType.payloadCapacity;
      int i = f cmp 0.0F;
      if (i > 0) {
        Seq seq = paramUnitType.commands;
        if (seq != null) {
          Seq seq1 = paramUnitType.commands;
          UnitCommand unitCommand = UnitCommand.loopPayloadCommand;
          boolean bool1 = seq1.contains(unitCommand);
          if (bool1)
            return true; 
        } 
      } 
    } 
    return false;
  }
  
  public static BuilderDialog make() {
    BuilderDialog builderDialog = new BuilderDialog();
    String str = title();
    AutoCargoUi$1 autoCargoUi$1 = new AutoCargoUi$1();
    this();
    AutoCargoUi$2 autoCargoUi$2 = new AutoCargoUi$2();
    this();
    this(str, autoCargoUi$1, autoCargoUi$2);
    return builderDialog;
  }
  
  private static String maxAll() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Todas" : "All";
  }
  
  private static String maxLabel() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Máx:" : "Max:";
  }
  
  private static String noClip() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Nada copiado" : "Nothing copied";
  }
  
  private static String pasteLabel() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Colar" : "Paste";
  }
  
  private static String pasted() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Slot colado" : "Slot pasted";
  }
  
  private static String pathBtn() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Pontos mapa" : "Map points";
  }
  
  private static String pathEmpty() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Sem rota — defina pontos" : "No path — set points";
  }
  
  private static String pickUnit() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Escolher" : "Choose";
  }
  
  private void rebuild() {
    this.list.clearChildren();
    Seq seq = LunariansSettings.cargoSlots;
    Table table = this.list;
    int i = seq.size;
    StringBuilder stringBuilder = new StringBuilder();
    this();
    stringBuilder.append(i);
    stringBuilder.append("/10");
    String str = stringBuilder.toString();
    Cell cell = table.add(str);
    Color color = Pal.accent;
    cell = cell.color(color);
    i = 1082130432;
    float f1 = 4.0F;
    cell = cell.padBottom(f1);
    cell.row();
    boolean bool = seq.isEmpty();
    if (bool) {
      Table table1 = this.list;
      String str1 = emptyMsg();
      Cell cell1 = table1.add(str1).wrap().growX().padBottom(f1);
      cell1.row();
    } else {
      bool = false;
      float f = 0.0F;
      cell = null;
      while (true) {
        i = seq.size;
        if (bool < i) {
          Table table2 = this.list;
          CargoSlot cargoSlot = (CargoSlot)seq.get(bool);
          Table table1 = card(cargoSlot);
          Cell cell1 = table2.add((Element)table1).growX();
          float f3 = 3.0F;
          cell1 = cell1.padBottom(f3);
          cell1.row();
          int m = bool + 1;
          continue;
        } 
        break;
      } 
    } 
    int k = seq.size;
    int j = 10;
    float f2 = 1.4E-44F;
    if (k < j) {
      Table table1 = this.list;
      TextureRegionDrawable textureRegionDrawable = Icon.add;
      ImageButton.ImageButtonStyle imageButtonStyle = Styles.defaulti;
      AutoCargoUi$4 autoCargoUi$4 = new AutoCargoUi$4();
      this(this);
      Cell cell1 = table1.button((Drawable)textureRegionDrawable, imageButtonStyle, autoCargoUi$4).size(40.0F);
      j = 1073741824;
      f2 = 2.0F;
      cell1 = cell1.padTop(f2);
      cell1.row();
    } 
  }
  
  private static void reopen() {
    BuilderDialog builderDialog = make();
    AutoCargoUi$3 autoCargoUi$3 = new AutoCargoUi$3();
    this();
    builderDialog.after(autoCargoUi$3).show();
  }
  
  private static String title() {
    return "Auto cargo";
  }
}
