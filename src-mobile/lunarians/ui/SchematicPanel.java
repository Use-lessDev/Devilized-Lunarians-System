package lunarians.ui;

import arc.Core;
import arc.Graphics;
import arc.Settings;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.Element;
import arc.scene.event.EventListener;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.Label;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.SchematicPlacer;
import lunarians.data.SlotData;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;

public class SchematicPanel extends Table {
  private final Table body;
  
  private Label catLabel;
  
  private final Table content;
  
  private boolean dragging;
  
  private final Table header;
  
  private float lastTouchX;
  
  private float lastTouchY;
  
  private final ScrollPane scroll;
  
  private final Seq slots;
  
  public SchematicPanel() {
    Seq seq = new Seq();
    this();
    this.slots = seq;
    seq = null;
    this.dragging = false;
    Drawable drawable1 = Styles.black6;
    setBackground(drawable1);
    float f1 = LunariansSettings.panelWidth;
    float f2 = LunariansSettings.panelHeight;
    setSize(f1, f2);
    f1 = LunariansSettings.opacity;
    f2 = 1.0F;
    setColor(f2, f2, f2, f1);
    Table table1 = new Table();
    this();
    this.header = table1;
    Drawable drawable2 = Styles.black3;
    table1.setBackground(drawable2);
    TextureRegionDrawable textureRegionDrawable3 = Icon.move;
    ImageButton.ImageButtonStyle imageButtonStyle2 = Styles.clearNonei;
    SchematicPanel$2 schematicPanel$2 = new SchematicPanel$2();
    this(this);
    Cell cell4 = table1.button((Drawable)textureRegionDrawable3, imageButtonStyle2, schematicPanel$2).size(36.0F);
    float f3 = 2.0F;
    ImageButton imageButton2 = (ImageButton)cell4.pad(f3).get();
    SchematicPanel$1 schematicPanel$1 = new SchematicPanel$1();
    this(this);
    imageButton2.addListener((EventListener)schematicPanel$1);
    String str2 = "title";
    String str3 = LunariansI18n.get(str2);
    table1.add(str3).growX().left().padLeft(4.0F).name(str2);
    TextureRegionDrawable textureRegionDrawable2 = Icon.leftSmall;
    ImageButton.ImageButtonStyle imageButtonStyle3 = Styles.clearNonei;
    SchematicPanel$3 schematicPanel$3 = new SchematicPanel$3();
    this(this);
    Cell cell3 = table1.button((Drawable)textureRegionDrawable2, imageButtonStyle3, schematicPanel$3);
    float f4 = 28.0F;
    cell3.size(f4).pad(f2);
    SchematicPanel$4 schematicPanel$4 = new SchematicPanel$4();
    this(this);
    Label label = (Label)table1.label(schematicPanel$4).width(70.0F).get();
    this.catLabel = label;
    boolean bool = true;
    label.setAlignment(bool);
    this.catLabel.setEllipsis(bool);
    TextureRegionDrawable textureRegionDrawable1 = Icon.rightSmall;
    ImageButton.ImageButtonStyle imageButtonStyle4 = Styles.clearNonei;
    SchematicPanel$5 schematicPanel$5 = new SchematicPanel$5();
    this(this);
    table1.button((Drawable)textureRegionDrawable1, imageButtonStyle4, schematicPanel$5).size(f4).pad(f2);
    ImageButton imageButton1 = buildArrowButton();
    table1.add((Element)imageButton1).size(f4).pad(f2).name("dirBtn");
    TextureRegionDrawable textureRegionDrawable4 = Icon.downOpen;
    ImageButton.ImageButtonStyle imageButtonStyle1 = Styles.clearNonei;
    SchematicPanel$6 schematicPanel$6 = new SchematicPanel$6();
    this(this);
    table1.button((Drawable)textureRegionDrawable4, imageButtonStyle1, schematicPanel$6).size(32.0F).pad(f3).name("minBtn");
    Table table2 = new Table();
    this();
    this.content = table2;
    Cell cell2 = table2.defaults();
    f3 = 3.0F;
    cell2.pad(f3);
    rebuildSlots();
    ScrollPane scrollPane = new ScrollPane();
    ScrollPane.ScrollPaneStyle scrollPaneStyle = Styles.smallPane;
    this((Element)table2, scrollPaneStyle);
    this.scroll = scrollPane;
    scrollPane.setScrollingDisabled(bool, false);
    scrollPane.setFadeScrollBars(false);
    scrollPane.setForceScroll(false, bool);
    scrollPane.setOverscroll(false, false);
    table2 = new Table();
    this();
    this.body = table2;
    table2.add((Element)scrollPane).grow();
    top().defaults().growX();
    add((Element)table1).height(40.0F).growX();
    row();
    Cell cell1 = add((Element)table2).grow();
    float f5 = 6.0F;
    cell1 = cell1.pad(f5);
    String str1 = "body";
    cell1.name(str1);
    boolean bool1 = LunariansSettings.panelMinimized;
    if (bool1)
      table2.visible = false; 
  }
  
