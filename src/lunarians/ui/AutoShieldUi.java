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
 *  arc.scene.ui.Image
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
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Image;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.AutoShield;
import lunarians.LunariansSettings;
import lunarians.data.ShieldSlot;
import lunarians.ui.BuilderDialog;
import lunarians.ui.ConfigDialog;
import lunarians.ui.FeatureDialog;
import lunarians.ui.UnitOnMapPicker;
import lunarians.ui.UnitPickerDialog;
import mindustry.Vars;
import mindustry.gen.Groups;
import mindustry.gen.Icon;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

public class AutoShieldUi {
    private final Table list = new Table();
    private static ShieldSlot clipboard = null;

    public static BuilderDialog make() {
        return new BuilderDialog(AutoShieldUi.title(), new Cons<Table>(){

            public void get(Table table) {
                new AutoShieldUi(table);
            }
        }, new Runnable(){

            @Override
            public void run() {
                AutoShield.reset();
            }
        });
    }

    private static void reopen() {
        AutoShieldUi.make().after(new Runnable(){

            @Override
            public void run() {
                new ConfigDialog().show();
            }
        }).show();
    }

    public AutoShieldUi(Table table) {
        table.defaults().growX();
        table.add((CharSequence)AutoShieldUi.hint()).wrap().growX().color(Color.lightGray).padBottom(4.0f).row();
        table.add((Element)this.list).growX().row();
        this.rebuild();
    }

    private void rebuild() {
        this.list.clearChildren();
        Seq<ShieldSlot> seq = LunariansSettings.shieldSlots;
        this.list.add((CharSequence)(seq.size + "/8")).color(Pal.accent).padBottom(4.0f).row();
        if (seq.isEmpty()) {
            this.list.add((CharSequence)AutoShieldUi.emptyMsg()).wrap().growX().padBottom(4.0f).row();
        } else {
            for (int i = 0; i < seq.size; ++i) {
                this.list.add((Element)this.card((ShieldSlot)seq.get(i))).growX().padBottom(3.0f).row();
            }
        }
        if (seq.size < 8) {
            this.list.button((Drawable)Icon.add, Styles.defaulti, new Runnable(){

                @Override
                public void run() {
                    if (LunariansSettings.shieldSlots.size < 8) {
                        LunariansSettings.shieldSlots.add((Object)new ShieldSlot());
                        LunariansSettings.save();
                        AutoShieldUi.this.rebuild();
                    }
                }
            }).size(40.0f).padTop(2.0f).row();
        }
    }

