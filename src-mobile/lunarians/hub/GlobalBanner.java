package lunarians.hub;

import arc.Core;
import arc.scene.Action;
import arc.scene.Element;
import arc.scene.Scene;
import arc.scene.actions.Actions;
import arc.scene.actions.AlphaAction;
import arc.scene.actions.DelayAction;
import arc.scene.actions.RunnableAction;
import arc.scene.event.Touchable;
import arc.scene.style.Drawable;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Table;
import java.util.ArrayDeque;
import mindustry.gen.Tex;

public class GlobalBanner {
  private static final ArrayDeque queue;
  
  private static boolean showing;
  
  static {
    ArrayDeque arrayDeque = new ArrayDeque();
    this();
    queue = arrayDeque;
  }
  
  private static void close(Table paramTable) {
    Scene scene = paramTable.getScene();
    if (scene == null) {
      boolean bool = showing;
      if (!bool)
        return; 
    } 
    paramTable.remove();
    showing = false;
    next();
  }
  
  private static void next() {
    boolean bool = showing;
    if (!bool) {
      ArrayDeque<String> arrayDeque = queue;
      boolean bool1 = arrayDeque.isEmpty();
      if (!bool1) {
        Scene scene = Core.scene;
        if (scene != null) {
          bool1 = true;
          showing = bool1;
          String str1 = arrayDeque.poll();
          Table table1 = new Table();
          this();
          table1.setFillParent(bool1);
          Touchable touchable = Touchable.childrenOnly;
          table1.touchable = touchable;
          table1.top();
          Table table2 = new Table();
          Drawable drawable = Tex.buttonEdge3;
          this(drawable);
          float f1 = 8.0F;
          float f2 = 14.0F;
          table2.margin(f1, f2, f1, f2);
          Label label = new Label();
          StringBuilder stringBuilder = new StringBuilder();
          this("[accent]");
          String str3 = HubText.get("global.title");
          stringBuilder.append(str3);
          String str2 = stringBuilder.toString();
          this(str2);
          label.setFontScale(0.8F);
          table2.add((Element)label).left().row();
          label = new Label();
          this(str1);
          label.setWrap(bool1);
          label.setAlignment(8);
          float f3 = Core.scene.getWidth() - 40.0F;
          float f4 = 560.0F;
          f3 = Math.min(f3, f4);
          table2.add((Element)label).width(f3).left();
          table1.add((Element)table2).padTop(54.0F);
          table1.color.a = 0.0F;
          float f5 = str1.length() / 25.0F + 6.0F;
          f5 = Math.min(f2, f5);
          byte b2 = 4;
          f1 = 5.6E-45F;
          Action[] arrayOfAction = new Action[b2];
          f2 = 0.35F;
          AlphaAction alphaAction2 = Actions.fadeIn(f2);
          f3 = 0.0F;
          str2 = null;
          arrayOfAction[0] = (Action)alphaAction2;
          DelayAction delayAction = Actions.delay(f5);
          arrayOfAction[bool1] = (Action)delayAction;
          int i = 1058642330;
          f5 = 0.6F;
          AlphaAction alphaAction1 = Actions.fadeOut(f5);
          arrayOfAction[2] = (Action)alphaAction1;
          GlobalBanner$0 globalBanner$0 = new GlobalBanner$0();
          this(table1);
          RunnableAction runnableAction = Actions.run(globalBanner$0);
          byte b1 = 3;
          arrayOfAction[b1] = (Action)runnableAction;
          table1.actions(arrayOfAction);
          GlobalBanner$1 globalBanner$1 = new GlobalBanner$1();
          this(table1);
          table2.clicked(globalBanner$1);
          Scene scene1 = Core.scene;
          scene1.add((Element)table1);
        } 
      } 
    } 
  }
  
  public static void show(String paramString) {
    if (paramString != null) {
      paramString = paramString.trim();
      boolean bool = paramString.isEmpty();
      if (!bool) {
        ArrayDeque<String> arrayDeque = queue;
        int i = arrayDeque.size();
        byte b = 5;
        if (i < b)
          arrayDeque.add(paramString); 
        next();
        return;
      } 
    } 
  }
}
