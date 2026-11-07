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
import mindustry.Vars;
import mindustry.game.Schematic;
import mindustry.game.Schematics;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.ui.Styles;
import mindustry.world.Block;

public class SlotButton extends ImageButton {
  private SlotData data = new SlotData();
  
  private float holdTime = 0.0F;
  
  private boolean holding = false;
  
  private boolean dirty = true;
  
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
  
  public void setChecked(boolean paramBoolean) {
    super.setChecked(SchematicPlacer.isSelected(this));
  }
  
  public SlotButton(int paramInt) {
    super(selectStyle());
    this.index = paramInt;
    setSize(48.0F);
    updateVisual();
    addListener((EventListener)new ClickListener() {
          public boolean touchDown(InputEvent param1InputEvent, float param1Float1, float param1Float2, int param1Int, KeyCode param1KeyCode) {
            if (param1KeyCode == KeyCode.mouseLeft) {
              SlotButton.this.holding = true;
              SlotButton.this.holdTime = 0.0F;
              return true;
            } 
            return false;
          }
          
          public void touchUp(InputEvent param1InputEvent, float param1Float1, float param1Float2, int param1Int, KeyCode param1KeyCode) {
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
    update(new Runnable() {
          public final void run() {
            SlotButton.this.lam_new$0();
          }
        });
  }
  
  public void lam_new$0() {
    if (this.holding) {
      this.holdTime += Time.delta / 60.0F;
      if (this.holdTime >= LunariansSettings.holdSeconds * 0.7F)
        setColor(Color.red.cpy().lerp(Color.white, 1.0F - this.holdTime / LunariansSettings.holdSeconds)); 
      return;
    } 
    boolean bool = SchematicPlacer.isSelected(this);
    if (isChecked() != bool)
      setChecked(bool); 
    if (this.dirty) {
      this.dirty = false;
      setColor(Color.white);
    } 
  }
  
  public void onClick() {
    if (this.data.hasSchematic()) {
      placeSchematic();
    } else {
      (new SchematicInputDialog(this)).show();
    } 
  }
  
  private void placeSchematic() {
    if (SchematicPlacer.isSelected(this)) {
      SchematicPlacer.deselect();
      return;
    } 
    try {
      Schematic schematic = Schematics.readBase64(this.data.schematicBase64);
      if (schematic != null)
        SchematicPlacer.select(this, schematic); 
    } catch (Exception exception) {
      Vars.ui.showErrorMessage("Schematic inválida:\n" + exception.getMessage());
      Log.err(exception);
    } 
  }
  
  public void setData(SlotData paramSlotData) {
    setData(paramSlotData, true);
  }
  
  public void setData(SlotData paramSlotData, boolean paramBoolean) {
    this.data = paramSlotData;
    updateVisual();
    if (paramBoolean && LunariansMod.panel != null)
      LunariansMod.panel.saveSlot(this.index, this.data); 
  }
  
  public void clearSlot() {
    if (SchematicPlacer.isSelected(this))
      SchematicPlacer.deselect(); 
    this.data.clear();
    updateVisual();
    if (LunariansMod.panel != null)
      LunariansMod.panel.saveSlot(this.index, this.data); 
  }
  
  private static float iconSize() {
    return Math.max(20.0F, LunariansSettings.slotSize * 0.6F);
  }
  
  private void updateVisual() {
    clearChildren();
    if (this.data.hasSchematic()) {
      TextureAtlas.AtlasRegion atlasRegion;
      TextureRegion textureRegion = Icon.file.getRegion();
      if (this.data.iconName != null && !this.data.iconName.isEmpty()) {
        Block block = Vars.content.block(this.data.iconName);
        if (block != null && block.uiIcon != null && block.uiIcon.found()) {
          textureRegion = block.uiIcon;
        } else {
          TextureAtlas.AtlasRegion atlasRegion1 = Core.atlas.find(this.data.iconName);
          if (atlasRegion1 != null && atlasRegion1.found())
            atlasRegion = atlasRegion1; 
        } 
      } 
      image((TextureRegion)atlasRegion).size(iconSize());
      return;
    } 
    image((Drawable)Icon.add).size(iconSize() * 0.85F).color(Color.lightGray);
  }
  
  public SlotData getData() {
    return this.data;
  }
  
  public int getIndex() {
    return this.index;
  }
}
