package lunarians.ui;

import arc.func.Boolf;
import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.AutoUnitHeal;
import lunarians.LunariansSettings;
import lunarians.data.UnitHealSlot;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoUnitHealUi.class */
public class AutoUnitHealUi {
    private final Table list = new Table();
    private static UnitHealSlot clipboard = null;

    public static BuilderDialog make() {
        return new BuilderDialog(title(), new Cons<Table>() { // from class: lunarians.ui.AutoUnitHealUi.1
            public void get(Table table) {
                new AutoUnitHealUi(table);
            }
        }, new Runnable() { // from class: lunarians.ui.AutoUnitHealUi.2
            @Override // java.lang.Runnable
            public void run() {
                AutoUnitHeal.reset();
            }
        });
    }

    public AutoUnitHealUi(Table table) {
        table.defaults().growX();
        table.add(hint()).wrap().growX().color(Color.lightGray).padBottom(4.0f).row();
        table.add(this.list).growX().row();
        rebuild();
    }

    private void rebuild() {
        this.list.clearChildren();
        Seq<UnitHealSlot> seq = LunariansSettings.unitHealSlots;
        this.list.add(seq.size + "/8").color(Pal.accent).padBottom(4.0f).row();
        if (seq.isEmpty()) {
            this.list.add(empty()).wrap().growX().row();
        } else {
            for (int i = 0; i < seq.size; i++) {
                this.list.add(card((UnitHealSlot) seq.get(i))).growX().padBottom(3.0f).row();
            }
        }
        if (seq.size < 8) {
            this.list.button(Icon.add, Styles.defaulti, new Runnable() { // from class: lunarians.ui.AutoUnitHealUi.3
                @Override // java.lang.Runnable
                public void run() {
                    LunariansSettings.unitHealSlots.add(new UnitHealSlot());
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
        UnitType unit = unitHealSlot.unit.isEmpty() ? null : Vars.content.unit(unitHealSlot.unit);
        ImageButton imageButton = new ImageButton(unit != null ? new TextureRegionDrawable(unit.uiIcon) : Icon.add, Styles.defaulti);
        imageButton.clicked(new Runnable() { // from class: lunarians.ui.AutoUnitHealUi.4
            @Override // java.lang.Runnable
            public void run() {
                new UnitPickerDialog(new Cons<UnitType>() { // from class: lunarians.ui.AutoUnitHealUi.4.1
                    public void get(UnitType unitType) {
                        unitHealSlot.unit = unitType.name;
                        LunariansSettings.save();
                        AutoUnitHealUi.this.rebuild();
                    }
                }, new Boolf<UnitType>() { // from class: lunarians.ui.AutoUnitHealUi.4.2
                    public boolean get(UnitType unitType) {
                        return AutoUnitHeal.isHealerType(unitType);
                    }
                }).show();
            }
        });
        table2.add(imageButton).size(40.0f).padRight(6.0f);
        table2.add(unit != null ? unit.localizedName : pick()).wrap().growX().left().fontScale(0.9f);
        table2.button(copyLabel(), new Runnable() { // from class: lunarians.ui.AutoUnitHealUi.5
            @Override // java.lang.Runnable
            public void run() {
                AutoUnitHealUi.clipboard = unitHealSlot.copy();
                Vars.ui.showInfoToast(AutoUnitHealUi.copied(), 2.0f);
                AutoUnitHealUi.this.rebuild();
            }
        }).width(64.0f).height(32.0f).padLeft(4.0f).get().getLabel().setFontScale(0.8f);
        table2.button(Icon.trash, Styles.defaulti, new Runnable() { // from class: lunarians.ui.AutoUnitHealUi.6
            @Override // java.lang.Runnable
            public void run() {
                LunariansSettings.unitHealSlots.remove(unitHealSlot);
                LunariansSettings.save();
                AutoUnitHealUi.this.rebuild();
            }
        }).size(36.0f);
        table.add(table2).growX().row();
        Table table3 = new Table();
        TextButton textButton = table3.button(pasteLabel(), new Runnable() { // from class: lunarians.ui.AutoUnitHealUi.7
            @Override // java.lang.Runnable
            public void run() {
                if (AutoUnitHealUi.clipboard == null) {
                    Vars.ui.showInfoToast(AutoUnitHealUi.noClip(), 2.0f);
                    return;
                }
                if (LunariansSettings.unitHealSlots.size >= 8) {
                    Vars.ui.showInfoToast("Max slots", 2.0f);
                    return;
                }
                LunariansSettings.unitHealSlots.add(AutoUnitHealUi.clipboard.copy());
                LunariansSettings.save();
                Vars.ui.showInfoToast(AutoUnitHealUi.pasted(), 2.0f);
                AutoUnitHealUi.this.rebuild();
            }
        }).width(90.0f).height(36.0f).pad(2.0f).get();
        textButton.getLabel().setFontScale(0.85f);
        textButton.setDisabled(clipboard == null);
        this.list.add(table3).left().padTop(2.0f).row();
        Table table4 = new Table();
        table4.add(maxL()).left().padRight(4.0f).fontScale(0.85f);
        table4.button("-", new Runnable() { // from class: lunarians.ui.AutoUnitHealUi.8
            @Override // java.lang.Runnable
            public void run() {
                if (unitHealSlot.maxUnits > 0) {
                    unitHealSlot.maxUnits--;
                    LunariansSettings.save();
                    AutoUnitHealUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table4.label(new Prov<CharSequence>() { // from class: lunarians.ui.AutoUnitHealUi.9
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m58get() {
                return unitHealSlot.maxUnits <= 0 ? AutoUnitHealUi.all() : String.valueOf(unitHealSlot.maxUnits);
            }
        }).width(40.0f).center();
        table4.button("+", new Runnable() { // from class: lunarians.ui.AutoUnitHealUi.10
            @Override // java.lang.Runnable
            public void run() {
                if (unitHealSlot.maxUnits < 50) {
                    unitHealSlot.maxUnits++;
                    LunariansSettings.save();
                    AutoUnitHealUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table.add(table4).left().row();
        return table;
    }

    private static String title() {
        return "Auto unit heal";
    }

    private static String hint() {
        if ("pt".equals(LunariansSettings.language)) {
            return "Healers se aproximam de aliados com vida baixa. Não usados por defence/attack.";
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
        return "pt".equals(LunariansSettings.language) ? "Máx:" : "Max:";
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
