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
 *  arc.struct.IntSeq
 *  arc.struct.Seq
 *  mindustry.Vars
 *  mindustry.ai.UnitCommand
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
import arc.struct.IntSeq;
import arc.struct.Seq;
import lunarians.AutoCargo;
import lunarians.LunariansSettings;
import lunarians.data.CargoSlot;
import lunarians.ui.BuilderDialog;
import lunarians.ui.ConfigDialog;
import lunarians.ui.FeatureDialog;
import lunarians.ui.PathPicker;
import lunarians.ui.UnitPickerDialog;
import mindustry.Vars;
import mindustry.ai.UnitCommand;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

public class AutoCargoUi {
    private final Table list = new Table();
    private static CargoSlot clipboard = null;

    public static BuilderDialog make() {
        return new BuilderDialog(AutoCargoUi.title(), new Cons<Table>(){

            public void get(Table table) {
                new AutoCargoUi(table);
            }
        }, new Runnable(){

            @Override
            public void run() {
                AutoCargo.reset();
            }
        });
    }

    private static void reopen() {
        AutoCargoUi.make().after(new Runnable(){

            @Override
            public void run() {
                new ConfigDialog().show();
            }
        }).show();
    }

    private static boolean isCarrier(UnitType unitType) {
        return unitType.pickupUnits && unitType.payloadCapacity > 0.0f && unitType.commands != null && unitType.commands.contains((Object)UnitCommand.loopPayloadCommand);
    }

    public AutoCargoUi(Table table) {
        table.defaults().growX();
        table.add((CharSequence)AutoCargoUi.hint()).wrap().growX().color(Color.lightGray).padBottom(4.0f).row();
        table.add((Element)this.list).growX().row();
        this.rebuild();
    }

    private void rebuild() {
        this.list.clearChildren();
        Seq<CargoSlot> seq = LunariansSettings.cargoSlots;
        this.list.add((CharSequence)(seq.size + "/10")).color(Pal.accent).padBottom(4.0f).row();
        if (seq.isEmpty()) {
            this.list.add((CharSequence)AutoCargoUi.emptyMsg()).wrap().growX().padBottom(4.0f).row();
        } else {
            for (int i = 0; i < seq.size; ++i) {
                this.list.add((Element)this.card((CargoSlot)seq.get(i))).growX().padBottom(3.0f).row();
            }
        }
        if (seq.size < 10) {
            this.list.button((Drawable)Icon.add, Styles.defaulti, new Runnable(){

                @Override
                public void run() {
                    if (LunariansSettings.cargoSlots.size < 10) {
                        LunariansSettings.cargoSlots.add((Object)new CargoSlot());
                        LunariansSettings.save();
                        AutoCargoUi.this.rebuild();
                    }
                }
            }).size(40.0f).padTop(2.0f).row();
        }
    }

