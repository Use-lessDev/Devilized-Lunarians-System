package lunarians.ui;

import arc.Core;
import arc.Graphics;
import arc.func.Boolf;
import arc.func.Cons;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.ui.dialogs.BaseDialog;

public class UnitPickerDialog extends BaseDialog {
  private final Cons callback;
  
  public UnitPickerDialog(Cons paramCons) {
    this(paramCons, null);
  }
  
  public UnitPickerDialog(Cons paramCons, Boolf paramBoolf) {
    super(str);
    this.callback = paramCons;
    addCloseButton();
    Table table = new Table();
    this();
    Graphics graphics = Core.graphics;
    float f1 = graphics.getWidth();
    float f2 = Scl.scl(96.0F);
    f1 /= f2;
    int i = (int)f1;
    i = Math.min(6, i);
    byte b = 3;
    f2 = 4.2E-45F;
    i = Math.max(b, i);
    Iterator iterator = Vars.content.units().iterator();
    boolean bool = false;
    while (true) {
      boolean bool1;
      while (true) {
        bool1 = iterator.hasNext();
        break;
      } 
      if (!bool1)
        table.row(); 
    } 
  }
}
