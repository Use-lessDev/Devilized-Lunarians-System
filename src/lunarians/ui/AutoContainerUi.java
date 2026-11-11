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
 *  arc.scene.ui.layout.Table
 *  arc.struct.IntSeq
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
import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import lunarians.AutoContainer;
import lunarians.LunariansSettings;
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

public class AutoContainerUi {
    private final Table box = new Table();

    public static BuilderDialog make() {
        return new BuilderDialog(AutoContainerUi.title(), (Cons<Table>)((Cons)table -> new AutoContainerUi((Table)table)), AutoContainer::reset);
    }

    private static void reopen() {
        AutoContainerUi.make().after(() -> new ConfigDialog().show()).show();
    }

    public static String tr(String string, String string2, String string3, String string4) {
        String string5 = LunariansSettings.language;
        if ("pt".equals(string5)) {
            return string2;
        }
        if ("es".equals(string5)) {
            return string3;
        }
        if ("ru".equals(string5)) {
            return string4;
        }
        return string;
    }

    public static String title() {
        return AutoContainerUi.tr("Auto container (Beta)", "Auto container (Beta)", "Auto contenedor (Beta)", "\u0410\u0432\u0442\u043e-\u043a\u043e\u043d\u0442\u0435\u0439\u043d\u0435\u0440 (\u0411\u0435\u0442\u0430)");
    }

    private static boolean canCarryBlocks(UnitType unitType) {
        return unitType.payloadCapacity > 0.0f && unitType.commands != null && unitType.commands.contains((Object)UnitCommand.loadBlocksCommand) && unitType.commands.contains((Object)UnitCommand.unloadPayloadCommand);
    }

    public AutoContainerUi(Table table) {
        table.defaults().growX();
        table.add((CharSequence)AutoContainerUi.tr("The carrier unit picks up the container at point A, places it at point B, waits, picks it up again, returns it to A and waits again. Then it repeats. Point A must already have the container. Needs the unit in command mode.", "A unidade de carga pega o container no ponto A, coloca no ponto B, espera, pega de novo, devolve em A e espera mais um pouco. Depois repete. O ponto A precisa j\u00e1 ter o container. Precisa da unidade em modo comando.", "La unidad de carga recoge el contenedor en el punto A, lo coloca en el punto B, espera, lo recoge de nuevo, lo devuelve a A y espera otra vez. Luego repite. El punto A ya debe tener el contenedor. Requiere la unidad en modo comando.", "\u042e\u043d\u0438\u0442 \u0431\u0435\u0440\u0451\u0442 \u043a\u043e\u043d\u0442\u0435\u0439\u043d\u0435\u0440 \u0432 \u0442\u043e\u0447\u043a\u0435 A, \u0441\u0442\u0430\u0432\u0438\u0442 \u0432 \u0442\u043e\u0447\u043a\u0435 B, \u0436\u0434\u0451\u0442, \u0431\u0435\u0440\u0451\u0442 \u0441\u043d\u043e\u0432\u0430, \u0432\u043e\u0437\u0432\u0440\u0430\u0449\u0430\u0435\u0442 \u0432 A \u0438 \u0441\u043d\u043e\u0432\u0430 \u0436\u0434\u0451\u0442. \u0417\u0430\u0442\u0435\u043c \u043f\u043e\u0432\u0442\u043e\u0440. \u0412 \u0442\u043e\u0447\u043a\u0435 A \u0443\u0436\u0435 \u0434\u043e\u043b\u0436\u0435\u043d \u0441\u0442\u043e\u044f\u0442\u044c \u043a\u043e\u043d\u0442\u0435\u0439\u043d\u0435\u0440. \u041d\u0443\u0436\u0435\u043d \u0440\u0435\u0436\u0438\u043c \u043a\u043e\u043c\u0430\u043d\u0434\u043e\u0432\u0430\u043d\u0438\u044f.")).wrap().growX().color(Color.lightGray).padBottom(6.0f).row();
        table.add((Element)this.box).growX().row();
        this.rebuild();
    }

