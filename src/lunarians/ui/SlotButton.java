/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.graphics.Color
 *  arc.graphics.g2d.TextureAtlas$AtlasRegion
 *  arc.graphics.g2d.TextureRegion
 *  arc.input.KeyCode
 *  arc.scene.event.ClickListener
 *  arc.scene.event.EventListener
 *  arc.scene.event.InputEvent
 *  arc.scene.style.Drawable
 *  arc.scene.ui.ImageButton
 *  arc.scene.ui.ImageButton$ImageButtonStyle
 *  arc.util.Log
 *  arc.util.Time
 *  mindustry.Vars
 *  mindustry.game.Schematic
 *  mindustry.game.Schematics
 *  mindustry.gen.Icon
 *  mindustry.gen.Tex
 *  mindustry.ui.Styles
 *  mindustry.world.Block
 */
package lunarians.ui;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.TextureAtlas;
import arc.graphics.g2d.TextureRegion;
import arc.input.KeyCode;
import arc.scene.event.ClickListener;
import arc.scene.event.EventListener;
import arc.scene.event.InputEvent;
import arc.scene.style.Drawable;
import arc.scene.ui.ImageButton;
import arc.util.Log;
import arc.util.Time;
import lunarians.LunariansMod;
import lunarians.LunariansSettings;
import lunarians.SchematicPlacer;
import lunarians.data.SlotData;
import lunarians.ui.SchematicInputDialog;
import mindustry.Vars;
import mindustry.game.Schematic;
import mindustry.game.Schematics;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.ui.Styles;
import mindustry.world.Block;

public class SlotButton
extends ImageButton {
    private SlotData data = new SlotData();
    private float holdTime = 0.0f;
    private boolean holding = false;
    private boolean dirty = true;
    private final int index;
    private static ImageButton.ImageButtonStyle style;

    private static ImageButton.ImageButtonStyle selectStyle() {
        if (style == null) {
            style = new ImageButton.ImageButtonStyle(Styles.defaulti);
            SlotButton.style.checked = Tex.buttonSelect;
            SlotButton.style.checkedOver = Tex.buttonSelect;
        }
        return style;
    }

    public void setChecked(boolean bl) {
        super.setChecked(SchematicPlacer.isSelected(this));
    }

    public SlotButton(int n) {
        super(SlotButton.selectStyle());
        this.index = n;
        this.setSize(48.0f);
        this.updateVisual();
        this.addListener((EventListener)new ClickListener(){

            public boolean touchDown(InputEvent inputEvent, float f, float f2, int n, KeyCode keyCode) {
                if (keyCode == KeyCode.mouseLeft) {
                    SlotButton.this.holding = true;
                    SlotButton.this.holdTime = 0.0f;
                    return true;
                }
                return false;
            }

            public void touchUp(InputEvent inputEvent, float f, float f2, int n, KeyCode keyCode) {
                if (SlotButton.this.holding) {
                    SlotButton.this.holding = false;
                    SlotButton.this.dirty = true;
                    if (SlotButton.this.holdTime >= LunariansSettings.holdSeconds) {
                        SlotButton.this.clearSlot();
                    } else {
                        SlotButton.this.onClick();
                    }
                }
            }
        });
        this.update(new Runnable(){

            @Override
            public final void run() {
                SlotButton.this.lam_new$0();
            }
        });
    }

    public void lam_new$0() {
        if (this.holding) {
            this.holdTime += Time.delta / 60.0f;
            if (this.holdTime >= LunariansSettings.holdSeconds * 0.7f) {
                this.setColor(Color.red.cpy().lerp(Color.white, 1.0f - this.holdTime / LunariansSettings.holdSeconds));
            }
            return;
        }
        boolean bl = SchematicPlacer.isSelected(this);
        if (this.isChecked() != bl) {
            this.setChecked(bl);
        }
        if (this.dirty) {
            this.dirty = false;
            this.setColor(Color.white);
        }
    }

    public void onClick() {
        if (this.data.hasSchematic()) {
            this.placeSchematic();
        } else {
            new SchematicInputDialog(this).show();
        }
    }

    private void placeSchematic() {
        if (SchematicPlacer.isSelected(this)) {
            SchematicPlacer.deselect();
            return;
        }
        try {
            Schematic schematic = Schematics.readBase64((String)this.data.schematicBase64);
            if (schematic != null) {
                SchematicPlacer.select(this, schematic);
            }
        }
        catch (Exception exception) {
            Vars.ui.showErrorMessage("Schematic inv\u00e1lida:\n" + exception.getMessage());
            Log.err((Throwable)exception);
        }
    }

    public void setData(SlotData slotData) {
        this.setData(slotData, true);
    }

    public void setData(SlotData slotData, boolean bl) {
        this.data = slotData;
        this.updateVisual();
        if (bl && LunariansMod.panel != null) {
            LunariansMod.panel.saveSlot(this.index, this.data);
        }
    }

    public void clearSlot() {
        if (SchematicPlacer.isSelected(this)) {
            SchematicPlacer.deselect();
        }
        this.data.clear();
        this.updateVisual();
        if (LunariansMod.panel != null) {
            LunariansMod.panel.saveSlot(this.index, this.data);
        }
    }

    private static float iconSize() {
        return Math.max(20.0f, LunariansSettings.slotSize * 0.6f);
    }

    private void updateVisual() {
        this.clearChildren();
        if (this.data.hasSchematic()) {
            TextureRegion textureRegion = Icon.file.getRegion();
            if (this.data.iconName != null && !this.data.iconName.isEmpty()) {
                Block block = Vars.content.block(this.data.iconName);
                if (block != null && block.uiIcon != null && block.uiIcon.found()) {
                    textureRegion = block.uiIcon;
                } else {
                    TextureAtlas.AtlasRegion atlasRegion = Core.atlas.find(this.data.iconName);
                    if (atlasRegion != null && atlasRegion.found()) {
                        textureRegion = atlasRegion;
                    }
                }
            }
            this.image(textureRegion).size(SlotButton.iconSize());
            return;
        }
        this.image((Drawable)Icon.add).size(SlotButton.iconSize() * 0.85f).color(Color.lightGray);
    }

    public SlotData getData() {
        return this.data;
    }

    public int getIndex() {
        return this.index;
    }
}

