package lunarians.ui;

import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.AutoDefence;
import lunarians.LunariansSettings;
import lunarians.data.DefenceSlot;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoDefenceUi.class */
public class AutoDefenceUi {
    private final Table list = new Table();
    private static DefenceSlot clipboard = null;

    public static BuilderDialog make() {
        return new BuilderDialog(title(), new Cons<Table>() { // from class: lunarians.ui.AutoDefenceUi.1
            public void get(Table table) {
                new AutoDefenceUi(table);
            }
        }, new Runnable() { // from class: lunarians.ui.AutoDefenceUi.2
            @Override // java.lang.Runnable
            public void run() {
                AutoDefence.reset();
            }
        });
    }

    private static void reopen() {
        make().after(new Runnable() { // from class: lunarians.ui.AutoDefenceUi.3
            @Override // java.lang.Runnable
            public void run() {
                new ConfigDialog().show();
            }
        }).show();
    }

    public AutoDefenceUi(Table table) {
        table.defaults().growX();
        table.add(hint()).wrap().growX().color(Color.lightGray).padBottom(4.0f).row();
        table.add(this.list).growX().row();
        rebuild();
    }

    private void rebuild() {
        this.list.clearChildren();
        Seq<DefenceSlot> seq = LunariansSettings.defenceSlots;
        this.list.add(seq.size + "/8").color(Pal.accent).padBottom(4.0f).row();
        if (seq.isEmpty()) {
            this.list.add(emptyMsg()).wrap().growX().padBottom(4.0f).row();
        } else {
            for (int i = 0; i < seq.size; i++) {
                this.list.add(card((DefenceSlot) seq.get(i))).growX().padBottom(3.0f).row();
            }
        }
        if (seq.size < 8) {
            this.list.button(Icon.add, Styles.defaulti, new Runnable() { // from class: lunarians.ui.AutoDefenceUi.4
                @Override // java.lang.Runnable
                public void run() {
                    if (LunariansSettings.defenceSlots.size < 8) {
                        LunariansSettings.defenceSlots.add(new DefenceSlot());
                        LunariansSettings.save();
                        AutoDefenceUi.this.rebuild();
                    }
                }
            }).size(40.0f).padTop(2.0f).row();
        }
    }

