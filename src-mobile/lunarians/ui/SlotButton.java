package lunarians.ui;

import arc.graphics.Color;
import arc.scene.event.EventListener;
import arc.scene.style.Drawable;
import arc.scene.ui.ImageButton;
import arc.util.Log;
import arc.util.Time;
import lunarians.LunariansMod;
import lunarians.LunariansSettings;
import lunarians.SchematicPlacer;
import lunarians.data.SlotData;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.game.Schematic;
import mindustry.game.Schematics;
import mindustry.gen.Tex;
import mindustry.ui.Styles;

public class SlotButton extends ImageButton {
  private static ImageButton.ImageButtonStyle style;
  
  private SlotData data;
  
  private boolean dirty;
  
  private float holdTime;
  
  private boolean holding;
  
  private final int index;
  
  public SlotButton(int paramInt) {
    super(imageButtonStyle);
    SlotData slotData = new SlotData();
    this();
    this.data = slotData;
    this.holdTime = 0.0F;
    this.holding = false;
    this.dirty = true;
    this.index = paramInt;
    setSize(48.0F);
    updateVisual();
    SlotButton$1 slotButton$1 = new SlotButton$1();
    this(this);
    addListener((EventListener)slotButton$1);
    SlotButton$2 slotButton$2 = new SlotButton$2();
    this(this);
    update(slotButton$2);
  }
  
  private static float iconSize() {
    float f = LunariansSettings.slotSize * 0.6F;
    return Math.max(20.0F, f);
  }
  
  private void placeSchematic() {
    boolean bool = SchematicPlacer.isSelected(this);
    if (bool) {
      SchematicPlacer.deselect();
      return;
    } 
    try {
      SlotData slotData = this.data;
      String str = slotData.schematicBase64;
      Schematic schematic = Schematics.readBase64(str);
      if (schematic != null)
        SchematicPlacer.select(this, schematic); 
      return;
    } catch (Exception exception) {
      UI uI = Vars.ui;
      StringBuilder stringBuilder = new StringBuilder();
      this("Schematic inválida:\n");
      String str2 = exception.getMessage();
      stringBuilder.append(str2);
      String str1 = stringBuilder.toString();
      uI.showErrorMessage(str1);
      Log.err(exception);
      return;
    } 
  }
  
  private static ImageButton.ImageButtonStyle selectStyle() {
    ImageButton.ImageButtonStyle imageButtonStyle = style;
    if (imageButtonStyle == null) {
      imageButtonStyle = new ImageButton.ImageButtonStyle();
      ImageButton.ImageButtonStyle imageButtonStyle1 = Styles.defaulti;
      this(imageButtonStyle1);
      style = imageButtonStyle;
      Drawable drawable = Tex.buttonSelect;
      imageButtonStyle.checked = drawable;
      imageButtonStyle = style;
      drawable = Tex.buttonSelect;
      imageButtonStyle.checkedOver = drawable;
    } 
    return style;
  }
  
