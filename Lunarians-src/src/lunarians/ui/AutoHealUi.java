package lunarians.ui;

import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.AutoHeal;
import lunarians.LunariansSettings;
import lunarians.data.HealSlot;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoHealUi.class */
public class AutoHealUi {
    private final Table list = new Table();
    private static HealSlot clipboard = null;

    public static BuilderDialog make() {
        return new BuilderDialog(title(), new Cons<Table>() { // from class: lunarians.ui.AutoHealUi.1
            public void get(Table table) {
                new AutoHealUi(table);
            }
        }, new Runnable() { // from class: lunarians.ui.AutoHealUi.2
            @Override // java.lang.Runnable
            public void run() {
                AutoHeal.reset();
            }
        });
    }

    public AutoHealUi(Table table) {
        table.defaults().growX();
        table.add(hint()).wrap().growX().color(Color.lightGray).padBottom(4.0f).row();
        Table table2 = new Table();
        table2.add(pctLabel()).left().padRight(6.0f);
        table2.button("-", new Runnable() { // from class: lunarians.ui.AutoHealUi.3
            @Override // java.lang.Runnable
            public void run() {
                LunariansSettings.autoHealPercent = Math.max(5.0f, LunariansSettings.autoHealPercent - 5.0f);
                LunariansSettings.save();
            }
        }).size(36.0f, 32.0f);
        table2.label(new Prov<CharSequence>() { // from class: lunarians.ui.AutoHealUi.4
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m53get() {
                return ((int) LunariansSettings.autoHealPercent) + "%";
            }
        }).width(48.0f).center().pad(4.0f).color(Pal.accent);
        table2.button("+", new Runnable() { // from class: lunarians.ui.AutoHealUi.5
            @Override // java.lang.Runnable
            public void run() {
                LunariansSettings.autoHealPercent = Math.min(90.0f, LunariansSettings.autoHealPercent + 5.0f);
                LunariansSettings.save();
            }
        }).size(36.0f, 32.0f);
        table.add(table2).left().padBottom(6.0f).row();
        table.add(this.list).growX().row();
        rebuild();
    }

    private void rebuild() {
        this.list.clearChildren();
        Seq<HealSlot> seq = LunariansSettings.healSlots;
        this.list.add(seq.size + "/10").color(Pal.accent).padBottom(4.0f).row();
        if (seq.isEmpty()) {
            this.list.add(emptyMsg()).wrap().growX().padBottom(4.0f).row();
        } else {
            for (int i = 0; i < seq.size; i++) {
                this.list.add(card((HealSlot) seq.get(i))).growX().padBottom(3.0f).row();
            }
        }
        if (seq.size < 10) {
            this.list.button(Icon.add, Styles.defaulti, new Runnable() { // from class: lunarians.ui.AutoHealUi.6
                @Override // java.lang.Runnable
                public void run() {
                    if (LunariansSettings.healSlots.size < 10) {
                        LunariansSettings.healSlots.add(new HealSlot());
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
        UnitType unit = healSlot.unit.isEmpty() ? null : Vars.content.unit(healSlot.unit);
        ImageButton imageButton = new ImageButton(unit != null ? new TextureRegionDrawable(unit.uiIcon) : Icon.add, Styles.defaulti);
        imageButton.clicked(new Runnable() { // from class: lunarians.ui.AutoHealUi.7
            @Override // java.lang.Runnable
            public void run() {
                new UnitPickerDialog(new Cons<UnitType>() { // from class: lunarians.ui.AutoHealUi.7.1
                    public void get(UnitType unitType) {
                        healSlot.unit = unitType.name;
                        LunariansSettings.save();
                        AutoHealUi.this.rebuild();
                    }
                }, null).show();
            }
        });
        table2.add(imageButton).size(40.0f).padRight(6.0f);
        table2.add(unit != null ? unit.localizedName : pickUnit()).wrap().growX().left().fontScale(0.9f);
        table2.button(copyLabel(), new Runnable() { // from class: lunarians.ui.AutoHealUi.8
            @Override // java.lang.Runnable
            public void run() {
                AutoHealUi.clipboard = healSlot.copy();
                Vars.ui.showInfoToast(AutoHealUi.copied(), 2.0f);
                AutoHealUi.this.rebuild();
            }
        }).width(64.0f).height(32.0f).padLeft(4.0f).get().getLabel().setFontScale(0.8f);
        table2.button(Icon.trash, Styles.defaulti, new Runnable() { // from class: lunarians.ui.AutoHealUi.9
            @Override // java.lang.Runnable
            public void run() {
                LunariansSettings.healSlots.remove(healSlot);
                LunariansSettings.save();
                AutoHealUi.this.rebuild();
            }
        }).size(36.0f).padLeft(4.0f);
        table.add(table2).growX().row();
        Table table3 = new Table();
        TextButton textButton = table3.button(pasteLabel(), new Runnable() { // from class: lunarians.ui.AutoHealUi.10
            @Override // java.lang.Runnable
            public void run() {
                if (AutoHealUi.clipboard == null) {
                    Vars.ui.showInfoToast(AutoHealUi.noClip(), 2.0f);
                    return;
                }
                if (LunariansSettings.healSlots.size >= 10) {
                    Vars.ui.showInfoToast("Max slots", 2.0f);
                    return;
                }
                LunariansSettings.healSlots.add(AutoHealUi.clipboard.copy());
                LunariansSettings.save();
                Vars.ui.showInfoToast(AutoHealUi.pasted(), 2.0f);
                AutoHealUi.this.rebuild();
            }
        }).width(90.0f).height(36.0f).pad(2.0f).get();
        textButton.getLabel().setFontScale(0.85f);
        textButton.setDisabled(clipboard == null);
        this.list.add(table3).left().padTop(2.0f).row();
        Table table4 = new Table();
        table4.add(maxLabel()).left().padRight(4.0f).fontScale(0.85f);
        table4.button("-", new Runnable() { // from class: lunarians.ui.AutoHealUi.11
            @Override // java.lang.Runnable
            public void run() {
                if (healSlot.maxUnits > 0) {
                    healSlot.maxUnits--;
                    LunariansSettings.save();
                    AutoHealUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table4.label(new Prov<CharSequence>() { // from class: lunarians.ui.AutoHealUi.12
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m52get() {
                return healSlot.maxUnits <= 0 ? AutoHealUi.all() : String.valueOf(healSlot.maxUnits);
            }
        }).width(40.0f).center().pad(2.0f);
        table4.button("+", new Runnable() { // from class: lunarians.ui.AutoHealUi.13
            @Override // java.lang.Runnable
            public void run() {
                if (healSlot.maxUnits < 50) {
                    healSlot.maxUnits++;
                    LunariansSettings.save();
                    AutoHealUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table.add(table4).left().padTop(2.0f).row();
        return table;
    }

    private static String title() {
        return "Auto heal";
    }

    private static String hint() {
        if ("pt".equals(LunariansSettings.language)) {
            return "Sem slots = todas as unidades em comando. Com slots = só esses tipos. Vai ao repair (borda do raio, pathfind).";
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
