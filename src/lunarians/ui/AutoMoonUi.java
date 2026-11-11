/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.func.Boolf
 *  arc.func.Cons
 *  arc.graphics.Color
 *  arc.scene.Element
 *  arc.scene.style.Drawable
 *  arc.scene.style.TextureRegionDrawable
 *  arc.scene.ui.ImageButton
 *  arc.scene.ui.TextButton
 *  arc.scene.ui.layout.Table
 *  arc.struct.Seq
 *  mindustry.Vars
 *  mindustry.gen.Groups
 *  mindustry.gen.Icon
 *  mindustry.gen.Unit
 *  mindustry.graphics.Pal
 *  mindustry.type.UnitType
 *  mindustry.ui.Styles
 */
package lunarians.ui;

import arc.func.Boolf;
import arc.func.Cons;
import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.AutoMoon;
import lunarians.LunariansSettings;
import lunarians.data.MoonSlot;
import lunarians.ui.BuilderDialog;
import lunarians.ui.ConfigDialog;
import lunarians.ui.FeatureDialog;
import lunarians.ui.PositionPicker;
import lunarians.ui.UnitOnMapPicker;
import lunarians.ui.UnitPickerDialog;
import mindustry.Vars;
import mindustry.gen.Groups;
import mindustry.gen.Icon;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

public class AutoMoonUi {
    private final Table list = new Table();
    private static MoonSlot clipboard = null;

    public static BuilderDialog make() {
        return new BuilderDialog(AutoMoonUi.title(), new Cons<Table>(){

            public void get(Table table) {
                new AutoMoonUi(table);
            }
        }, new Runnable(){

            @Override
            public void run() {
                AutoMoon.reset();
            }
        });
    }

    private static void reopen() {
        AutoMoonUi.make().after(new Runnable(){

            @Override
            public void run() {
                new ConfigDialog().show();
            }
        }).show();
    }

    public AutoMoonUi(Table table) {
        table.defaults().growX();
        table.add((CharSequence)AutoMoonUi.hint()).wrap().growX().color(Color.lightGray).padBottom(4.0f).row();
        table.add((Element)this.list).growX().row();
        this.rebuild();
    }

    private void rebuild() {
        this.list.clearChildren();
        Seq<MoonSlot> seq = LunariansSettings.moonSlots;
        this.list.add((CharSequence)(seq.size + "/8")).color(Pal.accent).padBottom(4.0f).row();
        if (seq.isEmpty()) {
            this.list.add((CharSequence)AutoMoonUi.empty()).wrap().growX().row();
        } else {
            for (int i = 0; i < seq.size; ++i) {
                this.list.add((Element)this.card((MoonSlot)seq.get(i))).growX().padBottom(3.0f).row();
            }
        }
        if (seq.size < 8) {
            this.list.button((Drawable)Icon.add, Styles.defaulti, new Runnable(){

                @Override
                public void run() {
                    LunariansSettings.moonSlots.add((Object)new MoonSlot());
                    LunariansSettings.save();
                    AutoMoonUi.this.rebuild();
                }
            }).size(40.0f).padTop(2.0f).row();
        }
    }