  private static Drawable arrowIcon(int paramInt) {
    byte b1 = 3;
    paramInt &= b1;
    byte b2 = 1;
    if (paramInt != b2) {
      b2 = 2;
      return (Drawable)((paramInt != b2) ? ((paramInt != b1) ? Icon.rightSmall : Icon.downSmall) : Icon.leftSmall);
    } 
    return (Drawable)Icon.upSmall;
  }
  
  private ImageButton buildArrowButton() {
    ImageButton.ImageButtonStyle imageButtonStyle1 = new ImageButton.ImageButtonStyle();
    ImageButton.ImageButtonStyle imageButtonStyle2 = Styles.clearNonei;
    this(imageButtonStyle2);
    Drawable drawable = arrowIcon(SchematicPlacer.rotation());
    imageButtonStyle1.imageUp = drawable;
    Color color = Pal.accent;
    imageButtonStyle1.imageUpColor = color;
    color = Color.white;
    imageButtonStyle1.imageDownColor = color;
    ImageButton imageButton = new ImageButton();
    this(imageButtonStyle1);
    SchematicPanel$7 schematicPanel$7 = new SchematicPanel$7();
    this(this, imageButtonStyle1);
    imageButton.clicked(schematicPanel$7);
    return imageButton;
  }
  
  static void lam_new$0() {}
  
  static CharSequence lam_new$1() {
    String[] arrayOfString = LunariansSettings.categoryNames;
    int i = LunariansSettings.currentCategory;
    return arrayOfString[i];
  }
  
  private void loadSlots() {
    int i = 0;
    while (true) {
      Seq seq = this.slots;
      int j = seq.size;
      if (i < j) {
        Settings settings = Core.settings;
        String str2 = LunariansSettings.slotKey(i);
        String str3 = "";
        String str1 = settings.getString(str2, str3);
        boolean bool = str1.isEmpty();
        if (bool) {
          int k = LunariansSettings.currentCategory;
          if (k == 0) {
            Settings settings1 = Core.settings;
            StringBuilder stringBuilder = new StringBuilder();
            String str5 = "lunarians-slot-";
            this(str5);
            stringBuilder.append(i);
            String str4 = stringBuilder.toString();
            str1 = settings1.getString(str4, str3);
          } 
        } 
        SlotButton slotButton = (SlotButton)this.slots.get(i);
        SlotData slotData = SlotData.deserialize(str1);
        slotButton.setData(slotData, false);
        i++;
        continue;
      } 
      break;
    } 
  }
  
  private void saveCurrentSlots() {
    int i = 0;
    while (true) {
      Seq seq = this.slots;
      int j = seq.size;
      if (i < j) {
        Settings settings = Core.settings;
        String str1 = LunariansSettings.slotKey(i);
        String str2 = ((SlotButton)this.slots.get(i)).getData().serialize();
        settings.put(str1, str2);
        i++;
        continue;
      } 
      Core.settings.saveValues();
      return;
    } 
  }
  
