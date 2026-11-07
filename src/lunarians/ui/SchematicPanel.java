package lunarians.ui;

import arc.Core;
import arc.func.Prov;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.scene.Element;
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

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/SchematicPanel.class */
public class SchematicPanel extends Table {
    private final Table body;
    private Label catLabel;
    private final Table content;
    private final Table header;
    private float lastTouchX;
    private float lastTouchY;
    private final ScrollPane scroll;
    private final Seq<SlotButton> slots = new Seq<>();
    private boolean dragging = false;

    public void toggleMinimize() {
        PanelMinimizeAnimation.toggle(this);
    }

    public SchematicPanel() {
        setBackground(Styles.black6);
        setSize(LunariansSettings.panelWidth, LunariansSettings.panelHeight);
        setColor(1.0f, 1.0f, 1.0f, LunariansSettings.opacity);
        this.header = new Table();
        this.header.setBackground(Styles.black3);
        this.header.button(Icon.move, Styles.clearNonei, new Runnable() { // from class: lunarians.ui.SchematicPanel.2
            @Override // java.lang.Runnable
            public final void run() {
                SchematicPanel.lam_new$0();
            }
        }).size(36.0f).pad(2.0f).get().addListener(new InputListener() { // from class: lunarians.ui.SchematicPanel.1
            public boolean touchDown(InputEvent inputEvent, float f, float f2, int i, KeyCode keyCode) {
                SchematicPanel.this.dragging = true;
                SchematicPanel.this.lastTouchX = f;
                SchematicPanel.this.lastTouchY = f2;
                inputEvent.stop();
                return true;
            }

            public void touchDragged(InputEvent inputEvent, float f, float f2, int i) {
                if (SchematicPanel.this.dragging) {
                    SchematicPanel.this.setPosition(Mathf.clamp(SchematicPanel.this.getX(12) + (f - SchematicPanel.this.lastTouchX), 0.0f, Math.max(0.0f, Core.graphics.getWidth() - SchematicPanel.this.getWidth())), Mathf.clamp(SchematicPanel.this.getY(12) + (f2 - SchematicPanel.this.lastTouchY), 0.0f, Math.max(0.0f, Core.graphics.getHeight() - SchematicPanel.this.getHeight())));
                }
            }

            public void touchUp(InputEvent inputEvent, float f, float f2, int i, KeyCode keyCode) {
                if (SchematicPanel.this.dragging) {
                    SchematicPanel.this.dragging = false;
                    LunariansSettings.panelX = SchematicPanel.this.getX(12);
                    LunariansSettings.panelY = SchematicPanel.this.getY(12);
                    LunariansSettings.save();
                }
            }
        });
        this.header.add(LunariansI18n.get("title")).growX().left().padLeft(4.0f).name("title");
        this.header.button(Icon.leftSmall, Styles.clearNonei, new Runnable() { // from class: lunarians.ui.SchematicPanel.3
            @Override // java.lang.Runnable
            public final void run() {
                SchematicPanel.this.prevCategory();
            }
        }).size(28.0f).pad(1.0f);
        this.catLabel = this.header.label(new Prov() { // from class: lunarians.ui.SchematicPanel.4
            public final Object get() {
                return SchematicPanel.lam_new$1();
            }
        }).width(70.0f).get();
        this.catLabel.setAlignment(1);
        this.catLabel.setEllipsis(true);
        this.header.button(Icon.rightSmall, Styles.clearNonei, new Runnable() { // from class: lunarians.ui.SchematicPanel.5
            @Override // java.lang.Runnable
            public final void run() {
                SchematicPanel.this.nextCategory();
            }
        }).size(28.0f).pad(1.0f);
        this.header.add(buildArrowButton()).size(28.0f).pad(1.0f).name("dirBtn");
        this.header.button(Icon.downOpen, Styles.clearNonei, new Runnable() { // from class: lunarians.ui.SchematicPanel.6
            @Override // java.lang.Runnable
            public final void run() {
                SchematicPanel.this.toggleMinimize();
            }
        }).size(32.0f).pad(2.0f).name("minBtn");
        this.content = new Table();
        this.content.defaults().pad(3.0f);
        rebuildSlots();
        this.scroll = new ScrollPane(this.content, Styles.smallPane);
        this.scroll.setScrollingDisabled(true, false);
        this.scroll.setFadeScrollBars(false);
        this.scroll.setForceScroll(false, true);
        this.scroll.setOverscroll(false, false);
        this.body = new Table();
        this.body.add(this.scroll).grow();
        top().defaults().growX();
        add(this.header).height(40.0f).growX();
        row();
        add(this.body).grow().pad(6.0f).name("body");
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
        saveCurrentSlots();
        LunariansSettings.currentCategory = ((LunariansSettings.currentCategory - 1) + LunariansSettings.categoryCount) % LunariansSettings.categoryCount;
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
        for (int i = 0; i < this.slots.size; i++) {
            Core.settings.put(LunariansSettings.slotKey(i), ((SlotButton) this.slots.get(i)).getData().serialize());
        }
        Core.settings.saveValues();
    }

