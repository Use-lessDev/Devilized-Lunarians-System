/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.func.Boolf
 *  arc.func.Cons
 *  arc.graphics.g2d.TextureAtlas$AtlasRegion
 *  arc.graphics.g2d.TextureRegion
 *  arc.scene.style.Drawable
 *  arc.scene.style.TextureRegionDrawable
 *  arc.scene.ui.ImageButton
 *  arc.scene.ui.TextField
 *  arc.scene.ui.layout.Table
 *  mindustry.Vars
 *  mindustry.game.Schematics
 *  mindustry.gen.Icon
 *  mindustry.ui.Styles
 *  mindustry.ui.dialogs.BaseDialog
 *  mindustry.world.Block
 */
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
import lunarians.ui.SlotButton;
import mindustry.Vars;
import mindustry.game.Schematics;
import mindustry.gen.Icon;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;

public class SchematicInputDialog
extends BaseDialog {
    private TextField codeField;
    private ImageButton iconPreview;
    private String selectedIcon = null;
    private final SlotButton targetSlot;

    public SchematicInputDialog(SlotButton slotButton) {
        super(LunariansI18n.get("configure_slot") + (slotButton.getIndex() + 1));
        this.targetSlot = slotButton;
        this.cont.defaults().pad(6.0f);
        this.cont.add((CharSequence)LunariansI18n.get("schematic_code")).left().row();
        this.codeField = (TextField)this.cont.field("", new Cons(){

            public final void get(Object object) {
                SchematicInputDialog.lam_new$0((String)object);
            }
        }).growX().height(40.0f).get();
        this.codeField.setMessageText(LunariansI18n.get("paste_code"));
        this.cont.row();
        this.cont.add((CharSequence)LunariansI18n.get("icon")).left().padTop(12.0f).row();
        this.cont.table(new Cons(){

            public final void get(Object object) {
                SchematicInputDialog.this.lam_new$3((Table)object);
            }
        }).left().row();
        this.buttons.defaults().size(140.0f, 50.0f).pad(4.0f);
        this.buttons.button(LunariansI18n.get("back"), new Runnable(){

            @Override
            public final void run() {
                SchematicInputDialog.this.hide();
            }
        });
        this.buttons.button(LunariansI18n.get("save"), new Runnable(){

            @Override
            public final void run() {
                SchematicInputDialog.this.save();
            }
        });
        if (slotButton.getData().hasSchematic()) {
            this.codeField.setText(slotButton.getData().schematicBase64);
            if (slotButton.getData().iconName != null) {
                this.selectedIcon = slotButton.getData().iconName;
                this.updateIconPreview();
            }
        }
        this.closeOnBack();
    }

    static void lam_new$0(String string) {
    }

    static void lam_new$1() {
    }

    public void lam_new$3(Table table) {
        this.iconPreview = (ImageButton)table.button((Drawable)Icon.file, Styles.clearNonei, new Runnable(){

            @Override
            public final void run() {
                SchematicInputDialog.lam_new$1();
            }
        }).size(48.0f).get();
        this.iconPreview.setDisabled(true);
        table.button(LunariansI18n.get("choose_icon"), new Runnable(){

            @Override
            public final void run() {
                SchematicInputDialog.this.lam_new$2();
            }
        }).size(160.0f, 48.0f).padLeft(8.0f);
    }

    public void lam_new$2() {
        final BaseDialog baseDialog = new BaseDialog(LunariansI18n.get("choose_icon_title"));
        baseDialog.cont.pane(new Cons(){

            public final void get(Object object) {
                SchematicInputDialog.this.lam_openIconPicker$6(baseDialog, (Table)object);
            }
        }).grow().maxHeight((float)Core.graphics.getHeight() * 0.6f);
        Table table = baseDialog.buttons;
        String string = LunariansI18n.get("back");
        Objects.requireNonNull(baseDialog);
        table.button(string, new Runnable(){

            @Override
            public final void run() {
                baseDialog.hide();
            }
        }).size(140.0f, 50.0f);
        baseDialog.closeOnBack();
        baseDialog.show();
    }

    public void lam_openIconPicker$6(final BaseDialog baseDialog, Table table) {
        table.defaults().size(48.0f).pad(3.0f);
        Iterator iterator = Vars.content.blocks().select(new Boolf(){

            public final boolean get(Object object) {
                return SchematicInputDialog.lam_openIconPicker$4((Block)object);
            }
        }).iterator();
        int n = 0;
        while (iterator.hasNext()) {
            final Block block = (Block)iterator.next();
            TextureRegion textureRegion = block.uiIcon;
            if (textureRegion == null || !textureRegion.found()) continue;
            ((ImageButton)table.button((Drawable)new TextureRegionDrawable(textureRegion), Styles.clearNonei, new Runnable(){

                @Override
                public final void run() {
                    SchematicInputDialog.this.lam_openIconPicker$5(block, baseDialog);
                }
            }).get()).resizeImage(32.0f);
            if (++n % 8 != 0) continue;
            table.row();
        }
    }

    static boolean lam_openIconPicker$4(Block block) {
        return block.isVisible() && block.synthetic() && !block.isHidden();
    }

    public void lam_openIconPicker$5(Block block, BaseDialog baseDialog) {
        this.selectedIcon = block.name;
        this.updateIconPreview();
        baseDialog.hide();
    }

    private void updateIconPreview() {
        TextureAtlas.AtlasRegion atlasRegion;
        if (this.selectedIcon == null) {
            this.iconPreview.getStyle().imageUp = Icon.file;
            return;
        }
        Block block = Vars.content.block(this.selectedIcon);
        this.iconPreview.getStyle().imageUp = block != null && block.uiIcon != null ? new TextureRegionDrawable(block.uiIcon) : ((atlasRegion = Core.atlas.find(this.selectedIcon)) != null && atlasRegion.found() ? new TextureRegionDrawable((TextureRegion)atlasRegion) : Icon.file);
        this.iconPreview.invalidateHierarchy();
    }

    public void save() {
        String string = this.codeField.getText().trim();
        if (string.isEmpty()) {
            Vars.ui.showErrorMessage(LunariansI18n.get("empty_code"));
            return;
        }
        try {
            Schematics.readBase64((String)string);
            SlotData slotData = new SlotData();
            slotData.set(string, this.selectedIcon);
            this.targetSlot.setData(slotData);
            this.hide();
        }
        catch (Exception exception) {
            Vars.ui.showErrorMessage(LunariansI18n.get("invalid_schematic"));
        }
    }
}

