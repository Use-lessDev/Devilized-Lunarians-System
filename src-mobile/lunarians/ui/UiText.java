package lunarians.ui;

import arc.func.Boolc;
import arc.func.Floatc;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.ui.CheckBox;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import mindustry.graphics.Pal;

public final class UiText {
  public static Cell check(Table paramTable, String paramString, boolean paramBoolean, Boolc paramBoolc) {
    Cell cell = paramTable.check(paramString, paramBoolean, paramBoolc);
    CheckBox checkBox = (CheckBox)cell.get();
    checkBox.getLabel().setWrap(true);
    checkBox.getLabelCell().growX().minWidth(0.0F);
    cell.growX().left().minWidth(0.0F);
    return cell;
  }
  
  public static Cell checkSmall(Table paramTable, String paramString, boolean paramBoolean, Boolc paramBoolc) {
    Cell cell = check(paramTable, paramString, paramBoolean, paramBoolc);
    ((CheckBox)cell.get()).getLabel().setFontScale(0.85F);
    return cell;
  }
  
  public static void section(Table paramTable, String paramString) {
    Cell cell2 = paramTable.add(paramString);
    Color color2 = Pal.accent;
    cell2.color(color2).left().wrap().growX().padTop(8.0F).padBottom(4.0F).row();
    Cell cell1 = paramTable.image();
    Color color1 = Pal.accent;
    cell1.color(color1).height(2.0F).growX().padBottom(6.0F).row();
  }
  
  public static void slider(Table paramTable, String paramString, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, Floatc paramFloatc, Prov paramProv) {
    String str = paramString;
    Table table = new Table();
    this();
    table.add(str).width(130.0F).left().wrap();
    table.slider(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloatc).growX();
    table.label(paramProv).width(56.0F).right().padLeft(6.0F);
    paramTable.add((Element)table).growX().padBottom(2.0F).row();
  }
}