    public void rebuildSlots() {
        SchematicPlacer.deselect();
        this.content.clear();
        this.slots.clear();
        int clamp = Mathf.clamp(LunariansSettings.slotCount, 1, 300);
        float f = LunariansSettings.slotSize;
        int columns = PanelLayout.columns();
        if (columns <= 0) {
            columns = Mathf.clamp((int) (Math.max(getWidth() - 24.0f, 120.0f) / (6.0f + f)), 2, 8);
        }
        int i = 0;
        while (i < clamp) {
            SlotButton slotButton = new SlotButton(i);
            this.slots.add(slotButton);
            this.content.add(slotButton).size(f);
            i++;
            if (i % columns == 0) {
                this.content.row();
            }
        }
        loadSlots();
    }

    private void loadSlots() {
        for (int i = 0; i < this.slots.size; i++) {
            String string = Core.settings.getString(LunariansSettings.slotKey(i), "");
            if (string.isEmpty() && LunariansSettings.currentCategory == 0) {
                string = Core.settings.getString("lunarians-slot-" + i, "");
            }
            ((SlotButton) this.slots.get(i)).setData(SlotData.deserialize(string), false);
        }
    }

    public void saveSlot(int i, SlotData slotData) {
        if (i < 0 || i >= this.slots.size) {
            return;
        }
        Core.settings.put(LunariansSettings.slotKey(i), slotData.serialize());
        Core.settings.saveValues();
    }

    public void applySettings() {
        float clamp = Mathf.clamp(PanelLayout.panelWidth(), 160.0f, 700.0f);
        float clamp2 = LunariansSettings.panelMinimized ? 40.0f : Mathf.clamp(PanelLayout.panelHeight(), 120.0f, 600.0f);
        setSize(clamp, clamp2);
        float max = Math.max(0.0f, Core.graphics.getWidth() - clamp);
        float max2 = Math.max(0.0f, Core.graphics.getHeight() - clamp2);
        float clamp3 = Mathf.clamp(LunariansSettings.panelX, 0.0f, max);
        float clamp4 = Mathf.clamp(LunariansSettings.panelY, 0.0f, max2);
        LunariansSettings.panelX = clamp3;
        LunariansSettings.panelY = clamp4;
        setPosition(clamp3, clamp4);
        setColor(1.0f, 1.0f, 1.0f, LunariansSettings.opacity);
        this.body.visible = !LunariansSettings.panelMinimized;
        Element find = this.header.find("title");
        if (find != null) {
            find.visible = LunariansSettings.showTitle;
        }
        rebuildSlots();
        invalidateHierarchy();
        toFront();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Drawable arrowIcon(int i) {
        switch (i & 3) {
            case 1:
                return Icon.upSmall;
            case 2:
                return Icon.leftSmall;
            case 3:
                return Icon.downSmall;
            default:
                return Icon.rightSmall;
        }
    }

    private ImageButton buildArrowButton() {
        final ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle(Styles.clearNonei);
        imageButtonStyle.imageUp = arrowIcon(SchematicPlacer.rotation());
        imageButtonStyle.imageUpColor = Pal.accent;
        imageButtonStyle.imageDownColor = Color.white;
        ImageButton imageButton = new ImageButton(imageButtonStyle);
        imageButton.clicked(new Runnable() { // from class: lunarians.ui.SchematicPanel.7
            @Override // java.lang.Runnable
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
