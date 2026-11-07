package lunarians.ui;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.input.KeyCode;
import arc.scene.event.ClickListener;
import arc.scene.event.InputEvent;
import arc.scene.ui.ImageButton;
import arc.util.Log;
import arc.util.Time;
import lunarians.LunariansMod;
import lunarians.LunariansSettings;
import lunarians.SchematicPlacer;
import lunarians.data.SlotData;
import mindustry.Vars;
import mindustry.game.Schematic;
import mindustry.game.Schematics;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.ui.Styles;
import mindustry.world.Block;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/SlotButton.class */
public class SlotButton extends ImageButton {
    private SlotData data;
    private float holdTime;
    private boolean holding;
    private boolean dirty;
    private final int index;
    private static ImageButton.ImageButtonStyle style;

    private static ImageButton.ImageButtonStyle selectStyle() {
        if (style == null) {
            style = new ImageButton.ImageButtonStyle(Styles.defaulti);
            style.checked = Tex.buttonSelect;
            style.checkedOver = Tex.buttonSelect;
        }
        return style;
    }

    public void setChecked(boolean z) {
        super.setChecked(SchematicPlacer.isSelected(this));
    }

    public SlotButton(int i) {
        super(selectStyle());
        this.data = new SlotData();
        this.holdTime = 0.0f;
        this.holding = false;
        this.dirty = true;
        this.index = i;
        setSize(48.0f);
        updateVisual();
        addListener(new ClickListener() { // from class: lunarians.ui.SlotButton.1
            public boolean touchDown(InputEvent inputEvent, float f, float f2, int i2, KeyCode keyCode) {
                if (keyCode == KeyCode.mouseLeft) {
                    SlotButton.this.holding = true;
                    SlotButton.this.holdTime = 0.0f;
                    return true;
                }
                return false;
            }

            public void touchUp(InputEvent inputEvent, float f, float f2, int i2, KeyCode keyCode) {
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
        update(new Runnable() { // from class: lunarians.ui.SlotButton.2
            @Override // java.lang.Runnable
            public final void run() {
                SlotButton.this.lam_new$0();
            }
        });
    }

    public void lam_new$0() {
        if (this.holding) {
            this.holdTime += Time.delta / 60.0f;
            if (this.holdTime >= LunariansSettings.holdSeconds * 0.7f) {
                setColor(Color.red.cpy().lerp(Color.white, 1.0f - (this.holdTime / LunariansSettings.holdSeconds)));
                return;
            }
            return;
        }
        boolean isSelected = SchematicPlacer.isSelected(this);
        if (isChecked() != isSelected) {
            setChecked(isSelected);
        }
        if (this.dirty) {
            this.dirty = false;
            setColor(Color.white);
        }
    }

    public void onClick() {
        if (this.data.hasSchematic()) {
            placeSchematic();
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
            Schematic readBase64 = Schematics.readBase64(this.data.schematicBase64);
            if (readBase64 != null) {
                SchematicPlacer.select(this, readBase64);
            }
        } catch (Exception e) {
            Vars.ui.showErrorMessage("Schematic inválida:\n" + e.getMessage());
            Log.err(e);
        }
    }

    public void setData(SlotData slotData) {
        setData(slotData, true);
    }

    public void setData(SlotData slotData, boolean z) {
        this.data = slotData;
        updateVisual();
        if (z && LunariansMod.panel != null) {
            LunariansMod.panel.saveSlot(this.index, this.data);
        }
    }

    public void clearSlot() {
        if (SchematicPlacer.isSelected(this)) {
            SchematicPlacer.deselect();
        }
        this.data.clear();
        updateVisual();
        if (LunariansMod.panel != null) {
            LunariansMod.panel.saveSlot(this.index, this.data);
        }
    }

    private static float iconSize() {
        return Math.max(20.0f, LunariansSettings.slotSize * 0.6f);
    }

    private void updateVisual() {
        clearChildren();
        if (this.data.hasSchematic()) {
            TextureRegion region = Icon.file.getRegion();
            if (this.data.iconName != null && !this.data.iconName.isEmpty()) {
                Block block = Vars.content.block(this.data.iconName);
                if (block != null && block.uiIcon != null && block.uiIcon.found()) {
                    region = block.uiIcon;
                } else {
                    TextureRegion find = Core.atlas.find(this.data.iconName);
                    if (find != null && find.found()) {
                        region = find;
                    }
                }
            }
            image(region).size(iconSize());
            return;
        }
        image(Icon.add).size(iconSize() * 0.85f).color(Color.lightGray);
    }

    public SlotData getData() {
        return this.data;
    }

    public int getIndex() {
        return this.index;
    }
}
