/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.func.Cons
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
 *  mindustry.type.UnitType
 *  mindustry.ui.Styles
 */
package lunarians.ui;

import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.AutoHeal;
import lunarians.LunariansSettings;
import lunarians.data.HealSlot;
import lunarians.ui.BuilderDialog;
import lunarians.ui.UnitPickerDialog;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

public class AutoHealUi {
    private final Table list = new Table();
    private static HealSlot clipboard = null;

    public static BuilderDialog make() {
        return new BuilderDialog(AutoHealUi.title(), new Cons<Table>(){

            public void get(Table table) {
                new AutoHealUi(table);
            }
        }, new Runnable(){

            @Override
            public void run() {
                AutoHeal.reset();
            }
        });
    }

    public AutoHealUi(Table table) {
        table.defaults().growX();
        table.add((CharSequence)AutoHealUi.hint()).wrap().growX().color(Color.lightGray).padBottom(4.0f).row();
        Table table2 = new Table();
        table2.add((CharSequence)AutoHealUi.pctLabel()).left().padRight(6.0f);
        table2.button("-", new Runnable(){

            @Override
            public void run() {
                LunariansSettings.autoHealPercent = Math.max(5.0f, LunariansSettings.autoHealPercent - 5.0f);
                LunariansSettings.save();
            }
        }).size(36.0f, 32.0f);
        table2.label((Prov)new Prov<CharSequence>(){

            public CharSequence get() {
                return (int)LunariansSettings.autoHealPercent + "%";
            }
        }).width(48.0f).center().pad(4.0f).color(Pal.accent);
        table2.button("+", new Runnable(){

            @Override
            public void run() {
                LunariansSettings.autoHealPercent = Math.min(90.0f, LunariansSettings.autoHealPercent + 5.0f);
                LunariansSettings.save();
            }
        }).size(36.0f, 32.0f);
        table.add((Element)table2).left().padBottom(6.0f).row();
        table.add((Element)this.list).growX().row();
        this.rebuild();
    }

    private void rebuild() {
        this.list.clearChildren();
        Seq<HealSlot> seq = LunariansSettings.healSlots;
        this.list.add((CharSequence)(seq.size + "/10")).color(Pal.accent).padBottom(4.0f).row();
        if (seq.isEmpty()) {
            this.list.add((CharSequence)AutoHealUi.emptyMsg()).wrap().growX().padBottom(4.0f).row();
        } else {
            for (int i = 0; i < seq.size; ++i) {
                this.list.add((Element)this.card((HealSlot)seq.get(i))).growX().padBottom(3.0f).row();
            }
        }
        if (seq.size < 10) {
            this.list.button((Drawable)Icon.add, Styles.defaulti, new Runnable(){

                @Override
                public void run() {
                    if (LunariansSettings.healSlots.size < 10) {
                        LunariansSettings.healSlots.add((Object)new HealSlot());
                        LunariansSettings.save();
                        AutoHealUi.this.rebuild();
                    }
                }
            }).size(40.0f).padTop(2.0f).row();
        }
    }

