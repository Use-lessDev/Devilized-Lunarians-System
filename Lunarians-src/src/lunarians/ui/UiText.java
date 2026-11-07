package lunarians.ui;

import arc.func.Boolc;
import arc.func.Floatc;
import arc.func.Prov;
import arc.scene.ui.CheckBox;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import mindustry.graphics.Pal;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/ui/UiText.class */
public final class UiText {
    private UiText() {
    }

    public static Cell<CheckBox> checkSmall(Table table, String str, boolean z, Boolc boolc) {
        Cell<CheckBox> check = check(table, str, z, boolc);
        check.get().getLabel().setFontScale(0.85f);
        return check;
    }

    public static Cell<CheckBox> check(Table table, String str, boolean z, Boolc boolc) {
        Cell<CheckBox> check = table.check(str, z, boolc);
        CheckBox checkBox = check.get();
        checkBox.getLabel().setWrap(true);
        checkBox.getLabelCell().growX().minWidth(0.0f);
        check.growX().left().minWidth(0.0f);
        return check;
    }

    public static void section(Table table, String str) {
        table.add(str).color(Pal.accent).left().wrap().growX().padTop(8.0f).padBottom(4.0f).row();
        table.image().color(Pal.accent).height(2.0f).growX().padBottom(6.0f).row();
    }

    public static void slider(Table table, String str, float f, float f2, float f3, float f4, Floatc floatc, Prov<CharSequence> prov) {
        Table table2 = new Table();
        table2.add(str).width(130.0f).left().wrap();
        table2.slider(f, f2, f3, f4, floatc).growX();
        table2.label(prov).width(56.0f).right().padLeft(6.0f);
        table.add(table2).growX().padBottom(2.0f).row();
    }
}