  public void applySettings() {
    float f1 = PanelLayout.panelWidth();
    float f2 = 160.0F;
    int i = 1143930880;
    float f3 = 700.0F;
    f1 = Mathf.clamp(f1, f2, f3);
    boolean bool = LunariansSettings.panelMinimized;
    if (bool) {
      int m = 1109393408;
      f2 = 40.0F;
    } else {
      f2 = PanelLayout.panelHeight();
      i = 1123024896;
      f3 = 120.0F;
      int m = 1142292480;
      float f = 600.0F;
      f2 = Mathf.clamp(f2, f3, f);
    } 
    setSize(f1, f2);
    Graphics graphics1 = Core.graphics;
    i = graphics1.getWidth();
    f3 = i - f1;
    f3 = Math.max(0.0F, f3);
    Graphics graphics2 = Core.graphics;
    int k = graphics2.getHeight();
    float f4 = k - f2;
    f2 = Math.max(0.0F, f4);
    f3 = Mathf.clamp(LunariansSettings.panelX, 0.0F, f3);
    f4 = LunariansSettings.panelY;
    f1 = Mathf.clamp(f4, 0.0F, f2);
    LunariansSettings.panelX = f3;
    LunariansSettings.panelY = f1;
    setPosition(f3, f1);
    f1 = LunariansSettings.opacity;
    f2 = 1.0F;
    setColor(f2, f2, f2, f1);
    Table table = this.body;
    int j = LunariansSettings.panelMinimized ^ true;
    table.visible = j;
    table = this.header;
    String str = "title";
    Element element = table.find(str);
    if (element != null) {
      Element element1 = element;
      element1 = element;
      boolean bool1 = LunariansSettings.showTitle;
      element.visible = bool1;
    } 
    rebuildSlots();
    invalidateHierarchy();
    toFront();
  }
  
  public Table getBody() {
    return this.body;
  }
  
  public Seq getSlots() {
    return this.slots;
  }
  
  public void nextCategory() {
    saveCurrentSlots();
    int i = LunariansSettings.currentCategory + 1;
    int j = LunariansSettings.categoryCount;
    LunariansSettings.currentCategory = i % j;
    LunariansSettings.save();
    rebuildSlots();
  }
  
  public void prevCategory() {
    saveCurrentSlots();
    int i = LunariansSettings.currentCategory + -1;
    int j = LunariansSettings.categoryCount;
    i += j;
    j = LunariansSettings.categoryCount;
    LunariansSettings.currentCategory = i % j;
    LunariansSettings.save();
    rebuildSlots();
  }
  
  public void rebuildSlots() {
    SchematicPlacer.deselect();
    this.content.clear();
    Seq seq = this.slots;
    seq.clear();
    int i = LunariansSettings.slotCount;
    boolean bool = true;
    float f1 = 4.2E-43F;
    i = Mathf.clamp(i, bool, 300);
    float f2 = LunariansSettings.slotSize;
    int j = PanelLayout.columns();
    if (j <= 0) {
      f1 = Math.max(getWidth() - 24.0F, 120.0F);
      float f = 6.0F + f2;
      f1 /= f;
      j = (int)f1;
      byte b1 = 2;
      f = 2.8E-45F;
      byte b2 = 8;
      j = Mathf.clamp(j, b1, b2);
    } 
    int k = 0;
    float f3 = 0.0F;
    while (k < i) {
      SlotButton slotButton = new SlotButton();
      this(k);
      this.slots.add(slotButton);
      Table table = this.content;
      Cell cell = table.add((Element)slotButton);
      cell.size(f2);
      int m = ++k % j;
      if (m == 0) {
        Table table1 = this.content;
        table1.row();
      } 
    } 
    loadSlots();
  }
  
  public void saveSlot(int paramInt, SlotData paramSlotData) {
    if (paramInt >= 0) {
      Seq seq = this.slots;
      int i = seq.size;
      if (paramInt < i) {
        Settings settings1 = Core.settings;
        String str2 = LunariansSettings.slotKey(paramInt);
        String str1 = paramSlotData.serialize();
        settings1.put(str2, str1);
        Settings settings2 = Core.settings;
        settings2.saveValues();
      } 
    } 
  }
  
  public void toggleMinimize() {
    PanelMinimizeAnimation.toggle(this);
  }
}