    private Table card(final MoonSlot moonSlot) {
        Unit unit;
        Table table = new Table(Styles.black6);
        table.defaults().pad(4.0f).growX();
        Table table2 = new Table();
        UnitType unitType = moonSlot.unit.isEmpty() ? null : Vars.content.unit(moonSlot.unit);
        ImageButton imageButton = new ImageButton((Drawable)(unitType != null ? new TextureRegionDrawable(unitType.uiIcon) : Icon.add), Styles.defaulti);
        imageButton.clicked(new Runnable(){

            @Override
            public void run() {
                new UnitPickerDialog(new Cons<UnitType>(){

                    public void get(UnitType unitType) {
                        moonSlot.unit = unitType.name;
                        LunariansSettings.save();
                        AutoMoonUi.this.rebuild();
                    }
                }, new Boolf<UnitType>(){

                    public boolean get(UnitType unitType) {
                        return unitType.flying;
                    }
                }).show();
            }
        });
        table2.add((Element)imageButton).size(40.0f).padRight(6.0f);
        table2.add((CharSequence)(unitType != null ? unitType.localizedName : AutoMoonUi.pick())).wrap().growX().left().fontScale(0.9f);
        TextButton textButton = (TextButton)table2.button(AutoMoonUi.copyLabel(), new Runnable(){

            @Override
            public void run() {
                clipboard = moonSlot.copy();
                Vars.ui.showInfoToast(AutoMoonUi.copied(), 2.0f);
                AutoMoonUi.this.rebuild();
            }
        }).width(64.0f).height(32.0f).padLeft(4.0f).get();
        textButton.getLabel().setFontScale(0.8f);
        table2.button((Drawable)Icon.trash, Styles.defaulti, new Runnable(){

            @Override
            public void run() {
                LunariansSettings.moonSlots.remove((Object)moonSlot);
                LunariansSettings.save();
                AutoMoonUi.this.rebuild();
            }
        }).size(36.0f);
        table.add((Element)table2).growX().row();
        Table table3 = new Table();
        Object object = (TextButton)table3.button(AutoMoonUi.pasteLabel(), new Runnable(){

            @Override
            public void run() {
                if (clipboard == null) {
                    Vars.ui.showInfoToast(AutoMoonUi.noClip(), 2.0f);
                    return;
                }
                if (LunariansSettings.moonSlots.size >= 8) {
                    Vars.ui.showInfoToast("Max slots", 2.0f);
                    return;
                }
                LunariansSettings.moonSlots.add((Object)clipboard.copy());
                LunariansSettings.save();
                Vars.ui.showInfoToast(AutoMoonUi.pasted(), 2.0f);
                AutoMoonUi.this.rebuild();
            }
        }).width(90.0f).height(36.0f).pad(2.0f).get();
        object.getLabel().setFontScale(0.85f);
        object.setDisabled(clipboard == null);
        this.list.add((Element)table3).left().padTop(2.0f).row();
        this.rowStepper(table, AutoMoonUi.maxL(), moonSlot.maxUnits <= 0 ? AutoMoonUi.all() : String.valueOf(moonSlot.maxUnits), new Runnable(){

            @Override
            public void run() {
                if (moonSlot.maxUnits > 0) {
                    --moonSlot.maxUnits;
                    LunariansSettings.save();
                    AutoMoonUi.this.rebuild();
                }
            }
        }, new Runnable(){

            @Override
            public void run() {
                if (moonSlot.maxUnits < 50) {
                    ++moonSlot.maxUnits;
                    LunariansSettings.save();
                    AutoMoonUi.this.rebuild();
                }
            }
        });
        this.rowStepper(table, AutoMoonUi.radL(), (int)moonSlot.radius + "b", new Runnable(){

            @Override
            public void run() {
                if (moonSlot.radius > 1.0f) {
                    moonSlot.radius = Math.max(1.0f, moonSlot.radius - 1.0f);
                    LunariansSettings.save();
                    AutoMoonUi.this.rebuild();
                }
            }
        }, new Runnable(){

            @Override
            public void run() {
                if (moonSlot.radius < 40.0f) {
                    moonSlot.radius += 1.0f;
                    LunariansSettings.save();
                    AutoMoonUi.this.rebuild();
                }
            }
        });
        this.rowStepper(table, AutoMoonUi.speedL(), String.format("%.2fx", Float.valueOf(moonSlot.speed)), new Runnable(){

            @Override
            public void run() {
                moonSlot.speed = Math.max(0.25f, moonSlot.speed - 0.25f);
                LunariansSettings.save();
                AutoMoonUi.this.rebuild();
            }
        }, new Runnable(){

            @Override
            public void run() {
                moonSlot.speed = Math.min(4.0f, moonSlot.speed + 0.25f);
                LunariansSettings.save();
                AutoMoonUi.this.rebuild();
            }
        });
        table3 = new Table();
        object = moonSlot.hasUnitCenter() ? ((unit = (Unit)Groups.unit.getByID(moonSlot.followId)) != null && unit.isValid() ? unit.type.localizedName + " #" + moonSlot.followId : (moonSlot.followLabel != null && !moonSlot.followLabel.isEmpty() ? moonSlot.followLabel + " #" + moonSlot.followId : "#" + moonSlot.followId)) : (moonSlot.hasTileCenter() ? "tile " + moonSlot.x + "," + moonSlot.y : AutoMoonUi.followPlayer());
        table3.add((CharSequence)(AutoMoonUi.centerL() + " " + (String)object)).wrap().growX().left().fontScale(0.85f).color(moonSlot.hasUnitCenter() || moonSlot.hasTileCenter() ? Color.white : Color.lightGray);
        table.add((Element)table3).growX().row();
        unit = new Table();
        TextButton textButton2 = (TextButton)unit.button(AutoMoonUi.unitMapBtn(), new Runnable(){

            @Override
            public void run() {
                FeatureDialog featureDialog = FeatureDialog.top;
                if (featureDialog != null) {
                    featureDialog.after(null);
                    featureDialog.hide();
                }
                UnitOnMapPicker.pick(new Cons<Unit>(){

                    public void get(Unit unit) {
                        moonSlot.followId = unit.id;
                        moonSlot.followLabel = unit.type != null ? unit.type.localizedName : "";
                        LunariansSettings.save();
                        Vars.ui.showInfoToast("pt".equals(LunariansSettings.language) ? "\u00d3rbita: " + moonSlot.followLabel + " #" + moonSlot.followId : "Orbit: " + moonSlot.followLabel + " #" + moonSlot.followId, 2.0f);
                        AutoMoonUi.reopen();
                    }
                }, new Runnable(){

                    @Override
                    public void run() {
                        AutoMoonUi.reopen();
                    }
                });
            }
        }).width(110.0f).height(32.0f).get();
        textButton2.getLabel().setFontScale(0.8f);
        TextButton textButton3 = (TextButton)unit.button(AutoMoonUi.mapBtn(), new Runnable(){

            @Override
            public void run() {
                FeatureDialog featureDialog = FeatureDialog.top;
                if (featureDialog != null) {
                    featureDialog.after(null);
                    featureDialog.hide();
                }
                PositionPicker.pick(new Cons<int[]>(){

                    public void get(int[] nArray) {
                        moonSlot.x = nArray[0];
                        moonSlot.y = nArray[1];
                        LunariansSettings.save();
                        AutoMoonUi.reopen();
                    }
                }, new Runnable(){

                    @Override
                    public void run() {
                        AutoMoonUi.reopen();
                    }
                });
            }
        }).width(70.0f).height(32.0f).padLeft(4.0f).get();
        textButton3.getLabel().setFontScale(0.8f);
        unit.button((Drawable)Icon.trash, Styles.defaulti, new Runnable(){

            @Override
            public void run() {
                moonSlot.followId = -1;
                moonSlot.followLabel = "";
                moonSlot.x = Integer.MIN_VALUE;
                moonSlot.y = Integer.MIN_VALUE;
                LunariansSettings.save();
                AutoMoonUi.this.rebuild();
            }
        }).size(32.0f).padLeft(4.0f);
        table.add((Element)unit).left().row();
        return table;
    }

