package lunarians.ui;

import arc.func.Boolc;
import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
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
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoUnitUi.class */
public class AutoUnitUi {
    private final Table list = new Table();
    private static UnitSlot clipboard = null;

    public AutoUnitUi(Table table) {
        UiText.check(table, LunariansI18n.get("autounit"), LunariansSettings.enableAutoUnit, new Boolc() { // from class: lunarians.ui.AutoUnitUi.1
            public void get(boolean z) {
                LunariansSettings.enableAutoUnit = z;
                AutoUnit.reset();
            }
        }).left().row();
        table.add(LunariansI18n.get("autounit_hint")).wrap().growX().color(Color.lightGray).padBottom(6.0f).row();
        table.add(this.list).growX().row();
        rebuild();
    }

    private void rebuild() {
        this.list.clear();
        this.list.defaults().growX();
        Seq<UnitSlot> seq = LunariansSettings.unitSlots;
        if (seq.isEmpty()) {
            this.list.add(LunariansI18n.get("au_empty")).wrap().growX().padBottom(6.0f).row();
        }
        for (int i = 0; i < seq.size; i++) {
            this.list.add(card((UnitSlot) seq.get(i))).growX().padBottom(6.0f).row();
        }
        if (seq.size < 10) {
            this.list.button(Icon.add, Styles.defaulti, new Runnable() { // from class: lunarians.ui.AutoUnitUi.2
                @Override // java.lang.Runnable
                public void run() {
                    LunariansSettings.unitSlots.add(new UnitSlot());
                    LunariansSettings.save();
                    AutoUnitUi.this.rebuild();
                }
            }).size(48.0f).padTop(4.0f).row();
        }
        this.list.add(seq.size + "/10").color(Pal.accent).row();
        Table table = new Table();
        TextButton textButton = table.button(pasteLabel(), new Runnable() { // from class: lunarians.ui.AutoUnitUi.3

            /* JADX WARN: Classes with same name are omitted:
              classes.dex
             */
            /* renamed from: lunarians.ui.AutoUnitUi$3$1, reason: invalid class name */
            /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoUnitUi$3$1.class */
            class AnonymousClass1 implements Cons<UnitType> {
                AnonymousClass1() {
                }

                public void get(UnitType unitType) {
                    if (!unitType.name.equals(AnonymousClass3.this.val$slot.unit)) {
                        AnonymousClass3.this.val$slot.unit = unitType.name;
                        AnonymousClass3.this.val$slot.command = "";
                        AnonymousClass3.this.val$slot.items.clear();
                    }
                    LunariansSettings.save();
                    AutoUnitUi.this.rebuild();
                }
            }

            @Override // java.lang.Runnable
            public void run() {
                if (AutoUnitUi.clipboard == null) {
                    Vars.ui.showInfoToast(AutoUnitUi.noClip(), 2.0f);
                    return;
                }
                if (LunariansSettings.unitSlots.size >= 10) {
                    Vars.ui.showInfoToast("Max slots", 2.0f);
                    return;
                }
                LunariansSettings.unitSlots.add(AutoUnitUi.clipboard.copy());
                LunariansSettings.save();
                Vars.ui.showInfoToast(AutoUnitUi.pasted(), 2.0f);
                AutoUnitUi.this.rebuild();
            }
        }).width(90.0f).height(36.0f).pad(2.0f).get();
        textButton.getLabel().setFontScale(0.85f);
        textButton.setDisabled(clipboard == null);
        this.list.add(table).left().padTop(2.0f).row();
    }