  private void updateVisual() {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual clearChildren : ()V
    //   4: aload_0
    //   5: getfield data : Llunarians/data/SlotData;
    //   8: astore_1
    //   9: aload_1
    //   10: invokevirtual hasSchematic : ()Z
    //   13: istore_2
    //   14: iload_2
    //   15: ifeq -> 173
    //   18: getstatic mindustry/gen/Icon.file : Larc/scene/style/TextureRegionDrawable;
    //   21: invokevirtual getRegion : ()Larc/graphics/g2d/TextureRegion;
    //   24: astore_1
    //   25: aload_0
    //   26: getfield data : Llunarians/data/SlotData;
    //   29: getfield iconName : Ljava/lang/String;
    //   32: astore_3
    //   33: aload_3
    //   34: ifnull -> 154
    //   37: aload_0
    //   38: getfield data : Llunarians/data/SlotData;
    //   41: getfield iconName : Ljava/lang/String;
    //   44: astore_3
    //   45: aload_3
    //   46: invokevirtual isEmpty : ()Z
    //   49: istore #4
    //   51: iload #4
    //   53: ifne -> 154
    //   56: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   59: astore_3
    //   60: aload_0
    //   61: getfield data : Llunarians/data/SlotData;
    //   64: getfield iconName : Ljava/lang/String;
    //   67: astore #5
    //   69: aload_3
    //   70: aload #5
    //   72: invokevirtual block : (Ljava/lang/String;)Lmindustry/world/Block;
    //   75: astore_3
    //   76: aload_3
    //   77: ifnull -> 117
    //   80: aload_3
    //   81: getfield uiIcon : Larc/graphics/g2d/TextureRegion;
    //   84: astore #5
    //   86: aload #5
    //   88: ifnull -> 117
    //   91: aload_3
    //   92: getfield uiIcon : Larc/graphics/g2d/TextureRegion;
    //   95: astore #5
    //   97: aload #5
    //   99: invokevirtual found : ()Z
    //   102: istore #6
    //   104: iload #6
    //   106: ifeq -> 117
    //   109: aload_3
    //   110: getfield uiIcon : Larc/graphics/g2d/TextureRegion;
    //   113: astore_1
    //   114: goto -> 154
    //   117: getstatic arc/Core.atlas : Larc/graphics/g2d/TextureAtlas;
    //   120: astore_3
    //   121: aload_0
    //   122: getfield data : Llunarians/data/SlotData;
    //   125: getfield iconName : Ljava/lang/String;
    //   128: astore #5
    //   130: aload_3
    //   131: aload #5
    //   133: invokevirtual find : (Ljava/lang/String;)Larc/graphics/g2d/TextureAtlas$AtlasRegion;
    //   136: astore_3
    //   137: aload_3
    //   138: ifnull -> 154
    //   141: aload_3
    //   142: invokevirtual found : ()Z
    //   145: istore #6
    //   147: iload #6
    //   149: ifeq -> 154
    //   152: aload_3
    //   153: astore_1
    //   154: aload_0
    //   155: aload_1
    //   156: invokevirtual image : (Larc/graphics/g2d/TextureRegion;)Larc/scene/ui/layout/Cell;
    //   159: astore_1
    //   160: invokestatic iconSize : ()F
    //   163: fstore #7
    //   165: aload_1
    //   166: fload #7
    //   168: invokevirtual size : (F)Larc/scene/ui/layout/Cell;
    //   171: pop
    //   172: return
    //   173: getstatic mindustry/gen/Icon.add : Larc/scene/style/TextureRegionDrawable;
    //   176: astore_1
    //   177: aload_0
    //   178: aload_1
    //   179: invokevirtual image : (Larc/scene/style/Drawable;)Larc/scene/ui/layout/Cell;
    //   182: astore_1
    //   183: invokestatic iconSize : ()F
    //   186: ldc 0.85
    //   188: fmul
    //   189: fstore #7
    //   191: aload_1
    //   192: fload #7
    //   194: invokevirtual size : (F)Larc/scene/ui/layout/Cell;
    //   197: astore_1
    //   198: getstatic arc/graphics/Color.lightGray : Larc/graphics/Color;
    //   201: astore_3
    //   202: aload_1
    //   203: aload_3
    //   204: invokevirtual color : (Larc/graphics/Color;)Larc/scene/ui/layout/Cell;
    //   207: pop
    //   208: return
  }
  
  public void clearSlot() {
    boolean bool = SchematicPlacer.isSelected(this);
    if (bool)
      SchematicPlacer.deselect(); 
    this.data.clear();
    updateVisual();
    SchematicPanel schematicPanel = LunariansMod.panel;
    if (schematicPanel != null) {
      schematicPanel = LunariansMod.panel;
      int i = this.index;
      SlotData slotData = this.data;
      schematicPanel.saveSlot(i, slotData);
    } 
  }
  
  public SlotData getData() {
    return this.data;
  }
  
  public int getIndex() {
    return this.index;
  }
  
  public void lam_new$0() {
    boolean bool = this.holding;
    if (bool) {
      float f1 = this.holdTime;
      float f2 = Time.delta / 60.0F;
      f1 += f2;
      this.holdTime = f1;
      f2 = LunariansSettings.holdSeconds;
      float f3 = 0.7F;
      f2 *= f3;
      int i = f1 cmp f2;
      if (i >= 0) {
        Color color1 = Color.red.cpy();
        Color color2 = Color.white;
        f3 = this.holdTime;
        float f = LunariansSettings.holdSeconds;
        f3 /= f;
        f = 1.0F - f3;
        color1 = color1.lerp(color2, f);
        setColor(color1);
        return;
      } 
    } else {
      bool = SchematicPlacer.isSelected(this);
      boolean bool1 = isChecked();
      if (bool1 != bool)
        setChecked(bool); 
      bool = this.dirty;
      if (bool) {
        bool = false;
        float f = 0.0F;
        this.dirty = false;
        Color color = Color.white;
        setColor(color);
      } 
    } 
  }
  
  public void onClick() {
    SlotData slotData = this.data;
    boolean bool = slotData.hasSchematic();
    if (bool) {
      placeSchematic();
      return;
    } 
    SchematicInputDialog schematicInputDialog = new SchematicInputDialog();
    this(this);
    schematicInputDialog.show();
  }
  
  public void setChecked(boolean paramBoolean) {
    paramBoolean = SchematicPlacer.isSelected(this);
    super.setChecked(paramBoolean);
  }
  
  public void setData(SlotData paramSlotData) {
    setData(paramSlotData, true);
  }
  
  public void setData(SlotData paramSlotData, boolean paramBoolean) {
    this.data = paramSlotData;
    updateVisual();
    if (paramBoolean) {
      SchematicPanel schematicPanel = LunariansMod.panel;
      if (schematicPanel != null) {
        schematicPanel = LunariansMod.panel;
        int i = this.index;
        SlotData slotData = this.data;
        schematicPanel.saveSlot(i, slotData);
      } 
    } 
  }
}