    private Table card(final DefenceSlot defenceSlot) {
        Table table = new Table(Styles.black6);
        table.defaults().growX();
        table.defaults().pad(4.0f);
        Table table2 = new Table();
        UnitType unit = defenceSlot.unit.isEmpty() ? null : Vars.content.unit(defenceSlot.unit);
        ImageButton imageButton = new ImageButton(unit != null ? new TextureRegionDrawable(unit.uiIcon) : Icon.add, Styles.defaulti);
        imageButton.clicked(new Runnable() { // from class: lunarians.ui.AutoDefenceUi.5
            @Override // java.lang.Runnable
            public void run() {
                new UnitPickerDialog(new Cons<UnitType>() { // from class: lunarians.ui.AutoDefenceUi.5.1
                    public void get(UnitType unitType) {
                        defenceSlot.unit = unitType.name;
                        LunariansSettings.save();
                        AutoDefenceUi.this.rebuild();
                    }
                }, null).show();
            }
        });
        table2.add(imageButton).size(40.0f).padRight(6.0f);
        table2.add(unit != null ? unit.localizedName : pickUnit()).wrap().growX().left().fontScale(0.9f);
        table2.button(copyLabel(), new Runnable() { // from class: lunarians.ui.AutoDefenceUi.6
            @Override // java.lang.Runnable
            public void run() {
                AutoDefenceUi.clipboard = defenceSlot.copy();
                Vars.ui.showInfoToast(AutoDefenceUi.copied(), 2.0f);
                AutoDefenceUi.this.rebuild();
            }
        }).width(64.0f).height(32.0f).padLeft(4.0f).get().getLabel().setFontScale(0.8f);
        table2.button(Icon.trash, Styles.defaulti, new Runnable() { // from class: lunarians.ui.AutoDefenceUi.7
            @Override // java.lang.Runnable
            public void run() {
                LunariansSettings.defenceSlots.remove(defenceSlot);
                LunariansSettings.save();
                AutoDefenceUi.this.rebuild();
            }
        }).size(36.0f).padLeft(4.0f);
        table.add(table2).growX().row();
        Table table3 = new Table();
        TextButton textButton = table3.button(pasteLabel(), new Runnable() { // from class: lunarians.ui.AutoDefenceUi.8
            @Override // java.lang.Runnable
            public void run() {
                if (AutoDefenceUi.clipboard == null) {
                    Vars.ui.showInfoToast(AutoDefenceUi.noClip(), 2.0f);
                    return;
                }
                if (LunariansSettings.defenceSlots.size >= 8) {
                    Vars.ui.showInfoToast("Max slots", 2.0f);
                    return;
                }
                LunariansSettings.defenceSlots.add(AutoDefenceUi.clipboard.copy());
                LunariansSettings.save();
                Vars.ui.showInfoToast(AutoDefenceUi.pasted(), 2.0f);
                AutoDefenceUi.this.rebuild();
            }
        }).width(90.0f).height(36.0f).pad(2.0f).get();
        textButton.getLabel().setFontScale(0.85f);
        textButton.setDisabled(clipboard == null);
        this.list.add(table3).left().padTop(2.0f).row();
        Table table4 = new Table();
        table4.add(maxLabel()).left().padRight(4.0f).fontScale(0.85f);
        table4.button("-", new Runnable() { // from class: lunarians.ui.AutoDefenceUi.9
            @Override // java.lang.Runnable
            public void run() {
                if (defenceSlot.maxUnits > 0) {
                    defenceSlot.maxUnits--;
                    LunariansSettings.save();
                    AutoDefenceUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table4.label(new Prov<CharSequence>() { // from class: lunarians.ui.AutoDefenceUi.10
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m49get() {
                return defenceSlot.maxUnits <= 0 ? AutoDefenceUi.all() : String.valueOf(defenceSlot.maxUnits);
            }
        }).width(40.0f).center().pad(2.0f);
        table4.button("+", new Runnable() { // from class: lunarians.ui.AutoDefenceUi.11
            @Override // java.lang.Runnable
            public void run() {
                if (defenceSlot.maxUnits < 50) {
                    defenceSlot.maxUnits++;
                    LunariansSettings.save();
                    AutoDefenceUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table.add(table4).left().padTop(2.0f).row();
        Table table5 = new Table();
        table5.add(radLabel()).left().padRight(4.0f).fontScale(0.85f);
        table5.button("-", new Runnable() { // from class: lunarians.ui.AutoDefenceUi.12
            @Override // java.lang.Runnable
            public void run() {
                if (defenceSlot.radius > 2.0f) {
                    defenceSlot.radius = Math.max(2.0f, defenceSlot.radius - 1.0f);
                    LunariansSettings.save();
                    AutoDefenceUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table5.label(new Prov<CharSequence>() { // from class: lunarians.ui.AutoDefenceUi.13
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m50get() {
                return ((int) defenceSlot.radius) + "b";
            }

            /* JADX WARN: Classes with same name are omitted:
              classes.dex
             */
            /* renamed from: lunarians.ui.AutoDefenceUi$13$1, reason: invalid class name */
            /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoDefenceUi$13$1.class */
            class AnonymousClass1 implements Cons<int[]> {
                AnonymousClass1() {
                }

                public void get(int[] iArr) {
                    AnonymousClass13.this.val$defenceSlot.x = iArr[0];
                    AnonymousClass13.this.val$defenceSlot.y = iArr[1];
                    LunariansSettings.save();
                    AutoDefenceUi.access$200();
                }
            }

            /* JADX WARN: Classes with same name are omitted:
              classes.dex
             */
            /* renamed from: lunarians.ui.AutoDefenceUi$13$2, reason: invalid class name */
            /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoDefenceUi$13$2.class */
            class AnonymousClass2 implements Runnable {
                AnonymousClass2() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    AutoDefenceUi.access$200();
                }
            }
        }).width(40.0f).center().pad(2.0f);
        table5.button("+", new Runnable() { // from class: lunarians.ui.AutoDefenceUi.14
            @Override // java.lang.Runnable
            public void run() {
                if (defenceSlot.radius < 70.0f) {
                    defenceSlot.radius += 1.0f;
                    LunariansSettings.save();
                    AutoDefenceUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table.add(table5).left().padTop(2.0f).row();
        Table table6 = new Table();
        boolean z = defenceSlot.x != Integer.MIN_VALUE;
        table6.add("Pos: " + (z ? defenceSlot.x + "," + defenceSlot.y : "—")).wrap().growX().left().fontScale(0.85f).color(z ? Color.white : Color.lightGray);
        table6.button(mapBtn(), new Runnable() { // from class: lunarians.ui.AutoDefenceUi.15
            @Override // java.lang.Runnable
            public void run() {
                FeatureDialog featureDialog = FeatureDialog.top;
                if (featureDialog != null) {
                    featureDialog.after(null);
                    featureDialog.hide();
                }
                PositionPicker.pick(new Cons<int[]>() { // from class: lunarians.ui.AutoDefenceUi.15.1
                    public void get(int[] iArr) {
                        defenceSlot.x = iArr[0];
                        defenceSlot.y = iArr[1];
                        LunariansSettings.save();
                        AutoDefenceUi.reopen();
                    }
                }, new Runnable() { // from class: lunarians.ui.AutoDefenceUi.15.2
                    @Override // java.lang.Runnable
                    public void run() {
                        AutoDefenceUi.reopen();
                    }
                });
            }
        }).width(90.0f).height(32.0f).padLeft(4.0f);
        table.add(table6).growX().row();
        return table;
    }

    private static String title() {
        String str = LunariansSettings.language;
        return "pt".equals(str) ? "Auto defesa" : "es".equals(str) ? "Auto defensa" : "ru".equals(str) ? "Авто-защита" : "Auto defence";
    }

    private static String hint() {
        String str = LunariansSettings.language;
        return "pt".equals(str) ? "Unidade + máx + posição + raio. Círculo vermelho = zona de ataque." : "es".equals(str) ? "Unidad + máx + posición + radio. Círculo rojo = zona." : "Unit + max + position + radius. Red circle = attack zone.";
    }

    private static String emptyMsg() {
        return "pt".equals(LunariansSettings.language) ? "Nenhuma zona. Aperte +" : "No zones. Press +";
    }

    private static String pickUnit() {
        return "pt".equals(LunariansSettings.language) ? "Escolher" : "Choose";
    }

    private static String maxLabel() {
        String str = LunariansSettings.language;
        return ("pt".equals(str) || "es".equals(str)) ? "Máx:" : "Max:";
    }

    private static String radLabel() {
        String str = LunariansSettings.language;
        return "pt".equals(str) ? "Raio:" : "es".equals(str) ? "Radio:" : "Radius:";
    }

    private static String all() {
        String str = LunariansSettings.language;
        return ("pt".equals(str) || "es".equals(str)) ? "Todas" : "All";
    }

    private static String mapBtn() {
        String str = LunariansSettings.language;
        return ("pt".equals(str) || "es".equals(str)) ? "Mapa" : "Map";
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
