/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Core
 *  arc.func.Boolc
 *  arc.func.Cons
 *  arc.func.Floatc
 *  arc.func.Prov
 *  arc.graphics.Color
 *  arc.scene.Element
 *  arc.scene.style.Drawable
 *  arc.scene.style.TextureRegionDrawable
 *  arc.scene.ui.ImageButton
 *  arc.scene.ui.TextButton
 *  arc.scene.ui.layout.Table
 *  arc.struct.Seq
 *  mindustry.Vars
 *  mindustry.gen.Icon
 *  mindustry.graphics.Pal
 *  mindustry.type.Item
 *  mindustry.ui.Styles
 */
package lunarians.ui;

import arc.Core;
import arc.func.Boolc;
import arc.func.Cons;
import arc.func.Floatc;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.ResourceLossWarn;
import lunarians.data.LossSlot;
import lunarians.ui.ItemPickerDialog;
import lunarians.ui.UiText;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.ui.Styles;

public final class LossWarnUi {
    private static final String[] PANEL_KEYS = new String[]{"loss_p1", "loss_p2", "loss_p3", "loss_p4"};

    private LossWarnUi() {
    }

    public static void build(Table table) {
        UiText.check(table, LunariansI18n.get("loss"), LunariansSettings.enableLossWarn, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableLossWarn = bl;
                ResourceLossWarn.reset();
                LunariansSettings.save();
            }
        }).padTop(6.0f).row();
        table.add((CharSequence)LunariansI18n.get("loss_hint")).wrap().growX().color(Color.lightGray).padBottom(6.0f).row();
        Table table2 = new Table();
        Table table3 = new Table();
        LossWarnUi.buildTabs(table2, table3);
        table.add((Element)table2).growX().row();
        table.add((Element)table3).growX().row();
        LossWarnUi.rebuild(table3);
    }

    private static int tabCols() {
        return Core.graphics.getWidth() >= 640 ? 4 : 2;
    }

    private static void buildTabs(final Table table, final Table table2) {
        table.clear();
        table.defaults().height(42.0f).growX().pad(3.0f).uniformX();
        for (int i = 0; i < PANEL_KEYS.length; ++i) {
            final int n = i;
            TextButton textButton = new TextButton(LunariansI18n.get(PANEL_KEYS[i]), Styles.togglet);
            textButton.setProgrammaticChangeEvents(false);
            textButton.setChecked(LunariansSettings.lossPanel == i);
            textButton.getLabel().setWrap(true);
            textButton.getLabel().setAlignment(1);
            textButton.clicked(new Runnable(){

                @Override
                public void run() {
                    if (LunariansSettings.lossPanel != n) {
                        LunariansSettings.lossPanel = n;
                        ResourceLossWarn.reset();
                        LunariansSettings.save();
                    }
                    LossWarnUi.buildTabs(table, table2);
                    LossWarnUi.rebuild(table2);
                }
            });
            table.add((Element)textButton);
            if (i % LossWarnUi.tabCols() != LossWarnUi.tabCols() - 1) continue;
            table.row();
        }
    }

    private static void rebuild(final Table table) {
        table.clear();
        table.defaults().growX();
        final Seq<LossSlot> seq = LunariansSettings.lossSlots(LunariansSettings.lossPanel);
        if (seq.isEmpty()) {
            table.add((CharSequence)LunariansI18n.get("loss_empty")).wrap().growX().padBottom(6.0f).row();
        }
        for (int i = 0; i < seq.size; ++i) {
            table.add((Element)LossWarnUi.card(table, seq, (LossSlot)seq.get(i))).growX().padBottom(5.0f).row();
        }
        if (seq.size < 25) {
            table.button((Drawable)Icon.add, Styles.defaulti, new Runnable(){

                @Override
                public void run() {
                    seq.add((Object)new LossSlot());
                    LunariansSettings.save();
                    LossWarnUi.rebuild(table);
                }
            }).size(52.0f).padBottom(2.0f).row();
        }
        table.add((CharSequence)(seq.size + "/" + 25)).color(Pal.accent).padBottom(6.0f).row();
        if (LunariansSettings.lossPanel < 3) {
            table.button(LunariansI18n.get("loss_presets"), new Runnable(){

                @Override
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
        TextureRegionDrawable textureRegionDrawable = item != null ? new TextureRegionDrawable(item.uiIcon) : Icon.add;
        ImageButton imageButton = new ImageButton((Drawable)textureRegionDrawable, Styles.defaulti);
        imageButton.clicked(new Runnable(){

            @Override
            public void run() {
                new ItemPickerDialog(new Cons<Item>(){

                    public void get(Item item) {
                        lossSlot.item = item.name;
                        LunariansSettings.save();
                        LossWarnUi.rebuild(table);
                    }
                }).show();
            }
        });
        table3.add((Element)imageButton).size(56.0f).padRight(8.0f);
        table3.add((CharSequence)(item != null ? item.localizedName : LunariansI18n.get("loss_pick_item"))).wrap().growX().left();
        table3.button((Drawable)Icon.trash, Styles.defaulti, new Runnable(){

            @Override
            public void run() {
                seq.remove((Object)lossSlot);
                LunariansSettings.save();
                LossWarnUi.rebuild(table);
            }
        }).size(48.0f).padLeft(6.0f);
        table2.add((Element)table3).growX().row();
        Table table4 = new Table();
        table4.add((CharSequence)LunariansI18n.get("loss_per_sec")).width(110.0f).left().wrap();
        table4.slider(0.5f, 300.0f, 0.5f, Math.min(300.0f, lossSlot.perSec), new Floatc(){

            public void get(float f) {
                lossSlot.perSec = f;
            }
        }).growX().padLeft(4.0f);
        table4.label((Prov)new Prov<CharSequence>(){

            public CharSequence get() {
                return LossWarnUi.fmt(lossSlot.perSec) + "/s";
            }
        }).width(64.0f).right().padLeft(6.0f);
        table2.add((Element)table4).growX().row();
        Table table5 = new Table();
        table5.add((CharSequence)LunariansI18n.get("loss_delay")).width(110.0f).left().wrap();
        table5.slider(1.0f, 60.0f, 1.0f, Math.min(60.0f, lossSlot.delay), new Floatc(){

            public void get(float f) {
                lossSlot.delay = f;
            }
        }).growX().padLeft(4.0f);
        table5.label((Prov)new Prov<CharSequence>(){

            public CharSequence get() {
                return LossWarnUi.fmt(lossSlot.delay) + "s";
            }
        }).width(64.0f).right().padLeft(6.0f);
        table2.add((Element)table5).growX().row();
        return table2;
    }

    private static String fmt(float f) {
        return Math.abs(f - (float)Math.round(f)) < 0.05f ? String.valueOf(Math.round(f)) : String.valueOf((float)Math.round(f * 10.0f) / 10.0f);
    }
}

