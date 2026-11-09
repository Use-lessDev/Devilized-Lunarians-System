/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.func.Prov
 *  arc.graphics.Color
 *  arc.input.KeyCode
 *  arc.math.Mathf
 *  arc.scene.Element
 *  arc.scene.event.EventListener
 *  arc.scene.event.InputEvent
 *  arc.scene.event.InputListener
 *  arc.scene.style.Drawable
 *  arc.scene.ui.ImageButton
 *  arc.scene.ui.ImageButton$ImageButtonStyle
 *  arc.scene.ui.Label
 *  arc.scene.ui.ScrollPane
 *  arc.scene.ui.layout.Table
 *  arc.struct.Seq
 *  mindustry.gen.Icon
 *  mindustry.graphics.Pal
 *  mindustry.ui.Styles
 */
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
import lunarians.ui.PanelLayout;
import lunarians.ui.PanelMinimizeAnimation;
import lunarians.ui.SlotButton;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;

public class SchematicPanel
extends Table {
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
        this.setBackground(Styles.black6);
        this.setSize(LunariansSettings.panelWidth, LunariansSettings.panelHeight);
        this.setColor(1.0f, 1.0f, 1.0f, LunariansSettings.opacity);
        this.header = new Table();
        this.header.setBackground(Styles.black3);
        ((ImageButton)this.header.button((Drawable)Icon.move, Styles.clearNonei, new Runnable(){

            @Override
            public final void run() {
                SchematicPanel.lam_new$0();
            }
        }).size(36.0f).pad(2.0f).get()).addListener((EventListener)new InputListener(){

            public boolean touchDown(InputEvent inputEvent, float f, float f2, int n, KeyCode keyCode) {
                SchematicPanel.this.dragging = true;
                SchematicPanel.this.lastTouchX = f;
                SchematicPanel.this.lastTouchY = f2;
                inputEvent.stop();
                return true;
            }

            public void touchDragged(InputEvent inputEvent, float f, float f2, int n) {
                if (SchematicPanel.this.dragging) {
                    SchematicPanel.this.setPosition(Mathf.clamp((float)(SchematicPanel.this.getX(12) + (f - SchematicPanel.this.lastTouchX)), (float)0.0f, (float)Math.max(0.0f, (float)Core.graphics.getWidth() - SchematicPanel.this.getWidth())), Mathf.clamp((float)(SchematicPanel.this.getY(12) + (f2 - SchematicPanel.this.lastTouchY)), (float)0.0f, (float)Math.max(0.0f, (float)Core.graphics.getHeight() - SchematicPanel.this.getHeight())));
                }
            }

            public void touchUp(InputEvent inputEvent, float f, float f2, int n, KeyCode keyCode) {
                if (SchematicPanel.this.dragging) {
                    SchematicPanel.this.dragging = false;
                    LunariansSettings.panelX = SchematicPanel.this.getX(12);
                    LunariansSettings.panelY = SchematicPanel.this.getY(12);
                    LunariansSettings.save();
                }
            }
        });
        this.header.add((CharSequence)LunariansI18n.get("title")).growX().left().padLeft(4.0f).name("title");
        this.header.button((Drawable)Icon.leftSmall, Styles.clearNonei, new Runnable(){

            @Override
            public final void run() {
                SchematicPanel.this.prevCategory();
            }
        }).size(28.0f).pad(1.0f);
        this.catLabel = (Label)this.header.label(new Prov(){

            public final Object get() {
                return SchematicPanel.lam_new$1();
            }
        }).width(70.0f).get();
        this.catLabel.setAlignment(1);
        this.catLabel.setEllipsis(true);
        this.header.button((Drawable)Icon.rightSmall, Styles.clearNonei, new Runnable(){

            @Override
            public final void run() {
                SchematicPanel.this.nextCategory();
            }
        }).size(28.0f).pad(1.0f);
        this.header.add((Element)this.buildArrowButton()).size(28.0f).pad(1.0f).name("dirBtn");
        this.header.button((Drawable)Icon.downOpen, Styles.clearNonei, new Runnable(){

            @Override
            public final void run() {
                SchematicPanel.this.toggleMinimize();
            }
        }).size(32.0f).pad(2.0f).name("minBtn");
        this.content = new Table();
        this.content.defaults().pad(3.0f);
        this.rebuildSlots();
        this.scroll = new ScrollPane((Element)this.content, Styles.smallPane);
        this.scroll.setScrollingDisabled(true, false);
        this.scroll.setFadeScrollBars(false);
        this.scroll.setForceScroll(false, true);
        this.scroll.setOverscroll(false, false);
        this.body = new Table();
        this.body.add((Element)this.scroll).grow();
        this.top().defaults().growX();
        this.add((Element)this.header).height(40.0f).growX();
        this.row();
        this.add((Element)this.body).grow().pad(6.0f).name("body");
        if (LunariansSettings.panelMinimized) {
            this.body.visible = false;
        }
    }

    static void lam_new$0() {
    }

    static CharSequence lam_new$1() {
        return LunariansSettings.categoryNames[LunariansSettings.currentCategory];
    }

    public void prevCategory() {
        this.saveCurrentSlots();
        LunariansSettings.currentCategory = (LunariansSettings.currentCategory - 1 + LunariansSettings.categoryCount) % LunariansSettings.categoryCount;
        LunariansSettings.save();
        this.rebuildSlots();
    }

    public void nextCategory() {
        this.saveCurrentSlots();
        LunariansSettings.currentCategory = (LunariansSettings.currentCategory + 1) % LunariansSettings.categoryCount;
        LunariansSettings.save();
        this.rebuildSlots();
    }

    private void saveCurrentSlots() {
        for (int i = 0; i < this.slots.size; ++i) {
            Core.settings.put(LunariansSettings.slotKey(i), (Object)((SlotButton)((Object)this.slots.get(i))).getData().serialize());
        }
        Core.settings.saveValues();
    }

    public void rebuildSlots() {
        SchematicPlacer.deselect();
        this.content.clear();
        this.slots.clear();
        int n = Mathf.clamp((int)LunariansSettings.slotCount, (int)1, (int)300);
        float f = LunariansSettings.slotSize;
        int n2 = PanelLayout.columns();
        if (n2 <= 0) {
            n2 = Mathf.clamp((int)((int)(Math.max(this.getWidth() - 24.0f, 120.0f) / (6.0f + f))), (int)2, (int)8);
        }
        int n3 = 0;
        while (n3 < n) {
            SlotButton slotButton = new SlotButton(n3);
            this.slots.add((Object)slotButton);
            this.content.add((Element)slotButton).size(f);
            if (++n3 % n2 != 0) continue;
            this.content.row();
        }
        this.loadSlots();
    }

    private void loadSlots() {
        for (int i = 0; i < this.slots.size; ++i) {
            String string = Core.settings.getString(LunariansSettings.slotKey(i), "");
            if (string.isEmpty() && LunariansSettings.currentCategory == 0) {
                string = Core.settings.getString("lunarians-slot-" + i, "");
            }
            ((SlotButton)((Object)this.slots.get(i))).setData(SlotData.deserialize(string), false);
        }
    }

    public void saveSlot(int n, SlotData slotData) {
        if (n < 0 || n >= this.slots.size) {
            return;
        }
        Core.settings.put(LunariansSettings.slotKey(n), (Object)slotData.serialize());
        Core.settings.saveValues();
    }

    public void applySettings() {
        float f = Mathf.clamp((float)PanelLayout.panelWidth(), (float)160.0f, (float)700.0f);
        float f2 = LunariansSettings.panelMinimized ? 40.0f : Mathf.clamp((float)PanelLayout.panelHeight(), (float)120.0f, (float)600.0f);
        this.setSize(f, f2);
        float f3 = Math.max(0.0f, (float)Core.graphics.getWidth() - f);
        float f4 = Math.max(0.0f, (float)Core.graphics.getHeight() - f2);
        float f5 = Mathf.clamp((float)LunariansSettings.panelX, (float)0.0f, (float)f3);
        float f6 = Mathf.clamp((float)LunariansSettings.panelY, (float)0.0f, (float)f4);
        LunariansSettings.panelX = f5;
        LunariansSettings.panelY = f6;
        this.setPosition(f5, f6);
        this.setColor(1.0f, 1.0f, 1.0f, LunariansSettings.opacity);
        this.body.visible = !LunariansSettings.panelMinimized;
        Element element = this.header.find("title");
        if (element != null) {
            element.visible = LunariansSettings.showTitle;
        }
        this.rebuildSlots();
        this.invalidateHierarchy();
        this.toFront();
    }

    private static Drawable arrowIcon(int n) {
        switch (n & 3) {
            case 1: {
                return Icon.upSmall;
            }
            case 2: {
                return Icon.leftSmall;
            }
            case 3: {
                return Icon.downSmall;
            }
        }
        return Icon.rightSmall;
    }

    private ImageButton buildArrowButton() {
        final ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle(Styles.clearNonei);
        imageButtonStyle.imageUp = SchematicPanel.arrowIcon(SchematicPlacer.rotation());
        imageButtonStyle.imageUpColor = Pal.accent;
        imageButtonStyle.imageDownColor = Color.white;
        ImageButton imageButton = new ImageButton(imageButtonStyle);
        imageButton.clicked(new Runnable(){

            @Override
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

