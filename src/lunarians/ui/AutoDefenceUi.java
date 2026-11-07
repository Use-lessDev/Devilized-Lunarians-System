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
import lunarians.AutoDefence;
import lunarians.LunariansSettings;
import lunarians.data.DefenceSlot;
import lunarians.ui.BuilderDialog;
import lunarians.ui.ConfigDialog;
import lunarians.ui.FeatureDialog;
import lunarians.ui.PositionPicker;
import lunarians.ui.UnitPickerDialog;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

public class AutoDefenceUi {
    private final Table list = new Table();
    private static DefenceSlot clipboard = null;

    public static BuilderDialog make() {
        return new BuilderDialog(AutoDefenceUi.title(), new Cons<Table>(){

            public void get(Table table) {
                new AutoDefenceUi(table);
            }
        }, new Runnable(){

            @Override
            public void run() {
                AutoDefence.reset();
            }
        });
    }

    private static void reopen() {
        AutoDefenceUi.make().after(new Runnable(){

            @Override
            public void run() {
                new ConfigDialog().show();
            }
        }).show();
    }

    public AutoDefenceUi(Table table) {
        table.defaults().growX();
        table.add((CharSequence)AutoDefenceUi.hint()).wrap().growX().color(Color.lightGray).padBottom(4.0f).row();
        table.add((Element)this.list).growX().row();
        this.rebuild();
    }