    private void rebuild() {
        this.box.clearChildren();
        this.box.defaults().growX();
        UnitType unitType = LunariansSettings.containerUnit.isEmpty() ? null : Vars.content.unit(LunariansSettings.containerUnit);
        Table table = new Table(Styles.black6);
        table.defaults().pad(4.0f);
        ImageButton imageButton = new ImageButton((Drawable)(unitType != null ? new TextureRegionDrawable(unitType.uiIcon) : Icon.add), Styles.defaulti);
        imageButton.clicked(() -> new UnitPickerDialog((Cons<UnitType>)((Cons)unitType -> {
            LunariansSettings.containerUnit = unitType.name;
            LunariansSettings.save();
            AutoContainer.reset();
            this.rebuild();
        }), (Boolf<UnitType>)((Boolf)AutoContainerUi::canCarryBlocks)).show());
        table.add((Element)imageButton).size(40.0f).padRight(6.0f);
        table.add((CharSequence)(unitType != null ? unitType.localizedName : AutoContainerUi.tr("Choose carrier unit", "Escolher unidade de carga", "Elegir unidad de carga", "\u0412\u044b\u0431\u0440\u0430\u0442\u044c \u044e\u043d\u0438\u0442"))).wrap().growX().left();
        table.button((Drawable)Icon.trash, Styles.defaulti, () -> {
            LunariansSettings.containerUnit = "";
            LunariansSettings.save();
            AutoContainer.reset();
            this.rebuild();
        }).size(36.0f);
        this.box.add((Element)table).growX().padBottom(4.0f).row();
        this.stepper(AutoContainerUi.tr("Max units:", "M\u00e1x unidades:", "M\u00e1x unidades:", "\u041c\u0430\u043a\u0441. \u044e\u043d\u0438\u0442\u043e\u0432:"), (Prov<String>)((Prov)() -> LunariansSettings.containerMaxUnits <= 0 ? AutoContainerUi.tr("All", "Todas", "Todas", "\u0412\u0441\u0435") : String.valueOf(LunariansSettings.containerMaxUnits)), () -> {
            if (LunariansSettings.containerMaxUnits > 0) {
                --LunariansSettings.containerMaxUnits;
            }
        }, () -> {
            if (LunariansSettings.containerMaxUnits < 10) {
                ++LunariansSettings.containerMaxUnits;
            }
        });
        boolean bl = AutoContainer.hasPoints();
        String string = !bl ? AutoContainerUi.tr("Points not set. Press \"Map points\": 1st = where the container is (A), 2nd = where it will stay (B).", "Pontos n\u00e3o definidos. Aperte \"Pontos mapa\": 1\u00ba = onde est\u00e1 o container (A), 2\u00ba = onde ele vai ficar (B).", "Puntos sin definir. Pulsa \"Puntos mapa\": 1.\u00ba = donde est\u00e1 el contenedor (A), 2.\u00ba = donde se quedar\u00e1 (B).", "\u0422\u043e\u0447\u043a\u0438 \u043d\u0435 \u0437\u0430\u0434\u0430\u043d\u044b. \u041d\u0430\u0436\u043c\u0438\u0442\u0435 \u00ab\u0422\u043e\u0447\u043a\u0438\u00bb: 1-\u044f = \u0433\u0434\u0435 \u043a\u043e\u043d\u0442\u0435\u0439\u043d\u0435\u0440 (A), 2-\u044f = \u0433\u0434\u0435 \u043e\u043d \u043e\u0441\u0442\u0430\u043d\u0435\u0442\u0441\u044f (B).") : "A (" + LunariansSettings.containerPickX + "," + LunariansSettings.containerPickY + ")  \u2192  B (" + LunariansSettings.containerDropX + "," + LunariansSettings.containerDropY + ")";
        this.box.add((CharSequence)string).wrap().growX().left().color(bl ? Color.white : Color.lightGray).padTop(4.0f).row();
        Table table2 = new Table();
        table2.button(AutoContainerUi.tr("Map points", "Pontos mapa", "Puntos mapa", "\u0422\u043e\u0447\u043a\u0438"), () -> {
            FeatureDialog featureDialog = FeatureDialog.top;
            if (featureDialog != null) {
                featureDialog.after(null);
                featureDialog.hide();
            }
            PathPicker.pick((Cons<IntSeq>)((Cons)intSeq -> {
                if (intSeq.size >= 4) {
                    AutoContainer.setPoints(intSeq.get(0), intSeq.get(1), intSeq.get(intSeq.size - 2), intSeq.get(intSeq.size - 1));
                }
                AutoContainerUi.reopen();
            }), AutoContainerUi::reopen);
        }).width(130.0f).height(36.0f);
        if (bl) {
            table2.button((Drawable)Icon.trash, Styles.defaulti, () -> {
                AutoContainer.clearPoints();
                this.rebuild();
            }).size(36.0f).padLeft(4.0f);
        }
        this.box.add((Element)table2).left().padTop(2.0f).padBottom(4.0f).row();
        this.stepper(AutoContainerUi.tr("Wait after placing (s):", "Espera ap\u00f3s colocar (s):", "Espera tras colocar (s):", "\u041e\u0436\u0438\u0434\u0430\u043d\u0438\u0435 \u043f\u043e\u0441\u043b\u0435 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0438 (\u0441):"), (Prov<String>)((Prov)() -> AutoContainerUi.fmt(AutoContainer.waitAfterPlace())), () -> {
            LunariansSettings.containerWaitSec = Math.max(0.0f, LunariansSettings.containerWaitSec - 1.0f);
        }, () -> {
            LunariansSettings.containerWaitSec = Math.min(300.0f, LunariansSettings.containerWaitSec + 1.0f);
        });
        this.stepper(AutoContainerUi.tr("Wait after returning (s):", "Espera ap\u00f3s devolver (s):", "Espera tras devolver (s):", "\u041e\u0436\u0438\u0434\u0430\u043d\u0438\u0435 \u043f\u043e\u0441\u043b\u0435 \u0432\u043e\u0437\u0432\u0440\u0430\u0442\u0430 (\u0441):"), (Prov<String>)((Prov)() -> AutoContainerUi.fmt(AutoContainer.waitAfterReturn())), () -> AutoContainer.setWaitAfterReturn(Math.max(0.0f, AutoContainer.waitAfterReturn() - 1.0f)), () -> AutoContainer.setWaitAfterReturn(Math.min(300.0f, AutoContainer.waitAfterReturn() + 1.0f)));
    }

    private static String fmt(float f) {
        return f == (float)((int)f) ? String.valueOf((int)f) : String.valueOf(f);
    }

    private void stepper(String string, Prov<String> prov, Runnable runnable, Runnable runnable2) {
        Table table = new Table();
        table.add((CharSequence)string).left().padRight(6.0f).wrap().width(190.0f).color(Pal.accent).fontScale(0.9f);
        table.button("-", () -> {
            runnable.run();
            LunariansSettings.save();
            this.rebuild();
        }).size(36.0f, 32.0f);
        table.add((CharSequence)prov.get()).width(50.0f).center().pad(2.0f);
        table.button("+", () -> {
            runnable2.run();
            LunariansSettings.save();
            this.rebuild();
        }).size(36.0f, 32.0f);
        this.box.add((Element)table).left().padTop(3.0f).row();
    }
}

