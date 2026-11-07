package lunarians.ui;

import arc.Core;
import arc.Graphics;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.data.LossSlot;
import mindustry.Vars;
import mindustry.core.ContentLoader;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.ui.Styles;

public final class LossWarnUi {
  private static final String[] PANEL_KEYS = new String[] { "loss_p1", "loss_p2", "loss_p3", "loss_p4" };
  
  public static void build(Table paramTable) {
    String str2 = LunariansI18n.get("loss");
    boolean bool = LunariansSettings.enableLossWarn;
    LossWarnUi$1 lossWarnUi$1 = new LossWarnUi$1();
    this();
    Cell cell2 = UiText.check(paramTable, str2, bool, lossWarnUi$1);
    float f = 6.0F;
    cell2.padTop(f).row();
    String str1 = LunariansI18n.get("loss_hint");
    Cell cell1 = paramTable.add(str1).wrap().growX();
    Color color = Color.lightGray;
    cell1.color(color).padBottom(f).row();
    Table table1 = new Table();
    this();
    Table table2 = new Table();
    this();
    buildTabs(table1, table2);
    paramTable.add((Element)table1).growX().row();
    paramTable.add((Element)table2).growX().row();
    rebuild(table2);
  }
  
  private static void buildTabs(Table paramTable1, Table paramTable2) {
    paramTable1.clear();
    paramTable1.defaults().height(42.0F).growX().pad(3.0F).uniformX();
    int i = 0;
    while (true) {
      String[] arrayOfString = PANEL_KEYS;
      int j = arrayOfString.length;
      if (i < j) {
        TextButton textButton = new TextButton();
        String str = LunariansI18n.get(arrayOfString[i]);
        TextButton.TextButtonStyle textButtonStyle = Styles.togglet;
        this(str, textButtonStyle);
        textButton.setProgrammaticChangeEvents(false);
        int k = LunariansSettings.lossPanel;
        byte b = 1;
        if (k == i) {
          k = 1;
        } else {
          k = 0;
          str = null;
        } 
        textButton.setChecked(k);
        textButton.getLabel().setWrap(b);
        textButton.getLabel().setAlignment(b);
        LossWarnUi$2 lossWarnUi$2 = new LossWarnUi$2();
        this(i, paramTable1, paramTable2);
        textButton.clicked(lossWarnUi$2);
        paramTable1.add((Element)textButton);
        k = tabCols();
        k = i % k;
        j = tabCols() - b;
        if (k == j)
          paramTable1.row(); 
        i++;
        continue;
      } 
      break;
    } 
  }
  
  private static Table card(Table paramTable, Seq paramSeq, LossSlot paramLossSlot) {
    Table table1 = new Table();
    Drawable drawable = Styles.black6;
    this(drawable);
    String str3;
    Item item;
    TextureRegionDrawable textureRegionDrawable2;
    float f1 = 8.0F;
    table1.margin(f1);
    Cell cell4 = table1.defaults().growX();
    float f2 = 4.0F;
    cell4.padBottom(f2);
    String str4 = paramLossSlot.item;
    boolean bool = str4.isEmpty();
    if (bool) {
      bool = false;
      float f = 0.0F;
      str4 = null;
    } else {
      ContentLoader contentLoader = Vars.content;
      String str = paramLossSlot.item;
      item = contentLoader.item(str);
    } 
    Table table2 = new Table();
    this();
    if (item != null) {
      textureRegionDrawable2 = new TextureRegionDrawable();
      TextureRegion textureRegion = item.uiIcon;
      this(textureRegion);
    } else {
      textureRegionDrawable2 = Icon.add;
    } 
    ImageButton imageButton = new ImageButton();
    ImageButton.ImageButtonStyle imageButtonStyle2 = Styles.defaulti;
    this((Drawable)textureRegionDrawable2, imageButtonStyle2);
    LossWarnUi$5 lossWarnUi$5 = new LossWarnUi$5();
    this(paramLossSlot, paramTable);
    imageButton.clicked(lossWarnUi$5);
    Cell cell5 = table2.add((Element)imageButton);
    float f4 = 56.0F;
    cell5 = cell5.size(f4);
    cell5.padRight(f1);
    if (item != null) {
      str3 = item.localizedName;
    } else {
      str3 = LunariansI18n.get("loss_pick_item");
    } 
    table2.add(str3).wrap().growX().left();
    TextureRegionDrawable textureRegionDrawable1 = Icon.trash;
    ImageButton.ImageButtonStyle imageButtonStyle1 = Styles.defaulti;
    LossWarnUi$6 lossWarnUi$6 = new LossWarnUi$6();
    this(paramSeq, paramLossSlot, paramTable);
    Cell cell3 = table2.button((Drawable)textureRegionDrawable1, imageButtonStyle1, lossWarnUi$6).size(48.0F);
    float f5 = 6.0F;
    cell3.padLeft(f5);
    table1.add((Element)table2).growX().row();
    table2 = new Table();
    this();
    String str2 = LunariansI18n.get("loss_per_sec");
    Cell cell2 = table2.add(str2);
    f1 = 110.0F;
    cell2.width(f1).left().wrap();
    float f3 = paramLossSlot.perSec;
    float f6 = Math.min(300.0F, f3);
    LossWarnUi$7 lossWarnUi$7 = new LossWarnUi$7();
    this(paramLossSlot);
    table2.slider(0.5F, 300.0F, 0.5F, f6, lossWarnUi$7).growX().padLeft(f2);
    LossWarnUi$8 lossWarnUi$8 = new LossWarnUi$8();
    this(paramLossSlot);
    Cell cell1 = table2.label(lossWarnUi$8);
    f3 = 64.0F;
    cell1.width(f3).right().padLeft(f5);
    table1.add((Element)table2).growX().row();
    table2 = new Table();
    this();
    String str1 = LunariansI18n.get("loss_delay");
    table2.add(str1).width(f1).left().wrap();
    f1 = paramLossSlot.delay;
    f6 = Math.min(60.0F, f1);
    LossWarnUi$9 lossWarnUi$9 = new LossWarnUi$9();
    this(paramLossSlot);
    table2.slider(1.0F, 60.0F, 1.0F, f6, lossWarnUi$9).growX().padLeft(f2);
    LossWarnUi$10 lossWarnUi$10 = new LossWarnUi$10();
    this(paramLossSlot);
    table2.label(lossWarnUi$10).width(f3).right().padLeft(f5);
    table1.add((Element)table2).growX().row();
    return table1;
  }
  
