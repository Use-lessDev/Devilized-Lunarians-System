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
import lunarians.data.HealSlot;
import mindustry.Vars;
import mindustry.core.ContentLoader;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

public class AutoHealUi {
  private static HealSlot clipboard;
  
  private final Table list;
  
  public AutoHealUi(Table paramTable) {
    Table table1 = new Table();
    this();
    this.list = table1;
    paramTable.defaults().growX();
    String str1 = hint();
    Cell cell1 = paramTable.add(str1).wrap().growX();
    Color color1 = Color.lightGray;
    cell1 = cell1.color(color1);
    float f1 = 4.0F;
    cell1.padBottom(f1).row();
    Table table2 = new Table();
    this();
    String str2 = pctLabel();
    Cell cell4 = table2.add(str2).left();
    float f2 = 6.0F;
    cell4.padRight(f2);
    AutoHealUi$3 autoHealUi$3 = new AutoHealUi$3();
    this(this);
    Cell cell3 = table2.button("-", autoHealUi$3);
    float f3 = 36.0F;
    float f4 = 32.0F;
    cell3.size(f3, f4);
    AutoHealUi$4 autoHealUi$4 = new AutoHealUi$4();
    this(this);
    Cell cell2 = table2.label(autoHealUi$4).width(48.0F).center().pad(f1);
    Color color2 = Pal.accent;
    cell2.color(color2);
    AutoHealUi$5 autoHealUi$5 = new AutoHealUi$5();
    this(this);
    table2.button("+", autoHealUi$5).size(f3, f4);
    paramTable.add((Element)table2).left().padBottom(f2).row();
    paramTable.add((Element)table1).growX().row();
    rebuild();
  }
  
  private static String all() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Todas" : "All";
  }
  
  private Table card(HealSlot paramHealSlot) {
    Table table1 = new Table();
    Drawable drawable = Styles.black6;
    this(drawable);
    UnitType unitType;
    TextureRegionDrawable textureRegionDrawable2;
    table1.defaults().growX();
    Cell cell1 = table1.defaults();
    float f1 = 4.0F;
    cell1.pad(f1);
    Table table2 = new Table();
    this();
    String str4 = paramHealSlot.unit;
    boolean bool = str4.isEmpty();
    if (bool) {
      bool = false;
      float f = 0.0F;
      str4 = null;
    } else {
      ContentLoader contentLoader = Vars.content;
      String str = paramHealSlot.unit;
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
    AutoHealUi$7 autoHealUi$7 = new AutoHealUi$7();
    this(this, paramHealSlot);
    imageButton.clicked(autoHealUi$7);
    Cell cell5 = table2.add((Element)imageButton);
    float f3 = 40.0F;
    cell5 = cell5.size(f3);
    float f4 = 6.0F;
    cell5.padRight(f4);
    if (unitType != null) {
      str3 = unitType.localizedName;
    } else {
      str3 = pickUnit();
    } 
    table2.add(str3).wrap().growX().left().fontScale(0.9F);
    String str3 = copyLabel();
    AutoHealUi$8 autoHealUi$8 = new AutoHealUi$8();
    this(this, paramHealSlot);
    Cell cell4 = table2.button(str3, autoHealUi$8).width(64.0F);
    float f5 = 32.0F;
    ((TextButton)cell4.height(f5).padLeft(f1).get()).getLabel().setFontScale(0.8F);
    TextureRegionDrawable textureRegionDrawable1 = Icon.trash;
    imageButtonStyle = Styles.defaulti;
    AutoHealUi$9 autoHealUi$9 = new AutoHealUi$9();
    this(this, paramHealSlot);
    Cell cell3 = table2.button((Drawable)textureRegionDrawable1, imageButtonStyle, autoHealUi$9);
    f4 = 36.0F;
    cell3.size(f4).padLeft(f1);
    table1.add((Element)table2).growX().row();
    table2 = new Table();
    this();
    String str2 = pasteLabel();
    AutoHealUi$10 autoHealUi$10 = new AutoHealUi$10();
    this(this);
    Cell cell2 = table2.button(str2, autoHealUi$10);
    int i = 1119092736;
    float f6 = 90.0F;
    cell2 = cell2.width(f6).height(f4);
    f4 = 2.0F;
    TextButton textButton = (TextButton)cell2.pad(f4).get();
    Label label = textButton.getLabel();
    float f7 = 0.85F;
    label.setFontScale(f7);
    HealSlot healSlot = clipboard;
    if (healSlot == null) {
      i = 1;
      f6 = Float.MIN_VALUE;
    } else {
      i = 0;
      f6 = 0.0F;
      healSlot = null;
    } 
    textButton.setDisabled(i);
    this.list.add((Element)table2).left().padTop(f4).row();
    table2 = new Table();
    this();
    String str1 = maxLabel();
    table2.add(str1).left().padRight(f1).fontScale(f7);
    AutoHealUi$11 autoHealUi$11 = new AutoHealUi$11();
    this(this, paramHealSlot);
    Cell cell6 = table2.button("-", autoHealUi$11);
    float f2 = 28.0F;
    cell6.size(f5, f2);
    AutoHealUi$12 autoHealUi$12 = new AutoHealUi$12();
    this(this, paramHealSlot);
    table2.label(autoHealUi$12).width(f3).center().pad(f4);
    AutoHealUi$13 autoHealUi$13 = new AutoHealUi$13();
    this(this, paramHealSlot);
    table2.button("+", autoHealUi$13).size(f5, f2);
    table1.add((Element)table2).left().padTop(f4).row();
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
    return bool ? "Vazio = cura todas. Aperte + para filtrar por tipo." : "Empty = heal all. Press + to filter by type.";
  }
  
  private static String hint() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Sem slots = todas as unidades em comando. Com slots = só esses tipos. Vai ao repair (borda do raio, pathfind)." : "No slots = all command units. With slots = those types only. Paths to repair edge.";
  }
  
  public static BuilderDialog make() {
    BuilderDialog builderDialog = new BuilderDialog();
    String str = title();
    AutoHealUi$1 autoHealUi$1 = new AutoHealUi$1();
    this();
    AutoHealUi$2 autoHealUi$2 = new AutoHealUi$2();
    this();
    this(str, autoHealUi$1, autoHealUi$2);
    return builderDialog;
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
  
  private static String pctLabel() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Vida <" : "HP <";
  }
  
  private static String pickUnit() {
    String str1 = "pt";
    String str2 = LunariansSettings.language;
    boolean bool = str1.equals(str2);
    return bool ? "Escolher" : "Choose";
  }
  
  private void rebuild() {
    this.list.clearChildren();
    Seq seq = LunariansSettings.healSlots;
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
          HealSlot healSlot = (HealSlot)seq.get(bool);
          Table table1 = card(healSlot);
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
      AutoHealUi$6 autoHealUi$6 = new AutoHealUi$6();
      this(this);
      Cell cell1 = table1.button((Drawable)textureRegionDrawable, imageButtonStyle, autoHealUi$6).size(40.0F);
      j = 1073741824;
      f2 = 2.0F;
      cell1 = cell1.padTop(f2);
      cell1.row();
    } 
  }
  
  private static String title() {
    return "Auto heal";
  }
}
