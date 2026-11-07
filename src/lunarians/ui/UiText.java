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
  public static Cell<CheckBox> checkSmall(Table paramTable, String paramString, boolean paramBoolean, Boolc paramBoolc) {
    Cell<CheckBox> cell = check(paramTable, paramString, paramBoolean, paramBoolc);
    ((CheckBox)cell.get()).getLabel().setFontScale(0.85F);
    return cell;
  }
  
  public static Cell<CheckBox> check(Table paramTable, String paramString, boolean paramBoolean, Boolc paramBoolc) {
    Cell<CheckBox> cell = paramTable.check(paramString, paramBoolean, paramBoolc);
    CheckBox checkBox = (CheckBox)cell.get();
    checkBox.getLabel().setWrap(true);
    checkBox.getLabelCell().growX().minWidth(0.0F);
    cell.growX().left().minWidth(0.0F);
    return cell;
  }
  
  public static void section(Table paramTable, String paramString) {
    paramTable.add(paramString).color(Pal.accent).left().wrap().growX().padTop(8.0F).padBottom(4.0F).row();
    paramTable.image().color(Pal.accent).height(2.0F).growX().padBottom(6.0F).row();
  }
  
  public static void slider(Table paramTable, String paramString, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, Floatc paramFloatc, Prov<CharSequence> paramProv) {
    Table table = new Table();
    table.add(paramString).width(130.0F).left().wrap();
    table.slider(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloatc).growX();
    table.label(paramProv).width(56.0F).right().padLeft(6.0F);
    paramTable.add((Element)table).growX().padBottom(2.0F).row();
  }
}
