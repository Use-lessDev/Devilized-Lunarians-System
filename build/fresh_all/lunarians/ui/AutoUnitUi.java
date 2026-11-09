/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.func.Boolc
 *  arc.func.Cons
 *  arc.func.Prov
 *  arc.graphics.Color
 *  arc.scene.Element
 *  arc.scene.style.Drawable
 *  arc.scene.style.TextureRegionDrawable
 *  arc.scene.ui.Button
 *  arc.scene.ui.Button$ButtonStyle
 *  arc.scene.ui.ImageButton
 *  arc.scene.ui.TextButton
 *  arc.scene.ui.layout.Table
 *  arc.struct.Seq
 *  mindustry.Vars
 *  mindustry.ai.UnitCommand
 *  mindustry.gen.Icon
 *  mindustry.graphics.Pal
 *  mindustry.type.Item
 *  mindustry.type.UnitType
 *  mindustry.ui.Styles
 */
package lunarians.ui;

import arc.func.Boolc;
import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Button;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.AutoUnit;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.data.UnitSlot;
import lunarians.ui.UiText;
import lunarians.ui.UnitPickerDialog;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

public class AutoUnitUi {
    private final Table list = new Table();
    private static UnitSlot clipboard = null;

    public AutoUnitUi(Table table) {
        UiText.check(table, LunariansI18n.get("autounit"), LunariansSettings.enableAutoUnit, new Boolc(){

            public void get(boolean bl) {
                LunariansSettings.enableAutoUnit = bl;
                AutoUnit.reset();
            }
        }).left().row();
        table.add((CharSequence)LunariansI18n.get("autounit_hint")).wrap().growX().color(Color.lightGray).padBottom(6.0f).row();
        table.add((Element)this.list).growX().row();
        this.rebuild();
    }

    private void rebuild() {
        this.list.clear();
        this.list.defaults().growX();
        Seq<UnitSlot> seq = LunariansSettings.unitSlots;
        if (seq.isEmpty()) {
            this.list.add((CharSequence)LunariansI18n.get("au_empty")).wrap().growX().padBottom(6.0f).row();
        }
        for (int i = 0; i < seq.size; ++i) {
            this.list.add((Element)this.card((UnitSlot)seq.get(i))).growX().padBottom(6.0f).row();
        }
        if (seq.size < 10) {
            this.list.button((Drawable)Icon.add, Styles.defaulti, new Runnable(){

                @Override
                public void run() {
                    LunariansSettings.unitSlots.add((Object)new UnitSlot());
                    LunariansSettings.save();
                    AutoUnitUi.this.rebuild();
                }
            }).size(48.0f).padTop(4.0f).row();
        }
        this.list.add((CharSequence)(seq.size + "/10")).color(Pal.accent).row();
        Table table = new Table();
        TextButton textButton = (TextButton)table.button(AutoUnitUi.pasteLabel(), new Runnable(){

            @Override
            public void run() {
                if (clipboard == null) {
                    Vars.ui.showInfoToast(AutoUnitUi.noClip(), 2.0f);
                    return;
                }
                if (LunariansSettings.unitSlots.size >= 10) {
                    Vars.ui.showInfoToast("Max slots", 2.0f);
                    return;
                }
                LunariansSettings.unitSlots.add((Object)clipboard.copy());
                LunariansSettings.save();
                Vars.ui.showInfoToast(AutoUnitUi.pasted(), 2.0f);
                AutoUnitUi.this.rebuild();
            }
        }).width(90.0f).height(36.0f).pad(2.0f).get();
        textButton.getLabel().setFontScale(0.85f);
        textButton.setDisabled(clipboard == null);
        this.list.add((Element)table).left().padTop(2.0f).row();
    }

