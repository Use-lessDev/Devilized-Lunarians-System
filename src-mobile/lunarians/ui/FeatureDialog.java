package lunarians.ui;

import arc.Core;
import arc.scene.Element;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import lunarians.LunariansI18n;
import mindustry.ui.dialogs.BaseDialog;

public abstract class FeatureDialog extends BaseDialog {
  public static FeatureDialog top;
  
  private Runnable after;
  
  protected final Table body;
  
  protected FeatureDialog(String paramString) {
    super(paramString);
    Table table2 = new Table();
    this();
    this.body = table2;
    Cell cell2 = table2.defaults().growX();
    float f1 = 4.0F;
    cell2.padBottom(f1);
    Cell cell1 = this.cont.pane((Element)table2).grow();
    float f2 = Core.graphics.getHeight() * 0.78F;
    cell1 = cell1.maxHeight(f2);
    f2 = Math.min(Core.graphics.getWidth() * 0.96F, 900.0F);
    cell1.width(f2);
    this.buttons.defaults().size(150.0F, 48.0F).pad(f1);
    Table table1 = this.buttons;
    String str = LunariansI18n.get("back");
    FeatureDialog$1 featureDialog$1 = new FeatureDialog$1();
    this(this);
    table1.button(str, featureDialog$1);
    FeatureDialog$2 featureDialog$2 = new FeatureDialog$2();
    this(this);
    shown(featureDialog$2);
    closeOnBack();
    FeatureDialog$3 featureDialog$3 = new FeatureDialog$3();
    this(this);
    hidden(featureDialog$3);
  }
  
  public FeatureDialog after(Runnable paramRunnable) {
    this.after = paramRunnable;
    return this;
  }
  
  protected void onClosed() {}
}
