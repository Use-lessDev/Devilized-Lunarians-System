package lunarians.ui;

import arc.Core;
import arc.scene.Element;
import arc.scene.ui.layout.Table;
import lunarians.LunariansI18n;
import lunarians.LunariansSettings;
import mindustry.ui.dialogs.BaseDialog;

public abstract class FeatureDialog extends BaseDialog {
  protected final Table body = new Table();
  
  private Runnable after;
  
  public static FeatureDialog top;
  
  public FeatureDialog after(Runnable paramRunnable) {
    this.after = paramRunnable;
    return this;
  }
  
  protected FeatureDialog(String paramString) {
    super(paramString);
    this.body.defaults().growX().padBottom(4.0F);
    this.cont.pane((Element)this.body).grow().maxHeight(Core.graphics.getHeight() * 0.78F).width(Math.min(Core.graphics.getWidth() * 0.96F, 900.0F));
    this.buttons.defaults().size(150.0F, 48.0F).pad(4.0F);
    this.buttons.button(LunariansI18n.get("back"), new Runnable() {
          public void run() {
            FeatureDialog.this.hide();
          }
        });
    shown(new Runnable() {
          public void run() {
            FeatureDialog.top = FeatureDialog.this;
          }
        });
    closeOnBack();
    hidden(new Runnable() {
          public void run() {
            if (FeatureDialog.top == FeatureDialog.this)
              FeatureDialog.top = null; 
            LunariansSettings.save();
            FeatureDialog.this.onClosed();
            Runnable runnable = FeatureDialog.this.after;
            FeatureDialog.this.after = null;
            if (runnable != null)
              runnable.run(); 
          }
        });
  }
  
  protected void onClosed() {}
}