    private Table card(final ShieldSlot shieldSlot) {
        Object object;
        Unit unit;
        Table table = new Table(Styles.black6);
        table.defaults().growX();
        table.defaults().pad(4.0f);
        Table table2 = new Table();
        UnitType unitType = shieldSlot.unit.isEmpty() ? null : Vars.content.unit(shieldSlot.unit);
        ImageButton imageButton = new ImageButton((Drawable)(unitType != null ? new TextureRegionDrawable(unitType.uiIcon) : Icon.add), Styles.defaulti);
        imageButton.clicked(new Runnable(){

            @Override
            public void run() {
                new UnitPickerDialog(new Cons<UnitType>(){

                    public void get(UnitType unitType) {
                        shieldSlot.unit = unitType.name;
                        LunariansSettings.save();
                        Vars.ui.showInfoToast(unitType.localizedName, 2.0f);
                        AutoShieldUi.this.rebuild();
                    }
                }, new Boolf<UnitType>(){

                    public boolean get(UnitType unitType) {
                        return AutoShield.hasShieldAbility(unitType);
                    }
                }).show();
            }
        });
        table2.add((Element)imageButton).size(40.0f).padRight(6.0f);
        table2.add((CharSequence)(unitType != null ? unitType.localizedName : AutoShieldUi.pickShield())).wrap().growX().left().fontScale(0.9f).color(unitType != null ? Color.white : Color.lightGray);
        TextButton textButton = (TextButton)table2.button(AutoShieldUi.copyLabel(), new Runnable(){

            @Override
            public void run() {
                clipboard = shieldSlot.copy();
                Vars.ui.showInfoToast(AutoShieldUi.copied(), 2.0f);
                AutoShieldUi.this.rebuild();
            }
        }).width(64.0f).height(32.0f).padLeft(4.0f).get();
        textButton.getLabel().setFontScale(0.8f);
        table2.button((Drawable)Icon.trash, Styles.defaulti, new Runnable(){

            @Override
            public void run() {
                LunariansSettings.shieldSlots.remove((Object)shieldSlot);
                LunariansSettings.save();
                AutoShieldUi.this.rebuild();
            }
        }).size(36.0f).padLeft(4.0f);
        table.add((Element)table2).growX().row();
        Table table3 = new Table();
        TextButton textButton2 = (TextButton)table3.button(AutoShieldUi.pasteLabel(), new Runnable(){

            @Override
            public void run() {
                if (clipboard == null) {
                    Vars.ui.showInfoToast(AutoShieldUi.noClip(), 2.0f);
                    return;
                }
                if (LunariansSettings.shieldSlots.size >= 8) {
                    Vars.ui.showInfoToast("Max slots", 2.0f);
                    return;
                }
                LunariansSettings.shieldSlots.add((Object)clipboard.copy());
                LunariansSettings.save();
                Vars.ui.showInfoToast(AutoShieldUi.pasted(), 2.0f);
                AutoShieldUi.this.rebuild();
            }
        }).width(90.0f).height(36.0f).pad(2.0f).get();
        textButton2.getLabel().setFontScale(0.85f);
        textButton2.setDisabled(clipboard == null);
        this.list.add((Element)table3).left().padTop(2.0f).row();
        table3 = new Table();
        table3.add((CharSequence)AutoShieldUi.maxLabel()).left().padRight(4.0f).fontScale(0.85f);
        table3.button("-", new Runnable(){

            @Override
            public void run() {
                if (shieldSlot.maxUnits > 0) {
                    --shieldSlot.maxUnits;
                    LunariansSettings.save();
                    AutoShieldUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table3.label((Prov)new Prov<CharSequence>(){

            public CharSequence get() {
                return shieldSlot.maxUnits <= 0 ? AutoShieldUi.all() : String.valueOf(shieldSlot.maxUnits);
            }
        }).width(40.0f).center().pad(2.0f);
        table3.button("+", new Runnable(){

            @Override
            public void run() {
                if (shieldSlot.maxUnits < 50) {
                    ++shieldSlot.maxUnits;
                    LunariansSettings.save();
                    AutoShieldUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table.add((Element)table3).left().padTop(2.0f).row();
        textButton2 = new Table();
        textButton2.add((CharSequence)AutoShieldUi.followLabel()).left().padRight(4.0f).fontScale(0.85f);
        Unit unit2 = unit = shieldSlot.followId >= 0 ? (Unit)Groups.unit.getByID(shieldSlot.followId) : null;
        if (unit != null && unit.isValid()) {
            object = new Image(unit.type.uiIcon);
            textButton2.add((Element)object).size(28.0f).padRight(4.0f);
            textButton2.add((CharSequence)(unit.type.localizedName + "  #" + shieldSlot.followId)).wrap().growX().left().fontScale(0.9f).color(Pal.accent);
        } else if (shieldSlot.followId >= 0) {
            object = shieldSlot.followLabel != null && !shieldSlot.followLabel.isEmpty() ? shieldSlot.followLabel : "#" + shieldSlot.followId;
            textButton2.add((CharSequence)((String)object + "  #" + shieldSlot.followId + " [dead?]")).wrap().growX().left().fontScale(0.85f).color(Color.orange);
        } else {
            textButton2.add((CharSequence)AutoShieldUi.pickFollow()).wrap().growX().left().fontScale(0.85f).color(Color.lightGray);
        }
        object = (TextButton)textButton2.button(AutoShieldUi.mapBtn(), new Runnable(){

            @Override
            public void run() {
                FeatureDialog featureDialog = FeatureDialog.top;
                if (featureDialog != null) {
                    featureDialog.after(null);
                    featureDialog.hide();
                }
                UnitOnMapPicker.pick(new Cons<Unit>(){

                    public void get(Unit unit) {
                        shieldSlot.followId = unit.id;
                        shieldSlot.followLabel = unit.type.localizedName;
                        LunariansSettings.save();
                        Vars.ui.showInfoToast(("pt".equals(LunariansSettings.language) ? "Seguindo: " : "Following: ") + unit.type.localizedName + " #" + unit.id, 3.0f);
                        AutoShieldUi.reopen();
                    }
                }, new Runnable(){

                    @Override
                    public void run() {
                        AutoShieldUi.reopen();
                    }
                });
            }
        }).width(100.0f).height(32.0f).padLeft(4.0f).get();
        object.getLabel().setFontScale(0.85f);
        table.add((Element)textButton2).growX().padTop(2.0f).row();
        return table;
    }

    private static String title() {
        String string = LunariansSettings.language;
        if ("pt".equals(string)) {
            return "Auto shield";
        }
        return "Auto shield";
    }

    private static String hint() {
        String string = LunariansSettings.language;
        if ("pt".equals(string)) {
            return "1) Unidade shield  2) M\u00e1x  3) Mapa = toque na unidade a seguir. Movimento por comando.";
        }
        return "1) Shield unit  2) Max  3) Map = tap unit to follow. Uses unit commands.";
    }

    private static String emptyMsg() {
        String string = LunariansSettings.language;
        if ("pt".equals(string)) {
            return "Nenhum slot. Aperte +";
        }
        return "No slots. Press +";
    }

    private static String pickShield() {
        String string = LunariansSettings.language;
        if ("pt".equals(string)) {
            return "Escolher shield";
        }
        return "Pick shield unit";
    }

    private static String pickFollow() {
        String string = LunariansSettings.language;
        if ("pt".equals(string)) {
            return "Nenhuma \u2014 toque Mapa";
        }
        return "None \u2014 tap Map";
    }

    private static String followLabel() {
        String string = LunariansSettings.language;
        if ("pt".equals(string) || "es".equals(string)) {
            return "Seguir:";
        }
        return "Follow:";
    }

    private static String maxLabel() {
        String string = LunariansSettings.language;
        if ("pt".equals(string) || "es".equals(string)) {
            return "M\u00e1x:";
        }
        return "Max:";
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