    private void rowStepper(Table table, String string, String string2, Runnable runnable, Runnable runnable2) {
        Table table2 = new Table();
        table2.add((CharSequence)string).left().padRight(4.0f).fontScale(0.85f);
        table2.button("-", runnable).size(32.0f, 28.0f);
        table2.add((CharSequence)string2).width(48.0f).center().pad(2.0f);
        table2.button("+", runnable2).size(32.0f, 28.0f);
        table.add((Element)table2).left().row();
    }

    private static String title() {
        return "Auto Moon";
    }

    private static String hint() {
        if ("pt".equals(LunariansSettings.language)) {
            return "Voadoras orbitam jogador / tile / unidade no mapa. Velocidade ajust\u00e1vel. Atacam perto.";
        }
        return "Flyers orbit player / tile / map unit. Adjustable speed. Attack nearby.";
    }

    private static String empty() {
        return "pt".equals(LunariansSettings.language) ? "Nenhum slot. +" : "No slots. +";
    }

    private static String pick() {
        return "pt".equals(LunariansSettings.language) ? "Unidade voadora" : "Flying unit";
    }

    private static String maxL() {
        return "pt".equals(LunariansSettings.language) ? "M\u00e1x:" : "Max:";
    }

    private static String all() {
        return "pt".equals(LunariansSettings.language) ? "Todas" : "All";
    }

    private static String radL() {
        return "pt".equals(LunariansSettings.language) ? "Raio:" : "Radius:";
    }

    private static String speedL() {
        return "pt".equals(LunariansSettings.language) ? "Vel:" : "Speed:";
    }

    private static String centerL() {
        return "pt".equals(LunariansSettings.language) ? "Centro:" : "Center:";
    }

    private static String followPlayer() {
        return "pt".equals(LunariansSettings.language) ? "jogador" : "player";
    }

    private static String mapBtn() {
        return "pt".equals(LunariansSettings.language) ? "Tile" : "Tile";
    }

    private static String unitMapBtn() {
        return "pt".equals(LunariansSettings.language) ? "Unidade mapa" : "Map unit";
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

