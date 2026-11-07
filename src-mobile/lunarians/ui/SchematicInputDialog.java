package lunarians.ui;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import java.util.Iterator;
import lunarians.LunariansI18n;
import lunarians.data.SlotData;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.game.Schematics;
import mindustry.gen.Icon;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;

public class SchematicInputDialog extends BaseDialog {
  private TextField codeField;
  
  private ImageButton iconPreview;
  
  private String selectedIcon;
  
  private final SlotButton targetSlot;
  
  public SchematicInputDialog(SlotButton paramSlotButton) {
    this();
    String str4 = LunariansI18n.get("configure_slot");
    stringBuilder.append(str4);
    int i = paramSlotButton.getIndex() + 1;
    stringBuilder.append(i);
    String str1 = stringBuilder.toString();
    super(str1);
    this.selectedIcon = null;
    this.targetSlot = paramSlotButton;
    this.cont.defaults().pad(6.0F);
    Table table3 = this.cont;
    str4 = LunariansI18n.get("schematic_code");
    table3.add(str4).left().row();
    table3 = this.cont;
    SchematicInputDialog$1 schematicInputDialog$1 = new SchematicInputDialog$1();
    this(this);
    TextField textField = (TextField)table3.field("", schematicInputDialog$1).growX().height(40.0F).get();
    this.codeField = textField;
    String str3 = LunariansI18n.get("paste_code");
    textField.setMessageText(str3);
    this.cont.row();
    Table table2 = this.cont;
    str3 = LunariansI18n.get("icon");
    table2.add(str3).left().padTop(12.0F).row();
    table2 = this.cont;
    SchematicInputDialog$2 schematicInputDialog$2 = new SchematicInputDialog$2();
    this(this);
    table2.table(schematicInputDialog$2).left().row();
    Cell cell = this.buttons.defaults();
    float f1 = 50.0F;
    cell = cell.size(140.0F, f1);
    i = 1082130432;
    float f2 = 4.0F;
    cell.pad(f2);
    Table table1 = this.buttons;
    String str2 = LunariansI18n.get("back");
    SchematicInputDialog$3 schematicInputDialog$3 = new SchematicInputDialog$3();
    this(this);
    table1.button(str2, schematicInputDialog$3);
    table1 = this.buttons;
    str2 = LunariansI18n.get("save");
    SchematicInputDialog$4 schematicInputDialog$4 = new SchematicInputDialog$4();
    this(this);
    table1.button(str2, schematicInputDialog$4);
    SlotData slotData = paramSlotButton.getData();
    boolean bool = slotData.hasSchematic();
    if (bool) {
      TextField textField1 = this.codeField;
      str2 = (paramSlotButton.getData()).schematicBase64;
      textField1.setText(str2);
      String str = (paramSlotButton.getData()).iconName;
      if (str != null) {
        String str5 = (paramSlotButton.getData()).iconName;
        this.selectedIcon = str5;
        updateIconPreview();
      } 
    } 
    closeOnBack();
  }
  
  static void lam_new$0(String paramString) {}
  
  static void lam_new$1() {}
  
  static boolean lam_openIconPicker$4(Block paramBlock) {
    boolean bool = paramBlock.isVisible();
    if (bool) {
      bool = paramBlock.synthetic();
      if (bool) {
        boolean bool1 = paramBlock.isHidden();
        if (!bool1)
          return true; 
      } 
    } 
    return false;
  }
  
  private void updateIconPreview() {
    // Byte code:
    //   0: aload_0
    //   1: getfield selectedIcon : Ljava/lang/String;
    //   4: astore_1
    //   5: aload_1
    //   6: ifnonnull -> 27
    //   9: aload_0
    //   10: getfield iconPreview : Larc/scene/ui/ImageButton;
    //   13: invokevirtual getStyle : ()Larc/scene/ui/ImageButton$ImageButtonStyle;
    //   16: astore_1
    //   17: getstatic mindustry/gen/Icon.file : Larc/scene/style/TextureRegionDrawable;
    //   20: astore_2
    //   21: aload_1
    //   22: aload_2
    //   23: putfield imageUp : Larc/scene/style/Drawable;
    //   26: return
    //   27: getstatic mindustry/Vars.content : Lmindustry/core/ContentLoader;
    //   30: astore_1
    //   31: aload_0
    //   32: getfield selectedIcon : Ljava/lang/String;
    //   35: astore_2
    //   36: aload_1
    //   37: aload_2
    //   38: invokevirtual block : (Ljava/lang/String;)Lmindustry/world/Block;
    //   41: astore_1
    //   42: aload_0
    //   43: getfield iconPreview : Larc/scene/ui/ImageButton;
    //   46: invokevirtual getStyle : ()Larc/scene/ui/ImageButton$ImageButtonStyle;
    //   49: astore_2
    //   50: aload_1
    //   51: ifnull -> 80
    //   54: aload_1
    //   55: getfield uiIcon : Larc/graphics/g2d/TextureRegion;
    //   58: astore_3
    //   59: aload_3
    //   60: ifnull -> 80
    //   63: new arc/scene/style/TextureRegionDrawable
    //   66: astore_3
    //   67: aload_1
    //   68: getfield uiIcon : Larc/graphics/g2d/TextureRegion;
    //   71: astore_1
    //   72: aload_3
    //   73: aload_1
    //   74: invokespecial <init> : (Larc/graphics/g2d/TextureRegion;)V
    //   77: goto -> 126
    //   80: getstatic arc/Core.atlas : Larc/graphics/g2d/TextureAtlas;
    //   83: astore_1
    //   84: aload_0
    //   85: getfield selectedIcon : Ljava/lang/String;
    //   88: astore_3
    //   89: aload_1
    //   90: aload_3
    //   91: invokevirtual find : (Ljava/lang/String;)Larc/graphics/g2d/TextureAtlas$AtlasRegion;
    //   94: astore_1
    //   95: aload_1
    //   96: ifnull -> 122
    //   99: aload_1
    //   100: invokevirtual found : ()Z
    //   103: istore #4
    //   105: iload #4
    //   107: ifeq -> 122
    //   110: new arc/scene/style/TextureRegionDrawable
    //   113: astore_3
    //   114: aload_3
    //   115: aload_1
    //   116: invokespecial <init> : (Larc/graphics/g2d/TextureRegion;)V
    //   119: goto -> 126
    //   122: getstatic mindustry/gen/Icon.file : Larc/scene/style/TextureRegionDrawable;
    //   125: astore_3
    //   126: aload_2
    //   127: aload_3
    //   128: putfield imageUp : Larc/scene/style/Drawable;
    //   131: aload_0
    //   132: getfield iconPreview : Larc/scene/ui/ImageButton;
    //   135: invokevirtual invalidateHierarchy : ()V
    //   138: return
  }
  
