package lunarians.ui;

import arc.Core;
import arc.func.Boolf;
import arc.func.Cons;
import arc.graphics.g2d.TextureAtlas;
import arc.graphics.g2d.TextureRegion;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import java.util.Iterator;
import java.util.Objects;
import lunarians.LunariansI18n;
import lunarians.data.SlotData;
import mindustry.Vars;
import mindustry.game.Schematics;
import mindustry.gen.Icon;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;

public class SchematicInputDialog extends BaseDialog {
  private TextField codeField;
  
  private ImageButton iconPreview;
  
  private String selectedIcon = null;
  
  private final SlotButton targetSlot;
  
  public SchematicInputDialog(SlotButton paramSlotButton) {
    super(LunariansI18n.get("configure_slot") + (paramSlotButton.getIndex() + 1));
    this.targetSlot = paramSlotButton;
    this.cont.defaults().pad(6.0F);
    this.cont.add(LunariansI18n.get("schematic_code")).left().row();
    this



      
      .codeField = (TextField)this.cont.field("", new Cons() {
          public final void get(Object param1Object) {
            SchematicInputDialog.lam_new$0((String)param1Object);
          }
        }).growX().height(40.0F).get();
    this.codeField.setMessageText(LunariansI18n.get("paste_code"));
    this.cont.row();
    this.cont.add(LunariansI18n.get("icon")).left().padTop(12.0F).row();
    this.cont.table(new Cons() {
          public final void get(Object param1Object) {
            SchematicInputDialog.this.lam_new$3((Table)param1Object);
          }
        }).left().row();
    this.buttons.defaults().size(140.0F, 50.0F).pad(4.0F);
    this.buttons.button(LunariansI18n.get("back"), new Runnable() {
          public final void run() {
            SchematicInputDialog.this.hide();
          }
        });
    this.buttons.button(LunariansI18n.get("save"), new Runnable() {
          public final void run() {
            SchematicInputDialog.this.save();
          }
        });
    if (paramSlotButton.getData().hasSchematic()) {
      this.codeField.setText((paramSlotButton.getData()).schematicBase64);
      if ((paramSlotButton.getData()).iconName != null) {
        this.selectedIcon = (paramSlotButton.getData()).iconName;
        updateIconPreview();
      } 
    } 
    closeOnBack();
  }
  
  static void lam_new$0(String paramString) {}
  
  static void lam_new$1() {}
  
  public void lam_new$3(Table paramTable) {
    this




      
      .iconPreview = (ImageButton)paramTable.button((Drawable)Icon.file, Styles.clearNonei, new Runnable() {
          public final void run() {
            SchematicInputDialog.lam_new$1();
          }
        }).size(48.0F).get();
    this.iconPreview.setDisabled(true);
    paramTable.button(LunariansI18n.get("choose_icon"), new Runnable() {
          public final void run() {
            SchematicInputDialog.this.lam_new$2();
          }
        }).size(160.0F, 48.0F).padLeft(8.0F);
  }
  
  public void lam_new$2() {
    final BaseDialog baseDialog = new BaseDialog(LunariansI18n.get("choose_icon_title"));
    baseDialog.cont.pane(new Cons() {
          public final void get(Object param1Object) {
            SchematicInputDialog.this.lam_openIconPicker$6(baseDialog, (Table)param1Object);
          }
        }).grow().maxHeight(Core.graphics.getHeight() * 0.6F);
    Table table = baseDialog.buttons;
    String str = LunariansI18n.get("back");
    Objects.requireNonNull(baseDialog);
    table.button(str, new Runnable() {
          public final void run() {
            baseDialog.hide();
          }
        }).size(140.0F, 50.0F);
    baseDialog.closeOnBack();
    baseDialog.show();
  }
  
  public void lam_openIconPicker$6(final BaseDialog baseDialog, Table paramTable) {
    paramTable.defaults().size(48.0F).pad(3.0F);
    Iterator<Block> iterator = Vars.content.blocks().select(new Boolf() {
          public final boolean get(Object param1Object) {
            return SchematicInputDialog.lam_openIconPicker$4((Block)param1Object);
          }
        }).iterator();
    byte b = 0;
    while (iterator.hasNext()) {
      final Block block = iterator.next();
      TextureRegion textureRegion = block.uiIcon;
      if (textureRegion == null || !textureRegion.found())
        continue; 
      ((ImageButton)paramTable.button((Drawable)new TextureRegionDrawable(textureRegion), Styles.clearNonei, new Runnable() {
            public final void run() {
              SchematicInputDialog.this.lam_openIconPicker$5(block, baseDialog);
            }
          }).get()).resizeImage(32.0F);
      if (++b % 8 != 0)
        continue; 
      paramTable.row();
    } 
  }
  
  static boolean lam_openIconPicker$4(Block paramBlock) {
    return (paramBlock.isVisible() && paramBlock.synthetic() && !paramBlock.isHidden());
  }
  
  public void lam_openIconPicker$5(Block paramBlock, BaseDialog paramBaseDialog) {
    this.selectedIcon = paramBlock.name;
    updateIconPreview();
    paramBaseDialog.hide();
  }
  
  private void updateIconPreview() {
    if (this.selectedIcon == null) {
      (this.iconPreview.getStyle()).imageUp = (Drawable)Icon.file;
      return;
    } 
    Block block = Vars.content.block(this.selectedIcon);
    TextureAtlas.AtlasRegion atlasRegion;
    (this.iconPreview.getStyle()).imageUp = (block != null && block.uiIcon != null) ? (Drawable)new TextureRegionDrawable(block.uiIcon) : (((atlasRegion = Core.atlas.find(this.selectedIcon)) != null && atlasRegion.found()) ? (Drawable)new TextureRegionDrawable((TextureRegion)atlasRegion) : (Drawable)Icon.file);
    this.iconPreview.invalidateHierarchy();
  }
  
  public void save() {
    String str = this.codeField.getText().trim();
    if (str.isEmpty()) {
      Vars.ui.showErrorMessage(LunariansI18n.get("empty_code"));
      return;
    } 
    try {
      Schematics.readBase64(str);
      SlotData slotData = new SlotData();
      slotData.set(str, this.selectedIcon);
      this.targetSlot.setData(slotData);
      hide();
    } catch (Exception exception) {
      Vars.ui.showErrorMessage(LunariansI18n.get("invalid_schematic"));
    } 
  }
}
