package lunarians.ui;

import arc.func.Boolf;
import arc.func.Cons;
import arc.graphics.Color;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import lunarians.AutoMoon;
import lunarians.LunariansSettings;
import lunarians.data.MoonSlot;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.gen.Groups;
import mindustry.gen.Icon;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Styles;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoMoonUi.class */
public class AutoMoonUi {
    private final Table list = new Table();
    private static MoonSlot clipboard = null;

    public static BuilderDialog make() {
        return new BuilderDialog(title(), new Cons<Table>() { // from class: lunarians.ui.AutoMoonUi.1
            public void get(Table table) {
                new AutoMoonUi(table);
            }
        }, new Runnable() { // from class: lunarians.ui.AutoMoonUi.2
            @Override // java.lang.Runnable
            public void run() {
                AutoMoon.reset();
            }
        });
    }

    private static void reopen() {
        make().after(new Runnable() { // from class: lunarians.ui.AutoMoonUi.3
            @Override // java.lang.Runnable
            public void run() {
                new ConfigDialog().show();
            }
        }).show();
    }

    public AutoMoonUi(Table table) {
        table.defaults().growX();
        table.add(hint()).wrap().growX().color(Color.lightGray).padBottom(4.0f).row();
        table.add(this.list).growX().row();
        rebuild();
    }

    private void rebuild() {
        this.list.clearChildren();
        Seq<MoonSlot> seq = LunariansSettings.moonSlots;
        this.list.add(seq.size + "/8").color(Pal.accent).padBottom(4.0f).row();
        if (seq.isEmpty()) {
            this.list.add(empty()).wrap().growX().row();
        } else {
            for (int i = 0; i < seq.size; i++) {
                this.list.add(card((MoonSlot) seq.get(i))).growX().padBottom(3.0f).row();
            }
        }
        if (seq.size < 8) {
            this.list.button(Icon.add, Styles.defaulti, new Runnable() { // from class: lunarians.ui.AutoMoonUi.4
                @Override // java.lang.Runnable
                public void run() {
                    LunariansSettings.moonSlots.add(new MoonSlot());
                    LunariansSettings.save();
                    AutoMoonUi.this.rebuild();
                }
            }).size(40.0f).padTop(2.0f).row();
        }
    }