  private static String fmt(float paramFloat) {
    float f1 = Math.round(paramFloat);
    f1 = Math.abs(paramFloat - f1);
    float f2 = 0.05F;
    int i = f1 cmp f2;
    if (i < 0)
      return String.valueOf(Math.round(paramFloat)); 
    f1 = 10.0F;
    return String.valueOf(Math.round(paramFloat * f1) / f1);
  }
  
  private static void rebuild(Table paramTable) {
    paramTable.clear();
    paramTable.defaults().growX();
    int i = LunariansSettings.lossPanel;
    Seq seq = LunariansSettings.lossSlots(i);
    boolean bool = seq.isEmpty();
    float f = 6.0F;
    if (bool) {
      String str = LunariansI18n.get("loss_empty");
      Cell cell = paramTable.add(str).wrap().growX().padBottom(f);
      cell.row();
    } 
    bool = false;
    TextureRegionDrawable textureRegionDrawable = null;
    while (true) {
      int k = seq.size;
      if (bool < k) {
        LossSlot lossSlot = (LossSlot)seq.get(bool);
        Table table = card(paramTable, seq, lossSlot);
        Cell cell1 = paramTable.add((Element)table).growX();
        float f2 = 5.0F;
        cell1 = cell1.padBottom(f2);
        cell1.row();
        int m = bool + 1;
        continue;
      } 
      int j = seq.size;
      k = 25;
      float f1 = 3.5E-44F;
      if (j < k) {
        textureRegionDrawable = Icon.add;
        ImageButton.ImageButtonStyle imageButtonStyle = Styles.defaulti;
        LossWarnUi$3 lossWarnUi$3 = new LossWarnUi$3();
        this(seq, paramTable);
        Cell cell1 = paramTable.button((Drawable)textureRegionDrawable, imageButtonStyle, lossWarnUi$3).size(52.0F);
        k = 1073741824;
        f1 = 2.0F;
        cell1 = cell1.padBottom(f1);
        cell1.row();
      } 
      StringBuilder stringBuilder = new StringBuilder();
      this();
      i = seq.size;
      stringBuilder.append(i);
      stringBuilder.append("/25");
      String str = stringBuilder.toString();
      Cell cell = paramTable.add(str);
      Color color = Pal.accent;
      cell = cell.color(color).padBottom(f);
      cell.row();
      i = LunariansSettings.lossPanel;
      j = 3;
      if (i < j) {
        String str1 = LunariansI18n.get("loss_presets");
        LossWarnUi$4 lossWarnUi$4 = new LossWarnUi$4();
        this(paramTable);
        Cell cell1 = paramTable.button(str1, lossWarnUi$4).height(42.0F).growX();
        i = 1082130432;
        float f2 = 4.0F;
        cell1 = cell1.padBottom(f2);
        cell1.row();
      } 
      return;
    } 
  }
  
  private static int tabCols() {
    Graphics graphics = Core.graphics;
    int i = graphics.getWidth();
    char c = 'ʀ';
    return (i >= c) ? 4 : 2;
  }
}
