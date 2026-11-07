package lunarians.ui;

import arc.func.Cons;
import arc.scene.ui.layout.Table;

public class BuilderDialog extends FeatureDialog {
  private final Runnable closed;
  
  public BuilderDialog(String paramString, Cons<Table> paramCons, Runnable paramRunnable) {
    super(paramString);
    this.closed = paramRunnable;
    paramCons.get(this.body);
  }
  
  protected void onClosed() {
    if (this.closed != null)
      this.closed.run(); 
  }
}