    private Table card(final CargoSlot cargoSlot) {
        StringBuilder stringBuilder;
        String string;
        Table table = new Table(Styles.black6);
        table.defaults().growX();
        table.defaults().pad(4.0f);
        Table table2 = new Table();
        UnitType unitType = cargoSlot.unit.isEmpty() ? null : Vars.content.unit(cargoSlot.unit);
        ImageButton imageButton = new ImageButton((Drawable)(unitType != null ? new TextureRegionDrawable(unitType.uiIcon) : Icon.add), Styles.defaulti);
        imageButton.clicked(new Runnable(){

            @Override
            public void run() {
                new UnitPickerDialog(new Cons<UnitType>(){

                    public void get(UnitType unitType) {
                        cargoSlot.unit = unitType.name;
                        LunariansSettings.save();
                        AutoCargoUi.this.rebuild();
                    }
                }, new Boolf<UnitType>(){

                    public boolean get(UnitType unitType) {
                        return AutoCargoUi.isCarrier(unitType);
                    }
                }).show();
            }
        });
        table2.add((Element)imageButton).size(40.0f).padRight(6.0f);
        table2.add((CharSequence)(unitType != null ? unitType.localizedName : AutoCargoUi.pickUnit())).wrap().growX().left().fontScale(0.9f);
        TextButton textButton = (TextButton)table2.button(AutoCargoUi.copyLabel(), new Runnable(){

            @Override
            public void run() {
                clipboard = cargoSlot.copy();
                Vars.ui.showInfoToast(AutoCargoUi.copied(), 2.0f);
                AutoCargoUi.this.rebuild();
            }
        }).width(64.0f).height(32.0f).padLeft(4.0f).get();
        textButton.getLabel().setFontScale(0.8f);
        table2.button((Drawable)Icon.trash, Styles.defaulti, new Runnable(){

            @Override
            public void run() {
                LunariansSettings.cargoSlots.remove((Object)cargoSlot);
                LunariansSettings.save();
                AutoCargoUi.this.rebuild();
            }
        }).size(36.0f).padLeft(4.0f);
        table.add((Element)table2).growX().row();
        Table table3 = new Table();
        TextButton textButton2 = (TextButton)table3.button(AutoCargoUi.pasteLabel(), new Runnable(){

            @Override
            public void run() {
                if (clipboard == null) {
                    Vars.ui.showInfoToast(AutoCargoUi.noClip(), 2.0f);
                    return;
                }
                if (LunariansSettings.cargoSlots.size >= 10) {
                    Vars.ui.showInfoToast("Max slots", 2.0f);
                    return;
                }
                LunariansSettings.cargoSlots.add((Object)clipboard.copy());
                LunariansSettings.save();
                Vars.ui.showInfoToast(AutoCargoUi.pasted(), 2.0f);
                AutoCargoUi.this.rebuild();
            }
        }).width(90.0f).height(36.0f).pad(2.0f).get();
        textButton2.getLabel().setFontScale(0.85f);
        textButton2.setDisabled(clipboard == null);
        this.list.add((Element)table3).left().padTop(2.0f).row();
        table3 = new Table();
        table3.add((CharSequence)AutoCargoUi.maxLabel()).left().padRight(4.0f).fontScale(0.85f);
        table3.button("-", new Runnable(){

            @Override
            public void run() {
                if (cargoSlot.maxUnits > 0) {
                    --cargoSlot.maxUnits;
                    LunariansSettings.save();
                    AutoCargoUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table3.label((Prov)new Prov<CharSequence>(){

            public CharSequence get() {
                return cargoSlot.maxUnits <= 0 ? AutoCargoUi.maxAll() : String.valueOf(cargoSlot.maxUnits);
            }
        }).width(40.0f).center().pad(2.0f);
        table3.button("+", new Runnable(){

            @Override
            public void run() {
                if (cargoSlot.maxUnits < 50) {
                    ++cargoSlot.maxUnits;
                    LunariansSettings.save();
                    AutoCargoUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table.add((Element)table3).left().padTop(2.0f).row();
        int n = cargoSlot.pointCount();
        if (n == 0) {
            string = AutoCargoUi.pathEmpty();
        } else {
            stringBuilder = new StringBuilder();
            for (int i = 0; i < n; ++i) {
                if (i > 0) {
                    stringBuilder.append(" \u2192 ");
                }
                String string2 = i == 0 ? "P1" : (i == n - 1 ? "Drop" : "P" + (i + 1));
                stringBuilder.append(string2).append("(").append(cargoSlot.px(i)).append(",").append(cargoSlot.py(i)).append(")");
            }
            string = stringBuilder.toString();
        }
        table.add((CharSequence)string).wrap().growX().left().fontScale(0.8f).color(n >= 2 ? Color.white : Color.lightGray).row();
        stringBuilder = new Table();
        TextButton textButton3 = (TextButton)stringBuilder.button(AutoCargoUi.pathBtn(), new Runnable(){

            @Override
            public void run() {
                FeatureDialog featureDialog = FeatureDialog.top;
                if (featureDialog != null) {
                    featureDialog.after(null);
                    featureDialog.hide();
                }
                PathPicker.pick(new Cons<IntSeq>(){

                    public void get(IntSeq intSeq) {
                        cargoSlot.clearPoints();
                        int n = 0;
                        while (n + 1 < intSeq.size) {
                            cargoSlot.addPoint(intSeq.get(n), intSeq.get(n + 1));
                            n += 2;
                        }
                        LunariansSettings.save();
                        AutoCargoUi.reopen();
                    }
                }, new Runnable(){

                    @Override
                    public void run() {
                        AutoCargoUi.reopen();
                    }
                });
            }
        }).width(120.0f).height(32.0f).get();
        textButton3.getLabel().setFontScale(0.85f);
        if (n > 0) {
            stringBuilder.button((Drawable)Icon.trash, Styles.defaulti, new Runnable(){

                @Override
                public void run() {
                    cargoSlot.clearPoints();
                    LunariansSettings.save();
                    AutoCargoUi.this.rebuild();
                }
            }).size(32.0f).padLeft(4.0f);
        }
        table.add((Element)stringBuilder).left().padTop(2.0f).row();
        return table;
    }

    private static String title() {
        return "Auto cargo";
    }

    private static String hint() {
        if ("pt".equals(LunariansSettings.language)) {
            return "Pontos: 1=pegar, meio=caminho, \u00faltimo=soltar. Mapa \u2192 Add point / Back / Done. Camera/Unit.";
        }
        return "Points: 1=pick, mid=path, last=drop. Map \u2192 Add/Back/Done. Camera/Unit mode.";
    }

    private static String emptyMsg() {
        return "pt".equals(LunariansSettings.language) ? "Nenhum slot. Aperte +" : "No slots. Press +";
    }

    private static String pickUnit() {
        return "pt".equals(LunariansSettings.language) ? "Escolher" : "Choose";
    }

    private static String maxLabel() {
        return "pt".equals(LunariansSettings.language) ? "M\u00e1x:" : "Max:";
    }

    private static String maxAll() {
        return "pt".equals(LunariansSettings.language) ? "Todas" : "All";
    }

    private static String pathEmpty() {
        return "pt".equals(LunariansSettings.language) ? "Sem rota \u2014 defina pontos" : "No path \u2014 set points";
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

