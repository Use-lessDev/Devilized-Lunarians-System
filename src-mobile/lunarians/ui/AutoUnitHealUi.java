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
import lunarians.data.UnitHealSlot;
import mindustry.Vars;
import mindustry.core.ContentLoader;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

public class AutoUnitHealUi {
  private static UnitHealSlot clipboard;
  
  private final Table list;
  
  public AutoUnitHealUi(Table paramTable) {
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
  
  private static String all() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Todas" : "All";
  }
  
  private Table card(UnitHealSlot paramUnitHealSlot) {
    Table table1 = new Table();
    Drawable drawable = Styles.black6;
    this(drawable);
    UnitType unitType;
    TextureRegionDrawable textureRegionDrawable2;
    Cell cell1 = table1.defaults();
    float f1 = 4.0F;
    cell1.pad(f1).growX();
    Table table2 = new Table();
    this();
    String str4 = paramUnitHealSlot.unit;
    boolean bool = str4.isEmpty();
    if (bool) {
      bool = false;
      float f = 0.0F;
      str4 = null;
    } else {
      ContentLoader contentLoader = Vars.content;
      String str = paramUnitHealSlot.unit;
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
    ImageButton.ImageButtonStyle imageButtonStyle = Styles.defaulti;
    this((Drawable)textureRegionDrawable2, imageButtonStyle);
    AutoUnitHealUi$4 autoUnitHealUi$4 = new AutoUnitHealUi$4();
    this(this, paramUnitHealSlot);
    imageButton.clicked(autoUnitHealUi$4);
    Cell cell5 = table2.add((Element)imageButton);
    float f3 = 40.0F;
    cell5 = cell5.size(f3);
    float f4 = 6.0F;
    cell5.padRight(f4);
    if (unitType != null) {
      str3 = unitType.localizedName;
    } else {
      str3 = pick();
    } 
    table2.add(str3).wrap().growX().left().fontScale(0.9F);
    String str3 = copyLabel();
    AutoUnitHealUi$5 autoUnitHealUi$5 = new AutoUnitHealUi$5();
    this(this, paramUnitHealSlot);
    Cell cell4 = table2.button(str3, autoUnitHealUi$5).width(64.0F);
    float f5 = 32.0F;
    ((TextButton)cell4.height(f5).padLeft(f1).get()).getLabel().setFontScale(0.8F);
    TextureRegionDrawable textureRegionDrawable1 = Icon.trash;
    imageButtonStyle = Styles.defaulti;
    AutoUnitHealUi$6 autoUnitHealUi$6 = new AutoUnitHealUi$6();
    this(this, paramUnitHealSlot);
    Cell cell3 = table2.button((Drawable)textureRegionDrawable1, imageButtonStyle, autoUnitHealUi$6);
    f4 = 36.0F;
    cell3.size(f4);
    table1.add((Element)table2).growX().row();
    table2 = new Table();
    this();
    String str2 = pasteLabel();
    AutoUnitHealUi$7 autoUnitHealUi$7 = new AutoUnitHealUi$7();
    this(this);
    Cell cell2 = table2.button(str2, autoUnitHealUi$7);
    int i = 1119092736;
    float f6 = 90.0F;
    cell2 = cell2.width(f6).height(f4);
    f4 = 2.0F;
    TextButton textButton = (TextButton)cell2.pad(f4).get();
    Label label = textButton.getLabel();
    float f7 = 0.85F;
    label.setFontScale(f7);
    UnitHealSlot unitHealSlot = clipboard;
    if (unitHealSlot == null) {
      i = 1;
      f6 = Float.MIN_VALUE;
    } else {
      i = 0;
      f6 = 0.0F;
      unitHealSlot = null;
    } 
    textButton.setDisabled(i);
    this.list.add((Element)table2).left().padTop(f4).row();
    table2 = new Table();
    this();
    String str1 = maxL();
    table2.add(str1).left().padRight(f1).fontScale(f7);
    AutoUnitHealUi$8 autoUnitHealUi$8 = new AutoUnitHealUi$8();
    this(this, paramUnitHealSlot);
    Cell cell6 = table2.button("-", autoUnitHealUi$8);
    float f2 = 28.0F;
    cell6.size(f5, f2);
    AutoUnitHealUi$9 autoUnitHealUi$9 = new AutoUnitHealUi$9();
    this(this, paramUnitHealSlot);
    table2.label(autoUnitHealUi$9).width(f3).center();
    AutoUnitHealUi$10 autoUnitHealUi$10 = new AutoUnitHealUi$10();
    this(this, paramUnitHealSlot);
    table2.button("+", autoUnitHealUi$10).size(f5, f2);
    table1.add((Element)table2).left().row();
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
  
  private static String empty() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Nenhum slot. +" : "No slots. +";
  }
  
  private static String hint() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Healers se aproximam de aliados com vida baixa. Não usados por defence/attack." : "Healers approach low-HP allies. Not used by defence/attack.";
  }
  
  public static BuilderDialog make() {
    BuilderDialog builderDialog = new BuilderDialog();
    String str = title();
    AutoUnitHealUi$1 autoUnitHealUi$1 = new AutoUnitHealUi$1();
    this();
    AutoUnitHealUi$2 autoUnitHealUi$2 = new AutoUnitHealUi$2();
    this();
    this(str, autoUnitHealUi$1, autoUnitHealUi$2);
    return builderDialog;
  }
  
  private static String maxL() {
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
  
  private static String pick() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Escolher healer" : "Pick healer";
  }
  
  private void rebuild() {
    this.list.clearChildren();
    Seq seq = LunariansSettings.unitHealSlots;
    Table table = this.list;
    int i = seq.size;
    StringBuilder stringBuilder = new StringBuilder();
    this();
    stringBuilder.append(i);
    stringBuilder.append("/8");
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
      String str1 = empty();
      Cell cell1 = table1.add(str1).wrap().growX();
      cell1.row();
    } else {
      bool = false;
      float f = 0.0F;
      cell = null;
      while (true) {
        i = seq.size;
        if (bool < i) {
          Table table2 = this.list;
          UnitHealSlot unitHealSlot = (UnitHealSlot)seq.get(bool);
          Table table1 = card(unitHealSlot);
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
    int j = 8;
    float f2 = 1.1E-44F;
    if (k < j) {
      Table table1 = this.list;
      TextureRegionDrawable textureRegionDrawable = Icon.add;
      ImageButton.ImageButtonStyle imageButtonStyle = Styles.defaulti;
      AutoUnitHealUi$3 autoUnitHealUi$3 = new AutoUnitHealUi$3();
      this(this);
      Cell cell1 = table1.button((Drawable)textureRegionDrawable, imageButtonStyle, autoUnitHealUi$3).size(40.0F);
      j = 1073741824;
      f2 = 2.0F;
      cell1 = cell1.padTop(f2);
      cell1.row();
    } 
  }
  
  private static String title() {
    return "Auto unit heal";
  }
}