  public void lam_new$2() {
    BaseDialog baseDialog = new BaseDialog();
    String str1 = LunariansI18n.get("choose_icon_title");
    this(str1);
    Table table2 = baseDialog.cont;
    SchematicInputDialog$7 schematicInputDialog$7 = new SchematicInputDialog$7();
    this(this, baseDialog);
    Cell cell = table2.pane(schematicInputDialog$7).grow();
    float f = Core.graphics.getHeight() * 0.6F;
    cell.maxHeight(f);
    Table table1 = baseDialog.buttons;
    String str2 = LunariansI18n.get("back");
    SchematicInputDialog$8 schematicInputDialog$8 = new SchematicInputDialog$8();
    this(this, baseDialog);
    table1.button(str2, schematicInputDialog$8).size(140.0F, 50.0F);
    baseDialog.closeOnBack();
    baseDialog.show();
  }
  
  public void lam_new$3(Table paramTable) {
    TextureRegionDrawable textureRegionDrawable = Icon.file;
    ImageButton.ImageButtonStyle imageButtonStyle = Styles.clearNonei;
    SchematicInputDialog$5 schematicInputDialog$5 = new SchematicInputDialog$5();
    this(this);
    Cell cell = paramTable.button((Drawable)textureRegionDrawable, imageButtonStyle, schematicInputDialog$5);
    float f = 48.0F;
    ImageButton imageButton = (ImageButton)cell.size(f).get();
    this.iconPreview = imageButton;
    imageButton.setDisabled(true);
    String str = LunariansI18n.get("choose_icon");
    SchematicInputDialog$6 schematicInputDialog$6 = new SchematicInputDialog$6();
    this(this);
    paramTable.button(str, schematicInputDialog$6).size(160.0F, f).padLeft(8.0F);
  }
  
  public void lam_openIconPicker$5(Block paramBlock, BaseDialog paramBaseDialog) {
    String str = paramBlock.name;
    this.selectedIcon = str;
    updateIconPreview();
    paramBaseDialog.hide();
  }
  
  public void lam_openIconPicker$6(BaseDialog paramBaseDialog, Table paramTable) {
    paramTable.defaults().size(48.0F).pad(3.0F);
    Seq seq = Vars.content.blocks();
    SchematicInputDialog$9 schematicInputDialog$9 = new SchematicInputDialog$9();
    this(this);
    Iterator<Block> iterator = seq.select(schematicInputDialog$9).iterator();
    int i = 0;
    schematicInputDialog$9 = null;
    while (true) {
      boolean bool = iterator.hasNext();
      if (bool) {
        Block block = iterator.next();
        TextureRegion textureRegion = block.uiIcon;
        if (textureRegion != null) {
          boolean bool1 = textureRegion.found();
          if (bool1) {
            TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable();
            this(textureRegion);
            ImageButton.ImageButtonStyle imageButtonStyle = Styles.clearNonei;
            SchematicInputDialog$10 schematicInputDialog$10 = new SchematicInputDialog$10();
            this(this, block, paramBaseDialog);
            ImageButton imageButton = (ImageButton)paramTable.button((Drawable)textureRegionDrawable, imageButtonStyle, schematicInputDialog$10).get();
            float f = 32.0F;
            imageButton.resizeImage(f);
            int j = ++i % 8;
            if (j == 0)
              paramTable.row(); 
          } 
        } 
        continue;
      } 
      break;
    } 
  }
  
  public void save() {
    UI uI;
    String str = this.codeField.getText().trim();
    boolean bool = str.isEmpty();
    if (bool) {
      uI = Vars.ui;
      String str1 = LunariansI18n.get("empty_code");
      uI.showErrorMessage(str1);
      return;
    } 
    try {
      Schematics.readBase64((String)uI);
      SlotData slotData = new SlotData();
      this();
      String str1 = this.selectedIcon;
      slotData.set((String)uI, str1);
      SlotButton slotButton = this.targetSlot;
      slotButton.setData(slotData);
      hide();
      return;
    } catch (Exception exception) {
      uI = Vars.ui;
      String str1 = LunariansI18n.get("invalid_schematic");
      uI.showErrorMessage(str1);
      return;
    } 
  }
}
