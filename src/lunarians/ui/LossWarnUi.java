package lunarians.ui;

import arc.Core;
import arc.func.Boolc;
import arc.func.Cons;
import arc.func.Floatc;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.ResourceLossWarn;
import lunarians.data.LossSlot;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.ui.Styles;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/LossWarnUi.class */
public final class LossWarnUi {
    private static final String[] PANEL_KEYS = {"loss_p1", "loss_p2", "loss_p3", "loss_p4"};

    private LossWarnUi() {
    }

    public static void build(Table table) {
        UiText.check(table, LunariansI18n.get("loss"), LunariansSettings.enableLossWarn, new Boolc() { // from class: lunarians.ui.LossWarnUi.1
            public void get(boolean z) {
                LunariansSettings.enableLossWarn = z;
                ResourceLossWarn.reset();
                LunariansSettings.save();
            }
        }).padTop(6.0f).row();
        table.add(LunariansI18n.get("loss_hint")).wrap().growX().color(Color.lightGray).padBottom(6.0f).row();
        Table table2 = new Table();
        Table table3 = new Table();
        buildTabs(table2, table3);
        table.add(table2).growX().row();
        table.add(table3).growX().row();
        rebuild(table3);
    }

    private static int tabCols() {
        return Core.graphics.getWidth() >= 640 ? 4 : 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void buildTabs(final Table table, final Table table2) {
        table.clear();
        table.defaults().height(42.0f).growX().pad(3.0f).uniformX();
        int i = 0;
        while (i < PANEL_KEYS.length) {
            final int i2 = i;
            TextButton textButton = new TextButton(LunariansI18n.get(PANEL_KEYS[i]), Styles.togglet);
            textButton.setProgrammaticChangeEvents(false);
            textButton.setChecked(LunariansSettings.lossPanel == i);
            textButton.getLabel().setWrap(true);
            textButton.getLabel().setAlignment(1);
            textButton.clicked(new Runnable() { // from class: lunarians.ui.LossWarnUi.2
                @Override // java.lang.Runnable
                public void run() {
                    if (LunariansSettings.lossPanel != i2) {
                        LunariansSettings.lossPanel = i2;
                        ResourceLossWarn.reset();
                        LunariansSettings.save();
                    }
                    LossWarnUi.buildTabs(table, table2);
                    LossWarnUi.rebuild(table2);
                }
            });
            table.add(textButton);
            if (i % tabCols() == tabCols() - 1) {
                table.row();
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void rebuild(final Table table) {
        table.clear();
        table.defaults().growX();
        final Seq<LossSlot> lossSlots = LunariansSettings.lossSlots(LunariansSettings.lossPanel);
        if (lossSlots.isEmpty()) {
            table.add(LunariansI18n.get("loss_empty")).wrap().growX().padBottom(6.0f).row();
        }
        for (int i = 0; i < lossSlots.size; i++) {
            table.add(card(table, lossSlots, (LossSlot) lossSlots.get(i))).growX().padBottom(5.0f).row();
        }
        if (lossSlots.size < 25) {
            table.button(Icon.add, Styles.defaulti, new Runnable() { // from class: lunarians.ui.LossWarnUi.3
                @Override // java.lang.Runnable
                public void run() {
                    lossSlots.add(new LossSlot());
                    LunariansSettings.save();
                    LossWarnUi.rebuild(table);
                }
            }).size(52.0f).padBottom(2.0f).row();
        }
        table.add(lossSlots.size + "/25").color(Pal.accent).padBottom(6.0f).row();
        if (LunariansSettings.lossPanel < 3) {
            table.button(LunariansI18n.get("loss_presets"), new Runnable() { // from class: lunarians.ui.LossWarnUi.4
                @Override // java.lang.Runnable
                public void run() {
                    LunariansSettings.applyLossPreset(LunariansSettings.lossPanel);
                    LunariansSettings.save();
                    ResourceLossWarn.reset();
                    LossWarnUi.rebuild(table);
                }
            }).height(42.0f).growX().padBottom(4.0f).row();
        }
    }

    private static Table card(final Table table, final Seq<LossSlot> seq, final LossSlot lossSlot) {
        Table table2 = new Table(Styles.black6);
        table2.margin(8.0f);
        table2.defaults().growX().padBottom(4.0f);
        Item item = lossSlot.item.isEmpty() ? null : Vars.content.item(lossSlot.item);
        Table table3 = new Table();
        ImageButton imageButton = new ImageButton(item != null ? new TextureRegionDrawable(item.uiIcon) : Icon.add, Styles.defaulti);
        imageButton.clicked(new Runnable() { // from class: lunarians.ui.LossWarnUi.5
            @Override // java.lang.Runnable
            public void run() {
                new ItemPickerDialog(new Cons<Item>() { // from class: lunarians.ui.LossWarnUi.5.1
                    public void get(Item item2) {
                        LossSlot.this.item = item2.name;
                        LunariansSettings.save();
                        LossWarnUi.rebuild(table);
                    }
                }).show();
            }
        });
        table3.add(imageButton).size(56.0f).padRight(8.0f);
        table3.add(item != null ? item.localizedName : LunariansI18n.get("loss_pick_item")).wrap().growX().left();
        table3.button(Icon.trash, Styles.defaulti, new Runnable() { // from class: lunarians.ui.LossWarnUi.6
            @Override // java.lang.Runnable
            public void run() {
                seq.remove(lossSlot);
                LunariansSettings.save();
                LossWarnUi.rebuild(table);
            }
        }).size(48.0f).padLeft(6.0f);
        table2.add(table3).growX().row();
        Table table4 = new Table();
        table4.add(LunariansI18n.get("loss_per_sec")).width(110.0f).left().wrap();
        table4.slider(0.5f, 300.0f, 0.5f, Math.min(300.0f, lossSlot.perSec), new Floatc() { // from class: lunarians.ui.LossWarnUi.7
            public void get(float f) {
                LossSlot.this.perSec = f;
            }
        }).growX().padLeft(4.0f);
        table4.label(new Prov<CharSequence>() { // from class: lunarians.ui.LossWarnUi.8
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m76get() {
                return LossWarnUi.fmt(LossSlot.this.perSec) + "/s";
            }
        }).width(64.0f).right().padLeft(6.0f);
        table2.add(table4).growX().row();
        Table table5 = new Table();
        table5.add(LunariansI18n.get("loss_delay")).width(110.0f).left().wrap();
        table5.slider(1.0f, 60.0f, 1.0f, Math.min(60.0f, lossSlot.delay), new Floatc() { // from class: lunarians.ui.LossWarnUi.9
            public void get(float f) {
                LossSlot.this.delay = f;
            }
        }).growX().padLeft(4.0f);
        table5.label(new Prov<CharSequence>() { // from class: lunarians.ui.LossWarnUi.10
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m75get() {
                return LossWarnUi.fmt(LossSlot.this.delay) + "s";
            }
        }).width(64.0f).right().padLeft(6.0f);
        table2.add(table5).growX().row();
        return table2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String fmt(float f) {
        return Math.abs(f - ((float) Math.round(f))) < 0.05f ? String.valueOf(Math.round(f)) : String.valueOf(Math.round(f * 10.0f) / 10.0f);
    }
}