    private Table card(final UnitSlot unitSlot) {
        Table table = new Table(Styles.black6);
        table.defaults().growX();
        Table table2 = new Table();
        UnitType unit = unitSlot.unit.isEmpty() ? null : Vars.content.unit(unitSlot.unit);
        ImageButton imageButton = new ImageButton(unit != null ? new TextureRegionDrawable(unit.uiIcon) : Icon.add, Styles.defaulti);
        imageButton.clicked(new Runnable() { // from class: lunarians.ui.AutoUnitUi.4
            @Override // java.lang.Runnable
            public void run() {
                new UnitPickerDialog(new Cons<UnitType>() { // from class: lunarians.ui.AutoUnitUi.4.1
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
        table2.add(imageButton).size(56.0f).padRight(8.0f);
        table2.add(unit != null ? unit.localizedName : LunariansI18n.get("au_pick_unit")).wrap().growX().left();
        table2.button(copyLabel(), new Runnable() { // from class: lunarians.ui.AutoUnitUi.5
            @Override // java.lang.Runnable
            public void run() {
                AutoUnitUi.clipboard = unitSlot.copy();
                Vars.ui.showInfoToast(AutoUnitUi.copied(), 2.0f);
                AutoUnitUi.this.rebuild();
            }
        }).width(64.0f).height(32.0f).padLeft(4.0f).get().getLabel().setFontScale(0.8f);
        table2.button(Icon.trash, Styles.defaulti, new Runnable() { // from class: lunarians.ui.AutoUnitUi.6
            @Override // java.lang.Runnable
            public void run() {
                LunariansSettings.unitSlots.remove(unitSlot);
                LunariansSettings.save();
                AutoUnitUi.this.rebuild();
            }
        }).size(48.0f);
        table.add(table2).growX().row();
        Table table3 = new Table();
        table3.add("pt".equals(LunariansSettings.language) ? "Máx:" : "Max:").left().padRight(4.0f).fontScale(0.85f);
        table3.button("-", new Runnable() { // from class: lunarians.ui.AutoUnitUi.7
            @Override // java.lang.Runnable
            public void run() {
                if (unitSlot.maxUnits > 0) {
                    unitSlot.maxUnits--;
                    LunariansSettings.save();
                    AutoUnitUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table3.label(new Prov<CharSequence>() { // from class: lunarians.ui.AutoUnitUi.8
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m60get() {
                return unitSlot.maxUnits <= 0 ? "pt".equals(LunariansSettings.language) ? "Todas" : "All" : String.valueOf(unitSlot.maxUnits);
            }
        }).width(40.0f).center();
        table3.button("+", new Runnable() { // from class: lunarians.ui.AutoUnitUi.9
            @Override // java.lang.Runnable
            public void run() {
                if (unitSlot.maxUnits < 50) {
                    unitSlot.maxUnits++;
                    LunariansSettings.save();
                    AutoUnitUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table.add(table3).left().row();
        if (unit != null && unit.commands != null) {
            table.add(LunariansI18n.get("au_command")).color(Pal.accent).left().row();
            Table table4 = new Table();
            for (int i = 0; i < unit.commands.size; i++) {
                final UnitCommand unitCommand = (UnitCommand) unit.commands.get(i);
                table4.button(unitCommand.localized(), new Runnable() { // from class: lunarians.ui.AutoUnitUi.10
                    @Override // java.lang.Runnable
                    public void run() {
                        unitSlot.command = unitCommand.name;
                        if (unitCommand != UnitCommand.mineCommand) {
                            unitSlot.items.clear();
                        }
                        LunariansSettings.save();
                        AutoUnitUi.this.rebuild();
                    }
                }).height(36.0f).pad(2.0f).get().setChecked(unitCommand.name.equals(unitSlot.command));
            }
            table.add(table4).growX().left().row();
        }
        if (unit != null && UnitCommand.mineCommand.name.equals(unitSlot.command)) {
            table.add(LunariansI18n.get("au_items")).color(Pal.accent).left().row();
            Table table5 = new Table();
            int i2 = 0;
            Seq items = Vars.content.items();
            for (int i3 = 0; i3 < items.size; i3++) {
                final Item item = (Item) items.get(i3);
                if (item != null && (unit.mineTier < 0 || item.hardness <= unit.mineTier)) {
                    final Button button = new Button(Styles.togglet);
                    button.setProgrammaticChangeEvents(false);
                    button.image(new TextureRegionDrawable(item.uiIcon)).size(28.0f).row();
                    button.add(item.localizedName).fontScale(0.7f).width(70.0f);
                    button.setChecked(unitSlot.items.contains(item.name));
                    button.clicked(new Runnable() { // from class: lunarians.ui.AutoUnitUi.11
                        @Override // java.lang.Runnable
                        public void run() {
                            if (!button.isChecked()) {
                                unitSlot.items.remove(item.name);
                            } else if (!unitSlot.items.contains(item.name)) {
                                unitSlot.items.add(item.name);
                            }
                            LunariansSettings.save();
                        }
                    });
                    table5.add(button).size(80.0f).pad(2.0f);
                    i2++;
                    if (i2 >= 4) {
                        table5.row();
                        i2 = 0;
                    }
                }
            }
            table.add(table5).left().row();
        }
        return table;
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
