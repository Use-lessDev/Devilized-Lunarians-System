package lunarians.ui;

import arc.func.Cons;
import arc.scene.ui.layout.Table;

public class BuilderDialog extends FeatureDialog {
  private final Runnable closed;
  
  public BuilderDialog(String paramString, Cons paramCons, Runnable paramRunnable) {
    super(paramString);
    this.closed = paramRunnable;
    Table table = this.body;
    paramCons.get(table);
  }
  
  protected void onClosed() {
    Runnable runnable = this.closed;
    if (runnable != null)
      runnable.run(); 
  }
}
