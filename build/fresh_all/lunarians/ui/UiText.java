/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.func.Boolc
 *  arc.func.Floatc
 *  arc.func.Prov
 *  arc.scene.Element
 *  arc.scene.ui.CheckBox
 *  arc.scene.ui.layout.Cell
 *  arc.scene.ui.layout.Table
 *  mindustry.graphics.Pal
 */
package lunarians.ui;

import arc.func.Boolc;
import arc.func.Floatc;
import arc.func.Prov;
import arc.scene.Element;
import arc.scene.ui.CheckBox;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import mindustry.graphics.Pal;

public final class UiText {
    private UiText() {
    }

    public static Cell<CheckBox> checkSmall(Table table, String string, boolean bl, Boolc boolc) {
        Cell<CheckBox> cell = UiText.check(table, string, bl, boolc);
        ((CheckBox)cell.get()).getLabel().setFontScale(0.85f);
        return cell;
    }

    public static Cell<CheckBox> check(Table table, String string, boolean bl, Boolc boolc) {
        Cell cell = table.check(string, bl, boolc);
        CheckBox checkBox = (CheckBox)cell.get();
        checkBox.getLabel().setWrap(true);
        checkBox.getLabelCell().growX().minWidth(0.0f);
        cell.growX().left().minWidth(0.0f);
        return cell;
    }

    public static void section(Table table, String string) {
        table.add((CharSequence)string).color(Pal.accent).left().wrap().growX().padTop(8.0f).padBottom(4.0f).row();
        table.image().color(Pal.accent).height(2.0f).growX().padBottom(6.0f).row();
    }

    public static void slider(Table table, String string, float f, float f2, float f3, float f4, Floatc floatc, Prov<CharSequence> prov) {
        Table table2 = new Table();
        table2.add((CharSequence)string).width(130.0f).left().wrap();
        table2.slider(f, f2, f3, f4, floatc).growX();
        table2.label(prov).width(56.0f).right().padLeft(6.0f);
        table.add((Element)table2).growX().padBottom(2.0f).row();
    }
}