    private void rebuild() {
        this.list.clearChildren();
        Seq<DefenceSlot> seq = LunariansSettings.defenceSlots;
        this.list.add((CharSequence)(seq.size + "/8")).color(Pal.accent).padBottom(4.0f).row();
        if (seq.isEmpty()) {
            this.list.add((CharSequence)AutoDefenceUi.emptyMsg()).wrap().growX().padBottom(4.0f).row();
        } else {
            for (int i = 0; i < seq.size; ++i) {
                this.list.add((Element)this.card((DefenceSlot)seq.get(i))).growX().padBottom(3.0f).row();
            }
        }
        if (seq.size < 8) {
            this.list.button((Drawable)Icon.add, Styles.defaulti, new Runnable(){

                @Override
                public void run() {
                    if (LunariansSettings.defenceSlots.size < 8) {
                        LunariansSettings.defenceSlots.add((Object)new DefenceSlot());
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
        UnitType unitType = defenceSlot.unit.isEmpty() ? null : Vars.content.unit(defenceSlot.unit);
        ImageButton imageButton = new ImageButton((Drawable)(unitType != null ? new TextureRegionDrawable(unitType.uiIcon) : Icon.add), Styles.defaulti);
        imageButton.clicked(new Runnable(){

            @Override
            public void run() {
                new UnitPickerDialog(new Cons<UnitType>(){

                    public void get(UnitType unitType) {
                        defenceSlot.unit = unitType.name;
                        LunariansSettings.save();
                        AutoDefenceUi.this.rebuild();
                    }
                }, null).show();
            }
        });
        table2.add((Element)imageButton).size(40.0f).padRight(6.0f);
        table2.add((CharSequence)(unitType != null ? unitType.localizedName : AutoDefenceUi.pickUnit())).wrap().growX().left().fontScale(0.9f);
        TextButton textButton = (TextButton)table2.button(AutoDefenceUi.copyLabel(), new Runnable(){

            @Override
            public void run() {
                clipboard = defenceSlot.copy();
                Vars.ui.showInfoToast(AutoDefenceUi.copied(), 2.0f);
                AutoDefenceUi.this.rebuild();
            }
        }).width(64.0f).height(32.0f).padLeft(4.0f).get();
        textButton.getLabel().setFontScale(0.8f);
        table2.button((Drawable)Icon.trash, Styles.defaulti, new Runnable(){

            @Override
            public void run() {
                LunariansSettings.defenceSlots.remove((Object)defenceSlot);
                LunariansSettings.save();
                AutoDefenceUi.this.rebuild();
            }
        }).size(36.0f).padLeft(4.0f);
        table.add((Element)table2).growX().row();
        Table table3 = new Table();
        TextButton textButton2 = (TextButton)table3.button(AutoDefenceUi.pasteLabel(), new Runnable(){

            @Override
            public void run() {
                if (clipboard == null) {
                    Vars.ui.showInfoToast(AutoDefenceUi.noClip(), 2.0f);
                    return;
                }
                if (LunariansSettings.defenceSlots.size >= 8) {
                    Vars.ui.showInfoToast("Max slots", 2.0f);
                    return;
                }
                LunariansSettings.defenceSlots.add((Object)clipboard.copy());
                LunariansSettings.save();
                Vars.ui.showInfoToast(AutoDefenceUi.pasted(), 2.0f);
                AutoDefenceUi.this.rebuild();
            }
        }).width(90.0f).height(36.0f).pad(2.0f).get();
        textButton2.getLabel().setFontScale(0.85f);
        textButton2.setDisabled(clipboard == null);
        this.list.add((Element)table3).left().padTop(2.0f).row();
        table3 = new Table();
        table3.add((CharSequence)AutoDefenceUi.maxLabel()).left().padRight(4.0f).fontScale(0.85f);
        table3.button("-", new Runnable(){

            @Override
            public void run() {
                if (defenceSlot.maxUnits > 0) {
                    --defenceSlot.maxUnits;
                    LunariansSettings.save();
                    AutoDefenceUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table3.label((Prov)new Prov<CharSequence>(){

            public CharSequence get() {
                return defenceSlot.maxUnits <= 0 ? AutoDefenceUi.all() : String.valueOf(defenceSlot.maxUnits);
            }
        }).width(40.0f).center().pad(2.0f);
        table3.button("+", new Runnable(){

            @Override
            public void run() {
                if (defenceSlot.maxUnits < 50) {
                    ++defenceSlot.maxUnits;
                    LunariansSettings.save();
                    AutoDefenceUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table.add((Element)table3).left().padTop(2.0f).row();
        textButton2 = new Table();
        textButton2.add((CharSequence)AutoDefenceUi.radLabel()).left().padRight(4.0f).fontScale(0.85f);
        textButton2.button("-", new Runnable(){

            @Override
            public void run() {
                if (defenceSlot.radius > 2.0f) {
                    defenceSlot.radius = Math.max(2.0f, defenceSlot.radius - 1.0f);
                    LunariansSettings.save();
                    AutoDefenceUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        textButton2.label((Prov)new Prov<CharSequence>(){

            public CharSequence get() {
                return (int)defenceSlot.radius + "b";
            }
        }).width(40.0f).center().pad(2.0f);
        textButton2.button("+", new Runnable(){

            @Override
            public void run() {
                if (defenceSlot.radius < 70.0f) {
                    defenceSlot.radius += 1.0f;
                    LunariansSettings.save();
                    AutoDefenceUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table.add((Element)textButton2).left().padTop(2.0f).row();
        Table table4 = new Table();
        boolean bl = defenceSlot.x != Integer.MIN_VALUE;
        String string = bl ? defenceSlot.x + "," + defenceSlot.y : "\u2014";
        table4.add((CharSequence)("Pos: " + string)).wrap().growX().left().fontScale(0.85f).color(bl ? Color.white : Color.lightGray);
        table4.button(AutoDefenceUi.mapBtn(), new Runnable(){

            @Override
            public void run() {
                FeatureDialog featureDialog = FeatureDialog.top;
                if (featureDialog != null) {
                    featureDialog.after(null);
                    featureDialog.hide();
                }
                PositionPicker.pick(new Cons<int[]>(){

                    public void get(int[] nArray) {
                        defenceSlot.x = nArray[0];
                        defenceSlot.y = nArray[1];
                        LunariansSettings.save();
                        AutoDefenceUi.reopen();
                    }
                }, new Runnable(){

                    @Override
                    public void run() {
                        AutoDefenceUi.reopen();
                    }
                });
            }
        }).width(90.0f).height(32.0f).padLeft(4.0f);
        table.add((Element)table4).growX().row();
        return table;
    }

    private static String title() {
        String string = LunariansSettings.language;
        if ("pt".equals(string)) {
            return "Auto defesa";
        }
        if ("es".equals(string)) {
            return "Auto defensa";
        }
        if ("ru".equals(string)) {
            return "\u0410\u0432\u0442\u043e-\u0437\u0430\u0449\u0438\u0442\u0430";
        }
        return "Auto defence";
    }

    private static String hint() {
        String string = LunariansSettings.language;
        if ("pt".equals(string)) {
            return "Unidade + m\u00e1x + posi\u00e7\u00e3o + raio. C\u00edrculo vermelho = zona de ataque.";
        }
        if ("es".equals(string)) {
            return "Unidad + m\u00e1x + posici\u00f3n + radio. C\u00edrculo rojo = zona.";
        }
        return "Unit + max + position + radius. Red circle = attack zone.";
    }

    private static String emptyMsg() {
        String string = LunariansSettings.language;
        if ("pt".equals(string)) {
            return "Nenhuma zona. Aperte +";
        }
        return "No zones. Press +";
    }

    private static String pickUnit() {
        String string = LunariansSettings.language;
        if ("pt".equals(string)) {
            return "Escolher";
        }
        return "Choose";
    }

    private static String maxLabel() {
        String string = LunariansSettings.language;
        if ("pt".equals(string) || "es".equals(string)) {
            return "M\u00e1x:";
        }
        return "Max:";
    }

    private static String radLabel() {
        String string = LunariansSettings.language;
        if ("pt".equals(string)) {
            return "Raio:";
        }
        if ("es".equals(string)) {
            return "Radio:";
        }
        return "Radius:";
    }

    private static String all() {
        String string = LunariansSettings.language;
        if ("pt".equals(string) || "es".equals(string)) {
            return "Todas";
        }
        return "All";
    }

    private static String mapBtn() {
        String string = LunariansSettings.language;
        if ("pt".equals(string) || "es".equals(string)) {
            return "Mapa";
        }
        return "Map";
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