    private Table card(final HealSlot healSlot) {
        Table table = new Table(Styles.black6);
        table.defaults().growX();
        table.defaults().pad(4.0f);
        Table table2 = new Table();
        UnitType unitType = healSlot.unit.isEmpty() ? null : Vars.content.unit(healSlot.unit);
        ImageButton imageButton = new ImageButton((Drawable)(unitType != null ? new TextureRegionDrawable(unitType.uiIcon) : Icon.add), Styles.defaulti);
        imageButton.clicked(new Runnable(){

            @Override
            public void run() {
                new UnitPickerDialog(new Cons<UnitType>(){

                    public void get(UnitType unitType) {
                        healSlot.unit = unitType.name;
                        LunariansSettings.save();
                        AutoHealUi.this.rebuild();
                    }
                }, null).show();
            }
        });
        table2.add((Element)imageButton).size(40.0f).padRight(6.0f);
        table2.add((CharSequence)(unitType != null ? unitType.localizedName : AutoHealUi.pickUnit())).wrap().growX().left().fontScale(0.9f);
        TextButton textButton = (TextButton)table2.button(AutoHealUi.copyLabel(), new Runnable(){

            @Override
            public void run() {
                clipboard = healSlot.copy();
                Vars.ui.showInfoToast(AutoHealUi.copied(), 2.0f);
                AutoHealUi.this.rebuild();
            }
        }).width(64.0f).height(32.0f).padLeft(4.0f).get();
        textButton.getLabel().setFontScale(0.8f);
        table2.button((Drawable)Icon.trash, Styles.defaulti, new Runnable(){

            @Override
            public void run() {
                LunariansSettings.healSlots.remove((Object)healSlot);
                LunariansSettings.save();
                AutoHealUi.this.rebuild();
            }
        }).size(36.0f).padLeft(4.0f);
        table.add((Element)table2).growX().row();
        Table table3 = new Table();
        TextButton textButton2 = (TextButton)table3.button(AutoHealUi.pasteLabel(), new Runnable(){

            @Override
            public void run() {
                if (clipboard == null) {
                    Vars.ui.showInfoToast(AutoHealUi.noClip(), 2.0f);
                    return;
                }
                if (LunariansSettings.healSlots.size >= 10) {
                    Vars.ui.showInfoToast("Max slots", 2.0f);
                    return;
                }
                LunariansSettings.healSlots.add((Object)clipboard.copy());
                LunariansSettings.save();
                Vars.ui.showInfoToast(AutoHealUi.pasted(), 2.0f);
                AutoHealUi.this.rebuild();
            }
        }).width(90.0f).height(36.0f).pad(2.0f).get();
        textButton2.getLabel().setFontScale(0.85f);
        textButton2.setDisabled(clipboard == null);
        this.list.add((Element)table3).left().padTop(2.0f).row();
        table3 = new Table();
        table3.add((CharSequence)AutoHealUi.maxLabel()).left().padRight(4.0f).fontScale(0.85f);
        table3.button("-", new Runnable(){

            @Override
            public void run() {
                if (healSlot.maxUnits > 0) {
                    --healSlot.maxUnits;
                    LunariansSettings.save();
                    AutoHealUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table3.label((Prov)new Prov<CharSequence>(){

            public CharSequence get() {
                return healSlot.maxUnits <= 0 ? AutoHealUi.all() : String.valueOf(healSlot.maxUnits);
            }
        }).width(40.0f).center().pad(2.0f);
        table3.button("+", new Runnable(){

            @Override
            public void run() {
                if (healSlot.maxUnits < 50) {
                    ++healSlot.maxUnits;
                    LunariansSettings.save();
                    AutoHealUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table.add((Element)table3).left().padTop(2.0f).row();
        return table;
    }

    private static String title() {
        return "Auto heal";
    }

    private static String hint() {
        if ("pt".equals(LunariansSettings.language)) {
            return "Sem slots = todas as unidades em comando. Com slots = s\u00f3 esses tipos. Vai ao repair (borda do raio, pathfind).";
        }
        return "No slots = all command units. With slots = those types only. Paths to repair edge.";
    }

    private static String emptyMsg() {
        if ("pt".equals(LunariansSettings.language)) {
            return "Vazio = cura todas. Aperte + para filtrar por tipo.";
        }
        return "Empty = heal all. Press + to filter by type.";
    }

    private static String pctLabel() {
        return "pt".equals(LunariansSettings.language) ? "Vida <" : "HP <";
    }

    private static String pickUnit() {
        return "pt".equals(LunariansSettings.language) ? "Escolher" : "Choose";
    }

    private static String maxLabel() {
        return "pt".equals(LunariansSettings.language) ? "M\u00e1x:" : "Max:";
    }

    private static String all() {
        return "pt".equals(LunariansSettings.language) ? "Todas" : "All";
    }

    private static String copyLabel() {
        return "pt".equals(LunariansSettings.language) ? "Copiar" : "Copy";
    }

    private static String pasteLabel() {
        return "pt".equals(LunariansSettings.language) ? "Colar" : "Paste";
    }

    private static String copied() {
        return "pt".equals(LunariansSettings.language) ? "Slot copiado" : "Slot copied";
    }

    private static String pasted() {
        return "pt".equals(LunariansSettings.language) ? "Slot colado" : "Slot pasted";
    }

    private static String noClip() {
        return "pt".equals(LunariansSettings.language) ? "Nada copiado" : "Nothing copied";
    }
}

