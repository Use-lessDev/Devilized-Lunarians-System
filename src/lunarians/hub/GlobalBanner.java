package lunarians.hub;

import arc.Core;
import arc.scene.Action;
import arc.scene.Element;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Table;
import java.util.ArrayDeque;
import mindustry.gen.Tex;

public class GlobalBanner {
  private static final ArrayDeque<String> queue = new ArrayDeque<>();
  
  private static boolean showing;
  
  public static void show(String paramString) {
    if (paramString == null)
      return; 
    paramString = paramString.trim();
    if (paramString.isEmpty())
      return; 
    if (queue.size() < 5)
      queue.add(paramString); 
    next();
  }
  
  private static void next() {
    if (showing || queue.isEmpty() || Core.scene == null)
      return; 
    showing = true;
    String str = queue.poll();
    Table table1 = new Table();
    table1.setFillParent(true);
    table1.touchable = Touchable.childrenOnly;
    table1.top();
    Table table2 = new Table(Tex.buttonEdge3);
    table2.margin(8.0F, 14.0F, 8.0F, 14.0F);
    Label label1 = new Label("[accent]" + HubText.get("global.title"));
    label1.setFontScale(0.8F);
    table2.add((Element)label1).left().row();
    Label label2 = new Label(str);
    label2.setWrap(true);
    label2.setAlignment(8);
    float f1 = Math.min(Core.scene.getWidth() - 40.0F, 560.0F);
    table2.add((Element)label2).width(f1).left();
    table1.add((Element)table2).padTop(54.0F);
    table1.color.a = 0.0F;
    float f2 = Math.min(14.0F, 6.0F + str.length() / 25.0F);
    table1.actions(new Action[] { (Action)Actions.fadeIn(0.35F), (Action)Actions.delay(f2), (Action)Actions.fadeOut(0.6F), (Action)Actions.run(() -> close(paramTable)) });
    table2.clicked(() -> close(paramTable));
    Core.scene.add((Element)table1);
  }
  
  private static void close(Table paramTable) {
    if (paramTable.getScene() == null && !showing)
      return; 
    paramTable.remove();
    showing = false;
    next();
  }
}
