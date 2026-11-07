package lunarians.ui;

import arc.Core;
import arc.func.Boolf;
import arc.func.Cons;
import arc.graphics.g2d.TextureAtlas;
import arc.graphics.g2d.TextureRegion;
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

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/SchematicInputDialog.class */
public class SchematicInputDialog extends BaseDialog {
    private TextField codeField;
    private ImageButton iconPreview;
    private String selectedIcon;
    private final SlotButton targetSlot;

    public SchematicInputDialog(SlotButton slotButton) {
        super(LunariansI18n.get("configure_slot") + (slotButton.getIndex() + 1));
        this.selectedIcon = null;
        this.targetSlot = slotButton;
        this.cont.defaults().pad(6.0f);
        this.cont.add(LunariansI18n.get("schematic_code")).left().row();
        this.codeField = this.cont.field("", new Cons() { // from class: lunarians.ui.SchematicInputDialog.1
            public final void get(Object obj) {
                SchematicInputDialog.lam_new$0((String) obj);
            }
        }).growX().height(40.0f).get();
        this.codeField.setMessageText(LunariansI18n.get("paste_code"));
        this.cont.row();
        this.cont.add(LunariansI18n.get("icon")).left().padTop(12.0f).row();
        this.cont.table(new Cons() { // from class: lunarians.ui.SchematicInputDialog.2
            public final void get(Object obj) {
                SchematicInputDialog.this.lam_new$3((Table) obj);
            }
        }).left().row();
        this.buttons.defaults().size(140.0f, 50.0f).pad(4.0f);
        this.buttons.button(LunariansI18n.get("back"), new Runnable() { // from class: lunarians.ui.SchematicInputDialog.3
            @Override // java.lang.Runnable
            public final void run() {
                SchematicInputDialog.this.hide();
            }
        });
        this.buttons.button(LunariansI18n.get("save"), new Runnable() { // from class: lunarians.ui.SchematicInputDialog.4
            @Override // java.lang.Runnable
            public final void run() {
                SchematicInputDialog.this.save();
            }
        });
        if (slotButton.getData().hasSchematic()) {
            this.codeField.setText(slotButton.getData().schematicBase64);
            if (slotButton.getData().iconName != null) {
                this.selectedIcon = slotButton.getData().iconName;
                updateIconPreview();
            }
        }
        closeOnBack();
    }

    static void lam_new$0(String str) {
    }

    static void lam_new$1() {
    }

    public void lam_new$3(Table table) {
        this.iconPreview = table.button(Icon.file, Styles.clearNonei, new Runnable() { // from class: lunarians.ui.SchematicInputDialog.5
            @Override // java.lang.Runnable
            public final void run() {
                SchematicInputDialog.lam_new$1();
            }
        }).size(48.0f).get();
        this.iconPreview.setDisabled(true);
        table.button(LunariansI18n.get("choose_icon"), new Runnable() { // from class: lunarians.ui.SchematicInputDialog.6
            @Override // java.lang.Runnable
            public final void run() {
                SchematicInputDialog.this.lam_new$2();
            }
        }).size(160.0f, 48.0f).padLeft(8.0f);
    }

    public void lam_new$2() {
        final BaseDialog baseDialog = new BaseDialog(LunariansI18n.get("choose_icon_title"));
        baseDialog.cont.pane(new Cons() { // from class: lunarians.ui.SchematicInputDialog.7
            public final void get(Object obj) {
                SchematicInputDialog.this.lam_openIconPicker$6(baseDialog, (Table) obj);
            }
        }).grow().maxHeight(Core.graphics.getHeight() * 0.6f);
        Table table = baseDialog.buttons;
        String str = LunariansI18n.get("back");
        Objects.requireNonNull(baseDialog);
        table.button(str, new Runnable() { // from class: lunarians.ui.SchematicInputDialog.8
            @Override // java.lang.Runnable
            public final void run() {
                baseDialog.hide();
            }
        }).size(140.0f, 50.0f);
        baseDialog.closeOnBack();
        baseDialog.show();
    }

    public void lam_openIconPicker$6(final BaseDialog baseDialog, Table table) {
        table.defaults().size(48.0f).pad(3.0f);
        Iterator it = Vars.content.blocks().select(new Boolf() { // from class: lunarians.ui.SchematicInputDialog.9
            public final boolean get(Object obj) {
                return SchematicInputDialog.lam_openIconPicker$4((Block) obj);
            }
        }).iterator();
        int i = 0;
        while (it.hasNext()) {
            final Block block = (Block) it.next();
            TextureRegion textureRegion = block.uiIcon;
            if (textureRegion != null && textureRegion.found()) {
                table.button(new TextureRegionDrawable(textureRegion), Styles.clearNonei, new Runnable() { // from class: lunarians.ui.SchematicInputDialog.10
                    @Override // java.lang.Runnable
                    public final void run() {
                        SchematicInputDialog.this.lam_openIconPicker$5(block, baseDialog);
                    }
                }).get().resizeImage(32.0f);
                i++;
                if (i % 8 == 0) {
                    table.row();
                }
            }
        }
    }

    static boolean lam_openIconPicker$4(Block block) {
        return block.isVisible() && block.synthetic() && !block.isHidden();
    }

    public void lam_openIconPicker$5(Block block, BaseDialog baseDialog) {
        this.selectedIcon = block.name;
        updateIconPreview();
        baseDialog.hide();
    }

    private void updateIconPreview() {
        TextureRegionDrawable textureRegionDrawable;
        if (this.selectedIcon == null) {
            this.iconPreview.getStyle().imageUp = Icon.file;
            return;
        }
        Block block = Vars.content.block(this.selectedIcon);
        ImageButton.ImageButtonStyle style = this.iconPreview.getStyle();
        if (block == null || block.uiIcon == null) {
            TextureAtlas.AtlasRegion find = Core.atlas.find(this.selectedIcon);
            textureRegionDrawable = (find == null || !find.found()) ? Icon.file : new TextureRegionDrawable(find);
        } else {
            textureRegionDrawable = new TextureRegionDrawable(block.uiIcon);
        }
        style.imageUp = textureRegionDrawable;
        this.iconPreview.invalidateHierarchy();
    }

    public void save() {
        String trim = this.codeField.getText().trim();
        if (trim.isEmpty()) {
            Vars.ui.showErrorMessage(LunariansI18n.get("empty_code"));
            return;
        }
        try {
            Schematics.readBase64(trim);
            SlotData slotData = new SlotData();
            slotData.set(trim, this.selectedIcon);
            this.targetSlot.setData(slotData);
            hide();
        } catch (Exception e) {
            Vars.ui.showErrorMessage(LunariansI18n.get("invalid_schematic"));
        }
    }
}
