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
import lunarians.data.DefenceSlot;
import mindustry.Vars;
import mindustry.core.ContentLoader;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

public class AutoDefenceUi {
  private static DefenceSlot clipboard;
  
  private final Table list;
  
  public AutoDefenceUi(Table paramTable) {
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
    String str1 = LunariansSettings.language;
    String str2 = "pt";
    boolean bool = str2.equals(str1);
    if (!bool) {
      str2 = "es";
      boolean bool1 = str2.equals(str1);
      if (!bool1)
        return "All"; 
    } 
    return "Todas";
  }
  
  private Table card(DefenceSlot paramDefenceSlot) {
    Table table1 = new Table();
    Drawable drawable = Styles.black6;
    this(drawable);
    UnitType unitType;
    TextureRegionDrawable textureRegionDrawable2;
    Color color;
    boolean bool2;
    table1.defaults().growX();
    Cell cell1 = table1.defaults();
    float f1 = 4.0F;
    cell1.pad(f1);
    Table table2 = new Table();
    this();
    String str7 = paramDefenceSlot.unit;
    boolean bool = str7.isEmpty();
    if (bool) {
      bool = false;
      str7 = null;
    } else {
      ContentLoader contentLoader = Vars.content;
      String str = paramDefenceSlot.unit;
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
    AutoDefenceUi$5 autoDefenceUi$5 = new AutoDefenceUi$5();
    this(this, paramDefenceSlot);
    imageButton.clicked(autoDefenceUi$5);
    Cell cell8 = table2.add((Element)imageButton);
    int j = 1109393408;
    float f2 = 40.0F;
    cell8 = cell8.size(f2);
    float f3 = 6.0F;
    cell8.padRight(f3);
    if (unitType != null) {
      str6 = unitType.localizedName;
    } else {
      str6 = pickUnit();
    } 
    table2.add(str6).wrap().growX().left().fontScale(0.9F);
    String str6 = copyLabel();
    AutoDefenceUi$6 autoDefenceUi$6 = new AutoDefenceUi$6();
    this(this, paramDefenceSlot);
    Cell cell7 = table2.button(str6, autoDefenceUi$6).width(64.0F);
    float f4 = 32.0F;
    ((TextButton)cell7.height(f4).padLeft(f1).get()).getLabel().setFontScale(0.8F);
    TextureRegionDrawable textureRegionDrawable1 = Icon.trash;
    imageButtonStyle = Styles.defaulti;
    AutoDefenceUi$7 autoDefenceUi$7 = new AutoDefenceUi$7();
    this(this, paramDefenceSlot);
    Cell cell6 = table2.button((Drawable)textureRegionDrawable1, imageButtonStyle, autoDefenceUi$7);
    f3 = 36.0F;
    cell6.size(f3).padLeft(f1);
    table1.add((Element)table2).growX().row();
    table2 = new Table();
    this();
    String str5 = pasteLabel();
    AutoDefenceUi$8 autoDefenceUi$8 = new AutoDefenceUi$8();
    this(this);
    Cell cell5 = table2.button(str5, autoDefenceUi$8);
    float f5 = 90.0F;
    cell5 = cell5.width(f5).height(f3);
    f3 = 2.0F;
    TextButton textButton = (TextButton)cell5.pad(f3).get();
    Label label = textButton.getLabel();
    float f6 = 0.85F;
    label.setFontScale(f6);
    DefenceSlot defenceSlot = clipboard;
    boolean bool1 = true;
    if (defenceSlot == null) {
      bool2 = true;
    } else {
      bool2 = false;
      defenceSlot = null;
    } 
    textButton.setDisabled(bool2);
    this.list.add((Element)table2).left().padTop(f3).row();
    table2 = new Table();
    this();
    String str4 = maxLabel();
    table2.add(str4).left().padRight(f1).fontScale(f6);
    AutoDefenceUi$9 autoDefenceUi$9 = new AutoDefenceUi$9();
    this(this, paramDefenceSlot);
    String str9 = "-";
    Cell cell4 = table2.button(str9, autoDefenceUi$9);
    float f7 = 28.0F;
    cell4.size(f4, f7);
    AutoDefenceUi$10 autoDefenceUi$10 = new AutoDefenceUi$10();
    this(this, paramDefenceSlot);
    table2.label(autoDefenceUi$10).width(f2).center().pad(f3);
    AutoDefenceUi$11 autoDefenceUi$11 = new AutoDefenceUi$11();
    this(this, paramDefenceSlot);
    String str10 = "+";
    table2.button(str10, autoDefenceUi$11).size(f4, f7);
    table1.add((Element)table2).left().padTop(f3).row();
    table2 = new Table();
    this();
    String str3 = radLabel();
    table2.add(str3).left().padRight(f1).fontScale(f6);
    AutoDefenceUi$12 autoDefenceUi$12 = new AutoDefenceUi$12();
    this(this, paramDefenceSlot);
    table2.button(str9, autoDefenceUi$12).size(f4, f7);
    AutoDefenceUi$13 autoDefenceUi$13 = new AutoDefenceUi$13();
    this(this, paramDefenceSlot);
    table2.label(autoDefenceUi$13).width(f2).center().pad(f3);
    AutoDefenceUi$14 autoDefenceUi$14 = new AutoDefenceUi$14();
    this(this, paramDefenceSlot);
    Cell cell3 = table2.button(str10, autoDefenceUi$14);
    cell3.size(f4, f7);
    table1.add((Element)table2).left().padTop(f3).row();
    table2 = new Table();
    this();
    int i = paramDefenceSlot.x;
    j = -1 << -1;
    f2 = -0.0F;
    if (i == j)
      bool1 = false; 
    if (bool1) {
      i = paramDefenceSlot.x;
      j = paramDefenceSlot.y;
      StringBuilder stringBuilder1 = new StringBuilder();
      this();
      stringBuilder1.append(i);
      stringBuilder1.append(",");
      stringBuilder1.append(j);
      str2 = stringBuilder1.toString();
    } else {
      str2 = "—";
    } 
    StringBuilder stringBuilder = new StringBuilder();
    String str8 = "Pos: ";
    this(str8);
    stringBuilder.append(str2);
    String str2 = stringBuilder.toString();
    Cell cell2 = table2.add(str2).wrap().growX().left().fontScale(f6);
    if (bool1) {
      color = Color.white;
    } else {
      color = Color.lightGray;
    } 
    cell2.color(color);
    String str1 = mapBtn();
    AutoDefenceUi$15 autoDefenceUi$15 = new AutoDefenceUi$15();
    this(this, paramDefenceSlot);
    table2.button(str1, autoDefenceUi$15).width(f5).height(f4).padLeft(f1);
    table1.add((Element)table2).growX().row();
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
    String str1 = LunariansSettings.language;
    String str2 = "pt";
    boolean bool = str2.equals(str1);
    return bool ? "Nenhuma zona. Aperte +" : "No zones. Press +";
  }
  
  private static String hint() {
    String str1 = LunariansSettings.language;
    String str2 = "pt";
    boolean bool1 = str2.equals(str1);
    if (bool1)
      return "Unidade + máx + posição + raio. Círculo vermelho = zona de ataque."; 
    str2 = "es";
    boolean bool2 = str2.equals(str1);
    return bool2 ? "Unidad + máx + posición + radio. Círculo rojo = zona." : "Unit + max + position + radius. Red circle = attack zone.";
  }
  
  public static BuilderDialog make() {
    BuilderDialog builderDialog = new BuilderDialog();
    String str = title();
    AutoDefenceUi$1 autoDefenceUi$1 = new AutoDefenceUi$1();
    this();
    AutoDefenceUi$2 autoDefenceUi$2 = new AutoDefenceUi$2();
    this();
    this(str, autoDefenceUi$1, autoDefenceUi$2);
    return builderDialog;
  }
  
  private static String mapBtn() {
    String str1 = LunariansSettings.language;
    String str2 = "pt";
    boolean bool = str2.equals(str1);
    if (!bool) {
      str2 = "es";
      boolean bool1 = str2.equals(str1);
      if (!bool1)
        return "Map"; 
    } 
    return "Mapa";
  }
  
  private static String maxLabel() {
    String str1 = LunariansSettings.language;
    String str2 = "pt";
    boolean bool = str2.equals(str1);
    if (!bool) {
      str2 = "es";
      boolean bool1 = str2.equals(str1);
      if (!bool1)
        return "Max:"; 
    } 
    return "Máx:";
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
  
  private static String pickUnit() {
    String str1 = LunariansSettings.language;
    String str2 = "pt";
    boolean bool = str2.equals(str1);
    return bool ? "Escolher" : "Choose";
  }
  
  private static String radLabel() {
    String str1 = LunariansSettings.language;
    String str2 = "pt";
    boolean bool1 = str2.equals(str1);
    if (bool1)
      return "Raio:"; 
    str2 = "es";
    boolean bool2 = str2.equals(str1);
    return bool2 ? "Radio:" : "Radius:";
  }
  
  private void rebuild() {
    this.list.clearChildren();
    Seq seq = LunariansSettings.defenceSlots;
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
          DefenceSlot defenceSlot = (DefenceSlot)seq.get(bool);
          Table table1 = card(defenceSlot);
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
      AutoDefenceUi$4 autoDefenceUi$4 = new AutoDefenceUi$4();
      this(this);
      Cell cell1 = table1.button((Drawable)textureRegionDrawable, imageButtonStyle, autoDefenceUi$4).size(40.0F);
      j = 1073741824;
      f2 = 2.0F;
      cell1 = cell1.padTop(f2);
      cell1.row();
    } 
  }
  
  private static void reopen() {
    BuilderDialog builderDialog = make();
    AutoDefenceUi$3 autoDefenceUi$3 = new AutoDefenceUi$3();
    this();
    builderDialog.after(autoDefenceUi$3).show();
  }
  
  private static String title() {
    String str1 = LunariansSettings.language;
    String str2 = "pt";
    boolean bool1 = str2.equals(str1);
    if (bool1)
      return "Auto defesa"; 
    str2 = "es";
    bool1 = str2.equals(str1);
    if (bool1)
      return "Auto defensa"; 
    str2 = "ru";
    boolean bool2 = str2.equals(str1);
    return bool2 ? "Авто-защита" : "Auto defence";
  }
}
