package lunarians.ui;

import arc.Core;
import arc.func.Prov;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.scene.Element;
import arc.scene.event.EventListener;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.style.Drawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.Label;
import arc.scene.ui.ScrollPane;
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
  
  private final Table header;
  
  private float lastTouchX;
  
  private float lastTouchY;
  
  private final ScrollPane scroll;
  
  private final Seq<SlotButton> slots = new Seq();
  
  private boolean dragging = false;
  
  public void toggleMinimize() {
    PanelMinimizeAnimation.toggle(this);
  }
  
  public SchematicPanel() {
    setBackground(Styles.black6);
    setSize(LunariansSettings.panelWidth, LunariansSettings.panelHeight);
    setColor(1.0F, 1.0F, 1.0F, LunariansSettings.opacity);
    this.header = new Table();
    this.header.setBackground(Styles.black3);
    ((ImageButton)this.header.button((Drawable)Icon.move, Styles.clearNonei, new Runnable() {
          public final void run() {
            SchematicPanel.lam_new$0();
          }
        }).size(36.0F).pad(2.0F).get()).addListener((EventListener)new InputListener() {
          public boolean touchDown(InputEvent param1InputEvent, float param1Float1, float param1Float2, int param1Int, KeyCode param1KeyCode) {
            SchematicPanel.this.dragging = true;
            SchematicPanel.this.lastTouchX = param1Float1;
            SchematicPanel.this.lastTouchY = param1Float2;
            param1InputEvent.stop();
            return true;
          }
          
          public void touchDragged(InputEvent param1InputEvent, float param1Float1, float param1Float2, int param1Int) {
            if (SchematicPanel.this.dragging)
              SchematicPanel.this.setPosition(Mathf.clamp(SchematicPanel.this.getX(12) + param1Float1 - SchematicPanel.this.lastTouchX, 0.0F, Math.max(0.0F, Core.graphics.getWidth() - SchematicPanel.this.getWidth())), Mathf.clamp(SchematicPanel.this.getY(12) + param1Float2 - SchematicPanel.this.lastTouchY, 0.0F, Math.max(0.0F, Core.graphics.getHeight() - SchematicPanel.this.getHeight()))); 
          }
          
          public void touchUp(InputEvent param1InputEvent, float param1Float1, float param1Float2, int param1Int, KeyCode param1KeyCode) {
            if (SchematicPanel.this.dragging) {
              SchematicPanel.this.dragging = false;
              LunariansSettings.panelX = SchematicPanel.this.getX(12);
              LunariansSettings.panelY = SchematicPanel.this.getY(12);
              LunariansSettings.save();
            } 
          }
        });
    this.header.add(LunariansI18n.get("title")).growX().left().padLeft(4.0F).name("title");
    this.header.button((Drawable)Icon.leftSmall, Styles.clearNonei, new Runnable() {
          public final void run() {
            SchematicPanel.this.prevCategory();
          }
        }).size(28.0F).pad(1.0F);
    this



      
      .catLabel = (Label)this.header.label(new Prov() {
          public final Object get() {
            return SchematicPanel.lam_new$1();
          }
        }).width(70.0F).get();
    this.catLabel.setAlignment(1);
    this.catLabel.setEllipsis(true);
    this.header.button((Drawable)Icon.rightSmall, Styles.clearNonei, new Runnable() {
          public final void run() {
            SchematicPanel.this.nextCategory();
          }
        }).size(28.0F).pad(1.0F);
    this.header.add((Element)buildArrowButton()).size(28.0F).pad(1.0F).name("dirBtn");
    this.header.button((Drawable)Icon.downOpen, Styles.clearNonei, new Runnable() {
          public final void run() {
            SchematicPanel.this.toggleMinimize();
          }
        }).size(32.0F).pad(2.0F).name("minBtn");
    this.content = new Table();
    this.content.defaults().pad(3.0F);
    rebuildSlots();
    this.scroll = new ScrollPane((Element)this.content, Styles.smallPane);
    this.scroll.setScrollingDisabled(true, false);
    this.scroll.setFadeScrollBars(false);
    this.scroll.setForceScroll(false, true);
    this.scroll.setOverscroll(false, false);
    this.body = new Table();
    this.body.add((Element)this.scroll).grow();
    top().defaults().growX();
    add((Element)this.header).height(40.0F).growX();
    row();
    add((Element)this.body).grow().pad(6.0F).name("body");
    if (LunariansSettings.panelMinimized)
      this.body.visible = false; 
  }
  
  static void lam_new$0() {}
  
  static CharSequence lam_new$1() {
    return LunariansSettings.categoryNames[LunariansSettings.currentCategory];
  }
  
  public void prevCategory() {
    saveCurrentSlots();
    LunariansSettings.currentCategory = (LunariansSettings.currentCategory - 1 + LunariansSettings.categoryCount) % LunariansSettings.categoryCount;
    LunariansSettings.save();
    rebuildSlots();
  }
  
  public void nextCategory() {
    saveCurrentSlots();
    LunariansSettings.currentCategory = (LunariansSettings.currentCategory + 1) % LunariansSettings.categoryCount;
    LunariansSettings.save();
    rebuildSlots();
  }
  
  private void saveCurrentSlots() {
    for (byte b = 0; b < this.slots.size; b++)
      Core.settings.put(LunariansSettings.slotKey(b), ((SlotButton)this.slots.get(b)).getData().serialize()); 
    Core.settings.saveValues();
  }
  
  public void rebuildSlots() {
    SchematicPlacer.deselect();
    this.content.clear();
    this.slots.clear();
    int i = Mathf.clamp(LunariansSettings.slotCount, 1, 300);
    float f = LunariansSettings.slotSize;
    int j = PanelLayout.columns();
    if (j <= 0)
      j = Mathf.clamp((int)(Math.max(getWidth() - 24.0F, 120.0F) / (6.0F + f)), 2, 8); 
    byte b = 0;
    while (b < i) {
      SlotButton slotButton = new SlotButton(b);
      this.slots.add(slotButton);
      this.content.add((Element)slotButton).size(f);
      if (++b % j != 0)
        continue; 
      this.content.row();
    } 
    loadSlots();
  }
  
  private void loadSlots() {
    for (byte b = 0; b < this.slots.size; b++) {
      String str = Core.settings.getString(LunariansSettings.slotKey(b), "");
      if (str.isEmpty() && LunariansSettings.currentCategory == 0)
        str = Core.settings.getString("lunarians-slot-" + b, ""); 
      ((SlotButton)this.slots.get(b)).setData(SlotData.deserialize(str), false);
    } 
  }
  
  public void saveSlot(int paramInt, SlotData paramSlotData) {
    if (paramInt < 0 || paramInt >= this.slots.size)
      return; 
    Core.settings.put(LunariansSettings.slotKey(paramInt), paramSlotData.serialize());
    Core.settings.saveValues();
  }
  
  public void applySettings() {
    float f1 = Mathf.clamp(PanelLayout.panelWidth(), 160.0F, 700.0F);
    float f2 = LunariansSettings.panelMinimized ? 40.0F : Mathf.clamp(PanelLayout.panelHeight(), 120.0F, 600.0F);
    setSize(f1, f2);
    float f3 = Math.max(0.0F, Core.graphics.getWidth() - f1);
    float f4 = Math.max(0.0F, Core.graphics.getHeight() - f2);
    float f5 = Mathf.clamp(LunariansSettings.panelX, 0.0F, f3);
    float f6 = Mathf.clamp(LunariansSettings.panelY, 0.0F, f4);
    LunariansSettings.panelX = f5;
    LunariansSettings.panelY = f6;
    setPosition(f5, f6);
    setColor(1.0F, 1.0F, 1.0F, LunariansSettings.opacity);
    this.body.visible = !LunariansSettings.panelMinimized;
    Element element = this.header.find("title");
    if (element != null)
      element.visible = LunariansSettings.showTitle; 
    rebuildSlots();
    invalidateHierarchy();
    toFront();
  }
  
  private static Drawable arrowIcon(int paramInt) {
    switch (paramInt & 0x3) {
      case 1:
        return (Drawable)Icon.upSmall;
      case 2:
        return (Drawable)Icon.leftSmall;
      case 3:
        return (Drawable)Icon.downSmall;
    } 
    return (Drawable)Icon.rightSmall;
  }
  
  private ImageButton buildArrowButton() {
    final ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle(Styles.clearNonei);
    imageButtonStyle.imageUp = arrowIcon(SchematicPlacer.rotation());
    imageButtonStyle.imageUpColor = Pal.accent;
    imageButtonStyle.imageDownColor = Color.white;
    ImageButton imageButton = new ImageButton(imageButtonStyle);
    imageButton.clicked(new Runnable() {
          public void run() {
            SchematicPlacer.cycleRotation();
            imageButtonStyle.imageUp = SchematicPanel.arrowIcon(SchematicPlacer.rotation());
          }
        });
    return imageButton;
  }
  
  public Table getBody() {
    return this.body;
  }
  
  public Seq<SlotButton> getSlots() {
    return this.slots;
  }
}
