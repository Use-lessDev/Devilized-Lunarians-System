package lunarians.ui;

import arc.func.Boolf;
import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Image;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.AutoShield;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import lunarians.data.ShieldSlot;
import mindustry.Vars;
import mindustry.gen.Groups;
import mindustry.gen.Icon;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoShieldUi.class */
public class AutoShieldUi {
    private final Table list = new Table();
    private static ShieldSlot clipboard = null;

    public static BuilderDialog make() {
        return new BuilderDialog(title(), new Cons<Table>() { // from class: lunarians.ui.AutoShieldUi.1
            public void get(Table table) {
                new AutoShieldUi(table);
            }
        }, new Runnable() { // from class: lunarians.ui.AutoShieldUi.2
            @Override // java.lang.Runnable
            public void run() {
                AutoShield.reset();
            }
        });
    }

    private static void reopen() {
        make().after(new Runnable() { // from class: lunarians.ui.AutoShieldUi.3
            @Override // java.lang.Runnable
            public void run() {
                new ConfigDialog().show();
            }
        }).show();
    }

    public AutoShieldUi(Table table) {
        table.defaults().growX();
        table.add(hint()).wrap().growX().color(Color.lightGray).padBottom(4.0f).row();
        table.add(this.list).growX().row();
        rebuild();
    }

    private void rebuild() {
        this.list.clearChildren();
        Seq<ShieldSlot> seq = LunariansSettings.shieldSlots;
        this.list.add(seq.size + "/8").color(Pal.accent).padBottom(4.0f).row();
        if (seq.isEmpty()) {
            this.list.add(emptyMsg()).wrap().growX().padBottom(4.0f).row();
        } else {
            for (int i = 0; i < seq.size; i++) {
                this.list.add(card((ShieldSlot) seq.get(i))).growX().padBottom(3.0f).row();
            }
        }
        if (seq.size < 8) {
            this.list.button(Icon.add, Styles.defaulti, new Runnable() { // from class: lunarians.ui.AutoShieldUi.4
                @Override // java.lang.Runnable
                public void run() {
                    if (LunariansSettings.shieldSlots.size < 8) {
                        LunariansSettings.shieldSlots.add(new ShieldSlot());
                        LunariansSettings.save();
                        AutoShieldUi.this.rebuild();
                    }
                }
            }).size(40.0f).padTop(2.0f).row();
        }
    }