    private Table card(final MoonSlot moonSlot) {
        String followPlayer;
        Table table = new Table(Styles.black6);
        table.defaults().pad(4.0f).growX();
        Table table2 = new Table();
        UnitType unit = moonSlot.unit.isEmpty() ? null : Vars.content.unit(moonSlot.unit);
        ImageButton imageButton = new ImageButton(unit != null ? new TextureRegionDrawable(unit.uiIcon) : Icon.add, Styles.defaulti);
        imageButton.clicked(new Runnable() { // from class: lunarians.ui.AutoMoonUi.5
            @Override // java.lang.Runnable
            public void run() {
                new UnitPickerDialog(new Cons<UnitType>() { // from class: lunarians.ui.AutoMoonUi.5.1
                    public void get(UnitType unitType) {
                        moonSlot.unit = unitType.name;
                        LunariansSettings.save();
                        AutoMoonUi.this.rebuild();
                    }
                }, new Boolf<UnitType>() { // from class: lunarians.ui.AutoMoonUi.5.2
                    public boolean get(UnitType unitType) {
                        return unitType.flying;
                    }
                }).show();
            }
        });
        table2.add(imageButton).size(40.0f).padRight(6.0f);
        table2.add(unit != null ? unit.localizedName : pick()).wrap().growX().left().fontScale(0.9f);
        table2.button(copyLabel(), new Runnable() { // from class: lunarians.ui.AutoMoonUi.6
            @Override // java.lang.Runnable
            public void run() {
                AutoMoonUi.clipboard = moonSlot.copy();
                Vars.ui.showInfoToast(AutoMoonUi.copied(), 2.0f);
                AutoMoonUi.this.rebuild();
            }
        }).width(64.0f).height(32.0f).padLeft(4.0f).get().getLabel().setFontScale(0.8f);
        table2.button(Icon.trash, Styles.defaulti, new Runnable() { // from class: lunarians.ui.AutoMoonUi.7
            @Override // java.lang.Runnable
            public void run() {
                LunariansSettings.moonSlots.remove(moonSlot);
                LunariansSettings.save();
                AutoMoonUi.this.rebuild();
            }
        }).size(36.0f);
        table.add(table2).growX().row();
        Table table3 = new Table();
        TextButton textButton = table3.button(pasteLabel(), new Runnable() { // from class: lunarians.ui.AutoMoonUi.8
            @Override // java.lang.Runnable
            public void run() {
                if (AutoMoonUi.clipboard == null) {
                    Vars.ui.showInfoToast(AutoMoonUi.noClip(), 2.0f);
                    return;
                }
                if (LunariansSettings.moonSlots.size >= 8) {
                    Vars.ui.showInfoToast("Max slots", 2.0f);
                    return;
                }
                LunariansSettings.moonSlots.add(AutoMoonUi.clipboard.copy());
                LunariansSettings.save();
                Vars.ui.showInfoToast(AutoMoonUi.pasted(), 2.0f);
                AutoMoonUi.this.rebuild();
            }
        }).width(90.0f).height(36.0f).pad(2.0f).get();
        textButton.getLabel().setFontScale(0.85f);
        textButton.setDisabled(clipboard == null);
        this.list.add(table3).left().padTop(2.0f).row();
        rowStepper(table, maxL(), moonSlot.maxUnits <= 0 ? all() : String.valueOf(moonSlot.maxUnits), new Runnable() { // from class: lunarians.ui.AutoMoonUi.9
            @Override // java.lang.Runnable
            public void run() {
                if (moonSlot.maxUnits > 0) {
                    moonSlot.maxUnits--;
                    LunariansSettings.save();
                    AutoMoonUi.this.rebuild();
                }
            }
        }, new Runnable() { // from class: lunarians.ui.AutoMoonUi.10
            @Override // java.lang.Runnable
            public void run() {
                if (moonSlot.maxUnits < 50) {
                    moonSlot.maxUnits++;
                    LunariansSettings.save();
                    AutoMoonUi.this.rebuild();
                }
            }
        });
        rowStepper(table, radL(), ((int) moonSlot.radius) + "b", new Runnable() { // from class: lunarians.ui.AutoMoonUi.11
            @Override // java.lang.Runnable
            public void run() {
                if (moonSlot.radius > 1.0f) {
                    moonSlot.radius = Math.max(1.0f, moonSlot.radius - 1.0f);
                    LunariansSettings.save();
                    AutoMoonUi.this.rebuild();
                }
            }
        }, new Runnable() { // from class: lunarians.ui.AutoMoonUi.12
            @Override // java.lang.Runnable
            public void run() {
                if (moonSlot.radius < 40.0f) {
                    moonSlot.radius += 1.0f;
                    LunariansSettings.save();
                    AutoMoonUi.this.rebuild();
                }
            }
        });
        rowStepper(table, speedL(), String.format("%.2fx", Float.valueOf(moonSlot.speed)), new Runnable() { // from class: lunarians.ui.AutoMoonUi.13
            @Override // java.lang.Runnable
            public void run() {
                moonSlot.speed = Math.max(0.25f, moonSlot.speed - 0.25f);
                LunariansSettings.save();
                AutoMoonUi.this.rebuild();
            }

            /* JADX WARN: Classes with same name are omitted:
              classes.dex
             */
            /* renamed from: lunarians.ui.AutoMoonUi$13$1, reason: invalid class name */
            /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoMoonUi$13$1.class */
            class AnonymousClass1 implements Cons<Unit> {
                AnonymousClass1() {
                }

                public void get(Unit unit) {
                    String str;
                    moonSlot.followId = unit.id;
                    moonSlot.followLabel = unit.type != null ? unit.type.localizedName : "";
                    LunariansSettings.save();
                    UI ui = Vars.ui;
                    if ("pt".equals(LunariansSettings.language)) {
                        str = "Órbita: " + moonSlot.followLabel + " #" + moonSlot.followId;
                    } else {
                        str = "Orbit: " + moonSlot.followLabel + " #" + moonSlot.followId;
                    }
                    ui.showInfoToast(str, 2.0f);
                    AutoMoonUi.reopen();
                }
            }

            /* JADX WARN: Classes with same name are omitted:
              classes.dex
             */
            /* renamed from: lunarians.ui.AutoMoonUi$13$2, reason: invalid class name */
            /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoMoonUi$13$2.class */
            class AnonymousClass2 implements Runnable {
                AnonymousClass2() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    AutoMoonUi.reopen();
                }
            }
        }, new Runnable() { // from class: lunarians.ui.AutoMoonUi.14
            @Override // java.lang.Runnable
            public void run() {
                moonSlot.speed = Math.min(4.0f, moonSlot.speed + 0.25f);
                LunariansSettings.save();
                AutoMoonUi.this.rebuild();
            }

            /* JADX WARN: Classes with same name are omitted:
              classes.dex
             */
            /* renamed from: lunarians.ui.AutoMoonUi$14$1, reason: invalid class name */
            /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoMoonUi$14$1.class */
            class AnonymousClass1 implements Cons<int[]> {
                AnonymousClass1() {
                }

                public void get(int[] iArr) {
                    moonSlot.x = iArr[0];
                    moonSlot.y = iArr[1];
                    LunariansSettings.save();
                    AutoMoonUi.reopen();
                }
            }

            /* JADX WARN: Classes with same name are omitted:
              classes.dex
             */
            /* renamed from: lunarians.ui.AutoMoonUi$14$2, reason: invalid class name */
            /* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/AutoMoonUi$14$2.class */
            class AnonymousClass2 implements Runnable {
                AnonymousClass2() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    AutoMoonUi.reopen();
                }
            }
        });
        Table table4 = new Table();
        if (moonSlot.hasUnitCenter()) {
            Unit byID = Groups.unit.getByID(moonSlot.followId);
            if (byID != null && byID.isValid()) {
                followPlayer = byID.type.localizedName + " #" + moonSlot.followId;
            } else if (moonSlot.followLabel != null && !moonSlot.followLabel.isEmpty()) {
                followPlayer = moonSlot.followLabel + " #" + moonSlot.followId;
            } else {
                followPlayer = "#" + moonSlot.followId;
            }
        } else if (moonSlot.hasTileCenter()) {
            followPlayer = "tile " + moonSlot.x + "," + moonSlot.y;
        } else {
            followPlayer = followPlayer();
        }
        table4.add(centerL() + " " + followPlayer).wrap().growX().left().fontScale(0.85f).color((moonSlot.hasUnitCenter() || moonSlot.hasTileCenter()) ? Color.white : Color.lightGray);
        table.add(table4).growX().row();
        Table table5 = new Table();
        table5.button(unitMapBtn(), new Runnable() { // from class: lunarians.ui.AutoMoonUi.15
            @Override // java.lang.Runnable
            public void run() {
                FeatureDialog featureDialog = FeatureDialog.top;
                if (featureDialog != null) {
                    featureDialog.after(null);
                    featureDialog.hide();
                }
                UnitOnMapPicker.pick(new Cons<Unit>() { // from class: lunarians.ui.AutoMoonUi.15.1
                    public void get(Unit unit2) {
                        String str;
                        moonSlot.followId = unit2.id;
                        moonSlot.followLabel = unit2.type != null ? unit2.type.localizedName : "";
                        LunariansSettings.save();
                        UI ui = Vars.ui;
                        if ("pt".equals(LunariansSettings.language)) {
                            str = "Órbita: " + moonSlot.followLabel + " #" + moonSlot.followId;
                        } else {
                            str = "Orbit: " + moonSlot.followLabel + " #" + moonSlot.followId;
                        }
                        ui.showInfoToast(str, 2.0f);
                        AutoMoonUi.reopen();
                    }
                }, new Runnable() { // from class: lunarians.ui.AutoMoonUi.15.2
                    @Override // java.lang.Runnable
                    public void run() {
                        AutoMoonUi.reopen();
                    }
                });
            }
        }).width(110.0f).height(32.0f).get().getLabel().setFontScale(0.8f);
        table5.button(mapBtn(), new Runnable() { // from class: lunarians.ui.AutoMoonUi.16
            @Override // java.lang.Runnable
            public void run() {
                FeatureDialog featureDialog = FeatureDialog.top;
                if (featureDialog != null) {
                    featureDialog.after(null);
                    featureDialog.hide();
                }
                PositionPicker.pick(new Cons<int[]>() { // from class: lunarians.ui.AutoMoonUi.16.1
                    public void get(int[] iArr) {
                        moonSlot.x = iArr[0];
                        moonSlot.y = iArr[1];
                        LunariansSettings.save();
                        AutoMoonUi.reopen();
                    }
                }, new Runnable() { // from class: lunarians.ui.AutoMoonUi.16.2
                    @Override // java.lang.Runnable
                    public void run() {
                        AutoMoonUi.reopen();
                    }
                });
            }
        }).width(70.0f).height(32.0f).padLeft(4.0f).get().getLabel().setFontScale(0.8f);
        table5.button(Icon.trash, Styles.defaulti, new Runnable() { // from class: lunarians.ui.AutoMoonUi.17
            @Override // java.lang.Runnable
            public void run() {
                moonSlot.followId = -1;
                moonSlot.followLabel = "";
                moonSlot.x = Integer.MIN_VALUE;
                moonSlot.y = Integer.MIN_VALUE;
                LunariansSettings.save();
                AutoMoonUi.this.rebuild();
            }
        }).size(32.0f).padLeft(4.0f);
        table.add(table5).left().row();
        return table;
    }

    private void rowStepper(Table table, String str, String str2, Runnable runnable, Runnable runnable2) {
        Table table2 = new Table();
        table2.add(str).left().padRight(4.0f).fontScale(0.85f);
        table2.button("-", runnable).size(32.0f, 28.0f);
        table2.add(str2).width(48.0f).center().pad(2.0f);
        table2.button("+", runnable2).size(32.0f, 28.0f);
        table.add(table2).left().row();
    }

    private static String title() {
        return "Auto Moon";
    }

    private static String hint() {
        if ("pt".equals(LunariansSettings.language)) {
            return "Voadoras orbitam jogador / tile / unidade no mapa. Velocidade ajustável. Atacam perto.";
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
        return "pt".equals(LunariansSettings.language) ? "Máx:" : "Max:";
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
