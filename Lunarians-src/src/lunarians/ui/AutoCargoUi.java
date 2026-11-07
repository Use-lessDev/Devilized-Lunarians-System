package lunarians.ui;

import arc.func.Boolf;
import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import arc.struct.Seq;
import lunarians.AutoCargo;
import lunarians.LunariansSettings;
import lunarians.data.CargoSlot;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoCargoUi.class */
public class AutoCargoUi {
    private final Table list = new Table();
    private static CargoSlot clipboard = null;

    public static BuilderDialog make() {
        return new BuilderDialog(title(), new Cons<Table>() { // from class: lunarians.ui.AutoCargoUi.1
            public void get(Table table) {
                new AutoCargoUi(table);
            }
        }, new Runnable() { // from class: lunarians.ui.AutoCargoUi.2
            @Override // java.lang.Runnable
            public void run() {
                AutoCargo.reset();
            }
        });
    }

    private static void reopen() {
        make().after(new Runnable() { // from class: lunarians.ui.AutoCargoUi.3
            @Override // java.lang.Runnable
            public void run() {
                new ConfigDialog().show();
            }
        }).show();
    }

    private static boolean isCarrier(UnitType unitType) {
        return unitType.pickupUnits && unitType.payloadCapacity > 0.0f && unitType.commands != null && unitType.commands.contains(UnitCommand.loopPayloadCommand);
    }

    public AutoCargoUi(Table table) {
        table.defaults().growX();
        table.add(hint()).wrap().growX().color(Color.lightGray).padBottom(4.0f).row();
        table.add(this.list).growX().row();
        rebuild();
    }

    private void rebuild() {
        this.list.clearChildren();
        Seq<CargoSlot> seq = LunariansSettings.cargoSlots;
        this.list.add(seq.size + "/10").color(Pal.accent).padBottom(4.0f).row();
        if (seq.isEmpty()) {
            this.list.add(emptyMsg()).wrap().growX().padBottom(4.0f).row();
        } else {
            for (int i = 0; i < seq.size; i++) {
                this.list.add(card((CargoSlot) seq.get(i))).growX().padBottom(3.0f).row();
            }
        }
        if (seq.size < 10) {
            this.list.button(Icon.add, Styles.defaulti, new Runnable() { // from class: lunarians.ui.AutoCargoUi.4
                @Override // java.lang.Runnable
                public void run() {
                    if (LunariansSettings.cargoSlots.size < 10) {
                        LunariansSettings.cargoSlots.add(new CargoSlot());
                        LunariansSettings.save();
                        AutoCargoUi.this.rebuild();
                    }
                }
            }).size(40.0f).padTop(2.0f).row();
        }
    }