    private Table card(final UnitSlot unitSlot) {
        Seq seq;
        int n;
        Table table;
        Table table2 = new Table(Styles.black6);
        table2.defaults().growX();
        Table table3 = new Table();
        UnitType unitType = unitSlot.unit.isEmpty() ? null : Vars.content.unit(unitSlot.unit);
        ImageButton imageButton = new ImageButton((Drawable)(unitType != null ? new TextureRegionDrawable(unitType.uiIcon) : Icon.add), Styles.defaulti);
        imageButton.clicked(new Runnable(){

            @Override
            public void run() {
                new UnitPickerDialog(new Cons<UnitType>(){

                    public void get(UnitType unitType) {
                        if (!unitType.name.equals(unitSlot.unit)) {
                            unitSlot.unit = unitType.name;
                            unitSlot.command = "";
                            unitSlot.items.clear();
                        }
                        LunariansSettings.save();
                        AutoUnitUi.this.rebuild();
                    }
                }).show();
            }
        });
        table3.add((Element)imageButton).size(56.0f).padRight(8.0f);
        table3.add((CharSequence)(unitType != null ? unitType.localizedName : LunariansI18n.get("au_pick_unit"))).wrap().growX().left();
        TextButton textButton = (TextButton)table3.button(AutoUnitUi.copyLabel(), new Runnable(){

            @Override
            public void run() {
                clipboard = unitSlot.copy();
                Vars.ui.showInfoToast(AutoUnitUi.copied(), 2.0f);
                AutoUnitUi.this.rebuild();
            }
        }).width(64.0f).height(32.0f).padLeft(4.0f).get();
        textButton.getLabel().setFontScale(0.8f);
        table3.button((Drawable)Icon.trash, Styles.defaulti, new Runnable(){

            @Override
            public void run() {
                LunariansSettings.unitSlots.remove((Object)unitSlot);
                LunariansSettings.save();
                AutoUnitUi.this.rebuild();
            }
        }).size(48.0f);
        table2.add((Element)table3).growX().row();
        Table table4 = new Table();
        table4.add((CharSequence)("pt".equals(LunariansSettings.language) ? "M\u00e1x:" : "Max:")).left().padRight(4.0f).fontScale(0.85f);
        table4.button("-", new Runnable(){

            @Override
            public void run() {
                if (unitSlot.maxUnits > 0) {
                    --unitSlot.maxUnits;
                    LunariansSettings.save();
                    AutoUnitUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table4.label((Prov)new Prov<CharSequence>(){

            public CharSequence get() {
                return unitSlot.maxUnits <= 0 ? ("pt".equals(LunariansSettings.language) ? "Todas" : "All") : String.valueOf(unitSlot.maxUnits);
            }
        }).width(40.0f).center();
        table4.button("+", new Runnable(){

            @Override
            public void run() {
                if (unitSlot.maxUnits < 50) {
                    ++unitSlot.maxUnits;
                    LunariansSettings.save();
                    AutoUnitUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table2.add((Element)table4).left().row();
        if (unitType != null && unitType.commands != null) {
            table2.add((CharSequence)LunariansI18n.get("au_command")).color(Pal.accent).left().row();
            table = new Table();
            for (n = 0; n < unitType.commands.size; ++n) {
                seq = (UnitCommand)unitType.commands.get(n);
                TextButton textButton2 = (TextButton)table.button(seq.localized(), new Runnable(){
                    final /* synthetic */ UnitCommand val$c;
                    {
                        this.val$c = unitCommand;
                    }

                    @Override
                    public void run() {
                        unitSlot.command = this.val$c.name;
                        if (this.val$c != UnitCommand.mineCommand) {
                            unitSlot.items.clear();
                        }
                        LunariansSettings.save();
                        AutoUnitUi.this.rebuild();
                    }
                }).height(36.0f).pad(2.0f).get();
                textButton2.setChecked(seq.name.equals(unitSlot.command));
            }
            table2.add((Element)table).growX().left().row();
        }
        if (unitType != null && UnitCommand.mineCommand.name.equals(unitSlot.command)) {
            table2.add((CharSequence)LunariansI18n.get("au_items")).color(Pal.accent).left().row();
            table = new Table();
            n = 0;
            seq = Vars.content.items();
            for (int i = 0; i < seq.size; ++i) {
                final Item item = (Item)seq.get(i);
                if (item == null || unitType.mineTier >= 0 && item.hardness > unitType.mineTier) continue;
                final Button button = new Button((Button.ButtonStyle)Styles.togglet);
                button.setProgrammaticChangeEvents(false);
                button.image((Drawable)new TextureRegionDrawable(item.uiIcon)).size(28.0f).row();
                button.add((CharSequence)item.localizedName).fontScale(0.7f).width(70.0f);
                button.setChecked(unitSlot.items.contains((Object)item.name));
                button.clicked(new Runnable(){

                    @Override
                    public void run() {
                        if (button.isChecked()) {
                            if (!unitSlot.items.contains((Object)item.name)) {
                                unitSlot.items.add((Object)item.name);
                            }
                        } else {
                            unitSlot.items.remove((Object)item.name);
                        }
                        LunariansSettings.save();
                    }
                });
                table.add((Element)button).size(80.0f).pad(2.0f);
                if (++n < 4) continue;
                table.row();
                n = 0;
            }
            table2.add((Element)table).left().row();
        }
        return table2;
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

