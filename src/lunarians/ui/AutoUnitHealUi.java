/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.func.Boolf
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

import arc.func.Boolf;
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
import lunarians.AutoUnitHeal;
import lunarians.LunariansSettings;
import lunarians.data.UnitHealSlot;
import lunarians.ui.BuilderDialog;
import lunarians.ui.UnitPickerDialog;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

public class AutoUnitHealUi {
    private final Table list = new Table();
    private static UnitHealSlot clipboard = null;

    public static BuilderDialog make() {
        return new BuilderDialog(AutoUnitHealUi.title(), new Cons<Table>(){

            public void get(Table table) {
                new AutoUnitHealUi(table);
            }
        }, new Runnable(){

            @Override
            public void run() {
                AutoUnitHeal.reset();
            }
        });
    }

    public AutoUnitHealUi(Table table) {
        table.defaults().growX();
        table.add((CharSequence)AutoUnitHealUi.hint()).wrap().growX().color(Color.lightGray).padBottom(4.0f).row();
        table.add((Element)this.list).growX().row();
        this.rebuild();
    }

    private void rebuild() {
        this.list.clearChildren();
        Seq<UnitHealSlot> seq = LunariansSettings.unitHealSlots;
        this.list.add((CharSequence)(seq.size + "/8")).color(Pal.accent).padBottom(4.0f).row();
        if (seq.isEmpty()) {
            this.list.add((CharSequence)AutoUnitHealUi.empty()).wrap().growX().row();
        } else {
            for (int i = 0; i < seq.size; ++i) {
                this.list.add((Element)this.card((UnitHealSlot)seq.get(i))).growX().padBottom(3.0f).row();
            }
        }
        if (seq.size < 8) {
            this.list.button((Drawable)Icon.add, Styles.defaulti, new Runnable(){

                @Override
                public void run() {
                    LunariansSettings.unitHealSlots.add((Object)new UnitHealSlot());
                    LunariansSettings.save();
                    AutoUnitHealUi.this.rebuild();
                }
            }).size(40.0f).padTop(2.0f).row();
        }
    }

    private Table card(final UnitHealSlot unitHealSlot) {
        Table table = new Table(Styles.black6);
        table.defaults().pad(4.0f).growX();
        Table table2 = new Table();
        UnitType unitType = unitHealSlot.unit.isEmpty() ? null : Vars.content.unit(unitHealSlot.unit);
        ImageButton imageButton = new ImageButton((Drawable)(unitType != null ? new TextureRegionDrawable(unitType.uiIcon) : Icon.add), Styles.defaulti);
        imageButton.clicked(new Runnable(){

            @Override
            public void run() {
                new UnitPickerDialog(new Cons<UnitType>(){

                    public void get(UnitType unitType) {
                        unitHealSlot.unit = unitType.name;
                        LunariansSettings.save();
                        AutoUnitHealUi.this.rebuild();
                    }
                }, new Boolf<UnitType>(){

                    public boolean get(UnitType unitType) {
                        return AutoUnitHeal.isHealerType(unitType);
                    }
                }).show();
            }
        });
        table2.add((Element)imageButton).size(40.0f).padRight(6.0f);
        table2.add((CharSequence)(unitType != null ? unitType.localizedName : AutoUnitHealUi.pick())).wrap().growX().left().fontScale(0.9f);
        TextButton textButton = (TextButton)table2.button(AutoUnitHealUi.copyLabel(), new Runnable(){

            @Override
            public void run() {
                clipboard = unitHealSlot.copy();
                Vars.ui.showInfoToast(AutoUnitHealUi.copied(), 2.0f);
                AutoUnitHealUi.this.rebuild();
            }
        }).width(64.0f).height(32.0f).padLeft(4.0f).get();
        textButton.getLabel().setFontScale(0.8f);
        table2.button((Drawable)Icon.trash, Styles.defaulti, new Runnable(){

            @Override
            public void run() {
                LunariansSettings.unitHealSlots.remove((Object)unitHealSlot);
                LunariansSettings.save();
                AutoUnitHealUi.this.rebuild();
            }
        }).size(36.0f);
        table.add((Element)table2).growX().row();
        Table table3 = new Table();
        TextButton textButton2 = (TextButton)table3.button(AutoUnitHealUi.pasteLabel(), new Runnable(){

            @Override
            public void run() {
                if (clipboard == null) {
                    Vars.ui.showInfoToast(AutoUnitHealUi.noClip(), 2.0f);
                    return;
                }
                if (LunariansSettings.unitHealSlots.size >= 8) {
                    Vars.ui.showInfoToast("Max slots", 2.0f);
                    return;
                }
                LunariansSettings.unitHealSlots.add((Object)clipboard.copy());
                LunariansSettings.save();
                Vars.ui.showInfoToast(AutoUnitHealUi.pasted(), 2.0f);
                AutoUnitHealUi.this.rebuild();
            }
        }).width(90.0f).height(36.0f).pad(2.0f).get();
        textButton2.getLabel().setFontScale(0.85f);
        textButton2.setDisabled(clipboard == null);
        this.list.add((Element)table3).left().padTop(2.0f).row();
        table3 = new Table();
        table3.add((CharSequence)AutoUnitHealUi.maxL()).left().padRight(4.0f).fontScale(0.85f);
        table3.button("-", new Runnable(){

            @Override
            public void run() {
                if (unitHealSlot.maxUnits > 0) {
                    --unitHealSlot.maxUnits;
                    LunariansSettings.save();
                    AutoUnitHealUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table3.label((Prov)new Prov<CharSequence>(){

            public CharSequence get() {
                return unitHealSlot.maxUnits <= 0 ? AutoUnitHealUi.all() : String.valueOf(unitHealSlot.maxUnits);
            }
        }).width(40.0f).center();
        table3.button("+", new Runnable(){

            @Override
            public void run() {
                if (unitHealSlot.maxUnits < 50) {
                    ++unitHealSlot.maxUnits;
                    LunariansSettings.save();
                    AutoUnitHealUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table.add((Element)table3).left().row();
        return table;
    }

    private static String title() {
        return "Auto unit heal";
    }

    private static String hint() {
        if ("pt".equals(LunariansSettings.language)) {
            return "Healers se aproximam de aliados com vida baixa. N\u00e3o usados por defence/attack.";
        }
        return "Healers approach low-HP allies. Not used by defence/attack.";
    }

    private static String empty() {
        return "pt".equals(LunariansSettings.language) ? "Nenhum slot. +" : "No slots. +";
    }

    private static String pick() {
        return "pt".equals(LunariansSettings.language) ? "Escolher healer" : "Pick healer";
    }

    private static String maxL() {
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