    private Table card(final ShieldSlot shieldSlot) {
        Table table = new Table(Styles.black6);
        table.defaults().growX();
        table.defaults().pad(4.0f);
        Table table2 = new Table();
        UnitType unit = shieldSlot.unit.isEmpty() ? null : Vars.content.unit(shieldSlot.unit);
        ImageButton imageButton = new ImageButton(unit != null ? new TextureRegionDrawable(unit.uiIcon) : Icon.add, Styles.defaulti);
        imageButton.clicked(new Runnable() { // from class: lunarians.ui.AutoShieldUi.5
            @Override // java.lang.Runnable
            public void run() {
                new UnitPickerDialog(new Cons<UnitType>() { // from class: lunarians.ui.AutoShieldUi.5.1
                    public void get(UnitType unitType) {
                        shieldSlot.unit = unitType.name;
                        LunariansSettings.save();
                        Vars.ui.showInfoToast(unitType.localizedName, 2.0f);
                        AutoShieldUi.this.rebuild();
                    }
                }, new Boolf<UnitType>() { // from class: lunarians.ui.AutoShieldUi.5.2
                    public boolean get(UnitType unitType) {
                        return AutoShield.hasShieldAbility(unitType);
                    }
                }).show();
            }
        });
        table2.add(imageButton).size(40.0f).padRight(6.0f);
        table2.add(unit != null ? unit.localizedName : pickShield()).wrap().growX().left().fontScale(0.9f).color(unit != null ? Color.white : Color.lightGray);
        table2.button(copyLabel(), new Runnable() { // from class: lunarians.ui.AutoShieldUi.6
            @Override // java.lang.Runnable
            public void run() {
                AutoShieldUi.clipboard = shieldSlot.copy();
                Vars.ui.showInfoToast(AutoShieldUi.copied(), 2.0f);
                AutoShieldUi.this.rebuild();
            }
        }).width(64.0f).height(32.0f).padLeft(4.0f).get().getLabel().setFontScale(0.8f);
        table2.button(Icon.trash, Styles.defaulti, new Runnable() { // from class: lunarians.ui.AutoShieldUi.7
            @Override // java.lang.Runnable
            public void run() {
                LunariansSettings.shieldSlots.remove(shieldSlot);
                LunariansSettings.save();
                AutoShieldUi.this.rebuild();
            }
        }).size(36.0f).padLeft(4.0f);
        table.add(table2).growX().row();
        Table table3 = new Table();
        TextButton textButton = table3.button(pasteLabel(), new Runnable() { // from class: lunarians.ui.AutoShieldUi.8
            @Override // java.lang.Runnable
            public void run() {
                if (AutoShieldUi.clipboard == null) {
                    Vars.ui.showInfoToast(AutoShieldUi.noClip(), 2.0f);
                    return;
                }
                if (LunariansSettings.shieldSlots.size >= 8) {
                    Vars.ui.showInfoToast("Max slots", 2.0f);
                    return;
                }
                LunariansSettings.shieldSlots.add(AutoShieldUi.clipboard.copy());
                LunariansSettings.save();
                Vars.ui.showInfoToast(AutoShieldUi.pasted(), 2.0f);
                AutoShieldUi.this.rebuild();
            }
        }).width(90.0f).height(36.0f).pad(2.0f).get();
        textButton.getLabel().setFontScale(0.85f);
        textButton.setDisabled(clipboard == null);
        this.list.add(table3).left().padTop(2.0f).row();
        Table table4 = new Table();
        table4.add(maxLabel()).left().padRight(4.0f).fontScale(0.85f);
        table4.button("-", new Runnable() { // from class: lunarians.ui.AutoShieldUi.9
            @Override // java.lang.Runnable
            public void run() {
                if (shieldSlot.maxUnits > 0) {
                    shieldSlot.maxUnits--;
                    LunariansSettings.save();
                    AutoShieldUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table4.label(new Prov<CharSequence>() { // from class: lunarians.ui.AutoShieldUi.10
            /* renamed from: get, reason: merged with bridge method [inline-methods] */
            public CharSequence m56get() {
                return shieldSlot.maxUnits <= 0 ? AutoShieldUi.all() : String.valueOf(shieldSlot.maxUnits);
            }

            /* JADX WARN: Classes with same name are omitted:
              classes.dex
             */
            /* renamed from: lunarians.ui.AutoShieldUi$10$1, reason: invalid class name */
            /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoShieldUi$10$1.class */
            class AnonymousClass1 implements Cons<Unit> {
                AnonymousClass1() {
                }

                public void get(Unit unit) {
                    AnonymousClass10.this.val$shieldSlot.followId = unit.id;
                    AnonymousClass10.this.val$shieldSlot.followLabel = unit.type.localizedName;
                    LunariansSettings.save();
                    Vars.ui.showInfoToast(LunariansI18n.get("shield_following") + " " + unit.type.localizedName + " #" + unit.id, 3.0f);
                    AutoShieldUi.access$200();
                }
            }

            /* JADX WARN: Classes with same name are omitted:
              classes.dex
             */
            /* renamed from: lunarians.ui.AutoShieldUi$10$2, reason: invalid class name */
            /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoShieldUi$10$2.class */
            class AnonymousClass2 implements Runnable {
                AnonymousClass2() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    AutoShieldUi.access$200();
                }
            }
        }).width(40.0f).center().pad(2.0f);
        table4.button("+", new Runnable() { // from class: lunarians.ui.AutoShieldUi.11
            @Override // java.lang.Runnable
            public void run() {
                if (shieldSlot.maxUnits < 50) {
                    shieldSlot.maxUnits++;
                    LunariansSettings.save();
                    AutoShieldUi.this.rebuild();
                }
            }
        }).size(32.0f, 28.0f);
        table.add(table4).left().padTop(2.0f).row();
        Table table5 = new Table();
        table5.add(followLabel()).left().padRight(4.0f).fontScale(0.85f);
        Unit unit2 = shieldSlot.followId >= 0 ? (Unit) Groups.unit.getByID(shieldSlot.followId) : null;
        if (unit2 != null && unit2.isValid()) {
            table5.add(new Image(unit2.type.uiIcon)).size(28.0f).padRight(4.0f);
            table5.add(unit2.type.localizedName + "  #" + shieldSlot.followId).wrap().growX().left().fontScale(0.9f).color(Pal.accent);
        } else if (shieldSlot.followId >= 0) {
            table5.add(((shieldSlot.followLabel == null || shieldSlot.followLabel.isEmpty()) ? "#" + shieldSlot.followId : shieldSlot.followLabel) + "  #" + shieldSlot.followId + " [dead?]").wrap().growX().left().fontScale(0.85f).color(Color.orange);
        } else {
            table5.add(pickFollow()).wrap().growX().left().fontScale(0.85f).color(Color.lightGray);
        }
        table5.button(mapBtn(), new Runnable() { // from class: lunarians.ui.AutoShieldUi.12
            @Override // java.lang.Runnable
            public void run() {
                FeatureDialog featureDialog = FeatureDialog.top;
                if (featureDialog != null) {
                    featureDialog.after(null);
                    featureDialog.hide();
                }
                UnitOnMapPicker.pick(new Cons<Unit>() { // from class: lunarians.ui.AutoShieldUi.12.1
                    public void get(Unit unit3) {
                        shieldSlot.followId = unit3.id;
                        shieldSlot.followLabel = unit3.type.localizedName;
                        LunariansSettings.save();
                        Vars.ui.showInfoToast(("pt".equals(LunariansSettings.language) ? "Seguindo: " : "Following: ") + unit3.type.localizedName + " #" + unit3.id, 3.0f);
                        AutoShieldUi.reopen();
                    }
                }, new Runnable() { // from class: lunarians.ui.AutoShieldUi.12.2
                    @Override // java.lang.Runnable
                    public void run() {
                        AutoShieldUi.reopen();
                    }
                });
            }
        }).width(100.0f).height(32.0f).padLeft(4.0f).get().getLabel().setFontScale(0.85f);
        table.add(table5).growX().padTop(2.0f).row();
        return table;
    }

    private static String title() {
        return "pt".equals(LunariansSettings.language) ? "Auto shield" : "Auto shield";
    }

    private static String hint() {
        return "pt".equals(LunariansSettings.language) ? "1) Unidade shield  2) Máx  3) Mapa = toque na unidade a seguir. Movimento por comando." : "1) Shield unit  2) Max  3) Map = tap unit to follow. Uses unit commands.";
    }

    private static String emptyMsg() {
        return "pt".equals(LunariansSettings.language) ? "Nenhum slot. Aperte +" : "No slots. Press +";
    }

    private static String pickShield() {
        return "pt".equals(LunariansSettings.language) ? "Escolher shield" : "Pick shield unit";
    }

    private static String pickFollow() {
        return "pt".equals(LunariansSettings.language) ? "Nenhuma — toque Mapa" : "None — tap Map";
    }

    private static String followLabel() {
        String str = LunariansSettings.language;
        return ("pt".equals(str) || "es".equals(str)) ? "Seguir:" : "Follow:";
    }

    private static String maxLabel() {
        String str = LunariansSettings.language;
        return ("pt".equals(str) || "es".equals(str)) ? "Máx:" : "Max:";
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