    private Table card(final CargoSlot cargoSlot) {
        String sb;
        Table table = new Table(Styles.black6);
        table.defaults().growX();
        table.defaults().pad(4.0f);
        Table table2 = new Table();
        UnitType unit = cargoSlot.unit.isEmpty() ? null : Vars.content.unit(cargoSlot.unit);
        ImageButton imageButton = new ImageButton(unit != null ? new TextureRegionDrawable(unit.uiIcon) : Icon.add, Styles.defaulti);
        imageButton.clicked(new Runnable() { // from class: lunarians.ui.AutoCargoUi.5
            @Override // java.lang.Runnable
            public void run() {
                new UnitPickerDialog(new Cons<UnitType>() { // from class: lunarians.ui.AutoCargoUi.5.1
                    public void get(UnitType unitType) {
                        cargoSlot.unit = unitType.name;
                        LunariansSettings.save();
                        AutoCargoUi.this.rebuild();
                    }
                }, new Boolf<UnitType>() { // from class: lunarians.ui.AutoCargoUi.5.2
                    public boolean get(UnitType unitType) {
                        return AutoCargoUi.isCarrier(unitType);
                    }
                }).show();
            }
        });
        table2.add(imageButton).size(40.0f).padRight(6.0f);
        table2.add(unit != null ? unit.localizedName : pickUnit()).wrap().growX().left().fontScale(0.9f);
        table2.button(copyLabel(), new Runnable() { // from class: lunarians.ui.AutoCargoUi.6
            @Override // java.lang.Runnable
            public void run() {
                AutoCargoUi.clipboard = cargoSlot.copy();
                Vars.ui.showInfoToast(AutoCargoUi.copied(), 2.0f);
                AutoCargoUi.this.rebuild();
            }
        }).width(64.0f).height(32.0f).padLeft(4.0f).get().getLabel().setFontScale(0.8f);
        table2.button(Icon.trash, Styles.defaulti, new Runnable() { // from class: lunarians.ui.AutoCargoUi.7
            @Override // java.lang.Runnable
            public void run() {
                LunariansSettings.cargoSlots.remove(cargoSlot);
                LunariansSettings.save();
                AutoCargoUi.this.rebuild();
            }
        }).size(36.0f).padLeft(4.0f);
        table.add(table2).growX().row();
        Table table3 = new Table();
        TextButton textButton = table3.button(pasteLabel(), new Runnable() { // from class: lunarians.ui.AutoCargoUi.8
            @Override // java.lang.Runnable
            public void run() {
                if (AutoCargoUi.clipboard == null) {
                    Vars.ui.showInfoToast(AutoCargoUi.noClip(), 2.0f);
                    return;
                }
                if (LunariansSettings.cargoSlots.size >= 10) {
                    Vars.ui.showInfoToast("Max slots", 2.0f);
                    return;
                }
                LunariansSettings.cargoSlots.add(AutoCargoUi.clipboard.copy());
                LunariansSettings.save();
                Vars.ui.showInfoToast(AutoCargoUi.pasted(), 2.0f);
                AutoCargoUi.this.rebuild();
            }
        }).width(90.0f).height(36.0f).pad(2.0f).get();
        textButton.getLabel().setFontScale(0.85f);
        textButton.setDisabled(clipboard == null);
        this.list.add(table3).left().padTop(2.0f).row();
        Table table4 = new Table();
        table4.add(maxLabel()).left().padRight(4.0f).fontScale(0.85f);
        table4.button("-", new Runnable() { // from class: lunarians.ui.AutoCargoUi.9
            @Override // java.lang.Runnable
            public void run() {
                if (cargoSlot.maxUnits > 0) {
                    cargoSlot.maxUnits--;
                    LunariansSettings.save();
                    AutoCargoUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table4.label(new Prov<CharSequence>() { // from class: lunarians.ui.AutoCargoUi.10
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m47get() {
                return cargoSlot.maxUnits <= 0 ? AutoCargoUi.maxAll() : String.valueOf(cargoSlot.maxUnits);
            }

            /* JADX WARN: Classes with same name are omitted:
              classes.dex
             */
            /* renamed from: lunarians.ui.AutoCargoUi$10$1, reason: invalid class name */
            /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoCargoUi$10$1.class */
            class AnonymousClass1 implements Cons<IntSeq> {
                AnonymousClass1() {
                }

                public void get(IntSeq intSeq) {
                    AnonymousClass10.this.val$cargoSlot.clearPoints();
                    for (int i = 0; i + 1 < intSeq.size; i += 2) {
                        AnonymousClass10.this.val$cargoSlot.addPoint(intSeq.get(i), intSeq.get(i + 1));
                    }
                    LunariansSettings.save();
                    AutoCargoUi.access$300();
                }
            }

            /* JADX WARN: Classes with same name are omitted:
              classes.dex
             */
            /* renamed from: lunarians.ui.AutoCargoUi$10$2, reason: invalid class name */
            /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoCargoUi$10$2.class */
            class AnonymousClass2 implements Runnable {
                AnonymousClass2() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    AutoCargoUi.access$300();
                }
            }
        }).width(40.0f).center().pad(2.0f);
        table4.button("+", new Runnable() { // from class: lunarians.ui.AutoCargoUi.11
            @Override // java.lang.Runnable
            public void run() {
                if (cargoSlot.maxUnits < 50) {
                    cargoSlot.maxUnits++;
                    LunariansSettings.save();
                    AutoCargoUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table.add(table4).left().padTop(2.0f).row();
        int pointCount = cargoSlot.pointCount();
        if (pointCount == 0) {
            sb = pathEmpty();
        } else {
            StringBuilder sb2 = new StringBuilder();
            int i = 0;
            while (i < pointCount) {
                if (i > 0) {
                    sb2.append(" → ");
                }
                sb2.append(i == 0 ? "P1" : i == pointCount - 1 ? "Drop" : "P" + (i + 1)).append("(").append(cargoSlot.px(i)).append(",").append(cargoSlot.py(i)).append(")");
                i++;
            }
            sb = sb2.toString();
        }
        table.add(sb).wrap().growX().left().fontScale(0.8f).color(pointCount >= 2 ? Color.white : Color.lightGray).row();
        Table table5 = new Table();
        table5.button(pathBtn(), new Runnable() { // from class: lunarians.ui.AutoCargoUi.12
            @Override // java.lang.Runnable
            public void run() {
                FeatureDialog featureDialog = FeatureDialog.top;
                if (featureDialog != null) {
                    featureDialog.after(null);
                    featureDialog.hide();
                }
                PathPicker.pick(new Cons<IntSeq>() { // from class: lunarians.ui.AutoCargoUi.12.1
                    public void get(IntSeq intSeq) {
                        cargoSlot.clearPoints();
                        for (int i2 = 0; i2 + 1 < intSeq.size; i2 += 2) {
                            cargoSlot.addPoint(intSeq.get(i2), intSeq.get(i2 + 1));
                        }
                        LunariansSettings.save();
                        AutoCargoUi.reopen();
                    }
                }, new Runnable() { // from class: lunarians.ui.AutoCargoUi.12.2
                    @Override // java.lang.Runnable
                    public void run() {
                        AutoCargoUi.reopen();
                    }
                });
            }
        }).width(120.0f).height(32.0f).get().getLabel().setFontScale(0.85f);
        if (pointCount > 0) {
            table5.button(Icon.trash, Styles.defaulti, new Runnable() { // from class: lunarians.ui.AutoCargoUi.13
                @Override // java.lang.Runnable
                public void run() {
                    cargoSlot.clearPoints();
                    LunariansSettings.save();
                    AutoCargoUi.this.rebuild();
                }
            }).size(32.0f).padLeft(4.0f);
        }
        table.add(table5).left().padTop(2.0f).row();
        return table;
    }

    private static String title() {
        return "Auto cargo";
    }

    private static String hint() {
        if ("pt".equals(LunariansSettings.language)) {
            return "Pontos: 1=pegar, meio=caminho, último=soltar. Mapa → Add point / Back / Done. Camera/Unit.";
        }
        return "Points: 1=pick, mid=path, last=drop. Map → Add/Back/Done. Camera/Unit mode.";
    }

    private static String emptyMsg() {
        return "pt".equals(LunariansSettings.language) ? "Nenhum slot. Aperte +" : "No slots. Press +";
    }

    private static String pickUnit() {
        return "pt".equals(LunariansSettings.language) ? "Escolher" : "Choose";
    }

    private static String maxLabel() {
        return "pt".equals(LunariansSettings.language) ? "Máx:" : "Max:";
    }

    private static String maxAll() {
        return "pt".equals(LunariansSettings.language) ? "Todas" : "All";
    }

    private static String pathEmpty() {
        return "pt".equals(LunariansSettings.language) ? "Sem rota — defina pontos" : "No path — set points";
    }

    private static String pathBtn() {
        return "pt".equals(LunariansSettings.language) ? "Pontos mapa" : "Map points";
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
